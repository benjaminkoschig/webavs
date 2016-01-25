package globaz.osiris.db.recaprubriques;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;

/**
 * @author dda
 */
public class CARecapRubriquesManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String filtreAnnee;
    private String forIdCategorie;
    private String forIdGenreCompte;
    private String forSelectionRole;
    private String fromDateValeur;
    private String fromIdExterne;
    private String fromIdExterneRole;
    private String toDateValeur;
    private String toIdExterne;
    private String toIdExterneRole;

    /**
     * @see globaz.globall.db.BManager#_getSql(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getSql(BStatement statement) {
        String select = "SELECT sum(a." + CAOperation.FIELD_MONTANT + ") " + CAOperation.FIELD_MONTANT + ", sum(a."
                + CAOperation.FIELD_MASSE + ") " + CAOperation.FIELD_MASSE + ", d."
                + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + ", d." + CACompteAnnexe.FIELD_IDEXTERNEROLE + ", d."
                + CACompteAnnexe.FIELD_IDTIERS;
        select += " FROM " + _getCollection() + CAOperation.TABLE_CAOPERP + " a, " + _getCollection()
                + CARubrique.TABLE_CARUBRP + " b, " + _getCollection() + CAJournal.TABLE_CAJOURP + " c, "
                + _getCollection() + CACompteAnnexe.TABLE_CACPTAP + " d ";
        select += " WHERE " + getWhereEtatOperation();
        select += getWhereJoin();
        select += getWhereRubriqueBetween();
        select += getWhereDateValeurBetween();
        select += getWhereEtatJournal();
        select += getWhereIdExterneRoleBetween();
        select += getWhereForRole();
        select += getWhereForIdGenreCompte();
        select += getWhereForIdCategorie();
        select += " GROUP BY d." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + ", d." + CACompteAnnexe.FIELD_IDEXTERNEROLE
                + ", d." + CACompteAnnexe.FIELD_IDTIERS;
        select += " ORDER BY d." + CACompteAnnexe.FIELD_IDEXTERNEROLE;

        return select;

    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CARecapRubriques();
    }

    public String getFiltreAnnee() {
        return filtreAnnee;
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
     * @return
     */
    public String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * @return
     */
    public String getFromDateValeur() {
        return fromDateValeur;
    }

    /**
     * @return
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
     * @return
     */
    public String getToDateValeur() {
        return toDateValeur;
    }

    /**
     * @return
     */
    public String getToIdExterne() {
        return toIdExterne;
    }

    /**
     * @return
     */
    public String getToIdExterneRole() {
        return toIdExterneRole;
    }

    private String getWhereDateValeurBetween() {
        String sql = "";

        if (!JadeStringUtil.isBlank(getFromDateValeur()) && !JadeStringUtil.isBlank(getToDateValeur())) {
            sql = " AND c." + CAJournal.FIELD_DATEVALEURCG + " BETWEEN " + getFromDateValeur() + " AND "
                    + getToDateValeur();
            if (!JadeStringUtil.isBlank(getFiltreAnnee())) {
                sql += " AND a." + CAOperation.FIELD_ANNEECOTISATION + "=" + getFiltreAnnee();
            }
            return sql;
        } else {
            return "";
        }
    }

    private String getWhereEtatJournal() {
        return " AND c." + CAJournal.FIELD_ETAT + " = " + CAJournal.COMPTABILISE;
    }

    private String getWhereEtatOperation() {
        return "a." + CAOperation.FIELD_ETAT + " = " + APIOperation.ETAT_COMPTABILISE;
    }

    private String getWhereForIdCategorie() {
        if (!JadeStringUtil.isBlank(getForIdCategorie())
                && !getForIdCategorie().equals(CACompteAnnexeManager.ALL_CATEGORIE)) {
            return " AND d." + CACompteAnnexe.FIELD_IDCATEGORIE + " = " + getForIdCategorie();
        } else {
            return "";
        }
    }

    private String getWhereForIdGenreCompte() {
        if (!JadeStringUtil.isBlank(getForIdGenreCompte())) {
            return " AND d." + CACompteAnnexe.FIELD_IDGENRECOMPTE + " = " + getForIdGenreCompte();
        } else {
            return "";
        }
    }

    private String getWhereForRole() {
        if (!JadeStringUtil.isBlank(getForSelectionRole())) {
            if (getForSelectionRole().indexOf(',') != -1) {
                String[] roles = JadeStringUtil.split(getForSelectionRole(), ',', Integer.MAX_VALUE);
                StringBuffer retValue = new StringBuffer();

                retValue.append(" AND d.");
                retValue.append(CACompteAnnexe.FIELD_IDROLE);
                retValue.append(" IN (");

                for (int id = 0; id < roles.length; ++id) {
                    if (id > 0) {
                        retValue.append(',');
                    }

                    retValue.append(getForSelectionRole());
                }

                retValue.append(")");

                return retValue.toString();
            } else {
                return " AND d." + CACompteAnnexe.FIELD_IDROLE + " = " + getForSelectionRole();
            }
        } else {
            return "";
        }
    }

    private String getWhereIdExterneRoleBetween() {
        if (!JadeStringUtil.isBlank(getFromIdExterneRole()) && !JadeStringUtil.isBlank(getToIdExterneRole())) {
            return " AND d." + CACompteAnnexe.FIELD_IDEXTERNEROLE + " BETWEEN '" + getFromIdExterneRole() + "' AND '"
                    + getToIdExterneRole() + "' ";
        } else {
            return "";
        }
    }

    private String getWhereJoin() {
        return " AND a." + CAOperation.FIELD_IDCOMPTE + " = b." + CARubrique.FIELD_IDRUBRIQUE + " AND a."
                + CAOperation.FIELD_IDJOURNAL + " = c." + CAJournal.FIELD_IDJOURNAL + " AND a."
                + CAOperation.FIELD_IDCOMPTEANNEXE + " = d." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE;
    }

    private String getWhereRubriqueBetween() {
        if (!JadeStringUtil.isBlank(getFromIdExterne()) && !JadeStringUtil.isBlank(getToIdExterne())) {
            return " AND b." + CARubrique.FIELD_IDEXTERNE + " BETWEEN '" + getFromIdExterne() + "' AND '"
                    + getToIdExterne() + "' ";
        } else {
            return "";
        }
    }

    public void setFiltreAnnee(String string) {
        filtreAnnee = string;
    }

    /**
     * @param string
     */
    public void setForIdCategorie(String string) {
        forIdCategorie = string;
    }

    /**
     * @param string
     */
    public void setForIdGenreCompte(String string) {
        forIdGenreCompte = string;
    }

    /**
     * @param string
     */
    public void setForSelectionRole(String string) {
        forSelectionRole = string;
    }

    /**
     * @param string
     */
    public void setFromDateValeur(String string) {
        fromDateValeur = string;
    }

    /**
     * @param string
     */
    public void setFromIdExterne(String string) {
        fromIdExterne = string;
    }

    /**
     * @param string
     */
    public void setFromIdExterneRole(String string) {
        fromIdExterneRole = string;
    }

    /**
     * @param string
     */
    public void setToDateValeur(String string) {
        toDateValeur = string;
    }

    /**
     * @param string
     */
    public void setToIdExterne(String string) {
        toIdExterne = string;
    }

    /**
     * @param string
     */
    public void setToIdExterneRole(String string) {
        toIdExterneRole = string;
    }

}
