/*
 * Créé le 7 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.tools;

import globaz.jade.client.util.JadeStringUtil;
import java.text.CharacterIterator;
import java.text.MessageFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class SFStringUtils {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Enlève tous les caractères autres que les digits dans la chaine 'chaine'
     * 
     * @param chaine
     *            la chaine a traiter
     * 
     * @return la chaine 'chaine' sans tous les caractères différents que [0-9]
     */
    public static final String extraireDigits(String chaine) {
        StringBuffer retValue = new StringBuffer();

        for (int idChar = 0; idChar < chaine.length(); ++idChar) {
            char c = chaine.charAt(idChar);

            if (Character.isDigit(c)) {
                retValue.append(c);
            }
        }

        return retValue.toString();
    }

    /**
     * Remplacement d'un tableau de caractères et formattage
     * 
     * @param message
     * @param args
     * @return
     */
    public static String formatMessage(StringBuffer message, Object[] args) {

        // doubler les guillemets simples si necessaire
        for (int idChar = 0; idChar < message.length(); ++idChar) {
            if ((message.charAt(idChar) == '\'')
                    && ((idChar == (message.length() - 1)) || (message.charAt(idChar + 1) != '\''))) {
                message.insert(idChar, '\'');
                ++idChar;
            }
        }

        // remplacer les arguments null par chaine vide
        for (int idArg = 0; idArg < args.length; ++idArg) {
            if (args[idArg] == null) {
                args[idArg] = "";
            }
        }
        // remplacer et retourner
        return MessageFormat.format(message.toString(), args);
    }

    /**
     * Remplacement d'une chaîne de caractères et formattage
     * 
     * @param message
     * @param args
     * @return
     */
    public static String formatMessage(StringBuffer message, String args) {

        // doubler les guillemets simples si necessaire
        for (int idChar = 0; idChar < message.length(); ++idChar) {
            if ((message.charAt(idChar) == '\'')
                    && ((idChar == (message.length() - 1)) || (message.charAt(idChar + 1) != '\''))) {
                message.insert(idChar, '\'');
                ++idChar;
            }
        }

        // remplacer les arguments null par chaine vide
        if (args == null) {
            args = "";
        }

        Object[] rempl = new Object[] { args };

        // remplacer et retourner
        return MessageFormat.format(message.toString(), rempl);
    }

    /**
     * renvoie au maximum 'maxChars' de la chaine 'chaine' suivis de '... (indice)'
     * 
     * @param chaine
     *            DOCUMENT ME!
     * @param maxChars
     *            DOCUMENT ME!
     * @param indice
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String indicer(String chaine, int maxChars, int indice) {
        if (chaine.length() > maxChars) {
            chaine = chaine.substring(0, maxChars) + "...";
        }

        return chaine.concat(" (" + indice + ")");
    }

    /**
     * Remplace une chaîne par une autre à l'intérieur d'une chaine de caractères
     * 
     * @return le résultat du remplacement
     * @param baseString
     *            la chaine contenant le texte à traiter
     * @param textToReplace
     *            le texte à remplacer dans la chaine de base
     * @param replaceWith
     *            le texte de remplacement
     */
    public final static String replaceString(String baseString, String textToReplace, String replaceWith) {
        if ((JadeStringUtil.isEmpty(baseString)) || (JadeStringUtil.isEmpty(textToReplace)) || (replaceWith == null)) {
            return baseString;
        }
        StringBuffer buffer = new StringBuffer();
        int index = -1;
        while ((index = baseString.indexOf(textToReplace)) != -1) {
            buffer.append(baseString.substring(0, index));
            buffer.append(replaceWith);
            baseString = baseString.substring(index + textToReplace.length());
        }
        buffer.append(baseString);
        return buffer.toString();
    }

    /**
     * <p>
     * remplace la sous chaine entre <code>start</code> et <code>end</code> par <code>replaceBy</code>
     * </p>
     * 
     * <p>
     * exemple : replaceStringIn("12345", 1 , 2, "0") renvoie 10345
     * </p>
     * 
     * @param s
     *            la String de départ
     * @param start
     *            la position de départ
     * @param end
     *            la position de fin
     * @param replaceBy
     *            la String par laquelle on doit remplacer
     * 
     * @return une String modifiée
     */
    public static final String replaceStringIn(String s, int start, int end, String replaceBy) {
        StringBuffer buffer = new StringBuffer(s);
        buffer.replace(start, end, replaceBy);

        return buffer.toString();
    }

    /**
     * "coupe" une String aux endroits définis par le char. split("trucMachinChose",'h') -> {"trucMac","inC","ose"}
     * 
     * @param s
     *            DOCUMENT ME!
     * @param c
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String[] split(String s, char c) {
        List strings = new ArrayList();
        StringBuffer tmp = new StringBuffer();

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                if (tmp.length() != 0) {
                    strings.add(tmp.toString());
                    tmp.delete(0, tmp.length());
                }
            } else {
                tmp.append(s.charAt(i));
            }
        }

        if (tmp.length() != 0) {
            strings.add(tmp.toString());
        }

        Object[] trucs = strings.toArray();

        String[] result = new String[trucs.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = (String) trucs[i];
        }

        return result;
    }

    /**
     * Pour chacun des mots de la chaine string, mets la première lettre en majuscule et les suivantes en minuscules.
     * 
     * @param string
     *            une chaine de caractère non nulle.
     * 
     * @return une chaine de même longueur avec la casse modifiée.
     */
    public static final String toWordsFirstLetterUppercase(String string) {
        if (string.length() <= 1) {
            return string.toUpperCase();
        }

        // changement de la casse
        StringBuffer tampon = new StringBuffer(string.length());
        StringCharacterIterator chars = new StringCharacterIterator(string);
        char c = chars.first();
        boolean upperCase = true;

        do {
            if (Character.isLetter(c)) {
                if (upperCase) {
                    c = Character.toUpperCase(c);
                    upperCase = false;
                } else {
                    c = Character.toLowerCase(c);
                }
            } else {
                upperCase = true;
            }

            tampon.append(c);
        } while ((c = chars.next()) != CharacterIterator.DONE);

        return tampon.toString();
    }

    /**
     * @param in
     * @return
     */
    public static String upperCaseWithoutSpecialChars(String in) {
        String out = "";
        if (!JadeStringUtil.isEmpty(in)) {
            out = JadeStringUtil.convertSpecialChars(in);
            out = out.toUpperCase();
        }
        return out;
    }
}
