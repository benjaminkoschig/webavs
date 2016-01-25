package ch.globaz.pegasus.businessimpl.utils;

import globaz.globall.util.JACalendar;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class PegasusUtil {

    /**
     * Convertit le nom et prénom dans un format "NOM Prénom", uniformisant la capitalisation.
     * 
     * @param nom
     *            Le nom de la personne
     * @param prenom
     *            Le prénom de la personne
     * @return La designation formattée, ou null si l'un des champs est null.
     */
    public static String formatNomPrenom(String nom, String prenom) {
        if ((nom == null) || (prenom == null)) {
            return null;
        }

        return nom.toUpperCase() + " " + PegasusUtil.toProperCase(prenom);
    }

    /**
     * Formate le nom et prénom d'un tiers
     * 
     * @param tiers
     *            Personne tiers
     * @return La designation formattée, ou null si le tiers est incomplet ou null.
     */
    public static String formatNomPrenom(TiersSimpleModel tiers) {
        if (tiers == null) {
            return null;
        }
        return PegasusUtil.formatNomPrenom(tiers.getDesignation1(), tiers.getDesignation2());
    }

    /**
     * Cherche l'adresse d'un tiers sur plusieurs sources d'adresse. Respectant un ordre de recherche, la première
     * adresse trouvée sera retournée.
     * 
     * @param idTiers
     *            Le tiers à chercher
     * @param domaine
     * @param listOrderType
     *            L'ordre de recherche
     * @return L'adresse formatée de la personne, ou un texte vide.
     * @throws JadeApplicationServiceNotAvailableException
     *             En cas d'erreur de service
     * @throws JadePersistenceException
     *             En cas d'erreur de persistance
     * @throws JadeApplicationException
     *             En cas d'erreur
     */
    public static AdresseTiersDetail getAdresseCascadeByType(String idTiers, String domaine,
            ArrayList<String> listOrderType) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {

        AdresseTiersDetail adresseTiersDetail = null;

        for (int i = 0; i < listOrderType.size(); i++) {
            adresseTiersDetail = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(idTiers, Boolean.TRUE,
                    JACalendar.todayJJsMMsAAAA(), domaine, listOrderType.get(i), null);
            if (adresseTiersDetail.getAdresseFormate() != null) {
                break;
            }
        }

        if ((adresseTiersDetail != null) && (adresseTiersDetail.getFields() == null)) {
            adresseTiersDetail.setFields(new HashMap<String, String>());
        }

        return adresseTiersDetail;

    }

    /**
     * concatène une Liste de String en un seul string, avec un séparateur entre chaque élément.<br>
     * Cette méthode est inspirée de la méthode join des Array en Javascript.
     * 
     * @param words
     *            Liste d'éléments à concaténer.
     * @param sepparator
     *            Texte inséré entre chaque élément.
     * @return String du tableau concaténé, ou null si le tableau est null.
     */
    public final static String joinArray(final Collection<String> words, String sepparator) {
        if ((words == null)) {
            return null;
        } else if (words.isEmpty()) {
            return "";
        }

        if (sepparator == null) {
            sepparator = "";
        }
        final Iterator<String> it = words.iterator();
        final StringBuilder result = new StringBuilder(it.next());
        for (; it.hasNext();) {
            result.append(sepparator);
            result.append(it.next());

        }

        return result.toString();
    }

    /**
     * concatène un tableau de String en un seul string, avec un séparateur entre chaque élément.<br>
     * Cette méthode est inspirée de la méthode join des Array en Javascript.
     * 
     * @param words
     *            Tableau d'éléments à concaténer.
     * @param sepparator
     *            Texte inséré entre chaque élément.
     * @return String du tableau concaténé, ou null si le tableau est null.
     */
    public final static String joinArray(final String[] words, String sepparator) {
        if ((words == null)) {
            return null;
        } else if (words.length == 0) {
            return "";
        }

        if (sepparator == null) {
            sepparator = "";
        }
        final StringBuilder result = new StringBuilder(words[0]);
        for (int i = 1; i < words.length; i++) {
            result.append(sepparator);
            result.append(words[i]);
        }

        return result.toString();
    }

    /**
     * Adapte les majuscules d'un texte selon le format standard des noms propres (1ère lettre de chaque mot en
     * majuscule).<br />
     * <br />
     * Exemple:<br />
     * <code>"alain de st-antoine"</code> devient <code>"Alain De St-Antoine"</code>
     * 
     * @param input
     *            Texte à convertir
     * @return Texte formaté, ou null si l'input est null.
     */
    public static String toProperCase(String input) {
        if (input == null) {
            return null;
        }

        input = input.toLowerCase();
        // A pattern for all (UNICODE-) lower case characters preceded by a word boundary
        Pattern p = Pattern.compile("\\b([\\w])", Pattern.UNICODE_CASE);
        Matcher m = p.matcher(input);
        StringBuffer sb = new StringBuffer(input.length());
        while (m.find()) {
            m.appendReplacement(sb, m.group(1).toUpperCase());
        }
        m.appendTail(sb);
        return sb.toString();
    }

}
