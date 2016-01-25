package globaz.musca.db.facturation;

import globaz.musca.api.IFAPrintDoc;

public class FAEnteteFactureAPG extends FAEnteteFacture implements globaz.musca.api.IFAEnteteFacture,
        java.io.Serializable, IFAPrintDoc {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static java.lang.String APG_TABLE_FIELDS = FAEnteteFacture.TABLE_FIELDS
            + ", CASECTP.IDSECTION, CASECTP.IDTYPESECTION, CASECTP.IDEXTERNE, CASECTP.SOLDE, CACPTAP.IDCOMPTEANNEXE, FAENTFP.MODIMP";
    private String idCompteAnnexe = "";
    private String idExterne = "";
    private String idSection = "";
    private String idTypeSection = "";
    private String soldeSection = "";

    /**
     * Commentaire relatif au constructeur FAEnteteFacture
     */
    public FAEnteteFactureAPG() {
        super();
    }

    @Override
    protected String _getFields(globaz.globall.db.BStatement statement) {
        return FAEnteteFactureAPG.APG_TABLE_FIELDS;
    }

    /*
     * Traitement après suppression
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        super._readProperties(statement);
        idSection = statement.dbReadNumeric("IDSECTION");
        idTypeSection = statement.dbReadNumeric("IDTYPESECTION");
        idExterne = statement.dbReadString("IDEXTERNE");
        soldeSection = statement.dbReadNumeric("SOLDE");
        idCompteAnnexe = statement.dbReadNumeric("IDCOMPTEANNEXE");

    }

    /**
     * @return
     */
    public java.lang.String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * @return
     */
    public java.lang.String getIdExterne() {
        return idExterne;
    }

    /**
     * @return
     */
    public java.lang.String getIdSection() {
        return idSection;
    }

    /**
     * @return
     */
    public java.lang.String getIdTypeSection() {
        return idTypeSection;
    }

    /**
     * @return
     */
    public java.lang.String getSoldeSection() {
        return soldeSection;
    }

}
