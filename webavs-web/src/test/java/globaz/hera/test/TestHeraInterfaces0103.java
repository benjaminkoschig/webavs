/*
 * Créé le 18 août 06
 */
package globaz.hera.test;


/**
 * @author hpe
 * 
 *         Classe permettant de tester les interfaces d'HERA --> ID = 0103
 */
public class TestHeraInterfaces0103 {

    /**
     * 
     * Le test 0103a est fait pour le requérant :
     * 
     * N° AVS : 217.57.338.219 Nom : Brugger Daniel Date : date courante
     * 
     * Il doit retourner :
     * 
     * Père1, mère1 --> Divorcé Père1, mère2 --> Marié Mère1, Père2 --> Divorcé
     * 
     */

    // public void test0103a() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // String date = null;
    //
    // //Retrouver l'idTiers
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "217.57.338.219");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    //
    //
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFRelationFamiliale[] rfs =
    // sitFam.getRelationsConjointsEtendues(idTiers, date);
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0103a ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    //
    //
    // for (int i = 0; i < rfs.length; i++) {
    // ISFRelationFamiliale rf = rfs[i];
    //
    // sb.append("Liens : " + session.getCodeLibelle(rf.getTypeLien()) + "\n");
    // sb.append(rf.getDateDebut() + " -> " + rf.getDateFin()+ "\n");
    // sb.append(rf.getNomHomme() + " " + rf.getPrenomHomme() + " " +
    // rf.getNoAvsHomme()+ "\n");
    // sb.append(rf.getNomFemme() + " " +rf.getPrenomFemme() + " " +
    // rf.getNoAvsFemme()+ "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    // }
    // System.out.println(sb.toString());
    //
    // }
    //
    // /**
    // *
    // * Le test 0103b est fait pour le requérant :
    // *
    // * N° AVS : 217.57.338.219
    // * Nom : Brugger Daniel
    // * Date : 05.01.2000
    // *
    // * Il doit retourner :
    // *
    // * Père1, mère1 --> Marié
    // * Mère1, Père2 --> Divorcé
    // *
    // */
    //
    // public void test0103b() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // String date = "05.01.2000";
    //
    // //Retrouver l'idTiers
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "217.57.338.219");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    //
    //
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFRelationFamiliale[] rfs =
    // sitFam.getRelationsConjointsEtendues(idTiers, date);
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0103b ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    //
    //
    // for (int i = 0; i < rfs.length; i++) {
    // ISFRelationFamiliale rf = rfs[i];
    //
    // sb.append("Liens : " + session.getCodeLibelle(rf.getTypeLien()) + "\n");
    // sb.append(rf.getDateDebut() + " -> " + rf.getDateFin()+ "\n");
    // sb.append(rf.getNomHomme() + " " + rf.getPrenomHomme() + " " +
    // rf.getNoAvsHomme()+ "\n");
    // sb.append(rf.getNomFemme() + " " +rf.getPrenomFemme() + " " +
    // rf.getNoAvsFemme()+ "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    // }
    // System.out.println(sb.toString());
    //
    // }
    //
    // /**
    // *
    // * Le test 0103c est fait pour le requérant :
    // *
    // * N° AVS : 368.62.424.144
    // * Nom : Frossard Dominique
    // * Date : date courante
    // *
    // * Il doit retourner :
    // *
    // * Père4, Mère4 --> Divorcé
    // *
    // */
    //
    // public void test0103c() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // String date = null;
    //
    // //Retrouver l'idTiers
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "368.62.424.144");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    //
    //
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFRelationFamiliale[] rfs =
    // sitFam.getRelationsConjointsEtendues(idTiers, date);
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0103c ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    //
    //
    // for (int i = 0; i < rfs.length; i++) {
    // ISFRelationFamiliale rf = rfs[i];
    //
    // sb.append("Liens : " + session.getCodeLibelle(rf.getTypeLien()) + "\n");
    // sb.append(rf.getDateDebut() + " -> " + rf.getDateFin()+ "\n");
    // sb.append(rf.getNomHomme() + " " + rf.getPrenomHomme() + " " +
    // rf.getNoAvsHomme()+ "\n");
    // sb.append(rf.getNomFemme() + " " +rf.getPrenomFemme() + " " +
    // rf.getNoAvsFemme()+ "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    // }
    // System.out.println(sb.toString());
    //
    // }
    //
    // /**
    // *
    // * Le test 0103d est fait pour le requérant :
    // *
    // * N° AVS : 368.59.329.114
    // * Nom : Froidevaux Bernard
    // * Date : date courante
    // *
    // * Il doit retourner :
    // *
    // * Père2, Mère1 --> Divorcé
    // * Mère1, Père1 --> Divorcé
    // *
    // */
    //
    // public void test0103d() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // String date = null;
    //
    // //Retrouver l'idTiers
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "368.59.329.114");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    //
    //
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFRelationFamiliale[] rfs =
    // sitFam.getRelationsConjointsEtendues(idTiers, date);
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0103d ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    //
    //
    // for (int i = 0; i < rfs.length; i++) {
    // ISFRelationFamiliale rf = rfs[i];
    //
    // sb.append("Liens : " + session.getCodeLibelle(rf.getTypeLien()) + "\n");
    // sb.append(rf.getDateDebut() + " -> " + rf.getDateFin()+ "\n");
    // sb.append(rf.getNomHomme() + " " + rf.getPrenomHomme() + " " +
    // rf.getNoAvsHomme()+ "\n");
    // sb.append(rf.getNomFemme() + " " +rf.getPrenomFemme() + " " +
    // rf.getNoAvsFemme()+ "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    // }
    // System.out.println(sb.toString());
    //
    // }
    //
    // /**
    // *
    // * Le test 0103e est fait pour le requérant :
    // *
    // * N° AVS : 368.59.329.114
    // * Nom : Froidevaux Bernard
    // * Date : 10.01.2000
    // *
    // * Il doit retourner :
    // *
    // * Père2, Mère1 --> Divorcé
    // * Mère1, Père1 --> Marié
    // *
    // */
    //
    // public void test0103e() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // String date = "10.01.2000";
    //
    // //Retrouver l'idTiers
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "368.59.329.114");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    //
    //
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFRelationFamiliale[] rfs =
    // sitFam.getRelationsConjointsEtendues(idTiers, date);
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0103e ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    //
    //
    // for (int i = 0; i < rfs.length; i++) {
    // ISFRelationFamiliale rf = rfs[i];
    //
    // sb.append("Liens : " + session.getCodeLibelle(rf.getTypeLien()) + "\n");
    // sb.append(rf.getDateDebut() + " -> " + rf.getDateFin()+ "\n");
    // sb.append(rf.getNomHomme() + " " + rf.getPrenomHomme() + " " +
    // rf.getNoAvsHomme()+ "\n");
    // sb.append(rf.getNomFemme() + " " +rf.getPrenomFemme() + " " +
    // rf.getNoAvsFemme()+ "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    // }
    // System.out.println(sb.toString());
    //
    // }
    //
    // /**
    // *
    // * Le test 0103f est fait pour le requérant :
    // *
    // * N° AVS : 368.59.329.114
    // * Nom : Froidevaux Bernard
    // * Date : 21.08.1997
    // *
    // * Il doit retourner :
    // *
    // * Père2, Mère1 --> Séparé
    // *
    // */
    //
    // public void test0103f() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // String date = "21.08.1997";
    //
    // //Retrouver l'idTiers
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "368.59.329.114");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    //
    //
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFRelationFamiliale[] rfs =
    // sitFam.getRelationsConjointsEtendues(idTiers, date);
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0103f ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    //
    //
    // for (int i = 0; i < rfs.length; i++) {
    // ISFRelationFamiliale rf = rfs[i];
    //
    // sb.append("Liens : " + session.getCodeLibelle(rf.getTypeLien()) + "\n");
    // sb.append(rf.getDateDebut() + " -> " + rf.getDateFin()+ "\n");
    // sb.append(rf.getNomHomme() + " " + rf.getPrenomHomme() + " " +
    // rf.getNoAvsHomme()+ "\n");
    // sb.append(rf.getNomFemme() + " " +rf.getPrenomFemme() + " " +
    // rf.getNoAvsFemme()+ "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    // }
    // System.out.println(sb.toString());
    //
    // }

}
