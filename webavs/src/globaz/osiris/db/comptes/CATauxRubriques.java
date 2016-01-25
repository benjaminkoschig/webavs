package globaz.osiris.db.comptes;

import globaz.globall.api.BISession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APITauxRubriques;
import globaz.osiris.application.CAApplication;
import globaz.pyxis.api.osiris.TITiersAdministrationOSI;
import java.math.BigDecimal;

/**
 * Insérez la description du type ici. Date de création : (12.12.2001 10:57:38)
 * 
 * @author: Administrator
 */
public class CATauxRubriques extends globaz.globall.db.BEntity implements java.io.Serializable, APITauxRubriques {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String GENRE_ADMIN_CAISSE_PROF = "509028";
    private java.lang.String date = new String();
    private java.lang.String idCaisseProf = new String();
    private java.lang.String idRubrique = new String();
    private java.lang.String idTauxRubrique = new String();
    private String numeroRubrique = new String();
    private java.lang.String tauxEmployeur = new String();

    private java.lang.String tauxSalarie = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CATauxRubriques
     */
    public CATauxRubriques() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(idTauxRubrique)) {
            setIdTauxRubrique(this._incCounter(transaction, "0"));
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CATAUXP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idTauxRubrique = statement.dbReadNumeric("ANID");
        idRubrique = statement.dbReadNumeric("AGID");
        date = statement.dbReadDateAMJ("ANDATE");
        tauxSalarie = statement.dbReadNumeric("ANTASA", 5);
        tauxEmployeur = statement.dbReadNumeric("ANTAEM", 5);
        idCaisseProf = statement.dbReadNumeric("IDCAIPRO");

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        if (!JadeStringUtil.isBlank(numeroRubrique)) {
            CARubriqueManager mgr = new CARubriqueManager();
            mgr.setSession(getSession());
            mgr.setForIdExterne(numeroRubrique);
            mgr.find();
            if (mgr.size() != 0) {
                idRubrique = ((CARubrique) mgr.getFirstEntity()).getIdRubrique();
            }
        }
        // plausi
        if (JadeStringUtil.isIntegerEmpty(idRubrique)) {
            statement.getTransaction().addErrors(getSession().getLabel("VENTILATION_PLA_RUB"));
        }
        if (JadeStringUtil.isBlank(tauxSalarie)) {
            statement.getTransaction().addErrors(getSession().getLabel("VENTILATION_PLA_TAUX_SAL"));
        }
        if (JadeStringUtil.isBlank(tauxEmployeur)) {
            statement.getTransaction().addErrors(getSession().getLabel("VENTILATION_PLA_TAUX_EMP"));
        }
        if (JadeStringUtil.isIntegerEmpty(date)) {
            statement.getTransaction().addErrors(getSession().getLabel("VENTILATION_PLA_DATE"));
        }
        if ((new BigDecimal(tauxSalarie).compareTo(new BigDecimal(0)) < 0)
                || (new BigDecimal(tauxSalarie).compareTo(new BigDecimal(100)) > 0)) {
            statement.getTransaction().addErrors(getSession().getLabel("VENTILATION_PLA_TAUX"));
        }
        if ((new BigDecimal(tauxEmployeur).compareTo(new BigDecimal(0)) < 0)
                || (new BigDecimal(tauxEmployeur).compareTo(new BigDecimal(100)) > 0)) {
            statement.getTransaction().addErrors(getSession().getLabel("VENTILATION_PLA_TAUX"));
        }

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("ANID", this._dbWriteNumeric(statement.getTransaction(), getIdTauxRubrique(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("ANID",
                this._dbWriteNumeric(statement.getTransaction(), getIdTauxRubrique(), "idTauxRubrique"));
        statement.writeField("AGID", this._dbWriteNumeric(statement.getTransaction(), getIdRubrique(), "idRubrique"));
        statement.writeField("ANDATE", this._dbWriteDateAMJ(statement.getTransaction(), getDate(), "date"));
        statement.writeField("ANTASA",
                this._dbWriteNumeric(statement.getTransaction(), getTauxSalarie(), "taux salarie"));
        statement.writeField("ANTAEM",
                this._dbWriteNumeric(statement.getTransaction(), getTauxEmployeur(), "taux employeur"));
        statement.writeField("IDCAIPRO",
                this._dbWriteNumeric(statement.getTransaction(), getIdCaisseProf(), "idCaisseProf"));

    }

    /**
     * Return le libellé de la caisse professionnelle.
     * 
     * @return Le libellé. Si id vide => return "".
     */
    public String getCaisseProfessionnelleLibelle() {
        if (!JadeStringUtil.isIntegerEmpty(getIdCaisseProf())) {
            try {
                BISession pyxisSession = ((CAApplication) GlobazServer.getCurrentSystem().getApplication(
                        CAApplication.DEFAULT_APPLICATION_OSIRIS)).getSessionPyxis(getSession(), true);
                return TITiersAdministrationOSI.getAdministrationLibelle(pyxisSession, getIdCaisseProf());
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * Return le numéro de la caisse professionnelle.
     * 
     * @return Le numéro. Si id vide => return "".
     */
    public String getCaisseProfessionnelleNumero() {
        if (!JadeStringUtil.isIntegerEmpty(getIdCaisseProf())) {
            try {
                BISession pyxisSession = ((CAApplication) GlobazServer.getCurrentSystem().getApplication(
                        CAApplication.DEFAULT_APPLICATION_OSIRIS)).getSessionPyxis(getSession(), true);
                return TITiersAdministrationOSI.getAdministrationNumero(pyxisSession, getIdCaisseProf());
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }
    }

    @Override
    public java.lang.String getDate() {
        return date;
    }

    public String getDescription() {
        CARubrique rub = new CARubrique();
        rub.setSession(getSession());
        rub.setIdRubrique(getIdRubrique());
        try {
            rub.retrieve();
        } catch (Exception e) {
            // TODO Bloc catch auto-généré
            return "";
        }
        if (!rub.isNew()) {
            return rub.getIdExterne() + "  " + rub.getDescription(getSession().getIdLangueISO());
        }
        return "";
    }

    public java.lang.String getIdCaisseProf() {
        return idCaisseProf;
    }

    @Override
    public java.lang.String getIdRubrique() {
        return idRubrique;
    }

    /**
     * Getter
     */
    @Override
    public java.lang.String getIdTauxRubrique() {
        return idTauxRubrique;
    }

    /**
     * @return
     */
    public String getNumeroRubrique() {
        CARubrique rub = new CARubrique();
        rub.setSession(getSession());
        rub.setIdRubrique(getIdRubrique());
        try {
            rub.retrieve();
        } catch (Exception e) {
            // TODO Bloc catch auto-généré
            return "";
        }
        if (!rub.isNew()) {
            return rub.getIdExterne();
        }

        return "";
    }

    /**
     * @return
     */
    @Override
    public java.lang.String getTauxEmployeur() {
        return tauxEmployeur;
    }

    /**
     * @return
     */
    @Override
    public java.lang.String getTauxSalarie() {
        return tauxSalarie;
    }

    @Override
    public void setDate(java.lang.String newDate) {
        date = newDate;
    }

    public void setIdCaisseProf(java.lang.String newIdCaisseProf) {
        idCaisseProf = newIdCaisseProf;
    }

    @Override
    public void setIdRubrique(java.lang.String newIdRubrique) {
        idRubrique = newIdRubrique;
    }

    /**
     * Setter
     */
    @Override
    public void setIdTauxRubrique(java.lang.String newIdTauxRubrique) {
        idTauxRubrique = newIdTauxRubrique;
    }

    /**
     * @param string
     */
    public void setNumeroRubrique(String string) {
        numeroRubrique = string;
    }

    /**
     * @param string
     */
    @Override
    public void setTauxEmployeur(java.lang.String string) {
        tauxEmployeur = string;
    }

    /**
     * @param string
     */
    @Override
    public void setTauxSalarie(java.lang.String string) {
        tauxSalarie = string;
    }
}
