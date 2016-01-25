/*
 * Créé le 7 sept. 05
 */
package globaz.ij.db.annonces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.ij.api.annonces.IIJAnnonce;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prestations.IJPrestationManager;
import globaz.prestation.tools.PRHierarchique;
import globaz.prestation.tools.PRStringUtils;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Attention, les périodes des annonces ne sont pas chargées si l'entity est chargée à partir d'un manager. Il faut
 * utiliser {@link #loadPeriodesAnnonces(BTransaction) loadPeriodeAnnonces(BTransaction)}
 * </p>
 * 
 * @author dvh
 */
public class IJAnnonce extends BEntity implements PRHierarchique {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     */
    public static final String FIELDNAME_CODEAPPLICATION = "XXACOA";

    /**
     */
    public static final String FIELDNAME_CODEENREGISTREMENT = "XXACOE";

    /**
     */
    public static final String FIELDNAME_CODEENREGISTREMENTSUIVANTPERIODEIJ2ET3 = "XXACES";

    /**
     */
    public static final String FIELDNAME_CODEETATCIVIL = "XXACEC";

    /**
     */
    public static final String FIELDNAME_CONTENUANNONCE = "XXACON";

    /**
     */
    public static final String FIELDNAME_CS_ETAT = "XXTETA";

    /**
     */
    public static final String FIELDNAME_CS_GENREREADAPTATION = "XXAGRE";

    /**
     */
    public static final String fieldname_DATEENVOI = "XXDENV";

    /**
     */
    public static final String FIELDNAME_DROITACQUIS4EMEREVISION = "XXBDAC";

    /**
     */
    public static final String FIELDNAME_GARANTIEAA = "XXBGAA";

    /**
     */
    public static final String FIELDNAME_IDANNONCE = "XXIANN";

    /**
     */
    public static final String FIELDNAME_IDPARENT = "XXIPAR";

    /**
     */
    public static final String FIELDNAME_IJREDUITE = "XXBIJR";

    /**
     */
    public static final String FIELDNAME_MOISANNEECOMPTABLE = "XXNMAC";

    /**
     */
    public static final String FIELDNAME_NO_DECISION_AI_COMMUNICATION = "XXNDAI";

    /**
     */
    public static final String FIELDNAME_NOAGENCE = "XXANOA";

    /**
     */
    public static final String FIELDNAME_NOASSURE = "XXNNAS";

    /**
     */
    public static final String FIELDNAME_NOASSURECONJOINT = "XXNNOC";

    /**
     */
    public static final String FIELDNAME_NOCAISSE = "XXANOC";

    /**
     */
    public static final String FIELDNAME_NOMBREENFANTS = "XXNNEN";

    /**
     */
    public static final String FIELDNAME_OFFICEAI = "XXNOAI";

    /**
     */
    public static final String FIELDNAME_PARAMSPECIFIQUE3EMEREVISIONSUR5POSITIONS = "XXAALO";

    /**
     */
    public static final String FIELDNAME_PETITEIJ = "XXBPIJ";

    /**
     */
    public static final String FIELDNAME_REVENUJOURNALIERDETERMINANT = "XXMRJD";

    /**
     */
    public static final String TABLE_NAME = "IJANNONC";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String codeApplication = "";
    private String codeEnregistrement = "";
    private String codeEnregistrementSuivantPeriodeIJ2Et3 = "";
    private String codeEtatCivil = "";
    private String codeGenreCarte = "";
    private String codeGenreReadaptation = "";
    private String csEtat = IIJAnnonce.CS_VALIDE;
    private String dateEnvoi = "";
    private String droitAcquis4emeRevision = "";
    private String garantieAA = "";
    private String idAnnonce = "";
    private String idParent = "";
    private String ijReduite = "";
    private String moisAnneeComptable = "";
    private String noAgence = "";
    private String noAssure = "";
    private String noAssureConjoint = "";
    private String noCaisse = "";
    private String noDecisionAiCommunication = "";
    private String nombreEnfants = "";
    private String officeAI = "";
    private String paramSpecifique3emeRevisionSur5Positions = "     ";
    private IJPeriodeAnnonce periodeAnnonce1 = new IJPeriodeAnnonce();
    private IJPeriodeAnnonce periodeAnnonce2 = new IJPeriodeAnnonce();

    private String petiteIJ = "";
    private String revenuJournalierDeterminant = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        super._afterDelete(transaction);

        // effacement des periodes d'annonce
        IJPeriodeAnnonceManager periodeAnnonceManager = new IJPeriodeAnnonceManager();
        periodeAnnonceManager.setSession(getSession());
        periodeAnnonceManager.setForIdAnnonce(idAnnonce);
        periodeAnnonceManager.find(transaction, BManager.SIZE_NOLIMIT);

        for (int i = 0; i < periodeAnnonceManager.size(); i++) {
            IJPeriodeAnnonce periodeAnnonce = (IJPeriodeAnnonce) periodeAnnonceManager.getEntity(i);
            periodeAnnonce.delete(transaction);
        }

        // on enleve l'annonce de la prestation
        IJPrestationManager prestationManager = new IJPrestationManager();
        prestationManager.setSession(getSession());
        prestationManager.setForIdAnnonce(idAnnonce);
        prestationManager.find(transaction);

        for (int i = 0; i < prestationManager.size(); i++) {
            IJPrestation prestation = (IJPrestation) prestationManager.getEntity(i);
            prestation.setIdAnnonce("");
            prestation.update(transaction);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        super._afterRetrieve(transaction);

        if (!isLoadedFromManager()) {
            loadPeriodesAnnonces(transaction);
        }
    }

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        setIdAnnonce(_incCounter(transaction, "0"));
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idAnnonce = statement.dbReadNumeric(FIELDNAME_IDANNONCE);
        codeApplication = statement.dbReadString(FIELDNAME_CODEAPPLICATION);
        codeEnregistrement = statement.dbReadString(FIELDNAME_CODEENREGISTREMENT);
        noCaisse = statement.dbReadString(FIELDNAME_NOCAISSE);
        noAgence = statement.dbReadString(FIELDNAME_NOAGENCE);
        moisAnneeComptable = formatMoisAnneeComptableFromDB(statement.dbReadNumeric(FIELDNAME_MOISANNEECOMPTABLE));
        codeGenreCarte = statement.dbReadString(FIELDNAME_CONTENUANNONCE);
        petiteIJ = statement.dbReadString(FIELDNAME_PETITEIJ);
        noAssure = statement.dbReadString(FIELDNAME_NOASSURE);
        noAssureConjoint = statement.dbReadString(FIELDNAME_NOASSURECONJOINT);
        codeEtatCivil = statement.dbReadString(FIELDNAME_CODEETATCIVIL);
        nombreEnfants = statement.dbReadNumeric(FIELDNAME_NOMBREENFANTS);
        revenuJournalierDeterminant = statement.dbReadNumeric(FIELDNAME_REVENUJOURNALIERDETERMINANT);
        officeAI = statement.dbReadString(FIELDNAME_OFFICEAI);
        codeGenreReadaptation = statement.dbReadString(FIELDNAME_CS_GENREREADAPTATION);
        garantieAA = statement.dbReadString(FIELDNAME_GARANTIEAA);
        ijReduite = statement.dbReadString(FIELDNAME_IJREDUITE);
        droitAcquis4emeRevision = statement.dbReadString(FIELDNAME_DROITACQUIS4EMEREVISION);
        codeEnregistrementSuivantPeriodeIJ2Et3 = statement
                .dbReadString(FIELDNAME_CODEENREGISTREMENTSUIVANTPERIODEIJ2ET3);
        csEtat = statement.dbReadNumeric(FIELDNAME_CS_ETAT);
        idParent = statement.dbReadNumeric(FIELDNAME_IDPARENT);
        paramSpecifique3emeRevisionSur5Positions = deformatParamSpec(statement
                .dbReadString(FIELDNAME_PARAMSPECIFIQUE3EMEREVISIONSUR5POSITIONS));
        dateEnvoi = statement.dbReadDateAMJ(fieldname_DATEENVOI);
        noDecisionAiCommunication = statement.dbReadString(FIELDNAME_NO_DECISION_AI_COMMUNICATION);
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_IDANNONCE, _dbWriteNumeric(statement.getTransaction(), idAnnonce, "idAnnonce"));
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_IDANNONCE, _dbWriteNumeric(statement.getTransaction(), idAnnonce, "idAnnonce"));
        statement.writeField(FIELDNAME_CODEAPPLICATION,
                _dbWriteString(statement.getTransaction(), codeApplication, "codeApplication"));
        statement.writeField(FIELDNAME_CODEENREGISTREMENT,
                _dbWriteString(statement.getTransaction(), codeEnregistrement, "codeEnregistrement"));
        statement.writeField(FIELDNAME_NOCAISSE, _dbWriteString(statement.getTransaction(), noCaisse, "noCaisse"));
        statement.writeField(FIELDNAME_NOAGENCE, _dbWriteString(statement.getTransaction(), noAgence, "noAgence"));
        statement.writeField(
                FIELDNAME_MOISANNEECOMPTABLE,
                _dbWriteNumeric(statement.getTransaction(), formatMoisAnneeComptableToDB(moisAnneeComptable),
                        "moisAnneeComptable"));
        statement.writeField(FIELDNAME_CONTENUANNONCE,
                _dbWriteString(statement.getTransaction(), codeGenreCarte, "codeGenreCarte"));
        statement.writeField(FIELDNAME_PETITEIJ, _dbWriteString(statement.getTransaction(), petiteIJ, "petiteIJ"));
        statement.writeField(FIELDNAME_NOASSURE, _dbWriteString(statement.getTransaction(), noAssure, "noAssure"));
        statement.writeField(FIELDNAME_NOASSURECONJOINT,
                _dbWriteString(statement.getTransaction(), noAssureConjoint, "noAssureConjoint"));
        statement.writeField(FIELDNAME_CODEETATCIVIL,
                _dbWriteString(statement.getTransaction(), codeEtatCivil, "codeEtatCivil"));

        if (nombreEnfants.equals("")) {
            statement.writeField(FIELDNAME_NOMBREENFANTS, null);
        } else {
            statement.writeField(FIELDNAME_NOMBREENFANTS,
                    _dbWriteNumeric(statement.getTransaction(), nombreEnfants, "nombreEnfants"));
        }

        if (revenuJournalierDeterminant.equals("")) {
            statement.writeField(FIELDNAME_REVENUJOURNALIERDETERMINANT, null);
        } else {
            statement.writeField(
                    FIELDNAME_REVENUJOURNALIERDETERMINANT,
                    _dbWriteNumeric(statement.getTransaction(), revenuJournalierDeterminant,
                            "revenuJournalierDeterminant"));
        }

        statement.writeField(FIELDNAME_OFFICEAI, _dbWriteString(statement.getTransaction(), officeAI, "officeAI"));
        statement.writeField(FIELDNAME_CS_GENREREADAPTATION,
                _dbWriteString(statement.getTransaction(), codeGenreReadaptation, "codeGenreReadaptation"));
        statement
                .writeField(FIELDNAME_GARANTIEAA, _dbWriteString(statement.getTransaction(), garantieAA, "garantieAA"));
        statement.writeField(FIELDNAME_IJREDUITE, _dbWriteString(statement.getTransaction(), ijReduite, "ijReduite"));
        statement.writeField(FIELDNAME_DROITACQUIS4EMEREVISION,
                _dbWriteString(statement.getTransaction(), droitAcquis4emeRevision, "droitAcquis4emeRevision"));
        statement.writeField(
                FIELDNAME_CODEENREGISTREMENTSUIVANTPERIODEIJ2ET3,
                _dbWriteString(statement.getTransaction(), codeEnregistrementSuivantPeriodeIJ2Et3,
                        "codeEnregistrementSuivantPeriodeIJ2Et3"));
        statement.writeField(FIELDNAME_CS_ETAT, _dbWriteNumeric(statement.getTransaction(), csEtat, "csEtat"));
        statement.writeField(FIELDNAME_IDPARENT, _dbWriteNumeric(statement.getTransaction(), idParent, "idParent"));
        statement.writeField(
                FIELDNAME_PARAMSPECIFIQUE3EMEREVISIONSUR5POSITIONS,
                _dbWriteString(statement.getTransaction(), formatParamSpec(paramSpecifique3emeRevisionSur5Positions),
                        "paramSpecifique3emeRevisionSur5Positions"));
        statement.writeField(fieldname_DATEENVOI, _dbWriteDateAMJ(statement.getTransaction(), dateEnvoi, "dateEnvoi"));
        statement.writeField(FIELDNAME_NO_DECISION_AI_COMMUNICATION,
                _dbWriteString(statement.getTransaction(), noDecisionAiCommunication, "noDecisionAiCommunication"));
    }

    /**
     * copie tous les champs (y compris les périodes de l'annonce) excepté la clef primaire dans un nouvel entity, et
     * donne la session du parent
     * 
     * @return DOCUMENT ME!
     */
    public IJAnnonce createClone() {
        IJAnnonce annonce = new IJAnnonce();
        annonce.setSession(getSession());
        annonce.setCodeApplication(codeApplication);
        annonce.setCodeEnregistrement(codeEnregistrement);
        annonce.setNoCaisse(noCaisse);
        annonce.setNoAgence(noAgence);
        annonce.setMoisAnneeComptable(moisAnneeComptable);
        annonce.setCodeGenreCarte(codeGenreCarte);
        annonce.setPetiteIJ(petiteIJ);
        annonce.setNoAssure(noAssure);
        annonce.setNoAssureConjoint(noAssureConjoint);
        annonce.setCodeEtatCivil(codeEtatCivil);
        annonce.setNombreEnfants(nombreEnfants);
        annonce.setRevenuJournalierDeterminant(revenuJournalierDeterminant);
        annonce.setOfficeAI(officeAI);
        annonce.setCodeGenreReadaptation(codeGenreReadaptation);
        annonce.setGarantieAA(garantieAA);
        annonce.setIjReduite(ijReduite);
        annonce.setDroitAcquis4emeRevision(droitAcquis4emeRevision);
        annonce.setCodeEnregistrementSuivantPeriodeIJ2Et3(codeEnregistrementSuivantPeriodeIJ2Et3);
        annonce.setCsEtat(csEtat);
        annonce.setIdParent(idParent);
        annonce.setParamSpecifique3emeRevisionSur5Positions(paramSpecifique3emeRevisionSur5Positions);
        annonce.setDateEnvoi(dateEnvoi);
        annonce.setPeriodeAnnonce1(periodeAnnonce1);
        annonce.setPeriodeAnnonce2(periodeAnnonce2);
        annonce.setNoDecisionAiCommunication(noDecisionAiCommunication);

        return annonce;
    }

    private String deformatParamSpec(String string) {
        return PRStringUtils.split(string, '_')[0];
    }

    private String formatMoisAnneeComptableFromDB(String date) {
        try {
            String ret = JACalendar.format(date.substring(4, 6) + date.substring(0, 4), JACalendar.FORMAT_MMsYYYY);

            return ret;
        } catch (Exception e) {
            return date;
        }
    }

    private String formatMoisAnneeComptableToDB(String date) {
        try {
            if (date.length() == 6) {
                return date.substring(2) + "0" + date.substring(0, 1);
            } else {
                return date.substring(3) + date.substring(0, 2);
            }
        } catch (Exception e) {
            return "0";
        }
    }

    private String formatParamSpec(String string) {
        return "_" + string + "_";
    }

    /**
     * getter pour l'attribut code application
     * 
     * @return la valeur courante de l'attribut code application
     */
    public String getCodeApplication() {
        return codeApplication;
    }

    /**
     * getter pour l'attribut code enregistrement
     * 
     * @return la valeur courante de l'attribut code enregistrement
     */
    public String getCodeEnregistrement() {
        return codeEnregistrement;
    }

    /**
     * getter pour l'attribut code enregistrement suivant periode IJ2Et3
     * 
     * @return la valeur courante de l'attribut code enregistrement suivant periode IJ2Et3
     */
    public String getCodeEnregistrementSuivantPeriodeIJ2Et3() {
        return codeEnregistrementSuivantPeriodeIJ2Et3;
    }

    /**
     * getter pour l'attribut code etat civil
     * 
     * @return la valeur courante de l'attribut code etat civil
     */
    public String getCodeEtatCivil() {
        return codeEtatCivil;
    }

    /**
     * getter pour l'attribut code genre carte
     * 
     * @return la valeur courante de l'attribut code genre carte
     */
    public String getCodeGenreCarte() {
        return codeGenreCarte;
    }

    /**
     * getter pour l'attribut code genre readaptation
     * 
     * @return la valeur courante de l'attribut code genre readaptation
     */
    public String getCodeGenreReadaptation() {
        return codeGenreReadaptation;
    }

    /**
     * getter pour l'attribut cs etat
     * 
     * @return la valeur courante de l'attribut cs etat
     */
    public String getCsEtat() {
        return csEtat;
    }

    /**
     * getter pour l'attribut date envoi
     * 
     * @return la valeur courante de l'attribut date envoi
     */
    public String getDateEnvoi() {
        return dateEnvoi;
    }

    /**
     * getter pour l'attribut droit acquis4eme revision
     * 
     * @return la valeur courante de l'attribut droit acquis4eme revision
     */
    public String getDroitAcquis4emeRevision() {
        return droitAcquis4emeRevision;
    }

    /**
     * getter pour l'attribut garantie AA
     * 
     * @return la valeur courante de l'attribut garantie AA
     */
    public String getGarantieAA() {
        return garantieAA;
    }

    /**
     * getter pour l'attribut id annonce
     * 
     * @return la valeur courante de l'attribut id annonce
     */
    public String getIdAnnonce() {
        return idAnnonce;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.tools.PRHierarchique#getIdMajeur()
     * 
     * @return la valeur courante de l'attribut id majeur
     */
    @Override
    public String getIdMajeur() {
        return idAnnonce;
    }

    /**
     * getter pour l'attribut id parent
     * 
     * @return la valeur courante de l'attribut id parent
     */
    @Override
    public String getIdParent() {
        return idParent;
    }

    /**
     * getter pour l'attribut ij reduite
     * 
     * @return la valeur courante de l'attribut ij reduite
     */
    public String getIjReduite() {
        return ijReduite;
    }

    /**
     * getter pour l'attribut mois annee comptable
     * 
     * @return la valeur courante de l'attribut mois annee comptable
     */
    public String getMoisAnneeComptable() {
        return moisAnneeComptable;
    }

    /**
     * getter pour l'attribut no agence
     * 
     * @return la valeur courante de l'attribut no agence
     */
    public String getNoAgence() {
        return noAgence;
    }

    /**
     * getter pour l'attribut no assure
     * 
     * @return la valeur courante de l'attribut no assure
     */
    public String getNoAssure() {
        return noAssure;
    }

    /**
     * getter pour l'attribut no assure conjoint
     * 
     * @return la valeur courante de l'attribut no assure conjoint
     */
    public String getNoAssureConjoint() {
        return noAssureConjoint;
    }

    /**
     * getter pour l'attribut no caisse
     * 
     * @return la valeur courante de l'attribut no caisse
     */
    public String getNoCaisse() {
        return noCaisse;
    }

    public String getNoDecisionAiCommunication() {
        return noDecisionAiCommunication;
    }

    /**
     * getter pour l'attribut nombre enfants
     * 
     * @return la valeur courante de l'attribut nombre enfants
     */
    public String getNombreEnfants() {
        return nombreEnfants;
    }

    /**
     * getter pour l'attribut office AI
     * 
     * @return la valeur courante de l'attribut office AI
     */
    public String getOfficeAI() {
        return officeAI;
    }

    /**
     * getter pour l'attribut param specifique3eme revision sur5 positions
     * 
     * @return la valeur courante de l'attribut param specifique3eme revision sur5 positions
     */
    public String getParamSpecifique3emeRevisionSur5Positions() {
        return paramSpecifique3emeRevisionSur5Positions;
    }

    /**
     * getter pour l'attribut periode annonce1
     * 
     * @return la valeur courante de l'attribut periode annonce1
     */
    public IJPeriodeAnnonce getPeriodeAnnonce1() {
        return periodeAnnonce1;
    }

    /**
     * getter pour l'attribut periode annonce2
     * 
     * @return la valeur courante de l'attribut periode annonce2
     */
    public IJPeriodeAnnonce getPeriodeAnnonce2() {
        return periodeAnnonce2;
    }

    /**
     * getter pour l'attribut petite IJ
     * 
     * @return la valeur courante de l'attribut petite IJ
     */
    public String getPetiteIJ() {
        return petiteIJ;
    }

    /**
     * getter pour l'attribut revenu journalier determinant
     * 
     * @return la valeur courante de l'attribut revenu journalier determinant
     */
    public String getRevenuJournalierDeterminant() {
        return revenuJournalierDeterminant;
    }

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void loadPeriodesAnnonces(BTransaction transaction) throws Exception {
        // si la periode1 est nulle c'est qu'elles ne sont pas chargées
        // puisqu'il en existe forcément une

        if (periodeAnnonce1.isNew()) {
            IJPeriodeAnnonceManager periodeAnnonceManager = new IJPeriodeAnnonceManager();
            periodeAnnonceManager.setSession(getSession());
            periodeAnnonceManager.setForIdAnnonce(idAnnonce);
            periodeAnnonceManager.find(transaction);

            if (periodeAnnonceManager.isEmpty() || (periodeAnnonceManager.size() > 2)) {
                throw new Exception("nombre de periode d'annonce incohérante (" + periodeAnnonceManager.size() + ")");
            }

            periodeAnnonce1 = (IJPeriodeAnnonce) periodeAnnonceManager.getEntity(0);

            if (periodeAnnonceManager.size() == 2) {
                periodeAnnonce2 = (IJPeriodeAnnonce) periodeAnnonceManager.getEntity(1);
            }
        }
    }

    /**
     * setter pour l'attribut code application
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCodeApplication(String string) {
        codeApplication = string;
    }

    /**
     * setter pour l'attribut code enregistrement
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCodeEnregistrement(String string) {
        codeEnregistrement = string;
    }

    /**
     * setter pour l'attribut code enregistrement suivant periode IJ2Et3
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCodeEnregistrementSuivantPeriodeIJ2Et3(String string) {
        codeEnregistrementSuivantPeriodeIJ2Et3 = string;
    }

    /**
     * setter pour l'attribut code etat civil
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCodeEtatCivil(String string) {
        codeEtatCivil = string;
    }

    /**
     * setter pour l'attribut code genre carte
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCodeGenreCarte(String string) {
        codeGenreCarte = string;
    }

    /**
     * setter pour l'attribut code genre readaptation
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCodeGenreReadaptation(String string) {
        codeGenreReadaptation = string;
    }

    /**
     * setter pour l'attribut cs etat
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsEtat(String string) {
        csEtat = string;
    }

    /**
     * setter pour l'attribut date envoi
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateEnvoi(String string) {
        dateEnvoi = string;
    }

    /**
     * setter pour l'attribut droit acquis4eme revision
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDroitAcquis4emeRevision(String string) {
        droitAcquis4emeRevision = string;
    }

    /**
     * setter pour l'attribut garantie AA
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setGarantieAA(String string) {
        garantieAA = string;
    }

    /**
     * setter pour l'attribut id annonce
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdAnnonce(String string) {
        idAnnonce = string;
    }

    /**
     * setter pour l'attribut id parent
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdParent(String string) {
        idParent = string;
    }

    /**
     * setter pour l'attribut ij reduite
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIjReduite(String string) {
        ijReduite = string;
    }

    /**
     * setter pour l'attribut mois annee comptable
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setMoisAnneeComptable(String string) {
        moisAnneeComptable = string;
    }

    /**
     * setter pour l'attribut no agence
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoAgence(String string) {
        noAgence = string;
    }

    /**
     * setter pour l'attribut no assure
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoAssure(String string) {
        noAssure = string;
    }

    /**
     * setter pour l'attribut no assure conjoint
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoAssureConjoint(String string) {
        noAssureConjoint = string;
    }

    /**
     * setter pour l'attribut no caisse
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoCaisse(String string) {
        noCaisse = string;
    }

    public void setNoDecisionAiCommunication(String noDecisionAiCommunication) {
        this.noDecisionAiCommunication = noDecisionAiCommunication;
    }

    /**
     * setter pour l'attribut nombre enfants
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNombreEnfants(String string) {
        nombreEnfants = string;
    }

    /**
     * setter pour l'attribut office AI
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setOfficeAI(String string) {
        officeAI = string;
    }

    /**
     * setter pour l'attribut param specifique3eme revision sur5 positions
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setParamSpecifique3emeRevisionSur5Positions(String string) {
        paramSpecifique3emeRevisionSur5Positions = string;
    }

    private void setPeriodeAnnonce1(IJPeriodeAnnonce annonce) {
        periodeAnnonce1 = annonce;
    }

    private void setPeriodeAnnonce2(IJPeriodeAnnonce annonce) {
        periodeAnnonce2 = annonce;
    }

    /**
     * setter pour l'attribut petite IJ
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setPetiteIJ(String string) {
        petiteIJ = string;
    }

    /**
     * setter pour l'attribut revenu journalier determinant
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setRevenuJournalierDeterminant(String string) {
        revenuJournalierDeterminant = string;
    }
}
