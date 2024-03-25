package kit.organiser.model.tag;

/**
 * Represents information about the organizational efficiency of a Tag.
 * This record encapsulates a Tag object along with  the efficiency of the tag when organizing documents with it.
 * @param tag The Tag object.
 * @param tagEfficiency The efficiency of the tag when organizing documents with it.
 * @author ukgyh
 */
public record TagEfficiencyInfo(Tag tag, double tagEfficiency) {
    private static final String NAME_EFFICIENCY_SEPARATOR = "=";
    private static final String ROUNDING_STRING_FORMAT = "%.2f";

    /**
     * Returns the name of the Tag.
     * @return name of the Tag.
     */
    public String getTagName() {
        return tag.getName();
    }

    /**
     * Returns a string representation of this TagEfficiencyInfo.
     * The string consists of the tag's name and the efficiency value rounded to two decimal places.
     *
     * @return A String representing the tag name and its efficiency, separated by {@code NAME_EFFICIENCY_SEPARATOR}.
     */
    public String toString() {
        return tag.getName() + NAME_EFFICIENCY_SEPARATOR + String.format(ROUNDING_STRING_FORMAT, tagEfficiency);
    }
}
