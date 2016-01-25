package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation;

import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.renderer.JadeBusinessMessageRenderer;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitSearch;
import ch.globaz.pegasus.business.models.renteijapi.RenteAvsAi;
import ch.globaz.pegasus.business.models.renteijapi.RenteAvsAiSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.tests.retroAndOv.DonneeUtilsForTestRetroAndOV;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;

public class TestChageComptabilisation {

    List<String> nss = new ArrayList<String>();

    public void clearTablePC() throws JadePersistenceException, Exception {
    }

    public void corrigeDroitAndChangeRente(Droit droit) throws Exception {

        DroitMembreFamille droitMembreFamille = DonneeUtilsForTestRetroAndOV.findDroitMembreFamilleRequerant(droit);

        RenteAvsAi rente = findRente(droit, droitMembreFamille);

        droit = PegasusServiceLocator.getDroitService().corrigerDroit(droit, DonneeUtilsForTestRetroAndOV.getDate(-1),
                IPCDroits.CS_MOTIF_DROIT_MODIFICATIONS_DIVERSES);
        String montantNewRente = String.valueOf(new BigDecimal(rente.getSimpleRenteAvsAi().getMontant())
                .add(new BigDecimal(150)));

        DonneeUtilsForTestRetroAndOV.createAndCloseRenteAvs(droit, droitMembreFamille, montantNewRente,
                DonneeUtilsForTestRetroAndOV.getDateMonth(-3));

        PegasusServiceLocator.getDroitService().calculerDroit(droit.getSimpleVersionDroit().getIdVersionDroit(), true,
                null);

        DonneeUtilsForTestRetroAndOV.createDecisionAcCaluleAndPreValide(droit);

        DonneeUtilsForTestRetroAndOV.validerToutesLesDesionsionApresCalcule(droit);

    }

    private List<Droit> findCurrentDroitOctroy() throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, DroitException {
        DroitSearch search = new DroitSearch();
        search.setDefinedSearchSize(80);
        search.setForCsEtatDroit(IPCDroits.CS_VALIDE);
        search.setForCsEtatDemande(IPCDemandes.CS_OCTROYE);
        search.setWhereKey(DroitSearch.CURRENT_VERSION_WITH_DATE_FIN_DEMANDE_NULL);
        search = PegasusServiceLocator.getDroitService().searchDroit(search);
        return PersistenceUtil.typeSearch(search, search.whichModelClass());
    }

    private RenteAvsAi findRente(Droit droit, DroitMembreFamille droitMembreFamille) throws DroitException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        RenteAvsAiSearch renteAvsAiSearch = new RenteAvsAiSearch();
        renteAvsAiSearch.setForNumeroVersion(droit.getSimpleVersionDroit().getNoVersion());
        renteAvsAiSearch.setWhereKey("forVersioned");
        renteAvsAiSearch.setIdDroitMembreFamille(droitMembreFamille.getSimpleDroitMembreFamille().getId());
        renteAvsAiSearch = PegasusServiceLocator.getDroitService().searchRenteAvsAi(renteAvsAiSearch);
        RenteAvsAi renteAvsAiOld = (RenteAvsAi) renteAvsAiSearch.getSearchResults()[0];
        return renteAvsAiOld;
    }

    @Test
    @Ignore
    public void genearteCas() throws Exception {
        List<Droit> droits = findCurrentDroitOctroy();
        for (Droit droit : droits) {
            String nss = droit.getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                    .getPersonneEtendue().getNumAvsActuel();
            try {
                System.out.println("NSS:" + nss);
                corrigeDroitAndChangeRente(droit);
                this.nss.add(nss);
            } catch (Exception e) {
                this.nss.add("ERROR-NSS:" + nss);
                e.printStackTrace();
            }
        }
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("*****NSS****");
        for (String nss : this.nss) {
            System.out.println("NSS: " + nss);
        }
        if (JadeThread.logHasMessages()) {
            JadeBusinessMessageRenderer render = JadeBusinessMessageRenderer.getInstance();
            System.out.println(render.getDefaultAdapter().render(JadeThread.logMessages(), "fr"));
        }
        JadeThread.logClear();
        JadeThread.commitSession();
        System.out.println("STOPING CONTEXT");
        JadeThreadActivator.stopUsingContext(this);
    }

}
