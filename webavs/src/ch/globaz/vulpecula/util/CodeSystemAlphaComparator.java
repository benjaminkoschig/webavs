package ch.globaz.vulpecula.util;

import java.util.Comparator;

public class CodeSystemAlphaComparator implements Comparator<CodeSystem> {

    @Override
    public int compare(CodeSystem cs1, CodeSystem cs2) {
        return cs1.getLibelle().compareTo(cs2.getLibelle());
    }

}
