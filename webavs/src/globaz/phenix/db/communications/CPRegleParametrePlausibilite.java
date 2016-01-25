/*
 * Créé le 16 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.db.communications;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author mmu
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPRegleParametrePlausibilite extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean actifParametre = new Boolean(true);
    private Boolean actifRegle = new Boolean(false);
    private String canton = "";
    private String classpath = "";
    private String declenchement = "";
    private String descriptionParametre_de = "";
    private String descriptionParametre_fr = "";
    private String descriptionParametre_it = "";
    private String descriptionRegle_de = "";
    private String descriptionRegle_fr = "";
    private String descriptionRegle_it = "";
    private String idParametre = "";
    private String idPlausibilite = "";
    private String nomCle = "";
    private String prioriteParametre = "";
    private String prioriteRegle = "";
    private String type = "0"; // par défaut le type du parametre est 'pas de
    private String typeMessage = "";
    // parametre'
    private String valeur = "";

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdParametre(_incCounter(transaction, idParametre));

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        String table1 = "CPREGPLP";
        String table2 = "CPPARAP";
        return _getCollection() + table1 + " INNER JOIN " + _getCollection() + table2 + " ON (" + _getCollection()
                + table1 + ".IPIDPLAU=" + _getCollection() + table2 + ".IPIDPLAU)";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idPlausibilite = statement.dbReadString("IPIDPLAU");
        descriptionRegle_fr = statement.dbReadString("IPNOMFR");
        descriptionRegle_it = statement.dbReadString("IPNOMIT");
        descriptionRegle_de = statement.dbReadString("IPNOMDE");
        classpath = statement.dbReadString("IPCLASS");
        prioriteRegle = statement.dbReadNumeric("IPPRIO");
        actifRegle = statement.dbReadBoolean("IPACTIF");
        declenchement = statement.dbReadNumeric("IPDECL");
        canton = statement.dbReadNumeric("IPCANT");
        idParametre = statement.dbReadNumeric("IXIDPA");
        descriptionParametre_fr = statement.dbReadString("IXNOMDFR");
        descriptionParametre_it = statement.dbReadString("IXNOMDIT");
        descriptionParametre_de = statement.dbReadString("IXNOMDDE");
        nomCle = statement.dbReadString("IXMETH");
        type = statement.dbReadString("IXTYPE");
        valeur = statement.dbReadString("IXVAL");
        prioriteParametre = statement.dbReadString("IXPRIO");
        actifParametre = statement.dbReadBoolean("IXACTI");
        typeMessage = statement.dbReadNumeric("IXTMSG");
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
        statement.writeKey("IXIDPA", _dbWriteNumeric(statement.getTransaction(), getIdParametre(), ""));

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public Boolean getActifParametre() {
        return actifParametre;
    }

    public Boolean getActifRegle() {
        return actifRegle;
    }

    public String getCanton() {
        return canton;
    }

    public String getClasspath() {
        return classpath;
    }

    public String getDeclenchement() {
        return declenchement;
    }

    public String getDescriptionParametre_de() {
        return descriptionParametre_de;
    }

    public String getDescriptionParametre_fr() {
        return descriptionParametre_fr;
    }

    public String getDescriptionParametre_it() {
        return descriptionParametre_it;
    }

    public String getDescriptionRegle_de() {
        return descriptionRegle_de;
    }

    public String getDescriptionRegle_fr() {
        return descriptionRegle_fr;
    }

    public String getDescriptionRegle_it() {
        return descriptionRegle_it;
    }

    /**
     * @return
     */
    public String getIdParametre() {
        return idParametre;
    }

    /**
     * @return
     */
    public String getIdPlausibilite() {
        return idPlausibilite;
    }

    public String getLibelleTypeMessage() {
        try {
            return globaz.phenix.translation.CodeSystem.getLibelle(getSession(), getTypeMessage());
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @return
     */
    public String getNomCle() {
        return nomCle;
    }

    public String getPrioriteParametre() {
        return prioriteParametre;
    }

    public String getPrioriteRegle() {
        return prioriteRegle;
    }

    /**
     * @return
     */
    public String getType() {
        return type;
    }

    public String getTypeMessage() {
        return typeMessage;
    }

    /**
     * @return
     */
    public String getValeur() {
        return valeur;
    }

    public void setActifParametre(Boolean actifParametre) {
        this.actifParametre = actifParametre;
    }

    public void setActifRegle(Boolean actifRegle) {
        this.actifRegle = actifRegle;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public void setClasspath(String classpath) {
        this.classpath = classpath;
    }

    public void setDeclenchement(String declenchement) {
        this.declenchement = declenchement;
    }

    public void setDescriptionParametre_de(String descriptionParametreDe) {
        descriptionParametre_de = descriptionParametreDe;
    }

    public void setDescriptionParametre_fr(String descriptionParametreFr) {
        descriptionParametre_fr = descriptionParametreFr;
    }

    public void setDescriptionParametre_it(String descriptionParametreIt) {
        descriptionParametre_it = descriptionParametreIt;
    }

    public void setDescriptionRegle_de(String descriptionRegleDe) {
        descriptionRegle_de = descriptionRegleDe;
    }

    public void setDescriptionRegle_fr(String descriptionRegleFr) {
        descriptionRegle_fr = descriptionRegleFr;
    }

    public void setDescriptionRegle_it(String descriptionRegleIt) {
        descriptionRegle_it = descriptionRegleIt;
    }

    /**
     * @param string
     */
    public void setIdParametre(String string) {
        idParametre = string;
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
    public void setNomCle(String string) {
        nomCle = string;
    }

    public void setPrioriteParametre(String prioriteParametre) {
        this.prioriteParametre = prioriteParametre;
    }

    public void setPrioriteRegle(String prioriteRegle) {
        this.prioriteRegle = prioriteRegle;
    }

    /**
     * @param string
     */
    public void setType(String string) {
        type = string;
    }

    public void setTypeMessage(String typeMessage) {
        this.typeMessage = typeMessage;
    }

    /**
     * @param string
     */
    public void setValeur(String string) {
        valeur = string;
    }

}
