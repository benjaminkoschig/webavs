package ch.globaz.vulpecula.process.is;

import ch.globaz.al.business.services.ALRepositoryLocator;
import ch.globaz.al.exception.TauxImpositionNotFoundException;
import ch.globaz.al.impotsource.domain.TauxImpositions;
import ch.globaz.al.impotsource.domain.TypeImposition;
import ch.globaz.al.impotsource.persistence.TauxImpositionRepository;
import ch.globaz.al.impotsource.process.ListeISRetenuesProcess;
import ch.globaz.common.properties.PropertiesException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CAOperationManager;
import globaz.osiris.db.comptes.CAOperationOrdreVersement;
import globaz.osiris.db.comptes.CAOperationOrdreVersementManager;
import globaz.osiris.external.IntRole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import ch.globaz.al.businessimpl.services.rubriques.comptables.RubriquesComptablesBMSServiceImpl;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.osiris.business.data.JournalConteneur;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.EcritureSimpleModel;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.vulpecula.business.models.is.EntetePrestationComplexModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.is.ImpotSourceService;
import ch.globaz.vulpecula.businessimpl.services.is.PrestationGroupee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.is.HistoriqueProcessusAf;
import ch.globaz.vulpecula.external.BProcessWithContext;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;
import ch.globaz.vulpecula.util.ExceptionsUtil;
import ch.globaz.vulpecula.util.I18NUtil;
import ch.globaz.vulpecula.util.RubriqueUtil;

public class TraitementISProcess extends BProcessWithContext {
    private static final long serialVersionUID = -6539358010435862391L;

    private String idProcessusDirect;

    private List<EntetePrestationComplexModel> entetesPaiementsDirects;
    private final Map<String, Collection<PrestationGroupee>> prestationsAImprimer = new HashMap<String, Collection<PrestationGroupee>>();

    private TauxImpositions tauxGroupByCanton;

    private ImpotSourceService impotSourceService = VulpeculaServiceLocator.getImpotSourceService();

    private TauxImpositionRepository tauxImpositionRepository = ALRepositoryLocator
            .getTauxImpositionRepository();

    // Lock static permettant de garantir que seul un processus peut être lancé en parralèle
    private static ReentrantLock lock = new ReentrantLock();

    @Override
    protected boolean _executeProcess() throws Exception {
        lock.lock();
        super._executeProcess();
        if (!isProcessusDejaTraite()) {
            retrieve();

            boolean noError = traiterDirects();
            if (noError) {
                print();
            }
            return noError;
        }
        lock.unlock();
        return false;
    }

    /**
     * Retourne si le processus AF a déjà été traité ultérieurement.
     * 
     * @return true si déjà traitée
     */
    private boolean isProcessusDejaTraite() {
        return VulpeculaRepositoryLocator.getHistoriqueProcessusAfRepository().findByIdProcessus(idProcessusDirect) != null;
    }

    private boolean traiterDirects() {
        boolean isOnError = false;
        String date = Date.now().getSwissValue();
        try {
            JournalSimpleModel journal = CABusinessServiceLocator.getJournalService().createJournal(
                    BSessionUtil.getSessionFromThreadContext().getLabel("LIBELLE_JOURNAL_AF_IS"),
                    Date.now().getSwissValue());
            JournalConteneur jc = new JournalConteneur();
            jc.AddJournal(journal);
            try {
                // Commit le journal car si le premier cas plante, le journal ne sera pas créé
                JadeThread.commitSession();
            } catch (Exception e2) {
                throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e2);
            }

            for (EntetePrestationComplexModel entete : entetesPaiementsDirects) {
                try {
                    traiter(entete, jc, date);
                } catch (TauxImpositionNotFoundException e) {
                    getTransaction().addErrors(ExceptionsUtil.translateException(e, entete));
                    isOnError = true;
                }
            }

            if (!isOnError) {
                CABusinessServiceLocator.getJournalService().comptabilise(
                        CABusinessServiceLocator.getJournalService().createJournalAndOperations(jc));
                ajouterHistorique(idProcessusDirect);
            } else {
                return false;
            }
        } catch (Exception e) {
            getTransaction().addErrors(getSession().getLabel("ERREUR_CREATION_ECRITURE") + "\r\n" + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Traitement d'une entête de prestation par la création des opérations dans le journal de comptabilité auxiliaire.
     * 
     * @param entete Entete de prestation
     * @param jc JournalContainer contenant les opérations
     * @param date Date
     * @throws Exception
     */
    private void traiter(EntetePrestationComplexModel entete, JournalConteneur jc, String date) throws Exception {
        // Correctioon Jira BMS-1922
        // Selon exemple PaiementDirectServiceImpl ln450
    	String annee = entete.getNumFactureRecap().substring(0, 4);
        String numeroFacture = annee + APISection.CATEGORIE_SECTION_AF + "000";

        CompteAnnexeSimpleModel compteAnnexe = findCompteAnnexe(jc.getJournalModel().getId(), entete.getIdTiers(),
                entete.getNumAvsActuel());

        SectionSimpleModel section = CABusinessServiceLocator.getSectionService().getSectionByIdExterne(
                compteAnnexe.getIdCompteAnnexe(), APISection.CATEGORIE_SECTION_AF, numeroFacture, jc.getJournalModel());

        Taux tauxApplicable = findTauxApplicable(entete);
        Montant montantPrestation = new Montant(entete.getMontantTotal());

        String caisseAF = entete.getCodeCaisseAF();

        String idExterneIS = RubriquesComptablesBMSServiceImpl.getRubriqueForIS(caisseAF, date);
        String idRubriqueIS = RubriqueUtil.findIdRubriqueForIdExterne(idExterneIS);

        Montant impots = montantPrestation.multiply(tauxApplicable).normalize();

        EcritureSimpleModel operationModel = new EcritureSimpleModel();
        if (montantPrestation.isPositive()) {
            operationModel.setCodeDebitCredit(APIEcriture.DEBIT);
        } else {
            operationModel.setCodeDebitCredit(APIEcriture.CREDIT);
        }
        operationModel.setIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
        operationModel.setIdSection(section.getIdSection());
        operationModel.setDate(Date.now().getSwissValue());
        operationModel.setIdRubrique(idRubriqueIS);
        operationModel.setMontant(montantPrestation.multiply(tauxApplicable).getValueNormalisee());

        jc.addEcriture(operationModel);

        Adresse adresse = VulpeculaRepositoryLocator.getAdresseRepository().findAdresseDomicileByIdTiers(
                entete.getIdTiers());
        PrestationGroupee prestation = PrestationGroupee.create(entete.getNumAvsActuel(), entete.getNom(),
                entete.getPrenom(), entete.getDateNaissance(), entete.getReferencePermis(), adresse, impots,
                montantPrestation, Montant.ZERO, entete.getLibelleCaisseAF(), entete.getCodeCaisseAF(),
                entete.getTitre(), entete.getRaisonSociale(), entete.getLangue(), new Date(entete.getPeriodeDe()),
                new Date(entete.getPeriodeA()), Date.now());

        prestation.setCantonResidence(entete.getCantonResidence());

        addPrestationToMap(prestation);

        adapterOV(compteAnnexe, entete.getIdJournal(), impots.negate(), annee);
    }

    private void addPrestationToMap(PrestationGroupee prestation) {
        String libelleCaisseAF = prestation.getLibelleCaisseAF();
        if (!prestationsAImprimer.containsKey(libelleCaisseAF)) {
            prestationsAImprimer.put(libelleCaisseAF, new ArrayList<PrestationGroupee>());
        }
        Collection<PrestationGroupee> prestations = prestationsAImprimer.get(libelleCaisseAF);
        prestations.add(prestation);
    }

    private void adapterOV(CompteAnnexeSimpleModel compteAnnexe, String idJournal, Montant montantAAjouter, String annee)
            throws Exception {
    	boolean found=false;
    	
        CAOperationOrdreVersementManager caoov = new CAOperationOrdreVersementManager();
        caoov.setSession(getSession());
        caoov.setForEtatNotIn(Arrays.asList(APIOperation.ETAT_VERSE));
        caoov.setForIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
        caoov.setOrderBy(CAOperationManager.ORDER_IDOPERATION_DESC);
        caoov.setForIdJournal(idJournal);
        caoov.find();
        
        if(caoov.size()==1) {
        	CAOperationOrdreVersement ov = (CAOperationOrdreVersement) caoov.getFirstEntity();
        	found = updateOV(montantAAjouter, annee, ov);
        } else {
	        for (int i=0; i < caoov.size() && !found; i++) {
	        	CAOperationOrdreVersement ov = (CAOperationOrdreVersement) caoov.get(i);
	        	found = updateOV(montantAAjouter, annee, ov);
	        }
        }
        
  	    // Si aucun ordre de versement n'a été trouvé, on averti l'utilisateur.
        if (!found) {
            getMemoryLog().logMessage(
                    I18NUtil.getMessageFromResource("vulpecula.is.ov_nontrouve", compteAnnexe.getIdExterneRole(),
                            idJournal), FWMessage.AVERTISSEMENT, getClass().getName());
            return;
        }
        
    }

	private boolean updateOV(Montant montantAAjouter, String annee, CAOperationOrdreVersement ov) throws Exception {
	    if(annee.equals(ov.getSection().getIdExterne().substring(0, 4))) {
	        Montant montantCourant = new Montant(ov.getMontant());
	        ov.setMontant(montantCourant.add(montantAAjouter).getValue());
	        ov.update(getTransaction());
	        return true;
	    }
	    return false;
    }

    private void print() throws Exception {
        ListeISRetenuesProcess process = new ListeISRetenuesProcess();
        process.setPrestationsAImprimer(prestationsAImprimer);
        process.setWantRetrieve(false);
        process.setIdProcessusAF(idProcessusDirect);
        process.setEMailAddress(getEMailAddress());
        BProcessLauncher.start(process);
    }

    private String ajouterHistorique(String idProcessus) {
        HistoriqueProcessusAf historique = new HistoriqueProcessusAf();
        historique.setIdProcessus(idProcessus);
        HistoriqueProcessusAf histo = VulpeculaRepositoryLocator.getHistoriqueProcessusAfRepository()
                .create(historique);
        return histo.getId();
    }

    private CompteAnnexeSimpleModel findCompteAnnexe(String idJournal, String idTiers, String numAvs)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        return CABusinessServiceLocator.getCompteAnnexeService().getCompteAnnexe(idJournal, idTiers, IntRole.ROLE_AF,
                numAvs, true);
    }

    private void retrieve() throws PropertiesException {
        entetesPaiementsDirects = impotSourceService.getEntetesPrestationsIS(idProcessusDirect);
        tauxGroupByCanton = tauxImpositionRepository.findAll(TypeImposition.IMPOT_SOURCE);
    }

    private Taux findTauxApplicable(EntetePrestationComplexModel entetePrestation)
            throws TauxImpositionNotFoundException {
        return tauxGroupByCanton.getTauxImpotSource(entetePrestation.getCantonResidence(),
                new Date(entetePrestation.getPeriodeDe()));
    }

    @Override
    protected String getEMailObject() {
        return "";
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public String getIdProcessusDirect() {
        return idProcessusDirect;
    }

    public void setIdProcessusDirect(String idProcessusDirect) {
        this.idProcessusDirect = idProcessusDirect;
    }

}
