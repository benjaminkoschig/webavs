package globaz.tucana.db.bouclement.access;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Fri May 05 15:07:58 CEST 2006
 */
public interface ITUNoPassageDefTable {
    /** csApplication - cs application tu_appli (CSAPPL) */
    public final String CS_APPLICATION = "CSAPPL";

    /** idBouclement - cl� primaire du fichier bouclement (BBOUID) */
    public final String ID_BOUCLEMENT = "BBOUID";

    /**
     * idNoPassage - repr�sentera la cl� primaire du fichier une fois g�n�r�e (BPNPID)
     */
    public final String ID_NO_PASSAGE = "BPNPID";

    /** noPassage - num�ro de passage (BPNPNP) */
    public final String NO_PASSAGE = "BPNPNP";

    /** Table : TUBPNP */
    public final String TABLE_NAME = "TUBPNP";
}
