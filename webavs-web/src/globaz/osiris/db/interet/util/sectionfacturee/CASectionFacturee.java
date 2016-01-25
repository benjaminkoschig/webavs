package globaz.osiris.db.interet.util.sectionfacturee;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CASection;

public class CASectionFacturee extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String idCompteCourant;

    private String idSection;
    private String montant;

    private CASection section;

    @Override
    protected String _getTableName() {
        return CAOperation.TABLE_CAOPERP;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setMontant(statement.dbReadNumeric(CAOperation.FIELD_MONTANT));
        setIdSection(statement.dbReadNumeric(CAOperation.FIELD_IDSECTION));
        setIdCompteCourant(statement.dbReadNumeric(CAOperation.FIELD_IDCOMPTECOURANT));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Do nothing.
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // Do nothing.
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Do nothing.
    }

    public String getIdCompteCourant() {
        return idCompteCourant;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getMontant() {
        return montant;
    }

    public FWCurrency getMontantAsCurrency() {
        return new FWCurrency(getMontant());
    }

    public CASection getSection() throws Exception {
        if (section == null) {
            section = new CASection();
            section.setSession(getSession());

            section.setIdSection(getIdSection());

            section.retrieve();

            if (section.hasErrors() || section.isNew()) {
                throw new Exception(getSession().getLabel("5126"));
            }
        }

        return section;
    }

    public void setIdCompteCourant(String idCompteCourant) {
        this.idCompteCourant = idCompteCourant;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

}
