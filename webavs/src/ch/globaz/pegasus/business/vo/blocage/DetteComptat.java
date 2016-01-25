package ch.globaz.pegasus.business.vo.blocage;

import java.math.BigDecimal;

public class DetteComptat extends LigneDeblocage {
    private String description = null;
    private String descriptionCompteAnnexe = null;
    private Boolean hasConjointDetteCompense = false;
    private String idRoleCA = null;
    private String idSectionDetteEnCompta = null;
    private BigDecimal montantCompense = new BigDecimal(0);
    private BigDecimal montantCompenseConjoint = new BigDecimal(0);
    private Boolean settled = false;

    public String getDescription() {
        return description;
    }

    public String getDescriptionCompteAnnexe() {
        return descriptionCompteAnnexe;
    }

    public Boolean getHasConjointDetteCompense() {
        return hasConjointDetteCompense;
    }

    public String getIdRoleCA() {
        return idRoleCA;
    }

    public String getIdSectionDetteEnCompta() {
        return idSectionDetteEnCompta;
    }

    public BigDecimal getMontantCompense() {
        return montantCompense;
    }

    public BigDecimal getMontantCompenseConjoint() {
        return montantCompenseConjoint;
    }

    public Boolean isSettled() {
        return settled;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDescriptionCompteAnnexe(String descriptionCompteAnnexe) {
        this.descriptionCompteAnnexe = descriptionCompteAnnexe;
    }

    public void setHasConjointDetteCompense(Boolean hasConjointDetteCompense) {
        this.hasConjointDetteCompense = hasConjointDetteCompense;
    }

    public void setIdRoleCA(String idRoleCA) {
        this.idRoleCA = idRoleCA;
    }

    public void setIdSectionDetteEnCompta(String idSectionDetteEnCompta) {
        this.idSectionDetteEnCompta = idSectionDetteEnCompta;
    }

    public void setMontantCompense(BigDecimal montantCompense) {
        this.montantCompense = montantCompense;
    }

    public void setMontantCompenseConjoint(BigDecimal montantCompenseConjoint) {
        this.montantCompenseConjoint = montantCompenseConjoint;
    }

    public void setSettled(Boolean settled) {
        this.settled = settled;
    }

}
