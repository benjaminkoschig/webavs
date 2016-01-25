package globaz.osiris.db.ordres.format;

import globaz.framework.util.FWMessage;
import globaz.globall.util.JACCP;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.ordreversement.LXOrdreVersement;
import globaz.osiris.api.ordre.APICommonOdreVersement;
import globaz.osiris.api.ordre.APIOrdreGroupe;
import globaz.osiris.db.comptes.CAOperationOrdreRecouvrement;
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
public final class CAProcessFormatOrdreDTAFournisseur extends CAOrdreFormateur {
    public final static int CLEARING_BENEFICIAIRE_END = 20;
    public final static int CLEARING_BENEFICIAIRE_START = 8;

    public final static String CRLF = "\n";

    public static final int DEBUT_DATE_PAIEMENT_PREVU = 2;
    public static final int FIN_DATE_PAIEMENT_PREVU = 8;
    public static final int GENRE_TRANSACTION_END = 51;
    private static final String GENRE_TRANSACTION_PAIEMENT_BVR = "826";
    private static final String GENRE_TRANSACTION_PAIEMENT_CHF_SUISSE = "827";
    public static final int GENRE_TRANSACTION_START = 48;
    public static final int NO_REFERENCE_END = 69;
    public static final int NO_REFERENCE_START = 58;
    public static final int NUM_TRANSACTION_END = 48;
    public static final int NUM_TRANSACTION_START = 43;
    public static final int TAILLE_20 = 20;
    public static final int TAILLE_24 = 24;
    public final static int VIRGULE = 111;

    private CAAdresseCourrierFormatter adresseDonneurOrdre;

    private String header = null;

    /**
     * Commentaire relatif au constructeur CAProcessFormatOrdreDTA.
     */
    public CAProcessFormatOrdreDTAFournisseur() {
        super();
        setPrintWriter(new PrintWriter(System.out));
    }

    /**
     * Commentaire formatter une transaction isolée
     */
    @Override
    public StringBuffer format(APICommonOdreVersement ov) throws Exception {

        if (!(ov instanceof LXOrdreVersement)) {
            throw new Exception("Unabled to format the versement. Bad class");
        }

        // On récupère notre ordre de versement de type compta four
        LXOrdreVersement lxOv = (LXOrdreVersement) ov;

        // Vérifications
        if (lxOv == null) {
            return null;
        }

        if (ordreGroupe == null) {
            return null;
        }

        // Préparer un buffer
        StringBuffer sb = new StringBuffer();

        // Insérer le header
        sb.append(header);

        // Insérer le numéro de transaction
        sb.replace(CAProcessFormatOrdreDTAFournisseur.NUM_TRANSACTION_START,
                CAProcessFormatOrdreDTAFournisseur.NUM_TRANSACTION_END,
                JadeStringUtil.rightJustifyInteger(lxOv.getNumTransaction(), 5));

        // Sélection en fonction du type d'adresse de paiement
        String typeAd = lxOv.getAdressePaiement().getTypeAdresse();

        // Vérifier
        if (typeAd.equals(IntAdressePaiement.BANQUE)) {
            formatBanqueCH(lxOv, sb);
        } else if (typeAd.equals(IntAdressePaiement.MANDAT)) {
            formatMandatCH(lxOv, sb);
        } else if (typeAd.equals(IntAdressePaiement.CCP)) {
            formatCCPCH(lxOv, sb);
        } else if (typeAd.equals(IntAdressePaiement.BANQUE_INTERNATIONAL)) {
            formatInternational(lxOv, sb);
        } else {
            getMemoryLog().logMessage("5206", typeAd, FWMessage.FATAL, this.getClass().getName());
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
     * Commentaire relatif à la méthode format.
     */
    @Override
    public StringBuffer format(CAOperationOrdreRecouvrement or) throws Exception {
        return null;
    }

    private void formatAdresseBeneficiaire(LXOrdreVersement ov, StringBuffer sb, int taille) throws Exception {

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

        // L'insérer (max "taille" pos par lignes)
        for (int i = 0; i < adr.length; i++) {
            if (JadeStringUtil.isBlank(adr[i])) {
                sb.append(JadeStringUtil.leftJustify(" ", taille));
            } else {
                sb.append(JadeStringUtil.leftJustify(adr[i], taille));
            }
        }

        // Insérer un retour chariot
        if (getInsertNewLine()) {
            sb.append(CAProcessFormatOrdreDTAFournisseur.CRLF);
        }
    }

    /**
     * @param ov
     * @param sb
     * @throws Exception
     */
    private void formatBanqueCH(LXOrdreVersement ov, StringBuffer sb) throws Exception {
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

        // Si il y a un numero de référence BVR, on fait un paiement BVR (Code 826)
        if (!JadeStringUtil.isEmpty(ov.getReferenceBVR()) && !"null".equals(ov.getReferenceBVR())) {
            // genre de transaction
            sb.replace(CAProcessFormatOrdreDTAFournisseur.GENRE_TRANSACTION_START,
                    CAProcessFormatOrdreDTAFournisseur.GENRE_TRANSACTION_END,
                    CAProcessFormatOrdreDTAFournisseur.GENRE_TRANSACTION_PAIEMENT_BVR);

        } else {
            // Sinon on fait un paiement classique
            // genre de transaction
            sb.replace(CAProcessFormatOrdreDTAFournisseur.GENRE_TRANSACTION_START,
                    CAProcessFormatOrdreDTAFournisseur.GENRE_TRANSACTION_END,
                    CAProcessFormatOrdreDTAFournisseur.GENRE_TRANSACTION_PAIEMENT_CHF_SUISSE);
        }

        // Numéro de référence : numéro de transaction (11x)
        sb.replace(CAProcessFormatOrdreDTAFournisseur.NO_REFERENCE_START,
                CAProcessFormatOrdreDTAFournisseur.NO_REFERENCE_END,
                JadeStringUtil.leftJustify(ov.getNumTransaction(), 11));

        // Numéro de clearing du bénéficiaire
        sb.replace(CAProcessFormatOrdreDTAFournisseur.CLEARING_BENEFICIAIRE_START,
                CAProcessFormatOrdreDTAFournisseur.CLEARING_BENEFICIAIRE_END, JadeStringUtil.leftJustify(clearing, 12));

        // Date prévu du paiement
        JADate echeance = new JADate(ov.getDateOperation());
        sb.replace(CAProcessFormatOrdreDTAFournisseur.DEBUT_DATE_PAIEMENT_PREVU,
                CAProcessFormatOrdreDTAFournisseur.FIN_DATE_PAIEMENT_PREVU, echeance.toStrAMJ().substring(2));

        // Bonification 6x + CHF
        sb.append("      CHF");

        // Montant avec virgule (12x)
        sb.append(JadeStringUtil.rightJustifyInteger(ov.getMontant(), 12));
        sb.replace(CAProcessFormatOrdreDTAFournisseur.VIRGULE, CAProcessFormatOrdreDTAFournisseur.VIRGULE + 1, ",");

        // Réserve (14)
        sb.append(JadeStringUtil.leftJustify(" ", 14));

        // Insérer un retour chariot
        if (getInsertNewLine()) {
            sb.append(CAProcessFormatOrdreDTAFournisseur.CRLF);
        }

        // Donneur d'ordre
        formatDonneurOrdre(ov, sb, false);

        // Segment 03
        sb.append("03");
        // Si il y a un numero de référence BVR, on fait un paiement BVR (Code 826)
        if (!JadeStringUtil.isEmpty(ov.getReferenceBVR())) {

            sb.append(JadeStringUtil.leftJustify("/C/" + JACCP.formatNoDash(compte), 12));
            formatAdresseBeneficiaire(ov, sb, CAProcessFormatOrdreDTAFournisseur.TAILLE_20);
            sb.append(JadeStringUtil.rightJustify(ov.getReferenceBVR(), 27, '0'));
            sb.append(JadeStringUtil.leftJustify(" ", 7)); // caractere vide

        } else {
            // Cas ou on a un IBAN (Code 827)
            sb.append(JadeStringUtil.leftJustify("/C/" + compte, 30));
            formatAdresseBeneficiaire(ov, sb, CAProcessFormatOrdreDTAFournisseur.TAILLE_24);
        }

        // Motif
        if (!JadeStringUtil.isBlank(ov.getMotif())) {
            formatMotif(ov, sb, false);
        }
    }

    /**
     * @param ov
     * @param sb
     * @throws Exception
     */
    private void formatCCPCH(LXOrdreVersement ov, StringBuffer sb) throws Exception {

        // Mettre en cache les valeurs
        String ccp = null;

        // Vérifier le numéro de ccp
        try {
            ccp = JACCP.parseCCP(ov.getAdressePaiement().getNumCompte().trim());
        } catch (Exception e) {
            getMemoryLog().logMessage("5213", null, FWMessage.ERREUR, this.getClass().getName());
            return;
        }

        // Si il y a un numero de référence BVR, on fait un paiement BVR (Code 826)
        if (!JadeStringUtil.isEmpty(ov.getReferenceBVR())) {
            // genre de transaction
            sb.replace(CAProcessFormatOrdreDTAFournisseur.GENRE_TRANSACTION_START,
                    CAProcessFormatOrdreDTAFournisseur.GENRE_TRANSACTION_END,
                    CAProcessFormatOrdreDTAFournisseur.GENRE_TRANSACTION_PAIEMENT_BVR);
        } else {
            // genre de transaction
            sb.replace(CAProcessFormatOrdreDTAFournisseur.GENRE_TRANSACTION_START,
                    CAProcessFormatOrdreDTAFournisseur.GENRE_TRANSACTION_END,
                    CAProcessFormatOrdreDTAFournisseur.GENRE_TRANSACTION_PAIEMENT_CHF_SUISSE);
        }

        // Numéro de référence : numéro de transaction (11x)
        sb.replace(CAProcessFormatOrdreDTAFournisseur.NO_REFERENCE_START,
                CAProcessFormatOrdreDTAFournisseur.NO_REFERENCE_END,
                JadeStringUtil.leftJustify(ov.getNumTransaction(), 11));

        // Date prévu du paiement
        JADate echeance = new JADate(ov.getDateOperation());
        sb.replace(CAProcessFormatOrdreDTAFournisseur.DEBUT_DATE_PAIEMENT_PREVU,
                CAProcessFormatOrdreDTAFournisseur.FIN_DATE_PAIEMENT_PREVU, echeance.toStrAMJ().substring(2));

        // Bonification 6x + CHF
        sb.append("      CHF");

        // Montant avec virgule (12x)
        sb.append(JadeStringUtil.rightJustifyInteger(ov.getMontant(), 12));
        sb.replace(CAProcessFormatOrdreDTAFournisseur.VIRGULE, CAProcessFormatOrdreDTAFournisseur.VIRGULE + 1, ",");

        // Réserve (14)
        sb.append(JadeStringUtil.leftJustify(" ", 14));

        // Insérer un retour chariot
        if (getInsertNewLine()) {
            sb.append(CAProcessFormatOrdreDTAFournisseur.CRLF);
        }

        // Donneur d'ordre
        formatDonneurOrdre(ov, sb, false);

        // Bénéficiaire
        sb.append("03");
        // Si il y a un numero de référence BVR, on fait un paiement BVR (Code 826)
        if (!JadeStringUtil.isEmpty(ov.getReferenceBVR())) {

            if (ccp.length() == 5) {
                // Compte à 5 chiffres
                // Alignement a gauche du numero de reference qui comporte 15 chiffres
                sb.append("/C/");
                sb.append(JadeStringUtil.rightJustify(JACCP.formatNoDash(ccp), 9, '0'));

            } else {
                sb.append(JadeStringUtil.leftJustify("/C/" + JACCP.formatNoDash(ccp), 12));
            }
            formatAdresseBeneficiaire(ov, sb, CAProcessFormatOrdreDTAFournisseur.TAILLE_20);
            sb.append(JadeStringUtil.rightJustify(ov.getReferenceBVR(), 27, '0'));
            sb.append(JadeStringUtil.leftJustify(" ", 7)); // caractere vide

        } else {
            // (Code 827)
            sb.append(JadeStringUtil.leftJustify("/C/" + ccp, 30));
            formatAdresseBeneficiaire(ov, sb, CAProcessFormatOrdreDTAFournisseur.TAILLE_24);
        }

        // Motif
        if (!JadeStringUtil.isBlank(ov.getMotif())) {
            formatMotif(ov, sb, false);
        }
    }

    /**
     * @param ov
     * @param sb
     * @param international
     * @throws Exception
     */
    private void formatDonneurOrdre(LXOrdreVersement ov, StringBuffer sb, boolean international) throws Exception {
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
            sb.append(CAProcessFormatOrdreDTAFournisseur.CRLF);
        }
    }

    /**
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

        // genre de transaction
        sb.replace(CAProcessFormatOrdreDTAFournisseur.GENRE_TRANSACTION_START,
                CAProcessFormatOrdreDTAFournisseur.GENRE_TRANSACTION_END, "890");

        // Insérer le numéro de transaction
        sb.replace(CAProcessFormatOrdreDTAFournisseur.NUM_TRANSACTION_START,
                CAProcessFormatOrdreDTAFournisseur.NUM_TRANSACTION_END,
                JadeStringUtil.rightJustifyInteger(og.getNbTransactions(), 5));

        // Insérer le montant total
        sb.append(JadeStringUtil.rightJustifyInteger(og.getTotal(), 16));
        sb.replace(66, 67, ",");

        // Réserve 59
        sb.append(JadeStringUtil.leftJustify(" ", 59));

        // Insérer un retour chariot
        if (getInsertNewLine()) {
            sb.append(CAProcessFormatOrdreDTAFournisseur.CRLF);
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
     * 
     * @param ov
     * @param sb
     */
    private void formatInternational(LXOrdreVersement ov, StringBuffer sb) throws Exception {
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
        sb.replace(CAProcessFormatOrdreDTAFournisseur.GENRE_TRANSACTION_START,
                CAProcessFormatOrdreDTAFournisseur.GENRE_TRANSACTION_END, "830");

        // Numéro de référence : numéro de transaction (11x)
        sb.replace(CAProcessFormatOrdreDTAFournisseur.NO_REFERENCE_START,
                CAProcessFormatOrdreDTAFournisseur.NO_REFERENCE_END,
                JadeStringUtil.leftJustify(ov.getNumTransaction(), 11));

        // Bonification 6x + CODE ISO Monnaie
        sb.append("      " + ov.getCodeISOMonnaieDepot());

        // Montant avec virgule (12x)
        sb.append(JadeStringUtil.rightJustifyInteger(ov.getMontant(), 12));
        sb.replace(CAProcessFormatOrdreDTAFournisseur.VIRGULE, CAProcessFormatOrdreDTAFournisseur.VIRGULE + 1, ",");

        // Réserve (14)
        sb.append(JadeStringUtil.leftJustify(" ", 14));

        // Insérer un retour chariot
        if (getInsertNewLine()) {
            sb.append(CAProcessFormatOrdreDTAFournisseur.CRLF);
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
        formatAdresseBeneficiaire(ov, sb, CAProcessFormatOrdreDTAFournisseur.TAILLE_24);

        // Motif
        if (!JadeStringUtil.isBlank(ov.getMotif())) {
            formatMotif(ov, sb, true);
        }
    }

    /**
     * Formatter le DTA - mandat de paiement en suisse Date de création : (08.02.2002 16:21:18)
     * 
     * @param ov
     *            globaz.osiris.db.comptes.APICommonOdreVersement
     * @param sb
     *            StringBuffer
     */
    private void formatMandatCH(LXOrdreVersement ov, StringBuffer sb) throws Exception {
        // genre de transaction
        sb.replace(CAProcessFormatOrdreDTAFournisseur.GENRE_TRANSACTION_START,
                CAProcessFormatOrdreDTAFournisseur.GENRE_TRANSACTION_END,
                CAProcessFormatOrdreDTAFournisseur.GENRE_TRANSACTION_PAIEMENT_CHF_SUISSE);

        // Numéro de référence : numéro de transaction (11x)
        sb.replace(CAProcessFormatOrdreDTAFournisseur.NO_REFERENCE_START,
                CAProcessFormatOrdreDTAFournisseur.NO_REFERENCE_END,
                JadeStringUtil.leftJustify(ov.getNumTransaction(), 11));

        // Bonification 6x + CHF
        sb.append("      CHF");

        // Montant avec virgule (12x)
        sb.append(JadeStringUtil.rightJustifyInteger(ov.getMontant(), 12));
        sb.replace(CAProcessFormatOrdreDTAFournisseur.VIRGULE, CAProcessFormatOrdreDTAFournisseur.VIRGULE + 1, ",");

        // Réserve (14)
        sb.append(JadeStringUtil.leftJustify(" ", 14));

        // Insérer un retour chariot
        if (getInsertNewLine()) {
            sb.append(CAProcessFormatOrdreDTAFournisseur.CRLF);
        }

        // Donneur d'ordre
        formatDonneurOrdre(ov, sb, false);

        // Bénéficiaire
        sb.append("03");
        sb.append(JadeStringUtil.leftJustify("/C/", 30));
        formatAdresseBeneficiaire(ov, sb, CAProcessFormatOrdreDTAFournisseur.TAILLE_24);

        // Motif
        if (!JadeStringUtil.isBlank(ov.getMotif())) {
            formatMotif(ov, sb, false);
        }
    }

    /**
     * @param ov
     * @param sb
     * @param international
     */
    private void formatMotif(APICommonOdreVersement ov, StringBuffer sb, boolean international) {
        // Genre d'enregistrement pour le motif
        if (international) {
            sb.append("05");
        } else {
            sb.append("04");
        }

        // Suppression des retour a la ligne
        String motif = JadeStringUtil.change(ov.getMotif(), "\r\n", "  ");

        // Insérer le motif
        sb.append(JadeStringUtil.leftJustify(motif, 112));

        // Réserve de 14
        sb.append(JadeStringUtil.leftJustify(" ", 14));

        // Insérer un retour chariot
        if (getInsertNewLine()) {
            sb.append(CAProcessFormatOrdreDTAFournisseur.CRLF);
        }
    }
}
