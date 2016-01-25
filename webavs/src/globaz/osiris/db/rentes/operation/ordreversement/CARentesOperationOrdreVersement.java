package globaz.osiris.db.rentes.operation.ordreversement;

import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.rentes.operation.CARentesOperation;

public class CARentesOperationOrdreVersement extends CARentesOperation {

    public CARentesOperationOrdreVersement(BTransaction transaction) throws Exception {
        super(transaction);
    }

    @Override
    public void fillSpecialVariables(int index) throws Exception {
        insert.setDouble(index, JadeStringUtil.parseDouble(getMontant(), 0.00));

    }

    /**
	 * 
	 */
    @Override
    protected String getIdTypeOperation() {
        return "'" + APIOperation.CAOPERATIONORDREVERSEMENT + "'";
    }

    @Override
    protected String getPreparedSqlQuerySpecialFields(BTransaction transaction) {
        return CAOperation.FIELD_MONTANT;
    }

    @Override
    protected String getPreparedSqlQuerySpecialValues(BTransaction transaction) {
        return "?";
    }

}
