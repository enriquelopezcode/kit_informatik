package edu.kit.kastel.tva.bacchus.net;

import portunus.util.CharacterSet;

import java.util.Collection;

/**
 * A request for generating a password.
 * @param length the length of the password
 * @param characterSets the character sets to be used
 */
public record GenerationRequest(int length, Collection<CharacterSet> characterSets) {
}
