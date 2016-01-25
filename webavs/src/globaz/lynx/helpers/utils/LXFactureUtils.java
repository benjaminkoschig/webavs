package globaz.lynx.helpers.utils;

import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.facture.LXFactureManager;
import globaz.lynx.db.facture.LXFactureViewBean;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.operation.LXOperationManager;
import globaz.lynx.db.section.LXSection;
import globaz.lynx.db.section.LXSectionManager;

public class LXFactureUtils {

    /**
     * Test si l'idExterne passé en parametre est unique pour les informations passées en parametre.
     * 
     * @param idSection
     * @param idSociete
     * @param idFournisseur
     * @param csTypeSection
     * @param idExterne
     *            : No de facture interne
     * @return
     */
    public static boolean isIdExterneExiste(BISession session, String idSection, String idSociete,
            String idFournisseur, String csTypeSection, String idExterne) throws Exception {

        LXSectionManager manager = new LXSectionManager();
        manager.setSession((BSession) session);
        manager.setForIdSociete(idSociete);
        manager.setForIdFournisseur(idFournisseur);
        manager.setForCsTypeSection(csTypeSection);
        manager.setForIdExterne(idExterne);
        manager.setForNotIdSection(idSection);
        manager.find();

        if (manager.size() == 1) {
            return true;
        }

        return false;
    }

    /**
     * Test si le numero de facture fournisseur (reference externe dans la table des operations) n'est pas déjà utilisé
     * !
     * 
     * @param session
     * @param idOperation
     * @param referenceExterne
     * @return
     * @throws Exception
     */
    public static boolean isReferenceExterne(BISession session, String idOperation, String referenceExterne)
            throws Exception {

        LXOperationManager manager = new LXOperationManager();
        manager.setSession((BSession) session);
        manager.setForIdOperationNot(idOperation);
        manager.setLikeReferenceExterne(referenceExterne);
        manager.find();

        if (manager.size() == 1) {
            return true;
        }

        return false;
    }

    /**
     * Test si la référence externe (numero de facture fournisseur) passé en parametre est unique pour les informations
     * passées en parametre.
     * 
     * @param session
     * @param idOperation
     * @param idFournisseur
     * @param idReferenceExterne
     *            : No de facture fournisseur
     * @return
     * @throws Exception
     */
    public static boolean isReferenceExterneExiste(BISession session, String idOperation, String idFournisseur,
            String idReferenceExterne) throws Exception {

        if (!JadeStringUtil.isBlankOrZero(idReferenceExterne)) {
            LXFactureManager manager = new LXFactureManager();
            manager.setSession((BSession) session);
            manager.setForReferenceExterne(idReferenceExterne);
            manager.setForIdOperationNot(idOperation);
            manager.setForIdFournisseur(idFournisseur);
            manager.setCheckExtourne(true);
            manager.find();

            if (manager.size() == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validation des informations saisies par l'utilisateur.
     * 
     * @param session
     * @param transaction
     * @param facture
     * @throws Exception
     */
    public static void validate(BISession session, BTransaction transaction, LXFactureViewBean facture)
            throws Exception {

        LXHelperUtils.validateCommonPart(session, transaction, facture);

        if (JadeStringUtil.isBlank(facture.getDateEcheance())) {
            throw new Exception(((BSession) session).getLabel("FACTURE_VALIDATE_DATE_ECHEANCE"));
        }

        if (facture.getEstBloque().booleanValue() && JadeStringUtil.isIntegerEmpty(facture.getCsMotifBlocage())) {
            throw new Exception(((BSession) session).getLabel("FACTURE_VALIDATE_MOTIF_BLOCAGE"));
        }

        if (JadeStringUtil.isBlank(facture.getReferenceBVR())
                && (LXOperation.CS_TYPE_FACTURE_BVR_ORANGE.equals(facture.getCsTypeOperation()))) {
            throw new Exception(((BSession) session).getLabel("VAL_NUMERO_REFERENCE_BVR"));
        }

        if (!JadeStringUtil.isBlank(facture.getReferenceBVR())
                && (LXOperation.CS_TYPE_FACTURE_BVR_ORANGE.equals(facture.getCsTypeOperation()))) {
            LXHelperUtils.checkReferenveBVRModulo(transaction, facture.getReferenceBVR());
        }

        if (JadeStringUtil.isBlank(facture.getCcpFournisseur())
                && (LXOperation.CS_TYPE_FACTURE_BVR_ORANGE.equals(facture.getCsTypeOperation()) || LXOperation.CS_TYPE_FACTURE_BVR_ROUGE
                        .equals(facture.getCsTypeOperation()))) {
            throw new Exception(((BSession) session).getLabel("FACTURE_VALIDATE_CCP"));
        }

        if (LXFactureUtils.isIdExterneExiste(session, facture.getIdSection(), facture.getIdSociete(),
                facture.getIdFournisseur(), LXSection.CS_TYPE_FACTURE, facture.getIdExterne())) {
            throw new Exception(((BSession) session).getLabel("FACTURE_VALIDATE_ID_EXTERNE"));
        }

        if (LXFactureUtils.isReferenceExterneExiste(session, facture.getIdOperation(), facture.getIdFournisseur(),
                facture.getReferenceExterne())) {
            throw new Exception(((BSession) session).getLabel("FACTURE_VALIDATE_REFERENCE_EXTERNE"));
        }
    }

    /**
     * Constructeur
     */
    protected LXFactureUtils() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }
}
