package ch.globaz.al.businessimpl.echeances;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.exceptions.model.tarif.ALEcheanceModelException;
import ch.globaz.al.business.exceptions.protocoles.ALProtocoleException;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * 
 * Impl?mentation des services d'?ch?ances li?es aux droits ? imprimer et destin?s ? l'affili? indirect sans document
 * pour l'allocataire
 * 
 * avec texte2
 * 
 * @author PTA
 * 
 */

public class EcheancesAffilieIndirectSansDocAlloc extends EcheancesAffilieAbstract {

    @Override
    protected void setIdDocument(DocumentData document) throws JadeApplicationException {
        if (document == null) {
            throw new ALProtocoleException("EcheancesAffilieIndirectSansDocAlloc#setIdDocument : document is null");
        }

        document.addData("AL_idDocument", "AL_droitsEcheancesAffilieIndirectSansDocAlloc");
    }

    /**
     * M?thode ajoutant le texte de fin dans le document ? imprimer pour l'affili? indirect sans document pour
     * l'allocataire
     * 
     * @param document
     *            document ? imprimer
     * @throws JadePersistenceException
     *             Exception lev?e lorsque le chargement ou la mise ? jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev?e par la couche m?tier lorsqu'elle n'a pu effectuer l'op?ration souhait?e
     */

    @Override
    protected void setTexteFin(DocumentData document, String langueDocument) throws JadeApplicationException,
            JadePersistenceException {
        // v?rification du param?tre
        if (document == null) {
            throw new ALEcheanceModelException("EcheancesListeAffilieIndirect#setTexteFin: document is null");

        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALEcheanceModelException("EcheancesAffilieIndirect#setTexteFin: language  " + langueDocument
                    + " is not  valid ");
        }

        // texte disant que l'affili? doit transmettre des attestations le cas
        // ?ch?ant
        document.addData("droitEcheanceAffilieInd_texteFin",
                this.getText("al.echeances.AffilieAvisEcheance.texte.fin", langueDocument));
        // texte en gras remettre copie ?
        document.addData("droitEcheanceAffilieInd_texteCopie",
                this.getText("al.echeances.AffilieAvisEcheance.texteCopie", langueDocument));
        // texte revision
        document.addData("droitEcheanceAffilieInd_texteRevision",
                this.getText("al.echeances.AffilieAvisEcheance.texteRevision", langueDocument));
        // suite texte normal copie
        document.addData("droitEcheanceAffilieInd_texteCopieFin",
                this.getText("al.echeances.AffilieAvisEcheance.texteCopieFin", langueDocument));
        // texte mentionner nss
        document.addData("droitEcheanceAffilieInd_texteNss",
                this.getText("al.echeances.AffilieAvisEcheances.texteNSS", langueDocument));
        // texte annoncer fin/interruption ?tude
        document.addData("droitEcheanceAffilieInd_texteFinFormation",
                this.getText("al.echeances.AffilieAvisEcheance.texteFinFormation", langueDocument));

    }

}
