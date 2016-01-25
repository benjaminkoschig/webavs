/*
 * Créé le 10 oct. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.cepheus.servlet;

import globaz.cepheus.db.dossier.DOMetaDossier;
import globaz.cepheus.db.intervenant.DOIntervenant;
import globaz.cepheus.vb.dossier.DOMetaDossierJointIntervenantsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class DOMetaDossierJointIntervenantsAction extends PRDefaultAction {

    public final static String ERROR_PAGE = "/errorPage.jsp";
    private static final String VERS_ECRAN_DE = "_de.jsp?";
    private static final String VERS_ECRAN_DE_ADD = VERS_ECRAN_DE + METHOD_ADD;
    private static final String VERS_ECRAN_DE_UPD = VERS_ECRAN_DE + METHOD_UPD;
    private String _destination = "";

    /**
     * @param servlet
     */
    public DOMetaDossierJointIntervenantsAction(FWServlet servlet) {
        super(servlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        FWViewBeanInterface viewBean = loadViewBean(session);

        String destination = "";

        if (isRetourPyxis(viewBean, session)) {

            ((DOMetaDossierJointIntervenantsViewBean) viewBean).setRetourFromPyxis(false);
            session.setAttribute("retourPyxis", Boolean.FALSE);

            // on revient depuis pyxis on se contente de forwarder car le bon
            // viewBean est deja en session
            if (JadeStringUtil.isIntegerEmpty(((DOMetaDossierJointIntervenantsViewBean) viewBean).getIdIntervenant())) {
                destination = getRelativeURL(request, session) + VERS_ECRAN_DE_ADD;
            } else {
                destination = getRelativeURL(request, session) + VERS_ECRAN_DE_UPD;
            }

        } else {
            try {

                if (!JadeStringUtil.isIntegerEmpty(request.getParameter("selectedId"))) {
                    viewBean = new DOMetaDossierJointIntervenantsViewBean();
                    JSPUtils.setBeanProperties(request, viewBean);
                    viewBean.setISession(mainDispatcher.getSession());

                    // Recherche de l'intervenant
                    DOIntervenant intervenant = new DOIntervenant();
                    intervenant.setISession(mainDispatcher.getSession());
                    intervenant.setIdIntervenant(request.getParameter("selectedId"));
                    intervenant.retrieve();

                    // Recherche des infos. du tiers
                    PRTiersWrapper tiers = PRTiersHelper.getTiersAdresseParId(mainDispatcher.getSession(),
                            intervenant.getIdTiersIntervenant());

                    // recuperation des data
                    ((DOMetaDossierJointIntervenantsViewBean) viewBean).setIdTiersIntervenant(intervenant
                            .getIdTiersIntervenant());
                    ((DOMetaDossierJointIntervenantsViewBean) viewBean).setNomIntervenant(tiers
                            .getProperty(PRTiersWrapper.PROPERTY_NOM));
                    ((DOMetaDossierJointIntervenantsViewBean) viewBean).setPrenomIntervenant(tiers
                            .getProperty(PRTiersWrapper.PROPERTY_PRENOM));
                    ((DOMetaDossierJointIntervenantsViewBean) viewBean)
                            .setCsDescription(intervenant.getCsDescription());
                    ((DOMetaDossierJointIntervenantsViewBean) viewBean).setDateDebut(intervenant.getDateDebut());
                    ((DOMetaDossierJointIntervenantsViewBean) viewBean).setDateFin(intervenant.getDateFin());

                    destination = getRelativeURL(request, session) + VERS_ECRAN_DE;
                } else {
                    viewBean = new DOMetaDossierJointIntervenantsViewBean();
                    viewBean.setISession(mainDispatcher.getSession());
                    destination = getRelativeURL(request, session) + VERS_ECRAN_DE_ADD;
                }

            } catch (Exception e) {
                destination = ERROR_PAGE;
                viewBean.setMessage(e.getMessage());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }

        }

        ((DOMetaDossierJointIntervenantsViewBean) viewBean).setIdMetaDossier((String) session
                .getAttribute("idMetaDossier"));
        ((DOMetaDossierJointIntervenantsViewBean) viewBean).setIdTiersMetaDossier((String) session
                .getAttribute("idTiersMetaDossier"));
        ((DOMetaDossierJointIntervenantsViewBean) viewBean).setNomTiersMetaDossier((String) session
                .getAttribute("nomTiersMetaDossier"));
        ((DOMetaDossierJointIntervenantsViewBean) viewBean).setPrenomTiersMetaDossier((String) session
                .getAttribute("prenomTiersMetaDossier"));
        ((DOMetaDossierJointIntervenantsViewBean) viewBean).setNoAvsTiersMetaDossier((String) session
                .getAttribute("noAvsTiersMetaDossier"));
        ((DOMetaDossierJointIntervenantsViewBean) viewBean).setCsTypeDemande((String) session
                .getAttribute("csTypeDemande"));

        saveViewBean(viewBean, session);
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        DOMetaDossierJointIntervenantsViewBean viewBean = (DOMetaDossierJointIntervenantsViewBean) request
                .getAttribute("viewBean");

        if (viewBean == null) {

            viewBean = new DOMetaDossierJointIntervenantsViewBean();
            viewBean.setSession((BSession) mainDispatcher.getSession());
        }

        request.setAttribute("viewBean", viewBean);
        servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_rc.jsp")
                .forward(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionSelectionner (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionSelectionner(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        // StringBuffer queryString = new StringBuffer();
        //
        // queryString.append(USER_ACTION);
        // queryString.append("=");
        // queryString.append(IDOActions.ACTION_CHERCHER_PRESTATION_INTERVENANTS);
        // queryString.append(".");
        // queryString.append(FWAction.ACTION_CHERCHER);
        //
        // // HACK: on remplace une des valeurs sauvee en session par
        // FWSelectorTag
        // session.setAttribute(FWDefaultServletAction.ATTRIBUT_SELECTOR_CUSTOMERURL,
        // queryString.toString());

        DOMetaDossierJointIntervenantsViewBean viewBean = (DOMetaDossierJointIntervenantsViewBean) session
                .getAttribute("viewBean");
        viewBean.setNomIntervenant("");
        viewBean.setPrenomIntervenant("");
        saveViewBean(viewBean, session);

        session.setAttribute("retourPyxis", Boolean.TRUE);

        super.actionSelectionner(session, request, response, mainDispatcher);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    public void chercherDepuisDemande(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        DOMetaDossierJointIntervenantsViewBean MDJIViewBean = (DOMetaDossierJointIntervenantsViewBean) request
                .getAttribute("viewBean");

        if (MDJIViewBean == null) {

            MDJIViewBean = new DOMetaDossierJointIntervenantsViewBean();
            MDJIViewBean.setSession((BSession) mainDispatcher.getSession());

            // recuperer les infos du tiers et du type de meta dossier
            try {
                JSPUtils.setBeanProperties(request, MDJIViewBean);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                MDJIViewBean.setMessage(e.getMessage());
                MDJIViewBean.setMsgType(FWViewBeanInterface.ERROR);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                MDJIViewBean.setMessage(e.getMessage());
                MDJIViewBean.setMsgType(FWViewBeanInterface.ERROR);
            }

            // si l'id meta dossier n'est pas donne, il faut creer un meta
            // dossier
            String idMetaDossier = request.getParameter("idMetaDossier");

            if (JadeStringUtil.isDecimalEmpty(idMetaDossier)) {

                createMetaDossier(MDJIViewBean, request.getParameter("idDemande"),
                        (BSession) mainDispatcher.getSession());
            }
        }

        request.setAttribute("viewBean", MDJIViewBean);
        session.setAttribute("idMetaDossier", MDJIViewBean.getIdMetaDossier());
        session.setAttribute("NomTiersMetaDossier", MDJIViewBean.getNomTiersMetaDossier());
        session.setAttribute("PrenomTiersMetaDossier", MDJIViewBean.getPrenomTiersMetaDossier());
        session.setAttribute("NoAvsTiersMetaDossier", MDJIViewBean.getNoAvsTiersMetaDossier());
        session.setAttribute("csTypeDemande", MDJIViewBean.getCsTypeDemande());
        session.setAttribute("detailIntervenant", MDJIViewBean.getDetailIntervenant());

        servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_rc.jsp")
                .forward(request, response);
    }

    /**
     * Creation d'un meta dossier pour le type et la demande donne
     * 
     * @param viewBean
     *            pour sauvegarder l'id du meta dossier creer
     * @param idDemande
     *            la demande pour laquelle le meta dossier est ceeer
     * @param csTypeMetaDossier
     *            le type du meta dossier creer
     * @param session
     */
    private void createMetaDossier(DOMetaDossierJointIntervenantsViewBean viewBean, String idDemande, BSession session) {

        BITransaction transaction = null;
        try {
            transaction = session.newTransaction();

            if (!JadeStringUtil.isDecimalEmpty(idDemande)) {
                // creation de meta dossier
                DOMetaDossier metaDossier = new DOMetaDossier();
                metaDossier.setSession(session);
                metaDossier.add(transaction);

                // mise à jour de la demande
                PRDemande demande = new PRDemande();
                demande.setSession(session);
                demande.setIdDemande(idDemande);
                demande.retrieve();
                demande.setIdMetaDossier(metaDossier.getIdMetaDossier());
                demande.update(transaction);

                transaction.commit();

                // sauvegarde de l'id meta dossier dans le view bean pour la
                // recherche
                viewBean.setIdMetaDossier(metaDossier.getIdMetaDossier());

            } else {
                viewBean.setMessage("id demande absent");
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }

        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (RemoteException e1) {
                viewBean.setMessage(e1.getMessage());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            } catch (Exception e1) {
                viewBean.setMessage(e1.getMessage());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        } finally {
            try {
                transaction.closeTransaction();
            } catch (RemoteException e1) {
                viewBean.setMessage(e1.getMessage());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            } catch (Exception e1) {
                viewBean.setMessage(e1.getMessage());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        }
    }

    /**
     * inspecte le viewBean et retourne vrai si celui-ci indique que l'on revient de pyxis.
     * 
     * @param viewBean
     * 
     * @return
     */
    private boolean isRetourPyxis(FWViewBeanInterface viewBean, HttpSession session) {
        return ((viewBean != null) && (viewBean instanceof DOMetaDossierJointIntervenantsViewBean)
                && ((DOMetaDossierJointIntervenantsViewBean) viewBean).isRetourFromPyxis() && ((Boolean) session
                    .getAttribute("retourPyxis")).booleanValue());
    }

}
