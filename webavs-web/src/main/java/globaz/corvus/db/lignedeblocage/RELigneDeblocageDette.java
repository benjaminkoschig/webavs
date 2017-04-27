package globaz.corvus.db.lignedeblocage;

import ch.globaz.common.domaine.Montant;

public class RELigneDeblocageDette extends RELigneDeblocage {
    private String description;
    private String descriptionCompteAnnexe;
    private String idCompteAnnexe;
    private Montant montanDette;
    private String idExterne;

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

    public Montant getMontanDette() {
        return montanDette;
    }

    public void setMontanDette(Montant montanDette) {
        this.montanDette = montanDette;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public String generateKey() {
        return getIdSectionCompensee() + "_" + getIdRoleSection();
    }

    public String getIdExterne() {
        return idExterne;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

}
