package ch.globaz.orion.business.models.adi;

public class RecapDemandesTransmises {
    private final String noAffilie;
    private final String nom;
    private final String prenom;
    private final String annee;
    private final String beneficeNetNew;
    private final String capitalNew;
    private final String beneficeNetCurrent;
    private final String capitalCurrent;
    private final String statut;
    private final String type;
    private final String refus;
    private final String message;

    public RecapDemandesTransmises(RecapDemandesTransmisesBuilder builder) {
        noAffilie = builder.getNoAffilie();
        nom = builder.getNom();
        prenom = builder.getPrenom();
        annee = builder.getAnnee();
        beneficeNetNew = builder.getBeneficeNetNew();
        capitalNew = builder.getCapitalNew();
        beneficeNetCurrent = builder.getBeneficeNetCurrent();
        capitalCurrent = builder.getCapitalCurrent();
        statut = builder.getStatut();
        type = builder.getType();
        refus = builder.getRefus();
        message = builder.getMessages();
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

    public String getType() {
        return type;
    }

    public String getRefus() {
        return refus;
    }

    public String getMessage() {
        return message;
    }
}
