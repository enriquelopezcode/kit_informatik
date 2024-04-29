package edu.kit.kastel.tva.bacchus.net;

import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * The trust manager factory providing the CA for this demonstration.
 *
 * <p>
 *     The trust managers provided here are Bacchus' global root of trust.
 *     Please do not modify this class unless you want our test cases to fail.
 * </p>
 */
public final class TrustManagerFactory {
    private TrustManagerFactory() {
        throw new IllegalStateException("utility-class constructor");
    }

    /**
     * Creates the trust managers containing Bacchus' CA.
     *
     * @return Bacchus' global trust managers
     * @throws IllegalStateException if any assumptions fail
     */
    public static TrustManager[] create() {
        try {
            CharBuffer buffer = CharBuffer.allocate(88);
            new InputStreamReader(
                    TrustManagerFactory.class.getResourceAsStream("bacchus_trust.pass")
            ).read(buffer);

            KeyStore trustKeyStore = KeyStore.getInstance("jks");
            trustKeyStore.load(TrustManagerFactory.class.getResourceAsStream("bacchus_trust.jks"),
                    buffer.array());

            javax.net.ssl.TrustManagerFactory tmf = javax.net.ssl.TrustManagerFactory.getInstance("PKIX");
            tmf.init(trustKeyStore);

            return tmf.getTrustManagers();
        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            throw new IllegalStateException("Missing an algorithm or key store implementation", e);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read trust certificate store", e);
        } catch (CertificateException e) {
            throw new IllegalStateException("Could not read certificate from key store", e);
        }
    }
}
