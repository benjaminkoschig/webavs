package globaz.naos.api.musca;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;
import globaz.naos.process.taxeCo2.AFProcessFacturerTaxeCo2EnteteExiste;

/**
 * Insérez la description du type ici. Date de création : (20.08.2009 11:09:01)
 * 
 * @author: mar
 */
public class AFFacturationTaxeCo2EnteteExisteImpl extends AFFacturationGenericImpl implements IntModuleFacturation {
    /**  
 *
 */
    public AFFacturationTaxeCo2EnteteExisteImpl() {
        super();
    }

    @Override
    public boolean avantRecomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    /**
     * Avant regénérer, il faut effacer les afacts
     * 
     * @return true s'il faut effacer les afacts avant de regénérer, false sinon
     */
    @Override
    public boolean avantRegenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return false;
    }

    @Override
    public boolean avantRepriseErrCom(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean avantRepriseErrGen(IFAPassage passage, BProcess context, String idModuleFacturation)
            throws Exception {
        return false;
    }

    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {

        AFProcessFacturerTaxeCo2EnteteExiste procFacturation = new AFProcessFacturerTaxeCo2EnteteExiste();

        // copier le process parent
        procFacturation.setParentWithCopy(context);
        procFacturation.setPassage(passage);
        procFacturation.setEMailAddress(context.getEMailAddress());
        procFacturation.setIdModuleFacturation(idModuleFacturation);
        procFacturation.setPassage(passage);
        procFacturation.executeProcess();

        // contrôler si le process a fonctionné
        if (!context.isAborted()) {
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
