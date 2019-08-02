package ch.globaz.naos.ws.contact;

public enum EnumSexe {

    H("H"),
    F("F");

    private String sexe;

    EnumSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getSexe() {
        return sexe;
    }
}
