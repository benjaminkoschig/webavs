package ch.globaz.orion.business.models.adi;

public class RecapDemandesTransmisesBuilder {
    private String noAffilie;
    private String nom;
    private String prenom;
    private String annee;
    private String beneficeNetNew;
    private String capitalNew;
    private String beneficeNetCurrent;
    private String capitalCurrent;
    private String statut;
    private String type;
    private String refus;
    private String messages;

    public RecapDemandesTransmisesBuilder() {
        super();
    }

    public RecapDemandesTransmises build() {
        return new RecapDemandesTransmises(this);
    }

    public RecapDemandesTransmisesBuilder withNoAffilie(String noAffilie) {
        this.noAffilie = noAffilie;
        return this;
    }

    public RecapDemandesTransmisesBuilder withNom(String nom) {
        this.nom = nom;
        return this;
    }

    public RecapDemandesTransmisesBuilder withPrenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public RecapDemandesTransmisesBuilder withAnnee(String annee) {
        this.annee = annee;
        return this;
    }

    public RecapDemandesTransmisesBuilder withBeneficeNetNew(String beneficeNetNew) {
        this.beneficeNetNew = beneficeNetNew;
        return this;
    }

    public RecapDemandesTransmisesBuilder withCapitalNew(String capitalNew) {
        this.capitalNew = capitalNew;
        return this;
    }

    public RecapDemandesTransmisesBuilder withBeneficeNetCurrent(String beneficeNetCurrent) {
        this.beneficeNetCurrent = beneficeNetCurrent;
        return this;
    }

    public RecapDemandesTransmisesBuilder withCapitalCurrent(String capitalCurrent) {
        this.capitalCurrent = capitalCurrent;
        return this;
    }

    public RecapDemandesTransmisesBuilder withStatut(String statut) {
        this.statut = statut;
        return this;
    }

    public RecapDemandesTransmisesBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public RecapDemandesTransmisesBuilder withRefus(String refus) {
        this.refus = refus;
        return this;
    }

    public RecapDemandesTransmisesBuilder withMessages(String messages) {
        this.messages = messages;
        return this;
    }

    public String getNoAffilie() {
        return noAffilie;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getAnnee() {
        return annee;
    }

    public String getBeneficeNetNew() {
        return beneficeNetNew;
    }

    public String getCapitalNew() {
        return capitalNew;
    }

    public String getBeneficeNetCurrent() {
        return beneficeNetCurrent;
    }

    public String getCapitalCurrent() {
        return capitalCurrent;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRefus() {
        return refus;
    }

    public String getMessages() {
        return messages;
    }
}
