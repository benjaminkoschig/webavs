/**
 * 
 */
package ch.globaz.perseus.business.models.demande;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author DDE
 * 
 */
public class SimpleDemande extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean calculable = null;
    private Boolean calculParticulier = null;
    private Boolean casRigueur = null;
    private Boolean coaching = null;
    private String csCaisse = null;
    private String csEtatDemande = null;
    private String dateArrivee = null;
    private String dateDebut = null;
    private String dateDepart = null;
    private String dateDepot = null;
    private String dateFin = null;
    private String dateDecisionValidation = null;
    private String timeDecisionValidation = null;
    private Boolean fromRI = null;
    private String idAgenceCommunale = null;
    private String idAgenceRi = null;
    private String idDemande = null;
    private String idDossier = null;
    private String idGestionnaire = null;
    private String idSituationFamiliale = null;
    private String listCsAutresDemandes = null;
    private String listCsAutresPrestations = null;
    private String numeroOFS = null;
    private Boolean permisB = null;
    private Boolean refusForce = null;
    private String remarques = null;
    private String statutSejour = null;
    private String typeDemande = null;
    private String dateListeNonEntreeEnMatiere = null;
    private Boolean nonEntreeEnMatiere = null;
    private String timeDemandeSaisie = null;
    private String dateDemandeSaisie = null;

    public String getTimeDemandeSaisie() {
        return timeDemandeSaisie;
    }

    public void setTimeDemandeSaisie(String timeDemandeSaisie) {
        this.timeDemandeSaisie = timeDemandeSaisie;
    }

    public String getDateDemandeSaisie() {
        return dateDemandeSaisie;
    }

    public void setDateDemandeSaisie(String dateDemandeSaisie) {
        this.dateDemandeSaisie = dateDemandeSaisie;
    }

    public String getDateListeNonEntreeEnMatiere() {
        return dateListeNonEntreeEnMatiere;
    }

    public void setDateListeNonEntreeEnMatiere(String dateListeNonEntreeEnMatiere) {
        this.dateListeNonEntreeEnMatiere = dateListeNonEntreeEnMatiere;
    }

    public Boolean getNonEntreeEnMatiere() {
        return nonEntreeEnMatiere;
    }

    public void setNonEntreeEnMatiere(Boolean nonEntreeEnMatiere) {
        this.nonEntreeEnMatiere = nonEntreeEnMatiere;
    }

    /**
     * @return the calculable
     */
    public Boolean getCalculable() {
        return calculable;
    }

    public Boolean getCalculParticulier() {
        return calculParticulier;
    }

    /**
     * @return the casRigueur
     */
    public Boolean getCasRigueur() {
        return casRigueur;
    }

    public Boolean getCoaching() {
        return coaching;
    }

    /**
     * @return the csCaisse
     */
    public String getCsCaisse() {
        return csCaisse;
    }

    /**
     * @return the csEtatDemande
     */
    public String getCsEtatDemande() {
        return csEtatDemande;
    }

    /**
     * @return the dateArrivee
     */
    public String getDateArrivee() {
        return dateArrivee;
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return the dateDepart
     */
    public String getDateDepart() {
        return dateDepart;
    }

    /**
     * @return the dateDepot
     */
    public String getDateDepot() {
        return dateDepot;
    }

    /**
     * @return the dateFin
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * @return the la date concatener de date et time
     */
    public String getDateTimeDecisionValidation() {
        String dateConcat = "";

        dateConcat = getDateDecisionValidation();

        if ("0".equals(getTimeDecisionValidation()) == false) {
            if (getTimeDecisionValidation().length() < 6) {
                // Si la date etait tomber sur des heure avec un 0 au début, le time ne sera plus avec 6 chiffres.
                // C'est pour cela que nous les ajoutons a nouveau selon le contexte.
                // Exemple : 00:05:25 => 525 en BD. Il faut donc ajouter 3x0 au début pour concatener.
                for (int i = 0; i < (6 - getTimeDecisionValidation().length()); i++) {
                    dateConcat += "0";
                }
            }

            dateConcat += getTimeDecisionValidation();
        }

        return dateConcat;
    }

    public String getDateDecisionValidation() {
        return dateDecisionValidation;
    }

    public void setDateDecisionValidation(String dateDecisionValidation) {
        this.dateDecisionValidation = dateDecisionValidation;
    }

    public String getTimeDecisionValidation() {
        return timeDecisionValidation;
    }

    public void setTimeDecisionValidation(String timeDecisionValidation) {
        this.timeDecisionValidation = timeDecisionValidation;
    }

    /**
     * @return the fromRI
     */
    public Boolean getFromRI() {
        return fromRI;
    }

    @Override
    public String getId() {
        return idDemande;
    }

    /**
     * @return the idAgenceCommunale
     */
    public String getIdAgenceCommunale() {
        return idAgenceCommunale;
    }

    /**
     * @return the idAgenceRi
     */
    public String getIdAgenceRi() {
        return idAgenceRi;
    }

    /**
     * @return the idDemande
     */
    public String getIdDemande() {
        return idDemande;
    }

    /**
     * @return the idDossier
     */
    public String getIdDossier() {
        return idDossier;
    }

    /**
     * @return the idGestionnaire
     */
    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    /**
     * @return the idSituationFamiliale
     */
    public String getIdSituationFamiliale() {
        return idSituationFamiliale;
    }

    /**
     * @return the listCsAutresDemandes
     */
    public String getListCsAutresDemandes() {
        return listCsAutresDemandes;
    }

    /**
     * @return the listCsAutresPrestations
     */
    public String getListCsAutresPrestations() {
        return listCsAutresPrestations;
    }

    public String getNumeroOFS() {
        return numeroOFS;
    }

    /**
     * @return the permisB
     */
    public Boolean getPermisB() {
        return permisB;
    }

    public Boolean getRefusForce() {
        return refusForce;
    }

    /**
     * @return the remarques
     */
    public String getRemarques() {
        return remarques;
    }

    public String getStatutSejour() {
        return statutSejour;
    }

    /**
     * @return the typeDemande
     */
    public String getTypeDemande() {
        return typeDemande;
    }

    /**
     * @param calculable
     *            the calculable to set
     */
    public void setCalculable(Boolean calculable) {
        this.calculable = calculable;
    }

    public void setCalculParticulier(Boolean calculParticulier) {
        this.calculParticulier = calculParticulier;
    }

    /**
     * @param casRigueur
     *            the casRigueur to set
     */
    public void setCasRigueur(Boolean casRigueur) {
        this.casRigueur = casRigueur;
    }

    public void setCoaching(Boolean coaching) {
        this.coaching = coaching;
    }

    /**
     * @param csCaisse
     *            the csCaisse to set
     */
    public void setCsCaisse(String csCaisse) {
        this.csCaisse = csCaisse;
    }

    /**
     * @param csEtatDemande
     *            the csEtatDemande to set
     */
    public void setCsEtatDemande(String csEtatDemande) {
        this.csEtatDemande = csEtatDemande;
    }

    /**
     * @param dateArrivee
     *            the dateArrivee to set
     */
    public void setDateArrivee(String dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @param dateDepart
     *            the dateDepart to set
     */
    public void setDateDepart(String dateDepart) {
        this.dateDepart = dateDepart;
    }

    /**
     * @param dateDepot
     *            the dateDepot to set
     */
    public void setDateDepot(String dateDepot) {
        this.dateDepot = dateDepot;
    }

    /**
     * @param dateFin
     *            the dateFin to set
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * @param dateTimeDecisionValidation
     *            the dateTimeDecisionValidation to set
     */
    public void setDateTimeDecisionValidation(String dateTimeDecisionValidation) {
        if (JadeStringUtil.isBlankOrZero(dateTimeDecisionValidation) == false) {
            setDateDecisionValidation(JadeStringUtil.substring(dateTimeDecisionValidation, 0, 8));
            setTimeDecisionValidation(JadeStringUtil.substring(dateTimeDecisionValidation, 8, 6));
        } else {
            setDateDecisionValidation("");
            setTimeDecisionValidation("");
        }
    }

    /**
     * @param fromRI
     *            Définir si l'assuré vient des RI
     */
    public void setFromRI(Boolean fromRI) {
        this.fromRI = fromRI;
    }

    @Override
    public void setId(String id) {
        idDemande = id;
    }

    /**
     * @param idAgenceCommunale
     *            the idAgenceCommunale to set
     */
    public void setIdAgenceCommunale(String idAgenceCommunale) {
        this.idAgenceCommunale = idAgenceCommunale;
    }

    /**
     * @param idAgenceRi
     *            the idAgenceRi to set
     */
    public void setIdAgenceRi(String idAgenceRi) {
        this.idAgenceRi = idAgenceRi;
    }

    /**
     * @param idDemande
     *            the idDemande to set
     */
    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    /**
     * @param idDossier
     *            the idDossier to set
     */
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    /**
     * @param idGestionnaire
     *            the idGestionnaire to set
     */
    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    /**
     * @param idSituationFamiliale
     *            the idSituationFamiliale to set
     */
    public void setIdSituationFamiliale(String idSituationFamiliale) {
        this.idSituationFamiliale = idSituationFamiliale;
    }

    /**
     * @param listCsAutresDemandes
     *            the listCsAutresDemandes to set
     */
    public void setListCsAutresDemandes(String listCsAutresDemandes) {
        this.listCsAutresDemandes = listCsAutresDemandes;
    }

    /**
     * @param listCsAutresPrestations
     *            the listCsAutresPrestations to set
     */
    public void setListCsAutresPrestations(String listCsAutresPrestations) {
        this.listCsAutresPrestations = listCsAutresPrestations;
    }

    public void setNumeroOFS(String numeroOFS) {
        this.numeroOFS = numeroOFS;
    }

    /**
     * @param permisB
     *            the permisB to set
     */
    public void setPermisB(Boolean permisB) {
        this.permisB = permisB;
    }

    public void setRefusForce(Boolean refusForce) {
        this.refusForce = refusForce;
    }

    /**
     * @param remarques
     *            the remarques to set
     */
    public void setRemarques(String remarques) {
        this.remarques = remarques;
    }

    public void setStatutSejour(String statutSejour) {
        this.statutSejour = statutSejour;
    }

    /**
     * @param typeDemande
     *            the typeDemande to set
     */
    public void setTypeDemande(String typeDemande) {
        this.typeDemande = typeDemande;
    }

}
