package ch.globaz.pegasus.business.services.models.annonce.annoncelaprams;

import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionSuppression;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;
import ch.globaz.pegasus.business.models.decision.SimpleValidationDecision;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.pcaccordee.PcaForDecompte;

public interface PrepareAnnonceLapramsService extends JadeApplicationService {

    public void genereAnnonceLapramsSuppression(DecisionSuppression decisionSuppression) throws AnnonceException;

    public void genereAnnonceLapramsValidation(DecisionApresCalcul decisionApresCalcul, DecisionApresCalcul dcAvant,
            PcaForDecompte pcaReplacedHome) throws AnnonceException;

    public void genereAnnonceLapramsValidation(SimpleVersionDroit simpleVersionDroit,
            SimpleDecisionHeader simpleDecisionHeader, SimpleValidationDecision simpleValidationDecision)
            throws AnnonceException;
}
