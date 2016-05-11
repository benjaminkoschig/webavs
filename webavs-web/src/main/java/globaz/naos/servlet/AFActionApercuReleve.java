/*
 * Created on 13-Jan-05
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.http.JSPUtils;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.releve.AFApercuReleve;
import globaz.naos.db.releve.AFApercuReleveLineFacturation;
import globaz.naos.db.releve.AFApercuReleveListViewBean;
import globaz.naos.db.releve.AFApercuReleveViewBean;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFUtil;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.pyxis.summary.TIActionSummary;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour l'entité Relevé.
 * 
 * @author sau
 */
public class AFActionApercuReleve extends AFDefaultActionChercher {

    public final static String ACTION_CALCULER = "calculer";
    public final static String ACTION_PRE_SAISIE = "afficherPreSaisie";
    public final static String ACTION_SET_PERIODE = "setPeriode";

    /**
     * Constructeur d'AFActionReleve.
     * 
     * @param servlet
     */
    public AFActionApercuReleve(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Determine la destination après une ajout d'une nouvelle entité effectuée avec succès.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getActionFullURL() + ".afficher&_valid=&_method=&selectedId="
                + ((AFApercuReleve) viewBean).getIdReleve();
    }

    /**
     * Determine la destination après une modification effectuée avec succès.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getActionFullURL() + ".afficher&_valid=&_method=&selectedId="
                + ((AFApercuReleve) viewBean).getIdReleve();
    }

    /**
     * Effectue les traitements pour récuperer l'entité dans la DB, et en afficher les détails.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = null;
        AFApercuReleveViewBean viewBean = new AFApercuReleveViewBean();
        try {

            if ((request.getParameter("apercuReleve") != null) && request.getParameter("apercuReleve").equals("saisie")) {

                // On vient de la page de Pre-Saisie (Nouvelle entité)
                Object vBean = session.getAttribute("viewBean");

                if ((vBean != null) && (vBean instanceof AFApercuReleveViewBean)) {
                    viewBean = (AFApercuReleveViewBean) vBean;

                    viewBean.retrieveIdPassage();

                    viewBean.getAffilieNumero();
                    viewBean.getDateDebut();
                    viewBean.getDateFin();
                    viewBean.getType();
                    viewBean.getPlanAffiliationId();

                    request.getSession().setAttribute(TIActionSummary.PYXIS_VG_IDTIERS_CTX,
                            viewBean.getAffiliation().getIdTiers());
                    JSPUtils.setBeanProperties(request, viewBean);

                    // Formatage des dates de début et de fin
                    // ! Removed since setter force dd.mm.yyyy format !
                    // if (viewBean.getDateFin().length() == 7) {
                    // viewBean.setDateFin("01." + viewBean.getDateFin());
                    // }
                    // if (viewBean.getDateDebut().length() == 7) {
                    // viewBean.setDateDebut("01." + viewBean.getDateDebut());
                    // }
                    // viewBean.setDateDebut(AFUtil.getDateBeginingOfMonth(viewBean.getDateDebut()));
                    // viewBean.setDateFin(AFUtil.getDateEndOfMonth(viewBean.getDateFin()));

                    String errorMessage = viewBean.validationMandatory();
                    if (!JadeStringUtil.isEmpty(errorMessage)) {
                        viewBean.setMsgType(FWViewBeanInterface.ERROR);
                        viewBean.setMessage(errorMessage);
                    } else {
                        viewBean.setMsgType(FWViewBeanInterface.OK);
                    }

                    if (viewBean.getMsgType().equals(FWViewBeanInterface.OK)) {

                        viewBean.generationCotisationList();

                        if (CodeSystem.TYPE_RELEVE_SALAIRE_DIFFERES.equals(viewBean.getType())) {
                            viewBean.setInterets(CAInteretMoratoire.CS_EXEMPTE);
                        }
                        /* } */
                    }
                }

                // affiche un message d'erreur si on souhaite créer un
                // bouclement d'accompte et
                // qu'un bouclement,une taxation d'office, un concordance ou un
                // décompte final existe déjà pour cette période
                viewBean.checkPlausiBouclement();

                // On regarde si il existe des salaires différés
                viewBean.checkWarningSalaireDifferes();
            } else {
                // On vient de la Liste des Relevés (Entité déjà existante) ou
                // d'un détail d'un Relevé
                viewBean.setIdReleve(request.getParameter("selectedId"));
                viewBean = (AFApercuReleveViewBean) mainDispatcher.dispatch(viewBean, getAction());

                if (viewBean.getEtat().equals(CodeSystem.ETATS_RELEVE_SAISIE)) {
                    viewBean.retrieveIdPassage();

                    viewBean.generationCotisationList();
                    viewBean.retrieveReleveMontant();
                } else {
                    // Etat = CodeSystem.ETATS_RELEVE_FACTURER ou
                    // CodeSystem.ETATS_RELEVE_COMPTABILISER
                    // Recupération des valeurs depuis la facturation MUSCA.
                    viewBean.generationCotisationList();
                    viewBean.getCotisationListFromFacturation();
                }
            }

            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = getRelativeURL(request, session) + "Pre_de.jsp?_valid=fail&_back=sl";
            } else {
                request.getSession().setAttribute(TIActionSummary.PYXIS_VG_IDTIERS_CTX,
                        viewBean.getAffiliation().getIdTiers());
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(viewBean.getMessage() + " " + JadeUtil.getStackTrace(e));
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Afficher les détails de pré-saisie d'une nouvelle entité Relevé..
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    protected void actionAfficherPreSaisie(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher) throws javax.servlet.ServletException,
            java.io.IOException {

        String _destination = "";

        AFApercuReleveViewBean viewBean = new AFApercuReleveViewBean();
        try {
            if ((request.getParameter("apercuReleve") != null)
                    && request.getParameter("apercuReleve").equals("details")) {

                // On vient de l'écran de détail.
                viewBean.setSession((BSession) CodeSystem.getSession(session));
                Object vBean = session.getAttribute("viewBean");
                if ((vBean != null) && (vBean instanceof AFApercuReleveViewBean)) {
                    viewBean = (AFApercuReleveViewBean) session.getAttribute("viewBean");
                    JSPUtils.setBeanProperties(request, viewBean);
                    prepareCalculeCotisation(viewBean, request);
                }
            } else {
                viewBean.setSession((BSession) CodeSystem.getSession(session));
                viewBean.setEtat(CodeSystem.ETATS_RELEVE_SAISIE);
                // viewBean.setType(CodeSystem.TYPE_RELEVE_PERIODIQUE);
                viewBean.setInterets(CodeSystem.INTERETS_RELEVE_AUTO);
                viewBean.setCollaborateur(viewBean.getSession().getUserId());
            }
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }
        // PO 9395
        // ! Removed since setter force dd.mm.yyyy format !
        // if (viewBean.getDateDebut().length() > 7) {
        // viewBean.setDateDebut(viewBean.getDateDebut().substring(3));
        // }
        // if (viewBean.getDateFin().length() > 7) {
        // viewBean.setDateFin(viewBean.getDateFin().substring(3));
        // }
        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            _destination = getRelativeURL(request, session) + "Pre_de.jsp?_valid=fail";
        } else {
            _destination = getRelativeURL(request, session) + "Pre_de.jsp";
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Effectue la mise à jour les calcules des cotisations et re-affiche l'ècran de détails.
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    protected void actionCalculer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";
        AFApercuReleveViewBean viewBean = (AFApercuReleveViewBean) session.getAttribute("viewBean");
        String method = request.getParameter("_method");
        try {
            JSPUtils.setBeanProperties(request, viewBean);
            prepareCalculeCotisation(viewBean, request);
            getAction().setRight(FWSecureConstants.ADD);
            getAction().setRight(FWSecureConstants.UPDATE);
            getAction().setRight(FWSecureConstants.READ);
            dispatcher.dispatch(viewBean, getAction());
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            _destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail";
        } else {
            _destination = getRelativeURL(request, session) + "_de.jsp?_method=" + method;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Traitement des actions non standard.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        if (AFActionApercuReleve.ACTION_PRE_SAISIE.equals(getAction().getActionPart())) {
            actionAfficherPreSaisie(session, request, response, dispatcher);
        } else if (AFActionApercuReleve.ACTION_CALCULER.equals(getAction().getActionPart())) {
            actionCalculer(session, request, response, dispatcher);
        } else if (AFActionApercuReleve.ACTION_SET_PERIODE.equals(getAction().getActionPart())) {
            actionSetPeriode(session, request, response);
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }
    }

    /**
     * .
     * 
     * @param session
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    protected void actionSetPeriode(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";
        AFApercuReleveViewBean viewBean = (AFApercuReleveViewBean) session.getAttribute("viewBean");
        String oldIdTiers = viewBean.getIdTiers();
        String oldType = viewBean.getType();
        String method = request.getParameter("_method");
        try {
            JSPUtils.setBeanProperties(request, viewBean);

            if ((JadeStringUtil.isEmpty(viewBean.getDateDebut()) && JadeStringUtil.isEmpty(viewBean.getDateFin()))
                    || !viewBean.getIdTiers().equals(oldIdTiers) || !viewBean.getType().equals(oldType)) {

                BSession bSession = (BSession) CodeSystem.getSession(session);

                String today = JACalendar.todayJJsMMsAAAA();
                AFAffiliationManager affiliationManager = new AFAffiliationManager();
                affiliationManager.setSession(bSession);
                affiliationManager.setForAffilieNumero(viewBean.getAffilieNumero());
                affiliationManager.setForTillDateDebut(today);
                affiliationManager.setOrder("MADDEB DESC");
                affiliationManager.setForTypesAffParitaires();
                affiliationManager.find();

                if (affiliationManager.size() > 0) {
                    AFAffiliation affiliation = (AFAffiliation) affiliationManager.getFirstEntity();

                    if (((viewBean.getType().equalsIgnoreCase(CodeSystem.TYPE_RELEVE_DECOMP_FINAL))
                            || (viewBean.getType().equalsIgnoreCase(CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE)) || (viewBean
                                .getType().equalsIgnoreCase(CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA)))) {
                        if (JadeStringUtil.isBlankOrZero(affiliation.getDateFin())) {

                            viewBean.setDateFin(AFUtil.getDateEndOfPreviousYear(today));
                            viewBean.setDateDebut("01.01." + viewBean.getDateFin().substring(6));
                        } else {
                            viewBean.setDateFin(affiliation.getDateFin());
                            viewBean.setDateDebut("01.01." + viewBean.getDateFin().substring(6));
                        }
                    } else {

                        if ((viewBean.getType().equalsIgnoreCase(CodeSystem.TYPE_RELEVE_RECTIF))
                                || (viewBean.getType().equalsIgnoreCase(CodeSystem.TYPE_RELEVE_TAXATION_OFFICE))
                                || (viewBean.getType().equalsIgnoreCase(CodeSystem.TYPE_RELEVE_CONTROL_EMPL))) {

                            viewBean.setDateFin("");
                            viewBean.setDateDebut("");
                        } else {

                            if (JadeStringUtil.isIntegerEmpty(affiliation.getDateFin())
                                    || BSessionUtil.compareDateFirstGreaterOrEqual(bSession, affiliation.getDateFin(),
                                            today)) {

                                if (affiliation.getPeriodicite().equals(CodeSystem.PERIODICITE_MENSUELLE)) {
                                    viewBean.setDateFin(AFUtil.getDateEndOfPreviousMonth(today));
                                    viewBean.setDateDebut(AFUtil.getDateBeginingOfMonth(viewBean.getDateFin()));

                                } else if (affiliation.getPeriodicite().equals(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {
                                    viewBean.setDateFin(AFUtil.getDateEndOfPreviousTrim(today));
                                    viewBean.setDateDebut(AFUtil.getDateBeginingOfMonth(AFUtil
                                            .getDateEndOfPreviousMonth(2, viewBean.getDateFin())));

                                } else {
                                    viewBean.setDateFin(AFUtil.getDateEndOfPreviousYear(today));
                                    viewBean.setDateDebut("01.01." + viewBean.getDateFin().substring(6));
                                }
                            } else {

                                if (affiliation.getPeriodicite().equals(CodeSystem.PERIODICITE_MENSUELLE)) {

                                    // On récupère la date de fin du dernier
                                    // relevé
                                    String dateDernierReleve = getDateFinDernierReleve(session, viewBean);
                                    String dateEndOfNextMonth = AFUtil.getDateEndOfNextMonth(dateDernierReleve);

                                    // Si la récupération a donné null, on
                                    // récupère la date de fin de l'affiliation
                                    if (dateEndOfNextMonth == null) {
                                        viewBean.setDateFin(AFUtil.getDateEndOfMonth(affiliation.getDateFin()));
                                    } else {
                                        viewBean.setDateFin(dateEndOfNextMonth);
                                    }

                                    if (BSessionUtil.compareDateFirstLower(bSession, today, viewBean.getDateFin())) {
                                        viewBean.setDateFin(AFUtil.getDateEndOfPreviousMonth(viewBean.getDateFin()));
                                    }
                                    viewBean.setDateDebut(AFUtil.getDateBeginingOfMonth(viewBean.getDateFin()));

                                } else if (affiliation.getPeriodicite().equals(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {

                                    viewBean.setDateFin(AFUtil.getDateEndOfTrim(affiliation.getDateFin()));

                                    if (BSessionUtil.compareDateFirstLower(bSession, today, viewBean.getDateFin())) {
                                        viewBean.setDateFin(AFUtil.getDateEndOfPreviousTrim(viewBean.getDateFin()));
                                    }
                                    viewBean.setDateDebut(AFUtil.getDateBeginingOfMonth(AFUtil
                                            .getDateEndOfPreviousMonth(2, viewBean.getDateFin())));

                                } else {
                                    viewBean.setDateFin(AFUtil.getDateEndOfYear(affiliation.getDateFin()));

                                    if (BSessionUtil.compareDateFirstLower(bSession, today, viewBean.getDateFin())) {
                                        viewBean.setDateFin(AFUtil.getDateEndOfPreviousYear(viewBean.getDateFin()));
                                    }
                                    viewBean.setDateDebut("01.01." + viewBean.getDateFin().substring(6));
                                }
                            }
                        }
                    }

                }

            }

            viewBean.setDateReception(JACalendar.todayJJsMMsAAAA());

            if ((viewBean.getType().equalsIgnoreCase(CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE))) {
                viewBean.setDateReception(viewBean.giveDateReceptionInfoRom206());
            }
            // PO 9395
            // ! Removed since setter force dd.mm.yyyy format !
            // if (viewBean.getDateDebut().length() > 7) {
            // viewBean.setDateDebut(viewBean.getDateDebut().substring(3));
            // }
            // if (viewBean.getDateFin().length() > 7) {
            // viewBean.setDateFin(viewBean.getDateFin().substring(3));
            // }

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            _destination = getRelativeURL(request, session) + "Pre_de.jsp?_valid=fail";
        } else {
            _destination = getRelativeURL(request, session) + "Pre_de.jsp?_method=" + method;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Effectue des traitements avant l'ajout d'une nouvelle entité dans la DB.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAjouter(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAjouter(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        AFApercuReleveViewBean vBean = (AFApercuReleveViewBean) viewBean;
        prepareCalculeCotisation(vBean, request);
        return vBean;
    }

    /**
     * Effectue des traitements avant la mise à jour d'une entité dans la DB.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeModifier(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        AFApercuReleveViewBean vBean = (AFApercuReleveViewBean) viewBean;
        if ((request.getParameter("doCalculation") != null) && request.getParameter("doCalculation").equals("true")) {
            prepareCalculeCotisation(vBean, request);
        }
        return vBean;
    }

    /**
     * Methode de recherche de la date de fin du dernier relevé<br>
     * Si un relevé est trouvé, la date de fin du relevé est retournée<br>
     * Sinon, NULL est retourné
     * 
     * @param session
     * @param viewBean
     * @return
     * @throws Exception
     */
    private String getDateFinDernierReleve(HttpSession session, AFApercuReleveViewBean viewBean) throws Exception {
        BSession bSession = (BSession) CodeSystem.getSession(session);

        AFApercuReleveListViewBean listReleve = new AFApercuReleveListViewBean();
        listReleve.setSession(bSession);
        listReleve.setForAffilieNumero(viewBean.getAffilieNumero());
        listReleve.setOrdreByDateFin(true);
        listReleve.find();

        if (listReleve.size() > 0) {
            AFApercuReleve releve = (AFApercuReleve) listReleve.getFirstEntity();
            return releve.getDateFin();
        }

        return null;
    }

    /**
     * Calcule et met à jour les montant dans la liste des cotisations du relevé.
     * 
     * @param viewBean
     * @param request
     * @param dispatcher
     */
    private void prepareCalculeCotisation(AFApercuReleveViewBean viewBean, HttpServletRequest request) {

        String theIdCompteAnnexe = "";
        try {
            theIdCompteAnnexe = viewBean.getCompteAnnexe().getIdCompteAnnexe();
        } catch (Exception e) {
            theIdCompteAnnexe = "";
        }

        if (viewBean.getEtat().equals(CodeSystem.ETATS_RELEVE_SAISIE)) {

            try {
                List<AFApercuReleveLineFacturation> cotisationList = viewBean.getCotisationList();
                List<AFApercuReleveLineFacturation> newLineList = new ArrayList<AFApercuReleveLineFacturation>();
                for (int i = 0; i < cotisationList.size(); i++) {
                    AFApercuReleveLineFacturation newLine = new AFApercuReleveLineFacturation();
                    // Recupération des valeurs de l'écran
                    newLine.setAssuranceId(request.getParameter("assuranceId" + i));
                    newLine.setDebutPeriode(request.getParameter("dateDebutMontant" + i));
                    newLine.setMontantCalculer(request.getParameter("montantCalculer" + i), false);
                    if (JadeStringUtil.isEmpty(request.getParameter("masse" + i))) {
                        newLine.setMasseVide(true);
                    } else {
                        newLine.setMasse(request.getParameter("masse" + i));
                        newLine.setMasse(AFUtil.plafonneMasse(newLine.getMasseString(false), viewBean.getType(),
                                newLine.getAssuranceId(), viewBean.getDateDebut(), viewBean.getSession(),
                                theIdCompteAnnexe));
                    }
                    newLineList.add(newLine);
                }

                viewBean.setCotisationList(cotisationList);
                viewBean.setApercuReleveLineFacturationList(newLineList);
                viewBean.setDoCalculCotisation(true);
            } catch (NumberFormatException e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage("Ce n'est pas un nombre valide : '" + e.getMessage() + "'");
            } catch (Exception e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.getMessage());
            }
        }
    }
}
