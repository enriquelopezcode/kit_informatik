package kit.organiser.model.tag;

import java.util.Comparator;

/**
 * Compares two TagEfficiencyInfo objects based on their organizational efficiency and tag names.
 * It prioritizes higher efficiency tags; if efficiencies are equal, it compares alphabetically by tag names.
 * This comparator is intended for sorting lists of TagEfficiencyInfo objects in a descending efficiency order.
 * @author ukgyh
 */
public class TagEfficiencyComparator implements Comparator<TagEfficiencyInfo> {
    @Override
    public int compare(TagEfficiencyInfo o1, TagEfficiencyInfo o2) {
        int efficiencyComparison = Double.compare(o2.tagEfficiency(), o1.tagEfficiency());
        if (efficiencyComparison == 0) {
            // tagEfficiency values are equal, compare tag names
            return o1.getTagName().compareTo(o2.getTagName());
        } else {
            return efficiencyComparison;
        }
    }
}
