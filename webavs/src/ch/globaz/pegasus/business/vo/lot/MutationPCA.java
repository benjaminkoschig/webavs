package ch.globaz.pegasus.business.vo.lot;

public class MutationPCA {
    private boolean aiToAvs = false;
    private String ancienMontant;
    private boolean avsToAi = false;
    private String csMotif;
    private String csTypePreparationDecision;
    private String dateDebutPcaActuel;
    private String dateDebutPcaPrecedant;
    private String dateFinPcaActuel;
    private String dateFinPcaPrecedant;
    private boolean hasDiminutation = false;
    private String idPca;
    private String idVersionDroit;
    private boolean isAugementationFutur = false;
    private boolean isToSeparerMal = false;
    private String montantActuel;
    private String montantRetro;
    private String nom;
    private String noVersion;
    private String nss;
    private String prenom;
    private boolean purRetro = false;

    private String typeDecision = null;
    private String typePcActuel;
    private String typePcPrecedant;

    public int compareTo(MutationPCA o) {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getAncienMontant() {
        return ancienMontant;
    }

    public String getCsMotif() {
        return csMotif;
    }

    public String getCsTypePreparationDecision() {
        return csTypePreparationDecision;
    }

    public String getDateDebutPcaActuel() {
        return dateDebutPcaActuel;
    }

    public String getDateDebutPcaPrecedant() {
        return dateDebutPcaPrecedant;
    }

    public String getDateFinPcaActuel() {
        return dateFinPcaActuel;
    }

    public String getDateFinPcaPrecedant() {
        return dateFinPcaPrecedant;
    }

    public String getIdPca() {
        return idPca;
    }

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public String getMontantActuel() {
        return montantActuel;
    }

    public String getMontantRetro() {
        return montantRetro;
    }

    public String getNom() {
        return nom;
    }

    public String getNoVersion() {
        return noVersion;
    }

    public String getNss() {
        return nss;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getTypeDecision() {
        return typeDecision;
    }

    public String getTypePcActuel() {
        return typePcActuel;
    }

    public String getTypePcPrecedant() {
        return typePcPrecedant;
    }

    public boolean hasDiminutation() {
        return hasDiminutation;
    }

    public boolean isAiToAvs() {
        return aiToAvs;
    }

    public boolean isAugementationFutur() {
        return isAugementationFutur;
    }

    public boolean isAvsToAi() {
        return avsToAi;
    }

    public boolean isPurRetro() {
        return purRetro;
    }

    public boolean isToSeparerMal() {
        return isToSeparerMal;
    }

    public void setAiToAvs(boolean aiToAvs) {
        this.aiToAvs = aiToAvs;
    }

    public void setAncienMontant(String ancienMontant) {
        this.ancienMontant = ancienMontant;
    }

    public void setAugementationFutur(boolean isAugementationFutur) {
        this.isAugementationFutur = isAugementationFutur;
    }

    public void setAvsToAi(boolean avsToAi) {
        this.avsToAi = avsToAi;
    }

    public void setCsMotif(String csMotif) {
        this.csMotif = csMotif;
    }

    public void setCsTypePreparationDecision(String csTypePreparationDecision) {
        this.csTypePreparationDecision = csTypePreparationDecision;
    }

    public void setDateDebutPcaActuel(String dateDebutPcaActuel) {
        this.dateDebutPcaActuel = dateDebutPcaActuel;
    }

    public void setDateDebutPcaPrecedant(String dateDebutPcaPrecedant) {
        this.dateDebutPcaPrecedant = dateDebutPcaPrecedant;
    }

    public void setDateFinPcaActuel(String dateFinPcaActuel) {
        this.dateFinPcaActuel = dateFinPcaActuel;
    }

    public void setDateFinPcaPrecedant(String dateFinPcaPrecedant) {
        this.dateFinPcaPrecedant = dateFinPcaPrecedant;
    }

    public void setHasDiminutation(boolean hasDiminutation) {
        this.hasDiminutation = hasDiminutation;
    }

    public void setIdPca(String idPca) {
        this.idPca = idPca;
    }

    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public void setMontantActuel(String montantActuel) {
        this.montantActuel = montantActuel;
    }

    public void setMontantRetro(String montantRetro) {
        this.montantRetro = montantRetro;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNoVersion(String noVersion) {
        this.noVersion = noVersion;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setPurRetro(boolean purRetro) {
        this.purRetro = purRetro;
    }

    public void setToSeparerMal(boolean isToSeparerMal) {
        this.isToSeparerMal = isToSeparerMal;
    }

    public void setTypeDecision(String typeDecision) {
        this.typeDecision = typeDecision;
    }

    public void setTypePcActuel(String typePcActuel) {
        this.typePcActuel = typePcActuel;
    }

    public void setTypePcPrecedant(String typePcPrecedant) {
        this.typePcPrecedant = typePcPrecedant;
    }
}
