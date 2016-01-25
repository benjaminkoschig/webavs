package globaz.musca.db.facturation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import java.io.Serializable;

public class FAOrdreRegroupement extends BEntity implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String CAISSE_PROF = "509028";
    public final static java.lang.String TABLE_FIELDS = "FAORDIP.EHIDOR, FAORDIP.EHNORD, FAORDIP.EHLLIF, FAORDIP.EHLLID, "
            + "FAORDIP.EHLLII, FAORDIP.EHIDCA, FAORDIP.EHNCAI, FAORDIP.PSPY, FAORDIP.NATURE ";
    private String idOrdreRegroupement = new String();
    private String idTiersCaisse = new String();
    private String libelleDE = new String();
    private String libelleFR = new String();
    private String libelleIT = new String();
    private String likeOrdreRegroupement = new String();
    private String nature = new String();

    private String numCaisse = new String();
    private String ordreRegroupement = new String();

    /**
     * Commentaire relatif au constructeur FARemarque
     */
    public FAOrdreRegroupement() {
        super();
    }

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdOrdreRegroupement(this._incCounter(transaction, "0"));
    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        /**
         * Interdire la suppression d'un ordre si celui-ci est attribué
         * 
         */
        FAOrdreAttribuerManager mgrOrdreAttribuer = new FAOrdreAttribuerManager();
        mgrOrdreAttribuer.setSession(getSession());
        mgrOrdreAttribuer.setForIdOrdreRegroupement(getIdOrdreRegroupement());
        if (mgrOrdreAttribuer.getCount() >= 1) {
            throw new Exception(getSession().getLabel("PAS_DELETE_SI_ATTRIBUTION"));
        }
    }

    @Override
    protected String _getFields(BStatement statement) {
        return FAOrdreRegroupement.TABLE_FIELDS;
    }

    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "FAORDIP AS FAORDIP";
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "FAORDIP";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        if (getLikeOrdreRegroupement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAORDIP.EHNORD >=" + getLikeOrdreRegroupement();
        }

        return sqlWhere;
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idOrdreRegroupement = statement.dbReadNumeric("EHIDOR");
        ordreRegroupement = statement.dbReadNumeric("EHNORD");
        libelleFR = statement.dbReadString("EHLLIF");
        libelleDE = statement.dbReadString("EHLLID");
        libelleIT = statement.dbReadString("EHLLII");
        idTiersCaisse = statement.dbReadNumeric("EHIDCA");
        numCaisse = statement.dbReadNumeric("EHNCAI");
        nature = statement.dbReadNumeric("NATURE");

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        if (!JadeStringUtil.isBlank(getNumCaisse())) {
            TIAdministrationManager adminManager = new TIAdministrationManager();
            adminManager.setSession(getSession());
            adminManager.setForCodeAdministration(getNumCaisse());
            adminManager.setForGenreAdministration(FAOrdreRegroupement.CAISSE_PROF);
            try {
                adminManager.find();
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
            if (adminManager.size() == 0) {
                _addError(statement.getTransaction(), "Erreur : Ce numéro de caisse n'existe pas !");
            }
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("EHIDOR", this._dbWriteNumeric(statement.getTransaction(), getIdOrdreRegroupement(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("EHIDOR",
                this._dbWriteNumeric(statement.getTransaction(), getIdOrdreRegroupement(), "idOrdreRegroupement"));
        statement.writeField("EHNORD",
                this._dbWriteNumeric(statement.getTransaction(), getOrdreRegroupement(), "odreRegroupement"));
        statement.writeField("EHLLIF", this._dbWriteString(statement.getTransaction(), getLibelleFR(), "libelleFR"));
        statement.writeField("EHLLID", this._dbWriteString(statement.getTransaction(), getLibelleDE(), "libelleDE"));
        statement.writeField("EHLLII", this._dbWriteString(statement.getTransaction(), getLibelleIT(), "libelleIT"));
        statement.writeField("EHIDCA",
                this._dbWriteNumeric(statement.getTransaction(), getIdTiersCaisse(), "idTiersCaisse"));
        statement.writeField("EHNCAI", this._dbWriteNumeric(statement.getTransaction(), getNumCaisse(), "numCaisse"));
        statement.writeField("NATURE", this._dbWriteNumeric(statement.getTransaction(), getNature(), "nature"));
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

    /**
     * Getter
     */

    /**
     * @return
     */
    public String getIdOrdreRegroupement() {
        return idOrdreRegroupement;
    }

    /**
     * @return
     */
    public String getIdTiersCaisse() {
        return idTiersCaisse;
    }

    public java.lang.String getLibelle() {
        String langue = new String();
        langue = getSession().getIdLangueISO();
        if (langue.equalsIgnoreCase("fr")) {
            return libelleFR;
        }
        if (langue.equalsIgnoreCase("de")) {
            return libelleDE;
        } else {
            return libelleIT;
        }
    }

    /**
     * @return
     */
    public String getLibelleDE() {
        return libelleDE;
    }

    /**
     * @return
     */
    public String getLibelleFR() {
        return libelleFR;
    }

    /**
     * @return
     */
    public String getLibelleIT() {
        return libelleIT;
    }

    /**
     * @return
     */
    public String getLikeOrdreRegroupement() {
        return likeOrdreRegroupement;
    }

    public String getNature() {
        return nature;
    }

    public String getNumCaisse() {
        return numCaisse;
    }

    /**
     * @return
     */
    public String getOrdreRegroupement() {
        return ordreRegroupement;
    }

    /**
     * @param string
     */
    public void setIdOrdreRegroupement(String string) {
        idOrdreRegroupement = string;
    }

    /**
     * @param string
     */
    public void setIdTiersCaisse(String string) {
        if (!string.equals(null) && !string.equals("")) {
            TIAdministrationManager adminManager = new TIAdministrationManager();
            adminManager.setSession(getSession());
            adminManager.setForCodeAdministration(string);
            adminManager.setForGenreAdministration(FAOrdreRegroupement.CAISSE_PROF);
            try {
                adminManager.find();
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
            if (adminManager.size() > 0) {
                idTiersCaisse = ((TIAdministrationViewBean) adminManager.getFirstEntity()).getIdTiersAdministration();
            }
        } else {
            idTiersCaisse = string;
        }
    }

    /**
     * @param string
     */
    public void setLibelleDE(String string) {
        libelleDE = string;
    }

    /**
     * @param string
     */
    public void setLibelleFR(String string) {
        libelleFR = string;
    }

    /**
     * @param string
     */
    public void setLibelleIT(String string) {
        libelleIT = string;
    }

    /**
     * @param string
     */
    public void setLikeOrdreRegroupement(String string) {
        likeOrdreRegroupement = string;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    /**
     * @param string
     */
    public void setNumCaisse(String string) {
        numCaisse = string;
        setIdTiersCaisse(string);
    }

    /**
     * @param string
     */
    public void setOrdreRegroupement(String string) {
        ordreRegroupement = string;
    }

}
