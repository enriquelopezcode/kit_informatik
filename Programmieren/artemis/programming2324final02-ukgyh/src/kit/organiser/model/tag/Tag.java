package kit.organiser.model.tag;

import kit.organiser.exceptions.InvalidTagValueException;
import kit.organiser.model.document.Document;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents an abstract base for tags that can be associated with documents.
 * Each tag has a unique name and manages a collection of documents organized by their tag values.
 * It ensures that each document is only added once and that all tag values are valid.
 * @author ukgyh
 */
public abstract class Tag {
    private static final String INVALID_VALUE_FORMAT = "Invalid value for tag %s";
    private static final String DUPLICATE_DOCUMENT_FORMAT = "Document %s already defined for tag";
    private static final String UNDEFINED_STRING = "undefined";
    private static final String UNDEFINED_VALUE = null;
    protected final Map<String, List<Document>> documentsByValue;
    protected final String name;
    protected final String firstValue;
    protected final Set<String> definedDocuments;

    /**
     * Constructs a new Tag object with a specified name and first value. Initializes the documents by value map
     * and a set to track defined documents. Validates the first value and throws if it's invalid.
     *
     * @param name The name of the tag.
     * @param firstValue The predefined first value for this tag, which must pass the validation.
     * @throws IllegalArgumentException If the first value is invalid according to {@link #isValidValue(String)}.
     */
    protected Tag(String name, String firstValue) {
        this.name = name;
        this.firstValue = firstValue;
        this.documentsByValue = new LinkedHashMap<>();
        this.definedDocuments = new HashSet<>();
        initializeDocumentsByValue();
    }

    /**
     * Returns the name of the tag.
     * @return name of tag.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Checks if a document by the given name has not been defined/added under this tag.
     *
     * @param documentName The name of the document to check.
     * @return true if the document has not been added, false otherwise.
     */
    public boolean isNewDocument(String documentName) {
        return !definedDocuments.contains(documentName);
    }

    /**
     * Adds a document to this tag under a specific value, if the document hasn't been added before and the value is valid.
     *
     * @param document The document to add.
     * @param value The tag value to associate with the document.
     * @throws InvalidTagValueException If the document has already been defined or the value is invalid.
     */
    public void addDocument(Document document, String value) throws InvalidTagValueException {
        if (isNewDocument(document.getFileName())) {
            if (!isValidValue(value)) {
                throw new InvalidTagValueException(INVALID_VALUE_FORMAT.formatted(name));
            }
            documentsByValue.computeIfAbsent(value, k -> new ArrayList<>()).add(document);
            definedDocuments.add(document.getFileName());
        } else {
            throw new InvalidTagValueException(DUPLICATE_DOCUMENT_FORMAT.formatted(document.getFileName()));
        }
    }

    /**
     * Returns the first value the tag was created with.
     * @return The first value of this tag.
     */
    public String getFirstValue() {
        return firstValue;
    }

    /**
     * Generates a list of string representations for all the values associated with this tag.
     * @return A list of tag value strings.
     */
    public List<String> getValueStrings() {
        List<String> valueStrings = new ArrayList<>();
        for (String value : documentsByValue.keySet()) {
            String valueString = Optional.ofNullable(value).orElse(UNDEFINED_STRING);
            valueStrings.add(valueString);
        }
        return valueStrings;
    }

    /**
     * Filters and returns a list of documents grouped by tag values, but only includes documents whose names
     * are in the specified set of names.
     *
     * @param names The set of document names to include in the result.
     * @return A list of lists, each containing documents that match the provided names and are grouped by tag value.
     */
    public List<List<Document>> filterDocumentsByNames(Set<String> names) {
        List<List<Document>> documentsOrderedByTag = new ArrayList<>();

        for (String tagValue : documentsByValue.keySet()) {
            documentsOrderedByTag.add(new ArrayList<>(documentsByValue.get(tagValue)));
        }

        for (List<Document> documentList : documentsOrderedByTag) {
            documentList.removeIf(document -> !names.contains(document.getFileName()));
        }
        return documentsOrderedByTag;
    }

    /**
     * Populates the undefined value category with documents from the provided list that have not been defined under
     * any value of this tag.
     *
     * @param documents The list of documents to be potentially added under the undefined value.
     */
    public void populateUndefined(List<Document> documents) {
        for (Document document : documents) {
            if (isNewDocument(document.getFileName())) {
                documentsByValue.get(UNDEFINED_VALUE).add(document);
            }
        }
    }

    /**
     * Abstract method that must be implemented to define the criteria for valid tag values.
     *
     * @param value The tag value to validate.
     * @return true if the value is valid according to this tag's criteria, false otherwise.
     */
    public abstract boolean isValidValue(String value);

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Tag other = (Tag) obj;
        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    private void initializeDocumentsByValue() {
        documentsByValue.put(UNDEFINED_VALUE, new ArrayList<>());
    }

}
