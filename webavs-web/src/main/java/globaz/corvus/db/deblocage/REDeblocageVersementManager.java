/*
 * Globaz SA
 */
package globaz.corvus.db.deblocage;

import globaz.corvus.db.lignedeblocage.RELigneDeblocageTableDef;
import globaz.corvus.db.lignedeblocageventilation.RELigneDeblocageVentilationTableDef;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import ch.globaz.common.sql.SQLWriter;

public class REDeblocageVersementManager extends BManager {

    private static final long serialVersionUID = 1L;

    private Long forIdRenteAccordee;
    private Long forIdLot;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REDeblocageVersement();
    }

    @Override
    protected String _getSql(BStatement statement) {

        return SQLWriter
                .write(_getCollection())
                .select()
                .fields(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE,
                        REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE,
                        REPrestationsAccordees.FIELDNAME_CODE_PRESTATION,
                        REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT,
                        REInformationsComptabilite.FIELDNAME_ID_DOMAINE_APPLICATION,
                        REInformationsComptabilite.FIELDNAME_ID_COMPTE_ANNEXE)
                .fields("ld", RELigneDeblocageTableDef.class)
                .fields("ldv", RELigneDeblocageVentilationTableDef.class)
                .from("schema." + RELigneDeblocageTableDef.TABLE_NAME + " ld "
                        + "inner join schema.RE_LIGNE_DEBLOCAGE_VENTIL ldv on ldv.ID_LIGNE_DEBLOCAGE = ld.ID "
                        + "inner join schema.REPRACC ra on ra.ZTIPRA = ld.ID_RENTE_ACCORDEE "
                        + "inner join schema.REINCOM ri on ri.YNIIIC = ra.ZTIICT").where().and("ra")
                .equal(forIdRenteAccordee).and("ld", RELigneDeblocageTableDef.ID_LOT).equal(forIdLot).toSql();
    }

    public Long getForIdRenteAccordee() {
        return forIdRenteAccordee;
    }

    public Long getForIdLot() {
        return forIdLot;
    }

    public void setForIdLot(Long forIdLot) {
        this.forIdLot = forIdLot;
    }

    public void setForIdRenteAccordee(Long forIdRenteAccordee) {
        this.forIdRenteAccordee = forIdRenteAccordee;
    }

}
