package ch.globaz.al.businessimpl.echeances;

import globaz.globall.parameters.FWParametersSystemCode;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import ch.globaz.al.business.exceptions.protocoles.ALProtocoleException;
import ch.globaz.al.utils.ALDateUtils;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Classe d'implémentation des méthodes pour les avis échéances liées aux droits à imprimer et destinés à l'allocataire
 * à bonification indirect
 * 
 * @author PTA
 * 
 */
public class EcheancesAllocataireViaAffilie extends EcheancesAllocataireAbstract {

    @Override
    public DocumentData loadData(ArrayList droits, String nss, String idTiersAllocataire, String numDossier,
                                 String numAffilie, String nomAlloc, String prenomAlloc, String titre, String idTiersBenficiaire)
            throws JadePersistenceException, JadeApplicationException {

        DocumentData document = super.loadData(droits, nss, idTiersAllocataire, numDossier, numAffilie, nomAlloc,
                prenomAlloc, titre, idTiersBenficiaire);

        String dateCourante = ALDateUtils.getDateCourante();
        // infos relatives à l'idTiers de l'affilie
        String idTiersAffilie = AFBusinessServiceLocator.getAffiliationService()
                .findIdTiersForNumeroAffilie(numAffilie);

        addAdresse(document, idTiersAffilie, dateCourante);

        // idLangueCodeSyteme titre
        FWParametersSystemCode cs = new FWParametersSystemCode();
        // recherche des données liées au code système du titre
        cs.getCode(titre);
        String titreAlloc = cs.getCodeUtilisateur(getLangueCodeSystem(getLangueDocument())).getLibelle();

        addAdresseLibre(document, dateCourante, titreAlloc + "\n" + nomAlloc + " " + prenomAlloc, idTiersAffilie,
                getLangueDocument());

        return document;
    }

    @Override
    public DocumentData loadData(ArrayList droits, String nss, String idTiersAllocataire, String numDossier,
                                 String numAffilie, String numContribuable, String nomAlloc, String prenomAlloc, String titre, String idTiersBenficiaire)
            throws JadePersistenceException, JadeApplicationException {

        DocumentData document = super.loadData(droits, nss, idTiersAllocataire, numDossier, numAffilie, numContribuable, nomAlloc,
                prenomAlloc, titre, idTiersBenficiaire);

        String dateCourante = ALDateUtils.getDateCourante();
        // infos relatives à l'idTiers de l'affilie
        String idTiersAffilie = AFBusinessServiceLocator.getAffiliationService()
                .findIdTiersForNumeroAffilie(numAffilie);

        addAdresse(document, idTiersAffilie, dateCourante);

        // idLangueCodeSyteme titre
        FWParametersSystemCode cs = new FWParametersSystemCode();
        // recherche des données liées au code système du titre
        cs.getCode(titre);
        String titreAlloc = cs.getCodeUtilisateur(getLangueCodeSystem(getLangueDocument())).getLibelle();

        addAdresseLibre(document, dateCourante, titreAlloc + "\n" + nomAlloc + " " + prenomAlloc, idTiersAffilie,
                getLangueDocument());

        return document;
    }

    @Override
    protected void setIdDocument(DocumentData document) throws JadeApplicationException {
        if (document == null) {
            throw new ALProtocoleException("EcheancesAllocataireViaAffilie#setIdDocument : document is null");
        }

        document.addData("AL_idDocument", "AL_droitsEcheancesAllocataireIndirect");
    }
}
