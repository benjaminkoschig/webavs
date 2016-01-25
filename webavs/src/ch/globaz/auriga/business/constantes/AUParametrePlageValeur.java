package ch.globaz.auriga.business.constantes;

import ch.globaz.common.business.interfaces.ParametrePlageValeurInterface;

public enum AUParametrePlageValeur implements ParametrePlageValeurInterface {

    ALLOCATION_00_16("ALLOC0016"),
    ALLOCATION_17_20("ALLOC1720"),
    RUBRIQUE_ALLOCATION_ENFANT("RUBRIAFCAP");

    private String cle;

    private AUParametrePlageValeur(String cle) {
        this.cle = cle;
    }

    @Override
    public String getCle() {
        return cle;
    }

}
