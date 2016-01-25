package globaz.tucana.db.bouclement;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BStatement;
import globaz.tucana.db.bouclement.access.ITUBouclementDefTable;
import globaz.tucana.db.bouclement.access.ITUNoPassageDefTable;
import globaz.tucana.db.bouclement.access.TUBouclement;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Wed May 03 13:47:49 CEST 2006
 */
public class TUApplicationBouclementViewBean extends TUBouclement implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** anneeComptable - année comptable (BBOUAN) */
    private String anneeComptable = new String();
    /** csAgence - code système agence (CSAGEN) */
    private String csAgence = new String();
    /** csApplication - cs application tu_appli (CSAPPL) */
    private String csApplication = new String();
    /** csEtat - code système etat (CSETAT) */
    private String csEtat = new String();
    /** dateCreation - date de création (BBOUCR) */
    private String dateCreation = new String();
    /** dateEtat - date état (BBOUET) */
    private String dateEtat = new String();
    /** idBouclement - clé primaire du fichier bouclement (BBOUID) */
    private String idBouclement = new String();
    /** idImportation - id importation clé étrangère (BBOUIM) */
    private String idImportation = new String();
    /**
     * idNoPassage - représentera la clé primaire du fichier une fois générée (BPNPID)
     */
    private String idNoPassage = new String();
    /** moisComptable - mois comptable (BBOUMO) */
    private String moisComptable = new String();
    /** noPassage - numéro de passage (BPNPNP) */
    private String noPassage = new String();
    /** soldeBouclement - solde bouclement (BBOUSO) */
    private String soldeBouclement = new String();

    /**
     * Constructeur de la classe TUNoPassage
     */
    public TUApplicationBouclementViewBean() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer sqlFields = new StringBuffer();
        sqlFields.append(ITUNoPassageDefTable.ID_NO_PASSAGE).append(", ");
        sqlFields.append(ITUNoPassageDefTable.NO_PASSAGE).append(", ");
        sqlFields.append(ITUNoPassageDefTable.CS_APPLICATION).append(", ");
        sqlFields.append(ITUBouclementDefTable.ANNEE_COMPTABLE).append(", ");
        sqlFields.append(ITUBouclementDefTable.DATE_CREATION).append(", ");
        sqlFields.append(ITUBouclementDefTable.DATE_ETAT).append(", ");
        sqlFields.append(ITUBouclementDefTable.ID_BOUCLEMENT).append(", ");
        sqlFields.append(ITUBouclementDefTable.ID_IMPORTATION).append(", ");
        sqlFields.append(ITUBouclementDefTable.MOIS_COMPTABLE).append(", ");
        sqlFields.append(ITUBouclementDefTable.SOLDE_BOUCLEMENT).append(", ");
        sqlFields.append(ITUBouclementDefTable.CS_AGENCE).append(", ");
        sqlFields.append(ITUBouclementDefTable.CS_ETAT);
        return sqlFields.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection()).append(ITUNoPassageDefTable.TABLE_NAME).append(" AS pca");

        // jointure sur bouclement
        sqlFrom.append(" INNER JOIN ");
        sqlFrom.append(_getCollection()).append(ITUBouclementDefTable.TABLE_NAME).append(" AS bou");
        sqlFrom.append(" ON bou.").append(ITUBouclementDefTable.ID_BOUCLEMENT).append("=pca.")
                .append(ITUNoPassageDefTable.ID_BOUCLEMENT);

        return sqlFrom.toString();
    }

    /**
     * Renvoie le nom de la table TUBPNP
     * 
     * @return String TUBPNP
     */
    @Override
    protected String _getTableName() {
        return ITUNoPassageDefTable.TABLE_NAME;
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @param statement
     *            L'objet d'accès à la base
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idNoPassage = statement.dbReadNumeric(ITUNoPassageDefTable.ID_NO_PASSAGE);
        noPassage = statement.dbReadNumeric(ITUNoPassageDefTable.NO_PASSAGE);
        csApplication = statement.dbReadNumeric(ITUNoPassageDefTable.CS_APPLICATION);
        anneeComptable = statement.dbReadNumeric(ITUBouclementDefTable.ANNEE_COMPTABLE);
        dateCreation = statement.dbReadDateAMJ(ITUBouclementDefTable.DATE_CREATION);
        dateEtat = statement.dbReadDateAMJ(ITUBouclementDefTable.DATE_ETAT);
        idBouclement = statement.dbReadNumeric(ITUBouclementDefTable.ID_BOUCLEMENT);
        idImportation = statement.dbReadNumeric(ITUBouclementDefTable.ID_IMPORTATION);
        moisComptable = statement.dbReadNumeric(ITUBouclementDefTable.MOIS_COMPTABLE);
        soldeBouclement = statement.dbReadNumeric(ITUBouclementDefTable.SOLDE_BOUCLEMENT, 2);
        csAgence = statement.dbReadNumeric(ITUBouclementDefTable.CS_AGENCE);
        csEtat = statement.dbReadNumeric(ITUBouclementDefTable.CS_ETAT);
    }

    /**
     * Valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) {
    }

    /**
     * Renvoie la zone anneeComptable - année comptable (BBOUAN)
     * 
     * @return String anneeComptable - année comptable
     */
    @Override
    public String getAnneeComptable() {
        return anneeComptable;
    }

    /**
     * Renvoie la zone csAgence - code système agence (CSAGEN)
     * 
     * @return String csAgence - code système agence
     */
    @Override
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
     * Renvoie la zone csEtat - code système etat (CSETAT)
     * 
     * @return String csEtat - code système etat
     */
    @Override
    public String getCsEtat() {
        return csEtat;
    }

    /**
     * Renvoie la zone dateCreation - date de création (BBOUCR)
     * 
     * @return String dateCreation - date de création
     */
    @Override
    public String getDateCreation() {
        return dateCreation;
    }

    /**
     * Renvoie la zone dateEtat - date état (BBOUET)
     * 
     * @return String dateEtat - date état
     */
    @Override
    public String getDateEtat() {
        return dateEtat;
    }

    @Override
    public String getIdBouclement() {
        return idBouclement;
    }

    /**
     * Renvoie la zone idImportation - id importation clé étrangère (BBOUIM)
     * 
     * @return String idImportation - id importation clé étrangère
     */
    @Override
    public String getIdImportation() {
        return idImportation;
    }

    /**
     * Renvoie la zone idNoPassage - représentera la clé primaire du fichier une fois générée (BPNPID)
     * 
     * @return String idNoPassage - représentera la clé primaire du fichier une fois générée
     */
    public String getIdNoPassage() {
        return idNoPassage;
    }

    /**
     * Renvoie la zone moisComptable - mois comptable (BBOUMO)
     * 
     * @return String moisComptable - mois comptable
     */
    @Override
    public String getMoisComptable() {
        return moisComptable;
    }

    /**
     * Renvoie la zone noPassage - numéro de passage (BPNPNP)
     * 
     * @return String noPassage - numéro de passage
     */
    public String getNoPassage() {
        return noPassage;
    }

    /**
     * Renvoie la zone soldeBouclement - solde bouclement (BBOUSO)
     * 
     * @return String soldeBouclement - solde bouclement
     */
    @Override
    public String getSoldeBouclement() {
        return soldeBouclement;
    }

    /**
     * Modifie la zone anneeComptable - année comptable (BBOUAN)
     * 
     * @param newAnneeComptable
     *            - année comptable String
     */
    @Override
    public void setAnneeComptable(String newAnneeComptable) {
        anneeComptable = newAnneeComptable;
    }

    /**
     * Modifie la zone csAgence - code système agence (CSAGEN)
     * 
     * @param newCsAgence
     *            - code système agence String
     */
    @Override
    public void setCsAgence(String newCsAgence) {
        csAgence = newCsAgence;
    }

    /**
     * Modifie la zone csApplication - cs application tu_appli (CSAPPL)
     * 
     * @param newCsApplication
     *            - cs application tu_appli String
     */
    public void setCsApplication(String newCsApplication) {
        csApplication = newCsApplication;
    }

    /**
     * Modifie la zone csEtat - code système etat (CSETAT)
     * 
     * @param newCsEtat
     *            - code système etat String
     */
    @Override
    public void setCsEtat(String newCsEtat) {
        csEtat = newCsEtat;
    }

    /**
     * Modifie la zone dateCreation - date de création (BBOUCR)
     * 
     * @param newDateCreation
     *            - date de création String
     */
    @Override
    public void setDateCreation(String newDateCreation) {
        dateCreation = newDateCreation;
    }

    /**
     * Modifie la zone dateEtat - date état (BBOUET)
     * 
     * @param newDateEtat
     *            - date état String
     */
    @Override
    public void setDateEtat(String newDateEtat) {
        dateEtat = newDateEtat;
    }

    /**
     * Modifie la zone idBouclement - clé primaire du fichier bouclement (BBOUID)
     * 
     * @param newIdBouclement
     *            - clé primaire du fichier bouclement String
     */
    @Override
    public void setIdBouclement(String newIdBouclement) {
        idBouclement = newIdBouclement;
    }

    /**
     * Modifie la zone idImportation - id importation clé étrangère (BBOUIM)
     * 
     * @param newIdImportation
     *            - id importation clé étrangère String
     */
    @Override
    public void setIdImportation(String newIdImportation) {
        idImportation = newIdImportation;
    }

    /**
     * Modifie la zone idNoPassage - représentera la clé primaire du fichier une fois générée (BPNPID)
     * 
     * @param newIdNoPassage
     *            - représentera la clé primaire du fichier une fois générée String
     */
    public void setIdNoPassage(String newIdNoPassage) {
        idNoPassage = newIdNoPassage;
    }

    /**
     * Modifie la zone moisComptable - mois comptable (BBOUMO)
     * 
     * @param newMoisComptable
     *            - mois comptable String
     */
    @Override
    public void setMoisComptable(String newMoisComptable) {
        moisComptable = newMoisComptable;
    }

    /**
     * Modifie la zone noPassage - numéro de passage (BPNPNP)
     * 
     * @param newNoPassage
     *            - numéro de passage String
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
    @Override
    public void setSoldeBouclement(String newSoldeBouclement) {
        soldeBouclement = newSoldeBouclement;
    }

}
