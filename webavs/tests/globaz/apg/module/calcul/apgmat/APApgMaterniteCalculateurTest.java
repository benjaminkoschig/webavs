package globaz.apg.module.calcul.apgmat;

import globaz.apg.module.calcul.constantes.IAPConstantes;
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class APApgMaterniteCalculateurTest {

    private Map<Double, Double> valeurTabelleConfederation;

    public APApgMaterniteCalculateurTest() {
        // ajout des salaire - APG correspondant selon la tabelle de la conf�d�ration
        populateMap();
    }

    private void populateMap() {
        valeurTabelleConfederation = new HashMap<Double, Double>();

        try {

            final BufferedReader br = new BufferedReader(new FileReader("resources/apg_maternite.csv"));

            // ignorer la 1ere ligne -> header
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                Double salaireJournalierAttendu = null;
                Double allocation = null;

                final String[] splitedLine = line.split(",");
                for (int i = 0; i < splitedLine.length; i++) {
                    // 0) Revenu annuel
                    // 1) Salaire mensuel
                    // 2) Salaire des quatre derni�res semaines
                    // 3) Salaire d�une semaine
                    // 4) Salaire ou revenu journalier moyen
                    // 5) Allocation
                    switch (i) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                            break; // on ne teste pas les salaires ici
                        case 4:
                            salaireJournalierAttendu = Double.parseDouble(splitedLine[i]);
                            break;
                        case 5:
                            allocation = Double.parseDouble(splitedLine[i]);
                            valeurTabelleConfederation.put(allocation, salaireJournalierAttendu);
                            break;
                        default:
                            continue; // on ignore les r�sultats des APG pour ce teste
                    }
                }
            }

            br.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void testAllocationMaternite(final Double expected, final Double salaireMoyenJournalier) {
        Assert.assertEquals(BigDecimal.valueOf(expected),
                APApgMaterniteCalculateurTest.getIndemniteJournaliere(BigDecimal.valueOf(salaireMoyenJournalier)));
    }

    @Test
    public void testGetIndemniteJournaliereAvecTabelle() {
        for (final Double expected : valeurTabelleConfederation.keySet()) {
            testAllocationMaternite(expected, valeurTabelleConfederation.get(expected));
        }
    }

    @Ignore
    @Test
    public void testIndemniteJournaliereValeurLimiteSuperieur() {
        // limite sup�rieur : 245.- CHF --> limite max donc 196.- CHF/jour
        Assert.assertEquals(IAPConstantes.APG_JOURNALIERE_MAX,
                APApgMaterniteCalculateurTest.getIndemniteJournaliere(BigDecimal.valueOf(245.0)));
        // limite sup�rieur + 1 : 245.00001 CHF --> limite max donc 196.- CHF/jour
        Assert.assertEquals(IAPConstantes.APG_JOURNALIERE_MAX,
                APApgMaterniteCalculateurTest.getIndemniteJournaliere(BigDecimal.valueOf(245.00001)));
        // limite sup�rieur - 1 : 243.99999 CHF --> limite max moins 0.80 donc 195.20 CHF/jour
        Assert.assertEquals(IAPConstantes.APG_JOURNALIERE_MAX.subtract(BigDecimal.valueOf(0.8)),
                APApgMaterniteCalculateurTest.getIndemniteJournaliere(BigDecimal.valueOf(243.99999)));

        // limite inf�rieur : 0.- CHF --> limite inf�rieur donc 0.- CHF/jour
        Assert.assertEquals(BigDecimal.ZERO, APApgMaterniteCalculateurTest.getIndemniteJournaliere(BigDecimal.ZERO));
        // limite inf�rieur + 1 : 0.00001 CHF --> limite inf�rieur plus 0.80 donc 0.80 CHF/jour
        Assert.assertEquals(BigDecimal.valueOf(0.80),
                APApgMaterniteCalculateurTest.getIndemniteJournaliere(BigDecimal.valueOf(0.00001)));
        // limite inf�rieur - 1 : -0.00001 CHF --> limite inf�rieur donc 0.- CHF/jour
        Assert.assertEquals(BigDecimal.valueOf(0.80),
                APApgMaterniteCalculateurTest.getIndemniteJournaliere(BigDecimal.valueOf(-0.00001)));
    }

    /**
     * Retourne l'ind�mnit� journali�re (arrondi au 10 centime pr�s) calcul�e selon le salaire journalier moyen pass� en
     * param�tre
     * 
     * @param salaireJournalierMoyen
     * @return
     */
    public static BigDecimal getIndemniteJournaliere(final BigDecimal salaireJournalierMoyen) {

        if (IAPConstantes.MONTANT_JOURNALIER_MAX.compareTo(salaireJournalierMoyen) == 1) {
            // calcul selon le fascicule de la Conf�d�ration
            final double apgJournaliere = ((8.0 * salaireJournalierMoyen.doubleValue()) + 0.9) / 10.0;
            // arrondi au 10 centimes
            return BigDecimal.valueOf(apgJournaliere).setScale(1, BigDecimal.ROUND_DOWN);
        }
        // si �gal ou plus grand que le revenu journalier max, retourne l'apg journalier max
        return IAPConstantes.APG_JOURNALIERE_MAX;
    }

}
