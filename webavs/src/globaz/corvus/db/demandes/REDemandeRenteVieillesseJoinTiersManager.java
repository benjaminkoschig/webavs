package globaz.corvus.db.demandes;

import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.db.tiers.ITITiersDefTable;

public class REDemandeRenteVieillesseJoinTiersManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean avecAjournement;
    private String forCsEtatDemandeRente;
    private String forIdTiersRequerant;

    public REDemandeRenteVieillesseJoinTiersManager() {
        super();

        avecAjournement = false;
        forCsEtatDemandeRente = "";
        forIdTiersRequerant = "";
    }

    @Override
    protected String _getWhere(BStatement statement) {

        String tableDemandeRenteVieillesse = _getCollection()
                + REDemandeRenteVieillesse.TABLE_NAME_DEMANDE_RENTE_VIEILLESSE;
        String tableDemandeRente = _getCollection() + REDemandeRente.TABLE_NAME_DEMANDE_RENTE;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;

        StringBuilder sql = new StringBuilder();

        if (avecAjournement) {
            sql.append(tableDemandeRenteVieillesse).append(".")
                    .append(REDemandeRenteVieillesse.FIELDNAME_IS_AJOURNEMENT_REQUERANT).append("='1'");
        }

        if (!JadeStringUtil.isBlank(forCsEtatDemandeRente)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_CS_ETAT);
            sql.append("=");
            sql.append(forCsEtatDemandeRente);
        }

        if (!JadeStringUtil.isBlank(forIdTiersRequerant)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS);
            sql.append("=");
            sql.append(forIdTiersRequerant);
        }

        return sql.toString();
    }

    @Override
    protected REDemandeRenteVieillesseJoinTiers _newEntity() throws Exception {
        return new REDemandeRenteVieillesseJoinTiers();
    }

    public String getForCsEtatDemandeRente() {
        return forCsEtatDemandeRente;
    }

    public String getForIdTiersRequerant() {
        return forIdTiersRequerant;
    }

    public boolean isAvecAjournement() {
        return avecAjournement;
    }

    public void setAvecAjournement(boolean avecAjournement) {
        this.avecAjournement = avecAjournement;
    }

    public void setForCsEtatDemandeRente(String forCsEtatDemandeRente) {
        this.forCsEtatDemandeRente = forCsEtatDemandeRente;
    }

    public void setForIdTiersRequerant(String forIdTiersRequerant) {
        this.forIdTiersRequerant = forIdTiersRequerant;
    }
}
