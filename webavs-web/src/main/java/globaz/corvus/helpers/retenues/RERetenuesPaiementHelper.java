/*
 * Créé le 17 juil. 07
 */
package globaz.corvus.helpers.retenues;

import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.vb.retenues.RERetenuesPaiementViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.hera.utils.SFFamilleUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdressePaiementBanqueFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementBeneficiaireFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementCppFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.util.HashSet;
import java.util.Set;

/**
 * @author HPE
 * 
 */
public class RERetenuesPaiementHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * redefini pour renseigner les champs du viewbean qui sera affiche dans l'ecran rc.
     * 
     * <p>
     * Cherche le montant retroactif du tiers beneficiaire et obtient une adresse de paiement valide.
     * </p>
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _chercher(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        actionPreparerChercher(viewBean, action, (BSession) session);
    }

    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._init(viewBean, action, session);

        chercherIdTiersFamille(viewBean, action, session);
    }

    /**
     * redefini pour charger l'adresse de paiement.
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._retrieve(viewBean, action, session);

        chercherIdTiersFamille(viewBean, action, session);
        // charger l'adresse de paiement
        rechargerAdressePaiement((BSession) session, (RERetenuesPaiementViewBean) viewBean);
        rechargerMontantRenteAccordee(viewBean, (BSession) session);
    }

    /**
     * prepare un viewBean pour l'affichage d'informations dans la page rc de la ca page.
     * 
     * <p>
     * Cherche le montant retroactif du tiers beneficiaire et obtient une adresse de paiement valide.
     * </p>
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public FWViewBeanInterface actionPreparerChercher(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        RERetenuesPaiementViewBean rpViewBean = (RERetenuesPaiementViewBean) viewBean;

        // recharger l'adresse de paiement pour les cas ou l'adresse est
        // invalide
        rechargerAdressePaiement(session, rpViewBean);
        rechargerMontantRenteAccordee(rpViewBean, session);

        return rpViewBean;
    }

    private void chercherIdTiersFamille(FWViewBeanInterface viewBean, FWAction action, BISession session)
            throws Exception {

        RERetenuesPaiementViewBean vb = (RERetenuesPaiementViewBean) viewBean;

        // récupération des ID Tiers des membres de la famille
        Set<String> idMembreFamille = new HashSet<String>();

        Set<PRTiersWrapper> famille = SFFamilleUtils.getTiersFamilleProche(vb.getSession(),
                vb.getForIdTiersBeneficiaire());

        for (PRTiersWrapper unMembre : famille) {
            if (!JadeStringUtil.isBlank(unMembre.getIdTiers())) {
                idMembreFamille.add(unMembre.getIdTiers());
            }
        }

        vb.setIdTiersFamille(idMembreFamille);
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    /**
     * charge une adresse de paiement valide.
     * 
     * <p>
     * si les id adresse de paiment et domaine d'adresses sont renseignes, charge et formatte l'adresse correspondante,
     * sinon recherche, charge et formatte une adresse pour le tiers courant.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param raViewBean
     * 
     * @throws Exception
     */
    private void rechargerAdressePaiement(BSession session, RERetenuesPaiementViewBean rpViewBean) throws Exception {

        // si le tiers beneficiaire a change on met a jours le tiers adresse
        // paiement
        if (rpViewBean.isTiersBeneficiaireChange()) {

            rpViewBean.setIdTiersAdressePmt(rpViewBean.getIdTiersAdressePmtDepuisPyxis());
            rpViewBean.setIdDomaineApplicatif(rpViewBean.getIdDomaineApplicatifDepuisPyxis());
        }

        // si le tiers beneficiaire est null, il ne sert a rien de faire une
        // recherche
        // ce cas de figure peut survenir lors du chargement du viewBean utilise
        // dans l'ecran rc
        if (JadeStringUtil.isIntegerEmpty(rpViewBean.getIdTiersAdressePmt())) {
            return;
        }

        // charcher une adresse de paiement pour ce beneficiaire
        TIAdressePaiementData adresse = PRTiersHelper.getAdressePaiementData(session,
                session.getCurrentThreadTransaction(), rpViewBean.getIdTiersAdressePmt(),
                rpViewBean.getIdDomaineApplicatif(), rpViewBean.getIdExterne(), JACalendar.todayJJsMMsAAAA());

        rpViewBean.setAdressePaiement(adresse);

        // formatter les infos de l'adresse pour l'affichage correct dans
        // l'ecran
        if ((adresse != null) && !adresse.isNew()) {
            TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();

            source.load(adresse);

            // formatter le no de ccp ou le no bancaire
            if (JadeStringUtil.isEmpty(adresse.getCcp())) {
                rpViewBean.setCcpOuBanqueFormatte(new TIAdressePaiementBanqueFormater().format(source));
            } else {
                rpViewBean.setCcpOuBanqueFormatte(new TIAdressePaiementCppFormater().format(source));
            }

            // formatter l'adresse
            rpViewBean.setAdresseFormattee(new TIAdressePaiementBeneficiaireFormater().format(source));
        } else {
            rpViewBean.setCcpOuBanqueFormatte("");
            rpViewBean.setAdresseFormattee("");

            // si le tiers beneficiaire a change et que l'on a pas trouve
            // d'adresse
            // on enleve l'idTiersAdresseDePaiement
            if (rpViewBean.isTiersBeneficiaireChange()) {
                rpViewBean.setIdTiersAdressePmt("0");
            }
        }
    }

    private void rechargerMontantRenteAccordee(FWViewBeanInterface viewBean, BSession session) throws Exception {
        RERetenuesPaiementViewBean retenueViewBean = (RERetenuesPaiementViewBean) viewBean;

        if (JadeStringUtil.isBlankOrZero(retenueViewBean.getMontantRenteAccordee())) {
            REPrestationsAccordees prestation = new REPrestationsAccordees();
            prestation.setSession(session);
            prestation.setIdPrestationAccordee(retenueViewBean.getIdRenteAccordee());
            prestation.retrieve();

            retenueViewBean.setMontantRenteAccordee(prestation.getMontantPrestation());
        }
    }
}
