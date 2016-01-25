package globaz.phenix.db.communications;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import java.io.Serializable;

public class CPAvancementJournauxCF extends BEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_NBR_ABANDONNE = "nbrAbandonne";
    public static final String FIELDNAME_NBR_ACONTROLER = "nbrAControler";
    public static final String FIELDNAME_NBR_AVERTISSEMENT = "nbrAvertissement";
    public static final String FIELDNAME_NBR_COMPTABILISE = "nbrComptabilise";
    public static final String FIELDNAME_NBR_ENENQUETE = "nbrEnEnquete";
    public static final String FIELDNAME_NBR_ERREUR = "nbrErreur";
    public static final String FIELDNAME_NBR_RECEPTIONNE = "nbrReceptionne";
    public static final String FIELDNAME_NBR_SANSANOMALIE = "nbrSansAnomalie";
    public static final String FIELDNAME_NBR_TOTAL = "nbrTotal";
    public static final String FIELDNAME_NBR_VALIDE = "nbrValide";
    public static final String FIELDNAME_NUM_JOURNAL = "IWRJOU";

    private String nbrAbandonne = new String();
    private String nbrAControler = new String();
    private String nbrAvertissement = new String();
    private String nbrComptabilise = new String();
    private String nbrEnEnquete = new String();
    private String nbrErreur = new String();
    private String nbrReceptionne = new String();
    private String nbrSansAnomalie = new String();
    private String nbrTotal = new String();
    // private String totalAControler = new String();
    // private String totalAbandonne = new String();
    // private String totalAvertissement = new String();
    // private String totalComptabilise = new String();
    // private String totalErreur = new String();
    // private String totalReceptionne = new String();
    // private String totalSansAnomalie = new String();
    // private String totalEnEnquete = new String();
    // private String totalValide = new String();
    // private String totalTotal = new String();
    private String nbrValide = new String();
    // DB
    // Fields
    private String numJouranl = new String();

    /**
     * Constructeur de AFAffiliation
     */
    public CPAvancementJournauxCF() {
        super();
    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return null;
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        numJouranl = statement.dbReadNumeric(FIELDNAME_NUM_JOURNAL);
        nbrAControler = statement.dbReadNumeric(FIELDNAME_NBR_ACONTROLER);
        nbrAbandonne = statement.dbReadNumeric(FIELDNAME_NBR_ABANDONNE);
        nbrAvertissement = statement.dbReadNumeric(FIELDNAME_NBR_AVERTISSEMENT);
        nbrComptabilise = statement.dbReadNumeric(FIELDNAME_NBR_COMPTABILISE);
        nbrErreur = statement.dbReadNumeric(FIELDNAME_NBR_ERREUR);
        nbrReceptionne = statement.dbReadNumeric(FIELDNAME_NBR_RECEPTIONNE);
        nbrSansAnomalie = statement.dbReadNumeric(FIELDNAME_NBR_SANSANOMALIE);
        nbrEnEnquete = statement.dbReadNumeric(FIELDNAME_NBR_ENENQUETE);
        nbrValide = statement.dbReadNumeric(FIELDNAME_NBR_VALIDE);
        nbrTotal = statement.dbReadNumeric(FIELDNAME_NBR_TOTAL);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    /**
     * Renvoie le Manager de l'entité.
     * 
     * @return
     */
    protected BManager getManager() {
        return new CPAvancementJournauxCFManager();
    }

    public String getNbrAbandonne() {
        return nbrAbandonne;
    }

    public String getNbrAControler() {
        return nbrAControler;
    }

    public String getNbrAvertissement() {
        return nbrAvertissement;
    }

    public String getNbrComptabilise() {
        return nbrComptabilise;
    }

    public String getNbrEnEnquete() {
        return nbrEnEnquete;
    }

    public String getNbrErreur() {
        return nbrErreur;
    }

    public String getNbrReceptionne() {
        return nbrReceptionne;
    }

    public String getNbrSansAnomalie() {
        return nbrSansAnomalie;
    }

    public String getNbrTotal() {
        return nbrTotal;
    }

    public String getNbrValide() {
        return nbrValide;
    }

    // public void getTousLesTotaux(BSession session, String annee, String
    // genre, String numJournal){
    // int totalAbandonne=0;
    // int totalAControler=0;
    // int totalAvertissement=0;
    // int totalComptabilise=0;
    // int totalErreur=0;
    // int totalReceptionne=0;
    // int totalSansAnomalie=0;
    // int totalEnEnquete=0;
    // int totalValide=0;
    // int totalTotal=0;
    // CPAvancementJournauxCFManager manaTotaux = new
    // CPAvancementJournauxCFManager();
    // manaTotaux.setSession(session);
    // manaTotaux.setForAnneeComFisc(annee);
    // manaTotaux.setForGenreAffilie(genre);
    // manaTotaux.setForNumJournal(numJournal);
    // try {
    // manaTotaux.find();
    // if(manaTotaux.size()>0){
    // for(int i=0;i<manaTotaux.size();i++){
    // totalAbandonne=totalAbandonne +
    // JadeStringUtil.toInt(((CPAvancementJournauxCF)manaTotaux.getEntity(i)).getNbrAbandonne());
    // totalAControler=totalAControler +
    // JadeStringUtil.toInt(((CPAvancementJournauxCF)manaTotaux.getEntity(i)).getNbrAControler());
    // totalAvertissement=totalAvertissement +
    // JadeStringUtil.toInt(((CPAvancementJournauxCF)manaTotaux.getEntity(i)).getNbrAvertissement());
    // totalComptabilise=totalComptabilise +
    // JadeStringUtil.toInt(((CPAvancementJournauxCF)manaTotaux.getEntity(i)).getNbrComptabilise());
    // totalErreur=totalErreur +
    // JadeStringUtil.toInt(((CPAvancementJournauxCF)manaTotaux.getEntity(i)).getNbrErreur());;
    // totalReceptionne=totalReceptionne +
    // JadeStringUtil.toInt(((CPAvancementJournauxCF)manaTotaux.getEntity(i)).getNbrReceptionne());
    // totalSansAnomalie=totalSansAnomalie +
    // JadeStringUtil.toInt(((CPAvancementJournauxCF)manaTotaux.getEntity(i)).getNbrSansAnomalie());
    // totalEnEnquete=totalEnEnquete +
    // JadeStringUtil.toInt(((CPAvancementJournauxCF)manaTotaux.getEntity(i)).getNbrEnEnquete());
    // totalValide=totalValide +
    // JadeStringUtil.toInt(((CPAvancementJournauxCF)manaTotaux.getEntity(i)).getNbrValide());
    // totalTotal=totalTotal +
    // JadeStringUtil.toInt(((CPAvancementJournauxCF)manaTotaux.getEntity(i)).getNbrTotal());
    // }
    //
    // setTotalAbandonne(""+totalAbandonne);
    // setTotalAControler(""+totalAControler);
    // setTotalAvertissement(""+totalAvertissement);
    // setTotalComptabilise(""+totalComptabilise);
    // setTotalErreur(""+totalErreur);
    // setTotalReceptionne(""+totalReceptionne);
    // setTotalSansAnomalie(""+totalSansAnomalie);
    // setTotalEnEnquete(""+totalEnEnquete);
    // setTotalValide(""+totalValide);
    // setTotalTotal(""+totalTotal);
    // }
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    //
    public String getNumJouranl() {
        return numJouranl;
    }

    public void setNbrAbandonne(String nbrAbandonne) {
        this.nbrAbandonne = nbrAbandonne;
    }

    public void setNbrAControler(String nbrAControler) {
        this.nbrAControler = nbrAControler;
    }

    public void setNbrAvertissement(String nbrAvertissement) {
        this.nbrAvertissement = nbrAvertissement;
    }

    public void setNbrComptabilise(String nbrComptabilise) {
        this.nbrComptabilise = nbrComptabilise;
    }

    public void setNbrEnEnquete(String nbrEnEnquete) {
        this.nbrEnEnquete = nbrEnEnquete;
    }

    public void setNbrErreur(String nbrErreur) {
        this.nbrErreur = nbrErreur;
    }

    public void setNbrReceptionne(String nbrReceptionne) {
        this.nbrReceptionne = nbrReceptionne;
    }

    public void setNbrSansAnomalie(String nbrSansAnomalie) {
        this.nbrSansAnomalie = nbrSansAnomalie;
    }

    public void setNbrTotal(String nbrTotal) {
        this.nbrTotal = nbrTotal;
    }

    public void setNbrValide(String nbrValide) {
        this.nbrValide = nbrValide;
    }

    public void setNumJouranl(String numJouranl) {
        this.numJouranl = numJouranl;
    }
}
