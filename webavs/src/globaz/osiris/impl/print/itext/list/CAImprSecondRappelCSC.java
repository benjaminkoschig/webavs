package globaz.osiris.impl.print.itext.list;

import globaz.framework.printing.itext.fill.FWIImportManager;
import globaz.globall.api.BIDocument;
import globaz.osiris.application.CAApplication;
import java.util.HashMap;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * @author Sylvain Crelier
 * @version 1.0.0
 * 
 *          Préparer les données à remplir pour impression du premier rappel
 */
public class CAImprSecondRappelCSC extends CAImprPremierRappelCSC {

    private static final String DISPOSITIONS_LEGALES_TEMPLATE = "CADispositionsLegalesContentieux";

    /**
     * Commentaire relatif au constructeur AJPremierRappel.
     */
    public CAImprSecondRappelCSC() {
        super();
    }

    /**
     * @return
     */
    @Override
    public JasperPrint getDispositionsLegales(CAApplication application, String codeIsoLangue, String name)
            throws Exception {
        HashMap parameters = new HashMap();
        parameters.put("P_PRENOM_NOM", name);
        parameters.put("P_PAGE_LABEL", application.getLabel("IMPR_DISPOSITIONS_LEGALES_PAGE", codeIsoLangue) + " 2");

        parameters.put("P_TITLE", application.getLabel("IMPR_DISPOSITIONS_LEGALES_TITLE", codeIsoLangue));
        parameters.put("P_ARTICLE2", application.getLabel("IMPR_DISPOSITIONS_LEGALES_ARTICLE2", codeIsoLangue));
        parameters.put("P_ARTICLE2_CONTENT",
                application.getLabel("IMPR_DISPOSITIONS_LEGALES_ARTICLE2_CONTENT", codeIsoLangue));
        parameters.put("P_ARTICLE13", application.getLabel("IMPR_DISPOSITIONS_LEGALES_ARTICLE13", codeIsoLangue));
        parameters.put("P_ORDONNANCE_1", application.getLabel("IMPR_DISPOSITIONS_LEGALES_ORDONNANCE_1", codeIsoLangue));
        parameters.put("P_ORDONNANCE_2", application.getLabel("IMPR_DISPOSITIONS_LEGALES_ORDONNANCE_2", codeIsoLangue));
        parameters.put("P_ORDONNANCE_3", application.getLabel("IMPR_DISPOSITIONS_LEGALES_ORDONNANCE_3", codeIsoLangue));
        parameters.put("P_INDICATIONS_COMPL",
                application.getLabel("IMPR_DISPOSITIONS_LEGALES_INDICATIONS_COMPL", codeIsoLangue));
        parameters.put("P_INDICATIONS_COMPL_CONTENT_1",
                application.getLabel("IMPR_DISPOSITIONS_LEGALES_INDICATIONS_COMPL_CONTENT_1", codeIsoLangue));
        parameters.put("P_INDICATIONS_COMPL_CONTENT_2",
                application.getLabel("IMPR_DISPOSITIONS_LEGALES_INDICATIONS_COMPL_CONTENT_2", codeIsoLangue));

        FWIImportManager importManager = new FWIImportManager();
        importManager.setImportPath(CAApplication.DEFAULT_OSIRIS_ROOT);
        importManager.setParametre(parameters);
        importManager.setDocumentTemplate(CAImprSecondRappelCSC.DISPOSITIONS_LEGALES_TEMPLATE);

        importManager.createDocument();

        return importManager.getDocument();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.07.2002 18:14:45)
     * 
     * @return globaz.framework.printing.FWDocument
     */
    @Override
    public BIDocument getDocumentClass() throws Exception {
        return new CAProcessImprSecondRappelCSC();
    }

    /**
     * Returns the montant.
     * 
     * @return String
     */
    @Override
    public String getMontant() {
        return "";
    }

}
