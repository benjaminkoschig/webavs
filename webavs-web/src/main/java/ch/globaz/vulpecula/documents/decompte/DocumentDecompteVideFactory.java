package ch.globaz.vulpecula.documents.decompte;

/**
 * @author Arnaud Geiser (AGE) | Créé le 5 mai 2014
 * 
 */
public final class DocumentDecompteVideFactory {

    private DocumentDecompteVideFactory() {
        throw new UnsupportedOperationException();
    }

    public static DocumentDecompteVide getTypeDocumentDecompteVide(final DecompteContainer decompteContainer,
            boolean isPrintingFromEbu) throws Exception {

        switch (decompteContainer.getDecompte().getType()) {
            case COMPLEMENTAIRE:
                return new DocumentDecompteVideComplementaire(decompteContainer, isPrintingFromEbu);
            default:
                return new DocumentDecompteVidePeriodique(decompteContainer, isPrintingFromEbu);
        }
    }
}
