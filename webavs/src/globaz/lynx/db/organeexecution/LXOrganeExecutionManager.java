package globaz.lynx.db.organeexecution;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.utils.LXUtils;
import java.util.ArrayList;

public class LXOrganeExecutionManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsGenre;
    private ArrayList<String> forCsGenreIn;
    private String forIdSocieteDebitrice;

    private String likeNomOrganeExecution;

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + LXOrganeExecution.TABLE_LXOREXP;
    }

    /**
     * Renvoie la clause de tri
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return _getCollection() + LXOrganeExecution.TABLE_LXOREXP + "." + LXOrganeExecution.FIELD_NOM;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(getForIdSocieteDebitrice())
                && JadeStringUtil.isDigit(getForIdSocieteDebitrice())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXOrganeExecution.FIELD_IDSOCIETE).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdSocieteDebitrice()));
        }

        if (!JadeStringUtil.isBlank(getLikeNomOrganeExecution())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXOrganeExecution.FIELD_NOM).append(" like ")
                    .append(this._dbWriteString(statement.getTransaction(), "%" + getLikeNomOrganeExecution() + "%"));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsGenre())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXOrganeExecution.FIELD_CSGENRE).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForCsGenre()));
        }

        if (getForCsGenreIn() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXUtils.getWhereValueMultiple(LXOrganeExecution.FIELD_CSGENRE, getForCsGenreIn()));
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXOrganeExecution();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForCsGenre() {
        return forCsGenre;
    }

    public ArrayList<String> getForCsGenreIn() {
        return forCsGenreIn;
    }

    public String getForIdSocieteDebitrice() {
        return forIdSocieteDebitrice;
    }

    public String getLikeNomOrganeExecution() {
        return likeNomOrganeExecution;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForCsGenre(String forCsGenre) {
        this.forCsGenre = forCsGenre;
    }

    public void setForCsGenreIn(ArrayList<String> forCsGenreIn) {
        this.forCsGenreIn = forCsGenreIn;
    }

    public void setForIdSocieteDebitrice(String forIdSocieteDebitrice) {
        this.forIdSocieteDebitrice = forIdSocieteDebitrice;
    }

    public void setLikeNomOrganeExecution(String likeNomOrganeExecution) {
        this.likeNomOrganeExecution = likeNomOrganeExecution;
    }

}
