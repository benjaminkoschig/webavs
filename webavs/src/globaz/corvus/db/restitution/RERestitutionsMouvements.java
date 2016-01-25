/**
 * 
 */
package globaz.corvus.db.restitution;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CASection;

/**
 * @author sel
 * 
 */
public class RERestitutionsMouvements extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_DESCRIPTION = "DESCRIPTION";
    public static final String FIELD_NUM_RUBRIQUE = "NUMRUBRIQUE";

    private String ecritureDate = "";
    private String ecritureLibelle = "";
    private String ecritureMontant = "";
    private String nomPrenom = "";
    private String nss = "";
    private String numSection = "";
    private String rubriqueDescription = "";
    private String rubriqueNumero = "";

    /**
     * @return false
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * @return false
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * @return false
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * @return null
     */
    @Override
    protected String _getTableName() {
        return null; // PAS DE TABLES !!!
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        nomPrenom = statement.dbReadString(CACompteAnnexe.FIELD_DESCRIPTION);
        nss = statement.dbReadString(CACompteAnnexe.FIELD_IDEXTERNEROLE);
        numSection = statement.dbReadString(CASection.FIELD_IDEXTERNE);
        rubriqueNumero = statement.dbReadString(RERestitutionsMouvements.FIELD_NUM_RUBRIQUE);
        rubriqueDescription = statement.dbReadString(RERestitutionsMouvements.FIELD_DESCRIPTION);
        ecritureDate = statement.dbReadDateAMJ(CAOperation.FIELD_DATE);
        ecritureLibelle = statement.dbReadString(CAOperation.FIELD_LIBELLE);
        ecritureMontant = statement.dbReadNumeric(CAOperation.FIELD_MONTANT, 2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        _addError(statement.getTransaction(), "interdit d'ajouter");
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
    }

    /**
     * @return the ecritureDate
     */
    public String getEcritureDate() {
        return ecritureDate;
    }

    /**
     * @return the ecritureLibelle
     */
    public String getEcritureLibelle() {
        return ecritureLibelle;
    }

    /**
     * @return the ecritureMontant
     */
    public String getEcritureMontant() {
        return ecritureMontant;
    }

    /**
     * @return the nomPrenom
     */
    public String getNomPrenom() {
        return nomPrenom;
    }

    /**
     * @return the nss
     */
    public String getNss() {
        return nss;
    }

    /**
     * @return the numSection
     */
    public String getNumSection() {
        return numSection;
    }

    /**
     * @return the rubriqueDescription
     */
    public String getRubriqueDescription() {
        return rubriqueDescription;
    }

    /**
     * @return the rubriqueNumero
     */
    public String getRubriqueNumero() {
        return rubriqueNumero;
    }

    /**
     * @param ecritureDate
     *            the ecritureDate to set
     */
    public void setEcritureDate(String ecritureDate) {
        this.ecritureDate = ecritureDate;
    }

    /**
     * @param ecritureLibelle
     *            the ecritureLibelle to set
     */
    public void setEcritureLibelle(String ecritureLibelle) {
        this.ecritureLibelle = ecritureLibelle;
    }

    /**
     * @param ecritureMontant
     *            the ecritureMontant to set
     */
    public void setEcritureMontant(String ecritureMontant) {
        this.ecritureMontant = ecritureMontant;
    }

    /**
     * @param nomPrenom
     *            the nomPrenom to set
     */
    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    /**
     * @param nss
     *            the nss to set
     */
    public void setNss(String nss) {
        this.nss = nss;
    }

    /**
     * @param numSection
     *            the numSection to set
     */
    public void setNumSection(String numSection) {
        this.numSection = numSection;
    }

    /**
     * @param rubriqueDescription
     *            the rubriqueDescription to set
     */
    public void setRubriqueDescription(String rubriqueDescription) {
        this.rubriqueDescription = rubriqueDescription;
    }

    /**
     * @param rubriqueNumero
     *            the rubriqueNumero to set
     */
    public void setRubriqueNumero(String rubriqueNumero) {
        this.rubriqueNumero = rubriqueNumero;
    }
}
