package globaz.helios.process.helper;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.helios.db.avs.CGCompteOfas;
import globaz.helios.db.avs.CGExtendedCompteOfas;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.comptes.CGJournal;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.helper.CGEcritureBalanceMouvementManager;
import globaz.helios.db.comptes.helper.CGEcritureCptExpoitationManager;
import globaz.helios.db.utils.CGCompteOfasFictifHelper;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CGHelperReleveAVS {

    /**
     * Constructor for CGHelperReleveAVS.
     */
    public CGHelperReleveAVS() {
        super();
    }

    public CGMontantHelper getMontantComptesAdministration(BTransaction transaction, BSession session,
            CGExtendedCompteOfas extendedCompteOfas, String idExerciceComptable, String idPeriodeComptable,
            boolean isProvisoire) throws Exception {
        CGMontantHelper result = new CGMontantHelper();
        boolean isCompteFictif = CGCompteOfas.CS_NATURE_FICTIF.equals(extendedCompteOfas.getIdNature()) ? true : false;
        // Si compte appartient à un compte fictif, et n'est pas un compte
        // fictif, on l'ignore. Il sera pris
        // en compte par le compte fictif.
        if (!isCompteFictif
                && CGCompteOfasFictifHelper.getInstance().belongsToCompteFictif(extendedCompteOfas.getIdExterne())) {
            return result;
            // } else if (isCompteFictif) {
            // return extendedCompteOfas.computeSoldeFictifCumule(transaction, idExerciceComptable, idPeriodeComptable,
            // "0", isProvisoire);
        } else {
            return extendedCompteOfas.computeSoldeCumule(transaction, idExerciceComptable, idPeriodeComptable, "0",
                    isProvisoire);
        }
    }

    public CGMontantHelper getMontantComptesBilan(BTransaction transaction, BSession session,
            CGExtendedCompteOfas extendedCompteOfas, String idExerciceComptable, String idPeriodeComptable,
            boolean isProvisoire) throws Exception {
        CGMontantHelper result = null;
        result = extendedCompteOfas.computeSoldeCumule(transaction, idExerciceComptable, idPeriodeComptable, "0",
                isProvisoire);
        return result;
    }

    public CGMontantHelper getMontantComptesExploitation(BTransaction transaction, BSession session,
            CGExtendedCompteOfas extendedCompteOfas, String idMandat, String idExerciceComptable,
            String idPeriodeComptable, boolean isProvisoire) throws Exception {
        CGMontantHelper result = null;

        result = extendedCompteOfas.computeSoldeCumule(transaction, idExerciceComptable, idPeriodeComptable, "0",
                isProvisoire);
        if (result.passif != null) {
            result.soldeCumule = result.passif;
        } else if (result.actif != null) {
            result.soldeCumule = result.actif;
        }
        result.actif = null;
        result.passif = null;

        CGEcritureCptExpoitationManager ecriManager = new CGEcritureCptExpoitationManager();
        ecriManager.setSession(session);
        ecriManager.setForIdCompteOfas(extendedCompteOfas.getIdCompteOfas());
        ecriManager.setForIdExterneComptable(idExerciceComptable);
        ecriManager.setForIdMandat(idMandat);
        ecriManager.setForIdPeriodeComptable(idPeriodeComptable);
        ecriManager.setForNotIdTypeJournal(CGJournal.CS_TYPE_SYSTEME);
        ecriManager.setForIsActive(new Boolean(true));
        ecriManager.setForIsProvisoire(new Boolean(isProvisoire));
        ecriManager.find(transaction, BManager.SIZE_NOLIMIT);

        for (int l = 0; l < ecriManager.size(); l++) {
            if (((CGEcritureViewBean) ecriManager.getEntity(l)).isDoit()) {
                if (result.actif == null) {
                    result.actif = new FWCurrency(0);
                }
                result.actif.add(((CGEcritureViewBean) ecriManager.getEntity(l)).getMontantAffiche());
            } else {
                if (result.passif == null) {
                    result.passif = new FWCurrency(0);
                }
                result.passif.add(((CGEcritureViewBean) ecriManager.getEntity(l)).getMontantAffiche());
            }
        }

        // Si des mouvements ont eu lieu même pendant les périodes précédentes,
        // le
        // compte doit être affiché -> actif et/ou passif setté à 0
        if ((result.actif == null) && (result.passif == null)
                && ((result.soldeCumule == null) || result.soldeCumule.equals(new FWCurrency(0)))) {

            ecriManager.setForIdPeriodeComptable(null);
            ecriManager.find(transaction, 2);

            if (ecriManager.size() > 0) {
                result.actif = new FWCurrency(0);
                result.passif = new FWCurrency(0);
            }
        }
        return result;
    }

    public CGMontantHelper getMontantMvtAnnuel(BTransaction transaction, BSession session,
            CGExtendedCompteOfas extendedCompteOfas, String idMandat, String idExerciceComptable,
            String idPeriodeComptable, boolean isProvisoire) throws Exception {
        CGMontantHelper result = new CGMontantHelper();

        if ((extendedCompteOfas == null) || (extendedCompteOfas.getId() == null)
                || (extendedCompteOfas.getId().length() == 0)) {
            return result;
        }
        boolean isCompteFictif = CGCompteOfas.CS_NATURE_FICTIF.equals(extendedCompteOfas.getIdNature()) ? true : false;
        // compte fictif pour admin & investissement (secteurs 4 à 8)
        boolean isCompteFictifAdmInvest;
        int[] secteurs = { 4, 5, 6, 7, 8 };
        isCompteFictifAdmInvest = (Arrays.binarySearch(secteurs,
                JadeStringUtil.toIntMIN(extendedCompteOfas.getIdExterne().substring(0, 1))) > 0);

        // CGCompte[] comptes= null;
        Iterator comptes = null;
        if (isCompteFictif && !isCompteFictifAdmInvest) {
            comptes = null;
        } else {
            // comptes= extendedCompteOfas.getListComptes(transaction, null);
            comptes = extendedCompteOfas.getListComptesIterator(transaction, null);
        }
        if (comptes != null) {
            // Manager
            CGEcritureBalanceMouvementManager balance = new CGEcritureBalanceMouvementManager();
            balance.setSession(session);
            balance.setForIdExerciceComptable(idExerciceComptable);
            balance.setForIdMandat(idMandat);
            balance.setForIdPeriodeComptable(idPeriodeComptable);
            balance.setForNotIdTypeJournal(CGJournal.CS_TYPE_SYSTEME);
            balance.setForNotIdTypePeriode(CGPeriodeComptable.CS_CLOTURE);
            balance.setForIsActive(new Boolean(true));

            // for (int j= 0; j < comptes.length; j++) {
            while (comptes.hasNext()) {
                // Calcul du solde cumule pour la periode et l'exercice donné.
                // balance.setForIdCompte(comptes[j].getIdCompte());
                balance.setForIdCompte(((CGCompte) comptes.next()).getIdCompte());
                balance.find(transaction, BManager.SIZE_NOLIMIT);
                for (int x = 0; x < balance.size(); x++) {
                    if (((CGEcritureViewBean) balance.getEntity(x)).isDoit()) {
                        if (result.actif == null) {
                            result.actif = new FWCurrency(0);
                        }
                        result.actif.add(((CGEcritureViewBean) balance.getEntity(x)).getMontantAffiche());
                    } else {
                        if (result.passif == null) {
                            result.passif = new FWCurrency(0);
                        }
                        result.passif.add(((CGEcritureViewBean) balance.getEntity(x)).getMontantAffiche());
                    }
                }
            }
        }
        return result;
    }
}
