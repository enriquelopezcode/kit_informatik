package kit.organiser.model.document;

import kit.organiser.model.tag.BinaryTag;
import kit.organiser.model.tag.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a program document.
 * This document automatically receives a binary executable tag.
 * @author ukgyh
 */
public class ProgramDocument extends Document {
    private static final String EXECUTABLE_TAG_NAME = "executable";
    private static final String DEFINED_VALUE = "defined";
    private boolean hasExecutableTag;
    ProgramDocument(String name, int accessAmount) {
        super(name, accessAmount);
        this.hasExecutableTag = false;
    }

    @Override
    public List<Tag> getExtraTags() {
        //no tags to add
        List<Tag> tags = new ArrayList<>();
        if (!hasExecutableTag) {
            tags.add(new BinaryTag(EXECUTABLE_TAG_NAME, DEFINED_VALUE));
            hasExecutableTag = true;
        }
        return tags;
    }
    @Override
    public Tag translateTag(Tag tag) {
        //no tag to translate
        return tag;
    }
}
