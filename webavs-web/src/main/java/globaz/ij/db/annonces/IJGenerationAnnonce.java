/*
 * Créé le 2 nov. 05
 */
package globaz.ij.db.annonces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFRelationFamiliale;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.ij.api.annonces.IIJAnnonce;
import globaz.ij.api.prestations.IIJRepartitionPaiements;
import globaz.ij.api.prononces.IIJMesure;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.IJGrandeIJCalculee;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJIndemniteJournaliere;
import globaz.ij.db.prestations.IJIndemniteJournaliereManager;
import globaz.ij.db.prestations.IJPetiteIJCalculee;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prestations.IJRepartitionPaiements;
import globaz.ij.db.prestations.IJRepartitionPaiementsManager;
import globaz.ij.db.prononces.IJPrononce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/**
 * Classe faisant les jointures nécessaires pour la génération des annonces
 * 
 * @author dvh
 */
public class IJGenerationAnnonce extends BEntity {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @return DOCUMENT ME!
     */
    public static final String createFields() {
        StringBuffer truc = new StringBuffer();
        truc.append(IJPrestation.FIELDNAME_DATEDEBUT);
        truc.append(", ");
        truc.append(IJPrestation.FIELDNAME_CS_TYPE);
        truc.append(", ");
        truc.append(PRDemande.FIELDNAME_IDTIERS);
        truc.append(", ");
        truc.append(IJIJCalculee.FIELDNAME_REVENU_DETERMINANT);
        truc.append(", ");
        truc.append(IJIJCalculee.FIELDNAME_SUPPLEMENT_PERSONNE_SEULE);
        truc.append(", ");
        truc.append(IJGrandeIJCalculee.FIELDNAME_NB_ENFANTS);
        truc.append(", ");
        truc.append(IJIJCalculee.FIELDNAME_CS_TYPE_BASE);
        truc.append(", ");
        truc.append(IJGrandeIJCalculee.FIELDNAME_MONTANT_INDEMNITE_EXPLOITATION);
        truc.append(", ");
        truc.append(IJGrandeIJCalculee.FIELDNAME_MONTANT_INDEMNITE_ASSISTANCE);
        truc.append(", ");
        truc.append(IJPrononce.FIELDNAME_CS_TYPE_IJ);
        truc.append(", ");
        truc.append(IJIJCalculee.FIELDNAME_ID_IJ_CALCULEE);
        truc.append(", ");
        truc.append(IJPrestation.FIELDNAME_MONTANTBRUT);
        truc.append(", ");
        truc.append(IJBaseIndemnisation.FIELDNAME_DATEDEBUTPERIODE);
        truc.append(", ");
        truc.append(IJBaseIndemnisation.FIELDNAME_DATEFINPERIODE);
        truc.append(", ");
        truc.append(IJPrestation.FIELDNAME_MONTANTBRUTINTERNE);
        truc.append(", ");
        truc.append(IJPrestation.FIELDNAME_MONTANTBRUTEXTERNE);
        truc.append(", ");
        truc.append(IJPrestation.FIELDNAME_NOMBRE_JOURS_EXT);
        truc.append(", ");
        truc.append(IJPrestation.FIELDNAME_NOMBRE_JOURS_INT);
        truc.append(", ");
        truc.append(IJPrestation.FIELDNAME_MONTANT_JOURNALIER_EXT);
        truc.append(", ");
        truc.append(IJPrestation.FIELDNAME_MONTANT_JOURNALIER_INT);
        truc.append(", ");
        truc.append(IJPrononce.FIELDNAME_CS_GENRE);
        truc.append(", ");
        truc.append(IJPrononce.FIELDNAME_MONTANT_GARANTI_AA);
        truc.append(", ");
        truc.append(IJPrononce.FIELDNAME_MONTANT_GARANTI_AA_REDUIT);
        truc.append(", ");
        truc.append(IJPrestation.FIELDNAME_IDPRESTATION);
        truc.append(", ");
        truc.append(IJPrestation.FIELDNAME_DROITACQUIS);
        truc.append(", ");
        truc.append(IJBaseIndemnisation.FIELDNAME_NOMBREJOURSINTERRUPTION);
        truc.append(", ");
        truc.append(IJBaseIndemnisation.FIELDNAME_MOTIFINTERRUPTION);
        truc.append(", ");
        truc.append(IJPrononce.FIELDNAME_OFFICE_AI);
        truc.append(", ");
        truc.append(IJPrestation.FIELDNAME_IDANNONCE);
        truc.append(", ");
        truc.append(IJPrononce.FIELDNAME_NO_DECISION_AI);
        truc.append(", ");
        truc.append(IJIJCalculee.FIELDNAME_DROIT_PRESTATION_ENFANT);
        truc.append(", ");
        truc.append(IJIJCalculee.FIELDNAME_NO_REVISION);
        truc.append(", ");
        truc.append(IJIJCalculee.FIELDNAME_DROIT_PRESTATION_ENFANT);

        return truc.toString();
    }

    /**
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrestation.TABLE_NAME);

        // Jointure entre prestation et base indemnisation
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJBaseIndemnisation.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrestation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrestation.FIELDNAME_ID_BASEINDEMNISATION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJBaseIndemnisation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJBaseIndemnisation.FIELDNAME_IDBASEINDEMNISATION);

        // jointure entre tables bases indemnisation et prononce
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononce.TABLE_NAME_PRONONCE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJBaseIndemnisation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJBaseIndemnisation.FIELDNAME_IDPRONONCE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononce.TABLE_NAME_PRONONCE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrononce.FIELDNAME_ID_PRONONCE);

        // jointure entre tables prononces et demande
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononce.TABLE_NAME_PRONONCE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrononce.FIELDNAME_ID_DEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDDEMANDE);

        // jointure avec ijcalculee
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJIJCalculee.TABLE_NAME_IJ_CALCULEE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrestation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrestation.FIELDNAME_ID_IJCALCULEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJIJCalculee.TABLE_NAME_IJ_CALCULEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJIJCalculee.FIELDNAME_ID_IJ_CALCULEE);

        // jointure avec grandeIJCalculee
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJGrandeIJCalculee.TABLE_NAME_GRANDE_IJ_CALCULEE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJIJCalculee.TABLE_NAME_IJ_CALCULEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJIJCalculee.FIELDNAME_ID_IJ_CALCULEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJGrandeIJCalculee.TABLE_NAME_GRANDE_IJ_CALCULEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJGrandeIJCalculee.FIELDNAME_ID_GRANDE_IJ_CALCULEE);

        // jointure avec petiteIJCalculee
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPetiteIJCalculee.TABLE_NAME_PETITE_IJ_CALCULEE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJIJCalculee.TABLE_NAME_IJ_CALCULEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJIJCalculee.FIELDNAME_ID_IJ_CALCULEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPetiteIJCalculee.TABLE_NAME_PETITE_IJ_CALCULEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPetiteIJCalculee.FIELDNAME_ID_IJ_CALCULEE_PETITE);

        return fromClauseBuffer.toString();
    }

    private String allocationPersonneSeule = "";
    private String codeVersement = "";
    private String csEtatCivil = "";
    private String csGenreReadaptation = "";
    private String csMotifInterruption = "";
    private String csTypeBaseIJCalculee = "";
    private String csTypeIJ = "";
    private String csTypePrestation = "";
    private String dateDebut = "";
    private String droitAcquis = "";
    private String fields = null;
    private String fromClause = null;
    private Boolean garantieAAReduite = Boolean.FALSE;
    private String idAnnonce = "";
    private String idIJCalculee = "";
    private String idPrestation = "";
    private String idTiersPrononce = "";
    private IJIndemniteJournaliere indemnitesJournalieresExternes = null;
    private IJIndemniteJournaliere indemnitesJournalieresInternes = null;
    private Boolean isDroitPrestationPourEnfant = Boolean.FALSE;
    private String montantAllocationAssistance = "";
    private String montantAllocationExploitation = "";
    private String montantBrutExterne = "";
    private String montantBrutInterne = "";
    private String montantBrutPrestation = "";
    private String montantGarantieAA = "";
    private String nbJoursExternes = "";
    private String nbJoursInternes = "";
    private String noAssureConjoint = "";
    private String noAVSPrononce = "";
    private String noDecisionAiCommunication = "";
    private String nombreEnfants = "";
    private String nombreJoursInterruption = "";
    private String noRevision = "";

    private String officeAI = "";
    private String periodeA = "";

    private String periodeDe = "";
    private String revenuMoyenDeterminantNonPlafonne = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String tauxJournalierExterne = "";

    private String tauxJournalierInterne = "";

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        noAVSPrononce = PRTiersHelper.getTiersParId(getSession(), idTiersPrononce).getProperty(
                PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

        // recup des indemnités journalieres
        IJIndemniteJournaliereManager indemniteJournaliereManager = new IJIndemniteJournaliereManager();
        indemniteJournaliereManager.setSession(getSession());
        indemniteJournaliereManager.setForIdIJCalculee(idIJCalculee);
        indemniteJournaliereManager.find(transaction);

        if (indemniteJournaliereManager.isEmpty()) {
            throw new Exception(getSession().getLabel("IJ_NON_TROUVEE") + noAVSPrononce + " idIJCalculee = "
                    + idIJCalculee);
        }

        IJIndemniteJournaliere indemniteJournaliere = null;

        for (int i = 0; i < indemniteJournaliereManager.size(); i++) {
            indemniteJournaliere = (IJIndemniteJournaliere) indemniteJournaliereManager.getEntity(i);

            if (indemniteJournaliere.getCsTypeIndemnisation().equals(IIJMesure.CS_INTERNE)) {
                indemnitesJournalieresInternes = indemniteJournaliere;
            } else if (indemniteJournaliere.getCsTypeIndemnisation().equals(IIJMesure.CS_EXTERNE)) {
                indemnitesJournalieresExternes = indemniteJournaliere;
            } else {
                // les IJ des prononce AIT ne sont pas type
                // throw new Exception("type d'indemnité journalière non connu "
                // + noAVSPrononce + " idIJCalculee = " + idIJCalculee);
            }
        }

        indemniteJournaliereManager = null;
        indemniteJournaliere = null;

        // etatCivil et noAssureConjoint
        ISFSituationFamiliale situationFamiliale = SFSituationFamilialeFactory.getSituationFamiliale(getSession(),
                ISFSituationFamiliale.CS_DOMAINE_INDEMNITEE_JOURNALIERE, idTiersPrononce);

        ISFMembreFamilleRequerant[] membreFamilleRequerants = situationFamiliale.getMembresFamilleRequerant(
                idTiersPrononce, dateDebut);

        if (membreFamilleRequerants == null) {
            throw new Exception("Le requérant n'a pas été trouvé dans les familles. idTiersPrononcé/idPrestation = "
                    + idTiersPrononce + "/" + getIdPrestation());
        }
        ISFMembreFamilleRequerant membreFamilleRequerant = null;

        // on garde le sexe pour avoir le numero AVS du conjoint plus tard
        String idMembreFamilleRequerant = null;

        for (int i = 0; i < membreFamilleRequerants.length; i++) {
            membreFamilleRequerant = membreFamilleRequerants[i];

            if (ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT
                    .equals(membreFamilleRequerant.getRelationAuRequerant())) {
                ISFMembreFamille membreFamille = situationFamiliale.getMembreFamille(
                        membreFamilleRequerant.getIdMembreFamille(), dateDebut);

                csEtatCivil = membreFamille.getCsEtatCivil();
                idMembreFamilleRequerant = membreFamilleRequerant.getIdMembreFamille();

                break;
            }
        }

        ISFRelationFamiliale[] relationFamiliales = situationFamiliale
                .getRelationsConjoints(idTiersPrononce, dateDebut);

        // parcours des relations familiales pour déterminer s'il est marié avec
        // une personne
        for (int i = 0; i < relationFamiliales.length; i++) {
            if (relationFamiliales[i].getTypeLien().equals(ISFSituationFamiliale.CS_TYPE_LIEN_MARIE)
                    || relationFamiliales[i].getTypeLien().equals(ISFSituationFamiliale.CS_TYPE_LIEN_LPART_ENREGISTRE)) {
                if (relationFamiliales[i].getIdMembreFamilleFemme().equals(idMembreFamilleRequerant)) {
                    noAssureConjoint = relationFamiliales[i].getNoAvsHomme();
                } else {
                    noAssureConjoint = relationFamiliales[i].getNoAvsFemme();
                }
            }
            // BZ 9926
            break;
        }

        // codeVersement
        IJRepartitionPaiementsManager repartitionPaiementsManager = new IJRepartitionPaiementsManager();
        repartitionPaiementsManager.setSession(getSession());
        repartitionPaiementsManager.setForIdPrestation(idPrestation);
        repartitionPaiementsManager.setForParentOnly(Boolean.TRUE.toString());
        repartitionPaiementsManager.find(transaction);

        boolean hasVersementAssure = false;
        boolean hasVersementEmployeurOuAgentExecution = false;
        IJRepartitionPaiements repartitionPaiements = null;

        for (int i = 0; i < repartitionPaiementsManager.size(); i++) {
            repartitionPaiements = (IJRepartitionPaiements) repartitionPaiementsManager.getEntity(i);

            if ((repartitionPaiements.getTypePaiement().equals(IIJRepartitionPaiements.CS_PAIEMENT_EMPLOYEUR))
                    || repartitionPaiements.getTypePaiement().equals(
                            IIJRepartitionPaiements.CS_PAIEMENT_AGENT_EXECUTION)) {

                // Il est possible que le montant versé au bénéficiaire soit
                // égal à zéro, principalement dans le cas de l'assuré,
                // car l'assuré n'est jamais supprimé de la répartition.
                if (!JadeStringUtil.isDecimalEmpty(repartitionPaiements.getMontantBrut())) {
                    hasVersementEmployeurOuAgentExecution = true;
                }
            } else {
                if (!JadeStringUtil.isDecimalEmpty(repartitionPaiements.getMontantBrut())) {
                    hasVersementAssure = true;
                }
            }
        }

        if (hasVersementAssure) {
            if (hasVersementEmployeurOuAgentExecution) {
                codeVersement = IIJAnnonce.VERSEMENT_REPARTI;
            } else {
                codeVersement = IIJAnnonce.VERSEMENT_ASSURE;
            }
        } else {
            codeVersement = IIJAnnonce.VERSEMENT_EMPLOYEUR_CENTRE_READAPTATION;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_allowDelete()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_allowUpdate()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFields(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFields(BStatement statement) {
        if (fields == null) {
            fields = createFields();
        }

        return fields;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return IJPrestation.TABLE_NAME;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        dateDebut = statement.dbReadDateAMJ(IJPrestation.FIELDNAME_DATEDEBUT);
        csTypePrestation = statement.dbReadNumeric(IJPrestation.FIELDNAME_CS_TYPE);
        idTiersPrononce = statement.dbReadNumeric(PRDemande.FIELDNAME_IDTIERS);
        revenuMoyenDeterminantNonPlafonne = statement.dbReadNumeric(IJIJCalculee.FIELDNAME_REVENU_DETERMINANT, 2);
        allocationPersonneSeule = statement.dbReadNumeric(IJIJCalculee.FIELDNAME_SUPPLEMENT_PERSONNE_SEULE, 2);
        nombreEnfants = statement.dbReadNumeric(IJGrandeIJCalculee.FIELDNAME_NB_ENFANTS);
        csTypeBaseIJCalculee = statement.dbReadNumeric(IJIJCalculee.FIELDNAME_CS_TYPE_BASE);
        montantAllocationExploitation = statement.dbReadNumeric(
                IJGrandeIJCalculee.FIELDNAME_MONTANT_INDEMNITE_EXPLOITATION, 2);
        montantAllocationAssistance = statement.dbReadNumeric(
                IJGrandeIJCalculee.FIELDNAME_MONTANT_INDEMNITE_ASSISTANCE, 2);
        csTypeIJ = statement.dbReadNumeric(IJPrononce.FIELDNAME_CS_TYPE_IJ);
        idIJCalculee = statement.dbReadNumeric(IJIJCalculee.FIELDNAME_ID_IJ_CALCULEE);
        montantBrutPrestation = statement.dbReadNumeric(IJPrestation.FIELDNAME_MONTANTBRUT);
        periodeDe = statement.dbReadDateAMJ(IJBaseIndemnisation.FIELDNAME_DATEDEBUTPERIODE);
        periodeA = statement.dbReadDateAMJ(IJBaseIndemnisation.FIELDNAME_DATEFINPERIODE);
        tauxJournalierExterne = statement.dbReadNumeric(IJPrestation.FIELDNAME_MONTANT_JOURNALIER_EXT, 2);
        tauxJournalierInterne = statement.dbReadNumeric(IJPrestation.FIELDNAME_MONTANT_JOURNALIER_INT, 2);
        montantBrutInterne = statement.dbReadNumeric(IJPrestation.FIELDNAME_MONTANTBRUTINTERNE, 2);
        montantBrutExterne = statement.dbReadNumeric(IJPrestation.FIELDNAME_MONTANTBRUTEXTERNE, 2);
        nbJoursExternes = statement.dbReadNumeric(IJPrestation.FIELDNAME_NOMBRE_JOURS_EXT);
        nbJoursInternes = statement.dbReadNumeric(IJPrestation.FIELDNAME_NOMBRE_JOURS_INT);
        csGenreReadaptation = statement.dbReadNumeric(IJPrononce.FIELDNAME_CS_GENRE);
        montantGarantieAA = statement.dbReadNumeric(IJPrononce.FIELDNAME_MONTANT_GARANTI_AA, 2);
        garantieAAReduite = statement.dbReadBoolean(IJPrononce.FIELDNAME_MONTANT_GARANTI_AA_REDUIT);
        idPrestation = statement.dbReadNumeric(IJPrestation.FIELDNAME_IDPRESTATION);
        droitAcquis = statement.dbReadNumeric(IJPrestation.FIELDNAME_DROITACQUIS, 2);
        nombreJoursInterruption = statement.dbReadNumeric(IJBaseIndemnisation.FIELDNAME_NOMBREJOURSINTERRUPTION);
        csMotifInterruption = statement.dbReadNumeric(IJBaseIndemnisation.FIELDNAME_MOTIFINTERRUPTION);
        officeAI = statement.dbReadNumeric(IJPrononce.FIELDNAME_OFFICE_AI);
        idAnnonce = statement.dbReadNumeric(IJPrestation.FIELDNAME_IDANNONCE);
        noDecisionAiCommunication = statement.dbReadNumeric(IJPrononce.FIELDNAME_NO_DECISION_AI);
        noRevision = statement.dbReadString(IJIJCalculee.FIELDNAME_NO_REVISION);
        isDroitPrestationPourEnfant = statement.dbReadBoolean(IJIJCalculee.FIELDNAME_DROIT_PRESTATION_ENFANT);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // lecture seule
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    /**
     * getter pour l'attribut allocation personne seule
     * 
     * @return la valeur courante de l'attribut allocation personne seule
     */
    public String getAllocationPersonneSeule() {
        return allocationPersonneSeule;
    }

    /**
     * getter pour l'attribut code versement
     * 
     * @return la valeur courante de l'attribut code versement
     */
    public String getCodeVersement() {
        return codeVersement;
    }

    /**
     * getter pour l'attribut cs etat civil
     * 
     * @return la valeur courante de l'attribut cs etat civil
     */
    public String getCsEtatCivil() {
        return csEtatCivil;
    }

    /**
     * getter pour l'attribut cs genre readaptation
     * 
     * @return la valeur courante de l'attribut cs genre readaptation
     */
    public String getCsGenreReadaptation() {
        return csGenreReadaptation;
    }

    /**
     * getter pour l'attribut motif interruption
     * 
     * @return la valeur courante de l'attribut motif interruption
     */
    public String getCsMotifInterruption() {
        return csMotifInterruption;
    }

    /**
     * getter pour l'attribut cs type base IJCalculee
     * 
     * @return la valeur courante de l'attribut cs type base IJCalculee
     */
    public String getCsTypeBaseIJCalculee() {
        return csTypeBaseIJCalculee;
    }

    /**
     * getter pour l'attribut cs type IJ
     * 
     * @return la valeur courante de l'attribut cs type IJ
     */
    public String getCsTypeIJ() {
        return csTypeIJ;
    }

    /**
     * getter pour l'attribut cs type prestation
     * 
     * @return la valeur courante de l'attribut cs type prestation
     */
    public String getCsTypePrestation() {
        return csTypePrestation;
    }

    /**
     * getter pour l'attribut date debut
     * 
     * @return la valeur courante de l'attribut date debut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * getter pour l'attribut droit acquis
     * 
     * @deprecated Remplacé par le champ noRevisionGaranti dans IJCalcul. Utilisé pour la génération des annonces
     * @return la valeur courante de l'attribut droit acquis
     */
    @Deprecated
    public String getDroitAcquis() {
        return droitAcquis;
    }

    /**
     * getter pour l'attribut garantie AAReduite
     * 
     * @return la valeur courante de l'attribut garantie AAReduite
     */
    public Boolean getGarantieAAReduite() {
        return garantieAAReduite;
    }

    /**
     * getter pour l'attribut id annonce
     * 
     * @return la valeur courante de l'attribut id annonce
     */
    public String getIdAnnonce() {
        return idAnnonce;
    }

    /**
     * getter pour l'attribut id IJCalculee
     * 
     * @return la valeur courante de l'attribut id IJCalculee
     */
    public String getIdIJCalculee() {
        return idIJCalculee;
    }

    /**
     * getter pour l'attribut id prestation
     * 
     * @return la valeur courante de l'attribut id prestation
     */
    public String getIdPrestation() {
        return idPrestation;
    }

    /**
     * getter pour l'attribut id tiers prononce
     * 
     * @return la valeur courante de l'attribut id tiers prononce
     */
    public String getIdTiersPrononce() {
        return idTiersPrononce;
    }

    /**
     * null si n'existe pas
     * 
     * @return DOCUMENT ME!
     */
    public IJIndemniteJournaliere getIndemnitesJournalieresExternes() {
        return indemnitesJournalieresExternes;
    }

    /**
     * null si n'existe pas
     * 
     * @return DOCUMENT ME!
     */
    public IJIndemniteJournaliere getIndemnitesJournalieresInternes() {
        return indemnitesJournalieresInternes;
    }

    public Boolean getIsDroitPrestationPourEnfant() {
        return isDroitPrestationPourEnfant;
    }

    /**
     * getter pour l'attribut montant brut externe
     * 
     * @return la valeur courante de l'attribut montant brut externe
     */
    public String getMontantBrutExterne() {
        return montantBrutExterne;
    }

    /**
     * getter pour l'attribut montant brut interne
     * 
     * @return la valeur courante de l'attribut montant brut interne
     */
    public String getMontantBrutInterne() {
        return montantBrutInterne;
    }

    /**
     * getter pour l'attribut montant brut prestation
     * 
     * @return la valeur courante de l'attribut montant brut prestation
     */
    public String getMontantBrutPrestation() {
        return montantBrutPrestation;
    }

    /**
     * getter pour l'attribut montant garantie AA
     * 
     * @return la valeur courante de l'attribut montant garantie AA
     */
    public String getMontantGarantieAA() {
        return montantGarantieAA;
    }

    /**
     * getter pour l'attribut nb jours externes
     * 
     * @return la valeur courante de l'attribut nb jours externes
     */
    public String getNbJoursExternes() {
        return nbJoursExternes;
    }

    /**
     * getter pour l'attribut nb jours internes
     * 
     * @return la valeur courante de l'attribut nb jours internes
     */
    public String getNbJoursInternes() {
        return nbJoursInternes;
    }

    /**
     * getter pour l'attribut getno assure conjoint
     * 
     * @return la valeur courante de l'attribut getno assure conjoint
     */
    public String getNoAssureConjoint() {
        return noAssureConjoint;
    }

    /**
     * getter pour l'attribut no AVSPrononce
     * 
     * @return la valeur courante de l'attribut no AVSPrononce
     */
    public String getNoAVSPrononce() {
        return noAVSPrononce;
    }

    public String getNoDecisionAiCommunication() {
        return noDecisionAiCommunication;
    }

    /**
     * getter pour l'attribut nombre enfants
     * 
     * @return la valeur courante de l'attribut nombre enfants
     */
    public String getNombreEnfants() {
        return nombreEnfants;
    }

    /**
     * getter pour l'attribut nombre jours interruption
     * 
     * @return la valeur courante de l'attribut nombre jours interruption
     */
    public String getNombreJoursInterruption() {
        return nombreJoursInterruption;
    }

    public String getNoRevision() {
        return noRevision;
    }

    /**
     * getter pour l'attribut office AI
     * 
     * @return la valeur courante de l'attribut office AI
     */
    public String getOfficeAI() {
        return officeAI;
    }

    /**
     * getter pour l'attribut periode a
     * 
     * @return la valeur courante de l'attribut periode a
     */
    public String getPeriodeA() {
        return periodeA;
    }

    /**
     * getter pour l'attribut periode de
     * 
     * @return la valeur courante de l'attribut periode de
     */
    public String getPeriodeDe() {
        return periodeDe;
    }

    /**
     * getter pour l'attribut revenu moyen determinant non plafonne
     * 
     * @return la valeur courante de l'attribut revenu moyen determinant non plafonne
     */
    public String getRevenuMoyenDeterminantNonPlafonne() {
        return revenuMoyenDeterminantNonPlafonne;
    }

    /**
     * getter pour l'attribut taux journalier externe
     * 
     * @return la valeur courante de l'attribut taux journalier externe
     */
    public String getTauxJournalierExterne() {
        return tauxJournalierExterne;
    }

    /**
     * getter pour l'attribut taux journalier interne
     * 
     * @return la valeur courante de l'attribut taux journalier interne
     */
    public String getTauxJournalierInterne() {
        return tauxJournalierInterne;
    }

    /**
     * getter pour l'attribut allocation assistance
     * 
     * @return la valeur courante de l'attribut allocation assistance
     */
    public boolean isAllocationAssistance() {
        return !JadeStringUtil.isDecimalEmpty(montantAllocationAssistance);
    }

    /**
     * getter pour l'attribut allocation exploitation
     * 
     * @return la valeur courante de l'attribut allocation exploitation
     */
    public boolean isAllocationExploitation() {
        return !JadeStringUtil.isDecimalEmpty(montantAllocationExploitation);
    }

    public void setIsDroitPrestationPourEnfant(Boolean isDroitPrestationPourEnfant) {
        this.isDroitPrestationPourEnfant = isDroitPrestationPourEnfant;
    }

    public void setNoDecisionAiCommunication(String noDecisionAiCommunication) {
        this.noDecisionAiCommunication = noDecisionAiCommunication;
    }

    public void setNoRevision(String noRevision) {
        this.noRevision = noRevision;
    }
}
