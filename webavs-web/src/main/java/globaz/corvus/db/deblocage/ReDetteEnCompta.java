package globaz.corvus.db.deblocage;

import ch.globaz.common.domaine.Montant;

public class ReDetteEnCompta {
    private String description = null;
    private String descriptionCompteAnnexe = null;
    private String idRoleCA = null;
    private String idSectionDetteEnCompta = null;
    private Montant montant = Montant.ZERO;
    private Boolean settled = false;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionCompteAnnexe() {
        return descriptionCompteAnnexe;
    }

    public void setDescriptionCompteAnnexe(String descriptionCompteAnnexe) {
        this.descriptionCompteAnnexe = descriptionCompteAnnexe;
    }

    public String getIdRoleCA() {
        return idRoleCA;
    }

    public void setIdRoleCA(String idRoleCA) {
        this.idRoleCA = idRoleCA;
    }

    public String getIdSectionDetteEnCompta() {
        return idSectionDetteEnCompta;
    }

    public void setIdSectionDetteEnCompta(String idSectionDetteEnCompta) {
        this.idSectionDetteEnCompta = idSectionDetteEnCompta;
    }

    public Boolean getSettled() {
        return settled;
    }

    public void setSettled(Boolean settled) {
        this.settled = settled;
    }

    public Montant getMontant() {
        return montant;
    }

    public void setMontant(Montant montant) {
        this.montant = montant;
    }

}
