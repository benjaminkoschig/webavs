package globaz.naos.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.naos.api.IAFCotisation;
import java.util.Hashtable;

/**
 * Helper des cotisation d'une affiliation. Date de cr�ation : (28.05.2002 09:11:43)
 * 
 * @author: David Girardin
 */
public class IAFCotisationHelper extends GlobazHelper implements IAFCotisation {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String[] METHODS_TO_LOAD = new String[] { "getAdhesionId", "getAssuranceId", "getDateDebut",
            "getDateFin", "getMassePeriodicite", "getMontantAnnuel", "getMontantMensuel", "getMontantSemestriel",
            "getMontantTrimestriel", "getMotifFin", "getPeriodicite", "getPlanAffiliationId", "getPlanCaisseId",
            "getIsAPI" };

    /**
     * Constructeur du type IAFCotisationHelper
     */
    public IAFCotisationHelper() {
        super("globaz.naos.db.cotisation.AFCotisation");
        setMethodsToLoad(IAFCotisationHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type IAFCotisationHelper
     * 
     * @param valueObject
     *            le Value Object contenant les donn�es
     */
    public IAFCotisationHelper(GlobazValueObject valueObject) {
        super(valueObject);
        setMethodsToLoad(IAFCotisationHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type IAFCotisationHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'impl�mentation
     */
    public IAFCotisationHelper(String implementationClassName) {
        super(implementationClassName);
        setMethodsToLoad(IAFCotisationHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * @see globaz.naos.api.IAFCotisation#find(java.util.Hashtable)
     */
    @Override
    public Object[] find(Hashtable params) throws Exception {
        Object[] result = null;
        result = _getArray("find", new Object[] { params });
        return result;
    }

    @Override
    public IAFCotisation[] findCotisations(Hashtable params) throws Exception {
        IAFCotisation[] result = null;
        Object[] objResult = find(params);
        if (objResult != null) {
            result = new IAFCotisationHelper[objResult.length];
            for (int i = 0; i < objResult.length; i++) {
                GlobazValueObject cotisation = (GlobazValueObject) objResult[i];
                result[i] = new IAFCotisationHelper(cotisation);
            }
        }
        return result;
    }

    /**
     * Retourne l'id de l'adh�sion li�e Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getAdhesionId() {
        return (java.lang.String) _getValueObject().getProperty("adhesionId");
    }

    /**
     * Retourne une liste de taux Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return
     */
    @Override
    public java.lang.String getAffilieAF(String idAffiliation, String dateDebut, String dateFin) throws Exception {

        return (String) _getObject("getAffilieAF", new Object[] { idAffiliation, dateDebut, dateFin });
    }

    /**
     * Retourne l'id de l'assurance li�e Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getAssuranceId() {
        return (java.lang.String) _getValueObject().getProperty("assuranceId");
    }

    /**
     * Retourne le canton de l'affili� Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return
     */
    @Override
    public java.lang.String getCantonAffilie(String idAffiliation) throws Exception {

        return (String) _getObject("getCantonAffilie", new Object[] { idAffiliation });
    }

    @Override
    public String getCotisationAF(String IdAffiliation, String dateValidite) throws Exception {
        return (String) _getObject("getLibelleCourtCotisationAF", new Object[] { IdAffiliation, dateValidite });
    }

    /**
     * Retourne la date de d�but de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getDateDebut() {
        return (java.lang.String) _getValueObject().getProperty("dateDebut");
    }

    /**
     * Retourne la date de fin de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getDateFin() {
        return (java.lang.String) _getValueObject().getProperty("dateFin");
    }

    /**
     * Retourne le montant de la masse salariale de la p�riodicit� Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getMassePeriodicite() {
        return (java.lang.String) _getValueObject().getProperty("massePeriodicite");
    }

    /**
     * /** Retourne le montant annuel de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getMontantAnnuel() {
        return (java.lang.String) _getValueObject().getProperty("montantAnnuel");
    }

    /**
     * Retourne le montant annuel de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getMontantMensuel() {
        return (java.lang.String) _getValueObject().getProperty("montantMensuel");
    }

    /**
     * Retourne le montant semestriel de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getMontantSemestriel() {
        return (java.lang.String) _getValueObject().getProperty("montantSemestriel");
    }

    /**
     * Retourne le montant trimestriel de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getMontantTrimestriel() {
        return (java.lang.String) _getValueObject().getProperty("montantTrimestriel");
    }

    /**
     * Retourne le code syst�me du motif de fin de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getMotifFin() {
        return (java.lang.String) _getValueObject().getProperty("motifFin");
    }

    /**
     * Retourne le code syst�me de la p�riodicit� de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getPeriodicite() {
        return (java.lang.String) _getValueObject().getProperty("periodicite");
    }

    /**
     * Retourne l'id du plan de l'affiliation li�e Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getPlanAffiliationId() {
        return (java.lang.String) _getValueObject().getProperty("planAffiliationId");
    }

    /**
     * Retourne l'id du plan de caisse li�e Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getPlanCaisseId() {
        return (java.lang.String) _getValueObject().getProperty("planCaisseId");
    }

    /**
     * D�finit l'id de l'adh�sion li�e Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public void setAdhesionId(java.lang.String newAdhesionId) {
        _getValueObject().setProperty("adhesionId", newAdhesionId);
    }

    /**
     * D�finit l'id de l'assurance li�e Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public void setAssuranceId(java.lang.String newAssuranceId) {
        _getValueObject().setProperty("assuranceId", newAssuranceId);
    }

    /**
     * D�finit la date de d�but de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public void setDateDebut(java.lang.String newDateDebut) {
        _getValueObject().setProperty("dateDebut", newDateDebut);
    }

    /**
     * D�finit la date de fin de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public void setDateFin(java.lang.String newDateFin) {
        _getValueObject().setProperty("dateFin", newDateFin);
    }

    /**
     * D�finit le montant de la masse salariale de la p�riodicit� Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public void setMassePeriodicite(java.lang.String newMassePeriodicit) {
        _getValueObject().setProperty("massePeriodicite", newMassePeriodicit);
    }

    /**
     * D�finit le montant annuel de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public void setMontantAnnuel(java.lang.String newMontantAnnuel) {
        _getValueObject().setProperty("montantAnnuel", newMontantAnnuel);
    }

    /**
     * D�finit le montant annuel de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public void setMontantMensuel(java.lang.String newMontantMensuel) {
        _getValueObject().setProperty("montantMensuel", newMontantMensuel);
    }

    /**
     * D�finit le montant semestriel de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public void setMontantSemestriel(java.lang.String newMontantSemestriel) {
        _getValueObject().setProperty("montantSemestriel", newMontantSemestriel);
    }

    /**
     * D�finit le montant trimestriel de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public void setMontantTrimestriel(java.lang.String newMontantTrimestriel) {
        _getValueObject().setProperty("montantTrimestriel", newMontantTrimestriel);
    }

    /**
     * D�finit le code syst�me du motif de fin de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public void setMotifFin(java.lang.String newMotifFin) {
        _getValueObject().setProperty("motifFin", newMotifFin);
    }

    /**
     * D�finit le code syst�me de la p�riodicit� de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public void setPeriodicite(java.lang.String newPeriodicite) {
        _getValueObject().setProperty("periodicite", newPeriodicite);
    }

    /**
     * D�finit l'id du plan de l'affiliation li�e Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public void setPlanAffiliationId(java.lang.String newPlanAffiliationId) {
        _getValueObject().setProperty("planAffiliationId", newPlanAffiliationId);
    }

    /**
     * D�finit l'id du plan de caisse li�e Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public void setPlanCaisseId(java.lang.String newPlanCaisseId) {
        _getValueObject().setProperty("planCaisseId", newPlanCaisseId);
    }

}
