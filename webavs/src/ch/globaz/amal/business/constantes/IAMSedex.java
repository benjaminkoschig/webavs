package ch.globaz.amal.business.constantes;

import java.math.BigInteger;

public interface IAMSedex {
    public static final String APPLICATION_TYPE_MANUFACTURER = "GLOBAZ SA";
    public static final String APPLICATION_TYPE_PRODUCT_NAME = "Web@AVS";
    public static final String APPLICATION_TYPE_PRODUCT_VERSION = "1-13-00";
    public static final String MESSAGE_ACTION_DEMANDE = "5";
    public static final String MESSAGE_ACTION_NOUVEAU = "1";
    public static final String MESSAGE_ACTION_REPONSE = "6";
    public static final BigInteger MESSAGE_MINOR_VERSION = new BigInteger("0");
}
