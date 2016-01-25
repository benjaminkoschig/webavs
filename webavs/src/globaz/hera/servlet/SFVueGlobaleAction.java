/*
 * Créé le 8 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.db.famille.SFRequerantDTO;
import globaz.hera.external.ISFUrlEncode;
import globaz.hera.tools.SFSessionDataContainerHelper;
import globaz.hera.vb.famille.SFApercuRelationConjointViewBean;
import globaz.hera.vb.famille.SFLiantVO;
import globaz.hera.vb.famille.SFMembreVO;
import globaz.hera.vb.famille.SFVueGlobaleViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.prestation.ged.PRGedAffichageDossier;
import globaz.prestation.tools.PRAssert;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author scr
 * 
 *         <p>
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 *         </p>
 */
public class SFVueGlobaleAction extends SFDefaultAction {

    private static final String VERS_ECRAN_DE_ADD = "_de.jsp?" + METHOD_ADD;
    private static final String VERS_ECRAN_DE_UPD = "_de.jsp?" + METHOD_UPD;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    /**
     * Crée une nouvelle instance de la classe SFApercuRelationConjointAction.
     * 
     * @param servlet
     */
    public SFVueGlobaleAction(FWServlet servlet) {
        super(servlet);
    }

    /**
     * 
     * @param session
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void actionAfficherDossierGed(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        SFVueGlobaleViewBean viewBean = new SFVueGlobaleViewBean();
        try {
            PRGedAffichageDossier.actionAfficherDossierGed(session, request, response, mainDispatcher, viewBean);
        } catch (JadeServiceLocatorException e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        } catch (JadeServiceActivatorException e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        } catch (NullPointerException e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        } catch (ClassCastException e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        } catch (JadeClassCastException e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */

    protected void actionAfficherFamilleMembre(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = "";
        String idMembreFamille = request.getParameter("idMembreFamille");
        String urlFrom = request.getParameter("urlFrom");
        String isDisplayPeriodes = request.getParameter("checkBoxDisplayPeriodes");

        SFVueGlobaleViewBean viewBean = new SFVueGlobaleViewBean();
        if (!JadeStringUtil.isBlankOrZero(isDisplayPeriodes)
                && ("true".equals(isDisplayPeriodes) || "on".equals(isDisplayPeriodes))) {
            viewBean.setIsDisplayPeriode(Boolean.TRUE);
        } else {
            viewBean.setIsDisplayPeriode(Boolean.FALSE);
        }

        // On récupère le membre de famille...
        SFMembreFamille mf = new SFMembreFamille();
        mf.setSession((BSession) mainDispatcher.getSession());
        mf.setIdMembreFamille(idMembreFamille);
        try {
            mf.retrieve();
            PRAssert.notIsNew(mf, ((BSession) mainDispatcher.getSession()).getLabel("ERROR_MEMBRE_FAMILLE_NON_TROUVE")
                    + idMembreFamille);
        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            saveViewBean(viewBean, request);

            destination = getRelativeURL(request, session) + "_de.jsp";
            servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
        }

        SFLiantVO liant = new SFLiantVO((BSession) mainDispatcher.getSession());
        liant.setRequerant(false);

        SFMembreVO mvo = new SFMembreVO();
        mvo.setCsDomaine(mf.getCsDomaineApplication());
        mvo.setCsNationalite(mf.getCsNationalite());
        mvo.setCsSexe(mf.getCsSexe());
        mvo.setDateDeces(mf.getDateDeces());
        mvo.setDateNaissance(mf.getDateNaissance());
        mvo.setIdMembreFamille(mf.getIdMembreFamille());
        mvo.setIdTiers(mf.getIdTiers());
        mvo.setNom(mf.getNom());
        mvo.setNssFormatte(mf.getNss());
        mvo.setPrenom(mf.getPrenom());
        liant.setMembreFamille(mvo);

        viewBean.setLiant(liant);
        mainDispatcher.dispatch(viewBean, getAction());
        saveViewBean(viewBean, request);

        // met l'adresse de retour en session
        if (!JadeStringUtil.isBlankOrZero(urlFrom)) {
            String urlDecode = ISFUrlEncode.decodeUrl(urlFrom);
            session.setAttribute(SFSessionDataContainerHelper.KEY_VALEUR_RETOUR, urlDecode);
        }

        destination = getRelativeURL(request, session) + "_de.jsp";
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    protected void actionAfficherFamilleRequerant(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = "";
        SFVueGlobaleViewBean viewBean = new SFVueGlobaleViewBean();

        // En cas de 'controle d'impact' suite au changement d'état civil, par
        // exemple.
        // il se peut que des messages d'avertissement aient été renseigné. On
        // les récupère
        // pour les afficher dans une popup.
        FWViewBeanInterface vv = (FWViewBeanInterface) session.getAttribute("viewBean");
        if (vv instanceof SFApercuRelationConjointViewBean) {
            if (FWViewBeanInterface.WARNING.equals(((SFApercuRelationConjointViewBean) vv).getMsgType())) {
                viewBean.setMsgType(FWViewBeanInterface.WARNING);
                viewBean.setMessage(((SFApercuRelationConjointViewBean) vv).getMessage());
            }
        }

        String isDisplayPeriodes = request.getParameter("checkBoxDisplayPeriodes");

        if (!JadeStringUtil.isBlankOrZero(isDisplayPeriodes)
                && ("true".equals(isDisplayPeriodes) || "on".equals(isDisplayPeriodes))) {
            viewBean.setIsDisplayPeriode(Boolean.TRUE);
        } else {
            viewBean.setIsDisplayPeriode(Boolean.FALSE);
        }

        // viewBean.setLiant(liant)

        SFRequerantDTO reqDTO = (SFRequerantDTO) SFSessionDataContainerHelper.getData(session,
                SFSessionDataContainerHelper.KEY_REQUERANT_DTO);

        // On controle que le réquérant soit sélectionné.
        if (reqDTO != null) {
            SFLiantVO liant = new SFLiantVO((BSession) mainDispatcher.getSession());
            liant.setIdRequerant(reqDTO.getIdRequerant());
            // Indique si cette famille est issue de la reprise de données !!!
            liant.setProvenance(reqDTO.getProvenance());
            liant.setRequerant(true);

            SFMembreVO mvo = new SFMembreVO();
            mvo.setCsDomaine(reqDTO.getIdDomaineApplication());
            mvo.setCsNationalite(reqDTO.getCsNationalite());
            mvo.setCsSexe(reqDTO.getCsSexe());
            mvo.setDateDeces(reqDTO.getDateDeces());
            mvo.setDateNaissance(reqDTO.getDateNaissance());
            mvo.setIdMembreFamille(reqDTO.getIdMembreFamille());
            mvo.setIdTiers(reqDTO.getIdTiers());
            mvo.setNom(reqDTO.getNom());
            mvo.setNssFormatte(reqDTO.getNss());
            mvo.setPrenom(reqDTO.getPrenom());
            liant.setMembreFamille(mvo);

            viewBean.setLiant(liant);
            mainDispatcher.dispatch(viewBean, getAction());

        } else {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(((BSession) mainDispatcher.getSession()).getLabel("JSP_SF_VUE_GLOBALE_REQ_NOT_FOUND"));
        }
        saveViewBean(viewBean, request);
        /*
         * choix destination
         */
        destination = getRelativeURL(request, session) + "_de.jsp";
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    protected void actionAfficherPeriodesVGMembreFamille(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {

        actionAfficherFamilleMembre(session, request, response, mainDispatcher);
    }

    protected void actionAfficherPeriodesVGRequerant(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {

        actionAfficherFamilleRequerant(session, request, response, mainDispatcher);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param dispatcher
     *            DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String actionPart = getAction().getActionPart();
        if ("afficherFamilleRequerant".equals(actionPart)) {
            actionAfficherFamilleRequerant(session, request, response, dispatcher);
        } else if ("afficherFamilleMembre".equals(actionPart)) {
            actionAfficherFamilleMembre(session, request, response, dispatcher);
        } else if ("afficherPeriodesVGRequerant".equals(actionPart)) {
            actionAfficherPeriodesVGRequerant(session, request, response, dispatcher);
        } else if ("afficherPeriodesVGMembreFamille".equals(actionPart)) {
            actionAfficherPeriodesVGMembreFamille(session, request, response, dispatcher);
        } else if ("actionAfficherDossierGed".equals(actionPart)) {
            actionAfficherDossierGed(session, request, response, dispatcher);
        } else if ("initialiserProvenance".equals(actionPart)) {
            actionInitialiserProvenanceRequerant(session, request, response, dispatcher);
        } else {
            // on a demandé un page qui n'existe pas
            servlet.getServletContext().getRequestDispatcher(UNDER_CONSTRUCTION_PAGE).forward(request, response);
        }
    }

    protected void actionInitialiserProvenanceRequerant(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = "";
        SFVueGlobaleViewBean viewBean = new SFVueGlobaleViewBean();

        // En cas de 'controle d'impact' suite au changement d'état civil, par
        // exemple.
        // il se peut que des messages d'avertissement aient été renseigné. On
        // les récupère
        // pour les afficher dans une popup.
        FWViewBeanInterface vv = (FWViewBeanInterface) session.getAttribute("viewBean");
        if (vv instanceof SFApercuRelationConjointViewBean) {
            if (FWViewBeanInterface.WARNING.equals(((SFApercuRelationConjointViewBean) vv).getMsgType())) {
                viewBean.setMsgType(FWViewBeanInterface.WARNING);
                viewBean.setMessage(((SFApercuRelationConjointViewBean) vv).getMessage());
            }
        }

        String isDisplayPeriodes = request.getParameter("checkBoxDisplayPeriodes");

        if (!JadeStringUtil.isBlankOrZero(isDisplayPeriodes)
                && ("true".equals(isDisplayPeriodes) || "on".equals(isDisplayPeriodes))) {
            viewBean.setIsDisplayPeriode(Boolean.TRUE);
        } else {
            viewBean.setIsDisplayPeriode(Boolean.FALSE);
        }

        // viewBean.setLiant(liant)

        SFRequerantDTO reqDTO = (SFRequerantDTO) SFSessionDataContainerHelper.getData(session,
                SFSessionDataContainerHelper.KEY_REQUERANT_DTO);

        // On controle que le réquérant soit sélectionné.
        if (reqDTO != null) {
            SFLiantVO liant = new SFLiantVO((BSession) mainDispatcher.getSession());
            liant.setIdRequerant(reqDTO.getIdRequerant());
            // Indique si cette famille est issue de la reprise de données !!!
            liant.setProvenance(reqDTO.getProvenance());
            liant.setRequerant(true);

            SFMembreVO mvo = new SFMembreVO();
            mvo.setCsDomaine(reqDTO.getIdDomaineApplication());
            mvo.setCsNationalite(reqDTO.getCsNationalite());
            mvo.setCsSexe(reqDTO.getCsSexe());
            mvo.setDateDeces(reqDTO.getDateDeces());
            mvo.setDateNaissance(reqDTO.getDateNaissance());
            mvo.setIdMembreFamille(reqDTO.getIdMembreFamille());
            mvo.setIdTiers(reqDTO.getIdTiers());
            mvo.setNom(reqDTO.getNom());
            mvo.setNssFormatte(reqDTO.getNss());
            mvo.setPrenom(reqDTO.getPrenom());
            liant.setMembreFamille(mvo);

            viewBean.setLiant(liant);
            mainDispatcher.dispatch(viewBean, getAction());
            if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
                reqDTO.setProvenance("");
            }
        } else {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(((BSession) mainDispatcher.getSession()).getLabel("JSP_SF_VUE_GLOBALE_REQ_NOT_FOUND"));
        }
        saveViewBean(viewBean, request);
        /*
         * choix destination
         */
        destination = getRelativeURL(request, session) + "_de.jsp";
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

}