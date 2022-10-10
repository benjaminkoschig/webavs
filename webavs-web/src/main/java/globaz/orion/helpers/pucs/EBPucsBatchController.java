package globaz.orion.helpers.pucs;

import ch.globaz.orion.business.domaine.pucs.DeclarationSalaire;
import ch.globaz.orion.business.domaine.pucs.Employee;
import ch.globaz.orion.business.domaine.pucs.SalaryAvs;
import ch.globaz.orion.business.models.pucs.PucsFile;
import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.process.DSProcessValidation;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.orion.utils.EBDanUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Regroupe tous les contrôles additionels effectués lors de la mise à jour
 * ou de la validation des fichiers PUCS s'effectue au travers du batch
 */
public class EBPucsBatchController {

    BSession session = null;

    public BSession getSession() {
        return session;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    /**
     * Contrôle qu'il n'y a pas plus de un fichiers pucs pour l’année en cours avec le même total de contrôle.
     * utilisé lors du processus de mise à jour des PUCS.
     */
    public boolean contientDeclarationAvecAnneDeclarationEtTotalIdentique(PucsFile pucsFile, List<PucsFile> mergedPucsFiles) {
        int compteurDoublon = 0;
        for (PucsFile nextPucsFile : mergedPucsFiles) {
            if (Objects.equals(pucsFile.getAnneeDeclaration(), nextPucsFile.getAnneeDeclaration()) && Objects.equals(pucsFile.getNumeroAffilie(), nextPucsFile.getNumeroAffilie()) && Objects.equals(pucsFile.getTotalControle(), nextPucsFile.getTotalControle()) && pucsFile.getTypeDeclaration() == nextPucsFile.getTypeDeclaration()) {
                compteurDoublon ++;
            }
        }
        if (compteurDoublon > 1) {
            return true;
        }

        return false;
    }

    /**
     * Contrôle si il n'y a pas de déclaration de salaire ouverte dans le module Draco pour l’année concernée.
     * utilisé lors du processus de mise à jour des PUCS.
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
     * d’un fichier SwissDec Mixte ou il y a un détail par canton
     * utilisé lors du processus de mise à jour des PUCS.
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
}
