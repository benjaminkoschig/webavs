/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.decision;

import globaz.framework.security.FWSecurityLoginException;
import globaz.framework.util.FWCurrency;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.perseus.utils.PFUserHelper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRStringUtils;
import java.util.ArrayList;
import java.util.Iterator;
import ch.globaz.perseus.business.calcul.OutputCalcul;
import ch.globaz.perseus.business.constantes.CSCaisse;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.CSEtatDemande;
import ch.globaz.perseus.business.constantes.CSTypeCreance;
import ch.globaz.perseus.business.constantes.CSTypeDemande;
import ch.globaz.perseus.business.constantes.CSTypeRetenue;
import ch.globaz.perseus.business.constantes.IPFCatalogueTextes;
import ch.globaz.perseus.business.constantes.IPFConstantes;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.creancier.Creancier;
import ch.globaz.perseus.business.models.decision.AnnexeDecision;
import ch.globaz.perseus.business.models.decision.CopieDecision;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.decision.DecisionOO;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.demande.DemandeSearchModel;
import ch.globaz.perseus.business.models.pcfaccordee.SimplePCFAccordee;
import ch.globaz.perseus.business.models.retenue.SimpleRetenue;
import ch.globaz.perseus.business.models.retenue.SimpleRetenueSearchModel;
import ch.globaz.perseus.business.models.situationfamille.Enfant;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamille;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamilleSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.decision.DecisionBuilder;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;
import ch.globaz.perseus.businessimpl.services.document.PlanDeCalculBuilder;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * @author MBO
 * 
 */
public class AbstractDecisionBuilder extends PlanDeCalculBuilder implements DecisionBuilder {

    private static final String CDT_DATE_DEBUT = "{dateDebut}";
    private static final String CDT_DATE_FIN = "{dateFin}";
    private static final String CDT_NBR_MOIS = "{nbrMois}";

    private static final String CDT_NIP = "{nipDecision}";
    private String dateDebutDecompte = null;
    private String dateFinDecompte = null;
    private DecisionOO decisionOO = null;
    private String gestionnaire = null;
    private String idAdresseCourrier = null;
    private String idDecision = null;
    private String idDemande = null;
    private String idTiers = null;
    private boolean isNotImpotSource = false;
    private boolean isPlusieursAnnexes = false;
    private boolean isRestitution = false;
    private FWCurrency montantCreancier = new FWCurrency("0.00");
    private Float montantDecompte = null;
    private Float montantDuDecompte = null;
    Float montantRetro = new Float(0);
    private Float montantTotalDecompte = null;
    private Float montantVerseDecompte = null;
    private int nbChoixAideCat = 0;
    private int nbrMoisDecompte = 0;
    private String numeroDecision = null;
    public String titreTiers = null;

    public AbstractDecisionBuilder() {

    }

    @Override
    public JadePrintDocumentContainer build(String decisionId, String mailGest, String dateDoc, boolean isSendToGed)
            throws DecisionException, JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {
        // TODO Auto-generated method stub
        return null;
    }

    // Methode permettant de construir le bas de page de document pour les décisions.
    protected DocumentData buildBasDePage(DocumentData data, boolean copieAgence, boolean copieRi, boolean copieOcc)
            throws Exception {

        int nbCopie = 0;

        try {
            AdministrationComplexModel AgenceSociale = TIBusinessServiceLocator.getAdministrationService().read(
                    getDecisionOO().getDemande().getSimpleDemande().getIdAgenceCommunale());

            String texteCopie = "";

            // Copie pour l'agence sociale
            if (copieAgence == true) {
                if (!IPFConstantes.ID_AGENCE_LAUSANNE.equals(getDecisionOO().getDemande().getSimpleDemande()
                        .getIdAgenceCommunale())) {
                    texteCopie = AgenceSociale.getTiers().getDesignation1() + " "
                            + AgenceSociale.getTiers().getDesignation2();
                    nbCopie++;
                } else {
                    if (CSCaisse.CCVD.getCodeSystem().equals(
                            getDecisionOO().getDemande().getSimpleDemande().getCsCaisse())) {
                        texteCopie = AgenceSociale.getTiers().getDesignation1() + " "
                                + AgenceSociale.getTiers().getDesignation2();
                        nbCopie++;
                    }
                }
            }

            // Copie pour le RI
            if (copieRi == true) {
                if (!JadeStringUtil.isBlankOrZero(getDecisionOO().getDemande().getSimpleDemande().getIdAgenceRi())) {
                    AdministrationComplexModel Ri = TIBusinessServiceLocator.getAdministrationService().read(
                            getDecisionOO().getDemande().getSimpleDemande().getIdAgenceRi());
                    if (JadeStringUtil.isEmpty(texteCopie)) {
                        texteCopie = Ri.getTiers().getDesignation1() + " " + Ri.getTiers().getDesignation2();
                        nbCopie++;
                    } else {
                        texteCopie = texteCopie + "\n" + Ri.getTiers().getDesignation1() + " "
                                + Ri.getTiers().getDesignation2();
                        nbCopie++;
                    }

                }
            }

            // Copie pour l'OCC
            if (copieOcc == true) {
                nbCopie++;
                if (JadeStringUtil.isEmpty(texteCopie)) {
                    texteCopie = getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 5, 3);
                } else {
                    texteCopie = texteCopie + "\n" + getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 5, 3);
                }
            }

            // Copie pour les tiers
            for (Iterator iterator = getListeCopies().iterator(); iterator.hasNext();) {
                nbCopie++;
                String nomTierscopie = (String) iterator.next();
                texteCopie = texteCopie + "\n" + nomTierscopie;
            }
            if (nbCopie > 0) {
                data.addData("TexteCopie", texteCopie);
                if (nbCopie > 1) {
                    data.addData("Copie", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 5, 2));
                } else {
                    data.addData("Copie", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 6, 5));
                }

            }

            // Insertion du titre et du texte pour l'annexe
            String annexes = getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 6, 3);

            // Ajouter BVR en bas de page si le montant final est négatif
            if (isRestitution) {
                isPlusieursAnnexes = true;
                annexes = annexes + ", " + getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 6, 4);
            }
            for (Iterator iterator = getListeAnnexeDecision().iterator(); iterator.hasNext();) {
                isPlusieursAnnexes = true;
                String nomAnnexe = (String) iterator.next();
                annexes = annexes + ", " + nomAnnexe;
            }
            if (isPlusieursAnnexes) {
                data.addData("Annexe", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 6, 7));
            } else {
                data.addData("Annexe", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 6, 1));
            }

            data.addData("TexteAnnexe", annexes);

            return data;
        } catch (Exception e) {
            throw new DecisionException("AbstractDecisionBuilder - erreur dans le buildBasDePage :" + e.toString(), e);
        }
    }

    // Methode permettant de construire l'en-tête d'un document pour les décisions
    protected DocumentData buildHeaderDecision(DocumentData data, boolean isProjet, boolean isCopie,
            boolean isLettreEnTete, String dateDoc, String titreDocument) throws FWSecurityLoginException, Exception {
        try {

            data = buildHeader(data, isCopie, isLettreEnTete, idAdresseCourrier, gestionnaire, getDecisionOO()
                    .getDemande().getSimpleDemande().getCsCaisse(), dateDoc, titreDocument, false);

            // Affiche un bandeau sur le document généré s'il n'est pas validé
            if (!CSEtatDecision.VALIDE.getCodeSystem().equals(decisionOO.getSimpleDecision().getCsEtat())) {
                data.addData("isNonValide", "TRUE");
                data.addData("TexteBandeau", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 4, 1));
            } else {
                data.addData("isNonValide", "FALSE");
            }

            // Modifie le texte "Décision numero" selon le type de décision
            if (isProjet) {
                data.addData("DecisionNumero",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 2, 2) + " "
                                + decisionOO.getSimpleDecision().getNumeroDecision());
            } else {
                data.addData("DecisionNumero",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 2, 1) + " "
                                + decisionOO.getSimpleDecision().getNumeroDecision());
            }

            // Insertion du NIP dans l'entête de la décision
            data.addData("Nip", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 2, 3),
                    AbstractDecisionBuilder.CDT_NIP, decisionOO.getDemande().getDossier().getDemandePrestation()
                            .getPersonneEtendue().getTiers().getIdTiers()));

            // Insertion des coordonnées de l'ayant droit et de son numero d'AVS
            data.addData("AyantDroit", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 3, 1)
                    + " "
                    + decisionOO.getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                            .getPersonneEtendue().getNumAvsActuel()
                    + " - "
                    + decisionOO.getDemande().getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                            .getDesignation2()
                    + " "
                    + decisionOO.getDemande().getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                            .getDesignation1());

            return data;
        } catch (Exception e) {
            throw new DecisionException(
                    "AbstractDecisionBuilder - Erreur dans le buildHeaderDecision :" + e.toString(), e);
        }
    }

    // Création d'une méthode qui va appeler les différents éléments du tableau
    protected DocumentData buildTableau(DocumentData data) throws Exception {
        Collection unTableau = new Collection("tableauPerseus");
        // Appel de la première partie du tableau
        String dateDebut = PRStringUtils.replaceString(
                getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 5, 1),
                AbstractDecisionBuilder.CDT_DATE_DEBUT, dateDebutDecompte);
        // Construction du titre du tableau
        data.addData("TitreDecompte",
                PRStringUtils.replaceString(dateDebut, AbstractDecisionBuilder.CDT_DATE_FIN, dateFinDecompte));

        buildTableauDecompte(unTableau);

        // Appel de la deuxième partie du tableau, selon condition
        if (getNombreCreancier() != 0) {
            buildTableauCreancier(unTableau);
        } else if (montantRetro < 0) {
            buildTableauImpotSource(unTableau);
        }

        // Appel de la troisième partie du tableau
        buildTableauSolde(unTableau);
        data.add(unTableau);
        return data;
    }

    // Methode permettant de construire la deuxième partie "Créancier" du tableau
    private FWCurrency buildTableauCreancier(Collection tab) throws Exception {
        // Réinitialisation de la variable
        montantCreancier = new FWCurrency(0);

        // itteration de chaque créancier
        Creancier creancierImpotSource = null;
        for (Creancier ca : getDecisionOO().getCreancier()) {
            if (!CSTypeCreance.TYPE_CREANCE_IMPOT_SOURCE.getCodeSystem().equals(
                    ca.getSimpleCreancier().getCsTypeCreance())) {
                if (isNotImpotSource == false) {
                    // Création d'une DataList pour la sixième ligne du tableau
                    DataList ligneSix = new DataList("typeLigneSix");
                    // Construction du texte de la sixième ligne
                    ligneSix.addData("AvanceAccordee",
                            getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 7, 8));
                    tab.add(ligneSix);
                    // Affectation au boolean pour ne pas réitérer le label
                    isNotImpotSource = true;
                }

                // Création d'une DataList pour la septième ligne
                DataList ligneSept = new DataList("typeLigneSept");
                ligneSept.addData("NomCSR", getNomCreancier(ca.getSimpleCreancier().getIdCreancier()));
                ligneSept.addData("CHF", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 7, 4));
                ligneSept.addData("MontantCSR", convertCentimes(ca.getSimpleCreancier().getMontantRevendique()));
                ligneSept.addData("CHFM", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 7, 6));
                ligneSept.addData("MontantAccorde", convertCentimes(ca.getSimpleCreancier().getMontantAccorde()));
                montantCreancier.add(ca.getSimpleCreancier().getMontantAccorde());
                tab.add(ligneSept);
            } else {
                creancierImpotSource = ca;
            }
        }

        if (null != creancierImpotSource) {
            // Création d'une DataList pour la septième ligne
            DataList ligneSept = new DataList("typeLigneSept");
            ligneSept.addData("NomCSR", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 7, 11));
            ligneSept.addData("CHF", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 7, 4));
            ligneSept.addData("MontantCSR", convertCentimes(creancierImpotSource.getSimpleCreancier()
                    .getMontantRevendique()));
            ligneSept.addData("CHFM", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 7, 6));
            ligneSept.addData("MontantAccorde", convertCentimes(creancierImpotSource.getSimpleCreancier()
                    .getMontantAccorde()));
            montantCreancier.add(creancierImpotSource.getSimpleCreancier().getMontantAccorde());
            tab.add(ligneSept);
        }

        return montantCreancier;
    }

    // Methode permettant de construire la première partie "Decompte" du tableau
    private void buildTableauDecompte(Collection tab) throws Exception {
        // Création d'une dataList pour la deuxième ligne du tableau
        DataList ligneDeux = new DataList("typeLigneDeux");
        // Construction du texte de la deuxième ligne, selon condition
        if (!JadeStringUtil.isEmpty(getDecisionOO().getSimpleDecision().getRemarqueUtilisateur())) {
            ligneDeux.addData("TextePriseEnCompte",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 7, 1));
            // Insertion de la deuxième ligne
            tab.add(ligneDeux);
        }
        // Création d'une dataList pour la troisième ligne du tableau
        DataList ligneTrois = new DataList("typeLigneTrois");
        // Construction du texte de la troisième ligne
        ligneTrois.addData("DebutDroit", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 7, 2));
        // Construction du nbr de mois
        ligneTrois.addData("Mois", PRStringUtils.replaceString(
                getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 7, 3),
                AbstractDecisionBuilder.CDT_NBR_MOIS, String.valueOf(nbrMoisDecompte)));
        // Construction de la devise monétaire
        ligneTrois.addData("CHF", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 7, 4));
        // Construction du montant initial
        ligneTrois.addData("MontantUnitaire", convertCentimes(String.valueOf(montantDecompte)));
        // Construction du montant total
        ligneTrois.addData("MontantTotal1", convertCentimes(String.valueOf(montantDuDecompte)));
        // Insertion de la troisième ligne
        tab.add(ligneTrois);

        // Création d'une dataList pour la quatrième ligne du tableau
        DataList ligneQuatre = new DataList("typeLigneQuatre");
        // Construction du texte de a quatrième ligne
        ligneQuatre.addData("PrestationDejaVersee",
                getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 7, 5));
        // Construction de la devise monétaire
        ligneQuatre.addData("CHFM", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 7, 6));
        // Construction du montant déjà versé
        ligneQuatre.addData("MontantVerse", convertCentimes(String.valueOf(montantVerseDecompte)));
        // Insertion de la quatrième ligne
        tab.add(ligneQuatre);

        // Création d'une DataList pour la cinquième ligne
        DataList ligneCinq = new DataList("typeLigneCinq");
        // Construction du texte de la cinquième ligne
        ligneCinq.addData("TexteMontantTotal",
                getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 7, 7));
        // Construction de la devise monaitaire, selon condition
        if (montantTotalDecompte < 0) {
            ligneCinq.addData("CHF", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 7, 6));
        } else {
            ligneCinq.addData("CHF", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 7, 4));
        }
        // Construction du montant total
        ligneCinq.addData("MontantTotalDroit", convertCentimes(Float.toString(Math.abs((montantTotalDecompte)))));
        // Insertion de la ligne cinq
        tab.add(ligneCinq);
    }

    private FWCurrency buildTableauImpotSource(Collection tab) throws Exception {
        // je Commence par calculer le montant de la restitution impôt source
        Float montantImpotSourceVerse = PerseusServiceLocator.getDemandeService().calculerMontantVerseImpotSource(
                getDecisionOO().getDemande());
        // BZ 8543
        montantCreancier = new FWCurrency(0);

        if (0 != montantImpotSourceVerse) {
            // Création d'une DataList pour la septième ligne
            DataList ligneSept = new DataList("typeLigneSept");
            ligneSept.addData("NomCSR", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 7, 11));
            // ligneSept.addData("CHF", this.getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 7,
            // 4));
            // ligneSept.addData("MontantCSR",
            // this.convertCentimes(creancierImpotSource.getSimpleCreancier().getMontantRevendique()));
            ligneSept.addData("CHFM", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 7, 4));
            ligneSept.addData("MontantAccorde", convertCentimes(montantImpotSourceVerse.toString()));
            montantCreancier.add(montantImpotSourceVerse.toString());
            tab.add(ligneSept);
        }
        return montantCreancier;

    }

    // Methode permettant de construire la troisième partie "Solde" du tableau
    private void buildTableauSolde(Collection tab) throws Exception {
        // Création d'une DataList pour la huitième ligne
        DataList ligneHuit = new DataList("typeLigneHuit");

        // Construction de la devise monaitaire
        ligneHuit.addData("CHF", getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 7, 4));

        // Récupération du montant final, selon condition
        Float montantFinal = null;

        if ((getNombreCreancier() == 0) && !montantCreancier.equals("0.00")) {
            montantFinal = montantTotalDecompte + montantCreancier.floatValue();
        } else if (getNombreCreancier() == 0) {
            montantFinal = montantTotalDecompte;
        } else {
            montantFinal = montantTotalDecompte - montantCreancier.floatValue();
        }
        if (CSTypeDemande.AIDES_CATEGORIELLES.getCodeSystem().equals(
                decisionOO.getDemande().getSimpleDemande().getTypeDemande())) {

            if (nombreChoixAideCategorielle() == 1) {
                ligneHuit.addData("TexteVersement",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 10, 1));

            } else {
                ligneHuit.addData("TexteVersement",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 10, 2));
            }
        } else {
            if (montantFinal < 0) {
                ligneHuit.addData("TexteVersement",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 7, 10));
                // Permet d'afficher dans le bas de page, la metion 'BVR'
                isRestitution = true;
            } else {
                ligneHuit.addData("TexteVersement",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 7, 9));
            }
        }

        String stringMF = convertCentimes(Float.toString(Math.abs(montantFinal)));

        // Construction du montant final
        ligneHuit.addData("MontantFinal", stringMF);
        tab.add(ligneHuit);
    }

    public void createJadePublishDocumentInfo(String dateDoc, String adMail, boolean isSendToGed, String titreMail)
            throws Exception {
        createJadePublishDocInfo(dateDoc, adMail, isSendToGed, idTiers, titreMail);
    }

    public DocumentData createLettreEnTete(String dateDoc, String idTiersAdresseCourrier) throws Exception {
        DocumentData data = new DocumentData();
        // data = this.buildLettreEntete(data, this.idAdresseCourrier, idTiersCopie, this.idDemande, dateDoc,
        // this.gestionnaire, this.numeroDecision);
        data = buildLettreEntete(data, idTiersAdresseCourrier, idTiers, getDecisionOO().getDemande().getSimpleDemande()
                .getCsCaisse(), dateDoc, gestionnaire);
        return data;
    }

    public DocumentData createPlanDeCalcul(String dateDoc, boolean isCopy, DocumentData data) throws Exception {
        // DocumentData data = new DocumentData();
        OutputCalcul calcul = PerseusServiceLocator.getPCFAccordeeService().deserializeCalcul(
                decisionOO.getPcfAccordee().getCalcul());
        // TODO Voir pour implémenter 'calcul' plus tôt, et le passer en paramètre au builder
        data = buildPlanDeCalcul(data, dateDoc, getDecisionOO(), calcul, isCopy);
        return data;
    }

    public Decision decisionPrecedente(Demande demande) {
        Demande demandePrecedante = null;
        Decision decisionPrecedante = null;
        try {

            // Retrouver la date à laquelle on va rechercher une demande active
            String dateFin = "";
            if (JadeStringUtil.isEmpty(demande.getSimpleDemande().getDateFin())) {
                // Cas où la demande sera active après
                dateFin = JadeDateUtil.addDays(JadeDateUtil.addMonths("01."
                        + PerseusServiceLocator.getPmtMensuelService().getDateDernierPmt(), 1), -1);
            } else if (!JadeDateUtil.isDateMonthYearAfter(demande.getSimpleDemande().getDateFin().substring(3),
                    PerseusServiceLocator.getPmtMensuelService().getDateDernierPmt())) {
                // Cas où la demande est purement retro active
                dateFin = demande.getSimpleDemande().getDateFin();
            } else {
                // cas normalement impossible puisque ca voudrait dire que la demande se ferme dans le future et se
                // n'est pas possible
                // mais on prend quand même le dernier paiement mensuel
                dateFin = JadeDateUtil.addDays(JadeDateUtil.addMonths("01."
                        + PerseusServiceLocator.getPmtMensuelService().getDateDernierPmt(), 1), -1);
            }

            DemandeSearchModel ds = new DemandeSearchModel();
            ds.setForIdDossier(demande.getSimpleDemande().getIdDossier());
            ds.setForNotIdDemande(demande.getId());
            ds.setForCsEtatDemande(CSEtatDemande.VALIDE.getCodeSystem());
            ds.setForDateValable(dateFin);
            ds.setOrderBy(DemandeSearchModel.ORDER_BY_DATETIME_DECISION_DESC);
            ds = PerseusServiceLocator.getDemandeService().search(ds);
            // On peut considérer que la première est la dernière décision prise
            if (ds.getSize() > 0) {
                demandePrecedante = (Demande) ds.getSearchResults()[0];
            } else {

                ds.setForIdDossier(demande.getSimpleDemande().getIdDossier());
                ds.setForNotIdDemande(demande.getId());
                ds.setForCsEtatDemande(CSEtatDemande.VALIDE.getCodeSystem());
                ds.setForDateValable(JadeDateUtil.addDays(demande.getSimpleDemande().getDateDebut(), -1));
                ds.setOrderBy(DemandeSearchModel.ORDER_BY_DATETIME_DECISION_DESC);
                ds = PerseusServiceLocator.getDemandeService().search(ds);

                if (ds.getSize() > 0) {
                    demandePrecedante = (Demande) ds.getSearchResults()[0];
                }

            }

            if (null != demandePrecedante) {
                Decision decision = null;
                DecisionSearchModel decisionSearchModel = new DecisionSearchModel();
                decisionSearchModel.setForIdDemande(demandePrecedante.getSimpleDemande().getIdDemande());
                decisionSearchModel = PerseusServiceLocator.getDecisionService().search(decisionSearchModel);

                for (JadeAbstractModel model : decisionSearchModel.getSearchResults()) {
                    decisionPrecedante = (Decision) model;
                    break;
                }
            }

            return decisionPrecedante;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    // Methode permettant d'obtenir l'adresse de paiement d'un tiers
    public String getAdressePaiement() throws Exception {
        AdresseTiersDetail adresseTiersDetail = PFUserHelper.getAdressePaiementAssure(getDecisionOO()
                .getSimpleDecision().getIdTiersAdressePaiement(), getDecisionOO().getSimpleDecision()
                .getIdDomaineApplicatifAdressePaiement(), JACalendar.todayJJsMMsAAAA());

        String numeroCompte = "";
        if (adresseTiersDetail.getFields() != null) {
            if (JadeStringUtil.isEmpty(adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_COMPTE))) {
                return numeroCompte = adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_CCP);
            } else {
                return numeroCompte = adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_COMPTE);
            }
        } else {
            throw new DecisionException("AbstractDecisionBuilder - Absence d'adresse de paiement");
        }
    }

    public DecisionOO getDecisionOO() {
        return decisionOO;
    }

    protected void getDecompte() throws Exception {
        // Récupération de la date de début
        dateDebutDecompte = getDecisionOO().getDemande().getSimpleDemande().getDateDebut();
        // Récupération de la date de fin
        if (CSEtatDecision.VALIDE.getCodeSystem().equals(getDecisionOO().getSimpleDecision().getCsEtat())) {
            dateFinDecompte = getDecisionOO().getPrestation().getSimplePrestation().getDateFin();
        } else if (!JadeStringUtil.isEmpty(getDecisionOO().getDemande().getSimpleDemande().getDateFin())) {
            dateFinDecompte = getDecisionOO().getDemande().getSimpleDemande().getDateFin();
        } else {
            dateFinDecompte = JadeDateUtil
                    .addDays(
                            JadeDateUtil.addMonths("01."
                                    + PerseusServiceLocator.getPmtMensuelService().getDateDernierPmt(), 1), -1);
        }

        // Récupération du nombre de mois
        nbrMoisDecompte = JadeDateUtil.getNbMonthsBetween(dateDebutDecompte, dateFinDecompte);

        // Récupération du montant initial
        if (!JadeStringUtil.isEmpty(getDecisionOO().getPcfAccordee().getMontant())) {
            montantDecompte = Float.parseFloat(getDecisionOO().getPcfAccordee().getMontant());
        } else {
            montantDecompte = (float) 0.0;
        }

        // Calcul du montant du
        montantDuDecompte = nbrMoisDecompte * montantDecompte;

        // Récupération du montant versé et du montant total
        if (CSEtatDecision.VALIDE.getCodeSystem().equals(getDecisionOO().getSimpleDecision().getCsEtat())) {
            montantTotalDecompte = Float.parseFloat(getDecisionOO().getPrestation().getSimplePrestation()
                    .getMontantTotal());
            montantVerseDecompte = montantDuDecompte - montantTotalDecompte;
        } else {
            montantVerseDecompte = PerseusServiceLocator.getDossierService().calculerMontantVerse(
                    getDecisionOO().getDemande().getDossier(),
                    PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dateDebutDecompte),
                    PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dateFinDecompte));
            montantTotalDecompte = PerseusServiceLocator.getDemandeService().calculerRetro(
                    getDecisionOO().getDemande(), dateFinDecompte);
        }
    }

    // Methode permettant de retourner la liste des annexes pour une décision
    protected ArrayList<String> getListeAnnexeDecision() throws Exception {
        ArrayList<String> listeAnnexes = new ArrayList<String>();

        for (AnnexeDecision annexeDecision : getDecisionOO().getListeAnnexe()) {
            AnnexeDecision annexe = PerseusServiceLocator.getAnnexeDecisionService().read(annexeDecision.getId());

            String nom = annexe.getSimpleAnnexeDecision().getDescriptionAnnexe();
            listeAnnexes.add(nom);
        }
        return listeAnnexes;
    }

    // Methode permettant de retourner la liste des copies pour une décision
    protected ArrayList<String> getListeCopies() throws Exception {
        ArrayList<String> listeCopies = new ArrayList<String>();

        for (CopieDecision copieDecision : getDecisionOO().getListeCopie()) {
            CopieDecision copie = PerseusServiceLocator.getCopieDecisionService().read(copieDecision.getId());

            String nom = copie.getDesignation1() + " " + copie.getDesignation2();
            listeCopies.add(nom);
        }
        return listeCopies;
    }

    // Methode permettant de retounrer la liste des membres de la famille
    protected String getMembreFamille(DocumentData data) throws Exception {
        String PersonneCompriseCalcul = "";
        // Ajout du requerant
        PersonneCompriseCalcul = getDecisionOO().getDemande().getSituationFamiliale().getRequerant().getMembreFamille()
                .getPersonneEtendue().getTiers().getDesignation1()
                + " "
                + getDecisionOO().getDemande().getSituationFamiliale().getRequerant().getMembreFamille()
                        .getPersonneEtendue().getTiers().getDesignation2();

        // Ajout du conjoint pour autant qu'il y en ai un
        if (!JadeStringUtil.isEmpty(getDecisionOO().getDemande().getSituationFamiliale().getConjoint().getId())) {
            PersonneCompriseCalcul = PersonneCompriseCalcul
                    + ", "
                    + getDecisionOO().getDemande().getSituationFamiliale().getConjoint().getMembreFamille()
                            .getPersonneEtendue().getTiers().getDesignation1()
                    + " "
                    + getDecisionOO().getDemande().getSituationFamiliale().getConjoint().getMembreFamille()
                            .getPersonneEtendue().getTiers().getDesignation2();
        }

        // Ajout des enfants
        for (Iterator iterator = getDecisionOO().getListeEnfants().iterator(); iterator.hasNext();) {
            Enfant enfant = (Enfant) iterator.next();

            PersonneCompriseCalcul = PersonneCompriseCalcul + ", "
                    + enfant.getMembreFamille().getPersonneEtendue().getTiers().getDesignation1() + " "
                    + enfant.getMembreFamille().getPersonneEtendue().getTiers().getDesignation2();
        }
        return PersonneCompriseCalcul;
    }

    // Methode permettant d'obtenir la quantité de créanciers pour une demande
    protected int getNombreCreancier() throws Exception {
        return getDecisionOO().getCreancier().size();
    }

    protected String getNomCreancier(String idCreancier) throws Exception {
        Creancier creancier = PerseusServiceLocator.getCreancierService().read(idCreancier);
        return creancier.getSimpleTiers().getDesignation1() + " " + creancier.getSimpleTiers().getDesignation2();
    }

    // Methode permettant de retourner le numero NSS du tiers
    protected String getNssNomTiers() {
        String nssNom = getDecisionOO().getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                .getPersonneEtendue().getNumAvsActuel()
                + " - "
                + decisionOO.getDemande().getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                        .getDesignation1()
                + " "
                + decisionOO.getDemande().getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                        .getDesignation2();
        return nssNom;
    }

    protected String getRetenueImpotSource(SimplePCFAccordee pcfAccordee) throws Exception {

        String montantRetenue = null;
        SimpleRetenueSearchModel retenueSearch = new SimpleRetenueSearchModel();
        retenueSearch.setForIdPcfAccordee(pcfAccordee.getId());
        retenueSearch.setForCsTypeRetenue(CSTypeRetenue.IMPOT_SOURCE.getCodeSystem());
        retenueSearch = PerseusImplServiceLocator.getSimpleRetenueService().search(retenueSearch);

        for (JadeAbstractModel retenueModel : retenueSearch.getSearchResults()) {
            SimpleRetenue retenue = (SimpleRetenue) retenueModel;

            if (!JadeStringUtil.isEmpty(retenue.getMontantRetenuMensuel())) {
                montantRetenue = retenue.getMontantRetenuMensuel();
            }
        }

        return montantRetenue;

    }

    // Récupération du nom, prenom et dates de période pour les afficher dans le sujet du mail reçu.
    protected String getSujetMail() {
        String requerant = "";
        // Retourne le requerant avec les date de début et date de fin si il en a une
        if (!JadeStringUtil.isEmpty(getDecisionOO().getDemande().getSimpleDemande().getDateFin())) {
            requerant = getDecisionOO().getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                    .getTiers().getDesignation1()
                    + " "
                    + getDecisionOO().getDemande().getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                            .getDesignation2()
                    + " "
                    + "-"
                    + " "
                    + getDecisionOO().getDemande().getSimpleDemande().getDateDebut()
                    + "-"
                    + getDecisionOO().getDemande().getSimpleDemande().getDateFin();
        } else {
            // Retourne le requerant avec les date de début uniquement
            requerant = getDecisionOO().getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                    .getTiers().getDesignation1()
                    + " "
                    + getDecisionOO().getDemande().getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                            .getDesignation2()
                    + " "
                    + "-"
                    + " "
                    + getDecisionOO().getDemande().getSimpleDemande().getDateDebut() + "-" + "...";
        }
        return requerant;
    }

    // Methode permettant de savoir si il y a des enfants de moins de 16ans dans le ménage
    protected Boolean hasEnfant() throws Exception {
        EnfantFamilleSearchModel enfantFamilleSearchModel = new EnfantFamilleSearchModel();
        enfantFamilleSearchModel.setForIdSituationFamiliale(getDecisionOO().getDemande().getSituationFamiliale()
                .getId());
        enfantFamilleSearchModel = PerseusImplServiceLocator.getEnfantFamilleService().search(enfantFamilleSearchModel);

        Boolean enfantMoins16ans = false;
        for (JadeAbstractModel model : enfantFamilleSearchModel.getSearchResults()) {
            EnfantFamille enfantFamille = (EnfantFamille) model;
            String dateNaissance = enfantFamille.getEnfant().getMembreFamille().getPersonneEtendue().getPersonne()
                    .getDateNaissance();
            int ageEnfant = JadeDateUtil.getNbYearsBetween(dateNaissance, getDecisionOO().getDemande()
                    .getSimpleDemande().getDateDebut());
            if (ageEnfant < IPFConstantes.AGE_16ANS) {
                enfantMoins16ans = true;
                break;
            } else {
                enfantMoins16ans = false;
            }
        }
        return enfantMoins16ans;
    }

    // TODO Test de methode pour récupérer le retro de l'ancienne demande
    protected boolean hasTableau() throws Exception {

        montantRetro = new Float(0);
        if (JadeDateUtil.isDateMonthYearBefore(decisionOO.getDemande().getSimpleDemande().getDateDebut().substring(3),
                PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt())) {
            if (CSEtatDecision.VALIDE.getCodeSystem().equals(getDecisionOO().getSimpleDecision().getCsEtat())) {
                if (!JadeStringUtil.isEmpty(getDecisionOO().getPrestation().getSimplePrestation().getIdPrestation())) {
                    montantRetro = Float.parseFloat(getDecisionOO().getPrestation().getSimplePrestation()
                            .getMontantTotal());
                }
            } else {
                montantRetro = PerseusServiceLocator.getDemandeService().calculerRetro(getDecisionOO().getDemande());
            }
        }

        if (montantRetro != 0) {
            getDecompte();
            return true;
        } else {
            return false;
        }

    }

    public void init(String idDecision) throws Exception {
        // Chargement du catalogue de texte
        getBabelContainer().RegisterCtx(IPFCatalogueTextes.CS_DECISION_COMMUNE);

        // Chargement entité decision
        setDecisionOO(PerseusServiceLocator.getDecisionOOService().read(idDecision));

        // Chargement des variables
        gestionnaire = getDecisionOO().getSimpleDecision().getUtilisateurPreparation();
        this.idDecision = getDecisionOO().getSimpleDecision().getIdDecision();
        idDemande = getDecisionOO().getDemande().getId();
        idTiers = getDecisionOO().getDemande().getDossier().getDemandePrestation().getDemandePrestation().getIdTiers();
        numeroDecision = getDecisionOO().getSimpleDecision().getNumeroDecision();

    }

    // Methode permettant de savoir si le requerant si le requerant est depuis plus de 3 ans dans le canton ET si il a
    // des enfants de moins de 16ans
    protected Boolean isCalculable() {
        if (true == getDecisionOO().getDemande().getSimpleDemande().getCalculable()) {
            return true;
        } else {
            return false;
        }
    }

    // Methode permettant de savoir si le requerant est dans le canton depuis plus de 3ans
    protected Boolean isEtabli() throws Exception {

        if (JadeDateUtil.getNbYearsBetween(getDecisionOO().getDemande().getSimpleDemande().getDateArrivee(),
                getDecisionOO().getDemande().getSimpleDemande().getDateDebut()) >= IPFConstantes.MINIMUM_ANNEES_CANTON) {
            return true;
        } else {
            return false;
        }
    }

    public void loadEntityDecision(String CatTexte, String decisionId) throws Exception {

        init(decisionId);

        if (!getDecisionOO().getDemande().getDossier().getDemandePrestation().getDemandePrestation().getIdTiers()
                .equals(getDecisionOO().getSimpleDecision().getIdTiersAdresseCourrier())) {
            PersonneEtendueComplexModel personne = TIBusinessServiceLocator.getPersonneEtendueService().read(
                    getDecisionOO().getSimpleDecision().getIdTiersAdresseCourrier());

            loadEntity(CatTexte, personne.getTiers().getLangue());

            titreTiers = getSession().getCodeLibelle(personne.getTiers().getTitreTiers());
            idAdresseCourrier = personne.getTiers().getIdTiers();
        } else {
            loadEntity(CatTexte, getDecisionOO().getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                    .getTiers().getLangue());

            titreTiers = getSession().getCodeLibelle(
                    getDecisionOO().getDemande().getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                            .getTitreTiers());
            idAdresseCourrier = getDecisionOO().getDemande().getDossier().getDemandePrestation().getPersonneEtendue()
                    .getTiers().getIdTiers();
        }
    }

    private int nombreChoixAideCategorielle() {
        nbChoixAideCat = 0;
        if (getDecisionOO().getSimpleDecision().getAideAuLogement()) {
            nbChoixAideCat++;
        }
        if (getDecisionOO().getSimpleDecision().getPensionAlimentaire()) {
            nbChoixAideCat++;

        }
        if (getDecisionOO().getSimpleDecision().getAideAuxEtudes()) {
            nbChoixAideCat++;

        }
        return nbChoixAideCat;
    }

    public void setDecisionOO(DecisionOO decisionOO) {
        this.decisionOO = decisionOO;
    }

    public void setMontantCreancier(FWCurrency montantCreancier) {
        this.montantCreancier = montantCreancier;
    }
}
