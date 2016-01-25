package globaz.osiris.db.access.recouvrement;

import globaz.globall.db.BEntity;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Classe abstraite parente de toutes les entités du projet osiris.
 * 
 * @see globaz.globall.db.BEntity
 * @author Pascal Lovy, 25-may-2005
 */
public abstract class CABEntity extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Retourne une entité en fonction de son id. Les erreurs éventuelles sont directement ajoutées à la transaction.
     * 
     * @param transaction
     *            La transaction courante
     * @param id
     *            L'id de l'entité
     * @param type
     *            Le type de l'entité
     * @return L'entité recherchée, null si inexistante
     */
    protected BEntity retrieveEntityById(BTransaction transaction, String id, Class type) {
        BEntity result = null;
        if (!JadeStringUtil.isBlank(id)) {
            try {
                result = (BEntity) type.newInstance();
                result.setSession(getSession());
                result.setId(id);
                result.retrieve(transaction);
            } catch (Exception e) {
                _addError(transaction, e.toString());
                return null;
            }
            if (result.isNew()) {
                _addError(transaction, "Objet inexistant! id=" + id + " class=" + type.getName());
                return null;
            }
        }
        return result;
    }

    /**
     * Retourne une entité en fonction de son id uniquement si nécessaire. Les erreurs éventuelles sont directement
     * ajoutées à la transaction.
     * 
     * @param transaction
     *            La transaction courante
     * @param id
     *            L'id de l'entité
     * @param type
     *            Le type de l'entité
     * @return L'entité recherchée, null si inexistante
     */
    protected BEntity retrieveEntityByIdIfNeeded(BTransaction transaction, String id, Class type, BEntity currentValue) {
        if ((currentValue == null) || !currentValue.getId().equals(id)) {
            return retrieveEntityById(transaction, id, type);
        } else {
            return currentValue;
        }
    }

}
