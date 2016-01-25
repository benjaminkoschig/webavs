/*
 * mmu Créé le 18 oct. 05
 */
package globaz.hera.impl.standard;

import globaz.globall.db.BEntity;
import globaz.hera.db.famille.SFApercuRelationConjointManager;

/**
 * @author mmu
 * 
 *         18 oct. 05
 */
public class SFRelationFamilialeManagerStd extends SFApercuRelationConjointManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new SFRelationFamilialeStd();
    }

}
