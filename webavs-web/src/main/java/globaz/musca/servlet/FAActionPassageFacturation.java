package globaz.musca.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.*;
import globaz.musca.external.ServicesFacturation;
import globaz.musca.process.FAGenericProcess;
import globaz.musca.process.FAImpressionFactureEBillProcess;
import globaz.musca.process.FAImpressionFactureProcess;
import globaz.musca.process.FANewImpressionFactureProcess;
import globaz.musca.util.FAUtil;
import globaz.naos.db.controleEmployeur.AFImprimerDecisionViewBean;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.interets.CAApercuInteretMoratoireViewBean;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.db.interets.CAInteretMoratoireViewBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Iterator;

/**
 * Insérez la description du type ici. Date de création : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */
public class FAActionPassageFacturation extends FWDefaultServletAction {
    public final static String CLASSE_IMPLE_FACTURE_SANS_LSVREMB = "globaz.musca.api.musca.FAImpressionFactureSansLSVRemb";
    public final static String CLASSE_IMPLE_FACTURE_SANS_LSVREMB_EBILL = "globaz.musca.api.musca.FAImpressionFactureSansLSVRembEBill";
    public final static String CLASSE_IMPLE_FACTURE_STANDARD = "globaz.musca.api.musca.FAImpressionFacture_BVR";
    public final static String CLASSE_IMPLE_NEW_FACTURE_SANS_LSVREMB = "globaz.musca.api.musca.FANewImpressionFactureSansLSVRemb";
    public final static String CLASSE_IMPLE_NEW_FACTURE_STANDARD = "globaz.musca.api.musca.FANewImpressionFacture_BVR";
    public final static String CLASSE_IMPLE_FACTURE_EBILL = "globaz.musca.api.musca.FAImpressionFactureEBill";

    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public FAActionPassageFacturation(FWServlet servlet) {
        super(servlet);
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     */
    private void _actionAfactAQuittancer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";
        FAPassageViewBean viewBean = new FAPassageViewBean();
        try {
            viewBean.setSession((BSession) dispatcher.getSession());
            String passageId = request.getParameter("selectedId");
            viewBean.setIdPassage(passageId);
            viewBean.retrieve();
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
            JadeLogger.error(this, e);
        }

        // GESTION DES DROITS
        viewBean = (FAPassageViewBean) dispatcher.dispatch(viewBean, getAction());

        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);
        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        } else {
            _destination = getRelativeURLwithoutClassPart(request, session) + "afactAQuittancer_rc.jsp";
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Appeler la page _de.jsp de PassageFacturation Date de création : (06.03.2003 14:23:35)
     * 
     * @auteur: btc
     */
    private void _actionAnnuler(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWDispatcher mainDispatcher)
            throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {

            /*
             * Creation dynamique de notre viewBean
             */
            FAPassageGenererViewBean viewBean = new FAPassageGenererViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // mettre l'action générer
            // viewBean.setActionModulePassage(FAModulePassage.CS_ACTION_SUPPRIMER);

            // Id du passage passé en paramètre
            String selectedId = request.getParameter("selectedId");
            viewBean.setIdPassage(selectedId);

            viewBean.setSession((BSession) globaz.musca.translation.CodeSystem.getSession(session));

            // GESTION DES DROITS
            viewBean = (FAPassageGenererViewBean) mainDispatcher.dispatch(viewBean, getAction());

            // mettre en session le viewbean
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "Annuler_de.jsp";
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    /**
     * Appeler la page _de.jsp de PassageFacturation Date de création : (06.03.2003 14:23:35)
     * 
     * @auteur: btc
     */
    private void _actionComptabiliser(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {

            /*
             * Creation dynamique de notre viewBean
             */
            FAPassageGenererViewBean viewBean = new FAPassageGenererViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // Id du passage passé en paramètre
            String selectedId = request.getParameter("selectedId");
            viewBean.setIdPassage(selectedId);

            // Met l'action du viewBean à Comptabilisé comme l'action à
            // accomplir
            viewBean.setActionModulePassage(FAModulePassage.CS_ACTION_COMPTABILISE);
            viewBean.setSession((BSession) globaz.musca.translation.CodeSystem.getSession(session));

            // GESTION DES DROITS
            viewBean = (FAPassageGenererViewBean) mainDispatcher.dispatch(viewBean, getAction());

            // mettre en session le viewbean
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "Comptabiliser_de.jsp";
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    private void _actionExecuterSucces(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String _destination;
        _destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail";
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Appeler la page _de.jsp de PassageFacturation Date de création : (06.03.2003 14:23:35)
     * 
     * @auteur: btc
     */
    private void _actionGenerer(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWDispatcher mainDispatcher)
            throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {

            /*
             * Creation dynamique de notre viewBean
             */
            FAPassageGenererViewBean viewBean = new FAPassageGenererViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // Id du passage passé en paramètre
            String selectedId = request.getParameter("selectedId");
            viewBean.setIdPassage(selectedId);

            viewBean.setFromIdExterneRole(request.getParameter("fromIdExterneRole"));
            viewBean.setTillIdExterneRole(request.getParameter("tillIdExterneRole"));

            // Met l'action du viewBean à Generé comme l'action à accomplir
            viewBean.setActionModulePassage(FAModulePassage.CS_ACTION_GENERE);
            viewBean.setSession((BSession) globaz.musca.translation.CodeSystem.getSession(session));

            // GESTION DES DROITS
            viewBean = (FAPassageGenererViewBean) mainDispatcher.dispatch(viewBean, getAction());

            // mettre en session le viewbean
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "Generer_de.jsp";
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    /**
     * Appeler la page _de.jsp de PassageFacturation Date de création : (22.02.2005 10:52:41)
     * 
     * @auteur: rri
     */
    private void _actionImprimeBVR(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        BTransaction transaction = null;
        try {

            /*
             * Creation dynamique de notre viewBean
             */
            FAPassageModuleFacturationViewBean viewBean = new FAPassageModuleFacturationViewBean("");
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            // Mettre la session
            viewBean.setSession((BSession) globaz.musca.translation.CodeSystem.getSession(session));
            // Id du passage passé en paramètre
            String selectedId = request.getParameter("selectedId");
            viewBean.setIdPassage(selectedId);
            // Créer une transaction
            transaction = new BTransaction(viewBean.getSession());
            transaction.openTransaction();

            // Contrôle si eBill est actif
            boolean isEBillActive = CAApplication.getApplicationOsiris().getCAParametres().isEbill(viewBean.getSession());
            if (isEBillActive) {
                FAModulePassageListViewBean vBean = new FAModulePassageListViewBean();
                vBean.setSession((BSession) globaz.musca.translation.CodeSystem.getSession(session));
                vBean.setForIdTypeModule(FAModuleFacturation.CS_MODULE_BULLETINS_SOLDES_EBILL);
                vBean.setForIdPassage(viewBean.getIdPassage());
                vBean.find(BManager.SIZE_NOLIMIT);
                // Contrôle si le plan de facturation contient un module de type "Module_Bulletin_De_Soldes_EBill"
                if (vBean.size() == 1) {
                    viewBean.setIdModuleFact(ServicesFacturation.getIdModFacturationByType(viewBean.getSession(), transaction,
                        FAModuleFacturation.CS_MODULE_BULLETINS_SOLDES_EBILL));
                } else {
                    viewBean.setIdModuleFact(ServicesFacturation.getIdModFacturationByType(viewBean.getSession(), transaction,
                        FAModuleFacturation.CS_MODULE_BULLETINS_SOLDES));
                }
            } else {
            viewBean.setIdModuleFact(ServicesFacturation.getIdModFacturationByType(viewBean.getSession(), transaction,
                        FAModuleFacturation.CS_MODULE_BULLETINS_SOLDES));
            }
            viewBean.setFromIdExterneRole(request.getParameter("fromIdExterneRole"));
            viewBean.setTillIdExterneRole(request.getParameter("tillIdExterneRole"));

            // mettre l'action générer
            viewBean.setActionModulePassage(FAModulePassage.CS_ACTION_COMPTABILISE);
            viewBean.setSession((BSession) globaz.musca.translation.CodeSystem.getSession(session));

            // GESTION DES DROITS
            viewBean = (FAPassageModuleFacturationViewBean) mainDispatcher.dispatch(viewBean, getAction());

            // mettre en session le viewbean
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "ModuleFact_de.jsp";
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        } finally {
            if (transaction != null) {
                try {
                    transaction.closeTransaction();
                } catch (Exception e1) {
                    JadeLogger.error(this, e1);
                }
            }
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Appeler la page _de.jsp de PassageFacturation Date de création : (22.02.2005 10:52:41)
     * 
     * @auteur: rri
     */
    private void _actionImprimeDecisionControle(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        BTransaction transaction = null;
        try {

            /*
             * Creation dynamique de notre viewBean avec l'id du Module à Imprimer. Faire attention au cas où le module
             * disparait ou est remplacé par un autre. Changer le n° d'Id ici v
             */
            AFImprimerDecisionViewBean viewBean = new AFImprimerDecisionViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // Id du passage passé en paramètre
            String selectedId = request.getParameter("selectedId");
            viewBean.setIdPassage(selectedId);

            // mettre l'action générer
            viewBean.setSession((BSession) globaz.musca.translation.CodeSystem.getSession(session));

            // Créer une transaction
            transaction = new BTransaction(viewBean.getSession());
            transaction.openTransaction();

            // GESTION DES DROITS
            viewBean = (AFImprimerDecisionViewBean) mainDispatcher.dispatch(viewBean, getAction());

            // mettre en session le viewbean
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "ListerDecisionControle_de.jsp";
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        } finally {
            if (transaction != null) {
                try {
                    transaction.closeTransaction();
                } catch (Exception e1) {
                    JadeLogger.error(this, e1);
                }
            }
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Appeler la page _de.jsp de PassageFacturation Date de création : (22.02.2005 10:52:41)
     * 
     * @auteur: rri
     */
    private void _actionImprimeDecisionIM(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        BTransaction transaction = null;
        try {

            /*
             * Creation dynamique de notre viewBean avec l'id du Module à Imprimer. Faire attention au cas où le module
             * disparait ou est remplacé par un autre. Changer le n° d'Id ici v
             */
            FAPassageModuleFacturationViewBean viewBean = new FAPassageModuleFacturationViewBean("");
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // GESTION DES DROITS
            viewBean = (FAPassageModuleFacturationViewBean) mainDispatcher.dispatch(viewBean, getAction());
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                // On cherche la décision d'IM pour passer l'Id du passage et de
                // l'entete de facture en paramètre
                String selectedId = request.getParameter("selectedId");
                CAInteretMoratoire interetMoratoire = new CAInteretMoratoire();
                BSession sessionMusca = (BSession) mainDispatcher.getSession();
                FAApplication muscaApli = (FAApplication) sessionMusca.getApplication();
                BSession sessionOsiris = (BSession) muscaApli.getSessionOsiris(sessionMusca);

                interetMoratoire.setSession(sessionOsiris);
                interetMoratoire.setIdInteretMoratoire(selectedId);
                interetMoratoire.retrieve();

                // Créer une transaction
                transaction = new BTransaction(sessionMusca);
                transaction.openTransaction();

                viewBean.setIdModuleFact(ServicesFacturation.getIdModFacturationByType(sessionMusca, transaction,
                        FAModuleFacturation.CS_MODULE_PRINT_DECISIONMORATOIRE));

                // choix destination: si idSectionFacture ou idJournalPassage
                // n'est pas renseigné sur l'IM, on redirige vers la page
                // précedante avec un méssage
                if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                    _destination = FWDefaultServletAction.ERROR_PAGE;
                } else if (JadeStringUtil.isIntegerEmpty(interetMoratoire.getIdSectionFacture())
                        || JadeStringUtil.isIntegerEmpty(interetMoratoire.getIdJournalFacturation())) { // si
                    // idPassage
                    // ou
                    // idEnteteFacture
                    // ne
                    // sont
                    // pas
                    // renseigné
                    // dans
                    // la
                    // décision
                    // (dans
                    // Osiris)
                    // on
                    // ne
                    // peux
                    // pas
                    // imprimer
                    // la
                    // décision
                    FWViewBeanInterface vb = (FWViewBeanInterface) session.getAttribute("viewBean");
                    FWViewBeanInterface element = (FWViewBeanInterface) session.getAttribute("element");
                    // Affiche un message d'erreur dans l'écran
                    if (element instanceof CAApercuInteretMoratoireViewBean) {
                        request.setAttribute("message", sessionMusca.getLabel("ERROR_NO_IDPASSAGE"));
                        _destination = "/osiris?userAction=osiris.interets.gestionInterets.chercher";
                    } else if (vb instanceof CAInteretMoratoireViewBean) {
                        vb.setMsgType(FWViewBeanInterface.ERROR);
                        vb.setMessage(sessionMusca.getLabel("ERROR_NO_IDPASSAGE"));
                        _destination = "/osiris?userAction=osiris.interets.interetMoratoire.afficherDecisionWithError";
                    } else {
                        _destination = FWDefaultServletAction.ERROR_PAGE;
                    }
                } else {
                    // selectedId
                    viewBean.setIdPassage(interetMoratoire.getIdJournalFacturation());
                    viewBean.setIdEnteteFacture(interetMoratoire.getIdSectionFacture());

                    viewBean.setFromIdExterneRole(request.getParameter("fromIdExterneRole"));
                    viewBean.setTillIdExterneRole(request.getParameter("tillIdExterneRole"));

                    // mettre l'action générer
                    viewBean.setActionModulePassage(FAModulePassage.CS_ACTION_IMPRIMER);
                    viewBean.setSession(sessionMusca);

                    setSessionAttribute(session, "viewBean", viewBean);

                    _destination = getRelativeURL(request, session) + "ModuleFactDecompte_de.jsp";
                }
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        } finally {
            if (transaction != null) {
                try {
                    transaction.closeTransaction();
                } catch (Exception e1) {
                    JadeLogger.error(this, e1);
                }
            }
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Appeler la page _de.jsp de PassageFacturation Date de création : (22.02.2005 10:52:41)
     * 
     * @auteur: rri
     */
    private void _actionImprimeIntMoratoire(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        BTransaction transaction = null;
        try {

            /*
             * Creation dynamique de notre viewBean avec l'id du Module à Imprimer. Faire attention au cas où le module
             * disparait ou est remplacé par un autre. Changer le n° d'Id ici v
             */
            FAPassageModuleFacturationViewBean viewBean = new FAPassageModuleFacturationViewBean("");
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // Id du passage passé en paramètre
            String selectedId = request.getParameter("selectedId");
            viewBean.setIdPassage(selectedId);

            viewBean.setFromIdExterneRole(request.getParameter("fromIdExterneRole"));
            viewBean.setTillIdExterneRole(request.getParameter("tillIdExterneRole"));

            // mettre l'action générer
            // viewBean.setActionModulePassage(FAModulePassage.CS_ACTION_IMPRIMER);
            viewBean.setActionModulePassage(FAModulePassage.CS_ACTION_COMPTABILISE);
            viewBean.setSession((BSession) globaz.musca.translation.CodeSystem.getSession(session));

            // Créer une transaction
            transaction = new BTransaction(viewBean.getSession());
            transaction.openTransaction();

            viewBean.setIdModuleFact(ServicesFacturation.getIdModFacturationByType(viewBean.getSession(), transaction,
                    FAModuleFacturation.CS_MODULE_PRINT_DECISIONMORATOIRE));

            // GESTION DES DROITS
            viewBean = (FAPassageModuleFacturationViewBean) mainDispatcher.dispatch(viewBean, getAction());

            // mettre en session le viewbean
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "ModuleFact_de.jsp";
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        } finally {
            if (transaction != null) {
                try {
                    transaction.closeTransaction();
                } catch (Exception e1) {
                    JadeLogger.error(this, e1);
                }
            }
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    private void _actionImprimeLettreTaxeCo2(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        BTransaction transaction = null;
        try {

            /*
             * Creation dynamique de notre viewBean
             */
            FAPassageModuleFacturationViewBean viewBean = new FAPassageModuleFacturationViewBean("");
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            // Mettre la session
            viewBean.setSession((BSession) globaz.musca.translation.CodeSystem.getSession(session));
            // Id du passage passé en paramètre
            String selectedId = request.getParameter("selectedId");
            viewBean.setIdPassage(selectedId);
            // Créer une transaction
            transaction = new BTransaction(viewBean.getSession());
            transaction.openTransaction();

            viewBean.setIdModuleFact(ServicesFacturation.getIdModFacturationByType(viewBean.getSession(), transaction,
                    FAModuleFacturation.CS_MODULE_LETTRE_TAXE_CO2));

            viewBean.setFromIdExterneRole(request.getParameter("fromIdExterneRole"));
            viewBean.setTillIdExterneRole(request.getParameter("tillIdExterneRole"));

            // mettre l'action générer
            viewBean.setActionModulePassage(FAModulePassage.CS_ACTION_COMPTABILISE);
            viewBean.setSession((BSession) globaz.musca.translation.CodeSystem.getSession(session));

            // GESTION DES DROITS
            viewBean = (FAPassageModuleFacturationViewBean) mainDispatcher.dispatch(viewBean, getAction());

            // mettre en session le viewbean
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "LettreTaxeCO2_de.jsp";
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        } finally {
            if (transaction != null) {
                try {
                    transaction.closeTransaction();
                } catch (Exception e1) {
                    JadeLogger.error(this, e1);
                }
            }
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    /**
     * Appeler la page _de.jsp de PassageFacturation Date de création : (06.03.2003 14:23:35)
     * 
     * @auteur: btc
     */
    private void _actionImprimer(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWDispatcher mainDispatcher)
            throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {

            /*
             * Creation dynamique de notre viewBean
             */
            FAPassageGenererViewBean viewBean = new FAPassageGenererViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // Id du passage passé en paramètre
            String selectedId = request.getParameter("selectedId");
            viewBean.setIdPassage(selectedId);

            viewBean.setSession((BSession) globaz.musca.translation.CodeSystem.getSession(session));

            // GESTION DES DROITS
            viewBean = (FAPassageGenererViewBean) mainDispatcher.dispatch(viewBean, getAction());

            // mettre en session le viewbean
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "Imprimer_de.jsp";
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    /**
     * Appeler la page _de.jsp de PassageFacturation Date de création : (06.03.2003 14:23:35)
     * 
     * @auteur: btc
     */
    private void _actionImprimerDecomptes(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {
            BSession NewSession = (BSession) globaz.musca.translation.CodeSystem.getSession(session);

            // Id du passage passé en paramètre
            String selectedId = request.getParameter("selectedId");
            String classeImplementation = "";

            boolean oldSystem = false;
            boolean newSystem = false;
            boolean eBillSystem = false;

            FAModulePassageManager moduleMana = new FAModulePassageManager();
            moduleMana.setSession(NewSession);
            moduleMana.setForIdPassage(selectedId);
            moduleMana.setForIdTypeModule(FAModuleFacturation.CS_MODULE_IMPRESSION);
            moduleMana.find();
            if (moduleMana.size() == 1) {
                FAModulePassage module = (FAModulePassage) moduleMana.getFirstEntity();
                classeImplementation = module.getNomClasse();
                /*
                 * Creation dynamique de notre viewBean
                 */
                FAGenericProcess viewBean = null;
                String libellePassage = FAUtil.getLibellePassage(selectedId, session);
                if (FAActionPassageFacturation.CLASSE_IMPLE_FACTURE_STANDARD.equals(classeImplementation)) {
                    viewBean = new FAPassageImprimerDecomptesViewBean();
                    oldSystem = true;
                }
                if (FAActionPassageFacturation.CLASSE_IMPLE_FACTURE_SANS_LSVREMB.equals(classeImplementation)) {
                    viewBean = new FAPassageImprimerDecomptesSansLSVRembViewBean();
                    oldSystem = true;
                }
                if (FAActionPassageFacturation.CLASSE_IMPLE_FACTURE_SANS_LSVREMB_EBILL.equals(classeImplementation)) {
                    viewBean = new FAPassageImprimerFactureEBillSansLSVRembViewBean();
                    eBillSystem = true;
                }
                if (FAActionPassageFacturation.CLASSE_IMPLE_NEW_FACTURE_STANDARD.equals(classeImplementation)) {
                    viewBean = new FANewPassageImprimerDecomptesViewBean();
                    newSystem = true;
                }
                if (FAActionPassageFacturation.CLASSE_IMPLE_NEW_FACTURE_SANS_LSVREMB.equals(classeImplementation)) {
                    viewBean = new FANewPassageImprimerDecomptesSansLSVRembViewBean();
                    newSystem = true;
                }
                if (FAActionPassageFacturation.CLASSE_IMPLE_FACTURE_EBILL.equals(classeImplementation)) {
                    viewBean = new FAPassageImprimerFactureEBillViewBean();
                    eBillSystem = true;
                }

                if (oldSystem) {
                    ((FAImpressionFactureProcess) viewBean).setLibelle(libellePassage);
                }
                if (newSystem) {
                    ((FANewImpressionFactureProcess) viewBean).setLibelle(libellePassage);
                }
                if (eBillSystem) {
                    ((FAImpressionFactureEBillProcess) viewBean).setLibelle(libellePassage);
                }

                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

                viewBean.setIdPassage(selectedId);
                viewBean.setSession(NewSession);

                // GESTION DES DROITS
                if (oldSystem) {
                    viewBean = (FAImpressionFactureProcess) mainDispatcher.dispatch(viewBean, getAction());
                }
                if (newSystem) {
                    viewBean = (FANewImpressionFactureProcess) mainDispatcher.dispatch(viewBean, getAction());
                }
                if (eBillSystem) {
                    viewBean = (FAImpressionFactureEBillProcess) mainDispatcher.dispatch(viewBean, getAction());
                }

                // mettre en session le viewbean
                session.removeAttribute("viewBean");
                session.setAttribute("viewBean", viewBean);

                /*
                 * choix destination
                 */
                if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                    _destination = FWDefaultServletAction.ERROR_PAGE;
                } else {
                    if (oldSystem) {
                        _destination = getRelativeURL(request, session) + "ImprimerDecomptes_de.jsp";
                    } else if (newSystem){
                        _destination = getRelativeURL(request, session) + "NewImprimerDecomptes_de.jsp";
                    } else if (eBillSystem) {
                        _destination = getRelativeURL(request, session) + "ImprimerDecomptesEBill_de.jsp";
                    }

                }
            } else {
                if (moduleMana.size() == 2) {
                    boolean oldSys = false;
                    boolean newSys = false;
                    boolean sysEBill = false;
                    String oldClasse = "";
                    String newClasse = "";
                    String eBillClasse = "";

                    for (Iterator iterator = moduleMana.iterator(); iterator.hasNext();) {
                        FAModulePassage mod = (FAModulePassage) iterator.next();
                        String nomClasse = mod.getNomClasse();

                        if (FAActionPassageFacturation.CLASSE_IMPLE_FACTURE_STANDARD.equals(nomClasse)
                                || FAActionPassageFacturation.CLASSE_IMPLE_FACTURE_SANS_LSVREMB.equals(nomClasse)) {
                            oldSys = true;
                            oldClasse = nomClasse;
                        } else {
                            newSys = true;
                            oldSys = false;
                            oldClasse = "";
                            newClasse = nomClasse;
                            break;
                        }
                    }

                    if (newSys) {
                        FAGenericProcess viewBean = null;
                        String libellePassage = FAUtil.getLibellePassage(selectedId, session);
                        if (FAActionPassageFacturation.CLASSE_IMPLE_NEW_FACTURE_STANDARD.equals(newClasse)) {
                            viewBean = new FANewPassageImprimerDecomptesViewBean();
                        }
                        if (FAActionPassageFacturation.CLASSE_IMPLE_NEW_FACTURE_SANS_LSVREMB.equals(newClasse)) {
                            viewBean = new FANewPassageImprimerDecomptesSansLSVRembViewBean();
                        }

                        ((FANewImpressionFactureProcess) viewBean).setLibelle(libellePassage);

                        globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

                        viewBean.setIdPassage(selectedId);
                        viewBean.setSession(NewSession);

                        // GESTION DES DROITS
                        viewBean = (FANewImpressionFactureProcess) mainDispatcher.dispatch(viewBean, getAction());

                        // mettre en session le viewbean
                        session.removeAttribute("viewBean");
                        session.setAttribute("viewBean", viewBean);

                        /*
                         * choix destination
                         */
                        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                            _destination = FWDefaultServletAction.ERROR_PAGE;
                        } else {
                            _destination = getRelativeURL(request, session) + "NewImprimerDecomptes_de.jsp";
                        }
                    }

                    if (oldSys) {
                        FAGenericProcess viewBean = null;
                        String libellePassage = FAUtil.getLibellePassage(selectedId, session);
                        if (FAActionPassageFacturation.CLASSE_IMPLE_FACTURE_STANDARD.equals(oldClasse)) {
                            viewBean = new FAPassageImprimerDecomptesViewBean();
                            oldSystem = true;
                        }
                        if (FAActionPassageFacturation.CLASSE_IMPLE_FACTURE_SANS_LSVREMB.equals(oldClasse)) {
                            viewBean = new FAPassageImprimerDecomptesSansLSVRembViewBean();
                        }

                        ((FAImpressionFactureProcess) viewBean).setLibelle(libellePassage);

                        globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

                        viewBean.setIdPassage(selectedId);
                        viewBean.setSession(NewSession);

                        // GESTION DES DROITS
                        viewBean = (FAImpressionFactureProcess) mainDispatcher.dispatch(viewBean, getAction());

                        // mettre en session le viewbean
                        session.removeAttribute("viewBean");
                        session.setAttribute("viewBean", viewBean);

                        /*
                         * choix destination
                         */
                        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                            _destination = FWDefaultServletAction.ERROR_PAGE;
                        } else {
                            _destination = getRelativeURL(request, session) + "ImprimerDecomptes_de.jsp";
                        }
                    }

                    if (sysEBill) {
                        FAGenericProcess viewBean = null;
                        String libellePassage = FAUtil.getLibellePassage(selectedId, session);
                        if (FAActionPassageFacturation.CLASSE_IMPLE_FACTURE_STANDARD.equals(oldClasse)) {
                            viewBean = new FAPassageImprimerDecomptesViewBean();
                            oldSystem = true;
                        }
                        if (FAActionPassageFacturation.CLASSE_IMPLE_FACTURE_SANS_LSVREMB.equals(oldClasse)) {
                            viewBean = new FAPassageImprimerDecomptesSansLSVRembViewBean();
                        }

                        ((FAImpressionFactureProcess) viewBean).setLibelle(libellePassage);

                        globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

                        viewBean.setIdPassage(selectedId);
                        viewBean.setSession(NewSession);

                        // GESTION DES DROITS
                        viewBean = (FAImpressionFactureProcess) mainDispatcher.dispatch(viewBean, getAction());

                        // mettre en session le viewbean
                        session.removeAttribute("viewBean");
                        session.setAttribute("viewBean", viewBean);

                        /*
                         * choix destination
                         */
                        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                            _destination = FWDefaultServletAction.ERROR_PAGE;
                        } else {
                            _destination = getRelativeURL(request, session) + "ImprimerDecomptes_de.jsp";
                        }
                    }

                } else {
                    _destination = FWDefaultServletAction.ERROR_PAGE;
                }

            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    private void _actionImprimerLettreRentier(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {

            /*
             * Creation dynamique de notre viewBean
             */
            FAPassageImprimerLettreRentierViewBean viewBean = new FAPassageImprimerLettreRentierViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // Id du passage passé en paramètre
            String selectedId = request.getParameter("selectedId");
            viewBean.setIdPassage(selectedId);
            viewBean.setSession((BSession) globaz.musca.translation.CodeSystem.getSession(session));

            // GESTION DES DROITS
            viewBean = (FAPassageImprimerLettreRentierViewBean) mainDispatcher.dispatch(viewBean, getAction());

            // mettre en session le viewbean
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "ImprimerLettreRentier_de.jsp";
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    // Appel de la première fenêtre (générique) pour l'appel des autres listes//
    private void _actionLister(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWDispatcher mainDispatcher)
            throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";

        _destination = getRelativeURL(request, session) + "Listes_de.jsp";

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    protected void _actionListerAfactAQuittancer(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _destination = "";
        try {

            /*
             * Creation dynamique de notre viewBean
             */
            FAPassageListerAfactsAQuittancerViewBean viewBean = new FAPassageListerAfactsAQuittancerViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // Id du passage passé en paramètre
            String selectedId = request.getParameter("selectedId");
            viewBean.setIdPassage(selectedId);
            viewBean.setSession((BSession) globaz.musca.translation.CodeSystem.getSession(session));

            // GESTION DES DROITS
            viewBean = (FAPassageListerAfactsAQuittancerViewBean) mainDispatcher.dispatch(viewBean, getAction());

            // mettre en session le viewbean
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "ListerAfactAQuittancer_de.jsp";
            }

        } catch (Exception e) {

            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Appeler la page _de.jsp de PassageFacturation Date de création : (06.03.2003 14:23:35)
     * 
     * @auteur: btc
     */
    private void _actionListerAfacts(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {

            /*
             * Creation dynamique de notre viewBean
             */
            FAPassageListerAfactsViewBean viewBean = new FAPassageListerAfactsViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // Id du passage passé en paramètre
            String selectedId = request.getParameter("selectedId");
            viewBean.setIdPassage(selectedId);
            viewBean.setSession((BSession) globaz.musca.translation.CodeSystem.getSession(session));

            // GESTION DES DROITS
            viewBean = (FAPassageListerAfactsViewBean) mainDispatcher.dispatch(viewBean, getAction());

            // mettre en session le viewbean
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "ListerAfacts_de.jsp";
            }

        } catch (Exception e) {

            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    /**
     * Appeler la page _de.jsp de PassageFacturation Date de création : (06.03.2003 14:23:35)
     * 
     * @auteur: btc
     */
    private void _actionListerCompensations(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {

            /*
             * Creation dynamique de notre viewBean
             */
            FAPassageListerCompensationViewBean viewBean = new FAPassageListerCompensationViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // Id du passage passé en paramètre
            String selectedId = request.getParameter("selectedId");
            viewBean.setIdPassage(selectedId);
            viewBean.setSession((BSession) globaz.musca.translation.CodeSystem.getSession(session));

            // GESTION DES DROITS
            viewBean = (FAPassageListerCompensationViewBean) mainDispatcher.dispatch(viewBean, getAction());

            // mettre en session le viewbean
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "ListerCompensations_de.jsp";
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    /**
     * Appeler la page _de.jsp de PassageFacturation Date de création : (06.03.2003 14:23:35)
     * 
     * @auteur: btc
     */
    private void _actionListerDecomptes(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {

            /*
             * Creation dynamique de notre viewBean
             */
            FAPassageListerDecomptesViewBean viewBean = new FAPassageListerDecomptesViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // Id du passage passé en paramètre
            String selectedId = request.getParameter("selectedId");
            viewBean.setIdPassage(selectedId);
            viewBean.setSession((BSession) globaz.musca.translation.CodeSystem.getSession(session));

            // GESTION DES DROITS
            viewBean = (FAPassageListerDecomptesViewBean) mainDispatcher.dispatch(viewBean, getAction());

            // mettre en session le viewbean
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "ListerDecomptes_de.jsp";
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    private void _actionListerTaxation(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {

            /*
             * Creation dynamique de notre viewBean
             */
            FAPassageListerTaxationViewBean viewBean = new FAPassageListerTaxationViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // Id du passage passé en paramètre
            String selectedId = request.getParameter("selectedId");
            viewBean.setNoPassage(selectedId);
            viewBean.setSession((BSession) globaz.musca.translation.CodeSystem.getSession(session));

            // GESTION DES DROITS
            viewBean = (FAPassageListerTaxationViewBean) mainDispatcher.dispatch(viewBean, getAction());

            // mettre en session le viewbean
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "ListerTaxation_de.jsp";
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    private void _actionListerIndeRevenuMin(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {

            /*
             * Creation dynamique de notre viewBean
             */
            FAPassageListerIndeRevenuMinViewBean viewBean = new FAPassageListerIndeRevenuMinViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // Récupération du passage pour set les infos utiles à la liste
            String selectedId = request.getParameter("selectedId");
            viewBean.setNoPassage(selectedId);
            viewBean.setSession((BSession) globaz.musca.translation.CodeSystem.getSession(session));

            // GESTION DES DROITS
            viewBean = (FAPassageListerIndeRevenuMinViewBean) mainDispatcher.dispatch(viewBean, getAction());

            // mettre en session le viewbean
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "ListerIndeRevenuMin_de.jsp";
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {

        if (getAction().getActionPart().equals("annuler")) {
            // chercher avec chargement des données nécessaire
            _actionAnnuler(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("comptabiliser")) {
            // chercher avec chargement des données nécessaire
            _actionComptabiliser(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("generer")) {
            // chercher avec chargement des données nécessaire
            _actionGenerer(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("imprimer")) {
            // chercher avec chargement des données nécessaire
            _actionImprimer(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("imprimerDecomptes")) {
            // chercher avec chargement des données nécessaire
            _actionImprimerDecomptes(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("listerAfacts")) {
            // chercher avec chargement des données nécessaire
            _actionListerAfacts(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("listerCompensations")) {
            // chercher avec chargement des données nécessaire
            _actionListerCompensations(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("listerDecomptes")) {
            // chercher avec chargement des données nécessaire
            _actionListerDecomptes(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("listerTaxation")) {
            // chercher avec chargement des données nécessaire
            _actionListerTaxation(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("listes")) {
            // chercher avec chargement des données nécessaire
            _actionLister(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("genererSoldeBVR")) {
            // chercher avec chargement des données nécessaire
            _actionImprimeBVR(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("genererIntMoratoire")) {
            // chercher avec chargement des données nécessaire
            _actionImprimeIntMoratoire(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("genererLettreTaxeCo2")) {
            // chercher avec chargement des données nécessaire
            _actionImprimeLettreTaxeCo2(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("listerDecisionControle")) {
            // chercher avec chargement des données nécessaire
            _actionImprimeDecisionControle(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("imprimerLettreRentier")) {
            // chercher avec chargement des données nécessaire
            _actionImprimerLettreRentier(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("imprimerDecisionIM")) {
            // chercher avec chargement des données nécessaire
            _actionImprimeDecisionIM(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("aQuittancer")) {
            // chercher avec chargement des données nécessaire
            _actionAfactAQuittancer(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("listerAfactAQuittancer")) {
            _actionListerAfactAQuittancer(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("executerSucces")) {
            _actionExecuterSucces(session, request, response);
        } else if (getAction().getActionPart().equals("listerIndeRevenuMinimal")) {
            _actionListerIndeRevenuMin(session, request, response, dispatcher);
        }
    }

}
