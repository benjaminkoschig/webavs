package globaz.cygnus.utils;

import globaz.cygnus.db.documents.RFDocuments;
import globaz.cygnus.db.documents.RFDocumentsManager;
import globaz.cygnus.db.documents.RFTypeDocuments;
import globaz.cygnus.db.documents.RFTypeDocumentsManager;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JAVector;
import java.util.Iterator;
import java.util.Vector;
import ch.globaz.cygnus.process.document.RFDocumentEnum;

/**
 * author fha
 */
public class RFDocumentsListsBuilder {

    private static RFDocumentsListsBuilder instance;

    /**
     * Récupère l'instance unique de la class Singleton.
     * <p>
     * Remarque : le constructeur est rendu inaccessible
     * 
     * @return RFSoinsListsBuilder
     */
    public static RFDocumentsListsBuilder getInstance(BSession session) {
        if (null == RFDocumentsListsBuilder.instance) { // Premier appel
            RFDocumentsListsBuilder.instance = new RFDocumentsListsBuilder(session);
        }

        return RFDocumentsListsBuilder.instance;
    }

    private String documentsParInnerJavascript = "";

    private Vector typeDocuments = null;

    /**
     * Constructeur redéfini comme étant privé pour interdire son appel et forcer à passer par la méthode <link
     * 
     */
    private RFDocumentsListsBuilder(BSession session) {
        buildDocumentsInnerJavascript(session);
        buildTypeDocuments(session);
    }

    /**
     * Méthode qui construit un tableau javascript de sous type de soins à 2 dimension (code,CSlibelle)
     * 
     * Méthode qui construit un tableau javascript de documents (CSlibelle)
     * 
     */
    private void buildDocumentsInnerJavascript(BSession session) {

        try {
            // TODO: Attention dangereux dépend de l'ordre !! créer un tableau
            // associatif en javascript
            StringBuffer bufferStr = new StringBuffer();

            StringBuffer libelleDocumentsStr = new StringBuffer();
            StringBuffer codeDocumentsStr = new StringBuffer();
            StringBuffer isGedStr = new StringBuffer();// Chaine pour setter false/true --> mise en ged

            RFDocumentsManager documentsManager = new RFDocumentsManager();
            documentsManager.setSession(session);
            documentsManager.find(BManager.SIZE_NOLIMIT);

            JAVector documentData = documentsManager.getContainer();
            String idDocuments = "";
            String typeDocuments = "";
            RFDocuments currentDocuments = null;
            boolean fin = false;

            bufferStr.append("var documentsTab=[");

            // pour chaque type de document
            for (int typeDeDocument = 0; typeDeDocument <= documentData.size();) {
                if (null == currentDocuments) {
                    currentDocuments = (RFDocuments) documentData.get(typeDeDocument);
                    typeDeDocument++;
                }

                idDocuments = currentDocuments.getIdDocument();
                typeDocuments = currentDocuments.getTypeDocument();

                bufferStr.append("new Array(");
                codeDocumentsStr.append("[");
                libelleDocumentsStr.append("[");
                // ged
                isGedStr.append("[");

                // pour chaque document
                while ((typeDocuments.compareTo(currentDocuments.getTypeDocument()) == 0) && !fin) {
                    codeDocumentsStr.append("\"" + currentDocuments.getIdDocument() + "\"");
                    libelleDocumentsStr.append("\"" + session.getCodeLibelle(currentDocuments.getIdDocument()) + "\"");

                    isGedStr.append(setIsDocumentForGed(currentDocuments.getIdDocument()));

                    if (typeDeDocument < documentData.size()) {
                        currentDocuments = (RFDocuments) documentData.get(typeDeDocument);
                        typeDeDocument++;
                    } else {
                        fin = true;
                        typeDeDocument++;
                    }

                    if ((typeDocuments.compareTo(currentDocuments.getTypeDocument()) == 0) && !fin) {
                        codeDocumentsStr.append(",");
                        libelleDocumentsStr.append(",");
                        isGedStr.append(",");

                    } else {
                        codeDocumentsStr.append("]");
                        libelleDocumentsStr.append("]");
                        isGedStr.append("]");
                    }
                }

                bufferStr.append(codeDocumentsStr.toString());
                bufferStr.append("," + libelleDocumentsStr.toString());
                bufferStr.append("," + isGedStr.toString());
                // cs
                if (typeDeDocument <= documentData.size()) {
                    bufferStr.append("),");
                } else {
                    bufferStr.append(")");
                }
                codeDocumentsStr = new StringBuffer();
                libelleDocumentsStr = new StringBuffer();
                isGedStr = new StringBuffer();
            }

            bufferStr.append("];");

            documentsParInnerJavascript = bufferStr.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode qui construit un tableau javascript de type de documents(code,CSlibelle)
     * 
     * Méthode qui construit un tableau javascript de type de documents (csLibelle)
     * 
     * @return String
     */
    private void buildTypeDocuments(BSession session) {

        try {
            RFTypeDocumentsManager documentsManager = new RFTypeDocumentsManager();
            documentsManager.setSession(session);
            documentsManager.changeManagerSize(0);
            documentsManager.find();

            Iterator<RFTypeDocuments> documentIter = documentsManager.getContainer().iterator();
            typeDocuments = new Vector();
            // Ajout de l'élément null
            typeDocuments.add(new String[] { "", "" });

            while (documentIter.hasNext()) {
                RFTypeDocuments currentDocument = documentIter.next();
                typeDocuments.add(new String[] { session.getCodeLibelle(currentDocument.getIdTypeDocument()) });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDocumentsParInnerJavascript() {

        return documentsParInnerJavascript;
    }

    public Vector getTypeDocuments() {
        return typeDocuments;
    }

    private Boolean setIsDocumentForGed(String cs) throws Exception {

        for (RFDocumentEnum doc : RFDocumentEnum.values()) {

            if (doc.getCsDocument().equals(cs)) {
                if ((doc.getIsGed() != null) && doc.getIsGed()) {
                    return true;
                }
            }
        }
        return false;
    }
}
