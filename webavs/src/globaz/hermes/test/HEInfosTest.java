/*
 * Créé le 14 févr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hermes.test;

import globaz.globall.db.BSession;
import globaz.hermes.application.HEApplication;
import globaz.hermes.db.access.HEInfos;
import globaz.hermes.db.access.HEInfosManager;

/**
 * @author dostes Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class HEInfosTest {
    /**
     * Main
     */
    public static void main(String[] args) throws Exception {
        HEInfosTest test = new HEInfosTest();
        BSession session = new BSession(HEApplication.DEFAULT_APPLICATION_HERMES);
        session.connect("globazf", "ssiiadm");
        // test.testAdd(session);
        // test.testRetrieve(session);
        // test.testUpdate(session);
        // test.testRetrieve(session);
        // //test.testDelete(session);
        test.testFind(session);
        System.exit(0);
    }

    /**
     * Commentaire relatif au constructeur HEInfosTest
     */
    public HEInfosTest() {
        super();
    }

    //
    public void testAdd(BSession session) {
        HEInfos test = new HEInfos();
        test.setSession(session);
        test.setIdArc("1"); // Numeric
        test.setTypeInfo("2"); // Numeric
        test.setLibInfo("3"); // String
        try {
            test.add();
            if (test.hasErrors()) {
                System.out.println("Erreur");
            }
        } catch (Exception e) {
            System.out.println("e.toString()");
        }
    }

    public void testDelete(BSession session) {
        HEInfos test = new HEInfos();
        test.setSession(session);
        test.setIdInfoComp("");
        try {
            test.retrieve();
        } catch (Exception e) {
            System.out.println("Erreur retrieve");
        }
        try {
            test.delete();
        } catch (Exception e) {
            System.out.println("Erreur delete");
        }
    }

    public void testFind(BSession session) {
        HEInfosManager manager = new HEInfosManager();
        manager.setSession(session);
        // manager.setForIdInfoComp("");
        // manager.setForIdArc("");
        // manager.setForTypeInfo("");
        // manager.setForLibInfo("");
        // manager.setFromIdInfoComp("");
        // manager.setFromIdArc("");
        // manager.setFromTypeInfo("");
        // manager.setFromLibInfo("");
        try {
            manager.find();
        } catch (Exception e) {
            System.out.println("Erreur find");
        }
        System.out.println(" Manager contains " + manager.size() + " elements:");
        for (int i = 0; i < manager.size(); i++) {
            HEInfos test = (HEInfos) manager.getEntity(i);
            System.out.println("  id=" + manager.getEntity(i).toString());
            System.out.println("idInfoComp :" + test.getIdInfoComp());
            System.out.println("idArc :" + test.getIdArc());
            System.out.println("typeInfo :" + test.getTypeInfo());
            System.out.println("libInfo :" + test.getLibInfo());
        }
    }

    public void testRetrieve(BSession session) {
        HEInfos test = new HEInfos();
        test.setSession(session);
        test.setIdInfoComp("4");
        try {
            test.retrieve();
        } catch (Exception e) {
            System.out.println("Erreur retrieve");
        }
        System.out.println("idInfoComp :" + test.getIdInfoComp());
        System.out.println("idArc :" + test.getIdArc());
        System.out.println("typeInfo :" + test.getTypeInfo());
        System.out.println("libInfo :" + test.getLibInfo());
    }

    public void testUpdate(BSession session) {
        HEInfos test = new HEInfos();
        test.setSession(session);
        test.setIdInfoComp("4");
        try {
            test.retrieve();
        } catch (Exception e) {
            System.out.println("Erreur retrieve");
        }
        // mettre les champs a updater ici
        test.setIdArc("2");
        test.setLibInfo("new");
        try {
            test.update();
        } catch (Exception e) {
            System.out.println("Erreur update");
        }
    }
}