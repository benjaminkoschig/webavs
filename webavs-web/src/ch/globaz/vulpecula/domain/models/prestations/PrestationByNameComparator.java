package ch.globaz.vulpecula.domain.models.prestations;

import java.util.Comparator;

public class PrestationByNameComparator implements Comparator<Prestation> {
    @Override
    public int compare(Prestation p1, Prestation p2) {
        return p1.getRaisonSocialeEmployeur().compareTo(p2.getRaisonSocialeEmployeur());
    }
}
