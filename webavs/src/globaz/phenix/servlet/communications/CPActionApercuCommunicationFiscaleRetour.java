/*
 * Créé le 30 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.servlet.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.communications.CPApercuCommunicationFiscaleRetourListViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourGEViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourJUViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourNEViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourSEDEXViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourVDViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourVSViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean;
import globaz.phenix.db.communications.CPGenererUneDecisionViewBean;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.db.communications.CPSedexConjoint;
import globaz.phenix.db.communications.CPSedexContribuable;
import globaz.phenix.db.communications.CPSedexDonneesBase;
import globaz.phenix.db.communications.CPSedexDonneesCommerciales;
import globaz.phenix.db.communications.CPSedexDonneesPrivees;
import globaz.phenix.db.communications.CPValidationCalculCommunication;
import globaz.phenix.db.communications.CPValidationCalculCommunicationManager;
import globaz.phenix.db.communications.CPValidationJournalRetourViewBean;
import globaz.phenix.interfaces.ICommunicationRetour;
import globaz.phenix.process.communications.CPProcessAbandonnerEnMasseViewBean;
import globaz.phenix.process.communications.CPProcessCommunicationRetourReinitialiser;
import globaz.phenix.process.communications.CPProcessEnqueterEnMasse;
import globaz.phenix.process.communications.CPProcessEnqueterEnMasseViewBean;
import globaz.phenix.translation.CodeSystem;
import globaz.phenix.vb.communications.CPApercuCommunicationFiscaleRetourComViewBean;
import globaz.pyxis.constantes.IConstantes;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author mmu Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPActionApercuCommunicationFiscaleRetour extends FWDefaultServletAction {
    public final static String CS_CANTON_SEDEX = "620001";

    public CPActionApercuCommunicationFiscaleRetour(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Action qui permet de lancer le calcul des cotisations pour une décision Date de création : (03.05.2002 08:57:00)
     */
    /**
     * Action qui permet de lancer le calcul des cotisations pour une décision Date de création : (03.05.2002 08:57:00)
     * HACK : Lancement du process depuis le Helper Variables settées dans le viewbean à la place du process Le forward
     * se faisait depuis une valeur récupérée depuis le process. Désormais, comme le process est executé dans le helper
     * la valeur d'idJournalRetour est setter dans le viewbean (dans le Helper) de manière à pouvoir obtenir la valeur
     * pour la redirection getRight supprimer car les droits sont récupérés depuis CPApplication
     */
    private void _actionAbandonner(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        // --- Get value from request
        CPCommunicationFiscaleRetourViewBean viewBean = new CPCommunicationFiscaleRetourViewBean();
        String idRetour = request.getParameter("idRetour");
        if (JadeStringUtil.isEmpty(idRetour)) {
            idRetour = (String) session.getAttribute("idRetour");
        }
        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            viewBean.setIdRetour(idRetour);
            session.setAttribute("viewBean", viewBean);
            dispatcher.dispatch(viewBean, getAction());

        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        String idJrn = (String) session.getAttribute("idJournalRetour");
        servlet.getServletContext().getRequestDispatcher(getActionFullURL() + ".chercher&idJournalRetour=" + idJrn)
                .forward(request, response);
        /*
         * this.servlet .getServletContext() .getRequestDispatcher( this.getActionFullURL() +
         * ".chercher&idJournalRetour=" + viewBean.getIdJournalRetour()) .forward(request, response);
         */
    }

    /**
     * Action qui permet (a travers le helper) de lancer le process de mise a jour du status en "abandon"
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    private void _actionAbandonnerEnMasse(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {

        CPProcessAbandonnerEnMasseViewBean viewBean = new CPProcessAbandonnerEnMasseViewBean();
        String[] listIdRetour = request.getParameterValues("listIdRetour");
        String listId = ""; // Construction de l'url de retour
        String destination = getActionFullURL() + ".afficherAbandonnerEnMasse";

        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            viewBean.setListIdRetour(listIdRetour);
            JSPUtils.setBeanProperties(request, viewBean);
            session.setAttribute("viewBean", viewBean);
            dispatcher.dispatch(viewBean, getAction());

            for (int i = 1; i < listIdRetour.length; i++) {
                listId = "&listIdRetour=" + listIdRetour[i];
            }

            destination += "&process=launched";
        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            destination += "&_valid=fail";
        }

        goSendRedirect(destination + listId, request, response);
    }

    /**
     * Action qui permet d'afficher l'écran récapitulatif d'abandon d'une communication fiscale de retour
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    private void _actionAfficherAbandonnerEnMasse(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String destination = null;
        try {

            CPProcessAbandonnerEnMasseViewBean viewBean = new CPProcessAbandonnerEnMasseViewBean();

            String[] listIdRetour = request.getParameterValues("listIdRetour");
            if (listIdRetour == null) {
                listIdRetour = creationListeIdRetourDapresCriteresSelection((BSession) dispatcher.getSession(),
                        session, request);
            }
            viewBean.setListIdRetour(listIdRetour);
            viewBean.setSession((BSession) dispatcher.getSession());
            JSPUtils.setBeanProperties(request, viewBean);

            setSessionAttribute(session, "viewBean", viewBean);

            destination = getRelativeURLwithoutClassPart(request, session)
                    + "abandonnerCommunicationFiscaleRetour_de.jsp";
        } catch (Exception ex) {
            ex.printStackTrace();
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    protected void _actionAfficherConjoint(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        FWAction _action = FWAction.newInstance(request.getParameter("userAction"));
        try {
            /*
             * pour compatibilité : si on a le parametre _method=add, c'est que l'on a une action nouveau
             */
            String method = request.getParameter("_method");
            String backup = request.getParameter("isForBackup");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                _action.changeActionPart(FWAction.ACTION_NOUVEAU);
            }
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            // Recherche du canton
            String idRetour = request.getParameter("idRetour");
            if (JadeStringUtil.isEmpty(idRetour)) {
                idRetour = (String) session.getAttribute("idRetour");
            }
            session.setAttribute("idRetour", idRetour);

            CPSedexConjoint vb = new CPSedexConjoint();
            vb.setIdRetour(idRetour);
            if ((backup != null) && backup.equalsIgnoreCase("true")) {
                vb.setForBackup(true);
            }
            vb.setSession((BSession) bSession);
            vb.retrieve();
            mainDispatcher.dispatch(vb, getAction());
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", vb);
            request.setAttribute(FWServlet.VIEWBEAN, vb);
            String bouton = "";

            if (idRetour == null) {
                idRetour = request.getParameter("selectedId");
            }
            if (this.checkOptionAllowed(session, request, mainDispatcher, idRetour)) {
                bouton = "true";
            } else {
                bouton = "false";
            }
            session.setAttribute("bouton", bouton);
            /*
             * choix destination
             */
            if (vb.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                if (vb.isForBackup()) {
                    if (JadeStringUtil.isEmpty(request.getParameter("forIdJournalRetour"))) {
                        _destination = getRelativeURL(request, session) + "SE" + "ConjointBCK_de.jsp?allComm=yes";
                    } else {
                        _destination = getRelativeURL(request, session) + "SE" + "ConjointBCK_de.jsp?allComm=no";
                    }
                } else {
                    if (JadeStringUtil.isEmpty(request.getParameter("forIdJournalRetour"))) {
                        _destination = getRelativeURL(request, session) + "SE" + "Conjoint_de.jsp?allComm=yes";
                    } else {
                        _destination = getRelativeURL(request, session) + "SE" + "Conjoint_de.jsp?allComm=no";
                    }
                }
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    protected void _actionAfficherContribuable(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        FWAction _action = FWAction.newInstance(request.getParameter("userAction"));
        try {
            /*
             * pour compatibilité : si on a le parametre _method=add, c'est que l'on a une action nouveau
             */
            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                _action.changeActionPart(FWAction.ACTION_NOUVEAU);
            }
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            // Recherche du canton
            String idRetour = request.getParameter("idRetour");
            String backup = request.getParameter("isForBackup");
            if (JadeStringUtil.isEmpty(idRetour)) {
                idRetour = (String) session.getAttribute("idRetour");
            }
            session.setAttribute("idRetour", idRetour);

            CPSedexContribuable vb = new CPSedexContribuable();
            vb.setIdRetour(idRetour);
            if ((backup != null) && backup.equalsIgnoreCase("true")) {
                vb.setForBackup(true);
            }
            vb.setSession((BSession) bSession);
            vb.retrieve();
            mainDispatcher.dispatch(vb, getAction());
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", vb);
            request.setAttribute(FWServlet.VIEWBEAN, vb);
            String bouton = "";

            if (idRetour == null) {
                idRetour = request.getParameter("selectedId");
            }
            if (this.checkOptionAllowed(session, request, mainDispatcher, idRetour)) {
                bouton = "true";
            } else {
                bouton = "false";
            }
            session.setAttribute("bouton", bouton);
            /*
             * choix destination
             */
            if (vb.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                if (vb.isForBackup()) {
                    if (JadeStringUtil.isEmpty(request.getParameter("forIdJournalRetour"))) {
                        _destination = getRelativeURL(request, session) + "SE" + "ContribuableBCK_de.jsp?allComm=yes";
                    } else {
                        _destination = getRelativeURL(request, session) + "SE" + "ContribuableBCK_de.jsp?allComm=no";
                    }
                } else {
                    if (JadeStringUtil.isEmpty(request.getParameter("forIdJournalRetour"))) {
                        _destination = getRelativeURL(request, session) + "SE" + "Contribuable_de.jsp?allComm=yes";
                    } else {
                        _destination = getRelativeURL(request, session) + "SE" + "Contribuable_de.jsp?allComm=no";
                    }
                }
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    protected void _actionAfficherDonneesBase(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        FWAction _action = FWAction.newInstance(request.getParameter("userAction"));
        try {
            /*
             * pour compatibilité : si on a le parametre _method=add, c'est que l'on a une action nouveau
             */
            String backup = request.getParameter("isForBackup");
            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                _action.changeActionPart(FWAction.ACTION_NOUVEAU);
            }
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            // Recherche du canton
            String idRetour = request.getParameter("idRetour");
            if (JadeStringUtil.isEmpty(idRetour)) {
                idRetour = (String) session.getAttribute("idRetour");
            }
            session.setAttribute("idRetour", idRetour);

            CPSedexDonneesBase vb = new CPSedexDonneesBase();
            if ((backup != null) && backup.equalsIgnoreCase("true")) {
                vb.setForBackup(true);
            }
            vb.setIdRetour(idRetour);
            vb.setSession((BSession) bSession);
            vb.retrieve();
            mainDispatcher.dispatch(vb, getAction());
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", vb);
            request.setAttribute(FWServlet.VIEWBEAN, vb);
            String bouton = "";

            if (idRetour == null) {
                idRetour = request.getParameter("selectedId");
            }
            if (this.checkOptionAllowed(session, request, mainDispatcher, idRetour)) {
                bouton = "true";
            } else {
                bouton = "false";
            }
            session.setAttribute("bouton", bouton);
            /*
             * choix destination
             */
            if (vb.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                if (vb.isForBackup()) {
                    if (JadeStringUtil.isEmpty(request.getParameter("forIdJournalRetour"))) {
                        _destination = getRelativeURL(request, session) + "SE" + "DonneesBaseBCK_de.jsp?allComm=yes";
                    } else {
                        _destination = getRelativeURL(request, session) + "SE" + "DonneesBaseBCK_de.jsp?allComm=no";
                    }
                } else {
                    if (JadeStringUtil.isEmpty(request.getParameter("forIdJournalRetour"))) {
                        _destination = getRelativeURL(request, session) + "SE" + "DonneesBase_de.jsp?allComm=yes";
                    } else {
                        _destination = getRelativeURL(request, session) + "SE" + "DonneesBase_de.jsp?allComm=no";
                    }
                }
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    protected void _actionAfficherDonneesCommerciales(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        FWAction _action = FWAction.newInstance(request.getParameter("userAction"));
        try {
            /*
             * pour compatibilité : si on a le parametre _method=add, c'est que l'on a une action nouveau
             */
            String method = request.getParameter("_method");
            String backup = request.getParameter("isForBackup");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                _action.changeActionPart(FWAction.ACTION_NOUVEAU);
            }
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            // Recherche du canton
            String idRetour = request.getParameter("idRetour");
            if (JadeStringUtil.isEmpty(idRetour)) {
                idRetour = (String) session.getAttribute("idRetour");
            }
            session.setAttribute("idRetour", idRetour);

            CPSedexDonneesCommerciales vb = new CPSedexDonneesCommerciales();
            vb.setIdRetour(idRetour);
            if ((backup != null) && backup.equalsIgnoreCase("true")) {
                vb.setForBackup(true);
            }
            vb.setSession((BSession) bSession);
            vb.retrieve();
            mainDispatcher.dispatch(vb, getAction());
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", vb);
            request.setAttribute(FWServlet.VIEWBEAN, vb);
            String bouton = "";

            if (idRetour == null) {
                idRetour = request.getParameter("selectedId");
            }
            if (this.checkOptionAllowed(session, request, mainDispatcher, idRetour)) {
                bouton = "true";
            } else {
                bouton = "false";
            }
            session.setAttribute("bouton", bouton);
            /*
             * choix destination
             */
            if (vb.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                if (vb.isForBackup()) {
                    if (JadeStringUtil.isEmpty(request.getParameter("forIdJournalRetour"))) {
                        _destination = getRelativeURL(request, session) + "SE"
                                + "DonneesCommercialesBCK_de.jsp?allComm=yes";
                    } else {
                        _destination = getRelativeURL(request, session) + "SE"
                                + "DonneesCommercialesBCK_de.jsp?allComm=no";
                    }
                } else {
                    if (JadeStringUtil.isEmpty(request.getParameter("forIdJournalRetour"))) {
                        _destination = getRelativeURL(request, session) + "SE"
                                + "DonneesCommerciales_de.jsp?allComm=yes";
                    } else {
                        _destination = getRelativeURL(request, session) + "SE"
                                + "DonneesCommerciales_de.jsp?allComm=no";
                    }
                }
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    protected void _actionAfficherDonneesPrivees(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        FWAction _action = FWAction.newInstance(request.getParameter("userAction"));
        try {
            /*
             * pour compatibilité : si on a le parametre _method=add, c'est que l'on a une action nouveau
             */
            String method = request.getParameter("_method");
            String backup = request.getParameter("isForBackup");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                _action.changeActionPart(FWAction.ACTION_NOUVEAU);
            }
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            // Recherche du canton
            String idRetour = request.getParameter("idRetour");
            if (JadeStringUtil.isEmpty(idRetour)) {
                idRetour = (String) session.getAttribute("idRetour");
            }
            session.setAttribute("idRetour", idRetour);

            CPSedexDonneesPrivees vb = new CPSedexDonneesPrivees();
            vb.setIdRetour(idRetour);
            if ((backup != null) && backup.equalsIgnoreCase("true")) {
                vb.setForBackup(true);
            }
            vb.setSession((BSession) bSession);
            vb.retrieve();
            mainDispatcher.dispatch(vb, getAction());
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", vb);
            request.setAttribute(FWServlet.VIEWBEAN, vb);
            String bouton = "";

            if (idRetour == null) {
                idRetour = request.getParameter("selectedId");
            }
            if (this.checkOptionAllowed(session, request, mainDispatcher, idRetour)) {
                bouton = "true";
            } else {
                bouton = "false";
            }
            session.setAttribute("bouton", bouton);
            /*
             * choix destination
             */
            if (vb.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                if (vb.isForBackup()) {
                    if (JadeStringUtil.isEmpty(request.getParameter("forIdJournalRetour"))) {
                        _destination = getRelativeURL(request, session) + "SE" + "DonneesPriveesBCK_de.jsp?allComm=yes";
                    } else {
                        _destination = getRelativeURL(request, session) + "SE" + "DonneesPriveesBCK_de.jsp?allComm=no";
                    }
                } else {
                    if (JadeStringUtil.isEmpty(request.getParameter("forIdJournalRetour"))) {
                        _destination = getRelativeURL(request, session) + "SE" + "DonneesPrivees_de.jsp?allComm=yes";
                    } else {
                        _destination = getRelativeURL(request, session) + "SE" + "DonneesPrivees_de.jsp?allComm=no";
                    }
                }
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    private void _actionAfficherEnqueterEnMasse(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String destination = null;
        try {

            CPProcessEnqueterEnMasseViewBean viewBean = new CPProcessEnqueterEnMasseViewBean();
            CPProcessEnqueterEnMasse process = new CPProcessEnqueterEnMasse();

            String[] listIdRetour = request.getParameterValues("listIdRetour");
            if (listIdRetour == null) {
                listIdRetour = creationListeIdRetourDapresCriteresSelection((BSession) dispatcher.getSession(),
                        session, request);
            }
            viewBean.setListIdRetour(listIdRetour);
            viewBean.setSession((BSession) dispatcher.getSession());
            process.setSession((BSession) dispatcher.getSession());
            process.setListIdRetour(listIdRetour);
            JSPUtils.setBeanProperties(request, viewBean);

            setSessionAttribute(session, "viewBean", viewBean);

            destination = getRelativeURLwithoutClassPart(request, session) + "processEnqueterEnMasse_de.jsp";
        } catch (Exception ex) {
            ex.printStackTrace();
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    private void _actionAfficherEnqueterEnMasseExecuter(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String destination = getActionFullURL() + ".afficherEnqueterEnMasse";
        try {

            CPProcessEnqueterEnMasseViewBean viewBean = new CPProcessEnqueterEnMasseViewBean();
            CPProcessEnqueterEnMasse process = new CPProcessEnqueterEnMasse();

            String[] listIdRetour = request.getParameterValues("listIdRetour");
            if (listIdRetour == null) {
                listIdRetour = creationListeIdRetourDapresCriteresSelection((BSession) dispatcher.getSession(),
                        session, request);
            }
            viewBean.setListIdRetour(listIdRetour);
            viewBean.setSession((BSession) dispatcher.getSession());
            process.setSession((BSession) dispatcher.getSession());
            process.setListIdRetour(listIdRetour);
            process.executeProcess();
            JSPUtils.setBeanProperties(request, viewBean);

            setSessionAttribute(session, "viewBean", viewBean);

            // destination = getRelativeURLwithoutClassPart(request, session) +
            // "processEnqueterEnMasse_de.jsp";
            destination += "&process=launched";
        } catch (Exception ex) {
            ex.printStackTrace();
            destination += "&_valid=fail";
        }

        goSendRedirect(destination, request, response);

    }

    private void _actionAfficherGenererEnMasse(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String destination = null;
        try {

            CPProcessAbandonnerEnMasseViewBean viewBean = new CPProcessAbandonnerEnMasseViewBean();

            String[] listIdRetour = request.getParameterValues("listIdRetour");
            if (listIdRetour == null) {
                listIdRetour = creationListeIdRetourDapresCriteresSelection((BSession) dispatcher.getSession(),
                        session, request);
            }
            viewBean.setListIdRetour(listIdRetour);
            viewBean.setSession((BSession) dispatcher.getSession());
            JSPUtils.setBeanProperties(request, viewBean);

            setSessionAttribute(session, "viewBean", viewBean);

            destination = getRelativeURLwithoutClassPart(request, session) + "genererCommunicationFiscaleRetour_de.jsp";
        } catch (Exception ex) {
            ex.printStackTrace();
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    private void _actionAfficherReinitialiserEnMasse(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String destination = null;
        try {

            CPProcessAbandonnerEnMasseViewBean viewBean = new CPProcessAbandonnerEnMasseViewBean();

            String[] listIdRetour = request.getParameterValues("listIdRetour");
            if (listIdRetour == null) {
                listIdRetour = creationListeIdRetourDapresCriteresSelection((BSession) dispatcher.getSession(),
                        session, request);
            }
            viewBean.setListIdRetour(listIdRetour);
            viewBean.setSession((BSession) dispatcher.getSession());
            JSPUtils.setBeanProperties(request, viewBean);

            setSessionAttribute(session, "viewBean", viewBean);

            destination = getRelativeURLwithoutClassPart(request, session)
                    + "reinitialiserCommunicationFiscaleRetour_de.jsp";
        } catch (Exception ex) {
            ex.printStackTrace();
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * Action qui permet (a travers le helper) de lancer le process de mise a jour du status en "enquete"
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    private void _actionEnqueterEnMasse(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = null;
        try {

            CPProcessEnqueterEnMasseViewBean viewBean = new CPProcessEnqueterEnMasseViewBean();

            String[] listIdRetour = request.getParameterValues("listIdRetour");
            if (listIdRetour == null) {
                listIdRetour = creationListeIdRetourDapresCriteresSelection((BSession) dispatcher.getSession(),
                        session, request);
            }
            viewBean.setListIdRetour(listIdRetour);
            viewBean.setSession((BSession) dispatcher.getSession());
            JSPUtils.setBeanProperties(request, viewBean);

            setSessionAttribute(session, "viewBean", viewBean);

            destination = getRelativeURLwithoutClassPart(request, session) + "enqueterEnMasse_de.jsp";
        } catch (Exception ex) {
            ex.printStackTrace();
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     *            HACK : set des variables dans le viewBean appel du process dans le Helper de la classe goSendRedirect
     *            pour redirection sur action
     */
    private void _actionExecuterImprimer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {

            String idRetour = request.getParameter("idRetour");
            String impression = request.getParameter("impression");

            CPCommunicationFiscaleRetourViewBean viewBean = (CPCommunicationFiscaleRetourViewBean) session
                    .getAttribute("viewBean");
            viewBean.setSession((BSession) dispatcher.getSession());
            viewBean.setEMailAddress(request.getParameter("eMailAddress"));
            if ("on".equalsIgnoreCase(request.getParameter("wantDetail"))) {
                viewBean.setWantDetail(Boolean.TRUE);
            } else {
                viewBean.setWantDetail(Boolean.FALSE);
            }
            viewBean.setImpression(impression);
            viewBean.setIdRetour(idRetour);
            dispatcher.dispatch(viewBean, getAction());
            _destination = "/phenix?userAction=phenix.communications.apercuCommunicationFiscaleRetour.imprimer";
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = _destination + "&_valid=fail";
            } else {
                _destination = _destination + "&process=launched";
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * affiche la prochaine page
         */
        // response.sendRedirect(request.getContextPath() + _destination);
        goSendRedirect(_destination, request, response);

    }

    /**
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     *             Cette méthode permet uniquement de forwarder l'utilisateur vers la page de lancement du process On
     *             conserve le forward car page spécifique Suppression du process + suppression du hasRight (droit dans
     *             CPApplication)
     */
    private void _actionGenerer(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher dispatcher)
            throws javax.servlet.ServletException, java.io.IOException {
        // --- Get value from request
        String _destination = "";
        CPGenererUneDecisionViewBean viewBean = new CPGenererUneDecisionViewBean();
        String idRetour = request.getParameter("idRetour");
        if (JadeStringUtil.isEmpty(idRetour)) {
            idRetour = (String) session.getAttribute("idRetour");
        }
        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            session.setAttribute("viewBean", viewBean);
            dispatcher.dispatch(viewBean, getAction());
            _destination = getRelativeURLwithoutClassPart(request, session)
                    + "processReceptionGenererUneDecisions_de.jsp";

        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    private void _actionGenererEnMasse(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {

        CPProcessAbandonnerEnMasseViewBean viewBean = new CPProcessAbandonnerEnMasseViewBean();
        String[] listIdRetour = request.getParameterValues("listIdRetour");
        String listId = ""; // Construction de l'url de retour
        String destination = getActionFullURL() + ".afficherGenererEnMasse";

        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            viewBean.setListIdRetour(listIdRetour);
            JSPUtils.setBeanProperties(request, viewBean);
            session.setAttribute("viewBean", viewBean);
            dispatcher.dispatch(viewBean, getAction());

            for (int i = 1; i < listIdRetour.length; i++) {
                listId = "&listIdRetour=" + listIdRetour[i];
            }

            destination += "&process=launched";
        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            destination += "&_valid=fail";
        }

        goSendRedirect(destination + listId, request, response);
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     *            Forward non modifié car redirection sur une page JSP Le retrieve est maintenant executé dans le Helper
     *            de la classe de manière à passer par les droits
     */
    private void _actionImprimer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";

        try {
            String idRetour = request.getParameter("idRetour");
            CPCommunicationFiscaleRetourViewBean retour = new CPCommunicationFiscaleRetourViewBean();
            retour.setISession(dispatcher.getSession());
            retour.setIdRetour(idRetour);
            // journal.retrieve();
            dispatcher.dispatch(retour, getAction());

            session.setAttribute("viewBean", retour);
            _destination = getRelativeURLwithoutClassPart(request, session) + "communicationRetourImprimerUne_de.jsp";
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        /*
         * affiche la prochaine page
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    private void _actionModifierCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) {
        try {
            String _destination = "";
            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);

            /*
             * recupération du bean depuis la sesison
             */
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

            /*
             * set des properietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            String idRetour = request.getParameter("idRetour");
            if (JadeStringUtil.isEmpty(idRetour)) {
                idRetour = (String) session.getAttribute("idRetour");
            }
            if (!JadeStringUtil.isEmpty(idRetour)) {
                if (CPCommunicationFiscaleRetourSEDEXViewBean.class.isAssignableFrom(viewBean.getClass())) {
                    CPCommunicationFiscaleRetourSEDEXViewBean viewBeanPourUpdate = new CPCommunicationFiscaleRetourSEDEXViewBean();
                    viewBeanPourUpdate.setSession((BSession) mainDispatcher.getSession());
                    viewBeanPourUpdate.setIdRetour(idRetour);
                    viewBeanPourUpdate.setIdCommunication(idRetour);
                    viewBeanPourUpdate.retrieve();
                    // On rajoute les infos de l'utilisateur qui a modifié la
                    // communication
                    GregorianCalendar d = new GregorianCalendar();
                    int heure = d.get(Calendar.HOUR_OF_DAY);
                    String heureH = String.valueOf(heure);
                    if (heureH.length() == 1) {
                        heureH = "0" + heureH;
                    }
                    int min = d.get(Calendar.MINUTE);
                    String minM = String.valueOf(min);
                    if (minM.length() == 1) {
                        minM = "0" + minM;
                    }
                    int sec = d.get(Calendar.SECOND);
                    String secS = String.valueOf(sec);
                    if (secS.length() == 1) {
                        secS = "0" + secS;
                    }

                    viewBeanPourUpdate.setLastUser(((BSession) mainDispatcher.getSession()).getUserFullName());
                    viewBeanPourUpdate.setLastDate(JACalendar.todayJJsMMsAAAA());
                    viewBeanPourUpdate.setLastTime(heureH + ":" + minM + ":" + secS);
                    globaz.globall.http.JSPUtils.setBeanProperties(request, viewBeanPourUpdate);
                    viewBeanPourUpdate.update();
                    _destination = getRelativeURL(request, session) + "SE" + "_de.jsp?allComm=yes";

                }
                if (CPSedexContribuable.class.isAssignableFrom(viewBean.getClass())) {
                    CPSedexContribuable viewBeanPourUpdate = new CPSedexContribuable();
                    viewBeanPourUpdate.setSession((BSession) mainDispatcher.getSession());
                    viewBeanPourUpdate.setIdRetour(idRetour);
                    viewBeanPourUpdate.retrieve();

                    globaz.globall.http.JSPUtils.setBeanProperties(request, viewBeanPourUpdate);
                    viewBeanPourUpdate.update();
                    _destination = getRelativeURL(request, session) + "SE" + "Contribuable_de.jsp?allComm=yes";
                }
                if (CPSedexDonneesBase.class.isAssignableFrom(viewBean.getClass())) {
                    CPSedexDonneesBase viewBeanPourUpdate = new CPSedexDonneesBase();
                    viewBeanPourUpdate.setSession((BSession) mainDispatcher.getSession());
                    viewBeanPourUpdate.setIdRetour(idRetour);
                    viewBeanPourUpdate.retrieve();

                    globaz.globall.http.JSPUtils.setBeanProperties(request, viewBeanPourUpdate);
                    viewBeanPourUpdate.update();
                    _destination = getRelativeURL(request, session) + "SE" + "DonneesBase_de.jsp?allComm=yes";
                }
                if (CPSedexDonneesPrivees.class.isAssignableFrom(viewBean.getClass())) {
                    CPSedexDonneesPrivees viewBeanPourUpdate = new CPSedexDonneesPrivees();
                    viewBeanPourUpdate.setSession((BSession) mainDispatcher.getSession());
                    viewBeanPourUpdate.setIdRetour(idRetour);
                    viewBeanPourUpdate.retrieve();

                    globaz.globall.http.JSPUtils.setBeanProperties(request, viewBeanPourUpdate);
                    viewBeanPourUpdate.update();
                    _destination = getRelativeURL(request, session) + "SE" + "DonneesPrivees_de.jsp?allComm=yes";
                }
                if (CPSedexDonneesCommerciales.class.isAssignableFrom(viewBean.getClass())) {
                    CPSedexDonneesCommerciales viewBeanPourUpdate = new CPSedexDonneesCommerciales();
                    viewBeanPourUpdate.setSession((BSession) mainDispatcher.getSession());
                    viewBeanPourUpdate.setIdRetour(idRetour);
                    viewBeanPourUpdate.retrieve();

                    globaz.globall.http.JSPUtils.setBeanProperties(request, viewBeanPourUpdate);
                    viewBeanPourUpdate.update();
                    _destination = getRelativeURL(request, session) + "SE" + "DonneesCommerciales_de.jsp?allComm=yes";
                }
                if (CPSedexConjoint.class.isAssignableFrom(viewBean.getClass())) {
                    CPSedexConjoint viewBeanPourUpdate = new CPSedexConjoint();
                    viewBeanPourUpdate.setSession((BSession) mainDispatcher.getSession());
                    viewBeanPourUpdate.setIdRetour(idRetour);
                    viewBeanPourUpdate.retrieve();

                    globaz.globall.http.JSPUtils.setBeanProperties(request, viewBeanPourUpdate);
                    viewBeanPourUpdate.update();
                    _destination = getRelativeURL(request, session) + "SE" + "Conjoint_de.jsp?allComm=yes";
                }

            }
            // Réinitialisation de la communication
            if (!JadeStringUtil.isEmpty(idRetour)) {
                CPProcessCommunicationRetourReinitialiser reinit = new CPProcessCommunicationRetourReinitialiser();
                String listIdRetour[] = { idRetour };
                reinit.setListIdRetour(listIdRetour);
                reinit.setISession(mainDispatcher.getSession());
                reinit.setSendMailOnError(false);
                reinit.setSendCompletionMail(false);
                reinit.executeProcess();
            }
            /*
             * beforeUpdate, call du dispatcher puis mis en session
             */
            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            session.setAttribute("viewBean", viewBean);
            /*
             * choix de la destination _valid=fail : revient en mode edition
             */
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                // this._destination = this._getDestModifierSucces(session,
                // request, response, viewBean);
            } else {
                // this._destination = this._getDestModifierEchec(session,
                // request, response, viewBean);
            }

        } catch (Exception e) {
            // this._destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        // this.goSendRedirect(this._destination, request, response);
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     *             HACK : hasRight supprimé lancement du process depuis le Helper de la classe on appelle désormais
     *             l'action actionChercher au lieu de faire le forward sur le même action
     */
    private void _actionProcessGenerer(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        // --- Get value from request
        CPGenererUneDecisionViewBean viewBean = new CPGenererUneDecisionViewBean();
        String idRetour = (String) session.getAttribute("idRetour");
        viewBean.setIdRetour(idRetour);

        try {
            JSPUtils.setBeanProperties(request, viewBean);
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            // if (viewBean.getSession().hasRight(getAction().toString(),
            // globaz.framework.secure.FWSecureConstants.UPDATE)) {
            // viewBean.setSession((globaz.globall.db.BSession) bSession);

            // CPProcessReceptionGenererDecision processGeneration = new
            // CPProcessReceptionGenererDecision();
            // processGeneration.setIdRetour(idRetour);
            // processGeneration.setISession((globaz.globall.db.BSession)
            // bSession);
            // processGeneration.setEMailAddress(request.getParameter("eMailAddress"));
            // processGeneration.start();
            session.setAttribute("viewBean", viewBean);
            dispatcher.dispatch(viewBean, getAction());
            // } else {
            // viewBean.setMsgType(FWViewBeanInterface.ERROR);
            // }
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        // servlet.getServletContext().getRequestDispatcher("/phenix?userAction=phenix.communications.apercuCommunicationFiscaleRetour.chercher").forward(request,
        // response);
        // super.actionChercher(session, request, response, dispatcher);
        String idJrn = (String) session.getAttribute("idJournalRetour");
        servlet.getServletContext().getRequestDispatcher(getActionFullURL() + ".chercher&idJournalRetour=" + idJrn)
                .forward(request, response);
    }

    /**
     * Action qui permet de lancer le calcul des cotisations pour une décision Date de création : (03.05.2002 08:57:00)
     */
    /**
     * Action qui permet de lancer le calcul des cotisations pour une décision Date de création : (03.05.2002 08:57:00)
     * HACK : Lancement du process depuis le Helper Variables settées dans le viewbean à la place du process Le forward
     * se faisait depuis une valeur récupérée depuis le process. Désormais, comme le process est executé dans le helper
     * la valeur d'idJournalRetour est setter dans le viewbean (dans le Helper) de manière à pouvoir obtenir la valeur
     * pour la redirection getRight supprimer car les droits sont récupérés depuis CPApplication
     */
    private void _actionReinitialiser(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        // --- Get value from request
        CPCommunicationFiscaleRetourViewBean viewBean = new CPCommunicationFiscaleRetourViewBean();
        String idRetour = request.getParameter("idRetour");
        if (JadeStringUtil.isEmpty(idRetour)) {
            idRetour = (String) session.getAttribute("idRetour");
        }
        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            viewBean.setIdRetour(idRetour);
            session.setAttribute("viewBean", viewBean);
            dispatcher.dispatch(viewBean, getAction());

        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        servlet.getServletContext()
                .getRequestDispatcher(
                        getActionFullURL() + ".chercher&idJournalRetour="
                                + (String) session.getAttribute("idJournalRetour")).forward(request, response);
    }

    private void _actionReinitialiserEnMasse(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {

        CPProcessAbandonnerEnMasseViewBean viewBean = new CPProcessAbandonnerEnMasseViewBean();
        String[] listIdRetour = request.getParameterValues("listIdRetour");
        String listId = ""; // Construction de l'url de retour
        String destination = getActionFullURL() + ".afficherReinitialiserEnMasse";

        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            viewBean.setListIdRetour(listIdRetour);
            JSPUtils.setBeanProperties(request, viewBean);
            session.setAttribute("viewBean", viewBean);
            dispatcher.dispatch(viewBean, getAction());

            for (int i = 1; i < listIdRetour.length; i++) {
                listId = "&listIdRetour=" + listIdRetour[i];
            }

            destination += "&process=launched";
        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            destination += "&_valid=fail";
        }

        goSendRedirect(destination + listId, request, response);
    }

    protected void _afficherDonneesCommunication(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        FWAction _action = FWAction.newInstance(request.getParameter("userAction"));
        try {
            /*
             * pour compatibilité : si on a le parametre _method=add, c'est que l'on a une action nouveau
             */
            String backup = request.getParameter("isForBackup");
            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                _action.changeActionPart(FWAction.ACTION_NOUVEAU);
            }
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            // Recherche du canton
            String idRetour = (String) session.getAttribute("idRetour");
            session.setAttribute("idRetour", idRetour);

            CPCommunicationFiscaleRetourSEDEXViewBean vb = new CPCommunicationFiscaleRetourSEDEXViewBean();

            mainDispatcher.dispatch(vb, getAction());
            vb.setIdRetour(idRetour);
            if ((backup != null) && backup.equalsIgnoreCase("true")) {
                vb.setForBackup(true);
            }
            vb.setSession((BSession) bSession);
            vb.setWantDonneeBase(true);
            vb.retrieve();
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", vb);
            request.setAttribute(FWServlet.VIEWBEAN, vb);
            String bouton = "";

            if (idRetour == null) {
                idRetour = request.getParameter("selectedId");
            }
            if (this.checkOptionAllowed(session, request, mainDispatcher, idRetour)) {
                bouton = "true";
            } else {
                bouton = "false";
            }
            session.setAttribute("bouton", bouton);
            /*
             * choix destination
             */
            if (vb.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                if (vb.isForBackup()) {
                    if (JadeStringUtil.isEmpty(request.getParameter("forIdJournalRetour"))) {
                        _destination = getRelativeURL(request, session) + "SEBCK" + "_de.jsp?allComm=yes";
                    } else {
                        _destination = getRelativeURL(request, session) + "SEBCK" + "_de.jsp?allComm=no";
                    }
                } else {
                    if (JadeStringUtil.isEmpty(request.getParameter("forIdJournalRetour"))) {
                        _destination = getRelativeURL(request, session) + "SE" + "_de.jsp?allComm=yes";
                    } else {
                        _destination = getRelativeURL(request, session) + "SE" + "_de.jsp?allComm=no";
                    }
                }
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestEchec(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestEchec(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        // return super._getDestEchec(
        // session,
        // request,
        // response,
        // viewBean)+getParamIdJournal(request);
        String selectedId = request.getParameter("idRetour");
        return getActionFullURL() + ".afficher" + "&_valid=fail" + getParamIdJournal(request) + "&selectedId="
                + selectedId;
        // return getRelativeURL(request,session)+"_de.jsp?_valid=fail";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String selectedId = request.getParameter("idRetour");
        return getActionFullURL() + ".afficher" + getParamIdJournal(request) + "&selectedId=" + selectedId;

        // return super._getDestModifierSucces(session, request, response,
        // viewBean) + getParamIdJournal(request);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        FWAction _action = FWAction.newInstance(request.getParameter("userAction"));
        try {
            /*
             * pour compatibilité : si on a le parametre _method=add, c'est que l'on a une action nouveau
             */
            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                _action.changeActionPart(FWAction.ACTION_NOUVEAU);
            }
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            // Recherche du canton
            String canton = request.getParameter("canton");
            String idRetour = request.getParameter("idRetour");
            String selectedId = request.getParameter("selectedId");
            if (JadeStringUtil.isEmpty(selectedId)) {
                if (JadeStringUtil.isEmpty(idRetour)) {
                    idRetour = (String) session.getAttribute("idRetour");
                }
            } else {
                idRetour = selectedId;
            }

            CPApercuCommunicationFiscaleRetourComViewBean vb = new CPApercuCommunicationFiscaleRetourComViewBean();
            vb.setCanton(canton);

            // if (JadeStringUtil.isEmpty(canton)) {
            if (JadeStringUtil.isEmpty(idRetour)) {
                vb.setSession((globaz.globall.db.BSession) bSession);
                vb.setIdRetour(selectedId);
            } else {
                vb.setSession((globaz.globall.db.BSession) bSession);
                vb.setIdRetour(idRetour);
            }
            // }
            mainDispatcher.dispatch(vb, getAction());

            ICommunicationRetour viewBean = vb.getCommunicationRetour();

            if (_action.getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                viewBean = (ICommunicationRetour) beforeNouveau(session, request, response,
                        (FWViewBeanInterface) viewBean);
            }

            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);
            String bouton = "";

            if (idRetour == null) {
                idRetour = request.getParameter("selectedId");
            }
            if (this.checkOptionAllowed(session, request, mainDispatcher, idRetour)) {
                bouton = "true";
            } else {
                bouton = "false";
            }
            session.setAttribute("bouton", bouton);
            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                if (JadeStringUtil.isEmpty(request.getParameter("forIdJournalRetour"))) {
                    _destination = getRelativeURL(request, session) + viewBean.getJournalRetour().getCodeCanton()
                            + "_de.jsp?allComm=yes";
                } else {
                    _destination = getRelativeURL(request, session) + viewBean.getJournalRetour().getCodeCanton()
                            + "_de.jsp?allComm=no";
                }
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    protected void actionAfficherOriginale(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        FWAction _action = FWAction.newInstance(request.getParameter("userAction"));
        try {
            /*
             * pour compatibilité : si on a le parametre _method=add, c'est que l'on a une action nouveau
             */
            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                _action.changeActionPart(FWAction.ACTION_NOUVEAU);
            }
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            // Recherche du canton
            String canton = request.getParameter("canton");
            String idRetour = request.getParameter("idRetour");
            String selectedId = request.getParameter("selectedId");
            if (JadeStringUtil.isEmpty(selectedId)) {
                if (JadeStringUtil.isEmpty(idRetour)) {
                    idRetour = (String) session.getAttribute("idRetour");
                }
            } else {
                idRetour = selectedId;
            }

            CPApercuCommunicationFiscaleRetourComViewBean vb = new CPApercuCommunicationFiscaleRetourComViewBean();
            vb.setCanton(canton);

            // if (JadeStringUtil.isEmpty(canton)) {
            if (JadeStringUtil.isEmpty(idRetour)) {
                // Cas ou l'on vient de l'écran détail des décisions
                vb.setSession((globaz.globall.db.BSession) bSession);
                vb.setIdRetour(selectedId);
            } else {
                // Cas ou l'on vient de l'écran détail des décisions
                vb.setSession((globaz.globall.db.BSession) bSession);
                vb.setIdRetour(idRetour);
            }
            // }
            mainDispatcher.dispatch(vb, getAction());

            ICommunicationRetour viewBean = null;
            // Il faut tester le canton
            CPCommunicationFiscaleRetourViewBean testCanton = new CPCommunicationFiscaleRetourViewBean();
            testCanton.setSession((globaz.globall.db.BSession) bSession);
            testCanton.setIdRetour(idRetour);
            testCanton.retrieve();
            CPJournalRetour jrn = new CPJournalRetour();
            jrn.setSession((globaz.globall.db.BSession) bSession);
            jrn.setIdJournalRetour(testCanton.getIdJournalRetour());
            jrn.retrieve();

            if (IConstantes.CS_LOCALITE_CANTON_JURA.equalsIgnoreCase(jrn.getCanton())) {
                viewBean = new CPCommunicationFiscaleRetourJUViewBean();
            } else if (IConstantes.CS_LOCALITE_CANTON_NEUCHATEL.equalsIgnoreCase(jrn.getCanton())) {
                viewBean = new CPCommunicationFiscaleRetourNEViewBean();
            } else if (IConstantes.CS_LOCALITE_CANTON_GENEVE.equalsIgnoreCase(jrn.getCanton())) {
                viewBean = new CPCommunicationFiscaleRetourGEViewBean();
            } else if (IConstantes.CS_LOCALITE_CANTON_VAUD.equalsIgnoreCase(jrn.getCanton())) {
                viewBean = new CPCommunicationFiscaleRetourVDViewBean();
            } else if (IConstantes.CS_LOCALITE_CANTON_VALAIS.equalsIgnoreCase(jrn.getCanton())) {
                viewBean = new CPCommunicationFiscaleRetourVSViewBean();
            } else if (CPActionApercuCommunicationFiscaleRetour.CS_CANTON_SEDEX.equalsIgnoreCase(jrn.getCanton())) {
                viewBean = new CPCommunicationFiscaleRetourSEDEXViewBean();
                viewBean.setWantDonneeBase(true);
            }

            viewBean.setSession((globaz.globall.db.BSession) bSession);
            viewBean.setIdRetour(idRetour);
            viewBean.setForBackup(true);
            viewBean.setWantAfterRetrieve(true);
            viewBean.retrieve();

            if (_action.getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                viewBean = (ICommunicationRetour) beforeNouveau(session, request, response,
                        (FWViewBeanInterface) viewBean);
            }

            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);
            String bouton = "";

            if (idRetour == null) {
                idRetour = request.getParameter("selectedId");
            }
            // if (checkOptionAllowed(session, request, mainDispatcher,
            // idRetour))
            // bouton = "true";
            // else
            bouton = "false";
            session.setAttribute("bouton", bouton);
            /*
             * choix destination
             */
            if ((viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true)
                    || JadeStringUtil.isEmpty(viewBean.getJournalRetour().getCodeCanton())) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                if (JadeStringUtil.isEmpty(request.getParameter("forIdJournalRetour"))) {
                    _destination = getRelativeURL(request, session) + viewBean.getJournalRetour().getCodeCanton()
                            + "BCK_de.jsp?allComm=yes";
                } else {
                    _destination = getRelativeURL(request, session) + viewBean.getJournalRetour().getCodeCanton()
                            + "BCK_de.jsp?allComm=no";
                }
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * HACK On laisse le checkOption qui renvoie un boolean malgré le retrieve car il n'influe pas sur les droits
     */
    @Override
    protected void actionCustom(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher mainDispatcher)
            throws javax.servlet.ServletException, java.io.IOException {
        // En création on propose un écran d'initialisation pour préparer
        // l'écran d'encodage
        // de la décision suivant le type et l'année saisis.
        if ("afficherOriginale".equals(getAction().getActionPart())) {
            actionAfficherOriginale(session, request, response, mainDispatcher);
        } else if ("imprimer".equals(getAction().getActionPart())) {
            _actionImprimer(session, request, response, mainDispatcher);
        } else if ("executerImprimer".equals(getAction().getActionPart())) {
            _actionExecuterImprimer(session, request, response, mainDispatcher);
        } else if ("retournerOriginale".equals(getAction().getActionPart())) {
            actionRetournerOriginale(session, request, response, mainDispatcher);
        } else if ("afficherEnqueterEnMasseExecuter".equals(getAction().getActionPart())) {
            _actionAfficherEnqueterEnMasseExecuter(session, request, response, mainDispatcher);
        } else if ("modifierCustom".equals(getAction().getActionPart())) {
            _actionModifierCustom(session, request, response, mainDispatcher);
        } else if ("afficherDonneesCommunication".equals(getAction().getActionPart())) {
            _afficherDonneesCommunication(session, request, response, mainDispatcher);
        } else if ("afficherDonneesBase".equals(getAction().getActionPart())) {
            _actionAfficherDonneesBase(session, request, response, mainDispatcher);
        } else if ("afficherDonneesCommerciales".equals(getAction().getActionPart())) {
            _actionAfficherDonneesCommerciales(session, request, response, mainDispatcher);
        } else if ("afficherContribuable".equals(getAction().getActionPart())) {
            _actionAfficherContribuable(session, request, response, mainDispatcher);
        } else if ("afficherConjoint".equals(getAction().getActionPart())) {
            _actionAfficherConjoint(session, request, response, mainDispatcher);
        } else if ("afficherDonneesPrivees".equals(getAction().getActionPart())) {
            _actionAfficherDonneesPrivees(session, request, response, mainDispatcher);
        } else if ("afficherEnqueterEnMasse".equals(getAction().getActionPart())) {
            _actionAfficherEnqueterEnMasse(session, request, response, mainDispatcher);
        } else if ("afficherAbandonnerEnMasse".equals(getAction().getActionPart())) {
            _actionAfficherAbandonnerEnMasse(session, request, response, mainDispatcher);
        } else if ("enqueterEnMasse".equals(getAction().getActionPart())) {
            _actionEnqueterEnMasse(session, request, response, mainDispatcher);
        } else if ("enqueterEnMasseExecuter".equals(getAction().getActionPart())) {
            _actionAfficherEnqueterEnMasseExecuter(session, request, response, mainDispatcher);
            // _actionEnqueterEnMasseExecuter(session, request, response, mainDispatcher);
        } else if ("afficherReinitialiserEnMasse".equals(getAction().getActionPart())) {
            _actionAfficherReinitialiserEnMasse(session, request, response, mainDispatcher);
        } else if ("reinitialiserEnMasse".equals(getAction().getActionPart())) {
            _actionReinitialiserEnMasse(session, request, response, mainDispatcher);
        } else if ("afficherGenererEnMasse".equals(getAction().getActionPart())) {
            _actionAfficherGenererEnMasse(session, request, response, mainDispatcher);
        } else if ("genererEnMasse".equals(getAction().getActionPart())) {
            _actionGenererEnMasse(session, request, response, mainDispatcher);
        } else if ("abandonnerEnMasse".equals(getAction().getActionPart())) {
            _actionAbandonnerEnMasse(session, request, response, mainDispatcher);
        } else if ("abandonner".equals(getAction().getActionPart())) {
            if (this.checkOptionAllowed(session, request, mainDispatcher)) {
                _actionAbandonner(session, request, response, mainDispatcher);
            } else {
                // mettre une erreur
                session.setAttribute("errorMessage", "Cette option pour cet état est interdite");
                servlet.getServletContext()
                        .getRequestDispatcher(
                                "/phenix?userAction=phenix.communications.apercuCommunicationFiscaleRetour.chercher")
                        .forward(request, response);
            }
        } else if ("generer".equals(getAction().getActionPart())) {
            if (this.checkOptionAllowed(session, request, mainDispatcher)) {
                _actionGenerer(session, request, response, mainDispatcher);
            } else {
                // mettre une erreur
                session.setAttribute("errorMessage", "Cette option pour cet état est interdite");
                servlet.getServletContext()
                        .getRequestDispatcher(
                                "/phenix?userAction=phenix.communications.apercuCommunicationFiscaleRetour.chercher")
                        .forward(request, response);
            }
        } else if ("reinitialiser".equals(getAction().getActionPart())) {
            if (this.checkOptionAllowed(session, request, mainDispatcher)) {
                _actionReinitialiser(session, request, response, mainDispatcher);
            } else {
                // mettre une erreur
                session.setAttribute("errorMessage", "Cette option pour cet état est interdite");
                // servlet.getServletContext().getRequestDispatcher("/phenix?userAction=phenix.communications.apercuCommunicationFiscaleRetour.chercher").forward(request,
                // response);
                goSendRedirect("/phenix?userAction=phenix.communications.apercuCommunicationFiscaleRetour.chercher",
                        request, response);
            }
        } else if ("processGenerer".equals(getAction().getActionPart())) {
            _actionProcessGenerer(session, request, response, mainDispatcher);
        }
    }

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _destination = "";
        FWAction _action = FWAction.newInstance(request.getParameter("userAction"));
        try {
            /*
             * pour compatibilité : si on a le parametre _method=add, c'est que l'on a une action nouveau
             */
            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                _action.changeActionPart(FWAction.ACTION_NOUVEAU);
            }
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            // Recherche du canton
            String canton = request.getParameter("canton");
            String idRetour = request.getParameter("idRetour");
            String selectedId = request.getParameter("selectedId");
            if (JadeStringUtil.isEmpty(selectedId)) {
                if (JadeStringUtil.isEmpty(idRetour)) {
                    idRetour = (String) session.getAttribute("idRetour");
                }
            } else {
                idRetour = selectedId;
            }

            CPApercuCommunicationFiscaleRetourComViewBean vb = new CPApercuCommunicationFiscaleRetourComViewBean();
            vb.setCanton(canton);

            if (JadeStringUtil.isEmpty(canton)) {
                if (JadeStringUtil.isEmpty(idRetour)) {
                    // Cas ou l'on vient de l'écran détail des décisions
                    vb.setSession((globaz.globall.db.BSession) bSession);
                    vb.setIdRetour(selectedId);
                } else {
                    // Cas ou l'on vient de l'écran détail des décisions
                    vb.setSession((globaz.globall.db.BSession) bSession);
                    vb.setIdRetour(idRetour);
                }
            }
            mainDispatcher.dispatch(vb, getAction());

            ICommunicationRetour viewBean = null;
            // Il faut tester le canton
            CPCommunicationFiscaleRetourViewBean testCanton = new CPCommunicationFiscaleRetourViewBean();
            testCanton.setSession((globaz.globall.db.BSession) bSession);
            testCanton.setIdRetour(idRetour);
            testCanton.retrieve();
            CPJournalRetour jrn = new CPJournalRetour();
            jrn.setSession((globaz.globall.db.BSession) bSession);
            jrn.setIdJournalRetour(testCanton.getIdJournalRetour());
            jrn.retrieve();

            // globaz.globall.http.JSPUtils.setBeanProperties(request,
            // viewBean);
            // On rajoute les infos de l'utilisateur qui a modifié la
            // communication
            GregorianCalendar d = new GregorianCalendar();
            int heure = d.get(Calendar.HOUR_OF_DAY);
            String heureH = String.valueOf(heure);
            if (heureH.length() == 1) {
                heureH = "0" + heureH;
            }
            int min = d.get(Calendar.MINUTE);
            String minM = String.valueOf(min);
            if (minM.length() == 1) {
                minM = "0" + minM;
            }
            int sec = d.get(Calendar.SECOND);
            String secS = String.valueOf(sec);
            if (secS.length() == 1) {
                secS = "0" + secS;
            }

            if (IConstantes.CS_LOCALITE_CANTON_JURA.equalsIgnoreCase(jrn.getCanton())) {
                CPCommunicationFiscaleRetourJUViewBean viewBean2 = new CPCommunicationFiscaleRetourJUViewBean();
                viewBean2.setSession((globaz.globall.db.BSession) bSession);
                viewBean2.setIdRetour(idRetour);
                viewBean2.retrieve();
                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean2);
                viewBean2.setLastUser(((BSession) mainDispatcher.getSession()).getUserFullName());
                viewBean2.setLastDate(JACalendar.todayJJsMMsAAAA());
                viewBean2.setLastTime(heureH + ":" + minM + ":" + secS);
                viewBean = new CPCommunicationFiscaleRetourJUViewBean();
                viewBean2.update();
            } else if (IConstantes.CS_LOCALITE_CANTON_NEUCHATEL.equalsIgnoreCase(jrn.getCanton())) {
                CPCommunicationFiscaleRetourNEViewBean viewBean2 = new CPCommunicationFiscaleRetourNEViewBean();
                viewBean2.setSession((globaz.globall.db.BSession) bSession);
                viewBean2.setIdRetour(idRetour);
                viewBean2.retrieve();
                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean2);
                viewBean2.setLastUser(((BSession) mainDispatcher.getSession()).getUserFullName());
                viewBean2.setLastDate(JACalendar.todayJJsMMsAAAA());
                viewBean2.setLastTime(heureH + ":" + minM + ":" + secS);
                viewBean = new CPCommunicationFiscaleRetourNEViewBean();
                viewBean2.update();
            } else if (IConstantes.CS_LOCALITE_CANTON_GENEVE.equalsIgnoreCase(jrn.getCanton())) {
                CPCommunicationFiscaleRetourGEViewBean viewBean2 = new CPCommunicationFiscaleRetourGEViewBean();
                viewBean2.setSession((globaz.globall.db.BSession) bSession);
                viewBean2.setIdRetour(idRetour);
                viewBean2.retrieve();
                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean2);
                viewBean2.setLastUser(((BSession) mainDispatcher.getSession()).getUserFullName());
                viewBean2.setLastDate(JACalendar.todayJJsMMsAAAA());
                viewBean2.setLastTime(heureH + ":" + minM + ":" + secS);
                viewBean = new CPCommunicationFiscaleRetourGEViewBean();
                viewBean2.update();
            } else if (IConstantes.CS_LOCALITE_CANTON_VAUD.equalsIgnoreCase(jrn.getCanton())) {
                CPCommunicationFiscaleRetourVDViewBean viewBean2 = new CPCommunicationFiscaleRetourVDViewBean();
                viewBean2.setSession((globaz.globall.db.BSession) bSession);
                viewBean2.setIdRetour(idRetour);
                viewBean2.retrieve();
                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean2);
                viewBean2.setLastUser(((BSession) mainDispatcher.getSession()).getUserFullName());
                viewBean2.setLastDate(JACalendar.todayJJsMMsAAAA());
                viewBean2.setLastTime(heureH + ":" + minM + ":" + secS);
                viewBean = new CPCommunicationFiscaleRetourVDViewBean();
                viewBean2.update();
            } else if (IConstantes.CS_LOCALITE_CANTON_VALAIS.equalsIgnoreCase(jrn.getCanton())) {
                CPCommunicationFiscaleRetourVSViewBean viewBean2 = new CPCommunicationFiscaleRetourVSViewBean();
                viewBean2.setSession((globaz.globall.db.BSession) bSession);
                viewBean2.setIdRetour(idRetour);
                viewBean2.retrieve();
                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean2);
                viewBean2.setLastUser(((BSession) mainDispatcher.getSession()).getUserFullName());
                viewBean2.setLastDate(JACalendar.todayJJsMMsAAAA());
                viewBean2.setLastTime(heureH + ":" + minM + ":" + secS);
                viewBean = new CPCommunicationFiscaleRetourVSViewBean();
                viewBean2.update();
            } else if (CPActionApercuCommunicationFiscaleRetour.CS_CANTON_SEDEX.equalsIgnoreCase(jrn.getCanton())) {
                CPCommunicationFiscaleRetourSEDEXViewBean viewBean2 = new CPCommunicationFiscaleRetourSEDEXViewBean();
                viewBean2.setSession((globaz.globall.db.BSession) bSession);
                viewBean2.setIdRetour(idRetour);
                viewBean2.retrieve();
                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean2);
                viewBean2.setLastUser(((BSession) mainDispatcher.getSession()).getUserFullName());
                viewBean2.setLastDate(JACalendar.todayJJsMMsAAAA());
                viewBean2.setLastTime(heureH + ":" + minM + ":" + secS);
                viewBean = new CPCommunicationFiscaleRetourSEDEXViewBean();
                viewBean2.update();
            }

            viewBean.setSession((globaz.globall.db.BSession) bSession);
            viewBean.setIdRetour(idRetour);
            viewBean.retrieve();

            if (_action.getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                viewBean = (ICommunicationRetour) beforeNouveau(session, request, response,
                        (FWViewBeanInterface) viewBean);
            }

            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);
            String bouton = "";

            if (idRetour == null) {
                idRetour = request.getParameter("selectedId");
            }
            // if (checkOptionAllowed(session, request, mainDispatcher,
            // idRetour))
            // bouton = "true";
            // else
            bouton = "false";
            session.setAttribute("bouton", bouton);
            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                if (JadeStringUtil.isEmpty(request.getParameter("forIdJournalRetour"))) {
                    _destination = getRelativeURL(request, session) + viewBean.getJournalRetour().getCodeCanton()
                            + "_de.jsp?allComm=yes";
                } else {
                    _destination = getRelativeURL(request, session) + viewBean.getJournalRetour().getCodeCanton()
                            + "_de.jsp?allComm=no";
                }
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    @Override
    protected void actionReAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String processLaunchedStr = request.getParameter("process");
        boolean processSeemsOk = "launched".equals(processLaunchedStr);
        String validFail = processSeemsOk ? "" : "?_valid=fail";
        servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_de.jsp" + validFail)
                .forward(request, response);

    }

    protected void actionRetournerOriginale(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        FWAction _action = FWAction.newInstance(request.getParameter("userAction"));
        try {
            /*
             * pour compatibilité : si on a le parametre _method=add, c'est que l'on a une action nouveau
             */
            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                _action.changeActionPart(FWAction.ACTION_NOUVEAU);
            }
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            // Recherche du canton
            String canton = request.getParameter("canton");
            String idRetour = request.getParameter("idRetour");
            String selectedId = request.getParameter("selectedId");
            if (JadeStringUtil.isEmpty(selectedId)) {
                if (JadeStringUtil.isEmpty(idRetour)) {
                    idRetour = (String) session.getAttribute("idRetour");
                }
            } else {
                idRetour = selectedId;
            }

            CPApercuCommunicationFiscaleRetourComViewBean vb = new CPApercuCommunicationFiscaleRetourComViewBean();
            vb.setCanton(canton);

            if (JadeStringUtil.isEmpty(canton)) {
                if (JadeStringUtil.isEmpty(idRetour)) {
                    // Cas ou l'on vient de l'écran détail des décisions
                    vb.setSession((globaz.globall.db.BSession) bSession);
                    vb.setIdRetour(selectedId);
                } else {
                    // Cas ou l'on vient de l'écran détail des décisions
                    vb.setSession((globaz.globall.db.BSession) bSession);
                    vb.setIdRetour(idRetour);
                }
            }
            mainDispatcher.dispatch(vb, getAction());

            ICommunicationRetour viewBean = vb.getCommunicationRetour();
            viewBean = retournerAOriginale(idRetour, (BSession) bSession, viewBean);
            if (_action.getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                viewBean = (ICommunicationRetour) beforeNouveau(session, request, response,
                        (FWViewBeanInterface) viewBean);
            }

            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);
            String bouton = "";

            if (idRetour == null) {
                idRetour = request.getParameter("selectedId");
            }
            if (this.checkOptionAllowed(session, request, mainDispatcher, idRetour)) {
                bouton = "true";
            } else {
                bouton = "false";
            }
            session.setAttribute("bouton", bouton);
            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                if (JadeStringUtil.isEmpty(request.getParameter("forIdJournalRetour"))) {
                    _destination = getRelativeURL(request, session) + viewBean.getJournalRetour().getCodeCanton()
                            + "_de.jsp?allComm=yes";
                } else {
                    _destination = getRelativeURL(request, session) + viewBean.getJournalRetour().getCodeCanton()
                            + "_de.jsp?allComm=no";
                }
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return super.beforeAfficher(session, request, response, viewBean);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeLister(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        CPApercuCommunicationFiscaleRetourListViewBean listViewBean = (CPApercuCommunicationFiscaleRetourListViewBean) viewBean;

        // Fixes les parametres de recherche
        String recherche = request.getParameter("reqLibelle");
        String idJournalRetour = request.getParameter("forIdJournalRetour");
        session.setAttribute("idJournalRetour", idJournalRetour);
        if (!JadeStringUtil.isBlank((recherche))) {
            String critere = request.getParameter("critere");
            if ("CRITERE_NOM".equalsIgnoreCase(critere)) {
                listViewBean.setLikeNom(recherche);
            } else if ("CRITERE_CONTRIBUABLE".equalsIgnoreCase(critere)) {
                listViewBean.setLikeNumContribuable(recherche);
            } else if ("CRITERE_PRENOM".equalsIgnoreCase(critere)) {
                listViewBean.setLikePrenom(recherche);
            } else if ("CRITERE_ANNEE".equalsIgnoreCase(critere)) {
                listViewBean.setForAnnee(recherche);
            } else if ("CRITERE_AFFILIE".equalsIgnoreCase(critere)) {
                listViewBean.setLikeNumAffilie(recherche);
            }
        }

        String orderBy = request.getParameter("trierPar");
        if (!JadeStringUtil.isBlank(orderBy)) {
            if ("ORDER_BY_NOM_PRENOM".equals(orderBy)) {
                listViewBean.setOrderBy(" TITIERP.HTLDE1, TITIERP.HTLDE2");
            } else if ("ORDER_BY_CONTRIBUABLE".equals(orderBy)) {
                listViewBean.setOrderBy("HXNCON DESC");
            } else if ("ORDER_BY_ANNEE".equals(orderBy)) {
                listViewBean.setOrderBy("IKANN1, HXNCON DESC");
            } else if ("ORDER_BY_ETAT".equals(orderBy)) {
                listViewBean.setOrderBy("IKTSTA");
            } else if ("ORDER_BY_AFFILIE".equals(orderBy)) {
                listViewBean.setOrderBy("AFAFFIP.MALNAF ASC");
            }
        }

        String forReportType = request.getParameter("forReportType");
        if (!JadeStringUtil.isBlank(forReportType)) {
            listViewBean.setForReportType(forReportType);
        }

        String likeSndId = request.getParameter("likeSenderId");
        if (!JadeStringUtil.isBlank(likeSndId)) {
            // Recherche du code canton
            try {
                globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
                likeSndId = CodeSystem.getCode((BSession) bSession, likeSndId);
                listViewBean.setLikeSenderId(likeSndId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        listViewBean.changeManagerSize(20);
        listViewBean.wantCallMethodAfter(false);

        return viewBean;
    }

    private boolean checkOptionAllowed(HttpSession session, HttpServletRequest request, FWDispatcher dispatcher) {
        try {
            String idRetour = request.getParameter("idRetour");
            if (JadeStringUtil.isEmpty(idRetour)) {
                idRetour = (String) session.getAttribute("idRetour");
            }
            return this.checkOptionAllowed(session, request, dispatcher, idRetour);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkOptionAllowed(HttpSession session, HttpServletRequest request, FWDispatcher dispatcher,
            String idRetour) {
        try {
            String idValidation = "";
            if (idRetour != null) {
                if ("reinitialiser".equals(getAction().getActionPart())) {
                    CPCommunicationFiscaleRetourViewBean retour = new CPCommunicationFiscaleRetourViewBean();
                    retour.setIdRetour(idRetour);
                    retour.setSession((BSession) dispatcher.getSession());
                    retour.retrieve();
                    if (!retour.isNew()
                            && CPCommunicationFiscaleRetourViewBean.CS_COMPTABILISE
                                    .equalsIgnoreCase(retour.getStatus())) {
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    CPValidationCalculCommunicationManager manager = new CPValidationCalculCommunicationManager();
                    manager.setSession((BSession) dispatcher.getSession());
                    manager.setForIdCommunicationRetour(idRetour);
                    manager.find();
                    CPValidationCalculCommunication communication = (CPValidationCalculCommunication) manager
                            .getFirstEntity();
                    if (communication != null) {
                        idValidation = communication.getIdValidationCommunication();
                        if (idValidation != null) {
                            CPValidationJournalRetourViewBean validation = new CPValidationJournalRetourViewBean();
                            validation.setSession((BSession) dispatcher.getSession());
                            validation.setIdValidation(idValidation);
                            validation.getIdDecision();
                            validation.retrieve();
                            if (validation.getCodeValidation().equalsIgnoreCase("1")) {
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    } else {
                        // la communication n'est pas encore dans la table
                        // calcul communication
                        return true;
                    }
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private String[] creationListeIdRetourDapresCriteresSelection(BSession session, HttpSession httpSession,
            HttpServletRequest request) {
        String[] listIdRetour = null;
        String idRetour = request.getParameter("idRetour");
        if (httpSession.getAttribute("listViewBean") != null) {
            if (CPApercuCommunicationFiscaleRetourListViewBean.class.equals(httpSession.getAttribute("listViewBean")
                    .getClass())) {
                CPApercuCommunicationFiscaleRetourListViewBean manager = (CPApercuCommunicationFiscaleRetourListViewBean) httpSession
                        .getAttribute("listViewBean");

                try {
                    manager.setForIdRetour(idRetour);
                    manager.changeManagerSize(BManager.SIZE_NOLIMIT);
                    manager.find();
                    listIdRetour = new String[manager.size()];
                } catch (Exception e) {
                    return null;
                }
                for (int i = 0; i < manager.size(); i++) {
                    CPCommunicationFiscaleRetourViewBean com = (CPCommunicationFiscaleRetourViewBean) manager
                            .getEntity(i);
                    listIdRetour[i] = com.getIdRetour();
                }
            }
            return listIdRetour;
        } else {
            return null;
        }

    }

    private String getParamIdJournal(HttpServletRequest request) {
        String idJournalRetour = request.getParameter("idJournalRetour");
        if (!JadeStringUtil.isIntegerEmpty(idJournalRetour)) {
            return "&idJournalRetour=" + idJournalRetour;
        } else {
            return "";
        }
    }

    private ICommunicationRetour retournerAOriginale(String idRetour, BSession session, ICommunicationRetour viewBean)
            throws Exception {
        ICommunicationRetour communication = null;
        CPCommunicationFiscaleRetourViewBean testCanton = new CPCommunicationFiscaleRetourViewBean();
        testCanton.setSession(session);
        testCanton.setIdRetour(idRetour);
        testCanton.retrieve();
        CPJournalRetour jrn = new CPJournalRetour();
        jrn.setSession(session);
        jrn.setIdJournalRetour(testCanton.getIdJournalRetour());
        jrn.retrieve();

        if (IConstantes.CS_LOCALITE_CANTON_JURA.equalsIgnoreCase(jrn.getCanton())) {
            CPCommunicationFiscaleRetourJUViewBean cBackup = new CPCommunicationFiscaleRetourJUViewBean();
            cBackup.setSession(session);
            cBackup.setIdRetour(idRetour);
            cBackup.setForBackup(true);
            cBackup.retrieve();
            cBackup.setForRetourOriginale(true);
            cBackup.setLastDate("");
            cBackup.setLastUser("");
            cBackup.setLastTime("");
            cBackup.setForBackup(false);
            cBackup.getSession().getCurrentThreadTransaction().disableSpy();
            cBackup.update();
            cBackup.getSession().getCurrentThreadTransaction().enableSpy();
            CPCommunicationFiscaleRetourJUViewBean c = new CPCommunicationFiscaleRetourJUViewBean();
            c.setSession(session);
            c.setIdRetour(idRetour);
            c.retrieve();
            communication = c;
        } else if (IConstantes.CS_LOCALITE_CANTON_NEUCHATEL.equalsIgnoreCase(jrn.getCanton())) {
            CPCommunicationFiscaleRetourNEViewBean cBackup = new CPCommunicationFiscaleRetourNEViewBean();
            cBackup.setSession(session);
            cBackup.setIdRetour(idRetour);
            cBackup.setForBackup(true);
            cBackup.retrieve();
            cBackup.setForRetourOriginale(true);
            cBackup.setLastDate("");
            cBackup.setLastUser("");
            cBackup.setLastTime("");
            cBackup.setForBackup(false);
            cBackup.getSession().getCurrentThreadTransaction().disableSpy();
            cBackup.update();
            cBackup.getSession().getCurrentThreadTransaction().enableSpy();
            CPCommunicationFiscaleRetourNEViewBean c = new CPCommunicationFiscaleRetourNEViewBean();
            c.setSession(session);
            c.setIdRetour(idRetour);
            c.retrieve();
            communication = c;
        } else if (IConstantes.CS_LOCALITE_CANTON_GENEVE.equalsIgnoreCase(jrn.getCanton())) {
            CPCommunicationFiscaleRetourGEViewBean cBackup = new CPCommunicationFiscaleRetourGEViewBean();
            cBackup.setSession(session);
            cBackup.setIdRetour(idRetour);
            cBackup.setForBackup(true);
            cBackup.retrieve();
            cBackup.setForRetourOriginale(true);
            cBackup.setLastDate("");
            cBackup.setLastUser("");
            cBackup.setLastTime("");
            cBackup.setForBackup(false);
            cBackup.getSession().getCurrentThreadTransaction().disableSpy();
            cBackup.update();
            cBackup.getSession().getCurrentThreadTransaction().enableSpy();
            CPCommunicationFiscaleRetourGEViewBean c = new CPCommunicationFiscaleRetourGEViewBean();
            c.setSession(session);
            c.setIdRetour(idRetour);
            c.retrieve();
            communication = c;
        } else if (IConstantes.CS_LOCALITE_CANTON_VAUD.equalsIgnoreCase(jrn.getCanton())) {
            CPCommunicationFiscaleRetourVDViewBean cBackup = new CPCommunicationFiscaleRetourVDViewBean();
            cBackup.setSession(session);
            cBackup.setIdRetour(idRetour);
            cBackup.setForBackup(true);
            cBackup.retrieve();
            cBackup.setForRetourOriginale(true);
            cBackup.setLastDate("");
            cBackup.setLastUser("");
            cBackup.setLastTime("");
            cBackup.setForBackup(false);
            cBackup.getSession().getCurrentThreadTransaction().disableSpy();
            cBackup.update();
            cBackup.getSession().getCurrentThreadTransaction().enableSpy();
            CPCommunicationFiscaleRetourVDViewBean c = new CPCommunicationFiscaleRetourVDViewBean();
            c.setSession(session);
            c.setIdRetour(idRetour);
            c.retrieve();
            communication = c;
        } else if (IConstantes.CS_LOCALITE_CANTON_VALAIS.equalsIgnoreCase(jrn.getCanton())) {
            CPCommunicationFiscaleRetourVSViewBean cBackup = new CPCommunicationFiscaleRetourVSViewBean();
            cBackup.setSession(session);
            cBackup.setIdRetour(idRetour);
            cBackup.setForBackup(true);
            cBackup.retrieve();
            cBackup.setForRetourOriginale(true);
            cBackup.setLastDate("");
            cBackup.setLastUser("");
            cBackup.setLastTime("");
            cBackup.setForBackup(false);
            cBackup.getSession().getCurrentThreadTransaction().disableSpy();
            cBackup.update();
            cBackup.getSession().getCurrentThreadTransaction().enableSpy();
            CPCommunicationFiscaleRetourVSViewBean c = new CPCommunicationFiscaleRetourVSViewBean();
            c.setSession(session);
            c.setIdRetour(idRetour);
            c.retrieve();
            communication = c;
        } else if (CPActionApercuCommunicationFiscaleRetour.CS_CANTON_SEDEX.equalsIgnoreCase(jrn.getCanton())) {
            CPCommunicationFiscaleRetourSEDEXViewBean cBackup = new CPCommunicationFiscaleRetourSEDEXViewBean();
            CPCommunicationFiscaleRetourSEDEXViewBean c = new CPCommunicationFiscaleRetourSEDEXViewBean();

            // Contribuable
            CPSedexContribuable contri = new CPSedexContribuable();
            contri.setSession(session);
            contri.setIdRetour(idRetour);
            contri.setForBackup(true);
            contri.retrieve();
            contri.setForRetourOriginale(true);
            contri.setForBackup(false);
            contri.update();
            // Conjoint
            CPSedexConjoint conjoint = new CPSedexConjoint();
            conjoint.setSession(session);
            conjoint.setIdRetour(idRetour);
            conjoint.setForBackup(true);
            conjoint.retrieve();
            conjoint.setForRetourOriginale(true);
            conjoint.setForBackup(false);
            conjoint.update();
            // Base
            CPSedexDonneesBase base = new CPSedexDonneesBase();
            base.setSession(session);
            base.setIdRetour(idRetour);
            base.setForBackup(true);
            base.retrieve();
            base.setForRetourOriginale(true);
            base.setForBackup(false);
            base.update();
            // Commerce
            CPSedexDonneesCommerciales commerce = new CPSedexDonneesCommerciales();
            commerce.setSession(session);
            commerce.setIdRetour(idRetour);
            commerce.setForBackup(true);
            commerce.retrieve();
            commerce.setForRetourOriginale(true);
            commerce.setForBackup(false);
            commerce.update();
            // prive
            CPSedexDonneesPrivees prive = new CPSedexDonneesPrivees();
            prive.setSession(session);
            prive.setIdRetour(idRetour);
            prive.setForBackup(true);
            prive.retrieve();
            prive.setForBackup(false);
            prive.setForRetourOriginale(true);
            prive.update();

            cBackup.setSession(session);
            cBackup.setIdRetour(idRetour);
            cBackup.setForBackup(true);
            cBackup.retrieve();

            c.setSession(session);
            c.setIdRetour(idRetour);
            c.retrieve();

            cBackup.setForRetourOriginale(true);
            cBackup.setLastDate("");
            cBackup.setLastUser("");
            cBackup.setLastTime("");
            cBackup.setForBackup(false);
            cBackup.update();

            communication = c;
        }

        // Réinitialisation de la communication
        if (!JadeStringUtil.isEmpty(idRetour)) {
            CPProcessCommunicationRetourReinitialiser reinit = new CPProcessCommunicationRetourReinitialiser();
            String listIdRetour[] = { idRetour };
            reinit.setListIdRetour(listIdRetour);
            reinit.setISession(session);
            reinit.setSendMailOnError(false);
            reinit.setSendCompletionMail(false);
            reinit.executeProcess();
        }

        return communication;

    }
}
