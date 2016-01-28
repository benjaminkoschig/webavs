package ch.globaz.pyxis.domaine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import ch.globaz.common.domaine.Checkers;

/**
 * <p>
 * Objet de domaine représentant un nouveau numéro de sécurité sociale suisse (NNSS)<br />
 * Dès le moment où cette objet a pu être instancié, cela signifie que le NSS contenu est valide, et l'objet restera
 * inchangé quoiqu'il arrive (immutabilité)
 * </p>
 * <p>
 * <strong>Attention!</strong> : cette objet ne gère pas les anciens numéros AVS à 11 chiffres.
 * </p>
 * <p>
 * Pour plus d'informations quant à la validité d'un NSS : veuillez vous référez à la doc de la méthode
 * {@link #validate(String)}
 * </p>
 */
public class NumeroSecuriteSociale implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String REGEX_NSS = "756\\.[0-9]{4}\\.[0-9]{4}\\.[0-9]{2}";

    private static void checkFormat(final String nss) {
        if (!nss.matches(NumeroSecuriteSociale.REGEX_NSS)) {
            throw new IllegalArgumentException("NSS must be formated like 756.xxxx.xxxx.xx where x are digits");
        }
    }

    /**
     * <p>
     * Vérifie si le NSS rempli les conditions pour la norme de somme de contrôle (norme EAN13) <br />
     * Voir <a href="https://fr.wikipedia.org/wiki/EAN_13#Calcul_de_la_cl.C3.A9_de_contr.C3.B4le_EAN_13">EAN13 sur
     * Wikipédia</a> et <a href="http://barcode-coder.com/fr/specification-ean-13-102.html">Une page avec des exemples
     * interactifs</a>
     * </p>
     * 
     * @param nss
     */
    private static void checkSumEAN13(final String nss) {

        LinkedList<Integer> nssCharByChar = NumeroSecuriteSociale.splitNssCharByChar(nss);
        int checkSum = nssCharByChar.pollLast();

        int sumOdd = NumeroSecuriteSociale.sum(NumeroSecuriteSociale.oddFilter(nssCharByChar));
        int sumEven = NumeroSecuriteSociale.sum(NumeroSecuriteSociale.evenFilter(nssCharByChar));

        if (checkSum != ((10 - (((3 * sumOdd) + sumEven) % 10)) % 10)) {
            throw new IllegalArgumentException("Checksum error");
        }
    }

    private static List<Integer> evenFilter(final List<Integer> nssCharByChar) {
        return NumeroSecuriteSociale.filter(nssCharByChar, 0);
    }

    private static List<Integer> filter(final List<Integer> list, final int expectedIndexModuloResult) {
        List<Integer> filteredList = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            if ((i % 2) == expectedIndexModuloResult) {
                filteredList.add(list.get(i));
            }
        }
        return filteredList;
    }

    private static List<Integer> oddFilter(final List<Integer> nssCharByChar) {
        return NumeroSecuriteSociale.filter(nssCharByChar, 1);
    }

    private static LinkedList<Integer> splitNssCharByChar(final String nss) {
        String nssSansLesPoinds = nss.replaceAll("\\.", "");

        LinkedList<Integer> nssCharByChar = new LinkedList<Integer>();

        for (Character aChar : nssSansLesPoinds.toCharArray()) {
            nssCharByChar.add(Integer.valueOf(aChar.toString()));
        }

        return nssCharByChar;
    }

    private static Integer sum(final List<Integer> list) {
        int sum = 0;

        for (Integer oneInt : list) {
            sum += oneInt;
        }

        return sum;
    }

    /**
     * <p>
     * Contrôle la validité du numéro de sécurité sociale passé en paramètre.
     * </p>
     * <p>
     * Un numéro est valide si :
     * <ul>
     * <li>il est sous la forme 756.xxxx.xxxx.xx où x sont des numéros.</li>
     * <li>le dernier numéro, qui est la somme de contrôle, doit être égal à
     * 
     * <pre>
     * ( 10 - ( (3 * somme des nombre à index impaires + somme des nombres à index paires) % 10 ) ) % 10
     * </pre>
     * 
     * </li>
     * </ul>
     * </p>
     * 
     * @param nss
     *            un numéro de sécurité de sociale dont on veut valider la forme
     * @throws IllegalArgumentException
     *             si une des conditions citées plus haut n'est pas remplie
     * @see #checkSumEAN13(String)
     */
    public static void validate(final String nss) throws IllegalArgumentException {
        NumeroSecuriteSociale.checkFormat(nss);
        NumeroSecuriteSociale.checkSumEAN13(nss);
    }

    private final String nss;

    /**
     * Vérifie la validité du numéro de sécurité sociale passé en paramètre avant de construire cet objet.<br/>
     * Un numéro est valide si :
     * <ul>
     * <li>il est sous la forme 756.xxxx.xxxx.xx où x sont des numéros.</li>
     * <li>le dernier numéro, qui est la somme de contrôle, doit être égal à
     * 
     * <pre>
     * ( 10 - ( (3 * somme des nombre à index impaires + somme des nombres à index paires) % 10 ) ) % 10
     * </pre>
     * 
     * @param nss
     * @throws IllegalArgumentException
     *             si une des conditions citées plus haut n'est pas remplie
     */
    public NumeroSecuriteSociale(final String nss) {
        Checkers.checkNotNull(nss, "nss");
        NumeroSecuriteSociale.validate(nss);

        this.nss = nss;
    }

    @Override
    public String toString() {
        return nss;
    }
}
