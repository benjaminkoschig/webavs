package globaz.apg.servlet;

import globaz.apg.db.lots.APLot;
import globaz.apg.itext.decompte.APDecompteGenerationProcess;
import globaz.apg.vb.lots.APLotViewBean;
import globaz.apg.vb.process.APGenererDecisionAPATViewBean;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
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
 * @author eniv
 */
public class APGenererDecisionAPATAction extends APDefaultProcessAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APGenererDecomptesAction.
     *
     * @param servlet
     *            DOCUMENT ME!
     */
    public APGenererDecisionAPATAction(final FWServlet servlet) {
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
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAfficher(final HttpSession session, final HttpServletRequest request,
                                  final HttpServletResponse response, final FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            final FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            final FWViewBeanInterface viewBean = FWViewBeanActionFactory
                    .newInstance(action, mainDispatcher.getPrefix());
            viewBean.setISession(mainDispatcher.getSession());

            // on lui donne les parametres en requete au cas ou.
            JSPUtils.setBeanProperties(request, viewBean);
            session.setAttribute("viewBean", viewBean);

            final APGenererDecisionAPATViewBean gcViewBean = (APGenererDecisionAPATViewBean) viewBean;

            final String processClassName = APDecompteGenerationProcess.class.getName();
            gcViewBean.setDisplaySendToGed("0");
            if (JadeGedFacade.isInstalled()) {
                final List<String> l = JadeGedFacade.getDocumentNamesList();
                for (final Iterator<String> iterator = l.iterator(); iterator.hasNext();) {
                    final String s = iterator.next();
                    if ((s != null)
                            && (s.startsWith(processClassName) || s
                            .startsWith(IPRConstantesExternes.DECOMPTE_APG_NORMAL))) {
                        gcViewBean.setDisplaySendToGed("1");
                        break;
                    }
                }
            }

            if (JadeStringUtil.isIntegerEmpty(gcViewBean.getEtatLot())) {

                final FWViewBeanInterface viewBeanDansSession = (FWViewBeanInterface) session.getAttribute("viewBean");

                if ((viewBeanDansSession != null) && (viewBeanDansSession instanceof APLotViewBean)
                        && ((APLotViewBean) viewBeanDansSession).getIdLot().equals(gcViewBean.getIdLot())) {
                    gcViewBean.setEtatLot(((APLotViewBean) viewBeanDansSession).getEtat());
                } else {
                    // On doit aller le chercher dans la base

                    final APLot lot = new APLot();
                    lot.setSession(gcViewBean.getSession());
                    lot.setIdLot(gcViewBean.getIdLot());
                    lot.retrieve();
                    gcViewBean.setEtatLot(lot.getEtat());
                }
            }
            /*---------------*/

            session.setAttribute("viewBean", gcViewBean);
        } catch (final Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }
}
