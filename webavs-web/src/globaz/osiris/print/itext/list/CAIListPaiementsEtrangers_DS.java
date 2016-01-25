package globaz.osiris.print.itext.list;

import globaz.globall.db.BProcess;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.db.comptes.CAPaiementEtranger;
import globaz.osiris.db.comptes.CAPaiementEtrangerManager;
import java.math.BigDecimal;
import java.util.Iterator;
import net.sf.jasperreports.engine.JRCloneableDataSource;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (03.06.2003 15:56:05)
 * 
 * @author: Administrator
 */
public class CAIListPaiementsEtrangers_DS extends CAPaiementEtrangerManager implements JRCloneableDataSource {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static int TYPE_MONNAIE = 2;
    public final static int TYPE_NORMAL = 0;
    public final static int TYPE_RUBRIQUE = 1;
    private int _index = 0;

    private java.math.BigDecimal avoirCompteCourant;
    private java.math.BigDecimal avoirRubrique;
    private java.lang.String compteAnnexe = null;
    private Iterator container = null;
    private java.math.BigDecimal credit;
    private java.math.BigDecimal debit;
    private java.math.BigDecimal doitCompteCourant;
    private java.math.BigDecimal doitRubrique;
    private CAPaiementEtranger entity = new CAPaiementEtranger();
    private BProcess process;
    private int showDetail = 0;
    private BigDecimal totalCompteCourant = null;
    private BigDecimal totalRubrique = null;
    private int typeSource = 0;

    /**
     * Commentaire relatif au constructeur CAIListPaiementsEtrangers_DS.
     */
    public CAIListPaiementsEtrangers_DS() {
        super();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.06.2003 08:14:14)
     * 
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal _getAvoirCompteCourant() {
        return avoirCompteCourant;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.06.2003 08:14:14)
     * 
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal _getAvoirRubrique() {
        return avoirRubrique;
    }

    /**
     * Cette m�thode r�cup�re la description du compte annexe
     * 
     * @return java.lang.String
     */
    private String _getCompteAnnexe() {
        try {
            compteAnnexe = entity.getCompteAnnexe().getRole().getDescription();
            compteAnnexe = compteAnnexe + " " + entity.getCompteAnnexe().getIdExterneRole();
            compteAnnexe = compteAnnexe + " " + entity.getCompteAnnexe().getTiers().getNom();
            // compteAnnexe = compteAnnexe + ", " +
            // entity.getSection().getFullDescription();
        } catch (Exception e) {
            compteAnnexe = null;
        }
        return compteAnnexe;
    }

    /**
     * Cette m�thode r�cup�re le montant au cr�dit
     * 
     * @return java.lang.String
     */
    private BigDecimal _getCredit() {
        float fMontant = 0f;
        try {
            fMontant = Float.parseFloat(entity.getMontant());
        } catch (Exception e) {
        }
        if (entity.getCodeDebitCredit().equals(APIEcriture.CREDIT)
                || entity.getCodeDebitCredit().equals(APIEcriture.EXTOURNE_CREDIT)) {
            credit = new java.math.BigDecimal(String.valueOf(-fMontant));
        } else {
            credit = new java.math.BigDecimal(0);
        }
        return credit;
    }

    /**
     * Cette m�thode r�cup�re le montant au d�bit
     * 
     * @return java.lang.String
     */
    private BigDecimal _getDebit() {
        try {
            if (entity.getCodeDebitCredit().equals(APIEcriture.DEBIT)
                    || entity.getCodeDebitCredit().equals(APIEcriture.EXTOURNE_DEBIT)) {
                debit = new java.math.BigDecimal(entity.getMontant());
            } else {
                debit = new java.math.BigDecimal(0);
            }
        } catch (Exception e) {
            debit = null;
        }
        return debit;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.06.2003 08:13:47)
     * 
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal _getDoitCompteCourant() {
        return doitCompteCourant;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.06.2003 08:13:47)
     * 
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal _getDoitRubrique() {
        return doitRubrique;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.06.2003 07:20:14)
     * 
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal _getTotalCompteCourant() {
        return totalCompteCourant;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.06.2003 07:19:29)
     * 
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal _getTotalRubrique() {
        return totalRubrique;
    }

    /**
     * Copiez la m�thode tel quel, permet la copy de l'objet Date de cr�ation : (01.04.2003 14:45:18)
     * 
     * @return java.lang.Object
     * @exception java.lang.CloneNotSupportedException
     *                La description de l'exception.
     */
    @Override
    public Object clone() throws java.lang.CloneNotSupportedException {
        return super.clone();
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.jasperreports.engine.JRCloneableDataSource#getContext()
     */
    public BProcess getContext() {
        return process;
    }

    /**
     * Appelle chaque champ du mod�le
     */
    @Override
    public Object getFieldValue(net.sf.jasperreports.engine.JRField jrField)
            throws net.sf.jasperreports.engine.JRException {
        // Retourne chaque champ
        if (jrField.getName().equals("COL_ID")) {
            return new Integer(_index);
        }
        // COL_1 --> Compte annexe
        if (jrField.getName().equals("COL_1")) {
            return _getCompteAnnexe();
        }
        // COL_3 --> Date
        if (jrField.getName().equals("COL_3")) {
            return entity.getDate();
        }
        // COL_4 --> Rubrique
        if (jrField.getName().equals("COL_4")) {
            return entity.getCompte().getIdExterne();
        }
        // COL_5 --> Libell�
        if (jrField.getName().equals("COL_5")) {
            if (!JadeStringUtil.isBlank(entity.getLibelle())) {
                return entity.getLibelle();
            } else {
                return entity.getCompte().getDescription();
            }
        }

        // COL_6 --> Montant ME, ISO, Cours
        if (jrField.getName().equals("COL_6")) {
            return entity.getMontantME() + ", " + entity.getCodeIsoME() + ", " + entity.getCoursME();
        }

        // COL_7 --> D�bit
        if (jrField.getName().equals("COL_7")) {
            return _getDebit();
        }
        // COL_8 --> Cr�dit
        if (jrField.getName().equals("COL_8")) {
            return _getCredit();
        }

        /*
         * Sub report 1
         */

        // COL_10 --> Rubrique
        if (jrField.getName().equals("COL_10")) {
            return entity.getCompte().getIdExterne() + " " + entity.getCompte().getDescription();
        }

        // COL_11 --> Montant �tranger
        if (jrField.getName().equals("COL_11")) {
            return new BigDecimal(entity.getMontantME());
        }

        // COL_16 --> Code ISO
        if (jrField.getName().equals("COL_16")) {
            return entity.getCodeIsoME();
        }

        setDoitAvoir();

        // COL_12 --> Doit
        if (jrField.getName().equals("COL_12")) {
            return _getDoitRubrique();
        }

        // COL_13 --> Avoir
        if (jrField.getName().equals("COL_13")) {
            return _getAvoirRubrique();
        }

        // COL_15 --> Total
        if (jrField.getName().equals("COL_15")) {
            return _getTotalRubrique();
        }

        /*
         * Sub report 2
         */

        // COL_19 --> TOTAL CHF
        if (jrField.getName().equals("COL_19")) {
            return new BigDecimal(entity.getMontant());
        }

        return null;
    }

    /**
     * Cette m�thode permet de savoir si l'impression d�taill�e (true) ou non (false) a �t� s�lectionn�e. Date de
     * cr�ation : (02.06.2003 07:35:08)
     * 
     * @return boolean
     */
    public int getShowDetail() {
        return showDetail;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (04.06.2003 09:09:47)
     * 
     * @return int
     */
    public int getTypeSource() {
        return typeSource;
    }

    /**
	 *
	 */
    @Override
    public boolean next() throws net.sf.jasperreports.engine.JRException {
        entity = null;
        try {
            if (container == null) {
                this.find(0);
                container = getContainer().iterator();
            }
            // lit le nouveau entity
            if (container.hasNext()) {
                entity = (CAPaiementEtranger) container.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // vraix : il existe une entity, faux fin du select
        if (process != null) {
            return ((entity != null) && !process.isAborted());
        } else {
            return (entity != null);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.jasperreports.engine.JRCloneableDataSource#setContext(BProcess)
     */
    public void setContext(BProcess newProcess) {
        process = newProcess;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.06.2003 10:00:47)
     */
    public void setDoitAvoir() {
        // Rubrique
        if (entity.getCodeDebitCredit().equals(APIEcriture.DEBIT)
                || entity.getCodeDebitCredit().equals(APIEcriture.EXTOURNE_DEBIT)) {
            avoirRubrique = new BigDecimal(entity.getMontant());
            doitRubrique = new BigDecimal(0);
        } else {
            BigDecimal zero = new BigDecimal(0);
            doitRubrique = new BigDecimal(zero.subtract(new BigDecimal(entity.getMontant())).toString());
            avoirRubrique = new BigDecimal(0);
        }
        BigDecimal zero = new BigDecimal(0);
        totalRubrique = new BigDecimal(zero.subtract(new BigDecimal(entity.getMontant())).toString());

    }

    /**
     * Cette m�thode permet de d�finir si on veut le d�tail des �critures ou non.
     * 
     * @param newShowDetail
     *            boolean
     */
    public void setShowDetail(int newShowDetail) {
        showDetail = newShowDetail;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (04.06.2003 09:09:47)
     * 
     * @param newTypeSource
     *            int
     */
    public void setTypeSource(int newTypeSource) {
        typeSource = newTypeSource;
    }

}
