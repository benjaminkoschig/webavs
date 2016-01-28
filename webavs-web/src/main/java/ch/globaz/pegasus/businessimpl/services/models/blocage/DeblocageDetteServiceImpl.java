package ch.globaz.pegasus.businessimpl.services.models.blocage;

import globaz.globall.db.BEntity;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
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
import ch.globaz.pegasus.business.constantes.EPCEtatDeblocage;
import ch.globaz.pegasus.business.constantes.EPCTypeDeblocage;
import ch.globaz.pegasus.business.exceptions.models.blocage.BlocageException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.blocage.PcaBloque;
import ch.globaz.pegasus.business.models.blocage.SimpleLigneDeblocage;
import ch.globaz.pegasus.business.models.blocage.SimpleLigneDeblocageSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.blocage.DeblocageDetteService;
import ch.globaz.pegasus.business.vo.blocage.DetteComptat;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class DeblocageDetteServiceImpl implements DeblocageDetteService {

    private void addDetteComptatConjointIfExist(List<SimpleLigneDeblocage> dettesConjoint, List<DetteComptat> list,
            Map<String, SimpleLigneDeblocage> mapDeblocageConjoint) {
        if (dettesConjoint != null) {
            for (SimpleLigneDeblocage deblocage : dettesConjoint) {
                mapDeblocageConjoint.put(
                        deblocage.getIdSectionDetteEnCompta() + "_" + deblocage.getIdRoleDetteEnCompta(), deblocage);
            }
        }
        for (DetteComptat dette : list) {
            String key = dette.getIdSectionDetteEnCompta() + "_" + dette.getIdRoleCA();
            if (mapDeblocageConjoint.containsKey(key)) {
                dette.setMontantCompenseConjoint(new BigDecimal(mapDeblocageConjoint.get(key).getMontant()));
                dette.setHasConjointDetteCompense(true);
            }
        }
    }

    private List<SimpleLigneDeblocage> findDetteDebloqueByIdPca(String idPca) throws BlocageException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleLigneDeblocageSearch search = new SimpleLigneDeblocageSearch();
        search.setForIdPca(idPca);
        search.setForCsTypeDeblocage(EPCTypeDeblocage.CS_DETTE_EN_COMPTA.getCsCode());
        search = PegasusServiceLocator.getSimpleDeblocageService().search(search);
        return PersistenceUtil.typeSearch(search, search.whichModelClass());
    }

    private List<SimpleLigneDeblocage> findDetteDebloqueConjoint(PersonneEtendueComplexModel conjoint,
            PcaBloque pcaBloque) throws BlocageException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        PCAccordee pcaConjoint = findPcaConjoint(conjoint, pcaBloque);
        List<SimpleLigneDeblocage> dettesConjoint = null;
        if (pcaConjoint != null) {
            dettesConjoint = findDetteDebloqueByIdPca(pcaConjoint.getSimplePCAccordee().getIdPCAccordee());
        }
        return dettesConjoint;
    }

    // private List<Creancier> findListCreancier(){
    // Creancier creancier = PegasusServiceLocator.getCreancierService().
    // }

    private List<DetteComptat> findListDetteEnCompta(String idDroit, List<String> listIdTiers) throws BlocageException {
        List<DetteComptat> list = new ArrayList<DetteComptat>();
        try {

            // TODO: Créer un service d'intefacage
            CASectionJoinCompteAnnexeJoinTiersManager mgr = new CASectionJoinCompteAnnexeJoinTiersManager();
            mgr.setForIdTiersIn(listIdTiers);
            mgr.setForSoldePositif(true);

            mgr = (CASectionJoinCompteAnnexeJoinTiersManager) PersistenceUtil.executeOldFind(mgr);

            for (Iterator<BEntity> it = mgr.iterator(); it.hasNext();) {
                CASectionJoinCompteAnnexeJoinTiers e = (CASectionJoinCompteAnnexeJoinTiers) it.next();

                DetteComptat dette = new DetteComptat();
                dette.setIdRoleCA(e.getIdRole());
                dette.setDescriptionCompteAnnexe(e.getDescriptionCompteAnnexe());
                dette.setIdSectionDetteEnCompta(e.getIdSection());
                dette.setMontant(new BigDecimal(e.getSolde()));
                dette.setDescription(CABusinessServiceLocator.getSectionService().findDescription(e.getIdSection()));
                e.getDescriptionCompteAnnexe();
                list.add(dette);
            }
        } catch (Exception e1) {
            throw new BlocageException("Unable to search the dette en compta ", e1);
        }
        return list;
    }

    @Override
    public PCAccordee findPcaByTiersDroitDateDebut(String idTiersConjoint, String idVersionDroit, String dateDebutPca)
            throws BlocageException {

        if (idTiersConjoint == null) {
            throw new BlocageException("Unable to findPcaByTiersDroitDateDebut, the idTiersConjoint pased is null!");
        }
        if (idVersionDroit == null) {
            throw new BlocageException("Unable to findPcaByTiersDroitDateDebut, the idVersionDroit pased is null!");
        }
        if (dateDebutPca == null) {
            throw new BlocageException("Unable to findPcaByTiersDroitDateDebut, the dateDebutPca pased is null!");
        }

        PCAccordee pcaConjoint = null;
        PCAccordeeSearch search = new PCAccordeeSearch();
        search.setForIdTiers(idTiersConjoint);
        search.setForVersionDroit(idVersionDroit);
        search.setForDateDebut(dateDebutPca);
        try {
            search = PegasusImplServiceLocator.getPCAccordeeService().search(search);
        } catch (PCAccordeeException e) {
            throw new BlocageException("Unable to search the pca for the conjoint", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new BlocageException("The service for search the pca conjoin is not available", e);

        } catch (JadePersistenceException e) {
            throw new BlocageException("Unable to search the pca for the conjoint", e);
        }
        if (search.getSize() == 1) {
            pcaConjoint = (PCAccordee) search.getSearchResults()[0];
        } else if (search.getSize() > 1) {
            throw new BlocageException("Too many pca was founded for the conjoint with this criterias -> idTiers: "
                    + idTiersConjoint + " idVersionDroit:" + idVersionDroit + " dateDebutPCa:" + dateDebutPca);
        }

        return pcaConjoint;
    }

    private PCAccordee findPcaConjoint(PersonneEtendueComplexModel conjoint, PcaBloque pcaBloque)
            throws BlocageException, JadeApplicationServiceNotAvailableException {
        PCAccordee pcaConjoint = null;
        if (conjoint != null) {
            pcaConjoint = PegasusImplServiceLocator.getDeblocageDetteService().findPcaByTiersDroitDateDebut(
                    conjoint.getTiers().getIdTiers(), pcaBloque.getIdVersionDroit(), pcaBloque.getDateDebutPca());
        }

        return pcaConjoint;
    }

    @Override
    public List<DetteComptat> generateDetteForDeblocage(List<SimpleLigneDeblocage> deblocagesDettes,
            List<String> listIdTiers, PcaBloque pcaBloque, PersonneEtendueComplexModel conjoint)
            throws JadePersistenceException, JadeApplicationException {

        if (listIdTiers == null) {
            throw new BlocageException("Unable to generateDetteForDeblocage , the listIdTiers is null!");
        }

        if (pcaBloque == null) {
            throw new BlocageException("Unable to generateDetteForDeblocage , the pcaBloque is null!");
        }

        List<SimpleLigneDeblocage> dettesConjoint = findDetteDebloqueConjoint(conjoint, pcaBloque);
        // Recherche des dettes en compta
        List<DetteComptat> dettes = findListDetteEnCompta(pcaBloque.getIdDroit(), listIdTiers);
        List<DetteComptat> dettesMerged = mergedDetteWithDeblocage(dettes, deblocagesDettes, dettesConjoint);
        return dettesMerged;
    }

    private List<DetteComptat> mergedDetteWithDeblocage(List<DetteComptat> dettes,
            List<SimpleLigneDeblocage> deblocagesDettes, List<SimpleLigneDeblocage> dettesConjoint)
            throws JadeApplicationServiceNotAvailableException, JadeApplicationException {

        Map<String, DetteComptat> mapDettes = new HashMap<String, DetteComptat>();
        Map<String, SimpleLigneDeblocage> mapDeblocageEnregistre = new HashMap<String, SimpleLigneDeblocage>();

        Map<String, BigDecimal> mapMerged = new HashMap<String, BigDecimal>();
        List<DetteComptat> list = new ArrayList<DetteComptat>();

        Map<String, SimpleLigneDeblocage> mapDeblocageConjoint = new HashMap<String, SimpleLigneDeblocage>();

        // remplissage des dettes en compta
        for (DetteComptat dette : dettes) {
            mapDettes.put(dette.getIdSectionDetteEnCompta() + "_" + dette.getIdRoleCA(), dette);
        }

        // Merge les dettes en compta et les dettes de déblocage, iteration sur les dettes de déblocage
        if (deblocagesDettes != null) {
            for (SimpleLigneDeblocage deblocage : deblocagesDettes) {
                String key = deblocage.getIdSectionDetteEnCompta() + "_" + deblocage.getIdRoleDetteEnCompta();
                DetteComptat dette = new DetteComptat();
                DeblocageUtils.addValueDeblocage(deblocage, dette);
                if (mapDettes.containsKey(key)) {
                    dette.setMontant(mapDettes.get(key).getMontant());
                    dette.setDescription(mapDettes.get(key).getDescription());
                    dette.setDescriptionCompteAnnexe(mapDettes.get(key).getDescriptionCompteAnnexe());
                } else {
                    dette.setDescription(CABusinessServiceLocator.getSectionService().findDescription(
                            deblocage.getIdSectionDetteEnCompta()));
                    dette.setSettled(true);
                }

                dette.setMontantCompense(new BigDecimal(deblocage.getMontant()));
                dette.setIdRoleCA(deblocage.getIdRoleDetteEnCompta());
                dette.setIdSectionDetteEnCompta(deblocage.getIdSectionDetteEnCompta());

                if (mapMerged.containsKey(key)) {
                    mapMerged.put(key, mapMerged.get(key).add(dette.getMontantCompense()));
                } else {
                    mapMerged.put(key, dette.getMontantCompense());
                }
                list.add(dette);
                if (EPCEtatDeblocage.ENREGISTRE.getCsCode().equals(deblocage.getCsEtat())) {
                    mapDeblocageEnregistre.put(key, deblocage);
                }
            }
        }

        // itertaion sur les dettes
        for (Entry<String, DetteComptat> entry : mapDettes.entrySet()) {
            DetteComptat dette = entry.getValue();
            String key = dette.getIdSectionDetteEnCompta() + "_" + dette.getIdRoleCA();

            if (!mapDeblocageEnregistre.containsKey(key)) {
                list.add(dette);
            }
        }

        addDetteComptatConjointIfExist(dettesConjoint, list, mapDeblocageConjoint);

        return list;
    }

    @Override
    public SimpleLigneDeblocage readDetteDeblocageConjointEnregistrer(String idSectionDette, String idPca)
            throws BlocageException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleLigneDeblocageSearch search = new SimpleLigneDeblocageSearch();
        search.setForCsTypeDeblocage(EPCTypeDeblocage.CS_DETTE_EN_COMPTA.getCsCode());
        search.setForIdSectionDetteEnCompta(idSectionDette);
        search.setForIdPca(idPca);
        search.setForCsEtat(EPCEtatDeblocage.ENREGISTRE.getCsCode());
        search.setWhereKey(SimpleLigneDeblocageSearch.WITH_IDPCA_NOT_EQUALS);
        search = PegasusServiceLocator.getSimpleDeblocageService().search(search);
        if (search.getSize() == 1) {
            return (SimpleLigneDeblocage) search.getSearchResults()[0];
        } else if (search.getSize() > 1) {
            throw new BlocageException(
                    "Too many dette of deblocage was founded wiht this parameters-> idSectionDette: " + idSectionDette
                            + " idPca" + idPca);
        }
        return null;
    }
}
