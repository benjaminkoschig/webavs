package ch.globaz.pyxis.domaine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import ch.globaz.common.domaine.Checkers;

/**
 * <p>
 * Objet de domaine repr�sentant un nouveau num�ro de s�curit� sociale suisse (NNSS)<br />
 * D�s le moment o� cette objet a pu �tre instanci�, cela signifie que le NSS contenu est valide, et l'objet restera
 * inchang� quoiqu'il arrive (immutabilit�)
 * </p>
 * <p>
 * <strong>Attention!</strong> : cette objet ne g�re pas les anciens num�ros AVS � 11 chiffres.
 * </p>
 * <p>
 * Pour plus d'informations quant � la validit� d'un NSS : veuillez vous r�f�rez � la doc de la m�thode
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
     * V�rifie si le NSS rempli les conditions pour la norme de somme de contr�le (norme EAN13) <br />
     * Voir <a href="https://fr.wikipedia.org/wiki/EAN_13#Calcul_de_la_cl.C3.A9_de_contr.C3.B4le_EAN_13">EAN13 sur
     * Wikip�dia</a> et <a href="http://barcode-coder.com/fr/specification-ean-13-102.html">Une page avec des exemples
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
     * Contr�le la validit� du num�ro de s�curit� sociale pass� en param�tre.
     * </p>
     * <p>
     * Un num�ro est valide si :
     * <ul>
     * <li>il est sous la forme 756.xxxx.xxxx.xx o� x sont des num�ros.</li>
     * <li>le dernier num�ro, qui est la somme de contr�le, doit �tre �gal �
     * 
     * <pre>
     * ( 10 - ( (3 * somme des nombre � index impaires + somme des nombres � index paires) % 10 ) ) % 10
     * </pre>
     * 
     * </li>
     * </ul>
     * </p>
     * 
     * @param nss
     *            un num�ro de s�curit� de sociale dont on veut valider la forme
     * @throws IllegalArgumentException
     *             si une des conditions cit�es plus haut n'est pas remplie
     * @see #checkSumEAN13(String)
     */
    public static void validate(final String nss) throws IllegalArgumentException {
        NumeroSecuriteSociale.checkFormat(nss);
        NumeroSecuriteSociale.checkSumEAN13(nss);
    }

    private final String nss;

    /**
     * V�rifie la validit� du num�ro de s�curit� sociale pass� en param�tre avant de construire cet objet.<br/>
     * Un num�ro est valide si :
     * <ul>
     * <li>il est sous la forme 756.xxxx.xxxx.xx o� x sont des num�ros.</li>
     * <li>le dernier num�ro, qui est la somme de contr�le, doit �tre �gal �
     * 
     * <pre>
     * ( 10 - ( (3 * somme des nombre � index impaires + somme des nombres � index paires) % 10 ) ) % 10
     * </pre>
     * 
     * @param nss
     * @throws IllegalArgumentException
     *             si une des conditions cit�es plus haut n'est pas remplie
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
