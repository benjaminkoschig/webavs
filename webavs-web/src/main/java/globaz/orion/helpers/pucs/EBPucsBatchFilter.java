package globaz.orion.helpers.pucs;

import ch.globaz.draco.business.domaine.DeclarationSalaireType;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaire;
import ch.globaz.orion.business.domaine.pucs.Employee;
import ch.globaz.orion.business.domaine.pucs.SalaryAvs;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.business.models.pucs.PucsFileMerge;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.draco.db.declaration.DSDeclarationListViewBean;
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

    // Contr�ler qu�il n�y ait pas de fichiers pour l�ann�e en cours avec le m�me total de contr�le ;
    // Si plusieurs fichiers identiques (ann�e et total), ne garder que le premier et rejeter les autres ;
    public boolean hasDeclarationAvecAnneDeclarationEtTotalIdentique(PucsFile pucsFile, List<PucsFileMerge> pucsFileMerges) {
        for (PucsFileMerge pucsFileMerge : pucsFileMerges) {
            for (PucsFile nextPucsFile : pucsFileMerge.getPucsFileToMergded()) {
                if (Objects.equals(pucsFile.getAnneeDeclaration(), nextPucsFile.getAnneeDeclaration()) && Objects.equals(pucsFile.getNumeroAffilie(), nextPucsFile.getNumeroAffilie()) && Objects.equals(pucsFile.getTotalControle(), nextPucsFile.getTotalControle()) && pucsFile.getTypeDeclaration() == nextPucsFile.getTypeDeclaration()) {
                    return true;
                }
            }
        }

        return false;
    }

    // V�rifier qu�il n�y ait pas de d�claration de salaire ouverte dans le module Draco pour l�ann�e concern�e ;
    public boolean hasDeclarationSalaireDracoOuverteDansAnneeConcernee(DeclarationSalaire ds, AFAffiliation aff, BSession session) throws Exception {
        DSDeclarationListViewBean manager = new DSDeclarationListViewBean();
        manager.setForAnnee(Integer.toString(ds.getAnnee()));
        manager.setForAffiliationId(aff.getAffiliationId());
        manager.setForEtat(IAPPrestation.CS_ETAT_PRESTATION_OUVERT);
        manager.setForTypeDeclaration(DeclarationSalaireType.PRINCIPALE.getCodeSystem());
        manager.setSession(session);
        manager.find(BManager.SIZE_NOLIMIT);

        if (manager.getSize() > 0) {
            return true;
        }

        return false;
    }

    // Le num�ro d�affili� et le nom de la soci�t� correspondent sur WebAVS (�cran EB0004) ;
    public boolean hasNumeroAffilieEtNomSocieteNonExistant(PucsFile pucsFile, BSession session) throws Exception {
        AFAffiliation aff = EBDanUtils.findAffilie(session, pucsFile.getNumeroAffilie(), "31.12."
                + pucsFile.getAnneeDeclaration(), "01.01." + pucsFile.getAnneeDeclaration());
        if (aff == null) {
            return true;
        }

        return false;
    }

    //	Si des collaborateurs sont dans plusieurs cantons alors il doit s�agir d�un fichier MIX ou il y a un d�tail par canton ;
    public boolean hasCollaborateursDansPlusieursCantonsEtTypeNonMixe(DeclarationSalaire ds) {
        Set<String> cantons = ds.resolveDistinctContant();
        if (cantons.size() > 1) {
            return true;
        }

        return false;
    }

    // Rejeter le fichier lorsqu�il y a un salaire n�gatif d�clar�.
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
