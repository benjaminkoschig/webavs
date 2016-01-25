package globaz.osiris.db.comptes;

/**
 * Insérez la description du type ici. Date de création : (30.04.2002 15:09:00)
 * 
 * @author: Administrator
 */
public class CASoldeSectionManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdTypeSection = new String();
    private java.lang.String forSelectionRole = new String();
    private java.lang.String forSelectionSigne = new String();
    private java.lang.String forSelectionTriCA = new String();
    private java.lang.String forSelectionTriSection = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        // return _getCollection() + "CASECTV1";
        return _getCollection() + CASection.TABLE_CASECTP + " INNER JOIN " + _getCollection()
                + CACompteAnnexe.TABLE_CACPTAP + " ON " + _getCollection() + CASection.TABLE_CASECTP + "."
                + CASection.FIELD_IDCOMPTEANNEXE + "=" + _getCollection() + CACompteAnnexe.TABLE_CACPTAP + "."
                + CACompteAnnexe.FIELD_IDCOMPTEANNEXE;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {

        String vOrdre = new String();

        // Traitement du positionnement d'un compte annexe par numéro ou par nom

        switch (java.lang.Integer.parseInt(getForSelectionTriCA())) {
            case 1:
                vOrdre += CACompteAnnexe.FIELD_IDROLE + ", " + CACompteAnnexe.FIELD_IDEXTERNEROLE;
                break;
            case 2:
                vOrdre += CACompteAnnexe.FIELD_DESCUPCASE;
                break;
            default:
                break;
        }
        vOrdre += ", ";

        // Traitement du positionnement d'une section par id externe ou par date

        switch (java.lang.Integer.parseInt(getForSelectionTriSection())) {
            case 1:
                vOrdre += CASection.FIELD_IDEXTERNE;
                break;
            case 2:
                vOrdre += CASection.FIELD_DATESECTION;
                break;
            default:
                break;
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
            sqlWhere += CACompteAnnexe.FIELD_IDROLE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForSelectionRole());
        }

        // traitement du positionnement
        if ((getForIdTypeSection().length() != 0) && !getForIdTypeSection().equals("1000")) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CASection.FIELD_IDTYPESECTION + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdTypeSection());
        }

        // Traitement de la sélection du signe du solde
        if (getForSelectionSigne().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            switch (java.lang.Integer.parseInt(getForSelectionSigne())) {
                case 1:
                    sqlWhere += _getCollection() + CASection.FIELD_SOLDE + " <> 0";
                    break;
                case 2:
                    sqlWhere += _getCollection() + CASection.FIELD_SOLDE + " > 0";
                    break;
                case 3:
                    sqlWhere += _getCollection() + CASection.FIELD_SOLDE + " < 0";
                    break;
                default:
                    break;
            }

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
     * Insérez la description de la méthode ici. Date de création : (30.04.2002 15:20:41)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdTypeSection() {
        return forIdTypeSection;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.05.2002 09:02:53)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.05.2002 11:40:12)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForSelectionSigne() {
        return forSelectionSigne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.05.2002 09:38:59)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForSelectionTriCA() {
        return forSelectionTriCA;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.05.2002 09:39:20)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForSelectionTriSection() {
        return forSelectionTriSection;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2002 15:20:41)
     * 
     * @param newForIdTypeSection
     *            java.lang.String
     */
    public void setForIdTypeSection(java.lang.String newForIdTypeSection) {
        forIdTypeSection = newForIdTypeSection;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.05.2002 09:02:53)
     * 
     * @param newForSelectionRole
     *            java.lang.String
     */
    public void setForSelectionRole(java.lang.String newForSelectionRole) {
        forSelectionRole = newForSelectionRole;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.05.2002 11:40:12)
     * 
     * @param newForSelectionSigne
     *            java.lang.String
     */
    public void setForSelectionSigne(java.lang.String newForSelectionSigne) {
        forSelectionSigne = newForSelectionSigne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.05.2002 09:38:59)
     * 
     * @param newForSelectionTriCA
     *            java.lang.String
     */
    public void setForSelectionTriCA(java.lang.String newForSelectionTriCA) {
        forSelectionTriCA = newForSelectionTriCA;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.05.2002 09:39:20)
     * 
     * @param newForSelectionTriSection
     *            java.lang.String
     */
    public void setForSelectionTriSection(java.lang.String newForSelectionTriSection) {
        forSelectionTriSection = newForSelectionTriSection;
    }
}
