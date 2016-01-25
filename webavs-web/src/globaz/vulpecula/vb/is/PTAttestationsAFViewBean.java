package globaz.vulpecula.vb.is;

import globaz.vulpecula.vb.listes.PTListeProcessViewBean;
import ch.globaz.al.business.services.models.allocataire.AllocataireComplexModelService;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.is.TypeListAttestationAF;

public class PTAttestationsAFViewBean extends PTListeProcessViewBean {
    private String idAllocataire;
    private String dateDebut;
    private String dateFin;
    private String listeAttestationAF;
    private String descriptionAllocataire;
    private String annee;

    public String getIdAllocataire() {
        return idAllocataire;
    }

    public void setIdAllocataire(String idAllocataire) {
        this.idAllocataire = idAllocataire;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getListeAttestationAF() {
        return String.valueOf(listeAttestationAF);
    }

    public String getDescriptionAllocataire() {
        return descriptionAllocataire;
    }

    public void setDescriptionAllocataire(String descriptionAllocataire) {
        this.descriptionAllocataire = descriptionAllocataire;
    }

    public String getAllocataireComplexModelService() {
        return AllocataireComplexModelService.class.getName();
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setListeAttestationAF(String listeAttestationAF) {
        this.listeAttestationAF = listeAttestationAF;
    }

    public String getCSListeAllocataire() {
        return TypeListAttestationAF.ALLOCATAIRE.getValue();
    }

    public String getCSListeFisc() {
        return TypeListAttestationAF.FISC.getValue();
    }

    public String getCurrentYear() {
        return String.valueOf(Annee.getCurrentYear().getValue());
    }
}
