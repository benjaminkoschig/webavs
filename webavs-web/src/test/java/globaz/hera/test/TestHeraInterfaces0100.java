/*
 * Créé le 17 août 06
 */
package globaz.hera.test;


/**
 * @author hpe
 * 
 *         Classe permettant de tester les interfaces d'HERA --> ID = 0100
 */
public class TestHeraInterfaces0100 {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    // /**
    // *
    // * Le test 0100a est fait pour :
    // *
    // * N° AVS : 217.57.338.219
    // * Nom : Brugger Daniel
    // * Date : date actuelle
    // *
    // * Il doit retourner :
    // *
    // * mère1 : Brugger Isabelle
    // * mère2 : Grimm Ursula
    // * enfant1 : Brugger Zoé
    // * enfant2 : Brugger Léon
    // *
    // */
    //
    // public void test0100a() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // //Retrouver l'idTiers par le n° AVS
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "217.57.338.219");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    //
    // //Retrouver la situation familiale du tiers ET pour une certaine date
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFMembreFamilleRequerant[] mbrs =
    // sitFam.getMembresFamilleRequerant(idTiers);
    //
    //
    // //Test sur le nombre de membre retourné
    // assertTrue("!! Nombre de membre incorrect !!",mbrs.length==5);
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0100a ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    //
    // for (int i = 0; i < mbrs.length; i++) {
    // ISFMembreFamilleRequerant mbr = mbrs[i];
    //
    // // Si mère1 --> confirmation
    // if (mbr.getNom().equals("Brugger") &&
    // mbr.getPrenom().equals("Isabelle")){
    // assertTrue(true);
    //
    // // Si mère2 --> confirmation
    // } else if (mbr.getNom().equals("Grimm") &&
    // mbr.getPrenom().equals("Ursula")){
    // assertTrue(true);
    //
    // // Si enfant1 --> confirmation
    // } else if (mbr.getNom().equals("Brugger") &&
    // mbr.getPrenom().equals("Zoé")){
    // assertTrue(true);
    //
    // // Si enfant2 --> confirmation
    // } else if (mbr.getNom().equals("Brugger") &&
    // mbr.getPrenom().equals("Léon")){
    // assertTrue(true);
    //
    // // Si père1 --> confirmation
    // } else if (mbr.getNom().equals("Brugger") &&
    // mbr.getPrenom().equals("Daniel")){
    // assertTrue(true);
    //
    // } else {
    // assertTrue(false);
    //
    // }
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append(mbr.getNss() + " : " + mbr.getNom() + " " + mbr.getPrenom() +
    // "\n");
    // sb.append("nom prénom : " + mbr.getDateNaissance() + " " +
    // mbr.getDateDeces()+ "\n");
    // sb.append("Relation requerant : " +
    // session.getCodeLibelle(mbr.getRelationAuRequerant())+ "\n");
    // sb.append("Nationalite : " +
    // session.getCodeLibelle(mbr.getCsNationalite()) + " canton " +
    // session.getCodeLibelle(mbr.getCsCantonDomicile())+ "\n");
    // sb.append("sexe : " + session.getCodeLibelle(mbr.getCsSexe())+ "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    //
    // }
    //
    // System.out.println(sb.toString());
    // }
    //
    // /**
    // *
    // * Le test 0100b est fait pour :
    // *
    // * N° AVS : 217.57.338.219
    // * Nom : Brugger Daniel
    // * Date : 05.01.2002
    // *
    // * Il doit retourner :
    // *
    // * mère1 : Brugger Isabelle
    // * enfant1 : Brugger Zoé
    // *
    // */
    //
    // public void test0100b() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // //Retrouver l'idTiers par le n° AVS
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "217.57.338.219");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    //
    // //Retrouver la situation familiale du tiers ET pour une certaine date
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFMembreFamilleRequerant[] mbrs =
    // sitFam.getMembresFamilleRequerant(idTiers, "05.01.2002");
    //
    //
    // //Test sur le nombre de membre retourné
    // assertTrue("!! Nombre de membre incorrect !!",mbrs.length==3);
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0100b ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    //
    // for (int i = 0; i < mbrs.length; i++) {
    // ISFMembreFamilleRequerant mbr = mbrs[i];
    //
    // // Si mère1 --> confirmation
    // if (mbr.getNom().equals("Brugger") &&
    // mbr.getPrenom().equals("Isabelle")){
    // assertTrue(true);
    //
    // // Si enfant1 --> confirmation
    // } else if (mbr.getNom().equals("Brugger") &&
    // mbr.getPrenom().equals("Zoé")){
    // assertTrue(true);
    //
    // // Si père1 --> confirmation
    // } else if (mbr.getNom().equals("Brugger") &&
    // mbr.getPrenom().equals("Daniel")){
    // assertTrue(true);
    //
    // } else {
    // assertTrue(false);
    //
    // }
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append(mbr.getNss() + " : " + mbr.getNom() + " " + mbr.getPrenom() +
    // "\n");
    // sb.append("nom prénom : " + mbr.getDateNaissance() + " " +
    // mbr.getDateDeces()+ "\n");
    // sb.append("Relation requerant : " +
    // session.getCodeLibelle(mbr.getRelationAuRequerant())+ "\n");
    // sb.append("Nationalite : " +
    // session.getCodeLibelle(mbr.getCsNationalite()) + " canton " +
    // session.getCodeLibelle(mbr.getCsCantonDomicile())+ "\n");
    // sb.append("sexe : " + session.getCodeLibelle(mbr.getCsSexe())+ "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    //
    // }
    //
    // System.out.println(sb.toString());
    // }
    //
    // /**
    // *
    // * Le test 0100c est fait pour :
    // *
    // * N° AVS : 368.59.329.114
    // * Nom : Froidevaux Bernard
    // * Date : date actuelle
    // *
    // * Il doit retourner :
    // *
    // * mère1 : Brugger Isabelle
    // *
    // */
    //
    // public void test0100c() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // //Retrouver l'idTiers par le n° AVS
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "368.59.329.114");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    //
    // //Retrouver la situation familiale du tiers ET pour une certaine date
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFMembreFamilleRequerant[] mbrs =
    // sitFam.getMembresFamilleRequerant(idTiers);
    //
    //
    // //Test sur le nombre de membre retourné
    // assertTrue("!! Nombre de membre incorrect !!",mbrs.length==2);
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0100c ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    //
    // for (int i = 0; i < mbrs.length; i++) {
    // ISFMembreFamilleRequerant mbr = mbrs[i];
    //
    // // Si père2 --> confirmation
    // if (mbr.getNom().equals("Froidevaux") &&
    // mbr.getPrenom().equals("Bernard")){
    // assertTrue(true);
    //
    // // Si mère1 --> confirmation
    // } else if (mbr.getNom().equals("Brugger") &&
    // mbr.getPrenom().equals("Isabelle")){
    // assertTrue(true);
    //
    // } else {
    // assertTrue(false);
    //
    // }
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append(mbr.getNss() + " : " + mbr.getNom() + " " + mbr.getPrenom() +
    // "\n");
    // sb.append("nom prénom : " + mbr.getDateNaissance() + " " +
    // mbr.getDateDeces()+ "\n");
    // sb.append("Relation requerant : " +
    // session.getCodeLibelle(mbr.getRelationAuRequerant())+ "\n");
    // sb.append("Nationalite : " +
    // session.getCodeLibelle(mbr.getCsNationalite()) + " canton " +
    // session.getCodeLibelle(mbr.getCsCantonDomicile())+ "\n");
    // sb.append("sexe : " + session.getCodeLibelle(mbr.getCsSexe())+ "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    //
    // }
    //
    // System.out.println(sb.toString());
    // }
    //
    // /**
    // *
    // * Le test 0100d est fait pour :
    // *
    // * N° AVS : 368.59.329.114
    // * Nom : Froidevaux Bernard
    // * Date : 05.08.1998
    // *
    // * Il doit retourner :
    // *
    // * mère1 : Brugger Isabelle
    // *
    // */
    //
    // public void test0100d() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // //Retrouver l'idTiers par le n° AVS
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "368.59.329.114");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    //
    // //Retrouver la situation familiale du tiers ET pour une certaine date
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFMembreFamilleRequerant[] mbrs =
    // sitFam.getMembresFamilleRequerant(idTiers, "05.08.1998");
    //
    //
    // //Test sur le nombre de membre retourné
    // assertTrue("!! Nombre de membre incorrect !!",mbrs.length==2);
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0100d ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    //
    // for (int i = 0; i < mbrs.length; i++) {
    // ISFMembreFamilleRequerant mbr = mbrs[i];
    //
    // // Si mère1 --> confirmation
    // if (mbr.getNom().equals("Brugger") &&
    // mbr.getPrenom().equals("Isabelle")){
    // assertTrue(true);
    //
    // // Si père2 --> confirmation
    // } else if (mbr.getNom().equals("Froidevaux") &&
    // mbr.getPrenom().equals("Bernard")){
    // assertTrue(true);
    //
    // } else {
    // assertTrue(false);
    //
    // }
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append(mbr.getNss() + " : " + mbr.getNom() + " " + mbr.getPrenom() +
    // "\n");
    // sb.append("nom prénom : " + mbr.getDateNaissance() + " " +
    // mbr.getDateDeces()+ "\n");
    // sb.append("Relation requerant : " +
    // session.getCodeLibelle(mbr.getRelationAuRequerant())+ "\n");
    // sb.append("Nationalite : " +
    // session.getCodeLibelle(mbr.getCsNationalite()) + " canton " +
    // session.getCodeLibelle(mbr.getCsCantonDomicile())+ "\n");
    // sb.append("sexe : " + session.getCodeLibelle(mbr.getCsSexe())+ "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    //
    // }
    //
    // System.out.println(sb.toString());
    // }
    //
    // /**
    // *
    // * Le test 0100e est fait pour :
    // *
    // * N° AVS : 411.56.723.116
    // * Nom : Grimm Ursula
    // * Date : date actuelle
    // *
    // * Il doit retourner :
    // *
    // * père1 : Brugger Daniel
    // * enfant2 : Brugger Léon
    // * enfant3 : Grimm Agathe
    // * Conjoint inconnu
    // *
    // */
    //
    // public void test0100e() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // //Retrouver l'idTiers par le n° AVS
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "411.56.723.116");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    //
    // //Retrouver la situation familiale du tiers ET pour une certaine date
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFMembreFamilleRequerant[] mbrs =
    // sitFam.getMembresFamilleRequerant(idTiers);
    //
    //
    // //Test sur le nombre de membre retourné
    // assertTrue("!! Nombre de membre incorrect !!",mbrs.length==5);
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0100e ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    //
    // for (int i = 0; i < mbrs.length; i++) {
    // ISFMembreFamilleRequerant mbr = mbrs[i];
    //
    // // Si père1 --> confirmation
    // if (mbr.getNom().equals("Brugger") && mbr.getPrenom().equals("Daniel")){
    // assertTrue(true);
    //
    // // Si mère2 --> confirmation
    // } else if (mbr.getNom().equals("Grimm") &&
    // mbr.getPrenom().equals("Ursula")){
    // assertTrue(true);
    //
    // // Si enfant2 --> confirmation
    // } else if (mbr.getNom().equals("Brugger") &&
    // mbr.getPrenom().equals("Léon")){
    // assertTrue(true);
    //
    // // Si enfant3 --> confirmation
    // } else if (mbr.getNom().equals("Grimm") &&
    // mbr.getPrenom().equals("Agatte")){
    // assertTrue(true);
    //
    // // Si conjoint inconnu --> confirmation
    // } else if (mbr.getNom().equals("Conjoint") &&
    // mbr.getPrenom().equals("Inconnu")){
    // assertTrue(true);
    //
    // } else {
    // assertTrue(false);
    //
    // }
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append(mbr.getNss() + " : " + mbr.getNom() + " " + mbr.getPrenom() +
    // "\n");
    // sb.append("nom prénom : " + mbr.getDateNaissance() + " " +
    // mbr.getDateDeces()+ "\n");
    // sb.append("Relation requerant : " +
    // session.getCodeLibelle(mbr.getRelationAuRequerant())+ "\n");
    // sb.append("Nationalite : " +
    // session.getCodeLibelle(mbr.getCsNationalite()) + " canton " +
    // session.getCodeLibelle(mbr.getCsCantonDomicile())+ "\n");
    // sb.append("sexe : " + session.getCodeLibelle(mbr.getCsSexe())+ "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    //
    // }
    //
    // System.out.println(sb.toString());
    // }
    //
    // /**
    // *
    // * Le test 0100f est fait pour :
    // *
    // * N° AVS : 368.62.424.144
    // * Nom : Frossard Dominique
    // * Date : date actuelle
    // *
    // * Il doit retourner :
    // *
    // * père4 : Frossard George
    // *
    // */
    //
    // public void test0100f() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // //Retrouver l'idTiers par le n° AVS
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "368.62.424.144");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    //
    // //Retrouver la situation familiale du tiers ET pour une certaine date
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFMembreFamilleRequerant[] mbrs =
    // sitFam.getMembresFamilleRequerant(idTiers);
    //
    //
    // //Test sur le nombre de membre retourné
    // assertTrue("!! Nombre de membre incorrect !!",mbrs.length==2);
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0100f ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    //
    // for (int i = 0; i < mbrs.length; i++) {
    // ISFMembreFamilleRequerant mbr = mbrs[i];
    //
    // // Si mère4 --> confirmation
    // if (mbr.getNom().equals("Frossard") &&
    // mbr.getPrenom().equals("Dominique")){
    // assertTrue(true);
    //
    // // Si père4 --> confirmation
    // } else if (mbr.getNom().equals("Frossard") &&
    // mbr.getPrenom().equals("George")){
    // assertTrue(true);
    //
    // } else {
    // assertTrue(false);
    //
    // }
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append(mbr.getNss() + " : " + mbr.getNom() + " " + mbr.getPrenom() +
    // "\n");
    // sb.append("nom prénom : " + mbr.getDateNaissance() + " " +
    // mbr.getDateDeces()+ "\n");
    // sb.append("Relation requerant : " +
    // session.getCodeLibelle(mbr.getRelationAuRequerant())+ "\n");
    // sb.append("Nationalite : " +
    // session.getCodeLibelle(mbr.getCsNationalite()) + " canton " +
    // session.getCodeLibelle(mbr.getCsCantonDomicile())+ "\n");
    // sb.append("sexe : " + session.getCodeLibelle(mbr.getCsSexe())+ "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    //
    // }
    //
    // System.out.println(sb.toString());
    // }
    //
    // /**
    // *
    // * Le test 0100g est fait pour :
    // *
    // * N° AVS : 215.01.810.115
    // * Nom : Brugger Zoé
    // * Date : date actuelle
    // *
    // * Il doit retourner :
    // *
    // * père10 : Bourquin Elliot
    // * enfant10: Bourquin Elliot Junior
    // *
    // */
    //
    // public void test0100g() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // //Retrouver l'idTiers par le n° AVS
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "215.01.810.115");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    //
    // //Retrouver la situation familiale du tiers ET pour une certaine date
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFMembreFamilleRequerant[] mbrs =
    // sitFam.getMembresFamilleRequerant(idTiers);
    //
    //
    // //Test sur le nombre de membre retourné
    // assertTrue("!! Nombre de membre incorrect !!",mbrs.length==3);
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0100g ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    //
    // for (int i = 0; i < mbrs.length; i++) {
    // ISFMembreFamilleRequerant mbr = mbrs[i];
    //
    // // Si enfant1 --> confirmation
    // if (mbr.getNom().equals("Brugger") && mbr.getPrenom().equals("Zoé")){
    // assertTrue(true);
    //
    // // Si père10 --> confirmation
    // } else if (mbr.getNom().equals("Bourquin") &&
    // mbr.getPrenom().equals("Elliot")){
    // assertTrue(true);
    //
    // // Si enfant10 --> confirmation
    // } else if (mbr.getNom().equals("Bourquin") &&
    // mbr.getPrenom().equals("Elliot Junior")){
    // assertTrue(true);
    //
    // } else {
    // assertTrue(false);
    //
    // }
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append(mbr.getNss() + " : " + mbr.getNom() + " " + mbr.getPrenom() +
    // "\n");
    // sb.append("nom prénom : " + mbr.getDateNaissance() + " " +
    // mbr.getDateDeces()+ "\n");
    // sb.append("Relation requerant : " +
    // session.getCodeLibelle(mbr.getRelationAuRequerant())+ "\n");
    // sb.append("Nationalite : " +
    // session.getCodeLibelle(mbr.getCsNationalite()) + " canton " +
    // session.getCodeLibelle(mbr.getCsCantonDomicile())+ "\n");
    // sb.append("sexe : " + session.getCodeLibelle(mbr.getCsSexe())+ "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    //
    // }
    //
    // System.out.println(sb.toString());
    // }
    //
    // /**
    // *
    // * Le test 0100h est fait pour :
    // *
    // * N° AVS : 215.01.810.115
    // * Nom : Brugger Zoé
    // * Date : 01.01.2000
    // *
    // * Il doit retourner :
    // *
    // * rien (juste le requérant)
    // *
    // */
    //
    // public void test0100h() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // //Retrouver l'idTiers par le n° AVS
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "215.01.810.115");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    //
    // //Retrouver la situation familiale du tiers ET pour une certaine date
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFMembreFamilleRequerant[] mbrs =
    // sitFam.getMembresFamilleRequerant(idTiers, "01.01.2000");
    //
    //
    // //Test sur le nombre de membre retourné
    // assertTrue("!! Nombre de membre incorrect !!",mbrs.length==1);
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0100h ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    //
    // for (int i = 0; i < mbrs.length; i++) {
    // ISFMembreFamilleRequerant mbr = mbrs[i];
    //
    // // Si enfant1 --> confirmation
    // if (mbr.getNom().equals("Brugger") && mbr.getPrenom().equals("Zoé")){
    // assertTrue(true);
    //
    // } else {
    // assertTrue(false);
    //
    // }
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append(mbr.getNss() + " : " + mbr.getNom() + " " + mbr.getPrenom() +
    // "\n");
    // sb.append("nom prénom : " + mbr.getDateNaissance() + " " +
    // mbr.getDateDeces()+ "\n");
    // sb.append("Relation requerant : " +
    // session.getCodeLibelle(mbr.getRelationAuRequerant())+ "\n");
    // sb.append("Nationalite : " +
    // session.getCodeLibelle(mbr.getCsNationalite()) + " canton " +
    // session.getCodeLibelle(mbr.getCsCantonDomicile())+ "\n");
    // sb.append("sexe : " + session.getCodeLibelle(mbr.getCsSexe())+ "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    //
    // }
    //
    // System.out.println(sb.toString());
    // }

}
