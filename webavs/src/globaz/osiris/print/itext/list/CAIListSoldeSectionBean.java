package globaz.osiris.print.itext.list;

import globaz.framework.util.FWCurrency;
import globaz.globall.util.JANumberFormatter;
import globaz.osiris.db.comptes.CASection;
import java.math.BigDecimal;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class CAIListSoldeSectionBean {

    private java.lang.String compteAnnexe = "";
    private boolean empty = true;
    private String m_contentieux = new String();
    private String m_date = new String();
    private BigDecimal m_montantBase = new BigDecimal(0);
    private BigDecimal m_pmtComp = new BigDecimal(0);
    private String m_sectionDesc = new String();
    private BigDecimal m_soldeSection = new BigDecimal(0);
    private String soldeCompte = new String();

    public CAIListSoldeSectionBean() {
    }

    public CAIListSoldeSectionBean(CASection entity) {
        add(entity);
    }

    /**
     * Cette méthode récupère la description du compte annexe
     * 
     * @return java.lang.String
     */
    private String _getCompteAnnexe(CASection entity) {
        try {
            compteAnnexe = entity.getCompteAnnexe().getRole().getDescription() + " "
                    + entity.getCompteAnnexe().getIdExterneRole() + " " + entity.getCompteAnnexe().getTiers().getNom();
            soldeCompte = entity.getSoldeFormate();
        } catch (Exception e) {
            compteAnnexe = null;
        }
        return compteAnnexe;
    }

    /**
     * Cette méthode permet de retourner le montant de base de la section Date de création : (10.06.2003 08:14:14)
     * 
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal _getMontantBase(CASection section) {
        // Montant de base
        FWCurrency base = new FWCurrency(0.0);
        base.add(section.getSolde());
        base.sub(section.getPmtCmp());
        return new java.math.BigDecimal(JANumberFormatter.deQuote(base.toString()));
    }

    /**
     * Method _getPmtComp.
     * 
     * @return java.math.BigDecimal
     */
    private java.math.BigDecimal _getPmtComp(CASection section) {
        return new java.math.BigDecimal(JANumberFormatter.deQuote(section.getPmtCmp()));
    }

    /**
     * Method _getSoldeSection.
     * 
     * @return java.math.BigDecimal
     */
    private java.math.BigDecimal _getSoldeSection(CASection section) {
        return new java.math.BigDecimal(JANumberFormatter.deQuote(section.getSolde()));
    }

    /**
     * Cette méthode permet d'ajouter des valeurs dans le bean
     */
    public void add(CASection entity) {
        try {
            setCompteAnnexe(_getCompteAnnexe(entity));
            setSectionDesc(entity.getIdExterne() + " " + entity.getDescription());
            setDate(entity.getDateSection());
            setContentieux(entity.getResumeContentieux());
            setMontantBase(_getMontantBase(entity));
            setPmtComp(_getPmtComp(entity));
            setSoldeSection(_getSoldeSection(entity));
            setEmpty(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retourne le compte annexe.
     * 
     * @return String
     */
    public String getCOL_1() {
        return getCompteAnnexe();
    }

    /**
     * Retourne la description de la section.
     * 
     * @return String
     */
    public String getCOL_2() {
        return getSectionDesc();
    }

    /**
     * Retourne la date de la section.
     * 
     * @return String
     */
    public String getCOL_3() {
        return getDate();
    }

    /**
     * Retourne l'état du contentieux.
     * 
     * @return String
     */
    public String getCOL_4() {
        return getContentieux();
    }

    /**
     * Retourne le montant de base.
     * 
     * @return BigDecimal
     */
    public BigDecimal getCOL_5() {
        return getMontantBase();
    }

    /**
     * Retourne le paiement / la compensation.
     * 
     * @return BigDecimal
     */
    public BigDecimal getCOL_6() {
        return getPmtComp();
    }

    /**
     * Retourne le solde de la section.
     * 
     * @return BigDecimal
     */
    public BigDecimal getCOL_7() {
        return getSoldeSection();
    }

    public String getCOL_8() {
        return soldeCompte;
    }

    /**
     * Retourne le compte annexe (compteAnnexe).
     * 
     * @return String
     */
    protected String getCompteAnnexe() {
        return compteAnnexe;
    }

    /**
     * Returns the m_contentieux.
     * 
     * @return String
     */
    protected String getContentieux() {
        return m_contentieux;
    }

    /**
     * Retourne la date de la section.
     * 
     * @return String
     */
    protected String getDate() {
        return m_date;
    }

    /**
     * Returns the m_montantBase.
     * 
     * @return BigDecimal
     */
    protected BigDecimal getMontantBase() {
        return m_montantBase;
    }

    /**
     * Returns the m_pmtComp.
     * 
     * @return BigDecimal
     */
    protected BigDecimal getPmtComp() {
        return m_pmtComp;
    }

    /**
     * Returns the m_sectionDesc.
     * 
     * @return String
     */
    protected String getSectionDesc() {
        return m_sectionDesc;
    }

    /**
     * Returns the m_soldeSection.
     * 
     * @return BigDecimal
     */
    protected BigDecimal getSoldeSection() {
        return m_soldeSection;
    }

    /**
     * Returns the empty.
     * 
     * @return boolean
     */
    public boolean isEmpty() {
        return empty;
    }

    /**
     * Sets the compteAnnexe.
     * 
     * @param compteAnnexe
     *            The compteAnnexe to set
     */
    protected void setCompteAnnexe(String cptAnnexe) {
        compteAnnexe = cptAnnexe;
        setEmpty(false);
    }

    /**
     * Sets the contentieux.
     * 
     * @param contentieux
     *            The contentieux to set
     */
    protected void setContentieux(String contentieux) {
        m_contentieux = contentieux;
        setEmpty(false);
    }

    /**
     * Sets the m_date.
     * 
     * @param m_date
     *            The date to set
     */
    protected void setDate(String date) {
        m_date = date;
        setEmpty(false);
    }

    /**
     * Sets the empty.
     * 
     * @param empty
     *            The empty to set
     */
    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    /**
     * Sets the m_montantBase.
     * 
     * @param m_montant
     *            The m_montant to set
     */
    protected void setMontantBase(BigDecimal montantBase) {
        m_montantBase = montantBase;
        setEmpty(false);

    }

    /**
     * Sets the m_pmtComp.
     * 
     * @param m_pmtComp
     *            The m_pmtComp to set
     */
    protected void setPmtComp(BigDecimal pmtComp) {
        m_pmtComp = pmtComp;
        setEmpty(false);

    }

    /**
     * Sets the m_sectionDesc.
     * 
     * @param m_sectionDesc
     *            The m_sectionDesc to set
     */
    protected void setSectionDesc(String sectionDesc) {
        m_sectionDesc = sectionDesc;
        setEmpty(false);
    }

    /**
     * Sets the m_soldeSection.
     * 
     * @param m_soldeSection
     *            The m_soldeSection to set
     */
    protected void setSoldeSection(BigDecimal soldeSection) {
        m_soldeSection = soldeSection;
        setEmpty(false);
    }

}