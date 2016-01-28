package ch.globaz.pegasus.businessimpl.services.process.adaptation;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.io.File;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.process.AdaptationException;
import ch.globaz.pegasus.business.exceptions.models.process.RenteAdapationDemandeException;
import ch.globaz.pegasus.business.models.process.adaptation.DemandePcaPrestationSearch;
import ch.globaz.pegasus.business.models.process.adaptation.DonneeFinanciere;
import ch.globaz.pegasus.business.models.process.adaptation.DonneeFinanciereSearch;
import ch.globaz.pegasus.business.services.process.adaptation.AdaptationService;
import ch.globaz.pegasus.business.vo.process.adaptation.DonneeFinancierePartiel;
import ch.globaz.pegasus.businessimpl.utils.PegasusDateUtil;
import ch.globaz.simpleoutputlist.outimpl.SimpleOutputListBuilder;

public class AdaptationServiceImpl implements AdaptationService {
    private static final Logger LOG = LoggerFactory.getLogger(AdaptationServiceImpl.class);

    /**
     * Calcule le prix du home en soustrayant le par lca annualisé si elle existe.
     * 
     * @param search
     * @param df
     * @return
     */
    private BigDecimal calculePrixHome(DonneeFinanciereSearch search, DonneeFinanciere df) {
        BigDecimal prixHome;
        prixHome = new BigDecimal(df.getPrixChambreJournalier()).multiply(new BigDecimal(PegasusDateUtil
                .getDayInYear("01." + search.getForDateValable())));
        //
        // if (df.getIsParticipationLCA()) {
        // BigDecimal lcaAnnuel = new BigDecimal(df.getMontantJournalierLCA()).multiply(new BigDecimal(12));
        // prixHome = prixHome.subtract(lcaAnnuel);
        // }
        return prixHome;
    }

    @Override
    public DonneeFinancierePartiel findDonneeFinanciereAncienne(DonneeFinanciereSearch search)
            throws RenteAdapationDemandeException, AdaptationException, JadePersistenceException {
        search = this.search(search);
        BigDecimal montantTotalRente = new BigDecimal(0);
        BigDecimal montantTotalDessaisissement = new BigDecimal(0);
        BigDecimal prixHome = new BigDecimal(0);
        // Liste des dessaisssements

        int nbEnfant = 0;
        Map<String, String> map = new HashMap<String, String>();
        DonneeFinancierePartiel dfp = new DonneeFinancierePartiel();

        for (JadeAbstractModel model : search.getSearchResults()) {
            DonneeFinanciere df = (DonneeFinanciere) model;
            map.put(df.getIdMembreFamille(), df.getCsRole());

            if (IPCDroits.CS_RENTE_AVS_AI.equals(df.getCsTypeDonneeFinanciere())) {
                if (IPCDroits.CS_ROLE_FAMILLE_ENFANT.equals(df.getCsRole())) {
                    nbEnfant++;
                }
                montantTotalRente = montantTotalRente.add(new BigDecimal(df.getRenteAVSAIMontant()));
            } else if (IPCDroits.CS_DESSAISISSEMENT_FORTUNE.equals(df.getCsTypeDonneeFinanciere())) {
                montantTotalDessaisissement = montantTotalDessaisissement.add(new BigDecimal(df
                        .getDessaisissementFortuneMontant()));

            } else if (IPCDroits.CS_TAXE_JOURN_HOME.equals(df.getCsTypeDonneeFinanciere())) {
                if (!JadeStringUtil.isBlankOrZero(df.getPrixChambreJournalier())) {
                    if (IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(df.getCsRole())) {
                        prixHome = calculePrixHome(search, df);
                        dfp.setPrixHome(prixHome.toString());
                    } else if (IPCDroits.CS_ROLE_FAMILLE_CONJOINT.equals(df.getCsRole())) {
                        prixHome = calculePrixHome(search, df);
                        dfp.setPrixHomeConjoint(prixHome.toString());
                    }
                }
            } else {
                throw new AdaptationException(
                        "This type of donneeFinanciere is not process by this methode(findDonneeFinanciereAncienne) :"
                                + df.getCsTypeDonneeFinanciere());
            }
        }

        dfp.setDonation(montantTotalDessaisissement.toString());
        dfp.setMontantRentes(montantTotalRente.multiply(new BigDecimal(12)).toString());
        dfp.setNbEnfant(String.valueOf(nbEnfant));
        return dfp;
    }

    @Override
    public DemandePcaPrestationSearch search(DemandePcaPrestationSearch search) throws JadePersistenceException,
            AdaptationException {
        if (search == null) {
            throw new AdaptationException("Unable to search demandePcaPrestationSearch, the model passed is null!");
        }
        return (DemandePcaPrestationSearch) JadePersistenceManager.search(search);
    }

    private DonneeFinanciereSearch search(DonneeFinanciereSearch search) throws RenteAdapationDemandeException,
            JadePersistenceException, AdaptationException {
        if (search == null) {
            throw new AdaptationException("Unable to search donneeFinanciereSearch, the model passed is null!");
        }
        return (DonneeFinanciereSearch) JadePersistenceManager.search(search);
    }

    @Override
    public String findInfosForImpression(String idProcess) throws JadePersistenceException, JadeApplicationException {
        StopWatch watch = new StopWatch();

        watch.start();
        PcaLoader pcaLoader = new PcaLoader();
        List<PcaForImpression> pcasForImpressions = pcaLoader.load(idProcess);
        watch.stop();

        LOG.warn("Loading time :{} ms", watch.getTime());

        watch = new StopWatch();
        watch.start();

        Locale locale = new Locale(BSessionUtil.getSessionFromThreadContext().getIdLangueISO());
        String path = Jade.getInstance().getPersistenceDir() + JadeUUIDGenerator.createStringUUID();

        Collections.sort(pcasForImpressions, new Comparator<PcaForImpression>() {
            @Override
            public int compare(PcaForImpression o1, PcaForImpression o2) {
                return o1.getIdVersionDroit().compareTo(o2.getIdVersionDroit());
            }
        });

        File file = SimpleOutputListBuilder.newInstance().local(locale).addList(pcasForImpressions)
                .classElementList(PcaForImpression.class).asXls().outputName(path).build();

        watch.stop();

        LOG.warn("Output time :{} ms", watch.getTime());
        return file.getAbsolutePath();
    }
}
