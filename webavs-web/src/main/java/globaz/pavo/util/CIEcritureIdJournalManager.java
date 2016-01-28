package globaz.pavo.util;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.pavo.db.compte.CIEcriture;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
// Manager qui permet de récupérer tous les journaux pour lesquels il y a des
// ecritures d'un employeur donné
// CF comparaison de la masse salariale
public class CIEcritureIdJournalManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = new String();
    private String forEmployeur = new String();

    /**
     * Constructor for CIEcritureIdJournalManager.
     */
    public CIEcritureIdJournalManager() {
        super();
    }

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "KCID";
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CIECRIP";
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        if (getForAnnee().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBNANN="
                    + _dbWriteNumeric(statement.getTransaction(), getForAnnee());
        }
        if (getForEmployeur().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBITIE= "
                    + _dbWriteNumeric(statement.getTransaction(), getForEmployeur());

        }
        // seulement les écritures avtives
        sqlWhere += " AND KBTCPT IN (" + CIEcriture.CS_CI + "," + CIEcriture.CS_GENRE_6 + "," + CIEcriture.CS_GENRE_7
                + "," + CIEcriture.CS_CI_SUSPENS + ")";
        // On les regroupes pour par journal
        sqlWhere += " GROUP BY KCID ";
        return sqlWhere;

    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CIEcritureIdJournal();
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
     * Returns the forEmployeur.
     * 
     * @return String
     */
    public String getForEmployeur() {
        return forEmployeur;
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
     * Sets the forEmployeur.
     * 
     * @param forEmployeur
     *            The forEmployeur to set
     */
    public void setForEmployeur(String forEmployeur) {
        this.forEmployeur = forEmployeur;
    }

}
