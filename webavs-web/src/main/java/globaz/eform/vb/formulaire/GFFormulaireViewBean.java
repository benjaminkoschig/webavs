package globaz.eform.vb.formulaire;

import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.models.GFEFormModel;
import ch.globaz.eform.constant.GFTypeEForm;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import javassist.NotFoundException;

public class GFFormulaireViewBean extends BJadePersistentObjectViewBean {

    GFEFormModel formulaire;

    private String byGestionnaire = null;

    public String getByGestionnaire() {
        return byGestionnaire;
    }

    public void setByGestionnaire(String byGestionnaire) {
        this.byGestionnaire = byGestionnaire;
    }

    public GFFormulaireViewBean() {
        super();
        formulaire = new GFEFormModel();
    }

    public GFFormulaireViewBean(GFEFormModel formulaire) {
        super();
        this.formulaire = formulaire;
    }

    @Override
    public BSpy getSpy() {
        return (formulaire != null) && !formulaire.isNew() ? new BSpy(formulaire.getSpy()) : new BSpy(getSession());
    }


    @Override
    public void add() throws Exception {
        formulaire = GFEFormServiceLocator.getGFEFormService().create(formulaire);
    }

    @Override
    public void delete() throws Exception {
        GFEFormServiceLocator.getGFEFormService().delete(formulaire.getId());
    }

    @Override
    public String getId() {
        return formulaire.getId();
    }

    @Override
    public void retrieve() throws Exception {

    }

    @Override
    public void setId(String newId) {
        formulaire.setId(newId);
    }

    @Override
    public void update() throws Exception {
        GFEFormServiceLocator.getGFEFormService().update(formulaire);
    }

    public BSession getSession() {
        return (BSession) getISession();
    }

    public GFEFormModel getFormulaire() {
        return formulaire;
    }

    public String getSubject() {
//        try{
            return GFTypeEForm.getGFTypeEForm(formulaire.getSubject())
                    .getDesignation(getSession());
//        } catch (NotFoundException ex) {
//            return "";
//        }

    }
}
