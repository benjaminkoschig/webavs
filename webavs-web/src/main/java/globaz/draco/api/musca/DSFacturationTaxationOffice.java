/*
 * Cr�� le 16 ao�t 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.draco.api.musca;

import globaz.draco.process.DSProcessFacturationTaxationOffice;
import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.external.IntModuleFacturation;

/**
 * @author MMO 17.06.2011
 * 
 */
public class DSFacturationTaxationOffice extends DSFacturationGenericImpl implements IntModuleFacturation {
    /**
	 * 
	 */
    public DSFacturationTaxationOffice() {
        super();
    }

    @Override
    public boolean avantRecomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    /**
     * Avant reg�n�rer, il faut effacer les afacts
     * 
     * @return true s'il faut effacer les afacts avant de reg�n�rer, false sinon
     */
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

        DSProcessFacturationTaxationOffice procFacturation = new DSProcessFacturationTaxationOffice();

        /**
         * Lors d'un setParentWithCopy() la transaction, le MemoryLog et la session sont partag�s De plus, il n'y a pas
         * d'envoi d'email. Le parent s'en occupe
         */
        procFacturation.setIdModuleFacturation(idModuleFacturation);
        procFacturation.setParentWithCopy(context);
        procFacturation.setPassage((FAPassage) passage);
        procFacturation.executeProcess();

        /**
         * Contr�le de la bonne ex�cution du process via le process parent En effet, � la sortie de la m�thode
         * executeProcess(), le process voit sa transaction et son parent mis � null Il n'est donc pas possible de le
         * contr�ler directement
         */
        return !(context.isAborted() || context.isOnError());
    }

    @Override
    public boolean imprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean recomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return comptabiliser(passage, context);
    }

    @Override
    public boolean regenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return generer(passage, context, idModuleFacturation);
    }

    @Override
    public boolean repriseOnErrorCompta(IFAPassage passage, BProcess context) throws Exception {
        return comptabiliser(passage, context);
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
