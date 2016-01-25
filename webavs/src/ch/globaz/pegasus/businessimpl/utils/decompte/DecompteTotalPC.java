package ch.globaz.pegasus.businessimpl.utils.decompte;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.common.domaine.GroupePeriodes;
import ch.globaz.common.domaine.Periode;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.domaine.ListTotal;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.AllocationDeNoelException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.creancier.Creancier;
import ch.globaz.pegasus.business.models.creancier.CreancierSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PcaForDecompte;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoelSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppoint;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppointSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.vo.decompte.CreancierVO;
import ch.globaz.pegasus.business.vo.decompte.DecompteTotalPcVO;
import ch.globaz.pegasus.business.vo.decompte.PCAccordeeDecompteVO;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.pcaccordee.PcaPrecedante;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.utils.periode.GroupePeriodesResolver;
import ch.globaz.utils.periode.GroupePeriodesResolver.EachPeriode;

public class DecompteTotalPC {

    private String dateDebutDecompte = null;
    private String dateDecision = null;
    private DecompteTotalPcVO decompteVO = null;
    /* ide demande */
    private String idDemande = null;
    private String idDroit = null;
    /* Creancier */
    // private List<CreancierVO> creanciers = null;
    /* idversion Droit */
    private String idVersionDroit = null;
    private Map<String, PersonneEtendueComplexModel> mapPersonne = new HashMap<String, PersonneEtendueComplexModel>();
    /* Total des pca */
    private BigDecimal montantTotalPeriodesPCA = null;
    private BigDecimal montantVerifRetro = null;
    /* private String noVersion */
    private String noVersionDroit = null;
    private PCAccordeeSearch pcaForVersion = null;
    private boolean isEffetMoisSuivant = false;

    public DecompteTotalPC(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
        // this.periodesPca = new ArrayList<PCAccordeeDecompteVO>();
        decompteVO = new DecompteTotalPcVO();
        montantTotalPeriodesPCA = new BigDecimal(0);

    }

    private void addAllocationNoelToPca(Map<String, SimpleAllocationNoel> mapAllocationNoel,
            List<PCAccordeeDecompteVO> list) {
        for (PCAccordeeDecompteVO pca : list) {
            if (mapAllocationNoel.containsKey(pca.getIdPca())) {
                pca.setSimpleAllocationNoel(mapAllocationNoel.get(pca.getIdPca()));
                decompteVO.addSimpleAllocationNoel(pca.getSimpleAllocationNoel());
            }
        }
    }

    private int computeNbMonthInPeriodePca(PCAccordeeDecompteVO pcaVo, boolean hasDateDeFinForce) {
        String dateFin = resolveDateFin(pcaVo.getDateFinPeriode(), hasDateDeFinForce);
        // date de debut du decompte
        String dateDebut = pcaVo.getDateDebutPeriode();
        // nombre de mois de decompte
        int nbreMois = JadeDateUtil.getNbMonthsBetween("01." + dateDebut, "01." + dateFin) + 1;
        return nbreMois;
    }

    /**
     * Creation de la liste et du total des creancieers
     * 
     * @throws JadeApplicationException
     * @throws DecisionException
     */
    private void createCreanciers() throws JadePersistenceException, DecisionException, JadeApplicationException {
        // montant total des créances
        BigDecimal totalCreances = new BigDecimal(0);

        CreancierSearch search = new CreancierSearch();
        search.setForIdDemande(idDemande);
        // creancier
        search = PegasusServiceLocator.getCreancierService().search(search);
        // iteration sur les creanciers
        for (JadeAbstractModel creancier : search.getSearchResults()) {
            BigDecimal montantForCreancier = PegasusServiceLocator.getCreanceAccordeeService()
                    .findTotalCreanceVerseByVersionDroitForCreancier(idDemande, idVersionDroit,
                            ((Creancier) creancier).getSimpleCreancier().getIdCreancier());
            // Si le montant deja verse est différent de 0 , on traite la ligne
            if (montantForCreancier.intValue() != 0.0f) {
                // ajout dans le vo
                CreancierVO creancierVO = new CreancierVO();
                creancierVO.setCreancier(((Creancier) creancier).getSimpleCreancier());
                // Description
                creancierVO.setDescription(getCreancierLibelle(((Creancier) creancier).getSimpleCreancier()
                        .getIdTiers()));
                creancierVO.setMontantVerse(montantForCreancier);
                decompteVO.getCreanciers().getList().add(creancierVO);
                totalCreances = totalCreances.add(montantForCreancier);
            }
        }
        // ajout du total
        decompteVO.getCreanciers().setTotal(totalCreances);
    }

    /**
     * Generation de la liste des objets pcaccordeeVO pour le decompte
     * 
     * @param idVersionDroit
     * @return
     * @throws PCAccordeeException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws PmtMensuelException
     * @throws PropertiesException
     * @throws AllocationDeNoelException
     */
    private void createDecomptePca() throws PCAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, PmtMensuelException, AllocationDeNoelException, PropertiesException {

        // Recherce des pca pour la version du droit, avec un idpca parent null
        pcaForVersion = new PCAccordeeSearch();
        pcaForVersion.setForVersionDroit(idVersionDroit);
        pcaForVersion.setWhereKey("forCreateDAC");
        pcaForVersion.setOrderKey("byDateDebut");
        pcaForVersion = PegasusServiceLocator.getPCAccordeeService().search(pcaForVersion);

        // on va setter les paramètres manquant pour le rétro ici (noversion et idDemande)
        // on va setter les pca, pour les prestation verses d'après la recherche
        if (pcaForVersion.getSearchResults().length > 0) {
            setAdditionalParameters((PCAccordee) pcaForVersion.getSearchResults()[0]);
        }

        List<String> idsPca = new ArrayList<String>();

        // Parcours des pca triés par date de début
        for (int cpt = 0; cpt < pcaForVersion.getSearchResults().length; cpt++) {

            PCAccordee pcaToDeal = (PCAccordee) pcaForVersion.getSearchResults()[cpt];
            if (!mapPersonne.containsKey(pcaToDeal.getPersonneEtendue().getTiers().getIdTiers())) {
                mapPersonne.put(pcaToDeal.getPersonneEtendue().getTiers().getIdTiers(), pcaToDeal.getPersonneEtendue());
            }
            if ((pcaToDeal.getSimplePrestationsAccordeesConjoint() != null)
                    && !JadeStringUtil.isBlankOrZero(pcaToDeal.getSimplePrestationsAccordeesConjoint()
                            .getIdTiersBeneficiaire())) {
                if (!mapPersonne.containsKey(pcaToDeal.getPersonneEtendueConjoint().getTiers().getIdTiers())) {
                    mapPersonne.put(pcaToDeal.getPersonneEtendueConjoint().getTiers().getIdTiers(),
                            pcaToDeal.getPersonneEtendueConjoint());
                }
            }

            PCAccordee nextPca = null;
            idsPca.add(pcaToDeal.getId());
            // gestion compteur
            if (cpt < (pcaForVersion.getSearchResults().length - 1)) {
                nextPca = (PCAccordee) pcaForVersion.getSearchResults()[cpt + 1];
                idsPca.add(nextPca.getId());
                // Cas de deux pc pour l même période, séparation par la maladie
                if (pcaToDeal.getSimplePCAccordee().getDateDebut().equals(nextPca.getSimplePCAccordee().getDateDebut())) {
                    addPcaToDecompte(nextPca);
                    addPcaToDecompte(pcaToDeal);
                    // on en traite deux, double incrément
                    cpt++;
                } else {
                    addPcaToDecompte(pcaToDeal);
                }
            } else {
                addPcaToDecompte(pcaToDeal);
            }

            dateDebutDecompte = pcaToDeal.getSimplePCAccordee().getDateDebut();
        }

        if (isEffetMoisSuivant) {
            decompteVO.setTotal(BigDecimal.ZERO);
            decompteVO.setMontantVerifRetro(BigDecimal.ZERO);
            decompteVO.setSousTotalDeduction(BigDecimal.ZERO);
            decompteVO.setSousTotalPCA(BigDecimal.ZERO);
            decompteVO.setPrestationsVerses(new ListTotal<PCAccordeeDecompteVO>());
        } else {
            // on set le montant total
            // Set le montant total

            findAndSetAllocationNoelIfIsUsed(idsPca, decompteVO.getPeriodesPca().getList());
            findAndSetJourAppointlIfIsUsed(idsPca, decompteVO.getPeriodesPca().getList());

            BigDecimal sumJourAppoint = sumJourAppoint(decompteVO.getPeriodesPca().getList());
            decompteVO.getPeriodesPca().setTotal(montantTotalPeriodesPCA.add(sumJourAppoint));
            sort(decompteVO.getPeriodesPca().getList());
        }
    }

    private void addPcaToDecompte(PCAccordee pca) throws PmtMensuelException,
            JadeApplicationServiceNotAvailableException {
        PCAccordeeDecompteVO pcaVo = createPCAccordeeDecomteVO(pca);
        if (pcaVo != null) {
            decompteVO.getPeriodesPca().getList().add(pcaVo);
        }
    }

    private BigDecimal sumJourAppoint(List<PCAccordeeDecompteVO> pcas) {
        BigDecimal sumJourAppoint = new BigDecimal(0);
        for (PCAccordeeDecompteVO pca : pcas) {
            if (pca.getSimpleJoursAppoint() != null) {
                sumJourAppoint = sumJourAppoint.add(new BigDecimal(pca.getSimpleJoursAppoint().getMontantTotal()));
            }
        }
        return sumJourAppoint;
    }

    private void createDetteCompta() throws JadeApplicationServiceNotAvailableException, JadeApplicationException,
            JadePersistenceException {
        decompteVO.setDettesCompta(PegasusServiceLocator.getDetteComptatCompenseService()
                .findListTotalCompenseAsDetteEnComptaVO(idVersionDroit, idDroit));
    }

    /**
     * Creation d'une entite PCAccordeeVO
     * 
     * @param pca
     * @return
     * @throws PmtMensuelException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private PCAccordeeDecompteVO createPCAccordeeDecomteVO(PCAccordee pca) throws PmtMensuelException,
            JadeApplicationServiceNotAvailableException {

        // Objet de retour
        PCAccordeeDecompteVO pcAccordee = new PCAccordeeDecompteVO();

        String dateFinDecompte = null;
        // Calcul du nombre de mois à payer
        if (!JadeStringUtil.isBlank(pca.getSimplePCAccordee().getDateFin())
                && !pca.getSimplePCAccordee().getIsDateFinForce()) {
            dateFinDecompte = pca.getSimplePCAccordee().getDateFin();
        } else {
            // Date prochain paiement, on supprime le jour pour la variable
            dateFinDecompte = JadeDateUtil.convertDateMonthYear(dateDecision);
        }
        // date de debut du decompte
        String dateDebutDecompte = pca.getSimplePCAccordee().getDateDebut();
        // nombre de mois de decompte
        int nbreMois = JadeDateUtil.getNbMonthsBetween("01." + dateDebutDecompte, "01." + dateFinDecompte) + 1;

        // Si nombre de mois plus grand que 0
        if (nbreMois > 0) {
            // nbre mois
            pcAccordee.setNbreMois(nbreMois);

            // Montant pc mensuelle
            BigDecimal montantMensuelPca = new BigDecimal(pca.getSimplePrestationsAccordees().getMontantPrestation());
            // Conjoint
            if (!JadeStringUtil.isBlankOrZero(pca.getSimpleInformationsComptabiliteConjoint().getId())) {
                montantMensuelPca = montantMensuelPca.add(new BigDecimal(pca.getSimplePrestationsAccordeesConjoint()
                        .getMontantPrestation()));
            }
            pcAccordee.setMontantPcaMensuel(montantMensuelPca);

            // montant total
            pcAccordee.setMontantForPeriod(montantMensuelPca.multiply(new BigDecimal(nbreMois)));
            montantTotalPeriodesPCA = montantTotalPeriodesPCA.add(pcAccordee.getMontantForPeriod());
            // Date debut et fin
            pcAccordee.setDateDebutPeriode(dateDebutDecompte);
            pcAccordee.setDateFinPeriode(dateFinDecompte);

            String descTiers = this.getDescriptionTiers(pca);
            pcAccordee.setIdPca(pca.getId());
            pcAccordee.setDescTiers(descTiers);
            pcAccordee.setCsRoleBeneficiaire(pca.getSimplePCAccordee().getCsRoleBeneficiaire());
            // Cs Type pca
            pcAccordee.setCsGenrePCA(pca.getSimplePCAccordee().getCsGenrePC());
        } else {
            return null;
        }

        return pcAccordee;

    }

    private void createPrestationVerses() throws JadePersistenceException, JadeApplicationException {
        /*
         * CalculPrestationsVerses prestationsVerses = new CalculPrestationsVerses(this.noVersionDroit, this.idDemande);
         * this.montantVerifRetro = prestationsVerses.getMontantRetro();
         */
        GroupePeriodes periodes = GroupePeriodesResolver.genearateListPeriode(decompteVO.getPeriodesPca().getList(),
                new EachPeriode<PCAccordeeDecompteVO>() {
                    @Override
                    public String[] dateDebutFin(PCAccordeeDecompteVO t) {
                        return new String[] { t.getDateDebutPeriode(), t.getDateFinPeriode() };
                    }
                });

        List<PcaForDecompte> list = PcaPrecedante.findPcaToReplaced(periodes, idDroit, noVersionDroit);

        boolean first = true;

        BigDecimal sum = new BigDecimal(0);
        List<PCAccordeeDecompteVO> listPcaVo = new ArrayList<PCAccordeeDecompteVO>();
        String dateDebutFirst = null;
        Collections.reverse(list);
        List<String> idsPca = new ArrayList<String>();
        for (PcaForDecompte pca : list) {
            PCAccordeeDecompteVO pcaVo = new PCAccordeeDecompteVO();
            SimplePCAccordee simplePCAccordee = pca.getSimplePCAccordee();

            pcaVo.setCsGenrePCA(pca.getSimplePCAccordee().getCsGenrePC());
            // on plafonne la date de debut
            if (first || pca.getSimplePCAccordee().getDateDebut().equals(dateDebutFirst)) {
                first = false;
                dateDebutFirst = pca.getSimplePCAccordee().getDateDebut();
                pcaVo.setDateDebutPeriode(periodes.getDateDebutMin());
            } else {
                pcaVo.setDateDebutPeriode(pca.getSimplePCAccordee().getDateDebut());
            }
            pcaVo.setDateFinPeriode(resolveDateFin(simplePCAccordee.getDateFin(), simplePCAccordee.getIsDateFinForce()));

            int nbreMois = computeNbMonthInPeriodePca(pcaVo, pca.getSimplePCAccordee().getIsDateFinForce());

            pcaVo.setDescTiers(this.getDescriptionTiers(pca.getSimplePrestationsAccordees(),
                    pca.getSimplePrestationsAccordeesConjoint()));
            pcaVo.setIdPca(pca.getId());
            pcaVo.setMontantForPeriod(new BigDecimal(pca.getMontantPCMensuelle()).multiply(new BigDecimal(nbreMois)));
            pcaVo.setMontantPcaMensuel(new BigDecimal(pca.getMontantPCMensuelle()));
            pcaVo.setNbreMois(nbreMois);
            pcaVo.setCsRoleBeneficiaire(pca.getSimplePCAccordee().getCsRoleBeneficiaire());
            listPcaVo.add(pcaVo);
            sum = sum.add(pcaVo.getMontantForPeriod());
            idsPca.add(pca.getId());
        }
        Collections.reverse(listPcaVo);

        decompteVO.getPeriodesPca().getList();

        List<String> idsPcaToUsedForRestitution = new ArrayList<String>();
        for (PcaForDecompte pcaOld : list) {
            for (PCAccordeeDecompteVO pcaNew : decompteVO.getPeriodesPca().getList()) {
                if (pcaOld.getSimplePCAccordee().getDateDebut().equals(pcaNew.getDateDebutPeriode())) {
                    idsPcaToUsedForRestitution.add(pcaOld.getId());
                }
            }
        }

        findAndSetJourAppointlIfIsUsed(idsPcaToUsedForRestitution, listPcaVo);

        // On ne créer pas de restitution d'alloation de noel
        // findAndSetAllocationNoelIfIsUsed(idsPcaToUsedForRestitution, listPcaVo);

        sort(listPcaVo);
        decompteVO.getPrestationsVerses().getList().addAll(listPcaVo);
        // total
        BigDecimal sumJourAppoint = sumJourAppoint(listPcaVo);

        decompteVO.getPrestationsVerses().setTotal(sum.add(sumJourAppoint));

    }

    private void sort(List<PCAccordeeDecompteVO> list) {
        Collections.sort(list, new Comparator<PCAccordeeDecompteVO>() {
            @Override
            public int compare(PCAccordeeDecompteVO o1, PCAccordeeDecompteVO o2) {

                Periode period1 = new Periode(o1.getDateDebutPeriode(), o1.getDateFinPeriode());
                Periode period2 = new Periode(o2.getDateDebutPeriode(), o2.getDateFinPeriode());
                if (period1.compareTo(period2) != 0) {
                    return -1 * period1.compareTo(period2);
                }
                if (o1.getCsRoleBeneficiaire().equals(o2.getCsRoleBeneficiaire())) {
                    return 0;
                } else if (IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(o1.getCsRoleBeneficiaire())) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
    }

    /**
     * creation du total du decompte
     */
    private void createTotal() {
        BigDecimal total = new BigDecimal(0);
        BigDecimal soutTotalPCA = new BigDecimal(0);
        BigDecimal soutTotalDeduction = new BigDecimal(0);

        // BigDecimal totalRetro = this.decompteVO.get
        // ajout du montant total des pca du decompte
        total = total.add(decompteVO.getPeriodesPca().getTotal());
        // soustraction des prestations versée
        soutTotalPCA = total.subtract(decompteVO.getPrestationsVerses().getTotal());
        // Definit le montant du retro
        decompteVO.setSousTotalPCA(soutTotalPCA);

        // addition des déductions
        soutTotalDeduction = decompteVO.getCreanciers().getTotal().add(decompteVO.getDettesCompta().getTotal());

        // calcule du montant disponible ou à restituer
        total = soutTotalPCA.subtract(soutTotalDeduction);

        // On ajoute le montant des allocations de noel
        total = total.add(decompteVO.getMontantTotalAllocationNoel());

        // ajout
        decompteVO.setTotal(total);
        decompteVO.setSousTotalDeduction(soutTotalDeduction);
        decompteVO.setSousTotalPCA(soutTotalPCA);
    }

    private List<SimpleAllocationNoel> findAllocationDeNoel(List<String> idsPca) throws JadePersistenceException,
            PCAccordeeException, AllocationDeNoelException, JadeApplicationServiceNotAvailableException {
        List<SimpleAllocationNoel> list = new ArrayList<SimpleAllocationNoel>();
        if (idsPca.size() > 0) {
            SimpleAllocationNoelSearch search = new SimpleAllocationNoelSearch();
            search.setInIdsPcAccordee(idsPca);
            search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            search = PegasusServiceLocator.getSimpleAllocationDeNoelService().search(search);
            list = PersistenceUtil.typeSearch(search);
        }
        return list;
    }

    private Map<String, SimpleAllocationNoel> findAllocationNoelAndCreateMapByIdPca(List<String> idPca)
            throws JadePersistenceException, PCAccordeeException, AllocationDeNoelException,
            JadeApplicationServiceNotAvailableException {
        List<SimpleAllocationNoel> list = findAllocationDeNoel(idPca);
        Map<String, SimpleAllocationNoel> map = new HashMap<String, SimpleAllocationNoel>();
        for (SimpleAllocationNoel simpleAllocationNoel : list) {
            // On ne veut pas afficher les allocations de noel qui ont été généreré par le processus des allocations de
            // noel.
            // On veut utiliser que les rétroactive, il ne doit pas avoir d'id presation
            if (JadeStringUtil.isBlankOrZero(simpleAllocationNoel.getIdPrestationAccordee())) {
                map.put(simpleAllocationNoel.getIdPCAccordee(), simpleAllocationNoel);
            }
        }
        return map;
    }

    private Map<String, SimpleAllocationNoel> findAllocationNoelIfIsUsed(List<String> idsPca)
            throws PCAccordeeException, AllocationDeNoelException, PropertiesException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        Map<String, SimpleAllocationNoel> map = new HashMap<String, SimpleAllocationNoel>();
        if (EPCProperties.ALLOCATION_NOEL.getBooleanValue()) {
            map = findAllocationNoelAndCreateMapByIdPca(idsPca);
        }
        return map;
    }

    private void findAndSetJourAppointlIfIsUsed(List<String> idsPca, List<PCAccordeeDecompteVO> pcas)
            throws PCAccordeeException, PropertiesException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        Map<String, SimpleJoursAppoint> map = new HashMap<String, SimpleJoursAppoint>();
        if (EPCProperties.GESTION_JOURS_APPOINTS.getBooleanValue()) {
            List<SimpleJoursAppoint> list = new ArrayList<SimpleJoursAppoint>();
            if (idsPca.size() > 0) {
                SimpleJoursAppointSearch search = new SimpleJoursAppointSearch();
                search.setInIdsPca(idsPca);
                search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                search = PegasusImplServiceLocator.getSimpleJoursAppointService().search(search);
                list = PersistenceUtil.typeSearch(search, search.whichModelClass());
                for (SimpleJoursAppoint joursAppoint : list) {
                    map.put(joursAppoint.getIdPCAccordee(), joursAppoint);
                }
            }

            if (map.size() > 0) {
                for (PCAccordeeDecompteVO pca : pcas) {
                    if (map.containsKey(pca.getIdPca())) {
                        pca.setSimpleJoursAppoint(map.get(pca.getIdPca()));
                    }
                }
            }
        }

    }

    private void findAndSetAllocationNoelIfIsUsed(List<String> idsPca, List<PCAccordeeDecompteVO> list)
            throws PCAccordeeException, AllocationDeNoelException, PropertiesException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        Map<String, SimpleAllocationNoel> mapAllocationNoel = findAllocationNoelIfIsUsed(idsPca);
        addAllocationNoelToPca(mapAllocationNoel, list);
    }

    /**
     * Point d'entrée pour le retour de l'objet VO
     * 
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public DecompteTotalPcVO generateVO() throws JadePersistenceException, JadeApplicationException {

        dateDecision = PegasusImplServiceLocator.getDecisionBusinessService().findDateDecision(idVersionDroit);

        // ValiderDecisionAcLoader loader = new ValiderDecisionAcLoader();
        // loader.load(this.idVersionDroit);

        // creation liste des periode pc
        createDecomptePca();
        if (!isEffetMoisSuivant) {
            // creation pca deja verses
            createPrestationVerses();
            // creation creanciers
            createCreanciers();
            // dette compta
            createDetteCompta();
            // montant total
            createTotal();
            // on retourne l'objet vo
            decompteVO.setMontantVerifRetro(montantVerifRetro);
        }

        return decompteVO;
    }

    /**
     * Retourne la description du creancier
     * 
     * @param idTiers
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     * @throws DecisionException
     */
    private String getCreancierLibelle(String idTiers) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException, DecisionException {
        if (JadeStringUtil.isBlank(idTiers)) {
            throw new DecisionException("Unable to retrieve Tiers for building libelle for OO, the id passed is null");
        }

        TiersSimpleModel tiersModel = TIBusinessServiceLocator.getTiersService().read(idTiers);
        // JadeStringUtil.isB
        // TiersSimpleModel tiers = tiersSearch.getSearchResults()[0];
        String description = tiersModel.getDesignation1() + " " + tiersModel.getDesignation2() + " "
                + tiersModel.getDesignation3() + " " + tiersModel.getDesignation4();

        return description;
    }

    public String getDescriptionTiers(PCAccordee pca) {
        String descTiers = pca.getPersonneEtendue().getTiers().getDesignation1() + " "
                + pca.getPersonneEtendue().getTiers().getDesignation2();

        if (((pca.getSimplePrestationsAccordeesConjoint() != null) && !JadeStringUtil.isBlankOrZero((pca
                .getSimplePrestationsAccordeesConjoint().getIdPrestationAccordee())))
                && (pca.getPersonneEtendueConjoint() != null)) {
            descTiers = descTiers + " " + JadeThread.getMessage("pegasus.et") + " "
                    + pca.getPersonneEtendueConjoint().getTiers().getDesignation1() + " "
                    + pca.getPersonneEtendueConjoint().getTiers().getDesignation2();
        }
        return descTiers;
    }

    public String getDescriptionTiers(SimplePrestationsAccordees prestationsAccordeesRequerant,
            SimplePrestationsAccordees prestationsAccordeesConjoint)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {

        PersonneEtendueComplexModel requerant = mapPersonne.get(prestationsAccordeesRequerant.getIdTiersBeneficiaire());
        if (requerant == null) {
            requerant = TIBusinessServiceLocator.getPersonneEtendueService().read(
                    prestationsAccordeesRequerant.getIdTiersBeneficiaire());
        }
        String descTiers = requerant.getTiers().getDesignation1() + " " + requerant.getTiers().getDesignation2();

        if ((prestationsAccordeesConjoint != null)
                && !JadeStringUtil.isBlankOrZero(prestationsAccordeesConjoint.getIdTiersBeneficiaire())) {
            PersonneEtendueComplexModel conjoint = mapPersonne.get(prestationsAccordeesConjoint
                    .getIdTiersBeneficiaire());
            if (conjoint == null) {
                conjoint = TIBusinessServiceLocator.getPersonneEtendueService().read(
                        prestationsAccordeesConjoint.getIdTiersBeneficiaire());
            }

            descTiers = descTiers + " " + JadeThread.getMessage("pegasus.et") + " "
                    + conjoint.getTiers().getDesignation1() + " " + conjoint.getTiers().getDesignation2();
        }

        return descTiers;
    }

    private String resolveDateFin(String dateFinPca, Boolean hasDateFinForce) {
        String dateFin = null;
        if (!JadeStringUtil.isBlankOrZero(dateFinPca) && !hasDateFinForce) {
            dateFin = dateFinPca;
        } else {
            dateFin = JadeDateUtil.convertDateMonthYear(dateDecision);
        }
        return dateFin;
    }

    /**
     * On va setter les paramètres manquant à l'appel du service pour le calcul montant rétro, avec la première pca
     * retourné
     * 
     * @param pca
     */
    private void setAdditionalParameters(PCAccordee pca) {
        idDemande = pca.getSimpleDroit().getIdDemandePC();
        noVersionDroit = pca.getSimpleVersionDroit().getNoVersion();
        idDroit = pca.getSimpleDroit().getId();
        isEffetMoisSuivant = !pca.getSimplePCAccordee().getIsCalculRetro();
    }
}
