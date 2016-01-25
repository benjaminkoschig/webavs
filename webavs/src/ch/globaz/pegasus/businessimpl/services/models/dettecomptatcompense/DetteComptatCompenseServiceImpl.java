package ch.globaz.pegasus.businessimpl.services.models.dettecomptatcompense;

import globaz.globall.db.BEntity;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeListUtil.Key;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.osiris.db.comptes.CASectionJoinCompteAnnexeJoinTiers;
import globaz.osiris.db.comptes.CASectionJoinCompteAnnexeJoinTiersManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.pegasus.business.domaine.ListTotal;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.dettecomptatcompense.DetteComptatCompenseException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.dettecomptatcompense.DetteCompenseOrdreVersement;
import ch.globaz.pegasus.business.models.dettecomptatcompense.DetteCompenseOrdreVersementSearch;
import ch.globaz.pegasus.business.models.dettecomptatcompense.SimpleDetteComptatCompense;
import ch.globaz.pegasus.business.models.dettecomptatcompense.SimpleDetteComptatCompenseSearch;
import ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.MembreFamilleEtenduSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.dettecomptatcompense.DetteComptatCompenseService;
import ch.globaz.pegasus.business.vo.decompte.DetteEnComptaVO;
import ch.globaz.pegasus.businessimpl.checkers.dettecomptatcompense.DetteComptatCompenseChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;

public class DetteComptatCompenseServiceImpl implements DetteComptatCompenseService {

    private void convert(SimpleDetteComptatCompense detteComptatCompense) {
        detteComptatCompense.setMontant(JANumberFormatter.deQuote(detteComptatCompense.getMontant()));
        if (JadeStringUtil.isEmpty(detteComptatCompense.getMontantModifie())) {
            detteComptatCompense.setMontantModifie("0");
        } else {
            detteComptatCompense.setMontantModifie(JANumberFormatter.deQuote(detteComptatCompense.getMontantModifie()));
        }
    }

    @Override
    public int count(SimpleDetteComptatCompenseSearch search) throws JadeApplicationException, JadePersistenceException {
        return PegasusImplServiceLocator.getSimpleDetteComptatCompenseService().count(search);
    }

    @Override
    public SimpleDetteComptatCompense create(SimpleDetteComptatCompense entity) throws JadeApplicationException,
            JadePersistenceException {
        convert(entity);
        // CECI est un hack il faudra plus analysé.
        // On fait ceci car lors que l'on comptabilise il n'exite plus de dette il faut donc mémorisé le montant de la
        // dette.
        if (JadeStringUtil.isBlankOrZero(entity.getMontantModifie())) {
            entity.setMontantModifie(entity.getMontant());
        }
        DetteComptatCompenseChecker.checkForCreate(entity);
        return PegasusImplServiceLocator.getSimpleDetteComptatCompenseService().create(entity);
    }

    @Override
    public SimpleDetteComptatCompense delete(SimpleDetteComptatCompense entity) throws JadeApplicationException,
            JadePersistenceException {
        return PegasusImplServiceLocator.getSimpleDetteComptatCompenseService().delete(entity);
    }

    public List<SimpleDetteComptatCompense> findDette(String idDroit, String idVersionDroit)
            throws JadeApplicationException, JadePersistenceException {
        return new ArrayList<SimpleDetteComptatCompense>(findDetteComptat(idDroit, idVersionDroit, false).values());
    }

    private Map<String, SimpleDetteComptatCompense> findDetteComptat(String idDroit, String idVersionDroit,
            boolean isCompense) throws JadeApplicationException, JadePersistenceException {
        List<CASectionJoinCompteAnnexeJoinTiers> listDetteCompta = null;
        if (idDroit == null) {
            throw new DetteComptatCompenseException("Unable to findDetteComptat  the idDroit passed is null!");
        }

        if (idVersionDroit == null) {
            throw new DetteComptatCompenseException("Unable to findDetteComptat the idVersionDroit passed is null!");
        }

        listDetteCompta = findListDetteEnCompta(idDroit);

        SimpleDetteComptatCompenseSearch search = new SimpleDetteComptatCompenseSearch();
        search.setForIdVersionDroit(idVersionDroit);
        search = PegasusImplServiceLocator.getSimpleDetteComptatCompenseService().search(search);

        Map<String, SimpleDetteComptatCompense> map = new HashMap<String, SimpleDetteComptatCompense>();
        for (JadeAbstractModel model : search.getSearchResults()) {
            SimpleDetteComptatCompense comptatCompense = (SimpleDetteComptatCompense) model;
            map.put(comptatCompense.getIdSectionDetteEnCompta(), comptatCompense);
        }

        for (CASectionJoinCompteAnnexeJoinTiers dette : listDetteCompta) {
            if (!map.containsKey(dette.getIdSection())) {
                if (!isCompense) {
                    SimpleDetteComptatCompense compense = new SimpleDetteComptatCompense();
                    compense.setIdSectionDetteEnCompta(dette.getIdSection());
                    compense.setIdVersionDroit(idVersionDroit);
                    compense.setMontant(dette.getSolde());
                    map.put(compense.getIdSectionDetteEnCompta(), compense);
                }
            } else {
                map.get(dette.getIdSection()).setMontant(dette.getSolde());
            }

        }
        return map;
    }

    @Override
    public Map<String, ListTotal<DetteCompenseOrdreVersement>> findDetteOvMontantCompense(String idVersionDroit)
            throws DetteComptatCompenseException, JadeApplicationServiceNotAvailableException, JadePersistenceException {

        DetteCompenseOrdreVersementSearch search = new DetteCompenseOrdreVersementSearch();
        search.setForIdVersionDroit(idVersionDroit);
        search.setForIsCompense(true);
        search = this.search(search);
        List<DetteCompenseOrdreVersement> listDette = PersistenceUtil.typeSearch(search, search.whichModelClass());

        Map<String, ListTotal<DetteCompenseOrdreVersement>> mapGroupe = new HashMap<String, ListTotal<DetteCompenseOrdreVersement>>();

        Map<String, List<DetteCompenseOrdreVersement>> mapDette = JadeListUtil.groupBy(listDette,
                new Key<DetteCompenseOrdreVersement>() {
                    @Override
                    public String exec(DetteCompenseOrdreVersement e) {
                        return e.getSimpleDetteComptatCompense().getIdSectionDetteEnCompta();
                    }
                });

        for (Entry<String, List<DetteCompenseOrdreVersement>> entry : mapDette.entrySet()) {
            ListTotal<DetteCompenseOrdreVersement> list = new ListTotal<DetteCompenseOrdreVersement>();
            // BigDecimal montantDette = new BigDecimal(mapDetteCa.get(entry.getKey()).getMontant());

            list.setList(entry.getValue());
            list.setTotal(sumOvDetteCompens(entry.getValue()));
            mapGroupe.put(entry.getKey(), list);
        }

        return mapGroupe;
    }

    private List<CASectionJoinCompteAnnexeJoinTiers> findListDetteEnCompta(String idDroit)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, DecisionException {
        // Trouver les idTiers de la famille
        List<String> listIdTiers = new ArrayList<String>();
        List<CASectionJoinCompteAnnexeJoinTiers> list = new ArrayList<CASectionJoinCompteAnnexeJoinTiers>();

        MembreFamilleEtenduSearch membreSearch = new MembreFamilleEtenduSearch();
        membreSearch.setForIdDroit(idDroit);
        try {
            membreSearch = PegasusServiceLocator.getDroitService().searchMembreFamilleEtendu(membreSearch);
        } catch (DroitException e1) {
            throw new DecisionException("Unable to search the membreFamille ", e1);
        }

        for (JadeAbstractModel model : membreSearch.getSearchResults()) {
            MembreFamilleEtendu mf = (MembreFamilleEtendu) model;
            listIdTiers.add(mf.getDroitMembreFamille().getMembreFamille().getPersonneEtendue().getPersonneEtendue()
                    .getIdTiers());
        }

        try {

            // TODO: Créer un service d'intefacage
            CASectionJoinCompteAnnexeJoinTiersManager mgr = new CASectionJoinCompteAnnexeJoinTiersManager();
            mgr.setForIdTiersIn(listIdTiers);
            mgr.setForSoldePositif(true);
            mgr = (CASectionJoinCompteAnnexeJoinTiersManager) PersistenceUtil.executeOldFind(mgr);

            for (Iterator<BEntity> it = mgr.iterator(); it.hasNext();) {
                CASectionJoinCompteAnnexeJoinTiers e = (CASectionJoinCompteAnnexeJoinTiers) it.next();
                list.add(e);
            }
            ;
        } catch (Exception e1) {
            throw new DecisionException("Unable to search the dette en compta ", e1);
        }

        return list;
    }

    @Override
    public ListTotal<SimpleDetteComptatCompense> findListTotalCompense(String idVersionDroit, String idDroit)
            throws JadeApplicationServiceNotAvailableException, JadeApplicationException, JadePersistenceException {
        // Droit droit = this.findCurrentDroit();
        // String idVersionDroit = droit.getSimpleVersionDroit().getIdVersionDroit();
        // String idDroit = droit.getSimpleDroit().getIdDroit();
        ListTotal<SimpleDetteComptatCompense> listTotal = new ListTotal<SimpleDetteComptatCompense>();

        String montant = "";
        BigDecimal total = new BigDecimal(0);
        SimpleDetteComptatCompenseSearch search = new SimpleDetteComptatCompenseSearch();
        search.setForIdDroit(idDroit);
        search.setForIdVersionDroit(idVersionDroit);
        search.setForIsCompens(true);
        search = PegasusServiceLocator.getDetteComptatCompenseService().search(search);

        List<SimpleDetteComptatCompense> list = PersistenceUtil.typeSearch(search, search.whichModelClass());

        for (SimpleDetteComptatCompense dette : list) {
            montant = dette.getMontant();
            if (!JadeStringUtil.isBlankOrZero(dette.getMontantModifie())) {
                montant = dette.getMontantModifie();
            }
            total = total.add(new BigDecimal(montant));
        }
        listTotal.setTotal(total);
        listTotal.setList(list);
        return listTotal;
    }

    @Override
    public ListTotal<DetteEnComptaVO> findListTotalCompenseAsDetteEnComptaVO(String idVersioNDroit, String idDroit)
            throws JadeApplicationServiceNotAvailableException, JadeApplicationException, JadePersistenceException {

        // Récupération de la liste des dette en compta via le service de base
        ListTotal<SimpleDetteComptatCompense> listDetteEnCompta = findListTotalCompense(idVersioNDroit, idDroit);

        // Lste total de retour
        ListTotal<DetteEnComptaVO> listeVO = new ListTotal<DetteEnComptaVO>();

        // Iteration et creation du value Object
        for (SimpleDetteComptatCompense dette : listDetteEnCompta.getList()) {
            DetteEnComptaVO detteVO = new DetteEnComptaVO();
            detteVO.setDette(dette);
            detteVO.setDescription(CABusinessServiceLocator.getSectionService().findDescription(
                    dette.getIdSectionDetteEnCompta()));
            listeVO.getList().add(detteVO);
        }

        listeVO.setTotal(listDetteEnCompta.getTotal());

        return listeVO;
    }

    @Override
    public SimpleDetteComptatCompense read(String idEntity) throws JadeApplicationException, JadePersistenceException {
        return PegasusImplServiceLocator.getSimpleDetteComptatCompenseService().read(idEntity);
    }

    @Override
    public SimpleDetteComptatCompense read(String idSection, String idVersionDroit, String idDroit)
            throws JadeApplicationException, JadePersistenceException {
        Map<String, SimpleDetteComptatCompense> map = findDetteComptat(idDroit, idVersionDroit, false);
        return map.get(idSection);
    }

    public DetteCompenseOrdreVersementSearch search(DetteCompenseOrdreVersementSearch search)
            throws DetteComptatCompenseException, JadePersistenceException {
        if (search == null) {
            throw new DetteComptatCompenseException("Unable to search search, the model passed is null!");
        }
        return (DetteCompenseOrdreVersementSearch) JadePersistenceManager.search(search);
    }

    @Override
    public SimpleDetteComptatCompenseSearch search(SimpleDetteComptatCompenseSearch search)
            throws JadeApplicationException {
        try {
            Map<String, SimpleDetteComptatCompense> map = findDetteComptat(search.getForIdDroit(),
                    search.getForIdVersionDroit(), search.getForIsCompens());
            JadeAbstractModel[] table = new JadeAbstractModel[map.size()];

            int i = 0;
            for (Entry<String, SimpleDetteComptatCompense> entry : map.entrySet()) {
                table[i] = entry.getValue();
                i++;
            }

            search.setSearchResults(table);
            search.setNbOfResultMatchingQuery(map.size());

            return search;
        } catch (JadePersistenceException e) {
            return null;
        }
    }

    private BigDecimal sumOvDetteCompens(List<DetteCompenseOrdreVersement> list) {
        BigDecimal total = new BigDecimal(0);
        String montant = "0";
        for (DetteCompenseOrdreVersement dette : list) {
            if (dette.getSimpleOrdreVersement().getIsCompense()) {
                montant = dette.getSimpleOrdreVersement().getMontant();
                if (!JadeStringUtil.isBlankOrZero(dette.getSimpleOrdreVersement().getMontantDetteModifier())) {
                    montant = dette.getSimpleOrdreVersement().getMontantDetteModifier();
                }
            }
            total = total.add(new BigDecimal(montant));
        }
        return total;
    }

    @Override
    public SimpleDetteComptatCompense update(SimpleDetteComptatCompense entity) throws JadeApplicationException,
            JadePersistenceException {

        convert(entity);
        DetteComptatCompenseChecker.checkForUpdate(entity);

        SimpleDetteComptatCompense dette = PegasusImplServiceLocator.getSimpleDetteComptatCompenseService().update(
                entity);
        return dette;
    }
}
