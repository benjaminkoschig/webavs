/*
 * Créé le 7 oct. 05
 */
package globaz.ij.servlet;

import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.ij.db.lots.IJLot;
import globaz.ij.itext.IJDecomptes;
import globaz.ij.vb.lots.IJLotViewBean;
import globaz.ij.vb.process.IJGenererDecomptesViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.ged.client.JadeGedFacade;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJGenererDecomptesAction extends IJDefaultProcessAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJGenererDecomptesAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public IJGenererDecomptesAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Redéfinie pour permettre d'afficher une erreur à l'affichage de la page si le lot n'est pas compensé
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(action, mainDispatcher.getPrefix());
            viewBean.setISession(mainDispatcher.getSession());

            // on lui donne les parametres en requete au cas ou.
            JSPUtils.setBeanProperties(request, viewBean);
            session.setAttribute(FWServlet.VIEWBEAN, viewBean);

            IJGenererDecomptesViewBean gcViewBean = (IJGenererDecomptesViewBean) viewBean;

            if (JadeGedFacade.isInstalled()) {
                List l = JadeGedFacade.getDocumentNamesList();
                for (Iterator iterator = l.iterator(); iterator.hasNext();) {
                    String s = (String) iterator.next();

                    if (s != null
                            && (s.startsWith(IJDecomptes.class.getName()) || s
                                    .startsWith(IPRConstantesExternes.DECOMPTE_IJ))) {

                        gcViewBean.setDisplaySendToGed("1");
                        break;
                    } else {
                        gcViewBean.setDisplaySendToGed("0");
                    }
                }
            }

            /*---------------*/
            // TODO virer ce bloc qd on pourra passer des paramètres autre que
            // l'id dans le menu d'option

            if (JadeStringUtil.isIntegerEmpty(gcViewBean.getCsEtatLot())) { // si
                // on
                // n'a
                // pas
                // l'état
                // du
                // lot,
                // il
                // faut
                // aller
                // le

                // rechercher. 2 solutions : soit le VB en session
                // est un lot (si on arrive par les options du DE,
                // et au cas ou, si ça ne marche pas, on va le
                // rechercher dans la base

                FWViewBeanInterface viewBeanDansSession = (FWViewBeanInterface) session.getAttribute("viewBean");

                if ((viewBeanDansSession != null) && (viewBeanDansSession instanceof IJLotViewBean)
                        && ((IJLotViewBean) viewBeanDansSession).getIdLot().equals(gcViewBean.getIdLot())) {
                    gcViewBean.setCsEtatLot(((IJLotViewBean) viewBeanDansSession).getCsEtat());
                } else {
                    // On doit aller le chercher dans la base

                    IJLot lot = new IJLot();
                    lot.setSession(gcViewBean.getSession());
                    lot.setIdLot(gcViewBean.getIdLot());
                    lot.retrieve();
                    gcViewBean.setCsEtatLot(lot.getCsEtat());
                }
            }

            session.setAttribute("viewBean", gcViewBean);
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }
}
