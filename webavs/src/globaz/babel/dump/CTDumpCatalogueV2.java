/*
 * 
 */
package globaz.babel.dump;

import globaz.babel.application.CTApplication;
import globaz.babel.db.cat.CTDocument;
import globaz.babel.db.cat.CTDocumentManager;
import globaz.babel.db.cat.CTElement;
import globaz.babel.db.cat.CTElementManager;
import globaz.babel.db.cat.CTTexte;
import globaz.babel.db.cat.CTTexteManager;
import globaz.babel.db.cat.CTTypeDocument;
import globaz.babel.db.cat.CTTypeDocumentManager;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.common.Jade;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Cree un script de sauvegarde d'un catalogue de texte et le sauvegarde dans un writer.
 * </p>
 * 
 * <p>
 * Les identifiants des lignes sont recréés Ã  partir de la borne inférieure. Ils sont indiqués sous la forme
 * 'XXX_BASE+id' de telle sorte qu'il est très facile de remplacer avec un editeur de texte les XXX_BASE par un nombre
 * correct pour eviter les doublons.
 * </p>
 * <b> Version initiale modifiée. Un problème de suppression à été détecté. Les corrections apportées sont les
 * suivantes: <b>problème</b>: la supression des au niveau des tables CTDOCUME, CTELEM et CTTEXTES, était effectué, pour
 * chque document, ce qui entraine la supression des dosumenst insérés précédemment pour un mêm type.
 * <b>corrections</b>: la méthode qui génère les scripts de suppression à été renommé, et est inséré une fois au niveau
 * du domaine. On supprime tout les documents du domaine. Si un type de document est passé en argument, on supprime
 * uniquement le type.
 * 
 * 
 * </b>
 * 
 * @author sce
 * @version 2
 */
public class CTDumpCatalogueV2 {
    private static final String DOC_BASE = "DOC_BASE";
    private static final String ELE_BASE = "ELE_BASE";
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    private static final String TEX_BASE = "TEX_BASE";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    /**
     * Un main pour lancer le dump des catalogues, la structure des arguments est la suivante:.
     * 
     * <ol>
     * <li>chemin vers un fichier de sortie</li>
     * <li>csDomaine ou le caractère . pour tous</li>
     * <li>csTypeDocument ou le caractère . pour tous</li>
     * <li>schema ou rien pour le schema par defaut (celui du globaz.xml)</li>
     * </ol>
     * 
     * @param args
     *            DOCUMENT ME!
     */
    public static void main(String[] args) {
        if ((args == null) || !((args.length == 4) || (args.length == 3))) {
            System.out
                    .println("Usage:\n\narg1: nom de fichier de sortie\narg2: csDomaine ou le caractère . pour tous\narg3: csTypeDocument ou le caractère . pour tous\narg4: schema ou rien pour le schema par defaut (celui du globaz.xml)");
            System.exit(1);
        }
        // crée les objets necessaires au fonctionnement
        FileWriter file = null;
        try {
            BSession session = (BSession) ((CTApplication) GlobazSystem
                    .getApplication(CTApplication.DEFAULT_APPLICATION_BABEL)).newSession("ccjuglo", "glob4az");
            file = new FileWriter(args[0]);
            new CTDumpCatalogueV2(session, args[1].equals(".") ? null : args[1], args[2].equals(".") ? null : args[2],
                    (args.length > 3) ? args[3] : null).dumpSQL(file);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            System.exit(0);
        }
    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String csDomaine;
    private String csTypeDocument;
    private DateFormat df = DateFormat.getDateTimeInstance();
    private Date now = new Date();
    private String schema;

    private BSession session;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    /**
     * Crée une nouvelle instance de la classe CTDumpCatalogueV2.
     * 
     * @param session
     *            une session (non null)
     * @param csDomaine
     *            un domaine ou null pour tous
     * @param csTypeDocument
     *            un type document ou null pour tous
     * @param schema
     *            un nom de schema ou null pour le schema configure pour l'application en cours
     */
    public CTDumpCatalogueV2(BSession session, String csDomaine, String csTypeDocument, String schema) {
        this.session = session;
        this.csDomaine = csDomaine;
        this.csTypeDocument = csTypeDocument;
        this.schema = schema;
    }

    /**
     * cree un script SQL de sauvegarde d'un catalogue de texte.
     * 
     * @param out
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void dumpSQL(Writer out) throws Exception {
        // preparation de l'exportation
        PrintWriter print = new PrintWriter(out);
        CTInsertQueryBuilder queryBuilder = new CTInsertQueryBuilder();
        // preparation de l'acces à  la base
        CTDocumentManager docs = new CTDocumentManager();
        CTElementManager elements = new CTElementManager();
        CTTexteManager textes = new CTTexteManager();
        elements.setSession(session);
        textes.setSession(session);
        docs.setSession(session);
        docs.setForCsDomaine(csDomaine);
        docs.setForCsTypeDocument(csTypeDocument);
        // Les docs sont triés par type de document.
        docs.find(BManager.SIZE_NOLIMIT);

        List typesDocumentTraites = new ArrayList();
        // création d'une transaction
        BTransaction transaction = null;
        try {
            transaction = (BTransaction) session.newTransaction();
            transaction.openTransaction();
            int docCount = 0;
            int eleCount = 0;
            int texCount = 0;

            // ******************Modification suppression ici, pour tout le domaine
            printDeleteCatalogueQuery(print, csDomaine, csTypeDocument);

            for (int idDoc = 0; idDoc < docs.size(); ++idDoc) {
                CTDocument doc = (CTDocument) docs.get(idDoc);

                // A chaque nouveau type de document, on remet les compteurs à
                // 0.
                // Cela ne doit pas poser de problème dans la générations des
                // id, car les bornes inférieurs
                // sont toutes liées au type de document.
                String idTypeDocument = doc.getIdTypeDocument();
                // Nouveau type de document... on remet les compteurs à zéro
                if (!typesDocumentTraites.contains(idTypeDocument)) {
                    typesDocumentTraites.add(idTypeDocument);
                    docCount = 0;
                    eleCount = 0;
                    texCount = 0;
                }

                docCount++;

                // exporter le document courant
                printComment(doc, print);
                // this.printDeleteCatalogueQuery(print, doc.getCsDomaine(), doc.getCsTypeDocument());

                printInsertQuery(doc, queryBuilder.reset(), doc.getIdTypeDocument(), 0, null, docCount,
                        CTDumpCatalogueV2.DOC_BASE, doc.getBorneInferieure(), print, transaction);
                // exporter les elements constituants ce document
                elements.setForIdDocument(doc.getIdDocument());
                elements.find(BManager.SIZE_NOLIMIT);
                for (int idElement = 0; idElement < elements.size(); ++idElement) {
                    CTElement element = (CTElement) elements.get(idElement);
                    eleCount++;
                    print.println();
                    printInsertQuery(element, queryBuilder.reset(), null, docCount, CTDumpCatalogueV2.DOC_BASE,
                            eleCount, CTDumpCatalogueV2.ELE_BASE, doc.getBorneInferieure(), print, transaction);
                    // exporter les textes de cet element
                    textes.setForIdElement(element.getIdElement());
                    textes.find(BManager.SIZE_NOLIMIT);
                    for (int idTexte = 0; idTexte < textes.size(); ++idTexte) {
                        texCount++;
                        printInsertQuery((CTTexte) textes.get(idTexte), queryBuilder.reset(), null, eleCount,
                                CTDumpCatalogueV2.ELE_BASE, texCount, CTDumpCatalogueV2.TEX_BASE,
                                doc.getBorneInferieure(), print, transaction);
                    }
                }
            }

            CTTypeDocumentManager typeDocMan = new CTTypeDocumentManager();
            typeDocMan.setSession(session);
            typeDocMan.setForCsDomaine(csDomaine);
            typeDocMan.setForCsTypeDocument(csTypeDocument);
            typeDocMan.find(BManager.SIZE_NOLIMIT);
            CTTypeDocument tdoc = new CTTypeDocument();
            print.println();
            print.println("--");
            print.println("-- Insertion des types de documents dans la table CTTYPDOC");
            print.println("--");
            print.println();
            CTTypeDocument tydoc = null;
            for (int i = 0; i < typeDocMan.size(); i++) {
                tydoc = (CTTypeDocument) typeDocMan.getEntity(i);
                printInsertCttypdoc(print, session, tydoc);
            }
            print.println();
            print.println("--");
            print.println("-- Requêtes de mise à jour pour FWINCP --");
            print.println("--");
            print.println();
            CTDocumentManager manDoc = new CTDocumentManager();

            for (int i = 0; i < typeDocMan.size(); i++) {

                // Traitement spécial pour les documents d'un même type, partagé
                // sur plusieurs domaine différent.
                // workaround en attendant que tous les documents de ce genre
                // soient corrigé.
                boolean isMultipleDocForSameTypeWithMultipleDomaine = false;

                FWCurrency maxIdDoc = new FWCurrency(0);
                FWCurrency maxIdElement = new FWCurrency(0);
                FWCurrency maxIdTexte = new FWCurrency(0);

                tdoc = (CTTypeDocument) typeDocMan.getEntity(i);

                // On récupère le max id du document :
                CTDocumentManager dMgr = new CTDocumentManager();
                dMgr.setSession(session);
                dMgr.setForCsTypeDocument(tdoc.getCsTypeDocument());
                dMgr.find();
                String domaine = null;
                for (int j = 0; j < dMgr.size(); j++) {
                    CTDocument doc = (CTDocument) dMgr.get(j);
                    String oldDomaine = domaine;
                    domaine = doc.getCsDomaine();
                    if ((oldDomaine != null) && !domaine.equals(oldDomaine)) {
                        isMultipleDocForSameTypeWithMultipleDomaine = true;
                        break;
                    }
                }

                if (isMultipleDocForSameTypeWithMultipleDomaine) {

                    for (int j = 0; j < dMgr.size(); j++) {
                        CTDocument entity = (CTDocument) dMgr.get(j);
                        FWCurrency idDoc = new FWCurrency(entity.getIdDocument());
                        idDoc.sub(new FWCurrency(entity.getBorneInferieure()));

                        if (maxIdDoc.compareTo(idDoc) <= 0) {
                            maxIdDoc = new FWCurrency(idDoc.toString());
                        }

                        CTElementManager eMgr = new CTElementManager();
                        eMgr.setSession(session);
                        eMgr.setForIdDocument(entity.getIdDocument());
                        eMgr.find(BManager.SIZE_NOLIMIT);
                        for (int k = 0; k < eMgr.size(); k++) {
                            CTElement el = (CTElement) eMgr.getEntity(k);

                            FWCurrency idElem = new FWCurrency(el.getIdElement());
                            idElem.sub(new FWCurrency(entity.getBorneInferieure()));

                            if (maxIdElement.compareTo(idElem) <= 0) {
                                maxIdElement = new FWCurrency(idElem.toString());
                            }

                            CTTexteManager tMgr = new CTTexteManager();
                            tMgr.setSession(session);
                            tMgr.setForIdElement(el.getIdElement());
                            tMgr.find(BManager.SIZE_NOLIMIT);
                            for (int l = 0; l < tMgr.size(); l++) {
                                CTTexte txt = (CTTexte) tMgr.getEntity(l);

                                FWCurrency idTxt = new FWCurrency(txt.getIdTexte());
                                idTxt.sub(new FWCurrency(entity.getBorneInferieure()));

                                if (maxIdTexte.compareTo(idTxt) <= 0) {
                                    maxIdTexte = new FWCurrency(idTxt.toString());
                                }
                            }
                        }
                    }
                }

                print.println();
                print.println("--");

                print.println("-- Type de document: " + tdoc.getCsTypeDocument());
                print.println("--");
                print.println();
                manDoc.setSession(session);
                manDoc.setForIdTypeDocument(tdoc.getIdTypeDocument());
                if (manDoc.getCount() == 0) {
                    // pas de documents pour ce type, la requete va lancer une
                    // erreur si on l'execute
                    // donc on ne l'ajoute pas
                    print.println("-- pas de documents");

                    continue;
                }

                String requestDoc = "";
                String requestElem = "";
                String requestTexte = "";

                if (isMultipleDocForSameTypeWithMultipleDomaine) {
                    requestDoc = String.valueOf(maxIdDoc.intValue());
                    requestElem = String.valueOf(maxIdElement.intValue());
                    requestTexte = String.valueOf(maxIdTexte.intValue());

                    print.println("-- Cas spécial, ce document utilise un type de document qui est partagé avec d'autres doc dans des domaines différents.: ");
                    print.println("-- On cumul les id max de tous ces document de meme type.");
                } else {
                    requestDoc += "((SELECT MAX(CBIDOC) FROM ";
                    requestDoc += getSchema();
                    requestDoc += ".CTDOCUME WHERE CBITYD = ";
                    requestDoc += tdoc.getIdTypeDocument();
                    requestDoc += ") - (SELECT DISTINCT min(CANBIN) FROM ";
                    requestDoc += getSchema();
                    requestDoc += ".CTTYPDOC WHERE CATTYP = ";
                    requestDoc += tdoc.getCsTypeDocument();

                    requestDoc += " and ";
                    requestDoc += " CATDOM = ";
                    requestDoc += tdoc.getCsDomaine();

                    requestDoc += "))";

                    requestElem += "((SELECT MAX(CCIELE) FROM ";
                    requestElem += getSchema();
                    requestElem += ".CTELEMEN WHERE CCIDOC IN (SELECT CBIDOC FROM ";
                    requestElem += getSchema();
                    requestElem += ".CTDOCUME WHERE CBITYD = ";
                    requestElem += tdoc.getIdTypeDocument();
                    requestElem += ")) - (SELECT DISTINCT min(CANBIN) FROM ";
                    requestElem += getSchema();
                    requestElem += ".CTTYPDOC WHERE CATTYP = ";
                    requestElem += tdoc.getCsTypeDocument();

                    requestElem += " and ";
                    requestElem += " CATDOM = ";
                    requestElem += tdoc.getCsDomaine();

                    requestElem += "))";

                    requestTexte += "((SELECT MAX (CDITXT) FROM ";
                    requestTexte += getSchema();
                    requestTexte += ".CTTEXTES WHERE CDIELE IN ";
                    requestTexte += "(SELECT CCIELE FROM ";
                    requestTexte += getSchema();
                    requestTexte += ".CTELEMEN WHERE CCIDOC IN (SELECT CBIDOC FROM ";
                    requestTexte += getSchema();
                    requestTexte += ".CTDOCUME WHERE CBITYD = ";
                    requestTexte += tdoc.getIdTypeDocument();
                    requestTexte += "))) - (SELECT DISTINCT min(CANBIN) FROM ";
                    requestTexte += getSchema();
                    requestTexte += ".CTTYPDOC WHERE CATTYP = ";
                    requestTexte += tdoc.getCsTypeDocument();

                    requestTexte += " and ";
                    requestTexte += " CATDOM = ";
                    requestTexte += tdoc.getCsDomaine();

                    requestTexte += "))";

                }

                printDeleteFwincp(print, "CTDOCUME", tdoc.getCsTypeDocument());
                printInsertFwincp(print, "CTDOCUME", tdoc.getCsTypeDocument());
                printUpdateFwincp(print, requestDoc, "CTDOCUME", tdoc.getCsTypeDocument());

                printDeleteFwincp(print, "CTELEMEN", tdoc.getCsTypeDocument());
                printInsertFwincp(print, "CTELEMEN", tdoc.getCsTypeDocument());
                printUpdateFwincp(print, requestElem, "CTELEMEN", tdoc.getCsTypeDocument());

                printDeleteFwincp(print, "CTTEXTES", tdoc.getCsTypeDocument());
                printInsertFwincp(print, "CTTEXTES", tdoc.getCsTypeDocument());
                printUpdateFwincp(print, requestTexte, "CTTEXTES", tdoc.getCsTypeDocument());
            }
            // affichage des infos sur le compte de lignes
            /*
             * print.println(); print.println("--"); print.println(); print.println
             * ("-- recommandations pour la mise à jour de FWINCP:"); print.println("--");
             * print.print("-- le nombre de documents: "); print.println(docCount);
             * print.print("-- le nombre d'éléments: "); print.println(eleCount);
             * print.print("-- le nombre de textes: "); print.println(texCount); print.println(); print.println("--");
             * print.println();
             */
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
    }

    /**
     * Ajout de la condition de suppression d'une clause and avec le type du doc
     * 
     * @param out
     */
    private void generateDeleteForType(PrintWriter out) {
        out.print(CTTypeDocument.FIELDNAME_CS_TYPE_DOCUMENT);
        out.print("=");
        out.print(csTypeDocument);
        out.print(" AND ");
    }

    String getSchema() {
        if (schema == null) {
            schema = Jade.getInstance().getDefaultJdbcSchema();
        }

        return schema;
    }

    private void printComment(CTDocument document, PrintWriter print) throws IOException {
        print.println();
        print.println();
        print.println("--");
        print.println();
        print.print("-- Dump de catalogue de texte: ");
        print.println(df.format(now));
        print.println("--");
        print.print("-- idTypeDocument\t\t= ");
        print.println(document.getIdTypeDocument());
        print.print("-- csDomaine\t\t\t= ");
        print.println(document.getCsDomaine());
        print.print("-- csTypeDocument\t\t= ");
        print.println(document.getCsTypeDocument());
        print.print("-- nomDocument\t\t\t= ");
        print.println(document.getNom());
        print.print("-- borneInferieure\t\t= ");
        print.println(document.getBorneInferieure());
        print.println("--");
        print.println("-- - Les identifiants de tous les enregistrements sont recalculés");
        print.print("-- - La chaîne ");
        print.print(CTDumpCatalogueV2.DOC_BASE);
        print.println(" doit être remplacée par la valeur du premier identifiant libre dans la base pour ce type de document");
        print.print("-- - La chaîne ");
        print.print(CTDumpCatalogueV2.ELE_BASE);
        print.println(" doit être remplacée par la valeur du premier identifiant libre dans la base pour ce type de document");
        print.print("-- - La chaîne ");
        print.print(CTDumpCatalogueV2.TEX_BASE);
        print.println(" être remplacée par la valeur du premier identifiant libre dans la base pour ce type de document");
        print.println();
        print.println("--");
        print.println();
    }

    private void printDeleteCatalogueQuery(PrintWriter out, String csDomaine, String csTypeDocument) throws IOException {
        out.println();
        // effacer les textes;
        out.print("DELETE FROM ");
        out.print(getSchema());
        out.print(".");
        out.print(CTTexte.TABLE_NAME_TEXTES);
        out.print(" WHERE ");
        out.print(CTTexte.FIELDNAME_ID_ELEMENT);
        out.print(" IN (SELECT ");
        out.print(CTElement.FIELDNAME_ID_ELEMENT);
        out.print(" FROM ");
        out.print(getSchema());
        out.print(".");
        out.print(CTElement.TABLE_NAME_ELEMENTS);
        out.print(" WHERE ");
        out.print(CTElement.FIELDNAME_ID_DOCUMENT);
        out.print(" IN (SELECT ");
        out.print(CTDocument.FIELDNAME_ID_DOCUMENT);
        out.print(" FROM ");
        out.print(getSchema());
        out.print(".");
        out.print(CTDocument.TABLE_NAME_DOCUMENT);
        out.print(" WHERE ");
        out.print(CTDocument.FIELDNAME_ID_TYPE_DOCUMENT);
        out.print(" IN (SELECT ");
        out.print(CTTypeDocument.FIELDNAME_ID_TYPE_DOCUMENT);
        out.print(" FROM ");
        out.print(getSchema());
        out.print(".");
        out.print(CTTypeDocument.TABLE_NAME_TYPE_DOCUMENT);
        out.print(" WHERE ");
        if (csTypeDocument != null) {
            generateDeleteForType(out);
        }
        // out.print(CTTypeDocument.FIELDNAME_CS_TYPE_DOCUMENT);
        // out.print("=");
        // out.print(csTypeDocument);
        // out.print(" AND ");
        out.print(CTTypeDocument.FIELDNAME_CS_DOMAINE);
        out.print("=");
        out.print(csDomaine);
        out.println(")));");
        // effacer les elements;
        out.print("DELETE FROM ");
        out.print(getSchema());
        out.print(".");
        out.print(CTElement.TABLE_NAME_ELEMENTS);
        out.print(" WHERE ");
        out.print(CTElement.FIELDNAME_ID_DOCUMENT);
        out.print(" IN (SELECT ");
        out.print(CTDocument.FIELDNAME_ID_DOCUMENT);
        out.print(" FROM ");
        out.print(getSchema());
        out.print(".");
        out.print(CTDocument.TABLE_NAME_DOCUMENT);
        out.print(" WHERE ");
        out.print(CTDocument.FIELDNAME_ID_TYPE_DOCUMENT);
        out.print(" IN (SELECT ");
        out.print(CTTypeDocument.FIELDNAME_ID_TYPE_DOCUMENT);
        out.print(" FROM ");
        out.print(getSchema());
        out.print(".");
        out.print(CTTypeDocument.TABLE_NAME_TYPE_DOCUMENT);
        out.print(" WHERE ");
        if (csTypeDocument != null) {
            generateDeleteForType(out);
        }
        // out.print(CTTypeDocument.FIELDNAME_CS_TYPE_DOCUMENT);
        // out.print("=");
        // out.print(csTypeDocument);
        // out.print(" AND ");
        out.print(CTTypeDocument.FIELDNAME_CS_DOMAINE);
        out.print("=");
        out.print(csDomaine);
        out.println("));");
        // effacer le document;
        out.print("DELETE FROM ");
        out.print(getSchema());
        out.print(".");
        out.print(CTDocument.TABLE_NAME_DOCUMENT);
        out.print(" WHERE ");
        out.print(CTDocument.FIELDNAME_ID_TYPE_DOCUMENT);
        out.print(" IN (SELECT ");
        out.print(CTTypeDocument.FIELDNAME_ID_TYPE_DOCUMENT);
        out.print(" FROM ");
        out.print(getSchema());
        out.print(".");
        out.print(CTTypeDocument.TABLE_NAME_TYPE_DOCUMENT);
        out.print(" WHERE ");
        if (csTypeDocument != null) {
            generateDeleteForType(out);
        }
        // out.print(CTTypeDocument.FIELDNAME_CS_TYPE_DOCUMENT);
        // out.print("=");
        // out.print(csTypeDocument);
        // out.print(" AND ");
        out.print(CTTypeDocument.FIELDNAME_CS_DOMAINE);
        out.print("=");
        out.print(csDomaine);
        out.println(");");
        out.println();
    }

    void printDeleteFwincp(PrintWriter out, String tableName, String typeDocument) throws IOException {
        out.print("DELETE FROM ");
        out.print(getSchema());
        out.print(".FWINCP WHERE PINCID = '");
        out.print(tableName);
        out.print("' AND PCOSID = ");
        out.print(typeDocument);
        out.println(";");
    }

    void printInsertCttypdoc(PrintWriter out, BSession session, CTTypeDocument tdoc) throws Exception {
        out.print("INSERT INTO ");
        out.print(getSchema());
        out.print(".CTTYPDOC (CAITYD,CATDOM,CATTYP,PSPY,CANBIN) VALUES (");
        out.print(tdoc.getIdTypeDocument());
        out.print(",");
        out.print(tdoc.getCsDomaine());
        out.print(",");
        out.print(tdoc.getCsTypeDocument());
        out.print(",'',");
        out.print(tdoc.getBorneInferieure());
        out.println(");");
    }

    void printInsertFwincp(PrintWriter out, String tableName, String typeDocument) throws IOException {
        out.print("INSERT INTO ");
        out.print(getSchema());
        out.print(".FWINCP (PINCID,PCOSID,PINCAN,PINCLI,PINCVA,PSPY) VALUES ('");
        out.print(tableName);
        out.print("',");
        out.print(typeDocument);
        out.println(",0,'',0,'');");
    }

    private void printInsertQuery(ICTExportableSQL exportable, CTInsertQueryBuilder queryBuilder,
            String forcedParentId, int parentId, String parentBase, int selfId, String selfBase, String binf,
            PrintWriter print, BTransaction transaction) throws IOException {
        exportable.export(queryBuilder, transaction);
        if (schema != null) {
            queryBuilder.setSchema(schema);
        }
        queryBuilder.printInsertQuery(print, forcedParentId, parentId, parentBase, selfId, selfBase, binf);
    }

    void printUpdateFwincp(PrintWriter out, String size, String tableName, String typeDocument) throws IOException {
        out.print("UPDATE ");
        out.print(getSchema());
        out.print(".FWINCP SET PINCVA= ");
        out.print(size);
        out.print(" WHERE PCOSID= ");
        out.print(typeDocument);
        out.print(" AND PINCID= '");
        out.print(tableName);
        out.println("';");
    }
}
