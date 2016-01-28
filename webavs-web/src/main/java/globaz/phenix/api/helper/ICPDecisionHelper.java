package globaz.phenix.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.phenix.api.ICPDecision;
import java.util.Hashtable;

/**
 * Classe helper d'une interface d'API
 * 
 * @author EFLCreateAPITool
 */
public class ICPDecisionHelper extends GlobazHelper implements ICPDecision {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur du type ICPDecisionHelper
     */
    public ICPDecisionHelper() {
        super("globaz.phenix.db.principale.CPDecision");
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type ICPDecisionHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public ICPDecisionHelper(GlobazValueObject valueObject) {
        super(valueObject);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type ICPDecisionHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public ICPDecisionHelper(java.lang.String implementationClassName) {
        super(implementationClassName);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Renvoie un tableau d'objet representant des decisions<br>
     * <br>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * critières de recherche ds la liste suivante : <br>
     * <br>
     * <li>FIND_FOR_ANNEE_DECISION <li>FIND_FOR_IS_ACTIVE <li>
     * FIND_FOR_ID_AFFILIATION <br>
     * <br>
     * <br>
     * 100 enregistrements au max sont retournés<br>
     * <br>
     * <br>
     * 
     * @return Object[]
     * @param params
     *            params
     * @exception Exception
     *                si echec
     */
    @Override
    public Object[] find(Hashtable params) throws Exception {
        Object[] result = null;
        result = _getArray("find", new Object[] { params });
        return result;
    }

    /**
     * Idem méthode Object[] find(Hashtable), mais retourne un tableau typé.
     * 
     * @return ICPDecision[]
     * @param params
     *            params
     * @exception Exception
     *                si echec
     */
    @Override
    public ICPDecision[] findDecisions(Hashtable params) throws Exception {
        ICPDecision[] result = null;
        Object[] objResult = find(params);
        if (objResult != null) {
            result = new ICPDecision[objResult.length];
            for (int i = 0; i < objResult.length; i++) {
                GlobazValueObject obj = (GlobazValueObject) objResult[i];
                result[i] = new ICPDecisionHelper(obj);
                result[i].setISession(getISession());
            }
        }
        return result;
    }

    // Getter
    @Override
    public java.lang.String getAnneeDecision() {
        return (java.lang.String) _getValueObject().getProperty("anneeDecision");
    }

    @Override
    public java.lang.String getAnneePrise() {
        return (java.lang.String) _getValueObject().getProperty("anneePrise");
    }

    @Override
    public java.lang.String getDateInformation() {
        return (java.lang.String) _getValueObject().getProperty("dateInformation");
    }

    @Override
    public java.lang.String getDebutDecision() {
        return (java.lang.String) _getValueObject().getProperty("debutDecision");
    }

    @Override
    public String getDescriptionDecision() {
        return (java.lang.String) _getValueObject().getProperty("descriptionDecision");
    }

    @Override
    public java.lang.String getFinDecision() {
        return (java.lang.String) _getValueObject().getProperty("finDecision");
    }

    @Override
    public java.lang.String getGenreAffilie() {
        return (java.lang.String) _getValueObject().getProperty("genreAffilie");
    }

    @Override
    public java.lang.String getIdAffiliation() {
        return (java.lang.String) _getValueObject().getProperty("idAffiliation");
    }

    @Override
    public java.lang.String getIdCommunication() {
        return (java.lang.String) _getValueObject().getProperty("idCommunication");
    }

    @Override
    public java.lang.String getIdConjoint() {
        return (java.lang.String) _getValueObject().getProperty("idConjoint");
    }

    @Override
    public java.lang.String getIdDecision() {
        return (java.lang.String) _getValueObject().getProperty("idDecision");
    }

    @Override
    public java.lang.String getIdIfdDefinitif() {
        return (java.lang.String) _getValueObject().getProperty("idIfdDefinitif");
    }

    @Override
    public java.lang.String getIdIfdProvisoire() {
        return (java.lang.String) _getValueObject().getProperty("idIfdProvisoire");
    }

    @Override
    public java.lang.String getIdPassage() {
        return (java.lang.String) _getValueObject().getProperty("idPassage");
    }

    @Override
    public java.lang.String getIdTiers() {
        return (java.lang.String) _getValueObject().getProperty("idTiers");
    }

    @Override
    public java.lang.String getNumIfd() {
        return (java.lang.String) _getValueObject().getProperty("numIfd");
    }

    @Override
    public java.lang.String getPeriodicite() {
        return (java.lang.String) _getValueObject().getProperty("periodicite");
    }

    @Override
    public java.lang.String getResponsable() {
        return (java.lang.String) _getValueObject().getProperty("responsable");
    }

    @Override
    public java.lang.String getTaxation() {
        return (java.lang.String) _getValueObject().getProperty("taxation");
    }

    @Override
    public java.lang.String getTypeDecision() {
        return (java.lang.String) _getValueObject().getProperty("typeDecision");
    }

    // Setter
    @Override
    public void setAnneeDecision(java.lang.String newAnneeDecision) {
        _getValueObject().setProperty("anneeDecision", newAnneeDecision);
    }

    @Override
    public void setAnneePrise(java.lang.String newAnneePrise) {
        _getValueObject().setProperty("anneePrise", newAnneePrise);
    }

    @Override
    public void setAssujetissement(Boolean newAssujetissement) {
        _getValueObject().setProperty("assujetissement", newAssujetissement);
    }

    @Override
    public void setBloque(Boolean newBloque) {
        _getValueObject().setProperty("bloque", newBloque);
    }

    @Override
    public void setDateInformation(java.lang.String newDateInformation) {
        _getValueObject().setProperty("dateInformation", newDateInformation);
    }

    @Override
    public void setDebutDecision(java.lang.String newDebutDecision) {
        _getValueObject().setProperty("debutDecision", newDebutDecision);
    }

    @Override
    public void setFacturation(Boolean newFacturation) {
        _getValueObject().setProperty("facturation", newFacturation);
    }

    @Override
    public void setFinDecision(java.lang.String newFinDecision) {
        _getValueObject().setProperty("finDecision", newFinDecision);
    }

    @Override
    public void setGenreAffilie(java.lang.String newGenreAffilie) {
        _getValueObject().setProperty("genreAffilie", newGenreAffilie);
    }

    @Override
    public void setIdAffiliation(java.lang.String newIdAffiliation) {
        _getValueObject().setProperty("idAffiliation", newIdAffiliation);
    }

    @Override
    public void setIdCommunication(java.lang.String newIdCommunication) {
        _getValueObject().setProperty("idCommunication", newIdCommunication);
    }

    @Override
    public void setIdConjoint(java.lang.String newIdConjoint) {
        _getValueObject().setProperty("idConjoint", newIdConjoint);
    }

    @Override
    public void setIdDecision(java.lang.String newIdDecision) {
        _getValueObject().setProperty("idDecision", newIdDecision);
    }

    @Override
    public void setIdIfdDefinitif(java.lang.String newIdIfdDefinitif) {
        _getValueObject().setProperty("idIfdDefinitif", newIdIfdDefinitif);
    }

    @Override
    public void setIdIfdProvisoire(java.lang.String newIdIfdProvisoire) {
        _getValueObject().setProperty("idIfdProvisoire", newIdIfdProvisoire);
    }

    @Override
    public void setIdPassage(java.lang.String newIdPassage) {
        _getValueObject().setProperty("idPassage", newIdPassage);
    }

    @Override
    public void setIdTiers(java.lang.String newIdTiers) {
        _getValueObject().setProperty("idTiers", newIdTiers);
    }

    @Override
    public void setImpression(Boolean newImpression) {
        _getValueObject().setProperty("impression", newImpression);
    }

    @Override
    public void setInteret(Boolean newInteret) {
        _getValueObject().setProperty("interet", newInteret);
    }

    @Override
    public void setOpposition(Boolean newOpposition) {
        _getValueObject().setProperty("opposition", newOpposition);
    }

    @Override
    public void setResponsable(java.lang.String newResponsable) {
        _getValueObject().setProperty("responsable", newResponsable);
    }

    @Override
    public void setTaxation(java.lang.String newTaxation) {
        _getValueObject().setProperty("taxation", newTaxation);
    }

    @Override
    public void setTypeDecision(java.lang.String newTypeDecision) {
        _getValueObject().setProperty("typeDecision", newTypeDecision);
    }
}
