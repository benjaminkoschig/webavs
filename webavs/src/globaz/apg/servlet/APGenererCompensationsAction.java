/*
 * Créé le 11 juil. 05
 */
package globaz.apg.servlet;

import globaz.apg.api.lots.IAPLot;
import globaz.apg.db.lots.APLot;
import globaz.apg.vb.lots.APLotViewBean;
import globaz.apg.vb.process.APGenererCompensationsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.api.IFAPassage;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APGenererCompensationsAction extends APDefaultProcessAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APGenererCompensationsAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public APGenererCompensationsAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
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

            // on lui donne les parametres en requete (pour l'état du lot si on
            // arrive de la rcListe des lots.
            JSPUtils.setBeanProperties(request, viewBean);

            String selectedId = request.getParameter("selectedId");

            APGenererCompensationsViewBean gcViewBean = (APGenererCompensationsViewBean) viewBean; // pas
            // d'exception

            // Reprise du mois de la dernière facturation périodique
            // Mise par défaut du calendarTag pour calcul des compensations

            // Il faut trouver le mois de la dernière facturation périodique de
            // MUSCA afin de setter
            // le mois suivant pour calcul des compensations (CCJU)
            IFAPassage facturationPeriodique = (IFAPassage) mainDispatcher.getSession().getAPIFor(IFAPassage.class);
            facturationPeriodique.setISession(mainDispatcher.getSession());
            facturationPeriodique.setIdPassage(facturationPeriodique.getIdDernierPassage());
            facturationPeriodique.retrieve(null);

            String mois = "";
            try {
                mois = facturationPeriodique.getDatePeriode().substring(0, 2);
            } catch (Exception e) {
                // On prend le mois courant
                mois = String.valueOf(new JADate(JACalendar.todayJJsMMsAAAA()).getMonth());
            }

            // Ajouter 1 au mois, ou 1 à l'année, puis reconstruire la chaîne
            if (mois != "12") {
                int moisInt = Integer.parseInt(mois);
                moisInt++;
                if (moisInt < 10) {
                    mois = "0" + moisInt;
                } else {
                    mois = moisInt + "";
                }
            } else {
                mois = "01";
            }

            gcViewBean.setMoisPeriodeFacturation(mois); // possible

            if (selectedId != null) { // l'id sera null si on arrive du menu
                // principal. Si on arrive du menu
                // d'option
                // des rcListes ou du menu d'option classique, l'id sera
                // présent.
                gcViewBean.setForIdLot(selectedId);

                /*---------------*/
                // TODO virer ce bloc qd on pourra passer des paramètres autre
                // que l'id dans le menu d'option

                if (JadeStringUtil.isIntegerEmpty(gcViewBean.getEtatLot())) { // si
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

                    if ((viewBeanDansSession != null) && (viewBeanDansSession instanceof APLotViewBean)
                            && ((APLotViewBean) viewBeanDansSession).getIdLot().equals(selectedId)) {
                        gcViewBean.setEtatLot(((APLotViewBean) viewBeanDansSession).getEtat());
                    } else {
                        // On doit aller le chercher dans la base
                        APLot lot = new APLot();
                        lot.setSession(gcViewBean.getSession());
                        lot.setIdLot(selectedId);
                        lot.retrieve();
                        gcViewBean.setEtatLot(lot.getEtat());
                    }
                }
                /*---------------*/
            }

            if (!(gcViewBean.getEtatLot().equals(IAPLot.CS_OUVERT)
                    || gcViewBean.getEtatLot().equals(IAPLot.CS_COMPENSE) || JadeStringUtil.isIntegerEmpty(gcViewBean
                    .getEtatLot()))) {
                gcViewBean._addError("GENERATION_COMPENSATIONS_IMPOSSIBLE");
            }

            session.setAttribute("viewBean", gcViewBean);
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }
}
