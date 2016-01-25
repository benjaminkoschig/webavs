package ch.globaz.al.businessimpl.services.adiDecomptes;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.Date;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALConstAdiDecomptes;
import ch.globaz.al.business.exceptions.adiDecomptes.ALAdiDecomptesException;
import ch.globaz.al.business.models.adi.AdiDecompteComplexModel;
import ch.globaz.al.business.models.dossier.DossierSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.adiDecomptes.AdiDecompteService;
import ch.globaz.al.business.services.adiDecomptes.AdiDecomptesService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Classe d'implémentation du service AdiDecomptesService
 * 
 * @author PTA
 * 
 */
public class AdiDecomptesServiceImpl implements AdiDecomptesService {

    /**
     * 
     * @param docContainer
     *            Container du docuemtn
     * @param adiDecompte
     *            decompte ADi
     * @param isGed
     *            boolean pour l'envoi en ged ou non
     * 
     * @return {@link JadePublishDocumentInfo}
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée FIXME: ne
     *             semble plus utilisé ?? voir pour "nettoyer" code
     */
    private JadePublishDocumentInfo gedInfoDocAdi(JadePrintDocumentContainer docContainer,
            AdiDecompteComplexModel adiDecompte, boolean isGed) throws JadeApplicationException
    /* ALRecapitulatifEntrepriseImpressionModelException */{
        // méthode pour insérer les infos de la ged pour chaque document

        String nssAllocataire = adiDecompte.getDossierComplexModel().getAllocataireComplexModel()
                .getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel();
        String numAffilie = adiDecompte.getDossierComplexModel().getDossierModel().getNumeroAffilie();
        // préparation pour le document envoyé en GED
        JadePublishDocumentInfo archiveInfo = new JadePublishDocumentInfo();
        // archivage du document
        archiveInfo.setArchiveDocument(isGed);
        // publication du document
        archiveInfo.setPublishDocument(false);
        // type de document
        // FIXME (lot 2) devrait contenir le numéro de document InfoRom
        archiveInfo.setDocumentType("Adi");
        // numéro de type de document
        // FIXME (lot 2) devrait contenir le numéro de document InfoRom
        archiveInfo.setDocumentTypeNumber("Adi");
        // propriétaire du document
        archiveInfo.setOwnerId(JadeThread.currentUserId());
        // email du propriétaire du document
        archiveInfo.setOwnerEmail(JadeThread.currentUserEmail());
        // date document document
        archiveInfo.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
        // titre du document
        archiveInfo.setDocumentTitle((JadeThread.getMessage("al.adi.decomptes.titre.attestation")));
        // sujet du document
        archiveInfo.setDocumentSubject((JadeThread.getMessage("al.adi.decomptes.titre.attestation")));

        archiveInfo.setPublishProperty("numero.role.formatte", numAffilie);
        archiveInfo.setPublishProperty("numero.affilie.formatte", numAffilie);
        archiveInfo.setPublishProperty("numero.role.non.formatte", JadeStringUtil.removeChar(numAffilie, '.'));
        archiveInfo.setPublishProperty("pyxis.tiers.numero.avs.non.formatte",
                JadeStringUtil.removeChar(nssAllocataire, '.'));
        archiveInfo.setPublishProperty("numero.avs.formatte", nssAllocataire);
        // setting des type ou type sous dossier?
        archiveInfo.setPublishProperty(
                "type.dossier",
                ALServiceLocator.getGedBusinessService().getTypeSousDossier(
                        adiDecompte.getDossierComplexModel().getDossierModel()));

        return archiveInfo;

    }

    @Override
    public JadePrintDocumentContainer getAdiDecompteDossier(String idDecompteAdi, String typeDecompte, boolean isGed)
            throws JadeApplicationException, JadePersistenceException {
        // vérification des paramètres
        if (!JadeStringUtil.equals(ALConstAdiDecomptes.ADI_DECOMPTE_ALL, typeDecompte, false)
                && !JadeStringUtil.equals(ALConstAdiDecomptes.ADI_DECOMPTE_DETAILLE, typeDecompte, false)
                && !JadeStringUtil.equals(ALConstAdiDecomptes.ADI_DECOMPTE_GLOBAL, typeDecompte, false)) {
            throw new ALAdiDecomptesException("AdiDecomptesServiceImpl#getAdiDecompteDossier: " + typeDecompte
                    + " is not a valid type");
        }
        if (!JadeNumericUtil.isIntegerPositif(idDecompteAdi)) {
            throw new ALAdiDecomptesException("AdiDecomptesServiceImpl#getAdiDecompteDossier: " + idDecompteAdi
                    + " is not valid, is not a positif integer");
        }

        AdiDecompteComplexModel adiDecompte = ALImplServiceLocator.getAdiDecompteComplexModelService().read(
                idDecompteAdi);

        return getDocuments(adiDecompte, typeDecompte, isGed);

    }

    @Override
    public JadePrintDocumentContainer getAdiDecomptesAll(String periode, String typeDecompte)
            throws JadeApplicationException, JadePersistenceException {
        JadePrintDocumentContainer docContainer = new JadePrintDocumentContainer();

        DossierSearchModel dossierSearch = new DossierSearchModel();
        dossierSearch.setForStatut(ALCSDossier.STATUT_IS);
        dossierSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        return docContainer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.adiDecomptes.AdiDecomptesService#getDocuments
     * (ch.globaz.al.business.models.adi.AdiDecompteComplexModel, java.lang.String)
     */
    @Override
    public JadePrintDocumentContainer getDocuments(AdiDecompteComplexModel adiDecompte, String typeDecompte,
            boolean isGed) throws JadeApplicationException, JadePersistenceException {
        JadePrintDocumentContainer container = new JadePrintDocumentContainer();
        // contrôle des paramètres
        if (adiDecompte == null) {
            throw new ALAdiDecomptesException("AdiDecomptesServiceImpl#getDocuments: adiDecompte is null");
        }
        if (!JadeStringUtil.equals(ALConstAdiDecomptes.ADI_DECOMPTE_ALL, typeDecompte, false)
                && !JadeStringUtil.equals(ALConstAdiDecomptes.ADI_DECOMPTE_DETAILLE, typeDecompte, false)
                && !JadeStringUtil.equals(ALConstAdiDecomptes.ADI_DECOMPTE_GLOBAL, typeDecompte, false)) {
            throw new ALAdiDecomptesException("AdiDecomptesServiceImpl#getDocuments: " + typeDecompte
                    + " is not a valid type");
        }

        DocumentData doc = new DocumentData();

        // langue pour les documents
        String langueDocument = null;
        // Si langue reprise langue affilié
        if (adiDecompte.getDossierComplexModel().getAllocataireComplexModel().getAllocataireModel().getLangueAffilie()) {
            langueDocument = ALServiceLocator.getLangueAllocAffilieService().langueTiersAffilie(
                    adiDecompte.getDossierComplexModel().getDossierModel().getNumeroAffilie());
        }// si reprise langue allocataire
        else {
            langueDocument = ALServiceLocator.getLangueAllocAffilieService().langueTiersAlloc(
                    adiDecompte.getDossierComplexModel().getAllocataireComplexModel().getAllocataireModel()
                            .getIdTiersAllocataire(),
                    adiDecompte.getDossierComplexModel().getDossierModel().getNumeroAffilie());
        }

        if (JadeStringUtil.equals(ALConstAdiDecomptes.ADI_DECOMPTE_ALL, typeDecompte, false)) {

            // provider pour décompte détaillé
            AdiDecompteService adiDecompteDetail = AdiDecompteProvider.getAdiDecompteService(adiDecompte,
                    ALConstAdiDecomptes.ADI_DECOMPTE_DETAILLE);

            DocumentData docDetaill = new DocumentData();

            container.addDocument(adiDecompteDetail.getDocument(adiDecompte, ALConstAdiDecomptes.ADI_DECOMPTE_DETAILLE,
                    docDetaill, langueDocument), null);

            DocumentData docGlobal = new DocumentData();

            // provider pour décompte global
            AdiDecompteService adiDecompteGlobal = AdiDecompteProvider.getAdiDecompteService(adiDecompte,
                    ALConstAdiDecomptes.ADI_DECOMPTE_GLOBAL);

            container.addDocument(adiDecompteGlobal.getDocument(adiDecompte, ALConstAdiDecomptes.ADI_DECOMPTE_GLOBAL,
                    docGlobal, langueDocument), null);

        } else {
            AdiDecompteService adiDecompt = AdiDecompteProvider.getAdiDecompteService(adiDecompte, typeDecompte);

            container.addDocument(adiDecompt.getDocument(adiDecompte, typeDecompte, doc, langueDocument), null);

        }

        return container;
    }
}
