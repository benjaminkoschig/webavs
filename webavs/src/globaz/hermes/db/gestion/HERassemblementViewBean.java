package globaz.hermes.db.gestion;

import globaz.commons.nss.NSUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.hermes.db.access.HERassemblement;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class HERassemblementViewBean extends HERassemblement implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for HERassemblementViewBean.
     */
    public HERassemblementViewBean() {
        super();
    }

    public String getNumAvsWithDots() {
        /*
         * String numAvs = getNumAVS(); if (numAvs.length() > 8) { numAvs = numAvs.substring(0, 3) + "." +
         * numAvs.substring(3, 5) + "." + numAvs.substring(5, 8) + "." + numAvs.substring(8, 11); } else { numAvs =
         * numAvs.substring(0, 3) + "." + numAvs.substring(3, 5) + "." + numAvs.substring(5, 8); }
         */
        return NSUtil.formatAVSUnknown(getNumAVS());
    }
}
