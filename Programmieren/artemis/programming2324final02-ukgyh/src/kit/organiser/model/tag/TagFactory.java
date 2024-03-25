package kit.organiser.model.tag;

import kit.organiser.exceptions.TagCreationException;

/**
 * A factory utility class for creating {@link Tag} instances from string inputs.
 * It creates {@link NumericTag}, {@link MultiTag} and {@link BinaryTag} instances based on the input format
 * @author ukgyh
 */
public final class TagFactory {
    private static final String SEPARATOR_SYMBOL = "=";
    private static final String ALPHABETIC_REGEX = "^[A-Za-z][A-Za-z0-9]*";
    private static final String VALUE_REGEX = "[A-Za-z0-9 ]+";
    private static final String NUMERIC_REGEX = "\\d+";
    private static final int TAG_NAME_INDEX = 0;
    private static final int TAG_VALUE_INDEX = 1;
    private static final String INVALID_TAG_FORMAT = "Invalid tag: %s";
    private static final String DEFINED_VALUE = "defined";
    private static final String INSTANTIATION_ERROR = "utility class cannot be instantiated";

    private TagFactory() {
        throw new UnsupportedOperationException(INSTANTIATION_ERROR);
    }

    /**
     * Creates a {@link Tag} object from a string representation of tag information.
     * It supports creating numeric tags, multi-value tags, and binary tags.
     *
     * @param tagInfo The string representation of the tag to be created.
     * @return A new instance of a {@link Tag} subclass based on the input format.
     * @throws TagCreationException if the input format does not match any expected tag pattern.
     */
    public static Tag createTagFromString(String tagInfo) throws TagCreationException {
        String[] tagArguments = tagInfo.split(SEPARATOR_SYMBOL);
        String tagName = tagArguments[TAG_NAME_INDEX];
        tagName = tagName.toLowerCase();

        if (isNumericTag(tagInfo)) {
            String tagValue = tagArguments[TAG_VALUE_INDEX];
            try {
                Integer.parseInt(tagValue);
            } catch (NumberFormatException e) {
                throw new TagCreationException(INVALID_TAG_FORMAT.formatted(tagInfo));
            }
            return new NumericTag(tagName, tagValue);

        } else if (isMultiValueTag(tagInfo)) {
            String tagValue = tagArguments[TAG_VALUE_INDEX];
            return new MultiTag(tagName, tagValue);

        } else if (isBinaryValueTag(tagInfo)) {
            return new BinaryTag(tagName, DEFINED_VALUE);

        } else {
            throw new TagCreationException(INVALID_TAG_FORMAT.formatted(tagInfo));
        }
    }

    private static boolean isNumericTag(String tag) {
        return tag.matches(ALPHABETIC_REGEX + SEPARATOR_SYMBOL + NUMERIC_REGEX);
    }

    private static boolean isMultiValueTag(String tag) {
        return tag.matches(ALPHABETIC_REGEX + SEPARATOR_SYMBOL + VALUE_REGEX);
    }

    private static boolean isBinaryValueTag(String tag) {
        return tag.matches(ALPHABETIC_REGEX);
    }
}
