/*
 * Cr?? le 16 ao?t 05
 * 
 * Pour changer le mod?le de ce fichier g?n?r?, allez ? : Fen?tre&gt;Pr?f?rences&gt;Java&gt;G?n?ration de code&gt;Code
 * et commentaires
 */
package globaz.phenix.db.communications;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * @author mmu
 * 
 *         Pour changer le mod?le de ce commentaire de type g?n?r?, allez ? :
 *         Fen?tre&gt;Pr?f?rences&gt;Java&gt;G?n?ration de code&gt;Code et commentaires
 */
public class CPParametrePlausibiliteListViewBean extends CPParametrePlausibiliteManager implements
        FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CPParametrePlausibiliteViewBean();
    }

}
