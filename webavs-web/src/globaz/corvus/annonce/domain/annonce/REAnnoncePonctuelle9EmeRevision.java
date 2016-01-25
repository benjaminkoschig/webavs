package globaz.corvus.annonce.domain.annonce;

import globaz.corvus.annonce.RECodeApplicationProvider;
import globaz.corvus.annonce.RENSS;
import globaz.corvus.annonce.REPrefixPourReferenceInterneCaisse;
import globaz.corvus.annonce.REPrefixPourReferenceInterneCaisseProvider;
import globaz.corvus.annonce.RETypeAnnonce9emeRevision;
import globaz.corvus.annonce.domain.REAnnonce3B;

/**
 * REANN41
 * Représente une annonce d'augmentation 9ème révision (annonce 44)
 * 
 * @author lga
 * 
 */
public final class REAnnoncePonctuelle9EmeRevision extends REAnnonce3B {

    private Integer montantRenteOrdinaireRemplace;
    private RENSS nouveauNoAssureAyantDroit;

    /**
     * Ces champs existent dans la base de données (REAAL3B) mais ne sont pas/plus gérés actuellement
     * codeTraitement = "";// ZGTCOD
     * dateLiquidation = "";// ZGDDAL
     */

    @Override
    public final RECodeApplicationProvider getCodeApplicationProvider() {
        return RETypeAnnonce9emeRevision.ANNONCE_PONCTUELLE_SUBSEQUENTE;
    }

    @Override
    public REPrefixPourReferenceInterneCaisseProvider getPrefixPourReferenceInterneCaisseProvider() {
        return REPrefixPourReferenceInterneCaisse.ANNONCE_PONCTUELLE;
    }

    /**
     * @return the montantRenteOrdinaireRemplace
     */
    public final Integer getMontantRenteOrdinaireRemplace() {
        return montantRenteOrdinaireRemplace;
    }

    /**
     * @param montantRenteOrdinaireRemplace the montantRenteOrdinaireRemplace to set
     */
    public final void setMontantRenteOrdinaireRemplace(Integer montantRenteOrdinaireRemplace) {
        this.montantRenteOrdinaireRemplace = montantRenteOrdinaireRemplace;
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
