package edu.kit.kastel.tva.bacchus.server;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import edu.kit.kastel.tva.bacchus.net.GenerationRequest;
import portunus.passwordgenerator.PasswordGenerator;

import java.io.IOException;
import java.security.SecureRandom;

/**
 * A {@link HttpHandler} for the <tt>/demo</tt> endpoint.
 *
 * <p>
 *     The endpoint accepts the canonical marshalling of a
 *     GenerationRequest provided using a POST
 *     request. It responds with the generated password
 *     and the response code 200 OK.
 * </p>
 * <p>
 *     If this endpoint is requested with another method
 *     as POST, the endpoint responds with 405 Method Not Allowed
 *     and an error message.
 * </p>
 * <p>
 *     If the provided body in a POST request cannot be unmarshalled
 *     due to an illegal format supplied by the client, the
 *     endpoint responds with 400 Bad Request and an error message.
 * </p>
 */
public class DemoEventHandler implements HttpHandler {
    private static final String POST_METHOD_STRING = "POST";
    private static final String INVALID_FORMAT_ERROR_MESSAGE = "unexpected format for request body.";
    private static final String INVALID_METHOD_ERROR_MESSAGE = "Request Method has to be POST.";
    private final PasswordGenerator passwordGenerator;
    private final ObjectMapper mapper;

    /**
     * Create a new instance.
     */
    public DemoEventHandler() {
        this.passwordGenerator = new PasswordGenerator(new SecureRandom());
        this.mapper = new ObjectMapper();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (!POST_METHOD_STRING.equals(requestMethod)) {
            close(exchange, 405, INVALID_METHOD_ERROR_MESSAGE);
            return;

        }
        GenerationRequest passwordRequest;
        try {
            passwordRequest = mapper.readValue(exchange.getRequestBody(), GenerationRequest.class);
        } catch (JsonMappingException e) {
            close(exchange, 400, INVALID_FORMAT_ERROR_MESSAGE);
            return;
        }

        int passwordLength = passwordRequest.length();
        var characterSets = passwordRequest.characterSets();

        String password = passwordGenerator.generatePassword(passwordLength, characterSets);
        close(exchange, 200, password);
    }

    /**
     * Convenience method for closing a {@link HttpExchange}.
     *
     * @param exchange the exchange to close
     * @param responseCode the response code to send
     * @param message the message to send
     * @throws IOException if there is any error while closing the exchange
     */
    private void close(HttpExchange exchange, int responseCode, String message) throws IOException {
        byte[] messageBytes = message.getBytes();
        exchange.sendResponseHeaders(responseCode, messageBytes.length);
        exchange.getResponseBody().write(messageBytes);
        exchange.close();
    }
}
