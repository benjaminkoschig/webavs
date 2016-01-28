package ch.globaz.pegasus.business.domaine.revisionquadriennale;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Periode;

public class RevisionQuadriennale {
    List<DemandeAReviser> demandesARevisers = new ArrayList<DemandeAReviser>();
    Periode periode;

    public RevisionQuadriennale(Periode periode) {
        this.periode = periode;
    }

    public boolean add(DemandeAReviser e) {
        return demandesARevisers.add(e);
    }

    public List<DemandeAReviser> getDemandesARevisers() {
        return demandesARevisers;
    }

    public Periode getPeriode() {
        return periode;
    }
}
