package ch.globaz.pegasus.business.constantes;

public enum EPCRegionLoyer {

    REGION_1("64049100"),
    REGION_2("64049101"),
    REGION_3("64049102");

    private String code = null;

    private EPCRegionLoyer(String code) {this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static EPCRegionLoyer fromValue(String code){
        for(EPCRegionLoyer region : EPCRegionLoyer.values()){
            if(region.code.equals(code)) {
                return region;
            }
        }
        return null;
    }

}
