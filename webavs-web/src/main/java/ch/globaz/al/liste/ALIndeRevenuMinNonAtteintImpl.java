/**
 * 
 */
package ch.globaz.al.liste;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;
import ch.globaz.al.liste.process.ALIndeRevenuMinNonAtteintProcess;

/**
 * Module permettant de g�n�rer la liste des ind�pendants avec des AF n'ayant pas atteint le revenu minimal
 * 
 * @author est
 * 
 */
public class ALIndeRevenuMinNonAtteintImpl implements IntModuleFacturation {

    @Override
    public boolean avantRecomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean avantRegenerer(IFAPassage passage, BProcess context, String idModule) throws Exception {
        return false;
    }

    @Override
    public boolean avantRepriseErrCom(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean avantRepriseErrGen(IFAPassage passage, BProcess context, String idModule) throws Exception {
        return false;
    }

    /**
     * M�thode appel�e pour la g�n�ration et envoi de la liste.
     * 
     * @throws Exception
     * 
     */
    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {

        ALIndeRevenuMinNonAtteintProcess process = new ALIndeRevenuMinNonAtteintProcess();
        process.setSession(context.getSession());
        process.setNoPassage(passage.getIdPassage());
        process.setEMailAddress(context.getEMailAddress());
        process.executeProcess();

        // contr�ler si le process a fonctionn�
        return context.isAborted();
    }

    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModule) throws Exception {
        return false;
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
    public boolean regenerer(IFAPassage passage, BProcess context, String idModule) throws Exception {

        return false;
    }

    @Override
    public boolean repriseOnErrorCompta(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean repriseOnErrorGen(IFAPassage passage, BProcess context, String idModule) throws Exception {

        return false;
    }

    @Override
    public boolean supprimer(IFAPassage passage, BProcess context) throws Exception {

        return false;
    }
}