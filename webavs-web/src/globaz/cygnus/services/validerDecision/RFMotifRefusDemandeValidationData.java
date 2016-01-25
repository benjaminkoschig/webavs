/*
 * Créé le 8 novembre 2010
 */
package globaz.cygnus.services.validerDecision;

/**
 * author fha
 */
/*
 * un motif de refus a : - un id - une description
 */
public class RFMotifRefusDemandeValidationData {

    String descriptionDe = "";
    String descriptionFr = "";
    String descriptionIt = "";
    String idMotifRefus = "";
    String idMotifRefusSysteme = "";
    String montantMotif = "";

    public RFMotifRefusDemandeValidationData(String idMotifRefus, String descriptionFr, String descriptionDe,
            String descriptionIt, String montantMotif, String idMotifRefusSysteme) {
        super();
        this.idMotifRefus = idMotifRefus;
        this.descriptionFr = descriptionFr;
        this.descriptionDe = descriptionDe;
        this.descriptionIt = descriptionIt;
        this.montantMotif = montantMotif;
        this.idMotifRefusSysteme = idMotifRefusSysteme;
    }

    public String getDescriptionDe() {
        return descriptionDe;
    }

    public String getDescriptionFr() {
        return descriptionFr;
    }

    public String getDescriptionIt() {
        return descriptionIt;
    }

    public String getIdMotifRefus() {
        return idMotifRefus;
    }

    public String getIdMotifRefusSysteme() {
        return idMotifRefusSysteme;
    }

    public String getMontantMotif() {
        return montantMotif;
    }

    public void setDescriptionDe(String descriptionDe) {
        this.descriptionDe = descriptionDe;
    }

    public void setDescriptionFr(String descriptionFr) {
        this.descriptionFr = descriptionFr;
    }

    public void setDescriptionIt(String descriptionIt) {
        this.descriptionIt = descriptionIt;
    }

    public void setIdMotifRefus(String idMotifRefus) {
        this.idMotifRefus = idMotifRefus;
    }

    public void setIdMotifRefusSysteme(String idMotifRefusSysteme) {
        this.idMotifRefusSysteme = idMotifRefusSysteme;
    }

    public void setMontantMotif(String montantMotif) {
        this.montantMotif = montantMotif;
    }

}
