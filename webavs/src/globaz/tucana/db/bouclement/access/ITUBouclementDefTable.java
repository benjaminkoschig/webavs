package globaz.tucana.db.bouclement.access;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Wed May 03 13:47:48 CEST 2006
 */
public interface ITUBouclementDefTable {
    /** anneeComptable - ann�e comptable (BBOUAN) */
    public final String ANNEE_COMPTABLE = "BBOUAN";

    /** csAgence - code syst�me agence (CSAGEN) */
    public final String CS_AGENCE = "CSAGEN";

    /** csEtat - code syst�me etat (CSETAT) */
    public final String CS_ETAT = "CSETAT";

    /** dateCreation - date de cr�ation (BBOUCR) */
    public final String DATE_CREATION = "BBOUCR";

    /** dateEtat - date �tat (BBOUET) */
    public final String DATE_ETAT = "BBOUET";

    /** idBouclement - cl� primaire du fichier bouclement (BBOUID) */
    public final String ID_BOUCLEMENT = "BBOUID";

    /** idImportation - id importation cl� �trang�re (BBOUIM) */
    public final String ID_IMPORTATION = "BBOUIM";

    /** moisComptable - mois comptable (BBOUMO) */
    public final String MOIS_COMPTABLE = "BBOUMO";

    /** soldeBouclement - solde bouclement (BBOUSO) */
    public final String SOLDE_BOUCLEMENT = "BBOUSO";

    /** Table : TUBPBOU */
    public final String TABLE_NAME = "TUBPBOU";
}
