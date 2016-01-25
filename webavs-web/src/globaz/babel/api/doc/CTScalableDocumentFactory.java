/*
 * Créé le 15 nov. 06
 */
package globaz.babel.api.doc;

import globaz.babel.api.doc.impl.CTScalableDocumentAnnexe;
import globaz.babel.api.doc.impl.CTScalableDocumentCopie;
import globaz.babel.api.doc.impl.CTScalableDocumentNiveau;
import globaz.babel.api.doc.impl.CTScalableDocumentPosition;
import globaz.babel.api.doc.impl.CTScalableDocumentProperties;

/**
 * Description Factory retournant les implementation des interfaces permetants la gestion des scalableDocument.
 * 
 * @author bsc
 * 
 */
public class CTScalableDocumentFactory {

    /** Class statique, référence par cette instance */
    private static CTScalableDocumentFactory instance = new CTScalableDocumentFactory();

    /**
     * @return L'instance de cette classe
     * @throws Exception
     */
    public static CTScalableDocumentFactory getInstance() {
        return instance;
    }

    private Integer nextAnnexeKey = new Integer(Integer.MIN_VALUE);
    private Integer nextCopieKey = new Integer(Integer.MIN_VALUE);

    private Integer nextNiveauKey = new Integer(Integer.MIN_VALUE);

    private Integer nextPositionKey = new Integer(Integer.MIN_VALUE);

    /**
     * Constructeur prive
     * 
     * @throws Exception
     */
    private CTScalableDocumentFactory() {
    }

    /**
     * Retourne l'implementation de ICTScalableDocumentAnnexe
     * 
     * @return la nouvelle annexe
     * @throws Exception
     */
    public ICTScalableDocumentAnnexe createNewScalableDocumentAnnexe() throws Exception {
        ICTScalableDocumentAnnexe newScalableDocAnnexe = new CTScalableDocumentAnnexe(getNextAnnexeKey());
        return newScalableDocAnnexe;
    }

    /**
     * Retourne l'implementation de ICTScalableDocumentCopie
     * 
     * @return la nouvelle copie
     * @throws Exception
     */
    public ICTScalableDocumentCopie createNewScalableDocumentCopie() throws Exception {
        ICTScalableDocumentCopie newScalableDocCopie = new CTScalableDocumentCopie(getNextCopieKey());
        return newScalableDocCopie;
    }

    /**
     * Retourne l'implementation de ICTScalableDocumentNiveau
     * 
     * @return le nouveau niveau
     * @throws Exception
     */
    public ICTScalableDocumentNiveau createNewScalableDocumentNiveau() throws Exception {
        ICTScalableDocumentNiveau newScalableDocNiveau = new CTScalableDocumentNiveau(getNextNiveauKey());
        return newScalableDocNiveau;
    }

    /**
     * Retourne l'implementation de ICTScalableDocumentPosition
     * 
     * @return la nouvelle position
     * @throws Exception
     */
    public ICTScalableDocumentPosition createNewScalableDocumentPosition() throws Exception {
        ICTScalableDocumentPosition newScalableDocPosition = new CTScalableDocumentPosition(getNextPositionKey());
        return newScalableDocPosition;
    }

    /**
     * Retourne l'implementation de ICTScalableDocumentProperties
     * 
     * @return le document
     * @throws Exception
     */
    public ICTScalableDocumentProperties createNewScalableDocumentProperties() throws Exception {
        ICTScalableDocumentProperties newScalableDocProp = new CTScalableDocumentProperties();
        return newScalableDocProp;
    }

    /**
     * @return
     */
    private synchronized Integer getNextAnnexeKey() {
        Integer key = nextAnnexeKey;

        if (nextAnnexeKey.intValue() == Integer.MAX_VALUE) {
            nextAnnexeKey = new Integer(Integer.MIN_VALUE);
        } else {
            nextAnnexeKey = new Integer(nextAnnexeKey.intValue() + 1);
        }

        return key;
    }

    /**
     * @return
     */
    private synchronized Integer getNextCopieKey() {
        Integer key = nextCopieKey;

        if (nextCopieKey.intValue() == Integer.MAX_VALUE) {
            nextCopieKey = new Integer(Integer.MIN_VALUE);
        } else {
            nextCopieKey = new Integer(nextCopieKey.intValue() + 1);
        }

        return key;
    }

    /**
     * @return
     */
    private synchronized Integer getNextNiveauKey() {
        Integer key = nextNiveauKey;

        if (nextNiveauKey.intValue() == Integer.MAX_VALUE) {
            nextNiveauKey = new Integer(Integer.MIN_VALUE);
        } else {
            nextNiveauKey = new Integer(nextNiveauKey.intValue() + 1);
        }

        return key;
    }

    /**
     * @return
     */
    private synchronized Integer getNextPositionKey() {
        Integer key = nextPositionKey;

        if (nextPositionKey.intValue() == Integer.MAX_VALUE) {
            nextPositionKey = new Integer(Integer.MIN_VALUE);
        } else {
            nextPositionKey = new Integer(nextPositionKey.intValue() + 1);
        }

        return key;
    }

}
