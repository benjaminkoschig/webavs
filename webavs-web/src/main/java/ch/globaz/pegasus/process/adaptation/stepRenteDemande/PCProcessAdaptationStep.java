package ch.globaz.pegasus.process.adaptation.stepRenteDemande;

import globaz.globall.db.BSessionUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEOutputAnnonce;
import globaz.hermes.service.HEAnnoncesCentrale;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepBeforable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepEnable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import ch.globaz.jade.process.utils.JadeProcessCommonUtils;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.RenteAvsAiException;
import ch.globaz.pegasus.business.models.renteijapi.RenteMembreFamilleCalculeField;
import ch.globaz.pegasus.business.models.renteijapi.RenteMembreFamilleCalculeFieldSearch;
import ch.globaz.pegasus.business.models.renteijapi.RenteMembreFamilleCalculeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pegasus.process.adaptation.PCProcessAdapationEnum;

public class PCProcessAdaptationStep implements JadeProcessStepInterface, JadeProcessStepEnable,
        JadeProcessStepBeforable {

    private String idProcess;
    private Map<Enum<?>, String> mapProperties;
    private Map<String, List<RenteMembreFamilleCalculeField>> mapRente;
    private int JEUNE_ADULTE = 19;

    @Override
    public void before(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        mapProperties = map;
        mapRente = findRente();
        idProcess = step.getIdExecutionProcess();
    }

    private Map<String, List<IHEOutputAnnonce>> findAnnonce() throws Exception {
        HEAnnoncesCentrale annoncesCentrale = new HEAnnoncesCentrale();

        IHEOutputAnnonce[] annonceRentes = annoncesCentrale.getAnnoncesAdaptationRentesPC(BSessionUtil
                .getSessionFromThreadContext().getCurrentThreadTransaction());

        Map<String, List<IHEOutputAnnonce>> mapAnnonceRentes = JadeListUtil.groupBy(Arrays.asList(annonceRentes),
                new JadeListUtil.Key<IHEOutputAnnonce>() {
                    @Override
                    public String exec(IHEOutputAnnonce e) {
                        try {
                            return e.getField(IHEAnnoncesViewBean.PC_REFERENCE_INTERNE_OFFICE_PC);
                        } catch (Exception e1) {
                            JadeProcessCommonUtils.addError(e1);
                        }
                        return null;
                    }
                });
        return mapAnnonceRentes;
    }

    private Map<String, List<RenteMembreFamilleCalculeField>> findRente() throws JadePersistenceException,
            RenteAvsAiException, JadeApplicationServiceNotAvailableException {
        RenteMembreFamilleCalculeFieldSearch search = new RenteMembreFamilleCalculeFieldSearch();
        List<String> csInEtat = new ArrayList<String>();
        csInEtat.add(IPCDroits.CS_VALIDE);
        csInEtat.add(IPCDroits.CS_COURANT_VALIDE);
        csInEtat.add(IPCDroits.CS_HISTORISE);

        search.setInCsEtatVersionDroit(csInEtat);
        search.setForCsEtatDemande(IPCDemandes.CS_OCTROYE);
        search.setOrderKey(RenteMembreFamilleCalculeSearch.ORDER_DROIT);
        search.setWhereKey(RenteMembreFamilleCalculeSearch.WITH_DATE_VALABLE);
        search.setForDate(mapProperties.get(PCProcessAdapationEnum.DATE_ADAPTATION));
        search.setForIsPlanRetenu(true);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        List<String> csInEtatPCA = new ArrayList<String>();
        csInEtatPCA.add(IPCPCAccordee.ETAT_PCA_COURANT_VALIDE);
        csInEtatPCA.add(IPCPCAccordee.CS_ETAT_PCA_VALIDE);
        search.setInCsEtatPca(csInEtatPCA);

        search = PegasusServiceLocator.getRenteIjApiService().searchRenteMembreFamilleCalcule(search);

        List<RenteMembreFamilleCalculeField> list = PersistenceUtil.typeSearch(search, search.whichModelClass());

        // Ajout des enfants exclus du plan de calcul retenu
        search.setForIsPlanRetenu(false);
        RenteMembreFamilleCalculeFieldSearch newSearch = PegasusServiceLocator.getRenteIjApiService().searchRenteMembreFamilleCalcule(search);
        list = addChildToList(newSearch, list);

        Map<String, List<RenteMembreFamilleCalculeField>> mapRente = JadeListUtil.groupBy(list,
                new JadeListUtil.Key<RenteMembreFamilleCalculeField>() {
                    @Override
                    public String exec(RenteMembreFamilleCalculeField e) {
                        return e.getIdDemandePC();
                    }
                });

        return mapRente;
    }

    private List<RenteMembreFamilleCalculeField> addChildToList(RenteMembreFamilleCalculeFieldSearch newSearch, List<RenteMembreFamilleCalculeField> list) {
        List<RenteMembreFamilleCalculeField> newList = list;
        for (JadeAbstractModel objet : newSearch.getSearchResults()) {
            RenteMembreFamilleCalculeField model = (RenteMembreFamilleCalculeField) objet;
            boolean toAdd = true;
            for (RenteMembreFamilleCalculeField listElement : list) {
                if (listElement.getIdDemandePC().equals(model.getIdDemandePC()) && listElement.getNss().equals(model.getNss())) {
                    toAdd = false;
                    break;
                }
            }
            if (toAdd && isChild(model)) {
                newList.add(model);
            }
        }
        return newList;

    }

    private boolean isChild(RenteMembreFamilleCalculeField model) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("dd.MM.yyyy").toFormatter();
        LocalDate dateNaissance = LocalDate.parse(model.getDateNaissance(), formatter);
        LocalDate toDay = LocalDate.now();

        return toDay.minusYears(JEUNE_ADULTE).isBefore(dateNaissance);
    }

    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new PCProcessAdaptationEntityHandler(mapRente, idProcess);
    }

    @Override
    public Boolean isEnabled(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        if ("true".equalsIgnoreCase(map.get(PCProcessAdapationEnum.HAS_ADAPTATION_DES_RENTE))) {
            return true;
        } else {
            return false;
        }
    }

}
