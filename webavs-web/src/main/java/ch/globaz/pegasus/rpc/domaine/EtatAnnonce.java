package ch.globaz.pegasus.rpc.domaine;

public enum EtatAnnonce {

    ERROR,
    OUVERT,
    CORRECTION,
    POUR_ENVOI,
    PLAUSI_KO;

    public boolean isError() {
        return ERROR.equals(this);
    }
}
