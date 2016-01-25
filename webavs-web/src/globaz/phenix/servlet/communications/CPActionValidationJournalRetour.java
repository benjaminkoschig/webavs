/*
 * Créé le 14 mars 2007
 * 
 * @author JPA
 */
package globaz.phenix.servlet.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.communications.CPDevalidationJournalRetourListViewBean;
import globaz.phenix.db.communications.CPValidationJournalRetourListViewBean;
import globaz.phenix.db.communications.CPValidationJournalRetourViewBean;
import globaz.phenix.db.principale.CPDecisionViewBean;
import globaz.phenix.process.communications.CPProcessAbandonSelectionJournalRetourViewBean;
import globaz.phenix.process.communications.CPProcessDevalidationSelectionJournalRetourViewBean;
import globaz.phenix.process.communications.CPProcessValidationSelectionJournalRetourViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author mmu Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPActionValidationJournalRetour extends FWDefaultServletAction {

    public CPActionValidationJournalRetour(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Execution du process dans le Helper pour passer par les droits remplacement du forward par un goSendRedirect car
     * la redirection se fait sur une action
     */
    private void _actionAbandonner(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            String idValidation = request.getParameter("selectedId");
            CPValidationJournalRetourViewBean vb = new CPValidationJournalRetourViewBean();
            vb.setSession((BSession) dispatcher.getSession());
            vb.setIdValidation(idValidation);
            dispatcher.dispatch(vb, getAction());
            // On recharge la com à valider et on change le codeValidation
            // CPProcessAbandonSelectionJournalRetourViewBean abandon = new
            // CPProcessAbandonSelectionJournalRetourViewBean();
            // abandon.setSession((BSession) dispatcher.getSession());
            // abandon.setForIdValidationCommunication(idValidation);
            // abandon.setSendMailOnError(true);
            // abandon.setSendCompletionMail(false);
            // abandon.start();
            _destination = "/phenix?userAction=phenix.communications.validationJournalRetour.chercher";
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * affiche la prochaine page
         */
        // servlet.getServletContext().getRequestDispatcher(_destination).forward(request,
        // response);
        goSendRedirect(_destination, request, response);
    }

    /**
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * 
     *             COMMENTAIRE: Ici aucune modification du fait que le forward se fait sur une page jsp spécifique
     *             (urlWithoutClassPart) la méthode ci-dessous permet uniquement la redirection vers la jsp et n'execute
     *             aucune autre action par conséquence nous pouvons la laisser dans cet etat
     * 
     */
    private void _actionAbandonnerSelection(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = getRelativeURLwithoutClassPart(request, session)
                + "abandonnerSelectionJournalRetour_de.jsp";
        try {
            CPProcessAbandonSelectionJournalRetourViewBean viewBean = new CPProcessAbandonSelectionJournalRetourViewBean();
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            JSPUtils.setBeanProperties(request, viewBean);
            session.setAttribute("viewBean", viewBean);
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * 
     *             HACK : On instancie un nouveau viewBean dans lequel on set les variables dont nous avons besoin à la
     *             place de les setter directement dans le process. Le process est maintenant executé dans le Helper de
     *             la classe Modification du forward en goSendRedirect car la redirection se fait sur une action et non
     *             une jsp
     */
    private void _actionDevalider(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            String idValidation = request.getParameter("selectedId");
            CPValidationJournalRetourViewBean viewBean = new CPValidationJournalRetourViewBean();
            viewBean.setSession((BSession) dispatcher.getSession());
            viewBean.setIdValidation(idValidation);
            dispatcher.dispatch(viewBean, getAction());

            // CPProcessDevalidationSelectionJournalRetourViewBean devalidation
            // = new CPProcessDevalidationSelectionJournalRetourViewBean();
            // devalidation.setSession((BSession) dispatcher.getSession());
            // devalidation.setForIdValidationCommunication(idValidation);
            // devalidation.start();
            _destination = "/phenix?userAction=phenix.communications.devalidationJournalRetour.chercher";
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * affiche la prochaine page
         */
        // servlet.getServletContext().getRequestDispatcher(_destination).forward(request,
        // response);
        goSendRedirect(_destination, request, response);
    }

    /**
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * 
     *             COMMENTAIRE: Ici aucune modification du fait que le forward se fait sur une page jsp spécifique
     *             (urlWithoutClassPart) la méthode ci-dessous permet uniquement la redirection vers la jsp et n'execute
     *             aucune autre action par conséquence nous pouvons la laisser dans cet etat
     */
    private void _actionDevaliderSelection(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = getRelativeURLwithoutClassPart(request, session)
                + "devalidationSelectionJournalRetour_de.jsp";
        try {
            CPProcessDevalidationSelectionJournalRetourViewBean viewBean = new CPProcessDevalidationSelectionJournalRetourViewBean();
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            JSPUtils.setBeanProperties(request, viewBean);
            session.setAttribute("viewBean", viewBean);
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * 
     *             HACK: Redirection sur action = modification du forward en goSendRedirect les actions retrieve et
     *             delete sont maintenant executées dans le Helper de la classe
     */
    private void _actionExecuterSuppression(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "/phenix?userAction=phenix.communications.validationJournalRetour.chercher";
        try {
            String idValidation = request.getParameter("selectedId");

            CPValidationJournalRetourViewBean viewBean = new CPValidationJournalRetourViewBean();
            viewBean.setSession((BSession) dispatcher.getSession());
            viewBean.setIdValidation(idValidation);
            dispatcher.dispatch(viewBean, getAction());

            // if (idValidation != null) {
            // CPValidationJournalRetourViewBean validation = new
            // CPValidationJournalRetourViewBean();
            // validation.setSession((BSession) dispatcher.getSession());
            // validation.setIdValidation(idValidation);
            // CPCommunicationFiscaleRetourViewBean communication = new
            // CPCommunicationFiscaleRetourViewBean();
            // communication.setSession((BSession) dispatcher.getSession());
            // validation.retrieve();
            // communication.setIdRetour(validation.getIdRetour());
            // communication.retrieve();
            // communication.delete();
            // }
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        // servlet.getServletContext().getRequestDispatcher(_destination).forward(request,
        // response);
        goSendRedirect(_destination, request, response);
    }

    /**
     * HACK : Rajout de getter/setter dans CPDecisionViewBean de manière à pouvoir renseigné l'idValidation
     * l'instanciation de la classe CPValidationJournalRetourViewBean se fait désormais dans le Helper de la classe avec
     * les différents retrieves
     */
    private void _actionModifierDecision(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";

        try {
            String idValidation = request.getParameter("selectedId");

            CPDecisionViewBean viewBean = new CPDecisionViewBean();
            viewBean.setSession((BSession) dispatcher.getSession());
            viewBean.setIdValidation(idValidation);

            dispatcher.dispatch(viewBean, getAction());
            // globaz.phenix.db.communications.CPValidationJournalRetourViewBean
            // validation = new
            // globaz.phenix.db.communications.CPValidationJournalRetourViewBean();
            // validation.setSession((BSession) dispatcher.getSession());
            // validation.setIdValidation(idValidation);
            // validation.retrieve();
            // globaz.phenix.db.principale.CPDecisionViewBean viewBean = new
            // globaz.phenix.db.principale.CPDecisionViewBean();
            // viewBean.setSession((BSession) dispatcher.getSession());
            // viewBean.setIdDecision(validation.getIdDecision());
            // viewBean.setIdTiers(validation.getIdTiers());
            // viewBean.retrieve();
            session.setAttribute("viewBean", viewBean);
            _destination = getRelativeURLwithoutClassPart(request, session) + "validationJournalRetour_de.jsp";
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * 
     *             HACK: Nouvelle instance de CPValidationJournalRetourViewBean pour prendre les valeurs à la place de
     *             les setter dans le process Execution du process dans le Helper de la classe Modification du forward
     *             en goSendRedirect car la redirection se fait sur une action et non une jsp
     */
    private void _actionProcessAbandonnerSelection(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {

            CPValidationJournalRetourViewBean vb = new CPValidationJournalRetourViewBean();
            vb.setSession((BSession) dispatcher.getSession());
            vb.setForAnnee(request.getParameter("forAnnee"));
            vb.setLikeNumAffilie(request.getParameter("likeNumAffilie"));
            vb.setForGrpTaxation(request.getParameter("forGrpTaxation"));
            vb.setForGrpExtraction(request.getParameter("forGrpExtraction"));
            vb.setGenreAffilie(request.getParameter("forGenreAffilie"));
            vb.setForJournal(request.getParameter("forJournal"));
            vb.setTypeDecision(request.getParameter("forTypeDecision"));
            vb.setEMailAdress(request.getParameter("eMailAddress"));
            vb.setIdJournal(request.getParameter("idJournalFacturation")); // id
            // journal
            // facturation

            dispatcher.dispatch(vb, getAction());

            // CPProcessAbandonSelectionJournalRetourViewBean viewBean = new
            // CPProcessAbandonSelectionJournalRetourViewBean();
            // globaz.globall.api.BISession bSession =
            // globaz.phenix.translation.CodeSystem.getSession(session);
            // viewBean.setISession(bSession);
            // viewBean.setForAnnee(request.getParameter("forAnnee"));
            // viewBean.setLikeNumAffilie(request.getParameter("likeNumAffilie"));
            // viewBean.setForGrpTaxation(request.getParameter("forGrpTaxation"));
            // viewBean.setForGenreAffilie(request.getParameter("forGenreAffilie"));
            // viewBean.setForGrpExtraction(request.getParameter("forGrpExtraction"));
            // viewBean.setForTypeDecision(request.getParameter("forTypeDecision"));
            // viewBean.setForJournal(request.getParameter("forJournal"));
            // viewBean.setEMailAddress(request.getParameter("eMailAddress"));
            // viewBean.setIdJournalFacturation(request.getParameter("idJournalFacturation"));
            // viewBean.setSendCompletionMail(true);
            // viewBean.setSendMailOnError(true);
            // viewBean.start();
            // // viewBean.executeProcess();
            // if (viewBean.getMsgType().equals(BProcess.ERROR)) {
            // viewBean.setMsgType(viewBean.getMsgType());
            // viewBean.setMessage(viewBean.getMessage());
            // }
            _destination = "/phenix?userAction=phenix.communications.validationJournalRetour.chercher";
            if (vb.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = _destination + "&_valid=fail";
            } else {
                _destination = _destination + "&process=launched";
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        // servlet.getServletContext().getRequestDispatcher(_destination).forward(request,
        // response);
        goSendRedirect(_destination, request, response);
    }

    /**
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * 
     *             HACK: Instanciation d'un viewBean qui prend les paramètres nécessaires au process L'execution du
     *             process se fait maintenant dans le Helper de la classe remplacement du forward par un goSendRedirect
     *             car la redirection se fait sur une action et non une jsp
     * 
     */
    private void _actionProcessDevaliderSelection(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {

            CPValidationJournalRetourViewBean vb = new CPValidationJournalRetourViewBean();
            vb.setSession((BSession) dispatcher.getSession());
            vb.setForAnnee(request.getParameter("forAnnee"));
            vb.setLikeNumAffilie(request.getParameter("likeNumAffilie"));
            vb.setForGrpTaxation(request.getParameter("forGrpTaxation"));
            vb.setForGrpExtraction(request.getParameter("forGrpExtraction"));
            vb.setGenreAffilie(request.getParameter("forGenreAffilie"));
            vb.setForJournal(request.getParameter("forJournal"));
            vb.setTypeDecision(request.getParameter("forTypeDecision"));
            vb.setEMailAdress(request.getParameter("eMailAddress"));
            vb.setIdJournal(request.getParameter("idJournalFacturation")); // id
            // journal
            // facturation
            dispatcher.dispatch(vb, getAction());

            // CPProcessDevalidationSelectionJournalRetourViewBean viewBean =
            // new CPProcessDevalidationSelectionJournalRetourViewBean();
            // globaz.globall.api.BISession bSession =
            // globaz.phenix.translation.CodeSystem.getSession(session);
            // viewBean.setISession(bSession);
            // viewBean.setForAnnee(request.getParameter("forAnnee"));
            // viewBean.setLikeNumAffilie(request.getParameter("likeNumAffilie"));
            // viewBean.setForGrpTaxation(request.getParameter("forGrpTaxation"));
            // viewBean.setForGrpExtraction(request.getParameter("forGrpExtraction"));
            // viewBean.setForGenreAffilie(request.getParameter("forGenreAffilie"));
            // viewBean.setForJournal(request.getParameter("forJournal"));
            // viewBean.setForTypeDecision(request.getParameter("forTypeDecision"));
            // viewBean.setEMailAddress(request.getParameter("eMailAddress"));
            // viewBean.setIdJournalFacturation(request.getParameter("idJournalFacturation"));
            // viewBean.setSendCompletionMail(true);
            // viewBean.setSendMailOnError(true);
            // viewBean.start();
            // // viewBean.executeProcess();
            // if (viewBean.getMsgType().equals(BProcess.ERROR)) {
            // viewBean.setMsgType(viewBean.getMsgType());
            // viewBean.setMessage(viewBean.getMessage());
            // }
            _destination = "/phenix?userAction=phenix.communications.devalidationJournalRetour.chercher";
            if (vb.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = _destination + "&_valid=fail";
            } else {
                _destination = _destination + "&process=launched";
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        // servlet.getServletContext().getRequestDispatcher(_destination).forward(request,
        // response);
        goSendRedirect(_destination, request, response);
    }

    /**
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * 
     *             HACK: Instanciation d'un viewBean qui prend les paramètres nécessaires au process L'execution du
     *             process se fait maintenant dans le Helper de la classe remplacement du forward par un goSendRedirect
     *             car la redirection se fait sur une action et non une jsp
     */
    private void _actionProcessValiderSelection(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {
            CPValidationJournalRetourViewBean vb = new CPValidationJournalRetourViewBean();
            vb.setSession((BSession) dispatcher.getSession());
            vb.setForAnnee(request.getParameter("forAnnee"));
            vb.setLikeNumAffilie(request.getParameter("likeNumAffilie"));
            vb.setForGrpTaxation(request.getParameter("forGrpTaxation"));
            vb.setForGrpExtraction(request.getParameter("forGrpExtraction"));
            vb.setGenreAffilie(request.getParameter("forGenreAffilie"));
            vb.setForJournal(request.getParameter("forJournal"));
            vb.setTypeDecision(request.getParameter("forTypeDecision"));
            vb.setEMailAdress(request.getParameter("eMailAddress"));
            vb.setIdJournal(request.getParameter("idJournalFacturation")); // id
            // journal
            // facturation

            dispatcher.dispatch(vb, getAction());

            // CPProcessValidationSelectionJournalRetourViewBean viewBean = new
            // CPProcessValidationSelectionJournalRetourViewBean();
            // globaz.globall.api.BISession bSession =
            // globaz.phenix.translation.CodeSystem.getSession(session);
            // viewBean.setISession(bSession);
            // viewBean.setForAnnee(request.getParameter("forAnnee"));
            // viewBean.setLikeNumAffilie(request.getParameter("likeNumAffilie"));
            // viewBean.setForGrpTaxation(request.getParameter("forGrpTaxation"));
            // viewBean.setForGrpExtraction(request.getParameter("forGrpExtraction"));
            // viewBean.setForGenreAffilie(request.getParameter("forGenreAffilie"));
            // viewBean.setForJournal(request.getParameter("forJournal"));
            // viewBean.setForTypeDecision(request.getParameter("forTypeDecision"));
            // viewBean.setEMailAddress(request.getParameter("eMailAddress"));
            // viewBean.setIdJournalFacturation(request.getParameter("idJournalFacturation"));
            // viewBean.setSendCompletionMail(true);
            // viewBean.setSendMailOnError(true);
            // viewBean.start();
            // if (viewBean.getMsgType().equals(BProcess.ERROR)) {
            // viewBean.setMsgType(viewBean.getMsgType());
            // viewBean.setMessage(viewBean.getMessage());
            // }
            _destination = "/phenix?userAction=phenix.communications.validationJournalRetour.chercher";
            if (vb.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = _destination + "&_valid=fail";
            } else {
                _destination = _destination + "&process=launched";
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        // servlet.getServletContext().getRequestDispatcher(_destination).forward(request,
        // response);
        goSendRedirect(_destination, request, response);
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * 
     * 
     *            les retrieves sont maintenant executés dans le Helper pour passer par les droits on redirige sur une
     *            action alors on change le forward par un goSendRedirect
     */
    private void _actionValider(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            String idValidation = request.getParameter("selectedId");
            CPValidationJournalRetourViewBean viewBean = new CPValidationJournalRetourViewBean();
            viewBean.setSession((BSession) dispatcher.getSession());
            viewBean.setIdValidation(idValidation);

            dispatcher.dispatch(viewBean, getAction());

            // viewBean.retrieve();
            // On recharge la com à valider et on change le codeValidation
            // CPValidationCalculCommunication validation = new
            // CPValidationCalculCommunication();
            // validation.setSession((BSession) dispatcher.getSession());
            // validation.setIdValidationCommunication(idValidation);
            // validation.retrieve();
            // if (!validation.isNew()) {
            // validation.setValidation(Boolean.FALSE);
            // validation.update();
            // }
            _destination = "/phenix?userAction=phenix.principale.decisionValider.afficher&_method=upd&selectedId="
                    + viewBean.getIdDecision() + "&provenance=journalRetour";
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = _destination + "&_valid=fail";
            } else {
                _destination = _destination + "&process=launched";
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * affiche la prochaine page
         */
        // servlet.getServletContext().getRequestDispatcher(_destination).forward(request,
        // response);
        goSendRedirect(_destination, request, response);
    }

    /**
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * 
     *             COMMENTAIRE: Ici aucune modification du fait que le forward se fait sur une page jsp spécifique
     *             (urlWithoutClassPart) la méthode ci-dessous permet uniquement la redirection vers la jsp et n'execute
     *             aucune autre action par conséquence nous pouvons la laisser dans cet etat
     */
    private void _actionValiderSelection(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = getRelativeURLwithoutClassPart(request, session)
                + "validationSelectionJournalRetour_de.jsp";
        try {
            CPProcessValidationSelectionJournalRetourViewBean viewBean = new CPProcessValidationSelectionJournalRetourViewBean();
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            JSPUtils.setBeanProperties(request, viewBean);
            session.setAttribute("viewBean", viewBean);
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        // --- Variables
        CPValidationJournalRetourViewBean viewBean = new CPValidationJournalRetourViewBean();
        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        session.setAttribute("viewBean", viewBean);

        super.actionChercher(session, request, response, mainDispatcher);
    }

    // protected void actionModifier(HttpSession session, HttpServletRequest
    // request, HttpServletResponse response, FWDispatcher mainDispatcher)
    // throws ServletException, IOException {
    // super.actionModifier(session, request, response, mainDispatcher);
    // // On valida la communication
    // try {
    // CPProcessCalculCotisation calcul = new CPProcessCalculCotisation();
    // globaz.globall.api.BISession bSession =
    // globaz.phenix.translation.CodeSystem.getSession(session);
    // calcul.setIdDecision((String) request.getParameter("idDecision"));
    // calcul.setISession((globaz.globall.db.BSession) bSession);
    // calcul.setSendMailOnError(true);
    // calcul.setSendCompletionMail(false);
    // calcul.executeProcess();
    // } catch (Exception e) {
    // e.toString();
    // }
    // }

    // protected void actionLister(HttpSession session, HttpServletRequest
    // request, HttpServletResponse response, FWDispatcher mainDispatcher)
    // throws ServletException, IOException {
    // super.actionLister(session, request, response, mainDispatcher);
    // }

    /**
     * HACK : On laisse le checkOption qui retourne un boolean et qui n'influe pas sur les droits
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if ("processValiderSelection".equals(getAction().getActionPart())) {
            _actionProcessValiderSelection(session, request, response, dispatcher);
        } else if ("processDevaliderSelection".equals(getAction().getActionPart())) {
            _actionProcessDevaliderSelection(session, request, response, dispatcher);
        } else if ("validerSelection".equals(getAction().getActionPart())) {
            _actionValiderSelection(session, request, response, dispatcher);
        } else if ("devaliderSelection".equals(getAction().getActionPart())) {
            _actionDevaliderSelection(session, request, response, dispatcher);
        } else if ("devalider".equals(getAction().getActionPart())) {
            _actionDevalider(session, request, response, dispatcher);
        } else if ("abandonner".equals(getAction().getActionPart())) {
            _actionAbandonner(session, request, response, dispatcher);
        } else if ("abandonnerSelection".equals(getAction().getActionPart())) {
            _actionAbandonnerSelection(session, request, response, dispatcher);
        } else if ("processAbandonnerSelection".equals(getAction().getActionPart())) {
            _actionProcessAbandonnerSelection(session, request, response, dispatcher);
        }
        if (("valider".equals(getAction().getActionPart()))
                || ("executerSuppression".equals(getAction().getActionPart()))
                || ("modifierDecision".equals(getAction().getActionPart()))) {
            // Pour ces 3 actions on doit tester l'état pour savoir si elles
            // sont autorisés
            if (checkOptionAllowed(session, request, dispatcher)) {
                if ("valider".equals(getAction().getActionPart())) {
                    _actionValider(session, request, response, dispatcher);
                } else if ("executerSuppression".equals(getAction().getActionPart())) {
                    _actionExecuterSuppression(session, request, response, dispatcher);
                } else if ("modifierDecision".equals(getAction().getActionPart())) {
                    _actionModifierDecision(session, request, response, dispatcher);
                }
            } else {
                // mettre une erreur
                session.setAttribute("errorMessage", "Cette option pour cet état est interdite");
                // servlet.getServletContext().getRequestDispatcher("/phenix?userAction=phenix.communications.apercuCommunicationFiscaleRetour.abandonner").forward(request,
                // response);
                goSendRedirect("/phenix?userAction=phenix.communications.apercuCommunicationFiscaleRetour.abandonner",
                        request, response);
            }
        }
    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean.getClass() == CPValidationJournalRetourListViewBean.class) {
            CPValidationJournalRetourListViewBean listViewBean = (CPValidationJournalRetourListViewBean) viewBean;
            listViewBean.setWhitPavsAffilie(true);
            listViewBean.setWhitPersAffilie(true);
            listViewBean.setWhitAffiliation(true);
            listViewBean.setWhitPavsConjoint(true);
            listViewBean.setWhitPersConjoint(true);
            listViewBean.setWhitAffiliationConjoint(true);
            if (!JadeStringUtil.isNull(request.getParameter("forValide"))) {
                if (request.getParameter("forValide").equalsIgnoreCase("on")) {
                    listViewBean.setForValide(Boolean.TRUE);
                }
            }
            if (!JadeStringUtil.isNull(request.getParameter("isDevalidation"))) {
                if (request.getParameter("isDevalidation").equalsIgnoreCase("true")) {
                    listViewBean.setIsDevalidation(Boolean.TRUE);
                }
            }
            session.setAttribute("listViewBean", listViewBean);
            return listViewBean;
        }

        else {
            CPDevalidationJournalRetourListViewBean listViewBean = (CPDevalidationJournalRetourListViewBean) viewBean;
            listViewBean.setWhitPavsAffilie(true);
            listViewBean.setWhitPersAffilie(true);
            listViewBean.setWhitAffiliation(true);
            listViewBean.setWhitPavsConjoint(true);
            listViewBean.setWhitPersConjoint(true);
            listViewBean.setWhitAffiliationConjoint(true);
            if (!JadeStringUtil.isNull(request.getParameter("forValide"))) {
                if (request.getParameter("forValide").equalsIgnoreCase("on")) {
                    listViewBean.setForValide(Boolean.TRUE);
                }
            }
            if (!JadeStringUtil.isNull(request.getParameter("isDevalidation"))) {
                if (request.getParameter("isDevalidation").equalsIgnoreCase("true")) {
                    listViewBean.setIsDevalidation(Boolean.TRUE);
                }
            }
            session.setAttribute("listViewBean", listViewBean);
            return listViewBean;
        }

    }

    private boolean checkOptionAllowed(HttpSession session, HttpServletRequest request, FWDispatcher dispatcher) {
        try {
            String idValidation = request.getParameter("selectedId");
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
        } catch (Exception e) {
            return false;
        }
    }
}
