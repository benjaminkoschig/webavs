package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.corvus.business.exceptions.models.LotException;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModelSearch;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleSearch;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.osiris.exception.OsirisException;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.lot.OrdreVersementForList;
import ch.globaz.pegasus.business.models.lot.OrdreversementTiers;
import ch.globaz.pegasus.business.models.lot.OrdreversementTiersSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;

class ComptabilisationLoader {
    ComptabilisationData data = new ComptabilisationData();
    String idLot = null;

    public ComptabilisationLoader() {
    }

    public ComptabilisationLoader(String idLot, String idOrganeExecution, String numeroOG, String libelleJournal,
            String dateValeur, String dateEchance) throws JadeApplicationException, JadePersistenceException,
            JAException {

        data.setDateValeur(new JADate(dateValeur));
        data.setDateEchance(new JADate(dateEchance));
        data.setNumeroOG(numeroOG);
        data.setIdOrganeExecution(idOrganeExecution);
        data.setLibelleJournal(libelleJournal);
        this.idLot = idLot;
    }

    private List<CompteAnnexeSimpleModel> findCompteAnnexe(List<OrdreVersementForList> ovs,
            List<SectionSimpleModel> sections) throws ComptabiliserLotException,
            JadeApplicationServiceNotAvailableException {
        Set<String> idsCompteAnnexe = new LinkedHashSet<String>();
        for (SectionSimpleModel section : sections) {
            idsCompteAnnexe.add(section.getIdCompteAnnexe());
        }

        for (OrdreVersementForList ov : ovs) {
            if (OrdreVersementTypeResolver.isBeneficiarePrincipal(ov.getSimpleOrdreVersement())
                    || OrdreVersementTypeResolver.isRestitution(ov.getSimpleOrdreVersement())) {

                if (!JadeStringUtil.isBlankOrZero(ov.getIdCompteAnnexeRequerant())) {
                    idsCompteAnnexe.add(ov.getIdCompteAnnexeRequerant());
                }

                if (!JadeStringUtil.isBlankOrZero(ov.getIdCompteAnnexeConjoint())) {
                    idsCompteAnnexe.add(ov.getIdCompteAnnexeConjoint());
                }
            }
        }

        if (idsCompteAnnexe.isEmpty()) {
            StringBuilder listOv = new StringBuilder();
            for (OrdreVersementForList ordre : ovs) {
                listOv.append(" ---> " + ordre.toString());
            }

            throw new ComptabiliserLotException(
                    "Aucun compte annexe n'a été trouvé pour les ordres de versements suivants : " + listOv.toString());
        }

        CompteAnnexeSimpleModelSearch search;
        search = new CompteAnnexeSimpleModelSearch();
        search.setForIdCompteAnnexeIn(idsCompteAnnexe);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        return searchCompteAnnex(search);
    }

    private List<SectionSimpleModel> findSection(List<OrdreVersementForList> ovs) throws ComptabiliserLotException,
            JadeApplicationServiceNotAvailableException, OsirisException {

        Set<String> idsSection = new LinkedHashSet<String>();

        for (OrdreVersementForList ov : ovs) {
            if (OrdreVersementTypeResolver.isDette(ov.getSimpleOrdreVersement())) {
                idsSection.add(ov.getSimpleOrdreVersement().getIdSectionDetteEnCompta());

            }
            if (!JadeStringUtil.isBlankOrZero(ov.getSimpleOrdreVersement().getIdSection())) {
                idsSection.add(ov.getSimpleOrdreVersement().getIdSection());
            }
        }
        if (idsSection.size() > 0) {
            SectionSimpleSearch search = new SectionSimpleSearch();
            search.setInIdsSection(idsSection);
            search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            return CABusinessServiceLocator.getSectionService().search(search);
        } else {
            return new ArrayList<SectionSimpleModel>();
        }
    }

    private Map<String, OrdreversementTiers> generateMapPersonne(List<OrdreversementTiers> tiers) {
        Map<String, OrdreversementTiers> mapTiers = new HashMap<String, OrdreversementTiers>();
        for (OrdreversementTiers t : tiers) {
            mapTiers.put(t.getIdTiers(), t);
        }
        return mapTiers;
    }

    public ComptabilisationData getData() {
        return data;
    }

    public ComptabilisationData load() throws JadeApplicationException, JadePersistenceException, JAException {
        data.setListOV(PegasusServiceLocator.getOrdreVersementService().searchOvByLot(idLot));
        loadStandard();
        return data;
    }

    public ComptabilisationData loadForCheck() throws LotException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, PmtMensuelException, JAException {
        data.setSimpleLot(CorvusServiceLocator.getLotService().read(idLot));
        data.setDateDernierPmt(new JADate(PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt()));
        return data;
    }

    public ComptabilisationData loadForPrestation(String idPrestation) throws JadeApplicationException,
            JadePersistenceException, JAException {
        data.setListOV(PegasusServiceLocator.getOrdreVersementService().searchOvByPrestation(idPrestation));
        idLot = PegasusImplServiceLocator.getSimplePrestationService().read(idPrestation).getIdLot();
        data.setDateEchance(new JADate(JACalendar.todayJJsMMsAAAA()));
        data.setDateValeur(new JADate(JACalendar.todayJJsMMsAAAA()));
        loadStandard();
        return data;
    }

    private void loadStandard() throws ComptabiliserLotException, JadeApplicationServiceNotAvailableException,
            OsirisException, JadePersistenceException, JadeApplicationException {
        if (data.getListOV().size() == 0) {
            throw new ComptabiliserLotException(" Any ov founded unabled to load!");
        }
        data.setSections(findSection(data.getListOV()));
        data.setComptesAnnexes(findCompteAnnexe(data.getListOV(), data.getSections()));
        data.setMapIdTierDescription(generateMapPersonne(searchTiers(data.getListOV())));
    }

    private List<CompteAnnexeSimpleModel> searchCompteAnnex(CompteAnnexeSimpleModelSearch search)
            throws JadeApplicationServiceNotAvailableException, ComptabiliserLotException {
        try {
            return CABusinessServiceLocator.getCompteAnnexeService().search(search);
        } catch (OsirisException e) {
            throw new ComptabiliserLotException("Unable to search the comtpe annexe", e);
        }
    }

    private List<OrdreversementTiers> searchTiers(List<OrdreVersementForList> ovs) throws JadePersistenceException,
            JadeApplicationException {

        Set<String> idsTiers = new LinkedHashSet<String>();
        for (OrdreVersementForList ov : ovs) {
            if (OrdreVersementTypeResolver.isBeneficiarePrincipal(ov.getSimpleOrdreVersement())
                    || OrdreVersementTypeResolver.isRestitution(ov.getSimpleOrdreVersement())) {
                if (!JadeStringUtil.isBlankOrZero(ov.getSimpleOrdreVersement().getIdTiersAdressePaiementConjoint())) {
                    idsTiers.add(ov.getSimpleOrdreVersement().getIdTiersAdressePaiementConjoint());
                }
                if (!JadeStringUtil.isBlankOrZero(ov.getSimpleOrdreVersement().getIdTiersAdressePaiement())) {
                    idsTiers.add(ov.getSimpleOrdreVersement().getIdTiersAdressePaiement());
                }
                if (!JadeStringUtil.isBlankOrZero(ov.getSimpleOrdreVersement().getIdTiers())) {
                    idsTiers.add(ov.getSimpleOrdreVersement().getIdTiers());
                }
                if (!JadeStringUtil.isBlankOrZero(ov.getSimpleOrdreVersement().getIdTiersConjoint())) {
                    idsTiers.add(ov.getSimpleOrdreVersement().getIdTiersConjoint());
                }
            } else if (OrdreVersementTypeResolver.isCreancier(ov.getSimpleOrdreVersement())) {
                idsTiers.add(ov.getSimpleOrdreVersement().getIdTiers());
            }
        }

        List<OrdreversementTiers> tiers = PersistenceUtil.searchByLot(idsTiers,
                new PersistenceUtil.SearchLotExecutor<OrdreversementTiers>() {
                    @Override
                    public JadeAbstractSearchModel execute(List<String> ids) throws JadeApplicationException,
                            JadePersistenceException {
                        OrdreversementTiersSearch search = new OrdreversementTiersSearch();
                        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                        search.setInIdsTiers(ids);
                        return JadePersistenceManager.search(search);
                    }
                }, 1000);
        return tiers;

    }
}
