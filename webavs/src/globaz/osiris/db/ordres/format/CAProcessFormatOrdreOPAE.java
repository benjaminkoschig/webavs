package globaz.osiris.db.ordres.format;

import globaz.framework.filetransfer.FWAsciiFileFieldDescriptor;
import globaz.framework.filetransfer.FWAsciiFileRecordDescriptor;
import globaz.framework.util.FWMessage;
import globaz.globall.util.JACCP;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.ordre.APICommonOdreVersement;
import globaz.osiris.api.ordre.APIOrdreGroupe;
import globaz.osiris.db.utils.CAAdressePaiementFormatter;
import globaz.osiris.external.IntAdressePaiement;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;

/**
 * Insérez la description du type ici. Date de création : (08.04.2002 09:05:57)
 * 
 * @author: Administrator
 */
public final class CAProcessFormatOrdreOPAE extends CAOrdreFormateur {
    private static final String CCP_DESTINATAIRE = "ccpDestinataire";

    private static final int CCP_DESTINATAIRE_MAX_LENGTH = 35;

    private static final String CLEARING = "clearing";
    private static final String CODE_ISO_MONNAIE_BONIFICATION = "codeIsoMonBon";

    private static final String CODE_ISO_MONNAIE_DEPOT = "codeIsoMonDep";
    private static final String CODE_ISO_MONNAIRE_TOTAL = "codeIsoMonnaieTotal";
    private static final String CODE_ISO_PAYS = "codeIsoPays";
    private static final String CODE_TAXE = "codeTaxe";
    private static final String COMMUNICATION = "communication";
    private static final String COMPTE_DEBIT = "compteDebit";
    private static final String COMPTE_DEBIT_TAXES = "compteDebitTaxes";
    private static final String DATE_ECHEANCE = "dateEcheance";
    private static final String DESIGNATION_BENEFICIAIRE = "designationBeneficiaire";
    private static final String DESIGNATION_DESTINATAIRE = "designationDestinataire";
    private static final String DESIGNATION_DONNEUR_ORDRE = "designationDonneurOrdre";
    private static final String ELEMENT_COMMANDE = "elementCommande";
    private static final String FRAIS_PRIS_EN_CHARGE_PAR_DESTINATAIRE = "BEN";
    private static final String FRAIS_PRIS_EN_CHARGE_PAR_DONNEUR_ORDRE = "OUR";
    private static final String GENRE_PAIEMENT = "genrePaiement";

    private static final String GENRE_TRANSACTION = "genreTransaction";
    private static final String ID_FICHIER = "idFichier";
    private static final String LIEU_BENEFICIAIRE = "lieuBeneficiaire";
    private static final String LIEU_DESTINATAIRE = "lieuDestinataire";
    private static final String LIEU_DONNEUR_ORDRE = "lieuDonneurOrdre";
    private static final String MAIN_PROPRE = "mainPropre";
    private static final String MODULO_11 = "modulo11";
    private static final String MONTANT_DEPOT = "montantDepot";
    private static final String NO_ADHERENT = "noAdherent";
    private static final String NO_COMPTE_BANCAIRE_BENEFICIAIRE = "noCompteBancaireBeneficiaire";
    private static final String NO_REFERENCE = "noReference";
    private static final String NOM_BENEFICIAIRE = "nomBeneficiaire";
    private static final String NOM_DESTINATAIRE = "nomDestinataire";
    private static final String NOM_DONNEUR_ORDRE = "nomDonneurOrdre";
    private static final String NPA_BENEFICIAIRE = "npaBeneficiaire";
    private static final String NPA_DESTINATAIRE = "npaDestinataire";
    private static final String NPA_DONNEUR_ORDRE = "npaDonneurOrdre";
    private static final String NUMERO_OG = "numeroOG";
    private static final String NUMERO_TRANSACTION = "numeroTransaction";
    private static final String REFERENCE_EXPEDITEUR = "referenceExpediteur";
    private static final String RESERVE_1 = "reserve1";
    private static final String RESERVE_10 = "reserve10";
    private static final String RESERVE_2 = "reserve2";
    private static final String RESERVE_3 = "reserve3";
    private static final String RESERVE_4 = "reserve4";
    private static final String RESERVE_5 = "reserve5";
    private static final String RESERVE_6 = "reserve6";
    private static final String RESERVE_7 = "reserve7";
    private static final String RESERVE_8 = "reserve8";
    private static final String RESERVE_9 = "reserve9";
    private static final String RUE_BENEFICIAIRE = "rueBeneficiaire";
    private static final String RUE_DESTINATAIRE = "rueDestinataire";
    private static final String RUE_DONNEUR_ORDRE = "rueDonneurOrdre";
    private static final String SWIFT = "swift";
    private static final String TOTAL_MONNAIE_1 = "totalMonnaie1";
    private static final String TOTAL_MONNAIE_10 = "totalMonnaie10";
    private static final String TOTAL_MONNAIE_11 = "totalMonnaie11";
    private static final String TOTAL_MONNAIE_12 = "totalMonnaie12";
    private static final String TOTAL_MONNAIE_13 = "totalMonnaie13";
    private static final String TOTAL_MONNAIE_14 = "totalMonnaie14";
    private static final String TOTAL_MONNAIE_15 = "totalMonnaie15";
    private static final String TOTAL_MONNAIE_2 = "totalMonnaie2";

    private static final String TOTAL_MONNAIE_3 = "totalMonnaie3";
    private static final String TOTAL_MONNAIE_4 = "totalMonnaie4";
    private static final String TOTAL_MONNAIE_5 = "totalMonnaie5";
    private static final String TOTAL_MONNAIE_6 = "totalMonnaie6";
    private static final String TOTAL_MONNAIE_7 = "totalMonnaie7";
    private static final String TOTAL_MONNAIE_8 = "totalMonnaie8";
    private static final String TOTAL_MONNAIE_9 = "totalMonnaie9";
    private static final String TOTAL_MONTANT = "totalMontant";
    private static final String TOTAL_TRANSACTIONS = "totalTransactions";
    private static final String URGENT = "urgent";

    // Enregistrement pour paiements sur un Compte Jaune du service Intérieur
    private FWAsciiFileRecordDescriptor opae22 = new FWAsciiFileRecordDescriptor();

    // Enregistrement pour mandats de paiement du service intérieur
    private FWAsciiFileRecordDescriptor opae24 = new FWAsciiFileRecordDescriptor();

    // Enregistrement pour paiements clearing du service intérieur
    private FWAsciiFileRecordDescriptor opae27 = new FWAsciiFileRecordDescriptor();

    // Enregistrement pour bulletins de versement avec n° de référence (BVR)
    private FWAsciiFileRecordDescriptor opae28 = new FWAsciiFileRecordDescriptor();

    // Enregistrement pour virement sur un compte postal du service
    // international (Postgiro)
    private FWAsciiFileRecordDescriptor opae32 = new FWAsciiFileRecordDescriptor();
    // Enregistrement pour mandats de poste du service international (Postcash)
    private FWAsciiFileRecordDescriptor opae34 = new FWAsciiFileRecordDescriptor();

    // Enregistrement pour paiements bancaires du service international
    // (virement bancaire)
    private FWAsciiFileRecordDescriptor opae37 = new FWAsciiFileRecordDescriptor();

    // Enregistrement total
    private FWAsciiFileRecordDescriptor opae97 = new FWAsciiFileRecordDescriptor();

    private FWAsciiFileRecordDescriptor opae97Item = new FWAsciiFileRecordDescriptor();

    // Header commun à chaques enregistrements
    private FWAsciiFileRecordDescriptor opaeHeader = new FWAsciiFileRecordDescriptor();

    /**
     * Insérez la description de la méthode ici. Date de création : (16.05.2002 11:48:04)
     */
    private void createRecordDefinition() {

        // Définir les champs de contrôle
        FWAsciiFileFieldDescriptor fIdFichier = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreOPAE.ID_FICHIER,
                FWAsciiFileFieldDescriptor.STRING, 3, 0, 1);
        FWAsciiFileFieldDescriptor fDateEcheance = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.DATE_ECHEANCE, FWAsciiFileFieldDescriptor.DATE_AMJ, 6, 0, 4);
        FWAsciiFileFieldDescriptor fReserve1 = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreOPAE.RESERVE_1,
                getZeroString(5), 10);
        FWAsciiFileFieldDescriptor fElementCommande = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.ELEMENT_COMMANDE, "1", 15);
        FWAsciiFileFieldDescriptor fCompteDebit = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreOPAE.COMPTE_DEBIT,
                FWAsciiFileFieldDescriptor.INTEGER, 9, 0, 16);
        FWAsciiFileFieldDescriptor fCompdeDebitTaxes = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.COMPTE_DEBIT_TAXES, FWAsciiFileFieldDescriptor.INTEGER, 9, 0, 25);
        FWAsciiFileFieldDescriptor fNumeroOG = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreOPAE.NUMERO_OG,
                FWAsciiFileFieldDescriptor.INTEGER, 2, 0, 34);
        FWAsciiFileFieldDescriptor fGenreTransaction = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.GENRE_TRANSACTION, FWAsciiFileFieldDescriptor.STRING, 2, 0, 36);
        FWAsciiFileFieldDescriptor fNumeroTransaction = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.NUMERO_TRANSACTION, FWAsciiFileFieldDescriptor.INTEGER, 6, 0, 38);
        FWAsciiFileFieldDescriptor fReserve2 = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreOPAE.RESERVE_2,
                getZeroString(2), 44);
        FWAsciiFileFieldDescriptor fGenrePaiement = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.GENRE_PAIEMENT, FWAsciiFileFieldDescriptor.STRING, 1, 0, 46);
        FWAsciiFileFieldDescriptor fReserve3 = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreOPAE.RESERVE_3,
                getZeroString(4), 47);

        // Définir les autres champs
        FWAsciiFileFieldDescriptor fCodeIsoMonnaieDepot = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.CODE_ISO_MONNAIE_DEPOT, FWAsciiFileFieldDescriptor.STRING, 3, 0, 51);
        FWAsciiFileFieldDescriptor fMontantDepot = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.MONTANT_DEPOT, FWAsciiFileFieldDescriptor.NUMBER_FIXED_DECIMALS, 13, 2, 54);
        FWAsciiFileFieldDescriptor fReserve4 = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreOPAE.RESERVE_4,
                getSpaceString(1), 67);
        FWAsciiFileFieldDescriptor fCodeIsoMonnaieBonification = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.CODE_ISO_MONNAIE_BONIFICATION, FWAsciiFileFieldDescriptor.STRING, 3, 0, 68);
        FWAsciiFileFieldDescriptor fCodeIsoPays = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.CODE_ISO_PAYS, FWAsciiFileFieldDescriptor.STRING, 2, 0, 71);
        FWAsciiFileFieldDescriptor fccpDestinataire = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.CCP_DESTINATAIRE, FWAsciiFileFieldDescriptor.INTEGER, 9, 0, 73);
        FWAsciiFileFieldDescriptor fReserve5 = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreOPAE.RESERVE_5,
                getSpaceString(6), 82);
        FWAsciiFileFieldDescriptor fNoCompteBancaireBeneficiaire = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.NO_COMPTE_BANCAIRE_BENEFICIAIRE, FWAsciiFileFieldDescriptor.STRING, 35, 0, 88);
        FWAsciiFileFieldDescriptor fNomDestinataire = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.NOM_DESTINATAIRE, FWAsciiFileFieldDescriptor.STRING, 35, 0, 123);
        FWAsciiFileFieldDescriptor fDesignationDestinataire = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.DESIGNATION_DESTINATAIRE, FWAsciiFileFieldDescriptor.STRING, 35, 0, 158);
        FWAsciiFileFieldDescriptor fRueDestinataire = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.RUE_DESTINATAIRE, FWAsciiFileFieldDescriptor.STRING, 35, 0, 193);
        FWAsciiFileFieldDescriptor fNPADestinataire = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.NPA_DESTINATAIRE, FWAsciiFileFieldDescriptor.STRING, 10, 0, 228);
        FWAsciiFileFieldDescriptor fLieuDestinataire = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.LIEU_DESTINATAIRE, FWAsciiFileFieldDescriptor.STRING, 25, 0, 238);
        FWAsciiFileFieldDescriptor fNomBeneficiaire = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.NOM_BENEFICIAIRE, FWAsciiFileFieldDescriptor.STRING, 35, 0, 263);
        FWAsciiFileFieldDescriptor fDesignationBeneficiaire = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.DESIGNATION_BENEFICIAIRE, FWAsciiFileFieldDescriptor.STRING, 35, 0, 298);
        FWAsciiFileFieldDescriptor fRueBeneficiaire = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.RUE_BENEFICIAIRE, FWAsciiFileFieldDescriptor.STRING, 35, 0, 333);
        FWAsciiFileFieldDescriptor fNPABeneficiaire = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.NPA_BENEFICIAIRE, FWAsciiFileFieldDescriptor.STRING, 10, 0, 368);
        FWAsciiFileFieldDescriptor fLieuBeneficiaire = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.LIEU_BENEFICIAIRE, FWAsciiFileFieldDescriptor.STRING, 25, 0, 378);
        FWAsciiFileFieldDescriptor fCommunication = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.COMMUNICATION, FWAsciiFileFieldDescriptor.STRING, 140, 0, 403);
        FWAsciiFileFieldDescriptor fReserve6 = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreOPAE.RESERVE_6,
                getSpaceString(4), 543);
        FWAsciiFileFieldDescriptor fNomDonneurOrdre = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.NOM_DONNEUR_ORDRE, FWAsciiFileFieldDescriptor.STRING, 35, 0, 547);
        FWAsciiFileFieldDescriptor fDesignationDonneurOrdre = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.DESIGNATION_DONNEUR_ORDRE, FWAsciiFileFieldDescriptor.STRING, 35, 0, 582);
        FWAsciiFileFieldDescriptor fRueDonneurOrdre = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.RUE_DONNEUR_ORDRE, FWAsciiFileFieldDescriptor.STRING, 35, 0, 617);
        FWAsciiFileFieldDescriptor fNPADonneurOrdre = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.NPA_DONNEUR_ORDRE, FWAsciiFileFieldDescriptor.STRING, 10, 0, 652);
        FWAsciiFileFieldDescriptor fLieuDonneurOrdre = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.LIEU_DONNEUR_ORDRE, FWAsciiFileFieldDescriptor.STRING, 25, 0, 662);

        // OPAE 24 et 34
        FWAsciiFileFieldDescriptor fReserve7 = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreOPAE.RESERVE_7,
                getSpaceString(15), 73);
        FWAsciiFileFieldDescriptor fReserve8 = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreOPAE.RESERVE_8,
                getSpaceString(34), 88);

        // OPAE 24
        FWAsciiFileFieldDescriptor fMainPropre = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreOPAE.MAIN_PROPRE,
                FWAsciiFileFieldDescriptor.STRING, 1, 0, 122);

        // OPAE 27
        FWAsciiFileFieldDescriptor fClearing = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreOPAE.CLEARING,
                FWAsciiFileFieldDescriptor.STRING, 15, 0, 73);

        // OPAE 28
        FWAsciiFileFieldDescriptor fModulo11 = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreOPAE.MODULO_11,
                FWAsciiFileFieldDescriptor.STRING, 2, 0, 73);
        FWAsciiFileFieldDescriptor fNoAdherent = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreOPAE.NO_ADHERENT,
                FWAsciiFileFieldDescriptor.INTEGER, 9, 0, 75);
        FWAsciiFileFieldDescriptor fNoReference = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreOPAE.NO_REFERENCE,
                FWAsciiFileFieldDescriptor.INTEGER, 27, 0, 84);
        FWAsciiFileFieldDescriptor fReferenceExpediteur = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.REFERENCE_EXPEDITEUR, FWAsciiFileFieldDescriptor.STRING, 35, 0, 111);

        // OPAE 37 et 34
        FWAsciiFileFieldDescriptor fCodeTaxe = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreOPAE.CODE_TAXE,
                FWAsciiFileFieldDescriptor.STRING, 3, 0, 543);

        // OPAE 37
        FWAsciiFileFieldDescriptor fUrgent = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreOPAE.URGENT,
                FWAsciiFileFieldDescriptor.STRING, 1, 0, 546);
        FWAsciiFileFieldDescriptor fSwift = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreOPAE.SWIFT,
                FWAsciiFileFieldDescriptor.STRING, 15, 0, 73);

        // OPAE 32
        FWAsciiFileFieldDescriptor fccpDestinataireOpae32 = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.CCP_DESTINATAIRE, FWAsciiFileFieldDescriptor.INTEGER,
                CAProcessFormatOrdreOPAE.CCP_DESTINATAIRE_MAX_LENGTH, 0, 88);

        // OPAE 34
        FWAsciiFileFieldDescriptor fReserve9 = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreOPAE.RESERVE_9,
                getSpaceString(35 + 35 + 35 + 10 + 25), 263);
        FWAsciiFileFieldDescriptor fReserve10 = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreOPAE.RESERVE_10,
                getSpaceString(1), 546);

        // OPAE 97
        FWAsciiFileFieldDescriptor fCodeIsoMonnaieTotal = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.CODE_ISO_MONNAIRE_TOTAL, FWAsciiFileFieldDescriptor.STRING, 3, 0, 1);
        FWAsciiFileFieldDescriptor fTotalTransaction = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.TOTAL_TRANSACTIONS, FWAsciiFileFieldDescriptor.INTEGER, 6, 0, 4);
        FWAsciiFileFieldDescriptor fTotalMontant = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.TOTAL_MONTANT, FWAsciiFileFieldDescriptor.NUMBER_FIXED_DECIMALS, 13, 2, 10);
        FWAsciiFileFieldDescriptor fTotalMonnaie1 = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_1, FWAsciiFileFieldDescriptor.STRING, 22, 0, 51);
        FWAsciiFileFieldDescriptor fTotalMonnaie2 = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_2, FWAsciiFileFieldDescriptor.STRING, 22, 0, 73);
        FWAsciiFileFieldDescriptor fTotalMonnaie3 = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_3, FWAsciiFileFieldDescriptor.STRING, 22, 0, 95);
        FWAsciiFileFieldDescriptor fTotalMonnaie4 = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_4, FWAsciiFileFieldDescriptor.STRING, 22, 0, 117);
        FWAsciiFileFieldDescriptor fTotalMonnaie5 = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_5, FWAsciiFileFieldDescriptor.STRING, 22, 0, 139);
        FWAsciiFileFieldDescriptor fTotalMonnaie6 = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_6, FWAsciiFileFieldDescriptor.STRING, 22, 0, 161);
        FWAsciiFileFieldDescriptor fTotalMonnaie7 = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_7, FWAsciiFileFieldDescriptor.STRING, 22, 0, 183);
        FWAsciiFileFieldDescriptor fTotalMonnaie8 = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_8, FWAsciiFileFieldDescriptor.STRING, 22, 0, 205);
        FWAsciiFileFieldDescriptor fTotalMonnaie9 = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_9, FWAsciiFileFieldDescriptor.STRING, 22, 0, 227);
        FWAsciiFileFieldDescriptor fTotalMonnaie10 = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_10, FWAsciiFileFieldDescriptor.STRING, 22, 0, 249);
        FWAsciiFileFieldDescriptor fTotalMonnaie11 = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_11, FWAsciiFileFieldDescriptor.STRING, 22, 0, 271);
        FWAsciiFileFieldDescriptor fTotalMonnaie12 = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_12, FWAsciiFileFieldDescriptor.STRING, 22, 0, 293);
        FWAsciiFileFieldDescriptor fTotalMonnaie13 = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_13, FWAsciiFileFieldDescriptor.STRING, 22, 0, 315);
        FWAsciiFileFieldDescriptor fTotalMonnaie14 = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_14, FWAsciiFileFieldDescriptor.STRING, 22, 0, 337);
        FWAsciiFileFieldDescriptor fTotalMonnaie15 = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_15, FWAsciiFileFieldDescriptor.STRING, 22, 0, 359);

        try {
            // Définition du header
            opaeHeader.setLength(700);
            opaeHeader.addFieldDescriptor(fIdFichier);
            opaeHeader.addFieldDescriptor(fDateEcheance);
            opaeHeader.addFieldDescriptor(fReserve1);
            opaeHeader.addFieldDescriptor(fElementCommande);
            opaeHeader.addFieldDescriptor(fCompteDebit);
            opaeHeader.addFieldDescriptor(fCompdeDebitTaxes);
            opaeHeader.addFieldDescriptor(fNumeroOG);
            opaeHeader.addFieldDescriptor(fGenreTransaction);
            opaeHeader.addFieldDescriptor(fNumeroTransaction);
            opaeHeader.addFieldDescriptor(fReserve2);
            opaeHeader.addFieldDescriptor(fGenrePaiement);
            opaeHeader.addFieldDescriptor(fReserve3);

            // Définition OPAE 22 (pmt sur CCP en suisse)
            opae22.setLength(700);
            opae22.addFieldDescriptor(fIdFichier);
            opae22.addFieldDescriptor(fDateEcheance);
            opae22.addFieldDescriptor(fReserve1);
            opae22.addFieldDescriptor(fElementCommande);
            opae22.addFieldDescriptor(fCompteDebit);
            opae22.addFieldDescriptor(fCompdeDebitTaxes);
            opae22.addFieldDescriptor(fNumeroOG);
            opae22.addFieldDescriptor(fGenreTransaction);
            opae22.addFieldDescriptor(fNumeroTransaction);
            opae22.addFieldDescriptor(fReserve2);
            opae22.addFieldDescriptor(fGenrePaiement);
            opae22.addFieldDescriptor(fReserve3);
            opae22.addFieldDescriptor(fCodeIsoMonnaieDepot);
            opae22.addFieldDescriptor(fMontantDepot);
            opae22.addFieldDescriptor(fReserve4);
            opae22.addFieldDescriptor(fCodeIsoMonnaieBonification);
            opae22.addFieldDescriptor(fCodeIsoPays);
            opae22.addFieldDescriptor(fccpDestinataire);
            opae22.addFieldDescriptor(fReserve5);
            opae22.addFieldDescriptor(fNoCompteBancaireBeneficiaire);
            opae22.addFieldDescriptor(fNomDestinataire);
            opae22.addFieldDescriptor(fDesignationDestinataire);
            opae22.addFieldDescriptor(fRueDestinataire);
            opae22.addFieldDescriptor(fNPADestinataire);
            opae22.addFieldDescriptor(fLieuDestinataire);
            opae22.addFieldDescriptor(fNomBeneficiaire);
            opae22.addFieldDescriptor(fDesignationBeneficiaire);
            opae22.addFieldDescriptor(fRueBeneficiaire);
            opae22.addFieldDescriptor(fNPABeneficiaire);
            opae22.addFieldDescriptor(fLieuBeneficiaire);
            opae22.addFieldDescriptor(fCommunication);
            opae22.addFieldDescriptor(fReserve6);
            opae22.addFieldDescriptor(fNomDonneurOrdre);
            opae22.addFieldDescriptor(fDesignationDonneurOrdre);
            opae22.addFieldDescriptor(fRueDonneurOrdre);
            opae22.addFieldDescriptor(fNPADonneurOrdre);
            opae22.addFieldDescriptor(fLieuDonneurOrdre);

            // Définition OPAE 24 (mandat de paiement en suisse)
            opae24.setLength(700);
            opae24.addFieldDescriptor(fIdFichier);
            opae24.addFieldDescriptor(fDateEcheance);
            opae24.addFieldDescriptor(fReserve1);
            opae24.addFieldDescriptor(fElementCommande);
            opae24.addFieldDescriptor(fCompteDebit);
            opae24.addFieldDescriptor(fCompdeDebitTaxes);
            opae24.addFieldDescriptor(fNumeroOG);
            opae24.addFieldDescriptor(fGenreTransaction);
            opae24.addFieldDescriptor(fNumeroTransaction);
            opae24.addFieldDescriptor(fReserve2);
            opae24.addFieldDescriptor(fGenrePaiement);
            opae24.addFieldDescriptor(fReserve3);
            opae24.addFieldDescriptor(fCodeIsoMonnaieDepot);
            opae24.addFieldDescriptor(fMontantDepot);
            opae24.addFieldDescriptor(fReserve4);
            opae24.addFieldDescriptor(fCodeIsoMonnaieBonification);
            opae24.addFieldDescriptor(fCodeIsoPays);
            opae24.addFieldDescriptor(fReserve7);
            opae24.addFieldDescriptor(fReserve8);
            opae24.addFieldDescriptor(fMainPropre);
            opae24.addFieldDescriptor(fNomDestinataire);
            opae24.addFieldDescriptor(fDesignationDestinataire);
            opae24.addFieldDescriptor(fRueDestinataire);
            opae24.addFieldDescriptor(fNPADestinataire);
            opae24.addFieldDescriptor(fLieuDestinataire);
            opae24.addFieldDescriptor(fNomBeneficiaire);
            opae24.addFieldDescriptor(fDesignationBeneficiaire);
            opae24.addFieldDescriptor(fRueBeneficiaire);
            opae24.addFieldDescriptor(fNPABeneficiaire);
            opae24.addFieldDescriptor(fLieuBeneficiaire);
            opae24.addFieldDescriptor(fCommunication);
            opae24.addFieldDescriptor(fReserve6);
            opae24.addFieldDescriptor(fNomDonneurOrdre);
            opae24.addFieldDescriptor(fDesignationDonneurOrdre);
            opae24.addFieldDescriptor(fRueDonneurOrdre);
            opae24.addFieldDescriptor(fNPADonneurOrdre);
            opae24.addFieldDescriptor(fLieuDonneurOrdre);

            // Définition OPAE 27 (mandat de paiement en suisse)
            opae27.setLength(700);
            opae27.addFieldDescriptor(fIdFichier);
            opae27.addFieldDescriptor(fDateEcheance);
            opae27.addFieldDescriptor(fReserve1);
            opae27.addFieldDescriptor(fElementCommande);
            opae27.addFieldDescriptor(fCompteDebit);
            opae27.addFieldDescriptor(fCompdeDebitTaxes);
            opae27.addFieldDescriptor(fNumeroOG);
            opae27.addFieldDescriptor(fGenreTransaction);
            opae27.addFieldDescriptor(fNumeroTransaction);
            opae27.addFieldDescriptor(fReserve2);
            opae27.addFieldDescriptor(fGenrePaiement);
            opae27.addFieldDescriptor(fReserve3);
            opae27.addFieldDescriptor(fCodeIsoMonnaieDepot);
            opae27.addFieldDescriptor(fMontantDepot);
            opae27.addFieldDescriptor(fReserve4);
            opae27.addFieldDescriptor(fCodeIsoMonnaieBonification);
            opae27.addFieldDescriptor(fCodeIsoPays);
            opae27.addFieldDescriptor(fClearing);
            opae27.addFieldDescriptor(fNoCompteBancaireBeneficiaire);
            opae27.addFieldDescriptor(fNomDestinataire);
            opae27.addFieldDescriptor(fDesignationDestinataire);
            opae27.addFieldDescriptor(fRueDestinataire);
            opae27.addFieldDescriptor(fNPADestinataire);
            opae27.addFieldDescriptor(fLieuDestinataire);
            opae27.addFieldDescriptor(fNomBeneficiaire);
            opae27.addFieldDescriptor(fDesignationBeneficiaire);
            opae27.addFieldDescriptor(fRueBeneficiaire);
            opae27.addFieldDescriptor(fNPABeneficiaire);
            opae27.addFieldDescriptor(fLieuBeneficiaire);
            opae27.addFieldDescriptor(fCommunication);
            opae27.addFieldDescriptor(fReserve6);
            opae27.addFieldDescriptor(fNomDonneurOrdre);
            opae27.addFieldDescriptor(fDesignationDonneurOrdre);
            opae27.addFieldDescriptor(fRueDonneurOrdre);
            opae27.addFieldDescriptor(fNPADonneurOrdre);
            opae27.addFieldDescriptor(fLieuDonneurOrdre);

            // OPAE 28
            opae28.setLength(700);
            opae28.addFieldDescriptor(fIdFichier);
            opae28.addFieldDescriptor(fDateEcheance);
            opae28.addFieldDescriptor(fReserve1);
            opae28.addFieldDescriptor(fElementCommande);
            opae28.addFieldDescriptor(fCompteDebit);
            opae28.addFieldDescriptor(fCompdeDebitTaxes);
            opae28.addFieldDescriptor(fNumeroOG);
            opae28.addFieldDescriptor(fGenreTransaction);
            opae28.addFieldDescriptor(fNumeroTransaction);
            opae28.addFieldDescriptor(fReserve2);
            opae28.addFieldDescriptor(fGenrePaiement);
            opae28.addFieldDescriptor(fReserve3);
            opae28.addFieldDescriptor(fCodeIsoMonnaieDepot);
            opae28.addFieldDescriptor(fMontantDepot);
            opae28.addFieldDescriptor(fReserve4);
            opae28.addFieldDescriptor(fCodeIsoMonnaieBonification);
            opae28.addFieldDescriptor(fCodeIsoPays);
            opae28.addFieldDescriptor(fModulo11);
            opae28.addFieldDescriptor(fNoAdherent);
            opae28.addFieldDescriptor(fNoReference);
            opae28.addFieldDescriptor(fReferenceExpediteur);

            // Définition OPAE 37 (virement bancaire international)
            opae37.setLength(700);
            opae37.addFieldDescriptor(fIdFichier);
            opae37.addFieldDescriptor(fDateEcheance);
            opae37.addFieldDescriptor(fReserve1);
            opae37.addFieldDescriptor(fElementCommande);
            opae37.addFieldDescriptor(fCompteDebit);
            opae37.addFieldDescriptor(fCompdeDebitTaxes);
            opae37.addFieldDescriptor(fNumeroOG);
            opae37.addFieldDescriptor(fGenreTransaction);
            opae37.addFieldDescriptor(fNumeroTransaction);
            opae37.addFieldDescriptor(fReserve2);
            opae37.addFieldDescriptor(fGenrePaiement);
            opae37.addFieldDescriptor(fReserve3);
            opae37.addFieldDescriptor(fCodeIsoMonnaieDepot);
            opae37.addFieldDescriptor(fMontantDepot);
            opae37.addFieldDescriptor(fReserve4);
            opae37.addFieldDescriptor(fCodeIsoMonnaieBonification);
            opae37.addFieldDescriptor(fCodeIsoPays);
            opae37.addFieldDescriptor(fSwift);
            opae37.addFieldDescriptor(fNoCompteBancaireBeneficiaire);
            opae37.addFieldDescriptor(fNomDestinataire);
            opae37.addFieldDescriptor(fDesignationDestinataire);
            opae37.addFieldDescriptor(fRueDestinataire);
            opae37.addFieldDescriptor(fNPADestinataire);
            opae37.addFieldDescriptor(fLieuDestinataire);
            opae37.addFieldDescriptor(fNomBeneficiaire);
            opae37.addFieldDescriptor(fDesignationBeneficiaire);
            opae37.addFieldDescriptor(fRueBeneficiaire);
            opae37.addFieldDescriptor(fNPABeneficiaire);
            opae37.addFieldDescriptor(fLieuBeneficiaire);
            opae37.addFieldDescriptor(fCommunication);
            opae37.addFieldDescriptor(fCodeTaxe);
            opae37.addFieldDescriptor(fUrgent);
            opae37.addFieldDescriptor(fNomDonneurOrdre);
            opae37.addFieldDescriptor(fDesignationDonneurOrdre);
            opae37.addFieldDescriptor(fRueDonneurOrdre);
            opae37.addFieldDescriptor(fNPADonneurOrdre);
            opae37.addFieldDescriptor(fLieuDonneurOrdre);

            // Définition OPAE 34 (mandats de poste du service international
            // (Postcash))
            opae34.setLength(700);
            opae34.addFieldDescriptor(fIdFichier);
            opae34.addFieldDescriptor(fDateEcheance);
            opae34.addFieldDescriptor(fReserve1);
            opae34.addFieldDescriptor(fElementCommande);
            opae34.addFieldDescriptor(fCompteDebit);
            opae34.addFieldDescriptor(fCompdeDebitTaxes);
            opae34.addFieldDescriptor(fNumeroOG);
            opae34.addFieldDescriptor(fGenreTransaction);
            opae34.addFieldDescriptor(fNumeroTransaction);
            opae34.addFieldDescriptor(fReserve2);
            opae34.addFieldDescriptor(fGenrePaiement);
            opae34.addFieldDescriptor(fReserve3);
            opae34.addFieldDescriptor(fCodeIsoMonnaieDepot);
            opae34.addFieldDescriptor(fMontantDepot);
            opae34.addFieldDescriptor(fReserve4);
            opae34.addFieldDescriptor(fCodeIsoMonnaieBonification);
            opae34.addFieldDescriptor(fCodeIsoPays);
            opae34.addFieldDescriptor(fReserve7);
            opae34.addFieldDescriptor(fReserve8);
            opae34.addFieldDescriptor(fNomDestinataire);
            opae34.addFieldDescriptor(fDesignationDestinataire);
            opae34.addFieldDescriptor(fRueDestinataire);
            opae34.addFieldDescriptor(fNPADestinataire);
            opae34.addFieldDescriptor(fLieuDestinataire);
            opae34.addFieldDescriptor(fReserve9);
            opae34.addFieldDescriptor(fCommunication);
            opae34.addFieldDescriptor(fCodeTaxe);
            opae34.addFieldDescriptor(fReserve10);
            opae34.addFieldDescriptor(fNomDonneurOrdre);
            opae34.addFieldDescriptor(fDesignationDonneurOrdre);
            opae34.addFieldDescriptor(fRueDonneurOrdre);
            opae34.addFieldDescriptor(fNPADonneurOrdre);
            opae34.addFieldDescriptor(fLieuDonneurOrdre);

            // Définition OPAE 32 (virements sur un compte postal du service
            // international (PostGiro)
            opae32.setLength(700);
            opae32.addFieldDescriptor(fIdFichier);
            opae32.addFieldDescriptor(fDateEcheance);
            opae32.addFieldDescriptor(fReserve1);
            opae32.addFieldDescriptor(fElementCommande);
            opae32.addFieldDescriptor(fCompteDebit);
            opae32.addFieldDescriptor(fCompdeDebitTaxes);
            opae32.addFieldDescriptor(fNumeroOG);
            opae32.addFieldDescriptor(fGenreTransaction);
            opae32.addFieldDescriptor(fNumeroTransaction);
            opae32.addFieldDescriptor(fReserve2);
            opae32.addFieldDescriptor(fGenrePaiement);
            opae32.addFieldDescriptor(fReserve3);
            opae32.addFieldDescriptor(fCodeIsoMonnaieDepot);
            opae32.addFieldDescriptor(fMontantDepot);
            opae32.addFieldDescriptor(fReserve4);
            opae32.addFieldDescriptor(fCodeIsoMonnaieBonification);
            opae32.addFieldDescriptor(fCodeIsoPays);
            opae32.addFieldDescriptor(fReserve7);
            opae32.addFieldDescriptor(fccpDestinataireOpae32);
            opae32.addFieldDescriptor(fNomDestinataire);
            opae32.addFieldDescriptor(fDesignationDestinataire);
            opae32.addFieldDescriptor(fRueDestinataire);
            opae32.addFieldDescriptor(fNPADestinataire);
            opae32.addFieldDescriptor(fLieuDestinataire);
            opae32.addFieldDescriptor(fReserve9);
            opae32.addFieldDescriptor(fCommunication);
            opae32.addFieldDescriptor(fCodeTaxe);
            opae32.addFieldDescriptor(fReserve10);
            opae32.addFieldDescriptor(fNomDonneurOrdre);
            opae32.addFieldDescriptor(fDesignationDonneurOrdre);
            opae32.addFieldDescriptor(fRueDonneurOrdre);
            opae32.addFieldDescriptor(fNPADonneurOrdre);
            opae32.addFieldDescriptor(fLieuDonneurOrdre);

            // OPAE 97
            opae97Item.setLength(22);
            opae97Item.addFieldDescriptor(fCodeIsoMonnaieTotal);
            opae97Item.addFieldDescriptor(fTotalTransaction);
            opae97Item.addFieldDescriptor(fTotalMontant);

            opae97.setLength(700);
            opae97.addFieldDescriptor(fIdFichier);
            opae97.addFieldDescriptor(fDateEcheance);
            opae97.addFieldDescriptor(fReserve1);
            opae97.addFieldDescriptor(fElementCommande);
            opae97.addFieldDescriptor(fCompteDebit);
            opae97.addFieldDescriptor(fCompdeDebitTaxes);
            opae97.addFieldDescriptor(fNumeroOG);
            opae97.addFieldDescriptor(fGenreTransaction);
            opae97.addFieldDescriptor(fNumeroTransaction);
            opae97.addFieldDescriptor(fReserve2);
            opae97.addFieldDescriptor(fGenrePaiement);
            opae97.addFieldDescriptor(fReserve3);
            opae97.addFieldDescriptor(fTotalMonnaie1);
            opae97.addFieldDescriptor(fTotalMonnaie2);
            opae97.addFieldDescriptor(fTotalMonnaie3);
            opae97.addFieldDescriptor(fTotalMonnaie4);
            opae97.addFieldDescriptor(fTotalMonnaie5);
            opae97.addFieldDescriptor(fTotalMonnaie6);
            opae97.addFieldDescriptor(fTotalMonnaie7);
            opae97.addFieldDescriptor(fTotalMonnaie8);
            opae97.addFieldDescriptor(fTotalMonnaie9);
            opae97.addFieldDescriptor(fTotalMonnaie10);
            opae97.addFieldDescriptor(fTotalMonnaie11);
            opae97.addFieldDescriptor(fTotalMonnaie12);
            opae97.addFieldDescriptor(fTotalMonnaie13);
            opae97.addFieldDescriptor(fTotalMonnaie14);
            opae97.addFieldDescriptor(fTotalMonnaie15);

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }

    }

    /**
     * Commentaire relatif à la méthode format.
     */
    @Override
    public StringBuffer format(APICommonOdreVersement ov) throws Exception {

        StringBuffer sb = new StringBuffer();

        // Vérifications
        if (ov == null) {
            return null;
        }

        if (ordreGroupe == null) {
            return null;
        }

        // Sous contrôle d'exceptions
        try {
            if (ov.getAdressePaiement() != null) {
                // Formattage de l'adresse de paiement
                CAAdressePaiementFormatter adp = new CAAdressePaiementFormatter();
                adp.setAdressePaiement(ov.getAdressePaiement());
                // Sélection fonction du genre d'adresse de paiement
                if (!JadeStringUtil.isBlankOrZero(ov.getReferenceBVR())) {
                    sb = formatBvr(ov); // Cas pour la comptabilité fournisseur ou il y a un BVR avec numéro de
                                        // référence.
                } else if (adp.getTypeAdresse().equals(IntAdressePaiement.CCP)) {
                    sb = formatCCP(ov);
                } else if (adp.getTypeAdresse().equals(IntAdressePaiement.BANQUE)) {
                    sb = formatBanque(ov);
                } else if (adp.getTypeAdresse().equals(IntAdressePaiement.MANDAT)) {
                    sb = formatMandat(ov);
                } else if (adp.getTypeAdresse().equals(IntAdressePaiement.BVR)) {
                    sb = formatBvr(ov);
                } else if (adp.getTypeAdresse().equals(IntAdressePaiement.BANQUE_INTERNATIONAL)) {
                    sb = formatBanqueInternational(ov);
                } else if (adp.getTypeAdresse().equals(IntAdressePaiement.MANDAT_INTERNATIONAL)) {
                    sb = formatPostCash(ov);
                } else if (adp.getTypeAdresse().equals(IntAdressePaiement.CCP_INTERNATIONAL)) {
                    sb = formatPostGiro(ov);
                } else {
                    getMemoryLog()
                            .logMessage("5206", adp.getTypeAdresse(), FWMessage.ERREUR, this.getClass().getName());
                    return null;
                }
            } else {
                sb = formatMandatCourrier(ov);
            }

            // Insérer un retour chariot
            if (getInsertNewLine()) {
                sb.append(CAOrdreFormateur.CRLF);
            }

            // Transmettre le buffer
            if (sb != null) {
                getPrintWriter().print(sb);

                // Echo à la console
                if (getEchoToConsole()) {
                    if (getInsertNewLine()) {
                        System.out.print(sb);
                    } else {
                        System.out.println(sb);
                    }
                }
            }

        } catch (Exception e) {
            String detail = "idOperation : " + ov.getIdOperation();

            try {
                detail = ", " + ov.getNomPrenom();
            } catch (Exception eDetail) {
                // do nothing
            }

            getMemoryLog().logMessage(e.getMessage() + detail, FWMessage.FATAL, this.getClass().getName());
        }

        return sb;
    }

    /**
     * Commentaire relatif à la méthode format.
     */
    @Override
    public StringBuffer format(globaz.osiris.db.comptes.CAOperationOrdreRecouvrement or) throws Exception {

        return null;

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.04.2002 13:11:09)
     * 
     * @param ordreVersement
     *            globaz.osiris.db.comptes.APICommonOdreVersement
     * @param buffer
     *            java.lang.StringBuffer
     */
    private StringBuffer formatBanque(APICommonOdreVersement ov) throws Exception {
        // Récupérer l'adresse de paiement
        CAAdressePaiementFormatter adp = getAdressePaiement(ov);

        // Genre de transaction
        opae27.setFieldValue(CAProcessFormatOrdreOPAE.GENRE_TRANSACTION, "27");

        setCommonFieldValues(ov, opae27);

        // Code ISO pays
        opae27.setFieldValue(CAProcessFormatOrdreOPAE.CODE_ISO_PAYS, "CH");

        // No de clearing (ajout de zéros pour compléter le clearing sur 6
        // positions)
        String sClearing = new String();
        sClearing = adp.getClearing();
        if (sClearing.length() < 6) {
            sClearing = sClearing.concat("000000");
            sClearing = sClearing.substring(0, 6);
        }
        opae27.setFieldValue(CAProcessFormatOrdreOPAE.CLEARING, "  " + sClearing);

        // No de compte
        opae27.setFieldValue(CAProcessFormatOrdreOPAE.NO_COMPTE_BANCAIRE_BENEFICIAIRE, adp.getNumCompte());

        // Destinataire : nom
        opae27.clearFieldValue(CAProcessFormatOrdreOPAE.NOM_DESTINATAIRE);

        // Destinataire : désignation supplémentaire
        opae27.clearFieldValue(CAProcessFormatOrdreOPAE.DESIGNATION_DESTINATAIRE);

        // Destinataire : rue
        opae27.clearFieldValue(CAProcessFormatOrdreOPAE.RUE_DESTINATAIRE);

        // Destinataire : npa
        opae27.clearFieldValue(CAProcessFormatOrdreOPAE.NPA_DESTINATAIRE);

        // Destinataire : lieu
        opae27.clearFieldValue(CAProcessFormatOrdreOPAE.LIEU_DESTINATAIRE);

        // Bénéficiaire final : nom
        String beneficiaire = new String();
        if ("".equalsIgnoreCase(adp.getAdresseCourrierBeneficiaire().getAutreNom())) {
            beneficiaire = adp.getNomTiersBeneficiaire();
        } else {
            beneficiaire = adp.getAdresseCourrierBeneficiaire().getAutreNom();
        }
        // Si bénéficiaire > 35 on le coupe
        if (beneficiaire.length() >= 35) {
            beneficiaire = beneficiaire.substring(0, 35);
        }
        opae27.setFieldValue(CAProcessFormatOrdreOPAE.NOM_BENEFICIAIRE, beneficiaire);
        // Bénéficiaire final : désignation supplémentaire
        String[] adrComp = adp.getAdresseCourrierBeneficiaire().getAdresse();
        if ((adrComp == null) || JadeStringUtil.isBlank(adrComp[0])) {
            opae27.clearFieldValue(CAProcessFormatOrdreOPAE.DESIGNATION_BENEFICIAIRE);
        } else {
            if ((adrComp[0] != null) && (adrComp[0].length() >= 35)) {
                opae27.setFieldValue(CAProcessFormatOrdreOPAE.DESIGNATION_BENEFICIAIRE, adrComp[0].substring(0, 35));
            } else {
                opae27.setFieldValue(CAProcessFormatOrdreOPAE.DESIGNATION_BENEFICIAIRE, adrComp[0]);
            }
        }
        // Bénéficiaire final : rue
        if ((adp.getAdresseCourrierBeneficiaire().getRue() != null)
                && (adp.getAdresseCourrierBeneficiaire().getRue().length() >= 35)) {
            opae27.setFieldValue(CAProcessFormatOrdreOPAE.RUE_BENEFICIAIRE, adp.getAdresseCourrierBeneficiaire()
                    .getRue().substring(0, 35));
        } else {
            opae27.setFieldValue(CAProcessFormatOrdreOPAE.RUE_BENEFICIAIRE, adp.getAdresseCourrierBeneficiaire()
                    .getRue());
        }

        // Bénéficiaire final : npa
        opae27.setFieldValue(CAProcessFormatOrdreOPAE.NPA_BENEFICIAIRE, adp.getAdresseCourrierBeneficiaire()
                .getNumPostal());
        // Bénéficiaire final : lieu

        if ((adp.getAdresseCourrierBeneficiaire().getLocalite() != null)
                && (adp.getAdresseCourrierBeneficiaire().getLocalite().length() >= 25)) {
            opae27.setFieldValue(CAProcessFormatOrdreOPAE.LIEU_BENEFICIAIRE, adp.getAdresseCourrierBeneficiaire()
                    .getLocalite().substring(0, 25));
        } else {
            opae27.setFieldValue(CAProcessFormatOrdreOPAE.LIEU_BENEFICIAIRE, adp.getAdresseCourrierBeneficiaire()
                    .getLocalite());
        }

        // Communications
        opae27.setFieldValue(CAProcessFormatOrdreOPAE.COMMUNICATION, ov.getMotifFormatOPAE());

        // Récupérer le record et le convertir dans le jeu de caractères swifts
        StringBuffer sb = new StringBuffer(JAUtil.toSwiftCharacterSet(opae27.createRecord(getSession())));

        return sb;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.04.2002 13:11:09)
     * 
     * @param ordreVersement
     *            globaz.osiris.db.comptes.APICommonOdreVersement
     * @param buffer
     *            java.lang.StringBuffer
     */
    private StringBuffer formatBanqueInternational(APICommonOdreVersement ov) throws Exception {
        // Récupérer l'adresse de paiement
        CAAdressePaiementFormatter adp = getAdressePaiement(ov);

        // Genre de transaction
        opae37.setFieldValue(CAProcessFormatOrdreOPAE.GENRE_TRANSACTION, "37");

        setCommonFieldValues(ov, opae37);

        // Code ISO pays
        opae37.setFieldValue(CAProcessFormatOrdreOPAE.CODE_ISO_PAYS, adp.getAdresseCourrierBanque().getPaysISO());

        // Code swift
        String codeSwift = adp.getCodeSwiftWithoutSpaces();

        if ((JadeStringUtil.isBlank(codeSwift))
                || (codeSwift.length() < CAAdressePaiementFormatter.CODE_SWIFT_FORMAT_MIN_LENGTH)
                || (codeSwift.length() > CAAdressePaiementFormatter.CODE_SWIFT_FORMAT_MAX_LENGTH)) {
            getMemoryLog().logMessage("5228", null, FWMessage.FATAL, this.getClass().getName());
            return null;
        }

        opae37.setFieldValue(CAProcessFormatOrdreOPAE.SWIFT,
                JadeStringUtil.leftJustify(codeSwift, CAAdressePaiementFormatter.CODE_SWIFT_FORMAT_MAX_LENGTH));

        // No de compte
        opae37.setFieldValue(CAProcessFormatOrdreOPAE.NO_COMPTE_BANCAIRE_BENEFICIAIRE, adp.getNumCompte());

        String[] adrComp = setInternationalDestinataire(adp, opae37);

        // Bénéficiaire final
        // Bénéficiaire final : nom
        String beneficiaire = new String();
        if ("".equalsIgnoreCase(adp.getAdresseCourrierBeneficiaire().getAutreNom())) {
            beneficiaire = adp.getNomTiersBeneficiaire();
        } else {
            beneficiaire = adp.getAdresseCourrierBeneficiaire().getAutreNom();
        }
        // Si bénéficiaire > 35 on le coupe
        if (beneficiaire.length() >= 35) {
            beneficiaire = beneficiaire.substring(0, 35);
        }
        opae37.setFieldValue(CAProcessFormatOrdreOPAE.NOM_BENEFICIAIRE, beneficiaire);
        adrComp = adp.getAdresseCourrierBeneficiaire().getAdresse();
        if ((adrComp == null) || JadeStringUtil.isBlank(adrComp[0])) {
            opae37.clearFieldValue(CAProcessFormatOrdreOPAE.DESIGNATION_BENEFICIAIRE);
        } else {
            if ((adrComp[0] != null) && (adrComp[0].length() >= 35)) {
                opae37.setFieldValue(CAProcessFormatOrdreOPAE.DESIGNATION_BENEFICIAIRE, adrComp[0].substring(0, 35));
            } else {
                opae37.setFieldValue(CAProcessFormatOrdreOPAE.DESIGNATION_BENEFICIAIRE, adrComp[0]);
            }
        }

        if ((adp.getAdresseCourrierBeneficiaire().getRue() != null)
                && (adp.getAdresseCourrierBeneficiaire().getRue().length() >= 35)) {
            opae37.setFieldValue(CAProcessFormatOrdreOPAE.RUE_BENEFICIAIRE, adp.getAdresseCourrierBeneficiaire()
                    .getRue().substring(0, 35));
        } else {
            opae37.setFieldValue(CAProcessFormatOrdreOPAE.RUE_BENEFICIAIRE, adp.getAdresseCourrierBeneficiaire()
                    .getRue());
        }

        opae37.setFieldValue(CAProcessFormatOrdreOPAE.NPA_BENEFICIAIRE, adp.getAdresseCourrierBeneficiaire()
                .getNumPostal());
        if ((adp.getAdresseCourrierBeneficiaire().getLocalite() != null)
                && (adp.getAdresseCourrierBeneficiaire().getLocalite().length() >= 25)) {
            opae37.setFieldValue(CAProcessFormatOrdreOPAE.LIEU_BENEFICIAIRE, adp.getAdresseCourrierBeneficiaire()
                    .getLocalite().substring(0, 25));
        } else {
            opae37.setFieldValue(CAProcessFormatOrdreOPAE.LIEU_BENEFICIAIRE, adp.getAdresseCourrierBeneficiaire()
                    .getLocalite());
        }

        // Communications
        opae37.setFieldValue(CAProcessFormatOrdreOPAE.COMMUNICATION, ov.getMotifFormatOPAE());

        // Code taxes
        opae37.setFieldValue(CAProcessFormatOrdreOPAE.CODE_TAXE,
                CAProcessFormatOrdreOPAE.FRAIS_PRIS_EN_CHARGE_PAR_DONNEUR_ORDRE);

        // Urgent
        opae37.clearFieldValue(CAProcessFormatOrdreOPAE.URGENT);

        // Récupérer le record et le convertir en codes swifts
        StringBuffer sb = new StringBuffer(JAUtil.toSwiftCharacterSet(opae37.createRecord(getSession())));

        return sb;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.04.2002 13:11:09)
     * 
     * @param ordreVersement
     *            globaz.osiris.db.comptes.APICommonOdreVersement
     * @param buffer
     *            java.lang.StringBuffer
     */
    private StringBuffer formatBvr(APICommonOdreVersement ov) throws Exception {
        // Récupérer l'adresse de paiement
        CAAdressePaiementFormatter adp = getAdressePaiement(ov);

        // Genre de transaction
        opae28.setFieldValue(CAProcessFormatOrdreOPAE.GENRE_TRANSACTION, "28");

        setCommonFieldValues(ov, opae28);

        // Code ISO pays
        opae28.setFieldValue(CAProcessFormatOrdreOPAE.CODE_ISO_PAYS, "CH");

        // Chiffre clé modulo 11
        opae28.clearFieldValue(CAProcessFormatOrdreOPAE.MODULO_11);

        // Numéro d'adhérent à 5 ou 9 positions
        if (adp.isAdherentBvr5()) {
            opae28.setFieldValue(CAProcessFormatOrdreOPAE.NO_ADHERENT, adp.getNumCompte());
        } else {
            opae28.setFieldValue(CAProcessFormatOrdreOPAE.NO_ADHERENT, JACCP.formatNoDash(adp.getNumCompte()));
        }

        // Numéro de référence
        opae28.setFieldValue(CAProcessFormatOrdreOPAE.NO_REFERENCE, ov.getReferenceBVR());

        // Reference expéditeur
        if (ov.getNomPrenom().length() >= 35) {
            opae28.setFieldValue(CAProcessFormatOrdreOPAE.REFERENCE_EXPEDITEUR, ov.getNomPrenom().substring(0, 35));
        } else {
            opae28.setFieldValue(CAProcessFormatOrdreOPAE.REFERENCE_EXPEDITEUR, ov.getNomPrenom());
        }

        return new StringBuffer(opae28.createRecord(getSession()));

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.04.2002 13:11:09)
     * 
     * @param ordreVersement
     *            globaz.osiris.db.comptes.APICommonOdreVersement
     * @param buffer
     *            java.lang.StringBuffer
     */
    private StringBuffer formatCCP(APICommonOdreVersement ov) throws Exception {
        // Récupérer l'adresse de paiement
        CAAdressePaiementFormatter adp = getAdressePaiement(ov);

        // Genre de transaction
        opae22.setFieldValue(CAProcessFormatOrdreOPAE.GENRE_TRANSACTION, "22");

        setCommonFieldValues(ov, opae22);

        // Code ISO pays
        opae22.setFieldValue(CAProcessFormatOrdreOPAE.CODE_ISO_PAYS, "CH");

        // CCP destinataire
        opae22.setFieldValue(CAProcessFormatOrdreOPAE.CCP_DESTINATAIRE, JACCP.formatNoDash(adp.getNumCompte()));

        // No de compte
        opae22.clearFieldValue(CAProcessFormatOrdreOPAE.NO_COMPTE_BANCAIRE_BENEFICIAIRE);

        setCommonDestinataireFields(adp, opae22);

        // Bénéficiaire final
        opae22.clearFieldValue(CAProcessFormatOrdreOPAE.NOM_BENEFICIAIRE);
        opae22.clearFieldValue(CAProcessFormatOrdreOPAE.DESIGNATION_BENEFICIAIRE);
        opae22.clearFieldValue(CAProcessFormatOrdreOPAE.RUE_BENEFICIAIRE);
        opae22.clearFieldValue(CAProcessFormatOrdreOPAE.NPA_BENEFICIAIRE);
        opae22.clearFieldValue(CAProcessFormatOrdreOPAE.LIEU_BENEFICIAIRE);

        // Communications
        opae22.setFieldValue(CAProcessFormatOrdreOPAE.COMMUNICATION, ov.getMotifFormatOPAE());

        return new StringBuffer(opae22.createRecord(getSession()));
    }

    /**
     * Commentaire relatif à la méthode formatEOF.
     */
    @Override
    public StringBuffer formatEOF(APIOrdreGroupe og) throws Exception {

        // Sous contrôle d'exceptions
        try {
            // Vérifications
            if (og == null) {
                return null;
            }
            if (ordreGroupe == null) {
                return null;
            }

            // genre de transaction
            opae97.setFieldValue(CAProcessFormatOrdreOPAE.GENRE_TRANSACTION, "97");

            // nombre de transactions
            int n = Integer.parseInt(og.getNbTransactions());
            n++;
            opae97.setFieldValue(CAProcessFormatOrdreOPAE.NUMERO_TRANSACTION, String.valueOf(n));

            // Total de la monnaie CHF
            opae97Item.clear();
            opae97Item.setFieldValue(CAProcessFormatOrdreOPAE.CODE_ISO_MONNAIRE_TOTAL, "CHF");
            opae97Item.setFieldValue(CAProcessFormatOrdreOPAE.TOTAL_TRANSACTIONS, og.getNbTransactions());
            opae97Item.setFieldValue(CAProcessFormatOrdreOPAE.TOTAL_MONTANT, og.getTotal());
            opae97.setFieldValue(CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_1, opae97Item.createRecord(getSession()));

            // Les autres monnaies sont à zéro
            opae97Item.clear();
            opae97Item.setFieldValue(CAProcessFormatOrdreOPAE.CODE_ISO_MONNAIRE_TOTAL, "000");
            opae97Item.setFieldValue(CAProcessFormatOrdreOPAE.TOTAL_TRANSACTIONS, "0");
            opae97Item.setFieldValue(CAProcessFormatOrdreOPAE.TOTAL_MONTANT, "0.00");
            String sTemp = opae97Item.createRecord(getSession());
            opae97.setFieldValue(CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_2, sTemp);
            opae97.setFieldValue(CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_3, sTemp);
            opae97.setFieldValue(CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_4, sTemp);
            opae97.setFieldValue(CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_5, sTemp);
            opae97.setFieldValue(CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_6, sTemp);
            opae97.setFieldValue(CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_7, sTemp);
            opae97.setFieldValue(CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_8, sTemp);
            opae97.setFieldValue(CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_9, sTemp);
            opae97.setFieldValue(CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_10, sTemp);
            opae97.setFieldValue(CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_11, sTemp);
            opae97.setFieldValue(CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_12, sTemp);
            opae97.setFieldValue(CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_13, sTemp);
            opae97.setFieldValue(CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_14, sTemp);
            opae97.setFieldValue(CAProcessFormatOrdreOPAE.TOTAL_MONNAIE_15, sTemp);

            // Créer l'enregistrement
            StringBuffer sb = new StringBuffer(opae97.createRecord(getSession()));

            // New line
            if (getInsertNewLine()) {
                sb.append(CAOrdreFormateur.CRLF);
            }

            // Transmettre le record
            getPrintWriter().print(sb);

            // Echo à la console
            if (getEchoToConsole()) {
                if (getInsertNewLine()) {
                    System.out.print(sb);
                } else {
                    System.out.println(sb);
                }
            }

            return sb;

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return null;
        }

    }

    /**
     * Commentaire relatif à la méthode formatHeader.
     */
    @Override
    public StringBuffer formatHeader(APIOrdreGroupe og) throws Exception {

        // Initialiser
        StringBuffer sb = new StringBuffer();

        // Sous contrôle d'exceptions
        try {

            // Définition des records
            createRecordDefinition();

            // Sauvegarder l'ordre groupé
            ordreGroupe = og;

            // Récupérer l'adresse de paiement
            CAAdressePaiementFormatter adp = new CAAdressePaiementFormatter();
            adp.setAdressePaiement(og.getOrganeExecution().getAdressePaiement());
            adp.checkAdressePaiement(getSession());

            // Vérifier que l'adresse de paiement est une adresse postale
            if (!adp.getTypeAdresse().equals(IntAdressePaiement.CCP)) {
                getMemoryLog().logMessage("5228", null, FWMessage.FATAL, this.getClass().getName());
                return null;
            }

            // Construire l'header
            opaeHeader.clear();
            opaeHeader.setFieldValue(CAProcessFormatOrdreOPAE.ID_FICHIER, "036");

            // Insérer la date d'échéance
            opaeHeader.setFieldValue(CAProcessFormatOrdreOPAE.DATE_ECHEANCE, og.getDateEcheance());

            // Numéro de compte débité
            opaeHeader.setFieldValue(CAProcessFormatOrdreOPAE.COMPTE_DEBIT, (JACCP.formatNoDash(adp.getNumCompte())));

            // Débit des taxes
            if (og.getOrganeExecution().getAdresseDebitTaxes() != null) {
                // Récupérer l'adresse de débit des taxes
                CAAdressePaiementFormatter adpTaxes = new CAAdressePaiementFormatter();
                adpTaxes.setAdressePaiement(og.getOrganeExecution().getAdresseDebitTaxes());

                // Vérifier l'adresse de débit des taxes
                if (!adpTaxes.getTypeAdresse().equals(IntAdressePaiement.CCP)) {
                    getMemoryLog().logMessage("5228", null, FWMessage.FATAL, this.getClass().getName());
                    return null;
                }

                // Débit des taxes
                opaeHeader.setFieldValue(CAProcessFormatOrdreOPAE.COMPTE_DEBIT_TAXES,
                        JACCP.formatNoDash(adpTaxes.getNumCompte()));
            } else {
                opaeHeader.setFieldValue(CAProcessFormatOrdreOPAE.COMPTE_DEBIT_TAXES,
                        JACCP.formatNoDash(adp.getNumCompte()));
            }

            // Numéro d'og
            opaeHeader.setFieldValue(CAProcessFormatOrdreOPAE.NUMERO_OG, og.getNumeroOG());

            // Genre de transaction
            opaeHeader.setFieldValue(CAProcessFormatOrdreOPAE.GENRE_TRANSACTION, "00");

            // Numéro de transaction
            opaeHeader.setFieldValue(CAProcessFormatOrdreOPAE.NUMERO_TRANSACTION, "0");

            // Genre de paiement
            opaeHeader.setFieldValue(CAProcessFormatOrdreOPAE.GENRE_PAIEMENT, "0");

            // Créer l'enregistrement
            sb.append(opaeHeader.createRecord(getSession()));

            // New line
            if (getInsertNewLine()) {
                sb.append(CAOrdreFormateur.CRLF);
            }

            // Transmettre le record
            getPrintWriter().print(sb);

            // Echo à la console
            if (getEchoToConsole()) {
                if (getInsertNewLine()) {
                    System.out.print(sb);
                } else {
                    System.out.println(sb);
                }
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }

        return sb;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.04.2002 13:11:09)
     * 
     * @param ordreVersement
     *            globaz.osiris.db.comptes.APICommonOdreVersement
     * @param buffer
     *            java.lang.StringBuffer
     */
    private StringBuffer formatMandat(APICommonOdreVersement ov) throws Exception {
        // Récupérer l'adresse de paiement
        CAAdressePaiementFormatter adp = getAdressePaiement(ov);

        // Genre de transaction
        opae24.setFieldValue(CAProcessFormatOrdreOPAE.GENRE_TRANSACTION, "24");

        setCommonFieldValues(ov, opae24);

        // Code ISO pays
        opae24.setFieldValue(CAProcessFormatOrdreOPAE.CODE_ISO_PAYS, "CH");

        setCommonDestinataireFields(adp, opae24);

        // Bénéficiaire final
        opae24.clearFieldValue(CAProcessFormatOrdreOPAE.NOM_BENEFICIAIRE);
        opae24.clearFieldValue(CAProcessFormatOrdreOPAE.DESIGNATION_BENEFICIAIRE);
        opae24.clearFieldValue(CAProcessFormatOrdreOPAE.RUE_BENEFICIAIRE);
        opae24.clearFieldValue(CAProcessFormatOrdreOPAE.NPA_BENEFICIAIRE);
        opae24.clearFieldValue(CAProcessFormatOrdreOPAE.LIEU_BENEFICIAIRE);

        // Communications
        opae24.setFieldValue(CAProcessFormatOrdreOPAE.COMMUNICATION, ov.getMotifFormatOPAE());

        return new StringBuffer(opae24.createRecord(getSession()));
    }

    private StringBuffer formatMandatCourrier(APICommonOdreVersement ov) throws Exception {
        // Genre de transaction
        opae24.setFieldValue(CAProcessFormatOrdreOPAE.GENRE_TRANSACTION, "24");

        setCommonFieldValues(ov, opae24);

        // Code ISO pays
        opae24.setFieldValue(CAProcessFormatOrdreOPAE.CODE_ISO_PAYS, "CH");

        setCourrierDestinataireFields(ov, opae24);

        // Bénéficiaire final
        opae24.clearFieldValue(CAProcessFormatOrdreOPAE.NOM_BENEFICIAIRE);
        opae24.clearFieldValue(CAProcessFormatOrdreOPAE.DESIGNATION_BENEFICIAIRE);
        opae24.clearFieldValue(CAProcessFormatOrdreOPAE.RUE_BENEFICIAIRE);
        opae24.clearFieldValue(CAProcessFormatOrdreOPAE.NPA_BENEFICIAIRE);
        opae24.clearFieldValue(CAProcessFormatOrdreOPAE.LIEU_BENEFICIAIRE);

        // Communications
        opae24.setFieldValue(CAProcessFormatOrdreOPAE.COMMUNICATION, ov.getMotif());

        return new StringBuffer(opae24.createRecord(getSession()));
    }

    /**
     * Format l'enregistrement pour mandats de poste du service international (Postcash). Genre de transaction : 34.
     * 
     * @param ov
     * @return L'enregistrement pour mandats formatté.
     * @throws Exception
     */
    private StringBuffer formatPostCash(APICommonOdreVersement ov) throws Exception {
        // Récupérer l'adresse de paiement
        CAAdressePaiementFormatter adp = getAdressePaiement(ov);

        // Genre de transaction
        opae34.setFieldValue(CAProcessFormatOrdreOPAE.GENRE_TRANSACTION, "34");

        setCommonFieldValues(ov, opae34);

        // Code ISO pays
        opae34.setFieldValue(CAProcessFormatOrdreOPAE.CODE_ISO_PAYS, adp.getAdressePaiement().getCodeISOPays());

        setCommonDestinataireFields(adp, opae34);

        // Communications
        opae34.setFieldValue(CAProcessFormatOrdreOPAE.COMMUNICATION, ov.getMotifFormatOPAE());

        // Code taxes
        opae34.setFieldValue(CAProcessFormatOrdreOPAE.CODE_TAXE,
                CAProcessFormatOrdreOPAE.FRAIS_PRIS_EN_CHARGE_PAR_DONNEUR_ORDRE);

        return new StringBuffer(opae34.createRecord(getSession()));
    }

    /**
     * Format l'enregistrement pour virements sur un compte postal du service international (Postgiro) Genre de
     * transaction : 32.
     * 
     * @param ov
     * @return
     * @throws Exception
     */
    private StringBuffer formatPostGiro(APICommonOdreVersement ov) throws Exception {
        // Récupérer l'adresse de paiement
        CAAdressePaiementFormatter adp = getAdressePaiement(ov);

        // Genre de transaction
        opae32.setFieldValue(CAProcessFormatOrdreOPAE.GENRE_TRANSACTION, "32");

        setCommonFieldValues(ov, opae32);

        // Code ISO pays
        opae32.setFieldValue(CAProcessFormatOrdreOPAE.CODE_ISO_PAYS, adp.getAdressePaiement().getCodeISOPays());

        // CCP destinataire
        // Le CCP doit-être aligné à gauche du String
        String numCompte = adp.getNumCompte();
        if (numCompte.length() < CAProcessFormatOrdreOPAE.CCP_DESTINATAIRE_MAX_LENGTH) {
            String rightPart = "";
            for (int i = 0; i < CAProcessFormatOrdreOPAE.CCP_DESTINATAIRE_MAX_LENGTH - numCompte.length(); i++) {
                rightPart += "0";
            }
            numCompte += rightPart;
        }
        opae32.setFieldValue(CAProcessFormatOrdreOPAE.CCP_DESTINATAIRE, numCompte);

        setCommonDestinataireFields(adp, opae32);

        // Communications
        opae32.setFieldValue(CAProcessFormatOrdreOPAE.COMMUNICATION, ov.getMotifFormatOPAE());

        // Code taxes
        opae32.setFieldValue(CAProcessFormatOrdreOPAE.CODE_TAXE,
                CAProcessFormatOrdreOPAE.FRAIS_PRIS_EN_CHARGE_PAR_DONNEUR_ORDRE);

        return new StringBuffer(opae32.createRecord(getSession()));
    }

    /**
     * Récupérer et contrôler l'adresse de paiement.
     * 
     * @param ov
     * @return
     * @throws Exception
     */
    private CAAdressePaiementFormatter getAdressePaiement(APICommonOdreVersement ov) throws Exception {
        CAAdressePaiementFormatter adp = new CAAdressePaiementFormatter();
        adp.setAdressePaiement(ov.getAdressePaiement());
        adp.checkAdressePaiement(getSession());
        return adp;
    }

    /**
     * Retourne un String contenant des espaces.
     * 
     * @param length
     *            Nombre d'espaces que le String doit contenir.
     * @return
     */
    private String getSpaceString(int length) {
        String result = "";
        for (int i = 0; i < length; i++) {
            result += " ";
        }
        return result;
    }

    /**
     * Retourne un String contenant des zéros (0).
     * 
     * @param length
     *            Nombre de zéros que le String doit contenir.
     * @return
     */
    private String getZeroString(int length) {
        String result = "";
        for (int i = 0; i < length; i++) {
            result += "0";
        }
        return result;
    }

    /**
     * Ajout des fields communes aux ordres CCP et Mandats (Suisse et internationaux).
     * 
     * @param adp
     * @param opae
     * @throws Exception
     */
    private void setCommonDestinataireFields(CAAdressePaiementFormatter adp, FWAsciiFileRecordDescriptor opae)
            throws Exception {
        // Destinataire : nom
        String beneficiaire = adp.getNomTiersBeneficiaire();
        // Si bénéficiaire > 35 on le coupe
        if (beneficiaire.length() >= 35) {
            beneficiaire = beneficiaire.substring(0, 35);
        }
        opae.setFieldValue(CAProcessFormatOrdreOPAE.NOM_DESTINATAIRE, beneficiaire);

        // Destinataire : désignation supplémentaire
        String[] adrComp = adp.getAdresseCourrierBeneficiaire().getAdresse();

        if ((adrComp == null) || JadeStringUtil.isBlank(adrComp[0])) {
            opae.clearFieldValue(CAProcessFormatOrdreOPAE.DESIGNATION_DESTINATAIRE);
        } else {
            if (adrComp[0].length() >= 35) {
                opae.setFieldValue(CAProcessFormatOrdreOPAE.DESIGNATION_DESTINATAIRE, adrComp[0].substring(0, 35));
            } else {
                opae.setFieldValue(CAProcessFormatOrdreOPAE.DESIGNATION_DESTINATAIRE, adrComp[0]);
            }
        }

        // Destinataire : rue
        if (adp.getAdresseCourrierBeneficiaire().getRue().length() >= 35) {
            opae.setFieldValue(CAProcessFormatOrdreOPAE.RUE_DESTINATAIRE, adp.getAdresseCourrierBeneficiaire().getRue()
                    .substring(0, 35));
        } else {
            opae.setFieldValue(CAProcessFormatOrdreOPAE.RUE_DESTINATAIRE, adp.getAdresseCourrierBeneficiaire().getRue());
        }

        // Destinataire : npa
        opae.setFieldValue(CAProcessFormatOrdreOPAE.NPA_DESTINATAIRE, adp.getAdresseCourrierBeneficiaire()
                .getNumPostal());

        // Destinataire : lieu
        if ((adp.getAdresseCourrierBeneficiaire().getLocalite() != null)
                && (adp.getAdresseCourrierBeneficiaire().getLocalite().length() >= 25)) {
            opae.setFieldValue(CAProcessFormatOrdreOPAE.LIEU_DESTINATAIRE, adp.getAdresseCourrierBeneficiaire()
                    .getLocalite().substring(0, 25));
        } else {
            opae.setFieldValue(CAProcessFormatOrdreOPAE.LIEU_DESTINATAIRE, adp.getAdresseCourrierBeneficiaire()
                    .getLocalite());
        }
    }

    /**
     * Défini les valeurs des champs communs des enregistrements.
     * 
     * @param ov
     * @param opae
     * @throws Exception
     */
    private void setCommonFieldValues(APICommonOdreVersement ov, FWAsciiFileRecordDescriptor opae) throws Exception {
        // Numéro transaction
        opae.setFieldValue(CAProcessFormatOrdreOPAE.NUMERO_TRANSACTION, ov.getNumTransaction());

        // Code ISO
        opae.setFieldValue(CAProcessFormatOrdreOPAE.CODE_ISO_MONNAIE_DEPOT, ov.getCodeISOMonnaieDepot());

        // Montant
        opae.setFieldValue(CAProcessFormatOrdreOPAE.MONTANT_DEPOT, ov.getMontant());

        // Monnaie de bonification
        opae.setFieldValue(CAProcessFormatOrdreOPAE.CODE_ISO_MONNAIE_BONIFICATION, ov.getCodeISOMonnaieBonification());
    }

    private void setCourrierDestinataireFields(APICommonOdreVersement ov, FWAsciiFileRecordDescriptor opae)
            throws Exception {
        // On recherche le tiers
        TIAdresseDataSource d = ov.getDataSourceAdresseCourrier();
        // Destinataire : nom
        String beneficiaire = ov.getNomPrenom();
        // Si bénéficiaire > 35 on le coupe
        if (beneficiaire.length() >= 35) {
            beneficiaire = beneficiaire.substring(0, 35);
        }
        opae.setFieldValue(CAProcessFormatOrdreOPAE.NOM_DESTINATAIRE, beneficiaire);

        // Désignation supplémentaire
        opae.clearFieldValue(CAProcessFormatOrdreOPAE.DESIGNATION_DESTINATAIRE);

        // Destinataire : rue
        String rue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE) + " "
                + d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO);
        if (rue.length() >= 35) {
            opae.setFieldValue(CAProcessFormatOrdreOPAE.RUE_DESTINATAIRE, rue.substring(0, 35));
        } else {
            opae.setFieldValue(CAProcessFormatOrdreOPAE.RUE_DESTINATAIRE, rue);
        }

        // Destinataire : npa
        opae.setFieldValue(CAProcessFormatOrdreOPAE.NPA_DESTINATAIRE,
                d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA));

        // Destinataire : lieu
        String lieu = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
        if (lieu.length() >= 25) {
            opae.setFieldValue(CAProcessFormatOrdreOPAE.LIEU_DESTINATAIRE, lieu.substring(0, 35));
        } else {
            opae.setFieldValue(CAProcessFormatOrdreOPAE.LIEU_DESTINATAIRE, lieu);
        }
    }

    /**
     * Défini le destinataire international.
     * 
     * @param adp
     * @param opae
     * @return Adresse complète du destinataire.
     * @throws Exception
     */
    private String[] setInternationalDestinataire(CAAdressePaiementFormatter adp, FWAsciiFileRecordDescriptor opae)
            throws Exception {
        // Destinataire : nom
        if (!JadeStringUtil.isBlank(adp.getTiersBanque().getNom()) && (adp.getTiersBanque().getNom().length() >= 35)) {
            opae.setFieldValue(CAProcessFormatOrdreOPAE.NOM_DESTINATAIRE, adp.getTiersBanque().getNom()
                    .substring(0, 35));
        } else {
            opae.setFieldValue(CAProcessFormatOrdreOPAE.NOM_DESTINATAIRE, adp.getTiersBanque().getNom());
        }

        // Destinataire : désignation supplémentaire
        String[] adrComp = adp.getAdresseCourrierBanque().getAdresse();
        if ((adrComp == null) || JadeStringUtil.isBlank(adrComp[0])) {
            opae.clearFieldValue(CAProcessFormatOrdreOPAE.DESIGNATION_DESTINATAIRE);
        } else {
            if (adrComp[0].length() >= 35) {
                opae.setFieldValue(CAProcessFormatOrdreOPAE.DESIGNATION_DESTINATAIRE, adrComp[0].substring(0, 35));
            } else {
                opae.setFieldValue(CAProcessFormatOrdreOPAE.DESIGNATION_DESTINATAIRE, adrComp[0]);
            }
        }

        // Destinataire : rue
        if (adp.getAdresseCourrierBanque().getRue().length() >= 35) {
            opae.setFieldValue(CAProcessFormatOrdreOPAE.RUE_DESTINATAIRE, adp.getAdresseCourrierBanque().getRue()
                    .substring(0, 35));
        } else {
            opae.setFieldValue(CAProcessFormatOrdreOPAE.RUE_DESTINATAIRE, adp.getAdresseCourrierBanque().getRue());
        }

        // Destinataire : npa
        opae.setFieldValue(CAProcessFormatOrdreOPAE.NPA_DESTINATAIRE, adp.getAdresseCourrierBanque().getNumPostal());

        // Destinataire : lieu
        if (adp.getAdresseCourrierBanque().getLocalite().length() >= 25) {
            opae.setFieldValue(CAProcessFormatOrdreOPAE.LIEU_DESTINATAIRE, adp.getAdresseCourrierBanque().getLocalite()
                    .substring(0, 25));
        } else {
            opae.setFieldValue(CAProcessFormatOrdreOPAE.LIEU_DESTINATAIRE, adp.getAdresseCourrierBanque().getLocalite());
        }

        return adrComp;
    }
}
