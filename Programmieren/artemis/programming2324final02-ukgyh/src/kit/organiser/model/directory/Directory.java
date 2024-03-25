package kit.organiser.model.directory;

import kit.organiser.model.document.Document;
import kit.organiser.model.tag.Tag;
import kit.organiser.model.tag.TagEfficiencyComparator;
import kit.organiser.model.tag.TagEfficiencyInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a directory in a file system, which is part of an automated system for organizing documents based on tags.
 * The Directory class is responsible for organizing documents recursively into a tree-like structure based on the efficiency of tags,
 * which are keywords or labels assigned to documents. It also supports the generation of detailed paths for each document
 * based on the organized structure.
 * @author ukgyh
 **/
public class Directory {
    private static final String DIRECTORY_SEPARATION_PATH = "/";
    private static final String TAG_VALUE_NAME_SEPARATOR = "=";
    private static final char FILE_NAME_WRAPPER = '"';
    private static final int EFFICIENT_TAG_POSITION = 0;
    private static final double MINIMUM_TAG_EFFICIENCY = 0.003;
    private final String name;
    private final String path;
    private final List<Directory> children;
    private final List<Document> documents;
    private final List<Document> allDocuments;
    private final Set<String> documentNames;
    private final List<Tag> tags;
    private final double uncertainty;

    /**
     * Constructs a new Directory instance with specified path, name, documents, and tags.
     *
     * @param path The path of the directory within the file system.
     * @param name The name of the directory.
     * @param documents A list of documents contained in the directory.
     * @param allDocuments A list of all documents in the folder.
     * @param tags A list of tags associated with the documents in the directory.
     */
    public Directory(String path, String name, List<Document> documents, List<Document> allDocuments, List<Tag> tags) {
        this.name = name;
        this.path = path;
        this.children = new ArrayList<>();
        this.documents = new ArrayList<>(documents);
        this.allDocuments = new ArrayList<>(allDocuments);
        this.uncertainty = DirectoryCalculator.calculateSubsetProbability(documents, allDocuments);
        this.documentNames = documents.stream().map(Document::getFileName).collect(Collectors.toSet());
        this.tags = new ArrayList<>(tags);
    }

    /**
     * Retrieves the current uncertainty of the directory. The uncertainty is a measure of how predictably
     * a document can be found within this directory, based on the distribution and access probabilities
     * of the documents it contains.
     * @return The uncertainty of the directory.
     **/
    public double getUncertainty() {
        return uncertainty;
    }

    /**
     * Organizes documents within the directory based on the efficiency of tags. It sorts tags by their efficiency
     * and restructures the directory to prioritize the organization of documents by the most efficient tag. Documents
     * are then grouped into new child directories according to the tag values.
     *
     * @return A list of strings representing the organization information of documents, including the path and name
     *         of directories and the efficiency of used tags.
     */
    public List<String> organise() {
        List<TagEfficiencyInfo> tagEfficiencyInfos = new ArrayList<>();
        List<String> orgInformation = new ArrayList<>();

        for (Tag tag : tags) {
            List<List<Document>> orderedDocuments = tag.filterDocumentsByNames(documentNames);
            double tagEfficiency = DirectoryCalculator.calculateTagEfficiency(orderedDocuments, this.documents);
            TagEfficiencyInfo tagEfficiencyInfo = new TagEfficiencyInfo(tag, tagEfficiency);
            tagEfficiencyInfos.add(tagEfficiencyInfo);
        }

        TagEfficiencyComparator comparator = new TagEfficiencyComparator();

        // Sort the list descending by tag efficiency
        tagEfficiencyInfos.sort(comparator);

        for (TagEfficiencyInfo tagEfficiencyInfo : tagEfficiencyInfos) {
            String orgInfoString = this.path + this.name + DIRECTORY_SEPARATION_PATH + tagEfficiencyInfo.toString();

            //info is only added if minimum efficiency was surpassed
            if (tagEfficiencyInfo.tagEfficiency() > MINIMUM_TAG_EFFICIENCY) {
                orgInformation.add(orgInfoString);
            }
        }

        //if there is at least one tag efficient enough we organize directory after the best one
        if (!orgInformation.isEmpty()) {
            TagEfficiencyInfo mostEfficientTagInfo = tagEfficiencyInfos.get(EFFICIENT_TAG_POSITION);
            orgInformation.addAll(createChildren(mostEfficientTagInfo));
        }

        return orgInformation;

    }

    /**
     * Creates child directories based on the most efficient tag and its values. Each value of the tag leads to the
     * creation of a new child directory under the current directory. Documents are distributed among these child
     * directories based on their association with the tag values.
     *
     * @param mostEfficientTagInfo Information about the most efficient tag used for organizing documents.
     * @return A list of strings representing the organization information for child directories.
     */
    private List<String> createChildren(TagEfficiencyInfo mostEfficientTagInfo) {
        List<String> orgInformation = new ArrayList<>();
        Tag mostEfficientTag = mostEfficientTagInfo.tag();
        List<String> valueStrings = mostEfficientTag.getValueStrings();
        List<List<Document>> newChildrenDocuments = mostEfficientTag.filterDocumentsByNames(documentNames);

        for (int i = 0; i < newChildrenDocuments.size(); i++) {
            String valueString = valueStrings.get(i);
            List<Document> childDocuments = newChildrenDocuments.get(i);

            String childPath = this.path + this.name + DIRECTORY_SEPARATION_PATH;
            String childName = mostEfficientTag.getName() + TAG_VALUE_NAME_SEPARATOR + valueString;

            Directory childDirectory = new Directory(childPath, childName, childDocuments, allDocuments, tags);
            children.add(childDirectory);
        }
        //after documents have been transferred to child directories we remove them here
        this.documents.clear();

        //sort children by uncertainty/ alphabetically
        DirectoryComparator comparator = new DirectoryComparator();
        children.sort(comparator);

        for (Directory child : children) {
            //recursively organize children and add information
            orgInformation.addAll(child.organise());
        }
        return orgInformation;
    }

    /**
     * Retrieves the name of the directory.
     * @return The name of the directory.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the file paths of documents within the directory. The file paths are generated based on the
     * organized structure of the directory, including the paths of child directories and the names of documents.
     * @return A list of strings representing the file paths of documents within the directory.
     */
    public List<String> getFilePaths() {
        List<String> filePaths = new ArrayList<>();
        if (!documents.isEmpty()) {
            //if directory contains documents it has no children
            for (Document document : documents) {
                String wrappedFileName = FILE_NAME_WRAPPER + document.getFileName() + FILE_NAME_WRAPPER;
                filePaths.add(this.path + this.name + DIRECTORY_SEPARATION_PATH + wrappedFileName);
            }
            //sort filenames alphabetically
            Collections.sort(filePaths);
            return filePaths;
        } else {
            //sort child directories by uncertainty
            DirectoryComparator comparator = new DirectoryComparator();
            children.sort(comparator);
            for (Directory child : children) {
                //recursively add document names of child directories
                filePaths.addAll(child.getFilePaths());
            }
        }
        return filePaths;
    }
}
