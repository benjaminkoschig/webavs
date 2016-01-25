package globaz.osiris.db.ordres.format;

import globaz.framework.filetransfer.FWAsciiFileFieldDescriptor;
import globaz.framework.filetransfer.FWAsciiFileRecordDescriptor;
import globaz.framework.util.FWMessage;
import globaz.globall.util.JACCP;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.ordre.APICommonOdreVersement;
import globaz.osiris.api.ordre.APIOrdreGroupe;
import globaz.osiris.db.comptes.CAOperationOrdreRecouvrement;
import globaz.osiris.db.ordres.format.utils.CAOrdreFormatterUtils;
import globaz.osiris.db.utils.CAAdressePaiementFormatter;
import globaz.osiris.external.IntAdressePaiement;

/**
 * Insérez la description du type ici. Date de création : (08.04.2002 09:05:57)
 * 
 * @author: Administrator
 */
public final class CAProcessFormatOrdreLSV extends CAOrdreFormateur {
    private static final String CCP_DEBITEUR = "ccpDebiteur";
    private static final String CODE_ISO = "codeISO";
    private static final String CODE_ISOPRIX = "codeISOPrix";
    private static final String CODE_MONNAIE = "codeMonnaie";
    private static final String DATE_ECHEANCE = "dateEcheance";
    private static final String DATE_ECRITURE = "dateEcriture";
    private static final String DATE_VALEUR = "dateValeur";
    private static final String DESIGNATION_SUPP_DEBITEUR = "designationSuppDebiteur";
    private static final String DESIGNATION_SUPP_DONNER_ORDRE = "designationSuppDonnerOrdre";
    private static final String ID_FICHIER = "idFichier";
    private static final String LIEU_DEBITEUR = "lieuDebiteur";
    private static final String LIEU_DONNEUR_ORDRE = "lieuDonneurOrdre";
    private static final String NO_REFERENCE = "noReference";
    private static final String NOM_DEBITEUR = "nomDebiteur";
    private static final String NOM_DONNEUR_ORDRE = "nomDonneurOrdre";
    private static final String NPADEBITEUR = "NPADebiteur";
    private static final String NPADONNEUR_ORDRE = "NPADonneurOrdre";
    private static final String NUMERO_ADHERENT = "numeroAdherent";
    private static final String NUMERO_OG = "numeroOG";
    private static final String NUMERO_TRANSACTION = "numeroTransaction";
    private static final String PRIX = "prix";
    private static final String REF_REMISE = "refRemise";
    private static final String REFUS_CONTRE_PASSATION = "refus_contrePassation";
    private static final String RESERVE1 = "reserve1";
    private static final String RESERVE10 = "reserve10";
    private static final String RESERVE11 = "reserve11";
    private static final String RESERVE12 = "reserve12";
    private static final String RESERVE13 = "reserve13";
    private static final String RESERVE14 = "reserve14";
    private static final String RESERVE15 = "reserve15";
    private static final String RESERVE16 = "reserve16";
    private static final String RESERVE17 = "reserve17";
    private static final String RESERVE18 = "reserve18";
    private static final String RESERVE19 = "reserve19";
    private static final String RESERVE2 = "reserve2";
    private static final String RESERVE20 = "reserve20";
    private static final String RESERVE21 = "reserve21";
    private static final String RESERVE22 = "reserve22";
    private static final String RESERVE23 = "reserve23";
    private static final String RESERVE24 = "reserve24";
    private static final String RESERVE25 = "reserve25";
    private static final String RESERVE26 = "reserve26";
    private static final String RESERVE27 = "reserve27";
    private static final String RESERVE28 = "reserve28";
    private static final String RESERVE3 = "reserve3";
    private static final String RESERVE4 = "reserve4";
    private static final String RESERVE5 = "reserve5";
    private static final String RESERVE6 = "reserve6";
    private static final String RESERVE7 = "reserve7";
    private static final String RESERVE8 = "reserve8";
    private static final String RESERVE9 = "reserve9";
    private static final String RUE_DEBITEUR = "rueDebiteur";
    private static final String RUE_DONNEUR_ORDRE = "rueDonneurOrdre";
    private static final String TOTAL_MONTANT = "totalMontant";
    private static final String TOTAL_TRANSACTIONS = "totalTransactions";

    private FWAsciiFileRecordDescriptor lsv47 = new FWAsciiFileRecordDescriptor();
    private FWAsciiFileRecordDescriptor lsv81 = new FWAsciiFileRecordDescriptor();
    private FWAsciiFileRecordDescriptor lsv84 = new FWAsciiFileRecordDescriptor();
    private FWAsciiFileRecordDescriptor lsv97 = new FWAsciiFileRecordDescriptor();
    private FWAsciiFileRecordDescriptor lsvHeader = new FWAsciiFileRecordDescriptor();

    /**
     * Insérez la description de la méthode ici. Date de création : (16.05.2002 11:48:04)
     */
    private void _createRecordDefinition() {

        // Header
        // e-banking, Description des enregistrement, (p21, 5.1 Secteur de
        // contrôle)
        FWAsciiFileFieldDescriptor fIdFichier = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.ID_FICHIER,
                FWAsciiFileFieldDescriptor.STRING, 3, 0, 1);
        FWAsciiFileFieldDescriptor fDateEcheance = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreLSV.DATE_ECHEANCE, FWAsciiFileFieldDescriptor.DATE_AMJ, 6, 0, 4);
        FWAsciiFileFieldDescriptor fNumeroAdherent = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreLSV.NUMERO_ADHERENT, FWAsciiFileFieldDescriptor.INTEGER, 6, 0, 10);
        FWAsciiFileFieldDescriptor fReserve1 = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE1,
                "100000000", 16);
        FWAsciiFileFieldDescriptor fReserve2 = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE2,
                "000000000", 25);
        FWAsciiFileFieldDescriptor fNumeroOG = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.NUMERO_OG,
                FWAsciiFileFieldDescriptor.INTEGER, 2, 0, 34);
        FWAsciiFileFieldDescriptor fGenreTransaction = new FWAsciiFileFieldDescriptor(
                CAOrdreFormateur.GENRE_TRANSACTION, FWAsciiFileFieldDescriptor.STRING, 2, 0, 36);
        FWAsciiFileFieldDescriptor fNumeroTransaction = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreLSV.NUMERO_TRANSACTION, FWAsciiFileFieldDescriptor.INTEGER, 6, 0, 38);
        FWAsciiFileFieldDescriptor fReserve3 = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE3,
                "0000000", 44);

        // Paramètres utilisés plusieures fois (47, 81, 84)
        FWAsciiFileFieldDescriptor fCodeISO = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.CODE_ISO,
                FWAsciiFileFieldDescriptor.STRING, 3, 0, 51);
        FWAsciiFileFieldDescriptor fMontant = new FWAsciiFileFieldDescriptor(CAOrdreFormateur.MONTANT,
                FWAsciiFileFieldDescriptor.NUMBER_FIXED_DECIMALS, 13, 2, 54);
        FWAsciiFileFieldDescriptor fReserve4 = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE4,
                FWAsciiFileFieldDescriptor.STRING, 1, 0, 67);
        FWAsciiFileFieldDescriptor fReserve5 = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE5,
                FWAsciiFileFieldDescriptor.STRING, 3, 0, 68);
        FWAsciiFileFieldDescriptor fReserve6 = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE6,
                FWAsciiFileFieldDescriptor.STRING, 2, 0, 71);
        FWAsciiFileFieldDescriptor fccpDebiteur = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.CCP_DEBITEUR,
                FWAsciiFileFieldDescriptor.INTEGER, 9, 0, 73);
        FWAsciiFileFieldDescriptor fReserve7 = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE7,
                FWAsciiFileFieldDescriptor.STRING, 6, 0, 82);
        FWAsciiFileFieldDescriptor fNoReference = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.NO_REFERENCE,
                FWAsciiFileFieldDescriptor.STRING, 27, 0, 88);

        FWAsciiFileFieldDescriptor fCommunication1 = new FWAsciiFileFieldDescriptor(
                CAOrdreFormatterUtils.COMMUNICATION1, FWAsciiFileFieldDescriptor.STRING,
                CAOrdreFormatterUtils.LSV_COMMUNICATION_MAX_LENGTH, 0, 403);
        FWAsciiFileFieldDescriptor fCommunication2 = new FWAsciiFileFieldDescriptor(
                CAOrdreFormatterUtils.COMMUNICATION2, FWAsciiFileFieldDescriptor.STRING,
                CAOrdreFormatterUtils.LSV_COMMUNICATION_MAX_LENGTH, 0, 438);
        FWAsciiFileFieldDescriptor fCommunication3 = new FWAsciiFileFieldDescriptor(
                CAOrdreFormatterUtils.COMMUNICATION3, FWAsciiFileFieldDescriptor.STRING,
                CAOrdreFormatterUtils.LSV_COMMUNICATION_MAX_LENGTH, 0, 473);
        FWAsciiFileFieldDescriptor fCommunication4 = new FWAsciiFileFieldDescriptor(
                CAOrdreFormatterUtils.COMMUNICATION4, FWAsciiFileFieldDescriptor.STRING,
                CAOrdreFormatterUtils.LSV_COMMUNICATION_MAX_LENGTH, 0, 508);

        FWAsciiFileFieldDescriptor fNomDonneurOrdre = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreLSV.NOM_DONNEUR_ORDRE, FWAsciiFileFieldDescriptor.STRING, 35, 0, 547);
        FWAsciiFileFieldDescriptor fDesignationSuppDonneurOrdre = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreLSV.DESIGNATION_SUPP_DONNER_ORDRE, FWAsciiFileFieldDescriptor.STRING, 35, 0, 582);
        FWAsciiFileFieldDescriptor fRueDonneurOrdre = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreLSV.RUE_DONNEUR_ORDRE, FWAsciiFileFieldDescriptor.STRING, 35, 0, 617);
        FWAsciiFileFieldDescriptor fNPADonneurOrdre = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreLSV.NPADONNEUR_ORDRE, FWAsciiFileFieldDescriptor.STRING, 10, 0, 652);
        FWAsciiFileFieldDescriptor fLieuDonneurOrdre = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreLSV.LIEU_DONNEUR_ORDRE, FWAsciiFileFieldDescriptor.STRING, 25, 0, 662);

        // Paramètres utilisés plusieures fois (81, 84)
        FWAsciiFileFieldDescriptor fdateValeur = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.DATE_VALEUR,
                FWAsciiFileFieldDescriptor.NUMBER_FIXED_DECIMALS, 8, 0, 115);
        FWAsciiFileFieldDescriptor fdateEcriture = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreLSV.DATE_ECRITURE, FWAsciiFileFieldDescriptor.NUMBER_FIXED_DECIMALS, 8, 0, 123);
        FWAsciiFileFieldDescriptor fNomDebiteur = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.NOM_DEBITEUR,
                FWAsciiFileFieldDescriptor.STRING, 35, 0, 131);
        FWAsciiFileFieldDescriptor fDesignationSuppDebiteur = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreLSV.DESIGNATION_SUPP_DEBITEUR, FWAsciiFileFieldDescriptor.STRING, 35, 0, 166);
        FWAsciiFileFieldDescriptor fRueDebiteur = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RUE_DEBITEUR,
                FWAsciiFileFieldDescriptor.STRING, 35, 0, 201);
        FWAsciiFileFieldDescriptor fNPADebiteur = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.NPADEBITEUR,
                FWAsciiFileFieldDescriptor.STRING, 10, 0, 236);
        FWAsciiFileFieldDescriptor fLieuDebiteur = new FWAsciiFileFieldDescriptor(
                CAProcessFormatOrdreLSV.LIEU_DEBITEUR, FWAsciiFileFieldDescriptor.STRING, 25, 0, 246);
        FWAsciiFileFieldDescriptor fReserve8 = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE8,
                FWAsciiFileFieldDescriptor.STRING, 35, 0, 271);
        FWAsciiFileFieldDescriptor fReserve9 = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE9,
                FWAsciiFileFieldDescriptor.STRING, 35, 0, 306);
        FWAsciiFileFieldDescriptor fReserve10 = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE10,
                FWAsciiFileFieldDescriptor.STRING, 27, 0, 341);
        FWAsciiFileFieldDescriptor fRefRemise = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.REF_REMISE,
                FWAsciiFileFieldDescriptor.STRING, 35, 0, 368);

        FWAsciiFileFieldDescriptor fReserve11 = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE11,
                FWAsciiFileFieldDescriptor.STRING, 2, 0, 545);

        FWAsciiFileFieldDescriptor fCodeISOPrix = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.CODE_ISOPRIX,
                FWAsciiFileFieldDescriptor.STRING, 3, 0, 687);
        FWAsciiFileFieldDescriptor fPrix = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.PRIX,
                FWAsciiFileFieldDescriptor.NUMBER_FIXED_DECIMALS, 4, 2, 690);
        FWAsciiFileFieldDescriptor fReserve13 = new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE13,
                FWAsciiFileFieldDescriptor.STRING, 5, 0, 696);

        try {
            // Définition du header
            lsvHeader.setLength(700);
            lsvHeader.addFieldDescriptor(fIdFichier);
            lsvHeader.addFieldDescriptor(fDateEcheance);
            lsvHeader.addFieldDescriptor(fNumeroAdherent);
            lsvHeader.addFieldDescriptor(fReserve1);
            lsvHeader.addFieldDescriptor(fReserve2);
            lsvHeader.addFieldDescriptor(fNumeroOG);
            lsvHeader.addFieldDescriptor(fGenreTransaction);
            lsvHeader.addFieldDescriptor(fNumeroTransaction);
            lsvHeader.addFieldDescriptor(fReserve3);

            // 5.7 Enregistrement pour refus/contre-passation (Description des
            // enregistrements p25, Genre de transaction 84)
            lsv84.setLength(700);
            lsv84.addFieldDescriptor(fIdFichier);
            lsv84.addFieldDescriptor(fDateEcheance);
            lsv84.addFieldDescriptor(fNumeroAdherent);
            lsv84.addFieldDescriptor(fReserve1);
            lsv84.addFieldDescriptor(fReserve2);
            lsv84.addFieldDescriptor(fNumeroOG);
            lsv84.addFieldDescriptor(fGenreTransaction);
            lsv84.addFieldDescriptor(fNumeroTransaction);
            lsv84.addFieldDescriptor(fReserve3);
            lsv84.addFieldDescriptor(fCodeISO);
            lsv84.addFieldDescriptor(fMontant);
            lsv84.addFieldDescriptor(fReserve4);
            lsv84.addFieldDescriptor(fReserve5);
            lsv84.addFieldDescriptor(fReserve6);
            lsv84.addFieldDescriptor(fccpDebiteur);
            lsv84.addFieldDescriptor(fReserve7);
            lsv84.addFieldDescriptor(fNoReference);
            lsv84.addFieldDescriptor(fdateValeur);
            lsv84.addFieldDescriptor(fdateEcriture);
            lsv84.addFieldDescriptor(fNomDebiteur);
            lsv84.addFieldDescriptor(fDesignationSuppDebiteur);
            lsv84.addFieldDescriptor(fRueDebiteur);
            lsv84.addFieldDescriptor(fNPADebiteur);
            lsv84.addFieldDescriptor(fLieuDebiteur);
            lsv84.addFieldDescriptor(fReserve8);
            lsv84.addFieldDescriptor(fReserve9);
            lsv84.addFieldDescriptor(fReserve10);
            lsv84.addFieldDescriptor(fRefRemise);
            lsv84.addFieldDescriptor(fCommunication1);
            lsv84.addFieldDescriptor(fCommunication2);
            lsv84.addFieldDescriptor(fCommunication3);
            lsv84.addFieldDescriptor(fCommunication4);
            lsv84.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.REFUS_CONTRE_PASSATION,
                    FWAsciiFileFieldDescriptor.NUMBER_FIXED_DECIMALS, 2, 0, 543));
            lsv84.addFieldDescriptor(fReserve11);
            lsv84.addFieldDescriptor(fNomDonneurOrdre);
            lsv84.addFieldDescriptor(fDesignationSuppDonneurOrdre);
            lsv84.addFieldDescriptor(fRueDonneurOrdre);
            lsv84.addFieldDescriptor(fNPADonneurOrdre);
            lsv84.addFieldDescriptor(fLieuDonneurOrdre);
            lsv84.addFieldDescriptor(fCodeISOPrix);
            lsv84.addFieldDescriptor(fPrix);
            lsv84.addFieldDescriptor(fReserve13);

            // 5.6 Enregistrement pour ecriture au crédit (Description des
            // enregistrements p24, Genre de transaction 81)
            lsv81.setLength(700);
            lsv81.addFieldDescriptor(fIdFichier);
            lsv81.addFieldDescriptor(fDateEcheance);
            lsv81.addFieldDescriptor(fNumeroAdherent);
            lsv81.addFieldDescriptor(fReserve1);
            lsv81.addFieldDescriptor(fReserve2);
            lsv81.addFieldDescriptor(fNumeroOG);
            lsv81.addFieldDescriptor(fGenreTransaction);
            lsv81.addFieldDescriptor(fNumeroTransaction);
            lsv81.addFieldDescriptor(fReserve3);
            lsv81.addFieldDescriptor(fCodeISO);
            lsv81.addFieldDescriptor(fMontant);
            lsv81.addFieldDescriptor(fReserve4);
            lsv81.addFieldDescriptor(fReserve5);
            lsv81.addFieldDescriptor(fReserve6);
            lsv81.addFieldDescriptor(fccpDebiteur);
            lsv81.addFieldDescriptor(fReserve7);
            lsv81.addFieldDescriptor(fNoReference);
            lsv81.addFieldDescriptor(fdateValeur);
            lsv81.addFieldDescriptor(fdateEcriture);
            lsv81.addFieldDescriptor(fNomDebiteur);
            lsv81.addFieldDescriptor(fDesignationSuppDebiteur);
            lsv81.addFieldDescriptor(fRueDebiteur);
            lsv81.addFieldDescriptor(fNPADebiteur);
            lsv81.addFieldDescriptor(fLieuDebiteur);
            lsv81.addFieldDescriptor(fReserve8);
            lsv81.addFieldDescriptor(fReserve9);
            lsv81.addFieldDescriptor(fReserve10);
            lsv81.addFieldDescriptor(fRefRemise);
            lsv81.addFieldDescriptor(fCommunication1);
            lsv81.addFieldDescriptor(fCommunication2);
            lsv81.addFieldDescriptor(fCommunication3);
            lsv81.addFieldDescriptor(fCommunication4);
            lsv81.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE12, "00", 543));
            lsv81.addFieldDescriptor(fReserve11);
            lsv81.addFieldDescriptor(fNomDonneurOrdre);
            lsv81.addFieldDescriptor(fDesignationSuppDonneurOrdre);
            lsv81.addFieldDescriptor(fRueDonneurOrdre);
            lsv81.addFieldDescriptor(fNPADonneurOrdre);
            lsv81.addFieldDescriptor(fLieuDonneurOrdre);
            lsv81.addFieldDescriptor(fCodeISOPrix);
            lsv81.addFieldDescriptor(fPrix);
            lsv81.addFieldDescriptor(fReserve13);

            // 5.6 Enregistrement pour un prélèvement sur un compte jaune
            // (Description des enregistrements p22, Genre de transaction 47)
            lsv47.setLength(700);
            lsv47.addFieldDescriptor(fIdFichier);
            lsv47.addFieldDescriptor(fDateEcheance);
            lsv47.addFieldDescriptor(fNumeroAdherent);
            lsv47.addFieldDescriptor(fReserve1);
            lsv47.addFieldDescriptor(fReserve2);
            lsv47.addFieldDescriptor(fNumeroOG);
            lsv47.addFieldDescriptor(fGenreTransaction);
            lsv47.addFieldDescriptor(fNumeroTransaction);
            lsv47.addFieldDescriptor(fReserve3);
            lsv47.addFieldDescriptor(fCodeISO);
            lsv47.addFieldDescriptor(fMontant);
            lsv47.addFieldDescriptor(fReserve4);
            lsv47.addFieldDescriptor(fReserve5);
            lsv47.addFieldDescriptor(fReserve6);
            lsv47.addFieldDescriptor(fccpDebiteur);
            lsv47.addFieldDescriptor(fReserve7);
            lsv47.addFieldDescriptor(fNoReference);
            lsv47.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE8,
                    FWAsciiFileFieldDescriptor.STRING, 8, 0, 115));
            lsv47.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.NOM_DEBITEUR,
                    FWAsciiFileFieldDescriptor.STRING, 35, 0, 123));
            lsv47.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.DESIGNATION_SUPP_DEBITEUR,
                    FWAsciiFileFieldDescriptor.STRING, 35, 0, 158));
            lsv47.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RUE_DEBITEUR,
                    FWAsciiFileFieldDescriptor.STRING, 35, 0, 193));
            lsv47.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.NPADEBITEUR,
                    FWAsciiFileFieldDescriptor.STRING, 10, 0, 228));
            lsv47.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.LIEU_DEBITEUR,
                    FWAsciiFileFieldDescriptor.STRING, 25, 0, 238));
            lsv47.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE9,
                    FWAsciiFileFieldDescriptor.STRING, 35, 0, 263));
            lsv47.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE10,
                    FWAsciiFileFieldDescriptor.STRING, 35, 0, 298));
            lsv47.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE11,
                    FWAsciiFileFieldDescriptor.STRING, 35, 0, 333));
            lsv47.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE12,
                    FWAsciiFileFieldDescriptor.STRING, 10, 0, 368));
            lsv47.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE13,
                    FWAsciiFileFieldDescriptor.STRING, 25, 0, 378));
            lsv47.addFieldDescriptor(fCommunication1);
            lsv47.addFieldDescriptor(fCommunication2);
            lsv47.addFieldDescriptor(fCommunication3);
            lsv47.addFieldDescriptor(fCommunication4);
            lsv47.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE14,
                    FWAsciiFileFieldDescriptor.STRING, 3, 0, 543));
            lsv47.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE15,
                    FWAsciiFileFieldDescriptor.STRING, 1, 0, 546));
            lsv47.addFieldDescriptor(fNomDonneurOrdre);
            lsv47.addFieldDescriptor(fDesignationSuppDonneurOrdre);
            lsv47.addFieldDescriptor(fRueDonneurOrdre);
            lsv47.addFieldDescriptor(fNPADonneurOrdre);
            lsv47.addFieldDescriptor(fLieuDonneurOrdre);
            lsv47.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE16,
                    FWAsciiFileFieldDescriptor.STRING, 14, 0, 687));

            // 4.11 Enregistrement total (Description des enregistrements p20,
            // Genre de transaction 97)
            lsv97.setLength(700);
            lsv97.addFieldDescriptor(fIdFichier);
            lsv97.addFieldDescriptor(fDateEcheance);
            lsv97.addFieldDescriptor(fNumeroAdherent);
            lsv97.addFieldDescriptor(fReserve1);
            lsv97.addFieldDescriptor(fReserve2);
            lsv97.addFieldDescriptor(fNumeroOG);
            lsv97.addFieldDescriptor(fGenreTransaction);
            lsv97.addFieldDescriptor(fNumeroTransaction);
            lsv97.addFieldDescriptor(fReserve3);
            lsv97.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.CODE_MONNAIE,
                    FWAsciiFileFieldDescriptor.STRING, 3, 0, 51));
            lsv97.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.TOTAL_TRANSACTIONS,
                    FWAsciiFileFieldDescriptor.INTEGER, 6, 0, 54));
            lsv97.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.TOTAL_MONTANT,
                    FWAsciiFileFieldDescriptor.NUMBER_FIXED_DECIMALS, 13, 2, 60));
            lsv97.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE14,
                    FWAsciiFileFieldDescriptor.INTEGER, 22, 0, 73));
            lsv97.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE15,
                    FWAsciiFileFieldDescriptor.INTEGER, 22, 0, 95));
            lsv97.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE16,
                    FWAsciiFileFieldDescriptor.INTEGER, 22, 0, 117));
            lsv97.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE17,
                    FWAsciiFileFieldDescriptor.INTEGER, 22, 0, 139));
            lsv97.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE18,
                    FWAsciiFileFieldDescriptor.INTEGER, 22, 0, 161));
            lsv97.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE19,
                    FWAsciiFileFieldDescriptor.INTEGER, 22, 0, 183));
            lsv97.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE20,
                    FWAsciiFileFieldDescriptor.INTEGER, 22, 0, 205));
            lsv97.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE21,
                    FWAsciiFileFieldDescriptor.INTEGER, 22, 0, 227));
            lsv97.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE22,
                    FWAsciiFileFieldDescriptor.INTEGER, 22, 0, 249));
            lsv97.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE23,
                    FWAsciiFileFieldDescriptor.INTEGER, 22, 0, 271));
            lsv97.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE24,
                    FWAsciiFileFieldDescriptor.INTEGER, 22, 0, 293));
            lsv97.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE25,
                    FWAsciiFileFieldDescriptor.INTEGER, 22, 0, 315));
            lsv97.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE26,
                    FWAsciiFileFieldDescriptor.INTEGER, 22, 0, 337));
            lsv97.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE27,
                    FWAsciiFileFieldDescriptor.INTEGER, 22, 0, 359));
            lsv97.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSV.RESERVE28,
                    FWAsciiFileFieldDescriptor.STRING, 320, 0, 381));

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.04.2002 13:11:09)
     * 
     * @param ordreVersement
     *            globaz.osiris.db.comptes.CAOperationOrdreRecouvrement
     * @param buffer
     *            java.lang.StringBuffer
     */
    public StringBuffer _formatCCP(CAOperationOrdreRecouvrement or) throws Exception {

        // Récupérer l'adresse de paiement
        CAAdressePaiementFormatter adp = new CAAdressePaiementFormatter();
        adp.setAdressePaiement(or.getAdressePaiement());
        adp.checkAdressePaiement(getSession());

        // Genre de transaction
        lsv47.setFieldValue(CAOrdreFormateur.GENRE_TRANSACTION, "47");

        // Numéro transaction
        lsv47.setFieldValue(CAProcessFormatOrdreLSV.NUMERO_TRANSACTION, or.getNumTransaction());

        lsv47.setFieldValue(CAProcessFormatOrdreLSV.CODE_ISO, or.getCodeISOMonnaieDepot());

        // Montant
        lsv47.setFieldValue(CAOrdreFormateur.MONTANT, or.getMontant());

        // CCP débiteur
        lsv47.setFieldValue(CAProcessFormatOrdreLSV.CCP_DEBITEUR, JACCP.formatNoDash(adp.getNumCompte()));

        // Référence
        lsv47.setFieldValue(CAProcessFormatOrdreLSV.NO_REFERENCE, or.getReferenceBVR());

        // Debiteur : nom

        String beneficiaire = adp.getNomTiersBeneficiaire();
        // Si bénéficiaire > 35 on le coupe
        if (beneficiaire.length() >= 35) {
            beneficiaire = beneficiaire.substring(0, 35);
        }
        lsv47.setFieldValue(CAProcessFormatOrdreLSV.NOM_DEBITEUR, beneficiaire);

        // Debiteur : désignation supplémentaire
        String[] adrComp = adp.getAdresseCourrierBeneficiaire().getAdresse();
        if ((adrComp == null) || JadeStringUtil.isBlank(adrComp[0])) {
            lsv47.clearFieldValue(CAProcessFormatOrdreLSV.DESIGNATION_SUPP_DEBITEUR);
        } else {
            if (adrComp[0].length() >= 35) {
                adrComp[0] = adrComp[0].substring(0, 35);
            }
            lsv47.setFieldValue(CAProcessFormatOrdreLSV.DESIGNATION_SUPP_DEBITEUR, adrComp[0]);
        }

        // Debiteur : rue
        lsv47.setFieldValue(CAProcessFormatOrdreLSV.RUE_DEBITEUR, adp.getAdresseCourrierBeneficiaire().getRue());

        // Debiteur : npa
        lsv47.setFieldValue(CAProcessFormatOrdreLSV.NPADEBITEUR, adp.getAdresseCourrierBeneficiaire().getNumPostal());

        // Debiteur : lieu
        lsv47.setFieldValue(CAProcessFormatOrdreLSV.LIEU_DEBITEUR, adp.getAdresseCourrierBeneficiaire().getLocalite());

        // Communications
        CAOrdreFormatterUtils.initLSVRecordCommunications(or, lsv47);

        return new StringBuffer(lsv47.createRecord(getSession()));
    }

    /**
     * Commentaire relatif à la méthode format.
     */
    @Override
    public StringBuffer format(APICommonOdreVersement ov) throws Exception {

        return null;
    }

    /**
     * Commentaire relatif à la méthode format.
     */
    @Override
    public StringBuffer format(globaz.osiris.db.comptes.CAOperationOrdreRecouvrement or) throws Exception {

        StringBuffer sb = new StringBuffer();

        // Vérifications
        if (or == null) {
            return null;
        }

        if (ordreGroupe == null) {
            return null;
        }

        // Sous contrôle d'exceptions
        try {

            // Formattage de l'adresse de paiement
            CAAdressePaiementFormatter adp = new CAAdressePaiementFormatter();
            adp.setAdressePaiement(or.getAdressePaiement());

            // Sélection fonction du genre d'adresse de paiement
            if (adp.getTypeAdresse().equals(IntAdressePaiement.CCP)) {
                sb = _formatCCP(or);
            } else {
                getMemoryLog().logMessage("5206", adp.getTypeAdresse(), FWMessage.ERREUR, this.getClass().getName());
                return null;
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
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }

        return sb;

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
            lsv97.setFieldValue(CAOrdreFormateur.GENRE_TRANSACTION, "97");

            // nombre de transactions
            int n = Integer.parseInt(og.getNbTransactions());
            n++;
            lsv97.setFieldValue(CAProcessFormatOrdreLSV.NUMERO_TRANSACTION, String.valueOf(n));

            // Total de la monnaie CHF
            lsv97.setFieldValue(CAProcessFormatOrdreLSV.CODE_MONNAIE, "CHF");
            lsv97.setFieldValue(CAProcessFormatOrdreLSV.TOTAL_TRANSACTIONS, og.getNbTransactions());
            lsv97.setFieldValue(CAProcessFormatOrdreLSV.TOTAL_MONTANT, og.getTotal());
            lsv97.setFieldValue(CAProcessFormatOrdreLSV.RESERVE14, "0");
            lsv97.setFieldValue(CAProcessFormatOrdreLSV.RESERVE15, "0");
            lsv97.setFieldValue(CAProcessFormatOrdreLSV.RESERVE16, "0");
            lsv97.setFieldValue(CAProcessFormatOrdreLSV.RESERVE17, "0");
            lsv97.setFieldValue(CAProcessFormatOrdreLSV.RESERVE18, "0");
            lsv97.setFieldValue(CAProcessFormatOrdreLSV.RESERVE19, "0");
            lsv97.setFieldValue(CAProcessFormatOrdreLSV.RESERVE20, "0");
            lsv97.setFieldValue(CAProcessFormatOrdreLSV.RESERVE21, "0");
            lsv97.setFieldValue(CAProcessFormatOrdreLSV.RESERVE22, "0");
            lsv97.setFieldValue(CAProcessFormatOrdreLSV.RESERVE23, "0");
            lsv97.setFieldValue(CAProcessFormatOrdreLSV.RESERVE24, "0");
            lsv97.setFieldValue(CAProcessFormatOrdreLSV.RESERVE25, "0");
            lsv97.setFieldValue(CAProcessFormatOrdreLSV.RESERVE26, "0");
            lsv97.setFieldValue(CAProcessFormatOrdreLSV.RESERVE27, "0");

            // Créer l'enregistrement
            StringBuffer sb = new StringBuffer(lsv97.createRecord(getSession()));

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
            _createRecordDefinition();

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

            // Récupérer l'adresse de débit des taxes
            if (og.getOrganeExecution().getAdresseDebitTaxes() != null) {
                CAAdressePaiementFormatter adpTaxes = new CAAdressePaiementFormatter();
                adpTaxes.setAdressePaiement(og.getOrganeExecution().getAdresseDebitTaxes());

                // Vérifier l'adresse de débit des taxes
                if (!adpTaxes.getTypeAdresse().equals(IntAdressePaiement.CCP)) {
                    getMemoryLog().logMessage("5228", null, FWMessage.FATAL, this.getClass().getName());
                    return null;
                }
            }

            // Construire l'header
            lsvHeader.clear();
            lsvHeader.setFieldValue(CAProcessFormatOrdreLSV.ID_FICHIER, "036");

            // Insérer la date d'échéance
            lsvHeader.setFieldValue(CAProcessFormatOrdreLSV.DATE_ECHEANCE, og.getDateEcheance());

            // Numéro d'adhérent
            lsvHeader.setFieldValue(CAProcessFormatOrdreLSV.NUMERO_ADHERENT, (og.getOrganeExecution().getNoAdherent()));

            // Numéro d'og
            lsvHeader.setFieldValue(CAProcessFormatOrdreLSV.NUMERO_OG, og.getNumeroOG());

            // Genre de transaction
            lsvHeader.setFieldValue(CAOrdreFormateur.GENRE_TRANSACTION, "00");

            // Numéro de transaction
            lsvHeader.setFieldValue(CAProcessFormatOrdreLSV.NUMERO_TRANSACTION, "0");

            // Créer l'enregistrement
            sb.append(lsvHeader.createRecord(getSession()));

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
}
