package ch.globaz.vulpecula.process.statistiques;

import java.util.Collection;
import ch.globaz.utils.Pair;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;

public class EntreeSalaireQualification {
    private Pair<String, Qualification> pairRegionQualification;
    private Collection<PosteTravail> postes;
    private Montant moyenneSalaireHoraire;

    public EntreeSalaireQualification(Pair<String, Qualification> pairRegionQualification,
            Collection<PosteTravail> postes, Montant moyenneSalaireHoraire) {
        this.pairRegionQualification = pairRegionQualification;
        this.postes = postes;
        this.moyenneSalaireHoraire = moyenneSalaireHoraire;
    }

    public Pair<String, Qualification> getPairRegionQualification() {
        return pairRegionQualification;
    }

    public void setPairRegionQualification(Pair<String, Qualification> pairRegionQualification) {
        this.pairRegionQualification = pairRegionQualification;
    }

    public Montant getMoyenneSalaireHoraire() {
        return moyenneSalaireHoraire;
    }

    public void setMoyenneSalaireHoraire(Montant moyenneSalaireHoraire) {
        this.moyenneSalaireHoraire = moyenneSalaireHoraire;
    }

    public Collection<PosteTravail> getPostes() {
        return postes;
    }

    public void setPostes(Collection<PosteTravail> postes) {
        this.postes = postes;
    }
}