package globaz.helios.db.comptes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.helios.application.CGApplication;
import globaz.helios.db.bouclement.CGBouclement;
import globaz.helios.db.bouclement.CGBouclementManager;
import globaz.helios.db.interfaces.ITreeListable;
import globaz.helios.parser.CGPieceIncrementor;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class CGExerciceComptable extends BEntity implements ITreeListable, FWViewBeanInterface, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // Exerice libelle
    public final static String CS_EXERCICE = "732001";

    private final static String labelPrefix = "EXER_COMPTABLE_";

    private String dateDebut = new String();
    private String dateFin = new String();
    private Boolean estCloture = new Boolean(false);
    private boolean estNouveauMandat = false;
    private String idExerciceComptable = new String();
    private String idExerForPlan = "0";
    private String idMandat = new String();
    private CGMandat mandat = null;

    /**
     * Commentaire relatif au constructeur CGExerciceComptable
     */
    public CGExerciceComptable() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _afterAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        ouvrir(transaction);
        CGPieceIncrementor
                .createIncrementorForNewExerciceComptable(getSession(), transaction, getIdExerciceComptable());
    }

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        mandat = new CGMandat();
        mandat.setIdMandat(idMandat);
        mandat.setSession(transaction.getSession());
        mandat.retrieve(transaction);
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // BZ7760 : On ne peut pas créer deux exercices comptables pour la même période et le même mandat
        CGExerciceComptableManager exerciceComptableManager = new CGExerciceComptableManager();
        exerciceComptableManager.setSession(getSession());
        exerciceComptableManager.setForIdMandat(idMandat);
        exerciceComptableManager.setFromDateDebut(dateDebut);
        exerciceComptableManager.setUntilDateFin(dateFin);
        exerciceComptableManager.find();

        if (exerciceComptableManager.size() > 0) {
            _addError(transaction, label("DEJA_EXISTANT"));
            return;
        }
        // incrémente de +1 le numéro
        setIdExerciceComptable(this._incCounter(transaction, "0"));
    }

    /**
	 *
	 */
    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) {

        // on peut supprimer si :
        // aucun plan comptabe
        // aucune période
        // role de chef comptable

        try {
            if (!(CGApplication.isUserChefComptable(getSession()))) {
                _addError(transaction, label("ERROR_SUPPR_EXER_NON_AUTHORISE"));
                return;
            }

            // Des périodes associées ?
            CGPeriodeComptableManager periodeMgr = new CGPeriodeComptableManager();
            periodeMgr.setSession(getSession());
            periodeMgr.setForIdExerciceComptable(getIdExerciceComptable());
            periodeMgr.find(transaction);
            if (periodeMgr.size() > 0) {
                _addError(transaction, label("ERROR_SUPPR_EXER_PERIODE_PRESENT"));
                return;
            }

            // Un plan comptable ?
            CGPlanComptableManager planMgr = new CGPlanComptableManager();
            planMgr.setSession(getSession());
            planMgr.setForIdExerciceComptable(getIdExerciceComptable());
            planMgr.find(transaction);
            if (planMgr.size() > 0) {
                _addError(transaction, label("ERROR_SUPPR_EXER_PLAN_PRESENT"));
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            _addError(transaction, e.getMessage());
        }

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CGEXERP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idExerciceComptable = statement.dbReadNumeric("IDEXERCOMPTABLE");
        idMandat = statement.dbReadNumeric("IDMANDAT");
        dateDebut = statement.dbReadDateAMJ("DATEDEBUT");
        dateFin = statement.dbReadDateAMJ("DATEFIN");
        estCloture = statement.dbReadBoolean("ESTCLOTURE");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {

        if (JAUtil.isDateEmpty(dateDebut)) {
            _addError(statement.getTransaction(), label("DATE_DEBUT_NON_RENSEIGNEE"));
        } else {
            try {
                BSessionUtil.checkDateGregorian(getSession(), dateDebut);
            } catch (Exception e) {
                _addError(statement.getTransaction(), label("DATE_DEBUT_INVALID"));
                return;
            }
        }

        if (JAUtil.isDateEmpty(dateFin)) {
            _addError(statement.getTransaction(), label("DATE_FIN_NON_RENSEIGNEE"));
        } else {
            try {
                BSessionUtil.checkDateGregorian(getSession(), dateFin);
            } catch (Exception e) {
                _addError(statement.getTransaction(), label("DATE_FIN_INVALID"));
                return;
            }
        }

        if (!hasErrors()) {
            try {
                if (!BSessionUtil.compareDateFirstLower(getSession(), dateDebut, dateFin)) {
                    _addError(statement.getTransaction(), label("DATE_FIN_SUP_DATE_DEB"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                _addError(statement.getTransaction(), e.getMessage());
            }
        }

        JADate jDateDebut = new JADate();
        jDateDebut.fromString(getDateDebut());
        JADate jDateFin = new JADate();
        jDateFin.fromString(getDateFin());

        if (mandat == null) {
            mandat = new CGMandat();
            mandat.setSession(getSession());
            mandat.setIdMandat(getIdMandat());
            mandat.retrieve(statement.getTransaction());
        }

        if (!isEstNouveauMandat()) {
            if ((mandat == null) || mandat.isNew()) {
                _addError(statement.getTransaction(), getSession().getLabel("LABEL_GLOBAL_MANDAT_INEXISTANT"));
                return;
            }
        }

        // Date de début == 1er jour du mois
        if (jDateDebut.getDay() != 1) {
            _addError(statement.getTransaction(), label("DATE_INVALID"));
            return;
        }

        // Date de fin == dernier jour du mois
        JACalendarGregorian calGregorian = new JACalendarGregorian();
        if (!calGregorian.isLastInMonth(dateFin)) {
            _addError(statement.getTransaction(), label("DATE_INVALID"));
            return;
        }

        if (mandat.isEstComptabiliteAVS().booleanValue()) {
            if (jDateDebut.getMonth() != 1) {
                _addError(statement.getTransaction(), label("DATE_INVALID"));
                return;
            }
            if (jDateFin.getMonth() != 12) {
                _addError(statement.getTransaction(), label("DATE_INVALID"));
                return;
            }
            if (jDateFin.getYear() != jDateDebut.getYear()) {
                _addError(statement.getTransaction(), label("DATE_INVALID"));
                return;
            }
        }
        // Mandat non AVS
        // l'exercice peut commencer en milieur d'année
        // Exemple : 01.05.2004 --> 30.04.2005
        else {
            int monthDebut = jDateDebut.getMonth();
            int monthFin = jDateFin.getMonth();

            int diff = Math.abs(monthDebut - monthFin);

            // 01.01.05 --> 31.12.05
            if (diff > 1) {
                if ((monthDebut != 1) && (monthFin != 12)) {
                    _addError(statement.getTransaction(), label("DATE_ERROR"));
                    return;
                }
                if (jDateFin.getYear() != jDateDebut.getYear()) {
                    _addError(statement.getTransaction(), label("DATE_ERROR"));
                    return;
                }
            }
            // 01.01.05 --> 31.01.05
            else if (diff == 0) {
                _addError(statement.getTransaction(), label("DATE_ERROR"));
                return;
            }
            // diff == 1
            // On contrôle que l'année de début et de fin sont consécutive
            // 01.05.05 --> 30.04.06
            else {
                int yearDeb = jDateDebut.getYear();
                int yearFin = jDateFin.getYear();
                diff = Math.abs(yearDeb - yearFin);
                // 01.05.05 --> 30.04.07
                if (diff != 1) {
                    _addError(statement.getTransaction(), label("DATE_ERROR"));
                    return;
                }
            }
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDEXERCOMPTABLE",
                this._dbWriteNumeric(statement.getTransaction(), getIdExerciceComptable(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDEXERCOMPTABLE",
                this._dbWriteNumeric(statement.getTransaction(), getIdExerciceComptable(), "idExerciceComptable"));
        statement.writeField("IDMANDAT", this._dbWriteNumeric(statement.getTransaction(), getIdMandat(), "idMandat"));
        statement
                .writeField("DATEDEBUT", this._dbWriteDateAMJ(statement.getTransaction(), getDateDebut(), "dateDebut"));
        statement.writeField("DATEFIN", this._dbWriteDateAMJ(statement.getTransaction(), getDateFin(), "dateFin"));
        statement.writeField("ESTCLOTURE", this._dbWriteBoolean(statement.getTransaction(), isEstCloture(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "estcloture"));
    }

    @Override
    public BManager[] getChilds() throws Exception {
        BManager[] listables = new BManager[2];

        CGPeriodeComptableManager periodeManager = new CGPeriodeComptableManager();
        periodeManager.setForIdExerciceComptable(getIdExerciceComptable());
        listables[0] = periodeManager;

        CGPlanComptableManager compteManager = new CGPlanComptableManager();
        compteManager.setForIdExerciceComptable(getIdExerciceComptable());
        listables[1] = compteManager;

        return listables;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    /**
     * Récupère la description de l'exercice dans la langue de l'utilisateur. La mention "Exercice" + dateDebut + un
     * tiret + dateFin + un asterix si l'Exercice comptable est clôturé. Exemple : Exercice 01.02.2002 - 01.02.2003 *.
     * si l'exercice s'étends sur l'année complète,(01.01-31.12) on simplifie la description en mentinnant uniquement
     * l'année. Exemple : Exercice 2002 *
     * 
     * Date de création : (11.11.2002 17:16:49)
     * 
     * @return String
     */
    public String getFullDescription() {

        try {
            String exericeDescription = CodeSystem.getLibelle(getSession(), CGExerciceComptable.CS_EXERCICE);

            if (JadeStringUtil.isBlank(exericeDescription)) {
                // en cas de probleme, on ecrit en francais
                exericeDescription = "Exercice";
            }

            exericeDescription += " ";
            int pDebut = getDateDebut().lastIndexOf(".");
            int pFin = getDateFin().lastIndexOf(".");

            String anneeDebut = getDateDebut().substring(pDebut + 1);
            String jjmmDebut = getDateDebut().substring(0, pDebut);
            String anneeFin = getDateFin().substring(pFin + 1);
            String jjmmFin = getDateFin().substring(0, pFin);

            if ((anneeDebut.equals(anneeFin)) && ("01.01".equals(jjmmDebut)) && ("31.12".equals(jjmmFin))) {
                exericeDescription += anneeDebut;
            } else {
                exericeDescription += getDateDebut() + " - " + getDateFin();
            }

            return exericeDescription + " " + ((estCloture.booleanValue()) ? "*" : "");

        } catch (Exception e) {
            return getIdExerciceComptable() + " ?";
        }

    }

    /**
     * Getter
     */
    public String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2003 11:46:11)
     * 
     * @return String
     */
    public String getIdExerForPlan() {
        return idExerForPlan;
    }

    public String getIdMandat() {
        return idMandat;
    }

    @Override
    public final String getLibelle() {
        return getFullDescription();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.10.2002 10:34:14)
     * 
     * @param value
     *            globaz.helios.db.comptes.CGMandat
     */
    public CGMandat getMandat() {
        return mandat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.11.2002 17:16:49)
     * 
     * @return String
     */
    public String getPeriodeDeA() {
        return "(" + getIdExerciceComptable() + ") " + getDateDebut() + " - " + getDateFin();
    }

    public Boolean isEstCentreCharge() {
        // A Faire;
        return new Boolean(true);
    }

    public Boolean isEstCloture() {
        return estCloture;
    }

    public Boolean isEstMonnaieEtrangere() {
        // A Faire
        return new Boolean(true);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.09.2002 17:17:32)
     * 
     * @return Boolean
     */
    public boolean isEstNouveauMandat() {
        return estNouveauMandat;
    }

    private String label(String code) {
        return getSession().getLabel(CGExerciceComptable.labelPrefix + code);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.09.2002 13:53:36)
     */
    public void ouvrir(BTransaction transaction) throws Exception {

        // creation d'une periode comptable
        if (mandat == null) {

            /*
             * si on cree un exercice comptable depuis sans passer par le processus de creation de mandat, il faut cree
             * un mandat.
             */
            if (JadeStringUtil.isBlank(idMandat)) {
                _addError(transaction, label("MANDAT_NON_RENSEIGNE"));
            } else {
                mandat = new CGMandat();
                mandat.setIdMandat(idMandat);
                mandat.setSession(getSession());
                mandat.retrieve(transaction);
            }
        }

        if (!mandat.isEstComptabiliteAVS().booleanValue() && !mandat.isTypePlanComptableOfas()) {
            CGPeriodeComptable periodeComptable = new CGPeriodeComptable();
            periodeComptable.setIdTypePeriode(CGPeriodeComptable.CS_ANNUEL);

            CGBouclementManager bouclementMgr = new CGBouclementManager();
            bouclementMgr.setSession(getSession());
            bouclementMgr.setForIdMandat(getIdMandat());
            bouclementMgr.setForIdTypeBouclement(CGBouclement.CS_BOUCLEMENT_ANNUEL);
            bouclementMgr.find(transaction, 2);
            if (bouclementMgr.size() == 0) {
                throw new Exception(this.getClass().getName() + " : Bouclement ANNUEL inexistant pour le mandat : "
                        + getIdMandat());
            }
            CGBouclement bouclement = (CGBouclement) bouclementMgr.getEntity(0);

            periodeComptable.setIdBouclement(bouclement.getIdBouclement());
            periodeComptable.setIdExerciceComptable(getIdExerciceComptable());
            periodeComptable.setEstCloture(new Boolean(false));
            periodeComptable.setCode("  ");
            periodeComptable.setDateDebut(dateDebut);
            periodeComptable.setDateFin(dateFin);
            periodeComptable.add(transaction);
        }

        if (!isEstNouveauMandat()) {
            String idExer = null;
            if ((getIdExerForPlan() != null) && !getIdExerForPlan().equals("") && !getIdExerForPlan().equals("0")) {
                idExer = getIdExerForPlan();
            } else {
                CGExerciceComptable exercicePrec = retrieveLastExerciceContigu(transaction);
                if ((exercicePrec != null) && !exercicePrec.isNew()) {
                    idExer = exercicePrec.getIdExerciceComptable();
                }
            }
            if ((idExer != null) && !idExer.equals("") && !idExer.equals("0")) {
                CGExerciceComptable exercice = new CGExerciceComptable();
                exercice.setSession(getSession());
                exercice.setIdExerciceComptable(idExer);
                exercice.retrieve(transaction);
                if (exercice.isNew()) {
                    throw (new Exception(label("EXERCICE_INEXISTANT")));
                }

                CGPlanComptableManager planManager = new CGPlanComptableManager();
                planManager.setSession(getSession());
                planManager.setForIdMandat(getIdMandat());
                planManager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
                planManager.setForEstAReouvrir(new Boolean(true));

                BStatement statement = null;

                statement = planManager.cursorOpen(transaction);
                BEntity entity;
                while ((entity = planManager.cursorReadNext(statement)) != null) {
                    CGPlanComptableViewBean plan = (CGPlanComptableViewBean) entity;

                    CGPlanComptableViewBean newPlan = new CGPlanComptableViewBean();
                    newPlan.setSession(getSession());
                    newPlan.wantAutoInherit(false);
                    newPlan.setIdExerciceComptable(getIdExerciceComptable());
                    newPlan.setIdCompte(plan.getIdCompte());
                    newPlan.setAReouvrir(new Boolean(true));
                    newPlan.setLibelleDe(plan.getLibelleDe());
                    newPlan.setLibelleIt(plan.getLibelleIt());
                    newPlan.setLibelleFr(plan.getLibelleFr());
                    newPlan.setIdExterne(plan.getIdExterne());
                    newPlan.setEstCompteAvs(mandat.isEstComptabiliteAVS().booleanValue());

                    // Si pas un mandat avs, il faut mettre à jour le genre et
                    // le domaine, car on ne peut pas le déterminer
                    // automatiquement
                    // a partir du no de compte
                    if (!mandat.isEstComptabiliteAVS().booleanValue()) {
                        newPlan.setIdGenre(plan.getIdGenre());
                        newPlan.setIdDomaine(plan.getIdDomaine());
                    }

                    newPlan.add(transaction);
                }
                planManager.cursorClose(statement);
            } else {
                throw (new Exception(label("EXERCICE_INEXISTANT")));
            }
        }
    }

    /**
     * Method retrieveLastExercice.
     * 
     * Récupère le dernier exercice comptable avant celui-ci
     * 
     * @param transaction
     * @return CGExerciceComptable
     */

    public CGExerciceComptable retrieveLastExercice(globaz.globall.db.BTransaction transaction) {
        try {
            CGExerciceComptableManager exManager = new CGExerciceComptableManager();
            exManager.setSession(getSession());
            exManager.setForIdMandat(getIdMandat());
            exManager.setUntilDateFin(getDateDebut());
            exManager.setOrderBy(CGExerciceComptableManager.TRI_DATE_FIN_DESC);
            exManager.find(transaction);
            if (exManager.size() > 0) {
                return (CGExerciceComptable) exManager.getEntity(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method retrieveLastExerciceContigu.
     * 
     * Récupère le dernier exercice comptable contigu à celui ci.
     * 
     * @param transaction
     * @return CGExerciceComptable le dernier exercice comptable contigu à celui ci, null si inexistant
     */

    public CGExerciceComptable retrieveLastExerciceContigu(globaz.globall.db.BTransaction transaction) {

        try {
            CGExerciceComptable exercice = retrieveLastExercice(transaction);
            if ((exercice == null) || exercice.isNew()) {
                return null;
            }
            Calendar cal = new java.util.GregorianCalendar();
            JADate dateFin = new JADate(exercice.getDateFin());
            JADate dateDebut = new JADate(getDateDebut());
            cal.set(Calendar.YEAR, dateFin.getYear());
            cal.set(Calendar.MONTH, dateFin.getMonth() - 1);
            cal.set(Calendar.DAY_OF_MONTH, dateFin.getDay());
            cal.add(Calendar.DAY_OF_MONTH, 1);
            Date temp = cal.getTime();
            cal.set(Calendar.YEAR, dateDebut.getYear());
            cal.set(Calendar.MONTH, dateDebut.getMonth() - 1);
            cal.set(Calendar.DAY_OF_MONTH, dateDebut.getDay());
            if (!temp.equals(cal.getTime())) {
                return null;
            } else {
                return exercice;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 13:56:32)
     * 
     * @return globaz.helios.db.comptes.CGExerciceComptable
     */
    public CGExerciceComptable retrieveNextExercice(globaz.globall.db.BTransaction transaction) {
        try {
            CGExerciceComptableManager exManager = new CGExerciceComptableManager();
            exManager.setSession(getSession());
            exManager.setForIdMandat(getIdMandat());
            exManager.setFromDateDebut(getDateFin());
            exManager.setOrderBy(CGExerciceComptableManager.TRI_DATE_DEBUT);
            exManager.find(transaction);
            if (exManager.size() > 0) {
                return (CGExerciceComptable) exManager.getEntity(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 13:56:32)
     * 
     * @return globaz.helios.db.comptes.CGExerciceComptable
     */
    public CGExerciceComptable retrieveNextExerciceContigu(globaz.globall.db.BTransaction transaction) {

        try {
            CGExerciceComptable exercice = retrieveNextExercice(transaction);
            if ((exercice == null) || exercice.isNew()) {
                return null;
            }
            Calendar cal = new java.util.GregorianCalendar();
            JADate dateFin = new JADate(getDateFin());
            JADate dateDebut = new JADate(exercice.getDateDebut());
            cal.set(Calendar.YEAR, dateFin.getYear());
            cal.set(Calendar.MONTH, dateFin.getMonth() - 1);
            cal.set(Calendar.DAY_OF_MONTH, dateFin.getDay());
            cal.add(Calendar.DAY_OF_MONTH, 1);
            Date temp = cal.getTime();
            cal.set(Calendar.YEAR, dateDebut.getYear());
            cal.set(Calendar.MONTH, dateDebut.getMonth() - 1);
            cal.set(Calendar.DAY_OF_MONTH, dateDebut.getDay());
            if (!temp.equals(cal.getTime())) {
                return null;
            } else {
                return exercice;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setDateDebut(String newDateDebut) {
        dateDebut = newDateDebut;
    }

    public void setDateFin(String newDateFin) {
        dateFin = newDateFin;
    }

    public void setEstCloture(Boolean newEstCloture) {
        estCloture = newEstCloture;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.09.2002 17:17:32)
     * 
     * @param newEstNouveauMandat
     *            Boolean
     */
    public void setEstNouveauMandat(boolean newEstNouveauMandat) {
        estNouveauMandat = newEstNouveauMandat;
    }

    /**
     * Setter
     */
    public void setIdExerciceComptable(String newIdExerciceComptable) {
        idExerciceComptable = newIdExerciceComptable;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.05.2003 14:42:56)
     */
    public void setIdExerciceComptableFrom() {
    }

    /**
     * Method setIdExerciceComptableFrom. Appelée par des applications externe. Permet de setter l'Id de l'exercice
     * comptable à partir d'une date donnée, et du mandat.
     * 
     * @param date
     * @param idMandat
     * @throws Exception
     */
    public void setIdExerciceComptableFrom(String date, String idMandat) throws Exception {
        this.setIdExerciceComptableFrom(date, idMandat, null);
    }

    /**
     * Method setIdExerciceComptableFrom. Permet de setter l'Id de l'exercice comptable à partir d'une date donnée, et
     * du mandat.
     * 
     * @param date
     * @param idMandat
     * @param transaction
     *            peut être null.
     * @throws Exception
     */
    public void setIdExerciceComptableFrom(String date, String idMandat, globaz.globall.db.BTransaction transaction)
            throws Exception {
        CGExerciceComptableManager manager = new CGExerciceComptableManager();
        manager.setSession(getSession());
        manager.setForIdMandat(idMandat);
        manager.setBetweenDateDebutDateFin(date);
        manager.find(transaction);

        if (manager.size() == 0) {
            throw (new Exception(label("AUCUN_EXERCICE_COMPTABLE")));
        } else if (manager.size() > 1) {
            throw (new Exception(label("PLUSIEUR_EXERCICE_COMPTABLE")));
        } else {
            setIdExerciceComptable(((CGExerciceComptable) manager.getEntity(0)).getIdExerciceComptable());
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2003 11:46:11)
     * 
     * @param newIdExerForPlan
     *            String
     */
    public void setIdExerForPlan(String newIdExerForPlan) {
        idExerForPlan = newIdExerForPlan;
    }

    public void setIdMandat(String newIdMandat) {
        idMandat = newIdMandat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.10.2002 10:34:14)
     * 
     * @param value
     *            globaz.helios.db.comptes.CGMandat
     */
    public void setMandat(CGMandat value) {
        mandat = value;
    }

}
