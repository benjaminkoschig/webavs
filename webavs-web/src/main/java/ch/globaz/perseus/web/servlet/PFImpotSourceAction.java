package ch.globaz.perseus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.perseus.vb.impotsource.PFTauxViewBean;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.perseus.business.models.impotsource.Taux;

public class PFImpotSourceAction extends PFAbstractDefaultServletAction {

    public PFImpotSourceAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        // if (viewBean instanceof PFListeImpotSourceViewBean) {
        // destination = this.getRelativeURL(request, session) + "_de.jsp";
        // // return "/perseus?userAction=perseus.impotsource.listeImpotSource.afficher";
        // } else {
        return super._getDestExecuterSucces(session, request, response, viewBean);
        // }
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String annee = null;
        String csTypeBareme = null;

        if (viewBean instanceof PFTauxViewBean) {
            PFTauxViewBean vb = (PFTauxViewBean) viewBean;

            annee = vb.getAnnee();
            csTypeBareme = vb.getCsTypebareme();

            vb.setAnnee(annee);
            vb.setCsTypebareme(csTypeBareme);

            return getActionFullURL() + ".afficher&annee=" + annee + "&bareme=" + csTypeBareme;
        } else {
            return super._getDestModifierSucces(session, request, response, viewBean);
        }

    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PFTauxViewBean) {
            PFTauxViewBean vb = (PFTauxViewBean) viewBean;

            String annee = request.getParameter("annee");
            String bareme = request.getParameter("bareme");

            vb.setAnnee(annee);
            vb.setCsTypebareme(bareme);
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }

    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PFTauxViewBean) {
            PFTauxViewBean vb = (PFTauxViewBean) viewBean;

            // Déclaration d'une Map pour parcourir chaque taux contenantt les clés de chaque trancheSalaire /
            // nombrePersonne
            Map<String, Taux> map = vb.getTauxImposition();

            for (Object object : request.getParameterMap().keySet()) {
                String champ = object.toString();

                // Détection des éléments existants
                if (champ.startsWith("taux_")) {
                    // Création d'indices des éléments séparés par le caratère "_"
                    String str[] = champ.split("_");

                    // Récupération des éléments dans les indices 1 et 2
                    String idTrancheSalaire = str[1];
                    String idBareme = str[2];

                    String key = idTrancheSalaire + "," + idBareme;
                    if (map.containsKey(key)) {
                        map.get(key).getSimpleTaux().setValeurTaux(request.getParameter(champ));
                    } else {
                        if (!JadeStringUtil.isEmpty(request.getParameter(champ))
                                && (Float.valueOf(request.getParameter(champ)) > 0)) {
                            Taux taux = new Taux();
                            taux.getTrancheSalaire().setId(idTrancheSalaire);
                            taux.getSimpleBareme().setId(idBareme);
                            taux.getSimpleTaux().setValeurTaux(request.getParameter(champ));
                            map.put(key, taux);
                        }
                    }
                }
            }
        }
        return super.beforeModifier(session, request, response, viewBean);
    }
}
