/*
 * Globaz SA.
 */
package globaz.naos.db.controleLpp;

import globaz.globall.db.BEntity;
import ch.globaz.common.jadedb.JadeManager;
import ch.globaz.common.sql.SQLWriter;

public class AFSuiviLppAnnuelSalariesManager extends JadeManager<AFSuiviLppAnnuelSalarie> {

    private static final long serialVersionUID = 1L;
    private Long forIdAffiliation;
    private Integer forAnnee;

    @Override
    protected void createWhere(SQLWriter sqlWhere) {
        sqlWhere.and(AFSuiviLppAnnuelSalariesTableDef.ID_AFFILIATION).equal(forIdAffiliation);
        sqlWhere.and(AFSuiviLppAnnuelSalariesTableDef.ANNEE).equal(forAnnee);
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFSuiviLppAnnuelSalarie();
    }

    public void setForIdAffiliation(Long forIdAffiliation) {
        this.forIdAffiliation = forIdAffiliation;
    }

    public void setForAnnee(Integer forAnnee) {
        this.forAnnee = forAnnee;
    }

}
