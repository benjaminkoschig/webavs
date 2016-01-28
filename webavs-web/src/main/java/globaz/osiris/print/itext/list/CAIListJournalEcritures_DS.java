package globaz.osiris.print.itext.list;

import globaz.globall.db.BProcess;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.db.comptes.CAEcriture;
import globaz.osiris.db.comptes.CAEcritureManager;
import globaz.osiris.db.comptes.CAOperation;
import java.math.BigDecimal;
import java.util.Iterator;
import net.sf.jasperreports.engine.JRCloneableDataSource;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (03.06.2003 15:56:05)
 * 
 * @author: Administrator
 */
public class CAIListJournalEcritures_DS extends CAEcritureManager implements JRCloneableDataSource {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static int TYPE_CC = 2;
    public final static int TYPE_NORMAL = 0;
    public final static int TYPE_RUBRIQUE = 1;
    private int _index = 0;

    private java.math.BigDecimal avoirCompteCourant;
    private java.math.BigDecimal avoirRubrique;
    private java.math.BigDecimal base;
    private java.lang.String compteAnnexe = null;
    private Iterator container = null;
    private java.math.BigDecimal credit;
    private java.math.BigDecimal debit;
    private java.math.BigDecimal doitCompteCourant;
    private java.math.BigDecimal doitRubrique;
    private CAEcriture entity = new CAEcriture();
    private BProcess process;
    private int showDetail = 0;
    private BigDecimal totalCompteCourant = null;
    private BigDecimal totalRubrique = null;
    private int typeSource = 0;

    /**
     * Commentaire relatif au constructeur CAIListJournalEcritures_DS.
     */
    public CAIListJournalEcritures_DS() {
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
     * Cette m�thode r�cup�re la base
     * 
     * @return java.math.BigDecimal
     */
    private BigDecimal _getBase() {
        try {
            base = new BigDecimal(entity.getMasse());
        } catch (Exception e) {
            base = new BigDecimal(0);
        }
        return base;
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
            compteAnnexe = compteAnnexe + ", " + entity.getSection().getFullDescription();
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
        credit = new BigDecimal(0);
        try {
            if (entity.getCodeDebitCredit().equals(APIEcriture.CREDIT)
                    || entity.getCodeDebitCredit().equals(APIEcriture.EXTOURNE_CREDIT)) {
                credit = credit.subtract(new BigDecimal(entity.getMontant()));
            } else {
                credit = new BigDecimal(0);
            }
        } catch (Exception e) {
            credit = new BigDecimal(0);
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
                debit = new BigDecimal(entity.getMontant());
            } else {
                debit = new BigDecimal(0);
            }
        } catch (Exception e) {
            debit = new BigDecimal(0);
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
     * Surcharge : Cr�� le 04.10.2006
     * 
     * @param statement
     * @return
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        String _order = "";

        _order = super._getOrder(statement);

        if (JadeStringUtil.isEmpty(_order)) {
            _order = _getCollection() + "CACPTAP.IDEXTERNEROLE, " + _getCollection() + "CACPTAP.IDROLE, "
                    + _getCollection() + "CASECTP.IDEXTERNE, " + _getCollection() + "CACPTCP.IDEXTERNE, "
                    + _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_DATE + ", "
                    + _getCollection() + "CARUBRP.IDEXTERNE";
        }

        return _order;
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
        // COL_2 --> Compte courant
        if (jrField.getName().equals("COL_2")) {
            if (entity.getCompteCourant() != null) {
                return entity.getCompteCourant().getIdExterne();
            } else {
                return getSession().getLabel("INCONNU");
            }
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
        // COL_6 --> Base
        if (jrField.getName().equals("COL_6")) {
            return _getBase();
        }
        // COL_7 --> D�bit
        if (jrField.getName().equals("COL_7")) {
            return _getDebit();
        }
        // COL_8 --> Cr�dit
        if (jrField.getName().equals("COL_8")) {
            return _getCredit();
        }
        // COL_9 --> Solde
        if (jrField.getName().equals("COL_9")) {
            return new BigDecimal(entity.getMontant());
        }

        // COL_10 --> Rubrique / Compte courant
        if (jrField.getName().equals("COL_10")) {
            if (getTypeSource() == CAIListJournalEcritures_DS.TYPE_RUBRIQUE) {
                return entity.getCompte().getIdExterne() + " " + entity.getCompte().getDescription();
            } else if (getTypeSource() == CAIListJournalEcritures_DS.TYPE_CC) {
                if (entity.getCompteCourant() != null) {
                    return entity.getCompteCourant().getIdExterne() + " "
                            + entity.getCompteCourant().getRubrique().getDescription();
                } else {
                    return getSession().getLabel("INCONNU");
                }
            }
        }

        // COL_11 --> Date
        if (jrField.getName().equals("COL_11")) {
            return entity.getDate();
        }

        setDoitAvoir();

        // COL_12 --> Doit
        if (jrField.getName().equals("COL_12")) {
            if (getTypeSource() == CAIListJournalEcritures_DS.TYPE_RUBRIQUE) {
                return _getDoitRubrique();
            } else if (getTypeSource() == CAIListJournalEcritures_DS.TYPE_CC) {
                if (entity.getCompteCourant() != null) {
                    return _getDoitCompteCourant();
                } else {
                    return new BigDecimal(0);
                }
            }
        }

        // COL_13 --> Avoir
        if (jrField.getName().equals("COL_13")) {
            if (getTypeSource() == CAIListJournalEcritures_DS.TYPE_RUBRIQUE) {
                return _getAvoirRubrique();
            } else if (getTypeSource() == CAIListJournalEcritures_DS.TYPE_CC) {
                if (entity.getCompteCourant() != null) {
                    return _getAvoirCompteCourant();
                } else {
                    return new BigDecimal(0);
                }
            }
        }

        // COL_14 --> Solde
        // if(jrField.getName().equals("COL_14")){
        // return "";
        // }

        if (jrField.getName().equals("COL_15")) {
            if (getTypeSource() == CAIListJournalEcritures_DS.TYPE_RUBRIQUE) {
                return _getTotalRubrique();
            } else if (getTypeSource() == CAIListJournalEcritures_DS.TYPE_CC) {
                return _getTotalCompteCourant();
            }
        }

        // COL_15 et COL_17 --> Total
        /*
         * if (getTypeSource() == CAIListJournalEcritures_DS.TYPE_RUBRIQUE) { if (jrField.getName().equals("COL_15")){
         * return _getTotalRubrique(); } } else if (getTypeSource() == CAIListJournalEcritures_DS.TYPE_CC) { if
         * (jrField.getName().equals("COL_17")){ return _getTotalCompteCourant(); } }
         */
        return "";
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
                entity = (CAEcriture) container.next();
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
        if (getTypeSource() == CAIListJournalEcritures_DS.TYPE_RUBRIQUE) {
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
        } else if (getTypeSource() == CAIListJournalEcritures_DS.TYPE_CC) {
            // Compte courant
            if (entity.getCodeDebitCredit().equals(APIEcriture.DEBIT)
                    || entity.getCodeDebitCredit().equals(APIEcriture.EXTOURNE_DEBIT)) {
                doitCompteCourant = new BigDecimal(entity.getMontant());
                avoirCompteCourant = new BigDecimal(0);
            } else {
                BigDecimal zero = new BigDecimal(0);
                avoirCompteCourant = new BigDecimal(zero.subtract(new BigDecimal(entity.getMontant())).toString());
                doitCompteCourant = new BigDecimal(0);
            }
            totalCompteCourant = new BigDecimal(entity.getMontant());
        }

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
