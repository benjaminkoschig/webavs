package globaz.libra.vb.formules;

import globaz.envoi.db.parametreEnvoi.access.ENDefinitionFormule;
import globaz.envoi.db.parametreEnvoi.access.ENRappel;
import globaz.framework.bean.FWViewBeanInterface;

public class LIRappelViewBean extends ENRappel implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Object[] METHODES_SEL_FORMULE = new Object[] { new String[] { "csDocumentCS", "csDocument" },
            new String[] { "idDefFormule", "idDefinitionFormule" } };

    private String csDocumentCS = new String();
    private String idDefFormule = new String();

    boolean isRetourSelCS = false;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getCsDocumentCS() {
        return csDocumentCS;
    }

    public String getIdDefFormule() {
        return idDefFormule;
    }

    public String getLibelleDocument() throws Exception {

        LIFormulesViewBean formule = new LIFormulesViewBean();
        formule.setIdFormule(getIdFormule());
        formule.setSession(getSession());
        formule.retrieve();

        ENDefinitionFormule defFor = new ENDefinitionFormule();
        defFor.setSession(getSession());
        defFor.setIdDefinitionFormule(formule.getIdDefinitionFormule());
        defFor.retrieve();

        return getSession().getCodeLibelle(defFor.getCsDocument());

    }

    /**
     * getter pour l'attribut methodes selection formule
     * 
     * @return la valeur courante de l'attribut methodes selection formule
     */
    public Object[] getMethodesSelectionFormule() {
        return METHODES_SEL_FORMULE;
    }

    public boolean isRetourSelCS() {
        return isRetourSelCS;
    }

    public void setCsDocumentCS(String csDocumentCS) {
        getDefinitionFormule().setCsDocument(csDocumentCS);
        setRetourSelCS(true);
        this.csDocumentCS = csDocumentCS;
    }

    public void setIdDefFormule(String idDefFormule) {
        setIdDefinitionFormule(idDefFormule);
        setRetourSelCS(true);
        this.idDefFormule = idDefFormule;
    }

    public void setRetourSelCS(boolean isRetourSelCS) {
        this.isRetourSelCS = isRetourSelCS;
    }

}
