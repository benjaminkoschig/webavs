package globaz.tucana.db.bouclement.access;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Fri May 05 15:07:58 CEST 2006
 */
public interface ITUNoPassageDefTable {
    /** csApplication - cs application tu_appli (CSAPPL) */
    public final String CS_APPLICATION = "CSAPPL";

    /** idBouclement - clé primaire du fichier bouclement (BBOUID) */
    public final String ID_BOUCLEMENT = "BBOUID";

    /**
     * idNoPassage - représentera la clé primaire du fichier une fois générée (BPNPID)
     */
    public final String ID_NO_PASSAGE = "BPNPID";

    /** noPassage - numéro de passage (BPNPNP) */
    public final String NO_PASSAGE = "BPNPNP";

    /** Table : TUBPNP */
    public final String TABLE_NAME = "TUBPNP";
}
