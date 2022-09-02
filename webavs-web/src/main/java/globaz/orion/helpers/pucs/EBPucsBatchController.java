package globaz.orion.helpers.pucs;

import ch.globaz.orion.business.domaine.pucs.DeclarationSalaire;
import ch.globaz.orion.business.domaine.pucs.Employee;
import ch.globaz.orion.business.domaine.pucs.SalaryAvs;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.business.models.pucs.PucsFileMerge;
import ch.globaz.orion.ws.service.AFMassesForAffilie;
import ch.globaz.orion.ws.service.AppAffiliationService;
import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.process.DSProcessValidation;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.properties.JadePropertiesService;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.orion.utils.EBDanUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Regroupe tous les contr�les additionels effectu�s lors de la mise � jour
 * ou de la validation des fichiers PUCS s'effectue au travers du batch
 */
public class EBPucsBatchController {

    public static final String PROPERTY_ORION_PUCS_BATCH_VALIDATIONS_ACTIVER = "orion.pucs.batch.validations.activer";
    public static final String PROPERTY_ORION_PUCS_BATCH_VALIDATIONS_COTISATIONS = "orion.pucs.batch.validations.cotisations";

    BSession session = null;

    public BSession getSession() {
        return session;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    /**
     * Contr�le qu'il n'y a pas plus de un fichiers pucs pour l�ann�e en cours avec le m�me total de contr�le.
     * utilis� lors du processus de mise � jour des PUCS.
     */
    public boolean contientDeclarationAvecAnneDeclarationEtTotalIdentique(PucsFile pucsFile, List<PucsFileMerge> pucsFileMerges) {
        boolean alreadyFoundOne = false;
        for (PucsFileMerge pucsFileMerge : pucsFileMerges) {
            for (PucsFile nextPucsFile : pucsFileMerge.getPucsFileToMergded()) {
                if (Objects.equals(pucsFile.getAnneeDeclaration(), nextPucsFile.getAnneeDeclaration()) && Objects.equals(pucsFile.getNumeroAffilie(), nextPucsFile.getNumeroAffilie()) && Objects.equals(pucsFile.getTotalControle(), nextPucsFile.getTotalControle()) && pucsFile.getTypeDeclaration() == nextPucsFile.getTypeDeclaration()) {
                    if(alreadyFoundOne) {
                        return true;
                    }
                    alreadyFoundOne = true;
                }
            }
        }

        return false;
    }

    /**
     * Contr�le si il n'y a pas de d�claration de salaire ouverte dans le module Draco pour l�ann�e concern�e.
     * utilis� lors du processus de mise � jour des PUCS.
     */
    public boolean contientDeclarationSalaireOuverteDansAnneeConcernee(DeclarationSalaire ds, AFAffiliation aff) throws Exception {
        // TODO ESVE equivalant EBPucsValidationDetailViewBean.traiteDeclarationSalaire()? - decl principale
        DSDeclarationListViewBean manager = new DSDeclarationListViewBean();
        manager.setForAnnee(Integer.toString(ds.getAnnee()));
        manager.setForAffiliationId(aff.getAffiliationId());
        manager.setForEtat(DSDeclarationViewBean.CS_OUVERT);
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
     * d�un fichier SwissDec Mixte ou il y a un d�tail par canton
     * utilis� lors du processus de mise � jour des PUCS.
     */
    public boolean contientCollaborateursDansPlusieursCantonsEtPasSwissDecMixte(DeclarationSalaire ds, AFAffiliation aff) {
        if (!ds.getProvenance().isSwissDec()) {
            if (DSProcessValidation.CS_DECL_MIXTE.equals(aff.getDeclarationSalaire())) {
                Set<String> cantons = ds.resolveDistinctContant();
                if (cantons.size() > 1) {
                    return true;
                }
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
     * Contr�le si les validations additionnel sont activ�es dans la propri�t� syst�me
     * utilis� lors du processus de validation des PUCS.
     */
    public boolean isValidationsActive() {
        if (JadePropertiesService.getInstance().getProperty(PROPERTY_ORION_PUCS_BATCH_VALIDATIONS_ACTIVER).equals("true")) {
            return true;
        }

        return false;
    }

    /**
     * Contr�le si les cotisations list� dans la propri�t� syst�me sont toutes pr�sentes
     * utilis� lors du processus de validation des PUCS.
     */
    public boolean contientToutesLesCotisations(DSDeclarationViewBean decl) {
        // Recherche cotisations actives
        List<AFMassesForAffilie> listeMasseForAffilie = AppAffiliationService.retrieveListCotisationForNumAffilie(getSession(),
            decl.getNumeroAffilie(), decl.getAnnee() + "1231");

        // Recherche cotisations pr�sentes dans les propri�t�es
        String propertyCotisationsString = JadePropertiesService.getInstance().getProperty(PROPERTY_ORION_PUCS_BATCH_VALIDATIONS_COTISATIONS);
        propertyCotisationsString = propertyCotisationsString.replaceAll("\\s+", "");
        String[] propertyCotisationsArray = propertyCotisationsString.split("[,;:]");

        // Compares les cotisations actives avec les cotisations pr�sentes dans les propri�t�es
        if (listeMasseForAffilie.stream().map(AFMassesForAffilie::getIdCotisation).collect(Collectors.toList()).containsAll(Arrays.asList(propertyCotisationsArray))) {
            return true;
        }

        return false;
    }
}
