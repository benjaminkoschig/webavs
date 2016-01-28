package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSimpleModel;
import java.math.BigDecimal;
import ch.globaz.pegasus.business.domaine.Totalisable;

public class SimpleAllocationNoel extends JadeSimpleModel implements Totalisable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeAllocation = null;
    private Boolean hasPaiementPostal = false;
    private String idAdressePaiementPostaleCreer = null;
    // private EPCTypePaiement typePaiement = null;
    private String idAllocationNoel = null;
    private String idDemande = null;
    private String idPCAccordee = null;
    private String idPrestationAccordee = null;
    private String idPrestationAccordeeConjoint = null;
    private String montantAllocation = null;
    private String nbrePersonnes = null;

    public String getAnneeAllocation() {
        return anneeAllocation;
    }

    public Boolean getHasPaiementPostal() {
        return hasPaiementPostal;
    }

    @Override
    public String getId() {
        return idAllocationNoel;
    }

    public String getIdAdressePaiementPostaleCreer() {
        return idAdressePaiementPostaleCreer;
    }

    public String getIdAllocationNoel() {
        return idAllocationNoel;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdPCAccordee() {
        return idPCAccordee;
    }

    public String getIdPrestationAccordee() {
        return idPrestationAccordee;
    }

    public String getIdPrestationAccordeeConjoint() {
        return idPrestationAccordeeConjoint;
    }

    public String getMontantAllocation() {
        return montantAllocation;
    }

    public String getNbrePersonnes() {
        return nbrePersonnes;
    }

    public void setAnneeAllocation(String anneeAllocation) {
        this.anneeAllocation = anneeAllocation;
    }

    public void setHasPaiementPostal(Boolean hasPaiementPostal) {
        this.hasPaiementPostal = hasPaiementPostal;
    }

    @Override
    public void setId(String id) {
        idAllocationNoel = id;

    }

    public void setIdAdressePaiementPostaleCreer(String idAdressePaiementPostaleCreer) {
        this.idAdressePaiementPostaleCreer = idAdressePaiementPostaleCreer;
    }

    public void setIdAllocationNoel(String idAllocationNoel) {
        this.idAllocationNoel = idAllocationNoel;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setIdPCAccordee(String idPCAccordee) {
        this.idPCAccordee = idPCAccordee;
    }

    public void setIdPrestationAccordee(String idPrestationAccordee) {
        this.idPrestationAccordee = idPrestationAccordee;
    }

    public void setIdPrestationAccordeeConjoint(String idPrestationAccordeeConjoint) {
        this.idPrestationAccordeeConjoint = idPrestationAccordeeConjoint;
    }

    public void setMontantAllocation(String montantAllocation) {
        this.montantAllocation = montantAllocation;
    }

    public void setNbrePersonnes(String nbrePersonnes) {
        this.nbrePersonnes = nbrePersonnes;
    }

    @Override
    public BigDecimal getMontant() {
        // TODO Auto-generated method stub
        return new BigDecimal(montantAllocation);
    }

}
