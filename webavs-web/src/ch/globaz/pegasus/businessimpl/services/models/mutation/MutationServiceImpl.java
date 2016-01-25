package ch.globaz.pegasus.businessimpl.services.models.mutation;

import globaz.corvus.api.recap.IRERecap;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ch.globaz.corvus.business.exceptions.models.InfoRecapException;
import ch.globaz.corvus.business.models.recapinfo.SimpleInfoRecap;
import ch.globaz.corvus.business.models.recapinfo.SimpleInfoRecapSearch;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.exceptions.models.MutationException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.DecisionValidationPca;
import ch.globaz.pegasus.business.models.decision.DecisionValidationPcaSearch;
import ch.globaz.pegasus.business.models.mutation.MutationInfoRecap;
import ch.globaz.pegasus.business.models.mutation.MutationMontantPCAFileds;
import ch.globaz.pegasus.business.models.mutation.MutationMontantPCAFiledsSearch;
import ch.globaz.pegasus.business.services.models.mutation.MutationService;
import ch.globaz.pegasus.business.vo.lot.MutationPCA;
import ch.globaz.pegasus.businessimpl.utils.PegasusDateUtil;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;

public class MutationServiceImpl implements MutationService {

    // public List<MutationMontantPCA> findMutationMontantPCA(String dateMonth) throws JadePersistenceException,
    // MutationException {
    // MutationMontantPCASearch search = new MutationMontantPCASearch();
    //
    // String date;
    // if (JadeDateUtil.isGlobazDateMonthYear(dateMonth)) {
    // date = dateMonth;
    // } else if (JadeDateUtil.isGlobazDate(dateMonth)) {
    // date = dateMonth.substring(3);
    // } else {
    // throw new MutationException("dateMonth is not a vlide date");
    // }
    //
    // int nbJour = PegasusDateUtil.getLastDayOfMonth(date);
    //
    // search.setForDateDecisionMin("01." + date);
    // search.setForDateDecisionMax(Integer.valueOf(nbJour) + "." + date);
    // // search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
    // List<MutationMontantPCA> list = PersistenceUtil.search(search, search.whichModelClass());
    // return list;
    // }

    public MutationInfoRecap findInfoRecapByDate(String date) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, MutationException {
        SimpleInfoRecapSearch search = new SimpleInfoRecapSearch();
        MutationInfoRecap mutationInfoRecap = new MutationInfoRecap();
        search.setForDate(date);
        try {
            search = CorvusServiceLocator.getInfoRecapService().search(search);
        } catch (InfoRecapException e) {
            throw new MutationException("Unable to search the infoRecap");
        }
        for (JadeAbstractModel model : search.getSearchResults()) {
            SimpleInfoRecap simpleInfoRecap = (SimpleInfoRecap) model;

            if (IRERecap.RECAP_PC_MONTANT_AI.equals(simpleInfoRecap.getCode())) {
                mutationInfoRecap.setMontantTotalPCAVS(simpleInfoRecap.getMontant());
            } else if (IRERecap.RECAP_PC_MONTANT_AVS.equals(simpleInfoRecap.getCode())) {
                mutationInfoRecap.setMontantTotalPCAI(simpleInfoRecap.getMontant());
            } else if (IRERecap.RECAP_PC_NOMBRE_AI.equals(simpleInfoRecap.getCode())) {
                mutationInfoRecap.setNbPrestationPCAI(simpleInfoRecap.getMontant());
            } else if (IRERecap.RECAP_PC_NOMBRE_AVS.equals(simpleInfoRecap.getCode())) {
                mutationInfoRecap.setNbPrestationPCAvs(simpleInfoRecap.getMontant());
            }
        }

        return mutationInfoRecap;

    }

    private List<MutationMontantPCAFileds> findMutation(String dateMonth, boolean oldAugmentationFuture)
            throws JadePersistenceException, MutationException {
        MutationMontantPCAFiledsSearch search = new MutationMontantPCAFiledsSearch();

        String date;
        if (JadeDateUtil.isGlobazDateMonthYear(dateMonth)) {
            date = dateMonth;
        } else if (JadeDateUtil.isGlobazDate(dateMonth)) {
            date = dateMonth.substring(3);
        } else {
            throw new MutationException("dateMonth is not a vlide date");
        }

        int nbJour = PegasusDateUtil.getLastDayOfMonth(date);

        if (oldAugmentationFuture) {
            search.setWhereKey("wasAugementationFutur");
            search.setForDateDecisionMin(JadeDateUtil.addMonths("01." + dateMonth, -1));
            search.setForDateDecisionMax(JadeDateUtil.addMonths(Integer.valueOf(nbJour) + "." + dateMonth, -1));
            search.setForDateMonthFutur(dateMonth);
        } else {
            search.setForDateDecisionMin("01." + date);
            search.setForDateDecisionMax(Integer.valueOf(nbJour) + "." + date);
        }

        search.setForIsPlanRetenu(true);
        search.setInEtatPca(Arrays.asList(new String[] { IPCPCAccordee.CS_ETAT_PCA_VALIDE,
                IPCPCAccordee.ETAT_PCA_COURANT_VALIDE, IPCPCAccordee.CS_ETAT_PCA_HISTORISEE }));
        search.setInEtatPcaPrecedante(Arrays.asList(new String[] { IPCPCAccordee.CS_ETAT_PCA_HISTORISEE }));
        search.setForIsSupprime(false);
        search.setInEtatDroitCourant(Arrays.asList(new String[] { IPCDroits.CS_COURANT_VALIDE, IPCDroits.CS_VALIDE,
                IPCDroits.CS_HISTORISE }));
        search.setInEtatDroitPrecedant(Arrays.asList(new String[] { IPCDroits.CS_HISTORISE }));
        // search.setForIdPcaParent(forIdPcaParent);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        List<MutationMontantPCAFileds> list = PersistenceUtil.search(search, search.whichModelClass());
        return list;
    }

    @Override
    public List<MutationPCA> findMutationMontantPCA(String dateMonth) throws JadePersistenceException,
            MutationException {
        List<MutationPCA> listAvecRetro = new ArrayList<MutationPCA>();
        int nbMonth = 0;

        List<MutationMontantPCAFileds> list = findMutation(dateMonth, false);

        for (MutationMontantPCAFileds mutation : list) {
            MutationPCA mutationPCA = mapMutation(mutation);
            // On test si on a un passage d'un couple a séparé par la maladie.
            if (!mutation.getIdTiersActuel().equals(mutation.getIdTiersPrecedant())) {
                mutationPCA.setToSeparerMal(true);
            }

            if (JadeDateUtil.isDateMonthYearAfter(mutationPCA.getDateDebutPcaActuel(), dateMonth)) {
                mutationPCA.setAugementationFutur(true);
                mutationPCA.setMontantRetro("");
            } else {
                mutationPCA.setAugementationFutur(false);
                if (!IPCDecision.CS_PREP_COURANT.equals(mutation.getCsTypePreparationDecision())) {
                    if (!IPCDecision.CS_TYPE_SUPPRESSION_SC.equals(mutation.getTypeDecision())) {
                        if (IPCDecision.CS_PREP_RETRO.equals(mutation.getCsTypePreparationDecision())
                                || !JadeStringUtil.isEmpty(mutation.getDateFinPcaActuel())) {
                            nbMonth = JadeDateUtil.getNbMonthsBetween("01." + mutation.getDateDebutPcaActuel(), "01."
                                    + mutation.getDateFinPcaActuel()) + 1;
                        } else {
                            nbMonth = JadeDateUtil.getNbMonthsBetween("01." + mutation.getDateDebutPcaActuel(), "01."
                                    + dateMonth);
                        }

                        mutationPCA.setMontantRetro((new BigDecimal(mutation.getMontantActuel())
                                .multiply(new BigDecimal(nbMonth))).toString());
                        if (JadeDateUtil.isDateMonthYearBefore(mutationPCA.getDateFinPcaActuel(), dateMonth)) {
                            mutationPCA.setPurRetro(true);
                            // On ne peut pas avoir du retro avec une décision courante
                            if (!mutation.getNoVersion().equals("1")) {
                                mutationPCA.setHasDiminutation(true);
                            } else {
                                mutationPCA.setHasDiminutation(false);
                            }
                        }
                    } else {
                        mutationPCA.setHasDiminutation(true);
                    }
                }
            }

            listAvecRetro.add(mutationPCA);
        }

        return listAvecRetro;
    }

    @Override
    public List<MutationPCA> findMutationMontantPCAnew(String date) throws JadePersistenceException, MutationException {
        // TODO Auto-generated method stub
        return null;
    }

    // public Map<String, BigDecimal> findRetros(Set<String> listIdVersionDroit) throws JadePersistenceException,
    // MutationException {
    // Map<String, BigDecimal> retros = new HashMap<String, BigDecimal>();
    // try {
    // OrdreVersementForListSearch ovSearch = new OrdreVersementForListSearch();
    // ovSearch.setForInIdVersionDroit(listIdVersionDroit);
    // ovSearch.setForCsInTypeOv(Arrays.asList(IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL,
    // IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION));
    //
    // ovSearch = PegasusServiceLocator.getOrdreVersementService().search(ovSearch);
    // List<OrdreVersementForList> listov = PersistenceUtil.typeSearch(ovSearch, ovSearch.whichModelClass());
    //
    // Map<String, List<OrdreVersementForList>> mapOv = JadeListUtil.groupBy(listov,
    // new Key<OrdreVersementForList>() {
    // public String exec(OrdreVersementForList e) {
    // return e.getIdPca();
    // }
    // });
    //
    // for (Entry<String, List<OrdreVersementForList>> entry : mapOv.entrySet()) {
    // BigDecimal montantRestitution = new BigDecimal(0);
    // BigDecimal montantBeneficiaire = new BigDecimal(0);
    // BigDecimal retro = null;
    // // on peut avoir 2 restiution et 2 paiement (Dom2R)
    // if (entry.getValue().size() <= 4) {
    // for (OrdreVersementForList ov : entry.getValue()) {
    // if (IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL.equals(ov.getSimpleOrdreVersement()
    // .getCsType())) {
    // montantBeneficiaire = montantBeneficiaire.add(new BigDecimal(ov.getSimpleOrdreVersement()
    // .getMontant()));
    // } else if (IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION.equals(ov
    // .getSimpleOrdreVersement().getCsType())) {
    // montantRestitution = montantRestitution.add(new BigDecimal(ov.getSimpleOrdreVersement()
    // .getMontant()));
    // } else {
    // throw new MutationException("Type d'ordre de versment non pris en charge ("
    // + ov.getSimpleOrdreVersement().getIdOrdreVersement()
    // + ")"
    // + BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
    // ov.getSimpleOrdreVersement().getCsType()));
    // }
    // }
    // retro = montantBeneficiaire.subtract(montantRestitution);
    // retros.put(entry.getKey(), retro);
    // } else {
    // throw new MutationException("Trop d'ordre de versment ont été trouvé pour la pca(" + entry.getKey()
    // + ")");
    // }
    // }
    //
    // } catch (OrdreVersementException e) {
    // throw new MutationException("Unable to find the retro", e);
    // } catch (JadeApplicationServiceNotAvailableException e) {
    // throw new MutationException("The service for search the ordreVersment is not up", e);
    // }
    // return retros;
    // }

    @Override
    public List<MutationPCA> findOldAugmentationFutur(String dateMonth) throws MutationException,
            JadePersistenceException {
        List<MutationPCA> listAvecRetro = new ArrayList<MutationPCA>();
        List<MutationMontantPCAFileds> listOldAugmentationFutur = findMutation(dateMonth, true);

        for (MutationMontantPCAFileds mutation : listOldAugmentationFutur) {
            MutationPCA mutationPCA = mapMutation(mutation);
            listAvecRetro.add(mutationPCA);
        }
        return listAvecRetro;

    }

    public MutationPCA mapMutation(MutationMontantPCAFileds mutation) {
        MutationPCA mutationPCA = new MutationPCA();
        mutationPCA.setAncienMontant(mutation.getAncienMontant());
        mutationPCA.setAvsToAi(mutation.isAvsToAi());
        mutationPCA.setIdVersionDroit(mutation.getIdVersionDroit());
        mutationPCA.setMontantActuel(mutation.getMontantActuel());
        mutationPCA.setNom(mutation.getNom());
        mutationPCA.setNss(mutation.getNss());
        mutationPCA.setPrenom(mutation.getPrenom());
        mutationPCA.setTypePcActuel(mutation.getTypePcActuel());
        mutationPCA.setTypePcPrecedant(mutation.getTypePcPrecedant());
        mutationPCA.setIdPca(mutation.getIdPcaActuel());
        mutationPCA.setCsTypePreparationDecision(mutation.getCsTypePreparationDecision());
        mutationPCA.setDateDebutPcaActuel(mutation.getDateDebutPcaActuel());
        mutationPCA.setDateDebutPcaPrecedant(mutation.getDateDebutPcaPrecedant());
        mutationPCA.setDateFinPcaActuel(mutation.getDateFinPcaActuel());
        mutationPCA.setDateFinPcaPrecedant(mutation.getDateFinPcaPrecedant());
        mutationPCA.setNoVersion(mutation.getNoVersion());
        mutationPCA.setTypeDecision(mutation.getTypeDecision());
        mutationPCA.setCsMotif(mutation.getCsMotif());
        return mutationPCA;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.decision.DecisionService# searchDecisions
     * (ch.globaz.pegasus.business.models.decision.DecisionSearch)
     */
    private List<DecisionValidationPca> searchDecisions(DecisionValidationPcaSearch search) throws DecisionException,
            JadePersistenceException {
        if (search == null) {
            throw new DecisionException("Unable to search decisions, the search model passed is null!");
        }
        return PersistenceUtil.search(search, search.whichModelClass());
    }

}
