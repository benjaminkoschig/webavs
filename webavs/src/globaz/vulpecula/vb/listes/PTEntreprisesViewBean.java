package globaz.vulpecula.vb.listes;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.jade.context.JadeThread;
import java.util.List;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.util.I18NUtil;

public class PTEntreprisesViewBean extends BJadeSearchObjectELViewBean {
    private List<Convention> conventions;

    private String email;
    private String idConvention;

    private boolean launched = false;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        conventions = VulpeculaRepositoryLocator.getConventionRepository().findAll();
    }

    @Override
    public void setId(String newId) {
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    /**
     * Méthode utilisée par le framework pour setter l'email.
     * 
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Méthode utilisée par le framework pour setter l'id de la convention.
     * 
     * @param idTravailleur String représentant un id de travailleur
     */
    public void setIdConvention(String idConvention) {
        this.idConvention = idConvention;
    }

    /**
     * Méthode utilisée par le framework pour setter l'id de la convention.
     * 
     * @return email auquel le mail sera envoyé
     */

    public String getEmail() {
        if (email == null) {
            return JadeThread.currentUserEmail();
        }
        return email;
    }

    public String getIdConvention() {
        return idConvention;
    }

    public List<Convention> getConventions() {
        return conventions;
    }

    public boolean isLaunched() {
        return launched;
    }

    public void setLaunched(boolean launched) {
        this.launched = launched;
    }

    public String getMessageConventionRequise() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(),
                SpecificationMessage.ENTREPRISES_LISTES_CONVENTION_REQUISE);
    }

    public String getMessageProcessusLance() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("LISTE_PROCESSUS_LANCE");
    }

}
