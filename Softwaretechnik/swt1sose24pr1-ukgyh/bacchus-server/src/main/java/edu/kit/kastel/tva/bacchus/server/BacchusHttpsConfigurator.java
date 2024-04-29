package edu.kit.kastel.tva.bacchus.server;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import edu.kit.kastel.tva.bacchus.net.TrustManagerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

/**
 * A {@link HttpsConfigurator} for Bacchus' TLS configuration.
 *
 * <p>
 *     Do not touch this method unless you are sure
 *     what you are doing. Additionally, only touch it
 *     if you insist to break our test cases.
 * </p>
 */
public class BacchusHttpsConfigurator extends HttpsConfigurator {
    /**
     * Create a new instance.
     */
    public BacchusHttpsConfigurator() {
        super(createContext());
    }

    private static SSLContext createContext() {
        // See the documentation in the "bacchus-client" module's class
        // "edu.kit.kastel.tva.bacchus.client.RemotePasswordGenerator"
        // if you want to learn more about the following code.

        SSLContext context;

        try {
            context = SSLContext.getInstance("TLSv1.3");

            CharBuffer buffer = CharBuffer.allocate(88);
            new InputStreamReader(
                    BacchusHttpsConfigurator.class.getResourceAsStream("bacchus_server.pass")
            ).read(buffer);

            KeyStore serverKeyStore = KeyStore.getInstance("pkcs12");
            serverKeyStore.load(
                    BacchusHttpsConfigurator.class.getResourceAsStream("bacchus_server.p12"),
                    buffer.array()
            );

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("PKIX");
            kmf.init(serverKeyStore, buffer.array());

            context.init(kmf.getKeyManagers(), TrustManagerFactory.create(), SecureRandom.getInstance("NativePRNG"));
        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            throw new IllegalStateException("Missing an algorithm or key store implementation", e);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read server certificate store", e);
        } catch (CertificateException e) {
            throw new IllegalStateException("Could not read certificate from key store", e);
        } catch (UnrecoverableKeyException e) {
            throw new IllegalStateException("Server certificate could not be recovered, check password", e);
        } catch (KeyManagementException e) {
            throw new IllegalStateException("Could not initialize SSL context", e);
        }

        return context;
    }

    @Override
    public void configure(HttpsParameters params) {
        SSLParameters parameters = getSSLContext().getDefaultSSLParameters();
        parameters.setNeedClientAuth(true);
        params.setSSLParameters(parameters);
    }
}
