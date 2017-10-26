package ch.globaz.pegasus.rpc.plausi.core;

public enum RpcPlausiCategory {
    NONE("code", false, false),
    AUTO("A", true, false),
    MANUAL("M", true, false),
    BLOCKING("0", true, false),
    ERROR("1", false, true),
    WARNING("2", false, true),
    INFO("3", false, false),
    INACTIVE("4", false, false),
    DATA_INTEGRITY("GZ", false, false);
    // DATAWAREHOUSE("D",false, null);
    private String code;
    private boolean refus;
    private boolean retour;

    private RpcPlausiCategory(String code, boolean refus, boolean retour) {
        this.code = code;
        this.refus = refus;
        this.retour = retour;
    }

    boolean isError() {
        return ERROR.equals(this);
    }

    boolean isAuto() {
        return AUTO.equals(this);
    }

    public static RpcPlausiCategory parseByCode(String code) {
        for (RpcPlausiCategory enu : RpcPlausiCategory.class.getEnumConstants()) {
            if (enu.code.equals(code)) {
                return enu;
            }
        }
        return null;
    }
}
