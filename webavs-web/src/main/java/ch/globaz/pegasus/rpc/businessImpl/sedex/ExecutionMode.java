package ch.globaz.pegasus.rpc.businessImpl.sedex;

public enum ExecutionMode {

    REGENERATE,
    GENERATE,
    GENERATE_AND_SEND,
    SEND,
    SIMULATE;

    /**
     * Définit si il faut envoyer les annonces
     * 
     * @return true si on envoie les annonces
     */
    public boolean mustSendAnnonce() {
        return GENERATE_AND_SEND.equals(this) || SEND.equals(this);
    }

    public boolean isSimultate() {
        return SIMULATE.equals(this);
    }

}
