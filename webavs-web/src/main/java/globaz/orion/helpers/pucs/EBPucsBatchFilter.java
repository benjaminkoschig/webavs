package globaz.orion.helpers.pucs;

import ch.globaz.draco.business.domaine.DeclarationSalaireType;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaire;
import ch.globaz.orion.business.domaine.pucs.Employee;
import ch.globaz.orion.business.domaine.pucs.SalaryAvs;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.business.models.pucs.PucsFileMerge;
import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.orion.utils.EBDanUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Helper de filtre du process de traitement des fichiers PUCS
 * 
 */
public class EBPucsBatchFilter {

    // Contrôler qu’il n’y ait pas de fichiers pour l’année en cours avec le même total de contrôle ;
    // Si plusieurs fichiers identiques (année et total), ne garder que le premier et rejeter les autres ;
    public boolean hasDeclarationAvecAnneDeclarationEtTotalIdentique(PucsFile pucsFile, List<PucsFileMerge> pucsFileMerges) {
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

    // Vérifier qu’il n’y ait pas de déclaration de salaire ouverte dans le module Draco pour l’année concernée ;
    public boolean hasDeclarationSalaireOuverteDansAnneeConcernee(PucsFile pucsFile, DeclarationSalaire ds, AFAffiliation aff, BSession session) throws Exception {
        DSDeclarationListViewBean manager = new DSDeclarationListViewBean();
        manager.setForAnnee(Integer.toString(ds.getAnnee()));
        manager.setForAffiliationId(aff.getAffiliationId());
        manager.setForEtat(DSDeclarationViewBean.CS_OUVERT);
        manager.setSession(session);
        manager.find(BManager.SIZE_NOLIMIT);

        if (manager.getSize() > 0) {
            return true;
        }

        return false;
    }

    // Si le numéro d’affilié n'existe pas sur WebAVS (écran EB0004) ;
    public boolean hasNumeroAffilieNonExistant(PucsFile pucsFile, BSession session) throws Exception {
        AFAffiliation aff = EBDanUtils.findAffilie(session, pucsFile.getNumeroAffilie(), "31.12."
                + pucsFile.getAnneeDeclaration(), "01.01." + pucsFile.getAnneeDeclaration());
        if (aff == null) {
            return true;
        }

        return false;
    }

    //	Si des collaborateurs sont dans plusieurs cantons alors il doit s’agir d’un fichier SWISSDEC ou il y a un détail par canton ;
    public boolean hasCollaborateursDansPlusieursCantonsEtNonSwissDec(PucsFile pucsFile, DeclarationSalaire ds) {
        if (!pucsFile.getProvenance().isSwissDec()) {
            Set<String> cantons = ds.resolveDistinctContant();
            if (cantons.size() > 1) {
                return true;
            }
        }

        return false;
    }

    // Rejeter le fichier lorsqu’il y a un salaire négatif déclaré.
    public boolean hasSalaireNegatif(List<Employee> employees) {
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
