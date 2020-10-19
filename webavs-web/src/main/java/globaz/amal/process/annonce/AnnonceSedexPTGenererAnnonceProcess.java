package globaz.amal.process.annonce;

import ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;
import ch.globaz.amal.business.exceptions.models.annoncesedexco.AnnonceSedexCOException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedexSearch;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.business.models.famille.FamilleContribuableView;
import ch.globaz.amal.business.models.famille.FamilleContribuableViewSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.business.services.models.annoncesedex.ComplexAnnonceSedexService;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexRP.AnnoncesRPServiceImpl;
import com.google.common.collect.Lists;
import globaz.amal.process.AMALabstractProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.jade.smtp.JadeSmtpClient;
import org.sadun.util.test.C;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Process permettant le lancement de la génération des annonces de prime tarifaire
 */
public class AnnonceSedexPTGenererAnnonceProcess extends AMALabstractProcess {
    private String email = null;
    private String annee = null;
    public String dateReductionPrimeDe = null;
    public String dateReductionPrimeA = null;
    private Boolean isSimulation = Boolean.TRUE;;

    @Override
    public String getDescription() {
        if(this.isSimulation) {
            return "Amal : SIMULATION : génération des annonces PT par lot !";
        }
        return "Amal : génération des annonces PT par lot !";
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    protected void process() {
        List<String> listIdDetailFamille = new ArrayList<>();
        List<SimpleAnnonceSedex> listAnnoncesCreees = new ArrayList<>();
        try {
            // 1. Recherche de toutes les personnes
            listIdDetailFamille = _findAllFamilleContribuable(this.annee);

            // 2. Pour chaque cas, on génére les annonces potentielles
            this.getProgressHelper().setMax(listIdDetailFamille.size());
            for (String idDetailFamille : listIdDetailFamille) {
                listAnnoncesCreees.addAll(genererAnnonceDemandePT(idDetailFamille));;
                this.getProgressHelper().setCurrent(this.getProgressHelper().getCurrent() + 1);
            }

            // 3. Envoi du mail de confirmation
            createMail(listAnnoncesCreees);

            // Si on est en mode de simulation, on rollback
            if(this.isSimulation) {
                JadeThread.rollbackSession();
            }

        } catch (Exception e) {
            JadeLogger.error(this, new Exception("Erreur lors de la génération des annonces PT par lot\"", e));
            JadeThread.logError(this.getClass().getName() + ".process()",
                    "Erreur lors de la génération des annonces PT par lot");
            createMailError(listAnnoncesCreees, e);
        }
    }

    private List<SimpleAnnonceSedex> genererAnnonceDemandePT(String idDetailFamille) throws AnnonceSedexException {
        try {
            return AmalImplServiceLocator.getAnnoncesRPService().genererAnnonceDemandePT(idDetailFamille, dateReductionPrimeDe, dateReductionPrimeA, this.isSimulation);
        } catch (DetailFamilleException |JadeApplicationServiceNotAvailableException | JadePersistenceException | AnnonceSedexException e) {
            throw new AnnonceSedexException("Erreur dans la génération des annonces PT pour idDetailFamille : "+idDetailFamille, e);
        }
    }

    private List<String> _findAllFamilleContribuable(String annee)
            throws JadeApplicationException, JadePersistenceException {
        FamilleContribuableViewSearch simpleAnnonceSedexSearch = new FamilleContribuableViewSearch();
        simpleAnnonceSedexSearch.setForAnneeHistorique(annee);
        simpleAnnonceSedexSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        simpleAnnonceSedexSearch = AmalImplServiceLocator.getFamilleContribuableService()
                .search(simpleAnnonceSedexSearch);


        return Arrays.stream(simpleAnnonceSedexSearch.getSearchResults()).map( m -> ((FamilleContribuableView)m).getDetailFamilleId()).collect(Collectors.toList());
    }

    private void createMailError(List<SimpleAnnonceSedex> listAnnoncesSedex, Exception e) {
        // 1. Préparation du message
        String message = preparerCorpsMail(listAnnoncesSedex);
        message += "\n\n"+"\n***** INFORMATIONS POUR GLOBAZ *****\n"+getStackTrace(e);

        // 2. Envoi
        sendMail("Erreur lors de la génération des annonces PT par lot", message, null);
    }

    private void createMail(List<SimpleAnnonceSedex> listAnnoncesSedex) {
        // 1. Préparation du message
        String message = preparerCorpsMail(listAnnoncesSedex);

        // 2. Liste des cas générés
        String[] filename = {createList(listAnnoncesSedex)};

        // 3. Envoi
        sendMail(getDescription(), message, filename);
    }

    private String createList(List<SimpleAnnonceSedex> listAnnoncesSedex) {

        List<StringBuffer> listRecords = new ArrayList<StringBuffer>();
        for (JadeAbstractModel model : listAnnoncesSedex) {
            SimpleAnnonceSedex annonceSedex = (SimpleAnnonceSedex) model;

            ComplexAnnonceSedexSearch searchModel = new ComplexAnnonceSedexSearch();
            searchModel.setForSDXIdAnnonceSedex(annonceSedex.getIdAnnonceSedex());
            searchModel.setForSDXIdDetailFamille(annonceSedex.getIdDetailFamille());

            try {
                searchModel = AmalServiceLocator.getComplexAnnonceSedexService().search(searchModel);
            } catch (JadePersistenceException | AnnonceSedexException | JadeApplicationServiceNotAvailableException e) {
                e.printStackTrace();
            }

            BSession currentSession = BSessionUtil.getSessionFromThreadContext();
            ComplexAnnonceSedex complexModel = (ComplexAnnonceSedex) Arrays.stream(searchModel.getSearchResults()).findFirst().orElseThrow(RuntimeException::new);

            listRecords.add(AnnoncesRPServiceImpl.createLineForExport(currentSession, complexModel));
        }

        // CSV Line Header
        String lineHeader = AnnoncesRPServiceImpl.createHeaderForExportAnnonceRP();

        String fileName = "generationAnnocnePT.csv";
        if(this.isSimulation) {
            fileName = "simulationGenerationAnnocnePT.csv";
        }

        return AnnoncesRPServiceImpl._writeFile(listRecords, lineHeader, fileName);
    }

    private String preparerCorpsMail(List<SimpleAnnonceSedex> listAnnoncesSedex) {
        String message = "Nombre de cas : " + listAnnoncesSedex.size();
        message += "\nAnnée : " + this.annee;
        message += "\nDate réduction de prime de : " + this.dateReductionPrimeDe;
        message += "\nDate réduction de prime à : " + this.dateReductionPrimeA;
        return message;
    }

    private void sendMail(String description, String message, String[] filenames) {
        try {
            JadeSmtpClient.getInstance().sendMail(getEmail(), description, message, filenames);
        } catch (Exception e) {
            JadeThread.logError(
                    "AnnonceSedexPTGenererAnnonceProcess",
                    "Erreur lors de l'envoi du mail du processus de génération des annonces PT par lot : "
                            + e.getMessage());
        }
    }

    public String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }
    public boolean getIsSimulation() {
        return isSimulation;
    }
    public void setIsSimulation(boolean isSimulation) {
        this.isSimulation = isSimulation;
    }

}
