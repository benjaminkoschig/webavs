package globaz.external.tucana;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGExerciceComptableManager;
import globaz.helios.db.comptes.CGExtendedMvtCompteListViewBean;
import globaz.helios.db.comptes.CGExtendedMvtCompteViewBean;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGPeriodeComptableManager;
import globaz.helios.db.comptes.CGPlanComptableManager;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.helios.db.comptes.CGSolde;
import globaz.helios.db.comptes.CGSoldeManager;
import globaz.helios.translation.CodeSystem;
import globaz.itucana.constantes.IPropertiesNames;
import globaz.itucana.constantes.ITUCSRubriqueListeDesRubriques;
import globaz.itucana.exception.TUModelInstanciationException;
import globaz.itucana.model.ITUModelBouclement;
import globaz.itucana.process.TUProcessusBouclement;
import globaz.itucana.properties.TUPropertiesProvider;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CGProcessBouclementAlfa extends TUProcessusBouclement {

    public static final String CANTON_GE = "GE";
    public static final String CANTON_NE = "NE";
    public static final String CANTON_ZH = "ZH";

    private static final String FFPP = "FFPP";

    private static final String SOLDE_0_WHEN_NOTHING_FOUND = "0";

    /**
     * Constructor.
     * 
     * @param annee
     * @param mois
     */
    public CGProcessBouclementAlfa(String annee, String mois) {
        super(annee, mois);
    }

    /**
     * Recherche de la période comptable en fonction de l'année et du mois passé au constructeur.<br/>
     * Le période doit être une période du plan AVS 900.
     * 
     * @return
     * @throws Exception
     */
    public CGPeriodeComptable getActualPeriode() throws Exception {
        CGPeriodeComptableManager manager = new CGPeriodeComptableManager();
        manager.setSession(getSession());

        String forDate = "01." + getMoisFormatted() + "." + getAnnee();
        manager.setForDateInPeriode(forDate);

        manager.find();

        for (int i = 0; i < manager.size(); i++) {
            CGPeriodeComptable tmpPeriode = (CGPeriodeComptable) manager.get(i);

            if (tmpPeriode.getExerciceComptable().getMandat().isEstComptabiliteAVS().booleanValue()
                    && tmpPeriode.getExerciceComptable().getMandat().getIdMandat()
                            .equals(CGMandat.MANDAT_AVS_DEFAULT_NUMBER)) {
                return tmpPeriode;
            }
        }

        throw new Exception(getSession().getLabel("GLOBAL_PERIODE_INEXISTANT"));
    }

    /**
     * Retourne l'idCompte en fonction de l'idExterne type xxxx.xxxx.xxxx.
     * 
     * @param idExerciceComptable
     * @param idExterne
     * @return Le compte. Null si aucun compte résolu.
     * @throws Exception
     */
    public String getIdCompte(String idExerciceComptable, String idExterne) throws Exception {
        CGPlanComptableManager manager = new CGPlanComptableManager();
        manager.setSession(getSession());

        manager.setForIdExerciceComptable(idExerciceComptable);
        manager.setForIdExterne(idExterne);

        manager.find();

        if (manager.hasErrors()) {
            throw new Exception(getSession().getLabel("AUCUN_COMPTE_RESOLU") + " (" + idExterne + ")");
        }

        if (manager.isEmpty()) {
            return null;
        }

        return ((CGPlanComptableViewBean) manager.getFirstEntity()).getIdCompte();
    }

    /**
     * Retourne le mois formaté. <br/>
     * Exemple : Pour janvier 1 => 01.
     * 
     * @return
     */
    private String getMoisFormatted() {
        if (getMois().length() == 2) {
            return getMois();
        } else if (getMois().length() == 1) {
            return CGProcessBouclementAlfa.SOLDE_0_WHEN_NOTHING_FOUND + getMois();
        } else {
            return null;
        }
    }

    /**
     * Retourne le manager de mouvements.
     * 
     * @param periode
     * @param idCompte
     * @return
     */
    private CGExtendedMvtCompteListViewBean getMouvementsManager(CGPeriodeComptable periode, String idCompte) {
        CGExtendedMvtCompteListViewBean manager = new CGExtendedMvtCompteListViewBean();
        manager.setSession(getSession());

        manager.setForIdPeriodeComptable(periode.getIdPeriodeComptable());
        manager.setForIdCompte(idCompte);
        manager.wantForEstActive(true);
        manager.setForIdExerciceComptable(periode.getIdExerciceComptable());
        manager.setReqComptabilite(CodeSystem.CS_DEFINITIF);
        manager.setReqVue(CodeSystem.CS_GENERAL);

        manager.changeManagerSize(BManager.SIZE_NOLIMIT);
        return manager;
    }

    /**
     * Retrouve la période précédente. Si la période actuelle est celle de janvier la période précédente sera la période
     * ANNUEL de l'année passée.
     * 
     * @param periode
     * @return
     * @throws Exception
     */
    private CGPeriodeComptable getPeriodePrecedente(CGPeriodeComptable periode) throws Exception {
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.MONTH, Integer.parseInt(getMois()) - 1);

        if (myCalendar.get(Calendar.MONTH) == Calendar.JANUARY) {
            CGExerciceComptableManager exerManager = new CGExerciceComptableManager();
            exerManager.setSession(getSession());

            exerManager.setForIdMandat(periode.getExerciceComptable().getIdMandat());
            exerManager.setUntilDateFin("31.12." + (Integer.parseInt(getAnnee()) - 1));

            exerManager.setOrderBy(CGExerciceComptableManager.TRI_DATE_FIN_DESC);

            exerManager.find();

            if (exerManager.isEmpty()) {
                return null;
            }

            CGExerciceComptable exercicePrecedent = (CGExerciceComptable) exerManager.getFirstEntity();

            CGPeriodeComptableManager manager = new CGPeriodeComptableManager();
            manager.setSession(getSession());

            manager.setForIdExerciceComptable(exercicePrecedent.getIdExerciceComptable());
            manager.setForCode(CGPeriodeComptable.CS_CODE_ANNUEL);

            manager.find();

            if (manager.isEmpty()) {
                return null;
            }

            return (CGPeriodeComptable) manager.getFirstEntity();
        } else {
            CGPeriodeComptableManager manager = new CGPeriodeComptableManager();
            manager.setSession(getSession());

            manager.setForIdExerciceComptable(periode.getIdExerciceComptable());
            manager.setUntilDateFin(periode.getDateDebut());

            manager.setOrderBy(CGPeriodeComptableManager.TRI_DATE_FIN_AND_TYPE_DESC);

            manager.find();

            if (manager.isEmpty()) {
                return null;
            } else {
                return (CGPeriodeComptable) manager.getFirstEntity();
            }
        }
    }

    /**
     * Retourne le solde des crédits des mouvements générés sur le compte passé en paramètre et dont le libellé ne
     * contient pas FFPP.
     * 
     * @param periode
     * @param idCompte
     * @param idExterneContreCompte
     * @return Si aucun solde n'est résolu 0 est retourné.
     * @throws Exception
     */
    public String getSoldeCreditForPeriode(CGPeriodeComptable periode, String idCompte, String idExterneContreCompte)
            throws Exception {
        if (JadeStringUtil.isIntegerEmpty(idCompte)) {
            return CGProcessBouclementAlfa.SOLDE_0_WHEN_NOTHING_FOUND;
        }

        CGExtendedMvtCompteListViewBean manager = getMouvementsManager(periode, idCompte);

        manager.find();

        FWCurrency totalAvoir = new FWCurrency();
        for (int i = 0; i < manager.size(); i++) {
            CGExtendedMvtCompteViewBean mvt = (CGExtendedMvtCompteViewBean) manager.get(i);

            if (mvt.getIdExtCtrCompte().equals(idExterneContreCompte)
                    && (mvt.getLibelle().toUpperCase().indexOf(CGProcessBouclementAlfa.FFPP) == -1)) {
                totalAvoir.add(mvt.getAvoir());
            }
        }

        if (totalAvoir.isPositive()) {
            totalAvoir.negate();
        }

        return totalAvoir.toString();
    }

    /**
     * Retourne le solde des débits des mouvements générés sur le compte passé en paramètre et dont le libellé ne
     * contient pas FFPP.
     * 
     * @param periode
     * @param idCompte
     * @param idExterneContreCompte
     * @return Si aucun solde n'est résolu 0 est retourné.
     * @throws Exception
     */
    public String getSoldeDebitForPeriode(CGPeriodeComptable periode, String idCompte, String idExterneContreCompte)
            throws Exception {
        if (JadeStringUtil.isIntegerEmpty(idCompte)) {
            return CGProcessBouclementAlfa.SOLDE_0_WHEN_NOTHING_FOUND;
        }

        CGExtendedMvtCompteListViewBean manager = getMouvementsManager(periode, idCompte);

        manager.find();

        FWCurrency totalDoit = new FWCurrency();
        for (int i = 0; i < manager.size(); i++) {
            CGExtendedMvtCompteViewBean mvt = (CGExtendedMvtCompteViewBean) manager.get(i);

            if (mvt.getIdExtCtrCompte().equals(idExterneContreCompte)
                    && (mvt.getLibelle().toUpperCase().indexOf(CGProcessBouclementAlfa.FFPP) == -1)) {
                totalDoit.add(mvt.getDoit());
            }
        }

        return totalDoit.toString();
    }

    /**
     * Retourne le solde du compte pour la période actuelle.
     * 
     * @param periode
     * @param idCompte
     * @return Si aucun solde n'est résolu 0 est retourné.
     * @throws Exception
     */
    public String getSoldeForPeriode(CGPeriodeComptable periode, String idCompte) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(idCompte)) {
            return CGProcessBouclementAlfa.SOLDE_0_WHEN_NOTHING_FOUND;
        }

        CGSoldeManager manager = new CGSoldeManager();
        manager.setSession(getSession());

        manager.setForIdPeriodeComptable(periode.getIdPeriodeComptable());
        manager.setForIdExerComptable(periode.getIdExerciceComptable());
        manager.setForEstPeriode(new Boolean(true));
        manager.setForIdCompte(idCompte);

        manager.find();

        if (manager.isEmpty()) {
            return CGProcessBouclementAlfa.SOLDE_0_WHEN_NOTHING_FOUND;
        } else {
            FWCurrency solde = new FWCurrency();
            for (int i = 0; i < manager.size(); i++) {
                solde.add(((CGSolde) manager.get(i)).getSolde());
            }

            return solde.toString();
        }
    }

    /**
     * Retourne le solde des mouvements générés sur le compte passé en paramètre et dont le libellé contient FFPP et le
     * canton.
     * 
     * @param periode
     * @param idCompte
     * @param canton
     * @return Si aucun solde n'est résolu 0 est retourné.
     * @throws Exception
     */
    public String getSoldeForPeriode(CGPeriodeComptable periode, String idCompte, String canton) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(idCompte)) {
            return CGProcessBouclementAlfa.SOLDE_0_WHEN_NOTHING_FOUND;
        }

        CGExtendedMvtCompteListViewBean manager = getMouvementsManager(periode, idCompte);

        manager.find();

        FWCurrency totalSolde = new FWCurrency();
        for (int i = 0; i < manager.size(); i++) {
            CGExtendedMvtCompteViewBean mvt = (CGExtendedMvtCompteViewBean) manager.get(i);

            if (isLibelleMvtValid(mvt.getLibelle(), canton)) {
                totalSolde.add(mvt.getMontantBase());
            }
        }

        return totalSolde.toString();
    }

    /**
     * Retourne le solde cumulé pour les périodes précédentes.
     * 
     * @param periode
     * @param idCompte
     * @return Si aucun solde n'est résolu 0 est retourné.
     * @throws Exception
     */
    public String getSoldeUntilPeriode(CGPeriodeComptable periode, String idCompte) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(idCompte)) {
            return CGProcessBouclementAlfa.SOLDE_0_WHEN_NOTHING_FOUND;
        }

        CGPlanComptableManager manager = new CGPlanComptableManager();
        manager.setSession(getSession());

        manager.setForIdCompte(idCompte);
        manager.setReqComptabilite(CodeSystem.CS_DEFINITIF);

        CGPeriodeComptable periodePrecendente = getPeriodePrecedente(periode);

        if (periodePrecendente == null) {
            return CGProcessBouclementAlfa.SOLDE_0_WHEN_NOTHING_FOUND;
        }

        manager.setInclurePeriodesPrec(new Boolean(true));
        manager.setReqForListPeriodesComptable(periodePrecendente.getIdPeriodeComptable());
        manager.setForIdExerciceComptable(periodePrecendente.getIdExerciceComptable());

        manager.changeManagerSize(BManager.SIZE_NOLIMIT);

        manager.find();

        if (manager.isEmpty()) {
            return CGProcessBouclementAlfa.SOLDE_0_WHEN_NOTHING_FOUND;
        } else {
            FWCurrency solde = new FWCurrency();
            for (int i = 0; i < manager.size(); i++) {
                solde.add(((CGPlanComptableViewBean) manager.get(i)).getSolde());
            }

            return solde.toString();
        }
    }

    /**
     * @see TUProcessusBouclement.initBouclement(ITUModelBouclement bouclement) throws TUModelInstanciationException;
     */
    @Override
    protected void initBouclement(ITUModelBouclement bouclement) throws TUModelInstanciationException {
        try {
            bouclement.setEntete(getAnnee(), getMois(), getAnnee() + getMois());

            String ccpAlfa = TUPropertiesProvider.getInstance().getProperty(IPropertiesNames.HELIOS_RUBRIQUE_CCP_ALFA);
            String quotePartAdmAlfa = TUPropertiesProvider.getInstance().getProperty(
                    IPropertiesNames.HELIOS_RUBRIQUE_QUOTE_PART_ADM_ALFA);
            String quotePartAdmnistationAcmMilitaire = TUPropertiesProvider.getInstance().getProperty(
                    IPropertiesNames.HELIOS_RUBRIQUE_QUOTE_PART_ADM_ACM_MILITAIRE);
            String quotePartAdmnistationAcmMaternite = TUPropertiesProvider.getInstance().getProperty(
                    IPropertiesNames.HELIOS_RUBRIQUE_QUOTE_PART_ADM_ACM_MATERNITE);
            String ccSiege = TUPropertiesProvider.getInstance().getProperty(IPropertiesNames.HELIOS_RUBRIQUE_CC_SIEGE);
            String interetsCcp = TUPropertiesProvider.getInstance().getProperty(
                    IPropertiesNames.HELIOS_RUBRIQUE_INTERETS_CCP);

            CGPeriodeComptable periode = getActualPeriode();

            bouclement.addLine("", ITUCSRubriqueListeDesRubriques.CS_QUOTEPART_AUX_FRAIS_D_ADMINISTRATION_ALFA,
                    this.getSoldeForPeriode(periode, getIdCompte(periode.getIdExerciceComptable(), quotePartAdmAlfa)));
            bouclement.addLine(
                    "",
                    ITUCSRubriqueListeDesRubriques.CS_QUOTEPART_AUX_FRAIS_D_ADMINISTRATION_ACM_MILITAIRE,
                    this.getSoldeForPeriode(periode,
                            getIdCompte(periode.getIdExerciceComptable(), quotePartAdmnistationAcmMilitaire)));
            bouclement.addLine(
                    "",
                    ITUCSRubriqueListeDesRubriques.CS_QUOTEPART_AUX_FRAIS_D_ADMINISTRATION_ACM_MATERNITE,
                    this.getSoldeForPeriode(periode,
                            getIdCompte(periode.getIdExerciceComptable(), quotePartAdmnistationAcmMaternite)));

            String tmpIdCompteCCSiege = getIdCompte(periode.getIdExerciceComptable(), ccSiege);
            bouclement.addLine("", ITUCSRubriqueListeDesRubriques.CS_VERSEMENT_AGENCE_SIEGE_SANS_FFPP,
                    getSoldeDebitForPeriode(periode, tmpIdCompteCCSiege, ccpAlfa));
            bouclement.addLine("", ITUCSRubriqueListeDesRubriques.CS_VERSEMENT_SIEGE_AGENCE_SANS_FFPP,
                    getSoldeCreditForPeriode(periode, tmpIdCompteCCSiege, ccpAlfa));

            bouclement.addLine(CGProcessBouclementAlfa.CANTON_GE.toLowerCase(),
                    ITUCSRubriqueListeDesRubriques.CS_VERSEMENT_AGENCE_SIEGE_FFPP_GE,
                    this.getSoldeForPeriode(periode, tmpIdCompteCCSiege, CGProcessBouclementAlfa.CANTON_GE));
            bouclement.addLine(CGProcessBouclementAlfa.CANTON_NE.toLowerCase(),
                    ITUCSRubriqueListeDesRubriques.CS_VERSEMENT_AGENCE_SIEGE_FFPP_NE,
                    this.getSoldeForPeriode(periode, tmpIdCompteCCSiege, CGProcessBouclementAlfa.CANTON_NE));
            bouclement.addLine(CGProcessBouclementAlfa.CANTON_ZH.toLowerCase(),
                    ITUCSRubriqueListeDesRubriques.CS_VERSEMENT_AGENCE_SIEGE_FFPP_ZH,
                    this.getSoldeForPeriode(periode, tmpIdCompteCCSiege, CGProcessBouclementAlfa.CANTON_ZH));

            bouclement.addLine("",
                    ITUCSRubriqueListeDesRubriques.CS_SOLDE_A_LA_FIN_DU_MOIS_PRECEDENT_SELON_COMPTABILITE,
                    getSoldeUntilPeriode(periode, tmpIdCompteCCSiege));
            bouclement.addLine("", ITUCSRubriqueListeDesRubriques.CS_INTERETS_NET_DU_CCP,
                    this.getSoldeForPeriode(periode, getIdCompte(periode.getIdExerciceComptable(), interetsCcp)));
        } catch (Exception e) {
            throw new TUModelInstanciationException(e.getMessage());
        }
    }

    /**
     * Test si le libellé du mouvement contient FFPP et le canton (NE ou GE).
     * 
     * @param libelle
     * @param canton
     * @return
     */
    private boolean isLibelleMvtValid(String libelle, String canton) {
        String regex = CGProcessBouclementAlfa.FFPP.concat("([\\W]*)").concat(canton);

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(libelle);

        return matcher.find();
    }
}
