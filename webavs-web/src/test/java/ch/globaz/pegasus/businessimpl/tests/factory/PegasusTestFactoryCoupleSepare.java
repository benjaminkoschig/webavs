package ch.globaz.pegasus.businessimpl.tests.factory;

import java.util.HashMap;
import java.util.Map;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRenteAvsAi;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHome;
import ch.globaz.pegasus.businessimpl.tests.pcAccordee.DonneeForDonneeFinanciere;
import ch.globaz.pegasus.businessimpl.tests.retroAndOv.DonneeUtilsForTestRetroAndOV;

public class PegasusTestFactoryCoupleSepare extends PegasusTestFactory {

    public static Map<String, Object> factoryConjointEnHomeVersionDriotInitial(String nssRequerant,
            boolean addRenteConjoint) throws Exception {
        return PegasusTestFactoryCoupleSepare.factoryrRenteHome(nssRequerant, false, true, addRenteConjoint);
    }

    public static Map<String, Object> factoryLesDeuxEnHomVersionDriotInitial(String nssRequerant,
            boolean addRenteConjoint) throws Exception {
        return PegasusTestFactoryCoupleSepare.factoryrRenteHome(nssRequerant, true, true, addRenteConjoint);
    }

    public static Map<String, Object> factoryRequarantEnHomeVersionDriotInitial(String nssRequerant,
            boolean addRenteConjoint) throws Exception {
        return PegasusTestFactoryCoupleSepare.factoryrRenteHome(nssRequerant, true, false, addRenteConjoint);
    }

    private static Map<String, Object> factoryrRenteHome(String nssRequerant, boolean addRequerantInHome,
            boolean addConjointInHome, boolean addRenteConjoint) throws Exception {
        String dateDebutDonneFinanciere = "01.2011";
        String montantRente = "1000";
        String dateDepotDemande = "01.01.2011";
        // 4 Fondation Clair-Logis 309 - Fondation Clair-Logis 309 String idVersionDroit;
        String idHome = "309";
        String idTypeChembre = "40";
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> mapRequerant = new HashMap<String, Object>();
        Map<String, Object> mapConjoint = new HashMap<String, Object>();

        DonneeForDonneeFinanciere data = PegasusTestFactory.createDroitInitialAndUdateDonnesPersonnel(nssRequerant,
                dateDepotDemande, map);

        DroitMembreFamille droitMembreFamilleRequerant = DonneeUtilsForTestRetroAndOV
                .findDroitMembreFamilleRequerant(data.getDroit().getSimpleDroit().getIdDroit());

        DonneeUtilsForTestRetroAndOV.createRenteAvsAi(droitMembreFamilleRequerant, data.getDroit(), montantRente,
                dateDebutDonneFinanciere, IPCRenteAvsAi.CS_TYPE_RENTE_10);

        DroitMembreFamille droitMembreFamilleConjoint = null;
        if (addConjointInHome || addRenteConjoint) {
            droitMembreFamilleConjoint = DonneeUtilsForTestRetroAndOV.findDroitMembreFamilleConjoint(data.getDroit()
                    .getSimpleDroit().getIdDroit());
        }

        if (addRenteConjoint) {
            DonneeUtilsForTestRetroAndOV.createRenteAvsAi(droitMembreFamilleConjoint, data.getDroit(), montantRente,
                    dateDebutDonneFinanciere, IPCRenteAvsAi.CS_TYPE_RENTE_10);

        }

        if (addConjointInHome) {
            TaxeJournaliereHome homeConjoint = DonneeUtilsForTestRetroAndOV.createTaxeJournaliere(data.getDroit()
                    .getSimpleVersionDroit().getIdVersionDroit(), droitMembreFamilleConjoint, idHome, idTypeChembre,
                    dateDebutDonneFinanciere);
            mapConjoint.put(TaxeJournaliereHome.class.getSimpleName(), homeConjoint);
            map.put("conjoint", mapConjoint);
        }

        if (addRequerantInHome) {
            TaxeJournaliereHome homeRequerant = DonneeUtilsForTestRetroAndOV.createTaxeJournaliere(data.getDroit()
                    .getSimpleVersionDroit().getIdVersionDroit(), droitMembreFamilleRequerant, idHome, idTypeChembre,
                    dateDebutDonneFinanciere);
            mapRequerant.put(TaxeJournaliereHome.class.getSimpleName(), homeRequerant);
            map.put("requerant", mapRequerant);
        }

        return map;
    }

}
