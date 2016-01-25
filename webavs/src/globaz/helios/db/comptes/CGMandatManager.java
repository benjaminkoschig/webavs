package globaz.helios.db.comptes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import java.io.Serializable;

public class CGMandatManager extends BManager implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCode = "";
    private String forLibelleDeLike = new String();
    private String forLibelleFrLike = new String();

    private String forLibelleItLike = new String();

    private Boolean forNotComptabiliteAVS = false;
    private String fromIdMandat = new String();

    private String fromLibelleDe = new String();
    private String fromLibelleFr = new String();
    private String fromLibelleIt = new String();
    private String orderby = "";

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CGMandat.TABLE_NAME;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return orderby;
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getFromIdMandat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGMandat.FIELD_IDMANDAT + ">="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdMandat());
        }

        // traitement du positionnement
        if (getFromLibelleFr().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGMandat.FIELD_LIBELLEFR + ">="
                    + this._dbWriteString(statement.getTransaction(), getFromLibelleFr());
        }

        // traitement du positionnement
        if (getFromLibelleDe().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGMandat.FIELD_LIBELLEDE + ">="
                    + this._dbWriteString(statement.getTransaction(), getFromLibelleDe());
        }

        // traitement du positionnement
        if (getFromLibelleIt().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGMandat.FIELD_LIBELLEIT + ">="
                    + this._dbWriteString(statement.getTransaction(), getFromLibelleIt());
        }

        if (getForLibelleFrLike().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGMandat.FIELD_LIBELLEFR + " like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getForLibelleFrLike() + "%");
        }

        if (getForLibelleDeLike().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGMandat.FIELD_LIBELLEDE + " like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getForLibelleDeLike() + "%");
        }

        if (getForLibelleItLike().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGMandat.FIELD_LIBELLEIT + "  like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getForLibelleItLike() + "%");
        }

        if (getForNotComptabiliteAVS()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGMandat.FIELD_ESTCOMPTABILITEAVS + " = '2'";
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CGMandat();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.05.2003 11:09:58)
     * 
     * @return String
     */
    public String getForCode() {
        return forCode;
    }

    public String getForLibelleDeLike() {
        return forLibelleDeLike;
    }

    public String getForLibelleFrLike() {
        return forLibelleFrLike;
    }

    public String getForLibelleItLike() {
        return forLibelleItLike;
    }

    /**
     * @return the forEstComptabiliteAVS
     */
    public Boolean getForNotComptabiliteAVS() {
        return forNotComptabiliteAVS;
    }

    /**
     * Getter
     */
    public String getFromIdMandat() {
        return fromIdMandat;
    }

    public String getFromLibelleDe() {
        return fromLibelleDe;
    }

    public String getFromLibelleFr() {
        return fromLibelleFr;
    }

    public String getFromLibelleIt() {
        return fromLibelleIt;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.05.2003 11:09:58)
     * 
     * @param newForCode
     *            String
     */
    public void setForCode(String newForCode) {
        forCode = newForCode;
    }

    public void setForLibelleDeLike(String forLibelleDeLike) {
        this.forLibelleDeLike = forLibelleDeLike;
    }

    public void setForLibelleFrLike(String forLibelleFrLike) {
        this.forLibelleFrLike = forLibelleFrLike;
    }

    public void setForLibelleItLike(String forLibelleItLike) {
        this.forLibelleItLike = forLibelleItLike;
    }

    /**
     * @param forEstComptabiliteAVS
     *            the forEstComptabiliteAVS to set
     */
    public void setForNotComptabiliteAVS(Boolean forEstComptabiliteAVS) {
        forNotComptabiliteAVS = forEstComptabiliteAVS;
    }

    /**
     * Setter
     */
    public void setFromIdMandat(String newFromIdmandat) {
        fromIdMandat = newFromIdmandat;
    }

    public void setFromLibelleDe(String newFromLibellede) {
        fromLibelleDe = newFromLibellede;
    }

    public void setFromLibelleFr(String newFromLibellefr) {
        fromLibelleFr = newFromLibellefr;
    }

    public void setFromLibelleIt(String newFromLibelleit) {
        fromLibelleIt = newFromLibelleit;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.10.2002 16:50:07)
     * 
     * @param newOrderby
     *            String
     */
    public void setOrderby(String newOrderby) {
        orderby = newOrderby;
    }
}
