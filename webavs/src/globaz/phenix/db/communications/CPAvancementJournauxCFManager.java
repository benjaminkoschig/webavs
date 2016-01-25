package globaz.phenix.db.communications;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

public class CPAvancementJournauxCFManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeComFisc = "";
    private String forGenreAffilie = "";
    private String forNumJournal = "";

    private String totalAbandonne = new String();
    private String totalAControler = new String();
    private String totalAvertissement = new String();
    private String totalComptabilise = new String();
    private String totalEnEnquete = new String();
    private String totalErreur = new String();
    private String totalReceptionne = new String();
    private String totalSansAnomalie = new String();
    private String totalTotal = new String();
    private String totalValide = new String();

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        if (JadeStringUtil.isBlankOrZero(getForAnneeComFisc()) && JadeStringUtil.isBlankOrZero(getForGenreAffilie())) {
            return " IWRJOU, " + "(SELECT COUNT(IKTSTA) FROM " + _getCollection() + "CPCRETP WHERE IKTSTA="
                    + CPCommunicationFiscaleRetourViewBean.CS_A_CONTROLER + " AND A.IWRJOU=IWRJOU) AS nbrAControler, "
                    + "(SELECT COUNT(IKTSTA) FROM " + _getCollection() + "CPCRETP WHERE IKTSTA="
                    + CPCommunicationFiscaleRetourViewBean.CS_ABANDONNE + " AND A.IWRJOU=IWRJOU) AS nbrAbandonne, "
                    + "(SELECT COUNT(IKTSTA) FROM " + _getCollection() + "CPCRETP WHERE IKTSTA="
                    + CPCommunicationFiscaleRetourViewBean.CS_AVERTISSEMENT
                    + " AND A.IWRJOU=IWRJOU) AS nbrAvertissement, " + "(SELECT COUNT(IKTSTA) FROM " + _getCollection()
                    + "CPCRETP WHERE IKTSTA=" + CPCommunicationFiscaleRetourViewBean.CS_COMPTABILISE
                    + " AND A.IWRJOU=IWRJOU) AS nbrComptabilise, " + "(SELECT COUNT(IKTSTA) FROM " + _getCollection()
                    + "CPCRETP WHERE IKTSTA=" + CPCommunicationFiscaleRetourViewBean.CS_ERREUR
                    + " AND A.IWRJOU=IWRJOU) AS nbrErreur, " + "(SELECT COUNT(IKTSTA) FROM " + _getCollection()
                    + "CPCRETP WHERE IKTSTA=" + CPCommunicationFiscaleRetourViewBean.CS_RECEPTIONNE
                    + " AND A.IWRJOU=IWRJOU) AS nbrReceptionne, " + "(SELECT COUNT(IKTSTA) FROM " + _getCollection()
                    + "CPCRETP WHERE IKTSTA=" + CPCommunicationFiscaleRetourViewBean.CS_SANS_ANOMALIE
                    + " AND A.IWRJOU=IWRJOU) AS nbrSansAnomalie, " + "(SELECT COUNT(IKTSTA) FROM " + _getCollection()
                    + "CPCRETP WHERE IKTSTA=" + CPCommunicationFiscaleRetourViewBean.CS_ENQUETE
                    + " AND A.IWRJOU=IWRJOU) AS nbrEnEnquete, " + "(SELECT COUNT(IKTSTA) FROM " + _getCollection()
                    + "CPCRETP WHERE IKTSTA=" + CPCommunicationFiscaleRetourViewBean.CS_VALIDE
                    + " AND A.IWRJOU=IWRJOU) AS nbrValide, " + "(SELECT COUNT(IKTSTA) FROM " + _getCollection()
                    + "CPCRETP WHERE A.IWRJOU=IWRJOU) AS nbrTotal";
        } else if (!JadeStringUtil.isBlankOrZero(getForAnneeComFisc())
                && JadeStringUtil.isBlankOrZero(getForGenreAffilie())) {
            return " IWRJOU, " + "(SELECT COUNT(IKTSTA) FROM " + _getCollection() + "CPCRETP WHERE IKTSTA="
                    + CPCommunicationFiscaleRetourViewBean.CS_A_CONTROLER + " AND A.IWRJOU=IWRJOU AND IKANN1="
                    + getForAnneeComFisc() + ") AS nbrAControler, " + "(SELECT COUNT(IKTSTA) FROM " + _getCollection()
                    + "CPCRETP WHERE IKTSTA=" + CPCommunicationFiscaleRetourViewBean.CS_ABANDONNE
                    + " AND A.IWRJOU=IWRJOU AND IKANN1=" + getForAnneeComFisc() + ") AS nbrAbandonne, "
                    + "(SELECT COUNT(IKTSTA) FROM " + _getCollection() + "CPCRETP WHERE IKTSTA="
                    + CPCommunicationFiscaleRetourViewBean.CS_AVERTISSEMENT + " AND A.IWRJOU=IWRJOU AND IKANN1="
                    + getForAnneeComFisc() + ") AS nbrAvertissement, " + "(SELECT COUNT(IKTSTA) FROM "
                    + _getCollection() + "CPCRETP WHERE IKTSTA=" + CPCommunicationFiscaleRetourViewBean.CS_COMPTABILISE
                    + " AND A.IWRJOU=IWRJOU AND IKANN1=" + getForAnneeComFisc() + ") AS nbrComptabilise, "
                    + "(SELECT COUNT(IKTSTA) FROM " + _getCollection() + "CPCRETP WHERE IKTSTA="
                    + CPCommunicationFiscaleRetourViewBean.CS_ERREUR + " AND A.IWRJOU=IWRJOU AND IKANN1="
                    + getForAnneeComFisc() + ") AS nbrErreur, " + "(SELECT COUNT(IKTSTA) FROM " + _getCollection()
                    + "CPCRETP WHERE IKTSTA=" + CPCommunicationFiscaleRetourViewBean.CS_RECEPTIONNE
                    + " AND A.IWRJOU=IWRJOU AND IKANN1=" + getForAnneeComFisc() + ") AS nbrReceptionne, "
                    + "(SELECT COUNT(IKTSTA) FROM " + _getCollection() + "CPCRETP WHERE IKTSTA="
                    + CPCommunicationFiscaleRetourViewBean.CS_SANS_ANOMALIE + " AND A.IWRJOU=IWRJOU AND IKANN1="
                    + getForAnneeComFisc() + ") AS nbrSansAnomalie, " + "(SELECT COUNT(IKTSTA) FROM "
                    + _getCollection() + "CPCRETP WHERE IKTSTA=" + CPCommunicationFiscaleRetourViewBean.CS_ENQUETE
                    + " AND A.IWRJOU=IWRJOU AND IKANN1=" + getForAnneeComFisc() + ") AS nbrEnEnquete, "
                    + "(SELECT COUNT(IKTSTA) FROM " + _getCollection() + "CPCRETP WHERE IKTSTA="
                    + CPCommunicationFiscaleRetourViewBean.CS_VALIDE + " AND A.IWRJOU=IWRJOU AND IKANN1="
                    + getForAnneeComFisc() + ") AS nbrValide, " + "(SELECT COUNT(IKTSTA) FROM " + _getCollection()
                    + "CPCRETP WHERE A.IWRJOU=IWRJOU AND IKANN1=" + getForAnneeComFisc() + ") AS nbrTotal";
        } else if (JadeStringUtil.isBlankOrZero(getForAnneeComFisc())
                && !JadeStringUtil.isBlankOrZero(getForGenreAffilie())) {
            return " IWRJOU, " + "(SELECT COUNT(IKTSTA) FROM " + _getCollection() + "CPCRETP WHERE IKTSTA="
                    + CPCommunicationFiscaleRetourViewBean.CS_A_CONTROLER + " AND A.IWRJOU=IWRJOU AND IKTGAF="
                    + getForGenreAffilie() + ") AS nbrAControler, " + "(SELECT COUNT(IKTSTA) FROM " + _getCollection()
                    + "CPCRETP WHERE IKTSTA=" + CPCommunicationFiscaleRetourViewBean.CS_ABANDONNE
                    + " AND A.IWRJOU=IWRJOU AND IKTGAF=" + getForGenreAffilie() + ") AS nbrAbandonne, "
                    + "(SELECT COUNT(IKTSTA) FROM " + _getCollection() + "CPCRETP WHERE IKTSTA="
                    + CPCommunicationFiscaleRetourViewBean.CS_AVERTISSEMENT + " AND A.IWRJOU=IWRJOU AND IKTGAF="
                    + getForGenreAffilie() + ") AS nbrAvertissement, " + "(SELECT COUNT(IKTSTA) FROM "
                    + _getCollection() + "CPCRETP WHERE IKTSTA=" + CPCommunicationFiscaleRetourViewBean.CS_COMPTABILISE
                    + " AND A.IWRJOU=IWRJOU AND IKTGAF=" + getForGenreAffilie() + ") AS nbrComptabilise, "
                    + "(SELECT COUNT(IKTSTA) FROM " + _getCollection() + "CPCRETP WHERE IKTSTA="
                    + CPCommunicationFiscaleRetourViewBean.CS_ERREUR + " AND A.IWRJOU=IWRJOU AND IKTGAF="
                    + getForGenreAffilie() + ") AS nbrErreur, " + "(SELECT COUNT(IKTSTA) FROM " + _getCollection()
                    + "CPCRETP WHERE IKTSTA=" + CPCommunicationFiscaleRetourViewBean.CS_RECEPTIONNE
                    + " AND A.IWRJOU=IWRJOU AND IKTGAF=" + getForGenreAffilie() + ") AS nbrReceptionne, "
                    + "(SELECT COUNT(IKTSTA) FROM " + _getCollection() + "CPCRETP WHERE IKTSTA="
                    + CPCommunicationFiscaleRetourViewBean.CS_SANS_ANOMALIE + " AND A.IWRJOU=IWRJOU AND IKTGAF="
                    + getForGenreAffilie() + ") AS nbrSansAnomalie, " + "(SELECT COUNT(IKTSTA) FROM "
                    + _getCollection() + "CPCRETP WHERE IKTSTA=" + CPCommunicationFiscaleRetourViewBean.CS_ENQUETE
                    + " AND A.IWRJOU=IWRJOU AND IKTGAF=" + getForGenreAffilie() + ") AS nbrEnEnquete, "
                    + "(SELECT COUNT(IKTSTA) FROM " + _getCollection() + "CPCRETP WHERE IKTSTA="
                    + CPCommunicationFiscaleRetourViewBean.CS_VALIDE + " AND A.IWRJOU=IWRJOU AND IKTGAF="
                    + getForGenreAffilie() + ") AS nbrValide, " + "(SELECT COUNT(IKTSTA) FROM " + _getCollection()
                    + "CPCRETP WHERE A.IWRJOU=IWRJOU AND IKTGAF=" + getForGenreAffilie() + ") AS nbrTotal";
        } else {
            return " IWRJOU, " + "(SELECT COUNT(IKTSTA) FROM " + _getCollection() + "CPCRETP WHERE IKTSTA="
                    + CPCommunicationFiscaleRetourViewBean.CS_A_CONTROLER + " AND A.IWRJOU=IWRJOU AND IKTGAF="
                    + getForGenreAffilie() + " AND IKANN1=" + getForAnneeComFisc() + ") AS nbrAControler, "
                    + "(SELECT COUNT(IKTSTA) FROM " + _getCollection() + "CPCRETP WHERE IKTSTA="
                    + CPCommunicationFiscaleRetourViewBean.CS_ABANDONNE + " AND A.IWRJOU=IWRJOU AND IKTGAF="
                    + getForGenreAffilie() + " AND IKANN1=" + getForAnneeComFisc() + ") AS nbrAbandonne, "
                    + "(SELECT COUNT(IKTSTA) FROM " + _getCollection() + "CPCRETP WHERE IKTSTA="
                    + CPCommunicationFiscaleRetourViewBean.CS_AVERTISSEMENT + " AND A.IWRJOU=IWRJOU AND IKTGAF="
                    + getForGenreAffilie() + " AND IKANN1=" + getForAnneeComFisc() + ") AS nbrAvertissement, "
                    + "(SELECT COUNT(IKTSTA) FROM " + _getCollection() + "CPCRETP WHERE IKTSTA="
                    + CPCommunicationFiscaleRetourViewBean.CS_COMPTABILISE + " AND A.IWRJOU=IWRJOU AND IKTGAF="
                    + getForGenreAffilie() + " AND IKANN1=" + getForAnneeComFisc() + ") AS nbrComptabilise, "
                    + "(SELECT COUNT(IKTSTA) FROM " + _getCollection() + "CPCRETP WHERE IKTSTA="
                    + CPCommunicationFiscaleRetourViewBean.CS_ERREUR + " AND A.IWRJOU=IWRJOU AND IKTGAF="
                    + getForGenreAffilie() + " AND IKANN1=" + getForAnneeComFisc() + ") AS nbrErreur, "
                    + "(SELECT COUNT(IKTSTA) FROM " + _getCollection() + "CPCRETP WHERE IKTSTA="
                    + CPCommunicationFiscaleRetourViewBean.CS_RECEPTIONNE + " AND A.IWRJOU=IWRJOU AND IKTGAF="
                    + getForGenreAffilie() + " AND IKANN1=" + getForAnneeComFisc() + ") AS nbrReceptionne, "
                    + "(SELECT COUNT(IKTSTA) FROM " + _getCollection() + "CPCRETP WHERE IKTSTA="
                    + CPCommunicationFiscaleRetourViewBean.CS_SANS_ANOMALIE + " AND A.IWRJOU=IWRJOU AND IKTGAF="
                    + getForGenreAffilie() + " AND IKANN1=" + getForAnneeComFisc() + ") AS nbrSansAnomalie, "
                    + "(SELECT COUNT(IKTSTA) FROM " + _getCollection() + "CPCRETP WHERE IKTSTA="
                    + CPCommunicationFiscaleRetourViewBean.CS_ENQUETE + " AND A.IWRJOU=IWRJOU AND IKTGAF="
                    + getForGenreAffilie() + " AND IKANN1=" + getForAnneeComFisc() + ") AS nbrEnEnquete, "
                    + "(SELECT COUNT(IKTSTA) FROM " + _getCollection() + "CPCRETP WHERE IKTSTA="
                    + CPCommunicationFiscaleRetourViewBean.CS_VALIDE + " AND A.IWRJOU=IWRJOU AND IKTGAF="
                    + getForGenreAffilie() + " AND IKANN1=" + getForAnneeComFisc() + ") AS nbrValide, "
                    + "(SELECT COUNT(IKTSTA) FROM " + _getCollection() + "CPCRETP WHERE A.IWRJOU=IWRJOU AND IKTGAF="
                    + getForGenreAffilie() + " AND IKANN1=" + getForAnneeComFisc() + ") AS nbrTotal";
        }

        // select iwrjou,
        // (select count(iktsta) from webavsciam.CPCRETP where iktsta=612001 and
        // a.iwrjou=iwrjou) as nbrAControler,
        // (select count(iktsta) from webavsciam.CPCRETP where iktsta=612005 and
        // a.iwrjou=iwrjou) as nbrAbandonne,
        // (select count(iktsta) from webavsciam.CPCRETP where iktsta=612008 and
        // a.iwrjou=iwrjou) as nbrAvertissement,
        // (select count(iktsta) from webavsciam.CPCRETP where iktsta=612007 and
        // a.iwrjou=iwrjou) as nbrComptabilise,
        // (select count(iktsta) from webavsciam.CPCRETP where iktsta=612003 and
        // a.iwrjou=iwrjou) as nbrErreur,
        // (select count(iktsta) from webavsciam.CPCRETP where iktsta=612002 and
        // a.iwrjou=iwrjou) as nbrReceptionne,
        // (select count(iktsta) from webavsciam.CPCRETP where iktsta=612006 and
        // a.iwrjou=iwrjou) as nbrSansAnomalie,
        // (select count(iktsta) from webavsciam.CPCRETP where iktsta=612009 and
        // a.iwrjou=iwrjou) as nbrEnEnquete,
        // (select count(iktsta) from webavsciam.CPCRETP where iktsta=612004 and
        // a.iwrjou=iwrjou) as nbrValide,
        // (select count(iktsta) from webavsciam.CPCRETP where a.iwrjou=iwrjou)
        // as nbrTotal
        // from webavsciam.CPCRETP AS a group by iwrjou order by iwrjou desc
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CPCRETP AS A";
    }

    /**
     * Renvoie la composante de tri de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return " IWRJOU DESC";
    }

    @Override
    protected String _getWhere(BStatement statement) {
        changeManagerSize(BManager.SIZE_NOLIMIT);
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getForNumJournal())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IWRJOU=" + _dbWriteNumeric(statement.getTransaction(), getForNumJournal());
        } else {
            // Pour que s'il y a pas de condition where que la requête
            // fonctionne quand-même.
            sqlWhere = "1=1";
        }
        return sqlWhere + " GROUP BY IWRJOU";
    }

    /**
     * Crée une nouvelle entité.
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CPAvancementJournauxCF();
    }

    public String getForAnneeComFisc() {
        return forAnneeComFisc;
    }

    public String getForGenreAffilie() {
        return forGenreAffilie;
    }

    public String getForNumJournal() {
        return forNumJournal;
    }

    public String getTotalAbandonne() {
        return totalAbandonne;
    }

    public String getTotalAControler() {
        return totalAControler;
    }

    public String getTotalAvertissement() {
        return totalAvertissement;
    }

    public String getTotalComptabilise() {
        return totalComptabilise;
    }

    public String getTotalEnEnquete() {
        return totalEnEnquete;
    }

    public String getTotalErreur() {
        return totalErreur;
    }

    public String getTotalReceptionne() {
        return totalReceptionne;
    }

    public String getTotalSansAnomalie() {
        return totalSansAnomalie;
    }

    public String getTotalTotal() {
        return totalTotal;
    }

    public String getTotalValide() {
        return totalValide;
    }

    public void getTousLesTotaux(BSession session) {
        int totalAbandonne = 0;
        int totalAControler = 0;
        int totalAvertissement = 0;
        int totalComptabilise = 0;
        int totalErreur = 0;
        int totalReceptionne = 0;
        int totalSansAnomalie = 0;
        int totalEnEnquete = 0;
        int totalValide = 0;
        int totalTotal = 0;
        CPAvancementJournauxCFManager manaTotaux = new CPAvancementJournauxCFManager();
        manaTotaux.setSession(session);
        manaTotaux.setForAnneeComFisc(getForAnneeComFisc());
        manaTotaux.setForGenreAffilie(getForGenreAffilie());
        manaTotaux.setForNumJournal(getForNumJournal());
        try {
            manaTotaux.find();
            if (manaTotaux.size() > 0) {
                for (int i = 0; i < manaTotaux.size(); i++) {
                    totalAbandonne = totalAbandonne
                            + JadeStringUtil
                                    .toInt(((CPAvancementJournauxCF) manaTotaux.getEntity(i)).getNbrAbandonne());
                    totalAControler = totalAControler
                            + JadeStringUtil.toInt(((CPAvancementJournauxCF) manaTotaux.getEntity(i))
                                    .getNbrAControler());
                    totalAvertissement = totalAvertissement
                            + JadeStringUtil.toInt(((CPAvancementJournauxCF) manaTotaux.getEntity(i))
                                    .getNbrAvertissement());
                    totalComptabilise = totalComptabilise
                            + JadeStringUtil.toInt(((CPAvancementJournauxCF) manaTotaux.getEntity(i))
                                    .getNbrComptabilise());
                    totalErreur = totalErreur
                            + JadeStringUtil.toInt(((CPAvancementJournauxCF) manaTotaux.getEntity(i)).getNbrErreur());
                    ;
                    totalReceptionne = totalReceptionne
                            + JadeStringUtil.toInt(((CPAvancementJournauxCF) manaTotaux.getEntity(i))
                                    .getNbrReceptionne());
                    totalSansAnomalie = totalSansAnomalie
                            + JadeStringUtil.toInt(((CPAvancementJournauxCF) manaTotaux.getEntity(i))
                                    .getNbrSansAnomalie());
                    totalEnEnquete = totalEnEnquete
                            + JadeStringUtil
                                    .toInt(((CPAvancementJournauxCF) manaTotaux.getEntity(i)).getNbrEnEnquete());
                    totalValide = totalValide
                            + JadeStringUtil.toInt(((CPAvancementJournauxCF) manaTotaux.getEntity(i)).getNbrValide());
                    totalTotal = totalTotal
                            + JadeStringUtil.toInt(((CPAvancementJournauxCF) manaTotaux.getEntity(i)).getNbrTotal());
                }

                setTotalAbandonne("" + totalAbandonne);
                setTotalAControler("" + totalAControler);
                setTotalAvertissement("" + totalAvertissement);
                setTotalComptabilise("" + totalComptabilise);
                setTotalErreur("" + totalErreur);
                setTotalReceptionne("" + totalReceptionne);
                setTotalSansAnomalie("" + totalSansAnomalie);
                setTotalEnEnquete("" + totalEnEnquete);
                setTotalValide("" + totalValide);
                setTotalTotal("" + totalTotal);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setForAnneeComFisc(String forAnneeComFisc) {
        this.forAnneeComFisc = forAnneeComFisc;
    }

    public void setForGenreAffilie(String forGenreAffilie) {
        this.forGenreAffilie = forGenreAffilie;
    }

    public void setForNumJournal(String forNumJournal) {
        this.forNumJournal = forNumJournal;
    }

    public void setTotalAbandonne(String totalAbandonne) {
        this.totalAbandonne = totalAbandonne;
    }

    public void setTotalAControler(String totalAControler) {
        this.totalAControler = totalAControler;
    }

    public void setTotalAvertissement(String totalAvertissement) {
        this.totalAvertissement = totalAvertissement;
    }

    public void setTotalComptabilise(String totalComptabilise) {
        this.totalComptabilise = totalComptabilise;
    }

    public void setTotalEnEnquete(String totalEnEnquete) {
        this.totalEnEnquete = totalEnEnquete;
    }

    public void setTotalErreur(String totalErreur) {
        this.totalErreur = totalErreur;
    }

    public void setTotalReceptionne(String totalReceptionne) {
        this.totalReceptionne = totalReceptionne;
    }

    public void setTotalSansAnomalie(String totalSansAnomalie) {
        this.totalSansAnomalie = totalSansAnomalie;
    }

    public void setTotalTotal(String totalTotal) {
        this.totalTotal = totalTotal;
    }

    public void setTotalValide(String totalValide) {
        this.totalValide = totalValide;
    }
}
