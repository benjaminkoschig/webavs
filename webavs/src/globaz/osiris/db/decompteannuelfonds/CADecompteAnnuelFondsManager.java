package globaz.osiris.db.decompteannuelfonds;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteCourant;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CASection;

/**
 * @author BJO
 * 
 */
public class CADecompteAnnuelFondsManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebutPeriode = new String();
    private String dateFinPeriode = new String();
    private String forIdCompteCourant = new String();

    // permet de définir quelle type de somme on souhaite effectuer
    // 1=fsfp,2=Encaissé/compensé
    private int typeMontant;

    public CADecompteAnnuelFondsManager(int typeMontant) {
        this.typeMontant = typeMontant;
    }

    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {
    }

    @Override
    protected String _getSql(BStatement statement) {
        String select = new String();

        // pour calculer les cotisations
        if (typeMontant == 1) {
            select = "select ru." + CARubrique.FIELD_IDEXTERNE + ",sum(op." + CAOperation.FIELD_MONTANT + ") as "
                    + CAOperation.FIELD_MONTANT + " from " + _getCollection() + CAOperation.TABLE_CAOPERP + " op ";
            select += "inner join " + _getCollection() + CAJournal.TABLE_CAJOURP + " jo on (op."
                    + CAOperation.FIELD_IDJOURNAL + "=jo." + CAJournal.FIELD_IDJOURNAL + ") ";
            select += "inner join " + _getCollection() + CACompteCourant.TABLE_CACPTCP + " cc on (op."
                    + CAOperation.FIELD_IDCOMPTECOURANT + "=cc." + CACompteCourant.FIELD_IDCOMPTECOURANT + ") ";
            select += "inner join " + _getCollection() + CASection.TABLE_CASECTP + " se on (op."
                    + CAOperation.FIELD_IDSECTION + "=se." + CASection.FIELD_IDSECTION + ") ";
            select += "inner join " + _getCollection() + CACompteAnnexe.TABLE_CACPTAP + " ca on (op."
                    + CAOperation.FIELD_IDCOMPTEANNEXE + "=ca." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + ") ";
            select += "inner join " + _getCollection() + CARubrique.TABLE_CARUBRP + " ru on (op."
                    + CAOperation.FIELD_IDCOMPTE + "=ru." + CARubrique.FIELD_IDRUBRIQUE + ") ";
            select += "where op." + CAOperation.FIELD_ETAT + "=205002 ";
            select += "and cc." + CACompteCourant.FIELD_IDCOMPTECOURANT + "=" + getForIdCompteCourant() + " ";
            select += "and jo." + CAJournal.FIELD_DATEVALEURCG + " between " + getDateDebutPeriode() + " and "
                    + getDateFinPeriode() + " ";
            select += "and " + CARubrique.FIELD_NATURERUBRIQUE + " not in(200004,200005,200006,200007) ";
            select += "group by " + CAOperation.FIELD_IDCOMPTE + ",ru." + CARubrique.FIELD_IDEXTERNE + " ";
            select += "order by ru." + CARubrique.FIELD_IDEXTERNE;
            /*
             * select ru.IDEXTERNE,sum(op.MONTANT) as MONTANT from WEBAVS.CAOPERP op inner join WEBAVS.CAJOURP jo on
             * (op.IDJOURNAL=jo.IDJOURNAL) inner join WEBAVS.CACPTCP cc on (op.IDCOMPTECOURANT=cc.IDCOMPTECOURANT) inner
             * join WEBAVS.CASECTP se on (op.IDSECTION=se.IDSECTION) inner join WEBAVS.CACPTAP ca on
             * (op.IDCOMPTEANNEXE=ca.IDCOMPTEANNEXE) inner join WEBAVS.CARUBRP ru on (op.IDCOMPTE=ru.IDRUBRIQUE) where
             * op.ETAT=205002 and cc.IDCOMPTECOURANT=1 and jo.DATEVALEURCG between 20090101 and 20091231 and
             * NATURERUBRIQUE not in(200004,200005,200006,200007) group by IDCOMPTE,ru.IDEXTERNE order by ru.IDEXTERNE
             */
        } else if (typeMontant == 2) {
            select = "select ru." + CARubrique.FIELD_IDEXTERNE + ",sum(op." + CAOperation.FIELD_MONTANT + ") as "
                    + CAOperation.FIELD_MONTANT + " from " + _getCollection() + CAOperation.TABLE_CAOPERP + " op ";
            select += "inner join " + _getCollection() + CAJournal.TABLE_CAJOURP + " jo on (op."
                    + CAOperation.FIELD_IDJOURNAL + "=jo." + CAJournal.FIELD_IDJOURNAL + ") ";
            select += "inner join " + _getCollection() + CACompteCourant.TABLE_CACPTCP + " cc on (op."
                    + CAOperation.FIELD_IDCOMPTECOURANT + "=cc." + CACompteCourant.FIELD_IDCOMPTECOURANT + ") ";
            select += "inner join " + _getCollection() + CASection.TABLE_CASECTP + " se on (op."
                    + CAOperation.FIELD_IDSECTION + "=se." + CASection.FIELD_IDSECTION + ") ";
            select += "inner join " + _getCollection() + CACompteAnnexe.TABLE_CACPTAP + " ca on (op."
                    + CAOperation.FIELD_IDCOMPTEANNEXE + "=ca." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + ") ";
            select += "inner join " + _getCollection() + CARubrique.TABLE_CARUBRP + " ru on (op."
                    + CAOperation.FIELD_IDCOMPTE + "=ru." + CARubrique.FIELD_IDRUBRIQUE + ") ";
            select += "where op." + CAOperation.FIELD_ETAT + "=205002 ";
            select += "and cc." + CACompteCourant.FIELD_IDCOMPTECOURANT + "=" + getForIdCompteCourant() + " ";
            select += "and jo." + CAJournal.FIELD_DATEVALEURCG + " between " + getDateDebutPeriode() + " and "
                    + getDateFinPeriode() + " ";
            select += "and " + CARubrique.FIELD_NATURERUBRIQUE + " in(200004,200005,200006,200007) ";
            select += "group by " + CAOperation.FIELD_IDCOMPTE + ",ru." + CARubrique.FIELD_IDEXTERNE + " ";
            select += "order by ru." + CARubrique.FIELD_IDEXTERNE;
            /*
             * select ru.IDEXTERNE,sum(op.MONTANT) as MONTANT from WEBAVS.CAOPERP op inner join WEBAVS.CAJOURP jo on
             * (op.IDJOURNAL=jo.IDJOURNAL) inner join WEBAVS.CACPTCP cc on (op.IDCOMPTECOURANT=cc.IDCOMPTECOURANT) inner
             * join WEBAVS.CASECTP se on (op.IDSECTION=se.IDSECTION) inner join WEBAVS.CACPTAP ca on
             * (op.IDCOMPTEANNEXE=ca.IDCOMPTEANNEXE) inner join WEBAVS.CARUBRP ru on (op.IDCOMPTE=ru.IDRUBRIQUE) where
             * op.ETAT=205002 and cc.IDCOMPTECOURANT=1 and jo.DATEVALEURCG between 20090101 and 20091231 and
             * NATURERUBRIQUE in(200004,200005,200006,200007) group by IDCOMPTE,ru.IDEXTERNE order by ru.IDEXTERNE
             */

        } else if (typeMontant == 3) {
            select = "select sum(op." + CAOperation.FIELD_MONTANT + ") as " + CAOperation.FIELD_MONTANT + " from "
                    + _getCollection() + CAOperation.TABLE_CAOPERP + " op ";
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
            select += "and jo." + CAJournal.FIELD_DATEVALEURCG + "<=" + getDateFinPeriode() + "";

            /*
             * select sum(montant) from ccjuweb.caoperp op inner join ccjuweb.cajourp jo on (op.idjournal=jo.idjournal)
             * inner join ccjuweb.cacptcp cc on (op.idcomptecourant=cc.idcomptecourant) inner join ccjuweb.casectp se on
             * (op.idsection=se.idsection) inner join ccjuweb.cacptap ca on (op.idcompteannexe=ca.idcompteannexe) where
             * op.etat=205002 and cc.idexterne='7300.1101.0000' and jo.datevaleurcg <= 20080924
             */
        } else {
            Exception e = new Exception("le type de montant à calculer n'existe pas!!!");
            e.printStackTrace();
        }

        return select;

    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CADecompteAnnuelFonds();
    }

    public String getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    public String getDateFinPeriode() {
        return dateFinPeriode;
    }

    public String getForIdCompteCourant() {
        return forIdCompteCourant;
    }

    public void setDateDebutPeriode(String dateDebutPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
    }

    public void setDateFinPeriode(String dateFinPeriode) {
        this.dateFinPeriode = dateFinPeriode;
    }

    public void setForIdCompteCourant(String forIdCompteCourant) {
        this.forIdCompteCourant = forIdCompteCourant;
    }

}
