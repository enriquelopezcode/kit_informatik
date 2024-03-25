package kit.organiser.model.tag;

/**
 * Represents a Numeric Tag.
 * A Numeric Tag is a Tag that can only have a numeric value.
 * @author ukgyh
 */
public class NumericTag extends Tag {
    /**
     * Creates a new Numeric Tag with the given name and value.
     * @param name The name of the Tag.
     * @param value The first value of the Tag.
     */
    public NumericTag(String name, String value) {
        super(name, value);
    }

    @Override
    public boolean isValidValue(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
