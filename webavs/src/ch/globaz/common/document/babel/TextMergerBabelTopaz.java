package ch.globaz.common.document.babel;

import globaz.babel.api.ICTDocument;
import ch.globaz.common.document.TextGiver;
import ch.globaz.common.document.TextMerger;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.topaz.datajuicer.DocumentData;

public class TextMergerBabelTopaz implements TextMerger<BabelTextDefinition> {

    private final TextGiver<BabelTextDefinition> textGiver;
    private final DocumentData documentData;

    public TextMergerBabelTopaz(ICTDocument document, DocumentData documentData) {
        Checkers.checkNotNull(document, "document");
        Checkers.checkNotNull(documentData, "documentData");
        textGiver = new TextGiverBabel(document);
        this.documentData = documentData;
    }

    public TextMergerBabelTopaz(TextGiver<BabelTextDefinition> textGiver, DocumentData documentData) {
        Checkers.checkNotNull(textGiver, "textGiver");
        Checkers.checkNotNull(documentData, "documentData");
        this.textGiver = textGiver;
        this.documentData = documentData;
    }

    @Override
    public void addTextToDocument(BabelTextDefinition definition) {
        this.addTextToDocument(definition, textGiver.resolveText(definition));
    }

    @Override
    public void addTextToDocument(BabelTextDefinition definition, String value) {
        documentData.addData(definition.getKey(), value);
    }

    @Override
    public TextGiver<BabelTextDefinition> getTextGiver() {
        return textGiver;
    }
}
