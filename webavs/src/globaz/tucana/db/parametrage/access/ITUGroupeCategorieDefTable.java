package globaz.tucana.db.parametrage.access;

/**
 * Interface pour table groupe cat�gorie
 * 
 * @author fgo date de cr�ation : 22 juin 06
 * @version : version 1.0
 * 
 */
public interface ITUGroupeCategorieDefTable {
    /** csCategorie - id code syst�me (cs : tu_categ) (CSCATEG) */
    public final String CS_CATEGORIE = "CSCATEG";

    /** csGroupeRubrique - id code syst�me (cs : tu_grrubr (CSGRRU) */
    public final String CS_GROUPE_RUBRIQUE = "CSGRRU";

    /** csType - code syst�me type (cs : tu_tygrp) (CSTYPE) */
    public final String CS_TYPE = "CSTYPE";

    /** idGroupeCategorie - cl� primaire du fichier TUBPGRC (BGRCID) */
    public final String ID_GROUPE_CATEGORIE = "BGRCID";
    /** Table : TUBPGRC */
    public final String TABLE_NAME = "TUBPGRC";

}
