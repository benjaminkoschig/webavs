package ch.globaz.vulpecula.facturation;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.external.IntModuleFacturation;

public abstract class PTFacturationGenericImpl implements IntModuleFacturation {
    public PTFacturationGenericImpl() {
        super();
    }

    @Override
    public boolean avantRecomptabiliser(final IFAPassage passage, final BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean avantRegenerer(final IFAPassage passage, final BProcess context, String idModule) throws Exception {
        return false;
    }

    @Override
    public boolean avantRepriseErrCom(final IFAPassage passage, final BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean avantRepriseErrGen(final IFAPassage passage, final BProcess context, String idModule)
            throws Exception {
        return false;
    }

    @Override
    public boolean comptabiliser(final IFAPassage passage, final BProcess context) throws Exception {
        if (passage == null) {
            throw new NullPointerException("Le passage de facturation est null !!");
        }
        if (context == null) {
            throw new NullPointerException("Le BProcess context est null !!");
        }

        PTProcessFacturation procFacturation = getProcessComptabilisation();
        // copier le process parent
        procFacturation.setParentWithCopy(context);
        FAPassage myPassage = (FAPassage) passage;
        procFacturation.setPassage(myPassage);
        procFacturation.setEMailAddress(context.getEMailAddress());
        procFacturation.setSendCompletionMail(false);
        procFacturation.executeProcess();
        // contrôler si le process a fonctionné
        return !context.isAborted();
    }

    @Override
    public boolean generer(final IFAPassage passage, final BProcess context, String idModule) throws Exception {
        if (passage == null) {
            throw new NullPointerException("Le passage de facturation est null !!");
        }
        if (context == null) {
            throw new NullPointerException("Le BProcess context est null !!");
        }

        PTProcessFacturation procFacturation = getProcessGeneration();
        // copier le process parent
        procFacturation.setParentWithCopy(context);
        FAPassage myPassage = (FAPassage) passage;
        procFacturation.setIdModuleFacturation(idModule);
        procFacturation.setPassage(myPassage);
        procFacturation.setEMailAddress(context.getEMailAddress());
        procFacturation.setSendCompletionMail(false);
        procFacturation.executeProcess();

        // contrôler si le process a fonctionné
        return !context.isAborted();
    }

    /**
     * Retourne le processus utilisé pour comptabiliser le passage en facturation
     * 
     * @return Classe étendant PTProcessFacturation (BProcess)
     */
    public abstract PTProcessFacturation getProcessComptabilisation();

    /**
     * Retourne le processus utilisé pour générer le passage en facturation
     * 
     * @return Classe étendant PTProcessFacturation (BProcess)
     */
    public abstract PTProcessFacturation getProcessGeneration();

    @Override
    public boolean imprimer(final IFAPassage passage, final BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean recomptabiliser(final IFAPassage passage, final BProcess context) throws Exception {
        return comptabiliser(passage, context);
    }

    @Override
    public boolean regenerer(final IFAPassage passage, final BProcess context, String idModule) throws Exception {
        return generer(passage, context, idModule);
    }

    @Override
    public boolean repriseOnErrorCompta(final IFAPassage passage, final BProcess context) throws Exception {
        return comptabiliser(passage, context);
    }

    @Override
    public boolean repriseOnErrorGen(final IFAPassage passage, final BProcess context, String idModule)
            throws Exception {
        return generer(passage, context, idModule);
    }

    @Override
    public boolean supprimer(final IFAPassage passage, final BProcess context) throws Exception {
        return false;
    }
}
