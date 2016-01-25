package globaz.pavo.db.compte;

import globaz.commons.nss.NSUtil;
import globaz.framework.secure.user.FWSecureUserDetail;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BPreparedStatement;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pavo.application.CIApplication;
import globaz.pavo.util.CIAffilie;
import globaz.pavo.util.CIAffilieManager;
import globaz.pavo.util.CIUtil;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Manager de <tt>CIEcriture</tt>. Date de création : (12.11.2002 13:07:10)
 * 
 * @author: David Girardin
 */
/**
 * @author JMC
 * 
 */
public class CIEcritureManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // (KBIECR) des
    // écritures à cacher
    private static String NIV_SECURITY_0 = "317000";
    private static String NIV_SECURITY_X = "31700";
    // attributs pour la sécurité du CI par rapport à l'affilié
    public static String SECURE_CODE = "SecureCode";

    /**
     * Requete permettant de selectionner les écritures dont l'affiliation (employeur) est protégée par un niveau > que
     * celui de l'utilisateur
     * 
     * @param colonneForWhere
     *            : permet de spécifier sur que colonne le where va être effectué (KAIIND ou KCID seulement)
     * @return La requete (String)
     * @throws Exception
     */
    protected static String getSqlChercheEcritureACacher(String colonneForWhere) throws Exception {
        /*
         * select * KBIECR from webavs.CIECRIP ecr inner join webavs.CIINDIP ci on ecr.KAIIND=ci.KAIIND inner join
         * webavs.AFAFFIP aff on ecr.KBITIE=aff.MAIAFF where ci.KAIIND=idCI and aff.MATSEC>31700+usersecure;
         */

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT KBIECR FROM ");
        sql.append(Jade.getInstance().getDefaultJdbcSchema());
        sql.append(".CIECRIP ecr inner join ");
        sql.append(Jade.getInstance().getDefaultJdbcSchema());
        sql.append(".CIINDIP ci on ecr.KAIIND=ci.KAIIND inner join ");
        sql.append(Jade.getInstance().getDefaultJdbcSchema());
        sql.append(".AFAFFIP aff on ecr.KBITIE=aff.MAIAFF ");
        // sql.append("WHERE ci.KAIIND=? and aff.MATSEC>?");

        if (colonneForWhere.equalsIgnoreCase("KAIIND")) {
            sql.append("WHERE ci.KAIIND=? and aff.MATSEC>?");
        } else if (colonneForWhere.equalsIgnoreCase("KCID")) {
            sql.append("WHERE ecr.KCID=? and aff.MATSEC>?");
        } else {
            throw new Exception("La colonneForWhere n'est pas correcte ou n'est pas spécifé");
        }

        return sql.toString();
    }

    /**
     * Requete permettant de séléctionner les écritures protégées du conjoint liée à une écriture de genre 8
     * 
     * @param colonneForWhere
     *            : KAIIND ou KCID
     * @return la requete (String)
     * @throws Exception
     */
    protected static String getSqlChercheEcritureGenre8ACacher(String colonneForWhere) throws Exception {
        /*
         * select * from (select * from webavs.CIECRIP where kaiind in (select distinct kbipar from webavs.CIECRIP where
         * kaiind=200404 and kbtgen=310008 and kbtext=0 and kbipar!=0))ecr inner join webavs.CIINDIP ci on
         * ecr.KAIIND=ci.KAIIND inner join webavs.AFAFFIP aff on ecr.KBITIE=aff.MAIAFF where
         * aff.MATSEC>31700+usersecure;
         */

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT KBIECR FROM (SELECT * FROM ");
        sql.append(Jade.getInstance().getDefaultJdbcSchema());
        sql.append(".CIECRIP WHERE KAIIND IN (SELECT DISTINCT KBIPAR FROM ");
        sql.append(Jade.getInstance().getDefaultJdbcSchema());

        if (colonneForWhere.equalsIgnoreCase("KAIIND")) {
            sql.append(".CIECRIP WHERE KAIIND=? AND KBTGEN=310008 and KBTEXT=0 and KBIPAR!=0))ecr inner join ");
        } else if (colonneForWhere.equalsIgnoreCase("KCID")) {
            sql.append(".CIECRIP WHERE KCID=? AND KBTGEN=310008 and KBTEXT=0 and KBIPAR!=0))ecr inner join ");
        } else {
            throw new Exception("La colonneForWhere n'est pas correcte ou n'est pas spécifé");
        }

        // sql.append(".CIECRIP WHERE KAIIND=? AND KBTGEN=310008 and KBTEXT=0 and KBIPAR!=0))ecr inner join ");
        sql.append(Jade.getInstance().getDefaultJdbcSchema());
        sql.append(".CIINDIP ci on ecr.KAIIND=ci.KAIIND inner join ");
        sql.append(Jade.getInstance().getDefaultJdbcSchema());
        sql.append(".AFAFFIP aff on ecr.KBITIE=aff.MAIAFF ");
        sql.append("WHERE aff.MATSEC>?");

        return sql.toString();
    }

    // type de compte ajustable
    private boolean adjustType = true;
    private String apartir;
    // permettant de cacher les
    // écritures dont l'affiliation est
    // protégée 1=active 0=désactivé
    // (impossible d'utiliser un boolean
    // à cause du setBeanProperties qui
    // set tjs false si null dans la
    // request)
    private int cacherEcritureEnSuspensProtege = 0;// Active ou désactive la
    private int cacherEcritureProtege = 0;// Active ou désactive la fonction
    // Pour spécifier si une lettre pour assurés non connus au RA doit être
    // envoyée
    private boolean certificat = false;
    // les écritures de
    // genre 8
    private ArrayList ecritureACacher = new ArrayList();// contient l'id
    // Pour afficher uniquement les inscriptions dont le numéro AVS est égal ou
    // supérieur à neuf chiffres
    private boolean ecrituresNonRA = false;
    // état des écritures
    private String etatEcritures = new String();
    // Pour exclure les ecritures suspens avec CI
    private boolean exclureRA = false;
    // fonction permettant de
    // cacher les écritures en
    // supspens dont
    // l'affiliation est
    // protégée 1=active
    // 0=désactivé (impossible
    // d'utiliser un boolean à
    // cause du
    // setBeanProperties qui set
    // tjs false si null dans la
    // request)
    private boolean existEcritureACacher = false;// true si il existe des
    // écritures dont
    // l'affiliation est
    // protégée et que le niveau
    // de sécurité du user n'est
    // pas suffisant
    private boolean existEcritureGenre8ACacher = false;// true si il faut cacher
    private String forAffilie = new String();
    /** (KBNANN) */
    private String forAnnee = new String();
    private String forCategoriePersonnel = new String();
    private String forCode = "";
    /** (KAIIND) */
    private String forCompteIndividuelId = new String();
    // list d'id de CI
    private Object[] forCompteIndividuelIdList;
    private String forDateAnnonceCentrale = new String();
    private String forDateNaissanceNumeric = new String();
    /** (KBITIE) */
    private String forEmployeur = new String();
    private String forExtourne = new String();
    private String forGenre = new String();
    private String forGenreCotPers = "";
    private String forGenreIn = new String();
    private String forIdIn = new String();
    /** (KBIECR) */
    private String forIdJournal = new String();
    private String forIdJournalIn = "";
    // (KBIECR) des
    // écritures
    // cachées
    private String forIdTiers = new String();
    /** (KBTCPT) */
    private String forIdTypeCompte = new String();
    // type compte CI, genre 6 et 7
    private String forIdTypeCompteCompta = new String();
    private String[] forIdTypeCompteList;
    private String forMasseSalariale = new String();
    private String forMoisDebut = new String();
    private String forMoisFin = new String();
    private String forMontant = new String();
    // cot pers
    private String forNotCompense = new String();
    private String forNotExtourne = new String();
    private String forNotGenre = new String();
    private String forNotId = new String();
    /** inverse de forIdTypeCompte (<>) */
    private String forNotIdTypeCompte = new String();
    // toutes les écritures clôturées
    private String forNotRassemblementOuvertureId = new String();
    private String forNotSuspensSup = new String();
    /** (KKIRAO) */
    private String forRassemblementOuvertureId = new String();
    private String forRee = "";
    private String fromAffilie = new String();
    private String fromAnnee = new String();
    /** Tampons de données */
    // Numéro d'AVS
    private String fromAvs = new String();
    private String fromAvsNNSS = "";
    private String fromDateInscription = new String();
    private String fromEmployeur = new String();
    private String fromIdJournal = new String();
    private String fromIdTypeCompte = new String();
    // clauses pour les listes d'écritures
    private String fromNumeroAvs = new String();
    private String fromNumeroAvsSuspenes = new String();
    private String fromNumeroAvsSuspenesNNSS = new String();
    private String fromPartenaire = new String();
    private String likeNomPrenom = new String();

    private String likeNomPrenomPartiel = new String();
    private ArrayList listeEcrituresCachees = new ArrayList();// contient l'id
    // Query sur le montant
    private String montantQuery = new String();
    private int nbeRowsFetchFirst = 0;
    private java.lang.String order = new String();
    // recherche
    private String tri;
    private String untilAnnee = new String();
    private String untilDateInscription = new String();
    private String untilGeMoisFin = new String();
    private String untilIdJournal = new String();

    private String untilMoisDebut = new String();

    private String untilNumeroAvs = new String();

    /**
     * Constructeur. Date de création : (09.01.2003 07:49:46)
     */
    public CIEcritureManager() {
        wantCallMethodBeforeFind(true);
    }

    @Override
    protected void _afterFind(globaz.globall.db.BTransaction transaction) {
        // TRAITEMENT DES ECRITURES
        if (existEcritureACacher) {
            for (int i = 0; i < getSize(); i++) {
                CIEcriture ecriture = (CIEcriture) getEntity(i);
                if (ecritureACacher.contains(ecriture.getId())) {
                    // AFAffiliation affiliation;
                    try {
                        // récupération de l'affilié
                        // affiliation = getAffiliationEcriture(ecriture);
                        // on vérifie le niveau de sécurité
                        // if (!affiliation.hasRightAccesSecurity()) {
                        // on cache le montant
                        ecriture.setCacherMontant(true);
                        // on ajout l'écriture à la liste des écritures cachées
                        // (pour le calcul de la somme)
                        listeEcrituresCachees.add(ecriture.getId());
                        // }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // TRAITEMENT DES ECRITURES DE GENRE 18
            if (listeEcrituresCachees.size() > 0) {
                for (int i = 0; i < getSize(); i++) {
                    CIEcriture ecriture = (CIEcriture) getEntity(i);
                    if (ecriture.getGenreEcriture().equals(CIEcriture.CS_CIGENRE_8)
                            && (!ecriture.getExtourne().equals("0") && !ecriture.getExtourne().equals(""))) {
                        ecriture.setCacherMontant(true);
                        // on ajout l'écriture à la liste des écritures cachées
                        // (pour le calcul de la somme)
                        listeEcrituresCachees.add(ecriture.getId());
                    }
                }
            }
        }

        // TRAITEMENT DES ECRITURES DE GENRE 8
        if (existEcritureGenre8ACacher) {
            for (int i = 0; i < getSize(); i++) {
                CIEcriture ecriture = (CIEcriture) getEntity(i);
                if (ecriture.getGenreEcriture().equals(CIEcriture.CS_CIGENRE_8)
                        && (ecriture.getExtourne().equals("0") || ecriture.getExtourne().equals(""))) {
                    ecriture.setCacherMontant(true);
                    // on ajout l'écriture à la liste des écritures cachées
                    // (pour le calcul de la somme)
                    listeEcrituresCachees.add(ecriture.getId());
                }
            }
        }

        // TRAITEMENT DES ECRITURES EN SUSPENS
        if (cacherEcritureEnSuspensProtege == 1) {
            for (int i = 0; i < getSize(); i++) {
                CIEcriture ecriture = (CIEcriture) getEntity(i);
                AFAffiliation affiliation;
                try {
                    // récupération de l'affilié
                    affiliation = getAffiliationEcriture(ecriture);
                    // on vérifie le niveau de sécurité
                    if (!affiliation.hasRightAccesSecurity()) {
                        // on cache le montant
                        ecriture.setCacherMontant(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Tri. Date de création : (28.10.2002 08:31:46)
     */
    @Override
    protected void _beforeFind(globaz.globall.db.BTransaction transaction) {
        if ((tri != null) && (tri.length() != 0)) {
            /*
             * if ("genre".equals(tri)) { // tri par genre if (apartir != null && apartir.length() != 0) { // à partir
             * d'un genre donné setFromIdTypeCompte(apartir); } // order by setOrderBy("KBTCPT"); } else if
             * ("employeur".equals(tri)) { // tri par employeur if (apartir != null && apartir.length() != 0) { // à
             * partir d'un nom donné setFromEmployeur(apartir); } // order by setOrderBy("KBITIE");
             */
            if ("date".equals(tri)) {
                // tri par date d'inscription
                if ((apartir != null) && (apartir.length() != 0)) {
                    // à partir d'une date donnée
                    setFromDateInscription(apartir);
                }
                // order by
                setOrderBy(" KBLESP DESC, KBNANN DESC,  KBNMOD DESC ,KBNMOF DESC");
            } else if ("affilie".equals(tri)) {
                // tri par date d'inscription
                if ((apartir != null) && (apartir.length() != 0)) {
                    setFromAffilie(apartir);
                    // à partir d'une date donnée
                    // CIApplication application;
                    // try {
                    // application = (CIApplication)
                    // getSession().getApplication();
                    // AFAffiliation aff =
                    // application.getAffilieByNo(getSession(),apartir,null);
                    // if(aff!=null)
                    // setFromAffilie(aff.getAffiliationId());
                    // } catch (Exception e) {
                    // e.printStackTrace();
                    // }
                }

                // order by
                // setOrderBy("KBITIE");
                setOrderBy("MALNAF DESC");
            }

            else if ("avs".equals(tri)) {
                // order by
                setOrderBy(_getCollection() + "CIINDIP.KANAVS");
                // pour afficher dans ecriture_rc_liste, sans effet de bord
            } else if ("beitragsjahr".equals(tri)) {
                if ((apartir != null) && (apartir.length() != 0)) {
                    // à partir d'une année donnée
                    setFromAnnee(apartir);
                }
                // order by
                setOrderBy("KBNANN DESC, KBNMOF DESC,KBNMOD DESC,KBLESP DESC ");

            } else if ("ordreInscription".equals(tri)) {
                if ((apartir != null) && (apartir.length() != 0)) {
                    // à partir d'une année donnée
                    setFromAnnee(apartir);
                }
                setOrderBy(" KBNANN DESC, " + _getCollection() + "CIECRIP.KBIECR DESC ");
            }

            else {
                // tri par année(défaut)
                if ((apartir != null) && (apartir.length() != 0)) {
                    // à partir d'une année donnée
                    setFromAnnee(apartir);
                }
                // order by
                setOrderBy("KBNANN DESC, KBNMOF DESC");
            }
        }
        if (etatEcritures.length() != 0) {
            if ("tous".equals(etatEcritures)) {
                // toutes écritures clôturées
                setForNotRassemblementOuvertureId("0");
            } else if ("active".equals(etatEcritures)) {
                // toutes les écriture actives
                setForRassemblementOuvertureId("0");
            } else if ("toutesEcritures".equals(etatEcritures)) {
                // toutes les écriture actives et non actives
            } else if ("seulementClotures".equals(etatEcritures)) {
                // toutes les écriture actives et non actives
                setForNotRassemblementOuvertureId("0");
            } else {
                // écritures d'une clôture
                setForRassemblementOuvertureId(etatEcritures);
            }
        }

        // si la vérification des écritures protégé par affiliation est activée
        // on cherche si il existe des écritures à cacher
        if ((cacherEcritureProtege == 1)
                && (!JadeStringUtil.isEmpty(getForCompteIndividuelId()) || !JadeStringUtil.isEmpty(getForIdJournal()))) {
            try {
                String userLevelSecurity = "";
                BTransaction transactionEcriture = null;
                try {
                    // recherche du complément "secureCode" du user (niveau de
                    // sécutité)
                    BSession session = getSession();
                    FWSecureUserDetail user = new FWSecureUserDetail();
                    user.setSession(session);
                    user.setUser(session.getUserId());
                    user.setLabel(CIEcritureManager.SECURE_CODE);
                    user.retrieve();

                    if (!JadeStringUtil.isEmpty(user.getData())) {
                        userLevelSecurity = user.getData();
                    } else {
                        // si le complément n'existe pas on lui met un niveau à
                        // 0 (aucun droit)
                        userLevelSecurity = "0";
                    }

                    String colonneForWhere = "";// paramètre sur lequel on
                    // effectue le where (KIIND=idCi
                    // ou KCID=idJournal)
                    String valeurForWhere = "";// valeur associée au where

                    if (!JadeStringUtil.isEmpty(getForCompteIndividuelId())) {
                        colonneForWhere = "KAIIND";
                        valeurForWhere = getForCompteIndividuelId();
                    } else if (!JadeStringUtil.isEmpty(getForIdJournal())) {
                        colonneForWhere = "KCID";
                        valeurForWhere = getForIdJournal();
                    } else {
                        throw new Exception("La colonne doit être spécifié");
                    }

                    // recherche des écritures où l'affilié est protégé avec un
                    // niveau supérieur à celui du securecode du user
                    transactionEcriture = new BTransaction(getSession());
                    transactionEcriture.openTransaction();
                    BPreparedStatement psChercheEcritureACacher = new BPreparedStatement(transactionEcriture);
                    psChercheEcritureACacher.prepareStatement(CIEcritureManager
                            .getSqlChercheEcritureACacher(colonneForWhere));
                    psChercheEcritureACacher.clearParameters();
                    psChercheEcritureACacher.setBigDecimal(1, new BigDecimal(valeurForWhere));
                    psChercheEcritureACacher.setBigDecimal(2, new BigDecimal(CIEcritureManager.NIV_SECURITY_X
                            + userLevelSecurity));
                    ResultSet result = psChercheEcritureACacher.executeQuery();
                    if (result.next()) {
                        // si il en existe on indique qu'il faut parcourir les
                        // écritures dans le after_find et les cacher
                        existEcritureACacher = true;
                        wantCallMethodAfterFind(true);
                        // System.out.println("Des écritures ont été cachées");

                        // on récupère l'id de l'écriture concernée
                        ecritureACacher.add(result.getBigDecimal("KBIECR").toString());
                        while (result.next()) {
                            ecritureACacher.add(result.getBigDecimal("KBIECR").toString());
                        }

                    } else {
                        // System.out.println("Aucune écriture n'a été cachée");
                    }

                    // vérifie si il existe des écritures de genre 8 et de texte
                    // 0 et si le conjoint a des écritures protégées (afin de
                    // cacher les écritures protégées du conjoint)
                    BPreparedStatement psChercheEcritureGenre8ACacher = new BPreparedStatement(transactionEcriture);
                    psChercheEcritureGenre8ACacher.prepareStatement(CIEcritureManager
                            .getSqlChercheEcritureGenre8ACacher(colonneForWhere));
                    psChercheEcritureGenre8ACacher.clearParameters();
                    psChercheEcritureGenre8ACacher.setBigDecimal(1, new BigDecimal(valeurForWhere));
                    psChercheEcritureGenre8ACacher.setBigDecimal(2, new BigDecimal(CIEcritureManager.NIV_SECURITY_X
                            + userLevelSecurity));
                    ResultSet resultGenre8 = psChercheEcritureGenre8ACacher.executeQuery();
                    if (resultGenre8.next()) {
                        // si il en existe on cache les écritures de genre 8
                        existEcritureGenre8ACacher = true;
                        wantCallMethodAfterFind(true);
                        // System.out.println("Des écritures de genre 8 ont été cachées");
                    } else {
                        // System.out.println("Aucune écriture de genre 8 n'a été cachée");
                    }

                } catch (Exception e) {
                    transactionEcriture.rollback();
                    System.out.println(e.getMessage());
                } finally {
                    if (transactionEcriture != null) {
                        transactionEcriture.closeTransaction();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // si la vérification des écritures en suspens protégée par affiliation
        // est activée on passe dans le afterfind()
        if (cacherEcritureEnSuspensProtege == 1) {
            wantCallMethodAfterFind(true);
        }
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     * 
     * @param BStatement
     *            le statement
     * @return String le ORDER BY
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return order;
    }

    /*
     * protected java.lang.String _getSql(BStatement statement) { try { StringBuffer sqlBuffer = new
     * StringBuffer("SELECT "); String sqlFields = _getFields(statement); if ((sqlFields != null) &&
     * (sqlFields.trim().length() != 0)) { sqlBuffer.append(sqlFields); } else { sqlBuffer.append("*"); }
     * sqlBuffer.append(" FROM "); sqlBuffer.append(_getFrom(statement)); // String sqlWhere = _getWhere(statement); if
     * ((sqlWhere != null) && (sqlWhere.trim().length() != 0)) { sqlBuffer.append(" WHERE ");
     * sqlBuffer.append(sqlWhere); } String sqlOrder = _getOrder(statement); if ((sqlOrder != null) &&
     * (sqlOrder.trim().length() != 0)) { sqlBuffer.append(" ORDER BY "); sqlBuffer.append(sqlOrder); }
     * /*if(getContainerSize()<100) { sqlBuffer.append(" fetch first "+getContainerSize()+" rows only"); }
     */
    // sqlBuffer.append(" fetch first "+(getContainerSize() != Integer.MAX_VALUE
    // ? getContainerSize() : 1000)+" rows only");
    /*
     * return sqlBuffer.toString(); } catch (Exception e) { JadeLogger.warn(this, "ERROR IN FUNCTION _getSql() (" +
     * e.toString() + ")"); return ""; } }
     */
    /**
     * retourne la clause WHERE de la requete SQL
     * 
     * @param BStatement
     *            le statement
     * @return la clause WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        CIApplication ciApp = null;
        try {
            ciApp = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean needToIgnoreCopy = true;
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        // traitement du positionnement
        if (getForCompteIndividuelId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KAIIND="
                    + this._dbWriteNumeric(statement.getTransaction(), getForCompteIndividuelId());
        }
        // traitement du positionnement
        if (getForRassemblementOuvertureId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KKIRAO="
                    + this._dbWriteNumeric(statement.getTransaction(), getForRassemblementOuvertureId());
        }
        // traitement du positionnement
        if (getForIdIn().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBIECR in ( " + getForIdIn() + " ) ";

        }
        if (getForIdJournalIn().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KCID in ( " + getForIdJournalIn() + " ) ";

        }
        if (getForNotRassemblementOuvertureId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KKIRAO<>"
                    + this._dbWriteNumeric(statement.getTransaction(), getForNotRassemblementOuvertureId());
        }
        // traitement du positionnement
        if (getForEmployeur().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBITIE="
                    + this._dbWriteNumeric(statement.getTransaction(), getForEmployeur());
        }
        // traitement du positionnement
        if (getFromAffilie().length() != 0) {

            if ((fromAffilie != null) && (fromAffilie.indexOf('.') == -1)) {
                try {
                    IFormatData affilieFormater = ciApp.getAffileFormater();
                    if (affilieFormater != null) {
                        fromAffilie = affilieFormater.format(fromAffilie);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            String chaineAtraiter = new String();
            CIAffilieManager affMgr = new CIAffilieManager();
            affMgr.setSession(getSession());
            affMgr.setLikeAffilieNumero(getFromAffilie());

            try {
                affMgr.find();
            } catch (Exception e) {
            }
            for (int i = 0; i < affMgr.size(); i++) {
                if (JadeStringUtil.isBlank(chaineAtraiter)) {
                    chaineAtraiter = chaineAtraiter + ((CIAffilie) affMgr.getEntity(i)).getAffiliationId();
                } else {
                    chaineAtraiter = chaineAtraiter + "," + " " + ((CIAffilie) affMgr.getEntity(i)).getAffiliationId();
                }
            }
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + "AFAFFIP.MALNAF>="
                    + this._dbWriteString(statement.getTransaction(), getFromAffilie());
        }
        if (getLikeNomPrenom().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIINDIP.KALNOM like "
                    + this._dbWriteString(statement.getTransaction(), getLikeNomPrenom() + "%");
        }
        if (getLikeNomPrenomPartiel().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            int pos = getLikeNomPrenomPartiel().indexOf(',');
            if (pos == -1) {
                sqlWhere += _getCollection() + "CIINDIP.KALNOM like "
                        + this._dbWriteString(statement.getTransaction(), getLikeNomPrenomPartiel() + "%");
            } else {
                sqlWhere += _getCollection()
                        + "CIINDIP.KALNOM like "
                        + this._dbWriteString(statement.getTransaction(), getLikeNomPrenomPartiel().substring(0, pos)
                                + "%," + getLikeNomPrenomPartiel().substring(pos + 1) + "%");
            }
        }
        if (getForMontant().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBMMON = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForMontant());
        }
        if (getFromPartenaire().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CIINDIPPA.KANAVS LIKE "
                    + this._dbWriteString(statement.getTransaction(), CIUtil.unFormatAVS(getFromPartenaire()) + "%");
        }

        // traitement du positionnement
        if (getForAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFAFFIP.MALNAF="
                    + this._dbWriteString(statement.getTransaction(), getForAffilie());
        }
        //
        if (getForNotId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += getCollection() + "CIECRIP.kbiecr <> "
                    + this._dbWriteNumeric(statement.getTransaction(), getForNotId());
        }

        // traitement du positionnement
        if (getFromEmployeur().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFAFFIP.MALNAF LIKE "
                    + this._dbWriteString(statement.getTransaction(), getFromEmployeur() + "%");
        }
        // traitement du positionnement
        if (getForIdJournal().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KCID="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdJournal());
        }
        // traitement du positionnement
        if (getForAnnee().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBNANN="
                    + this._dbWriteNumeric(statement.getTransaction(), getForAnnee());
        }
        // traitement du genre
        if (getForGenre().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";

            }
            sqlWhere += _getCollection() + "CIECRIP.KBTGEN = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForGenre());

        }
        if (getForNotGenre().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";

            }
            sqlWhere += _getCollection() + "CIECRIP.KBTGEN <> "
                    + this._dbWriteNumeric(statement.getTransaction(), getForNotGenre());

        }
        if (getForMoisDebut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";

            }
            sqlWhere += _getCollection() + "CIECRIP.KBNMOD = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForMoisDebut());

        }

        if (getForMoisDebut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";

            }
            sqlWhere += _getCollection() + "CIECRIP.KBNMOF = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForMoisFin());

        }
        if (isExclureRA()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIINDIP.KAIREG = " + CICompteIndividuel.CS_REGISTRE_PROVISOIRE;

        }

        // traitement extourne
        if (getForExtourne().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBTEXT = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForExtourne());
        }
        // traitement extourne
        if (getForNotExtourne().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBTEXT <> "
                    + this._dbWriteNumeric(statement.getTransaction(), getForNotExtourne());
        }
        if (getForMasseSalariale().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBTCPT <> " + CIEcriture.CS_CI_SUSPENS_SUPPRIMES + " AND "
                    + _getCollection() + "CIECRIP.KBTCPT <> " + CIEcriture.CS_TEMPORAIRE + " AND " + _getCollection()
                    + "CIECRIP.KBTCPT <> " + CIEcriture.CS_TEMPORAIRE_SUSPENS + " AND " + _getCollection()
                    + "CIECRIP.KBTCPT <> " + CIEcriture.CS_CORRECTION;

        }

        // traitement du positionnement
        if (getForIdTypeCompte().length() != 0) {
            needToIgnoreCopy = false;
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            if (adjustType
                    && (CIEcriture.CS_TEMPORAIRE.equals(getForIdTypeCompte()) || CIEcriture.CS_TEMPORAIRE_SUSPENS
                            .equals(getForIdTypeCompte()))) {

                sqlWhere += " (" + _getCollection() + "CIECRIP.KBTCPT="
                        + this._dbWriteNumeric(statement.getTransaction(), CIEcriture.CS_TEMPORAIRE) + " OR "
                        + _getCollection() + "CIECRIP.KBTCPT="
                        + this._dbWriteNumeric(statement.getTransaction(), CIEcriture.CS_TEMPORAIRE_SUSPENS) + " ) ";
            } else {
                sqlWhere += _getCollection() + "CIECRIP.KBTCPT="
                        + this._dbWriteNumeric(statement.getTransaction(), getForIdTypeCompte());
            }
        }
        // traitement du positionnement
        if (getFromIdTypeCompte().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBTCPT>="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdTypeCompte());
        }
        // traitement liste de type
        if (getForIdTypeCompteList() != null) {
            needToIgnoreCopy = false;
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            for (int i = 0; i < getForIdTypeCompteList().length; i++) {
                if (i != 0) {
                    sqlWhere += " OR ";
                } else {
                    sqlWhere += "( ";
                }
                sqlWhere += _getCollection() + "CIECRIP.KBTCPT="
                        + this._dbWriteNumeric(statement.getTransaction(), getForIdTypeCompteList()[i]);
            }
            sqlWhere += " ) ";
        }
        // traitement liste de ci
        if (getForCompteIndividuelIdList() != null) {
            needToIgnoreCopy = false;
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KAIIND IN (";
            for (int i = 0; i < getForCompteIndividuelIdList().length; i++) {
                if (i != 0) {
                    sqlWhere += ",";
                }
                sqlWhere += this
                        ._dbWriteNumeric(statement.getTransaction(), (String) getForCompteIndividuelIdList()[i]);
            }
            sqlWhere += ") ";
        }
        // traitement du positionnement
        if (getFromAvs().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIINDIP.KANAVS like '"
                    + this._dbWriteNumeric(statement.getTransaction(), NSUtil.unFormatAVS(getFromAvs()));
            sqlWhere += "%'";
        }
        // traitement du positionnement
        if (getForDateNaissanceNumeric().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIINDIP.KADNAI = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForDateNaissanceNumeric());

        }
        if (getFromAvs().length() != 0) {
            if ("true".equalsIgnoreCase(getFromAvsNNSS())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += _getCollection() + "CIINDIP.KABNNS ='1'";
            } else if ("false".equalsIgnoreCase(getFromAvsNNSS())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += _getCollection() + "CIINDIP.KABNNS ='2'";
            }
        }
        if (getFromNumeroAvsSuspenes().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIINDIP.KANAVS like '"
                    + this._dbWriteNumeric(statement.getTransaction(), getFromNumeroAvsSuspenes());
            sqlWhere += "%'";
        }
        if (!JadeStringUtil.isBlankOrZero(getFromNumeroAvsSuspenes())) {
            if ("true".equalsIgnoreCase(getFromNumeroAvsSuspenesNNSS().trim())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += _getCollection() + "CIINDIP.KABNNS ='1'";
            }
            if ("false".equalsIgnoreCase(getFromNumeroAvsSuspenesNNSS().trim())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += _getCollection() + "CIINDIP.KABNNS ='2'";
            }
        }
        // traitement du positionnement
        if (getMontantQuery().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += getMontantQuery();
        }
        // traitement du positionnement

        if (getUntilMoisDebut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBNMOD<="
                    + this._dbWriteNumeric(statement.getTransaction(), getUntilMoisDebut());
        }
        if (getUntilGeMoisFin().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBNMOF>="
                    + this._dbWriteNumeric(statement.getTransaction(), getUntilGeMoisFin());
        }
        if (getFromNumeroAvs().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIINDIP.KANAVS>='"
                    + this._dbWriteNumeric(statement.getTransaction(), fillAvsWith(getFromNumeroAvs(), "0")) + "'";
        }
        if (getUntilNumeroAvs().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIINDIP.KANAVS<='"
                    + this._dbWriteNumeric(statement.getTransaction(), fillAvsWith(getUntilNumeroAvs(), "9")) + "'";
        }
        if (getFromIdJournal().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KCID>="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdJournal());
        }
        if (getUntilIdJournal().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KCID<="
                    + this._dbWriteNumeric(statement.getTransaction(), getUntilIdJournal());
        }
        if (getFromAnnee().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBNANN>="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromAnnee());
        }
        if (getUntilAnnee().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBNANN<="
                    + this._dbWriteNumeric(statement.getTransaction(), getUntilAnnee());
        }
        if (getFromDateInscription().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBLESP>='"
                    + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateInscription()) + "'";
        }
        if (getUntilDateInscription().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBLESP<='"
                    + this._dbWriteDateAMJ(statement.getTransaction(), getUntilDateInscription()) + "'";
        }
        if (getForCode().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBTCOD = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForCode());

        }
        // type compte CA, genre 6 et 7
        if (getForIdTypeCompteCompta().length() != 0) {
            needToIgnoreCopy = false;
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBTCPT IN (" + CIEcriture.CS_CI + "," + CIEcriture.CS_GENRE_6 + ","
                    + CIEcriture.CS_GENRE_7 + ")";
        }
        if (getForNotCompense().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBTCOD not in (" + getForNotCompense() + ")";
        }
        if (needToIgnoreCopy) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBTCPT<>" + CIEcriture.CS_CORRECTION;
        }
        if (getForDateAnnonceCentrale().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBDCEN="
                    + this._dbWriteNumeric(statement.getTransaction(), getForDateAnnonceCentrale());
        }
        if (getForNotSuspensSup().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBTCPT<>" + CIEcriture.CS_CI_SUSPENS_SUPPRIMES;

        }
        if (getForIdTiers().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "TITIERP.HTITIE="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdTiers());
        }
        if (getForGenreIn().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "KBTGEN IN ("
                    + this._dbWriteNumeric(statement.getTransaction(), getForGenreIn()) + ")";
        }
        if (getNbeRowsFetchFirst() > 0) {
            sqlWhere += " FETCH FIRST " + getNbeRowsFetchFirst() + " ROWS ONLY";
        }
        if (isCertificat()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND KBBIMP ='1' ";
            } else {
                sqlWhere += " KBBIMP ='1' ";
            }

        }
        if (isEcrituresNonRA()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ((LENGTH(RTRIM(" + getCollection() + "CIINDIP.KANAVS)) >=8 AND " + getCollection()
                        + "CIINDIP.KALNOM <>'')OR(LENGTH(RTRIM(" + getCollection() + "CIINDIP.KANAVS)) >=8 AND "
                        + getCollection() + "CIINDIP.KALNOM ='')OR(LENGTH(RTRIM(" + getCollection()
                        + "CIINDIP.KANAVS)) <8 AND " + getCollection() + "CIINDIP.KALNOM <>''))";

            } else {

                sqlWhere += "((LENGTH(RTRIM(" + getCollection() + "CIINDIP.KANAVS)) >=8 AND " + getCollection()
                        + "CIINDIP.KALNOM <>'')OR(LENGTH(RTRIM(" + getCollection() + "CIINDIP.KANAVS)) >=8 AND "
                        + getCollection() + "CIINDIP.KALNOM ='')OR(LENGTH(RTRIM(" + getCollection()
                        + "CIINDIP.KANAVS)) <8 AND " + getCollection() + "CIINDIP.KALNOM <>''))";
            }
        }
        if (getForCategoriePersonnel().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " KBTCAT = " + this._dbWriteNumeric(statement.getTransaction(), getForCategoriePersonnel());

        }
        if ((forGenreCotPers != null) && forGenreCotPers.equals("true")) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " (KBTGEN IN (" + CIEcriture.CS_CIGENRE_3 + ", " + CIEcriture.CS_CIGENRE_4 + ", "
                    + CIEcriture.CS_CIGENRE_9 + ", " + CIEcriture.CS_CIGENRE_2 + ")";
            sqlWhere += " OR KBTGEN =  " + CIEcriture.CS_CIGENRE_7 + " and KBTSPE IN (312002,312001,0))";

        }
        if ((forRee != null) && forRee.equals("true")) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " (KBTGEN IN (" + CIEcriture.CS_CIGENRE_1 + "," + CIEcriture.CS_CIGENRE_3 + ", "
                    + CIEcriture.CS_CIGENRE_9 + ")";
            sqlWhere += " OR (KBTGEN =  " + CIEcriture.CS_CIGENRE_7 + " and KBTSPE IN (312002,312003)))";
            sqlWhere += " AND KBTCPT IN (303001,303002,303004) ";

        }

        return sqlWhere;
    }

    @Override
    public final BEntity _newEntity() {
        return new CIEcriture();
    }

    /**
     * Insère le chiffre donné en paramètre à la fin du numéro avs afin de le compléter (longueur de 11).<br>
     * Remarque: utilisé pour les requêtes SQL WHERE Date de création : (08.01.2003 16:05:10)
     * 
     * @return le numéro avs complété
     * @param avs
     *            le numéro avs à compléter
     * @param number
     *            le chiffre à insérer (0 pour une comparaison >= et 9 pour <=)
     */
    public String fillAvsWith(String avs, String number) {
        int longueur = avs.length();
        if (longueur == 11) {
            return avs;
        }
        StringBuffer buf = new StringBuffer(avs);
        for (int i = longueur; i <= 10; i++) {
            buf.append(number);
        }
        return buf.toString();
    }

    /**
     * prend une liste en entrée et sépare les datas par des "," exemple : 1,2,3
     * 
     * @param Liste
     * @return String
     */
    protected String formatEcrituresCacheesForNotIn(ArrayList liste) {
        String ecritureCachees = "";
        for (int i = 0; i < liste.size(); i++) {
            ecritureCachees += liste.get(i).toString();
            if (i < liste.size() - 1) {
                ecritureCachees += ",";
            }
        }
        return ecritureCachees;
    }

    /**
     * @param ecriture
     * @return l'affiliation de l'écriture (employeur ou partenaire si de genre 8)
     * @throws Exception
     */
    private AFAffiliation getAffiliationEcriture(CIEcriture ecriture) throws Exception {
        AFAffiliation affiliation = new AFAffiliation();
        affiliation.setSession(getSession());
        if (ecriture.getGenreEcriture().equals(CIEcriture.CS_CIGENRE_8)) {
            affiliation.setAffiliationId(ecriture.getPartenaireId());
        } else {
            affiliation.setAffiliationId(ecriture.getEmployeur());
        }
        affiliation.retrieve();

        return affiliation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 07:58:17)
     * 
     * @return java.lang.String
     */
    public java.lang.String getApartir() {
        return apartir;
    }

    public int getCacherEcritureEnSuspensProtege() {
        return cacherEcritureEnSuspensProtege;
    }

    public int getCacherEcritureProtege() {
        return cacherEcritureProtege;
    }

    /**
     * Retourne la collection de la BD. Date de création : (24.01.2003 15:28:55)
     * 
     * @return la collection de la BD
     */
    public String getCollection() {
        return _getCollection();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.01.2003 11:16:03)
     * 
     * @return java.lang.String
     */
    public java.lang.String getEtatEcritures() {
        return etatEcritures;
    }

    /**
     * Returns the forAffilie.
     * 
     * @return String
     */
    public String getForAffilie() {
        return forAffilie;
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForCategoriePersonnel() {
        return forCategoriePersonnel;
    }

    /**
     * Returns the forCode.
     * 
     * @return String
     */
    public String getForCode() {
        return forCode;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForCompteIndividuelId() {
        return forCompteIndividuelId;
    }

    /**
     * Returns the forCompteIndividuelList.
     * 
     * @return String[]
     */
    public Object[] getForCompteIndividuelIdList() {
        return forCompteIndividuelIdList;
    }

    /**
     * Returns the forDateAnnonceCentrale.
     * 
     * @return String
     */
    public String getForDateAnnonceCentrale() {
        return forDateAnnonceCentrale;
    }

    public String getForDateNaissanceNumeric() {
        return forDateNaissanceNumeric;
    }

    public String getForEmployeur() {
        return forEmployeur;
    }

    /**
     * Returns the forExtourne.
     * 
     * @return String
     */
    public String getForExtourne() {
        return forExtourne;
    }

    /**
     * Returns the forGenre.
     * 
     * @return String
     */
    public String getForGenre() {
        return forGenre;
    }

    public String getForGenreCotPers() {
        return forGenreCotPers;
    }

    public String getForGenreIn() {
        return forGenreIn;
    }

    /**
     * Returns the forIdIn.
     * 
     * @return String
     */
    public String getForIdIn() {
        return forIdIn;
    }

    public String getForIdJournal() {
        return forIdJournal;
    }

    public String getForIdJournalIn() {
        return forIdJournalIn;
    }

    /**
     * @return
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForIdTypeCompte() {
        return forIdTypeCompte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.01.2003 10:30:14)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdTypeCompteCompta() {
        return forIdTypeCompteCompta;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 10:48:17)
     * 
     * @return java.lang.String
     */
    public java.lang.String[] getForIdTypeCompteList() {
        return forIdTypeCompteList;
    }

    /**
     * Returns the forMasseSalariale.
     * 
     * @return String
     */
    public String getForMasseSalariale() {
        return forMasseSalariale;
    }

    /**
     * Returns the forMoisDebut.
     * 
     * @return String
     */
    public String getForMoisDebut() {
        return forMoisDebut;
    }

    /**
     * Returns the forMoisFin.
     * 
     * @return String
     */
    public String getForMoisFin() {
        return forMoisFin;
    }

    /**
     * Returns the forMontant.
     * 
     * @return String
     */
    public String getForMontant() {
        return forMontant;
    }

    /**
     * Returns the forNotCompense.
     * 
     * @return String
     */
    public String getForNotCompense() {
        return forNotCompense;
    }

    /**
     * Returns the forNotExtourne.
     * 
     * @return String
     */
    public String getForNotExtourne() {
        return forNotExtourne;
    }

    /**
     * @return
     */
    public String getForNotGenre() {
        return forNotGenre;
    }

    /**
     * Returns the forNotId.
     * 
     * @return String
     */
    public String getForNotId() {
        return forNotId;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.12.2002 12:28:30)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNotIdTypeCompte() {
        return forNotIdTypeCompte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.01.2003 15:05:51)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNotRassemblementOuvertureId() {
        return forNotRassemblementOuvertureId;
    }

    /**
     * @return
     */
    public String getForNotSuspensSup() {
        return forNotSuspensSup;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.12.2002 13:26:53)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForRassemblementOuvertureId() {
        return forRassemblementOuvertureId;
    }

    /**
     * @return the forRee
     */
    public String getForRee() {
        return forRee;
    }

    /**
     * Returns the fromAffilie.
     * 
     * @return String
     */
    public String getFromAffilie() {
        return fromAffilie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.01.2003 15:58:12)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromAnnee() {
        return fromAnnee;
    }

    public String getFromAvs() {
        return fromAvs;
    }

    /**
     * @return
     */
    public String getFromAvsNNSS() {
        return fromAvsNNSS;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.01.2003 15:58:12)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromDateInscription() {
        return fromDateInscription;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 07:38:36)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromEmployeur() {
        return fromEmployeur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.01.2003 15:58:12)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromIdJournal() {
        return fromIdJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 07:38:36)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromIdTypeCompte() {
        return fromIdTypeCompte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.01.2003 15:58:12)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromNumeroAvs() {
        return fromNumeroAvs;
    }

    /**
     * @return
     */
    public String getFromNumeroAvsSuspenes() {
        return fromNumeroAvsSuspenes;
    }

    /**
     * @return
     */
    public String getFromNumeroAvsSuspenesNNSS() {
        return fromNumeroAvsSuspenesNNSS;
    }

    /**
     * @return
     */
    public String getFromPartenaire() {
        return fromPartenaire;
    }

    /**
     * @return
     */
    public String getLikeNomPrenom() {
        return likeNomPrenom;
    }

    /**
     * @return
     */
    public String getLikeNomPrenomPartiel() {
        return likeNomPrenomPartiel;
    }

    public String getMontantQuery() {
        String query = montantQuery;
        if (!JadeStringUtil.isBlank(query)) {
            String valueFrom = new String();
            String valueTo = new String();
            int separatorIndex = query.indexOf(",");
            int start = query.indexOf("[");
            int startStrict = query.indexOf("]");
            if ((start >= 0) && (startStrict >= 0)) {
                if (start < startStrict) {
                    valueFrom = query.substring(start + 1, separatorIndex);
                    valueTo = query.substring(separatorIndex + 1, startStrict);
                    query = _getCollection() + "CIECRIP.KBMMON >= " + valueFrom + " AND " + _getCollection()
                            + "CIECRIP.KBMMON <= " + valueTo;
                } else {
                    valueFrom = query.substring(startStrict + 1, separatorIndex);
                    valueTo = query.substring(separatorIndex + 1, start);
                    query = _getCollection() + "CIECRIP.KBMMON > " + valueFrom + " AND " + _getCollection()
                            + "CIECRIP.KBMMON < " + valueTo;
                }
            } else if ((start < 0) && (startStrict >= 0)) {
                valueFrom = query.substring(startStrict + 1, separatorIndex);
                startStrict = query.indexOf("]", startStrict + 1);
                valueTo = query.substring(separatorIndex + 1, startStrict);
                query = _getCollection() + "CIECRIP.KBMMON > " + valueFrom + " AND " + _getCollection()
                        + "CIECRIP.KBMMON <= " + valueTo;
            } else if ((start >= 0) && (startStrict < 0)) {
                valueFrom = query.substring(start + 1, separatorIndex);
                start = query.indexOf("[", start + 1);
                valueTo = query.substring(separatorIndex + 1, start);
                query = _getCollection() + "CIECRIP.KBMMON >= " + valueFrom + " AND " + _getCollection()
                        + "CIECRIP.KBMMON < " + valueTo;
            } else if ((start < 0) && (startStrict < 0)) {
                query = _getCollection() + "CIECRIP.KBMMON = " + query;
            }
        }
        return query;
    }

    /**
     * Returns the nbeRowsFetchFirst.
     * 
     * @return int
     * @deprecated utiliser getContainerSize()
     */
    @Deprecated
    public int getNbeRowsFetchFirst() {
        return nbeRowsFetchFirst;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.01.2003 09:51:17)
     * 
     * @return java.lang.String
     */
    public java.lang.String getOrder() {
        return order;
    }

    public String getSqlForUpdateDateCentrale() {
        try {
            // update sans BETWEEN car je veux qu'il inclut les deux bornes
            StringBuffer sql = new StringBuffer("UPDATE ");
            sql.append(_getCollection());
            sql.append("CIECRIP SET KBDCEN=? ");
            sql.append("WHERE KBIECR = ? ");
            return sql.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Retourne le total des montants des inscription liées au ci.<br>
     * Le champs <tt>forCompteIndividuelId</tt> doit être présente dans le manager appelé Date de création : (05.02.2003
     * 13:43:09)
     * 
     * @return le total des montants
     */
    public String getSumOfCI() {
        if (!CIEcriture.CS_CI.equals(forIdTypeCompte) && !JadeStringUtil.isBlank(forIdTypeCompte)) { // ||
            // !"0".equals(forRassemblementOuvertureId))
            // {
            return null;
        }
        try {
            CICompteIndividuel ci = new CICompteIndividuel();
            ci.setSession(getSession());
            ci.setCompteIndividuelId(getForCompteIndividuelId());
            ci.retrieve(null);
            if (ci.isNew() || !ci.hasUserShowRight(null)) {
                return null; // "("+getSession().getLabel("MSG_ECRITURE_CACHE")+")";
            }
            CIEcrituresSomme somme = new CIEcrituresSomme();
            somme.setSession(getSession());
            somme.setForCompteIndividuelId(getForCompteIndividuelId());
            somme.setEtatEcritures(getEtatEcritures());
            if (listeEcrituresCachees.size() != 0) {
                somme.setForNotEcriture(formatEcrituresCacheesForNotIn(listeEcrituresCachees));
            }
            BigDecimal result = somme.getSum("KBMMON", null);
            return JANumberFormatter.format(result.toString());
        } catch (Exception ex) {
            return "0.00";
        }
    }

    public String getSumOfCISansCts() {
        if (!CIEcriture.CS_CI.equals(forIdTypeCompte) && !JadeStringUtil.isBlank(forIdTypeCompte)) { // ||
            // !"0".equals(forRassemblementOuvertureId))
            // {
            return null;
        }
        try {
            CICompteIndividuel ci = new CICompteIndividuel();
            ci.setSession(getSession());
            ci.setCompteIndividuelId(getForCompteIndividuelId());
            ci.retrieve(null);
            if (ci.isNew() || !ci.hasUserShowRight(null)) {
                return null; // "("+getSession().getLabel("MSG_ECRITURE_CACHE")+")";
            }
            CIEcrituresSomme somme = new CIEcrituresSomme();
            somme.setSession(getSession());
            somme.setForCompteIndividuelId(getForCompteIndividuelId());
            somme.setEtatEcritures(getEtatEcritures());
            if (listeEcrituresCachees.size() != 0) {
                somme.setForNotEcriture(formatEcrituresCacheesForNotIn(listeEcrituresCachees));
            }
            BigDecimal result = somme.getSum(("TRUNCATE(KBMMON,0)"), null);
            return JANumberFormatter.format(result.toString());
        } catch (Exception ex) {
            return "0.00";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 07:58:17)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTri() {
        return tri;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.01.2003 15:58:12)
     * 
     * @return java.lang.String
     */
    public java.lang.String getUntilAnnee() {
        return untilAnnee;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.01.2003 15:58:12)
     * 
     * @return java.lang.String
     */
    public java.lang.String getUntilDateInscription() {
        return untilDateInscription;
    }

    /**
     * @return
     */
    public String getUntilGeMoisFin() {
        return untilGeMoisFin;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.01.2003 15:58:12)
     * 
     * @return java.lang.String
     */
    public java.lang.String getUntilIdJournal() {
        return untilIdJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.12.2002 08:50:30)
     * 
     * @return java.lang.String
     */
    public java.lang.String getUntilMoisDebut() {
        return untilMoisDebut;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.01.2003 15:58:12)
     * 
     * @return java.lang.String
     */
    public java.lang.String getUntilNumeroAvs() {
        return untilNumeroAvs;
    }

    public boolean hasEcritureProtegeParAffiliation() {
        if (existEcritureACacher || existEcritureGenre8ACacher) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the adjustType.
     * 
     * @return boolean
     */
    public boolean isAdjustType() {
        return adjustType;
    }

    /**
     * @return
     */
    public boolean isCertificat() {
        return certificat;
    }

    /**
     * @return
     */
    public boolean isEcrituresNonRA() {
        return ecrituresNonRA;
    }

    /**
     * @return
     */
    public boolean isExclureRA() {
        return exclureRA;
    }

    public boolean isExistEcritureACacher() {
        return existEcritureACacher;
    }

    public boolean isExistEcritureGenre8ACacher() {
        return existEcritureGenre8ACacher;
    }

    /**
     * Modifie la clause ORDER BY pour un tri par année. Date de création : (06.01.2003 09:52:42)
     */
    public void orderByAnnee() {
        order = "KBNANN";
    }

    /**
     * Sets the adjustType.
     * 
     * @param adjustType
     *            The adjustType to set
     */
    public void setAdjustType(boolean adjustType) {
        this.adjustType = adjustType;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 07:58:17)
     * 
     * @param newApartir
     *            java.lang.String
     */
    public void setApartir(java.lang.String newApartir) {
        apartir = newApartir;
    }

    public void setCacherEcritureEnSuspensProtege(int cacherEcritureEnSuspensProtege) {
        this.cacherEcritureEnSuspensProtege = cacherEcritureEnSuspensProtege;
    }

    /**
     * Permet d'activer ou de désactiver la fonction permettant de cacher les écritures dont l'affiliation est protégée
     * (1=activer 0=désactivé). Par défaut la fonction est désactivée
     * 
     * ATTENTION : pour fonctionner l'id du compte individuelle ou l'id du journal CI doit également être setter au
     * manager
     * 
     * @param cacherEcritureProtege
     *            (1 ou 0)
     */
    public void setCacherEcritureProtege(int cacherEcritureProtege) {
        this.cacherEcritureProtege = cacherEcritureProtege;
    }

    /**
     * @param b
     */
    public void setCertificat(boolean b) {
        certificat = b;
    }

    /**
     * @param b
     */
    public void setEcrituresNonRA(boolean b) {
        ecrituresNonRA = b;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.01.2003 11:16:03)
     * 
     * @param newEtatEcritures
     *            java.lang.String
     */
    public void setEtatEcritures(java.lang.String newEtatEcritures) {
        etatEcritures = newEtatEcritures;
    }

    /**
     * @param b
     */
    public void setExclureRA(boolean b) {
        exclureRA = b;
    }

    public void setExistEcritureACacher(boolean existEcritureACacher) {
        this.existEcritureACacher = existEcritureACacher;
    }

    public void setExistEcritureGenre8ACacher(boolean existEcritureGenre8ACacher) {
        this.existEcritureGenre8ACacher = existEcritureGenre8ACacher;
    }

    /**
     * Sets the forAffilie.
     * 
     * @param forAffilie
     *            The forAffilie to set
     */
    public void setForAffilie(String forAffilie) {
        this.forAffilie = forAffilie;
    }

    public void setForAnnee(String newForAnnee) {
        forAnnee = newForAnnee;
    }

    public void setForCategoriePersonnel(String forCategoriePersonnel) {
        this.forCategoriePersonnel = forCategoriePersonnel;
    }

    /**
     * Sets the forCode.
     * 
     * @param forCode
     *            The forCode to set
     */
    public void setForCode(String forCode) {
        this.forCode = forCode;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */

    public void setForCompteIndividuelId(String newForCompteIndividuelId) {
        forCompteIndividuelId = newForCompteIndividuelId;
    }

    /**
     * Sets the forCompteIndividuelList.
     * 
     * @param forCompteIndividuelList
     *            The forCompteIndividuelList to set
     */
    public void setForCompteIndividuelIdList(Object[] forCompteIndividuelIdList) {
        this.forCompteIndividuelIdList = forCompteIndividuelIdList;
    }

    /**
     * Sets the forDateAnnonceCentrale.
     * 
     * @param forDateAnnonceCentrale
     *            The forDateAnnonceCentrale to set
     */
    public void setForDateAnnonceCentrale(String forDateAnnonceCentrale) {
        this.forDateAnnonceCentrale = forDateAnnonceCentrale;
    }

    public void setForDateNaissanceNumeric(String forDateNaissanceNumeric) {
        this.forDateNaissanceNumeric = forDateNaissanceNumeric;
    }

    public void setForEmployeur(String newForEmployeur) {
        forEmployeur = newForEmployeur;
    }

    /**
     * Sets the forExtourne.
     * 
     * @param forExtourne
     *            The forExtourne to set
     */
    public void setForExtourne(String forExtourne) {
        this.forExtourne = forExtourne;
    }

    /**
     * Sets the forGenre.
     * 
     * @param forGenre
     *            The forGenre to set
     */
    public void setForGenre(String forGenre) {
        this.forGenre = forGenre;
    }

    public void setForGenreCotPers(String forGenreCotPers) {
        this.forGenreCotPers = forGenreCotPers;
    }

    /**
     * @param string
     */
    public void setForGenreIn(String string) {
        forGenreIn = string;
    }

    /**
     * Sets the forIdIn.
     * 
     * @param forIdIn
     *            The forIdIn to set
     */
    public void setForIdIn(String forIdIn) {
        this.forIdIn = forIdIn;
    }

    public void setForIdJournal(String newForIdJournal) {
        forIdJournal = newForIdJournal;
    }

    public void setForIdJournalIn(String forIdJournalIn) {
        this.forIdJournalIn = forIdJournalIn;
    }

    /**
     * @param string
     */
    public void setForIdTiers(String string) {
        forIdTiers = string;
    }

    public void setForIdTypeCompte(String newForIdTypeCompte) {
        if (CIEcriture.CS_CI.equals(newForIdTypeCompte) && adjustType) {
            // change la requête pour inclure aussi les genres 6/7 + provisoires
            setForIdTypeCompteCompta("true");
        } else {
            forIdTypeCompte = newForIdTypeCompte;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.01.2003 10:30:14)
     * 
     * @param newForIdTypeCompteCompta
     *            java.lang.String
     */
    public void setForIdTypeCompteCompta(java.lang.String newForIdTypeCompteCompta) {
        forIdTypeCompteCompta = newForIdTypeCompteCompta;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 10:48:17)
     * 
     * @param newForIdTypeCompteList
     *            java.lang.String
     */
    public void setForIdTypeCompteList(java.lang.String[] newForIdTypeCompteList) {
        forIdTypeCompteList = newForIdTypeCompteList;
    }

    /**
     * Sets the forMasseSalariale.
     * 
     * @param forMasseSalariale
     *            The forMasseSalariale to set
     */
    public void setForMasseSalariale(String forMasseSalariale) {
        this.forMasseSalariale = forMasseSalariale;
    }

    /**
     * Sets the forMoisDebut.
     * 
     * @param forMoisDebut
     *            The forMoisDebut to set
     */
    public void setForMoisDebut(String forMoisDebut) {
        this.forMoisDebut = forMoisDebut;
    }

    /**
     * Sets the forMoisFin.
     * 
     * @param forMoisFin
     *            The forMoisFin to set
     */
    public void setForMoisFin(String forMoisFin) {
        this.forMoisFin = forMoisFin;
    }

    /**
     * Sets the forMontant.
     * 
     * @param forMontant
     *            The forMontant to set
     */
    public void setForMontant(String forMontant) {
        this.forMontant = forMontant;
    }

    /**
     * Sets the forNotCompense.
     * 
     * @param forNotCompense
     *            The forNotCompense to set
     */
    public void setForNotCompense(String forNotCompense) {
        this.forNotCompense = forNotCompense;
    }

    /**
     * Sets the forNotExtourne.
     * 
     * @param forNotExtourne
     *            The forNotExtourne to set
     */
    public void setForNotExtourne(String forNotExtourne) {
        this.forNotExtourne = forNotExtourne;
    }

    /**
     * @param string
     */
    public void setForNotGenre(String string) {
        forNotGenre = string;
    }

    /**
     * Sets the forNotId.
     * 
     * @param forNotId
     *            The forNotId to set
     */
    public void setForNotId(String forNotId) {
        this.forNotId = forNotId;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.12.2002 12:28:30)
     * 
     * @param newForNotIdTypeCompte
     *            java.lang.String
     */
    public void setForNotIdTypeCompte(java.lang.String newForNotIdTypeCompte) {
        forNotIdTypeCompte = newForNotIdTypeCompte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.01.2003 15:05:51)
     * 
     * @param newForCloture
     *            java.lang.String
     */
    public void setForNotRassemblementOuvertureId(java.lang.String newForCloture) {
        forNotRassemblementOuvertureId = newForCloture;
    }

    /**
     * @param string
     */
    public void setForNotSuspensSup(String string) {
        forNotSuspensSup = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.12.2002 13:26:53)
     * 
     * @param newForRassemblementOuvertureId
     *            java.lang.String
     */
    public void setForRassemblementOuvertureId(java.lang.String newForRassemblementOuvertureId) {
        forRassemblementOuvertureId = newForRassemblementOuvertureId;
    }

    /**
     * @param forRee
     *            the forRee to set
     */
    public void setForRee(String forRee) {
        this.forRee = forRee;
    }

    /**
     * Sets the fromAffilie.
     * 
     * @param forAffilie
     *            The fromAffilie to set
     */
    public void setFromAffilie(String fromAffilie) {
        this.fromAffilie = fromAffilie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.01.2003 15:58:12)
     * 
     * @param newFromAnnee
     *            java.lang.String
     */
    public void setFromAnnee(java.lang.String newFromAnnee) {
        fromAnnee = newFromAnnee;
    }

    public void setFromAvs(String newFromAvs) {
        fromAvs = newFromAvs;
    }

    /**
     * @param string
     */
    public void setFromAvsNNSS(String string) {
        fromAvsNNSS = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.01.2003 15:58:12)
     * 
     * @param newFromDateInscription
     *            java.lang.String
     */
    public void setFromDateInscription(java.lang.String newFromDateInscription) {
        fromDateInscription = newFromDateInscription;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 07:38:36)
     * 
     * @param newFromEmployeur
     *            java.lang.String
     */
    public void setFromEmployeur(java.lang.String newFromEmployeur) {
        fromEmployeur = newFromEmployeur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.01.2003 15:58:12)
     * 
     * @param newFromIdJournal
     *            java.lang.String
     */
    public void setFromIdJournal(java.lang.String newFromIdJournal) {
        fromIdJournal = newFromIdJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 07:38:36)
     * 
     * @param newFromIdTypeCompte
     *            java.lang.String
     */
    public void setFromIdTypeCompte(java.lang.String newFromIdTypeCompte) {
        fromIdTypeCompte = newFromIdTypeCompte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.01.2003 15:58:12)
     * 
     * @param newFromNumeroAvs
     *            java.lang.String
     */
    public void setFromNumeroAvs(java.lang.String newFromNumeroAvs) {
        fromNumeroAvs = newFromNumeroAvs;
    }

    /**
     * @param string
     */
    public void setFromNumeroAvsSuspenes(String string) {
        fromNumeroAvsSuspenes = CIUtil.unFormatAVS(string);
    }

    /**
     * @param string
     */
    public void setFromNumeroAvsSuspenesNNSS(String string) {
        fromNumeroAvsSuspenesNNSS = string;
    }

    /**
     * @param string
     */
    public void setFromPartenaire(String string) {
        fromPartenaire = string;
    }

    /**
     * @param string
     */
    public void setLikeNomPrenom(String string) {
        likeNomPrenom = string;
    }

    /**
     * @param string
     */
    public void setLikeNomPrenomPartiel(String string) {
        likeNomPrenomPartiel = string;
    }

    /**
     * Sets the nbeRowsFetchFirst.
     * 
     * @param nbeRowsFetchFirst
     *            The nbeRowsFetchFirst to set
     * @deprecated utiliser changeManagerSize()
     */
    @Deprecated
    public void setNbeRowsFetchFirst(int nbeRowsFetchFirst) {
        this.nbeRowsFetchFirst = nbeRowsFetchFirst;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.01.2003 09:51:17)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void setOrder(java.lang.String newOrder) {
        order = newOrder;
    }

    /**
     * Applique une nouvelle clause order by. Date de création : (21.10.2002 10:03:23)
     * 
     * @param order
     *            la nouvelle clause
     */
    public void setOrderBy(String order) {
        this.order = order;
    }

    public void setOrderByIdEcr() {
        order = _getCollection() + "CIECRIP.KBIECR";
    }

    /**
     * Query sur le revenu. Construit la requête en fonction de l'expression donnée. Date de création : (28.02.2003
     * 12:06:38)
     * 
     * @param query
     *            l'expression de la recherche sur le revenu.
     */
    public void setQueryRevenu(String query) {
        montantQuery = query;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.01.2003 07:58:17)
     * 
     * @param newTri
     *            java.lang.String
     */
    public void setTri(java.lang.String newTri) {
        tri = newTri;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.01.2003 15:58:12)
     * 
     * @param newUntilAnnee
     *            java.lang.String
     */
    public void setUntilAnnee(java.lang.String newUntilAnnee) {
        untilAnnee = newUntilAnnee;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.01.2003 15:58:12)
     * 
     * @param newUntilDateInscription
     *            java.lang.String
     */
    public void setUntilDateInscription(java.lang.String newUntilDateInscription) {
        untilDateInscription = newUntilDateInscription;
    }

    /**
     * @param string
     */
    public void setUntilGeMoisFin(String string) {
        untilGeMoisFin = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.01.2003 15:58:12)
     * 
     * @param newUntilIdJournal
     *            java.lang.String
     */
    public void setUntilIdJournal(java.lang.String newUntilIdJournal) {
        untilIdJournal = newUntilIdJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.12.2002 08:50:30)
     * 
     * @param newUntilMoisDebut
     *            java.lang.String
     */
    public void setUntilMoisDebut(java.lang.String newUntilMoisDebut) {
        untilMoisDebut = newUntilMoisDebut;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.01.2003 15:58:12)
     * 
     * @param newUntilNumeroAvs
     *            java.lang.String
     */
    public void setUntilNumeroAvs(java.lang.String newUntilNumeroAvs) {
        untilNumeroAvs = newUntilNumeroAvs;
    }

}
