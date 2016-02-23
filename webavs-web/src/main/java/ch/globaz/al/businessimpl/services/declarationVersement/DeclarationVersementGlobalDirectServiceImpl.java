package ch.globaz.al.businessimpl.services.declarationVersement;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstDeclarationVersement;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.exceptions.declarationVersement.ALDeclarationVersementException;
import ch.globaz.al.business.exceptions.document.ALDocumentAddressException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.prestation.DeclarationVersementDetailleComplexModel;
import ch.globaz.al.business.models.prestation.DeclarationVersementDetailleSearchComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.declarationVersement.DeclarationVersementGlobalDirectService;
import ch.globaz.al.business.services.documents.DocumentDataContainer;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Classe d'implémentation du service de liste des attestations de versement pour un dossier imposé à la source
 * 
 * @author PTA
 * 
 */
public class DeclarationVersementGlobalDirectServiceImpl extends DeclarationVersementGlobalAbstractServiceImpl
        implements DeclarationVersementGlobalDirectService {

    /**
     * 
     * @param idDossier
     *            identifiant du dossier
     * @param dateDebut
     *            date du début
     * @param dateFin
     *            date de fin
     * @param dateImpression
     *            date d'impression
     * @return DocumentData
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    @Override
    public ArrayList<DocumentDataContainer> getDeclarationVersement(String idDossier, String dateDebut, String dateFin,
            String dateImpression, ProtocoleLogger logger, String langueDocument, Boolean textImpot)
            throws JadePersistenceException, JadeApplicationException {
        // contrôle des paramètres

        if (idDossier == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementGlobalDirectServiceImpl# getDeclarationVersement: idDossier is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateDebut)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementGlobalDirectServiceImpl# getDeclarationVersement:  " + dateDebut
                            + " is not a valid globaz's date ");
        }

        if (!JadeDateUtil.isGlobazDate(dateFin)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementGlobalDirectServiceImpl# getDeclarationVersement:  " + dateFin
                            + " is not a valid globaz's date ");
        }
        if (!JadeDateUtil.isGlobazDate(dateImpression)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementGlobalDirectServiceImpl# getDeclarationVersement:  " + dateImpression
                            + " is not a valid globaz's date ");
        }
        if (logger == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementGlobalDirectServiceImpl# getDeclarationVersement:  logger is null");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementGlobalDirectServiceImpl# getDeclarationVersement: language  " + langueDocument
                            + " is not  valid ");
        }

        // initialiser conteneur
        // DocumentDataContainer declaVerseContainer = new DocumentDataContainer();

        // Dossier complex
        DossierComplexModel dossier = ALServiceLocator.getDossierComplexModelService().read(idDossier);

        // DocumentData document = new DocumentData();

        HashSet<String> typeBoni = new HashSet<String>();

        typeBoni.add(ALCSPrestation.BONI_DIRECT);
        typeBoni.add(ALCSPrestation.BONI_RESTITUTION);
        // faire la recherche pour les prestation les ajouter au container, voir si utilis
        // DetailPrestationGenComplexSearchModel detPrestaSearch = this.getDetailsPrestations(idDossier, dateDebut,
        // dateFin, typeBoni);REMPL

        DeclarationVersementDetailleSearchComplexModel detPrestaSearch = getPrestationDetailles(idDossier, dateDebut,
                dateFin, typeBoni, langueDocument);

        // ArrayList<DeclarationVersementDetailleDossierTiers> listPrestDossier = this.loadDataDocument(detPrestaSearch,
        // dossier);REMPL

        ArrayList<DeclarationVersementDetailleDossierTiers> listPrestDossier = this.loadDataDocument(detPrestaSearch,
                dossier);

        ArrayList<DocumentDataContainer> listDocumentDataContainer = new ArrayList<DocumentDataContainer>();

        for (int i = 0; i < listPrestDossier.size(); i++) {
            // initialiser conteneur
            DocumentDataContainer declaVerseContainer = new DocumentDataContainer();
            DocumentData document = new DocumentData();

            document = initDocument();

            String activiteAllocataire = ALServiceLocator.getDossierModelService().read(idDossier)
                    .getActiviteAllocataire();
            this.setIdEntete(document, activiteAllocataire, ALConstDocument.DOCUMENT_DECLARATION_VERSEMENT,
                    langueDocument);
            this.setIdSignature(document, activiteAllocataire, ALConstDocument.DOCUMENT_DECLARATION_VERSEMENT,
                    langueDocument);

            // charger les données liés aux prestations
            setTable(document, idDossier, dossier.getDossierModel().getIdTiersBeneficiaire(), dossier
                    .getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire(), dateDebut, dateFin,
                    typeBoni, listPrestDossier.get(i).getListPrestatiion(),
                    ALConstDeclarationVersement.DECLA_VERSE_ATTEST_IMPOT, langueDocument,
                    dossier.getAllocataireComplexModel());

            String idTiersAffilie = AFBusinessServiceLocator.getAffiliationService().findIdTiersForNumeroAffilie(
                    dossier.getDossierModel().getNumeroAffilie());
            try {

                if (JadeStringUtil.equals(idTiersAffilie, listPrestDossier.get(i).getIdTiersDestinataire(), false)) {
                    this.addDateAdresse(document, dateImpression, listPrestDossier.get(i).getIdTiersDestinataire(),
                            langueDocument, dossier.getDossierModel().getNumeroAffilie());
                } else {
                    this.addDateAdresse(document, dateImpression, listPrestDossier.get(i).getIdTiersDestinataire(),
                            langueDocument, null);
                }
            } catch (ALDocumentAddressException e) {
                logger.getErrorsLogger(idDossier, "Dossier #" + idDossier).addMessage(
                        new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR,
                                DeclarationVersementGlobalDirectServiceImpl.class.getName(), e.getMessage()));

                declaVerseContainer.setProtocoleLogger(logger);
                listDocumentDataContainer.add(declaVerseContainer);
                return listDocumentDataContainer;
            }
            // infos générales relatives au dossier
            setInfos(document, dossier, dateDebut, dateFin, langueDocument);

            // ajouter la collection vide des copie (pas de copie pour ce type)
            Collection table = new Collection("declaration_Avis_A");
            document.add(table);

            if (textImpot) {
                document.addData("declaration_versement_destination_text",
                        this.getText("al.declarationVersement.prestation.destinationImpot", langueDocument));
            }

            // ajout du tiers destinataire en copie (si besoin)
            // this.setTableCopie(document, dossier, langueDocument);

            logger.getInfosLogger(idDossier, "Dossier #" + idDossier).addMessage(
                    new JadeBusinessMessage(JadeBusinessMessageLevels.INFO,
                            DeclarationVersementGlobalDirectServiceImpl.class.getName(), "document généré"));

            // ajoute le documentData au
            declaVerseContainer.setDocument(document);
            // ajoute le protocole logger
            declaVerseContainer.setProtocoleLogger(logger);
            listDocumentDataContainer.add(declaVerseContainer);
        }

        return listDocumentDataContainer;
    }

    /**
     * Méthode qui permet de récupérer les différents totaux
     * 
     * @param presta
     *            les prestation permettant de récupérer les données
     * @return HashMap
     * @throws JadeApplicationException
     *             levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    @Override
    protected HashMap<String, String> getTotauxPaiementDirect(
    /* DetailPrestationGenComplexSearchModel */ArrayList<DeclarationVersementDetailleComplexModel> presta,
            String idTiersAllocataire) throws JadeApplicationException {
        // contrôle des paramètres
        if (presta == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementAbstractServiceImpl# getTotaux: presta is null");
        }

        HashMap<String, String> totaux = new HashMap<String, String>();
        String montantRetroactif = "0.00";
        String montantAnneeVersement = "0.00";
        for (int i = 0; i < presta./* get */size(); i++) {
            /* DetailPrestationGenComplexModel */DeclarationVersementDetailleComplexModel declarationVersementLigne = presta
                    .get/* SearchResults()[i] */(i);

            // si le tiers bénéficiaire est différent de et que le le tiers bénéficiaire est égal ou tiers allocataire

            // if (!JadeNumericUtil.isEmptyOrZero(declarationVersementLigne.getIdTiersBeneficiaire())) {
            // FIXME: vérifier si il faut que ce soit à 0.- si bénéficiaire n'est pas allocataire

            // if (!JadeNumericUtil.isEmptyOrZero(declarationVersementLigne.getIdTiersBeneficiaire())
            // && JadeStringUtil.equals(declarationVersementLigne.getIdTiersBeneficiaire(),
            // idTiersAllocataire, false)) {
            // calcul pour le total des années précédentes
            if (!JadeStringUtil.equals(
                    JadeStringUtil.substring(declarationVersementLigne./* getPeriodeValidite() */getPeriode(), 3),
                    (JadeStringUtil.substring(declarationVersementLigne.getDateVersement(), 6)), false)) {

                // montant rétroactif
                montantRetroactif = calculMontant(montantRetroactif,
                        declarationVersementLigne./* getMontant() */getMontantDetailPrestation());

            }
            // calcul pour l'année
            else {
                montantAnneeVersement = calculMontant(montantAnneeVersement,
                        declarationVersementLigne./* getMontant() */getMontantDetailPrestation());

            }
            // }

        }
        // }

        String montantTotal = calculMontant(montantAnneeVersement, montantRetroactif);

        totaux.put(ALConstDeclarationVersement.TOTAL_RETROACTIF, montantRetroactif);
        totaux.put(ALConstDeclarationVersement.TOTAL_ANNEE, montantAnneeVersement);
        totaux.put(ALConstDeclarationVersement.TOTAL, montantTotal);

        return totaux;
    }

    /**
     * Méthode "settant" les données du tiers allocataire en copie à
     * 
     * @param doc
     *            document à fournir
     * @param dossier
     *            selon DossierComplexModel
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    // private void setTableCopie(DocumentData doc, DossierComplexModel dossier)
    // throws JadeApplicationException {
    // // vérification des paramètres
    //
    // if (doc == null) {
    // throw new ALDeclarationVersementException(
    // "DeclarationVersementDirectServieImpl# setTableCopie: DocumentData is null");
    // }
    // if (dossier == null) {
    // throw new ALDeclarationVersementException(
    // "DeclarationVersementDirectServieImpl# setTableCopie: DossierComplexModel is null");
    //
    // }
    //
    // Collection tableCopie = new Collection("declaration_Avis_A");
    //
    // if (!JadeStringUtil.equals(dossier.getTiersBeneficiaireModel()
    // .getIdTiers(), dossier.getAllocataireComplexModel()
    // .getAllocataireModel().getIdTiersAllocataire(), false)) {
    //
    // DataList ligne = new DataList("colonne");
    //
    // // ajout copie à
    // ligne.addData("col_copie_label", this
    // .getText("al.declarationVersement.prestation.copie"));
    //
    // // ajouter designation 1 et designation 2 pour colonne copie à
    // ligne.addData("col_copie", dossier.getAllocataireComplexModel()
    // .getPersonneEtendueComplexModel().getTiers()
    // .getDesignation1()
    //
    // + " "
    // + dossier.getAllocataireComplexModel()
    // .getPersonneEtendueComplexModel().getTiers()
    // .getDesignation2());
    //
    // tableCopie.add(ligne);
    // }
    // doc.add(tableCopie);
    //
    // }

}
