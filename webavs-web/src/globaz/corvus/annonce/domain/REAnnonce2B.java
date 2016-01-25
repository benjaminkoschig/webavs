package globaz.corvus.annonce.domain;

import globaz.corvus.annonce.RENSS;

/**
 * REAAL2B
 * Repr�sente le niveau 2B des annonces de rentes
 * 
 * @author lga
 * 
 */
public abstract class REAnnonce2B extends REAnnonce1A {

    private RENSS nouveauNoAssureAyantDroit;

    /**
     * @return the nouveauNoAssureAyantDroit
     */
    public final RENSS getNouveauNoAssureAyantDroit() {
        return nouveauNoAssureAyantDroit;
    }

    /**
     * Le num�ro NSS de l'ayant droit
     * 
     * @param nouveauNoAssureAyantDroit num�ro NSS de l'ayant droit
     */
    public final void setNouveauNoAssureAyantDroit(RENSS nouveauNoAssureAyantDroit) {
        this.nouveauNoAssureAyantDroit = nouveauNoAssureAyantDroit;
    }

}
