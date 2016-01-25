package globaz.pavo.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.pavo.api.ICIAnnonceSuspens;

/**
 * Classe helper d'une interface d'API
 * 
 * @author EFLCreateAPITool
 */
public class ICIAnnonceSuspensHelper extends GlobazHelper implements ICIAnnonceSuspens {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur du type ICIAnnonceSuspensHelper
     */
    public ICIAnnonceSuspensHelper() {
        super("globaz.pavo.db.compte.CIAnnonceSuspens");
    }

    /**
     * Constructeur du type ICIAnnonceSuspensHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public ICIAnnonceSuspensHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /**
     * Constructeur du type ICIAnnonceSuspensHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public ICIAnnonceSuspensHelper(String implementationClassName) {
        super(implementationClassName);
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    @Override
    public String getAnnonceSuspensId() {
        return (String) _getValueObject().getProperty("annonceSuspensId");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 15:58:57)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getCodeApplication() {
        return (java.lang.String) _getValueObject().getProperty("codeApplication");
    }

    @Override
    public String getDateReception() {
        return (String) _getValueObject().getProperty("dateReception");
    }

    @Override
    public String getIdAnnonce() {
        return (String) _getValueObject().getProperty("idAnnonce");
    }

    /**
     * Retourne l'id du CI associé à l'annonce s'il existe au registre des assurés. Date de création : (09.12.2002
     * 11:15:34)
     * 
     * @return l'id du CI ou null si non trouvé.
     */
    @Override
    public String getIdCIRA() {
        return (String) _getValueObject().getProperty("idCIRA");
    }

    public String getIdLog() {
        return (String) _getValueObject().getProperty("idLog");
    }

    @Override
    public String getIdMotifArc() {
        return (String) _getValueObject().getProperty("idMotifArc");
    }

    @Override
    public String getIdTypeTraitement() {
        return (String) _getValueObject().getProperty("idTypeTraitement");
    }

    @Override
    public String getNumeroAvs() {
        return (String) _getValueObject().getProperty("numeroAvs");
    }

    @Override
    public String getNumeroCaisse() {
        return (String) _getValueObject().getProperty("numeroCaisse");
    }

    @Override
    public void setAnnonceSuspens(Boolean newAnnonceSuspens) {
        _getValueObject().setProperty("annonceSuspens", newAnnonceSuspens);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */
    @Override
    public void setAnnonceSuspensId(String newAnnonceSuspensId) {
        _getValueObject().setProperty("annonceSuspensId", newAnnonceSuspensId);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 15:58:57)
     * 
     * @param newCodeApplication
     *            java.lang.String
     */
    @Override
    public void setCodeApplication(java.lang.String newCodeApplication) {
        _getValueObject().setProperty("codeApplication", newCodeApplication);
    }

    @Override
    public void setDateReception(String newDateReception) {
        _getValueObject().setProperty("dateReception", newDateReception);
    }

    @Override
    public void setIdAnnonce(String newIdAnnonce) {
        _getValueObject().setProperty("idAnnonce", newIdAnnonce);
    }

    public void setIdLog(String newIdLog) {
        _getValueObject().setProperty("idLog", newIdLog);
    }

    @Override
    public void setIdMotifArc(String newIdMotifArc) {
        _getValueObject().setProperty("idMotifArc", newIdMotifArc);
    }

    public void setIdTypeTraitement(String newIdTypeTraitement) {
        _getValueObject().setProperty("idTypeTraitement", newIdTypeTraitement);
    }

    @Override
    public void setNumeroAvs(String newNumeroAvs) {
        _getValueObject().setProperty("numeroAvs", newNumeroAvs);
    }

    @Override
    public void setNumeroCaisse(String newNumeroCaisse) {
        _getValueObject().setProperty("numeroCaisse", newNumeroCaisse);
    }
}
