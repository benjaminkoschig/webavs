package ch.globaz.al.businessimpl.echeances;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.exceptions.model.tarif.ALEcheanceModelException;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Classe d'impl�mentation des m�thodes d'�ch�ances li�es aux droits � imprimer et destin�s � l'affili� dont
 * le(s)allocataire(s) sont � bonification direct ou avec adresse de paiement
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
     * M�thode ajoutant le texte de fin dans le document � imprimer pour l'affili� dont le(s) allocataires sont �
     * paiement direct
     * 
     * @param document
     *            document � imprimer
     * @param langueDocument
     *            langue du document
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */

    @Override
    protected void setTexteFin(DocumentData document, String langueDocument) throws JadeApplicationException,
            JadePersistenceException {
        // contr�le des param�tres
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
