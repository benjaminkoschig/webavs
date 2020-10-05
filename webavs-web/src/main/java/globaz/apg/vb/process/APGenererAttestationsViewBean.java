/*
 * Crée le 21 septembre 2006
 */
package globaz.apg.vb.process;

import globaz.commons.nss.NSUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.util.nss.INSSViewBean;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.Calendar;

/**
 * <H1>Description</H1>
 * 
 * ViewBean pour la génération des attestations fiscales qui permet tout d'abord d'afficher une liste d'années à
 * l'affichage de la jsp du process, et ensuite qui envoie en paramètres l'année et l'adresse email.
 * 
 * @author hpe
 */
public class APGenererAttestationsViewBean extends PRAbstractViewBeanSupport implements FWViewBeanInterface,
        INSSViewBean {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String annee = "";
    private String displaySendToGed = "0";
    private String email = "";
    private Boolean isGenerationUnique = Boolean.TRUE;
    private Boolean isSendToGed = Boolean.FALSE;
    private String NSS = "";
    private String typePrestation = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut date.
     * 
     * @return la valeur courante de l'attribut date
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * 
     * @return
     */
    public String[] getAnneesList() {
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        String[] annees = { Integer.toString(year - 5), Integer.toString(year - 5), Integer.toString(year - 4),
                Integer.toString(year - 4), Integer.toString(year - 3), Integer.toString(year - 3),
                Integer.toString(year - 2), Integer.toString(year - 2), Integer.toString(year - 1),
                Integer.toString(year - 1), Integer.toString(year), Integer.toString(year), Integer.toString(year + 1),
                Integer.toString(year + 1) };
        return annees;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getCsCantonDomicile()
     */
    @Override
    public String getCsCantonDomicile() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getCsEtatCivil()
     */
    @Override
    public String getCsEtatCivil() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getCsNationalite()
     */
    @Override
    public String getCsNationalite() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getCsSexe()
     */
    @Override
    public String getCsSexe() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getDateDeces()
     */
    @Override
    public String getDateDeces() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getDateNaissance()
     */
    @Override
    public String getDateNaissance() {

        return null;
    }

    public String getDisplaySendToGed() {
        return displaySendToGed;
    }

    /**
     * getter pour l'attribut email.
     * 
     * @return la valeur courante de l'attribut email
     */
    public String getEmail() {
        if (JadeStringUtil.isEmpty(email) && (getSession() != null)) {
            email = getSession().getUserEMail();
        }

        return email;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getIdAssure()
     */
    @Override
    public String getIdAssure() {

        return null;
    }

    /**
     * @return
     */
    public Boolean getIsGenerationUnique() {
        return isGenerationUnique;
    }

    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getNom()
     */
    @Override
    public String getNom() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getNss()
     */
    @Override
    public String getNss() {

        return null;
    }

    /**
     * @return
     */
    public String getNSS() {
        return NSS;
    }

    /**
     * Méthode qui retourne le NNSS formaté sans le préfixe (756.) ou alors le NSS normal
     * 
     * @return NNSS formaté sans préfixe ou NSS normal
     */
    public String getNumeroAvsFormateSansPrefixe() {
        return NSUtil.formatWithoutPrefixe(getNSS(), isNNSS().equals("true") ? true : false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getPrenom()
     */
    @Override
    public String getPrenom() {

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#getProvenance()
     */
    @Override
    public String getProvenance() {

        return null;
    }

    /**
     * Méthode qui retourne une string avec true si le NSS dans le vb est un NNSS, sinon false
     * 
     * @return String (true ou false)
     */
    public String isNNSS() {

        if (JadeStringUtil.isBlankOrZero(getNSS())) {
            return "";
        }

        if (getNSS().length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public void retrieve() throws Exception {
        // jamais de retrieve sur ce bean
    }

    /**
     * setter pour l'attribut date.
     * 
     * @param date
     *            une nouvelle valeur pour cet attribut
     */
    public void setAnnee(String annee) {
        this.annee = annee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setCsCantonDomicile (java.lang.String)
     */
    @Override
    public void setCsCantonDomicile(String string) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setCsEtatCivil(java .lang.String)
     */
    @Override
    public void setCsEtatCivil(String s) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setCsNationalite(java .lang.String)
     */
    @Override
    public void setCsNationalite(String string) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setCsSexe(java.lang .String)
     */
    @Override
    public void setCsSexe(String string) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setDateDeces(java. lang.String)
     */
    @Override
    public void setDateDeces(String string) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setDateNaissance(java .lang.String)
     */
    @Override
    public void setDateNaissance(String string) {

    }

    public void setDisplaySendToGed(String displaySendToGed) {
        this.displaySendToGed = displaySendToGed;
    }

    /**
     * setter pour l'attribut email.
     * 
     * @param email
     *            une nouvelle valeur pour cet attribut
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setIdAssure(java.lang .String)
     */
    @Override
    public void setIdAssure(String string) {

    }

    /**
     * @param boolean1
     */
    public void setIsGenerationUnique(Boolean boolean1) {
        isGenerationUnique = boolean1;
    }

    public void setIsSendToGed(Boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setNom(java.lang.String )
     */
    @Override
    public void setNom(String string) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setNss(java.lang.String )
     */
    @Override
    public void setNss(String string) {

    }

    /**
     * @param string
     */
    public void setNSS(String string) {
        NSS = string;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setPrenom(java.lang .String)
     */
    @Override
    public void setPrenom(String string) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setProvenance(java .lang.String)
     */
    @Override
    public void setProvenance(String string) {

    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    public boolean validate() {
        return false;
    }


    public String getTypePrestation() {
        return typePrestation;
    }

    public void setTypePrestation(String typePrestation) {
        this.typePrestation = typePrestation;
    }
}
