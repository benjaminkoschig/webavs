package ch.globaz.param.business.vo;

import globaz.jade.client.util.JadeStringUtil;

/**
 * Value Object représentant la clé (nom) d'un paramètre métier AF
 * 
 * @author gmo
 * 
 */
public class KeyNameParameter {
    private String keyName = null;

    public KeyNameParameter(String newKeyName) {
        if (JadeStringUtil.isEmpty(newKeyName)) {
            throw new IllegalArgumentException("newKeyName parameter passed is empty");
        }
        keyName = newKeyName;
    }

    public String getKeyName() {
        return keyName;
    }

    @Override
    public String toString() {
        return keyName;
    }

}
