package globaz.naos.db.releve;

import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.globall.db.BSession;
import globaz.naos.translation.CodeSystem;
import java.util.ArrayList;
import java.util.List;

public class AFReleve1314Checker {

    public static boolean hasDeclarationSalaireAFacturer(String forType, int annee, String idAffiliation,
            BSession session) throws Exception {

        return hasDeclarationSalaireAFacturer(forType, annee, idAffiliation, session, null);
    }

    public static boolean hasDeclarationSalaireAFacturer(String forType, int annee, String idAffiliation,
            BSession session, String idDeclaration) throws Exception {

        // Preconditions.checkNotNull(forType, "Type of declaration de salaire is null");
        // Preconditions.checkNotNull(numAffilie, "Numero affilie is null");
        // Preconditions.checkNotNull(session, "Session is null");

        DSDeclarationListViewBean declarationManager = new DSDeclarationListViewBean();
        declarationManager.setSession(session);
        declarationManager.setForAffiliationId(idAffiliation);
        declarationManager.setForAnnee(new Integer(annee).toString());
        declarationManager.setForTypeDeclaration(forType);
        declarationManager.setForEtat(DSDeclarationViewBean.CS_AFACTURER);

        if (idDeclaration != null) {
            declarationManager.setNotForIdDeclaration(idDeclaration);
        }

        return declarationManager.getCount() > 0;
    }

    public static boolean hasReleveAFacturerOuValide(String forType, int annee, String numAffilie, BSession session)
            throws Exception {
        return AFReleve1314Checker.hasReleveAFacturerOuValide(forType, annee, numAffilie, "", session);
    }

    public static boolean hasReleveAFacturerOuValide(String forType, int annee, String numAffilie, String idReleve,
            BSession session) throws Exception {

        // Preconditions.checkNotNull(forType, "Type of declaration de salaire is null");
        // Preconditions.checkNotNull(numAffilie, "Numero affilie is null");
        // Preconditions.checkNotNull(session, "Session is null");
        // Preconditions.checkNotNull(idReleve, "Numero de releve is null");

        List<String> etats = new ArrayList<String>();
        etats.add(CodeSystem.ETATS_RELEVE_FACTURER);

        AFApercuReleveManager manager = new AFApercuReleveManager();
        manager.setSession(session);
        manager.setForAffilieNumero(numAffilie);
        manager.setFromDateDebut("01.01." + annee);
        manager.setUntilDateFin("31.12." + annee);
        manager.setForType(forType);
        manager.setInEtats(etats);
        manager.setNotForIdReleve(idReleve);

        return manager.getCount() > 0;
    }

}
