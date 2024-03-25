package kit.organiser.model.document;

import kit.organiser.model.tag.MultiTag;
import kit.organiser.model.tag.NumericTag;
import kit.organiser.model.tag.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an image document.
 * Extends the {@link Document} class by adding translation capabilities for size tag.
 * @author ukgyh
 */
public class ImageDocument extends Document {
    private static final String INITIAL_TAG_NAME = "size";
    private static final String TRANSLATED_TAG_NAME = "imagesize";
    private static final String ICON_VALUE_NAME = "icon";
    private static final String SMALL_VALUE_NAME = "small";
    private static final String MEDIUM_VALUE_NAME = "medium";
    private static final String LARGE_VALUE_NAME = "large";
    private static final String DUMMY_VALUE = null;
    private static final int ICON_MAXIMUM_THRESHOLD = 10000;
    private static final int SMALL_MAXIMUM_THRESHOLD = 40000;
    private static final int MEDIUM_MAXIMUM_THRESHOLD = 800000;
    private final Tag sizeComparisionTag;

    ImageDocument(String name, int accessAmount) {
        super(name, accessAmount);
        this.sizeComparisionTag = new NumericTag(INITIAL_TAG_NAME, DUMMY_VALUE);
    }

    @Override
    public List<Tag> getExtraTags() {
        //no tags to add
        return new ArrayList<>();
    }

    @Override
    public Tag translateTag(Tag tag) {
        if (tag.equals(sizeComparisionTag)) {
            return translateSizeTag(tag);
        }
        //if no translation needed just return the original tag
        return tag;
    }

    private Tag translateSizeTag(Tag tag) {
        String size = tag.getFirstValue();
        int sizeInt = Integer.parseInt(size);
        String imageSizeValue;

        if (sizeInt < ICON_MAXIMUM_THRESHOLD) {
            imageSizeValue = ICON_VALUE_NAME;
        } else if (sizeInt < SMALL_MAXIMUM_THRESHOLD) {
            imageSizeValue = SMALL_VALUE_NAME;
        } else if (sizeInt < MEDIUM_MAXIMUM_THRESHOLD) {
            imageSizeValue = MEDIUM_VALUE_NAME;
        } else {
            imageSizeValue = LARGE_VALUE_NAME;
        }
        return new MultiTag(TRANSLATED_TAG_NAME, imageSizeValue);
    }
}
