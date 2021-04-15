package globaz.osiris.db.ebill.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum CATraitementCodeErreurEBillEnum {

    NO_CODE_ERREUR("", ""),
    CODE_ERREUR_01("1", "01"),
    CODE_ERREUR_02("2", "02"),
    CODE_ERREUR_03("3", "03"),
    CODE_ERREUR_04("4", "04"),
    CODE_ERREUR_05("5", "05"),
    CODE_ERREUR_06("6", "06"),
    CODE_ERREUR_07("7", "07"),
    CODE_ERREUR_08("8", "08"),
    CODE_ERREUR_09("9", "09"),
    CODE_ERREUR_10("10", "10"),
    CODE_ERREUR_11("11", "11"),
    CODE_ERREUR_12("12", "12"),
    CODE_ERREUR_13("13", "13"),
    CODE_ERREUR_14("14", "14"),
    CODE_ERREUR_15("15", "15"),
    CODE_ERREUR_16("16", "16"),
    CODE_ERREUR_17("17", "17"),
    CODE_ERREUR_18("18", "18"),
    CODE_ERREUR_20("20", "20"),
    CODE_ERREUR_21("21", "21"),
    CODE_ERREUR_24("24", "24"),
    CODE_ERREUR_25("25", "25"),
    CODE_ERREUR_27("27", "27"),
    CODE_ERREUR_28("28", "28"),
    CODE_ERREUR_29("29", "29"),
    CODE_ERREUR_30("30", "30"),
    CODE_ERREUR_31("31", "31"),
    CODE_ERREUR_32("32", "32"),
    CODE_ERREUR_33("33", "33"),
    CODE_ERREUR_41("41", "41"),
    CODE_ERREUR_012("012", "012"),
    CODE_ERREUR_103("103", "103"),
    CODE_ERREUR_436("436", "436");

    private static final Logger LOG = LoggerFactory.getLogger(CATraitementCodeErreurEBillEnum.class);
    public static final String NUMERO_CODE_ERREUR_01 = "1";
    public static final String NUMERO_CODE_ERREUR_02 = "2";
    public static final String NUMERO_CODE_ERREUR_03 = "3";
    public static final String NUMERO_CODE_ERREUR_04 = "4";
    public static final String NUMERO_CODE_ERREUR_05 = "5";
    public static final String NUMERO_CODE_ERREUR_06 = "6";
    public static final String NUMERO_CODE_ERREUR_07 = "7";
    public static final String NUMERO_CODE_ERREUR_08 = "8";
    public static final String NUMERO_CODE_ERREUR_09 = "9";
    public static final String NUMERO_CODE_ERREUR_10 = "10";
    public static final String NUMERO_CODE_ERREUR_11 = "11";
    public static final String NUMERO_CODE_ERREUR_12 = "12";
    public static final String NUMERO_CODE_ERREUR_13 = "13";
    public static final String NUMERO_CODE_ERREUR_14 = "14";
    public static final String NUMERO_CODE_ERREUR_15 = "15";
    public static final String NUMERO_CODE_ERREUR_16 = "16";
    public static final String NUMERO_CODE_ERREUR_17 = "17";
    public static final String NUMERO_CODE_ERREUR_18 = "18";
    public static final String NUMERO_CODE_ERREUR_20 = "20";
    public static final String NUMERO_CODE_ERREUR_21 = "21";
    public static final String NUMERO_CODE_ERREUR_24 = "24";
    public static final String NUMERO_CODE_ERREUR_25 = "25";
    public static final String NUMERO_CODE_ERREUR_27 = "27";
    public static final String NUMERO_CODE_ERREUR_28 = "28";
    public static final String NUMERO_CODE_ERREUR_29 = "29";
    public static final String NUMERO_CODE_ERREUR_30 = "30";
    public static final String NUMERO_CODE_ERREUR_31 = "31";
    public static final String NUMERO_CODE_ERREUR_32 = "32";
    public static final String NUMERO_CODE_ERREUR_33 = "33";
    public static final String NUMERO_CODE_ERREUR_41 = "41";
    public static final String NUMERO_CODE_ERREUR_012 = "012";
    public static final String NUMERO_CODE_ERREUR_103 = "103";
    public static final String NUMERO_CODE_ERREUR_436 = "436";

    private final String numeroCodeErreur;
    private final String description;

    CATraitementCodeErreurEBillEnum(String numeroCodeErreur, String description) {
        this.numeroCodeErreur = numeroCodeErreur;
        this.description = description;
    }

    public static CATraitementCodeErreurEBillEnum parValeur(final String numeroType) {
        switch (numeroType) {
            case NUMERO_CODE_ERREUR_01:
                return CODE_ERREUR_01;
            case NUMERO_CODE_ERREUR_02:
                return CODE_ERREUR_02;
            case NUMERO_CODE_ERREUR_03:
                return CODE_ERREUR_03;
            case NUMERO_CODE_ERREUR_04:
                return CODE_ERREUR_04;
            case NUMERO_CODE_ERREUR_05:
                return CODE_ERREUR_05;
            case NUMERO_CODE_ERREUR_06:
                return CODE_ERREUR_06;
            case NUMERO_CODE_ERREUR_07:
                return CODE_ERREUR_07;
            case NUMERO_CODE_ERREUR_08:
                return CODE_ERREUR_08;
            case NUMERO_CODE_ERREUR_09:
                return CODE_ERREUR_09;
            case NUMERO_CODE_ERREUR_10:
                return CODE_ERREUR_10;
            case NUMERO_CODE_ERREUR_11:
                return CODE_ERREUR_11;
            case NUMERO_CODE_ERREUR_12:
                return CODE_ERREUR_12;
            case NUMERO_CODE_ERREUR_13:
                return CODE_ERREUR_13;
            case NUMERO_CODE_ERREUR_14:
                return CODE_ERREUR_14;
            case NUMERO_CODE_ERREUR_15:
                return CODE_ERREUR_15;
            case NUMERO_CODE_ERREUR_16:
                return CODE_ERREUR_16;
            case NUMERO_CODE_ERREUR_17:
                return CODE_ERREUR_17;
            case NUMERO_CODE_ERREUR_18:
                return CODE_ERREUR_18;
            case NUMERO_CODE_ERREUR_20:
                return CODE_ERREUR_20;
            case NUMERO_CODE_ERREUR_21:
                return CODE_ERREUR_21;
            case NUMERO_CODE_ERREUR_24:
                return CODE_ERREUR_24;
            case NUMERO_CODE_ERREUR_25:
                return CODE_ERREUR_25;
            case NUMERO_CODE_ERREUR_27:
                return CODE_ERREUR_27;
            case NUMERO_CODE_ERREUR_28:
                return CODE_ERREUR_28;
            case NUMERO_CODE_ERREUR_29:
                return CODE_ERREUR_29;
            case NUMERO_CODE_ERREUR_30:
                return CODE_ERREUR_30;
            case NUMERO_CODE_ERREUR_31:
                return CODE_ERREUR_31;
            case NUMERO_CODE_ERREUR_32:
                return CODE_ERREUR_32;
            case NUMERO_CODE_ERREUR_33:
                return CODE_ERREUR_33;
            case NUMERO_CODE_ERREUR_41:
                return CODE_ERREUR_41;
            case NUMERO_CODE_ERREUR_012:
                return CODE_ERREUR_012;
            case NUMERO_CODE_ERREUR_103:
                return CODE_ERREUR_103;
            case NUMERO_CODE_ERREUR_436:
                return CODE_ERREUR_436;
            default:
                LOG.warn("Le code erreur n'a pas été trouvé : " + numeroType);
                return NO_CODE_ERREUR;
        }
    }

    public String getNumeroCodeErreur() {
        return numeroCodeErreur;
    }

    public String getDescription() {
        return description;
    }
}
