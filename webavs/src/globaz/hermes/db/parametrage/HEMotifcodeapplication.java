package globaz.hermes.db.parametrage;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWIncrementation;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.hermes.application.HEApplication;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;

/**
 * Liste des motifs possibles pour un code d'aplication.<br>
 * Fichier HEMCAPP Date de création : (22.10.2002 13:37:06)
 * 
 * @author: Arnaud Dostes
 */
public class HEMotifcodeapplication extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Code application (RATCOA) */
    private String idCodeApplication = new String();
    /** id Critere Motif (RBICRM) */
    private String idCritereMotif = new String();
    /** id Motif (RATMOT) */
    private String idMotif = new String();
    /** Motif Code Application (RAIMCA) */
    private String idMotifCodeApplication = new String();
    /** libelle du critère */
    private String libelle = "";

    /**
     * Commentaire relatif au constructeur HEMotifCodeApplication.
     */
    public HEMotifcodeapplication() {
        super();
    }

    /**
     * Effectue des traitements après une lecture dans la BD et après avoir vidé le tampon de lecture <i>
     * <p>
     * A surcharger pour effectuer les traitements après la lecture de l'entité dans la BD
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws java.lang.Exception {
        /*
         * if (codeAppManager == null) { codeAppManager = new HEMotifcodeapplicationManager();
         * codeAppManager.setSession(getSession()); } codeAppManager.setForIdCritereMotif(getIdcriteremotif());
         * codeAppManager.find(); int i = codeAppManager.size();
         */
        // le libellé courant
        HECriteremotif criterePere = new HECriteremotif();
        criterePere.setSession(getSession());
        criterePere.setIdcriteremotif(getIdCritereMotif());
        criterePere.wantCallMethodAfter(false);
        criterePere.retrieve(transaction);
        //
        FWParametersSystemCodeManager pscM = ((HEApplication) getSession().getApplication())
                .getCsCritereListe(getSession());
        FWParametersSystemCode psc;
        FWParametersUserCode uc;
        /*
         * pscM.getCodeSysteme(criterePere.getCri_idcriteremotif()); FWParametersUserCode uc =
         * psc.getCurrentCodeUtilisateur(); libelle = uc.getLibelle() + libelle;
         */
        //
        /*
         * HECriteres critereLib = new HECriteres(); critereLib.setSession(getSession());
         * critereLib.setIdCode(getIdCritereMotif()); critereLib.retrieve(); libelle =
         * critereLib.getCurrentCodeUtilisateur().getCodeUtiLib() + libelle;
         */
        //
        String idCriterePere = criterePere.getIdcriteremotif();
        //
        // les libellés parents
        if (idCriterePere != null) {
            while (!JadeStringUtil.isBlankOrZero(idCriterePere)) {
                criterePere = new HECriteremotif();
                criterePere.setSession(getSession());
                criterePere.setIdcriteremotif(idCriterePere);
                criterePere.wantCallMethodAfter(false);
                criterePere.retrieve(transaction);
                idCriterePere = criterePere.getIdcritere();
                if (!JadeStringUtil.isBlankOrZero(idCriterePere)) {
                    if (pscM != null) {
                        psc = pscM.getCodeSysteme(criterePere.getCri_idcriteremotif());
                        if (psc != null) {
                            uc = psc.getCurrentCodeUtilisateur();
                            if (uc != null) {
                                libelle = uc.getLibelle() + ", " + libelle;
                            } else {
                                JadeLogger.info(this,
                                        "HEMotifcodeapplication._afterRetrieve(...), uc variable is null !");
                            }
                        } else {
                            JadeLogger.info(this, "HEMotifcodeapplication._afterRetrieve(...), psc variable is null !");
                        }
                    } else {
                        JadeLogger.info(this, "HEMotifcodeapplication._afterRetrieve(...), pscM variable is null !");
                    }
                }
            }
        } else {
            JadeLogger.info(this, "HEMotifcodeapplication._afterRetrieve(...), idCriterePere is null !");
        }
        if (!JadeStringUtil.isEmpty(libelle)) {
            libelle = libelle.trim().substring(0, libelle.trim().length() - 1);
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
        // choper le prochain incrément
        FWIncrementation inc = new FWIncrementation();
        inc.setSession(getSession());
        inc.setIdIncrement("HECODMOT");
        inc.setIdCodeSysteme("0");
        String s = "";
        inc.execute(transaction);
        // on set la nouvelle clef primaire
        setIdMotifCodeApplication(inc.getValeurIncrement());
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "HEMCAPP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception java.lang.Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idMotifCodeApplication = statement.dbReadNumeric("RAIMCA");
        idCritereMotif = statement.dbReadNumeric("RBICRM");
        idCodeApplication = statement.dbReadNumeric("RATCOA");
        idMotif = statement.dbReadNumeric("RATMOT");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
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
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("RAIMCA",
                _dbWriteNumeric(statement.getTransaction(), getIdMotifCodeApplication(), "Motif Code Application"));
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
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("RAIMCA",
                _dbWriteNumeric(statement.getTransaction(), getIdMotifCodeApplication(), "Motif Code Application"));
        statement.writeField("RBICRM",
                _dbWriteNumeric(statement.getTransaction(), getIdCritereMotif(), "id Critere Motif"));
        statement.writeField("RATCOA",
                _dbWriteNumeric(statement.getTransaction(), getIdCodeApplication(), "Code application"));
        statement.writeField("RATMOT", _dbWriteNumeric(statement.getTransaction(), getIdMotif(), "id Motif"));
    }

    public String getCritereLibelle() {
        return libelle;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @return String
     */
    public String getIdCodeApplication() {
        return idCodeApplication;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @return String
     */
    public String getIdCritereMotif() {
        return idCritereMotif;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @return String
     */
    public String getIdMotif() {
        return idMotif;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @return String
     */
    public String getIdMotifCodeApplication() {
        return idMotifCodeApplication;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newIdCodeApplication
     *            String
     */
    public void setIdCodeApplication(String newIdCodeApplication) {
        idCodeApplication = newIdCodeApplication;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newIdCritereMotif
     *            String
     */
    public void setIdCritereMotif(String newIdCritereMotif) {
        idCritereMotif = newIdCritereMotif;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newIdMotif
     *            String
     */
    public void setIdMotif(String newIdMotif) {
        idMotif = newIdMotif;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newIdMotifCodeApplication
     *            String
     */
    public void setIdMotifCodeApplication(String newIdMotifCodeApplication) {
        idMotifCodeApplication = newIdMotifCodeApplication;
    }
}
