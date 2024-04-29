package edu.kit.kastel.tva.bacchus.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kit.kastel.tva.bacchus.net.GenerationRequest;
import edu.kit.kastel.tva.bacchus.net.TrustManagerFactory;
import portunus.util.CharacterSet;
import portunus.util.IPasswordGenerator;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.CharBuffer;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Collection;

/**
 * Implementation of {@link IPasswordGenerator} using a remote
 * server generating the requested passwords.
 *
 * <p>
 *     The implementation contacts a remote server using HTTPS
 *     and sends GenerationRequest objects that are serialized
 *     using the jackson ObjectMapper. The server responds with
 *     the generated passwords.
 * </p>
 * <p>
 *     Any methods of this class may throw a {@link IllegalStateException} if there
 *     is any problem with the connection or execution in the remote entity.
 * </p>
 */
public class RemotePasswordGenerator implements IPasswordGenerator {
    private final HttpClient client;
    private final URI server;
    private final ObjectMapper mapper;
    private final HttpRequest.Builder requestBuilder;

    /**
     * Creates a new {@link RemotePasswordGenerator} connected
     * to the server at the provided {@link URI}.
     *
     * @param server a {@link URI} for the server's root address
     */
    public RemotePasswordGenerator(URI server) {
        // The following call initializes the encryption context
        // for the HTTPS connection.
        // For the interested folks: The functionality is explained
        // in the method called below.
        SSLContext clientContext = createContext();

        // Initialize TLS parameters and enable client auth
        SSLParameters parameters = clientContext.getDefaultSSLParameters();
        parameters.setNeedClientAuth(true);

        // Setup client using the context and parameters above
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .sslContext(clientContext)
                .sslParameters(parameters)
                .build();

        // Store server URI and object mapper for request marshalling
        this.server = server;

        // Initialize object mapper for marshalling
        this.mapper = new ObjectMapper();

        // Initialize request builder for POST requests
        this.requestBuilder = HttpRequest.newBuilder();
    }

    @Override
    public String generatePassword(int length, Collection<CharacterSet> characterSets) {

        // Create a new GenerationRequest object with the provided parameters
        GenerationRequest generationRequest = new GenerationRequest(length, characterSets);

        // Marshal the object to a JSON string
        String requestBody;
        try {
            requestBody = mapper.writeValueAsString(generationRequest);
        } catch (IOException e) {
            throw new IllegalStateException("Could not marshal request object", e);
        }

        // Create a new POST request to the server with the marshalled object
        HttpRequest request = requestBuilder
                .uri(server.resolve("/demo"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Send the request to the server
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException("Could not send request to server", e);
        }
        if (response.statusCode() != 200) {
            // Throw if the connection was successful but returned
            // an error.
            throw new IllegalStateException(String.format(
                    "Server responded with error code %d",
                    response.statusCode()
            ));
        }

        // Return body of response, as the response only contains
        // the generated password.
        return response.body();
    }

    /**
     * This method contains the logic for initializing the
     * client's encryption context.
     *
     * <p>
     *     Do not touch this method unless you are sure
     *     what you are doing. Additionally, only touch it
     *     if you insist to break our test cases.
     * </p>
     *
     * @return an initialized encryption context
     * @throws IllegalStateException if any assumptions fail
     */
    private static SSLContext createContext() {
        try {
            // Initialize encryption context to default context for TLSv1.3.
            SSLContext clientContext = SSLContext.getInstance("TLSv1.3");

            // We use TLS client authentication to ensure that only allowed
            // parties can access our server. Therefore, the client also holds
            // a certificate issued by the CA created for this exam.

            // This CA certificate is the global root of trust in this example.
            // It is stored in a separate key store managed by the class
            // "edu.kit.kastel.tva.bacchus.net.TrustManagerFactory" in the
            // module "bacchus-common".

            // If you want to learn more about the authentication process,
            // see https://en.wikipedia.org/wiki/Transport_Layer_Security#Client-authenticated_TLS_handshake.

            // Read password for the client certificate store from a file.
            // Usually, you would ask the user for their password, but this
            // is easier for this demonstration.
            CharBuffer buffer = CharBuffer.allocate(88);
            new InputStreamReader(
                    RemotePasswordGenerator.class.getResourceAsStream("bacchus_client.pass")
            ).read(buffer);

            // Load the client key store using the password above.
            // It contains the client key used for TLS client authentication.
            KeyStore clientKeyStore = KeyStore.getInstance("pkcs12");
            clientKeyStore.load(
                    RemotePasswordGenerator.class.getResourceAsStream("bacchus_client.p12"),
                    buffer.array()
            );

            // Create a key manager factory that is able to extract the
            // keys from our key store.
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("PKIX");
            kmf.init(clientKeyStore, buffer.array());

            // Initialize the encryption context using the key generators
            // created above, the global trust source (our CA) and a instance
            // of your native secure pseudo-random number generator (PRNG).
            clientContext.init(
                    kmf.getKeyManagers(),
                    TrustManagerFactory.create(),
                    SecureRandom.getInstance("NativePRNG")
            );

            // The initialized context is returned to the logic above
            // to use it as configuration in the server.
            return clientContext;
        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            throw new IllegalStateException("Missing an algorithm or key store implementation", e);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read client certificate store", e);
        } catch (CertificateException e) {
            throw new IllegalStateException("Could not read certificate from key store", e);
        } catch (UnrecoverableKeyException e) {
            throw new IllegalStateException("Client certificate could not be recovered, check password", e);
        } catch (KeyManagementException e) {
            throw new IllegalStateException("Could not initialize SSL context", e);
        }
    }
}
