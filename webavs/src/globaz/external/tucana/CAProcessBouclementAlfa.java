package globaz.external.tucana;

import globaz.external.tucana.db.CABouclementAlfa;
import globaz.external.tucana.db.CABouclementAlfaManager;
import globaz.external.tucana.db.CABouclementAlfaNewManager;
import globaz.external.tucana.db.CABouclementAlfaRemiseManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGComptePertesProfitsListViewBean;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGPeriodeComptableManager;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.helios.translation.CodeSystem;
import globaz.itucana.constantes.IPropertiesNames;
import globaz.itucana.constantes.ITUCSRubriqueListeDesRubriques;
import globaz.itucana.exception.TUModelInstanciationException;
import globaz.itucana.model.ITUModelBouclement;
import globaz.itucana.process.TUProcessusBouclement;
import globaz.itucana.properties.TUPropertiesProvider;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CATauxRubriques;
import globaz.osiris.db.comptes.CATauxRubriquesManager;
import globaz.tucana.model.TULigneBouclement;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CAProcessBouclementAlfa extends TUProcessusBouclement {

    private static final String LABEL_MONTANT_RUBRIQUE_DIFF_PERTES_PROFITS = "MONTANT_RUBRIQUE_DIFF_PERTES_PROFITS";

    /**
     * Constructor.
     * 
     * @param annee
     * @param mois
     */
    public CAProcessBouclementAlfa(String annee, String mois) {
        super(annee, mois);
    }

    /**
     * Ajout des informations de bouclement alfa pour la rubrique passée en paramètre.
     * 
     * @param bouclement
     * @param codeSystem
     * @param idExterneRubrique
     * @param forAssuranceFFPP
     * @throws TUModelInstanciationException
     */
    private void addBouclementAlfa(ITUModelBouclement bouclement, String codeSystem, String idExterneRubrique,
            Boolean forAssuranceFFPP, String genreAffiliation) throws TUModelInstanciationException {

        HashMap<String, TULigneBouclement> data = new HashMap<String, TULigneBouclement>();
        FWCurrency montantTotal = new FWCurrency();

        try {
            if (!forAssuranceFFPP) {
                CABouclementAlfaManager manager = new CABouclementAlfaManager();
                manager.setSession(getSession());
                manager.setForDatePeriodeBegin(getDatePeriodeBegin());
                manager.setForDatePeriodeEnd(getDatePeriodeEnd());
                manager.setForIdExterneRubrique(idExterneRubrique);
                manager.setForAssuranceFFPP(forAssuranceFFPP);
                manager.find();

                for (int i = 0; i < manager.size(); i++) {
                    CABouclementAlfa bouclementAlfa = (CABouclementAlfa) manager.get(i);
                    addMontantCanton(data, codeSystem, bouclementAlfa.getCanton(), bouclementAlfa.getSumMontant());
                    montantTotal.add(bouclementAlfa.getSumMontant());
                }
            }

            // S120614_004 - Allocations familiales ventilées par cabton en comptabilité
            if (!JadeStringUtil.isBlank(genreAffiliation) || forAssuranceFFPP) {
                CABouclementAlfaNewManager managerNew = new CABouclementAlfaNewManager();
                managerNew.setSession(getSession());
                managerNew.setForDatePeriodeBegin(getDatePeriodeBegin());
                managerNew.setForDatePeriodeEnd(getDatePeriodeEnd());
                managerNew.setForGenre(genreAffiliation);
                if (forAssuranceFFPP) {
                    managerNew.setForAssuranceFFPP(forAssuranceFFPP);
                    managerNew.setForIdExterneRubrique(idExterneRubrique);
                }
                managerNew.find();

                for (int i = 0; i < managerNew.size(); i++) {
                    CABouclementAlfa bouclementAlfa = (CABouclementAlfa) managerNew.get(i);
                    addMontantCanton(data, codeSystem, bouclementAlfa.getCanton(), bouclementAlfa.getSumMontant());
                    montantTotal.add(bouclementAlfa.getSumMontant());
                }
            }

            // Ajoute les lignes
            for (TULigneBouclement line : data.values()) {
                bouclement.addLine(line.getCanton(), line.getRubrique(), line.getMontantNombre());
            }

            checkMontant(idExterneRubrique, montantTotal);
        } catch (Exception e) {
            throw new TUModelInstanciationException(e.getMessage());
        }
    }

    /**
     * Ajout des informations de bouclement alfa pour la rubrique passée en paramètre.
     * 
     * @param bouclement
     * @param codeSystem
     * @param idExterneRubrique
     * @param forAssuranceFFPP
     * @throws TUModelInstanciationException
     */
    private void addBouclementAlfaRemise(ITUModelBouclement bouclement, String codeSystem, String idExterneRubrique)
            throws TUModelInstanciationException {

        FWCurrency montantTotal = new FWCurrency();
        try {
            // S120614_004 - Allocations familiales ventilées par cabton en comptabilité
            CABouclementAlfaRemiseManager manager = new CABouclementAlfaRemiseManager();
            manager.setSession(getSession());
            manager.setForDatePeriodeBegin(getDatePeriodeBegin());
            manager.setForDatePeriodeEnd(getDatePeriodeEnd());
            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                CABouclementAlfa bouclementAlfa = (CABouclementAlfa) manager.get(i);
                bouclement.addLine(bouclementAlfa.getCanton(), codeSystem, bouclementAlfa.getSumMontant());
                montantTotal.add(bouclementAlfa.getSumMontant());
            }

            checkMontant(idExterneRubrique, montantTotal);
        } catch (Exception e) {
            throw new TUModelInstanciationException(e.getMessage());
        }
    }

    /**
     * Ajout des informations du supplément légal de bouclement alfa pour la rubrique passé en paramètre.
     * 
     * @param bouclement
     * @param codeSystem
     * @param idExterneRubrique
     * @param tauxEmployeur
     * @throws TUModelInstanciationException
     */
    private void addBouclementAlfaSupplementLegal(ITUModelBouclement bouclement, String codeSystem,
            String idExterneRubrique, boolean tauxEmployeur) throws TUModelInstanciationException {

        HashMap<String, TULigneBouclement> data = new HashMap<String, TULigneBouclement>();

        try {
            CABouclementAlfaManager manager = new CABouclementAlfaManager();
            manager.setSession(getSession());
            manager.setForDatePeriodeBegin(getDatePeriodeBegin());
            manager.setForDatePeriodeEnd(getDatePeriodeEnd());
            manager.setForIdExterneRubrique(idExterneRubrique);
            manager.setForAssuranceFFPP(false);
            manager.setGroupByAnneeCotisation(true);
            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                treatBouclementAlfaSupplementLegal(manager, data, codeSystem, idExterneRubrique, tauxEmployeur, i);
            }

            // S120614_004 - Allocations familiales ventilées par canton en comptabilité
            CABouclementAlfaNewManager managerNew = new CABouclementAlfaNewManager();
            managerNew.setSession(getSession());
            managerNew.setForDatePeriodeBegin(getDatePeriodeBegin());
            managerNew.setForDatePeriodeEnd(getDatePeriodeEnd());
            managerNew.setForGenre(globaz.naos.translation.CodeSystem.GENRE_ASS_PARITAIRE);
            managerNew.setForAssuranceFFPP(false);
            managerNew.setGroupByAnneeCotisation(true);
            managerNew.find();
            for (int i = 0; i < managerNew.size(); i++) {
                treatBouclementAlfaSupplementLegal(managerNew, data, codeSystem, idExterneRubrique, tauxEmployeur, i);
            }

            // Ajoute les lignes
            for (TULigneBouclement line : data.values()) {
                bouclement.addLine(line.getCanton(), line.getRubrique(), line.getMontantNombre());
            }
        } catch (Exception e) {
            throw new TUModelInstanciationException(e.getMessage());
        }
    }

    /**
     * @param data
     * @param codeSystem
     * @param bouclementAlfa
     */
    private void addMontantCanton(HashMap<String, TULigneBouclement> data, String codeSystem, String canton,
            String montant) {
        if (data.containsKey(key(canton, codeSystem))) {
            BigDecimal total = new BigDecimal(data.get(key(canton, codeSystem)).getMontantNombre());
            data.get(key(canton, codeSystem)).setMontantNombre(total.add(new BigDecimal(montant)).toString());
        } else {
            data.put(key(canton, codeSystem), new TULigneBouclement(canton, codeSystem, montant));
        }
    }

    /**
     * Contrôle du montant total trouvé pour une rubrique (si le montant != 0). Ce montant doit être le même que le
     * montant "Pertes et profites" en comptabilité générale.
     * 
     * @param idExterneRubrique
     * @param montantTotal
     * @throws TUModelInstanciationException
     */
    private void checkMontant(String idExterneRubrique, FWCurrency montantTotal) throws TUModelInstanciationException {
        if (montantTotal.isZero()) {
            return;
        }

        CGPeriodeComptable periode = getActualPeriode();

        CGComptePertesProfitsListViewBean manager = new CGComptePertesProfitsListViewBean();
        manager.setSession(getSession());

        manager.setForSoldeOpen(false);
        manager.setForIdExerciceComptable(periode.getIdExerciceComptable());
        manager.setReqComptabilite(CodeSystem.CS_DEFINITIF);
        manager.setInclurePeriodesPrec(new Boolean(false));
        manager.setGroupIdCompteOfas(false);
        manager.setReqDomaine(CGCompte.CS_COMPTE_TOUS);

        manager.setReqForListPeriodesComptable(periode.getIdPeriodeComptable());

        manager.setForIdExterne(idExterneRubrique);

        try {
            manager.find();

            CGPlanComptableViewBean compte = (CGPlanComptableViewBean) manager.getFirstEntity();

            FWCurrency soldePertesEtProfits = new FWCurrency(compte.getSolde());
            soldePertesEtProfits.negate();

            if (montantTotal.compareTo(soldePertesEtProfits) != 0) {
                getMemoryLog().logMessage(
                        getSession().getLabel(CAProcessBouclementAlfa.LABEL_MONTANT_RUBRIQUE_DIFF_PERTES_PROFITS)
                                + " (" + idExterneRubrique + ")", FWViewBeanInterface.WARNING,
                        this.getClass().getName());
            }
        } catch (Exception e) {
            throw new TUModelInstanciationException(e.getMessage());
        }

    }

    /**
     * Recherche de la période comptable en fonction de l'année et du mois passé au constructeur.<br/>
     * Le période doit être une période du plan AVS 900.
     * 
     * @return
     * @throws TUModelInstanciationException
     * @throws Exception
     */
    private CGPeriodeComptable getActualPeriode() throws TUModelInstanciationException {
        CGPeriodeComptableManager manager = new CGPeriodeComptableManager();
        manager.setSession(getSession());

        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(getAnnee()), Integer.parseInt(getMois()) - 1, 1);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        manager.setForDateInPeriode(formatter.format(cal.getTime()));

        try {
            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                CGPeriodeComptable tmpPeriode = (CGPeriodeComptable) manager.get(i);

                if (tmpPeriode.getExerciceComptable().getMandat().isEstComptabiliteAVS().booleanValue()
                        && tmpPeriode.getExerciceComptable().getMandat().getIdMandat()
                                .equals(CGMandat.MANDAT_AVS_DEFAULT_NUMBER)) {
                    return tmpPeriode;
                }
            }
        } catch (Exception e) {
            throw new TUModelInstanciationException(e.getMessage());
        }

        throw new TUModelInstanciationException(getSession().getLabel("GLOBAL_PERIODE_INEXISTANT"));
    }

    /**
     * Retourne la date de début de la période (sans point).<br/>
     * Example : 31moisannee
     * 
     * @return
     */
    private String getDatePeriodeBegin() {
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(getAnnee()), Integer.parseInt(getMois()) - 1, 1);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(cal.getTime());
    }

    /**
     * Retourne la date de fin de la période (sans point).<br/>
     * Example : 31moisannee
     * 
     * @return
     */
    private String getDatePeriodeEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(getAnnee()), Integer.parseInt(getMois()) - 1, 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(cal.getTime());
    }

    /**
     * Retourne le taux officiel pour la dernière année trouvée <= 01.01.anneecotisation.
     * 
     * @param idExterneRubrique
     * @param anneeCotisation
     * @param tauxEmployeur
     *            true : tauxEmployeur, false tauxSalarie
     * @return
     * @throws TUModelInstanciationException
     */
    private BigDecimal getLastYearTauxOfficiel(String idExterneRubrique, String anneeCotisation, boolean tauxEmployeur)
            throws TUModelInstanciationException {
        CATauxRubriquesManager manager = new CATauxRubriquesManager();
        manager.setSession(getSession());
        manager.setLikeIdExterne(idExterneRubrique);

        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(anneeCotisation), 0, 1);

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        manager.setUntilDate(formatter.format(cal.getTime()));

        manager.setOrderBy(CATauxRubriquesManager.ORDER_BY_DATE_DESC);

        try {
            manager.find();

            if (manager.isEmpty()) {
                return null;
            }

            if (tauxEmployeur) {
                return new BigDecimal(((CATauxRubriques) manager.getFirstEntity()).getTauxEmployeur());
            } else {
                return new BigDecimal(((CATauxRubriques) manager.getFirstEntity()).getTauxSalarie());
            }
        } catch (Exception e) {
            throw new TUModelInstanciationException(e.getMessage());
        }
    }

    @Override
    protected void initBouclement(ITUModelBouclement bouclement) throws TUModelInstanciationException {
        try {
            bouclement.setEntete(getAnnee(), getMois(), getAnnee() + getMois());

            // Récupère les rubriques paramétrées
            String contributionsAf = TUPropertiesProvider.getInstance().getProperty(
                    IPropertiesNames.OSIRIS_RUBRIQUE_CONTRIBUTIONS_AF);
            String contributionsAfIndependants = TUPropertiesProvider.getInstance().getProperty(
                    IPropertiesNames.OSIRIS_RUBRIQUE_CONTRIBUTIONS_AF_INDEPENDANT);
            String remisesCotisations = TUPropertiesProvider.getInstance().getProperty(
                    IPropertiesNames.OSIRIS_RUBRIQUE_REMISES_COTISATIONS);
            // String quotePartCotChargeAcmApg = TUPropertiesProvider.getInstance().getProperty(
            // IPropertiesNames.OSIRIS_RUBRIQUE_PART_COT_CHARGE_ACM_APG);

            String fondsFormationProfNeuch = TUPropertiesProvider.getInstance().getProperty(
                    IPropertiesNames.OSIRIS_RUBRIQUE_FONDS_FORMATION_PROF_NE);
            String fondsFormationProfGeneve = TUPropertiesProvider.getInstance().getProperty(
                    IPropertiesNames.OSIRIS_RUBRIQUE_FONDS_FORMATION_PROF_GE);
            String fondsFormationProfZurich = TUPropertiesProvider.getInstance().getProperty(
                    IPropertiesNames.OSIRIS_RUBRIQUE_FONDS_FORMATION_PROF_ZH);

            // Génère les lignes de bouclement
            addBouclementAlfa(bouclement, ITUCSRubriqueListeDesRubriques.CS_CONTRIBUTIONS_AF_PAR_CANTON,
                    contributionsAf, false, globaz.naos.translation.CodeSystem.GENRE_ASS_PARITAIRE);

            addBouclementAlfa(bouclement, ITUCSRubriqueListeDesRubriques.CS_CONTRIBUTIONS_AF_PAR_CANTON_INDEPENDANTS,
                    contributionsAfIndependants, false, globaz.naos.translation.CodeSystem.GENRE_ASS_PERSONNEL);

            addBouclementAlfaSupplementLegal(
                    bouclement,
                    ITUCSRubriqueListeDesRubriques.CS_CONTRIBUTIONS_AF_SUPPLEMENT_LEGAL_CALCUL_DIFFERENTIEL_D_APRES_NAOS,
                    contributionsAf, true);

            addBouclementAlfaSupplementLegal(bouclement,
                    ITUCSRubriqueListeDesRubriques.CS_CONTRIBUTIONS_LEGALES_AF_PAR_CANTON, contributionsAf, false);

            // Gérer les remises
            addBouclementAlfaRemise(bouclement, ITUCSRubriqueListeDesRubriques.CS_REMISES_DE_COTISATIONS,
                    remisesCotisations);

            // Ne retourne rien et ne doit rien retourner !!! Inutile et faux !
            // this.addBouclementAlfa(bouclement,
            // ITUCSRubriqueListeDesRubriques.CS_PART_COTISATIONS_AVS_AC_A_CHARGE_DU_REGIME_ACM,
            // quotePartCotChargeAcmApg, false, "");

            // Gérer le FFPP
            addBouclementAlfa(bouclement, ITUCSRubriqueListeDesRubriques.CS_FONDS_DE_FORMATION_PROF_MONTANT_NE,
                    fondsFormationProfNeuch, true, "");
            addBouclementAlfa(bouclement, ITUCSRubriqueListeDesRubriques.CS_FONDS_DE_FORMATION_PROF_MONTANT_GE,
                    fondsFormationProfGeneve, true, "");
            addBouclementAlfa(bouclement, ITUCSRubriqueListeDesRubriques.CS_FONDS_DE_FORMATION_PROF_MONTANT_ZH,
                    fondsFormationProfZurich, true, "");

        } catch (Exception e) {
            throw new TUModelInstanciationException(e.getMessage());
        }
    }

    /**
     * @param canton
     * @param rubrique
     * @return
     */
    private String key(String canton, String rubrique) {
        return canton + rubrique;
    }

    /**
     * @param manager
     * @param data
     * @param codeSystem
     * @param idExterneRubrique
     * @param tauxEmployeur
     * @param i
     * @throws TUModelInstanciationException
     */
    private void treatBouclementAlfaSupplementLegal(BManager manager, HashMap<String, TULigneBouclement> data,
            String codeSystem, String idExterneRubrique, boolean tauxEmployeur, int i)
            throws TUModelInstanciationException {
        CABouclementAlfa bouclementAlfa = (CABouclementAlfa) manager.get(i);

        BigDecimal tauxOfficiel = getLastYearTauxOfficiel(idExterneRubrique.substring(0, 12),
                bouclementAlfa.getAnneeCotisation(), tauxEmployeur);

        if ((tauxOfficiel != null) && !JadeStringUtil.isDecimalEmpty(bouclementAlfa.getSumMontant())
                && !JadeStringUtil.isDecimalEmpty(bouclementAlfa.getSumMasse())
                && (bouclementAlfa.getTaux().compareTo(tauxOfficiel) == 1)) {
            FWCurrency montant = new FWCurrency(bouclementAlfa.getSumMontant());
            FWCurrency masse = new FWCurrency(bouclementAlfa.getSumMasse());

            tauxOfficiel = tauxOfficiel.divide(new BigDecimal(100.00), 5, BigDecimal.ROUND_HALF_EVEN);

            BigDecimal result = montant.getBigDecimalValue().subtract(
                    (masse.getBigDecimalValue().multiply(tauxOfficiel)));

            addMontantCanton(data, codeSystem, bouclementAlfa.getCanton(),
                    (new FWCurrency(result.toString())).toString());
        }
    }
}
