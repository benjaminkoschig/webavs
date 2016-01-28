package globaz.aquila.common;

import globaz.aquila.util.COBeanUtils;
import globaz.globall.db.BEntity;

/**
 * Classe abstraite parente de toutes les entit�s du projet aquila
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
     * Retourne une repr�sentation textuelle du contenu de l'entit� sur une ligne.
     * 
     * @return La repr�sentation textuelle de l'entit�
     */
    public String dump() {
        return COBeanUtils.dumpBean(this, COBEntity.class, false, false, 0);
    }

    /**
     * Retourne une repr�sentation textuelle du contenu de l'entit� sur plusieurs lignes.
     * 
     * @return La repr�sentation textuelle de l'entit�
     */
    public String dumpMultiLine() {
        return COBeanUtils.dumpBean(this, COBEntity.class, true, false, 0);
    }

}
