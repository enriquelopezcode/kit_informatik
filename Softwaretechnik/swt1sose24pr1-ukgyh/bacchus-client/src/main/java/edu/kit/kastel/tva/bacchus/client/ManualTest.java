package edu.kit.kastel.tva.bacchus.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import edu.kit.kastel.tva.bacchus.server.BacchusHttpsServer;
import portunus.util.CharacterSet;

import java.util.Collection;
import java.util.List;

/**
 * A manual test for the Bacchus client.
 */
public final class ManualTest {
    private static final int NUMBER_OF_PASSWORDS = 3;
    private static final int PASSWORD_LENGTH = 8;
    private static final Collection<CharacterSet> PASSWORD_CHARACTER_SET = List.of(CharacterSet.LOWER_CASE);
    private ManualTest() {
        throw new IllegalStateException("Utility class");
    }
    /**
     * method that starts the server and tests password generations.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BacchusHttpsServer server;
        try {
            System.out.println("Starting server...");
            server = new BacchusHttpsServer(new InetSocketAddress("localhost", 8443));
            server.start();
            System.out.println("Server started.");
        } catch (IOException e) {
            System.out.println("There was an error when starting the server");
            return;
        }

        RemotePasswordGenerator remotePasswordGenerator = new RemotePasswordGenerator(server.getRootUri());

        for (int i = 0; i < NUMBER_OF_PASSWORDS; i++) {
            String password = remotePasswordGenerator.generatePassword(PASSWORD_LENGTH, PASSWORD_CHARACTER_SET);
            System.out.println(password);
        }
    }
}

