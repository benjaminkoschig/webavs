package globaz.orion.process;

import ch.globaz.orion.business.domaine.pucs.EtatPucsFile;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.db.EBPucsFileEntity;
import ch.globaz.orion.db.EBPucsFileManager;
import ch.globaz.orion.service.EBPucsFileService;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.orion.utils.EBDanUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * CRON permettant d'importer les déclarations de salaires dans DRACO
 */
@Slf4j
public class EBImportDeclarationsSalairesDraco extends BProcess {

    public static final String CODE_FAILLITE_FPV = "19803042";

    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            LOG.info("Lancement du process d'importation des déclarations de salaires.");
            this.setSendCompletionMail(false);
            this.setSendMailOnError(false);

            initBsession();
            importDeclarationsSalaires();

        } catch (Exception e) {
            sendErrorMail(e.getMessage());
        } finally {
            closeBsession();
        }

        return true;
    }

    private void importDeclarationsSalaires() throws Exception {
        EBPucsFileManager manager = new EBPucsFileManager();
        manager.setStatut(EtatPucsFile.A_TRAITER.getValue());
        List<EBPucsFileEntity> filterList = manager.search().stream()
                .filter(this::validate)
                .collect(Collectors.toList());
        List<PucsFile> pucsFiles = EBPucsFileService.entitiesToPucsFile(filterList);
        importFile(pucsFiles);
    }

    private boolean validate(EBPucsFileEntity ebPucsFileEntity) {
        return !ebPucsFileEntity.isAfSeul()
                && validateAffilie(ebPucsFileEntity);
    }

    private boolean validateAffilie(EBPucsFileEntity ebPucsFileEntity) {
        AFAffiliation aff;
        try {
            aff = EBDanUtils.findAffilie(getSession(), ebPucsFileEntity.getNumeroAffilie(), "31.12."
                    + ebPucsFileEntity.getAnneeDeclaration(), "01.01." + ebPucsFileEntity.getAnneeDeclaration());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Objects.nonNull(aff)
                && !(CodeSystem.MOTIF_FIN_FAILLITE.equals(aff.getMotifFin()) || CODE_FAILLITE_FPV.equals(aff.getMotifFin()));
    }

    private void importFile(List<PucsFile> pucsFiles) throws Exception {
        EBTreatPucsFiles process = new EBTreatPucsFiles();
        process.setSendCompletionMail(true);
        process.setEmailAdress(getEMailAddress());
        process.setIsBatch(true);
        process.setMode("miseajour");
        process.setIdMiseEnGed(pucsFiles.stream().map(PucsFile::getIdDb).collect(Collectors.toList()));
        process.setIdValidationDeLaDs(pucsFiles.stream().map(PucsFile::getIdDb).collect(Collectors.toList()));
        process.setPucsEntrys(pucsFiles);
        process.setSession(getSession());
        process.setPucsToMerge(new HashMap<>());
        process.setName(getSession().getLabel("DESCRIPTION_PROCESSUS_TRAITEMENT_PUCS"));
        BProcessLauncher.start(process, false);
    }

    private void sendErrorMail(String errors) throws Exception {
        JadeSmtpClient.getInstance().sendMail(getEMailAddress(), getEMailObject(), errors, null);
    }

    private void initBsession() throws Exception {
        LOG.info("Initialisation de la session");
        BSessionUtil.initContext(getSession(), this);
    }

    private void closeBsession() {
        BSessionUtil.stopUsingContext(this);
    }

    @Override
    protected void _executeCleanUp() {
        //Nothing to do
    }

    @Override
    protected String getEMailObject() {
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }
}
