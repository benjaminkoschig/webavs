package ch.globaz.al.businessimpl.services.recapitulatifs;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import ch.globaz.al.business.constantes.ALCSAffilie;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.constantes.ALConstAttributsEntite;
import ch.globaz.al.business.constantes.ALConstCalcul;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.business.constantes.enumerations.generation.prestations.Bonification;
import ch.globaz.al.business.exceptions.model.prestation.ALRecapitulatifEntrepriseImpressionModelException;
import ch.globaz.al.business.exceptions.model.prestation.ALRecapitulatifEntrepriseModelException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.attribut.AttributEntiteModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseImpressionComplexModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseImpressionComplexSearchModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.recapitulatifs.RecapitulatifEntrepriseImpressionService;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextAffilie;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextDossier;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALDateUtils;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Impl�mentation du service des r�capitulatifs d'entreprise � imprimer
 * 
 * @author PTA
 * 
 */
public class RecapitulatifEntrepriseImpressionServiceImpl extends ALAbstractBusinessServiceImpl implements
        RecapitulatifEntrepriseImpressionService {

    /**
     * M�thode qui supprime les points au num�ro de l'affilie
     * 
     * @param numAffilie
     *            Num�ro de l'affili�
     * @return String
     */
    private static String removePointToNumAff(String numAffilie) {
        return JadeStringUtil.removeChar(numAffilie, '.');

    }

    /**
     * Calcul les prestations pour un dossier selon le context du dossier (periode de, periode a)
     * 
     * @param contextDossier
     * @param lastNumRecap
     * @param lastNumEntete
     * @return
     */
    private ArrayList<RecapitulatifEntrepriseImpressionComplexModel> calculEffectifDossier(
            ContextDossier contextDossier, int lastNumRecap, int lastNumEntete) {
        // prochain ent�te cr�� vaudra derni�re cr��e + 1
        int incrementEntete = lastNumEntete + 1;

        ArrayList<CalculBusinessModel> calcul = null;
        // conteneur pour les lignes repr�sentant le dossier sur la r�cap
        ArrayList<RecapitulatifEntrepriseImpressionComplexModel> entetesSurRecap = new ArrayList<RecapitulatifEntrepriseImpressionComplexModel>();

        try {
            int iterCalcul = 0;
            // variables pour d�tecter un changement d'unit� / montant entre 2 p�riodes calcul�es
            Double prevMontantTotal = null;
            String prevUnite = null;
            String prevNbUnites = "0";
            // variables pour stocker les infos unit� � int�grer dans les lignes imprim�s plus tard sur les r�caps
            String unite = null;
            String nbUnites = null;

            // cpt de mois cons�cutifs
            int cptMois = 0;
            ArrayList<DetailPrestationModel> details = new ArrayList<DetailPrestationModel>();
            ArrayList<String> idDroitsNaisAcceSurRecap = new ArrayList<String>();
            // on calcul autant de fois que le contexte dossier le d�finit (selon p�riode_de, p�riode_a, selon
            // p�riodicit�)
            while ((iterCalcul == 0) || (calcul != null)) {
                iterCalcul++;
                calcul = contextDossier.getCalcul();
                // calcul size = 0 si aucune droit
                if ((calcul != null) && (calcul.size() > 0)) {
                    // r�cup�ration des infos de l'unit� calcul

                    if (JadeNumericUtil.isEmptyOrZero(contextDossier.getCurrentNbJourDebutOuFin())) {
                        unite = contextDossier.getDossier().getDossierModel().getUniteCalcul();
                    } else {
                        unite = ALCSDossier.UNITE_CALCUL_JOUR;
                    }

                    // on retire les nais/acce du calcul => ca donnera une ligne r�cap ind�pendant des autres droits
                    // calcul�s
                    ArrayList<CalculBusinessModel> calculWithoutNaisAcce = new ArrayList<CalculBusinessModel>();
                    for (CalculBusinessModel droitCalcule : calcul) {

                        if ((ALCSDroit.TYPE_ACCE.equals(droitCalcule.getType()) || ALCSDroit.TYPE_NAIS
                                .equals(droitCalcule.getType()))) {
                            ArrayList<DetailPrestationModel> detailNaisAcce = new ArrayList<DetailPrestationModel>();

                            ArrayList<CalculBusinessModel> calculNaisAcce = new ArrayList<CalculBusinessModel>();
                            calculNaisAcce.add(droitCalcule);
                            // On doit appeler cette m�thode pour avoir montant effectif du droit qu'on veut afficher
                            ALImplServiceLocator.getCalculMontantsService().calculerTotalMontant(
                                    contextDossier.getDossier().getDossierModel(),
                                    calculNaisAcce,
                                    unite,
                                    ALCSDossier.UNITE_CALCUL_MOIS.equals(unite) ? "1" : JadeStringUtil
                                            .isEmpty(contextDossier.getCurrentNbJourDebutOuFin()) ? "1"
                                            : contextDossier.getCurrentNbJourDebutOuFin(), true,
                                    "01." + contextDossier.getCurrentPeriode());

                            // tant que la nais /acce n'existe pas en tant que prestation en DB, elle sera
                            // recalcul�...il faut donc indiqu� qu'on l'a d�j� int�gr�
                            if (!idDroitsNaisAcceSurRecap.contains(droitCalcule.getDroit().getId())) {
                                detailNaisAcce.add(initDetailPrestation(droitCalcule, contextDossier));
                                entetesSurRecap.add(detailsDossierToLineRecap(detailNaisAcce, contextDossier,
                                        lastNumRecap + 1, incrementEntete, ALCSDossier.UNITE_CALCUL_SPECIAL, "1"));
                                idDroitsNaisAcceSurRecap.add(droitCalcule.getDroit().getId());
                                incrementEntete++;
                            }

                        } else {
                            calculWithoutNaisAcce.add(droitCalcule);
                        }

                    }

                    // on calcule mois par mois, donc si unite = M => nb unites = 1
                    // sinon jour / h on a besoin de nb unit�s
                    HashMap total = ALImplServiceLocator.getCalculMontantsService().calculerTotalMontant(
                            contextDossier.getDossier().getDossierModel(),
                            calculWithoutNaisAcce,
                            unite,
                            ALCSDossier.UNITE_CALCUL_MOIS.equals(unite) ? "1" : JadeStringUtil.isEmpty(contextDossier
                                    .getCurrentNbJourDebutOuFin()) ? "1" : contextDossier.getCurrentNbJourDebutOuFin(),
                            true, "01." + contextDossier.getCurrentPeriode());

                    Double montantTotal = new Double((String) total.get(ALConstCalcul.TOTAL_EFFECTIF));

                    // si chmgt d'unit� on ajoute les d�tails accumul�s ensemble sur la r�cap
                    if ((prevUnite != null) && !prevUnite.equals(unite)) {
                        entetesSurRecap.add(detailsDossierToLineRecap(details, contextDossier, lastNumRecap + 1,
                                incrementEntete, prevUnite, prevNbUnites));
                        incrementEntete++;
                        cptMois = 0;
                        details.clear();
                        // sinon si changement dans le montant, on ajoute les d�tails accumul�s ensemble sur la r�cap
                    } else if ((prevMontantTotal != null) && (prevMontantTotal.compareTo(montantTotal) != 0)) {
                        entetesSurRecap.add(detailsDossierToLineRecap(details, contextDossier, lastNumRecap + 1,
                                incrementEntete, prevUnite, prevNbUnites));
                        cptMois = 0;
                        incrementEntete++;
                        details.clear();
                    }

                    // on calcule le reste de droits
                    for (CalculBusinessModel droitCalcule : calculWithoutNaisAcce) {

                        // on int�gre pas les droits inactifs
                        if (ALServiceLocator.getDroitBusinessService().isDroitActif(
                                droitCalcule.getDroit().getDroitModel(), "01." + contextDossier.getCurrentPeriode())) {
                            DetailPrestationModel currentDetail = initDetailPrestation(droitCalcule, contextDossier);
                            details.add(currentDetail);
                        }

                    }

                    // si unit� est le mois on ajoute les d�tails accumul�s ensemble sur la r�cap
                    if (ALCSDossier.UNITE_CALCUL_MOIS.equals(unite)) {

                        cptMois++;

                    }
                    nbUnites = ALCSDossier.UNITE_CALCUL_MOIS.equals(unite) ? Integer.toString(cptMois) : JadeStringUtil
                            .isEmpty(contextDossier.getCurrentNbJourDebutOuFin()) ? "1" : contextDossier
                            .getCurrentNbJourDebutOuFin();

                    prevUnite = unite;
                    prevNbUnites = nbUnites;
                    prevMontantTotal = montantTotal;
                } else {

                }
            }
            // les details restants doivent �tre ajout�es � la future r�cap
            // il faut v�rifier si le calcul a bien eu lieu (dossier sans droits actifs) ou si il a donn� droit � qqch
            if (details.size() > 0) {

                // r�cup�ration des infos de l'unit� calcul
                if (JadeNumericUtil.isEmptyOrZero(contextDossier.getCurrentNbJourDebutOuFin())) {
                    unite = contextDossier.getDossier().getDossierModel().getUniteCalcul();
                } else {
                    unite = ALCSDossier.UNITE_CALCUL_JOUR;
                }

                // si unit� est le mois on ajoute les d�tails accumul�s ensemble sur la r�cap
                if (ALCSDossier.UNITE_CALCUL_MOIS.equals(unite)) {

                    // cptMois++;

                }

                nbUnites = ALCSDossier.UNITE_CALCUL_MOIS.equals(unite) ? Integer.toString(cptMois) : JadeStringUtil
                        .isEmpty(contextDossier.getCurrentNbJourDebutOuFin()) ? "1" : contextDossier
                        .getCurrentNbJourDebutOuFin();

                entetesSurRecap.add(detailsDossierToLineRecap(details, contextDossier, lastNumRecap + 1,
                        incrementEntete, unite, nbUnites));
                incrementEntete++;
                details.clear();
            }

        } catch (JadeApplicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JadePersistenceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // ligne = entete

        return entetesSurRecap;

    }

    @Override
    public RecapitulatifEntrepriseImpressionComplexSearchModel calculPrestationsStoreInRecapsDocs(
            ArrayList<String> dossiersToGenerate, String periode, String bonification) throws JadePersistenceException,
            JadeApplicationException {

        // dossiersToGenerate est tri� par affili�
        ProtocoleLogger logger = new ProtocoleLogger();

        String prevNumAffilie = null;

        ContextAffilie contextAffilie = null;

        ArrayList<JadeAbstractModel> resultCalculPrestations = new ArrayList<JadeAbstractModel>();
        // compteurs pour la gestion des incr�ments virtuels r�caps et ent�te
        int cptEnteteFictive = 0;
        int cptRecapFictive = 0;

        for (String dossierId : dossiersToGenerate) {

            try {

                DossierComplexModel dossier = ALServiceLocator.getDossierComplexModelService().read(dossierId);
                // FIXME: enlever cette condition, voir comment g�rer cas ind�pendant FPV
                if (ALCSDossier.ACTIVITE_SALARIE.equals(dossier.getDossierModel().getActiviteAllocataire())) {

                    // d�tection chgmt affili�
                    if ((prevNumAffilie == null)
                            || ((prevNumAffilie != null) && !prevNumAffilie.equals(dossier.getDossierModel()
                                    .getNumeroAffilie()))) {

                        String periodicite = ALServiceLocator.getAffiliationBusinessService()
                                .getAssuranceInfo(dossier.getDossierModel(), "01." + periode)
                                .getPeriodicitieAffiliation();

                        contextAffilie = ContextAffilie.getContextAffilie(ALCSPrestation.BONI_DIRECT
                                .equals(bonification) ? periode : ALDateUtils.getDebutPeriode(periode, periodicite),
                                periode, ALCSPrestation.GENERATION_TYPE_GEN_DOSSIER, dossier.getDossierModel()
                                        .getNumeroAffilie(), null, "0", logger, JadeThread.currentContext()
                                        .getContext());
                        cptRecapFictive++;
                    }

                    contextAffilie.initContextDossier(dossier, contextAffilie.getDebutRecap(),
                            contextAffilie.getFinRecap(), "0", Bonification.AUTO, "0",
                            ALConstPrestations.TypeGeneration.STANDARD);
                    // on calcul pour le dossier initialis�,on passe en param�tre le dernier id r�cap cr�� et dernier
                    ArrayList<RecapitulatifEntrepriseImpressionComplexModel> lignesDossier = calculEffectifDossier(
                            contextAffilie.getContextDossier(), cptRecapFictive - 1, cptEnteteFictive);

                    cptEnteteFictive += lignesDossier.size();
                    // une ligne = une entete
                    // on ajoute les lignes du dossier aux lignes d�j� format�es pour l'affichage r�cap
                    for (RecapitulatifEntrepriseImpressionComplexModel ligne : lignesDossier) {

                        resultCalculPrestations.add(ligne);
                    }

                    contextAffilie.releaseDossier();
                    prevNumAffilie = dossier.getDossierModel().getNumeroAffilie();
                }

            } catch (Exception e) {
                throw new ALRecapitulatifEntrepriseImpressionModelException(
                        "RecapitulatifEntrepriseImpressionServiceImple#calculPrestationsStoreInRecapsDocs: erreur pour calculer la prestation du dossier n�"
                                + dossierId + " (" + e.getMessage() + ")");
            }
        }

        // On convertit les listes des prestations calcul�es dans une searchModel ad�quat pour l'impression r�caps
        JadeAbstractModel[] newResults = new JadeAbstractModel[resultCalculPrestations.size()];
        for (int iResult = 0; iResult < resultCalculPrestations.size(); iResult++) {
            newResults[iResult] = resultCalculPrestations.get(iResult);
        }

        RecapitulatifEntrepriseImpressionComplexSearchModel result = new RecapitulatifEntrepriseImpressionComplexSearchModel();
        result.setSearchResults(newResults);

        return result;
    }

    /**
     * M�thode de cr�ation d'un document de r�capitulatifs
     * 
     * @param docContainer
     *            container dan lequel on ajoute le document
     * @param recapitulatifsTemp
     *            list provisoire des r�capitulatifs
     * @param numAffilie
     *            num�ro de l'affili�
     * @param periodeDe
     *            d�but de la p�riode de r�capitulation
     * @param periodeA
     *            fin de la p�riode de r�capitulation
     * @param idRecap
     *            identifiant de la r�capitulation
     * @param dateImpression
     *            date d'impression des r�cap
     * @param agenceCommunaleAvs
     *            agence communale avs
     * @param activiteAllocataire
     *            activit� allocataire
     * @param isGed
     *            boolean pour les documents � archiver en ged ou non
     * @param conteneurNonActif
     *            <JadePrintDocumentContainer> conteneur de document pour non actifs
     * @param conteneurCollaAgri
     *            <JadePrintDocumentContainer> conteneur de document pour les collaborateurs agricoles
     * @param conteneurSalarie
     *            <JadePrintDocumentContainer> conteneur de document pour les salari�s
     * @param conteneurTavailleurAgri
     *            <JadePrintDocumentContainer> conteneur de document pour les taravailleurs agricoles
     * @param recapitulatifsTemp
     *            ArrayList liste des r�capitulatifs provisoires
     * @param listContainer
     *            <ArrayList> liste contenant les diff�rents containers
     * 
     * @param typeBonification
     *            <String>
     * @throws JadeApplicationException
     *             Exception lev�e lorsque une date n'est pas une date valide
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private void createDocRecapAffilie(ArrayList recapitulatifsTemp, String numAffilie, String periodeDe,
            String periodeA, String idRecap, String agenceCommunaleAvs, String activiteAllocataire,
            String dateImpression, String typeBonification, boolean isGed,
            JadePrintDocumentContainer conteneurNonActif, JadePrintDocumentContainer conteneurCollaAgri,
            JadePrintDocumentContainer conteneurSalarie, JadePrintDocumentContainer conteneurTavailleurAgri
    /* JadePrintDocumentContainer conteneurAgriInd, */) throws JadeApplicationException, JadePersistenceException {

        if (recapitulatifsTemp == null) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifEntrepriseImpressionServiceImpl#createDocRecapAffilie : recapitulatifsTemp is null");
        }

        if (JadeStringUtil.isEmpty(numAffilie)) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifEntrepriseImpressionServiceImpl#createDocRecapAffilie : numAffilie is null or empty");
        }

        if (!JadeDateUtil.isGlobazDateMonthYear(periodeDe)) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifEntrepriseImpressionServiceImpl#createDocRecapAffilie : " + periodeDe
                            + " is not a valid period");
        }

        if (!JadeDateUtil.isGlobazDateMonthYear(periodeA)) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifEntrepriseImpressionServiceImpl#createDocRecapAffilie : " + periodeA
                            + " is not a valid period");
        }

        if (!JadeNumericUtil.isInteger(idRecap)) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifEntrepriseImpressionServiceImpl#createDocRecapAffilie : idRecap " + idRecap
                            + " is not an integer");
        }

        if (JadeStringUtil.isEmpty(agenceCommunaleAvs)) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifEntrepriseImpressionServiceImpl#createDocRecapAffilie : agenceCommunaleAvs is null or empty");
        }

        if (JadeStringUtil.isEmpty(agenceCommunaleAvs)) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifEntrepriseImpressionServiceImpl#createDocRecapAffilie : agenceCommunaleAvs is null or empty");
        }

        if (JadeStringUtil.isEmpty(activiteAllocataire)) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifEntrepriseImpressionServiceImpl#createDocRecapAffilie : activiteAllocataire is null or empty");
        }

        if (!JadeDateUtil.isGlobazDate(dateImpression)) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifEntrepriseImpressionServiceImpl#createDocRecapAffilie : " + dateImpression
                            + " is not a valid date");

        }
        if (!JadeStringUtil.equals(typeBonification, ALCSPrestation.BONI_DIRECT, false)
                && !JadeStringUtil.equals(typeBonification, ALCSPrestation.BONI_INDIRECT, false)
                && !JadeStringUtil.equals(typeBonification, ALCSPrestation.BONI_RESTITUTION, false)) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifEntrepriseImpressionServiceImpl#createDocRecapAffilie: " + typeBonification
                            + " is not a valid traitement's type");

        }

        DocumentData docRecapAffilie = ALImplServiceLocator.getRecapitulatifsListeAffilieService().loadData(
                recapitulatifsTemp, numAffilie, idRecap, periodeDe, periodeA, agenceCommunaleAvs, activiteAllocataire,
                dateImpression, typeBonification);

        JadePublishDocumentInfo pubInfosRecapitulation = new JadePublishDocumentInfo();
        pubInfosRecapitulation.setOwnerEmail(JadeThread.currentUserEmail());
        pubInfosRecapitulation.setOwnerId(JadeThread.currentUserId());
        // titre du document
        String[] periodes = { periodeDe };
        // propri�t� ged: cha�ne vide afin que la ged TI reprenne la valeur par
        // d�faut pour la desciption
        pubInfosRecapitulation.setDocumentTitle("");
        // sujet du document
        pubInfosRecapitulation.setDocumentSubject(JadeThread.getMessage("al.recapitulatif.titre.impression.ged.label",
                periodes));
        pubInfosRecapitulation.setDocumentType("RecapEntreprise01");
        pubInfosRecapitulation.setDocumentTypeNumber(ALConstDocument.DOC_TYPE_NUMBER_INFOROM_RECAP);
        // date
        pubInfosRecapitulation.setDocumentDate(dateImpression);

        // archivage des documents
        pubInfosRecapitulation.setArchiveDocument(isGed);
        // publication du document
        pubInfosRecapitulation.setPublishDocument(false);

        // construction du code barre pour le document
        // code barre notamment pour la FPV
        pubInfosRecapitulation.setBarcode(numAffilie + "-" + periodeDe.substring(3, 7) + "-"
                + ALConstDocument.DOC_TYPE_NUMBER_INFOROM_RECAP);

        String numAffilieNonFormatte = RecapitulatifEntrepriseImpressionServiceImpl.removePointToNumAff(numAffilie);
        pubInfosRecapitulation.setPublishProperty("numero.affilie.formatte", numAffilie);
        pubInfosRecapitulation.setPublishProperty("id.recap", idRecap);

        DossierComplexModel dossier = ALServiceLocator.getDossierComplexModelService().read(
                ((RecapitulatifEntrepriseImpressionComplexModel) recapitulatifsTemp.get(0)).getIdDossier());
        pubInfosRecapitulation.setDocumentProperty("type.dossier", ALServiceLocator.getGedBusinessService()

        .getTypeSousDossier(dossier.getDossierModel()));
        pubInfosRecapitulation.setOwnerId(JadeThread.currentUserId());

        // FIXME:bz5857
        // recherche idTiersAffilie
        String idTiers = AFBusinessServiceLocator.getAffiliationService().findIdTiersForNumeroAffilie(numAffilie);

        try {
            TIBusinessServiceLocator.getDocInfoService().fill(pubInfosRecapitulation, idTiers, ALCSTiers.ROLE_AF,
                    numAffilie, numAffilieNonFormatte);
        } catch (Exception e) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifEntrepriseImpressionServiceImpl#createDocRecapAffilie : unable to fill DocInfoService",
                    e);
        }

        // ici ajouter au type de conteneur
        if (JadeStringUtil.equals(ALCSDossier.ACTIVITE_NONACTIF, activiteAllocataire, false)) {
            conteneurNonActif.addDocument(docRecapAffilie, pubInfosRecapitulation);

        } else if (JadeStringUtil.equals(ALCSDossier.ACTIVITE_COLLAB_AGRICOLE, activiteAllocataire, false)) {
            conteneurCollaAgri.addDocument(docRecapAffilie, pubInfosRecapitulation);

        } else if (JadeStringUtil.equals(ALCSDossier.ACTIVITE_SALARIE, activiteAllocataire, false)) {
            conteneurSalarie.addDocument(docRecapAffilie, pubInfosRecapitulation);
        } else if (JadeStringUtil.equals(ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE, activiteAllocataire, false)) {
            conteneurTavailleurAgri.addDocument(docRecapAffilie, pubInfosRecapitulation);
        } else {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifEntrepriseImpressionServiceImpl#createDocRecapAffilie : this allocataire activity ("
                            + activiteAllocataire + ") is not supported");
        }
    }

    private RecapitulatifEntrepriseImpressionComplexModel detailsDossierToLineRecap(
            ArrayList<DetailPrestationModel> details, ContextDossier contextDossier, int numRecap, int numEntete,
            String typeUnite, String nbUnites) throws JadeApplicationException, JadePersistenceException {
        // TODO: traiter les param�tres
        RecapitulatifEntrepriseImpressionComplexModel recapLine = new RecapitulatifEntrepriseImpressionComplexModel();

        recapLine.setActiviteAllocataire(contextDossier.getDossier().getDossierModel().getActiviteAllocataire());

        recapLine.setIdDossier(contextDossier.getDossier().getId());
        recapLine.setNomAllocataire(contextDossier.getDossier().getAllocataireComplexModel()
                .getPersonneEtendueComplexModel().getTiers().getDesignation1());
        recapLine.setNumNSS(contextDossier.getDossier().getAllocataireComplexModel().getPersonneEtendueComplexModel()
                .getPersonneEtendue().getNumAvsActuel());
        recapLine.setNumSalarieExterne(contextDossier.getDossier().getDossierModel().getNumSalarieExterne());
        recapLine.setPrenomAllocataire(contextDossier.getDossier().getAllocataireComplexModel()
                .getPersonneEtendueComplexModel().getTiers().getDesignation2());
        RecapitulatifEntrepriseModel recap = new RecapitulatifEntrepriseModel();
        recap.setBonification(JadeStringUtil.isBlankOrZero(contextDossier.getDossier().getDossierModel()
                .getIdTiersBeneficiaire()) ? ALCSPrestation.BONI_INDIRECT : ALCSPrestation.BONI_DIRECT);
        recap.setNumeroAffilie(contextDossier.getDossier().getDossierModel().getNumeroAffilie());
        recap.setIdRecap(Integer.toString(numRecap));
        recap.setPeriodeA(contextDossier.getContextAffilie().getFinRecap());
        recap.setPeriodeDe(contextDossier.getContextAffilie().getDebutRecap());

        recapLine.setRecapEntrepriseModel(recap);

        recapLine.setStatutDossier(contextDossier.getDossier().getDossierModel().getStatut());
        recapLine.setIdEntete(Integer.toString(numEntete));

        recapLine.setNbreUnite(nbUnites);
        recapLine.setTypeUnite(typeUnite);

        // TODO: voir agence communale
        // recapLine.setAgenceCommunale(agenceCommunale);
        // recapLine.setIntituleAgenceComm(intituleAgenceComm);

        recapLine.setMontant("0");
        recapLine.setNbrEnfant("0");

        String minPeriod = null;
        String maxPeriod = null;

        // liste des droits parmi les d�tails
        ArrayList<String> idDroitsDetails = new ArrayList<String>();
        for (DetailPrestationModel detail : details) {
            idDroitsDetails.add(detail.getIdDroit());
            Double nouveauMontant = new Double(detail.getMontant()) + new Double(recapLine.getMontant());
            recapLine.setMontant(nouveauMontant.toString());

            // on prend les extremes de validit� parmi les d�tails pour faire les p�riodes de la ligne r�cap
            if ((minPeriod == null) || JadeDateUtil.isDateMonthYearBefore(detail.getPeriodeValidite(), minPeriod)) {
                minPeriod = detail.getPeriodeValidite();
                recapLine.setPeriodeDeEntete(minPeriod);
            }
            if ((maxPeriod == null) || JadeDateUtil.isDateMonthYearAfter(detail.getPeriodeValidite(), maxPeriod)) {
                maxPeriod = detail.getPeriodeValidite();
                recapLine.setPeriodeAEntete(maxPeriod);
            }

        }
        recapLine.setNbrEnfant(Integer.toString(ALServiceLocator.getDroitBusinessService().countEnfantsInDroitsList(
                idDroitsDetails)));

        return recapLine;
    }

    private JadePublishDocumentInfo fillRecapInfo(JadePublishDocumentInfo pubInfosRecap, String periodeDe,
            String dateImpression, String numAffilie, boolean isGed)
            throws ALRecapitulatifEntrepriseImpressionModelException {

        pubInfosRecap.setOwnerEmail(JadeThread.currentUserEmail());
        pubInfosRecap.setOwnerId(JadeThread.currentUserId());
        // titre du document
        String[] periodes = { periodeDe };
        // propri�t� ged: cha�ne vide afin que la ged TI reprenne la valeur par
        // d�faut pour la desciption
        pubInfosRecap.setDocumentTitle("");
        // sujet du document
        pubInfosRecap
                .setDocumentSubject(JadeThread.getMessage("al.recapitulatif.titre.impression.ged.label", periodes));
        pubInfosRecap.setDocumentType("RecapEntreprise01");
        pubInfosRecap.setDocumentTypeNumber(ALConstDocument.DOC_TYPE_NUMBER_INFOROM_RECAP);
        // date
        pubInfosRecap.setDocumentDate(dateImpression);

        // archivage des documents
        pubInfosRecap.setArchiveDocument(isGed);
        // publication du document
        pubInfosRecap.setPublishDocument(false);

        // construction du code barre pour le document
        // code barre notamment pour la FPV
        pubInfosRecap.setBarcode(numAffilie + "-" + periodeDe.substring(3, 7) + "-"
                + ALConstDocument.DOC_TYPE_NUMBER_INFOROM_RECAP);

        String numAffilieNonFormatte = RecapitulatifEntrepriseImpressionServiceImpl.removePointToNumAff(numAffilie);
        pubInfosRecap.setPublishProperty("numero.affilie.formatte", numAffilie);
        // pubInfosRecap.setPublishProperty("id.recap", idRecap);

        // FIXME:bz5857
        // recherche idTiersAffilie
        try {
            String idTiers = AFBusinessServiceLocator.getAffiliationService().findIdTiersForNumeroAffilie(numAffilie);

            TIBusinessServiceLocator.getDocInfoService().fill(pubInfosRecap, idTiers, ALCSTiers.ROLE_AF, numAffilie,
                    numAffilieNonFormatte);
        } catch (Exception e) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifEntrepriseImpressionServiceImpl#createDocRecapAffilie : unable to fill DocInfoService",
                    e);
        }

        return pubInfosRecap;
    }

    /**
     * M�thode qui �limine les doublons au niveaux des entetes de prestations
     * 
     * @param droitsEcheanceSearchModel
     *            r�sultats de la recherche
     * @return listEntetePrestation
     * @throws JadeApplicationException
     */
    private ArrayList<RecapitulatifEntrepriseImpressionComplexModel> getListEntetePrestationUnique(
            ArrayList recapitulatifPrestationSearchModel) throws JadeApplicationException {
        // contr�le param�tre
        if (recapitulatifPrestationSearchModel == null) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseBusinessService#getListEntetePrestationUnique is null");
        }
        ArrayList<RecapitulatifEntrepriseImpressionComplexModel> listRecap = new ArrayList<RecapitulatifEntrepriseImpressionComplexModel>();
        if (recapitulatifPrestationSearchModel.size() > 0) {
            String idEntete = "";

            // �l�mine les doublons au niveau des entetes
            for (int i = 0; i < recapitulatifPrestationSearchModel.size(); i++) {
                RecapitulatifEntrepriseImpressionComplexModel recapitulatifEntrepriseModel = ((RecapitulatifEntrepriseImpressionComplexModel) recapitulatifPrestationSearchModel
                        .get(i));

                if (!JadeStringUtil.equals(idEntete, recapitulatifEntrepriseModel.getIdEntete(), false)) {
                    listRecap.add(recapitulatifEntrepriseModel);
                }
                idEntete = recapitulatifEntrepriseModel.getIdEntete();
            }
        }

        return listRecap;
    }

    private DetailPrestationModel initDetailPrestation(CalculBusinessModel droitCalcule, ContextDossier contextDossier) {
        DetailPrestationModel detail = new DetailPrestationModel();

        detail.setPeriodeValidite(contextDossier.getCurrentPeriode());
        if (ALCSDroit.TYPE_ENF.equals(droitCalcule.getType()) || ALCSDroit.TYPE_FORM.equals(droitCalcule.getType())) {
            // TODO: on se fout de l'�ge pour une r�cap
            try {
                detail.setAgeEnfant(ALServiceLocator.getPrestationBusinessService().getAgeEnfantDetailPrestation(
                        droitCalcule.getDroit(), detail));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (droitCalcule.getDroit() != null) {
            detail.setIdDroit(droitCalcule.getDroit().getId());
        }

        // b�n�ficiaire
        // TODO: on se fout du b�n�ficiaire pour une impression r�cap
        try {
            if (ALImplServiceLocator.getDossierBusinessService().isModePaiementDirect(
                    contextDossier.getDossier().getDossierModel())) {
                if ((droitCalcule.getDroit() != null)
                        && JadeNumericUtil.isIntegerPositif(droitCalcule.getDroit().getDroitModel()
                                .getIdTiersBeneficiaire())) {
                    detail.setIdTiersBeneficiaire(droitCalcule.getDroit().getDroitModel().getIdTiersBeneficiaire());
                } else {
                    detail.setIdTiersBeneficiaire(contextDossier.getDossier().getDossierModel()
                            .getIdTiersBeneficiaire());
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        detail.setRang(droitCalcule.getRang());
        detail.setTypePrestation(droitCalcule.getType());

        detail.setMontant(droitCalcule.getCalculResultMontantEffectif());
        detail.setCategorieTarif(droitCalcule.getTarif());

        detail.setMontantCaisse(droitCalcule.getCalculResultMontantEffectifCaisse());
        detail.setCategorieTarifCaisse(droitCalcule.getTarifCaisse());

        detail.setMontantCanton(droitCalcule.getCalculResultMontantEffectifCanton());
        detail.setCategorieTarifCanton(droitCalcule.getTarifCanton());

        detail.setTarifForce(new Boolean(droitCalcule.isTarifForce()));
        // Le num�ro de compte est d�fini dans ContextPrestation, il est
        // n�cessaire de conna�tre l'en-t�te pour d�terminer la rubrique
        // ==> pas n�cessaire pour un affichage r�cap fictive

        return detail;
    }

    @Override
    public ArrayList loadArrayListCsv(ArrayList listRecap) throws JadePersistenceException, JadeApplicationException {

        ArrayList listRecapCsv = new ArrayList();
        Iterator iter = listRecap.iterator();

        while (iter.hasNext()) {

            RecapitulatifEntrepriseImpressionComplexSearchModel recapListSearch = (RecapitulatifEntrepriseImpressionComplexSearchModel) iter
                    .next();

            // boucle sur la liste des r�cap
            for (int i = 0; i < recapListSearch.getSize(); i++) {

                RecapitulatifEntrepriseImpressionComplexModel recapEntreprise = (RecapitulatifEntrepriseImpressionComplexModel) recapListSearch
                        .getSearchResults()[i];

                AttributEntiteModel attriAffiliTypeDoc = ALServiceLocator.getAttributEntiteModelService()
                        .getAttributAffilieByNumAffilie(ALConstAttributsEntite.FORMAT_RECAP,
                                recapEntreprise.getRecapEntrepriseModel().getNumeroAffilie());

                if ((attriAffiliTypeDoc != null)
                        && (JadeStringUtil.equals(attriAffiliTypeDoc.getValeurAlpha(),
                                ALCSAffilie.ATTRIBUT_RECAP_FORMAT_CSV, false) || JadeStringUtil.equals(
                                attriAffiliTypeDoc.getValeurAlpha(), ALCSAffilie.ATTRIBUT_RECAP_FORMAT_PDF_CSV, false))) {
                    listRecapCsv.add(recapEntreprise);
                }
            }

        }

        return listRecapCsv;
    }

    @Override
    public ArrayList loadArrayListDocData(ArrayList listRecap) throws JadePersistenceException,
            JadeApplicationException {
        ArrayList listRecapDocData = new ArrayList();

        Iterator iter = listRecap.iterator();
        while (iter.hasNext()) {

            RecapitulatifEntrepriseImpressionComplexSearchModel recapListSearch = (RecapitulatifEntrepriseImpressionComplexSearchModel) iter
                    .next();

            // boucle sur la liste des r�cap

            for (int i = 0; i < recapListSearch.getSize(); i++) {
                RecapitulatifEntrepriseImpressionComplexModel recapEntreprise = (RecapitulatifEntrepriseImpressionComplexModel) recapListSearch
                        .getSearchResults()[i];

                // r�cup�re le type de document pour l'affilie
                // v�rification du type de document � cr�er (docData(pdf) ou
                // CSV)
                AttributEntiteModel attriAffiliTypeDoc = ALServiceLocator.getAttributEntiteModelService()
                        .getAttributAffilieByNumAffilie(ALConstAttributsEntite.FORMAT_RECAP,
                                recapEntreprise.getRecapEntrepriseModel().getNumeroAffilie());

                if ((attriAffiliTypeDoc == null)
                        || !JadeStringUtil.equals(attriAffiliTypeDoc.getValeurAlpha(),
                                ALCSAffilie.ATTRIBUT_RECAP_FORMAT_CSV, false)) {

                    listRecapDocData.add(recapEntreprise);
                }

            }

        }

        return listRecapDocData;
    }

    @Override
    public HashMap loadCSVDocument(ArrayList recapList) throws JadePersistenceException, JadeApplicationException {
        HashMap container = new HashMap();
        // identifiant de la r�cap
        String idRecap = null;

        ArrayList listRecapTemp = new ArrayList();
        String numeroAffilie = null;

        ArrayList listRecapEnteteUnique = getListEntetePrestationUnique(recapList);

        // StringBuffer csvContent = new StringBuffer();

        // boucle sur la liste des r�cap
        for (int i = 0; i < listRecapEnteteUnique.size(); i++) {
            RecapitulatifEntrepriseImpressionComplexModel recapEntreprise = (RecapitulatifEntrepriseImpressionComplexModel) listRecapEnteteUnique
                    .get(i);

            // si c'est pas la premi�re ou si
            if ((i == 0)
                    || (JadeStringUtil.equals(idRecap, recapEntreprise.getRecapEntrepriseModel().getIdRecap(), false))) {
                listRecapTemp.add(recapEntreprise);
            } else {

                container.put(idRecap,
                        ALImplServiceLocator.getRecapitulatifsListeAffilieService().loadDataCSV(listRecapTemp));

                listRecapTemp.clear();
                listRecapTemp.add(recapEntreprise);

            }

            // si c'�tait le dernier droit on g�n�re avant de sortir
            if (i == (listRecapEnteteUnique.size() - 1)) {

                container.put(recapEntreprise.getRecapEntrepriseModel().getIdRecap(), ALImplServiceLocator
                        .getRecapitulatifsListeAffilieService().loadDataCSV(listRecapTemp));

                listRecapTemp.clear();

            }

            idRecap = recapEntreprise.getRecapEntrepriseModel().getIdRecap();

        }

        listRecapTemp.clear();

        return container;
    }

    @Override
    public ArrayList<JadePrintDocumentContainer> loadDocuments(ArrayList recapList, String dateImpression, boolean isGed)
            throws JadePersistenceException, JadeApplicationException {
        /**
         * conteneur pour la liste des agriculteurs ind�pendants
         */
        JadePrintDocumentContainer conteneurAgriInd = new JadePrintDocumentContainer();

        /**
         * conteneur pour la liste des collaborateurs
         */
        JadePrintDocumentContainer conteneurCollaAgri = new JadePrintDocumentContainer();
        /**
         * conteneur pour les ind�pendants
         */
        // JadePrintDocumentContainer conteneurIndependant = new
        // JadePrintDocumentContainer();
        /**
         * conteneur pour la liste des non actifs
         */

        JadePrintDocumentContainer conteneurNonActif = new JadePrintDocumentContainer();
        /**
         * conteneur pour la liste des p�cheurs
         * 
         */
        JadePrintDocumentContainer conteneurPecheur = new JadePrintDocumentContainer();
        /**
         * conteneur pour la liste des salari�
         */
        JadePrintDocumentContainer conteneurSalarie = new JadePrintDocumentContainer();
        /**
         * conteneur pour la liste des travailleurs agricoles
         */
        JadePrintDocumentContainer conteneurTavailleurAgri = new JadePrintDocumentContainer();

        /**
         * conteneur pour la liste des travailleurs sans employeurs
         */
        JadePrintDocumentContainer conteneurTravailleurSansEmp = new JadePrintDocumentContainer();

        /**
         * tableau contenant les conteneur
         */
        ArrayList<JadePrintDocumentContainer> listContainer = new ArrayList<JadePrintDocumentContainer>();

        // v�rification des param�tres
        if (recapList == null) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifEntrepriseImpressionServiceImpl#loadDocuments: the recapList is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateImpression)) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifEntrepriseImpressionServiceImpl#loadDocuments: " + dateImpression
                            + " is not a valid globaz date (dd.MM.yyyy)");
        }
        // tableau temporaire contenant la liste de r�cap
        ArrayList listRecapTemp = new ArrayList();

        // identifiant de la r�cap
        String idRecap = null;
        // D�but de la p�riode
        String periodeDe = null;
        // Fin de la p�riode
        String periodeA = null;
        // num�ro de l'affili�
        String numAffilie = null;
        // agence communale avs
        String agenceCommunaleAvs = null;
        // typeAllocataire
        String activiteAllocataire = null;
        // type de bonification
        String typeBonification = null;

        ArrayList listRecapEnteteUnique = getListEntetePrestationUnique(recapList);

        // it�ration sur la liste des r�capa

        for (int i = 0; i < listRecapEnteteUnique.size(); i++) {

            RecapitulatifEntrepriseImpressionComplexModel recapEntreprise = (RecapitulatifEntrepriseImpressionComplexModel) listRecapEnteteUnique
                    .get(i);

            // �liminer les entete � double

            // si c'est pas la premi�re ou si
            if ((i == 0)
                    || (JadeStringUtil.equals(idRecap, recapEntreprise.getRecapEntrepriseModel().getIdRecap(), false))) {
                listRecapTemp.add(recapEntreprise);

            } else {
                // cr�ation des documents .odt
                createDocRecapAffilie(listRecapTemp, numAffilie, periodeDe, periodeA, idRecap, agenceCommunaleAvs,
                        activiteAllocataire, dateImpression, typeBonification, isGed, conteneurNonActif,
                        conteneurCollaAgri, conteneurSalarie, conteneurTavailleurAgri
                /* conteneurAgriInd, */);
                listRecapTemp.clear();
                listRecapTemp.add(recapEntreprise);

            }

            // si c'�tait le dernier droit on g�n�re avant de sortir
            // de
            // la
            // lieu de if
            if ((i == (listRecapEnteteUnique.size() - 1))) {
                createDocRecapAffilie(listRecapTemp, recapEntreprise.getRecapEntrepriseModel().getNumeroAffilie(),
                        recapEntreprise.getRecapEntrepriseModel().getPeriodeDe(), recapEntreprise
                                .getRecapEntrepriseModel().getPeriodeA(), recapEntreprise.getRecapEntrepriseModel()
                                .getIdRecap(),
                        recapEntreprise.getAgenceCommunale() + " " + recapEntreprise.getIntituleAgenceComm(),
                        recapEntreprise.getActiviteAllocataire(), dateImpression, recapEntreprise
                                .getRecapEntrepriseModel().getBonification(), isGed, conteneurNonActif,
                        conteneurCollaAgri, conteneurSalarie, conteneurTavailleurAgri
                /* conteneurAgriInd, */);

                listRecapTemp.clear();

            }
            idRecap = recapEntreprise.getRecapEntrepriseModel().getIdRecap();
            periodeDe = recapEntreprise.getRecapEntrepriseModel().getPeriodeDe();
            periodeA = recapEntreprise.getRecapEntrepriseModel().getPeriodeA();
            numAffilie = recapEntreprise.getRecapEntrepriseModel().getNumeroAffilie();
            agenceCommunaleAvs = recapEntreprise.getAgenceCommunale() + " " + recapEntreprise.getIntituleAgenceComm();
            activiteAllocataire = recapEntreprise.getActiviteAllocataire();
            typeBonification = recapEntreprise.getRecapEntrepriseModel().getBonification();

        }

        // // Ajouter les conteneurs � la liste
        listContainer.add(conteneurNonActif);
        // this.listContainer.add(this.conteneurIndependant);
        // this.listContainer.add(this.conteneurAgriInd);
        listContainer.add(conteneurCollaAgri);
        // this.listContainer.add(this.conteneurPecheur);
        listContainer.add(conteneurSalarie);
        listContainer.add(conteneurTavailleurAgri);
        // listContainer.add(conteneurNonActif);
        // // this.listContainer.add(this.conteneurIndependant);
        // listContainer.add(conteneurAgriInd);
        // listContainer.add(conteneurCollaAgri);
        // // this.listContainer.add(this.conteneurPecheur);
        // listContainer.add(conteneurSalarie);
        // listContainer.add(conteneurTavailleurAgri);
        // // this.listContainer.add(this.conteneurTravailleurSansEmp);

        return listContainer;

    }

}
