package kit.organiser.model.directory;

import java.util.Comparator;

/**
 * Comparator for {@link Directory} objects, ordering them in descending order by their uncertainty values.
 * Higher uncertainty values are prioritized over lower ones, facilitating sorting where more uncertain directories appear first.
 * @author ukgyh
 */
public class DirectoryComparator implements Comparator<Directory> {
    private static final int EQUAL_UNCERTAINTY_COMPARISON = 0;
    @Override
    public int compare(Directory di1, Directory di2) {
        // First compare by uncertainty
        int uncertaintyComparison = Double.compare(di2.getUncertainty(), di1.getUncertainty());
        if (uncertaintyComparison != EQUAL_UNCERTAINTY_COMPARISON) {
            return uncertaintyComparison;
        }

        // If uncertainties are equal, compare alphanumerically by name
        return di1.getName().compareToIgnoreCase(di2.getName());
    }
}
