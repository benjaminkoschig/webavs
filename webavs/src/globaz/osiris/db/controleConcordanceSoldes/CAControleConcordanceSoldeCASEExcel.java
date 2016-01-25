package globaz.osiris.db.controleConcordanceSoldes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CACompteAnnexe;

public class CAControleConcordanceSoldeCASEExcel extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String CUMULSECTIONS = "CUMULSECTIONS";

    private String idCompteAnnexe;

    private String idExterneRole;

    private String idRole;

    private String soldeCompteAnnexe;
    private String soldeCumulSections;

    @Override
    protected String _getTableName() {
        return "";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdCompteAnnexe(statement.dbReadNumeric(CACompteAnnexe.FIELD_IDCOMPTEANNEXE));
        setIdExterneRole(statement.dbReadString(CACompteAnnexe.FIELD_IDEXTERNEROLE));
        setIdRole(statement.dbReadNumeric(CACompteAnnexe.FIELD_IDROLE));
        setSoldeCumulSections(statement.dbReadNumeric(CUMULSECTIONS, 2));
        setSoldeCompteAnnexe(statement.dbReadNumeric(CACompteAnnexe.FIELD_SOLDE, 2));

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * @return the idcompteAnnexe
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * @return the idExterneRole
     */
    public String getIdExterneRole() {
        return idExterneRole;
    }

    /**
     * @return the idRole
     */
    public String getIdRole() {
        return idRole;
    }

    public String getRoleDescription() {
        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(getSession());

        compteAnnexe.setIdCompteAnnexe(getIdCompteAnnexe());

        try {
            compteAnnexe.retrieve();

            return compteAnnexe.getCARole().getDescription();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @return the soldeCompteAnnexe
     */
    public String getSoldeCompteAnnexe() {
        return soldeCompteAnnexe;
    }

    /**
     * @return the soldeCumulSections
     */
    public String getSoldeCumulSections() {
        return soldeCumulSections;
    }

    /**
     * @param idCompteAnnexe
     *            the idCompteAnnexe to set
     */
    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    /**
     * @param idExterneRole
     *            the idExterneRole to set
     */
    public void setIdExterneRole(String idExterneRole) {
        this.idExterneRole = idExterneRole;
    }

    /**
     * @param idRole
     *            the idRole to set
     */
    public void setIdRole(String idRole) {
        this.idRole = idRole;
    }

    /**
     * @param soldeCompteAnnexe
     *            the soldeCompteAnnexe to set
     */
    public void setSoldeCompteAnnexe(String soldeCompteAnnexe) {
        this.soldeCompteAnnexe = soldeCompteAnnexe;
    }

    /**
     * @param soldeCumulSections
     *            the soldeCumulSections to set
     */
    public void setSoldeCumulSections(String soldeCumulSections) {
        this.soldeCumulSections = soldeCumulSections;
    }
}
