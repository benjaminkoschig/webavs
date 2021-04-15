package globaz.osiris.db.comptes;

import globaz.globall.db.BConstants;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

/**
 * Insérez la description du type ici. Date de création : (10.12.2001 12:59:11)
 * 
 * @author: Administrator
 */
public class CACompteAnnexeManager extends BManager implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String ALL_CATEGORIE = "all";
    public final static String ORDER_IDCOMPTEANNEXE = "1000";

    public final static String ORDER_SOLDE = "1001";

    private String afterIdCompteAnnexe = new String();
    private boolean bloque = false;
    private String forDateReferenceBlocage = new String();
    private String forIdCategorie = new String();
    private String forIdCompteAnnexeIn = new String();
    private String forIdContMotifBloque = new String();
    private String forIdExterneRole = new String();
    private Collection<String> forIdExterneRoleIn = null;
    private String forIdGenreCompte = new String();
    private String forIdJournal = new String();
    private String forIdRole = new String();
    private String forIdTiers = new String();
    private Collection<String> forIdTiersIn = null;
    private String forMontantMinime = new String();
    private String forSelectionCompte = new String();
    private String forSelectionRole = new String();
    private String forSelectionSigne = new String();
    private String forSelectionTri = new String();
    private String forTriSpecial = new String();
    private String fromIdExterneRole = new String();
    private String fromNoSatConsul = new String();
    private String fromNumNom = new String();
    private String likeIdExterneRole = "";
    private String likeNumNom = new String();
    private boolean motifBloqueDefini = false;
    public String orderBy = new String();
    private String untilIdExterneRole = new String();
    private String forEBillAccountID;

    private String untilNoSatConsul = new String();

    private boolean verrouille = false;

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CACPTAP CACPTAP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        if (getForSelectionTri().equals("1")) {
            return getForTriSpecialOrder() + CACompteAnnexe.FIELD_IDEXTERNEROLE + ", " + CACompteAnnexe.FIELD_IDROLE;
        } else if (getForSelectionTri().equals("2")) {
            return getForTriSpecialOrder() + CACompteAnnexe.FIELD_DESCUPCASE;
        } else if (getOrderBy().equals(CACompteAnnexeManager.ORDER_IDCOMPTEANNEXE)) {
            return getForTriSpecialOrder() + CACompteAnnexe.FIELD_IDCOMPTEANNEXE;
        } else if (!JadeStringUtil.isIntegerEmpty(getForTriSpecial())) {
            return CACompteAnnexe.FIELD_IDTRI;
        }

        return "";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // Traitement du positionnement pour un id de tiers
        if ((!JadeStringUtil.isBlank(getForIdTiers())) && !getForIdTiers().equalsIgnoreCase("null")) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CACompteAnnexe.FIELD_IDTIERS + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdTiers());
        }

        if ((getForIdTiersIn() != null) && (getForIdTiersIn().size() > 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CACompteAnnexe.FIELD_IDTIERS + " IN(";

            boolean isFirstIdTiers = true;

            for (String unIdTiers : getForIdTiersIn()) {

                if (!JadeNumericUtil.isInteger(unIdTiers)) {
                    continue;
                }

                if (isFirstIdTiers) {
                    isFirstIdTiers = false;
                } else {
                    sqlWhere += ",";
                }
                sqlWhere += this._dbWriteNumeric(statement.getTransaction(), unIdTiers);
            }

            sqlWhere += ")";
        }

        // Traitement du positionnement pour une sélection du rôle
        if ((!JadeStringUtil.isBlank(getForSelectionRole())) && !getForSelectionRole().equals("1000")) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (getForSelectionRole().indexOf(',') != -1) {
                String[] roles = JadeStringUtil.split(getForSelectionRole(), ',', Integer.MAX_VALUE);

                sqlWhere += CACompteAnnexe.FIELD_IDROLE + " IN (";

                for (int idRole = 0; idRole < roles.length; ++idRole) {
                    if (idRole > 0) {
                        sqlWhere += ",";
                    }
                    sqlWhere += this._dbWriteNumeric(statement.getTransaction(), roles[idRole]);
                }

                sqlWhere += ")";
            } else {
                sqlWhere += CACompteAnnexe.FIELD_IDROLE + "="
                        + this._dbWriteNumeric(statement.getTransaction(), getForSelectionRole());
            }
        }

        // Traitement du positionnement pour id rôle
        if (!JadeStringUtil.isBlank(getForIdRole())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CACompteAnnexe.FIELD_IDROLE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdRole());
        }

        if (!JadeStringUtil.isBlank(getForIdCompteAnnexeIn())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CACompteAnnexe.FIELD_IDCOMPTEANNEXE + " in (" + getForIdCompteAnnexeIn() + ")";
        }

        // Traitement du positionnement pour id externe rôle
        if (!JadeStringUtil.isBlank(getForIdExterneRole())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CACompteAnnexe.FIELD_IDEXTERNEROLE + "="
                    + this._dbWriteString(statement.getTransaction(), getForIdExterneRole());
        }

        // Traitement du positionnement pour id externe rôle
        if (!JadeStringUtil.isBlank(getFromIdExterneRole())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CACompteAnnexe.FIELD_IDEXTERNEROLE + " >="
                    + this._dbWriteString(statement.getTransaction(), getFromIdExterneRole());
        }

        // Traitement du positionnement pour id externe rôle
        if (!JadeStringUtil.isBlank(getUntilIdExterneRole())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CACompteAnnexe.FIELD_IDEXTERNEROLE + "<="
                    + this._dbWriteString(statement.getTransaction(), getUntilIdExterneRole());
        }

        // Traitement du positionnement pour id journal
        if (!JadeStringUtil.isBlank(getForIdJournal())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CACompteAnnexe.FIELD_IDJOURNAL + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdJournal());
        }

        // Traitement du positionnement pour id compte annexe
        if (!JadeStringUtil.isBlank(getAfterIdCompteAnnexe())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CACompteAnnexe.FIELD_IDCOMPTEANNEXE + ">"
                    + this._dbWriteNumeric(statement.getTransaction(), getAfterIdCompteAnnexe());
        }

        // Traitement du positionnement pour une sélection des comptes
        if ((!JadeStringUtil.isBlank(getForSelectionCompte())) && !getForSelectionCompte().equals("1000")) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            switch (java.lang.Integer.parseInt(getForSelectionCompte())) {
                case 1:
                    sqlWhere += CACompteAnnexe.FIELD_SOLDE + " <> 0";
                    break;
                case 2:
                    sqlWhere += CACompteAnnexe.FIELD_SOLDE + " = 0";
                    break;
                default:
                    break;
            }
        }

        /*
         * //Traitement de la sélection à partir d'un numéro ou d'un nom if (getFromNumNom().length() != 0) { if
         * (sqlWhere.length() != 0) { sqlWhere += " AND "; } switch (java.lang.Integer.parseInt(getForSelectionTri())){
         * case 1: sqlWhere += CACompteAnnexe.FIELD_IDEXTERNEROLE + " >= " + _dbWriteString(statement.getTransaction(),
         * getFromNumNom()); break; case 2: sqlWhere += "DESCRIPTION >= "+_dbWriteString(statement.getTransaction(),
         * getFromNumNom()); break; default: break; } }
         */
        // Traitement de la sélection à partir d'un numéro ou d'un nom
        if (!JadeStringUtil.isBlank(getFromNumNom())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            String choix = JadeStringUtil.change(getFromNumNom(), ".", "");
            choix = JadeStringUtil.change(choix, "-", "");
            if (JadeStringUtil.isDigit(choix)) {
                sqlWhere += CACompteAnnexe.FIELD_IDEXTERNEROLE + " >= "
                        + this._dbWriteString(statement.getTransaction(), getFromNumNom());
                setForSelectionTri("1");
            } else {
                sqlWhere += CACompteAnnexe.FIELD_DESCUPCASE
                        + " >= "
                        + this._dbWriteString(statement.getTransaction(),
                                JadeStringUtil.convertSpecialChars(getFromNumNom()).toUpperCase());
                setForSelectionTri("2");
            }
        }
        // Traitement de la sélection à partir d'un numéro ou d'un nom
        if (!JadeStringUtil.isBlank(getLikeNumNom())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            String choix = JadeStringUtil.change(getLikeNumNom(), ".", "");
            choix = JadeStringUtil.change(choix, "-", "");
            if (JadeStringUtil.isDigit(choix)) {
                sqlWhere += CACompteAnnexe.FIELD_IDEXTERNEROLE + " like "
                        + this._dbWriteString(statement.getTransaction(), "%" + getLikeNumNom() + "%");
                setForSelectionTri("1");
            } else {
                sqlWhere += CACompteAnnexe.FIELD_DESCUPCASE
                        + " like "
                        + this._dbWriteString(statement.getTransaction(),
                                "%" + JadeStringUtil.convertSpecialChars(getLikeNumNom()).toUpperCase() + "%");
                setForSelectionTri("2");
            }
        }

        // Traitement de la sélection du signe du solde
        if (!JadeStringUtil.isBlank(getForSelectionSigne())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            switch (java.lang.Integer.parseInt(getForSelectionSigne())) {
                case 1:
                    sqlWhere += CACompteAnnexe.FIELD_SOLDE + " <> 0";
                    break;
                case 2:
                    sqlWhere += CACompteAnnexe.FIELD_SOLDE + " > 0";
                    break;
                case 3:
                    sqlWhere += CACompteAnnexe.FIELD_SOLDE + " < 0";
                    break;
                default:
                    break;
            }

        }

        // Traitement du positionnement pour id journal
        if (!JadeStringUtil.isBlank(getForIdContMotifBloque())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CACompteAnnexe.FIELD_IDCONTMOTIFBLOQUE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdContMotifBloque());
        }

        // Traitement du genre de compte
        if (!JadeStringUtil.isBlank(getForIdGenreCompte())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CACompteAnnexe.FIELD_IDGENRECOMPTE + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdGenreCompte());
        }

        // Traitement de la catégorie du compte
        if ((!JadeStringUtil.isBlank(getForIdCategorie()))
                && (!getForIdCategorie().equals(CACompteAnnexeManager.ALL_CATEGORIE))) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            if (getForIdCategorie().indexOf(',') != -1) {
                String[] categories = JadeStringUtil.split(getForIdCategorie(), ',', Integer.MAX_VALUE);

                sqlWhere += CACompteAnnexe.FIELD_IDCATEGORIE + " IN (";

                for (int id = 0; id < categories.length; ++id) {
                    if (id > 0) {
                        sqlWhere += ",";
                    }

                    sqlWhere += this._dbWriteNumeric(statement.getTransaction(), categories[id]);
                }

                sqlWhere += ")";
            } else {
                sqlWhere += CACompteAnnexe.FIELD_IDCATEGORIE + " = "
                        + this._dbWriteNumeric(statement.getTransaction(), getForIdCategorie());
            }
        }

        if (isVerrouille()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CACompteAnnexe.FIELD_ESTVERROUILLE + " = " + BConstants.DB_BOOLEAN_TRUE_DELIMITED;
        }

        if (isBloque()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CACompteAnnexe.FIELD_CONTESTBLOQUE + " = " + BConstants.DB_BOOLEAN_TRUE_DELIMITED;
        }

        if (isMotifBloqueDefini()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CACompteAnnexe.FIELD_IDCONTMOTIFBLOQUE + " > 0";
        }

        // filtre sur la période blocage
        if (!JadeStringUtil.isBlank(getForDateReferenceBlocage())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "( (" + getForDateReferenceBlocage() + " >= " + CACompteAnnexe.FIELD_CONTDATEDEBBLOQUE + " OR "
                    + CACompteAnnexe.FIELD_CONTDATEDEBBLOQUE + "=0) " + "AND (" + getForDateReferenceBlocage() + " <= "
                    + CACompteAnnexe.FIELD_CONTDATEFINBLOQUE + " OR " + CACompteAnnexe.FIELD_CONTDATEFINBLOQUE
                    + "=0) )";
        }

        if (!JadeStringUtil.isBlank(getFromNoSatConsul())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CACompteAnnexe.FIELD_IDTRI + " >= "
                    + this._dbWriteNumeric(statement.getTransaction(), getFromNoSatConsul());
        }

        if (!JadeStringUtil.isBlank(getUntilNoSatConsul())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CACompteAnnexe.FIELD_IDTRI + " <= "
                    + this._dbWriteNumeric(statement.getTransaction(), getUntilNoSatConsul());
        }

        if (!JadeStringUtil.isBlank(getForMontantMinime())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "SOLDE <> 0 AND " + CACompteAnnexe.FIELD_SOLDE + " BETWEEN -"
                    + this._dbWriteNumeric(statement.getTransaction(), getForMontantMinime()) + " AND "
                    + this._dbWriteNumeric(statement.getTransaction(), getForMontantMinime());
        }

        if (!JadeStringUtil.isBlank(getLikeIdExterneRole())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CACompteAnnexe.FIELD_IDEXTERNEROLE + " LIKE "
                    + this._dbWriteString(statement.getTransaction(), getLikeIdExterneRole() + "%");
        }

        if (getForIdExterneRoleIn() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            String ids = null;
            for (String id : getForIdExterneRoleIn()) {
                if (ids != null) {
                    ids += ",";
                    ids += this._dbWriteString(statement.getTransaction(), id);
                } else {
                    ids = this._dbWriteString(statement.getTransaction(), id);
                }
            }

            sqlWhere += CACompteAnnexe.FIELD_IDEXTERNEROLE + " in (" + ids + ")";
        }

        if (!JadeStringUtil.isBlank(getForEBillAccountID())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CACompteAnnexe.FIELD_EBILL_ID + " = "
                    + this._dbWriteString(statement.getTransaction(), getForEBillAccountID());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CACompteAnnexe();
    }

    /**
     * Format le no. de satellite et consulat. S'il contient un "/", le slash doit être effacé et les parties inversées.
     * 
     * @param s
     * @return
     */
    private String formatNoSatConsul(String s) {
        if ((!JadeStringUtil.isBlank(s)) && (s.indexOf(" ") > -1)) {
            StringTokenizer st = new StringTokenizer(s, " ", false);
            String tmp = "";
            while (st.hasMoreElements()) {
                tmp += st.nextElement();
            }

            s = tmp;
        }

        if ((!JadeStringUtil.isBlank(s)) && (s.indexOf("/") > -1)) {
            return s.substring(s.indexOf("/") + 1, s.length()) + s.substring(0, s.indexOf("/"));
        } else {
            return s;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.03.2002 10:18:06)
     * 
     * @return String
     */
    public String getAfterIdCompteAnnexe() {
        return afterIdCompteAnnexe;
    }

    /**
     * @return
     */
    public String getForDateReferenceBlocage() {
        return forDateReferenceBlocage;
    }

    /**
     * @return
     */
    public String getForIdCategorie() {
        return forIdCategorie;
    }

    public String getForIdCompteAnnexeIn() {
        return forIdCompteAnnexeIn;
    }

    /**
     * Returns the forIdContMotifBloque.
     * 
     * @return String
     */
    public String getForIdContMotifBloque() {
        return forIdContMotifBloque;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.01.2002 14:59:43)
     * 
     * @return String
     */
    public String getForIdExterneRole() {
        return forIdExterneRole;
    }

    public Collection<String> getForIdExterneRoleIn() {
        return forIdExterneRoleIn;
    }

    /**
     * @return
     */
    public String getForIdGenreCompte() {
        return forIdGenreCompte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.03.2002 10:17:05)
     * 
     * @return String
     */
    public String getForIdJournal() {
        return forIdJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.01.2002 14:58:54)
     * 
     * @return String
     */
    public String getForIdRole() {
        return forIdRole;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.12.2001 10:20:07)
     * 
     * @return String
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    public Collection<String> getForIdTiersIn() {
        return forIdTiersIn;
    }

    /**
     * @return
     */
    public String getForMontantMinime() {
        return forMontantMinime;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.12.2001 14:35:02)
     * 
     * @return String
     */
    public String getForSelectionCompte() {
        return forSelectionCompte;
    }

    /**
     * peut contenir des ','.
     * 
     * @see #setForSelectionRole(String)
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
     * @return
     */
    public String getForTriSpecial() {
        return forTriSpecial;
    }

    private String getForTriSpecialOrder() {
        if (!JadeStringUtil.isIntegerEmpty(getForTriSpecial())) {
            return CACompteAnnexe.FIELD_IDTRI + ", ";
        } else {
            return "";
        }
    }

    /**
     * Returns the fromIdExterneRole.
     * 
     * @return String
     */
    public String getFromIdExterneRole() {
        return fromIdExterneRole;
    }

    /**
     * @return
     */
    public String getFromNoSatConsul() {
        return fromNoSatConsul;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.01.2002 08:13:50)
     * 
     * @return String
     */
    public String getFromNumNom() {
        return fromNumNom;
    }

    public String getLikeIdExterneRole() {
        return likeIdExterneRole;
    }

    /**
     * Returns the likeNumNom.
     * 
     * @return String
     */
    public String getLikeNumNom() {
        return likeNumNom;
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
     * Returns the untilIdExterneRole.
     * 
     * @return String
     */
    public String getUntilIdExterneRole() {
        return untilIdExterneRole;
    }

    /**
     * @return
     */
    public String getUntilNoSatConsul() {
        return untilNoSatConsul;
    }

    /**
     * @return
     */
    public boolean isBloque() {
        return bloque;
    }

    /**
     * @return
     */
    public boolean isMotifBloqueDefini() {
        return motifBloqueDefini;
    }

    /**
     * @return
     */
    public boolean isVerrouille() {
        return verrouille;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.03.2002 10:18:06)
     * 
     * @param newAfterIdCompteAnnexe
     *            String
     */
    public void setAfterIdCompteAnnexe(String newAfterIdCompteAnnexe) {
        afterIdCompteAnnexe = newAfterIdCompteAnnexe;
    }

    /**
     * @param b
     */
    public void setBloque(boolean b) {
        bloque = b;
    }

    /**
     * @param string
     */
    public void setForDateReferenceBlocage(String string) {
        forDateReferenceBlocage = string;
    }

    /**
     * @param string
     */
    public void setForIdCategorie(String s) {
        forIdCategorie = s;
    }

    public void setForIdCompteAnnexeIn(String forIdCompteAnnexeIn) {
        this.forIdCompteAnnexeIn = forIdCompteAnnexeIn;
    }

    /**
     * Sets the forIdContMotifBloque.
     * 
     * @param forIdContMotifBloque
     *            The forIdContMotifBloque to set
     */
    public void setForIdContMotifBloque(String forIdContMotifBloque) {
        this.forIdContMotifBloque = forIdContMotifBloque;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.01.2002 14:59:43)
     * 
     * @param newForIdExterneRole
     *            String
     */
    public void setForIdExterneRole(String newForIdExterneRole) {
        forIdExterneRole = newForIdExterneRole;
    }

    public void setForIdExterneRoleIn(Collection<String> forIdExterneRoleIn) {
        this.forIdExterneRoleIn = forIdExterneRoleIn;
    }

    /**
     * @param string
     */
    public void setForIdGenreCompte(String s) {
        forIdGenreCompte = s;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.03.2002 10:17:05)
     * 
     * @param newForIdJournal
     *            String
     */
    public void setForIdJournal(String newForIdJournal) {
        forIdJournal = newForIdJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.01.2002 14:58:54)
     * 
     * @param newForIdRole
     *            String
     */
    public void setForIdRole(String newForIdRole) {
        forIdRole = newForIdRole;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.12.2001 10:20:07)
     * 
     * @param newForIdTiers
     *            String
     */
    public void setForIdTiers(String newForIdTiers) {
        forIdTiers = newForIdTiers;
    }

    public void setForIdTiersIn(Collection<String> forIdTiersIn) {
        this.forIdTiersIn = forIdTiersIn;
    }

    /**
     * Les ID Tiers sont passés dans une chaîne de caractères, séparés par le caractère '|'
     * 
     * @param forIdTiers
     */
    public void setForIdTiersIn(String forIdTiers) {
        if (!JadeStringUtil.isBlank(forIdTiers)) {
            String[] idSplit = forIdTiers.split("\\|");

            Collection<String> parsedIds = new ArrayList<String>();
            for (String unIdTiers : idSplit) {
                if (JadeNumericUtil.isInteger(unIdTiers)) {
                    parsedIds.add(unIdTiers);
                }
            }
            this.setForIdTiersIn(parsedIds);
        }
    }

    /**
     * @param string
     */
    public void setForMontantMinime(String s) {
        forMontantMinime = s;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.12.2001 14:35:02)
     * 
     * @param newSelectionCompte
     *            String
     */
    public void setForSelectionCompte(String newForSelectionCompte) {
        forSelectionCompte = newForSelectionCompte;
    }

    /**
     * newForSelectionRole peut contenir des ',' séparant plusieurs id de role, dans ce cas, le manager retourne toutes
     * les operations pour un compte annexe dont l'id du role est l'un de ceux transmis dans newForSelectionRole.
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
     * @param string
     */
    public void setForTriSpecial(String string) {
        forTriSpecial = string;
    }

    /**
     * Sets the fromIdExterneRole.
     * 
     * @param fromIdExterneRole
     *            The fromIdExterneRole to set
     */
    public void setFromIdExterneRole(String fromIdExterneRole) {
        this.fromIdExterneRole = fromIdExterneRole;
    }

    /**
     * @param string
     */
    public void setFromNoSatConsul(String s) {
        fromNoSatConsul = formatNoSatConsul(s);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.01.2002 08:13:50)
     * 
     * @param newFromNumNom
     *            String
     */
    public void setFromNumNom(String newFromNumNom) {
        fromNumNom = newFromNumNom;
    }

    public void setLikeIdExterneRole(String string) {
        likeIdExterneRole = string;
    }

    /**
     * Sets the likeNumNom.
     * 
     * @param likeNumNom
     *            The likeNumNom to set
     */
    public void setLikeNumNom(String likeNumNom) {
        this.likeNumNom = likeNumNom;
    }

    /**
     * @param b
     */
    public void setMotifBloqueDefini(boolean b) {
        motifBloqueDefini = b;
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

    /**
     * Sets the untilIdExterneRole.
     * 
     * @param untilIdExterneRole
     *            The untilIdExterneRole to set
     */
    public void setUntilIdExterneRole(String untilIdExterneRole) {
        this.untilIdExterneRole = untilIdExterneRole;
    }

    /**
     * @param string
     */
    public void setUntilNoSatConsul(String s) {
        untilNoSatConsul = formatNoSatConsul(s);
    }

    /**
     * @param boolean1
     */
    public void setVerrouille(boolean boolean1) {
        verrouille = boolean1;
    }

    public String getForEBillAccountID() {
        return forEBillAccountID;
    }

    public void setForEBillAccountID(String forEBillAccountID) {
        this.forEBillAccountID = forEBillAccountID;
    }
}
