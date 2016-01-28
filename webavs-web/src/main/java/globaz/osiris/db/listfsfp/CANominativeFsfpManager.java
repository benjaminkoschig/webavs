package globaz.osiris.db.listfsfp;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteCourant;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CASection;

/**
 * @author BJO
 * 
 */
public class CANominativeFsfpManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdCompteCourant = new String();
    private String forSelectionRole = new String();
    private String fromDateValeur = new String();

    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {
        // setModeListSection(true);
    }

    @Override
    protected String _getSql(BStatement statement) {
        String select = "select ca." + CACompteAnnexe.FIELD_IDEXTERNEROLE + ",ca." + CACompteAnnexe.FIELD_IDTIERS
                + ",se." + CASection.FIELD_IDEXTERNE + ",ca." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + ",se."
                + CASection.FIELD_IDSECTION + ", sum(op." + CAOperation.FIELD_MONTANT + ") as "
                + CAOperation.FIELD_MONTANT + " from " + _getCollection() + CAOperation.TABLE_CAOPERP + " op ";
        select += "inner join " + _getCollection() + CAJournal.TABLE_CAJOURP + " jo on (op."
                + CAOperation.FIELD_IDJOURNAL + "=jo." + CAJournal.FIELD_IDJOURNAL + ") ";
        select += "inner join " + _getCollection() + CACompteCourant.TABLE_CACPTCP + " cc on (op."
                + CAOperation.FIELD_IDCOMPTECOURANT + "=cc." + CACompteCourant.FIELD_IDCOMPTECOURANT + ") ";
        select += "inner join " + _getCollection() + CASection.TABLE_CASECTP + " se on (op."
                + CAOperation.FIELD_IDSECTION + "=se." + CASection.FIELD_IDSECTION + ") ";
        select += "inner join " + _getCollection() + CACompteAnnexe.TABLE_CACPTAP + " ca on (op."
                + CAOperation.FIELD_IDCOMPTEANNEXE + "=ca." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + ") ";
        select += "where op." + CAOperation.FIELD_ETAT + "=205002 ";
        select += "and cc." + CACompteCourant.FIELD_IDCOMPTECOURANT + "=" + getForIdCompteCourant() + " ";
        select += "and jo." + CAJournal.FIELD_DATEVALEURCG + "<=" + getFromDateValeur() + " ";
        select += "and se." + CASection.FIELD_SOLDE + ">0 ";
        select += "and se." + CASection.FIELD_DATEECHEANCE + "<=" + getFromDateValeur() + " ";
        if (!getForSelectionRole().equals("")) {
            select += "and ca." + CACompteAnnexe.FIELD_IDROLE + " in (" + getForSelectionRole() + ") ";
        }
        select += "group by " + CACompteAnnexe.FIELD_IDEXTERNEROLE + "," + CACompteAnnexe.FIELD_IDTIERS + ",se."
                + CASection.FIELD_IDEXTERNE + ",ca." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + ",se."
                + CASection.FIELD_IDSECTION + " having sum(" + CAOperation.FIELD_MONTANT + ")>0" + " order by "
                + CACompteAnnexe.FIELD_IDEXTERNEROLE;

        return select;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CANominativeFsfp();
    }

    public String getForIdCompteCourant() {
        return forIdCompteCourant;
    }

    public String getForSelectionRole() {
        return forSelectionRole;
    }

    public String getFromDateValeur() {
        return fromDateValeur;
    }

    public void setForIdCompteCourant(String forIdCompteCourant) {
        this.forIdCompteCourant = forIdCompteCourant;
    }

    public void setForSelectionRole(String forRole) {
        forSelectionRole = forRole;
    }

    public void setFromDateValeur(String fromDateValeur) {
        this.fromDateValeur = fromDateValeur;
    }

}
