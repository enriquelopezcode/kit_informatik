package kit.organiser.model.document;

import kit.organiser.exceptions.DocumentCreationException;

/**
 * Factory utility class for creating documents.
 * supported document types: image, text, video, audio, program
 * @author ukgyh
 */
public final class DocumentFactory {
    private static final String INVALID_DOCUMENT_FORMAT = "Invalid document type: %s";
    private static final String IMAGE_DOCUMENT_TYPE = "image";
    private static final String TEXT_DOCUMENT_TYPE = "text";
    private static final String VIDEO_DOCUMENT_TYPE = "video";
    private static final String AUDIO_DOCUMENT_TYPE = "audio";
    private static final String PROGRAM_DOCUMENT_TYPE = "program";
    private static final String INSTANTIATION_ERROR = "Utility class cannot be instantiated";

    private DocumentFactory() {
        throw new UnsupportedOperationException(INSTANTIATION_ERROR);
    }

    /**
     * Creates a document with the specified name, document type and access amount.
     * @param name the name of the document
     * @param documentType the type of the document
     * @param accessAmount the amount of times the document has been accessed
     * @return a new document instance
     * @throws DocumentCreationException if the document type is invalid
     */
    public static Document createDocument(String name, String documentType, int accessAmount) throws DocumentCreationException {
        return switch (documentType) {
            case IMAGE_DOCUMENT_TYPE -> new ImageDocument(name, accessAmount);
            case TEXT_DOCUMENT_TYPE -> new TextDocument(name, accessAmount);
            case VIDEO_DOCUMENT_TYPE -> new VideoDocument(name, accessAmount);
            case AUDIO_DOCUMENT_TYPE -> new AudioDocument(name, accessAmount);
            case PROGRAM_DOCUMENT_TYPE -> new ProgramDocument(name, accessAmount);
            default -> throw new DocumentCreationException(INVALID_DOCUMENT_FORMAT.formatted(documentType));
        };
    }
}
