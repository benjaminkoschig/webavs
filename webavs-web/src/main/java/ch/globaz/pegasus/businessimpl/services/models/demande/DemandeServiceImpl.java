/**
 *
 */
package ch.globaz.pegasus.businessimpl.services.models.demande;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.sql.JadeAbstractSqlModelDefinition;
import globaz.jade.persistence.sql.JadeSqlFieldDefinition;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import ch.globaz.hera.business.exceptions.models.MembreFamilleException;
import ch.globaz.hera.business.services.HeraServiceLocator;
import ch.globaz.hera.business.vo.famille.MembreFamilleVO;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.models.demande.DemandeSearch;
import ch.globaz.pegasus.business.models.demande.ListDemandes;
import ch.globaz.pegasus.business.models.demande.ListDemandesSearch;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeSearch;
import ch.globaz.pegasus.business.models.revisionquadriennale.ListRevisionsSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.demande.DemandeService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pegasus.businessimpl.utils.QueryAlter;
import ch.globaz.pegasus.businessimpl.utils.QueryAlterManager;
import ch.globaz.prestation.business.exceptions.models.DemandePrestationException;

/**
 * @author ECO
 */
public class DemandeServiceImpl extends PegasusAbstractServiceImpl implements DemandeService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.demande.DemandeService#count
     * (ch.globaz.pegasus.business.models.demande.DemandeSearch)
     */
    @Override
    public int count(DemandeSearch search) throws DemandeException, JadePersistenceException {
        if (search == null) {
            throw new DemandeException("Unable to count demandes, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.demande.DemandeService#create
     * (ch.globaz.pegasus.business.models.demande.Demande)
     */
    @Override
    public Demande create(Demande demande) throws JadePersistenceException, DemandeException,
            DemandePrestationException, DossierException {
        String idTiersRequerant = null;
        if (demande == null) {
            throw new DemandeException("Unable to create demande, the given model is null!");
        }

        // On ne peut pas indiquer de date de début et fin si la demande n'est pas purement rétroactive
        if (!demande.getSimpleDemande().getIsPurRetro()) {
            demande.getSimpleDemande().setDateDebut(null);
            demande.getSimpleDemande().setDateFin(null);
        }

        try {
            if (demande.getSimpleDemande().getIsPurRetro()) {
                demande.getSimpleDemande().setCsEtatDemande(IPCDemandes.CS_SUPPRIME);
            } else {
                demande.getSimpleDemande().setCsEtatDemande(IPCDemandes.CS_EN_ATTENTE_JUSTIFICATIFS);
            }

            demande.setSimpleDemande(PegasusImplServiceLocator.getSimpleDemandeService().create(
                    demande.getSimpleDemande()));

            // si on est pas en erreur, si les logs de ssesion n'ont pas de log d'erreur
            if (!JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
                // on crée le requerant damns hera, si inexistant
                idTiersRequerant = demande.getDossier().getDemandePrestation().getPersonneEtendue().getPersonne()
                        .getIdTiers();
                createRequerantIfNotExixst(idTiersRequerant);
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException("The service for demande is not available", e);
        }

        return demande;

    }

    private void createRequerantIfNotExixst(String idTiersRequerant)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, DemandeException {
        try {
            MembreFamilleVO[] requerant = HeraServiceLocator.getMembreFamilleService()
                    .searchMembresFamilleRequerantDomaineRentes(idTiersRequerant);
            if (requerant.length == 0) {
                HeraServiceLocator.getMembreFamilleService().createRequerant(idTiersRequerant);
            }
        } catch (MembreFamilleException e) {
            throw new DemandeException("unablde to create requerant or count", e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.demande.DemandeService#delete
     * (ch.globaz.pegasus.business.models.demande.Demande)
     */
    @Override
    public Demande delete(Demande demande) throws DemandeException, JadePersistenceException {
        if (demande == null) {
            throw new DemandeException("Unable to delete demande, the given model is null!");
        }
        try {
            demande.setSimpleDemande(PegasusImplServiceLocator.getSimpleDemandeService().delete(
                    demande.getSimpleDemande()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException("Service not available - " + e.getMessage());
        }

        return demande;
    }

    @Override
    public ListDemandesSearch findDemandeForList(final ListDemandesSearch searchModel) throws JadePersistenceException {

        searchModel.setOrderKey("nomPrenomDateDebut");
        ListDemandesSearch search = (ListDemandesSearch) QueryAlterManager.executSearch(searchModel, new QueryAlter() {
            @Override
            public String alterSql(String sql, JadeAbstractSearchModel search,
                    JadeAbstractSqlModelDefinition modelDefinition) throws JadePersistenceException {
                String[] s = sql.split("ORDER BY");
                String virgule = "";
                if (s.length > 0) {
                    virgule = ",";
                }

                JadeSqlFieldDefinition f = modelDefinition.getFieldDefinition("simpleDemande.dateFin");

                if (f == null) {
                    throw new JadePersistenceException("Unable to find the sqlAlias for: simpleDemande.dateFin ");
                }

                return sql = s[0] + " ORDER BY CASE WHEN " + f.getSqlAlias() + " is null or " + f.getSqlAlias()
                        + " =0 THEN 1 ELSE 2 END " + virgule + s[1];
            }
        });
        return search;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.pegasus.business.services.models.demande.DemandeService#hasDemandePCEnPremiereInstruction(java.lang
     * .String)
     */
    @Override
    public Boolean hasDemandePCEnPremiereInstruction(String idTiers)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, DemandeException {

        if (JadeStringUtil.isEmpty(idTiers)) {
            throw new DemandeException("Unable to check demands, the idTier passed is null!");
        }

        DemandeSearch demSearch = new DemandeSearch();
        demSearch.setForIdTiers(idTiers);
        demSearch.setInCsEtatDemande(new ArrayList<String>() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            {
                this.add(IPCDemandes.CS_EN_ATTENTE_JUSTIFICATIFS);
                this.add(IPCDemandes.CS_EN_ATTENTE_CALCUL);
            }
        });

        return count(demSearch) > 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.demande.DemandeService#read (java.lang.String)
     */
    @Override
    public Demande read(String idDemande) throws JadePersistenceException, DemandeException {
        if (JadeStringUtil.isEmpty(idDemande)) {
            throw new DemandeException("Unable to read dossier, the id passed is null!");
        }
        Demande demande = new Demande();
        demande.setId(idDemande);
        return (Demande) JadePersistenceManager.read(demande);
    }

    @Override
    public ListDemandes readDemande(String idDemande) throws JadePersistenceException, DemandeException {

        // ListDemandes demande = new ListDemandes();
        // try {
        // demandes = PegasusImplServiceLocator.getDecisionBusinessService().read(idDecision);
        // } catch (JadeApplicationServiceNotAvailableException e) {
        // throw new DecisionException("Service not available - " + e.getMessage());
        // }
        // return decision;
        if (JadeStringUtil.isEmpty(idDemande)) {
            throw new DemandeException("Unable to read demande, the id passed is null!");
        }
        ListDemandes demande = new ListDemandes();
        demande.setId(idDemande);
        return (ListDemandes) JadePersistenceManager.read(demande);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.demande.DemandeService#search
     * (ch.globaz.pegasus.business.models.demande.DemandeSearch)
     */
    @Override
    public DemandeSearch search(DemandeSearch demandeSearch) throws JadePersistenceException, DemandeException {
        if (demandeSearch == null) {
            throw new DemandeException("Unable to search demande, the search model passed is null!");
        }
        return (DemandeSearch) JadePersistenceManager.search(demandeSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.pegasus.business.services.models.demande.DemandeService#searchDemandes(ch.globaz.pegasus.business.
     * models
     * .demande.ListDemandesSearch)
     */
    @Override
    public ListDemandesSearch searchDemandes(ListDemandesSearch listDemandesSearch) throws DecisionException,
            JadePersistenceException {

        if (listDemandesSearch == null) {
            throw new DecisionException("Unable to search demandes, the search model passed is null!");
        }
        return (ListDemandesSearch) JadePersistenceManager.search(listDemandesSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.pegasus.business.services.models.demande.DemandeService#searchRevisions(ch.globaz.pegasus.business.
     * models.demande.ListRevisionsSearch)
     */
    @Override
    public ListRevisionsSearch searchRevisions(ListRevisionsSearch listRevisionsSearch) throws DemandeException,
            JadePersistenceException {

        if (listRevisionsSearch == null) {
            throw new DemandeException("Unable to search demandes for revision, the search model passed is null!");
        }
        return (ListRevisionsSearch) JadePersistenceManager.search(listRevisionsSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.demande.DemandeService#update
     * (ch.globaz.pegasus.business.models.demande.Demande)
     */
    @Override
    public Demande update(Demande demande) throws JadePersistenceException, DemandeException, DossierException {
        if (demande == null) {
            throw new DemandeException("Unable to update demande, the given model is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleDemandeService().update(demande.getSimpleDemande());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeException("Service not available - " + e.getMessage());
            // e.printStackTrace();
        }

        return demande;
    }

    @Override
    public boolean isDemandeReouvrable(Demande demande) throws PegasusException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        PCAccordeeSearch search = new PCAccordeeSearch();
        search.setWhereKey(PCAccordeeSearch.FOR_CURRENT_VERSIONED_WITH_DATE_FIN_FORCE);
        search.setForIdDemande(demande.getSimpleDemande().getIdDemande());
        search.setForCsEtatPca(IPCPCAccordee.CS_ETAT_PCA_VALIDE);
        int nb = PegasusImplServiceLocator.getPCAccordeeService().count(search);

        if ((nb > 0) && isLastDemande(demande)
                && !IPCDemandes.CS_REOUVERT.equals(demande.getSimpleDemande().getCsEtatDemande())) {
            return true;
        }
        return false;

    }

    @Override
    public boolean isLastDemande(Demande demande) throws DemandeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        DemandeSearch search = new DemandeSearch();
        search.setForIdDossier(demande.getDossier().getDossier().getIdDossier());
        search.setWhereKey(DemandeSearch.FOR_DEMANDE_WITH_DATE_DEBUT_MAX_OR_DATE_DEBUT_VIDE);
        search = PegasusServiceLocator.getDemandeService().search(search);
        Demande demandeWithDateMax = null;
        if (search.getSize() == 1) {
            demandeWithDateMax = (Demande) search.getSearchResults()[0];
            if (demandeWithDateMax.getId().equals(demande.getId())) {
                if (!JadeStringUtil.isBlankOrZero(demandeWithDateMax.getSimpleDemande().getDateDebut())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Demande rouvrir(Demande demande) throws JadePersistenceException, JadeApplicationException {

        if (isLastDemande(demande)) {
            demande.getSimpleDemande().setDateFin(null);
            demande.getSimpleDemande().setCsEtatDemande(IPCDemandes.CS_REOUVERT);
            update(demande);
            PCAccordeeSearch search = new PCAccordeeSearch();
            search.setWhereKey(PCAccordeeSearch.FOR_CURRENT_VERSIONED_WITHOUT_COPIE);
            search.setForIdDemande(demande.getSimpleDemande().getIdDemande());
            search = PegasusImplServiceLocator.getPCAccordeeService().search(search);
            for (JadeAbstractModel model : search.getSearchResults()) {
                PCAccordee pca = (PCAccordee) model;
                if (pca.getSimplePCAccordee().getIsDateFinForce()) {
                    pca.getSimplePCAccordee().setIsDateFinForce(false);
                    pca.getSimplePCAccordee().setDateFin(null);
                    pca.getSimplePrestationsAccordees().setDateFinDroit(null);
                    if (pca.getSimplePrestationsAccordeesConjoint() != null) {
                        pca.getSimplePrestationsAccordeesConjoint().setDateFinDroit(null);
                    }

                    PegasusImplServiceLocator.getSimplePCAccordeeService().update(pca.getSimplePCAccordee());

                    pca.setSimplePrestationsAccordees(PegasusImplServiceLocator.getSimplePrestatioAccordeeService()
                            .update(pca.getSimplePrestationsAccordees()));

                    // Si conjoint (DOM2Rentes)
                    if (!JadeStringUtil.isBlankOrZero(pca.getSimplePrestationsAccordeesConjoint()
                            .getIdPrestationAccordee())) {
                        pca.setSimplePrestationsAccordeesConjoint(PegasusImplServiceLocator
                                .getSimplePrestatioAccordeeService()
                                .update(pca.getSimplePrestationsAccordeesConjoint()));
                    }
                }
            }
        }
        return demande;
    }

    @Override
    public Demande reFermer(Demande demande) throws JadePersistenceException, JadeApplicationException {

        if (IPCDemandes.CS_REOUVERT.equals(demande.getSimpleDemande().getCsEtatDemande())) {
            String idDemande = demande.getSimpleDemande().getIdDemande();

            PCAccordeeSearch search = new PCAccordeeSearch();
            search.setWhereKey(PCAccordeeSearch.FOR_CURRENT_VERSIONED_WITHOUT_COPIE);
            search.setForIdDemande(idDemande);
            search = PegasusImplServiceLocator.getPCAccordeeService().search(search);
            List<PCAccordee> pcas = PersistenceUtil.typeSearch(search);

            Collections.sort(pcas, new Comparator<PCAccordee>() {
                @Override
                public int compare(PCAccordee o1, PCAccordee o2) {
                    try {
                        Date before = new SimpleDateFormat("MM.yyyy").parse(o1.getSimplePCAccordee().getDateDebut());
                        Date after = new SimpleDateFormat("MM.yyyy").parse(o2.getSimplePCAccordee().getDateDebut());
                        return after.compareTo(before);
                    } catch (ParseException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            });

            PCAccordee lastPca = pcas.get(0);
            if (lastPca == null) {
                throw new DemandeException("Unable to find the last pca with this idDemande:"
                        + demande.getSimpleDemande().getIdDemande());
            }

            String dateFin = "12." + lastPca.getSimplePCAccordee().getDateDebut().substring(3);
            demande.getSimpleDemande().setDateFin(dateFin);

            boolean hasOnlyPcaRefus = PegasusServiceLocator.getDecisionService().hasOnlyRefus(idDemande);
            if (hasOnlyPcaRefus) {
                demande.getSimpleDemande().setCsEtatDemande(IPCDemandes.CS_REFUSE);
            } else {
                demande.getSimpleDemande().setCsEtatDemande(IPCDemandes.CS_SUPPRIME);
            }

            update(demande);

            for (JadeAbstractModel model : search.getSearchResults()) {
                PCAccordee pca = (PCAccordee) model;
                if (lastPca.getSimplePCAccordee().getDateDebut().equals(pca.getSimplePCAccordee().getDateDebut())) {

                    pca.getSimplePCAccordee().setIsDateFinForce(true);
                    pca.getSimplePCAccordee().setDateFin(dateFin);
                    pca.getSimplePrestationsAccordees().setDateFinDroit(dateFin);
                    if (pca.getSimplePrestationsAccordeesConjoint() != null) {
                        pca.getSimplePrestationsAccordeesConjoint().setDateFinDroit(dateFin);
                    }

                    PegasusImplServiceLocator.getSimplePCAccordeeService().update(pca.getSimplePCAccordee());

                    pca.setSimplePrestationsAccordees(PegasusImplServiceLocator.getSimplePrestatioAccordeeService()
                            .update(pca.getSimplePrestationsAccordees()));

                    // Si conjoint (DOM2Rentes)
                    if (!JadeStringUtil.isBlankOrZero(pca.getSimplePrestationsAccordeesConjoint()
                            .getIdPrestationAccordee())) {
                        pca.setSimplePrestationsAccordeesConjoint(PegasusImplServiceLocator
                                .getSimplePrestatioAccordeeService()
                                .update(pca.getSimplePrestationsAccordeesConjoint()));
                    }
                }
            }
        }
        return demande;
    }

    @Override
    public Demande annuler(Demande demande, Boolean comptabilisationAuto) throws JadePersistenceException,
            DemandeException, DossierException, DroitException, JadeApplicationServiceNotAvailableException {

        List<Droit> droits = PegasusServiceLocator.getDroitService().findCurrentVersionDroitByIdsDemande(
                Arrays.asList(demande.getId()));
        String today = JadeDateUtil.getGlobazFormattedDate(new Date());
        String dateDeb = JadeDateUtil.addMonths("01." + demande.getSimpleDemande().getDateDebut(), -1).substring(3);
        for (Droit droit : droits) {
            droit = PegasusServiceLocator.getDroitService().corrigerDroitAnnulation(droit, today, dateDeb, today,
                    BSessionUtil.getSessionFromThreadContext().getUserId(), comptabilisationAuto, null);
        }
        demande.getSimpleDemande().setCsEtatDemande(IPCDemandes.CS_ANNULE);
        update(demande);

        return demande;
    }
}
