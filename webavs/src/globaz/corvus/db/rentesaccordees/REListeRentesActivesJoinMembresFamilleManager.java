package globaz.corvus.db.rentesaccordees;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.db.famille.SFRelationConjoint;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.db.tiers.ITITiersDefTable;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Retourne toutes les rentes actives pour le mois donné, avec les membres de la famille du requérant. <br/>
 * Utilisé pour optimiser la liste des rentes en erreur.
 */
public class REListeRentesActivesJoinMembresFamilleManager extends BManager implements
        BIGenericManager<RERenteActiveJoinMembresFamille> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Format : mm.aaaa */
    private String forDatePaiement = "";

    @Override
    protected String _getOrder(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tableMembreFamille = _getCollection() + SFMembreFamille.TABLE_NAME;
        String tableRelationConjugale = _getCollection() + SFRelationConjoint.TABLE_NAME;

        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_1_MAJ).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_2_MAJ).append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE)
                .append(",");
        sql.append(tableMembreFamille).append(".").append(SFMembreFamille.FIELD_DOMAINE_APPLICATION).append(",");
        sql.append(tableRelationConjugale).append(".").append(SFRelationConjoint.FIELD_DATEDEBUT);

        return sql.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
        String tableRenteAccordee = _getCollection() + RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE;

        sql.append("(");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT)
                .append(">=").append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDatePaiement()));
        sql.append(" OR ");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT)
                .append("=0");
        sql.append(" OR ");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT)
                .append(" IS NULL");
        sql.append(")");

        sql.append(" AND ").append(tablePrestationAccordee).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append("<=")
                .append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDatePaiement()));
        sql.append(" AND (").append(tablePrestationAccordee).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CS_ETAT).append(" IN(");
        sql.append(IREPrestationAccordee.CS_ETAT_VALIDE).append(",");
        sql.append(IREPrestationAccordee.CS_ETAT_PARTIEL).append(",");
        sql.append(IREPrestationAccordee.CS_ETAT_DIMINUE);
        sql.append(") OR (");
        sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_1)
                .append(" IN('07','08')");
        sql.append(" OR ").append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_2)
                .append(" IN('07','08')");
        sql.append(" OR ").append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_3)
                .append(" IN('07','08')");
        sql.append(" OR ").append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_4)
                .append(" IN('07','08')");
        sql.append(" OR ").append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_5)
                .append(" IN('07','08')");
        sql.append("))");

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RERenteActiveJoinMembresFamille();
    }

    @Override
    public List<RERenteActiveJoinMembresFamille> getContainerAsList() {
        List<RERenteActiveJoinMembresFamille> list = new ArrayList<RERenteActiveJoinMembresFamille>();
        for (int i = 0; i < size(); i++) {
            list.add((RERenteActiveJoinMembresFamille) get(i));
        }
        return list;
    }

    public String getForDatePaiement() {
        return forDatePaiement;
    }

    /**
     * 
     * @param forDatePaiement
     *            format : mm.aaaa
     */
    public void setForDatePaiement(String forDatePaiement) {
        this.forDatePaiement = forDatePaiement;
    }
}
