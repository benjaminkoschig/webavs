package ch.globaz.pegasus.businessimpl.services.models.annonce.annoncelaprams;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCDestinationSortieHome;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCHomes;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.annonce.DroitMembreSituationFamille;
import ch.globaz.pegasus.business.models.annonce.DroitMembreSituationFamilleSearch;
import ch.globaz.pegasus.business.models.annonce.RechercheHomeSash;
import ch.globaz.pegasus.business.models.annonce.RechercheHomeSashSearch;
import ch.globaz.pegasus.business.models.annonce.SimpleAnnonceLaprams;
import ch.globaz.pegasus.business.models.annonce.SimpleAnnonceLapramsDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionSuppression;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;
import ch.globaz.pegasus.business.models.decision.SimpleValidationDecision;
import ch.globaz.pegasus.business.models.droit.DonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.droit.DonneeFinanciereHeaderSearch;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.pcaccordee.PcaForDecompte;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;
import ch.globaz.pegasus.business.services.models.annonce.annoncelaprams.PrepareAnnonceLapramsService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;

public class PrepareAnnonceLapramsServiceImpl extends PegasusAbstractServiceImpl implements
        PrepareAnnonceLapramsService {

    private static final List<String> LIST_CS_SASH_SPAS = new ArrayList<String>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        {
            this.add(IPCHomes.CS_SERVICE_ETAT_SASH);
            this.add(IPCHomes.CS_SERVICE_ETAT_SPAS);
        }
    };

    private static final String CS_CODE_SORTIE_HOME_TO_DOM = "HOME_TO_DOM";

    private void addDonneesFinancieresHeaderLaprams(SimpleDecisionHeader simpleDecisionHeader,
            SimpleVersionDroit simpleVersionDroit, String idAnnonceLAPRAMS)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, AnnonceException {

        try {
            DonneeFinanciereHeaderSearch searchModel = new DonneeFinanciereHeaderSearch();
            searchModel.setWhereKey("forLaprams");
            searchModel.setForDateDebutCheckPeriode(simpleDecisionHeader.getDateDebutDecision());
            searchModel.setForDateFinCheckPeriode(simpleDecisionHeader.getDateFinDecision());
            searchModel.setForIdDroit(simpleVersionDroit.getIdDroit());
            searchModel.setForNumeroVersion(simpleVersionDroit.getNoVersion());

            searchModel = PegasusImplServiceLocator.getDonneeFinanciereHeaderService().search(searchModel);

            for (JadeAbstractModel absDonnee : searchModel.getSearchResults()) {
                DonneeFinanciereHeader donnee = (DonneeFinanciereHeader) absDonnee;

                // crée le model de mapping
                SimpleAnnonceLapramsDonneeFinanciereHeader donneeLapramsDoFinH = new SimpleAnnonceLapramsDonneeFinanciereHeader();
                donneeLapramsDoFinH.setIdAnnonceLaprams(idAnnonceLAPRAMS);
                donneeLapramsDoFinH.setIdDonneeFinanciereHeader(donnee.getSimpleDonneeFinanciereHeader()
                        .getIdDonneeFinanciereHeader());
                // donneeLapramsDoFinH.setCsRoleMembreFamille(donnee.get)
                donneeLapramsDoFinH = PegasusImplServiceLocator.getSimpleAnnonceLapramsDoFinHService().create(
                        donneeLapramsDoFinH);
            }
        } catch (DonneeFinanciereException e) {
            throw new AnnonceException("An error happened while search for donnee financiere!", e);
        }

    }

    private boolean checkForFonctionAnnonceLaprams() throws AnnonceException {
        try {
            return EPCProperties.GESTION_ANNONCES_LAPRAMS.getBooleanValue();
        } catch (Exception e) {
            throw new AnnonceException("Couldn't get property gestionAnnonceLaprams in Pegasus.properties!", e);
        }
    }

    /**
     * Verifie si le calcul en cours contient bien un home de type SASH ou SPAS.
     * 
     * @param dateFin
     * @param dateDebut
     * @param idParents
     * @param bs
     * @return vrai si un home SASH ou SPAS est trouvé
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws PrestationException
     */
    private RechercheHomeSash checkIsSASH_SPAS(String idDroit, String noVersionDroit, String idDroitMemebreFamille,
            String dateValable) throws JadeApplicationServiceNotAvailableException, AnnonceException,
            JadePersistenceException {

        // recherche toutes les taxes journalières et leur home concerné
        RechercheHomeSashSearch searchModel = new RechercheHomeSashSearch();
        searchModel.setForInCsServiceEtat(PrepareAnnonceLapramsServiceImpl.LIST_CS_SASH_SPAS);
        searchModel.setForIdDroit(idDroit);
        searchModel.setForNoVersionDroit(noVersionDroit);
        searchModel.setForIdDroitMemembreFamille(idDroitMemebreFamille);
        searchModel.setForDateDebut(dateValable);
        searchModel.setOrderKey("byNumVersionDroit");
        searchModel = PegasusImplServiceLocator.getAnnonceLapramsService().searchHomesSASH_SPAS(searchModel);

        if (searchModel.getSize() > 0) {
            return ((RechercheHomeSash) searchModel.getSearchResults()[0]);
        } else {
            return null;
        }
    }

    private void genereAnnonceLaprams(SimpleVersionDroit simpleVersionDroit, SimpleDecisionHeader simpleDecisionHeader,
            SimpleValidationDecision simpleValidationDecision, String csMotifSuppression) throws AnnonceException {
        try {

            if (!checkForFonctionAnnonceLaprams()) {
                return;
            }

            String[] idsDroitMembreFamille = getIdParents(simpleVersionDroit.getIdDroit(),
                    simpleDecisionHeader.getIdTiersBeneficiaire());

            String dateValable = simpleDecisionHeader.getDateDebutDecision();
            if (isHomeSorite(csMotifSuppression)) {
                dateValable = JadeDateUtil.addMonths("01." + dateValable, -1).substring(3);
            }
            RechercheHomeSash homeSash = checkIsSASH_SPAS(simpleVersionDroit.getIdDroit(),
                    simpleVersionDroit.getNoVersion(), idsDroitMembreFamille[0], dateValable);

            if (homeSash == null) {
                return;
            }

            SimpleAnnonceLaprams model = new SimpleAnnonceLaprams();

            model.setDateRapport(JadeDateUtil.getFormattedDate(new Date()));

            model.setCsTypeHome(homeSash.getCsServiceEtat());

            // DMA: ajout voir avec BCS
            // manque code Enfant ????
            model.setCodeEnfant("0");
            model.setIdTaxeJournaliereHome(homeSash.getIdTaxeJournaliereHome());

            if (isHomeSorite(csMotifSuppression)) {
                model.setCodeDestinationSortie(resolveDestinationSortie(csMotifSuppression,
                        homeSash.getCsDestinationSortie()));
            } else {
                model.setCodeDestinationSortie(resolveDestinationSortie(csMotifSuppression, null));
                model.setCsMotifSuppression(csMotifSuppression);
            }

            if (simpleValidationDecision != null) {
                model.setIdPcAccordee(simpleValidationDecision.getIdPCAccordee());
            }

            // Attention ici l'id requérant peut-être l'id du conjoint. En faite l'id requérant correspond a l'id du
            // Bénéficiare
            model.setIdDroitMbrFamRequerant(idsDroitMembreFamille[0]);
            model.setIdDroitMbrFamConjoint(idsDroitMembreFamille[1]);

            model.setIdDecisionHeader(simpleDecisionHeader.getId());
            model = PegasusImplServiceLocator.getSimpleAnnonceLapramsService().create(model);

            addDonneesFinancieresHeaderLaprams(simpleDecisionHeader, simpleVersionDroit, model.getIdAnnonceLAPRAMS());

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AnnonceException("Service not Available!", e);
        } catch (JadePersistenceException e) {
            throw new AnnonceException("An error happened during persistence!", e);
        } catch (DroitException e) {
            throw new AnnonceException("An error happened during the search of DroitMembreFamille!", e);
        } catch (PrestationException e) {
            throw new AnnonceException("A prestation error happened!", e);
        }
    }

    private boolean isHomeSorite(String csMotifSuppression) {
        return PrepareAnnonceLapramsServiceImpl.CS_CODE_SORTIE_HOME_TO_DOM.equals(csMotifSuppression);
    }

    /**
     * @param csMotifSuppression
     * @param csHomeDestinationSorite
     * @return
     */
    String resolveDestinationSortie(String csMotifSuppression, String csHomeDestinationSorite) {

        if (IPCDecision.CS_MOTIF_SUPPRESSION_DECES.equals(csMotifSuppression)) {
            return "DC";
        }

        if (IPCDestinationSortieHome.CS_HOPITAL.equals(csHomeDestinationSorite)) {
            return "HO";
        } else if (IPCDestinationSortieHome.CS_DOMICILE.equals(csHomeDestinationSorite)) {
            return "DO";
        } else if (IPCDestinationSortieHome.CS_AUTRE_HOME.equals(csHomeDestinationSorite)) {
            return "EM";
        } else if (IPCDestinationSortieHome.CS_DOMICILE.equals(csHomeDestinationSorite)) {
            return "DC";
        }

        return "";
    }

    @Override
    public void genereAnnonceLapramsSuppression(DecisionSuppression decisionSuppression) throws AnnonceException {
        genereAnnonceLaprams(decisionSuppression.getVersionDroit().getSimpleVersionDroit(), decisionSuppression
                .getDecisionHeader().getSimpleDecisionHeader(), null, decisionSuppression
                .getSimpleDecisionSuppression().getCsMotif());
    }

    @Override
    public void genereAnnonceLapramsValidation(DecisionApresCalcul decision, DecisionApresCalcul dcAvant,
            PcaForDecompte pcaReplacedHome) throws AnnonceException {

        String csCodeSortie = null;
        boolean createAnnonce = false;
        if (isGenrePcaHome(decision.getPcAccordee().getSimplePCAccordee())) {
            csCodeSortie = null;
            createAnnonce = true;
        } else if (IPCPCAccordee.CS_GENRE_PC_DOMICILE.equals(decision.getPcAccordee().getSimplePCAccordee()
                .getCsGenrePC())) {
            if ((dcAvant != null) && isGenrePcaHome(dcAvant.getPcAccordee().getSimplePCAccordee())) {
                csCodeSortie = PrepareAnnonceLapramsServiceImpl.CS_CODE_SORTIE_HOME_TO_DOM;
                createAnnonce = true;
            } else if ((pcaReplacedHome != null) && isGenrePcaHome(pcaReplacedHome.getSimplePCAccordee())) {
                csCodeSortie = PrepareAnnonceLapramsServiceImpl.CS_CODE_SORTIE_HOME_TO_DOM;
                createAnnonce = true;
            }// ON ne fait rien car cela signifie qu'il ne faut pas faire d'annonce

        } else {
            throw new AnnonceException("The genre of the pca is not supported, id pca:"
                    + decision.getPcAccordee().getSimplePCAccordee().getId());
        }

        if (createAnnonce) {
            genereAnnonceLaprams(decision.getVersionDroit().getSimpleVersionDroit(), decision.getDecisionHeader()
                    .getSimpleDecisionHeader(), decision.getSimpleValidationDecision(), csCodeSortie);
        }
    }

    private boolean isGenrePcaHome(SimplePCAccordee simplePCAccordee) {
        return IPCPCAccordee.CS_GENRE_PC_HOME.equals(simplePCAccordee.getCsGenrePC());
    }

    @Override
    public void genereAnnonceLapramsValidation(SimpleVersionDroit simpleVersionDroit,
            SimpleDecisionHeader simpleDecisionHeader, SimpleValidationDecision simpleValidationDecision)
            throws AnnonceException {
        try {
            if (EPCProperties.GESTION_ANNONCES_LAPRAMS.getBooleanValue()) {
                genereAnnonceLaprams(simpleVersionDroit, simpleDecisionHeader, simpleValidationDecision, null);
            }
        } catch (PropertiesException e) {
            throw new AnnonceException("Unbale to obtain properties for annonce laprams", e);
        }
    }

    private String[] getIdParents(String idDroit, String idTiersBeneficiaire) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // tous les membres de famille
        DroitMembreSituationFamilleSearch search = new DroitMembreSituationFamilleSearch();
        search.setForIdDroit(idDroit);
        search.setForInCsRoleFamille(new ArrayList<String>() {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            {
                this.add(IPCDroits.CS_ROLE_FAMILLE_CONJOINT);
                this.add(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
            }
        });
        List<DroitMembreSituationFamille> list = PersistenceUtil.search(search, search.whichModelClass());

        String[] result = new String[2];

        if (list.size() == 0) {
            throw new DroitException("No requerant is found for droit(" + idDroit + ")!");
        }

        if (list.size() > 2) {
            throw new DroitException("Too many people have been found for droit(" + idDroit + ")!");
        }

        for (DroitMembreSituationFamille donnee : list) {
            int idx;
            if (donnee.getIdTiers().equals(idTiersBeneficiaire)) {
                idx = 0;
            } else {
                idx = 1;
            }
            // on est dans un cas de parent (0=requerant,1=conjoint)
            result[idx] = donnee.getIdDroitMembreFamille();
        }

        if (result[0] == null) {
            throw new DroitException("No requerant(beneficaire) is found for droit(" + idDroit + ")!");
        }
        return result;
    }
}
