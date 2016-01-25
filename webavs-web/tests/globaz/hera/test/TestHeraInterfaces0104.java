/*
 * Créé le 18 août 06
 */
package globaz.hera.test;


/**
 * @author hpe
 * 
 *         Classe permettant de tester les interfaces d'HERA --> ID = 0104
 */
public class TestHeraInterfaces0104 {

    /**
     * 
     * Le test 0104a est fait pour le requérant :
     * 
     * N° AVS : 217.57.338.219 Nom : Brugger Daniel
     * 
     * Il doit retourner :
     * 
     * --> AE --> Travail Suisse
     * 
     */

    // public void test0104a() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // //Retrouver l'idMembre
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "217.57.338.219");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    // String idMembre = "";
    //
    // //Retrouver un membre de la famille ET pour une certaine date
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFMembreFamilleRequerant[] mbrs =
    // sitFam.getMembresFamilleRequerant(idTiers);
    //
    // for (int i = 0; i < mbrs.length; i++) {
    // ISFMembreFamilleRequerant mbr = mbrs[i];
    //
    // if (mbr.getNom().equals("Brugger") && mbr.getPrenom().equals("Daniel")){
    // idMembre = mbr.getIdMembreFamille();
    // }
    // }
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0104a ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    //
    // ISFPeriode[] periodes = sitFam.getPeriodes(idMembre);
    // for (int i = 0; i < periodes.length; i++) {
    // ISFPeriode periode = periodes[i];
    //
    //
    // sb.append(periode.getDateDebut() + " -> " + periode.getDateFin()+ "\n");
    // sb.append(periode.getNoAvs() + " : " + periode.getNoAvsDetenteurBTE()+
    // " = " + periode.getIdDetenteurBTE()+ "\n");
    // sb.append(session.getCodeLibelle(periode.getType())+ " " +
    // session.getCodeLibelle(periode.getPays()) + "\n");
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
    // * Le test 0104b est fait pour le requérant :
    // *
    // * N° AVS : 217.57.338.219
    // * Nom : Brugger Isabelle
    // *
    // * Il doit retourner :
    // *
    // * --> Bénéfice IJ
    // *
    // */
    //
    // public void test0104b() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // //Retrouver l'idMembre
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "217.57.338.219");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    // String idMembre = "";
    //
    // //Retrouver un membre de la famille ET pour une certaine date
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFMembreFamilleRequerant[] mbrs =
    // sitFam.getMembresFamilleRequerant(idTiers);
    //
    // for (int i = 0; i < mbrs.length; i++) {
    // ISFMembreFamilleRequerant mbr = mbrs[i];
    //
    // if (mbr.getNom().equals("Brugger") &&
    // mbr.getPrenom().equals("Isabelle")){
    // idMembre = mbr.getIdMembreFamille();
    // }
    // }
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0104b ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    //
    // ISFPeriode[] periodes = sitFam.getPeriodes(idMembre);
    // for (int i = 0; i < periodes.length; i++) {
    // ISFPeriode periode = periodes[i];
    //
    //
    // sb.append(periode.getDateDebut() + " -> " + periode.getDateFin()+ "\n");
    // sb.append(periode.getNoAvs() + " : " + periode.getNoAvsDetenteurBTE()+
    // " = " + periode.getIdDetenteurBTE()+ "\n");
    // sb.append(session.getCodeLibelle(periode.getType())+ " " +
    // session.getCodeLibelle(periode.getPays()) + "\n");
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
    // * Le test 0104c est fait pour le requérant :
    // *
    // * N° AVS : 217.57.338.219
    // * Nom : Brugger Léon
    // *
    // * Il doit retourner :
    // *
    // * --> Enfant recueilli
    // *
    // */
    //
    // public void test0104c() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // //Retrouver l'idMembre
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "217.57.338.219");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    // String idMembre = "";
    //
    // //Retrouver un membre de la famille ET pour une certaine date
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFMembreFamilleRequerant[] mbrs =
    // sitFam.getMembresFamilleRequerant(idTiers);
    //
    // for (int i = 0; i < mbrs.length; i++) {
    // ISFMembreFamilleRequerant mbr = mbrs[i];
    //
    // if (mbr.getNom().equals("Brugger") && mbr.getPrenom().equals("Léon")){
    // idMembre = mbr.getIdMembreFamille();
    // }
    // }
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0104c ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    //
    // ISFPeriode[] periodes = sitFam.getPeriodes(idMembre);
    // for (int i = 0; i < periodes.length; i++) {
    // ISFPeriode periode = periodes[i];
    //
    //
    // sb.append(periode.getDateDebut() + " -> " + periode.getDateFin()+ "\n");
    // sb.append(periode.getNoAvs() + " : " + periode.getNoAvsDetenteurBTE()+
    // " = " + periode.getIdDetenteurBTE()+ "\n");
    // sb.append(session.getCodeLibelle(periode.getType())+ " " +
    // session.getCodeLibelle(periode.getPays()) + "\n");
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
    // * Le test 0104d est fait pour le requérant :
    // *
    // * N° AVS : 215.01.810.115
    // * Nom : Brugger Zoé
    // *
    // * Il doit retourner :
    // *
    // * --> BTE père1
    // * --> BTE mère1
    // *
    // */
    //
    // public void test0104d() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // //Retrouver l'idMembre
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "215.01.810.115");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    // String idMembre = "";
    //
    // //Retrouver un membre de la famille ET pour une certaine date
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFMembreFamilleRequerant[] mbrs =
    // sitFam.getMembresFamilleRequerant(idTiers);
    //
    // for (int i = 0; i < mbrs.length; i++) {
    // ISFMembreFamilleRequerant mbr = mbrs[i];
    //
    // if (mbr.getNom().equals("Brugger") && mbr.getPrenom().equals("Zoé")){
    // idMembre = mbr.getIdMembreFamille();
    // }
    // }
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0104d ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    //
    // ISFPeriode[] periodes = sitFam.getPeriodes(idMembre);
    // for (int i = 0; i < periodes.length; i++) {
    // ISFPeriode periode = periodes[i];
    //
    //
    // sb.append(periode.getDateDebut() + " -> " + periode.getDateFin()+ "\n");
    // sb.append(periode.getNoAvs() + " : " + periode.getNoAvsDetenteurBTE()+
    // " = " + periode.getIdDetenteurBTE()+ "\n");
    // sb.append(session.getCodeLibelle(periode.getType())+ " " +
    // session.getCodeLibelle(periode.getPays()) + "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    // }
    // System.out.println(sb.toString());
    //
    // }

}
