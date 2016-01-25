/*
 * ado Créé le 28 sept. 05
 */
package globaz.hera.api.test;

import globaz.globall.api.BIApplication;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;

/**
 * @author ado
 * 
 *         28 sept. 05
 */
public class APITest {

    public static void main(String[] args) {
        try {
            new APITest().go();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(-1);
    }

    /**
	 * 
	 */
    private void go() throws Exception {
        BIApplication application = GlobazSystem.getApplication("HERA");
        // application = GlobazSystem.getApplication("HERA");
        BSession session = (BSession) application.newSession("globazf", "ssiiadm");
        // testGetMembreFamilleRequerant(session);
        // testGetMembreFamille(session);
        // testgetPeriodess(session);
        // testGetDetailEnfant(session);
        // testgetRelationsFamiliales(session);
        // testgetRelationsFamilialesEtendues(session);
        // test_5_1(session);
        // test_5_2(session);
        // test_5_3(session);
        // test_5_4(session);
        // test_5_5(session);
        // test_5_6(session);
        // testRequerant(session);
        // testPeriode(session);
        // testPeriodeRequerant(session);
        // testCI(session);
    }

    /**
     * @param session
     *            Pour ce test se référer au document de test au point 5.1 X:\Clients
     *            \GLOBAZ\Developpement\Produits\WebPrestations\Tests\pt -WebPrestations-HERA.doc
     */
    // private void test_5_1(BSession session) throws Exception {
    // ISFSituationFamiliale sf =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // StringBuffer resultStr = new StringBuffer();
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Get membres famille du père 1 (Schwaller Patrick) à la date actuelle: \n");
    // resultStr.append("**************************** \n");
    // ISFMembreFamilleRequerant[] membre =
    // sf.getMembresFamilleRequerant("217717");
    // for (int i = 0; i < membre.length; i++) {
    // ISFMembreFamilleRequerant element = membre[i];
    // resultStr.append( print(session, element) + "\n");
    // }
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Get membres famille du père 1 (Schwaller Patrick) au 5.1.2002: \n");
    // resultStr.append("**************************** \n");
    // membre = sf.getMembresFamilleRequerant("217717", "05.01.2002");
    // for (int i = 0; i < membre.length; i++) {
    // ISFMembreFamilleRequerant element = membre[i];
    // resultStr.append( print(session, element) + "\n");
    // }
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Get membres famille du père 2 (Marc Steger) date actuelle: \n");
    // resultStr.append("**************************** \n");
    // membre = sf.getMembresFamilleRequerant("257721");
    // for (int i = 0; i < membre.length; i++) {
    // ISFMembreFamilleRequerant element = membre[i];
    // resultStr.append( print(session, element) + "\n");
    // }
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Get membres famille du père 2 (Marc Steger) au 05.08.1998: \n");
    // resultStr.append("**************************** \n");
    // membre = sf.getMembresFamilleRequerant("257721", "05.08.1998");
    // for (int i = 0; i < membre.length; i++) {
    // ISFMembreFamilleRequerant element = membre[i];
    // resultStr.append( print(session, element) + "\n");
    // }
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Get membres famille de la mère 2 (Hindricksen Christina)  date actuelle: \n");
    // resultStr.append("**************************** \n");
    // membre = sf.getMembresFamilleRequerant("257716");
    // for (int i = 0; i < membre.length; i++) {
    // ISFMembreFamilleRequerant element = membre[i];
    // resultStr.append( print(session, element) + "\n");
    // }
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Get membres famille de la mère 4 (Schweingruber Liliane Astrid) date actuelle: \n");
    // resultStr.append("**************************** \n");
    // membre = sf.getMembresFamilleRequerant("257717");
    // for (int i = 0; i < membre.length; i++) {
    // ISFMembreFamilleRequerant element = membre[i];
    // resultStr.append( print(session, element) + "\n");
    // }
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Get membres famille de Enfant 1 (Schmed Maria) date actuelle: \n");
    // resultStr.append("**************************** \n");
    // membre = sf.getMembresFamilleRequerant("146987");
    // for (int i = 0; i < membre.length; i++) {
    // ISFMembreFamilleRequerant element = membre[i];
    // resultStr.append( print(session, element) + "\n");
    // }
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Get membres famille de Enfant 1 (Schmed Maria) au 01.01.2000:\n");
    // resultStr.append("**************************** \n");
    // membre = sf.getMembresFamilleRequerant("146987","01.01.2000");
    // for (int i = 0; i < membre.length; i++) {
    // ISFMembreFamilleRequerant element = membre[i];
    // resultStr.append( print(session, element) + "\n");
    // }
    // System.out.println(resultStr.toString());
    // }
    //
    // /**
    // * @param session
    // * Pour ce test se référer au document de test au point 5.2
    // *
    // X:\Clients\GLOBAZ\Developpement\Produits\WebPrestations\Tests\pt-WebPrestations-HERA.doc
    // */
    // private void test_5_2(BSession session) throws Exception {
    // ISFSituationFamiliale sf =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // StringBuffer resultStr = new StringBuffer();
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Get detail du père 1 (Schwaller Patrick) à la date actuelle: \n");
    // resultStr.append("**************************** \n");
    // ISFMembreFamille membre = sf.getMembreFamille("384");
    // resultStr.append( print(session, membre) + "\n");
    //
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Get detail du père 1 (Schwaller Patrick) au 5.1.2002: \n");
    // resultStr.append("**************************** \n");
    // membre = sf.getMembreFamille("384", "05.01.2002");
    // resultStr.append( print(session, membre) + "\n");
    //
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Get detail du père 2 (Marc Steger) date actuelle: \n");
    // resultStr.append("**************************** \n");
    // membre = sf.getMembreFamille("399");
    // resultStr.append( print(session, membre) + "\n");
    //
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Get detail de la mère 1 (Haettenschweiler Monica) date actuelle: \n");
    // resultStr.append("**************************** \n");
    // membre = sf.getMembreFamille("385");
    // resultStr.append( print(session, membre) + "\n");
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Get detail de la mère 1 (Haettenschweiler Monica) au 05.01.2000: \n");
    // resultStr.append("**************************** \n");
    // membre = sf.getMembreFamille("385","05.01.2000");
    // resultStr.append( print(session, membre) + "\n");
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Get detail de la mère 1 (Haettenschweiler Monica) au 10.10.1997: \n");
    // resultStr.append("**************************** \n");
    // membre = sf.getMembreFamille("385","10.10.1997");
    // resultStr.append( print(session, membre) + "\n");
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Get detail de Enfant 1 (Schmed Maria) date courante: \n");
    // resultStr.append("**************************** \n");
    // membre = sf.getMembreFamille("388");
    // resultStr.append( print(session, membre) + "\n");
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Get detail de Enfant 10 (Moser Sven) date courante: \n");
    // resultStr.append("**************************** \n");
    // membre = sf.getMembreFamille("423");
    // resultStr.append( print(session, membre) + "\n");
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Get detail de Enfant 10 (Moser Sven)  au 01.01.1999: \n");
    // resultStr.append("**************************** \n");
    // membre = sf.getMembreFamille("423");
    // resultStr.append( print(session, membre) + "\n");
    //
    // System.out.println(resultStr.toString());
    // }
    //
    // /**
    // * @param session
    // * Pour ce test se référer au document de test au point 5.3: relations
    // familliale
    // *
    // X:\Clients\GLOBAZ\Developpement\Produits\WebPrestations\Tests\pt-WebPrestations-HERA.doc
    // */
    // private void test_5_3(BSession session) throws Exception {
    // ISFSituationFamiliale sf =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // StringBuffer resultStr = new StringBuffer();
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Relations du père 1 (Schwaller Patrick) à la date actuelle: \n");
    // resultStr.append("**************************** \n");
    // ISFRelationFamiliale[] membre = sf.getRelationsConjoints("217717", null);
    // for (int i = 0; i < membre.length; i++) {
    // ISFRelationFamiliale element = membre[i];
    // resultStr.append( print(session, element) + "\n");
    // }
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Relations du père 1 (Schwaller Patrick) au 05.01.2000: \n");
    // resultStr.append("**************************** \n");
    // membre = sf.getRelationsConjoints("217717", "05.01.2000");
    // for (int i = 0; i < membre.length; i++) {
    // ISFRelationFamiliale element = membre[i];
    // resultStr.append( print(session, element) + "\n");
    // }
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Relations de la mère 4 (Schweingruber Liliane Astrid) date courante: \n");
    // resultStr.append("**************************** \n");
    // membre = sf.getRelationsConjoints("257717", null);
    // for (int i = 0; i < membre.length; i++) {
    // ISFRelationFamiliale element = membre[i];
    // resultStr.append( print(session, element) + "\n");
    // }
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Relations du père 2 (Marc Steger) date courante: \n");
    // resultStr.append("**************************** \n");
    // membre = sf.getRelationsConjoints("257721", null);
    // for (int i = 0; i < membre.length; i++) {
    // ISFRelationFamiliale element = membre[i];
    // resultStr.append( print(session, element) + "\n");
    // }
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Relations du père 2 (Marc Steger) au 21.08.1997: \n");
    // resultStr.append("**************************** \n");
    // membre = sf.getRelationsConjoints("257721", "21.08.1997");
    // for (int i = 0; i < membre.length; i++) {
    // ISFRelationFamiliale element = membre[i];
    // resultStr.append( print(session, element) + "\n");
    // }
    //
    // System.out.println(resultStr.toString());
    // }
    //
    // /**
    // * @param session
    // * Pour ce test se référer au document de test au point 5.4: relations
    // familliale étendue
    // *
    // X:\Clients\GLOBAZ\Developpement\Produits\WebPrestations\Tests\pt-WebPrestations-HERA.doc
    // */
    // private void test_5_4(BSession session) throws Exception {
    // StringBuffer resultStr = new StringBuffer();
    // ISFSituationFamiliale sf =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Relations étendue du père 1 (Schwaller Patrick) à la date actuelle: \n");
    // resultStr.append("**************************** \n");
    // ISFRelationFamiliale[] membre =
    // sf.getRelationsConjointsEtendues("217717", null);
    // for (int i = 0; i < membre.length; i++) {
    // ISFRelationFamiliale element = membre[i];
    // resultStr.append( print(session, element) + "\n");
    // }
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Relations étendue du père 1 (Schwaller Patrick) au 05.01.2000: \n");
    // resultStr.append("**************************** \n");
    // membre = sf.getRelationsConjointsEtendues("217717", "05.01.2000");
    // for (int i = 0; i < membre.length; i++) {
    // ISFRelationFamiliale element = membre[i];
    // resultStr.append( print(session, element) + "\n");
    // }
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Relations étendue de la mère 4 (Schweingruber Liliane Astrid) date courante: \n");
    // resultStr.append("**************************** \n");
    // membre = sf.getRelationsConjointsEtendues("257717", null);
    // for (int i = 0; i < membre.length; i++) {
    // ISFRelationFamiliale element = membre[i];
    // resultStr.append( print(session, element) + "\n");
    // }
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Relations étendue du père 2 (Marc Steger) date courante: \n");
    // resultStr.append("**************************** \n");
    // membre = sf.getRelationsConjointsEtendues("257721", null);
    // for (int i = 0; i < membre.length; i++) {
    // ISFRelationFamiliale element = membre[i];
    // resultStr.append( print(session, element) + "\n");
    // }
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Relations étendue du père 2 (Marc Steger) au 10.01.2000: \n");
    // resultStr.append("**************************** \n");
    // membre = sf.getRelationsConjointsEtendues("257721", "10.01.2000");
    // for (int i = 0; i < membre.length; i++) {
    // ISFRelationFamiliale element = membre[i];
    // resultStr.append( print(session, element) + "\n");
    // }
    //
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Relations étendue du père 2 (Marc Steger) au 21.08.1997: \n");
    // resultStr.append("**************************** \n");
    // membre = sf.getRelationsConjointsEtendues("257721", "21.08.1997");
    // for (int i = 0; i < membre.length; i++) {
    // ISFRelationFamiliale element = membre[i];
    // resultStr.append( print(session, element) + "\n");
    // }
    //
    // System.out.println(resultStr.toString());
    // }
    //
    // /**
    // * @param session
    // * Pour ce test se référer au document de test au point 5.5: périodes des
    // membres
    // *
    // X:\Clients\GLOBAZ\Developpement\Produits\WebPrestations\Tests\pt-WebPrestations-HERA.doc
    // */
    // private void test_5_5(BSession session) throws Exception {
    // StringBuffer resultStr = new StringBuffer();
    // ISFSituationFamiliale sf =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Périodes du père 1 (Marc Steger): \n");
    // resultStr.append("**************************** \n");
    // ISFPeriode[] periode = sf.getPeriodes("384", null);
    // for (int i = 0; i < periode.length; i++) {
    // ISFPeriode element = periode[i];
    // resultStr.append( print(session, element) + "\n");
    // }
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Périodes du mère 1 (Monica): \n");
    // resultStr.append("**************************** \n");
    // periode = sf.getPeriodes("385", null);
    // for (int i = 0; i < periode.length; i++) {
    // ISFPeriode element = periode[i];
    // resultStr.append( print(session, element) + "\n");
    // }
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Périodes de Enfant 2 (Elisa Degen): \n");
    // resultStr.append("**************************** \n");
    // periode = sf.getPeriodes("390", null);
    // for (int i = 0; i < periode.length; i++) {
    // ISFPeriode element = periode[i];
    // resultStr.append( print(session, element) + "\n");
    // }
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Périodes de Enfant 1 (Maria Schmed): \n");
    // resultStr.append("**************************** \n");
    // periode = sf.getPeriodes("388", null);
    // for (int i = 0; i < periode.length; i++) {
    // ISFPeriode element = periode[i];
    // resultStr.append( print(session, element) + "\n");
    // }
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Périodes du père 1 (Marc Steger) uniquement les assurances étrangères: \n");
    // resultStr.append("**************************** \n");
    // periode = sf.getPeriodes("384",
    // ISFSituationFamiliale.CS_TYPE_PERIODE_ASSURANCE_ETRANGERE);
    // for (int i = 0; i < periode.length; i++) {
    // ISFPeriode element = periode[i];
    // resultStr.append( print(session, element) + "\n");
    // }
    // System.out.println(resultStr.toString());
    //
    // }
    //
    // /**
    // * @param session
    // * Pour ce test se référer au document de test au point 5.5: enfant
    // *
    // X:\Clients\GLOBAZ\Developpement\Produits\WebPrestations\Tests\pt-WebPrestations-HERA.doc
    // */
    // private void test_5_6(BSession session) throws Exception {
    // StringBuffer resultStr = new StringBuffer();
    // ISFSituationFamiliale sf =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Détail de l'enfant 1 (Maria Schmed): \n");
    // resultStr.append("**************************** \n");
    // ISFEnfant enfant = sf.getEnfant("388");
    // resultStr.append( print(session, enfant) + "\n");
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Détail de Enfant 2 (Elisa Degen): \n");
    // resultStr.append("**************************** \n");
    // enfant = sf.getEnfant("390");
    // resultStr.append( print(session, enfant) + "\n");
    // resultStr.append("\n\n**************************** \n");
    // resultStr.append("Détail de Enfant 2 (Sven Moser): \n");
    // resultStr.append("**************************** \n");
    // enfant = sf.getEnfant("423");
    // resultStr.append( print(session, enfant) + "\n");
    // System.out.println(resultStr.toString());
    // }
    //
    // private String print(BSession session, ISFEnfant enfant) {
    // return session.getCodeLibelle("Père = " + enfant.getPrenomPere() + " " +
    // enfant.getNomPere() + ", Mère = " + enfant.getPrenomMere() + " " +
    // enfant.getNomMere() + ", date adoption = " + enfant.getDateAdoption());
    // }
    //
    // private String print(BSession session, ISFPeriode element) {
    // return session.getCodeLibelle(element.getType()) + ": " +
    // element.getDateDebut() + " - " + element.getDateFin() + ", pays = " +
    // session.getCodeLibelle(element.getPays()) + ", avs = " +
    // element.getNoAvs()+ ", avs BTE = " + element.getNoAvsDetenteurBTE() +
    // ", idDetententBTE = " + element.getIdDetenteurBTE();
    // }
    //
    // private String print(BSession session, ISFMembreFamilleRequerant element)
    // {
    // return element.getIdMembreFamille() + " " +element.getNom() + " " +
    // element.getPrenom() + " " + element.getDateNaissance() + " " +
    // session.getCodeLibelle(element.getRelationAuRequerant());
    // }
    //
    // private String print(BSession session, ISFMembreFamille element) {
    // return element.getNom() + " " + element.getPrenom() + " " +
    // element.getDateNaissance() + " " +
    // session.getCodeLibelle(element.getCsEtatCivil());
    // }
    //
    // private String print(BSession session, ISFRelationFamiliale relation) {
    // return "Homme = " + relation.getPrenomHomme() + " "
    // +relation.getNomHomme()
    // + ", Femme = " + relation.getPrenomFemme() + " " +relation.getNomFemme()
    // + ", dateDebut = " + relation.getDateDebut()
    // + ", dateFin = " + relation.getDateFin()
    // + ", typeLien = " + session.getCodeLibelle(relation.getTypeLien());
    // }
    //
    // private void testgetMembresFamilleRequerant(BSession session) throws
    // Exception {
    // ISFSituationFamiliale sf =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFMembreFamilleRequerant[] result =
    // sf.getMembresFamilleRequerant("257667"); // Arnaud Dostes (224)
    //
    //
    // for (int i = 0; i < result.length; i++) {
    // ISFMembreFamilleRequerant element = result[i];
    // System.out.println(element.getNom() + " " + element.getPrenom() + " " +
    // session.getCodeLibelle(element.getRelationAuRequerant()));
    // }
    // }
    //
    // private void testGetMembreFamille(BSession session) throws Exception {
    // ISFSituationFamiliale sf =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // int[] ids =
    // {218,222,224,228,231,232,234,235,245,256,247,248,249,255,256,257,258,259,260,261,262,263,264,265};
    // for (int i = 0; i < ids.length; i++) {
    // int id = ids[i];
    // ISFMembreFamille result = sf.getMembreFamille(String.valueOf(id));
    // // System.out.println(id + " " + result.getNom() + " " +
    // result.getPrenom() + " " +
    // session.getCodeLibelle(result.getEtatCivil()));
    // }
    // }
    //
    //
    // private void testgetPeriodes(BSession session) throws RemoteException,
    // Exception {
    // ISFSituationFamiliale sf =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFPeriode[] periodes = sf.getPeriodes("224", null); // Arnaud Dostes
    // (224)
    // for (int i = 0; i < periodes.length; i++) {
    // ISFPeriode periode = periodes[i];
    // System.out.println(periode + " noAVS detenteur = " +
    // periode.getNoAvsDetenteurBTE());
    // }
    // }
    //
    //
    // private void testGetDetailEnfant(BSession session) throws
    // RemoteException, Exception {
    // ISFSituationFamiliale sf =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFEnfant enfant = sf.getEnfant("248"); // 248 , 258
    // /* System.out.println("noAVS enfant = " + enfant.getNoAvs() +
    // "; noAVS pere = "+ enfant.getNoAvsPere() +
    // "; noAVS mere = " + enfant.getNoAvsMere() +
    // "; enfantRecueill = " + enfant.isRecueilli() +
    // "; date adoption = " + enfant.getDateAdoption());
    // */
    // }
    //
    // private void testgetRelationsFamiliales(BSession session) throws
    // RemoteException, Exception {
    // ISFSituationFamiliale sf =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFRelationFamiliale[] relations = sf.getRelationsConjoints("257667",
    // null); // Arnaud Dostes (224)
    // for (int i = 0; i < relations.length; i++) {
    // ISFRelationFamiliale relation = relations[i];
    // System.out.println("noAvsHomme = " + relation.getNoAvsHomme()
    // + "; noAVS femme = " + relation.getNoAvsHomme()
    // + "; dateDebut = " + relation.getDateDebut()
    // + "; dateFin = " + relation.getDateFin()
    // + "; typeLien = " + session.getCodeLibelle(relation.getTypeLien()));
    // }
    // }
    //
    // private void testgetRelationsFamilialesEtendues(BSession session) throws
    // RemoteException, Exception {
    // ISFSituationFamiliale sf =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // ISFRelationFamiliale[] relations =
    // sf.getRelationsConjointsEtendues("257667", null); // Arnaud Dostes (224)
    // for (int i = 0; i < relations.length; i++) {
    // ISFRelationFamiliale relation = relations[i];
    // System.out.println("noAvsHomme = " + relation.getNoAvsHomme()
    // + "; noAVS femme = " + relation.getNoAvsHomme()
    // + "; dateDebut = " + relation.getDateDebut()
    // + "; dateFin = " + relation.getDateFin()
    // + "; typeLien = " + session.getCodeLibelle(relation.getTypeLien()));
    // }
    // }

    /**
     * @param session
     */
    // private void testPeriodeEnfants(BISession session) throws
    // RemoteException, Exception {
    // BITransaction transaction = session.newTransaction();
    // //////////////////////////////////////////////
    // System.out.println("Test Retrieve");
    // ISFApercuPeriodesEnfant requerant = (ISFApercuPeriodesEnfant)
    // session.getAPIFor(ISFApercuPeriodesEnfant.class);
    // Hashtable t = new Hashtable();
    // t.put(ISFApercuPeriodesEnfant.FIND_FOR_ID_REQUERANT, "104");
    // ISFApercuPeriodesEnfant[] periodes = requerant.findPeriodes(t);
    // for (int i = 0; i < periodes.length; i++) {
    // System.out.println(periodes[i].getNomEnfant());
    // System.out.println(periodes[i].getPrenomEnfant());
    // System.out.println(periodes[i].getNumAVS());
    // System.out.println(periodes[i].getDateDebut());
    // System.out.println(periodes[i].getDateFin());
    // }
    //
    // System.out.println("Found ? :" + !requerant.isNew());
    //
    // //////////////////////////////////////////////
    //
    // //////////////////////////////////////////////
    // if (transaction.hasErrors()) {
    // Exception e = new Exception(transaction.getErrors().toString());
    // transaction.rollback();
    // throw e;
    // } else {
    // transaction.commit();
    // }
    // }
    //
    // private void testPeriodeRequerant(BISession session) throws
    // RemoteException, Exception {
    // BITransaction transaction = session.newTransaction();
    // //////////////////////////////////////////////
    // System.out.println("Test Retrieve");
    // ISFApercuPeriodesEnfant requerant = (ISFApercuPeriodesEnfant)
    // session.getAPIFor(ISFApercuPeriodesEnfant.class);
    // Hashtable t = new Hashtable();
    // t.put(ISFApercuPeriodesEnfant.FIND_FOR_ID_REQUERANT, "104");
    // ISFApercuPeriodesEnfant[] periodes = requerant.findPeriodes(t);
    // for (int i = 0; i < periodes.length; i++) {
    // System.out.println(periodes[i].getNomEnfant());
    // System.out.println(periodes[i].getPrenomEnfant());
    // System.out.println(periodes[i].getNumAVS());
    // System.out.println(periodes[i].getDateDebut());
    // System.out.println(periodes[i].getDateFin());
    // }
    //
    // System.out.println("Found ? :" + !requerant.isNew());
    //
    // //////////////////////////////////////////////
    //
    // //////////////////////////////////////////////
    // if (transaction.hasErrors()) {
    // Exception e = new Exception(transaction.getErrors().toString());
    // transaction.rollback();
    // throw e;
    // } else {
    // transaction.commit();
    // }
    // }
    //
    //
    // private void testRequerant(BISession session) throws RemoteException,
    // Exception {
    // BITransaction transaction = session.newTransaction();
    // //////////////////////////////////////////////
    // System.out.println("Test Retrieve");
    // ISFApercuRequerant requerant = (ISFApercuRequerant)
    // session.getAPIFor(ISFApercuRequerant.class);
    // requerant.setIdRequerant("120");
    // requerant.retrieve(transaction);
    // System.out.println("Found ? :" + !requerant.isNew());
    // System.out.println(requerant.getNom() + "," + requerant.getPrenom());
    // //////////////////////////////////////////////
    //
    // //////////////////////////////////////////////
    // if (transaction.hasErrors()) {
    // Exception e = new Exception(transaction.getErrors().toString());
    // transaction.rollback();
    // throw e;
    // } else {
    // transaction.commit();
    // }
    //
    // }

}
