package ch.globaz.pegasus.business.domaine.pca;

import ch.globaz.common.domaine.Checkers;

public enum PcaSituation {
    DOM2R(),
    DOMICILE(),
    COUPLE_SEPARE_CONJOINT_HOME(),
    COUPLE_SEPARE_REQUERANT_HOME(),
    HOME(),
    COUPLE_SEPARE_DEUX_EN_HOME(),
    INDEFINIT();

    public static PcaSituation resolve(PcaGenre pcaGenreRequerant, PcaGenre pcaGenreConjoint,
            boolean hasBeneficiaireConjointDom2R) {
        Checkers.checkNotNull(pcaGenreRequerant, "pcaGenreRequerant");

        boolean isDom2R = hasBeneficiaireConjointDom2R;
        if (pcaGenreRequerant.isDomicile()) {
            if (pcaGenreConjoint == null) {
                if (!isDom2R) {
                    return DOMICILE;
                } else {
                    return DOM2R;
                }
            } else if (pcaGenreConjoint.isHome() && !isDom2R) {
                return COUPLE_SEPARE_CONJOINT_HOME;
            } else if (isDom2R) {
                throw new IllegalArgumentException(
                        "Il n'est pas possible d'avoir un couple séparé par la maladi et dom2R en même temps");
            } else {
                throw new IllegalArgumentException("Il n'est pas possible d'avoir deux pca de type domicile");
            }
        } else if (pcaGenreRequerant.isHome()) {
            if (pcaGenreConjoint == null) {
                if (!isDom2R) {
                    return HOME;
                } else {
                    throw new IllegalArgumentException("Il n'est pas possible d'etre en home et dom2R en même temps");
                }
            } else {
                if (pcaGenreConjoint.isDomicile() && !isDom2R) {
                    return COUPLE_SEPARE_REQUERANT_HOME;
                } else if (pcaGenreConjoint.isHome() && !isDom2R) {
                    return COUPLE_SEPARE_DEUX_EN_HOME;
                } else {
                    throw new IllegalArgumentException(
                            "Il n'est pas possible d'etre un couple séparé para la maladei et d'étre un  dom2R en même temps");
                }
            }
        } else {
            throw new IllegalArgumentException("Impossible de détérminer le genre de pca");
        }
    }

    public boolean isDom2() {
        return equals(DOM2R);
    }

    public boolean isDomicile() {
        return equals(DOMICILE);
    }

    public boolean isHome() {
        return equals(HOME);
    }

    public boolean isCoupleSepareConjoinEnHome() {
        return equals(COUPLE_SEPARE_CONJOINT_HOME);
    }

    public boolean isCoupleSepareRequerantEnHome() {
        return equals(COUPLE_SEPARE_REQUERANT_HOME);
    }

    public boolean isCoupleSepareLesDeuxHome() {
        return equals(COUPLE_SEPARE_DEUX_EN_HOME);
    }

    public boolean isCoupleSepare() {
        return isCoupleSepareRequerantEnHome() || isCoupleSepareConjoinEnHome() || isCoupleSepareLesDeuxHome();
    }

}
