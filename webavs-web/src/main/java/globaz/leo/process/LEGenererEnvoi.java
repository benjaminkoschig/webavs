/*
 * Créé le 29 mars 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.leo.db.data.LEEnvoiDataSource;
import globaz.leo.db.data.LEParamEnvoiDataSource;
import globaz.leo.process.handler.LEGenererEtapeHandler;
import globaz.leo.process.handler.LEParamEnvoiHandler;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author ald
 * 
 *         Processus permettant à une application AVS d'envoyer un document. Il va donc ajouter un nouveau document dans
 *         la gestion de l'envoi Puis il va journaliser cet envoi et ensuite il va appeler la classe iText permettant de
 *         générer le document.
 */
public class LEGenererEnvoi extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csDocument;
    private String dateCreation;
    private String dateRappel;

    private LEEnvoiDataSource envoiDS;

    private Boolean isGenerateEtapeSuivante;
    private HashMap<?, ?> paramEnvoiList;

    public LEGenererEnvoi() {
        super();
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            LEParamEnvoiDataSource paramEnvoiDS;
            if (getParamEnvoiList() == null) {
                throw new Exception("Tentative d'utilisation de LEGenererEnvoi sans paramètre d''envoi!");
            } else {
                paramEnvoiDS = mapToDS();
            }
            if (JadeStringUtil.isEmpty(getCsDocument())) {
                throw new Exception("Tentative d'utilisation de LEGenererEnvoi sans définir le type d''envoi!");
            }
            // setSendCompletionMail(false);
            LEParamEnvoiHandler pHandler = new LEParamEnvoiHandler();
            LEGenererEtapeHandler genererEtape = new LEGenererEtapeHandler();
            // on charge les données relatives au csDocument dans le paramètrage
            // de l'envoi
            if (JadeStringUtil.isEmpty(getDateCreation())) {
                envoiDS = pHandler.loadParametreEnvoi(getCsDocument(), paramEnvoiDS, getSession(), getTransaction());
            } else {
                envoiDS = pHandler.loadParametreEnvoi(getCsDocument(), paramEnvoiDS, getSession(), getTransaction(),
                        dateRappel, dateCreation);
            }
            // modif DGI, écraser date rappel si spécifiée
            if (!JadeStringUtil.isEmpty(dateRappel)) {
                envoiDS.put(LEEnvoiDataSource.DATE_RAPPEL, dateRappel);
            }
            String idEnvoi;
            if (JadeStringUtil.isEmpty(getDateCreation())) {
                idEnvoi = genererEtape.genererEtape(envoiDS, getSession(), this);
            } else {
                idEnvoi = genererEtape.genererEtape(envoiDS, getSession(), this, "", getDateCreation());
            }
            if (getGenerateEtapeSuivante().booleanValue()) {
                if (JadeStringUtil.isEmpty(getDateCreation())) {
                    genererEtape.genererEtapeSuivante(idEnvoi, getSession(), getTransaction(), this, dateRappel, "");
                } else {
                    genererEtape.genererEtapeSuivante(idEnvoi, getSession(), getTransaction(), this, dateRappel,
                            getDateCreation());
                }
            }
            return true;

        } catch (Exception e) {
            JadeLogger.error(this, e);
            this._addError(getTransaction(), e.getMessage());
            return false;
        }
    }

    /**
     * @return
     */
    public String getCsDocument() {
        return csDocument;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public String getDateRappel() {
        return dateRappel;
    }

    @Override
    protected String getEMailObject() {
        return null;
    }

    /**
     * @return
     */
    public LEEnvoiDataSource getEnvoiDS() {
        return envoiDS;
    }

    /**
     * @return
     */
    public Boolean getGenerateEtapeSuivante() {
        return isGenerateEtapeSuivante;
    }

    /**
     * @return
     */
    public HashMap<?, ?> getParamEnvoiList() {
        return paramEnvoiList;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
	 * 
	 */
    private LEParamEnvoiDataSource mapToDS() {
        LEParamEnvoiDataSource paramEnvoiDS = new LEParamEnvoiDataSource();
        Iterator<?> it = paramEnvoiList.keySet().iterator();
        String paramKey;
        String value;
        while (it.hasNext()) {
            paramKey = (String) it.next();
            value = (String) paramEnvoiList.get(paramKey);
            paramEnvoiDS.addParamEnvoi(paramKey, value);
        }
        return paramEnvoiDS;
    }

    /**
     * @param string
     */
    public void setCsDocument(String string) {
        csDocument = string;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setDateRappel(String dateRappel) {
        this.dateRappel = dateRappel;
    }

    /**
     * @param source
     */
    public void setEnvoiDS(LEEnvoiDataSource source) {
        envoiDS = source;
    }

    /**
     * @param b
     */
    public void setGenerateEtapeSuivante(Boolean b) {
        isGenerateEtapeSuivante = b;
    }

    /**
     * @param list
     */
    public void setParamEnvoiList(HashMap<?, ?> list) {
        paramEnvoiList = list;
    }

}
