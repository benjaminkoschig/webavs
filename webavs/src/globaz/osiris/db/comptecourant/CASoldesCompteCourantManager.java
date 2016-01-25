package globaz.osiris.db.comptecourant;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteCourant;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CASection;

public class CASoldesCompteCourantManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateValeur;
    private String forDateValeurSection;
    private String forIdCompteAnnexe;
    private String forIdRubriqueCompteCourant;
    private String forSelectionRole;

    /**
     * ACR original query :
     * 
     * select idexternerole,se.idexterne,cc.idexterne,sum(montant) from h512web.caoperp op inner join h512web.cacptap ca
     * on (op.idcompteannexe=ca.idcompteannexe) inner join h512web.casectp se on (op.idsection=se.idsection) inner join
     * h512web.cacptcp cc on (op.idcomptecourant=cc.idcomptecourant) inner join h512web.cajourp jo on
     * (op.idjournal=jo.idjournal) where op.etat = 205002 and idtypeoperation like 'E%' and idrole = 517002 and
     * jo.datevaleurcg <=20070430 group by idexternerole,se.idexterne,cc.idexterne having sum(montant) <> 0 order by
     * idexternerole,se.idexterne,cc.idexterne
     */
    @Override
    protected String _getSql(BStatement statement) {
        String sql = "select ";

        sql += CACompteAnnexe.FIELD_IDEXTERNEROLE + ", ca." + CACompteAnnexe.FIELD_DESCRIPTION + ", se."
                + CASection.FIELD_IDEXTERNE + ", cc." + CACompteCourant.FIELD_IDEXTERNE + " as "
                + CACompteCourant.FIELD_IDEXTERNE + "CC , sum(" + CAOperation.FIELD_MONTANT + ") as "
                + CAOperation.FIELD_MONTANT + ", se." + CASection.FIELD_IDSECTION + " as " + CASection.FIELD_IDSECTION
                + " ";
        sql += "from " + _getCollection() + CAOperation.TABLE_CAOPERP + " op ";
        sql += "inner join " + _getCollection() + CACompteAnnexe.TABLE_CACPTAP + " ca on (op."
                + CAOperation.FIELD_IDCOMPTEANNEXE + " = ca." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + ")";
        sql += "inner join " + _getCollection() + CASection.TABLE_CASECTP + " se on (op." + CAOperation.FIELD_IDSECTION
                + " = se." + CASection.FIELD_IDSECTION + ")";
        sql += "inner join " + _getCollection() + CACompteCourant.TABLE_CACPTCP + " cc on (op."
                + CAOperation.FIELD_IDCOMPTECOURANT + " = cc." + CACompteCourant.FIELD_IDCOMPTECOURANT + ")";
        sql += "inner join " + _getCollection() + CAJournal.TABLE_CAJOURP + " jo on (op." + CAOperation.FIELD_IDJOURNAL
                + " = jo." + CAJournal.FIELD_IDJOURNAL + ")";

        sql += "where ";
        sql += "op." + CAOperation.FIELD_ETAT + " = " + APIOperation.ETAT_COMPTABILISE + " and ";
        sql += "op." + CAOperation.FIELD_IDTYPEOPERATION + " like '" + APIOperation.CAECRITURE + "%' and ";

        if (!JadeStringUtil.isBlank(getForSelectionRole())) {
            sql += "ca." + CACompteAnnexe.FIELD_IDROLE + " in (" + getForSelectionRole() + ") and ";
        }

        if (!JadeStringUtil.isBlank(forIdRubriqueCompteCourant)) {
            sql += "cc." + CACompteCourant.FIELD_IDRUBRIQUE + " = " + forIdRubriqueCompteCourant + " and ";
        }

        if (!JadeStringUtil.isBlank(forIdCompteAnnexe)) {
            sql += "ca." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + " = " + forIdCompteAnnexe + " and ";
        }

        if (!JadeStringUtil.isBlank(forDateValeurSection)) {
            sql += "(" + forDateValeurSection + " >=" + "se." + CASection.FIELD_DATEDEBUTPERIODE + " and ";
            sql += forDateValeurSection + " <=" + "se." + CASection.FIELD_DATEFINPERIODE + ") ";
        }

        sql += "group by " + CACompteAnnexe.FIELD_IDEXTERNEROLE + ", ca." + CACompteAnnexe.FIELD_DESCRIPTION + ", se."
                + CASection.FIELD_IDEXTERNE + ", cc." + CACompteCourant.FIELD_IDEXTERNE + ", se."
                + CASection.FIELD_IDSECTION + " ";

        sql += "order by " + CACompteAnnexe.FIELD_IDEXTERNEROLE + ", ca." + CACompteAnnexe.FIELD_DESCRIPTION + ", se."
                + CASection.FIELD_IDEXTERNE + ", cc." + CACompteCourant.FIELD_IDEXTERNE + ", se."
                + CASection.FIELD_IDSECTION;

        return sql;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CASoldesCompteCourant();
    }

    public String getForDateValeur() {
        return forDateValeur;
    }

    public String getForSelectionRole() {
        return forSelectionRole;
    }

    public void setForDateValeur(String forDateValeur) {
        this.forDateValeur = forDateValeur;
    }

    public void setForDateValeurSection(String forDateValeurSection) {
        this.forDateValeurSection = forDateValeurSection;
    }

    public void setForIdCompteAnnexe(String forIdCompteAnnexe) {
        this.forIdCompteAnnexe = forIdCompteAnnexe;
    }

    public void setForIdRubriqueCompteCourant(String forIdRubriqueCompteCourant) {
        this.forIdRubriqueCompteCourant = forIdRubriqueCompteCourant;
    }

    public void setForSelectionRole(String forSelectionRole) {
        this.forSelectionRole = forSelectionRole;
    }

}
