package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Mod�le complexe de recherche pour l'impression de d�clarations de versment d�taill�
 * 
 * @author PTA
 * 
 */
public class DeclarationVersementDetailleSearchComplexModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * recherche sur le d�but de la p�riode de versement
     */
    String forDateDebut = null;
    /**
     * recherche sur la fin de la p�riode de versement
     */
    String forDateFin = null;

    /**
     * recherche sur l'�tat de la prestation
     */
    String forEtat = null;

    /**
     * recherche sur le dossier
     */
    String forIdDossier = null;
    /**
     * recherche sur idDroit
     */
    String forIdDroit = null;
    /**
     * recherche su la recap
     */
    String forIdRecap = null;

    /**
     * recherche su le tiers beneficiaire (allocataire ou autre)
     */
    String forTiers = null;

    @Getter
    @Setter
    String forPeriodeDebut = null;
    @Getter
    @Setter
    String forPeriodeFin = null;
    @Getter
    @Setter
    String forMontantPositif = null;
    @Getter
    @Setter
    String forNotEtatPrestation = null;
    @Getter
    @Setter
    String forNss = null;

    /**
     * recherche sur le type de bonification de la prestation
     */
    Collection<String> inBonification = null;

    Collection<String> inIdDossier = new ArrayList<String>();

    /**
     * recherche sur les tiers autre que
     */
    String notForTiers = null;

    /**
     * @return the forDateDebut
     */
    public String getForDateDebut() {
        return forDateDebut;
    }

    /**
     * @return the forDateFin
     */
    public String getForDateFin() {
        return forDateFin;
    }

    /**
     * @return the forEtat
     */
    public String getForEtat() {
        return forEtat;
    }

    /**
     * @return the forIdossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    public String getForIdRecap() {
        return forIdRecap;
    }

    /**
     * @return the forTiers
     */
    public String getForTiers() {
        return forTiers;
    }

    public Collection<String> getInBonification() {
        return inBonification;
    }

    /**
     * @return the notForTiers
     */
    public String getNotForTiers() {
        return notForTiers;
    }

    /**
     * @param forDateDebut
     *            the forDateDebut to set
     */
    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    /**
     * @param forDateFin
     *            the forDateFin to set
     */
    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    /**
     * @param forEtat
     *            the forEtat to set
     */
    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    /**
     * @param forIdDossier
     *            the forIdossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setForIdRecap(String forIdRecap) {
        this.forIdRecap = forIdRecap;
    }

    /**
     * @param forTiers
     *            the forTiers to set
     */
    public void setForTiers(String forTiers) {
        this.forTiers = forTiers;
    }

    public void setInBonification(Collection<String> inBonification) {
        this.inBonification = inBonification;
    }

    /**
     * @param notForTiers
     *            the notForTiers to set
     */
    public void setNotForTiers(String notForTiers) {
        this.notForTiers = notForTiers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass ()
     */
    @Override
    public Class<DeclarationVersementDetailleComplexModel> whichModelClass() {
        return (DeclarationVersementDetailleComplexModel.class);
    }

    public Collection<String> getInIdDossier() {
        return inIdDossier;
    }

    public void setInIdDossier(Collection<String> inIdDossier) {
        this.inIdDossier = inIdDossier;
    }

}
