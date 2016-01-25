package globaz.corvus.annonce.domain.annonce;

import globaz.corvus.annonce.RECodeApplicationProvider;
import globaz.corvus.annonce.RENSS;
import globaz.corvus.annonce.REPrefixPourReferenceInterneCaisse;
import globaz.corvus.annonce.REPrefixPourReferenceInterneCaisseProvider;
import globaz.corvus.annonce.RETypeAnnonce10emeRevision;
import globaz.corvus.annonce.domain.REAnnonce3A;

/**
 * Représente une annonce d'augmentation 10ème révision (annonce 44)
 * 
 * @author lga
 * 
 */
public final class REAnnonceAugmentation10EmeRevision extends REAnnonce3A {

    private RENSS nouveauNoAssureAyantDroit;

    @Override
    public final RECodeApplicationProvider getCodeApplicationProvider() {
        return RETypeAnnonce10emeRevision.ANNONCE_AUGMENTATION;
    }

    @Override
    public REPrefixPourReferenceInterneCaisseProvider getPrefixPourReferenceInterneCaisseProvider() {
        return REPrefixPourReferenceInterneCaisse.ANNONCE_AUGMENTATION;
    }

    /**
     * @return the nouveauNoAssureAyantDroit
     */
    public final RENSS getNouveauNoAssureAyantDroit() {
        return nouveauNoAssureAyantDroit;
    }

    /**
     * Le numéro NSS de l'ayant droit
     * 
     * @param nouveauNoAssureAyantDroit numéro NSS de l'ayant droit
     */
    public final void setNouveauNoAssureAyantDroit(RENSS nouveauNoAssureAyantDroit) {
        this.nouveauNoAssureAyantDroit = nouveauNoAssureAyantDroit;
    }

}
