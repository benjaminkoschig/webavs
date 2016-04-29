package globaz.naos.db.tauxAssurance;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.translation.CodeSystem;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Classe utilitaire de gestion de taux par palier afin d'optimiser et de simplifier le calcul
 * 
 * @author DGI
 * 
 */

public class AFTauxVariableUtil {

    /**
     * Inner class repr�sentant une tranche de taux Contient la contribution minimum ainsi que la part de la masse
     * salariale � enlever Permet de simplifier les calculs de la cotisations finale.
     * 
     * @author DGI
     */
    public class TauxCalcul {
        // la cotisation jusqu'� cette tranche
        FWCurrency contriMinimum;
        // la masse � soustraire pour cette tranche
        FWCurrency masseMinimum;
        // le taux de cette tranche
        AFTauxAssurance tauxRang;

        /**
         * Constructeur
         * 
         * @param masse
         *            la masse � soustraire pour calculer le montant de la cotisation pour le rang donn�
         * @param contribution
         *            la contribution minimum � ajouter au montant calcul du rang donn�
         * @param taux
         *            le taux du rang donn�
         */
        public TauxCalcul(FWCurrency masse, FWCurrency contribution, AFTauxAssurance taux) {
            masseMinimum = masse;
            contriMinimum = contribution;
            tauxRang = taux;

        }

        /**
         * Calcul le montant de la contisation en fonction de la masse annuelle donn�e
         * 
         * @param session
         *            la session � utiliser
         * @param masse
         *            la masse salariale annuelle
         * @param cotiMinim
         *            mettre � true si une cotisation minimale doit �tre garantie
         * @return le montant de la cotisation
         * @throws Exception
         *             si une erreur survient lors du calcul
         */
        public String getMontantCotisation(BSession session, String masse, boolean cotiMinim) throws Exception {
            BigDecimal masseAFacturer = new BigDecimal(JANumberFormatter.deQuote(masse));
            if ("true".equals(session.getApplication().getProperty(AFApplication.PROPERTY_IS_TAUX_PAR_PALIER))) {
                // enlever la partie des rangs inf�rieurs
                masseAFacturer = masseAFacturer.subtract(masseMinimum.getBigDecimalValue());

                // utiliser le taux de cette tranche pour la masse restante
                BigDecimal tauxCalcul = new BigDecimal(JANumberFormatter.deQuote(tauxRang.getValeurTotal()));
                if (JadeStringUtil.isDecimalEmpty(masse)) {
                    tauxCalcul = new BigDecimal("0.00");
                } else {
                    tauxCalcul = tauxCalcul.multiply(masseAFacturer);
                }
                masseAFacturer = tauxCalcul.divide(new BigDecimal(JANumberFormatter.deQuote(tauxRang.getFraction())));

                // finalement, ajouter la contribution minimale
                // si masse � facturer < 0 (taux forc� avec masse en dessous de la tranche), on effectue un calcul sans
                // taux par palier
                if (masseAFacturer.signum() == -1) { // isNegative()
                    // masseAFacturer = new FWCurrency(0);
                    return new FWCurrency(new FWCurrency(JANumberFormatter.deQuote(masse)).doubleValue()
                            * tauxRang.getTauxDouble()).toStringFormat();
                }
                masseAFacturer = masseAFacturer.add(contriMinimum.getBigDecimalValue());
                return masseAFacturer.toString();
            } else {
                FWCurrency coti = new FWCurrency(masseAFacturer.doubleValue() * tauxRang.getTauxDouble());
                if (cotiMinim && (coti.compareTo(contriMinimum) < 0)) {
                    // force cotisation minimale si inf�rieur
                    return contriMinimum.toStringFormat();
                } else {
                    return coti.toStringFormat();
                }
            }
        }

        public AFTauxAssurance getTauxRang() {
            return tauxRang;
        }

    }

    // r�f�rence singleton
    private static HashMap<String, AFTauxVariableUtil> singletons = new HashMap<String, AFTauxVariableUtil>();

    /**
     * Cr�er une instance unique pour cet utilitaire par assurance (ne devrait �tre utilis� que pour les frais
     * administratifs)
     * 
     * @return l'instance de cette classe
     */
    public static AFTauxVariableUtil getInstance(String assuraneId) throws Exception {
        AFTauxVariableUtil instance = AFTauxVariableUtil.singletons.get(assuraneId);
        if (instance == null) {
            // il n'existe pas d'instance pour cette assurance
            instance = new AFTauxVariableUtil(assuraneId);
            AFTauxVariableUtil.singletons.put(assuraneId, instance);
        }
        return instance;
    }

    // Application NAOS
    private AFApplication app = null;

    // ID de l'assurance
    private String assuranceId = null;

    // liste des taux variable par ann�e
    private HashMap<String, HashMap<String, TauxCalcul>> tauxList = new HashMap<String, HashMap<String, TauxCalcul>>();

    /**
     * Constructeur non public pour usage en singleton
     */
    private AFTauxVariableUtil(String assuraneId) throws Exception {
        assuranceId = assuraneId;
        app = (AFApplication) GlobazServer.getCurrentSystem().getApplication(AFApplication.DEFAULT_APPLICATION_NAOS);
    }

    /**
     * Recherche le calcul du taux variable pour la p�riode et le taux donn�
     * 
     * @param session
     *            la session � utiliser
     * @param masse
     *            la masse salariale
     * @param p�riode
     *            la p�riode � facturer
     * @return la calcul du taux variable
     * @throws Exception
     *             si une erreur survient lors de la recherche
     */
    public TauxCalcul getCalcul(BSession session, String masse, String periode) throws Exception {
        if ((session == null) || (periode == null)) {
            return null;
        }

        // charge les calculs de taux pour l'assurance et la p�riode donn�e
        HashMap<String, TauxCalcul> tauxCalculs = loadTauxVariable(session, periode);
        if (tauxCalculs == null) {
            return null;
        }

        if (JadeStringUtil.isDecimalEmpty(masse)) {
            masse = "1"; // permet de retourner le premier rang
        } else {
            masse = JANumberFormatter.deQuote(masse);
        }

        // test si n�gatif
        BigDecimal masseBig = new BigDecimal(masse);
        if (masseBig.compareTo(new BigDecimal("0")) < 0) {
            masse = "1";
        }
        // recherche du rang concern�
        TauxCalcul calcul = null;
        Iterator<TauxCalcul> it = tauxCalculs.values().iterator();
        while (it.hasNext()) {
            TauxCalcul calculIter = it.next();
            if (calculIter.masseMinimum.doubleValue() < Double.parseDouble(masse)) {
                if ((calcul == null) || (calcul.masseMinimum.doubleValue() < calculIter.masseMinimum.doubleValue())) {
                    calcul = calculIter;
                }
            }
        }
        return calcul;
    }

    /**
     * Recherche le calcul du taux variable pour la p�riode et le taux donn�
     * 
     * @param p�riode
     *            la p�riode � facturer
     * @param taux
     *            le taux utilis�
     * @return la calcul du taux variable
     * @throws Exception
     *             si une erreur survient lors de la recherche
     */
    private TauxCalcul getCalcul(String periode, AFTauxAssurance taux) throws Exception {
        // charge les calculs de taux pour l'assurance et la p�riode donn�e
        HashMap<?, ?> tauxCalculs = loadTauxVariable(taux.getSession(), periode);
        if (tauxCalculs == null) {
            return null;
        }
        // recherche du clacul correspondant
        TauxCalcul tauxCalcul = (TauxCalcul) tauxCalculs.get(taux.getTauxAssuranceId());
        if (tauxCalcul == null) {
            // instancier les currencies
            FWCurrency masseMinimum = new FWCurrency(taux.getTranche());
            FWCurrency masse = new FWCurrency(taux.getTranche());
            // trouver le taux de calcul
            double calcul = Double.parseDouble(JANumberFormatter.deQuote(taux.getValeurTotal()))
                    / Double.parseDouble(JANumberFormatter.deQuote(taux.getFraction()));
            // contribution minimum
            FWCurrency contriMinimum = new FWCurrency(masse.doubleValue() * calcul);
            // instancier un nouveau calcul de type TauxCalcul
            tauxCalcul = new TauxCalcul(masseMinimum, contriMinimum, taux);
        }

        return tauxCalcul;
    }

    /**
     * Recherche le calcul du taux variable pour le taux donn�
     * 
     * @param session
     *            la session � utiliser
     * @param p�riode
     *            la p�riode � facturer
     * @param taux
     *            le taux donn�
     * @return la calcul du taux variable
     * @throws Exception
     *             si une erreur survient lors de la recherche
     */
    private TauxCalcul getCalculTaux(BSession session, String periode, String taux) throws Exception {
        if ((session == null) || (periode == null)) {
            return null;
        }
        // charge les calculs de taux pour l'assurance et la p�riode donn�e
        HashMap<?, ?> tauxCalculs = loadTauxVariable(session, periode);
        if (tauxCalculs == null) {
            return null;
        }
        // recherche du rang concern�
        TauxCalcul calcul = null;
        Iterator<?> it = tauxCalculs.values().iterator();
        while (it.hasNext() && (calcul == null)) {
            TauxCalcul calculIter = (TauxCalcul) it.next();
            if (calculIter.tauxRang.getValeurTotal().equals(taux)) {
                calcul = calculIter;
            }
        }
        return calcul;
    }

    /**
     * Calcul le montant de la cotisation pour la masse salariale donn�e
     * 
     * @param session
     *            la session � utiliser
     * @param masse
     *            la masse salariale
     * @param p�riode
     *            la p�riode � facturer
     * @return le montant de la cotisation
     * @exception Exception
     *                si une erreur survient lors de la recherche
     */
    public String getMontantCotisation(BSession session, String masse, String periode) throws Exception {
        // recherche du calcul
        TauxCalcul calcul = this.getCalcul(session, JANumberFormatter.deQuote(masse), periode);
        if (calcul == null) {
            return "";
        }
        // calcul de la cotisation: contribution minimale + masse en plus de la
        // masse minimum de la tranche * taux
        return calcul.getMontantCotisation(session, JANumberFormatter.deQuote(masse), false);
    }

    /**
     * Calcul le montant de la cotisation pour la masse et le taux donn�s
     * 
     * @param session
     *            la session � utiliser
     * @param masse
     *            la masse salariale
     * @param p�riode
     *            la p�riode � facturer
     * @param taux
     *            le taux utilis�
     * @param cotiMinim
     *            mettre � true si une cotisation minimale doit �tre assur�e
     * @return le montant de la cotisation
     * @exception Exception
     *                si une erreur survient lors de la recherche
     */
    public String getMontantCotisation(BSession session, String masse, String periode, String taux, boolean cotiMinim)
            throws Exception {
        // chargement des calculs du taux de l'assurance si pas d�j� effectu�
        TauxCalcul calcul = getCalculTaux(session, periode, taux);
        if (calcul == null) {
            return "";
        }
        // calcul de la cotisation: contribution minimale + masse en plus de la
        // masse minimum de la tranche * taux
        return calcul.getMontantCotisation(session, JANumberFormatter.deQuote(masse), cotiMinim);
    }

    /**
     * Calcul le montant de la cotisation pour la masse et le taux donn�s
     * 
     * @param masse
     *            la masse salariale
     * @param p�riode
     *            la p�riode � facturer
     * @param taux
     *            le taux utilis�
     * @param cotiMinim
     *            mettre � true si une cotisation minimale doit �tre assur�e
     * @return le montant de la cotisation
     * @exception Exception
     *                si une erreur survient lors de la recherche
     */
    public String getMontantCotisation(String masse, String periode, AFTauxAssurance taux, boolean cotiMinim)
            throws Exception {
        // chargement des calculs du taux de l'assurance si pas d�j� effectu�
        if ((taux == null) || taux.isNew()) {
            // erreur
            return "";
        }
        TauxCalcul calcul = this.getCalcul(periode, taux);
        if (calcul == null) {
            return "";
        }
        // calcul de la cotisation: contribution minimale + masse en plus de la
        // masse minimum de la tranche * taux
        return calcul.getMontantCotisation(taux.getSession(), JANumberFormatter.deQuote(masse), cotiMinim);
    }

    /**
     * Retourne le taux variable en fonction de la masse et la p�riode donn�e
     * 
     * @param session
     *            la session � utiliser
     * @param masse
     *            la masse salariale
     * @param p�riode
     *            la p�riode � facturer
     * @return le taux de cotisation (palier)
     * @exception Exception
     *                si une erreur survient lors de la recherche
     */
    public AFTauxAssurance getTaux(BSession session, String masse, String periode) throws Exception {
        // recherche du calcul
        TauxCalcul calcul = this.getCalcul(session, JANumberFormatter.deQuote(masse), periode);
        if (calcul == null) {
            return null;
        }
        if (!app.isTauxParPalier()) {
            return calcul.tauxRang;
        } else {
            // on retourne un taux moyen calcul�
            AFTauxAssurance tauxCalc = new AFTauxAssurance();
            calcul.tauxRang.copyDataToEntity(tauxCalc);
            // tauxCalc.setSession(session);
            // tauxCalc.setAssuranceId(assuranceId);
            // tauxCalc.setFraction("100");
            // tauxCalc.setGenreValeur(CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE);
            // tauxCalc.setTypeId(CodeSystem.TYPE_TAUX_DEFAUT);
            tauxCalc.setAffichageTaux(calcul.tauxRang.isAffichageTaux());
            tauxCalc.setValeurEmploye("0");
            BigDecimal masseDec = new BigDecimal(JANumberFormatter.deQuote(masse));
            // calcul de la cotisation annuelle avec les taux variables
            String cotiResult = this.getMontantCotisation(session, masse, periode);
            if (!JadeStringUtil.isIntegerEmpty(cotiResult)) {
                BigDecimal montantCal = new BigDecimal(JANumberFormatter.deQuote(cotiResult));
                // calcul du taux moyen
                tauxCalc.setValeurEmployeur(montantCal.multiply(new BigDecimal("100"))
                        .divide(masseDec, 5, BigDecimal.ROUND_HALF_EVEN).toString());
                return tauxCalc;
            } else {
                return calcul.tauxRang;
            }
        }
    }

    /**
     * Retourne le taux moyen en fonction de l'affiliation et la p�riode donn�e
     * 
     * @param session
     *            la session � utiliser
     * @param idAffiliation
     *            l'affiliation concern�e
     * @param p�riode
     *            la p�riode � facturer, utile si recherche du taux par d�faut
     * @return le dernier taux moyen calcul�
     * @exception Exception
     *                si une erreur survient lors de la recherche
     */
    public AFTauxAssurance getTauxMoyen(BSession session, String idAffiliation, String periode) throws Exception {
        // recherche du taux moyen
        AFTauxMoyenManager mgr = new AFTauxMoyenManager();
        mgr.setSession(session);
        mgr.setForIdAffiliation(idAffiliation);
        if (periode != null) {
            if (periode.length() == 10) {
                mgr.setUntilAnnee(periode.substring(6));
            } else if (periode.length() == 4) {
                mgr.setUntilAnnee(periode);
            }
        }
        mgr.find(BManager.SIZE_USEDEFAULT);
        AFTauxAssurance result = null;
        if (mgr.isEmpty()) {
            // aucun taux trouv�, on retourne le taux de la premi�re tranche
            // if("true".equals(session.getApplication().getProperty(AFApplication.PROPERTY_IS_TAUX_PAR_PALIER)))
            // {
            result = getTaux(session, "0", periode);
            // } else {
            // return null;
            // }
        } else {
            // on retourne le dernier taux calcul�
            result = ((AFTauxMoyen) mgr.getFirstEntity()).getTaux();
            result.setAffichageTaux(false);
        }
        return result;
    }

    /**
     * Charge les taux variable de l'assurance donn�e et calcul ensuite les montants de cotisations minimum par palier
     * ainsi que les tranches � d�duire afin de simplifier le calcul.
     * 
     * @param session
     *            la session � utiliser pour la recherche
     * @param forDate
     *            date � laquelle les taux doivent �tre r�cup�r�s
     * @return la liste des calculs de taux
     * @exception Exception
     *                si une erreur survient lors de la recherche
     */
    private HashMap<String, TauxCalcul> loadTauxVariable(BSession session, String forDate) throws Exception {
        if (forDate == null) {
            return null;
        }
        String annee = String.valueOf(new JADate(forDate).getYear());
        if (!tauxList.containsKey(annee)) {
            AFTauxAssuranceManager manager = new AFTauxAssuranceManager();
            manager.setSession(session);
            manager.setForGenreValeur(CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE);
            manager.setForIdAssurance(assuranceId);
            manager.setForDate(forDate);
            manager.setOrderByDebutDescRang();
            manager.find();
            if (manager.size() > 0) {
                // si des taux existent, on les enregistre pour l'ann�e
                // concern�e
                HashMap<String, TauxCalcul> tauxCaluls = new HashMap<String, TauxCalcul>();
                FWCurrency contriMinimum = new FWCurrency();
                FWCurrency masseMinimum = new FWCurrency();
                // FWCurrency masseRangPresc = new FWCurrency();
                // AFTauxAssurance tauxRangPresc = null;
                // it�ration sur tous les taux trouv�s, ordr�s par rang
                int lastRang = -1;
                for (int i = 0; i < manager.size(); i++) {
                    AFTauxAssurance taux = (AFTauxAssurance) manager.getEntity(i);
                    // ne pas afficher le taux si taux par palier (exception
                    // pour cot min et premier rang)
                    if (app.isTauxParPalier()) {
                        if ((i != 0) || !app.isCotisationMinimale()) {
                            taux.setAffichageTaux(false);
                        }
                    }

                    if (!JadeStringUtil.isEmpty(taux.getRang())) {
                        int rang = Integer.parseInt(taux.getRang());
                        if (rang < lastRang) {
                            break;
                        }
                        lastRang = rang;
                    }
                    // cr�ation de l'instance de calcul du taux
                    TauxCalcul calcul = new TauxCalcul(masseMinimum, contriMinimum, taux);
                    tauxCaluls.put(taux.getTauxAssuranceId(), calcul);
                    // masse
                    FWCurrency masse = new FWCurrency(taux.getTranche());
                    double tauxCalcul = Double.parseDouble(JANumberFormatter.deQuote(taux.getValeurTotal()))
                            / Double.parseDouble(JANumberFormatter.deQuote(taux.getFraction()));
                    // calcul de la contribution minimum pour le rang suivant,
                    // ainsi que la masse � soustraire
                    if (app.isTauxParPalier()) {
                        masse.sub(masseMinimum);
                        contriMinimum = new FWCurrency(contriMinimum.doubleValue());
                        contriMinimum.add(new FWCurrency(masse.doubleValue() * tauxCalcul));
                    } else {
                        contriMinimum = new FWCurrency(masse.doubleValue() * tauxCalcul);
                    }
                    // masseMinimum = masse;
                    masseMinimum = new FWCurrency(taux.getTranche());
                    // masseRangPresc = new FWCurrency(taux.getTranche());
                    // masseMinimum.add(taux.getTranche());
                    // contriMinimum.add(taux.getTranche());
                    // tauxRangPresc = taux;
                }
                // ajouter la liste pour l'ann�e concern�e
                tauxList.put(annee, tauxCaluls);
                return tauxCaluls;
            } else {
                // aucun taux variable, retourner false
                return null;
            }
        } else {
            return tauxList.get(annee);
        }
    }

    public String toString(BSession session, String periode) throws Exception {
        if ((session == null) || (periode == null)) {
            return "";
        }
        // charge les calculs de taux pour l'assurance et la p�riode donn�e
        HashMap<?, ?> tauxCalculs = loadTauxVariable(session, periode);
        if (tauxCalculs == null) {
            return "";
        }
        // recherche du rang concern�
        TauxCalcul calcul = null;
        Iterator<?> it = tauxCalculs.values().iterator();
        StringBuffer result = new StringBuffer();
        result.append("Rang   taux   masse min   coti min\n");
        int rang = 1;
        while (it.hasNext() && (calcul == null)) {
            TauxCalcul calculIter = (TauxCalcul) it.next();
            result.append(rang + "   " + calculIter.tauxRang.getValeurTotal() + "   " + calculIter.masseMinimum + "   "
                    + calculIter.contriMinimum + "\n");
        }
        return result.toString();
    }

}
