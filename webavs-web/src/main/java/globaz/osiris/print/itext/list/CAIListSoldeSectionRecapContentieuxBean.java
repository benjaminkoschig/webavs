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
public class CAIListSoldeSectionRecapContentieuxBean {

    private boolean empty = true;
    private String m_contentieux = new String();
    private BigDecimal m_montantBase = new BigDecimal(0);
    private int m_nombreCas = 0;
    private BigDecimal m_pmtComp = new BigDecimal(0);
    private BigDecimal m_soldeSection = new BigDecimal(0);

    public CAIListSoldeSectionRecapContentieuxBean() {
    }

    public CAIListSoldeSectionRecapContentieuxBean(CASection entity) {
        add(entity);
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
            setContentieux(entity.getResumeContentieux());
            setNombreCas(1);
            setMontantBase(_getMontantBase(entity));
            setPmtComp(_getPmtComp(entity));
            setSoldeSection(_getSoldeSection(entity));
            setEmpty(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retourne le type de section.
     * 
     * @return String
     */
    public String getCOL_10() {
        return getContentieux();
    }

    /**
     * Returns the m_nombreCas.
     * 
     * @return Integer
     */
    public Integer getCOL_11() {
        return getNombreCas();
    }

    /**
     * Retourne le montant de base.
     * 
     * @return BigDecimal
     */
    public BigDecimal getCOL_12() {
        return getMontantBase();
    }

    /**
     * Retourne le paiement / la compensation.
     * 
     * @return BigDecimal
     */
    public BigDecimal getCOL_13() {
        return getPmtComp();
    }

    /**
     * Retourne le solde de la section.
     * 
     * @return BigDecimal
     */
    public BigDecimal getCOL_14() {
        return getSoldeSection();
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
     * Returns the m_montantBase.
     * 
     * @return BigDecimal
     */
    protected BigDecimal getMontantBase() {
        return m_montantBase;
    }

    /**
     * Returns the m_nombreCas.
     * 
     * @return Integer
     */
    protected Integer getNombreCas() {
        return new Integer(m_nombreCas);
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
        if (m_montantBase.equals(new BigDecimal(0))) {
            m_montantBase = montantBase;
        } else {
            m_montantBase = m_montantBase.add(montantBase);
        }
        setEmpty(false);

    }

    /**
     * Sets the m_nombreCas.
     * 
     * @param m_nombreCas
     *            The m_nombreCas to set
     */
    protected void setNombreCas(int nombreCas) {
        if (m_nombreCas == 0) {
            m_nombreCas = nombreCas;
        } else {
            m_nombreCas = m_nombreCas + nombreCas;
        }
        setEmpty(false);
    }

    /**
     * Sets the m_pmtComp.
     * 
     * @param m_pmtComp
     *            The m_pmtComp to set
     */
    protected void setPmtComp(BigDecimal pmtComp) {
        if (m_pmtComp.equals(new BigDecimal(0))) {
            m_pmtComp = pmtComp;
        } else {
            m_pmtComp = m_pmtComp.add(pmtComp);
        }
        setEmpty(false);

    }

    /**
     * Sets the m_soldeSection.
     * 
     * @param m_soldeSection
     *            The m_soldeSection to set
     */
    protected void setSoldeSection(BigDecimal soldeSection) {
        if (m_soldeSection.equals(new BigDecimal(0))) {
            m_soldeSection = soldeSection;
        } else {
            m_soldeSection = m_soldeSection.add(soldeSection);
        }
        setEmpty(false);
    }

}