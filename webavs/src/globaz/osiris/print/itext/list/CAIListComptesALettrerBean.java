package globaz.osiris.print.itext.list;

import globaz.globall.util.JANumberFormatter;
import globaz.osiris.db.comptes.CASection;
import java.math.BigDecimal;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class CAIListComptesALettrerBean {

    private String compteAnnexe = "";
    private boolean empty = true;
    private String m_date = new String();
    private String m_sectionDesc = new String();
    private BigDecimal m_soldeCompteAnnexe = new BigDecimal(0);
    private BigDecimal m_soldeSection = new BigDecimal(0);

    public CAIListComptesALettrerBean() {
    }

    public CAIListComptesALettrerBean(CASection entity) {
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
        } catch (Exception e) {
            compteAnnexe = null;
        }
        return compteAnnexe;
    }

    /**
     * Récupère le solde du compte annexe retourne null si le compte annexe n'a pas pu être récupéré
     * 
     * @return java.lang.String
     */
    private BigDecimal _getSoldeCompteAnnexe(CASection entity) {
        try {
            m_soldeCompteAnnexe = new BigDecimal(JANumberFormatter.deQuote(entity.getCompteAnnexe().getSolde()));
        } catch (Exception e) {
            m_soldeCompteAnnexe = null;
        }
        return m_soldeCompteAnnexe;
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
            setSoldeCompteAnnexe(_getSoldeCompteAnnexe(entity));
            setSectionDesc(entity.getIdExterne() + " " + entity.getDescription());
            setDate(entity.getDateSection());
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
     * Retourne le solde de la section.
     * 
     * @return BigDecimal
     */
    public BigDecimal getCOL_7() {
        return getSoldeSection();
    }

    /**
     * Retourne le solde de la section.
     * 
     * @return BigDecimal
     */
    public BigDecimal getCOL_8() {
        return getSoldeCompteAnnexe();
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
     * Retourne la date de la section.
     * 
     * @return String
     */
    protected String getDate() {
        return m_date;
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
     * @return
     */
    public BigDecimal getSoldeCompteAnnexe() {
        return m_soldeCompteAnnexe;
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
     * @param decimal
     */
    public void setSoldeCompteAnnexe(BigDecimal decimal) {
        m_soldeCompteAnnexe = decimal;
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