package globaz.apg.module.calcul.salaire;

import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.api.IPRSituationProfessionnelle;
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class APSalaireJournalierCalculateurTest {

    private APSituationProfessionnelle situationProfessionnelle = null;
    private Map<Double, List<APSituationProfessionnelle>> valeurTabelleConfederation = null;

    public APSalaireJournalierCalculateurTest() {
        populateMap();
    }

    @Before
    public void beforeTest() {
        resetSituationProfessionnelle();
    }

    private APSituationProfessionnelle getCleanSituationProfessionnelle() {
        APSituationProfessionnelle situationProfessionnelle = new APSituationProfessionnelle();
        situationProfessionnelle.setIsIndependant(Boolean.FALSE);
        situationProfessionnelle.setSalaireMensuel("0.0");
        situationProfessionnelle.setMontantVerse("0.0");
        situationProfessionnelle.setSalaireHoraire("0.0");
        situationProfessionnelle.setAutreSalaire("0.0");
        situationProfessionnelle.setAutreRemuneration("0.0");
        situationProfessionnelle.setSalaireNature("0.0");

        return situationProfessionnelle;
    }

    private void populateMap() {
        valeurTabelleConfederation = new HashMap<Double, List<APSituationProfessionnelle>>();

        try {

            BufferedReader br = new BufferedReader(new FileReader("resources/apg_service_normal.csv"));

            // ignorer la 1ere ligne -> header
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                List<APSituationProfessionnelle> list = new ArrayList<APSituationProfessionnelle>();
                Double salaireJournalierAttendu = null;
                String[] splitedLine = line.split(",");
                for (int i = 0; i < splitedLine.length; i++) {
                    APSituationProfessionnelle situationPro = getCleanSituationProfessionnelle();
                    // 0) Revenu annuel
                    // 1) Salaire mensuel
                    // 2) Salaire des quatre dernières semaines
                    // 3) Salaire d’une semaine
                    // 4) Salaire ou revenu journalier moyen
                    // 5) APG sans enfant
                    // 6) APG avec 1 enfant
                    // 7) APG avec 2 enfants
                    // 8) APG dès 3 enfants
                    switch (i) {
                        case 0:
                            situationPro.setIsIndependant(Boolean.TRUE);
                            situationPro.setRevenuIndependant(Double.toString(Double.valueOf(splitedLine[i])));
                            list.add(situationPro);
                            break;
                        case 1:
                            situationPro.setSalaireMensuel(Double.toString(Double.parseDouble(splitedLine[i])));
                            list.add(situationPro);
                            break;
                        case 2:
                            situationPro
                                    .setPeriodiciteAutreSalaire(IPRSituationProfessionnelle.CS_PERIODICITE_4_SEMAINES);
                            situationPro.setAutreSalaire(Double.toString(Double.parseDouble(splitedLine[i])));
                            list.add(situationPro);
                            break;
                        case 3:
                            continue; // le salaire hebdomadaire n'est pas supporté par notre application
                        case 4:
                            salaireJournalierAttendu = Double.parseDouble(splitedLine[i]);
                            valeurTabelleConfederation.put(salaireJournalierAttendu, list);
                            continue;
                        default:
                            continue; // on ignore les résultats des APG pour ce teste
                    }
                }
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetSituationProfessionnelle() {
        situationProfessionnelle = getCleanSituationProfessionnelle();
    }

    @Test
    public void testGetSalaireJournalierMoyenAvecTabelles() {
        for (Double expected : valeurTabelleConfederation.keySet()) {
            for (APSituationProfessionnelle situation : valeurTabelleConfederation.get(expected)) {
                testSituationProfessionnelle(expected, situation);
            }
        }
    }

    @Test
    public void testLimitesGetSalaireJournalierMoyen() {
        // limite supérieur : 99.00001 x 30 = 2970.0003 CHF
        testPourSalaireMensuel(100.00, 2970.0003);
        // limite supérieur - 1 : 99.000009 x 30 = 2970.00027
        testPourSalaireMensuel(99.00, 2970.00027);
    }

    private void testPourSalaireMensuel(double salaireJournalierAttendu, double salaireMensuel) {
        situationProfessionnelle = getCleanSituationProfessionnelle();
        situationProfessionnelle.setSalaireMensuel(Double.toString(salaireMensuel));
        testSituationProfessionnelle(salaireJournalierAttendu, situationProfessionnelle);
    }

    private void testSituationProfessionnelle(double salaireJournalierAttendu, APSituationProfessionnelle situation) {
        StringBuilder messageEnCasErreur = new StringBuilder();
        if (!JadeStringUtil.isBlankOrZero(situation.getSalaireHoraire())) {
            messageEnCasErreur.append("Salaire horaire : ").append(situation.getSalaireHoraire()).append(" ");
        }
        if (!JadeStringUtil.isBlankOrZero(situation.getSalaireMensuel())) {
            messageEnCasErreur.append("Salaire mensuel : ").append(situation.getSalaireMensuel()).append(" ");
        }
        if (!JadeStringUtil.isBlankOrZero(situation.getSalaireNature())) {
            messageEnCasErreur.append("Salaire en natur : ").append(situation.getSalaireNature()).append(" ");
        }
        if (!JadeStringUtil.isBlankOrZero(situation.getAutreSalaire())) {
            messageEnCasErreur.append("Autre salaire : ").append(situation.getAutreSalaire()).append(" ");
        }

        Assert.assertEquals(messageEnCasErreur.toString(), BigDecimal.valueOf(salaireJournalierAttendu).doubleValue(),
                APSalaireJournalierCalculateur.getSalaireJournalierMoyen(situation).doubleValue());
    }
}
