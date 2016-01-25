package globaz.tucana.db.parametrage.access;

/**
 * Interface pour table groupe catégorie
 * 
 * @author fgo date de création : 22 juin 06
 * @version : version 1.0
 * 
 */
public interface ITUGroupeCategorieDefTable {
    /** csCategorie - id code système (cs : tu_categ) (CSCATEG) */
    public final String CS_CATEGORIE = "CSCATEG";

    /** csGroupeRubrique - id code système (cs : tu_grrubr (CSGRRU) */
    public final String CS_GROUPE_RUBRIQUE = "CSGRRU";

    /** csType - code système type (cs : tu_tygrp) (CSTYPE) */
    public final String CS_TYPE = "CSTYPE";

    /** idGroupeCategorie - clé primaire du fichier TUBPGRC (BGRCID) */
    public final String ID_GROUPE_CATEGORIE = "BGRCID";
    /** Table : TUBPGRC */
    public final String TABLE_NAME = "TUBPGRC";

}
