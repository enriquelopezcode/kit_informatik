package kit.organiser.model.tag;

/**
 * Represents a binary tag.
 * The binary tag can only have one of two values: "defined" or "undefined".
 * @author ukgyh
 */
public class BinaryTag extends Tag {
    private static final String DEFINED_VALUE = "defined";

    /**
     * Constructs a binary tag with the given name and value.
     * @param name the name of the tag
     * @param value the first value of the tag
     */
    public BinaryTag(String name, String value) {
        super(name, value);
    }

    @Override
    public boolean isValidValue(String value) {
        //if a document is added through this method it is defined
        return value.equals(DEFINED_VALUE);
    }
}
