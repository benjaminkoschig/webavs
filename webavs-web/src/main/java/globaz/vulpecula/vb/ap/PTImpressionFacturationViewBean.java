package globaz.vulpecula.vb.ap;

import java.util.List;
import ch.globaz.musca.business.models.PassageModel;
import ch.globaz.musca.businessimpl.services.FABusinessImplServiceLocator;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.employeur.EmployeurServiceCRUD;
import ch.globaz.vulpecula.domain.models.association.FactureAssociation;
import ch.globaz.vulpecula.domain.models.association.FacturesAssociations;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.jade.context.JadeThread;
import globaz.vulpecula.vb.listes.PTListeProcessViewBean;

public class PTImpressionFacturationViewBean extends PTListeProcessViewBean {

    private String email;
    private String idPassage;
    private String libellePassage = "";

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
    }

    @Override
    public void setId(String newId) {
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    @Override
    public String getEmail() {
        if (email == null) {
            return JadeThread.currentUserEmail();
        }
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmployeurViewService() {
        return EmployeurServiceCRUD.class.getName();
    }

    public String getIdPassage() {
        return idPassage;
    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    public String getLibellePassage() {
        return libellePassage;
    }

    public void setLibellePassage(String libellePassage) {
        this.libellePassage = libellePassage;
    }
    

    public String getMessageErreurDate() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_IMPRESSION_FACTURATION_ERREUR_DATE");
    }

    public String getMessageErreurPassage() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_IMPRESSION_FACTURATION_ERREUR_PASSAGE");
    }

    public String getMessageErreurEmployeur() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_IMPRESSION_FACTURATION_ERREUR_EMPLOYEUR");
    }

    public String getMessageErreurEmail() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_IMPRESSION_FACTURATION_ERREUR_EMAIL");
    }
}