package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader;

class CalculeBlob {
    private final String idPlanCalcul;
    private final byte[] bytes;
    private final int sequence;

    public CalculeBlob(String idPlanCalcul, byte[] bytes, int sequence) {
        this.idPlanCalcul = idPlanCalcul;
        this.bytes = bytes;
        this.sequence = sequence;
    }

    public String getIdPlanCalcul() {
        return idPlanCalcul;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getSequence() {
        return sequence;
    }

}
