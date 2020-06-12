package globaz.corvus.db.rentesaccordees;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.application.REApplication;
import globaz.corvus.process.liste.rentedouble.REAnalyseurRenteDouble;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAVector;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITIPersonneDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Recherche en base de données toutes les rentes en cours, et retourne avec quelques informations sur le tiers
 * bénéficiaire.<br/>
 * N'est utilisé que pour la génération de la liste des rentes doubles
 * 
 * @author PBA
 */
public class RERenteJoinPersonneAvsManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        try {
            BSession session = new BSession(REApplication.DEFAULT_APPLICATION_CORVUS);
            session.connect("ccjuglo", "glob4az");
            RERenteJoinPersonneAvsManager manager = new RERenteJoinPersonneAvsManager();
            manager.setSession(session);
            manager.setForDate("07.2020");
            manager.setNss("756.5133.1244.89");
            manager.find(BManager.SIZE_NOLIMIT);

            System.out.println("\n\nNombre de rentes trouvées : " + manager.size() + "\nRequête : "
                    + manager._getSql(null) + "\n\n");

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        System.exit(0);
    }

    private String forDate = "";


    private String nss ="";

    public RERenteJoinPersonneAvsManager() {
        super();

        wantCallMethodAfterFind(true);
    }

    @Override
    protected void _afterFind(BTransaction transaction) throws Exception {

        REAnalyseurRenteDouble analyseurRenteDouble = new REAnalyseurRenteDouble();
        List<RERenteJoinPersonneAvs> rentesDoubles = analyseurRenteDouble.getRentesDoubles(getListeDesResultats());
        Collections.sort(rentesDoubles);

        JAVector newContainer = new JAVector();
        newContainer.addAll(rentesDoubles);
        setContainer(newContainer);
    }

    @Override
    protected String _getFields(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append(",");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(",");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(",");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(",");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                .append(REPrestationsAccordees.FIELDNAME_FRACTION_RENTE).append(",");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                .append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION).append(",");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CS_ETAT).append(",");

        sql.append(_getCollection()).append(ITITiersDefTable.TABLE_NAME).append(".").append(ITITiersDefTable.ID_TIERS)
                .append(",");
        sql.append(_getCollection()).append(ITITiersDefTable.TABLE_NAME).append(".").append(ITITiersDefTable.ID_PAYS)
                .append(",");
        sql.append(_getCollection()).append(ITITiersDefTable.TABLE_NAME).append(".")
                .append(ITITiersDefTable.DESIGNATION_1).append(",");
        sql.append(_getCollection()).append(ITITiersDefTable.TABLE_NAME).append(".")
                .append(ITITiersDefTable.DESIGNATION_1_MAJ).append(",");
        sql.append(_getCollection()).append(ITITiersDefTable.TABLE_NAME).append(".")
                .append(ITITiersDefTable.DESIGNATION_2).append(",");
        sql.append(_getCollection()).append(ITITiersDefTable.TABLE_NAME).append(".")
                .append(ITITiersDefTable.DESIGNATION_2_MAJ).append(",");

        sql.append(_getCollection()).append(ITIPersonneDefTable.TABLE_NAME).append(".")
                .append(ITIPersonneDefTable.CS_SEXE).append(",");
        sql.append(_getCollection()).append(ITIPersonneDefTable.TABLE_NAME).append(".")
                .append(ITIPersonneDefTable.DATE_NAISSANCE).append(",");

        sql.append(_getCollection()).append(ITIPersonneAvsDefTable.TABLE_NAME).append(".")
                .append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL).append(",");
        sql.append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(".")
                .append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_1).append(",");
        sql.append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(".")
                .append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_2).append(",");
        sql.append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(".")
                .append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_3).append(",");
        sql.append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(".")
                .append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_4).append(",");
        sql.append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(".")
                .append(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_5).append(",");
        sql.append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(".")
                .append(RERenteAccordee.FIELDNAME_ID_TIERS_COMPLEMENTAIRE_1).append(",");
        sql.append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(".")
                .append(RERenteAccordee.FIELDNAME_ID_TIERS_COMPLEMENTAIRE_2);

        return sql.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        sql.append(" INNER JOIN ");
        sql.append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        sql.append(" ON ");
        sql.append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(".").append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);
        sql.append("=");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

        // jointure entre table des prestations et table des tiers
        sql.append(" INNER JOIN ");
        sql.append(_getCollection()).append(ITITiersDefTable.TABLE_NAME);
        sql.append(" ON ");
        sql.append(_getCollection()).append(ITITiersDefTable.TABLE_NAME).append(".").append(ITITiersDefTable.ID_TIERS);
        sql.append("=");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);

        // jointure entre table des tiers et la table des personnes
        sql.append(" INNER JOIN ");
        sql.append(_getCollection()).append(ITIPersonneDefTable.TABLE_NAME);
        sql.append(" ON ");
        sql.append(_getCollection()).append(ITIPersonneDefTable.TABLE_NAME).append(".")
                .append(ITIPersonneDefTable.ID_TIERS);
        sql.append("=");
        sql.append(_getCollection()).append(ITITiersDefTable.TABLE_NAME).append(".").append(ITITiersDefTable.ID_TIERS);

        // jointure entre la table des personnes AVS et la table des personnes
        sql.append(" INNER JOIN ");
        sql.append(_getCollection()).append(ITIPersonneAvsDefTable.TABLE_NAME);
        sql.append(" ON ");
        sql.append(_getCollection()).append(ITIPersonneAvsDefTable.TABLE_NAME).append(".")
                .append(ITIPersonneAvsDefTable.ID_TIERS);
        sql.append("=");
        sql.append(_getCollection()).append(ITIPersonneDefTable.TABLE_NAME).append(".")
                .append(ITIPersonneDefTable.ID_TIERS);

        return sql.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sql = new StringBuffer();

        // critères sur le mois
        if (!JadeStringUtil.isEmpty(getForDate())) {

            sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                    .append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT);
            sql.append(" <= ");
            sql.append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDate()));

            sql.append(" AND (");

            sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                    .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
            sql.append(" >= ");
            sql.append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDate()));
            sql.append(" OR ");
            sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                    .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
            sql.append("=0");

            sql.append(") AND (");

            sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                    .append(REPrestationsAccordees.FIELDNAME_CS_ETAT);
            sql.append(" IN (");
            sql.append(IREPrestationAccordee.CS_ETAT_VALIDE).append(",");
            sql.append(IREPrestationAccordee.CS_ETAT_PARTIEL).append(",");
            sql.append(IREPrestationAccordee.CS_ETAT_DIMINUE);
            sql.append(")");
            if(!JadeStringUtil.isBlankOrZero(nss)){
                sql.append(") AND (");

                sql.append(_getCollection()).append(ITIPersonneAvsDefTable.TABLE_NAME).append(".")
                        .append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL).append("=");
                sql.append("'");
                sql.append(nss);
                sql.append("'");
                sql.append(")");
            }
        }

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RERenteJoinPersonneAvs();
    }

    public String getForDate() {
        return forDate;
    }

    private List<RERenteJoinPersonneAvs> getListeDesResultats() {
        List<RERenteJoinPersonneAvs> rentes = new ArrayList<RERenteJoinPersonneAvs>();

        for (Iterator<RERenteJoinPersonneAvs> iterator = iterator(); iterator.hasNext();) {
            rentes.add(iterator.next());
        }

        return rentes;
    }

    @Override
    public String getOrderByDefaut() {
        StringBuilder sql = new StringBuilder();

        sql.append(_getCollection()).append(ITITiersDefTable.TABLE_NAME).append(".")
                .append(ITITiersDefTable.DESIGNATION_1_MAJ).append(",");
        sql.append(_getCollection()).append(ITITiersDefTable.TABLE_NAME).append(".")
                .append(ITITiersDefTable.DESIGNATION_2_MAJ).append(",");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);

        return sql.toString();
    }

    public void setForDate(String string) {
        forDate = string;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }
}
