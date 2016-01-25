package globaz.helios.db.modeles;

import globaz.globall.db.BManager;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

public class CGLigneModeleEcritureManager extends BManager implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdCentreCharge = new String();
    private String forIdCompte = new String();
    private String forIdEnteteModeleEcriture = new String();

    private String forIdModeleEcriture = new String();

    /**
     * Commentaire relatif au constructeur CGLigneModeleEcritureManager.
     */
    public CGLigneModeleEcritureManager() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        if (JadeStringUtil.isBlank(getForIdModeleEcriture())) {
            return _getCollection() + "CGLMODP";
        } else {
            return _getCollection() + "CGLMODP INNER JOIN " + _getCollection() + "CGEMODP on " + _getCollection()
                    + "CGLMODP.IDENTETEMODECRIT = " + _getCollection() + "CGEMODP.IDENTETEMODECRIT";
        }
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CGLMODP.IDENTETEMODECRIT ";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdEnteteModeleEcriture().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGLMODP.IDENTETEMODECRIT="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdEnteteModeleEcriture());
        }

        // traitement du positionnement
        if (getForIdCentreCharge() != null && getForIdCentreCharge().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGLMODP.IDCENTRECHARGE="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdCentreCharge());
        }

        // traitement du positionnement
        if (getForIdCompte().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGLMODP.IDCOMPTE="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdCompte());
        }

        if (!JadeStringUtil.isBlank(getForIdModeleEcriture())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGEMODP.IDMODELEECRITURE ="
                    + _dbWriteString(statement.getTransaction(), getForIdModeleEcriture());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGLigneModeleEcriture();
    }

    public String getForIdCentreCharge() {
        return forIdCentreCharge;
    }

    public String getForIdCompte() {
        return forIdCompte;
    }

    /**
     * Getter
     */
    public String getForIdEnteteModeleEcriture() {
        return forIdEnteteModeleEcriture;
    }

    /**
     * @return
     */
    public String getForIdModeleEcriture() {
        return forIdModeleEcriture;
    }

    public void setForIdCentreCharge(String newForIdCentreCharge) {
        forIdCentreCharge = newForIdCentreCharge;
    }

    public void setForIdCompte(String newForIdCompte) {
        forIdCompte = newForIdCompte;
    }

    /**
     * Setter
     */
    public void setForIdEnteteModeleEcriture(String newForIdEnteteModeleEcriture) {
        forIdEnteteModeleEcriture = newForIdEnteteModeleEcriture;
    }

    /**
     * @param string
     */
    public void setForIdModeleEcriture(String s) {
        forIdModeleEcriture = s;
    }

}
