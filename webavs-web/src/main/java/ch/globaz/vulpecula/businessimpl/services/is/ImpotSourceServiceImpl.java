package ch.globaz.vulpecula.businessimpl.services.is;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.vulpecula.business.exception.VulpeculaException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexModel;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
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
import ch.globaz.vulpecula.domain.models.is.DetailPrestationAF;
import ch.globaz.vulpecula.domain.models.is.TauxImpositionNotFoundException;
import ch.globaz.vulpecula.domain.models.is.TauxImpositions;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.util.DBUtil;
import ch.globaz.vulpecula.util.ExceptionsUtil;
import com.google.common.base.Function;
import com.google.common.collect.Multimaps;

public class ImpotSourceServiceImpl implements ImpotSourceService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

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
    public Map<String, Collection<DetailPrestationAF>> getPrestationsForListAFVersees(String dateDebut, String dateFin)
            throws VulpeculaException {
        Date dateDebutFormated = new Date(dateDebut);
        Date dateFinFormated = new Date(dateFin);
        // récupérer la liste des prestations détaillées
        List<DetailPrestationAF> detailPrestationsAFList = getPrestationsWithDetailQuery(
                dateDebutFormated.getFirstDayOfMonth(), dateFinFormated.getLastDayOfMonth());
        // si la liste n'est pas vide, on fait fructifier le contenu des objets
        if (detailPrestationsAFList != null && !detailPrestationsAFList.isEmpty()) {
            detailPrestationsAFList = resolveCaisseAFAndCantonAffilie(detailPrestationsAFList);
            TauxImpositions tauxImpositions = VulpeculaRepositoryLocator.getTauxImpositionRepository().findAll();
            calculImpotSourceForAllPrestations(detailPrestationsAFList, tauxImpositions);
        }
        Map<String, Collection<DetailPrestationAF>> prestationsGroupedByCaisseAF = groupByCaisseAFForPrestationsDetaillees(detailPrestationsAFList);
        return prestationsGroupedByCaisseAF;
    }

    /**
     * 
     * Cette méthode permet de récupérer tous les cas d'entetes et de details de prestations compris entre une date x et
     * une date y.
     * 
     * @param dateDebut
     * @param dateFin
     * @return List<DetailPrestationAF>
     * @throws VulpeculaException
     */
    private List<DetailPrestationAF> getPrestationsWithDetailQuery(Date dateDebut, Date dateFin)
            throws VulpeculaException {

        StringBuilder sbsql = new StringBuilder();
        // la requête suivante permet de sommer les prestations avec leur détail par ligne. Cela permettra d'obtenir le
        // tiers bénéficiaire de la prestation.
        sbsql.append(
                "SELECT SUM(ALDETPRE1.NMONT) AS MONTANT_PRESTATION, ALDETPRE1.HTITIE AS ID_TIERS_BENEFICIAIRE, ALENTPRE1.MPERD AS ALENTPRE1_MPERD, ALDOS1.CSCAAL AS ALDOS1_CSCAAL,")
                .append(" TITIERP1.HTTTTI AS TITIERP1_HTTTTI, TITIERP1.HTLDE2 AS TITIERP1_HTLDE2, TIPAVSP1.HXNAVS AS TIPAVSP1_HXNAVS, TIPERSP1.HPDNAI AS TIPERSP1_HPDNAI, PT_TRAVAILLEURS1.REFERENCE_PERMIS AS PT_TRAVAILLEURS1_REFERENCE_PERMIS,")
                .append(" TITIERP1.HTLDE1 AS TITIERP1_HTLDE1, ALENTPRE1.MPERA AS ALENTPRE1_MPERA, AFAFFIP1.MADESL AS AFAFFIP1_MADESL, ALALLOC1.HTITIE AS ALALLOC1_HTITIE, ALENTPRE1.MDVC AS ALENTPRE1_MDVC,")
                .append(" PT_EMPLOYEURS1.CS_TYPE_FACTURATION AS PT_EMPLOYEURS1_CS_TYPE_FACTURATION, AFAFFIP1.MALNAF AS AFAFFIP1_MALNAF, AFAFFIP1.HTITIE AS AFAFFIP1_HTITIE, ALENTPRE1.AFID AS ALENTPRE1_AFID,")
                .append(" ALDOS1.EIMPOT AS ALDOS1_EIMPOT, TITIERP1.HTTLAN AS TITIERP1_HTTLAN, ALDOS1.EID AS ALDOS1_EID, ALRECAP1.LNOFA AS ALRECAP1_LNOFA, ALENTPRE1.MID AS ALENTPRE1_MID,")
                .append(" TITIERP2.HTLDE1 AS NOM_TIERS_BENEFICIAIRE, TITIERP2.HTLDE2 AS PRENOM_TIERS_BENEFICIAIRE")
                .append(" FROM SCHEMA.ALENTPRE ALENTPRE1")
                .append(" INNER JOIN SCHEMA.ALDOS ALDOS1 ON ( ALENTPRE1.EID=ALDOS1.EID )")
                .append(" INNER JOIN SCHEMA.ALALLOC ALALLOC1 ON ( ALDOS1.BID=ALALLOC1.BID )")
                .append(" INNER JOIN SCHEMA.TIPAVSP TIPAVSP1 ON ( ALALLOC1.HTITIE=TIPAVSP1.HTITIE )")
                .append(" INNER JOIN SCHEMA.AFAFFIP AFAFFIP1 ON ( ALDOS1.MALNAF=AFAFFIP1.MALNAF )")
                .append(" LEFT JOIN SCHEMA.PT_EMPLOYEURS PT_EMPLOYEURS1 ON (AFAFFIP1.MAIAFF=PT_EMPLOYEURS1.ID_AFAFFIP)")
                .append(" INNER JOIN SCHEMA.ALRECAP ALRECAP1 ON ( ALENTPRE1.LID=ALRECAP1.LID )")
                .append(" INNER JOIN SCHEMA.TITIERP TITIERP1 ON ( ALALLOC1.HTITIE=TITIERP1.HTITIE )")
                .append(" INNER JOIN SCHEMA.TIPERSP TIPERSP1 ON ( ALALLOC1.HTITIE=TIPERSP1.HTITIE )")
                .append(" LEFT JOIN SCHEMA.PT_TRAVAILLEURS PT_TRAVAILLEURS1 ON (ALALLOC1.HTITIE=PT_TRAVAILLEURS1.ID_TITIERP)")
                .append(" INNER JOIN SCHEMA.ALDETPRE ALDETPRE1 ON (ALDETPRE1.MID = ALENTPRE1.MID)")
                .append(" LEFT JOIN WEBAVSS.TITIERP TITIERP2 ON (ALDETPRE1.HTITIE = TITIERP2.HTITIE)")
                .append(" WHERE ( ALENTPRE1.MDVC<=")
                .append(dateFin.getValue())
                .append(" AND ALENTPRE1.MDVC>=")
                .append(dateDebut.getValue())
                .append(")")
                // a l'état comptabilisées
                .append(" AND ALENTPRE1.CSETAT = 61170001")
                .append(" GROUP BY ALDETPRE1.HTITIE, ALENTPRE1.MPERD, ALDOS1.CSCAAL, TITIERP1.HTTTTI, TITIERP1.HTLDE2, TIPAVSP1.HXNAVS, TIPERSP1.HPDNAI,")
                .append(" PT_TRAVAILLEURS1.REFERENCE_PERMIS, TITIERP1.HTLDE1, ALENTPRE1.MPERA, AFAFFIP1.MADESL, ALALLOC1.HTITIE, ALENTPRE1.MDVC, PT_EMPLOYEURS1.CS_TYPE_FACTURATION,")
                .append(" AFAFFIP1.MALNAF, AFAFFIP1.HTITIE, ALENTPRE1.AFID, ALDOS1.EIMPOT, TITIERP1.HTTLAN, ALDOS1.EID, ALRECAP1.LNOFA, ALENTPRE1.MID,")
                .append(" TITIERP2.HTLDE1, TITIERP2.HTLDE2")
                .append(" ORDER BY AFAFFIP1_MADESL, TITIERP1_HTLDE1, TITIERP1_HTLDE2,  ALENTPRE1_MPERD ASC");

        List<HashMap<String, Object>> queryContentResult;
        try {
            queryContentResult = DBUtil.executeQuery(sbsql.toString(), getClass());
        } catch (JadePersistenceException e) {
            throw new VulpeculaException(e.getMessage());
        }

        return prepareListeDetailAF(queryContentResult);

    }

    /**
     * Cette méthode permet de préparer une liste d'objets DetailPrestationAF
     * 
     * @param queryContentResult
     * @return List<DetailPrestationAF>
     */
    private List<DetailPrestationAF> prepareListeDetailAF(List<HashMap<String, Object>> queryContentResult) {
        // Construction des objets de type DetailPrestationAF (contient l'entête + les détails associés)
        List<DetailPrestationAF> detailPrestationsAFList = new ArrayList<DetailPrestationAF>();
        for (HashMap<String, Object> prest : queryContentResult) {

            DetailPrestationAF detailPrestAF = new DetailPrestationAF();
            Montant montantPrestation = Montant.valueOf((BigDecimal) prest.get("MONTANT_PRESTATION"));
            detailPrestAF.setMontantPrestation(montantPrestation);
            detailPrestAF.setIdTiersBeneficiaire(String.valueOf(prest.get("ID_TIERS_BENEFICIAIRE")));
            detailPrestAF.setPeriodeDebut(String.valueOf(prest.get("ALENTPRE1_MPERD")));
            detailPrestAF.setActiviteAllocataire(String.valueOf(prest.get("ALDOS1_CSCAAL")));
            detailPrestAF.setTitreAllocataire(String.valueOf(prest.get("TITIERP1_HTTTTI")));
            detailPrestAF.setPrenomAllocataire(String.valueOf(prest.get("TITIERP1_HTLDE2")));
            detailPrestAF.setNssAllocataire(String.valueOf(prest.get("TIPAVSP1_HXNAVS")));
            detailPrestAF.setDateNaissanceAllocataire(String.valueOf(prest.get("TIPERSP1_HPDNAI")));
            detailPrestAF.setReferencePermis(String.valueOf(prest.get("PT_TRAVAILLEURS1_REFERENCE_PERMIS")));
            detailPrestAF.setNomAllocataire(String.valueOf(prest.get("TITIERP1_HTLDE1")));
            detailPrestAF.setPeriodeFin(String.valueOf(prest.get("ALENTPRE1_MPERA")));
            detailPrestAF.setRaisonSocialEmployeur(String.valueOf(prest.get("AFAFFIP1_MADESL")));
            detailPrestAF.setIdTiersAllocataire(String.valueOf(prest.get("ALALLOC1_HTITIE")));
            detailPrestAF.setDateComptabilisation(String.valueOf(prest.get("ALENTPRE1_MDVC")));
            detailPrestAF.setTypeFacturation(String.valueOf(prest.get("PT_EMPLOYEURS1_CS_TYPE_FACTURATION")));
            detailPrestAF.setNumeroAffilie(String.valueOf(prest.get("AFAFFIP1_MALNAF")));
            detailPrestAF.setIdTiersEmployeur(String.valueOf(prest.get("AFAFFIP1_HTITIE")));
            detailPrestAF.setNumeroFacture(String.valueOf(prest.get("ALENTPRE1_AFID")));
            String hasImpot = String.valueOf(prest.get("ALDOS1_EIMPOT"));
            if ("1".equals(hasImpot)) {
                detailPrestAF.setImpotSource(true);
            }
            detailPrestAF.setLangueTiersAllocataire(String.valueOf(prest.get("TITIERP1_HTTLAN")));
            detailPrestAF.setNumeroDossier(String.valueOf(prest.get("ALDOS1_EID")));
            detailPrestAF.setNumeroFacture(String.valueOf(prest.get("ALRECAP1_LNOFA")));
            detailPrestAF.setId(String.valueOf(prest.get("ALENTPRE1_MID")));
            detailPrestAF.setNomTiersBeneficiaire(String.valueOf(prest.get("NOM_TIERS_BENEFICIAIRE")));
            detailPrestAF.setPrenomTiersBeneficiaire(String.valueOf(prest.get("PRENOM_TIERS_BENEFICIAIRE")));

            if ("0".equals(detailPrestAF.getIdTiersBeneficiaire())) {
                detailPrestAF.setIsBenefEmployeur(true);
            }

            detailPrestationsAFList.add(detailPrestAF);
        }

        return detailPrestationsAFList;
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
    public Map<String, PrestationGroupee> getPrestationsForAllocISGroupByCaisseAF(Annee annee, String canton) {
        List<EntetePrestationComplexModel> prestationsAF = getPrestationsISParCAF(annee.getFirstDayOfYear(),
                annee.getLastDayOfYear(), canton);
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
            List<Montant> montantTotal = new ArrayList<Montant>();
            Date dateDebut = null;
            Date dateFin = null;

            for (int i = 0; i < prestationsGroupees.size(); i++) {
                EntetePrestationComplexModel entetePrestation = prestationsGroupees.get(i);
                Periode periodePrestation = new Periode(entetePrestation.getPeriodeDe(), entetePrestation.getPeriodeA());
                Adresse adresse = getAdresseForAttestation(prestationsGroupees.get(i));
                if (ALCSPrestation.BONI_INDIRECT.equals(entetePrestation.getBonification())) {
                    break;
                }

                if (i == 0) {
                    dateDebut = periodePrestation.getDateDebut();
                    dateFin = periodePrestation.getDateFin();
                    montantTotal.add(new Montant(entetePrestation.getMontantTotal()));
                    if (i == prestationsGroupees.size() - 1) {
                        PrestationGroupee prestationGroupee = createPrestationGroupee(entetePrestation,
                                tauxImpositions, adresse, montantTotal, dateDebut, dateFin);
                        prestationGroupee.setCantonResidence(entetePrestation.getCantonAllocataire());
                        prestationGroupee.setIdTiersBeneficiaire(getIdTiersBeneficiaire(entetePrestation));
                        liste.add(prestationGroupee);
                    }
                    continue;
                }

                if (periodePrestation.getDateDebut().isMemeMois(dateDebut)
                        || periodePrestation.getDateDebut().suitMois(dateFin)
                        || dateFin.isMemeMois(periodePrestation.getDateDebut())) {
                    dateFin = periodePrestation.getDateFin();
                    montantTotal.add(new Montant(entetePrestation.getMontantTotal()));
                } else {
                    PrestationGroupee prestationGroupee = createPrestationGroupee(entetePrestation, tauxImpositions,
                            adresse, montantTotal, dateDebut, dateFin);
                    prestationGroupee.setCantonResidence(entetePrestation.getCantonAllocataire());
                    prestationGroupee.setIdTiersBeneficiaire(getIdTiersBeneficiaire(entetePrestation));
                    liste.add(prestationGroupee);

                    montantTotal = new ArrayList<Montant>();
                    montantTotal.add(new Montant(entetePrestation.getMontantTotal()));
                    dateDebut = periodePrestation.getDateDebut();
                    dateFin = periodePrestation.getDateFin();
                }
                if (i == prestationsGroupees.size() - 1) {
                    PrestationGroupee prestationGroupee = createPrestationGroupee(entetePrestation, tauxImpositions,
                            adresse, montantTotal, dateDebut, dateFin);
                    prestationGroupee.setCantonResidence(entetePrestation.getCantonAllocataire());
                    prestationGroupee.setIdTiersBeneficiaire(getIdTiersBeneficiaire(entetePrestation));
                    liste.add(prestationGroupee);
                }
            }

            entetesPrestations.put(entry.getKey(), liste);
        }
        return entetesPrestations;
    }

    private String getIdTiersBeneficiaire(EntetePrestationComplexModel entetePrestation) {
        DetailPrestationComplexSearchModel searchModel = new DetailPrestationComplexSearchModel();
        searchModel.setForIdEntete(entetePrestation.getId());
        try {
            searchModel = ALServiceLocator.getDetailPrestationComplexModelService().search(searchModel);
        } catch (JadeApplicationServiceNotAvailableException e) {
            LOGGER.error(e.getMessage());
        } catch (JadeApplicationException e) {
            LOGGER.error(e.getMessage());
        } catch (JadePersistenceException e) {
            LOGGER.error(e.getMessage());
        }
        DetailPrestationComplexModel details = (DetailPrestationComplexModel) searchModel.getSearchResults()[0];
        if (entetePrestation.getIdTiers().trim()
                .equals(details.getDetailPrestationModel().getIdTiersBeneficiaire().trim())) {
            return entetePrestation.getIdTiers().trim();
        } else {
            return details.getDetailPrestationModel().getIdTiersBeneficiaire().trim();
        }
    }

    private Adresse getAdresseForAttestation(EntetePrestationComplexModel entetePrestation) {
        DetailPrestationComplexSearchModel searchModel = new DetailPrestationComplexSearchModel();
        searchModel.setForIdEntete(entetePrestation.getId());
        try {
            searchModel = ALServiceLocator.getDetailPrestationComplexModelService().search(searchModel);
        } catch (JadeApplicationServiceNotAvailableException e) {
            LOGGER.error(e.getMessage());
        } catch (JadeApplicationException e) {
            LOGGER.error(e.getMessage());
        } catch (JadePersistenceException e) {
            LOGGER.error(e.getMessage());
        }
        DetailPrestationComplexModel details = (DetailPrestationComplexModel) searchModel.getSearchResults()[0];

        return VulpeculaRepositoryLocator.getAdresseRepository().findAdressePrioriteCourrierByIdTiers(
                findTiersForAdresse(entetePrestation.getIdTiers(), details.getDetailPrestationModel()
                        .getIdTiersBeneficiaire()));
    }

    private String findTiersForAdresse(String idTiersAllocataire, String idTiersBeneficiaire) {
        if (idTiersAllocataire.trim().equals(idTiersBeneficiaire.trim())) {
            return idTiersAllocataire;
        }
        return idTiersBeneficiaire;

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
            TauxImpositions tauxImpositions, Adresse adresse, List<Montant> montantTotal, Date dateDebut, Date dateFin)
            throws TauxImpositionNotFoundException {
        PrestationGroupee entetePrestation = new PrestationGroupee();
        entetePrestation.setNss(entetePrestationComplexModel.getNumAvsActuel());
        entetePrestation.setNom(entetePrestationComplexModel.getNom());
        entetePrestation.setPrenom(entetePrestationComplexModel.getPrenom());
        entetePrestation.setDateNaissance(new Date(entetePrestationComplexModel.getDateNaissance()));
        entetePrestation.setReferencePermis(entetePrestationComplexModel.getReferencePermis());
        entetePrestation.setDebutVersement(dateDebut);
        entetePrestation.setFinVersement(dateFin);
        entetePrestation.setMontantPrestations(getSommeMontant(montantTotal));
        if (entetePrestationComplexModel.getRetenueImpot()) {
            Montant impot = Montant.ZERO;
            for (Montant montant : montantTotal) {
                impot = impot.add(montant
                        .multiply(
                                tauxImpositions.getTauxImpotSource(entetePrestationComplexModel.getCantonResidence(),
                                        dateDebut)).normalize().doubleValue());
            }
            entetePrestation.setImpots(impot);
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

    /**
     * Permet d'additionner les montant d'une liste
     * 
     * @param montants La liste des montants à additionner
     * @return Le montant additionné
     */
    private Montant getSommeMontant(List<Montant> montants) {
        Montant montant = Montant.ZERO;

        for (Montant mont : montants) {
            montant = montant.add(mont);
        }
        return montant;
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
                return prestation.getIdDossier();
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
     * Groupe un ensemble de prestations par caisseAF.
     * 
     * ATTENTION -> Cette fonction est prévue uniquement pour le type d'objet suivant : DetailPrestationAF
     * 
     * @param prestations Ensemble de prestations
     * @return Map de prestations groupées par caisseAF
     */
    private Map<String, Collection<DetailPrestationAF>> groupByCaisseAFForPrestationsDetaillees(
            Collection<DetailPrestationAF> prestations) {
        Function<DetailPrestationAF, String> funcGroupLibelleCaisseAF = new Function<DetailPrestationAF, String>() {
            @Override
            public String apply(DetailPrestationAF entetePrestationComplexModel) {
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
            impot = impot.add(new Montant(prestation.getMontantTotal()).multiply(taux).normalize());
        }
        return impot;
    }

    /**
     * Cette méthode permet de vérifier si la prestation est soumise à l'IS et si c'est le cas, la méthode set l'impôt à
     * la source pour le montant de la prestation.
     * 
     * @param prestations
     * @param tauxImpositions
     */
    private void calculImpotSourceForAllPrestations(Collection<DetailPrestationAF> prestations,
            TauxImpositions tauxImpositions) {

        for (DetailPrestationAF prestation : prestations) {
            Taux taux = null;
            try {
                // si le dossier est de type impôt à la source, on va renseigner le montant d'impôt à la source
                Montant impotSource = Montant.ZERO;
                if (prestation.isImpotSource()) {
                    taux = tauxImpositions.getTauxImpotSource(prestation.getCantonResidence(),
                            new Date(prestation.getPeriodeDebut()));
                    impotSource = prestation.getMontantPrestation().multiply(taux);
                    impotSource = impotSource.normalize();
                }
                prestation.setMontantImpotSource(impotSource);
            } catch (TauxImpositionNotFoundException ex) {
                ExceptionsUtil.translateAndThrowUncheckedException(ex);
            }
        }
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
    private List<EntetePrestationComplexModel> getPrestationsISParCAF(Date dateDebut, Date dateFin, String canton) {
        EntetePrestationSearchComplexModel searchModel = new EntetePrestationSearchComplexModel();
        searchModel.setForDateComptabilisationAfterOrEquals(dateDebut);
        searchModel.setForDateComptabilisationBeforeOrEquals(dateFin);
        searchModel.setForEtat(ALCSPrestation.ETAT_CO);
        searchModel.setForIsRetenueImpot(true);
        // searchModel.setForBonification(ALCSPrestation.BONI_DIRECT);
        searchModel.setForCantonResidence(canton);
        List<EntetePrestationComplexModel> prestations = RepositoryJade.searchForAndFetch(searchModel);
        prestations = sortAndExcludePaiementIndirect(prestations);
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
        searchModel.setForDateComptabilisationAfterOrEquals(dateDebut);
        searchModel.setForDateComptabilisationBeforeOrEquals(dateFin);
        searchModel.setForEtat(ALCSPrestation.ETAT_CO);
        searchModel.setForIsRetenueImpot(true);
        // searchModel.setForBonification(ALCSPrestation.BONI_DIRECT);
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
        searchModel.setForDateComptabilisationAfterOrEquals(dateDebut);
        searchModel.setForDateComptabilisationBeforeOrEquals(dateFin);
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
        searchModel.setForDateComptabilisationAfterOrEquals(annee.getFirstDayOfYear());
        searchModel.setForDateComptabilisationBeforeOrEquals(annee.getLastDayOfYear());
        searchModel.setForCantonResidence(canton);
        searchModel.setForEtat(ALCSPrestation.ETAT_CO);
        searchModel.setForIsRetenueImpot(true);
        List<EntetePrestationComplexModel> prestations = RepositoryJade.searchForAndFetch(searchModel);
        prestations = sortAndExcludePaiementIndirect(prestations);
        return findCaisseAFAndCantonAffilie(prestations, caisseAF);
    }

    private List<EntetePrestationComplexModel> findCaisseAFAndCantonAffilie(
            List<EntetePrestationComplexModel> prestations) {
        return findCaisseAFAndCantonAffilie(prestations, null);
    }

    /**
     * Permet de trier la liste des prestations passée en paramètre et de retirer les objets dont le type de paiement
     * est "INDIRECT"
     * 
     * @param listPrestations
     * @return une liste des prestations dont le type de paiement indirect est retiré.
     */
    private List<EntetePrestationComplexModel> sortAndExcludePaiementIndirect(
            List<EntetePrestationComplexModel> listPrestations) {
        List<EntetePrestationComplexModel> finalPrestationsList = new ArrayList<EntetePrestationComplexModel>();
        for (EntetePrestationComplexModel prestation : listPrestations) {

            if (!prestation.getBonification().equals(ALCSPrestation.BONI_INDIRECT)) {

                finalPrestationsList.add(prestation);
            }
        }
        return finalPrestationsList;
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
            // XXX BUG codeCaisseAF contient l'idTiersAdministration
            if (JadeStringUtil.isEmpty(codeCaisseAF) || assuranceInfo.getIdTiersCaisseProf().equals(codeCaisseAF)) {
                prestationsFiltrees.add(prestation);
            }
            prestation.setCantonResidence(assuranceInfo.getCanton());
        }
        return prestationsFiltrees;
    }

    /**
     * Permet la résolution de la caisse AF pour une liste de prestations passées en paramètres.
     * 
     * @param prestations
     * @return List<DetailPrestationAF>
     */
    private List<DetailPrestationAF> resolveCaisseAFAndCantonAffilie(List<DetailPrestationAF> prestations) {

        List<DetailPrestationAF> prestationsFiltrees = new ArrayList<DetailPrestationAF>();

        CaisseAFProvider caisseAFProvider = new CaisseAFProvider();
        for (DetailPrestationAF prestation : prestations) {
            AssuranceInfo assuranceInfo = caisseAFProvider.get(prestation.getNumeroAffilie(),
                    prestation.getActiviteAllocataire(), new Date(prestation.getPeriodeDebut()));
            prestation.setLibelleCaisseAF(assuranceInfo.getLibelleCourt());
            prestation.setCodeCaisseAF(assuranceInfo.getCodeCaisseProf());
            prestation.setCantonResidence(assuranceInfo.getCanton());
            prestationsFiltrees.add(prestation);
        }
        return prestationsFiltrees;
    }

}
