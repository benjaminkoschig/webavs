package globaz.osiris.db.interets;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendarMonth;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CASection;

/**
 * Insérez la description du type ici. Date de création : (30.12.2002 10:52:23)
 * 
 * @author: Administrator
 */
public class CADetailInteretMoratoire extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELD_ANNEECOTISATION = "ANNEECOTISATION";

    public static final String FIELD_DATEDEBUT = "DATEDEBUT";
    public static final String FIELD_DATEFIN = "DATEFIN";
    public static final String FIELD_IDDETINTMOR = "IDDETINTMOR";
    public static final String FIELD_IDINTERETMORATOIRE = "IDINTERETMORATOIRE";
    public static final String FIELD_IDJOUFAC = "IDJOUFAC";
    public static final String FIELD_MONTANTINTERET = "MONTANTINTERET";
    public static final String FIELD_MONTANTSOUMIS = "MONTANTSOUMIS";
    public static final String FIELD_TAUX = "TAUX";
    public static final String TABLE_CAIMDEP = "CAIMDEP";

    private String anneeCotisation = new String();
    /** (DATEDEBUT) */
    private String dateDebut = new String();
    /** (DATEFIN) */
    private String dateFin = new String();
    private String domaine = "CA";
    /** (IDDETINTMOR) */
    private String idDetailInteretMoratoire = new String();
    /** (IDINTERETMORATOIRE) */
    private String idInteretMoratoire = new String();
    // Cette variable sera uniquement renseigner lors d'une facturation, jamais
    // depuis la comptabilité auxiliaire.
    // Utile pour connaitre si un detail d'un intéret moratoire provient d'une
    // facturation COT Pers 25%
    private String idJournalFacturation = "0";

    private CAInteretMoratoire interetMoratoire;

    // cet attribut perment de générer des action (synchronisation des affacts
    // lors que les IM ont été modifiés)
    private boolean manualModification = false;

    /** (MONTANTINTERET) */
    private String montantInteret = new String();

    /** (MONTANTSOUMIS) */
    private String montantSoumis = new String();

    private CASection section;
    /** (TAUX) */
    private String taux = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CADetailInteretMoratoire
     */
    public CADetailInteretMoratoire() {
        super();
    }

    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        if (isManualModification()) {
            if (!JadeStringUtil.isIntegerEmpty(this.getInteretMoratoire(transaction).getIdJournalFacturation())) {
                this.getInteretMoratoire(transaction).updateAfact(transaction);
            }
        }

    }

    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        if (isManualModification()) {
            if (!JadeStringUtil.isIntegerEmpty(this.getInteretMoratoire(transaction).getIdJournalFacturation())) {
                this.getInteretMoratoire(transaction).updateAfact(transaction);
            }
        }

    }

    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        if (isManualModification()) {
            if (!JadeStringUtil.isIntegerEmpty(this.getInteretMoratoire(transaction).getIdJournalFacturation())) {
                this.getInteretMoratoire(transaction).updateAfact(transaction);
            }
        }
    }

    /**
     * Effectue des traitements avant un ajout dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements avant l'ajout de l'entité dans la BD
     * <p>
     * L'exécution de l'ajout n'est pas effectuée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_beforeAdd()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // incrémente le prochain numéro
        setIdDetailInteretMoratoire(this._incCounter(transaction, idDetailInteretMoratoire));
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return CADetailInteretMoratoire.TABLE_CAIMDEP;
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idDetailInteretMoratoire = statement.dbReadNumeric(CADetailInteretMoratoire.FIELD_IDDETINTMOR);
        idInteretMoratoire = statement.dbReadNumeric(CADetailInteretMoratoire.FIELD_IDINTERETMORATOIRE);
        dateDebut = statement.dbReadDateAMJ(CADetailInteretMoratoire.FIELD_DATEDEBUT);
        dateFin = statement.dbReadDateAMJ(CADetailInteretMoratoire.FIELD_DATEFIN);
        montantSoumis = statement.dbReadNumeric(CADetailInteretMoratoire.FIELD_MONTANTSOUMIS, 2);
        taux = statement.dbReadNumeric(CADetailInteretMoratoire.FIELD_TAUX, 5);
        montantInteret = statement.dbReadNumeric(CADetailInteretMoratoire.FIELD_MONTANTINTERET, 2);
        idJournalFacturation = statement.dbReadNumeric(CADetailInteretMoratoire.FIELD_IDJOUFAC);
        anneeCotisation = statement.dbReadNumeric(CADetailInteretMoratoire.FIELD_ANNEECOTISATION);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) {

        boolean checkDateDebut = false;
        boolean checkDateFin = false;

        // vérification des id
        _propertyMandatory(statement.getTransaction(), getIdInteretMoratoire(), getSession().getLabel("7350"));
        _propertyMandatory(statement.getTransaction(), getIdDetailInteretMoratoire(), getSession().getLabel("7351"));

        // Vérification de la date de début
        if (_propertyMandatory(statement.getTransaction(), getDateDebut(), getSession().getLabel("7352"))) {
            if (_checkDate(statement.getTransaction(), getDateDebut(), getSession().getLabel("7085"))) {
                checkDateDebut = true;
            }
        }

        // Vérification de la date de fin
        if (_propertyMandatory(statement.getTransaction(), getDateFin(), getSession().getLabel("7353"))) {
            if (_checkDate(statement.getTransaction(), getDateFin(), getSession().getLabel("7086"))) {
                checkDateFin = true;
            }
        }

        // si les deux date sont renseignées et sont dans un format correct on
        // vérifie que la date de début soit avant celle de fin
        if (checkDateDebut && checkDateFin) {

            try {
                if (BSessionUtil.compareDateFirstGreater(getSession(), getDateDebut(), getDateFin())) {
                    _addError(statement.getTransaction(), getSession().getLabel("7370"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // vérification du montant
        if (JadeStringUtil.isBlank(getMontantSoumis())) {
            _addError(statement.getTransaction(), getSession().getLabel("5129"));
        } else if (getMontantSoumis().equals("0")) {
            _addError(statement.getTransaction(), getSession().getLabel("7385"));
        }

        // vérification du taux
        if (JadeStringUtil.isBlank(getTaux())) {
            _addError(statement.getTransaction(), getSession().getLabel("7383"));
        } else {
            float taux = new Float(getTaux()).floatValue();
            if (taux <= 0) {
                _addError(statement.getTransaction(), getSession().getLabel("7384"));
            }
        }

        // si l'intérêt est vide on le calcule
        if (JadeStringUtil.isBlank(getMontantInteret())) {
            try {
                setMontantInteret(getInteretCalcule());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CADetailInteretMoratoire.FIELD_IDDETINTMOR,
                this._dbWriteNumeric(statement.getTransaction(), getIdDetailInteretMoratoire(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CADetailInteretMoratoire.FIELD_IDDETINTMOR, this._dbWriteNumeric(
                statement.getTransaction(), getIdDetailInteretMoratoire(), "idDetailInteretMoratoire"));
        statement.writeField(CADetailInteretMoratoire.FIELD_IDINTERETMORATOIRE,
                this._dbWriteNumeric(statement.getTransaction(), getIdInteretMoratoire(), "idInteretMoratoire"));
        statement.writeField(CADetailInteretMoratoire.FIELD_DATEDEBUT,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateDebut(), "dateDebut"));
        statement.writeField(CADetailInteretMoratoire.FIELD_DATEFIN,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateFin(), "dateFin"));
        statement.writeField(CADetailInteretMoratoire.FIELD_MONTANTSOUMIS,
                this._dbWriteNumeric(statement.getTransaction(), getMontantSoumis(), "montantSoumis"));
        statement.writeField(CADetailInteretMoratoire.FIELD_TAUX,
                this._dbWriteNumeric(statement.getTransaction(), getTaux(), "taux"));
        statement.writeField(CADetailInteretMoratoire.FIELD_MONTANTINTERET,
                this._dbWriteNumeric(statement.getTransaction(), getMontantInteret(), "montantInteret"));
        statement.writeField(CADetailInteretMoratoire.FIELD_IDJOUFAC,
                this._dbWriteNumeric(statement.getTransaction(), getIdJournalFacturation(), "idJouFac"));
        statement.writeField(CADetailInteretMoratoire.FIELD_ANNEECOTISATION,
                this._dbWriteNumeric(statement.getTransaction(), getAnneeCotisation(), "anneeCotisation"));
    }

    public String getAnneeCotisation() {
        return anneeCotisation;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    /**
     * @return
     */
    public String getDomaine() {
        return domaine;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getIdDetailInteretMoratoire() {
        return idDetailInteretMoratoire;
    }

    public String getIdInteretMoratoire() {
        return idInteretMoratoire;
    }

    public String getIdJournalFacturation() {
        return idJournalFacturation;
    }

    public String getInteretCalcule() throws Exception {

        FWCurrency soumis = new FWCurrency(getMontantSoumis());
        FWCurrency interet;
        double taux = Double.parseDouble(getTaux());
        int jours = getNbJours();

        double dInt = soumis.doubleValue() * taux * jours / 36000;
        interet = new FWCurrency(dInt);
        interet.round(FWCurrency.ROUND_5CT);

        return interet.toString();
    }

    public CAInteretMoratoire getInteretMoratoire() {
        try {
            return this.getInteretMoratoire(null);
        } catch (Exception e) {
            // TODO Bloc catch auto-généré
            e.printStackTrace();
        }

        return null;
    }

    public CAInteretMoratoire getInteretMoratoire(BTransaction transaction) {

        if ((interetMoratoire == null) && !JadeStringUtil.isBlank(idInteretMoratoire)) {
            try {
                interetMoratoire = new CAInteretMoratoire();
                interetMoratoire.setSession(getSession());
                interetMoratoire.setIdInteretMoratoire(idInteretMoratoire);
                interetMoratoire.retrieve(transaction);
                interetMoratoire.setDomaine("CA");
                if (interetMoratoire.isNew()) {
                    interetMoratoire = null;
                }
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.addErrors(e.getMessage());
                }
                e.printStackTrace();
                interetMoratoire = null;
            }
        }

        return interetMoratoire;

    }

    public String getMontantInteret() {
        return montantInteret;
    }

    public String getMontantSoumis() {
        return montantSoumis;
    }

    public int getNbJours() throws Exception {
        JACalendarMonth calendar = new JACalendarMonth(30);

        JADate dateFin = new JADate(getDateFin());

        dateFin = getSession().getApplication().getCalendar().addDays(dateFin, 1);

        return (int) calendar.daysBetween(getDateDebut(), dateFin.toString());
    }

    public CASection getSection() {
        if (section == null) {
            section = new CASection();
            section.setISession(getSession());
            section.setIdSection(this.getInteretMoratoire().getIdSection());
            try {
                section.retrieve();
                if (section.isNew()) {
                    section = null;
                }
            } catch (Exception e) {
                section = null;
            }
        }

        return section;
    }

    public String getTaux() {
        return taux;
    }

    public boolean isDomaineCA() {
        return domaine.equals("CA");
    }

    /**
     * @return
     */
    public boolean isManualModification() {
        return manualModification;
    }

    public void setAnneeCotisation(String anneeCotisation) {
        this.anneeCotisation = anneeCotisation;
    }

    public void setDateDebut(String newDateDebut) {
        dateDebut = newDateDebut;
    }

    public void setDateFin(String newDateFin) {
        dateFin = newDateFin;
    }

    /**
     * @param string
     */
    public void setDomaine(String string) {
        domaine = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */
    public void setIdDetailInteretMoratoire(String newIdDetailInteretMoratoire) {
        idDetailInteretMoratoire = newIdDetailInteretMoratoire;
    }

    public void setIdInteretMoratoire(String newIdInteretMoratoire) {
        idInteretMoratoire = newIdInteretMoratoire;
    }

    public void setIdJournalFacturation(String idJournalFacturation) {
        this.idJournalFacturation = idJournalFacturation;
    }

    /**
     * @param b
     */
    public void setManualModification(boolean b) {
        manualModification = b;
    }

    public void setMontantInteret(String newMontantInteret) {
        montantInteret = newMontantInteret;
    }

    public void setMontantSoumis(String newMontantSoumis) {
        montantSoumis = newMontantSoumis;
    }

    public void setTaux(String newTaux) {
        taux = newTaux;
    }

}
