package globaz.naos.api.test;

import globaz.framework.api.IFWPostIt;
import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.util.JACalendar;
import globaz.naos.api.IAFAdhesion;
import globaz.naos.api.IAFAffiliation;
import globaz.naos.api.IAFAssurance;
import globaz.naos.api.IAFCotisation;
import globaz.naos.api.IAFLienAffiliation;
import globaz.naos.api.IAFNombreAssures;
import globaz.naos.api.IAFParticulariteAffiliation;
import globaz.naos.api.IAFPlanAffiliation;
import globaz.naos.api.IAFPlanCaisse;
import globaz.naos.api.IAFSuiviCaisseAffiliation;
import globaz.pyxis.api.ITIAdministration;
import java.util.Hashtable;

/**
 * <code>AffiliationAPITest</code> est une classe de test des API d'Affiliation
 * 
 * @author David Girardin
 */
public class AffiliationAPITest {
    /**
     * Exécute le test
     * 
     * @param args
     *            [] arguments: idTiers
     */
    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                throw new Exception("Wrong number of parameters");
            }
            AffiliationAPITest test = new AffiliationAPITest();
            // API GLOBAZ:
            // - recherche d'une application
            // (sa localisation est définie dans le fichier GlobazSystem.xml)
            //
            BIApplication application = GlobazSystem.getApplication("NAOS");
            // API GLOBAZ:
            // - ouverture d'une session utilisateur sur l'application obtenue
            //
            test.session = application.newSession("cicicam", "cicicam");
            //
            test.findAffiliations(args);

        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.exit(0);
    }

    /** Session */
    private BISession session;

    /** Mode de renvoi des informations sur System.out */
    private boolean verbose = true;

    /**
     * Récupère une liste des affiliations
     */
    public long findAffiliations(String[] idAffs) {

        long beginTime = System.currentTimeMillis();
        try {
            // API GLOBAZ:
            // - récupération de la classe d'implémentation d'une API définie
            // par une interface
            //
            IAFAffiliation affiliationIfc = (IAFAffiliation) session.getAPIFor(IAFAffiliation.class);
            // des critères de recherche peuvent être passés (voir la
            // documentation dans la javadoc)
            Hashtable criteres = new Hashtable();
            // criteres.put(IAFAffiliation.FIND_FOR_IDTIERS, idTiers);

            for (int ids = 0; ids < idAffs.length; ids++) {
                String idAff = idAffs[ids];

                criteres.put(IAFAffiliation.FIND_FOR_AFFILIATIONID, idAff);
                // chargement des affiliation du tiers
                IAFAffiliation[] result = affiliationIfc.findAffiliation(criteres);
                if (result != null && result.length != 0) {
                    IAFAdhesion adhesionIfc = (IAFAdhesion) session.getAPIFor(IAFAdhesion.class);
                    IAFPlanAffiliation planAffiliationIfc = (IAFPlanAffiliation) session
                            .getAPIFor(IAFPlanAffiliation.class);
                    IAFPlanCaisse planCaisseIfc = (IAFPlanCaisse) session.getAPIFor(IAFPlanCaisse.class);
                    IAFCotisation cotisationIfc = (IAFCotisation) session.getAPIFor(IAFCotisation.class);
                    IAFAssurance assuranceIfc = (IAFAssurance) session.getAPIFor(IAFAssurance.class);
                    IAFLienAffiliation liaisonIfc = (IAFLienAffiliation) session.getAPIFor(IAFLienAffiliation.class);
                    IAFNombreAssures assures = (IAFNombreAssures) session.getAPIFor(IAFNombreAssures.class);
                    IAFParticulariteAffiliation particularites = (IAFParticulariteAffiliation) session
                            .getAPIFor(IAFParticulariteAffiliation.class);
                    IAFSuiviCaisseAffiliation suivis = (IAFSuiviCaisseAffiliation) session
                            .getAPIFor(IAFSuiviCaisseAffiliation.class);
                    IFWPostIt observations = (IFWPostIt) session.getAPIFor(IFWPostIt.class);
                    // effacer observation tiers
                    // observations.setEntityId(idTiers,IFWPostIt.ENTITY_TIERS);
                    /*
                     * observations.setEntityId(idAff,IFWPostIt.ENTITY_AFFILIATION ); observations.retrieve(null);
                     * if(observations.getText().length()!=0) { observations.delete(null); }
                     */
                    for (int i = 0; i < result.length; i++) {
                        println("Affiliation no " + idAff + ": " + result[i].getAffilieNumero()
                                + (result[i].getDateFin().length() != 0 ? " (radiée)" : ""));
                        println("  Date d'affiliation: " + result[i].getDateDebut());
                        println("  Date de création: " + result[i].getDateCreation());
                        println("  Périodicité: " + session.getCodeLibelle(result[i].getPeriodicite()));
                        println("  Type: " + session.getCodeLibelle(result[i].getTypeAffiliation()));
                        // recherche de l'observation associée
                        /*
                         * observations.setText(""); observations.setEntityId(result
                         * [i].getAffiliationId(),IFWPostIt.ENTITY_AFFILIATION); observations.retrieve(null);
                         * if(observations.getText().length()!=0) { println("  Observations (id) = "
                         * +observations.extractEntityId()+":"); println("  content:"+observations.getText()); // add
                         * line observations.addLine("nouvelle ligne"); observations.update(null); } else { // création
                         * d'une nouvelle observation observations.addLine("nouvelle observation");
                         * observations.add(null); } // test suppression observations.delete(null);
                         * observations.setText(""); observations .setEntityId(result[i].getAffiliationId(),
                         * IFWPostIt.ENTITY_AFFILIATION); observations.retrieve(null);
                         * if(observations.getText().length()!=0) { println("  Observations (id) = "
                         * +observations.extractEntityId()+":"); println("  content:"+observations.getText()); // add
                         * line observations.addLine("nouvelle ligne"); observations.update(null); } else { // création
                         * d'une nouvelle observation observations.addLine("nouvelle observation");
                         * observations.add(null); }
                         * 
                         * // ajout observation sur tiers observations.setText(""); observations.setEntityId(result
                         * [i].getIdTiers(),IFWPostIt.ENTITY_TIERS); observations.retrieve(null);
                         * if(observations.getText().length()!=0) { observations.
                         * addLine("Affilié sous "+result[i].getAffilieNumero ()); observations.update(null); } else {
                         * observations. addLine("Affilié sous "+result[i].getAffilieNumero ()); observations.add(null);
                         * }
                         */
                        // recherche des adhésions aux caisses
                        criteres = new Hashtable();
                        criteres.put(IAFAdhesion.FIND_FOR_AFFILIATION_ID, result[i].getAffiliationId());
                        IAFAdhesion[] resultAdhesion = adhesionIfc.findAdhesions(criteres);
                        if (resultAdhesion != null && resultAdhesion.length != 0) {
                            for (int j = 0; j < resultAdhesion.length; j++) {
                                planCaisseIfc.setPlanCaisseId(resultAdhesion[j].getPlanCaisseId());
                                planCaisseIfc.retrieve(null);
                                println("  Adhère à la caisse: " + planCaisseIfc.getAdministrationNo() + " (plan "
                                        + planCaisseIfc.getLibelle() + ")");
                            }
                        }
                        // recherche des plans d'affiliation
                        criteres = new Hashtable();
                        criteres.put(IAFPlanAffiliation.FIND_FOR_AFFILIATION_ID, result[i].getAffiliationId());
                        IAFPlanAffiliation[] resultPlan = planAffiliationIfc.findPlanAffiliation(criteres);
                        if (resultPlan != null && resultPlan.length != 0) {
                            for (int j = 0; j < resultPlan.length; j++) {
                                // recherche des cotisations
                                criteres = new Hashtable();
                                criteres.put(IAFCotisation.FIND_FOR_PLAN_AFFILIATION_ID,
                                        resultPlan[j].getPlanAffiliationId());
                                IAFCotisation[] resultCoti = cotisationIfc.findCotisations(criteres);
                                if (resultCoti != null && resultCoti.length != 0) {
                                    for (int k = 0; k < resultCoti.length; k++) {
                                        assuranceIfc.setAssuranceId(resultCoti[k].getAssuranceId());
                                        assuranceIfc.retrieve(null);
                                        println("  cotise pour l'assurance "
                                                + assuranceIfc.getAssuranceLibelleCourtFr() + " dès le "
                                                + resultCoti[k].getDateDebut());
                                    }
                                }
                            }
                        }

                        // recherche des liaisons
                        criteres = new Hashtable();
                        // criteres.put(IAFLienAffiliation.FIND_FOR_AFFILIATION_ID,
                        // result[i].getAffiliationId());
                        criteres.put(IAFLienAffiliation.FIND_FOR_AFFILIATION_ID_LIEN, "268");
                        IAFLienAffiliation[] resultLiaisons = liaisonIfc.findLiaisons(criteres);
                        if (resultLiaisons != null && resultLiaisons.length != 0) {
                            for (int j = 0; j < resultLiaisons.length; j++) {
                                String idAffiliationLiee = resultLiaisons[j].getLienAffiliationId();
                                // recherce de l'affiliation liée
                                affiliationIfc.setAffiliationId(idAffiliationLiee);
                                affiliationIfc.retrieve(null);
                                println("  " + session.getCodeLibelle(resultLiaisons[j].getTypeLien()) + " " + "("
                                        + resultLiaisons[j].getAffiliationId() + "/"
                                        + resultLiaisons[j].getAffiliationLieeId() + ")"
                                        + affiliationIfc.getAffilieNumero());
                            }
                        }

                        // recherche des assurés
                        criteres = new Hashtable();
                        criteres.put(IAFNombreAssures.FIND_FOR_AFFILIATION_ID, result[i].getAffiliationId());
                        IAFNombreAssures[] resultAssures = assures.findNombreAssures(criteres);
                        if (resultAssures != null && resultAssures.length != 0) {
                            for (int j = 0; j < resultAssures.length; j++) {
                                assuranceIfc.setAssuranceId(resultAssures[j].getAssuranceId());
                                assuranceIfc.retrieve(null);
                                println("  " + resultAssures[j].getNbrAssures() + " assurés pour l'assurance "
                                        + assuranceIfc.getAssuranceLibelleCourtFr() + " en "
                                        + resultAssures[j].getAnnee());
                            }
                        }

                        // recherche des particularités
                        criteres = new Hashtable();
                        // criteres.put(IAFParticulariteAffiliation.FIND_FOR_AFFILIATION_ID,
                        // result[i].getAffiliationId());
                        criteres.put(IAFParticulariteAffiliation.FIND_FOR_PARTICULARITE_ID, "818008");
                        IAFParticulariteAffiliation[] resultParticularites = particularites
                                .findParticularites(criteres);
                        if (resultParticularites != null && resultParticularites.length != 0) {
                            for (int j = 0; j < resultParticularites.length; j++) {
                                println("  "
                                        + session.getCodeLibelle(resultParticularites[j].getParticularite())
                                        + " depuis le "
                                        + resultParticularites[j].getDateDebut()
                                        + (resultParticularites[j].getDateFin().length() != 0 ? " jusqu'au "
                                                + resultParticularites[j].getDateFin() : ""));
                            }
                        }

                        // recherche suivi
                        criteres = new Hashtable();
                        criteres.put(IAFSuiviCaisseAffiliation.FIND_FOR_AFFILIATION_ID, result[i].getAffiliationId());
                        IAFSuiviCaisseAffiliation[] resultSuivi = suivis.findSuiviCaisse(criteres);
                        if (resultSuivi != null && resultSuivi.length != 0) {
                            for (int j = 0; j < resultSuivi.length; j++) {
                                if (resultSuivi[j].getIdTiersCaisse().length() != 0) {
                                    BISession sessionTiers = GlobazSystem.getApplication("PYXIS").newSession(session);
                                    // sessionTiers.connectSession(session);
                                    ITIAdministration caisse = (ITIAdministration) sessionTiers
                                            .getAPIFor(ITIAdministration.class);
                                    caisse.setIdTiersAdministration(resultSuivi[j].getIdTiersCaisse());
                                    caisse.retrieve(null);
                                    println("  "
                                            + "cotise à la caisse externe "
                                            + caisse.getCodeAdministration()
                                            + " dès le "
                                            + resultSuivi[j].getDateDebut()
                                            + (resultSuivi[j].getDateFin().length() != 0 ? " jusqu'au "
                                                    + resultSuivi[j].getDateFin() : "") + " pour l'assurance "
                                            + session.getCodeLibelle(resultSuivi[j].getGenreCaisse()));
                                }
                            }
                        }
                        // tests pour les AF
                        IAFAffiliation affToTest = result[i];
                        affToTest.setISession(session);
                        // agence communale
                        println("  " + "agence communale: " + affToTest.getAgenceCom("", JACalendar.todayJJsMMsAAAA()));
                        // actif AF
                        if (affToTest.getActifAF("01.01.2004", "S").booleanValue()) {
                            // ok pour les AF
                            println("  " + "a droit aux prestations AF");
                        } else {
                            println("  " + "n'a pas droit aux prestations AF: " + affToTest.getISession().getErrors());
                        }
                        // affiliation de facturation
                        IAFAffiliation affMM = affToTest.getAffiliationFacturationAF("01.01.2004");
                        if (affMM != null) {
                            println("  " + "décompte AF sous " + affMM.getAffilieNumero());
                        } else {
                            println("  " + "décompte AF impossible: " + affToTest.getISession().getErrors());
                        }

                        // canton AF
                        println("  " + "canton AF: " + affToTest.getCantonAF("01.01.2004"));

                    }
                } else {
                    println("Aucune affiliation trouvée pour l'id " + idAff);
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        return (endTime - beginTime);
    }

    /**
     * Imprime une ligne d'information
     * 
     * @param text
     *            le texte à imprimer
     */
    public void println(String text) {
        if (verbose) {
            System.out.println(text);
        }
    }
}
