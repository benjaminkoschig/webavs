package ch.globaz.pegasus.businessimpl.tests.mutation.testCase;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.pegasus.business.exceptions.models.MutationException;
import ch.globaz.pegasus.business.models.mutation.RecapInfoDomaine;
import ch.globaz.pegasus.business.models.recap.Recap;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.mutation.MutationCategorieResolver.RecapDomainePca;

public class MutationVerifiMontant {

    @Test
    @Ignore
    public final void testDiffEtComptat() throws MutationException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        Map<String, BigDecimal> mapDonneeComptaGenAi = new HashMap<String, BigDecimal>();
        Map<String, BigDecimal> mapDonneeComptaGenAvs = new HashMap<String, BigDecimal>();
        Map<RecapDomainePca, Map<String, BigDecimal>> mapComptaGen = new HashMap<RecapDomainePca, Map<String, BigDecimal>>();
        Map<String, Integer> mapDiff = new HashMap<String, Integer>();

        // mapDiff.put("11.2012", 2571);

        // mapDonneeComptaGenAi.put("06.2012", new BigDecimal(1113333).setScale(0));
        mapDonneeComptaGenAi.put("07.2012", new BigDecimal(1195925).setScale(0));
        mapDonneeComptaGenAi.put("08.2012", new BigDecimal(1244589).setScale(0));
        mapDonneeComptaGenAi.put("09.2012", new BigDecimal(1247484).setScale(0));
        mapDonneeComptaGenAi.put("10.2012", new BigDecimal(1276771).setScale(0));
        mapDonneeComptaGenAi.put("11.2012", new BigDecimal(1468869).setScale(0));
        mapDonneeComptaGenAi.put("12.2012", new BigDecimal(1349110).setScale(0));
        mapDonneeComptaGenAi.put("01.2013", new BigDecimal(1401112).setScale(0));
        // mapDonneeComptaGenAi.put("02.2013", new BigDecimal(1025216).setScale(0));

        // mapDonneeComptaGenAvs.put("06.2012", new BigDecimal(1488407).setScale(0));
        mapDonneeComptaGenAvs.put("07.2012", new BigDecimal(1640187).setScale(0));
        mapDonneeComptaGenAvs.put("08.2012", new BigDecimal(1691410).setScale(0));
        mapDonneeComptaGenAvs.put("09.2012", new BigDecimal(1694392).setScale(0));
        mapDonneeComptaGenAvs.put("10.2012", new BigDecimal(1753128).setScale(0));
        mapDonneeComptaGenAvs.put("11.2012", new BigDecimal(2162089).setScale(0));
        mapDonneeComptaGenAvs.put("12.2012", new BigDecimal(1831100).setScale(0));
        mapDonneeComptaGenAvs.put("01.2013", new BigDecimal(1757503).setScale(0));
        // mapDonneeComptaGenAvs.put("02.2013", new BigDecimal(1734667).setScale(0));

        mapComptaGen.put(RecapDomainePca.AI, mapDonneeComptaGenAi);
        mapComptaGen.put(RecapDomainePca.AVS, mapDonneeComptaGenAvs);

        for (Entry<String, BigDecimal> e : mapDonneeComptaGenAvs.entrySet()) {
            Recap recap = PegasusServiceLocator.getRecapService().createRecap(e.getKey());

            for (RecapDomainePca domaine : RecapDomainePca.values()) {
                recap.getInfosDomaine().get(domaine).getDifference();
                BigDecimal expectedMontant = mapComptaGen.get(domaine).get(e.getKey());
                RecapInfoDomaine infoDomaine = recap.getInfosDomaine().get(domaine);

                Assert.assertEquals(
                        "Test montant paiement recap avec la montant de la compta gent pour la date:" + e.getKey()
                                + " , domaine " + domaine.getCode(), expectedMontant, infoDomaine.getTotalPaiement()
                                .setScale(0));

                // Les donnée ne sont pas juste du a la rprise de donées de la ccju
                if ("11.2012".equals(e.getKey()) && domaine.equals(RecapDomainePca.AVS)) {
                    Assert.assertEquals(
                            "Vérifi que l'on na pas de diff. Domaine:" + domaine.getCode() + ", date:" + e.getKey(),
                            2571, infoDomaine.getDifference().toBigInteger().intValue());
                } else {
                    Assert.assertEquals(
                            "Vérifi que l'on na pas de diff. Domaine:" + domaine.getCode() + ", date:" + e.getKey(), 0,
                            infoDomaine.getDifference().toBigInteger().intValue());
                }
            }

        }

    }
}
