/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.document;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.helper.ICTDocumentHelper;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import ch.globaz.perseus.business.constantes.IPFCatalogueTextes;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;

/**
 * @author MBO
 * 
 */
public class BabelContainer {

    private String codeIsoLangue = null;
    private HashMap<String, ICTDocumentHelper> container = new HashMap<String, ICTDocumentHelper>();
    private List listeCatalogue = new ArrayList();

    public String getCodeIsoLangue() {
        return codeIsoLangue;
    }

    public String getCodeIsoLangue(String typeCatalogue) throws Exception {
        if (container.containsKey(typeCatalogue)) {
            ICTDocumentHelper doc = container.get(typeCatalogue);
            return doc.getCodeIsoLangue();
        } else {
            throw new DecisionException("BabelContainer - type de catalogue pas dans le container");
        }
    }

    /**
     * Methode qui va retourner le container.
     */
    public HashMap<String, ICTDocumentHelper> getContainer() {
        return container;
    }

    /**
     * Methode permettant d'obtenir le texte avec son niveau et sa position pour l'insertion dans le document.
     * 
     */
    public String getTexte(String typeCatalogue, int niveau, int position) throws Exception {
        if (container.containsKey(typeCatalogue)) {
            ICTDocumentHelper doc = container.get(typeCatalogue);
            return doc.getTextes(niveau).getTexte(position).getDescription();
        } else {
            throw new DecisionException("BabelContainer - texte absent dans le catalogue");
        }
    }

    /**
     * Methode permettant de charger tous les catalogues de textes concernés.
     */
    public void load() throws Exception {
        if (this.getCodeIsoLangue() != null) {
            for (Iterator iterator = listeCatalogue.iterator(); iterator.hasNext();) {
                String typeDocument = (String) iterator.next();
                ICTDocument iTCDocCommune = null;
                iTCDocCommune = PRBabelHelper.getDocumentHelper(BSessionUtil.getSessionFromThreadContext());
                iTCDocCommune.setCsDomaine(IPFCatalogueTextes.CS_PF);
                iTCDocCommune.setCsTypeDocument(typeDocument);
                iTCDocCommune.setNom(IPFCatalogueTextes.NOM_OPEN_OFFICE);
                iTCDocCommune.setDefault(Boolean.FALSE);
                iTCDocCommune.setActif(Boolean.TRUE);
                // TODO attention au format du CodeIsoLangue, doit être : "fr" "de" "it";
                iTCDocCommune.setCodeIsoLangue(codeIsoLangue);

                ICTDocument[] documents = iTCDocCommune.load();
                if (container.containsKey(typeDocument)) {
                    container.remove(typeDocument);
                    container.put(typeDocument, (ICTDocumentHelper) documents[0]);
                } else {
                    container.put(typeDocument, (ICTDocumentHelper) documents[0]);
                }

            }

            listeCatalogue.clear();
        } else {
            throw new Exception("BabelContainer - pas de codeIsolangue");
        }

    }

    /**
     * Methode permettant de charger la listeCatalogue
     */
    public void RegisterCtx(ArrayList<String> list) {

        if ((list != null) || (!list.isEmpty())) {
            listeCatalogue = list;
        }
    }

    /**
     * Methode permettant d'ajouter un catalogue à la liste
     */
    public void RegisterCtx(String nomCatalogue) {

        if ((nomCatalogue != null) || (!JadeStringUtil.isEmpty(nomCatalogue))) {
            listeCatalogue.add(nomCatalogue);
        }
    }

    public void setCodeIsoLangue(String codeIsoLangue) {
        this.codeIsoLangue = codeIsoLangue;
    }
}
