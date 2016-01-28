package ch.globaz.al.businessimpl.services.adiDecomptes;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.exceptions.adiDecomptes.ALAdiDecomptesException;
import ch.globaz.al.business.models.adi.AdiDecompteComplexModel;
import ch.globaz.al.business.services.adiDecomptes.AdiDecompteDetailleService;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Classe d'implémentation des services d'un décompte Adi détaillé
 * 
 * @author PTA
 * 
 */
public class AdiDecompteDetailleServiceImpl extends AdiDecompteDetailleAbstractServiceImpl implements
        AdiDecompteDetailleService {

    @Override
    public DocumentData getDocument(
    /* DossierComplexModel dossier */AdiDecompteComplexModel decompteGlobal,
    /* String periode, */String typeDecompte, DocumentData doc, String langueDocument) throws JadeApplicationException,
            JadePersistenceException {
        // DocumentData doc = new DocumentData();
        setIdDocument(doc);

        doc = super.getDocument(decompteGlobal,/* periode, */typeDecompte, doc, langueDocument);
        return doc;
    }

    @Override
    protected void setIdDocument(DocumentData document) throws JadeApplicationException {
        // vérification paramètre

        if (document == null) {
            throw new ALAdiDecomptesException("AdiDecompteDetailleIndirectServiceImpl#setIdDocument : document is null");
        }
        document.addData("AL_idDocument", "AL_adiDecompteDetailleIndirect");
    }

}
