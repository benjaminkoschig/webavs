package globaz.aquila.db.access.tutorial;

import globaz.aquila.common.COBManager;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * Repr�sente un container de type Personne
 * 
 * @author Pascal Lovy, 04-oct-2004
 */
public class COPersonneManager extends COBManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (datenaissance) */
    private String forDateNaissance = "";
    /** (genre) */
    private String forGenre = "";
    /** (idemployeur) */
    private String forIdEmployeur = "";
    /** (idpersonne) */
    private String forIdPersonne = "";
    /** (nom) */
    private String forNom = "";
    /** (numavs) */
    private String forNumAVS = "";
    /** (prenom) */
    private String forPrenom = "";
    /** (datenaissance) */
    private String fromDateNaissance = "";
    /** (genre) */
    private String fromGenre = "";
    /** (idemployeur) */
    private String fromIdEmployeur = "";
    /** (idpersonne) */
    private String fromIdPersonne = "";
    /** (nom) */
    private String fromNom = "";
    /** (numavs) */
    private String fromNumAVS = "";
    /** (prenom) */
    private String fromPrenom = "";

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "PERSONNE";
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdPersonne().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "idpersonne=" + this._dbWriteString(statement.getTransaction(), getForIdPersonne());
        }

        // traitement du positionnement
        if (getForIdEmployeur().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "idemployeur=" + this._dbWriteString(statement.getTransaction(), getForIdEmployeur());
        }

        // traitement du positionnement
        if (getForNom().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "nom=" + this._dbWriteString(statement.getTransaction(), getForNom());
        }

        // traitement du positionnement
        if (getForPrenom().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "prenom=" + this._dbWriteString(statement.getTransaction(), getForPrenom());
        }

        // traitement du positionnement
        if (getForGenre().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "genre=" + this._dbWriteString(statement.getTransaction(), getForGenre());
        }

        // traitement du positionnement
        if (getForDateNaissance().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "datenaissance=" + this._dbWriteString(statement.getTransaction(), getForDateNaissance());
        }

        // traitement du positionnement
        if (getForNumAVS().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "numavs=" + this._dbWriteString(statement.getTransaction(), getForNumAVS());
        }

        // traitement du positionnement
        if (getFromIdPersonne().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "idpersonne>=" + this._dbWriteString(statement.getTransaction(), getFromIdPersonne());
        }

        // traitement du positionnement
        if (getFromIdEmployeur().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "idemployeur>=" + this._dbWriteString(statement.getTransaction(), getFromIdEmployeur());
        }

        // traitement du positionnement
        if (getFromNom().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "nom>=" + this._dbWriteString(statement.getTransaction(), getFromNom());
        }

        // traitement du positionnement
        if (getFromPrenom().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "prenom>=" + this._dbWriteString(statement.getTransaction(), getFromPrenom());
        }

        // traitement du positionnement
        if (getFromGenre().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "genre>=" + this._dbWriteString(statement.getTransaction(), getFromGenre());
        }

        // traitement du positionnement
        if (getFromDateNaissance().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "datenaissance>=" + this._dbWriteString(statement.getTransaction(), getFromDateNaissance());
        }

        // traitement du positionnement
        if (getFromNumAVS().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "numavs>=" + this._dbWriteString(statement.getTransaction(), getFromNumAVS());
        }

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new COPersonne();
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    public String getForDateNaissance() {
        return forDateNaissance;
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    public String getForGenre() {
        return forGenre;
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
    public String getForIdPersonne() {
        return forIdPersonne;
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    public String getForNom() {
        return forNom;
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    public String getForNumAVS() {
        return forNumAVS;
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    public String getForPrenom() {
        return forPrenom;
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    public String getFromDateNaissance() {
        return fromDateNaissance;
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    public String getFromGenre() {
        return fromGenre;
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
    public String getFromIdPersonne() {
        return fromIdPersonne;
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    public String getFromNom() {
        return fromNom;
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    public String getFromNumAVS() {
        return fromNumAVS;
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    public String getFromPrenom() {
        return fromPrenom;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propri�t�
     */
    public void setForDateNaissance(String string) {
        forDateNaissance = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propri�t�
     */
    public void setForGenre(String string) {
        forGenre = string;
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
    public void setForIdPersonne(String string) {
        forIdPersonne = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propri�t�
     */
    public void setForNom(String string) {
        forNom = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propri�t�
     */
    public void setForNumAVS(String string) {
        forNumAVS = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propri�t�
     */
    public void setForPrenom(String string) {
        forPrenom = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propri�t�
     */
    public void setFromDateNaissance(String string) {
        fromDateNaissance = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propri�t�
     */
    public void setFromGenre(String string) {
        fromGenre = string;
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
    public void setFromIdPersonne(String string) {
        fromIdPersonne = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propri�t�
     */
    public void setFromNom(String string) {
        fromNom = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propri�t�
     */
    public void setFromNumAVS(String string) {
        fromNumAVS = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propri�t�
     */
    public void setFromPrenom(String string) {
        fromPrenom = string;
    }

}