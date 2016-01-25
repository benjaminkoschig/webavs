package globaz.osiris.db.rentes.operation.ecriture;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.rentes.operation.CARentesOperation;

public class CARentesEcriture extends CARentesOperation {

    public CARentesEcriture(BTransaction transaction) throws Exception {
        super(transaction);
    }

    @Override
    public void fillSpecialVariables(int index) throws Exception {
        insert.setInt(index, JadeStringUtil.parseInt(getIdRubrique(), 0));
        index++;

        insert.setDouble(index, JadeStringUtil.parseDouble(getMontant(), 0.00));
        index++;

        insert.setString(index, getLibelle());
        index++;

        if (new FWCurrency(getMontant()).isPositive()) {
            insert.setString(index, APIEcriture.DEBIT);
        } else {
            insert.setString(index, APIEcriture.CREDIT);
        }
        index++;

    }

    /**
	 *
	 */
    @Override
    protected String getIdTypeOperation() {
        return "'" + APIOperation.CAECRITURE + "'";
    }

    /**
     * @param transaction
     * @return
     */
    @Override
    protected String getPreparedSqlQuerySpecialFields(BTransaction transaction) {
        String fields = CAOperation.FIELD_IDCOMPTE + ", ";
        fields += CAOperation.FIELD_MONTANT + ", ";
        fields += CAOperation.FIELD_LIBELLE + ", ";

        fields += CAOperation.FIELD_CODEDEBITCREDIT + ", ";
        fields += CAOperation.FIELD_ANNEECOTISATION + ", ";
        fields += CAOperation.FIELD_IDCONTREPARTIE + ", ";
        fields += CAOperation.FIELD_MASSE + ", ";
        fields += CAOperation.FIELD_NOECRCOL + ", ";
        fields += CAOperation.FIELD_PIECE + ", ";
        fields += CAOperation.FIELD_TAUX + ", ";
        fields += CAOperation.FIELD_IDECHEANCEPLAN + ", ";
        fields += CAOperation.FIELD_IDSECTIONAUX + ", ";
        fields += CAOperation.FIELD_IDCAISSEPROFESSIONNELLE + " ";

        return fields;
    }

    /**
	 *
	 */
    @Override
    protected String getPreparedSqlQuerySpecialValues(BTransaction transaction) {
        String values = "?, ?, ?, ?, ";
        values += "0, 0, 0, 0, '0', 0.00000, 0, 0, 0 ";

        return values;
    }
}
