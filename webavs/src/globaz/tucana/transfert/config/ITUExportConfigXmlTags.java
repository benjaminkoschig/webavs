package globaz.tucana.transfert.config;

/**
 * Interface faisant le lien entre le fichier de configuration de l'exportation et les classes
 * 
 * @author fgo
 * 
 */
public interface ITUExportConfigXmlTags {

    public static final String ATTRIBUTE_FK = "fk";

    public static final String ATTRIBUTE_PK = "pk";

    public static final String NODE_DETAIL = "globaz.tucana.db.bouclement.access.TUDetail";

    public static final String NODE_ROOT = "root";

    public static final String NODE_TUBOUCLEMENT = "globaz.tucana.db.bouclement.access.TUBouclement";

    public static final String NODE_TUNOPASSAGE = "globaz.tucana.db.bouclement.access.TUNOPassage";

}
