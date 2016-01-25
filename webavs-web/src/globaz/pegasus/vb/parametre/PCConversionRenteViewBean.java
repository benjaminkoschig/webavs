package globaz.pegasus.vb.parametre;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.util.JACalendar;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.conversionRente.ConversionRenteException;
import ch.globaz.pegasus.business.models.parametre.ConversionRente;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCConversionRenteViewBean extends BJadePersistentObjectViewBean {

    private String annee;
    private ConversionRente conversionRente;

    public PCConversionRenteViewBean() {
        super();
        conversionRente = new ConversionRente();
    }

    /**
     * @param conversionRente
     */
    public PCConversionRenteViewBean(ConversionRente conversionRente) {
        super();
        this.conversionRente = conversionRente;
    }

    @Override
    public void add() throws ConversionRenteException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        filDateDebut();
        conversionRente = PegasusServiceLocator.getConversionRenteService().create(conversionRente);
    }

    /**
     * @throws ConversionRenteException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void delete() throws ConversionRenteException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        conversionRente = PegasusServiceLocator.getConversionRenteService().delete(conversionRente);
    }

    /**
     * @throws ConversionRenteException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */

    private void filDateDebut() {
        conversionRente.getSimpleConversionRente().setDateDebut("01." + annee);
    }

    /**
     * @return the annee
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * @return ConversionRente
     */
    public ConversionRente getConversionRente() {
        return conversionRente;
    }

    @Override
    public String getId() {
        return conversionRente.getId();
    }

    /**
     * Retourne l'objet session
     * 
     * @return objet BSession
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return (conversionRente != null) && !conversionRente.isNew() ? new BSpy(conversionRente.getSpy()) : new BSpy(
                getSession());
    }

    /**
     * @throws Exception
     */
    @Override
    public void retrieve() throws Exception {
        conversionRente = PegasusServiceLocator.getConversionRenteService().read(conversionRente.getId());
        annee = JACalendar.format(conversionRente.getSimpleConversionRente().getDateDebut(), JACalendar.FORMAT_MMsYYYY);
        /*
         * this.annee = JADate.get (this.conversionRente.getSimpleConversionRente().getDateDebut()).toString() + "." +
         * JADate.getYear(this.conversionRente.getSimpleConversionRente().getDateDebut()).toString();
         */
    }

    /**
     * @param annee
     *            the annee to set
     */
    public void setAnnee(String annee) {
        this.annee = annee;
    }

    /**
     * @param ConversionRente
     *            the ConversionRente to set
     */
    public void setConversionRente(ConversionRente conversionRente) {
        this.conversionRente = conversionRente;

    }

    /**
     * @param idConversionRente
     */
    @Override
    public void setId(String id) {
        conversionRente.setId(id);
    }

    /**
     * @throws ConversionRenteException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void update() throws ConversionRenteException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        filDateDebut();
        conversionRente = PegasusServiceLocator.getConversionRenteService().update(conversionRente);
    }

}