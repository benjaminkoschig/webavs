package globaz.pavo.db.compte;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hermes.utils.StringUtils;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Commentaire javadoc
 * 
 * @author user GLOBAZ
 */
public class CIAnnonceCentraleManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Fichier CIANCEP */
    /** (KRIANC) */
    private String beforeDateEnvoi = new String();
    /** (KRDCRE) */
    private String forAnnee = new String();
    private String forAnneeEnvoi = new String();
    private String forAnneeMoisCreation = new String();
    private String forAnnonceCentraleId = new String();
    /** (KRTETA) */
    private String forDateCreation = new String();
    /** (KRDENV) */
    private String forDateEnvoi = new String();
    private String forDateEnvoiJJMMYYYY = new String();
    /** (KRTANN) */
    private String forIdTypeAnnonce = new String();

    private String forNotAnnonceCentraleId = new String();

    private String forStatut = new String();
    private String forUserSpy = new String();

    private String inStatut = new String();

    private String order = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CIANCEP";
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
        String sqlWhere = "";

        if (!JadeStringUtil.isBlankOrZero(getForUserSpy())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " PSPY LIKE " + this._dbWriteString(statement.getTransaction(), "%" + getForUserSpy() + "%");
        }

        if (!JadeStringUtil.isBlankOrZero(getForDateEnvoiJJMMYYYY())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " KRDENV= " + this._dbWriteDateAMJ(statement.getTransaction(), getForDateEnvoiJJMMYYYY());
        }

        // traitement du positionnement
        if (getForAnnonceCentraleId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KRIANC=" + this._dbWriteNumeric(statement.getTransaction(), getForAnnonceCentraleId());
        }

        if (!JadeStringUtil.isBlankOrZero(getForNotAnnonceCentraleId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KRIANC <>" + this._dbWriteNumeric(statement.getTransaction(), getForNotAnnonceCentraleId());
        }

        if (!JadeStringUtil.isBlankOrZero(getForAnneeMoisCreation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "( KRDCRE >= " + this._dbWriteNumeric(statement.getTransaction(), getForAnneeMoisCreation())
                    + "01 AND KRDCRE <= " + this._dbWriteNumeric(statement.getTransaction(), getForAnneeMoisCreation())
                    + "31 )";
        }

        // traitement du positionnement
        if (getForDateEnvoi().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KRDENV=" + this._dbWriteNumeric(statement.getTransaction(), getForDateEnvoi());
        }
        // traitement du positionnement
        if (getForIdTypeAnnonce().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KRTANN=" + this._dbWriteNumeric(statement.getTransaction(), getForIdTypeAnnonce());
        }
        // traitement du positionnement
        if (getForStatut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KRTETA=" + this._dbWriteNumeric(statement.getTransaction(), getForStatut());
        }

        if (!JadeStringUtil.isBlankOrZero(getInStatut())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KRTETA IN (" + getInStatut() + ") ";
        }

        // traitement du positionnement
        if (getForDateCreation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KRDCRE=" + this._dbWriteNumeric(statement.getTransaction(), getForDateCreation());
        }
        // traitement du positionnement
        if (getForAnnee().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            int annee = Integer.parseInt(getForAnnee());
            sqlWhere += "KRDCRE BETWEEN "
                    + this._dbWriteNumeric(statement.getTransaction(),
                            StringUtils.padAfterString(getForAnnee(), "0", 8));
            sqlWhere += " AND "
                    + this._dbWriteNumeric(statement.getTransaction(),
                            StringUtils.padAfterString(String.valueOf(annee + 1), "0", 8));
        }
        // traitement du positionnement
        if (getForAnneeEnvoi().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            int annee = Integer.parseInt(getForAnneeEnvoi());
            sqlWhere += "KRDENV BETWEEN "
                    + this._dbWriteNumeric(statement.getTransaction(),
                            StringUtils.padAfterString(getForAnneeEnvoi(), "0", 8));
            sqlWhere += " AND "
                    + this._dbWriteNumeric(statement.getTransaction(),
                            StringUtils.padAfterString(String.valueOf(annee + 1), "0", 8));
        }
        // traitement du positionnement
        if (getForDateCreation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KRDCRE=" + this._dbWriteNumeric(statement.getTransaction(), getForDateCreation());
        }
        // traitement du positionnement
        if (beforeDateEnvoi.length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KRDENV < " + this._dbWriteNumeric(statement.getTransaction(), getBeforeDateEnvoi());
        }
        return sqlWhere;
    }

    /**
     * Instancie un objet étendant BEntity
     * 
     * @return BEntity un objet repésentant le résultat
     * @throws Exception
     *             la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CIAnnonceCentrale();
    }

    /**
     * @return
     */
    public String getBeforeDateEnvoi() {
        return beforeDateEnvoi;
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
     * @return
     */
    public String getForAnneeEnvoi() {
        return forAnneeEnvoi;
    }

    public String getForAnneeMoisCreation() {
        return forAnneeMoisCreation;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForAnnonceCentraleId() {
        return forAnnonceCentraleId;
    }

    /**
     * Returns the forDateCreation.
     * 
     * @return String
     */
    public String getForDateCreation() {
        return forDateCreation;
    }

    public String getForDateEnvoi() {
        return forDateEnvoi;
    }

    public String getForDateEnvoiJJMMYYYY() {
        return forDateEnvoiJJMMYYYY;
    }

    public String getForIdTypeAnnonce() {
        return forIdTypeAnnonce;
    }

    public String getForNotAnnonceCentraleId() {
        return forNotAnnonceCentraleId;
    }

    /**
     * Returns the forStatut.
     * 
     * @return String
     */
    public String getForStatut() {
        return forStatut;
    }

    public String getForUserSpy() {
        return forUserSpy;
    }

    public String getInStatut() {
        return inStatut;
    }

    /**
     * Returns the order.
     * 
     * @return String
     */
    public String getOrder() {
        return order;
    }

    /**
     * @param string
     */
    public void setBeforeDateEnvoi(String string) {
        beforeDateEnvoi = string;
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
     * @param string
     */
    public void setForAnneeEnvoi(String string) {
        forAnneeEnvoi = string;
    }

    public void setForAnneeMoisCreation(String forAnneeMoisCreation) {
        this.forAnneeMoisCreation = forAnneeMoisCreation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */
    public void setForAnnonceCentraleId(String newForAnnonceCentraleId) {
        forAnnonceCentraleId = newForAnnonceCentraleId;
    }

    /**
     * Sets the forDateCreation.
     * 
     * @param forDateCreation
     *            The forDateCreation to set
     */
    public void setForDateCreation(String forDateCreation) {
        this.forDateCreation = forDateCreation;
    }

    public void setForDateEnvoi(String newForDateEnvoi) {
        forDateEnvoi = newForDateEnvoi;
    }

    public void setForDateEnvoiJJMMYYYY(String forDateEnvoiJJMMYYYY) {
        this.forDateEnvoiJJMMYYYY = forDateEnvoiJJMMYYYY;
    }

    public void setForIdTypeAnnonce(String newForIdTypeAnnonce) {
        forIdTypeAnnonce = newForIdTypeAnnonce;
    }

    public void setForNotAnnonceCentraleId(String forNotAnnonceCentraleId) {
        this.forNotAnnonceCentraleId = forNotAnnonceCentraleId;
    }

    /**
     * Sets the forStatut.
     * 
     * @param forStatut
     *            The forStatut to set
     */
    public void setForStatut(String forStatut) {
        this.forStatut = forStatut;
    }

    public void setForUserSpy(String forUserSpy) {
        this.forUserSpy = forUserSpy;
    }

    public void setInStatut(String inStatut) {
        this.inStatut = inStatut;
    }

    /**
     * Sets the order.
     * 
     * @param order
     *            The order to set
     */
    public void setOrder(String order) {
        this.order = order;
    }

    public void setOrderByDateEnvoiDesc() {
        order = _getCollection() + "CIANCEP.KRDENV DESC";
    }

    public void setOrderByTypeEnrDesc() {
        order = _getCollection() + "CIANCEP.KRDCRE DESC";
    }

}
