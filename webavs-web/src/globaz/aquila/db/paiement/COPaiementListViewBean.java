package globaz.aquila.db.paiement;

import globaz.aquila.api.ICOContentieuxConstante;
import globaz.aquila.db.access.paiement.COPaiementManager;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CASection;

public class COPaiementListViewBean extends COPaiementManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected String _getFields(BStatement statement) {
        String Fields = " op.IDCOMPTEANNEXE,op.IDSECTION,op.DATE,op.MONTANT,op.IDCOMPTE,op.PROVENANCEPMT,co.OAICON,co.OFISEQ ";
        return Fields;
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CAOperation.TABLE_CAOPERP + " op " + INNER_JOIN + _getCollection()
                + CASection.TABLE_CASECTP + " se " + ON + " op." + CAOperation.FIELD_IDSECTION + EGAL + " se."
                + CASection.FIELD_IDSECTION + INNER_JOIN + _getCollection() + CACompteAnnexe.TABLE_CACPTAP + " ca "
                + ON + " op." + CAOperation.FIELD_IDCOMPTEANNEXE + EGAL + " ca." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE
                + INNER_JOIN + " ( " + SELECT + ICOContentieuxConstante.FNAME_ID_CONTENTIEUX + ","
                + ICOContentieuxConstante.FNAME_ID_SECTION + "," + ICOContentieuxConstante.FNAME_ID_SEQUENCE + FROM
                + _getCollection() + ICOContentieuxConstante.TABLE_NAME_AVS + " ) " + " co " + ON + " op."
                + CAOperation.FIELD_IDSECTION + " = " + "co." + ICOContentieuxConstante.FNAME_ID_SECTION;
    }

    @Override
    protected String _getSql(BStatement statement) {
        // TODO Auto-generated method stub
        return super._getSql(statement);
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new COPaiementViewBean();
    }

}
