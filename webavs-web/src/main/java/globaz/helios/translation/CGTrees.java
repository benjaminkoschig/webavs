package globaz.helios.translation;

import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JATreeNode;
import globaz.helios.db.classifications.CGClasseCompte;
import globaz.helios.db.classifications.CGClasseCompteManager;
import globaz.helios.db.classifications.CGDefinitionListe;
import globaz.jade.client.util.JadeStringUtil;
import java.util.HashMap;

public class CGTrees {

    private static JATreeNode _classeCompteFind(JATreeNode tree, String id) {
        if (tree == null || tree.getValue() == null) {
            return null;
        }

        String[] classeCompte = (String[]) tree.getValue();
        if (classeCompte[0].equals(id)) {
            return tree;
        }

        for (int i = 0; i < tree.getChildrenCount(); i++) {
            JATreeNode result = _classeCompteFind(tree.getChildAt(i), id);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public static JATreeNode getClassesCompteExtendedTree(javax.servlet.http.HttpSession session,
            String idClassification) {

        JATreeNode tree = new JATreeNode();
        String[] temp = new String[2];
        temp[0] = "";
        temp[1] = "Tous";
        tree.setValue(temp);

        class HashItem {
            public Object item = null;

            public JATreeNode tree = null;

            public HashItem(Object item) {
                this.item = item;
            }
        }
        ;

        try {
            CGClasseCompteManager manager = new CGClasseCompteManager();
            manager.setSession((BSession) CodeSystem.getSession(session));
            manager.changeManagerSize(9999);
            manager.setForIdClassification(idClassification);
            // manager.setOrder("NOCLASSE");
            manager.find();

            HashMap hash = new HashMap(manager.size());
            for (int i = 0; i < manager.size(); i++) {
                hash.put(((CGClasseCompte) manager.getEntity(i)).getIdClasseCompte(),
                        new HashItem(manager.getEntity(i)));
            }

            for (int i = 0; i < manager.size(); i++) {
                CGClasseCompte classeTemp = (CGClasseCompte) manager.getEntity(i);
                HashItem classeHash = (HashItem) hash.get(classeTemp.getIdClasseCompte());
                JATreeNode treeTemp = null;
                boolean condition = true;
                if (classeHash.tree == null) {
                    do {
                        temp = new String[2];
                        temp[0] = classeTemp.getIdClasseCompte();
                        temp[1] = classeTemp.getNoClasse() + " " + classeTemp.getLibelle() + "\t" + "["
                                + classeTemp.getIdClasseCompte() + "]";
                        String stringForJavascript = "";
                        for (int j = 0; j < temp[1].length(); j++) {
                            if (temp[1].charAt(j) == '\'' || temp[1].charAt(j) == '"') {
                                stringForJavascript += "\\";
                            }
                            stringForJavascript += temp[1].charAt(j);
                        }
                        temp[1] = stringForJavascript;
                        if (classeHash.tree == null) {
                            classeHash.tree = new JATreeNode(temp);
                        }
                        if (treeTemp != null
                                && _classeCompteFind(classeHash.tree, ((String[]) treeTemp.getValue())[0]) == null) {
                            classeHash.tree.addChild(treeTemp);
                        }
                        treeTemp = classeHash.tree;
                        if (!classeTemp.getIdSuperClasse().equals("0")) {
                            classeHash = (HashItem) hash.get(classeTemp.getIdSuperClasse());
                            classeTemp = (CGClasseCompte) classeHash.item;
                        } else {
                            condition = false;
                        }
                    } while (condition);
                    if (_classeCompteFind(tree, classeTemp.getIdClasseCompte()) == null) {
                        tree.addChild(treeTemp);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            tree = null;
        }

        return tree;
    }

    public static JATreeNode getClassesCompteExtendedTreePlanComptable(javax.servlet.http.HttpSession session,
            String idClassification) {

        JATreeNode tree = new JATreeNode();
        String[] temp = new String[2];
        temp[0] = "";
        temp[1] = "Tous";
        tree.setValue(temp);

        class HashItem {
            public Object item = null;

            public JATreeNode tree = null;

            public HashItem(Object item) {
                this.item = item;
            }
        }
        ;

        try {
            CGClasseCompteManager manager = new CGClasseCompteManager();
            manager.setSession((BSession) CodeSystem.getSession(session));
            manager.changeManagerSize(9999);
            manager.setForIdClassification(idClassification);
            // manager.setOrder("NOCLASSE");
            manager.find();

            HashMap hash = new HashMap(manager.size());
            for (int i = 0; i < manager.size(); i++) {
                hash.put(((CGClasseCompte) manager.getEntity(i)).getIdClasseCompte(),
                        new HashItem(manager.getEntity(i)));
            }

            for (int i = 0; i < manager.size(); i++) {
                CGClasseCompte classeTemp = (CGClasseCompte) manager.getEntity(i);
                HashItem classeHash = (HashItem) hash.get(classeTemp.getIdClasseCompte());
                JATreeNode treeTemp = null;
                boolean condition = true;
                if (classeHash.tree == null) {
                    do {
                        temp = new String[2];
                        temp[0] = classeTemp.getIdClasseCompte();
                        temp[1] = classeTemp.getNoClasse() + " " + classeTemp.getLibelle();
                        String stringForJavascript = "";
                        for (int j = 0; j < temp[1].length(); j++) {
                            if (temp[1].charAt(j) == '\'' || temp[1].charAt(j) == '"') {
                                stringForJavascript += "\\";
                            }
                            stringForJavascript += temp[1].charAt(j);
                        }
                        temp[1] = stringForJavascript;
                        if (classeHash.tree == null) {
                            classeHash.tree = new JATreeNode(temp);
                        }
                        if (treeTemp != null
                                && _classeCompteFind(classeHash.tree, ((String[]) treeTemp.getValue())[0]) == null) {
                            classeHash.tree.addChild(treeTemp);
                        }
                        treeTemp = classeHash.tree;
                        if (!classeTemp.getIdSuperClasse().equals("0")) {
                            classeHash = (HashItem) hash.get(classeTemp.getIdSuperClasse());
                            classeTemp = (CGClasseCompte) classeHash.item;
                        } else {
                            condition = false;
                        }
                    } while (condition);
                    if (_classeCompteFind(tree, classeTemp.getIdClasseCompte()) == null) {
                        tree.addChild(treeTemp);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            tree = null;
        }

        return tree;
    }

    public static JATreeNode getClassesCompteTree(javax.servlet.http.HttpSession session, String definitionListeId) {

        if (definitionListeId == null) {
            definitionListeId = "";
        }

        String classificationId = "";

        if (!definitionListeId.equals("")) {
            try {
                CGDefinitionListe definitionListe = new CGDefinitionListe();
                definitionListe.setIdDefinitionListe(definitionListeId);
                definitionListe.setSession((BSession) CodeSystem.getSession(session));
                definitionListe.retrieve();
                classificationId = definitionListe.getIdClassification();
            } catch (Exception e) {
                return null;
            }
        }

        class HashItem {
            public Object item = null;

            public JATreeNode tree = null;

            public HashItem(Object item) {
                this.item = item;
            }
        }
        ;

        JATreeNode tree = new JATreeNode();
        String[] temp = new String[2];
        temp[0] = "";
        temp[1] = "Tous";
        tree.setValue(temp);

        try {
            CGClasseCompteManager manager = new CGClasseCompteManager();
            manager.setSession((BSession) CodeSystem.getSession(session));
            manager.setForIdClassification(classificationId);
            // manager.setOrder("NOCLASSE");
            manager.find(BManager.SIZE_NOLIMIT);

            HashMap hash = new HashMap(manager.size());
            for (int i = 0; i < manager.size(); i++) {
                hash.put(((CGClasseCompte) manager.getEntity(i)).getIdClasseCompte(),
                        new HashItem(manager.getEntity(i)));
            }

            for (int i = 0; i < manager.size(); i++) {
                CGClasseCompte classeTemp = (CGClasseCompte) manager.getEntity(i);
                HashItem classeHash = (HashItem) hash.get(classeTemp.getIdClasseCompte());

                JATreeNode treeTemp = null;
                boolean condition = true;
                if (classeHash.tree == null) {
                    while (condition) {
                        temp = new String[2];
                        temp[0] = classeTemp.getIdClasseCompte();
                        temp[1] = classeTemp.getLibelle();

                        if (temp[1].indexOf("\"") > -1) {
                            temp[1] = JadeStringUtil.change(temp[1], "\"", "\\\"");
                        }

                        if (classeHash.tree == null) {
                            classeHash.tree = new JATreeNode(temp);
                        }

                        if (treeTemp != null
                                && _classeCompteFind(classeHash.tree, ((String[]) treeTemp.getValue())[0]) == null) {
                            classeHash.tree.addChild(treeTemp);
                        }

                        treeTemp = classeHash.tree;
                        if (!classeTemp.getIdSuperClasse().equals("0")) {
                            classeHash = (HashItem) hash.get(classeTemp.getIdSuperClasse());
                            classeTemp = (CGClasseCompte) classeHash.item;
                        } else {
                            condition = false;
                        }
                    }

                    if (_classeCompteFind(tree, classeTemp.getIdClasseCompte()) == null) {
                        tree.addChild(treeTemp);
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            tree = null;
        }

        return tree;
    }

}
