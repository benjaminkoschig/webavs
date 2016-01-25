/**
 * 
 */
package globaz.musca.db.facturation;

/**
 * @author MMO
 * @since 28 juin 2012
 */

public class FABeanCompensationDecomptePeriodiqueSection {

    private String idExterneFactureCompensation = "";
    private String idSection = "";
    private String idTypeFactureCompensation = "";
    private String montant = "";

    public String getIdExterneFactureCompensation() {
        return idExterneFactureCompensation;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getIdTypeFactureCompensation() {
        return idTypeFactureCompensation;
    }

    public String getMontant() {
        return montant;
    }

    public void setIdExterneFactureCompensation(String idExterneFactureCompensation) {
        this.idExterneFactureCompensation = idExterneFactureCompensation;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setIdTypeFactureCompensation(String idTypeFactureCompensation) {
        this.idTypeFactureCompensation = idTypeFactureCompensation;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

}
