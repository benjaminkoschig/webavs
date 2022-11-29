package globaz.orion.process;

import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.domaine.pucs.EtatPucsFile;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.db.EBPucsFileEntity;
import ch.globaz.orion.db.EBPucsFileManager;
import ch.globaz.orion.service.EBPucsFileService;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.services.AFAffiliationServices;
import globaz.naos.translation.CodeSystem;
import globaz.orion.utils.EBDanUtils;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * CRON permettant d'importer les déclarations de salaires dans DRACO
 */
@Slf4j
public class EBImportDeclarationsSalairesDraco extends BProcess {

    public static final String IMPORT_MOTIF_DE_FIN = "import.motifdefin";
    public static final String IMPORT_PERSONNALITE_JURIDIQUE = "import.personnalitejuridique";
    public static final String IMPORT_DECLARATION_SALAIRE = "import.declarationsalaire";
    public static final String ORION_PUCS_VALIDATION_DECLARATION_BATCH = "orion.pucs.validation.declaration.validationDs.batch";

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
        List<EBPucsFileEntity> ebuList = manager.search();

        // Fichier swissDec etat A_VALIDER
        manager.setStatut(EtatPucsFile.A_VALIDE.getValue());
        manager.setForProvenance(DeclarationSalaireProvenance.SWISS_DEC);
        ebuList.addAll(manager.search());

        List<EBPucsFileEntity> filterList = ebuList.stream()
                .filter(this::validate)
                .collect(Collectors.toList());
        List<PucsFile> pucsFiles = EBPucsFileService.entitiesToPucsFile(filterList);
        importFile(pucsFiles);
    }

    private boolean validate(EBPucsFileEntity ebPucsFileEntity) {
        return !ebPucsFileEntity.isAfSeul()
                && !ebPucsFileEntity.isForTest()
                && validateAffilie(ebPucsFileEntity)
                && validateParticularite(ebPucsFileEntity)
                && validateMasseNonZero(ebPucsFileEntity);
    }

    private boolean validateAffilie(EBPucsFileEntity ebPucsFileEntity) {
        List<String> motifsDeFins;
        List<String> personnalitesJuridiques;
        List<String> declarationSalaire;
        AFAffiliation aff;
        try {
            motifsDeFins = getListPropriete(IMPORT_MOTIF_DE_FIN);
            personnalitesJuridiques = getListPropriete(IMPORT_PERSONNALITE_JURIDIQUE);
            declarationSalaire = getListPropriete(IMPORT_DECLARATION_SALAIRE);
            aff = EBDanUtils.findAffilie(getSession(), ebPucsFileEntity.getNumeroAffilie(), "31.12."
                    + ebPucsFileEntity.getAnneeDeclaration(), "01.01." + ebPucsFileEntity.getAnneeDeclaration());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return Objects.nonNull(aff)
                && (JadeStringUtil.isBlankOrZero(aff.getMotifFin())
                    || !motifsDeFins.contains(aff.getMotifFin()))
                && (JadeStringUtil.isBlankOrZero(aff.getPersonnaliteJuridique())
                    || !personnalitesJuridiques.contains(aff.getPersonnaliteJuridique()))
                && (JadeStringUtil.isBlankOrZero(aff.getDeclarationSalaire())
                    || !declarationSalaire.contains(aff.getDeclarationSalaire()));
    }

    private List<String> getListPropriete(String proprieteName) throws Exception {
        String properties = getSession().getApplication().getProperty(proprieteName, "");
        return Arrays.stream(properties.split("[,;]+"))
                .map(String::trim).collect(Collectors.toList());
    }

    private boolean validateMasseNonZero(EBPucsFileEntity ebPucsFileEntity) {
        return BigDecimal.ZERO.compareTo(ebPucsFileEntity.getTotalControle()) != 0;
    }

    private boolean validateParticularite(EBPucsFileEntity ebPucsFileEntity) {
        List<AFParticulariteAffiliation> listeParticularites = AFAffiliationServices.findListParticulariteAffiliation(ebPucsFileEntity.getIdAffiliation(),
                getSession());

        return listeParticularites.stream()
                .noneMatch(p -> CodeSystem.PARTIC_AFFILIE_FICHE_PARTIELLE.equals(p.getParticularite())
                    || CodeSystem.PARTIC_AFFILIE_CODE_BLOCAGE_DECFINAL.equals(p.getParticularite()));
    }

    private void importFile(List<PucsFile> pucsFiles) throws Exception {
        EBTreatPucsFiles process = new EBTreatPucsFiles();
        process.setSendCompletionMail(true);
        process.setEmailAdress(getEMailAddress());
        process.setIsBatch(true);
        process.setMode("miseajour");
        process.setIdMiseEnGed(pucsFiles.stream().map(PucsFile::getIdDb).collect(Collectors.toList()));
        if (isValidationDeclarationBatch()) {
             // active la validation des Ds après l'import batch
            process.setIdValidationDeLaDs(pucsFiles.stream().map(PucsFile::getIdDb).collect(Collectors.toList()));
        }
        process.setPucsEntrys(pucsFiles);
        process.setSession(getSession());
        process.setPucsToMerge(new HashMap<>());
        process.setName(getSession().getLabel("DESCRIPTION_PROCESSUS_TRAITEMENT_PUCS"));
        BProcessLauncher.start(process, false);
    }

    /**
     * Contrôle si la validation doit être lancée après la mise à jour des PUCS dans
     * la propriété orion.pucs.validation.declaration.validationDs.batch
     * utilisé lors du processus de mise à jour des PUCS.
     */
    public boolean isValidationDeclarationBatch() {
        return "true".equals(JadePropertiesService.getInstance().getProperty(EBImportDeclarationsSalairesDraco.ORION_PUCS_VALIDATION_DECLARATION_BATCH));
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
