/*
 * Créé le 15 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.db.communications;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author mmu
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPReglePlausibilite extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_APRES_GENERATION = "617002";
    public final static String CS_AVANT_GENERATION = "617001";
    private Boolean actif = new Boolean(false);
    private String canton = "";
    private String classpath = "";
    private String declenchement = "";
    private String description_de = "";
    private String description_fr = "";
    private String description_it = "";
    private String idPlausibilite = "";
    private String priorite = "";

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdPlausibilite(_incCounter(transaction, idPlausibilite));

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "CPREGPLP";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idPlausibilite = statement.dbReadString("IPIDPLAU");
        description_fr = statement.dbReadString("IPNOMFR");
        description_it = statement.dbReadString("IPNOMIT");
        description_de = statement.dbReadString("IPNOMDE");
        classpath = statement.dbReadString("IPCLASS");
        priorite = statement.dbReadNumeric("IPPRIO");
        actif = statement.dbReadBoolean("IPACTIF");
        declenchement = statement.dbReadNumeric("IPDECL");
        canton = statement.dbReadNumeric("IPCANT");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        _propertyMandatory(statement.getTransaction(), getDescription_fr(), getSession().getLabel("CP_MSG_0001"));
        _propertyMandatory(statement.getTransaction(), getDescription_de(), getSession().getLabel("CP_MSG_0002"));
        _propertyMandatory(statement.getTransaction(), getDescription_it(), getSession().getLabel("CP_MSG_0003"));
        _propertyMandatory(statement.getTransaction(), getClasspath(), getSession().getLabel("CP_MSG_0004"));
        _propertyMandatory(statement.getTransaction(), getPriorite(), getSession().getLabel("CP_MSG_0005"));

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("IPIDPLAU", _dbWriteNumeric(statement.getTransaction(), getIdPlausibilite(), ""));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("IPIDPLAU",
                _dbWriteNumeric(statement.getTransaction(), getIdPlausibilite(), "IdPlausibilité"));
        statement.writeField("IPNOMFR", _dbWriteString(statement.getTransaction(), getDescription_fr(), "NomFR"));
        statement.writeField("IPNOMIT", _dbWriteString(statement.getTransaction(), getDescription_it(), "NomIT"));
        statement.writeField("IPNOMDE", _dbWriteString(statement.getTransaction(), getDescription_de(), "NomDE"));
        statement.writeField("IPCLASS", _dbWriteString(statement.getTransaction(), getClasspath(), "Class"));
        statement.writeField("IPPRIO", _dbWriteNumeric(statement.getTransaction(), getPriorite(), "Priorite"));
        statement.writeField("IPACTIF",
                _dbWriteBoolean(statement.getTransaction(), getActif(), BConstants.DB_TYPE_BOOLEAN_CHAR, "Actif"));
        statement
                .writeField("IPDECL", _dbWriteNumeric(statement.getTransaction(), getDeclenchement(), "Declenchement"));
        statement.writeField("IPCANT", _dbWriteNumeric(statement.getTransaction(), getCanton(), "canton"));
    }

    /**
     * @return
     */
    public Boolean getActif() {
        return actif;
    }

    public String getCanton() {
        return canton;
    }

    /**
     * @return
     */
    public String getClasspath() {
        return classpath;
    }

    public String getDeclenchement() {
        return declenchement;
    }

    /**
     * @return
     */
    public String getDescription_de() {
        return description_de;
    }

    /**
     * @return
     */
    public String getDescription_fr() {
        return description_fr;
    }

    /**
     * @return
     */
    public String getDescription_it() {
        return description_it;
    }

    /**
     * @return
     */
    public String getIdPlausibilite() {
        return idPlausibilite;
    }

    /**
     * @return
     */
    public String getPriorite() {
        return priorite;
    }

    public boolean isActif() {
        if (actif == null) {
            return true;
        }
        return actif.booleanValue();
    }

    /**
     * @param boolean1
     */
    public void setActif(Boolean boolean1) {
        actif = boolean1;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    /**
     * @param string
     */
    public void setClasspath(String string) {
        classpath = string;
    }

    public void setDeclenchement(String declenchement) {
        this.declenchement = declenchement;
    }

    /**
     * @param string
     */
    public void setDescription_de(String string) {
        description_de = string;
    }

    /**
     * @param string
     */
    public void setDescription_fr(String string) {
        description_fr = string;
    }

    /**
     * @param string
     */
    public void setDescription_it(String string) {
        description_it = string;
    }

    /**
     * @param string
     */
    public void setIdPlausibilite(String string) {
        idPlausibilite = string;
    }

    /**
     * @param string
     */
    public void setPriorite(String string) {
        priorite = string;
    }

}
