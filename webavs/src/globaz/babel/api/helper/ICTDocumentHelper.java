package globaz.babel.api.helper;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.jade.client.util.JadeStringUtil;
import globaz.webavs.common.CommonProperties;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class ICTDocumentHelper extends GlobazHelper implements ICTDocument {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String ENTITY_CLASS_NAME = "globaz.babel.db.cat.CTDocumentAPIAdapter";

    static final String PROP_ACTIF = "actif";
    static final String PROP_CODE_ISO_LANGUE = "codeIsoLangue";
    static final String PROP_CS_DESTINATAIRE = "csDestinataire";
    static final String PROP_CS_DOMAINE = "csDomaine";
    static final String PROP_CS_EDITABLE = "csEditable";
    static final String PROP_CS_TYPE_DOCUMENT = "csTypeDocument";
    static final String PROP_DATE_DESACTIVATION = "dateDesactivation";
    static final String PROP_DEFAUT = "defaut";
    static final String PROP_ID_DOCUMENT = "idDocument";
    static final String PROP_NIVEAU = "niveau";
    static final String PROP_NOM = "nom";
    static final String PROP_NOM_LIKE = "nomLike";
    static final String PROP_STYLED_DOCUMENT = "isStyledDocument";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private HashMap niveaux;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe ICTDocumentHelper.
     */
    public ICTDocumentHelper() {
        super(ICTDocumentHelper.ENTITY_CLASS_NAME);
    }

    /**
     * Crée une nouvelle instance de la classe ICTDocumentHelper.
     * 
     * @param vo
     *            un tableau non null et non vide de value objects
     * 
     * @throws NumberFormatException
     *             Si le niveau n'est pas un entier valide
     */
    public ICTDocumentHelper(GlobazValueObject[] vo) throws NumberFormatException {
        super(vo[0]);

        niveaux = new HashMap();

        // regrouper les vo par niveau
        for (int id = 0; id < vo.length; ++id) {
            Integer niveau = new Integer(this.getProp(vo[id], ICTDocumentHelper.PROP_NIVEAU));
            ICTListeTextesImpl textes = (ICTListeTextesImpl) niveaux.get(niveau);

            if (textes == null) {
                textes = new ICTListeTextesImpl();
                niveaux.put(niveau, textes);
            }

            // instancier un ICTTexteHelper pour wrapper chaque value object
            textes.addTexte(new ICTTexteHelper(vo[id]));
        }
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut code iso langue.
     * 
     * @return la valeur courante de l'attribut code iso langue
     */
    @Override
    public String getCodeIsoLangue() {
        if (JadeStringUtil.isEmpty(this.getProp(ICTDocumentHelper.PROP_CODE_ISO_LANGUE))) {
            try {
                setProp(ICTDocumentHelper.PROP_CODE_ISO_LANGUE, getISession().getIdLangueISO());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return this.getProp(ICTDocumentHelper.PROP_CODE_ISO_LANGUE);
    }

    /**
     * getter pour l'attribut cs destinataire.
     * 
     * @return la valeur courante de l'attribut cs destinataire
     */
    @Override
    public String getCsDestinataire() {
        return this.getProp(ICTDocumentHelper.PROP_CS_DESTINATAIRE);
    }

    /**
     * getter pour l'attribut cs domaine.
     * 
     * @return la valeur courante de l'attribut cs domaine
     */
    @Override
    public String getCsDomaine() {
        return this.getProp(ICTDocumentHelper.PROP_CS_DOMAINE);
    }

    /**
     * getter pour l'attribut cs editable.
     * 
     * @return la valeur courante de l'attribut cs editable
     */
    @Override
    public String getCsEditable() {
        return this.getProp(ICTDocumentHelper.PROP_CS_EDITABLE);
    }

    /**
     * getter pour l'attribut cs type document.
     * 
     * @return la valeur courante de l'attribut cs type document
     */
    @Override
    public String getCsTypeDocument() {
        return this.getProp(ICTDocumentHelper.PROP_CS_TYPE_DOCUMENT);
    }

    /**
     * getter pour l'attribut date desactivation.
     * 
     * @return la valeur courante de l'attribut date desactivation
     */
    @Override
    public String getDateDesactivation() {
        return this.getProp(ICTDocumentHelper.PROP_DATE_DESACTIVATION);
    }

    /**
     * getter pour l'attribut id document.
     * 
     * @return la valeur courante de l'attribut id document
     */
    @Override
    public String getIdDocument() {
        return this.getProp(ICTDocumentHelper.PROP_ID_DOCUMENT);
    }

    /**
     * getter pour l'attribut is actif.
     * 
     * @return la valeur courante de l'attribut is actif
     */
    public String getIsActif() {
        return this.getProp(ICTDocumentHelper.PROP_ACTIF);
    }

    /**
     * getter pour l'attribut is defaut.
     * 
     * @return la valeur courante de l'attribut is defaut
     */
    public String getIsDefaut() {
        return this.getProp(ICTDocumentHelper.PROP_DEFAUT);
    }

    public String getIsStyledDocument() {
        return this.getProp(ICTDocumentHelper.PROP_STYLED_DOCUMENT);
    }

    public String getNoCaisse() {
        return this.getProp(CommonProperties.KEY_NO_CAISSE);
    }

    /**
     * getter pour l'attribut nom.
     * 
     * @return la valeur courante de l'attribut nom
     */
    @Override
    public String getNom() {
        return this.getProp(ICTDocumentHelper.PROP_NOM);
    }

    @Override
    public String getNomLike() {
        return this.getProp(ICTDocumentHelper.PROP_NOM_LIKE);
    }

    private String getProp(GlobazValueObject vo, String name) {
        Object retValue = vo.getProperty(name);

        if (retValue == null) {
            return "";
        } else {
            return retValue.toString();
        }
    }

    private String getProp(String name) {
        return this.getProp(_getValueObject(), name);
    }

    /**
     * getter pour l'attribut niveau.
     * 
     * @param idNiveau
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut niveau
     * 
     * @throws IndexOutOfBoundsException
     *             s'il n'y a pas d'elements au niveau idNiveau
     */
    @Override
    public ICTListeTextes getTextes(int idNiveau) {
        ICTListeTextes retValue = (ICTListeTextes) niveaux.get(new Integer(idNiveau));

        if (retValue == null) {
            throw new IndexOutOfBoundsException("pas de textes au niveau " + idNiveau);
        }

        return retValue;
    }

    /**
     * getter pour l'attribut actif.
     * 
     * @return la valeur courante de l'attribut actif
     */
    @Override
    public Boolean isActif() {
        return (Boolean) _getValueObject().getProperty(ICTDocumentHelper.PROP_ACTIF);
    }

    /**
     * getter pour l'attribut defaut.
     * 
     * @return la valeur courante de l'attribut defaut
     */
    @Override
    public Boolean isDefaut() {
        return (Boolean) _getValueObject().getProperty(ICTDocumentHelper.PROP_DEFAUT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.ICTDocument#isStyledDocument()
     */
    @Override
    public Boolean isStyledDocument() {
        return (Boolean) _getValueObject().getProperty(ICTDocumentHelper.PROP_STYLED_DOCUMENT);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see ICTDocument#load()
     */
    @Override
    public ICTDocument[] load() throws Exception {
        // instancie le manager (globaz.babel.db.CTDocumentAPIAdapter) et
        // effectue la recherche multicritere
        Object[] result = _getArray("load", new Object[] { getIdDocument(), getCsDomaine(), getCsTypeDocument(),
                getCsDestinataire(), getIsDefaut(), getIsActif(), getNom(), getNomLike(), getCodeIsoLangue() });

        if ((result != null) && (result.length > 0)) {
            HashMap documents = new HashMap();

            // regroupe les vo resultats par id de document
            for (int idResult = 0; idResult < result.length; ++idResult) {
                ArrayList vos = (ArrayList) documents.get(((GlobazValueObject) result[idResult])
                        .getProperty("idDocument"));

                if (vos == null) {
                    vos = new ArrayList();
                    documents.put(((GlobazValueObject) result[idResult]).getProperty("idDocument"), vos);
                }

                vos.add(result[idResult]);
            }

            // cree une instance de ICTDocumentHelper par nom de document
            // différent
            if (!documents.isEmpty()) {
                ICTDocument[] retValue = new ICTDocument[documents.size()];
                int idResult = 0;

                for (Iterator iter = documents.values().iterator(); iter.hasNext(); ++idResult) {
                    ArrayList vos = (ArrayList) iter.next();

                    retValue[idResult] = new ICTDocumentHelper(
                            (GlobazValueObject[]) vos.toArray(new GlobazValueObject[vos.size()]));
                }

                return retValue;
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see ICTDocument#loadListeNoms()
     */
    @Override
    public Map loadListeNoms() throws Exception {
        // instancie le manager (globaz.babel.db.CTDocumentAPIAdapter) et
        // effectue la recherche multicritere
        Object[] result = _getArray("loadListeNoms", new Object[] { getIdDocument(), getCsDomaine(),
                getCsTypeDocument(), getCsDestinataire(), getIsDefaut(), getIsActif(), getNom() });

        // retour des valeurs
        HashMap retValue = new HashMap();

        if (result != null) {
            for (int idDocument = 0; idDocument < result.length; ++idDocument) {
                GlobazValueObject vo = (GlobazValueObject) result[idDocument];

                retValue.put(this.getProp(vo, ICTDocumentHelper.PROP_ID_DOCUMENT),
                        this.getProp(vo, ICTDocumentHelper.PROP_NOM));
            }
        }

        return retValue;
    }

    /**
     * setter pour l'attribut actif.
     * 
     * @param actif
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setActif(Boolean actif) {
        setProp(ICTDocumentHelper.PROP_ACTIF, actif);
    }

    /**
     * setter pour l'attribut code iso langue.
     * 
     * @param codeIsoLangue
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setCodeIsoLangue(String codeIsoLangue) {
        codeIsoLangue = codeIsoLangue.toLowerCase();

        if (codeIsoLangue.equals(Locale.GERMAN.getLanguage()) || codeIsoLangue.equals(Locale.FRENCH.getLanguage())
                || codeIsoLangue.equals(Locale.ITALIAN.getLanguage())) {
            setProp(ICTDocumentHelper.PROP_CODE_ISO_LANGUE, codeIsoLangue);
        }
    }

    /**
     * setter pour l'attribut cs destinataire.
     * 
     * @param csDestinataire
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setCsDestinataire(String csDestinataire) {
        setProp(ICTDocumentHelper.PROP_CS_DESTINATAIRE, csDestinataire);
    }

    /**
     * setter pour l'attribut cs domaine.
     * 
     * @param csDomaine
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setCsDomaine(String csDomaine) {
        setProp(ICTDocumentHelper.PROP_CS_DOMAINE, csDomaine);
    }

    /**
     * setter pour l'attribut cs type document.
     * 
     * @param csTypeDocument
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setCsTypeDocument(String csTypeDocument) {
        setProp(ICTDocumentHelper.PROP_CS_TYPE_DOCUMENT, csTypeDocument);
    }

    /**
     * setter pour l'attribut default.
     * 
     * @param defaut
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setDefault(Boolean defaut) {
        setProp(ICTDocumentHelper.PROP_DEFAUT, defaut);
    }

    /**
     * setter pour l'attribut id document.
     * 
     * @param idDocument
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setIdDocument(String idDocument) {
        setProp(ICTDocumentHelper.PROP_ID_DOCUMENT, idDocument);
    }

    /**
     * setter pour l'attribut nom.
     * 
     * @param nom
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setNom(String nom) {
        setProp(ICTDocumentHelper.PROP_NOM, nom);
    }

    @Override
    public void setNomLike(String nom) {
        setProp(ICTDocumentHelper.PROP_NOM_LIKE, nom);
    }

    private void setProp(String name, Object value) {
        _getValueObject().setProperty(name, value);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public int size() {
        return niveaux.size();
    }

}
