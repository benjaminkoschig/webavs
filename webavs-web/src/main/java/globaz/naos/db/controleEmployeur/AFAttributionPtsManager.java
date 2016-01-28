package globaz.naos.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import java.io.Serializable;

public class AFAttributionPtsManager extends BManager implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdAttributionPts = "";
    private String forIdControle = "";
    private String forLastModification = "";
    private String forLastUser = "";
    private String forNumAffilie = "";
    private String likeNumAffilie = "";
    private String likeUser = "";
    private boolean orderByModification = false;

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "AFATTPTS";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        if (isOrderByModification()) {
            return " MPAPID DESC ";
        } else {
            return "";
        }
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getLikeNumAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MALNAF like '" + getLikeNumAffilie() + "%'";
        }

        if (getForNumAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MALNAF=" + this._dbWriteString(statement.getTransaction(), getForNumAffilie());
        }

        if (getForIdControle().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MDICON=" + this._dbWriteNumeric(statement.getTransaction(), getForIdControle());
        }

        if (getForIdAttributionPts().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MPAPID=" + this._dbWriteNumeric(statement.getTransaction(), getForIdAttributionPts());
        }

        if (getForLastUser().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MPLUSR=" + this._dbWriteNumeric(statement.getTransaction(), getForLastUser());
        }

        if (getLikeUser().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MPLUSR like '" + getLikeUser() + "%'";
        }

        if (getForLastModification().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MPLMOD like '" + getForLastModification() + "%'";
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFAttributionPts();
    }

    public String getForIdAttributionPts() {
        return forIdAttributionPts;
    }

    public String getForIdControle() {
        return forIdControle;
    }

    public String getForLastModification() {
        return forLastModification;
    }

    public String getForLastUser() {
        return forLastUser;
    }

    public String getForNumAffilie() {
        return forNumAffilie;
    }

    public String getLikeNumAffilie() {
        return likeNumAffilie;
    }

    public String getLikeUser() {
        return likeUser;
    }

    public boolean isOrderByModification() {
        return orderByModification;
    }

    public void setForIdAttributionPts(String forIdAttributionPts) {
        this.forIdAttributionPts = forIdAttributionPts;
    }

    public void setForIdControle(String forIdControle) {
        this.forIdControle = forIdControle;
    }

    public void setForLastModification(String forLastModification) {
        this.forLastModification = forLastModification;
    }

    public void setForLastUser(String forLastUser) {
        this.forLastUser = forLastUser;
    }

    public void setForNumAffilie(String forNumAffilie) {
        this.forNumAffilie = forNumAffilie;
    }

    public void setLikeNumAffilie(String likeNumAffilie) {
        this.likeNumAffilie = likeNumAffilie;
    }

    public void setLikeUser(String likeUser) {
        this.likeUser = likeUser;
    }

    public void setOrderByModification(boolean orderByModification) {
        this.orderByModification = orderByModification;
    }

}
