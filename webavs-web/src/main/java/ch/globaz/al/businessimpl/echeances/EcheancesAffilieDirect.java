package ch.globaz.al.businessimpl.echeances;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.exceptions.model.tarif.ALEcheanceModelException;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Classe d'implémentation des méthodes d'échéances liées aux droits à imprimer et destinés à l'affilié dont
 * le(s)allocataire(s) sont à bonification direct ou avec adresse de paiement
 * 
 * avec texte1
 * 
 * @author PTA
 * 
 */
public class EcheancesAffilieDirect extends EcheancesAffilieAbstract {

    @Override
    protected void setIdDocument(DocumentData document) throws JadeApplicationException {
        if (document == null) {
            throw new ALEcheanceModelException("EcheancesAffilieDirect#setIdDocument : document is null");
        }

        document.addData("AL_idDocument", "AL_droitsEcheancesAffilieDirect");
    }

    /**
     * Méthode ajoutant le texte de fin dans le document à imprimer pour l'affilié dont le(s) allocataires sont à
     * paiement direct
     * 
     * @param document
     *            document à imprimer
     * @param langueDocument
     *            langue du document
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */

    @Override
    protected void setTexteFin(DocumentData document, String langueDocument) throws JadeApplicationException,
            JadePersistenceException {
        // contrôle des paramètres
        if (document == null) {
            throw new ALEcheanceModelException("EcheancesAffilieDirect#setTexteFin : document is null");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALEcheanceModelException("EcheancesAffilieDirect#setTexteFin: language  " + langueDocument
                    + " is not  valid ");
        }

        document.addData("droitEcheanceAffilieDir_texteFin",
                this.getText("al.echeances.AffilieAvisEcheanceDirect.texte.fin", langueDocument));
        // texte revision
        document.addData("droitEcheanceAffilieDir_texteRevision",
                this.getText("al.echeances.AffilieAvisEcheance.texteRevision", langueDocument));
    }

}
