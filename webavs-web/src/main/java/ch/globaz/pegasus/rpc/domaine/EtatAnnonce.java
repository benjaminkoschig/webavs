package ch.globaz.pegasus.rpc.domaine;

public enum EtatAnnonce {

    ERROR,
    OUVERT,
    ENVOYE,
    CORRECTION,
    POUR_ENVOI,
    PLAUSI_KO;

    public boolean isError() {
        return ERROR.equals(this);
    }
}
