package globaz.osiris.print.itext.list;

import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;

/**
 * Cette classe prépare le manager pour l'impression de la liste des soldes par section
 * 
 * @author: Sébastien Chappatte
 */
public class CAIListSoldeSectionManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String TYPE_ID_PARAMETRE_ETAPE_ORDRE = "2";
    /** Tri du manager selon le type de section */
    public final static String TYPE_SECTION_ORDRE = "1";
    private String forIdCategorie = new String();
    private String forIdGenreCompte = new String();
    private java.lang.String forIdTypeSection = new String();
    private java.lang.String forSelectionRole = new String();
    private java.lang.String forSelectionSigne = new String();
    private java.lang.String forSelectionTriCA = new String();

    private java.lang.String forSelectionTriSection = new String();
    private java.lang.String orderBy = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        // return _getCollection() + "CASECTV1";
        if (!JadeStringUtil.isBlank(getOrderBy())) {
            return _getCollection() + "CASECTP INNER JOIN " + _getCollection() + "CACPTAP ON " + _getCollection()
                    + "CASECTP.IDCOMPTEANNEXE=" + _getCollection() + "CACPTAP.IDCOMPTEANNEXE " + "LEFT OUTER JOIN "
                    + _getCollection() + "CAEVCTP ON " + _getCollection() + "CASECTP.IDSECTION=" + _getCollection()
                    + "CAEVCTP.IDSECTION";
        }
        return _getCollection() + "CASECTP INNER JOIN " + _getCollection() + "CACPTAP ON " + _getCollection()
                + "CASECTP.IDCOMPTEANNEXE=" + _getCollection() + "CACPTAP.IDCOMPTEANNEXE";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {

        String vOrdre = new String();

        // Traitement du positionnement par type de section

        if (!JadeStringUtil.isBlank(getOrderBy())) {
            switch (java.lang.Integer.parseInt(getOrderBy())) {
                case 1:
                    vOrdre += "IDTYPESECTION";
                    break;
                case 2:
                    vOrdre += "IDPARAMETREETAPE";
                    break;
                default:
                    break;
            }
        }
        // Traitement du positionnement d'un compte annexe par numéro ou par nom

        if (!JadeStringUtil.isBlank(getForSelectionTriCA())) {
            switch (java.lang.Integer.parseInt(getForSelectionTriCA())) {
                case 1:
                    if (vOrdre.length() != 0) {
                        vOrdre += ", ";
                    }
                    vOrdre += "IDROLE, IDEXTERNEROLE";
                    break;
                case 2:
                    if (vOrdre.length() != 0) {
                        vOrdre += ", ";
                    }
                    vOrdre += CACompteAnnexe.FIELD_DESCUPCASE;
                    break;
                default:
                    break;
            }
        }

        // Traitement du positionnement d'une section par id externe ou par date

        if (!JadeStringUtil.isBlank(getForSelectionTriSection())) {
            switch (java.lang.Integer.parseInt(getForSelectionTriSection())) {
                case 1:
                    if (vOrdre.length() != 0) {
                        vOrdre += ", ";
                    }
                    vOrdre += "IDEXTERNE";
                    break;
                case 2:
                    if (vOrdre.length() != 0) {
                        vOrdre += ", ";
                    }
                    vOrdre += "DATE";
                    break;
                default:
                    break;
            }
        }
        return vOrdre;
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du role
        if ((getForSelectionRole().length() != 0) && !getForSelectionRole().equals("1000")) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (getForSelectionRole().indexOf(',') != -1) {
                String[] roles = JadeStringUtil.split(getForSelectionRole(), ',', Integer.MAX_VALUE);

                sqlWhere += "IDROLE IN (";

                for (int id = 0; id < roles.length; ++id) {
                    if (id > 0) {
                        sqlWhere += ",";
                    }
                    sqlWhere += this._dbWriteNumeric(statement.getTransaction(), roles[id]);
                }

                sqlWhere += ")";
            } else {
                sqlWhere += "IDROLE=" + this._dbWriteNumeric(statement.getTransaction(), getForSelectionRole());
            }
        }

        // traitement du positionnement
        if ((getForIdTypeSection().length() != 0) && !getForIdTypeSection().equals("1000")) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDTYPESECTION=" + this._dbWriteNumeric(statement.getTransaction(), getForIdTypeSection());
        }

        // Traitement de la sélection du signe du solde
        if (getForSelectionSigne().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            switch (java.lang.Integer.parseInt(getForSelectionSigne())) {
                case 1:
                    sqlWhere += _getCollection() + "CASECTP.SOLDE <> 0";
                    break;
                case 2:
                    sqlWhere += _getCollection() + "CASECTP.SOLDE > 0";
                    break;
                case 3:
                    sqlWhere += _getCollection() + "CASECTP.SOLDE < 0";
                    break;
                default:
                    break;
            }

        }

        // Traitement du genre de compte
        if (!JadeStringUtil.isBlank(getForIdGenreCompte())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDGENRECOMPTE = " + this._dbWriteNumeric(statement.getTransaction(), getForIdGenreCompte());
        }

        // Traitement de la catégorie du compte
        if ((!JadeStringUtil.isBlank(getForIdCategorie()))
                && (!getForIdCategorie().equals(CACompteAnnexeManager.ALL_CATEGORIE))) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCATEGORIE = " + this._dbWriteNumeric(statement.getTransaction(), getForIdCategorie());
        }

        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité
     * 
     * @return la nouvelle entité
     * @exception java.lang.Exception
     *                si la création a échouée
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new globaz.osiris.db.comptes.CASection();
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
     * Cette méthode retourne l'idTypeSection dans la clause where (for)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdTypeSection() {
        return forIdTypeSection;
    }

    /**
     * peut contenir des ','
     * 
     * @see #setForSelectionRole(java.lang.String)
     * @return java.lang.String
     */
    public java.lang.String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * Cette méthode retourne la sélection du signe dans la clause where (for)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForSelectionSigne() {
        return forSelectionSigne;
    }

    /**
     * Cette méthode retourne la sélection du tri de compte annexe dans la clause where (for)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForSelectionTriCA() {
        return forSelectionTriCA;
    }

    /**
     * Cette méthode retourne la sélection du tri de la section dans la clause where (for)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForSelectionTriSection() {
        return forSelectionTriSection;
    }

    /**
     * Returns the orderBy.
     * 
     * @return java.lang.String
     */
    public java.lang.String getOrderBy() {
        return orderBy;
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
     * Cette méthode définit la valeur de la propriété forIdTypeSection
     * 
     * @param newForIdTypeSection
     *            java.lang.String
     */
    public void setForIdTypeSection(java.lang.String newForIdTypeSection) {
        forIdTypeSection = newForIdTypeSection;
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
     * Cette méthode définit la valeur de la propriété forSelectionSigne
     * 
     * @param newForSelectionSigne
     *            java.lang.String
     */
    public void setForSelectionSigne(java.lang.String newForSelectionSigne) {
        forSelectionSigne = newForSelectionSigne;
    }

    /**
     * Cette méthode définit la valeur de la propriété forSelectionTriCA
     * 
     * @param newForSelectionTriCA
     *            java.lang.String
     */
    public void setForSelectionTriCA(java.lang.String newForSelectionTriCA) {
        forSelectionTriCA = newForSelectionTriCA;
    }

    /**
     * Cette méthode définit la valeur de la propriété forSelectionTriSection
     * 
     * @param newForSelectionTriSection
     *            java.lang.String
     */
    public void setForSelectionTriSection(java.lang.String newForSelectionTriSection) {
        forSelectionTriSection = newForSelectionTriSection;
    }

    /**
     * Sets the orderBy.
     * 
     * @param orderBy
     *            The orderBy to set
     */
    public void setOrderBy(java.lang.String orderBy) {
        this.orderBy = orderBy;
    }

}
