package globaz.osiris.db.listfsfp;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CASection;

/**
 * @author BJO
 * 
 */
public class CANominativeFsfp extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idCompteAnnexe;
    // De la table section(CASECTP)
    private String idExterne;
    // De la table compteAnnexe(CACPTAP)
    private String idExterneRole;

    private String idSection;
    private String idTiers;

    // De la table operation(CAOPERP)
    private String sommeMontant;

    /**
     * Return nothing. Entity est utilisé uniquement par un Inner-Join.
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
        setIdExterneRole(statement.dbReadString(CACompteAnnexe.FIELD_IDEXTERNEROLE));
        setIdTiers(statement.dbReadNumeric(CACompteAnnexe.FIELD_IDTIERS));
        setIdExterne(statement.dbReadString(CASection.FIELD_IDEXTERNE));
        setIdCompteAnnexe(statement.dbReadString(CACompteAnnexe.FIELD_IDCOMPTEANNEXE));
        setIdSection(statement.dbReadString(CASection.FIELD_IDSECTION));
        setSommeMontant(statement.dbReadNumeric(CAOperation.FIELD_MONTANT));
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

    /**
     * @return Returns the idExterne.
     */
    public String getIdExterne() {
        return idExterne;
    }

    public String getIdExterneRole() {
        return idExterneRole;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getSommeMontant() {
        return sommeMontant;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    /**
     * @param idExterne
     *            The idExterne to set.
     */
    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setIdExterneRole(String idExterneRole) {
        this.idExterneRole = idExterneRole;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setSommeMontant(String sommeMontant) {
        this.sommeMontant = sommeMontant;
    }

    /**
     * @return
     * @author
     */
    public String toMyString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("CAListNominativeFsfp[");
        buffer.append("idExterneRole = ").append(idExterneRole);
        buffer.append("idTiers = ").append(idTiers);
        buffer.append("idExterne = ").append(idExterne);
        buffer.append("idCompteAnnexe = ").append(idCompteAnnexe);
        buffer.append("idSection = ").append(idSection);
        buffer.append("sommeMontant = ").append(sommeMontant);
        buffer.append("]");
        return buffer.toString();
    }

}
