/*
 * Cr�� le 18 ao�t 06
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
     * Le test 0103a est fait pour le requ�rant :
     * 
     * N� AVS : 217.57.338.219 Nom : Brugger Daniel Date : date courante
     * 
     * Il doit retourner :
     * 
     * P�re1, m�re1 --> Divorc� P�re1, m�re2 --> Mari� M�re1, P�re2 --> Divorc�
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
    // * Le test 0103b est fait pour le requ�rant :
    // *
    // * N� AVS : 217.57.338.219
    // * Nom : Brugger Daniel
    // * Date : 05.01.2000
    // *
    // * Il doit retourner :
    // *
    // * P�re1, m�re1 --> Mari�
    // * M�re1, P�re2 --> Divorc�
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
    // * Le test 0103c est fait pour le requ�rant :
    // *
    // * N� AVS : 368.62.424.144
    // * Nom : Frossard Dominique
    // * Date : date courante
    // *
    // * Il doit retourner :
    // *
    // * P�re4, M�re4 --> Divorc�
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
    // * Le test 0103d est fait pour le requ�rant :
    // *
    // * N� AVS : 368.59.329.114
    // * Nom : Froidevaux Bernard
    // * Date : date courante
    // *
    // * Il doit retourner :
    // *
    // * P�re2, M�re1 --> Divorc�
    // * M�re1, P�re1 --> Divorc�
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
    // * Le test 0103e est fait pour le requ�rant :
    // *
    // * N� AVS : 368.59.329.114
    // * Nom : Froidevaux Bernard
    // * Date : 10.01.2000
    // *
    // * Il doit retourner :
    // *
    // * P�re2, M�re1 --> Divorc�
    // * M�re1, P�re1 --> Mari�
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
    // * Le test 0103f est fait pour le requ�rant :
    // *
    // * N� AVS : 368.59.329.114
    // * Nom : Froidevaux Bernard
    // * Date : 21.08.1997
    // *
    // * Il doit retourner :
    // *
    // * P�re2, M�re1 --> S�par�
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
