package ch.globaz.perseus.businessimpl.services.facture;

import globaz.externe.IPRConstantesExternes;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.interfaces.fx.PRGestionnaireHelper;
import globaz.prestation.tools.PRStringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.babel.business.exception.CatalogueTexteException;
import ch.globaz.perseus.business.constantes.CSCaisse;
import ch.globaz.perseus.business.constantes.CSEtatLot;
import ch.globaz.perseus.business.constantes.IPFCatalogueTextes;
import ch.globaz.perseus.business.constantes.IPFConstantes;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.dossier.Dossier;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.qd.CSTypeQD;
import ch.globaz.perseus.business.models.qd.Facture;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.document.AbstractDocumentBuilder;
import ch.globaz.perseus.businessimpl.utils.PFTypeImpressionEnum;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

public class ImprimerDecisionFactureBuilder extends AbstractFactureBuilder {

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

    private String csCaisse = null;
    private String dateDocument = null;
    private Demande derniereDemande = null;
    private Dossier dossier = null;
    private Facture facture = null;
    private List<Facture> factures = null;
    private boolean hasDecompte = false;
    private String idDossier = null;
    private String idMbFamille = "";
    private boolean isAgence = false;
    private boolean isSendToGed = false;
    private List<String> listeDAgence = null;
    private Map<String, List<Facture>> listeDecomptes;
    private String nss = null;
    private String nssAyantDroit = null;

    private String numDecision = "";

    private String numFacture = null;
    private String titreTiers = null;

    public ImprimerDecisionFactureBuilder() {

    }

    /**
     * Construction des documents selon le type d'impression
     * 
     * @param idUserAgence La liste des agences selectionnées sur l écran.
     * @param isAgence Si c'est le type agence.
     * @param typeImpression Le type d'impression : Original, Copies, Copies aux requérants
     * @return Un container de documents assemblés.
     * @throws DecisionException Une exception.
     * @throws JadeApplicationServiceNotAvailableException Une exception.
     * @throws JadePersistenceException Une exception.
     * @throws Exception Une exception.
     */
    public JadePrintDocumentContainer build(List<String> idUserAgence, boolean isAgence,
            PFTypeImpressionEnum typeImpression) throws DecisionException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, Exception {
        try {
            this.isAgence = isAgence;
            Set keys = listeDecomptes.keySet();

            // Un décompte crée par key
            for (java.util.Iterator iterator = keys.iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();
                List<String> liste = JadeStringUtil.split(key, ",");

                setFactures(listeDecomptes.get(key));
                setIdMbFamille(liste.get(1).replace(",", ""));
                setAnnee(liste.get(2).replace(",", ""));
                setIdDossier(liste.get(0).replace(",", ""));
                setFacture(factures.get(0));
                setDossier(PerseusServiceLocator.getDossierService().read(getIdDossier()));

                if (PFTypeImpressionEnum.COPIES_AUX_REQUERANTS.equals(typeImpression)) {
                    String idTiersAdresse = dossier.getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers();

                    // Dans le cadre des anciennes factures sans adresse de courrier, on prend l id tiers du requerant
                    if (liste.size() > 4) {
                        idTiersAdresse = liste.get(4).replace(",", "");
                    }

                    // Si l id tiers du requrant est la même que l id tiers de l adresse de courrier, nous creons pas de
                    // décomptes dans le pdf pour les copies des requerants
                    // JadeStringUtil.isBlankOrZero(idTiersAdresse) pour eviter d imprimer les anciens lot sans
                    // courriers.
                    if (dossier.getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers()
                            .equals(idTiersAdresse)
                            || JadeStringUtil.isBlankOrZero(idTiersAdresse)) {
                        // Ne va pas créer de décompte si le tiers de l adresse de courrier et le meme que le requérant
                        // Nous faisons a cette endroit pour éviter de charger le CdT, services etc... au lieu de le
                        // faire à la fin.
                        continue;
                    }
                }

                derniereDemande = PerseusServiceLocator.getDemandeService().getDerniereDemande(idDossier);

                if (!this.isAgence) {
                    // Chargement de la dernière demande
                    nss = derniereDemande.getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                            .getNumAvsActuel();

                    // Si la dernière demande du dossier concerne l'autre caisse et bien le cas n'est pas traité, il le
                    // sera
                    // dans la génération du décompte pour l'autre caisse
                    if (!csCaisse.equals(derniereDemande.getSimpleDemande().getCsCaisse())) {
                        continue;
                    }
                } else {
                    if (getFactures().get(0) != null) {
                        nss = getFactures().get(0).getQd().getQdAnnuelle().getDossier().getDemandePrestation()
                                .getPersonneEtendue().getPersonneEtendue().getNumAvsActuel();
                    }

                    csCaisse = CSCaisse.CCVD.getCodeSystem();
                }

                // Chargement de Babel
                this.loadEntity();
                // Création du décompte
                createDecompteSelonTypeImpression(typeImpression);
            }

            initLogWhenNoDecompte(idUserAgence, typeImpression);

            allDoc.setMergedDocDestination(getConteneurPubInfos().get(AbstractDocumentBuilder.PUBINFO_DOC_DESTINATION));

            return allDoc;

        } catch (Exception e) {
            throw new DecisionException("ImprimerDecisionFactureBuilder / build -  NSS : " + nss
                    + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    /**
     * Methode permettant de construire le tableau de décompte
     * 
     * @param unTableau Collection de factures
     * @param data Le document data
     * @param idTiersRequerant L'id tiers du requérant
     * @return Le document data passé en paramètre.
     * @throws Exception Une exception.
     */
    private DocumentData buildTableauDecompteFactureGarde(Collection unTableau, DocumentData data,
            String idTiersRequerant) throws Exception {
        try {
            // Création de variables pour additionner les montants saisies lors de chaque passage dans la boucle
            // ...Par facture...
            double montantTotalAdmis = 0.0;
            double montantTotalDemande = 0.0;
            double montantTotalExcedantRevenu = 0.0;
            double montantTotalDepassementQd = 0.0;
            double montantDemande = 0.0;

            // Renseignement des données du requerant en haut de page
            data.addData("AnneeDecompte", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 7, 1),
                    ImprimerDecisionFactureBuilder.CDT_ANNEE, annee));

            String numeroDecision = PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 7, 2),
                    ImprimerDecisionFactureBuilder.CDT_NUMERO_DECISION, numDecision);
            data.addData("DateDecisionDecompte", PRStringUtils.replaceString(numeroDecision,
                    ImprimerDecisionFactureBuilder.CDT_DATE_DECISION, dateDocument));

            data.addData("NumNssDecompte", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 7, 3),
                    ImprimerDecisionFactureBuilder.CDT_NUM_NSS, facture.getQd().getMembreFamille().getPersonneEtendue()
                            .getPersonneEtendue().getNumAvsActuel()));

            String ayantDroit = PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 7, 4),
                    ImprimerDecisionFactureBuilder.CDT_AYANT_DROIT, facture.getQd().getMembreFamille()
                            .getPersonneEtendue().getTiers().getDesignation1()
                            + " "
                            + facture.getQd().getMembreFamille().getPersonneEtendue().getTiers().getDesignation2());
            data.addData(
                    "AyantDroitDecompte",
                    PRStringUtils.replaceString(ayantDroit, ImprimerDecisionFactureBuilder.CDT_DATE_NAISSANCE, facture
                            .getQd().getMembreFamille().getPersonneEtendue().getPersonne().getDateNaissance()));

            DataList ligneZero = new DataList("typeLigneZero");
            // Renseignement du titre du décompte
            ligneZero.addData("TitreDecomptePrestation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 8, 2));
            // Renseignement des titre de colonnes du tableau
            ligneZero.addData("TitreMontantDemande",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 9, 1));
            ligneZero.addData("TitreMontantNonReconnu",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 9, 2));
            ligneZero.addData("TitreMontantAdmis",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 9, 3));
            unTableau.add(ligneZero);

            // Iteration pour chaque facture de la liste
            for (Object element : factures) {
                Facture fact = (Facture) element;

                if (CSTypeQD.FRAIS_GARDE.getCodeSystem().equals(fact.getQd().getTypeQD().getCodeSystem())) {
                    numFacture = fact.getSimpleFacture().getIdFacture();
                    // Création d'une DataList pour la deuxieme ligne du tableau
                    DataList ligneDeux = new DataList("typeLigneDeux");
                    // Renseignement de la valeur
                    ligneDeux.addData("DateFacturePrestation", PRStringUtils.replaceString(getBabelContainer()
                            .getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 10, 1),
                            ImprimerDecisionFactureBuilder.CDT_DATE_FACTURE, fact.getSimpleFacture().getDateFacture()));

                    // Renseignement des montants
                    ligneDeux
                            .addData("MontantDemandePeriodeRFM", convertCentimes(fact.getSimpleFacture().getMontant()));
                    montantDemande = Double.parseDouble(fact.getSimpleFacture().getMontant());
                    montantTotalDemande += montantDemande;

                    // Insertion de la valeur
                    unTableau.add(ligneDeux);

                    // Création d'une DataList pour la troisieme ligne du tableau
                    if (!JadeStringUtil.isEmpty(fact.getSimpleFacture().getFournisseur())) {
                        DataList ligneTrois = new DataList("typeLigneTrois");
                        // Renseignement de la valeur
                        ligneTrois.addData("BeneficiairePrestation", fact.getSimpleFacture().getFournisseur());
                        // Insertion de la valeur
                        unTableau.add(ligneTrois);
                    }
                    // Création d'une DataList pour la quatrième ligne du tableau
                    if (!JadeStringUtil.isEmpty(fact.getSimpleFacture().getDateDebutTraitement())) {
                        DataList ligneQuatre = new DataList("typeLigneQuatre");
                        // Remplacement de variable par le texte
                        String texteDateDebutPrestation = PRStringUtils
                                .replaceString(
                                        getBabelContainer().getTexte(
                                                IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 10, 2),
                                        ImprimerDecisionFactureBuilder.CDT_DATE_DEBUT_TRAITEMENT, fact
                                                .getSimpleFacture().getDateDebutTraitement());
                        // Renseignement de la valeur
                        ligneQuatre.addData("PeriodePrestation", PRStringUtils.replaceString(texteDateDebutPrestation,
                                ImprimerDecisionFactureBuilder.CDT_DATE_FIN_TRAITEMENT, fact.getSimpleFacture()
                                        .getDateFinTraitement()));

                        // Insertion de la valeur
                        unTableau.add(ligneQuatre);
                    }
                    if (!JadeStringUtil.isBlankOrZero(fact.getSimpleFacture().getCsMotif())) {
                        // Création d'une DataList pour la cinquième ligne du tableau
                        DataList ligneCinq = new DataList("typeLigneCinq");
                        // Renseignement de la valeur
                        if (IPFConstantes.CS_MOTIF_FACTURE_AUTRE.equals(fact.getSimpleFacture().getCsMotif())) {
                            ligneCinq.addData("DetailPrestation", "- " + fact.getSimpleFacture().getMotifLibre());
                        } else if (!JadeStringUtil.isEmpty(fact.getSimpleFacture().getCsMotif())) {
                            ligneCinq.addData("DetailPrestation",
                                    "- " + getSession().getCodeLibelle(fact.getSimpleFacture().getCsMotif()));
                        }
                        double montantAdmis = Double.parseDouble(fact.getSimpleFacture().getMontantRembourse());
                        double montantNonReconnu = montantDemande - montantAdmis;
                        // Insertion du montant non pris en charge
                        ligneCinq.addData("MontantNonReconnuPeriodeRFM",
                                convertCentimes(String.valueOf(montantNonReconnu)));

                        // Insertion de la valeur
                        unTableau.add(ligneCinq);
                    }

                    // Création d'une DataList pour la sixième ligne du tableau
                    DataList ligneSix = new DataList("typeLigneSix");
                    // Renseignement de la valeur
                    ligneSix.addData("TexteMontantTotal",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 10, 4));
                    ligneSix.addData("MontantTotal", convertCentimes(fact.getSimpleFacture().getMontantRembourse()));
                    montantTotalAdmis += Double.parseDouble(fact.getSimpleFacture().getMontantRembourse());
                    // Insertion de la valeur
                    unTableau.add(ligneSix);

                    // Incrémentation des variables qui vont additionner les factures
                    montantTotalDepassementQd += Double.parseDouble(fact.getSimpleFacture().getMontantDepassant());
                    montantTotalExcedantRevenu += Double.parseDouble(fact.getSimpleFacture()
                            .getExcedantRevenuCompense());

                    // Affichage de l'adresse de paiement si celle-ci diffère de l'adresse du requerant
                    if (!idTiersRequerant.equals(fact.getSimpleFacture().getIdTiersAdressePaiement())) {
                        // Récupération des information sur l'adresse de paiement
                        PersonneEtendueComplexModel pers = TIBusinessServiceLocator.getPersonneEtendueService().read(
                                fact.getSimpleFacture().getIdTiersAdressePaiement());
                        String adrPaiement = "";
                        if (JadeStringUtil.isEmpty(pers.getTiers().getDesignation1())) {
                            AdministrationComplexModel admin = TIBusinessServiceLocator.getAdministrationService()
                                    .read(fact.getSimpleFacture().getIdTiersAdressePaiement());
                            adrPaiement = getSession().getCodeLibelle(admin.getTiers().getTitreTiers()) + " "
                                    + admin.getTiers().getDesignation1() + " " + admin.getTiers().getDesignation2();
                        } else {
                            adrPaiement = getSession().getCodeLibelle(pers.getTiers().getTitreTiers()) + " "
                                    + pers.getTiers().getDesignation1() + " " + pers.getTiers().getDesignation2();
                        }

                        // Création d'une DataList pour la ligne adressePaiement
                        DataList ligneAdressePaiement = new DataList("ligneAdressePaiement");
                        // Renseignement de la valeur
                        ligneAdressePaiement.addData("AdressePaiement", PRStringUtils.replaceString(getBabelContainer()
                                .getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 10, 5),
                                ImprimerDecisionFactureBuilder.CDT_ADR_PAIEMENT, adrPaiement));
                        // Insertion de la valeur
                        unTableau.add(ligneAdressePaiement);
                    }
                }
            }

            // Insertion d'un tableau
            data.add(unTableau);

            // Création d'une DataList pour la septième ligne du tableau
            DataList ligneSept = new DataList("typeLigneSept");
            // Renseignement des montants finaux en bas de tableau
            ligneSept.addData("TexteMontantTotalACharge",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 11, 6));
            ligneSept.addData("MontantTotalACharge", convertCentimes(String.valueOf(montantTotalAdmis)));
            unTableau.add(ligneSept);

            // Création d'une DataList pour la huitième ligne du tableau si un montant est présent
            if (montantTotalDepassementQd > 0) {
                DataList ligneHuit = new DataList("typeLigneHuit");
                ligneHuit.addData("TexteDepassementQuotite",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 11, 2));
                ligneHuit.addData("MontantTotalQuotite", convertCentimes(String.valueOf(montantTotalDepassementQd)));
                unTableau.add(ligneHuit);
            }

            // Création d'une DataList pour la neuvième ligne du tableau
            DataList ligneNeuf = new DataList("typeLigneNeuf");
            ligneNeuf.addData("TexteExcedantRevenus",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 11, 3));
            ligneNeuf.addData("MontantTotalExcedant", convertCentimes(String.valueOf(montantTotalExcedantRevenu)));
            unTableau.add(ligneNeuf);

            // Création d'une DataList pour la onzième ligne du tableau
            DataList ligneOnze = new DataList("typeLigneOnze");
            ligneOnze.addData("TexteMontantTotalARembourser",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 11, 5));
            double totalRembourser = montantTotalAdmis;
            ligneOnze.addData("MontantARembourser", convertCentimes(String.valueOf(totalRembourser)));
            unTableau.add(ligneOnze);

            // Insertion du texte de versement en bas de tableau
            data.addData("RemarqueVersement",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 12, 1));

            return data;
        } catch (Exception e) {
            throw new DecisionException("buildTableauDecompteFactureGarde -  IdFacture : " + numFacture
                    + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    /**
     * Methode permettant de construire le tableau de décompte
     * 
     * @param unTableau Collection de factures.
     * @param data Le document data.
     * @param idTiersRequerant L'id tiers du requérant.
     * @return Le document data passé en paramètre.
     * @throws Exception Une exception.
     */
    private DocumentData buildTableauDecompteFactureMaladie(Collection unTableau, DocumentData data,
            String idTiersRequerant) throws Exception {
        try {
            // Création de variables pour additionner les montants saisies lors de chaque passage dans la boucle
            // ...Par facture...
            double montantTotalAdmis = 0.0;
            double montantTotalDemande = 0.0;
            double montantTotalExcedantRevenu = 0.0;
            double montantTotalDepassementQd = 0.0;
            double montantDemande = 0.0;

            // Renseignement des données du requerant en haut de page
            data.addData("AnneeDecompte", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 7, 1),
                    ImprimerDecisionFactureBuilder.CDT_ANNEE, annee));

            String numeroDecision = PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 7, 2),
                    ImprimerDecisionFactureBuilder.CDT_NUMERO_DECISION, numDecision);
            data.addData("DateDecisionDecompte", PRStringUtils.replaceString(numeroDecision,
                    ImprimerDecisionFactureBuilder.CDT_DATE_DECISION, dateDocument));

            data.addData("NumNssDecompte", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 7, 3),
                    ImprimerDecisionFactureBuilder.CDT_NUM_NSS, facture.getQd().getMembreFamille().getPersonneEtendue()
                            .getPersonneEtendue().getNumAvsActuel()));

            String ayantDroit = PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 7, 4),
                    ImprimerDecisionFactureBuilder.CDT_AYANT_DROIT, facture.getQd().getMembreFamille()
                            .getPersonneEtendue().getTiers().getDesignation1()
                            + " "
                            + facture.getQd().getMembreFamille().getPersonneEtendue().getTiers().getDesignation2());
            data.addData(
                    "AyantDroitDecompte",
                    PRStringUtils.replaceString(ayantDroit, ImprimerDecisionFactureBuilder.CDT_DATE_NAISSANCE, facture
                            .getQd().getMembreFamille().getPersonneEtendue().getPersonne().getDateNaissance()));

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
            for (Object element : factures) {
                Facture fact = (Facture) element;

                if (!CSTypeQD.FRAIS_GARDE.getCodeSystem().equals(fact.getQd().getTypeQD().getCodeSystem())) {
                    numFacture = fact.getSimpleFacture().getIdFacture();
                    // Création d'une DataList pour la premiere ligne du tableau
                    DataList ligneUne = new DataList("typeLigneUne");
                    // Renseignement de la valeur
                    ligneUne.addData("SujetPrestation",
                            getSession().getCodeLibelle(fact.getQd().getTypeQD().getCodeSystem()));
                    // Insertion de la valeur
                    unTableau.add(ligneUne);

                    // Création d'une DataList pour la deuxieme ligne du tableau
                    DataList ligneDeux = new DataList("typeLigneDeux");
                    // Renseignement de la valeur
                    ligneDeux.addData("DateFacturePrestation", PRStringUtils.replaceString(getBabelContainer()
                            .getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 10, 1),
                            ImprimerDecisionFactureBuilder.CDT_DATE_FACTURE, fact.getSimpleFacture().getDateFacture()));
                    // Renseignement des montants
                    ligneDeux
                            .addData("MontantDemandePeriodeRFM", convertCentimes(fact.getSimpleFacture().getMontant()));
                    montantDemande = Double.parseDouble(fact.getSimpleFacture().getMontant());
                    montantTotalDemande += montantDemande;

                    // Insertion de la valeur
                    unTableau.add(ligneDeux);

                    // Création d'une DataList pour la troisieme ligne du tableau
                    if (!JadeStringUtil.isEmpty(fact.getSimpleFacture().getFournisseur())) {
                        DataList ligneTrois = new DataList("typeLigneTrois");
                        // Renseignement de la valeur
                        ligneTrois.addData("BeneficiairePrestation", fact.getSimpleFacture().getFournisseur());
                        // Insertion de la valeur
                        unTableau.add(ligneTrois);
                    }

                    // Création d'une DataList pour la quatrième ligne du tableau
                    if (!JadeStringUtil.isEmpty(fact.getSimpleFacture().getDateDebutTraitement())) {
                        DataList ligneQuatre = new DataList("typeLigneQuatre");
                        // Remplacement de variable par le texte
                        String texteDateDebutPrestation = PRStringUtils
                                .replaceString(
                                        getBabelContainer().getTexte(
                                                IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 10, 2),
                                        ImprimerDecisionFactureBuilder.CDT_DATE_DEBUT_TRAITEMENT, fact
                                                .getSimpleFacture().getDateDebutTraitement());
                        // Renseignement de la valeur
                        ligneQuatre.addData("PeriodePrestation", PRStringUtils.replaceString(texteDateDebutPrestation,
                                ImprimerDecisionFactureBuilder.CDT_DATE_FIN_TRAITEMENT, fact.getSimpleFacture()
                                        .getDateFinTraitement()));

                        // Insertion de la valeur
                        unTableau.add(ligneQuatre);
                    }

                    if (!JadeStringUtil.isBlankOrZero(fact.getSimpleFacture().getCsMotif())) {
                        // Création d'une DataList pour la cinquième ligne du tableau
                        DataList ligneCinq = new DataList("typeLigneCinq");
                        // Renseignement de la valeur
                        if (IPFConstantes.CS_MOTIF_FACTURE_AUTRE.equals(fact.getSimpleFacture().getCsMotif())) {
                            ligneCinq.addData("DetailPrestation", "- " + fact.getSimpleFacture().getMotifLibre());
                        } else if (!JadeStringUtil.isEmpty(fact.getSimpleFacture().getCsMotif())) {
                            ligneCinq.addData("DetailPrestation",
                                    "- " + getSession().getCodeLibelle(fact.getSimpleFacture().getCsMotif()));
                        }
                        double montantAdmis = Double.parseDouble(fact.getSimpleFacture().getMontantRembourse());
                        double montantNonReconnu = montantDemande - montantAdmis;
                        // Insertion du montant non pris en charge
                        ligneCinq.addData("MontantNonReconnuPeriodeRFM",
                                convertCentimes(String.valueOf(montantNonReconnu)));

                        // Insertion de la valeur
                        unTableau.add(ligneCinq);
                    }

                    // Création d'une DataList pour la sixième ligne du tableau
                    DataList ligneSix = new DataList("typeLigneSix");
                    // Renseignement de la valeur
                    ligneSix.addData("TexteMontantTotal",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 10, 3));
                    ligneSix.addData("MontantTotal", convertCentimes(fact.getSimpleFacture().getMontantRembourse()));
                    montantTotalAdmis += Double.parseDouble(fact.getSimpleFacture().getMontantRembourse());
                    // Insertion de la valeur
                    unTableau.add(ligneSix);

                    // Incrémentation des variables qui vont additionner les factures
                    montantTotalDepassementQd += Double.parseDouble(fact.getSimpleFacture().getMontantDepassant());
                    montantTotalExcedantRevenu += Double.parseDouble(fact.getSimpleFacture()
                            .getExcedantRevenuCompense());

                    // Affichage de l'adresse de paiement si celle-ci diffère de l'adresse du requerant
                    if (!idTiersRequerant.equals(fact.getSimpleFacture().getIdTiersAdressePaiement())) {
                        // Récupération des information sur l'adresse de paiement
                        PersonneEtendueComplexModel pers = TIBusinessServiceLocator.getPersonneEtendueService().read(
                                fact.getSimpleFacture().getIdTiersAdressePaiement());
                        String adrPaiement = "";
                        if (JadeStringUtil.isEmpty(pers.getTiers().getDesignation1())) {
                            AdministrationComplexModel admin = TIBusinessServiceLocator.getAdministrationService()
                                    .read(fact.getSimpleFacture().getIdTiersAdressePaiement());
                            adrPaiement = getSession().getCodeLibelle(admin.getTiers().getTitreTiers()) + " "
                                    + admin.getTiers().getDesignation1() + " " + admin.getTiers().getDesignation2();
                        } else {
                            adrPaiement = getSession().getCodeLibelle(pers.getTiers().getTitreTiers()) + " "
                                    + pers.getTiers().getDesignation1() + " " + pers.getTiers().getDesignation2();
                        }

                        // Création d'une DataList pour la ligne adressePaiement
                        DataList ligneAdressePaiement = new DataList("ligneAdressePaiement");
                        // Renseignement de la valeur
                        ligneAdressePaiement.addData("AdressePaiement", PRStringUtils.replaceString(getBabelContainer()
                                .getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 10, 5),
                                ImprimerDecisionFactureBuilder.CDT_ADR_PAIEMENT, adrPaiement));
                        // Insertion de la valeur
                        unTableau.add(ligneAdressePaiement);
                    }
                }
            }

            // Insertion d'un tableau
            data.add(unTableau);

            // Création d'une DataList pour la septième ligne du tableau
            DataList ligneSept = new DataList("typeLigneSept");
            // Renseignement des montants finaux en bas de tableau
            ligneSept.addData("TexteMontantTotalACharge",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 11, 1));
            ligneSept.addData("MontantTotalACharge", convertCentimes(String.valueOf(montantTotalAdmis)));
            unTableau.add(ligneSept);

            // Création d'une DataList pour la huitième ligne du tableau si un montant est présent
            if (montantTotalDepassementQd > 0) {
                DataList ligneHuit = new DataList("typeLigneHuit");
                ligneHuit.addData("TexteDepassementQuotite",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 11, 2));
                ligneHuit.addData("MontantTotalQuotite", convertCentimes(String.valueOf(montantTotalDepassementQd)));
                unTableau.add(ligneHuit);
            }

            // Création d'une DataList pour la neuvième ligne du tableau
            DataList ligneNeuf = new DataList("typeLigneNeuf");
            ligneNeuf.addData("TexteExcedantRevenus",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 11, 3));
            ligneNeuf.addData("MontantTotalExcedant", convertCentimes(String.valueOf(montantTotalExcedantRevenu)));
            unTableau.add(ligneNeuf);

            // Création d'une DataList pour la onzième ligne du tableau
            DataList ligneOnze = new DataList("typeLigneOnze");
            ligneOnze.addData("TexteMontantTotalARembourser",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 11, 5));
            double totalRembourser = montantTotalAdmis;
            ligneOnze.addData("MontantARembourser", convertCentimes(String.valueOf(totalRembourser)));
            unTableau.add(ligneOnze);

            // Insertion du texte de versement en bas de tableau
            data.addData("RemarqueVersement",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 12, 1));

            return data;
        } catch (Exception e) {
            throw new DecisionException("buildTableauDecompteFactureMaladie -  idFacture : " + numFacture
                    + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    /**
     * Méthode permettant de générer un décompte selon les factures passées en paramètres.
     * 
     * @param isCopie Savoir si on veut la mention COPIE sur le document.
     * @param ged Savoir si on veut mettre le décompte en GED.
     * @throws Exception Une exception.
     */
    private void createDecompte(boolean isCopie, boolean ged) throws Exception {
        try {
            numDecision = "";

            // Chargement de l'ayant droit, de son nss et de son nip
            String ayantDroit = facture.getQd().getMembreFamille().getPersonneEtendue().getPersonneEtendue()
                    .getNumAvsActuel()
                    + " - "
                    + facture.getQd().getMembreFamille().getPersonneEtendue().getTiers().getDesignation1()
                    + " " + facture.getQd().getMembreFamille().getPersonneEtendue().getTiers().getDesignation2();
            nssAyantDroit = ayantDroit;
            // remplissage data pour document original
            DocumentData data = new DocumentData();

            // Récupération du gestionnaire dans le dossier
            String gestionnaire;
            if (isAgence) {
                gestionnaire = factures.get(0).getSimpleFacture().getIdGestionnaire();
            } else {
                gestionnaire = dossier.getDossier().getGestionnaire();
            }

            // Renseignement du premier paragraphe selon type de facture
            boolean isFraisGarde = false;
            boolean isFraisMaladie = false;
            String dateDecision = dateDocument;

            for (Facture fact : factures) {
                if (!JadeStringUtil.isEmpty(fact.getSimpleFacture().getNumDecision())) {
                    numDecision = fact.getSimpleFacture().getNumDecision();
                    dateDecision = fact.getSimpleFacture().getDateDecision();
                }

                if (CSTypeQD.FRAIS_GARDE.getCodeSystem().equals(fact.getQd().getTypeQD().getCodeSystem())) {
                    isFraisGarde = true;
                } else {
                    isFraisMaladie = true;
                }
            }

            // Chargement du titre du document pour le header
            String titreDocument = "";
            if (isFraisGarde && isFraisMaladie) {
                titreDocument = getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 1, 6);
            } else if (isFraisGarde) {
                titreDocument = getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 1, 5);
            } else {
                titreDocument = getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 1, 4);
            }

            // Insertion d'un bandeau si NON-COMPTABILISE
            boolean isBandeau = false;

            Lot lot = PerseusServiceLocator.getLotService().getLotForFacture(facture.getId());
            if (!CSEtatLot.LOT_VALIDE.getCodeSystem().equals(lot.getSimpleLot().getEtatCs())) {
                isBandeau = true;
            }

            if (derniereDemande.getSimpleDemande().getCoaching()) {
                titreDocument = titreDocument + "\n"
                        + getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 6, 8);
            }

            if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(csCaisse)) {
                data.addData("TitreDocument", titreDocument);
            }

            String idTiers;
            if (JadeStringUtil.isBlankOrZero(factures.get(0).getSimpleFacture().getIdTiersAdresseCourrier())) {
                idTiers = dossier.getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers();
            } else {
                idTiers = factures.get(0).getSimpleFacture().getIdTiersAdresseCourrier();
            }

            if (isAgence) {
                data = buildHeaderFacture(data, isCopie, idTiers, gestionnaire, csCaisse, dateDocument, isBandeau,
                        titreDocument, true);
            } else {
                data = buildHeaderFacture(data, isCopie, idTiers, gestionnaire, derniereDemande.getSimpleDemande()
                        .getCsCaisse(), dateDocument, isBandeau, titreDocument, true);
            }

            if (isAgence) {
                JadeUser users = PRGestionnaireHelper.getGestionnaire(factures.get(0).getSimpleFacture()
                        .getIdGestionnaire());

                if (!JadeStringUtil.isEmpty(users.getPhone())) {
                    data.addData("SERVICE_COLLABORATEUR",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 1, 3) + " " + users.getPhone());
                }
            }

            data.addData("idProcess", "PCFFactureRemboursementFrais");

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

            // Complément du Nip de l'ayant droit
            ayantDroit = ayantDroit + " / "
                    + getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 1, 2) + " "
                    + facture.getQd().getMembreFamille().getPersonneEtendue().getTiers().getIdTiers();

            // Renseignement de l'ayant droit
            data.addData("AyantDroit",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 1, 3) + " "
                            + ayantDroit);

            if (JadeStringUtil.isEmpty(numDecision)) {
                String cleDeCalcul = IPFConstantes.DECISION_CLE_INCREMENTATION;
                // Recuperation de l'increment
                String increment = JadePersistenceManager.incIndentifiant(cleDeCalcul);
                increment = JadeStringUtil.fillWithZeroes(increment, 6);
                numDecision = annee + "-" + increment;
            }

            // BZ 7776 Ne pas mettre à jour 2x la même facture
            ArrayList<String> factureADouble = new ArrayList<String>();
            for (Object element : factures) {
                Facture fact = (Facture) element;
                if (!factureADouble.contains(fact.getSimpleFacture().getIdFacture())) {
                    fact.getSimpleFacture().setNumDecision(numDecision);
                    fact.getSimpleFacture().setDateDecision(dateDecision);
                    PerseusServiceLocator.getFactureService().update(fact);
                    factureADouble.add(fact.getSimpleFacture().getIdFacture());
                }

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

            // Renseignement du titre du tiers
            data.addData("TitreTiers", titreTiers + ",");

            // Renseignement du numero de décision
            data.addData("NumDecision", numDecision);

            if (isFraisGarde && isFraisMaladie) {
                data.addData("Paragraphe1",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 3, 3));
            } else {
                if (isFraisGarde) {
                    data.addData("Paragraphe1",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 3, 2));

                } else {
                    data.addData("Paragraphe1",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 3, 1));

                }
            }

            // Renseignement du deuxieme paragraphe
            data.addData("Paragraphe2",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 4, 1));

            // Renseignement du titre et du texte 'Remarques'
            data.addData("TitreRemarque",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 4, 2));
            if (CSCaisse.CCVD.getCodeSystem().equals(csCaisse)) {
                // Texte spécifique pour la CCVD
                data.addData("RemarqueDebut",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 4, 3));

                if (derniereDemande.getSimpleDemande().getCoaching()) {
                    data.addData("RemarqueSouligne",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 4, 11));
                } else {
                    data.addData("RemarqueSouligne",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 4, 4));
                }

                data.addData("RemarqueFin",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 4, 5));
            } else {
                // Texte spécifique pour l'AGLAU mais si mesure de coaching, c'est le même que la CCVD
                if (derniereDemande.getSimpleDemande().getCoaching()) {
                    data.addData("RemarqueDebut",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 4, 3));
                    data.addData("RemarqueSouligne",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 4, 11));
                    data.addData("RemarqueFin",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 4, 5));
                } else {
                    data.addData("RemarqueDebut",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 4, 7));
                    data.addData("RemarqueSouligne",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 4, 8));
                    data.addData("RemarqueFin",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 4, 9));
                }
            }

            // Renseignement du dernier paragraphe 'Salutation'
            data.addData("Salutations", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 4, 6),
                    ImprimerDecisionFactureBuilder.CDT_TITRE_TIERS, titreTiers));

            // Ajout du texte de la copie si pas de mesure de coaching
            if (!derniereDemande.getSimpleDemande().getCoaching()) {
                // Insertion du titre et du texte Copie en bas de page
                data = buildBasDePageFacture(data, derniereDemande.getSimpleDemande().getIdAgenceCommunale(), true);
            }

            // Renseignement du titre 'Réclamation'
            data.addData("TitreReclamation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 5, 1));

            // Renseignement du texte 'reclamation'
            data.addData("TexteReclamation",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 5, 2));

            // Renseignement du bas de page
            data.addData("TexteNbrPage",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 6, 1));
            data.addData("SuiteTexteNbrPage",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS, 6, 2));

            // ################################# FIN DE PREMIERE PAGE #######################################

            // Récupération de l'id tiers requerant pour la passer aux buildTableaux
            String idTiersRequerant = dossier.getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers();

            // ################################# CONSTRUCTION DES TABLEAUX #######################################
            // Insertion du tableau si présence de frais de maladie
            if (isFraisMaladie) {
                data.addData("isTableauMaladieInclude", "TRUE");

                // Déclaration d'une collection
                Collection unTableau = new Collection("tableauDecompteFactureMaladiePerseus");
                data = buildTableauDecompteFactureMaladie(unTableau, data, idTiersRequerant);

            } else {
                data.addData("isTableauMaladieInclude", "FALSE");
            }

            // Insertion du tableau si présence de frais de garde
            if (isFraisGarde) {
                data.addData("isTableauGardeInclude", "TRUE");
                Collection unTableau = new Collection("tableauDecompteFactureGardePerseus");
                data = buildTableauDecompteFactureGarde(unTableau, data, idTiersRequerant);
            } else {
                data.addData("isTableauGardeInclude", "FALSE");
            }

            allDoc = dataAndPubInfoRecoVerso(allDoc, data, isCopie, ged,
                    IPRConstantesExternes.PCF_FACTURE_IMPRIMER_DECISION_FACTURE, true);
        } catch (Exception e) {
            throw new DecisionException("createDecompte -  NSS Ayant Droit : " + nssAyantDroit
                    + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    /**
     * Méthode permettant de créer des décomptes selon le type d'impression.
     * 
     * @param typeImpression Le type d'impression.
     * @throws Exception Une exception.
     */
    private void createDecompteSelonTypeImpression(PFTypeImpressionEnum typeImpression) throws Exception {
        // préparation des informations pour le mail et pour le document.
        createJadePublishDocInfo(
                dateDocument,
                adrMail,
                isSendToGed,
                dossier.getDemandePrestation().getDemandePrestation().getIdTiers(),
                getSession().getLabel("PDF_PF_FACTURE_REMBOURSEMENT_FRAIS_TITRE_MAIL") + " - "
                        + getSession().getCodeLibelle(csCaisse) + " - "
                        + getSession().getLabel(typeImpression.toString()));

        // Documents originaux
        if (PFTypeImpressionEnum.ORIGINAUX.equals(typeImpression)) {
            createDecompte(false, isSendToGed);
            hasDecompte = true;
        }

        // Documents copies aux requérants
        if (PFTypeImpressionEnum.COPIES_AUX_REQUERANTS.equals(typeImpression)) {
            String idGestionnaire;

            if (isAgence) {
                idGestionnaire = factures.get(0).getSimpleFacture().getIdGestionnaire();
            } else {
                idGestionnaire = dossier.getDossier().getGestionnaire();
            }

            // Entete créée pour chaque copie de décompte aux requérants
            createLettreEntete(dateDocument, dossier.getDemandePrestation().getPersonneEtendue().getTiers()
                    .getIdTiers(), facture.getQd().getMembreFamille().getPersonneEtendue().getTiers().getIdTiers(),
                    idGestionnaire);

            createDecompte(true, false);
        }
    }

    /**
     * Méthode permettant la génération d'une lettre d'entête avant le décompte.
     * 
     * @param dateDoc La date du document.
     * @param idTiers L'id tiers pour l'adresse.
     * @param idTiersConcerne L'id du tiers a afficher dans la mention CONCERNE : ....
     * @param idGestionnaire L'id L'id du gestionnaire.
     * @throws Exception Une exception.
     */
    private void createLettreEntete(String dateDoc, String idTiers, String idTiersConcerne, String idGestionnaire)
            throws Exception {
        DocumentData dataLettreEnTete = new DocumentData();

        dataLettreEnTete = buildLettreEntete(dataLettreEnTete, idTiers, idTiersConcerne, csCaisse, dateDoc,
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

    private List<Facture> getFactures() {
        return factures;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdMbFamille() {
        return idMbFamille;
    }

    public Map<String, List<Facture>> getListeDecomptes() {
        return listeDecomptes;
    }

    /**
     * Méthode permettant de logger si aucun décompte n'a pu être généré.
     * 
     * @param idUserAgence La liste des agences, si y'en a autrement c'est une caisse.
     */
    private void initLogWhenNoDecompte(List<String> idUserAgence, PFTypeImpressionEnum typeImpression) {
        if (!hasDecompte && PFTypeImpressionEnum.ORIGINAUX.equals(typeImpression)) {
            String caisse = "";

            if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(csCaisse)) {
                caisse = CSCaisse.AGENCE_LAUSANNE.toString();
            } else if (CSCaisse.CCVD.getCodeSystem().equals(csCaisse)) {
                caisse = CSCaisse.CCVD.toString();
            }
            // Message d'erreur pour indiquer qu'on n'a pas de décompte à généré avec le nom des caisses
            List<String> param = new ArrayList<String>();

            if (isAgence) {
                for (String idAgence : idUserAgence) {
                    param.add(idAgence);
                }
            } else {
                param.add(caisse);
            }

            String lesAgences = new String();
            for (String uneAgence : param) {
                if (lesAgences.equals("")) {
                    lesAgences = uneAgence;
                } else {
                    lesAgences += "; " + uneAgence;
                }
            }

            String[] arrayParam = new String[1];
            arrayParam[0] = lesAgences;

            JadeThread.logWarn(this.getClass().getName(),
                    "perseus.decision.imprimerdecisionfacturebuilder.pasdefacturepourcaisse", arrayParam);
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

            getBabelContainer().RegisterCtx(IPFCatalogueTextes.CS_FACTURES_REMBOURSEMENT_FRAIS);

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

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    public void setFactures(List<Facture> factures) {
        this.factures = factures;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdMbFamille(String idMbFamille) {
        this.idMbFamille = idMbFamille;
    }

    public void setListeDecomptes(Map<String, List<Facture>> listeDecomptes) {
        this.listeDecomptes = listeDecomptes;
    }

    @Override
    public void setSendToGed(boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

}
