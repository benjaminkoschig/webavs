/**
 *
 */
package globaz.helios.db.comptes;

import globaz.helios.process.CGPeriodeComptableImportJournalDebit;

/**
 * @author sel
 * 
 */
public class CGPeriodeComptableImportJournalDebitViewBean extends CGPeriodeComptableImportJournalDebit {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void _validate() throws Exception {

        if ((getIdPeriodeComptable() == null) || getIdPeriodeComptable().equals("")
                || getIdPeriodeComptable().equals("0")) {
            getSession().getLabel("GLOBAL_PERIODE_INVALIDE");
            return;
        }

        if (!CGPeriodeComptable.CS_MENSUEL.equals(retrievePeriodeComptable().getIdTypePeriode())) {
            this._addError(getSession().getLabel("JOURNALDEBIT_ERREUR_PERIODE_NOT_MENSUEL") + " ["
                    + getIdPeriodeComptable() + "]");
            return;
        }

        if (!retrievePeriodeComptable().isEstCloture()) {
            this._addError(getSession().getLabel("JOURNALDEBIT_ERREUR_PERIODE_NOT_CLOTURE") + " ["
                    + getIdPeriodeComptable() + "]");
            return;
        }
    }

}
