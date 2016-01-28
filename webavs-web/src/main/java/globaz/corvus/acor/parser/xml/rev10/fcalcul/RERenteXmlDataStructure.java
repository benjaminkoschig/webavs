package globaz.corvus.acor.parser.xml.rev10.fcalcul;

public class RERenteXmlDataStructure {

    private String anneeBonificationTransitoire = null;
    private String codeCasSpeciaux = null;
    private String codeMutation = null;
    private String codePrestation = null;

    private String dateDebutDroit = null;
    private String dateFinDroit = null;
    private String droitApplique = null;
    private String echelleRente = null;
    /* not used fields */
    private REEtatXmlDataStructure[] etats = null;
    private String finDroitEcheance = null;

    private String idBaseCalcul = null;

    private String nssCmpl1 = null;
    private String nssCmpl2 = null;
    private String prescrApplicArt29LAI = null;
    private String prescrApplicArt48LAI = null;

    private String prescrApplicAt46LAVS = null;
    private String reducFauteGrave = null;
    private String remarques = null;
    private REVersementXmlDataStructure versement = null;

    public String getAnneeBonificationTransitoire() {
        return anneeBonificationTransitoire;
    }

    public String getCodeCasSpeciaux() {
        return codeCasSpeciaux;
    }

    public String getCodeMutation() {
        return codeMutation;
    }

    public String getCodePrestation() {
        return codePrestation;
    }

    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    public String getDateFinDroit() {
        return dateFinDroit;
    }

    public String getDroitApplique() {
        return droitApplique;
    }

    public String getEchelleRente() {
        return echelleRente;
    }

    public REEtatXmlDataStructure[] getEtats() {
        return etats;
    }

    public String getFinDroitEcheance() {
        return finDroitEcheance;
    }

    public String getIdBaseCalcul() {
        return idBaseCalcul;
    }

    public String getNssCmpl1() {
        return nssCmpl1;
    }

    public String getNssCmpl2() {
        return nssCmpl2;
    }

    public String getPrescrApplicArt29LAI() {
        return prescrApplicArt29LAI;
    }

    public String getPrescrApplicArt48LAI() {
        return prescrApplicArt48LAI;
    }

    public String getPrescrApplicAt46LAVS() {
        return prescrApplicAt46LAVS;
    }

    public String getReducFauteGrave() {
        return reducFauteGrave;
    }

    public String getRemarques() {
        return remarques;
    }

    public REVersementXmlDataStructure getVersement() {
        return versement;
    }

    public void setAnneeBonificationTransitoire(String anneeBonificationTransitoire) {
        this.anneeBonificationTransitoire = anneeBonificationTransitoire;
    }

    public void setCodeCasSpeciaux(String codeCasSpeciaux) {
        this.codeCasSpeciaux = codeCasSpeciaux;
    }

    public void setCodeMutation(String codeMutation) {
        this.codeMutation = codeMutation;
    }

    public void setCodePrestation(String codePrestation) {
        this.codePrestation = codePrestation;
    }

    public void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    public void setDateFinDroit(String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    public void setDroitApplique(String elm) {
        droitApplique = elm;
    }

    public void setEchelleRente(String echelleRente) {
        this.echelleRente = echelleRente;
    }

    public void setEtat(REEtatXmlDataStructure[] etats) {
        this.etats = etats;
    }

    public void setFinDroitEcheance(String finDroitEcheance) {
        this.finDroitEcheance = finDroitEcheance;
    }

    public void setIdBaseCalcul(String idBaseCalcul) {
        this.idBaseCalcul = idBaseCalcul;
    }

    public void setNssCmpl1(String nssCmpl1) {
        this.nssCmpl1 = nssCmpl1;
    }

    public void setNssCmpl2(String nssCmpl2) {
        this.nssCmpl2 = nssCmpl2;
    }

    public void setPrescrApplicArt29LAI(String prescrApplicArt29LAI) {
        this.prescrApplicArt29LAI = prescrApplicArt29LAI;
    }

    public void setPrescrApplicArt48LAI(String prescrApplicArt48LAI) {
        this.prescrApplicArt48LAI = prescrApplicArt48LAI;
    }

    public void setPrescrApplicAt46LAVS(String prescrApplicAt46LAVS) {
        this.prescrApplicAt46LAVS = prescrApplicAt46LAVS;
    }

    public void setReducFauteGrave(String reducFauteGrave) {
        this.reducFauteGrave = reducFauteGrave;
    }

    public void setRemarques(String remarques) {
        this.remarques = remarques;
    }

    public void setVersement(REVersementXmlDataStructure versement) {
        this.versement = versement;
    }

    public String toStringgg() {
        StringBuffer sb = new StringBuffer();
        sb.append("==================================================>>>\n");
        sb.append("\t\tRERenteXmlDataStructure").append("\n");
        sb.append("==================================================>>>\n");
        sb.append("idBaseCalcul 				= " + idBaseCalcul).append("\n");
        sb.append("codeCasSpeciaux 				= " + codeCasSpeciaux).append("\n");
        sb.append("anneeBonificationTransitoire = " + anneeBonificationTransitoire).append("\n");
        sb.append("echelleRente 				= " + echelleRente).append("\n");
        sb.append("codeMutation 				= " + codeMutation).append("\n");
        sb.append("codePrestation 				= " + codePrestation).append("\n");
        sb.append("droitApplique 				= " + droitApplique).append("\n");
        sb.append("reducFauteGrave 				= " + reducFauteGrave).append("\n");
        sb.append("prescrApplicArt48LAI 		= " + prescrApplicArt48LAI).append("\n");
        sb.append("prescrApplicArt29LAI 		= " + prescrApplicArt29LAI).append("\n");
        sb.append("prescrApplicAt46LAVS 		= " + prescrApplicAt46LAVS).append("\n");
        sb.append("dateDebutDroit 				= " + dateDebutDroit).append("\n");
        sb.append("dateFinDroit 				= " + dateFinDroit).append("\n");
        sb.append("finDroitEcheance 			= " + finDroitEcheance).append("\n");
        sb.append("nssCmpl1 					= " + nssCmpl1).append("\n");
        sb.append("nssCmpl2 					= " + nssCmpl2).append("\n");
        sb.append("etats 						= \n\n");

        if (etats != null) {
            for (int i = 0; i < etats.length; i++) {
                sb.append(etats[i].toStringgg());
            }
        }

        if (versement != null) {
            sb.append("versement 					= \n\n" + versement.toStringgg()).append("\n");
        }
        sb.append("==================================================<<<\n\n");
        return sb.toString();

    }

}
