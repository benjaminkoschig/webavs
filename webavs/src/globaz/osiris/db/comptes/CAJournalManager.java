package globaz.osiris.db.comptes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

/**
 * Insérez la description du type ici. Date de création : (12.12.2001 09:26:37)
 * 
 * @author: Administrator
 */
public class CAJournalManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateValeurCG = new String();
    private String forEtat = new String();
    private String forIdJournal = new String();
    private String forLibelleLike;
    private String forProprietaire = new String();
    private String forSelectionJournaux = new String();
    private String forSelectionTri = new String();
    private String forTypeJournal = new String();
    private String fromDescription = new String();
    private String fromIdJournal = new String();
    private String fromNumero = new String();
    private String untilDateValeurCG = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CAJournal.TABLE_CAJOURP;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(BStatement statement) {

        // Traitement du positionnement pour une sélection des sélections

        if ((getForSelectionTri().length() != 0) && !getForSelectionTri().equals("1000")) {

            switch (java.lang.Integer.parseInt(getForSelectionTri())) {
                case 1:
                    return CAJournal.FIELD_DATE + " DESC";
                case 2:
                    return CAJournal.FIELD_DATEVALEURCG + " DESC";
                case 3:
                    return CAJournal.FIELD_LIBELLE;
                case 4:
                    return CAJournal.FIELD_PROPRIETAIRE + ", " + CAJournal.FIELD_IDJOURNAL + " DESC";
                case 5:
                    return CAJournal.FIELD_PROPRIETAIRE + ", " + CAJournal.FIELD_DATE + " DESC";
                case 6:
                    return CAJournal.FIELD_PROPRIETAIRE + ", " + CAJournal.FIELD_LIBELLE;
                default:
                    break;
            }
        }

        return CAJournal.FIELD_IDJOURNAL + " DESC";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // Traitement du positionnement depuis un numéro
        if ((getForTypeJournal() != null) && (getForTypeJournal().trim().length() != 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAJournal.FIELD_TYPEJOURNAL + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForTypeJournal());
        } else {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere = CAJournal.FIELD_TYPEJOURNAL + " <> " + CAJournal.TYPE_TEMPORAIRE;
        }

        // Traitement du positionnement pour une sélection des sections
        if ((getForSelectionJournaux().length() != 0) && !getForSelectionJournaux().equals("1000")) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            switch (java.lang.Integer.parseInt(getForSelectionJournaux())) {
                case 1:
                    sqlWhere += CAJournal.FIELD_ETAT + " = " + CAJournal.OUVERT;
                    break;
                case 2:
                    sqlWhere += CAJournal.FIELD_ETAT + " = " + CAJournal.COMPTABILISE;
                    break;
                case 3:
                    sqlWhere += CAJournal.FIELD_ETAT + " = " + CAJournal.PARTIEL;
                    break;
                case 4:
                    sqlWhere += CAJournal.FIELD_ETAT + " = " + CAJournal.ERREUR;
                    break;
                case 5:
                    sqlWhere += CAJournal.FIELD_ETAT + " = " + CAJournal.ANNULE;
                    break;
                case 6:
                    sqlWhere += CAJournal.FIELD_ETAT + " = " + CAJournal.TRAITEMENT;
                    break;
                case 7:
                    sqlWhere += CAJournal.FIELD_ETAT + " in (" + CAJournal.TRAITEMENT + ", " + CAJournal.OUVERT + ", "
                            + CAJournal.PARTIEL + ", " + CAJournal.ERREUR + ") ";
                    break;
                default:
                    break;
            }
        }

        // Traitement du positionnement depuis un numéro
        if (getForProprietaire().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "UPPER(" + CAJournal.FIELD_PROPRIETAIRE + ") = UPPER("
                    + this._dbWriteString(statement.getTransaction(), getForProprietaire()) + ")";
        }

        // Traitement du positionnement depuis un numéro
        if (getFromNumero().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAJournal.FIELD_IDJOURNAL + " <= "
                    + this._dbWriteNumeric(statement.getTransaction(), getFromNumero());
        }
        // Traitement du positionnement depuis un numéro
        if (getFromIdJournal().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAJournal.FIELD_IDJOURNAL + " >= "
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdJournal());
        }
        // Traitement du positionnement depuis un numéro
        if (getForIdJournal().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAJournal.FIELD_IDJOURNAL + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdJournal());
        }
        // Traitement du positionnement depuis une Description
        if (getFromDescription().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "UPPER(" + CAJournal.FIELD_LIBELLE + ") >= UPPER("
                    + this._dbWriteString(statement.getTransaction(), getFromDescription()) + ")";
        }
        // Traitement du positionnement pour une date de valeur
        if (getForDateValeurCG().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAJournal.FIELD_DATEVALEURCG + "="
                    + this._dbWriteDateAMJ(statement.getTransaction(), getForDateValeurCG());
        }
        // Traitement du positionnement jusqu'à une date de valeur
        if (getUntilDateValeurCG().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAJournal.FIELD_DATEVALEURCG + " <= "
                    + this._dbWriteDateAMJ(statement.getTransaction(), getUntilDateValeurCG());
        }
        // Traitement du positionnement l'état du journal
        if (getForEtat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAJournal.FIELD_ETAT + "=" + this._dbWriteNumeric(statement.getTransaction(), getForEtat());
        }

        if (!JadeStringUtil.isBlank(getForLibelleLike())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "UPPER(" + CAJournal.FIELD_LIBELLE + ") like UPPER("
                    + this._dbWriteString(statement.getTransaction(), "%" + getForLibelleLike() + "%") + ")";
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAJournal();
    }

    /**
     * Returns the forDateValeurCG.
     * 
     * @return String
     */
    public String getForDateValeurCG() {
        return forDateValeurCG;
    }

    /**
     * Returns the forEtat.
     * 
     * @return String
     */
    public String getForEtat() {
        return forEtat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2002 12:11:37)
     * 
     * @return String
     */
    public String getForIdJournal() {
        return forIdJournal;
    }

    public String getForLibelleLike() {
        return forLibelleLike;
    }

    public String getForProprietaire() {
        return forProprietaire;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2002 11:48:04)
     * 
     * @return String
     */
    public String getForSelectionJournaux() {
        return forSelectionJournaux;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2002 11:51:23)
     * 
     * @return String
     */
    public String getForSelectionTri() {
        return forSelectionTri;
    }

    /**
     * Returns the forTypeJournal.
     * 
     * @return String
     */
    public String getForTypeJournal() {
        return forTypeJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2002 11:53:19)
     * 
     * @return String
     */
    public String getFromDescription() {
        return fromDescription;
    }

    /**
     * Returns the fromIdJournal.
     * 
     * @return String
     */
    public String getFromIdJournal() {
        return fromIdJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2002 11:52:46)
     * 
     * @return String
     */
    public String getFromNumero() {
        return fromNumero;
    }

    /**
     * @return the untilDateValeurCG
     */
    public String getUntilDateValeurCG() {
        return untilDateValeurCG;
    }

    /**
     * Sets the forDateValeurCG.
     * 
     * @param forDateValeurCG
     *            The forDateValeurCG to set
     */
    public void setForDateValeurCG(String forDateValeurCG) {
        this.forDateValeurCG = forDateValeurCG;
    }

    /**
     * Sets the forEtat.
     * 
     * @param forEtat
     *            The forEtat to set
     */
    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2002 12:11:37)
     * 
     * @param newForIdJournal
     *            String
     */
    public void setForIdJournal(String newForIdJournal) {
        forIdJournal = newForIdJournal;
    }

    public void setForLibelleLike(String forLibelleLike) {
        this.forLibelleLike = forLibelleLike;
    }

    public void setForProprietaire(String forProprietaire) {
        this.forProprietaire = forProprietaire;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2002 11:48:04)
     * 
     * @param newForSelectionJournaux
     *            String
     */
    public void setForSelectionJournaux(String newForSelectionJournaux) {
        forSelectionJournaux = newForSelectionJournaux;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2002 11:51:23)
     * 
     * @param newForSelectionTri
     *            String
     */
    public void setForSelectionTri(String newForSelectionTri) {
        forSelectionTri = newForSelectionTri;
    }

    /**
     * Sets the forTypeJournal.
     * 
     * @param forTypeJournal
     *            The forTypeJournal to set
     */
    public void setForTypeJournal(String forTypeJournal) {
        this.forTypeJournal = forTypeJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2002 11:53:19)
     * 
     * @param newFromDescription
     *            String
     */
    public void setFromDescription(String newFromDescription) {
        fromDescription = newFromDescription;
    }

    /**
     * Sets the fromIdJournal.
     * 
     * @param fromIdJournal
     *            The fromIdJournal to set
     */
    public void setFromIdJournal(String forIdJournalAfter) {
        fromIdJournal = forIdJournalAfter;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2002 11:52:46)
     * 
     * @param newFromNumero
     *            String
     */
    public void setFromNumero(String newFromNumero) {
        fromNumero = newFromNumero;
    }

    /**
     * @param untilDateValeurCG
     *            the untilDateValeurCG to set
     */
    public void setUntilDateValeurCG(String untilDateValeurCG) {
        this.untilDateValeurCG = untilDateValeurCG;
    }

}
