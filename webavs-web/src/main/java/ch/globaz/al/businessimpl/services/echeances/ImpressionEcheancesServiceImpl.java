package ch.globaz.al.businessimpl.services.echeances;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import ch.globaz.al.business.constantes.ALCSAffilie;
import ch.globaz.al.business.constantes.ALCSCopie;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.constantes.ALConstAttributsEntite;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.constantes.ALConstProtocoles;
import ch.globaz.al.business.exceptions.echeances.ALEcheancesException;
import ch.globaz.al.business.exceptions.model.prestation.ALRecapitulatifEntrepriseImpressionModelException;
import ch.globaz.al.business.exceptions.model.tarif.ALEcheanceModelException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.attribut.AttributEntiteModel;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.documents.DocumentDataContainer;
import ch.globaz.al.business.services.echeances.ImpressionEcheancesService;
import ch.globaz.al.businessimpl.echeances.EcheancesAffilie;
import ch.globaz.al.businessimpl.echeances.EcheancesAffilieFactory;
import ch.globaz.al.businessimpl.echeances.EcheancesAllocataire;
import ch.globaz.al.businessimpl.echeances.EcheancesAllocataireDirect;
import ch.globaz.al.businessimpl.echeances.EcheancesAllocataireFactory;
import ch.globaz.al.utils.ALDeepCopy;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Classe d'impl�mentation du service d'impression des droits arrivant � �ch�ance � imprimer
 * 
 * @author PTA
 */
public class ImpressionEcheancesServiceImpl implements ImpressionEcheancesService {

    /**
     * Conteneur pour la liste des affili�s et allocataires (liste de l'affili� suivi des avis destin�s aux
     * allocataires)
     */
    private JadePrintDocumentContainer containerAffAllocs;
    /**
     * conteneur pour la liste des affili�s uniquement (l'avis �cheance allocataire n'est pas g�n�r� ou directement
     * adress� � l'ayant droit)
     */
    private JadePrintDocumentContainer containerAffSeuls;

    /**
     * Conteneur pour la liste des allocataires uniquement (pour les cas o� l'avis d'�ch�ance ne passe pas par
     * l'affili�)
     */
    private JadePrintDocumentContainer containerAllocSeuls;
    /**
     * Conteneur pour la liste des copies des avis d'�ch�ances (lettre d'accompagnemant et copie de l'avis d'�ch�ance ou
     * autre copies)
     */
    private JadePrintDocumentContainer containerCopiesAlloc;

    /**
     * M�thode ajoutant le document au conteneur ad�quat selon le cas de figure
     * 
     * @param documentToAdd
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private void addInSuitableDocumentContainer(DocumentData documentToAdd, AttributEntiteModel attrAffilieTypeEch,
            boolean isTypeDocAffilie, boolean docAffilieGenerated, JadePublishDocumentInfo pubInfos, boolean docAllocSeul)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {

        if(docAllocSeul) {
            containerAllocSeuls.addDocument(documentToAdd, pubInfos);
        // si l'attribut est liste + lettres allocataires
        // ET
        // (document est un docAffili� OU (doc est un docAlloc ET un docAffili� a �t� g�n�r�)),
        } else if (ALCSAffilie.ATTRIBUT_AVIS_ECH_AFFILIE.equals(attrAffilieTypeEch.getValeurAlpha())
                && (isTypeDocAffilie || (!isTypeDocAffilie && docAffilieGenerated))) {
            containerAffAllocs.addDocument(documentToAdd, pubInfos);
        } else {
            if (isTypeDocAffilie) {
                containerAffSeuls.addDocument(documentToAdd, pubInfos);
            } else {
                containerAllocSeuls.addDocument(documentToAdd, pubInfos);
            }
        }
    }

    private JadePublishDocumentInfo fillDocInfos(ArrayList<DroitEcheanceComplexModel> droitsList, boolean docAffilie,
            String increment) throws Exception {

        JadePublishDocumentInfo pubInfos = new JadePublishDocumentInfo();
        pubInfos.setOwnerId(JadeThread.currentUserId());
        pubInfos.setOwnerEmail(JadeThread.currentUserEmail());
        pubInfos.setDocumentType("AvisEcheances");
        pubInfos.setPublishProperty("numero.affilie.formatte", droitsList.get(0).getNumAffilie());
        pubInfos.setDocumentProperty("numero.affilie.formatte", droitsList.get(0).getNumAffilie());
        pubInfos.setDocumentProperty("numero.affilie.increment", droitsList.get(0).getNumAffilie() + "-" + increment);

        // Collecte des NSS stock�s dans la liste pour savoir si on renseigne le NSS (si unique, doc alloc pour affili�)
        // ou si on met 0 car plusieurs diff�rents (doc affili�)
        List<String> allNssFromList = new ArrayList<String>();

        for (DroitEcheanceComplexModel droit : droitsList) {
            if (!allNssFromList.contains(droit.getNumNss())) {
                allNssFromList.add(droit.getNumNss());
            }
        }

        if (allNssFromList.size() == 0) {
            throw new ALEcheancesException("ImpressionEcheancesServiceImpl#fillDocInfos: droitsList contains no NSS");
        }

        if (!docAffilie) {

            pubInfos.setDocumentTypeNumber(ALConstDocument.DOC_TYPE_NUMBER_INFOROM_ECHEANCE_ALLOCATAIRE);
            pubInfos.setPublishProperty("numero.avs.formatte", allNssFromList.get(0));
            pubInfos.setPublishProperty(
                    "type.dossier",
                    ALServiceLocator.getGedBusinessService().getTypeSousDossier(
                            ALServiceLocator.getDossierModelService().read(
                                    droitsList.get(0).getDroitModel().getIdDossier())));

            TIBusinessServiceLocator.getDocInfoService().fill(pubInfos, droitsList.get(0).getIdTiersAllocataire(),
                    null, null, ALCSTiers.ROLE_AF, droitsList.get(0).getNumAffilie(),
                    JadeStringUtil.removeChar(droitsList.get(0).getNumAffilie(), '.'), null);
        } else {
            pubInfos.setDocumentTypeNumber(ALConstDocument.DOC_TYPE_NUMBER_INFOROM_ECHEANCE_AFFILIE);

            String idTiers = AFBusinessServiceLocator.getAffiliationService().findIdTiersForNumeroAffilie(
                    droitsList.get(0).getNumAffilie());
            String numAffilieNonFormatte = JadeStringUtil.removeChar(droitsList.get(0).getNumAffilie(), '.');
            try {
                TIBusinessServiceLocator.getDocInfoService().fill(pubInfos, idTiers, ALCSTiers.ROLE_AF,
                        droitsList.get(0).getNumAffilie(), numAffilieNonFormatte);
            } catch (Exception e) {
                throw new ALRecapitulatifEntrepriseImpressionModelException(
                        "ImpressionEcheancesServiceImpl#fillDocInfos : unable to fill DocInfoService", e);
            }

        }

        // FIXME: bz 8229
        if (allNssFromList.size() == 1) {

            pubInfos.setPublishProperty("pyxis.tiers.numero.avs.non.formatte",
                    JadeStringUtil.removeChar(allNssFromList.get(0), '.'));
            pubInfos.setPublishProperty("numero.avs.formatte", allNssFromList.get(0));

        } else {
            pubInfos.setPublishProperty("pyxis.tiers.numero.avs.non.formatte", "0");
            pubInfos.setPublishProperty("numero.avs.formatte", "0");
        }

        // date
        pubInfos.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
        // archivage des documents
        pubInfos.setArchiveDocument(true);
        // publication du document
        pubInfos.setPublishDocument(false);

        return pubInfos;

    }

    private boolean isDocAffilieRequired(String activiteAlloc) throws JadeApplicationException,
            JadePersistenceException {

        return ALCSDossier.ACTIVITE_COLLAB_AGRICOLE.equals(activiteAlloc)
                || ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE.equals(activiteAlloc)
                || ALCSDossier.ACTIVITE_SALARIE.equals(activiteAlloc);

    }

    private boolean isNextAffilieDifferent(ArrayList<DroitEcheanceComplexModel> droitsAffilie, int currentIndex) {
        // si c'est le dernier
        if ((currentIndex + 1) > droitsAffilie.size()) {
            throw new IllegalArgumentException(
                    "l'index pass� en param�tre repr�sente le dernier �l�ment de la liste test�e. Test impossible.");
        } else {

            boolean isCurrentDossierDirect = JadeNumericUtil.isEmptyOrZero(droitsAffilie.get(currentIndex)
                    .getIdTiersBeneficiaire()) ? false : true;
            boolean isNextDossierDirect = JadeNumericUtil.isEmptyOrZero(droitsAffilie.get(currentIndex + 1)
                    .getIdTiersBeneficiaire()) ? false : true;
            boolean modePaiementDifferent = isCurrentDossierDirect == isNextDossierDirect ? false : true;

            String currentNumAffilie = droitsAffilie.get(currentIndex).getNumAffilie();
            String nextNumAffilie = droitsAffilie.get(currentIndex + 1).getNumAffilie();
            boolean numAffilieDifferent = currentNumAffilie.equals(nextNumAffilie) ? false : true;
            // on retourne true si une des 2 vaut true
            return (modePaiementDifferent || numAffilieDifferent);
        }

    }

    private boolean isNextAllocataireDifferent(ArrayList<DroitEcheanceComplexModel> droitsAffilie, int currentIndex) {
        if ((currentIndex + 1) > droitsAffilie.size()) {
            throw new IllegalArgumentException(
                    "l'index pass� en param�tre repr�sente le dernier �l�ment de la liste test�e. Test impossible.");
        } else {
            return !(droitsAffilie.get(currentIndex).getIdTiersAllocataire().equals(droitsAffilie.get(currentIndex + 1)
                    .getIdTiersAllocataire()));
        }

    }

    @Override
    public JadePrintDocumentContainer loadDocuments(ArrayList<DroitEcheanceComplexModel> droitsResult,
            String dateEcheance, boolean printCopieAllocPourDossierBeneficiaire, ProtocoleLogger logger,
            boolean allocSeul)
            throws ALEcheanceModelException {

        containerAffAllocs = new JadePrintDocumentContainer();
        containerAffSeuls = new JadePrintDocumentContainer();
        containerAllocSeuls = new JadePrintDocumentContainer();
        containerCopiesAlloc = new JadePrintDocumentContainer();

        /**
         * Conteneur d�finitif
         */
        JadePrintDocumentContainer containerDefinitif = new JadePrintDocumentContainer();

        // v�rification du param�tre
        if (droitsResult == null) {
            throw new ALEcheanceModelException("ImpressionEcheancesServiceImpl#loadDocuments : droitResult is null");
        }

        // tableau temporaire contient des DroitEcheancheComplexModel
        ArrayList<DroitEcheanceComplexModel> droitsAffilieTemp = new ArrayList<DroitEcheanceComplexModel>();

        // boucle liste affili�s
        int incrementAffilie = 0;
        for (int i = 0; i < droitsResult.size(); i++) {

            DroitEcheanceComplexModel droitModel = droitsResult.get(i);
            droitsAffilieTemp.add(droitModel);

            // si le prochain affili� est consid�r� comme diff�rent OU que c'est le dernier droit de la liste
            if (((i == (droitsResult.size() - 1)) || isNextAffilieDifferent(droitsResult, i))) {
                try {
                    DocumentData docAffilie = null;
                    boolean docAffilieGenerated = false;
                    // recherche le type d'avis d'�ch�ance destin� � l'allocataire
                    AttributEntiteModel attributAffilieTypeAvisEch = ALServiceLocator.getAttributEntiteModelService()
                            .getAttributAffilieByNumAffilie(ALConstAttributsEntite.AVIS_ECHEANCE_DESTINATAIRE,
                                    droitModel.getNumAffilie());

                    if (isDocAffilieRequired(droitModel.getActiviteAllocataire()) && !allocSeul) {
                        docAffilieGenerated = true;
                        EcheancesAffilie echAffilie = EcheancesAffilieFactory
                                .getEcheanceListeAffilie(attributAffilieTypeAvisEch);

                        docAffilie = echAffilie.loadData(droitsAffilieTemp, droitsAffilieTemp.get(0).getNumAffilie(),
                                droitsAffilieTemp.get(0).getActiviteAllocataire());


                        JadePublishDocumentInfo pubInfos = fillDocInfos(droitsAffilieTemp, true,
                                Integer.toString(incrementAffilie));

                        addInSuitableDocumentContainer(docAffilie, attributAffilieTypeAvisEch, true, true, pubInfos, false);

                    }
                    // pas besoin de g�n�rer les avis �ch�ances allocataires si le param�tre est liste r�capitulative ET
                    // qu'on a g�n�r� un doc affili�
                    // cas o� l'allocataire != affili� => seul liste r�cap (salari�,collabo.agri,trav.agri)
                    // le cas inverse est on g�n�re pas la liste r�cap mais seul avis � l'allocataire, car c'est aussi
                    // lui
                    // l'affili�
                    // (agri,expl.alpage,ind.,non-actif,p�cheur,tse)
                    if (!(ALCSAffilie.ATTRIBUT_SANS_AVIS_ECH.equals(attributAffilieTypeAvisEch.getValeurAlpha()) && isDocAffilieRequired(droitModel
                            .getActiviteAllocataire()))) {
                        traiterAvisAllocatairesDeAffilie(droitsAffilieTemp, attributAffilieTypeAvisEch,
                                docAffilieGenerated, printCopieAllocPourDossierBeneficiaire, logger,
                                Integer.toString(incrementAffilie), allocSeul);
                    }
                } catch (Exception e) {
                    String idDossier = droitModel.getDroitModel().getIdDossier();
                    try {
                        logger.getErrorsLogger(idDossier, "Dossier #" + idDossier)
                                .addMessage(
                                        new JadeBusinessMessage(
                                                JadeBusinessMessageLevels.ERROR,
                                                ImpressionEcheancesServiceImpl.class.getName(),
                                                droitModel.getDroitModel().getIdDossier()
                                                        + JadeI18n
                                                                .getInstance()
                                                                .getMessage(
                                                                        JadeThread.currentLanguage(),
                                                                        "al.protocoles.impressionAvisEcheances.erreur.document.dossierId",
                                                                        null)));
                    } catch (JadeApplicationException e1) {
                        JadeLogger.error(this,
                                "Une erreur s'est produite pendant la g�n�ration des avis d'�ch�ances de l'affili� n� "
                                        + droitModel.getNumAffilie() + " : " + e.getMessage());
                    }

                }
                droitsAffilieTemp.clear();
            }
            incrementAffilie++;
        }

        containerAffSeuls.copyDocsTo(containerDefinitif);
        containerAllocSeuls.copyDocsTo(containerDefinitif);
        containerCopiesAlloc.copyDocsTo(containerDefinitif);
        containerAffAllocs.copyDocsTo(containerDefinitif);

        // Ajoute le logger au container d�finitif avec les infos
        // liste des param�tres pour protocole Declaration versement
        HashMap<String, String> params = new HashMap<String, String>();
        // infos li�es au protocole de dclaration de versement
        // passage pour l'instant en dur, � voir par la suite
        params.put(ALConstProtocoles.INFO_PASSAGE, "123466");
        params.put(ALConstProtocoles.INFO_PROCESSUS, "al.protocoles.impressionAvisEcheances.info.processus.val");
        params.put(ALConstProtocoles.INFO_TRAITEMENT, "al.protocoles.impressionAvisEcheances.info.traitement.val");
        params.put(ALConstProtocoles.INFO_PERIODE, dateEcheance);

        return containerDefinitif;

    }

    private void traiterAvisAllocatairesDeAffilie(ArrayList<DroitEcheanceComplexModel> droitsAffilie,
            AttributEntiteModel attributAffilieTypeAvisEch, boolean docAffilieGenerated, boolean printCopieAlloc,
            ProtocoleLogger logger, String incrementAffilie, boolean allocSeul) throws JadePersistenceException, JadeApplicationException {

        ArrayList<DroitEcheanceComplexModel> droitsAllocTemp = new ArrayList<DroitEcheanceComplexModel>();

        // incrementer pour les paiements directs
        int incrementPourPaiementDirect = 0;

        if(allocSeul) {
            docAffilieGenerated = true;
        }

        // boucle sur la liste des enfants des allocataires de l'affili�
        for (int i = 0; i < droitsAffilie.size(); i++) {
            DroitEcheanceComplexModel droitModelAlloc = droitsAffilie.get(i);
            // ajoute le droit � la liste provisoire
            droitsAllocTemp.add(droitModelAlloc);
            // imprime le document si l'allocataire suivant est diff�rent ou si c'est le dernier de la liste
            if (((i == (droitsAffilie.size() - 1)) || isNextAllocataireDifferent(droitsAffilie, i))) {

                // cr�ation du document
                EcheancesAllocataire echAlloc = EcheancesAllocataireFactory.getEcheanceListeAllocataire(
                        droitModelAlloc, attributAffilieTypeAvisEch, docAffilieGenerated);

                DocumentData docAlloc = echAlloc.loadData(droitsAllocTemp, droitModelAlloc.getNumNss(),
                        droitModelAlloc.getIdTiersAllocataire(), droitModelAlloc.getDroitModel().getIdDossier(),
                        droitModelAlloc.getNumAffilie(), droitModelAlloc.getNumContribuable(), droitModelAlloc.getNomAllocataire(),
                        droitModelAlloc.getPrenomAllocataire(), droitModelAlloc.getTitre(),
                        droitModelAlloc.getIdTiersBeneficiaire());

                boolean pubDestinationAffilie = false;
                // si l'avis �ch�ances allocataire est destin�e � l'affili�
                if (ALCSAffilie.ATTRIBUT_AVIS_ECH_AFFILIE.equals(attributAffilieTypeAvisEch.getValeurAlpha())
                        && docAffilieGenerated) {
                    pubDestinationAffilie = true;
                }

                JadePublishDocumentInfo pubInfos = new JadePublishDocumentInfo();
                try {
                    if (echAlloc instanceof EcheancesAllocataireDirect) {
                        // Si on est en paiement direct, on incr�mente
                        // Permet de sp�cifier nom distinct pour faciliter la mise sous pli des documents.
                        pubInfos = fillDocInfos(droitsAllocTemp, pubDestinationAffilie, incrementAffilie
                                + incrementPourPaiementDirect);
                        incrementPourPaiementDirect++;
                    } else {
                        pubInfos = fillDocInfos(droitsAllocTemp, pubDestinationAffilie, incrementAffilie);
                    }
                } catch (Exception e) {
                    String idDossier = droitsAllocTemp.get(0).getDroitModel().getIdDossier();
                    try {
                        logger.getWarningsLogger(idDossier, "Dossier #" + idDossier)
                                .addMessage(
                                        new JadeBusinessMessage(
                                                JadeBusinessMessageLevels.ERROR,
                                                ImpressionEcheancesServiceImpl.class.getName(),
                                                droitsAllocTemp.get(0).getDroitModel().getIdDossier()
                                                        + JadeI18n
                                                                .getInstance()
                                                                .getMessage(
                                                                        JadeThread.currentLanguage(),
                                                                        "al.protocoles.impressionAvisEcheances.erreur.publication.dossierId",
                                                                        null)));
                    } catch (JadeApplicationException e1) {
                        JadeLogger.error(this,
                                "Une erreur s'est produite pendant la g�n�ration des avis d'�ch�ances de l'affili� n� "
                                        + droitsAllocTemp.get(0).getNumAffilie() + " : " + e.getMessage());
                    }
                }

                addInSuitableDocumentContainer(docAlloc, attributAffilieTypeAvisEch, false, docAffilieGenerated,
                        pubInfos, allocSeul);

                // si dossier non-actif => copie AC texte sur le doc allocataire
                if (!allocSeul && ALCSDossier.ACTIVITE_NONACTIF.equals(droitModelAlloc.getActiviteAllocataire())
                        && ALServiceLocator.getAffiliationBusinessService().requireDocumentLienAgenceCommunale()) {
                    DocumentData copiePourAC = (DocumentData) ALDeepCopy.copy(docAlloc);
                    containerCopiesAlloc.addDocument(copiePourAC, null);
                }

                // si dossier � tiersBenef ET (docAffili� pas g�n�r� OU attributAffilieTypeEch = Lettres allocataires)
                if (!allocSeul && printCopieAlloc
                        && (JadeNumericUtil.isIntegerPositif(droitModelAlloc.getIdTiersBeneficiaire()) && !droitModelAlloc
                                .getIdTiersBeneficiaire().equals(droitModelAlloc.getIdTiersAllocataire()))
                        && (!docAffilieGenerated || ALCSAffilie.ATTRIBUT_AVIS_ECH_ALLOCATAIRE
                                .equals(attributAffilieTypeAvisEch.getValeurAlpha()))) {

                    DocumentDataContainer lettreAccompagnment = ALServiceLocator.getLettreAccompagnementCopieService()
                            .loadData(logger, droitModelAlloc.getIdTiersAllocataire(), ALCSCopie.TYPE_ECHEANCE,
                                    droitModelAlloc.getDroitModel().getIdDossier(), echAlloc.getLangueDocument());
                    containerCopiesAlloc.addDocument(lettreAccompagnment.getDocument(), null);
                    DocumentData copiePourAlloc = (DocumentData) ALDeepCopy.copy(docAlloc);
                    containerCopiesAlloc.addDocument(copiePourAlloc, null);

                }
                droitsAllocTemp.clear();
            }

        }
    }

}
