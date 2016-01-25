package globaz.corvus.acor.parser.xml.rev10.fcalcul;

public class REBaseCalculXmlDataStructure {

    private String anneeBte1 = null;
    private String anneeBte2 = null;

    private String anneeBte4 = null;
    private REBaseEchelleXmlDataStructure baseEchelle = null;
    /* not used fields */
    private String codeRefugie = null;

    private String codeSplitting = null;

    private String dateDebutAnticipation = null;
    private String dateRevocationAjournement = null;
    private String degreInvalidite = null;
    private String dureeAjournement = null;

    private String dureeRam = null;
    // Bases ram
    private String facteurRevalorisation = null;
    private String fractionRenteAI = null;
    private String generateur = null;
    private String genreInvalidite = null;
    private String id = null;
    // Invalidite
    private String invaliditePrecoce = null;
    private Boolean isAjournement = null;
    private Boolean isRevocationAjournement = null;

    private String nbrAnneeAnticipation = null;
    private String oai = null;

    private String ram = null;
    private String supplementCarriere = null;
    private String survenanceEvAssure = null;

    private String tauxReductionAnticipation = null;

    public String getAnneeBte1() {
        return anneeBte1;
    }

    public String getAnneeBte2() {
        return anneeBte2;
    }

    public String getAnneeBte4() {
        return anneeBte4;
    }

    public REBaseEchelleXmlDataStructure getBaseEchelle() {
        return baseEchelle;
    }

    public String getCodeRefugie() {
        return codeRefugie;
    }

    public String getCodeSplitting() {
        return codeSplitting;
    }

    public String getDateDebutAnticipation() {
        return dateDebutAnticipation;
    }

    public String getDateRevocationAjournement() {
        return dateRevocationAjournement;
    }

    public String getDegreInvalidite() {
        return degreInvalidite;
    }

    public String getDureeAjournement() {
        return dureeAjournement;
    }

    public String getDureeRam() {
        return dureeRam;
    }

    public String getFacteurRevalorisation() {
        return facteurRevalorisation;
    }

    public String getFractionRenteAI() {
        return fractionRenteAI;
    }

    public String getGenerateur() {
        return generateur;
    }

    public String getGenreInvalidite() {
        return genreInvalidite;
    }

    public String getId() {
        return id;
    }

    public String getInvaliditePrecoce() {
        return invaliditePrecoce;
    }

    public Boolean getIsAjournement() {
        return isAjournement;
    }

    public Boolean getIsRevocationAjournement() {
        return isRevocationAjournement;
    }

    public String getNbrAnneeAnticipation() {
        return nbrAnneeAnticipation;
    }

    public String getOai() {
        return oai;
    }

    public String getRam() {
        return ram;
    }

    public String getSupplementCarriere() {
        return supplementCarriere;
    }

    public String getSurvenanceEvAssure() {
        return survenanceEvAssure;
    }

    public String getTauxReductionAnticipation() {
        return tauxReductionAnticipation;
    }

    public void setAnneeBte1(String anneeBte1) {
        this.anneeBte1 = anneeBte1;
    }

    public void setAnneeBte2(String anneeBte2) {
        this.anneeBte2 = anneeBte2;
    }

    public void setAnneeBte4(String anneeBte4) {
        this.anneeBte4 = anneeBte4;
    }

    public void setBaseEchelle(REBaseEchelleXmlDataStructure baseEchelle) {
        this.baseEchelle = baseEchelle;
    }

    public void setCodeRefugie(String codeRefugie) {
        this.codeRefugie = codeRefugie;
    }

    public void setCodeSplitting(String codeSplitting) {
        this.codeSplitting = codeSplitting;
    }

    public void setDateDebutAnticipation(String dateDebutAnticipation) {
        this.dateDebutAnticipation = dateDebutAnticipation;
    }

    public void setDateRevocationAjournement(String dateRevocationAjourneent) {
        dateRevocationAjournement = dateRevocationAjourneent;
    }

    public void setDegreInvalidite(String degreInvalidite) {
        this.degreInvalidite = degreInvalidite;
    }

    public void setDureeAjournement(String dureeAjournement) {
        this.dureeAjournement = dureeAjournement;
    }

    public void setDureeRam(String dureeRam) {
        this.dureeRam = dureeRam;
    }

    public void setFacteurRevalorisation(String facteurRevalorisation) {
        this.facteurRevalorisation = facteurRevalorisation;
    }

    public void setFractionRenteAI(String fractionRenteAI) {
        this.fractionRenteAI = fractionRenteAI;
    }

    public void setGenerateur(String generateur) {
        this.generateur = generateur;
    }

    public void setGenreInvalidite(String genreInvalidite) {
        this.genreInvalidite = genreInvalidite;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setInvaliditePrecoce(String invaliditePrecoce) {
        this.invaliditePrecoce = invaliditePrecoce;
    }

    public void setIsAjournement(Boolean isAjournement) {
        this.isAjournement = isAjournement;
    }

    public void setIsRevocationAjournement(Boolean isRevocationAjournement) {
        this.isRevocationAjournement = isRevocationAjournement;
    }

    public void setNbrAnneeAnticipation(String anneeAnticipation) {
        nbrAnneeAnticipation = anneeAnticipation;
    }

    public void setOai(String oai) {
        this.oai = oai;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public void setSupplementCarriere(String supplementCarriere) {
        this.supplementCarriere = supplementCarriere;
    }

    public void setSurvenanceEvAssure(String survenanceEvAssure) {
        this.survenanceEvAssure = survenanceEvAssure;
    }

    public void setTauxReductionAnticipation(String tauxReductionAnticipation) {
        this.tauxReductionAnticipation = tauxReductionAnticipation;
    }

    public String toStringgg() {
        StringBuffer sb = new StringBuffer();
        sb.append("==================================================>>>\n");
        sb.append("\t\tREBaseCalculXmlDataStructure").append("\n");
        sb.append("==================================================>>>\n");
        sb.append("id = " + id).append("\n");
        sb.append("generateur 				= " + generateur).append("\n");
        sb.append("codeRefugie 				= " + codeRefugie).append("\n");
        sb.append("isAjournement 			= " + isAjournement).append("\n");
        sb.append("nbrAnneeAnticipation 	= " + nbrAnneeAnticipation).append("\n");
        sb.append("dateDebutAnticipation 	= " + dateDebutAnticipation).append("\n");
        sb.append("tauxReductionAnticipation= " + tauxReductionAnticipation).append("\n");
        sb.append("isRevocationAjournement 	= " + isRevocationAjournement).append("\n");
        sb.append("dateRevocationAjournement= " + dateRevocationAjournement).append("\n");
        sb.append("dureeAjournement 		= " + dureeAjournement).append("\n");
        sb.append("oai 						= " + oai).append("\n");
        sb.append("dureeRam 				= " + dureeRam).append("\n");
        sb.append("fractionRenteAI 			= " + fractionRenteAI).append("\n");
        sb.append("degreInvalidite 			= " + degreInvalidite).append("\n");
        sb.append("genreInvalidite 			= " + genreInvalidite).append("\n");
        sb.append("supplementCarriere 		= " + supplementCarriere).append("\n");
        sb.append("invaliditePrecoce 		= " + invaliditePrecoce).append("\n");
        sb.append("survenanceEvAssure 		= " + survenanceEvAssure).append("\n");
        sb.append("facteurRevalorisation 	= " + facteurRevalorisation).append("\n");
        sb.append("codeSplitting 			= " + codeSplitting).append("\n");
        sb.append("ram 						= " + ram).append("\n");

        if (baseEchelle != null) {
            sb.append("baseEchelle 				= \n" + baseEchelle.toStringgg()).append("\n");
        }

        sb.append("==================================================<<<\n\n");
        return sb.toString();

    }

}
