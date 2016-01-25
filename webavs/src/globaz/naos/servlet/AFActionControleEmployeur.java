/*
 * Created on 13-Jan-05
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.FWSmartUrl;
import globaz.framework.utils.urls.FWUrl;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.controleEmployeur.AFControleEmployeur;
import globaz.naos.db.controleEmployeur.AFControleEmployeurListViewBean;
import globaz.naos.db.controleEmployeur.AFControleEmployeurViewBean;
import globaz.naos.db.controleEmployeur.AFImprimerControleViewBean;
import globaz.naos.db.controleEmployeur.AFImprimerLettreLibreViewBean;
import globaz.naos.db.controleEmployeur.AFLettreProchainControleViewBean;
import globaz.naos.db.controleEmployeur.AFSaisieRapideReviseurViewBean;
import globaz.naos.translation.CodeSystem;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour l'entité Control Employeur.
 * 
 * @author sau
 */
public class AFActionControleEmployeur extends AFDefaultActionChercher {

    /**
     * Constructeur d'AFActionControleEmployeur.
     * 
     * @param servlet
     */
    public AFActionControleEmployeur(FWServlet servlet) {
        super(servlet);
    }

    private void _actionImprimerControle(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {

            /*
             * Creation dynamique de notre viewBean
             */
            AFImprimerControleViewBean viewBean = new AFImprimerControleViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // Id du passage passé en paramètre
            String selectedId = request.getParameter("selectedId");
            viewBean.setControleId(selectedId);
            viewBean.setSession((BSession) CodeSystem.getSession(session));

            // mettre en session le viewbean
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURLwithoutClassPart(request, session) + "imprimerControle_de.jsp";
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

    private void _actionImprimerlettrelibre(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {

            /*
             * Creation dynamique de notre viewBean
             */
            AFImprimerLettreLibreViewBean viewBean = new AFImprimerLettreLibreViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // Id du passage passé en paramètre
            String selectedId = request.getParameter("selectedId");
            viewBean.setControleId(selectedId);
            viewBean.setSession((BSession) CodeSystem.getSession(session));

            // mettre en session le viewbean
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURLwithoutClassPart(request, session) + "imprimerlettrelibre_de.jsp";
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

    private void _actionLettreProchainControle(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {

            /*
             * Creation dynamique de notre viewBean
             */
            AFLettreProchainControleViewBean viewBean = new AFLettreProchainControleViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // Id du passage passé en paramètre
            String selectedId = request.getParameter("selectedId");
            viewBean.setIdControle(selectedId);
            viewBean.setSession((BSession) CodeSystem.getSession(session));

            // mettre en session le viewbean
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURLwithoutClassPart(request, session) + "lettreProchainControle_de.jsp";
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

    private void _actionOuvrirPage(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {
            _destination = getRelativeURL(request, session) + "saisieRapideReviseur_de.jsp";

        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    private void _actionSaisieRapide(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {

            /*
             * Creation dynamique de notre viewBean
             */
            AFSaisieRapideReviseurViewBean viewBean = new AFSaisieRapideReviseurViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // Id du passage passé en paramètre
            String numAffilie = request.getParameter("numAffilie");
            String annee = request.getParameter("annee");
            String controleurVisa = request.getParameter("controleurVisa");
            viewBean.setSession((BSession) CodeSystem.getSession(session));
            viewBean.miseAJourControle(numAffilie, annee, controleurVisa);

            // mettre en session le viewbean
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            /*
             * choix destination
             */
            _destination = getRelativeURLwithoutClassPart(request, session) + "saisieRapideReviseur_de.jsp";

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
     * Action utilisée pour la recherche.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        AFControleEmployeurViewBean vBean = new AFControleEmployeurViewBean();
        vBean.setAffiliationId(request.getParameter("affiliationId"));

        this.actionChercher(vBean, session, request, response, mainDispatcher);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {

        if (getAction().getActionPart().equals("imprimerControle")) {
            // chercher avec chargement des données nécessaire
            _actionImprimerControle(session, request, response, dispatcher);
        }
        if (getAction().getActionPart().equals("imprimerlettrelibre")) {
            // chercher avec chargement des données nécessaire
            _actionImprimerlettrelibre(session, request, response, dispatcher);
        }

        if (getAction().getActionPart().equals("saisieRapideReviseur")) {
            // chercher avec chargement des données nécessaire
            _actionSaisieRapide(session, request, response, dispatcher);
        }
        if (getAction().getActionPart().equals("saisieRapideReviseur.ouvrir")) {
            // chercher avec chargement des données nécessaire
            _actionOuvrirPage(session, request, response, dispatcher);
        }
        if (getAction().getActionPart().equals("lettreProchainControle")) {
            _actionLettreProchainControle(session, request, response, dispatcher);
        }
        // if (getAction().getActionPart().equals("statOFASControle")){
        // _actionStatOFASControle(session, request, response, dispatcher);
        // }
    }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _destination = "";
        try {
            AFControleEmployeurListViewBean listViewBean = new AFControleEmployeurListViewBean();
            if (!JadeStringUtil.isEmpty(request.getParameter("forNumAffilie"))) {
                listViewBean.setForNumAffilie(request.getParameter("forNumAffilie"));
            }
            if (!JadeStringUtil.isEmpty(request.getParameter("forAnnee"))) {
                listViewBean.setForAnnee(request.getParameter("forAnnee"));
            }
            if (!JadeStringUtil.isEmpty(request.getParameter("likeNouveauNumRapport"))) {
                listViewBean.setLikeNouveauNumRapport(request.getParameter("likeNouveauNumRapport"));
            }
            listViewBean.setISession(mainDispatcher.getSession());
            listViewBean.setOrderBy("MDDPRE");
            listViewBean.find();
            if (!JadeStringUtil.isEmpty(request.getParameter("forNumAffilie"))) {
                listViewBean.setProchainControle(calculProchainControle(mainDispatcher.getSession(), listViewBean));
            }
            request.setAttribute("viewBean", listViewBean);
            session.setAttribute("listViewBean", listViewBean);
            _destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    // private void _actionStatOFASControle(javax.servlet.http.HttpSession
    // session, javax.servlet.http.HttpServletRequest request,
    // javax.servlet.http.HttpServletResponse response,
    // globaz.framework.controller.FWDispatcher mainDispatcher) throws
    // javax.servlet.ServletException, java.io.IOException {
    // String _destination = "";
    // try {
    //
    // /*
    // * Creation dynamique de notre viewBean
    // */
    // AFStatOFASControleViewBean viewBean = new AFStatOFASControleViewBean();
    // globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
    //
    // viewBean.setSession((BSession) CodeSystem.getSession(session));
    //
    // // mettre en session le viewbean
    // session.removeAttribute("viewBean");
    // session.setAttribute("viewBean", viewBean);
    //
    // /*
    // * choix destination
    // */
    // if (viewBean.getMsgType().equals(FWViewBean.ERROR)) {
    // _destination = ERROR_PAGE;
    // } else {
    // _destination = getRelativeURLwithoutClassPart(request, session) +
    // "statOFASControle_de.jsp";
    // }
    //
    // } catch (Exception e) {
    // JadeLogger.error(this, e);
    // _destination = ERROR_PAGE;
    // }
    //
    // /*
    // * redirection vers la destination
    // */
    // servlet.getServletContext().getRequestDispatcher(_destination).forward(request,
    // response);
    //
    // }

    @Override
    protected String addParametersFrom(HttpServletRequest request, String url) throws UnsupportedEncodingException {
        FWSmartUrl sUrl = new FWSmartUrl(new FWUrl(request));
        if ("naos.controleEmployeur.controleEmployeur.modifier".equals(sUrl.getUserAction())) {
            return url;
        } else {
            return super.addParametersFrom(request, url);
        }
    }

    /**
     * Effectue des traitements avant la saisie d'une nouvelle entité.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeNouveau(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        AFControleEmployeurViewBean vBean = (AFControleEmployeurViewBean) viewBean;
        vBean.setAffiliationId(request.getParameter("affiliationId"));
        vBean.setDateDebutControle(request.getParameter("dateDebutControle"));

        return vBean;
    }

    private String calculProchainControle(BISession iSession, AFControleEmployeurListViewBean listViewBean) {
        BSession session = (BSession) iSession;
        int prochainControle;
        int anneeRadiation = 0;
        // On va rechercher la propriété de la périodicité du contrôle par
        // défaut
        int periodicite;
        try {
            periodicite = Integer.parseInt(session.getApplication().getProperty(
                    AFApplication.PERIODICITECONTROLEEMPLOYEURCAISSE));
        } catch (Exception e) {
            periodicite = 4;
        }
        // On contrôle le numéro d'affilié
        try {
            AFAffiliationManager affManager = new AFAffiliationManager();
            affManager.setISession(session);
            affManager.setForAffilieNumero(listViewBean.getForNumAffilie());
            affManager.setForTypesAffParitaires();
            affManager.find();
            if (affManager.size() > 0) {
                AFAffiliation affiliation = (AFAffiliation) affManager.getFirstEntity();
                // On calcul la valeur du prochain contrôle
                if (listViewBean.size() == 0) {
                    // La liste est vide
                    prochainControle = Integer.valueOf(affiliation.getDateDebut().substring(6, 10)).intValue()
                            + periodicite;
                    if (!JadeStringUtil.isEmpty(affiliation.getDateFin())) {
                        anneeRadiation = Integer.valueOf(affiliation.getDateFin().substring(6, 10)).intValue();
                    }
                    if (anneeRadiation != 0) {
                        if (prochainControle > anneeRadiation) {
                            prochainControle = anneeRadiation;
                        }
                    }
                } else {
                    // La liste contient des contrôle

                    String prochainControleLibelle = (getAnneeDatePrevuePlusAncienControleSansDateEffective(listViewBean));
                    if (JadeStringUtil.isEmpty(prochainControleLibelle)) {
                        // Il n'y a que des contrôles avec date effective
                        prochainControle = Integer.valueOf(
                                (getAnneeDatePrevuPlusRecentePlusPropriete(listViewBean, periodicite, affiliation)))
                                .intValue();
                    } else {
                        prochainControle = Integer.valueOf(prochainControleLibelle).intValue();
                    }
                }
                return String.valueOf(prochainControle);
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }

    }

    private String getAnneeDatePrevuePlusAncienControleSansDateEffective(AFControleEmployeurListViewBean listViewBean) {
        for (int i = 0; i < listViewBean.size(); i++) {
            AFControleEmployeur entity = (AFControleEmployeur) listViewBean.getEntity(i);
            if (JadeStringUtil.isEmpty(entity.getDateEffective())) {
                return entity.getDatePrevue().substring(6, 10);
            }
        }
        return "";
    }

    private String getAnneeDatePrevuPlusRecentePlusPropriete(AFControleEmployeurListViewBean listViewBean,
            int periodicite, AFAffiliation affiliation) throws Exception {
        listViewBean.setOrderBy("MDDPRE DESC");
        listViewBean.find();
        AFControleEmployeur entity = (AFControleEmployeur) listViewBean.getFirstEntity();
        int periodeTotale = Integer.valueOf(entity.getDatePrevue().substring(6, 10)).intValue();
        int anneeRadiation = 0;
        if (!JadeStringUtil.isEmpty(affiliation.getDateFin())) {
            anneeRadiation = Integer.valueOf(affiliation.getDateFin().substring(6, 10)).intValue();
        }
        periodeTotale += periodicite;
        if (anneeRadiation != 0) {
            if (periodeTotale > anneeRadiation) {
                periodeTotale = anneeRadiation;
            }
        }
        return String.valueOf(periodeTotale);
    }
}