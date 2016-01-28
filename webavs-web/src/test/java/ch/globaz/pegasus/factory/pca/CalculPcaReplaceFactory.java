package ch.globaz.pegasus.factory.pca;

import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;
import ch.globaz.pegasus.business.models.calcul.CalculPcaReplace;

public class CalculPcaReplaceFactory {

    public static CalculPcaReplace generateDom2R(String dateDebut, String dateFin, String idPca, String idVersionDroit) {
        CalculPcaReplace pcaReplace = new CalculPcaReplace();
        pcaReplace.setNoVersion("2");
        pcaReplace.setIdDroit("155");
        pcaReplace.setSimpleInformationsComptabilite(SimpleInformationsComptabiliteFactory.generate());
        pcaReplace.setSimpleInformationsComptabiliteConjoint(SimpleInformationsComptabiliteFactory.generate());
        pcaReplace.setSimplePCAccordee(SimplePCAccordeeFactory.generate(dateDebut, dateFin, idVersionDroit));
        pcaReplace.setSimplePlanDeCalcul(SimplePlanDeCalculFactory.generate());
        pcaReplace.setSimplePrestationsAccordees(SimplePrestationsAccordeesFactory.generateForRequerant(dateDebut,
                dateFin));
        pcaReplace.setSimplePrestationsAccordeesConjoint(SimplePrestationsAccordeesFactory.generateForConjoint(
                dateDebut, dateFin));
        pcaReplace.setId(idPca);
        return pcaReplace;
    }

    public static CalculPcaReplace generateForConjoint(String dateDebut, String dateFin, String idPca) {
        CalculPcaReplace pcaReplace = CalculPcaReplaceFactory.generateForConjoint(dateDebut, dateFin, idPca, null);
        return pcaReplace;
    }

    private static CalculPcaReplace generateForConjoint(String dateDebut, String dateFin, String idPca,
            String idVersionDroit) {
        CalculPcaReplace pcaReplace = new CalculPcaReplace();
        pcaReplace.setNoVersion("2");
        pcaReplace.setIdDroit("155");
        pcaReplace.setSimpleInformationsComptabilite(SimpleInformationsComptabiliteFactory.generate());
        pcaReplace.setSimpleInformationsComptabiliteConjoint(SimpleInformationsComptabiliteFactory.generate());
        pcaReplace.setSimplePCAccordee(SimplePCAccordeeFactory.generate(dateDebut, dateFin, idVersionDroit));
        pcaReplace.setSimplePlanDeCalcul(SimplePlanDeCalculFactory.generate());
        pcaReplace.setSimplePrestationsAccordees(SimplePrestationsAccordeesFactory.generateForConjoint(dateDebut,
                dateFin));
        pcaReplace.setSimplePrestationsAccordeesConjoint(new SimplePrestationsAccordees());
        pcaReplace.setId(idPca);
        return pcaReplace;
    }

    public static CalculPcaReplace generateForRequerant(String dateDebut, String dateFin, String idPca) {
        CalculPcaReplace pcaReplace = CalculPcaReplaceFactory.generateForRequerant(dateDebut, dateFin, idPca, null);
        return pcaReplace;
    }

    private static CalculPcaReplace generateForRequerant(String dateDebut, String dateFin, String idPca,
            String idVersionDroit) {
        CalculPcaReplace pcaReplace = new CalculPcaReplace();
        pcaReplace.setNoVersion("2");
        pcaReplace.setIdDroit("155");
        pcaReplace.setSimpleInformationsComptabilite(SimpleInformationsComptabiliteFactory.generate());
        pcaReplace.setSimpleInformationsComptabiliteConjoint(SimpleInformationsComptabiliteFactory.generate());
        pcaReplace.setSimplePCAccordee(SimplePCAccordeeFactory.generate(dateDebut, dateFin, idVersionDroit));
        pcaReplace.setSimplePlanDeCalcul(SimplePlanDeCalculFactory.generate());
        pcaReplace.setSimplePrestationsAccordees(SimplePrestationsAccordeesFactory.generateForRequerant(dateDebut,
                dateFin));
        pcaReplace.setSimplePrestationsAccordeesConjoint(new SimplePrestationsAccordees());
        pcaReplace.setId(idPca);
        return pcaReplace;
    }

    public static CalculPcaReplace generateInitial(String dateDebut, String dateFin) {
        CalculPcaReplace pcaReplace = CalculPcaReplaceFactory.generateForRequerant(dateDebut, dateFin, null, null);
        pcaReplace.setNoVersion("1");
        return pcaReplace;
    }

}
