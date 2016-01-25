package globaz.libra.vb.journalisations;

import globaz.journalisation.db.journalisation.access.JOReferenceDestination;
import globaz.journalisation.db.journalisation.access.JOReferenceDestinationManager;
import globaz.libra.vb.formules.LIFormulesDetailViewBean;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.ArrayList;
import java.util.Collection;
import ch.globaz.libra.constantes.ILIConstantesExternes;

public class LIDocumentsExecutionViewBean extends PRAbstractViewBeanSupport {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private String adresseEmail = new String();
    private String dateExecution = new String();
    private LIJournalisationsDetailViewBean joDetail = new LIJournalisationsDetailViewBean();

    private Collection listeIdRappel = new ArrayList();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getAdresseEmail() {
        return adresseEmail;
    }

    public String getDateExecution() {
        return dateExecution;
    }

    public LIJournalisationsDetailViewBean getJoDetail() {
        return joDetail;
    }

    public Collection getListeIdRappel() {
        return listeIdRappel;
    }

    public String getNomFormule(String idJO) throws Exception {

        JOReferenceDestinationManager refDestMgr = new JOReferenceDestinationManager();
        refDestMgr.setSession(getSession());
        refDestMgr.setForIdJournalisation(idJO);
        refDestMgr.setForTypeReferenceDestination(ILIConstantesExternes.CS_TYPE_FORM_PRESTATIONS);
        refDestMgr.find();

        if (!refDestMgr.isEmpty()) {
            JOReferenceDestination refDest = (JOReferenceDestination) refDestMgr.getFirstEntity();

            LIFormulesDetailViewBean formule = new LIFormulesDetailViewBean();
            formule.setISession(getISession());
            formule.setId(refDest.getIdCleReferenceDestination());
            formule.retrieve();

            return getSession().getCodeLibelle(formule.getDefinitionFormule().getCsDocument());

        } else {
            return "";
        }

    }

    public void loadJournalisation(String idJO) throws Exception {

        LIJournalisationsDetailViewBean joViewBean = new LIJournalisationsDetailViewBean();
        joViewBean.setId(idJO);
        joViewBean.setISession(getISession());
        joViewBean.retrieve();

        joDetail = joViewBean;

    }

    public void setAdresseEmail(String adresseEmail) {
        this.adresseEmail = adresseEmail;
    }

    public void setDateExecution(String dateExecution) {
        this.dateExecution = dateExecution;
    }

    public void setJoDetail(LIJournalisationsDetailViewBean joDetail) {
        this.joDetail = joDetail;
    }

    public void setListeIdRappel(Collection listeIdRappel) {
        this.listeIdRappel = listeIdRappel;
    }

    @Override
    public boolean validate() {
        return false;
    }

}
