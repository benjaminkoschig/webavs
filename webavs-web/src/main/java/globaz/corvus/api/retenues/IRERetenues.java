/*
 * Créé le 17 juil. 07
 */
package globaz.corvus.api.retenues;

/**
 * @author HPE
 */
public interface IRERetenues {

    // Type de la retenue de paiement
    public final static String CS_GROUPE_TYPE_RETENUE = "RETYPRET";
    public final static String CS_TYPE_ADRESSE_PMT = "52830001";
    public final static String CS_TYPE_COMPTE_SPECIAL = "52830003";
    public final static String CS_TYPE_FACTURE_EXISTANTE = "52830005";
    public final static String CS_TYPE_FACTURE_FUTURE = "52830004";
    public final static String CS_TYPE_IMPOT_SOURCE = "52830002";

    public final static int ID_TYPE_ADRESSE_PMT = 52830001;
    public final static int ID_TYPE_COMPTE_SPECIAL = 52830003;
    public final static int ID_TYPE_FACTURE_EXISTANTE = 52830005;
    public final static int ID_TYPE_FACTURE_FUTURE = 52830004;
    public final static int ID_TYPE_IMPOT_SOURCE = 52830002;

}
