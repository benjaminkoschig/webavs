package globaz.tucana.db.parametrage;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.tucana.db.parametrage.access.TUCategorieRubriqueManager;

/**
 * Représentation de la classe catégorie rubrique manager
 * 
 * @author fgo date de création : 22 juin 06
 * @version : version 1.0
 * 
 */
public class TUCategorieRubriqueListViewBean extends TUCategorieRubriqueManager implements FWListViewBeanInterface {
    /** Table : TUBPCRU */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Instancie un objet étendant BEntity
     * 
     * @return BEntity un objet repésentant le résultat
     * @throws Exception
     *             la création a échouée
     */
    @Override
    public BEntity _newEntity() throws Exception {
        return new TUCategorieRubriqueViewBean();
    }

}
