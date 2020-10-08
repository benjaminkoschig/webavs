/**
 *
 */
package ch.globaz.pegasus.businessimpl.services.models.calcul;

import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.corvus.business.exceptions.CorvusException;
import ch.globaz.corvus.business.exceptions.models.RentesAccordeesException;
import ch.globaz.corvus.business.models.rentesaccordees.SimpleInformationsComptabilite;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;
import ch.globaz.corvus.business.models.ventilation.SimpleVentilation;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.pegasus.business.constantes.*;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculBusinessException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.calcul.CalculPcaReplace;
import ch.globaz.pegasus.business.models.calcul.CalculPcaReplaceSearch;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.pcaccordee.*;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.calcul.CalculPersistanceService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.determineSousCodePrestation.DetermineSousCodePrestation;
import ch.globaz.pegasus.businessimpl.utils.PCproperties;
import ch.globaz.pegasus.businessimpl.utils.calcul.*;
import ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee.TypeSeparationCC;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.ventilation.constantes.REVentilationType;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadeCloneModelException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ECO
 */
public class CalculPersistanceServiceImpl extends PegasusAbstractServiceImpl implements CalculPersistanceService {

    private static final int ID_SIMPLE_PRESTATION_ACCORDEE_CONJOINT = 1;
    private static final int ID_SIMPLE_PRESTATION_ACCORDEE_REQUERANT = 0;

    /**
     * @param droit
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    @Override
    public void clearPCAccordee(Droit droit) throws JadePersistenceException, JadeApplicationException {
        PegasusServiceLocator.getPCAccordeeService().deleteByIdVersionDroit(droit);
        /*
         * try { PegasusImplServiceLocator.getCleanDecisionService().deleteDecisionsApresCalculForVersion(
         * droit.getSimpleVersionDroit().getIdVersionDroit()); } catch (DecisionException e) { throw new
         * JadePersistenceException("Unable to delete the decisions", e); }
         */
    }

    private List<SimpleInformationsComptabilite> createInformationsComptabilite(Droit droit, PeriodePCAccordee periode)
            throws RentesAccordeesException, JadePersistenceException, JadeApplicationServiceNotAvailableException,
            CalculException {
        List<SimpleInformationsComptabilite> resultat = new ArrayList<SimpleInformationsComptabilite>();

        SimpleInformationsComptabilite simpleInformationsComptabilite = new SimpleInformationsComptabilite();
        simpleInformationsComptabilite.setIdTiersAdressePmt(droit.getDemande().getDossier().getDemandePrestation()
                .getDemandePrestation().getIdTiers());

        simpleInformationsComptabilite = CorvusServiceLocator.getSimpleInformationsComptabiliteService().create(
                simpleInformationsComptabilite);
        resultat.add(simpleInformationsComptabilite);

        // si cas de couple à domicile avec 2 rentes principales OU separe par maladie, créer aussi info comptabilité
        // pour conjoint
        if ((periode.getTypeSeparationCC() == TypeSeparationCC.CALCUL_DOM2_PRINCIPALE)
                || (periode.getTypeSeparationCC() == TypeSeparationCC.CALCUL_SEPARE_MALADIE)) {

            PersonnePCAccordee conjoint = null;
            for (PersonnePCAccordee personne : periode.getPersonnes().values()) {
                if (IPCDroits.CS_ROLE_FAMILLE_CONJOINT.equals(personne.getCsRoleFamille())) {
                    conjoint = personne;
                    break;
                }
            }

            if (conjoint == null) {
                throw new CalculBusinessException("pegasus.calcul.persistance.infoCompta.coupleDOM2Rentes.integrity",
                        periode.getStrDateDebut(), periode.getStrDateFin());
            }

            // cherche idtiers du conjoint
            DroitMembreFamille conjointDMF;
            try {
                conjointDMF = PegasusImplServiceLocator.getDroitMembreFamilleService().read(
                        conjoint.getIdDroitPersonne());
            } catch (DroitException e) {
                throw new CalculException("Error while trying to find Tiers", e);
            }

            SimpleInformationsComptabilite simpleInformationsComptabiliteConjoint = new SimpleInformationsComptabilite();
            simpleInformationsComptabiliteConjoint.setIdTiersAdressePmt(conjointDMF.getMembreFamille()
                    .getSimpleMembreFamille().getIdTiers());
            simpleInformationsComptabiliteConjoint = CorvusServiceLocator.getSimpleInformationsComptabiliteService()
                    .create(simpleInformationsComptabiliteConjoint);
            resultat.add(simpleInformationsComptabiliteConjoint);

        }
        return resultat;
    }

    private PCAccordeePlanCalcul createPCAccordee(Droit droit, PeriodePCAccordee periode,
                                                  CalculPcaReplaceSearch anciennesPCAccordees, SimplePrestationsAccordees spa, boolean isConjoint,
                                                  String idEntityGroupPCA) throws CalculException, JadePersistenceException, PCAccordeeException,
            JadeApplicationServiceNotAvailableException {
        return this.createPCAccordee(droit, periode, anciennesPCAccordees, spa, null, isConjoint, idEntityGroupPCA);
    }

    private PCAccordeePlanCalcul createPCAccordee(Droit droit, PeriodePCAccordee periode,
                                                  CalculPcaReplaceSearch anciennesPCAccordees, SimplePrestationsAccordees spa,
                                                  SimplePrestationsAccordees spaConjoint, boolean isConjoint, String idEntityGroupPCA)
            throws CalculException, JadePersistenceException, PCAccordeeException,
            JadeApplicationServiceNotAvailableException {

        SimplePCAccordee simplePcAccordee = new SimplePCAccordee();

        PCAccordeePlanCalcul pcAccordeePlanCalcul = new PCAccordeePlanCalcul();

        final String csRoleBeneficiaire = (isConjoint ? IPCDroits.CS_ROLE_FAMILLE_CONJOINT
                : IPCDroits.CS_ROLE_FAMILLE_REQUERANT);

        simplePcAccordee.setIdPrestationAccordee(spa.getId());
        // Couple a DOM 2 rentes principales
        if (spaConjoint != null) {
            simplePcAccordee.setIdPrestationAccordeeConjoint(spaConjoint.getId());
        }

        simplePcAccordee.setIdVersionDroit(droit.getSimpleVersionDroit().getId());
        simplePcAccordee.setDateDebut(JadeDateUtil.convertDateMonthYear(periode.getStrDateDebut()));
        simplePcAccordee.setDateFin(JadeDateUtil.convertDateMonthYear(periode.getStrDateFin()));
        simplePcAccordee.setIsCalculManuel(false);
        simplePcAccordee.setHasCalculComparatif(periode.getCalculsComparatifs().size() > 1);
        simplePcAccordee.setIsCalculRetro(periode.isCalculRetro());
        simplePcAccordee.setCsRoleBeneficiaire(csRoleBeneficiaire);
        simplePcAccordee.setCodeRente(periode.getCodeRente());
        // TODO determine valeurs des champs
        simplePcAccordee.setCsEtatPC(IPCPCAccordee.CS_ETAT_PCA_CALCULE);

        SimpleJoursAppoint joursAppoint = (isConjoint ? periode.getJoursAppointConjoint() : periode
                .getJoursAppointRequerant());
        simplePcAccordee.setHasJoursAppoint(joursAppoint != null);

        // boolean isHome = periode.getIsHome();

        boolean isHome;

        // ne doit pas arriver, le cs beneficiaire doit retourner une personne
        try {
            isHome = periode.getPersonneByCsRole(csRoleBeneficiaire).getIsHome();
        } catch (NullPointerException e) {
            throw new CalculBusinessException("The cs beneficiaire: " + csRoleBeneficiaire + " is not found!");
        }

        String typePc = periode.getTypePc();

        simplePcAccordee.setCsGenrePC(isHome ? IPCPCAccordee.CS_GENRE_PC_HOME : IPCPCAccordee.CS_GENRE_PC_DOMICILE);
        simplePcAccordee.setCsTypePC(typePc);

        // TODO determine isSupprimé
        simplePcAccordee.setIsSupprime(false);

        // recherche relations avec anciennes pc accordées
        for (JadeAbstractModel absDonnee : anciennesPCAccordees.getSearchResults()) {
            CalculPcaReplace ancienneDonnee = (CalculPcaReplace) absDonnee;
            if (csRoleBeneficiaire.equals(ancienneDonnee.getSimplePCAccordee().getCsRoleBeneficiaire())
                    && ancienneDonnee.getSimplePCAccordee().getDateDebut().equals(simplePcAccordee.getDateDebut())) {
                simplePcAccordee.setIdEntity(ancienneDonnee.getSimplePCAccordee().getIdEntity());
            }
        }
        if (simplePcAccordee.getIdEntity() == null) {
            simplePcAccordee.setIdEntity(JadePersistenceManager.incIndentifiant(Compteurs.PC_ACCORDEE_ID_ENTITY));
        }
        simplePcAccordee.setIdEntityGroup(idEntityGroupPCA);

        addDateFinForPcaEnRefus(periode, simplePcAccordee);
        // creation du pc accorde
        simplePcAccordee = PegasusImplServiceLocator.getSimplePCAccordeeService().create(simplePcAccordee);

        // on set l'id correspondant dans la periode pca
        if (simplePcAccordee.getCsRoleBeneficiaire().equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {
            periode.setIdSimplePcAccordee(simplePcAccordee.getIdPCAccordee());
        } else {
            periode.setIdSimplePcAccordeeConjoint(simplePcAccordee.getIdPCAccordee());
        }

        periode.setIdSimplePcAccordee(simplePcAccordee.getIdPCAccordee());

        simplePcAccordee.setIdTiersBeneficiaire(spa.getIdTiersBeneficiaire());

        pcAccordeePlanCalcul.setSimplePCAccordee(simplePcAccordee);

        // ajout des plans de calcul
        for (CalculComparatif cc : periode.getCalculsComparatifs()) {

            // verifier que le plan de calcul soit celui du bénéficiaire actuel(en cas de couple séparé)
            if (isConjoint ^ periode.getCalculsComparatifsConjoint().contains(cc)) {
                continue;
            }

            String calculSerialise = PegasusImplServiceLocator.getCalculPersistanceService().serialiseDonneesCcXML(cc);
            int length = calculSerialise.length();
            int lenght2 = calculSerialise.getBytes().length;

            SimplePlanDeCalcul simplePlanCalcul = new SimplePlanDeCalcul();

            simplePlanCalcul.setIdPCAccordee(simplePcAccordee.getId());
            simplePlanCalcul.setResultatCalcul(calculSerialise.getBytes());
            simplePlanCalcul.setIsPlanRetenu(cc.isPlanRetenu());
            simplePlanCalcul.setMontantPCMensuelle(cc.getMontantPCMensuel());
            simplePlanCalcul.setExcedentPCAnnuel(cc.getExcedentAnnuel());
            simplePlanCalcul.setPrimeMoyenneAssMaladie(cc.getPrimeMoyenneAssMaladie());
            simplePlanCalcul.setMontantPrixHome(cc.getMontantPrixHomeReforme());
            simplePlanCalcul.setPrimeVerseeAssMaladie(cc.getPrimeVerseeAssMaladie());
            simplePlanCalcul.setEtatPC(cc.getEtatPC());
            simplePlanCalcul.setIsPlanCalculAccessible(Boolean.TRUE);
            simplePlanCalcul.setReformePc(cc.isReformePc());

            if (cc.isPlanRetenu()) {
                pcAccordeePlanCalcul.setSimplePlanDeCalcul(simplePlanCalcul);
            }

            // creation du plan de calcul
            simplePlanCalcul = PegasusImplServiceLocator.getSimplePlanDeCalculService().create(simplePlanCalcul);
            cc.setIdPlanCalcul(simplePlanCalcul.getIdPlanDeCalcul());

            // creation des personnesDansPlanCalcul
            for (PersonnePCAccordee personne : periode.getPersonnes().values()) {
                SimplePersonneDansPlanCalcul simplePersonneBD = new SimplePersonneDansPlanCalcul();
                simplePersonneBD.setIdDroitMembreFamille(personne.getIdDroitPersonne());
                simplePersonneBD.setIdPlanDeCalcul(simplePlanCalcul.getId());
                simplePersonneBD.setIsComprisDansCalcul(cc.getPersonnes().contains(personne));
                simplePersonneBD.setIsRentier(!personne.isSansRente());
                PegasusImplServiceLocator.getSimplePersonneDansPlanCalculService().create(simplePersonneBD);
            }

        }
        return pcAccordeePlanCalcul;
    }

    private void addDateFinForPcaEnRefus(PeriodePCAccordee periode, SimplePCAccordee simplePcAccordee) {
        if (periode.getDateFin() == null) {
            ICalculComparatif ccRequerant = periode.getCalculComparatifRetenus()[0];
            ICalculComparatif ccConjoint = null;
            if (periode.getCalculComparatifRetenus().length > 1) {
                ccConjoint = periode.getCalculComparatifRetenus()[1];
            }

            if (ccRequerant != null) {
                if (IPCValeursPlanCalcul.STATUS_REFUS.equals(ccRequerant.getEtatPC())) {
                    // on froce une date fin pour le couple séparé par la maladie seulement si les 2 PCA sont en refsu.
                    if ((ccConjoint == null)
                            || ((ccConjoint != null) && IPCValeursPlanCalcul.STATUS_REFUS
                            .equals(ccConjoint.getEtatPC()))) {
                        simplePcAccordee.setDateFin("12."
                                + JadeDateUtil.convertDateMonthYear(periode.getStrDateDebut()).substring(3));
                        simplePcAccordee.setIsDateFinForce(true);
                    }
                }
            }
        }
    }

    private List<SimplePrestationsAccordees> createPrestationAccordee(Droit droit, PeriodePCAccordee periode,
                                                                      List<SimpleInformationsComptabilite> simpleInformationsComptabilite,
                                                                      CalculPcaReplaceSearch anciennesPCAccordees) throws CalculException, JadePersistenceException,
            JadeApplicationException, JadeApplicationServiceNotAvailableException {

        List<SimplePrestationsAccordees> result = new ArrayList<SimplePrestationsAccordees>(2);

        List<CalculPcaReplace> anciennePcaCourrantes = resolvePcaCourrante(anciennesPCAccordees);
        CalculPcaReplace anciennePcaRequerant = null;
        CalculPcaReplace anciennePcaConjoint = null;
        if (anciennePcaCourrantes.size() > 0) {
            anciennePcaRequerant = anciennePcaCourrantes.get(0);
            if (anciennePcaCourrantes.size() == 2) {
                anciennePcaConjoint = anciennePcaCourrantes.get(1);
            }
        }

        // prerequis: le simpleinfocompta[0] est toujours celui du requerant. le 2e est celui du conjoint.
        SimplePrestationsAccordees simplePrestationAccordeeRequerant = createSimplePrestationAccordee(periode,

                simpleInformationsComptabilite.get(0), periode.getCCRetenu()[0], false, anciennePcaRequerant);
        result.add(simplePrestationAccordeeRequerant); // pour requerant
        createVentilationPartCantonalePC(periode, simplePrestationAccordeeRequerant.getId(), false,
                periode.getCCRetenu()[0].getMontantPartCantonale());

        if (TypeSeparationCC.CALCUL_SEPARE_MALADIE.equals(periode.getTypeSeparationCC())) {

            SimplePrestationsAccordees simplePrestationAccordeeConjoint = createSimplePrestationAccordee(periode,
                    simpleInformationsComptabilite.get(1), periode.getCCRetenu()[1], true, anciennePcaConjoint);

            result.add(simplePrestationAccordeeConjoint); // pour conjoint
            createVentilationPartCantonalePC(periode, simplePrestationAccordeeConjoint.getId(), true,
                    periode.getCCRetenu()[1].getMontantPartCantonale());

        } else if (TypeSeparationCC.CALCUL_DOM2_PRINCIPALE.equals(periode.getTypeSeparationCC())) {
            SimplePrestationsAccordees simplePrestationAccordeeConjoint = createSimplePrestationAccordee(periode,
                    simpleInformationsComptabilite.get(1), periode.getCCRetenu()[0], true, anciennePcaRequerant);

            result.add(simplePrestationAccordeeConjoint); // pour conjoint
            createVentilationPartCantonalePC(periode, simplePrestationAccordeeConjoint.getId(), true,
                    periode.getCCRetenu()[0].getMontantPartCantonale());
        }

        return result;
    }

    private SimpleVentilation createVentilationPartCantonalePC(PeriodePCAccordee periode, String idPrestatoinAccordee,
                                                               boolean isConjoint, String montantPartCantonale) throws CorvusException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // S160704_002 : si on a une part cantonale on créé une écriture dans la table de ventilation des rentes /
        // prestations accordées
        if (!"0.0".equals(montantPartCantonale)) {

            SimpleVentilation ventilationPartCantonale = new SimpleVentilation();
            ventilationPartCantonale.setCsTypeVentilation(REVentilationType.PART_CANTONALE.getValue());
            ventilationPartCantonale.setIdPrestationAccordee(idPrestatoinAccordee);

            if (TypeSeparationCC.CALCUL_DOM2_PRINCIPALE.equals(periode.getTypeSeparationCC())) {
                if (!JadeNumericUtil.isEmptyOrZero(montantPartCantonale)) {
                    ventilationPartCantonale.setMontantVentile(calculPartCantonaleConjoint(isConjoint,
                            montantPartCantonale));
                }
            } else {
                ventilationPartCantonale.setMontantVentile(montantPartCantonale);
            }

            return CorvusServiceLocator.getSimpleVentilationService().create(ventilationPartCantonale);
        }
        return null;
    }

    // S160704_002 :Il faut splitter la part cantonal entre les 2 conjoints
    private String calculPartCantonaleConjoint(boolean isConjoint, String montant) {
        float value = Float.valueOf(montant);

        float partRequerant = (float) Math.ceil(value / 2f);
        float partConjoint = value - partRequerant;

        return isConjoint ? String.valueOf(partConjoint) : String.valueOf(partRequerant);
    }

    private SimplePrestationsAccordees createSimplePrestationAccordee(PeriodePCAccordee periode,
                                                                      SimpleInformationsComptabilite simpleInfoCompta, CalculComparatif ccRetenu, boolean isConjoint,
                                                                      CalculPcaReplace calculPcaReplace) throws CalculException, JadePersistenceException,
            JadeApplicationException, JadeApplicationServiceNotAvailableException {

        SimplePrestationsAccordees spa = new SimplePrestationsAccordees();

        spa.setIdTiersBeneficiaire(simpleInfoCompta.getIdTiersAdressePmt());

        spa.setCodePrestation(getCodePrestationFromTypePc(periode.getTypePc()));

        spa.setDateDebutDroit(periode.getStrDateDebut().substring(3));
        // On sette le sous type de prestation pour avoir des infos suplémentaire en compta

        spa.setSousCodePrestation(determineSousTypeGenre(periode, isConjoint));

        if (!JadeStringUtil.isBlankOrZero(periode.getStrDateFin())) {
            spa.setDateFinDroit(periode.getStrDateFin().substring(3));
        }

        spa.setCsGenre(IREPrestationAccordee.CS_GENRE_PC);

        spa.setIdInfoCompta(simpleInfoCompta.getIdInfoCompta());
        // Dans le cas de couple à domicile avec 2 rentes principale, mettre le montant divisé. Sinon mettre
        // montant total

        // TODO gérer les montants vide ????
        if (TypeSeparationCC.CALCUL_DOM2_PRINCIPALE.equals(periode.getTypeSeparationCC())) {
            if (isConjoint && !JadeNumericUtil.isEmptyOrZero(ccRetenu.getMontantMensuelConjoint())) {
                spa.setMontantPrestation(ccRetenu.getMontantMensuelConjoint());
            } else if (!isConjoint && !JadeNumericUtil.isEmptyOrZero(ccRetenu.getMontantMensuelRequerant())) {
                spa.setMontantPrestation(ccRetenu.getMontantMensuelRequerant());
            }
        } else {
            spa.setMontantPrestation(ccRetenu.getMontantPCMensuel());
        }
        // gestipn etat repracc prestationsAccordee
        if (!JadeStringUtil.isBlank(periode.getDateFinDemandeToClosePca())) {
            // la date de fin de la période est déjà celle de la demande
            spa.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
        } else {
            spa.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
        }

        if ((calculPcaReplace != null)
                && !(IPCValeursPlanCalcul.STATUS_REFUS.equals(ccRetenu.getEtatPC()) || IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL
                .equals(ccRetenu.getEtatPC()))) {
            if (calculPcaReplace.getSimplePrestationsAccordees().getIsPrestationBloquee()) {
                spa.setIsPrestationBloquee(true);
            } else {
                spa.setIsPrestationBloquee(false);
            }
        }

        spa.setIsRetenues(false);
        spa.setIsAttenteMajBlocage(false);
        spa.setIsAttenteMajRetenue(false);

        return PegasusImplServiceLocator.getSimplePrestatioAccordeeService().create(spa);
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.pegasus.business.services.models.calcul.CalculPersistanceService
     * #deserialiseDonneesCcXML(ch.globaz.pegasus.businessimpl.utils.calcul. CalculComparatif, java.lang.String)
     */
    @Override
    public TupleDonneeRapport deserialiseDonneesCcXML(String donneeSerialisee) {

        XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(donneeSerialisee.getBytes()));
        TupleDonneeRapport tupleroot = (TupleDonneeRapport) decoder.readObject();

        return tupleroot;

    }

    private String determineSousTypeGenre(PeriodePCAccordee periode, boolean isConjoint)
            throws JadeApplicationException {
        String sousTypeGenrePrestation = null;
        // On ne sauvegarde pas de sous code si cela n'est pas définit via la propriété
        if (CommonProperties.SOUS_TYPE_GENRE_PRESTATION_ACTIF.getBooleanValue()) {
            DetermineSousCodePrestation sousCode = DetermineSousCodePrestation.factory();
            sousTypeGenrePrestation = sousCode.determineSousCode(periode, isConjoint);

            if (sousTypeGenrePrestation == null) {
                throw new CalculException("Unable to find the sousCodeGenrePresation");
            }
        }

        return sousTypeGenrePrestation;
    }

    /**
     * Remise à zéro des id et spy de l'information compta pour persister
     *
     * @param infoCompta
     */
    private void generateNewInfoComptaForPersist(SimpleInformationsComptabilite infoCompta) {
        infoCompta.setId("");// = pcaToSave.getSimpleInformationsComptabilite();
        infoCompta.setSpy("");// = pcaToSave.getSimpleInformationsComptabilite();
    }

    private String getCodePrestationFromTypePc(String typePc) throws CalculException {

        final Map<String, String> mappage = new HashMap<String, String>() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            {
                put(IPCPCAccordee.CS_TYPE_PC_VIELLESSE, REGenresPrestations.GENRE_110);
                put(IPCPCAccordee.CS_TYPE_PC_SURVIVANT, REGenresPrestations.GENRE_113);
                put(IPCPCAccordee.CS_TYPE_PC_INVALIDITE, REGenresPrestations.GENRE_150);
            }
        };

        if (mappage.containsKey(typePc)) {
            return mappage.get(typePc);
        }
        throw new CalculException("Type PC not found");
    }

    @Override
    public void recupereAnciensPCAccordee(String dateDebutPlage, Droit droit,
                                          Map<String, JadeAbstractSearchModel> cacheDonneesBD) throws PCAccordeeException, JadePersistenceException,
            CalculException, PropertiesException {
        CalculPcaReplaceSearch search = new CalculPcaReplaceSearch();
        search.setForIdDroit(droit.getSimpleDroit().getIdDroit());
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search.setOrderKey(CalculPcaReplaceSearch.ORDER_BY_DATE_DEBUT);
        String date = dateDebutPlage.substring(3);
        // On fait moins un mois pour les jours d'appoints car on a besoin de l'ancienne pca pour les calculer
        if (EPCProperties.GESTION_JOURS_APPOINTS.getBooleanValue()
            //    && PCproperties.isJourAppoint(dateDebutPlage)
        ) {
            date = JadeDateUtil.addMonths(dateDebutPlage, -1).substring(3);
        }
        search.setForDateFin(date);
        search = (CalculPcaReplaceSearch) JadePersistenceManager.search(search);
        cacheDonneesBD.put(ConstantesCalcul.CONTAINER_DONNEES_PCACCORDEES_REPLACED, search);
    }

    /**
     * Permet de reporter le blocage des anciennes pca dans la nouvelle ansi que les retenues
     *
     * @param anciennesPCAccordees
     * @param spas
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private void reporterLaRetenuSiExistant(CalculPcaReplaceSearch anciennesPCAccordees, PCAccordeePlanCalcul pca,
                                            boolean isForDom2R) throws JadePersistenceException, JadeApplicationException,
            JadeApplicationServiceNotAvailableException {
        CalculPcaReplace anciennePCACourante;
        String idPcaOld;

        if (JadeStringUtil.isBlankOrZero(pca.getSimplePCAccordee().getDateFin())) {
            for (JadeAbstractModel absDonnee : anciennesPCAccordees.getSearchResults()) {
                CalculPcaReplace ancienneDonnee = (CalculPcaReplace) absDonnee;
                // On recherche la pca courante (Qui n'a pas de date fin)
                if (JadeStringUtil.isBlankOrZero(ancienneDonnee.getSimplePCAccordee().getDateFin())
                        && IPCPCAccordee.CS_ETAT_PCA_VALIDE.equals(ancienneDonnee.getSimplePCAccordee().getCsEtatPC())) {
                    anciennePCACourante = ancienneDonnee;
                    if (anciennePCACourante.getSimplePrestationsAccordees().getIsRetenues()) {
                        idPcaOld = ancienneDonnee.getSimplePCAccordee().getIdPCAccordee();
                        PcaRetenueSearch search = new PcaRetenueSearch();
                        search.setForIdPca(idPcaOld);
                        search = PegasusServiceLocator.getRetenueService().search(search);
                        BigDecimal montantNewPca = new BigDecimal(pca.getSimplePlanDeCalcul().getMontantPCMensuelle());
                        CheckReportRetenue checkReportRetenue = new CheckReportRetenue(anciennePCACourante,
                                montantNewPca, search, isForDom2R);
                        if (checkReportRetenue.hasRetenue()) {
                            for (JadeAbstractModel model : search.getSearchResults()) {
                                PcaRetenue retenueAncienne = (PcaRetenue) model;
//                                Float montantAVerser = getMontantHome(donneeInterneHomeVersement.getMontantHomes(), 1);
                                Float montantAVerserOld = Float.parseFloat(retenueAncienne.getSimpleRetenue().getMontantRetenuMensuel());
                                if(retenueAncienne.getCsRoleFamillePC().equals(pca.getSimplePCAccordee().getCsRoleBeneficiaire())){
                                    PcaRetenue retenue;
                                    try {
                                        retenue = (PcaRetenue) JadePersistenceUtil.clone(model);
                                    } catch (JadeCloneModelException e) {
                                        throw new PCAccordeeException("Unable to clone this PCA id: "
                                                + pca.getSimplePCAccordee().getIdPCAccordee());
                                    }
                                    retenue.setIdPCAccordee(pca.getSimplePCAccordee().getIdPCAccordee());
                                    retenue.getSimpleRetenue().setDateDebutRetenue(
                                            PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt());
                                    PegasusServiceLocator.getRetenueService().createWithOutCheck(retenue);
                                }

                            }
                        }
//                        } else{
//                            String[] param = new String[3];
//
//                            if (isForDom2R) {
//                                if (!checkReportRetenue.isMontantPresationSuffisantRequerant()) {
//                                    param[0] = new FWCurrency(checkReportRetenue.getSumRetenueRequerant().toString())
//                                            .toStringFormat();
//                                    param[1] = new FWCurrency(pca.getSimplePlanDeCalcul().getMontantPCMensuelle())
//                                            .toStringFormat();
//                                    param[2] = BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
//                                            IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
//                                    JadeThread.logWarn(this.getClass().getName(),
//                                            "pegasus.calcul.persistance.retenu.montantInsufisant", param);
//                                }
//                                if (!checkReportRetenue.isMontantPresationSuffisantConjoint()) {
//                                    param[0] = new FWCurrency(checkReportRetenue.getSumRetenueConjoint().toString())
//                                            .toStringFormat();
//                                    param[1] = new FWCurrency(pca.getSimplePlanDeCalcul().getMontantPCMensuelle())
//                                            .toStringFormat();
//                                    param[2] = BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
//                                            IPCDroits.CS_ROLE_FAMILLE_CONJOINT);
//                                    JadeThread.logWarn(this.getClass().getName(),
//                                            "pegasus.calcul.persistance.retenu.montantInsufisant", param);
//                                }
//                            } else {
//                                param[0] = new FWCurrency(checkReportRetenue.getSumRetenueRequerant().toString())
//                                        .toStringFormat();
//                                param[1] = new FWCurrency(pca.getSimplePlanDeCalcul().getMontantPCMensuelle())
//                                        .toStringFormat();
//                                param[2] = BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
//                                        IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
//                                JadeThread.logWarn(this.getClass().getName(),
//                                        "pegasus.calcul.persistance.retenu.montantInsufisant", param);
//
//                            }
                    }
                }
            }
        }
    }

    private List<CalculPcaReplace> resolvePcaCourrante(CalculPcaReplaceSearch anciennesPCAccordees) {
        List<CalculPcaReplace> list = new ArrayList<CalculPcaReplace>();

        CalculPcaReplace pcaRequerant = null;
        CalculPcaReplace pcaConjoint = null;
        for (JadeAbstractModel absDonnee : anciennesPCAccordees.getSearchResults()) {
            CalculPcaReplace ancienneDonnee = (CalculPcaReplace) absDonnee;

            // On recherche la pca courante (Qui n'a pas de date fin)
            if (JadeStringUtil.isBlankOrZero(ancienneDonnee.getSimplePCAccordee().getDateFin())
                    && IPCPCAccordee.CS_ETAT_PCA_VALIDE.equals(ancienneDonnee.getSimplePCAccordee().getCsEtatPC())) {
                if (IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(ancienneDonnee.getSimplePCAccordee()
                        .getCsRoleBeneficiaire())) {
                    pcaRequerant = ancienneDonnee;
                } else {
                    pcaConjoint = ancienneDonnee;
                }
            }
        }
        list.add(0, pcaRequerant);
        if (pcaConjoint != null) {
            list.add(1, pcaConjoint);
        }
        return list;
    }

    @Override
    public List<PCAccordeePlanCalcul> sauvePCAccordee(Droit droit, PeriodePCAccordee periode,
                                                      CalculPcaReplaceSearch anciennesPCAccordees) throws JadePersistenceException, JadeApplicationException {
        PCAccordeePlanCalcul pca;
        // Création des informations comptable
        List<SimpleInformationsComptabilite> simpleInformationsComptabilite = createInformationsComptabilite(droit,
                periode);

        // création de la nouvelle prestation accordée
        List<SimplePrestationsAccordees> spas = createPrestationAccordee(droit, periode,
                simpleInformationsComptabilite, anciennesPCAccordees);

        String idEntityGroup = null;

        List<PCAccordeePlanCalcul> newPca = new ArrayList<PCAccordeePlanCalcul>();
        // création des nouvelle pcaccordée

        if (periode.getTypeSeparationCC() == TypeSeparationCC.CALCUL_DOM2_PRINCIPALE) {
            pca = this
                    .createPCAccordee(droit, periode, anciennesPCAccordees,
                            spas.get(CalculPersistanceServiceImpl.ID_SIMPLE_PRESTATION_ACCORDEE_REQUERANT),
                            spas.get(CalculPersistanceServiceImpl.ID_SIMPLE_PRESTATION_ACCORDEE_CONJOINT), false,
                            idEntityGroup);
//            reporterLaRetenuSiExistant(anciennesPCAccordees, pca, true);
            newPca.add(pca);
        } else {
            // cherche cas de couple séparé
            if (periode.getTypeSeparationCC() == TypeSeparationCC.CALCUL_SEPARE_MALADIE) {
                idEntityGroup = JadePersistenceManager.incIndentifiant(Compteurs.PC_ACCORDEE_ID_ENTITY_GROUP);
                pca = this.createPCAccordee(droit, periode, anciennesPCAccordees,
                        spas.get(CalculPersistanceServiceImpl.ID_SIMPLE_PRESTATION_ACCORDEE_CONJOINT), true,
                        idEntityGroup);
//                reporterLaRetenuSiExistant(anciennesPCAccordees, pca, false);
                if (periode.getJoursAppointConjoint() != null) {
                    periode.getJoursAppointConjoint().setIdPCAccordee(pca.getId());
                    SimpleJoursAppoint sjaConjoint = PegasusImplServiceLocator.getSimpleJoursAppointService().create(
                            periode.getJoursAppointConjoint());
                }
                newPca.add(pca);
            }
            pca = this.createPCAccordee(droit, periode, anciennesPCAccordees,
                    spas.get(CalculPersistanceServiceImpl.ID_SIMPLE_PRESTATION_ACCORDEE_REQUERANT), false,
                    idEntityGroup);
//            reporterLaRetenuSiExistant(anciennesPCAccordees, pca, false);
            if (periode.getJoursAppointRequerant() != null) {
                periode.getJoursAppointRequerant().setIdPCAccordee(pca.getId());
                periode.getJoursAppointRequerant().setIdPCAccordee(pca.getId());
                SimpleJoursAppoint sjaRequerant = PegasusImplServiceLocator.getSimpleJoursAppointService().create(
                        periode.getJoursAppointRequerant());
            }
            newPca.add(pca);
        }

        return newPca;
    }

    @Override
    public void sauvePCAccordeeToCopie(CalculPcaReplace pcaToSave) throws PCAccordeeException,
            JadePersistenceException, JadeApplicationException {

        SimplePCAccordee simplePcaToSave = pcaToSave.getSimplePCAccordee();
        SimplePlanDeCalcul plandeCalcul = pcaToSave.getSimplePlanDeCalcul();

        // Set id parent, si la pca a elle meme un pcaParent, on set la valeur du parent originel
        if (!JadeStringUtil.isBlankOrZero(pcaToSave.getSimplePCAccordee().getIdPcaParent())) {
            simplePcaToSave.setIdPcaParent(pcaToSave.getSimplePCAccordee().getIdPcaParent());
        } else {
            simplePcaToSave.setIdPcaParent(pcaToSave.getId());
        }

        // TODO Voir pour factor eventuel
        SimplePrestationsAccordees simplePrestToSaveReq = pcaToSave.getSimplePrestationsAccordees();
        SimplePrestationsAccordees simplePrestToSaveCon = pcaToSave.getSimplePrestationsAccordeesConjoint();
        SimpleInformationsComptabilite simpleInfoCompta = pcaToSave.getSimpleInformationsComptabilite();
        SimpleInformationsComptabilite simpleInfoComptaCon = pcaToSave.getSimpleInformationsComptabiliteConjoint();

        // SI conjoint
        if (!JadeStringUtil.isBlankOrZero(simplePcaToSave.getIdPrestationAccordeeConjoint())) {

            // SAuvegarde infoCompta -->REINCOM
            generateNewInfoComptaForPersist(simpleInfoComptaCon);
            generateNewInfoComptaForPersist(simpleInfoCompta);

            simpleInfoCompta = CorvusServiceLocator.getSimpleInformationsComptabiliteService().create(simpleInfoCompta);
            simpleInfoComptaCon = CorvusServiceLocator.getSimpleInformationsComptabiliteService().create(
                    simpleInfoComptaCon);

            // Sauvegarde des prestations-->REPRACC
            simplePrestToSaveCon.setIdInfoCompta(simpleInfoComptaCon.getIdInfoCompta());
            simplePrestToSaveCon.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
            simplePrestToSaveCon.setDateDebutDroit(pcaToSave.getSimplePCAccordee().getDateDebut());
            simplePrestToSaveCon.setDateFinDroit(pcaToSave.getSimplePCAccordee().getDateFin());
            simplePrestToSaveCon.setId("");
            simplePrestToSaveCon = PegasusImplServiceLocator.getSimplePrestatioAccordeeService().create(
                    simplePrestToSaveCon);

            simplePrestToSaveCon.setIsAttenteMajRetenue(false);
            simplePrestToSaveCon.setIsAttenteMajBlocage(false);
            simplePrestToSaveCon.setIsRetenues(false);
            simplePrestToSaveCon.setIsPrestationBloquee(false);

            simplePrestToSaveReq.setId("");
            simplePrestToSaveReq.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
            simplePrestToSaveReq.setIdInfoCompta(simpleInfoCompta.getIdInfoCompta());
            simplePrestToSaveReq.setDateDebutDroit(pcaToSave.getSimplePCAccordee().getDateDebut());
            simplePrestToSaveReq.setDateFinDroit(pcaToSave.getSimplePCAccordee().getDateFin());
            simplePrestToSaveReq.setIsAttenteMajRetenue(false);
            simplePrestToSaveReq.setIsAttenteMajBlocage(false);
            simplePrestToSaveReq.setIsRetenues(false);
            simplePrestToSaveReq.setIsPrestationBloquee(false);
            simplePrestToSaveReq = PegasusImplServiceLocator.getSimplePrestatioAccordeeService().create(
                    simplePrestToSaveReq);
            // sauvegarde de la pca, on set les nouveau id prestations accordees
            simplePcaToSave.setIdPrestationAccordeeConjoint(simplePrestToSaveCon.getIdPrestationAccordee());
            simplePcaToSave.setIdPrestationAccordee(simplePrestToSaveReq.getIdPrestationAccordee());
            // simplePcaToSave = PegasusImplServiceLocator.getSimplePCAccordeeService().create(simplePcaToSave);

        } else {
            // Sauvegarde infoCompta -->REINCOM
            generateNewInfoComptaForPersist(simpleInfoCompta);
            simpleInfoCompta = CorvusServiceLocator.getSimpleInformationsComptabiliteService().create(simpleInfoCompta);

            // prestations
            simplePrestToSaveReq.setId("");
            simplePrestToSaveReq.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
            simplePrestToSaveReq.setIdInfoCompta(simpleInfoCompta.getIdInfoCompta());
            simplePrestToSaveReq.setDateDebutDroit(pcaToSave.getSimplePCAccordee().getDateDebut());
            simplePrestToSaveReq.setDateFinDroit(pcaToSave.getSimplePCAccordee().getDateFin());
            simplePrestToSaveReq.setIsAttenteMajRetenue(false);
            simplePrestToSaveReq.setIsAttenteMajBlocage(false);
            simplePrestToSaveReq.setIsRetenues(false);
            simplePrestToSaveReq.setIsPrestationBloquee(false);
            simplePrestToSaveReq = PegasusImplServiceLocator.getSimplePrestatioAccordeeService().create(
                    simplePrestToSaveReq);
            // sauvegarde de la pca, on set les nouveau id prestations accordees
            simplePcaToSave.setIdPrestationAccordee(simplePrestToSaveReq.getIdPrestationAccordee());
        }

        SimpleJoursAppointSearch joursAppointSearch = new SimpleJoursAppointSearch();
        if (EPCProperties.GESTION_JOURS_APPOINTS.getBooleanValue() && !simplePcaToSave.getIsSupprime()) {
            joursAppointSearch.setForIdPCAccordee(simplePcaToSave.getIdPcaParent());
            joursAppointSearch = PegasusImplServiceLocator.getSimpleJoursAppointService().search(joursAppointSearch);
        }

        // Save pca
        simplePcaToSave.setCsEtatPC(IPCPCAccordee.CS_ETAT_PCA_CALCULE);
        simplePcaToSave.setHasJoursAppoint(joursAppointSearch.getSize() > 0);
        PegasusImplServiceLocator.getSimplePCAccordeeService().create(simplePcaToSave);
        if (joursAppointSearch.getSize() > 0) {
            SimpleJoursAppoint joursAppoint;
            try {
                joursAppoint = (SimpleJoursAppoint) JadePersistenceUtil.clone(joursAppointSearch.getSearchResults()[0]);
            } catch (JadeCloneModelException e) {
                throw new PCAccordeeException("Unable to clone the model simpleJoursAppoint", e);
            }
            joursAppoint.setIdPCAccordee(simplePcaToSave.getId());
            PegasusImplServiceLocator.getSimpleJoursAppointService().create(joursAppoint);
        }

        // Plans de calcul
        plandeCalcul.setIdPCAccordee(simplePcaToSave.getIdPCAccordee());
        plandeCalcul.setResultatCalcul(new String().getBytes());
        plandeCalcul = PegasusImplServiceLocator.getSimplePlanDeCalculService().create(plandeCalcul);

    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.pegasus.business.services.models.calcul.CalculPersistanceService
     * #serialiseDonneesCcXML(ch.globaz.pegasus.businessimpl.utils.calcul. CalculComparatif)
     */
    @Override
    public String serialiseDonneesCcXML(CalculComparatif cc) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLEncoder encoder = new XMLEncoder(out);

        encoder.writeObject(cc.getMontants());
        encoder.close();

        return out.toString();
    }

}
