package globaz.vulpecula.vb.listes;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.business.services.employeur.EmployeurServiceCRUD;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.util.I18NUtil;

public class PTSalairesAVSViewBean extends BJadeSearchObjectELViewBean {

    private String email;
    private String annee;
    private String idEmployeur;

    /**
     * Collection contenant les type de decomptes à prendre en compte
     */
    private HashSet<String> inDecompteFilter = new HashSet<String>();

    private boolean launched = false;

    public String getEmployeurService() {
        return EmployeurServiceCRUD.class.getName();
    }

    public void setFilterDecompte(String decomptes) {
        String[] decomptesTabs = decomptes.split(",");
        for (int i = 0; i < decomptesTabs.length; i++) {
            if (!JadeStringUtil.isEmpty(decomptesTabs[i])) {
                inDecompteFilter.add(decomptesTabs[i]);
            }

        }
    }

    public Collection<String> getInTypeDecompte() {
        return inDecompteFilter;
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
     * @return email auquel le mail sera envoyé
     */

    public String getEmail() {
        if (email == null) {
            return JadeThread.currentUserEmail();
        }
        return email;
    }

    public boolean isLaunched() {
        return launched;
    }

    public void setLaunched(boolean launched) {
        this.launched = launched;
    }

    public List<String> getTypes() {
        return TypeDecompte.getList().subList(0, TypeDecompte.getList().size() - 1);
    }

    public String getMessageTypeDecompteRequis() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(),
                SpecificationMessage.SALAIRES_AVS_LISTES_TYPE_DECOMPTE_REQUISE);
    }

    public String getMessageEmployeurRequis() {
        return SpecificationMessage.getMessage(I18NUtil.getUserLocale(), SpecificationMessage.EMPLOYEUR_MANQUANT);
    }

    public String getMessageProcessusLance() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("LISTE_PROCESSUS_LANCE");
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public String getCurrentYear() {
        return Date.now().getAnnee();
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
    }

    @Override
    public void setId(String paramString) {

    }

    @Override
    public BSpy getSpy() {
        return null;
    }

}
