package globaz.osiris.db.ordres.format;

import globaz.framework.filetransfer.FWAsciiFileFieldDescriptor;
import globaz.framework.filetransfer.FWAsciiFileRecordDescriptor;
import globaz.framework.util.FWMessage;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.ordre.APICommonOdreVersement;
import globaz.osiris.api.ordre.APIOrdreGroupe;
import globaz.osiris.db.comptes.CAOperationOrdreRecouvrement;
import globaz.osiris.db.ordres.format.utils.CAOrdreFormatterUtils;
import globaz.osiris.db.ordres.sepa.utils.CASepaCommonUtils;
import globaz.osiris.db.utils.CAAdressePaiementFormatter;
import globaz.osiris.external.IntAdressePaiement;
import ch.globaz.osiris.business.constantes.CAProperties;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (08.04.2002 09:05:57)
 * 
 * @author: Administrator
 */
public final class CAProcessFormatOrdreLSVBanque extends CAOrdreFormateur {
    private static final String BENEFICIAIRE1 = "beneficiaire1";
    private static final String BENEFICIAIRE2 = "beneficiaire2";
    private static final String BENEFICIAIRE3 = "beneficiaire3";
    private static final String BENEFICIAIRE4 = "beneficiaire4";
    private static final String CBBENEFICIAIRE = "CBBeneficiaire";
    private static final String CBDEBITEUR = "CBDebiteur";
    private static final String DATE_CREATION = "dateCreation";
    private static final String DATE_TRAITEMENT = "dateTraitement";
    private static final String DEBITEUR_ADRESSE1 = "debiteurAdresse1";
    private static final String DEBITEUR_ADRESSE2 = "debiteurAdresse2";
    private static final String DEBITEUR_ADRESSE3 = "debiteurAdresse3";
    private static final String DEBITEUR_ADRESSE4 = "debiteurAdresse4";
    private static final String DEBITEUR_NO_COMPTE = "debiteurNoCompte";
    private static final String FLAG_REFERENCE = "flagReference";
    private static final String ID_EXPEDITEUR = "idExpediteur";
    private static final String IDENTIFICATION_LSV = "identificationLSV";
    private static final String MONTANT_MONNAIE = "montantMonnaie";
    private static final String MONTANT_TOTAL = "montantTotal";
    private static final String NO_SEQUENCE_ENTREE = "noSequenceEntree";
    private static final String NUM_PARTICIPANT_BVR = "numParticipantBVR";
    private static final String NUMERO_COMPTE_BENEFICIAIRE = "numeroCompteBeneficiaire";
    private static final String NUMERO_VERSION = "numeroVersion";
    private static final String REFERENCE_LSV = "referenceLSV";
    private static final String TYPE_TRAITEMENT = "typeTraitement";

    private FWAsciiFileRecordDescriptor record1 = new FWAsciiFileRecordDescriptor();
    private FWAsciiFileRecordDescriptor record2 = new FWAsciiFileRecordDescriptor();
    private FWAsciiFileRecordDescriptor record3 = new FWAsciiFileRecordDescriptor();
    private FWAsciiFileRecordDescriptor record4 = new FWAsciiFileRecordDescriptor();
    private FWAsciiFileRecordDescriptor record5 = new FWAsciiFileRecordDescriptor();

    private FWAsciiFileRecordDescriptor recordEOF = new FWAsciiFileRecordDescriptor();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.05.2002 11:48:04)
     */
    private void _createRecordDefinition() {
        try {
            // D�finition record 1
            record1.setLength(97);
            record1.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAOrdreFormateur.GENRE_TRANSACTION,
                    FWAsciiFileFieldDescriptor.INTEGER, 3, 0, 1));
            record1.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSVBanque.NUMERO_VERSION,
                    FWAsciiFileFieldDescriptor.INTEGER, 1, 0, 4));
            record1.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSVBanque.TYPE_TRAITEMENT,
                    FWAsciiFileFieldDescriptor.STRING, 1, 0, 5));
            record1.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSVBanque.DATE_TRAITEMENT,
                    FWAsciiFileFieldDescriptor.DATE_AMJ, 8, 0, 6));
            record1.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSVBanque.CBDEBITEUR,
                    FWAsciiFileFieldDescriptor.STRING, 5, 0, 14));
            record1.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSVBanque.DATE_CREATION,
                    FWAsciiFileFieldDescriptor.DATE_AMJ, 8, 0, 19));
            record1.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSVBanque.CBBENEFICIAIRE,
                    FWAsciiFileFieldDescriptor.STRING, 5, 0, 27));
            record1.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSVBanque.ID_EXPEDITEUR,
                    FWAsciiFileFieldDescriptor.STRING, 5, 0, 32));
            record1.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSVBanque.NO_SEQUENCE_ENTREE,
                    FWAsciiFileFieldDescriptor.INTEGER, 7, 0, 37));
            record1.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSVBanque.IDENTIFICATION_LSV,
                    FWAsciiFileFieldDescriptor.STRING, 5, 0, 44));
            record1.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSVBanque.MONTANT_MONNAIE,
                    FWAsciiFileFieldDescriptor.STRING, 3, 0, 49));

            FWAsciiFileFieldDescriptor montantDescriptor = new FWAsciiFileFieldDescriptor(CAOrdreFormateur.MONTANT,
                    FWAsciiFileFieldDescriptor.NUMBER_VAR_DECIMALS, 12, 0, 52);
            montantDescriptor.setDecimalCharacter(',');
            record1.addFieldDescriptor(montantDescriptor);
            record1.addFieldDescriptor(new FWAsciiFileFieldDescriptor(
                    CAProcessFormatOrdreLSVBanque.NUMERO_COMPTE_BENEFICIAIRE, FWAsciiFileFieldDescriptor.STRING, 34, 0,
                    64));

            // D�finition record 2
            record2.setLength(140);
            record2.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSVBanque.BENEFICIAIRE1,
                    FWAsciiFileFieldDescriptor.STRING, 35, 0, 1));
            record2.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSVBanque.BENEFICIAIRE2,
                    FWAsciiFileFieldDescriptor.STRING, 35, 0, 36));
            record2.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSVBanque.BENEFICIAIRE3,
                    FWAsciiFileFieldDescriptor.STRING, 35, 0, 71));
            record2.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSVBanque.BENEFICIAIRE4,
                    FWAsciiFileFieldDescriptor.STRING, 35, 0, 106));

            // D�finition record 3
            record3.setLength(174);
            record3.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSVBanque.DEBITEUR_NO_COMPTE,
                    FWAsciiFileFieldDescriptor.STRING, 34, 0, 1));
            record3.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSVBanque.DEBITEUR_ADRESSE1,
                    FWAsciiFileFieldDescriptor.STRING, 35, 0, 35));
            record3.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSVBanque.DEBITEUR_ADRESSE2,
                    FWAsciiFileFieldDescriptor.STRING, 35, 0, 70));
            record3.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSVBanque.DEBITEUR_ADRESSE3,
                    FWAsciiFileFieldDescriptor.STRING, 35, 0, 105));
            record3.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSVBanque.DEBITEUR_ADRESSE4,
                    FWAsciiFileFieldDescriptor.STRING, 35, 0, 140));

            // d�finition record 4
            record4.setLength(140);
            record4.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAOrdreFormatterUtils.COMMUNICATION1,
                    FWAsciiFileFieldDescriptor.STRING, CAOrdreFormatterUtils.LSV_COMMUNICATION_MAX_LENGTH, 0, 1));
            record4.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAOrdreFormatterUtils.COMMUNICATION2,
                    FWAsciiFileFieldDescriptor.STRING, CAOrdreFormatterUtils.LSV_COMMUNICATION_MAX_LENGTH, 0, 36));
            record4.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAOrdreFormatterUtils.COMMUNICATION3,
                    FWAsciiFileFieldDescriptor.STRING, CAOrdreFormatterUtils.LSV_COMMUNICATION_MAX_LENGTH, 0, 71));
            record4.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAOrdreFormatterUtils.COMMUNICATION4,
                    FWAsciiFileFieldDescriptor.STRING, CAOrdreFormatterUtils.LSV_COMMUNICATION_MAX_LENGTH, 0, 106));

            // d�finition record 5
            record5.setLength(37);
            record5.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSVBanque.FLAG_REFERENCE,
                    FWAsciiFileFieldDescriptor.STRING, 1, 0, 1));
            record5.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSVBanque.REFERENCE_LSV,
                    FWAsciiFileFieldDescriptor.STRING, 27, 0, 2));
            record5.addFieldDescriptor(new FWAsciiFileFieldDescriptor(
                    CAProcessFormatOrdreLSVBanque.NUM_PARTICIPANT_BVR, FWAsciiFileFieldDescriptor.STRING, 9, 0, 29));

            // d�finition record EOF
            recordEOF.setLength(43);
            recordEOF.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAOrdreFormateur.GENRE_TRANSACTION,
                    FWAsciiFileFieldDescriptor.INTEGER, 3, 0, 1));
            recordEOF.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSVBanque.NUMERO_VERSION,
                    FWAsciiFileFieldDescriptor.INTEGER, 1, 0, 4));
            recordEOF.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSVBanque.DATE_CREATION,
                    FWAsciiFileFieldDescriptor.DATE_AMJ, 8, 0, 5));
            recordEOF.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSVBanque.ID_EXPEDITEUR,
                    FWAsciiFileFieldDescriptor.STRING, 5, 0, 13));
            recordEOF.addFieldDescriptor(new FWAsciiFileFieldDescriptor(
                    CAProcessFormatOrdreLSVBanque.NO_SEQUENCE_ENTREE, FWAsciiFileFieldDescriptor.INTEGER, 7, 0, 18));
            recordEOF.addFieldDescriptor(new FWAsciiFileFieldDescriptor(CAProcessFormatOrdreLSVBanque.MONTANT_MONNAIE,
                    FWAsciiFileFieldDescriptor.STRING, 3, 0, 25));

            FWAsciiFileFieldDescriptor montantTotalDescriptor = new FWAsciiFileFieldDescriptor(
                    CAProcessFormatOrdreLSVBanque.MONTANT_TOTAL, FWAsciiFileFieldDescriptor.NUMBER_VAR_DECIMALS, 16, 0,
                    28);
            montantTotalDescriptor.setDecimalCharacter(',');
            recordEOF.addFieldDescriptor(montantTotalDescriptor);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }

    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.04.2002 13:11:09)
     * 
     * @param ordreVersement
     *            globaz.osiris.db.comptes.CAOperationOrdreRecouvrement
     * @param buffer
     *            java.lang.StringBuffer
     */
    public StringBuffer _formatBanque(CAOperationOrdreRecouvrement or, APIOrdreGroupe og) throws Exception {
        // R�cup�rer l'adresse de recouvrement
        CAAdressePaiementFormatter adp = new CAAdressePaiementFormatter();
        adp.setAdressePaiement(or.getAdressePaiement());
        adp.checkAdressePaiement(getSession());

        initRecord1(or, og, adp);
        initRecord2(og);
        initRecord3(adp);
        CAOrdreFormatterUtils.initLSVRecordCommunications(or, record4);
        initRecord5(or, og);

        StringBuffer sb = new StringBuffer();
        sb.append(record1.createRecord(getSession()));
        sb.append(record2.createRecord(getSession()));
        sb.append(record3.createRecord(getSession()));
        sb.append(record4.createRecord(getSession()));
        sb.append(record5.createRecord(getSession()));

        // TODO Dal Replace with upcoming FWAsciiFileFieldDescriptor for LSV
        // montant
        sb.replace(60, 61, ",");

        return sb;
    }

    /**
     * Commentaire relatif � la m�thode format.
     */
    @Override
    public StringBuffer format(APICommonOdreVersement ov) throws Exception {

        return null;
    }

    /**
     * Commentaire relatif � la m�thode format.
     */
    @Override
    public StringBuffer format(globaz.osiris.db.comptes.CAOperationOrdreRecouvrement or) throws Exception {

        StringBuffer sb = new StringBuffer();

        // V�rifications
        if (or == null) {
            return null;
        }

        if (ordreGroupe == null) {
            return null;
        }

        // Sous contr�le d'exceptions
        try {

            // Formattage de l'adresse de paiement
            CAAdressePaiementFormatter adp = new CAAdressePaiementFormatter();
            adp.setAdressePaiement(or.getAdressePaiement());

            if (CAProperties.ISO_SEPA_ENABLE_IBAN_POSTAL.getBooleanValue()) {
                String typeAdresse = CASepaCommonUtils.getTypeAdresseWithIBANPostalEnable(adp);
                if (IntAdressePaiement.BANQUE.equals(typeAdresse)) {
                    sb = _formatBanque(or, ordreGroupe);
                } else {
                    getMemoryLog()
                            .logMessage("5206", adp.getTypeAdresse(), FWMessage.ERREUR, this.getClass().getName());
                    return null;
                }
            } else {
                // S�lection fonction du genre d'adresse de paiement
                if (adp.getTypeAdresse().equals(IntAdressePaiement.BANQUE)) {
                    sb = _formatBanque(or, ordreGroupe);
                } else {
                    getMemoryLog()
                            .logMessage("5206", adp.getTypeAdresse(), FWMessage.ERREUR, this.getClass().getName());
                    return null;
                }
            }

            // Ins�rer un retour chariot
            if (getInsertNewLine()) {
                sb.append(CAOrdreFormateur.CRLF);
            }

            // Transmettre le buffer
            if (sb != null) {
                getPrintWriter().print(sb);

                // Echo � la console
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
     * Commentaire relatif � la m�thode formatEOF.
     */
    @Override
    public StringBuffer formatEOF(APIOrdreGroupe og) throws Exception {
        // Sous contr�le d'exceptions
        try {
            // V�rifications
            if (og == null) {
                return null;
            }
            if (ordreGroupe == null) {
                return null;
            }

            // Genre de transaction
            recordEOF.setFieldValue(CAOrdreFormateur.GENRE_TRANSACTION, "890");
            // No version
            recordEOF.setFieldValue(CAProcessFormatOrdreLSVBanque.NUMERO_VERSION, "0");
            // Date de cr�ation
            recordEOF.setFieldValue(CAProcessFormatOrdreLSVBanque.DATE_CREATION, og.getDateCreation());
            // Identification de l'exp�diteur du fichier de donn�es
            recordEOF.setFieldValue(CAProcessFormatOrdreLSVBanque.ID_EXPEDITEUR, og.getOrganeExecution()
                    .getIdentifiantDTA());
            // Num�ro de s�quence d'entr�e
            recordEOF.setFieldValue(CAProcessFormatOrdreLSVBanque.NO_SEQUENCE_ENTREE,
                    "" + (Integer.parseInt(og.getNbTransactions()) + 1));
            // Montant de la note de recouvrement (monnaie)
            recordEOF.setFieldValue(CAProcessFormatOrdreLSVBanque.MONTANT_MONNAIE, "CHF");
            // Montant total
            recordEOF.setFieldValue(CAProcessFormatOrdreLSVBanque.MONTANT_TOTAL, og.getTotal());

            // Cr�er l'enregistrement
            StringBuffer sb = new StringBuffer(recordEOF.createRecord(getSession()));

            // TODO Dal Replace with upcoming FWAsciiFileFieldDescriptor for LSV
            // montant
            sb.replace(40, 41, ",");

            // New line
            if (getInsertNewLine()) {
                sb.append(CAOrdreFormateur.CRLF);
            }

            // Transmettre le record
            getPrintWriter().print(sb);

            // Echo � la console
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
     * Commentaire relatif � la m�thode formatHeader.
     */
    @Override
    public StringBuffer formatHeader(APIOrdreGroupe og) throws Exception {

        // Initialiser
        StringBuffer sb = new StringBuffer();

        // Sous contr�le d'exceptions
        try {

            // D�finition des records
            _createRecordDefinition();

            // Sauvegarder l'ordre group�
            ordreGroupe = og;

            // R�cup�rer l'adresse de paiement
            CAAdressePaiementFormatter adp = new CAAdressePaiementFormatter();
            adp.setAdressePaiement(og.getOrganeExecution().getAdressePaiement());
            adp.checkAdressePaiement(getSession());

            if (CAProperties.ISO_SEPA_ENABLE_IBAN_POSTAL.getBooleanValue()) {
                String typeAdresse = CASepaCommonUtils.getTypeAdresseWithIBANPostalEnable(adp);
                if (!IntAdressePaiement.BANQUE.equals(typeAdresse)) {
                    getMemoryLog().logMessage("5228", null, FWMessage.FATAL, this.getClass().getName());
                    return null;
                }
            } else {
                // V�rifier que l'adresse de paiement est une adresse bancaire
                if (!adp.getTypeAdresse().equals(IntAdressePaiement.BANQUE)) {
                    getMemoryLog().logMessage("5228", null, FWMessage.FATAL, this.getClass().getName());
                    return null;
                }
            }

            // Construire l'header
            // --> le fichier header est construit � chaque passage du fait des
            // infos
            // qu'il contient

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }

        return sb;
    }

    /**
     * Pr�paration du record 1
     * 
     * @param or
     * @param og
     * @param adp
     * @throws Exception
     */
    private void initRecord1(CAOperationOrdreRecouvrement or, APIOrdreGroupe og, CAAdressePaiementFormatter adp)
            throws Exception {
        // Genre de transaction
        record1.setFieldValue(CAOrdreFormateur.GENRE_TRANSACTION, "875");
        // No version
        record1.setFieldValue(CAProcessFormatOrdreLSVBanque.NUMERO_VERSION, "0");
        // Type de traitement (P : productif, T : test)
        record1.setFieldValue(CAProcessFormatOrdreLSVBanque.TYPE_TRAITEMENT, "P");
        // Ins�rer la date de traitement
        record1.setFieldValue(CAProcessFormatOrdreLSVBanque.DATE_TRAITEMENT, or.getDate());
        // Num�ro de clearing de la banque du d�biteur
        record1.setFieldValue(CAProcessFormatOrdreLSVBanque.CBDEBITEUR, adp.getAdressePaiement().getBanque()
                .getClearing());
        // Date de cr�ation
        record1.setFieldValue(CAProcessFormatOrdreLSVBanque.DATE_CREATION, og.getDateCreation());
        // Num�ro de clearing de la banque du b�n�ficiaire
        record1.setFieldValue(CAProcessFormatOrdreLSVBanque.CBBENEFICIAIRE, og.getOrganeExecution()
                .getAdressePaiement().getBanque().getClearing());
        // Identification de l'exp�diteur du fichier de donn�es
        record1.setFieldValue(CAProcessFormatOrdreLSVBanque.ID_EXPEDITEUR, og.getOrganeExecution().getIdentifiantDTA());
        // Num�ro de s�quence d'entr�e
        record1.setFieldValue(CAProcessFormatOrdreLSVBanque.NO_SEQUENCE_ENTREE, or.getNumTransaction());
        // Identification LSV
        record1.setFieldValue(CAProcessFormatOrdreLSVBanque.IDENTIFICATION_LSV, og.getOrganeExecution()
                .getIdentifiantDTA());
        // Montant de la note de recouvrement (monnaie)
        record1.setFieldValue(CAProcessFormatOrdreLSVBanque.MONTANT_MONNAIE, "CHF");
        // Montant de la note de recouvrement (montant)
        record1.setFieldValue(CAOrdreFormateur.MONTANT, or.getMontant());

        // Num�ro de compte du b�n�ficiaire
        if (og.getOrganeExecution().getAdressePaiement().isCompteIBAN()) {
            record1.setFieldValue(CAProcessFormatOrdreLSVBanque.NUMERO_COMPTE_BENEFICIAIRE, og.getOrganeExecution()
                    .getAdressePaiement().getNumCompte().replaceAll("\\s", ""));
        } else {
            record1.setFieldValue(CAProcessFormatOrdreLSVBanque.NUMERO_COMPTE_BENEFICIAIRE, og.getOrganeExecution()
                    .getAdressePaiement().getNumCompte());
        }
    }

    /**
     * Pr�paration du record 2
     * 
     * @param og
     * @throws Exception
     */
    private void initRecord2(APIOrdreGroupe og) throws Exception {
        record2.setFieldValue(CAProcessFormatOrdreLSVBanque.BENEFICIAIRE1, og.getOrganeExecution().getAdressePaiement()
                .getTiers().getNom());
        record2.setFieldValue(CAProcessFormatOrdreLSVBanque.BENEFICIAIRE2, og.getOrganeExecution().getAdressePaiement()
                .getTiers().getLieu());
        record2.setFieldValue(CAProcessFormatOrdreLSVBanque.BENEFICIAIRE3, "");
        record2.setFieldValue(CAProcessFormatOrdreLSVBanque.BENEFICIAIRE4, "");
    }

    /**
     * Pr�paration du record 3
     * 
     * @param adp
     * @throws Exception
     */
    private void initRecord3(CAAdressePaiementFormatter adp) throws Exception {

        // D�biteur num�ro de compte
        if (adp.getAdressePaiement().isCompteIBAN()) {
            record3.setFieldValue(CAProcessFormatOrdreLSVBanque.DEBITEUR_NO_COMPTE, adp.getAdressePaiement()
                    .getNumCompte().replaceAll("\\s", ""));
        } else {
            record3.setFieldValue(CAProcessFormatOrdreLSVBanque.DEBITEUR_NO_COMPTE, adp.getAdressePaiement()
                    .getNumCompte());
        }

        // D�biteur adresse 1
        if (adp.getNomTiersBeneficiaire().length() > 35) {
            record3.setFieldValue(CAProcessFormatOrdreLSVBanque.DEBITEUR_ADRESSE1, adp.getNomTiersBeneficiaire()
                    .substring(0, 35));
        } else {
            record3.setFieldValue(CAProcessFormatOrdreLSVBanque.DEBITEUR_ADRESSE1, adp.getNomTiersBeneficiaire());
        }

        // D�biteur adresse 2
        if (!JadeStringUtil.isBlank(adp.getAdresseCourrierBeneficiaire().getRue())) {
            if (adp.getAdresseCourrierBeneficiaire().getRue().length() > 35) {
                record3.setFieldValue(CAProcessFormatOrdreLSVBanque.DEBITEUR_ADRESSE2, adp
                        .getAdresseCourrierBeneficiaire().getRue().substring(0, 35));
            } else {
                record3.setFieldValue(CAProcessFormatOrdreLSVBanque.DEBITEUR_ADRESSE2, adp
                        .getAdresseCourrierBeneficiaire().getRue());
            }
            // D�biteur adresse 3
            if (JadeStringUtil.isBlank(adp.getAdresseCourrierBeneficiaire().getNumPostal())
                    && JadeStringUtil.isBlank(adp.getAdresseCourrierBeneficiaire().getLocalite())) {
                record3.clearFieldValue(CAProcessFormatOrdreLSVBanque.DEBITEUR_ADRESSE3);
            } else {
                if ((adp.getAdresseCourrierBeneficiaire().getNumPostal() + " " + adp.getAdresseCourrierBeneficiaire()
                        .getLocalite()).length() > 35) {
                    record3.setFieldValue(CAProcessFormatOrdreLSVBanque.DEBITEUR_ADRESSE3, (adp
                            .getAdresseCourrierBeneficiaire().getNumPostal() + " " + adp
                            .getAdresseCourrierBeneficiaire().getLocalite()).substring(0, 35));
                } else {
                    record3.setFieldValue(CAProcessFormatOrdreLSVBanque.DEBITEUR_ADRESSE3, adp
                            .getAdresseCourrierBeneficiaire().getNumPostal()
                            + " "
                            + adp.getAdresseCourrierBeneficiaire().getLocalite());
                }
            }
        } else {
            // D�biteur adresse 2
            if (JadeStringUtil.isBlank(adp.getAdresseCourrierBeneficiaire().getNumPostal())
                    && JadeStringUtil.isBlank(adp.getAdresseCourrierBeneficiaire().getLocalite())) {
                record3.clearFieldValue(CAProcessFormatOrdreLSVBanque.DEBITEUR_ADRESSE2);
            } else {
                if ((adp.getAdresseCourrierBeneficiaire().getNumPostal() + " " + adp.getAdresseCourrierBeneficiaire()
                        .getLocalite()).length() > 35) {
                    record3.setFieldValue(CAProcessFormatOrdreLSVBanque.DEBITEUR_ADRESSE2, (adp
                            .getAdresseCourrierBeneficiaire().getNumPostal() + " " + adp
                            .getAdresseCourrierBeneficiaire().getLocalite()).substring(0, 35));
                } else {
                    record3.setFieldValue(CAProcessFormatOrdreLSVBanque.DEBITEUR_ADRESSE2, adp
                            .getAdresseCourrierBeneficiaire().getNumPostal()
                            + " "
                            + adp.getAdresseCourrierBeneficiaire().getLocalite());
                }
            }
            record3.clearFieldValue(CAProcessFormatOrdreLSVBanque.DEBITEUR_ADRESSE3);
        }
        // D�biteur adresse 4
        record3.clearFieldValue(CAProcessFormatOrdreLSVBanque.DEBITEUR_ADRESSE4);

    }

    /**
     * Pr�paration du record 5
     * 
     * @throws Exception
     */
    private void initRecord5(CAOperationOrdreRecouvrement or, APIOrdreGroupe og) throws Exception {
        record5.setFieldValue(CAProcessFormatOrdreLSVBanque.FLAG_REFERENCE, "A");

        if (!JadeStringUtil.isBlank(or.getReferenceBVR())
                && !JadeStringUtil.isBlank(og.getOrganeExecution().getNumInterneLsv())) {
            if (or.getReferenceBVR().length() > og.getOrganeExecution().getNumInterneLsv().length()) {
                // TODO : Contr�le et correction !!! CICICAM
                String tmp;
                if (or.getReferenceBVR().startsWith("00" + or.getCompteAnnexe().getIdRole())) {
                    tmp = og.getOrganeExecution().getNumInterneLsv()
                            + or.getReferenceBVR().substring(og.getOrganeExecution().getNumInterneLsv().length());
                } else {
                    String compteAnnexe = JadeStringUtil.rightJustifyInteger(or.getCompteAnnexe().getIdCompteAnnexe(),
                            7);
                    tmp = og.getOrganeExecution().getNumInterneLsv() + "99" + compteAnnexe
                            + or.getReferenceBVR().substring(og.getOrganeExecution().getNumInterneLsv().length() + 9);
                }

                // Mise � jour du modulo de la r�f�rence BVR
                if (tmp.length() == 27) {
                    String partNoChange = tmp.substring(0, 26);
                    tmp = partNoChange + JAUtil.getKeyNumberModulo10(partNoChange);
                }
                record5.setFieldValue(CAProcessFormatOrdreLSVBanque.REFERENCE_LSV, tmp);
            } else {
                record5.setFieldValue(CAProcessFormatOrdreLSVBanque.REFERENCE_LSV, og.getOrganeExecution()
                        .getNumInterneLsv());
            }
        } else {
            record5.setFieldValue(CAProcessFormatOrdreLSVBanque.REFERENCE_LSV, or.getReferenceBVR());
        }

        record5.setFieldValue(CAProcessFormatOrdreLSVBanque.NUM_PARTICIPANT_BVR, og.getOrganeExecution()
                .getNoAdherentBVR());
    }
}
