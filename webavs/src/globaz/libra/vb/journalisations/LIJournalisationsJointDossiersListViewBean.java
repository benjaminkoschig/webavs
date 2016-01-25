package globaz.libra.vb.journalisations;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.journalisation.db.common.access.JOCommonLoggedEntity;
import globaz.libra.db.domaines.LIDomaines;
import globaz.libra.db.dossiers.LIDossiers;
import globaz.libra.db.journalisations.LIJournalisationsJointDossiersManager;
import globaz.libra.utils.LIEcransUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

public class LIJournalisationsJointDossiersListViewBean extends LIJournalisationsJointDossiersManager implements
        FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getLibelleDossier() throws Exception {

        if (!JadeStringUtil.isBlankOrZero(getForIdDossier())) {
            LIDossiers dossier = new LIDossiers();
            dossier.setSession(getSession());
            dossier.setIdDossier(getForIdDossier());
            dossier.retrieve();

            LIDomaines domaine = new LIDomaines();
            domaine.setSession(getSession());
            domaine.setIdDomaine(dossier.getIdDomaine());
            domaine.retrieve();

            return dossier.getIdDossier() + " (" + getSession().getCodeLibelle(domaine.getCsDomaine()) + ")";
        }

        return getForIdDossier();

    }

    // Pour écran d'impression

    public String getLibelleTiers() throws Exception {

        if (!JadeStringUtil.isBlankOrZero(getForIdTiers())) {
            PRTiersWrapper tier = PRTiersHelper.getTiersAdresseParId(getSession(), getForIdTiers());
            if (null != tier) {
                return tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)
                        + " / "
                        + tier.getProperty(PRTiersWrapper.PROPERTY_NOM)
                        + " "
                        + tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM)
                        + " / "
                        + tier.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE)
                        + " / "
                        + getSession().getCodeLibelle(tier.getProperty(PRTiersWrapper.PROPERTY_SEXE))
                        + " / "
                        + LIEcransUtil.getLibellePays(tier.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE),
                                getSession());
            }
        }

        return "";

    }

    public String getLibelleUtilisateur() throws Exception {

        return getForIdUser();

    }

    public String getLibelleVueFamille() throws Exception {

        if (getIsVueFamille()) {
            return getSession().getLabel("ECRAN_IMP_VUE_FAM");
        } else {
            return "";
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.journalisation.db.journalisation.access.JOJournalisationManager #newEntity()
     */
    @Override
    protected JOCommonLoggedEntity newEntity() {
        return new LIJournalisationsJointDossiersViewBean();
    }

}
