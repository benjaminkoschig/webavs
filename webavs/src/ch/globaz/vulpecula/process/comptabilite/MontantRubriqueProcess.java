package ch.globaz.vulpecula.process.comptabilite;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Date;

public class MontantRubriqueProcess extends BProcess {
    private static final long serialVersionUID = -2788022038569808403L;
    private String dateSaisie = "";
    private Date date;

    private List<MontantRubriqueEntity> entities;

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        date = new Date(dateSaisie);
        try {
            retrieve();
            print();
        } catch (IllegalStateException ex) {
            getTransaction().addErrors(ex.getMessage());
        }

        return true;
    }

    private void print() throws IOException {
        MontantRubriqueExcel excel = new MontantRubriqueExcel(getSession(),
                DocumentConstants.LISTES_MONTANT_RUBRIQUE_FILENAME, DocumentConstants.LISTES_MONTANT_RUBRIQUE_TITLE);

        excel.setEntities(entities);
        excel.setDate(date);
        excel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), excel.getOutputFile());
    }

    private void retrieve() throws Exception {
        MontantRubriqueManager manager = new MontantRubriqueManager();
        manager.setSession(getSession());
        manager.setDate(new Date());
        manager.find();

        entities = new ArrayList<MontantRubriqueEntity>();
        for (Iterator iterator = manager.iterator(); iterator.hasNext();) {
            entities.add((MontantRubriqueEntity) iterator.next());
        }
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LISTES_MONTANT_RUBRIQUE_TITLE;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * @return the dateSaisie
     */
    public String getDateSaisie() {
        return dateSaisie;
    }

    /**
     * @param dateSaisie the dateSaisie to set
     */
    public void setDateSaisie(String dateSaisie) {
        this.dateSaisie = dateSaisie;
    }

}
