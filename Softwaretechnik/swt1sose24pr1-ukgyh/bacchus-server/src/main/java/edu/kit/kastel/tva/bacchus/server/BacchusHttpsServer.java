package edu.kit.kastel.tva.bacchus.server;

import com.sun.net.httpserver.HttpsServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;


/**
 * Bacchus' HTTPS server implementation.
 *
 * <p>
 *     This server features the following endpoints:
 *     <ul>
 *         <li><tt>/demo</tt>: Creation of passwords using a GenerationRequest</li>
 *     </ul>
 * </p>
 */
public class BacchusHttpsServer {
    private final HttpsServer httpsServer;

    /**
     * Create a new instance of this server that binds to the provided address.
     *
     * @param bindAddress the local address to bind the server to
     * @throws IOException thrown if such an exception occurs during initialization
     */
    public BacchusHttpsServer(InetSocketAddress bindAddress) throws IOException {
        this.httpsServer = HttpsServer.create(bindAddress, 0);
        this.httpsServer.setHttpsConfigurator(new BacchusHttpsConfigurator());
        this.httpsServer.createContext("/demo", new DemoEventHandler());
    }

    /**
     * Starts this server in a new background thread.
     */
    public void start() {
        this.httpsServer.start();
    }

    /**
     * Stops this server by disallowing any new connections
     * to the server. If the server is not closed after the
     * provided number of seconds, all open connections are
     * interrupted.
     *
     * @param delay the number of seconds to wait until
     *              interrupting all remaining connections
     */
    public void stop(int delay) {
        this.httpsServer.stop(delay);
    }

    /**
     * Returns the root URI of this server.
     *
     * @return the URI representing the root address of the server
     */
    public URI getRootUri() {
        InetSocketAddress address = this.httpsServer.getAddress();
        return URI.create("https://" + address.getHostName() + ":" + address.getPort());
    }
}
