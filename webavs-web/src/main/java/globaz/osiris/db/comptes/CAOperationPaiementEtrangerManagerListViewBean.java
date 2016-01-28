package globaz.osiris.db.comptes;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class CAOperationPaiementEtrangerManagerListViewBean extends CAEcritureManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CAListPaiementEtranger();
    }

}
