package globaz.pavo.db.comparaison;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.pavo.util.CIUtil;

public class CIAnomalieCIManager extends BManager {

    private static final long serialVersionUID = 8203546465451581434L;
    private String forAtraiter = "";
    private String forIdTypeAnomalie = "";
    private String forInIdTypeAnomalieMaj = "";
    private String forTypeDonneesCI = "";
    private String likeNumeroAvs = "";
    private String likeNumeroAvsNNSS = "";

    private String order = "";

    public CIAnomalieCIManager() {
        super();
    }

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
        // String sqlWhere = "KMBSUS = 1 OR KMIDLO != 0";
        String sqlWhere = "";
        // traitement du positionnement
        if (getLikeNumeroAvs().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if ("true".equalsIgnoreCase(likeNumeroAvsNNSS)) {
                String like = likeNumeroAvs.trim();
                for (int i = like.length(); i < 13; i++) {
                    like += "_";
                }
                sqlWhere = "RTRIM(KTNAVS) like '" + like + "'";
            } else if ("false".equalsIgnoreCase(likeNumeroAvsNNSS)) {
                String like = likeNumeroAvs.trim();
                for (int i = like.length(); i < 11; i++) {
                    like += "_";
                }
                sqlWhere = "RTRIM(KTNAVS) like '" + like + "'";
            } else {
                sqlWhere += "KTNAVS like '" + _dbWriteNumeric(statement.getTransaction(), getLikeNumeroAvs());
                sqlWhere += "%'";
            }
        }
        if (getForIdTypeAnomalie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KTITYP= " + _dbWriteNumeric(statement.getTransaction(), getForIdTypeAnomalie());
        }
        if (getForAtraiter().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KTIETA= " + _dbWriteNumeric(statement.getTransaction(), getForAtraiter());
        }
        if (getForInIdTypeAnomalieMaj().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " KTITYP in(" + CIAnomalieCI.CS_ANNEE_OUVERTURE + "," + CIAnomalieCI.CS_MOTIF_OUVERTURE + ","
                    + CIAnomalieCI.CS_NOM + "," + CIAnomalieCI.CS_NUMERO_AVS_ANCIEN + ","
                    + CIAnomalieCI.CS_NATIONNALITE + ")";

        }
        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CIAnomalieCI();
    }

    /**
     * @return
     */
    public String getForAtraiter() {
        return forAtraiter;
    }

    /**
     * @return
     */
    public String getForIdTypeAnomalie() {
        return forIdTypeAnomalie;
    }

    /**
     * @return
     */
    public String getForInIdTypeAnomalieMaj() {
        return forInIdTypeAnomalieMaj;
    }

    /**
     * @return
     */
    public String getForTypeDonneesCI() {
        return forTypeDonneesCI;
    }

    /**
     * @return
     */
    public String getLikeNumeroAvs() {
        return likeNumeroAvs;
    }

    /**
     * @return
     */
    public String getLikeNumeroAvsNNSS() {
        return likeNumeroAvsNNSS;
    }

    /**
     * @param string
     */
    public void setForAtraiter(String string) {
        forAtraiter = string;
    }

    /**
     * @param string
     */
    public void setForIdTypeAnomalie(String string) {
        forIdTypeAnomalie = string;
    }

    /**
     * @param string
     */
    public void setForInIdTypeAnomalieMaj(String string) {
        forInIdTypeAnomalieMaj = string;
    }

    /**
     * @param string
     */
    public void setForTypeDonneesCI(String string) {
        forTypeDonneesCI = string;
    }

    /**
     * @param string
     */
    public void setLikeNumeroAvs(String string) {
        likeNumeroAvs = CIUtil.unFormatAVS(string);
    }

    /**
     * @param string
     */
    public void setLikeNumeroAvsNNSS(String string) {
        likeNumeroAvsNNSS = string;
    }

    /**
     * @param string
     */
    public void setOrder(String string) {
        order = string;
    }

}
