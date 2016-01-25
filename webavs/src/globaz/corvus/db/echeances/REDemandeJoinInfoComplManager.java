package globaz.corvus.db.echeances;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BApplication;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.db.infos.PRInfoCompl;

public class REDemandeJoinInfoComplManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        try {
            BApplication application = (BApplication) GlobazSystem
                    .getApplication(REApplication.DEFAULT_APPLICATION_CORVUS);
            BSession session = (BSession) application.newSession();

            // CICICAM qualité
            session.connect("ciciglo", "glob4az");

            // FPV Qualité
            // session.connect("sparis", "8p4r1s.");

            REDemandeJoinInfoComplManager manager = new REDemandeJoinInfoComplManager(session);
            manager.setForCsTypeInfoCompl(IREDemandeRente.CS_TYPE_INFORMATION_COMPLEMENTAIRE_RENTE_VEUVE_PERDURE);
            manager.find();

            System.out.println("Nombre de rente de veuve perdure en base de données : " + manager.size());

            manager.setForIdTiersRequerant("12345");
            manager.setForCsTypeInfoCompl("");
            manager.find();
            System.out.println("Nombre d'info compl pour le tiers 12345 : " + manager.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.exit(0);
    }

    private String forCsTypeInfoCompl;
    private String forIdTiersRequerant;

    public REDemandeJoinInfoComplManager(BSession session) {
        super();

        setSession(session);

        forCsTypeInfoCompl = "";
        forIdTiersRequerant = "";
    }

    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        sql.append(PRInfoCompl.FIELDNAME_ID_INFO_COMPL).append(",");
        sql.append(PRInfoCompl.FIELDNAME_TYPE_INFO_COMPL);

        return sql.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        sql.append(_getCollection()).append(PRInfoCompl.TABLE_NAME);

        sql.append(" INNER JOIN ");
        sql.append(_getCollection()).append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        sql.append(" ON ");
        sql.append(_getCollection()).append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE).append(".")
                .append(REDemandeRente.FIELDNAME_ID_INFO_COMPLEMENTAIRE);
        sql.append("=");
        sql.append(_getCollection()).append(PRInfoCompl.TABLE_NAME).append(".")
                .append(PRInfoCompl.FIELDNAME_ID_INFO_COMPL);

        sql.append(" INNER JOIN ");
        sql.append(_getCollection()).append(PRDemande.TABLE_NAME);
        sql.append(" ON ");
        sql.append(_getCollection()).append(PRDemande.TABLE_NAME).append(".").append(PRDemande.FIELDNAME_IDDEMANDE);
        sql.append("=");
        sql.append(_getCollection()).append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE).append(".")
                .append(REDemandeRente.FIELDNAME_ID_DEMANDE_PRESTATION);

        return sql.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        if (!JadeStringUtil.isBlankOrZero(forCsTypeInfoCompl)) {
            sql.append(_getCollection()).append(PRInfoCompl.TABLE_NAME).append(".")
                    .append(PRInfoCompl.FIELDNAME_TYPE_INFO_COMPL);
            sql.append("=");
            sql.append(forCsTypeInfoCompl);
        }

        if (!JadeStringUtil.isBlankOrZero(forIdTiersRequerant)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(_getCollection()).append(PRDemande.TABLE_NAME).append(".").append(PRDemande.FIELDNAME_IDTIERS);
            sql.append("=");
            sql.append(forIdTiersRequerant);
        }

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REDemandeJoinInfoCompl();
    }

    public String getForCsTypeInfoCompl() {
        return forCsTypeInfoCompl;
    }

    public String getForIdTiersRequerant() {
        return forIdTiersRequerant;
    }

    public void setForCsTypeInfoCompl(String forCsTypeInfoCompl) {
        this.forCsTypeInfoCompl = forCsTypeInfoCompl;
    }

    public void setForIdTiersRequerant(String forIdTiersRequerant) {
        this.forIdTiersRequerant = forIdTiersRequerant;
    }
}
