package ch.globaz.amal.business.calcul;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.constantes.IAMParametresAnnuels;
import ch.globaz.amal.business.exceptions.models.detailFamille.CalculSubsidesException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.exceptions.models.parametreannuel.ParametreAnnuelException;
import ch.globaz.amal.business.exceptions.models.primeavantageuse.PrimeAvantageuseException;
import ch.globaz.amal.business.exceptions.models.primemoyenne.PrimeMoyenneException;
import ch.globaz.amal.business.exceptions.models.subsideannee.SubsideAnneeException;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamilleSearch;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.models.famille.SimpleFamilleSearch;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuel;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuelSearch;
import ch.globaz.amal.business.models.primeavantageuse.SimplePrimeAvantageuse;
import ch.globaz.amal.business.models.primeavantageuse.SimplePrimeAvantageuseSearch;
import ch.globaz.amal.business.models.primemoyenne.SimplePrimeMoyenne;
import ch.globaz.amal.business.models.primemoyenne.SimplePrimeMoyenneSearch;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplex;
import ch.globaz.amal.business.models.revenu.SimpleRevenu;
import ch.globaz.amal.business.models.revenu.SimpleRevenuContribuable;
import ch.globaz.amal.business.models.subsideannee.SimpleSubsideAnnee;
import ch.globaz.amal.business.models.subsideannee.SimpleSubsideAnneeSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.checkers.subsideannee.SimpleSubsideAnneeChecker;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.utils.parametres.ContainerParametres;
import ch.globaz.amal.businessimpl.utils.parametres.ParametresAnnuelsProvider;
import ch.globaz.common.domaine.Montant;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public class CalculsSubsides {
    public static final int REVENU_MAX = 99999;
    private ContainerParametres containerParametres = null;
    private String anneeCalcul;
    private int mtMaxRevenu = 0;
    private int mtSubsideFamille1Personne = 0;
    private int mtSubsideFamille2Personnes = 0;
    private List<ParamsPCFamilleContainer> listSubsideMonoParental = new ArrayList<ParamsPCFamilleContainer>();
    private List<ParamsPCFamilleContainer> listSubsideBiParental = new ArrayList<ParamsPCFamilleContainer>();
    private boolean stillInFamily = true;
    private Montant montantRevenuPrisEnComptePC = Montant.ZERO;

    public CalculsSubsides(String anneeCalcul) throws NumberFormatException, ParametreAnnuelException {
        super();
        this.anneeCalcul = anneeCalcul;
        initParamAnnuels();
        boolean isSubsidePCFamille = SimpleSubsideAnneeChecker.checkIsSubsidePCFKind(anneeCalcul);

        if (!isSubsidePCFamille) {
            mtSubsideFamille1Personne = Integer.parseInt(containerParametres.getParametresAnnuelsProvider()
                    .getListeParametresAnnuels().get(IAMParametresAnnuels.CS_MONTANT_SUBSIDE_FAMILLE_1_PERSONNE)
                    .getFormatedValueByYear(anneeCalcul, null, 0));

            mtSubsideFamille2Personnes = Integer.parseInt(containerParametres.getParametresAnnuelsProvider()
                    .getListeParametresAnnuels().get(IAMParametresAnnuels.CS_MONTANT_SUBSIDE_FAMILLE_2_PERSONNES)
                    .getFormatedValueByYear(anneeCalcul, null, 0));
        }

        mtMaxRevenu = Integer.parseInt(containerParametres.getParametresAnnuelsProvider().getListeParametresAnnuels()
                .get(IAMParametresAnnuels.CS_MONTANT_MAX_REVENU_MODESTE).getFormatedValueByYear(anneeCalcul, null, 0));
    }

    private int calculAgeMembre(SimpleDetailFamille simpleDetailFamille, SimpleFamille simpleFamille)
            throws CalculSubsidesException {
        int ageMembre = 0;
        try {
            ageMembre = Integer.valueOf(simpleDetailFamille.getAnneeHistorique())
                    - Integer.valueOf(JACalendar.getYear(simpleFamille.getDateNaissance()));
        } catch (Exception e) {
            throw new CalculSubsidesException("Error while searching age of family member!");
        }
        return ageMembre;
    }

    /**
     * @param revenuHistoriqueComplex
     * @param simpleDetailFamille
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws CalculSubsidesException
     * @throws DetailFamilleException
     * @throws ParametreAnnuelException
     */
    public AmalSubsideContainer calculSubside(RevenuHistoriqueComplex revenuHistoriqueComplex,
            SimpleDetailFamille simpleDetailFamille, String typeDemande)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, CalculSubsidesException,
            DetailFamilleException {
        return this.calculSubside(revenuHistoriqueComplex, simpleDetailFamille, typeDemande, false);

    }

    /**
     * Cette méthode va calculer le montant des subsides pour chaque membre de la famille
     *
     * @param simpleRevenu
     *            le SimpleRevenu
     * @param simpleDetailFamille
     *            le SimpleDetailFamille
     * @param revenuRDU
     *            true si on veut utiliser le RDU
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws CalculSubsidesException
     * @throws DetailFamilleException
     * @throws ParametreAnnuelException
     */
    public AmalSubsideContainer calculSubside(RevenuHistoriqueComplex revenuHistoriqueComplex,
            SimpleDetailFamille simpleDetailFamille, String typeDemande, boolean revenuRDU)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, CalculSubsidesException,
            DetailFamilleException {
        // Prepare working variables
        AmalSubsideContainer subsideContainer = new AmalSubsideContainer();
        SimpleRevenu simpleRevenu = new SimpleRevenu();
        String idRevenu = null;
        if (revenuHistoriqueComplex != null) {
            simpleRevenu = revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu();
            idRevenu = revenuHistoriqueComplex.getId();
        }
        // Set value of common fields >> usefull to play again the simulation
        subsideContainer.setRevenuPriseEnCompteIsRDU(revenuRDU);
        subsideContainer.setAnneeSubside(simpleDetailFamille.getAnneeHistorique());
        subsideContainer.setIdRevenuPrisEnCompte(idRevenu);
        subsideContainer.setSelectedRevenu(revenuHistoriqueComplex);

        boolean revenuExist = true;
        if (simpleRevenu.isNew()) {
            revenuExist = false;
        }

        String revenuPrisEnCompte = "0";
        if (revenuExist) {
            revenuPrisEnCompte = revenuHistoriqueComplex.getSimpleRevenuDeterminant().getRevenuDeterminantCalcul();
            if (revenuRDU) {
                revenuPrisEnCompte = simpleRevenu.getRevDetUnique();
            }
        }
        montantRevenuPrisEnComptePC = new Montant(revenuPrisEnCompte);
        if (containerParametres == null) {
            initParamAnnuels();
        }

        Double montantTotalSubsides = 0.0;
        Double montantTotalSubsidesAssistes = 0.0;

        try {
            SimpleFamilleSearch simpleFamilleSearch = new SimpleFamilleSearch();
            simpleFamilleSearch.setForIdContribuable(simpleDetailFamille.getIdContribuable());
            simpleFamilleSearch = AmalServiceLocator.getFamilleContribuableService().search(simpleFamilleSearch);

            for (int membreIdx = 0; membreIdx < simpleFamilleSearch.getSize(); membreIdx++) {
                boolean hasDroit = false;
                SimpleFamille simpleFamille = (SimpleFamille) simpleFamilleSearch.getSearchResults()[membreIdx];
                SimpleDetailFamille simpleDetailFamilleMembre = new SimpleDetailFamille();
                simpleDetailFamilleMembre.setAnneeHistorique(simpleDetailFamille.getAnneeHistorique());
                simpleDetailFamilleMembre.setIdContribuable(simpleFamille.getIdContribuable());
                simpleDetailFamilleMembre.setIdFamille(simpleFamille.getIdFamille());
                try {
                    searchDebutAndFinDroit(simpleDetailFamille.getAnneeHistorique(), simpleFamille.getIdFamille(),
                            simpleDetailFamilleMembre);
                } catch (JAException e1) {
                    simpleDetailFamilleMembre.setDebutDroit("");
                    simpleDetailFamilleMembre.setFinDroit("");
                }

                int ageMembre = calculAgeMembre(simpleDetailFamille, simpleFamille);

                stillInFamily = inFamilyInYear(simpleFamille, simpleDetailFamille.getAnneeHistorique());

                if (stillInFamily) {
                    hasDroit = setMontantsSubsidesStandard(simpleDetailFamille, revenuExist, revenuPrisEnCompte,
                            simpleFamille, simpleDetailFamilleMembre, ageMembre);

                    // On ajoute un supplément pour les adultes, toujours présents, si le rev. det est inférieur à
                    // mtMaxRevenu (réduction pour familles modestes)
                    int supplementContribution = 0;
                    // Check année d'activation pour le nouvelle calcul des supplements PCF.
                    boolean isSubsidePCFamille = SimpleSubsideAnneeChecker
                            .checkIsSubsidePCFKind(simpleDetailFamilleMembre);
                    if (revenuExist && !isSubsidePCFamille) {
                        supplementContribution = calculSupplementContribution(simpleDetailFamille,
                                simpleDetailFamilleMembre.getDebutDroit(), simpleDetailFamilleMembre.getFinDroit());
                        setMontantSupplementFamilleModeste(simpleDetailFamille, simpleRevenu, revenuPrisEnCompte,
                                supplementContribution, simpleFamille, simpleDetailFamilleMembre);
                    } else if (isSubsidePCFamille && hasRightPCFamille(revenuHistoriqueComplex)) {
                        supplementContribution = calculSupplementContributionPCFamille(simpleDetailFamille,
                                simpleDetailFamilleMembre.getDebutDroit(), simpleDetailFamilleMembre.getFinDroit());
                        setMontantSupplementFamilleModeste(simpleDetailFamille, simpleRevenu, revenuPrisEnCompte,
                                supplementContribution, simpleFamille, simpleDetailFamilleMembre);
                    }

                    hasDroit = setMontantAssiste(simpleDetailFamille, simpleFamille, simpleDetailFamilleMembre,
                            ageMembre, typeDemande);

                    if (hasDroit) {
                        try {
                            if (!JadeStringUtil.isBlankOrZero(simpleDetailFamilleMembre.getMontantContribution())
                                    && simpleDetailFamilleMembre.getCodeActif()) {
                                montantTotalSubsides = montantTotalSubsides
                                        + Double.valueOf(simpleDetailFamilleMembre.getMontantContribution());
                            }

                            if (!JadeStringUtil.isBlankOrZero(simpleDetailFamilleMembre.getMontantContributionAssiste())
                                    && simpleDetailFamilleMembre.getCodeActif()) {
                                montantTotalSubsidesAssistes = montantTotalSubsidesAssistes
                                        + Double.valueOf(simpleDetailFamilleMembre.getMontantContributionAssiste());
                            }

                            if (!JadeStringUtil.isBlankOrZero(simpleDetailFamilleMembre.getMontantContributionPC())
                                    && simpleDetailFamilleMembre.getCodeActif()) {
                                montantTotalSubsidesAssistes = montantTotalSubsidesAssistes
                                        + Double.valueOf(simpleDetailFamilleMembre.getMontantContributionPC());
                            }
                        } catch (Exception e) {
                            JadeLogger.warn(this,
                                    "Problem parsing " + simpleDetailFamilleMembre.getMontantContribution()
                                            + " to Double ! --> " + e.toString());
                        }
                        String designation = simpleFamille.getNomPrenom();

                        if (JadeStringUtil.isEmpty(designation)) {
                            designation = "Membre #" + simpleFamille.getIdFamille();
                        }

                        if (simpleFamille.getIsContribuable()) {
                            subsideContainer.setContribuableName(simpleFamille.getNomPrenom());
                        }
                        subsideContainer.setMontantSupplementContribution(String.valueOf(supplementContribution));
                        subsideContainer.setMontantContributionsTotal(String.valueOf(montantTotalSubsides));
                        subsideContainer
                                .setMontantContributionPCAssisteTotal(String.valueOf(montantTotalSubsidesAssistes));

                        subsideContainer.getMapSimpleDetailFamille().put(designation, simpleDetailFamilleMembre);

                    }// Fin IF HAS DROIT
                }// Fin IF stillInFamily (=CODEFIN == VIDE ET DATE SORTIE == VIDE)
            }// Fin boucle membre famille

            subsideContainer.setRevenuPrisEnCompte(revenuPrisEnCompte);
        } catch (FamilleException fe2) {
            throw new CalculSubsidesException("Error while searching simpleFamilleSearch with idContribuable :"
                    + simpleDetailFamille.getIdContribuable() + " ==>" + fe2.getMessage());
        }
        return subsideContainer;
    }

    private boolean hasRightPCFamille(RevenuHistoriqueComplex revenuHistoriqueComplex) {
        if (revenuHistoriqueComplex != null && revenuHistoriqueComplex.getRevenuFullComplex() != null
                && revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu() != null) {
            SimpleRevenuContribuable revenu = revenuHistoriqueComplex.getRevenuFullComplex()
                    .getSimpleRevenuContribuable();
            if (revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().getTypeRevenu()
                    .equals(IAMCodeSysteme.CS_TYPE_SOURCIER)) {
                return revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().getRevDetUnique() != null
                        && !JadeStringUtil.isBlankOrZero(
                                revenuHistoriqueComplex.getSimpleRevenuDeterminant().getRevenuDeterminantCalcul());
            } else {
                return !JadeStringUtil.isBlankOrZero(revenu.getRevenuNetEmploi())
                        || !JadeStringUtil.isBlankOrZero(revenu.getRevenuNetEpouse())
                        || !JadeStringUtil.isBlankOrZero(revenu.getRevenuActIndep())
                        || !JadeStringUtil.isBlankOrZero(revenu.getRevenuActIndepEpouse())
                        || !JadeStringUtil.isBlankOrZero(revenu.getRevenuActAgricole())
                        || !JadeStringUtil.isBlankOrZero(revenu.getRevenuActAgricoleEpouse());
            }
        } else {
            return false;
        }
    }

    private int calculSupplementContribution(SimpleDetailFamille simpleDetailFamille, String debutDroit,
            String finDroit)
            throws JadePersistenceException, JadeApplicationServiceNotAvailableException, CalculSubsidesException {
        int supplementContribution = 0;
        SimpleFamilleSearch nbParentsSearch = new SimpleFamilleSearch();
        nbParentsSearch.setWhereKey("calculSupplementContribution");
        nbParentsSearch.setForIdContribuable(simpleDetailFamille.getIdContribuable());
        nbParentsSearch.setForDifferentPereMereEnfant(IAMCodeSysteme.CS_TYPE_ENFANT);
        if (JadeStringUtil.isBlankOrZero(finDroit)) {
            nbParentsSearch.setForFinDefinitiveGOE("12." + simpleDetailFamille.getAnneeHistorique());
        } else {
            nbParentsSearch.setForFinDefinitiveGOE(finDroit);
        }
        nbParentsSearch.setForFinDefinitive("0");
        try {
            int nbParents = AmalServiceLocator.getFamilleContribuableService().count(nbParentsSearch);
            if (nbParents == 1) {
                supplementContribution = mtSubsideFamille1Personne;
            } else if (nbParents >= 2) {
                supplementContribution = mtSubsideFamille2Personnes;
            }
        } catch (FamilleException fe) {
            throw new CalculSubsidesException("Error while counting nbParentsSearch with idContribuable : "
                    + simpleDetailFamille.getIdContribuable() + " ==> " + fe.getMessage());
        }
        return supplementContribution;
    }

    // Nouveau calcul depuis 01.01.2019 => la date est prise depuis Jadeprop
    private int calculSupplementContributionPCFamille(SimpleDetailFamille simpleDetailFamille, String debutDroit,
            String finDroit)
            throws JadePersistenceException, JadeApplicationServiceNotAvailableException, CalculSubsidesException {
        int supplementContribution = 0;
        SimpleFamilleSearch nbParentsSearch = new SimpleFamilleSearch();
        nbParentsSearch.setWhereKey("calculSupplementContribution");
        nbParentsSearch.setForIdContribuable(simpleDetailFamille.getIdContribuable());
        nbParentsSearch.setForDifferentPereMereEnfant(IAMCodeSysteme.CS_TYPE_ENFANT);
        if (JadeStringUtil.isBlankOrZero(finDroit)) {
            nbParentsSearch.setForFinDefinitiveGOE("12." + simpleDetailFamille.getAnneeHistorique());
        } else {
            nbParentsSearch.setForFinDefinitiveGOE(finDroit);
        }
        nbParentsSearch.setForFinDefinitive("0");
        try {
            int nbParents = AmalServiceLocator.getFamilleContribuableService().count(nbParentsSearch);
            if (nbParents == 1) {
                supplementContribution = getSupplementPC(listSubsideMonoParental);
            } else if (nbParents >= 2) {
                supplementContribution = getSupplementPC(listSubsideBiParental);
            }
        } catch (FamilleException fe) {
            throw new CalculSubsidesException("Error while counting nbParentsSearch with idContribuable : "
                    + simpleDetailFamille.getIdContribuable() + " ==> " + fe.getMessage());
        }
        return supplementContribution;
    }

    private int getSupplementPC(List<ParamsPCFamilleContainer> listSubsideParental) {
        int montantSupplement = 0;
        for (ParamsPCFamilleContainer element : listSubsideParental) {
            if (montantRevenuPrisEnComptePC.greaterOrEquals(element.getMontantFrom())
                    && montantRevenuPrisEnComptePC.getValueDouble() <= element.getMontantTo().getValueDouble()) {
                montantSupplement = element.getValeurNumerique().getMontantSansCentimes();
            }
        }
        return montantSupplement;
    }

    private int countNbEnfantsCharge(String idContribuable, String anneeHistorique, String debutDroit, String finDroit)
            throws FamilleException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            DetailFamilleException {

        int currentAnneeHistorique = Integer.valueOf(anneeHistorique);
        int year25 = currentAnneeHistorique - 25;
        String dateNaissanceMin = "01.01." + year25;

        SimpleFamilleSearch simpleFamilleSearch = new SimpleFamilleSearch();
        simpleFamilleSearch.setForIdContribuable(idContribuable);
        simpleFamilleSearch.setForPereMereEnfant(IAMCodeSysteme.CS_TYPE_ENFANT);
        if (JadeStringUtil.isBlankOrZero(finDroit)) {
            simpleFamilleSearch.setForFinDefinitiveGOE("12." + anneeHistorique);
        } else {
            simpleFamilleSearch.setForFinDefinitiveGOE(finDroit);
        }
        simpleFamilleSearch.setForFinDefinitive("0");
        simpleFamilleSearch.setForDateNaissanceGOE(dateNaissanceMin);
        simpleFamilleSearch.setWhereKey("subsides");
        return AmalImplServiceLocator.getSimpleFamilleService().count(simpleFamilleSearch);
    }

    private SimpleSubsideAnnee findMontantsSubside(SimpleDetailFamille simpleDetailFamille, String revenuPrisEnCompte)
            throws SubsideAnneeException {
        SimpleSubsideAnneeSearch simpleSubsideAnneeSearch = new SimpleSubsideAnneeSearch();
        simpleSubsideAnneeSearch.setForAnneeSubside(simpleDetailFamille.getAnneeHistorique());

        if (Integer.parseInt(
                JANumberFormatter.fmt(revenuPrisEnCompte, false, false, false, 0)) > CalculsSubsides.REVENU_MAX) {
            simpleSubsideAnneeSearch.setForLimiteRevenuGOE(String.valueOf(CalculsSubsides.REVENU_MAX));
        } else {
            simpleSubsideAnneeSearch.setForLimiteRevenuGOE(revenuPrisEnCompte);
        }

        SimpleSubsideAnnee simpleSubsideAnnee = new SimpleSubsideAnnee();
        try {
            simpleSubsideAnneeSearch = AmalServiceLocator.getSimpleSubsideAnneeService()
                    .search(simpleSubsideAnneeSearch);
            simpleSubsideAnnee = (SimpleSubsideAnnee) simpleSubsideAnneeSearch.getSearchResults()[0];
        } catch (Exception e) {
            throw new SubsideAnneeException(
                    "Error while searching SubsideAnnee with year " + simpleDetailFamille.getAnneeHistorique()
                            + " and revenu " + revenuPrisEnCompte + " ! (idSimpleDetailFamille : "
                            + simpleDetailFamille.getIdDetailFamille() + ") ==> " + e.getMessage());
        }

        return simpleSubsideAnnee;
    }

    private boolean inFamilyInYear(SimpleFamille simpleFamille, String anneeHistorique) {
        if (JAUtil.isDateEmpty(simpleFamille.getFinDefinitive())
                && JadeStringUtil.isBlankOrZero(simpleFamille.getCodeTraitementDossier())) {
            return true;
        } else {
            try {
                Integer i_annee = Integer.parseInt(simpleFamille.getFinDefinitive().substring(3));

                if (Integer.parseInt(anneeHistorique) <= i_annee) {
                    return true;
                }
            } catch (Exception e) {
                // Si on a une erreur ici, c'est qu'on a pas de date de fin au membre, donc il était encore dans la
                // famille
                return true;
            }
        }

        return false;
    }

    private void initParamAnnuels() {
        try {
            containerParametres = new ContainerParametres();
            SimpleParametreAnnuelSearch simpleParametreAnnuelSearch = new SimpleParametreAnnuelSearch();
            simpleParametreAnnuelSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            simpleParametreAnnuelSearch = AmalServiceLocator.getParametreAnnuelService()
                    .search(simpleParametreAnnuelSearch);
            containerParametres
                    .setParametresAnnuelsProvider(new ParametresAnnuelsProvider(simpleParametreAnnuelSearch));

            initParamPCFamille(simpleParametreAnnuelSearch);
        } catch (Exception e) {
            JadeLogger.error(this, "Error loading parametre annuels --> " + e.getMessage());
        }
    }

    private void initParamPCFamille(SimpleParametreAnnuelSearch parametresAnnuelSearch) {
        for (globaz.jade.persistence.model.JadeAbstractModel absDonnee : parametresAnnuelSearch.getSearchResults()) {
            SimpleParametreAnnuel simpleParametreAnnuel = (SimpleParametreAnnuel) absDonnee;
            if (simpleParametreAnnuel.getAnneeParametre().equals(anneeCalcul)) {
                if (simpleParametreAnnuel.getCodeTypeParametre()
                        .equals(IAMParametresAnnuels.CS_MONTANT_SUBSIDE_MONOPARENTALE)) {
                    listSubsideMonoParental.add(getPCContainer(simpleParametreAnnuel));
                } else if (simpleParametreAnnuel.getCodeTypeParametre()
                        .equals(IAMParametresAnnuels.CS_MONTANT_SUBSIDE_BIPARENTAL)) {
                    listSubsideBiParental.add(getPCContainer(simpleParametreAnnuel));
                }
            }
        }

    }

    private ParamsPCFamilleContainer getPCContainer(SimpleParametreAnnuel parametreAnnuel) {
        ParamsPCFamilleContainer container = new ParamsPCFamilleContainer();
        if (parametreAnnuel.getValeurFrom() != null) {
            container.setMontantFrom(new Montant(parametreAnnuel.getValeurFrom()));
        }
        if (parametreAnnuel.getValeurTo() != null) {
            container.setMontantTo(new Montant(parametreAnnuel.getValeurTo()));
        }
        container.setParamYear(parametreAnnuel.getAnneeParametre());
        container.setTypeSubside(parametreAnnuel.getCodeTypeParametre());
        if (parametreAnnuel.getValeurParametre() != null) {
            container.setValeurNumerique(new Montant(parametreAnnuel.getValeurParametre()));
        }
        if (parametreAnnuel.getValeurParametreString() != null) {
            container.setValeurString(parametreAnnuel.getValeurParametreString());
        }
        return container;
    }

    private void searchDebutAndFinDroit(String anneeHistorique, String idFamille,
            SimpleDetailFamille simpleDetailFamilleMembre) throws JAException, CalculSubsidesException {

        // ---------------------------------------------------------------------------------
        // 1) Recherche des subsides existants pour l'annee historique
        // ---------------------------------------------------------------------------------
        String debutDroitDefault = "01." + anneeHistorique;
        int nbSubsideExistant = 0;
        SimpleDetailFamilleSearch simpleDetailFamilleExistantSearch = null;
        simpleDetailFamilleExistantSearch = new SimpleDetailFamilleSearch();
        simpleDetailFamilleExistantSearch.setForAnneeHistorique(anneeHistorique);
        simpleDetailFamilleExistantSearch.setForIdFamille(idFamille);
        simpleDetailFamilleExistantSearch.setForCodeActif(true);
        try {
            simpleDetailFamilleExistantSearch = AmalServiceLocator.getDetailFamilleService()
                    .search(simpleDetailFamilleExistantSearch);
            nbSubsideExistant = simpleDetailFamilleExistantSearch.getSize();
        } catch (Exception e) {
            JadeLogger.error(this, "Error loading detailFamille ==> idFamille : " + idFamille + " / Year : "
                    + anneeHistorique + " -- " + e.getMessage());
        }
        if (nbSubsideExistant == 0) {
            // ---------------------------------------------------------------------------------
            // 2) Pas de subside .getSize(0) >> début du nouveau droit 01.anneeHistorique
            // ---------------------------------------------------------------------------------
            simpleDetailFamilleMembre.setNoAssure("");
            simpleDetailFamilleMembre.setNoCaisseMaladie("");
            simpleDetailFamilleMembre.setDebutDroit(debutDroitDefault);
        } else if (nbSubsideExistant == 1) {
            // ---------------------------------------------------------------------------------
            // 3) Un subside .getSize(1)
            // subsideTrouve.dateFin==0 ou 12.xxxx >> début du nouveau droit à subsideTrouve.debutDroit
            // subsideTrouve.dateFin!=0 >> début du nouveau droit à subsideTrouve.dateFin+1
            // ---------------------------------------------------------------------------------
            SimpleDetailFamille detailFamilleSearched = (SimpleDetailFamille) simpleDetailFamilleExistantSearch
                    .getSearchResults()[0];
            simpleDetailFamilleMembre.setNoAssure(detailFamilleSearched.getNoAssure());
            simpleDetailFamilleMembre.setNoCaisseMaladie(detailFamilleSearched.getNoCaisseMaladie());
            if (JadeStringUtil.isBlankOrZero(detailFamilleSearched.getFinDroit())) {
                simpleDetailFamilleMembre.setDebutDroit(detailFamilleSearched.getDebutDroit());
            } else {
                if (JadeDateUtil.isGlobazDateMonthYear(detailFamilleSearched.getFinDroit())) {
                    String currentEnd = detailFamilleSearched.getFinDroit();
                    if (currentEnd.equals("12." + anneeHistorique)) {
                        simpleDetailFamilleMembre.setDebutDroit(detailFamilleSearched.getDebutDroit());
                    } else {
                        BigDecimal endMonth = JADate.getMonth(currentEnd);
                        // incrémente la date de début
                        String currentStart = ""
                                + (JadeStringUtil.fillWithZeroes(String.valueOf(endMonth.intValue() + 1), 2)) + "."
                                + anneeHistorique;
                        simpleDetailFamilleMembre.setDebutDroit(currentStart);
                    }
                } else {
                    simpleDetailFamilleMembre.setDebutDroit(detailFamilleSearched.getDebutDroit());
                }
            }
            if (!JadeStringUtil.isEmpty(detailFamilleSearched.getDateRecepDemande())) {
                simpleDetailFamilleMembre.setDateRecepDemande(detailFamilleSearched.getDateRecepDemande());
            }
        } else if (nbSubsideExistant > 1) {
            // ---------------------------------------------------------------------------------
            // 4) Plusieurs subsides .getSize(>1)
            // 1 sans dateFin >> début du nouveau droit à subsideTrouve.debutDroit
            // x sans dateFin >> début du nouveau droit à 01.anneeHistorique
            // ---------------------------------------------------------------------------------
            List<SimpleDetailFamille> subsidesWithFinDroit = new ArrayList<SimpleDetailFamille>();
            List<SimpleDetailFamille> subsidesWithoutFinDroit = new ArrayList<SimpleDetailFamille>();
            for (int iSubside = 0; iSubside < simpleDetailFamilleExistantSearch.getSize(); iSubside++) {
                SimpleDetailFamille detailFamilleSearched = (SimpleDetailFamille) simpleDetailFamilleExistantSearch
                        .getSearchResults()[iSubside];
                if (JadeStringUtil.isBlankOrZero(detailFamilleSearched.getFinDroit())) {
                    subsidesWithoutFinDroit.add(detailFamilleSearched);
                } else {
                    subsidesWithFinDroit.add(detailFamilleSearched);
                }
            }
            if (subsidesWithoutFinDroit.size() == 1) {
                SimpleDetailFamille detailFamilleSearched = subsidesWithoutFinDroit.get(0);
                String currentEnd = detailFamilleSearched.getFinDroit();
                BigDecimal endMonth = JADate.getMonth(currentEnd);
                // incrémente la date de début
                String currentStart = "" + (JadeStringUtil.fillWithZeroes(String.valueOf(endMonth.intValue() + 1), 2))
                        + "." + anneeHistorique;
                simpleDetailFamilleMembre.setDebutDroit(currentStart);
                simpleDetailFamilleMembre.setNoAssure(detailFamilleSearched.getNoAssure());
                simpleDetailFamilleMembre.setNoCaisseMaladie(detailFamilleSearched.getNoCaisseMaladie());
                if (!JadeStringUtil.isEmpty(detailFamilleSearched.getDateRecepDemande())) {
                    simpleDetailFamilleMembre.setDateRecepDemande(detailFamilleSearched.getDateRecepDemande());
                }
            } else {
                simpleDetailFamilleMembre.setNoAssure("");
                simpleDetailFamilleMembre.setNoCaisseMaladie("");
                simpleDetailFamilleMembre.setDebutDroit(debutDroitDefault);
            }
        }
        simpleDetailFamilleMembre.setCodeActif(true);
        // ---------------------------------------------------------------------------------
        // 5) Informations complémentaires si nécessaire
        // Rechercher le dernier subside ou le noCama et noAssure étaientt renseignés
        // ---------------------------------------------------------------------------------
        if (JadeStringUtil.isEmpty(simpleDetailFamilleMembre.getNoCaisseMaladie())
                || JadeStringUtil.isEmpty(simpleDetailFamilleMembre.getNoAssure())) {
            // Recherche de tous les subsides du membre de famille courant
            SimpleDetailFamilleSearch simpleDetailFamilleSearch = null;
            simpleDetailFamilleSearch = new SimpleDetailFamilleSearch();
            simpleDetailFamilleSearch.setForIdFamille(idFamille);
            simpleDetailFamilleSearch.setForCodeActif(true);
            try {
                simpleDetailFamilleSearch = AmalServiceLocator.getDetailFamilleService()
                        .search(simpleDetailFamilleSearch);
            } catch (Exception e) {
                JadeLogger.error(this, "Error loading detailFamille, complete list ==> idFamille : " + idFamille
                        + " / Year : " + anneeHistorique + " -- " + e.getMessage());
            }
            if (simpleDetailFamilleSearch.getSize() > 0) {
                // enregistrement dans une map, par date
                HashMap<Long, SimpleDetailFamille> allSubsides = new HashMap<Long, SimpleDetailFamille>();
                for (int iSubside = 0; iSubside < simpleDetailFamilleSearch.getSize(); iSubside++) {
                    SimpleDetailFamille currentSubside = (SimpleDetailFamille) simpleDetailFamilleSearch
                            .getSearchResults()[iSubside];
                    if (!JadeStringUtil.isBlankOrZero(currentSubside.getNoCaisseMaladie())
                            && !JadeStringUtil.isBlankOrZero(currentSubside.getNoAssure())
                            && !JadeStringUtil.isBlankOrZero(currentSubside.getDebutDroit())) {
                        Long dateStart = JadeDateUtil.getGlobazDate("01." + currentSubside.getDebutDroit()).getTime();
                        allSubsides.put(dateStart, currentSubside);
                    }
                }
                if (allSubsides.size() > 0) {
                    // Tri par date, plus ancienne au début
                    List<Long> allKeyDates = new ArrayList<Long>(allSubsides.keySet());
                    Collections.sort(allKeyDates);
                    Collections.reverse(allKeyDates);
                    SimpleDetailFamille lastFilledSubside = allSubsides.get(allKeyDates.get(0));
                    // récupération de la caisse maladie et no assuré
                    simpleDetailFamilleMembre.setNoAssure(lastFilledSubside.getNoAssure());
                    simpleDetailFamilleMembre.setNoCaisseMaladie(lastFilledSubside.getNoCaisseMaladie());
                }
            }

        }

    }

    private boolean setMontantAssiste(SimpleDetailFamille simpleDetailFamille, SimpleFamille simpleFamille,
            SimpleDetailFamille simpleDetailFamilleMembre, int ageMembre, String typeDemande)
            throws JadePersistenceException, JadeApplicationServiceNotAvailableException, FamilleException {
        try {
            // Pour les Assistés dès 2015, on va récupérer les primes avantageuses pour les assistés et les primes
            // moyennes pour les PC
            SimplePrimeAvantageuseSearch simplePrimeAvantageuseSearch = new SimplePrimeAvantageuseSearch();
            simplePrimeAvantageuseSearch.setForAnneeSubside(simpleDetailFamille.getAnneeHistorique());
            simplePrimeAvantageuseSearch = AmalServiceLocator.getSimplePrimeAvantageuseService()
                    .search(simplePrimeAvantageuseSearch);
            SimplePrimeAvantageuse simplePrimeAvantageuse = null;

            // Récupération des primes avantageuses
            if (simplePrimeAvantageuseSearch.getSearchResults().length > 0) {
                simplePrimeAvantageuse = (SimplePrimeAvantageuse) simplePrimeAvantageuseSearch.getSearchResults()[0];
            }

            // Récupération des primes moyennes
            SimplePrimeMoyenneSearch simplePrimeMoyenneSearch = new SimplePrimeMoyenneSearch();
            simplePrimeMoyenneSearch.setForAnneeSubside(simpleDetailFamille.getAnneeHistorique());
            simplePrimeMoyenneSearch = AmalServiceLocator.getSimplePrimeMoyenneService()
                    .search(simplePrimeMoyenneSearch);
            SimplePrimeMoyenne simplePrimeMoyenne = null;
            if (simplePrimeMoyenneSearch.getSearchResults().length > 0) {
                simplePrimeMoyenne = (SimplePrimeMoyenne) simplePrimeMoyenneSearch.getSearchResults()[0];
            }

            boolean isAssiste = IAMCodeSysteme.AMTypeDemandeSubside.ASSISTE.getValue().equals(typeDemande);
            int anneHistorique = Integer.parseInt(simpleDetailFamille.getAnneeHistorique());
            boolean isYear2015 = anneHistorique == 2015;
            boolean isYearDes2016 = anneHistorique >= 2016 && anneHistorique < 2018;
            boolean isYearDes2018 = anneHistorique >= 2018;
            boolean isPC = IAMCodeSysteme.AMTypeDemandeSubside.PC.getValue().equals(typeDemande);

            String montantA = null;
            String montantP = null;
            if ((ageMembre >= 0) && (ageMembre <= 18)) {
                if (isYear2015) {
                    montantA = simplePrimeAvantageuse.getMontantPrimeEnfant();
                    montantP = simplePrimeMoyenne.getMontantPrimeEnfant();
                } else if (isYearDes2016) {
                    montantA = simplePrimeAvantageuse.getMontantPrimeEnfant();
                    montantP = simplePrimeAvantageuse.getMontantPrimeEnfant();
                } else if (isYearDes2018) {
                    montantA = simplePrimeAvantageuse.getMontantPrimeEnfant();
                    montantP = simplePrimeMoyenne.getMontantPrimeEnfant();
                } else {
                    montantA = simplePrimeMoyenne.getMontantPrimeEnfant();
                }
            } else if ((ageMembre > 18) && (ageMembre <= 25)) {
                if (isYear2015) {
                    montantA = simplePrimeAvantageuse.getMontantPrimeFormation();
                    montantP = simplePrimeMoyenne.getMontantPrimeFormation();
                } else if (isYearDes2016) {
                    montantA = simplePrimeAvantageuse.getMontantPrimeFormation();
                    montantP = simplePrimeAvantageuse.getMontantPrimeFormation();
                } else if (isYearDes2018) {
                    montantA = simplePrimeAvantageuse.getMontantPrimeFormation();
                    montantP = simplePrimeMoyenne.getMontantPrimeFormation();
                } else {
                    montantA = simplePrimeMoyenne.getMontantPrimeFormation();
                }
            } else {
                if (!IAMCodeSysteme.CS_TYPE_ENFANT.equals(simpleFamille.getPereMereEnfant()) && stillInFamily) {
                    if (isYear2015) {
                        montantA = simplePrimeAvantageuse.getMontantPrimeAdulte();
                        montantP = simplePrimeMoyenne.getMontantPrimeAdulte();
                    } else if (isYearDes2016) {
                        montantA = simplePrimeAvantageuse.getMontantPrimeAdulte();
                        montantP = simplePrimeAvantageuse.getMontantPrimeAdulte();
                    } else if (isYearDes2018) {
                        montantA = simplePrimeAvantageuse.getMontantPrimeAdulte();
                        montantP = simplePrimeMoyenne.getMontantPrimeAdulte();
                    } else {
                        montantA = simplePrimeMoyenne.getMontantPrimeAdulte();
                    }
                }
            }

            simpleDetailFamilleMembre.setMontantContributionAssiste(montantA);
            simpleDetailFamilleMembre.setMontantContributionPC(montantP);
            if (montantA != null || montantP != null) {
                return true;
            }
        } catch (PrimeMoyenneException e) {
            throw new FamilleException("Error while searching prime moyenne --> " + e.getMessage());
        } catch (PrimeAvantageuseException e) {
            throw new FamilleException("Error while searching prime avantageuse --> " + e.getMessage());
        }
        return false;
    }

    private boolean setMontantsSubsidesStandard(SimpleDetailFamille simpleDetailFamille, boolean revenuExist,
            String revenuPrisEnCompte, SimpleFamille simpleFamille, SimpleDetailFamille simpleDetailFamilleMembre,
            int ageMembre) throws CalculSubsidesException {
        if (revenuExist) {
            try {
                SimpleSubsideAnnee montantsSubsideAnnee = findMontantsSubside(simpleDetailFamille, revenuPrisEnCompte);

                if (IAMCodeSysteme.CS_TYPE_ENFANT.equals(simpleFamille.getPereMereEnfant())) {
                    if ((ageMembre >= 0) && (ageMembre <= 18)) {
                        simpleDetailFamilleMembre.setMontantContribution(montantsSubsideAnnee.getSubsideEnfant());
                        return true;
                    }
                    if ((ageMembre > 18) && (ageMembre <= 25)) {
                        simpleDetailFamilleMembre.setMontantContribution(montantsSubsideAnnee.getSubsideAdo());
                        return true;
                    }
                } else {
                    if ((ageMembre >= 16) && (ageMembre < 19)) {
                        simpleDetailFamilleMembre.setMontantContribution(montantsSubsideAnnee.getSubsideSalarie1618());
                        return true;
                    } else if ((ageMembre >= 19) && (ageMembre <= 25)) {
                        simpleDetailFamilleMembre.setMontantContribution(montantsSubsideAnnee.getSubsideSalarie1925());
                        return true;
                    } else {
                        simpleDetailFamilleMembre.setMontantContribution(montantsSubsideAnnee.getSubsideAdulte());
                        return true;
                    }
                }
            } catch (SubsideAnneeException e) {
                throw new CalculSubsidesException(e.getMessage());
            }
        } else {
            simpleDetailFamilleMembre.setMontantContribution("0.00");
            return false;
        }
        return false;
    }

    private void setMontantSupplementFamilleModeste(SimpleDetailFamille simpleDetailFamille, SimpleRevenu simpleRevenu,
            String revenuPrisEnCompte, int supplementContribution, SimpleFamille simpleFamille,
            SimpleDetailFamille simpleDetailFamilleMembre) throws FamilleException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DetailFamilleException {

        if ((Integer.parseInt(JANumberFormatter.fmt(revenuPrisEnCompte, false, false, false, 0)) < mtMaxRevenu)
                && !IAMCodeSysteme.CS_TYPE_ENFANT.equals(simpleFamille.getPereMereEnfant())
                && (!JadeStringUtil.isBlankOrZero(simpleDetailFamille.getAnneeHistorique())
                        && (Integer.valueOf(simpleDetailFamille.getAnneeHistorique()) >= 2009))) {

            int nbEnfantsACharge = countNbEnfantsCharge(simpleFamille.getIdContribuable(),
                    simpleDetailFamille.getAnneeHistorique(), simpleDetailFamilleMembre.getDebutDroit(),
                    simpleDetailFamilleMembre.getFinDroit());

            if (nbEnfantsACharge > 0) {
                simpleDetailFamilleMembre.setMontantSupplement(String.valueOf(supplementContribution));
                simpleDetailFamilleMembre.setSupplExtra(String.valueOf(supplementContribution));
                simpleDetailFamilleMembre
                        .setMontantContribSansSuppl(simpleDetailFamilleMembre.getMontantContribution());
            }
        }
    }
}
