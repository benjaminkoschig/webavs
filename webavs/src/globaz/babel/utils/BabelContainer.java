package globaz.babel.utils;

import globaz.babel.api.ICTDocument;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.perseus.business.constantes.IPFCatalogueTextes;

/**
 * Permet de charger un (ou plusieurs) catalogue(s) de texte et d'en r�cup�rer les valeurs. <br/>
 * <br/>
 * L'acc�s aux valeurs se fait gr�ce � une cl� d�finissant le catalogue et au syst�me de niveau/position. <br/>
 * La cl� est g�n�r�e par la m�thode {@link CatalogueText#getKey()}, il est donc vivement conseill� d'utiliser un objet
 * {@link CatalogueText} pour utiliser cette classe
 * 
 * @see CatalogueText
 * @author MBO
 * @author PBA
 */
public class BabelContainer {

    /** @deprecated assure la compatibilit� avec l'ancienne version */
    @Deprecated
    private static final String csDomaineParDefault = IPFCatalogueTextes.CS_PF;
    /** @deprecated assure la compatibilit� avec l'ancienne version */
    @Deprecated
    private static final String nomCatalogueParDefault = IPFCatalogueTextes.NOM_OPEN_OFFICE;

    private Map<String, ICTDocument> container = null;
    private List<CatalogueText> listeCatalogues = null;
    /** N�cessaire pour une utilisation dans l'ancien framework */
    private BSession session = null;

    public BabelContainer() {
        super();

        container = new HashMap<String, ICTDocument>();
        listeCatalogues = new ArrayList<CatalogueText>();
    }

    /**
     * Ajoute un catalogue pour un chargement ult�rieur (par la m�thode {@link #load()})
     * 
     * @param catalogueText
     */
    public void addCatalogueText(CatalogueText catalogueText) {
        if (catalogueText != null) {
            listeCatalogues.add(catalogueText);
        }
    }

    /**
     * Ajoute un catalogue pour un chargement ult�rieur (par la m�thode {@link #load()})<br/>
     * Il est pr�f�rable d'utiliser la m�thode {@link #addCatalogueText(CatalogueText)} pour pouvoir par la suite
     * acc�der aux valeurs du catalogue gr�ce � la cl� g�n�r�e par {@link CatalogueText#getKey()} sans devoir
     * r�-instancier un objet de ce type
     * 
     * @param csDomaine
     * @param nomCatalogue
     * @param csTypeDocument
     */
    public void addCatalogueText(String csDomaine, String nomCatalogue, String csTypeDocument) {
        if (!JadeStringUtil.isBlank(csDomaine) && !JadeStringUtil.isBlank(nomCatalogue)
                && !JadeStringUtil.isBlank(csTypeDocument)) {
            CatalogueText catalogueText = new CatalogueText();
            catalogueText.setCsDomaine(csDomaine);
            catalogueText.setNomCatalogue(nomCatalogue);
            catalogueText.setCsTypeDocument(csTypeDocument);
            listeCatalogues.add(catalogueText);
        }
    }

    /**
     * Ajoute une liste de catalogues pour un chargement ult�rieur (par la m�thode {@link #load()})
     * 
     * @param catalogues
     */
    public void appendCataloguesText(List<CatalogueText> catalogues) {
        listeCatalogues.addAll(catalogues);
    }

    private boolean checkCodeIsoLangue(CatalogueText catalogueText) {
        return !JadeStringUtil.isBlank(catalogueText.getCodeIsoLangue())
                && ("fr".equalsIgnoreCase(catalogueText.getCodeIsoLangue())
                        || "de".equalsIgnoreCase(catalogueText.getCodeIsoLangue()) || "it"
                            .equalsIgnoreCase(catalogueText.getCodeIsoLangue()));
    }

    /**
     * Recherche la session dans le thread courant (nouveau framework :
     * {@link BSessionUtil#getSessionFromThreadContext()}).<br/>
     * Si introuvable, prend la session d�finie par {@link #setSession(BSession)} (pour l'ancien framework)
     * 
     * @return la session
     * @throws Exception
     *             si aucune des m�thodes ne trouve de session
     */
    public BSession getSession() throws Exception {
        BSession session = BSessionUtil.getSessionFromThreadContext();

        if (session != null) {
            return session;
        }

        if (this.session == null) {
            throw new Exception("Session null");
        }
        return this.session;
    }

    /**
     * Retourne le texte du catalogue d�fini par la cl� du catalogue, le niveau et la position du texte dans ce
     * catalogue
     * 
     * @param catalogue
     * @param niveau
     * @param position
     * @return le texte, ou <code>null</code> si le niveau et/ou la position dans le catalogue n'existe pas
     * @throws Exception
     *             si le catalogue est introuvable
     */
    public String getTexte(CatalogueText catalogue, int niveau, int position) throws Exception {
        return this.getTexte(catalogue.getKey(), niveau, position);
    }

    /**
     * Retourne le texte du catalogue d�fini par la cl� du catalogue (g�n�r�e par {@link CatalogueText#getKey()}), le
     * niveau et la position du texte dans ce catalogue
     * 
     * @param cleCatalogue
     *            cl� g�n�r�e par {@link CatalogueText#getKey()}
     * @param niveau
     * @param position
     * @return le texte, ou <code>null</code> si le niveau et/ou la position dans le catalogue n'existe pas
     * @throws CatalogueInexistantException
     *             si le catalogue est introuvable dans ce container
     * @throws TexteIntrouvalbeException
     *             si le texte voulu n'est pas d�fini dans le catalogue de texte
     */
    public String getTexte(String cleCatalogue, int niveau, int position) throws TexteIntrouvalbeException,
            CatalogueInexistantException {
        if (container.containsKey(cleCatalogue)) {
            try {
                return container.get(cleCatalogue).getTextes(niveau).getTexte(position).getDescription();
            } catch (Exception ex) {
                throw new TexteIntrouvalbeException(cleCatalogue, niveau, position);
            }
        } else {
            StringBuilder message = new StringBuilder();
            message.append("Catalogue ").append(cleCatalogue).append(" non d�fini dans ce container");
            throw new CatalogueInexistantException(message.toString());
        }
    }

    /**
     * M�thode permettant de charger tous les catalogues de textes d�finis au pr�alable
     * 
     * @throws Exception
     *             si le code ISO de la langue n'est pas d�fini ou non valide, ou si le chargement d'un catalogue
     *             rencontre un probl�me (par exemple, session introuvable)
     */
    public void load() throws Exception {
        for (CatalogueText unCatalogue : listeCatalogues) {
            if (checkCodeIsoLangue(unCatalogue)) {
                ICTDocument iTCDocCommune = null;
                iTCDocCommune = PRBabelHelper.getDocumentHelper(getSession());
                iTCDocCommune.setCsDomaine(unCatalogue.getCsDomaine());
                iTCDocCommune.setCsTypeDocument(unCatalogue.getCsTypeDocument());
                iTCDocCommune.setNom(unCatalogue.getNomCatalogue());
                iTCDocCommune.setActif(Boolean.TRUE);
                iTCDocCommune.setCodeIsoLangue(unCatalogue.getCodeIsoLangue().toUpperCase());

                ICTDocument[] documentsCharges = iTCDocCommune.load();

                if (documentsCharges == null) {
                    throw new Exception("Text catalog not found with key : " + unCatalogue.getKey());
                }

                for (ICTDocument document : documentsCharges) {
                    // remplace si d�j� pr�sent
                    container.put(unCatalogue.getKey(), document);
                }
            } else {
                throw new Exception("Fatal error, invalid codeIsolangue");
            }
        }
        listeCatalogues.clear();
    }

    /**
     * M�thode permettant de charger la listeCatalogue
     * 
     * @deprecated use {@link #appendCataloguesText(List) appendCataloguesText(List&lt;CatalogueText&gt;)}
     */
    @Deprecated
    public void RegisterCtx(List<String> list) {
        if (list != null) {
            for (String csTypeDocument : list) {
                CatalogueText catalogueText = new CatalogueText();
                catalogueText.setCsDomaine(BabelContainer.csDomaineParDefault);
                catalogueText.setCsTypeDocument(csTypeDocument);
                catalogueText.setNomCatalogue(BabelContainer.nomCatalogueParDefault);

                listeCatalogues.add(catalogueText);
            }
        }
    }

    /**
     * M�thode permettant d'ajouter un catalogue � la liste
     * 
     * @deprecated use {@link #addCatalogueText(String csDomaine, String nomCatalogue, String csTypeDocument)}
     */
    @Deprecated
    public void RegisterCtx(String csTypeDocument) {
        if (!JadeStringUtil.isBlank(csTypeDocument)) {
            CatalogueText catalogueText = new CatalogueText();
            catalogueText.setCsDomaine(BabelContainer.csDomaineParDefault);
            catalogueText.setCsTypeDocument(csTypeDocument);
            catalogueText.setNomCatalogue(BabelContainer.nomCatalogueParDefault);
            listeCatalogues.add(catalogueText);
        }
    }

    /**
     * D�fini la session pour cet objet, n�cessaire uniquement pour une utilisation avec l'ancien framework
     * 
     * @see #getSession()
     * @param session
     */
    public void setSession(BSession session) {
        this.session = session;
    }
}
