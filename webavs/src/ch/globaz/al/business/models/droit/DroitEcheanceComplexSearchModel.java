package ch.globaz.al.business.models.droit;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

/**
 * classe modèle recherche pour les droits arrivant à échéance
 * 
 * @author PTA
 * 
 */
public class DroitEcheanceComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * nombre d'années à enlever à la date d'échéance du droit
     */

    private String anneeEnlever = null;
    /**
     * recherche sur date naissance
     */
    private String forDateNaissance = null;
    /**
     * recherche sur l'état du droit
     */
    private String forEtatDossier = null;
    /**
     * recherche sur l'état du droit
     */
    private String forEtatDroit = null;
    /**
     * par la date de fin du droit forcée
     */
    private String forFinDroitForce = null;
    /**
     * recherche par la valeur de imprimer échéance
     */
    private Boolean forImprimerEcheance = null;
    /**
     * recherche sur motif fin (CTAR)
     */
    private String forMotifFin = null;

    /**
     * Recherche sur la motif fin (Echeance)
     */

    private String forMotifFinEch = null;

    /**
     * excluit le statut Dossier
     */
    private String forNotStatuDossier = null;

    /**
     * recherche pour les paiments directs
     */
    private String forPaiementDirect = null;

    /**
     * recherche sur les paiements indirects
     */

    private String forPaiementIndirect = null;

    /**
     * recherche sutr type droit enfant
     */
    private String forTypeDroitEnfant = null;

    /**
     * recherche sur type droit formation
     */

    private String forTypeDroitForm = null;

    /**
     * type de liaison
     */
    private String forTypeLiaison = null;
    /**
     * états du droit
     */

    private Collection inEtatDroit = null;

    /**
     * recherche motif
     */
    private Collection inMotifFin = null;

    /**
     * recherche sur le type de droit
     */
    private Collection inTypeDroit = null;

    /**
     * @return the anneeEnlever
     */
    public String getAnneeEnlever() {
        return anneeEnlever;
    }

    /**
     * @return the forDateNaissance
     */
    public String getForDateNaissance() {
        return forDateNaissance;
    }

    /**
     * @return the forEtatDossier
     */
    public String getForEtatDossier() {
        return forEtatDossier;
    }

    /**
     * @return the forEtatDroit
     */
    public String getForEtatDroit() {
        return forEtatDroit;
    }

    /**
     * @return the forFindroitForce
     */
    public String getForFinDroitForce() {
        return forFinDroitForce;
    }

    /**
     * @return the forImprimerEcheance
     */
    public Boolean getForImprimerEcheance() {
        return forImprimerEcheance;
    }

    /**
     * @return the forMotifFin
     */
    public String getForMotifFin() {
        return forMotifFin;
    }

    /**
     * @return the forMotifFinEch
     */
    public String getForMotifFinEch() {
        return forMotifFinEch;
    }

    public String getForNotStatuDossier() {
        return forNotStatuDossier;
    }

    /**
     * @return the forPaiementDirect
     */
    public String getForPaiementDirect() {
        return forPaiementDirect;
    }

    /**
     * @return the forPaiementIndirect
     */
    public String getForPaiementIndirect() {
        return forPaiementIndirect;
    }

    /**
     * @return the forTypeDroitEnfant
     */
    public String getForTypeDroitEnfant() {
        return forTypeDroitEnfant;
    }

    /**
     * @return the forTypeDroitForm
     */
    public String getForTypeDroitForm() {
        return forTypeDroitForm;
    }

    /**
     * @return the forTypeLiaison
     */
    public String getForTypeLiaison() {
        return forTypeLiaison;
    }

    public Collection getInEtatDroit() {
        return inEtatDroit;
    }

    /**
     * @return the inMotifFin
     */
    public Collection getInMotifFin() {
        return inMotifFin;
    }

    /**
     * @return the inTypeDroit
     */
    public Collection getInTypeDroit() {
        return inTypeDroit;
    }

    /**
     * @param anneeEnlever
     *            the anneeEnlever to set
     */
    public void setAnneeEnlever(String anneeEnlever) {
        this.anneeEnlever = anneeEnlever;
    }

    /**
     * @param forDateNaissance
     *            the forDateNaissance to set
     */
    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    /**
     * @param forEtatDossier
     *            the forEtatDossier to set
     */
    public void setForEtatDossier(String forEtatDossier) {
        this.forEtatDossier = forEtatDossier;
    }

    /**
     * @param forEtatDroit
     *            the forEtatDroit to set
     */
    public void setForEtatDroit(String forEtatDroit) {
        this.forEtatDroit = forEtatDroit;
    }

    /**
     * @param forFindroitForce
     *            the forFindroitForce to set
     */
    public void setForFinDroitForce(String forFindroitForce) {
        forFinDroitForce = forFindroitForce;
    }

    /**
     * @param forImprimerEcheance
     *            the forImprimerEcheance to set
     */
    public void setForImprimerEcheance(Boolean forImprimerEcheance) {
        this.forImprimerEcheance = forImprimerEcheance;
    }

    /**
     * @param forMotifFin
     *            the forMotifFin to set
     */
    public void setForMotifFin(String forMotifFin) {
        this.forMotifFin = forMotifFin;
    }

    /**
     * @param forMotifFinEch
     *            the forMotifFinEch to set
     */
    public void setForMotifFinEch(String forMotifFinEch) {
        this.forMotifFinEch = forMotifFinEch;
    }

    public void setForNotStatuDossier(String forNotStatuDossier) {
        this.forNotStatuDossier = forNotStatuDossier;
    }

    /**
     * @param forPaiementDirect
     *            the forPaiementDirect to set
     */
    public void setForPaiementDirect(String forPaiementDirect) {
        this.forPaiementDirect = forPaiementDirect;
    }

    /**
     * @param forPaiementIndirect
     *            the forPaiementIndirect to set
     */
    public void setForPaiementIndirect(String forPaiementIndirect) {
        this.forPaiementIndirect = forPaiementIndirect;
    }

    /**
     * @param forTypeDroitEnfant
     *            the forTypeDroitEnfant to set
     */
    public void setForTypeDroitEnfant(String forTypeDroitEnfant) {
        this.forTypeDroitEnfant = forTypeDroitEnfant;
    }

    /**
     * @param forTypeDroitForm
     *            the forTypeDroitForm to set
     */
    public void setForTypeDroitForm(String forTypeDroitForm) {
        this.forTypeDroitForm = forTypeDroitForm;
    }

    /**
     * @param forTypeLiaison
     *            the forTypeLiaison to set
     */
    public void setForTypeLiaison(String forTypeLiaison) {
        this.forTypeLiaison = forTypeLiaison;
    }

    public void setInEtatDroit(Collection inEtatDroit) {
        this.inEtatDroit = inEtatDroit;
    }

    /**
     * @param inMotifFin
     *            the inMotifFin to set
     */
    public void setInMotifFin(Collection inMotifFin) {
        this.inMotifFin = inMotifFin;
    }

    /**
     * @param inTypeDroit
     *            the inTypeDroit to set
     */
    public void setInTypeDroit(Collection inTypeDroit) {
        this.inTypeDroit = inTypeDroit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return DroitEcheanceComplexModel.class;
    }

}
