package globaz.osiris.db.contentieux;

import globaz.globall.db.BConstants;
import globaz.globall.db.BManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CASection;

/**
 * @author sel Créé le 19 sept. 06
 */
public class CASectionsBloqueesContentieuxManager extends BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean forContentieuxEstSuspendu = false;
    private String forDateReferenceBlocage;
    private String forIdCategorie;
    private String forIdGenreCompte;
    private String forIdMotConSus;
    private String forIdTypeSection;
    private String forSelectionCompte; // Solde
    private String forSelectionRole;
    private String forSelectionTriCA;
    private String forSelectionTriSection;

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + CASection.TABLE_CASECTP + " INNER JOIN " + _getCollection() + "CACPTAP ON "
                + _getCollection() + "CASECTP.IDCOMPTEANNEXE=" + _getCollection() + "CACPTAP.IDCOMPTEANNEXE";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {

        String vOrdre = "";

        // Traitement du positionnement d'un compte annexe par numéro ou par nom
        switch (Integer.parseInt(getForSelectionTriCA())) {
            case 1:
                vOrdre += "IDROLE, IDEXTERNEROLE";
                break;
            case 2:
                vOrdre += CACompteAnnexe.FIELD_DESCUPCASE;
                break;
            default:
                break;
        }
        vOrdre += ", ";

        // Traitement du positionnement d'une section par id externe ou par date
        switch (Integer.parseInt(getForSelectionTriSection())) {
            case 1:
                vOrdre += "IDEXTERNE";
                break;
            case 2:
                vOrdre += "DATE";
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

        // Traitement des contentieux suspendus
        if (isForContentieuxEstSuspendu()) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += CASection.FIELD_CONTENTIEUXESTSUS + " LIKE '" + BConstants.DB_BOOLEAN_TRUE + "'";
        }

        // Traitement du positionnement pour une selection de section
        if (!JadeStringUtil.isBlank(getForIdTypeSection()) && !getForIdTypeSection().equals("1000")) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDTYPESECTION=" + this._dbWriteNumeric(statement.getTransaction(), getForIdTypeSection());
        }

        // Traitement du positionnement pour une sélection du rôle
        if ((!JadeStringUtil.isBlank(getForSelectionRole())) && !getForSelectionRole().equals("1000")) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            if (getForSelectionRole().indexOf(',') != -1) {
                String[] roles = JadeStringUtil.split(getForSelectionRole(), ',', Integer.MAX_VALUE);

                sqlWhere += "IDROLE IN (";

                for (int idRole = 0; idRole < roles.length; ++idRole) {
                    if (idRole > 0) {
                        sqlWhere += ",";
                    }
                    sqlWhere += this._dbWriteNumeric(statement.getTransaction(), roles[idRole]);
                }

                sqlWhere += ")";
            } else {
                sqlWhere += "IDROLE=" + this._dbWriteNumeric(statement.getTransaction(), getForSelectionRole());
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
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            if (getForIdCategorie().indexOf(',') != -1) {
                String[] categories = JadeStringUtil.split(getForIdCategorie(), ',', Integer.MAX_VALUE);

                sqlWhere += "IDCATEGORIE IN (";

                for (int id = 0; id < categories.length; ++id) {
                    if (id > 0) {
                        sqlWhere += ",";
                    }
                    sqlWhere += this._dbWriteNumeric(statement.getTransaction(), categories[id]);
                }
                sqlWhere += ")";
            } else {
                sqlWhere += "IDCATEGORIE = " + this._dbWriteNumeric(statement.getTransaction(), getForIdCategorie());
            }
        }

        // traitement du motif de blocage
        if (!JadeStringUtil.isBlank(getForIdMotConSus())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDMOTCONSUS=" + this._dbWriteNumeric(statement.getTransaction(), getForIdMotConSus());
        }

        // Traitement du positionnement pour une sélection des comptes (solde)
        if ((!JadeStringUtil.isBlank(getForSelectionCompte())) && !getForSelectionCompte().equals("1000")) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            switch (java.lang.Integer.parseInt(getForSelectionCompte())) {
                case 1:
                    sqlWhere += _getCollection() + "CASECTP.SOLDE <> 0";
                    break;
                case 2:
                    sqlWhere += _getCollection() + "CASECTP.SOLDE = 0";
                    break;
                default:
                    break;
            }
        }

        // filtre sur la période blocage
        if (!JadeStringUtil.isBlank(getForDateReferenceBlocage())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "( (" + getForDateReferenceBlocage() + " >= CONTDATEDEBBLOQUE OR CONTDATEDEBBLOQUE=0) "
                    + "AND (" + getForDateReferenceBlocage() + " <= CONTDATEFINBLOQUE OR CONTDATEFINBLOQUE=0) )";
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
     * @author: sel Créé le : 27 sept. 06
     * @return
     */
    public String getForDateReferenceBlocage() {
        return forDateReferenceBlocage;
    }

    /**
     * @author: sel Créé le : 27 sept. 06
     * @return
     */
    public String getForIdCategorie() {
        return forIdCategorie;
    }

    /**
     * @author: sel Créé le : 27 sept. 06
     * @return
     */
    public String getForIdGenreCompte() {
        return forIdGenreCompte;
    }

    /**
     * @author: sel Créé le : 26 sept. 06
     * @return
     */
    public String getForIdMotConSus() {
        return forIdMotConSus;
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
     * @author: sel Créé le : 27 sept. 06
     * @return
     */
    public String getForSelectionCompte() {
        return forSelectionCompte;
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
     * @author: sel Créé le : 25 sept. 06
     * @return
     */
    public boolean isForContentieuxEstSuspendu() {
        return forContentieuxEstSuspendu;
    }

    /**
     * @author: sel Créé le : 25 sept. 06
     * @param b
     */
    public void setForContentieuxEstSuspendu(boolean b) {
        forContentieuxEstSuspendu = b;
    }

    /**
     * @author: sel Créé le : 27 sept. 06
     * @param string
     */
    public void setForDateReferenceBlocage(String string) {
        forDateReferenceBlocage = string;
    }

    /**
     * @author: sel Créé le : 27 sept. 06
     * @param string
     */
    public void setForIdCategorie(String string) {
        forIdCategorie = string;
    }

    /**
     * @author: sel Créé le : 27 sept. 06
     * @param string
     */
    public void setForIdGenreCompte(String string) {
        forIdGenreCompte = string;
    }

    /**
     * @author: sel Créé le : 26 sept. 06
     * @param string
     */
    public void setForIdMotConSus(String string) {
        forIdMotConSus = string;
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
     * @author: sel Créé le : 27 sept. 06
     * @param string
     */
    public void setForSelectionCompte(String string) {
        forSelectionCompte = string;
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
