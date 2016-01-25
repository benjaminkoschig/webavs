package globaz.corvus.process.communicationMutation;

import globaz.corvus.topaz.RECommunicationMutationOO;
import globaz.jade.job.AbstractJadeJob;
import java.util.List;

public class RECommunicationMutationProcess extends AbstractJadeJob {

    private static final long serialVersionUID = 1L;

    private String codeIsoLangueOfficeAI;
    private String emailAdresse;
    private Boolean sendToGed;

    private String adresseOfficeAi;
    private String adresseActuelleTiers = "";
    private String adresseAncienneTiers = "";

    private String dateDecesTiers;
    private Boolean isNouvelleAdresseTiers;
    private Boolean isNouvelleAdresseAutre;

    private String dateDebutHospitalisation;
    private String dateFinHospitalisation;
    private String dateDebutHome;
    private String dateFinHome;
    private String texteObservation;
    private List<String> listeAnnexes;
    private String idTiers;

    private String nssTiers;
    private String nomTiers;
    private String prenomTiers;

    private Boolean changementNom;
    private Boolean changementPrenom;
    private Boolean changementNSS;
    private Boolean changementAutre;
    private String inputChangementAutre;

    public String getAdresseActuelleTiers() {
        return adresseActuelleTiers;
    }

    public void setAdresseActuelleTiers(String adresseActuelleTiers) {
        this.adresseActuelleTiers = adresseActuelleTiers;
    }

    public String getAdresseAncienneTiers() {
        return adresseAncienneTiers;
    }

    public void setAdresseAncienneTiers(String adresseAncienneTiers) {
        this.adresseAncienneTiers = adresseAncienneTiers;
    }

    public Boolean getChangementNom() {
        return changementNom;
    }

    public void setChangementNom(Boolean changementNom) {
        this.changementNom = changementNom;
    }

    public Boolean getChangementPrenom() {
        return changementPrenom;
    }

    public void setChangementPrenom(Boolean changementPrenom) {
        this.changementPrenom = changementPrenom;
    }

    public Boolean getChangementNSS() {
        return changementNSS;
    }

    public void setChangementNSS(Boolean changementNSS) {
        this.changementNSS = changementNSS;
    }

    public Boolean getChangementAutre() {
        return changementAutre;
    }

    public void setChangementAutre(Boolean changementAutre) {
        this.changementAutre = changementAutre;
    }

    public String getInputChangementAutre() {
        return inputChangementAutre;
    }

    public void setInputChangementAutre(String inputChangementAutre) {
        this.inputChangementAutre = inputChangementAutre;
    }

    public RECommunicationMutationProcess() {
    }

    /**
     * Valide les paramètres du process
     * 
     * @param parameters Les paramètres à valider
     * @return une liste d'erreur(s). Retourne une liste vide si pas d'erreur
     */
    public final List<RECommunicationMutationParameterValidationError> validate() {
        return new RECommunicationMutationProcessParametersValidator().validate(this);
    }

    @Override
    public void run() {

        if (getSession() == null) {
            throw new IllegalArgumentException(
                    "RECommunicationMutationProcess constructor : you must provide a valid non null BSession");
        }

        // Validation des paramètres du process
        List<RECommunicationMutationParameterValidationError> errors = validate();
        if (errors != null && errors.size() > 0) {
            StringBuilder message = new StringBuilder();
            for (RECommunicationMutationParameterValidationError error : errors) {
                message.append(" -> ");
                message.append(getSession().getLabel(error.getLabelKey()));
            }
            throw new IllegalArgumentException(message.toString());
        }

        // running du process OO
        RECommunicationMutationOO processOO = new RECommunicationMutationOO();
        processOO.setSession(getSession());
        processOO.setAdresseEmail(emailAdresse);
        processOO.setCodeIsoLangueOfficeAI(codeIsoLangueOfficeAI);
        processOO.setSendToGed(sendToGed);
        processOO.setAdresseOfficeAi(adresseOfficeAi);
        processOO.setAdresseActuelleTiers(adresseActuelleTiers);
        processOO.setAdresseAncienneTiers(adresseAncienneTiers);
        processOO.setDateDecesTiers(dateDecesTiers);
        processOO.setIsNouvelleAdresseTiers(isNouvelleAdresseTiers);
        processOO.setIsNouvelleAdresseAutre(isNouvelleAdresseAutre);
        processOO.setDateDebutHospitalisation(dateDebutHospitalisation);
        processOO.setDateFinHospitalisation(dateFinHospitalisation);
        processOO.setDateDebutHome(dateDebutHome);
        processOO.setDateFinHome(dateFinHome);
        processOO.setTexteObservation(texteObservation);
        processOO.setListeAnnexes(listeAnnexes);
        processOO.setIdTiers(idTiers);
        processOO.setNssTiers(nssTiers);
        processOO.setNomTiers(nomTiers);
        processOO.setPrenomTiers(prenomTiers);
        processOO.setChangementNom(changementNom);
        processOO.setChangementPrenom(changementPrenom);
        processOO.setChangementNSS(changementNSS);
        processOO.setChangementAutre(changementAutre);
        processOO.setInputChangementAutre(inputChangementAutre);
        processOO.run();
    }

    @Override
    public String getDescription() {
        return "Impression de la lettre de communication de mutation à l'office AI";
    }

    @Override
    public String getName() {
        return "Impression de la lettre de communication de mutation à l'office AI";
    }

    /**
     * @return the codeIsoLangueOfficeAI
     */
    public final String getCodeIsoLangueOfficeAI() {
        return codeIsoLangueOfficeAI;
    }

    /**
     * @param codeIsoLangueOfficeAI the codeIsoLangueOfficeAI to set
     */
    public final void setCodeIsoLangueOfficeAI(String codeIsoLangueOfficeAI) {
        this.codeIsoLangueOfficeAI = codeIsoLangueOfficeAI;
    }

    /**
     * @return the emailAdresse
     */
    public final String getEmailAdresse() {
        return emailAdresse;
    }

    /**
     * @param emailAdresse the emailAdresse to set
     */
    public final void setEmailAdresse(String emailAdresse) {
        this.emailAdresse = emailAdresse;
    }

    /**
     * @return the sendToGed
     */
    public final Boolean getSendToGed() {
        return sendToGed;
    }

    /**
     * @param sendToGed the sendToGed to set
     */
    public final void setSendToGed(Boolean sendToGed) {
        this.sendToGed = sendToGed;
    }

    /**
     * @return the adresseOfficeAi
     */
    public final String getAdresseOfficeAi() {
        return adresseOfficeAi;
    }

    /**
     * @param adresseOfficeAi the adresseOfficeAi to set
     */
    public final void setAdresseOfficeAi(String adresseOfficeAi) {
        this.adresseOfficeAi = adresseOfficeAi;
    }

    /**
     * @return the dateDecesTiers
     */
    public final String getDateDecesTiers() {
        return dateDecesTiers;
    }

    /**
     * @param dateDecesTiers the dateDecesTiers to set
     */
    public final void setDateDecesTiers(String dateDecesTiers) {
        this.dateDecesTiers = dateDecesTiers;
    }

    /**
     * @return the isNouvelleAdresseTiers
     */
    public final Boolean getIsNouvelleAdresseTiers() {
        return isNouvelleAdresseTiers;
    }

    /**
     * @param isNouvelleAdresseTiers the isNouvelleAdresseTiers to set
     */
    public final void setIsNouvelleAdresseTiers(Boolean isNouvelleAdresseTiers) {
        this.isNouvelleAdresseTiers = isNouvelleAdresseTiers;
    }

    /**
     * @return the isNouvelleAdresseAutre
     */
    public final Boolean getIsNouvelleAdresseAutre() {
        return isNouvelleAdresseAutre;
    }

    /**
     * @param isNouvelleAdresseAutre the isNouvelleAdresseAutre to set
     */
    public final void setIsNouvelleAdresseAutre(Boolean isNouvelleAdresseAutre) {
        this.isNouvelleAdresseAutre = isNouvelleAdresseAutre;
    }

    /**
     * @return the dateDebutHospitalisation
     */
    public final String getDateDebutHospitalisation() {
        return dateDebutHospitalisation;
    }

    /**
     * @param dateDebutHospitalisation the dateDebutHospitalisation to set
     */
    public final void setDateDebutHospitalisation(String dateDebutHospitalisation) {
        this.dateDebutHospitalisation = dateDebutHospitalisation;
    }

    /**
     * @return the dateFinHospitalisation
     */
    public final String getDateFinHospitalisation() {
        return dateFinHospitalisation;
    }

    /**
     * @param dateFinHospitalisation the dateFinHospitalisation to set
     */
    public final void setDateFinHospitalisation(String dateFinHospitalisation) {
        this.dateFinHospitalisation = dateFinHospitalisation;
    }

    /**
     * @return the dateDebutHome
     */
    public final String getDateDebutHome() {
        return dateDebutHome;
    }

    /**
     * @param dateDebutHome the dateDebutHome to set
     */
    public final void setDateDebutHome(String dateDebutHome) {
        this.dateDebutHome = dateDebutHome;
    }

    /**
     * @return the dateFinHome
     */
    public final String getDateFinHome() {
        return dateFinHome;
    }

    /**
     * @param dateFinHome the dateFinHome to set
     */
    public final void setDateFinHome(String dateFinHome) {
        this.dateFinHome = dateFinHome;
    }

    /**
     * @return the texteObservation
     */
    public final String getTexteObservation() {
        return texteObservation;
    }

    /**
     * @param texteObservation the texteObservation to set
     */
    public final void setTexteObservation(String texteObservation) {
        this.texteObservation = texteObservation;
    }

    /**
     * @return the listeAnnexes
     */
    public final List<String> getListeAnnexes() {
        return listeAnnexes;
    }

    /**
     * @param listeAnnexes the listeAnnexes to set
     */
    public final void setListeAnnexes(List<String> listeAnnexes) {
        this.listeAnnexes = listeAnnexes;
    }

    /**
     * @return the idTiers
     */
    public final String getIdTiers() {
        return idTiers;
    }

    /**
     * @param idTiers the idTiers to set
     */
    public final void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * @return the nssTiers
     */
    public final String getNssTiers() {
        return nssTiers;
    }

    /**
     * @param nssTiers the nssTiers to set
     */
    public final void setNssTiers(String nssTiers) {
        this.nssTiers = nssTiers;
    }

    /**
     * @return the nomTiers
     */
    public final String getNomTiers() {
        return nomTiers;
    }

    /**
     * @param nomTiers the nomTiers to set
     */
    public final void setNomTiers(String nomTiers) {
        this.nomTiers = nomTiers;
    }

    /**
     * @return the prenomTiers
     */
    public final String getPrenomTiers() {
        return prenomTiers;
    }

    /**
     * @param prenomTiers the prenomTiers to set
     */
    public final void setPrenomTiers(String prenomTiers) {
        this.prenomTiers = prenomTiers;
    }

}
