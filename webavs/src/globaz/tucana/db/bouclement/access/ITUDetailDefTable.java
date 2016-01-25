package globaz.tucana.db.bouclement.access;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Fri May 12 07:35:56 CEST 2006
 */
public interface ITUDetailDefTable {
    /** canton - canton (BDETCA) */
    public final String CANTON = "BDETCA";

    /** csAplication - cs application tu_appli (CSAPPL) */
    public final String CS_APPLICATION = "CSAPPL";

    /** csRubrique - cs rubrique tu_rubr (CSRUBR) */
    public final String CS_RUBRIQUE = "CSRUBR";

    /** csTypeRubrique - cs type de rubr tu_tyrubr (CSTYRU) */
    public final String CS_TYPE_RUBRIQUE = "CSTYRU";

    /** dateImport - date importation (BDETIM) */
    public final String DATE_IMPORT = "BDETIM";

    /** idBouclement - clé primaire du fichier bouclement (BBOUID) */
    public final String ID_BOUCLEMENT = "BBOUID";

    /** idDetail - id détail (BDETID) */
    public final String ID_DETAIL = "BDETID";

    /** noPassage - numéro de passage (BDETNP) */
    public final String NO_PASSAGE = "BDETNP";

    /** nombreMontant - nombre ou montant (BDETNM) */
    public final String NOMBRE_MONTANT = "BDETNM";

    /** Table : TUBPDET */
    public final String TABLE_NAME = "TUBPDET";
}
