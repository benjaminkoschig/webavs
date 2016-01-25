package globaz.lynx.utils;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.operation.LXOperationManager;
import globaz.lynx.db.section.LXSection;
import java.util.ArrayList;

public class LXNoteDeCreditUtil {

    /**
     * Permet la création d'une note de crédit
     * 
     * @param operation
     * @param inverse
     * @throws Exception
     */
    public static void createNoteDeCredit(BSession session, String idSection, String idSectionLiee, String montant,
            String idOperationLiee, String idJournal, String idOperationSrc) throws Exception {

        LXOperation operation = new LXOperation();
        operation.setIdSection(idSection);
        operation.setMontant(montant);

        operation.setCsTypeOperation(LXOperation.CS_TYPE_NOTEDECREDIT_LIEE);
        operation.setCsEtatOperation(LXOperation.CS_ETAT_COMPTABILISE);
        operation.setDateOperation(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));
        operation.setIdJournal(idJournal);
        operation.setIdOperationLiee(idOperationLiee);
        operation.setIdOperationSrc(idOperationSrc);
        operation.setIdOperation(null);
        operation.setCsCodeIsoMonnaie(LXConstants.CODE_ISO_CHF);

        operation.setIdSectionLiee(idSectionLiee);

        operation.add(session.getCurrentThreadTransaction());

        if (operation.hasErrors()) {
            throw new Exception(operation.getErrors().toString());
        }

        if (operation.isNew()) {
            throw new Exception(session.getLabel("LIER_NOTEDECREDIT_NON_CREE"));
        }

    }

    /**
     * Permet la récupération de l'id externe d'une section
     * 
     * @param session
     * @param idSectionLiee
     * @return
     * @throws Exception
     */
    public static String getIdExterneSection(BSession session, String idSection) throws Exception {
        LXSection section = new LXSection();
        section.setSession(session);
        section.setIdSection(idSection);

        section.retrieve();

        if (section.hasErrors() || section.isNew()) {
            return "";
        } else {
            return section.getIdExterne();
        }
    }

    /**
     * Return le montant de la facture (opération) réduite du montant de ces opérations liées, paiements et escomptes.
     * 
     * @param session
     * @param idOperation
     * @return
     * @throws Exception
     */
    public static FWCurrency getMontantFactureDejaUtilise(BSession session, String idOperation, String idSectionFacture)
            throws Exception {
        LXOperationManager opeManager = new LXOperationManager();
        opeManager.setSession(session);
        opeManager.setForIdOperationLiee(idOperation);

        ArrayList<String> forIdTypeOperationIn = new ArrayList<String>();
        forIdTypeOperationIn.add(LXOperation.CS_TYPE_NOTEDECREDIT_LIEE);
        opeManager.setForIdTypeOperationIn(forIdTypeOperationIn);

        ArrayList<String> forCsEtatIn = new ArrayList<String>();
        forCsEtatIn.add(LXOperation.CS_ETAT_COMPTABILISE);
        forCsEtatIn.add(LXOperation.CS_ETAT_PREPARE);
        forCsEtatIn.add(LXOperation.CS_ETAT_SOLDE);
        opeManager.setForCsEtatIn(forCsEtatIn);

        opeManager.setForIdSection(idSectionFacture);

        opeManager.find();
        FWCurrency valeurReelleFacture = new FWCurrency(opeManager.getSum(LXOperation.FIELD_MONTANT).toString());

        opeManager = new LXOperationManager();
        opeManager.setSession(session);
        opeManager.setForIdOperationSrc(idOperation);

        forIdTypeOperationIn = new ArrayList<String>();
        forIdTypeOperationIn.add(LXOperation.CS_TYPE_PAIEMENT_BVR_ORANGE);
        forIdTypeOperationIn.add(LXOperation.CS_TYPE_PAIEMENT_BVR_ROUGE);
        forIdTypeOperationIn.add(LXOperation.CS_TYPE_PAIEMENT_VIREMENT);
        forIdTypeOperationIn.add(LXOperation.CS_TYPE_PAIEMENT_CAISSE);
        forIdTypeOperationIn.add(LXOperation.CS_TYPE_PAIEMENT_LSV);
        forIdTypeOperationIn.add(LXOperation.CS_TYPE_ESCOMPTE);
        opeManager.setForIdTypeOperationIn(forIdTypeOperationIn);

        forCsEtatIn = new ArrayList<String>();
        forCsEtatIn.add(LXOperation.CS_ETAT_OUVERT);
        forCsEtatIn.add(LXOperation.CS_ETAT_COMPTABILISE);
        forCsEtatIn.add(LXOperation.CS_ETAT_PREPARE);
        forCsEtatIn.add(LXOperation.CS_ETAT_SOLDE);
        opeManager.setForCsEtatIn(forCsEtatIn);

        valeurReelleFacture.add(opeManager.getSum(LXOperation.FIELD_MONTANT).toString());

        return valeurReelleFacture;
    }

    /**
     * Retourne le motant restant à attribuer pour une note de crédit.
     * 
     * @param session
     * @param transaction
     * @param idSection
     * @return
     * @throws Exception
     */
    public static String getMontantRestantNoteDeCredit(BSession session, String idSection, String idOperationSrc,
            String withoutIdOperation) throws Exception {
        LXOperationManager operationManager = new LXOperationManager();
        operationManager.setSession(session);

        ArrayList<String> forIdTypeOperationIn = new ArrayList<String>();
        forIdTypeOperationIn.add(LXOperation.CS_TYPE_NOTEDECREDIT_DEBASE);
        forIdTypeOperationIn.add(LXOperation.CS_TYPE_NOTEDECREDIT_ENCAISSEE);
        forIdTypeOperationIn.add(LXOperation.CS_TYPE_NOTEDECREDIT_LIEE);

        ArrayList<String> forCsEtatIn = new ArrayList<String>();
        forCsEtatIn.add(LXOperation.CS_ETAT_COMPTABILISE);
        forCsEtatIn.add(LXOperation.CS_ETAT_PREPARE);
        forCsEtatIn.add(LXOperation.CS_ETAT_SOLDE);
        operationManager.setForCsEtatIn(forCsEtatIn);

        operationManager.setForIdOperationOrIdOperationSrc(idOperationSrc);
        operationManager.setForIdTypeOperationIn(forIdTypeOperationIn);
        operationManager.setForIdSection(idSection);
        operationManager.setWithoutIdOperation(withoutIdOperation);

        return operationManager.getSum(LXOperation.FIELD_MONTANT).toString();
    }

    /**
     * Retourne l'autre note de crédit liée.
     * 
     * @param session
     * @param transaction
     * @param idOperationSrc
     * @param idOperationLiee
     * @param idSectionLiee
     * @return
     * @throws Exception
     */
    public static LXOperation getNoteCreditLieeSurSectionLiee(BSession session, BTransaction transaction,
            String idOperationSrc, String idOperationLiee, String idSectionLiee) throws Exception {
        LXOperationManager manager = new LXOperationManager();
        manager.setSession(session);

        manager.setForIdOperationSrc(idOperationSrc);
        manager.setForIdOperationLiee(idOperationLiee);
        manager.setForIdSectionLiee(idSectionLiee);

        manager.find(transaction);

        if (manager.hasErrors()) {
            throw new Exception(manager.getErrors().toString());
        }

        if (manager.isEmpty() || (manager.size() > 1)) {
            throw new Exception(session.getLabel("NDC_MONTANT_FACTURE"));
        }

        return (LXOperation) manager.getFirstEntity();
    }

    /**
     * Return l'opération liée.
     * 
     * @param session
     * @param idFournisseur
     * @param idSociete
     * @param idSection
     * @return Montant de la somme des factures d'une section
     * @throws Exception
     */
    public static LXOperation getOperationLiee(BSession session, BTransaction transaction, String idOperation)
            throws Exception {
        LXOperation operation = new LXOperation();
        operation.setSession(session);

        operation.setIdOperation(idOperation);

        operation.retrieve(transaction);

        if (operation.hasErrors()) {
            throw new Exception(operation.getErrors().toString());
        }

        if (operation.isNew()) {
            throw new Exception(session.getLabel("NDC_MONTANT_FACTURE"));
        }

        return operation;
    }

    /**
     * Recherche si il existe une note de crédit du type passé en paramètre - <b>true</b> si il la note de credit existe
     * - <b>false</b> si il la note de credit n'existe pas
     * 
     * @return
     */
    public static boolean isExiste(BSession session, String idSection, String idOperationSrc, String csTypeNoteDeCredit) {
        try {
            LXOperationManager opeManager = new LXOperationManager();
            opeManager.setForIdSection(idSection);
            opeManager.setSession(session);

            ArrayList<String> idTypeOperationIn = new ArrayList<String>();
            idTypeOperationIn.add(csTypeNoteDeCredit);
            opeManager.setForIdTypeOperationIn(idTypeOperationIn);

            opeManager.setForIdOperationSrc(idOperationSrc);

            opeManager.find();

            if (opeManager.getSize() > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Recherche si il existe une note de crédit de type <b>Encaissée</b> suivant la section et l'opération données
     * 
     * @param session
     * @param idSection
     * @param idOperationSrc
     * @return
     */
    public static boolean isExisteNoteDeCreditEncaissee(BSession session, String idSection, String idOperationSrc) {
        return LXNoteDeCreditUtil.isExiste(session, idSection, idOperationSrc,
                LXOperation.CS_TYPE_NOTEDECREDIT_ENCAISSEE);
    }

    /**
     * Recherche si il existe une note de crédit de type <b>Liée</b> suivant la section et l'opération données
     * 
     * @param session
     * @param idSection
     * @param idOperationSrc
     * @return
     */
    public static boolean isExisteNoteDeCreditLiee(BSession session, String idSection, String idOperationSrc) {
        return LXNoteDeCreditUtil.isExiste(session, idSection, idOperationSrc, LXOperation.CS_TYPE_NOTEDECREDIT_LIEE);
    }

    /**
     * Retourne vrai si une note de crédit peut être Encaissable ou Liable
     * 
     * @param session
     * @param idSection
     * @return
     */
    public static boolean isPossibleEncaissable(BSession session, String idSection) {
        // Recupération des notes de crédit de la section donné

        try {
            LXOperationManager opeManager = new LXOperationManager();
            opeManager.setForIdSection(idSection);
            opeManager.setSession(session);

            // On ne prend ne compte que les factures de type Note de crédit
            ArrayList<String> idTypeOperationIn = new ArrayList<String>();
            idTypeOperationIn.add(LXOperation.CS_TYPE_NOTEDECREDIT_DEBASE);
            idTypeOperationIn.add(LXOperation.CS_TYPE_NOTEDECREDIT_ENCAISSEE);
            idTypeOperationIn.add(LXOperation.CS_TYPE_NOTEDECREDIT_LIEE);
            opeManager.setForIdTypeOperationIn(idTypeOperationIn);

            // On ne prend en compte que les factures qui sont comptabilisées
            ArrayList<String> idEtatOperationIn = new ArrayList<String>();
            idEtatOperationIn.add(LXOperation.CS_ETAT_COMPTABILISE);
            opeManager.setForCsEtatIn(idEtatOperationIn);

            FWCurrency totalEcritures = new FWCurrency(opeManager.getSum(LXOperation.FIELD_MONTANT).doubleValue());

            if (!totalEcritures.isPositive()) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Constructeur
     */
    protected LXNoteDeCreditUtil() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }

}
