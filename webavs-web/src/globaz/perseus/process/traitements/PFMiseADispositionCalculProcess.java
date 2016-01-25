/**
 * Processus de mise a disposition des calculs PC Familles dans une table. Ce processus ne sera executé qu'une seule
 * fois.
 */
package globaz.perseus.process.traitements;

import globaz.globall.db.BSessionUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.perseus.process.PFAbstractJob;
import java.util.Iterator;
import java.util.Set;
import ch.globaz.perseus.business.calcul.OutputData;
import ch.globaz.perseus.business.exceptions.models.pcfaccordee.PCFAccordeeException;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordeeForDetailsCalcul;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordeeForDetailsCalculSearchModel;
import ch.globaz.perseus.business.models.pcfaccordee.SimpleDetailsCalcul;
import ch.globaz.perseus.business.models.pcfaccordee.SimpleDetailsCalculSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

public class PFMiseADispositionCalculProcess extends PFAbstractJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adresseMail = null;

    private void createDetailsCalcul(OutputData dataType, PCFAccordeeForDetailsCalcul pcfAccordee)
            throws PCFAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {

        // SimpleDetailsCalculSearchModel searchDetailsCalcul = this.lineExistForDataType(pcfAccordee
        // .getSimplePCFAccordee().getIdPCFAccordee(), dataType);
        // if (searchDetailsCalcul.getSize() > 0) {
        // this.updateLineForDataType(pcfAccordee, dataType, searchDetailsCalcul);
        // } else {
        createLineForDataType(pcfAccordee, dataType);
        // }
    }

    private void createLineForDataType(PCFAccordeeForDetailsCalcul pcfAccordee, OutputData dataType)
            throws PCFAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleDetailsCalcul simpleDetailsCalcul = new SimpleDetailsCalcul();
        simpleDetailsCalcul.setIdPCFAccordee(pcfAccordee.getId());
        simpleDetailsCalcul.setTypeData(dataType.getCodeChamp());
        simpleDetailsCalcul.setMontant(pcfAccordee.getCalcul().getDonneeString(dataType));
        simpleDetailsCalcul = PerseusImplServiceLocator.getSimpleDetailsCalculService().create(simpleDetailsCalcul);

    }

    public String getAdresseMail() {
        return adresseMail;
    }

    @Override
    public String getDescription() {
        return getSession().getLabel("JSP_PF_TRAITEMENT_MISE_A_DISPOSITION_CALCUL_DESCRIPTION");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getName()
     */
    @Override
    public String getName() {
        return getSession().getLabel("JSP_PF_TRAITEMENT_MISE_A_DISPOSITION_CALCUL_DESCRIPTION");
    }

    // private SimpleDetailsCalculSearchModel lineExistForDataType(String idPCFAccordee, OutputData dataType)
    // throws PCFAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
    // SimpleDetailsCalculSearchModel searchModel = new SimpleDetailsCalculSearchModel();
    // searchModel.setForIdPCFAccordee(idPCFAccordee);
    // searchModel.setForTypeData(dataType.getCodeChamp());
    // return PerseusImplServiceLocator.getSimpleDetailsCalculService().search(searchModel);
    // }

    private PCFAccordeeForDetailsCalculSearchModel loadPCFAccordee()
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, PCFAccordeeException {
        PCFAccordeeForDetailsCalculSearchModel pcfAccSearch = new PCFAccordeeForDetailsCalculSearchModel();
        pcfAccSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        return PerseusServiceLocator.getPCFAccordeeService().searchWithBlobs(pcfAccSearch);
    }

    private String messageDonnantNBCalculEtNBCalculMisADisposition(int NbPcfAccordee) throws PCFAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        StringBuilder message = new StringBuilder();
        message.append(BSessionUtil.getSessionFromThreadContext().getLabel(
                "TRAITEMENT_MISE_A_DISPOSITION_CALCUL_NOMBRE"));
        message.append(NbPcfAccordee);
        message.append("\n");
        message.append(BSessionUtil.getSessionFromThreadContext().getLabel(
                "TRAITEMENT_MISE_A_DISPOSITION_CALCUL_NOMBRE_A_DISPOSITION"));
        message.append(nombreCalculDansLaTable());
        return message.toString();

    }

    private void miseADispositionDuCalcul(PCFAccordeeForDetailsCalcul pcfAccordee) throws PCFAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        Set<OutputData> keySet = pcfAccordee.getCalcul().getCalcul().keySet();

        for (Iterator<OutputData> iterator = keySet.iterator(); iterator.hasNext();) {
            OutputData dataType = iterator.next();
            createDetailsCalcul(dataType, pcfAccordee);
        }
    }

    private int nombreCalculDansLaTable() throws PCFAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        SimpleDetailsCalculSearchModel searchDetailsCalcul = new SimpleDetailsCalculSearchModel();
        searchDetailsCalcul.setForTypeData(OutputData.DEPENSES_BESOINS_VITAUX.getCodeChamp());
        return PerseusImplServiceLocator.getSimpleDetailsCalculService().count(searchDetailsCalcul);
    }

    /*
     * Processus de mise à disposition des calculs PC Familles
     * 
     * @see globaz.perseus.process.PFAbstractJob#process()
     */
    @Override
    protected void process() throws Exception {
        int nbCalcul = 0;

        try {
            PCFAccordeeForDetailsCalculSearchModel pcfSearch = loadPCFAccordee();
            nbCalcul = pcfSearch.getSize();

            for (JadeAbstractModel model : pcfSearch.getSearchResults()) {
                PCFAccordeeForDetailsCalcul pcfAcc = (PCFAccordeeForDetailsCalcul) model;

                miseADispositionDuCalcul(pcfAcc);
                JadeThread.commitSession();

            }

        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(),
                    "Erreur technique grave : " + e.toString() + " : " + e.getMessage());
            e.printStackTrace();
        }

        logMessage(messageDonnantNBCalculEtNBCalculMisADisposition(nbCalcul));
        logError(getSession().getLabel("PROCESS_ERREUR_MESSAGE"));
        this.sendCompletionMail(getAdresseMail());

    }

    public void setAdresseMail(String adresseMail) {
        this.adresseMail = adresseMail;
    }

    // private void updateLineForDataType(PCFAccordeeForDetailsCalcul pcfAccordee, OutputData dataType,
    // SimpleDetailsCalculSearchModel searchModel) throws PCFAccordeeException,
    // JadeApplicationServiceNotAvailableException, JadePersistenceException {
    // SimpleDetailsCalcul simpleDetailsCalcul = (SimpleDetailsCalcul) searchModel.getSearchResults()[0];
    // simpleDetailsCalcul.setIdPCFAccordee(pcfAccordee.getId());
    // simpleDetailsCalcul.setTypeData(dataType.getCodeChamp());
    // simpleDetailsCalcul.setMontant(pcfAccordee.getCalcul().getDonneeString(dataType));
    // simpleDetailsCalcul = PerseusImplServiceLocator.getSimpleDetailsCalculService().update(simpleDetailsCalcul);
    // }
}
