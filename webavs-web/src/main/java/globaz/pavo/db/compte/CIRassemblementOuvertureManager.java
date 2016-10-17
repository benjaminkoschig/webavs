package globaz.pavo.db.compte;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JAUtil;
import globaz.pavo.application.CIApplication;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager de <tt>CIRassemblementOuverture</tt>. Date de création : (12.11.2002 13:27:56)
 * 
 * @author: David Girardin
 */
public class CIRassemblementOuvertureManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (KAIIND) */
    private String forCompteIndividuelId = new String();
    /** (KKDCLO) */
    private String forDateCloture = new String();
    private String forDateOrdre = new String();
    /** (KKTARC) */
    private String forMotifArc = new String();
    private String forTypeEnregistrement = new String();
    private List<String> forNotInTypeEnregistrement = new ArrayList<String>();
    private String fromDateCloture = new String();
    private String fromDateOrdre = new String();
    private String order = new String();
    private String untilDateOrdre = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CIRAOUP";
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
        // traitement du positionnement
        if (getForCompteIndividuelId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KAIIND=" + _dbWriteNumeric(statement.getTransaction(), getForCompteIndividuelId());
        }
        // traitement du positionnement
        if (getForMotifArc().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KKTARC=" + _dbWriteNumeric(statement.getTransaction(), getForMotifArc());
        }
        // traitement du positionnement
        if (getForDateCloture().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KKDCLO=" + _dbWriteDateAMJ(statement.getTransaction(), getForDateCloture());
        }
        if (getFromDateCloture().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KKDCLO>=" + _dbWriteDateAMJ(statement.getTransaction(), getFromDateCloture());
        }
        if (getForDateOrdre().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KKDORD=" + _dbWriteDateAMJ(statement.getTransaction(), getForDateOrdre());
        }
        if (getForTypeEnregistrement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            try {
                CIApplication app = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                        CIApplication.DEFAULT_APPLICATION_PAVO);
                if (app.isAnnoncesWA()) {
                    sqlWhere += "substr(cast(KKTENR as char(6)),5,2)="
                            + _dbWriteString(statement.getTransaction(), getForTypeEnregistrement().substring(4));
                } else {
                    sqlWhere += "KKTENR =" + _dbWriteNumeric(statement.getTransaction(), getForTypeEnregistrement());
                }
            } catch (Exception e) {
            }
        }

        if (!getForNotInTypeEnregistrement().isEmpty()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            try {
                CIApplication app = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                        CIApplication.DEFAULT_APPLICATION_PAVO);

                if (app.isAnnoncesWA()) {
                    StringBuilder builder = new StringBuilder("substr(cast(KKTENR as char(6)),5,2) NOT IN (");

                    boolean firstWay = true;
                    for (String type : getForNotInTypeEnregistrement()) {
                        if (firstWay) {
                            builder.append(_dbWriteString(statement.getTransaction(), type.substring(4)));
                            firstWay = false;
                        } else {
                            builder.append(",");
                            builder.append(_dbWriteString(statement.getTransaction(), type.substring(4)));
                        }
                    }
                    builder.append(") ");

                    sqlWhere += builder.toString();
                } else {
                    StringBuilder builder = new StringBuilder("KKTENR NOT IN (");

                    boolean firstWay = true;
                    for (String type : getForNotInTypeEnregistrement()) {
                        if (firstWay) {
                            builder.append(_dbWriteNumeric(statement.getTransaction(), type));
                            firstWay = false;
                        } else {
                            builder.append(",");
                            builder.append(_dbWriteNumeric(statement.getTransaction(), type));
                        }
                    }
                    builder.append(") ");

                    sqlWhere += builder.toString();
                }
            } catch (Exception e) {
            }
        }

        if (getFromDateOrdre().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KKDORD >= " + _dbWriteDateAMJ(statement.getTransaction(), getFromDateOrdre());
        }
        if (getUntilDateOrdre().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KKDORD <= " + _dbWriteDateAMJ(statement.getTransaction(), getUntilDateOrdre());
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
        return new CIRassemblementOuverture();
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForCompteIndividuelId() {
        return forCompteIndividuelId;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.12.2002 10:38:34)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForDateCloture() {
        return forDateCloture;
    }

    /**
     * Returns the fromDateOrdre.
     * 
     * @return String
     */
    public String getForDateOrdre() {
        return forDateOrdre;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.12.2002 10:38:34)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForMotifArc() {
        return forMotifArc;
    }

    /**
     * Returns the forTypeEnregistrement.
     * 
     * @return String
     */
    public String getForTypeEnregistrement() {
        return forTypeEnregistrement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.01.2003 13:42:35)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromDateCloture() {
        return fromDateCloture;
    }

    /**
     * @return
     */
    public String getFromDateOrdre() {
        return fromDateOrdre;
    }

    /**
     * @return
     */
    public String getUntilDateOrdre() {
        return untilDateOrdre;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */

    public void setForCompteIndividuelId(String newForCompteIndividuelId) {
        forCompteIndividuelId = newForCompteIndividuelId;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.12.2002 10:38:34)
     * 
     * @param newForDateCloture
     *            java.lang.String
     */
    public void setForDateCloture(java.lang.String newForDateCloture) {
        forDateCloture = newForDateCloture;
    }

    /**
     * Sets the fromDateOrdre.
     * 
     * @param fromDateOrdre
     *            The fromDateOrdre to set
     */
    public void setForDateOrdre(String fromDateOrdre) {
        forDateOrdre = fromDateOrdre;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.12.2002 10:38:34)
     * 
     * @param newForMotifArc
     *            java.lang.String
     */
    public void setForMotifArc(java.lang.String newForMotifArc) {
        forMotifArc = newForMotifArc;
    }

    /**
     * Sets the forTypeEnregistrement.
     * 
     * @param forTypeEnregistrement
     *            The forTypeEnregistrement to set
     */
    public void setForTypeEnregistrement(String forTypeEnregistrement) {
        this.forTypeEnregistrement = forTypeEnregistrement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.01.2003 13:42:35)
     * 
     * @param newUntilDateCloture
     *            java.lang.String
     */
    public void setFromDateCloture(java.lang.String newFromDateCloture) {
        fromDateCloture = newFromDateCloture;
    }

    /**
     * @param string
     */
    public void setFromDateOrdre(String string) {
        fromDateOrdre = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.01.2003 13:49:38)
     */
    public void setOrderByDateCloture(boolean ascending) {
        if (JAUtil.isStringEmpty(order)) {
            order = "KKDCLO";
        } else {
            order += ",KKDCLO";
        }
        if (!ascending) {
            order += " DESC";
        }
    }

    public void setOrderByDateOrdre() {
        if (JAUtil.isStringEmpty(order)) {
            order = "KKDORD";
        } else {
            order += "KKDORD";
        }
    }

    public void setOrderByOrdre() {
        if (JAUtil.isStringEmpty(order)) {
            order = "KKDORD,KKTENR";
        } else {
            order += ",KKDORD,KKTENR";
        }
    }

    /**
     * @param string
     */
    public void setUntilDateOrdre(String string) {
        untilDateOrdre = string;
    }

    public void setForNotInTypeEnregistrement(List<String> types) {
        forNotInTypeEnregistrement = types;
    }

    public List<String> getForNotInTypeEnregistrement() {
        return forNotInTypeEnregistrement;
    }

}
