package ch.globaz.pegasus.process.adaptation;

import globaz.jade.client.util.JadeListUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.pcaccordee.PCAIdMembreFamilleRetenu;
import ch.globaz.pegasus.business.models.pcaccordee.PCAIdMembreFamilleRetenuSearch;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pegasus.businessimpl.utils.calcul.IPeriodePCAccordee;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.DonneesHorsDroitsProvider;

public abstract class AdaptationEntityHandlerAbstract extends PCProcessDroitUpdateAbsract {

    private DonneesHorsDroitsProvider containerGlobal = null;

    private void addError(Exception e) throws JadeNoBusinessLogSessionError {
        this.addError(e, null);
    }

    private void addError(Exception e, String[] param) throws JadeNoBusinessLogSessionError {
        e.printStackTrace();
        JadeThread.logError("", (e.getMessage() != null) ? e.getMessage() : e.toString(), param);
        if (e.getCause() != null) {
            String cause = "<br />" + e.getCause().toString();
            JadeThread.logError("", cause);
        }
    }

    public DonneesHorsDroitsProvider getContainerGlobal() {
        return containerGlobal;
    }

    public abstract String getCsModification();

    public abstract boolean hasRetroactif();

    @Override
    public void run() {
        PCAIdMembreFamilleRetenuSearch pcAIdMembreFamilleRetenuSearch = null;

        IPeriodePCAccordee periodePCAccordee = null;
        try {

            fillDroitToUpdate();

            if (!JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
                Collection<String> collectionIdMembreFamille = new LinkedList<String>();

                List<String> listIdDroitMembreFamille = new LinkedList<String>();

                pcAIdMembreFamilleRetenuSearch = PCAdaptationUtils
                        .findIdMembreFamilleWithPlanPlanCalculRetenu(currentDroit.getSimpleVersionDroit()
                                .getIdVersionDroit());

                List<PCAIdMembreFamilleRetenu> listepca = PersistenceUtil.typeSearch(pcAIdMembreFamilleRetenuSearch,
                        pcAIdMembreFamilleRetenuSearch.whichModelClass());

                Map<String, List<PCAIdMembreFamilleRetenu>> mapEtatPc = JadeListUtil.groupBy(listepca,
                        new JadeListUtil.Key<PCAIdMembreFamilleRetenu>() {
                            @Override
                            public String exec(PCAIdMembreFamilleRetenu e) {
                                return e.getEtatPC();
                            }
                        });

                if ((mapEtatPc.size() == 1) && mapEtatPc.containsKey(IPCValeursPlanCalcul.STATUS_REFUS)) {
                    JadeThread.logError("", "pegasus.process.adaptaion.etatPcEnRefus");
                } else {
                    PCAdaptationUtils.createListMembreFamille(pcAIdMembreFamilleRetenuSearch,
                            collectionIdMembreFamille, listIdDroitMembreFamille);

                    runFunction(pcAIdMembreFamilleRetenuSearch, droitACalculer, listIdDroitMembreFamille);

                    // calcule le droit
                    periodePCAccordee = PegasusImplServiceLocator.getCalculDroitService().calculDroitPourComparaison(
                            droitACalculer, collectionIdMembreFamille, containerGlobal, true, hasRetroactif());

                    dataToSave = PCAdaptationUtils.saveData(pcAIdMembreFamilleRetenuSearch, periodePCAccordee,
                            currentDroit, properties.get(PCProcessAdapationEnum.DATE_ADAPTATION),
                            collectionIdMembreFamille, containerGlobal);
                    dataToSave.put(PCProcessAdapationEnum.HAS_DELETE_VERSION_DROIT, hasDeleteVersionDroit.toString());
                }

            }
        } catch (CalculException e) {
            this.addError(e, e.getParameters());
        } catch (Exception e) {
            this.addError(e);
        }
    }

    public abstract void runFunction(PCAIdMembreFamilleRetenuSearch pcAIdMembreFamilleRetenuSearch,
            Droit droitACalculer, List<String> listeMb) throws Exception;

    public void setContainerGlobal(DonneesHorsDroitsProvider containerGlobal) {
        this.containerGlobal = containerGlobal;
    }

}
