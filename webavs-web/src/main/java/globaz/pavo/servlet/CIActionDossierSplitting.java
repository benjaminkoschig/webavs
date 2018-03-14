package globaz.pavo.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWListViewBeanActionFactory;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.http.JSPUtils;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.db.splitting.CIDossierSplitting;
import globaz.pavo.db.splitting.CIDossierSplittingViewBean;
import globaz.pavo.db.splitting.CIImprimerAnalyseViewBean;
import globaz.pavo.db.splitting.CIImprimerApercuViewBean;

/**
 * Insérez la description du type ici. Date de création : (16.10.2002 09:56:57)
 * 
 * @author: Administrator
 */
public class CIActionDossierSplitting extends CIActionCIDefault {
    /**
     * Commentaire relatif au constructeur CIActionSplitting.
     * 
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    public CIActionDossierSplitting(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    private void _actionAfficherAnnulation(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = null;
        try {
            FWAction action = getAction();
            action.setRight(FWSecureConstants.READ);

            CIDossierSplittingViewBean viewBean = new CIDossierSplittingViewBean();
            viewBean.setId(request.getParameter("idDossierSplitting"));

            viewBean = (CIDossierSplittingViewBean) mainDispatcher.dispatch(viewBean, action);
            session.setAttribute("viewBean", viewBean);
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail&_back=sl";
            } else {
                _destination = getRelativeURLwithoutClassPart(request, session) + "annulerSplitting_de.jsp";

            }

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    private void _actionAfficherDepuisMandat(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = null;
        try {
            FWAction action = getAction();

            action.changeActionPart(FWAction.ACTION_AFFICHER);

            CIDossierSplittingViewBean viewBean = new CIDossierSplittingViewBean();
            viewBean.setId(request.getParameter("idDossierSplitting"));

            viewBean = (CIDossierSplittingViewBean) mainDispatcher.dispatch(viewBean, action);
            session.setAttribute("viewBean", viewBean);
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    private void _actionAnnulerDossier(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = null;
        try {
            FWAction action = getAction();
            action.setRight(FWSecureConstants.READ);

            CIDossierSplittingViewBean viewBean = new CIDossierSplittingViewBean();
            String idDossierSplittingToSet = "";
            if (!JadeStringUtil.isBlankOrZero(request.getParameter("idDossierSplitting"))) {
                idDossierSplittingToSet = request.getParameter("idDossierSplitting");
            } else {
                CIDossierSplittingViewBean dos = (CIDossierSplittingViewBean) session.getAttribute("viewBean");
                idDossierSplittingToSet = dos.getId();
            }
            viewBean.setId(idDossierSplittingToSet);

            viewBean = (CIDossierSplittingViewBean) mainDispatcher.dispatch(viewBean, action);

            viewBean = (CIDossierSplittingViewBean) mainDispatcher.dispatch(viewBean, action);
            session.setAttribute("viewBean", viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = getRelativeURLwithoutClassPart(request, session)
                        + "annulerSplitting_de.jsp?_valid=fail&_back=sl";
            } else {
                _destination = getAction().getApplicationPart().toString()
                        + "?userAction=pavo.splitting.dossierSplitting.afficher&selectedId="
                        + viewBean.getIdDossierSplitting();
                // _destination=
                // getRelativeURLwithoutClassPart(request,session)+"annulerSplitting_de.jsp";
            }

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    /**
     * Exécute la fonction aperçu des RCI (via Hermes).
     * 
     */
    private void _actionApercuRCI(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String destination = "/hermes?userAction=hermes.parametrage.attenteReception.chercher&typeAnnonce=8";
        FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
        try {
            FWViewBeanInterface newBean = checkViewBean(viewBean);
            if (newBean != null) {
                // le type du viewBean n'est pas valide, préparer le nouveau
                viewBean = newBean;
                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            }
            ((CIDossierSplitting) viewBean).setTypePersonne(request.getParameter("typePersonne"));
            FWAction action = getAction();
            action.setRight(FWSecureConstants.READ);
            viewBean = mainDispatcher.dispatch(viewBean, action);
            session.setAttribute("viewBean", viewBean);
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                // erreur
                destination = getRelativeURL(request, session) + "_de.jsp";
            } else {
                destination += "&referenceUnique=" + ((CIDossierSplittingViewBean) viewBean).getRefUniqueRCI();
                destination += "&idAnnonce=" + ((CIDossierSplittingViewBean) viewBean).getIdAnnonceRCI();
                destination += "&isArchivage="
                        + String.valueOf(((CIDossierSplittingViewBean) viewBean).getIsArchivage());
            }
        } catch (Exception ex) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    private void _actionImprimerAnalyseSplitting(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = null;
        try {
            // instance du bean
            CIImprimerAnalyseViewBean viewBean = (CIImprimerAnalyseViewBean) session.getAttribute("viewBean");
            JSPUtils.setBeanProperties(request, viewBean);
            FWAction action = getAction();
            action.setRight(FWSecureConstants.UPDATE);

            // appel du controlleur
            viewBean = (CIImprimerAnalyseViewBean) mainDispatcher.dispatch(viewBean, action);
            // sauve le bean dans la session
            // session.setAttribute("viewBean", viewBean);
            // redirection vers destination
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = getRelativeURLwithoutClassPart(request, session)
                        + "imprimerAnalyse_de.jsp?_valid=fail&_back=sl";
            } else {
                _destination = "/" + getAction().getApplicationPart().toString()
                        + "?userAction=pavo.splitting.dossierSplitting.afficher&selectedId="
                        + viewBean.getIdDossierSplitting();
            }

        } catch (Exception ex) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    private void _actionImprimerApercuSplitting(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = null;
        try {
            // instance du bean
            CIImprimerApercuViewBean viewBean = (CIImprimerApercuViewBean) session.getAttribute("viewBean");
            JSPUtils.setBeanProperties(request, viewBean);
            FWAction action = getAction();
            action.setRight(FWSecureConstants.UPDATE);

            // appel du controlleur
            viewBean = (CIImprimerApercuViewBean) mainDispatcher.dispatch(viewBean, action);
            // sauve le bean dans la session
            // session.setAttribute("viewBean", viewBean);
            // redirection vers destination
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = getRelativeURLwithoutClassPart(request, session)
                        + "imprimerApercu_de.jsp?_valid=fail&_back=sl";
            } else {
                _destination = "/" + getAction().getApplicationPart().toString()
                        + "?userAction=pavo.splitting.dossierSplitting.afficher&selectedId="
                        + viewBean.getIdDossierSplitting();
            }

        } catch (Exception ex) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    private void _actionOuvrir(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher mainDispatcher)
            throws javax.servlet.ServletException, java.io.IOException {
        String destination;
        FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
        try {
            FWViewBeanInterface newBean = checkViewBean(viewBean);
            if (newBean != null) {
                viewBean = newBean;
                // le type du viewBean n'est pas valide, préparer le nouveau
                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            }
            FWAction action = getAction();
            // action.setRight(FWSecureConstants.READ);
            viewBean = mainDispatcher.dispatch(viewBean, action);
            session.setAttribute("viewBean", viewBean);
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                if ("pavo.splitting.dossierSplitting.executerSplitting".equals(request.getParameter("userAction"))) {
                    destination = getRelativeURL(request, session) + "_de.jsp";
                } else {
                    destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail&_back=sl&";
                }
            } else {
                destination = getRelativeURL(request, session) + "_de.jsp";
            }
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    protected void _actionReAfficherDossier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_de.jsp")
                .forward(request, response);
    }

    private void _actionRevoquer(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher mainDispatcher)
            throws javax.servlet.ServletException, java.io.IOException {
        String _destination = null;
        try {
            FWAction action = getAction();
            action.setRight(FWSecureConstants.READ);

            CIDossierSplittingViewBean viewBean = new CIDossierSplittingViewBean();
            viewBean.setId(request.getParameter("idDossierSplitting"));

            viewBean = (CIDossierSplittingViewBean) mainDispatcher.dispatch(viewBean, action);
            session.setAttribute("viewBean", viewBean);
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail&_back=sl";
            } else {
                _destination = getRelativeURLwithoutClassPart(request, session) + "revoquerSplitting_de.jsp";

            }

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    private void _actionRouvrir(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher mainDispatcher)
            throws javax.servlet.ServletException, java.io.IOException {
        String _destination = null;
        try {
            FWAction action = getAction();
            action.setRight(FWSecureConstants.READ);

            // CIDossierSplittingViewBean dos = (CIDossierSplittingViewBean) session.getAttribute("viewBean");
            CIDossierSplittingViewBean viewBean = new CIDossierSplittingViewBean();
            viewBean.setId(request.getParameter("idDossierSplitting"));

            viewBean = (CIDossierSplittingViewBean) mainDispatcher.dispatch(viewBean, action);
            session.setAttribute("viewBean", viewBean);
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail&_back=sl";
            } else {
                _destination = getRelativeURLwithoutClassPart(request, session) + "reouvrirSplitting_de.jsp";

            }

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    /**
     * critères obligatoires (au moins 1 parmis la liste)
     */
    private boolean _criteresOk(HttpServletRequest request) {
        boolean res = false;
        String[] criteres = new String[] { "forIdDossierInterneSplitting", "fromDateOuvertureDossier",
                "forIdTiersAssure", "forIdTiersConjoint", };

        StringBuffer buf = new StringBuffer("");
        for (int i = 0; i < criteres.length; i++) {
            buf.append(request.getParameter(criteres[i]));
        }
        return !JAUtil.isStringEmpty(buf.toString());
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getActionFullURL() + ".afficher&_method=upd&_back=sl&selectedId="
                + ((CIDossierSplittingViewBean) viewBean).getIdDossierSplitting() + "&idDossierSplitting="
                + ((CIDossierSplittingViewBean) viewBean).getIdDossierSplitting();
        // return getActionFullURL()+".reAfficherDossier";
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getActionFullURL() + ".afficher";
    }

    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = null;
        try {
            /*
             * recuperation du bean depuis la session
             */
            CIDossierSplittingViewBean viewBean = (CIDossierSplittingViewBean) session.getAttribute("viewBean");
            /*
             * set automatique des proprietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /*
             * beforeAdd() call du dispatcher, puis mis en session
             */
            // viewBean.setNumeroAffilie(viewBean.getIdAffiliation());
            viewBean = (CIDossierSplittingViewBean) beforeAjouter(session, request, response, viewBean);
            viewBean = (CIDossierSplittingViewBean) mainDispatcher.dispatch(viewBean, getAction());

            session.setAttribute("viewBean", viewBean);
            /*
             * chois de la destination _valid=fail : revient en mode edition _back=sl : sans effacer les champs deja
             * rempli par l'utilisateur
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = _getDestEchec(session, request, response, viewBean);
            } else {
                _destination = _getDestAjouterSucces(session, request, response, viewBean);
                // "/"+getAction().getApplicationPart()+"?userAction=pavo.compte.ecriture.chercherSurPage&idJournal="+((CIJournal)viewBean).getIdJournal()+"&addNewEcriture=true";
                // _destination = request.getContextPath() +
                // request.getServletPath()
                // +"?userAction=pavo.compte.ecriture.chercherSurPage&idJournal="+((CIJournal)viewBean).getIdJournal();
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        /*
         * redirection vers la destination
         */
        goSendRedirect(_destination, request, response);
    }

    @Override
    protected void actionCustom(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher dispatcher)
            throws javax.servlet.ServletException, java.io.IOException {
        if (getAction().getActionPart().equals("apercuRCI")) {
            // aperçu des RCI
            _actionApercuRCI(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("afficherDepuisMandat")) {
            // afficher le dossier depuis l'écran du mandat
            _actionAfficherDepuisMandat(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("imprimerAnalyse")) {
            // afficher le dossier depuis l'écran du mandat
            _actionImprimerAnalyseSplitting(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("imprimerApercu")) {
            // afficher le dossier depuis l'écran du mandat
            _actionImprimerApercuSplitting(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("annulerDossier")) {
            _actionAfficherAnnulation(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("rouvrir")) {
            _actionRouvrir(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("revoquer")) {
            _actionRevoquer(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("annulerDossierExecuter")) {
            _actionAnnulerDossier(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("reAfficherDossier")) {
            _actionReAfficherDossier(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("ouvrir")) {
            _actionOuvrir(session, request, response, dispatcher);
        } else {
            actionDefault(session, request, response, dispatcher);
        }
    }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";

        try {
            FWAction _action = FWAction.newInstance(request.getParameter("userAction"));
            FWViewBeanInterface viewBean = FWListViewBeanActionFactory.newInstance(_action, mainDispatcher.getPrefix());
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            viewBean = beforeLister(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, _action);

            request.setAttribute("viewBean", viewBean);
            session.removeAttribute("listViewBean");
            session.setAttribute("listViewBean", viewBean);
            _destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    public FWViewBeanInterface checkViewBean(FWViewBeanInterface viewBean) {
        if (!(viewBean instanceof CIDossierSplittingViewBean)) {
            return new CIDossierSplittingViewBean();
        }
        return null;
    }

}
