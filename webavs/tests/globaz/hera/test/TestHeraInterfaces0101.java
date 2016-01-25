/*
 * Cr�� le 18 ao�t 06
 */
package globaz.hera.test;


/**
 * @author hpe
 * 
 *         Classe permettant de tester les interfaces d'HERA --> ID = 0101
 * 
 */
public class TestHeraInterfaces0101 {

    // // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    //
    // /**
    // *
    // * Le test 0101a est fait pour :
    // *
    // * N� AVS : 217.57.338.219
    // * Nom : Brugger Daniel
    // * Date : date courante
    // *
    // * Il doit retourner :
    // *
    // * info du p�re
    // * �tat civil = MARIE
    // *
    // */
    //
    // public void test0101a() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // //Retrouver l'idMembre
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "217.57.338.219");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    // String idMembre = "";
    //
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
    //
    // ISFMembreFamille mbr = sitFam.getMembreFamille(idMembre);
    //
    // //Test sur l'�tat civil
    // assertTrue("!! Etat civil incorrect !!",
    // mbr.getCsEtatCivil().equals(ISFSituationFamiliale.CS_ETAT_CIVIL_MARIE));
    //
    // //Test sur le membre
    // assertTrue("Le Membre retourn� n'est pas celui recherch� !",
    // mbr.getNom().equals("Brugger") && mbr.getPrenom().equals("Daniel"));
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0101a ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append(mbr.getNss() + " : " + mbr.getNom() + " " + mbr.getPrenom() +
    // "\n");
    // sb.append("nom pr�nom : " + mbr.getDateNaissance() + " " +
    // mbr.getDateDeces()+ "\n");
    // sb.append("Nationalite : " +
    // session.getCodeLibelle(mbr.getCsNationalite()) + " canton " +
    // session.getCodeLibelle(mbr.getCsCantonDomicile())+ "\n");
    // sb.append("sexe : " + session.getCodeLibelle(mbr.getCsSexe())+ "\n");
    // sb.append("etat civil = " + session.getCodeLibelle(mbr.getCsEtatCivil())+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    //
    //
    // System.out.println(sb.toString());
    // }
    //
    // /**
    // *
    // * Le test 0101b est fait pour :
    // *
    // * N� AVS : 217.57.338.219
    // * Nom : Brugger Daniel
    // * Date : 05.01.2002
    // *
    // * Il doit retourner :
    // *
    // * info du p�re
    // * �tat civil = DIVORCE
    // *
    // */
    //
    // public void test0101b() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // //Retrouver l'idMembre
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "217.57.338.219");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    // String idMembre = "";
    //
    //
    // //Retrouver un membre de la famille ET pour une certaine date
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFMembreFamilleRequerant[] mbrs =
    // sitFam.getMembresFamilleRequerant(idTiers, "05.01.2002");
    //
    // for (int i = 0; i < mbrs.length; i++) {
    // ISFMembreFamilleRequerant mbr = mbrs[i];
    //
    // if (mbr.getNom().equals("Brugger") && mbr.getPrenom().equals("Daniel")){
    // idMembre = mbr.getIdMembreFamille();
    // }
    // }
    //
    // ISFMembreFamille mbr = sitFam.getMembreFamille(idMembre, "05.01.2002");
    //
    // //Test sur l'�tat civil
    // assertTrue("!! Etat civil incorrect !!",
    // mbr.getCsEtatCivil().equals(ISFSituationFamiliale.CS_ETAT_CIVIL_DIVORCE));
    //
    // //Test sur le membre
    // assertTrue("Le Membre retourn� n'est pas celui recherch� !",
    // mbr.getNom().equals("Brugger") && mbr.getPrenom().equals("Daniel"));
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0101b ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append(mbr.getNss() + " : " + mbr.getNom() + " " + mbr.getPrenom() +
    // "\n");
    // sb.append("nom pr�nom : " + mbr.getDateNaissance() + " " +
    // mbr.getDateDeces()+ "\n");
    // sb.append("Nationalite : " +
    // session.getCodeLibelle(mbr.getCsNationalite()) + " canton " +
    // session.getCodeLibelle(mbr.getCsCantonDomicile())+ "\n");
    // sb.append("sexe : " + session.getCodeLibelle(mbr.getCsSexe())+ "\n");
    // sb.append("etat civil = " + session.getCodeLibelle(mbr.getCsEtatCivil())+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    //
    //
    // System.out.println(sb.toString());
    // }
    //
    // /**
    // *
    // * Le test 0101c est fait pour :
    // *
    // * N� AVS : 368.59.329.114
    // * Nom : Froidevaux Bernard
    // * Date : date courante
    // *
    // * Il doit retourner :
    // *
    // * info du p�re2
    // * �tat civil = DIVORCE
    // *
    // */
    //
    // public void test0101c() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // //Retrouver l'idMembre
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "368.59.329.114");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    // String idMembre = "";
    //
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
    // if (mbr.getNom().equals("Froidevaux") &&
    // mbr.getPrenom().equals("Bernard")){
    // idMembre = mbr.getIdMembreFamille();
    // }
    // }
    //
    // ISFMembreFamille mbr = sitFam.getMembreFamille(idMembre);
    //
    // //Test sur l'�tat civil
    // assertTrue("!! Etat civil incorrect !!",
    // mbr.getCsEtatCivil().equals(ISFSituationFamiliale.CS_ETAT_CIVIL_DIVORCE));
    //
    // //Test sur le membre
    // assertTrue("Le Membre retourn� n'est pas celui recherch� !",
    // mbr.getNom().equals("Froidevaux") && mbr.getPrenom().equals("Bernard"));
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0101c ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append(mbr.getNss() + " : " + mbr.getNom() + " " + mbr.getPrenom() +
    // "\n");
    // sb.append("nom pr�nom : " + mbr.getDateNaissance() + " " +
    // mbr.getDateDeces()+ "\n");
    // sb.append("Nationalite : " +
    // session.getCodeLibelle(mbr.getCsNationalite()) + " canton " +
    // session.getCodeLibelle(mbr.getCsCantonDomicile())+ "\n");
    // sb.append("sexe : " + session.getCodeLibelle(mbr.getCsSexe())+ "\n");
    // sb.append("etat civil = " + session.getCodeLibelle(mbr.getCsEtatCivil())+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    //
    //
    // System.out.println(sb.toString());
    // }
    //
    // /**
    // *
    // * Le test 0101d est fait pour :
    // *
    // * N� AVS : 000.00.000.000
    // * Nom : Brugger Isabelle
    // * Date : date courante
    // *
    // * --> Recherche par le requ�rant du moment (selon situation familiale)
    // * Etant donn� que l'assur� n'a pas de N� AVS
    // *
    // * --> Requ�rant : Brugger Daniel (Divorc�)
    // *
    // * Il doit retourner :
    // *
    // * info m�re1
    // * �tat civil = DIVORCE
    // *
    // */
    //
    // public void test0101d() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // //Retrouver l'idMembre
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "217.57.338.219");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    // String idMembre = "";
    //
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
    //
    // ISFMembreFamille mbr = sitFam.getMembreFamille(idMembre);
    //
    // //Test sur l'�tat civil
    // assertTrue("!! Etat civil incorrect !!",
    // mbr.getCsEtatCivil().equals(ISFSituationFamiliale.CS_ETAT_CIVIL_DIVORCE));
    //
    // //Test sur le membre
    // assertTrue("Le Membre retourn� n'est pas celui recherch� !",
    // mbr.getNom().equals("Brugger") && mbr.getPrenom().equals("Isabelle"));
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0101d ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append(mbr.getNss() + " : " + mbr.getNom() + " " + mbr.getPrenom() +
    // "\n");
    // sb.append("nom pr�nom : " + mbr.getDateNaissance() + " " +
    // mbr.getDateDeces()+ "\n");
    // sb.append("Nationalite : " +
    // session.getCodeLibelle(mbr.getCsNationalite()) + " canton " +
    // session.getCodeLibelle(mbr.getCsCantonDomicile())+ "\n");
    // sb.append("sexe : " + session.getCodeLibelle(mbr.getCsSexe())+ "\n");
    // sb.append("etat civil = " + session.getCodeLibelle(mbr.getCsEtatCivil())+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    //
    //
    // System.out.println(sb.toString());
    // }
    //
    // /**
    // *
    // * Le test 0101e est fait pour :
    // *
    // * N� AVS : 000.00.000.000
    // * Nom : Brugger Isabelle
    // * Date : 05.01.2000
    // *
    // * --> Recherche par le requ�rant du moment (selon situation familiale)
    // * Etant donn� que l'assur� n'a pas de N� AVS
    // *
    // * --> Requ�rant : Brugger Daniel (Mari�)
    // *
    // * Il doit retourner :
    // *
    // * info m�re1
    // * �tat civil = MARIE
    // *
    // */
    //
    // public void test0101e() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // //Retrouver l'idMembre
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "217.57.338.219");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    // String idMembre = "";
    //
    //
    // //Retrouver un membre de la famille ET pour une certaine date
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFMembreFamilleRequerant[] mbrs =
    // sitFam.getMembresFamilleRequerant(idTiers, "05.01.2000");
    //
    // for (int i = 0; i < mbrs.length; i++) {
    // ISFMembreFamilleRequerant mbr = mbrs[i];
    //
    // if (mbr.getNom().equals("Brugger") &&
    // mbr.getPrenom().equals("Isabelle")){
    // idMembre = mbr.getIdMembreFamille();
    // }
    // }
    //
    // ISFMembreFamille mbr = sitFam.getMembreFamille(idMembre, "05.01.2000");
    //
    // //Test sur l'�tat civil
    // assertTrue("!! Etat civil incorrect !!",
    // mbr.getCsEtatCivil().equals(ISFSituationFamiliale.CS_ETAT_CIVIL_MARIE));
    //
    // //Test sur le membre
    // assertTrue("Le Membre retourn� n'est pas celui recherch� !",
    // mbr.getNom().equals("Brugger") && mbr.getPrenom().equals("Isabelle"));
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0101e ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append(mbr.getNss() + " : " + mbr.getNom() + " " + mbr.getPrenom() +
    // "\n");
    // sb.append("nom pr�nom : " + mbr.getDateNaissance() + " " +
    // mbr.getDateDeces()+ "\n");
    // sb.append("Nationalite : " +
    // session.getCodeLibelle(mbr.getCsNationalite()) + " canton " +
    // session.getCodeLibelle(mbr.getCsCantonDomicile())+ "\n");
    // sb.append("sexe : " + session.getCodeLibelle(mbr.getCsSexe())+ "\n");
    // sb.append("etat civil = " + session.getCodeLibelle(mbr.getCsEtatCivil())+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    //
    //
    // System.out.println(sb.toString());
    // }
    //
    // /**
    // *
    // * Le test 0101f est fait pour :
    // *
    // * N� AVS : 000.00.000.000
    // * Nom : Brugger Isabelle
    // * Date : 10.10.1997
    // *
    // * --> Recherche par le requ�rant du moment (selon situation familiale)
    // * Etant donn� que l'assur� n'a pas de N� AVS
    // *
    // * --> Requ�rant : Froidevaux Bernard (S�par� de fait)
    // *
    // * Il doit retourner :
    // *
    // * info m�re1
    // * �tat civil = SEPARE DE FAIT
    // *
    // */
    //
    // public void test0101f() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // //Retrouver l'idMembre
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "368.59.329.114");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    // String idMembre = "";
    //
    //
    // //Retrouver un membre de la famille ET pour une certaine date
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFMembreFamilleRequerant[] mbrs =
    // sitFam.getMembresFamilleRequerant(idTiers, "10.10.1997");
    //
    // for (int i = 0; i < mbrs.length; i++) {
    // ISFMembreFamilleRequerant mbr = mbrs[i];
    //
    // if (mbr.getNom().equals("Brugger") &&
    // mbr.getPrenom().equals("Isabelle")){
    // idMembre = mbr.getIdMembreFamille();
    // }
    // }
    //
    // ISFMembreFamille mbr = sitFam.getMembreFamille(idMembre, "10.10.1997");
    //
    // //Test sur l'�tat civil
    // assertTrue("!! Etat civil incorrect !!",
    // mbr.getCsEtatCivil().equals(ISFSituationFamiliale.CS_ETAT_CIVIL_SEPARE_DE_FAIT));
    //
    // //Test sur le membre
    // assertTrue("Le Membre retourn� n'est pas celui recherch� !",
    // mbr.getNom().equals("Brugger") && mbr.getPrenom().equals("Isabelle"));
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0101f ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append(mbr.getNss() + " : " + mbr.getNom() + " " + mbr.getPrenom() +
    // "\n");
    // sb.append("nom pr�nom : " + mbr.getDateNaissance() + " " +
    // mbr.getDateDeces()+ "\n");
    // sb.append("Nationalite : " +
    // session.getCodeLibelle(mbr.getCsNationalite()) + " canton " +
    // session.getCodeLibelle(mbr.getCsCantonDomicile())+ "\n");
    // sb.append("sexe : " + session.getCodeLibelle(mbr.getCsSexe())+ "\n");
    // sb.append("etat civil = " + session.getCodeLibelle(mbr.getCsEtatCivil())+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    //
    //
    // System.out.println(sb.toString());
    // }
    //
    // /**
    // *
    // * Le test 0101g est fait pour :
    // *
    // * N� AVS : 000.00.000.000
    // * Nom : Bourquin Elliot Junior
    // * Date : date courante
    // *
    // * --> Recherche par le requ�rant du moment (selon situation familiale)
    // * Etant donn� que l'assur� n'a pas de N� AVS
    // *
    // * --> Requ�rant : Brugger Zo� (m�re de l'enfant)
    // *
    // * Il doit retourner :
    // *
    // * info enfant10
    // * �tat civil = null
    // *
    // */
    //
    // public void test0101g() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // //Retrouver l'idMembre
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "215.01.810.115");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    // String idMembre = "";
    //
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
    // if (mbr.getNom().equals("Bourquin") &&
    // mbr.getPrenom().equals("Elliot Junior")){
    // idMembre = mbr.getIdMembreFamille();
    // }
    // }
    //
    // ISFMembreFamille mbr = sitFam.getMembreFamille(idMembre);
    //
    // //Test sur l'�tat civil
    // assertNull("!! Etat civil incorrect !!", mbr.getCsEtatCivil());
    //
    // //Test sur le membre
    // assertTrue("Le Membre retourn� n'est pas celui recherch� !",
    // mbr.getNom().equals("Bourquin") &&
    // mbr.getPrenom().equals("Elliot Junior"));
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0101g ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append(mbr.getNss() + " : " + mbr.getNom() + " " + mbr.getPrenom() +
    // "\n");
    // sb.append("nom pr�nom : " + mbr.getDateNaissance() + " " +
    // mbr.getDateDeces()+ "\n");
    // sb.append("Nationalite : " +
    // session.getCodeLibelle(mbr.getCsNationalite()) + " canton " +
    // session.getCodeLibelle(mbr.getCsCantonDomicile())+ "\n");
    // sb.append("sexe : " + session.getCodeLibelle(mbr.getCsSexe())+ "\n");
    // sb.append("etat civil = " + session.getCodeLibelle(mbr.getCsEtatCivil())+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    //
    //
    // System.out.println(sb.toString());
    // }
    //
    // /**
    // *
    // * Le test 0101h est fait pour :
    // *
    // * N� AVS : 215.01.810.115
    // * Nom : Brugger Zo�
    // * Date : date courante
    // *
    // *
    // * Il doit retourner :
    // *
    // * info enfant1
    // * �tat civil = MARIE
    // *
    // */
    //
    // public void test0101h() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // //Retrouver l'idMembre
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "215.01.810.115");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    // String idMembre = "";
    //
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
    // ISFMembreFamille mbr = sitFam.getMembreFamille(idMembre);
    //
    // //Test sur l'�tat civil
    // assertTrue("!! Etat civil incorrect !!",
    // mbr.getCsEtatCivil().equals(ISFSituationFamiliale.CS_ETAT_CIVIL_MARIE));
    //
    // //Test sur le membre
    // assertTrue("Le Membre retourn� n'est pas celui recherch� !",
    // mbr.getNom().equals("Brugger") && mbr.getPrenom().equals("Zo�"));
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0101h ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append(mbr.getNss() + " : " + mbr.getNom() + " " + mbr.getPrenom() +
    // "\n");
    // sb.append("nom pr�nom : " + mbr.getDateNaissance() + " " +
    // mbr.getDateDeces()+ "\n");
    // sb.append("Nationalite : " +
    // session.getCodeLibelle(mbr.getCsNationalite()) + " canton " +
    // session.getCodeLibelle(mbr.getCsCantonDomicile())+ "\n");
    // sb.append("sexe : " + session.getCodeLibelle(mbr.getCsSexe())+ "\n");
    // sb.append("etat civil = " + session.getCodeLibelle(mbr.getCsEtatCivil())+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    //
    //
    // System.out.println(sb.toString());
    // }
    //
    // /**
    // *
    // * Le test 0101i est fait pour :
    // *
    // * N� AVS : 215.01.810.115
    // * Nom : Brugger Zo�
    // * Date : 01.01.1999
    // *
    // *
    // * Il doit retourner :
    // *
    // * info enfant1
    // * �tat civil = c�libataire
    // *
    // */
    //
    // public void test0101i() throws Exception {
    //
    // StringBuffer sb = new StringBuffer();
    // BSession session = TestHera.createSession();
    //
    // //Retrouver l'idMembre
    // SFTiersWrapper tiers = SFTiersHelper.getTiers(session, "215.01.810.115");
    // String idTiers = tiers.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS);
    // String idMembre = "";
    //
    //
    // //Retrouver un membre de la famille ET pour une certaine date
    // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFMembreFamilleRequerant[] mbrs =
    // sitFam.getMembresFamilleRequerant(idTiers, "01.01.1999");
    //
    // for (int i = 0; i < mbrs.length; i++) {
    // ISFMembreFamilleRequerant mbr = mbrs[i];
    //
    // if (mbr.getNom().equals("Brugger") && mbr.getPrenom().equals("Zo�")){
    // idMembre = mbr.getIdMembreFamille();
    // }
    // }
    //
    // ISFMembreFamille mbr = sitFam.getMembreFamille(idMembre, "01.01.1999");
    //
    // //Test sur l'�tat civil
    // assertTrue("!! Etat civil incorrect !!",
    // mbr.getCsEtatCivil().equals(ISFSituationFamiliale.CS_ETAT_CIVIL_CELIBATAIRE));
    //
    // //Test sur le membre
    // assertTrue("Le Membre retourn� n'est pas celui recherch� !",
    // mbr.getNom().equals("Brugger") && mbr.getPrenom().equals("Zo�"));
    //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("------------------------------------ TEST 0101i ---------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append(mbr.getNss() + " : " + mbr.getNom() + " " + mbr.getPrenom() +
    // "\n");
    // sb.append("nom pr�nom : " + mbr.getDateNaissance() + " " +
    // mbr.getDateDeces()+ "\n");
    // sb.append("Nationalite : " +
    // session.getCodeLibelle(mbr.getCsNationalite()) + " canton " +
    // session.getCodeLibelle(mbr.getCsCantonDomicile())+ "\n");
    // sb.append("sexe : " + session.getCodeLibelle(mbr.getCsSexe())+ "\n");
    // sb.append("etat civil = " + session.getCodeLibelle(mbr.getCsEtatCivil())+
    // "\n");
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // sb.append("\n");
    //
    //
    // System.out.println(sb.toString());
    // }

}
