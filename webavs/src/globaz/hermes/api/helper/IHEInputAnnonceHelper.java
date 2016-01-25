package globaz.hermes.api.helper;

import globaz.globall.shared.GlobazValueObject;
import globaz.hermes.api.IHEInputAnnonce;

/**
 * Classe helper d'une interface d'API repr�sentant une �criture d'annonce
 * 
 * @author EFLCreateAPITool
 */
public class IHEInputAnnonceHelper extends IHEAnnoncesViewBeanHelper implements IHEInputAnnonce {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur du type IHEInputAnnonceHelper
     */
    public IHEInputAnnonceHelper() {
        super("globaz.hermes.db.gestion.HEInputAnnonceViewBean");
    }

    /**
     * Constructeur du type IHEInputAnnonceHelper
     * 
     * @param valueObject
     *            le Value Object contenant les donn�es
     */
    public IHEInputAnnonceHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /**
     * Constructeur du type IHEInputAnnonceHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'impl�mentation
     */
    public IHEInputAnnonceHelper(String implementationClassName) {
        super(implementationClassName);
    }
}
