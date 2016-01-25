package globaz.pavo.db.compte;

import globaz.framework.bean.FWListViewBeanInterface;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CIRassemblementOuvertureEcrituresListViewBean extends CIRassemblementOuvertureEcrituresManager implements
        FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CIRassemblementOuvertureEcrituresListViewBean() {
        super();
        // on active le control pour la sécurité CI par rapport à une
        // affiliation
        setCacherEcritureProtege(1);// 1=true, 0=false
        wantCallMethodBeforeFind(true);
    }

}
