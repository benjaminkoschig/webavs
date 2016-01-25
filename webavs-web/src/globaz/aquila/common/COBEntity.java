package globaz.aquila.common;

import globaz.aquila.util.COBeanUtils;
import globaz.globall.db.BEntity;

/**
 * Classe abstraite parente de toutes les entités du projet aquila
 * 
 * @see globaz.globall.db.BEntity
 * @author Pascal Lovy, 07-oct-2004
 */
public abstract class COBEntity extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Retourne une représentation textuelle du contenu de l'entité sur une ligne.
     * 
     * @return La représentation textuelle de l'entité
     */
    public String dump() {
        return COBeanUtils.dumpBean(this, COBEntity.class, false, false, 0);
    }

    /**
     * Retourne une représentation textuelle du contenu de l'entité sur plusieurs lignes.
     * 
     * @return La représentation textuelle de l'entité
     */
    public String dumpMultiLine() {
        return COBeanUtils.dumpBean(this, COBEntity.class, true, false, 0);
    }

}
