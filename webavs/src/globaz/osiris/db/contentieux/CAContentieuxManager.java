package globaz.osiris.db.contentieux;

import globaz.globall.db.BConstants;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CASection;
import java.util.List;

/**
 * Insérez la description du type ici. Date de création : (03.06.2002 09:13:52)
 * 
 * @author: Administrator
 */
public class CAContentieuxManager extends globaz.globall.db.BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateEcheance = new String();
    private String forDateReference = new String();
    private String forIdCategorie = new String();
    private String forIdExterneLike = new String();
    private String forIdGenreCompte = new String();
    private String forIdSequenceContentieux = new String();
    private String forNotIdContMotifBloque = new String();
    private List forRoles = null;
    private boolean forSoldePositifOnly = true;
    private String forTriListeCA = new String();

    private String forTriListeSection = new String();

    private List forTypeSections = null;
    private String fromIdExterne = new String();

    private String fromIdExterneRole = new String();
    private String untilIdExterne = new String();
    private String untilIdExterneRole = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CASECTP INNER JOIN " + _getCollection() + "CACPTAP ON " + _getCollection()
                + "CASECTP.IDCOMPTEANNEXE=" + _getCollection() + "CACPTAP.IDCOMPTEANNEXE";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {

        String _order = "";

        // Traitement du tri du compte annexe de la liste et des documents

        if (getForTriListeCA().length() != 0) {
            switch (java.lang.Integer.parseInt(getForTriListeCA())) {
                case 1:
                    _order += CACompteAnnexe.FIELD_DESCUPCASE + ", " + _getCollection() + "CASECTP.IDCOMPTEANNEXE";
                    break;
                case 2:
                    _order += "IDROLE, IDEXTERNEROLE";
                    break;
                default:
                    break;
            }
        }

        // Traitement du tri de la section de la liste et des documents

        if (getForTriListeSection().length() != 0) {
            _order += ", ";
            switch (java.lang.Integer.parseInt(getForTriListeSection())) {
                case 1:
                    _order += "IDEXTERNE";
                    break;
                case 2:
                    _order += "DATE";
                    break;
                default:
                    break;
            }
        } else {
            // Pour les autres cas
            _order += "IDEXTERNE";
        }

        return _order;
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        sqlWhere += _getCollection() + "CASECTP." + CASection.FIELD_CATEGORIESECTION + " <> "
                + APISection.ID_CATEGORIE_SECTION_DECOMPTE_PCG;

        // traitement du solde > 0
        if (isForSoldePositifOnly()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CASECTP.SOLDE > 0 AND " + _getCollection() + "CASECTP."
                    + CASection.FIELD_IDMODECOMPENSATION + " <> " + APISection.MODE_REPORT;
        }

        // traitement de la séquence
        if ((getForIdSequenceContentieux() != null) && !getForIdSequenceContentieux().equalsIgnoreCase("-1")) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDSEQCON=" + this._dbWriteNumeric(statement.getTransaction(), getForIdSequenceContentieux());
        }

        // traitement du vecteur de type de section
        if (getForTypeSections() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDTYPESECTION IN (";
            for (int i = 0; i < getForTypeSections().size(); i++) {
                sqlWhere += getForTypeSections().get(i);
                if (i != getForTypeSections().size() - 1) {
                    sqlWhere += ", ";
                }
            }
            sqlWhere += ")";
        }

        // traitement du vecteur de roles
        if (getForRoles() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDROLE IN (";
            for (int i = 0; i < getForRoles().size(); i++) {
                sqlWhere += getForRoles().get(i);
                if (i != getForRoles().size() - 1) {
                    sqlWhere += ", ";
                }
            }
            sqlWhere += ")";
        }

        // traitement du contentieux suspendu ou de la propriété dateSuspendu
        if (getForDateReference().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(CONTENTIEUXESTSUS ="
                    + this._dbWriteBoolean(statement.getTransaction(), new Boolean(false),
                            BConstants.DB_TYPE_BOOLEAN_CHAR) + " OR (DATESUSPENDU < "
                    + this._dbWriteDateAMJ(statement.getTransaction(), getForDateReference())
                    + " AND DATESUSPENDU <> 0))";
        }

        // traitement du vecteur de type de section
        if ((getForIdExterneLike() != null) && (getForIdExterneLike().trim().length() > 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDEXTERNE LIKE " + this._dbWriteString(statement.getTransaction(), getForIdExterneLike())
                    + "%";
        }

        if ((getFromIdExterne() != null) && (getFromIdExterne().trim().length() > 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDEXTERNE >= " + this._dbWriteString(statement.getTransaction(), getFromIdExterne());
        }

        if ((getUntilIdExterne() != null) && (getUntilIdExterne().trim().length() > 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDEXTERNE <= " + this._dbWriteString(statement.getTransaction(), getUntilIdExterne());
        }

        if ((getForNotIdContMotifBloque() != null) && (getForNotIdContMotifBloque().trim().length() > 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCONTMOTIFBLOQUE <> "
                    + this._dbWriteNumeric(statement.getTransaction(), getForNotIdContMotifBloque());
        }

        // Traitement du positionnement pour id externe rôle
        if ((getFromIdExterneRole() != null) && (getFromIdExterneRole().length() != 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDEXTERNEROLE >=" + this._dbWriteString(statement.getTransaction(), getFromIdExterneRole());
        }

        // Traitement du positionnement pour id externe rôle
        if ((getUntilIdExterneRole() != null) && (getUntilIdExterneRole().length() != 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDEXTERNEROLE<=" + this._dbWriteString(statement.getTransaction(), getUntilIdExterneRole());
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

        // Traitement de la date d'échéance
        if (!JadeStringUtil.isBlankOrZero(getForDateEcheance())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "DATEECHEANCE <= " + this._dbWriteDateAMJ(statement.getTransaction(), getForDateEcheance());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new globaz.osiris.db.comptes.CASection();
    }

    public String getForDateEcheance() {
        return forDateEcheance;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.06.2002 09:54:27)
     * 
     * @return String
     */
    public String getForDateReference() {
        return forDateReference;
    }

    /**
     * @return
     */
    public String getForIdCategorie() {
        return forIdCategorie;
    }

    /**
     * Returns the forIdExterneLike.
     * 
     * @return String
     */
    public String getForIdExterneLike() {
        return forIdExterneLike;
    }

    /**
     * @return
     */
    public String getForIdGenreCompte() {
        return forIdGenreCompte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.06.2002 09:25:01)
     * 
     * @return String
     */
    public String getForIdSequenceContentieux() {
        return forIdSequenceContentieux;
    }

    /**
     * Returns the forNotIdContMotifBloque.
     * 
     * @return String
     */
    public String getForNotIdContMotifBloque() {
        return forNotIdContMotifBloque;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.06.2002 13:05:48)
     * 
     * @return List
     */
    public List getForRoles() {
        return forRoles;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.07.2002 13:38:18)
     * 
     * @return String
     */
    public String getForTriListeCA() {
        return forTriListeCA;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.07.2002 13:45:22)
     * 
     * @return String
     */
    public String getForTriListeSection() {
        return forTriListeSection;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.06.2002 13:05:48)
     * 
     * @return List
     */
    public List getForTypeSections() {
        return forTypeSections;
    }

    /**
     * Returns the fromIdExterne.
     * 
     * @return String
     */
    public String getFromIdExterne() {
        return fromIdExterne;
    }

    /**
     * @return
     */
    public String getFromIdExterneRole() {
        return fromIdExterneRole;
    }

    /**
     * Returns the untilIdExterne.
     * 
     * @return String
     */
    public String getUntilIdExterne() {
        return untilIdExterne;
    }

    /**
     * @return
     */
    public String getUntilIdExterneRole() {
        return untilIdExterneRole;
    }

    /**
     * @return
     */
    public boolean isForSoldePositifOnly() {
        return forSoldePositifOnly;
    }

    public void setForDateEcheance(String forDateEcheance) {
        this.forDateEcheance = forDateEcheance;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.06.2002 09:54:27)
     * 
     * @param newForDateReference
     *            String
     */
    public void setForDateReference(String newForDateReference) {
        forDateReference = newForDateReference;
    }

    /**
     * @param string
     */
    public void setForIdCategorie(String s) {
        forIdCategorie = s;
    }

    /**
     * Sets the forIdExterneLike.
     * 
     * @param forIdExterneLike
     *            The forIdExterneLike to set
     */
    public void setForIdExterneLike(String forIdExterneLike) {
        this.forIdExterneLike = forIdExterneLike;
    }

    /**
     * @param string
     */
    public void setForIdGenreCompte(String s) {
        forIdGenreCompte = s;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.06.2002 09:25:01)
     * 
     * @param newForIdSequenceContentieux
     *            String
     */
    public void setForIdSequenceContentieux(String newForIdSequenceContentieux) {
        forIdSequenceContentieux = newForIdSequenceContentieux;
    }

    /**
     * Sets the forNotIdContMotifBloque.
     * 
     * @param forNotIdContMotifBloque
     *            The forNotIdContMotifBloque to set
     */
    public void setForNotIdContMotifBloque(String forNotIdContMotifBloque) {
        this.forNotIdContMotifBloque = forNotIdContMotifBloque;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.06.2002 13:05:48)
     * 
     * @param newForRoles
     *            List
     */
    public void setForRoles(List newForRoles) {
        forRoles = newForRoles;
    }

    /**
     * @param b
     */
    public void setForSoldePositifOnly(boolean b) {
        forSoldePositifOnly = b;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.07.2002 13:38:18)
     * 
     * @param newForTriListeCA
     *            String
     */
    public void setForTriListeCA(String newForTriListeCA) {
        forTriListeCA = newForTriListeCA;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.07.2002 13:45:22)
     * 
     * @param newForTriListeSection
     *            String
     */
    public void setForTriListeSection(String newForTriListeSection) {
        forTriListeSection = newForTriListeSection;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.06.2002 13:05:48)
     * 
     * @param newForTypeSections
     *            List
     */
    public void setForTypeSections(List newForTypeSections) {
        forTypeSections = newForTypeSections;
    }

    /**
     * Sets the fromIdExterne.
     * 
     * @param fromIdExterne
     *            The fromIdExterne to set
     */
    public void setFromIdExterne(String fromIdExterne) {
        this.fromIdExterne = fromIdExterne;
    }

    /**
     * @param string
     */
    public void setFromIdExterneRole(String string) {
        fromIdExterneRole = string;
    }

    /**
     * Sets the untilIdExterne.
     * 
     * @param untilIdExterne
     *            The untilIdExterne to set
     */
    public void setUntilIdExterne(String untilIdExterne) {
        this.untilIdExterne = untilIdExterne;
    }

    /**
     * @param string
     */
    public void setUntilIdExterneRole(String string) {
        untilIdExterneRole = string;
    }

}
