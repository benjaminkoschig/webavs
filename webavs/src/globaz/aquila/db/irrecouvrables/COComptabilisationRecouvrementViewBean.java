package globaz.aquila.db.irrecouvrables;

import globaz.aquila.vb.COAbstractViewBeanSupport;
import globaz.osiris.db.irrecouvrable.CARecouvrementCiContainer;
import globaz.osiris.db.irrecouvrable.CARecouvrementPosteContainer;
import java.util.List;

public class COComptabilisationRecouvrementViewBean extends COAbstractViewBeanSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idCompteAnnexe;
    private String idCompteIndividuelAffilie;
    private CARecouvrementPosteContainer posteContainer;
    private CARecouvrementCiContainer ciContainer;
    private List<String> idSectionsList;
    private String email;
    private boolean effectuerRectificationCI;
    private Integer annee;
    private String montantARecouvrir;

    public COComptabilisationRecouvrementViewBean() {
        posteContainer = new CARecouvrementPosteContainer();
        ciContainer = new CARecouvrementCiContainer();
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public CARecouvrementPosteContainer getPosteContainer() {
        return posteContainer;
    }

    public void setPosteContainer(CARecouvrementPosteContainer posteContainer) {
        this.posteContainer = posteContainer;
    }

    public CARecouvrementCiContainer getCiContainer() {
        return ciContainer;
    }

    public void setCiContainer(CARecouvrementCiContainer ciContainer) {
        this.ciContainer = ciContainer;
    }

    public List<String> getIdSectionsList() {
        return idSectionsList;
    }

    public void setIdSectionsList(List<String> idSectionsList) {
        this.idSectionsList = idSectionsList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getEffectuerRectificationCI() {
        return effectuerRectificationCI;
    }

    public void setEffectuerRectificationCI(boolean effectuerRectificationCI) {
        this.effectuerRectificationCI = effectuerRectificationCI;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public String getIdCompteIndividuelAffilie() {
        return idCompteIndividuelAffilie;
    }

    public void setIdCompteIndividuelAffilie(String idCompteIndividuelAffilie) {
        this.idCompteIndividuelAffilie = idCompteIndividuelAffilie;
    }

    public String getMontantARecouvrir() {
        return montantARecouvrir;
    }

    public void setMontantARecouvrir(String montantARecouvrir) {
        this.montantARecouvrir = montantARecouvrir;
    }

}
