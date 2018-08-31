package ch.globaz.vulpecula.domain.repositories.decompte;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.models.servicemilitaire.ServiceMilitaire;

/***
 * Le poste de travail correspond à la relation entre un travailleur et un
 * employeur.
 * 
 * @author Arnaud Geiser (AGE) | Créé le 17 déc. 2013
 */
public class TravailleurPourRevision {
    private Employeur employeur = null;
    private Travailleur travailleur = null;

    private List<DecompteSalaire> decomptesSalairePR = new ArrayList<DecompteSalaire>();
    private List<DecompteSalaire> decomptesSalaireCP = new ArrayList<DecompteSalaire>();
    private List<DecompteSalaire> decomptesSalaireSP = new ArrayList<DecompteSalaire>();
    private List<DecompteSalaire> decomptesSalaireCT = new ArrayList<DecompteSalaire>();
    private List<DecompteSalaire> decomptesSalaireCPP = new ArrayList<DecompteSalaire>();

    private List<CongePaye> listeCP = new ArrayList<CongePaye>();
    private List<ServiceMilitaire> listeSM = new ArrayList<ServiceMilitaire>();
    private List<AbsenceJustifiee> listeAJ = new ArrayList<AbsenceJustifiee>();

    private Montant montantAF = Montant.ZERO;
    private Montant montantAVS = Montant.ZERO;
    private Montant montantCP = Montant.ZERO;
    private Montant montantBase = Montant.ZERO;

    private List<PrestationsAFPourRevision> listePrestationsAF = new ArrayList<PrestationsAFPourRevision>();

    public Montant getMontantBase() {
        return montantBase;
    }

    public void setMontantBase(Montant montantBase) {
        this.montantBase = montantBase;
    }

    public Montant getMontantAF() {
        return montantAF;
    }

    public void setMontantAF(Montant montantAF) {
        this.montantAF = montantAF;
    }

    public Montant getMontantAVS() {
        return montantAVS;
    }

    public void setMontantAVS(Montant montantAVS) {
        this.montantAVS = montantAVS;
    }

    public Montant getMontantCP() {
        return montantCP;
    }

    public void setMontantCP(Montant montantCP) {
        this.montantCP = montantCP;
    }

    public Employeur getEmployeur() {
        return employeur;
    }

    public void setEmployeur(Employeur employeur) {
        this.employeur = employeur;
    }

    public Travailleur getTravailleur() {
        return travailleur;
    }

    public void setTravailleur(Travailleur travailleur) {
        this.travailleur = travailleur;
    }

    public List<DecompteSalaire> getDecomptesSalairePR() {
        return decomptesSalairePR;
    }

    public void setDecomptesSalaire(List<DecompteSalaire> decomptesSalaire) {
        decomptesSalairePR = decomptesSalaire;
    }

    public List<DecompteSalaire> getDecomptesSalaireCP() {
        return decomptesSalaireCP;
    }

    public void setDecomptesSalaireCP(List<DecompteSalaire> decomptesSalaireCP) {
        this.decomptesSalaireCP = decomptesSalaireCP;
    }

    public List<DecompteSalaire> getDecomptesSalaireSP() {
        return decomptesSalaireSP;
    }

    public void setDecomptesSalaireSP(List<DecompteSalaire> decomptesSalaireSP) {
        this.decomptesSalaireSP = decomptesSalaireSP;
    }

    public List<DecompteSalaire> getDecomptesSalaireCPP() {
        return decomptesSalaireCPP;
    }

    public List<PrestationsAFPourRevision> getListePrestationsAF() {
        return listePrestationsAF;
    }

    public void setListePrestationsAF(List<PrestationsAFPourRevision> listePrestationsAF) {
        this.listePrestationsAF = listePrestationsAF;
    }

    public List<CongePaye> getListeCP() {
        return listeCP;
    }

    public void setListeCP(List<CongePaye> listeCP) {
        this.listeCP = listeCP;
    }

    public List<ServiceMilitaire> getListeSM() {
        return listeSM;
    }

    public void setListeSM(List<ServiceMilitaire> listeSM) {
        this.listeSM = listeSM;
    }

    public List<AbsenceJustifiee> getListeAJ() {
        return listeAJ;
    }

    public void setListeAJ(List<AbsenceJustifiee> listeAJ) {
        this.listeAJ = listeAJ;
    }

    public List<DecompteSalaire> getDecomptesSalaireCT() {
        return decomptesSalaireCT;
    }

    public void setDecomptesSalaireCT(List<DecompteSalaire> decomptesSalaireCT) {
        this.decomptesSalaireCT = decomptesSalaireCT;
    }

    public Montant getMontantTotal() {
        return montantBase.add(montantCP);
    }

    /**
     * Retourne la liste de tous les décomptes du travailleur (PR, CP, CT, SP)
     * 
     * @return Liste de décomptes
     */
    public List<DecompteSalaire> getDecomptesSalaires() {
        List<DecompteSalaire> list = new ArrayList<DecompteSalaire>();
        list.addAll(decomptesSalairePR);
        list.addAll(decomptesSalaireCP);
        list.addAll(decomptesSalaireCT);
        list.addAll(decomptesSalaireSP);
        return list;
    }

    public boolean hasDecomptesCPP() {
        return !decomptesSalaireCPP.isEmpty();
    }

    public Montant getTotalCPP() {
        Montant montant = Montant.ZERO;
        for (DecompteSalaire ds : decomptesSalaireCPP) {
            montant = montant.add(ds.getSalaireTotal());
        }
        return montant;
    }

    public Montant getMontantCPP() {
        return getTotalCPP();
    }
}
