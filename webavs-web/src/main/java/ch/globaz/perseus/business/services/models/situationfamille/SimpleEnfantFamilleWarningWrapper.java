package ch.globaz.perseus.business.services.models.situationfamille;

public class SimpleEnfantFamilleWarningWrapper {

    public String msg = null;

    public SimpleEnfantFamilleWarningWrapper() {
    }

    public SimpleEnfantFamilleWarningWrapper(String msg) {
        this.msg = msg;

    }

    public Boolean containsMessages() {
        return null != msg;
    }

}
