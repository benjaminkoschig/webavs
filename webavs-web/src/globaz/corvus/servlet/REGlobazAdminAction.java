/*
 * Créé le 8 nov. 05
 */
package globaz.corvus.servlet;

import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteManager;
import globaz.corvus.db.rentesaccordees.RERentesAccordeesJoinDemRente;
import globaz.corvus.db.rentesaccordees.RERentesAccordeesJoinDemRenteManager;
import globaz.corvus.vb.REDummyViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRDateFormater;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class REGlobazAdminAction extends PRDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJAnnonceAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public REGlobazAdminAction(FWServlet servlet) {
        super(servlet);
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
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // return getRelativeURLwithoutClassPart(request, session) +
        // "annonce_rc.jsp";
        return super._getDestAjouterSucces(session, request, response, viewBean);
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
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // return getRelativeURLwithoutClassPart(request, session) +
        // "annonce_rc.jsp";
        return super._getDestModifierSucces(session, request, response, viewBean);
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
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // return getRelativeURLwithoutClassPart(request, session) +
        // "annonce_rc.jsp";
        return super._getDestSupprimerSucces(session, request, response, viewBean);
    }

    private String getLastDateDiminutionDemande(BSession session, BTransaction transaction, REDemandeRente demande)
            throws Exception {

        // Est-ce que toutes les RA de cette demande sont diminuées ?
        RERentesAccordeesJoinDemRenteManager mgr = new RERentesAccordeesJoinDemRenteManager();
        mgr.setForNoDemandeRente(demande.getIdDemandeRente());
        mgr.setSession(session);
        mgr.find(transaction, BManager.SIZE_NOLIMIT);

        JADate plusGrandeDateDiminution = new JADate("01.01.1900");
        JACalendar cal = new JACalendarGregorian();

        if (mgr.size() == 0) {
            return null;
        }
        for (int i = 0; i < mgr.size(); i++) {
            RERentesAccordeesJoinDemRente elm = (RERentesAccordeesJoinDemRente) mgr.getEntity(i);

            if (JadeStringUtil.isBlankOrZero(elm.getDateFinDroit())) {
                return null;
            }

            else {
                JADate date = new JADate(elm.getDateFinDroit());
                if (cal.compare(date, plusGrandeDateDiminution) == JACalendar.COMPARE_FIRSTUPPER) {
                    plusGrandeDateDiminution = new JADate(date.toStr("."));
                }
            }
        }

        int dayInMonth = cal.daysInMonth(plusGrandeDateDiminution.getMonth(), plusGrandeDateDiminution.getYear());

        // Si on arrive ici, toute les RA ont été diminuées...
        return String.valueOf(dayInMonth) + "."
                + PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(plusGrandeDateDiminution.toStrAMJ());
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     * 
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
     * 
     * 
     * 
     * @see bz-4397
     * 
     *      Appel a ce traitement via l'url.... http://serveurName/webavs/corvus?
     *      userAction=corvus.globaz.administrator.majDateFinDemandeRente
     * 
     *      résultat dans fichier log : sous cette forme :
     *      -id-id-id(28.02.2008/29.02.2008)-id-id-id-id-ERR:...........-id seul les id(*) sont mis à jours. les champs
     *      id correspondent aux id des demandes de rentes.
     * 
     *      résultat dans error_page.jsp : -id(28.02.2008/29.02.2008) Seul les demandes modifiées sont affichées format
     *      : -[idDemandeRente]([ancienne date de fin]/[nouvelle date de fin]).
     * 
     * 
     * 
     */

    public void majDateFinDemandeRente(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        viewBean = new REDummyViewBean();

        String destination = "";
        BTransaction transaction = null;
        BStatement statement = null;

        StringBuffer sb = new StringBuffer("Resultats.....\n");
        try {
            BSession bSession = (BSession) mainDispatcher.getSession();

            transaction = (BTransaction) bSession.newTransaction();
            transaction.openTransaction();

            REDemandeRenteManager mgr = new REDemandeRenteManager();
            mgr.setSession(bSession);
            statement = mgr.cursorOpen(transaction);
            REDemandeRente dem = null;

            JACalendar cal = new JACalendarGregorian();

            while ((dem = (REDemandeRente) mgr.cursorReadNext(statement)) != null) {
                try {

                    // do traitement
                    // Format : jj.mm.aaaa
                    String dateDiminution = getLastDateDiminutionDemande(bSession, transaction, dem);

                    JADate dDim = null;
                    if (!JadeStringUtil.isBlankOrZero(dateDiminution)) {
                        dDim = new JADate(dateDiminution);
                    }

                    // Format : jj.mm.aaaa
                    JADate dFin = null;

                    if (!JadeStringUtil.isBlankOrZero(dem.getDateFin())) {
                        dFin = new JADate(dem.getDateFin());
                    }

                    if (dDim != null) {
                        // On fait la mise a jours
                        if (dFin == null) {
                            dem.setDateFin(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(dDim.toStrAMJ()));
                            dem.update(transaction);
                            String s = "-" + dem.getIdDemandeRente() + "(null/" + dDim.toStr(".") + ")";
                            System.out.println(s);
                            sb.append(s);

                        } else {
                            if (cal.compare(dFin, dDim) == JACalendar.COMPARE_EQUALS) {
                                String s = "-" + dem.getIdDemandeRente();
                                System.out.println(s);
                                // sb.append(s);

                            } else {
                                dem.setDateFin(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(dDim.toStrAMJ()));
                                dem.update(transaction);
                                String s = "-" + dem.getIdDemandeRente() + "(" + dFin.toStr(".") + "/"
                                        + dDim.toStr(".") + ")";
                                System.out.println(s);
                                sb.append(s);
                            }
                        }
                    }

                    if (transaction.hasErrors()) {
                        sb.append("ERR:" + dem.getIdDemandeRente() + "(" + dem.getDateDebut() + "/" + dem.getDateFin()
                                + ")");
                        throw new Exception(transaction.getErrors().toString());
                    }

                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                    transaction.clearErrorBuffer();
                    sb.append("ERR:" + dem.getIdDemandeRente() + "(" + dem.getDateDebut() + "/" + dem.getDateFin()
                            + ")");
                    e.printStackTrace();
                }
            }

            viewBean.setMessage(sb.toString());
            this.saveViewBean(viewBean, request);
            this.saveViewBean(viewBean, session);
            destination = FWDefaultServletAction.ERROR_PAGE;

        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception e1) {
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                if (statement != null) {
                    statement.closeStatement();
                }
            } finally {
                if (transaction != null) {
                    try {
                        transaction.closeTransaction();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

}
