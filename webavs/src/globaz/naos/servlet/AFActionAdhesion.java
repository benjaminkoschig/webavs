/*
 * Created on 18-Jan-05
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.adhesion.AFAdhesionViewBean;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour l'entité Adhésion.
 * 
 * @author sau
 */
public class AFActionAdhesion extends AFDefaultActionChercher {

    public final static String ACTION_AFFICHER_SELECTION_COTISATION = "afficherSelectionCotisation";
    public final static String ACTION_AJOUTER_COTISATION = "ajouterCotisation";

    /**
     * Constructeur d'AFActionAdhesion.
     * 
     * @param servlet
     */
    public AFActionAdhesion(FWServlet servlet) {
        super(servlet);
    }

    protected boolean _checkRealDate(BSession session, String fieldValue) {
        try {
            BSessionUtil.checkRealDateGregorian(session, fieldValue);
            return true;
        } catch (Exception e) {
            return false;
        }
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

        AFAdhesionViewBean vBean = new AFAdhesionViewBean();
        vBean.setAffiliationId(request.getParameter("affiliationId"));

        this.actionChercher(vBean, session, request, response, mainDispatcher);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        if (AFActionAdhesion.ACTION_AFFICHER_SELECTION_COTISATION.equals(getAction().getActionPart())) {
            try {
                afficherSelectionCotisation(session, request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (AFActionAdhesion.ACTION_AJOUTER_COTISATION.equals(getAction().getActionPart())) {
            ajouterCotisation(session, request, response, dispatcher);
        }
    }

    protected void afficherSelectionCotisation(HttpSession session, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String date = request.getParameter("dateDebut");
        String dateFin = request.getParameter("dateFin");
        String idPlanAffiliation = request.getParameter("planAffiliationId");

        AFAdhesionViewBean viewBean = new AFAdhesionViewBean();

        Object vBean = session.getAttribute("viewBean");

        if ((vBean != null) && (vBean instanceof AFAdhesionViewBean)) {

            viewBean = (AFAdhesionViewBean) session.getAttribute("viewBean");
            BSession bSession = (BSession) CodeSystem.getSession(session);
            viewBean.setPlanAffiliationId(idPlanAffiliation);

            boolean valide = _checkRealDate(bSession, date);
            boolean valideFin = _checkRealDate(bSession, dateFin);

            if (!JadeStringUtil.isBlankOrZero(date) && !JadeStringUtil.isBlankOrZero(dateFin)) {
                if (valide && valideFin) {
                    if (BSessionUtil.compareDateFirstGreater(bSession, date, dateFin)) {
                        String _destination = "/" + getAction().getApplicationPart()
                                + "?userAction=naos.adhesion.adhesion.ajouter";
                        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
                    }
                }
            }

            if (!valide || (!JadeStringUtil.isBlankOrZero(dateFin) && !valideFin)) {
                String _destination = "/" + getAction().getApplicationPart()
                        + "?userAction=naos.adhesion.adhesion.ajouter";
                servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
            } else {
                viewBean.setDateDebut(date);
                viewBean.setDateFin(dateFin);

                if (JadeStringUtil.isBlankOrZero(viewBean.getPlanCaisseId())) {
                    String _destination = "/" + getAction().getApplicationPart()
                            + "?userAction=naos.adhesion.adhesion.ajouter";
                    servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
                } else {
                    String _destination = getRelativeURLwithoutClassPart(request, session)
                            + "adhesionCotisation_de.jsp?_valid=fail";
                    servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
                }
            }
        }
    }

    protected void ajouterCotisation(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        String _destination = "";
        String affiliationId = "";
        String idAdhesion = "";

        List cotisationCreated = new ArrayList();

        String dateDebut = request.getParameter("dateDebutReadOnly").trim();
        String dateFin = request.getParameter("dateFinReadOnly").trim();

        AFAdhesionViewBean viewBean = (AFAdhesionViewBean) session.getAttribute("viewBean");

        // BSession bSessionCtrl = (BSession) CodeSystem.getSession(session);

        boolean valideDebut = _checkRealDate(viewBean.getSession(), dateDebut);
        boolean valideFin = _checkRealDate(viewBean.getSession(), dateFin);

        if (JadeStringUtil.isBlank(dateDebut) || !valideDebut) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(viewBean.getSession().getLabel("160"));
        } else if (!JadeStringUtil.isBlank(dateFin) && !valideFin) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(viewBean.getSession().getLabel("180"));
        } else {
            viewBean.setDateDebut(dateDebut);
            viewBean.setDateFin(dateFin);
            viewBean.setAddOnlyAdhesion(true);

            // Appel au dispatcher
            try {
                updateCotisationMap(request, viewBean);
                getAction().setRight(FWSecureConstants.ADD);
                dispatcher.dispatch(viewBean, getAction());
            } catch (Exception e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.toString());
            }
        }
        boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
        if (!goesToSuccessDest) {
            _destination = getRelativeURLwithoutClassPart(request, session) + "adhesionCotisation_de.jsp?_valid=fail";

        } else {
            _destination = "/" + getAction().getApplicationPart() + "?userAction=naos.cotisation.cotisation.chercher";
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Effectue des traitements avant la création d'une nouvelle entité dans la DB.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeNouveau(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        AFAdhesionViewBean vBean = (AFAdhesionViewBean) viewBean;

        try {
            AFAffiliation affiliation = new AFAffiliation();
            affiliation.setSession((BSession) CodeSystem.getSession(session));
            affiliation.setAffiliationId(request.getParameter("affiliationId"));
            affiliation.retrieve();

            vBean.setAffiliationId(affiliation.getAffiliationId());
            vBean.setDateDebut(affiliation.getDateDebut());
            vBean.setDateFin(affiliation.getDateFin());
            vBean.setTypeAdhesion(CodeSystem.TYPE_ADHESION_CAISSE);
        } catch (Exception e) {
            vBean.setMsgType(FWViewBeanInterface.ERROR);
            vBean.setMessage(e.getMessage());
        }
        return vBean;
    }

    protected void updateCotisationMap(HttpServletRequest request, AFAdhesionViewBean viewBean) throws Exception {

        Map updatedMap = new HashMap();
        Map handleMap = new HashMap();

        for (Iterator iterator = viewBean.getCotisationMap().keySet().iterator(); iterator.hasNext();) {

            String j = (String) iterator.next();
            String masse = request.getParameter("nouvelleMasse" + j);
            String plan = request.getParameter("planAffiliationSelectedId" + j);
            String periodiciteNouvelleMasse = request.getParameter("periodiciteNouvelleMasse" + j);
            String nouvellePeriodiciteCotisation = request.getParameter("nouvellePeriodicite" + j);

            AFCotisation coti = (AFCotisation) viewBean.getCotisationMap().get(j);

            if (!JadeStringUtil.isBlankOrZero(masse)) {
                try {
                    coti.setMasseAnnuelle(AFUtil.transformMasseToMasseAnnuelle(masse, periodiciteNouvelleMasse));
                } catch (Exception e) {
                    throw new Exception(viewBean.getSession().getLabel("ERREUR_TRANSFORM_MASSE_TO_MASSE_ANNUELLE")
                            + " : " + e.toString());
                }
            } else {
                coti.setMasseAnnuelle("");
            }

            coti.setPlanAffiliationId(plan);
            coti.setPeriodicite(nouvellePeriodiciteCotisation);

            coti.setDateDebut(request.getParameter("dateDebutCotisation" + j));

            if (!JadeStringUtil.isBlankOrZero(request.getParameter("dateFinCotisation" + j))) {
                coti.setDateFin(request.getParameter("dateFinCotisation" + j));
                coti.setMotifFin(CodeSystem.MOTIF_FIN_FIN_ADHESION);
            }

            String value = request.getParameter("cotisationToAdd" + j);
            if ("on".equals(value)) {
                handleMap.put(j, coti);
            }
            updatedMap.put(j, coti);
        }
        viewBean.setCotisationMap(updatedMap);
        viewBean.setHandleCotisationMap(handleMap);
    }
}
