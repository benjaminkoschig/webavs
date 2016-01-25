package globaz.tucana.db.parametrage;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.tucana.db.parametrage.access.TUCategorieRubriqueManager;

/**
 * Repr�sentation de la classe cat�gorie rubrique manager
 * 
 * @author fgo date de cr�ation : 22 juin 06
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
     * Instancie un objet �tendant BEntity
     * 
     * @return BEntity un objet rep�sentant le r�sultat
     * @throws Exception
     *             la cr�ation a �chou�e
     */
    @Override
    public BEntity _newEntity() throws Exception {
        return new TUCategorieRubriqueViewBean();
    }

}
