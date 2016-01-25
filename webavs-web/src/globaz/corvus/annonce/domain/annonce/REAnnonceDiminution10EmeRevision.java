package globaz.corvus.annonce.domain.annonce;

import globaz.corvus.annonce.RECodeApplicationProvider;
import globaz.corvus.annonce.REPrefixPourReferenceInterneCaisse;
import globaz.corvus.annonce.REPrefixPourReferenceInterneCaisseProvider;
import globaz.corvus.annonce.RETypeAnnonce10emeRevision;
import globaz.corvus.annonce.domain.REAnnonce2B;

/**
 * Représente une annonce d'augmentation 10ème révision (annonce 44)
 * 
 * @author lga
 * 
 */
public final class REAnnonceDiminution10EmeRevision extends REAnnonce2B {

    @Override
    public final RECodeApplicationProvider getCodeApplicationProvider() {
        return RETypeAnnonce10emeRevision.ANNONCE_DIMINUTION;
    }

    @Override
    public REPrefixPourReferenceInterneCaisseProvider getPrefixPourReferenceInterneCaisseProvider() {
        return REPrefixPourReferenceInterneCaisse.ANNONCE_DIMINUTION;
    }

}
