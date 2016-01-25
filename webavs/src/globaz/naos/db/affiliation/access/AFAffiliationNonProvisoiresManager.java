package globaz.naos.db.affiliation.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * Manager pour les Affiliation Non-Provisoires.
 * 
 * @author ado 21 avr. 04
 */
public class AFAffiliationNonProvisoiresManager extends BManager {

    private static final long serialVersionUID = 4016646250121602686L;

    @Override
    protected String _getFields(BStatement statement) {
        return "distinct " + _getCollection() + "afaffip.htitie," + _getCollection() + "afaffip.maiaff,"
                + _getCollection() + "afaffip.malnaf ";
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "afaffip inner join " + _getCollection() + "afcotip on " + _getCollection()
                + "afaffip.maiaff=" + _getCollection()
                + "afcotip.maiaff and mabtra=2 and memmen=0 and madfin=0 and mattaf=804004";
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFAffiliationNonProvisoires();
    }
}