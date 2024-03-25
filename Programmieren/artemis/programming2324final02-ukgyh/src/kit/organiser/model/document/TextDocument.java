package kit.organiser.model.document;

import kit.organiser.model.tag.MultiTag;
import kit.organiser.model.tag.NumericTag;
import kit.organiser.model.tag.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a text document.
 * It extends the {@link Document} class, adding text-specific translations for length and genre tags.
 * @author ukgyh
 */
public class TextDocument extends Document {
    private static final String INITIAL_WORD_TAG_NAME = "words";
    private static final String TRANSLATED_WORDS_TAG_NAME = "textlength";
    private static final String INITIAL_GENRE_TAG_NAME = "genre";
    private static final String TRANSLATED_GENRE_TAG_NAME = "textgenre";
    private static final String SHORT_VALUE_NAME = "short";
    private static final String MEDIUM_VALUE_NAME = "medium";
    private static final String LONG_VALUE_NAME = "long";
    private static final String DUMMY_VALUE = null;
    private static final int SHORT_MAXIMUM_THRESHOLD = 100;
    private static final int MEDIUM_MAXIMUM_THRESHOLD = 1000;
    private final Tag wordsComparisionTag;
    private final Tag genreComparisionTag;
    TextDocument(String name, int accessAmount) {
        super(name, accessAmount);
        this.wordsComparisionTag = new NumericTag(INITIAL_WORD_TAG_NAME, DUMMY_VALUE);
        this.genreComparisionTag = new MultiTag(INITIAL_GENRE_TAG_NAME, DUMMY_VALUE);
    }

    @Override
    public List<Tag> getExtraTags() {
        //no tags to add
        return new ArrayList<>();
    }

    @Override
    public Tag translateTag(Tag tag) {
        if (tag.equals(wordsComparisionTag)) {
            return translateLengthTag(tag);
        } else if (tag.equals(genreComparisionTag)) {
            return translateGenreTag(tag);
        }
        //if no translation needed just return the original tag
        return tag;
    }

    private Tag translateLengthTag(Tag tag) {

        String size = tag.getFirstValue();
        int sizeInt = Integer.parseInt(size);

        String imageSizeValue;
        if (sizeInt < SHORT_MAXIMUM_THRESHOLD) {
            imageSizeValue = SHORT_VALUE_NAME;
        } else if (sizeInt < MEDIUM_MAXIMUM_THRESHOLD) {
            imageSizeValue = MEDIUM_VALUE_NAME;
        } else {
            imageSizeValue = LONG_VALUE_NAME;
        }
        return new MultiTag(TRANSLATED_WORDS_TAG_NAME, imageSizeValue);
    }

    private Tag translateGenreTag(Tag tag) {
        String genre = tag.getFirstValue();
        return new MultiTag(TRANSLATED_GENRE_TAG_NAME, genre);
    }
}
