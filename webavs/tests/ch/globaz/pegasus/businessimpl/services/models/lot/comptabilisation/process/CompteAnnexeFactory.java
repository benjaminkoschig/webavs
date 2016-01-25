package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

import globaz.osiris.external.IntRole;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.InfosTiers;

public class CompteAnnexeFactory {

    public static final String COMPTE_ANNEXE_CONJOINT = "20";
    public static final String COMPTE_ANNEXE_REQUERANT = "10";

    public static final String ID_TIERS_ADRESSE_PAIEMENT_CONJOINT = "22";
    public static final String ID_TIERS_ADRESSE_PAIEMENT_REQUERANT = "11";

    public static final String ID_TIERS_CONJOINT = "2";
    public static final String ID_TIERS_DOMMAINE_APPLICATION_CONJOINT = "222";

    public static final String ID_TIERS_DOMMAINE_APPLICATION_REQUERANT = "111";
    public static final String ID_TIERS_REQUERANT = "1";

    public static CompteAnnexeSimpleModel generateCompteAnnexe(String idCompteAnnexe) {
        CompteAnnexeSimpleModel compteAnnexe = new CompteAnnexeSimpleModel();
        compteAnnexe.setIdCompteAnnexe(idCompteAnnexe);
        return compteAnnexe;
    }

    public static CompteAnnexeSimpleModel generateCompteAnnexe(String idCompteAnnexe, String idTiers) {
        CompteAnnexeSimpleModel compteAnnexe = new CompteAnnexeSimpleModel();
        compteAnnexe.setIdCompteAnnexe(idCompteAnnexe);
        compteAnnexe.setIdTiers(idTiers);
        compteAnnexe.setIdRole(IntRole.ROLE_RENTIER);
        return compteAnnexe;
    }

    public static List<CompteAnnexeSimpleModel> generateComptesAnnexes() {
        List<CompteAnnexeSimpleModel> comptesAnnexes = new ArrayList<CompteAnnexeSimpleModel>();
        comptesAnnexes.add(CompteAnnexeFactory.generateCompteAnnexe(CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT, "2"));
        comptesAnnexes.add(CompteAnnexeFactory.generateCompteAnnexe(CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT, "1"));
        comptesAnnexes.add(CompteAnnexeFactory.generateCompteAnnexe("555", "55"));
        return comptesAnnexes;
    }

    public static CompteAnnexeSimpleModel generateConjoint() {
        return CompteAnnexeFactory.generateCompteAnnexe(CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT);
    }

    public static CompteAnnexeSimpleModel generateRequerant() {
        return CompteAnnexeFactory.generateCompteAnnexe(CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT);
    }

    public static InfosTiers getInfosConjoint() {
        InfosTiers infosConjoint = new InfosTiers();
        infosConjoint.setCompteAnnexe(CompteAnnexeFactory.generateRequerant());
        infosConjoint.setIdTiers(CompteAnnexeFactory.ID_TIERS_CONJOINT);
        infosConjoint.setIdTiersAddressePaiement(CompteAnnexeFactory.ID_TIERS_ADRESSE_PAIEMENT_CONJOINT);
        infosConjoint.setIdDomaineApplication(CompteAnnexeFactory.ID_TIERS_DOMMAINE_APPLICATION_CONJOINT);
        return infosConjoint;
    }

    public static InfosTiers getInfosRequerant() {
        InfosTiers infosRequerant = new InfosTiers();
        infosRequerant.setCompteAnnexe(CompteAnnexeFactory.generateRequerant());
        infosRequerant.setIdTiers(CompteAnnexeFactory.ID_TIERS_REQUERANT);
        infosRequerant.setIdTiersAddressePaiement(CompteAnnexeFactory.ID_TIERS_ADRESSE_PAIEMENT_REQUERANT);
        infosRequerant.setIdDomaineApplication(CompteAnnexeFactory.ID_TIERS_DOMMAINE_APPLICATION_REQUERANT);
        return infosRequerant;
    }

}
