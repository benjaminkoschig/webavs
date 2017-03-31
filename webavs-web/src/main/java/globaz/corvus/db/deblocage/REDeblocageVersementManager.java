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
import java.util.Locale;
import ch.globaz.common.sql.SQLWriter;

public class REDeblocageVersementManager extends BManager {

    private static final long serialVersionUID = 1L;

    private String forIdRenteAccordee;
    private String forIdLot;
    private String forCsSexe;
    private String likeNom;
    private String likePrenom;
    private String forDateNaissance;
    private String likeNumeroAVS;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REDeblocageVersement();
    }

    @Override
    protected String _getSql(BStatement statement) {

        return SQLWriter
                .write(_getCollection())
                .locale(new Locale(getSession().getIdLangueISO()))
                .select()
                .fields(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE,
                        REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE,
                        REPrestationsAccordees.FIELDNAME_CODE_PRESTATION,
                        REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT,
                        REInformationsComptabilite.FIELDNAME_ID_DOMAINE_APPLICATION,
                        REInformationsComptabilite.FIELDNAME_ID_COMPTE_ANNEXE, "cpta.DESCRIPTION",
                        "cpta.IDEXTERNEROLE", "sec.IDEXTERNE", "tpers.hptsex", "tpers.hpdnai", "tiers.hnipay")
                .fields("ld", RELigneDeblocageTableDef.class)
                .fields("ldv", RELigneDeblocageVentilationTableDef.class)
                .from("schema." + RELigneDeblocageTableDef.TABLE_NAME + " ld "
                        + "inner join schema.RE_LIGNE_DEBLOCAGE_VENTIL ldv on ldv.ID_LIGNE_DEBLOCAGE = ld.ID "
                        + "inner join schema.REPRACC ra on ra.ZTIPRA = ld.ID_RENTE_ACCORDEE "
                        + "inner join schema.REINCOM ri on ri.YNIIIC = ra.ZTIICT "
                        + "inner join schema.CACPTAP cpta on cpta.idcompteannexe = ri."
                        + REInformationsComptabilite.FIELDNAME_ID_COMPTE_ANNEXE + " "
                        + "inner join schema.titierp tiers on tiers.HTITIE = cpta.IDTIERS "
                        + "inner join schema.TIPERSP tpers on tpers.HTITIE = cpta.IDTIERS "
                        + "inner join schema.CASECTP sec on sec.IDSECTION = ldv.ID_SECTION_SOURCE").where()
                .and("ra." + REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE)
                .equalForNumber(forIdRenteAccordee).and("ld", RELigneDeblocageTableDef.ID_LOT).equalForNumber(forIdLot)
                .and("tiers.HTLDE1").fullLikeCaseInsensitive(likeNom).and("tiers.HTLDE2")
                .fullLikeCaseInsensitive(likePrenom).and("cpta.IDEXTERNEROLE").fullLikeCaseInsensitive(likeNumeroAVS)
                .and("tpers.HPDNAI").equalForDate(forDateNaissance).and("tpers.HPTSEX").equalForNumber(forCsSexe)
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

    public String getForCsSexe() {
        return forCsSexe;
    }

    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public String getLikeNom() {
        return likeNom;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public String getLikePrenom() {
        return likePrenom;
    }

    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public String getLikeNumeroAVS() {
        return likeNumeroAVS;
    }

    public void setLikeNumeroAVS(String likeNumeroAVS) {
        this.likeNumeroAVS = likeNumeroAVS;
    }

}
