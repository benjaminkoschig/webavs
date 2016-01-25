/*
 * Créé le 16 août 05
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
public class CPParametrePlausibilite extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_MSG_AVERTISSEMENT = "614002";
    // niveauErreur
    public final static String CS_MSG_ERREUR = "614001";
    public final static String CS_MSG_ERREUR_CRITIQUE = "614003";
    public static final String TYPE_CODE_SYSTEM = "4";
    public static final String TYPE_DATE = "3";
    public static final String TYPE_NUMERIC = "1";
    public static final String TYPE_STRING = "2";
    // Types que peut avoir le parametre
    public static final String TYPE_VIDE = "0";
    private Boolean actif = new Boolean(true);
    private String description_de = "";
    private String description_fr = "";

    private String description_it = "";
    private String idParametre = "";
    private String idPlausibilite = "";
    private String nomCle = "";

    private String priorite = "";
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
        return "CPPARAP";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idParametre = statement.dbReadNumeric("IXIDPA");
        idPlausibilite = statement.dbReadNumeric("IPIDPLAU");
        description_fr = statement.dbReadString("IXNOMDFR");
        description_it = statement.dbReadString("IXNOMDIT");
        description_de = statement.dbReadString("IXNOMDDE");
        nomCle = statement.dbReadString("IXMETH");
        type = statement.dbReadString("IXTYPE");
        valeur = statement.dbReadString("IXVAL");
        priorite = statement.dbReadString("IXPRIO");
        actif = statement.dbReadBoolean("IXACTI");
        typeMessage = statement.dbReadNumeric("IXTMSG");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        _propertyMandatory(statement.getTransaction(), getDescription_fr(), getSession().getLabel("CP_MSG_0017"));
        _propertyMandatory(statement.getTransaction(), getDescription_de(), getSession().getLabel("CP_MSG_0018"));
        _propertyMandatory(statement.getTransaction(), getDescription_it(), getSession().getLabel("CP_MSG_0019"));
        _propertyMandatory(statement.getTransaction(), getNomCle(), getSession().getLabel("CP_MSG_0020"));
        _propertyMandatory(statement.getTransaction(), getPriorite(), getSession().getLabel("CP_MSG_0021"));

        /*
         * if (getType().equals(TYPE_VIDE)) { // la valeur de parametre doit être nulle if
         * (!JadeStringUtil.isEmpty(getValeur())) { _addError(statement.getTransaction(),
         * "La valeur du paramètre doit être nulle si le type est nul"); } } else if (getType().equals(TYPE_NUMERIC)){
         * if (JadeStringUtil.isIntegerEmpty(getValeur()) && !getValeur().equals("0")) {
         * _addError(statement.getTransaction(), "La valeur du paramètre doit être un nombre"); } } else if
         * (getType().equals(TYPE_STRING)){ if (JadeStringUtil.isEmpty(getValeur()) && !getValeur().equals("")) {
         * _addError(statement.getTransaction(), "La valeur du paramètre doit être une chaîne de caracter"); } } else if
         * (getType().equals(TYPE_DATE)){ if (JAUtil.isDateEmpty(getValeur()) ) { _addError(statement.getTransaction(),
         * "La valeur du paramêtre doit être une date au format... "); } }
         */
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
        statement.writeField("IXIDPA", _dbWriteNumeric(statement.getTransaction(), getIdParametre(), "IdParametre"));
        statement.writeField("IPIDPLAU",
                _dbWriteNumeric(statement.getTransaction(), getIdPlausibilite(), "IdPlausibilité"));
        statement.writeField("IXNOMDFR",
                _dbWriteString(statement.getTransaction(), getDescription_fr(), "DescriptionFR"));
        statement.writeField("IXNOMDIT",
                _dbWriteString(statement.getTransaction(), getDescription_it(), "DescriptionIT"));
        statement.writeField("IXNOMDDE",
                _dbWriteString(statement.getTransaction(), getDescription_de(), "DescriptionDE"));
        statement.writeField("IXMETH", _dbWriteString(statement.getTransaction(), getNomCle(), "MonMethode"));
        statement.writeField("IXTYPE", _dbWriteString(statement.getTransaction(), getType(), "Type"));
        statement.writeField("IXVAL", _dbWriteString(statement.getTransaction(), getValeur(), "Valeur"));
        statement.writeField("IXPRIO", _dbWriteNumeric(statement.getTransaction(), getPriorite(), "Priorite"));
        statement.writeField("IXACTI",
                _dbWriteBoolean(statement.getTransaction(), getActif(), BConstants.DB_TYPE_BOOLEAN_CHAR, "Actif"));
        statement.writeField("IXTMSG", _dbWriteNumeric(statement.getTransaction(), getTypeMessage(), "typeMessage"));
    }

    /**
     * @return
     */
    public Boolean getActif() {
        return actif;
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

    /**
     * @return
     */
    public String getPriorite() {
        return priorite;
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

    public boolean isActif() {
        return actif.booleanValue();
    }

    /**
     * @param boolean1
     */
    public void setActif(Boolean boolean1) {
        actif = boolean1;
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

    /**
     * @param string
     */
    public void setPriorite(String string) {
        priorite = string;
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
