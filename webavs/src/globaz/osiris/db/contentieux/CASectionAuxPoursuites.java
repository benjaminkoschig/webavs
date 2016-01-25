package globaz.osiris.db.contentieux;

import globaz.aquila.api.ICOHistoriqueConstante;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CASection;
import java.io.Serializable;

public class CASectionAuxPoursuites extends BEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private COHistorique historique = new COHistorique();

    public COHistorique getHistorique() {
        return historique;
    }

    public void setHistorique(COHistorique historique) {
        this.historique = historique;
    }

    public CASectionAuxPoursuites() {
        super();
    }

    @Override
    protected String _getTableName() {
        return CASection.TABLE_CASECTP;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        historique.setIdHistorique(statement.dbReadNumeric(ICOHistoriqueConstante.FNAME_ID_HISTORIQUE));
        historique.setIdContentieux(statement.dbReadNumeric(ICOHistoriqueConstante.FNAME_ID_CONTENTIEUX));
        historique.setIdSequence(statement.dbReadNumeric(ICOHistoriqueConstante.FNAME_ID_SEQUENCE));
        historique.setIdEtape(statement.dbReadNumeric(ICOHistoriqueConstante.FNAME_ID_ETAPE));
        historique.setDateDeclenchement(statement.dbReadDateAMJ(ICOHistoriqueConstante.FNAME_DATE_DECLENCHEMENT));
        historique.setDateExecution(statement.dbReadDateAMJ(ICOHistoriqueConstante.FNAME_DATE_EXECUTION));
        historique.setSolde(statement.dbReadNumeric(ICOHistoriqueConstante.FNAME_SOLDE, 2));
        historique.setTaxes(statement.dbReadNumeric(ICOHistoriqueConstante.FNAME_TAXES, 2));
        historique.setUser(statement.dbReadString(ICOHistoriqueConstante.FNAME_USERNAME));
        historique.setMotif(statement.dbReadString(ICOHistoriqueConstante.FNAME_MOTIF));
        historique.setIdHistoriquePrecedant(statement
                .dbReadNumeric(ICOHistoriqueConstante.FNAME_ID_HISTORIQUE_PRECEDENT));
        historique.setIdJournal(statement.dbReadNumeric(ICOHistoriqueConstante.FNAME_ID_JOURNAL));
        historique.setIdGroupement(statement.dbReadNumeric(ICOHistoriqueConstante.FNAME_ID_GROUPEMENT));
        historique.setTypeJournal(statement.dbReadNumeric(ICOHistoriqueConstante.FNAME_TYPE_JOURNAL));
        historique.setNumPoursuite(statement.dbReadString(ICOHistoriqueConstante.FNAME_NUMERO_POURSUITE));
        historique.setAnnule(statement.dbReadBoolean(ICOHistoriqueConstante.FNAME_EST_ANNULE));
        // historique.setEtapeSansInfluence(statement.dbReadBoolean(ICOHistoriqueConstante.FNAME_ETAPE_SANS_INFLUENCE));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // non implémenté
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // non implémenté
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // non implémenté
    }

}
