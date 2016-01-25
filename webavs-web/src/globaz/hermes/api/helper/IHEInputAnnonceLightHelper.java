package globaz.hermes.api.helper;

import globaz.globall.shared.GlobazValueObject;
import globaz.hermes.api.IHEInputAnnonceLight;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class IHEInputAnnonceLightHelper extends IHEAnnoncesViewBeanHelper implements IHEInputAnnonceLight {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur du type IHEInputAnnonceHelper
     */
    public IHEInputAnnonceLightHelper() {
        super("globaz.hermes.db.gestion.HEInputAnnonceLightViewBean");
    }

    /**
     * Constructeur du type IHEInputAnnonceHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public IHEInputAnnonceLightHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /**
     * Constructeur du type IHEInputAnnonceHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public IHEInputAnnonceLightHelper(String implementationClassName) {
        super(implementationClassName);
    }
}
