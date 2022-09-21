package globaz.apg.servlet;

import globaz.apg.acor.APACORBatchFilePrinter;
import globaz.apg.helpers.prestation.APPrestationHelper;
import globaz.apg.vb.prestation.APCalculACORViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APCalculACORAction extends PRDefaultAction {

    private static final String VERS_ECRAN_DE = "_de.jsp";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APCalculACORAction.
     *
     * @param servlet DOCUMENT ME!
     */
    public APCalculACORAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param session        DOCUMENT ME!
     * @param request        DOCUMENT ME!
     * @param response       DOCUMENT ME!
     * @param mainDispatcher DOCUMENT ME!
     * @param viewBean       DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws ServletException DOCUMENT ME!
     * @throws IOException      DOCUMENT ME!
     */
    public String actionCalculerACOR(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                     FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        APCalculACORViewBean caViewBean = new APCalculACORViewBean();

        viewBean = caViewBean;
        this.saveViewBean(viewBean, session);

        caViewBean.setIdDroit(getSelectedId(request));
        caViewBean.setGenreService(request.getParameter("genreService"));

        // pour savoir si l'on veut calculer uniquement les prestations standard
        // ou toutes les prestations
        if (-1 != request.getQueryString().indexOf(APPrestationHelper.CALCULER_TOUTES_LES_PRESTATIONS)) {
            caViewBean.setAppelant(APCalculACORViewBean.CALCULER_TOUTES_PREST_ACOR);
        } else {
            caViewBean.setAppelant(APCalculACORViewBean.CALCULER_PREST_ACOR);
        }

        try {
            getAction().changeActionPart("actionSetSession");
            // getAction().setRight(FWSecureConstants.ADD);
            mainDispatcher.dispatch(viewBean, getAction()); // set session

            return getRelativeURL(request, session) + APCalculACORAction.VERS_ECRAN_DE + "?"
                    + PRDefaultAction.METHOD_ADD;
        } catch (Exception e) {
            e.printStackTrace();

            caViewBean.setMsgType(FWViewBeanInterface.ERROR);
            caViewBean.setMessage(e.getMessage());

            return FWDefaultServletAction.ERROR_PAGE;
        }
    }

    /**
     * Méthode qui appelle le service Web ACOR v4
     *
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param viewBean
     * @return
     * @throws Exception
     */
    public String actionCallACORWeb(HttpSession session, HttpServletRequest request,
                                    HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        try {

            APCalculACORViewBean caViewBean = (APCalculACORViewBean) viewBean;

            if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                caViewBean.setAcorV4Web(true);
            }

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }
        this.saveViewBean(viewBean, session);

        FWAction action = FWAction.newInstance(IAPActions.ACTION_CALCUL_ACOR + "." + FWAction.ACTION_REAFFICHER);

        return this.getUserActionURL(request, action.toString());
    }

    @Deprecated
    public String actionTelechargerFichier(HttpSession session, HttpServletRequest request,
                                            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {

        APCalculACORViewBean caViewBean = (APCalculACORViewBean) viewBean;
        try {

            getAction().changeActionPart("actionSetSession");
            mainDispatcher.dispatch(viewBean, getAction()); // set session

            // creer le fichier bat

            BSession bSession = (BSession) caViewBean.getISession();
            APACORBatchFilePrinter batman = APACORBatchFilePrinter.getInstance();

            Map filesContent = new HashMap();
            batman.printBatchFile(filesContent, bSession, caViewBean.loadDroit(), PRACORConst.dossierACOR(bSession));

            caViewBean.setFilesContent(filesContent);
            caViewBean.setIsFileContent(true);
            this.saveViewBean(caViewBean, session);

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }
        return getRelativeURL(request, session) + APCalculACORAction.VERS_ECRAN_DE + "?"
                + PRDefaultAction.METHOD_ADD;
    }
}
