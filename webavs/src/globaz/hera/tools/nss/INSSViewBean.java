package globaz.hera.tools.nss;

/**
 * Descpription
 * 
 * @author scr Date de création 27 sept. 05
 */
public interface INSSViewBean {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getCsCantonDomicile();

    public String getCsEtatCivil();

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getCsNationalite();

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getCsSexe();

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getDateDeces();

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getDateNaissance();

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getIdAssure();

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getNom();

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getNss();

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getPrenom();

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getProvenance();

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setCsCantonDomicile(String string);

    public void setCsEtatCivil(String s);

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setCsNationalite(String string);

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setCsSexe(String string);

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setDateDeces(String string);

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setDateNaissance(String string);

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setIdAssure(String string);

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setNom(String string);

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setNss(String string);

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setPrenom(String string);

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setProvenance(String string);
}
