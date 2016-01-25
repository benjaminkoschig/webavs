package ch.globaz.aries.business.constantes;

import ch.globaz.common.business.interfaces.ParametrePlageValeurInterface;

public enum ARParametrePlageValeur implements ParametrePlageValeurInterface {

    COTISATION_PALIER_1("COTPALIER1"),

    COTISATION_PALIER_2("COTPALIER2"),

    COTISATION_PLAFOND("COTPLAFOND"),

    MONTANT_UNITAIRE_ALPAGE("MONTUNALP"),

    MONTANT_UNITAIRE_CULTURE_ARBORICOLE("MONTUNCA"),

    MONTANT_UNITAIRE_CULTURE_MARAICHERE("MONTUNCM"),

    MONTANT_UNITAIRE_CULTURE_PLAINE("MONTUNCP"),

    MONTANT_UNITAIRE_UGB_MONTAGNE("MONTUNUGBM"),

    MONTANT_UNITAIRE_UGB_PLAINE("MONTUNUGBP"),

    MONTANT_UNITAIRE_UGB_SPECIAL("MONTUNUGBS"),

    MONTANT_UNITAIRE_VIGNE_EST_CANTON("MONTUNVEC"),

    MONTANT_UNITAIRE_VIGNE_LA_COTE("MONTUNVLC"),

    MONTANT_UNITAIRE_VIGNE_NORD_CANTON("MONTUNVNC");

    private String cle;

    private ARParametrePlageValeur(String cle) {
        this.cle = cle;
    }

    @Override
    public String getCle() {
        return cle;
    }

}
