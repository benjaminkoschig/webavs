package globaz.corvus.vb.mutation;

import globaz.globall.db.BSession;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.ArrayList;
import java.util.List;

public class RECommunicationMutationOaiViewBean extends PRAbstractViewBeanSupport {

    private String adresseActuelleTiers = "";
    private String adresseAncienneTiers = "";

    private StringBuffer adresseOfficeAi = new StringBuffer();
    private String langueOfficeAI;
    private String dateDebutEntreeHome = "";
    private String dateDebutHospitalisation = "";
    private String dateDecesAssure = "";
    private String dateFinEntreeHome = "";
    private String dateFinHospitalisation = "";
    private Boolean displaySendToGed = Boolean.FALSE;
    private String idDemande = "";
    private String idTiers = "";
    private String nssTiers = "";
    private String nomTiers = "";
    private String prenomTiers = "";
    private String nouveauTexteAnnexe = "";
    private Boolean isNouvelleAdresseAssure = Boolean.FALSE;
    private Boolean isNouvelleAdresseRepresentantAutorite = Boolean.FALSE;
    private BSession session = null;
    private String texteObservation = "";
    private List<String> listeAnnexe = new ArrayList<String>();
    private Boolean sendToGed = Boolean.FALSE;

    private Boolean changementNom = Boolean.FALSE;
    private Boolean changementPrenom = Boolean.FALSE;
    private Boolean changementNSS = Boolean.FALSE;
    private Boolean changementAutre = Boolean.FALSE;
    private String inputChangementAutre = "";

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

    public String getInputChangementAutre() {
        return inputChangementAutre;
    }

    public void setInputChangementAutre(String inputChangementAutre) {
        this.inputChangementAutre = inputChangementAutre;
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

    public Boolean getSendToGed() {
        return sendToGed;
    }

    public void setSendToGed(Boolean sendToGed) {
        this.sendToGed = sendToGed;
    }

    public String getNouveauTexteAnnexe() {
        return nouveauTexteAnnexe;
    }

    public void setNouveauTexteAnnexe(String nouveauTexteAnnexe) {
        this.nouveauTexteAnnexe = nouveauTexteAnnexe;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNssTiers() {
        return nssTiers;
    }

    public void setNssTiers(String nssTiers) {
        this.nssTiers = nssTiers;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * @return the dateDebutEntreeHome
     */
    public String getDateDebutEntreeHome() {
        return dateDebutEntreeHome;
    }

    /**
     * @return the dateDebutHospitalisation
     */
    public String getDateDebutHospitalisation() {
        return dateDebutHospitalisation;
    }

    /**
     * @return the dateDecesAssure
     */
    public String getDateDecesAssure() {
        return dateDecesAssure;
    }

    /**
     * @return the dateFinEntreeHome
     */
    public String getDateFinEntreeHome() {
        return dateFinEntreeHome;
    }

    /**
     * @return the dateFinHospitalisation
     */
    public String getDateFinHospitalisation() {
        return dateFinHospitalisation;
    }

    /**
     * @return the displaySendToGed
     */
    public Boolean getDisplaySendToGed() {
        return displaySendToGed;
    }

    /**
     * @return the idDemande
     */
    public String getIdDemande() {
        return idDemande;
    }

    /**
     * @return the isNouvelleAdresseAssure
     */
    public Boolean getIsNouvelleAdresseAssure() {
        return isNouvelleAdresseAssure;
    }

    /**
     * @return the isNouvelleAdresseRepresentantAutorite
     */
    public Boolean getIsNouvelleAdresseRepresentantAutorite() {
        return isNouvelleAdresseRepresentantAutorite;
    }

    /**
     * @return the session
     */
    @Override
    public BSession getSession() {
        return session;
    }

    /**
     * @return the texteObservation
     */
    public String getTexteObservation() {
        return texteObservation;
    }

    /**
     * @param dateDebutEntreeHome
     *            the dateDebutEntreeHome to set
     */
    public void setDateDebutEntreeHome(String dateDebutEntreeHome) {
        this.dateDebutEntreeHome = dateDebutEntreeHome;
    }

    public StringBuffer getAdresseOfficeAi() {
        return adresseOfficeAi;
    }

    public void setAdresseOfficeAi(StringBuffer adresseOfficeAi) {
        this.adresseOfficeAi = adresseOfficeAi;
    }

    /**
     * @param dateDebutHospitalisation
     *            the dateDebutHospitalisation to set
     */
    public void setDateDebutHospitalisation(String dateDebutHospitalisation) {
        this.dateDebutHospitalisation = dateDebutHospitalisation;
    }

    /**
     * @param dateDecesAssure
     *            the dateDecesAssure to set
     */
    public void setDateDecesAssure(String dateDecesAssure) {
        this.dateDecesAssure = dateDecesAssure;
    }

    /**
     * @param dateFinEntreeHome
     *            the dateFinEntreeHome to set
     */
    public void setDateFinEntreeHome(String dateFinEntreeHome) {
        this.dateFinEntreeHome = dateFinEntreeHome;
    }

    /**
     * @param dateFinHospitalisation
     *            the dateFinHospitalisation to set
     */
    public void setDateFinHospitalisation(String dateFinHospitalisation) {
        this.dateFinHospitalisation = dateFinHospitalisation;
    }

    /**
     * @param displaySendToGed
     *            the displaySendToGed to set
     */
    public void setDisplaySendToGed(Boolean displaySendToGed) {
        this.displaySendToGed = displaySendToGed;
    }

    /**
     * @param idDemande
     *            the idDemande to set
     */
    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    /**
     * @param isNouvelleAdresseAssure
     *            the isNouvelleAdresseAssure to set
     */
    public void setIsNouvelleAdresseAssure(Boolean isNouvelleAdresseAssure) {
        this.isNouvelleAdresseAssure = isNouvelleAdresseAssure;
    }

    /**
     * @param isNouvelleAdresseRepresentantAutorite
     *            the isNouvelleAdresseRepresentantAutorite to set
     */
    public void setIsNouvelleAdresseRepresentantAutorite(Boolean isNouvelleAdresseRepresentantAutorite) {
        this.isNouvelleAdresseRepresentantAutorite = isNouvelleAdresseRepresentantAutorite;
    }

    /**
     * @param session
     *            the session to set
     */
    public void setSession(BSession session) {
        this.session = session;
    }

    /**
     * @param texteObservation
     *            the texteObservation to set
     */
    public void setTexteObservation(String texteObservation) {
        this.texteObservation = texteObservation;
    }

    public List<String> getListeAnnexe() {
        return listeAnnexe;
    }

    public void setListeAnnexe(List<String> annexe) {
        listeAnnexe = annexe;
    }

    @Override
    public boolean validate() {
        // TODO Auto-generated method stub
        return true;
    }

    public String getLangueOfficeAI() {
        return langueOfficeAI;
    }

    public void setLangueOfficeAI(String langueOfficeAI) {
        this.langueOfficeAI = langueOfficeAI;
    }

    public String getNomTiers() {
        return nomTiers;
    }

    public void setNomTiers(String nomTiers) {
        this.nomTiers = nomTiers;
    }

    public String getPrenomTiers() {
        return prenomTiers;
    }

    public void setPrenomTiers(String prenomTiers) {
        this.prenomTiers = prenomTiers;
    }

}
