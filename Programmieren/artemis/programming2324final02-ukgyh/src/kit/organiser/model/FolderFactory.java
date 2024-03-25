package kit.organiser.model;

import kit.organiser.exceptions.FolderCreationException;
import kit.organiser.exceptions.DocumentCreationException;
import kit.organiser.exceptions.InvalidTagValueException;
import kit.organiser.exceptions.TagCreationException;
import kit.organiser.model.document.Document;
import kit.organiser.model.document.DocumentFactory;
import kit.organiser.model.tag.Tag;
import kit.organiser.model.tag.TagFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class responsible for creating folders from document arguments.
 * @author ukgyh
 */
public final class FolderFactory {
    private static final int DOCUMENT_NAME_INDEX = 0;
    private static final int DOCUMENT_TYPE_INDEX = 1;
    private static final int DOCUMENT_ACCESS_AMOUNT_INDEX = 2;
    private static final int TAGS_START_INDEX = 3;
    private static final int MINIMUM_ACCESS_AMOUNT = 0;
    private static final int MINIMUM_ARGUMENT_AMOUNT = 3;
    private static final String INVALID_ACCESS_AMOUNT_FORMAT = "invalid access amount: %s";
    private static final String INVALID_ACCESS_AMOUNT_SUM_FORMAT = "the sum of access amounts of documents must be at least %d";
    private static final String DUPLICATE_TAG_FORMAT = "tags with the same name but different types not allowed: %s";
    private static final String INSTANTIATION_ERROR = "utility class cannot be instantiated";
    private static final String FORBIDDEN_DOCUMENT_SYMBOL = " ";
    private static final String INVALID_DOCUMENT_FORMAT = "invalid document: %s";
    private static final String INVALID_DOCUMENT_NAME_FORMAT = "document name cannot contain the symbol: %s";

    private FolderFactory() {
        throw new UnsupportedOperationException(INSTANTIATION_ERROR);
    }

    /**
     * Creates a {@link Folder} instance with specified documents based on the provided arguments.
     *
     * @param documentsArguments A list of document arguments, where each is an array representing the document's properties and tags.
     * @return A new {@link Folder} instance populated with documents and tags as specified in {@code documentsArguments}.
     * @throws FolderCreationException If any validation fails or document/tag creation encounters errors.
     */
    public static Folder createFolder(List<String[]> documentsArguments) throws FolderCreationException {
        Map<String, Tag> existingTags = new HashMap<>();
        List<Document> documents = new ArrayList<>();
        int accessAmountSum = 0;

        for (String[] documentArguments : documentsArguments) {
            validateDocumentArguments(documentArguments);
            Document document = createDocument(documentArguments);

            documents.add(document);
            accessAmountSum += document.getAccessAmount();

            String[] tagArguments = Arrays.copyOfRange(documentArguments, TAGS_START_INDEX, documentArguments.length);

            for (String tagArgument : tagArguments) {
                Tag tagToAdd = createTag(tagArgument, document);
                addTag(existingTags, tagToAdd, document);
            }

            List<Tag> extraTags = document.getExtraTags();
            for (Tag extraTag : extraTags) {
                addTag(existingTags, extraTag, document);
            }
        }

        List<Tag> tagList = new ArrayList<>(existingTags.values());

        //add all documents not defined for the tag to the undefined value
        for (Tag tag : tagList) {
            tag.populateUndefined(documents);
        }

        if (accessAmountSum <= MINIMUM_ACCESS_AMOUNT) {
            throw new FolderCreationException(INVALID_ACCESS_AMOUNT_SUM_FORMAT.formatted(MINIMUM_ACCESS_AMOUNT));
        }
        return new Folder(documents, tagList);
    }

    private static void validateDocumentArguments(String[] documentArguments) throws FolderCreationException {
        if (documentArguments.length < MINIMUM_ARGUMENT_AMOUNT) {
            throw new FolderCreationException(INVALID_DOCUMENT_FORMAT.formatted(Arrays.toString(documentArguments)));
        }
        String documentName = documentArguments[DOCUMENT_NAME_INDEX];
        if (documentName.contains(FORBIDDEN_DOCUMENT_SYMBOL)) {
            throw new FolderCreationException(INVALID_DOCUMENT_NAME_FORMAT.formatted(FORBIDDEN_DOCUMENT_SYMBOL));
        }

    }

    private static Document createDocument(String[] documentArguments) throws FolderCreationException {
        String documentName = documentArguments[DOCUMENT_NAME_INDEX];
        String documentType = documentArguments[DOCUMENT_TYPE_INDEX];
        String documentAccessAmountString = documentArguments[DOCUMENT_ACCESS_AMOUNT_INDEX];

        int documentAccessAmount;
        try {
            documentAccessAmount = Integer.parseInt(documentAccessAmountString);
        } catch (NumberFormatException e) {
            throw new FolderCreationException(INVALID_ACCESS_AMOUNT_FORMAT.formatted(documentAccessAmountString));
        }

        if (documentAccessAmount < MINIMUM_ACCESS_AMOUNT) {
            throw new FolderCreationException(INVALID_ACCESS_AMOUNT_FORMAT.formatted(documentAccessAmountString));
        }

        try {
            return DocumentFactory.createDocument(documentName, documentType, documentAccessAmount);
        } catch (DocumentCreationException e) {
            throw new FolderCreationException(e.getMessage());
        }
    }

    private static Tag createTag(String tagArgument, Document document) throws FolderCreationException {
        Tag tag;
        try {
            tag = TagFactory.createTagFromString(tagArgument);
        } catch (TagCreationException e) {
            throw new FolderCreationException(e.getMessage());
        }
        //tag is translated to the document's specific tags
        return document.translateTag(tag);
    }

    private static void addTag(Map<String, Tag> existingTags, Tag tag, Document document) throws FolderCreationException {
        if (existingTags.containsKey(tag.getName())) {
            Tag duplicateTag = existingTags.get(tag.getName());

            //if tag already exists, add document to it instead
            if (tag.equals(duplicateTag)) {
                try {
                    duplicateTag.addDocument(document, tag.getFirstValue());
                } catch (InvalidTagValueException e) {
                    throw new FolderCreationException(e.getMessage());
                }
            } else {
                //tags with the same name but different types are not allowed
                throw new FolderCreationException(DUPLICATE_TAG_FORMAT.formatted(tag.getName()));
            }
        } else {
            //new tag, add it to the existing tags
            try {
                tag.addDocument(document, tag.getFirstValue());
            } catch (InvalidTagValueException e) {
                throw new FolderCreationException(e.getMessage());
            }
            existingTags.put(tag.getName(), tag);
        }
    }
}
