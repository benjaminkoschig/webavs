/*
 * Créé le 18 août 06
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
    // * N° AVS : 217.57.338.219
    // * Nom : Brugger Daniel
    // * Date : date courante
    // *
    // * Il doit retourner :
    // *
    // * info du père
    // * état civil = MARIE
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
    // //Test sur l'état civil
    // assertTrue("!! Etat civil incorrect !!",
    // mbr.getCsEtatCivil().equals(ISFSituationFamiliale.CS_ETAT_CIVIL_MARIE));
    //
    // //Test sur le membre
    // assertTrue("Le Membre retourné n'est pas celui recherché !",
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
    // sb.append("nom prénom : " + mbr.getDateNaissance() + " " +
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
    // * N° AVS : 217.57.338.219
    // * Nom : Brugger Daniel
    // * Date : 05.01.2002
    // *
    // * Il doit retourner :
    // *
    // * info du père
    // * état civil = DIVORCE
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
    // //Test sur l'état civil
    // assertTrue("!! Etat civil incorrect !!",
    // mbr.getCsEtatCivil().equals(ISFSituationFamiliale.CS_ETAT_CIVIL_DIVORCE));
    //
    // //Test sur le membre
    // assertTrue("Le Membre retourné n'est pas celui recherché !",
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
    // sb.append("nom prénom : " + mbr.getDateNaissance() + " " +
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
    // * N° AVS : 368.59.329.114
    // * Nom : Froidevaux Bernard
    // * Date : date courante
    // *
    // * Il doit retourner :
    // *
    // * info du père2
    // * état civil = DIVORCE
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
    // //Test sur l'état civil
    // assertTrue("!! Etat civil incorrect !!",
    // mbr.getCsEtatCivil().equals(ISFSituationFamiliale.CS_ETAT_CIVIL_DIVORCE));
    //
    // //Test sur le membre
    // assertTrue("Le Membre retourné n'est pas celui recherché !",
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
    // sb.append("nom prénom : " + mbr.getDateNaissance() + " " +
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
    // * N° AVS : 000.00.000.000
    // * Nom : Brugger Isabelle
    // * Date : date courante
    // *
    // * --> Recherche par le requérant du moment (selon situation familiale)
    // * Etant donné que l'assuré n'a pas de N° AVS
    // *
    // * --> Requérant : Brugger Daniel (Divorcé)
    // *
    // * Il doit retourner :
    // *
    // * info mère1
    // * état civil = DIVORCE
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
    // //Test sur l'état civil
    // assertTrue("!! Etat civil incorrect !!",
    // mbr.getCsEtatCivil().equals(ISFSituationFamiliale.CS_ETAT_CIVIL_DIVORCE));
    //
    // //Test sur le membre
    // assertTrue("Le Membre retourné n'est pas celui recherché !",
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
    // sb.append("nom prénom : " + mbr.getDateNaissance() + " " +
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
    // * N° AVS : 000.00.000.000
    // * Nom : Brugger Isabelle
    // * Date : 05.01.2000
    // *
    // * --> Recherche par le requérant du moment (selon situation familiale)
    // * Etant donné que l'assuré n'a pas de N° AVS
    // *
    // * --> Requérant : Brugger Daniel (Marié)
    // *
    // * Il doit retourner :
    // *
    // * info mère1
    // * état civil = MARIE
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
    // //Test sur l'état civil
    // assertTrue("!! Etat civil incorrect !!",
    // mbr.getCsEtatCivil().equals(ISFSituationFamiliale.CS_ETAT_CIVIL_MARIE));
    //
    // //Test sur le membre
    // assertTrue("Le Membre retourné n'est pas celui recherché !",
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
    // sb.append("nom prénom : " + mbr.getDateNaissance() + " " +
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
    // * N° AVS : 000.00.000.000
    // * Nom : Brugger Isabelle
    // * Date : 10.10.1997
    // *
    // * --> Recherche par le requérant du moment (selon situation familiale)
    // * Etant donné que l'assuré n'a pas de N° AVS
    // *
    // * --> Requérant : Froidevaux Bernard (Séparé de fait)
    // *
    // * Il doit retourner :
    // *
    // * info mère1
    // * état civil = SEPARE DE FAIT
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
    // //Test sur l'état civil
    // assertTrue("!! Etat civil incorrect !!",
    // mbr.getCsEtatCivil().equals(ISFSituationFamiliale.CS_ETAT_CIVIL_SEPARE_DE_FAIT));
    //
    // //Test sur le membre
    // assertTrue("Le Membre retourné n'est pas celui recherché !",
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
    // sb.append("nom prénom : " + mbr.getDateNaissance() + " " +
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
    // * N° AVS : 000.00.000.000
    // * Nom : Bourquin Elliot Junior
    // * Date : date courante
    // *
    // * --> Recherche par le requérant du moment (selon situation familiale)
    // * Etant donné que l'assuré n'a pas de N° AVS
    // *
    // * --> Requérant : Brugger Zoé (mère de l'enfant)
    // *
    // * Il doit retourner :
    // *
    // * info enfant10
    // * état civil = null
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
    // //Test sur l'état civil
    // assertNull("!! Etat civil incorrect !!", mbr.getCsEtatCivil());
    //
    // //Test sur le membre
    // assertTrue("Le Membre retourné n'est pas celui recherché !",
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
    // sb.append("nom prénom : " + mbr.getDateNaissance() + " " +
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
    // * N° AVS : 215.01.810.115
    // * Nom : Brugger Zoé
    // * Date : date courante
    // *
    // *
    // * Il doit retourner :
    // *
    // * info enfant1
    // * état civil = MARIE
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
    // if (mbr.getNom().equals("Brugger") && mbr.getPrenom().equals("Zoé")){
    // idMembre = mbr.getIdMembreFamille();
    // }
    // }
    //
    // ISFMembreFamille mbr = sitFam.getMembreFamille(idMembre);
    //
    // //Test sur l'état civil
    // assertTrue("!! Etat civil incorrect !!",
    // mbr.getCsEtatCivil().equals(ISFSituationFamiliale.CS_ETAT_CIVIL_MARIE));
    //
    // //Test sur le membre
    // assertTrue("Le Membre retourné n'est pas celui recherché !",
    // mbr.getNom().equals("Brugger") && mbr.getPrenom().equals("Zoé"));
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
    // sb.append("nom prénom : " + mbr.getDateNaissance() + " " +
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
    // * N° AVS : 215.01.810.115
    // * Nom : Brugger Zoé
    // * Date : 01.01.1999
    // *
    // *
    // * Il doit retourner :
    // *
    // * info enfant1
    // * état civil = célibataire
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
    // if (mbr.getNom().equals("Brugger") && mbr.getPrenom().equals("Zoé")){
    // idMembre = mbr.getIdMembreFamille();
    // }
    // }
    //
    // ISFMembreFamille mbr = sitFam.getMembreFamille(idMembre, "01.01.1999");
    //
    // //Test sur l'état civil
    // assertTrue("!! Etat civil incorrect !!",
    // mbr.getCsEtatCivil().equals(ISFSituationFamiliale.CS_ETAT_CIVIL_CELIBATAIRE));
    //
    // //Test sur le membre
    // assertTrue("Le Membre retourné n'est pas celui recherché !",
    // mbr.getNom().equals("Brugger") && mbr.getPrenom().equals("Zoé"));
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
    // sb.append("nom prénom : " + mbr.getDateNaissance() + " " +
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
