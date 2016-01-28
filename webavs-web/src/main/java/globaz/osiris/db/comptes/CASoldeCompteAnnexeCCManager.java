package globaz.osiris.db.comptes;

import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import java.io.Serializable;

/**
 * Insérez la description du type ici. Date de création : (10.12.2001 12:59:11)
 * 
 * @author: Administrator
 */
public class CASoldeCompteAnnexeCCManager extends BManager implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String ORDER_BY = " ORDER BY ";

    public final static String ORDER_IDCOMPTEANNEXE = "1000";

    private String forIdCategorie = new String();

    private String forIdCompteAnnexe = new String();
    private String forIdGenreCompte = new String();
    private String forSelectionCC = new String();
    private String forSelectionCompte = new String();
    private String forSelectionRole = new String();
    private String forSelectionSigne = new String();
    private String forSelectionTri = new String();
    public String orderBy = new String();

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getSql(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getSql(BStatement statement) {
        String sql = "";

        sql += "SELECT a." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + ", a." + CACompteAnnexe.FIELD_DESCRIPTION + ", a."
                + CACompteAnnexe.FIELD_IDEXTERNEROLE + ", a." + CACompteAnnexe.FIELD_IDROLE + ", b."
                + CAOperation.FIELD_IDCOMPTECOURANT + ", sum(b." + CAOperation.FIELD_MONTANT + ") as "
                + CAOperation.FIELD_MONTANT;
        sql += " FROM " + _getCollection() + CACompteAnnexe.TABLE_CACPTAP + " a, " + _getCollection()
                + CAOperation.TABLE_CAOPERP + " b";

        sql += getWhere(statement);

        sql += " GROUP BY a." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + ", a." + CACompteAnnexe.FIELD_DESCRIPTION
                + ", a." + CACompteAnnexe.FIELD_IDEXTERNEROLE + ", a." + CACompteAnnexe.FIELD_IDROLE + ", b."
                + CAOperation.FIELD_IDCOMPTECOURANT;

        sql += getOrder();

        return sql;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CASoldeCompteAnnexeCC();
    }

    /**
     * @return
     */
    public String getForIdCategorie() {
        return forIdCategorie;
    }

    /**
     * @return
     */
    public String getForIdCompteAnnexe() {
        return forIdCompteAnnexe;
    }

    /**
     * @return
     */
    public String getForIdGenreCompte() {
        return forIdGenreCompte;
    }

    /**
     * Returns the forSelectionCC.
     * 
     * @return String
     */
    public String getForSelectionCC() {
        return forSelectionCC;
    }

    /**
     * @return
     */
    public String getForSelectionCompte() {
        return forSelectionCompte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.01.2002 16:02:21)
     * 
     * @return String
     */
    public String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2002 08:57:25)
     * 
     * @return String
     */
    public String getForSelectionSigne() {
        return forSelectionSigne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.01.2002 08:12:36)
     * 
     * @return String
     */
    public String getForSelectionTri() {
        return forSelectionTri;
    }

    /**
     * Retourne la clause ORDER BY de la requete SQL.
     * 
     * @return
     */
    private String getOrder() {
        if (getForSelectionTri().equals("1")) {
            return CASoldeCompteAnnexeCCManager.ORDER_BY + "a.IDEXTERNEROLE, a.IDROLE";
        } else if (getForSelectionTri().equals("2")) {
            return CASoldeCompteAnnexeCCManager.ORDER_BY + "a.DESCRIPTION";
        } else if (getOrderBy().equals(CASoldeCompteAnnexeCCManager.ORDER_IDCOMPTEANNEXE)) {
            return CASoldeCompteAnnexeCCManager.ORDER_BY + "a.IDCOMPTEANNEXE";
        }

        return "";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.03.2002 10:17:52)
     * 
     * @return String
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * Retourne la clause WHERE de la requete SQL.
     * 
     * @param statement
     * @return
     */
    private String getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // Traitement du positionnement pour une sélection du rôle
        if ((getForSelectionRole().length() != 0) && !getForSelectionRole().equals("1000")) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (getForSelectionRole().indexOf(',') != -1) {
                String[] roles = JadeStringUtil.split(getForSelectionRole(), ',', Integer.MAX_VALUE);

                sqlWhere += "a." + CACompteAnnexe.FIELD_IDROLE + " IN (";

                for (int id = 0; id < roles.length; ++id) {
                    if (id > 0) {
                        sqlWhere += ",";
                    }
                    sqlWhere += this._dbWriteNumeric(statement.getTransaction(), roles[id]);
                }

                sqlWhere += ")";
            } else {
                sqlWhere += "a." + CACompteAnnexe.FIELD_IDROLE + "="
                        + this._dbWriteNumeric(statement.getTransaction(), getForSelectionRole());
            }
        }

        // Traitement de la sélection du signe du solde
        if (getForSelectionSigne().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            switch (java.lang.Integer.parseInt(getForSelectionSigne())) {
                case 1:
                    sqlWhere += "a." + CACompteAnnexe.FIELD_SOLDE + " <> 0";
                    break;
                case 2:
                    sqlWhere += "a." + CACompteAnnexe.FIELD_SOLDE + " > 0";
                    break;
                case 3:
                    sqlWhere += "a." + CACompteAnnexe.FIELD_SOLDE + " < 0";
                    break;
                default:
                    break;
            }

        }

        // Sélections des comptes ouvert ou soldés.
        if ((getForSelectionCompte().length() != 0) && !getForSelectionCompte().equals("1000")) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            switch (java.lang.Integer.parseInt(getForSelectionCompte())) {
                case 1:
                    sqlWhere += "a." + CACompteAnnexe.FIELD_SOLDE + " <> 0";
                    break;
                case 2:
                    sqlWhere += "a." + CACompteAnnexe.FIELD_SOLDE + " = 0";
                    break;
                default:
                    break;
            }
        }

        // Traitement du positionnement pour une sélection du compte courant
        if ((getForSelectionCC().length() != 0) && !getForSelectionCC().equals("1000")) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "b." + CAOperation.FIELD_IDCOMPTECOURANT + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForSelectionCC());
        }

        // Traitement du genre de compte
        if (!JadeStringUtil.isBlank(getForIdGenreCompte())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "a." + CACompteAnnexe.FIELD_IDGENRECOMPTE + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdGenreCompte());
        }

        // Traitement de la catégorie du compte
        if ((!JadeStringUtil.isBlank(getForIdCategorie()))
                && (!getForIdCategorie().equals(CACompteAnnexeManager.ALL_CATEGORIE))) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "a." + CACompteAnnexe.FIELD_IDCATEGORIE + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdCategorie());
        }

        if (!JadeStringUtil.isBlank(getForIdCompteAnnexe())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "a." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdCompteAnnexe());
        }

        if (sqlWhere.length() != 0) {
            sqlWhere += " AND ";
        }
        sqlWhere += "a." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + " = b." + CAOperation.FIELD_IDCOMPTEANNEXE;
        sqlWhere += " AND b." + CAOperation.FIELD_ETAT + " = " + APIOperation.ETAT_COMPTABILISE;
        sqlWhere += " AND b." + CAOperation.FIELD_IDCOMPTECOURANT + " > 0 ";

        return " WHERE " + sqlWhere;
    }

    /**
     * @param string
     */
    public void setForIdCategorie(String s) {
        forIdCategorie = s;
    }

    /**
     * @param string
     */
    public void setForIdCompteAnnexe(String string) {
        forIdCompteAnnexe = string;
    }

    /**
     * @param string
     */
    public void setForIdGenreCompte(String s) {
        forIdGenreCompte = s;
    }

    /**
     * Sets the forSelectionCC.
     * 
     * @param forSelectionCC
     *            The forSelectionCC to set
     */
    public void setForSelectionCC(String forSelectionCC) {
        this.forSelectionCC = forSelectionCC;
    }

    /**
     * @param string
     */
    public void setForSelectionCompte(String string) {
        forSelectionCompte = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.01.2002 16:02:21)
     * 
     * @param newForSelectionRole
     *            String
     */
    public void setForSelectionRole(String newForSelectionRole) {
        forSelectionRole = newForSelectionRole;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2002 08:57:25)
     * 
     * @param newForSelectionSigne
     *            String
     */
    public void setForSelectionSigne(String newForSelectionSigne) {
        forSelectionSigne = newForSelectionSigne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.01.2002 08:12:36)
     * 
     * @param newForSelectionTri
     *            String
     */
    public void setForSelectionTri(String newForSelectionTri) {
        forSelectionTri = newForSelectionTri;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.03.2002 10:17:52)
     * 
     * @param newOrderBy
     *            String
     */
    public void setOrderBy(String newOrderBy) {
        orderBy = newOrderBy;
    }

}
