/*
 * Créé le 20 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.db.envoi.LEChampUtilisateurListViewBean;
import globaz.leo.db.envoi.LEChampUtilisateurViewBean;
import globaz.leo.db.envoi.LEEditerFormuleViewBean;
import globaz.leo.db.envoi.LEEnvoiViewBean;
import globaz.leo.process.handler.LEEditerFormuleHandler;
import globaz.leo.process.handler.LEEnvoiHandler;
import globaz.leo.process.handler.LEGenererEtapeHandler;
import globaz.leo.process.handler.LEJournalHandler;
import globaz.lupus.db.journalisation.LUGroupeJournalViewBean;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import globaz.lupus.db.journalisation.LUJournalViewBean;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LEEnvoiServletAction extends FWDefaultServletAction {
    public static final String ACTION_AFFICHER_RECU = "afficherRecu";
    public static final String ACTION_ANNULE_ETAPE = "annulerEtape";
    public static final String ACTION_EDITION = "editerFormule";
    public static final String ACTION_EDITION_OK = "editerFormuleOk";
    public static final String ACTION_ETAPE_SUIVANTE = "etapeSuivante";
    public static final String ACTION_LISTER = "lister";
    public static final String ACTION_RECEPTION = "reception";
    public static final String ACTION_SELECTIONNER = "selectionner";
    public static final String ACTION_UPDATE_DATE = "updateDate";

    public LEEnvoiServletAction(FWServlet servlet) {
        super(servlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    protected void actionAfficherRecu(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        if ((request.getParameter("action") != null) && (request.getParameter("action").equals("reçu"))) {
            String journalId = request.getParameter("selectedId");
            LEJournalHandler complement = new LEJournalHandler();
            LEEnvoiHandler envoiHandler = new LEEnvoiHandler();
            BSession sessionUt = (BSession) ((globaz.framework.controller.FWController) session
                    .getAttribute("objController")).getSession();
            try {
                // on a déjà une réception??
                String idDernierJournal = envoiHandler.getDernierEnvoi(journalId, sessionUt, null);
                if (complement.getCsDocument(idDernierJournal, sessionUt, null).equals(
                        ILEConstantes.CS_DEF_FORMULE_RECEPTION)) {

                    // getAction().changeActionPart(FWAction.ACTION_AFFICHER);
                    String _destination = "/" + getAction().getApplicationPart() + "?userAction="
                            + getAction().toString() + "&action=error";

                    // On redirige sur envoi_de avec un message d'erreur
                    try {
                        /*
                         * pour compatibilité : si on a le parametre _method=add, c'est que l'on a une action nouveau
                         */
                        String method = "upd";
                        if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                            getAction().changeActionPart(FWAction.ACTION_NOUVEAU);
                        }
                        String selectedId = request.getParameter("selectedId");
                        /*
                         * Creation dynamique de notre viewBean
                         */
                        FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(getAction(),
                                mainDispatcher.getPrefix());
                        /*
                         * pour pouvoir faire un setId remarque : si il y a d'autre set a faire (si plusieurs id par ex)
                         * il faut le faire dans le beforeAfficher(...)
                         */
                        Class b = Class.forName("globaz.globall.db.BIPersistentObject");
                        Method mSetId = b.getDeclaredMethod("setId", new Class[] { String.class });
                        mSetId.invoke(viewBean, new Object[] { selectedId });
                        /*
                         * initialisation du viewBean
                         */
                        if (getAction().getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                            viewBean = beforeNouveau(session, request, response, viewBean);
                        }
                        /*
                         * appelle beforeAfficher, puis le Dispatcher, puis met le bean en session
                         */
                        viewBean = beforeAfficher(session, request, response, viewBean);

                        // passage dans le dispatcher
                        // on change l'action pour que le helper puisse utiliser
                        // l'action afficher dans le invoke et faire le retrieve
                        // correctement.
                        getAction().changeActionPart("afficher");
                        viewBean = mainDispatcher.dispatch(viewBean, getAction());
                        viewBean.setMsgType(FWViewBeanInterface.ERROR);
                        viewBean.setMessage("Il existe déjà une réception pour ce journal\nVous pouvez néanmoins en changer la date !");
                        session.removeAttribute("viewBean");
                        session.setAttribute("viewBean", viewBean);
                        _destination = getRelativeURL(request, session) + "_de.jsp?_method=" + method;
                    } catch (Exception e) {
                        JadeLogger.error(this, e);
                        _destination = FWDefaultServletAction.ERROR_PAGE;
                    }

                    /*
                     * redirection vers la destination
                     */
                    servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
                } else {
                    super.actionAfficher(session, request, response, mainDispatcher);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            super.actionAfficher(session, request, response, mainDispatcher);
        }
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String action = request.getParameter("userAction");
        FWAction _action = FWAction.newInstance(action);
        BSession sessionUt = (BSession) ((globaz.framework.controller.FWController) session
                .getAttribute("objController")).getSession();
        String journalId = request.getParameter("journalId");

        if (LEEnvoiServletAction.ACTION_AFFICHER_RECU.equals(_action.getActionPart())) {
            actionAfficherRecu(session, request, response, dispatcher);
        }

        // on ne passe dans le dispatcher dans cette action, néanmoins l'option
        // (menu pop up dans le rc_list) n'apparait que si l'utilisateur à les
        // droits update
        else if (LEEnvoiServletAction.ACTION_ETAPE_SUIVANTE.equals(_action.getActionPart())) {
            String _destination = null;

            try {

                // on regarde si l'étape suivante est éditable
                LEJournalHandler complement = new LEJournalHandler();
                LEEnvoiHandler envoiHandler = new LEEnvoiHandler();
                String idDernier = envoiHandler.getDernierEnvoi(journalId, sessionUt, null);

                if (JadeStringUtil.isEmpty(complement.getComplementJournal(idDernier,
                        ILEConstantes.CS_ETAPE_SUIVANTE_GROUPE, sessionUt, null))) {
                    _action.changeActionPart(FWAction.ACTION_CHERCHER);
                    _destination = "/" + getAction().getApplicationPart() + "?userAction=" + _action.toString();
                } else {

                    if (complement.getComplementJournal(idDernier, ILEConstantes.CS_EDITION_MANUELLE_GROUPE, sessionUt,
                            null) != null) {
                        if (complement.getComplementJournal(idDernier, ILEConstantes.CS_EDITION_MANUELLE_GROUPE,
                                sessionUt, null).equals(ILEConstantes.CS_NON)) {
                            LEGenererEtapeHandler etapeSuivante = new LEGenererEtapeHandler();
                            etapeSuivante.genererEtapeSuivante(journalId, sessionUt);
                            _action.changeActionPart(FWAction.ACTION_CHERCHER);
                            _destination = "/" + getAction().getApplicationPart() + "?userAction=" + _action.toString();
                        } else {
                            _destination = "/" + getAction().getApplicationPart() + "?userAction="
                                    + _action.getApplicationPart() + "." + _action.getClassPart()
                                    + ".editerFormule.afficher&selectedId=" + idDernier + "&_method=add";
                        }
                    } else {
                        // on a déjà une réception
                        /*
                         * LEEnvoiViewBean viewBean = (LEEnvoiViewBean)session.getAttribute("viewBean");
                         * viewBean.setMsgType(FWViewBeanInterface.ERROR); viewBean.setMessage(
                         * "On a déjà une réception pour ce journal, veuillez d'abord la supprimer ! idJournal : "
                         * +journalId); session.setAttribute("viewBean",viewBean);
                         */
                        JadeLogger.error(this,
                                "On a déjà une réception pour ce journal, veuillez d'abord la supprimer ! idJournal : "
                                        + journalId);
                        JadeLogger.error(this, "ou l'etape n'a pas d'étape suivante");
                        _action.changeActionPart(FWAction.ACTION_CHERCHER);
                        _destination = "/" + getAction().getApplicationPart() + "?userAction=" + _action.toString();
                    }
                }

            } catch (Exception e) {

                try {
                    JadeSmtpClient.getInstance().sendMail(
                            sessionUt.getUserEMail(),
                            sessionUt.getLabel("MAIL_SUBJECT_ERROR_GENERATION_ETAPE_SUIVANTE_UNITAIRE") + " "
                                    + journalId, e.toString(), null);
                } catch (Exception e2) {
                    JadeLogger.error(this, "Error during sending mail (actionCustom etape suivante) " + e2.toString());
                }

                JadeLogger.error(this, "erreur dans le génération de l etape suivante pour l'id : " + journalId + " / "
                        + e.getMessage());
                try {
                    getAction().changeActionPart(FWAction.ACTION_CHERCHER);
                    _destination = "/" + getAction().getApplicationPart() + "?userAction=" + getAction().toString();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

            // on ne passe dans le dispatcher dans cette action, néanmoins
            // l'option (menu pop up dans le rc_list) n'apparait que si
            // l'utilisateur à les droits update
        } else if (LEEnvoiServletAction.ACTION_ANNULE_ETAPE.equals(_action.getActionPart())) {
            LEJournalHandler jHandler = new LEJournalHandler();
            String _destination;
            try {
                // on regarde si l'étape suivante est éditable
                jHandler.annulerEtape(request.getParameter("journalId"), sessionUt, null);
                _action.changeActionPart(FWAction.ACTION_CHERCHER);
                _destination = "/" + getAction().getApplicationPart() + "?userAction=" + _action.toString();
            } catch (Exception e) {

                _destination = FWDefaultServletAction.ERROR_PAGE;
            }
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        } else if (LEEnvoiServletAction.ACTION_RECEPTION.equals(_action.getActionPart())) {
            LEJournalHandler jHandler = new LEJournalHandler();
            String _destination = "";
            // String journalId="";
            try {
                String method = request.getParameter("_method");
                System.out.println("valeur de method = " + method);
                // String dateRappel="";
                String dateReception = "";
                // if(request.getParameter("journalId")!=null){
                // journalId = request.getParameter("journalId");
                // }//if(request.getParameter("dateRappel").length()>0){
                // dateRappel = request.getParameter("dateRappel");
                // }
                System.out.print(journalId);
                if (!method.equals("upd")) {
                    if (request.getParameter("dateReception").length() > 0) {
                        dateReception = request.getParameter("dateReception");
                    }
                    LUGroupeJournalViewBean groupe = new LUGroupeJournalViewBean();
                    groupe.setSession(sessionUt);
                    // passage dans le dispatcher
                    groupe = (LUGroupeJournalViewBean) dispatcher.dispatch(groupe, _action);
                    if (groupe.checkDate(dateReception) && !groupe.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                        jHandler.receptionDocument(request.getParameter("journalId"), sessionUt, request);
                        _action.changeActionPart(FWAction.ACTION_CHERCHER);
                        _destination = "/" + getAction().getApplicationPart() + "?userAction=" + _action.toString();
                    } else {
                        if (!groupe.checkDate(dateReception)) {
                            _destination = getRelativeURL(request, session)
                                    + "_de.jsp?_valid=fail&_back=sl&action=reçu";
                            LEEnvoiViewBean viewBean = (LEEnvoiViewBean) session.getAttribute("viewBean");
                            viewBean.setMsgType(FWViewBeanInterface.ERROR);
                            viewBean.setMessage("Erreur : La date de réception : " + dateReception
                                    + " n'est pas valide !");
                            viewBean.setDateReception(dateReception);
                            session.setAttribute("viewBean", viewBean);
                        }
                        if (groupe.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                            _destination = FWDefaultServletAction.ERROR_PAGE;
                            session.setAttribute("viewBean", groupe);
                        }
                    }
                } else {
                    if (request.getParameter("dateReception").length() > 0) {
                        dateReception = request.getParameter("dateReception");
                    }
                    LUGroupeJournalViewBean groupe = new LUGroupeJournalViewBean();
                    groupe.setSession(sessionUt);
                    // passage dans le dispatcher
                    groupe = (LUGroupeJournalViewBean) dispatcher.dispatch(groupe, _action);
                    if (groupe.checkDate(dateReception) && !groupe.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                        LUJournalViewBean journal = new LUJournalViewBean();
                        journal.setSession(sessionUt);
                        journal.setIdJournalisation(journalId);
                        journal.retrieve();
                        jHandler.updateDate(request.getParameter("journalId"), sessionUt, journal.getDateRappel(),
                                dateReception);
                        _action.changeActionPart(FWAction.ACTION_CHERCHER);
                        _destination = "/" + getAction().getApplicationPart() + "?userAction=" + _action.toString();
                    } else {
                        if (!groupe.checkDate(dateReception)) {
                            _destination = getRelativeURL(request, session)
                                    + "_de.jsp?_valid=fail&_back=sl&action=reçu";
                            LEEnvoiViewBean viewBean = (LEEnvoiViewBean) session.getAttribute("viewBean");
                            viewBean.setMsgType(FWViewBeanInterface.ERROR);
                            viewBean.setMessage("Erreur : La date de réception : " + dateReception
                                    + " n'est pas valide !");
                            viewBean.setDateReception(dateReception);
                            session.setAttribute("viewBean", viewBean);
                        }
                        if (groupe.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                            _destination = FWDefaultServletAction.ERROR_PAGE;
                            session.setAttribute("viewBean", groupe);
                        }
                    }
                }

            } catch (Exception e) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            }
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        } else if (LEEnvoiServletAction.ACTION_UPDATE_DATE.equals(_action.getActionPart())) {
            LEJournalHandler jHandler = new LEJournalHandler();
            String dateRappel = "";
            String dateReception = "";
            boolean isDateReceptionValid = true;
            boolean isDateRappelValid = true;
            String _destination;

            if (request.getParameter("dateRappel").length() > 0) {
                dateRappel = request.getParameter("dateRappel");
            }
            if (request.getParameter("dateReception").length() > 0) {
                dateReception = request.getParameter("dateReception");
            }
            try {
                LUGroupeJournalViewBean groupe = new LUGroupeJournalViewBean();
                groupe.setSession(sessionUt);

                // passage dans le dispatcher
                groupe = (LUGroupeJournalViewBean) dispatcher.dispatch(groupe, _action);

                if (!JAUtil.isStringEmpty(dateReception)) {
                    if (!groupe.checkDate(dateReception)) {
                        isDateReceptionValid = false;
                    }
                }
                if (!JAUtil.isStringEmpty(dateRappel)) {
                    if (!groupe.checkDate(dateRappel)) {
                        isDateRappelValid = false;
                    }
                }
                if ((isDateReceptionValid) && (isDateRappelValid)
                        && !groupe.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                    jHandler.updateDate(request.getParameter("journalId"), sessionUt, dateRappel, dateReception);
                    _action.changeActionPart(FWAction.ACTION_CHERCHER);
                    _destination = "/" + getAction().getApplicationPart() + "?userAction=" + _action.toString();
                } else {
                    _destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail&_back=sl";
                    LEEnvoiViewBean viewBean = (LEEnvoiViewBean) session.getAttribute("viewBean");
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    if (!isDateReceptionValid) {
                        viewBean.setMessage("Erreur : La date de réception : " + dateReception + " n'est pas valide !");
                        viewBean.setDateReception(dateReception);
                        session.setAttribute("viewBean", viewBean);
                    }
                    if (!isDateRappelValid) {
                        viewBean.setMessage("Erreur : La date de rappel : " + dateRappel + " n'est pas valide !");
                        viewBean.setDateRappel(dateRappel);
                        session.setAttribute("viewBean", viewBean);
                    }
                    if (groupe.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                        _destination = FWDefaultServletAction.ERROR_PAGE;
                        session.setAttribute("viewBean", groupe);
                    }

                }
            } catch (Exception e) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            }
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        } else if (LEEnvoiServletAction.ACTION_EDITION_OK.equals(_action.getActionPart())) {
            // on sauvegarde les params d'edition et on redirige sur la rcListe;
            String _destination = "";
            LEEditerFormuleViewBean viewBean = (LEEditerFormuleViewBean) session.getAttribute("viewBean");
            try {
                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            } catch (Exception e) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            }

            // passage dans le dispatcher pour la gestion des droits
            viewBean = (LEEditerFormuleViewBean) dispatcher.dispatch(viewBean, _action);
            if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                LEGenererEtapeHandler handler = new LEGenererEtapeHandler();
                String idJournal = request.getParameter("selectedId");
                String idJournalSuivant = null;
                try {
                    idJournalSuivant = handler.genererEtapeSuivante(idJournal, sessionUt,
                            viewBean.getIdChoixDestinataire());
                } catch (Exception e2) {
                    JadeLogger.info(this, e2);
                }
                LEChampUtilisateurViewBean champUt = null;
                java.util.Enumeration e = request.getParameterNames();
                try {
                    while (e.hasMoreElements()) {
                        champUt = new LEChampUtilisateurViewBean();
                        champUt.setSession(sessionUt);
                        String key = (String) e.nextElement();
                        if (key.startsWith(ILEConstantes.VALUE)) {
                            String[] values = request.getParameterValues(key);
                            for (int i = 0; i < values.length; i++) {
                                String value = values[i];
                                LEEditerFormuleHandler parseur = new LEEditerFormuleHandler(key);
                                champUt.setIdJournalisation(idJournalSuivant);
                                champUt.setCsGroupe(parseur.get_typePart());
                                champUt.setCsChamp(parseur.get_valuePart());
                                champUt.setValeur(value);
                                champUt.add();
                            }
                        }
                    }
                } catch (Exception exception) {
                    try {
                        if (JAUtil.parseInt(idJournal, 0) > 0) {
                            // on efface les journaux
                            LEEnvoiViewBean journal = new LEEnvoiViewBean();
                            viewBean.setSession(sessionUt);
                            viewBean.setIdJournalisation(idJournalSuivant);
                            viewBean.retrieve();
                            viewBean.delete();
                            // on efface les champs utilisateurs où sont stockés
                            // les params de la formule
                            LEChampUtilisateurListViewBean manager = new LEChampUtilisateurListViewBean();
                            manager.setSession(sessionUt);
                            manager.setForIdJournalisation(idJournalSuivant);
                            manager.find();
                            for (int i = 0; i < manager.getSize(); i++) {
                                champUt = (LEChampUtilisateurViewBean) manager.get(i);
                                champUt.delete();
                            }

                        }
                    } catch (Exception e1) {
                        JadeLogger.error(this, "Le rollback n'as pas fonctionné pour l'idJournal :" + idJournal
                                + "Il reste peut-être des paramètres en BD pour cet id");
                        JadeLogger.info(this, e1);
                    }
                }
                try {
                    _action.changeActionPart(FWAction.ACTION_CHERCHER);
                    _destination = "/" + getAction().getApplicationPart() + "?userAction=" + _action.toString();
                } catch (Exception except) {
                    _destination = FWDefaultServletAction.ERROR_PAGE;
                }
            } else {
                _destination = FWDefaultServletAction.ERROR_PAGE;
                session.setAttribute("viewBean", viewBean);
            }
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    // protected void actionChercher(HttpSession session, HttpServletRequest
    // request, HttpServletResponse response, FWDispatcher mainDispatcher)
    // throws ServletException, IOException {
    // // passer les paramètres en input dans l'action chercher....
    // String params = request.getParameter("typeProv1");
    // String _destination =
    // getRelativeURL(request,session)+"_rc.jsp"+(JAUtil.isStringEmpty(params)?"":("?"+params));
    //
    // servlet.getServletContext().getRequestDispatcher (_destination).forward
    // (request, response);
    // // super.actionChercher(session, request, response, mainDispatcher);
    // }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
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
        // on sauvegarde les critères de recherche
        LUJournalListViewBean vb = (LUJournalListViewBean) viewBean;
        // de même pour les paramètres de provenance
        globaz.lupus.db.data.LUProvenanceDataSource provenanceCriteres = new globaz.lupus.db.data.LUProvenanceDataSource();
        if ((request.getParameter("typeProv1") != null) && (request.getParameter("valProv1") != null)) {
            provenanceCriteres.addProvenance(request.getParameter("typeProv1"), request.getParameter("valProv1"));
        }
        if ((request.getParameter("typeProv2") != null) && (request.getParameter("valProv2") != null)) {
            provenanceCriteres.addProvenance(request.getParameter("typeProv2"), request.getParameter("valProv2"));
        }
        if ((request.getParameter("typeProv3") != null) && (request.getParameter("valProv3") != null)) {
            provenanceCriteres.addProvenance(request.getParameter("typeProv3"), request.getParameter("valProv3"));
        }
        if ((request.getParameter("typeProv4") != null) && (request.getParameter("valProv4") != null)) {
            provenanceCriteres.addProvenance(request.getParameter("typeProv4"), request.getParameter("valProv4"));
        }

        vb.setProvenance(provenanceCriteres);
        return super.beforeLister(session, request, response, viewBean);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeNouveau(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        try {
            getAction().changeActionPart(FWAction.ACTION_AFFICHER);
        } catch (Exception e) {
            JadeLogger.error(this, e);
        } finally {
            return viewBean;
        }
    }

}
