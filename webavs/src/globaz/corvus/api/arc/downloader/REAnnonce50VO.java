/*
 * Créé le 25 sept 08
 */
package globaz.corvus.api.arc.downloader;

public class REAnnonce50VO {

    public static final String CODE_TRAITEMENT_EN_COURS = "1";
    public static final String CODE_TRAITEMENT_NON_TRAITE = "2";
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    public static final String CODE_TRAITEMENT_TRAITE = "0";

    private String cantonEtatDomicil = "";
    private String codeMutation = "";
    private String codeTraitement = "";
    private String debutDroit = "";
    private String etatCivil = "";
    private String finDroit = "";
    private String genreAnnonce = "";
    private String genrePrestations = "";
    private String mensualitePrestation = "";
    private String mensualiteRORemplacee = "";
    private String moisRapport = "";
    private String nouveauNssAyantDroit = "";
    private String nssAssureCompl1 = "";
    private String nssAssureCompl2 = "";
    private String nssAyantDroit = "";
    private String numeroAgence = "";
    private String numeroAnnonce = "";
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String numeroCaisse = "";
    private String refInterne = "";
    private String refugie = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getCantonEtatDomicil() {
        return cantonEtatDomicil;
    }

    public String getCodeMutation() {
        return codeMutation;
    }

    public String getCodeTraitement() {
        return codeTraitement;
    }

    public String getDebutDroit() {
        return debutDroit;
    }

    public String getEtatCivil() {
        return etatCivil;
    }

    public String getFinDroit() {
        return finDroit;
    }

    public String getGenreAnnonce() {
        return genreAnnonce;
    }

    public String getGenrePrestations() {
        return genrePrestations;
    }

    /**
     * Donne le Level en fonction du genre de prestations
     * 
     * @return
     */
    public String getLevelRente() {

        // RO AVS
        if ("10".equals(getGenrePrestations()) || "13".equals(getGenrePrestations())
                || "14".equals(getGenrePrestations()) || "15".equals(getGenrePrestations())
                || "16".equals(getGenrePrestations()) || "33".equals(getGenrePrestations())
                || "34".equals(getGenrePrestations()) || "35".equals(getGenrePrestations())
                || "36".equals(getGenrePrestations())) {

            return "6";

            // REO AVS
        } else if ("20".equals(getGenrePrestations()) || "23".equals(getGenrePrestations())
                || "24".equals(getGenrePrestations()) || "25".equals(getGenrePrestations())
                || "26".equals(getGenrePrestations()) || "45".equals(getGenrePrestations())
                || "46".equals(getGenrePrestations())) {

            return "5";

            // API AVS
        } else if ("85".equals(getGenrePrestations()) || "86".equals(getGenrePrestations())
                || "87".equals(getGenrePrestations()) || "89".equals(getGenrePrestations())
                || "94".equals(getGenrePrestations()) || "95".equals(getGenrePrestations())
                || "96".equals(getGenrePrestations()) || "97".equals(getGenrePrestations())) {

            return "2";

            // RO AI
        } else if ("50".equals(getGenrePrestations()) || "53".equals(getGenrePrestations())
                || "54".equals(getGenrePrestations()) || "55".equals(getGenrePrestations())
                || "56".equals(getGenrePrestations())) {

            return "4";

            // REO AI
        } else if ("70".equals(getGenrePrestations()) || "72".equals(getGenrePrestations())
                || "73".equals(getGenrePrestations()) || "74".equals(getGenrePrestations())
                || "75".equals(getGenrePrestations()) || "76".equals(getGenrePrestations())) {

            return "3";

            // API AI
        } else {
            return "1";
        }
    }

    public String getMensualitePrestation() {
        return mensualitePrestation;
    }

    public String getMensualiteRORemplacee() {
        return mensualiteRORemplacee;
    }

    public String getMoisRapport() {
        return moisRapport;
    }

    public String getNouveauNssAyantDroit() {
        return nouveauNssAyantDroit;
    }

    public String getNssAssureCompl1() {
        return nssAssureCompl1;
    }

    public String getNssAssureCompl2() {
        return nssAssureCompl2;
    }

    public String getNssAyantDroit() {
        return nssAyantDroit;
    }

    public String getNumeroAgence() {
        return numeroAgence;
    }

    public String getNumeroAnnonce() {
        return numeroAnnonce;
    }

    public String getNumeroCaisse() {
        return numeroCaisse;
    }

    public String getRefInterne() {
        return refInterne;
    }

    public String getRefugie() {
        return refugie;
    }

    public void setCantonEtatDomicil(final String cantonEtatDomicil) {
        this.cantonEtatDomicil = cantonEtatDomicil;
    }

    public void setCodeMutation(final String codeMutation) {
        this.codeMutation = codeMutation;
    }

    public void setCodeTraitement(final String codeTraitement) {
        this.codeTraitement = codeTraitement;
    }

    public void setDebutDroit(final String debutDroit) {
        this.debutDroit = debutDroit;
    }

    public void setEtatCivil(final String etatCivil) {
        this.etatCivil = etatCivil;
    }

    public void setFinDroit(final String finDroit) {
        this.finDroit = finDroit;
    }

    public void setGenreAnnonce(final String genreAnnonce) {
        this.genreAnnonce = genreAnnonce;
    }

    public void setGenrePrestations(final String genrePrestations) {
        this.genrePrestations = genrePrestations;
    }

    public void setMensualitePrestation(final String mensualitePrestation) {
        this.mensualitePrestation = mensualitePrestation;
    }

    public void setMensualiteRORemplacee(final String mensualiteRORemplacee) {
        this.mensualiteRORemplacee = mensualiteRORemplacee;
    }

    public void setMoisRapport(final String moisRapport) {
        this.moisRapport = moisRapport;
    }

    public void setNouveauNssAyantDroit(final String nouveauNssAyantDroit) {
        this.nouveauNssAyantDroit = nouveauNssAyantDroit;
    }

    public void setNssAssureCompl1(final String nssAssureCompl1) {
        this.nssAssureCompl1 = nssAssureCompl1;
    }

    public void setNssAssureCompl2(final String nssAssureCompl2) {
        this.nssAssureCompl2 = nssAssureCompl2;
    }

    public void setNssAyantDroit(final String nssAyantDroit) {
        this.nssAyantDroit = nssAyantDroit;
    }

    public void setNumeroAgence(final String numeroAgence) {
        this.numeroAgence = numeroAgence;
    }

    public void setNumeroAnnonce(final String numeroAnnonce) {
        this.numeroAnnonce = numeroAnnonce;
    }

    public void setNumeroCaisse(final String numeroCaisse) {
        this.numeroCaisse = numeroCaisse;
    }

    public void setRefInterne(final String refInterne) {
        this.refInterne = refInterne;
    }

    public void setRefugie(final String refugie) {
        this.refugie = refugie;
    }

}
