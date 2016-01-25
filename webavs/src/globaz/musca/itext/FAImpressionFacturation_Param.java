package globaz.musca.itext;

import globaz.framework.printing.itext.fill.FWIImportParametre;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class FAImpressionFacturation_Param extends FWIImportParametre {
    public static String getParamP(int pos) {
        return "P_" + pos;
    }
}
