package globaz.hera.db.famille;

import globaz.corvus.application.REApplication;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <p>
 * Manager permettant de recherche un enfant et ses données de la tables des membres de familles.
 * </p>
 * 
 * @author PBA
 */
public class SFEnfantJoinMembreFamilleManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {

        try {
            // connexion à CICIQUA
            BSession session = (BSession) GlobazSystem.getApplication(REApplication.DEFAULT_APPLICATION_CORVUS)
                    .newSession("ciciglo", "glob4az");

            SFEnfantJoinMembreFamilleManager manager = new SFEnfantJoinMembreFamilleManager();
            manager.setSession(session);
            manager.setForIdConjoints("4643");
            manager.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < manager.getSize(); i++) {
                SFEnfantJoinMembreFamille enfant = (SFEnfantJoinMembreFamille) manager.get(i);

                StringBuilder message = new StringBuilder();
                message.append("nss: ").append(enfant.getNss()).append("\n");
                message.append("nom: ").append(enfant.getNom()).append("\n");
                message.append("prenom: ").append(enfant.getPrenom()).append("\n");
                message.append("idTiers: ").append(enfant.getIdTiers()).append("\n");
                message.append("idMembreFamille: ").append(enfant.getIdMembreFamille()).append("\n");
                message.append("idEnfant: ").append(enfant.getIdEnfant()).append("\n");
                message.append("idConjoints: ").append(enfant.getIdConjoint()).append("\n");
                System.out.println(message.toString());
            }
            System.out.println("nb resultats : " + manager.getSize());
            System.out.println("requete : " + manager._getSql(null));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String forIdConjoints;
    private String forIdEnfant;
    private String forIdMembreFamille;

    public SFEnfantJoinMembreFamilleManager() {
        super();

        forIdConjoints = "";
        forIdEnfant = "";
        forIdMembreFamille = "";
    }

    @Override
    protected String _getWhere(BStatement statement) {

        String tableEnfant = _getCollection() + SFEnfant.TABLE_NAME;

        StringBuilder sql = new StringBuilder();

        if (!JadeStringUtil.isBlank(forIdConjoints)) {
            sql.append(tableEnfant).append(".").append(SFEnfant.FIELD_IDCONJOINT);
            sql.append("=");
            sql.append(forIdConjoints);
        }

        if (!JadeStringUtil.isBlank(forIdEnfant)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableEnfant).append(".").append(SFEnfant.FIELD_IDENFANT);
            sql.append("=");
            sql.append(forIdEnfant);
        }

        if (!JadeStringUtil.isBlank(forIdMembreFamille)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableEnfant).append(".").append(SFEnfant.FIELD_IDMEMBREFAMILLE);
            sql.append("=");
            sql.append(forIdMembreFamille);
        }

        return sql.toString();
    }

    @Override
    protected SFEnfantJoinMembreFamille _newEntity() throws Exception {
        return new SFEnfantJoinMembreFamille();
    }

    public String getForIdConjoints() {
        return forIdConjoints;
    }

    public String getForIdEnfant() {
        return forIdEnfant;
    }

    public String getForIdMembreFamille() {
        return forIdMembreFamille;
    }

    public void setForIdConjoints(String forIdConjoints) {
        this.forIdConjoints = forIdConjoints;
    }

    public void setForIdEnfant(String forIdEnfant) {
        this.forIdEnfant = forIdEnfant;
    }

    public void setForIdMembreFamille(String forIdMembreFamille) {
        this.forIdMembreFamille = forIdMembreFamille;
    }
}
