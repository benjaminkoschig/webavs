package globaz.pavo.db.comparaison;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.pavo.util.CIUtil;

public class CICompteIndividuelComparaisonManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forNumeroAvs = "";
    private String forSuspens = "";

    private String likeNumeroAvs = "";

    public CICompteIndividuelComparaisonManager() {
        super();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        // traitement du positionnement
        if (getForNumeroAvs().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KSNAVS=" + _dbWriteString(statement.getTransaction(), getForNumeroAvs());
        }
        if (getLikeNumeroAvs().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KSNAVS like'" + getLikeNumeroAvs();
            sqlWhere += "%'";
        }
        if (getForSuspens().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KSBTRA=" + _dbWriteString(statement.getTransaction(), getForSuspens());
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CICompteIndividuelComparaison();
    }

    /**
     * @return
     */
    public String getForNumeroAvs() {
        return forNumeroAvs;
    }

    /**
     * @return
     */
    public String getForSuspens() {
        return forSuspens;
    }

    /**
     * @return
     */
    public String getLikeNumeroAvs() {
        return likeNumeroAvs;
    }

    /**
     * @param string
     */
    public void setForNumeroAvs(String string) {
        forNumeroAvs = CIUtil.unFormatAVS(string);
    }

    /**
     * @param string
     */
    public void setForSuspens(String string) {
        forSuspens = string;
    }

    /**
     * @param string
     */
    public void setLikeNumeroAvs(String string) {
        likeNumeroAvs = CIUtil.unFormatAVS(string);
    }

}
