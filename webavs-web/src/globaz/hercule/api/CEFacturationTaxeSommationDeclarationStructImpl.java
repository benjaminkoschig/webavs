package globaz.hercule.api;

import globaz.globall.db.BProcess;
import globaz.hercule.process.facturation.CEProcessFacturationTaxeSommation;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.external.IntModuleFacturation;

/**
 * @author JPA
 * @since 31 aout. 2010
 */
public class CEFacturationTaxeSommationDeclarationStructImpl implements IntModuleFacturation {

    /**
     * Constructeur de CEFacturationTaxeSommationDeclarationStructImpl
     */
    public CEFacturationTaxeSommationDeclarationStructImpl() {
        super();
    }

    @Override
    public boolean avantRecomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean avantRegenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return true;
    }

    @Override
    public boolean avantRepriseErrCom(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean avantRepriseErrGen(IFAPassage passage, BProcess context, String idModuleFacturation)
            throws Exception {
        return true;
    }

    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {

        CEProcessFacturationTaxeSommation procFacturation = new CEProcessFacturationTaxeSommation();
        // copier le process parent
        procFacturation.setParentWithCopy(context);

        FAPassage myPassage = (FAPassage) passage;
        procFacturation.setPassage(myPassage);
        procFacturation.setIdModuleFacturation(idModuleFacturation);
        procFacturation.setEMailAddress(context.getEMailAddress());
        procFacturation.setSendCompletionMail(false);
        procFacturation.executeProcess();
        // contrôler si le process a fonctionné
        if (!context.isAborted() && !procFacturation.isOnError()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean imprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean recomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean regenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return generer(passage, context, idModuleFacturation);
    }

    @Override
    public boolean repriseOnErrorCompta(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean repriseOnErrorGen(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return generer(passage, context, idModuleFacturation);
    }

    @Override
    public boolean supprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }
}
