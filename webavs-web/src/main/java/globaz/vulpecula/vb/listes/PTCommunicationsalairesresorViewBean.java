package globaz.vulpecula.vb.listes;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.jade.context.JadeThread;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.util.I18NUtil;

public class PTCommunicationsalairesresorViewBean extends BJadeSearchObjectELViewBean {
    private List<Convention> conventions;

    private String email;
    private String idConvention;
    private String annee;
    private String codeTypeDecompte;
    private boolean miseAJour;
    private String typeListe;

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

    public String getAnnee() {
        if (annee == null) {
            annee = new Annee(Date.getCurrentYear()).toString();
        }
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public List<Decompte> getTypeDecompte() {
        Decompte decompte = new Decompte();
        decompte.setType(TypeDecompte.CONTROLE_EMPLOYEUR);
        decompte.setTypeDecompteLibelle(BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                TypeDecompte.CONTROLE_EMPLOYEUR.getValue()));
        List<Decompte> liste = new ArrayList<Decompte>();
        liste.add(decompte);
        decompte = new Decompte();
        decompte.setTypeDecompteLibelle(BSessionUtil.getSessionFromThreadContext().getLabel("JSP_AUTRE_DECOMPTE"));
        liste.add(decompte);
        return liste;
    }

    public String getCodeTypeDecompte() {
        return codeTypeDecompte;
    }

    public void setCodeTypeDecompte(String codeTypeDecompte) {
        this.codeTypeDecompte = codeTypeDecompte;
    }

    public boolean isMiseAJour() {
        return miseAJour;
    }

    public void setMiseAJour(boolean miseAJour) {
        this.miseAJour = miseAJour;
    }

    public String getTypeListe() {
        return typeListe;
    }

    public void setTypeListe(String typeListe) {
        this.typeListe = typeListe;
    }
}
