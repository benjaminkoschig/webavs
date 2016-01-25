package ch.globaz.common.business.models;

import java.util.ArrayList;
import java.util.List;

public class ResultatTraitementMasseCsvJournal {
    private boolean hasErreursTechniques = false;

    private List<String> lignes = null;

    public ResultatTraitementMasseCsvJournal(boolean hasErreursTechniques, List<String> lignes) {
        super();
        this.hasErreursTechniques = hasErreursTechniques;
        this.lignes = lignes;
    }

    public List<String> getLignes() {
        return lignes;
    }

    public List<Byte> getLignesInByteFormat() {
        List<Byte> lignesInByteFormat = new ArrayList<Byte>();

        if (lignes != null) {
            for (String aLigne : lignes) {
                for (byte aByte : aLigne.getBytes()) {
                    lignesInByteFormat.add(Byte.valueOf(aByte));
                }
            }
        }

        return lignesInByteFormat;
    }

    public boolean hasErreursTechniques() {
        return hasErreursTechniques;
    }

}