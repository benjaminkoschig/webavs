package globaz.helios.db.comptes;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JAUtil;
import globaz.helios.api.consolidation.ICGConsolidationSolde;
import globaz.helios.db.comptes.helper.CGSoldeDataContainer;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Insérez la description du type ici. Date de création : (26.11.2002 16:26:31)
 * 
 * @author: Administrator
 */
public class CGSolde extends BEntity implements ICGConsolidationSolde {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELD_AVOIR = "AVOIR";

    public static final String FIELD_AVOIRMONNAIE = "AVOIRMONNAIE";
    public static final String FIELD_AVOIRPROVIMONNAIE = "AVOIRPROVIMONNAIE";
    public static final String FIELD_AVOIRPROVISOIRE = "AVOIRPROVISOIRE";
    public static final String FIELD_BUDGET = "BUDGET";
    public static final String FIELD_DOIT = "DOIT";
    public static final String FIELD_DOITMONNAIE = "DOITMONNAIE";
    public static final String FIELD_DOITPROVIMONNAIE = "DOITPROVIMONNAIE";
    public static final String FIELD_DOITPROVISOIRE = "DOITPROVISOIRE";
    public static final String FIELD_ESTPERIODE = "ESTPERIODE";
    public static final String FIELD_IDCENTRECHARGE = "IDCENTRECHARGE";
    public static final String FIELD_IDCOMPTE = "IDCOMPTE";
    public static final String FIELD_IDEXERCOMPTABLE = "IDEXERCOMPTABLE";
    public static final String FIELD_IDMANDAT = "IDMANDAT";
    public static final String FIELD_IDPERIODECOMPTABLE = "IDPERIODECOMPTABLE";
    public static final String FIELD_IDSOLDE = "IDSOLDE";
    public static final String FIELD_SOLDE = "SOLDE";
    public static final String FIELD_SOLDEMONNAIE = "SOLDEMONNAIE";
    public static final String FIELD_SOLDEPROVIMONNAIE = "SOLDEPROVIMONNAIE";
    public static final String FIELD_SOLDEPROVISOIRE = "SOLDEPROVISOIRE";
    public static final String FIELD_TAUXTVA = "TAUXTVA";
    public static final String TABLE_NAME = "CGSOLDP";

    private static FWCurrency computeSoldeCumule(boolean isMonnaieEtrangere, String idExerComptable, String idCompte,
            String idPeriode, String idCentreCharge, BSession session, boolean isProvisoire) throws Exception {

        // Solde actuel
        globaz.framework.util.FWCurrency solde = new globaz.framework.util.FWCurrency();

        if ("0".equals(idPeriode)) {
            // Préparation du manager pour récupérer les soldes de l'exercice
            CGSoldeManager soldeManager = new CGSoldeManager();
            soldeManager.setSession(session);
            soldeManager.setForIdExerComptable(idExerComptable);
            soldeManager.setForIdCompte(idCompte);
            soldeManager.setForIdCentreCharge(idCentreCharge);
            soldeManager.setForEstPeriode(new Boolean(false));
            soldeManager.find(BManager.SIZE_NOLIMIT);
            if (soldeManager.size() > 1) {
                throw (new Exception("Erreur computeSoldeCumule : plusieurs soldes pour un exercice comptable."));
            }
            if (soldeManager.size() == 1) {
                if (isProvisoire) {
                    if (isMonnaieEtrangere) {
                        solde.add(((CGSolde) soldeManager.getEntity(0)).getSoldeProvisoireMonnaie());
                    } else {
                        solde.add(((CGSolde) soldeManager.getEntity(0)).getSoldeProvisoire());
                    }

                } else {
                    if (isMonnaieEtrangere) {
                        solde.add(((CGSolde) soldeManager.getEntity(0)).getSoldeMonnaie());
                    } else {
                        solde.add(((CGSolde) soldeManager.getEntity(0)).getSolde());
                    }
                }
            }

        } else {

            // Récupération de la période comptable liée au solde
            CGPeriodeComptable periode = new CGPeriodeComptable();
            periode.setSession(session);
            periode.setIdPeriodeComptable(idPeriode);
            periode.retrieve();
            if (periode.isNew()) {
                throw (new Exception(
                        "Erreur computeSoldeCumule : impossible de récupérer la période comptable fournie."));
            }

            // Récupération de l'exercice comptable
            CGExerciceComptable exercice = new CGExerciceComptable();
            exercice.setSession(session);
            exercice.setIdExerciceComptable(idExerComptable);
            exercice.retrieve();
            if (exercice.isNew()) {
                throw (new Exception(
                        "Erreur computeSoldeCumule : impossible de récupérer l'exercice comptable fournie."));
            }

            // Récupération des périodes comptables précédentes, incluant la
            // periode courante :
            List listPeriodeComptables = CGSolde.getListIdPreviousPeriodeComptables(session, idExerComptable, periode,
                    null);
            listPeriodeComptables.add(periode.getIdPeriodeComptable());

            // Préparation du manager pour récupérer les soldes des périodes
            // comptables précédentes
            CGSoldeManager soldeManager = new CGSoldeManager();
            soldeManager.setSession(session);
            soldeManager.setForIdExerComptable(idExerComptable);
            soldeManager.setForIdCompte(idCompte);
            soldeManager.setForIdCentreCharge(idCentreCharge);
            soldeManager.setForEstPeriode(new Boolean(true));

            for (Iterator iterator = listPeriodeComptables.iterator(); iterator.hasNext();) {
                String idPeriodeComtable = (String) iterator.next();

                soldeManager.setForIdPeriodeComptable(idPeriodeComtable);
                soldeManager.find(BManager.SIZE_NOLIMIT);
                if (soldeManager.size() > 1) {
                    throw (new Exception(
                            "Erreur computeSoldeCumule : plusieurs soldes pour une période comptable et un compte donné."));
                }
                if (soldeManager.size() == 1) {
                    if (isProvisoire) {
                        if (isMonnaieEtrangere) {
                            solde.add(((CGSolde) soldeManager.getEntity(0)).getSoldeProvisoireMonnaie());
                        } else {
                            solde.add(((CGSolde) soldeManager.getEntity(0)).getSoldeProvisoire());
                        }
                    } else {
                        if (isMonnaieEtrangere) {
                            solde.add(((CGSolde) soldeManager.getEntity(0)).getSoldeMonnaie());
                        } else {
                            solde.add(((CGSolde) soldeManager.getEntity(0)).getSolde());
                        }
                    }
                }
            }
        }
        return solde;
    }

    public static FWCurrency computeSoldeCumule(String idExerComptable, String idCompte, String idPeriode,
            String idCentreCharge, BSession session, boolean isProvisoire) throws Exception {
        return CGSolde.computeSoldeCumule(false, idExerComptable, idCompte, idPeriode, idCentreCharge, session,
                isProvisoire);
    }

    /**
     * Method computeSoldeCumuleData. Si le budget égal à zéro, le montant sera calculé au prorata par rapport au buget
     * annuel
     * 
     * @param idExerComptable
     * @param idCompte
     * @param idPeriode
     * @param session
     * @return CGSoldeDataContainer
     * @throws Exception
     */
    public static CGSoldeDataContainer computeSoldeCumuleData(String idExerComptable, String idCompte,
            String idPeriode, String idCentreCharge, BSession session) throws Exception {

        CGSoldeDataContainer result = new CGSoldeDataContainer();

        // Préparation du manager pour récupérer les soldes de l'exercice
        CGSoldeManager soldeManager = new CGSoldeManager();
        soldeManager.setSession(session);
        soldeManager.setForIdExerComptable(idExerComptable);
        soldeManager.setForIdCompte(idCompte);
        soldeManager.setForIdCentreCharge(idCentreCharge);
        soldeManager.setForEstPeriode(new Boolean(false));
        soldeManager.find(2);
        if (soldeManager.size() > 1) {
            throw (new Exception("Erreur computeSoldeCumuleData : " + soldeManager.size()
                    + " solde(s) pour un exercice comptable, id compte = " + idCompte + " id exercice = "
                    + idExerComptable));
        }

        FWCurrency budgetAnnuel = new FWCurrency(0);
        if (soldeManager.size() == 1) {
            budgetAnnuel = new FWCurrency(((CGSolde) soldeManager.getEntity(0)).getBudget());
        }

        if ("0".equals(idPeriode)) {
            if (soldeManager.size() == 1) {
                CGSolde entity = (CGSolde) soldeManager.getEntity(0);
                result.setAvoir(new FWCurrency(entity.getAvoir()));
                result.setAvoirMonnaie(new FWCurrency(entity.getAvoirMonnaie()));
                result.setAvoirProvisoire(new FWCurrency(entity.getAvoirProvisoire()));
                result.setAvoirProvisoireMonnaie(new FWCurrency(entity.getAvoirProvisoireMonnaie()));

                result.setDoit(new FWCurrency(entity.getDoit()));
                result.setDoitMonnaie(new FWCurrency(entity.getDoitMonnaie()));
                result.setDoitProvisoire(new FWCurrency(entity.getDoitProvisoire()));
                result.setDoitProvisoireMonnaie(new FWCurrency(entity.getDoitProvisoireMonnaie()));

                result.setSolde(new FWCurrency(entity.getSolde()));
                result.setSoldeMonnaie(new FWCurrency(entity.getSoldeMonnaie()));
                result.setSoldeProvisoire(new FWCurrency(entity.getSoldeProvisoire()));
                result.setSoldeProvisoireMonnaie(new FWCurrency(entity.getSoldeProvisoireMonnaie()));

                result.setBudget(budgetAnnuel);
            }

        } else {

            // Récupération de la période comptable liée au solde
            CGPeriodeComptable periode = new CGPeriodeComptable();
            periode.setSession(session);
            periode.setIdPeriodeComptable(idPeriode);
            periode.retrieve();
            if (periode.isNew()) {
                throw (new Exception(
                        "Erreur computeSoldeCumule : impossible de récupérer la période comptable fournie."));
            }

            // Récupération de l'exercice comptable
            CGExerciceComptable exercice = new CGExerciceComptable();
            exercice.setSession(session);
            exercice.setIdExerciceComptable(idExerComptable);
            exercice.retrieve();
            if (exercice.isNew()) {
                throw (new Exception(
                        "Erreur computeSoldeCumule : impossible de récupérer l'exercice comptable fournie."));
            }

            // Récupération des périodes comptables précédentes, incluant la
            // periode courante :
            List listPeriodeComptables = CGSolde.getListPreviousPeriodeComptables(session, idExerComptable, periode);
            listPeriodeComptables.add(periode);

            // Préparation du manager pour récupérer les soldes des périodes
            // comptables précédentes
            soldeManager = new CGSoldeManager();
            soldeManager.setSession(session);
            soldeManager.setForIdExerComptable(idExerComptable);
            soldeManager.setForIdCompte(idCompte);
            soldeManager.setForIdCentreCharge(idCentreCharge);
            soldeManager.setForEstPeriode(new Boolean(true));

            for (Iterator iterator = listPeriodeComptables.iterator(); iterator.hasNext();) {
                CGPeriodeComptable periodePrec = (CGPeriodeComptable) iterator.next();

                soldeManager.setForIdPeriodeComptable(periodePrec.getIdPeriodeComptable());
                soldeManager.find(BManager.SIZE_NOLIMIT);
                if (soldeManager.size() > 1) {
                    throw (new Exception(
                            "Erreur computeSoldeCumule : plusieurs soldes pour une période comptable et un compte donné."));
                }
                if (soldeManager.size() == 1) {
                    CGSolde entity = (CGSolde) soldeManager.getEntity(0);

                    result.addAvoir(entity.getAvoir());
                    result.addAvoirMonnaie(entity.getAvoirMonnaie());
                    result.addAvoirProvisoire(entity.getAvoirProvisoire());
                    result.addAvoirProvisoireMonnaie(entity.getAvoirProvisoireMonnaie());

                    result.addDoit(entity.getDoit());
                    result.addDoitMonnaie(entity.getDoitMonnaie());
                    result.addDoitProvisoire(entity.getDoitProvisoire());
                    result.addDoitProvisoireMonnaie(entity.getDoitProvisoireMonnaie());

                    result.addSolde(entity.getSolde());
                    result.addSoldeMonnaie(entity.getSoldeMonnaie());
                    result.addSoldeProvisoire(entity.getSoldeProvisoire());
                    result.addSoldeProvisoireMonnaie(entity.getSoldeProvisoireMonnaie());

                    // Pas de budget specifie pour la periode -> calcul d'une
                    // moyenne au prorata
                    if (entity.getBudget() != null) {
                        result.addBudget(entity.getBudget());
                    }
                }
            }

            // Le budget est calcul au prorata du budget annuel uniquement si
            // aucun des budgets mensuel
            // n'a été renseignée, c'est à dire somme des budgets == 0
            // On se base sur 360 jours par année (30 jours par mois)
            if (result.getBudget() == null || (new FWCurrency(0)).equals(result.getBudget())) {
                FWCurrency tmp = CGSolde.getBudgetProRata(exercice.getDateDebut(), periode.getDateFin(), 360,
                        budgetAnnuel);
                if (tmp == null) {
                    result.setBudget(null);
                } else {
                    result.setBudget(new FWCurrency(tmp.toString()));
                }
            }
        }
        return result;
    }

    /**
     * Method computeSoldeCumuleDataUntilDate.
     * 
     * @deprecated Si le budget égal à zéro, le montant sera calculé au prorata par rapport au buget annuel
     * @param idExerComptable
     * @param idCompte
     * @param date
     * @param session
     * @return CGSoldeDataContainer
     * @throws Exception
     */
    @Deprecated
    public static CGSoldeDataContainer computeSoldeCumuleDataAtDate(String idExerComptable, String idCompte,
            String date, String idCentreCharge, BSession session) throws Exception {

        CGSoldeDataContainer result = new CGSoldeDataContainer();

        // Préparation du manager pour récupérer les soldes de l'exercice
        CGSoldeManager soldeManager = new CGSoldeManager();
        soldeManager.setSession(session);
        soldeManager.setForIdExerComptable(idExerComptable);
        soldeManager.setForIdCompte(idCompte);
        soldeManager.setForIdCentreCharge(idCentreCharge);
        soldeManager.setForEstPeriode(new Boolean(false));
        soldeManager.find(BManager.SIZE_NOLIMIT);
        if (soldeManager.size() > 1) {
            throw (new Exception("Erreur computeSoldeCumuleData : " + soldeManager.size()
                    + " solde(s) pour un exercice comptable, id compte = " + idCompte + " id exercice = "
                    + idExerComptable));
        }

        FWCurrency budgetAnnuel = new FWCurrency(0);
        if (soldeManager.size() == 1) {
            budgetAnnuel = new FWCurrency(((CGSolde) soldeManager.getEntity(0)).getBudget());
        }

        // Récupération de l'exercice comptable
        CGExerciceComptable exercice = new CGExerciceComptable();
        exercice.setSession(session);
        exercice.setIdExerciceComptable(idExerComptable);
        exercice.retrieve();
        if (exercice.isNew()) {
            throw (new Exception("Erreur computeSoldeCumule : impossible de récupérer l'exercice comptable fournie."));
        }

        // Récupération des périodes comptables précédentes, incluant la periode
        // courante :
        List listPeriodeComptables = CGSolde.getListPeriodeComptablesUntilDate(session, idExerComptable, date);

        for (Iterator iterator = listPeriodeComptables.iterator(); iterator.hasNext();) {
            CGPeriodeComptable periode = (CGPeriodeComptable) iterator.next();

            JACalendarGregorian calendar = new JACalendarGregorian();

            // La période comprend la date passée en paramètre
            // -> le calcul du solde doit se faire en additionnant les
            // mouvements effectue sur le compte,
            // jusqu'à la date donnée.
            if ((calendar.compare(periode.getDateDebut(), date) == JACalendar.COMPARE_FIRSTLOWER || calendar.compare(
                    periode.getDateDebut(), date) == JACalendar.COMPARE_EQUALS)
                    && (calendar.compare(periode.getDateFin(), date) == JACalendar.COMPARE_SECONDUPPER || calendar
                            .compare(periode.getDateFin(), date) == JACalendar.COMPARE_EQUALS)) {

                CGExtendedEcritureManager ecriManager = new CGExtendedEcritureManager();
                ecriManager.setSession(session);
                ecriManager.setForIdCompte(idCompte);
                ecriManager.setForIdExerciceComptable(idExerComptable);
                ecriManager.setForIdListePeriodeComptable(periode.getIdPeriodeComptable());
                ecriManager.setForDateFin(date);
                ecriManager.setForIsActive(new Boolean(true));
                ecriManager.find(BManager.SIZE_NOLIMIT);

                for (int l = 0; l < ecriManager.size(); l++) {
                    CGExtendedEcriture ecriture = (CGExtendedEcriture) ecriManager.getEntity(l);
                    if (ecriture.isDoit()) {
                        // Si écriture définitive, le total des mouvements
                        // provisoire doivent tout de même
                        // être mis à jours.
                        if (!ecriture.isProvisoire().booleanValue()) {
                            result.addDoit(ecriture.getMontant());
                            result.addDoitMonnaie(ecriture.getMontantMonnaie());
                        }
                        result.addDoitProvisoire(ecriture.getMontant());
                        result.addDoitProvisoireMonnaie(ecriture.getMontantMonnaie());
                    } else {
                        // Si écriture définitive, le total des mouvements
                        // provisoire doivent tout de même
                        // être mis à jours.
                        if (!ecriture.isProvisoire().booleanValue()) {
                            result.addAvoir(ecriture.getMontant());
                            result.addAvoirMonnaie(ecriture.getMontantMonnaie());
                        }
                        result.addAvoirProvisoire(ecriture.getMontant());
                        result.addAvoirProvisoireMonnaie(ecriture.getMontantMonnaie());
                    }
                    if (!ecriture.isProvisoire().booleanValue()) {
                        result.addSolde(ecriture.getMontant());
                        result.addSoldeMonnaie(ecriture.getMontantMonnaie());
                    }
                    result.addSoldeProvisoire(ecriture.getMontant());
                    result.addSoldeProvisoireMonnaie(ecriture.getMontantMonnaie());
                }

                // Calcul du budget de la période p, au prorata des nombres de
                // jours
                // (assert : 1 mois = 30jours)
                soldeManager = new CGSoldeManager();
                soldeManager.setSession(session);
                soldeManager.setForIdCompte(idCompte);
                soldeManager.setForIdCentreCharge(idCentreCharge);
                soldeManager.setForIdExerComptable(idExerComptable);
                soldeManager.setForIdPeriodeComptable(periode.getIdPeriodeComptable());
                soldeManager.setForEstPeriode(new Boolean(true));
                soldeManager.find(2);
                if (soldeManager.size() > 1) {
                    throw (new Exception("Erreur computeSoldeCumuleDataAtDate : " + soldeManager.size()
                            + " solde(s) pour un exercice comptable, id compte = " + idCompte + " id exercice = "
                            + idExerComptable));
                }

                if (soldeManager.size() == 1) {
                    CGSolde solde = (CGSolde) soldeManager.getEntity(0);

                    // calcul d'une moyenne au prorata
                    if (solde.getBudget() != null) {

                        int nbrDays;
                        if (CGPeriodeComptable.CS_MENSUEL.equals(periode.getIdTypePeriode())) {
                            nbrDays = 30;
                        } else if (CGPeriodeComptable.CS_TRIMESTRIEL.equals(periode.getIdTypePeriode())) {
                            nbrDays = 90;
                        } else if (CGPeriodeComptable.CS_SEMESTRIEL.equals(periode.getIdTypePeriode())) {
                            nbrDays = 180;
                        } else {
                            nbrDays = 360;
                        }

                        // Le budget est calcul au prorata du budget selon la
                        // periode
                        FWCurrency tmp = CGSolde.getBudgetProRata(periode.getDateDebut(), date, nbrDays,
                                new FWCurrency(solde.getBudget()));
                        if (tmp != null) {
                            result.addBudget(tmp.toString());
                        }
                    }
                }
            } else {
                // Préparation du manager pour récupérer les soldes des périodes
                // comptables
                soldeManager = new CGSoldeManager();
                soldeManager.setSession(session);
                soldeManager.setForIdExerComptable(idExerComptable);
                soldeManager.setForIdCompte(idCompte);
                soldeManager.setForIdCentreCharge(idCentreCharge);
                soldeManager.setForEstPeriode(new Boolean(true));
                soldeManager.setForIdPeriodeComptable(periode.getIdPeriodeComptable());
                soldeManager.find(2);

                if (soldeManager.size() > 1) {
                    throw (new Exception(
                            "Erreur computeSoldeCumule : plusieurs soldes pour une période comptable et un compte donné."));
                }
                if (soldeManager.size() == 1) {

                    CGSolde entity = (CGSolde) soldeManager.getEntity(0);

                    result.addAvoir(entity.getAvoir());
                    result.addAvoirMonnaie(entity.getAvoirMonnaie());
                    result.addAvoirProvisoire(entity.getAvoirProvisoire());
                    result.addAvoirProvisoireMonnaie(entity.getAvoirProvisoireMonnaie());

                    result.addDoit(entity.getDoit());
                    result.addDoitMonnaie(entity.getDoitMonnaie());
                    result.addDoitProvisoire(entity.getDoitProvisoire());
                    result.addDoitProvisoireMonnaie(entity.getDoitProvisoireMonnaie());

                    result.addSolde(entity.getSolde());
                    result.addSoldeMonnaie(entity.getSoldeMonnaie());
                    result.addSoldeProvisoire(entity.getSoldeProvisoire());
                    result.addSoldeProvisoireMonnaie(entity.getSoldeProvisoireMonnaie());

                    // Pas de budget specifie pour la periode -> calcul d'une
                    // moyenne au prorata
                    if (entity.getBudget() != null) {
                        result.addBudget(entity.getBudget());
                    }
                }
            }
        }

        // Le budget est calcul au prorata du budget annuel uniquement si aucun
        // des budgets mensuel
        // n'a été renseignée, c'est à dire somme des budgets == 0
        // On se base sur 360 jours par année (30 jours par mois)
        if (result.getBudget() == null || (new FWCurrency(0)).equals(result.getBudget())) {
            FWCurrency tmp = CGSolde.getBudgetProRata(exercice.getDateDebut(), date, 360, budgetAnnuel);
            if (tmp == null) {
                result.setBudget(null);
            } else {
                result.setBudget(new FWCurrency(tmp.toString()));
            }
        }
        return result;
    }

    /**
     * Method computeSoldeCumuleDataUntilDate. Si le budget égal à zéro, le montant sera calculé au prorata par rapport
     * au buget annuel
     * 
     * @param idExerComptable
     * @param idCompte
     * @param date
     * @param session
     * @return CGSoldeDataContainer
     * @throws Exception
     */

    public static CGSoldeDataContainer computeSoldeCumuleDataAtDate(String idExerComptable, String idPeriode,
            String idCompte, String date, String idCentreCharge, BSession session) throws Exception {

        CGSoldeDataContainer result = new CGSoldeDataContainer();

        // Préparation du manager pour récupérer les soldes de l'exercice
        CGSoldeManager soldeManager = new CGSoldeManager();
        soldeManager.setSession(session);
        soldeManager.setForIdExerComptable(idExerComptable);
        soldeManager.setForIdCompte(idCompte);
        soldeManager.setForIdCentreCharge(idCentreCharge);
        soldeManager.setForEstPeriode(new Boolean(false));
        soldeManager.find(BManager.SIZE_NOLIMIT);
        if (soldeManager.size() > 1) {
            throw (new Exception("Erreur computeSoldeCumuleData : " + soldeManager.size()
                    + " solde(s) pour un exercice comptable, id compte = " + idCompte + " id exercice = "
                    + idExerComptable));
        }

        FWCurrency budgetAnnuel = new FWCurrency(0);
        if (soldeManager.size() == 1) {
            budgetAnnuel = new FWCurrency(((CGSolde) soldeManager.getEntity(0)).getBudget());
        }

        // Récupération de l'exercice comptable
        CGExerciceComptable exercice = new CGExerciceComptable();
        exercice.setSession(session);
        exercice.setIdExerciceComptable(idExerComptable);
        exercice.retrieve();
        if (exercice.isNew()) {
            throw (new Exception("Erreur computeSoldeCumule : impossible de récupérer l'exercice comptable fournie."));
        }

        if (JadeStringUtil.isIntegerEmpty(idPeriode) && JAUtil.isDateEmpty(date)) {
            if (soldeManager.size() == 1) {
                CGSolde entity = (CGSolde) soldeManager.getEntity(0);
                result.setAvoir(new FWCurrency(entity.getAvoir()));
                result.setAvoirMonnaie(new FWCurrency(entity.getAvoirMonnaie()));
                result.setAvoirProvisoire(new FWCurrency(entity.getAvoirProvisoire()));
                result.setAvoirProvisoireMonnaie(new FWCurrency(entity.getAvoirProvisoireMonnaie()));

                result.setDoit(new FWCurrency(entity.getDoit()));
                result.setDoitMonnaie(new FWCurrency(entity.getDoitMonnaie()));
                result.setDoitProvisoire(new FWCurrency(entity.getDoitProvisoire()));
                result.setDoitProvisoireMonnaie(new FWCurrency(entity.getDoitProvisoireMonnaie()));

                result.setSolde(new FWCurrency(entity.getSolde()));
                result.setSoldeMonnaie(new FWCurrency(entity.getSoldeMonnaie()));
                result.setSoldeProvisoire(new FWCurrency(entity.getSoldeProvisoire()));
                result.setSoldeProvisoireMonnaie(new FWCurrency(entity.getSoldeProvisoireMonnaie()));

                result.setBudget(budgetAnnuel);
            }

        } else {

            List listPeriodeComptables = null;

            // Date == date - 1 jours, car l'on prend le solde cumulé jusqu'au
            // jours d'avant.
            JACalendar calendar = new JACalendarGregorian();
            JADate dayBefore = calendar.addDays(new JADate(date), -1);
            date = dayBefore.toStr(".");

            if (JadeStringUtil.isIntegerEmpty(idPeriode)) {
                // Récupération des périodes comptables précédentes, avec la
                // periode courante :

                listPeriodeComptables = CGSolde.getListPeriodeComptablesUntilDate(session, idExerComptable, date);
            } else {
                // Récupération de la période comptable liée au solde
                CGPeriodeComptable periode = new CGPeriodeComptable();
                periode.setSession(session);
                periode.setIdPeriodeComptable(idPeriode);
                periode.retrieve();
                if (periode.isNew()) {
                    throw (new Exception(
                            "Erreur computeSoldeCumule : impossible de récupérer la période comptable fournie."));
                }

                // Récupération des périodes comptables précédentes, incluant la
                // periode courante :
                listPeriodeComptables = CGSolde.getListPreviousPeriodeComptables(session, idExerComptable, periode);
                listPeriodeComptables.add(periode);
            }

            for (Iterator iterator = listPeriodeComptables.iterator(); iterator.hasNext();) {
                CGPeriodeComptable periode = (CGPeriodeComptable) iterator.next();

                // La période comprend la date passée en paramètre
                // -> le calcul du solde doit se faire en additionnant les
                // mouvements effectue sur le compte,
                // jusqu'à la date donnée.
                if (!JAUtil.isDateEmpty(date)
                        && ((calendar.compare(periode.getDateDebut(), date) == JACalendar.COMPARE_FIRSTLOWER || calendar
                                .compare(periode.getDateDebut(), date) == JACalendar.COMPARE_EQUALS) && (calendar
                                .compare(periode.getDateFin(), date) == JACalendar.COMPARE_FIRSTUPPER || calendar
                                .compare(periode.getDateFin(), date) == JACalendar.COMPARE_EQUALS))) {

                    CGExtendedEcritureManager ecriManager = new CGExtendedEcritureManager();
                    ecriManager.setSession(session);
                    ecriManager.setForIdCompte(idCompte);
                    ecriManager.setForIdExerciceComptable(idExerComptable);
                    ecriManager.setForIdListePeriodeComptable(periode.getIdPeriodeComptable());
                    ecriManager.setForDateFin(date);
                    ecriManager.setForIsActive(new Boolean(true));
                    ecriManager.find(BManager.SIZE_NOLIMIT);

                    for (int l = 0; l < ecriManager.size(); l++) {
                        CGExtendedEcriture ecriture = (CGExtendedEcriture) ecriManager.getEntity(l);
                        if (ecriture.isDoit()) {
                            // Si écriture définitive, le total des mouvements
                            // provisoire doivent tout de même
                            // être mis à jours.
                            if (!ecriture.isProvisoire().booleanValue()) {
                                result.addDoit(ecriture.getMontant());
                                result.addDoitMonnaie(ecriture.getMontantMonnaie());
                            }
                            result.addDoitProvisoire(ecriture.getMontant());
                            result.addDoitProvisoireMonnaie(ecriture.getMontantMonnaie());
                        } else {
                            // Si écriture définitive, le total des mouvements
                            // provisoire doivent tout de même
                            // être mis à jours.
                            if (!ecriture.isProvisoire().booleanValue()) {
                                result.addAvoir(ecriture.getMontant());
                                result.addAvoirMonnaie(ecriture.getMontantMonnaie());
                            }
                            result.addAvoirProvisoire(ecriture.getMontant());
                            result.addAvoirProvisoireMonnaie(ecriture.getMontantMonnaie());
                        }
                        if (!ecriture.isProvisoire().booleanValue()) {
                            result.addSolde(ecriture.getMontant());
                            result.addSoldeMonnaie(ecriture.getMontantMonnaie());
                        }
                        result.addSoldeProvisoire(ecriture.getMontant());
                        result.addSoldeProvisoireMonnaie(ecriture.getMontantMonnaie());
                    }

                    // Calcul du budget de la période p, au prorata des nombres
                    // de jours
                    // (assert : 1 mois = 30jours)
                    soldeManager = new CGSoldeManager();
                    soldeManager.setSession(session);
                    soldeManager.setForIdCompte(idCompte);
                    soldeManager.setForIdCentreCharge(idCentreCharge);
                    soldeManager.setForIdExerComptable(idExerComptable);
                    soldeManager.setForIdPeriodeComptable(periode.getIdPeriodeComptable());
                    soldeManager.setForEstPeriode(new Boolean(true));
                    soldeManager.find(2);
                    if (soldeManager.size() > 1) {
                        throw (new Exception("Erreur computeSoldeCumuleDataAtDate : " + soldeManager.size()
                                + " solde(s) pour un exercice comptable, id compte = " + idCompte + " id exercice = "
                                + idExerComptable));
                    }

                    if (soldeManager.size() == 1) {
                        CGSolde solde = (CGSolde) soldeManager.getEntity(0);

                        // calcul d'une moyenne au prorata
                        if (solde.getBudget() != null) {

                            int nbrDays;
                            if (CGPeriodeComptable.CS_MENSUEL.equals(periode.getIdTypePeriode())) {
                                nbrDays = 30;
                            } else if (CGPeriodeComptable.CS_TRIMESTRIEL.equals(periode.getIdTypePeriode())) {
                                nbrDays = 90;
                            } else if (CGPeriodeComptable.CS_SEMESTRIEL.equals(periode.getIdTypePeriode())) {
                                nbrDays = 180;
                            } else {
                                nbrDays = 360;
                            }

                            // Le budget est calcul au prorata du budget selon
                            // la periode
                            FWCurrency tmp = CGSolde.getBudgetProRata(periode.getDateDebut(), date, nbrDays,
                                    new FWCurrency(solde.getBudget()));
                            if (tmp != null) {
                                result.addBudget(tmp.toString());
                            }
                        }
                    }
                } else {
                    // Préparation du manager pour récupérer les soldes des
                    // périodes comptables
                    soldeManager = new CGSoldeManager();
                    soldeManager.setSession(session);
                    soldeManager.setForIdExerComptable(idExerComptable);
                    soldeManager.setForIdCompte(idCompte);
                    soldeManager.setForIdCentreCharge(idCentreCharge);
                    soldeManager.setForEstPeriode(new Boolean(true));
                    soldeManager.setForIdPeriodeComptable(periode.getIdPeriodeComptable());
                    soldeManager.find(2);

                    if (soldeManager.size() > 1) {
                        throw (new Exception(
                                "Erreur computeSoldeCumule : plusieurs soldes pour une période comptable et un compte donné."));
                    }
                    if (soldeManager.size() == 1) {

                        CGSolde entity = (CGSolde) soldeManager.getEntity(0);

                        result.addAvoir(entity.getAvoir());
                        result.addAvoirMonnaie(entity.getAvoirMonnaie());
                        result.addAvoirProvisoire(entity.getAvoirProvisoire());
                        result.addAvoirProvisoireMonnaie(entity.getAvoirProvisoireMonnaie());

                        result.addDoit(entity.getDoit());
                        result.addDoitMonnaie(entity.getDoitMonnaie());
                        result.addDoitProvisoire(entity.getDoitProvisoire());
                        result.addDoitProvisoireMonnaie(entity.getDoitProvisoireMonnaie());

                        result.addSolde(entity.getSolde());
                        result.addSoldeMonnaie(entity.getSoldeMonnaie());
                        result.addSoldeProvisoire(entity.getSoldeProvisoire());
                        result.addSoldeProvisoireMonnaie(entity.getSoldeProvisoireMonnaie());

                        // Pas de budget specifie pour la periode -> calcul
                        // d'une moyenne au prorata
                        if (entity.getBudget() != null) {
                            result.addBudget(entity.getBudget());
                        }
                    }
                }
            }

            // Le budget est calcul au prorata du budget annuel uniquement si
            // aucun des budgets mensuel
            // n'a été renseignée, c'est à dire somme des budgets == 0
            // On se base sur 360 jours par année (30 jours par mois)
            if (result.getBudget() == null || (new FWCurrency(0)).equals(result.getBudget())) {
                FWCurrency tmp = CGSolde.getBudgetProRata(exercice.getDateDebut(), date, 360, budgetAnnuel);
                if (tmp == null) {
                    result.setBudget(null);
                } else {
                    result.setBudget(new FWCurrency(tmp.toString()));
                }
            }
        }
        return result;
    }

    public static FWCurrency computeSoldeCumuleMonnaie(String idExerComptable, String idCompte, String idPeriode,
            String idCentreCharge, BSession session, boolean isProvisoire) throws Exception {
        return CGSolde.computeSoldeCumule(true, idExerComptable, idCompte, idPeriode, idCentreCharge, session,
                isProvisoire);
    }

    /**
     * Method getBudgetProRata. Calcul le budget au pro rata du nbrJourBase par rapport au montant et aux date données
     * en paramètre.
     * 
     * @param dateDebut
     * @param dateFin
     * @param nbrJourBase
     * @param montant
     * @return FWCurrency
     */

    private static FWCurrency getBudgetProRata(String dateDebut, String dateFin, int nbrJourBase, FWCurrency montant) {

        BigDecimal result;
        try {
            JACalendarGregorian calendar = new JACalendarGregorian();
            long dayBetween = calendar.daysBetween(dateDebut, dateFin);
            result = new BigDecimal(0);
            result = result.add(montant.getBigDecimalValue());
            result = result.divide(new BigDecimal(nbrJourBase), BigDecimal.ROUND_HALF_DOWN);
            result = result.multiply(new BigDecimal(dayBetween));
        } catch (JAException e) {
            e.printStackTrace();
            return null;
        }

        return (new FWCurrency(result.toString()));
    }

    /**
     * Method getListIdPreviousPeriodeComptables. retourne une liste contenant les id des périodes comptable précédente
     * à la période passée en paramètre. Ordonnancement des période selon le schéma suivant :
     * M1/M2/M3/T1/M4/M5/M6/T2/S1/M7/M8/M9/T3/M10/M11/M12/T4/S2/AN/CL M : Mensuel T : Trimestriel S : Semestriel AN:
     * Annuel CL: Cloture
     * 
     * @param session
     * @param idExerComptable
     * @param periode
     * @return List
     */
    public static List getListIdPreviousPeriodeComptables(BSession session, String idExerComptable,
            CGPeriodeComptable periode, String searchPeriodeFromDate) throws Exception {
        return CGSolde.getListPreviousPeriodeComptables(session, idExerComptable, periode, true, searchPeriodeFromDate);
    }

    /**
     * Method getListPeriodeComptablesUntilDate. retourne une liste contenant les périodes comptable jusqu'à la date
     * passée en paramètre.. Ordonnancement des période selon le schéma suivant : (la periode de cloture n'est jamais
     * retrounée) M1/M2/M3/T1/M4/M5/M6/T2/S1/M7/M8/M9/T3/M10/M11/M12/T4/S2/AN M : Mensuel T : Trimestriel S : Semestriel
     * AN: Annuel CL: Cloture
     * 
     * @param session
     * @param idExerComptable
     * @param date
     * @return List
     */
    public static List getListPeriodeComptablesUntilDate(BSession session, String idExerComptable, String date)
            throws Exception {

        // Récupération des périodes comptables précédentes, incluant la(les)
        // periode(s) contenant la date d :
        CGPeriodeComptableManager periodeComptableManager = new CGPeriodeComptableManager();
        periodeComptableManager.setSession(session);
        periodeComptableManager.setForIdExerciceComptable(idExerComptable);
        periodeComptableManager.setUntilDateFin(date);

        List result = new ArrayList();

        HashSet exceptTypesPeriode = new HashSet();
        exceptTypesPeriode.add(CGPeriodeComptable.CS_CLOTURE);
        periodeComptableManager.setExceptIdTypePeriode(exceptTypesPeriode);
        periodeComptableManager.setOrderBy(CGPeriodeComptableManager.TRI_DATE_FIN_AND_TYPE_DESC);
        periodeComptableManager.find(BManager.SIZE_NOLIMIT);

        // Addition des soldes précédents
        for (int i = 0; i < periodeComptableManager.size(); i++) {
            CGPeriodeComptable periode = (CGPeriodeComptable) periodeComptableManager.getEntity(i);
            result.add(periode);
        }

        // Ajouter la(les) periode(s) courante dans la liste.
        periodeComptableManager.setForDateInPeriode(date);
        periodeComptableManager.setUntilDateFin("");
        periodeComptableManager.find();
        // Addition des soldes précédents
        for (int i = 0; i < periodeComptableManager.size(); i++) {
            CGPeriodeComptable periode = (CGPeriodeComptable) periodeComptableManager.getEntity(i);
            if (!result.contains(periode)) {
                result.add(periode);
            }
        }
        return result;
    }

    /**
     * Method getListPreviousPeriodeComptables. retourne une liste contenant les périodes comptable précédente à la
     * période passée en paramètre. Ordonnancement des période selon le schéma suivant :
     * M1/M2/M3/T1/M4/M5/M6/T2/S1/M7/M8/M9/T3/M10/M11/M12/T4/S2/AN/CL M : Mensuel T : Trimestriel S : Semestriel AN:
     * Annuel CL: Cloture
     * 
     * @param session
     * @param idExerComptable
     * @param periode
     * @return List
     */

    public static List getListPreviousPeriodeComptables(BSession session, String idExerComptable,
            CGPeriodeComptable periode) throws Exception {
        return CGSolde.getListPreviousPeriodeComptables(session, idExerComptable, periode, false, null);
    }

    /**
     * Method getListPreviousPeriodeComptables. retourne une liste contenant soit des id, soit des periodes
     * comptable(suivant la valeur de isReturnTypeId) précédente à la période passée en paramètre. Ordonnancement des
     * période selon le schéma suivant : M1/M2/M3/T1/M4/M5/M6/T2/S1/M7/M8/M9/T3/M10/M11/M12/T4/S2/AN/CL M : Mensuel T :
     * Trimestriel S : Semestriel AN: Annuel CL: Cloture
     * 
     * @param session
     * @param idExerComptable
     * @param periode
     * @param isReturnTypeId
     *            si true, retourne une list d'id de période comptable, si false, une liste de période comptable.
     * @return List
     */
    private static List getListPreviousPeriodeComptables(BSession session, String idExerComptable,
            CGPeriodeComptable periode, boolean isReturnTypeId, String searchPeriodeFromDate) throws Exception {
        // Récupération des périodes comptables précédentes, incluant la periode
        // courante :
        CGPeriodeComptableManager periodeComptableManager = new CGPeriodeComptableManager();
        periodeComptableManager.setSession(session);
        periodeComptableManager.setForIdExerciceComptable(idExerComptable);
        periodeComptableManager.setUntilDateFin(periode.getDateFin());

        if (!JadeStringUtil.isBlank(searchPeriodeFromDate)) {
            periodeComptableManager.setFromDateDebut(searchPeriodeFromDate);
        }

        List result = new ArrayList();

        HashSet exceptTypesPeriode = new HashSet();
        if (periode.isAnnuel()) {
            exceptTypesPeriode.add(CGPeriodeComptable.CS_CLOTURE);
            periodeComptableManager.setExceptIdTypePeriode(exceptTypesPeriode);
        } else if (!periode.isCloture()) {
            exceptTypesPeriode.add(CGPeriodeComptable.CS_CLOTURE);
            exceptTypesPeriode.add(CGPeriodeComptable.CS_ANNUEL);
            periodeComptableManager.setExceptIdTypePeriode(exceptTypesPeriode);
        }

        periodeComptableManager.setOrderBy(CGPeriodeComptableManager.TRI_DATE_FIN_AND_TYPE_DESC);
        periodeComptableManager.find(BManager.SIZE_NOLIMIT);

        // Addition des soldes précédents
        for (int i = 0; i < periodeComptableManager.size(); i++) {
            CGPeriodeComptable periodePrecedente = (CGPeriodeComptable) periodeComptableManager.getEntity(i);

            // On ne prend pas en compte la période courante
            if (periodePrecedente.getIdPeriodeComptable().equals(periode.getIdPeriodeComptable())) {
                continue;
            }

            // Cas spéciaux, selon ordonnancement des periodes :
            //
            // M1/M2/M3/T1/M4/M5/M6/T2/S1/M7/M8/M9/T3/M10/M11/M12/T4/S2/AN/CL
            //
            // On exclu le semestre 2 + (cloture + annuel fait ci-dessus)
            if (CGPeriodeComptable.CS_CODE_TRIMESTRE_4.equals(periode.getCode())) {
                if (CGPeriodeComptable.CS_CODE_SEMESTRE_2.equals(periodePrecedente.getCode())) {
                    continue;
                }
            } else if (CGPeriodeComptable.CS_CODE_TRIMESTRE_2.equals(periode.getCode())) {
                // On exclu le semestre 1 + (cloture + annuel fait ci-dessus)
                if (CGPeriodeComptable.CS_CODE_SEMESTRE_1.equals(periodePrecedente.getCode())) {
                    continue;
                }
            } else if ("12".equals(periode.getCode())) {
                // On exclu T4 + S2 + (cloture + annuel fait ci-dessus)
                if (CGPeriodeComptable.CS_CODE_SEMESTRE_2.equals(periodePrecedente.getCode())
                        || CGPeriodeComptable.CS_CODE_TRIMESTRE_4.equals(periodePrecedente.getCode())) {
                    continue;
                }
            } else if ("09".equals(periode.getCode())) {
                // On exclu T3 + (cloture + annuel fait ci-dessus)
                if (CGPeriodeComptable.CS_CODE_TRIMESTRE_3.equals(periodePrecedente.getCode())) {
                    continue;
                }
            } else if ("06".equals(periode.getCode())) {
                // On exclu T2 + S1 + (cloture + annuel fait ci-dessus)
                if (CGPeriodeComptable.CS_CODE_SEMESTRE_1.equals(periodePrecedente.getCode())
                        || CGPeriodeComptable.CS_CODE_TRIMESTRE_2.equals(periodePrecedente.getCode())) {
                    continue;
                }
            } else if ("03".equals(periode.getCode())) {
                // On exclu T2 + (cloture + annuel fait ci-dessus)
                if (CGPeriodeComptable.CS_CODE_TRIMESTRE_1.equals(periodePrecedente.getCode())) {
                    continue;
                }
            }

            if (isReturnTypeId) {
                result.add(periodePrecedente.getIdPeriodeComptable());
            } else {
                result.add(periodePrecedente);
            }
        }

        return result;
    }

    private String avoir = new String();
    private String avoirMonnaie = new String();
    private String avoirProvisoire = new String();
    private String avoirProvisoireMonnaie = new String();
    private String budget = new String();
    private String doit = new String();

    private String doitMonnaie = new String();
    private String doitProvisoire = new String();
    private String doitProvisoireMonnaie = new String();
    private Boolean estPeriode = new Boolean(false);

    private String idCentreCharge = new String();

    private String idCompte = new String();

    private String idExerComptable = new String();

    private String idMandat = new String();

    private String idPeriodeComptable = new String();

    private String idSolde = new String();

    private String solde = new String();

    private String soldeMonnaie = new String();

    private String soldeProvisoire = new String();

    private String soldeProvisoireMonnaie = new String();

    private String tauxTVA = new String();

    /**
     * Commentaire relatif au constructeur CGSolde.
     */
    public CGSolde() {
        super();
    }

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdSolde(_incCounter(transaction, "0"));
    }

    /**
     * Renvoie le nom de la table
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la base de données
     * 
     * @exception java.lang.Exception
     *                si la lecture des propriétés a échouée
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {

        idPeriodeComptable = statement.dbReadNumeric(FIELD_IDPERIODECOMPTABLE, 0);
        idCompte = statement.dbReadNumeric(FIELD_IDCOMPTE, 0);
        idSolde = statement.dbReadNumeric(FIELD_IDSOLDE, 0);
        idCentreCharge = statement.dbReadNumeric(FIELD_IDCENTRECHARGE, 0);
        idMandat = statement.dbReadNumeric(FIELD_IDMANDAT, 0);
        doit = statement.dbReadNumeric(FIELD_DOIT, 2);
        avoir = statement.dbReadNumeric(FIELD_AVOIR, 2);
        solde = statement.dbReadNumeric(FIELD_SOLDE, 2);
        doitProvisoire = statement.dbReadNumeric(FIELD_DOITPROVISOIRE, 2);
        avoirProvisoire = statement.dbReadNumeric(FIELD_AVOIRPROVISOIRE, 2);
        soldeProvisoire = statement.dbReadNumeric(FIELD_SOLDEPROVISOIRE, 2);

        doitMonnaie = statement.dbReadNumeric(FIELD_DOITMONNAIE, 2);
        avoirMonnaie = statement.dbReadNumeric(FIELD_AVOIRMONNAIE, 2);
        soldeMonnaie = statement.dbReadNumeric(FIELD_SOLDEMONNAIE, 2);
        doitProvisoireMonnaie = statement.dbReadNumeric(FIELD_DOITPROVIMONNAIE, 2);
        avoirProvisoireMonnaie = statement.dbReadNumeric(FIELD_AVOIRPROVIMONNAIE, 2);
        soldeProvisoireMonnaie = statement.dbReadNumeric(FIELD_SOLDEPROVIMONNAIE, 2);

        tauxTVA = statement.dbReadNumeric(FIELD_TAUXTVA, 5);
        budget = statement.dbReadNumeric(FIELD_BUDGET, 2);
        idExerComptable = statement.dbReadNumeric(FIELD_IDEXERCOMPTABLE, 0);
        estPeriode = statement.dbReadBoolean(FIELD_ESTPERIODE);

    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité composant la clé primaire
     * 
     * @exception java.lang.Exception
     *                si la sauvegarde des propriétés a échouée
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(FIELD_IDSOLDE, _dbWriteNumeric(statement.getTransaction(), getIdSolde(), ""));

    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité dans la base de données
     * 
     * @param statement
     *            l'instruction à utiliser
     * @exception java.lang.Exception
     *                si la sauvegarde des propriétés a échouée
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(FIELD_IDPERIODECOMPTABLE,
                _dbWriteNumeric(statement.getTransaction(), getIdPeriodeComptable(), "idPeriodeComptable"));
        statement.writeField(FIELD_IDCOMPTE, _dbWriteNumeric(statement.getTransaction(), getIdCompte(), "idCompte"));
        statement.writeField(FIELD_IDSOLDE, _dbWriteNumeric(statement.getTransaction(), getIdSolde(), "idSolde"));
        statement.writeField(FIELD_IDCENTRECHARGE,
                _dbWriteNumeric(statement.getTransaction(), getIdCentreCharge(), "idCentreCharge"));
        statement.writeField(FIELD_IDMANDAT, _dbWriteNumeric(statement.getTransaction(), getIdMandat(), "idMandat"));
        statement.writeField(FIELD_DOIT, _dbWriteNumeric(statement.getTransaction(), getDoit(), "doit"));
        statement.writeField(FIELD_AVOIR, _dbWriteNumeric(statement.getTransaction(), getAvoir(), "avoir"));
        statement.writeField(FIELD_SOLDE, _dbWriteNumeric(statement.getTransaction(), getSolde(), "solde"));
        statement.writeField(FIELD_DOITPROVISOIRE,
                _dbWriteNumeric(statement.getTransaction(), getDoitProvisoire(), "doitProvisoire"));
        statement.writeField(FIELD_AVOIRPROVISOIRE,
                _dbWriteNumeric(statement.getTransaction(), getAvoirProvisoire(), "avoirProvisoire"));
        statement.writeField(FIELD_SOLDEPROVISOIRE,
                _dbWriteNumeric(statement.getTransaction(), getSoldeProvisoire(), "soldeProvisoire"));

        statement.writeField(FIELD_DOITMONNAIE,
                _dbWriteNumeric(statement.getTransaction(), getDoitMonnaie(), "doitMonnaie"));
        statement.writeField(FIELD_AVOIRMONNAIE,
                _dbWriteNumeric(statement.getTransaction(), getAvoirMonnaie(), "avoirMonnaie"));
        statement.writeField(FIELD_SOLDEMONNAIE,
                _dbWriteNumeric(statement.getTransaction(), getSoldeMonnaie(), "soldeMonnaie"));
        statement.writeField(FIELD_DOITPROVIMONNAIE,
                _dbWriteNumeric(statement.getTransaction(), getDoitProvisoireMonnaie(), "doitProviMonnaie"));
        statement.writeField(FIELD_AVOIRPROVIMONNAIE,
                _dbWriteNumeric(statement.getTransaction(), getAvoirProvisoireMonnaie(), "avoirProviMonnaie"));
        statement.writeField(FIELD_SOLDEPROVIMONNAIE,
                _dbWriteNumeric(statement.getTransaction(), getSoldeProvisoireMonnaie(), "soldeProviMonnaie"));

        statement.writeField(FIELD_TAUXTVA, _dbWriteNumeric(statement.getTransaction(), getTauxTVA(), "tauxTVA"));
        statement.writeField(FIELD_BUDGET, _dbWriteNumeric(statement.getTransaction(), getBudget(), "budget"));
        statement.writeField(FIELD_IDEXERCOMPTABLE,
                _dbWriteNumeric(statement.getTransaction(), getIdExerComptable(), "idExerComptable"));
        statement.writeField(
                FIELD_ESTPERIODE,
                _dbWriteBoolean(statement.getTransaction(), isEstPeriode(),
                        globaz.globall.db.BConstants.DB_TYPE_BOOLEAN_CHAR, "estPeriode"));

    }

    public FWCurrency computeSoldeCumule(boolean isProvisoire) throws Exception {
        return computeSoldeCumule(false, getIdExerComptable(), getIdCompte(), getIdPeriodeComptable(),
                getIdCentreCharge(), getSession(), isProvisoire);
    }

    public FWCurrency computeSoldeCumuleMonnaie(boolean isProvisoire) throws Exception {
        return computeSoldeCumule(true, getIdExerComptable(), getIdCompte(), getIdPeriodeComptable(),
                getIdCentreCharge(), getSession(), isProvisoire);
    }

    @Override
    public String getAvoir() {
        return avoir;
    }

    /**
     * Returns the avoirMonnaie.
     * 
     * @return String
     */
    public String getAvoirMonnaie() {
        return avoirMonnaie;
    }

    @Override
    public String getAvoirProvisoire() {
        return avoirProvisoire;
    }

    /**
     * Returns the avoirProvisoireMonnaie.
     * 
     * @return String
     */
    public String getAvoirProvisoireMonnaie() {
        return avoirProvisoireMonnaie;
    }

    public String getBudget() {
        return budget;
    }

    @Override
    public String getDoit() {
        return doit;
    }

    /**
     * Returns the doitMonnaie.
     * 
     * @return String
     */
    public String getDoitMonnaie() {
        return doitMonnaie;
    }

    @Override
    public String getDoitProvisoire() {
        return doitProvisoire;
    }

    /**
     * Returns the doitProvisoireMonnaie.
     * 
     * @return String
     */
    public String getDoitProvisoireMonnaie() {
        return doitProvisoireMonnaie;
    }

    public String getIdCentreCharge() {
        return idCentreCharge;
    }

    @Override
    public String getIdCompte() {
        return idCompte;
    }

    @Override
    public String getIdExerComptable() {
        return idExerComptable;
    }

    @Override
    public String getIdMandat() {
        return idMandat;
    }

    @Override
    public String getIdPeriodeComptable() {
        return idPeriodeComptable;
    }

    public String getIdSolde() {
        return idSolde;
    }

    @Override
    public String getSolde() {
        return solde;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.03.2003 14:46:33)
     * 
     * @return java.lang.String
     * @param date
     *            java.lang.String
     */
    public String getSoldeAt(String date) {
        return null;
    }

    /**
     * Returns the soldeMonnaie.
     * 
     * @return String
     */
    public String getSoldeMonnaie() {
        return soldeMonnaie;
    }

    @Override
    public String getSoldeProvisoire() {
        return soldeProvisoire;
    }

    /**
     * Returns the soldeProvisoireMonnaie.
     * 
     * @return String
     */
    public String getSoldeProvisoireMonnaie() {
        return soldeProvisoireMonnaie;
    }

    public String getTauxTVA() {
        return tauxTVA;
    }

    @Override
    public Boolean isEstPeriode() {
        return estPeriode;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.04.2003 17:02:21)
     * 
     * @return boolean
     */
    public boolean isSoldeAvoir() {
        return (new FWCurrency(getSolde())).isNegative();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.04.2003 17:02:21)
     * 
     * @return boolean
     */
    public boolean isSoldeAvoirMonnaie() {
        return (new FWCurrency(getSoldeMonnaie())).isNegative();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.04.2003 17:02:21)
     * 
     * @return boolean
     */
    public boolean isSoldeDoit() {
        return (new FWCurrency(getSolde())).isPositive();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.04.2003 17:02:21)
     * 
     * @return boolean
     */
    public boolean isSoldeDoitMonnaie() {
        return (new FWCurrency(getSoldeMonnaie())).isPositive();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.04.2003 17:02:21)
     * 
     * @return boolean
     */
    public boolean isSoldeMonnaieZero() {
        return (new FWCurrency(getSoldeMonnaie())).isZero();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.04.2003 17:02:21)
     * 
     * @return boolean
     */
    public boolean isSoldeProvisoireAvoir() {
        return (new FWCurrency(getSoldeProvisoire())).isNegative();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.04.2003 17:02:21)
     * 
     * @return boolean
     */
    public boolean isSoldeProvisoireAvoirMonnaie() {
        return (new FWCurrency(getSoldeProvisoireMonnaie())).isNegative();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.04.2003 17:02:21)
     * 
     * @return boolean
     */
    public boolean isSoldeProvisoireDoit() {
        return (new FWCurrency(getSoldeProvisoire())).isPositive();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.04.2003 17:02:21)
     * 
     * @return boolean
     */
    public boolean isSoldeProvisoireDoitMonnaie() {
        return (new FWCurrency(getSoldeProvisoireMonnaie())).isPositive();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.04.2003 17:02:21)
     * 
     * @return boolean
     */
    public boolean isSoldeProvisoireMonnaieZero() {
        return (new FWCurrency(getSoldeProvisoireMonnaie())).isZero();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.04.2003 17:02:21)
     * 
     * @return boolean
     */
    public boolean isSoldeProvisoireZero() {
        return (new FWCurrency(getSoldeProvisoire())).isZero();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.04.2003 17:02:21)
     * 
     * @return boolean
     */
    public boolean isSoldeZero() {
        return (new FWCurrency(getSolde())).isZero();
    }

    @Override
    public void setAvoir(String newAvoir) {
        avoir = newAvoir;
    }

    /**
     * Sets the avoirMonnaie.
     * 
     * @param avoirMonnaie
     *            The avoirMonnaie to set
     */
    public void setAvoirMonnaie(String avoirMonnaie) {
        this.avoirMonnaie = avoirMonnaie;
    }

    @Override
    public void setAvoirProvisoire(String newAvoirProvisoire) {
        avoirProvisoire = newAvoirProvisoire;
    }

    /**
     * Sets the avoirProvisoireMonnaie.
     * 
     * @param avoirProvisoireMonnaie
     *            The avoirProvisoireMonnaie to set
     */
    public void setAvoirProvisoireMonnaie(String avoirProvisoireMonnaie) {
        this.avoirProvisoireMonnaie = avoirProvisoireMonnaie;
    }

    public void setBudget(String newBudget) {
        budget = newBudget;
    }

    @Override
    public void setDoit(String newDoit) {
        doit = newDoit;
    }

    /**
     * Sets the doitMonnaie.
     * 
     * @param doitMonnaie
     *            The doitMonnaie to set
     */
    public void setDoitMonnaie(String doitMonnaie) {
        this.doitMonnaie = doitMonnaie;
    }

    @Override
    public void setDoitProvisoire(String newDoitProvisoire) {
        doitProvisoire = newDoitProvisoire;
    }

    /**
     * Sets the doitProvisoireMonnaie.
     * 
     * @param doitProvisoireMonnaie
     *            The doitProvisoireMonnaie to set
     */
    public void setDoitProvisoireMonnaie(String doitProvisoireMonnaie) {
        this.doitProvisoireMonnaie = doitProvisoireMonnaie;
    }

    @Override
    public void setEstPeriode(Boolean newEstPeriode) {
        estPeriode = newEstPeriode;
    }

    public void setIdCentreCharge(String newIdCentreCharge) {
        idCentreCharge = newIdCentreCharge;
    }

    @Override
    public void setIdCompte(String newIdCompte) {
        idCompte = newIdCompte;
    }

    @Override
    public void setIdExerComptable(String newIdExerComptable) {
        idExerComptable = newIdExerComptable;
    }

    @Override
    public void setIdMandat(String newIdMandat) {
        idMandat = newIdMandat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */
    @Override
    public void setIdPeriodeComptable(String newIdPeriodeComptable) {
        idPeriodeComptable = newIdPeriodeComptable;
    }

    public void setIdSolde(String newIdSolde) {
        idSolde = newIdSolde;
    }

    @Override
    public void setIdSuccursale(String newIdSuccursale) {
        // Do nothing. Not use into CGSolde. Only CGSoldeBouclementSuccursale.
    }

    @Override
    public void setSolde(String newSolde) {
        solde = newSolde;
    }

    /**
     * Sets the soldeMonnaie.
     * 
     * @param soldeMonnaie
     *            The soldeMonnaie to set
     */
    public void setSoldeMonnaie(String soldeMonnaie) {
        this.soldeMonnaie = soldeMonnaie;
    }

    @Override
    public void setSoldeProvisoire(String newSoldeProvisoire) {
        soldeProvisoire = newSoldeProvisoire;
    }

    /**
     * Sets the soldeProvisoireMonnaie.
     * 
     * @param soldeProvisoireMonnaie
     *            The soldeProvisoireMonnaie to set
     */
    public void setSoldeProvisoireMonnaie(String soldeProvisoireMonnaie) {
        this.soldeProvisoireMonnaie = soldeProvisoireMonnaie;
    }

    public void setTauxTVA(String newTauxTVA) {
        tauxTVA = newTauxTVA;
    }

}
