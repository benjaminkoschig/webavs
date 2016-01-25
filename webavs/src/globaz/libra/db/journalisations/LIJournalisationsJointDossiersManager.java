package globaz.libra.db.journalisations;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeStringUtil;
import globaz.journalisation.db.common.access.IJOCommonComplementJournalDefTable;
import globaz.journalisation.db.common.access.IJOCommonJournalisationDefTable;
import globaz.journalisation.db.common.access.IJOCommonReferenceProvenanceDefTable;
import globaz.journalisation.db.common.access.JOCommonLoggedEntity;
import globaz.journalisation.db.journalisation.access.JOJournalisationManager;
import globaz.libra.db.domaines.LIDomaines;
import globaz.libra.db.dossiers.LIDossiers;
import globaz.libra.db.groupes.LIGroupes;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import ch.globaz.libra.constantes.ILIConstantesExternes;

public class LIJournalisationsJointDossiersManager extends JOJournalisationManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATEDECES = "HPDDEC";
    public static final String FIELDNAME_DATENAISSANCE = "HPDNAI";

    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";

    public static final String FIELDNAME_NATIONALITE = "HNIPAY";
    public static final String FIELDNAME_NOM = "HTLDE1";
    public static final String FIELDNAME_NOM_FOR_SEARCH = "HTLDU1";
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_PRENOM = "HTLDE2";
    public static final String FIELDNAME_PRENOM_FOR_SEARCH = "HTLDU2";

    public static final String FIELDNAME_SEXE = "HPTSEX";
    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HISTO = "TIHAVSP";
    public static final String TABLE_PERSONNE = "TIPERSP";
    public static final String TABLE_TIERS = "TITIERP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String forDateDebut = new String();
    private String forDateFin = new String();
    private String forIdDossier = new String();
    private String forIdTiers = new String();
    private String forIdUser = new String();

    private String idTiersRechercheFamilleWhere = "";

    private boolean isVueFamille = false;
    private int nbTiersFamille = 0;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe LIJournalisationsJointDossiersManager.
     */
    public LIJournalisationsJointDossiersManager() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_beforeFind(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {

        if (isVueFamille) {

            // Début de la création de la String
            idTiersRechercheFamilleWhere += "(";

            // on chercher le tiers avec le nss
            PRTiersWrapper tw = PRTiersHelper.getTiersParId(getSession(), forIdTiers);

            if (tw != null) {

                // on cherche le membreFamille pour le tiers
                SFMembreFamille mf = new SFMembreFamille();
                mf.setSession(getSession());
                mf.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
                mf.setId(tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                mf.setCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_RENTES);
                mf.retrieve(transaction);

                if (mf.isNew()) {
                    mf.setCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_STANDARD);
                    mf.retrieve(transaction);
                }

                if (!mf.isNew()) {

                    boolean isPremierPassage = true;

                    if (!JadeStringUtil.isIntegerEmpty(tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS))) {
                        idTiersRechercheFamilleWhere += tw.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
                        nbTiersFamille++;
                        isPremierPassage = false;
                    }

                    // La méthode getParentsParMbrFamille est indépendant du
                    // domaine, on prend donc la SF pour le domaine rente,
                    // en passant null comme idTiersRequerant, aucun test n'est
                    // fait, et on est certain d'en obtenir une.
                    ISFSituationFamiliale sitFam = SFSituationFamilialeFactory.getSituationFamiliale(getSession(),
                            ISFSituationFamiliale.CS_DOMAINE_RENTES, null);
                    ISFMembreFamille[] membresFamille = sitFam.getMembresFamilleEtendue(mf.getIdMembreFamille(),
                            Boolean.TRUE);

                    for (int i = 0; i < membresFamille.length; i++) {
                        ISFMembreFamille membreFamille = membresFamille[i];

                        if (membreFamille != null) {

                            // Pas d'idTiers, pas de rentes
                            if (!JadeStringUtil.isIntegerEmpty(membreFamille.getIdTiers())
                                    && idTiersRechercheFamilleWhere.indexOf(membreFamille.getIdTiers()) == -1) {
                                nbTiersFamille++;

                                if (isPremierPassage) {
                                    idTiersRechercheFamilleWhere += membreFamille.getIdTiers();
                                    isPremierPassage = false;
                                } else {
                                    idTiersRechercheFamilleWhere += ", " + membreFamille.getIdTiers();
                                }
                            }
                        }
                    }
                }
            }

            // finir la string
            idTiersRechercheFamilleWhere += ")";

        }

    }

    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer sqlFields = new StringBuffer();
        sqlFields.append(super._getFields(statement));
        if (sqlFields.toString().length() != 0) {
            sqlFields.append(", ");
        }
        sqlFields.append(LIDossiers.FIELDNAME_ID_DOSSIER).append(", ");
        sqlFields.append(LIDossiers.FIELDNAME_ID_GESTIONNAIRE).append(", ");
        sqlFields.append(LIDossiers.FIELDNAME_ID_TIERS).append(", ");
        sqlFields.append(LIDossiers.FIELDNAME_ID_GROUPE).append(", ");
        sqlFields.append(LIDossiers.FIELDNAME_IS_URGENT).append(", ");
        sqlFields.append(LIDossiers.FIELDNAME_CS_ETAT).append(", ");
        sqlFields.append(FIELDNAME_NUM_AVS).append(", ");
        sqlFields.append(FIELDNAME_DATENAISSANCE).append(", ");
        sqlFields.append(FIELDNAME_DATEDECES).append(", ");
        sqlFields.append(FIELDNAME_SEXE).append(", ");
        sqlFields.append(FIELDNAME_NOM).append(", ");
        sqlFields.append(FIELDNAME_PRENOM).append(", ");
        sqlFields.append(FIELDNAME_NATIONALITE).append(", ");
        sqlFields.append(LIDomaines.FIELDNAME_CS_DOMAINE).append(", ");
        sqlFields.append("ComplementJournal.").append(IJOCommonComplementJournalDefTable.CSTYPECODESYSTEME)
                .append(", ");
        sqlFields.append("ComplementJournal.").append(IJOCommonComplementJournalDefTable.VALEURCODESYSTEME);

        return sqlFields.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String schema = _getCollection();
        fromClauseBuffer.append(super._getFrom(statement));

        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String and = " AND ";
        String point = ".";
        String egal = "=";

        // Jointure entre table des ReferenceProvnance et des dossiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIDossiers.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append("Provenance");
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJOCommonReferenceProvenanceDefTable.IDCLEREFERENCEPROVENANCE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append("replace(ltrim(replace(digits(");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIDossiers.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(LIDossiers.FIELDNAME_ID_DOSSIER);
        fromClauseBuffer.append("), '0', ' ')), ' ', '0')");
        fromClauseBuffer.append(and);
        fromClauseBuffer.append("Provenance");
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJOCommonReferenceProvenanceDefTable.TYPEREFERENCEPROVENANCE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append("'" + ILIConstantesExternes.REF_PRO_DOSSIER + "'");

        // Jointure entre table des dossiers et tables des domaines
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIDomaines.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIDossiers.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(LIDossiers.FIELDNAME_ID_DOMAINE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIDomaines.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(LIDomaines.FIELDNAME_ID_DOMAINE);

        // Jointure entre table des domaines et tables des groupes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIGroupes.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIDomaines.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(LIDomaines.FIELDNAME_ID_DOMAINE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIGroupes.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(LIGroupes.FIELDNAME_ID_DOMAINE);

        // Jointure entre table des dossier et tables des tiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(LIDossiers.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(LIDossiers.FIELDNAME_ID_TIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        // jointure entre table table des numeros AVS et table des personnes
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_PERSONNE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        // jointure entre table des personnes et table des tiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        return fromClauseBuffer.toString();
    }

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        setForIdCleReferenceProvenance(forIdDossier);

        StringBuffer sqlWhere = new StringBuffer();
        sqlWhere.append(super._getWhere(statement));

        if (isVueFamille && nbTiersFamille > 0) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(_getCollection() + LIDossiers.TABLE_NAME + "." + LIDossiers.FIELDNAME_ID_TIERS + " IN "
                    + idTiersRechercheFamilleWhere);

        } else {
            if (!JadeStringUtil.isIntegerEmpty(forIdDossier)) {
                if (sqlWhere.length() > 0) {
                    sqlWhere.append(" AND ");
                }
                sqlWhere.append(LIDossiers.FIELDNAME_ID_DOSSIER);
                sqlWhere.append(" = ");
                sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdDossier));
            }

            if (!JadeStringUtil.isIntegerEmpty(forIdTiers)) {
                if (sqlWhere.length() > 0) {
                    sqlWhere.append(" AND ");
                }
                sqlWhere.append(LIDossiers.FIELDNAME_ID_TIERS);
                sqlWhere.append(" = ");
                sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdTiers));
            }

            if (!JadeStringUtil.isIntegerEmpty(forIdUser)) {
                if (sqlWhere.length() > 0) {
                    sqlWhere.append(" AND ");
                }
                sqlWhere.append(IJOCommonJournalisationDefTable.IDUTILISATEUR);
                sqlWhere.append(" = ");
                sqlWhere.append(_dbWriteString(statement.getTransaction(), forIdUser));
            }

            if (!JadeStringUtil.isIntegerEmpty(forDateDebut)) {
                if (sqlWhere.length() > 0) {
                    sqlWhere.append(" AND ");
                }
                sqlWhere.append(IJOCommonJournalisationDefTable.DATE);
                sqlWhere.append(" >= ");
                sqlWhere.append(_dbWriteDateAMJ(statement.getTransaction(), forDateDebut));
            }

            if (!JadeStringUtil.isIntegerEmpty(forDateFin)) {
                if (sqlWhere.length() > 0) {
                    sqlWhere.append(" AND ");
                }
                sqlWhere.append(IJOCommonJournalisationDefTable.DATE);
                sqlWhere.append(" <= ");
                sqlWhere.append(_dbWriteDateAMJ(statement.getTransaction(), forDateFin));
            }
        }

        return sqlWhere.toString();
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    // ~ Getter & Setter
    // -----------------------------------------------------------------------------------------------------

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForIdDossier() {
        return forIdDossier;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForIdUser() {
        return forIdUser;
    }

    public boolean getIsVueFamille() {
        return isVueFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.journalisation.db.journalisation.access.JOJournalisationManager #newEntity()
     */
    @Override
    protected JOCommonLoggedEntity newEntity() {
        return new LIJournalisationsJointDossiers();
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForIdUser(String forIdUser) {
        this.forIdUser = forIdUser;
    }

    public void setIsVueFamille(boolean isVueFamille) {
        this.isVueFamille = isVueFamille;
    }

}
