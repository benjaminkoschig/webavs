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
 * Classe d'implémentation des méthodes d'échéances liées aux droits à imprimer et destinés à l'allocataire à
 * bonification direct
 * 
 * @author PTA
 * 
 */
public class EcheancesAllocataireDirect extends EcheancesAllocataireAbstract {

    @Override
    public DocumentData loadData(ArrayList<DroitEcheanceComplexModel> droits, String nss, String idTiersAllocataire,
                                 String numDossier, String numAffilie, String nomAlloc, String prenomAlloc, String titre,
                                 String idTiersBenficiaire) throws JadePersistenceException, JadeApplicationException {
        // document à créer
        DocumentData document = super.loadData(droits, nss, idTiersAllocataire, numDossier, numAffilie, nomAlloc,
                prenomAlloc, titre, idTiersBenficiaire);
        DossierModel dossier = ALServiceLocator.getDossierModelService().read(numDossier);
        // entête de la caisse d'allocation familiale
        this.setIdEntete(document, dossier.getActiviteAllocataire(), ALConstDocument.DOCUMENT_ECHEANCE_ALLOC_DIR,
                super.getLangueDocument());

        this.addDateAdresse(document, JadeDateUtil.getGlobazFormattedDate(new Date()), idTiersBenficiaire,
                getLangueDocument(), null);

        setTableCopie(document, droits.get(0), getLangueDocument());
        // signature de la caisse AF
        this.setIdSignature(document, dossier.getActiviteAllocataire(), ALConstDocument.DOCUMENT_ECHEANCE_AFFILIE,
                super.getLangueDocument());

        return document;
    }

    @Override
    public DocumentData loadData(ArrayList<DroitEcheanceComplexModel> droits, String nss, String idTiersAllocataire,
                                 String numDossier, String numAffilie, String numContribuable, String nomAlloc, String prenomAlloc, String titre,
                                 String idTiersBenficiaire) throws JadePersistenceException, JadeApplicationException {
        // document à créer
        DocumentData document = super.loadData(droits, nss, idTiersAllocataire, numDossier, numAffilie, numContribuable, nomAlloc,
                prenomAlloc, titre, idTiersBenficiaire);
        DossierModel dossier = ALServiceLocator.getDossierModelService().read(numDossier);
        // entête de la caisse d'allocation familiale
        this.setIdEntete(document, dossier.getActiviteAllocataire(), ALConstDocument.DOCUMENT_ECHEANCE_ALLOC_DIR,
                super.getLangueDocument());

        this.addDateAdresse(document, JadeDateUtil.getGlobazFormattedDate(new Date()), idTiersBenficiaire,
                getLangueDocument(), null);

        setTableCopie(document, droits.get(0), getLangueDocument());
        // signature de la caisse AF
        this.setIdSignature(document, dossier.getActiviteAllocataire(), ALConstDocument.DOCUMENT_ECHEANCE_AFFILIE,
                super.getLangueDocument());

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
