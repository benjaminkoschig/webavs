package ch.globaz.pegasus.business.models.externalmodule;

import globaz.jade.persistence.model.JadeSimpleModel;
import com.google.gson.GsonBuilder;

/**
 * Modèle java de la table de soumission des job externes du module PC
 * 
 * @author sce
 * 
 */
public class SimpleExternalModule<E> extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private ExternalJobEtat etat = null;
    private ExternalJobActionSource sourceAction = null;
    private String parameters = null;
    private String idJob = null;

    @Override
    public String getId() {
        return idJob;
    }

    public ExternalJobEtat getEtat() {
        return etat;
    }

    public void setEtatJob(ExternalJobEtat etat) {
        this.etat = etat;
    }

    public ExternalJobActionSource getSourceAction() {
        return sourceAction;
    }

    public void setSourceAction(ExternalJobActionSource sourceAction) {
        this.sourceAction = sourceAction;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getIdJob() {
        return idJob;
    }

    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }

    @Override
    public void setId(String id) {
        idJob = id;
    }

    public void setParameterAsJson(E object) {
        GsonBuilder builder = new GsonBuilder();
        this.setParameters(builder.create().toJson(object));
    }

    @SuppressWarnings({ "unchecked", "hiding" })
    public <E> E getParameterAsObject() {

        GsonBuilder builder = new GsonBuilder();
        return (E) (builder.create().fromJson(this.getParameters(), sourceAction.parameterClass));

    }

}
