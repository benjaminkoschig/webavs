/*
 * Cr�� le 23.05.2008
 */
package globaz.corvus.process;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.db.demandes.REDemandeRenteJointDemandeManager;
import globaz.corvus.db.demandes.REDemandeRenteVieillesse;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteJoinAjour;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteJoinAjourManager;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.corvus.utils.enumere.genre.prestations.REGenrePrestationEnum;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWLog;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFRelationFamiliale;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFMembreFamilleJointPeriode;
import globaz.hera.db.famille.SFMembreFamilleJointPeriodeManager;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.hera.impl.rentes.SFSituationFamiliale;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.enums.CommunePolitique;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRDateFormater;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;

/**
 * <H1>Description</H1>
 * 
 * Classe ex�cut�e depuis LYRA (Ech�ances) pour cr�er un document pdf listant les �ch�ances des rentes selon les
 * crit�res d�crit dans le document "x:\Clients\WebPrestation\analyse\INFOROM\Echeance.doc"
 * 
 * @author jje
 */
public class REListerEcheancesProcess extends FWIAbstractManagerDocumentList {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final int AGE_AVS_FEMME = 64;
    public static final int AGE_AVS_HOMME = 65;

    public static final String GENRE_PRESTATION_VEUF = "13";
    // Motifs qui sont affich�s dans la liste des �ch�ances
    public static final String MOTIF_18_ANS = "18 ans";
    public static final String MOTIF_25_ANS = "25 ans";
    public static final String MOTIF_AGE_VIEILLESSE = "Age vieillesse";
    public static final String MOTIF_AGE_VIEILLESSE_API = "Age vieillesse api";
    // Motifs qui sont utilis�s pour d�terminer quels documents d'�ch�ances g�n�rer
    public static final String MOTIF_AGE_VIEILLESSE_CONJOINT = "Age vieillesse conjoint";
    public static final String MOTIF_AUTRES_ECHEANCE = "Autres �ch�ances";
    public static final String MOTIF_F_AGE_VIEILLESSE = "Femme en �ge AVS";
    public static final String MOTIF_F_AGE_VIEILLESSE_ANTICIPE = "Femme en �ge AVS - Rente anticip�e";
    public static final String MOTIF_FIN_AJOURNEMENT = "Fin d'ajournement";
    public static final String MOTIF_FIN_ETUDES = "Fin d'�tudes";

    public static final String MOTIF_H_AGE_VIEILLESSE = "Homme en �ge AVS";
    public static final String MOTIF_H_AGE_VIEILLESSE_ANTICIPE = "Homme en age AVS - Rente anticip�e";
    public static final String MOTIF_NON_DEFINIE = "Ech�ance non d�finie";
    public static final String MOTIF_RENTE_DE_VEUF_SANS_ENFANTS = "Rente de veuf sans enfant";
    public static final String MOTIF_RENTE_DE_VEUF_SUPPRESSION = "Suppression de la rente de veuf";

    public static final String MOTIF_RENTE_VEUF = "Veuf";
    public static final String TITRE_DIMINUTION = "TITRE_DOCUMENT_SUPPRESSION_ECHEANCES";
    public static final String TITRE_LISTE = "TITRE_DOCUMENT_LISTE_ECHEANCES";
    private JADate dateTraitementJad = null;
    private List echeances = new ArrayList();

    private String forAnnee = "";
    private String forMois = "";
    private Set groupGp = new HashSet();

    private Set groupNss = new HashSet();
    private boolean hasDemandeVieillesseEnCours = false;
    private boolean hasEnfants = false;

    private boolean hasRenteAPI = false;
    private String idLog = null;
    private boolean isAjournementGe = false;
    private boolean isAutreEcheanceGe = false;
    private boolean isEcheanceEtudesGe = false;
    private boolean isEnfantDe18AnsGe = false;

    private boolean isEnfantDe25AnsGe = false;
    private boolean isFemmmeArrivantAgeVieillesseGe = false;
    private boolean isHommeArrivantAgeVieillesseGe = false;
    private boolean isRenteDeVeufGe = false;
    private boolean isRentevieillesseAvecAPI = false;
    private boolean isRenteVieillesseStandard = false;
    private boolean isSuppression = false;
    public FWMemoryLog journalLog = new FWMemoryLog();
    private ISFMembreFamille MembreFamille = null;
    private int nbCurrentRows = 0;
    private int nbDocRows = 0;
    private RERenteAccJoinTblTiersJoinDemRenteJoinAjour oldEntity = null;
    private String oldMotif = "";
    private SFMembreFamilleJointPeriodeManager sfmembrefamille = null;

    private ISFSituationFamiliale situationFamiliale = null;

    public REListerEcheancesProcess() {
        super();
    }

    /**
     * Constructeur
     * 
     * @param session
     */
    public REListerEcheancesProcess(BProcess parent) throws Exception {
        this();
        super.setParentWithCopy(parent);
    }

    /**
     * Constructeur
     * 
     * @param session
     */
    public REListerEcheancesProcess(BSession session) {
        super(session, "PRESTATIONS", "GLOBAZ", session.getLabel(REListerEcheancesProcess.TITRE_LISTE),
                new RERenteAccJoinTblTiersJoinDemRenteJoinAjourManager(), REApplication.DEFAULT_APPLICATION_CORVUS);

    }

    public REListerEcheancesProcess(BSession session, String titre) {
        super(session, "PRESTATIONS", "GLOBAZ", titre, new RERenteAccJoinTblTiersJoinDemRenteJoinAjourManager(),
                REApplication.DEFAULT_APPLICATION_CORVUS);
    }

    public REListerEcheancesProcess(BSession session, String filenameRoot, String companyName, String documentTitle,
            BManager manager) {
        super(session, filenameRoot, companyName, documentTitle, manager);
    }

    public REListerEcheancesProcess(BSession session, String filenameRoot, String companyName, String documentTitle,
            BManager manager, String applicationId) {
        super(session, filenameRoot, companyName, documentTitle, manager, applicationId);
    }

    public REListerEcheancesProcess(String filenameRoot, String companyName, String documentTitle, String applicationId) {
        super(filenameRoot, companyName, documentTitle, applicationId);
    }

    /**
     * M�thode appel�e avant l'ex�cution du rapport
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractDocumentList#_beforeExecuteReport()
     **/

    @Override
    public void _beforeExecuteReport() {

        try {

            JACalendarGregorian jaCalGre = new JACalendarGregorian();

            dateTraitementJad = jaCalGre.addDays(
                    jaCalGre.addMonths(new JADate("01."
                            + (getForMois().length() < 2 ? "0" + getForMois() + "." + getForAnnee() : getForMois()
                                    + "." + getForAnnee())), 1), -1);

            // getDocumentInfo().setDocumentTypeNumber(IPRConstantesExternes.ECHEANCES_ENFANT_18_ANS);

            RERenteAccJoinTblTiersJoinDemRenteJoinAjourManager manager = (RERenteAccJoinTblTiersJoinDemRenteJoinAjourManager) _getManager();
            manager.setSession(getSession());

            // Tri par nom, pr�nom, num AVS
            manager.setOrderBy(IPRConstantesExternes.FIELDNAME_TABLE_TIERS_NOM + " , "
                    + IPRConstantesExternes.FIELDNAME_TABLE_TIERS_PRENOM + " , "
                    + IPRConstantesExternes.FIELDNAME_TABLE_AVS_NUM_AVS + " DESC ");

            // La personne doit �tre en vie
            manager.setDateDecesVide(true);

            _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                    ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));

            if (isSuppression()) {
                _setDocumentTitle(getSession().getLabel(REListerEcheancesProcess.TITRE_DIMINUTION)
                        + " "
                        + getSession().getLabel("TITRE_AU_LISTE_ECHEANCES")
                        + (dateTraitementJad.getMonth() < 10 ? "0"
                                + new Integer(dateTraitementJad.getMonth()).toString() : new Integer(
                                dateTraitementJad.getMonth()).toString()) + "." + dateTraitementJad.getYear());
            } else {
                _setDocumentTitle(getSession().getLabel(REListerEcheancesProcess.TITRE_LISTE)
                        + " "
                        + getSession().getLabel("TITRE_AU_LISTE_ECHEANCES")
                        + (dateTraitementJad.getMonth() < 10 ? "0"
                                + new Integer(dateTraitementJad.getMonth()).toString() : new Integer(
                                dateTraitementJad.getMonth()).toString()) + "." + dateTraitementJad.getYear());
            }

        } catch (Exception e) {
            getMemoryLog()
                    .logMessage(e.toString(), FWMessage.ERREUR, "REListerEcheancesProcess - _beforeExecuteReport");
            abort();
        }

        super._beforeExecuteReport();
    }

    @Override
    protected void _validate() throws Exception {

        if ((getEMailAddress() == null) || getEMailAddress().equals("")) {

            journalLog.logMessage(getSession().getLabel("EMAIL_NON_RENSEIGNE"), FWMessage.ERREUR,
                    "REListerEcheancesProcess");
            getMemoryLog().logMessage(getSession().getLabel("EMAIL_NON_RENSEIGNE"), FWMessage.ERREUR,
                    "REListerEcheancesProcess");
            setSendCompletionMail(false);
            setSendMailOnError(true);
        }

        if (getSession().hasErrors()) {

            journalLog.logMessage(getSession().getErrors().toString(), FWMessage.ERREUR, "REListerEcheancesProcess");
            getMemoryLog()
                    .logMessage(getSession().getErrors().toString(), FWMessage.ERREUR, "REListerEcheancesProcess");
            setSendCompletionMail(false);
            setSendMailOnError(true);
        }

        if (!journalLog.hasMessages()) {

            journalLog.logMessage(getSession().getLabel("EXECUTION_OK"), FWMessage.INFORMATION, "");
        }

        if (journalLog.hasMessages()) {

            FWLog log = journalLog.saveToFWLog(getTransaction());
            setIdLog(log.getIdLog());
        }

        super._validate();
    }

    @Override
    protected void addRow(BEntity value) throws FWIException {

        RERenteAccJoinTblTiersJoinDemRenteJoinAjour entity = (RERenteAccJoinTblTiersJoinDemRenteJoinAjour) value;

        // Je set les attributs � false et null pour ne pas garder les propri�t�s de l'entity pr�c�dente
        setRentevieillesseAvecAPI(false);
        setRenteVieillesseStandard(false);
        setHasRenteAPI(false);
        setMembreFamille(null);
        setSituationFamiliale(null);
        setSfmembrefamille(null);

        nbCurrentRows++;

        try {
            if (entity != null) {
                // Ajournement ?
                if (isAjournementGe && isDemandeAjournee(entity)
                        && JadeStringUtil.isBlank(entity.getDateRevocationAjournement())
                        && isDebutDroitPlus5ansEgaleDt(entity)) {

                    preparerAjoutLigne(entity, REListerEcheancesProcess.MOTIF_FIN_AJOURNEMENT, true, true);
                    setNbDocRows(getNbDocRows() + 1);

                } else {
                    // Si pas d'ajournement on passe au cas Rente de veuf
                    if (isRenteDeVeufGe && PRACORConst.CS_HOMME.equals(entity.getSexe())
                            && REListerEcheancesProcess.GENRE_PRESTATION_VEUF.equals(entity.getGenreDePrestation())) {

                        // Cette m�thode va aussi m'indiquer si le tiers � des enfants ou non
                        if (hasEnfantOuHasDernierEnfant18AnsDansDt(entity)) {
                            if (hasRenteAccEnfantEnCours(entity)) {
                                preparerAjoutLigne(entity, REListerEcheancesProcess.MOTIF_RENTE_DE_VEUF_SUPPRESSION,
                                        true, true);
                                setNbDocRows(getNbDocRows() + 1);
                            }
                        } else if (!isHasEnfants()) {
                            preparerAjoutLigne(entity, REListerEcheancesProcess.MOTIF_RENTE_DE_VEUF_SANS_ENFANTS, true,
                                    false);
                        }
                    } else {

                        // 18, 25 ans, �tude, �ch�ance non d�finie
                        if (!PRTiersHelper.isRentier(getSession(), entity.getIdTiersBeneficiaire(),
                                dateTraitementJad.toStr("."))) {

                            if ((isEnfantDe18AnsGe || isEnfantDe25AnsGe || isEcheanceEtudesGe)
                                    && REGenrePrestationEnum.groupeEnfant.contains(entity.getGenreDePrestation())
                                    && isRenteEnCours(entity) && !hasPeriodesEtudeDansMoisDeTraitement(entity)) {

                                if (isEnfantDe18AnsGe && isNAnsDansMoisDeTraitement(entity.getDateNaissance(), 18)) {

                                    preparerAjoutLigne(entity, REListerEcheancesProcess.MOTIF_18_ANS, true, true);
                                    setNbDocRows(getNbDocRows() + 1);

                                } else {
                                    if (isEnfantDe25AnsGe && isNAnsDansMoisDeTraitement(entity.getDateNaissance(), 25)) {

                                        preparerAjoutLigne(entity, REListerEcheancesProcess.MOTIF_25_ANS, true, true);
                                        setNbDocRows(getNbDocRows() + 1);

                                    } else {
                                        if (isEcheanceEtudesGe && isEntre18et25AnsExclusif(entity)
                                                && isFinEtudeDansDT(entity)) {

                                            preparerAjoutLigne(entity, REListerEcheancesProcess.MOTIF_FIN_ETUDES, true,
                                                    true);
                                            setNbDocRows(getNbDocRows() + 1);

                                        } else {// Si rente accord�e mais aucune de celle-ci dessus, rente non d�finie
                                            if (isEntre18et25AnsExclusif(entity) && hasNotPeriodeEtude(entity)) {
                                                preparerAjoutLigne(entity, REListerEcheancesProcess.MOTIF_NON_DEFINIE,
                                                        true, false);
                                                setNbDocRows(getNbDocRows() + 1);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            // Si �ge de vieillesse
                            if ((isHommeArrivantAgeVieillesseGe || isFemmmeArrivantAgeVieillesseGe)
                                    && (isRenteEnCours(entity))) {

                                if (isHommeArrivantAgeVieillesseGe && PRACORConst.CS_HOMME.equals(entity.getSexe())) {
                                    if (isNAnsDansMoisDeTraitement(entity.getDateNaissance(),
                                            REListerEcheancesProcess.AGE_AVS_HOMME)) {
                                        if (asDemandeVieillesseAnticipe(entity.getIdTierRequerant())) {
                                            // Si demande anticip�e, je liste mais n'�cris pas
                                            preparerAjoutLigne(entity,
                                                    REListerEcheancesProcess.MOTIF_H_AGE_VIEILLESSE_ANTICIPE, true,
                                                    false);
                                        } else if (hasDemandeAISurvivantAPIEnCours(entity.getIdTierRequerant())
                                                && !isHasDemandeVieillesseEnCours()) {
                                            if (isHasRenteAPI()) {
                                                setRentevieillesseAvecAPI(true);
                                            } else {
                                                setRenteVieillesseStandard(true);
                                            }

                                            preparerAjoutLigne(entity, REListerEcheancesProcess.MOTIF_H_AGE_VIEILLESSE,
                                                    true, true);
                                            setNbDocRows(getNbDocRows() + 1);
                                        }
                                    } else if (isMarier(entity)) {
                                        findConjoint(entity);
                                        if (isConjointAgeVieillesse()) {
                                            // Je test si le conjoint � une rente accord�e en cours, si c'est le cas, je
                                            // m'arr�te,
                                            // si ce n'est pas le cas, je traite la suite
                                            if (!asConjointRenteAccordeeEnCours()) {
                                                if (!hasDemandeAISurvivantAPIEnCours(getMembreFamille().getIdTiers())) {
                                                    if (asDemandeVieillesseAnticipe(getMembreFamille().getIdTiers())) {
                                                        // Si demande anticip�e, je liste mais n'�cris pas
                                                        preparerAjoutLigne(
                                                                entity,
                                                                REListerEcheancesProcess.MOTIF_F_AGE_VIEILLESSE_ANTICIPE,
                                                                false, false);
                                                    } else if (!isHasDemandeVieillesseEnCours()) {
                                                        // le conjoint n'a pas de demande de vieillesse, on le met dans
                                                        // la liste
                                                        // et on g�n�re la lettre d'�ch�ance
                                                        preparerAjoutLigne(entity,
                                                                REListerEcheancesProcess.MOTIF_F_AGE_VIEILLESSE, false,
                                                                true);
                                                        // comme on genere le doc, j'incr�mente le nombre de doc �
                                                        // generer
                                                        setNbDocRows(getNbDocRows() + 1);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (isFemmmeArrivantAgeVieillesseGe
                                            && PRACORConst.CS_FEMME.equals(entity.getSexe())) {
                                        if (isNAnsDansMoisDeTraitement(entity.getDateNaissance(),
                                                REListerEcheancesProcess.AGE_AVS_FEMME)) {
                                            if (asDemandeVieillesseAnticipe(entity.getIdTierRequerant())) {
                                                // Si demande anticip�e, je liste mais n'�cris pas
                                                preparerAjoutLigne(entity,
                                                        REListerEcheancesProcess.MOTIF_F_AGE_VIEILLESSE_ANTICIPE, true,
                                                        false);
                                            } else if (hasDemandeAISurvivantAPIEnCours(entity.getIdTierRequerant())
                                                    && !isHasDemandeVieillesseEnCours()) {
                                                if (isHasRenteAPI()) {
                                                    setRentevieillesseAvecAPI(true);
                                                } else {
                                                    setRenteVieillesseStandard(true);
                                                }
                                                preparerAjoutLigne(entity,
                                                        REListerEcheancesProcess.MOTIF_F_AGE_VIEILLESSE, true, true);
                                                setNbDocRows(getNbDocRows() + 1);
                                            }
                                        } else if (isMarier(entity)) {
                                            findConjoint(entity);
                                            if (isConjointAgeVieillesse()) {
                                                // Je test si le conjoint � une rente accord�e en cours, si c'est le
                                                // cas, je m'arr�te,
                                                // si ce n'est pas le cas, je traite la suite
                                                if (!asConjointRenteAccordeeEnCours()) {
                                                    if (!hasDemandeAISurvivantAPIEnCours(getMembreFamille()
                                                            .getIdTiers())) {
                                                        if (asDemandeVieillesseAnticipe(getMembreFamille().getIdTiers())) {
                                                            // Si demande anticip�e, je liste mais n'�cris pas
                                                            preparerAjoutLigne(
                                                                    entity,
                                                                    REListerEcheancesProcess.MOTIF_H_AGE_VIEILLESSE_ANTICIPE,
                                                                    false, false);
                                                        } else if (!isHasDemandeVieillesseEnCours()) {
                                                            // le conjoint n'a pas de demande de vieillesse, on le met
                                                            // dans la liste
                                                            // et on g�n�re la lettre d'�ch�ance
                                                            preparerAjoutLigne(entity,
                                                                    REListerEcheancesProcess.MOTIF_H_AGE_VIEILLESSE,
                                                                    false, true);
                                                            // comme on genere le doc, j'incr�mente le nombre de doc �
                                                            // generer
                                                            setNbDocRows(getNbDocRows() + 1);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                // Autres �ch�ances
                                if (isDateDansDt(entity.getDateFinDroit())) {
                                    // pr�parerAjoutLigne(entity,MOTIF_AUTRES_ECHEANCE);
                                }
                            }
                        }
                    }
                }
            }
            // Ne pas oublier d'ajouter la derni�re ligne lorsqu'on est � la fin du manager.

            if (nbCurrentRows == _getManager().size()) {

                // Si aucune rente n'est � diminuer, le doucment n'est pas g�n�rer et un mail d'informations est envoy�
                // � l'utilisateur
                if (isSuppression()) {
                    // Compteur pour savoir si une rente doit �tre diminu�e
                    if (getNbDocRows() == 0) {
                        getMemoryLog().logMessage(
                                getSession().getLabel("ERREUR_AUCUNERENTE_RELISTERECHEANCESPROCESS") + " "
                                        + JACalendar.getMonthName(Integer.parseInt(getForMois())) + " " + getForAnnee()
                                        + "\n", FWMessage.ERREUR, "REListerEcheancesProcess");
                        getParent().abort();
                    }
                }

                this._addDataTableRow();
                incProgressCounter();

                // Utilie pour l'ajout de la derni�re ligne, car si oldMotif est renseign�e, on va ajouter dans la liste
                // la valeur de la derni�re rente accord�e trait�e, par contre, si le motif n'est pas renseign�, on ne
                // va pas l'ajouter !
                oldMotif = "";
                ajoutLigne(false, false);

            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "REListerEcheancesProcess - addRow");
            abort();
        }
    }

    private void ajouterLigne(String motif, RERenteAccJoinTblTiersJoinDemRenteJoinAjour entity,
            boolean listerInfoEntity, boolean genererLettreEcheance) {
        // Si ajout de la ligne -> nouveau tiers, on peut donc afficher le tiers pr�cedent en �tant s�r d'avoir tous les
        // genres de pr�stations
        // le concernant (le manager �tant tri� par No AVS)
        groupGp = new HashSet();
        groupGp.add(entity.getGenreDePrestation());
        oldEntity = entity;
        oldMotif = motif;
        ajoutLigne(listerInfoEntity, genererLettreEcheance);
    }

    private void ajoutLigne(boolean listerInfoEntity, boolean genererLettreEcheance) {

        if (listerInfoEntity) {
            // Ce test est utile lors du 1er appel car oldEntity=null
            if ((null != oldEntity) && !JadeStringUtil.isBlank(oldMotif)) {
                if (getAjouterCommunePolitique()) {
                    _addCell(oldEntity.getNss());
                }

                _addCell(oldEntity.getNss());
                _addCell(oldEntity.getNom() + " " + oldEntity.getPrenom());
                _addCell(oldEntity.getDateNaissance());
                _addCell(getLibelleCourtSexe(oldEntity.getSexe()));

                // On regroupe tous les genres de pr�stations sur la m�me ligne
                String genresDePrestation = "";
                Object[] tblGp = groupGp.toArray();
                for (int i = 0; i < tblGp.length; i++) {
                    if (tblGp[i] != null) {
                        if (i == 0) {
                            genresDePrestation = (String) tblGp[i];
                        } else {
                            genresDePrestation += " / " + (String) tblGp[i];
                        }
                    }
                }

                _addCell(genresDePrestation);
                _addCell(oldMotif);

                if (genererLettreEcheance) {
                    // On m�morise l'entit� pour la g�n�ration des lettres
                    if (isRentevieillesseAvecAPI()) {
                        oldEntity.setMotif(REListerEcheancesProcess.MOTIF_AGE_VIEILLESSE_API);
                    } else if (isRenteVieillesseStandard()) {
                        oldEntity.setMotif(REListerEcheancesProcess.MOTIF_AGE_VIEILLESSE);
                    } else {
                        oldEntity.setMotif(oldMotif);
                    }
                    echeances.add(oldEntity);
                }
            }
            // Dans ce cas, on m�morise les informations du conjoint
        } else {
            // Ce test est utile lors du 1er appel car oldEntity=null
            if ((null != oldEntity) && !JadeStringUtil.isBlank(oldMotif)) {
                _addCell(getMembreFamille().getNss());
                _addCell(getMembreFamille().getNom() + " " + getMembreFamille().getPrenom());
                _addCell(getMembreFamille().getDateNaissance());
                _addCell(getLibelleCourtSexe(getMembreFamille().getCsSexe()));
                _addCell("");
                _addCell(oldMotif);

                if (genererLettreEcheance) {
                    // On m�morise l'entit� pour la g�n�ration des lettres
                    oldEntity.setMotif(REListerEcheancesProcess.MOTIF_AGE_VIEILLESSE_CONJOINT);
                    echeances.add(oldEntity);
                }
            }
        }
    }

    private boolean asConjointRenteAccordeeEnCours() {

        // Cette m�thode m'indique si le conjoint � une rente accord�e en cours, si c'est le cas, je ne dois plus le
        // traiter car
        // il sera trait� de lui-m�me, par contre s'il n'a aucune rente accord�e, je dois le traiter.

        boolean asConjointRenteEnCours = false;

        try {

            RERenteAccordeeManager reRenteAccManager = new RERenteAccordeeManager();
            reRenteAccManager.setSession(getSession());
            String dateTraitementMMxAAAAStr = new Integer(dateTraitementJad.getMonth()).toString() + "."
                    + new Integer(dateTraitementJad.getYear()).toString();
            reRenteAccManager.setForEnCoursAtMois(dateTraitementJad.getMonth() < 10 ? "0" + dateTraitementMMxAAAAStr
                    : dateTraitementMMxAAAAStr);
            reRenteAccManager.setForIdTiersBeneficiaire(getMembreFamille().getIdTiers());
            reRenteAccManager.find();
            if (!reRenteAccManager.isEmpty()) {
                asConjointRenteEnCours = true;
            }

            return asConjointRenteEnCours;

        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR,
                    "REListerEcheancesProcess - asConjointRenteAccordeeEnCours");
            return false;
        }
    }

    private boolean asDemandeVieillesseAnticipe(String idTiersRequerant) {

        // Cette m�thode m'indique si un tiers a une demande de vieillesse de type calcul standard anticip�e
        boolean AsConjointDemandeVieillesseAnticipe = false;

        try {
            // Je recherche toutes les rentes en cours de type de calcul standard pour le conjoint
            REDemandeRenteJointDemandeManager demrenteMgr = new REDemandeRenteJointDemandeManager();
            demrenteMgr.setSession(getSession());
            demrenteMgr.setForIdTiersRequ(idTiersRequerant);
            demrenteMgr.setEnCours(Boolean.TRUE);
            demrenteMgr.find();

            Iterator reDemRenteAccMgrIter = demrenteMgr.iterator();
            while (reDemRenteAccMgrIter.hasNext()) {

                REDemandeRenteJointDemande redemrente = (REDemandeRenteJointDemande) reDemRenteAccMgrIter.next();

                if (null != redemrente) {
                    // je test si la rente trouv� est de type vieillesse
                    if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE.equals(redemrente.getCsTypeDemande())) {
                        REDemandeRenteVieillesse demv = new REDemandeRenteVieillesse();
                        demv.setSession(getSession());
                        demv.setIdDemandeRente(redemrente.getIdDemandeRente());
                        demv.retrieve();
                        // Si le calcul de la demande est de type standard, je test si la rente est anticip�e
                        if (IREDemandeRente.CS_TYPE_CALCUL_STANDARD.equals(demv.getCsTypeCalcul())) {
                            if (IREDemandeRente.CS_ANNEE_ANTICIPATION_1ANNEE.equals(demv.getCsAnneeAnticipation())
                                    || IREDemandeRente.CS_ANNEE_ANTICIPATION_2ANNEES.equals(demv
                                            .getCsAnneeAnticipation())) {
                                AsConjointDemandeVieillesseAnticipe = true;
                                break;
                            } else {
                                AsConjointDemandeVieillesseAnticipe = false;
                                setHasDemandeVieillesseEnCours(true);
                                break;
                            }
                        }
                    }
                }
            }

            return AsConjointDemandeVieillesseAnticipe;
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR,
                    "REListerEcheancesProcess - asDemandeVieillesseAnticipe");
            return false;
        }
    }

    private void findConjoint(RERenteAccJoinTblTiersJoinDemRenteJoinAjour entity) {

        // Cette m�thode me retourne le dernier conjoint d'un tiers

        try {
            ISFRelationFamiliale[] relfam = getSituationFamiliale().getRelationsConjoints(entity.getIdTierRequerant(),
                    null);

            // Dead code
            // for (int j = 0;relfam != null && j < relfam.length; j++) {
            // ISFRelationFamiliale rf = relfam[j];
            // if(PRACORConst.CS_FEMME.equals(entity.getSexe())){
            // setMembreFamille(getSituationFamiliale().getMembreFamille(rf.getIdMembreFamilleHomme()));
            // break;
            // }else{
            // setMembreFamille(getSituationFamiliale().getMembreFamille(rf.getIdMembreFamilleFemme()));
            // break;
            // }
            // }

            if ((relfam != null) && (relfam.length > 0)) {
                ISFRelationFamiliale rf = relfam[0];
                if (PRACORConst.CS_FEMME.equals(entity.getSexe())) {
                    setMembreFamille(getSituationFamiliale().getMembreFamille(rf.getIdMembreFamilleHomme()));
                } else {
                    setMembreFamille(getSituationFamiliale().getMembreFamille(rf.getIdMembreFamilleFemme()));
                }
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "REListerEcheancesProcess - findConjoint");
        }
    }

    public List getEcheances() {
        return echeances;
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForMois() {
        return forMois;
    }

    public String getIdLog() {
        return idLog;
    }

    public FWMemoryLog getJournalLog() {
        return journalLog;
    }

    private String getLibelleCourtSexe(String csSexe) {

        if (PRACORConst.CS_HOMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }
    }

    private ISFMembreFamille getMembreFamille() {
        return MembreFamille;
    }

    public int getNbDocRows() {
        return nbDocRows;
    }

    SFMembreFamilleJointPeriodeManager getSfmembrefamille() {
        return sfmembrefamille;
    }

    private ISFSituationFamiliale getSituationFamiliale() {
        return situationFamiliale;
    }

    private boolean hasDemandeAISurvivantAPIEnCours(String idTiersRequerant) {

        boolean hasDemandeAISurvivantAPI = false;

        try {

            REDemandeRenteJointDemandeManager demrenteMgr = new REDemandeRenteJointDemandeManager();
            demrenteMgr.setForIdTiersRequ(idTiersRequerant);
            demrenteMgr.setSession(getSession());
            demrenteMgr.setEnCours(Boolean.TRUE);
            demrenteMgr.find();

            Iterator it = demrenteMgr.iterator();

            while (it.hasNext()) {
                REDemandeRenteJointDemande rd = (REDemandeRenteJointDemande) it.next();

                if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(rd.getCsTypeDemande())) {
                    hasDemandeAISurvivantAPI = true;
                    setHasRenteAPI(true);
                    break;
                }
                if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(rd.getCsTypeDemande())
                        || IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT.equals(rd.getCsTypeDemande())) {
                    hasDemandeAISurvivantAPI = true;
                }
            }

            return hasDemandeAISurvivantAPI;

        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR,
                    "REListerEcheancesProcess - hasDemandeAISurvivantAPIEnCours");
            return false;
        }
    }

    private boolean hasEnfantOuHasDernierEnfant18AnsDansDt(RERenteAccJoinTblTiersJoinDemRenteJoinAjour entity) {

        boolean hasDernierEnfant18AnsDt = false;
        // Compteur pour savoir si le tiers a des enfants ou non
        int nbEnfant = 0;

        try {

            SFSituationFamiliale aSfSituationFam = new SFSituationFamiliale();
            aSfSituationFam.setSession(getSession());
            ISFMembreFamille[] membresFamille = aSfSituationFam.getMembresFamilleEtendue(entity.getIdMembreFamille(),
                    new Boolean(false));

            ISFMembreFamille dernierEnfant = null;
            boolean premiereIterationEnfant = true;

            for (int i = 0; i < membresFamille.length; i++) {

                ISFMembreFamille membreFamille = membresFamille[i];

                if (null != membreFamille) {
                    if (ISFSituationFamiliale.CS_TYPE_RELATION_ENFANT.equals(membreFamille.getRelationAuLiant())) {
                        nbEnfant++;
                        if (premiereIterationEnfant) {
                            premiereIterationEnfant = false;
                            dernierEnfant = membreFamille;
                        }

                        if (new Integer(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(membreFamille
                                .getDateNaissance())).intValue() > new Integer(
                                PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(dernierEnfant.getDateNaissance()))
                                .intValue()) {

                            dernierEnfant = membreFamille;
                        }
                    }
                }
            }

            if (isNAnsDansMoisDeTraitement(dernierEnfant.getDateNaissance(), 18)) {
                hasDernierEnfant18AnsDt = true;
                setMembreFamille(dernierEnfant);
            }

            if (nbEnfant > 0) {
                setHasEnfants(true);
            }

            return hasDernierEnfant18AnsDt;

        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR,
                    "REListerEcheancesProcess - hasEnfantOuHasDernierEnfant18AnsDansDt");
            return false;
        }
    }

    private boolean hasNotPeriodeEtude(RERenteAccJoinTblTiersJoinDemRenteJoinAjour entity) {

        boolean hasNotPeriodeEtude = false;
        try {

            if (getSfmembrefamille().getSize() == 0) {
                hasNotPeriodeEtude = true;
            }

            return hasNotPeriodeEtude;

        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR,
                    "REListerEcheancesProcess - hasPeriodesEtudeDansMoisDeTraitement");
            return false;
        }
    }

    private boolean hasPeriodesEtudeDansMoisDeTraitement(RERenteAccJoinTblTiersJoinDemRenteJoinAjour entity) {

        boolean isDansMoisTraitement = false;

        try {
            // On commence par tester si la personne poss�de une ou pls p�riodes d'�tudes dans le domaine des rentes
            SFMembreFamilleJointPeriodeManager sfMemFamJointPerMgr = new SFMembreFamilleJointPeriodeManager();
            sfMemFamJointPerMgr.setSession(getSession());
            sfMemFamJointPerMgr.setForTypePeriode(ISFSituationFamiliale.CS_TYPE_PERIODE_ETUDE);
            sfMemFamJointPerMgr.setForCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_RENTES);
            sfMemFamJointPerMgr.setForIdMembreFamille(entity.getIdMembreFamille());
            sfMemFamJointPerMgr.find();

            // Si ce n'est pas le cas on test avec le domaine standard
            if (sfMemFamJointPerMgr.getSize() == 0) {
                sfMemFamJointPerMgr.setForCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_STANDARD);
                sfMemFamJointPerMgr.find();
            }

            setSfmembrefamille(sfMemFamJointPerMgr);

            for (Iterator sfMemFamJointPerIter = sfMemFamJointPerMgr.iterator(); sfMemFamJointPerIter.hasNext();) {
                SFMembreFamilleJointPeriode sfMemFamJoiPer = (SFMembreFamilleJointPeriode) sfMemFamJointPerIter.next();

                if (null != sfMemFamJoiPer) {
                    // Si une date de d�but de periode d'�tude est renseign�e
                    if (!JadeStringUtil.isBlank(sfMemFamJoiPer.getDateDebut())) {
                        // Si date d�but <= date traitement
                        if (new Integer(
                                PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(sfMemFamJoiPer.getDateDebut()))
                                .intValue() <= dateTraitementJad.toAMJ().intValue()) {

                            if (JadeStringUtil.isBlank(sfMemFamJoiPer.getDateFin())) {
                                isDansMoisTraitement = true;
                            } else {
                                if (new Integer(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(sfMemFamJoiPer
                                        .getDateFin())).intValue() > dateTraitementJad.toAMJ().intValue()) {
                                    isDansMoisTraitement = true;
                                }
                            }
                        }
                    } else {
                        // Si une date de d�but de periode d'�tude n'est pas renseign�e, je regarde si la date de fin
                        // est plus grande ou �gale � la date de traitement.
                        if (!JadeStringUtil.isBlank(sfMemFamJoiPer.getDateFin())) {
                            if (new Integer(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(sfMemFamJoiPer
                                    .getDateFin())).intValue() > dateTraitementJad.toAMJ().intValue()) {
                                isDansMoisTraitement = true;
                            }
                        }
                    }
                }
            }

            return isDansMoisTraitement;

        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR,
                    "REListerEcheancesProcess - hasPeriodesEtudeDansMoisDeTraitement");
            return false;
        }
    }

    private boolean hasRenteAccEnfantEnCours(RERenteAccJoinTblTiersJoinDemRenteJoinAjour entity) {

        boolean hasRenteAccEnfant = false;

        RERenteAccordeeManager reRenteAccMgr = new RERenteAccordeeManager();
        reRenteAccMgr.setSession(getSession());
        String dateTraitementMMxAAAAStr = new Integer(dateTraitementJad.getMonth()).toString() + "."
                + new Integer(dateTraitementJad.getYear()).toString();
        reRenteAccMgr.setForEnCoursAtMois(dateTraitementJad.getMonth() < 10 ? "0" + dateTraitementMMxAAAAStr
                : dateTraitementMMxAAAAStr);
        reRenteAccMgr.setForIdTiersBeneficiaire(getMembreFamille().getIdTiers());

        try {
            reRenteAccMgr.find();

            for (Iterator reRenteAccIter = reRenteAccMgr.iterator(); reRenteAccIter.hasNext();) {
                RERenteAccordee reRenteAcc = (RERenteAccordee) reRenteAccIter.next();
                if (REGenrePrestationEnum.groupeEnfant.contains(reRenteAcc.getCodePrestation())) {
                    hasRenteAccEnfant = true;
                }
            }

            return hasRenteAccEnfant;

        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR,
                    "REListerEcheancesProcess - hasRenteAccEnfantEnCours");
            return false;
        }
    }

    /**
     * Initialise la table des donn�es
     * <p>
     * <u>Utilisation</u>:
     * <ul>
     * <li><code>_addColumn(..)</code> permet de d�clarer les colonnes
     * <li><code>_group...(..)</code> permet de d�clarer les groupages
     * </ul>
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList#initializeTable()
     **/
    @Override
    protected void initializeTable() {
        if (getAjouterCommunePolitique()) {
            this._addColumnLeft(getSession().getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_TITRE_COLONNE.getKey()),
                    1);
        }
        this._addColumnLeft(getSession().getLabel("NSS_DOCUMENT_ANALYSE_ECHEANCE"), 1);
        this._addColumnLeft(getSession().getLabel("NOM_PRENOM_DOCUMENT_ANALYSE_ECHEANCE"), 2);
        this._addColumnLeft(getSession().getLabel("DATE_NAISSANCE_DOCUMENT_ANALYSE_ECHEANCE"), 1);
        this._addColumnLeft(getSession().getLabel("SEXE_DOCUMENT_ANALYSE_ECHEANCE"), 1);
        this._addColumnLeft(getSession().getLabel("GENRE_PRESTATION_DOCUMENT_ANALYSE_ECHEANCE"), 1);
        this._addColumnLeft(getSession().getLabel("MOTIF_DOCUMENT_ANALYSE_ECHEANCE"), 2);

        _groupManual();

        // TODO
        // JAVector pojos = new JAVector();
        //
        // try {
        // BSessionUtil.initContext(getSession(), this);
        // } catch (Exception e) {
        // throw new RuntimeException(e);
        // }
        //
        // Pojo pojo = null;
        // try {
        // for (Object object : getManager().getContainer()) {
        // pojo = new Pojo((REEcheancesEntityAvecCommunePolitique) object);
        // setIdTiers.add(pojo.getIdTiersCommunePolitique());
        // pojos.add(pojo);
        // }
        // } catch (Exception e) {
        // throw new RuntimeException(e);
        // } finally {
        // BSessionUtil.stopUsingContext(this);
        // }

        // Renseigne la commune politique en fonction de l'idTiers pour chaque pojo

        // protected void ajouterCommunePolitiques();
    }

    public boolean getAjouterCommunePolitique() {
        try {
            return CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue();
        } catch (PropertiesException e) {
            throw new RuntimeException(e.toString(), e);
        }
    }

    public boolean isAjournementGe() {
        return isAjournementGe;
    }

    public boolean isAutreEcheanceGe() {
        return isAutreEcheanceGe;
    }

    private boolean isConjointAgeVieillesse() {

        // Cette m�thode m'indique si le tiers arrivera � l'�ge vieillesse dans la date de traitement (date selectionner
        // dans l'�cran des �ch�ances)

        boolean IsConjointAgeVieillesse = false;
        try {
            if (PRACORConst.CS_FEMME.equals(getMembreFamille().getCsSexe())) {
                IsConjointAgeVieillesse = isNAnsDansMoisDeTraitement(getMembreFamille().getDateNaissance(),
                        REListerEcheancesProcess.AGE_AVS_FEMME);

            } else {
                IsConjointAgeVieillesse = isNAnsDansMoisDeTraitement(getMembreFamille().getDateNaissance(),
                        REListerEcheancesProcess.AGE_AVS_HOMME);

            }
            return IsConjointAgeVieillesse;
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR,
                    "REListerEcheancesProcess - isConjointAgeVieillesse");
            return false;
        }
    }

    private boolean isDateDansDt(String dateJJxMMxAAAA) {

        try {
            JADate date = new JADate(dateJJxMMxAAAA);
            if ((date.getMonth() == dateTraitementJad.getMonth()) && (date.getYear() == dateTraitementJad.getYear())) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "REListerEcheancesProcess - isDateDansDt");
            return false;
        }
    }

    private boolean isDebutDroitPlus5ansEgaleDt(RERenteAccJoinTblTiersJoinDemRenteJoinAjour entity) {

        boolean isDebDroPlu5EgaleDt = false;

        try {

            if (!JadeStringUtil.isBlank(entity.getDateDebutDroit())) {

                JACalendarGregorian jaCalGre = new JACalendarGregorian();
                JADate dateDebutDroit = new JADate(entity.getDateDebutDroit());
                jaCalGre.addYears(dateDebutDroit, 5);

                /*
                 * if (forMois.equals((dateDebutDroit.getMonth()<10?"0"+new
                 * Integer(dateDebutDroit.getMonth()).toString(): new Integer(dateDebutDroit.getMonth()).toString())) &&
                 * forAnnee.equals(new Integer(dateDebutDroit.getYear()).toString())){
                 */

                if ((dateTraitementJad.getMonth() == dateDebutDroit.getMonth())
                        && (dateTraitementJad.getYear() == dateDebutDroit.getYear())) {

                    isDebDroPlu5EgaleDt = true;
                }
            }

            return isDebDroPlu5EgaleDt;

        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR,
                    "REListerEcheancesProcess - isDebutDroitPlus5ansEgaleDt");
            return false;
        }
    }

    private boolean isDemandeAjournee(RERenteAccJoinTblTiersJoinDemRenteJoinAjour entity) {
        if ("true".equals(entity.getIsAjournementRequerant())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isEntre18et25AnsExclusif(RERenteAccJoinTblTiersJoinDemRenteJoinAjour entity) {

        boolean isEntre18et25Ex = false;

        try {
            int annee = new Integer(forAnnee).intValue() - new JADate(entity.getDateNaissance()).getYear();
            int mois = new Integer(forMois).intValue() - new JADate(entity.getDateNaissance()).getMonth();

            if ((annee >= 18) && (annee <= 25)) {
                if ((annee == 18) && (mois > 0)) {
                    isEntre18et25Ex = true;
                }
                if ((annee == 25) && (mois < 0)) {
                    isEntre18et25Ex = true;
                }
                if ((annee != 25) && (annee != 18)) {
                    isEntre18et25Ex = true;
                }
            }

            return isEntre18et25Ex;

        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR,
                    "REListerEcheancesProcess - isEntre18et25AnsExclusif");
            return false;
        }

    }

    public boolean isFemmmeArrivantAgeVieillesseGe() {
        return isFemmmeArrivantAgeVieillesseGe;
    }

    private boolean isFinEtudeDansDT(RERenteAccJoinTblTiersJoinDemRenteJoinAjour entity) {

        boolean isFinEtudeDansDt = false;

        try {

            String dateFinEtude = "";
            boolean premiereIteration = true;
            for (Iterator sfMemFamJointPerIter = getSfmembrefamille().iterator(); sfMemFamJointPerIter.hasNext();) {
                SFMembreFamilleJointPeriode sfMemFamJoiPer = (SFMembreFamilleJointPeriode) sfMemFamJointPerIter.next();

                if (null != sfMemFamJoiPer) {
                    if (premiereIteration) {
                        dateFinEtude = sfMemFamJoiPer.getDateFin();
                        premiereIteration = false;
                    }
                    if (!JadeStringUtil.isBlank(sfMemFamJoiPer.getDateDebut())) {
                        if (new Integer(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(sfMemFamJoiPer.getDateFin()))
                                .intValue() > new Integer(
                                PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(dateFinEtude)).intValue()) {
                            dateFinEtude = sfMemFamJoiPer.getDateFin();
                        }
                    } else {
                        if (new Integer(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(sfMemFamJoiPer.getDateFin()))
                                .intValue() > new Integer(
                                PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(dateFinEtude)).intValue()) {
                            dateFinEtude = sfMemFamJoiPer.getDateFin();
                        }
                    }
                }
            }

            JADate finEtude = new JADate(dateFinEtude);

            if (JadeStringUtil.isEmpty(dateFinEtude) || (finEtude.getMonth() != dateTraitementJad.getMonth())
                    || (finEtude.getYear() != dateTraitementJad.getYear())) {
                isFinEtudeDansDt = false;
            } else {
                isFinEtudeDansDt = true;
            }

            return isFinEtudeDansDt;

        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "REListerEcheancesProcess - isFinEtudeDansDT");
            return false;
        }
    }

    private boolean isHasDemandeVieillesseEnCours() {
        return hasDemandeVieillesseEnCours;
    }

    private boolean isHasEnfants() {
        return hasEnfants;
    }

    private boolean isHasRenteAPI() {
        return hasRenteAPI;
    }

    public boolean isHommeArrivantAgeVieillesseGe() {
        return isHommeArrivantAgeVieillesseGe;
    }

    private boolean isMarier(RERenteAccJoinTblTiersJoinDemRenteJoinAjour entity) {

        // Je recherche l'�tat civil d'un tiers dans sa situation familiale

        boolean isMarier = false;

        try {
            ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(getSession(),
                    ISFSituationFamiliale.CS_DOMAINE_RENTES, entity.getIdTierRequerant());
            setSituationFamiliale(sf);
            ISFMembreFamille x = getSituationFamiliale().getMembreFamille(entity.getIdMembreFamille());
            if (ISFSituationFamiliale.CS_ETAT_CIVIL_MARIE.equals(x.getCsEtatCivil())
                    || ISFSituationFamiliale.CS_ETAT_CIVIL_SEPARE_DE_FAIT.equals(x.getCsEtatCivil())
                    || ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_ENREGISTRE.equals(x.getCsEtatCivil())
                    || ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_SEPARE_DE_FAIT.equals(x.getCsEtatCivil())) {
                isMarier = true;
            }
            return isMarier;
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "REListerEcheancesProcess - isMarier");
            return false;
        }
    }

    private boolean isNAnsDansMoisDeTraitement(String dateNaiss, int nbAnnee) {

        try {
            int annee = new Integer(forAnnee).intValue() - new JADate(dateNaiss).getYear();
            int mois = new Integer(forMois).intValue() - new JADate(dateNaiss).getMonth();

            if ((annee == nbAnnee) && (mois == 0)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR,
                    "REListerEcheancesProcess - isNAnsDansMoisDeTraitement");
            return false;
        }
    }

    public boolean isRenteDeVeufGe() {
        return isRenteDeVeufGe;
    }

    private boolean isRenteEnCours(RERenteAccJoinTblTiersJoinDemRenteJoinAjour entity) {

        boolean isEnCours = false;

        try {

            if (new Integer(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(entity.getDateDebutDroit())).intValue() <= new Integer(
                    PRDateFormater.convertDate_AAAAMMJJ_to_AAAAMM(dateTraitementJad.toStrAMJ())).intValue()) {

                if (JadeStringUtil.isBlank(entity.getDateFinDroit())) {
                    isEnCours = true;
                }
            } else {
                isEnCours = false;
            }

            return isEnCours;

        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "REListerEcheancesProcess - isRenteEnCours");
            return false;
        }
    }

    private boolean isRentevieillesseAvecAPI() {
        return isRentevieillesseAvecAPI;
    }

    private boolean isRenteVieillesseStandard() {
        return isRenteVieillesseStandard;
    }

    private boolean isSuppression() {
        return isSuppression;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return null;
    }

    private void preparerAjoutLigne(RERenteAccJoinTblTiersJoinDemRenteJoinAjour entity, String motif,
            boolean listerInfoEntity, boolean genererLettreEcheance) {
        // On s'assure que l'on tra�te qu'une seule fois un tiers, si le tiers a d�j� �t� trait� on m�morise
        // son genre de pr�station pour completer l'ajout de la ligne.
        if (groupNss.add(entity.getNss())) {
            ajouterLigne(motif, entity, listerInfoEntity, genererLettreEcheance);
        } else {
            groupGp.add(entity.getGenreDePrestation());
        }
    }

    public boolean isEcheanceEtudesGe() {
        return isEcheanceEtudesGe;
    }

    public boolean isEnfantDe18AnsGe() {
        return isEnfantDe18AnsGe;
    }

    public boolean isEnfantDe25AnsGe() {
        return isEnfantDe25AnsGe;
    }

    public void setAjournementGe(boolean isAjournementGe) {
        this.isAjournementGe = isAjournementGe;
    }

    public void setAutreEcheanceGe(boolean isAutreEcheanceGe) {
        this.isAutreEcheanceGe = isAutreEcheanceGe;
    }

    public void setEcheanceEtudesGe(boolean isEcheanceEtudesGe) {
        this.isEcheanceEtudesGe = isEcheanceEtudesGe;
    }

    public void setEcheances(List echeances) {
        this.echeances = echeances;
    }

    public void setEnfantDe18AnsGe(boolean isEnfantDe18AnsGe) {
        this.isEnfantDe18AnsGe = isEnfantDe18AnsGe;
    }

    public void setEnfantDe25AnsGe(boolean isEnfantDe25AnsGe) {
        this.isEnfantDe25AnsGe = isEnfantDe25AnsGe;
    }

    public void setFemmmeArrivantAgeVieillesseGe(boolean isFemmmeArrivantAgeVieillesseGe) {
        this.isFemmmeArrivantAgeVieillesseGe = isFemmmeArrivantAgeVieillesseGe;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForMois(String forMois) {
        this.forMois = forMois;
    }

    private void setHasDemandeVieillesseEnCours(boolean hasDemandeVieillesseEnCours) {
        this.hasDemandeVieillesseEnCours = hasDemandeVieillesseEnCours;
    }

    private void setHasEnfants(boolean hasEnfants) {
        this.hasEnfants = hasEnfants;
    }

    private void setHasRenteAPI(boolean hasRenteAPI) {
        this.hasRenteAPI = hasRenteAPI;
    }

    public void setHommeArrivantAgeVieillesseGe(boolean isHommeArrivantAgeVieillesseGe) {
        this.isHommeArrivantAgeVieillesseGe = isHommeArrivantAgeVieillesseGe;
    }

    public void setIdLog(String idLog) {
        this.idLog = idLog;
    }

    public void setJournalLog(FWMemoryLog journalLog) {
        this.journalLog = journalLog;
    }

    private void setMembreFamille(ISFMembreFamille membreFamille) {
        MembreFamille = membreFamille;
    }

    public void setNbDocRows(int nbDocRows) {
        this.nbDocRows = nbDocRows;
    }

    public void setRenteDeVeufGe(boolean isRenteDeVeufGe) {
        this.isRenteDeVeufGe = isRenteDeVeufGe;
    }

    private void setRentevieillesseAvecAPI(boolean isRentevieillesseAvecAPI) {
        this.isRentevieillesseAvecAPI = isRentevieillesseAvecAPI;
    }

    private void setRenteVieillesseStandard(boolean isRenteVieillesseStandard) {
        this.isRenteVieillesseStandard = isRenteVieillesseStandard;
    }

    void setSfmembrefamille(SFMembreFamilleJointPeriodeManager sfmembrefamille) {
        this.sfmembrefamille = sfmembrefamille;
    }

    private void setSituationFamiliale(ISFSituationFamiliale situationFamiliale) {
        this.situationFamiliale = situationFamiliale;
    }

    public void setSuppression(boolean isSuppression) {
        this.isSuppression = isSuppression;
    }
}