package kit.organiser.model;

import kit.organiser.exceptions.AccessAmountChangeException;
import kit.organiser.exceptions.MissingDocumentException;
import kit.organiser.exceptions.MissingFolderException;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages folders with unique IDs and supports organizing operations on them.
 * @author ukgyh
 */
public class FileManager {
    private static final String MISSING_FOLDER_MESSAGE = "folder with specified id does not exist";
    private static final String INVALID_ACCESS_AMOUNT_FORMAT = "Access amount must be bigger than %d";
    private static final int MINIMUM_ACCESS_AMOUNT = 0;
    private final Map<Integer, Folder> folders;
    private int nextId;

    /**
     * Initializes a new FileManager instance.
     */
    public FileManager() {
        this.folders = new HashMap<>();
        this.nextId = 0;
    }

    /**
     * Adds a new folder and assigns it a unique ID.
     *
     * @param folder The folder to add.
     * @return The unique ID assigned to the folder.
     */
    public int addFolder(Folder folder) {
        this.folders.put(this.nextId, folder);
        return this.nextId++;
    }

    /**
     * Runs the organiser for a specified folder ID.
     *
     * @param id The ID of the folder to organise.
     * @return The resulting information about the organisation process.
     * @throws MissingFolderException If the folder with the specified ID does not exist.
     */
    public String runOrganiser(int id) throws MissingFolderException {
        if (!this.folders.containsKey(id)) {
            throw new MissingFolderException(MISSING_FOLDER_MESSAGE);
        }
        Folder folder = this.folders.get(id);
        return folder.organise();
    }

    /**
     * Changes the access amount of a document in a folder.
     *
     * @param folderId The ID of the folder containing the document.
     * @param documentName The name of the document.
     * @param newAccessAmount The new access amount to set.
     * @return oldAccessAmount The old access amount of the document.
     * @throws AccessAmountChangeException if there is an issue changing the access amount.
     */
    public int changeDocumentAccessAmount(int folderId, String documentName, int newAccessAmount) throws AccessAmountChangeException {
        if (!this.folders.containsKey(folderId)) {
            throw new AccessAmountChangeException(MISSING_FOLDER_MESSAGE);
        }

        if (newAccessAmount < MINIMUM_ACCESS_AMOUNT) {
            throw new AccessAmountChangeException(INVALID_ACCESS_AMOUNT_FORMAT.formatted(MINIMUM_ACCESS_AMOUNT));
        }

        Folder folder = this.folders.get(folderId);
        try {
            return folder.changeDocumentAccessAmount(documentName, newAccessAmount);
        } catch (MissingDocumentException e) {
            throw new AccessAmountChangeException(e.getMessage());
        }
    }
}
