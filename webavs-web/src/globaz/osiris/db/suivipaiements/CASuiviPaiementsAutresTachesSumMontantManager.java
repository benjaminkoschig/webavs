package globaz.osiris.db.suivipaiements;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CACompteCourant;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;

/**
 * @author dda
 */
public class CASuiviPaiementsAutresTachesSumMontantManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdCompteCourant = new String();
    private String fromDateValeur = new String();
    private boolean modeListSection = false;

    private String untilDateValeur = new String();

    /**
     * @see globaz.globall.db.BManager#_getSql(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getSql(BStatement statement) {
        String select = "select " + getColumnNames() + " from " + _getCollection() + CAOperation.TABLE_CAOPERP + " a, "
                + _getCollection() + CARubrique.TABLE_CARUBRP + " b, " + getTemporaryTableSecteur() + " e, "
                + _getCollection() + CAJournal.TABLE_CAJOURP + " f ";
        select += "where ";
        select += "a." + CAOperation.FIELD_IDTYPEOPERATION + " like 'E%' and a." + CAOperation.FIELD_ETAT + " = "
                + APIOperation.ETAT_COMPTABILISE + " and a." + CACompteCourant.FIELD_IDCOMPTECOURANT + " = "
                + getForIdCompteCourant() + " and ";
        select += "a." + CAOperation.FIELD_IDJOURNAL + " = f." + CAJournal.FIELD_IDJOURNAL;
        select += getWhereDateValeurBetween();
        select += getWhereDateUntilBetween();
        select += " and a." + CAOperation.FIELD_IDCOMPTE + " = b." + CARubrique.FIELD_IDRUBRIQUE + " and ";
        select += "((b." + CARubrique.FIELD_IDSECTEUR + " <> e." + CARubrique.FIELD_IDSECTEUR + ") ";
        select += "or ";
        select += "(b." + CARubrique.FIELD_IDSECTEUR + " = e." + CARubrique.FIELD_IDSECTEUR + " and b."
                + CARubrique.FIELD_NATURERUBRIQUE + " <> " + APIRubrique.COTISATION_AVEC_MASSE + " and b."
                + CARubrique.FIELD_NATURERUBRIQUE + " <> " + APIRubrique.COTISATION_SANS_MASSE + ")";
        select += ") ";
        select += getGroupBy();

        return select;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CASuiviPaiementsAutresTachesSumMontant();
    }

    private String getColumnNames() {
        if (isModeListSection()) {
            return CAOperation.FIELD_IDSECTION;
        } else {
            return "sum(a." + CAOperation.FIELD_MONTANT + ") as " + CAOperation.FIELD_MONTANT;
        }
    }

    /**
     * @return Returns the forIdCompteCourant.
     */
    public String getForIdCompteCourant() {
        return forIdCompteCourant;
    }

    /**
     * @return Returns the fromDateValeur.
     */
    public String getFromDateValeur() {
        return fromDateValeur;
    }

    private String getGroupBy() {
        if (isModeListSection()) {
            return "group by a." + CAOperation.FIELD_IDSECTION;
        } else {
            return "";
        }
    }

    protected String getTemporaryTableSecteur() {
        return "(select c." + CARubrique.FIELD_IDSECTEUR + " as " + CARubrique.FIELD_IDSECTEUR + " from "
                + _getCollection() + CARubrique.TABLE_CARUBRP + " c, " + _getCollection()
                + CACompteCourant.TABLE_CACPTCP + " d where c." + CARubrique.FIELD_IDRUBRIQUE + " = d."
                + CACompteCourant.FIELD_IDRUBRIQUE + " and d." + CACompteCourant.FIELD_IDCOMPTECOURANT + " = "
                + getForIdCompteCourant() + ")";
    }

    /**
     * @return Returns the untilDateValeur.
     */
    public String getUntilDateValeur() {
        return untilDateValeur;
    }

    protected String getWhereDateUntilBetween() {
        if (!JadeStringUtil.isBlank(getUntilDateValeur())) {
            return " and f." + CAJournal.FIELD_DATEVALEURCG + " <= " + getUntilDateValeur();
        } else {
            return "";
        }
    }

    private String getWhereDateValeurBetween() {
        if (!JadeStringUtil.isBlank(getFromDateValeur())) {
            return " and f." + CAJournal.FIELD_DATEVALEURCG + " >= " + getFromDateValeur();
        } else {
            return "";
        }
    }

    /**
     * @return Returns the modeListSection.
     */
    public boolean isModeListSection() {
        return modeListSection;
    }

    /**
     * @param forIdCompteCourant
     *            The forIdCompteCourant to set.
     */
    public void setForIdCompteCourant(String forIdCompteCourant) {
        this.forIdCompteCourant = forIdCompteCourant;
    }

    /**
     * @param fromDateValeur
     *            The fromDateValeur to set.
     */
    public void setFromDateValeur(String fromDateValeur) {
        this.fromDateValeur = fromDateValeur;
    }

    /**
     * @param modeListSection
     *            The modeListSection to set.
     */
    public void setModeListSection(boolean modeListSection) {
        this.modeListSection = modeListSection;
    }

    /**
     * @param untilDateValeur
     *            The untilDateValeur to set.
     */
    public void setUntilDateValeur(String untilDateValeur) {
        this.untilDateValeur = untilDateValeur;
    }
}
