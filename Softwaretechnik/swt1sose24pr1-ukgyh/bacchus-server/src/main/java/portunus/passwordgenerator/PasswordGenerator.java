package portunus.passwordgenerator;

import portunus.util.CharacterSet;
import portunus.util.IPasswordGenerator;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * This class implements a basic password generator for Portunus supporting different password
 * lengths and character sets.
 */
public class PasswordGenerator implements IPasswordGenerator {
    private final Random random;

    private final Map<CharacterSet, String> characterSetToCharactersMap;

    /**
     * Constructor for a new PasswordGenerator using a given random generator.
     *
     * @param random The random generator to use
     */
    public PasswordGenerator(Random random) {
        this.random = random;

        characterSetToCharactersMap = new HashMap<>();

        characterSetToCharactersMap.put(CharacterSet.UPPER_CASE, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        characterSetToCharactersMap.put(CharacterSet.LOWER_CASE, "abcdefghijklmnopqrstuvwxyz");
        characterSetToCharactersMap.put(CharacterSet.DIGITS, "0123456789");
        characterSetToCharactersMap.put(CharacterSet.MINUS, "-");
        characterSetToCharactersMap.put(CharacterSet.UNDERSCORE, "_");
        characterSetToCharactersMap.put(CharacterSet.SPACE, " ");
    }

    /**
     * Returns a string with all characters in the given characterset.
     *
     * @param characterSet The characterset
     * @return The generated string
     */
    public String getCharacters(CharacterSet characterSet) {
        String characters = characterSetToCharactersMap.get(characterSet);

        if (characters == null) {
            return "";
        }

        return characters;
    }

    @Override
    public String generatePassword(int length, Collection<CharacterSet> characterSets) {
        if (length < 0 || characterSets == null || characterSets.isEmpty()) {
            return "";
        }

        Set<CharacterSet> localCharacterSets = removeDuplicateCharacterSets(characterSets);

        String availableCharacters = collectAvailableCharacters(localCharacterSets);
        int numberOfAvailableCharacters = availableCharacters.length();

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int characterIndex = random.nextInt(numberOfAvailableCharacters);
            password.append(availableCharacters.charAt(characterIndex));
        }

        return password.toString();
    }

    /**
     * Removes duplicate character sets from a collection of charactersets.
     *
     * @param characterSets The charactersets to filter.
     * @return The filtered sets.
     */
    private Set<CharacterSet> removeDuplicateCharacterSets(Collection<CharacterSet> characterSets) {
        return new HashSet<>(characterSets);
    }

    private String collectAvailableCharacters(Collection<CharacterSet> characterSets) {
        StringBuilder availableCharacters = new StringBuilder();

        for (CharacterSet characterSet : characterSets) {
            availableCharacters.append(getCharacters(characterSet));
        }

        return availableCharacters.toString();
    }
}
