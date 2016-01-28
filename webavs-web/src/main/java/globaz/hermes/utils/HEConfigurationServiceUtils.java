package globaz.hermes.utils;

import globaz.globall.db.BSession;
import globaz.hermes.db.gestion.HEConfigurationServiceListViewBean;
import globaz.hermes.db.gestion.HEConfigurationServiceViewBean;
import java.util.Iterator;

public class HEConfigurationServiceUtils {

    public static String getListReferenceInterne(BSession session, String fieldName) throws Exception {

        String newSelect = new String();
        HEConfigurationServiceListViewBean mgr = new HEConfigurationServiceListViewBean();
        mgr.setSession(session);
        mgr.find();
        if (mgr.size() > 0) {
            newSelect = "<SELECT id=\"" + fieldName + "\" name=\"" + fieldName + "\">";
            newSelect += "<OPTION value=\"";
            newSelect += " ";
            newSelect += "\"";
            newSelect += ">";
            newSelect += "</OPTION>";
            for (int i = 0; i < mgr.size(); i++) {
                HEConfigurationServiceViewBean cs = (HEConfigurationServiceViewBean) mgr.getEntity(i);
                newSelect += "<OPTION value=\"";
                newSelect += cs.getReferenceInterne();
                newSelect += "\"";
                newSelect += ">";
                newSelect += cs.getReferenceInterne();
                newSelect += "</OPTION>";
            }
            newSelect += "</SELECT>";
        }
        return newSelect;
    }

    public boolean checkReferenceInterneExiste(BSession session) throws Exception {
        boolean existe = false;
        HEConfigurationServiceListViewBean mgr = new HEConfigurationServiceListViewBean();
        mgr.setSession(session);
        mgr.find();

        if (mgr.size() > 0) {
            existe = true;
        }
        return existe;
    }

    public HEConfigurationServiceViewBean getReferencePrestation(BSession session) throws Exception {
        HEConfigurationServiceViewBean cf = null;
        HEConfigurationServiceListViewBean mgr = new HEConfigurationServiceListViewBean();
        mgr.setSession(session);
        mgr.setLikeReferenceInterne("RE");
        mgr.find();

        if (mgr.size() > 0) {
            cf = (HEConfigurationServiceViewBean) mgr.getFirstEntity();
        }

        return cf;
    }

    public boolean isCorrespondanceServiceExistant(BSession session, String ref1) throws Exception {
        boolean hasCorrespondance = false;
        HEConfigurationServiceUtils util = new HEConfigurationServiceUtils();
        boolean checkServiceExist = util.checkReferenceInterneExiste(session);

        if (checkServiceExist == true) {
            HEConfigurationServiceListViewBean listService = new HEConfigurationServiceListViewBean();
            listService.setSession(session);
            listService.find();

            for (Iterator iterator = listService.iterator(); iterator.hasNext();) {
                HEConfigurationServiceViewBean service = (HEConfigurationServiceViewBean) iterator.next();

                if (ref1.equals(service.getReferenceInterne())) {
                    hasCorrespondance = true;
                    break;
                } else {
                    hasCorrespondance = false;
                }
            }
            return hasCorrespondance;
        } else {
            return true;
        }
    }
}
