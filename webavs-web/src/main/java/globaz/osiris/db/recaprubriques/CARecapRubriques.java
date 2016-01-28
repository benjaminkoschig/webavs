package globaz.osiris.db.recaprubriques;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAOperation;

/**
 * @author dda CAOPERP table entity. Use for the "Extrait de Compte" function.
 */
public class CARecapRubriques extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // CACAPTAP variable
    private String idCompteAnnexe;
    private String idExterneRole;

    private String idTiers;
    private String sumMasse;
    // CAOPERP variable
    private String sumMontant;

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
        setSumMontant(statement.dbReadNumeric(CAOperation.FIELD_MONTANT));
        setSumMasse(statement.dbReadNumeric(CAOperation.FIELD_MASSE));

        setIdCompteAnnexe(statement.dbReadNumeric(CACompteAnnexe.FIELD_IDCOMPTEANNEXE));
        setIdTiers(statement.dbReadNumeric(CACompteAnnexe.FIELD_IDTIERS));
        setIdExterneRole(statement.dbReadString(CACompteAnnexe.FIELD_IDEXTERNEROLE));
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

    /**
     * @return
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * @return
     */
    public String getIdExterneRole() {
        return idExterneRole;
    }

    /**
     * @return
     */
    public String getIdTiers() {
        return idTiers;
    }

    public String getNomTiers() {
        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(getSession());

        compteAnnexe.setIdTiers(getIdTiers());

        return compteAnnexe.getTiers().getNom();
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
     * @return
     */
    public String getSumMasse() {
        return sumMasse;
    }

    /**
     * @return
     */
    public String getSumMontant() {
        return sumMontant;
    }

    /**
     * @param string
     */
    public void setIdCompteAnnexe(String string) {
        idCompteAnnexe = string;
    }

    /**
     * @param string
     */
    public void setIdExterneRole(String string) {
        idExterneRole = string;
    }

    /**
     * @param string
     */
    public void setIdTiers(String string) {
        idTiers = string;
    }

    /**
     * @param string
     */
    public void setSumMasse(String string) {
        sumMasse = string;
    }

    /**
     * @param string
     */
    public void setSumMontant(String string) {
        sumMontant = string;
    }

}
