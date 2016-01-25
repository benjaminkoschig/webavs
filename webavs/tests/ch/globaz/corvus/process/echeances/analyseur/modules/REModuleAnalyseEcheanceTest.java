package ch.globaz.corvus.process.echeances.analyseur.modules;

import globaz.corvus.db.echeances.REEcheancesEntity;
import globaz.globall.api.BISession;
import globaz.utils.SessionForTestBuilder;
import org.junit.Assert;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;

/**
 * Permet de vérifier le fonctionnement des modules d'analyse d'échéance, dans les rentes
 * 
 * @author PBA
 */
public class REModuleAnalyseEcheanceTest {

    protected REEcheancesEntity entity;
    protected REModuleAnalyseEcheance module;
    protected BISession session;

    public REModuleAnalyseEcheanceTest() {
        session = SessionForTestBuilder.getSessionStub();
    }

    protected void assertFalse(REModuleAnalyseEcheance module, IREEcheances entity, String moisTraitement) {
        module.setMoisTraitement(moisTraitement);
        REReponseModuleAnalyseEcheance reponse = module.eval(entity);

        StringBuilder message = new StringBuilder();
        message.append("\n\nAttendu : faux");
        message.append("\nReçu : ").append(reponse.getMotif().toString());
        message.append("\n");

        Assert.assertFalse(message.toString(), reponse.isListerEcheance());
    }

    protected void assertTrue(REModuleAnalyseEcheance module, IREEcheances entity, String moisTraitement,
            REMotifEcheance motif) {
        module.setMoisTraitement(moisTraitement);
        REReponseModuleAnalyseEcheance reponse = module.eval(entity);

        StringBuilder message = new StringBuilder();
        message.append("\n\nAttendu : ").append(motif);
        message.append("\nReçu : ");
        if (reponse.isListerEcheance()) {
            message.append(reponse.getMotif());
        } else {
            message.append("faux");
        }
        message.append("\n\n");
        Assert.assertTrue(message.toString(), module.eval(entity).isListerEcheance());
        Assert.assertEquals(message.toString(), motif, module.eval(entity).getMotif());
    }
}
