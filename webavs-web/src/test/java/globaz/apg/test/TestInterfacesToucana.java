package globaz.apg.test;

import globaz.apg.api.alfa.IAPBouclementAlfa;
import globaz.apg.api.alfa.IAPBouclementAlfaLoader;
import globaz.apg.api.droits.IAPDroitAPG;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitMaternite;
import globaz.apg.db.droits.APEmployeur;
import globaz.apg.db.droits.APPeriodeAPG;
import globaz.apg.db.droits.APSituationFamilialeAPG;
import globaz.apg.db.droits.APSituationFamilialeMat;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.db.prestation.APRepartitionJointPrestationManager;
import globaz.apg.db.prestation.APRepartitionPaiements;
import globaz.apg.module.calcul.APBasesCalculBuilder;
import globaz.apg.module.calcul.standard.APCalculateurPrestationStandardLamatAcmAlpha;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.shared.GlobazValueObject;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.db.demandes.PRDemande;
import globaz.pyxis.api.ITIPersonneAvsAdresse;
import java.util.Hashtable;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class TestInterfacesToucana {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // des affilies valides, le no d'affilie sur 12 positions et son nom sur 32
    // positions
    private static final String[][] SETUP_AFFILIES = {
            { "     000.646", "11", "3811", "Kaufmann-Bucher                 " },
            { "     000.596", "9", "3810", "Wernly-Raths                    " } };

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private BSession session;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    // public static Test suite() {
    // TestSuite suite = new TestSuite("Test for globaz.apg.test");
    //
    // // suite.addTestSuite(TestInterfacesToucana.class);
    //
    // return suite;
    // }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */

    public void testInterfaces() throws Exception {
        BTransaction transaction = (BTransaction) session.newTransaction();

        try {
            transaction.openTransaction();

            // creation de trois droits
            genererPrestations(creerDroitAPGSimple(transaction), transaction);
            genererPrestations(creerDroitAPGSimple(transaction), transaction);
            genererPrestations(creerDroitMaterniteSimple(transaction), transaction);

            IAPBouclementAlfaLoader helper = (IAPBouclementAlfaLoader) session.getAPIFor(IAPBouclementAlfaLoader.class);
            IAPBouclementAlfa[] bouclements = helper.load("8", "2005");

            // assertNotNull(bouclements);
            // assertEquals(2, bouclements.length);

            IAPBouclementAlfa bouclementAPG, bouclementAMAT;

            if (IAPBouclementAlfa.TYPE_AMAT.equals(bouclements[0].getType())) {
                bouclementAMAT = bouclements[0];
                bouclementAPG = bouclements[1];
            } else {
                bouclementAMAT = bouclements[1];
                bouclementAPG = bouclements[0];
            }

            // assertEquals("2", bouclementAPG.getNombreCartesACM());
            // assertEquals("954.80", bouclementAPG.getMontantBrutACM());
            // assertEquals("57.70", bouclementAPG.getMontantCotisationsACM());
            // assertEquals("0.00", bouclementAPG.getMontantImpotsACM());

            // assertEquals("5", bouclementAMAT.getNombreCartesACM());
            // assertEquals("954.80", bouclementAMAT.getMontantBrutACM());
            // assertEquals("57.70", bouclementAMAT.getMontantCotisationsACM());
            // assertEquals("0.00", bouclementAMAT.getMontantImpotsACM());
        } finally {
            transaction.rollback();
            transaction.closeTransaction();
        }
    }

    /**
     * setter pour l'attribut up.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */

    protected void setUp() throws Exception {
        if (session == null) {
            session = TestAll.createSession();
        }
    }

    private String creerDemande(BTransaction transaction, boolean apg) throws Exception {
        PRDemande demande = new PRDemande();

        // trouver un tiers
        ITIPersonneAvsAdresse personneAVS = (ITIPersonneAvsAdresse) session.getAPIFor(ITIPersonneAvsAdresse.class);
        Hashtable criteres = new Hashtable();
        Object[] result;

        for (int debutAVS = 510; debutAVS <= 910; debutAVS = debutAVS + 100) {
            criteres.clear();
            criteres.put(ITIPersonneAvsAdresse.FIND_FOR_NUM_AVS_ACTUEL_LIKE, String.valueOf(debutAVS));
            result = personneAVS.find(criteres);

            if ((result != null) && (result.length > 0)) {
                demande.setIdTiers((String) ((GlobazValueObject) result[0]).getProperty("idTiers"));

                break;
            }
        }

        if (JadeStringUtil.isIntegerEmpty(demande.getIdTiers())) {
            throw new Exception("impossible de trouver un tiers");
        }

        demande.setEtat(IPRDemande.CS_ETAT_OUVERT);
        demande.setTypeDemande(apg ? IPRDemande.CS_TYPE_APG : IPRDemande.CS_TYPE_MATERNITE);
        demande.add(transaction);

        return demande.getIdDemande();
    }

    private APDroitLAPG creerDroitAPGSimple(BTransaction transaction) throws Exception {
        // droit
        APDroitAPG droit = new APDroitAPG();

        droit.setSession(session);
        droit.setDateDebutDroit("01.08.2005");
        droit.setDateDepot("01.08.2005");
        droit.setDateReception("01.08.2005");
        droit.setDateFinDroit("31.08.2005");
        droit.setDuplicata(Boolean.FALSE);
        droit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
        droit.setGenreService(IAPDroitLAPG.CS_SERVICE_AVANCEMENT);
        droit.setNbrJourSoldes("31");
        droit.setNoRevision(IAPDroitAPG.CS_REVISION_APG_2005);
        droit.setNpa("1008");
        droit.setPays("100");
        droit.setIdDemande(creerDemande(transaction, true));
        droit.wantCallValidate(false);
        droit.add(transaction);
        droit.wantCallValidate(true);

        // situation familliale
        APSituationFamilialeAPG sitFam = new APSituationFamilialeAPG();

        sitFam.setSession(session);
        sitFam.setIdSitFamAPG(droit.getIdSituationFam());
        sitFam.retrieve(transaction);
        sitFam.setNbrEnfantsDebutDroit("1");
        sitFam.update(transaction);

        // periode
        APPeriodeAPG periodeAPG = new APPeriodeAPG();

        periodeAPG.setSession(session);
        periodeAPG.setDateDebutPeriode("01.08.2005");
        periodeAPG.setDateFinPeriode("31.08.2005");
        periodeAPG.setIdDroit(droit.getIdDroit());
        periodeAPG.add(transaction);
        droit.update(transaction);

        // situation pro
        APEmployeur employeur = new APEmployeur();

        employeur.setSession(session);
        employeur.setIdAffilie(SETUP_AFFILIES[0][1]);
        employeur.setIdTiers(SETUP_AFFILIES[0][2]);
        employeur.setIdParticularite("0");
        employeur.add(transaction);

        APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();

        situationProfessionnelle.setSession(session);
        situationProfessionnelle.setIdEmployeur(employeur.getIdEmployeur());
        situationProfessionnelle.setSalaireMensuel("5000.00");
        situationProfessionnelle.setIdDroit(droit.getIdDroit());
        situationProfessionnelle.add(transaction);

        return droit;
    }

    private APDroitLAPG creerDroitMaterniteSimple(BTransaction transaction) throws Exception {
        // droit
        APDroitMaternite droit = new APDroitMaternite();

        droit.setSession(session);
        droit.setDateDebutDroit("01.08.2005");
        droit.setDateDepot("01.08.2005");
        droit.setDateReception("01.08.2005");
        droit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
        droit.setGenreService(IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE);
        droit.setNpa("1008");
        droit.setPays("100");
        droit.setIdDemande(creerDemande(transaction, false));
        droit.add(transaction);

        // situation familliale
        APSituationFamilialeMat situationFamilialeMat = new APSituationFamilialeMat();

        situationFamilialeMat.setSession(session);
        situationFamilialeMat.setIdDroitMaternite(droit.getIdDroit());
        situationFamilialeMat.setNom("truc");
        situationFamilialeMat.setPrenom("machin");
        situationFamilialeMat.setNoAVS("111.11.111.121");
        situationFamilialeMat.setType(IAPDroitMaternite.CS_TYPE_ENFANT);
        situationFamilialeMat.setDateNaissance("01.08.2005");
        situationFamilialeMat.add(transaction);

        // situation pro
        APEmployeur employeur = new APEmployeur();

        employeur.setSession(session);
        employeur.setIdAffilie(SETUP_AFFILIES[0][1]);
        employeur.setIdTiers(SETUP_AFFILIES[0][2]);
        employeur.setIdParticularite("0");
        employeur.add(transaction);

        APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();

        situationProfessionnelle.setSession(session);
        situationProfessionnelle.setIdEmployeur(employeur.getIdEmployeur());
        situationProfessionnelle.setSalaireMensuel("3000.00");
        situationProfessionnelle.setIdDroit(droit.getIdDroit());
        situationProfessionnelle.add(transaction);

        return droit;
    }

    private void genererPrestations(APDroitLAPG droit, BITransaction transaction) throws Exception {
        APCalculateurPrestationStandardLamatAcmAlpha calculateur = new APCalculateurPrestationStandardLamatAcmAlpha();
        List baseCalculs =APBasesCalculBuilder.of(session, droit).createBasesCalcul();

        calculateur.genererPrestations(session, droit, new FWCurrency(0), baseCalculs);

        // renseigner des adresses de paiement bidons pour les prestations
        APRepartitionJointPrestationManager repartitions = new APRepartitionJointPrestationManager();

        repartitions.setForIdDroit(droit.getIdDroit());
        repartitions.setSession(session);
        repartitions.find();

        for (int idRepartition = 0; idRepartition < repartitions.size(); ++idRepartition) {
            APRepartitionPaiements repartition = (APRepartitionPaiements) repartitions.get(idRepartition);

            repartition.setIdDomaineAdressePaiement("1");
            repartition.setIdTiersAdressePaiement("1");
            repartition.update(transaction);
        }

        calculateur.calculerAcmAlpha(session, droit, baseCalculs);

        // renseigner le champ date de paiement
        APPrestationManager prestations = new APPrestationManager();

        prestations.setForIdDroit(droit.getIdDroit());
        prestations.setSession(session);
        prestations.find();

        for (int idPrestation = 0; idPrestation < prestations.size(); ++idPrestation) {
            APPrestation prestation = (APPrestation) prestations.get(idPrestation);

            prestation.setDatePaiement("18.08.2005");
            prestation.update(transaction);
        }
    }
}
