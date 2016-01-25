package globaz.musca.process.gestionJourFerie;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.gestionJourFerie.FAGestionJourFerie;
import java.util.ArrayList;

public class FAPeriodeWeekendProcess extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Attributs
    private String dateDebut = "";
    private String dateFin = "";
    private ArrayList<String> domaineFerie = new ArrayList<String>();
    private String libelle = "";

    // Constructeur
    public FAPeriodeWeekendProcess() {
        super();
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        try {
            JACalendarGregorian calendrier = new JACalendarGregorian();
            JADate dateJourATraiter = new JADate(getDateDebut());
            long nbJourATraiter = calendrier.daysBetween(getDateDebut(), getDateFin()) + 1;

            FAGestionJourFerie entityJourFerie = null;
            for (int i = 1; i <= nbJourATraiter; i++) {
                entityJourFerie = new FAGestionJourFerie();
                entityJourFerie.setSession(getSession());
                entityJourFerie.setAlternateKey(FAGestionJourFerie.AK_DATE_JOUR);
                entityJourFerie.setDateJour(dateJourATraiter.toStr("."));
                entityJourFerie.retrieve(getTransaction());

                if (entityJourFerie.isNew()) {
                    entityJourFerie.setLibelle(getLibelle());
                    entityJourFerie.setDomaineFerie(getDomaineFerie());
                    entityJourFerie.add(getTransaction());
                } else {
                    if (!JadeStringUtil.isBlankOrZero(entityJourFerie.getLibelle())
                            && !JadeStringUtil.isBlankOrZero(getLibelle())) {
                        entityJourFerie.setLibelle(entityJourFerie.getLibelle() + " / " + getLibelle());
                    } else if (!JadeStringUtil.isBlankOrZero(getLibelle())) {
                        entityJourFerie.setLibelle(getLibelle());
                    }

                    for (String domaine : getDomaineFerie()) {
                        if (!entityJourFerie.getDomaineFerie().contains(domaine)) {
                            entityJourFerie.getDomaineFerie().add(domaine);
                        }
                    }
                    entityJourFerie.update(getTransaction());
                }
                dateJourATraiter = calendrier.addDays(dateJourATraiter, 1);
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.FATAL, this.getClass().getName());
        }

        if (isOnError()) {
            setSendCompletionMail(true);
            return false;
        }

        return true;
    }

    // Méthode
    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {

        setControleTransaction(true);
        setSendCompletionMail(false);
        setSendMailOnError(true);

        if (JadeStringUtil.isBlankOrZero(getDateDebut())) {
            getSession().addError(getSession().getLabel("PERIODE_WEEKEND_DATE_DEBUT_OBLIGATOIRE"));
            return;
        }

        if (JadeStringUtil.isBlankOrZero(getDateFin())) {
            getSession().addError(getSession().getLabel("PERIODE_WEEKEND_DATE_FIN_OBLIGATOIRE"));
            return;
        }

        JACalendarGregorian calendrier = new JACalendarGregorian();
        if (JACalendar.COMPARE_FIRSTUPPER == calendrier.compare(getDateDebut(), getDateFin())) {
            getSession().addError(getSession().getLabel("PERIODE_WEEKEND_ERREUR_DATE_FIN_LOWER_DATE_DEBUT"));
            return;
        }

        /**
         * sécurité supplémentaire ------------------------ mais on ne devrait pas se retrouver avec une adresse email
         * vide en effet, si l'email n'est pas renseigné getEMailAddress() prend l'email du parent ou à défaut, celui du
         * user connecté
         * 
         */
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            getSession().addError(getSession().getLabel("EMAIL_OBLIGATOIRE"));
            setSendCompletionMail(false);
            setSendMailOnError(false);
            return;
        }
    }

    /**
     * getter
     */
    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public ArrayList<String> getDomaineFerie() {
        return domaineFerie;
    }

    @Override
    protected String getEMailObject() {

        /**
         * On envoie un mail uniquement en cas d'erreur Ceci est paramétré dans la méthode _validate()
         */
        return getSession().getLabel("PERIODE_WEEKEND_ERREUR_TRAITEMENT");
    }

    public String getLibelle() {
        return libelle;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * setter
     */
    public void setDateDebut(String newDateDebut) {
        dateDebut = newDateDebut;
    }

    public void setDateFin(String newDateFin) {
        dateFin = newDateFin;
    }

    public void setDomaineFerie(ArrayList<String> newDomaineFerie) {
        domaineFerie = newDomaineFerie;
    }

    public void setLibelle(String newLibelle) {
        libelle = newLibelle;
    }

}
