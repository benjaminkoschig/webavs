package globaz.orion.helpers.pucs;

import ch.globaz.orion.business.domaine.pucs.DeclarationSalaire;
import ch.globaz.orion.business.domaine.pucs.Employee;
import ch.globaz.orion.business.domaine.pucs.SalaryAvs;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.business.models.pucs.PucsFileMerge;
import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.properties.JadePropertiesService;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.translation.CodeSystem;
import globaz.orion.utils.EBDanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Regroupe tous les contrôles additionels effectués lors de la mise à jour
 * ou de la validation des fichiers PUCS s'effectue au travers du batch
 */
public class EBPucsBatchController {

    BSession session = null;

    public static final String ORION_PUCS_VALIDATION_DECLARATION_ANNEE = "orion.pucs.validation.declaration.annee";

    public BSession getSession() {
        return session;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    /**
     * Contrôle qu'il n'y a pas plus de deux fichiers pucs dans mergedPucsFiles pour l’année en cours avec le même total de contrôle.
     * utilisé lors du processus de mise à jour des PUCS.
     */
    public boolean contientDeclarationAvecAnneDeclarationEtTotalIdentique(PucsFile pucsFile, List<PucsFile> mergedPucsFiles) {
        int compteurDoublon = 0;
        for (PucsFile nextPucsFile : mergedPucsFiles) {
            if (Objects.equals(pucsFile.getAnneeDeclaration(), nextPucsFile.getAnneeDeclaration()) && Objects.equals(pucsFile.getNumeroAffilie(), nextPucsFile.getNumeroAffilie()) && Objects.equals(pucsFile.getTotalControle(), nextPucsFile.getTotalControle())) {
                compteurDoublon ++;
            }
        }
        if (compteurDoublon > 2) {
            return true;
        }

        return false;
    }

    /**
     * Contrôle qu'il n'y a pas plus de deux fichiers pucs dans mergedPucsFiles pour l’année en cours avec total de contrôle différents.
     * utilisé lors du processus de mise à jour des PUCS.
     */
    public boolean contientDeclarationAvecAnneDeclarationEtTotalDifferent(PucsFileMerge pucsFilemerge, List<PucsFileMerge> pucsFiles) {
        PucsFile pucsFile = pucsFilemerge.getPucsFile();
        for (PucsFileMerge nextMergePucsFile : pucsFiles) {
            PucsFile nextPucsFile = nextMergePucsFile.getPucsFile();
            if (!nextPucsFile.getIdDb().equals(pucsFile.getIdDb())
                    && Objects.equals(pucsFile.getAnneeDeclaration(), nextPucsFile.getAnneeDeclaration())
                    && Objects.equals(pucsFile.getNumeroAffilie(), nextPucsFile.getNumeroAffilie())
                    && !Objects.equals(pucsFile.getTotalControle(), nextPucsFile.getTotalControle())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Contrôle si il n'y a pas de déclaration de salaire dans le module Draco pour l’année concernée.
     * utilisé lors du processus de mise à jour des PUCS.
     */
    public boolean contientDeclarationSalaireDansAnneeConcernee(DeclarationSalaire ds, AFAffiliation aff) throws Exception {
        DSDeclarationListViewBean manager = new DSDeclarationListViewBean();
        manager.setForAnnee(Integer.toString(ds.getAnnee()));
        manager.setForAffiliationId(aff.getAffiliationId());
        manager.setSession(getSession());
        manager.find(BManager.SIZE_NOLIMIT);

        if (manager.getSize() > 0) {
            return true;
        }

        return false;
    }

    /**
     * Contrôle si le numéro d’affilié n'existe pas sur WebAVS
     * utilisé lors du processus de mise à jour des PUCS.
     */
    public boolean contientNumeroAffilieNonExistant(PucsFile pucsFile) throws Exception {
        AFAffiliation aff = EBDanUtils.findAffilie(getSession(), pucsFile.getNumeroAffilie(), "31.12."
                + pucsFile.getAnneeDeclaration(), "01.01." + pucsFile.getAnneeDeclaration());
        if (aff == null) {
            return true;
        }

        return false;
    }

    /**
     * Contrôle si des collaborateurs sont dans plusieurs cantons alors il doit s’agir
     * d’un fichier qui commencent par MIX
     * utilisé lors du processus de mise à jour des PUCS.
     */
    public boolean contientCollaborateursDansPlusieursCantonsEtPasMix(PucsFile pucsFile, AFAffiliation aff) {
        if (!pucsFile.getFilename().startsWith("MIX")) {
            List<AFCotisation> cotisations = new ArrayList<>();
            AFCotisationManager manager = new AFCotisationManager();
            manager.setSession(getSession());
            manager.setForAffiliationId(aff.getAffiliationId());
            manager.setForAnneeActive(pucsFile.getAnneeDeclaration());
            manager.setForNotMotifFin(CodeSystem.MOTIF_FIN_EXCEPTION);
            try {
                manager.find(BManager.SIZE_NOLIMIT);
                if (manager.size() > 0) {
                    for (int i = 0; i < manager.size(); i++) {
                        cotisations.add((AFCotisation) manager.getEntity(i));
                    }
                }
            } catch (Exception e) {
                JadeLogger.info(e, "Une erreur s'est produite lors de la recherche des cotisations de l'affiliation." + e.getMessage());
            }

            Map<String, Long> cotisationsParCantonAssurance = cotisations.stream()
                    .filter(c -> !JadeStringUtil.isBlankOrZero(c.getAssurance().getAssuranceCanton()))
                    .collect(Collectors.groupingBy(c -> c.getAssurance().getAssuranceCanton(), Collectors.counting()));

            if (cotisationsParCantonAssurance.size() > 1) {
                return true;
            }
        }

        return false;
    }

    /**
     * Contrôle si le fichier à un un salaire négatif déclaré
     * utilisé lors du processus de mise à jour des PUCS.
     */
    public boolean contientSalaireNegatif(List<Employee> employees) {
        for (Employee employee : employees) {
            for (SalaryAvs salaryAvs : employee.getSalariesAvs()) {
                if (salaryAvs.getMontantAvs().isNegative()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Contrôle si l'année de la déclaration correspond à
     * la propriété orion.pucs.validation.declaration.annee
     * utilisé lors du processus de mise à jour des PUCS.
     */
    public boolean anneeDeclarationPasEgalPropriete(Integer anneeDeclaration) {
        try {
            Integer anneePropriete = Integer.parseInt(JadePropertiesService.getInstance().getProperty(EBPucsBatchController.ORION_PUCS_VALIDATION_DECLARATION_ANNEE));
            if (anneeDeclaration != null && !anneeDeclaration.equals(anneePropriete)) {
                return true;
            }
        } catch (NumberFormatException e) {
            JadeLogger.error(e, "Une erreur s'est produite lors de la recherche de la propriété : " + EBPucsBatchController.ORION_PUCS_VALIDATION_DECLARATION_ANNEE + " " + e.getMessage());
            return true;
        }

        return false;
    }

    /**
     * Contrôle si le nom trouvé dans la table des fichier pucs EBPUCS_FILE.NOM_AFFILIE
     * correspond au nom du tiers de l'affiliation dans WebAvs
     * utilisé lors du processus de mise à jour des PUCS.
     */
    public boolean nomAffiliePucsFilePasEgalNomAffilieTiers(String nomAffiliePucsFile, String nomAffilieTiers) {
        if (nomAffiliePucsFile != null && nomAffilieTiers != null) {
            String nomAffiliePucsFileFormatted = nomAffiliePucsFile.replaceAll("[^a-zA-Z0-9]", "");
            String nomAffilieTiersFormatted = nomAffilieTiers.replaceAll("[^a-zA-Z0-9]", "");
            if (!nomAffiliePucsFileFormatted.equalsIgnoreCase(nomAffilieTiersFormatted)) {
                return true;
            }
        }

        return false;
    }
}
