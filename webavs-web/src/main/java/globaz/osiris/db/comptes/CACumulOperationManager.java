package globaz.osiris.db.comptes;

import globaz.globall.db.BStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Insérez la description du type ici. Date de création : (24.01.2002 14:57:39)
 * 
 * @author: Administrator
 */
public class CACumulOperationManager extends globaz.globall.db.BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List forEtatNotIn = new ArrayList();
    private java.lang.String forIdCompteAnnexe = new String();
    private java.lang.String forIdSection = new String();
    private java.lang.String fromIdJournal = new String();
    private java.lang.String likeIdTypeOperation = new String();

    @Override
    protected String _getFields(BStatement statement) {

        return _getCollection() + "CAOPERP.IDTYPEOPERATION, SUM(" + _getCollection()
                + "CAOPERP.MONTANT) AS TOTALMONTANT";
    }

    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CAOPERP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if ((getFromIdJournal() != null) && (getFromIdJournal().length() != 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CAOPERP.IDJOURNAL>="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdJournal());
        }

        if ((getForEtatNotIn() != null) && (getForEtatNotIn().size() > 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CAOPERP.ETAT NOT IN (";
            Iterator iter = getForEtatNotIn().iterator();
            while (iter.hasNext()) {
                String element = (String) iter.next();
                sqlWhere += element + ",";
            }
            sqlWhere = sqlWhere.substring(0, sqlWhere.length() - 1);
            sqlWhere += ")";
        }

        // traitement du positionnement
        if (getLikeIdTypeOperation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CAOPERP.IDTYPEOPERATION LIKE "
                    + this._dbWriteString(statement.getTransaction(), getLikeIdTypeOperation() + "%");
        }

        if ((getForIdCompteAnnexe() != null) && (getForIdCompteAnnexe().length() != 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CAOPERP.IDCOMPTEANNEXE="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdCompteAnnexe());
        }

        if (getForIdSection().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CAOPERP.IDSECTION="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdSection());
        }

        if (sqlWhere.length() != 0) {
            sqlWhere += " GROUP BY " + _getCollection() + "CAOPERP.IDTYPEOPERATION";
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CACumulOperation();
    }

    /**
     * Returns the forEtatNotIn.
     * 
     * @return List
     */
    public List getForEtatNotIn() {
        return forEtatNotIn;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.02.2002 14:03:50)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdCompteAnnexe() {
        return forIdCompteAnnexe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.02.2002 15:13:11)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdSection() {
        return forIdSection;
    }

    /**
     * Returns the fromIdJournal.
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromIdJournal() {
        return fromIdJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.02.2002 13:52:41)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLikeIdTypeOperation() {
        return likeIdTypeOperation;
    }

    /**
     * Sets the forEtatNotIn.
     * 
     * @param forEtatNotIn
     *            The forEtatNotIn to set
     */
    public void setForEtatNotIn(List forEtatNotIn) {
        this.forEtatNotIn = forEtatNotIn;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.02.2002 14:03:50)
     * 
     * @param newForIdCompteAnnexe
     *            java.lang.String
     */
    public void setForIdCompteAnnexe(java.lang.String newForIdCompteAnnexe) {
        forIdCompteAnnexe = newForIdCompteAnnexe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.02.2002 15:13:11)
     * 
     * @param newForIdSection
     *            java.lang.String
     */
    public void setForIdSection(java.lang.String newForIdSection) {
        forIdSection = newForIdSection;
    }

    /**
     * Sets the fromIdJournal.
     * 
     * @param fromIdJournal
     *            The fromIdJournal to set
     */
    public void setFromIdJournal(java.lang.String fromIdJournal) {
        this.fromIdJournal = fromIdJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.02.2002 13:52:41)
     * 
     * @param newLikeIdTypeOperation
     *            java.lang.String
     */
    public void setLikeIdTypeOperation(java.lang.String newLikeIdTypeOperation) {
        likeIdTypeOperation = newLikeIdTypeOperation;
    }

}
