/*
 * Créé le 20 janv. 06
 */
package globaz.osiris.db.ventilation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.osiris.db.comptes.CASectionViewBean;
import globaz.osiris.db.comptes.CATypeSection;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jmc
 */
public class CAVPImprimerVentilationViewBean extends CASectionViewBean implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String eMailAddress = "";
    private String forCompteAnnexe = "";
    private List listIdSections = new ArrayList();
    private String typeDeProcedure = "";
    private Boolean ventilationGlobale = new Boolean(false);

    /**
     * Constructeur par défaut.
     */
    public CAVPImprimerVentilationViewBean() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2002 12:00:16)
     * 
     * @return java.lang.String
     */
    public String getCSTypeSection() {
        try {
            CATypeSection typeSection = new CATypeSection();
            typeSection.setSession(getSession());
            typeSection.setIdTypeSection(getIdTypeSection());
            typeSection.retrieve();

            if (typeSection.isNew()) {
                return null;
            } else {
                return typeSection.getDescription();
            }
        } catch (Exception e1) {
            return e1.toString();
        }
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    /**
     * @return
     */
    public String getForCompteAnnexe() {
        return forCompteAnnexe;
    }

    /**
     * @return
     */
    public List getListIdSections() {
        return listIdSections;
    }

    /**
     * @return
     */
    public String getTypeDeProcedure() {
        return typeDeProcedure;
    }

    /**
     * @return the ventilationGlobale
     */
    public Boolean getVentilationGlobale() {
        return ventilationGlobale;
    }

    public void setEMailAddress(String mailAddress) {
        eMailAddress = mailAddress;
    }

    /**
     * @param string
     */
    public void setForCompteAnnexe(String string) {
        forCompteAnnexe = string;
    }

    /**
     * @param listIdSections
     */
    public void setListIdSections(List listIdSections) {
        this.listIdSections = listIdSections;
    }

    /**
     * @param string
     */
    public void setTypeDeProcedure(String string) {
        typeDeProcedure = string;
    }

    /**
     * @param ventilationGlobale
     *            the ventilationGlobale to set
     */
    public void setVentilationGlobale(Boolean ventilationGlobale) {
        this.ventilationGlobale = ventilationGlobale;
    }
}
