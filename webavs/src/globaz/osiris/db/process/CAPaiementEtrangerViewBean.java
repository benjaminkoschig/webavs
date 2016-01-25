package globaz.osiris.db.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.osiris.process.CAProcessPaiementEtranger;

/**
 * Classe : type_conteneur Description : Date de création: 2 juin 04
 * 
 * @author scr
 */
public class CAPaiementEtrangerViewBean extends CAProcessPaiementEtranger implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CAPaiementEtrangerViewBean() {
        super();
    }

    /**
     * Constructor for CAPaiementEtrangerViewBean.
     */
    public CAPaiementEtrangerViewBean(BProcess parent) {
        super(parent);
    }

}
