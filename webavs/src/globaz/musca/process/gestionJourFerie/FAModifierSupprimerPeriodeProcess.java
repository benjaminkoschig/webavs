package globaz.musca.process.gestionJourFerie;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.gestionJourFerie.FAGestionJourFerie;
import globaz.musca.db.gestionJourFerie.FAModifierSupprimerPeriodeViewBean;
import java.util.ArrayList;

public class FAModifierSupprimerPeriodeProcess extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ArrayList<String> domaineFerie = new ArrayList<String>();
    private String libelle = "";
    // Attributs
    private ArrayList<FAGestionJourFerie> listFerieAModifierSupprimer = new ArrayList<FAGestionJourFerie>();
    private String operationARealiser = "";

    // Constructeur
    public FAModifierSupprimerPeriodeProcess() {
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
            if (FAModifierSupprimerPeriodeViewBean.OPERATION_MODIFIER.equalsIgnoreCase(operationARealiser)) {
                for (FAGestionJourFerie entityJourFerie : listFerieAModifierSupprimer) {
                    entityJourFerie.retrieve(getTransaction());
                    entityJourFerie.setLibelle(getLibelle());
                    entityJourFerie.setDomaineFerie(getDomaineFerie());
                    entityJourFerie.update(getTransaction());
                }
            } else if (FAModifierSupprimerPeriodeViewBean.OPERATION_SUPPRIMER.equalsIgnoreCase(operationARealiser)) {
                for (FAGestionJourFerie entityJourFerie : listFerieAModifierSupprimer) {
                    entityJourFerie.retrieve(getTransaction());
                    entityJourFerie.delete(getTransaction());
                }
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

        if (listFerieAModifierSupprimer.size() <= 0) {
            getSession().addError(getSession().getLabel("MODIFIER_SUPPRIMER_PERIODE_LISTE_FERIE_VIDE"));
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

    public ArrayList<String> getDomaineFerie() {
        return domaineFerie;
    }

    @Override
    protected String getEMailObject() {

        /**
         * On envoie un mail uniquement en cas d'erreur Ceci est paramétré dans la méthode _validate()
         */
        return getSession().getLabel("MODIFIER_SUPPRIMER_PERIODE_ERREUR_TRAITEMENT");
    }

    public String getLibelle() {
        return libelle;
    }

    /**
     * getter
     */

    public ArrayList<FAGestionJourFerie> getListFerieAModifierSupprimer() {
        return listFerieAModifierSupprimer;
    }

    public String getOperationARealiser() {
        return operationARealiser;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setDomaineFerie(ArrayList<String> newDomaineFerie) {
        domaineFerie = newDomaineFerie;
    }

    public void setLibelle(String newLibelle) {
        libelle = newLibelle;
    }

    /**
     * setter
     */

    public void setListFerieAModifierSupprimer(ArrayList<FAGestionJourFerie> newListFerieAModifierSupprimer) {
        listFerieAModifierSupprimer = newListFerieAModifierSupprimer;
    }

    public void setOperationARealiser(String newOperationARealiser) {
        operationARealiser = newOperationARealiser;
    }

}
