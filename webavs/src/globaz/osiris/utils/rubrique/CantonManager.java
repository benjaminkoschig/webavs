/**
 * 
 */
package globaz.osiris.utils.rubrique;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * @author sel
 * 
 */
public class CantonManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected String _getSql(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        sql.append("select cs.pcosid, cs.pcosid-505000 numero, pcouid canton from ")
                .append(_getCollection())
                .append("fwcoup cu inner join ")
                .append(_getCollection())
                .append("fwcosp cs on cs.pcosid=cu.pcosid where pptygr like 'PYCANTON%' and pcoitc=10500005 and PLAIDE='F';");

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new Canton();
    }

}
