/*
 * Cr�� le 18 ao�t 06
 */
package globaz.hera.test;


/**
 * @author hpe
 * 
 *         Classe permettant de tester les interfaces d'HERA --> ID = 0105
 */
public class TestHeraInterfaces0105 {

    /**
     * 
     * Le test 0105a est fait pour :
     * 
     * N� AVS : 215.01.810.115 Nom : Brugger Zo�
     * 
     * Il doit retourner :
     * 
     * --> N�AVSP�re = p�re1 --> N�AVSM�re = m�re1 --> isRecueilli = non -->
     * dateAdoption = non
     * 
     */

    // public void test0105a() throws Exception {
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
    // if (mbr.getNom().equals("Brugger") && mbr.getPrenom().equals("Zo�")){
    // idMembre = mbr.getIdMembreFamille();
    // }
    // }
    //
    // ISFEnfant enfant = sitFam.getEnfant(idMembre);
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0105a ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("no avs  enfant = " + enfant.getNss() + "\n");
    // sb.append( "date adoption = " + enfant.getDateAdoption()+ "\n");
    // sb.append( "isRecueilli = " + enfant.isRecueilli()+ "\n");
    // sb.append("p�re = " + enfant.getPrenomPere() + " " +
    // enfant.getNoAvsPere() + "; m�re = " + enfant.getPrenomMere() +" "
    // +enfant.getNoAvsMere()+ "\n");
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    // System.out.println(sb.toString());
    //
    // }
    //
    // /**
    // *
    // * Le test 0105b est fait pour :
    // *
    // * N� AVS : 217.57.338.219
    // * Nom : Brugger L�on
    // *
    // * Il doit retourner :
    // *
    // * --> N�AVSP�re = p�re1
    // * --> N�AVSM�re = m�re2
    // * --> isRecueilli = oui
    // * --> dateAdoption = 21.09.2004
    // *
    // */
    //
    // public void test0105b() throws Exception {
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
    // if (mbr.getNom().equals("Brugger") && mbr.getPrenom().equals("L�on")){
    // idMembre = mbr.getIdMembreFamille();
    // }
    // }
    //
    // ISFEnfant enfant = sitFam.getEnfant(idMembre);
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0105b ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("no avs  enfant = " + enfant.getNss() + "\n");
    // sb.append( "date adoption = " + enfant.getDateAdoption()+ "\n");
    // sb.append( "isRecueilli = " + enfant.isRecueilli()+ "\n");
    // sb.append("p�re = " + enfant.getPrenomPere() + " " +
    // enfant.getNoAvsPere() + "; m�re = " + enfant.getPrenomMere() +" "
    // +enfant.getNoAvsMere()+ "\n");
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    // System.out.println(sb.toString());
    //
    // }
    //
    // /**
    // *
    // * Le test 0105c est fait pour :
    // *
    // * N� AVS : 411.56.723.116
    // * Nom : Grimm Agatte
    // *
    // * Il doit retourner :
    // *
    // * --> N�AVSP�re = inconnu
    // * --> N�AVSM�re = m�re2
    // * --> isRecueilli = non
    // * --> dateAdoption = non
    // *
    // */
    //
    // public void test0105c() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // //Retrouver l'idMembre
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "411.56.723.116");
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
    // if (mbr.getNom().equals("Grimm") && mbr.getPrenom().equals("Agatte")){
    // idMembre = mbr.getIdMembreFamille();
    // }
    // }
    //
    // ISFEnfant enfant = sitFam.getEnfant(idMembre);
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0105c ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("no avs  enfant = " + enfant.getNss() + "\n");
    // sb.append( "date adoption = " + enfant.getDateAdoption()+ "\n");
    // sb.append( "isRecueilli = " + enfant.isRecueilli()+ "\n");
    // sb.append("p�re = " + enfant.getPrenomPere() + " " +
    // enfant.getNoAvsPere() + "; m�re = " + enfant.getPrenomMere() +" "
    // +enfant.getNoAvsMere()+ "\n");
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    // System.out.println(sb.toString());
    //
    // }
    //
    //
    //

}
