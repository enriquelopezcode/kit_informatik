package kit.organiser.model.document;

import kit.organiser.model.tag.Tag;

import java.util.List;

/**
 * Represents a document.
 * @author ukgyh
 */
public abstract class Document {
    private final String fileName;
    private int accessAmount;

    /**
     * Creates a new document.
     * @param fileName the name of the document
     * @param accessAmount the amount of times the document has been accessed
     */
    protected Document(String fileName, int accessAmount) {
        this.accessAmount = accessAmount;
        this.fileName = fileName;
    }

    /**
     * Returns the name of the document.
     * @return the name of the document
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * Returns the amount of times the document has been accessed.
     * @return the amount of times the document has been accessed
     */
    public int getAccessAmount() {
        return accessAmount;
    }

    /**
     * Sets the amount of times the document has been accessed.
     * @param accessAmount the amount of times the document has been accessed
     */
    public void setAccessAmount(int accessAmount) {
        this.accessAmount = accessAmount;
    }

    /**
     * Translates a tag to a document specific tag.
     * @param tag the tag to translate
     * @return document specific tag
     */
    public abstract Tag translateTag(Tag tag);

    /**
     * returns a list of document specific tags that should be added.
     * @return list of tags to add to the document
     */
    public abstract List<Tag> getExtraTags();
}
