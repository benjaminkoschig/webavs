package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.decision;

import static org.fest.assertions.api.Assertions.*;
import org.junit.Test;
import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.domaine.decision.Decision;
import ch.globaz.pegasus.business.domaine.decision.EtatDecision;
import ch.globaz.pegasus.business.domaine.decision.MotifDecision;
import ch.globaz.pegasus.business.domaine.decision.TypeDecision;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;

public class DecisionConverterTest {
    @Test
    public void testConvertToDomainWithOutDateFin() throws Exception {
        DecisionConverter converter = new DecisionConverter();
        SimpleDecisionHeader simpleDecisionHeader = new SimpleDecisionHeader();
        simpleDecisionHeader.setId("1");
        simpleDecisionHeader.setNoDecision("10");
        simpleDecisionHeader.setDateDebutDecision("01.01.2015");
        simpleDecisionHeader.setDateDecision("01.06.2017");
        simpleDecisionHeader.setDateFinDecision(null);
        simpleDecisionHeader.setDatePreparation("03.05.2017");
        simpleDecisionHeader.setDateValidation("04.05.2017");
        simpleDecisionHeader.setValidationPar("toto");
        simpleDecisionHeader.setPreparationPar("tata");
        simpleDecisionHeader.setCsEtatDecision(IPCDecision.CS_ETAT_DECISION_VALIDE);
        simpleDecisionHeader.setCsTypeDecision(IPCDecision.CS_TYPE_OCTROI_AC);
        Decision decsion = converter.convertToDomain(simpleDecisionHeader, null);
        Decision decsion2 = new Decision();
        decsion2.setId("1");
        decsion2.setNumero("10");
        decsion2.setDateDebut(new Date("01.01.2015"));
        decsion2.setDateDecision(new Date("01.06.2017"));
        decsion2.setDateFin(null);
        decsion2.setDatePreparation(new Date("03.05.2017"));
        decsion2.setDateValidation(new Date("04.05.2017"));
        decsion2.setValidationPar("toto");
        decsion2.setPreparationPar("tata");
        decsion2.setEtat(EtatDecision.VALIDE);
        decsion2.setType(TypeDecision.OCTROI_APRES_CALCUL);
        decsion2.setMotif(MotifDecision.INDEFINIT);
        assertThat(decsion).isEqualsToByComparingFields(decsion2);
    }

    @Test
    public void testConvertToDomainWithDateFin() throws Exception {
        DecisionConverter converter = new DecisionConverter();
        SimpleDecisionHeader simpleDecisionHeader = new SimpleDecisionHeader();
        simpleDecisionHeader.setId("1");
        simpleDecisionHeader.setNoDecision("10");
        simpleDecisionHeader.setDateDebutDecision("01.01.2015");
        simpleDecisionHeader.setDateDecision("01.06.2015");
        simpleDecisionHeader.setDateFinDecision("31.12.2016");
        simpleDecisionHeader.setDatePreparation("03.05.2017");
        simpleDecisionHeader.setDateValidation("04.05.2017");
        simpleDecisionHeader.setValidationPar("toto");
        simpleDecisionHeader.setPreparationPar("tata");
        simpleDecisionHeader.setCsEtatDecision(IPCDecision.CS_ETAT_DECISION_VALIDE);
        simpleDecisionHeader.setCsTypeDecision(IPCDecision.CS_TYPE_OCTROI_AC);
        Decision decsion = converter.convertToDomain(simpleDecisionHeader, null);
        Decision decsion2 = new Decision();
        decsion2.setId("1");
        decsion2.setNumero("10");
        decsion2.setDateDebut(new Date("01.01.2015"));
        decsion2.setDateDecision(new Date("01.06.2015"));
        decsion2.setDateFin(new Date("31.12.2016"));
        decsion2.setDatePreparation(new Date("03.05.2017"));
        decsion2.setDateValidation(new Date("04.05.2017"));
        decsion2.setValidationPar("toto");
        decsion2.setPreparationPar("tata");
        decsion2.setEtat(EtatDecision.VALIDE);
        decsion2.setType(TypeDecision.OCTROI_APRES_CALCUL);
        decsion2.setMotif(MotifDecision.INDEFINIT);
        assertThat(decsion).isEqualsToByComparingFields(decsion2);
    }
}
