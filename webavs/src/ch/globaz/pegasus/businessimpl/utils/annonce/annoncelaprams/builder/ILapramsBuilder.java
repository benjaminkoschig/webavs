package ch.globaz.pegasus.businessimpl.utils.annonce.annoncelaprams.builder;

import globaz.globall.db.BProcess;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.pegasus.businessimpl.utils.annonce.annoncelaprams.model.LapramsDataMediator;

public interface ILapramsBuilder {

    public abstract void build(LapramsDataMediator dataMediator, BProcess process) throws AnnonceException,
            JadePersistenceException;

    public abstract void setMailGest(String mailGest);

}
