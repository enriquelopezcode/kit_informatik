package kit.organiser.model;

import kit.organiser.exceptions.MissingDocumentException;
import kit.organiser.model.directory.Directory;
import kit.organiser.model.document.Document;
import kit.organiser.model.tag.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Represents a folder that contains directories.
 * @author ukgyh
 */
public class Folder {
    private static final String DIRECTORY_STARTING_PATH = "";
    private static final String HEAD_DIRECTORY_NAME = "";
    private static final String EFFICIENCY_FILENAMES_SEPARATOR = "---";
    private static final String MISSING_DOCUMENT_FORMAT = "Document not found: %s";
    private final List<Document> documents;
    private final List<Tag> tags;
    private Directory headDirectory;
    private boolean isOrganized;

    /**
     * Constructs a Folder with specified documents and tags.
     * @param documents A list of documents to be included in the folder.
     * @param tags A list of tags associated with the documents.
     */
    public Folder(List<Document> documents, List<Tag> tags) {
        this.documents = documents;
        this.tags = tags;
        createNewHeadDirectory();
    }

    /**
     * Organizes the folder's documents and updates its state to ordered.
     * @return A string containing information about organisation efficiency and document paths.
     */
    public String organise() {

        if (isOrganized) {
            createNewHeadDirectory();
        }

        List<String> orgInformation = headDirectory.organise();
        List<String> filePaths = headDirectory.getFilePaths();

        StringJoiner infoJoiner = new StringJoiner(System.lineSeparator());

        for (String info : orgInformation) {
            infoJoiner.add(info);
        }

        infoJoiner.add(EFFICIENCY_FILENAMES_SEPARATOR);

        for (String fileName : filePaths) {
            infoJoiner.add(fileName);
        }

        isOrganized = true;
        return infoJoiner.toString();
    }

    /**
     * Changes the access amount of a document in the folder.
     * @param documentName The name of the document.
     * @param newAccessAmount The new access amount to set.
     * @return The old access amount of the document.
     * @throws MissingDocumentException If the document with the specified name does not exist.
     */
    public int changeDocumentAccessAmount(String documentName, int newAccessAmount) throws MissingDocumentException {
        for (Document document : documents) {
            if (document.getFileName().equals(documentName)) {
                int oldAccessAmount = document.getAccessAmount();
                document.setAccessAmount(newAccessAmount);
                return oldAccessAmount;
            }
        }
        throw new MissingDocumentException(MISSING_DOCUMENT_FORMAT.formatted(documentName));
    }

    private void createNewHeadDirectory() {
        List<Document> copiedDocuments = new ArrayList<>(documents);
        List<Document> allDocuments = new ArrayList<>(documents);
        List<Tag> copiedTags = new ArrayList<>(tags);
        headDirectory = new Directory(DIRECTORY_STARTING_PATH, HEAD_DIRECTORY_NAME, copiedDocuments, allDocuments, copiedTags);
        isOrganized = false;
    }
}
