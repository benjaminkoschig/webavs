package globaz.pavo.print.itext;

import globaz.framework.printing.itext.fill.FWIImportParametre;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CIItextParam extends FWIImportParametre {
    public static final String getFooter(int pos) {
        return "P_FOOTER_" + pos;
    }

    public static final String getHeader(int pos) {
        return "P_HEAD_" + pos;
    }

    public static final String getHeaderLabel(int pos) {
        return "L_HEAD_" + pos;
    }

    public static final String getSummary(int pos) {
        return "P_SUMMARY_" + pos;
    }

}
