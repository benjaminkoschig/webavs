package ch.globaz.pegasus.businessimpl.services.doc.excel.impl;

import globaz.framework.util.FWCurrency;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.op.common.merge.IMergingContainer;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.jade.process.business.JadeProcessServiceLocator;
import ch.globaz.jade.process.business.enumProcess.JadeProcessEntityStateEnum;
import ch.globaz.jade.process.business.exceptions.EntiteException;
import ch.globaz.jade.process.business.exceptions.JadeProcessException;
import ch.globaz.jade.process.business.models.logInfo.EntityLogsProperties;
import ch.globaz.jade.process.business.models.process.ProcessStep;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.doc.DocException;
import ch.globaz.pegasus.business.models.droit.VersionDroit;
import ch.globaz.pegasus.business.models.droit.VersionDroitSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeePlanCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeePlanCalculSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalculSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pegasus.process.adaptation.PCProcessAdapationEnum;

public class ListeControleProcessAdaptation extends PegasusAbstractExcelServiceImpl {
    public final static String MODEL_NAME = "listeDeControleProcessAdaptation.xml";
    private String idExecutionProcess = null;
    private String outPutName = "listeDeControleProcessAdaptation";

    public ExcelmlWorkbook createDoc(String idExecutionProcess) throws DocException {
        if (idExecutionProcess == null) {
            throw new DocException("Unable to execute createDoc, the idLot is null!");
        }
        setIdExecutionProcess(idExecutionProcess);
        return this.createDoc();
    }

    public String createDocAndSave(String idExecutionProcess) throws Exception {
        ExcelmlWorkbook wk = this.createDoc(idExecutionProcess);
        String nomDoc = outPutName + "_" + JadeUUIDGenerator.createStringUUID() + ".xml";
        return save(wk, nomDoc);
    }

    private Map<String, List<VersionDroit>> findDroitCalcule(List<EntityLogsProperties<PCProcessAdapationEnum>> list)
            throws JadePersistenceException, JadeApplicationException {
        List<String> listId = new ArrayList<String>();
        for (EntityLogsProperties<PCProcessAdapationEnum> entity : list) {
            listId.add(entity.getSimpleEntite().getIdRef());
        }

        List<VersionDroit> listPca = PersistenceUtil.searchByLot(listId,
                new PersistenceUtil.SearchLotExecutor<VersionDroit>() {
                    @Override
                    public JadeAbstractSearchModel execute(List<String> ids) throws JadeApplicationException,
                            JadePersistenceException {
                        VersionDroitSearch search = new VersionDroitSearch();
                        search.setForCsEtatDroit(IPCDroits.CS_CALCULE);
                        search.setInIdDemandes(ids);
                        search.setForCsModif(IPCDroits.CS_MOTIF_DROIT_ADAPTATION);
                        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                        return PegasusServiceLocator.getDroitService().searchVersionDroit(search);
                    }
                }, 2000);

        // On regroupe les demandes
        Map<String, List<VersionDroit>> map = JadeListUtil.groupBy(listPca, new JadeListUtil.Key<VersionDroit>() {
            @Override
            public String exec(VersionDroit e) {
                return e.getDemande().getSimpleDemande().getIdDemande();
            }
        });
        return map;
    }

    private Map<String, List<PCAccordeePlanCalcul>> findPcaInError(
            List<EntityLogsProperties<PCProcessAdapationEnum>> list, String dateAdaptation)
            throws JadePersistenceException, JadeApplicationException {
        List<String> idsDemande = new ArrayList<String>();
        Map<String, List<PCAccordeePlanCalcul>> mapDemandeWithPCaEnErreur = new HashMap<String, List<PCAccordeePlanCalcul>>();
        for (EntityLogsProperties<PCProcessAdapationEnum> entity : list) {
            if (JadeProcessEntityStateEnum.ERROR.equals(entity.getSimpleEntite().getCsEtat())) {
                idsDemande.add(entity.getSimpleEntite().getIdRef());
            }
        }
        if (idsDemande.size() > 0) {

            List<PCAccordeePlanCalcul> listPca = null;

            listPca = PersistenceUtil.searchByLot(idsDemande,
                    new PersistenceUtil.SearchLotExecutor<PCAccordeePlanCalcul>() {
                        @Override
                        public JadeAbstractSearchModel execute(List<String> ids) throws JadeApplicationException,
                                JadePersistenceException {
                            PCAccordeePlanCalculSearch pcAccordeePlanCalculSearch = new PCAccordeePlanCalculSearch();
                            pcAccordeePlanCalculSearch
                                    .setWhereKey(PCAccordeePlanCalculSearch.FOR_CURRENT_VERSIONED_FOR_ADAPTATION);
                            pcAccordeePlanCalculSearch.setForInIdDemande(ids);
                            pcAccordeePlanCalculSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                            pcAccordeePlanCalculSearch.setForCsEtat(IPCPCAccordee.CS_ETAT_PCA_VALIDE);
                            return PegasusServiceLocator.getPCAccordeeService().search(pcAccordeePlanCalculSearch);
                        }
                    }, 2000);

            // On regroupe les demandes qui sont en erreur
            mapDemandeWithPCaEnErreur = JadeListUtil.groupBy(listPca, new JadeListUtil.Key<PCAccordeePlanCalcul>() {
                @Override
                public String exec(PCAccordeePlanCalcul e) {
                    return e.getSimpleDemande().getIdDemande();
                }
            });
        }

        return mapDemandeWithPCaEnErreur;
    }

    private Map<String, SimplePlanDeCalcul> findPlanCal(List<EntityLogsProperties<PCProcessAdapationEnum>> list)
            throws JadePersistenceException, JadeApplicationException {
        List<String> listId = new ArrayList<String>();
        for (EntityLogsProperties<PCProcessAdapationEnum> entity : list) {
            if (!JadeStringUtil.isBlankOrZero(entity.getProperties().get(PCProcessAdapationEnum.ID_PLAN_CALCUL_RETENU))) {
                listId.add(entity.getProperties().get(PCProcessAdapationEnum.ID_PLAN_CALCUL_RETENU));
            }
            if (!JadeStringUtil.isBlankOrZero(entity.getProperties().get(
                    PCProcessAdapationEnum.ID_PLAN_CALCUL_RETENU_CONJOINT))) {
                listId.add(entity.getProperties().get(PCProcessAdapationEnum.ID_PLAN_CALCUL_RETENU_CONJOINT));
            }
        }

        List<SimplePlanDeCalcul> listCalcule = PersistenceUtil.searchByLot(listId,
                new PersistenceUtil.SearchLotExecutor<SimplePlanDeCalcul>() {
                    @Override
                    public JadeAbstractSearchModel execute(List<String> ids) throws JadeApplicationException,
                            JadePersistenceException {
                        SimplePlanDeCalculSearch search = new SimplePlanDeCalculSearch();
                        search.setInIdPlanDeCalcul(ids);
                        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                        return PegasusImplServiceLocator.getSimplePlanDeCalculService().search(search);
                    }
                }, 2000);

        Map<String, SimplePlanDeCalcul> map = new HashMap<String, SimplePlanDeCalcul>();
        for (SimplePlanDeCalcul calcule : listCalcule) {
            map.put(calcule.getIdPlanDeCalcul(), calcule);
        }

        return map;
    }

    /**
     * Calcul de la diff entre l'ancien et le nouveau montant
     */
    private String getDiffPc(String oldPc, String newPc) {
        String diff = "";

        if (!JadeStringUtil.isBlank(newPc) && !JadeStringUtil.isBlank(oldPc)) {

            diff = String.valueOf((new BigDecimal(newPc).subtract(new BigDecimal(oldPc))));
        } else {
            if (!JadeStringUtil.isBlank(newPc)) {
                diff = newPc;
            } else {
                diff = oldPc;
            }
        }

        return diff;
    }

    public String getIdExecutionProcess() {
        return idExecutionProcess;
    }

    public String getMessage(String messageId) {
        return JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(), messageId);
    }

    @Override
    public String getModelName() {
        return ListeControleProcessAdaptation.MODEL_NAME;
    }

    @Override
    public String getOutPutName() {
        return outPutName;
    }

    @Override
    public IMergingContainer loadResults() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {

        PegasusContainer container = new PegasusContainer();

        BigDecimal totalPcaInError = new BigDecimal(0);
        BigDecimal totalWihtOutErrorNewPca = new BigDecimal(0);
        BigDecimal totalWihtOutErrorOldPca = new BigDecimal(0);
        BigDecimal totalWihtOutErrorEcart = new BigDecimal(0);

        BigDecimal totalOldPca = new BigDecimal(0);

        Integer nbPcaInerror = 0;
        Integer nbPca = 0;
        String diff = "0";
        String montantError = "9999999";
        Map<Enum<?>, String> propMap = JadeProcessServiceLocator.getPropertiesService().findPropertiesProvided(
                idExecutionProcess);

        List<EntityLogsProperties<PCProcessAdapationEnum>> list = searchEntity();

        ProcessStep step = JadeProcessServiceLocator.getProcessStepService().findByIdexecutionProcess(
                idExecutionProcess);

        String dateAdaptation = propMap.get(PCProcessAdapationEnum.DATE_ADAPTATION);
        Map<String, List<PCAccordeePlanCalcul>> mapDemandeWithPCaEnErreur = findPcaInError(list, dateAdaptation);

        Map<String, List<VersionDroit>> mapDroitCalcule = new HashMap<String, List<VersionDroit>>();

        if (mustCheckDroitStat(step)) {
            mapDroitCalcule = findDroitCalcule(list);
        }

        Map<String, SimplePlanDeCalcul> mapCalcul = findPlanCal(list);
        for (EntityLogsProperties<PCProcessAdapationEnum> entity : list) {
            // Regarde si une modification a été faite dans les données financière et que le droit n'a pas été
            // recalculé.
            List<VersionDroit> versionDroitCalcule = null;
            // if (this.mustCheckDroitStat(step)) {
            versionDroitCalcule = mapDroitCalcule.get(entity.getSimpleEntite().getIdRef().trim());
            // }
            String messageError = "";
            if ((versionDroitCalcule != null) && (versionDroitCalcule.size() > 1)) {
                messageError = "Too many droit 'Au calcule' found for this idDroit:"
                        + versionDroitCalcule.get(0).getSimpleDroit().getIdDroit();
            }
            if (entity.getSimpleEntite().getIsManual()) {
                montantError = "0";
            }

            // Permet d'identifier si le cas a été calculé depuis l'application si c'et le cas le plan de calcule sera
            // vide
            SimplePlanDeCalcul simplePlanDeCalcul = mapCalcul.get(entity.getProperties().get(
                    PCProcessAdapationEnum.ID_PLAN_CALCUL_RETENU));

            if (!JadeProcessEntityStateEnum.ERROR.equals(entity.getSimpleEntite().getCsEtat())
                    && entity.getProperties().containsKey(PCProcessAdapationEnum.NEW_RENTE_AVS_AI)) {
                nbPca++;

                // la ligne du requérant
                container.put("description", entity.getSimpleEntite().getDescription());
                container.put("manual", entity.getSimpleEntite().getIsManual().toString());

                boolean hasValues = !mustCheckDroitStat(step) || (versionDroitCalcule != null);

                String csEtatPca = "";
                String csOldEtatPca = "";
                String fleche = "";
                if ((simplePlanDeCalcul != null) && hasValues) {
                    container.put("csEtat", entity.getSimpleEntite().getCsEtat().toLabel());
                    container.put("newRenteAVsAI", entity.getProperties().get(PCProcessAdapationEnum.NEW_RENTE_AVS_AI));
                    container.put("newNbEnfant", entity.getProperties().get(PCProcessAdapationEnum.NEW_NB_ENFANT));
                    container.put("newPrixHome", entity.getProperties().get(PCProcessAdapationEnum.NEW_PRIX_HOME));
                    container.put("newDonation", entity.getProperties().get(PCProcessAdapationEnum.NEW_DONATION));
                    container.put("newMontant", entity.getProperties().get(PCProcessAdapationEnum.NEW_PC));
                    if (entity.getProperties().containsKey(PCProcessAdapationEnum.NEW_ETAT_PC)) {
                        if (IPCValeursPlanCalcul.STATUS_REFUS.equals(entity.getProperties().get(
                                PCProcessAdapationEnum.NEW_ETAT_PC))) {
                            csEtatPca = "R";
                            fleche = " -> ";
                        }
                    }
                    totalWihtOutErrorNewPca = totalWihtOutErrorNewPca.add(new BigDecimal(entity.getProperties().get(
                            PCProcessAdapationEnum.NEW_PC)));
                } else {
                    container.put("csEtat", getMessage("pegasus.process.adaptation.etat.aCalculer"));
                    container.put("newRenteAVsAI", montantError);
                    container.put("newNbEnfant", montantError);
                    container.put("newPrixHome", montantError);
                    container.put("newDonation", montantError);
                    container.put("newMontant", montantError);
                    totalWihtOutErrorNewPca = totalWihtOutErrorNewPca.add(new BigDecimal(montantError));
                }

                if ((simplePlanDeCalcul == null) && !entity.getSimpleEntite().getIsManual()) {
                    messageError = messageError + getMessage("pegasus.process.adaptation.calculerDePuisApplication");
                }

                container.put("infoError", messageError);

                container.put("oldRenteAvsAi", entity.getProperties().get(PCProcessAdapationEnum.OLD_RENTE_AVS_AI));
                container.put("oldNbEnfant", entity.getProperties().get(PCProcessAdapationEnum.OLD_NB_ENFANT));
                container.put("oldPrixHome", entity.getProperties().get(PCProcessAdapationEnum.OLD_PRIX_HOME));
                container.put("oldDonation", entity.getProperties().get(PCProcessAdapationEnum.OLD_DONATION));
                container.put("oldMontant", entity.getProperties().get(PCProcessAdapationEnum.OLD_PC));

                if (entity.getProperties().containsKey(PCProcessAdapationEnum.OLD_ETAT_PC)) {
                    if (IPCValeursPlanCalcul.STATUS_REFUS.equals(entity.getProperties().get(
                            PCProcessAdapationEnum.OLD_ETAT_PC))) {
                        csOldEtatPca = "R";
                        fleche = " -> ";
                    }
                }

                container.put("csEtatPlanCalPca", csOldEtatPca + fleche + csEtatPca);

                diff = getDiffPc(entity.getProperties().get(PCProcessAdapationEnum.OLD_PC),
                        entity.getProperties().get(PCProcessAdapationEnum.NEW_PC));
                container.put("diff", diff);

                totalOldPca = totalOldPca
                        .add(new BigDecimal(entity.getProperties().get(PCProcessAdapationEnum.OLD_PC)));

                totalWihtOutErrorOldPca = totalWihtOutErrorOldPca.add(new BigDecimal(entity.getProperties().get(
                        PCProcessAdapationEnum.OLD_PC)));
                totalWihtOutErrorEcart = totalWihtOutErrorEcart.add(new BigDecimal(diff));

                // si couple separe par la maladie -> une deuxieme ligne pour le conjoint
                if (!JadeStringUtil.isBlank(entity.getProperties().get(PCProcessAdapationEnum.DESCRIPTION_CONJOINT))) {
                    nbPca++;
                    String csEtatPcaConjoint = "";
                    String flecheC = "";
                    String csOldEtatPcaConjoint = "";

                    container.put("description", entity.getProperties()
                            .get(PCProcessAdapationEnum.DESCRIPTION_CONJOINT));
                    container.put("manual", entity.getSimpleEntite().getIsManual().toString());

                    // On regarde si on a bien fait le calcule
                    if ((simplePlanDeCalcul != null) && hasValues) {
                        container.put("csEtat", entity.getSimpleEntite().getCsEtat().toLabel());
                        container.put("newRenteAVsAI",
                                entity.getProperties().get(PCProcessAdapationEnum.NEW_RENTE_AVS_AI_CONJOINT));
                        container.put("newNbEnfant", entity.getProperties().get(PCProcessAdapationEnum.NEW_NB_ENFANT));
                        container.put("newPrixHome",
                                entity.getProperties().get(PCProcessAdapationEnum.NEW_PRIX_HOME_CONJOINT));
                        container.put("newDonation",
                                entity.getProperties().get(PCProcessAdapationEnum.NEW_DONATION_CONJOINT));
                        container.put("newMontant", entity.getProperties().get(PCProcessAdapationEnum.NEW_PC_CONJOINT));
                        if (entity.getProperties().containsKey(PCProcessAdapationEnum.NEW_ETAT_PC_CONJOINT)) {
                            if (IPCValeursPlanCalcul.STATUS_REFUS.equals(entity.getProperties().get(
                                    PCProcessAdapationEnum.NEW_ETAT_PC_CONJOINT))) {
                                csEtatPcaConjoint = "R";
                                flecheC = " -> ";
                            }
                        }

                        totalWihtOutErrorNewPca = totalWihtOutErrorNewPca.add(new BigDecimal(entity.getProperties()
                                .get(PCProcessAdapationEnum.NEW_PC_CONJOINT)));
                    } else {
                        container.put("csEtat", getMessage("pegasus.process.adaptation.etat.aCalculer"));
                        container.put("newRenteAVsAI", montantError);
                        container.put("newNbEnfant", montantError);
                        container.put("newPrixHome", montantError);
                        container.put("newDonation", montantError);
                        container.put("newMontant", montantError);
                        totalWihtOutErrorNewPca = totalWihtOutErrorNewPca.add(new BigDecimal(montantError));
                    }

                    if ((simplePlanDeCalcul == null) && !entity.getSimpleEntite().getIsManual()) {
                        messageError = messageError
                                + getMessage("pegasus.process.adaptation.calculerDePuisApplication");
                    }

                    container.put("infoError", messageError);

                    container.put("oldRenteAvsAi",
                            entity.getProperties().get(PCProcessAdapationEnum.OLD_RENTE_AVS_AI_CONJOINT));
                    container.put("oldNbEnfant", entity.getProperties().get(PCProcessAdapationEnum.OLD_NB_ENFANT));
                    container.put("oldPrixHome",
                            entity.getProperties().get(PCProcessAdapationEnum.OLD_PRIX_HOME_CONJOINT));
                    container.put("oldDonation",
                            entity.getProperties().get(PCProcessAdapationEnum.OLD_DONATION_CONJOINT));
                    container.put("oldMontant", entity.getProperties().get(PCProcessAdapationEnum.OLD_PC_CONJOINT));

                    if (entity.getProperties().containsKey(PCProcessAdapationEnum.OLD_ETAT_PC_CONJOINT)) {
                        if (IPCValeursPlanCalcul.STATUS_REFUS.equals(entity.getProperties().get(
                                PCProcessAdapationEnum.OLD_ETAT_PC_CONJOINT))) {
                            csOldEtatPcaConjoint = "R";
                            flecheC = " -> ";
                        }
                    }

                    container.put("csEtatPlanCalPca", csOldEtatPcaConjoint + flecheC + csEtatPcaConjoint);
                    totalOldPca = totalOldPca.add(new BigDecimal(entity.getProperties().get(
                            PCProcessAdapationEnum.OLD_PC_CONJOINT)));

                    diff = getDiffPc(entity.getProperties().get(PCProcessAdapationEnum.OLD_PC_CONJOINT), entity
                            .getProperties().get(PCProcessAdapationEnum.NEW_PC_CONJOINT));
                    container.put("diff", diff);

                    totalWihtOutErrorOldPca = totalWihtOutErrorOldPca.add(new BigDecimal(entity.getProperties().get(
                            PCProcessAdapationEnum.OLD_PC_CONJOINT)));
                    totalWihtOutErrorEcart = totalWihtOutErrorEcart.add(new BigDecimal(diff));
                }

            } else {
                nbPca++;
                nbPcaInerror = nbPcaInerror + 1;
                // pour les entites en erreur
                container.put("description", entity.getSimpleEntite().getDescription());
                container.put("csEtatPca", entity.getSimpleEntite().getCsEtat().toLabel());
                container.put("manual", entity.getSimpleEntite().getIsManual().toString());
                List<PCAccordeePlanCalcul> listePCA = mapDemandeWithPCaEnErreur
                        .get(entity.getSimpleEntite().getIdRef());
                String montantPCA = null;

                if (listePCA != null) {
                    if (listePCA.size() == 1) {
                        montantPCA = listePCA.get(0).getSimplePlanDeCalcul().getMontantPCMensuelle();
                    } else if (listePCA.size() == 0) {
                        montantPCA = "";
                    } else if (listePCA.size() == 2) {
                        montantPCA = new BigDecimal(listePCA.get(0).getSimplePlanDeCalcul().getMontantPCMensuelle())
                                .add(new BigDecimal(listePCA.get(1).getSimplePlanDeCalcul().getMontantPCMensuelle()))
                                .toString();
                        nbPca++;
                        nbPcaInerror = nbPcaInerror + 1;
                    }
                } else {
                    // On est obliger de mettre un numéro sinon excel dira que le fichier est corrompu
                    montantPCA = montantError;
                    messageError = "Unable to get the pca old " + messageError;
                }

                if (!JadeStringUtil.isEmpty(montantPCA)) {
                    totalPcaInError = totalPcaInError.add(new BigDecimal(montantPCA));
                    totalOldPca = totalOldPca.add(new BigDecimal(montantPCA));
                }

                String csEtatPca = null;

                container.put("infoError", messageError);

                // pas de resultats
                container.put("oldRenteAvsAi", "");
                container.put("newRenteAVsAI", "");

                container.put("oldNbEnfant", "");
                container.put("newNbEnfant", "");

                container.put("oldPrixHome", "");
                container.put("newPrixHome", "");

                container.put("oldDonation", "");
                container.put("newDonation", "");

                container.put("oldMontant", montantPCA);
                container.put("newMontant", "");

                container.put("diff", "");
                container.put("csEtat", entity.getSimpleEntite().getCsEtat().toLabel());
                container.put("csEtatPlanCalPca", csEtatPca);
                listePCA = null;
            }
        }

        // les valeurs globales au document
        container.put("toDay", JACalendar.todayJJsMMsAAAA());

        // les entetes de colonnes
        container.put("labelDescription", getMessage("globaz.jade.process.excel.description"));
        container.put("labeleEtat", getMessage("globaz.jade.process.excel.etat"));
        container.put("labelManual", getMessage("globaz.jade.process.excel.manual"));

        // BigDecimal totalNewPca = new BigDecimal(0);
        // BigDecimal totalEcart = new BigDecimal(0);

        container.put("TOTAL_oldMontant", new FWCurrency(totalOldPca.toString()).toStringFormat());
        container.put("TOTAL_newMontant", new FWCurrency(totalWihtOutErrorNewPca.toString()).toStringFormat());
        container.put("TOTAL_diff", new FWCurrency(totalWihtOutErrorEcart.toString()).toStringFormat());

        container.put("NB_TOTAL_PCA_IN_ERROR", nbPcaInerror.toString());
        container.put("NB_TOTAL_WITH_OUT_ERROR", String.valueOf((nbPca - nbPcaInerror)));
        container.put("NB_TOTAL_PCA", nbPca.toString());

        container.put("TOTAL_PCA_IN_ERROR", new FWCurrency(totalPcaInError.toString()).toStringFormat());
        container.put("TOTAL_WITH_OUT_ERROR_oldMontant",
                new FWCurrency(totalWihtOutErrorOldPca.toString()).toStringFormat());
        container.put("TOTAL_WITH_OUT_ERROR_newMontant",
                new FWCurrency(totalWihtOutErrorNewPca.toString()).toStringFormat());

        container.put("TOTAL_WITH_OUT_ERROR_diff", new FWCurrency(totalWihtOutErrorEcart.toString()).toStringFormat());

        return container;
    }

    /**
     * Permet de savoir si il faut faire une vérification de l'état du droit. Il n'y a pas besoin de faire de
     * vérification pour l' étape de validation et l'étatpe d'impression
     * 
     * @param step
     * @return
     */
    private boolean mustCheckDroitStat(ProcessStep step) {
        return !(ch.globaz.pegasus.process.adaptation.step2.PCProcessAdaptationStep.KEY_STEP.equals(step
                .getSimpleStep().getKeyStep()) || ch.globaz.pegasus.process.adaptation.imprimerDecisions.PCProcessAdaptationStep.KEY_STEP
                .equals(step.getSimpleStep().getKeyStep()));
    }

    private List<EntityLogsProperties<PCProcessAdapationEnum>> searchEntity() {
        List<EntityLogsProperties<PCProcessAdapationEnum>> list = null;
        try {

            // list = JadeProcessServiceLocator.getJadeProcessEntityService().searchEntityWithProperties(
            // this.idExecutionProcess, 0, JadeAbstractSearchModel.SIZE_NOLIMIT);

            list = JadeProcessServiceLocator.getJadeProcessEntityService().searchEntityLogsAndProperties(
                    idExecutionProcess);

        } catch (EntiteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JadeApplicationServiceNotAvailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JadePersistenceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // catch (JadeProcessException e) {
        catch (JadeProcessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        return list;

    }

    public void setIdExecutionProcess(String idExecutionProcess) {
        this.idExecutionProcess = idExecutionProcess;
    }
}
