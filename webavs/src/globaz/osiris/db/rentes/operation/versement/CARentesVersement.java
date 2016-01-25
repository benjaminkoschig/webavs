package globaz.osiris.db.rentes.operation.versement;

import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.rentes.operation.ecriture.CARentesEcriture;

public class CARentesVersement extends CARentesEcriture {

    public CARentesVersement(BTransaction transaction) throws Exception {
        super(transaction);
    }

    @Override
    public void fillSpecialVariables(int index) throws Exception {
        super.fillSpecialVariables(index);
        index = index + 4;

        insert.setInt(index, JadeStringUtil.parseInt(getIdOrdreVersement(), 0));

    }

    @Override
    protected String getIdTypeOperation() {
        return "'" + APIOperation.CAVERSEMENT + "'";
    }

    @Override
    protected String getPreparedSqlQuerySpecialFields(BTransaction transaction) {
        String fields = super.getPreparedSqlQuerySpecialFields(transaction);
        fields += ", " + CAOperation.FIELD_IDORDRE + " ";
        return fields;
    }

    @Override
    protected String getPreparedSqlQuerySpecialValues(BTransaction transaction) {
        String values = super.getPreparedSqlQuerySpecialValues(transaction);
        values += ", ? ";

        return values;
    }

}