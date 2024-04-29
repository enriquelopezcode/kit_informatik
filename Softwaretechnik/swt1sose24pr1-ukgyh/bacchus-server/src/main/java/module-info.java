/**
 * Bacchus' module for the HTTPS server implementation.
 */
module edu.kit.kastel.tva.bacchus.server {
    requires edu.kit.kastel.tva.bacchus.common;
    requires jdk.httpserver;
    requires com.fasterxml.jackson.databind;

    exports edu.kit.kastel.tva.bacchus.server;
    exports portunus.passwordgenerator;
}
