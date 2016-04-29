package globaz.naos.db.releve;

import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.translation.CodeSystem;
import java.util.ArrayList;
import java.util.List;

public class AFReleve1314Checker {

    public static boolean hasDeclarationSalaireAFacturer(String forType, int annee, String numAffilie, BSession session)
            throws Exception {

        // int annee = JACalendar.getYear(getDateDebut());

        AFAffiliationManager affiliationManager = new AFAffiliationManager();
        affiliationManager.setSession(session);
        affiliationManager.setForAffilieNumero(numAffilie);
        affiliationManager.setForTypesAffParitaires();
        affiliationManager.find(BManager.SIZE_NOLIMIT);
        AFAffiliation affilie = (AFAffiliation) affiliationManager.getFirstEntity();

        DSDeclarationListViewBean declarationManager = new DSDeclarationListViewBean();
        declarationManager.setSession(session);
        declarationManager.setForAffiliationId(affilie.getAffiliationId());
        declarationManager.setForAnnee(new Integer(annee).toString());
        declarationManager.setForTypeDeclaration(forType);
        declarationManager.setForEtat(DSDeclarationViewBean.CS_AFACTURER);
        declarationManager.find(BManager.SIZE_NOLIMIT);

        return declarationManager.getCount() > 0;
    }

    public static boolean hasReleveAFacturerOuValide(String forType, int annee, String numAffilie, BSession session)
            throws Exception {
        return AFReleve1314Checker.hasReleveAFacturerOuValide(forType, annee, numAffilie, "", session);
    }

    public static boolean hasReleveAFacturerOuValide(String forType, int annee, String numAffilie, String idReleve,
            BSession session) throws Exception {

        AFApercuReleveManager manager = new AFApercuReleveManager();

        // int annee = JACalendar.getYear(getDateDebut());

        manager.setSession(session);
        manager.setForAffilieNumero(numAffilie);
        manager.setFromDateDebut("01.01." + annee);
        manager.setUntilDateFin("31.12." + annee);
        manager.setForType(forType);
        List<String> etats = new ArrayList<String>();
        etats.add(CodeSystem.ETATS_RELEVE_FACTURER);
        etats.add(CodeSystem.ETATS_RELEVE_COMPTABILISER);
        manager.setInEtats(etats);
        manager.setNotForIdReleve(idReleve);

        return manager.getCount() > 0;
    }
}
