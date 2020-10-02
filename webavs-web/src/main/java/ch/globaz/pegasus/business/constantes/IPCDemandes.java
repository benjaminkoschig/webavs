package ch.globaz.pegasus.business.constantes;

/*
 * Créé le 11 novembre 2009
 */

/**
 * @author bsc
 */
public interface IPCDemandes {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public final static String CS_COURIER_LIBRE = "64002004";
    public final static String CS_DEMANDE_RFM = "64002005";
    public final static String CS_EN_ATTENTE_CALCUL = "64001002";
    public final static String CS_EN_ATTENTE_JUSTIFICATIFS = "64001001";
    // ETAT DEMANDE
    public static final String CS_ETAT_DEMANDE = "PCETATDEM";
    public final static String CS_FORM_OFFI_ATTEST = "64002001";
    public final static String CS_FORM_OFFI_CHANG_DOMI = "64002003";
    public final static String CS_FORM_OFFI_NON_ATTEST = "64002002";

    public final static String CS_OCTROYE = "64001003";
    public final static String CS_REOUVERT = "64001009";
        public final static String CS_REFUSE = "64001007";
    public final static String CS_RENONCE = "64001006";
    public final static String CS_REVISION = "64001008";
    public final static String CS_SUPPRIME = "64001004";
    public final static String CS_TRANSFERE = "64001005";
    public final static String CS_ANNULE = "64001010";
    // TYPE DEMANDE
    public static final String CS_TYPE_DEMANDE = "PCTYPEDEM";

}
