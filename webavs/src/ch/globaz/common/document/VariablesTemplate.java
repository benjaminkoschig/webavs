package ch.globaz.common.document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ch.globaz.common.domaine.Checkers;

/**
 * 
 * Le but de cette class et de pouvoir remplacer des variables dans une chains de caractères(template)
 * Le format par défaut est : {MA_VARIALBE} il est possible de changer ce format
 * 
 * @author dma
 * 
 */
public class VariablesTemplate {

    private final String template;
    private final List<String> variablesInTemplate;
    private static final String defaultFormat = "{?}";
    private String formatVariable = defaultFormat;

    /**
     * @param template: Chain de caractère que l'on vas remplacer avec des variables.
     *            Exemple: {MA_VARIABLE} - {MA_VARIABLE2}
     */
    public VariablesTemplate(final String template) {
        Checkers.checkNotNull(template, "template");
        this.template = template;
        variablesInTemplate = split(this.template);
    }

    /**
     * @param template: Chain de caractères que l'on vas remplacer avec des variables.
     *            Exemple: {MA_VARIABLE} - {MA_VARIABLE2}
     * @param formatVariable: format de la définition de variable. Le "?" est le caractère de de substitution.
     *            Exemple: {?}
     */
    public VariablesTemplate(final String template, final String formatVariable) {
        Checkers.checkNotNull(template, "template");
        this.template = template;
        this.formatVariable = formatVariable;
        variablesInTemplate = split(this.template);
    }

    /**
     * Réalise le remplacement des variables du template
     * 
     * @param map: La key correspond au nom de la variable et la value à la valeur qui sera remplacer dans le template
     * @throws RuntimeException si une variable n'existe pas
     * @return Chain de cratères
     */
    public String render(final Map<String, String> map) {
        Checkers.checkNotNull(map, "maping variable");
        String result = template;
        if (map.size() > 0) {
            List<String> varialbesNoDeclared = resoleveVarialbesNoDeclared(map);
            if (varialbesNoDeclared.size() > 0) {
                String s = join(varialbesNoDeclared);
                throw new RuntimeException("Variables not defined -> " + s);
            }
            for (Entry<String, String> entry : map.entrySet()) {
                result = result.replace(formatVariable.replace("?", entry.getKey()), entry.getValue().trim());
            }
        }
        return result;
    }

    public static String render(final String template, final Map<String, String> map) {
        String result = template;
        if (map != null) {
            for (Entry<String, String> entry : map.entrySet()) {
                result = result.replace(defaultFormat.replace("?", entry.getKey()), entry.getValue().trim());
            }
        }
        return result;
    }

    public String availableVaraibles() {
        return join(variablesInTemplate);
    }

    private String join(List<String> varialbesNoDeclared) {
        String s = "";
        for (String varaible : varialbesNoDeclared) {
            s = s + "," + varaible;
        }
        return s.replaceFirst(",", "");
    }

    List<String> resoleveVarialbesNoDeclared(Map<String, String> map) {
        List<String> notDeclaredVariables = new ArrayList<String>();
        for (String variable : variablesInTemplate) {
            if (!map.containsKey(variable)) {
                notDeclaredVariables.add(variable);
            }
        }
        return notDeclaredVariables;
    }

    List<String> split(String template) {

        String format = escapeChars("{}[]$", formatVariable);

        Pattern pattern = Pattern.compile(format.replace("?", "(\\w*)"));
        Matcher matcher = pattern.matcher(template);
        List<String> variables = new ArrayList<String>();
        while (matcher.find()) {
            variables.add(matcher.group(1));
        }
        return variables;
    }

    String escapeChars(String specialeChar, String replacement) {

        char[] escapeChars = specialeChar.toCharArray();
        String format = replacement;
        for (char c : escapeChars) {
            format = format.replace("" + c, "\\" + c);
        }
        return format;
    }
}
