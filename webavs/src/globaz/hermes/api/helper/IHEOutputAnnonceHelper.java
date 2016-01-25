package globaz.hermes.api.helper;

import globaz.globall.shared.GlobazValueObject;
import globaz.hermes.api.IHEOutputAnnonce;

/**
 * Classe helper d'une interface d'API
 * 
 * @author EFLCreateAPITool
 */
public class IHEOutputAnnonceHelper extends IHEAnnoncesViewBeanHelper implements IHEOutputAnnonce {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur du type IHEOutputAnnonceHelper
     */
    public IHEOutputAnnonceHelper() {
        super("globaz.hermes.db.gestion.HEOutputAnnonceViewBean");
    }

    /**
     * Constructeur du type IHEOutputAnnonceHelper
     * 
     * @param valueObject
     *            le Value Object contenant les donn�es
     */
    public IHEOutputAnnonceHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /**
     * Constructeur du type IHEOutputAnnonceHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'impl�mentation
     */
    public IHEOutputAnnonceHelper(String implementationClassName) {
        super(implementationClassName);
    }

    /*
     * V�rifie si toutes les annonces attendues pour cette annonce sont arriv�es
     * 
     * @return true si oui, false si non
     */
    @Override
    public Boolean getAllAnnonceRetour() {
        return (Boolean) _getValueObject().getProperty("allAnnonceRetour");
    }

    @Override
    public String getChampEnregistrement() {
        return (String) _getValueObject().getProperty("champEnregistrement");
    }

    /**
     * Charge l'enregistrement depuis la BD
     * 
     * @exception java.lang.Exception
     *                si le chargement a �chou�
     */
    @Override
    public Boolean getConfirmed() {
        return (Boolean) _getValueObject().getProperty("confirmed");
    }

    /**
     * Renvoit la PK de l'annonce
     * 
     * @return String la clef primaire
     */
    @Override
    public String getIdAnnonce() {
        return (String) _getValueObject().getProperty("idAnnonce");
    }

    @Override
    public String getStatut() {
        return (String) _getValueObject().getProperty("statut");
    }

}
