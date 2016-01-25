package globaz.lacerta.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationViewBean;
import globaz.naos.db.annonceAffilie.AFAnnonceAffilieViewBean;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliationViewBean;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresseManager;
import globaz.pyxis.db.adressecourrier.TILocalite;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TIAlias;
import globaz.pyxis.db.tiers.TIAliasManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.pyxis.services.decoration.TIDecoratorFacade;
import globaz.pyxis.services.decoration.TIHtmlDecoratorRender;
import globaz.pyxis.util.TINSSFormater;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import process.LACreationTiersProcess;
import db.LAFichierCentralListViewBean;
import db.LAFichierCentralViewBean;
import db.LAInsertionFichierViewBean;
import db.LASuiviCaisseListViewBean;
import db.LASuiviCaisseViewBean;

/**
 * @author jpa
 */
public class LAActionFichier extends FWDefaultServletAction {

    private static String CS_MOTIF_INCONNU = "506007";

    public LAActionFichier(FWServlet servlet) {
        super(servlet);
    }

    private void _actionAfficherHistorique(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            BSession bSession) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            String idAffiliation = (String) session.getAttribute("idAffiliation");
            LAInsertionFichierViewBean viewBean = new LAInsertionFichierViewBean();
            viewBean.setIdAffiliation(idAffiliation);
            chargerInformationsAffiliationTiers(idAffiliation, viewBean, bSession);
            request.setAttribute("viewBean", viewBean);
            servlet.getServletContext()
                    .getRequestDispatcher(
                            getRelativeURLwithoutClassPart(request, session) + "historique_de.jsp?_method=add")
                    .forward(request, response);
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    private void _actionAfficherMutation(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            BSession bSession) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            String idAffiliation = (String) session.getAttribute("idAffiliation");
            LAInsertionFichierViewBean viewBean = new LAInsertionFichierViewBean();
            chargerInformationsAffiliationTiers(idAffiliation, viewBean, bSession);
            request.setAttribute("viewBean", viewBean);
            session.setAttribute("viewBean", viewBean);
            servlet.getServletContext()
                    .getRequestDispatcher(
                            getRelativeURLwithoutClassPart(request, session) + "mutation_de.jsp?_method=add")
                    .forward(request, response);
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    private void _actionAfficherSuivi(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            BSession bSession) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            String idSuiviCaisse = request.getParameter("selectedId");
            String idAffiliation = (String) session.getAttribute("idAffiliation");
            LASuiviCaisseViewBean viewBean = new LASuiviCaisseViewBean();
            viewBean.setSession(bSession);
            if (!JadeStringUtil.isEmpty(idSuiviCaisse)) {
                viewBean.setSuiviCaisseId(idSuiviCaisse);
                viewBean.retrieve();
            }
            viewBean.setAffiliationId(idAffiliation);
            request.setAttribute("viewBean", viewBean);
            servlet.getServletContext()
                    .getRequestDispatcher(
                            getRelativeURLwithoutClassPart(request, session) + "suiviCaisse_de.jsp?_method=add")
                    .forward(request, response);
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    private void _actionAfficherSuiviMenu(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            BSession bSession) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        AFAffiliation affiliation = null;
        try {
            LAInsertionFichierViewBean viewBean;
            viewBean = new LAInsertionFichierViewBean();
            String idAffiliation = request.getParameter("idAffiliation");
            if (JadeStringUtil.isEmpty(idAffiliation)) {
                idAffiliation = (String) session.getAttribute("idAffiliation");
            }
            if (JadeStringUtil.isEmpty(idAffiliation)) {
                idAffiliation = (String) request.getAttribute("idAffiliation");
            }
            if (idAffiliation == null || JadeStringUtil.isEmpty(idAffiliation)) {
                String idTiers = request.getParameter("selectedId");
                // On redirige sur tiers
                if (!JadeStringUtil.isEmpty(idTiers)) {
                    servlet.getServletContext()
                            .getRequestDispatcher("/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=" + idTiers)
                            .forward(request, response);
                }
                // chargerInformationsTiers(idTiers, viewBean,
                // (BSession)mainDispatcher.getSession());
            } else {
                affiliation = chargerInformationsAffiliationTiers(idAffiliation, viewBean, bSession);
            }
            if (affiliation != null) {
                // Redirection sur affiliation ou sur lacerta suivant si
                // l'affiliation est de type FC
                if (affiliation.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_FICHIER_CENT)) {
                    // On redirige sur fichier central modifier tiers :
                    // modifierTiers_de.jsp
                    // _destination = getRelativeURLwithoutClassPart(request,
                    // session) + "modifierTiers_de.jsp";
                    session.setAttribute("viewBean", viewBean);
                    request.setAttribute("viewBean", viewBean);
                    session.setAttribute("idAffiliation", idAffiliation);
                    // On redirige sur le suivi des caisses
                    _destination = getRelativeURLwithoutClassPart(request, session) + "suiviCaisse_rc.jsp";
                } else {
                    // On redirige sur affiliation
                    servlet.getServletContext()
                            .getRequestDispatcher(
                                    "/naos?userAction=naos.affiliation.autreDossier.modifier&numAffilie="
                                            + affiliation.getAffilieNumero() + "&forAction=null")
                            .forward(request, response);
                }
            } else {
                session.setAttribute("viewBean", viewBean);
                request.setAttribute("viewBean", viewBean);
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    private void _actionInserer(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, BSession bSession) {
        String _destination = "";
        BTransaction transaction = null;
        try {
            transaction = new BTransaction(bSession);
            transaction.openTransaction();
            LAInsertionFichierViewBean viewBean = new LAInsertionFichierViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            LACreationTiersProcess.insertionFichier(bSession, viewBean, transaction);
            boolean isOnError = controleDates(viewBean, bSession);
            if ((!bSession.hasErrors()) && (!isOnError) && (!transaction.hasErrors())) {
                _destination = getRelativeURL(request, session) + "_rc.jsp";
                transaction.commit();
            } else {
                transaction.rollback();
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(bSession.getErrors().toString() + " " + transaction.getErrors().toString());
                session.setAttribute("viewBean", viewBean);
                _destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail&_back=sl";
            }
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        } finally {
            try {
                transaction.closeTransaction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /*
         * redirection vers la destination
         */
        try {
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void _actionInsererSuivi(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            BSession bSession) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            // boolean onError = validateSuivi(bSession);
            LASuiviCaisseViewBean viewBean = new LASuiviCaisseViewBean();
            String numeroAffilieCaisse = request.getParameter("numeroAffilieCaisse");
            String numCaisse = request.getParameter("numCaisse");
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            AFSuiviCaisseAffiliationViewBean suivi = new AFSuiviCaisseAffiliationViewBean();
            suivi.setSession(bSession);
            suivi.setIdTiersCaisse(getIdCaisse(bSession, numCaisse));
            suivi.setGenreCaisse(viewBean.getGenreCaisse());
            suivi.setNumeroAffileCaisse(numeroAffilieCaisse);
            suivi.setDateDebut(viewBean.getDateDebut());
            suivi.setDateFin(viewBean.getDateFin());
            if (validateSuivi(bSession, suivi, numCaisse)) {
                suivi.add();
                if (!bSession.hasErrors()) {
                    _destination = "/lacerta?userAction=lacerta.fichier.afficher&idAffiliation="
                            + suivi.getAffiliationId();
                } else {
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage(bSession.getErrors().toString());
                    _destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail&_back=sl";
                }
            } else {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(bSession.getErrors().toString());
                _destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail&_back=sl";
            }
            request.setAttribute("viewBean", viewBean);
            session.setAttribute("idAffiliation", suivi.getAffiliationId());

            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    private void _actionListerSuivi(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            BSession bSession) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            // LAInsertionFichierViewBean insertionViewBean =
            // (LAInsertionFichierViewBean) session.getAttribute("viewBean");
            LASuiviCaisseListViewBean viewBean = new LASuiviCaisseListViewBean();
            String idAffiliation = (String) session.getAttribute("idAffiliation");
            viewBean.setForAffiliationId(idAffiliation);
            viewBean.setSession(bSession);
            viewBean.find();
            request.setAttribute("viewBean", viewBean);
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            _destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    private void _actionModifierSuivi(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            BSession bSession) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        String idAffiliation = (String) session.getAttribute("idAffiliation");
        AFSuiviCaisseAffiliationViewBean oldSuivi = new AFSuiviCaisseAffiliationViewBean();
        try {
            // On recherche l'idAffiliation
            String idSuiviCaisse = request.getParameter("suiviCaisseId");
            LASuiviCaisseViewBean viewBean = new LASuiviCaisseViewBean();
            String numeroAffilieCaisse = request.getParameter("numeroAffilieCaisse");
            String numCaisse = request.getParameter("numCaisse");
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            oldSuivi.setSession(bSession);
            oldSuivi.setSuiviCaisseId(idSuiviCaisse);
            oldSuivi.retrieve();
            if (validateSuivi(bSession, viewBean, numCaisse)) {
                if (!oldSuivi.isNew()) {
                    // On va rechercher l'id correspondant à ce numéro de caisse
                    oldSuivi.setIdTiersCaisse(getIdCaisse(bSession, numCaisse));
                    oldSuivi.setGenreCaisse(viewBean.getGenreCaisse());
                    oldSuivi.setNumeroAffileCaisse(numeroAffilieCaisse);
                    oldSuivi.setDateDebut(viewBean.getDateDebut());
                    oldSuivi.setDateFin(viewBean.getDateFin());
                    oldSuivi.update();
                } else {
                    oldSuivi = new AFSuiviCaisseAffiliationViewBean();
                    oldSuivi.setSession(bSession);
                    oldSuivi.setIdTiersCaisse(getIdCaisse(bSession, numCaisse));
                    oldSuivi.setGenreCaisse(viewBean.getGenreCaisse());
                    oldSuivi.setNumeroAffileCaisse(numeroAffilieCaisse);
                    oldSuivi.setDateDebut(viewBean.getDateDebut());
                    oldSuivi.setDateFin(viewBean.getDateFin());
                    oldSuivi.setAffiliationId(idAffiliation);
                    oldSuivi.add();
                }
                if (!bSession.hasErrors()) {
                    request.setAttribute("viewBean", viewBean);
                    session.setAttribute("idAffiliation", oldSuivi.getAffiliationId());
                    _destination = "/lacerta?userAction=lacerta.fichier.afficher&idAffiliation="
                            + oldSuivi.getAffiliationId();
                    servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
                } else {
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage(bSession.getErrors().toString());
                    session.setAttribute("viewBean", viewBean);
                    request.setAttribute("viewBean", viewBean);
                    _destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail&_back=sl";
                }

            } else {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(bSession.getErrors().toString());
                session.setAttribute("viewBean", viewBean);
                request.setAttribute("viewBean", viewBean);
                _destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail&_back=sl";
            }
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    private void _actionMutation(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, BSession bSession) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {
            LAInsertionFichierViewBean viewBean = new LAInsertionFichierViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            String idAffiliation = (String) session.getAttribute("idAffiliation");
            modifierTiers(idAffiliation, viewBean, bSession);
            if ((!bSession.hasErrors())) {
                _destination = getRelativeURLwithoutClassPart(request, session) + "fichier_rc.jsp?_method=add";
                servlet.getServletContext()
                        .getRequestDispatcher(
                                getRelativeURLwithoutClassPart(request, session) + "fichier_rc.jsp?_method=add")
                        .forward(request, response);
            } else {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(bSession.getErrors().toString());
                session.setAttribute("viewBean", viewBean);
                _destination = getRelativeURLwithoutClassPart(request, session)
                        + "mutation_de.jsp?_valid=fail&_back=sl";
            }
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * critères obligatoires (au moins 1 parmis la liste)
     */
    private boolean _criteresOk(HttpServletRequest request) {
        String[] criteres = new String[] { "forNumAvs", "forNumAffilie", "forNumContribuableLike",
                "forDesignationUpper1Like", "forDesignationUpper2Like", "forAlias", "forNpaOrLocaliteLike",
                "forIdTiersExterneLike", "forDateNaissance" };

        StringBuffer buf = new StringBuffer("");
        for (int i = 0; i < criteres.length; i++) {
            buf.append(request.getParameter(criteres[i]));
        }
        return !JadeStringUtil.isBlank(buf.toString());
    }

    /**
     * @param viewBean
     */
    private void _decoreBean(LAFichierCentralListViewBean viewBean) throws Exception {
        String listIdTiers = "";
        for (int i = 0; i < viewBean.size(); i++) {
            if (!JadeStringUtil.isEmpty(listIdTiers)) {
                listIdTiers += ",";
            }
            listIdTiers += ((LAFichierCentralViewBean) viewBean.getEntity(i)).getIdTiers();
        }

        if (!JadeStringUtil.isEmpty(listIdTiers)) {
            Map map = TIDecoratorFacade.buildDecoratorsMap(viewBean.getSession(), listIdTiers,
                    new TIHtmlDecoratorRender());
            for (int i = 0; i < viewBean.size(); i++) {
                LAFichierCentralViewBean bean = (LAFichierCentralViewBean) viewBean.getEntity(i);
                bean.setDecorations((String) map.get(bean.getIdTiers()));
            }

        }

    }

    /**
     * @return
     */
    private boolean _isRechEmpty(HttpServletRequest request) {
        String[] criteres = new String[] { "forNumAvs", "forNumAffilie", "forNumContribuableLike",
                "forDesignationUpper1Like", "forDesignationUpper2Like", "forAlias", "forNpaOrLocaliteLike", "forRole",
                "forDateNaissance", "forSexe", "forIdTiersExterneLike", "forActiviteEntreDebutEtFin"

        };

        StringBuffer buf = new StringBuffer("");
        for (int i = 0; i < criteres.length; i++) {
            if (request.getParameter(criteres[i]) != null) {
                buf.append(request.getParameter(criteres[i]));
            }
        }
        return JadeStringUtil.isBlank(buf.toString());

    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _destination = "";
        AFAffiliation affiliation = null;
        try {
            LAInsertionFichierViewBean viewBean;
            viewBean = new LAInsertionFichierViewBean();
            String idAffiliation = request.getParameter("idAffiliation");
            if (idAffiliation == null || JadeStringUtil.isEmpty(idAffiliation)) {
                String idTiers = request.getParameter("selectedId");
                // On redirige sur tiers
                if (!JadeStringUtil.isEmpty(idTiers)) {
                    servlet.getServletContext()
                            .getRequestDispatcher("/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=" + idTiers)
                            .forward(request, response);
                }
                // chargerInformationsTiers(idTiers, viewBean,
                // (BSession)mainDispatcher.getSession());
            } else {
                affiliation = chargerInformationsAffiliationTiers(idAffiliation, viewBean,
                        (BSession) mainDispatcher.getSession());
                viewBean.setIdAffiliation(affiliation.getAffiliationId());
            }
            if (affiliation != null) {
                // Redirection sur affiliation ou sur lacerta suivant si
                // l'affiliation est de type FC
                if (affiliation.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_FICHIER_CENT)) {
                    // On redirige sur fichier central modifier tiers :
                    // modifierTiers_de.jsp
                    // _destination = getRelativeURLwithoutClassPart(request,
                    // session) + "modifierTiers_de.jsp";
                    session.setAttribute("viewBean", viewBean);
                    request.setAttribute("viewBean", viewBean);
                    session.setAttribute("idAffiliation", idAffiliation);
                    // On redirige sur le suivi des caisses
                    _destination = getRelativeURLwithoutClassPart(request, session) + "suiviCaisse_rc.jsp";
                } else {
                    // On redirige sur affiliation
                    servlet.getServletContext()
                            .getRequestDispatcher(
                                    "/naos?userAction=naos.affiliation.autreDossier.modifier&numAffilie="
                                            + affiliation.getAffilieNumero() + "&forAction=null")
                            .forward(request, response);
                }
            } else {
                session.setAttribute("viewBean", viewBean);
                request.setAttribute("viewBean", viewBean);
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    protected void actionCreeAvisMutation(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws javax.servlet.ServletException, java.io.IOException {

        String _destination;

        AFAnnonceAffilieViewBean vBean = new AFAnnonceAffilieViewBean();

        // Tiers
        String idTiers = "";
        if (!JadeStringUtil.isEmpty((String) session.getAttribute("tiersPrincipale"))) {
            idTiers = (String) session.getAttribute("tiersPrincipale");
        } else if (!JadeStringUtil.isEmpty(request.getParameter("idTiers"))) {
            idTiers = request.getParameter("idTiers");
        }

        if (!JadeStringUtil.isEmpty(idTiers)) {
            try {
                BISession bSession = CodeSystem.getSession(session);
                vBean.setSession((BSession) bSession);

                vBean._creationAvisMutation();
                getAction().changeActionPart(FWAction.ACTION_AFFICHER);
            } catch (Exception e) {
                vBean.setMsgType(FWViewBeanInterface.ERROR);
                vBean.setMessage(e.getMessage());
            }
        }

        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", vBean);

        if (vBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            _destination = ERROR_PAGE;
        } else if (JadeStringUtil.isEmpty(idTiers)) {
            // _destination =
            // "/"+getAction().getApplicationPart()+"?userAction=naos.affiliation.affilieSelect.chercher";
            _destination = "/" + getAction().getApplicationPart()
                    + "?userAction=naos.affiliation.autreDossier.afficher&_method=upd";
        } else {
            _destination = "/" + getAction().getApplicationPart()
                    + "?userAction=naos.affiliation.affiliation.chercher&idTiers=" + idTiers;
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        if ("inserer".equals(getAction().getActionPart())) {
            _actionInserer(session, request, response, (BSession) dispatcher.getSession());
        } else if ("listerSuivi".equals(getAction().getActionPart())) {
            _actionListerSuivi(session, request, response, (BSession) dispatcher.getSession());
        } else if ("afficherSuivi".equals(getAction().getActionPart())) {
            _actionAfficherSuivi(session, request, response, (BSession) dispatcher.getSession());
        } else if ("modifierSuivi".equals(getAction().getActionPart())) {
            _actionModifierSuivi(session, request, response, (BSession) dispatcher.getSession());
        } else if ("insererSuivi".equals(getAction().getActionPart())) {
            _actionInsererSuivi(session, request, response, (BSession) dispatcher.getSession());
        } else if ("afficherMutation".equals(getAction().getActionPart())) {
            _actionAfficherMutation(session, request, response, (BSession) dispatcher.getSession());
        } else if ("mutation".equals(getAction().getActionPart())) {
            _actionMutation(session, request, response, (BSession) dispatcher.getSession());
        } else if ("afficherHistorique".equals(getAction().getActionPart())) {
            _actionAfficherHistorique(session, request, response, (BSession) dispatcher.getSession());
        } else if ("afficherSuiviMenu".equals(getAction().getActionPart())) {
            _actionAfficherSuiviMenu(session, request, response, (BSession) dispatcher.getSession());
        }
    }

    /**
	 * 
	 */
    @Override
    protected void actionFindNext(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("listViewBean");

            FWAction _action = new FWRequestActionAdapter().adapt(request, FWSecureConstants.READ);
            viewBean = mainDispatcher.dispatch(viewBean, _action);

            _decoreBean((LAFichierCentralListViewBean) viewBean);

            request.setAttribute("viewBean", viewBean);

            _destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
	 * 
	 */
    @Override
    protected void actionFindPrevious(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("listViewBean");

            FWAction _action = new FWRequestActionAdapter().adapt(request, FWSecureConstants.READ);
            viewBean = mainDispatcher.dispatch(viewBean, _action);

            _decoreBean((LAFichierCentralListViewBean) viewBean);

            request.setAttribute("viewBean", viewBean);

            _destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";

        try {
            FWAction _action = FWAction.newInstance(request.getParameter("userAction"));
            LAFichierCentralListViewBean viewBean = new LAFichierCentralListViewBean();// FWListViewBeanActionFactory.newInstance(_action,mainDispatcher.getPrefix());
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setIncludeLibelleExterne(true);

            // TODO : AVSMOD
            if ("true".equals(request.getParameter("forNumAvsNNSS"))) {
                viewBean.setTypeRechercheAvs(TINSSFormater.TYPE_EAN13);
            } else {
                viewBean.setTypeRechercheAvs(TINSSFormater.TYPE_NAVS);
            }

            /**
             * check des critères
             */
            boolean criteresOk = _criteresOk(request);

            String forDateNaissance = request.getParameter("forDateNaissance");
            if (!JadeStringUtil.isEmpty(forDateNaissance)) {
                if (forDateNaissance.length() == 4) {
                    // année seulement XXXX
                    viewBean.setForDateNaissance("");
                    viewBean.setForDateNaissanceYear(forDateNaissance);
                }
                if (forDateNaissance.length() == 2) {
                    // année seulement XX
                    viewBean.setForDateNaissance("");
                    viewBean.setForDateNaissanceYear("19" + forDateNaissance);
                }
            }

            /*
             * si la recherche est vide, on cherche les tiers crées/modifiés aujourd'hui
             */
            if (_isRechEmpty(request)) {
                viewBean.setForTodaySpy(new Boolean(true));
                criteresOk = true;

            }

            // System.gc();
            BSession ses = (BSession) mainDispatcher.getSession();

            if (criteresOk) {
                viewBean = (LAFichierCentralListViewBean) beforeLister(session, request, response, viewBean);
                viewBean = (LAFichierCentralListViewBean) mainDispatcher.dispatch(viewBean, _action);
                _decoreBean(viewBean);

            } else {

                viewBean.setMessage(ses.getLabel("CRITERES_INSUFFISANTS"));
                viewBean.setMsgType(FWViewBeanInterface.ERROR);

            }
            request.setAttribute("viewBean", viewBean);

            session.removeAttribute("listViewBean");
            session.setAttribute("listViewBean", viewBean);
            _destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    private boolean adresseCourrierChanged(String idTiers, LAInsertionFichierViewBean viewBean, BSession session) {
        TIAvoirAdresseManager manager = new TIAvoirAdresseManager();
        manager.setSession(session);
        // manager.setForIdApplication(idApplication);
        manager.setForIdTiers(idTiers);
        manager.setForTypeAdresse(TIAvoirAdresse.CS_COURRIER);
        manager.setOrderBy(" HEDDAD DESC ");
        // si il n'y a qu'une adresse, est-ce celle que l'on est en train de
        // traiter
        try {
            manager.find();
        } catch (Exception e) {
            return true;
        }
        if (manager.size() > 0) {
            TIAvoirAdresse entity = (TIAvoirAdresse) manager.getFirstEntity();
            if (!entity.getRue().equals(viewBean.getAdresseCRue())) {
                return true;
            }
            if (!entity.getNumeroRue().equals(viewBean.getAdresseCNumero())) {
                return true;
            }
            if (!entity.getIdLocalite().equals(viewBean.getIdCLocalite())) {
                return true;
            }
        } else {
            if (!JadeStringUtil.isEmpty(viewBean.getAdresseCRue())) {
                return true;
            }
            if (!JadeStringUtil.isEmpty(viewBean.getAdresseCNumero())) {
                return true;
            }
            if (!JadeStringUtil.isEmpty(viewBean.getIdCLocalite())) {
                return true;
            }
        }
        return false;
    }

    private boolean adresseDomicileChanged(String idTiers, LAInsertionFichierViewBean viewBean, BSession session) {
        TIAvoirAdresseManager manager = new TIAvoirAdresseManager();
        manager.setSession(session);
        // manager.setForIdApplication(idApplication);
        manager.setForIdTiers(idTiers);
        manager.setForTypeAdresse(TIAvoirAdresse.CS_DOMICILE);
        manager.setOrderBy(" HEDDAD DESC ");
        // si il n'y a qu'une adresse, est-ce celle que l'on est en train de
        // traiter
        try {
            manager.find();
        } catch (Exception e) {
            return true;
        }
        if (manager.size() > 0) {
            TIAvoirAdresse entity = (TIAvoirAdresse) manager.getFirstEntity();
            if (!entity.getRue().equals(viewBean.getAdresseDRue())) {
                return true;
            }
            if (!entity.getNumeroRue().equals(viewBean.getAdresseDNumero())) {
                return true;
            }
            if (!entity.getIdLocalite().equals(viewBean.getIdDLocalite())) {
                return true;
            }
        } else {
            if (!JadeStringUtil.isEmpty(viewBean.getAdresseDRue())) {
                return true;
            }
            if (!JadeStringUtil.isEmpty(viewBean.getAdresseDNumero())) {
                return true;
            }
            if (!JadeStringUtil.isEmpty(viewBean.getIdDLocalite())) {
                return true;
            }
        }
        return false;
    }

    private AFAffiliation chargerInformationsAffiliationTiers(String idAffiliation,
            LAInsertionFichierViewBean viewBean, BSession session) {
        try {
            BSession sessionNaos = ((BSession) GlobazSystem.getApplication(AFApplication.DEFAULT_APPLICATION_NAOS)
                    .newSession(session));
            AFAffiliationViewBean affiliation = new AFAffiliationViewBean();
            affiliation.setSession(sessionNaos);
            affiliation.setAffiliationId(idAffiliation);
            viewBean.setIdTiers(affiliation.getIdTiers());
            affiliation.retrieve();
            // On charge les informations de l'affiliation
            viewBean.setNumAffilie(affiliation.getAffilieNumero());
            viewBean.setFormeJuridique(affiliation.getPersonnaliteJuridique());
            // On charge les informations du tiers
            chargerInformationsTiers(affiliation.getIdTiers(), viewBean, session);
            return affiliation;
        } catch (Exception e) {
            return null;
        }

    }

    private LAInsertionFichierViewBean chargerInformationsTiers(String idTiers, LAInsertionFichierViewBean viewBean,
            BSession session) {
        TITiersViewBean tiers = new TITiersViewBean();
        tiers.setSession(session);
        tiers.setIdTiers(idTiers);
        try {
            // /On charge les infos de l'alias
            TIAliasManager aliasManager = new TIAliasManager();
            aliasManager.setSession(session);
            aliasManager.setForIdTiers(idTiers);
            aliasManager.find();
            if (aliasManager.size() > 0) {
                TIAlias alias = (TIAlias) aliasManager.getFirstEntity();
                viewBean.setAlias(alias.getLibelleAlias());
            }
            // On charge les infos spécifique au tiers
            tiers.retrieve();
            viewBean.setNom(tiers.getDesignation1());
            viewBean.setPrenom(tiers.getDesignation2());
            viewBean.setNomSuite(tiers.getDesignation3());
            viewBean.setTitre(tiers.getTitreTiers());
            viewBean.setNumAvs(tiers.getNumAvsActuel());
            // viewBean.setNomSuite(tiers.get)
            // On charge les infos spécifique à l'adresse de domicile
            TIAvoirAdresseManager avAdrMng = new TIAvoirAdresseManager();
            avAdrMng.setSession(session);
            avAdrMng.setForIdTiers(idTiers);
            avAdrMng.setForTypeAdresse(TIAvoirAdresse.CS_DOMICILE);
            avAdrMng.setForDateEntreDebutEtFin(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));
            avAdrMng.find();
            if (avAdrMng.size() > 0) {
                TIAvoirAdresse adresseDomicile = (TIAvoirAdresse) avAdrMng.getFirstEntity();
                viewBean.setAdresseDRue(adresseDomicile.getRue());
                viewBean.setAdresseDNumero(adresseDomicile.getNumeroRue());
                viewBean.setIdDLocalite(adresseDomicile.getIdLocalite());
                TILocalite localiteDomicile = new TILocalite();
                localiteDomicile.setSession(session);
                localiteDomicile.setIdLocalite(adresseDomicile.getIdLocalite());
                localiteDomicile.retrieve();
                if (!localiteDomicile.isNew()) {
                    viewBean.setLocaliteDCode(localiteDomicile.getNumPostal());
                    viewBean.setLocaliteD(localiteDomicile.getLocalite());
                }
            }
            // On charge les infos spécifique à l'adresse de courrier
            TIAvoirAdresseManager avAdrMngCourrier = new TIAvoirAdresseManager();
            avAdrMngCourrier.setSession(session);
            avAdrMngCourrier.setForIdTiers(idTiers);
            avAdrMngCourrier.setForTypeAdresse(TIAvoirAdresse.CS_COURRIER);
            avAdrMngCourrier.setForDateEntreDebutEtFin(JACalendar.format(JACalendar.today(),
                    JACalendar.FORMAT_DDsMMsYYYY));
            avAdrMngCourrier.find();
            if (avAdrMngCourrier.size() > 0) {
                TIAvoirAdresse adresseCourrier = (TIAvoirAdresse) avAdrMngCourrier.getFirstEntity();
                viewBean.setAdresseCRue(adresseCourrier.getRue());
                viewBean.setAdresseCNumero(adresseCourrier.getNumeroRue());
                viewBean.setIdCLocalite(adresseCourrier.getIdLocalite());
                TILocalite localiteCourrier = new TILocalite();
                localiteCourrier.setSession(session);
                localiteCourrier.setIdLocalite(adresseCourrier.getIdLocalite());
                localiteCourrier.retrieve();
                if (!localiteCourrier.isNew()) {
                    viewBean.setLocaliteCCode(localiteCourrier.getNumPostal());
                    viewBean.setLocaliteC(localiteCourrier.getLocalite());
                }
            }
            return viewBean;
        } catch (Exception e) {
            return new LAInsertionFichierViewBean();
        }
    }

    private boolean controleDates(LAInsertionFichierViewBean viewBean, BSession session) {
        boolean isOnError = false;
        // On contrôlle la validité des dates
        try {
            BSessionUtil.checkDateGregorian(session, viewBean.getCaisseAVSDateDebut());
        } catch (Exception e) {
            session.addError(session.getLabel("DATE_DEBUT_AVS_NON_VALIDE") + " : " + viewBean.getCaisseAVSDateDebut());
            isOnError = true;
        }
        try {
            BSessionUtil.checkDateGregorian(session, viewBean.getCaisseAVSDateFin());
        } catch (Exception e) {
            session.addError(session.getLabel("DATE_FIN_AVS_NON_VALIDE") + " : " + viewBean.getCaisseAVSDateFin());
            isOnError = true;
        }
        try {
            BSessionUtil.checkDateGregorian(session, viewBean.getCaisseAFDateDebut());
        } catch (Exception e) {
            session.addError(session.getLabel("DATE_DEBUT_AF_NON_VALIDE") + " : " + viewBean.getCaisseAFDateDebut());
            isOnError = true;
        }
        try {
            BSessionUtil.checkDateGregorian(session, viewBean.getCaisseAFDateFin());
        } catch (Exception e) {
            session.addError(session.getLabel("DATE_FIN_AF_NON_VALIDE") + " : " + viewBean.getCaisseAFDateFin());
            isOnError = true;
        }
        try {
            BSessionUtil.checkDateGregorian(session, viewBean.getPersonnelMaisonDateDebut());
        } catch (Exception e) {
            session.addError(session.getLabel("DATE_DEBUT_PERSONNEL_MAISON_NON_VALIDE") + " : "
                    + viewBean.getPersonnelMaisonDateDebut());
            isOnError = true;
        }
        try {
            BSessionUtil.checkDateGregorian(session, viewBean.getPersonnelMaisonDateFin());
        } catch (Exception e) {
            session.addError(session.getLabel("DATE_FIN_PERSONNEL_MAISON_NON_VALIDE") + " : "
                    + viewBean.getPersonnelMaisonDateFin());
            isOnError = true;
        }
        // On controle que si la date de fin est renseignée, la date de début
        // doit l'être
        if (!JadeStringUtil.isEmpty(viewBean.getCaisseAVSDateFin())) {
            if (JadeStringUtil.isEmpty(viewBean.getCaisseAVSDateDebut())) {
                session.addError(session.getLabel("DATE_DEBUT_AVS_VIDE"));
                isOnError = true;
            }
        }
        if (!JadeStringUtil.isEmpty(viewBean.getCaisseAFDateFin())) {
            if (JadeStringUtil.isEmpty(viewBean.getCaisseAFDateDebut())) {
                session.addError(session.getLabel("DATE_DEBUT_AF_VIDE"));
                isOnError = true;
            }
        }
        if (!JadeStringUtil.isEmpty(viewBean.getPersonnelMaisonDateFin())) {
            if (JadeStringUtil.isEmpty(viewBean.getPersonnelMaisonDateDebut())) {
                session.addError(session.getLabel("DATE_DEBUT_PERSONNEL_MAISON_VIDE"));
                isOnError = true;
            }
        }
        try {
            // On contrôlle que la date de fin soit supérieure à la date de
            // début
            if ((!JadeStringUtil.isEmpty(viewBean.getCaisseAVSDateFin()))
                    && (!JadeStringUtil.isEmpty(viewBean.getCaisseAVSDateDebut()))) {
                if (!BSessionUtil.compareDateFirstLowerOrEqual(session, viewBean.getCaisseAVSDateDebut(),
                        viewBean.getCaisseAVSDateFin())) {
                    session.addError(session.getLabel("DATE_AVS_FIN_SUPERIEUR_DATE_DEBUT"));
                    isOnError = true;
                }
            }
            if ((!JadeStringUtil.isEmpty(viewBean.getCaisseAFDateFin()))
                    && (!JadeStringUtil.isEmpty(viewBean.getCaisseAFDateDebut()))) {
                if (!BSessionUtil.compareDateFirstLowerOrEqual(session, viewBean.getCaisseAFDateDebut(),
                        viewBean.getCaisseAFDateFin())) {
                    session.addError(session.getLabel("DATE_AF_FIN_SUPERIEUR_DATE_DEBUT"));
                    isOnError = true;
                }
            }
            if ((!JadeStringUtil.isEmpty(viewBean.getPersonnelMaisonDateFin()))
                    && (!JadeStringUtil.isEmpty(viewBean.getPersonnelMaisonDateDebut()))) {
                if (!BSessionUtil.compareDateFirstLowerOrEqual(session, viewBean.getPersonnelMaisonDateDebut(),
                        viewBean.getPersonnelMaisonDateFin())) {
                    session.addError(session.getLabel("DATE_PERSONNEL_MAISON_FIN_SUPERIEUR_DATE_DEBUT"));
                    isOnError = true;
                }
            }
            // Si on a une date la caisse doit être renseignée
            if ((!JadeStringUtil.isEmpty(viewBean.getCaisseAFDateDebut()))
                    || (!JadeStringUtil.isEmpty(viewBean.getCaisseAFDateDebut()))) {
                if (JadeStringUtil.isEmpty(viewBean.getCaisseAFNum())) {
                    session.addError(session.getLabel("NUM_CAISSE_AF_VIDE"));
                    isOnError = true;
                }
            }
            if ((!JadeStringUtil.isEmpty(viewBean.getCaisseAVSDateDebut()))
                    || (!JadeStringUtil.isEmpty(viewBean.getCaisseAVSDateDebut()))) {
                if (JadeStringUtil.isEmpty(viewBean.getCaisseAVSNum())) {
                    session.addError(session.getLabel("NUM_CAISSE_AVS_VIDE"));
                    isOnError = true;
                }
            }
        } catch (Exception e) {
            isOnError = true;
        }
        return isOnError;
    }

    private boolean controleDatesSuiviCaisses(BSession session, AFSuiviCaisseAffiliationViewBean viewBean) {
        boolean isOnError = false;
        // On contrôlle la validité des dates
        try {
            BSessionUtil.checkDateGregorian(session, viewBean.getDateDebut());
        } catch (Exception e) {
            session.addError(session.getLabel("DATE_DEBUT_NON_VALIDE") + " : " + viewBean.getDateDebut());
            isOnError = true;
        }
        try {
            BSessionUtil.checkDateGregorian(session, viewBean.getDateFin());
        } catch (Exception e) {
            session.addError(session.getLabel("DATE_FIN_NON_VALIDE") + " : " + viewBean.getDateFin());
            isOnError = true;
        }
        // On controle que si la date de fin est renseignée, la date de début
        // doit l'être
        if (JadeStringUtil.isEmpty(viewBean.getDateDebut())) {
            session.addError(session.getLabel("DATE_DEBUT_VIDE"));
            isOnError = true;
        }
        try {
            // On contrôlle que la date de fin soit supérieure à la date de
            // début
            if ((!JadeStringUtil.isEmpty(viewBean.getDateFin())) && (!JadeStringUtil.isEmpty(viewBean.getDateDebut()))) {
                if (!BSessionUtil.compareDateFirstLowerOrEqual(session, viewBean.getDateDebut(), viewBean.getDateFin())) {
                    session.addError(session.getLabel("DATE_FIN_SUPERIEUR_DATE_DEBUT"));
                    isOnError = true;
                }
            }
        } catch (Exception e) {
            isOnError = true;
        }
        return isOnError;
    }

    public String getIdCaisse(BSession session, String numCaisse) {
        try {
            TIAdministrationManager manager = new TIAdministrationManager();
            manager.setSession(session);
            manager.setForCodeAdministration(numCaisse);
            manager.find();
            TIAdministrationViewBean administration = (TIAdministrationViewBean) manager.getFirstEntity();
            return administration.getIdTiersAdministration();
        } catch (Exception e) {
            return "";
        }
    }

    private void modifierTiers(String idAffiliation, LAInsertionFichierViewBean viewBean, BSession session) {
        try {
            BSession sessionNaos = ((BSession) GlobazSystem.getApplication(AFApplication.DEFAULT_APPLICATION_NAOS)
                    .newSession(session));
            AFAffiliation affiliation = new AFAffiliation();
            affiliation.setSession(sessionNaos);
            affiliation.setAffiliationId(idAffiliation);
            affiliation.retrieve();
            if (!affiliation.isNew()) {
                if (JadeStringUtil.isEmpty(viewBean.getFormeJuridique())) {
                    viewBean.setFormeJuridique("0");
                }
                if (!affiliation.getPersonnaliteJuridique().equals(viewBean.getFormeJuridique())) {
                    if (!affiliation.getPersonnaliteJuridique().equals(viewBean.getFormeJuridique())) {
                        affiliation.setPersonnaliteJuridique(viewBean.getFormeJuridique());
                        affiliation.update();
                    }
                }
                String idTiers = affiliation.getIdTiers();
                TITiersViewBean tiers = new TITiersViewBean();
                tiers.setSession(session);
                tiers.setIdTiers(idTiers);
                tiers.retrieve();
                if (!tiers.isNew()) {
                    if (tiersChanged(tiers, viewBean)) {
                        tiers.setNumAvsActuelNNSS(viewBean.getNumAvs());
                        tiers.setNumAvsActuel(viewBean.getNumAvs());
                        tiers.setDesignation1(viewBean.getNom());
                        tiers.setDesignation2(viewBean.getPrenom());
                        tiers.setDesignation3(viewBean.getNomSuite());
                        tiers.setTitreTiers(viewBean.getTitre());
                        tiers.setMotifModifAvs(CS_MOTIF_INCONNU);
                        tiers.setDateModifAvs(JACalendar.todayJJsMMsAAAA());
                        tiers.update();
                    }
                    if (adresseCourrierChanged(tiers.getIdTiers(), viewBean, session)) {
                        LACreationTiersProcess.creationAdresseCourrier(session, tiers.getIdTiers(), viewBean,
                                session.getCurrentThreadTransaction());
                    }
                    if (adresseDomicileChanged(tiers.getIdTiers(), viewBean, session)) {
                        LACreationTiersProcess.creationAdresseDomicile(session, tiers.getIdTiers(), viewBean,
                                session.getCurrentThreadTransaction());
                    }

                }
            }
        } catch (Exception e) {
        }
    }

    private boolean tiersChanged(TITiersViewBean tiers, LAInsertionFichierViewBean viewBean) {
        if (!viewBean.getNumAvs().equals(tiers.getNumAvsActuel())) {
            return true;
        }
        if (!viewBean.getNom().equals(tiers.getDesignation1())) {
            return true;
        }
        if (!viewBean.getPrenom().equals(tiers.getDesignation2())) {
            return true;
        }
        if (!viewBean.getNomSuite().equals(tiers.getDesignation3())) {
            return true;
        }
        if (JadeStringUtil.isEmpty(viewBean.getTitre())) {
            viewBean.setTitre("0");
        }
        if (!tiers.getTitreTiers().equals(viewBean.getTitre())) {
            return true;
        }
        return false;
    }

    private boolean validateSuivi(BSession session, AFSuiviCaisseAffiliationViewBean oldSuivi, String numCaisse) {
        if (JadeStringUtil.isEmpty(numCaisse)) {
            session.addError(session.getLabel("NUM_CAISSE_OBLIGATOIRE"));
        }
        if (JadeStringUtil.isEmpty(getIdCaisse(session, numCaisse))) {
            session.addError(session.getLabel("NUM_CAISSE_INTROUVABLE"));
        }
        controleDatesSuiviCaisses(session, oldSuivi);
        if (session.hasErrors()) {
            return false;
        } else {
            return true;
        }
    }

}
