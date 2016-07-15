package ch.globaz.common.util.prestations;

/**
 * Permet de formatter les motifs de versements.
 * 
 * @author dcl
 * 
 */
public class MotifVersementUtil {

    private static final int NB_CHAR_PER_MOTIF = 35;
    private static final int NB_CHAR_MAX = 140;
    private static final String SEPARATOR = " ";

    private MotifVersementUtil() {
    }

    /**
     * Formatage du motif de versement pour les paiements mensuelles.
     * 
     * @param nss Le NSS.
     * @param nomPrenom Le nom et prénom.
     * @param refPaiement La référence de paiement.
     * @param genrePrestation La prestation Rente/PC...
     * @param pP Le mois.année payé.
     * @return La chaîne totale.
     */
    public static String formatPaiementMensuel(final String nss, final String nomPrenom, final String refPaiement,
            final String genrePrestation, final String pP) {

        final StringBuilder motifDeRetour = new StringBuilder();
        motifDeRetour.append(nss);
        motifDeRetour.append(appendSeparator(nomPrenom));
        motifDeRetour.append(appendSeparator(refPaiement));
        motifDeRetour.append(appendSeparator(genrePrestation));
        motifDeRetour.append(appendSeparator(pP));

        if (motifDeRetour.length() > NB_CHAR_MAX) {
            StringBuilder motif1 = new StringBuilder();
            motif1.append(nss);
            motif1.append(appendSeparator(nomPrenom));
            motif1 = subSequenceTo35Chars(motif1);

            StringBuilder motif2 = new StringBuilder();
            motif2.append(appendSeparator(refPaiement));
            motif2 = subSequenceTo35Chars(motif2);

            StringBuilder motif3 = new StringBuilder();
            motif3.append(appendSeparator(genrePrestation));
            motif3.append(appendSeparator(pP));
            motif3 = subSequenceTo35Chars(motif3);

            return motif1.append(motif2).append(motif3).toString();
        }

        return motifDeRetour.toString();
    }

    /**
     * Formatage du motif de versement pour les décisions.
     * 
     * @param nss Le NSS.
     * @param nomPrenom Le nom et prénom.
     * @param refPaiement La référence de paiement.
     * @param genrePrestation La prestation Rente/PC...
     * @param periode La période de la décision.
     * @param msgDecision Le message de la décision.
     * @return La chaîne totale.
     */
    public static String formatDecision(final String nss, final String nomPrenom, final String refPaiement,
            final String genrePrestation, final String periode, final String msgDecision) {

        final StringBuilder motifDeRetour = new StringBuilder();
        motifDeRetour.append(nss);
        motifDeRetour.append(appendSeparator(nomPrenom));
        motifDeRetour.append(appendSeparator(refPaiement));
        motifDeRetour.append(appendSeparator(genrePrestation));
        motifDeRetour.append(appendSeparator(periode));
        motifDeRetour.append(appendSeparator(msgDecision));

        if (motifDeRetour.length() > NB_CHAR_MAX) {
            StringBuilder motif1 = new StringBuilder();
            motif1.append(nss);
            motif1.append(appendSeparator(nomPrenom));
            motif1 = subSequenceTo35Chars(motif1);

            StringBuilder motif2 = new StringBuilder();
            motif2.append(appendSeparator(refPaiement));
            motif2 = subSequenceTo35Chars(motif2);

            StringBuilder motif3 = new StringBuilder();
            motif3.append(appendSeparator(genrePrestation));
            motif3.append(appendSeparator(periode));
            motif3 = subSequenceTo35Chars(motif3);

            StringBuilder motif4 = new StringBuilder();
            motif4.append(appendSeparator(msgDecision));
            motif4 = subSequenceTo35Chars(motif4);

            return motif1.append(motif2).append(motif3).append(motif4).toString();
        }

        return motifDeRetour.toString();
    }

    /**
     * Formatage du motif de versement pour le déblocage.
     * 
     * @param nss Le NSS.
     * @param nomPrenom Le nom et prénom.
     * @param refPaiement La référence de paiement.
     * @param genrePrestation La prestation Rente/PC...
     * @param versementDu Le versement du.
     * @return La chaîne totale.
     */
    public static String formatDeblocage(final String nss, final String nomPrenom, final String refPaiement,
            final String genrePrestation, final String versementDu) {

        final StringBuilder motifDeRetour = new StringBuilder();
        motifDeRetour.append(nss);
        motifDeRetour.append(appendSeparator(nomPrenom));
        motifDeRetour.append(appendSeparator(refPaiement));
        motifDeRetour.append(appendSeparator(genrePrestation));
        motifDeRetour.append(appendSeparator(versementDu));

        if (motifDeRetour.length() > NB_CHAR_MAX) {
            StringBuilder motif1 = new StringBuilder();
            motif1.append(nss);
            motif1.append(appendSeparator(nomPrenom));
            motif1 = subSequenceTo35Chars(motif1);

            StringBuilder motif2 = new StringBuilder();
            motif2.append(appendSeparator(refPaiement));
            motif2 = subSequenceTo35Chars(motif2);

            StringBuilder motif3 = new StringBuilder();
            motif3.append(appendSeparator(genrePrestation));
            motif3 = subSequenceTo35Chars(motif3);

            StringBuilder motif4 = new StringBuilder();
            motif4.append(appendSeparator(versementDu));
            motif4 = subSequenceTo35Chars(motif4);

            return motif1.append(motif2).append(motif3).append(motif4).toString();
        }

        return motifDeRetour.toString();
    }

    /**
     * Formatage du motif de versement pour les avances.
     * 
     * @param nss Le NSS.
     * @param nomPrenom Le nom et prénom.
     * @param avance Le message de l'avance.
     * @return La chaîne totale.
     */
    public static String formatAvance(final String nss, final String nomPrenom, final String avance) {

        final StringBuilder motifDeRetour = new StringBuilder();
        motifDeRetour.append(nss);
        motifDeRetour.append(appendSeparator(nomPrenom));
        motifDeRetour.append(appendSeparator(avance));

        if (motifDeRetour.length() > NB_CHAR_MAX) {
            StringBuilder motif1 = new StringBuilder();
            motif1.append(nss);
            motif1.append(appendSeparator(nomPrenom));
            motif1 = subSequenceTo35Chars(motif1);

            StringBuilder motif2 = new StringBuilder();
            motif2.append(appendSeparator(avance));
            motif2 = subSequenceTo35Chars(motif2);

            return motif1.append(motif2).toString();
        }

        return motifDeRetour.toString();
    }

    /**
     * Formatage du motif de versement pour les retours.
     * 
     * @param nss Le NSS.
     * @param nomPrenom Le nom et prénom.
     * @param mauvaiseAdresse La mauvaise adresse.
     * @return La chaîne totale.
     */
    public static String formatRetour(final String nss, final String nomPrenom, final String mauvaiseAdresse) {

        final StringBuilder motifDeRetour = new StringBuilder();
        motifDeRetour.append(nss);
        motifDeRetour.append(appendSeparator(nomPrenom));
        motifDeRetour.append(appendSeparator(mauvaiseAdresse));

        if (motifDeRetour.length() > NB_CHAR_MAX) {
            StringBuilder motif1 = new StringBuilder();
            motif1.append(nss);
            motif1.append(appendSeparator(nomPrenom));
            motif1 = subSequenceTo35Chars(motif1);

            StringBuilder motif2 = new StringBuilder();
            motif2.append(appendSeparator(mauvaiseAdresse));
            motif2 = subSequenceTo35Chars(motif2);

            return motif1.append(motif2).toString();
        }

        return motifDeRetour.toString();
    }

    private static String appendSeparator(final String chaine) {
        if (chaine == null || chaine.trim().isEmpty()) {
            return "";
        }
        return SEPARATOR + chaine;
    }

    private static StringBuilder subSequenceTo35Chars(final StringBuilder builder) {
        if (builder.length() > NB_CHAR_PER_MOTIF) {
            return new StringBuilder(builder.subSequence(0, NB_CHAR_PER_MOTIF - 1));
        }
        return builder;
    }
}
