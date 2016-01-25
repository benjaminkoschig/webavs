package globaz.osiris.db.remboursementauto;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CASection;

/**
 * @author dda
 */
public class CARemboursementAutomatique extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idCompteAnnexe;
    private String idExterneRole;
    private String idRole;

    private String idSection;

    private String montant;

    /**
     * Return le nom de la table (CAOPERP).
     */
    @Override
    protected String _getTableName() {
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdCompteAnnexe(statement.dbReadNumeric(CACompteAnnexe.FIELD_IDCOMPTEANNEXE));
        setIdExterneRole(statement.dbReadString(CACompteAnnexe.FIELD_IDEXTERNEROLE));
        setIdRole(statement.dbReadString(CACompteAnnexe.FIELD_IDROLE));

        setIdSection(statement.dbReadNumeric(CAOperation.FIELD_IDSECTION));
        setMontant(statement.dbReadNumeric(CAOperation.FIELD_MONTANT));

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Not needed here
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdExterneRole() {
        return idExterneRole;
    }

    public String getIdRole() {
        return idRole;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getMontant() {
        return montant;
    }

    public String getMontantAbs() {
        FWCurrency tmp = new FWCurrency(getMontant());
        tmp.abs();
        return tmp.toString();
    }

    public CASection getSection() throws Exception {
        CASection section = new CASection();
        section.setSession(getSession());

        section.setIdSection(getIdSection());

        section.retrieve();

        if (section.isNew() || section.hasErrors()) {
            // TODO dda add label
            throw new Exception("Section non résolue");
        }

        return section;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setIdExterneRole(String idExterneRole) {
        this.idExterneRole = idExterneRole;
    }

    public void setIdRole(String idRole) {
        this.idRole = idRole;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

}
