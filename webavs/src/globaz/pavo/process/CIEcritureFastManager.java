package globaz.pavo.process;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.pavo.db.compte.CIEcriture;

/**
 * Manager de <tt>CIEcriture</tt>. Date de création : (12.11.2002 13:07:10)
 * 
 * @author: David Girardin
 */
public class CIEcritureFastManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = new String();
    private String forCompteIndividuelId = new String();
    private String forIdTypeCompteCompta = new String();
    private String forNotIdTypeCompte = new String();
    private java.lang.String order = new String();

    /**
     * Constructeur. Date de création : (09.01.2003 07:49:46)
     */
    public CIEcritureFastManager() {
        wantCallMethodBeforeFind(true);
    }

    /**
     * Tri. Date de création : (28.10.2002 08:31:46)
     */
    @Override
    protected void _beforeFind(globaz.globall.db.BTransaction transaction) {

    }

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "KBIECR,KBITIE";
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CIECRIP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     * 
     * @param BStatement
     *            le statement
     * @return String le ORDER BY
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return order;
    }

    /**
     * retourne la clause WHERE de la requete SQL
     * 
     * @param BStatement
     *            le statement
     * @return la clause WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        boolean needToIgnoreCopy = true;
        String sqlWhere = "";
        // traitement du positionnement
        if (getForCompteIndividuelId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KAIIND="
                    + _dbWriteNumeric(statement.getTransaction(), getForCompteIndividuelId());
        }
        // traitement du positionnement
        if (getForAnnee().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBNANN="
                    + _dbWriteNumeric(statement.getTransaction(), getForAnnee());
        }
        // type compte CA, genre 6 et 7
        if (getForIdTypeCompteCompta().length() != 0) {
            needToIgnoreCopy = false;
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBTCPT IN (" + CIEcriture.CS_CI + "," + CIEcriture.CS_GENRE_6 + ","
                    + CIEcriture.CS_GENRE_7 + ")";
        }
        if (needToIgnoreCopy) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBTCPT<>" + CIEcriture.CS_CORRECTION;
        }
        return sqlWhere;
    }

    @Override
    public final BEntity _newEntity() {
        return new CIEcritureFast();
    }

    /**
     * Returns the forAnnee.
     * 
     * @return String
     */
    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * Returns the forCompteIndividuelId.
     * 
     * @return String
     */
    public String getForCompteIndividuelId() {
        return forCompteIndividuelId;
    }

    /**
     * Returns the forIdTypeCompteCompta.
     * 
     * @return String
     */
    public String getForIdTypeCompteCompta() {
        return forIdTypeCompteCompta;
    }

    /**
     * Returns the forNotIdTypeCompte.
     * 
     * @return String
     */
    public String getForNotIdTypeCompte() {
        return forNotIdTypeCompte;
    }

    /**
     * Returns the order.
     * 
     * @return java.lang.String
     */
    public java.lang.String getOrder() {
        return order;
    }

    /**
     * Sets the forAnnee.
     * 
     * @param forAnnee
     *            The forAnnee to set
     */
    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    /**
     * Sets the forCompteIndividuelId.
     * 
     * @param forCompteIndividuelId
     *            The forCompteIndividuelId to set
     */
    public void setForCompteIndividuelId(String forCompteIndividuelId) {
        this.forCompteIndividuelId = forCompteIndividuelId;
    }

    /**
     * Sets the forIdTypeCompteCompta.
     * 
     * @param forIdTypeCompteCompta
     *            The forIdTypeCompteCompta to set
     */
    public void setForIdTypeCompteCompta(String forIdTypeCompteCompta) {
        this.forIdTypeCompteCompta = forIdTypeCompteCompta;
    }

    /**
     * Sets the forNotIdTypeCompte.
     * 
     * @param forNotIdTypeCompte
     *            The forNotIdTypeCompte to set
     */
    public void setForNotIdTypeCompte(String forNotIdTypeCompte) {
        this.forNotIdTypeCompte = forNotIdTypeCompte;
    }

    /**
     * Sets the order.
     * 
     * @param order
     *            The order to set
     */
    public void setOrder(java.lang.String order) {
        this.order = order;
    }

}
