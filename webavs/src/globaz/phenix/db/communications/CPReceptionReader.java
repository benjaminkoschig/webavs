/*
 * Créé le 21 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.db.communications;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.translation.CodeSystem;

/**
 * @author mmu Un reception reader permet gérer les modules qui lisent les communications reçues. Cette classe n'est
 *         étendue par aucun viewBean car les reader doivent être transparent à l'utilisateur, les données doivent donc
 *         être insérée manuellement
 */
public class CPReceptionReader extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String canton = "";
    private Boolean formatXml = new Boolean(false);
    private String idCanton = "";
    private String idCommunicationReader = "";
    private String nomClass = "";
    private String nomFichier = "";
    private String rechercheTiers = "";

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // TODO Auto-generated method stub
        setIdCommunicationReader(this._incCounter(transaction, "0"));
    }

    @Override
    protected String _getTableName() {
        return "CPRECRE";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idCommunicationReader = statement.dbReadNumeric("IDCOMREADER");
        idCanton = statement.dbReadNumeric("IDCANTON");
        formatXml = statement.dbReadBoolean("FORMATXML");
        nomClass = statement.dbReadString("NOMCLASSE");
        rechercheTiers = statement.dbReadString("RECTIERS");
        canton = statement.dbReadString("CANTON");
        nomFichier = statement.dbReadString("NOMFICHIER");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        if (JadeStringUtil.isBlank(getIdCommunicationReader())) {
            _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0009"));
        }
        // L'id du canton est obligatoire, il se trouve dans la table AJPPCOU
        // sous PYCANTON
        if (JadeStringUtil.isBlank(getIdCanton())) {
            _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0010"));
        }
        if (JadeStringUtil.isBlank(getNomClass())) {
            _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0011"));
        }
        // Recherche du code court
        setCanton(globaz.phenix.translation.CodeSystem.getCode(getSession(), getIdCanton()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("IDCOMREADER",
                this._dbWriteNumeric(statement.getTransaction(), getIdCommunicationReader(), "IdCommunicationReader"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("IDCOMREADER",
                this._dbWriteNumeric(statement.getTransaction(), getIdCommunicationReader(), "IdCommunicationReader"));
        statement.writeField("FORMATXML", this._dbWriteBoolean(statement.getTransaction(), getFormatXml(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "FormatXML"));
        statement.writeField("IDCANTON", this._dbWriteNumeric(statement.getTransaction(), getIdCanton(), "IdCanton"));
        statement.writeField("NOMCLASSE", this._dbWriteString(statement.getTransaction(), getNomClass(), "NomClasse"));
        statement.writeField("RECTIERS",
                this._dbWriteString(statement.getTransaction(), getRechercheTiers(), "rechercheTiers"));
        statement.writeField("CANTON", this._dbWriteString(statement.getTransaction(), getCanton(), "canton"));
        statement.writeField("NOMFICHIER",
                this._dbWriteString(statement.getTransaction(), getNomFichier(), "nomFichier"));
    }

    public String getCanton() {
        return canton;
    }

    /**
     * @return
     */
    public Boolean getFormatXml() {
        return formatXml;
    }

    /**
     * @return
     */
    public String getIdCanton() {
        return idCanton;
    }

    /**
     * @return
     */
    public String getIdCommunicationReader() {
        return idCommunicationReader;
    }

    /**
     * @return
     */
    public String getIsoCanton() {
        return getSession().getCode(getIdCanton());
    }

    public String getLibelleCanton() {
        try {
            return CodeSystem.getLibelle(getSession(), idCanton);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @return
     */
    public String getNomCanton() {
        return getSession().getCodeLibelle(getIdCanton());
    }

    /**
     * @return
     */
    public String getNomClass() {
        return nomClass;
    }

    public String getNomFichier() {
        return nomFichier;
    }

    public String getRechercheTiers() {
        return rechercheTiers;
    }

    public boolean isFormatXml() {
        return formatXml.booleanValue();
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    /**
     * @param boolean1
     */
    public void setFormatXml(Boolean boolean1) {
        formatXml = boolean1;
    }

    /**
     * @param string
     */
    public void setIdCanton(String string) {
        idCanton = string;
    }

    /**
     * @param string
     */
    public void setIdCommunicationReader(String string) {
        idCommunicationReader = string;
    }

    /**
     * @param string
     */
    public void setNomClass(String string) {
        nomClass = string;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    public void setRechercheTiers(String rechercheTiers) {
        this.rechercheTiers = rechercheTiers;
    }

}
