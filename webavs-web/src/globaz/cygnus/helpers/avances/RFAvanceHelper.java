package globaz.cygnus.helpers.avances;

import globaz.corvus.utils.RETiersForJspUtils;
import globaz.corvus.vb.rentesaccordees.REAdressePmtUtil;
import globaz.cygnus.vb.avances.RFAvanceViewBean;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.hera.utils.SFFamilleUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdressePaiementBanqueFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementBeneficiaireFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementCppFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper pour la gestion des avances cygnus (paiement uniques et listes)
 * --> Mandat INFOROM547
 * 
 * @author sce
 * 
 */
public class RFAvanceHelper extends PRAbstractHelper {

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._retrieve(viewBean, action, session);

        RFAvanceViewBean vb = (RFAvanceViewBean) viewBean;
        vb.setSession((BSession) session);

        chargerNumeroAffilie((BSession) session, vb);
        chargerDetailBeneficiaire((BSession) session, vb);
        chargerAdressePaiememtMembresFamille((BSession) session, vb);
    }

    private void chargerAdressePaiememtMembresFamille(BSession session, RFAvanceViewBean viewBean) throws Exception {

        Map<String, REAdressePmtUtil> mapAdresses = new HashMap<String, REAdressePmtUtil>();

        for (PRTiersWrapper unMembreFamille : SFFamilleUtils.getTiersFamilleProche(session,
                viewBean.getIdTiersBeneficiaire())) {
            // chercher une adresse de paiement pour ce bénéficiaire
            TIAdressePaiementData adresse = PRTiersHelper.getAdressePaiementData(session,
                    session.getCurrentThreadTransaction(), unMembreFamille.getIdTiers(),
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", JACalendar.todayJJsMMsAAAA());

            REAdressePmtUtil adpmt = new REAdressePmtUtil();

            if ((adresse != null) && !adresse.isNew()) {
                TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();

                source.load(adresse);

                // formatter le no de ccp ou le no bancaire
                if (JadeStringUtil.isEmpty(adresse.getCcp())) {
                    adpmt.setCcpOuBanqueFormatte(new TIAdressePaiementBanqueFormater().format(source));
                } else {
                    adpmt.setCcpOuBanqueFormatte(new TIAdressePaiementCppFormater().format(source));
                }

                // formatter l'adresse
                adpmt.setAdresseFormattee(new TIAdressePaiementBeneficiaireFormater().format(source));

                adpmt.setIdTiers(unMembreFamille.getIdTiers());
                adpmt.setNom(unMembreFamille.getProperty(PRTiersWrapper.PROPERTY_NOM));
                adpmt.setPrenom(unMembreFamille.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
                adpmt.setCsDomaine(adresse.getIdApplication());
                mapAdresses.put(unMembreFamille.getIdTiers(), adpmt);
            }
        }

        viewBean.setMapAdrPmtMbresFamille(mapAdresses);
    }

    private void chargerAdressePaiement(BSession session, RFAvanceViewBean viewBean) throws Exception {

        // chercher une adresse de paiement pour ce bénéficiaire

        if (JadeStringUtil.isBlankOrZero(viewBean.getCsDomaine())) {
            viewBean.setCsDomaine(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
        }

        String idTiers = viewBean.getIdTiersBeneficiaire();
        // Si adresse paiement ok
        if (!JadeStringUtil.isBlank(viewBean.getIdTiersAdpmt())) {
            idTiers = viewBean.getIdTiersAdpmt();
        }

        TIAdressePaiementData adresse = PRTiersHelper.getAdressePaiementData(session,
                session.getCurrentThreadTransaction(), idTiers, viewBean.getCsDomaine(), "",
                JACalendar.todayJJsMMsAAAA());
        viewBean.setCsDomaine(adresse.getIdApplication());

        REAdressePmtUtil adressePaiementUtils = new REAdressePmtUtil();
        if ((adresse != null) && !adresse.isNew()) {
            TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();
            source.load(adresse);
            // formatter le no de ccp ou le no bancaire
            if (JadeStringUtil.isEmpty(adresse.getCcp())) {
                adressePaiementUtils.setCcpOuBanqueFormatte(new TIAdressePaiementBanqueFormater().format(source));
            } else {
                adressePaiementUtils.setCcpOuBanqueFormatte(new TIAdressePaiementCppFormater().format(source));
            }
            // formatter l'adresse
            adressePaiementUtils.setAdresseFormattee(new TIAdressePaiementBeneficiaireFormater().format(source));
            adressePaiementUtils.setNom(viewBean.getNom());
            adressePaiementUtils.setPrenom(viewBean.getPrenom());
        }
        viewBean.setAdpmt(adressePaiementUtils);
    }

    private void chargerDetailBeneficiaire(BSession session, RFAvanceViewBean viewBean) throws Exception {

        PRTiersWrapper tiersBeneficiaire = PRTiersHelper.getTiersParId(session, viewBean.getIdTiersBeneficiaire());

        String info = "";
        if (tiersBeneficiaire != null) {
            info = RETiersForJspUtils.getInstance(session).getDetailsTiers(tiersBeneficiaire);
        }
        viewBean.setInfoDuTiersFormatted(info);

        viewBean.setTiersBeneficiaireWrapper(tiersBeneficiaire);

        chargerAdressePaiement(session, viewBean);
        viewBean.setIdTiersAdrPmt(tiersBeneficiaire.getIdTiers());
    }

    private void chargerNumeroAffilie(BSession session, RFAvanceViewBean viewBean) {
        try {
            IPRAffilie affilie = PRAffiliationHelper.getEmployeurParIdAffilie(session,
                    session.getCurrentThreadTransaction(), viewBean.getIdAffilie(), viewBean.getIdTiersAdrPmt());

            if (affilie != null) {
                viewBean.setNumAffilie(affilie.getNumAffilie());
            }
        } catch (Exception e) {
            String message = session.getLabel("EMPLOYEUR_INTROUVABLE");

            JadeLogger.warn(this, e.getMessage() + " : " + message);

            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(message);
        }
    }

}
