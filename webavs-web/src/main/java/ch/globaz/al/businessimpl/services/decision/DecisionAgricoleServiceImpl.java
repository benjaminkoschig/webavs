package ch.globaz.al.businessimpl.services.decision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.constantes.ALConstCalcul;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.constantes.ALConstNumeric;
import ch.globaz.al.business.exceptions.decision.ALDecisionException;
import ch.globaz.al.business.exceptions.document.ALDocumentException;
import ch.globaz.al.business.models.allocataire.AgricoleModel;
import ch.globaz.al.business.models.allocataire.AgricoleSearchModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.decision.DecisionAgricoleService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.properties.ALProperties;
import ch.globaz.common.domaine.Date;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;

/**
 * Classe fille de DecisionAbstractServiceImpl qui contient toutes les spécificités des décisions pour agriculteurs
 *
 * @author JER
 */
public class DecisionAgricoleServiceImpl extends DecisionAbstractServiceImpl implements DecisionAgricoleService {

    @Override
    protected void loadListDroit(DocumentData documentData, DossierComplexModel dossier,
                                 List<CalculBusinessModel> calcul, String date, String langueDocument)
            throws JadePersistenceException, JadeApplicationException {

        // vérification des paramètres
        if (documentData == null) {
            throw new ALDecisionException("DecisionAgricoleServieImpl#loadListDroit: documentData is null");
        }
        if (dossier == null) {
            throw new ALDecisionException("DecisionAgricoleServieImpl#loadListDroit: dossier is null");
        }
        if (calcul == null) {
            throw new ALDecisionException("DecisionAgricoleServieImpl#loadListDroit: calcul is null");
        }
        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALDecisionException(
                    "DecisionAgricoleServieImpl#loadListDroit: " + date + " is not a valid globaz's date (dd.mm.yyyy");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDecisionException(
                    "DecisionAgricoleServiceImpl#loadListDroit: language  " + langueDocument + " is not  valid ");
        }

        documentData.addData("isAgricole", "OUI");

        Collection tableau_entete = new Collection("tableau_entete_definition");
        DataList list = new DataList("entete");
        list.addData("entete_enfant", this.getText("al.decision.liste.droit.enfant", langueDocument));
        list.addData("entete_naissance", this.getText("al.decision.liste.droit.naissance", langueDocument));
        list.addData("entete_echeance", this.getText("al.decision.liste.droit.echeance", langueDocument));
        list.addData("entete_motif", this.getText("al.decision.liste.droit.motif", langueDocument));
        list.addData("entete_tarif", this.getText("al.decision.liste.droit.tarif", langueDocument));
        list.addData("entete_montant", this.getText("al.decision.liste.droit.mois", langueDocument));
        tableau_entete.add(list);
        documentData.add(tableau_entete);

        // liste des tiers bénéficiaires des droits
        List<String> listTiersBeneficiaireDroit = new ArrayList<>();

        // calcul du montant total de la décision
        Map total = ALServiceLocator.getCalculBusinessService().getTotal(dossier, calcul,
                ALCSDossier.UNITE_CALCUL_MOIS, "1", false, date);

        calcul = (List<CalculBusinessModel>) total.get(ALConstCalcul.DROITS_CALCULES);

        Collection tableau_colonne = new Collection("tableau_colonne_definition");

        for (int i = 0; i < calcul.size(); i++) {
            boolean hasMontantBase = !JadeNumericUtil.isEmptyOrZero((calcul.get(i)).getCalculResultMontantBase());
            boolean hasMontantForce = !JadeNumericUtil
                    .isEmptyOrZero((calcul.get(i)).getDroit().getDroitModel().getMontantForce());
            hasMontantForce = hasMontantForce && (calcul.get(i)).getDroit().getDroitModel().getForce();

            boolean isDroitActif = false;
            Date dateFinDroit = null;
            Date dateDebutFinDossier = null;

            if (!JadeStringUtil.isBlank((calcul.get(i)).getDroit().getDroitModel().getFinDroitForcee())) {
                dateFinDroit = new Date((calcul.get(i)).getDroit().getDroitModel().getFinDroitForcee());
            }

            if (!JadeStringUtil.isBlank(dossier.getDossierModel().getDebutValidite())) {
                dateDebutFinDossier = new Date(dossier.getDossierModel().getDebutValidite());
            }

            if (dateFinDroit != null && dateDebutFinDossier != null) {
                isDroitActif = dateFinDroit.afterOrEquals(dateDebutFinDossier);
            }

            // Cas de radiation, pas de date de debut du dossier, comparer les dates de fin
            if (dateFinDroit != null && dateDebutFinDossier == null
                    && !JadeStringUtil.isBlank(dossier.getDossierModel().getFinValidite())) {
                dateDebutFinDossier = new Date(dossier.getDossierModel().getFinValidite());
                isDroitActif = !JadeStringUtil.isBlankOrZero((calcul.get(i)).getCalculResultMontantBase());
                isDroitActif = isDroitActif && dateFinDroit.afterOrEquals(dateDebutFinDossier);
            }
            boolean afficherLigne = true;

            if (ALProperties.GENERER_DECISION_NEW_TEST_MONTANT_ZERO.getBooleanValue()) {
                afficherLigne = hasMontantBase;
            } else {
                afficherLigne = isDroitActif && (hasMontantBase || hasMontantForce)
                        && (!JadeNumericUtil.isEmptyOrZero((calcul.get(i)).getCalculResultMontantBase())
                                || (calcul.get(i)).getDroit().getDroitModel().getForce());
            }

            // si montant <>0 et type de droit autre que accueil et naissance
            if (afficherLigne && (!ALCSDroit.TYPE_NAIS.equals((calcul.get(i)).getType())
                    && !ALCSDroit.TYPE_ACCE.equals((calcul.get(i)).getType()))) {
                list = new DataList("colonne");

                listTiersBeneficiaireDroit.add((calcul.get(i)).getDroit().getDroitModel().getIdTiersBeneficiaire());

                // si le droit est de type ménage
                if (JadeStringUtil.equals((calcul.get(i)).getDroit().getDroitModel().getTypeDroit(), ALCSDroit.TYPE_MEN,
                        false)) {
                    list.addData("colonne_enfant",
                            this.getText("al.decision.liste.droit.alloc.typeMenage", langueDocument));

                } else {
                    list.addData("colonne_enfant",
                            (calcul.get(i)).getDroit().getEnfantComplexModel().getPersonneEtendueComplexModel()
                                    .getTiers().getDesignation1()
                                    + " "
                                    + (calcul.get(i)).getDroit().getEnfantComplexModel()
                                            .getPersonneEtendueComplexModel().getTiers().getDesignation2()
                                    + "\n" + (calcul.get(i)).getDroit().getEnfantComplexModel()
                                            .getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel());

                }
                // récupère la date de fin de droit (pas si droit ménage)
                if (!JadeStringUtil.equals((calcul.get(i)).getDroit().getDroitModel().getTypeDroit(),
                        ALCSDroit.TYPE_MEN, false)) {
                    list.addData("colonne_naissance", (calcul.get(i)).getDroit().getEnfantComplexModel()
                            .getPersonneEtendueComplexModel().getPersonne().getDateNaissance());
                    list.addData("colonne_echeance", (calcul.get(i)).getDroit().getDroitModel().getFinDroitForcee());
                }

                // récupère le libellé du motif de fin de droit
                if (!JadeStringUtil.equals((calcul.get(i)).getDroit().getDroitModel().getTypeDroit(),
                        ALCSDroit.TYPE_MEN, false)) {
                    list.addData("colonne_motif",

                            ALImplServiceLocator.getCalculMontantsService().isMontantForceZero(calcul.get(i)) ? ""
                                    : ALServiceLocator.getDroitEcheanceService()
                                            .getLibelleMotif((calcul.get(i)).getDroit(), langueDocument));
                }

                // récupère uniquement le LFM , lFP
                if (ALCSTarif.CATEGORIE_LFM.equals((calcul.get(i)).getTarif())) {

                    list.addData("colonne_tarif", this.getText("al.decision.agricole.tarif.montagne", langueDocument));
                } else if (ALCSTarif.CATEGORIE_LFP.equals((calcul.get(i)).getTarif())) {
                    list.addData("colonne_tarif", this.getText("al.decision.agricole.tarif.plaine", langueDocument));

                }
                list.addData("colonne_code_monnaie", this.getText("al.decision.liste.droit.chf", langueDocument));

                list.addData("colonne_montant",
                        JANumberFormatter.fmt((calcul.get(i)).getCalculResultMontantBase(), true, true, false, 2));

                tableau_colonne.add(list);
                // cas de droits dont tiers bénéficiaire est autre que tiers
                // benfeciaire du dossier
                DataList DroitBenef = new DataList("droitBeneficiaire");

                if (!JadeStringUtil.isBlankOrZero((calcul.get(i)).getDroit().getDroitModel().getIdTiersBeneficiaire())
                        && !JadeStringUtil.equals(dossier.getDossierModel().getIdTiersBeneficiaire(),
                                (calcul.get(i)).getDroit().getDroitModel().getIdTiersBeneficiaire(), false)) {

                    loadDroitTiersBeneficiaire((calcul.get(i)).getDroit(), DroitBenef, langueDocument);
                    tableau_colonne.add(DroitBenef);

                } else {
                    DataList ligneVide = new DataList("ligneVide");
                    tableau_colonne.add(ligneVide);
                }

            }
        }

        // charge, le cas échéant l'adresse de paiement au niveau du dossier
        if (!JadeStringUtil.isBlankOrZero(dossier.getDossierModel().getIdTiersBeneficiaire())) {
            // savoir si adresse de paiement est nécessaire
            boolean notAdressePaiementDossier = isNotAllDroitBenEgalBenDos(listTiersBeneficiaireDroit,
                    dossier.getDossierModel().getIdTiersBeneficiaire());
            // si adresse de paiement est nécessaire
            if (!notAdressePaiementDossier) {
                loadInfoVersementDirect(documentData, dossier, langueDocument);
            }

        }

        documentData.add(tableau_colonne);

        Collection tableau_total = new Collection("tableau_total");

        // si taux de versement égal à 100% ou pas égal à 0
        if ((Double.valueOf(dossier.getDossierModel().getTauxVersement())
                .compareTo(ALConstNumeric.CENT_POURCENT) == 0)) {
            list = new DataList("total");
            list.addData("tableau_total_0", this.getText("al.decision.liste.droit.total.alloc", langueDocument));
            list.addData("tableau_total_1", this.getText("al.decision.liste.droit.chf", langueDocument));
            list.addData("tableau_total_2",
                    JANumberFormatter.fmt((String) total.get(ALConstCalcul.TOTAL_BASE), true, true, false, 2));
            tableau_total.add(list);

        }

        else if (Double.valueOf(dossier.getDossierModel().getTauxVersement())
                .compareTo(ALConstNumeric.ZERO_VALEUR) != 0) {
            // total des allocations pour un 100%
            list = new DataList("partiel");
            list.addData("tableau_partiel_0", this.getText("al.decision.liste.droit.total.alloc", langueDocument));
            list.addData("tableau_partiel_1", this.getText("al.decision.liste.droit.chf", langueDocument));

            list.addData("tableau_partiel_2",
                    JANumberFormatter.fmt((String) total.get(ALConstCalcul.TOTAL_BASE), true, true, false, 2));

            tableau_total.add(list);

            // total à payer par rapport au taux d'occupation
            list = new DataList("total");

            list.addData("tableau_total_0", this.getText("al.decision.liste.droit.alloc.taux.dossier", langueDocument)
                    + " " + dossier.getDossierModel().getTauxVersement() + " "
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
        tableau_sous_total = loadMontantJoursFin(dossier, calcul, total, date, list, tableau_sous_total,
                langueDocument);
        documentData.add(tableau_sous_total);

        // Rempli la partie jour
        Collection tableau_jour = new Collection("tableau_jour");

        if (!JadeStringUtil.equals(dossier.getDossierModel().getActiviteAllocataire(), ALCSDossier.ACTIVITE_AGRICULTEUR,
                false)) {

            list = new DataList("jour");
            list.addData("tableau_jour_0", this.getText("al.decision.liste.droit.total.jour", langueDocument));
            list.addData("tableau_jour_1", this.getText("al.decision.liste.droit.chf", langueDocument));
            list.addData("tableau_jour_2", JANumberFormatter.fmt((String) total.get(ALConstCalcul.TOTAL_UNITE_EFFECTIF),
                    true, true, false, 2));
            // ajoute la liste à la collection
            tableau_jour.add(list);
        }
        // ajoute la collection au document
        documentData.add(tableau_jour);

        addLigneTarifAgricole(documentData, dossier, calcul, langueDocument);
    }

    protected void addLigneTarifAgricole(DocumentData documentData, DossierComplexModel dossier,
            List<CalculBusinessModel> calcul, String langueDocument)
            throws JadeApplicationException, JadePersistenceException {

        String tarifCode = null;
        String tarifText = null;
        AgricoleModel infoAgriculteur = null;

        AgricoleSearchModel search = new AgricoleSearchModel();
        search.setForIdAllocataire(dossier.getDossierModel().getIdAllocataire());
        search = ALImplServiceLocator.getAgricoleModelService().search(search);

        if (search.getSize() == 1) {
            infoAgriculteur = (AgricoleModel) search.getSearchResults()[0];
        }

        if (infoAgriculteur == null || infoAgriculteur.getDomaineMontagne() == null) {
            throw new ALDecisionException("DecisionAgricoleServiceImpl#addLigneTarifAgricole : ce dossier ("
                    + dossier.getId() + ") ne contient pas d'information de domaine pour l'agriculteur");
        } else if (infoAgriculteur.getDomaineMontagne()) {
            tarifCode = "al.decision.agricole.tarif.montagne";
            tarifText = "al.decision.agricole.texte.tarif.montagne";
        } else {
            tarifCode = "al.decision.agricole.tarif.plaine";
            tarifText = "al.decision.agricole.texte.tarif.plaine";
        }

        documentData.addData("type_droit_agricole",
                this.getText(tarifCode, langueDocument) + "=" + this.getText(tarifText, langueDocument));
    }

    @Override
    protected void loadListNaissance(DocumentData documentData, DossierComplexModel dossier,
            List<CalculBusinessModel> calcul, String date, String langueDocument)
            throws JadeApplicationException, JadePersistenceException {

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

                list.addData("tableau_naissance_0",
                        libelle + " "
                                + (calcul.get(i)).getDroit().getEnfantComplexModel().getPersonneEtendueComplexModel()
                                        .getTiers().getDesignation1()
                                + " "
                                + (calcul.get(i)).getDroit().getEnfantComplexModel().getPersonneEtendueComplexModel()
                                        .getTiers().getDesignation2()
                                + "\n" + (calcul.get(i)).getDroit().getEnfantComplexModel()
                                        .getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel());

                String chaineVide = "";

                list.addData("tableau_naissance_1", chaineVide);
                list.addData("tableau_naissance_2", chaineVide);
                list.addData("tableau_naissance_3", this.getText("al.decision.liste.droit.chf", langueDocument));
                list.addData("tableau_naissance_4",
                        JANumberFormatter.fmt((calcul.get(i)).getCalculResultMontantEffectif(), true, true, false, 2));
                tableau_naissance.add(list);

            }
        }
        documentData.add(tableau_naissance);
    }

    @Override
    protected void loadTextCopies(DocumentData documentData, List<String> listCopies,
            DossierComplexModel dossierComplexModel, String langueDocument) throws JadeApplicationException {
        if (ALCSDossier.STATUT_IS.equals(dossierComplexModel.getDossierModel().getStatut())) {
            documentData.addData("copie_annexe_label", this.getText("al.decision.adi.annexe", langueDocument));
            documentData.addData("copie_annexe_destinataire",
                    this.getText("al.decision.adi.annexe.ment", langueDocument));
            Collection tableau_copie = new Collection("tableau_copie_definition");
            if (!listCopies.isEmpty()) {
                for (int i = 0; i < listCopies.size(); i++) {
                    DataList list = new DataList("copie");
                    if (i == 0) {
                        list.addData("copie_label", this.getText("al.decision.standard.copies.copie", langueDocument));
                    } else {
                        list.addData("copie_label", "");
                    }
                    list.addData("copie_destinataire", listCopies.get(i));
                    tableau_copie.add(list);
                }
            }
            documentData.add(tableau_copie);
        } else {
            super.loadTextCopies(documentData, listCopies, dossierComplexModel, langueDocument);
        }
    }

    @Override
    protected void loadTextesDecision(DocumentData documentData, DossierComplexModel dossierComplexModel,
            String commentaire, String langueDocument) throws JadeApplicationException, JadePersistenceException {

        // INFOROMD0028 - AF - Modifications montants AF VD (CBU)
        if (JadeStringUtil.equals(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS,
                dossierComplexModel.getDossierModel().getTarifForce(), false)) {
            documentData.addData("texte_paragraphe_droitAcquis",
                    this.getText("al.decision.standard.paragraphe.droitAcquis", langueDocument));
        }
        // FIN INFOROMD0028

        if (ALCSDossier.ETAT_RADIE.equals(dossierComplexModel.getDossierModel().getEtatDossier())) {
            if (!JadeStringUtil.isBlank(commentaire)) {
                documentData.addData("texte_paragraphe_libre", JadeStringUtil.removeChar(commentaire, '\n'));
            }
        } else if (ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE
                .equals(dossierComplexModel.getDossierModel().getActiviteAllocataire())) {
            super.loadTextesDecision(documentData, dossierComplexModel, commentaire, langueDocument);
        } else {
            if (!JadeStringUtil.isBlank(commentaire)) {
                documentData.addData("texte_paragraphe_libre", JadeStringUtil.removeChar(commentaire, '\n'));
            }
            documentData.addData("texte_paragraphe_2",
                    this.getText("al.decision.standard.paragraphe2", langueDocument));
            documentData.addData("texte_paragraphe_3",
                    this.getText("al.decision.standard.paragraphe3", langueDocument));
        }
    }

    /**
     * Définit l'id de l'en-tête à utiliser en fonction des paramètres de la caisse (ici les dossiers liés aux
     * agriculteurs)
     *
     * @param document
     *                     document à générer
     * @throws JadePersistenceException
     *                                      Exception levée si le nom de la caisse n'a pas pu être récupéré
     * @throws JadeApplicationException
     *                                      Exception levée si le nom de la caisse n'a pas pu être récupéré
     */
    @Override
    protected void setIdEntete(DocumentData document) throws JadePersistenceException, JadeApplicationException {

        document.addData("idEntete", ALServiceLocator.getParametersServices().getNomCaisse() + "_Agricole");
    }

    /**
     * Définit la signature pour le document
     *
     * @param document
     *                     document à générer
     * @throws JadePersistenceException
     *                                      Exception levée si le nom de la caisse n'a pas pu être récupéré
     * @throws JadeApplicationException
     *                                      Exception levée si le nom de la caisse n'a pas pu être récupéré
     */
    @Override
    protected void setIdSignature(DocumentData document) throws JadeApplicationException, JadePersistenceException {
        // contrôle du paramètre
        if (document == null) {
            throw new ALDocumentException("AbstractDocument# setIdSignature : document is null");
        }

        document.addData("idSignature", ALServiceLocator.getParametersServices().getNomCaisse() + "_Compensation");
    }

}
