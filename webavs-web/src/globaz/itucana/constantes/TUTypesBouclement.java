package globaz.itucana.constantes;

/**
 * Classe contenant les contantes des types de bouclement
 * 
 * @author fgo date de création : 13 juin 06
 * @version : version 1.0
 * 
 */
public class TUTypesBouclement {
    public static TUTypesBouclement BOUCLEMENT_ACM = new TUTypesBouclement("940011");
    public static TUTypesBouclement BOUCLEMENT_AF = new TUTypesBouclement("940012");
    public static TUTypesBouclement BOUCLEMENT_CA = new TUTypesBouclement("940009");
    public static TUTypesBouclement BOUCLEMENT_CG = new TUTypesBouclement("940010");

    private String typeBouclement = null;

    /**
     * Constructeur de la classe. Un type de bouclement doit être passé en paramètre
     * 
     * @param _typeBouclement
     */
    private TUTypesBouclement(String _typeBouclement) {
        super();
        typeBouclement = _typeBouclement;
    }

    /**
     * Récupère le type de bouclement
     * 
     * @return
     */
    public String getTypeBouclement() {
        return typeBouclement;
    }
}
