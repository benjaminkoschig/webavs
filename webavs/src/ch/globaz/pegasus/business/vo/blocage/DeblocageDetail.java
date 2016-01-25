package ch.globaz.pegasus.business.vo.blocage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.models.blocage.PcaBloque;

public class DeblocageDetail {
    private List<SoldeCompteCouranSection> comptesBlocage = new ArrayList<SoldeCompteCouranSection>();
    private List<Creancier> creanciers = new ArrayList<Creancier>();
    private List<DetteComptat> detteEnCompta = new ArrayList<DetteComptat>();
    private Boolean isDevalidable = false;
    private BigDecimal montantLiberer = new BigDecimal(0);
    private BigDecimal montantTotalADebloque = new BigDecimal(0);
    private PcaBloque pcaBloque = new PcaBloque();
    private BigDecimal solde = new BigDecimal(0);
    private BigDecimal soldeCompteBlocage = null;
    private List<VersementBeneficiaire> versementBeneficiaire = new ArrayList<VersementBeneficiaire>();

    public List<SoldeCompteCouranSection> getComptesBlocage() {
        return comptesBlocage;
    }

    public List<Creancier> getCreanciers() {
        return creanciers;
    }

    public List<DetteComptat> getDetteEnCompta() {
        return detteEnCompta;
    }

    public Boolean getIsDevalidable() {
        return isDevalidable;
    }

    public BigDecimal getMontantLiberer() {
        return montantLiberer;
    }

    public BigDecimal getMontantTotalADebloque() {
        return montantTotalADebloque;
    }

    public PcaBloque getPcaBloque() {
        return pcaBloque;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public BigDecimal getSoldeCompteBlocage() {
        return soldeCompteBlocage;
    }

    public List<VersementBeneficiaire> getVersementBeneficiaire() {
        return versementBeneficiaire;
    }

    public void setComptesBlocage(List<SoldeCompteCouranSection> comptesBlocage) {
        this.comptesBlocage = comptesBlocage;
    }

    public void setCreanciers(List<Creancier> creanciers) {
        this.creanciers = creanciers;
    }

    public void setDetteEnCompta(List<DetteComptat> detteEnCompta) {
        this.detteEnCompta = detteEnCompta;
    }

    public void setIsDevalidable(Boolean isDevalidable) {
        this.isDevalidable = isDevalidable;
    }

    public void setMontantLiberer(BigDecimal montantLiberer) {
        this.montantLiberer = montantLiberer;
    }

    public void setMontantTotalADebloque(BigDecimal montantTotalADebloque) {
        this.montantTotalADebloque = montantTotalADebloque;
    }

    public void setPcaBloque(PcaBloque pcaBloque) {
        this.pcaBloque = pcaBloque;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public void setSoldeCompteBlocage(BigDecimal soldeCompteBlocage) {
        this.soldeCompteBlocage = soldeCompteBlocage;
    }

    public void setVersementBeneficiaire(List<VersementBeneficiaire> versementBeneficiaire) {
        this.versementBeneficiaire = versementBeneficiaire;
    }

}
