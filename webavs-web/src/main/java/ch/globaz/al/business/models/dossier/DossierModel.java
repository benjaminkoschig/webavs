package ch.globaz.al.business.models.dossier;

/**
 * Modèle complet d'un dossier AF. Ce modèle contient toutes les colonnes de la table "Dossier" (ALDOS)
 *
 * @author jts
 *
 */
public class DossierModel extends DossierFkModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * catégorie professionnelle
     */
    private String categorieProf = null;
    /**
     * date début activité de l'allocataire chez son employeur
     */
    private String debutActivite = null;
    /**
     * date début de validité du dossier
     */
    private String debutValidite = null;
    /**
     * Date de fin d'activité de l'allocataire
     */
    private String finActivite = null;
    /**
     * Date de fin de validité du dossier
     */
    private String finValidite = null;
    /**
     * Indique si la décision doit être imprimée
     */
    private Boolean imprimerDecision = null;
    /**
     * Loi AF du conjoint. Utilisé dans le cas de prestation intercantonale
     */
    private String loiConjoint = null;

    /**
     * Montant forcé
     */
    private String montantForce = null;

    /**
     * Motif de réduction pour les dossiers ayant un taux inférieur à 100%
     *
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_MOTIF_REDUC
     */
    private String motifReduction = null;

    /**
     * Nombre de jour pour le premier mois de validité pour les cas où l'allocataire n'a pas travailler pendant la
     * totalité du mois
     */
    private String nbJoursDebut = null;

    /**
     * Nombre de jour pour le dernier mois de validité pour les cas où l'allocataire n'a pas travailler pendant la
     * totalité du mois
     */
    private String nbJoursFin = null;
    /** Indique si l'activité de l'allocataire lié au dossier est accessoire (utilisé pour les agriculteurs) */
    private Boolean professionAccessoire = null;
    /**
     * Personne de référence pour le dossier
     */
    private String reference = null;
    /**
     * Indique si une retenue d'impôt doit être effectuée
     */
    private Boolean retenueImpot = null;
    /**
     * Tarif forcé au niveau du dossier
     */
    private String tarifForce = null;
    /**
     * Taux d'occupation de l'allocataire chez son employeur
     */
    private String tauxOccupation = null;
    /**
     * Taux de versement des allocations. Valeur entre 1 et 100% inclus
     */
    private String tauxVersement = null;
    /**
     * Unité de calcul
     *
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_UNITE_CALCUL
     */
    private String uniteCalcul = null;

    /**
     * Le canton d'imposition
     */
    private String cantonImposition = null;

    /**
     * La date de début période à générer entrée sur l'écran de regénération des prestations d'allocations familiales
     */
    private String dateDebutPeriode = null;

    /**
     * La date de fin période à générer entrée sur l'écran de regénération des prestations d'allocations familiales
     */
    private String dateFinPeriode = null;

    /**
     *
     * Id du gestionnaire ayant réalisée le dernière modification sur le dossier.</br>
     * Cette information est utilisée pour l'impression par file d'attente.
     */
    private String idGestionnaire;

    /**
     * @return the categorieProf
     */
    public String getCategorieProf() {
        return categorieProf;
    }

    /**
     * Retourne la date de début d'activité de l'allocataire chez son employeur
     *
     * @return the debutActivite
     */
    public String getDebutActivite() {
        return debutActivite;
    }

    /**
     * Retourne la date de début de validité du dossier
     *
     * @return the debutValidite
     */
    public String getDebutValidite() {
        return debutValidite;
    }

    /**
     * Retourne la date de fin d'activité de l'allocataire chez l'affilié
     *
     * @return the finActivite
     */
    public String getFinActivite() {
        return finActivite;
    }

    /**
     * Retourne la date de fin de validité du dossier
     *
     * @return the finValidite
     */
    public String getFinValidite() {
        return finValidite;
    }

    /**
     * Indique si la décision doit être imprimée
     *
     * @return <code>true</code> si la décision doit être imprimée <code>false</code>
     */
    public Boolean getImprimerDecision() {
        return imprimerDecision;
    }

    /**
     * @return the loiConjoint
     */
    public String getLoiConjoint() {
        return loiConjoint;
    }

    /**
     * Retourne le montant Force du dossier.
     *
     * @return the montantForce
     */
    public String getMontantForce() {
        return montantForce;
    }

    /**
     * Retourne le motif de réduction du dossier. Utilisé si le taux de versement n'est pas de 100%
     *
     * @return the motifReduction
     * @see DossierModel#getTauxVersement
     */
    public String getMotifReduction() {
        return motifReduction;
    }

    /**
     * Retourne le nombre de jour effectif du premier mois de droit si ce n'est pas un mois complet
     *
     * @return the nbJoursDebut
     */
    public String getNbJoursDebut() {
        return nbJoursDebut;
    }

    /**
     * Retourne le nombre de jour effectif du dernier mois de droit si ce n'est pas un mois complet
     *
     * @return the nbJoursFin
     */
    public String getNbJoursFin() {
        return nbJoursFin;
    }

    /**
     * Indique si l'activité de l'allocataire lié au dossier est accessoire (utilisé pour les agriculteurs)
     *
     * @return
     */
    public Boolean getProfessionAccessoire() {
        return professionAccessoire;
    }

    /**
     * @return Personne de référence pour le dossier
     */
    public String getReference() {
        return reference;
    }

    /**
     * Indique si une retenue d'impôt doit être effectuée
     *
     * @return the retenueImpot
     */
    public Boolean getRetenueImpot() {
        return retenueImpot;
    }

    /**
     * @return the tarifForce
     */
    public String getTarifForce() {
        return tarifForce;
    }

    /**
     * Retourne le taux d'occupation de l'allocataire
     *
     * @return the tauxOccupation
     */
    public String getTauxOccupation() {
        return tauxOccupation;
    }

    /**
     * Retourne le taux de versement du dossier
     *
     * @return the tauxVersement
     */
    public String getTauxVersement() {
        return tauxVersement;
    }

    /**
     * Retourne l'unité de calcul (Mois, Jour ou Heure)
     *
     * @return the uniteCalcul
     */
    public String getUniteCalcul() {
        return uniteCalcul;
    }

    /**
     * @param categorieProf
     *            the categorieProf to set
     */
    public void setCategorieProf(String categorieProf) {
        this.categorieProf = categorieProf;
    }

    /**
     * Définit la date de début d'activité de l'allocataire chez son employeur
     *
     * @param debutActivite
     *            the debutActivite to set
     */
    public void setDebutActivite(String debutActivite) {
        this.debutActivite = debutActivite;
    }

    /**
     * Définit la date de début de validité du dossier
     *
     * @param debutValidite
     *            the debutValidite to set
     */
    public void setDebutValidite(String debutValidite) {
        this.debutValidite = debutValidite;
    }

    /**
     * retourne la date de fin d'activité de l'allocataire chez l'affilié
     *
     * @param finActivite
     *            the finActivite to set
     */
    public void setFinActivite(String finActivite) {
        this.finActivite = finActivite;
    }

    /**
     * Définit la date de fin de validité du dossier
     *
     * @param finValidite
     *            the finValidite to set
     */
    public void setFinValidite(String finValidite) {
        this.finValidite = finValidite;
    }

    /**
     * Définit l'indicateur d'impression de la décision
     *
     * @param imprimerDecision
     *            <code>true</code> si la décision doit être imprimée, <code>false</code> sinon
     */
    public void setImprimerDecision(Boolean imprimerDecision) {
        this.imprimerDecision = imprimerDecision;
    }

    /**
     * @param loiConjoint
     *            the loiConjoint to set
     */
    public void setLoiConjoint(String loiConjoint) {
        this.loiConjoint = loiConjoint;
    }

    /**
     * Définit le montant Force du dossier
     *
     * @param montantForce
     *            the montantForce to set
     */
    public void setMontantForce(String montantForce) {
        this.montantForce = montantForce;
    }

    /**
     * Définit le motif de réduction du dossier
     *
     * @param motifReduction
     *            the motifReduction to set
     *
     * @see DossierModel#setTauxVersement(String)
     */
    public void setMotifReduction(String motifReduction) {
        this.motifReduction = motifReduction;
    }

    /**
     * Définit le nombre de jour effectif du premier mois de droit si ce n'est pas un mois complet
     *
     * @param nbJoursDebut
     *            the nbJoursDebut to set
     */
    public void setNbJoursDebut(String nbJoursDebut) {
        this.nbJoursDebut = nbJoursDebut;
    }

    /**
     * Définit le nombre de jour effectif du dernier mois de droit si ce n'est pas un mois complet
     *
     * @param nbJoursFin
     *            the nbJoursFin to set
     */
    public void setNbJoursFin(String nbJoursFin) {
        this.nbJoursFin = nbJoursFin;
    }

    public void setProfessionAccessoire(Boolean professionAccessoire) {
        this.professionAccessoire = professionAccessoire;
    }

    /**
     * @param reference
     *            the reference to set
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * Définit l'indicateur de retenue d'impôt
     *
     * @param retenueImpot
     *            the retenueImpot to set
     */
    public void setRetenueImpot(Boolean retenueImpot) {
        this.retenueImpot = retenueImpot;
    }

    /**
     * @param tarifForce
     *            the tarifForce to set
     */
    public void setTarifForce(String tarifForce) {
        this.tarifForce = tarifForce;
    }

    /**
     * Définit le taux d'occupation de l'allocataire
     *
     * @param tauxOccupation
     *            the tauxOccupation to set
     */
    public void setTauxOccupation(String tauxOccupation) {
        this.tauxOccupation = tauxOccupation;
    }

    /**
     * Définit le taux de réduction du dossier
     *
     * @param tauxVersement
     *            the tauxVersement to set
     */
    public void setTauxVersement(String tauxVersement) {
        this.tauxVersement = tauxVersement;
    }

    /**
     * Définit l'unité de calcul (Mois, Jour ou Heure)
     *
     * @param uniteCalcul
     *            the uniteCalcul to set
     */
    public void setUniteCalcul(String uniteCalcul) {
        this.uniteCalcul = uniteCalcul;
    }

    /**
     * Retourne l'id du gestionnaire ayant réalisée le dernière modification sur le dossier.</br>
     * Cette information est utilisée pour l'impression par file d'attente.
     *
     * @return l'id du gestionnaire ayant réalisée le dernière modification sur le dossier.
     */
    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    /**
     * Set l'id du gestionnaire ayant réalisée le dernière modification sur le dossier.</br>
     * Cette information est utilisée pour l'impression par file d'attente.
     *
     * @param idGestionnaire l'id du gestionnaire ayant réalisée le dernière modification sur le dossier.
     */
    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public String getCantonImposition() {
        return cantonImposition;
    }

    public void setCantonImposition(String cantonImposition) {
        this.cantonImposition = cantonImposition;
    }

    public String getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    public void setDateDebutPeriode(String dateDebutPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
    }

    public String getDateFinPeriode() {
        return dateFinPeriode;
    }

    public void setDateFinPeriode(String dateFinPeriode) {
        this.dateFinPeriode = dateFinPeriode;
    }
}