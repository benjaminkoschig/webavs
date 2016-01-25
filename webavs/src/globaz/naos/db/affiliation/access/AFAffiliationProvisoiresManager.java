package globaz.naos.db.affiliation.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.naos.db.affiliation.AFAffiliationManager;

/**
 * Manager pour les Affiliation Provisoires.
 * 
 * @author ado 20 avr. 04
 */
public class AFAffiliationProvisoiresManager extends AFAffiliationManager {

    private static final long serialVersionUID = 11575024348178141L;

    @Override
    protected String _getWhere(BStatement statement) {
        this.forIsTraitement(true);
        return super._getWhere(statement);
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFAffiliationProvisoires();
    }
}
