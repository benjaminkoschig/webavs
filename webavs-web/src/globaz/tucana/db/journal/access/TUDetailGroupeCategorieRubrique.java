package globaz.tucana.db.journal.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.tucana.db.bouclement.access.ITUBouclementDefTable;
import globaz.tucana.db.bouclement.access.ITUDetailDefTable;
import globaz.tucana.db.parametrage.access.ITUCategorieRubriqueDefTable;
import globaz.tucana.db.parametrage.access.ITUGroupeCategorieDefTable;

/**
 * Classe représentée par les données de détail, catégorie-rubrique et groupe-catégorie
 * 
 * @author fgo date de création : 26 juin 06
 * @version : version 1.0
 * 
 */
public class TUDetailGroupeCategorieRubrique extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = new String();
    private String canton = new String();
    private String csAgence = new String();
    private String csCategorie = new String();
    private String csGroupeCategorie = new String();
    private String csOperation = new String();
    private String csRubrique = new String();
    private String csType = new String();
    private String dateImportation = new String();
    private String idCategorieRubrique = new String();
    private String idGroupeCategorie = new String();
    private String mois = new String();
    private String nombreMontant = new String();

    /**
     * Construteur
     */
    public TUDetailGroupeCategorieRubrique() {
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
        // champs de TUBouclement
        sqlFields.append("bou.").append(ITUBouclementDefTable.ANNEE_COMPTABLE).append(", ");
        sqlFields.append("bou.").append(ITUBouclementDefTable.MOIS_COMPTABLE).append(", ");
        sqlFields.append("bou.").append(ITUBouclementDefTable.CS_AGENCE).append(", ");
        // champs de TUDetail
        sqlFields.append("det.").append(ITUDetailDefTable.CANTON).append(", ");
        sqlFields.append("det.").append(ITUDetailDefTable.NOMBRE_MONTANT).append(", ");
        sqlFields.append("det.").append(ITUDetailDefTable.CS_RUBRIQUE).append(", ");
        sqlFields.append("det.").append(ITUDetailDefTable.DATE_IMPORT).append(", ");
        // champ de TUCategorieRubrique
        sqlFields.append("cat.").append(ITUCategorieRubriqueDefTable.ID_CATEGORIE_RUBRIQUE).append(", ");
        sqlFields.append("cat.").append(ITUCategorieRubriqueDefTable.CS_OPERATION).append(", ");
        // champ de TUGroupeCategorie
        sqlFields.append("grp.").append(ITUGroupeCategorieDefTable.ID_GROUPE_CATEGORIE).append(", ");
        sqlFields.append("grp.").append(ITUGroupeCategorieDefTable.CS_CATEGORIE).append(", ");
        sqlFields.append("grp.").append(ITUGroupeCategorieDefTable.CS_TYPE).append(", ");
        sqlFields.append("grp.").append(ITUGroupeCategorieDefTable.CS_GROUPE_RUBRIQUE);

        return sqlFields.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();
        sqlFrom.append(_getCollection()).append(ITUGroupeCategorieDefTable.TABLE_NAME).append(" AS grp");

        sqlFrom.append(" INNER JOIN ");
        sqlFrom.append(_getCollection()).append(ITUCategorieRubriqueDefTable.TABLE_NAME).append(" AS cat");
        sqlFrom.append(" ON cat.").append(ITUCategorieRubriqueDefTable.ID_GROUPE_CATEGORIE).append("=grp.")
                .append(ITUGroupeCategorieDefTable.ID_GROUPE_CATEGORIE);

        sqlFrom.append(" left outer JOIN ");
        sqlFrom.append(_getCollection()).append(ITUDetailDefTable.TABLE_NAME).append(" AS det");
        sqlFrom.append(" ON det.").append(ITUDetailDefTable.CS_RUBRIQUE).append("=cat.")
                .append(ITUCategorieRubriqueDefTable.CS_RUBRIQUE);

        sqlFrom.append(" left outer JOIN ");
        sqlFrom.append(_getCollection()).append(ITUBouclementDefTable.TABLE_NAME).append(" AS bou");
        sqlFrom.append(" ON det.").append(ITUDetailDefTable.ID_BOUCLEMENT).append("=bou.")
                .append(ITUBouclementDefTable.ID_BOUCLEMENT);

        return sqlFrom.toString();
    }

    /**
     * Renvoie le nom de la table TUBPNP
     * 
     * @return String TUBPNP
     */
    @Override
    protected String _getTableName() {
        return ITUDetailDefTable.TABLE_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        annee = statement.dbReadNumeric(ITUBouclementDefTable.ANNEE_COMPTABLE);
        mois = statement.dbReadNumeric(ITUBouclementDefTable.MOIS_COMPTABLE);
        canton = statement.dbReadString(ITUDetailDefTable.CANTON);
        nombreMontant = statement.dbReadString(ITUDetailDefTable.NOMBRE_MONTANT);
        dateImportation = statement.dbReadDateAMJ(ITUDetailDefTable.DATE_IMPORT);
        csRubrique = statement.dbReadNumeric(ITUDetailDefTable.CS_RUBRIQUE);
        csCategorie = statement.dbReadNumeric(ITUGroupeCategorieDefTable.CS_CATEGORIE);
        csAgence = statement.dbReadNumeric(ITUBouclementDefTable.CS_AGENCE);
        csGroupeCategorie = statement.dbReadNumeric(ITUGroupeCategorieDefTable.CS_GROUPE_RUBRIQUE);
        idCategorieRubrique = statement.dbReadNumeric(ITUCategorieRubriqueDefTable.ID_CATEGORIE_RUBRIQUE);
        csOperation = statement.dbReadNumeric(ITUCategorieRubriqueDefTable.CS_OPERATION);
        idGroupeCategorie = statement.dbReadNumeric(ITUGroupeCategorieDefTable.ID_GROUPE_CATEGORIE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    /**
     * Renvoie l'année de bouclement
     * 
     * @return
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * Renvoie le canton
     * 
     * @return
     */
    public String getCanton() {
        return canton;
    }

    /**
     * Renvoie le code système de l'agence
     * 
     * @return
     */
    public String getCsAgence() {
        return csAgence;
    }

    /**
     * Renvoie le code système de la catégorie
     * 
     * @return
     */
    public String getCsCategorie() {
        return csCategorie;
    }

    /**
     * Renvoie le code système groupe-catégorie
     * 
     * @return
     */
    public String getCsGroupeCategorie() {
        return csGroupeCategorie;
    }

    /**
     * Récurère le code système opération (+ / -)
     * 
     * @return
     */
    public String getCsOperation() {
        return csOperation;
    }

    /**
     * Renvoie l'id code système de la rubrique
     * 
     * @return
     */
    public String getCsRubrique() {
        return csRubrique;
    }

    /**
     * Renvoie le code système du type
     * 
     * @return
     */
    public String getCsType() {
        return csType;
    }

    /**
     * Renvoie la date d'importation
     * 
     * @return
     */
    public String getDateImportation() {
        return dateImportation;
    }

    /**
     * Renvoie l'id de la catégorie-rubrique
     * 
     * @return
     */
    public String getIdCategorieRubrique() {
        return idCategorieRubrique;
    }

    /**
     * Renvoie l'idGroupeCategorie
     * 
     * @return
     */
    public String getIdGroupeCategorie() {
        return idGroupeCategorie;
    }

    /**
     * Renvoie le mois de bouclement
     * 
     * @return
     */
    public String getMois() {
        return mois;
    }

    /**
     * Renvoie le nombre-montant
     * 
     * @return
     */
    public String getNombreMontant() {
        return nombreMontant;
    }

    /**
     * Modifie le code système opération (+ / -)
     * 
     * @param csOperation
     */
    public void setCsOperation(String csOperation) {
        this.csOperation = csOperation;
    }

    /**
     * Modifie le code système du type
     * 
     * @param string
     */
    public void setCsType(String string) {
        csType = string;
    }

}
