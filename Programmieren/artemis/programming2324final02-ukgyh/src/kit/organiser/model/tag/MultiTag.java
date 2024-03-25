package kit.organiser.model.tag;

/**
 * Represents a MultiTag.
 * A MultiTag is a Tag consisting of alphanumerical values.
 * MultiTag values always start with a letter.
 * @author ukgyh
 */
public class MultiTag extends Tag {
    private static final String VALUE_PATTERN = "^[a-zA-Z][a-zA-Z0-9 ]*$";

    /**
     * Constructs a MultiTag with the given name and value.
     * @param name the name of the tag
     * @param value the first value of the tag
     */
    public MultiTag(String name, String value) {
        super(name, value);
    }

    @Override
    public boolean isValidValue(String value) {
        return value.matches(VALUE_PATTERN);
    }
}
