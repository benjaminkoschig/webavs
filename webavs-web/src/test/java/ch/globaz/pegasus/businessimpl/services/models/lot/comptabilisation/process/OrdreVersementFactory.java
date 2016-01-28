package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

import globaz.corvus.api.ordresversements.IREOrdresVersements;
import org.apache.commons.lang.RandomStringUtils;
import ch.globaz.pegasus.business.constantes.IPCOrdresVersements;
import ch.globaz.pegasus.business.models.lot.OrdreVersementForList;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.OrdreVersement;

public class OrdreVersementFactory {

    public static OrdreVersement generateAllocationNoel(String montant, String idTiers) {
        OrdreVersement ov = new OrdreVersement("3", IREOrdresVersements.CS_TYPE_ALLOCATION_NOEL,
                IPCOrdresVersements.CS_DOMAINE_AI, null, idTiers, "1234", null, null, montant, null, "13", null, null);
        return ov;
    }

    private static OrdreVersementForList generateBaseOvList() {
        OrdreVersementForList ovList = new OrdreVersementForList();
        ovList.setMontantPresation("2000");
        ovList.setIdTiersRequerant("1");
        return ovList;
    }

    public static OrdreVersement generateBeneficiaire(String montant, String idTiers) {
        OrdreVersement ov = new OrdreVersement("2", IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL,
                IPCOrdresVersements.CS_DOMAINE_AI, null, idTiers, "1234", null, null, montant, null, "13", null, null);
        return ov;
    }

    public static OrdreVersement generateBeneficiaireConjoint(String montant, String idTiers) {
        OrdreVersement ov = new OrdreVersement("3", IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL,
                IPCOrdresVersements.CS_DOMAINE_AI, null, idTiers, "1234", null, null, montant, null, "13", null, null);
        return ov;
    }

    public static OrdreVersement generateCreancier(String montant, String idTiers, String idTiersOwnerDetteCreance) {
        OrdreVersement ov = new OrdreVersement("15", IREOrdresVersements.CS_TYPE_TIERS, null, null, idTiers, null,
                null, idTiersOwnerDetteCreance, montant, null, "13", null, null);
        return ov;
    }

    public static OrdreVersement generateDette(String montant, String idSection, String idTiersOwnerDetteCreance) {
        OrdreVersement ov = new OrdreVersement("2", IREOrdresVersements.CS_TYPE_DETTE, null, idSection, null, null,
                null, idTiersOwnerDetteCreance, montant, null, "13", null, null);

        return ov;
    }

    public static OrdreVersementForList generateOvAllocationNoel(String noGroupe) {
        OrdreVersementForList ovList = OrdreVersementFactory.generateBaseOvList();
        ovList.setSimpleOrdreVersement(OrdreVersementFactory.generateSimpleAllocationNoel("2000", "123", noGroupe));
        return ovList;
    }

    public static OrdreVersementForList generateOvListBeneficiaire(String noGroupe) {
        OrdreVersementForList ovList = OrdreVersementFactory.generateOvListBeneficiaire(noGroupe, "2000");
        return ovList;
    }

    public static OrdreVersementForList generateOvListBeneficiaire(String noGroupe, String montant) {
        return OrdreVersementFactory.generateOvListBeneficiaire(noGroupe, montant, null);
    }

    public static OrdreVersementForList generateOvListBeneficiaire(String noGroupe, String montant, String idSection) {
        OrdreVersementForList ovList = OrdreVersementFactory.generateBaseOvList();
        ovList.setSimpleOrdreVersement(OrdreVersementFactory.generateSimpleBeneficiaire(montant, "1", noGroupe));
        ovList.setIdCompteAnnexeRequerant(CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT);
        ovList.setId("111");
        ovList.getSimpleOrdreVersement().setIdSection(idSection);
        ovList.setMontantPresation(montant);
        ovList.setDesignationRequerant1("Nom");
        ovList.setDesignationRequerant2("Prenom");
        ovList.setNumAvs("756.000.000.000");
        ovList.getSimpleOrdreVersement().setIdTiersAdressePaiement(
                CompteAnnexeFactory.ID_TIERS_ADRESSE_PAIEMENT_REQUERANT);
        ovList.getSimpleOrdreVersement().setIdDomaineApplication("111");
        return ovList;
    }

    public static OrdreVersementForList generateOvListBeneficiaireDom2R(String noGroupe, String montant) {
        OrdreVersementForList ovList = OrdreVersementFactory.generateBaseOvList();
        ovList.setSimpleOrdreVersement(OrdreVersementFactory.generateSimpleBeneficiaire(montant, "1", noGroupe));
        ovList.setIdCompteAnnexeRequerant(CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT);
        ovList.setId("111");
        ovList.setMontantPresation(montant);
        ovList.setDesignationRequerant1("Nom");
        ovList.setDesignationRequerant2("Prenom");
        ovList.setNumAvs("756.000.000.000");
        ovList.getSimpleOrdreVersement().setIdTiersAdressePaiement(
                CompteAnnexeFactory.ID_TIERS_ADRESSE_PAIEMENT_REQUERANT);
        ovList.getSimpleOrdreVersement().setIdDomaineApplication("111");
        ovList.getSimpleOrdreVersement().setIdTiersAdressePaiementConjoint("22");
        ovList.getSimpleOrdreVersement().setIdDomaineApplicationConjoint("222");
        return ovList;
    }

    public static OrdreVersementForList generateOvListBeneficiaireSpecifiedIdPca(String noGroupe, String idPca) {
        OrdreVersementForList ov = OrdreVersementFactory.generateOvListBeneficiaire(noGroupe);
        ov.getSimpleOrdreVersement().setIdPca(idPca);
        return ov;
    }

    public static OrdreVersementForList generateOvListConjoint(String noGroupe) {
        return OrdreVersementFactory.generateOvListConjointBeneficiaire(noGroupe, "1000");
    }

    public static OrdreVersementForList generateOvListConjointBeneficiaire(String noGroupe, String montant) {
        return OrdreVersementFactory.generateOvListConjointBeneficiaire(noGroupe, montant, null);
    }

    public static OrdreVersementForList generateOvListConjointBeneficiaire(String noGroupe, String montant,
            String idSection) {
        OrdreVersementForList ovList = OrdreVersementFactory.generateBaseOvList();
        ovList.setSimpleOrdreVersement(OrdreVersementFactory.generateSimpleBeneficiaire(montant, "2", noGroupe));
        ovList.getSimpleOrdreVersement().setIdTiersAdressePaiement("22");
        ovList.getSimpleOrdreVersement().setIdSection(idSection);
        ovList.setIdCompteAnnexeRequerant(CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT);
        ovList.setIdCompteAnnexeConjoint(CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT);
        return ovList;
    }

    public static OrdreVersementForList generateOvListConjointRestitution(String noGroupe, String montant) {
        OrdreVersementForList ovList = OrdreVersementFactory.generateBaseOvList();
        ovList.setSimpleOrdreVersement(OrdreVersementFactory.generateSimpleRestitution(montant, "2", noGroupe));
        ovList.setIdCompteAnnexeRequerant(CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT);
        ovList.setIdCompteAnnexeConjoint(CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT);
        return ovList;
    }

    public static OrdreVersementForList generateOvListCreancier(String montant, String idTiers,
            String idTiersOwnerDetteCreance) {
        OrdreVersementForList ovList = OrdreVersementFactory.generateBaseOvList();
        ovList.setSimpleOrdreVersement(OrdreVersementFactory.generateSimpleCreancier(montant, idTiers,
                idTiersOwnerDetteCreance, "25"));
        return ovList;
    }

    public static OrdreVersementForList generateOvListDette(String montant, String idSectionDette,
            String idTiersOwnerDetteCreance) {
        return OrdreVersementFactory.generateOvListDette(montant, idSectionDette, idTiersOwnerDetteCreance, null);
    }

    public static OrdreVersementForList generateOvListDette(String montant, String idSectionDette,
            String idTiersOwnerDetteCreance, String idSection) {
        OrdreVersementForList ovList = OrdreVersementFactory.generateBaseOvList();
        ovList.setSimpleOrdreVersement(OrdreVersementFactory.generateSimpleDetteCompta(montant, idSectionDette,
                idTiersOwnerDetteCreance));
        ovList.getSimpleOrdreVersement().setIdSection(idSection);
        return ovList;
    }

    public static OrdreVersementForList generateOvListJourAppoint(String montant, String idPca, String noGroupe) {
        OrdreVersementForList ovList = OrdreVersementFactory.generateBaseOvList();
        ovList.setSimpleOrdreVersement(OrdreVersementFactory.generateSimpleJourAppoints(montant, idPca, noGroupe));
        return ovList;
    }

    public static OrdreVersementForList generateOvListRestitution(String noGroupe) {
        return OrdreVersementFactory.generateOvListRestitution(noGroupe, "500");
    }

    public static OrdreVersementForList generateOvListRestitution(String noGroupe, String montant) {
        OrdreVersementForList ovList = OrdreVersementFactory.generateBaseOvList();
        ovList.setSimpleOrdreVersement(OrdreVersementFactory.generateSimpleRestitution(montant, "1", noGroupe));
        ovList.setIdCompteAnnexeRequerant(CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT);
        return ovList;
    }

    public static OrdreVersementForList generateOvListRestitutionConjoint(String noGroupe) {
        return OrdreVersementFactory.generateOvListConjointRestitution(noGroupe, "1000");
    }

    public static OrdreVersementForList generateOvListRestitutionDom2R(String noGroupe, String montant) {
        OrdreVersementForList ovList = OrdreVersementFactory.generateBaseOvList();
        ovList.setSimpleOrdreVersement(OrdreVersementFactory.generateSimpleRestitution(montant, "1", noGroupe));
        ovList.setIdCompteAnnexeRequerant(CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT);
        ovList.getSimpleOrdreVersement().setIdTiersAdressePaiement(
                CompteAnnexeFactory.ID_TIERS_ADRESSE_PAIEMENT_REQUERANT);
        ovList.getSimpleOrdreVersement().setIdDomaineApplication("111");
        ovList.getSimpleOrdreVersement().setIdTiersAdressePaiementConjoint("22");
        ovList.getSimpleOrdreVersement().setIdDomaineApplicationConjoint("222");
        return ovList;
    }

    public static OrdreVersementForList generateOvListRestitutionSpecifiedIdPca(String noGroupe, String idPca) {
        OrdreVersementForList ov = OrdreVersementFactory.generateOvListRestitution(noGroupe);
        ov.getSimpleOrdreVersement().setIdPca(idPca);
        return ov;
    }

    public static OrdreVersement generateRestitution(String montant, String idTiers) {
        OrdreVersement ov = new OrdreVersement("10", IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION, null, null,
                idTiers, "222", null, null, montant, null, "13", null, null);
        return ov;
    }

    public static OrdreVersement generateRestitutionConjoint(String montant, String idTiers) {
        OrdreVersement ov = new OrdreVersement("20", IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION, null, null,
                idTiers, "222", null, null, montant, null, "13", null, null);
        return ov;
    }

    public static SimpleOrdreVersement generateSimpleAllocationNoel(String montant, String idTiers, String noGroupe) {
        SimpleOrdreVersement ov = new SimpleOrdreVersement();
        ov.setIdPca("2");
        ov.setIdPrestation("25");
        ov.setCsType(IREOrdresVersements.CS_TYPE_ALLOCATION_NOEL);
        ov.setIdPrestation("1");
        ov.setIdTiers(idTiers);
        ov.setIdTiersAdressePaiement("1234");
        ov.setMontant("2000");
        ov.setNoGroupePeriode(noGroupe);
        ov.setSousTypeGenrePrestation(null);
        return ov;
    }

    public static SimpleOrdreVersement generateSimpleBeneficiaire(String montant, String idTiers, String noGroupe) {
        SimpleOrdreVersement ov = OrdreVersementFactory.generateSimpleBeneficiaire(montant, idTiers, noGroupe, "25");
        // ov.setIdPca("2");
        return ov;
    }

    public static SimpleOrdreVersement generateSimpleBeneficiaire(String montant, String idTiers, String noGroupe,
            String idPrestation) {
        SimpleOrdreVersement ov = new SimpleOrdreVersement();
        ov.setIdPrestation(idPrestation);
        ov.setIdPca(RandomStringUtils.randomNumeric(5));
        ov.setCsType(IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL);
        ov.setIdTiers(idTiers);
        ov.setIdTiersAdressePaiement(idTiers);
        ov.setMontant(montant);
        ov.setNoGroupePeriode(noGroupe);
        ov.setCsTypeDomaine(IPCOrdresVersements.CS_DOMAINE_AI);
        ov.setSousTypeGenrePrestation(null);
        return ov;
    }

    public static SimpleOrdreVersement generateSimpleBeneficiaireDom2R(String montant, String idtiersRequerant,
            String idtiersConjoint, String noGroupe, String idPresation) {
        SimpleOrdreVersement ov = new SimpleOrdreVersement();
        ov.setIdPca(RandomStringUtils.randomNumeric(5));
        ov.setIdPrestation(idPresation);
        ov.setCsType(IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL);
        ov.setIdTiers(idtiersRequerant);
        ov.setIdTiersAdressePaiement(idtiersRequerant);
        ov.setIdTiersAdressePaiementConjoint(idtiersConjoint);
        ov.setMontant(montant);
        ov.setNoGroupePeriode(noGroupe);
        ov.setCsTypeDomaine(IPCOrdresVersements.CS_DOMAINE_AI);
        ov.setSousTypeGenrePrestation(null);
        return ov;
    }

    public static SimpleOrdreVersement generateSimpleCreancier(String montant, String idTiers,
            String idTiersOwnerDetteCreance, String idPrestation) {
        SimpleOrdreVersement ov = new SimpleOrdreVersement();
        ov.setIdPca(null);
        ov.setIdPrestation(idPrestation);
        ov.setCsType(IREOrdresVersements.CS_TYPE_TIERS);
        ov.setIdTiers(idTiers);
        ov.setIdTiersOwnerDetteCreance(idTiersOwnerDetteCreance);
        ov.setIdTiersAdressePaiement(idTiers);
        ov.setMontant(montant);
        ov.setIsCompense(true);
        ov.setMontantDetteModifier(montant);
        ov.setNoGroupePeriode(null);
        ov.setSousTypeGenrePrestation(null);
        ov.setIdDetteComptatCompense(null);
        return ov;
    }

    public static SimpleOrdreVersement generateSimpleDetteCompta(String montant, String idSection,
            String idTiersOwnerDetteCreance) {
        SimpleOrdreVersement ov = new SimpleOrdreVersement();
        ov.setIdPrestation("25");
        ov.setCsType(IREOrdresVersements.CS_TYPE_DETTE);
        ov.setIdTiers(null);
        ov.setIdTiersOwnerDetteCreance(idTiersOwnerDetteCreance);
        ov.setIdTiersAdressePaiement("1234");
        ov.setIdSectionDetteEnCompta(idSection);
        ov.setMontant(montant);
        ov.setIsCompense(true);
        ov.setMontantDetteModifier(montant);
        ov.setNoGroupePeriode(null);
        ov.setSousTypeGenrePrestation(null);
        ov.setIdDetteComptatCompense(null);
        return ov;
    }

    public static SimpleOrdreVersement generateSimpleJourAppoints(String montant, String idPca, String noGroupe) {
        SimpleOrdreVersement ov = new SimpleOrdreVersement();
        ov.setIdPca(idPca);
        ov.setIdPrestation("25");
        ov.setCsType(IREOrdresVersements.CS_TYPE_JOURS_APPOINT);
        ov.setIdTiers(null);
        ov.setIdTiersOwnerDetteCreance(null);
        ov.setIdTiersAdressePaiement(null);
        ov.setIdSectionDetteEnCompta(null);
        ov.setMontant(montant);
        ov.setIsCompense(true);
        ov.setMontantDetteModifier(montant);
        ov.setNoGroupePeriode(noGroupe);
        ov.setSousTypeGenrePrestation(null);
        ov.setIdDetteComptatCompense(null);
        return ov;
    }

    public static SimpleOrdreVersement generateSimpleRestitution(String montant, String idTiers, String noGroupe) {
        return OrdreVersementFactory.generateSimpleRestitution(montant, idTiers, noGroupe, "25");
    }

    public static SimpleOrdreVersement generateSimpleRestitution(String montant, String idTiers, String noGroupe,
            String idPresation) {
        SimpleOrdreVersement ov = new SimpleOrdreVersement();
        ov.setIdPrestation(idPresation);
        ov.setIdPca(RandomStringUtils.randomNumeric(5));
        ov.setCsType(IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION);
        ov.setIdTiers(idTiers);
        ov.setIdTiersAdressePaiement(idTiers);
        ov.setMontant(montant);
        ov.setNoGroupePeriode(noGroupe);
        ov.setCsTypeDomaine(IPCOrdresVersements.CS_DOMAINE_AI);
        ov.setSousTypeGenrePrestation(null);
        return ov;
    }

    public static SimpleOrdreVersement generateSimpleRestitutionDom2R(String montant, String idtiersRequerant,
            String idtiersConjoint, String noGroupe, String idPresation) {
        SimpleOrdreVersement ov = new SimpleOrdreVersement();
        ov.setIdPrestation(idPresation);
        ov.setIdPca(RandomStringUtils.randomNumeric(5));
        ov.setCsType(IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION);
        ov.setIdTiers(idtiersRequerant);
        ov.setIdTiersAdressePaiement(idtiersRequerant);
        ov.setIdTiersAdressePaiementConjoint(idtiersConjoint);
        ov.setMontant(montant);
        ov.setNoGroupePeriode(noGroupe);
        ov.setCsTypeDomaine(IPCOrdresVersements.CS_DOMAINE_AI);
        ov.setSousTypeGenrePrestation(null);
        return ov;
    }
}
