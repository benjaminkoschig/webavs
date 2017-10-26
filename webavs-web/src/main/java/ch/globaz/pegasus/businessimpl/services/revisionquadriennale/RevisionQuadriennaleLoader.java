package ch.globaz.pegasus.businessimpl.services.revisionquadriennale;

import globaz.externe.IPRConstantesExternes;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeListUtil.Key;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.common.ExceptionsHandler;
import ch.globaz.common.domaine.Adresse;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Periode;
import ch.globaz.cygnus.business.services.RegimeLoader;
import ch.globaz.cygnus.businessimpl.services.RegimeLoaderImpl;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresContainer;
import ch.globaz.pegasus.business.domaine.membreFamille.MembresFamilles;
import ch.globaz.pegasus.business.domaine.parametre.Parameters;
import ch.globaz.pegasus.business.domaine.pca.PcaRequerantConjoint;
import ch.globaz.pegasus.business.domaine.pca.PcaSituation;
import ch.globaz.pegasus.business.domaine.revisionquadriennale.DemandeAReviser;
import ch.globaz.pegasus.business.domaine.revisionquadriennale.RevisionQuadriennale;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.models.revisionquadriennale.ListrevisionWithPcaRequerantConjoint;
import ch.globaz.pegasus.business.models.revisionquadriennale.ListrevisionWithPcaRequerantConjointSearch;
import ch.globaz.pegasus.businessimpl.services.adresse.AdresseLoader;
import ch.globaz.pegasus.businessimpl.services.adresse.TechnicalExceptionWithTiers;
import ch.globaz.pegasus.businessimpl.services.donneeFinanciere.DonneeFinanciereLoader;
import ch.globaz.pegasus.businessimpl.services.loader.ParametersLoader;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.domaine.Pays;
import ch.globaz.pyxis.loader.PaysLoader;

public class RevisionQuadriennaleLoader {

    public RevisionQuadriennale load(String annee) {

        Periode periode = Periode.resolvePeriodeByYear(annee);
        RevisionQuadriennale revisionQuadriennale = new RevisionQuadriennale(periode);
        try {
            List<ListrevisionWithPcaRequerantConjoint> demandesWithInfosPca = loadInfoPcaConjointRequerant(periode);
            Map<String, PcaRequerantConjoint> mapPcas = convertAllPcaAndGrouByIdroit(demandesWithInfosPca);
            Map<String, List<ListrevisionWithPcaRequerantConjoint>> demandeGroupByIdDroit = groupeDemandeByIdDroit(demandesWithInfosPca);

            List<String> listeIdDroitAReviser = getIdsDroit(demandeGroupByIdDroit);
            Map<String, DonneesFinancieresContainer> donnerFianciereGroupByIdDroit = DonneeFinanciereLoader
                    .loadByIdsDroitAndGroupByIdDroit(listeIdDroitAReviser);

            List<String> idsTiersForAdresse = resolveIdsTiersForAdresse(mapPcas);
            AdresseLoader adresseLoader = new AdresseLoader();
            Map<String, List<Adresse>> mapAdressesCourrier = adresseLoader.loadLastByIdsTiersAndGroupByIdTiers(
                    idsTiersForAdresse, IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                    AdresseService.CS_TYPE_COURRIER);

            Map<String, List<Adresse>> mapAdressesDomicile = adresseLoader.loadLastByIdsTiersAndGroupByIdTiers(
                    idsTiersForAdresse, IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                    AdresseService.CS_TYPE_DOMICILE);

            ParametersLoader parameterLoader = new ParametersLoader();
            Parameters parameters = parameterLoader.load();
            PaysLoader paysLoader = new PaysLoader();

            MembreFamilleLoader membreFamilleLoader = new MembreFamilleLoader();
            Map<String, MembresFamilles> mapMembreFamille = membreFamilleLoader
                    .loadMembreFamilleComprisDansLeCalculAndGroupByIdDroit(resolveIdsPca(demandesWithInfosPca));

            Map<String, Regimes> mapRegimesByIdDroit = loadRegimes(mapMembreFamille);

            for (Entry<String, List<ListrevisionWithPcaRequerantConjoint>> entry : demandeGroupByIdDroit.entrySet()) {
                ListrevisionWithPcaRequerantConjoint revision = entry.getValue().get(0);

                try {
                    String idDroit = entry.getKey();
                    DonneesFinancieresContainer donneesFinancieres = donnerFianciereGroupByIdDroit.get(idDroit);
                    if (mapRegimesByIdDroit.containsKey(idDroit)) {
                        donneesFinancieres.addAll(mapRegimesByIdDroit.get(idDroit));
                    }
                    Pays pays = paysLoader.resolveById(revision.getIdPaysRequerant());
                    PcaRequerantConjoint pcas = mapPcas.get(idDroit);
                    Adresse adresseCourrier = resovleAdresse(mapAdressesCourrier, resolveIdTierToUsedForAdresse(pcas));
                    Adresse adresseDomicile = resovleAdresse(mapAdressesDomicile, resolveIdTierToUsedForAdresse(pcas));
                    MembresFamilles membresFamilles;
                    if (mapMembreFamille.containsKey(idDroit)) {
                        membresFamilles = mapMembreFamille.get(idDroit);
                    } else {
                        membresFamilles = new MembresFamilles();
                    }
                    revisionQuadriennale.add(new DemandeAReviser(donneesFinancieres, revision, adresseCourrier,
                            adresseDomicile, pays, pcas, membresFamilles, parameters));
                } catch (Exception e) {
                    ExceptionsHandler.add("Imposible de créer la révision pour ce droit:  " + revision.getIdDroit(),
                            revision.getIdTiersRequerant(), e);
                }

            }

        } catch (JadePersistenceException e1) {
            throw new RuntimeException(e1);
        } catch (DemandeException e) {
            throw new RuntimeException(e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RuntimeException(e);
        }
        ExceptionsHandler.print();
        return revisionQuadriennale;
    }

    String resolveIdTierToUsedForAdresse(PcaRequerantConjoint pcas) {
        String id = null;
        PcaSituation pcaCas = pcas.resolveCasPca();
        if (pcaCas.isCoupleSepareRequerantEnHome()) {
            id = String.valueOf(pcas.getConjoint().getBeneficiaire().getId());
        } else {
            id = String.valueOf(pcas.getRequerant().getBeneficiaire().getId());
        }
        return id;
    }

    private List<String> resolveIdsPca(List<ListrevisionWithPcaRequerantConjoint> list) {
        List<String> ids = new ArrayList<String>();
        for (ListrevisionWithPcaRequerantConjoint pca : list) {
            ids.add(pca.getPcaRequerant().getIdPCAccordee());
        }
        return ids;
    }

    private Adresse resovleAdresse(Map<String, List<Adresse>> mapAdresses, String idTiers) {
        List<Adresse> adresses = mapAdresses.get(idTiers);
        Adresse adresse;
        if (adresses == null) {
            adresse = new Adresse();
        } else {
            adresse = adresses.get(0);
        }
        return adresse;
    }

    private List<String> resolveIdsTiersForAdresse(Map<String, PcaRequerantConjoint> map) {
        List<String> idsTiers = new ArrayList<String>();
        for (Entry<String, PcaRequerantConjoint> entry : map.entrySet()) {
            PcaRequerantConjoint pcas = entry.getValue();
            String id = resolveIdTierToUsedForAdresse(pcas);
            if (id != null) {
                idsTiers.add(id);
            }
        }
        return idsTiers;
    }

    private List<String> getIdsDroit(Map<String, List<ListrevisionWithPcaRequerantConjoint>> demandeGroupByIdDroit) {
        List<String> listeIdDroitAReviser = new ArrayList<String>();
        listeIdDroitAReviser.addAll(demandeGroupByIdDroit.keySet());
        return listeIdDroitAReviser;
    }

    private List<ListrevisionWithPcaRequerantConjoint> loadInfoPcaConjointRequerant(Periode periode)
            throws DemandeException, JadePersistenceException, JadeApplicationServiceNotAvailableException {

        ListrevisionWithPcaRequerantConjointSearch listRevisionsSearch = new ListrevisionWithPcaRequerantConjointSearch();
        listRevisionsSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        // listRevisionsSearch.setDefinedSearchSize(100);

        listRevisionsSearch.setForMoisAnneeGreaterOrEquals(periode.getDateDebut());
        listRevisionsSearch.setForMoisAnneeLessOrEquals(periode.getDateFin());
        listRevisionsSearch.setForMoisDateFin(periode.getDateFin());

        int yearMin = new Date(periode.getDateFin()).getYear() - 6;
        listRevisionsSearch.setForMoinAnneeMinLess("12." + yearMin);

        listRevisionsSearch.setForDateFin(new Date(periode.getDateFin()).getLastDayOfMonth().getValue());
        listRevisionsSearch.setForDateDebut(new Date(periode.getDateDebut()).getFirstDayOfMonth().getValue());

        listRevisionsSearch.setForCsEtatDroit(IPCDroits.CS_VALIDE);

        return PersistenceUtil.search(listRevisionsSearch);

    }

    private Map<String, PcaRequerantConjoint> convertAllPcaAndGrouByIdroit(
            List<ListrevisionWithPcaRequerantConjoint> pcasDb) {
        PcaConverter pcaConverter = new PcaConverter();
        Map<String, PcaRequerantConjoint> map = new HashMap<String, PcaRequerantConjoint>();

        for (ListrevisionWithPcaRequerantConjoint pca : pcasDb) {
            try {
                // if (map.containsKey(pca.getIdDroit())) {
                // throw new RuntimeException("Le cas est double pour cette id droit: " + pca.getIdDroit());
                // }
                map.put(pca.getIdDroit(), convert(pcaConverter, pca));
            } catch (Exception e) {
                map.put(pca.getIdDroit(), new PcaRequerantConjoint());
                throw new TechnicalExceptionWithTiers("Impossible de convertir la pca id:" + pca.getId(),
                        pca.getIdTiersRequerant(), e);
            }
        }

        return map;
    }

    private PcaRequerantConjoint convert(PcaConverter pcaConverter, ListrevisionWithPcaRequerantConjoint pca) {
        // TODO Prestations
        PcaRequerantConjoint pcaRequerantConjoint = new PcaRequerantConjoint();
        pcaRequerantConjoint.setRequerant(pcaConverter.convert(pca.getPcaRequerant()));
        pcaRequerantConjoint.getRequerant().getBeneficiaire().setId(Long.valueOf(pca.getIdTiersRequerant()));
        if (pcaRequerantConjoint.hasConjoint()) {
            pcaRequerantConjoint.setConjoint(pcaConverter.convert(pca.getPcaConjoint()));
            pcaRequerantConjoint.getConjoint().getBeneficiaire().setId(Long.valueOf(pca.getIdTiersConjoint()));
        }
        return pcaRequerantConjoint;
    }

    private Map<String, List<ListrevisionWithPcaRequerantConjoint>> groupeDemandeByIdDroit(
            List<ListrevisionWithPcaRequerantConjoint> demandes) {

        Map<String, List<ListrevisionWithPcaRequerantConjoint>> map = JadeListUtil.groupBy(demandes,
                new Key<ListrevisionWithPcaRequerantConjoint>() {
                    @Override
                    public String exec(ListrevisionWithPcaRequerantConjoint e) {
                        return e.getIdDroit();
                    }
                });

        return map;
    }

    private Map<String, Regimes> loadRegimes(Map<String, MembresFamilles> mapMembreFamille) {

        RegimeLoader regLoader = new RegimeLoaderImpl();
        return regLoader.loadRegimes(mapMembreFamille);

    }
}
