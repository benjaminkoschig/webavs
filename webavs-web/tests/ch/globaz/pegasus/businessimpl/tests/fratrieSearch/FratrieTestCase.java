package ch.globaz.pegasus.businessimpl.tests.fratrieSearch;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import ch.globaz.pegasus.tests.util.BaseTestCase;

public class FratrieTestCase extends BaseTestCase {

    /**
     * Test doit retourner 2 décisions (28.4.2011)
     * 
     * @throws Exception
     */
    public final void testSearch() throws Exception {
        BSession session = BSessionUtil.getSessionFromThreadContext();
        // test nss barbier, pere
        PRTiersWrapper tiers = PRTiersHelper.getTiers(session, "756.3306.0397.14");

        PRTiersWrapper tiersFils = PRTiersHelper.getTiers(session, "756.7379.3718.01");
        // System.out.println(tiers.getDescription(session));
        String idTiersBarbier = tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);

        String idTiersBarbierFils = tiersFils.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);

        // int nbreResult = HeraServiceLocator.getMembreFamilleService().searchMembresFamilleRequerantDomaineRentes(
        // idTiersBarbierFils, null).length;
        // MembreFamilleVO objMembre1 = HeraServiceLocator.getMembreFamilleService().read(idMembreFamille)
        // for (MembreFamilleVO objMembre : HeraServiceLocator.getMembreFamilleService().
        // .searchMembresFamilleRequerantDomaineRentes(idTiersBarbierFils, null)) {
        //
        // }
        // recherche de la situation familiale du tiers en fonction du domaine
        ISFSituationFamiliale situationFamiliale = SFSituationFamilialeFactory.getSituationFamiliale(session,
                ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersBarbierFils);

        //
        String idTiersPourFamille = situationFamiliale.getParents(idTiersBarbierFils)[0].getIdTiers();
        // iteration sur le membres fanilles, d'apres id parents
        for (globaz.hera.api.ISFMembreFamilleRequerant membre : situationFamiliale
                .getMembresFamille(idTiersPourFamille)) {
            System.out.println(membre.getPrenom() + " " + membre.getNom());
            System.out.println(membre.getRelationAuRequerant());

        }

    }
}
