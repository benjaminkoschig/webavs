package globaz.hermes.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.hermes.api.IHELotViewBean;

/**
 * Classe helper d'une interface d'API représentant un lot
 * 
 * @author EFLCreateAPITool
 */
public class IHELotViewBeanHelper extends GlobazHelper implements IHELotViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur du type IHELotViewBeanHelper
     */
    public IHELotViewBeanHelper() {
        super("globaz.hermes.db.gestion.HELotViewBean");
    }

    /**
     * Constructeur du type IHELotViewBeanHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public IHELotViewBeanHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /**
     * Constructeur du type IHELotViewBeanHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public IHELotViewBeanHelper(String implementationClassName) {
        super(implementationClassName);
    }

    /**
     * Renvoit le type du lot (Envoi/réception)
     * 
     * @return String le type
     */
    @Override
    public String getCsTypeLibelle() {
        return (String) _getValueObject().getProperty("csTypeLibelle");
    }

    /**
     * Renvoit la date d'envoi
     * 
     * @return String la date
     */
    @Override
    public String getDateEnvoi() {
        return (String) _getValueObject().getProperty("dateEnvoi");
    }

    /**
     * Renvoit l'heure d'envoi
     * 
     * @return String l'heure
     */
    @Override
    public String getHeureEnvoi() {
        return (String) _getValueObject().getProperty("heureEnvoi");
    }

    /**
     * Retourne la PK du lot
     * 
     * @return String la PK
     */
    @Override
    public String getIdLot() {
        return (String) _getValueObject().getProperty("idLot");
    }

    /**
     * Renvoit la valeur de la quittance o/n
     * 
     * @return String Oui ou Non
     */
    @Override
    public String getQuittance() {
        return (String) _getValueObject().getProperty("quittance");
    }

    /**
     * Renvoit le type du lot (Envoi/réception)
     * 
     * @return String le type
     */
    @Override
    public String getType() {
        return (String) _getValueObject().getProperty("type");
    }

    /**
     * Renvoit le nom de l'utilisateur
     * 
     * @return String l'utilisateur
     */
    @Override
    public String getUtilisateur() {
        return (String) _getValueObject().getProperty("utilisateur");
    }

    /**
     * Fixe la date d'envoi
     * 
     * @param String
     *            la nouvelle date
     */
    @Override
    public void setDateEnvoi(String newDateEnvoi) {
        _getValueObject().setProperty("dateEnvoi", newDateEnvoi);
    }

    /**
     * Fixe l'heure d'envoi
     * 
     * @param String
     *            la nouvelle heure
     */
    @Override
    public void setHeureEnvoi(String newHeureEnvoi) {
        _getValueObject().setProperty("heureEnvoi", newHeureEnvoi);
    }

    /**
     * Fixe la pk du lot
     * 
     * @param String
     *            la nouvelle pk
     */
    @Override
    public void setIdLot(String newIdLot) {
        _getValueObject().setProperty("idLot", newIdLot);
    }

    /**
     * Fixe la quittance
     * 
     * @param String
     *            la nouvelle quittance
     */
    @Override
    public void setQuittance(String newQuittance) {
        _getValueObject().setProperty("quittance", newQuittance);
    }

    /**
     * Fixe le type (Envoi/Réception)
     * 
     * @param String
     *            le type
     */
    @Override
    public void setType(String newType) {
        _getValueObject().setProperty("type", newType);
    }

    /**
     * Fixe l'utilisateur
     * 
     * @param String
     *            l'utilisateur
     */
    @Override
    public void setUtilisateur(String newUtilisateur) {
        _getValueObject().setProperty("utilisateur", newUtilisateur);
    }
}
