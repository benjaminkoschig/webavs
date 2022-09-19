package ch.globaz.eform.businessimpl.services.sedex.constant;

public enum GFActionSedex {
    DEMANDE(5), REPONSE(6);

    private Integer code;

    GFActionSedex(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
