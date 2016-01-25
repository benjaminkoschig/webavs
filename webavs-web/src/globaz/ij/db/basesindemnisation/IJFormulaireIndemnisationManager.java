package globaz.ij.db.basesindemnisation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJFormulaireIndemnisationManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdBaseIndemnisation = "";
    private String forIdInstitutionResponsable = "";
    private String notForIdFormulaireIndemnisation = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer retValue = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(forIdBaseIndemnisation)) {
            if (retValue.length() > 0) {
                retValue.append(" AND ");
            }

            retValue.append(IJFormulaireIndemnisation.FIELDNAME_IDINDEMNISATION);
            retValue.append("=");
            retValue.append(forIdBaseIndemnisation);
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdInstitutionResponsable)) {
            if (retValue.length() > 0) {
                retValue.append(" AND ");
            }

            retValue.append(IJFormulaireIndemnisation.FIELDNAME_IDINSTITUTIONRESPONSABLE);
            retValue.append("=");
            retValue.append(forIdInstitutionResponsable);
        }

        if (!JadeStringUtil.isIntegerEmpty(notForIdFormulaireIndemnisation)) {
            if (retValue.length() > 0) {
                retValue.append(" AND ");
            }

            retValue.append(IJFormulaireIndemnisation.FIELDNAME_IDFORMULAIREINDEMNISATION);
            retValue.append("<>");
            retValue.append(notForIdFormulaireIndemnisation);
        }

        return retValue.toString();
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJFormulaireIndemnisation();
    }

    /**
     * getter pour l'attribut for id base indemnisation.
     * 
     * @return la valeur courante de l'attribut for id base indemnisation
     */
    public String getForIdBaseIndemnisation() {
        return forIdBaseIndemnisation;
    }

    /**
     * getter pour l'attribut for id agent execution.
     * 
     * @return la valeur courante de l'attribut for id agent execution
     */
    public String getForIdInstitutionResponsable() {
        return forIdInstitutionResponsable;
    }

    /**
     * getter pour l'attribut not for id formulaire indemnisation.
     * 
     * @return la valeur courante de l'attribut not for id formulaire indemnisation
     */
    public String getNotForIdFormulaireIndemnisation() {
        return notForIdFormulaireIndemnisation;
    }

    /**
     * setter pour l'attribut for id base indemnisation.
     * 
     * @param forIdBaseIndemnisation
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdBaseIndemnisation(String forIdBaseIndemnisation) {
        this.forIdBaseIndemnisation = forIdBaseIndemnisation;
    }

    /**
     * setter pour l'attribut for id agent execution.
     * 
     * @param forIdAgentExecution
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdInstitutionResponsable(String forIdAgentExecution) {
        forIdInstitutionResponsable = forIdAgentExecution;
    }

    /**
     * setter pour l'attribut not for id formulaire indemnisation.
     * 
     * @param notForIdFormulaireIndemnisation
     *            une nouvelle valeur pour cet attribut
     */
    public void setNotForIdFormulaireIndemnisation(String notForIdFormulaireIndemnisation) {
        this.notForIdFormulaireIndemnisation = notForIdFormulaireIndemnisation;
    }
}
