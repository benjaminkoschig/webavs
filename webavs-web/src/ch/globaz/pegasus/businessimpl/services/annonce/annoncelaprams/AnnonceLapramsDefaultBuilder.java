package ch.globaz.pegasus.businessimpl.services.annonce.annoncelaprams;

import globaz.globall.db.BProcess;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Calendar;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.annonce.AnnonceLaprams;
import ch.globaz.pegasus.business.services.annonce.annoncelaprams.AnnonceLapramsBuilder;
import ch.globaz.pegasus.businessimpl.services.lot.AbstractLotBuilder;
import ch.globaz.pegasus.businessimpl.utils.annonce.annoncelaprams.LapramsBuilderFactory;
import ch.globaz.pegasus.businessimpl.utils.annonce.annoncelaprams.builder.ILapramsBuilder;
import ch.globaz.pegasus.businessimpl.utils.annonce.annoncelaprams.model.LapramsDataMediator;

public class AnnonceLapramsDefaultBuilder extends AbstractLotBuilder implements AnnonceLapramsBuilder {

    /**
     * Methode de construction du document Chargement de l'entit�, chargement du catalogue
     * 
     * @param dateRapport
     * 
     * @throws Exception
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DecisionException
     */
    @Override
    public void build(List<AnnonceLaprams> annonces, String mailGest, String dateRapport, BProcess process)
            throws DecisionException, JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {

        // cr�e un m�diateur de donn�es
        LapramsDataMediator mediator = new LapramsDataMediator(annonces, dateRapport);

        // renseigne les informations au m�diateur
        mediator.setNoSerie(getNoSerie(dateRapport));

        // r�cup�re depuis la factory le builder concern�
        ILapramsBuilder builder = LapramsBuilderFactory.getTextBuilder();

        builder.setMailGest(mailGest);

        // g�n�re le(s) document(s)
        builder.build(mediator, process);

    }

    private String getNoSerie(String dateRapport) {
        Calendar cal = JadeDateUtil.getGlobazCalendar(dateRapport);
        return Integer.toString(cal.get(Calendar.YEAR)) + String.format("%02d", cal.get(Calendar.WEEK_OF_YEAR));
    }
}
