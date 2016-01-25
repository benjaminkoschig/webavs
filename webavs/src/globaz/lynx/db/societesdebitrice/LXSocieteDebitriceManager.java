package globaz.lynx.db.societesdebitrice;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class LXSocieteDebitriceManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdExterne;
    private String likeIdExterne;
    private String likeNomSociete;

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + LXSocieteDebitrice.TABLE_LXSOCIP + " INNER JOIN " + _getCollection() + "TITIERP ON "
                + _getCollection() + LXSocieteDebitrice.TABLE_LXSOCIP + ".IDTIERS=" + _getCollection()
                + "TITIERP.HTITIE";
    }

    /**
     * Renvoie la clause de tri
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return _getCollection() + LXSocieteDebitrice.TABLE_LXSOCIP + "." + LXSocieteDebitrice.FIELD_IDEXTERNE + ", "
                + _getCollection() + "TITIERP.HTLDU1";
    }

    /**
     * Renvoie la composante de sélection de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isBlank(getLikeIdExterne())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXSocieteDebitrice.TABLE_LXSOCIP).append(".")
                    .append(LXSocieteDebitrice.FIELD_IDEXTERNE).append(" like ")
                    .append(_dbWriteString(statement.getTransaction(), "%" + getLikeIdExterne() + "%"));
        }

        if (!JadeStringUtil.isBlank(getLikeNomSociete())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            String nomFormatte = JadeStringUtil.convertSpecialChars(getLikeNomSociete());
            nomFormatte = JadeStringUtil.toUpperCase(nomFormatte);

            sqlWhere.append(_getCollection()).append("TITIERP.HTLDU1 like ")
                    .append(_dbWriteString(statement.getTransaction(), "%" + nomFormatte + "%"));
        }

        if (!JadeStringUtil.isBlank(getForIdExterne())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXSocieteDebitrice.TABLE_LXSOCIP).append(".")
                    .append(LXSocieteDebitrice.FIELD_IDEXTERNE).append(" = ")
                    .append(_dbWriteString(statement.getTransaction(), getForIdExterne()));
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXSocieteDebitrice();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForIdExterne() {
        return forIdExterne;
    }

    public String getLikeIdExterne() {
        return likeIdExterne;
    }

    public String getLikeNomSociete() {
        return likeNomSociete;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForIdExterne(String forIdExterne) {
        this.forIdExterne = forIdExterne;
    }

    public void setLikeIdExterne(String likeIdExterne) {
        this.likeIdExterne = likeIdExterne;
    }

    public void setLikeNomSociete(String likeNomSociete) {
        this.likeNomSociete = likeNomSociete;
    }
}
