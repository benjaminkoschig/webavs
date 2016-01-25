package globaz.phenix.servlet.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichageListViewBean;
import globaz.phenix.db.communications.CPCommunicationImprimerViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Insérez la description du type ici. Date de création : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */

public class CPActionCommunicationFiscaleAffichage extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public CPActionCommunicationFiscaleAffichage(FWServlet servlet) {
        super(servlet);
    }

    // protected String _getDestAjouterEchec(
    // HttpSession session,
    // HttpServletRequest request,
    // HttpServletResponse response,
    // FWViewBeanInterface viewBean) {
    // //return getActionFullURL()+".reAfficher";
    // return getRelativeURLwithoutClassPart(request, session) +
    // "communicationFiscale.reAfficher";
    // }

    /**
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * 
     *            Execution du/des process dans le Helper Certains getter/setter ont du être rajouté au viewbean de
     *            manière a pouvoir obtenir l'ensemble des infos dans le helper send redirect modifier en forward
     */
    private void _actionExecuterImprimer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {

            CPCommunicationImprimerViewBean viewBean = (CPCommunicationImprimerViewBean) session
                    .getAttribute("viewBean");
            JSPUtils.setBeanProperties(request, viewBean);

            dispatcher.dispatch(viewBean, getAction());
            // Crée le process qui supprime dans la BD
            // if("on".equals(request.getParameter ("impressionListe"))){
            // CPProcessListeCommunicationsFiscales process = new
            // CPProcessListeCommunicationsFiscales();
            // //process.setEMailAddress(request.getParameter ("eMailAddress"));
            // process.setForCanton(request.getParameter ("canton"));
            // if("on".equals(request.getParameter ("dateEnvoiVide"))){
            // process.setDateEnvoiVide(Boolean.TRUE);
            // } else {
            // process.setDateEnvoiVide(Boolean.FALSE);
            // }
            // //process.setDateEdition(request.getParameter ("dateEdition"));
            // process.setDateEnvoi(request.getParameter ("dateEnvoi"));
            // process.setForGenreAffilie(request.getParameter
            // ("genreAffilie"));
            // process.setAnneeDecision(request.getParameter ("anneeDecision"));
            // // Avec année encours
            // if (JadeStringUtil.isEmpty("anneeDecision")){
            // if("on".equals(request.getParameter ("withAnneeEnCours"))){
            // process.setWithAnneeEnCours(Boolean.TRUE);
            // } else {
            // process.setWithAnneeEnCours(Boolean.FALSE);
            // }
            // } else {
            // process.setWithAnneeEnCours(Boolean.FALSE);
            // }
            // process.setSession((BSession)dispatcher.getSession());
            // process.start();
            // if (process.getMsgType().equals(BProcess.ERROR)) {
            // viewBean.setMsgType(process.getMsgType());
            // viewBean.setMessage(process.getMessage());
            // }
            // } else {
            // CPProcessImprimerCommunicationEnvoi process = new
            // CPProcessImprimerCommunicationEnvoi();
            // process.setEMailAddress(request.getParameter ("eMailAddress"));
            // process.setForCanton(request.getParameter ("canton"));
            // if("on".equals(request.getParameter ("dateEnvoiVide"))){
            // process.setDateEnvoiVide(Boolean.TRUE);
            // } else {
            // process.setDateEnvoiVide(Boolean.FALSE);
            // }
            // process.setForGenreAffilie(request.getParameter
            // ("genreAffilie"));
            // process.setAnneeDecision(request.getParameter ("anneeDecision"));
            // process.setDateEdition(request.getParameter ("dateEdition"));
            // process.setDateEnvoi(request.getParameter ("dateEnvoi"));
            // // Avec année encours
            // if (JadeStringUtil.isEmpty("anneeDecision")){
            // if("on".equals(request.getParameter ("withAnneeEnCours"))){
            // process.setWithAnneeEnCours(Boolean.TRUE);
            // } else {
            // process.setWithAnneeEnCours(Boolean.FALSE);
            // }
            // } else {
            // process.setWithAnneeEnCours(Boolean.FALSE);
            // }
            // process.setSession((BSession)dispatcher.getSession());
            // process.start();
            // if (process.getMsgType().equals(BProcess.ERROR)) {
            // viewBean.setMsgType(process.getMsgType());
            // viewBean.setMessage(process.getMessage());
            // }
            // }
            _destination = "/phenix?userAction=phenix.communications.communicationImprimer";
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = _destination + ".reAfficher";
            } else {
                _destination = _destination + ".afficher&process=launched";
            }
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        // servlet.getServletContext().getRequestDispatcher(_destination).forward(request,
        // response);
        /*
         * affiche la prochaine page
         */
        // response.sendRedirect(request.getContextPath() + _destination);
        goSendRedirect(_destination, request, response);
    }

    @Override
    protected String _getDestEchec(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        /*
         * _valid=fail : revient en mode edition _back=sl : sans effacer les champs deja rempli par l'utilisateur
         */
        return "/phenix?userAction=phenix.communications.communicationFiscale.reAfficher";
        // return getRelativeURLwithoutClassPart(request, session)+
        // "communicationFiscale.reAfficher";
        // return getActionFullURL() + ".afficher" + "&_valid=fail";
        // return getRelativeURL(request,session)+"_de.jsp?_valid=fail";
    }

    /**
     * ok car on ne veut pas forwarder sur une page blanche
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if ("imprimer".equals(getAction().getActionPart())) {
            _actionExecuterImprimer(session, request, response, dispatcher);
        } else {
            // on a demandé un page qui n'existe pas
            servlet.getServletContext().getRequestDispatcher(UNDER_CONSTRUCTION_PAGE).forward(request, response);
        }
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
        CPCommunicationFiscaleAffichageListViewBean listViewBean = (CPCommunicationFiscaleAffichageListViewBean) viewBean;

        // Fixes les parametres de recherche
        String orderBy = request.getParameter("trierPar");
        if (!JadeStringUtil.isBlank(orderBy)) {
            if ("ORDER_BY_NOM_PRENOM".equals(orderBy)) {
                listViewBean.orderByNom();
                listViewBean.orderByPrenom();
            } else if ("ORDER_BY_CONTRIBUABLE".equals(orderBy)) {
                listViewBean.orderByNumContribuable();
            } else if ("ORDER_BY_ANNEE".equals(orderBy)) {
                listViewBean.orderByAnnee();
            } else if ("ORDER_BY_AFFILIE".equals(orderBy)) {
                listViewBean.orderByNumAffilie();
            } else if ("ORDER_BY_NSS".equals(orderBy)) {
                listViewBean.orderByNumAVS();
            }
        }

        String forReportType = request.getParameter("forReportType");
        if (!JadeStringUtil.isBlank(forReportType)) {
            listViewBean.setReportType(forReportType);
        }

        listViewBean.changeManagerSize(20);
        listViewBean.wantCallMethodAfter(false);

        return viewBean;
    }

}
