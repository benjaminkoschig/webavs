package globaz.pavo.db.compte;

import globaz.framework.secure.user.FWSecureUserDetail;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BPreparedStatement;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Manager de <tt>CIRassemblementOuverture</tt>. Date de création : (12.11.2002 13:27:56)
 * 
 * @author: David Girardin
 */
public class CIRassemblementOuvertureEcrituresManager extends BManager {
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
     * @param forRassemblementOuvertureId
     * @return La requete (String)
     * @throws Exception
     */
    protected static String getSqlChercheEcritureACacher(String forRassemblementOuvertureId) throws Exception {
        /*
         * select KPIECA from webavs.CILRAEP ra inner join webavs.CIECRIP ecr on ecr.KBIECR=ra.KPIECA inner join
         * webavs.CIINDIP ci on ecr.KAIIND=ci.KAIIND inner join webavs.AFAFFIP aff on ecr.KBITIE=aff.MAIAFF where
         * ra.KPIRAO=idRassemblement and aff.MATSEC>31700+usersecure;
         */

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT KPIECA FROM ");
        sql.append(Jade.getInstance().getDefaultJdbcSchema());
        sql.append(".CILRAEP ra inner join ");
        sql.append(Jade.getInstance().getDefaultJdbcSchema());
        sql.append(".CIECRIP ecr on ecr.KBIECR=ra.KPIECA inner join ");
        sql.append(Jade.getInstance().getDefaultJdbcSchema());
        sql.append(".CIINDIP ci on ecr.KAIIND=ci.KAIIND inner join ");
        sql.append(Jade.getInstance().getDefaultJdbcSchema());
        sql.append(".AFAFFIP aff on ecr.KBITIE=aff.MAIAFF ");
        sql.append("WHERE ra.KPIRAO=? and aff.MATSEC>?");

        return sql.toString();
    }

    /**
     * Requete permettant de séléctionner les écritures protégées du conjoint liée à une écriture de genre 8
     * 
     * @param forRassemblementOuvertureId
     * @return la requete (String)
     * @throws Exception
     */
    protected static String getSqlChercheEcritureGenre8ACacher(String forRassemblementOuvertureId) throws Exception {
        /*--requete pour voir si on a des écritures de genre 8 dont le conjoint à des écriture dont l'affiliation est protégée
        select * from
        (select * from webavs.CIECRIP where kaiind in (select distinct kbipar from webavs.CIECRIP ecrtemp inner join webavs.CILRAEP ratemp on ecrtemp.KBIECR=ratemp.KPIECA where ratemp.KPIRAO=370177 and kbtgen=310008 and kbipar!=0))ecr inner join
        webavs.CIINDIP ci on ecr.KAIIND=ci.KAIIND inner join
        webavs.AFAFFIP aff on ecr.KBITIE=aff.MAIAFF
        where aff.MATSEC!=317005;*/

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT KBIECR FROM (SELECT * FROM ");
        sql.append(Jade.getInstance().getDefaultJdbcSchema());
        sql.append(".CIECRIP WHERE KAIIND IN (SELECT DISTINCT KBIPAR FROM ");
        sql.append(Jade.getInstance().getDefaultJdbcSchema());
        sql.append(".CIECRIP ecrtemp inner join "
                + Jade.getInstance().getDefaultJdbcSchema()
                + ".CILRAEP ratemp on ecrtemp.KBIECR=ratemp.KPIECA WHERE ratemp.KPIRAO=? AND KBTGEN=310008 and KBTEXT=0 and KBIPAR!=0))ecr inner join ");
        sql.append(Jade.getInstance().getDefaultJdbcSchema());
        sql.append(".CIINDIP ci on ecr.KAIIND=ci.KAIIND inner join ");
        sql.append(Jade.getInstance().getDefaultJdbcSchema());
        sql.append(".AFAFFIP aff on ecr.KBITIE=aff.MAIAFF ");
        sql.append("WHERE aff.MATSEC>?");

        return sql.toString();
    }

    // écritures dont
    // l'affiliation est
    // protégée et que le niveau
    // de sécurité du user n'est
    // pas suffisant
    private boolean cacherEcritureGenre8 = false;// true si il faut cacher les
    private int cacherEcritureProtege = 0;// Active ou désactive la fonction
    // écritures de genre 8
    private ArrayList ecritureACacher = new ArrayList();// contient l'id
    // permettant de cacher les
    // écritures dont l'affiliation est
    // protégée 1=active 0=désactivé
    // (impossible d'utiliser un boolean
    // à cause du setBeanProperties qui
    // set tjs false si null dans la
    // request)
    private boolean existEcritureACacher = false;// true si il existe des
    private String forAssure = new String();
    /** (KKIRAO) */
    private String forRassemblementOuvertureId = new String();

    // (KBIECR) des
    // écritures
    // cachées

    private ArrayList listeEcrituresCachees = new ArrayList();// contient l'id

    private String order = new String();

    @Override
    protected void _afterFind(globaz.globall.db.BTransaction transaction) {
        // TRAITEMENT DES ECRITURES
        if (existEcritureACacher) {
            for (int i = 0; i < getSize(); i++) {
                CIRassemblementOuvertureEcritures rassemblement = (CIRassemblementOuvertureEcritures) getEntity(i);
                if (ecritureACacher.contains(rassemblement.getIdEcritureAssure())) {
                    // AFAffiliation affiliation;
                    try {
                        // récupération de l'affilié
                        // affiliation = getAffiliationEcriture(ecriture);
                        // on vérifie le niveau de sécurité
                        // if (!affiliation.hasRightAccesSecurity()) {
                        // on cache le montant
                        rassemblement.setCacherMontant(true);
                        // on ajout l'écriture à la liste des écritures cachées
                        // (pour le calcul de la somme)
                        listeEcrituresCachees.add(rassemblement.getIdEcritureAssure());
                        // }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // TRAITEMENT DES ECRITURES DE GENRE 18
            if (listeEcrituresCachees.size() > 0) {
                for (int i = 0; i < getSize(); i++) {
                    CIRassemblementOuvertureEcritures rassemblement = (CIRassemblementOuvertureEcritures) getEntity(i);
                    if (rassemblement.getEcriture().getGenreEcriture().equals(CIEcriture.CS_CIGENRE_8)
                            && (!rassemblement.getEcriture().getExtourne().equals("0") && !rassemblement.getEcriture()
                                    .getExtourne().equals(""))) {
                        rassemblement.setCacherMontant(true);
                        // on ajout l'écriture à la liste des écritures cachées
                        // (pour le calcul de la somme)
                        listeEcrituresCachees.add(rassemblement.getIdEcritureAssure());
                    }
                }
            }
        }

        // TRAITEMENT DES ECRITURES DE GENRE 8
        if (cacherEcritureGenre8) {
            for (int i = 0; i < getSize(); i++) {
                CIRassemblementOuvertureEcritures rassemblement = (CIRassemblementOuvertureEcritures) getEntity(i);
                if (rassemblement.getEcriture().getGenreEcriture().equals(CIEcriture.CS_CIGENRE_8)
                        && (rassemblement.getEcriture().getExtourne().equals("0") || rassemblement.getEcriture()
                                .getExtourne().equals(""))) {
                    rassemblement.setCacherMontant(true);
                    // on ajout l'écriture à la liste des écritures cachées
                    // (pour le calcul de la somme)
                    listeEcrituresCachees.add(rassemblement.getIdEcritureAssure());
                }
            }
        }
    }

    @Override
    protected void _beforeFind(BTransaction transaction) {
        // si la vérification des écritures protégé par affiliation est activée
        // on cherche si il existe des écritures à cacher
        if (cacherEcritureProtege == 1 && !JadeStringUtil.isEmpty(forRassemblementOuvertureId)) {
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
                    user.setLabel(SECURE_CODE);
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
                    psChercheEcritureACacher
                            .prepareStatement(getSqlChercheEcritureACacher(forRassemblementOuvertureId));
                    psChercheEcritureACacher.clearParameters();
                    psChercheEcritureACacher.setBigDecimal(1, new BigDecimal(forRassemblementOuvertureId));
                    psChercheEcritureACacher.setBigDecimal(2, new BigDecimal(NIV_SECURITY_X + userLevelSecurity));
                    ResultSet result = psChercheEcritureACacher.executeQuery();
                    if (result.next()) {
                        // si il en existe on indique qu'il faut parcourir les
                        // écritures dans le after_find et les cacher
                        existEcritureACacher = true;
                        wantCallMethodAfterFind(true);
                        // System.out.println("Des écritures ont été cachées");

                        // on récupère l'id de l'écriture concernée
                        ecritureACacher.add(result.getBigDecimal("KPIECA").toString());
                        while (result.next()) {
                            ecritureACacher.add(result.getBigDecimal("KPIECA").toString());
                        }

                    } else {
                        // System.out.println("Aucune écriture n'a été cachée");
                    }

                    // vérifie si il existe des écritures de genre 8 et de texte
                    // 0 et si le conjoint a des écritures protégées (afin de
                    // cacher les écritures protégées du conjoint)
                    BPreparedStatement psChercheEcritureGenre8ACacher = new BPreparedStatement(transactionEcriture);
                    psChercheEcritureGenre8ACacher
                            .prepareStatement(getSqlChercheEcritureGenre8ACacher(forRassemblementOuvertureId));
                    psChercheEcritureGenre8ACacher.clearParameters();
                    psChercheEcritureGenre8ACacher.setBigDecimal(1, new BigDecimal(forRassemblementOuvertureId));
                    psChercheEcritureGenre8ACacher.setBigDecimal(2, new BigDecimal(NIV_SECURITY_X + userLevelSecurity));
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
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CILRAEP";
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

    /**
     * retourne la clause WHERE de la requete SQL
     * 
     * @param BStatement
     *            le statement
     * @return la clause WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        // traitement du positionnement
        if (getForRassemblementOuvertureId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KPIRAO=" + _dbWriteNumeric(statement.getTransaction(), getForRassemblementOuvertureId());
        }
        if (getForAssure().length() != 0) {
            if ("True".equals(getForAssure())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "KPIECA<>0 ";

            }
            if ("False".equals(getForAssure())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "KPIECC<>0 ";

            }
        }

        return sqlWhere;
    }

    /**
     * Instancie un objet étendant BEntity
     * 
     * @return BEntity un objet repésentant le résultat
     * @throws Exception
     *             la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CIRassemblementOuvertureEcritures();
    }

    public int getCacherEcritureProtege() {
        return cacherEcritureProtege;
    }

    /**
     * Returns the forAssure.
     * 
     * @return String
     */
    public String getForAssure() {
        return forAssure;
    }

    /**
     * Returns the forRassemblementOuvertureId.
     * 
     * @return String
     */
    public String getForRassemblementOuvertureId() {
        return forRassemblementOuvertureId;
    }

    /**
     * Returns the order.
     * 
     * @return String
     */
    public String getOrder() {
        return order;
    }

    public void setCacherEcritureProtege(int cacherEcritureProtege) {
        this.cacherEcritureProtege = cacherEcritureProtege;
    }

    /**
     * Sets the forAssure.
     * 
     * @param forAssure
     *            The forAssure to set
     */
    public void setForAssure(String forAssure) {
        this.forAssure = forAssure;
    }

    /**
     * Sets the forRassemblementOuvertureId.
     * 
     * @param forRassemblementOuvertureId
     *            The forRassemblementOuvertureId to set
     */
    public void setForRassemblementOuvertureId(String forRassemblementOuvertureId) {
        this.forRassemblementOuvertureId = forRassemblementOuvertureId;
    }

    /**
     * Sets the order.
     * 
     * @param order
     *            The order to set
     */
    public void setOrder(String order) {
        this.order = order;
    }

}
