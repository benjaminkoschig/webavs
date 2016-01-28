package globaz.tucana.db.bouclement.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Wed May 03 13:47:48 CEST 2006
 */
public class TUBouclementDetail extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ALIAS_BOUCLEMENT = "BOU";
    public static final String ALIAS_DETAIL = "DET";
    /** Table : TUBPBOU */
    /** anneeComptable - ann�e comptable (BBOUAN) */
    private String anneeComptable = new String();
    /** canton - canton (BDETCA) */
    private String canton = new String();
    /** csAgence - code syst�me agence (CSAGEN) */
    private String csAgence = new String();
    /** csApplication - cs application tu_appli (CSAPPL) */
    private String csApplication = new String();
    /** csEtat - code syst�me etat (CSETAT) */
    private String csEtat = new String();
    /** csRubrique - cs rubrique tu_rubr (CSRUBR) */
    private String csRubrique = new String();
    /** csTypeRubrique - cs type de rubr tu_tyrubr (CSTYRU) */
    private String csTypeRubrique = new String();
    /** dateCreation - date de cr�ation (BBOUCR) */
    private String dateCreation = new String();
    /** dateEtat - date �tat (BBOUET) */
    private String dateEtat = new String();
    /** dateImport - date importation (BDETIM) */
    private String dateImport = new String();
    /** idBouclement - cl� primaire du fichier bouclement (BBOUID) */
    private String idBouclement = new String();
    /** idDetail - id d�tail (BDETID) */
    private String idDetail = new String();
    /** idImportation - id importation cl� �trang�re (BBOUIM) */
    private String idImportation = new String();
    /** moisComptable - mois comptable (BBOUMO) */
    private String moisComptable = new String();
    /** nombreMontant - nombre ou montant (BDETNM) */
    private String nombreMontant = new String();
    /** noPassage - num�ro de passage (BDETNP) */
    private String noPassage = new String();
    /** soldeBouclement - solde bouclement (BBOUSO) */
    private String soldeBouclement = new String();

    /**
     * Constructeur de la classe TUBouclement
     */
    public TUBouclementDetail() {
        super();
    }

    /**
     * M�thode qui incr�mente la cl� primaire
     * 
     * @param transaction
     *            BTransaction transaction
     * @throws Exception
     *             exception
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sb = new StringBuffer();
        sb.append(_getCollection()).append(ITUBouclementDefTable.TABLE_NAME).append(" ")
                .append(TUBouclementDetail.ALIAS_BOUCLEMENT);
        sb.append(" INNER JOIN ").append(_getCollection()).append(ITUDetailDefTable.TABLE_NAME).append(" ")
                .append(TUBouclementDetail.ALIAS_DETAIL);
        sb.append(" ON ").append(TUBouclementDetail.ALIAS_BOUCLEMENT).append(".")
                .append(ITUBouclementDefTable.ID_BOUCLEMENT).append("=").append(TUBouclementDetail.ALIAS_DETAIL)
                .append(".").append(ITUDetailDefTable.ID_BOUCLEMENT);
        return sb.toString();
    }

    /**
     * Renvoie le nom de la table TUBPBOU
     * 
     * @return String TUBPBOU
     */
    @Override
    protected String _getTableName() {
        return "";
    }

    /**
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la bdd
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     * @exception Exception
     *                si la lecture des propri�t�s �choue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        anneeComptable = statement.dbReadNumeric(ITUBouclementDefTable.ANNEE_COMPTABLE);
        dateCreation = statement.dbReadDateAMJ(ITUBouclementDefTable.DATE_CREATION);
        dateEtat = statement.dbReadDateAMJ(ITUBouclementDefTable.DATE_ETAT);
        idBouclement = statement.dbReadNumeric(ITUBouclementDefTable.ID_BOUCLEMENT);
        idImportation = statement.dbReadNumeric(ITUBouclementDefTable.ID_IMPORTATION);
        moisComptable = statement.dbReadNumeric(ITUBouclementDefTable.MOIS_COMPTABLE);
        soldeBouclement = statement.dbReadNumeric(ITUBouclementDefTable.SOLDE_BOUCLEMENT, 2);
        csAgence = statement.dbReadNumeric(ITUBouclementDefTable.CS_AGENCE);
        csEtat = statement.dbReadNumeric(ITUBouclementDefTable.CS_ETAT);
        // d�tail
        canton = statement.dbReadString(ITUDetailDefTable.CANTON);
        idDetail = statement.dbReadNumeric(ITUDetailDefTable.ID_DETAIL);
        dateImport = statement.dbReadDateAMJ(ITUDetailDefTable.DATE_IMPORT);
        nombreMontant = statement.dbReadNumeric(ITUDetailDefTable.NOMBRE_MONTANT);
        noPassage = statement.dbReadNumeric(ITUDetailDefTable.NO_PASSAGE);
        csApplication = statement.dbReadNumeric(ITUDetailDefTable.CS_APPLICATION);
        csRubrique = statement.dbReadNumeric(ITUDetailDefTable.CS_RUBRIQUE);
        csTypeRubrique = statement.dbReadNumeric(ITUDetailDefTable.CS_TYPE_RUBRIQUE);
    }

    /**
     * Valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     */
    @Override
    protected void _validate(BStatement statement) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeAlternateKey(globaz.globall.db.BStatement , int)
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        switch (alternateKey) {
            case 1:
                // Cl� altern�e num�ro 1 : idType et idValeur
                statement.writeKey(ITUBouclementDefTable.ANNEE_COMPTABLE,
                        _dbWriteNumeric(statement.getTransaction(), getAnneeComptable(), ""));
                statement.writeKey(ITUBouclementDefTable.MOIS_COMPTABLE,
                        _dbWriteNumeric(statement.getTransaction(), getMoisComptable(), ""));
                break;
            default:
                throw new Exception("Alternate key " + alternateKey + " not implemented");
        }
    }

    /**
     * Indique la cl� principale UBouclement() du fichier TUBPBOU
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     * @throws Exception
     *             si probl�me lors de l'�criture de la cl�
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    /**
     * Ecriture des propri�t�s
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     * @throws Exception
     *             si probl�me lors de l'�critrues des propri�t�s
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    /**
     * Renvoie la zone anneeComptable - ann�e comptable (BBOUAN)
     * 
     * @return String anneeComptable - ann�e comptable
     */
    public String getAnneeComptable() {
        return anneeComptable;
    }

    /**
     * Renvoie la zone canton - canton (BDETCA)
     * 
     * @return String canton - canton
     */
    public String getCanton() {
        return canton;
    }

    /**
     * Renvoie la zone csAgence - code syst�me agence (CSAGEN)
     * 
     * @return String csAgence - code syst�me agence
     */
    public String getCsAgence() {
        return csAgence;
    }

    /**
     * Renvoie la zone csApplication - cs application tu_appli (CSAPPL)
     * 
     * @return String csApplication - cs application tu_appli
     */
    public String getCsApplication() {
        return csApplication;
    }

    /**
     * Renvoie la zone csEtat - code syst�me etat (CSETAT)
     * 
     * @return String csEtat - code syst�me etat
     */
    public String getCsEtat() {
        return csEtat;
    }

    /**
     * Renvoie la zone csRubrique - cs rubrique tu_rubr (CSRUBR)
     * 
     * @return String csRubrique - cs rubrique tu_rubr
     */
    public String getCsRubrique() {
        return csRubrique;
    }

    /**
     * Renvoie la zone csTypeRubrique - cs type de rubr tu_tyrubr (CSTYRU)
     * 
     * @return String csTypeRubrique - cs type de rubr tu_tyrubr
     */
    public String getCsTypeRubrique() {
        return csTypeRubrique;
    }

    /**
     * Renvoie la zone dateCreation - date de cr�ation (BBOUCR)
     * 
     * @return String dateCreation - date de cr�ation
     */
    public String getDateCreation() {
        return dateCreation;
    }

    /**
     * Renvoie la zone dateEtat - date �tat (BBOUET)
     * 
     * @return String dateEtat - date �tat
     */
    public String getDateEtat() {
        return dateEtat;
    }

    /**
     * Renvoie la zone dateImport - date importation (BDETIM)
     * 
     * @return String dateImport - date importation
     */
    public String getDateImport() {
        return dateImport;
    }

    /**
     * Renvoie la zone idBouclement - cl� primaire du fichier bouclement (BBOUID)
     * 
     * @return String idBouclement - cl� primaire du fichier bouclement
     */
    public String getIdBouclement() {
        return idBouclement;
    }

    /**
     * Renvoie la zone idDetail - id d�tail (BDETID)
     * 
     * @return String idDetail - id d�tail
     */
    public String getIdDetail() {
        return idDetail;
    }

    /**
     * Renvoie la zone idImportation - id importation cl� �trang�re (BBOUIM)
     * 
     * @return String idImportation - id importation cl� �trang�re
     */
    public String getIdImportation() {
        return idImportation;
    }

    /**
     * Renvoie la zone moisComptable - mois comptable (BBOUMO)
     * 
     * @return String moisComptable - mois comptable
     */
    public String getMoisComptable() {
        return moisComptable;
    }

    /**
     * Renvoie la zone nombreMontant - nombre ou montant (BDETNM)
     * 
     * @return String nombreMontant - nombre ou montant
     */
    public String getNombreMontant() {
        return nombreMontant;
    }

    /**
     * Renvoie la zone noPassage - num�ro de passage (BDETNP)
     * 
     * @return String noPassage - num�ro de passage
     */
    public String getNoPassage() {
        return noPassage;
    }

    /**
     * Renvoie la zone soldeBouclement - solde bouclement (BBOUSO)
     * 
     * @return String soldeBouclement - solde bouclement
     */
    public String getSoldeBouclement() {
        return soldeBouclement;
    }

    /**
     * Modifie la zone anneeComptable - ann�e comptable (BBOUAN)
     * 
     * @param newAnneeComptable
     *            - ann�e comptable String
     */
    public void setAnneeComptable(String newAnneeComptable) {
        anneeComptable = newAnneeComptable;
    }

    /**
     * Modifie la zone canton - canton (BDETCA)
     * 
     * @param newCanton
     *            - canton String
     */
    public void setCanton(String newCanton) {
        canton = newCanton;
    }

    /**
     * Modifie la zone csAgence - code syst�me agence (CSAGEN)
     * 
     * @param newCsAgence
     *            - code syst�me agence String
     */
    public void setCsAgence(String newCsAgence) {
        csAgence = newCsAgence;
    }

    /**
     * Modifie la zone csApplication - cs application tu_appli (CSAPPL)
     * 
     * @param newCsAplication
     *            - cs application tu_appli String
     */
    public void setCsApplication(String newCsAplication) {
        csApplication = newCsAplication;
    }

    /**
     * Modifie la zone csEtat - code syst�me etat (CSETAT)
     * 
     * @param newCsEtat
     *            - code syst�me etat String
     */
    public void setCsEtat(String newCsEtat) {
        csEtat = newCsEtat;
    }

    /**
     * Modifie la zone csRubrique - cs rubrique tu_rubr (CSRUBR)
     * 
     * @param newCsRubrique
     *            - cs rubrique tu_rubr String
     */
    public void setCsRubrique(String newCsRubrique) {
        csRubrique = newCsRubrique;
    }

    /**
     * Modifie la zone csTypeRubrique - cs type de rubr tu_tyrubr (CSTYRU)
     * 
     * @param newCsTypeRubrique
     *            - cs type de rubr tu_tyrubr String
     */
    public void setCsTypeRubrique(String newCsTypeRubrique) {
        csTypeRubrique = newCsTypeRubrique;
    }

    /**
     * Modifie la zone dateCreation - date de cr�ation (BBOUCR)
     * 
     * @param newDateCreation
     *            - date de cr�ation String
     */
    public void setDateCreation(String newDateCreation) {
        dateCreation = newDateCreation;
    }

    /**
     * Modifie la zone dateEtat - date �tat (BBOUET)
     * 
     * @param newDateEtat
     *            - date �tat String
     */
    public void setDateEtat(String newDateEtat) {
        dateEtat = newDateEtat;
    }

    /**
     * Modifie la zone dateImport - date importation (BDETIM)
     * 
     * @param newDateImport
     *            - date importation String
     */
    public void setDateImport(String newDateImport) {
        dateImport = newDateImport;
    }

    /**
     * Modifie la zone idBouclement - cl� primaire du fichier bouclement (BBOUID)
     * 
     * @param newIdBouclement
     *            - cl� primaire du fichier bouclement String
     */
    public void setIdBouclement(String newIdBouclement) {
        idBouclement = newIdBouclement;
    }

    /**
     * Modifie la zone idDetail - id d�tail (BDETID)
     * 
     * @param newIdDetail
     *            - id d�tail String
     */
    public void setIdDetail(String newIdDetail) {
        idDetail = newIdDetail;
    }

    /**
     * Modifie la zone idImportation - id importation cl� �trang�re (BBOUIM)
     * 
     * @param newIdImportation
     *            - id importation cl� �trang�re String
     */
    public void setIdImportation(String newIdImportation) {
        idImportation = newIdImportation;
    }

    /**
     * Modifie la zone moisComptable - mois comptable (BBOUMO)
     * 
     * @param newMoisComptable
     *            - mois comptable String
     */
    public void setMoisComptable(String newMoisComptable) {
        moisComptable = newMoisComptable;
    }

    /**
     * Modifie la zone nombreMontant - nombre ou montant (BDETNM)
     * 
     * @param newNombreMontant
     *            - nombre ou montant String
     */
    public void setNombreMontant(String newNombreMontant) {
        nombreMontant = newNombreMontant;
    }

    /**
     * Modifie la zone noPassage - num�ro de passage (BDETNP)
     * 
     * @param newNoPassage
     *            - num�ro de passage String
     */
    public void setNoPassage(String newNoPassage) {
        noPassage = newNoPassage;
    }

    /**
     * Modifie la zone soldeBouclement - solde bouclement (BBOUSO)
     * 
     * @param newSoldeBouclement
     *            - solde bouclement String
     */
    public void setSoldeBouclement(String newSoldeBouclement) {
        soldeBouclement = newSoldeBouclement;
    }

}
