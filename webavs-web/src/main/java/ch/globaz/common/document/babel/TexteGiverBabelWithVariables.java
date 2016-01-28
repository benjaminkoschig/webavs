package ch.globaz.common.document.babel;

import globaz.babel.api.ICTDocument;
import globaz.globall.db.BSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import ch.globaz.common.document.TextGiver;
import ch.globaz.common.document.VariablesTemplate;
import ch.globaz.jade.business.models.Langues;

public class TexteGiverBabelWithVariables implements TextGiver<BabelTextDefinition> {
    private TextGiver<BabelTextDefinition> textGiver;
    private Map<String, VariablesTemplate> variablesTemplate = new ConcurrentHashMap<String, VariablesTemplate>();

    public TexteGiverBabelWithVariables(TextGiver<BabelTextDefinition> textGiverBabel) {
        textGiver = textGiverBabel;
    }

    public TexteGiverBabelWithVariables(ICTDocument document, BSession session) {
        textGiver = new TextGiverBabel(document, session);
    }

    public TexteGiverBabelWithVariables(ICTDocument document) {
        textGiver = new TextGiverBabel(document);
    }

    public TexteGiverBabelWithVariables(Map<Langues, ICTDocument> map, BSession session) {
        textGiver = new TextGiverBabel(map, session);
    }

    @Override
    public String resolveText(BabelTextDefinition definition) {
        return textGiver.resolveText(definition);
    }

    @Override
    public String resolveText(BabelTextDefinition definition, String langue) {
        return textGiver.resolveText(definition, langue);
    }

    @Override
    public String resolveText(BabelTextDefinition definition, Langues langues) {
        return textGiver.resolveText(definition, langues);
    }

    public String resolveText(BabelTextDefinition definition, Map<String, String> variables) {
        return render(textGiver.resolveText(definition), variables);
    }

    public String resolveText(BabelTextDefinition definition, String langue, Map<String, String> variables) {
        return render(textGiver.resolveText(definition, langue), variables);
    }

    public String resolveText(BabelTextDefinition definition, Langues langues, Map<String, String> variables) {
        return render(textGiver.resolveText(definition, langues), variables);
    }

    @Override
    public void setLangue(String langue) {
        textGiver.setLangue(langue);
    }

    private String render(String template, Map<String, String> variables) {
        if (!variablesTemplate.containsKey(template)) {
            VariablesTemplate variablesTemplate = new VariablesTemplate(template);
            this.variablesTemplate.put(template, variablesTemplate);
        }
        return variablesTemplate.get(template).render(variables);
    }

}
