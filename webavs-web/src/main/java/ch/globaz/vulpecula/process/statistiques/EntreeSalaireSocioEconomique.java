package ch.globaz.vulpecula.process.statistiques;

import java.util.Collection;
import java.util.Map;
import ch.globaz.vulpecula.domain.models.common.DetailGroupeLocalites;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.external.models.affiliation.Adhesion;

public class EntreeSalaireSocioEconomique {
    private Employeur employeur;
    private DetailGroupeLocalites detailGroupeLocalites;
    private Adhesion caisseMetier;
    private Map<PosteTravail, Collection<DecompteSalaire>> decomptesGroupByPoste;

    public EntreeSalaireSocioEconomique(Employeur employeur, DetailGroupeLocalites detailGroupeLocalites,
            Adhesion caisseMetier, Map<PosteTravail, Collection<DecompteSalaire>> decomptesGroupByPoste) {
        this.employeur = employeur;
        this.detailGroupeLocalites = detailGroupeLocalites;
        this.caisseMetier = caisseMetier;
        this.decomptesGroupByPoste = decomptesGroupByPoste;
    }

    public Employeur getEmployeur() {
        return employeur;
    }

    public void setEmployeur(Employeur employeur) {
        this.employeur = employeur;
    }

    public DetailGroupeLocalites getDetailGroupeLocalites() {
        return detailGroupeLocalites;
    }

    public void setDetailGroupeLocalites(DetailGroupeLocalites detailGroupeLocalites) {
        this.detailGroupeLocalites = detailGroupeLocalites;
    }

    public Adhesion getCaisseMetier() {
        return caisseMetier;
    }

    public void setCaisseMetier(Adhesion caisseMetier) {
        this.caisseMetier = caisseMetier;
    }

    public Map<PosteTravail, Collection<DecompteSalaire>> getDecomptesGroupByPoste() {
        return decomptesGroupByPoste;
    }

    public void setDecomptesGroupByPoste(Map<PosteTravail, Collection<DecompteSalaire>> decomptesGroupByPoste) {
        this.decomptesGroupByPoste = decomptesGroupByPoste;
    }
}