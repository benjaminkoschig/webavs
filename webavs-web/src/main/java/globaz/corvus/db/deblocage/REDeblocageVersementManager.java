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

    private String forIdRenteAccordee;
    private String forIdLot;

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
                        REInformationsComptabilite.FIELDNAME_ID_COMPTE_ANNEXE, "cpta.DESCRIPTION",
                        "cpta.IDEXTERNEROLE", "sec.IDEXTERNE")
                .fields("ld", RELigneDeblocageTableDef.class)
                .fields("ldv", RELigneDeblocageVentilationTableDef.class)
                .from("schema." + RELigneDeblocageTableDef.TABLE_NAME + " ld "
                        + "inner join schema.RE_LIGNE_DEBLOCAGE_VENTIL ldv on ldv.ID_LIGNE_DEBLOCAGE = ld.ID "
                        + "inner join schema.REPRACC ra on ra.ZTIPRA = ld.ID_RENTE_ACCORDEE "
                        + "inner join schema.REINCOM ri on ri.YNIIIC = ra.ZTIICT "
                        + "inner join schema.CACPTAP cpta on cpta.idcompteannexe = ri."
                        + REInformationsComptabilite.FIELDNAME_ID_COMPTE_ANNEXE + " "
                        + "inner join CCJUWEB.CASECTP sec on sec.IDSECTION = ldv.ID_SECTION_SOURCE").where().and("ra")
                .equalForNumber(forIdRenteAccordee).and("ld", RELigneDeblocageTableDef.ID_LOT).equalForNumber(forIdLot)
                .toSql();
    }

    public String getForIdRenteAccordee() {
        return forIdRenteAccordee;
    }

    public void setForIdRenteAccordee(String forIdRenteAccordee) {
        this.forIdRenteAccordee = forIdRenteAccordee;
    }

    public String getForIdLot() {
        return forIdLot;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

}
