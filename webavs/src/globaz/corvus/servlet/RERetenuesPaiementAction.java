package globaz.corvus.servlet;

import globaz.corvus.api.retenues.IRERetenues;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.retenues.RERetenuesPaiement;
import globaz.corvus.db.retenues.RERetenuesPaiementManager;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.vb.retenues.RERetenuesPaiementViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.FWScenarios;
import globaz.framework.servlets.FWServlet;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.tauxImposition.PRTauxImposition;
import globaz.prestation.db.tauxImposition.PRTauxImpositionManager;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tauxImposition.api.IPRTauxImposition;
import globaz.prestation.tools.PRDateFormater;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author HPE
 */
public class RERetenuesPaiementAction extends PRDefaultAction {

    private static final String VERS_ECRAN_DE_ADD = "_de.jsp?" + PRDefaultAction.METHOD_ADD;
    private static final String VERS_ECRAN_DE_UPD = "_de.jsp?" + PRDefaultAction.METHOD_UPD;
    private static final String VERS_ECRAN_RC = "_rc.jsp";

    public RERetenuesPaiementAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // BZ 6398
        String destination = FWScenarios.getInstance().getDestination(
                (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
                new FWRequestActionAdapter().adapt(request), viewBean);
        if (JadeStringUtil.isBlank(destination)) {
            destination = getActionFullURL() + ".afficher&reloadAll=true";
        }

        return destination;
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // BZ 6398
        return _getDestAjouterSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // BZ 6398
        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        RERetenuesPaiementViewBean viewBean = (RERetenuesPaiementViewBean) this.loadViewBean(session);

        if (isRetourDepuisPyxis(viewBean)) {
            // on revient depuis pyxis on se contente de forwarder car le bon
            // viewBean est déjà en session

            // pour la prochaine fois
            viewBean.setRetourDepuisPyxis(false);

            if ((viewBean).isNew()) {
                forward(getRelativeURL(request, session) + RERetenuesPaiementAction.VERS_ECRAN_DE_ADD, request,
                        response);
            } else {
                forward(getRelativeURL(request, session) + RERetenuesPaiementAction.VERS_ECRAN_DE_UPD, request,
                        response);
            }
        } else {
            String type;
            if (viewBean.isReAfficher()) {
                type = viewBean.getCsTypeRetenue();
            } else {
                type = request.getParameter("csTypeRetenue");
            }

            if (!JadeStringUtil.isEmpty(type)) {

                String destination = getRelativeURL(request, session) + "_de.jsp";

                mainDispatcher.dispatch(viewBean, getAction());
                this.saveViewBean(viewBean, session);
                servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

            } else {
                super.actionAfficher(session, request, response, mainDispatcher);
            }
        }
    }

    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        BITransaction transaction = null;

        String idRa = request.getParameter("idRenteAccordee");
        String montantRenteAccordee = request.getParameter("montantRenteAccordee");

        RERetenuesPaiementViewBean rpViewBean = (RERetenuesPaiementViewBean) this.loadViewBean(session);

        // BZ 5270
        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, rpViewBean);
        } catch (Exception ex) {
            throw new ServletException(ex);
        }

        try {

            transaction = ((BSession) mainDispatcher.getSession()).newTransaction();

            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            String montantRetenue = rpViewBean.getMontantRetenuMensuelSpecial(montantRenteAccordee);

            FWCurrency montant = new FWCurrency("0.00");
            montant.add(montantRetenue);

            if (montant.isPositive() && !montant.isZero()) {
                RERenteAccordee ra = new RERenteAccordee();
                ra.setId(idRa);
                ra.setSession(rpViewBean.getSession());
                ra.retrieve(transaction);

                REPrestationsAccordees pr = new REPrestationsAccordees();
                pr.setSession(rpViewBean.getSession());
                pr.setIdPrestationAccordee(ra.getIdPrestationAccordee());
                pr.retrieve(transaction);

                pr.setIsRetenues(Boolean.TRUE);
                pr.save(transaction);
            }
        } catch (Exception e) {

            transaction.setRollbackOnly();
            rpViewBean.setMessage("Error " + e.getMessage());
            rpViewBean.setMsgType(FWViewBeanInterface.ERROR);
        } finally {
            try {
                if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                    if (transaction.hasErrors()) {
                        rpViewBean.setMessage(transaction.getErrors().toString());
                        rpViewBean.setMsgType(FWViewBeanInterface.ERROR);
                    }
                    transaction.rollback();
                } else {
                    transaction.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
                e.toString();
            } finally {
                try {
                    transaction.closeTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        super.actionAjouter(session, request, response, mainDispatcher);
    }

    /**
     * redefinition pour charger les informations qui doivent s'afficher dans l'ecran rc.
     * 
     * <p>
     * cette methode inspecte la session pour savoir si l'on revient depuis pyxis. Si c'est le cas, l'ancien viewBean
     * est conserve dans la session et il sera reutilise pour l'action afficher.
     * </p>
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        FWViewBeanInterface viewBean = this.loadViewBean(session);
        RERetenuesPaiementViewBean rpViewBean;

        if (isRetourDepuisPyxis(viewBean)) {
            // on revient de pyxis, on recupere le viewBean en session pour
            // afficher les donnees dans la page rc.
            rpViewBean = (RERetenuesPaiementViewBean) viewBean;

        } else {
            /*
             * on affiche cette page par un chemin habituel, dans ce cas tout est normal, mise a part que l'on met un
             * viewBean dans la session qui contient les donnees qui doivent s'afficher dans le cadre rc de la ca page.
             */
            rpViewBean = new RERetenuesPaiementViewBean();

            if (viewBean instanceof RERetenuesPaiementViewBean) {
                rpViewBean = (RERetenuesPaiementViewBean) viewBean;
            }
            try {
                JSPUtils.setBeanProperties(request, rpViewBean);
            } catch (Exception e) {
                rpViewBean.setMessage(e.getMessage());
                rpViewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        // on appelle le helper qui va charger les creanciers qui doivent
        // s'afficher
        mainDispatcher.dispatch(rpViewBean, getAction());

        /*
         * on sauve de toutes facons le viewBean dans la requete meme s'il est deja en session car c'est la que la page
         * rc va le rechercher.
         */
        this.saveViewBean(rpViewBean, request);
        this.saveViewBean(rpViewBean, session);
        forward(getRelativeURL(request, session) + RERetenuesPaiementAction.VERS_ECRAN_RC, request, response);

    }

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        BITransaction transaction = null;

        String idRa = request.getParameter("idRenteAccordee");
        String montantRenteAccordee = request.getParameter("montantRenteAccordee");

        boolean hasMontantSup = false;

        RERetenuesPaiementViewBean rpViewBean = (RERetenuesPaiementViewBean) this.loadViewBean(session);

        try {

            transaction = ((BSession) mainDispatcher.getSession()).newTransaction();

            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            RERetenuesPaiementManager rpm = new RERetenuesPaiementManager();
            rpm.setSession(rpViewBean.getSession());
            rpm.setForIdRenteAccordee(idRa);
            rpm.find(transaction);

            for (int i = 0; i < rpm.size(); i++) {
                RERetenuesPaiement rp = (RERetenuesPaiement) rpm.get(i);
                if (rpViewBean.getIdRetenue().equals(rp.getIdRetenue())) {
                    FWCurrency montant = new FWCurrency("0.00");
                    montant.add(rpViewBean.getMontantRetenuMensuelSpecial(montantRenteAccordee));
                    if (montant.isPositive() && !montant.isZero()) {
                        hasMontantSup = true;
                    }
                } else {
                    FWCurrency montant = new FWCurrency("0.00");
                    montant.add(getMontantRetenuMensuelSpecial(montantRenteAccordee, rp));
                    if (montant.isPositive() && !montant.isZero()) {
                        hasMontantSup = true;
                    }
                }
            }

            if (!hasMontantSup) {
                RERenteAccordee ra = new RERenteAccordee();
                ra.setId(idRa);
                ra.setSession(rpViewBean.getSession());
                ra.retrieve(transaction);

                REPrestationsAccordees pr = new REPrestationsAccordees();
                pr.setSession(rpViewBean.getSession());
                pr.setIdPrestationAccordee(ra.getIdPrestationAccordee());
                pr.retrieve(transaction);

                pr.setIsRetenues(Boolean.FALSE);
                pr.save(transaction);
            } else {
                RERenteAccordee ra = new RERenteAccordee();
                ra.setId(idRa);
                ra.setSession(rpViewBean.getSession());
                ra.retrieve(transaction);

                REPrestationsAccordees pr = new REPrestationsAccordees();
                pr.setSession(rpViewBean.getSession());
                pr.setIdPrestationAccordee(ra.getIdPrestationAccordee());
                pr.retrieve(transaction);

                pr.setIsRetenues(Boolean.TRUE);
                pr.save(transaction);
            }

        } catch (Exception e) {

            transaction.setRollbackOnly();
            rpViewBean.setMessage("Error " + e.getMessage());
            rpViewBean.setMsgType(FWViewBeanInterface.ERROR);
        } finally {
            try {
                if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                    if (transaction.hasErrors()) {
                        rpViewBean.setMessage(transaction.getErrors().toString());
                        rpViewBean.setMsgType(FWViewBeanInterface.ERROR);
                    }
                    transaction.rollback();
                } else {
                    transaction.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
                e.toString();
            } finally {
                try {
                    transaction.closeTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        super.actionModifier(session, request, response, mainDispatcher);
    }

    /**
     * ecrase une des valeurs sauvee dans la session par FWSelectorTag de telle sorte que l'on sache exactement quelle
     * action sera executee lorsque l'on revient de pyxis et avec quels parametres.
     */
    @Override
    protected void actionSelectionner(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        // recuperer les id et genre de service du droit pour le retour depuis
        // pyxis
        RERetenuesPaiementViewBean rpViewBean = (RERetenuesPaiementViewBean) this.loadViewBean(session);

        StringBuffer queryString = new StringBuffer();

        queryString.append(PRDefaultAction.USER_ACTION);
        queryString.append("=");
        queryString.append(IREActions.ACTION_RETENUES_SUR_PMT);
        queryString.append(".");
        queryString.append(FWAction.ACTION_CHERCHER);
        queryString.append("&idTierRequerant=");
        queryString.append(rpViewBean.getForIdTiersBeneficiaire());
        queryString.append("&idRenteAccordee=");
        queryString.append(rpViewBean.getForIdRenteAccordee());
        queryString.append("&montantRenteAccordee=");
        queryString.append(rpViewBean.getMontantRenteAccordee());

        rpViewBean.setRetourDepuisPyxis(true);

        this.saveViewBean(rpViewBean, session);

        // HACK: on remplace une des valeurs sauvee en session par FWSelectorTag
        session.setAttribute(FWDefaultServletAction.ATTRIBUT_SELECTOR_CUSTOMERURL, queryString.toString());

        // comportement par defaut
        super.actionSelectionner(session, request, response, mainDispatcher);
    }

    @Override
    protected void actionSupprimer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        BITransaction transaction = null;

        String idRa = request.getParameter("idRenteAccordee");
        String montantRenteAccordee = request.getParameter("montantRenteAccordee");

        boolean hasMontantSup = false;

        RERetenuesPaiementViewBean rpViewBean = (RERetenuesPaiementViewBean) this.loadViewBean(session);

        try {

            transaction = ((BSession) mainDispatcher.getSession()).newTransaction();

            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            RERetenuesPaiementManager rpm = new RERetenuesPaiementManager();
            rpm.setSession(rpViewBean.getSession());
            rpm.setForIdRenteAccordee(idRa);
            rpm.find(transaction);

            for (int i = 0; i < rpm.size(); i++) {
                RERetenuesPaiement rp = (RERetenuesPaiement) rpm.get(i);
                if (rpViewBean.getIdRetenue().equals(rp.getIdRetenue())) {
                    continue;
                } else {
                    FWCurrency montant = new FWCurrency("0.00");
                    montant.add(getMontantRetenuMensuelSpecial(montantRenteAccordee, rp));
                    if (montant.isPositive() && !montant.isZero()) {
                        hasMontantSup = true;
                    }
                }
            }

            if (!hasMontantSup) {
                RERenteAccordee ra = new RERenteAccordee();
                ra.setId(idRa);
                ra.setSession(rpViewBean.getSession());
                ra.retrieve(transaction);

                REPrestationsAccordees pr = new REPrestationsAccordees();
                pr.setSession(rpViewBean.getSession());
                pr.setIdPrestationAccordee(ra.getIdPrestationAccordee());
                pr.retrieve(transaction);

                pr.setIsRetenues(Boolean.FALSE);
                pr.save(transaction);
            }

        } catch (Exception e) {

            transaction.setRollbackOnly();
            rpViewBean.setMessage("Error " + e.getMessage());
            rpViewBean.setMsgType(FWViewBeanInterface.ERROR);
        } finally {
            try {
                if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                    if (transaction.hasErrors()) {
                        rpViewBean.setMessage(transaction.getErrors().toString());
                        rpViewBean.setMsgType(FWViewBeanInterface.ERROR);
                    }
                    transaction.rollback();
                } else {
                    transaction.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
                e.toString();
            } finally {
                try {
                    transaction.closeTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        super.actionSupprimer(session, request, response, mainDispatcher);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return viewBean;
    }

    public String getMontantRetenuMensuelSpecial(String montantRA, RERetenuesPaiement rp) throws JAException {

        // Voir si le montant déjà retenu atteint le montant total à retenir ou
        // si le montant à retenir n'est pas plus grand que la différence des 2
        // premiers.

        FWCurrency montantARetenir = new FWCurrency(rp.getMontantRetenuMensuel());
        FWCurrency montantTotalARetenir = new FWCurrency(rp.getMontantTotalARetenir());
        FWCurrency montantDejaRetenu = new FWCurrency(rp.getMontantDejaRetenu());

        // si sur adresse paiement

        if (rp.getCsTypeRetenue().equals(IRERetenues.CS_TYPE_ADRESSE_PMT)
                || rp.getCsTypeRetenue().equals(IRERetenues.CS_TYPE_COMPTE_SPECIAL)
                || rp.getCsTypeRetenue().equals(IRERetenues.CS_TYPE_FACTURE_FUTURE)
                || rp.getCsTypeRetenue().equals(IRERetenues.CS_TYPE_FACTURE_EXISTANTE)) {

            if (montantDejaRetenu.floatValue() >= montantTotalARetenir.floatValue()) {
                return "0.00";
            } else if (montantARetenir.floatValue() > (montantTotalARetenir.floatValue() - montantDejaRetenu
                    .floatValue())) {
                return new FWCurrency(montantTotalARetenir.floatValue() - montantDejaRetenu.floatValue()).toString();
            } else {
                return montantARetenir.toString();
            }

        } else if (rp.getCsTypeRetenue().equals(IRERetenues.CS_TYPE_IMPOT_SOURCE)) {

            FWCurrency montantImpoSource = null;

            if (!JadeStringUtil.isDecimalEmpty(rp.getTauxImposition())) {
                montantImpoSource = new FWCurrency((new FWCurrency(montantRA).floatValue() / 100)
                        * (new FWCurrency(rp.getTauxImposition())).floatValue());
                montantImpoSource.round(FWCurrency.ROUND_ENTIER);
            } else if (!JadeStringUtil.isDecimalEmpty(rp.getMontantRetenuMensuel())) {

                montantImpoSource = new FWCurrency(rp.getMontantRetenuMensuel());

            } else {

                // recherche du taux
                PRTauxImpositionManager tManager = new PRTauxImpositionManager();
                tManager.setSession(rp.getSession());
                tManager.setForCsCanton(rp.getCantonImposition());
                tManager.setForTypeImpot(IPRTauxImposition.CS_TARIF_D);

                JADate dateDebut = new JADate(REPmtMensuel.getDateDernierPmt(rp.getSession()));
                JADate dateFin = new JADate(REPmtMensuel.getDateDernierPmt(rp.getSession()));

                tManager.setForPeriode(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(dateDebut.toStrAMJ()),
                        PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(dateFin.toStrAMJ()));
                try {
                    tManager.find();

                    if (tManager.size() > 0) {
                        PRTauxImposition taux = (PRTauxImposition) tManager.getFirstEntity();

                        montantImpoSource = new FWCurrency((new FWCurrency(montantRA).floatValue() / 100)
                                * (new FWCurrency(taux.getTaux())).floatValue());
                        montantImpoSource.round(FWCurrency.ROUND_ENTIER);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    montantImpoSource = new FWCurrency("0.00");
                }

                return montantImpoSource.toString();
            }

            return montantImpoSource.toString();

        } else {
            return "";
        }

    }

    private boolean isRetourDepuisPyxis(FWViewBeanInterface viewBean) {
        return ((viewBean != null) && (viewBean instanceof RERetenuesPaiementViewBean) && ((RERetenuesPaiementViewBean) viewBean)
                .isRetourDepuisPyxis());
    }
}
