package globaz.pavo.db.compte;

import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.application.CIApplication;
import globaz.pavo.translation.CodeSystem;

public class CIAnnonceCentrale extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // code système du processus
    public static final String CS_ANNONCE_INTERCALAIRE = "323000";
    public static final String CS_DERNIER_ANNONCE_ANNEE = "323001";
    public static final String CS_ETAT_ENVOYE = "324001";
    public static final String CS_ETAT_ERREUR = "324002";
    public static final String CS_ETAT_GENERE = "324000";
    // code systeme
    public static final String PREFIX_FICHIER_SORTIE = "CIANNCEN";
    public static final String TYPE_ENR_ANNONCE_INTERCALAIRE = "1";
    public static final String TYPE_ENR_DERNIERE_ANNONCE_ANNEE = "2";
    /** Fichier CIANCEP */
    /** (KRIANC) */
    private String annonceCentraleId = new String();
    /**
     * non-persistent field
     */
    private boolean annonceHorsPeriodeQuittancee = true;
    /** (KRDCRE) */
    private String dateCreation = new String();
    /** (KRDENV) */
    private String dateEnvoi = new String();
    /** (KRTETA) */
    private String idEtat = new String();

    /** (KRTANN) */
    private String idTypeAnnonce = new String();
    /** (KRMINS) */
    private String montantTotal = new String();

    /** (KRNINS) */
    private String nbrInscriptions = new String();

    /**
     * Commentaire relatif au constructeur CIAnnoncesCentrale
     */
    public CIAnnonceCentrale() {
        super();
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setAnnonceCentraleId(this._incCounter(transaction, "0"));

        if (isDejaAnnonceExistante()) {
            _addError(transaction, getSession().getLabel("CI_ANNONCE_CENTRALE_ERREUR_ANNONCE_DEJA_EXISTANTE"));
        }

        if (CIAnnonceCentrale.CS_DERNIER_ANNONCE_ANNEE.equalsIgnoreCase(getIdTypeAnnonce())
                && isDerniereAnnonceAnneeDejaExistante()) {
            _addError(transaction,
                    getSession().getLabel("CI_ANNONCE_CENTRALE_ERREUR_DERNIERE_ANNONCE_ANNEE_DEJA_EXISTANTE"));
        }

    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {

        if (isDejaAnnonceExistante()) {
            _addError(transaction, getSession().getLabel("CI_ANNONCE_CENTRALE_ERREUR_ANNONCE_DEJA_EXISTANTE"));
        }

        if (CIAnnonceCentrale.CS_DERNIER_ANNONCE_ANNEE.equalsIgnoreCase(getIdTypeAnnonce())
                && isDerniereAnnonceAnneeDejaExistante()) {
            _addError(transaction,
                    getSession().getLabel("CI_ANNONCE_CENTRALE_ERREUR_DERNIERE_ANNONCE_ANNEE_DEJA_EXISTANTE"));
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CIANCEP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        annonceCentraleId = statement.dbReadNumeric("KRIANC");
        nbrInscriptions = statement.dbReadNumeric("KRNINS");
        montantTotal = statement.dbReadNumeric("KRMINS");
        dateCreation = statement.dbReadDateAMJ("KRDCRE");
        dateEnvoi = statement.dbReadDateAMJ("KRDENV");
        idTypeAnnonce = statement.dbReadNumeric("KRTANN");
        idEtat = statement.dbReadNumeric("KRTETA");

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) {

        boolean isDateCreationValide = true;
        boolean isDateEnvoiValide = true;
        boolean isMoisDebutPeriodeEnvoiValide = true;
        boolean isMoisFinPeriodeEnvoiValide = true;

        int moisDebutPeriodeEnvoi = 0;
        String joursMoisDebutPeriodeEnvoi = "";
        try {
            joursMoisDebutPeriodeEnvoi = GlobazServer.getCurrentSystem()
                    .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO).getProperty("annonce.periode.debut");
            moisDebutPeriodeEnvoi = Integer.valueOf(joursMoisDebutPeriodeEnvoi.substring(3, 5)).intValue();
            if (!((moisDebutPeriodeEnvoi >= 1) && (moisDebutPeriodeEnvoi <= 12))) {
                // Le message remonté dans le catch ci-dessous donne déjà toutes les informations nécessaires à
                // l'utilisateur
                throw new Exception();
            }
        } catch (Exception e) {
            isMoisDebutPeriodeEnvoiValide = false;
            _addError(statement.getTransaction(),
                    getSession().getLabel("CI_ANNONCE_CENTRALE_ERREUR_PROPERTY_ANNONCE_PERIODE_DEBUT_MANDATORY") + " "
                            + e.toString());
        }

        int moisFinPeriodeEnvoi = 0;
        String joursMoisFinPeriodeEnvoi = "";
        try {
            joursMoisFinPeriodeEnvoi = GlobazServer.getCurrentSystem()
                    .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO).getProperty("annonce.periode.fin");
            moisFinPeriodeEnvoi = Integer.valueOf(joursMoisFinPeriodeEnvoi.substring(3, 5)).intValue();
            if (!((moisFinPeriodeEnvoi >= 1) && (moisFinPeriodeEnvoi <= 12))) {
                // Le message remonté dans le catch ci-dessous donne déjà toutes les informations nécessaires à
                // l'utilisateur
                throw new Exception();
            }
        } catch (Exception e) {
            isMoisFinPeriodeEnvoiValide = false;
            _addError(statement.getTransaction(),
                    getSession().getLabel("CI_ANNONCE_CENTRALE_ERREUR_PROPERTY_ANNONCE_PERIODE_FIN_MANDATORY") + " "
                            + e.toString());
        }

        if (!JadeDateUtil.isGlobazDate(getDateCreation())) {
            isDateCreationValide = false;
            _addError(statement.getTransaction(), getSession().getLabel("CI_ANNONCE_CENTRALE_DATE_CREATION_MANDATORY"));
        }

        if (!JadeDateUtil.isGlobazDate(getDateEnvoi())) {
            isDateEnvoiValide = false;
            _addError(statement.getTransaction(), getSession().getLabel("CI_ANNONCE_CENTRALE_DATE_ENVOI_MANDATORY"));
        }

        if (isDateCreationValide && isDateEnvoiValide && JadeDateUtil.isDateAfter(getDateCreation(), getDateEnvoi())) {
            _addError(statement.getTransaction(),
                    getSession().getLabel("CI_ANNONCE_CENTRALE_ERREUR_DATE_CREATION_APRES_DATE_ENVOI"));
        }

        if (isDateCreationValide && isDateEnvoiValide) {
            String moisAnneeDateCreation = dateCreation.substring(3, 10);
            String moisAnneeDateEnvoi = dateEnvoi.substring(3, 10);

            if (!moisAnneeDateCreation.equalsIgnoreCase(moisAnneeDateEnvoi)) {
                _addError(
                        statement.getTransaction(),
                        getSession().getLabel(
                                "CI_ANNONCE_CENTRALE_ERREUR_MOIS_ANNEE_DATE_CREATION_DIFFERENT_MOIS_ANNEE_DATE_ENVOI"));
            }
        }

        if (isMoisDebutPeriodeEnvoiValide && isMoisFinPeriodeEnvoiValide && isDateCreationValide) {
            int moisDateCreation = Integer.valueOf(dateCreation.substring(3, 5)).intValue();
            boolean isMauvaisTypeAnnonce = ((moisDateCreation == moisFinPeriodeEnvoi) && !CIAnnonceCentrale.CS_DERNIER_ANNONCE_ANNEE
                    .equalsIgnoreCase(getIdTypeAnnonce()))
                    || ((moisDateCreation >= moisDebutPeriodeEnvoi) && (moisDateCreation < moisFinPeriodeEnvoi) && !CIAnnonceCentrale.CS_ANNONCE_INTERCALAIRE
                            .equalsIgnoreCase(getIdTypeAnnonce()));
            if (isMauvaisTypeAnnonce) {
                _addError(statement.getTransaction(), FWMessageFormat.format(
                        getSession().getLabel("CI_ANNONCE_CENTRALE_ERREUR_TYPE_ANNONCE_MANDATORY"),
                        moisDebutPeriodeEnvoi + " - " + moisFinPeriodeEnvoi,
                        CodeSystem.getLibelle(CIAnnonceCentrale.CS_ANNONCE_INTERCALAIRE, getSession()),
                        CodeSystem.getLibelle(CIAnnonceCentrale.CS_DERNIER_ANNONCE_ANNEE, getSession())));

            }

            boolean isAvantDebutPeriodeEnvoi = moisDateCreation < moisDebutPeriodeEnvoi;

            if (isAvantDebutPeriodeEnvoi) {
                _addError(statement.getTransaction(), FWMessageFormat.format(
                        getSession().getLabel("CI_ANNONCE_CENTRALE_ERREUR_ANNONCE_AVANT_DEBUT_PERIODE_ENVOI"),
                        joursMoisDebutPeriodeEnvoi));
            }

            boolean isApresFinPeriodeEnvoi = moisDateCreation > moisFinPeriodeEnvoi;

            if (isApresFinPeriodeEnvoi
                    && (!annonceHorsPeriodeQuittancee || !CIAnnonceCentrale.CS_DERNIER_ANNONCE_ANNEE
                            .equalsIgnoreCase(getIdTypeAnnonce()))) {
                _addError(statement.getTransaction(), FWMessageFormat.format(
                        getSession().getLabel("CI_ANNONCE_CENTRALE_ERREUR_ANNONCE_APRES_FIN_PERIODE_ENVOI_MANDATORY"),
                        joursMoisFinPeriodeEnvoi,
                        CodeSystem.getLibelle(CIAnnonceCentrale.CS_DERNIER_ANNONCE_ANNEE, getSession())));
            }

        }

    }

    /**
	
	 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("KRIANC", this._dbWriteNumeric(statement.getTransaction(), getAnnonceCentraleId(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("KRIANC",
                this._dbWriteNumeric(statement.getTransaction(), getAnnonceCentraleId(), "annonceCentraleId"));
        statement.writeField("KRNINS",
                this._dbWriteNumeric(statement.getTransaction(), getNbrInscriptions(), "nbrInscriptions"));
        statement.writeField("KRMINS",
                this._dbWriteNumeric(statement.getTransaction(), getMontantTotal(), "montantTotal"));
        statement.writeField("KRDCRE",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateCreation(), "dateCreation"));
        statement.writeField("KRDENV", this._dbWriteDateAMJ(statement.getTransaction(), getDateEnvoi(), "dateEnvoi"));
        statement.writeField("KRTANN",
                this._dbWriteNumeric(statement.getTransaction(), getIdTypeAnnonce(), "idTypeAnnonce"));
        statement.writeField("KRTETA", this._dbWriteNumeric(statement.getTransaction(), getIdEtat(), "idEtat"));
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getAnnonceCentraleId() {
        return annonceCentraleId;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public String getIdEtat() {
        return idEtat;
    }

    public String getIdTypeAnnonce() {
        return idTypeAnnonce;
    }

    public String getMontantTotal() {
        return montantTotal;
    }

    public String getNbrInscriptions() {
        return nbrInscriptions;
    }

    public boolean isDejaAnnonceExistante() throws Exception {

        int anneeNewAnnonce = JACalendar.getYear(getDateCreation());
        boolean isAnnonceExistante = false;

        CIAnnonceCentraleManager annonceCentraleManager = new CIAnnonceCentraleManager();
        annonceCentraleManager.setSession(getSession());
        annonceCentraleManager.setForNotAnnonceCentraleId(getAnnonceCentraleId());
        annonceCentraleManager.setForAnnee(String.valueOf(anneeNewAnnonce));
        annonceCentraleManager.find();

        int moisNewAnnonce = JACalendar.getMonth(getDateCreation());
        for (int i = 0; i < annonceCentraleManager.size(); i++) {
            CIAnnonceCentrale aAnnonce = (CIAnnonceCentrale) annonceCentraleManager.getEntity(i);
            int moisAnnonceExistante = JACalendar.getMonth(aAnnonce.getDateCreation());
            if (moisNewAnnonce == moisAnnonceExistante) {
                isAnnonceExistante = true;
            }
        }

        return isAnnonceExistante;

    }

    public boolean isDerniereAnnonceAnneeDejaExistante() throws Exception {

        int anneeNewAnnonce = JACalendar.getYear(getDateCreation());

        CIAnnonceCentraleManager annonceCentraleManager = new CIAnnonceCentraleManager();
        annonceCentraleManager.setSession(getSession());
        annonceCentraleManager.setForNotAnnonceCentraleId(getAnnonceCentraleId());
        annonceCentraleManager.setForAnnee(String.valueOf(anneeNewAnnonce));
        annonceCentraleManager.setForIdTypeAnnonce(CIAnnonceCentrale.CS_DERNIER_ANNONCE_ANNEE);

        return annonceCentraleManager.getCount() >= 1;

    }

    public boolean isGenerationAnnoncePossible() {

        boolean isGenerationPossible = false;

        String moisAnneeAnnonce = getDateCreation().substring(3, 10);
        String moisAnneeCourant = JACalendar.todayJJsMMsAAAA().substring(3, 10);

        if (moisAnneeCourant.equalsIgnoreCase(moisAnneeAnnonce) && JadeStringUtil.isBlankOrZero(getIdEtat())) {
            isGenerationPossible = true;
        }

        return isGenerationPossible;
    }

    public boolean isImpressionAnnoncePossible() {
        return CIAnnonceCentrale.CS_ETAT_GENERE.equalsIgnoreCase(getIdEtat())
                || CIAnnonceCentrale.CS_ETAT_ENVOYE.equalsIgnoreCase(getIdEtat());
    }

    public String returnAnneeCreation() throws Exception {
        return String.valueOf(JACalendar.getYear(dateCreation));
    }

    public String returnNomMoisCreation() throws Exception {
        return JACalendar.getMonthName(JACalendar.getMonth(dateCreation), getSession().getIdLangueISO());
    }

    public String returnUserVisaFromSpy() {
        return getSpy().getUser();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */
    public void setAnnonceCentraleId(String newAnnonceCentraleId) {
        annonceCentraleId = newAnnonceCentraleId;
    }

    public void setAnnonceHorsPeriodeQuittancee(boolean annonceHorsPeriodeQuittancee) {
        this.annonceHorsPeriodeQuittancee = annonceHorsPeriodeQuittancee;
    }

    public void setDateCreation(String newDateCreation) {
        dateCreation = newDateCreation;
    }

    public void setDateEnvoi(String newDateEnvoi) {
        dateEnvoi = newDateEnvoi;
    }

    public void setIdEtat(String newIdEtat) {
        idEtat = newIdEtat;
    }

    public void setIdTypeAnnonce(String newIdTypeAnnonce) {
        idTypeAnnonce = newIdTypeAnnonce;
    }

    public void setMontantTotal(String newMontantTotal) {
        montantTotal = newMontantTotal;
    }

    public void setNbrInscriptions(String newNbrInscriptions) {
        nbrInscriptions = newNbrInscriptions;
    }

}
