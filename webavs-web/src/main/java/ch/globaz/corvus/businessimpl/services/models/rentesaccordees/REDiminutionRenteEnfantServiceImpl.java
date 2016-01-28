package ch.globaz.corvus.businessimpl.services.models.rentesaccordees;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.echeances.REPeriodeEcheances;
import globaz.corvus.db.echeances.RERenteJoinDemandeEcheance;
import globaz.corvus.db.rentesaccordees.REEnteteBlocage;
import globaz.corvus.utils.REEcheancesUtils;
import globaz.corvus.utils.enumere.genre.prestations.REGenrePrestationEnum;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.corvus.business.exceptions.models.RERenteJoinPeriodeServiceException;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.IREPeriodeEcheances;
import ch.globaz.corvus.business.models.echeances.IRERenteEcheances;
import ch.globaz.corvus.business.models.echeances.REEnfantADiminuer;
import ch.globaz.corvus.business.models.rentesaccordees.RERenteJoinPeriode;
import ch.globaz.corvus.business.models.rentesaccordees.RERenteJoinPeriodeSearchModel;
import ch.globaz.corvus.business.services.models.rentesaccordees.REDiminutionRenteEnfantService;
import ch.globaz.corvus.businessimpl.services.CorvusAbstractServiceImpl;
import ch.globaz.corvus.process.echeances.analyseur.REAnalyseurEcheances;
import ch.globaz.corvus.process.echeances.analyseur.REAnalyseurEcheancesFactory;
import ch.globaz.corvus.process.echeances.analyseur.modules.REReponseModuleAnalyseEcheance;
import ch.globaz.hera.business.constantes.ISFPeriode;

public class REDiminutionRenteEnfantServiceImpl extends CorvusAbstractServiceImpl implements
        REDiminutionRenteEnfantService {

    private List<REEnfantADiminuer> analyserAvecModuleEcheance(List<RERenteJoinPeriode> rentesJoinPeriodes, String mois) {

        Map<String, List<RERenteJoinPeriode>> renteParTiers = sortRenteJoinPeriodeByTiers(rentesJoinPeriodes);

        REAnalyseurEcheancesFactory analyseurEcheancesFactory = new REAnalyseurEcheancesFactory(
                BSessionUtil.getSessionFromThreadContext());
        List<REEnfantADiminuer> results = new ArrayList<REEnfantADiminuer>();
        try {
            REAnalyseurEcheances analyseur = analyseurEcheancesFactory.getInstance(mois,
                    REAnalyseurEcheancesFactory.TypeAnalyseurEcheances.EcheanceEtudes,
                    REAnalyseurEcheancesFactory.TypeAnalyseurEcheances.Echeance18ans);

            for (String idTiers : renteParTiers.keySet()) {
                boolean addIdTiers = false;
                List<RERenteJoinPeriode> rentesDuTiers = renteParTiers.get(idTiers);

                for (REEnfantADiminuer uneEcheanceDuTiers : convertRenteJoinPeriodeIntoEcheances(rentesDuTiers)) {
                    for (REReponseModuleAnalyseEcheance uneReponse : analyseur.analyserEcheance(uneEcheanceDuTiers)) {
                        switch (uneReponse.getMotif()) {
                            case EcheanceFinEtudes:
                            case Echeance18ans:
                                // case EcheanceEtudesAucunePeriode: ne doit pas sortir sur cette liste !!
                                uneEcheanceDuTiers.getMotifs().add(uneReponse.getMotif());
                                addIdTiers = true;
                                break;
                            default:
                                break;
                        }
                    }
                    if (addIdTiers) {
                        results.add(uneEcheanceDuTiers);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            JadeThread.logError(this.getClass().getName(), "Analyser failure : " + ex.getMessage());
            return null;
        }

        // tri des résultat par ordre alphabétique (nom - prénom)
        Collections.sort(results);
        return results;
    }

    private REPeriodeEcheances buildPeriodeForEcheance(RERenteJoinPeriode renteJoinPeriode) {
        return new REPeriodeEcheances(renteJoinPeriode.getIdPeriode(), renteJoinPeriode.getDateDebutPeriode(),
                renteJoinPeriode.getDateFinPeriode(), renteJoinPeriode.getCsTypePeriode());
    }

    private RERenteJoinDemandeEcheance buildRenteEcheance(RERenteJoinPeriode renteJoinPeriode) {
        RERenteJoinDemandeEcheance uneRente = new RERenteJoinDemandeEcheance();
        uneRente.setIdPrestationAccordee(renteJoinPeriode.getIdPrestationAccordee());
        uneRente.setCodePrestation(renteJoinPeriode.getCodePrestation());
        uneRente.setMontant(renteJoinPeriode.getMontant());
        uneRente.setDateEcheance(renteJoinPeriode.getDateEcheance());
        return uneRente;
    }

    private RERenteJoinPeriodeSearchModel buildSearchModel(String mois) {

        Calendar moisCourant = Calendar.getInstance();
        moisCourant.set(Calendar.MONTH, Integer.parseInt(mois.substring(0, 2)) - 1);
        moisCourant.set(Calendar.YEAR, Integer.parseInt(mois.substring(3)));

        Set<String> codePrestationPourEnfant = new HashSet<String>(REGenrePrestationEnum.groupeEnfant);
        Set<String> csTypesPeriode = new HashSet<String>(Arrays.asList(ISFPeriode.CS_TYPE_PERIODE_ETUDE));
        Set<String> csEtatRenteAccordee = new HashSet<String>(Arrays.asList(IREPrestationAccordee.CS_ETAT_VALIDE,
                IREPrestationAccordee.CS_ETAT_PARTIEL));

        RERenteJoinPeriodeSearchModel searchModel = new RERenteJoinPeriodeSearchModel();
        searchModel.setWhereKey("listerRentePourEnfantFinissantDansMois");

        searchModel.setForCodePrestationIn(codePrestationPourEnfant);
        searchModel.setForCsTypePeriodeIn(csTypesPeriode);
        searchModel.setForCsEtatRenteAccordeeIn(csEtatRenteAccordee);

        searchModel.setForPrestationEnCoursDansMois(mois);

        SimpleDateFormat dateFormater = new SimpleDateFormat("dd.MM.yyyy");
        Calendar datePourAvoir18ans = (Calendar) moisCourant.clone();
        datePourAvoir18ans.add(Calendar.YEAR, -18);
        datePourAvoir18ans.add(Calendar.MONTH, 1);
        datePourAvoir18ans.set(Calendar.DAY_OF_MONTH, 1);
        datePourAvoir18ans.add(Calendar.DAY_OF_MONTH, -1);
        searchModel.setForDateNaissanceUlterieureOuEgaleA(dateFormater.format(datePourAvoir18ans.getTime()));

        Calendar datePrecedantLe25emeAnniversaire = (Calendar) moisCourant.clone();
        datePrecedantLe25emeAnniversaire.add(Calendar.YEAR, -25);
        datePrecedantLe25emeAnniversaire.set(Calendar.DAY_OF_MONTH, 1);
        datePrecedantLe25emeAnniversaire.add(Calendar.DAY_OF_MONTH, -1);
        searchModel.setForDateNaissanceAnterieureOuEgaleA(dateFormater.format(datePrecedantLe25emeAnniversaire
                .getTime()));

        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        return searchModel;
    }

    private List<REEnfantADiminuer> convertRenteJoinPeriodeIntoEcheances(List<RERenteJoinPeriode> resultats) {

        Map<String, REEnfantADiminuer> echeanceParIdTiers = new HashMap<String, REEnfantADiminuer>();
        for (RERenteJoinPeriode renteJoinPeriode : resultats) {
            if (echeanceParIdTiers.containsKey(renteJoinPeriode.getIdTiers())) {
                IREEcheances uneEcheance = echeanceParIdTiers.get(renteJoinPeriode.getIdTiers());

                if (!JadeStringUtil.isBlank(renteJoinPeriode.getIdPeriode())) {
                    boolean containsPeriode = false;
                    for (IREPeriodeEcheances unePeriodeDuTiers : uneEcheance.getPeriodes()) {
                        if (unePeriodeDuTiers.getIdPeriode().equals(renteJoinPeriode.getIdPeriode())) {
                            containsPeriode = true;
                            break;
                        }
                    }
                    if (!containsPeriode) {
                        uneEcheance.getPeriodes().add(buildPeriodeForEcheance(renteJoinPeriode));
                    }
                }

                boolean containsRente = false;
                for (IRERenteEcheances uneRenteDuTiers : uneEcheance.getRentesDuTiers()) {
                    if (uneRenteDuTiers.getIdPrestationAccordee().equals(renteJoinPeriode.getIdPrestationAccordee())) {
                        containsRente = true;
                        break;
                    }
                }
                if (!containsRente) {
                    uneEcheance.getRentesDuTiers().add(buildRenteEcheance(renteJoinPeriode));
                }
            } else {
                REEnfantADiminuer uneEcheance = new REEnfantADiminuer();

                uneEcheance.setIdTiers(renteJoinPeriode.getIdTiers());
                uneEcheance.setNomTiers(renteJoinPeriode.getNomTiers());
                uneEcheance.setPrenomTiers(renteJoinPeriode.getPrenomTiers());
                uneEcheance.setCsSexeTiers(renteJoinPeriode.getSexe());
                uneEcheance.setDateNaissanceTiers(renteJoinPeriode.getDateNaissance());
                uneEcheance.setDateDecesTiers(renteJoinPeriode.getDateDeces());

                if (!JadeStringUtil.isBlank(renteJoinPeriode.getIdPeriode())) {
                    uneEcheance.getPeriodes().add(buildPeriodeForEcheance(renteJoinPeriode));
                }

                uneEcheance.getRentesDuTiers().add(buildRenteEcheance(renteJoinPeriode));

                echeanceParIdTiers.put(uneEcheance.getIdTiers(), uneEcheance);
            }
        }
        return new ArrayList<REEnfantADiminuer>(echeanceParIdTiers.values());
    }

    @Override
    public List<REEnfantADiminuer> getRenteEnfantDontPeriodeFiniDansMois(String mois)
            throws RERenteJoinPeriodeServiceException, JadePersistenceException {

        if (!JadeDateUtil.isGlobazDateMonthYear(mois)) {
            throw new RERenteJoinPeriodeServiceException(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "ERREUR_MOIS_TRAITEMENT_INVALIDE"));
        }

        RERenteJoinPeriodeSearchModel searchModel = buildSearchModel(mois);
        searchModel = (RERenteJoinPeriodeSearchModel) JadePersistenceManager.search(searchModel);

        BSession session = BSessionUtil.getSessionFromThreadContext();
        if (session == null) {
            throw new RERenteJoinPeriodeServiceException("Unable to retrieve current context session");
        }
        List<RERenteJoinPeriode> resultats = new ArrayList<RERenteJoinPeriode>();
        for (JadeAbstractModel unResultat : searchModel.getSearchResults()) {
            RERenteJoinPeriode renteJoinPeriode = (RERenteJoinPeriode) unResultat;

            if (!hasCodeCasSpecial02ou05(renteJoinPeriode)) {
                try {
                    // Récupération de l'entête de blocage (si existant) pour tester si la prestation est bloquée
                    REEnteteBlocage enteteBlocage = null;
                    if (!JadeStringUtil.isBlankOrZero(renteJoinPeriode.getIdEnteteBlocage())) {
                        enteteBlocage = new REEnteteBlocage();
                        enteteBlocage.setSession(session);
                        enteteBlocage.setIdEnteteBlocage(renteJoinPeriode.getIdEnteteBlocage());
                        enteteBlocage.retrieve();
                        if (enteteBlocage.isNew()) {
                            enteteBlocage = null;
                        }
                    }
                    if (!REEcheancesUtils.isPrestationBloquee(renteJoinPeriode.getIsPrestationBloqueeBoolean(),
                            renteJoinPeriode.getIdEnteteBlocage(), enteteBlocage)) {
                        resultats.add(renteJoinPeriode);
                    }
                } catch (Exception exception) {
                    throw new RERenteJoinPeriodeServiceException("Exception thrown during process execution ",
                            exception);
                }
            }
        }
        return analyserAvecModuleEcheance(resultats, mois);
    }

    private boolean hasCodeCasSpecial02ou05(RERenteJoinPeriode renteJoinPeriode) {
        return isCodeCasSpecial02ou05(renteJoinPeriode.getCodeCasSpeciaux1())
                || isCodeCasSpecial02ou05(renteJoinPeriode.getCodeCasSpeciaux2())
                || isCodeCasSpecial02ou05(renteJoinPeriode.getCodeCasSpeciaux3())
                || isCodeCasSpecial02ou05(renteJoinPeriode.getCodeCasSpeciaux4())
                || isCodeCasSpecial02ou05(renteJoinPeriode.getCodeCasSpeciaux5());
    }

    private boolean isCodeCasSpecial02ou05(String codeCasSpecial) {
        return "02".equals(codeCasSpecial) || "05".equals(codeCasSpecial);
    }

    private Map<String, List<RERenteJoinPeriode>> sortRenteJoinPeriodeByTiers(
            List<RERenteJoinPeriode> rentesJoinPeriodes) {

        Map<String, List<RERenteJoinPeriode>> rentesJoinPeriodesTrieesParIdTiers = new HashMap<String, List<RERenteJoinPeriode>>();

        for (RERenteJoinPeriode uneRenteJoinPeriode : rentesJoinPeriodes) {
            if (rentesJoinPeriodesTrieesParIdTiers.containsKey(uneRenteJoinPeriode.getIdTiers())) {
                rentesJoinPeriodesTrieesParIdTiers.get(uneRenteJoinPeriode.getIdTiers()).add(uneRenteJoinPeriode);
            } else {
                List<RERenteJoinPeriode> list = new ArrayList<RERenteJoinPeriode>();
                list.add(uneRenteJoinPeriode);
                rentesJoinPeriodesTrieesParIdTiers.put(uneRenteJoinPeriode.getIdTiers(), list);
            }
        }

        return rentesJoinPeriodesTrieesParIdTiers;
    }
}
