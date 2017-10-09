package ch.globaz.pegasus.process.statistiquesOFAS.step1;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.pyxis.api.ITIPersonne;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityPropertySavable;
import ch.globaz.pegasus.business.constantes.EPCLoiCantonaleProperty;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.constantes.IPCRenteijapi;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCApiAvsAi;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRenteAvsAi;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.process.StatistiquesOFASException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.RenteAvsAiException;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalculSearch;
import ch.globaz.pegasus.business.models.process.statistiquesofas.PlanCalculeDemandeDroitMembreFamille;
import ch.globaz.pegasus.business.models.process.statistiquesofas.PlanCalculeDemandeDroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.process.statistiquesofas.RenteApiIj;
import ch.globaz.pegasus.business.models.process.statistiquesofas.RenteApiIjSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.calcul.IPeriodePCAccordee;
import ch.globaz.pegasus.businessimpl.utils.calcul.PegasusCalculUtil;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.TypeRenteMap;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.DonneesHorsDroitsProvider;
import ch.globaz.pegasus.process.statistiquesOFAS.PCProcessStatistiquesOFASEnum;
import ch.globaz.pegasus.process.statistiquesOFAS.StatistiquesOFAData;
import ch.globaz.pegasus.process.statistiquesOFAS.StatistiquesOFASBeneficiaire;
import ch.globaz.pegasus.process.statistiquesOFAS.StatistiquesOFASDepense;
import ch.globaz.pegasus.process.statistiquesOFAS.StatistiquesOFASIFortuneDettes;
import ch.globaz.pegasus.process.statistiquesOFAS.StatistiquesOFASRevenu;
import com.google.gson.Gson;

public class PCProcessStatistiqueOFASEntityHandler implements JadeProcessEntityInterface,
        JadeProcessEntityPropertySavable<PCProcessStatistiquesOFASEnum>, JadeProcessEntityNeedProperties {
    private Boolean checkOk = false;
    private DonneesHorsDroitsProvider containerGlobal = null;
    private String csGenrePc = null;
    private String csGenrePcConjoint = null;
    private Map<PCProcessStatistiquesOFASEnum, String> data = new HashMap<PCProcessStatistiquesOFASEnum, String>();
    JadeProcessEntity entity = null;
    private boolean hasConjoint = false;
    private String idPca = null;
    private String idVersionDroit;
    private Boolean isCoupleSepareParLaMaladie;
    private List<Integer> nbCheckFalse = null;
    private Map<Enum<?>, String> properties = null;

    public PCProcessStatistiqueOFASEntityHandler(final DonneesHorsDroitsProvider containerGlobal2,
            List<Integer> listCheckFalse) {
        containerGlobal = containerGlobal2;
        nbCheckFalse = listCheckFalse;
    }

    /**
     * Adapte le montant donné si couple séparé par la maladie (1/2 du montant donné)
     * 
     * @param montant
     * @param isCoupleSepareParMaladie
     * @return
     */
    private float adaptMontantPourCoupleSepareParMaladie(float montant) {

        return (isCoupleSepareParLaMaladie) ? (new BigDecimal(montant).divide(new BigDecimal(2), BigDecimal.ROUND_UP)
                .floatValue()) : montant;
    }

    private String convertEtatCivil(String etatCivil, boolean hasSameSexe) {
        Checkers.checkNotNull(etatCivil, "etatCivil");
        Checkers.checkNotNull(hasSameSexe, "hasSameSexe");
        if (TITiersViewBean.CS_CELIBATAIRE.equals(etatCivil)) {
            return "1";
        } else if (TITiersViewBean.CS_MARIE.equals(etatCivil)) {
            if (hasSameSexe) {
                return "6";
            } else {
                return "2";
            }
        } else if (TITiersViewBean.CS_VEUF.equals(etatCivil)) {
            if (hasSameSexe) {
                return "8";
            } else {
                return "3";
            }
        } else if (TITiersViewBean.CS_DIVORCE.equals(etatCivil)) {
            if (hasSameSexe) {
                return "7";
            } else {
                return "4";
            }
        } else if (TITiersViewBean.CS_SEPARE.equals(etatCivil) && TITiersViewBean.CS_SEPARE_DE_FAIT.equals(etatCivil)) {
            if (hasSameSexe) {
                return "9";
            } else {
                return "5";
            }
        } else {
            return "99";
        }
    }

    private void convertGenreHabitation(StatistiquesOFASBeneficiaire beneficiaire,
            PlanCalculeDemandeDroitMembreFamille pcaPersonne) throws StatistiquesOFASException {
        if (IPCPCAccordee.CS_GENRE_PC_DOMICILE.equals(pcaPersonne.getCsGenrePC())) {
            beneficiaire.setGenreHabitation("1");
        } else if (IPCPCAccordee.CS_GENRE_PC_HOME.equals(pcaPersonne.getCsGenrePC())) {
            beneficiaire.setGenreHabitation("2");
        } else {
            throw new StatistiquesOFASException("Unbale to determin the genre of the PC");
        }
    }

    private String convertSexe(String csSexe) {
        if (ITIPersonne.CS_FEMME.equals(csSexe)) {
            return "2";
        } else if (ITIPersonne.CS_HOMME.equals(csSexe)) {
            return "1";
        } else {
            return "9";
        }

    }

    /**
     * Construit une Collection des membres de familles retenu dans le calcul courant.
     * 
     * @param search
     *            PCAIdMembreFamilleRetenuSearch
     * @return
     */
    private Collection<String> createListIdMembreFamille(PlanCalculeDemandeDroitMembreFamilleSearch search) {

        Collection<String> collection = new ArrayList<String>();
        for (JadeAbstractModel model : search.getSearchResults()) {
            PlanCalculeDemandeDroitMembreFamille m = (PlanCalculeDemandeDroitMembreFamille) model;
            collection.add(m.getIdMembreFamilleSF());
        }

        return collection;
    }

    private String findCategorieRente(String idDroit) throws StatistiquesOFASException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        RenteApiIjSearch renteIjApiSearch = getRenteIjApiByIdPca(idPca, idDroit);
        String catRente = "9";
        for (JadeAbstractModel modelRente : renteIjApiSearch.getSearchResults()) {
            RenteApiIj renteApiIj = (RenteApiIj) modelRente;

            if (IPCDroits.CS_RENTE_AVS_AI.equals(renteApiIj.getDonnefinanciereHeaderCsType())) {
                if (TypeRenteMap.listeCsRenteInvalidite.contains(renteApiIj.getRenteAvsAiTypeRente())) {
                    return "3";
                } else if (TypeRenteMap.listeCsRenteSurvivant.contains(renteApiIj.getRenteAvsAiTypeRente())) {
                    return "2";
                } else if (TypeRenteMap.listeCsRenteVieillesse.contains(renteApiIj.getRenteAvsAiTypeRente())) {
                    return "1";
                } else if (IPCRenteijapi.CS_SANS_RENTE_AVS.equals(renteApiIj.getRenteAvsAiTypeRente())) {
                    if (IPCRenteAvsAi.CS_TYPE_SANS_RENTE_INVALIDITE.equals(renteApiIj.getRenteAvsAiTypePC())) {
                        return "3";
                    } else if (IPCRenteAvsAi.CS_TYPE_SANS_RENTE_SURVIVANT.equals(renteApiIj.getRenteAvsAiTypePC())) {
                        return "2";
                    } else if (IPCRenteAvsAi.CS_TYPE_SANS_RENTE_VIEILLESSE.equals(renteApiIj.getRenteAvsAiTypePC())) {
                        return "1";
                    } else {
                        throw new StatistiquesOFASException("Any type pc founded for the type sans rente");
                    }
                }
            } else if (IPCApiAvsAi.CS_TYPE_DONNEE_FINANCIERE.equals(renteApiIj.getDonnefinanciereHeaderCsType())) {
                catRente = "4";
            } else if (IPCDroits.CS_INDEMNITES_JOURNLIERES_APG.equals(renteApiIj.getDonnefinanciereHeaderCsType())) {
                catRente = "5";
            }
        }
        return catRente;
    }

    private PlanCalculeDemandeDroitMembreFamilleSearch findMembreFamilleWithPlanPlanCalculRetenu(String idPca)
            throws PCAccordeeException, JadePersistenceException, JadeApplicationServiceNotAvailableException,
            StatistiquesOFASException {
        PlanCalculeDemandeDroitMembreFamilleSearch search = new PlanCalculeDemandeDroitMembreFamilleSearch();
        search.setForIdPCAccordee(idPca);
        search.setIsPlanRetenu(true);
        search.setIsComprisDansCalcul(true);
        PegasusServiceLocator.getStatistiquesOFASService().search(search);
        return search;
    }

    public SimplePlanDeCalcul findPlanClaculeRetenu(String pcaId) throws JadePersistenceException, PCAccordeeException {
        SimplePlanDeCalcul simplePlanDeCalcul = null;
        SimplePlanDeCalculSearch planClaculeSearch = new SimplePlanDeCalculSearch();
        planClaculeSearch.setForIdPCAccordee(pcaId);
        planClaculeSearch.setForIsPlanRetenu(true);
        planClaculeSearch = (SimplePlanDeCalculSearch) JadePersistenceManager.search(planClaculeSearch, true);
        if (planClaculeSearch.getSize() == 1) {
            simplePlanDeCalcul = (SimplePlanDeCalcul) planClaculeSearch.getSearchResults()[0];
        } else if (planClaculeSearch.getSize() > 1) {
            throw new PCAccordeeException("Too many plans calcule founded with this idPca:" + pcaId);
        } else {
            throw new PCAccordeeException("Any one plan calcule founded with this idPca:" + pcaId);
        }
        return simplePlanDeCalcul;
    }

    public Boolean getCheckOk() {
        return checkOk;
    }

    /**
     * Origine de l'ayant droit 1 = Suisse 2 = Etranger 9 = Origine inconnue
     * 
     * @param cs
     */
    private String getOrigine(String cs) {
        if (JadeStringUtil.isEmpty(cs)) {
            return "9";
        }
        if (PRTiersHelper.ID_PAYS_SUISSE.equals(cs)) {
            return "1";
        } else {
            return "2";
        }
    }

    private String getPlanDeCalcule(SimplePlanDeCalcul simplePlanDeCalcul) throws PCAccordeeException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        // SimplePlanDeCalcul planDeCalcul = PegasusImplServiceLocator.getSimplePlanDeCalculService().read(
        // planCalculeDemandeDroitMembreFamille.getIdPlandDeCalcule());
        if (simplePlanDeCalcul.getResultatCalcul() == null) {
            return null;
        }
        String byteArrayToString = new String(simplePlanDeCalcul.getResultatCalcul());
        return byteArrayToString;
    }

    private RenteApiIjSearch getRenteIjApiByIdPca(String idPCAccodee, String idDroit)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, StatistiquesOFASException {
        RenteApiIjSearch search = new RenteApiIjSearch();
        search.setForIdPCAccordee(idPCAccodee);
        search.setForIsSupprime(false);
        search.setForCsRoleFamille(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
        search.setForIdDroit(idDroit);
        search.setForDateValable(properties.get(PCProcessStatistiquesOFASEnum.DATE_STATISTIQUE));
        search.setWhereKey(RenteApiIjSearch.FOR_PC_ACCORDEE_WITH_DATE_VALABLE);
        search = PegasusServiceLocator.getStatistiquesOFASService().search(search);
        return search;
    }

    @Override
    public Map<PCProcessStatistiquesOFASEnum, String> getValueToSave() {
        return data;
    }

    private StatistiquesOFASBeneficiaire mapBeneficiaire(TupleDonneeRapport tupleRoot,
            PlanCalculeDemandeDroitMembreFamilleSearch search, PCAccordee pcAccordee) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, StatistiquesOFASException,
            RenteAvsAiException {
        StatistiquesOFASBeneficiaire beneficiaire = new StatistiquesOFASBeneficiaire();
        int nbEnfant = 0;
        String csRole = null;
        boolean hasRequerant = false;
        boolean isEnfant = false;
        boolean inHome = false;
        String etatCivil = null;

        hasConjoint = false;
        // TODO Passer la constante
        beneficiaire.setOrgane("26");

        beneficiaire.setDate(properties.get(PCProcessStatistiquesOFASEnum.DATE_STATISTIQUE));

        // On met ces valeurs par défaut si il n'y a pas de conjoint
        beneficiaire.setNumAvsConj("0");
        beneficiaire.setSexeConj("0");
        beneficiaire.setOrigineConj("0");
        beneficiaire.setAnneeNaissConj("0");
        beneficiaire.setNumAvsDansHome("0");

        if (tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TAXES_PENSION_RECONNUE) > 0) {
            inHome = true;
        }

        csGenrePc = pcAccordee.getSimplePCAccordee().getCsGenrePC();

        for (JadeAbstractModel model : search.getSearchResults()) {
            PlanCalculeDemandeDroitMembreFamille pcaPersonne = (PlanCalculeDemandeDroitMembreFamille) model;

            csRole = pcaPersonne.getCsRoleFamillePC();

            // on est le conjoint si pas un enfant et idTiersBenenficiaire <> idTiersMembreFamille
            // CONJOINT
            if ((!pcAccordee.getSimplePrestationsAccordees().getIdTiersBeneficiaire()
                    .equals(pcaPersonne.getIdTiersMembreFamille()))
                    && (!IPCDroits.CS_ROLE_FAMILLE_ENFANT.equals(csRole))) {

                if (inHome) {
                    beneficiaire.setNumAvsDansHome(pcaPersonne.getNumAvs());
                } else {
                    beneficiaire.setNumAvsConj(pcaPersonne.getNumAvs());
                    // uniquement si conjoint à DOM
                    beneficiaire.setAnneeNaissConj(pcaPersonne.getDateNaissance());
                    beneficiaire.setSexeConj(convertSexe(pcaPersonne.getSexe()));
                    beneficiaire.setOrigineConj(getOrigine(pcaPersonne.getNationalite()));
                }
                hasConjoint = true;
                // on est le requerant si idTiersBenenficiaire = idTiersMembreFamille
                // REQUERANT
            } else if (pcAccordee.getSimplePrestationsAccordees().getIdTiersBeneficiaire()
                    .equals(pcaPersonne.getIdTiersMembreFamille())) {
                hasRequerant = true;
                etatCivil = pcaPersonne.getEtatCivil();
                isEnfant = pcaPersonne.getIsEnfant();
                convertGenreHabitation(beneficiaire, pcaPersonne);
                beneficiaire.setCatRente(findCategorieRente(pcaPersonne.getIdDroit()));
                beneficiaire.setDebutDroit(pcaPersonne.getDateDebutDemande());

                String codeCantonOfs = ch.globaz.pegasus.process.statistiquesOFAS.step1.TIUtil
                        .getCodeCantonOFS(pcaPersonne.getCanton());

                if (JadeStringUtil.isEmpty(codeCantonOfs)) {
                    codeCantonOfs = "99";
                }

                beneficiaire.setCantonDomicile(codeCantonOfs);
                beneficiaire.setLieuDomicile(pcaPersonne.getNumCommuneOfs());
                beneficiaire.setAnneeNaiss(pcaPersonne.getDateNaissance());
                beneficiaire.setSexe(convertSexe(pcaPersonne.getSexe()));
                beneficiaire.setOrigine(getOrigine(pcaPersonne.getNationalite()));
                beneficiaire.setNumAvs(pcaPersonne.getNumAvs());

            } else if (IPCDroits.CS_ROLE_FAMILLE_ENFANT.equals(csRole)) {
                nbEnfant++;
            } else {
                throw new StatistiquesOFASException("Unbale to find the role famille");
            }
            csRole = null;
        }

        if (hasRequerant == false) {
            throw new StatistiquesOFASException(
                    "Impossible de déterminer le bénéficiaire, aucun requérant n'a été trouvé dans les personnes comprises dans le calcul");
        }

        beneficiaire
                .setEtatCivil(convertEtatCivil(etatCivil, beneficiaire.getSexe().equals(beneficiaire.getSexeConj())));

        beneficiaire.setPartEnfants(nbEnfant);
        /**
         * 1 = Personne seules ou en home 2 = Couple 3 = Enfant/Orphelin
         */
        if (!isEnfant && (hasRequerant && !hasConjoint)) {
            beneficiaire.setCatBeneficiaire("1");
        } else if (hasConjoint && !inHome) {
            beneficiaire.setCatBeneficiaire("2");
        } else if (inHome) {
            beneficiaire.setCatBeneficiaire("1");
        } else if (isEnfant) {
            beneficiaire.setCatBeneficiaire("3");
        } else {
            throw new StatistiquesOFASException("Unbale to deternube the categorie for the beneficiaire");
        }

        return beneficiaire;
    }

    private StatistiquesOFAData mapData(TupleDonneeRapport tupleRoot,
            PlanCalculeDemandeDroitMembreFamilleSearch search, PCAccordee pcAccordee) throws PCAccordeeException,
            StatistiquesOFASException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            RenteAvsAiException, PropertiesException {
        StatistiquesOFAData data = new StatistiquesOFAData();

        if (search.getSearchResults()[0] != null) {
            data.setIdPlanCalcule(((PlanCalculeDemandeDroitMembreFamille) search.getSearchResults()[0])
                    .getIdPlandDeCalcule());
        }
        if (tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_TOTAL_CC_DEDUIT_MENSUEL) > 0) {
            // octrois
            data.setMontantPc(tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_TOTAL_CC_DEDUIT));
            // data.setMontantPc((tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_TOTAL_CC_DEDUIT_MENSUEL) * 12)
            // + (tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_TOTAL_PRIMEMAL_TOTAL)));
        } else {
            // octrois partiel
            data.setMontantPc(tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_TOTAL_PRIMEMAL_TOTAL));
        }

        // avant de traiter les données financières des revenus et des fortunes, on doit savoir si il y a un conjoint
        data.setBeneficiaire(mapBeneficiaire(tupleRoot, search, pcAccordee));
        data.setStatutCalcul(PegasusCalculUtil.getTypeSeparation(tupleRoot));
        if (isCoupleSepareParLaMaladie && hasConjoint == false) {
            JadeThread.logWarn("",
                    "Le cas a été détecté comme couple séparé par la maladie, mais aucun conjoint n'a été trouvé");
        }
        data.setDepense(mapDepense(tupleRoot));
        data.setRevenu(mapRevenu(tupleRoot));
        data.setFortuneDettes(mapFortuneDettes(tupleRoot));

        return data;
    }

    private void removePartCantonale(StatistiquesOFAData data, TupleDonneeRapport tupleRoot) {
        float diffPartCantonale = tupleRoot
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_DIFF_PART_CANTONALE);
        data.setMontantPc(data.getMontantPc() - diffPartCantonale);
    }

    private StatistiquesOFASDepense mapDepense(TupleDonneeRapport tupleRoot) throws PropertiesException {
        StatistiquesOFASDepense depense = new StatistiquesOFASDepense();

        depense.setLoyer(tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TOTAL_NON_PLAFONNE));
        depense.setLoyerCompte(tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TOTAL));

        // taxe journalier home que si au home
        if (IPCPCAccordee.CS_GENRE_PC_HOME.equals(csGenrePc)) {

            float taxHome = tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_TAXEHOME_TOTAL);

            if (EPCLoiCantonaleProperty.VALAIS.isLoiCantonPC()) {
                float fraisSoinLongueDuree = tupleRoot
                        .getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_FRAIS_LONGUE_DUREE);

                taxHome += fraisSoinLongueDuree;
            }

            // si les deux conjoints sont au home, on divise le montant par 2
            depense.setTaxeHome(taxHome);
            depense.setTaxeHomeCompte(taxHome);
        } else {
            depense.setTaxeHome(0);
            depense.setTaxeHomeCompte(0);
        }

        depense.setPersonnelles(tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_DEPPERSO_TOTAL));

        depense.setPrimeMaladie(tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_TOTAL_PRIMEMAL_REQUERANT));

        depense.setPrimeMaladieConjEnf(tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_TOTAL_PRIMEMAL_TOTAL)
                - depense.getPrimeMaladie());

        depense.setInteretHypothecaire(tupleRoot
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_INTERET_HYPOTHECAIRE));

        depense.setEntretienImmeubles(tupleRoot
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_FRAIS_ENTRETIEN_IMMEUBLE));

        // CLE_DEPEN_FRAISIMM_TOTAL remplacer par CLE_DEPEN_FRAISIMM_REVENU;
        depense.setInteretFraisDeterminant(tupleRoot
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_TOTAL_PLAFONNE));
        // HACK en attente de correction du calculateur
        if (depense.getInteretFraisDeterminant() == 0) {
            depense.setInteretFraisDeterminant(depense.getInteretHypothecaire() + depense.getEntretienImmeubles());
        }

        // si calcul en home -> 0
        if (IPCPCAccordee.CS_GENRE_PC_DOMICILE.equals(csGenrePc)) {
            depense.setBesoinsVitaux(tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_BES_VITA_TOTAL));
        } else {
            depense.setBesoinsVitaux(0);
        }

        depense.setAutresDepenses(tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_PENSVERS_TOTAL)
                + tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_COT_PSAL_TOTAL));

        return depense;

    }

    private StatistiquesOFASIFortuneDettes mapFortuneDettes(TupleDonneeRapport tupleRoot) {

        StatistiquesOFASIFortuneDettes fortuneDettes = new StatistiquesOFASIFortuneDettes();

        fortuneDettes.setImmobiliere(tupleRoot
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_NON_HABIT_PRINCIPALE)
                + tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_NON_HABITABLES));

        fortuneDettes.setHabitationPrincipal(tupleRoot
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_IMMO_HABIT_PRINCIPALE));
        // CLE_FORTU_FOR_DESS_TOTAL ajout
        fortuneDettes.setAutresFortunes(tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_TOTAL)
                + tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_DESS_TOTAL));

        fortuneDettes.setDettesHyp(tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_TOTAL));

        fortuneDettes.setAutresDettes(tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_AUT_DETT_TOTAL));

        fortuneDettes.setFranchise(tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_DED_LEGA_TOTAL));

        fortuneDettes.setPrisCompte(tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_TOTALNET_TOTAL));

        fortuneDettes.setDeductionForfaitaire(tupleRoot
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_DEDUCTION_FOFAITAIRE));

        return fortuneDettes;
    }

    private StatistiquesOFASRevenu mapRevenu(TupleDonneeRapport tupleRoot) {
        StatistiquesOFASRevenu revenu = new StatistiquesOFASRevenu();

        revenu.setRentesAvs(adaptMontantPourCoupleSepareParMaladie(tupleRoot
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENAVSAI_TOTAL)));

        // Revenu non partage pour les couples separe par la maladie
        revenu.setAllocationImpotant(tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_AVS_AI));

        revenu.setIj(adaptMontantPourCoupleSepareParMaladie(tupleRoot
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJAI)
                + tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_CHOMAGE)
                + tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_LAA)
                + tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_LAM)
                + tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_LAMAL)
                + tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_AUTRE_IJ)));

        // Revenu non partage pour les couples separe par la maladie
        revenu.setPrestaionsLAMAL(tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_LCA));

        revenu.setActLucBrut(adaptMontantPourCoupleSepareParMaladie(tupleRoot
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_DEPENDANTE)
                + tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE)
                + tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_HYPOTHETIQUE)));

        revenu.setActLucPrisEnCompt(adaptMontantPourCoupleSepareParMaladie(tupleRoot
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIVILEGIE)));

        revenu.setAutreRentes(adaptMontantPourCoupleSepareParMaladie((tupleRoot
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_TOTAL))
                + (tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_ALLOCATIONS_FAMILLIALES))));

        revenu.setRevenuFortuneMobilier(adaptMontantPourCoupleSepareParMaladie(tupleRoot
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_TOTAL)));
        // Ces valeur sont contenu dans la clez CLE_REVEN_RENFORMO_TOTAL
        float revenuImmobilier = tupleRoot
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_BIENS_IMMO_NON_HABITABLES)
                + tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_REVENUS_LOCATIONS)
                + tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_REVENUS_SOUS_LOCATIONS);

        // float revenuImmobilier = tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_TOTAL);

        revenu.setProduitFortuneImmobilier(adaptMontantPourCoupleSepareParMaladie(revenuImmobilier));
        // si il n'y a pas de bien immobilier servant d'habitation principale, la valeur locative vient dans l'usufruit
        if (JadeStringUtil.isBlankOrZero(String.valueOf(tupleRoot
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_IMMO_HABIT_PRINCIPALE)))) {
            // Revenu non partage pour les couples sépare par la maladie
            revenu.setValeurLocative(0);
            revenu.setUsufrutit(adaptMontantPourCoupleSepareParMaladie(tupleRoot
                    .getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_DROIT_HABITATION))
                    + (tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_VALEUR_LOCATIVE)));
        } else {

            // Revenu non partage pour les couples sépare par la maladie
            // revenu.setValeurLocative(tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_TOTAL)
            // - revenuImmobilier);
            revenu.setValeurLocative(tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_VALEUR_LOCATIVE));

            revenu.setUsufrutit(adaptMontantPourCoupleSepareParMaladie(tupleRoot
                    .getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_DROIT_HABITATION)));
        }

        revenu.setAutresRevenues(tupleRoot
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_CONTRAT_ENTRETIEN_VIAGER)// Revenu non partage
                                                                                                  // pour les couples
                                                                                                  // separe par la
                                                                                                  // maladie (CAV)
                + adaptMontantPourCoupleSepareParMaladie((tupleRoot
                        .getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_AUTRE_REVENUS))
                        + (tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_PENSION_ALIM_RECUE))
                        + (tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_DESS_REV_TOTAL))
                        + (tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_INTDESFO_TOTAL))));

        revenu.setForturnePriseEnCompte(adaptMontantPourCoupleSepareParMaladie(tupleRoot
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_IMP_FORT_TOTAL)));

        String taux = tupleRoot.getLegendeEnfant(IPCValeursPlanCalcul.CLE_REVEN_IMP_FORT_TOTAL);
        String tauxFractionArrondi = taux;
        if (taux.indexOf("/") >= 0) {
            String[] t = taux.split("/");
            BigDecimal tauxFraction = (new BigDecimal(t[0]).divide(new BigDecimal(t[1]), MathContext.DECIMAL128))
                    .multiply(new BigDecimal(100));
            tauxFractionArrondi = tauxFraction.setScale(0, BigDecimal.ROUND_UP).toString();
        }

        revenu.setForturnePriseEnCompteTaux(Float.valueOf(tauxFractionArrondi));

        return revenu;

    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {

        SimplePlanDeCalcul simplePlanDeCalcul = findPlanClaculeRetenu(idPca);

        PCAccordee pcAccordee = PegasusServiceLocator.getPCAccordeeService().readDetail(idPca);
        boolean isSansPlanDeCalcul = false;
        String idPcaUsed = idPca;
        if (simplePlanDeCalcul != null) {

            // On regarde si on a une copie et si c'est le cas on vas chercher le blob du parent
            if (!JadeStringUtil.isBlankOrZero(pcAccordee.getSimplePCAccordee().getIdPcaParent())) {
                idPcaUsed = pcAccordee.getSimplePCAccordee().getIdPcaParent();
                simplePlanDeCalcul = findPlanClaculeRetenu(idPcaUsed);

                // if (simplePlanDeCalcul == null) {
                // JadeThread.logError("", "Aucun plan de calcule n'a été trouvé avec l'id pca parent (id: "
                // + pcAccordee.getSimplePCAccordee().getIdPcaParent() + ")");
                // }
            }

            PlanCalculeDemandeDroitMembreFamilleSearch search = findMembreFamilleWithPlanPlanCalculRetenu(idPcaUsed);

            String byteArrayToString = getPlanDeCalcule(simplePlanDeCalcul);
            TupleDonneeRapport tupleRoot = null;

            if (byteArrayToString != null) {
                tupleRoot = PegasusImplServiceLocator.getCalculPersistanceService().deserialiseDonneesCcXML(
                        byteArrayToString);
            } else {
                // pour les cas issus de la reprise de données et qui n'ont pas de BLOB
                Droit droit = PegasusServiceLocator.getDroitService().readDroitFromVersion(idVersionDroit);

                IPeriodePCAccordee iPeriodePCAccordee = PegasusImplServiceLocator.getCalculDroitService()
                        .calculWithoutPersist(droit, createListIdMembreFamille(search), containerGlobal,
                                properties.get(PCProcessStatistiquesOFASEnum.DATE_STATISTIQUE));

                if (iPeriodePCAccordee.getCalculComparatifRetenus().length == 1) {
                    tupleRoot = iPeriodePCAccordee.getCalculComparatifRetenus()[0].getMontants();
                } else {
                    if (droit.getDemande().getDossier().getDemandePrestation().getDemandePrestation().getIdTiers()
                            .equals(pcAccordee.getSimplePrestationsAccordees().getIdTiersBeneficiaire())) {
                        tupleRoot = iPeriodePCAccordee.getCalculComparatifRetenus()[0].getMontants();
                    } else {
                        tupleRoot = iPeriodePCAccordee.getCalculComparatifRetenus()[1].getMontants();
                    }
                }
                isSansPlanDeCalcul = true;
            }
            if (PegasusCalculUtil.isCoupleSepareParMaladie(tupleRoot)) {
                isCoupleSepareParLaMaladie = true;
            }

            Gson gson = new Gson();
            StatistiquesOFAData data = mapData(tupleRoot, search, pcAccordee);
            data.setSansPlanDeCalcul(isSansPlanDeCalcul);
            testDeControleDeDonnees(data, simplePlanDeCalcul.getMontantPCMensuelle());
            // S160704_002 - retirer la part cantonale pour les stats OFAS
            removePartCantonale(data, tupleRoot);

            this.data = new HashMap<PCProcessStatistiquesOFASEnum, String>();
            this.data.put(PCProcessStatistiquesOFASEnum.OBJET_JSON_STATISTIQUESOFAS, gson.toJson(data));

        } else {
            JadeThread.logError("", "Aucun plan de calcule a été trouvé");
        }

    }

    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {
        idPca = entity.getIdRef();
        this.entity = entity;
        idVersionDroit = this.entity.getValue1().split(",")[0];
        isCoupleSepareParLaMaladie = Boolean.valueOf(this.entity.getValue1().split(",")[1]);
        csGenrePcConjoint = this.entity.getValue1().split(",")[2];
    }

    @Override
    public void setProperties(Map<Enum<?>, String> hashMap) {
        properties = hashMap;
    }

    private void testDeControleDeDonnees(StatistiquesOFAData data, String montanPCA) {
        float depensesReconues = data.getDepense().getLoyerCompte() + data.getDepense().getTaxeHomeCompte()
                + data.getDepense().getPersonnelles() + data.getDepense().getPrimeMaladie()
                + data.getDepense().getPrimeMaladieConjEnf() + data.getDepense().getInteretFraisDeterminant()
                + data.getDepense().getBesoinsVitaux() + data.getDepense().getAutresDepenses();

        float revenusDeterminants = data.getRevenu().getRentesAvs() + data.getRevenu().getAllocationImpotant()
                + data.getRevenu().getIj() + data.getRevenu().getPrestaionsLAMAL()
                + data.getRevenu().getActLucPrisEnCompt() + data.getRevenu().getAutreRentes()
                + data.getRevenu().getRevenuFortuneMobilier() + data.getRevenu().getProduitFortuneImmobilier()
                + data.getRevenu().getValeurLocative() + data.getRevenu().getUsufrutit()
                + data.getRevenu().getAutresRevenues() + data.getRevenu().getForturnePriseEnCompte();

        float montantPCCalcule = depensesReconues - revenusDeterminants;
        if (depensesReconues < 0) {
            montantPCCalcule = 0;
        }
        float mimimumGaranti = data.getDepense().getPrimeMaladie() + data.getDepense().getPrimeMaladieConjEnf();
        if ((montantPCCalcule > 0) && (montantPCCalcule < mimimumGaranti)) {
            montantPCCalcule = mimimumGaranti;
        }

        // les frais diététiques ne sont plus
        float montantPCEffectif = data.getMontantPc() - 0;

        float immo = Math.max(((data.getFortuneDettes().getHabitationPrincipal()) - data.getFortuneDettes()
                .getDeductionForfaitaire()), 0);

        float fortunePriseEnCompte = (immo + data.getFortuneDettes().getImmobiliere() + data.getFortuneDettes()
                .getAutresFortunes())
                - data.getFortuneDettes().getDettesHyp()
                - data.getFortuneDettes().getAutresDettes() - data.getFortuneDettes().getFranchise();

        float diff1 = montantPCCalcule - montantPCEffectif;

        if (fortunePriseEnCompte < 0) {
            fortunePriseEnCompte = 0;
        }

        float diff2 = fortunePriseEnCompte - data.getFortuneDettes().getPrisCompte();

        boolean controle1 = Math.abs(diff1) <= 300;
        boolean controle2 = Math.abs(diff2) <= 300;
        checkOk = true;
        if (!controle1) {
            checkOk = false;
            JadeThread.logWarn("", "Controle 1 pas passé différence de " + diff1 + " Montant pca clacule ofas:"
                    + montantPCCalcule + "Montant dans webPC:" + montantPCEffectif);
        }
        if (!controle2) {
            JadeThread.logWarn("", "Controle 2 pas passé différence de " + diff2);
            checkOk = false;
        }
        if (!checkOk) {
            nbCheckFalse.add(1);
        }

        BigDecimal montantPCAEffectiveAnulaise = new BigDecimal(montanPCA).multiply(new BigDecimal(12)).add(
                new BigDecimal(mimimumGaranti));

        if (montantPCAEffectiveAnulaise.subtract(new BigDecimal(montantPCEffectif)).abs().intValue() > 12) {
            JadeThread.logWarn("",
                    "Le montant de la pc ne correspond pas au montant défini dans le plan de calcul. PCA:  "
                            + montantPCAEffectiveAnulaise.intValue() + ", planCalcul: " + montantPCEffectif);
        }

        this.data.put(PCProcessStatistiquesOFASEnum.CONTROLE_ONE, String.valueOf(controle1));
        this.data.put(PCProcessStatistiquesOFASEnum.CONTROLE_TWO, String.valueOf(controle2));

    }
}
