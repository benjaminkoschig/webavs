package globaz.phenix.db.principale;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.toolbox.CPToolBox;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;

public class CPDonneesCalcul extends BEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_ACT_ACCESSOIRE = "600040";
    public final static String CS_CAPITAL_ARRONDI = "600011";
    public final static String CS_COTISATION_BRUT = "600025";
    public final static String CS_FORTUNE_PRORATA = "600008";
    public final static String CS_FORTUNE_TOTALE = "600007";
    public final static String CS_FRANCHISE = "600015";
    public final static String CS_INTERET = "600012";
    public final static String CS_INTERET_MORATOIRE = "600030";
    public final static String CS_REV_20 = "600005";
    public final static String CS_REV_50000INF = "600009";
    public final static String CS_REV_BRUT = "600001";
    public final static String CS_REV_CI = "600021";
    public final static String CS_REV_SANS_COTISATION = "600026";
    public final static String CS_REV_NON_ARRONDI = "600027";
    // Constantes
    public final static String CS_REV_COUPLE = "600000";
    public final static String CS_REV_MOYEN = "600003";
    public final static String CS_REV_NET = "600019";
    public final static String CS_REV_NET_AVANT_PRORATA = "600018";
    public final static String CS_REV_NET_NONARRONDI = "600017";
    public final static String CS_TAUX_COTISATION = "600023";
    public final static String CS_TAUX_INTCAP = "600013";
    /** Fichier CPDOCAP */
    private String idDecision = "";
    /** (IAIDEC) */
    private String idDonneesCalcul = "";
    /** (IHIDCA) */
    private String montant = "";
    /** (IHMDCA) */
    private String periodicite = "";

    // code systeme
    /**
     * Commentaire relatif au constructeur CPDonneesCalcul
     */
    public CPDonneesCalcul() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CPDOCAP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idDecision = statement.dbReadNumeric("IAIDEC");
        idDonneesCalcul = statement.dbReadNumeric("IHIDCA");
        montant = statement.dbReadNumeric("IHMDCA", 2);
    }

    /**
     * Sauvagarde une donnée calculée qui sera repris plus tard dans des traitements Date de création : (22.03.2002
     * 10:04:09)
     * 
     * @param context
     *            BProcess, idDecision, idDonnee, valeurDonnee
     */
    public void _sauvegardeCalcul(BProcess process, String idDecision, String idDonnee, float valeur) throws Exception {
        try {
            CPDonneesCalcul donCalcul = new CPDonneesCalcul();
            donCalcul.setSession(process.getSession());
            donCalcul.setIdDecision(idDecision);
            donCalcul.setIdDonneesCalcul(idDonnee);
            donCalcul.retrieve();
            if (donCalcul.isNew()) {
                donCalcul.setMontant(Float.toString(valeur));
                donCalcul.add(process.getTransaction());
            } else {
                donCalcul.setMontant(Float.toString(valeur));
                donCalcul.update(process.getTransaction());
            }
        } catch (Exception e) {
            getSession().addError(
                    "Erreur lors de la sauvegarde des données du calcul - Décision: " + idDecision + " type: "
                            + idDonnee);
            getSession().addError(e.getMessage());
            return;
        }
    }

    /**
     * Sauvagarde une donnée calculée qui sera repris plus tard dans des traitements Date de création : (22.03.2002
     * 10:04:09)
     * 
     * @param context
     *            transaction, session, idDecision, idDonnee, valeurDonnee
     */
    public void _sauvegardeCalcul(BTransaction transaction, BSession session, String idDecision, String idDonnee,
            float valeur) throws Exception {
        try {
            CPDonneesCalcul donCalcul = new CPDonneesCalcul();
            donCalcul.setSession(session);
            donCalcul.setIdDecision(idDecision);
            donCalcul.setIdDonneesCalcul(idDonnee);
            donCalcul.retrieve();
            if (donCalcul.isNew()) {
                donCalcul.setMontant(Float.toString(valeur));
                donCalcul.add(transaction);
            } else {
                donCalcul.setMontant(Float.toString(valeur));
                donCalcul.update(transaction);
            }
        } catch (Exception e) {
            getSession().addError(
                    "Erreur lors de la sauvegarde des données du calcul - Décision: " + idDecision + " type: "
                            + idDonnee);
            getSession().addError(e.getMessage());
            return;
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) {
    }

    /**
	
	 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("IAIDEC", this._dbWriteNumeric(statement.getTransaction(), getIdDecision(), ""));
        statement.writeKey("IHIDCA", this._dbWriteNumeric(statement.getTransaction(), getIdDonneesCalcul(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("IAIDEC", this._dbWriteNumeric(statement.getTransaction(), getIdDecision(), "idDecision"));
        statement.writeField("IHIDCA",
                this._dbWriteNumeric(statement.getTransaction(), getIdDonneesCalcul(), "idDonnesCalcul"));
        statement.writeField("IHMDCA", this._dbWriteNumeric(statement.getTransaction(), this.getMontant(), "montant"));
    }

    /**
     * Calcul et sauvegarde des intérêts du capital
     * 
     * @param context
     *            BProcess
     * @param CPDecision
     *            decision
     * @param CPDonneesBase
     *            donneeBase
     * @return String interet
     */
    public float calculInteretCapital(BProcess process, CPDecision decision, CPDonneesBase donneeBase) {
        float capital = 0;
        float interet = 0;
        String tauxInteret = "";
        try {
            // Détermination des intérêts du capital investi
            // interet = capital arrondi au 1000fr. supérieur * taux
            if (!JadeStringUtil.isBlank(donneeBase.getCapital())) {
                capital = Float.parseFloat(JANumberFormatter.deQuote(donneeBase.getCapital()));
                // Arrondir le capital au 1000 fr. supérieur
                capital = JANumberFormatter.round(Float.parseFloat(Float.toString(capital)), 1000, 0,
                        JANumberFormatter.SUP);
                this._sauvegardeCalcul(process, decision.getIdDecision(), CPDonneesCalcul.CS_CAPITAL_ARRONDI, capital);
                // Recherche du taux dans la table des paramètres correspondant
                // au début de décision (praenumerando) ou à la date de fin
                // d'exercice (postnumerando)
                try {
                    if (decision.getTaxation().equalsIgnoreCase("A")) {
                        tauxInteret = FWFindParameter.findParameter(process.getTransaction(), "10500020", "TAUXINTERE",
                                decision.getDebutDecision(), "", 2);
                    } else {
                        tauxInteret = FWFindParameter.findParameter(process.getTransaction(), "10500020", "TAUXINTERE",
                                donneeBase.getFinExercice1(), "", 2);
                    }
                } catch (Exception e) {
                    getSession().addError(e.getMessage());
                    tauxInteret = "";
                }
                // Sauvegarde du taux d'intérêts
                this._sauvegardeCalcul(process, decision.getIdDecision(), CPDonneesCalcul.CS_TAUX_INTCAP,
                        Float.parseFloat(tauxInteret));
                // Calcul des intêrets
                try {
                    if (!JadeStringUtil.isBlank(tauxInteret)) {
                        interet = (capital * Float.parseFloat(tauxInteret)) / 100;
                    }
                    // Ramener au prorata de la période de l'exercice pour le
                    // mode de taxation postnumerando
                    // if (decision.isProrata().equals(new Boolean(true))){
                    interet = Float.parseFloat(CPToolBox.prorataInteret(donneeBase.getDebutExercice1(),
                            donneeBase.getFinExercice1(), Float.toString(interet)));
                    interet = JANumberFormatter.round(interet, 1, 0, JANumberFormatter.INF);
                    if (interet < 0) {
                        interet = 0;
                    }
                    // }
                } catch (Exception e) {
                    getSession().addError(e.getMessage());
                    interet = 0;
                }
            }
            // Sauvegarde des intérêts
            this._sauvegardeCalcul(process, decision.getIdDecision(), CPDonneesCalcul.CS_INTERET, interet);
            return interet;
        } catch (Exception e) {
            _addError(process.getTransaction(),
                    e.getMessage() + getSession().getLabel("CP_MSG_0024") + " " + decision.getIdDecision());
            return 0;
        }
    }

    /**
     * Calcul et sauvegarde des intérêts du capital
     * 
     * @param context
     *            BProcess
     * @param CPDecision
     *            decision
     * @param CPDonneesBase
     *            donneeBase
     * @return String interet
     * @throws Exception
     */
    public static float calculInteretCapital(BProcess process, CPDecisionViewBean decision) throws Exception {
        float capital = 0;
        float interet = 0;
        String tauxInteret = "";
        // Détermination des intérêts du capital investi
        // interet = capital arrondi au 1000fr. supérieur * taux
        if (!JadeStringUtil.isBlank(decision.getCapital())) {
            capital = Float.parseFloat(JANumberFormatter.deQuote(decision.getCapital()));
            // Arrondir le capital au 1000 fr. supérieur
            capital = JANumberFormatter
                    .round(Float.parseFloat(Float.toString(capital)), 1000, 0, JANumberFormatter.SUP);
            // Recherche du taux dans la table des paramètres correspondant
            // au début de décision (praenumerando) ou à la date de fin
            // d'exercice (postnumerando)
            if (decision.getTaxation().equalsIgnoreCase("A")) {
                tauxInteret = FWFindParameter.findParameter(process.getTransaction(), "10500020", "TAUXINTERE",
                        decision.getDebutDecision(), "", 2);
            } else {
                tauxInteret = FWFindParameter.findParameter(process.getTransaction(), "10500020", "TAUXINTERE",
                        decision.getFinExercice1(), "", 2);
            }
            // Calcul des intêrets
            if (!JadeStringUtil.isBlank(tauxInteret)) {
                interet = (capital * Float.parseFloat(tauxInteret)) / 100;
            }
            // Ramener au prorata de la période de l'exercice pour le
            // mode de taxation postnumerando
            // if (decision.isProrata().equals(new Boolean(true))){
            interet = Float.parseFloat(CPToolBox.prorataInteret(decision.getDebutExercice1(),
                    decision.getFinExercice1(), Float.toString(interet)));
            interet = JANumberFormatter.round(interet, 1, 0, JANumberFormatter.INF);
            if (interet < 0) {
                interet = 0;
            }
            // }
        }
        return interet;
    }

    /**
     * Retourne si une donnée existe dans les données du calcul
     * 
     * @return : boolean - true si trouvé
     */
    public boolean existeDonnee(String idDecision, String IdDonnee) {
        CPDonneesCalcul donnee = new CPDonneesCalcul();
        donnee.setSession(getSession());
        donnee.setIdDecision(idDecision);
        donnee.setIdDonneesCalcul(IdDonnee);
        try {
            donnee.retrieve();
            if (!donnee.isNew()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return false;
        }
    }

    public BManager find(Hashtable<?, ?> params) throws Exception {

        BManager manager = getManager();
        manager.setSession(getSession());
        if (params != null) {
            Enumeration<?> methods = params.keys();
            while (methods.hasMoreElements()) {
                String methodName = (String) methods.nextElement();
                Object value = params.get(methodName);

                Method m = manager.getClass().getMethod(methodName, new Class[] { value.getClass() });

                if (m != null) {
                    m.invoke(manager, new Object[] { value });
                }
            }
        }

        manager.find();
        return manager;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getIdDecision() {
        return idDecision;
    }

    public String getIdDonneesCalcul() {
        return idDonneesCalcul;
    }

    public String getLibellePeriodicite() {
        try {
            return globaz.phenix.translation.CodeSystem.getLibelle(getSession(), getPeriodicite());
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.02.2003 15:48:31)
     * 
     * @return globaz.globall.db.BManager
     */
    protected BManager getManager() {
        return new CPDonneesCalculManager();
    }

    public String getMontant() {
        if ((CPDonneesCalcul.CS_TAUX_COTISATION.equalsIgnoreCase(idDonneesCalcul))
                || (CPDonneesCalcul.CS_TAUX_INTCAP.equalsIgnoreCase(idDonneesCalcul))) {
            return JANumberFormatter.fmt(montant, true, true, true, 5);
        } else if (CPDonneesCalcul.CS_COTISATION_BRUT.equalsIgnoreCase(idDonneesCalcul)
                || CPDonneesCalcul.CS_INTERET_MORATOIRE.equalsIgnoreCase(idDonneesCalcul)) {
            return JANumberFormatter.fmt(montant, true, true, true, 2);
        } else {
            return JANumberFormatter.fmt(montant, true, false, true, 0);
        }
    }

    /**
     * Retourne le montant pour un calcul d'une décision
     * 
     * @return : String montant formaté
     */
    public String getMontant(String idDecision, String IdDonnee) {
        CPDonneesCalcul donnee = new CPDonneesCalcul();
        donnee.setSession(getSession());
        donnee.setIdDecision(idDecision);
        donnee.setIdDonneesCalcul(IdDonnee);
        try {
            donnee.retrieve();
            if (!donnee.isNew()) {
                return donnee.getMontant();
            } else {
                return "";
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return "";
        }
    }

    public String getPeriodicite() {
        return periodicite;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */
    public void setIdDecision(String newIdDecision) {
        idDecision = newIdDecision;
    }

    public void setIdDonneesCalcul(String newIdDonneesCalcul) {
        idDonneesCalcul = newIdDonneesCalcul;
    }

    public void setMontant(String newMontant) {
        montant = JANumberFormatter.deQuote(newMontant);
    }

    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }

    public String toMyString() {
        return "Donnees calcul : " + getIdDonneesCalcul() + "\nMontant: " + this.getMontant();
    }
}
