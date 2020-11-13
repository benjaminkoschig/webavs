package ch.globaz.pegasus.businessimpl.services.models.mutation;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.constantes.EPCPMutationPassage;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.exceptions.models.MutationException;
import ch.globaz.pegasus.business.models.mutation.MutationAbstract;
import ch.globaz.pegasus.business.models.mutation.MutationPca;
import ch.globaz.pegasus.business.models.mutation.MutationPcaOld;
import ch.globaz.pegasus.business.models.mutation.MutationPcaOldSearch;
import ch.globaz.pegasus.business.models.mutation.MutationPcaSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;
import ch.globaz.pegasus.business.models.recap.RecapCategorie;
import ch.globaz.pegasus.business.services.models.mutation.MutationPcaService;
import ch.globaz.pegasus.business.vo.lot.MutationPcaVo;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.mutation.MutationCategorieResolver.RecapDomainePca;
import ch.globaz.pegasus.businessimpl.utils.PegasusDateUtil;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;

/**
 * @author dma
 */
/**
 * @author ebko
 *
 */
public class MutationPcaServiceImpl implements MutationPcaService {

    private void addJourAppointToMutation(List<MutationPca> listValidee, List<MutationAbstract> listOldCurent)
            throws MutationException {
        try {
            if (EPCProperties.GESTION_JOURS_APPOINTS.getBooleanValue()) {
                JoursAppointMerger joursAppointMerger;
                try {
                    joursAppointMerger = new JoursAppointMerger(
                            PegasusImplServiceLocator.getSimpleJoursAppointService());
                } catch (JadeApplicationServiceNotAvailableException e) {
                    throw new MutationException("simpleJoursAppointService is not available");
                }
                joursAppointMerger.mergeJourAppoint(listValidee);
                joursAppointMerger.mergeJourAppoint(listOldCurent);
            }
        } catch (PropertiesException e) {
            throw new MutationException("Unable to get the properties jours Appoints");
        }
    }

    /**
     * Permet d'ajouter des infos complémentaire sur la mutation et de la peupler. Ceci permet d'avoir la logique métier
     * dans cette fonction
     * 
     * @param dateMonth
     * @param mutationBd
     * @param mutationVO
     */
    private void consolideMutationVO(String dateMonth, MutationPca mutationBd, MutationPcaVo mutationVO) {

        // mutationVO.setIdVersionDroit(mutationBd.getIdVersionDroit());
        fillMutationVo(mutationBd, mutationVO);
        mutationVO.setMontantActuel(mutationBd.getMontant());
        mutationVO.setCsTypePreparationDecision(mutationBd.getCsTypePreparationDecision());
        mutationVO.setDateFinPcaActuel(mutationBd.getDateFinPca());
        if (mutationBd.getMontantJourAppoint() != null) {
            mutationVO.setMontantJourAppointActuel(mutationBd.getMontantJourAppoint().toString());
        }
        int nbMonth;

        // On regarde si on à une mutation future.
        if (JadeDateUtil.isDateMonthYearAfter(mutationVO.getDateDebutPcaActuel(), dateMonth)) {
            mutationVO.setAugementationFutur(true);
            mutationVO.setMontantRetro(null);
            mutationVO.setHasDiminutation(true);
        } else {
            // Si la décison n'est pas du courant cela signifie qu'il aura sûrement du retro.
            if (!IPCDecision.CS_PREP_COURANT.equals(mutationBd.getCsTypePreparationDecision())) {

                if (!IPCDecision.CS_TYPE_SUPPRESSION_SC.equals(mutationBd.getCsTypeDecision())) {

                    if (IPCDecision.CS_PREP_RETRO.equals(mutationBd.getCsTypePreparationDecision())
                            || (!JadeStringUtil.isEmpty(mutationBd.getDateFinPca()) && !mutationBd.getIsDateFinForce())) {
                        nbMonth = JadeDateUtil.getNbMonthsBetween("01." + mutationBd.getDateDebutPca(), "01."
                                + mutationBd.getDateFinPca()) + 1;
                    } else {
                        nbMonth = JadeDateUtil.getNbMonthsBetween("01." + mutationBd.getDateDebutPca(), "01."
                                + dateMonth);
                    }

                    mutationVO.setMontantRetro((new BigDecimal(mutationBd.getMontant())
                            .multiply(new BigDecimal(nbMonth))).toString());

                    // on test si on à une mutation purment rétro si c'est le cas on aura pas de diminution
                    if (JadeDateUtil.isDateMonthYearBefore(mutationVO.getDateFinPcaActuel(), dateMonth)
                            && !mutationBd.getIsDateFinForce()) {
                        mutationVO.setPurRetro(true);
                    } else {
                        // On ne peut pas avoir du retro avec une décision courante
                        if (!mutationBd.getNoVersion().equals("1")) {
                            mutationVO.setHasDiminutation(true);
                        } else {
                            mutationVO.setHasDiminutation(false);
                        }
                    }
                } else {
                    mutationVO.setHasDiminutation(true);
                }
            }
        }

        if (JadeStringUtil.isEmpty(mutationVO.getDateFinPcaActuel()) || mutationBd.getIsDateFinForce()) {
            mutationVO.setCurrent(true);
        }

        // Si on a une décision de suppression on ce base uniquement sur la nouvelle pca pour le type
        if (IPCDecision.CS_TYPE_SUPPRESSION_SC.equals(mutationBd.getCsTypeDecision())) {
            if (mutationVO.getCodeCategroriePcaPrecedante() != null) {
                mutationVO.setCodeCategroriePca(mutationVO.getCodeCategroriePcaPrecedante());
            }
        }

        detectPassage(mutationVO);

    }

    private void detectPassage(MutationPcaVo mutationVO) {
        // On peut seulement detecter le passage d'avs/ai si on travail avec des pca courante et que l'on a plus de une
        // version de droit. De plus on ne peut pas avoir de passage lors d'une décision de supression.

        if (!IPCDecision.CS_TYPE_SUPPRESSION_SC.equals(mutationVO.getCsTypeDecision())
                && (mutationVO.getMontantPrecedant() != null) && !mutationVO.getNoVersion().equals("1")
                && !mutationVO.getCodeCategroriePca().equals(mutationVO.getCodeCategroriePcaPrecedante())) {

            if (IPCPCAccordee.CS_TYPE_PC_INVALIDITE.equals(mutationVO.getCodeCategroriePca())) {
                mutationVO.setPassage(EPCPMutationPassage.AVS_AI);
            } else {
                mutationVO.setPassage(EPCPMutationPassage.AI_AVS);
            }
        } else {
            mutationVO.setPassage(EPCPMutationPassage.AUCUN);
        }
    }

    private List<MutationAbstract> determineAncienneVersionDroit(Map<String, List<MutationAbstract>> mapOldCurent,
            String key) {
        List<MutationAbstract> listVersionOldPca = null;
        Map<String, List<MutationAbstract>> grouperParVersionOldPca = null;
        List<MutationAbstract> listOldPca = null;

        if (mapOldCurent.containsKey(key)) {
            listOldPca = mapOldCurent.get(key);
            if (listOldPca.size() > 0) {
                grouperParVersionOldPca = groupByVersionDroitForOldPca(listOldPca);
            }
        }

        if (grouperParVersionOldPca != null) {
            listVersionOldPca = grouperParVersionOldPca.remove(findKeyMax(grouperParVersionOldPca));
        }
        return listVersionOldPca;
    }

    private void fillMutationVo(MutationAbstract mutationBd, MutationPcaVo mutationVO) {
        mutationVO.setNom(mutationBd.getNom());
        mutationVO.setNss(mutationBd.getNss());
        mutationVO.setPrenom(mutationBd.getPrenom());
        mutationVO.setCodeCategroriePca(MutationCategorieResolver.getCodeCategorie(mutationBd.getCsTypePca()));
        mutationVO.setCsTypeDecision(mutationBd.getCsTypeDecision());
        mutationVO.setNoVersion(mutationBd.getNoVersion());
        mutationVO.setIdPca(mutationBd.getIdPcaActuel());
        mutationVO.setDateDebutPcaActuel(mutationBd.getDateDebutPca());
    }

    /**
     * On filtre les cas ou on a plus d'une pca. On fait ceci du à la reprise de donnée normalement on ne devrait pas
     * filtrer
     * 
     * @param listVersionOldPca
     */
    private List<MutationAbstract> filtreListeVersionOldPc(final List<MutationAbstract> listVersionOldPca) {
        List<String> idsPca = new ArrayList<String>();
        List<MutationAbstract> listFiltree = new ArrayList<MutationAbstract>();
        for (MutationAbstract m : listVersionOldPca) {
            if (!idsPca.contains(m.getIdPcaActuel())) {
                listFiltree.add(m);
                idsPca.add(m.getIdPcaActuel());
            }
        }
        return listFiltree;

    }

    private AlloctionNoelGrouped findAllocationNoelIfUsed(List<String> idsPca) throws JadePersistenceException,
            MutationException {
        AlloctionNoelGrouped grouped = new AlloctionNoelGrouped(new HashMap<String, SimpleAllocationNoel>());
        try {
            if (EPCProperties.ALLOCATION_NOEL.getBooleanValue()) {
                try {
                    AllocationNoelForMutation allocationNoelForMutation = new AllocationNoelForMutation(
                            PegasusImplServiceLocator.getSimpleAllocationDeNoelService());
                    grouped = allocationNoelForMutation.findAllocationRetro(idsPca);
                } catch (JadeApplicationServiceNotAvailableException e) {
                    throw new MutationException("AllocationDeNoel service is not available");
                }
            }
            return grouped;
        } catch (PropertiesException e) {
            throw new MutationException("Unable to get the properties allocation noel");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.pegasus.business.services.models.mutation.MutationPcaService#findAndgroupeByCategorie(java.lang.String)
     */
    @Override
    public Map<RecapCategorie, Map<RecapDomainePca, List<MutationPcaVo>>> findAndgroupeByCategorie(String dateMonth)
            throws MutationException, JadePersistenceException {
        if (dateMonth == null) {
            throw new MutationException("Unable to findAndgroupeByCategorie dateMonth, the dateMonth passed is null!");
        }
        if (JadeStringUtil.isBlankOrZero(dateMonth)) {
            throw new MutationException("Unable to findAndgroupeByCategorie dateMonth, the dateMonth passed is blank!");
        }
        List<MutationPcaVo> list = findMutation(dateMonth);
        return groupeByCategorie(list);
    }

    private String findKeyMax(Map<String, List<MutationAbstract>> grouperParVersion) {
        Integer max = 0;
        Integer temp = 0;
        for (Entry<String, List<MutationAbstract>> e : grouperParVersion.entrySet()) {
            temp = Integer.parseInt(e.getKey());
            if (temp > max) {
                max = temp;
            }
        }
        return String.valueOf(max);
    }

    /**
     * Permet de rechercher les mutations qui on eu lieu lors d'une date donnée
     * 
     * @param dateMonth
     * @return Une liste contenant les mutations
     * @throws JadePersistenceException
     * @throws MutationException
     */
    public List<MutationPcaVo> findMutation(String dateMonth) throws JadePersistenceException, MutationException {
        if (dateMonth == null) {
            throw new MutationException("Unable to excute findMutation , the dateMonth passed is null!");
        }
        if (JadeStringUtil.isBlankOrZero(dateMonth)) {
            throw new MutationException("Unable to excute findMutation , the dateMonth passed is empty!");
        }
        List<MutationPca> listValidee = findNewPca(dateMonth);

        List<String> idsDriot = new ArrayList<String>();
        for (MutationPca mutation : listValidee) {
            idsDriot.add(mutation.getIdDroit());
        }

        List<String> idsPca = new ArrayList<String>();
        for (MutationPca mutation : listValidee) {
            // on filtre les type adaptation car il ne peut pas avoir de jour d'appoint ou d'allocation de noell sur une
            // adaptation annuel
            if (!IPCDecision.CS_TYPE_ADAPTATION_AC.equals(mutation.getCsTypeDecision())) {
                idsPca.add(mutation.getIdPcaActuel());
            }
        }

        List<MutationAbstract> listOldCurent = findOldCurentPca(dateMonth, idsDriot);

        AlloctionNoelGrouped allocationsNoel = findAllocationNoelIfUsed(idsPca);
        addJourAppointToMutation(listValidee, listOldCurent);
        return mergeListAndConsolideData(listValidee, listOldCurent, allocationsNoel, dateMonth);
    }

    /**
     * Permet de retrouver les pca qui on été validé à la date donnée en paramétre
     * 
     * @param date
     * @return
     * @throws JadePersistenceException
     */
    private List<MutationPca> findNewPca(String date) throws JadePersistenceException {
        MutationPcaSearch search = new MutationPcaSearch();
        int nbJour = PegasusDateUtil.getLastDayOfMonth(date);
        search.setForDateDecisionMin("01." + date);
        search.setForIsPlanRetenu(true);
        search.setForDateDecisionMax(Integer.valueOf(nbJour) + "." + date);
        search.setWhereKey(MutationPcaSearch.FOR_PCA_VALIDEE);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        List<MutationPca> list = PersistenceUtil.search(search, search.whichModelClass());
        list = filtrePcaDemandeAnnulee(list);

        return list;
    }
    
    /**
     * Filtre les pca : ne prend pas en compte les pca dues à l'annulation ou la réduction d'une demande
     * 
     * @param listPca
     * @return
     */
    private List<MutationPca> filtrePcaDemandeAnnulee(List<MutationPca> listPca) {
        Map<String, List<MutationPca>> mapPcaAnnule = new HashMap<>();
        List<MutationPca> listPcaFinale = new ArrayList<>();
        for(MutationPca pca : listPca) {
            if(isDemandeAnnule(pca)) {
                // groupe par id demande + numéro nss
                String key = pca.getIdDemande()+pca.getNss();
                if(mapPcaAnnule.get(key) == null) {
                    mapPcaAnnule.put(key, new ArrayList<MutationPca>());
                }
                mapPcaAnnule.get(key).add(pca);
            } else {
                listPcaFinale.add(pca);
            }
        }
        
        // Supprime la dernière pca d'une demande qui a été annulée ou réduite
        for(List<MutationPca> listPcaAnnule: mapPcaAnnule.values()) {
            Collections.sort(listPcaAnnule, new SortByNoVersion());
            listPcaAnnule.remove(0);
            listPcaFinale.addAll(listPcaAnnule);
        }
        return listPcaFinale;
    }
    
    class SortByNoVersion implements Comparator<MutationPca> {
        public int compare(MutationPca a, MutationPca b) {
            return Integer.valueOf(b.getNoVersion()) - Integer.valueOf(a.getNoVersion());
        }
    }
    
    private boolean isDemandeAnnule(MutationPca pca) {
        return IPCDemandes.CS_ANNULE.equals(pca.getCsEtatDemande()) || !JadeStringUtil.isBlankOrZero(pca.getDateFinInitial());
    }

    /**
     * Permet de retrouver les anciennes pca courantes qui on été historisé par de nouvelle pca a une date donnée On se
     * base sur les décision pour déterminer la date
     * 
     * @param date
     * @return
     * @throws JadePersistenceException
     */
    private List<MutationAbstract> findOldCurentPca(String date, List<String> idsDroit) throws JadePersistenceException {

        int nbJour = PegasusDateUtil.getLastDayOfMonth(date);
        MutationPcaOldSearch search = new MutationPcaOldSearch();
        search.setForIsPlanRetenu(true);
        search.setWhereKey(MutationPcaSearch.FOR_OLD_CURENT_PCA);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search.setForDateDecisionMin("01." + date);
        search.setForIsPlanRetenu(true);
        search.setForDateDecisionMax(Integer.valueOf(nbJour) + "." + date);
        search.setInIdDroit(idsDroit);
        List<MutationPcaOld> list = PersistenceUtil.search(search, search.whichModelClass());
        List<MutationAbstract> l = new ArrayList<MutationAbstract>();
        for (MutationPcaOld pca : list) {
            l.add(pca);
        }
        return l;
    }

    private Map<String, List<MutationPca>> groupByIdDroit(List<MutationPca> listeValidee) {
        return JadeListUtil.groupBy(listeValidee, new JadeListUtil.Key<MutationPca>() {
            @Override
            public String exec(MutationPca e) {
                return e.getIdDroit();
            }
        });
    }

    private Map<String, List<MutationAbstract>> groupByIdDroitForOldPca(List<MutationAbstract> listeValidee) {
        return JadeListUtil.groupBy(listeValidee, new JadeListUtil.Key<MutationAbstract>() {
            @Override
            public String exec(MutationAbstract e) {
                return e.getIdDroit();
            }
        });
    }

    private Map<String, List<MutationPca>> groupByIdVersionDroit(List<MutationPca> liste) {
        return JadeListUtil.groupBy(liste, new JadeListUtil.Key<MutationPca>() {
            @Override
            public String exec(MutationPca e) {
                return e.getIdVersionDroit();
            }
        });
    }

    private Map<String, List<MutationPca>> groupByNss(List<MutationPca> list) {
        return JadeListUtil.groupBy(list, new JadeListUtil.Key<MutationPca>() {
            @Override
            public String exec(MutationPca e) {
                return e.getNss();
            }
        });
    }

    private Map<String, List<MutationAbstract>> groupByVersionDroitForOldPca(List<MutationAbstract> liste) {
        return JadeListUtil.groupBy(liste, new JadeListUtil.Key<MutationAbstract>() {
            @Override
            public String exec(MutationAbstract e) {
                return e.getNoVersion();
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.mutation.MutationPcaService#groupeByCategorie(java.util.List)
     */
    @Override
    public Map<RecapCategorie, Map<RecapDomainePca, List<MutationPcaVo>>> groupeByCategorie(List<MutationPcaVo> list) {

        Map<RecapDomainePca, List<MutationPcaVo>> map = new LinkedHashMap<RecapDomainePca, List<MutationPcaVo>>();
        Map<RecapDomainePca, List<MutationPcaVo>> mapFutur = new LinkedHashMap<RecapDomainePca, List<MutationPcaVo>>();
        Map<RecapDomainePca, List<MutationPcaVo>> mapAdaptation = new LinkedHashMap<RecapDomainePca, List<MutationPcaVo>>();

        for (MutationPcaVo mutation : list) {
            // On ne fait pas de distinction dans le cas de suppression
            // et on ne peut pas avoir de passage de l'AVS à l'AI dans le cas de suppression.
            // if (IPCDecision.CS_TYPE_SUPPRESSION_SC.equals(mutation.getTypeDecision())) {
            // mutation.setTypePcActuel(mutation.getTypePcPrecedant());
            // }
            RecapDomainePca key = mutation.getCodeCategroriePca();

            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<MutationPcaVo>());
            }

            // On traite les augmentations future. Il ne faut pas que les adaption soit dans le future car une catégorie
            // existe pour cela.
            if (mutation.isAugementationFutur()
                    && !IPCDecision.CS_TYPE_ADAPTATION_AC.equals(mutation.getCsTypeDecision())) {
                if (!mapFutur.containsKey(key)) {
                    mapFutur.put(key, new ArrayList<MutationPcaVo>());
                }
                mapFutur.get(key).add(mutation);
                // On traite les pca de type adaptation
            } else if (IPCDecision.CS_TYPE_ADAPTATION_AC.equals(mutation.getCsTypeDecision())) {
                if (!mapAdaptation.containsKey(key)) {
                    mapAdaptation.put(key, new ArrayList<MutationPcaVo>());
                }
                mutation.setHasDiminutation(true);
                mapAdaptation.get(key).add(mutation);
            }

            // On test si on a eu un passage. Par exemple avs à l'ai. On peut avoir un passage que si on a du courant
            // Si on a un passage il faut aller mettre une diminution dans l'ancien type de pca.
            if (!EPCPMutationPassage.AUCUN.equals(mutation.getPassage()) && mutation.isCurrent()) {
                if (!map.containsKey(mutation.getCodeCategroriePcaPrecedante())) {
                    map.put(mutation.getCodeCategroriePcaPrecedante(), new ArrayList<MutationPcaVo>());
                }
                MutationPcaVo m = mutation.clone();
                mutation.setHasDiminutation(false);
                map.get(mutation.getCodeCategroriePcaPrecedante()).add(mutation.clone());
            }

            // On ajoute dans tout les cas car il faut afficher la diminution du future sauf si il y un
            // transfert
            // On filtre les première versions des pca car il ne peut pas avoir de diminution
            if (!(mutation.isAugementationFutur() && !EPCPMutationPassage.AUCUN.equals(mutation.getPassage()))
                    && !(mutation.isAugementationFutur() && mutation.getNoVersion().equals("1"))) {
                if (!IPCDecision.CS_TYPE_ADAPTATION_AC.equals(mutation.getCsTypeDecision())) {
                    // mutation = mutation.clone();
                    // mutation.setHasDiminutation(true);
                    // mutation.setPurDiminution(true);
                    map.get(key).add(mutation);
                }

            }
        }

        Map<RecapCategorie, Map<RecapDomainePca, List<MutationPcaVo>>> retour = new HashMap<RecapCategorie, Map<RecapDomainePca, List<MutationPcaVo>>>();

        retour.put(RecapCategorie.NORMAL, map);
        retour.put(RecapCategorie.FUTUR, mapFutur);
        retour.put(RecapCategorie.ADAPTATION, mapAdaptation);
        return retour;

    }

    private boolean isCoupleSepareParLaMaladie(List<MutationPca> listVersionNewPca) {
        boolean isCoupleSepareParLaMaladie = false;
        if (groupByNss(listVersionNewPca).size() == 2) {
            isCoupleSepareParLaMaladie = true;
        }
        return isCoupleSepareParLaMaladie;
    }

    private void merge(List<MutationAbstract> listVersionOldPca, Entry<String, List<MutationPca>> entry,
            MutationPca mutation, MutationPcaVo m) {
        if ((JadeStringUtil.isBlankOrZero(mutation.getDateFinPca()) || mutation.getIsDateFinForce() || IPCDecision.CS_TYPE_SUPPRESSION_SC
                .equals(mutation.getCsTypeDecision())) && !mutation.getNoVersion().equals("1")) {

            if (listVersionOldPca != null) {
                if (listVersionOldPca.size() > 0) {
                    MutationAbstract old = null;
                    MutationAbstract temp = null;
                    Iterator<MutationAbstract> it = listVersionOldPca.iterator();
                    while ((old == null) && it.hasNext()) {
                        temp = it.next();
                        if (temp.getCsRoleMembreFamille().equals(mutation.getCsRoleMembreFamille())) {
                            old = temp;
                        }
                    }
                    if (old != null) {
                        m.setMontantPrecedant(old.getMontant());
                        m.setCodeCategroriePcaPrecedante(MutationCategorieResolver.getCodeCategorie(old.getCsTypePca()));
                        m.setDateDebutPcaPrecedant(old.getDateDebutPca());
                        if (old.getMontantJourAppoint() != null) {
                            m.setMontantJourAppointPrecedant(old.getMontantJourAppoint().toString());
                        }
                    }
                }
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.pegasus.business.services.models.mutation.MutationPcaService#mergeListAndConsolideData(java.util.List,
     * java.util.List, java.lang.String)
     */
    @Override
    public List<MutationPcaVo> mergeListAndConsolideData(List<MutationPca> listeValidee,
            List<MutationAbstract> listOldCurent, AlloctionNoelGrouped allocation, String dateMonth) {

        sortListByNoDroitAndNoVersionDroit(listeValidee);
        sortListByNoDroitAndNoVersionDroit(listOldCurent);

        Map<String, List<MutationPca>> mapValidee = groupByIdDroit(listeValidee);
        Map<String, List<MutationAbstract>> mapOldCurent = groupByIdDroitForOldPca(listOldCurent);

        List<MutationPcaVo> list = new ArrayList<MutationPcaVo>();

        for (Entry<String, List<MutationPca>> entry : mapValidee.entrySet()) {
            List<MutationPca> listNewPca = entry.getValue();
            // On regroupe par version de droit pour détecter si on a des couple séparé par la maladie
            Map<String, List<MutationAbstract>> grouperParVersionOldPca = null;
            List<MutationAbstract> listOldPca = null;
            Map<String, List<MutationPca>> grouperParVersion = groupByIdVersionDroit(listNewPca);

            if (mapOldCurent.containsKey(entry.getKey())) {
                listOldPca = mapOldCurent.get(entry.getKey());
                if (listOldPca.size() > 0) {
                    grouperParVersionOldPca = groupByVersionDroitForOldPca(listOldPca);
                }
            }
            for (Entry<String, List<MutationPca>> e : grouperParVersion.entrySet()) {
                List<MutationPca> listVersionNewPca = e.getValue();
                List<MutationAbstract> listVersionOldPca = null;
                List<MutationPca> listVersionNewPcaCurrent = new ArrayList<MutationPca>();
                groupByNss(listVersionNewPca);

                boolean isCoupleSepareParLaMaladie = isCoupleSepareParLaMaladie(listVersionNewPca);

                for (MutationPca mutation : listVersionNewPca) {

                    if ((grouperParVersionOldPca != null) && (listVersionOldPca == null)) {
                        // On fait une synchronisation quand la date de fin est null ou si il s'agit d'une decision de
                        // suppression
                        if ((JadeStringUtil.isBlankOrZero(mutation.getDateFinPca()) || mutation.getIsDateFinForce() || IPCDecision.CS_TYPE_SUPPRESSION_SC
                                .equals(mutation.getCsTypeDecision())) && !mutation.getNoVersion().equals("1")) {
                            listVersionOldPca = grouperParVersionOldPca.remove(findKeyMax(grouperParVersionOldPca));
                        }
                    }

                    MutationPcaVo mVo = new MutationPcaVo();

                    merge(listVersionOldPca, entry, mutation, mVo);

                    consolideMutationVO(dateMonth, mutation, mVo);

                    if (mVo.isCurrent()) {
                        mVo.setMontantAllocationNoel(allocation.getMontantRetoForIdDemande(mutation.getIdDemande(),
                                isCoupleSepareParLaMaladie));
                        listVersionNewPcaCurrent.add(mutation);
                    }
                    list.add(mVo);
                }

                if (listVersionOldPca != null) {
                    listVersionOldPca = filtreListeVersionOldPc(listVersionOldPca);

                    // On test si on a un passage d'un couple séparé à un DOM2R
                    // Dans se cas il faut faire une diminution en plus pour le conjoint
                    if ((listVersionOldPca.size() == 2) && (listVersionNewPcaCurrent.size() == 1)) {
                        MutationPcaVo mVo = new MutationPcaVo();
                        // On cherche la pca du conjoint
                        MutationAbstract mutationConjoint = null;
                        for (MutationAbstract mutation : listVersionOldPca) {
                            if (IPCDroits.CS_ROLE_FAMILLE_CONJOINT.equals(mutation.getCsRoleMembreFamille())) {
                                mutationConjoint = mutation;
                            }
                        }
                        if (mutationConjoint != null) {

                            fillMutationVo(mutationConjoint, mVo);
                            mVo.setCsTypePreparationDecision(listVersionNewPcaCurrent.get(0)
                                    .getCsTypePreparationDecision());
                            mVo.setCurrent(true);
                            mVo.setHasDiminutation(true);
                            mVo.setMontantActuel("0");
                            mVo.setPurDiminution(true);
                            mVo.setMontantPrecedant(mutationConjoint.getMontant());
                            if (mutationConjoint.getMontantJourAppoint() != null) {
                                mVo.setMontantJourAppointPrecedant(mutationConjoint.getMontantJourAppoint().toString());
                            }// this.detectPassage(mVo);
                            mVo.setPassage(EPCPMutationPassage.AUCUN);
                            list.add(mVo);
                        }
                    }
                }

            }
        }

        return list;
    }

    @Override
    public void sortByDate(List<MutationPcaVo> list) {
        Comparator<MutationPcaVo> comparator = new Comparator<MutationPcaVo>() {
            @Override
            public int compare(MutationPcaVo o1, MutationPcaVo o2) {

                if (Integer.parseInt(JadePersistenceUtil.parseMonthYearToSql(o1.getDateDebutPcaActuel())) > Integer
                        .parseInt(JadePersistenceUtil.parseMonthYearToSql(o2.getDateDebutPcaActuel()))) {
                    return -1;
                } else if (Integer.parseInt(JadePersistenceUtil.parseMonthYearToSql(o1.getDateDebutPcaActuel())) < Integer
                        .parseInt(JadePersistenceUtil.parseMonthYearToSql(o2.getDateDebutPcaActuel()))) {
                    return 1;
                }
                return 0;

            }
        };
        Collections.sort(list, comparator);

    }

    private void sortListByNoDroitAndNoVersionDroit(List<? extends MutationAbstract> listeValidee) {
        Comparator<MutationAbstract> comparator = new Comparator<MutationAbstract>() {
            @Override
            public int compare(MutationAbstract o1, MutationAbstract o2) {

                if (Integer.parseInt(o1.getIdDroit()) > Integer.parseInt(o2.getIdDroit())) {
                    return -1;
                } else if (Integer.parseInt(o1.getIdDroit()) < Integer.parseInt(o2.getIdDroit())) {
                    return 1;
                } else {
                    if (Integer.parseInt(o1.getNoVersion()) > Integer.parseInt(o2.getNoVersion())) {
                        return -1;
                    } else if (Integer.parseInt(o1.getNoVersion()) < Integer.parseInt(o2.getNoVersion())) {
                        return 1;
                    } else {
                        return 0;
                    }
                }

            }
        };
        Collections.sort(listeValidee, comparator);
    }

}
