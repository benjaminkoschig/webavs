package ch.globaz.vulpecula.documents.decompte;

import ch.globaz.vulpecula.domain.models.decompte.Decompte;

/**
 * @author Arnaud Geiser (AGE) | Créé le 5 mai 2014
 * 
 */
public final class DocumentDecompteVideFactory {

    private DocumentDecompteVideFactory() {
        throw new UnsupportedOperationException();
    }

    public static DocumentDecompteVide getTypeDocumentDecompteVide(final DecompteContainer decompteContainer)
            throws Exception {
        Decompte decompte = decompteContainer.getDecompte();

        if (decompte.isReceptionnable()) {
            switch (decompteContainer.getDecompte().getType()) {
                case COMPLEMENTAIRE:
                    return new DocumentDecompteVideComplementaire(decompteContainer);
                case PERIODIQUE:
                    return new DocumentDecompteVidePeriodique(decompteContainer);
                default:
                    return new DocumentDecompteVidePeriodique(decompteContainer);
            }
        } else {
            return new DocumentDecompteVidePeriodique(decompteContainer);
        }
    }
}
