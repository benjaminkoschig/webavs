package globaz.osiris.db.cumulcotisations;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;

/**
 * @author dda
 */
public class CACumulCotisationsParAnneeManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeCotisation;
    private String fromDateValeur;
    private String fromIdExterne;
    private String toDateValeur;
    private String toIdExterne;

    /**
     * @see globaz.globall.db.BManager#_getSql(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getSql(BStatement statement) {
        String select = "SELECT sum(a." + CAOperation.FIELD_MONTANT + ") " + CAOperation.FIELD_MONTANT + ", sum(a."
                + CAOperation.FIELD_MASSE + ") " + CAOperation.FIELD_MASSE + ", b." + CARubrique.FIELD_IDEXTERNE
                + ", a." + CAOperation.FIELD_ANNEECOTISATION;
        select += " FROM " + _getCollection() + CAOperation.TABLE_CAOPERP + " a, " + _getCollection()
                + CARubrique.TABLE_CARUBRP + " b, " + _getCollection() + CAJournal.TABLE_CAJOURP + " c ";
        select += " WHERE " + getWhereEtatOperation();
        select += getWhereTypeOperation();
        select += getWhereJoin();
        select += getWhereRubriqueBetween();
        select += getWhereNatureRubrique();
        select += getWhereDateValeurBetween();
        select += getWhereEtatJournal();
        select += getWhereForAnneeCotisation();
        select += " GROUP BY b." + CARubrique.FIELD_IDEXTERNE + ", a." + CAOperation.FIELD_ANNEECOTISATION;
        select += " ORDER BY b." + CARubrique.FIELD_IDEXTERNE + ", a." + CAOperation.FIELD_ANNEECOTISATION;

        return select;

    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CACumulCotisationsParAnnee();
    }

    public String getForAnneeCotisation() {
        return forAnneeCotisation;
    }

    public String getFromDateValeur() {
        return fromDateValeur;
    }

    public String getFromIdExterne() {
        return fromIdExterne;
    }

    public String getToDateValeur() {
        return toDateValeur;
    }

    public String getToIdExterne() {
        return toIdExterne;
    }

    private String getWhereDateValeurBetween() {
        if (!JadeStringUtil.isBlank(getFromDateValeur()) && !JadeStringUtil.isBlank(getToDateValeur())) {
            return " AND c." + CAJournal.FIELD_DATEVALEURCG + " BETWEEN " + getFromDateValeur() + " AND "
                    + getToDateValeur();
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

    /**
     * @return
     */
    private String getWhereForAnneeCotisation() {
        if (!JadeStringUtil.isBlank(getForAnneeCotisation())) {
            return " AND a." + CAOperation.FIELD_ANNEECOTISATION + " = " + getForAnneeCotisation();
        } else {
            return "";
        }
    }

    private String getWhereJoin() {
        return " AND a." + CAOperation.FIELD_IDCOMPTE + " = b." + CARubrique.FIELD_IDRUBRIQUE + " AND a."
                + CAOperation.FIELD_IDJOURNAL + " = c." + CAJournal.FIELD_IDJOURNAL;
    }

    private String getWhereNatureRubrique() {
        return " AND b." + CARubrique.FIELD_NATURERUBRIQUE + " in (" + APIRubrique.COTISATION_AVEC_MASSE + ", "
                + APIRubrique.COTISATION_SANS_MASSE + ") ";
    }

    private String getWhereRubriqueBetween() {
        if (!JadeStringUtil.isBlank(getFromIdExterne()) && !JadeStringUtil.isBlank(getToIdExterne())) {
            return " AND b." + CARubrique.FIELD_IDEXTERNE + " BETWEEN '" + getFromIdExterne() + "' AND '"
                    + getToIdExterne() + "' ";
        } else {
            return "";
        }
    }

    private String getWhereTypeOperation() {
        return " AND a." + CAOperation.FIELD_IDTYPEOPERATION + " like '" + APIOperation.CAECRITURE + "%' ";
    }

    public void setForAnneeCotisation(String forAnneeCotisation) {
        this.forAnneeCotisation = forAnneeCotisation;
    }

    public void setFromDateValeur(String fromDateValeur) {
        this.fromDateValeur = fromDateValeur;
    }

    public void setFromIdExterne(String fromIdExterne) {
        this.fromIdExterne = fromIdExterne;
    }

    public void setToDateValeur(String toDateValeur) {
        this.toDateValeur = toDateValeur;
    }

    public void setToIdExterne(String toIdExterne) {
        this.toIdExterne = toIdExterne;
    }

}
