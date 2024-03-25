package kit.organiser.model.document;

import kit.organiser.model.tag.MultiTag;
import kit.organiser.model.tag.NumericTag;
import kit.organiser.model.tag.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a video document.
 * It extends the {@link Document} class, adding video-specific translations for length and genre tags.
 * @author ukgyh
 */
public class VideoDocument extends Document {
    private static final String INITIAL_LENGTH_TAG_NAME = "length";
    private static final String TRANSLATED_LENGTH_TAG_NAME = "videolength";
    private static final String INITIAL_GENRE_TAG_NAME = "genre";
    private static final String TRANSLATED_GENRE_TAG_NAME = "videogenre";
    private static final String CLIP_VALUE_NAME = "clip";
    private static final String SHORT_VALUE_NAME = "short";
    private static final String MOVIE_VALUE_NAME = "movie";
    private static final String LONG_VALUE_NAME = "long";
    private static final String DUMMY_VALUE = null;
    private static final int CLIP_MAXIMUM_THRESHOLD = 300;
    private static final int SHORT_MAXIMUM_THRESHOLD = 3600;
    private static final int MOVIE_MAXIMUM_THRESHOLD = 7200;
    private final Tag lengthComparisionTag;
    private final Tag genreComparisionTag;

    VideoDocument(String name, int accessAmount) {
        super(name, accessAmount);
        this.lengthComparisionTag = new NumericTag(INITIAL_LENGTH_TAG_NAME, DUMMY_VALUE);
        this.genreComparisionTag = new MultiTag(INITIAL_GENRE_TAG_NAME, DUMMY_VALUE);
    }

    @Override
    public List<Tag> getExtraTags() {
        //no tags to add
        return new ArrayList<>();
    }

    @Override
    public Tag translateTag(Tag tag) {
        if (tag.equals(lengthComparisionTag)) {
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
        if (sizeInt < CLIP_MAXIMUM_THRESHOLD) {
            imageSizeValue = CLIP_VALUE_NAME;
        } else if (sizeInt < SHORT_MAXIMUM_THRESHOLD) {
            imageSizeValue = SHORT_VALUE_NAME;
        } else if (sizeInt < MOVIE_MAXIMUM_THRESHOLD) {
            imageSizeValue = MOVIE_VALUE_NAME;
        } else {
            imageSizeValue = LONG_VALUE_NAME;
        }
        return new MultiTag(TRANSLATED_LENGTH_TAG_NAME, imageSizeValue);
    }

    private Tag translateGenreTag(Tag tag) {
        String genre = tag.getFirstValue();
        return new MultiTag(TRANSLATED_GENRE_TAG_NAME, genre);
    }
}
