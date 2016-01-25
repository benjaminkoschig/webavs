package globaz.ij.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JAException;
import globaz.ij.api.basseindemnisation.IIJBaseIndemnisation;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisationJointTiers;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisationJointTiersManager;
import globaz.ij.vb.basesindemnisation.IJBaseIndemnisationJoinTiersListViewBean;
import globaz.ij.vb.basesindemnisation.IJBaseIndemnisationJoinTiersViewBean;
import globaz.ij.vb.basesindemnisation.IJBaseIndemnisationViewBean;
import globaz.ij.vb.prononces.IJNSSDTO;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Descpription.
 * 
 * @author scr Date de création 16 sept. 05
 */
public class IJBaseIndemnisationAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String VERS_ECRAN_DE = "_de.jsp";
    private static final String VERS_ECRAN_RC = "_rc.jsp";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    /**
     * Crée une nouvelle instance de la classe IJBaseIndemnisationAction.
     * 
     * @param servlet
     */
    public IJBaseIndemnisationAction(FWServlet servlet) {
        super(servlet);
    }

    /**
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        this.saveViewBean(viewBean, request);

        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    /**
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        this.saveViewBean(viewBean, request);

        return super._getDestModifierSucces(session, request, response, viewBean);

    }

    /**
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        IJBaseIndemnisationJoinTiersViewBean viewBean = new IJBaseIndemnisationJoinTiersViewBean();

        IJBaseIndemnisationJointTiersManager manager = new IJBaseIndemnisationJointTiersManager();
        manager.setSession((BSession) mainDispatcher.getSession());
        try {
            JSPUtils.setBeanProperties(request, viewBean);
            String h = viewBean.getIdPrononce();
            String o = viewBean.getIdBaseIndemisation();
            String p = viewBean.getIdTiers();
            manager.setForIdPrononce(viewBean.getIdPrononce());
            manager.find();

            if (manager.getSize() > 0) {
                IJBaseIndemnisationJointTiers data = (IJBaseIndemnisationJointTiers) manager.getFirstEntity();
                viewBean.setIdTiers(data.getIdTiers());
                viewBean.setIdBaseIndemisation(data.getIdBaseAdministration());

            }

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }
        deleguerActionChercher(viewBean, session, request, response, mainDispatcher);

        IJNSSDTO dto = new IJNSSDTO();
        dto.setNSS(viewBean.getFullDescriptionPrononce()[2]);

        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);
    }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        IJBaseIndemnisationJoinTiersListViewBean viewBean = new IJBaseIndemnisationJoinTiersListViewBean();

        try {
            JSPUtils.setBeanProperties(request, viewBean);
            try {
                viewBean.setSession((BSession) mainDispatcher.getSession());
                viewBean.find();
                // System.out.println();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // viewBean = mainDispatcher.dispatch(viewBean, this.privateAction);
        request.setAttribute("viewBean", viewBean);

        // pour bt [...] et pagination
        session.removeAttribute("listViewBean");
        session.setAttribute("listViewBean", viewBean);

        String destination = getRelativeURL(request, session) + "_rcListe.jsp";
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    @Override
    protected void actionSupprimer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        /*
         * Recréation dynamique de notre viewBean
         */
        FWAction action = FWAction.newInstance(request.getParameter("userAction"));

        /*
         * recuperation du bean depuis la session
         */
        IJBaseIndemnisationViewBean viewBean = (IJBaseIndemnisationViewBean) request.getAttribute("viewBean");
        if (viewBean == null) {
            viewBean = new IJBaseIndemnisationViewBean();
        }

        try {
            // on rempli le viewBean avec les données de la requêtes
            JSPUtils.setBeanProperties(request, viewBean);

            String idBaseIndemnisation = request.getParameter("forNoBaseIndemnisation");
            viewBean.setIdBaseIndemisation(idBaseIndemnisation);

            // on l'envoie au Helper
            viewBean = (IJBaseIndemnisationViewBean) mainDispatcher.dispatch(viewBean, action);
            this.saveViewBean(viewBean, request);
            this.saveViewBean(viewBean, session);

            /*
             * choix de la destination
             */
            String destination;
            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                destination = super._getDestSupprimerSucces(session, request, response, viewBean);
            } else {
                destination = super._getDestSupprimerEchec(session, request, response, viewBean);
            }

            /*
             * redirection vers la destination
             */
            goSendRedirect(destination, request, response);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }
    }

    /**
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected FWViewBeanInterface beforeAjouter(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        StringBuffer sb = new StringBuffer();

        IJBaseIndemnisationJoinTiersViewBean biViewBean = (IJBaseIndemnisationJoinTiersViewBean) viewBean;
        if (biViewBean.getIsPeriodeEtendue().booleanValue()) {

            // Dans le cas de période étendue, si la période étendue est saisie
            // sur un même mois,
            // cette période ne sera pas considérée comme une période étendue.
            // Il faut donc initialiser le calendrier correctement (avec le nbr
            // de jours attesté) pour qu'il puisse se recharger.

            // Dans les cas véritable de période étendue, ce code n'est pas
            // util.
            JACalendar cal = new JACalendarGregorian();
            long nbrJours = 0;
            try {
                nbrJours = cal.daysBetween(biViewBean.getDateDebutPeriodeEtendue(),
                        biViewBean.getDateFinPeriodeEtendue());
                nbrJours++;
            } catch (JAException e) {
                e.printStackTrace();
            }

            long maxSize = 31;
            if (nbrJours < 31) {
                maxSize = nbrJours;
            }

            for (int i = 0; i < maxSize; i++) {
                sb.append(IIJBaseIndemnisation.IJ_CALENDAR_NON_ATTESTE);
            }
        }

        else {
            // Parsing du calendrier...
            // Seul les valeurs 1,2 et '.' sont autorisée.
            // Toutes les valeurs autres sont considérée comme non-attestée.

            for (int i = biViewBean.getJourDebutChecked(); i <= biViewBean.getJourFinChecked(); i++) {

                String value = request.getParameter(IIJBaseIndemnisation.PREFIX_JOUR_CALENDRIER + String.valueOf(i));

                if ((value == null) || (value.length() == 0)) {
                    value = IIJBaseIndemnisation.IJ_CALENDAR_NON_ATTESTE;
                }

                if (!IIJBaseIndemnisation.IJ_CALENDAR_EXTERNE.equals(value)
                        && !IIJBaseIndemnisation.IJ_CALENDAR_INTERNE.equals(value)) {

                    value = IIJBaseIndemnisation.IJ_CALENDAR_NON_ATTESTE;
                }

                if (value != null) {
                    sb.append(value);
                }
            }
        }

        ((IJBaseIndemnisationJoinTiersViewBean) viewBean).setAttestationJours(sb.toString());
        ((IJBaseIndemnisationJoinTiersViewBean) viewBean).wantCreerFormulaires(true);

        return super.beforeAjouter(session, request, response, viewBean);
    }

    /**
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        StringBuffer sb = new StringBuffer();
        IJBaseIndemnisationJoinTiersViewBean biViewBean = (IJBaseIndemnisationJoinTiersViewBean) viewBean;

        // Dans le cas de période étendue, si la période étendue est saisie sur
        // un même mois,
        // cette période ne sera pas considérée comme une période étendue.
        // Il faut donc initialiser le calendrier correctement (avec le nbr de
        // jours attesté) pour qu'il puisse se recharger.

        // Dans les cas véritable de période étendue, ce code n'est pas util.

        if (biViewBean.getIsPeriodeEtendue().booleanValue()) {
            JACalendar cal = new JACalendarGregorian();
            long nbrJours = 0;
            try {
                nbrJours = cal.daysBetween(biViewBean.getDateDebutPeriodeEtendue(),
                        biViewBean.getDateFinPeriodeEtendue());
                nbrJours++;
            } catch (JAException e) {
                e.printStackTrace();
            }

            long maxSize = 31;
            if (nbrJours < 31) {
                maxSize = nbrJours;
            }

            for (int i = 0; i < maxSize; i++) {
                sb.append(IIJBaseIndemnisation.IJ_CALENDAR_NON_ATTESTE);
            }
        }

        else {
            for (int i = biViewBean.getJourDebutChecked(); i <= biViewBean.getJourFinChecked(); i++) {
                String value = request.getParameter(IIJBaseIndemnisation.PREFIX_JOUR_CALENDRIER + String.valueOf(i));

                if ((value == null) || (value.length() == 0)) {
                    value = IIJBaseIndemnisation.IJ_CALENDAR_NON_ATTESTE;
                }

                if (!IIJBaseIndemnisation.IJ_CALENDAR_EXTERNE.equals(value)
                        && !IIJBaseIndemnisation.IJ_CALENDAR_INTERNE.equals(value)) {

                    value = IIJBaseIndemnisation.IJ_CALENDAR_NON_ATTESTE;
                }

                if (value != null) {
                    sb.append(value);
                }
            }
        }

        ((IJBaseIndemnisationJoinTiersViewBean) viewBean).setAttestationJours(sb.toString());

        return super.beforeModifier(session, request, response, viewBean);
    }

    /**
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        return super.beforeNouveau(session, request, response, viewBean);
    }

    /**
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public String creerCorrection(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        IJBaseIndemnisationJoinTiersViewBean correction = new IJBaseIndemnisationJoinTiersViewBean();

        correction.setIdBaseIndemisation(getSelectedId(request));

        mainDispatcher.dispatch(correction, getAction());
        this.saveViewBean(correction, request);

        return getRelativeURL(request, session) + IJBaseIndemnisationAction.VERS_ECRAN_RC;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public String displayCalendar(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        try {
            ((IJBaseIndemnisationJoinTiersViewBean) viewBean).resetCalendar();
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        return getRelativeURL(request, session) + IJBaseIndemnisationAction.VERS_ECRAN_DE;
    }

    public String displayFullMonthCalendar(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {
        try {
            ((IJBaseIndemnisationJoinTiersViewBean) viewBean).setDernierJourDuMois(true);
            ((IJBaseIndemnisationJoinTiersViewBean) viewBean).resetCalendar();
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        return getRelativeURL(request, session) + IJBaseIndemnisationAction.VERS_ECRAN_DE;
    }

}
