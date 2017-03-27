/*
 * Globaz SA
 */
package globaz.corvus.db.deblocage;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class REDeblocageVersementManager extends BManager {

    private static final long serialVersionUID = 1L;

    private Long forIdRenteAccordee;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REDeblocageVersement();
    }

    @Override
    protected String _getSql(BStatement statement) {

        // SELECT * FROM ccjuweb.RE_LIGNE_DEBLOCAGE ld
        // inner join ccjuweb.RE_LIGNE_DEBLOCAGE_VENTIL ldv on ldv.ID_LIGNE_DEBLOCAGE = ld.ID
        // inner join ccjuweb.REPRACC ra on ra.ZTIPRA = ld.ID_RENTE_ACCORDEE
        // inner join ccjuweb.REINCOM ri on ri.YNIIIC = ra.ZTIICT

        return super._getSql(statement);
    }

    public Long getForIdRenteAccordee() {
        return forIdRenteAccordee;
    }

    public void setForIdRenteAccordee(Long forIdRenteAccordee) {
        this.forIdRenteAccordee = forIdRenteAccordee;
    }

}
