package globaz.aquila.db.access.journal;

import globaz.aquila.api.ICOContentieuxConstante;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;

/**
 * <H1>Description</H1>
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class COElementJournalBatchManager extends BManager {

    private static final long serialVersionUID = -2760889673157734494L;

    private String forCsEtat;
    private String forIdJournal;
    private String forIdTraitementSpecifique;
    private String forNumeroAffilieLike;

    private String fromNumero;

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + COElementJournalBatch.TABLE_NAME + " a, " + _getCollection()
                + ICOContentieuxConstante.TABLE_NAME_AVS + " b, " + _getCollection() + CACompteAnnexe.TABLE_CACPTAP
                + " c";
    }

    /**
     * Retourne la clause ORDER BY de la requete SQL (la table). Order by idjournal, idtransition
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "a." + COElementJournalBatch.FNAME_ID_JOURNAL + " DESC, a." + COElementJournalBatch.FNAME_ID_TRANSITION
                + ", c." + CACompteAnnexe.FIELD_IDEXTERNEROLE + " asc, c." + CACompteAnnexe.FIELD_IDROLE + " asc";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String where = "a." + COElementJournalBatch.FNAME_ID_CONTENTIEUX + " = b."
                + ICOContentieuxConstante.FNAME_ID_CONTENTIEUX;
        where += " AND b." + ICOContentieuxConstante.FNAME_ID_COMPTE_ANNEXE + " = c."
                + CACompteAnnexe.FIELD_IDCOMPTEANNEXE;

        if (!JadeStringUtil.isBlank(getForIdJournal())) {
            where += " AND a." + COElementJournalBatch.FNAME_ID_JOURNAL + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdJournal());
        }

        if (!JadeStringUtil.isBlank(getFromNumero())) {
            where += " AND a." + COElementJournalBatch.FNAME_ID_ELEMENT_JOURNAL + " >= "
                    + this._dbWriteNumeric(statement.getTransaction(), getFromNumero());
        }

        if (!JadeStringUtil.isBlank(getForCsEtat())) {
            where += " AND a." + COElementJournalBatch.FNAME_CS_ETAT + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForCsEtat());
        }

        if (!JadeStringUtil.isBlank(getForIdTraitementSpecifique())) {
            where += " AND a." + COElementJournalBatch.FNAME_ID_TRAITEMENTSPECIFIQUE + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdTraitementSpecifique());
        }

        if (!JadeStringUtil.isBlank(getForNumeroAffilieLike())) {
            where += " AND c." + CACompteAnnexe.FIELD_IDEXTERNEROLE + " like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getForNumeroAffilieLike() + "%");
        }

        return where;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new COElementJournalBatch();
    }

    public String getForCsEtat() {
        return forCsEtat;
    }

    public String getForIdJournal() {
        return forIdJournal;
    }

    public String getForIdTraitementSpecifique() {
        return forIdTraitementSpecifique;
    }

    public String getForNumeroAffilieLike() {
        return forNumeroAffilieLike;
    }

    public String getFromNumero() {
        return fromNumero;
    }

    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public void setForIdJournal(String forIdJournal) {
        this.forIdJournal = forIdJournal;
    }

    public void setForIdTraitementSpecifique(String forIdTraitementSpecifique) {
        this.forIdTraitementSpecifique = forIdTraitementSpecifique;
    }

    public void setForNumeroAffilieLike(String forNumeroAffilie) {
        forNumeroAffilieLike = forNumeroAffilie;
    }

    public void setFromNumero(String fromNumero) {
        this.fromNumero = fromNumero;
    }

}
