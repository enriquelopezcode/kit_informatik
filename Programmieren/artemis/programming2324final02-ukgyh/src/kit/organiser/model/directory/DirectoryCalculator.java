package kit.organiser.model.directory;

import kit.organiser.model.document.Document;

import java.util.List;

/**
 * The {@code DirectoryCalculator} class provides static methods for calculating the efficiency of tag-based
 * document organization within a directory structure. This class is part of a document organization system
 * designed to automate the structuring of documents on a server by dynamically creating folders based on
 * predefined tags.
 * @author ukgyh
 **/
public final class DirectoryCalculator {
    private static final String INSTANTIATION_ERROR = "This class cannot be instantiated";
    private DirectoryCalculator() {
        throw new UnsupportedOperationException(INSTANTIATION_ERROR);
    }

    /**
     * Calculates the efficiency of tag-based document organization within a directory structure.
     * Uncertainty is measured in terms of the information-theoretic entropy, representing the average amount of
     * information required to identify a document within a set.
     * The efficiency is determined by the difference between the uncertainty of the documents and the uncertainty
     * of the tags used to organize the documents.
     * @param tagValues A list of lists of documents, where each list represents a subset of documents with the same tag.
     * @param documents A list of all documents in the directory.
     * @return The efficiency of the tag-based document organization.
     */
    public static double calculateTagEfficiency(List<List<Document>> tagValues, List<Document> documents) {
        return calculateUncertainty(documents) - calculateTagUncertainty(tagValues, documents);
    }
    private static double calculateTagUncertainty(List<List<Document>> tagValues, List<Document> documents) {
        double tagUncertainty = 0;
        for (List<Document> tagDocuments : tagValues) {
            tagUncertainty += calculateSubsetProbability(tagDocuments, documents) * calculateUncertainty(tagDocuments);
        }
        return tagUncertainty;
    }

    private static double calculateUncertainty(List<Document> documents) {
        double uncertainty = 0;
        for (Document document : documents) {
            double logUncertainty = Math.log(calculateAccessProbability(document, documents)) / Math.log(2);
            uncertainty -= calculateAccessProbability(document, documents) * logUncertainty;
        }
        return uncertainty;
    }


    /**
     * Calculates the probability of selecting a document from a specific subset within the total collection
     * of documents. This probability represents the likelihood of a document being in the subset based on
     * its access frequency relative to the access frequencies of all documents in the directory.
     *
     * @param subset A list of documents representing a specific subset for which the probability is to be calculated.
     *               Each document in the subset contributes to the total probability based on its access frequency.
     * @param documents The complete list of documents in the directory, used as the denominator in calculating
     *                  the probability of each document's access frequency relative to the total access frequencies.
     * @return The total probability of accessing a document from the specified subset, calculated as the sum
     *         of individual document probabilities within the subset. This value helps in assessing the relative
     *         significance of different subsets for document retrieval.
     */
    public static double calculateSubsetProbability(List<Document> subset, List<Document> documents) {
        double subsetProbability = 0;
        for (Document document : subset) {
            subsetProbability += calculateAccessProbability(document, documents);
        }
        return subsetProbability;
    }

    private static double calculateTotalAccessAmount(List<Document> documents) {
        double accessAmountSum = 0;
        for (Document document : documents) {
            accessAmountSum += document.getAccessAmount();
        }
        return accessAmountSum;
    }

    private static double calculateAccessProbability(Document document, List<Document> documents) {
        double accessAmountSum = calculateTotalAccessAmount(documents);
        double accessAmount = document.getAccessAmount();

        if (accessAmountSum > 0) {
            return (accessAmount / accessAmountSum);
        } else {
            return 0;
        }
    }
}
