/*
 * Créé le 17 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.servlet.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.db.communications.CPJournalRetourViewBean;
import globaz.phenix.process.communications.CPProcessAbandonnerEnMasseViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author mmu Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPActionJournalRetour extends FWDefaultServletAction {
    /**
     * Constructeur
     */
    public CPActionJournalRetour(FWServlet servlet) {
        super(servlet);

    }

    /**
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     *            Retrieve désormais appellé dans le Helper de la classe On conserve le forward car la redirection se
     *            fait sur une jsp et non pas une action
     */
    private void _actionAbandonner(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";

        try {
            String idJournalRetour = request.getParameter("idJournalRetour");
            CPJournalRetourViewBean journal = new CPJournalRetourViewBean();
            journal.setISession(dispatcher.getSession());
            journal.setIdJournalRetour(idJournalRetour);

            dispatcher.dispatch(journal, getAction());
            // journal.retrieve();
            session.setAttribute("viewBean", journal);
            _destination = getRelativeURLwithoutClassPart(request, session) + "processCommunicationAbandonner_de.jsp";
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        /*
         * affiche la prochaine page
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    private void _actionAbandonnerEnMasse(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";

        try {
            String[] listIdJournalRetour = request.getParameterValues("listIdJournalRetour");

            CPProcessAbandonnerEnMasseViewBean viewBean = new CPProcessAbandonnerEnMasseViewBean();
            viewBean.setISession(dispatcher.getSession());
            viewBean.setListIdJournalRetour(listIdJournalRetour);

            dispatcher.dispatch(viewBean, getAction());
            session.setAttribute("viewBean", viewBean);
            _destination = getRelativeURLwithoutClassPart(request, session)
                    + "processCommunicationAbandonnerEnMasse_de.jsp";

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     *            HACK : variables setter dans le viewBean a la place du process Process executer dans le Helper pour
     *            passer par la gestion des droits avant execution goSendRedirect pour la redirection sur une action
     */
    private void _actionExecuterAbandonner(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {
            CPJournalRetourViewBean viewBean = (CPJournalRetourViewBean) session.getAttribute("viewBean");
            viewBean.setEMailAddress(request.getParameter("eMailAddress"));
            viewBean.setFromNumAffilie(request.getParameter("fromNumAffilie"));
            viewBean.setTillNumAffilie(request.getParameter("tillNumAffilie"));
            viewBean.setStatus(request.getParameter("forStatus"));
            viewBean.setForIdPlausibilite(request.getParameter("forIdPlausibilite"));
            viewBean.setSession((BSession) dispatcher.getSession());

            dispatcher.dispatch(viewBean, getAction());
            // Crée le process qui supprime dans la BD
            // CPProcessAbandonner process = new CPProcessAbandonner();
            // process.setIdJournalRetour(viewBean.getIdJournalRetour());
            // process.setEMailAddress(request.getParameter ("eMailAddress"));
            // process.setFromNumAffilie(request.getParameter("fromNumAffilie"));
            // process.setTillNumAffilie(request.getParameter("tillNumAffilie"));
            // process.setForStatus(request.getParameter ("forStatus"));
            // process.setForIdPlausibilite(request.getParameter("forIdPlausibilite"));
            // process.setSession((BSession)dispatcher.getSession());
            // process.start();
            // if (process.getMsgType().equals(BProcess.ERROR)) {
            // viewBean.setMsgType(process.getMsgType());
            // viewBean.setMessage(process.getMessage());
            // }
            _destination = "/phenix?userAction=phenix.communications.journalRetour.abandonner";
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
        System.out.println(request.getContextPath() + _destination);
        // response.sendRedirect(request.getContextPath() + _destination);
        goSendRedirect(_destination, request, response);

    }

    /**
     * Action qui permet de lancer le processus d'abandon en masse de journaux
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    private void _actionExecuterAbandonnerEnMasse(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = "/phenix?userAction=phenix.communications.journalRetour.abandonnerEnMasse";

        try {
            CPProcessAbandonnerEnMasseViewBean viewBean = (CPProcessAbandonnerEnMasseViewBean) session
                    .getAttribute("viewBean");
            viewBean.setEMailAddress(request.getParameter("eMailAddress"));
            viewBean.setFromNumAffilie(request.getParameter("fromNumAffilie"));
            viewBean.setTillNumAffilie(request.getParameter("tillNumAffilie"));
            viewBean.setForStatus(request.getParameter("forStatus"));
            viewBean.setForIdPlausibilite(request.getParameter("forIdPlausibilite"));
            viewBean.setSession((BSession) dispatcher.getSession());

            dispatcher.dispatch(viewBean, getAction());

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination += "&_valid=fail";
            } else {
                destination += "&process=launched";
            }

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirect(destination, request, response);

    }

    /**
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     *            HACK : On rajouter un nouveau getter/setter dans le viewbean de manière a récupérer la valeur de
     *            idPlausibilite l'execution du process se fait désormais dans le Helper de la classe Modification du
     *            forward en goSendredirect car on appelle une action et non pas une JSP
     */
    private void _actionExecuterGenerer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            CPJournalRetourViewBean viewBean = (CPJournalRetourViewBean) session.getAttribute("viewBean");
            viewBean.setSession((BSession) dispatcher.getSession());
            viewBean.setEMailAddress(request.getParameter("eMailAddress"));
            viewBean.setFromNumAffilie(request.getParameter("fromNumAffilie"));
            viewBean.setTillNumAffilie(request.getParameter("tillNumAffilie"));
            viewBean.setStatus(request.getParameter("forStatus"));
            viewBean.setForIdPlausibilite(request.getParameter("forIdPlausibilite"));

            dispatcher.dispatch(viewBean, getAction());
            // Crée le process qui inserere le fichier dans la BD
            // CPProcessReceptionGenererDecision process = new
            // CPProcessReceptionGenererDecision();
            // process.setEMailAddress(request.getParameter ("eMailAddress"));
            // process.setIdJournal(viewBean.getIdJournalRetour());
            // process.setIdPassage(viewBean.getIdJournalFacturation());
            // process.setFromNumAffilie(request.getParameter("fromNumAffilie"));
            // process.setTillNumAffilie(request.getParameter("tillNumAffilie"));
            // process.setForStatus(request.getParameter("forStatus"));
            // process.setForIdPlausibilite(request.getParameter("forIdPlausibilite"));
            // process.setSession((BSession)dispatcher.getSession());
            // process.start();
            // if (process.getMsgType().equals(BProcess.ERROR)) {
            // viewBean.setMsgType(process.getMsgType());
            // viewBean.setMessage(process.getMessage());
            // }
            _destination = "/phenix?userAction=phenix.communications.journalRetour.generer";
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
        // servlet.getServletContext().getRequestDispatcher
        // (_destination).forward (request, response);
        goSendRedirect(_destination, request, response);

    }

    /**
     * Action qui permet de lancer le processus de génération en masse de journaux
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    private void _actionExecuterGenererEnMasse(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = "/phenix?userAction=phenix.communications.journalRetour.genererEnMasse";

        try {
            CPJournalRetourViewBean viewBean = (CPJournalRetourViewBean) session.getAttribute("viewBean");
            viewBean.setEMailAddress(request.getParameter("eMailAddress"));
            viewBean.setFromNumAffilie(request.getParameter("fromNumAffilie"));
            viewBean.setTillNumAffilie(request.getParameter("tillNumAffilie"));
            viewBean.setStatus(request.getParameter("forStatus"));
            viewBean.setForIdPlausibilite(request.getParameter("forIdPlausibilite"));
            viewBean.setSession((BSession) dispatcher.getSession());

            dispatcher.dispatch(viewBean, getAction());

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination += "&_valid=fail";
            } else {
                destination += "&process=launched";
            }

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirect(destination, request, response);

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

            String orderBy = request.getParameter("orderBy");
            String impression = request.getParameter("impression");

            CPJournalRetourViewBean viewBean = (CPJournalRetourViewBean) session.getAttribute("viewBean");
            viewBean.setSession((BSession) dispatcher.getSession());
            viewBean.setEMailAddress(request.getParameter("eMailAddress"));
            viewBean.setFromNumAffilie(request.getParameter("fromNumAffilie"));
            viewBean.setTillNumAffilie(request.getParameter("tillNumAffilie"));
            viewBean.setStatus(request.getParameter("forStatus"));
            viewBean.setForIdPlausibilite(request.getParameter("forIdPlausibilite"));
            viewBean.setForGenreAffilie(request.getParameter("forGenreAffilie"));
            viewBean.setImpression(impression);
            if ("on".equalsIgnoreCase(request.getParameter("wantDetail"))) {
                viewBean.setWantDetail(Boolean.TRUE);
            } else {
                viewBean.setWantDetail(Boolean.FALSE);
            }
            viewBean.setOrderBy(orderBy);
            dispatcher.dispatch(viewBean, getAction());
            // Crée le process qui supprime dans la BD
            // CPProcessCommunicationRetourImprimer process = new
            // CPProcessCommunicationRetourImprimer();
            // process.setEMailAddress(request.getParameter ("eMailAddress"));
            // process.setIdJournalRetour(viewBean.getIdJournalRetour());
            // process.setFromNumAffilie(request.getParameter("fromNumAffilie"));
            // process.setTillNumAffilie(request.getParameter("tillNumAffilie"));
            // process.setForStatus(request.getParameter ("forStatus"));
            // process.setForIdPlausibilite(request.getParameter("forIdPlausibilite"));
            // /*if("on".equals(request.getParameter ("impressionListe"))){
            // process.setImprimessionListe(Boolean.TRUE);
            // } else {
            // process.setImprimessionListe(Boolean.FALSE);
            // }*/
            // process.setSession((BSession)dispatcher.getSession());
            // process.setOrderBy(request.getParameter ("orderBy"));
            // process.setImpression(request.getParameter("impression"));
            // process.start();
            // if (process.getMsgType().equals(BProcess.ERROR)) {
            // viewBean.setMsgType(process.getMsgType());
            // viewBean.setMessage(process.getMessage());
            // }
            _destination = "/phenix?userAction=phenix.communications.journalRetour.imprimer";
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
     * Réceptionne le fichier selon trois différent mode: - réceptionner et créer un journal - réceptionner dans un
     * journal existant - re-réceptionner dans un journal existant
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     *            HACK : On set directement les variables dans le viewBean (création de nouvelles variables de classes
     *            dans le viewBean) pour éviter de les setter directement dans le process Appel du process dans le
     *            Helper de la classe Modification du forward en goSendRedirect car la redirection se fait sur une
     *            action
     */
    private void _actionExecuterReceptionner(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {
            CPJournalRetourViewBean viewBean = (CPJournalRetourViewBean) session.getAttribute("viewBean");
            // Retour de parametres
            String eMail = request.getParameter("eMailAddress");
            String typeReception = request.getParameter("typeReception");
            String idJournalRetour = request.getParameter("idJournalRetour");
            String csCanton = request.getParameter("csCanton");
            String receptionFileName = request.getParameter("fileName");
            String libelleJournal = request.getParameter("libelleJournal");

            viewBean.setEMailAddress(eMail);
            viewBean.setTypeReception(typeReception);
            viewBean.setIdJournalRetour(idJournalRetour);
            viewBean.setCanton(csCanton);
            viewBean.setReceptionFileName(receptionFileName);
            viewBean.setLibelleJournal(libelleJournal);
            viewBean.setSession((BSession) mainDispatcher.getSession());

            mainDispatcher.dispatch(viewBean, getAction());
            // Instanciation du process
            // CPProcessReceptionCommunication process = new
            // CPProcessReceptionCommunication();
            // process.setSession((BSession)mainDispatcher.getSession());
            // process.setEMailAddress(eMail);
            // process.setTypeReception(typeReception);
            // //Type de Reception = receptionner encore
            // if
            // (CPProcessReceptionCommunication.RECEPTIONNER_ENCORE.equals(typeReception))
            // {
            // String idJournalRetour = request.getParameter("idJournalRetour");
            // process.setIdJournalRetour(idJournalRetour);
            // } else {
            // // Attribue les parametres pour la lecture du fichier
            //
            // String csCanton = request.getParameter("csCanton");
            // //------------------------------------------------------------
            // String receptionFileName = request.getParameter("fileName");
            // // -------
            // process.setInputFileName(receptionFileName);
            // process.setCsCanton(csCanton);
            // if (typeReception ==
            // CPProcessReceptionCommunication.RECEPTION_TOTAL){
            // String idJournalRetour = request.getParameter("idJournalRetour");
            // process.setIdJournalRetour(idJournalRetour);
            // } else { // type de reception = receptionner créer journal
            // String libelleJournal = request.getParameter("libelleJournal");
            // process.setLibelleJournal(libelleJournal);
            // }
            // }
            // process.start();
            // if (process.getMsgType().equals(BProcess.ERROR)) {
            // viewBean.setMsgType(process.getMsgType());
            // viewBean.setMessage(process.getMessage());
            // }
            _destination = "/phenix?userAction=phenix.communications.journalRetour.receptionner";
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
        // servlet.getServletContext().getRequestDispatcher
        // (_destination).forward (request, response);
        goSendRedirect(_destination, request, response);
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     *            HACK: utilisation de goSendRedirect car redirection sur une action On sette les variables dans le
     *            viewBean de manière a ne pas devoir les setter dans le process directement L'execution du process se
     *            fait désormais dans le Helper de la classe
     */
    private void _actionExecuterReinitialiser(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {
            CPJournalRetourViewBean viewBean = (CPJournalRetourViewBean) session.getAttribute("viewBean");
            viewBean.setEMailAddress(request.getParameter("eMailAddress"));
            viewBean.setFromNumAffilie(request.getParameter("fromNumAffilie"));
            viewBean.setTillNumAffilie(request.getParameter("tillNumAffilie"));
            viewBean.setStatus(request.getParameter("forStatus"));
            viewBean.setForIdPlausibilite(request.getParameter("forIdPlausibilite"));
            dispatcher.dispatch(viewBean, getAction());
            // Crée le process qui supprime dans la BD
            // CPProcessCommunicationRetourReinitialiser process = new
            // CPProcessCommunicationRetourReinitialiser();
            // process.setIdJournalRetour(viewBean.getIdJournalRetour());
            // process.setEMailAddress(request.getParameter ("eMailAddress"));
            // process.setFromNumAffilie(request.getParameter("fromNumAffilie"));
            // process.setTillNumAffilie(request.getParameter("tillNumAffilie"));
            // process.setForStatus(request.getParameter ("forStatus"));
            // process.setForIdPlausibilite(request.getParameter("forIdPlausibilite"));
            // process.setSession((BSession)dispatcher.getSession());
            // process.start();
            // if (process.getMsgType().equals(BProcess.ERROR)) {
            // viewBean.setMsgType(process.getMsgType());
            // viewBean.setMessage(process.getMessage());
            // }
            _destination = "/phenix?userAction=phenix.communications.journalRetour.reinitialiser";
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
        System.out.println(request.getContextPath() + _destination);
        // response.sendRedirect(request.getContextPath() + _destination);
        goSendRedirect(_destination, request, response);

    }

    /**
     * Permet l'exécution du processus de réinitialisation en masse
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    private void _actionExecuterReinitialiserEnMasse(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {
            CPJournalRetourViewBean viewBean = (CPJournalRetourViewBean) session.getAttribute("viewBean");
            viewBean.setEMailAddress(request.getParameter("eMailAddress"));
            viewBean.setFromNumAffilie(request.getParameter("fromNumAffilie"));
            viewBean.setTillNumAffilie(request.getParameter("tillNumAffilie"));
            viewBean.setStatus(request.getParameter("forStatus"));
            viewBean.setForIdPlausibilite(request.getParameter("forIdPlausibilite"));
            dispatcher.dispatch(viewBean, getAction());

            _destination = "/phenix?userAction=phenix.communications.journalRetour.reinitialiserEnMasse";
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = _destination + "&_valid=fail";
            } else {
                _destination = _destination + "&process=launched";
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirect(_destination, request, response);

    }

    /**
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     *            HACK : On set directement les variables dans le viewBean (création de nouvelles variables de classes
     *            dans le viewBean) pour éviter de les setter directement dans le process Appel du process dans le
     *            Helper de la classe Modification du forward en goSendRedirect car la redirection se fait sur une
     *            action
     */
    private void _actionExecuterRetourner(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {
            CPJournalRetourViewBean viewBean = (CPJournalRetourViewBean) session.getAttribute("viewBean");
            viewBean.setEMailAddress(request.getParameter("eMailAddress"));
            viewBean.setFromNumAffilie(request.getParameter("fromNumAffilie"));
            viewBean.setTillNumAffilie(request.getParameter("tillNumAffilie"));
            viewBean.setStatus(request.getParameter("forStatus"));
            viewBean.setForIdPlausibilite(request.getParameter("forIdPlausibilite"));
            viewBean.setDateFichier(request.getParameter("dateFichier"));
            viewBean.setSimulation(request.getParameter("simulation"));
            viewBean.setListeExcel(request.getParameter("listeExcel"));
            viewBean.setSession((BSession) dispatcher.getSession());
            dispatcher.dispatch(viewBean, getAction());
            // Crée le process qui inserere le fichier dans la BD
            // CPProcessCommunicationRetourner process = new
            // CPProcessCommunicationRetourner();
            // process.setEMailAddress(request.getParameter ("eMailAddress"));
            // process.setIdJournalRetour(viewBean.getIdJournalRetour());
            // process.setFromNumAffilie(request.getParameter("fromNumAffilie"));
            // process.setTillNumAffilie(request.getParameter("tillNumAffilie"));
            // process.setForStatus(request.getParameter("forStatus"));
            // process.setForIdPlausibilite(request.getParameter("forIdPlausibilite"));
            // process.setDateFichier(request.getParameter("dateFichier"));
            // if("on".equals(request.getParameter ("simulation"))){
            // process.setSimulation(Boolean.TRUE);
            // } else {
            // process.setSimulation(Boolean.FALSE);
            // }
            // if("on".equals(request.getParameter ("listeExcel"))){
            // process.setListeExcel(Boolean.TRUE);
            // } else {
            // process.setListeExcel(Boolean.FALSE);
            // }
            // process.setSession((BSession)dispatcher.getSession());
            // process.start();
            // if (process.getMsgType().equals(BProcess.ERROR)) {
            // viewBean.setMsgType(process.getMsgType());
            // viewBean.setMessage(process.getMessage());
            // }
            _destination = "/phenix?userAction=phenix.communications.journalRetour.retourner";
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
        // servlet.getServletContext().getRequestDispatcher
        // (_destination).forward (request, response);
        goSendRedirect(_destination, request, response);

    }

    /**
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     *            HACK : Variables setter dans le viewBean à la place du process l'execution du process se fait
     *            désormais dans le Helper de la classe goSendRedirect pour la redirection vers une action
     */
    private void _actionExecuterSupprimer(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {
            CPJournalRetourViewBean viewBean = (CPJournalRetourViewBean) session.getAttribute("viewBean");
            viewBean.setEMailAddress(request.getParameter("eMailAddress"));
            // viewBean.setIdJournalRetour(string);
            viewBean.setFromNumAffilie(request.getParameter("fromNumAffilie"));
            viewBean.setTillNumAffilie(request.getParameter("tillNumAffilie"));
            viewBean.setStatus(request.getParameter("forStatus"));
            viewBean.setSession((BSession) dispatcher.getSession());
            viewBean.setForIdPlausibilite(request.getParameter("forIdPlausibilite"));
            dispatcher.dispatch(viewBean, getAction());
            // Crée le process qui supprime dans la BD
            // CPProcessCommunicationRetourSupprimer process = new
            // CPProcessCommunicationRetourSupprimer();
            // process.setEMailAddress(request.getParameter ("eMailAddress"));
            // process.setIdJournalRetour(viewBean.getIdJournalRetour());
            // process.setFromNumAffilie(request.getParameter("fromNumAffilie"));
            // process.setTillNumAffilie(request.getParameter("tillNumAffilie"));
            // process.setForStatus(request.getParameter ("forStatus"));
            // process.setSession((BSession)dispatcher.getSession());
            // process.setForIdPlausibilite(request.getParameter("forIdPlausibilite"));
            // process.start();
            // if (process.getMsgType().equals(BProcess.ERROR)) {
            // viewBean.setMsgType(process.getMsgType());
            // viewBean.setMessage(process.getMessage());
            // }
            _destination = "/phenix?userAction=phenix.communications.journalRetour.supprimerCommunication";
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
        // System.out.println(request.getContextPath() + _destination);
        // response.sendRedirect(request.getContextPath() + _destination);
        goSendRedirect(_destination, request, response);

    }

    /**
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     *            HACK : On conserve le forward car redirection sur page jsp retrieve désormais dans le Helper
     */
    private void _actionFermerJournal(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";

        try {
            String idJournalRetour = request.getParameter("idJournalRetour");
            CPJournalRetourViewBean journal = new CPJournalRetourViewBean();
            journal.setISession(dispatcher.getSession());
            journal.setIdJournalRetour(idJournalRetour);
            dispatcher.dispatch(journal, getAction());
            // journal.retrieve();
            session.setAttribute("viewBean", journal);
            _destination = "/phenix?userAction=phenix.communications.journalRetour.chercher";
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
     *            HACK : On conserve le forward car la redirection se fait sur une page JSP Le retrieve et l'execution
     *            de la tache rechercheProchainJournalFacturation sont executés dans le Helper de la classe
     */
    private void _actionGenerer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";

        try {
            String idJournalRetour = request.getParameter("idJournalRetour");
            CPJournalRetourViewBean journal = new CPJournalRetourViewBean();
            journal.setISession(dispatcher.getSession());
            journal.setIdJournalRetour(idJournalRetour);
            dispatcher.dispatch(journal, getAction());

            // journal.retrieve();
            // if("OK".equals(journal.getValidationDecision())){
            // journal.rechercheProchainJournalFacturation();
            // }
            session.setAttribute("viewBean", journal);
            _destination = getRelativeURLwithoutClassPart(request, session) + "processReceptionGenererDecisions_de.jsp";
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        /*
         * affiche la prochaine page
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Permet d'afficher l'ecran de lancement du processus de génération en masse
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    private void _actionGenererEnMasse(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";

        try {
            String[] listIdJournalRetour = request.getParameterValues("listIdJournalRetour");

            CPJournalRetourViewBean viewBean = new CPJournalRetourViewBean();
            viewBean.setISession(dispatcher.getSession());
            viewBean.setListIdJournalRetour(listIdJournalRetour);

            dispatcher.dispatch(viewBean, getAction());
            session.setAttribute("viewBean", viewBean);
            _destination = getRelativeURLwithoutClassPart(request, session)
                    + "processCommunicationGenererEnMasse_de.jsp";

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
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
            String idJournalRetour = request.getParameter("idJournalRetour");
            CPJournalRetourViewBean journal = new CPJournalRetourViewBean();
            journal.setISession(dispatcher.getSession());
            journal.setIdJournalRetour(idJournalRetour);
            // journal.retrieve();
            dispatcher.dispatch(journal, getAction());

            session.setAttribute("viewBean", journal);
            _destination = getRelativeURLwithoutClassPart(request, session) + "communicationRetourImprimer_de.jsp";
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        /*
         * affiche la prochaine page
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Receptionne un journal: cette action peut être executée - si le journal n'a pas encore crée (un journal sera
     * crée) - si le journal a déjà été crée mais se trouve dans l'état RECEPCTION_PARTIEL
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     *             HACK : Dans ce cas précis nous faisons appel au dispatcher uniquement lorsqu'un retrieve est
     *             nécessaire autrement l'action ne nécessite pas de passer par les droits Forward conservé car
     *             redirection sur JSP
     */
    protected void _actionReceptionner(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";

        try {

            String idJournalRetour = request.getParameter("idJournalRetour");
            CPJournalRetourViewBean journal = new CPJournalRetourViewBean();
            journal.setISession(mainDispatcher.getSession());

            if (JadeStringUtil.isEmpty(idJournalRetour)) {
                // Action appelée depuis le menu principale:
                // le journal n'a pas été créé, on réceptionne le fichier en
                // créant un journal
                session.setAttribute("viewBean", journal);
                _destination = getRelativeURLwithoutClassPart(request, session)
                        + "processReceptionCommunication_de.jsp";
            } else {
                // Action appelée depuis le menu option d'un journal existant
                // On recherche le journal
                journal.setIdJournalRetour(idJournalRetour);
                mainDispatcher.dispatch(journal, getAction());
                // journal.retrieve();
                session.setAttribute("viewBean", journal);
                String status = journal.getStatus();
                if (status.equals(CPJournalRetour.CS_RECEPTION_PARTIEL)) {
                    _destination = getRelativeURLwithoutClassPart(request, session)
                            + "processReceptionCommunication_de.jsp";
                }
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        /*
         * affiche la prochaine page
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     *            HACK: On conserve le forward car la redirection se fait sur une page JSP le retrieve est maintenant
     *            executé dans le Helper de la classe pour passer par les droits
     */
    private void _actionReinitialiser(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";

        try {
            String[] listIdJournalRetour = request.getParameterValues("idJournalRetour");
            CPJournalRetourViewBean journal = new CPJournalRetourViewBean();
            journal.setISession(dispatcher.getSession());
            journal.setListIdJournalRetour(listIdJournalRetour);

            dispatcher.dispatch(journal, getAction());
            // journal.retrieve();
            session.setAttribute("viewBean", journal);
            _destination = getRelativeURLwithoutClassPart(request, session)
                    + "processCommunicationReinitialiser_de.jsp";
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        /*
         * affiche la prochaine page
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Permet d'afficher l'ecran de lancement du processus de réinitialisation en masse
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    private void _actionReinitialiserEnMasse(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";

        try {
            String[] listIdJournalRetour = request.getParameterValues("listIdJournalRetour");

            CPJournalRetourViewBean viewBean = new CPJournalRetourViewBean();
            viewBean.setISession(dispatcher.getSession());
            viewBean.setListIdJournalRetour(listIdJournalRetour);

            dispatcher.dispatch(viewBean, getAction());
            session.setAttribute("viewBean", viewBean);
            _destination = getRelativeURLwithoutClassPart(request, session)
                    + "processCommunicationReinitialiserEnMasse_de.jsp";

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     *            HACK : on conserve le forward car redirection sur jsp retrieve executé dans le helper
     */
    private void _actionRetourner(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";

        try {
            String idJournalRetour = request.getParameter("idJournalRetour");
            CPJournalRetourViewBean journal = new CPJournalRetourViewBean();
            journal.setISession(dispatcher.getSession());
            journal.setIdJournalRetour(idJournalRetour);
            dispatcher.dispatch(journal, getAction());
            // journal.retrieve();
            session.setAttribute("viewBean", journal);
            _destination = getRelativeURLwithoutClassPart(request, session) + "processCommunicationRetourner_de.jsp";
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        /*
         * affiche la prochaine page
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     *            HACK : On conserve le forward car redirection sur page jsp retrieve désormais dans le Helper
     */
    private void _actionSupprimer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";

        try {
            String idJournalRetour = request.getParameter("idJournalRetour");
            CPJournalRetourViewBean journal = new CPJournalRetourViewBean();
            journal.setISession(dispatcher.getSession());
            journal.setIdJournalRetour(idJournalRetour);
            dispatcher.dispatch(journal, getAction());
            // journal.retrieve();
            session.setAttribute("viewBean", journal);
            _destination = getRelativeURLwithoutClassPart(request, session)
                    + "processReceptionCommunicationSupprimer_de.jsp";
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        /*
         * affiche la prochaine page
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if ("receptionner".equals(getAction().getActionPart())) {
            _actionReceptionner(session, request, response, dispatcher);
        } else if ("executerReceptionner".equals(getAction().getActionPart())) {
            _actionExecuterReceptionner(session, request, response, dispatcher);
        } else if ("generer".equals(getAction().getActionPart())) {
            _actionGenerer(session, request, response, dispatcher);
        } else if ("executerGenerer".equals(getAction().getActionPart())) {
            _actionExecuterGenerer(session, request, response, dispatcher);
        } else if ("supprimerCommunication".equals(getAction().getActionPart())) {
            _actionSupprimer(session, request, response, dispatcher);
        } else if ("executerSupprimer".equals(getAction().getActionPart())) {
            _actionExecuterSupprimer(session, request, response, dispatcher);
        } else if ("imprimer".equals(getAction().getActionPart())) {
            _actionImprimer(session, request, response, dispatcher);
        } else if ("executerImprimer".equals(getAction().getActionPart())) {
            _actionExecuterImprimer(session, request, response, dispatcher);
        } else if ("abandonner".equals(getAction().getActionPart())) {
            _actionAbandonner(session, request, response, dispatcher);
        } else if ("abandonnerEnMasse".equals(getAction().getActionPart())) {
            _actionAbandonnerEnMasse(session, request, response, dispatcher);
        } else if ("executerAbandonner".equals(getAction().getActionPart())) {
            _actionExecuterAbandonner(session, request, response, dispatcher);
        } else if ("executerAbandonnerEnMasse".equals(getAction().getActionPart())) {
            _actionExecuterAbandonnerEnMasse(session, request, response, dispatcher);
        } else if ("retourner".equals(getAction().getActionPart())) {
            _actionRetourner(session, request, response, dispatcher);
        } else if ("executerRetourner".equals(getAction().getActionPart())) {
            _actionExecuterRetourner(session, request, response, dispatcher);
        } else if ("reinitialiser".equals(getAction().getActionPart())) {
            _actionReinitialiser(session, request, response, dispatcher);
        } else if ("reinitialiserEnMasse".equals(getAction().getActionPart())) {
            _actionReinitialiserEnMasse(session, request, response, dispatcher);
        } else if ("executerReinitialiser".equals(getAction().getActionPart())) {
            _actionExecuterReinitialiser(session, request, response, dispatcher);
        } else if ("executerReinitialiserEnMasse".equals(getAction().getActionPart())) {
            _actionExecuterReinitialiserEnMasse(session, request, response, dispatcher);
        } else if ("genererEnMasse".equals(getAction().getActionPart())) {
            _actionGenererEnMasse(session, request, response, dispatcher);
        } else if ("executerGenererEnMasse".equals(getAction().getActionPart())) {
            _actionExecuterGenererEnMasse(session, request, response, dispatcher);
        } else if ("fermer".equals(getAction().getActionPart())) {
            _actionFermerJournal(session, request, response, dispatcher);
        } else {
            // on a demandé un page qui n'existe pas
            servlet.getServletContext().getRequestDispatcher(FWDefaultServletAction.UNDER_CONSTRUCTION_PAGE)
                    .forward(request, response);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAjouter(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAjouter(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        CPJournalRetourViewBean journal = (CPJournalRetourViewBean) viewBean;
        journal.setSucces(new Boolean(true));
        journal.setStatus(CPJournalRetour.CS_RECEPTION_PARTIEL);
        journal.setNbCommunication(journal.getNbCommunication());
        return journal;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeModifier(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        CPJournalRetourViewBean journal = (CPJournalRetourViewBean) viewBean;
        String success = request.getParameter("succes");
        if (success != null) {
            journal.setSucces(new Boolean(success));
        }
        return journal;
    }

}
