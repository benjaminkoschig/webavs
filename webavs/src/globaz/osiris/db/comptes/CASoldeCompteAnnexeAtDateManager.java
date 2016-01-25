package globaz.osiris.db.comptes;

import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;

/**
 * Insérez la description du type ici. Date de création : (10.12.2001 12:59:11)
 * 
 * @author: Administrator
 */
public class CASoldeCompteAnnexeAtDateManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static java.lang.String ORDER_IDCOMPTEANNEXE = "1000";
    private String forDate = new String();
    private String forIdCategorie = new String();
    private String forIdGenreCompte = new String();
    private java.lang.String forSelectionCC = new String();
    private java.lang.String forSelectionRole = new String();
    private java.lang.String forSelectionSigne = new String();
    private java.lang.String forSelectionTri = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String result = _getCollection() + "CACPTAP";

        result += " INNER JOIN " + _getCollection() + "CAOPERP ON " + _getCollection() + "CACPTAP.IDCOMPTEANNEXE = "
                + _getCollection() + "CAOPERP.IDCOMPTEANNEXE ";
        result += " INNER JOIN " + _getCollection() + "CAJOURP ON " + _getCollection() + "CAJOURP.IDJOURNAL = "
                + _getCollection() + "CAOPERP.IDJOURNAL ";
        return result;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {

        if (getForSelectionTri().equals("1")) {
            return _getCollection() + "CACPTAP.IDEXTERNEROLE, " + _getCollection() + "CACPTAP.IDROLE";
        } else if (getForSelectionTri().equals("2")) {
            return _getCollection() + "CACPTAP." + CACompteAnnexe.FIELD_DESCUPCASE;
        } else {
            return _getCollection() + "CACPTAP.IDCOMPTEANNEXE";
        }
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        sqlWhere = _getCollection() + "CAOPERP.ETAT = "
                + this._dbWriteNumeric(statement.getTransaction(), APIOperation.ETAT_COMPTABILISE);
        sqlWhere += " AND (" + _getCollection() + "CAOPERP.idtypeoperation like 'E%'	";
        sqlWhere += " OR " + _getCollection() + "CAOPERP.idtypeoperation like 'A%'	)";

        // Traitement du positionnement pour id compte annexe
        if ((getForDate() != null) && (getForDate().trim().length() != 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CAJOURP.DATEVALEURCG <= "
                    + this._dbWriteDateAMJ(statement.getTransaction(), getForDate());
        }

        // Traitement du positionnement pour idCompteCourant (CompteCourant)
        if ((getForSelectionCC().length() != 0) && !getForSelectionCC().equals("1000")) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CAOPERP.IDCOMPTECOURANT="
                    + this._dbWriteNumeric(statement.getTransaction(), getForSelectionCC());
        }

        // Traitement du positionnement pour une sélection du rôle
        if ((getForSelectionRole().length() != 0) && !getForSelectionRole().equals("1000")) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (getForSelectionRole().indexOf(',') != -1) {
                String[] roles = JadeStringUtil.split(getForSelectionRole(), ',', Integer.MAX_VALUE);

                sqlWhere += _getCollection() + "CACPTAP.IDROLE IN (";

                for (int id = 0; id < roles.length; ++id) {
                    if (id > 0) {
                        sqlWhere += ",";
                    }

                    sqlWhere += this._dbWriteNumeric(statement.getTransaction(), roles[id]);
                }

                sqlWhere += ")";
            } else {
                sqlWhere += _getCollection() + "CACPTAP.IDROLE="
                        + this._dbWriteNumeric(statement.getTransaction(), getForSelectionRole());
            }
        }

        // Traitement du genre de compte
        if (!JadeStringUtil.isBlank(getForIdGenreCompte())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CACPTAP.IDGENRECOMPTE = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdGenreCompte());
        }

        // Traitement de la catégorie du compte
        if ((!JadeStringUtil.isBlank(getForIdCategorie()))
                && (!getForIdCategorie().equals(CACompteAnnexeManager.ALL_CATEGORIE))) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CACPTAP.IDCATEGORIE = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdCategorie());
        }

        sqlWhere += " GROUP BY ";
        sqlWhere += _getCollection() + "CACPTAP.IDEXTERNEROLE, ";
        sqlWhere += _getCollection() + "CACPTAP.DESCRIPTION, ";
        sqlWhere += _getCollection() + "CACPTAP." + CACompteAnnexe.FIELD_DESCUPCASE + ", ";
        sqlWhere += _getCollection() + "CACPTAP.idrole	";

        // Traitement de la sélection du signe du solde
        if (getForSelectionSigne().length() != 0) {

            switch (java.lang.Integer.parseInt(getForSelectionSigne())) {
                case 1:
                    sqlWhere += "HAVING SUM (" + _getCollection() + "CAOPERP.montant)<>0 ";
                    break;
                case 2:
                    sqlWhere += "HAVING SUM (" + _getCollection() + "CAOPERP.montant)>0 ";
                    break;
                case 3:
                    sqlWhere += "HAVING SUM (" + _getCollection() + "CAOPERP.montant)<0 ";
                    break;
                default:
                    break;
            }

        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CASoldeCompteAnnexeAtDate();
    }

    /**
     * Returns the forDate.
     * 
     * @return String
     */
    public String getForDate() {
        return forDate;
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
    public String getForIdGenreCompte() {
        return forIdGenreCompte;
    }

    /**
     * Returns the forSelectionCC.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForSelectionCC() {
        return forSelectionCC;
    }

    /**
     * peut contenir des ','
     * 
     * @return java.lang.String
     */
    public java.lang.String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2002 08:57:25)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForSelectionSigne() {
        return forSelectionSigne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.01.2002 08:12:36)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForSelectionTri() {
        return forSelectionTri;
    }

    /**
     * Sets the forDate.
     * 
     * @param forDate
     *            The forDate to set
     */
    public void setForDate(java.lang.String forDate) {
        this.forDate = forDate;
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
    public void setForIdGenreCompte(String s) {
        forIdGenreCompte = s;
    }

    /**
     * Sets the forSelectionCC.
     * 
     * @param forSelectionCC
     *            The forSelectionCC to set
     */
    public void setForSelectionCC(java.lang.String forSelectionCC) {
        this.forSelectionCC = forSelectionCC;
    }

    /**
     * newForSelectionRole peut contenir des ',' séparant plusieurs id de role, dans ce cas, le manager retourne toutes
     * les operations pour un compte annexe dont l'id du role est l'un de ceux transmis dans newForSelectionRole.
     * 
     * @param newForSelectionRole
     *            java.lang.String
     */
    public void setForSelectionRole(java.lang.String newForSelectionRole) {
        forSelectionRole = newForSelectionRole;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2002 08:57:25)
     * 
     * @param newForSelectionSigne
     *            java.lang.String
     */
    public void setForSelectionSigne(java.lang.String newForSelectionSigne) {
        forSelectionSigne = newForSelectionSigne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.01.2002 08:12:36)
     * 
     * @param newForSelectionTri
     *            java.lang.String
     */
    public void setForSelectionTri(java.lang.String newForSelectionTri) {
        forSelectionTri = newForSelectionTri;
    }

}
