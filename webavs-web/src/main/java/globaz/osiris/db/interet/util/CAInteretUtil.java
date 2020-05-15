package globaz.osiris.db.interet.util;

import ch.globaz.common.domaine.Date;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.*;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarMonth;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.interet.tardif.montantsoumis.CASumMontantSoumisParPlan;
import globaz.osiris.db.interet.tardif.montantsoumis.CASumMontantSoumisParPlanManager;
import globaz.osiris.db.interet.util.ecriturenonsoumise.CAEcritureNonSoumiseManager;
import globaz.osiris.db.interet.util.planparsection.CAPlanParSectionManager;
import globaz.osiris.db.interet.util.tauxParametres.CATauxParametre;
import globaz.osiris.db.interets.CAGenreInteret;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.db.interets.CAInteretMoratoireManager;

import java.util.*;

public class CAInteretUtil {

    private static final String CS_PARAM_LIMITEIM = "LIMITEIM";
    private static final String CS_PARAM_LIMITEIR = "LIMITEIR";
    public static final String CS_PARAM_TAUX = "TAUX";
    public static final String CS_PARAM_TAUX_REMU = "TAUXREMU";
    private static final String CS_PARAMETRES_IM = "10200030";

    /**
     * Pour un plan liste les paiements et les compensations.
     * 
     * @param session
     * @param transaction
     * @param idPlan
     * @param idSection
     * @param forDateAfter
     * @param forDateBefore
     * @return
     * @throws Exception
     */
    public static CAEcritureNonSoumiseManager getEcrituresNonSoumises(BSession session, BTransaction transaction,
            String idPlan, String idSection, String forDateAfter, String forDateBefore) throws Exception {
        CAEcritureNonSoumiseManager manager = new CAEcritureNonSoumiseManager();

        manager.setSession(session);
        manager.setForIdSection(idSection);
        manager.setForIdPlan(idPlan);

        manager.setForDateAfter(forDateAfter);
        manager.setForDateBefore(forDateBefore);

        manager.find(transaction, BManager.SIZE_NOLIMIT);

        if (manager.hasErrors()) {
            throw new Exception(manager.getErrors().toString());
        }

        return manager;
    }

    /**
     * Return l'id de la contre partie de l'intérêt moratoire.
     * 
     * @param session
     * @param transaction
     * @param idPlan
     * @param typeInteret
     * @return
     * @throws Exception
     */
    public static String getIdContrePartie(BSession session, BTransaction transaction, String idPlan, String typeInteret)
            throws Exception {
        CAGenreInteret genreInteret = new CAGenreInteret();
        genreInteret.setSession(session);

        genreInteret.setAlternateKey(CAGenreInteret.AK_PLAN_TYPE);

        genreInteret.setIdPlanCalculInteret(idPlan);
        genreInteret.setIdTypeInteret(typeInteret);

        genreInteret.retrieve(transaction);

        if (genreInteret.isNew() || genreInteret.hasErrors()) {
            throw new Exception(session.getLabel("7357"));
        }

        return genreInteret.getIdRubrique();
    }

    /**
     * Return l'intérêt moratoire tardif lié à la section. Si ce dernier n'est pas trouvé alors créé un nouveau sans
     * l'ajouter en base de données.
     * 
     * @param session
     * @param transaction
     * @param idPlan
     * @return
     * @throws Exception
     */
    public static CAInteretMoratoire getInteretMoratoire(BTransaction transaction, String idPlan, String idSection,
            String idJournal) throws Exception {

        if (JadeStringUtil.isBlankOrZero(idSection)) {
            throw new Exception("L'idSection doit être renseigné"); // TODO Label
        }

        CAInteretMoratoireManager manager = new CAInteretMoratoireManager();
        manager.setSession(transaction.getSession());
        manager.setForIdSection(idSection);
        manager.setForIdPlan(idPlan);

        ArrayList<String> idGenreInteretIn = new ArrayList<String>();
        idGenreInteretIn.add(CAGenreInteret.CS_TYPE_TARDIF);
        manager.setForIdGenreInteretIn(idGenreInteretIn);

        manager.setForMotifCalculNot(CAInteretMoratoire.CS_A_CONTROLER);

        manager.find(transaction, BManager.SIZE_NOLIMIT);

        if (manager.hasErrors()) {
            throw new Exception(manager.getErrors().toString());
        }

        if (manager.isEmpty()) {
            CAInteretMoratoire interet = new CAInteretMoratoire();
            interet.setSession(transaction.getSession());

            interet.setIdSection(idSection);

            interet.setDateCalcul(JACalendar.today().toStr("."));
            interet.setMotifcalcul(CAInteretMoratoire.CS_EXEMPTE);
            interet.setIdGenreInteret(CAGenreInteret.CS_TYPE_TARDIF);
            interet.setIdJournalCalcul(idJournal);
            interet.setIdPlan(idPlan);
            interet.setIdRubrique(CAInteretUtil.getIdContrePartie(transaction.getSession(), transaction, idPlan,
                    CAGenreInteret.CS_TYPE_TARDIF));

            // Pas de add => pas chargé base avant insert réel (vraiment tardif)

            return interet;
        } else {
            return (CAInteretMoratoire) manager.getFirstEntity();
        }
    }

    /**
     * Return la limite jusqu'à laquelle l'intéret moratoire est EXEMPTE.
     * 
     * @param session
     * @param transaction
     * @return
     * @throws Exception
     */
    public static String getLimiteExempteInteretMoratoire(BSession session, BTransaction transaction) throws Exception {
        return FWFindParameter.findParameter(transaction, CAInteretUtil.CS_PARAMETRES_IM,
                CAInteretUtil.CS_PARAM_LIMITEIM, null, "0", 2);
    }

    /**
     * Return la limite jusqu'à laquelle l'intéret rémunératoire est EXEMPTE.
     * 
     * @param session
     * @param transaction
     * @return
     * @throws Exception
     */
    public static String getLimiteExempteInteretRenumeratoire(BSession session, BTransaction transaction)
            throws Exception {
        return FWFindParameter.findParameter(transaction, CAInteretUtil.CS_PARAMETRES_IM,
                CAInteretUtil.CS_PARAM_LIMITEIR, null, "0", 2);
    }

    /**
     * Return le montant de l'intérêt (pour une ligne de détail).
     * 
     * @param session
     * @param montantSoumis
     * @param ecriture
     * @param dateCaculDebutInteret
     * @param taux
     * @return
     * @throws Exception
     */
    public static FWCurrency getMontantInteret(BSession session, FWCurrency montantSoumis, JADate dateCalculFinInteret,
            JADate dateCaculDebutInteret, double taux) throws Exception {
        JACalendarMonth calendar = new JACalendarMonth(30);
        int days = (int) calendar.daysBetween(dateCaculDebutInteret,
                session.getApplication().getCalendar().addDays(dateCalculFinInteret, 1));

        double dInt = montantSoumis.doubleValue() * taux * days / 36000;

        FWCurrency montantInteret = new FWCurrency(dInt);
        montantInteret.round(FWCurrency.ROUND_5CT);

        return montantInteret;
    }

    /**
     * Pour un plan somme le montant des écritures soumis à intérêt.
     * 
     * @param session
     * @param transaction
     * @param idSection
     * @param idPlan
     * @param anneesCotisations
     * @return
     * @throws Exception
     */
    public static FWCurrency getMontantSoumisParPlans(BTransaction transaction, String idSection, String idPlan,
            ArrayList anneesCotisations) throws Exception {
        CASumMontantSoumisParPlanManager manager = new CASumMontantSoumisParPlanManager();

        manager.setSession(transaction.getSession());
        manager.setForIdSection(idSection);
        manager.setForIdPlan(idPlan);

        manager.setForAnneesCotisationsIn(anneesCotisations);

        manager.find(transaction, BManager.SIZE_NOLIMIT);

        if (manager.hasErrors()) {
            throw new Exception(manager.getErrors().toString());
        }

        if (manager.isEmpty()) {
            return null;
        } else {
            return new FWCurrency(((CASumMontantSoumisParPlan) manager.getFirstEntity()).getMontant());
        }
    }

    /**
     * Return la liste des plans qui touchent la section.<br/>
     * Ces plans ont un montant cumulé (total indépendant d'un secteur) <= 0, sauf en mode manuel.
     * 
     * @param session
     * @param transaction
     * @param idPlan
     *            optionnel
     * @param idSection
     * @param montantCumuleNotPositive
     *            : True si le montant cumulé <= 0
     * 
     * @return
     * @throws Exception
     */
    public static CAPlanParSectionManager getSectionPlans(BSession session, BTransaction transaction, String idPlan,
            String idSection, boolean montantCumuleNotPositive) throws Exception {
        CAPlanParSectionManager manager = new CAPlanParSectionManager();

        manager.setSession(session);
        manager.setForIdSection(idSection);

        if (!JadeStringUtil.isIntegerEmpty(idPlan)) {
            manager.setForIdPlan(idPlan);
        }

        manager.setMontantCumuleNotPositive(montantCumuleNotPositive);

        manager.find(transaction, BManager.SIZE_NOLIMIT);

        if (manager.hasErrors()) {
            throw new Exception(manager.getErrors().toString());
        }

        return manager;
    }

    /**
     * Return le taux en vigueur pour une année.
     * 
     * @param session
     * @param transaction
     * @param forDate
     * @return
     * @throws Exception
     */
    public static double getTaux(BTransaction transaction, String forDate) throws Exception {
        if (!JAUtil.isDateEmpty(forDate)) {
            return Double.parseDouble(FWFindParameter.findParameter(transaction, CAInteretUtil.CS_PARAMETRES_IM,
                    CAInteretUtil.CS_PARAM_TAUX, forDate, "0", 2));
        } else {
            return 0.00;
        }
    }

    /**
     * Return le taux en vigueur pour une année.
     *
     * @param session
     * @param transaction
     * @param forDate
     * @return
     * @throws Exception
     */
    public static List<CATauxParametre> getTaux(BTransaction transaction, String debutDate, String finDate, String cleDiff, int nbDigit) throws Exception {
        List<CATauxParametre> listeTaux = new ArrayList<>();
        //Récuperation des paramètres (données brutes)
        FWFindParameterManager mgr = new FWFindParameterManager();
        mgr.setSession(transaction.getSession());
        mgr.setIdApplParametre(transaction.getSession().getApplicationId());
        mgr.setIdCodeSysteme(CAInteretUtil.CS_PARAMETRES_IM);
        mgr.setIdCleDiffere(cleDiff);
        if (!JAUtil.isDateEmpty(finDate)) {
            mgr.setDateDebutValidite(finDate);
        }
        mgr.setIdActeurParametre("0");
        mgr.setPlageValDeParametre("0");
        mgr.find(transaction);

        //Mapping pour avoir les infos nécessaire pour la découpage des périodes lors d'ajouts des lignes.
        Date dateDebut = new Date(debutDate);
        Date dateFin = new Date(finDate);
        if (mgr.size() == 0) {
            throw new Exception("Parameter not found:" + CAInteretUtil.CS_PARAMETRES_IM + ", " + cleDiff + ", " + debutDate);
        } else {
            for (int i = 0; i < mgr.size(); i++) {
                FWFindParameter param = (FWFindParameter) mgr.getEntity(i);
                Date dateDebutParam = new Date(param.getDateDebutValidite());
                CATauxParametre tauxParam = new CATauxParametre();
                tauxParam.setLibelle(param.getDesignationParametre());
                tauxParam.setCleDiff(cleDiff);
                tauxParam.setCsFamille(param.getIdCodeSysteme());
                tauxParam.setDateDebut(dateDebutParam);
                String taux = "";
                if (param.getValeurNumParametre().trim().length() != 0) {
                    taux = JAUtil
                            .formatDecimal(new java.math.BigDecimal(param.getValeurNumParametre()), nbDigit);
                }
                tauxParam.setTaux(Double.parseDouble(taux));
                //Ajouter la date de fin
                if (!listeTaux.isEmpty()) {
                    tauxParam.setDateFin(listeTaux.get(i - 1).getDateDebut().addDays(-1));
                }
                listeTaux.add(tauxParam);
            }
            //Trié par ordre croissant de la date de début
            Collections.sort(listeTaux, new Comparator<CATauxParametre>() {
                @Override
                public int compare(CATauxParametre CATauxParametre, CATauxParametre t1) {
                    if (JadeDateUtil.isDateBefore(CATauxParametre.getDateDebut().getSwissValue(), t1.getDateDebut().getSwissValue())) {
                        return -1;
                    }
                    if (CATauxParametre.getDateDebut().equals(t1.getDateDebut())) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });
            Iterator<CATauxParametre> it = listeTaux.iterator();
            //Tri : Ceux qui sont hors de la période
            //Si la date de début et de la fin n'est pas inclus dans la période du taux => on l'enlève de la liste.
            CATauxParametre CATauxParametre;
            while(it.hasNext()){
                CATauxParametre = it.next();
//                if (!(checkPeriodeInclusTaux(dateDebut, CATauxParametre)
//                        || checkPeriodeInclusTaux(dateFin, CATauxParametre))) {
//                    it.remove();
//                }
             if (!checkPeriodeInclusTaux(dateDebut,dateFin, CATauxParametre)) {
                    it.remove();
                }
            }
        }


        return listeTaux;
    }

    private static boolean checkPeriodeInclusTaux(Date dateDebutInteret,Date dateFinInteret, CATauxParametre tauxParametre) {
        boolean isIncluded = false;
        if(tauxParametre.getDateFin() != null){
            if ((tauxParametre.getDateFin().beforeOrEquals(dateDebutInteret))
                    || tauxParametre.getDateDebut().afterOrEquals(dateFinInteret)) {
                return false;
            } else {
                return true;
            }
        }else{
            if (tauxParametre.getDateDebut().afterOrEquals(dateFinInteret)) {
                return false;
            } else {
                return true;
            }
        }
    }





    private static boolean checkPeriodeInclusTaux(Date date, CATauxParametre CATauxParametre) {
        boolean isIncluded = false;


        if(CATauxParametre.getDateFin() != null){
            if (date.afterOrEquals(CATauxParametre.getDateDebut()) && date.beforeOrEquals(CATauxParametre.getDateFin())) {
                return true;
            } else {
                return false;
            }
        }else{
            if (date.afterOrEquals(CATauxParametre.getDateDebut()) ) {
                return true;
            } else {
                return false;
            }
        }
    }
}
