/**
 * 
 */
package globaz.corvus.db.restitution;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author sel
 * 
 */
public class RERestitutionsSoldes extends BEntity {
    public List<REMotifsRestitutionsSoldes> getMotifsRestitutions() {
        return Collections.unmodifiableList(motifsRestitutions);
    }

    public void setMotifsRestitutions(List<REMotifsRestitutionsSoldes> motifsRestitutions) {
        this.motifsRestitutions = motifsRestitutions;
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_FICTIF_CONTENTIEUX = "CONTENTIEUX";
    public static final String FIELD_FICTIF_SOLDE = "SOLDE";

    private String contentieux = "";
    private String dateSection = "";
    private String nomPrenom = "";
    private String nss = "";
    private String numSection = "";
    private String solde = "";
    private String idSection = "";
    private List<REMotifsRestitutionsSoldes> motifsRestitutions = new ArrayList<REMotifsRestitutionsSoldes>();

    public String getIdSection() {
        return idSection;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

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
        contentieux = statement.dbReadString(RERestitutionsSoldes.FIELD_FICTIF_CONTENTIEUX);
        dateSection = statement.dbReadDateAMJ(CASection.FIELD_DATESECTION);
        nomPrenom = statement.dbReadString(CACompteAnnexe.FIELD_DESCRIPTION);
        nss = statement.dbReadString(CACompteAnnexe.FIELD_IDEXTERNEROLE);
        numSection = statement.dbReadString(CASection.FIELD_IDEXTERNE);
        solde = statement.dbReadNumeric(RERestitutionsSoldes.FIELD_FICTIF_SOLDE, 2);
        idSection = statement.dbReadString(CASection.FIELD_IDSECTION);
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
     * @return the contentieux
     */
    public String getContentieux() {
        return contentieux;
    }

    /**
     * @return the dateSection
     */
    public String getDateSection() {
        return dateSection;
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
     * @return the solde
     */
    public String getSolde() {
        return solde;
    }

    /**
     * @param contentieux
     *            the contentieux to set
     */
    public void setContentieux(String contentieux) {
        this.contentieux = contentieux;
    }

    /**
     * @param dateSection
     *            the dateSection to set
     */
    public void setDateSection(String dateSection) {
        this.dateSection = dateSection;
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
     * @param solde
     *            the solde to set
     */
    public void setSolde(String solde) {
        this.solde = solde;
    }

}
