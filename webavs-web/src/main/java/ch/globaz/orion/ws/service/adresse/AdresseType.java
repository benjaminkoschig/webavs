package ch.globaz.orion.ws.service.adresse;

public enum AdresseType {
    DOMICILE,
    COURRIER,
    INDEFINI;

    public boolean isDomicile() {
        return AdresseType.DOMICILE.equals(this);
    }

    public boolean isCourrier() {
        return AdresseType.COURRIER.equals(this);
    }

}
