package ch.globaz.orion.business.models.af;

import java.math.BigDecimal;
import java.util.Date;

public class LigneRecapAf {

    private Integer idLigneRecap;
    private Integer idRecap;
    private String nss;
    private Integer numeroDossierAf;
    private String nomAllocataire;
    private String prenomAllocataire;
    private Integer nbUniteTravail;
    private Integer nbUniteTravailCaisse;
    private UniteTempsEnum uniteTravail;
    private UniteTempsEnum uniteTravailCaisse;
    private MotifChangementAfEnum motifChangement;
    private MotifChangementAfEnum motifChangementCaisse;
    private Date dateDebutChangement;
    private Date dateDebutChangementCaisse;
    private String dateDebutChangementStr;
    private String dateDebutChangementStrCaisse;
    private Date dateFinChangement;
    private Date dateFinChangementCaisse;
    private String dateFinChangementStr;
    private String dateFinChangementStrCaisse;
    private Integer nbEnfant;
    private BigDecimal montantAllocation;
    private String remarque;
    private String remarqueCaisse;
    private StatutLigneRecapEnum statutLigne;
    private Date creationDate;
    private Date lastModificationDate;

    public Integer getIdLigneRecap() {
        return idLigneRecap;
    }

    public void setIdLigneRecap(Integer idLigneRecap) {
        this.idLigneRecap = idLigneRecap;
    }

    public Integer getIdRecap() {
        return idRecap;
    }

    public void setIdRecap(Integer idRecap) {
        this.idRecap = idRecap;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public Integer getNumeroDossierAf() {
        return numeroDossierAf;
    }

    public void setNumeroDossierAf(Integer numeroDossierAf) {
        this.numeroDossierAf = numeroDossierAf;
    }

    public String getNomAllocataire() {
        return nomAllocataire;
    }

    public void setNomAllocataire(String nomAllocataire) {
        this.nomAllocataire = nomAllocataire;
    }

    public String getPrenomAllocataire() {
        return prenomAllocataire;
    }

    public void setPrenomAllocataire(String prenomAllocataire) {
        this.prenomAllocataire = prenomAllocataire;
    }

    public Integer getNbUniteTravail() {
        return nbUniteTravail;
    }

    public void setNbUniteTravail(Integer nbUniteTravail) {
        this.nbUniteTravail = nbUniteTravail;
    }

    public UniteTempsEnum getUniteTravail() {
        return uniteTravail;
    }

    public void setUniteTravail(UniteTempsEnum uniteTravail) {
        this.uniteTravail = uniteTravail;
    }

    public MotifChangementAfEnum getMotifChangement() {
        return motifChangement;
    }

    public void setMotifChangement(MotifChangementAfEnum motifChangement) {
        this.motifChangement = motifChangement;
    }

    public Date getDateDebutChangement() {
        return dateDebutChangement;
    }

    public void setDateDebutChangement(Date dateDebutChangement) {
        this.dateDebutChangement = dateDebutChangement;
    }

    public Date getDateFinChangement() {
        return dateFinChangement;
    }

    public void setDateFinChangement(Date dateFinChangement) {
        this.dateFinChangement = dateFinChangement;
    }

    public Integer getNbEnfant() {
        return nbEnfant;
    }

    public void setNbEnfant(Integer nbEnfant) {
        this.nbEnfant = nbEnfant;
    }

    public BigDecimal getMontantAllocation() {
        return montantAllocation;
    }

    public void setMontantAllocation(BigDecimal montantAllocation) {
        this.montantAllocation = montantAllocation;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public StatutLigneRecapEnum getStatutLigne() {
        return statutLigne;
    }

    public void setStatutLigne(StatutLigneRecapEnum statutLigne) {
        this.statutLigne = statutLigne;
    }

    public String getDateDebutChangementStr() {
        return dateDebutChangementStr;
    }

    public void setDateDebutChangementStr(String dateDebutChangementStr) {
        this.dateDebutChangementStr = dateDebutChangementStr;
    }

    public String getDateFinChangementStr() {
        return dateFinChangementStr;
    }

    public void setDateFinChangementStr(String dateFinChangementStr) {
        this.dateFinChangementStr = dateFinChangementStr;
    }

    public boolean isMotifCongeMaternite() {
        boolean result = false;
        if (motifChangementCaisse != null && motifChangementCaisse.equals(MotifChangementAfEnum.CONGE_MATERNITE)) {
            result = true;
        }

        return result;
    }

    public boolean isRemarque() {
        if (remarqueCaisse != null) {
            return true;
        } else {
            return false;
        }
    }

    public Integer getNbUniteTravailCaisse() {
        return nbUniteTravailCaisse;
    }

    public void setNbUniteTravailCaisse(Integer nbUniteTravailCaisse) {
        this.nbUniteTravailCaisse = nbUniteTravailCaisse;
    }

    public UniteTempsEnum getUniteTravailCaisse() {
        return uniteTravailCaisse;
    }

    public void setUniteTravailCaisse(UniteTempsEnum uniteTravailCaisse) {
        this.uniteTravailCaisse = uniteTravailCaisse;
    }

    public MotifChangementAfEnum getMotifChangementCaisse() {
        return motifChangementCaisse;
    }

    public void setMotifChangementCaisse(MotifChangementAfEnum motifChangementCaisse) {
        this.motifChangementCaisse = motifChangementCaisse;
    }

    public Date getDateDebutChangementCaisse() {
        return dateDebutChangementCaisse;
    }

    public void setDateDebutChangementCaisse(Date dateDebutChangementCaisse) {
        this.dateDebutChangementCaisse = dateDebutChangementCaisse;
    }

    public String getDateDebutChangementStrCaisse() {
        return dateDebutChangementStrCaisse;
    }

    public void setDateDebutChangementStrCaisse(String dateDebutChangementStrCaisse) {
        this.dateDebutChangementStrCaisse = dateDebutChangementStrCaisse;
    }

    public Date getDateFinChangementCaisse() {
        return dateFinChangementCaisse;
    }

    public void setDateFinChangementCaisse(Date dateFinChangementCaisse) {
        this.dateFinChangementCaisse = dateFinChangementCaisse;
    }

    public String getDateFinChangementStrCaisse() {
        return dateFinChangementStrCaisse;
    }

    public void setDateFinChangementStrCaisse(String dateFinChangementStrCaisse) {
        this.dateFinChangementStrCaisse = dateFinChangementStrCaisse;
    }

    public String getRemarqueCaisse() {
        return remarqueCaisse;
    }

    public void setRemarqueCaisse(String remarqueCaisse) {
        this.remarqueCaisse = remarqueCaisse;
    }

}
