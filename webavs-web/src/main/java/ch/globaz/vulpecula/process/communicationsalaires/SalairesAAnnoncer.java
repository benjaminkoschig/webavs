package ch.globaz.vulpecula.process.communicationsalaires;

import java.util.List;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;

/**
 * @author JPA | Créé le 11.05.16
 * 
 */
public class SalairesAAnnoncer {
    private String idTravailleur;
    private String idEmployeur;
    private List<DecompteSalaire> listeDecomptes;
    private List<AbsenceJustifiee> listeAJ;
    private List<CongePaye> listeCP;

    public String getIdTravailleur() {
        return idTravailleur;
    }

    public void setIdTravailleur(String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }

    public List<DecompteSalaire> getListeDecomptes() {
        return listeDecomptes;
    }

    public void setListeDecomptes(List<DecompteSalaire> listeDecomptes) {
        this.listeDecomptes = listeDecomptes;
    }

    public List<AbsenceJustifiee> getListeAJ() {
        return listeAJ;
    }

    public void setListeAJ(List<AbsenceJustifiee> listeAJ) {
        this.listeAJ = listeAJ;
    }

    public List<CongePaye> getListeCP() {
        return listeCP;
    }

    public void setListeCP(List<CongePaye> listeCP) {
        this.listeCP = listeCP;
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }
}
