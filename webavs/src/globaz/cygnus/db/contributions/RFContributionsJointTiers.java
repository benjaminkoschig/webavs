package globaz.cygnus.db.contributions;

import globaz.cygnus.db.dossiers.RFDossier;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITIPersonneDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;

/**
 * <p>
 * Jointure entre les contributions d'assistance AI (ajouté au mandat InfoRom D0034) et les tiers
 * </p>
 * <p>
 * La clé primaire reste l'ID de la contribution, pour une recherche par ID tiers veuillez utiliser le manager
 * correspondant ({@link RFContributionsJointTiersManager})
 * </p>
 * 
 * @author PBA
 */
public class RFContributionsJointTiers extends RFContributionsAssistanceAI {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csSexe;
    private String dateNaissance;
    private String nom;
    private String nss;
    private String prenom;

    public RFContributionsJointTiers() {
        super();

        csSexe = null;
        dateNaissance = null;
        nom = null;
        nss = null;
        prenom = null;
    }

    @Override
    protected String _getFrom(BStatement statement) {

        String schema = _getCollection();
        String tableContributions = schema + RFContributionsAssistanceAI.TABLE_CONTRIBUTION_ASSISTANCE_AI;
        String tabelDossierRFM = schema + RFDossier.TABLE_NAME;
        String tableDemandePrestation = schema + PRDemande.TABLE_NAME;
        String tableTiers = schema + ITITiersDefTable.TABLE_NAME;
        String tablePersonne = schema + ITIPersonneDefTable.TABLE_NAME;
        String tablePersonneAvs = schema + ITIPersonneAvsDefTable.TABLE_NAME;

        StringBuilder sql = new StringBuilder();

        sql.append(tableContributions);

        sql.append(" INNER JOIN ").append(tabelDossierRFM);
        sql.append(" ON ").append(tableContributions).append(".").append(RFContributionsAssistanceAI.ID_DOSSIER_RFM)
                .append("=").append(tabelDossierRFM).append(".").append(RFDossier.FIELDNAME_ID_DOSSIER);

        sql.append(" INNER JOIN ").append(tableDemandePrestation);
        sql.append(" ON ").append(tabelDossierRFM).append(".").append(RFDossier.FIELDNAME_ID_PRDEM).append("=")
                .append(tableDemandePrestation).append(".").append(PRDemande.FIELDNAME_IDDEMANDE);

        sql.append(" INNER JOIN ").append(tableTiers);
        sql.append(" ON ").append(tableDemandePrestation).append(".").append(PRDemande.FIELDNAME_IDTIERS).append("=")
                .append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS);

        sql.append(" INNER JOIN ").append(tablePersonne);
        sql.append(" ON ").append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS).append("=")
                .append(tablePersonne).append(".").append(ITIPersonneDefTable.ID_TIERS);

        sql.append(" INNER JOIN ").append(tablePersonneAvs);
        sql.append(" ON ").append(tablePersonne).append(".").append(ITIPersonneDefTable.ID_TIERS).append("=")
                .append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.ID_TIERS);

        return sql.toString();
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        csSexe = statement.dbReadNumeric(ITIPersonneDefTable.CS_SEXE);
        dateNaissance = statement.dbReadDateAMJ(ITIPersonneDefTable.DATE_NAISSANCE);
        nom = statement.dbReadString(ITITiersDefTable.DESIGNATION_1);
        nss = statement.dbReadString(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL);
        prenom = statement.dbReadString(ITITiersDefTable.DESIGNATION_2);
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
