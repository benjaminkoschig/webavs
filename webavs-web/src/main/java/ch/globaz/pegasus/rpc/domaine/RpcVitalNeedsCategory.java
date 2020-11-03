package ch.globaz.pegasus.rpc.domaine;

public enum RpcVitalNeedsCategory {
    NO_NEEDS,
    ALONE,
    COUPLE,
    CHILD,
    TEENAGER;

    public boolean isChild() {
        return RpcVitalNeedsCategory.CHILD.equals(this);
    }
    public boolean isTeenager(){
        return RpcVitalNeedsCategory.TEENAGER.equals(this);
    }

    public boolean isCouple() {
        return RpcVitalNeedsCategory.COUPLE.equals(this);
    }

    public boolean isAlone() {
        return RpcVitalNeedsCategory.ALONE.equals(this);
    }

    public boolean isNoNeed() {
        return RpcVitalNeedsCategory.NO_NEEDS.equals(this);
    }


}
