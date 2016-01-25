package globaz.osiris.db.comptes;

import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BEntity;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APIRubrique;
import java.io.Serializable;

/**
 * @author sch Cette classe permet de r�f�rencer une rubrique avec un ou plusieurs codes syst�mes Ce qui permet aux
 *         applications externes de r�f�rence toujours le m�me code syst�me, sans se soucier des id des rubriques
 */
public class CAReferenceRubrique extends BEntity implements Serializable, APIReferenceRubrique {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_IDCODEREFERENCE = "IDCODEREFERENCE";
    public static final String FIELD_IDREFRUBRIQUE = "IDREFRUBRIQUE";
    public static final String FIELD_IDRUBRIQUE = "IDRUBRIQUE";
    public static final String TABLE_CARERUP = "CARERUP";
    // code systeme
    private FWParametersSystemCode csCodeReference = null;
    private FWParametersSystemCodeManager csCodeReferences = null;
    private String idCodeReference = new String();
    private String idExterneRubrique = new String();
    private String idRefRubrique = new String();
    private String idRubrique = new String();
    private CAReferenceRubrique referenceRubrique = null;

    private CARubrique rubrique = null;
    private CARubrique rubriqueRef = null;

    /**
     * Commentaire relatif au constructeur CAReferenceRubrique
     */
    public CAReferenceRubrique() {
        super();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.06.2002 14:51:53)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _afterAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
    }

    /**
     * Effectue des traitements avant un ajout dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements avant l'ajout de l'entit� dans la BD
     * <p>
     * L'ex�cution de l'ajout n'est pas effectu�e si le buffer d'erreurs n'est pas vide apr�s l'ex�cution de
     * <code>_beforeAdd()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incr�mente le prochain num�ro
        setIdRefRubrique(this._incCounter(transaction, idRefRubrique));
    }

    /**
     * Avant supression
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur
     */
    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
    }

    /**
     * Avant lecture
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur
     */
    @Override
    protected void _beforeRetrieve(BTransaction transaction) throws Exception {
    }

    /**
     * Avant Mise � jour
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur
     */
    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
    }

    /**
     * Renvoie le nom de la table
     * 
     * @return String
     */
    @Override
    protected String _getTableName() {
        return CAReferenceRubrique.TABLE_CARERUP;
    }

    /**
     * read
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idRefRubrique = statement.dbReadNumeric(CAReferenceRubrique.FIELD_IDREFRUBRIQUE);
        idRubrique = statement.dbReadNumeric(CAReferenceRubrique.FIELD_IDRUBRIQUE);
        idCodeReference = statement.dbReadNumeric(CAReferenceRubrique.FIELD_IDCODEREFERENCE);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {

        _propertyMandatory(statement.getTransaction(), getIdRefRubrique(),
                getSession().getLabel("IDREFRUBRIQUE_OBLIGATOIRE"));
        _propertyMandatory(statement.getTransaction(), getIdRubrique(), getSession().getLabel("7045"));
        _propertyMandatory(statement.getTransaction(), getIdCodeReference(),
                getSession().getLabel("IDCODEREFERENCE_OBLIGATOIRE"));

        // Contr�ler que le code de r�f�rence n'existe pas dans la base, il doit
        // �tre unique
        CAReferenceRubriqueManager manager = new CAReferenceRubriqueManager();
        manager.setSession(getSession());
        manager.setForCodeReference(getIdCodeReference());
        try {
            manager.find();
            if (manager.size() > 0) {
                CAReferenceRubrique ref = (CAReferenceRubrique) manager.getEntity(0);
                _addError(statement.getTransaction(), FWMessageFormat.format(
                        getSession().getLabel("CODE_REFERENCE_DEJA_UTILISE"), getCsCodeReference()
                                .getCurrentCodeUtilisateur().getLibelle(), ref.getRubrique().getIdExterne() + " "
                                + ref.getRubrique().getDescription()));
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    /**
     * Set la cl� altern�e pour effectuer un retrieve sur une autre zone que la cl� primaire
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _writeAlternateKey(globaz.globall.db.BStatement statement, int alternateKey)
            throws java.lang.Exception {
        // Cl� altern�e num�ro 1 : idExterne
        switch (alternateKey) {
            case AK_CODE_REFERENCE:
                statement.writeKey(CAReferenceRubrique.FIELD_IDCODEREFERENCE,
                        this._dbWriteNumeric(statement.getTransaction(), getIdCodeReference(), ""));
                break;
            default:
                throw new Exception("Alternate key " + alternateKey + " not implemented");
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(CAReferenceRubrique.FIELD_IDREFRUBRIQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRefRubrique(), ""));
    }

    /**
     * write
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(CAReferenceRubrique.FIELD_IDREFRUBRIQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRefRubrique(), "idRefRubrique"));
        statement.writeField(CAReferenceRubrique.FIELD_IDRUBRIQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRubrique(), "idRubrique"));
        statement.writeField(CAReferenceRubrique.FIELD_IDCODEREFERENCE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCodeReference(), "idCodeReference"));
    }

    public FWParametersSystemCode getCsCodeReference() {

        if (csCodeReference == null) {
            // liste pas encore chargee, on la charge
            csCodeReference = new FWParametersSystemCode();
            csCodeReference.setSession(getSession());
            csCodeReference.getCode(getIdCodeReference());
        }
        return csCodeReference;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.12.2001 11:19:02)
     * 
     * @return globaz.bambou.db.AJCodeSystemeManager
     */
    public FWParametersSystemCodeManager getCsCodeReferences() {
        // liste d�j� charg�e ?
        if (csCodeReferences == null) {
            // liste pas encore charg�e, on la charge
            csCodeReferences = new FWParametersSystemCodeManager();
            csCodeReferences.setSession(getSession());
            csCodeReferences.getListeCodesSup("OSIREFRUB", getSession().getIdLangue());
        }
        return csCodeReferences;
    }

    /**
     * Retourne l'id du code r�f�rence (code syst�me)
     * 
     * @return String idCodeReference
     */
    public String getIdCodeReference() {
        return idCodeReference;
    }

    public String getIdExterneRubrique() {
        if (getRubrique() != null) {
            idExterneRubrique = getRubrique().getIdExterne();
        }
        return idExterneRubrique;
    }

    /**
     * retourne l'id de r�f�rence de la rubrique
     * 
     * @return String idRefRubrique
     */
    public String getIdRefRubrique() {
        return idRefRubrique;
    }

    /**
     * retourne l'id de la rubrique
     * 
     * @return String idRubrique
     */
    public String getIdRubrique() {
        return idRubrique;
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

    /**
     * Cette m�thode permet de r�cup�rer une rubrique en fonction d'un code r�f�rence de rubrique (code syst�me) Cette
     * m�thode retourne null s'il y a des erreurs
     * 
     * @param String
     *            idCodeReference
     * @return CARubrique
     */
    @Override
    public APIRubrique getRubriqueByCodeReference(String idCodeReference) {
        // Lecture de referenceRubrique
        if ((referenceRubrique == null) || !idCodeReference.equalsIgnoreCase(referenceRubrique.getIdCodeReference())) {
            referenceRubrique = new CAReferenceRubrique();
            referenceRubrique.setISession(getSession());
            referenceRubrique.setAlternateKey(APIReferenceRubrique.AK_CODE_REFERENCE);
            referenceRubrique.setIdCodeReference(idCodeReference);
            try {
                referenceRubrique.retrieve();
                if (referenceRubrique.isNew()) {
                    rubriqueRef = null;
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                rubriqueRef = null;
                return null;
            }
        }
        // Lecture de la rubrique
        if ((rubriqueRef == null) || !referenceRubrique.getIdRubrique().equalsIgnoreCase(rubriqueRef.getIdRubrique())) {
            rubriqueRef = new CARubrique();
            rubriqueRef.setISession(getSession());
            rubriqueRef.setIdRubrique(referenceRubrique.getIdRubrique());
            try {
                rubriqueRef.retrieve();
                if (rubriqueRef.isNew()) {
                    rubriqueRef = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                rubriqueRef = null;
            }
        }
        return rubriqueRef;
    }

    /**
     * set l'id du code de r�f�rence (code syst�me)
     * 
     * @param String
     *            newIdCodeReference
     */
    @Override
    public void setIdCodeReference(String newIdCodeReference) {
        idCodeReference = newIdCodeReference;
    }

    /**
     * set l'id de r�f�rence de la rubrique
     * 
     * @param string
     *            newIdRefRubrique
     */
    public void setIdRefRubrique(String newIdRefRubrique) {
        idRefRubrique = newIdRefRubrique;
    }

    /**
     * set l'id de la rubrique
     * 
     * @param newIdRubrique
     */
    public void setIdRubrique(String newIdRubrique) {
        idRubrique = newIdRubrique;
    }
}
