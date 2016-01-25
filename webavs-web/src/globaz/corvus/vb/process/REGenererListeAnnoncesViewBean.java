/*
 * Créé le 05 sept. 07
 */
package globaz.corvus.vb.process;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * <H1>Description</H1>
 * 
 * @author BSC
 */
public class REGenererListeAnnoncesViewBean extends PRAbstractViewBeanSupport {

    public static final String EXCEL_OUTPUT = "EXCEL";
    public static final String PDF_OUTPUT = "PDF";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String eMailAddress = "";
    private String mois = "";
    private String outputType = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut EMail address
     * 
     * @return la valeur courante de l'attribut EMail address
     */
    public String getEMailAddress() {
        return eMailAddress;
    }

    /**
     * @return
     */
    public String getMois() {
        return mois;
    }

    public String getOutputType() {
        return outputType;
    }

    /**
     * setter pour l'attribut EMail address
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setEMailAddress(String string) {
        eMailAddress = string;
    }

    /**
     * @param string
     */
    public void setMois(String string) {
        mois = string;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.vb.PRAbstractViewBeanSupport#validate()
     */
    @Override
    public boolean validate() {
        return true;
    }

}
