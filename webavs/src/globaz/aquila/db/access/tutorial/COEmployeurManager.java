package globaz.aquila.db.access.tutorial;

import globaz.aquila.common.COBManager;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * Repr�sente un container de type Employeur
 * 
 * @author Arnaud Dostes, 04-oct-2004
 */
public class COEmployeurManager extends COBManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (idemployeur) */
    private String forIdEmployeur = "";
    /** (nomemployeur) */
    private String forNomEmployeur = "";
    /** (typeentreprise) */
    private String forTypeEntreprise = "";
    /** (idemployeur) */
    private String fromIdEmployeur = "";
    /** (nomemployeur) */
    private String fromNomEmployeur = "";
    /** (typeentreprise) */
    private String fromTypeEntreprise = "";

    /**
     * @see globaz.globall.db.BManager#_afterFind(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterFind(BTransaction transaction) throws Exception {
        System.out.println("_afterFind");
    }

    /**
     * @see globaz.globall.db.BManager#_beforeFind(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {
        System.out.println("_beforeFind");
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "EMPLOYEUR";
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdEmployeur().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "idemployeur=" + this._dbWriteNumeric(statement.getTransaction(), getForIdEmployeur());
        }

        // traitement du positionnement
        if (getForNomEmployeur().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "nomemployeur=" + this._dbWriteString(statement.getTransaction(), getForNomEmployeur());
        }

        // traitement du positionnement
        if (getForTypeEntreprise().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "typeentreprise=" + this._dbWriteString(statement.getTransaction(), getForTypeEntreprise());
        }

        // traitement du positionnement
        if (getFromIdEmployeur().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "idemployeur>=" + this._dbWriteNumeric(statement.getTransaction(), getFromIdEmployeur());
        }

        // traitement du positionnement
        if (getFromNomEmployeur().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "nomemployeur>=" + this._dbWriteString(statement.getTransaction(), getFromNomEmployeur());
        }

        // traitement du positionnement
        if (getFromTypeEntreprise().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "typeentreprise>=" + this._dbWriteString(statement.getTransaction(), getFromTypeEntreprise());
        }

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new COEmployeur();
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    public String getForIdEmployeur() {
        return forIdEmployeur;
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    public String getForNomEmployeur() {
        return forNomEmployeur;
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    public String getForTypeEntreprise() {
        return forTypeEntreprise;
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    public String getFromIdEmployeur() {
        return fromIdEmployeur;
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    public String getFromNomEmployeur() {
        return fromNomEmployeur;
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    public String getFromTypeEntreprise() {
        return fromTypeEntreprise;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propri�t�
     */
    public void setForIdEmployeur(String string) {
        forIdEmployeur = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propri�t�
     */
    public void setForNomEmployeur(String string) {
        forNomEmployeur = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propri�t�
     */
    public void setForTypeEntreprise(String string) {
        forTypeEntreprise = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propri�t�
     */
    public void setFromIdEmployeur(String string) {
        fromIdEmployeur = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propri�t�
     */
    public void setFromNomEmployeur(String string) {
        fromNomEmployeur = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propri�t�
     */
    public void setFromTypeEntreprise(String string) {
        fromTypeEntreprise = string;
    }

}
