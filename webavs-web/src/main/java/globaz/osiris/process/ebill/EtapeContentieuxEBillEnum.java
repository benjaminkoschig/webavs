package globaz.osiris.process.ebill;

public enum EtapeContentieuxEBillEnum {

    RECLAMATION_FRAIS_ET_INTERET("7"),
    SOMMATION("31"),
    DECISION("39");

    private String idEtape;

    EtapeContentieuxEBillEnum(String value){
        this.idEtape = value;
    }
    
    public String getIdEtape() {
        return idEtape;
    }

    public static boolean contains(String idEtape) {
        for (EtapeContentieuxEBillEnum c : EtapeContentieuxEBillEnum.values()) {
            if (c.getIdEtape().equals(idEtape)) {
                return true;
            }
        }
        return false;
    }
}
