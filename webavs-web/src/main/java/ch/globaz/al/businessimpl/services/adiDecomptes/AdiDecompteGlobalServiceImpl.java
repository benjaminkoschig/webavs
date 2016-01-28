package ch.globaz.al.businessimpl.services.adiDecomptes;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.Date;
import ch.globaz.al.business.exceptions.adiDecomptes.ALAdiDecomptesException;
import ch.globaz.al.business.models.adi.AdiDecompteComplexModel;
import ch.globaz.al.business.services.adiDecomptes.AdiDecompteGlobalService;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Implémentation du service permettant la gestion des documents pour les décomptes globaux
 * 
 * @author PTA
 * 
 */
public class AdiDecompteGlobalServiceImpl extends AdiDecompteGlobalAbstractServiceImpl implements
        AdiDecompteGlobalService {

    @Override
    public DocumentData getDocument(AdiDecompteComplexModel decompteGlobal, String typeDecompte, DocumentData doc,
            String langueDocument) throws JadeApplicationException, JadePersistenceException {
        // String langueDocument = ALImplServiceLocator.getLangueAllocAffilieService().langueTiersAffilie(
        // decompteGlobal.getDossierComplexModel().getDossierModel().getNumeroAffilie());
        // FIXME reprendre le tiers originale de la copie
        // DocumentData doc = new DocumentData();
        // initialise le bon document
        setIdDocument(doc);

        // FIXME ajouter date Adresse tiers originale de la copie
        getDateAdresseOriginale(doc, decompteGlobal.getDossierComplexModel().getDossierModel().getIdDossier(),
                langueDocument, JadeDateUtil.getGlobazFormattedDate(new Date()));

        // ajoute les données communes au décompte global
        doc = super.getDocument(decompteGlobal, typeDecompte, doc, langueDocument);
        return doc;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.businessimpl.documents.AbstractDocument#setIdDocument(ch .globaz.topaz.datajuicer.DocumentData)
     */
    @Override
    protected void setIdDocument(DocumentData document) throws JadeApplicationException {
        // vérification paramètre

        if (document == null) {
            throw new ALAdiDecomptesException("AdiDecompteGlobDetIndirectServiceImpl#setIdDocument : document is null");
        }
        document.addData("AL_idDocument", "AL_adiDecompteGlobalIndirect");
    }

}
