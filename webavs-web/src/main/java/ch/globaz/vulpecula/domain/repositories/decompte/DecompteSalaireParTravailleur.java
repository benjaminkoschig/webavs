package ch.globaz.vulpecula.domain.repositories.decompte;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;

/***
 * permet de grouper et sommer les decomptes salaire d'un travailleur pour une année (besoin de la liste Salaires AVS)
 * 
 * @author CEL
 */
public class DecompteSalaireParTravailleur implements Comparable<DecompteSalaireParTravailleur> {
    private Travailleur travailleur = null;
    private Annee annee;

    private final List<DecompteSalaire> decomptesSalaire = new ArrayList<DecompteSalaire>();
    private final List<AbsenceJustifiee> absenceJustifiees = new ArrayList<AbsenceJustifiee>();
    private final List<CongePaye> congePayes = new ArrayList<CongePaye>();

    public Travailleur getTravailleur() {
        return travailleur;
    }

    public void setTravailleur(Travailleur travailleur) {
        this.travailleur = travailleur;
    }

    public List<DecompteSalaire> getDecomptesSalaire() {
        return decomptesSalaire;
    }

    public List<AbsenceJustifiee> getAbsenceJustifiees() {
        return absenceJustifiees;
    }

    public List<CongePaye> getCongePayes() {
        return congePayes;
    }

    public Annee getAnnee() {
        return annee;
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }

    public void add(DecompteSalaire decompteSalaire) {
        decomptesSalaire.add(decompteSalaire);
    }

    public void add(AbsenceJustifiee absenceJustifiee) {
        absenceJustifiees.add(absenceJustifiee);
    }

    public void add(CongePaye congePaye) {
        congePayes.add(congePaye);
    }

    @Override
    public int compareTo(DecompteSalaireParTravailleur o) {
        return travailleur.getNomPrenomTravailleur().compareTo(o.getTravailleur().getNomPrenomTravailleur());
    }

}
