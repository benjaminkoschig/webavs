package ch.globaz.pegasus.rpc.domaine;

import ch.globaz.common.domaine.Canton;

public class RpcAddress {
    private final Canton canton;
    private final String numOFS;
    private final String id;
    private final String type;
    private final String domaine;
    private final String idExterne;

    public RpcAddress() {
        canton = new Canton("");
        numOFS = null;
        id = null;
        type = null;
        domaine = null;
        idExterne = null;
    }

    public RpcAddress(Canton canton, String numOFS, String id, String type, String domaine, String idExterne) {
        this.canton = canton;
        this.numOFS = numOFS;
        this.id = id;
        this.type = type;
        this.domaine = domaine;
        this.idExterne = idExterne;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public Canton getCanton() {
        return canton;
    }

    public String getNumOFS() {
        return numOFS;
    }

    public String getType() {
        return type;
    }

    public String getDomaine() {
        return domaine;
    }

    public boolean isEmpty() {
        return id == null && numOFS == null;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Adresse d'annonce RPC [id=" + id + ", Canton=" + canton.getAbreviation() + ", numOFS=" + numOFS
                + ", type=" + type + ", domaine=" + domaine + ", idExterne=" + idExterne + "]";
    }

}
