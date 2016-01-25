package globaz.ij.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JAException;
import globaz.ij.api.basseindemnisation.IIJBaseIndemnisation;
import globaz.ij.vb.basesindemnisation.IJBaseIndemnisationAitAaViewBean;
import globaz.ij.vb.prononces.IJNSSDTO;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Descpription.
 * 
 * @author bsc Date de création 24 oct. 07
 */
public class IJBaseIndemnisationAitAaAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String VERS_ECRAN_DE = "_de.jsp";
    private static final String VERS_ECRAN_RC = "baseIndemnisation_rc.jsp";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJBaseIndemnisationAction.
     * 
     * @param servlet
     */
    public IJBaseIndemnisationAitAaAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

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
        saveViewBean(viewBean, request);

        return "/ij?userAction=ij.basesindemnisation.baseIndemnisation.chercher";
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
        saveViewBean(viewBean, request);

        return "/ij?userAction=ij.basesindemnisation.baseIndemnisation.chercher";

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
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        saveViewBean(viewBean, request);

        return "/ij?userAction=ij.basesindemnisation.baseIndemnisation.chercher";
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

        IJBaseIndemnisationAitAaViewBean viewBean = new IJBaseIndemnisationAitAaViewBean();

        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }
        deleguerActionChercher(viewBean, session, request, response, mainDispatcher);

        IJNSSDTO dto = new IJNSSDTO();
        dto.setNSS(viewBean.getFullDescriptionPrononce()[2]);

        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);
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

        IJBaseIndemnisationAitAaViewBean biViewBean = (IJBaseIndemnisationAitAaViewBean) viewBean;
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

                if (value == null || value.length() == 0) {
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

        ((IJBaseIndemnisationAitAaViewBean) viewBean).setAttestationJours(sb.toString());
        ((IJBaseIndemnisationAitAaViewBean) viewBean).wantCreerFormulaires(true);

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
        IJBaseIndemnisationAitAaViewBean biViewBean = (IJBaseIndemnisationAitAaViewBean) viewBean;

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

                if (value == null || value.length() == 0) {
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

        ((IJBaseIndemnisationAitAaViewBean) viewBean).setAttestationJours(sb.toString());

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
        IJBaseIndemnisationAitAaViewBean correction = new IJBaseIndemnisationAitAaViewBean();

        correction.setIdBaseIndemisation(getSelectedId(request));
        mainDispatcher.dispatch(correction, getAction());
        saveViewBean(correction, request);

        return getRelativeURL(request, session) + VERS_ECRAN_RC;
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
            ((IJBaseIndemnisationAitAaViewBean) viewBean).resetCalendar();
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        return getRelativeURL(request, session) + VERS_ECRAN_DE;
    }

    public String displayFullMonthCalendar(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {
        try {
            ((IJBaseIndemnisationAitAaViewBean) viewBean).setDernierJourDuMois(true);
            ((IJBaseIndemnisationAitAaViewBean) viewBean).resetCalendar();
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        return getRelativeURL(request, session) + VERS_ECRAN_DE;
    }

}
