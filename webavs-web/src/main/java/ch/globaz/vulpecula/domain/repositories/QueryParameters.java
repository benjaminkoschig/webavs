package ch.globaz.vulpecula.domain.repositories;

import java.util.Map;

public interface QueryParameters extends Map<String, String> {
    Map<String, String> valuesWithCaps();
}
