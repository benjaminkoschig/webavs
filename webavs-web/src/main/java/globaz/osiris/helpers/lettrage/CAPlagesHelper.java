package globaz.osiris.helpers.lettrage;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.IFWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.vb.lettrage.CAChercherPlageViewBean;
import globaz.pyxis.util.TISQL;
import globaz.pyxis.util.TIToolBox;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CAPlagesHelper implements IFWHelper {

    /*
     * @param nb nombre de plage
     * 
     * @param list List[String]
     * 
     * @return List[String[from,to]] liste des plages
     */
    private static List _buildRange(int nb, List list) {
        if ((list == null) || (list.size() == 0)) {
            return new ArrayList();
        }

        List res = new ArrayList();
        if (list.size() < nb) {
            nb = list.size();
        }

        if (nb == 0) {
            nb = 1;
        }
        float range = (float) list.size() / (float) nb;
        int from = 0;
        for (int i = 1; i <= nb; i++) {
            int to = (int) (range * i) - 1;
            res.add(new String[] { (String) list.get(from), (String) list.get(to), (to - from) + 1 + "" });

            from = to + 1;
        }
        return res;
    }

    public static String mainQuery(String role) {
        String col = TIToolBox.getCollection();

        String req = " from " + col + "CASECTP a " + " inner join " + col
                + "CACPTAP b on (a.IDCOMPTEANNEXE = b.IDCOMPTEANNEXE) " + " inner join " + col
                + "titierp t on ( b.IDTIERS = t.htitie) " + " left outer join " + col
                + "CAEXLEP e on (a.IDSECTION = e.IDSECTION)" + " where a.SOLDE <> 0";
        if (!JadeStringUtil.isEmpty(role)) {
            req += "  and b.IDROLE IN (" + role + ")";
        }
        req +=

        " and a.CATEGORIESECTION <> 227096 " + " " + " " + " and IDEXTERNEROLE in (" + "	select IDEXTERNEROLE"
                + "		from " + col + "CASECTP a, " + col + "CACPTAP b " + "		where "
                + "			a.IDCOMPTEANNEXE = b.IDCOMPTEANNEXE ";
        if (!JadeStringUtil.isEmpty(role)) {
            req += "and b.IDROLE IN (" + role + ")";
        }
        req += "			and a.SOLDE < 0"
                + // creditrices
                " 			and a.CATEGORIESECTION <> 227096 " + ")" + " and IDEXTERNEROLE in (" + "	select IDEXTERNEROLE"
                + "		from " + col + "CASECTP a, " + col + "CACPTAP b " + "		where "
                + "			a.IDCOMPTEANNEXE = b.IDCOMPTEANNEXE ";
        if (!JadeStringUtil.isEmpty(role)) {
            req += "			and b.IDROLE IN (" + role + ")";
        }
        req += "			and a.SOLDE > 0" + // débitrices
                " 			and a.CATEGORIESECTION <> 227096 " + ")";
        return req;
    }

    /*
     * ------------------------------------------------------------------------ pyxis.test.lettrage.chercherPlages AJAX
     * QUERY, determine les plages
     */
    private void _chercherPlages(BSession session, CAChercherPlageViewBean viewBean) {
        String role = viewBean.getRole();
        String plages = viewBean.getNbPlages();

        int nbPlages = 1;
        try {
            nbPlages = Integer.parseInt(plages);
        } catch (Exception e) {// use defaut value}
            // use default value
        }

        String result = "";
        try {
            List rList = TISQL.querySingleField(session, "distinct IDEXTERNEROLE", CAPlagesHelper.mainQuery(role)
                    + " order by IDEXTERNEROLE");

            List res = CAPlagesHelper._buildRange(nbPlages, rList);
            for (Iterator it = res.iterator(); it.hasNext();) {
                String[] range = (String[]) it.next();
                result += "<li>" + range[0] + " - " + range[1] + " : " + range[2];
            }
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }

        viewBean.setResponse(new StringBuffer(result));

    }

    @Override
    public void afterExecute(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
    }

    @Override
    public void beforeExecute(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
    }

    @Override
    public FWViewBeanInterface invoke(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        String actionPart = action.getActionPart();
        if ("listerPlages".equals(actionPart)) {
            _chercherPlages((BSession) session, (CAChercherPlageViewBean) viewBean);
        }
        return viewBean;
    }

}
