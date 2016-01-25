package globaz.corvus.annonce.reader;

import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Classe abstraite poss�dant des m�thodes utilitaires de base pour la lecture des champs de BEntity
 * 
 * @author lga
 * 
 */
public abstract class REAbstractBEntityValueReader {

    /**
     * Convertis une date sous forme de String en JADate</br>
     * </br>
     * Formats support�s :</br>
     * dd.mm.yyyy</br>
     * ddmmyyyy</br>
     * mm.yyyy</br>
     * mmyyyy</br>
     * yyyy</br>
     * 
     * @param date la cha�ne de caract�re � convertir en JADate
     * @throws IllegalArgumentException Si la cha�ne de caract�res ne correspond pas � un format de date valide
     * @return Une JADate si la cha�ne de caract�res n'est pas vide et que le format le permet, <code>null</code> si la
     *         cha�ne est <code>null</code> ou vide. Dans tous les autres cas une <code>IllegalArgumentException</code>
     *         sera lanc�e
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
     * Format bool�en selon les r�gles suivantes :</br>
     * - Retourne <code>null</code> si <code>value</code> est null ou une cha�ne vide</br>
     * - Retourne <code>Boolean.FALSE</code> si <code>value</code> est �gal � '0'</br>
     * - Retourne <code>Boolean.TRUE</code> si <code>value</code> est �gal � '1'</br>
     * - Retourne <code>null</code> dans tous les autres cas (cha�ne trop longue, caract�re diff�rents de '0' ou '1',
     * etc)</br>
     * 
     * 
     * @param value la cha�ne de caract�res � convertir en Boolean
     * @return <code>Boolean.TRUE</code> si <code>value</code> est �gal � '1', <code>Boolean.FALSE</code> si
     *         <code>value</code> est �gal � '0', <strong><code>null</code> dans tous les autres cas</strong>
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
     * Convertis une cha�ne de caract�res en nombre entier Integer si la valeur le permet
     * 
     * @param intValue La valeur enti�re sous forme de cha�ne de caract�res (ne doit pas contenir de point)
     * @return un Integer si la cha�ne de caract�re <code>intValue</code> le permet sinon <code>null</code> dans tous
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
