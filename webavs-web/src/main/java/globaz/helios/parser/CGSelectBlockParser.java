package globaz.helios.parser;

import globaz.globall.db.BSession;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.comptes.CGMandatManager;
import globaz.jade.client.util.JadeStringUtil;

public class CGSelectBlockParser {

    public static String getMandatSelectBlock(BSession session, String idMandat, boolean noIdAllowed,
            String jsFunctionName) {
        CGMandatManager manager = new CGMandatManager();
        manager.setSession(session);

        try {
            manager.find();

            if (manager.hasErrors() || (manager.size() == 0)) {
                return "";
            }

            if (manager.size() == 1) {
                String oneLine = "<input type=\"text\" name=\"idMandatLibelle\" class=\"libelleLongDisabled\" value=\""
                        + ((CGMandat) manager.getFirstEntity()).getLibelle() + "\"/>";
                oneLine += "<input type=\"hidden\" name=\"idMandat\" value=\""
                        + ((CGMandat) manager.getFirstEntity()).getIdMandat() + "\"/>";
                return oneLine;
            }

            String select = "<select id=\"idMandat\" name=\"idMandat\"";

            if (!JadeStringUtil.isBlank(jsFunctionName)) {
                select += " onChange=\"" + jsFunctionName + "()\"";
            }

            select += ">";

            if (noIdAllowed) {
                select += "<option value=\"\"></option>";
            }

            for (int i = 0; i < manager.size(); i++) {
                CGMandat mandat = (CGMandat) manager.get(i);

                select += "<option value=\"";
                select += mandat.getIdMandat();

                if ((idMandat != null) && (idMandat.equals(mandat.getIdMandat()))) {
                    select += "\" selected>";
                } else {
                    select += "\">";
                }
                select += mandat.getLibelle();
                select += "</option>";
            }
            select += "</select>";

            return select;
        } catch (Exception e) {
            return "";
        }
    }
}
