/**
 * Bacchus' module for the HTTPS client implementation.
 */
module edu.kit.kastel.tva.bacchus.client {
    requires java.net.http;
    requires edu.kit.kastel.tva.bacchus.common;
    requires edu.kit.kastel.tva.bacchus.server;
    requires com.fasterxml.jackson.databind;

    exports edu.kit.kastel.tva.bacchus.client;
}
