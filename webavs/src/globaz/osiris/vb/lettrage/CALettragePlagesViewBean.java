package globaz.osiris.vb.lettrage;

import globaz.globall.db.BSpy;
import globaz.osiris.application.CAApplication;
import globaz.pyxis.util.TIViewBeanSupport;

public class CALettragePlagesViewBean extends TIViewBeanSupport {

    public BSpy getSpy() {
        return null;
    }

    public String selectedWhenSingleTypeAffilie() {
        String plusieursType = CAApplication.getApplicationOsiris().getProperty("plusieursTypeAffilie");
        if ("TRUE".equalsIgnoreCase(plusieursType)) {
            return "";
        } else {
            return "selected";
        }

    }

}
