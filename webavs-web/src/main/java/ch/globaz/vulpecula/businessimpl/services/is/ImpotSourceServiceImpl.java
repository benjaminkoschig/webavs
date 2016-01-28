package ch.globaz.vulpecula.businessimpl.services.is;

import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.naos.business.data.AssuranceInfo;
import ch.globaz.vulpecula.business.models.is.EntetePrestationComplexModel;
import ch.globaz.vulpecula.business.models.is.EntetePrestationSearchComplexModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.is.ImpotSourceService;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.is.TauxImpositionNotFoundException;
import ch.globaz.vulpecula.domain.models.is.TauxImpositions;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.util.ExceptionsUtil;
import com.google.common.base.Function;
import com.google.common.collect.Multimaps;

public class ImpotSourceServiceImpl implements ImpotSourceService {

    @Override
    public List<EntetePrestationComplexModel> getEntetesPrestationsIS(String idProcessus) {
        EntetePrestationSearchComplexModel searchModel = new EntetePrestationSearchComplexModel();
        searchModel.setForIdProcessus(idProcessus);
        searchModel.setForIsRetenueImpot(true);
        List<EntetePrestationComplexModel> prestations = RepositoryJade.searchForAndFetch(searchModel);
        return findCaisseAFAndCantonAffilie(prestations);
    }

    @Override
    public List<EntetePrestationComplexModel> getEntetesPrestations(String idProcessus) {
        EntetePrestationSearchComplexModel searchModel = new EntetePrestationSearchComplexModel();
        searchModel.setForIdProcessus(idProcessus);
        List<EntetePrestationComplexModel> prestations = RepositoryJade.searchForAndFetch(searchModel);
        return findCaisseAFAndCantonAffilie(prestations);
    }

    @Override
    public List<PrestationGroupee> getPrestationsForAllocNonIS(Annee annee) {
        TauxImpositions tauxImpositions = VulpeculaRepositoryLocator.getTauxImpositionRepository().findAll();
        List<EntetePrestationComplexModel> prestations = getPrestationsDirectsNonIS(annee.getFirstDayOfYear(),
                annee.getLastDayOfYear());
        Map<String, List<PrestationGroupee>> mapPrestations;
        try {
            mapPrestations = grouperPrestations(prestations, tauxImpositions);
        } catch (TauxImpositionNotFoundException e) {
            // On ne devrait normalement pas se présenter dans ce cas car les non IS ne disposent pas d'impôts à la
            // source.
            throw new IllegalStateException(e);
        }
        return mergeMap(mapPrestations);
    }

    @Override
    public Map<String, PrestationGroupee> getPrestationsForAllocISGroupByCaisseAF(Annee annee) {
        List<EntetePrestationComplexModel> prestationsAF = getPrestationsISParCAF(annee.getFirstDayOfYear(),
                annee.getLastDayOfYear());
        Map<String, Collection<EntetePrestationComplexModel>> prestationsGroupByCaisseAF = groupByCaisseAF(prestationsAF);
        return createMapPrestationsGroupees(annee.getFirstDayOfYear(), annee.getLastDayOfYear(),
                prestationsGroupByCaisseAF);

    }

    @Override
    public Map<String, Collection<PrestationGroupee>> getPrestationsForAllocIS(String canton, String caisseAF,
            Annee annee) throws TauxImpositionNotFoundException {
        TauxImpositions tauxImpositions = VulpeculaRepositoryLocator.getTauxImpositionRepository().findAll();
        List<EntetePrestationComplexModel> prestations = getPrestationsIS(canton, caisseAF, annee);
        Map<String, List<PrestationGroupee>> prestationGroupees = grouperPrestations(prestations, tauxImpositions);
        List<PrestationGroupee> prestationsMergees = mergeMap(prestationGroupees);
        return groupByLibelleCaisseAF(prestationsMergees);
    }

    @Override
    public List<PrestationGroupee> getPrestationsForAllocIS(String idAllocataire, Date dateDebut, Date dateFin)
            throws TauxImpositionNotFoundException {
        TauxImpositions tauxImpositions = VulpeculaRepositoryLocator.getTauxImpositionRepository().findAll();
        List<EntetePrestationComplexModel> prestations = getPrestationsIS(idAllocataire, dateDebut, dateFin);
        Map<String, List<PrestationGroupee>> prestationGroupees = grouperPrestations(prestations, tauxImpositions);
        return mergeMap(prestationGroupees);
    }

    private List<PrestationGroupee> mergeMap(Map<String, List<PrestationGroupee>> mapPrestations) {
        List<PrestationGroupee> prestations = new ArrayList<PrestationGroupee>();
        for (Map.Entry<String, List<PrestationGroupee>> entry : mapPrestations.entrySet()) {
            prestations.addAll(entry.getValue());
        }
        return prestations;
    }

    /**
     * Groupe les prestations de la manière suivante :
     * Si l'on considère les prestations :
     * 
     * <table border="1">
     * <tr>
     * <td>Periode de début</td>
     * <td>Periode de fin</td>
     * <td>Montant</td>
     * </tr>
     * <tr>
     * <td>01.2014</td>
     * <td>01.2014</td>
     * <td>200.-</td>
     * </tr>
     * <tr>
     * <td>02.2014</td>
     * <td>02.2014</td>
     * <td>200.-</td>
     * </tr>
     * <tr>
     * <td>04.2014</td>
     * <td>04.2014</td>
     * <td>200.-</td>
     * </tr>
     * </table>
     * Celles-ci seront regroupées de cette manière.
     * <table border="1">
     * <tr>
     * <td>Date début</td>
     * <td>Date fin</td>
     * <td>Montant</td>
     * </tr>
     * <tr>
     * <td>01.2014</td>
     * <td>02.2014</td>
     * <td>400.-</td>
     * </tr>
     * <tr>
     * <td>04.2014</td>
     * <td>04.2014</td>
     * <td>200.-</td>
     * </tr>
     * </table>
     * 
     * @throws TauxImpositionNotFoundException
     */
    private Map<String, List<PrestationGroupee>> grouperPrestations(List<EntetePrestationComplexModel> prestations,
            TauxImpositions tauxImpositions) throws TauxImpositionNotFoundException {
        Map<String, List<PrestationGroupee>> entetesPrestations = new HashMap<String, List<PrestationGroupee>>();

        // On groupe les cotisations par dossiers
        Map<String, Collection<EntetePrestationComplexModel>> prestationsGroupByDossier = groupByIdDossier(prestations);

        for (Map.Entry<String, Collection<EntetePrestationComplexModel>> entry : prestationsGroupByDossier.entrySet()) {
            List<EntetePrestationComplexModel> prestationsGroupees = orderByPeriode(entry.getValue());

            List<PrestationGroupee> liste = new ArrayList<PrestationGroupee>();
            Montant montantTotal = Montant.ZERO;
            Date dateDebut = null;
            Date dateFin = null;
            Adresse adresse = VulpeculaRepositoryLocator.getAdresseRepository().findAdressePrioriteCourrierByIdTiers(
                    prestationsGroupees.get(0).getIdTiers());
            for (int i = 0; i < prestationsGroupees.size(); i++) {
                EntetePrestationComplexModel entetePrestation = prestationsGroupees.get(i);
                Periode periodePrestation = new Periode(entetePrestation.getPeriodeDe(), entetePrestation.getPeriodeA());

                if (i == 0) {
                    dateDebut = periodePrestation.getDateDebut();
                    dateFin = periodePrestation.getDateFin();
                    montantTotal = montantTotal.add(new Montant(entetePrestation.getMontantTotal()));
                    if (i == prestationsGroupees.size() - 1) {
                        liste.add(createPrestationGroupee(entetePrestation, tauxImpositions, adresse, montantTotal,
                                dateDebut, dateFin));
                    }
                    continue;
                }

                if (periodePrestation.getDateDebut().isMemeMois(dateDebut)
                        || periodePrestation.getDateDebut().suitMois(dateFin)
                        || dateFin.isMemeMois(periodePrestation.getDateDebut())) {
                    dateFin = periodePrestation.getDateFin();
                    montantTotal = montantTotal.add(new Montant(entetePrestation.getMontantTotal()));
                } else {
                    liste.add(createPrestationGroupee(entetePrestation, tauxImpositions, adresse, montantTotal,
                            dateDebut, dateFin));

                    montantTotal = new Montant(entetePrestation.getMontantTotal());
                    dateDebut = periodePrestation.getDateDebut();
                    dateFin = periodePrestation.getDateFin();
                }
                if (i == prestationsGroupees.size() - 1) {
                    liste.add(createPrestationGroupee(entetePrestation, tauxImpositions, adresse, montantTotal,
                            dateDebut, dateFin));
                }
            }

            entetesPrestations.put(entry.getKey(), liste);
        }
        return entetesPrestations;
    }

    /**
     * Conversion d'un EntetePrestationComplexModel en EntetePrestation avec calcul de l'impôts à la source depuis le
     * paramétrage si celui-ci est soumis.
     * 
     * @param entetePrestationComplexModel
     * @param tauxImpositions
     * @param adresse
     * @param montantTotal
     * @param dateDebut
     * @param dateFin
     * @return
     * @throws TauxImpositionNotFoundException
     */
    private PrestationGroupee createPrestationGroupee(EntetePrestationComplexModel entetePrestationComplexModel,
            TauxImpositions tauxImpositions, Adresse adresse, Montant montantTotal, Date dateDebut, Date dateFin)
            throws TauxImpositionNotFoundException {
        PrestationGroupee entetePrestation = new PrestationGroupee();
        entetePrestation.setNss(entetePrestationComplexModel.getNumAvsActuel());
        entetePrestation.setNom(entetePrestationComplexModel.getNom());
        entetePrestation.setPrenom(entetePrestationComplexModel.getPrenom());
        entetePrestation.setDateNaissance(new Date(entetePrestationComplexModel.getDateNaissance()));
        entetePrestation.setReferencePermis(entetePrestationComplexModel.getReferencePermis());
        entetePrestation.setDebutVersement(dateDebut);
        entetePrestation.setFinVersement(dateFin);
        entetePrestation.setMontantPrestations(montantTotal);
        if (entetePrestationComplexModel.getRetenueImpot()) {
            entetePrestation.setImpots(montantTotal.multiply(tauxImpositions.getTauxImpotSource(
                    entetePrestationComplexModel.getCantonResidence(), dateDebut)));
        } else {
            entetePrestation.setImpots(Montant.ZERO);
        }
        entetePrestation.setLibelleCaisseAF(entetePrestationComplexModel.getLibelleCaisseAF());
        entetePrestation.setCodeCaisseAF(entetePrestationComplexModel.getCodeCaisseAF());
        if (adresse != null) {
            entetePrestation.setNpa(adresse.getNpa());
            entetePrestation.setLocalite(adresse.getLocalite());
            entetePrestation.setAdresseFormattee(adresse.getAdresseFormatte());
        }
        entetePrestation.setTitre(entetePrestationComplexModel.getTitre());
        entetePrestation.setRaisonSociale(entetePrestationComplexModel.getRaisonSociale());
        if (!JadeStringUtil.isEmpty(entetePrestationComplexModel.getLangue())) {
            entetePrestation.setLangue(CodeLangue.fromValue(entetePrestationComplexModel.getLangue()));
        }
        return entetePrestation;
    }

    private Map<String, PrestationGroupee> createMapPrestationsGroupees(Date dateDebut, Date dateFin,
            Map<String, Collection<EntetePrestationComplexModel>> prestationsGroupByCaisseAF) {
        TauxImpositions tauxImpositions = VulpeculaRepositoryLocator.getTauxImpositionRepository().findAll();

        Map<String, PrestationGroupee> prestationsGroupees = new HashMap<String, PrestationGroupee>();

        for (Map.Entry<String, Collection<EntetePrestationComplexModel>> entry : prestationsGroupByCaisseAF.entrySet()) {
            List<EntetePrestationComplexModel> prestations = new ArrayList<EntetePrestationComplexModel>(
                    entry.getValue());
            EntetePrestationComplexModel prestationComplexModel = prestations.get(0);
            Adresse adresse = VulpeculaRepositoryLocator.getAdresseRepository().findAdressePrioriteCourrierByIdTiers(
                    prestationComplexModel.getIdTiers());

            Montant montantTotal = calculMontantTotal(entry.getValue());
            Montant impot = calculImpots(prestations, tauxImpositions);
            Montant frais = calculFrais(prestations, tauxImpositions);

            PrestationGroupee prestationGroupee = PrestationGroupee.create(montantTotal, impot, frais, dateDebut,
                    dateFin, adresse, prestationComplexModel.getLibelleCaisseAF(),
                    prestationComplexModel.getCodeCaisseAF(), prestationComplexModel);
            prestationsGroupees.put(entry.getKey(), prestationGroupee);
        }
        return prestationsGroupees;
    }

    private List<EntetePrestationComplexModel> orderByPeriode(
            Collection<EntetePrestationComplexModel> prestationsAOrdrer) {
        List<EntetePrestationComplexModel> prestationsGroupees = new ArrayList<EntetePrestationComplexModel>(
                prestationsAOrdrer);
        Collections.sort(prestationsGroupees, new Comparator<EntetePrestationComplexModel>() {
            @Override
            public int compare(EntetePrestationComplexModel prestation1, EntetePrestationComplexModel prestation2) {
                return new Periode(prestation1.getPeriodeDe(), prestation1.getPeriodeA()).compareTo(new Periode(
                        prestation2.getPeriodeDe(), prestation2.getPeriodeA()));
            }
        });
        return prestationsGroupees;
    }

    /**
     * Groupe un ensemble de prestations par idDossier.
     * 
     * @param prestations Ensemble de prestations
     * @return Map de prestations groupées par idDossier
     */
    private Map<String, Collection<EntetePrestationComplexModel>> groupByIdDossier(
            Collection<EntetePrestationComplexModel> prestations) {
        return Multimaps.index(prestations, new Function<EntetePrestationComplexModel, String>() {
            @Override
            public String apply(EntetePrestationComplexModel prestation) {
                return prestation.getIdTiers();
            }
        }).asMap();
    }

    /**
     * Groupe un ensemble de prestations par caisseAF.
     * 
     * @param prestations Ensemble de prestations
     * @return Map de prestations groupées par caisseAF
     */
    private Map<String, Collection<EntetePrestationComplexModel>> groupByCaisseAF(
            Collection<EntetePrestationComplexModel> prestations) {
        Function<EntetePrestationComplexModel, String> funcGroupLibelleCaisseAF = new Function<EntetePrestationComplexModel, String>() {
            @Override
            public String apply(EntetePrestationComplexModel entetePrestationComplexModel) {
                return entetePrestationComplexModel.getLibelleCaisseAF();
            }
        };
        return Multimaps.index(prestations, funcGroupLibelleCaisseAF).asMap();
    }

    /**
     * Groupe un ensemble de prestation groupées par caisseAF.
     * 
     */
    private Map<String, Collection<PrestationGroupee>> groupByLibelleCaisseAF(Collection<PrestationGroupee> prestations) {
        Function<PrestationGroupee, String> funcGroupLibelleCaisseAF = new Function<PrestationGroupee, String>() {
            @Override
            public String apply(PrestationGroupee prestationGroupee) {
                return prestationGroupee.getLibelleCaisseAF();
            }
        };
        return Multimaps.index(prestations, funcGroupLibelleCaisseAF).asMap();
    }

    /**
     * Calcul du montant total d'un ensemble de prestation.
     * 
     * @param prestations Liste d'opérations
     * @return Montant total des prestations
     */
    private Montant calculMontantTotal(Collection<EntetePrestationComplexModel> prestations) {
        // Si la liste des opérations est null, c'est qu'il n'y a pas d'opérations et donc que le montant est égal à 0.
        if (prestations == null) {
            return Montant.ZERO;
        }

        Montant montant = Montant.ZERO;
        for (EntetePrestationComplexModel prestation : prestations) {
            montant = montant.add(new Montant(prestation.getMontantTotal()));
        }
        return montant;
    }

    /**
     * Calcul les frais d'administration
     * 
     * @param prestations Liste des prestations sur lesquelles calculer l'IS
     * @param tauxImpositions
     * @return Montant des impots
     */
    private Montant calculImpots(Collection<EntetePrestationComplexModel> prestations, TauxImpositions tauxImpositions) {
        // Si la liste des opérations est null, c'est qu'il n'y a pas d'opérations et donc que le montant est égal à 0.
        if (prestations == null) {
            return Montant.ZERO;
        }

        Montant impot = Montant.ZERO;
        for (EntetePrestationComplexModel prestation : prestations) {
            Taux taux = null;
            try {
                taux = tauxImpositions.getTauxImpotSource(prestation.getCantonResidence(),
                        new Date(prestation.getPeriodeDe()));
            } catch (TauxImpositionNotFoundException ex) {
                ExceptionsUtil.translateAndThrowUncheckedException(ex);
            }
            impot = impot.add(new Montant(prestation.getMontantTotal()).multiply(taux));
        }
        return impot;
    }

    /**
     * Calcul des frais d'administation
     * 
     * @param prestations Liste des prestations sur lesquelles calculer les frais
     * @param tauxImpositions Taux d'imposition
     * @return Montant des frais
     */
    private Montant calculFrais(Collection<EntetePrestationComplexModel> prestations, TauxImpositions tauxImpositions) {
        // Si la liste des opérations est null, c'est qu'il n'y a pas d'opérations et donc que le montant est égal à 0.
        if (prestations == null) {
            return Montant.ZERO;
        }

        Montant montantTotalFrais = Montant.ZERO;
        for (EntetePrestationComplexModel prestation : prestations) {
            Montant montant = new Montant(prestation.getMontantTotal());

            Taux tauxFrais = null;
            Taux tauxIS = null;

            try {
                tauxFrais = tauxImpositions.getTauxComissionPerception(prestation.getCantonResidence(), new Date(
                        prestation.getPeriodeDe()));
                tauxIS = tauxImpositions.getTauxImpotSource(prestation.getCantonResidence(),
                        new Date(prestation.getPeriodeDe()));
            } catch (TauxImpositionNotFoundException ex) {
                ExceptionsUtil.translateAndThrowUncheckedException(ex);
            }
            Montant frais = montant.multiply(tauxIS).multiply(tauxFrais);
            montantTotalFrais = montantTotalFrais.add(frais);
        }
        return montantTotalFrais;
    }

    /**
     * Retourne l'ensemble des prestations comptabilisées relatives aux allocataires imposée à la source.
     * Seules les prestations en bonification "DIRECT" sont retournées.
     * 
     * @param dateDebut Date de début à laquelle prendre les prestations
     * @param dateFin Date de fin à laquelle prendre les prestations
     * @return Liste des prestations
     */
    private List<EntetePrestationComplexModel> getPrestationsISParCAF(Date dateDebut, Date dateFin) {
        EntetePrestationSearchComplexModel searchModel = new EntetePrestationSearchComplexModel();
        searchModel.setForDateComptabilisationAfterOrEquals(dateDebut);
        searchModel.setForDateComptabilisationBeforeOrEquals(dateFin);
        searchModel.setForEtat(ALCSPrestation.ETAT_CO);
        searchModel.setForIsRetenueImpot(true);
        searchModel.setForBonification(ALCSPrestation.BONI_DIRECT);
        List<EntetePrestationComplexModel> prestations = RepositoryJade.searchForAndFetch(searchModel);
        return findCaisseAFAndCantonAffilie(prestations);
    }

    /**
     * Retourne l'ensemble des prestations comptabilisées relatives à un allocataire (optionnel) imposée à la source.
     * Seules les prestations en bonification "DIRECT" sont retournées.
     * 
     * @param idAllocataire String représentant l'id d'un allocataire
     * @param dateDebut Date de début à laquelle prendre les prestations
     * @param dateFin Date de fin à laquelle prendre les prestations
     * @return Liste des prestations
     */
    private List<EntetePrestationComplexModel> getPrestationsIS(String idAllocataire, Date dateDebut, Date dateFin) {
        EntetePrestationSearchComplexModel searchModel = new EntetePrestationSearchComplexModel();
        searchModel.setForIdAllocataire(idAllocataire);
        searchModel.setForPeriodeDeAfterOrEquals(dateDebut);
        searchModel.setForPeriodeDeBeforeOrEquals(dateFin);
        searchModel.setForEtat(ALCSPrestation.ETAT_CO);
        searchModel.setForIsRetenueImpot(true);
        searchModel.setForBonification(ALCSPrestation.BONI_DIRECT);
        List<EntetePrestationComplexModel> prestations = RepositoryJade.searchForAndFetch(searchModel);
        return findCaisseAFAndCantonAffilie(prestations);
    }

    /**
     * Retourne l'ensemble des prestations en paiements directs relatives à un allocataires NON imposée à la source.
     * 
     * @param dateDebut Date de début à laquelle prendre les prestations
     * @param dateFin Date de fin à laquelle prendre les prestations
     * @return Liste des prestations
     */
    private List<EntetePrestationComplexModel> getPrestationsDirectsNonIS(Date dateDebut, Date dateFin) {
        EntetePrestationSearchComplexModel searchModel = new EntetePrestationSearchComplexModel();
        searchModel.setForPeriodeDeAfterOrEquals(dateDebut);
        searchModel.setForPeriodeDeBeforeOrEquals(dateFin);
        searchModel.setForEtat(ALCSPrestation.ETAT_CO);
        searchModel.setForIsRetenueImpot(false);
        List<EntetePrestationComplexModel> prestations = RepositoryJade.searchForAndFetch(searchModel);
        return findCaisseAFAndCantonAffilie(prestations);
    }

    /**
     * Retourne l'ensemble des prestations comptabilisées relatives à aux allocataires imposée à la source selon un
     * canton
     * (optionnel), une caisseAf (optionnel) et une année. Seules les prestations en bonification "DIRECT" sont
     * retournées.
     * 
     * @param canton Code système représentant le canton
     * @param caisseAF Code système représentant le type de la caisse AF
     * @param annee Année pour la recherche des écritures
     * @return Liste des prestations
     */
    private List<EntetePrestationComplexModel> getPrestationsIS(String canton, String caisseAF, Annee annee) {
        EntetePrestationSearchComplexModel searchModel = new EntetePrestationSearchComplexModel();
        searchModel.setForPeriodeDeAfterOrEquals(annee.getFirstDayOfYear());
        searchModel.setForPeriodeDeBeforeOrEquals(annee.getLastDayOfYear());
        searchModel.setForCantonResidence(canton);
        searchModel.setForEtat(ALCSPrestation.ETAT_CO);
        searchModel.setForIsRetenueImpot(true);
        searchModel.setForBonification(ALCSPrestation.BONI_DIRECT);
        List<EntetePrestationComplexModel> prestations = RepositoryJade.searchForAndFetch(searchModel);
        return findCaisseAFAndCantonAffilie(prestations, caisseAF);
    }

    private List<EntetePrestationComplexModel> findCaisseAFAndCantonAffilie(
            List<EntetePrestationComplexModel> prestations) {
        return findCaisseAFAndCantonAffilie(prestations, null);
    }

    /**
     * Recherche la caisse AF d'une prestation et ne retourne que les prestations qui contiennent la caisse AF passé en
     * paramètre. Si celle-ci est null, toutes les caisses AF sont retournées.
     * 
     * @param prestations Liste de prestations
     * @param codeCaisseAF Caisse AF sur laquelle filtrer
     * @return Liste de prestations filtré selon le codeCaisseAF
     */
    private List<EntetePrestationComplexModel> findCaisseAFAndCantonAffilie(
            List<EntetePrestationComplexModel> prestations, String codeCaisseAF) {
        List<EntetePrestationComplexModel> prestationsFiltrees = new ArrayList<EntetePrestationComplexModel>();

        CaisseAFProvider caisseAFProvider = new CaisseAFProvider();
        for (EntetePrestationComplexModel prestation : prestations) {
            AssuranceInfo assuranceInfo = caisseAFProvider.get(prestation.getNumeroAffilie(),
                    prestation.getActiviteAllocataire(), new Date(prestation.getPeriodeDe()));
            prestation.setLibelleCaisseAF(assuranceInfo.getLibelleCourt());
            prestation.setCodeCaisseAF(assuranceInfo.getCodeCaisseProf());
            if (JadeStringUtil.isEmpty(codeCaisseAF) || assuranceInfo.getCodeCaisseProf().equals(codeCaisseAF)) {
                prestationsFiltrees.add(prestation);
            }
            prestation.setCantonResidence(assuranceInfo.getCanton());
        }
        return prestationsFiltrees;
    }
}
