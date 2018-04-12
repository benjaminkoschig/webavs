package globaz.orion.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.external.ServicesFacturation;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.orion.process.adi.ADIControlesEnum;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionManager;
import globaz.phenix.process.communications.CPProcessDemandePortailGenererDecision;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.orion.business.domaine.demandeacompte.DemandeModifAcompteStatut;
import ch.globaz.orion.business.models.adi.RecapDemandesTransmises;
import ch.globaz.orion.business.models.adi.RecapDemandesTransmisesBuilder;
import ch.globaz.orion.businessimpl.services.adi.AdiServiceImpl;
import ch.globaz.orion.db.EBDemandeModifAcompteEntity;
import ch.globaz.orion.ws.cotisation.InfosDerniereDecisionActive;
import ch.globaz.orion.ws.cotisation.WebAvsCotisationsServiceImpl;
import ch.globaz.orion.ws.enums.converters.TypeDecisionAcompteIndConverter;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.xmlns.eb.adi.DecisionAcompteIndEntity;
import ch.globaz.xmlns.eb.adi.StatusDecisionAcompteIndEnum;
import ch.globaz.xmlns.eb.adi.TypeDecisionAcompteInd;

/**
 * Process g�rant la r�cap des demandes transmises et les contr�les des d�cisions
 * 
 * @author cbu
 */
public class EBTreatADI extends BProcess {
    private static final long serialVersionUID = 1L;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private transient List<RecapDemandesTransmises> listeDemandesTransmises = new ArrayList<RecapDemandesTransmises>();
    private List<String> listDemandesToValidate = new ArrayList<String>();
    private String idPassageFacturation;

    public EBTreatADI() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
        // do nothing
    }

    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted() || getMemoryLog().hasErrors()) {
            return getSession().getLabel("PROCESS_ADI_KO");
        } else {
            return getSession().getLabel("PROCESS_ADI_OK");
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        boolean status = true;
        try {
            // recherche d'un passage de facturation pour valider automatiquement les potentielles demandes
            idPassageFacturation = findIdPassageFacturation(getSession());

            List<DecisionAcompteIndEntity> listDemandes = AdiServiceImpl.searchDemandes(getSession());

            // Pas de demande trouv�e, on ne fait rien...
            if (listDemandes == null || listDemandes.isEmpty()) {
                getMemoryLog().logMessage(getSession().getLabel("PROCESS_ADI_AUCUNE_DECISION"), FWMessage.INFORMATION,
                        this.getClass().getName());
                return true;
            }

            // Traitement des demandes : cr�ation en DB, changement de statut
            for (DecisionAcompteIndEntity demande : listDemandes) {
                treatDemande(demande);
            }

            // traitement des demandes � valider directement
            String[] tab = new String[listDemandesToValidate.size()];
            tab = listDemandesToValidate.toArray(tab);
            validerDemande(tab, idPassageFacturation);

            // Cr�ation du document excel
            genererListeExcelDemandesTransmises();
        } catch (Exception e) {
            LOGGER.error("", e);

            this._addError(getSession().getLabel("PROCESS_ADI_EXCEPTION_OCCURED"));
            CEUtils.addMailInformationsError(getMemoryLog(), CEUtils.stack2string(e), this.getClass().getName());

            status = false;
        }

        return status;
    }

    private void treatDemande(DecisionAcompteIndEntity demande) throws Exception {

        try {
            // R�cup�ration de l'affiliation li�e � la demande
            AFAffiliation affiliation = retrieveAffiliation(demande);

            // On effectue les contr�les � la suite puis on retourne la list pour cr�er les messages dans
            // EBDEM_MODIF_ACO_MESSAGE.
            EBControlesADI controlesADI = new EBControlesADI(getSession(), demande, affiliation);
            List<ADIControlesEnum> messages = controlesADI.isDifference25Pourcent().isActiviteAccessoire()
                    .isContentieux().isCIIrrecouvrable().isPerteCommerciale().isTaxationEnCours()
                    .isCommunicationFiscaleRecue().isCotisationPersonnelle().isDIN1181().isReductionCotisation()
                    .isRemiseCotisation().isPlusieursDecisionsPourAnnee().getListeErreurs();

            // Recherche de la d�cision li�e � l'ann�e
            CPDecision decision = retrieveDecision(affiliation, demande.getAnnee());

            // Cr�ation de la demande dans WebAVS
            EBDemandeModifAcompteEntity demandeCree = createDecisionWebAvs(demande, decision, affiliation, messages);

            // Si le demande est cr��e, on peut ajouter les messages
            if (!demandeCree.isNew()) {
                controlesADI.persistMessages(demandeCree);
                // Ajout de la demande dans la liste qui contiendra les lignes du fichier Excel
                addDemande(demande, demandeCree, affiliation, messages);

                // Changement du statut de la demande pour mettre "En cours"
                AdiServiceImpl.changeStatutDemande(getSession(), demande.getIdDecision(),
                        StatusDecisionAcompteIndEnum.TRAITEMENT_EN_COURS, null);
            }

            // Commit or Rollback
            // ----------------------------------------
            if (getTransaction().hasErrors()) {
                getTransaction().rollback();
                getTransaction().clearErrorBuffer();
            } else {
                getTransaction().commit();
            }
        } catch (Exception ex) {
            // Changement du statut de la demande pour mettre "Erreur"
            AdiServiceImpl.changeStatutDemande(getSession(), demande.getIdDecision(),
                    StatusDecisionAcompteIndEnum.ERREUR, null);
            _addError(getTransaction(), "Erreur traitement demande : " + ex.getMessage());
            getTransaction().rollback();
            getTransaction().clearErrorBuffer();
        }

    }

    private AFAffiliation retrieveAffiliation(DecisionAcompteIndEntity demande) throws Exception {
        AFAffiliationManager affiliationManager = new AFAffiliationManager();
        affiliationManager.setSession(getSession());
        affiliationManager.setForAffilieNumero(demande.getPartner().getNumeroAffilie());
        affiliationManager.find(BManager.SIZE_USEDEFAULT);

        if (affiliationManager.size() > 0) {
            return (AFAffiliation) affiliationManager.getFirstEntity();
        } else {
            throw new Exception("Aucune affiliation trouv�e : " + demande.getPartner().getNumeroAffilie());
        }

    }

    private void genererListeExcelDemandesTransmises() throws Exception {
        EBImprimerListeDemandesTransmisesProcess listeRecap = new EBImprimerListeDemandesTransmisesProcess();
        listeRecap.setParentWithCopy(this);
        listeRecap.setEMailAddress(getEMailAddress());
        listeRecap.setListeRecapDemandesTransmises(getListeRecapDemandesTransmises());
        listeRecap.executeProcess();
    }

    private void addDemande(DecisionAcompteIndEntity demande, EBDemandeModifAcompteEntity demandeCree,
            AFAffiliation affiliation, List<ADIControlesEnum> messages) throws Exception {

        String numAffilie = demande.getPartner().getNumeroAffilie();
        WebAvsCotisationsServiceImpl webAvsCotisationsServiceImpl = new WebAvsCotisationsServiceImpl();

        Annee anneeDemande = new Annee(demande.getAnnee());

        InfosDerniereDecisionActive infosDerniereDecisionActive = webAvsCotisationsServiceImpl
                .findInfosDerniereDecisionActive(numAffilie, anneeDemande.getValue());
        Montant beneficeNetNew = new Montant(demande.getResultatNet());
        Montant capitalNew = new Montant(demande.getCapitalInvesti());

        Montant beneficeNetCurrent = new Montant("0");
        Montant capitalCurrent = new Montant("0");
        if (infosDerniereDecisionActive != null) {
            beneficeNetCurrent = new Montant(infosDerniereDecisionActive.getResultatNet());
            capitalCurrent = new Montant(infosDerniereDecisionActive.getCapitalInvesti());
        }

        // Cr�ation de la string qui recevra le message, on ajoute de retours � la ligne � la fin de chaque message,
        // sauf le dernier
        StringBuilder sbMessage = new StringBuilder();
        int cpt = 0;
        for (ADIControlesEnum erreur : messages) {
            cpt++;
            String libelle = getSession().getLabel(erreur.getLabel());
            if (cpt < messages.size()) {
                sbMessage.append(libelle + "\n");
            } else {
                sbMessage.append(libelle);
            }
        }

        String libelleStatut = getSession().getLabel(
                DemandeModifAcompteStatut.fromValue(demandeCree.getCsStatut()).getLabel());

        String typeDecision = getSession().getCodeLibelle(
                TypeDecisionAcompteIndConverter.convertTypeDecisionAcompteIndEbusinessToCs(demande.getType()));

        RecapDemandesTransmises demandesTransmises = new RecapDemandesTransmisesBuilder()
                .withNoAffilie(demande.getPartner().getNumeroAffilie())
                .withNom(affiliation.getTiers().getDesignation1()).withPrenom(affiliation.getTiers().getDesignation2())
                .withAnnee(demande.getAnnee().toString()).withBeneficeNetNew(beneficeNetNew.toStringFormat())
                .withCapitalNew(capitalNew.toStringFormat())
                .withBeneficeNetCurrent(beneficeNetCurrent.toStringFormat())
                .withCapitalCurrent(capitalCurrent.toStringFormat()).withStatut(libelleStatut).withType(typeDecision)
                .withMessages(sbMessage.toString()).build();

        addRecapDemandesTransmises(demandesTransmises);
    }

    public void addRecapDemandesTransmises(RecapDemandesTransmises demandes) {
        if (listeDemandesTransmises == null) {
            listeDemandesTransmises = new ArrayList<RecapDemandesTransmises>();
        }
        listeDemandesTransmises.add(demandes);
    }

    public List<RecapDemandesTransmises> getListeRecapDemandesTransmises() {
        return new ArrayList<RecapDemandesTransmises>(listeDemandesTransmises);
    }

    /**
     * Cette m�thode cr��e la demande dans WebAVS et retourne ensuite les erreurs d�tect�es relatives � cette demande
     * 
     * @param demandeModification
     * @param affiliation
     * @param messages
     * @return
     * @throws Exception
     */
    private EBDemandeModifAcompteEntity createDecisionWebAvs(DecisionAcompteIndEntity demandeModification,
            CPDecision decision, AFAffiliation affiliation, List<ADIControlesEnum> messages) throws Exception {
        EBDemandeModifAcompteEntity modifAcompteEntity = new EBDemandeModifAcompteEntity();
        modifAcompteEntity.setSession(getSession());
        modifAcompteEntity.setAnnee(demandeModification.getAnnee());
        modifAcompteEntity.setCapital(demandeModification.getCapitalInvesti());
        String dateSoumission = parseDate(demandeModification.getDateSoumission());
        modifAcompteEntity.setDateReception(new Date(dateSoumission).getDate());

        if (decision != null && !decision.isNew()) {
            modifAcompteEntity.setIdDecision(decision.getId());
        }
        if (demandeModification.getIdDecision() != null) {
            modifAcompteEntity.setIdDemandePortail(demandeModification.getIdDecision().toString());
        }
        modifAcompteEntity.setRemarque(demandeModification.getRemarquePartner());
        modifAcompteEntity.setRevenu(demandeModification.getResultatNet());

        // Le status sera d�pendant des erreurs rencontr�es
        DemandeModifAcompteStatut statut = findStatutDemande(messages);
        modifAcompteEntity.setCsStatut(statut.getValue());

        String noAffilie = demandeModification.getPartner().getNumeroAffilie();
        modifAcompteEntity.setNumAffilie(noAffilie);
        modifAcompteEntity.setIdAffiliation(affiliation.getId());
        modifAcompteEntity.add();

        // Si la d�cision est un acompte et qu'elle est valide (aucune erreur), on la valide directement
        if (messages.isEmpty() && TypeDecisionAcompteInd.ACCOMPTE.equals(demandeModification.getType())) {
            listDemandesToValidate.add(modifAcompteEntity.getId());
        }

        return modifAcompteEntity;
    }

    private CPDecision retrieveDecision(AFAffiliation affiliation, Integer anneeDecision) throws Exception {
        CPDecisionManager decisionManager = new CPDecisionManager();

        decisionManager.setSession(getSession());
        decisionManager.setForIdAffiliation(affiliation.getAffiliationId());
        decisionManager.setForIsActive(true);
        decisionManager.setForAnneeDecision(anneeDecision.toString());
        decisionManager.setOrder("IAANNE DESC, IAIDEC DESC");
        decisionManager.find(BManager.SIZE_USEDEFAULT);

        CPDecision decision = null;
        if (!decisionManager.isEmpty()) {
            decision = (CPDecision) decisionManager.getFirstEntity();
        }

        return decision;
    }

    /**
     * Gestion des status
     * A traiter : Pour les cas qui ont un message � contr�ler.
     * Refuse : dans le cas o� on force une demande � refuser si elle a tel test ko
     * Valider : Dans le cas o� on g�n�re directement la d�cision si elle n�a pas de message, si c'est un acompte
     * 
     * @param messages
     * @return le statut
     */
    public DemandeModifAcompteStatut findStatutDemande(List<ADIControlesEnum> messages) {
        if (messages.isEmpty()) {
            return DemandeModifAcompteStatut.A_TRAITER;
        } else if (hasCriticalMessage(messages)) {
            return DemandeModifAcompteStatut.REFUSE;
        } else {
            return DemandeModifAcompteStatut.A_TRAITER;
        }
    }

    private boolean hasCriticalMessage(List<ADIControlesEnum> messages) {
        // Parcourir les message afin de voir si un message est critique, si oui == return true
        for (ADIControlesEnum message : messages) {
            if (message.isCritical()) {
                return true;
            }
        }
        return false;
    }

    private static String parseDate(XMLGregorianCalendar date) {
        return JadeStringUtil.fillWithZeroes(String.valueOf(date.getDay()), 2) + "."
                + JadeStringUtil.fillWithZeroes(String.valueOf(date.getMonth()), 2) + "." + date.getYear();
    }

    private String findIdPassageFacturation(BSession session) {
        // Recherche du prochain passage de facturation
        IFAPassage passage = ServicesFacturation.getProchainPassageFacturation(session, null,
                FAModuleFacturation.CS_MODULE_COT_PERS_PORTAIL);
        if (passage == null || JadeStringUtil.isIntegerEmpty(passage.getIdPassage())) {
            throw new RuntimeException(session.getLabel("CP_MSG_0102"));
        } else {
            return passage.getIdPassage();
        }
    }

    private void validerDemande(String[] listIdDemande, String idPassageFacturation) {
        if (listIdDemande.length > 0) {
            try {
                CPProcessDemandePortailGenererDecision process = new CPProcessDemandePortailGenererDecision();
                process.setParent(this);
                process.setSession(getSession());
                process.setListIdDemande(listIdDemande);
                process.setIdPassage(idPassageFacturation);
                process.executeProcess();

                if (process.isOnError()) {
                    System.out.println("erreur lors de la validation de la d�cision");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
