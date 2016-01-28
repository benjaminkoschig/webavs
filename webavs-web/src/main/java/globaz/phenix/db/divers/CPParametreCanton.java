package globaz.phenix.db.divers;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

public class CPParametreCanton extends globaz.globall.db.BEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static java.lang.String CS_AF_CUMUL_REVENU = "640000";
    public final static java.lang.String CS_AF_REVENU1 = "640001";
    public final static java.lang.String CS_AF_REVENU2 = "640002";
    public final static java.lang.String CS_AF_SELON_COTI = "640003";

    public final static java.lang.String CS_MODE_CALCUL_AF = "650001";
    public final static java.lang.String CS_MODE_ENVOI_SEDEX = "650000";
    public final static java.lang.String CS_MODE_RECEPTION_SEDEX = "650002";

    public final static java.lang.String CS_SEDEXENVOI_CJT_IND_SEPARE = "630003";
    public final static java.lang.String CS_SEDEXENVOI_CJT_SEPARE = "630002";
    public final static java.lang.String CS_SEDEXENVOI_DIRECTIVE = "630000";
    public final static java.lang.String CS_SEDEXENVOI_MODE_VD = "630001";

    public final static java.lang.String CS_SEDEXRECEPTION_CJT = "660000";
    public final static java.lang.String CS_SEDEXRECEPTION_CJT_NA = "660001";
    public final static java.lang.String CS_SEDEXRECEPTION_CJT_SEPARE = "660002";

    /**
     * Recherche de la valeur du paremètre pour un canton à une date donnée
     * 
     * @param session
     * @param canton
     * @param typeParametre
     * @param dateDebut
     * @return
     * @throws Exception
     */
    public static String findCodeWhitTypeAndCanton(BSession session, String canton, String typeParametre,
            String dateDebut) throws Exception {
        CPParametreCantonManager mng = new CPParametreCantonManager();
        mng.setSession(session);
        mng.setForCanton(canton);
        mng.setForTypeParametre(typeParametre);
        mng.setFromDateDebut(dateDebut);
        mng.setOrderByDateDesc();
        mng.find();
        if (mng.getSize() > 0) {
            return ((CPParametreCanton) mng.getFirstEntity()).getCodeParametre();
        } else {
            return "";
        }
    }

    /**
     * * Recherche de la valeur du paremètre pour un canton, une genre d'affilié à une date
     * 
     * @param session
     * @param canton
     * @param typeParametre
     * @param dateDebut
     * @param genreAffilie
     * @return
     * @throws Exception
     */
    public static String findCodeWhitTypeCantonAndGenre(BSession session, String canton, String typeParametre,
            String dateDebut, String genreAffilie) throws Exception {
        CPParametreCantonManager mng = new CPParametreCantonManager();
        mng.setSession(session);
        mng.setForCanton(canton);
        mng.setForGenreAffilie(genreAffilie);
        mng.setForTypeParametre(typeParametre);
        mng.setFromDateDebut(dateDebut);
        mng.setOrderByDateDesc();
        mng.find();
        if (mng.getSize() > 0) {
            return ((CPParametreCanton) mng.getFirstEntity()).getCodeParametre();
        } else {
            // Si il n'y a pas de type défini pour le genre d'affilié => regarder si il y une valeur générique pour tous
            // les genres
            return CPParametreCanton.findCodeWhitTypeAndCanton(session, canton, typeParametre, dateDebut);
        }
    }

    private java.lang.String canton = "";
    private java.lang.String codeParametre = "";
    private java.lang.String dateDebut = "";
    private java.lang.String genreAffilie = "";
    private java.lang.String idParametreCanton = "";
    private java.lang.String typeParametre = "";

    /**
     * Commentaire relatif au constructeur CPPeriodeFiscale
     */
    public CPParametreCanton() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdParametreCanton(this._incCounter(transaction, idParametreCanton));

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CPCANTOP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idParametreCanton = statement.dbReadNumeric("SPINDE");
        typeParametre = statement.dbReadNumeric("SPTPAR");
        genreAffilie = statement.dbReadNumeric("SPTGAF");
        canton = statement.dbReadNumeric("SPCCAN");
        codeParametre = statement.dbReadNumeric("SPCPAR");
        dateDebut = statement.dbReadDateAMJ("SPDDEB");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        if (JadeStringUtil.isEmpty(getCodeParametre())) {
            _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0203"));
        }
        // Recerche si paramètre déjà encodé
        CPParametreCantonManager mng = new CPParametreCantonManager();
        mng.setSession(getSession());
        mng.setForCanton(getCanton());
        mng.setForGenreAffilie(getGenreAffilie());
        mng.setForTypeParametre(getTypeParametre());
        mng.setForExceptIdParamatre(getIdParametreCanton());
        mng.setWantControleDateZero(Boolean.TRUE);
        mng.setForDateDebut(getDateDebut());
        mng.find();
        if (mng.getSize() > 0) {
            _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0202"));
        }

    }

    // code systeme

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("SPINDE", this._dbWriteNumeric(statement.getTransaction(), getIdParametreCanton(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("SPINDE",
                this._dbWriteNumeric(statement.getTransaction(), getIdParametreCanton(), "idParametreCanton"));
        statement.writeField("SPCCAN", this._dbWriteNumeric(statement.getTransaction(), getCanton(), "canton"));
        statement.writeField("SPTGAF",
                this._dbWriteNumeric(statement.getTransaction(), getGenreAffilie(), "genreAffilie"));
        statement.writeField("SPTPAR",
                this._dbWriteNumeric(statement.getTransaction(), getTypeParametre(), "typeParametre"));
        statement.writeField("SPCPAR",
                this._dbWriteNumeric(statement.getTransaction(), getCodeParametre(), "codeParametre"));
        statement.writeField("SPDDEB", this._dbWriteDateAMJ(statement.getTransaction(), getDateDebut(), "dateDebut"));
    }

    public java.lang.String getCanton() {
        return canton;
    }

    public java.lang.String getCodeParametre() {
        return codeParametre;
    }

    public java.lang.String getDateDebut() {
        return dateDebut;
    }

    public java.lang.String getGenreAffilie() {
        return genreAffilie;
    }

    /**
     * Getter
     */
    public java.lang.String getIdParametreCanton() {
        return idParametreCanton;
    }

    public java.lang.String getTypeParametre() {
        return typeParametre;
    }

    public void setCanton(java.lang.String codeCanton) {
        canton = codeCanton;
    }

    public void setCodeParametre(java.lang.String codeFonctionnalite) {
        codeParametre = codeFonctionnalite;
    }

    public void setDateDebut(java.lang.String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setGenreAffilie(java.lang.String genreAffilie) {
        this.genreAffilie = genreAffilie;
    }

    /**
     * Setter
     */
    public void setIdParametreCanton(java.lang.String newIdIfd) {
        idParametreCanton = newIdIfd;
    }

    public void setTypeParametre(java.lang.String newAnneeDecisionDebut) {
        typeParametre = newAnneeDecisionDebut;
    }

}
