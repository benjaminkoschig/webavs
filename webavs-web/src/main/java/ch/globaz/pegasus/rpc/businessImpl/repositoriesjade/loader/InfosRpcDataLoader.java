package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader;

import java.util.List;
import java.util.Map;
import ch.globaz.common.domaine.Date;

public class InfosRpcDataLoader {

    private Date dateDernierPaiement;
    private int nbDecisionsRefus;
    private int nbDecisionAc;
    private int nbPcaCourante;
    private int nbPlanCalcul;
    private int nbMembreFamille;
    private int nbVersionDroitFiltre;
    private int nbVersionDroitMembreFamille;
    private int nbIdTiersDomicile;
    private int nbIdTiersCourrier;
    private int nbAdresseCourrier;
    private int nbAdresseDomicile;
    private int nbDesicionsRestantes;
    private int nbVersionDroitRestant;
    private int nbErrorRetoursAnnoncePreviousMonth;

    public int getNbVersionDroitFiltre() {
        return nbVersionDroitFiltre;
    }

    public void setNbVersionDroitFiltre(int nbVersionDroitFiltre) {
        this.nbVersionDroitFiltre = nbVersionDroitFiltre;
    }

    public int getNbVersionDroitRestant() {
        return nbVersionDroitRestant;
    }

    public void setNbVersionDroitRestant(int nbVersionDroitRestant) {
        this.nbVersionDroitRestant = nbVersionDroitRestant;
    }

    public int getNbDesicionsRestantes() {
        return nbDesicionsRestantes;
    }

    public void setNbDesicionsRestantes(int nbDesicionsRestantes) {
        this.nbDesicionsRestantes = nbDesicionsRestantes;
    }

    public Date getDateDernierPaiement() {
        return dateDernierPaiement;
    }

    public void setDateDernierPaiement(Date dateDernierPaiement) {
        this.dateDernierPaiement = dateDernierPaiement;
    }

    public int getNbDecisionsRefus() {
        return nbDecisionsRefus;
    }

    public void setNbDecisionsRefus(int nbDecisionsRefus) {
        this.nbDecisionsRefus = nbDecisionsRefus;
    }

    public int getNbDecisionAc() {
        return nbDecisionAc;
    }

    public void setNbDecisionAc(int nbDecisionAc) {
        this.nbDecisionAc = nbDecisionAc;
    }

    public int getNbPcaCourante() {
        return nbPcaCourante;
    }

    public void setNbPcaCourante(int nbPcaCourante) {
        this.nbPcaCourante = nbPcaCourante;
    }

    public int getNbPlanCalcul() {
        return nbPlanCalcul;
    }

    public void setNbPlanCalcul(int nbPlanCalcul) {
        this.nbPlanCalcul = nbPlanCalcul;
    }

    public int getNbMembreFamille() {
        return nbMembreFamille;
    }

    public void setNbMembreFamille(int nbMembreFamille) {
        this.nbMembreFamille = nbMembreFamille;
    }

    public int getNbVersionDroitMembreFamille() {
        return nbVersionDroitMembreFamille;
    }

    public void setNbVersionDroitMembreFamille(int nbVersionDroitMembreFamille) {
        this.nbVersionDroitMembreFamille = nbVersionDroitMembreFamille;
    }

    public int getNbIdTiersDomicile() {
        return nbIdTiersDomicile;
    }

    public void setNbIdTiersDomicile(int nbIdTiersDomicile) {
        this.nbIdTiersDomicile = nbIdTiersDomicile;
    }

    public int getNbIdTiersCourrier() {
        return nbIdTiersCourrier;
    }

    public void setNbIdTiersCourrier(int nbIdTiersCourrier) {
        this.nbIdTiersCourrier = nbIdTiersCourrier;
    }

    public int getNbAdresseCourrier() {
        return nbAdresseCourrier;
    }

    public void setNbAdresseCourrier(int nbAdresseCourrier) {
        this.nbAdresseCourrier = nbAdresseCourrier;
    }

    public int getNbAdresseDomicile() {
        return nbAdresseDomicile;
    }

    public void setNbAdresseDomicile(int nbAdresseDomicile) {
        this.nbAdresseDomicile = nbAdresseDomicile;
    }

    static int computElementSize(Map<String, ? extends List<?>> map) {
        int nb = 0;
        for (List<?> list : map.values()) {
            nb = nb + list.size();
        }
        return nb;
    }

    static int computElementSizeMapMapList(Map<?, ? extends Map<?, ? extends List<?>>> map) {
        int nb = 0;
        for (Map<?, ? extends List<?>> list : map.values()) {
            for (List<?> l : list.values()) {
                nb = nb + l.size();
            }
        }
        return nb;
    }

    public int getNbErrorRetoursAnnoncePreviousMonth() {
        return nbErrorRetoursAnnoncePreviousMonth;
    }

    public void setNbErrorRetoursAnnoncePreviousMonth(int nbErrorRetoursAnnoncePreviousMonth) {
        this.nbErrorRetoursAnnoncePreviousMonth = nbErrorRetoursAnnoncePreviousMonth;
    }

}
