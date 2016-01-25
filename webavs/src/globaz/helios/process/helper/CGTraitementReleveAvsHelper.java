package globaz.helios.process.helper;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.helios.db.avs.CGCompteCompteOfas;
import globaz.helios.db.avs.CGCompteCompteOfasManager;
import globaz.helios.db.avs.CGCompteOfas;
import globaz.helios.db.avs.CGExtendedCompteOfas;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGEcritureListViewBean;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.comptes.CGJournal;
import globaz.helios.db.comptes.CGJournalManager;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGPeriodeComptableManager;
import globaz.helios.db.utils.CGCompteOfasFictifAdminInvestmentHelper;
import globaz.helios.db.utils.CGCompteOfasFictifHelper;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CGTraitementReleveAvsHelper {

    public CGTraitementReleveAvsHelper() {
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
        } else if (isCompteFictif) {
            return extendedCompteOfas.computeSoldeFictifCumule(transaction, idExerciceComptable, idPeriodeComptable,
                    "0", isProvisoire);
        } else {
            result = extendedCompteOfas.computeSoldeCumule(transaction, idExerciceComptable, idPeriodeComptable, "0",
                    isProvisoire);
        }
        return result;
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

        // Parse la liste des liens compteOfas / comptes
        CGCompteCompteOfasManager cptCptOfasMgr = new CGCompteCompteOfasManager();
        cptCptOfasMgr.setSession(session);
        cptCptOfasMgr.setForIdCompteOfas(extendedCompteOfas.getIdCompteOfas());
        cptCptOfasMgr.find(transaction, BManager.SIZE_NOLIMIT);
        result = extendedCompteOfas.computeSoldeCumule(transaction, idExerciceComptable, idPeriodeComptable, "0",
                isProvisoire);

        if (result.passif != null) {
            result.soldeCumule = result.passif;
        } else if (result.actif != null) {
            result.soldeCumule = result.actif;
        }

        result.actif = null;
        result.passif = null;

        for (int j = 0; j < cptCptOfasMgr.size(); j++) {

            CGCompteCompteOfas cptCptOfas = (CGCompteCompteOfas) cptCptOfasMgr.getEntity(j);
            CGCompte compte = new CGCompte();
            compte.setSession(session);
            compte.setIdCompte(cptCptOfas.getIdCompte());
            compte.setIdDomaine(CGCompte.CS_COMPTE_EXPLOITATION);
            compte.retrieve(transaction);
            if (compte == null) {
                throw new Exception("compte inexistant : cptCptOfas.getIdCompte() = " + cptCptOfas.getIdCompte());
            }

            // Calcul du solde cumule pour la periode et l'exercice donné.

            // Récupération des journaux de la période comptable.
            CGJournalManager journalManager = new CGJournalManager();
            journalManager.setSession(session);
            journalManager.setForIdExerciceComptable(idExerciceComptable);
            journalManager.setForIdPeriodeComptable(idPeriodeComptable);
            // ???? A controller avec steph
            journalManager.setExceptIdTypeJournal(CGJournal.CS_TYPE_SYSTEME);

            journalManager.find(transaction, BManager.SIZE_NOLIMIT);

            for (int k = 0; k < journalManager.size(); k++) {
                CGJournal journal = (CGJournal) journalManager.getEntity(k);

                // Récupération des écritures des journaux
                CGEcritureListViewBean ecriManager = new CGEcritureListViewBean();
                ecriManager.setSession(session);
                ecriManager.setForIdCompte(compte.getIdCompte());
                ecriManager.setForIdJournal(journal.getIdJournal());
                ecriManager.setForIdMandat(idMandat);
                ecriManager.setForIdExerciceComptable(idExerciceComptable);
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
            }
        }

        return result;
    }

    public CGMontantHelper getMontantMvtAnnuel(BTransaction transaction, BSession session,
            CGExtendedCompteOfas extendedCompteOfas, String idMandat, String idExerciceComptable,
            String idPeriodeComptable, boolean isProvisoire) throws Exception {

        CGMontantHelper result = new CGMontantHelper();
        CGPeriodeComptableManager periodeComptableManager = null;

        if ((extendedCompteOfas == null) || (extendedCompteOfas.getId() == null)
                || (extendedCompteOfas.getId().length() == 0)) {
            return result;
        }

        CGPeriodeComptable periode = new CGPeriodeComptable();
        periode.setSession(session);
        periode.setIdPeriodeComptable(idPeriodeComptable);
        periode.retrieve(transaction);

        periodeComptableManager = new CGPeriodeComptableManager();

        periodeComptableManager.setSession(session);
        periodeComptableManager.setForIdExerciceComptable(idExerciceComptable);
        periodeComptableManager.setUntilDateFin(periode.getDateFin());

        periodeComptableManager.setOrderBy(CGPeriodeComptableManager.TRI_DATE_FIN_AND_TYPE_DESC);
        // Prendre toute les périodes sauf la péeriode de clôture
        periodeComptableManager.setNotForIdTypePeriode(CGPeriodeComptable.CS_CLOTURE);
        periodeComptableManager.find(transaction, BManager.SIZE_NOLIMIT);

        boolean isCompteFictif = CGCompteOfas.CS_NATURE_FICTIF.equals(extendedCompteOfas.getIdNature()) ? true : false;

        // compte fictif pour admin & investissement (secteurs 4 à 8)
        boolean isCompteFictifAdmInvest = CGCompteOfas.CS_NATURE_FICTIF.equals(extendedCompteOfas.getIdNature()) ? true
                : false;
        String secteur1 = extendedCompteOfas.getIdExterne().substring(0, 1);

        if (isCompteFictifAdmInvest
                && ("4".equals(secteur1) || "5".equals(secteur1) || "6".equals(secteur1) || "7".equals(secteur1) || "8"
                        .equals(secteur1))) {
            isCompteFictifAdmInvest = true;
        } else {
            isCompteFictifAdmInvest = false;
        }

        // Si compte appartient à un compte fictif, et n'est pas un compte
        // fictif, on l'ignore. Il sera pris
        // en compte par le compte fictif.
        if (!isCompteFictif
                && CGCompteOfasFictifAdminInvestmentHelper.getInstance().belongsToCompteFictif(
                        extendedCompteOfas.getIdExterne())) {
            return result;
        }

        CGCompte[] comptes = null;
        if (isCompteFictif && !isCompteFictifAdmInvest) {
            comptes = null;
        } else {
            comptes = extendedCompteOfas.getListComptes(transaction, null);
        }

        if (comptes != null) {
            for (int j = 0; j < comptes.length; j++) {

                // Calcul du solde cumule pour la periode et l'exercice donné.

                for (int k = 0; k < periodeComptableManager.size(); k++) {
                    CGPeriodeComptable periodeComptable = (CGPeriodeComptable) periodeComptableManager.getEntity(k);

                    // Récupération des journaux de la période comptable.
                    CGJournalManager journalManager = new CGJournalManager();
                    journalManager.setSession(session);
                    journalManager.setForIdExerciceComptable(idExerciceComptable);
                    journalManager.setForIdPeriodeComptable(periodeComptable.getIdPeriodeComptable());

                    // ???? A controller avec steph
                    journalManager.setExceptIdTypeJournal(CGJournal.CS_TYPE_SYSTEME);
                    journalManager.find(transaction, BManager.SIZE_NOLIMIT);

                    for (int l = 0; l < journalManager.size(); l++) {
                        CGJournal journal = (CGJournal) journalManager.getEntity(l);

                        // Récupération des écritures des journaux
                        CGEcritureListViewBean ecriManager = new CGEcritureListViewBean();
                        ecriManager.setSession(session);
                        ecriManager.setForIdCompte(comptes[j].getIdCompte());
                        ecriManager.setForIdJournal(journal.getIdJournal());
                        ecriManager.setForIdMandat(idMandat);
                        ecriManager.setForIdExerciceComptable(idExerciceComptable);
                        ecriManager.find(transaction, BManager.SIZE_NOLIMIT);

                        for (int m = 0; m < ecriManager.size(); m++) {

                            if (((CGEcritureViewBean) ecriManager.getEntity(m)).isDoit()) {
                                if (result.actif == null) {
                                    result.actif = new FWCurrency(0);
                                }
                                result.actif.add(((CGEcritureViewBean) ecriManager.getEntity(m)).getMontantAffiche());
                            } else {
                                if (result.passif == null) {
                                    result.passif = new FWCurrency(0);
                                }
                                result.passif.add(((CGEcritureViewBean) ecriManager.getEntity(m)).getMontantAffiche());
                            }
                        }
                    }

                }
            }
        }
        return result;
    }

}
