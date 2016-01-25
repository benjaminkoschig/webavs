/*
 * Créé le 18 août 06
 */
package globaz.hera.test;


/**
 * @author hpe
 * 
 *         Classe permettant de tester les interfaces d'HERA --> ID = 0102
 */
public class TestHeraInterfaces0102 {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     * Le test 0102a est fait pour le requérant :
     * 
     * N° AVS : 217.57.338.219 Nom : Brugger Daniel Date : date courante
     * 
     * Il doit retourner :
     * 
     * Père1, mère1 --> Divorcé Père1, mère1 --> Marié
     * 
     */

    // public void test0102a() throws Exception {
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
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    //
    // ISFRelationFamiliale[] rfs = null;
    // if (date==null)
    // rfs = sitFam.getRelationsConjoints(idTiers, null);
    // else
    // rfs = sitFam.getRelationsConjoints(idTiers, date);
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0102a ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    //
    //
    // for (int i = 0; i < rfs.length; i++) {
    // ISFRelationFamiliale rf = rfs[i];
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("Liens : " + session.getCodeLibelle(rf.getTypeLien()) + "\n");
    // sb.append(rf.getDateDebut() + " -> " + rf.getDateFin()+ "\n");
    // sb.append(rf.getNomHomme() + " " +rf.getPrenomHomme() + " " +
    // rf.getNoAvsHomme()+ "\n");
    // sb.append(rf.getNomFemme() + " " + rf.getPrenomFemme() + " " +
    // rf.getNoAvsFemme()+ "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    //
    // //
    // // ISFRelationFamiliale[] rfs2 =
    // sitFam.getToutesRelationsConjoints(rf.getIdMembreFamilleFemme(),
    // rf.getIdMembreFamilleHomme(), Boolean.TRUE);
    // // for (int j = 0; j < rfs2.length; j++) {
    // // ISFRelationFamiliale rf2 = rfs2[j];
    // //
    // // sb.append("Liens : " + session.getCodeLibelle(rf2.getTypeLien()) +
    // "\n");
    // // sb.append(rf2.getDateDebut() + " -> " + rf2.getDateFin()+ "\n");
    // // sb.append(rf2.getPrenomHomme() + " " + rf2.getNoAvsHomme()+ "\n");
    // // sb.append(rf2.getPrenomFemme() + " " + rf2.getNoAvsFemme()+ "\n");
    // //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // // sb.append("\n");
    // // }
    //
    // }
    // System.out.println(sb.toString());
    //
    // }
    //
    // /**
    // *
    // * Le test 0102b est fait pour le requérant :
    // *
    // * N° AVS : 217.57.338.219
    // * Nom : Brugger Daniel
    // * Date : 05.10.2000
    // *
    // * Il doit retourner :
    // *
    // * Père1, mère1 --> Marié
    // *
    // */
    //
    // public void test0102b() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // String date = "05.10.2000";
    //
    // //Retrouver l'idTiers
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "217.57.338.219");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    //
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    //
    // ISFRelationFamiliale[] rfs = null;
    // if (date==null)
    // rfs = sitFam.getRelationsConjoints(idTiers, null);
    // else
    // rfs = sitFam.getRelationsConjoints(idTiers, date);
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0102b ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    //
    //
    // for (int i = 0; i < rfs.length; i++) {
    // ISFRelationFamiliale rf = rfs[i];
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("Liens : " + session.getCodeLibelle(rf.getTypeLien()) + "\n");
    // sb.append(rf.getDateDebut() + " -> " + rf.getDateFin()+ "\n");
    // sb.append(rf.getNomHomme() + " " +rf.getPrenomHomme() + " " +
    // rf.getNoAvsHomme()+ "\n");
    // sb.append(rf.getNomFemme() + " " + rf.getPrenomFemme() + " " +
    // rf.getNoAvsFemme()+ "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    //
    // //
    // // ISFRelationFamiliale[] rfs2 =
    // sitFam.getToutesRelationsConjoints(rf.getIdMembreFamilleFemme(),
    // rf.getIdMembreFamilleHomme(), Boolean.TRUE);
    // // for (int j = 0; j < rfs2.length; j++) {
    // // ISFRelationFamiliale rf2 = rfs2[j];
    // //
    // // sb.append("Liens : " + session.getCodeLibelle(rf2.getTypeLien()) +
    // "\n");
    // // sb.append(rf2.getDateDebut() + " -> " + rf2.getDateFin()+ "\n");
    // // sb.append(rf2.getPrenomHomme() + " " + rf2.getNoAvsHomme()+ "\n");
    // // sb.append(rf2.getPrenomFemme() + " " + rf2.getNoAvsFemme()+ "\n");
    // //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // // sb.append("\n");
    // // }
    //
    // }
    // System.out.println(sb.toString());
    //
    // }
    //
    // /**
    // *
    // * Le test 0102c est fait pour le requérant :
    // *
    // * N° AVS : 368.62.424.144
    // * Nom : Frossard Dominique
    // * Date : date courante
    // *
    // * Il doit retourner :
    // *
    // * Père4, mère4 --> Divorcé
    // *
    // */
    //
    // public void test0102c() throws Exception {
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
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    //
    // ISFRelationFamiliale[] rfs = null;
    // if (date==null)
    // rfs = sitFam.getRelationsConjoints(idTiers, null);
    // else
    // rfs = sitFam.getRelationsConjoints(idTiers, date);
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0102c ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    //
    //
    // for (int i = 0; i < rfs.length; i++) {
    // ISFRelationFamiliale rf = rfs[i];
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("Liens : " + session.getCodeLibelle(rf.getTypeLien()) + "\n");
    // sb.append(rf.getDateDebut() + " -> " + rf.getDateFin()+ "\n");
    // sb.append(rf.getNomHomme() + " " +rf.getPrenomHomme() + " " +
    // rf.getNoAvsHomme()+ "\n");
    // sb.append(rf.getNomFemme() + " " + rf.getPrenomFemme() + " " +
    // rf.getNoAvsFemme()+ "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    //
    // //
    // // ISFRelationFamiliale[] rfs2 =
    // sitFam.getToutesRelationsConjoints(rf.getIdMembreFamilleFemme(),
    // rf.getIdMembreFamilleHomme(), Boolean.TRUE);
    // // for (int j = 0; j < rfs2.length; j++) {
    // // ISFRelationFamiliale rf2 = rfs2[j];
    // //
    // // sb.append("Liens : " + session.getCodeLibelle(rf2.getTypeLien()) +
    // "\n");
    // // sb.append(rf2.getDateDebut() + " -> " + rf2.getDateFin()+ "\n");
    // // sb.append(rf2.getPrenomHomme() + " " + rf2.getNoAvsHomme()+ "\n");
    // // sb.append(rf2.getPrenomFemme() + " " + rf2.getNoAvsFemme()+ "\n");
    // //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // // sb.append("\n");
    // // }
    //
    // }
    // System.out.println(sb.toString());
    //
    // }
    //
    // /**
    // *
    // * Le test 0102d est fait pour le requérant :
    // *
    // * N° AVS : 368.59.329.114
    // * Nom : Froidevaux Bernard
    // * Date : date courante
    // *
    // * Il doit retourner :
    // *
    // * Père2, Mère1 --> Divorcé
    // *
    // */
    //
    // public void test0102d() throws Exception {
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
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    //
    // ISFRelationFamiliale[] rfs = null;
    // if (date==null)
    // rfs = sitFam.getRelationsConjoints(idTiers, null);
    // else
    // rfs = sitFam.getRelationsConjoints(idTiers, date);
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0102d ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    //
    //
    // for (int i = 0; i < rfs.length; i++) {
    // ISFRelationFamiliale rf = rfs[i];
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("Liens : " + session.getCodeLibelle(rf.getTypeLien()) + "\n");
    // sb.append(rf.getDateDebut() + " -> " + rf.getDateFin()+ "\n");
    // sb.append(rf.getNomHomme() + " " +rf.getPrenomHomme() + " " +
    // rf.getNoAvsHomme()+ "\n");
    // sb.append(rf.getNomFemme() + " " + rf.getPrenomFemme() + " " +
    // rf.getNoAvsFemme()+ "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    //
    // //
    // // ISFRelationFamiliale[] rfs2 =
    // sitFam.getToutesRelationsConjoints(rf.getIdMembreFamilleFemme(),
    // rf.getIdMembreFamilleHomme(), Boolean.TRUE);
    // // for (int j = 0; j < rfs2.length; j++) {
    // // ISFRelationFamiliale rf2 = rfs2[j];
    // //
    // // sb.append("Liens : " + session.getCodeLibelle(rf2.getTypeLien()) +
    // "\n");
    // // sb.append(rf2.getDateDebut() + " -> " + rf2.getDateFin()+ "\n");
    // // sb.append(rf2.getPrenomHomme() + " " + rf2.getNoAvsHomme()+ "\n");
    // // sb.append(rf2.getPrenomFemme() + " " + rf2.getNoAvsFemme()+ "\n");
    // //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // // sb.append("\n");
    // // }
    //
    // }
    // System.out.println(sb.toString());
    //
    // }
    //
    // /**
    // *
    // * Le test 0102e est fait pour le requérant :
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
    // public void test0102e() throws Exception {
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
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    //
    // ISFRelationFamiliale[] rfs = null;
    // if (date==null)
    // rfs = sitFam.getRelationsConjoints(idTiers, null);
    // else
    // rfs = sitFam.getRelationsConjoints(idTiers, date);
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0102e ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    //
    //
    // for (int i = 0; i < rfs.length; i++) {
    // ISFRelationFamiliale rf = rfs[i];
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("Liens : " + session.getCodeLibelle(rf.getTypeLien()) + "\n");
    // sb.append(rf.getDateDebut() + " -> " + rf.getDateFin()+ "\n");
    // sb.append(rf.getNomHomme() + " " +rf.getPrenomHomme() + " " +
    // rf.getNoAvsHomme()+ "\n");
    // sb.append(rf.getNomFemme() + " " + rf.getPrenomFemme() + " " +
    // rf.getNoAvsFemme()+ "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    //
    // //
    // // ISFRelationFamiliale[] rfs2 =
    // sitFam.getToutesRelationsConjoints(rf.getIdMembreFamilleFemme(),
    // rf.getIdMembreFamilleHomme(), Boolean.TRUE);
    // // for (int j = 0; j < rfs2.length; j++) {
    // // ISFRelationFamiliale rf2 = rfs2[j];
    // //
    // // sb.append("Liens : " + session.getCodeLibelle(rf2.getTypeLien()) +
    // "\n");
    // // sb.append(rf2.getDateDebut() + " -> " + rf2.getDateFin()+ "\n");
    // // sb.append(rf2.getPrenomHomme() + " " + rf2.getNoAvsHomme()+ "\n");
    // // sb.append(rf2.getPrenomFemme() + " " + rf2.getNoAvsFemme()+ "\n");
    // //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // // sb.append("\n");
    // // }
    //
    // }
    // System.out.println(sb.toString());
    //
    // }

}
