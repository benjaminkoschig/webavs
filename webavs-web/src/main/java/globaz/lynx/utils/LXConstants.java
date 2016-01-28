package globaz.lynx.utils;

public class LXConstants {

    public static final String CODE_ISO_CHF = "510002";

    public static final String ETAT_DEFINITIF = "DEFINITIF";

    public static final String ETAT_PROVISOIRE = "PROVISOIRE";
    public static final int INDEX_COLUMN_LIBELLE = 0;

    public static final int INDEX_COLUMN_SOLDE_A = 1;
    public static final int INDEX_COLUMN_SOLDE_B = 2;

    public static final int INDEX_COLUMN_SOLDE_C = 3;
    public static final int INDEX_COLUMN_SOLDE_D = 4;
    public static final int INDEX_COLUMN_SOLDE_E = 5;
    public static final int INDEX_COLUMN_SOLDE_F = 6;
    public static final int INDEX_COLUMN_SOLDE_G = 7;

    public static final int INDEX_COLUMN_SOLDE_H = 8;
    public static final int INDEX_COLUMN_SOLDE_I = 9;
    public static final int INDEX_COLUMN_SOLDE_J = 10;
    public static final int INDEX_COLUMN_SOLDE_K = 11;
    public static final int INDEX_COLUMN_SOLDE_L = 12;
    public static final int INDEX_COLUMN_SOLDE_M = 13;
    public static final int INDEX_COLUMN_SOLDE_N = 14;
    public static final int INDEX_COLUMN_SOLDE_O = 15;
    public static final String NUMERO_REFERENCE_INFOROM_BALANCE = "0220GLX";
    public static final String NUMERO_REFERENCE_INFOROM_BALANCE_AGEE = "0221GLX";
    public static final String NUMERO_REFERENCE_INFOROM_GRAND_LIVRE = "0222GLX";
    // --------------------------------------------
    // Pour les impresssions
    // --------------------------------------------
    public static final String NUMERO_REFERENCE_INFOROM_RESUME_JOURNAL = "0218GLX";
    public static final String NUMERO_REFERENCE_INFOROM_RESUME_ORDREGROUPE = "0219GLX";
    public static final String OUTPUT_FILE_NAME_BALANCE = "balance";
    public static final String OUTPUT_FILE_NAME_BALANCE_AGEE = "balance_agee";
    public static final String OUTPUT_FILE_NAME_GRAND_LIVRE = "grand_livre";

    public static final String OUTPUT_FILE_NAME_RESUME_JOURNAL = "resume_journal";
    public static final String OUTPUT_FILE_NAME_RESUME_ORDREGROUPE = "resume_ordregroupe";
    public static final String OUTPUT_FILE_WORK_DIR = "work";
    public static final String SELECTION_OUVERT = "OUVERT";
    public static final String SELECTION_SOLDE = "SOLDER";
    public static final boolean USE_COLOR = true;

    /**
     * Constructeur
     */
    protected LXConstants() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }
}
