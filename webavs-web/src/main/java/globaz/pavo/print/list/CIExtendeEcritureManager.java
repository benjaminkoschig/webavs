package globaz.pavo.print.list;

import globaz.framework.secure.user.FWSecureUserDetail;
import globaz.globall.db.BPreparedStatement;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CIEcriture;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Insert the type's description here. Creation date: (09.07.2003 17:05:00)
 * 
 * @author: Administrator
 */
public class CIExtendeEcritureManager extends globaz.globall.db.BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // (KBIECR) des
    // écritures à cacher
    private static String NIV_SECURITY_0 = "317000";
    private static String NIV_SECURITY_X = "31700";
    // SECURITE CI SUR AFFILIATION
    public static String SECURE_CODE = "SecureCode";

    protected static String getSqlChercheEcritureACacher(String fromNumeroJournal, String toNumeroJournal)
            throws Exception {
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
        sql.append(" WHERE aff.MATSEC>? ");

        if (!JadeStringUtil.isEmpty(fromNumeroJournal)) {
            sql.append(" and ecr.KCID>=? ");
        }

        if (!JadeStringUtil.isEmpty(toNumeroJournal)) {
            sql.append(" and ecr.KCID<=? ");
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
    protected static String getSqlChercheEcritureGenre8ACacher(String fromNumeroJournal, String toNumeroJournal)
            throws Exception {
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
        sql.append(".CIECRIP WHERE KBTGEN=310008 and KBTEXT=0 and KBIPAR!=0 ");

        if (!JadeStringUtil.isEmpty(fromNumeroJournal)) {
            sql.append(" and KCID>=? ");
        }

        if (!JadeStringUtil.isEmpty(toNumeroJournal)) {
            sql.append(" and KCID<=? ");
        }

        sql.append(" ))ecr inner join ");

        sql.append(Jade.getInstance().getDefaultJdbcSchema());
        sql.append(".CIINDIP ci on ecr.KAIIND=ci.KAIIND inner join ");
        sql.append(Jade.getInstance().getDefaultJdbcSchema());
        sql.append(".AFAFFIP aff on ecr.KBITIE=aff.MAIAFF ");
        sql.append("WHERE aff.MATSEC>?");

        return sql.toString();
    }

    public static String padAfterString(String stringToPad, String padString, int length) {
        while (stringToPad.length() < length) {
            stringToPad = stringToPad + padString;
        }
        return stringToPad;
    }

    public static String padBeforeString(String stringToPad, String padString, int length) {
        while (stringToPad.length() < length) {
            stringToPad = padString + stringToPad;
        }
        return stringToPad;
    }

    private Boolean avecEcrituresNegatives = new Boolean(false);
    // écritures dont
    // l'affiliation est
    // protégée et que le niveau
    // de sécurité du user n'est
    // pas suffisant
    private boolean cacherEcritureGenre8 = false;// true si il faut cacher les
    private int cacherEcritureProtege = 0;
    // écritures de genre 8
    private ArrayList ecritureACacher = new ArrayList();// contient l'id
    private Boolean ecrituresSalariees = new Boolean(false);
    private boolean existEcritureACacher = false;// true si il existe des
    private String forAnneeCotisation;
    private String forCode;
    private String forExtourne;
    private String forGenre;
    private String forGenreEcrituresAParser;
    private String forGenreParse = new String();
    private String forGenreSixSept;
    private java.lang.String forIdTypeCompte;
    private java.lang.String forIdTypeJournal;
    private String forItTypeJournalMultiple;
    private java.lang.String forNomEspion;
    private String forNotExtourne;
    private java.lang.String forNumeroAffilie;
    private String forParticulier;

    private java.lang.String fromAnneeCotisation;
    private java.lang.String fromDateInscription;
    private java.lang.String fromNumeroAffilie;
    private java.lang.String fromNumeroAvs;
    private java.lang.String fromNumeroJournal;
    private String isBta = new String();
    private ArrayList listeEcrituresCachees = new ArrayList();// contient l'id
    private java.lang.String toAnneeCotisation;
    private String forCodeSpecial;

    // (KBIECR) des
    // écritures
    // cachées

    public String getForCodeSpecial() {
        return forCodeSpecial;
    }

    public void setForCodeSpecial(String forCodeSpecial) {
        this.forCodeSpecial = forCodeSpecial;
    }

    private java.lang.String toDateInscription;

    private java.lang.String toNumeroAvs;

    private java.lang.String toNumeroJournal;

    private java.lang.String tri;

    /**
     * CIExtendeEcritureManager constructor comment.
     */
    public CIExtendeEcritureManager() {
        super();
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
        if (cacherEcritureGenre8) {
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
    }

    @Override
    protected void _beforeFind(BTransaction transaction) {
        // si la vérification des écritures protégé par affiliation est activée
        // on cherche si il existe des écritures à cacher
        if (cacherEcritureProtege == 1) {
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
                    user.setLabel(CIExtendeEcritureManager.SECURE_CODE);
                    user.retrieve();

                    if (!JadeStringUtil.isEmpty(user.getData())) {
                        userLevelSecurity = user.getData();
                    } else {
                        // si le complément n'existe pas on lui met un niveau à
                        // 0 (aucun droit)
                        userLevelSecurity = "0";
                    }

                    // recherche des écritures où l'affilié est protégé avec un
                    // niveau supérieur à celui du securecode du user
                    transactionEcriture = new BTransaction(getSession());
                    transactionEcriture.openTransaction();
                    BPreparedStatement psChercheEcritureACacher = new BPreparedStatement(transactionEcriture);
                    psChercheEcritureACacher.prepareStatement(CIExtendeEcritureManager.getSqlChercheEcritureACacher(
                            fromNumeroJournal, toNumeroJournal));
                    psChercheEcritureACacher.clearParameters();
                    psChercheEcritureACacher.setBigDecimal(1, new BigDecimal(CIExtendeEcritureManager.NIV_SECURITY_X
                            + userLevelSecurity));
                    if (!JadeStringUtil.isEmpty(fromNumeroJournal) && !JadeStringUtil.isEmpty(toNumeroJournal)) {
                        psChercheEcritureACacher.setBigDecimal(2, new BigDecimal(fromNumeroJournal));
                        psChercheEcritureACacher.setBigDecimal(3, new BigDecimal(toNumeroJournal));
                    } else if (!JadeStringUtil.isEmpty(fromNumeroJournal)) {
                        psChercheEcritureACacher.setBigDecimal(2, new BigDecimal(fromNumeroJournal));
                    } else if (!JadeStringUtil.isEmpty(toNumeroJournal)) {
                        psChercheEcritureACacher.setBigDecimal(2, new BigDecimal(toNumeroJournal));
                    }

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
                    psChercheEcritureGenre8ACacher.prepareStatement(CIExtendeEcritureManager
                            .getSqlChercheEcritureGenre8ACacher(fromNumeroJournal, toNumeroJournal));
                    psChercheEcritureGenre8ACacher.clearParameters();
                    if (!JadeStringUtil.isEmpty(fromNumeroJournal) && !JadeStringUtil.isEmpty(toNumeroJournal)) {
                        psChercheEcritureGenre8ACacher.setBigDecimal(1, new BigDecimal(fromNumeroJournal));
                        psChercheEcritureGenre8ACacher.setBigDecimal(2, new BigDecimal(toNumeroJournal));
                        psChercheEcritureGenre8ACacher.setBigDecimal(3, new BigDecimal(
                                CIExtendeEcritureManager.NIV_SECURITY_X + userLevelSecurity));
                    } else if (!JadeStringUtil.isEmpty(fromNumeroJournal)) {
                        psChercheEcritureGenre8ACacher.setBigDecimal(1, new BigDecimal(fromNumeroJournal));
                        psChercheEcritureGenre8ACacher.setBigDecimal(2, new BigDecimal(
                                CIExtendeEcritureManager.NIV_SECURITY_X + userLevelSecurity));
                    } else if (!JadeStringUtil.isEmpty(toNumeroJournal)) {
                        psChercheEcritureGenre8ACacher.setBigDecimal(1, new BigDecimal(toNumeroJournal));
                        psChercheEcritureGenre8ACacher.setBigDecimal(2, new BigDecimal(
                                CIExtendeEcritureManager.NIV_SECURITY_X + userLevelSecurity));
                    } else {
                        psChercheEcritureGenre8ACacher.setBigDecimal(1, new BigDecimal(
                                CIExtendeEcritureManager.NIV_SECURITY_X + userLevelSecurity));
                    }

                    ResultSet resultGenre8 = psChercheEcritureGenre8ACacher.executeQuery();
                    if (resultGenre8.next()) {
                        // si il en existe on cache les écritures de genre 8
                        cacherEcritureGenre8 = true;
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
    }

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "*";
    }

    /**
     * Renvoie la liste des tables
     * 
     * @return la liste des tables
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String sqlFrom = _getCollection() + "CIECRIP ";

        // jointures externes
        // journal
        sqlFrom += "LEFT OUTER JOIN " + _getCollection() + "CIJOURP ON " + _getCollection() + "CIECRIP.KCID="
                + _getCollection() + "CIJOURP.KCID ";
        // CI
        sqlFrom += "LEFT OUTER JOIN " + _getCollection() + "CIINDIP ON " + _getCollection() + "CIECRIP.KAIIND="
                + _getCollection() + "CIINDIP.KAIIND ";
        // Sur les affiliés
        sqlFrom += "LEFT OUTER JOIN " + _getCollection() + "AFAFFIP ON " + _getCollection() + "CIECRIP.KBITIE="
                + _getCollection() + "AFAFFIP.MAIAFF ";
        // Sur les tiers
        sqlFrom += "LEFT OUTER JOIN " + _getCollection() + "TITIERP ON " + _getCollection() + "AFAFFIP.HTITIE="
                + _getCollection() + "TITIERP.HTITIE ";
        System.out.println("FROM:" + sqlFrom);
        return sqlFrom;
    }

    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        String sqlOrder;
        // Tri sur le numéro AVS ou date d'inscription
        if ("avs".equals(getTri())) {
            sqlOrder = _getCollection() + "CIINDIP.KANAVS, MALNAF, KBNANN, KBNMOD, KBNMOF, KBTEXT";
        } else if ("numAff".equals(getTri())) {
            sqlOrder = _getCollection() + "AFAFFIP.MALNAF, KANAVS, KBNANN, KBNMOD, KBNMOF, KBTEXT";
        } else if ("date".equals(getTri())) {
            sqlOrder = "SUBSTR(" + _getCollection()
                    + "CIECRIP.KBLESP,1,8), MALNAF, KANAVS, KBNANN, KBNMOD, KBNMOF, KBTEXT";
            // sqlOrder="";
        } else if ("anneeCotisation".equals(getTri())) {
            sqlOrder = _getCollection() + "CIECRIP.KBNANN, CIECRIP.KBNMOD, CIECRIP.KBNMOF";
        } else if ("nomPrenom".equals(tri)) {
            sqlOrder = _getCollection() + "CIINDIP.KALNOM, KBNANN, KBNMOD, KBNMOF";
        } else {
            sqlOrder = "";
        }
        System.out.println("ORDER BY:" + sqlOrder);
        return sqlOrder;
    }

    /**
     * retourne la clause WHERE de la requete SQL + jointure
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        if (!JadeStringUtil.isBlankOrZero(getForCodeSpecial())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBTSPE = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForCodeSpecial());
        }

        // Les jointures
        // Avec les journeaux
        // sqlWhere = _getCollection() + "CIJOURP.KCID=" + _getCollection() +
        // "CIECRIP.KCID";
        // Avec les comptes individuels
        // sqlWhere += " AND " + _getCollection() + "CIINDIP.KAIIND=" +
        // _getCollection() + "CIECRIP.KAIIND";
        // Test sur le numéro d'affilié
        if (!globaz.globall.util.JAUtil.isStringEmpty(forNumeroAffilie)) {
            // recherche de l'id
            try {
                CIApplication app = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                        CIApplication.DEFAULT_APPLICATION_PAVO);
                String in = app.getAffiliesByNo(getSession(), forNumeroAffilie);
                if (in.length() > 0) {
                    sqlWhere += " AND " + _getCollection() + "CIECRIP.KBITIE IN (" + in + ") ";
                } else {
                    // recherche vide
                    sqlWhere += " AND " + _getCollection() + "CIECRIP.KBITIE=0 ";
                }
            } catch (Exception e) {
                // recherche vide
                sqlWhere += " AND " + _getCollection() + "CIECRIP.KBITIE=0 "; // laisser
                // tel
                // quel
            }
        }
        // Test sur le numero AVS
        if ((!globaz.globall.util.JAUtil.isStringEmpty(fromNumeroAvs))
                && (!globaz.globall.util.JAUtil.isStringEmpty(toNumeroAvs))) {
            // Padding
            String fromNumeroAvsPadded = CIExtendeEcritureManager.padAfterString(fromNumeroAvs, "0", 11);
            String toNumeroAvsPadded = CIExtendeEcritureManager.padAfterString(toNumeroAvs, "9", 11);
            // Si from et to sont égaux
            if (fromNumeroAvs.equals(toNumeroAvs)) {
                sqlWhere += " AND " + _getCollection() + "CIINDIP.KANAVS='" + fromNumeroAvsPadded + "'";
            } else {
                sqlWhere += " AND " + _getCollection() + "CIINDIP.KANAVS>='" + fromNumeroAvsPadded + "' AND "
                        + _getCollection() + "CIINDIP.KANAVS<='" + toNumeroAvsPadded + "'";
            }
        }
        // Seul from est non vide
        else if (!globaz.globall.util.JAUtil.isStringEmpty(fromNumeroAvs)) {
            // Padding
            String fromNumeroAvsPadded = CIExtendeEcritureManager.padAfterString(fromNumeroAvs, "0", 11);
            sqlWhere += " AND " + _getCollection() + "CIINDIP.KANAVS>='" + fromNumeroAvsPadded + "'";
        }
        // Seul to est non vide
        else if (!globaz.globall.util.JAUtil.isStringEmpty(toNumeroAvs)) {
            // Padding
            String toNumeroAvsPadded = CIExtendeEcritureManager.padAfterString(toNumeroAvs, "9", 11);
            sqlWhere += " AND " + _getCollection() + "CIINDIP.KANAVS<='" + toNumeroAvsPadded + "'";
        }
        // Test sur le numéro du journal
        if ((!globaz.globall.util.JAUtil.isStringEmpty(fromNumeroJournal))
                && (!globaz.globall.util.JAUtil.isStringEmpty(toNumeroJournal))) {
            // Si from et to sont égaux
            if (fromNumeroJournal.equals(toNumeroJournal)) {
                sqlWhere += " AND " + _getCollection() + "CIECRIP.KCID=" + fromNumeroJournal;
            } else {
                sqlWhere += " AND " + _getCollection() + "CIECRIP.KCID>=" + fromNumeroJournal + " AND "
                        + _getCollection() + "CIECRIP.KCID<=" + toNumeroJournal;
            }
        }
        // Seul from est non vide
        else if (!globaz.globall.util.JAUtil.isStringEmpty(fromNumeroJournal)) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KCID>=" + fromNumeroJournal;
        } else if (!globaz.globall.util.JAUtil.isStringEmpty(toNumeroJournal)) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KCID<=" + toNumeroJournal;
        }
        // Test sur l'année de cotisation
        if ((!globaz.globall.util.JAUtil.isStringEmpty(fromAnneeCotisation))
                && (!globaz.globall.util.JAUtil.isStringEmpty(toAnneeCotisation))) {
            // Si from et to sont égaux
            if (fromAnneeCotisation.equals(toAnneeCotisation)) {
                sqlWhere += " AND " + _getCollection() + "CIECRIP.KBNANN=" + fromAnneeCotisation;
            } else {
                sqlWhere += " AND " + _getCollection() + "CIECRIP.KBNANN>=" + fromAnneeCotisation + " AND "
                        + _getCollection() + "CIECRIP.KBNANN<=" + toAnneeCotisation;
            }
        }
        // Seul from est non vide
        else if (!globaz.globall.util.JAUtil.isStringEmpty(fromAnneeCotisation)) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBNANN>=" + fromAnneeCotisation;
        } else if (!globaz.globall.util.JAUtil.isStringEmpty(toAnneeCotisation)) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBNANN<=" + toAnneeCotisation;
        }
        // Test sur la date d'inscription
        if ((!globaz.globall.util.JAUtil.isStringEmpty(fromDateInscription))
                && (!globaz.globall.util.JAUtil.isStringEmpty(toDateInscription))) {
            // Si from et to sont égaux
            if (fromDateInscription.equals(toDateInscription)) {
                sqlWhere += " AND " + _getCollection() + "CIECRIP.KBLESP like '"
                        + this._dbWriteDateAMJ(statement.getTransaction(), fromDateInscription) + "%'";
            } else {
                sqlWhere += " AND " + _getCollection() + "CIECRIP.KBLESP>='"
                        + this._dbWriteDateAMJ(statement.getTransaction(), fromDateInscription) + "' AND " + "SUBSTR("
                        + _getCollection() + "CIECRIP.KBLESP,1,8)<='"
                        + this._dbWriteDateAMJ(statement.getTransaction(), toDateInscription) + "'";
            }
        }
        // Seul from est non vide
        else if (!globaz.globall.util.JAUtil.isStringEmpty(fromDateInscription)) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBLESP>='"
                    + this._dbWriteDateAMJ(statement.getTransaction(), fromDateInscription) + "'";
        } else if (!globaz.globall.util.JAUtil.isStringEmpty(toDateInscription)) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBLESP<='"
                    + this._dbWriteDateAMJ(statement.getTransaction(), toDateInscription) + "'";
        }
        // Le type de compte
        if (!globaz.globall.util.JAUtil.isStringEmpty(forIdTypeCompte)) {
            if (CIEcriture.CS_TEMPORAIRE.equals(forIdTypeCompte)) {
                // ajouter temporaire suspens
                sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTCPT in (" + CIEcriture.CS_TEMPORAIRE_SUSPENS
                        + ", " + CIEcriture.CS_TEMPORAIRE + ") ";
            } else {
                sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTCPT=" + forIdTypeCompte;
            }

        } else {
            // On exclut les types correction
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTCPT<>"
                    + globaz.pavo.db.compte.CIEcriture.CS_CORRECTION;
        }
        // Le type de journal
        if (!JAUtil.isStringEmpty(getForGenreEcrituresAParser())) {
            parseGenre(getForGenreEcrituresAParser(), statement.getTransaction());
        }
        // évite l'écrasement du genre par dans la boucle des totaux
        if (!JAUtil.isStringEmpty(getForGenreParse())) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTGEN= " + getForGenreParse();
        }
        if (!globaz.globall.util.JAUtil.isStringEmpty(forIdTypeJournal)) {
            sqlWhere += " AND " + _getCollection() + "CIJOURP.KCITIN=" + forIdTypeJournal;
        }
        // Test sur le nom d'utilisateur
        if (!globaz.globall.util.JAUtil.isStringEmpty(forNomEspion)) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBLESP LIKE '%" + forNomEspion + "%' ";
        }
        if (!JAUtil.isStringEmpty(getIsBta())) {
            if ("True".equals(getIsBta())) {
                sqlWhere += " AND " + _getCollection() + "CIECRIP.KBNBTA <> 0 ";

            }
        }
        if (!JAUtil.isStringEmpty(getForCode())) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTCOD=  " + getForCode();
        }

        if (!JAUtil.isStringEmpty(forItTypeJournalMultiple)) {
            sqlWhere += " AND " + _getCollection() + "CIJOURP.KCITIN IN( " + forItTypeJournalMultiple + " ) ";
        }
        if (!JAUtil.isStringEmpty(getForGenre())) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTGEN= " + getForGenre();
        }
        if (!JAUtil.isStringEmpty(getForGenreSixSept())) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTGEN IN ( " + CIEcriture.CS_CIGENRE_6 + ", "
                    + CIEcriture.CS_CIGENRE_7 + " ) ";
        }
        if (!JAUtil.isStringEmpty(getForAnneeCotisation())) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBNANN = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForAnneeCotisation());
        }
        if (!JAUtil.isStringEmpty(getForExtourne())) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTEXT = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForExtourne());
        }
        // /fonctionne seulement pour les extourne de genre 1
        if (!JAUtil.isStringEmpty(getForNotExtourne())) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTEXT <> "
                    + this._dbWriteNumeric(statement.getTransaction(), CIEcriture.CS_EXTOURNE_1);
        }
        if (!JAUtil.isStringEmpty(getForParticulier())) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTPAR = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForParticulier());
        }
        if (!avecEcrituresNegatives.booleanValue()) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTEXT = 0 ";
        }
        if (!avecEcrituresNegatives.booleanValue()) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTEXT = 0 ";
        }
        if (ecrituresSalariees.booleanValue()) {
            sqlWhere += " AND " + _getCollection()
                    + "CIECRIP.KBTGEN <> 310003 AND KBTGEN <> 310009 AND (KBTGEN <> 310007 OR KBTSPE <> 312002) ";
        }

        if (sqlWhere.startsWith(" AND")) {
            sqlWhere = sqlWhere.substring(5);
        }

        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité
     * 
     * @return la nouvelle entité
     * @exception java.lang.Exception
     *                si la création a échouée
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CIExtendedEcriture();
    }

    /**
     * Renvoie le code système asssocié à un code utilisateur et un groupe.
     * 
     * @param Un
     *            object BTransaction.
     * @param Le
     *            code utilisateur.
     * @param Le
     *            groupe.
     * @return Le code système asssocié à un code utilisateur et un groupe
     */
    public String codeUtilisateurToCodeSysteme(BTransaction transaction, String code, String groupe) throws Exception {
        if (!JAUtil.isIntegerEmpty(code.trim())) {
            try {
                int valeurCode = new Integer(code).intValue();
                // En dessous de 300000, c'est une code utilisateur (pour PAVO
                // en tout cas)
                if (valeurCode < 300000) {
                    throw new Exception();
                }
                // code est déjà un code système
                return code;
            } catch (Exception e) {
                // C'est un code utilisateur. Il faut obtenir le code système
                FWParametersSystemCodeManager systemCodeMng = new FWParametersSystemCodeManager();
                systemCodeMng.setSession(getSession());
                systemCodeMng.setForIdGroupe(groupe);
                systemCodeMng.setForCodeUtilisateur(code);
                systemCodeMng.find(transaction);
                if (!systemCodeMng.hasErrors()) {
                    if (systemCodeMng.getSize() > 0) {
                        return ((FWParametersSystemCode) systemCodeMng.getEntity(0)).getIdCode();
                    } else {
                        // Pas de code système pour le code utilisateur
                        return "";
                    }
                } else {
                    _addError(transaction, getSession().getLabel("MSG_ECRITURE_USER_CODE"));
                    return "";
                }
            }
        } else {
            return "";
        }
    }

    /**
     * Renvoie le code système asssocié à un code utilisateur et un groupe. Pour l'ecriture
     * 
     * @param Un
     *            object BTransaction.
     * @param Le
     *            code utilisateur.
     * @param Le
     *            groupe.
     * @return Le code système asssocié à un code utilisateur et un groupe
     */
    public String codeUtilisateurToCodeSystemeGre(BTransaction transaction, String code, String groupe)
            throws Exception {
        if (!JAUtil.isStringEmpty(code.trim())) {
            try {
                int valeurCode = new Integer(code).intValue();
                // En dessous de 300000, c'est une code utilisateur (pour PAVO
                // en tout cas)
                if (valeurCode < 300000) {
                    throw new Exception();
                }
                // code est déjà un code système
                return code;
            } catch (Exception e) {
                // C'est un code utilisateur. Il faut obtenir le code système
                FWParametersSystemCodeManager systemCodeMng = new FWParametersSystemCodeManager();
                systemCodeMng.setSession(getSession());
                systemCodeMng.setForIdGroupe(groupe);
                systemCodeMng.setForCodeUtilisateur(code);
                systemCodeMng.find(transaction);
                if (!systemCodeMng.hasErrors()) {
                    if (systemCodeMng.getSize() > 0) {
                        return ((FWParametersSystemCode) systemCodeMng.getEntity(0)).getIdCode();
                    } else {
                        // Pas de code système pour le code utilisateur
                        return "";
                    }
                } else {
                    _addError(transaction, getSession().getLabel("MSG_ECRITURE_USER_CODE"));
                    return "";
                }
            }
        } else {
            return "";
        }
    }

    /**
     * Returns the avecEcrituresNegatives.
     * 
     * @return Boolean
     */
    public Boolean getAvecEcrituresNegatives() {
        return avecEcrituresNegatives;
    }

    public int getCacherEcritureProtege() {
        return cacherEcritureProtege;
    }

    public Boolean getEcrituresSalariees() {
        return ecrituresSalariees;
    }

    /**
     * Returns the forAnneeCotisation.
     * 
     * @return String
     */
    public String getForAnneeCotisation() {
        return forAnneeCotisation;
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

    /**
     * Returns the forGenreEcrituresAParser.
     * 
     * @return String
     */
    public String getForGenreEcrituresAParser() {
        return forGenreEcrituresAParser;
    }

    /**
     * @return
     */
    public String getForGenreParse() {
        return forGenreParse;
    }

    /**
     * Returns the forGenreSixSept.
     * 
     * @return String
     */
    public String getForGenreSixSept() {
        return forGenreSixSept;
    }

    /**
     * Insert the method's description here. Creation date: (09.07.2003 18:06:12)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdTypeCompte() {
        return forIdTypeCompte;
    }

    /**
     * Returns the forIdTypeJournal.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdTypeJournal() {
        return forIdTypeJournal;
    }

    /**
     * Returns the forItTypeJournalMultiple.
     * 
     * @return String
     */
    public String getForItTypeJournalMultiple() {
        return forItTypeJournalMultiple;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 16:08:58)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNomEspion() {
        return forNomEspion;
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
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 16:07:29)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNumeroAffilie() {
        return forNumeroAffilie;
    }

    /**
     * Returns the forParticulier.
     * 
     * @return String
     */
    public String getForParticulier() {
        return forParticulier;
    }

    /**
     * Insert the method's description here. Creation date: (10.07.2003 09:39:03)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromAnneeCotisation() {
        return fromAnneeCotisation;
    }

    /**
     * Insert the method's description here. Creation date: (09.07.2003 18:01:13)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromDateInscription() {
        return fromDateInscription;
    }

    /**
     * Insert the method's description here. Creation date: (09.07.2003 17:39:01)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromNumeroAvs() {
        return fromNumeroAvs;
    }

    /**
     * Insert the method's description here. Creation date: (10.07.2003 09:39:42)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromNumeroJournal() {
        return fromNumeroJournal;
    }

    /**
     * Returns the isBta.
     * 
     * @return String
     */
    public String getIsBta() {
        return isBta;
    }

    /**
     * Insert the method's description here. Creation date: (10.07.2003 09:39:20)
     * 
     * @return java.lang.String
     */
    public java.lang.String getToAnneeCotisation() {
        return toAnneeCotisation;
    }

    /**
     * Insert the method's description here. Creation date: (09.07.2003 18:01:41)
     * 
     * @return java.lang.String
     */
    public java.lang.String getToDateInscription() {
        return toDateInscription;
    }

    /**
     * Insert the method's description here. Creation date: (09.07.2003 17:39:16)
     * 
     * @return java.lang.String
     */
    public java.lang.String getToNumeroAvs() {
        return toNumeroAvs;
    }

    /**
     * Insert the method's description here. Creation date: (10.07.2003 09:39:57)
     * 
     * @return java.lang.String
     */
    public java.lang.String getToNumeroJournal() {
        return toNumeroJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 09:38:51)
     * 
     * @return int
     */
    public String getTri() {
        return tri;
    }

    protected void parseGenre(String gre, BTransaction transaction) {
        if (!JAUtil.isStringEmpty(gre.trim())) {
            String greCourant = gre;
            if (greCourant.length() == 2) {
                if ("8".equals(greCourant.substring(0, 1))) {
                    greCourant = "0" + greCourant;
                } else {
                    greCourant = greCourant + "0";
                }
            }
            // Si GRE de bonne taille...
            if (greCourant.length() == 3) {
                try {
                    setForGenreParse(codeUtilisateurToCodeSystemeGre(transaction, greCourant.substring(1, 2),
                            "CICODGEN"));
                    setForExtourne(codeUtilisateurToCodeSysteme(transaction, greCourant.substring(0, 1), "CICODEXT"));
                    setForParticulier(codeUtilisateurToCodeSysteme(transaction, greCourant.substring(2, 3), "CIGENSPL"));
                } catch (Exception e) {
                }
                // pour le code particulier:
                // setParticulier(greCourant.substring(2, 3));
            }
        }
    }

    /**
     * Sets the avecEcrituresNegatives.
     * 
     * @param avecEcrituresNegatives
     *            The avecEcrituresNegatives to set
     */
    public void setAvecEcrituresNegatives(Boolean avecEcrituresNegatives) {
        this.avecEcrituresNegatives = avecEcrituresNegatives;
    }

    public void setCacherEcritureProtege(int cacherEcritureProtege) {
        this.cacherEcritureProtege = cacherEcritureProtege;
    }

    public void setEcrituresSalariees(Boolean ecrituresSalariees) {
        this.ecrituresSalariees = ecrituresSalariees;
    }

    /**
     * Sets the forAnneeCotisation.
     * 
     * @param forAnneeCotisation
     *            The forAnneeCotisation to set
     */
    public void setForAnneeCotisation(String forAnneeCotisation) {
        this.forAnneeCotisation = forAnneeCotisation;
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

    /**
     * Sets the forGenreEcrituresAParser.
     * 
     * @param forGenreEcrituresAParser
     *            The forGenreEcrituresAParser to set
     */
    public void setForGenreEcrituresAParser(String forGenreEcrituresAParser) {
        this.forGenreEcrituresAParser = forGenreEcrituresAParser;
    }

    /**
     * @param string
     */
    public void setForGenreParse(String string) {
        forGenreParse = string;
    }

    /**
     * Sets the forGenreSixSept.
     * 
     * @param forGenreSixSept
     *            The forGenreSixSept to set
     */
    public void setForGenreSixSept(String forGenreSixSept) {
        this.forGenreSixSept = forGenreSixSept;
    }

    /**
     * Insert the method's description here. Creation date: (09.07.2003 18:06:12)
     * 
     * @param newForIdTypeCompte
     *            java.lang.String
     */
    public void setForIdTypeCompte(java.lang.String newForIdTypeCompte) {
        forIdTypeCompte = newForIdTypeCompte;
    }

    /**
     * Sets the forIdTypeJournal.
     * 
     * @param forIdTypeJournal
     *            The forIdTypeJournal to set
     */
    public void setForIdTypeJournal(java.lang.String forIdTypeJournal) {
        this.forIdTypeJournal = forIdTypeJournal;
    }

    /**
     * Sets the forItTypeJournalMultiple.
     * 
     * @param forItTypeJournalMultiple
     *            The forItTypeJournalMultiple to set
     */
    public void setForItTypeJournalMultiple(String forItTypeJournalMultiple) {
        this.forItTypeJournalMultiple = forItTypeJournalMultiple;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 16:08:58)
     * 
     * @param newForNomEspion
     *            java.lang.String
     */
    public void setForNomEspion(java.lang.String newForNomEspion) {
        forNomEspion = newForNomEspion;
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
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 16:07:29)
     * 
     * @param newForNumeroAffilie
     *            java.lang.String
     */
    public void setForNumeroAffilie(java.lang.String newForNumeroAffilie) {
        forNumeroAffilie = newForNumeroAffilie;
    }

    /**
     * Sets the forParticulier.
     * 
     * @param forParticulier
     *            The forParticulier to set
     */
    public void setForParticulier(String forParticulier) {
        this.forParticulier = forParticulier;
    }

    /**
     * Insert the method's description here. Creation date: (10.07.2003 09:39:03)
     * 
     * @param newFromAnneeCotisation
     *            java.lang.String
     */
    public void setFromAnneeCotisation(java.lang.String newFromAnneeCotisation) {
        fromAnneeCotisation = newFromAnneeCotisation;
    }

    /**
     * Insert the method's description here. Creation date: (09.07.2003 18:01:13)
     * 
     * @param newFromDateInscription
     *            java.lang.String
     */
    public void setFromDateInscription(java.lang.String newFromDateInscription) {
        fromDateInscription = newFromDateInscription;
    }

    /**
     * Insert the method's description here. Creation date: (09.07.2003 17:39:01)
     * 
     * @param newFromNumeroAvs
     *            java.lang.String
     */
    public void setFromNumeroAvs(java.lang.String newFromNumeroAvs) {
        fromNumeroAvs = newFromNumeroAvs;
    }

    /**
     * Insert the method's description here. Creation date: (10.07.2003 09:39:42)
     * 
     * @param newFromNumeroJournal
     *            java.lang.String
     */
    public void setFromNumeroJournal(java.lang.String newFromNumeroJournal) {
        fromNumeroJournal = newFromNumeroJournal;
    }

    /**
     * Sets the isBta.
     * 
     * @param isBta
     *            The isBta to set
     */
    public void setIsBta(String isBta) {
        this.isBta = isBta;
    }

    /**
     * Insert the method's description here. Creation date: (10.07.2003 09:39:20)
     * 
     * @param newToAnneeCotisation
     *            java.lang.String
     */
    public void setToAnneeCotisation(java.lang.String newToAnneeCotisation) {
        toAnneeCotisation = newToAnneeCotisation;
    }

    /**
     * Insert the method's description here. Creation date: (09.07.2003 18:01:41)
     * 
     * @param newToDateInscription
     *            java.lang.String
     */
    public void setToDateInscription(java.lang.String newToDateInscription) {
        toDateInscription = newToDateInscription;
    }

    /**
     * Insert the method's description here. Creation date: (09.07.2003 17:39:16)
     * 
     * @param newToNumeroAvs
     *            java.lang.String
     */
    public void setToNumeroAvs(java.lang.String newToNumeroAvs) {
        toNumeroAvs = newToNumeroAvs;
    }

    /**
     * Insert the method's description here. Creation date: (10.07.2003 09:39:57)
     * 
     * @param newToNumeroJournal
     *            java.lang.String
     */
    public void setToNumeroJournal(java.lang.String newToNumeroJournal) {
        toNumeroJournal = newToNumeroJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.07.2003 09:38:51)
     * 
     * @param newTri
     *            int
     */
    public void setTri(String newTri) {
        tri = newTri;
    }

}
