package globaz.libra.vb.formules;

import globaz.envoi.db.parametreEnvoi.access.ENChamp;
import globaz.envoi.db.parametreEnvoi.access.ENDefinitionFormule;
import globaz.framework.bean.FWViewBeanInterface;

public class LIChampsViewBean extends ENChamp implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String idFormule = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getCsDocument() throws Exception {

        LIFormulesViewBean formule = new LIFormulesViewBean();
        formule.setIdFormule(getIdFormule());
        formule.setSession(getSession());
        formule.retrieve();

        ENDefinitionFormule defFor = new ENDefinitionFormule();
        defFor.setSession(getSession());
        defFor.setIdDefinitionFormule(formule.getIdDefinitionFormule());
        defFor.retrieve();

        return defFor.getCsDocument();

    }

    public String getIdFormule() {
        return idFormule;
    }

    public String getLibelleFormule() throws Exception {

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

    public void setIdFormule(String idFormule) {
        this.idFormule = idFormule;
    }

}
