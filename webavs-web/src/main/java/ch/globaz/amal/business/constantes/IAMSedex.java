package ch.globaz.amal.business.constantes;

import java.math.BigInteger;

public interface IAMSedex {
    public static final String APPLICATION_TYPE_MANUFACTURER = "GLOBAZ SA";
    public static final String APPLICATION_TYPE_PRODUCT_NAME = "Web@AVS";
    public static final String APPLICATION_TYPE_PRODUCT_VERSION = "1-18-00";
    public static final String MESSAGE_ACTION_DEMANDE = "5";
    public static final String MESSAGE_ACTION_NOUVEAU = "1";
    public static final String MESSAGE_ACTION_REPONSE = "6";
    public static final BigInteger MESSAGE_MINOR_VERSION = new BigInteger("0");

    // Sedex Contentieux
    public static final String MESSAGE_LIST_GUARANTED_TYPE = "5222";
    public static final String MESSAGE_LIST_GUARANTED_SUBTYPE = "000201";
    public static final String MESSAGE_LIST_GUARANTED_SUBJECT = "list of guaranteed assumptions";
}
