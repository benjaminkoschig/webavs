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
 * Regroupe tous les contr�les additionels effectu�s lors de la mise � jour
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
     * Contr�le qu'il n'y a pas plus de deux fichiers pucs dans mergedPucsFiles pour l�ann�e en cours avec le m�me total de contr�le.
     * utilis� lors du processus de mise � jour des PUCS.
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
     * Contr�le qu'il n'y a pas plus de deux fichiers pucs dans mergedPucsFiles pour l�ann�e en cours avec total de contr�le diff�rents.
     * utilis� lors du processus de mise � jour des PUCS.
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
     * Contr�le si il n'y a pas de d�claration de salaire dans le module Draco pour l�ann�e concern�e.
     * utilis� lors du processus de mise � jour des PUCS.
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
     * Contr�le si le num�ro d�affili� n'existe pas sur WebAVS
     * utilis� lors du processus de mise � jour des PUCS.
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
     * Contr�le si des collaborateurs sont dans plusieurs cantons alors il doit s�agir
     * d�un fichier qui commencent par MIX
     * utilis� lors du processus de mise � jour des PUCS.
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
     * Contr�le si le fichier � un un salaire n�gatif d�clar�
     * utilis� lors du processus de mise � jour des PUCS.
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
     * Contr�le si l'ann�e de la d�claration correspond �
     * la propri�t� orion.pucs.validation.declaration.annee
     * utilis� lors du processus de mise � jour des PUCS.
     */
    public boolean anneeDeclarationPasEgalPropriete(Integer anneeDeclaration) {
        try {
            Integer anneePropriete = Integer.parseInt(JadePropertiesService.getInstance().getProperty(EBPucsBatchController.ORION_PUCS_VALIDATION_DECLARATION_ANNEE));
            if (anneeDeclaration != null && !anneeDeclaration.equals(anneePropriete)) {
                return true;
            }
        } catch (NumberFormatException e) {
            JadeLogger.error(e, "Une erreur s'est produite lors de la recherche de la propri�t� : " + EBPucsBatchController.ORION_PUCS_VALIDATION_DECLARATION_ANNEE + " " + e.getMessage());
            return true;
        }

        return false;
    }

    /**
     * Contr�le si le nom trouv� dans la table des fichier pucs EBPUCS_FILE.NOM_AFFILIE
     * correspond au nom du tiers de l'affiliation dans WebAvs
     * utilis� lors du processus de mise � jour des PUCS.
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
