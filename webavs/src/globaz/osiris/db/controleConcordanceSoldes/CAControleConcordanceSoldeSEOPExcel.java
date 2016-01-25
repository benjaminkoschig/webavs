package globaz.osiris.db.controleConcordanceSoldes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;

public class CAControleConcordanceSoldeSEOPExcel extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String CUMULOPERATIONS = "CUMULOPERATIONS";
    private String idCompteAnnexe;
    private String idExterne;
    private String idExterneRole;

    private String idRole;

    private String idSection;

    private String idTypeSection;

    private String soldeCumulOperations;

    private String soldeSection;

    @Override
    protected String _getTableName() {
        return "";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdCompteAnnexe(statement.dbReadNumeric(CACompteAnnexe.FIELD_IDCOMPTEANNEXE));
        setIdExterne(statement.dbReadString(CASection.FIELD_IDEXTERNE));
        setIdExterneRole(statement.dbReadString(CACompteAnnexe.FIELD_IDEXTERNEROLE));
        setIdRole(statement.dbReadNumeric(CACompteAnnexe.FIELD_IDROLE));
        setIdSection(statement.dbReadNumeric(CASection.FIELD_IDSECTION));
        setIdTypeSection(statement.dbReadNumeric(CASection.FIELD_IDTYPESECTION));
        setSoldeCumulOperations(statement.dbReadNumeric(CAControleConcordanceSoldeSEOPExcel.CUMULOPERATIONS, 2));
        setSoldeSection(statement.dbReadNumeric(CASection.FIELD_SOLDE, 2));

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
     * @return the idCompteAnnexe
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * @return the idExterne
     */
    public String getIdExterne() {
        return idExterne;
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

    /**
     * @return the idSection
     */
    public String getIdSection() {
        return idSection;
    }

    /**
     * @return the idTypeSection
     */
    public String getIdTypeSection() {
        return idTypeSection;
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
     * @return the soldeCumulOperations
     */
    public String getSoldeCumulOperations() {
        return soldeCumulOperations;
    }

    /**
     * @return the soldeSection
     */
    public String getSoldeSection() {
        return soldeSection;
    }

    /**
     * @param idCompteAnnexe
     *            the idCompteAnnexe to set
     */
    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    /**
     * @param idExterne
     *            the idExterne to set
     */
    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
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
     * @param idSection
     *            the idSection to set
     */
    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    /**
     * @param idTypeSection
     *            the idTypeSection to set
     */
    public void setIdTypeSection(String idTypeSection) {
        this.idTypeSection = idTypeSection;
    }

    /**
     * @param soldeCumulOperations
     *            the soldeCumulOperations to set
     */
    public void setSoldeCumulOperations(String soldeCumulOperations) {
        this.soldeCumulOperations = soldeCumulOperations;
    }

    /**
     * @param soldeSection
     *            the soldeSection to set
     */
    public void setSoldeSection(String soldeSection) {
        this.soldeSection = soldeSection;
    }

}
