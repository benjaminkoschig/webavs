package globaz.tucana.db.parametrage.access;

/**
 * Interface pour table catégorie rubrique
 * 
 * @author fgo date de création : 22 juin 06
 * @version : version 1.0
 * 
 */
public interface ITUCategorieRubriqueDefTable {
    /** csOperation - cs operation tu_oper (CSOPER) */
    public final String CS_OPERATION = "CSOPER";

    /** csRubrique - cs rubrique tu_rubr (CSRUBR) */
    public final String CS_RUBRIQUE = "CSRUBR";

    /** idCategorieRubrique - clé primaire du fichier tubpcru (BCRUID) */
    public final String ID_CATEGORIE_RUBRIQUE = "BCRUID";

    /** idGroupeRubrique - clé primaire du fichier groupe de rubrique (BGRCID) */
    public final String ID_GROUPE_CATEGORIE = "BGRCID";

    /** Table : TUBPCRU */
    public final String TABLE_NAME = "TUBPCRU";

}
