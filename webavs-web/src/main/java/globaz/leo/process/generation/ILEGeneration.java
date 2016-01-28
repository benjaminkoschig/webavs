/*
 * Créé le 1 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.process.generation;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.leo.db.data.LEEnvoiDataSource;
import java.util.List;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public interface ILEGeneration {
    public void beforeBuildReport() throws FWIException;

    public void beforeExecuteReport() throws FWIException;

    public void createDataSource() throws Exception;

    public void executeProcess() throws Exception;

    public List getAttachedDocuments();

    public JadePublishDocumentInfo getDocumentInfo();

    public List getDocumentList();

    public String getDocumentTitle();

    public FWIImportManager getImporter();

    public LEEnvoiDataSource getResult();

    public boolean isAborted();

    public boolean next() throws FWIException;

    public void setDateImpression(String date);

    public void setDocumentDataSource(LEEnvoiDataSource source);

    public void setEMailAddress(String emailAdresse);

    public void setNomDoc(String nomDoc);

    public void setParent(BProcess parent);

    public void setPublishDocument(boolean value);

    public void setSessionModule(BSession session) throws Exception;

    public void start();
}
