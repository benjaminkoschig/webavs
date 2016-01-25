package ch.globaz.perseus.businessimpl.services.facture;

import globaz.externe.IPRConstantesExternes;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRStringUtils;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.babel.business.exception.CatalogueTexteException;
import ch.globaz.perseus.business.constantes.CSCaisse;
import ch.globaz.perseus.business.constantes.CSEtatLot;
import ch.globaz.perseus.business.constantes.CSEtatRentePont;
import ch.globaz.perseus.business.constantes.IPFCatalogueTextes;
import ch.globaz.perseus.business.constantes.IPFConstantes;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.dossier.Dossier;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.rentepont.FactureRentePont;
import ch.globaz.perseus.business.models.rentepont.RentePont;
import ch.globaz.perseus.business.models.rentepont.RentePontSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.document.AbstractDocumentBuilder;
import ch.globaz.perseus.businessimpl.utils.PFTypeImpressionEnum;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

public class ImprimerDecisionFactureRPBuilder extends AbstractFactureBuilder {

    private static final String CDT_ADR_PAIEMENT = "{adrPaiement}";
    private static final String CDT_ANNEE = "{annee}";
    private static final String CDT_AYANT_DROIT = "{ayantDroit}";
    private static final String CDT_DATE_DEBUT_TRAITEMENT = "{dateDebutTraitement}";
    private static final String CDT_DATE_DECISION = "{dateDecision}";
    private static final String CDT_DATE_FACTURE = "{dateFacture}";
    private static final String CDT_DATE_FIN_TRAITEMENT = "{dateFinTraitement}";
    private static final String CDT_DATE_NAISSANCE = "{dateNaissance}";
    private static final String CDT_NUM_NSS = "{numNss}";
    private static final String CDT_NUMERO_DECISION = "{numDecision}";
    private static final String CDT_TITRE_TIERS = "{titreTiers}";
    public String adrMail = null;
    private JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();
    public String annee = "";
    private String caisseDerniereRentePont = null;
    private String csCaisse = null;
    private String dateDocument = null;
    private RentePont derniereRentePont = null;
    private Dossier dossier = null;
    private FactureRentePont factureRentePont = null;
    private List<FactureRentePont> facturesRentePont = null;
    private boolean hasDecompte = false;
    private String idDossier = null;
    private String idMbFamille = "";
    private boolean isSendToGed = false;
    private Map<String, List<FactureRentePont>> listeDecomptes;
    private String numDecision = "";

    private String titreTiers = null;

    public ImprimerDecisionFactureRPBuilder() {
    }

    /**
     * Construction des documents selon le type d'impression
     * 
     * @param typeImpression Le type d'impression : Original, Copies, Copies aux requérants
     * @return Un container de documents assemblés.
     * @throws DecisionException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws Exception
     */
    public JadePrintDocumentContainer build(PFTypeImpressionEnum typeImpression) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {
        try {
            Set keys = listeDecomptes.keySet();

            // Un décompte crée par key
            for (java.util.Iterator iterator = keys.iterator(); iterator.hasNext();) {
                String idDossierAnnee = (String) iterator.next();
                List<String> liste = JadeStringUtil.split(idDossierAnnee, ",");

                setFacturesRentePont(listeDecomptes.get(idDossierAnnee));
                setIdDossier(liste.get(0).replace(",", ""));
                setAnnee(liste.get(1).replace(",", ""));
                setFactureRentePont(facturesRentePont.get(0));
                setDossier(PerseusServiceLocator.getDossierService().read(getIdDossier()));

                if (PFTypeImpressionEnum.COPIES_AUX_REQUERANTS.equals(typeImpression)) {
                    String idTiersAdresse = dossier.getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers();
                    // Dans le cadre des anciennes factures sans adresse de courrier, on prend l id tiers du requerant
                    if (liste.size() > 2) {
                        idTiersAdresse = liste.get(2).replace(",", "");
                    }
                    // Si l id tiers du requrant est la même que l id tiers de l adresse de courrier, nous creons pas de
                    // décomptes dans le pdf pour les copies des requerants
                    // JadeStringUtil.isBlankOrZero(idTiersAdresse) afin d eviter d imprimer des ancients lots sans
                    // courriers
                    if (dossier.getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers()
                            .equals(idTiersAdresse)
                            || JadeStringUtil.isBlankOrZero(idTiersAdresse)) {
                        // Ne va pas créer de décompte si le tiers de l adresse de courrier et le meme que le requérant
                        // Nous faisons a cette endroit pour éviter de charger le CdT, services etc... au lieu de le
                        // faire à la fin.
                        continue;
                    }
                }

                // Chargement de Babel
                this.loadEntity();

                // Test de récupération de la dernière demande rente pont
                RentePontSearchModel rps = new RentePontSearchModel();
                rps.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                rps.setForIdDossier(dossier.getDossier().getIdDossier());
                rps.setForCsEtat(CSEtatRentePont.VALIDE.getCodeSystem());
                rps.setWhereKey(RentePontSearchModel.WITHOUT_DATE_FIN);

                if (PerseusServiceLocator.getRentePontService().count(rps) == 0) {
                    rps.setWhereKey("");
                    rps.setOrderKey(RentePontSearchModel.ORDERBY_DATE_FIN_DESC);
                }
                rps = PerseusServiceLocator.getRentePontService().search(rps);

                for (JadeAbstractModel model : rps.getSearchResults()) {
                    derniereRentePont = (RentePont) model;

                    // Si une caisse est présente, on renseigne la variable
                    if (!JadeStringUtil.isEmpty(derniereRentePont.getSimpleRentePont().getCsCaisse())) {
                        caisseDerniereRentePont = derniereRentePont.getSimpleRentePont().getCsCaisse();
                    }
                    // Si la caisse à été renseignée, on sort de la boucle
                    if ((caisseDerniereRentePont != null) || (caisseDerniereRentePont == "")) {
                        break;
                    } else {
                        caisseDerniereRentePont = "";
                    }
                }

                if (null == derniereRentePont) {
                    throw new DecisionException(
                            "ImprimerDecisionFactureRPBuilder / createDecompte : Pas de dernière rente pont pour connaitre la caisse");
                }

                // Si la dernière demande du dossier concerne l'autre caisse et bien le cas n'est pas traité, il le sera
                // dans la
                // génération du décompte pour l'autre caisse
                if (!csCaisse.equals(derniereRentePont.getSimpleRentePont().getCsCaisse())) {
                    continue;
                }

                // Création du décompte
                createDecompteSelonTypeImpression(typeImpression);
            }

            initLogWhenNoDecompte(typeImpression);

            allDoc.setMergedDocDestination(getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_DOC_DESTINATION));

            return allDoc;

        } catch (Exception e) {
            throw new DecisionException("ImprimerDecisionFactureBuilder / build -  NSS : " + ", Détail de l'erreur : "
                    + e.toString(), e);
        }

    }

    /**
     * Méthode permettant de créer un décompte.
     * 
     * @param isCopie Si on veut le texte COPIE sur le document.
     * @param ged Si on veut mettre le document en GED.
     * @throws Exception Une exception.
     */
    private void createDecompte(boolean isCopie, boolean ged) throws Exception {
        try {

            numDecision = "";

            // remplissage data pour document original
            DocumentData data = new DocumentData();

            // Récupération du gestionnaire dans le dossier
            String gestionnaire = dossier.getDossier().getGestionnaire();

            // Renseignement du premier paragraphe selon type de facture
            String dateDecision = dateDocument;
            for (FactureRentePont fact : facturesRentePont) {
                if (!JadeStringUtil.isEmpty(fact.getSimpleFactureRentePont().getNumDecision())) {
                    numDecision = fact.getSimpleFactureRentePont().getNumDecision();
                    dateDecision = fact.getSimpleFactureRentePont().getDateDecision();
                }
            }

            // Insertion du titre dans le corps du document pour l'agence de Lausanne
            if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(csCaisse)) {
                data.addData("TitreDocument",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 1, 4));
            }

            // Chargement du titre du document pour le header
            String titreDocument = "";
            titreDocument = getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 1, 4);

            // Insertion d'un bandeau si NON-COMPTABILISE
            boolean isBandeau = false;

            Lot lot = PerseusServiceLocator.getLotService().getLotForFactureRP(factureRentePont.getId());
            if (!CSEtatLot.LOT_VALIDE.getCodeSystem().equals(lot.getSimpleLot().getEtatCs())) {
                isBandeau = true;
            }

            String idTiers;
            if (JadeStringUtil.isBlankOrZero(facturesRentePont.get(0).getSimpleFactureRentePont()
                    .getIdTiersAdresseCourrier())) {
                idTiers = dossier.getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers();
            } else {
                idTiers = facturesRentePont.get(0).getSimpleFactureRentePont().getIdTiersAdresseCourrier();
            }

            data = buildHeaderFacture(data, isCopie, idTiers, gestionnaire, caisseDerniereRentePont, dateDocument,
                    isBandeau, titreDocument, true);

            data.addData("idProcess", "PCFFactureRemboursementFraisRentePont");

            // Chargement du requerant, de son nss et de son nip
            String requerant = dossier.getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                    .getNumAvsActuel()
                    + " - "
                    + dossier.getDemandePrestation().getPersonneEtendue().getTiers().getDesignation1()
                    + " "
                    + dossier.getDemandePrestation().getPersonneEtendue().getTiers().getDesignation2()
                    + " / "
                    + getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 1, 2)
                    + " "
                    + dossier.getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers();

            // Renseignement du requerant
            data.addData("Requerant",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 1, 1) + " "
                            + requerant);

            if (JadeStringUtil.isEmpty(numDecision)) {
                String cleDeCalcul = IPFConstantes.DECISION_CLE_INCREMENTATION;
                // Recuperation de l'increment
                String increment = JadePersistenceManager.incIndentifiant(cleDeCalcul);
                increment = JadeStringUtil.fillWithZeroes(increment, 6);
                numDecision = annee + "-" + increment;

            }

            for (java.util.Iterator iterator = facturesRentePont.iterator(); iterator.hasNext();) {
                FactureRentePont fact = (FactureRentePont) iterator.next();
                fact.getSimpleFactureRentePont().setNumDecision(numDecision);
                fact.getSimpleFactureRentePont().setDateDecision(dateDecision);
                PerseusServiceLocator.getFactureRentePontService().update(fact);
            }

            // Renseignement de la date de décision
            data.addData("DateDecision",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 2, 1) + " "
                            + dateDecision);

            // Renseignement des textes 'Année' et 'N° de décision' dans le tableau
            data.addData("TexteAnnee",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 2, 2));
            data.addData("TexteNumDecision",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 2, 3));

            // Renseignement de l'année concerné
            data.addData("Annee", annee);

            // Renseignement du numero de décision
            data.addData("NumDecision", numDecision);

            // Renseignement du titre du tiers
            data.addData("TitreTiers", titreTiers + ",");

            // Renseignement du premier paragraphe
            data.addData("Paragraphe1",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 3, 1));

            // Renseignement du deuxieme paragraphe
            data.addData("Paragraphe2",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 4, 10));

            // Renseignement du titre et du texte 'Remarques'
            data.addData("TitreRemarque",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 4, 2));
            if (CSCaisse.CCVD.getCodeSystem().equals(csCaisse)) {
                // Texte spécifique pour la CCVD
                data.addData("RemarqueDebut",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 4, 3));
                data.addData("RemarqueSouligne",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 4, 4));
                data.addData("RemarqueFin",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 4, 5));
            } else {
                // Texte spécifique pour l'AGLAU
                // Attente du texte souhaité par l'agence de Lausanne
                data.addData("RemarqueDebut",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 4, 7));
                data.addData("RemarqueSouligne",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 4, 8));
                data.addData("RemarqueFin",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 4, 9));
            }
            // Renseignement du dernier paragraphe 'Salutation'
            data.addData("Salutations", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 4, 6),
                    ImprimerDecisionFactureRPBuilder.CDT_TITRE_TIERS, titreTiers));

            // Renseignement du titre 'Réclamation'
            data.addData("TitreReclamation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 5, 1));
            // Renseignement du texte 'reclamation'
            data.addData("TexteReclamation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 5, 2));

            // Insertion du titre et du texte Copie en bas de page
            data = buildBasDePageFactureRP(data, caisseDerniereRentePont, true);

            // Renseignement du bas de page
            data.addData("TexteNbrPage",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 6, 1));
            data.addData("SuiteTexteNbrPage",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 6, 2));

            // ################################# CONSTRUCTION DE LA 2EME PAGE DU TABLEAU

            data.addData("AnneeDecompte", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 7, 1),
                    ImprimerDecisionFactureRPBuilder.CDT_ANNEE, annee));

            String numeroDecision = PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 7, 2),
                    ImprimerDecisionFactureRPBuilder.CDT_NUMERO_DECISION, numDecision);
            data.addData("DateDecisionDecompte", PRStringUtils.replaceString(numeroDecision,
                    ImprimerDecisionFactureRPBuilder.CDT_DATE_DECISION, dateDocument));

            data.addData("NumNssDecompte", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 7, 3),
                    ImprimerDecisionFactureRPBuilder.CDT_NUM_NSS, factureRentePont.getQdRentePont().getDossier()
                            .getDemandePrestation().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel()));

            String requerantDecompte = PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 7, 4),
                    ImprimerDecisionFactureRPBuilder.CDT_AYANT_DROIT, factureRentePont.getQdRentePont().getDossier()
                            .getDemandePrestation().getPersonneEtendue().getTiers().getDesignation1()
                            + " "
                            + factureRentePont.getQdRentePont().getDossier().getDemandePrestation()
                                    .getPersonneEtendue().getTiers().getDesignation2());
            data.addData("RequerantDecompte", PRStringUtils.replaceString(requerantDecompte,
                    ImprimerDecisionFactureRPBuilder.CDT_DATE_NAISSANCE, factureRentePont.getQdRentePont().getDossier()
                            .getDemandePrestation().getPersonneEtendue().getPersonne().getDateNaissance()));

            // Création de variables pour additionner les montants saisies lors de chaque passage dans la boucle
            // ...Par facture...
            double montantTotalAdmis = 0.0;
            double montantTotalDemande = 0.0;
            double montantTotalExcedantRevenu = 0.0;
            double montantTotalDepassementQd = 0.0;
            double montantDemande = 0.0;

            // Création d'une collection pour le tableau
            Collection unTableau = new Collection("tableauDecompteFactureMaladiePerseus");

            // Création d'une DataList pour les titres de chaque colonne du tableau
            DataList ligneZero = new DataList("typeLigneZero");

            // Renseignement du titre du décompte
            ligneZero.addData("TitreDecomptePrestation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 8, 1));
            // Renseignement des titre de colonnes du tableau
            ligneZero.addData("TitreMontantDemande",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 9, 1));
            ligneZero.addData("TitreMontantNonReconnu",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 9, 2));
            ligneZero.addData("TitreMontantAdmis",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 9, 3));
            unTableau.add(ligneZero);

            // Itteration pour chaque facture de la liste
            for (java.util.Iterator iterator = facturesRentePont.iterator(); iterator.hasNext();) {
                FactureRentePont fact = (FactureRentePont) iterator.next();

                // Création d'une DataList pour la premiere ligne du tableau
                DataList ligneUne = new DataList("typeLigneUne");
                // Renseignement du sujet de la prestation
                ligneUne.addData("SujetPrestation",
                        getSession().getCodeLibelle(fact.getSimpleFactureRentePont().getCsSousTypeSoinRentePont()));
                // Insertion de la valeur
                unTableau.add(ligneUne);

                // Création d'une DataList pour la deuxieme ligne du tableau
                DataList ligneDeux = new DataList("typeLigneDeux");
                // Renseignement de la valeur
                ligneDeux.addData("DateFacturePrestation", PRStringUtils.replaceString(
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 10, 1),
                        ImprimerDecisionFactureRPBuilder.CDT_DATE_FACTURE, fact.getSimpleFactureRentePont()
                                .getDateFacture()));
                // Renseignement des montants
                ligneDeux.addData("MontantDemandePeriodeRFM", convertCentimes(fact.getSimpleFactureRentePont()
                        .getMontant()));
                montantDemande = Double.parseDouble(fact.getSimpleFactureRentePont().getMontant());
                montantTotalDemande += montantDemande;
                // Insertion de la valeur
                unTableau.add(ligneDeux);

                // Création d'une DataList pour la troisieme ligne du tableau
                if (!JadeStringUtil.isEmpty(fact.getSimpleFactureRentePont().getFournisseur())) {
                    DataList ligneTrois = new DataList("typeLigneTrois");
                    // Renseignement de la valeur
                    ligneTrois.addData("BeneficiairePrestation", fact.getSimpleFactureRentePont().getFournisseur());
                    // Insertion de la valeur
                    unTableau.add(ligneTrois);
                }

                // Création d'une DataList pour la quatrième ligne du tableau
                // TODO : Voir comment récupérer le nom du membre de famille concerné

                // Création d'une DataList pour la cinquième ligne du tableau
                if (!JadeStringUtil.isEmpty(fact.getSimpleFactureRentePont().getDateDebutTraitement())) {
                    DataList ligneCinq = new DataList("typeLigneCinq");
                    // Remplacement de variable par le texte
                    String texteDateDebutPrestation = PRStringUtils.replaceString(
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 10, 2),
                            ImprimerDecisionFactureRPBuilder.CDT_DATE_DEBUT_TRAITEMENT, fact
                                    .getSimpleFactureRentePont().getDateDebutTraitement());
                    // Renseignement de la valeur
                    ligneCinq.addData("PeriodePrestation", PRStringUtils.replaceString(texteDateDebutPrestation,
                            ImprimerDecisionFactureRPBuilder.CDT_DATE_FIN_TRAITEMENT, fact.getSimpleFactureRentePont()
                                    .getDateFinTraitement()));
                    // Insertion de la valeur
                    unTableau.add(ligneCinq);
                }

                // Création d'une DataList pour la sixième ligne du tableau
                if (!JadeStringUtil.isBlankOrZero(fact.getSimpleFactureRentePont().getCsMotif())) {
                    DataList ligneSix = new DataList("typeLigneSix");
                    // Renseignement de la valeur
                    if (IPFConstantes.CS_MOTIF_FACTURE_AUTRE.equals(fact.getSimpleFactureRentePont().getCsMotif())) {
                        ligneSix.addData("DetailPrestation", "- " + fact.getSimpleFactureRentePont().getMotifLibre());
                    } else if (!JadeStringUtil.isEmpty(fact.getSimpleFactureRentePont().getCsMotif())) {
                        ligneSix.addData("DetailPrestation",
                                "- " + getSession().getCodeLibelle(fact.getSimpleFactureRentePont().getCsMotif()));
                    }
                    double montantAdmis = Double.parseDouble(fact.getSimpleFactureRentePont().getMontantRembourse());
                    double montantNonReconnu = montantDemande - montantAdmis;
                    // Insertion du montant non pris en charge
                    ligneSix.addData("MontantNonReconnuPeriodeRFM", convertCentimes(String.valueOf(montantNonReconnu)));
                    // Insertion de la valeur
                    unTableau.add(ligneSix);
                }

                // Création d'une DataList pour la septième ligne du tableau
                DataList ligneSept = new DataList("typeLigneSept");
                // Renseignement de la valeur
                ligneSept.addData("TexteMontantTotal",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 10, 3));
                ligneSept.addData("MontantTotal", convertCentimes(fact.getSimpleFactureRentePont()
                        .getMontantRembourse()));
                montantTotalAdmis += Double.parseDouble(fact.getSimpleFactureRentePont().getMontantRembourse());
                // Insertion de la valeur
                unTableau.add(ligneSept);

                // Incrémentation des variables qui vont additionner les factures
                montantTotalDepassementQd += Double.parseDouble(fact.getSimpleFactureRentePont().getMontantDepassant());
                montantTotalExcedantRevenu += Double.parseDouble(fact.getSimpleFactureRentePont()
                        .getExcedantRevenuCompense());

                // Affichage de l'adresse de paiement si celle-ci diffère de l'adresse du requerant
                if (!dossier.getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers()
                        .equals(fact.getSimpleFactureRentePont().getIdTiersAdressePaiement())) {
                    // Récupération des information sur l'adresse de paiement
                    PersonneEtendueComplexModel pers = TIBusinessServiceLocator.getPersonneEtendueService().read(
                            fact.getSimpleFactureRentePont().getIdTiersAdressePaiement());
                    String adrPaiement = "";
                    if (JadeStringUtil.isEmpty(pers.getTiers().getDesignation1())) {
                        AdministrationComplexModel admin = TIBusinessServiceLocator.getAdministrationService().read(
                                fact.getSimpleFactureRentePont().getIdTiersAdressePaiement());
                        adrPaiement = getSession().getCodeLibelle(admin.getTiers().getTitreTiers()) + " "
                                + admin.getTiers().getDesignation1() + " " + admin.getTiers().getDesignation2();
                    } else {
                        adrPaiement = getSession().getCodeLibelle(pers.getTiers().getTitreTiers()) + " "
                                + pers.getTiers().getDesignation1() + " " + pers.getTiers().getDesignation2();
                    }

                    // Création d'une DataList pour la ligne adressePaiement
                    DataList ligneAdressePaiement = new DataList("ligneAdressePaiement");
                    // Renseignement de la valeur
                    ligneAdressePaiement.addData(
                            "AdressePaiement",
                            PRStringUtils.replaceString(
                                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS,
                                            10, 5), ImprimerDecisionFactureRPBuilder.CDT_ADR_PAIEMENT, adrPaiement));
                    // Insertion de la valeur
                    unTableau.add(ligneAdressePaiement);
                }
            }

            // Insertion d'un tableau
            data.add(unTableau);

            // Création d'une DataList pour la huitième ligne du tableau
            DataList ligneHuit = new DataList("typeLigneHuit");
            // Renseignement des montants finaux en bas de tableau
            ligneHuit.addData("TexteMontantTotalACharge",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 11, 1));
            ligneHuit.addData("MontantTotalACharge", convertCentimes(String.valueOf(montantTotalAdmis)));
            unTableau.add(ligneHuit);

            // Création d'une DataList pour la neuvième ligne du tableau si un montant est présent
            if (montantTotalDepassementQd > 0) {
                DataList ligneNeuf = new DataList("typeLigneNeuf");
                ligneNeuf.addData("TexteDepassementQuotite",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 11, 2));
                ligneNeuf.addData("MontantTotalQuotite", convertCentimes(String.valueOf(montantTotalDepassementQd)));
                unTableau.add(ligneHuit);
            }

            // Création d'une DataList pour la dixième ligne du tableau
            DataList ligneDix = new DataList("typeLigneDix");
            ligneDix.addData("TexteExcedantRevenus",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 11, 3));
            ligneDix.addData("MontantTotalExcedant", convertCentimes(String.valueOf(montantTotalExcedantRevenu)));
            unTableau.add(ligneDix);

            // TODO : Pas de ligne onze pour remboursement RFM

            // Création d'une DataList pour la douzième ligne du tableau
            DataList ligneDouze = new DataList("typeLigneDouze");
            ligneDouze.addData("TexteMontantTotalARembourser",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 11, 5));
            double totalRembourser = montantTotalAdmis;
            ligneDouze.addData("MontantARembourser", convertCentimes(String.valueOf(totalRembourser)));
            unTableau.add(ligneDouze);

            // Insertion du texte de versement en bas de tableau
            data.addData("RemarqueVersement",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 12, 1));

            allDoc = dataAndPubInfoRecoVerso(allDoc, data, isCopie, ged,
                    IPRConstantesExternes.PCF_FACTURE_IMPRIMER_DECISION_FACTURE, true);
        } catch (Exception e) {
            throw new DecisionException("ImprimerDecisionFactureRPBuilder / createFacture -  NSS : "
                    + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    /**
     * Méthode permettant de créer un décompte selon le type d'impression.
     * 
     * @param typeImpression Le type d'impression.
     * @throws Exception Une exception.
     */
    private void createDecompteSelonTypeImpression(PFTypeImpressionEnum typeImpression) throws Exception {

        createJadePublishDocInfo(
                dateDocument,
                adrMail,
                isSendToGed,
                dossier.getDemandePrestation().getDemandePrestation().getIdTiers(),
                getSession().getLabel("PDF_PF_FACTURE_REMBOURSEMENT_FRAIS_TITRE_MAIL") + " - "
                        + getSession().getCodeLibelle(csCaisse) + " - "
                        + getSession().getLabel(typeImpression.toString()));

        if (PFTypeImpressionEnum.ORIGINAUX.equals(typeImpression)) {
            // Création de la facture originale
            createDecompte(false, isSendToGed);
            // Boolean afin de savoir si un décompte à été généré
            hasDecompte = true;
        }

        if (PFTypeImpressionEnum.COPIES.equals(typeImpression)) {
            // Création de la copie de la facture
            createDecompte(true, false);
        }

        if (PFTypeImpressionEnum.COPIES_AUX_REQUERANTS.equals(typeImpression)) {
            String gestionnaire = dossier.getDossier().getGestionnaire();

            createLettreEntete(dateDocument, dossier.getDemandePrestation().getPersonneEtendue().getTiers()
                    .getIdTiers(), dossier.getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers(),
                    gestionnaire);

            // Création de la copie de la facture
            createDecompte(true, false);
        }
    }

    /**
     * Méthode permettant de créer une lettre d'entête avant un décompte.
     * 
     * @param dateDoc La date du document.
     * @param idTiersAdresse L'id tiers pour afficher l'adresse.
     * @param idTiersConcerne L'id tiers pour le concerne.
     * @param idGestionnaire L'id du gestionnaire.
     * @throws Exception Une exception.
     */
    private void createLettreEntete(String dateDoc, String idTiersAdresse, String idTiersConcerne, String idGestionnaire)
            throws Exception {
        DocumentData dataLettreEnTete = new DocumentData();

        dataLettreEnTete = buildLettreEntete(dataLettreEnTete, idTiersAdresse, idTiersConcerne, csCaisse, dateDoc,
                idGestionnaire);
        allDoc.addDocument(dataLettreEnTete,
                getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_LETTRE_ENTETE_SANS_GED));
    }

    public String getAdrMail() {
        return adrMail;
    }

    public String getAnnee() {
        return annee;
    }

    public String getCsCaisse() {
        return csCaisse;
    }

    public String getDateDocument() {
        return dateDocument;
    }

    public FactureRentePont getFactureRentePont() {
        return factureRentePont;
    }

    public List<FactureRentePont> getFacturesRentePont() {
        return facturesRentePont;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdMbFamille() {
        return idMbFamille;
    }

    public Map<String, List<FactureRentePont>> getListeDecomptes() {
        return listeDecomptes;
    }

    private void initLogWhenNoDecompte(PFTypeImpressionEnum typeImpression) {
        if (!hasDecompte && PFTypeImpressionEnum.ORIGINAUX.equals(typeImpression)) {
            String caisse = "";
            if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(csCaisse)) {
                caisse = CSCaisse.AGENCE_LAUSANNE.toString();
            } else if (CSCaisse.CCVD.getCodeSystem().equals(csCaisse)) {
                caisse = CSCaisse.CCVD.toString();
            }
            // Message d'erreur pour indiquer que pas de décompte à généré
            String[] param = new String[1];
            param[0] = caisse;

            JadeThread.logWarn(this.getClass().getName(),
                    "perseus.decision.imprimerdecisionfacturebuilder.pasdefacturepourcaisse", param);
        }
    }

    @Override
    public boolean isSendToGed() {
        return isSendToGed;
    }

    private void loadEntity() throws CatalogueTexteException, Exception {
        try {
            // chargement du membre de famille

            String codeIsoLangue = dossier.getDemandePrestation().getPersonneEtendue().getTiers().getLangue();

            // Test de chargement pour Babel du code isoLangue et de la methode load
            getBabelContainer().setCodeIsoLangue(getSession().getCode(codeIsoLangue));

            // Chargement du catalogue de texte de frais de maladie
            getBabelContainer().RegisterCtx(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS);

            // Chargement du catalogue de texte factures commune
            getBabelContainer().RegisterCtx(IPFCatalogueTextes.CS_FACTURES_COMMUNES);

            getBabelContainer().load();

            titreTiers = getSession().getCodeLibelle(
                    dossier.getDemandePrestation().getPersonneEtendue().getTiers().getTitreTiers());
        } catch (Exception e) {
            throw new DecisionException("ImprimerDecisionFactureBuilder / loadEntity -  NSS : "
                    + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    public void setAdrMail(String adrMail) {
        this.adrMail = adrMail;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setCsCaisse(String csCaisse) {
        this.csCaisse = csCaisse;
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }

    public void setFactureRentePont(FactureRentePont factureRentePont) {
        this.factureRentePont = factureRentePont;
    }

    public void setFacturesRentePont(List<FactureRentePont> facturesRentePont) {
        this.facturesRentePont = facturesRentePont;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdMbFamille(String idMbFamille) {
        this.idMbFamille = idMbFamille;
    }

    public void setListeDecomptes(Map<String, List<FactureRentePont>> listeDecomptes) {
        this.listeDecomptes = listeDecomptes;
    }

    @Override
    public void setSendToGed(boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }
}
