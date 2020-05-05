package ch.globaz.al.businessimpl.services.decision;

import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.constantes.ALConstCalcul;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.constantes.ALConstNumeric;
import ch.globaz.al.business.exceptions.decision.ALDecisionException;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.decision.DecisionAgricoleInterCantonalService;
import ch.globaz.al.business.services.models.droit.DroitBusinessService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

public class DecisionAgricoleInterCantonalServiceImpl extends DecisionAgricoleServiceImpl implements
        DecisionAgricoleInterCantonalService {

    @Override
    protected void loadListDroit(DocumentData documentData, DossierComplexModel dossier,
                                 List<CalculBusinessModel> calcul, String date, String langueDocument) throws JadePersistenceException,
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

        documentData.addData("isAgricoleInterCantonal", "OUI");

        Collection tableau_entete = new Collection("tableau_entete_definition");
        DataList list = new DataList("entete");
        list.addData("entete_enfant", this.getText("al.decision.liste.droit.enfant", langueDocument));
        list.addData("entete_naissance", this.getText("al.decision.liste.droit.naissance", langueDocument));
        list.addData("entete_echeance", this.getText("al.decision.liste.droit.echeance", langueDocument));
        list.addData("entete_motif", this.getText("al.decision.liste.droit.motif", langueDocument));
        list.addData("entete_tarif", this.getText("al.decision.liste.droit.tarif", langueDocument));
        list.addData("entete_montant_alloc", this.getText("al.decision.liste.droit.adc.alloc", langueDocument));
        list.addData("entete_montant_conjoint", this.getText("al.decision.liste.droit.adc.conjoint", langueDocument));
        list.addData("entete_montant", this.getText("al.decision.liste.droit.mois", langueDocument));
        tableau_entete.add(list);
        documentData.add(tableau_entete);

        // liste des tiers bénéficiaires des droits
        ArrayList<String> listTiersBeneficiaireDroit = new ArrayList<>();

        Map total = ALServiceLocator.getCalculBusinessService().getTotal(dossier, calcul,
                dossier.getDossierModel().getUniteCalcul(), "1", false, date);
        calcul = (List) total.get(ALConstCalcul.DROITS_CALCULES);

        DroitBusinessService dbs = ALServiceLocator.getDroitBusinessService();

        // Partie affichage de chaque droit
        Collection tableau_colonne = new Collection("tableau_colonne_definition");

        String idDroit = null;

        Map<String, String> droitDecision = new HashMap<>();

        for (int i = 0; i < calcul.size(); i++) {

            loadChampsDroitCalcule(dbs, list, tableau_colonne, langueDocument, calcul, dossier,
                    listTiersBeneficiaireDroit, i, date);

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

        addLigneTarifAgricole(documentData, dossier, calcul, langueDocument);
    }

    private void loadChampsDroitCalcule(DroitBusinessService dbs, DataList list, Collection tableau_colonne,
            String langueDocument, List<CalculBusinessModel> calcul, DossierComplexModel dossier,
            List<String> listTiersBeneficiaireDroit, int i, String date) throws JadeApplicationException,
            JadePersistenceException {
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

            if (ALCSTarif.CATEGORIE_LFM.equals((calcul.get(i)).getTarif())) {

                list.addData("colonne_tarif", this.getText("al.decision.agricole.tarif.montagne", langueDocument));
            } else if (ALCSTarif.CATEGORIE_LFP.equals((calcul.get(i)).getTarif())) {
                list.addData("colonne_tarif", this.getText("al.decision.agricole.tarif.plaine", langueDocument));

            }

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

    @Override
    protected void loadListNaissance(DocumentData documentData, DossierComplexModel dossier,
            List<CalculBusinessModel> calcul, String date, String langueDocument) throws JadeApplicationException,
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
                Map total = ALServiceLocator.getCalculBusinessService().getTotal(dossier, calcul,
                        dossier.getDossierModel().getUniteCalcul(), "1", false, date);

                calcul = (List<CalculBusinessModel>) total.get(ALConstCalcul.DROITS_CALCULES);

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
}