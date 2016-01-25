package globaz.pavo.db.compte;

import globaz.framework.util.FWLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.application.CIApplication;
import globaz.pavo.util.CIUtil;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Object représentant une annonce en suspens. Date de création : (06.12.2002 14:16:44)
 * 
 * @author: Administrator
 */
public class CIAnnonceSuspens extends BEntity {

    private static final long serialVersionUID = 6531798941771115061L;
    // codes application
    public final static String CODE_21 = "21";
    public final static String CODE_22 = "22";
    public final static String CODE_23 = "23";
    public final static String CODE_29 = "29";
    public final static String CS_CI_COMPLEMENT = "321007";
    public final static String CS_ORDRE_CLOTURE = "321005";
    public final static String CS_ORDRE_RASSEMBLEMENT = "321004";
    public final static String CS_ORDRE_SPLITTING = "321003";
    public final static String CS_OUVERTURE = "321006";
    // Type d'annonce en suspens
    public final static String CS_REVOCATION_CLOTURE = "321001";
    public final static String CS_REVOCATION_SPLITTING = "321002";
    public final static String[] TRI_CI_NON_OUVERT = new String[] { CS_OUVERTURE, CS_ORDRE_SPLITTING,
            CS_ORDRE_RASSEMBLEMENT, CS_ORDRE_CLOTURE, CS_REVOCATION_CLOTURE, CS_REVOCATION_SPLITTING, CS_CI_COMPLEMENT };
    // tri pour ci ouvert et non ouvert
    public final static String[] TRI_CI_OUVERT = new String[] { CS_ORDRE_SPLITTING, CS_ORDRE_RASSEMBLEMENT,
            CS_ORDRE_CLOTURE, CS_REVOCATION_CLOTURE, CS_REVOCATION_SPLITTING, CS_OUVERTURE, CS_CI_COMPLEMENT };

    /** (KMBSUS) */
    private Boolean annonceSuspens = new Boolean(false);
    /** (KMISUS) */
    private String annonceSuspensId = new String();
    /** code application */
    private String codeApplication = new String();
    /** (KMDREC) */
    private String dateReception = new String();
    /** (RNIANN) */
    private String idAnnonce = new String();
    /** (KMID65) */
    private String idArc65 = new String();
    /** (KMIDLO) */
    private String idLog = new String();
    /** (KMIMOT) */
    private String idMotifArc = new String();
    /** (KMITTR) */
    private String idTypeTraitement = new String();
    // object log
    private FWLog log;
    /** (KMNAVS) */
    private String numeroAvs = new String();

    /** (KMNCAI) */
    private String numeroCaisse = new String();

    // code systeme
    /**
     * Commentaire relatif au constructeur CIAnnonceSuspens
     */
    public CIAnnonceSuspens() {
        super();
    }

    /**
     * Effectue des traitements après une mise à jour dans la BD
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws java.lang.Exception {
    }

    /**
     * Effectue des traitements avant un ajout dans la BD
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setAnnonceSuspensId(_incCounter(transaction, "0"));
        if (JadeStringUtil.isBlank(codeApplication)) {
            transaction.addErrors(getSession().getLabel("MSG_ANNONCE_ADD_CA"));
        } else {
            // définition du type de traitement
            if (CODE_21.equals(codeApplication)) {
                idTypeTraitement = CS_OUVERTURE;
            } else if (CODE_23.equals(codeApplication)) {
                idTypeTraitement = CS_CI_COMPLEMENT;
            } else if (CODE_29.equals(codeApplication)) {
                if ("99".equals(idMotifArc)) {
                    idTypeTraitement = CS_REVOCATION_CLOTURE;
                } else if ("96".equals(idMotifArc)) {
                    idTypeTraitement = CS_REVOCATION_SPLITTING;
                }
            } else if (CODE_22.equals(codeApplication)) {
                if ("95".equals(idMotifArc)) {
                    idTypeTraitement = CS_ORDRE_SPLITTING;
                } else if ("92".equals(idMotifArc) || "93".equals(idMotifArc) || "94".equals(idMotifArc)
                        || "97".equals(idMotifArc) || "98".equals(idMotifArc)) {
                    idTypeTraitement = CS_ORDRE_RASSEMBLEMENT;
                } else {
                    idTypeTraitement = CS_ORDRE_CLOTURE;
                }
            }
        }
        if (CIUtil.getAnnoncesABloquer().indexOf(idTypeTraitement) >= 0) {
            setAnnonceSuspens(new Boolean(true));
        }

        // Blocage de l'annonce si Securité sur les CI activé et que le CI est
        // protégé (sur le CI ou sur l'affiliation)
        CIApplication application = (CIApplication) getSession().getApplication();
        if (application.wantBlocageSecuriteCi()) {
            if (CODE_22.equals(codeApplication)) {
                if ("71".equals(idMotifArc) || "81".equals(idMotifArc) || "75".equals(idMotifArc)
                        || "85".equals(idMotifArc) || "79".equals(idMotifArc) || "92".equals(idMotifArc)
                        || "93".equals(idMotifArc) || "94".equals(idMotifArc) || "95".equals(idMotifArc)
                        || "97".equals(idMotifArc) || "98".equals(idMotifArc)) {

                    if (getCi() != null) {
                        // Vérification de la sécurité sur une affiliation
                        CIEcritureManager ecritureManager = new CIEcritureManager();
                        ecritureManager.setSession(getSession());
                        ecritureManager.setCacherEcritureProtege(1);
                        ecritureManager.setForCompteIndividuelId(getCi().getCompteIndividuelId());
                        ecritureManager.find();
                        // si le manager contient des écritures protégées par
                        // affiliation on suspens l'annonce
                        if (ecritureManager.hasEcritureProtegeParAffiliation()) {
                            getLog(transaction).logMessage(getSession().getLabel("MSG_DEMANDE_CI_PROTEGE"),
                                    FWMessage.FATAL, annonceSuspens.getClass().getName());
                            try {
                                log.update(transaction);
                            } catch (Exception inEx) {
                                System.out.println("error in update:");
                            }
                            setAnnonceSuspens(new Boolean(true));
                        }
                        // Vérification de la sécurité sur le CI
                        if (!getCi().hasUserShowRight(transaction, getSession().getUserId())) {
                            getLog(transaction).logMessage(getSession().getLabel("MSG_DEMANDE_CI_PROTEGE"),
                                    FWMessage.FATAL, annonceSuspens.getClass().getName());
                            try {
                                log.update(transaction);
                            } catch (Exception inEx) {
                                System.out.println("error in update:");
                            }
                            setAnnonceSuspens(new Boolean(true));
                        }
                    } else {
                        System.out.println("CI introuvable " + getNumeroAvs());
                    }
                }
            }
        }
    }

    /**
     * Teste si la supression est autorisée.
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        if (getSession() != null) {
            if (CIUtil.isSpecialist(getSession())) {
                deleteLog(transaction);
            } else {
                _addError(transaction, getSession().getLabel("MSG_ANNONCE_DEL_USER"));
            }
        }
    }

    /**
     * Effectue des traitements avant une mise à jour dans la BD
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws java.lang.Exception {
        if (!isAnnonceSuspens().booleanValue()) {
            // n'est plus en suspens: supprimer le log
            deleteLog(transaction);
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CISUSPP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        annonceSuspensId = statement.dbReadNumeric("KMISUS");
        idAnnonce = statement.dbReadNumeric("RNIANN");
        annonceSuspens = statement.dbReadBoolean("KMBSUS");
        idLog = statement.dbReadNumeric("KMIDLO");
        numeroAvs = statement.dbReadString("KMNAVS");
        idMotifArc = statement.dbReadNumeric("KMIMOT");
        idTypeTraitement = statement.dbReadNumeric("KMITTR");
        numeroCaisse = statement.dbReadString("KMNCAI");
        dateReception = statement.dbReadDateAMJ("KMDREC");
        idArc65 = statement.dbReadNumeric("KMID65");
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

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("KMISUS", _dbWriteNumeric(statement.getTransaction(), getAnnonceSuspensId(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("KMISUS",
                _dbWriteNumeric(statement.getTransaction(), getAnnonceSuspensId(), "annonceSuspensId"));
        statement.writeField("RNIANN", _dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), "idAnnonce"));
        statement.writeField(
                "KMBSUS",
                _dbWriteBoolean(statement.getTransaction(), isAnnonceSuspens(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "annonceSuspens"));
        statement.writeField("KMIDLO", _dbWriteNumeric(statement.getTransaction(), getIdLog(), "idLog"));
        statement.writeField("KMNAVS", _dbWriteString(statement.getTransaction(), getNumeroAvs(), "numeroAvs"));
        statement.writeField("KMIMOT", _dbWriteNumeric(statement.getTransaction(), getIdMotifArc(), "idMotifArc"));
        statement.writeField("KMITTR",
                _dbWriteNumeric(statement.getTransaction(), getIdTypeTraitement(), "idTypeTraitement"));
        statement.writeField("KMNCAI", _dbWriteString(statement.getTransaction(), getNumeroCaisse(), "numeroCaisse"));
        statement
                .writeField("KMDREC", _dbWriteDateAMJ(statement.getTransaction(), getDateReception(), "dateReception"));
        statement.writeField("KMID65", _dbWriteNumeric(statement.getTransaction(), getIdArc65(), "idArc65"));
    }

    /**
     * Supprime le log associé à cette annonce.
     * 
     * @param transaction
     *            la transaction à utiliser. Date de création : (10.12.2002 08:51:37)
     */
    public void deleteLog(BTransaction transaction) throws Exception {
        // if (!JAUtil.isIntegerEmpty(getIdLog())) {
        if (getIdLogInt() > 0) {
            // log existe, effacement
            FWLog log = new FWLog();
            log.setSession(getSession());
            log.setIdLog(getIdLog());
            log.retrieve(transaction);
            if (!log.isNew()) {
                log.delete(transaction);
            }
            idLog = "";
        }
        // }
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getAnnonceSuspensId() {
        return annonceSuspensId;
    }

    private CICompteIndividuel getCi() throws Exception {
        CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
        ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        ciMgr.setSession(getSession());
        ciMgr.setForNumeroAvs(numeroAvs);
        ciMgr.find();
        if (ciMgr.size() > 0) {
            return (CICompteIndividuel) ciMgr.getFirstEntity();
        } else {
            return null;
        }
    }

    public java.lang.String getCodeApplication() {
        return codeApplication;
    }

    public String getDateNaissanceForNNSS() {
        try {
            return getCi().getDateNaissance();
        } catch (Exception e) {
            return "";
        }
    }

    public String getDateReception() {
        return dateReception;
    }

    public String getIdAnnonce() {
        return idAnnonce;
    }

    public java.lang.String getIdArc65() {
        return idArc65;
    }

    /**
     * Retourne l'id du CI associé à l'annonce s'il existe au registre des assurés. Date de création : (09.12.2002
     * 11:15:34)
     * 
     * @return l'id du CI ou null si non trouvé.
     */
    public String getIdCIRA() {
        CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
        ciMgr.setSession(getSession());
        ciMgr.orderByAvs(false);
        CICompteIndividuel ci = ciMgr.getCIRegistreAssures(getNumeroAvs(), null);
        if (ci == null) {
            return null;
        } else {
            return ci.getCompteIndividuelId();
        }
    }

    public String getIdLog() {
        return idLog;
    }

    public int getIdLogInt() {
        int result = 0;
        try {
            result = Integer.parseInt(idLog);
        } catch (Exception ex) {
            // laisse result = 0;
        }
        return result;
    }

    public String getIdMotifArc() {
        return idMotifArc;
    }

    public String getIdTypeTraitement() {
        return idTypeTraitement;
    }

    /**
     * Retourne un logger du type {@link globaz.framework.util.FWLog}. Date de création : (13.12.2002 15:10:57)
     * 
     * @param transaction
     *            la transaction à utiliser.
     * @return le logger associé à cette annonce.
     */
    public FWLog getLog(BTransaction transaction) throws Exception {
        // System.out.print("(idlog="+idLog);
        if (getIdLogInt() <= 0) {
            log = new FWLog();
            log.setISession(GlobazSystem.getApplication("FRAMEWORK").newSession(getSession()));
            log.add(transaction);
            idLog = log.getIdLog();
        } else {
            // System.out.print(", existing");
            if (log == null) {
                log = new FWLog();
                log.setISession(GlobazSystem.getApplication("FRAMEWORK").newSession(getSession()));
                log.setIdLog(idLog);
                log.retrieve(transaction);
                // System.out.print(", retrieved");
            }
        }
        // System.out.println();
        return log;
    }

    /**
     * Retourne la liste de log afin de l'utiliser pour {@link globaz.jsp.taglib.FWListSelectTag} Date de création :
     * (10.12.2002 08:55:41)
     * 
     * @return java.util.Vector
     */
    public Vector getLogs() {
        Vector list = new Vector();
        if (getIdLogInt() > 0) {
            FWLog logs;
            try {
                logs = getLog(null);
                if (!logs.isNew()) {
                    Enumeration enum1 = logs.getMessagesToEnumeration();
                    for (int i = 0; enum1.hasMoreElements(); i++) {
                        list.add(new String[] { String.valueOf(i), ((FWMessage) enum1.nextElement()).getMessageText() });
                    }
                }
            } catch (Exception ex) {
                // retourne liste vide
            }
        }
        return list;
    }

    /**
     * Retourne le type du message du plus haut niveau.<br>
     * <ul>
     * <li>1 - information</li>
     * <li>2 - avertissement</li>
     * <li>3 - erreur</li>
     * <li>4 - fatal</li>
     * <li>0 - pas de message</li> Date de création : (09.12.2002 10:12:09)
     * 
     * @return le type du message du plus haut niveau.
     */
    public int getLogType() {
        if (getIdLogInt() <= 0) {
            return 0;
        }
        FWLog logs;
        try {
            logs = getLog(null);
            if (logs.isNew()) {
                return 0;
            }
            String type = logs.getHighestMessage().getTypeMessage();
            if (FWMessage.INFORMATION.equals(type)) {
                return 1;
            }
            if (FWMessage.AVERTISSEMENT.equals(type)) {
                return 2;
            }
            if (FWMessage.ERREUR.equals(type)) {
                return 3;
            }
            if (FWMessage.FATAL.equals(type)) {
                return 4;
            }
        } catch (Exception ex) {
            return 0;
        }
        return 0;
    }

    public String getNumeroAvs() {
        return numeroAvs;
    }

    public String getNumeroCaisse() {
        return numeroCaisse;
    }

    public String getPaysFormate() {
        try {
            return getCi().getPaysFormate();
        } catch (Exception e) {
            return "";
        }
    }

    public String getPaysForNNSS() {
        try {
            return getCi().getPaysForNNSS();
        } catch (Exception e) {
            return "";
        }
    }

    public String getSexeForNNSS() {
        try {
            return getCi().getSexeForNNSS();
        } catch (Exception e) {
            return "";
        }
    }

    public String getSexeLibelle() {
        try {
            return getCi().getSexeLibelle();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Retourne l'instance de {@link CIAnnonceWrapper} correspondant au type de l'annonce.
     * 
     * @return le wrapper Date de création : (12.12.2002 15:14:46)
     */
    public CIAnnonceWrapper getWrapper() throws Exception {
        if (CS_REVOCATION_CLOTURE.equals(idTypeTraitement)) {
            return new CIAnnonce29Cloture(this);
        } else if (CS_REVOCATION_SPLITTING.equals(idTypeTraitement)) {
            return new CIAnnonce29Splitting(this);
        } else if (CS_ORDRE_SPLITTING.equals(idTypeTraitement)) {
            return new CIAnnonce22Splitting(this);
        } else if (CS_ORDRE_RASSEMBLEMENT.equals(idTypeTraitement)) {
            return new CIAnnonce22Rassemblement(this);
        } else if (CS_ORDRE_CLOTURE.equals(idTypeTraitement)) {
            return new CIAnnonce22Cloture(this);
        } else if (CS_OUVERTURE.equals(idTypeTraitement)) {
            return new CIAnnonce21Ouverture(this);
        } else if (CS_CI_COMPLEMENT.equals(idTypeTraitement)) {
            return new CIAnnonce23Complement(this);
        }
        return null;
    }

    /**
     * Permet de savoir si un user a le droit de débloquer une annonce ayant été bloquée à cause de la sécurité CI
     * (affiliation et CI)
     * 
     * @return true si le user a le droit de débloquer
     * @throws Exception
     */
    public boolean hasRightForUnblock() throws Exception {
        boolean hasRightForUnblock = true;

        // si le message correspond à un blocage du à un CI sécurisé
        // (affiliation et CI)
        // si le ci est null on ne fait pas les testes et on autorise le user à
        // débloquer
        if (getCi() != null) {
            // Vérification de la sécurité sur l'affiliation et le CI
            CIEcritureManager ecritureManager = new CIEcritureManager();
            ecritureManager.setSession(getSession());
            ecritureManager.setCacherEcritureProtege(1);
            ecritureManager.setForCompteIndividuelId(getCi().getCompteIndividuelId());
            ecritureManager.find();
            // si le manager contient des écritures protégées par affiliation ou
            // que le user n'a pas les droits sur le ci on refuse le droit de
            // débloquer
            if (ecritureManager.hasEcritureProtegeParAffiliation() || !getCi().hasUserShowRight(null)) {
                hasRightForUnblock = false;
            }
        }
        return hasRightForUnblock;
    }

    public Boolean isAnnonceSuspens() {
        return annonceSuspens;
    }

    public void setAnnonceSuspens(Boolean newAnnonceSuspens) {
        annonceSuspens = newAnnonceSuspens;
    }

    public void setAnnonceSuspensId(String newAnnonceSuspensId) {
        annonceSuspensId = newAnnonceSuspensId;
    }

    public void setCodeApplication(java.lang.String newCodeApplication) {
        codeApplication = newCodeApplication;
    }

    public void setDateReception(String newDateReception) {
        dateReception = newDateReception;
    }

    public void setIdAnnonce(String newIdAnnonce) {
        idAnnonce = newIdAnnonce;
    }

    public void setIdArc65(java.lang.String newIdArc65) {
        idArc65 = newIdArc65;
    }

    public void setIdLog(String newIdLog) {
        idLog = newIdLog;
    }

    public void setIdMotifArc(String newIdMotifArc) {
        idMotifArc = newIdMotifArc;
    }

    public void setIdTypeTraitement(String newIdTypeTraitement) {
        idTypeTraitement = newIdTypeTraitement;
    }

    public void setNumeroAvs(String newNumeroAvs) {
        numeroAvs = newNumeroAvs;
    }

    public void setNumeroCaisse(String newNumeroCaisse) {
        numeroCaisse = newNumeroCaisse;
    }
}
