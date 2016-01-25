package globaz.osiris.db.ordres.format;

import globaz.framework.util.FWMessage;
import globaz.globall.util.JACCP;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.ordre.APICommonOdreVersement;
import globaz.osiris.api.ordre.APIOrdreGroupe;
import globaz.osiris.db.comptes.CAOperationOrdreRecouvrement;
import globaz.osiris.db.ordres.exception.AucuneAdressePaiementException;
import globaz.osiris.db.utils.CAAdressePaiementFormatter;
import globaz.osiris.external.IntAdresseCourrier;
import globaz.osiris.external.IntAdressePaiement;
import globaz.osiris.formatter.CAAdresseCourrierFormatter;
import java.io.PrintWriter;

/**
 * Insérez la description du type ici. Date de création : (08.02.2002 13:40:46)
 * 
 * @author: Administrator
 */
public final class CAProcessFormatOrdreDTA extends CAOrdreFormateur {
    private static final int IBAN_CH_LENGTH = 21;
    // Constantes
    private static final String GENRE_TRANSACTION_PMT_IBAN = "836";
    public final static int CLEARING_BENEFICIAIRE_END = 20;
    public final static int CLEARING_BENEFICIAIRE_START = 8;
    public final static String CRLF = "\n";
    public static final int GENRE_TRANSACTION_END = 51;
    public static final int GENRE_TRANSACTION_START = 48;
    public static final int NO_REFERENCE_END = 69;
    public static final int NO_REFERENCE_START = 58;
    public static final int NUM_TRANSACTION_END = 48;
    public static final int NUM_TRANSACTION_START = 43;
    public final static int VIRGULE = 111;
    private static final int EMPLACEMENT_DECIMALE = 114;

    private CAAdresseCourrierFormatter adresseDonneurOrdre;
    private String header = null;

    /**
     * Commentaire relatif au constructeur CAProcessFormatOrdreDTA.
     */
    public CAProcessFormatOrdreDTA() {
        super();
        setPrintWriter(new PrintWriter(System.out));
    }

    /**
     * Commentaire formatter une transaction isolée
     */
    @Override
    public StringBuffer format(APICommonOdreVersement ov) throws Exception {
        // Vérifications
        if (ov == null) {
            return null;
        }

        if (ov.getAdressePaiement() == null) {
            throw new AucuneAdressePaiementException("Aucune adresse de paiement pour l'OV : N° transaction="
                    + ov.getNumTransaction() + " nom=" + ov.getNomPrenom() + " id=" + ov.getIdOperation());
        }

        if (ordreGroupe == null) {
            return null;
        }

        // Préparer un buffer
        StringBuffer sb = new StringBuffer();

        // Insérer le header
        sb.append(header);

        // Insérer le numéro de transaction
        sb.replace(CAProcessFormatOrdreDTA.NUM_TRANSACTION_START, CAProcessFormatOrdreDTA.NUM_TRANSACTION_END,
                JadeStringUtil.rightJustifyInteger(ov.getNumTransaction(), 5));

        // Formattage de l'adresse de paiement
        CAAdressePaiementFormatter adp = new CAAdressePaiementFormatter();
        adp.setAdressePaiement(ov.getAdressePaiement());

        // Sélection en fonction du type d'adresse de paiement
        if (IntAdressePaiement.BANQUE.equals(adp.getTypeAdresse())) {
            String numCompte = ov.getAdressePaiement().getNumCompte();
            if (isIban(numCompte)) {
                formatIbanCH(ov, sb);
            } else {
                formatBanqueCH(ov, sb);
            }
        } else if (IntAdressePaiement.MANDAT.equals(adp.getTypeAdresse())) {
            // Pas utilisé
            formatMandatCH(ov, sb);
        } else if (IntAdressePaiement.CCP.equals(adp.getTypeAdresse())) {
            formatCCPCH(ov, sb);
        } else if (IntAdressePaiement.BANQUE_INTERNATIONAL.equals(adp.getTypeAdresse())) {
            formatInternational(ov, sb);
        } else {
            getMemoryLog().logMessage("5206", adp.getTypeAdresse(), FWMessage.FATAL, this.getClass().getName());
            return null;
        }

        // Transmettre le buffer
        if (sb != null) {
            getPrintWriter().print(sb);

            // Echo à la console
            if (getEchoToConsole()) {
                System.out.println(sb);
            }
        }

        return sb;
    }

    /**
     * @param numCompte
     * @return
     */
    private boolean isIban(String numCompte) {
        return numCompte.length() == CAProcessFormatOrdreDTA.IBAN_CH_LENGTH && numCompte.startsWith("CH");
    }

    /**
     * Commentaire relatif à la méthode format.
     */
    @Override
    public StringBuffer format(CAOperationOrdreRecouvrement or) throws Exception {
        return null;
    }

    private void formatAdresseBeneficiaire(APICommonOdreVersement ov, StringBuffer sb) throws Exception {

        // Récupérer l'adresse du bénéficiaire
        CAAdressePaiementFormatter fmtAdp = new CAAdressePaiementFormatter(ov.getAdressePaiement());
        IntAdresseCourrier adrCo = fmtAdp.getAdresseCourrierBeneficiaire();
        CAAdresseCourrierFormatter fmtAdr = new CAAdresseCourrierFormatter(fmtAdp.getTiersBeneficiaire(), adrCo);
        fmtAdr.setUseTitle(false);
        fmtAdr.setUseCountry(false);

        // Si l'adresse est null, on sort
        if (adrCo == null) {
            getMemoryLog().logMessage("5210", null, FWMessage.ERREUR, this.getClass().getName());
            return;
        }

        // Récupérer l'adresse du donneur d'ordre sur 4 lignes
        String[] adr = fmtAdr.getAdresseLines(4);

        // L'insérer (max 24 pos par lignes)
        for (int i = 0; i < adr.length; i++) {
            if (JadeStringUtil.isBlank(adr[i])) {
                sb.append(JadeStringUtil.leftJustify(" ", 24));
            } else {
                sb.append(JadeStringUtil.leftJustify(adr[i], 24));
            }
        }

        // Insérer un retour chariot
        if (getInsertNewLine()) {
            sb.append(CAProcessFormatOrdreDTA.CRLF);
        }
    }

    /**
     * Genre de transaction 836
     * Segment 04
     * 
     * @param ov
     * @param fmtAdr
     * @return
     * @throws Exception
     */
    private StringBuffer gt836Segment04AdresseBeneficiaire(APICommonOdreVersement ov, CAAdresseCourrierFormatter fmtAdr)
            throws Exception {
        StringBuffer sb = new StringBuffer("");

        sb.append("04");
        String[] adr = fmtAdr.getAdresseLines(3);

        // L'insérer (max 35 pos par lignes)
        for (int i = 0; i < adr.length; i++) {
            if (JadeStringUtil.isBlank(adr[i])) {
                sb.append(JadeStringUtil.leftJustify(" ", 35));
            } else {
                sb.append(JadeStringUtil.leftJustify(adr[i], 35));
            }
        }

        // Réserve (21)
        sb.append(JadeStringUtil.leftJustify(" ", 21));
        // Insérer un retour chariot
        if (getInsertNewLine()) {
            sb.append(CAProcessFormatOrdreDTA.CRLF);
        }

        return sb;
    }

    private void formatBanqueCH(APICommonOdreVersement ov, StringBuffer sb) throws Exception {
        // Mettre en cache les valeurs
        String compte = ov.getAdressePaiement().getNumCompte().trim();
        String clearing = ov.getAdressePaiement().getBanque().getClearing().trim();

        // Vérifier le numéro de compte
        if (JadeStringUtil.isBlank(compte)) {
            getMemoryLog().logMessage("5211", null, FWMessage.ERREUR, this.getClass().getName());
            return;
        }

        // Vérifier le numéro de clearing
        if (JadeStringUtil.isBlank(clearing)) {
            getMemoryLog().logMessage("5212", null, FWMessage.ERREUR, this.getClass().getName());
            return;
        }

        // genre de transaction
        sb.replace(CAProcessFormatOrdreDTA.GENRE_TRANSACTION_START, CAProcessFormatOrdreDTA.GENRE_TRANSACTION_END,
                "827");

        // Numéro de référence : numéro de transaction (11x)
        sb.replace(CAProcessFormatOrdreDTA.NO_REFERENCE_START, CAProcessFormatOrdreDTA.NO_REFERENCE_END,
                JadeStringUtil.leftJustify(ov.getNumTransaction(), 11));

        // Numéro de clearing du bénéficiaire
        sb.replace(CAProcessFormatOrdreDTA.CLEARING_BENEFICIAIRE_START,
                CAProcessFormatOrdreDTA.CLEARING_BENEFICIAIRE_END, JadeStringUtil.leftJustify(clearing, 12));

        // Bonification 6x + CHF
        sb.append("      CHF");

        // Montant avec virgule (12x)
        sb.append(JadeStringUtil.rightJustifyInteger(ov.getMontant(), 12));
        sb.replace(CAProcessFormatOrdreDTA.VIRGULE, CAProcessFormatOrdreDTA.VIRGULE + 1, ",");

        // Réserve (14)
        sb.append(JadeStringUtil.leftJustify(" ", 14));

        // Insérer un retour chariot
        if (getInsertNewLine()) {
            sb.append(CAProcessFormatOrdreDTA.CRLF);
        }

        // Donneur d'ordre
        formatDonneurOrdre(ov, sb, false);

        // Bénéficiaire
        sb.append("03");
        sb.append(JadeStringUtil.leftJustify("/C/" + compte, 30));
        formatAdresseBeneficiaire(ov, sb);

        // Motif
        if (!JadeStringUtil.isBlank(ov.getMotif())) {
            formatMotif(ov, sb, false);
        }
    }

    private void formatCCPCH(APICommonOdreVersement ov, StringBuffer sb) throws Exception {

        // Mettre en cache les valeurs
        String ccp = null;

        // Vérifier le numéro de ccp
        try {
            ccp = JACCP.parseCCP(ov.getAdressePaiement().getNumCompte().trim());
        } catch (Exception e) {
            getMemoryLog().logMessage("5213", null, FWMessage.ERREUR, this.getClass().getName());
            return;
        }

        // genre de transaction
        sb.replace(CAProcessFormatOrdreDTA.GENRE_TRANSACTION_START, CAProcessFormatOrdreDTA.GENRE_TRANSACTION_END,
                "827");

        // Numéro de référence : numéro de transaction (11x)
        sb.replace(CAProcessFormatOrdreDTA.NO_REFERENCE_START, CAProcessFormatOrdreDTA.NO_REFERENCE_END,
                JadeStringUtil.leftJustify(ov.getNumTransaction(), 11));

        // Bonification 6x + CHF
        sb.append("      CHF");

        // Montant avec virgule (12x)
        sb.append(JadeStringUtil.rightJustifyInteger(ov.getMontant(), 12));
        sb.replace(CAProcessFormatOrdreDTA.VIRGULE, CAProcessFormatOrdreDTA.VIRGULE + 1, ",");

        // Réserve (14)
        sb.append(JadeStringUtil.leftJustify(" ", 14));

        // Insérer un retour chariot
        if (getInsertNewLine()) {
            sb.append(CAProcessFormatOrdreDTA.CRLF);
        }

        // Donneur d'ordre
        formatDonneurOrdre(ov, sb, false);

        // Bénéficiaire
        sb.append("03");
        sb.append(JadeStringUtil.leftJustify("/C/" + ccp, 30));
        formatAdresseBeneficiaire(ov, sb);

        // Motif
        if (!JadeStringUtil.isBlank(ov.getMotif())) {
            formatMotif(ov, sb, false);
        }
    }

    /**
     * format pour genre 836 gérant les IBAN
     * 
     * @param ov
     * @param sb
     * @throws Exception
     */
    private void formatIbanCH(APICommonOdreVersement ov, StringBuffer sb) throws Exception {
        // Mettre en cache les valeurs
        String iban = ov.getAdressePaiement().getNumCompte().trim();
        String clearing = ov.getAdressePaiement().getBanque().getClearing().trim();

        // Vérifier le numéro de compte
        if (JadeStringUtil.isBlank(iban)) {
            getMemoryLog().logMessage("5211", null, FWMessage.ERREUR, this.getClass().getName());
            return;
        }

        // Vérifier le numéro de clearing
        if (JadeStringUtil.isBlank(clearing)) {
            getMemoryLog().logMessage("5212", null, FWMessage.ERREUR, this.getClass().getName());
            return;
        }

        // Segment = 01
        gt836Segment01(ov, clearing, sb);

        // Segment 02 : cours de conversion et donneur d'ordre
        // Donneur d'ordre
        // Si l'adresse est null, on sort
        if (adresseDonneurOrdre == null) {
            getMemoryLog().logMessage("5205", null, FWMessage.FATAL, this.getClass().getName());
            return;
        }
        sb.append(gt836Segment02DonneurOrdre(ov, false));

        // Segment 03 : identification adresse de banque
        sb.append(gt836Segment03Beneficiaire(iban));

        // Récupérer l'adresse du bénéficiaire
        CAAdressePaiementFormatter fmtAdp = new CAAdressePaiementFormatter(ov.getAdressePaiement());
        IntAdresseCourrier adrCo = fmtAdp.getAdresseCourrierBeneficiaire();
        CAAdresseCourrierFormatter fmtAdr = new CAAdresseCourrierFormatter(fmtAdp.getTiersBeneficiaire(), adrCo);
        fmtAdr.setUseTitle(false);
        fmtAdr.setUseCountry(false);

        // Si l'adresse est null, on sort
        if (adrCo == null) {
            getMemoryLog().logMessage("5210", null, FWMessage.ERREUR, this.getClass().getName());
            return;
        }
        // Segment 04 : Nom et adresse du bénéficiaire
        sb.append(gt836Segment04AdresseBeneficiaire(ov, fmtAdr));

        // Segment 05 : motif du paiement
        sb.append(gt836Segment05Motif(ov));
    }

    /**
     * Genre de transaction 836
     * Segment 05
     * 
     * @param ov
     * @param sb
     */
    private StringBuffer gt836Segment05Motif(APICommonOdreVersement ov) {
        StringBuffer sb = new StringBuffer("");
        sb.append("05");
        sb.append("U"); // U Texte libre non structuré

        // Motif 3 * 35
        // HACK : car leftJustify retourne "" si motif est vide. On s'assure alors que motif ne soit pas vide pour avoir les 3*35 positions
        String motif = JadeStringUtil.isEmpty(ov.getMotif()) ? " " : ov.getMotif();
        sb.append(JadeStringUtil.leftJustify(motif, 3 * 35));

        sb.append("0"); // 0 = OUR = Tous les frais à la charge du donneur d'ordre
        // Réserve (19)
        sb.append(JadeStringUtil.leftJustify(" ", 19));

        // Insérer un retour chariot
        if (getInsertNewLine()) {
            sb.append(CAProcessFormatOrdreDTA.CRLF);
        }

        return sb;
    }

    /**
     * Genre de transaction 836
     * Segment 03
     * 
     * @param sb
     * @param iban
     */
    private StringBuffer gt836Segment03Beneficiaire(String iban) {
        StringBuffer sb = new StringBuffer("");
        // Bénéficiaire
        sb.append("03");
        // Nom et adresse de l'établissement bénéficiaire
        // Si CH-IBAN aucune indication relative à l'établissement financier n'est nécessaire. Remplir avec des espaces
        sb.append("A");
        sb.append(JadeStringUtil.leftJustify(" ", 35));
        sb.append(JadeStringUtil.leftJustify(" ", 35));

        // IBAN sur 34
        sb.append(JadeStringUtil.leftJustify(iban, 34));

        // Réserve (21)
        sb.append(JadeStringUtil.leftJustify(" ", 21));

        // Insérer un retour chariot
        if (getInsertNewLine()) {
            sb.append(CAProcessFormatOrdreDTA.CRLF);
        }

        return sb;
    }

    /**
     * Genre de transaction 836
     * Segment 01
     * 
     * @param ov
     * @param sb
     * @param clearing
     */
    private void gt836Segment01(APICommonOdreVersement ov, String clearing, StringBuffer sb) {
        // Modification du Header
        // Compte a débiter
        sb.replace(CAProcessFormatOrdreDTA.CLEARING_BENEFICIAIRE_START,
                CAProcessFormatOrdreDTA.CLEARING_BENEFICIAIRE_END, JadeStringUtil.leftJustify(clearing, 12));
        // genre de transaction
        sb.replace(CAProcessFormatOrdreDTA.GENRE_TRANSACTION_START, CAProcessFormatOrdreDTA.GENRE_TRANSACTION_END,
                CAProcessFormatOrdreDTA.GENRE_TRANSACTION_PMT_IBAN);

        // Genre de transaction 836
        // Numéro de référence : numéro de transaction (11x)
        sb.replace(CAProcessFormatOrdreDTA.NO_REFERENCE_START, CAProcessFormatOrdreDTA.NO_REFERENCE_END,
                JadeStringUtil.leftJustify(ov.getNumTransaction(), 11));

        // Valeur (Bonification 6x )
        // Insérer la date d'exécution
        JADate today = JACalendar.today();
        sb.append(today.toStrAMJ().substring(2));
        // CHF
        sb.append("CHF");

        // Montant avec virgule (12x)
        sb.append(JadeStringUtil.rightJustifyInteger(ov.getMontant(), 15));
        sb.replace(CAProcessFormatOrdreDTA.EMPLACEMENT_DECIMALE, CAProcessFormatOrdreDTA.EMPLACEMENT_DECIMALE + 1, ",");

        // Réserve (11)
        sb.append(JadeStringUtil.leftJustify(" ", 11));

        // Insérer un retour chariot
        if (getInsertNewLine()) {
            sb.append(CAProcessFormatOrdreDTA.CRLF);
        }

    }

    private void formatDonneurOrdre(APICommonOdreVersement ov, StringBuffer sb, boolean international) throws Exception {
        // Si l'adresse est null, on sort
        if (adresseDonneurOrdre == null) {
            getMemoryLog().logMessage("5205", null, FWMessage.FATAL, this.getClass().getName());
            return;
        }

        // Genre d'enregistrement pour le donneur d'ordre
        sb.append("02");

        if (international) {
            sb.append(JadeStringUtil.leftJustify(ov.getCoursConversion(), 12));
        }

        // Récupérer l'adresse du donneur d'ordre sur 4 lignes
        String[] adr = adresseDonneurOrdre.getAdresseLines(4);

        // L'insérer (max 24 pos par lignes)
        for (int i = 0; i < adr.length; i++) {
            if (JadeStringUtil.isBlank(adr[i])) {
                sb.append(JadeStringUtil.leftJustify(" ", 24));
            } else {
                sb.append(JadeStringUtil.leftJustify(adr[i], 24));
            }
        }

        // Réserve de 30 caractères
        sb.append(JadeStringUtil.leftJustify(" ", 30));

        // Insérer un retour chariot
        if (getInsertNewLine()) {
            sb.append(CAProcessFormatOrdreDTA.CRLF);
        }
    }

    /**
     * Genre de transaction 836
     * Segment 02
     * 
     * @param ov
     * @param international
     * @return
     * @throws Exception
     */
    private StringBuffer gt836Segment02DonneurOrdre(APICommonOdreVersement ov, boolean international) throws Exception {
        StringBuffer sb = new StringBuffer("");

        // Genre d'enregistrement pour le donneur d'ordre
        sb.append("02");

        if (international) {
            sb.append(JadeStringUtil.leftJustify(ov.getCoursConversion(), 12));
        } else {
            sb.append(JadeStringUtil.leftJustify(" ", 12));
        }

        // Récupérer l'adresse du donneur d'ordre sur 3 lignes
        String[] adr = adresseDonneurOrdre.getAdresseLines(3);

        // L'insérer (max 35 pos par lignes)
        for (int i = 0; i < adr.length; i++) {
            if (JadeStringUtil.isBlank(adr[i])) {
                sb.append(JadeStringUtil.leftJustify(" ", 35));
            } else {
                sb.append(JadeStringUtil.leftJustify(adr[i], 35));
            }
        }

        // Réserve de 9 caractères
        sb.append(JadeStringUtil.leftJustify(" ", 9));

        // Insérer un retour chariot
        if (getInsertNewLine()) {
            sb.append(CAProcessFormatOrdreDTA.CRLF);
        }

        return sb;
    }

    /**
     * Genre de transaction de fin 890
     * Formatter l'enregistrement final de l'ordre groupé
     */
    @Override
    public StringBuffer formatEOF(APIOrdreGroupe og) throws Exception {
        // Vérifications
        if (og == null) {
            return null;
        }

        if (ordreGroupe == null) {
            return null;
        }

        // Préparer un buffer
        StringBuffer sb = new StringBuffer();

        // Insérer le header
        sb.append(header.substring(0, 53));

        // sb.replace(CAProcessFormatOrdreDTA.NO_REFERENCE_START, CAProcessFormatOrdreDTA.NO_REFERENCE_END,
        // JadeStringUtil.leftJustify(String.valueOf(++numTransaction), 11));

        // genre de transaction
        sb.replace(CAProcessFormatOrdreDTA.GENRE_TRANSACTION_START, CAProcessFormatOrdreDTA.GENRE_TRANSACTION_END,
                "890");

        // Insérer le numéro de transaction
        String numLastTransaction = String.valueOf(Integer.valueOf(og.getNbTransactions()) + 1);
        sb.replace(CAProcessFormatOrdreDTA.NUM_TRANSACTION_START, CAProcessFormatOrdreDTA.NUM_TRANSACTION_END,
                JadeStringUtil.rightJustifyInteger(numLastTransaction, 5));

        // Insérer le montant total
        sb.append(JadeStringUtil.rightJustifyInteger(og.getTotal(), 16));
        sb.replace(66, 67, ",");

        // Réserve 59
        sb.append(JadeStringUtil.leftJustify(" ", 59));

        // Insérer un retour chariot
        if (getInsertNewLine()) {
            sb.append(CAProcessFormatOrdreDTA.CRLF);
        }

        // Transmettre le buffer
        if (sb != null) {
            getPrintWriter().print(sb);

            // Echo à la console
            if (getEchoToConsole()) {
                System.out.println(sb);
            }
        }

        return sb;
    }

    /**
     * Header contenu dans le segment 01 commun à tous les genres de transactions
     * Formatter l'entête de l'ordre groupé
     */
    @Override
    public StringBuffer formatHeader(APIOrdreGroupe og) throws Exception {

        try {
            // Sauvegarder l'ordre groupé
            ordreGroupe = og;

            // Vérifier que l'adresse de paiement est une banque
            if (!og.getOrganeExecution().getAdressePaiement().getTypeAdresse().equals(IntAdressePaiement.BANQUE)) {
                getMemoryLog().logMessage("5207", null, FWMessage.FATAL, this.getClass().getName());
                return null;
            }

            // Stocker l'adresse du donneur d'ordre
            IntAdresseCourrier adrCo = og.getOrganeExecution().getAdressePaiement().getTiersTitulaire()
                    .getAdresseCourrier(IntAdresseCourrier.PRINCIPALE);
            if (adrCo == null) {
                getMemoryLog().logMessage("5208", null, FWMessage.FATAL, this.getClass().getName());
                return null;
            }
            adresseDonneurOrdre = new CAAdresseCourrierFormatter();
            adresseDonneurOrdre.setAdresseCourrier(og.getOrganeExecution().getAdressePaiement().getTiersTitulaire(),
                    adrCo);
            adresseDonneurOrdre.setUseTitle(false);
            adresseDonneurOrdre.setUseCountry(false);

            // Préparer le header de base pour toutes les transactions
            StringBuffer sb = new StringBuffer();

            // Genre d'enregistrement
            sb.append("01");

            // Insérer la date d'échéance
            JADate echeance = new JADate(og.getDateEcheance());
            sb.append(echeance.toStrAMJ().substring(2));

            // Insérer un blanc pour le clearing du bénéficiaire (12x)
            sb.append("            ");

            // Réserve
            sb.append("00000");

            // Insérer la date d'exécution
            JADate today = JACalendar.today();
            sb.append(today.toStrAMJ().substring(2));

            // Insérer le numéro de clearing du donneur d'ordre (7n)
            String clearing = og.getOrganeExecution().getAdressePaiement().getBanque().getClearing().trim();

            if (JadeStringUtil.isBlank(clearing)) {
                getMemoryLog().logMessage("5209", null, FWMessage.FATAL, this.getClass().getName());
                return null;
            }

            sb.append(JadeStringUtil.leftJustify(clearing, 7));

            // Insérer l'identifiant DTA (5x)
            sb.append(JadeStringUtil.leftJustify(og.getOrganeExecution().getIdentifiantDTA(), 5));

            // Préparer pour le numéro de séquence (5n)
            sb.append("00000");

            // Préparer le genre de transaction (5n);
            sb.append("000");

            // Code salaires et flag traitement
            sb.append("00");

            // Numéro de référence : Identifiant DTA (5x)
            sb.append(JadeStringUtil.leftJustify(ordreGroupe.getOrganeExecution().getIdentifiantDTA(), 5));

            // Référence interne
            sb.append(JadeStringUtil.leftJustify(" ", 11));

            // Compte à débiter
            sb.append(JadeStringUtil.leftJustify(og.getOrganeExecution().getAdressePaiement().getNumCompte(), 24));

            // stocker le header
            header = sb.toString();

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }

        // On retourne null car il n'y a pas d'enregistrement d'entête seul dans
        // le format DTA
        return null;
    }

    /**
     * Format un paiements à l'étranger.
     * Genre de transaction 830
     * 
     * @param ov
     * @param sb
     */
    private void formatInternational(APICommonOdreVersement ov, StringBuffer sb) throws Exception {
        // Mettre en cache les valeurs
        String compte = ov.getAdressePaiement().getNumCompte().trim();
        String swift = ov.getAdressePaiement().getBanque().getCodeSwiftWithoutSpaces().trim();

        // Vérifier le numéro de compte
        if (JadeStringUtil.isBlank(compte)) {
            getMemoryLog().logMessage("5211", null, FWMessage.ERREUR, this.getClass().getName());
            return;
        }

        // Vérification du code swift
        if ((JadeStringUtil.isBlank(swift))
                || (swift.length() < CAAdressePaiementFormatter.CODE_SWIFT_FORMAT_MIN_LENGTH)
                || (swift.length() > CAAdressePaiementFormatter.CODE_SWIFT_FORMAT_MAX_LENGTH)) {
            getMemoryLog().logMessage("5228", null, FWMessage.FATAL, this.getClass().getName());
            return;
        }

        // genre de transaction
        sb.replace(CAProcessFormatOrdreDTA.GENRE_TRANSACTION_START, CAProcessFormatOrdreDTA.GENRE_TRANSACTION_END,
                "830");

        // Numéro de référence : numéro de transaction (11x)
        sb.replace(CAProcessFormatOrdreDTA.NO_REFERENCE_START, CAProcessFormatOrdreDTA.NO_REFERENCE_END,
                JadeStringUtil.leftJustify(ov.getNumTransaction(), 11));

        // Bonification 6x + CODE ISO Monnaie
        sb.append("      " + ov.getCodeISOMonnaieDepot());

        // Montant avec virgule (12x)
        sb.append(JadeStringUtil.rightJustifyInteger(ov.getMontant(), 12));
        sb.replace(CAProcessFormatOrdreDTA.VIRGULE, CAProcessFormatOrdreDTA.VIRGULE + 1, ",");

        // Réserve (14)
        sb.append(JadeStringUtil.leftJustify(" ", 14));

        // Insérer un retour chariot
        if (getInsertNewLine()) {
            sb.append(CAProcessFormatOrdreDTA.CRLF);
        }

        // Donneur d'ordre
        formatDonneurOrdre(ov, sb, true);

        // Identification Banque
        sb.append("03");

        // Identification de l'adresse bancaire : A pour Swift, D pour nom de la
        // banque
        sb.append("A");

        sb.append("/C/");

        sb.append(JadeStringUtil.leftJustify(swift, CAAdressePaiementFormatter.CODE_SWIFT_FORMAT_MAX_LENGTH));

        // Bénéficiaire
        sb.append("04");
        sb.append(JadeStringUtil.leftJustify("/C/" + compte, 30));
        formatAdresseBeneficiaire(ov, sb);

        // Motif
        if (!JadeStringUtil.isBlank(ov.getMotif())) {
            formatMotif(ov, sb, true);
        }
    }

    /**
     * Formatter le DTA - mandat de paiement en suisse
     * Date de création : (08.02.2002 16:21:18)
     * 
     * Genre de transaction 827
     * 
     * @param ov
     *            globaz.osiris.db.comptes.APICommonOdreVersement
     * @param sb
     *            StringBuffer
     */
    private void formatMandatCH(APICommonOdreVersement ov, StringBuffer sb) throws Exception {
        // genre de transaction
        sb.replace(CAProcessFormatOrdreDTA.GENRE_TRANSACTION_START, CAProcessFormatOrdreDTA.GENRE_TRANSACTION_END,
                "827");

        // Numéro de référence : numéro de transaction (11x)
        sb.replace(CAProcessFormatOrdreDTA.NO_REFERENCE_START, CAProcessFormatOrdreDTA.NO_REFERENCE_END,
                JadeStringUtil.leftJustify(ov.getNumTransaction(), 11));

        // Bonification 6x + CHF
        sb.append("      CHF");

        // Montant avec virgule (12x)
        sb.append(JadeStringUtil.rightJustifyInteger(ov.getMontant(), 12));
        sb.replace(CAProcessFormatOrdreDTA.VIRGULE, CAProcessFormatOrdreDTA.VIRGULE + 1, ",");

        // Réserve (14)
        sb.append(JadeStringUtil.leftJustify(" ", 14));

        // Insérer un retour chariot
        if (getInsertNewLine()) {
            sb.append(CAProcessFormatOrdreDTA.CRLF);
        }

        // Donneur d'ordre
        formatDonneurOrdre(ov, sb, false);

        // Bénéficiaire
        sb.append("03");
        sb.append(JadeStringUtil.leftJustify("/C/", 30));
        formatAdresseBeneficiaire(ov, sb);

        // Motif
        if (!JadeStringUtil.isBlank(ov.getMotif())) {
            formatMotif(ov, sb, false);
        }
    }

    private void formatMotif(APICommonOdreVersement ov, StringBuffer sb, boolean international) {
        // Genre d'enregistrement pour le motif
        if (international) {
            sb.append("05");
        } else {
            sb.append("04");
        }

        // Insérer le motif
        sb.append(JadeStringUtil.leftJustify(ov.getMotif(), 112));

        // Réserve de 14
        sb.append(JadeStringUtil.leftJustify(" ", 14));

        // Insérer un retour chariot
        if (getInsertNewLine()) {
            sb.append(CAProcessFormatOrdreDTA.CRLF);
        }
    }
}
