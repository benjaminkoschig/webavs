package globaz.helios.db.classifications;

import globaz.globall.db.BManager;
import globaz.helios.db.interfaces.ITreeListable;
import globaz.jade.client.util.JadeStringUtil;

public class CGDefinitionListe extends globaz.globall.db.BEntity implements ITreeListable, java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String idClassification = new String();
    private java.lang.String idDefinitionListe = new String();
    private java.lang.String idPoliceDetail = new String();
    private java.lang.String idPoliceTitre1 = new String();
    private java.lang.String idPoliceTitre2 = new String();
    private java.lang.String idPoliceTitre3 = new String();
    private java.lang.String idPoliceTitre4 = new String();
    private java.lang.String idPoliceTitre5 = new String();
    private java.lang.String idPoliceTitre6 = new String();
    private java.lang.String idPoliceTitre7 = new String();
    private java.lang.String idPoliceTitre8 = new String();
    private java.lang.String idPoliceTitre9 = new String();
    private java.lang.String idPoliceTotal1 = new String();
    private java.lang.String idPoliceTotal2 = new String();
    private java.lang.String idPoliceTotal3 = new String();
    private java.lang.String idPoliceTotal4 = new String();
    private java.lang.String idPoliceTotal5 = new String();
    private java.lang.String idPoliceTotal6 = new String();
    private java.lang.String idPoliceTotal7 = new String();
    private java.lang.String idPoliceTotal8 = new String();
    private java.lang.String idPoliceTotal9 = new String();
    private java.lang.String libelleDe = new String();
    private java.lang.String libelleFr = new String();
    private java.lang.String libelleIt = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CGDefinitionListe
     */
    public CGDefinitionListe() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdDefinitionListe(_incCounter(transaction, "0"));

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CGDFLIP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idDefinitionListe = statement.dbReadNumeric("IDDEFINITIONLISTE");
        idClassification = statement.dbReadNumeric("IDCLASSIFICATION");
        libelleFr = statement.dbReadString("LIBELLEFR");
        libelleDe = statement.dbReadString("LIBELLEDE");
        libelleIt = statement.dbReadString("LIBELLEIT");
        idPoliceDetail = statement.dbReadNumeric("IDPOLICEDETAIL");
        idPoliceTitre1 = statement.dbReadNumeric("IDPOLICETITRE1");
        idPoliceTitre2 = statement.dbReadNumeric("IDPOLICETITRE2");
        idPoliceTitre3 = statement.dbReadNumeric("IDPOLICETITRE3");
        idPoliceTitre4 = statement.dbReadNumeric("IDPOLICETITRE4");
        idPoliceTitre5 = statement.dbReadNumeric("IDPOLICETITRE5");
        idPoliceTitre6 = statement.dbReadNumeric("IDPOLICETITRE6");
        idPoliceTitre7 = statement.dbReadNumeric("IDPOLICETITRE7");
        idPoliceTitre8 = statement.dbReadNumeric("IDPOLICETITRE8");
        idPoliceTitre9 = statement.dbReadNumeric("IDPOLICETITRE9");
        idPoliceTotal1 = statement.dbReadNumeric("IDPOLICETOTAL1");
        idPoliceTotal2 = statement.dbReadNumeric("IDPOLICETOTAL2");
        idPoliceTotal3 = statement.dbReadNumeric("IDPOLICETOTAL3");
        idPoliceTotal4 = statement.dbReadNumeric("IDPOLICETOTAL4");
        idPoliceTotal5 = statement.dbReadNumeric("IDPOLICETOTAL5");
        idPoliceTotal6 = statement.dbReadNumeric("IDPOLICETOTAL6");
        idPoliceTotal7 = statement.dbReadNumeric("IDPOLICETOTAL7");
        idPoliceTotal8 = statement.dbReadNumeric("IDPOLICETOTAL8");
        idPoliceTotal9 = statement.dbReadNumeric("IDPOLICETOTAL9");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement
                .writeKey("IDDEFINITIONLISTE", _dbWriteNumeric(statement.getTransaction(), getIdDefinitionListe(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDDEFINITIONLISTE",
                _dbWriteNumeric(statement.getTransaction(), getIdDefinitionListe(), "idDefinitionListe"));
        statement.writeField("IDCLASSIFICATION",
                _dbWriteNumeric(statement.getTransaction(), getIdClassification(), "idClassification"));
        statement.writeField("LIBELLEFR", _dbWriteString(statement.getTransaction(), getLibelleFr(), "libelleFr"));
        statement.writeField("LIBELLEDE", _dbWriteString(statement.getTransaction(), getLibelleDe(), "libelleDe"));
        statement.writeField("LIBELLEIT", _dbWriteString(statement.getTransaction(), getLibelleIt(), "libelleIt"));
        statement.writeField("IDPOLICEDETAIL",
                _dbWriteNumeric(statement.getTransaction(), getIdPoliceDetail(), "idPoliceDetail"));
        statement.writeField("IDPOLICETITRE1",
                _dbWriteNumeric(statement.getTransaction(), getIdPoliceTitre1(), "idPoliceTitre1"));
        statement.writeField("IDPOLICETITRE2",
                _dbWriteNumeric(statement.getTransaction(), getIdPoliceTitre2(), "idPoliceTitre2"));
        statement.writeField("IDPOLICETITRE3",
                _dbWriteNumeric(statement.getTransaction(), getIdPoliceTitre3(), "idPoliceTitre3"));
        statement.writeField("IDPOLICETITRE4",
                _dbWriteNumeric(statement.getTransaction(), getIdPoliceTitre4(), "idPoliceTitre4"));
        statement.writeField("IDPOLICETITRE5",
                _dbWriteNumeric(statement.getTransaction(), getIdPoliceTitre5(), "idPoliceTitre5"));
        statement.writeField("IDPOLICETITRE6",
                _dbWriteNumeric(statement.getTransaction(), getIdPoliceTitre6(), "idPoliceTitre6"));
        statement.writeField("IDPOLICETITRE7",
                _dbWriteNumeric(statement.getTransaction(), getIdPoliceTitre7(), "idPoliceTitre7"));
        statement.writeField("IDPOLICETITRE8",
                _dbWriteNumeric(statement.getTransaction(), getIdPoliceTitre8(), "idPoliceTitre8"));
        statement.writeField("IDPOLICETITRE9",
                _dbWriteNumeric(statement.getTransaction(), getIdPoliceTitre9(), "idPoliceTitre9"));
        statement.writeField("IDPOLICETOTAL1",
                _dbWriteNumeric(statement.getTransaction(), getIdPoliceTotal1(), "idPoliceTotal1"));
        statement.writeField("IDPOLICETOTAL2",
                _dbWriteNumeric(statement.getTransaction(), getIdPoliceTotal2(), "idPoliceTotal2"));
        statement.writeField("IDPOLICETOTAL3",
                _dbWriteNumeric(statement.getTransaction(), getIdPoliceTotal3(), "idPoliceTotal3"));
        statement.writeField("IDPOLICETOTAL4",
                _dbWriteNumeric(statement.getTransaction(), getIdPoliceTotal4(), "idPoliceTotal4"));
        statement.writeField("IDPOLICETOTAL5",
                _dbWriteNumeric(statement.getTransaction(), getIdPoliceTotal5(), "idPoliceTotal5"));
        statement.writeField("IDPOLICETOTAL6",
                _dbWriteNumeric(statement.getTransaction(), getIdPoliceTotal6(), "idPoliceTotal6"));
        statement.writeField("IDPOLICETOTAL7",
                _dbWriteNumeric(statement.getTransaction(), getIdPoliceTotal7(), "idPoliceTotal7"));
        statement.writeField("IDPOLICETOTAL8",
                _dbWriteNumeric(statement.getTransaction(), getIdPoliceTotal8(), "idPoliceTotal8"));
        statement.writeField("IDPOLICETOTAL9",
                _dbWriteNumeric(statement.getTransaction(), getIdPoliceTotal9(), "idPoliceTotal9"));
    }

    @Override
    public BManager[] getChilds() {
        return null;
    }

    public CGClassification getClassification() {
        if (!JadeStringUtil.isBlank(getIdClassification())) {
            CGClassification cls = new CGClassification();
            cls.setSession(getSession());
            cls.setIdClassification(getIdClassification());
            try {
                cls.retrieve();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            if (cls != null && !cls.isNew()) {
                return cls;
            }
        }

        return null;

    }

    public java.lang.String getIdClassification() {
        return idClassification;
    }

    /**
     * Getter
     */
    public java.lang.String getIdDefinitionListe() {
        return idDefinitionListe;
    }

    public java.lang.String getIdPoliceDetail() {
        return idPoliceDetail;
    }

    public java.lang.String getIdPoliceTitre1() {
        return idPoliceTitre1;
    }

    public java.lang.String getIdPoliceTitre2() {
        return idPoliceTitre2;
    }

    public java.lang.String getIdPoliceTitre3() {
        return idPoliceTitre3;
    }

    public java.lang.String getIdPoliceTitre4() {
        return idPoliceTitre4;
    }

    public java.lang.String getIdPoliceTitre5() {
        return idPoliceTitre5;
    }

    public java.lang.String getIdPoliceTitre6() {
        return idPoliceTitre6;
    }

    public java.lang.String getIdPoliceTitre7() {
        return idPoliceTitre7;
    }

    public java.lang.String getIdPoliceTitre8() {
        return idPoliceTitre8;
    }

    public java.lang.String getIdPoliceTitre9() {
        return idPoliceTitre9;
    }

    public java.lang.String getIdPoliceTotal1() {
        return idPoliceTotal1;
    }

    public java.lang.String getIdPoliceTotal2() {
        return idPoliceTotal2;
    }

    public java.lang.String getIdPoliceTotal3() {
        return idPoliceTotal3;
    }

    public java.lang.String getIdPoliceTotal4() {
        return idPoliceTotal4;
    }

    public java.lang.String getIdPoliceTotal5() {
        return idPoliceTotal5;
    }

    public java.lang.String getIdPoliceTotal6() {
        return idPoliceTotal6;
    }

    public java.lang.String getIdPoliceTotal7() {
        return idPoliceTotal7;
    }

    public java.lang.String getIdPoliceTotal8() {
        return idPoliceTotal8;
    }

    public java.lang.String getIdPoliceTotal9() {
        return idPoliceTotal9;
    }

    /**
     * @see globaz.helios.db.interfaces.ITreeListable#getLibelle()
     */

    @Override
    public java.lang.String getLibelle() {
        String langue = getSession().getIdLangueISO();
        if ("IT".equalsIgnoreCase(langue)) {
            return libelleIt;
        } else if ("DE".equalsIgnoreCase(langue)) {
            return libelleDe;
        } else {
            return libelleFr;
        }
    }

    public java.lang.String getLibelleDe() {
        return libelleDe;
    }

    public java.lang.String getLibelleFr() {
        return libelleFr;
    }

    public java.lang.String getLibelleIt() {
        return libelleIt;
    }

    public void setIdClassification(java.lang.String newIdClassification) {
        idClassification = newIdClassification;
    }

    /**
     * Setter
     */
    public void setIdDefinitionListe(java.lang.String newIdDefinitionListe) {
        idDefinitionListe = newIdDefinitionListe;
    }

    public void setIdPoliceDetail(java.lang.String newIdPoliceDetail) {
        idPoliceDetail = newIdPoliceDetail;
    }

    public void setIdPoliceTitre1(java.lang.String newIdPoliceTitre1) {
        idPoliceTitre1 = newIdPoliceTitre1;
    }

    public void setIdPoliceTitre2(java.lang.String newIdPoliceTitre2) {
        idPoliceTitre2 = newIdPoliceTitre2;
    }

    public void setIdPoliceTitre3(java.lang.String newIdPoliceTitre3) {
        idPoliceTitre3 = newIdPoliceTitre3;
    }

    public void setIdPoliceTitre4(java.lang.String newIdPoliceTitre4) {
        idPoliceTitre4 = newIdPoliceTitre4;
    }

    public void setIdPoliceTitre5(java.lang.String newIdPoliceTitre5) {
        idPoliceTitre5 = newIdPoliceTitre5;
    }

    public void setIdPoliceTitre6(java.lang.String newIdPoliceTitre6) {
        idPoliceTitre6 = newIdPoliceTitre6;
    }

    public void setIdPoliceTitre7(java.lang.String newIdPoliceTitre7) {
        idPoliceTitre7 = newIdPoliceTitre7;
    }

    public void setIdPoliceTitre8(java.lang.String newIdPoliceTitre8) {
        idPoliceTitre8 = newIdPoliceTitre8;
    }

    public void setIdPoliceTitre9(java.lang.String newIdPoliceTitre9) {
        idPoliceTitre9 = newIdPoliceTitre9;
    }

    public void setIdPoliceTotal1(java.lang.String newIdPoliceTotal1) {
        idPoliceTotal1 = newIdPoliceTotal1;
    }

    public void setIdPoliceTotal2(java.lang.String newIdPoliceTotal2) {
        idPoliceTotal2 = newIdPoliceTotal2;
    }

    public void setIdPoliceTotal3(java.lang.String newIdPoliceTotal3) {
        idPoliceTotal3 = newIdPoliceTotal3;
    }

    public void setIdPoliceTotal4(java.lang.String newIdPoliceTotal4) {
        idPoliceTotal4 = newIdPoliceTotal4;
    }

    public void setIdPoliceTotal5(java.lang.String newIdPoliceTotal5) {
        idPoliceTotal5 = newIdPoliceTotal5;
    }

    public void setIdPoliceTotal6(java.lang.String newIdPoliceTotal6) {
        idPoliceTotal6 = newIdPoliceTotal6;
    }

    public void setIdPoliceTotal7(java.lang.String newIdPoliceTotal7) {
        idPoliceTotal7 = newIdPoliceTotal7;
    }

    public void setIdPoliceTotal8(java.lang.String newIdPoliceTotal8) {
        idPoliceTotal8 = newIdPoliceTotal8;
    }

    public void setIdPoliceTotal9(java.lang.String newIdPoliceTotal9) {
        idPoliceTotal9 = newIdPoliceTotal9;
    }

    public void setLibelleDe(java.lang.String newLibelleDe) {
        libelleDe = newLibelleDe;
    }

    public void setLibelleFr(java.lang.String newLibelleFr) {
        libelleFr = newLibelleFr;
    }

    public void setLibelleIt(java.lang.String newLibelleIt) {
        libelleIt = newLibelleIt;
    }
}
