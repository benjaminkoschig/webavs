/*
 * scr Créé le 3 oct. 05
 */
package globaz.hera.test;

import globaz.globall.db.BSession;

/**
 * @author ado
 * 
 *         3 oct. 05
 */
public class Test {

    public static void main(String[] args) {
        BSession session;
        try {
            session = new BSession("HERA");
            session.connect("adminuser", "adminuser");

            Test tst = new Test();
            // tst.tiersSeeker(session);
            // tst.planTests5_1(session);
            // tst.planTests5_2(session);
            // tst.planTests5_3(session);
            // tst.planTests5_4(session);
            // tst.planTests5_5(session);
            // tst.planTests5_6(session);

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(-1);
    }

    // public void planTests5_1(BSession session) throws Exception {
    //
    // System.out.println("Tests interfaces 5.1 a");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    //
    // testInterfaces5_1(session, "181118");
    //
    //
    //
    //
    // System.out.println("Tests interfaces 5.1 b");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    //
    // JADate date = new JADate("05.01.2002");
    // testInterfaces5_1_date(session, "181118", date);
    //
    //
    //
    //
    // System.out.println("Tests interfaces 5.1 c");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    //
    // testInterfaces5_1(session, "257749");
    //
    //
    //
    //
    // System.out.println("Tests interfaces 5.1 d");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    //
    // date = new JADate("05.08.1998");
    // testInterfaces5_1_date(session, "257749", date);
    //
    //
    //
    // System.out.println("Tests interfaces 5.1 e");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    //
    // testInterfaces5_1(session, "166095");
    //
    //
    //
    // System.out.println("Tests interfaces 5.1 f");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    //
    // testInterfaces5_1(session, "163734");
    //
    //
    //
    //
    // System.out.println("Tests interfaces 5.1 g");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    //
    // testInterfaces5_1(session, "257750");
    //
    //
    //
    // System.out.println("Tests interfaces 5.1 h");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    //
    // date = new JADate("01.01.2000");
    // testInterfaces5_1_date(session, "257750", date);
    //
    // }
    //
    //
    // public void planTests5_2(BSession session) throws Exception {
    //
    //
    // System.out.println("Tests interfaces 5.2 a");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // testInterfaces5_2(session, "517");
    //
    //
    // System.out.println("Tests interfaces 5.2 b");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // JADate date = new JADate("05.01.2002");
    // testInterfaces5_2_date(session, "517", date);
    //
    //
    // System.out.println("Tests interfaces 5.2 c");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // testInterfaces5_2(session, "526");
    //
    //
    // System.out.println("Tests interfaces 5.2 d");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // testInterfaces5_2(session, "518");
    //
    //
    // System.out.println("Tests interfaces 5.2 e");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // date = new JADate("05.01.2000");
    // testInterfaces5_2_date(session, "518", date);
    //
    //
    // System.out.println("Tests interfaces 5.2 f");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // date = new JADate("10.10.1997");
    // testInterfaces5_2_date(session, "518", date);
    //
    //
    // System.out.println("Tests interfaces 5.2 g");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // testInterfaces5_2(session, "528");
    //
    //
    // System.out.println("Tests interfaces 5.2 h");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // testInterfaces5_2(session, "519");
    //
    //
    // System.out.println("Tests interfaces 5.2 i");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // date = new JADate("01.01.1999");
    // testInterfaces5_2_date(session, "519", date);
    //
    // }
    //
    //
    // public void planTests5_3(BSession session) throws Exception {
    //
    // System.out.println("Tests interfaces 5.3 a");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // testInterfaces5_3(session, "181118", null);
    //
    //
    //
    // System.out.println("Tests interfaces 5.3 b");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // JADate date = new JADate("05.01.2000");
    // testInterfaces5_3(session, "181118", date);
    //
    //
    // System.out.println("Tests interfaces 5.3 c");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    //
    // testInterfaces5_3(session, "163734", null);
    //
    //
    //
    // System.out.println("Tests interfaces 5.3 d");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    //
    // testInterfaces5_3(session, "257749", null);
    //
    //
    //
    // System.out.println("Tests interfaces 5.3 e");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // date = new JADate("21.08.1997");
    // testInterfaces5_3(session, "257749", date);
    //
    // }
    //
    //
    //
    //
    //
    //
    //
    // public void planTests5_4(BSession session) throws Exception {
    // System.out.println("Tests interfaces 5.4 a");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // JADate date = new JADate("29.11.2005");
    // testInterfaces5_4(session, "181118", date);
    //
    //
    //
    // System.out.println("Tests interfaces 5.4 b");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // date = new JADate("05.01.2000");
    // testInterfaces5_4(session, "181118", date);
    //
    //
    // System.out.println("Tests interfaces 5.4 c");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // date = new JADate("29.11.2005");
    // testInterfaces5_4(session, "163734", date);
    //
    //
    //
    // System.out.println("Tests interfaces 5.4 d");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // date = new JADate("29.11.2005");
    // testInterfaces5_4(session, "257749", date);
    //
    //
    //
    // System.out.println("Tests interfaces 5.4 e");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // date = new JADate("10.01.2000");
    // testInterfaces5_4(session, "257749", date);
    //
    //
    //
    // System.out.println("Tests interfaces 5.4 f");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // date = new JADate("21.08.1997");
    // testInterfaces5_4(session, "257749", date);
    //
    // }
    //
    //
    //
    // public void planTests5_5(BSession session) throws Exception {
    //
    // System.out.println("Tests interfaces 5.5 a");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // testInterfaces5_5(session, "517");
    //
    //
    // System.out.println("Tests interfaces 5.5 b");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // testInterfaces5_5(session, "518");
    //
    //
    //
    // System.out.println("Tests interfaces 5.5 c");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // testInterfaces5_5(session, "521");
    //
    //
    //
    //
    // System.out.println("Tests interfaces 5.5 d");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // testInterfaces5_5(session, "519");
    // }
    //
    //
    // public void planTests5_6(BSession session) throws Exception {
    //
    // System.out.println("Tests interfaces 5.6 a");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // testInterfaces5_6(session, "519");
    //
    //
    // System.out.println("Tests interfaces 5.6 b");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // testInterfaces5_6(session, "521");
    //
    //
    //
    // System.out.println("Tests interfaces 5.6 c");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // System.out.println("###########################################################");
    // testInterfaces5_6(session, "522");
    // }
    //
    //
    //
    //
    //
    // // private void testInterfaces5_1(BSession session, String idTiers)
    // throws Exception {
    // //
    // // StringBuffer sb = new StringBuffer();
    // // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // //
    // // ISFMembreFamilleRequerant[] mbrs =
    // sitFam.getMembresFamilleRequerant(idTiers);
    // //
    // // for (int i = 0; i < mbrs.length; i++) {
    // // ISFMembreFamilleRequerant mbr = mbrs[i];
    // // sb.append(mbr.getNss() + " : " + mbr.getNom() + " " + mbr.getPrenom()
    // + "\n");
    // // sb.append("nom prénom : " + mbr.getDateNaissance() + " " +
    // mbr.getDateDeces()+ "\n");
    // // sb.append("Relation requerant : " +
    // session.getCodeLibelle(mbr.getRelationAuRequerant())+ "\n");
    // // sb.append("Nationalite : " + mbr.getCsNationalite() + " canton " +
    // session.getCodeLibelle(mbr.getCsCantonDomicile())+ "\n");
    // // sb.append("sexe : " + session.getCodeLibelle(mbr.getCsSexe())+ "\n");
    // // sb.append("ref = " + mbr.getIdMembreFamille()+ "\n");
    // //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // // sb.append("\n");
    // // }
    // // System.out.println(sb.toString());
    // // }
    // //
    // // private void testInterfaces5_1_date(BSession session, String idTiers,
    // JADate date) throws Exception {
    // //
    // // StringBuffer sb = new StringBuffer();
    // // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // //
    // //
    // //
    // //
    // // ISFMembreFamilleRequerant[] mbrs =
    // sitFam.getMembresFamilleRequerant(idTiers, date.toStr("."));
    // //
    // //
    // // for (int i = 0; i < mbrs.length; i++) {
    // // ISFMembreFamilleRequerant mbr = mbrs[i];
    // // sb.append(mbr.getNss() + " : " + mbr.getNom() + " " + mbr.getPrenom()
    // + "\n");
    // // sb.append("nom prénom : " + mbr.getDateNaissance() + " " +
    // mbr.getDateDeces()+ "\n");
    // // sb.append("Relation requerant : " +
    // session.getCodeLibelle(mbr.getRelationAuRequerant())+ "\n");
    // // sb.append("Nationalite : " +
    // session.getCodeLibelle(mbr.getCsNationalite()) + " canton " +
    // session.getCodeLibelle(mbr.getCsCantonDomicile())+ "\n");
    // // sb.append("sexe : " + session.getCodeLibelle(mbr.getCsSexe())+ "\n");
    // // sb.append("ref = " + mbr.getIdMembreFamille()+ "\n");
    // //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // // sb.append("\n");
    // // }
    // // System.out.println(sb.toString());
    // // }
    // //
    // //
    // //
    // //
    // //
    // // private void testInterfaces5_2(BSession session, String idMembre)
    // throws Exception {
    // //
    // // StringBuffer sb = new StringBuffer();
    // // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // //
    // // ISFMembreFamille mbr = sitFam.getMembreFamille(idMembre);
    // //
    // // sb.append(mbr.getNss() + " : " + mbr.getNom() + " " + mbr.getPrenom()
    // + "\n");
    // // sb.append("nom prénom : " + mbr.getDateNaissance() + " " +
    // mbr.getDateDeces()+ "\n");
    // // sb.append("Nationalite : " +
    // session.getCodeLibelle(mbr.getCsNationalite()) + " canton " +
    // session.getCodeLibelle(mbr.getCsCantonDomicile())+ "\n");
    // // sb.append("sexe : " + session.getCodeLibelle(mbr.getCsSexe())+ "\n");
    // // sb.append("etat civil = " +
    // session.getCodeLibelle(mbr.getCsEtatCivil())+ "\n");
    // //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // // sb.append("\n");
    // //
    // // System.out.println(sb.toString());
    // // }
    // //
    // //
    // // private void testInterfaces5_2_date(BSession session, String idMembre,
    // JADate date) throws Exception {
    // //
    // // StringBuffer sb = new StringBuffer();
    // // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // //
    // // ISFMembreFamille mbr = sitFam.getMembreFamille(idMembre,
    // date.toStr("."));
    // //
    // // sb.append(mbr.getNss() + " : " + mbr.getNom() + " " + mbr.getPrenom()
    // + "\n");
    // // sb.append("nom prénom : " + mbr.getDateNaissance() + " " +
    // mbr.getDateDeces()+ "\n");
    // // sb.append("Nationalite : " +
    // session.getCodeLibelle(mbr.getCsNationalite()) + " canton " +
    // session.getCodeLibelle(mbr.getCsCantonDomicile())+ "\n");
    // // sb.append("sexe : " + session.getCodeLibelle(mbr.getCsSexe())+ "\n");
    // // sb.append("etat civil = " +
    // session.getCodeLibelle(mbr.getCsEtatCivil())+ "\n");
    // //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // // sb.append("\n");
    // //
    // // System.out.println(sb.toString());
    // // }
    // //
    // //
    // // private void testInterfaces5_3(BSession session, String idTiers,
    // JADate date) throws Exception {
    // //
    // //
    // // //Père1 : Christian
    // // StringBuffer sb = new StringBuffer();
    // // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // //
    // // ISFRelationFamiliale[] rfs = null;
    // // if (date==null)
    // // rfs = sitFam.getRelationsConjoints(idTiers, null);
    // // else
    // // rfs = sitFam.getRelationsConjoints(idTiers, date.toStr("."));
    // //
    // //
    // // for (int i = 0; i < rfs.length; i++) {
    // // ISFRelationFamiliale rf = rfs[i];
    // //
    // //
    // sb.append("------------------------***************************-------------------------------------------------------->>>>>>>"+
    // "\n");
    // // sb.append("Liens : " + session.getCodeLibelle(rf.getTypeLien()) +
    // "\n");
    // // sb.append(rf.getDateDebut() + " -> " + rf.getDateFin()+ "\n");
    // // sb.append(rf.getPrenomHomme() + " " + rf.getNoAvsHomme()+ "\n");
    // // sb.append(rf.getPrenomFemme() + " " + rf.getNoAvsFemme()+ "\n");
    // //
    // sb.append(">>>>>>>-----------------***************************---------------------------------------------------------------"+
    // "\n");
    // // sb.append("\n");
    // //
    // //
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
    // //
    // // }
    // // System.out.println(sb.toString());
    // //
    // // }
    // //
    // //
    // // private void testInterfaces5_4(BSession session, String idTiers,
    // JADate date) throws Exception {
    // //
    // //
    // // StringBuffer sb = new StringBuffer();
    // // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // //
    // // ISFRelationFamiliale[] rfs =
    // sitFam.getRelationsConjointsEtendues(idTiers, date.toStr("."));
    // // for (int i = 0; i < rfs.length; i++) {
    // // ISFRelationFamiliale rf = rfs[i];
    // //
    // // sb.append("Liens : " + session.getCodeLibelle(rf.getTypeLien()) +
    // "\n");
    // // sb.append(rf.getDateDebut() + " -> " + rf.getDateFin()+ "\n");
    // // sb.append(rf.getPrenomHomme() + " " + rf.getNoAvsHomme()+ "\n");
    // // sb.append(rf.getPrenomFemme() + " " + rf.getNoAvsFemme()+ "\n");
    // //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // // sb.append("\n");
    // // }
    // // System.out.println(sb.toString());
    // //
    // // }
    // //
    // //
    // // private void testInterfaces5_5(BSession session, String idMembre)
    // throws Exception {
    // //
    // //
    // // StringBuffer sb = new StringBuffer();
    // // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // //
    // // ISFPeriode[] periodes = sitFam.getPeriodes(idMembre);
    // // for (int i = 0; i < periodes.length; i++) {
    // // ISFPeriode periode = periodes[i];
    // //
    // //
    // // sb.append(periode.getDateDebut() + " -> " + periode.getDateFin()+
    // "\n");
    // // sb.append(periode.getNoAvs() + " : " + periode.getNoAvsDetenteurBTE()+
    // " = " + periode.getIdDetenteurBTE()+ "\n");
    // // sb.append(session.getCodeLibelle(periode.getType())+ " " +
    // session.getCodeLibelle(periode.getPays()) + "\n");
    // //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // // sb.append("\n");
    // // }
    // // System.out.println(sb.toString());
    // //
    // // }
    // //
    // //
    // // private void testInterfaces5_6(BSession session, String idMembre)
    // throws Exception {
    // //
    // // StringBuffer sb = new StringBuffer();
    // // ISFSituationFamiliale sitFam =
    // SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // //
    // // ISFEnfant enfant = sitFam.getEnfant(idMembre);
    // // sb.append("no avs  enfant = " + enfant.getNss() + "\n");
    // // sb.append( "date adoption = " + enfant.getDateAdoption()+ "\n");
    // // sb.append( "isRecueilli = " + enfant.isRecueilli()+ "\n");
    // // sb.append("père = " + enfant.getPrenomPere() + " " +
    // enfant.getNoAvsPere() + "; mère = " + enfant.getPrenomMere() +" "
    // +enfant.getNoAvsMere()+ "\n");
    // //
    // //
    // sb.append("---------------------------------------------------------------------------------------"+
    // "\n");
    // // sb.append("\n");
    // // System.out.println(sb.toString());
    // //
    // // }
    // //
    // //
    //
    // /**
    // * @param session
    // */
    // private void testPeriodes(BSession session) throws Exception {
    //
    //
    // StringBuffer sb = new StringBuffer();
    //
    // // trouver les conjoints
    // SFApercuRelationFamilialeRequerantManager famille = new
    // SFApercuRelationFamilialeRequerantManager();
    // famille.setSession(session);
    // famille.setForIdRequerant("104");
    // famille.find();
    // for (int i = 0; i < famille.getSize(); i++) {
    // SFApercuRelationFamilialeRequerant f =
    // (SFApercuRelationFamilialeRequerant) famille.getEntity(i);
    // SFApercuEnfantManager enfants = new SFApercuEnfantManager();
    // enfants.setSession(session);
    // enfants.setForIdMembreFamille(f.getIdMembreFamille());
    // enfants.find();
    // for (int j = 0; j < enfants.getSize(); j++) {
    // SFApercuEnfant enfant = (SFApercuEnfant) enfants.getEntity(j);
    // System.out.println(enfant.getNom());
    // SFPeriodeManager periodes = new SFPeriodeManager();
    // periodes.setSession(session);
    // periodes.setForIdMembreFamille(enfant.getIdMembreFamille());
    // periodes.setForType(ISFSituationFamiliale.CS_TYPE_PERIODE_ETUDE);
    // periodes.find();
    // for (int k = 0; k < periodes.getSize(); k++) {
    // SFPeriode periode = (SFPeriode) periodes.getEntity(k);
    // System.out.println(periode.getDateDebut());
    // System.out.println(periode.getDateFin());
    // }
    // }
    // }
    // }
    // private void testCI(BSession session) throws Exception {
    //
    // System.out.println(SFApplication.searchTiersCI("9", session));
    //
    // }
    //
    //
    // private void tiersSeeker(BSession session) throws Exception{
    // List l = new ArrayList();
    // //l.add(PRUtil.PROVENANCE_CI);
    // l.add(SFUtil.PROVENANCE_TIERS);
    // //Recherche dans les CI
    // String s = SFUtil.getNumerosSecuriteSocialeOptionList(session, "112",
    // "false", l);
    // System.out.println(s);
    // //PRTiersWrapper vo = PRTiersHelper.getPersonneAVS(session, "174825");
    // //System.out.println(vo);
    // //System.out.println("code etat civil" +
    // vo.getProperty(PRTiersWrapper.PROPERTY_PERSONNE_AVS_ETAT_CIVIL));
    // //System.out.println("etat civil = " +
    // session.getCode((String)vo.getProperty(PRTiersWrapper.PROPERTY_PERSONNE_AVS_ETAT_CIVIL)));
    // }
    //
    //
    // private void getTiers(BSession session) throws Exception {
    //
    // //Recherche dans les CI
    // SFTiersWrapper tier = SFTiersHelper.getTiersAdresseParId(session,
    // "176624");
    //
    // System.out.println(tier.getProperty(SFTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)
    // + " " + tier.getProperty(SFTiersWrapper.PROPERTY_NOM) + " " +
    // tier.getProperty(SFTiersWrapper.PROPERTY_PRENOM) + " idTiers = " +
    // tier.getProperty(SFTiersWrapper.PROPERTY_ID_TIERS));
    // }
    //

}
