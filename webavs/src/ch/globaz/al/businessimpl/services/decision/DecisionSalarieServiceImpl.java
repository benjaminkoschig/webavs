package ch.globaz.al.businessimpl.services.decision;

import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.HashMap;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.constantes.ALConstCaisse;
import ch.globaz.al.business.constantes.ALConstCalcul;
import ch.globaz.al.business.constantes.ALConstDecisions;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.constantes.ALConstNumeric;
import ch.globaz.al.business.exceptions.decision.ALDecisionException;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.decision.DecisionSalarieService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Classe fille de DecisionAbstractServiceImpl qui contient toutes les spécificités des décisions pour salariés
 * 
 * @author JER/PTA
 */
public class DecisionSalarieServiceImpl extends DecisionAbstractServiceImpl implements DecisionSalarieService {

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
                    && (!JadeStringUtil.equals(ALCSDroit.TYPE_NAIS, listDroitCalculDossier.get(i).getType(), false) && !JadeStringUtil
                            .equals(ALCSDroit.TYPE_ACCE, listDroitCalculDossier.get(i).getType(), false))) {
                listCalculDroitIdentique.add(listDroitCalculDossier.get(i));
            }
        }

        return listCalculDroitIdentique;
    }

    private void loadChampsDroitHorloger(DroitComplexModel droit, String montantDroit, String montantSupplHorloger,
            DataList list, String typeTarif, String typePrestation, DossierComplexModel dossier, String langueDocument,
            Collection tableau_colonne, Boolean isHide) throws JadeApplicationException, JadePersistenceException {

        if ((!JadeNumericUtil.isEmptyOrZero(montantDroit) || droit.getDroitModel().getForce())
                && (!ALCSDroit.TYPE_NAIS.equals(typePrestation) && !ALCSDroit.TYPE_ACCE.equals(typePrestation))) {
            list = new DataList("colonne");

            // si le droit est de type ménage
            if (JadeStringUtil.equals(droit.getDroitModel().getTypeDroit(), ALCSDroit.TYPE_MEN, false)) {
                list.addData("colonne_enfant", this.getText("al.decision.liste.droit.alloc.typeMenage", langueDocument));

            } else {

                // FORMATION et ENFANT avec supplément
                list.addData(
                        "colonne_enfant",
                        (droit.getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1())
                                + " "
                                + (droit.getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers()
                                        .getDesignation2())
                                + "\n"
                                + (droit.getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonneEtendue()
                                        .getNumAvsActuel()));

            }

            list.addData("colonne_naissance", droit.getEnfantComplexModel().getPersonneEtendueComplexModel()
                    .getPersonne().getDateNaissance());
            list.addData("colonne_echeance", droit.getDroitModel().getFinDroitForcee());
            // récupère le libellé du motif de fin de droit

            if (!JadeStringUtil.equals(droit.getDroitModel().getTypeDroit(), ALCSDroit.TYPE_MEN, false)) {

                if (droit.getDroitModel().getForce()
                        && JadeNumericUtil.isEmptyOrZero(droit.getDroitModel().getMontantForce())
                        && !JadeNumericUtil.isEmptyOrZero(montantSupplHorloger)) {
                    list.addData(
                            "colonne_motif",

                            ""
                                    + "\n"

                                    + (JadeStringUtil.equals(ALCSDroit.TYPE_ENF, droit.getDroitModel().getTypeDroit(),
                                            false) ? this.getText("al.decision.liste.droit.supplHorlogerEnfant",
                                            langueDocument) : this.getText(
                                            "al.decision.liste.droit.supplHorlogerFormation", langueDocument)));

                }

                else {
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

            list.addData(
                    "colonne_code_monnaie",
                    JadeStringUtil.isBlankOrZero(montantSupplHorloger) /* || (!isHide) */? this.getText(
                            "al.decision.liste.droit.chf", langueDocument) : this.getText(
                            "al.decision.liste.droit.chf", langueDocument)
                            + "\n"
                            + this.getText("al.decision.liste.droit.chf", langueDocument));
            //
            list.addData(
                    "colonne_montant",
                    JadeStringUtil.isBlankOrZero(montantSupplHorloger) /* || (!isHide) */? JANumberFormatter.fmt(
                            montantDroit, true, true, false, 2) : JANumberFormatter.fmt(montantDroit, true, true,
                            false, 2) + "\n" + JANumberFormatter.fmt(montantSupplHorloger, true, true, false, 2));

            tableau_colonne.add(list);

            if (!JadeStringUtil.isBlankOrZero(droit.getDroitModel().getIdTiersBeneficiaire())
                    && !JadeStringUtil.equals(dossier.getDossierModel().getIdTiersBeneficiaire(), droit.getDroitModel()
                            .getIdTiersBeneficiaire(), false)) {

                DataList DroitBenef = new DataList("droitBeneficiaire");

                loadDroitTiersBeneficiaire(droit, DroitBenef, langueDocument);
                tableau_colonne.add(DroitBenef);

            } else {
                DataList ligneVide = new DataList("ligneVide");
                tableau_colonne.add(ligneVide);
            }
        }
    }

    private void loadChampsNonHorlogere(DataList list, Collection tableau_colonne, String langueDocument,
            ArrayList<CalculBusinessModel> calcul, DossierComplexModel dossier,
            ArrayList<String> listTiersBeneficiaireDroit, int i) throws JadeApplicationException,
            JadePersistenceException {
        // si le montant calcul de base est <> de 0 ou que le montant a été forcé et le type de droit
        // autre que naissance et accueil
        if ((!JadeNumericUtil.isEmptyOrZero((calcul.get(i)).getCalculResultMontantBase()) || (calcul.get(i)).getDroit()
                .getDroitModel().getForce())
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

                listTiersBeneficiaireDroit.add(calcul.get(i).getDroit().getDroitModel().getIdTiersBeneficiaire());
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

            list.addData("colonne_code_monnaie", this.getText("al.decision.liste.droit.chf", langueDocument));

            list.addData("colonne_montant",
                    JANumberFormatter.fmt((calcul.get(i)).getCalculResultMontantEffectif(), true, true, false, 2));

            // list.addData("colonne_montant", (calcul.get(i))
            // .getCalculResultMontantBase());
            tableau_colonne.add(list);

            if (!JadeStringUtil.isBlankOrZero((calcul.get(i)).getDroit().getDroitModel().getIdTiersBeneficiaire())
                    && !JadeStringUtil.equals(dossier.getDossierModel().getIdTiersBeneficiaire(), (calcul.get(i))
                            .getDroit().getDroitModel().getIdTiersBeneficiaire(), false)) {

                DataList DroitBenef = new DataList("droitBeneficiaire");

                loadDroitTiersBeneficiaire((calcul.get(i)).getDroit(), DroitBenef, langueDocument);
                tableau_colonne.add(DroitBenef);

            } else {
                DataList ligneVide = new DataList("ligneVide");
                tableau_colonne.add(ligneVide);
            }
        }

    }

    private void loadChampsSpecificiteHorlogere(DataList list, Collection tableau_colonne, String langueDocument,
            ArrayList<CalculBusinessModel> calcul, DossierComplexModel dossier,
            ArrayList<String> listTiersBeneficiaireDroit /* , int i */, String idDroit, String montantDroit,
            String montantSupplHorloger, String typePrestation) throws JadeApplicationException,
            JadePersistenceException {
        if (calcul.size() > 0) {

            // si c'est le premier et le dernier
            if ((calcul.size() == 1)
            /*
             * || JadeStringUtil.equals(calcul.get(i).getDroit().getDroitModel().getTypeDroit(), ALCSDroit.TYPE_MEN,
             * false)
             */) {
                loadChampsDroitHorloger(calcul.get(0).getDroit(), calcul.get(0).getCalculResultMontantEffectif(),
                        montantSupplHorloger, list, calcul.get(0).getTarif(), calcul.get(0).getType(), dossier,
                        langueDocument, tableau_colonne, calcul.get(0).isHideDroit());
            }

            // si c'est les droit sont au nobre de deux, une fois formation ou enfant et une fois sup. horloger

            else if (calcul.size() == 2) {

                String montantSuppHorloger = null;
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
                        // montantSupplHorloger = calcul.get(i).getCalculResultMontantEffectif();
                        calculEnfForm = calcul.get(i);
                    }

                }

                loadChampsDroitHorloger(calculEnfForm.getDroit(), calculEnfForm.getCalculResultMontantEffectif(),
                        montantSuppHorloger, list, calculEnfForm.getTarif(), calculEnfForm.getType(), dossier,
                        langueDocument, tableau_colonne, isHideDroit);

            } else if (calcul.size() > 2) {
                throw new ALDecisionException(
                        "DecisionSalarieServiceImpl#loadChampsSpecificiteHorlogere: more than 2 calcul for this droit: ");
            }

        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.businessimpl.services.decision.DecisionAbstractServiceImpl
     * #loadListDroit(ch.globaz.topaz.datajuicer.DocumentData, ch.globaz.al.business.models.dossier.DossierComplexModel,
     * java.util.ArrayList)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void loadListDroit(DocumentData documentData, DossierComplexModel dossier,
            ArrayList<CalculBusinessModel> calcul, String date, String langueDocument) throws JadeApplicationException,
            JadePersistenceException {

        // vérification des paramètres

        if (documentData == null) {
            throw new ALDecisionException("DecisionSalarieServiceImpl#loadListDroit: documentData is null");
        }
        if (dossier == null) {
            throw new ALDecisionException("DecisionSalarieServiceImpl#loadListDroit: dossier is null");
        }
        if (calcul == null) {
            throw new ALDecisionException("DecisionSalarieServiceImpl#loadListDroit: calcul is null");
        }
        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALDecisionException("DecisionSalarieServiceImpl#loadListDroit: " + date
                    + " is not a valid globaz's date (dd.mm.yyyy");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDecisionException("DecisionSalarieServiceImpl#loadListDroit: language  " + langueDocument
                    + " is not  valid ");
        }

        documentData.addData("isSalarie", "OUI");

        Collection tableau_entete = new Collection("tableau_entete_definition");
        DataList list = new DataList("entete");
        list.addData("entete_enfant", this.getText("al.decision.liste.droit.enfant", langueDocument));
        list.addData("entete_naissance", this.getText("al.decision.liste.droit.naissance", langueDocument));
        list.addData("entete_echeance", this.getText("al.decision.liste.droit.echeance", langueDocument));
        list.addData("entete_motif", this.getText("al.decision.liste.droit.motif", langueDocument));
        list.addData("entete_montant", this.getText("al.decision.liste.droit.mois", langueDocument));
        tableau_entete.add(list);
        documentData.add(tableau_entete);

        // liste des tiers bénéficiaires des droits
        ArrayList<String> listTiersBeneficiaireDroit = new ArrayList<String>();

        // Rempli la partie LISTE DROITS de la décision
        Collection tableau_colonne = new Collection("tableau_colonne_definition");

        // calcul du montant total de la décision
        HashMap total = ALServiceLocator.getCalculBusinessService().getTotal(dossier.getDossierModel(), calcul,
                ALCSDossier.UNITE_CALCUL_MOIS, "1", false, date);

        calcul = (ArrayList<CalculBusinessModel>) total.get(ALConstCalcul.DROITS_CALCULES);

        String idDroit = null;
        String montantDroit = null;
        String montantSupplHorloger = null;
        String typeTarif = null;
        HashMap<String, String> droitDecision = new HashMap<String, String>();
        for (int i = 0; i < calcul.size(); i++) {

            if ((ALImplServiceLocator.getHorlogerBusinessService().isCaisseHorlogere() == true)
                    && (JadeDateUtil.areDatesEquals(dossier.getDossierModel().getDebutValidite(),
                            ALConstDecisions.DEB_SUP_HORL)
                            || JadeDateUtil.isDateAfter(dossier.getDossierModel().getDebutValidite(),
                                    ALConstDecisions.DEB_SUP_HORL)
                            || JadeDateUtil.areDatesEquals(dossier.getDossierModel().getFinValidite(),
                                    ALConstDecisions.DEB_SUP_HORL) || JadeDateUtil.isDateAfter(dossier
                            .getDossierModel().getFinValidite(), ALConstDecisions.DEB_SUP_HORL))) {
                if (!JadeStringUtil.equals(calcul.get(i).getDroit().getDroitModel().getTypeDroit(), ALCSDroit.TYPE_MEN,
                        false)/* || !JadeStringUtil.isBlankOrZero(calcul.get(i).getCalculResultMontantEffectif()) */) {

                    if (!ALCSDroit.TYPE_NAIS.equals((calcul.get(i)).getType())
                            && !ALCSDroit.TYPE_ACCE.equals((calcul.get(i)).getType())) {
                        // récupère id Droit
                        idDroit = (calcul.get(i)).getDroit().getDroitModel().getIdDroit();

                        // récupère la lsite des calcul pour le même droit
                        ArrayList<CalculBusinessModel> listCalculUnDroitDossier = listCalculDroitIdentique(calcul,
                                idDroit);

                        if (!droitDecision.containsValue(idDroit)) {

                            loadChampsSpecificiteHorlogere(list, tableau_colonne, langueDocument,
                                    listCalculUnDroitDossier, dossier, listTiersBeneficiaireDroit /* , i */, idDroit,
                                    montantDroit, montantSupplHorloger, typeTarif);
                        }
                        // ajoute le droit dans la hashmap
                        droitDecision.put(idDroit, idDroit);

                    }
                }
            }

            else {

                loadChampsNonHorlogere(list, tableau_colonne, langueDocument, calcul, dossier,
                        listTiersBeneficiaireDroit, i);

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

        // Rempli la partie TOTAL de la décision
        Collection tableau_total = new Collection("tableau_total");

        // si taux de versement égal à 100%
        if (Double.valueOf(dossier.getDossierModel().getTauxVersement()).compareTo(ALConstNumeric.CENT_POURCENT) == 0) {
            list = new DataList("total");
            list.addData("tableau_total_0", this.getText("al.decision.liste.droit.total.alloc", langueDocument));
            list.addData("tableau_total_1", this.getText("al.decision.liste.droit.chf", langueDocument));

            list.addData("tableau_total_2",
                    JANumberFormatter.fmt((String) total.get(ALConstCalcul.TOTAL_BASE), true, true, false, 2));
            tableau_total.add(list);
            // taux de versement différent de 0
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
            list.addData("tableau_total_2", JANumberFormatter.fmt("0", true, true, false, 2));
            tableau_total.add(list);
        }

        documentData.add(tableau_total);

        // Rempli la partie PAR JOUR de la décision
        Collection tableau_sous_total = new Collection("tableau_sous_total");

        // création des tableaux "jours de début/fin"
        tableau_sous_total = loadMontantJoursDebut(dossier, calcul, total, date, list, tableau_sous_total,
                langueDocument);
        tableau_sous_total = loadMontantJoursFin(dossier, calcul, total, date, list, tableau_sous_total, langueDocument);
        documentData.add(tableau_sous_total);

        // Rempli la partie jour
        Collection tableau_jour = new Collection("tableau_jour");

        if (!JadeStringUtil.equals(dossier.getDossierModel().getActiviteAllocataire(),
                ALCSDossier.ACTIVITE_INDEPENDANT, false)) {
            list = new DataList("jour");
            list.addData("tableau_jour_0", this.getText("al.decision.liste.droit.total.jour", langueDocument));
            list.addData("tableau_jour_1", this.getText("al.decision.liste.droit.chf", langueDocument));
            list.addData("tableau_jour_2",
                    JANumberFormatter.fmt((String) total.get(ALConstCalcul.TOTAL_UNITE_EFFECTIF), true, true, false, 2));
            // ajoute la liste à la collection
            tableau_jour.add(list);
        }
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

                // calcul du montant
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
    protected void loadTextCopies(DocumentData documentData, ArrayList<String> listCopies,
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
                    // si le destinataire de la copie salarié, prendre en compte la traduction
                    if (JadeStringUtil.equals(listCopies.get(i),
                            JadeCodesSystemsUtil.getCodeLibelle(ALCSDossier.ACTIVITE_SALARIE), true)) {
                        list.addData("copie_destinataire",
                                this.getText("al.decision.standard.copie.salarie", langueDocument));
                    } else {
                        list.addData("copie_destinataire", listCopies.get(i));
                    }
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
        if (JadeStringUtil.equals(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS, dossierComplexModel.getDossierModel()
                .getTarifForce(), false)) {
            documentData.addData("texte_paragraphe_droitAcquis",
                    this.getText("al.decision.standard.paragraphe.droitAcquis", langueDocument));
        }
        // FIN INFOROMD0028

        if (ALCSDossier.ETAT_RADIE.equals(dossierComplexModel.getDossierModel().getEtatDossier())) {
            if (!JadeStringUtil.isBlank(commentaire)) {
                documentData.addData("texte_paragraphe_libre", JadeStringUtil.removeChar(commentaire, '\n'));
            }
            /*
             * documentData.addData("texte_paragraphe_3", this .getText("al.decision.standard.paragraphe3"));
             */

            documentData.addData("texte_signature_ligne_1",
                    this.getText("al.decision.standard.signature.ligne1", langueDocument));
            documentData.addData("texte_signature_ligne_2",
                    this.getText("al.decision.standard.signature.ligne2", langueDocument));
        } else if (ALCSDossier.ACTIVITE_INDEPENDANT.equals(dossierComplexModel.getDossierModel()
                .getActiviteAllocataire())
                && !JadeStringUtil.equals(ALServiceLocator.getParametersServices().getNomCaisse(),
                        ALConstCaisse.CAISSE_CCVD, true)) {
            if (!JadeStringUtil.isBlank(commentaire)) {
                documentData.addData("texte_paragraphe_libre", JadeStringUtil.removeChar(commentaire, '\n'));
            }

            documentData
                    .addData("texte_paragraphe_2", this.getText("al.decision.standard.paragraphe2", langueDocument));
            documentData
                    .addData("texte_paragraphe_3", this.getText("al.decision.standard.paragraphe3", langueDocument));

            documentData.addData("texte_signature_ligne_1",
                    this.getText("al.decision.standard.signature.ligne1", langueDocument));
            documentData.addData("texte_signature_ligne_2",
                    this.getText("al.decision.standard.signature.ligne2", langueDocument));
        } else {
            super.loadTextesDecision(documentData, dossierComplexModel, commentaire, langueDocument);
        }
    }

}
