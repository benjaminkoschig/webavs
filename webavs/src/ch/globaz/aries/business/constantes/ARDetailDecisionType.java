package ch.globaz.aries.business.constantes;

import ch.globaz.aries.business.exceptions.AriesTechnicalException;

public enum ARDetailDecisionType {

    ALPAGE("961010", ARParametrePlageValeur.MONTANT_UNITAIRE_ALPAGE),
    CULTURE_ARBORICOLE("961002", ARParametrePlageValeur.MONTANT_UNITAIRE_CULTURE_ARBORICOLE),
    CULTURE_MARAICHERE("961003", ARParametrePlageValeur.MONTANT_UNITAIRE_CULTURE_MARAICHERE),
    CULTURE_PLAINE("961001", ARParametrePlageValeur.MONTANT_UNITAIRE_CULTURE_PLAINE),
    UGB_MONTAGNE("961008", ARParametrePlageValeur.MONTANT_UNITAIRE_UGB_MONTAGNE),
    UGB_PLAINE("961007", ARParametrePlageValeur.MONTANT_UNITAIRE_UGB_PLAINE),

    UGB_SPECIAL("961009", ARParametrePlageValeur.MONTANT_UNITAIRE_UGB_SPECIAL),

    VIGNE_EST_CANTON("961005", ARParametrePlageValeur.MONTANT_UNITAIRE_VIGNE_EST_CANTON),

    VIGNE_LA_COTE("961006", ARParametrePlageValeur.MONTANT_UNITAIRE_VIGNE_LA_COTE),

    VIGNE_NORD_CANTON("961004", ARParametrePlageValeur.MONTANT_UNITAIRE_VIGNE_NORD_CANTON);

    public static ARDetailDecisionType getEnumFromCodeSystem(String codeSystem) {

        for (ARDetailDecisionType type : ARDetailDecisionType.values()) {
            if (type.getCodeSystem().equals(codeSystem)) {
                return type;
            }
        }

        throw new AriesTechnicalException(ARDetailDecisionType.class.getName() + " : no enum founded for codeSystem("
                + codeSystem + ")");

    }

    private String codeSystem;
    private ARParametrePlageValeur parametrePlageValeur;

    private ARDetailDecisionType(String codeSystem, ARParametrePlageValeur theParametrePlageValeur) {
        this.codeSystem = codeSystem;
        parametrePlageValeur = theParametrePlageValeur;
    }

    public boolean equals(String codeSystem) {
        return this.equals(ARDetailDecisionType.getEnumFromCodeSystem(codeSystem));
    }

    public String getCodeSystem() {
        return codeSystem;
    }

    public ARParametrePlageValeur getParametrePlageValeur() {
        return parametrePlageValeur;
    }

}
