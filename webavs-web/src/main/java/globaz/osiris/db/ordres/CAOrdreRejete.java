package globaz.osiris.db.ordres;

// TODO en faire un BEntity et l'intégrer dans JADE......
public class CAOrdreRejete {
    private String idOrdre;
    // balise <Cd>
    private String code;
    // balise <Prtry>
    private String proprietary;
    // balise <AddtlInf>
    private String additionalInformations;

    public String getIdOrdre() {
        return idOrdre;
    }

    public void setIdOrdre(String idOrdre) {
        this.idOrdre = idOrdre;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProprietary() {
        return proprietary;
    }

    public void setProprietary(String proprietary) {
        this.proprietary = proprietary;
    }

    public String getAdditionalInformations() {
        return additionalInformations;
    }

    public void setAdditionalInformations(String additionalInformations) {
        this.additionalInformations = additionalInformations;
    }

    public void save() {
        // sera hérité de BEntity lorsqu'il sera implémenté...
        System.out.println("should have saved the current Rejected status for ordre=" + getIdOrdre());
    }

}
