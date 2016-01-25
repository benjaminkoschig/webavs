package ch.globaz.al.businessimpl.services.decision;

import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.HashMap;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.constantes.ALConstCalcul;
import ch.globaz.al.business.constantes.ALConstDecisions;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.constantes.ALConstNumeric;
import ch.globaz.al.business.exceptions.decision.ALDecisionException;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.decision.DecisionInterCantonalService;
import ch.globaz.al.business.services.models.droit.DroitBusinessService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Classe fille de DecisionAbstractServiceImpl qui contient toutes les spécificités des décisions intercantonale
 * 
 * @author JER/PTA
 */
public class DecisionInterCantonalServiceImpl extends DecisionAbstractServiceImpl implements
        DecisionInterCantonalService {

    /**
     * Méthode qui retourne les différents calculs pour un même droit dont le montant effectif est supérieur à 0
     * 
     * @param listDroitCalculDossier
     *            liste des calculs pour un dossier
     * @param idDroit
     *            identifiant du droit pour lequel on recherche des droits calculés on recherche les calculs
     * @return
     */
    private ArrayList<CalculBusinessModel> listCalculDroitIdentique(
            ArrayList<CalculBusinessModel> listDroitCalculDossier, String idDroit) throws JadeApplicationException,
            JadePersistenceException {
        ArrayList<CalculBusinessModel> listCalculDroitIdentique = new ArrayList<CalculBusinessModel>();
        for (int i = 0; i < listDroitCalculDossier.size(); i++) {
            if (JadeStringUtil.equals(listDroitCalculDossier.get(i).getDroit().getDroitModel().getIdDroit(), idDroit,
                    false)
                    /** && !JadeStringUtil.isBlankOrZero(listDroitCalculDossier.get(i).getCalculResultMontantEffectif()) **/
                    && !JadeStringUtil.equals(ALCSDroit.TYPE_NAIS, listDroitCalculDossier.get(i).getType(), false)
                    && !JadeStringUtil.equals(ALCSDroit.TYPE_ACCE, listDroitCalculDossier.get(i).getType(), false)) {
                listCalculDroitIdentique.add(listDroitCalculDossier.get(i));
            }
        }

        return listCalculDroitIdentique;
    }

    private void loadChampsCantonaleSupplNonHorlogere(DroitBusinessService dbs, DataList list,
            Collection tableau_colonne, String langueDocument, ArrayList<CalculBusinessModel> calcul,
            DossierComplexModel dossier, ArrayList<String> listTiersBeneficiaireDroit, int i, String date)
            throws JadeApplicationException, JadePersistenceException {
        // si le droit est actif et le type de droit autre que naissance et
        // accueil
        if (((dbs.isDroitActif(calcul.get(i).getDroit().getDroitModel(), date) && !dbs.isDroitInactif(calcul.get(i)
                .getDroit().getDroitModel())) || (calcul.get(i)).getDroit().getDroitModel().getForce())
                && (!ALCSDroit.TYPE_NAIS.equals((calcul.get(i)).getType()) && !ALCSDroit.TYPE_ACCE.equals((calcul
                        .get(i)).getType()))) {

            list = new DataList("colonne");

            listTiersBeneficiaireDroit.add(calcul.get(i).getDroit().getDroitModel().getIdTiersBeneficiaire());

            // si le droit est de type ménage
            if (JadeStringUtil.equals((calcul.get(i)).getDroit().getDroitModel().getTypeDroit(), ALCSDroit.TYPE_MEN,
                    false)) {
                list.addData("colonne_enfant", this.getText("al.decision.liste.droit.alloc.typeMenage", langueDocument));

            } else {
                list.addData("colonne_enfant", (calcul.get(i)).getDroit().getEnfantComplexModel()
                        .getPersonneEtendueComplexModel().getTiers().getDesignation1()
                        + " "
                        + (calcul.get(i)).getDroit().getEnfantComplexModel().getPersonneEtendueComplexModel()
                                .getTiers().getDesignation2()
                        + "\n"
                        + (calcul.get(i)).getDroit().getEnfantComplexModel().getPersonneEtendueComplexModel()
                                .getPersonneEtendue().getNumAvsActuel());
            }
            list.addData("colonne_naissance", (calcul.get(i)).getDroit().getEnfantComplexModel()
                    .getPersonneEtendueComplexModel().getPersonne().getDateNaissance());
            list.addData("colonne_echeance", (calcul.get(i)).getDroit().getDroitModel().getFinDroitForcee());
            // récupère le libellé du motif de fin de droit

            if (!JadeStringUtil.equals((calcul.get(i)).getDroit().getDroitModel().getTypeDroit(), ALCSDroit.TYPE_MEN,
                    false)) {
                list.addData(
                        "colonne_motif",

                        ALImplServiceLocator.getCalculMontantsService().isMontantForceZero(calcul.get(i)) ? ""
                                : ALServiceLocator.getDroitEcheanceService().getLibelleMotif(
                                        (calcul.get(i)).getDroit(), langueDocument));
            }

            if (ALImplServiceLocator.getCalculMontantsService().isMontantForce(calcul.get(i))) {
                list.addData("colonne_montant_alloc", "");
                list.addData("colonne_montant_conjoint", "");
            } else {
                list.addData("colonne_montant_alloc",
                        JANumberFormatter.fmt((calcul.get(i)).getMontantAllocataire(), true, true, false, 2));
                list.addData("colonne_montant_conjoint",
                        JANumberFormatter.fmt((calcul.get(i)).getMontantAutreParent(), true, true, false, 2));
            }

            list.addData("colonne_code_monnaie", this.getText("al.decision.liste.droit.chf", langueDocument));
            list.addData("colonne_montant",
                    JANumberFormatter.fmt((calcul.get(i)).getCalculResultMontantEffectif(), true, true, false, 2));
            tableau_colonne.add(list);
            // cas d'un bénéficiaire du droit autre que celui du dossier

            DataList DroitBenef = new DataList("droitBeneficiaire");

            if (!JadeStringUtil.isBlankOrZero((calcul.get(i)).getDroit().getDroitModel().getIdTiersBeneficiaire())
                    && !JadeStringUtil.equals(dossier.getDossierModel().getIdTiersBeneficiaire(), (calcul.get(i))
                            .getDroit().getDroitModel().getIdTiersBeneficiaire(), false)) {

                loadDroitTiersBeneficiaire((calcul.get(i)).getDroit(), DroitBenef, langueDocument);
                tableau_colonne.add(DroitBenef);

            } else {
                DataList ligneVide = new DataList("ligneVide");
                tableau_colonne.add(ligneVide);
            }
        }

    }

    private void loadChampsSpecificiteHorlogere(DroitBusinessService dbs, DroitComplexModel droit, String montantDroit,
            String montantSupplHorloger, DataList list, String typeTarif, String typePrestation,
            DossierComplexModel dossier, String langueDocument, Collection tableau_colonne, String montantallocataire,
            String montantAutreParent, String date, Boolean isHide) throws JadeApplicationException,
            JadePersistenceException {

        if (((dbs.isDroitActif(droit.getDroitModel(), date) && !dbs.isDroitInactif(droit.getDroitModel())) || droit
                .getDroitModel().getForce())
                && (!ALCSDroit.TYPE_NAIS.equals(droit.getDroitModel().getTypeDroit()) && !ALCSDroit.TYPE_ACCE
                        .equals(droit.getDroitModel().getTypeDroit()))) {

            list = new DataList("colonne");
            // contrôle sur les droits actifs

            // si le droit est de type ménage
            if (JadeStringUtil.equals(droit.getDroitModel().getTypeDroit(), ALCSDroit.TYPE_MEN, false)) {
                list.addData("colonne_enfant", this.getText("al.decision.liste.droit.alloc.typeMenage", langueDocument));

            } else {
                list.addData("colonne_enfant", (droit.getEnfantComplexModel().getPersonneEtendueComplexModel()
                        .getTiers().getDesignation1()
                        + " "
                        + droit.getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2()
                        + "\n" + droit.getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonneEtendue()
                        .getNumAvsActuel()));
            }
            list.addData("colonne_naissance", (droit.getEnfantComplexModel().getPersonneEtendueComplexModel()
                    .getPersonne().getDateNaissance()));
            list.addData("colonne_echeance", (droit.getDroitModel().getFinDroitForcee()));
            // récupère le libellé du motif de fin de droit

            if (!JadeStringUtil.equals(droit.getDroitModel().getTypeDroit(), ALCSDroit.TYPE_MEN, false)) {

                // si tarif supp horloger et pas cachée ajouter sup horloger
                if (!isHide && JadeStringUtil.equals(typeTarif, ALCSTarif.CATEGORIE_SUP_HORLO, false)) {
                    list.addData(
                            "colonne_motif",
                            "\n"
                                    + (JadeStringUtil.equals(ALCSDroit.TYPE_ENF, droit.getDroitModel().getTypeDroit(),
                                            false) ? this.getText("al.decision.liste.droit.supplHorlogerEnfant",
                                            langueDocument) : this.getText(
                                            "al.decision.liste.droit.supplHorlogerFormation", langueDocument)));
                }

                else if (droit.getDroitModel().getForce()
                        && JadeNumericUtil.isEmptyOrZero(droit.getDroitModel().getMontantForce())) {
                    list.addData("colonne_motif",

                    "");

                } else {
                    list.addData(
                            "colonne_motif",
                            (JadeStringUtil.isBlankOrZero(montantSupplHorloger)) ? ALServiceLocator
                                    .getDroitEcheanceService().getLibelleMotif(droit, langueDocument)
                                    : ALServiceLocator.getDroitEcheanceService().getLibelleMotif(droit, langueDocument)
                                            + "\n"

                                            + (JadeStringUtil.equals(ALCSDroit.TYPE_ENF, droit.getDroitModel()
                                                    .getTypeDroit(), false) ? this.getText(
                                                    "al.decision.liste.droit.supplHorlogerEnfant", langueDocument)
                                                    : this.getText("al.decision.liste.droit.supplHorlogerFormation",
                                                            langueDocument)));

                }
            }

            if (droit.getDroitModel().getForce()) {
                list.addData("colonne_montant_alloc", "");
                list.addData("colonne_montant_conjoint", "");
            } else {
                list.addData("colonne_montant_alloc", JANumberFormatter.fmt(montantallocataire, true, true, false, 2));
                list.addData("colonne_montant_conjoint",
                        JANumberFormatter.fmt(montantAutreParent, true, true, false, 2));
            }

            list.addData(
                    "colonne_code_monnaie",

                    (JadeStringUtil.isBlankOrZero(montantSupplHorloger) || JadeStringUtil.isEmpty(montantSupplHorloger))
                            && (!isHide) ? this.getText("al.decision.liste.droit.chf", langueDocument) : this.getText(
                            "al.decision.liste.droit.chf", langueDocument)
                            + "\n"
                            + this.getText("al.decision.liste.droit.chf", langueDocument));

            if (!isHide && JadeStringUtil.equals(typeTarif, ALCSTarif.CATEGORIE_SUP_HORLO, false)) {
                list.addData(

                "colonne_montant", "0.00" + "\n" + JANumberFormatter.fmt(montantSupplHorloger, true, true, false, 2));

            } else {
                list.addData(
                        "colonne_montant",
                        (JadeStringUtil.isBlankOrZero(montantSupplHorloger) || JadeStringUtil
                                .isEmpty(montantSupplHorloger)) && (isHide) ? JANumberFormatter.fmt(montantDroit, true,
                                true, false, 2) : JANumberFormatter.fmt(montantDroit, true, true, false, 2)
                                + (JadeStringUtil.isEmpty(montantSupplHorloger) ? "" : "\n"
                                        + JANumberFormatter.fmt(montantSupplHorloger, true, true, false, 2)));
            }

            tableau_colonne.add(list);
            // cas d'un bénéficiaire du droit autre que celui du dossier

            DataList DroitBenef = new DataList("droitBeneficiaire");

            if (!JadeStringUtil.isBlankOrZero(droit.getDroitModel().getIdTiersBeneficiaire())
                    && !JadeStringUtil.equals(dossier.getDossierModel().getIdTiersBeneficiaire(), droit.getDroitModel()
                            .getIdTiersBeneficiaire(), false)) {

                loadDroitTiersBeneficiaire(droit, DroitBenef, langueDocument);
                tableau_colonne.add(DroitBenef);

            } else {
                DataList ligneVide = new DataList("ligneVide");
                tableau_colonne.add(ligneVide);
            }
        }
    }

    private void loadDroitsCantonaleSupplHorlogere(DroitBusinessService dbs, DataList list, Collection tableau_colonne,
            String langueDocument, ArrayList<CalculBusinessModel> calcul, DossierComplexModel dossier,
            ArrayList<String> listTiersBeneficiaireDroit,/* int i, */String idDroit, String date)
            throws JadeApplicationException, JadePersistenceException {

        if (calcul.size() > 0) {
            String montantSuppHorloger = null;
            // si c'est le premier et le dernier
            if ((calcul.size() == 1)) {
                loadChampsSpecificiteHorlogere(dbs, calcul.get(0).getDroit(), calcul.get(0)
                        .getCalculResultMontantEffectif(), montantSuppHorloger, list, calcul.get(0).getTarif(), calcul
                        .get(0).getType(), dossier, langueDocument, tableau_colonne, calcul.get(0)
                        .getMontantAllocataire(), calcul.get(0).getMontantAutreParent(), date, calcul.get(0)
                        .isHideDroit());
            }

            // si c'est les droit sont au nobre de deux, une fois formation ou enfant et une fois sup. horloger

            else if (calcul.size() == 2) {

                // String montantSuppHorloger = null;
                CalculBusinessModel calculEnfForm = new CalculBusinessModel();
                Boolean isHideDroit = true;
                String typeTarif = null;

                for (int i = 0; i < calcul.size(); i++) {

                    if (JadeStringUtil.equals(ALCSTarif.CATEGORIE_SUP_HORLO, calcul.get(i).getTarif(), false)) {
                        montantSuppHorloger = calcul.get(i).getCalculResultMontantEffectif();
                        isHideDroit = calcul.get(i).isHideDroit();
                        typeTarif = calcul.get(i).getTarif();
                    }

                    if ((JadeStringUtil.equals(ALCSDroit.TYPE_ENF, calcul.get(i).getType(), false) || JadeStringUtil
                            .equals(ALCSDroit.TYPE_FORM, calcul.get(i).getType(), false))
                            && !JadeStringUtil.equals(ALCSTarif.CATEGORIE_SUP_HORLO, calcul.get(i).getTarif(), false)) {

                        calculEnfForm = calcul.get(i);

                    }

                }

                loadChampsSpecificiteHorlogere(dbs, calculEnfForm.getDroit(),
                        calculEnfForm.getCalculResultMontantEffectif(), montantSuppHorloger, list, typeTarif,
                        calculEnfForm.getType(), dossier, langueDocument, tableau_colonne, calcul.get(0)
                                .getMontantAllocataire(), calcul.get(0).getMontantAutreParent(), date, isHideDroit);

            } else if (calcul.size() > 2) {
                throw new ALDecisionException(
                        "DecisionSalarieServiceImpl#loadChampsSpecificiteHorlogere: more than 2 calcul for this droit: ");
            }
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void loadListDroit(DocumentData documentData, DossierComplexModel dossier,
            ArrayList<CalculBusinessModel> calcul, String date, String langueDocument) throws JadePersistenceException,
            JadeApplicationException {

        // contrôle des paramètres
        if (documentData == null) {
            throw new ALDecisionException("DecisionInterCantonalServiceImpl#loadListDroit: documentData is null");
        }

        if (dossier == null) {
            throw new ALDecisionException("DecisionInterCantonalServiceImpl#loadListDroit: dossier is null");
        }
        if (calcul == null) {
            throw new ALDecisionException("DecisionInterCantonalServiceImpl#loadListDroit: calcul is null");
        }
        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALDecisionException("DecisionInterCantonalServiceImpl#loadListDroit: " + date
                    + " is not a valid globaz' date (dd.mm.yyyy)");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDecisionException("DecisionInterCantonalServiceImpl#loadListDroit: language  " + langueDocument
                    + " is not  valid ");
        }

        documentData.addData("isInterCantonal", "OUI");

        Collection tableau_entete = new Collection("tableau_entete_definition");
        DataList list = new DataList("entete");
        list.addData("entete_enfant", this.getText("al.decision.liste.droit.enfant", langueDocument));
        list.addData("entete_naissance", this.getText("al.decision.liste.droit.naissance", langueDocument));
        list.addData("entete_echeance", this.getText("al.decision.liste.droit.echeance", langueDocument));
        list.addData("entete_motif", this.getText("al.decision.liste.droit.motif", langueDocument));
        list.addData("entete_montant_alloc", this.getText("al.decision.liste.droit.adc.alloc", langueDocument));
        list.addData("entete_montant_conjoint", this.getText("al.decision.liste.droit.adc.conjoint", langueDocument));
        list.addData("entete_montant", this.getText("al.decision.liste.droit.mois", langueDocument));
        tableau_entete.add(list);
        documentData.add(tableau_entete);

        // liste des tiers bénéficiaires des droits
        ArrayList<String> listTiersBeneficiaireDroit = new ArrayList<String>();

        HashMap total = ALServiceLocator.getCalculBusinessService().getTotal(dossier.getDossierModel(), calcul,
                dossier.getDossierModel().getUniteCalcul(), "1", false, date);
        calcul = (ArrayList) total.get(ALConstCalcul.DROITS_CALCULES);

        DroitBusinessService dbs = ALServiceLocator.getDroitBusinessService();

        // Partie affichage de chaque droit
        Collection tableau_colonne = new Collection("tableau_colonne_definition");

        String idDroit = null;

        HashMap<String, String> droitDecision = new HashMap<String, String>();

        for (int i = 0; i < calcul.size(); i++) {

            // caisse horlogère et autre caisse

            if ((ALImplServiceLocator.getHorlogerBusinessService().isCaisseHorlogere() == true)
                    && (JadeDateUtil.areDatesEquals(dossier.getDossierModel().getDebutValidite(),
                            ALConstDecisions.DEB_SUP_HORL)
                            || JadeDateUtil.isDateAfter(dossier.getDossierModel().getDebutValidite(),
                                    ALConstDecisions.DEB_SUP_HORL)
                            || JadeDateUtil.areDatesEquals(dossier.getDossierModel().getFinValidite(),
                                    ALConstDecisions.DEB_SUP_HORL) || JadeDateUtil.isDateAfter(dossier
                            .getDossierModel().getFinValidite(), ALConstDecisions.DEB_SUP_HORL))) {
                if (!JadeStringUtil.equals(calcul.get(i).getDroit().getDroitModel().getTypeDroit(), ALCSDroit.TYPE_MEN,
                        false) || !JadeStringUtil.isBlankOrZero(calcul.get(i).getCalculResultMontantEffectif())) {

                    if (!ALCSDroit.TYPE_NAIS.equals((calcul.get(i)).getType())
                            && !ALCSDroit.TYPE_ACCE.equals((calcul.get(i)).getType())) {
                        // récupère id Droit
                        idDroit = (calcul.get(i)).getDroit().getDroitModel().getIdDroit();

                        // récupère la lsite des calcul pour le même droit
                        ArrayList<CalculBusinessModel> listCalculUnDroitDossier = listCalculDroitIdentique(calcul,
                                idDroit);

                        if (!droitDecision.containsValue(idDroit)) {

                            loadDroitsCantonaleSupplHorlogere(dbs, list, tableau_colonne, langueDocument,
                                    listCalculUnDroitDossier, dossier, listTiersBeneficiaireDroit, /* i, */idDroit,
                                    date);
                        }
                        // ajoute le droit dans la hashmap
                        droitDecision.put(idDroit, idDroit);

                    }
                }

            }

            else {

                loadChampsCantonaleSupplNonHorlogere(dbs, list, tableau_colonne, langueDocument, calcul, dossier,
                        listTiersBeneficiaireDroit, i, date);

            }
        }

        // charge, le cas échéant l'adresse de paiement au niveau du dossier
        if (!JadeStringUtil.isBlankOrZero(dossier.getDossierModel().getIdTiersBeneficiaire())) {
            // savoir si adresse de paiement est nécessaire
            boolean notAdressePaiementDossier = isNotAllDroitBenEgalBenDos(listTiersBeneficiaireDroit, dossier
                    .getDossierModel().getIdTiersBeneficiaire());
            // si adresse de paiement est nécessaire
            if (!notAdressePaiementDossier) {
                loadInfoVersementDirect(documentData, dossier, langueDocument);
            }

        }

        documentData.add(tableau_colonne);

        // Partie affichage du total
        Collection tableau_total = new Collection("tableau_total");
        // si taux de versement égal à 100%
        if (Double.valueOf(dossier.getDossierModel().getTauxVersement()).compareTo(ALConstNumeric.CENT_POURCENT) == 0) {
            list = new DataList("total");
            list.addData("tableau_total_0", this.getText("al.decision.liste.droit.total.alloc", langueDocument));
            list.addData("tableau_total_1", this.getText("al.decision.liste.droit.chf", langueDocument));
            list.addData("tableau_total_2",
                    JANumberFormatter.fmt((String) total.get(ALConstCalcul.TOTAL_EFFECTIF), true, true, false, 2));

            tableau_total.add(list);
        } else if (Double.valueOf(dossier.getDossierModel().getTauxVersement()).compareTo(ALConstNumeric.ZERO_VALEUR) != 0) {
            // total des allocations pour un 100%
            list = new DataList("partiel");
            list.addData("tableau_partiel_0", this.getText("al.decision.liste.droit.total.alloc", langueDocument));
            list.addData("tableau_partiel_1", this.getText("al.decision.liste.droit.chf", langueDocument));
            list.addData("tableau_partiel_2",
                    JANumberFormatter.fmt((String) total.get(ALConstCalcul.TOTAL_BASE), true, true, false, 2));
            // tableau_partiel.add(list);
            tableau_total.add(list);

            // total à payer par rapport au taux d'occupation
            list = new DataList("total");
            list.addData(
                    "tableau_total_0",
                    this.getText("al.decision.liste.droit.alloc.taux.dossier", langueDocument) + " "
                            + dossier.getDossierModel().getTauxVersement() + " "
                            + this.getText("al.decision.liste.droit.alloc.taux.dossier.pourcent", langueDocument) + " "
                            + this.getText("al.decision.liste.droit.chf", langueDocument) + " "

                            + JANumberFormatter.fmt((String) total.get(ALConstCalcul.TOTAL_BASE), true, true, false, 2));
            list.addData("tableau_total_1", this.getText("al.decision.liste.droit.chf", langueDocument));
            list.addData("tableau_total_2",
                    JANumberFormatter.fmt((String) total.get(ALConstCalcul.TOTAL_EFFECTIF), true, true, false, 2));
            tableau_total.add(list);
        } else {
            list = new DataList("total");
            list.addData("tableau_total_0", this.getText("al.decision.liste.droit.total.alloc", langueDocument));
            list.addData("tableau_total_1", this.getText("al.decision.liste.droit.chf", langueDocument));
            list.addData("tableau_total_2", "0.00");
            tableau_total.add(list);
        }

        documentData.add(tableau_total);

        Collection tableau_sous_total = new Collection("tableau_sous_total");

        // création des tableaux "jours de début/fin"
        tableau_sous_total = loadMontantJoursDebut(dossier, calcul, total, date, list, tableau_sous_total,
                langueDocument);
        tableau_sous_total = loadMontantJoursFin(dossier, calcul, total, date, list, tableau_sous_total, langueDocument);
        documentData.add(tableau_sous_total);

        // Rempli la partie jour
        Collection tableau_jour = new Collection("tableau_jour");
        list = new DataList("jour");
        list.addData("tableau_jour_0", this.getText("al.decision.liste.droit.total.jour", langueDocument));
        list.addData("tableau_jour_1", this.getText("al.decision.liste.droit.chf", langueDocument));
        list.addData("tableau_jour_2",
                JANumberFormatter.fmt((String) total.get(ALConstCalcul.TOTAL_UNITE_EFFECTIF), true, true, false, 2));
        // ajoute la liste à la collection
        tableau_jour.add(list);
        // ajoute la collection au document
        documentData.add(tableau_jour);

    }

    @Override
    protected void loadListNaissance(DocumentData documentData, DossierComplexModel dossier,
            ArrayList<CalculBusinessModel> calcul, String date, String langueDocument) throws JadeApplicationException,
            JadePersistenceException {

        Collection tableau_naissance = new Collection("tableau_naissance");

        for (int i = 0; i < calcul.size(); i++) {
            if (ALCSDroit.TYPE_NAIS.equals((calcul.get(i)).getType())
                    || ALCSDroit.TYPE_ACCE.equals((calcul.get(i)).getType())) {
                DataList list = new DataList("naissance");
                String libelle = "";
                // le libellé varie en fonction de naissance / accueil
                if (ALCSDroit.TYPE_NAIS.equals((calcul.get(i)).getType())) {
                    libelle = this.getText("al.decision.liste.droit.naissance.libelle", langueDocument);
                } else if (ALCSDroit.TYPE_ACCE.equals((calcul.get(i)).getType())) {
                    libelle = this.getText("al.decision.liste.droit.accueil.libelle", langueDocument);
                }
                HashMap total = ALServiceLocator.getCalculBusinessService().getTotal(dossier.getDossierModel(), calcul,
                        dossier.getDossierModel().getUniteCalcul(), "1", false, date);

                calcul = (ArrayList<CalculBusinessModel>) total.get(ALConstCalcul.DROITS_CALCULES);

                list.addData("tableau_naissance_0", libelle
                        + " "
                        + (calcul.get(i)).getDroit().getEnfantComplexModel().getPersonneEtendueComplexModel()
                                .getTiers().getDesignation1()
                        + " "
                        + (calcul.get(i)).getDroit().getEnfantComplexModel().getPersonneEtendueComplexModel()
                                .getTiers().getDesignation2()
                        + "\n"
                        + (calcul.get(i)).getDroit().getEnfantComplexModel().getPersonneEtendueComplexModel()
                                .getPersonneEtendue().getNumAvsActuel());

                // ajout tarif caisse
                list.addData("tableau_naissance_1",
                        JANumberFormatter.fmt((calcul.get(i)).getMontantAllocataire(), true, true, false, 2));

                // ajout tarif caisse autre parent
                list.addData("tableau_naissance_2",
                        JANumberFormatter.fmt((calcul.get(i)).getMontantAutreParent(), true, true, false, 2));

                list.addData("tableau_naissance_3", this.getText("al.decision.liste.droit.chf", langueDocument));

                // total allocation naissance caisse pour son allocataire
                list.addData("tableau_naissance_4",
                        JANumberFormatter.fmt((calcul.get(i)).getCalculResultMontantEffectif(), true, true, false, 2));

                tableau_naissance.add(list);

            }
        }
        documentData.add(tableau_naissance);
    }

    @Override
    protected void loadTextesDecision(DocumentData documentData, DossierComplexModel dossierComplexModel,
            String commentaire, String langueDocument) throws JadeApplicationServiceNotAvailableException,
            JadeApplicationException, JadePersistenceException {

        // INFOROMD0028 - AF - Modifications montants AF VD (CBU)
        if (JadeStringUtil.equals(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS, dossierComplexModel.getDossierModel()
                .getTarifForce(), false)) {
            documentData.addData("texte_paragraphe_droitAcquis",
                    this.getText("al.decision.standard.paragraphe.droitAcquis", langueDocument));
        }
        // FIN INFOROMD0028

        if (ALCSDossier.ETAT_RADIE.equals(dossierComplexModel.getDossierModel().getEtatDossier())) {

            if (!JadeStringUtil.isEmpty(commentaire)) {
                documentData.addData("texte_paragraphe_libre", JadeStringUtil.removeChar(commentaire, '\n'));
            }

        } else if (ALServiceLocator.getDossierBusinessService().isAgricole(
                dossierComplexModel.getDossierModel().getActiviteAllocataire())
                || ALCSDossier.ACTIVITE_INDEPENDANT.equals(dossierComplexModel.getDossierModel()
                        .getActiviteAllocataire())) {

            if (!JadeStringUtil.isEmpty(commentaire)) {
                documentData.addData("texte_paragraphe_libre", JadeStringUtil.removeChar(commentaire, '\n'));
            }
            documentData
                    .addData("texte_paragraphe_2", this.getText("al.decision.standard.paragraphe2", langueDocument));
            documentData
                    .addData("texte_paragraphe_3", this.getText("al.decision.standard.paragraphe3", langueDocument));
        } else {
            super.loadTextesDecision(documentData, dossierComplexModel, commentaire, langueDocument);
        }
    }

}
