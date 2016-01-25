package globaz.corvus.annonce.reader;

import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Classe abstraite possédant des méthodes utilitaires de base pour la lecture des champs de BEntity
 * 
 * @author lga
 * 
 */
public abstract class REAbstractBEntityValueReader {

    /**
     * Convertis une date sous forme de String en JADate</br>
     * </br>
     * Formats supportés :</br>
     * dd.mm.yyyy</br>
     * ddmmyyyy</br>
     * mm.yyyy</br>
     * mmyyyy</br>
     * yyyy</br>
     * 
     * @param date la chaîne de caractère à convertir en JADate
     * @throws IllegalArgumentException Si la chaîne de caractères ne correspond pas à un format de date valide
     * @return Une JADate si la chaîne de caractères n'est pas vide et que le format le permet, <code>null</code> si la
     *         chaîne est <code>null</code> ou vide. Dans tous les autres cas une <code>IllegalArgumentException</code>
     *         sera lancée
     */
    protected JADate convertToJADate(String date) throws IllegalArgumentException {
        // FIXME WTF retournais une new JADate()
        JADate d = null;
        if (!JadeStringUtil.isBlank(date)) {
            try {
                d = new JADate(date);
            } catch (JAException e) {
                throw new IllegalArgumentException("Wrong date fomrmat [" + date + "]. Can not convert it to JADate : "
                        + e.toString(), e);
            }
        }
        return d;
    }

    /**
     * Format booléen selon les règles suivantes :</br>
     * - Retourne <code>null</code> si <code>value</code> est null ou une chaîne vide</br>
     * - Retourne <code>Boolean.FALSE</code> si <code>value</code> est égal à '0'</br>
     * - Retourne <code>Boolean.TRUE</code> si <code>value</code> est égal à '1'</br>
     * - Retourne <code>null</code> dans tous les autres cas (chaîne trop longue, caractère différents de '0' ou '1',
     * etc)</br>
     * 
     * 
     * @param value la chaîne de caractères à convertir en Boolean
     * @return <code>Boolean.TRUE</code> si <code>value</code> est égal à '1', <code>Boolean.FALSE</code> si
     *         <code>value</code> est égal à '0', <strong><code>null</code> dans tous les autres cas</strong>
     */
    protected Boolean convertToBoolean(String value) {
        Boolean result = null;
        if (!JadeStringUtil.isBlank(value)) {
            if ("0".equals(value)) {
                result = false;
            } else if ("1".equals(value)) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Convertis une chaîne de caractères en nombre entier Integer si la valeur le permet
     * 
     * @param intValue La valeur entière sous forme de chaîne de caractères (ne doit pas contenir de point)
     * @return un Integer si la chaîne de caractère <code>intValue</code> le permet sinon <code>null</code> dans tous
     *         les autre cas
     */
    protected Integer convertToInteger(String intValue) {
        Integer value = null;
        if (JadeNumericUtil.isInteger(intValue)) {
            value = Integer.valueOf(intValue);
        }
        return value;
    }

}
