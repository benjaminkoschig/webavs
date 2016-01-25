package ch.globaz.al.businessimpl.echeances;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.exceptions.model.tarif.ALEcheanceModelException;
import ch.globaz.al.business.exceptions.protocoles.ALProtocoleException;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Implémentation des services d'échéances liées aux droits à imprimer et destinés à l'affilié
 * 
 * avec texte3
 * 
 * @author PTA
 * 
 */
public class EcheancesAffilieIndirect extends EcheancesAffilieAbstract {

    @Override
    protected void setIdDocument(DocumentData document) throws JadeApplicationException {
        if (document == null) {
            throw new ALProtocoleException("EcheancesAffilieIndirect#setIdDocument : document is null");
        }

        document.addData("AL_idDocument", "AL_droitsEcheancesAffilieIndirect");
    }

    /**
     * Méthode ajoutant le texte de fin dans le document à imprimer pour l'affilié indirect
     * 
     * @param document
     *            document à imprimer
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */

    @Override
    protected void setTexteFin(DocumentData document, String langueDocument) throws JadeApplicationException,
            JadePersistenceException {
        // vérification du paramètre
        if (document == null) {
            throw new ALEcheanceModelException("EcheancesListeAffilieIndirect#setTexteFin: document is null");

        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALEcheanceModelException("EcheancesAffilieIndirect#setTexteFin: language  " + langueDocument
                    + " is not  valid ");
        }

        // texte disant que l'affilié doit transmettre des attestations le cas
        // échéant
        document.addData("droitEcheanceAffilieInd_texteFin",
                this.getText("al.echeances.AffilieAvisEcheance.texte.fin", langueDocument));
        // texte en gras remettre copie à
        document.addData("droitEcheanceAffilieInd_texteCopie",
                this.getText("al.echeances.AffilieAvisEcheance.texteCopie", langueDocument));
        // suite texte normal copie
        document.addData("droitEcheanceAffilieInd_texteCopieFin",
                this.getText("al.echeances.AffilieAvisEcheance.texteCopieFin", langueDocument));
        // texte mentionner nss
        document.addData("droitEcheanceAffilieInd_texteNss",
                this.getText("al.echeances.AffilieAvisEcheances.texteNSS", langueDocument));
        // texte annoncer fin/interruption étude
        document.addData("droitEcheanceAffilieInd_texteFinFormation",
                this.getText("al.echeances.AffilieAvisEcheance.texteFinFormation", langueDocument));
        // texte disant que l'affilié reçoit les avis d'échéances à transmettre
        // à l'employé
        document.addData("droitEcheanceAffilieInd_texteAvecDocAlloc",
                this.getText("al.echeances.AffilieAvisEcheanceIndirectAvecDocument", langueDocument));

    }

}
