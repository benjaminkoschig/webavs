package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

/**
 * Modèle de recherche sur le modèle <code>DetailPrestationComplexModel</code>
 * 
 * @author jts
 * @see DetailPrestationGenComplexModel
 */
public class DetailPrestationGenComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * recherche sur le type de bonification
     */
    private String forBonification = null;
    /**
     * Recherche sur le début de la date de versement
     */
    private String forDateDebut = null;
    /**
     * Recherche sur la fin de la date de versement
     */
    private String forDateFin = null;
    /**
     * Recherche sur l'état
     */
    private String forEtat = null;
    /**
     * Recherche sur l'état des prestation
     * 
     * @see ch.globaz.al.business.constantes.ALCSPrestation#GROUP_ETAT
     */
    private String forEtatPrestation = null;
    /**
     * Recherche sur l'id de dossier
     */
    private String forIdDossier = null;
    /**
     * Recherche sur l'id du droit
     */
    private String forIdDroit = null;
    /**
     * Recherhce sur l'identifant de la récap
     */
    private String forIdRecap = null;

    /**
     * Recherche sur le montant du détail
     */
    private String forMontant = null;
    /**
     * Recherche sur la fin de la période
     */
    private String forPeriodeDebut = null;
    /**
     * Recherche sur le début de la période
     */
    private String forPeriodeFin = null;
    /**
     * Recherche sur les types de bonification
     */
    private Collection<String> inTypeBonification = null;

    /**
     * @return the forBonification
     */
    public String getForBonification() {
        return forBonification;
    }

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
     * @return the forEtatPrestation
     */
    public String getForEtatPrestation() {
        return forEtatPrestation;
    }

    /**
     * @return the forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * @return the forIdRecap
     */
    public String getForIdRecap() {
        return forIdRecap;
    }

    /**
     * @return the forMontant
     */
    public String getForMontant() {
        return forMontant;
    }

    /**
     * @return the forPeriodeDebut
     */
    public String getForPeriodeDebut() {
        return forPeriodeDebut;
    }

    /**
     * @return the forPeriodeFin
     */
    public String getForPeriodeFin() {
        return forPeriodeFin;
    }

    public Collection<String> getInTypeBonification() {
        return inTypeBonification;
    }

    /**
     * @param forBonification
     *            the forBonification to set
     */
    public void setForBonification(String forBonification) {
        this.forBonification = forBonification;
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
     * @param forEtatPrestation
     *            état à utiliser dans la recherche
     * 
     * @see ch.globaz.al.business.constantes.ALCSPrestation#GROUP_ETAT
     */
    public void setForEtatPrestation(String forEtatPrestation) {
        this.forEtatPrestation = forEtatPrestation;
    }

    /**
     * @param forIdDossier
     *            the forIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    /**
     * @param forIdRecap
     *            the forIdRecap to set
     */
    public void setForIdRecap(String forIdRecap) {
        this.forIdRecap = forIdRecap;
    }

    /**
     * @param forMontant
     *            the forMontant to set
     */
    public void setForMontant(String forMontant) {
        this.forMontant = forMontant;
    }

    /**
     * @param forPeriodeDebut
     *            the forPeriodeDebut to set
     */
    public void setForPeriodeDebut(String forPeriodeDebut) {
        this.forPeriodeDebut = forPeriodeDebut;
    }

    /**
     * @param forPeriodeFin
     *            the forPeriodeFin to set
     */
    public void setForPeriodeFin(String forPeriodeFin) {
        this.forPeriodeFin = forPeriodeFin;
    }

    public void setInTypeBonification(Collection<String> inTypeBonification) {
        this.inTypeBonification = inTypeBonification;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<DetailPrestationGenComplexModel> whichModelClass() {
        return DetailPrestationGenComplexModel.class;
    }
}
