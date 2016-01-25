package ch.globaz.al.businessimpl.echeances;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.Date;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.exceptions.protocoles.ALProtocoleException;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * 
 * @author PTA * Classe d'impl�mentation des m�thodes pour les avis �ch�ances li�es aux droits � imprimer et destin�s �
 *         l'allocataire � bonification indirect un avis d'�ch�ances est anvoyer directement � l'allocataire
 * 
 */

public class EcheancesAllocataireIndirectAvecAvisEcheance extends EcheancesAllocataireAbstract {

    @Override
    public DocumentData loadData(ArrayList<DroitEcheanceComplexModel> droits, String nss, String idTiersAllocataire,
            String numDossier, String numAffilie, String nomAlloc, String prenomAlloc, String titre,
            String idTiersBenficiaire) throws JadePersistenceException, JadeApplicationException {

        DocumentData document = super.loadData(droits, nss, idTiersAllocataire, numDossier, numAffilie, nomAlloc,
                prenomAlloc, titre, idTiersBenficiaire);

        DossierModel dossier = ALServiceLocator.getDossierModelService().read(numDossier);
        // ent�te de la caisse d'allocation familiale
        this.setIdEntete(document, dossier.getActiviteAllocataire(), ALConstDocument.DOCUMENT_ECHEANCE_ALLOC_IND,
                getLangueDocument());

        // adresse du tiers allocataire
        this.addDateAdresse(document, JadeDateUtil.getGlobazFormattedDate(new Date()), idTiersAllocataire,
                getLangueDocument(), null);

        setTableCopie(document, droits.get(0), getLangueDocument());

        // signature de la caisse AF
        this.setIdSignature(document, dossier.getActiviteAllocataire(), ALConstDocument.DOCUMENT_ECHEANCE_AFFILIE,
                getLangueDocument());

        return document;
    }

    @Override
    protected void setIdDocument(DocumentData document) throws JadeApplicationException {
        if (document == null) {
            throw new ALProtocoleException("EcheancesAllocataireDirect#setIdDocument : document is null");
        }

        document.addData("AL_idDocument", "AL_droitsEcheancesAllocataireDirect");
    }

}
