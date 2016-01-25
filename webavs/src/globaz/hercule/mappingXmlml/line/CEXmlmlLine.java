package globaz.hercule.mappingXmlml.line;

import globaz.hercule.mappingXmlml.ICEListeColumns;
import globaz.hercule.utils.HerculeContainer;

/**
 * 
 * 
 * @author Sullivann Corneille
 * @since 3 avr. 2014
 */
public class CEXmlmlLine {

    public String rue = "";
    public String casePostale = "";
    public String npa = "";
    public String localite = "";

    public void remplirContainerWithLineData(HerculeContainer container) {
        container.put(ICEListeColumns.RUE, rue);
        container.put(ICEListeColumns.CASE_POSTALE, casePostale);
        container.put(ICEListeColumns.NPA, npa);
        container.put(ICEListeColumns.LOCALITE, localite);
    }

    /**
     * Getter de rue
     * 
     * @return the rue
     */
    public String getRue() {
        return rue;
    }

    /**
     * Setter de rue
     * 
     * @param rue the rue to set
     */
    public void setRue(String rue) {
        this.rue = rue;
    }

    /**
     * Getter de casePostale
     * 
     * @return the casePostale
     */
    public String getCasePostale() {
        return casePostale;
    }

    /**
     * Setter de casePostale
     * 
     * @param casePostale the casePostale to set
     */
    public void setCasePostale(String casePostale) {
        this.casePostale = casePostale;
    }

    /**
     * Getter de npa
     * 
     * @return the npa
     */
    public String getNpa() {
        return npa;
    }

    /**
     * Setter de npa
     * 
     * @param npa the npa to set
     */
    public void setNpa(String npa) {
        this.npa = npa;
    }

    /**
     * Getter de localite
     * 
     * @return the localite
     */
    public String getLocalite() {
        return localite;
    }

    /**
     * Setter de localite
     * 
     * @param localite the localite to set
     */
    public void setLocalite(String localite) {
        this.localite = localite;
    }

}
