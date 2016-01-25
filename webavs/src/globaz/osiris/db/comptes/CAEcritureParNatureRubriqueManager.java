package globaz.osiris.db.comptes;

import globaz.globall.db.BStatement;
import globaz.osiris.api.APIOperation;

/**
 * Insérez la description du type ici. Date de création : (06.02.2002 14:08:00)
 * 
 * @author: Administrator
 */
public class CAEcritureParNatureRubriqueManager extends CAOperationManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean conditionSelectEtat = new Boolean(false);
    private String forIdJournalPassage = "";

    @Override
    protected String _getWhere(BStatement statement) {

        // Récupérer depuis la superclasse
        String sqlWhere = super._getWhere(statement);

        if (getConditionSelectEtat() == true) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(ETAT = " + APIOperation.ETAT_COMPTABILISE + " OR ETAT IN (" + APIOperation.ETAT_OUVERT + ", "
                    + APIOperation.ETAT_PROVISOIRE + ", " + APIOperation.ETAT_COMPTABILISE + ") AND IDJOURNAL = "
                    + getForIdJournalPassage() + ")";
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CAEcriture();
    }

    /**
     * @return the conditionSelectEtat
     */
    public Boolean getConditionSelectEtat() {
        return conditionSelectEtat;
    }

    /**
     * @return the forIdJournalPassage
     */
    public String getForIdJournalPassage() {
        return forIdJournalPassage;
    }

    /**
     * @param conditionSelectEtat
     *            the conditionSelectEtat to set
     */
    public void setConditionSelectEtat(Boolean conditionSelectEtat) {
        this.conditionSelectEtat = conditionSelectEtat;
    }

    /**
     * @param forIdJournalPassage
     *            the forIdJournalPassage to set
     */
    public void setForIdJournalPassage(String forIdJournalPassage) {
        this.forIdJournalPassage = forIdJournalPassage;
    }

}
