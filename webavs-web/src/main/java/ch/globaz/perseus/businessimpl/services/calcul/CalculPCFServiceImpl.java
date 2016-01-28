/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.calcul;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.perseus.utils.PFUserHelper;
import java.util.List;
import ch.globaz.perseus.business.calcul.InputCalcul;
import ch.globaz.perseus.business.calcul.OutputCalcul;
import ch.globaz.perseus.business.calcul.OutputData;
import ch.globaz.perseus.business.constantes.CSVariableMetier;
import ch.globaz.perseus.business.constantes.IPFConstantes;
import ch.globaz.perseus.business.exceptions.calcul.CalculException;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.exceptions.models.donneesfinancieres.DonneesFinancieresException;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.exceptions.models.variablemetier.VariableMetierException;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnue;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnueSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnueType;
import ch.globaz.perseus.business.models.donneesfinancieres.Dette;
import ch.globaz.perseus.business.models.donneesfinancieres.DetteSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.Fortune;
import ch.globaz.perseus.business.models.donneesfinancieres.FortuneSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.FortuneType;
import ch.globaz.perseus.business.models.donneesfinancieres.Revenu;
import ch.globaz.perseus.business.models.donneesfinancieres.RevenuSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.RevenuType;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamille;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamilleSearchModel;
import ch.globaz.perseus.business.models.variablemetier.VariableMetier;
import ch.globaz.perseus.business.models.variablemetier.VariableMetierSearch;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.calcul.CalculPCFService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

/**
 * Implémentation du service pour le calcul des prestations complémentaires pour les familles
 * 
 * @author DDE
 * 
 */
public class CalculPCFServiceImpl extends PerseusAbstractServiceImpl implements CalculPCFService {

    /**
     * Construit les données pour l'entete du plan de calcul
     * 
     * @param inputCalcul
     * @param outputCalcul
     * @return OutputCalcul
     * @throws JadePersistenceException
     * @throws CalculException
     */
    private OutputCalcul buildEntente(InputCalcul inputCalcul, OutputCalcul outputCalcul) throws CalculException {
        // Données à enregistrer
        String requerantInfos = "";
        String requerantNss = "";
        String conjointInfos = "";
        String enfants = "";
        String periode = "";

        // Retrouver les infos de toutes les personnes
        PersonneEtendueComplexModel requerant = inputCalcul.getDemande().getSituationFamiliale().getRequerant()
                .getMembreFamille().getPersonneEtendue();
        PersonneEtendueComplexModel conjoint = inputCalcul.getDemande().getSituationFamiliale().getConjoint()
                .getMembreFamille().getPersonneEtendue();
        requerantNss = requerant.getPersonneEtendue().getNumAvsActuel();
        requerantInfos = buildMembreFamilleString(requerant.getTiers().getDesignation1(), requerant.getTiers()
                .getDesignation2(), requerant.getPersonne().getDateNaissance());
        conjointInfos = buildMembreFamilleString(conjoint.getTiers().getDesignation1(), conjoint.getTiers()
                .getDesignation2(), conjoint.getPersonne().getDateNaissance());
        // Retrouver les enfants
        for (EnfantFamille enfantFamille : inputCalcul.getListeEnfants()) {
            enfants += buildMembreFamilleString(enfantFamille.getEnfant().getMembreFamille().getPersonneEtendue()
                    .getTiers().getDesignation1(), enfantFamille.getEnfant().getMembreFamille().getPersonneEtendue()
                    .getTiers().getDesignation2(), enfantFamille.getEnfant().getMembreFamille().getPersonneEtendue()
                    .getPersonne().getDateNaissance())
                    + ",";
        }
        // La période
        periode = inputCalcul.getDemande().getSimpleDemande().getDateDebut() + " - "
                + inputCalcul.getDemande().getSimpleDemande().getDateFin();

        outputCalcul.addDonnee(OutputData.ENTETE_REQUERANT_INFOS, requerantInfos);
        outputCalcul.addDonnee(OutputData.ENTETE_REQUERANT_NSS, requerantNss);
        outputCalcul.addDonnee(OutputData.ENTETE_CONJOINT_INFOS, conjointInfos);
        outputCalcul.addDonnee(OutputData.ENTETE_ENFANTS_INFOS, enfants);
        outputCalcul.addDonnee(OutputData.ENTETE_PERIODE, periode);
        // La localité est définie dans le calcul du loyer dans le calcul des dépenses reconnues

        return outputCalcul;
    }

    private String buildMembreFamilleString(String nom, String prenom, String naiss) {
        return nom + " " + prenom + " - " + naiss;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.calcul.CalculPCFService#calculerPCF(ch.globaz.perseus.business.models.demande
     * .Demande)
     */
    @Override
    public OutputCalcul calculerPCF(Demande demande) throws JadePersistenceException, CalculException {
        if ((demande == null) || demande.isNew()) {
            throw new CalculException("Unable to calculate, demande is null or new");
        }
        OutputCalcul outputCalcul = new OutputCalcul();

        InputCalcul inputCalcul = loadInputData(demande);

        outputCalcul = buildEntente(inputCalcul, outputCalcul);

        outputCalcul = this.calculerPCF(inputCalcul, outputCalcul);

        return outputCalcul;
    }

    private OutputCalcul calculerPCF(InputCalcul inputCalcul, OutputCalcul outputCalcul) throws CalculException {

        try {
            outputCalcul = PerseusImplServiceLocator.getCalculFortuneService().calculerFortuneNette(inputCalcul,
                    outputCalcul);

            outputCalcul = PerseusImplServiceLocator.getCalculRevenuService().calculerRevenusDeterminants(inputCalcul,
                    outputCalcul);

            outputCalcul = PerseusImplServiceLocator.getCalculRevenuService().calculerRevenusBrutImpotSource(
                    inputCalcul, outputCalcul);

            outputCalcul = PerseusImplServiceLocator.getCalculDepensesReconnuesService().calculerDepensesReconnues(
                    inputCalcul, outputCalcul);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CalculException("JadeApplicationServiceNotAvailableException during calcul : " + e.getMessage());
        }

        // Calcul de la prestation
        Float prestationAnnuelle = outputCalcul.getDonnee(OutputData.DEPENSES_RECONNUES);
        prestationAnnuelle -= outputCalcul.getDonnee(OutputData.REVENUS_DETERMINANT);

        Integer nbEnfants = inputCalcul.getListeEnfants().size();
        // Plafonnement de la prestation
        Float plafondPrestation = new Float(0);
        // Si il y'a un enfant de moins de 6 ans dans la famille
        Boolean hasEnfantMoins6ans = false;
        for (EnfantFamille ef : inputCalcul.getListeEnfants()) {
            String dateNaissance = ef.getEnfant().getMembreFamille().getPersonneEtendue().getPersonne()
                    .getDateNaissance();
            int age = JadeDateUtil.getNbYearsBetween(dateNaissance, inputCalcul.getDemande().getSimpleDemande()
                    .getDateDebut());
            if (age < IPFConstantes.AGE_6ANS) {
                hasEnfantMoins6ans = true;
            }
        }
        if (hasEnfantMoins6ans) {
            // Plafonné au montant des besoins vitaux
            plafondPrestation = outputCalcul.getDonnee(OutputData.DEPENSES_BESOINS_VITAUX);
        } else {
            // Si non trouvé le plafond en fonction du 2ème tableau
            if (JadeStringUtil.isEmpty(inputCalcul.getDemande().getSituationFamiliale().getConjoint().getId())) {
                // Pour un parent seul
                switch (nbEnfants) {
                    case 1:
                        plafondPrestation = inputCalcul.getVariableMetier(
                                CSVariableMetier.PLAFOND_PRESTATION_PARENT_SEUL_1_ENFANT).getMontant();
                        break;
                    case 2:
                        plafondPrestation = inputCalcul.getVariableMetier(
                                CSVariableMetier.PLAFOND_PRESTATION_PARENT_SEUL_2_ENFANTS).getMontant();
                        break;
                    case 3:
                        plafondPrestation = inputCalcul.getVariableMetier(
                                CSVariableMetier.PLAFOND_PRESTATION_PARENT_SEUL_3_ENFANTS).getMontant();
                        break;
                    case 4:
                        plafondPrestation = inputCalcul.getVariableMetier(
                                CSVariableMetier.PLAFOND_PRESTATION_PARENT_SEUL_4_ENFANTS).getMontant();
                        break;
                    case 5:
                        plafondPrestation = inputCalcul.getVariableMetier(
                                CSVariableMetier.PLAFOND_PRESTATION_PARENT_SEUL_5_ENFANTS).getMontant();
                        break;
                    case 6:
                        plafondPrestation = inputCalcul.getVariableMetier(
                                CSVariableMetier.PLAFOND_PRESTATION_PARENT_SEUL_6_ENFANTS).getMontant();
                        break;
                }

                // Pour chaque enfant supplémentaire ajouter le montant prévu
                Integer nbEnfantsSupplementaires = nbEnfants - 6;
                if (nbEnfantsSupplementaires > 0) {
                    plafondPrestation = inputCalcul.getVariableMetier(
                            CSVariableMetier.PLAFOND_PRESTATION_PARENT_SEUL_6_ENFANTS).getMontant();
                    plafondPrestation += inputCalcul.getVariableMetier(
                            CSVariableMetier.PLAFOND_PRESTATION_PARENT_SEUL_ENFANTS_SUPPLEMENTAIRE).getMontant()
                            * nbEnfantsSupplementaires;
                }

            } else {
                // Pour un couple
                switch (nbEnfants) {
                    case 1:
                        plafondPrestation = inputCalcul.getVariableMetier(
                                CSVariableMetier.PLAFOND_PRESTATION_COUPLE_1_ENFANT).getMontant();
                        break;
                    case 2:
                        plafondPrestation = inputCalcul.getVariableMetier(
                                CSVariableMetier.PLAFOND_PRESTATION_COUPLE_2_ENFANTS).getMontant();
                        break;
                    case 3:
                        plafondPrestation = inputCalcul.getVariableMetier(
                                CSVariableMetier.PLAFOND_PRESTATION_COUPLE_3_ENFANTS).getMontant();
                        break;
                    case 4:
                        plafondPrestation = inputCalcul.getVariableMetier(
                                CSVariableMetier.PLAFOND_PRESTATION_COUPLE_4_ENFANTS).getMontant();
                        break;
                    case 5:
                        plafondPrestation = inputCalcul.getVariableMetier(
                                CSVariableMetier.PLAFOND_PRESTATION_COUPLE_5_ENFANTS).getMontant();
                        break;
                    case 6:
                        plafondPrestation = inputCalcul.getVariableMetier(
                                CSVariableMetier.PLAFOND_PRESTATION_COUPLE_6_ENFANTS).getMontant();
                        break;
                }

                // Pour chaque enfant supplémentaire ajouter le montant prévu
                Integer nbEnfantsSupplementaires = nbEnfants - 6;
                if (nbEnfantsSupplementaires > 0) {
                    plafondPrestation = inputCalcul.getVariableMetier(
                            CSVariableMetier.PLAFOND_PRESTATION_COUPLE_6_ENFANTS).getMontant();
                    plafondPrestation += inputCalcul.getVariableMetier(
                            CSVariableMetier.PLAFOND_PRESTATION_COUPLE_ENFANT_SUPPLEMENTAIRE).getMontant()
                            * nbEnfantsSupplementaires;
                }
            }
        }

        Float prestationAnnuelleModif = new Float(0);
        if (prestationAnnuelle > plafondPrestation) {
            prestationAnnuelleModif = plafondPrestation;
        } else {
            prestationAnnuelleModif = prestationAnnuelle;
        }

        Float prestationMensuelle = new Float(0);
        Float excedantRevenu = new Float(0);
        if (prestationAnnuelleModif >= 0) {
            prestationMensuelle = prestationAnnuelleModif / 12;
            // Arrondir au franc supérieur dès le centime
            Double pmFloored = Math.floor(prestationMensuelle);
            if ((prestationMensuelle - pmFloored) > 0) {
                prestationMensuelle = new Float(pmFloored + 1);
            }
            // Arrondir la prestation au montant minimum des prestations complémentaires familles
            if (prestationMensuelle < inputCalcul.getVariableMetier(CSVariableMetier.MONTANT_MINIMAL_PCF).getMontant()) {
                prestationMensuelle = inputCalcul.getVariableMetier(CSVariableMetier.MONTANT_MINIMAL_PCF).getMontant();
            }
        } else {
            excedantRevenu = prestationAnnuelleModif * -1;
        }

        // Enregistrement des données
        outputCalcul.addDonnee(OutputData.PRESTATION_ANNUELLE, prestationAnnuelle);
        outputCalcul.addDonnee(OutputData.PRESTATION_ANNUELLE_MODIF, prestationAnnuelleModif);
        outputCalcul.addDonnee(OutputData.EXCEDANT_REVENU, excedantRevenu);
        outputCalcul.addDonnee(OutputData.PRESTATION_MENSUELLE, prestationMensuelle);

        return outputCalcul;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.calcul.CalculPCFService#calculerPCF(java.lang.String)
     */
    @Override
    public OutputCalcul calculerPCF(String idDemande) throws JadePersistenceException, CalculException {
        if (JadeStringUtil.isEmpty(idDemande)) {
            throw new CalculException("Unable to calculate, idDemande is null or empty");
        }
        Demande demande = new Demande();
        try {
            demande = PerseusServiceLocator.getDemandeService().read(idDemande);

        } catch (DemandeException e) {
            throw new CalculException("DemandeException during calcul : " + e.getMessage());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CalculException("JadeApplicationServiceNotAvailableException during calcul : " + e.getMessage());
        }

        return this.calculerPCF(demande);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.calcul.CalculPCFService#checkCalcul(ch.globaz.perseus.business.models.demande
     * .Demande)
     */
    @Override
    public String checkCalcul(Demande demande) throws JadePersistenceException, CalculException {
        String message = "";

        InputCalcul inputCalcul = loadInputData(demande);

        if (inputCalcul.getDonneesFinancieresRequerant().getElementDepenseReconnue(DepenseReconnueType.LOYER_ANNUEL)
                .isNew()) {
            message += JadeThread.getMessage("perseus.donneesfinancieres.depenseReconnue.loyerannuel.mandatory")
                    + "<br />";
        }
        // Si le nombre de personnes n'est pas renseigné
        if (!inputCalcul.getDonneesFinancieresRequerant().getElementDepenseReconnue(DepenseReconnueType.LOYER_ANNUEL)
                .isNew()
                && (inputCalcul.getDonneesFinancieresRequerant()
                        .getElementDepenseReconnue(DepenseReconnueType.LOYER_ANNUEL).getNbPersonnesLogement()
                        .equals("0"))) {
            message += JadeThread
                    .getMessage("perseus.donneesfinancieres.depenseReconnue.nbPersonnesLogement.mandatory") + "<br />";
        }
        // Si les frais de transport sont saisis, contrôler que les frais de transport acceptés sont remplis
        if (!inputCalcul.getDonneesFinancieresRequerant()
                .getElementDepenseReconnue(DepenseReconnueType.FRAIS_TRANSPORT).isNew()
                && JadeStringUtil.isEmpty(inputCalcul.getDonneesFinancieresRequerant()
                        .getElementDepenseReconnue(DepenseReconnueType.FRAIS_TRANSPORT).getSimpleDonneeFinanciere()
                        .getValeurModifieeTaxateur())) {
            message += JadeThread.getMessage("perseus.donneesfinancieres.depenseReconnue.fraisTransport.check")
                    + "<br />";
        }
        // Si la cession de fortune est remplie, contrôler que la date de cession a été remplie (requérant)
        if (!inputCalcul.getDonneesFinancieresRequerant().getElementFortune(FortuneType.CESSION).isNew()
                && JadeStringUtil.isEmpty(inputCalcul.getDonneesFinancieresRequerant()
                        .getElementFortune(FortuneType.CESSION).getDateCession())) {
            message += JadeThread.getMessage("perseus.donneesfinancieres.fortune.cession.date.mandatory") + "<br />";
        }
        // Si la cession de fortune est remplie, contrôler que la date de cession a été remplie (conjoint)
        if (!inputCalcul.getDonneesFinancieresConjoint().getElementFortune(FortuneType.CESSION).isNew()
                && JadeStringUtil.isEmpty(inputCalcul.getDonneesFinancieresConjoint()
                        .getElementFortune(FortuneType.CESSION).getDateCession())) {
            message += JadeThread.getMessage("perseus.donneesfinancieres.fortune.cession.date.mandatory") + "<br />";
        }

        // Contrôle pour vérifier si case est coché dans le cas d'un montant de rente
        // Si la cession de fortune est remplie, contrôler que la date de cession a été remplie (requérant)
        if (!inputCalcul.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.AUTRES_RENTES).isNew()
                || !inputCalcul.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.TOTAL_RENTES).isNew()) {
            List<String> listeRenteReq = inputCalcul.getDonneesFinancieresRequerant()
                    .getElementRevenu(RevenuType.TOTAL_RENTES).getListCsTypeRentes();
            if (listeRenteReq.isEmpty()) {
                message += JadeThread.getMessage("perseus.donneesfinancieres.revenu.rente.Checkbox.mandatory")
                        + "<br />";
            }

        }

        if (!inputCalcul.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.AUTRES_RENTES).isNew()
                || !inputCalcul.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.TOTAL_RENTES).isNew()) {
            List<String> listeConjointReq = inputCalcul.getDonneesFinancieresConjoint()
                    .getElementRevenu(RevenuType.TOTAL_RENTES).getListCsTypeRentes();
            if (listeConjointReq.isEmpty()) {
                message += JadeThread.getMessage("perseus.donneesfinancieres.revenu.rente.Checkbox.mandatory")
                        + "<br />";
            }

        }
        // Si le nombre de personnes n'est pas renseigné
        int nbPersonneSituationFamiliale = 0;
        if (!JadeStringUtil.isBlankOrZero(inputCalcul.getDemande().getSituationFamiliale().getConjoint().getId())) {
            nbPersonneSituationFamiliale++;
        }
        if (!JadeStringUtil.isBlankOrZero(inputCalcul.getDemande().getSituationFamiliale().getRequerant().getId())) {
            nbPersonneSituationFamiliale++;
        }

        // Recherche des enfants
        EnfantFamilleSearchModel searchModel = new EnfantFamilleSearchModel();
        searchModel.setForIdSituationFamiliale(inputCalcul.getDemande().getSituationFamiliale().getId());
        try {
            searchModel = PerseusImplServiceLocator.getEnfantFamilleService().search(searchModel);
        } catch (SituationFamilleException e1) {
            message += e1.getMessage() + "<br />";
            e1.printStackTrace();
        } catch (JadeApplicationServiceNotAvailableException e1) {
            message += e1.getMessage() + "<br />";
            e1.printStackTrace();
        }

        // Reparcourir la liste pour ajouter les enfants au nombre de personnes dans la situation familiale
        for (JadeAbstractModel abstractModel : searchModel.getSearchResults()) {
            nbPersonneSituationFamiliale++;
        }

        int nbPersonneDonneesFinancieres = 0;
        DepenseReconnueSearchModel depenseReconnueSearchModel = new DepenseReconnueSearchModel();
        depenseReconnueSearchModel.setForIdDemande(inputCalcul.getDemande().getId());

        try {
            depenseReconnueSearchModel = PerseusServiceLocator.getDepenseReconnueService().search(
                    depenseReconnueSearchModel);
        } catch (DonneesFinancieresException e1) {
            message += e1.getMessage() + "<br />";
            e1.printStackTrace();
        } catch (JadeApplicationServiceNotAvailableException e1) {
            message += e1.getMessage() + "<br />";
            e1.printStackTrace();
        }

        JadeAbstractModel abstractModel = depenseReconnueSearchModel.getSearchResults()[0];
        DepenseReconnue depenseReconnue = (DepenseReconnue) abstractModel;
        nbPersonneDonneesFinancieres = Integer.parseInt(depenseReconnue.getNbPersonnesLogement());

        if (nbPersonneSituationFamiliale > nbPersonneDonneesFinancieres) {
            message += JadeThread.getMessage("perseus.situationfamiliale.nbPersonnesLogement") + "<br />";
        }

        // Contrôler que le requérant a bien une adresse et qu'il habite dans le canton de vaud
        try {
            AdresseTiersDetail adresseTiersDetail = PFUserHelper.getAdresseAssure(inputCalcul.getDemande()
                    .getSituationFamiliale().getRequerant().getMembreFamille().getSimpleMembreFamille().getIdTiers(),
                    inputCalcul.getDemande().getSimpleDemande().getDateDebut());

            String idLocalite = adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE_ID);
            PerseusServiceLocator.getLoyerService().searchForLocalite(
                    idLocalite,
                    Integer.parseInt(inputCalcul.getDonneesFinancieresRequerant()
                            .getElementDepenseReconnue(DepenseReconnueType.LOYER_ANNUEL).getNbPersonnesLogement()),
                    inputCalcul.getDemande().getSimpleDemande().getDateDebut());
        } catch (Exception e) {
            message += e.getMessage() + "<br />";
        }

        return message;
    }

    /**
     * Permet de charger les données financières concerant la demande passée en paramètre
     * 
     * @param demande
     * @return InputCalcul Les données financières nécessaires au calcul
     * @throws JadePersistenceException
     * @throws CalculException
     */
    private InputCalcul loadInputData(Demande demande) throws JadePersistenceException, CalculException {
        if (demande == null) {
            throw new CalculException("Unable to load calcul data, demande is null");
        }
        InputCalcul inputCalcul = new InputCalcul(demande);

        try {
            // Chargement des revenus
            RevenuSearchModel revenuSearchModel = new RevenuSearchModel();
            revenuSearchModel.setForIdDemande(demande.getId());
            revenuSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            revenuSearchModel = PerseusServiceLocator.getRevenuService().search(revenuSearchModel);

            for (JadeAbstractModel monRevenu : revenuSearchModel.getSearchResults()) {
                Revenu revenu = (Revenu) monRevenu;
                inputCalcul.addRevenu(revenu);
            }

            // Chargement des dettes
            DetteSearchModel detteSearchModel = new DetteSearchModel();
            detteSearchModel.setForIdDemande(demande.getId());
            detteSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            detteSearchModel = PerseusServiceLocator.getDetteService().search(detteSearchModel);

            for (JadeAbstractModel maDette : detteSearchModel.getSearchResults()) {
                Dette dette = (Dette) maDette;
                inputCalcul.addDette(dette);
            }

            // Chargement de la fortune
            FortuneSearchModel fortuneSearchModel = new FortuneSearchModel();
            fortuneSearchModel.setForIdDemande(demande.getId());
            fortuneSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            fortuneSearchModel = PerseusServiceLocator.getFortuneService().search(fortuneSearchModel);

            for (JadeAbstractModel maFortune : fortuneSearchModel.getSearchResults()) {
                Fortune fortune = (Fortune) maFortune;
                inputCalcul.addFortune(fortune);
            }

            // Chargement des depensesReconnues
            DepenseReconnueSearchModel depenseReconnueSearchModel = new DepenseReconnueSearchModel();
            depenseReconnueSearchModel.setForIdDemande(demande.getId());
            depenseReconnueSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            depenseReconnueSearchModel = PerseusServiceLocator.getDepenseReconnueService().search(
                    depenseReconnueSearchModel);

            for (JadeAbstractModel maDepenseReconnue : depenseReconnueSearchModel.getSearchResults()) {
                DepenseReconnue depenseReconnue = (DepenseReconnue) maDepenseReconnue;
                inputCalcul.addDepenseReconnue(depenseReconnue);
            }

            // Chargement des variables métiers
            VariableMetierSearch variableMetierSearch = new VariableMetierSearch();
            variableMetierSearch.setForDateValable(demande.getSimpleDemande().getDateDebut().substring(3));
            variableMetierSearch = PerseusServiceLocator.getVariableMetierService().search(variableMetierSearch);

            for (JadeAbstractModel maVariableMetier : variableMetierSearch.getSearchResults()) {
                VariableMetier variableMetier = (VariableMetier) maVariableMetier;
                inputCalcul.addVariableMetier(variableMetier);
            }

        } catch (DonneesFinancieresException e) {
            throw new CalculException("DonneesFinancieresException during data loading in calcul : " + e.getMessage());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CalculException("JadeApplicationServiceNotAvailableException during data loading in calcul : "
                    + e.getMessage());
        } catch (VariableMetierException e) {
            throw new CalculException("VariableMetierException during data loading in calcul : " + e.getMessage());
        }

        return inputCalcul;
    }

}
