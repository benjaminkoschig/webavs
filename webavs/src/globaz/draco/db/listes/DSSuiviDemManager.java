package globaz.draco.db.listes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.constantes.ILEConstantes;
import globaz.naos.translation.CodeSystem;
import java.io.Serializable;

public class DSSuiviDemManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String affiliation = "afaffip";
    public final static String complementJournal = "jojpcjo";
    public final static String declaration = "dsdeclp";

    public final static String groupeJournal = "jojpgjo";
    public final static String journalisation = "jojpjou";
    public final static String referenceProvenance = "jojprep";
    public final static String releve = "afrevep";
    private String forAnnee = "";
    private String forEtatDeclaration = "";
    private String forEtatReleve = "";

    private Boolean forFromDS = new Boolean(false);
    private Boolean forFromLeo = new Boolean(false);

    private Boolean forFromReleve = new Boolean(false);
    private String forGenreDeclaration = "";
    private String forGenreReleve = "";
    private Boolean forNombreNonRecu = new Boolean(false);
    private Boolean forNombreRecu = new Boolean(false);
    private String forOrder = "";
    private String forTypeDeclaration = "";

    private String forTypeDeclarationLeo = "";

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return " COUNT(*) AS NOMBRE";
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String from = "";
        if (getForFromDS().booleanValue()) {
            from = _getCollection() + DSSuiviDemManager.declaration + " " + DSSuiviDemManager.declaration
                    + " INNER JOIN " + _getCollection() + DSSuiviDemManager.affiliation + " "
                    + DSSuiviDemManager.affiliation + " ON (" + DSSuiviDemManager.declaration + ".maiaff="
                    + DSSuiviDemManager.affiliation + ".MAIAFF)";
        }
        if (getForFromReleve().booleanValue()) {
            from = _getCollection() + DSSuiviDemManager.releve + " " + DSSuiviDemManager.releve + " INNER JOIN "
                    + _getCollection() + DSSuiviDemManager.affiliation + " " + DSSuiviDemManager.affiliation + " ON ("
                    + DSSuiviDemManager.releve + ".malnaf=" + DSSuiviDemManager.affiliation + ".MALNAF AND "
                    + DSSuiviDemManager.releve + ".HTITIE=" + DSSuiviDemManager.affiliation + ".HTITIE" + " AND "
                    + DSSuiviDemManager.releve + ".MMDDEB>=" + getForAnnee() + "0101 AND " + DSSuiviDemManager.releve
                    + ".MMDDEB<=" + getForAnnee() + "1231)";
        }
        if (getForFromLeo().booleanValue()) {
            from = _getCollection() + DSSuiviDemManager.journalisation + " " + DSSuiviDemManager.journalisation
                    + " INNER JOIN " + _getCollection() + DSSuiviDemManager.groupeJournal + " "
                    + DSSuiviDemManager.groupeJournal + " ON (" + DSSuiviDemManager.groupeJournal + ".JGJOID="
                    + DSSuiviDemManager.journalisation + ".JGJOID)" + " INNER JOIN (SELECT * FROM " + _getCollection()
                    + DSSuiviDemManager.complementJournal + " WHERE JCJOTY=" + ILEConstantes.CS_DEF_FORMULE_GROUPE
                    + " AND JCJOVA=" + ILEConstantes.CS_DEBUT_SUIVI_DS + ") AS CPL ON CPL.JJOUID="
                    + DSSuiviDemManager.journalisation + ".JJOUID" + " INNER JOIN (SELECT * FROM " + _getCollection()
                    + DSSuiviDemManager.referenceProvenance + " WHERE JREPTY='" + ILEConstantes.CS_PARAM_GEN_PERIODE
                    + "' AND JREPIP='" + getForAnnee() + "') AS PROV ON PROV.JJOUID="
                    + DSSuiviDemManager.journalisation + ".JJOUID" + " INNER JOIN (SELECT * FROM " + _getCollection()
                    + DSSuiviDemManager.complementJournal + " WHERE JCJOTY=" + ILEConstantes.CS_CATEGORIE_GROUPE
                    + " AND JCJOVA=" + ILEConstantes.CS_CATEGORIE_SUIVI_DS + ") AS CPL2 ON CPL2.JJOUID="
                    + DSSuiviDemManager.journalisation + ".JJOUID";
        }
        if (getForFromLeo().booleanValue()) {
            from = from + " INNER JOIN " + _getCollection() + DSSuiviDemManager.affiliation + " "
                    + DSSuiviDemManager.affiliation + " ON (" + DSSuiviDemManager.affiliation + ".MALNAF="
                    + DSSuiviDemManager.journalisation + ".JJOULI AND (" + DSSuiviDemManager.affiliation
                    + ".MADFIN=0 OR " + DSSuiviDemManager.affiliation + ".MADFIN>=" + getForAnnee() + "0101) AND "
                    + DSSuiviDemManager.affiliation + ".MATTAF IN(" + CodeSystem.TYPE_AFFILI_EMPLOY + ", "
                    + CodeSystem.TYPE_AFFILI_INDEP_EMPLOY + ", " + CodeSystem.TYPE_AFFILI_EMPLOY_D_F + "))";
        }

        return from;
    }

    /**
     * Renvoie la composante de tri de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        if (JadeStringUtil.isEmpty(getForOrder())) {
            return null;
        } else {
            return getForOrder();
        }
    }

    /**
     * Renvoie la composante de sélection de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        if (!JadeStringUtil.isEmpty(getForAnnee()) && getForFromDS().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += DSSuiviDemManager.declaration + ".TAANNE="
                    + this._dbWriteNumeric(statement.getTransaction(), getForAnnee());
        }

        if (!JadeStringUtil.isEmpty(getForEtatDeclaration())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += DSSuiviDemManager.declaration + ".TATETA="
                    + this._dbWriteNumeric(statement.getTransaction(), getForEtatDeclaration());
        }

        if (!JadeStringUtil.isEmpty(getForEtatReleve())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += DSSuiviDemManager.releve + ".MMETAT="
                    + this._dbWriteNumeric(statement.getTransaction(), getForEtatReleve());
        }

        if (!JadeStringUtil.isEmpty(getForTypeDeclaration())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += DSSuiviDemManager.affiliation + ".MATDEC="
                    + this._dbWriteNumeric(statement.getTransaction(), getForTypeDeclaration());
        }

        if (!JadeStringUtil.isEmpty(getForGenreDeclaration())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += DSSuiviDemManager.declaration + ".TATTYP="
                    + this._dbWriteNumeric(statement.getTransaction(), getForGenreDeclaration());
        }
        if (!JadeStringUtil.isEmpty(getForGenreReleve())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (CodeSystem.TYPE_RELEVE_DECOMP_FINAL.equals(getForGenreReleve())) {
                sqlWhere += DSSuiviDemManager.releve + ".MMTYRE IN ("
                        + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_RELEVE_DECOMP_FINAL) + ", "
                        + this._dbWriteNumeric(statement.getTransaction(), CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA)
                        + ")";
            } else {
                sqlWhere += DSSuiviDemManager.releve + ".MMTYRE ="
                        + this._dbWriteNumeric(statement.getTransaction(), getForGenreReleve());
            }
        }

        if (getForNombreRecu().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "JGJORE <> 0 AND JGJORA=0";
        }

        if (getForNombreNonRecu().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "JGJORA <> 0 AND JGJORE=0";
        }

        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité.
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new DSSuiviDem();
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForEtatDeclaration() {
        return forEtatDeclaration;
    }

    public String getForEtatReleve() {
        return forEtatReleve;
    }

    public Boolean getForFromDS() {
        return forFromDS;
    }

    public Boolean getForFromLeo() {
        return forFromLeo;
    }

    public Boolean getForFromReleve() {
        return forFromReleve;
    }

    public String getForGenreDeclaration() {
        return forGenreDeclaration;
    }

    public String getForGenreReleve() {
        return forGenreReleve;
    }

    public Boolean getForNombreNonRecu() {
        return forNombreNonRecu;
    }

    public Boolean getForNombreRecu() {
        return forNombreRecu;
    }

    public String getForOrder() {
        return forOrder;
    }

    public String getForTypeDeclaration() {
        return forTypeDeclaration;
    }

    public String getForTypeDeclarationLeo() {
        return forTypeDeclarationLeo;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForEtatDeclaration(String forEtatDeclaration) {
        this.forEtatDeclaration = forEtatDeclaration;
    }

    public void setForEtatReleve(String forEtatReleve) {
        this.forEtatReleve = forEtatReleve;
    }

    public void setForFromDS(Boolean forFromDS) {
        this.forFromDS = forFromDS;
    }

    public void setForFromLeo(Boolean forFromLeo) {
        this.forFromLeo = forFromLeo;
    }

    public void setForFromReleve(Boolean forFromReleve) {
        this.forFromReleve = forFromReleve;
    }

    public void setForGenreDeclaration(String forGenreDeclaration) {
        this.forGenreDeclaration = forGenreDeclaration;
    }

    public void setForGenreReleve(String forGenreReleve) {
        this.forGenreReleve = forGenreReleve;
    }

    public void setForNombreNonRecu(Boolean forNombreNonRecu) {
        this.forNombreNonRecu = forNombreNonRecu;
    }

    public void setForNombreRecu(Boolean forNombreRecu) {
        this.forNombreRecu = forNombreRecu;
    }

    public void setForOrder(String forOrder) {
        this.forOrder = forOrder;
    }

    public void setForTypeDeclaration(String forTypeDeclaration) {
        this.forTypeDeclaration = forTypeDeclaration;
    }

    public void setForTypeDeclarationLeo(String forTypeDeclarationLeo) {
        this.forTypeDeclarationLeo = forTypeDeclarationLeo;
    }

}
