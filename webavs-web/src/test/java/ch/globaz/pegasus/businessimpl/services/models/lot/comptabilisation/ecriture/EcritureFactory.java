package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.osiris.api.APIEcriture;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.CompteAnnexeFactory;

public class EcritureFactory {

    public static Ecriture generateEcrituresBeneficiaire(Integer montant, String csTypeRoleFamille,
            String idCompteAnnexe) {
        Ecriture e = new Ecriture();
        e.setMontant(new BigDecimal(montant));
        e.setCsTypeRoleFamille(csTypeRoleFamille);
        e.setSection(SectionPegasus.DECISION_PC);
        e.setTypeEcriture(TypeEcriture.STANDARD);
        e.setOrdreVersement(new OrdreVersement(null, IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL, null, null,
                null, null, null, null, "0", null, "13", null, null, "refPaiemnt"));
        e.setCompteAnnexe((new CompteAnnexeSimpleModel()));
        e.getCompteAnnexe().setId(idCompteAnnexe);
        return e;
    }

    public static Ecriture generateEcrituresDetteCredit(Integer montant, SectionSimpleModel sectionDette) {
        Ecriture e = new Ecriture();
        e.setMontant(new BigDecimal(montant));
        e.setSectionSimple(sectionDette);
        e.setTypeEcriture(TypeEcriture.DETTE);
        e.setCodeDebitCredit(APIEcriture.CREDIT);
        e.setOrdreVersement(new OrdreVersement(null, IREOrdresVersements.CS_TYPE_DETTE, null, null, null, null, null,
                null, "0", null, "13", null, null, "refPaiemnt"));
        e.setCompteAnnexe((new CompteAnnexeSimpleModel()));
        return e;
    }

    public static Ecriture generateEcrituresDetteDebit(Integer montant, String csTypeRoleFamille, String idCompteAnnexe) {
        Ecriture e = new Ecriture();
        e.setMontant(new BigDecimal(montant));
        e.setCsTypeRoleFamille(csTypeRoleFamille);
        e.setSection(SectionPegasus.RESTIUTION);
        e.setTypeEcriture(TypeEcriture.DETTE);
        e.setOrdreVersement(new OrdreVersement(null, IREOrdresVersements.CS_TYPE_DETTE, null, null, null, null, null,
                null, "0", null, "13", null, null, "refPaiemnt"));
        e.setCompteAnnexe((new CompteAnnexeSimpleModel()));
        e.getCompteAnnexe().setId(idCompteAnnexe);
        return e;
    }

    public static Ecriture generateEcrituresResitution(Integer montant, String csTypeRoleFamille, String idCompteAnnexe) {
        Ecriture e = new Ecriture();
        e.setMontant(new BigDecimal(montant));
        e.setCsTypeRoleFamille(csTypeRoleFamille);
        e.setSection(SectionPegasus.RESTIUTION);
        e.setTypeEcriture(TypeEcriture.STANDARD);
        e.setOrdreVersement(new OrdreVersement(null, IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION, null, null,
                null, null, null, null, "0", null, "13", null, null, "refPaiemnt"));
        e.setCompteAnnexe((new CompteAnnexeSimpleModel()));
        e.getCompteAnnexe().setId(idCompteAnnexe);
        return e;
    }

    public static List<Ecriture> generateListEcritureConjoint(Integer beneficiaire, Integer restitution) {
        List<Ecriture> ecritures = new ArrayList<Ecriture>();
        ecritures.add(EcritureFactory.generateEcrituresBeneficiaire(beneficiaire, IPCDroits.CS_ROLE_FAMILLE_CONJOINT,
                CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT));
        ecritures.add(EcritureFactory.generateEcrituresResitution(restitution, IPCDroits.CS_ROLE_FAMILLE_CONJOINT,
                CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT));
        return ecritures;
    }

    public static List<Ecriture> generateListEcritureDom2R(Integer beneficiaireReq, Integer restitutionReq,
            Integer beneficiaireConj, Integer restitutionConj) {
        List<Ecriture> ecritures = EcritureFactory.generateListEcritureRequConj(beneficiaireReq, restitutionReq,
                beneficiaireConj, restitutionConj);
        for (Ecriture ecriture : ecritures) {
            ecriture.getOrdreVersement().setIdTiersAdressePaiementConjoint("2");
        }
        return ecritures;
    }

    public static List<Ecriture> generateListEcritureDom2RToDom(int beneficiaireRequ, int restitutionReq,
            int restitutionCon) {
        List<Ecriture> ecritures = new ArrayList<Ecriture>();
        // Dom2R
        ecritures.add(EcritureFactory.generateEcrituresBeneficiaire(beneficiaireRequ,
                IPCDroits.CS_ROLE_FAMILLE_REQUERANT, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT));

        // DOM
        ecritures.add(EcritureFactory.generateEcrituresResitution(restitutionReq, IPCDroits.CS_ROLE_FAMILLE_REQUERANT,
                CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT));

        ecritures.add(EcritureFactory.generateEcrituresResitution(restitutionCon, IPCDroits.CS_ROLE_FAMILLE_CONJOINT,
                CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT));

        ecritures.get(2).getOrdreVersement().setIdTiersAdressePaiementConjoint("2");
        return ecritures;
    }

    public static List<Ecriture> generateListEcritureDom2RToSepMal(int beneficiaireRequ, int restitutionReq,
            int beneficiaireConj, int restitutionConj) {
        List<Ecriture> ecritures = new ArrayList<Ecriture>();

        // sep Mal
        ecritures.add(EcritureFactory.generateEcrituresBeneficiaire(beneficiaireRequ,
                IPCDroits.CS_ROLE_FAMILLE_REQUERANT, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT));

        ecritures.add(EcritureFactory.generateEcrituresBeneficiaire(beneficiaireConj,
                IPCDroits.CS_ROLE_FAMILLE_CONJOINT, CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT));

        // DOM
        ecritures.add(EcritureFactory.generateEcrituresResitution(restitutionReq, IPCDroits.CS_ROLE_FAMILLE_REQUERANT,
                CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT));
        ecritures.add(EcritureFactory.generateEcrituresResitution(restitutionConj, IPCDroits.CS_ROLE_FAMILLE_CONJOINT,
                CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT));

        ecritures.get(2).getOrdreVersement().setIdTiersAdressePaiementConjoint("2");
        ecritures.get(3).getOrdreVersement().setIdTiersAdressePaiementConjoint("2");

        return ecritures;
    }

    public static List<Ecriture> generateListEcritureDomToDom2R(int beneficiaireRequ, int restitution,
            int beneficiaireConj) {
        List<Ecriture> ecritures = new ArrayList<Ecriture>();
        // Dom2R
        ecritures.add(EcritureFactory.generateEcrituresBeneficiaire(beneficiaireRequ,
                IPCDroits.CS_ROLE_FAMILLE_REQUERANT, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT));

        ecritures.add(EcritureFactory.generateEcrituresBeneficiaire(beneficiaireConj,
                IPCDroits.CS_ROLE_FAMILLE_CONJOINT, CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT));

        ecritures.get(0).getOrdreVersement().setIdTiersAdressePaiementConjoint("2");
        ecritures.get(1).getOrdreVersement().setIdTiersAdressePaiementConjoint("2");
        // DOM
        ecritures.add(EcritureFactory.generateEcrituresResitution(restitution, IPCDroits.CS_ROLE_FAMILLE_REQUERANT,
                CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT));

        return ecritures;
    }

    public static List<Ecriture> generateListEcritureDomToSepMal(int beneficiaireRequ, int restitutionReq,
            int beneficiaireConj) {
        List<Ecriture> ecritures = new ArrayList<Ecriture>();

        // sep Mal
        ecritures.add(EcritureFactory.generateEcrituresResitution(restitutionReq, IPCDroits.CS_ROLE_FAMILLE_REQUERANT,
                CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT));
        // DOM
        ecritures.add(EcritureFactory.generateEcrituresBeneficiaire(beneficiaireRequ,
                IPCDroits.CS_ROLE_FAMILLE_REQUERANT, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT));

        ecritures.add(EcritureFactory.generateEcrituresBeneficiaire(beneficiaireConj,
                IPCDroits.CS_ROLE_FAMILLE_CONJOINT, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT));

        return ecritures;
    }

    public static List<Ecriture> generateListEcritureRequConj(Integer beneficiaireReq, Integer restitutionReq,
            Integer beneficiaireConj, Integer restitutionConj) {
        List<Ecriture> ecritures = new ArrayList<Ecriture>();
        ecritures.addAll(EcritureFactory.generateListEcritureRequerant(beneficiaireReq, restitutionReq));
        ecritures.addAll(EcritureFactory.generateListEcritureConjoint(beneficiaireConj, restitutionConj));
        return ecritures;
    }

    public static List<Ecriture> generateListEcritureRequerant(Integer beneficiaire, Integer restitution) {
        List<Ecriture> ecritures = new ArrayList<Ecriture>();
        ecritures.add(EcritureFactory.generateEcrituresBeneficiaire(beneficiaire, IPCDroits.CS_ROLE_FAMILLE_REQUERANT,
                CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT));
        ecritures.add(EcritureFactory.generateEcrituresResitution(restitution, IPCDroits.CS_ROLE_FAMILLE_REQUERANT,
                CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT));
        return ecritures;
    }

    public static List<Ecriture> generateListEcritureSepMalToDom(int beneficiaireRequ, int restitutionReq,
            int restitutionConj) {
        List<Ecriture> ecritures = new ArrayList<Ecriture>();

        // sep Mal
        ecritures.add(EcritureFactory.generateEcrituresResitution(restitutionReq, IPCDroits.CS_ROLE_FAMILLE_REQUERANT,
                CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT));
        ecritures.add(EcritureFactory.generateEcrituresResitution(restitutionConj, IPCDroits.CS_ROLE_FAMILLE_CONJOINT,
                CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT));

        // DOM
        ecritures.add(EcritureFactory.generateEcrituresBeneficiaire(beneficiaireRequ,
                IPCDroits.CS_ROLE_FAMILLE_REQUERANT, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT));

        return ecritures;
    }

    public static List<Ecriture> generateListEcritureSepMalToDom2R(int beneficiaireRequ, int restitutionReq,
            int beneficiaireConj, int restitutionConj) {
        List<Ecriture> ecritures = new ArrayList<Ecriture>();

        // sep Mal
        ecritures.add(EcritureFactory.generateEcrituresResitution(restitutionReq, IPCDroits.CS_ROLE_FAMILLE_REQUERANT,
                CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT));
        ecritures.add(EcritureFactory.generateEcrituresResitution(restitutionConj, IPCDroits.CS_ROLE_FAMILLE_CONJOINT,
                CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT));

        // DOM
        ecritures.add(EcritureFactory.generateEcrituresBeneficiaire(beneficiaireRequ,
                IPCDroits.CS_ROLE_FAMILLE_REQUERANT, CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT));
        ecritures.get(2).getOrdreVersement().setIdTiersAdressePaiementConjoint("2");

        ecritures.add(EcritureFactory.generateEcrituresBeneficiaire(beneficiaireConj,
                IPCDroits.CS_ROLE_FAMILLE_CONJOINT, CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT));
        ecritures.get(3).getOrdreVersement().setIdTiersAdressePaiementConjoint("2");

        return ecritures;
    }
}
