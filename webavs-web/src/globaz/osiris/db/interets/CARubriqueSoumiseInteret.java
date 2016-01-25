package globaz.osiris.db.interets;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CARubrique;
import java.util.HashMap;

/**
 * Insérez la description du type ici. Date de création : (30.12.2002 10:09:42)
 * 
 * @author: Administrator
 */
public class CARubriqueSoumiseInteret extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static HashMap cache = null;
    public static final String FIELD_IDPLACALINT = "IDPLACALINT";

    public static final String FIELD_IDRUBRIQUE = "IDRUBRIQUE";
    public static final String TABLE_CAIMRSP = "CAIMRSP";

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 15:15:51)
     * 
     * @return java.lang.String
     * @param idRubrique
     *            java.lang.String
     */
    public static String getFromCache(BSession session, String idRubrique) {
        // Chargement du cache si null
        if (CARubriqueSoumiseInteret.cache == null) {
            CARubriqueSoumiseInteret.cache = new HashMap();
            CARubriqueSoumiseInteretManager mgr = new CARubriqueSoumiseInteretManager();
            mgr.setSession(session);
            try {
                // Charger le manager
                mgr.find(BManager.SIZE_NOLIMIT);
                for (int i = 0; i < mgr.size(); i++) {
                    CARubriqueSoumiseInteret rub = (CARubriqueSoumiseInteret) mgr.getEntity(i);
                    CARubriqueSoumiseInteret.cache.put(rub.getIdRubrique(), rub.getIdPlanCalculInteret());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Retourne le plan ou null si pas trouvé
        return (String) CARubriqueSoumiseInteret.cache.get(idRubrique);
    }

    private String idPlanCalculInteret = new String();

    private String idRubrique = new String();

    private CAPlanCalculInteret planCalculInteret = null;

    // code systeme

    private CARubrique rubrique = null;

    /**
     * Commentaire relatif au constructeur CARubriqueSoumiseInteret
     */
    public CARubriqueSoumiseInteret() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return CARubriqueSoumiseInteret.TABLE_CAIMRSP;
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idRubrique = statement.dbReadNumeric(CARubriqueSoumiseInteret.FIELD_IDRUBRIQUE);
        idPlanCalculInteret = statement.dbReadNumeric(CARubriqueSoumiseInteret.FIELD_IDPLACALINT);
    }

    private void _synchroRubriqueFromEcran(globaz.globall.db.BTransaction transaction) {
        // Si l'id est null, on met à zéro le compte
        if (JadeStringUtil.isBlank(getNumeroRubrique())) {
            setIdRubrique("");
        } else if ((getRubrique() == null) || !getRubrique().getIdExterne().equals(getNumeroRubrique())) {

            // Instancier un nouveau bean
            CARubrique _rub = new CARubrique();
            _rub.setSession(getSession());
            _rub.setAlternateKey(APIRubrique.AK_IDEXTERNE);
            _rub.setIdExterne(getIdRubrique());

            // Lecture
            try {
                _rub.retrieve(transaction);

                // En cas d'erreur, on remet à zéro
                if (_rub.isNew()) {
                    setIdRubrique("");
                    _addError(transaction, getSession().getLabel("5115"));
                } else {
                    setIdRubrique(_rub.getIdRubrique());
                    rubrique = _rub;
                }

            } catch (Exception e) {
                setIdRubrique("");
                return;
            }
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) {
    }

    protected void _valider(globaz.globall.db.BTransaction transaction) {
        _synchroRubriqueFromEcran(transaction);
    }

    /**
	 *
	 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CARubriqueSoumiseInteret.FIELD_IDRUBRIQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRubrique(), ""));
        statement.writeKey(CARubriqueSoumiseInteret.FIELD_IDPLACALINT,
                this._dbWriteNumeric(statement.getTransaction(), getIdPlanCalculInteret(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(CARubriqueSoumiseInteret.FIELD_IDRUBRIQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRubrique(), "idRubrique"));
        statement.writeField(CARubriqueSoumiseInteret.FIELD_IDPLACALINT,
                this._dbWriteNumeric(statement.getTransaction(), getIdPlanCalculInteret(), "idPlanCalculInteret"));
    }

    public String getIdPlanCalculInteret() {
        return idPlanCalculInteret;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getIdRubrique() {
        return idRubrique;
    }

    public String getNumeroRubrique() {
        CARubrique rubr = new CARubrique();
        rubr.setSession(getSession());
        rubr.setIdRubrique(getIdRubrique());
        try {
            rubr.retrieve();
        } catch (Exception e) {
            return "";
        }
        if (!rubr.isNew()) {
            return rubr.getIdExterne();
        } else {
            return "";
        }
    }

    public CAPlanCalculInteret getPlanCalculInteret() {
        if (planCalculInteret == null) {
            planCalculInteret = new CAPlanCalculInteret();
            planCalculInteret.setISession(getSession());
            planCalculInteret.setIdPlanCalculInteret(getIdPlanCalculInteret());
            try {
                planCalculInteret.retrieve();
                if (planCalculInteret.isNew()) {
                    planCalculInteret = null;
                }
            } catch (Exception e) {
                planCalculInteret = null;
            }
        }
        return planCalculInteret;
    }

    public APIRubrique getRubrique() {
        if (rubrique == null) {
            rubrique = new CARubrique();
            rubrique.setISession(getSession());
            rubrique.setIdRubrique(getIdRubrique());
            try {
                rubrique.retrieve();
                if (rubrique.isNew()) {
                    rubrique = null;
                }
            } catch (Exception e) {
                rubrique = null;
            }
        }
        return rubrique;
    }

    public void setIdPlanCalculInteret(String newIdPlanCalculInteret) {
        idPlanCalculInteret = newIdPlanCalculInteret;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */
    public void setIdRubrique(String newIdRubrique) {
        idRubrique = newIdRubrique;
    }
}
