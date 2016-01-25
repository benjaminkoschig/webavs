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
 * Recherche en base de donn�es toutes les rentes en cours, et retourne avec quelques informations sur le tiers
 * b�n�ficiaire.<br/>
 * N'est utilis� que pour la g�n�ration de la liste des rentes doubles
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
            session.connect("pba", "pba");
            RERenteJoinPersonneAvsManager manager = new RERenteJoinPersonneAvsManager();
            manager.setSession(session);
            manager.setForDate("11.2011");
            manager.find(BManager.SIZE_NOLIMIT);

            System.out.println("\n\nNombre de rentes trouv�es : " + manager.size() + "\nRequ�te : "
                    + manager._getSql(null) + "\n\n");

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        System.exit(0);
    }

    private String forDate = "";

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
                .append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL);

        return sql.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);

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

        // crit�res sur le mois
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
            sql.append(IREPrestationAccordee.CS_ETAT_PARTIEL);
            sql.append(")");

            sql.append(")");
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
}
