/*
 * Globaz SA.
 */
package globaz.corvus.db.lignedeblocage;

import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageEtat;
import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageType;
import ch.globaz.common.jadedb.JadeManager;
import ch.globaz.common.sql.SQLWriter;

public class RELigneDeblocageManager extends JadeManager<RELigneDeblocage> {

    private static final long serialVersionUID = 1L;
    private Long forIdRentePrestation;
    private Long forIdLot;
    private RELigneDeblocageEtat forEtat;
    private RELigneDeblocageType forType;
    private Long forIdSectionCompensee;
    private Long forIdRoleSection;

    @Override
    protected void createWhere(SQLWriter sqlWhere) {
        sqlWhere.and(RELigneDeblocageTableDef.ID_RENTE_ACCORDEE).equal(forIdRentePrestation);
        sqlWhere.and(RELigneDeblocageTableDef.CS_ETAT).equal(forEtat);
        sqlWhere.and(RELigneDeblocageTableDef.CS_TYPE_DEBLOCAGE).equal(forType);
        sqlWhere.and(RELigneDeblocageTableDef.ID_LOT).equal(forIdLot);
        sqlWhere.and(RELigneDeblocageTableDef.ID_SECTION_COMPENSEE).equal(forIdSectionCompensee);
        sqlWhere.and(RELigneDeblocageTableDef.ID_ROLE_SECTION).equal(forIdRoleSection);
    }

    @Override
    protected RELigneDeblocage _newEntity() throws Exception {
        return new RELigneDeblocage();
    }

    public void setForIdRentePrestation(Long forIdRentePrestation) {
        this.forIdRentePrestation = forIdRentePrestation;
    }

    public void setForEtat(RELigneDeblocageEtat forEtat) {
        this.forEtat = forEtat;
    }

    public void setForType(RELigneDeblocageType forType) {
        this.forType = forType;
    }

    public void setForIdLot(Long forIdLot) {
        this.forIdLot = forIdLot;
    }

    public void setForIdSectionCompensee(Long forIdSectionCompensee) {
        this.forIdSectionCompensee = forIdSectionCompensee;
    }

    public void setForIdRoleSection(Long forIdRoleSection) {
        this.forIdRoleSection = forIdRoleSection;
    }

}
