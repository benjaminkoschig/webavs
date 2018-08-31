package ch.globaz.vulpecula.documents.ctrlemployeur;

import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.controleemployeur.ControleEmployeur;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.external.BProcessWithContext;

public class ListeControleEmployeurParAnneePrinter extends BProcessWithContext {
    private static final long serialVersionUID = 9192905802121978811L;
    private String annee;

    public ListeControleEmployeurParAnneePrinter() {
        super();
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();

        String dateDebut = getAnnee() + "0101";
        String dateFin = getAnnee() + "1231";
        Periode periode = new Periode(dateDebut, dateFin);

        List<ControleEmployeur> controlesEmployeursDansLaPeriode = VulpeculaRepositoryLocator
                .getControleEmployeurRepository().findControleEmployeurInPeriode(periode);

        Map<String, List<ControleEmployeur>> mapControlesByConvention = new TreeMap<String, List<ControleEmployeur>>();
        for (ControleEmployeur controleEmployeur : controlesEmployeursDansLaPeriode) {
            Convention convention = controleEmployeur.getEmployeur().getConvention();
            String key = convention.getCode() + " - " + convention.getDesignation();
            if (!mapControlesByConvention.containsKey(key)) {
                mapControlesByConvention.put(key, new ArrayList<ControleEmployeur>());
            }

            List<ControleEmployeur> listeControles = mapControlesByConvention.get(key);
            listeControles.add(controleEmployeur);
        }
        RecapControleEmployeur recap = new RecapControleEmployeur();

        ListeControleEmployeurParAnneeExcel doc = new ListeControleEmployeurParAnneeExcel(getSession(),
                mapControlesByConvention, getAnnee());

        doc.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), doc.getOutputFile());

        return true;
    }

    @Override
    protected String getEMailObject() {
        return (DocumentConstants.LISTES_CONTROLES_EMPLOYEURS_PAR_ANNEE_NAME);
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

}
