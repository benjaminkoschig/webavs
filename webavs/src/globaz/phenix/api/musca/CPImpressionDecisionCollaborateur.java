package globaz.phenix.api.musca;

public class CPImpressionDecisionCollaborateur extends CPImpressionDecisionImpl {
    @Override
    protected String getOrder() {
        // tri par collaborateur et ensuite N� affili�
        return "1";
    }
}
