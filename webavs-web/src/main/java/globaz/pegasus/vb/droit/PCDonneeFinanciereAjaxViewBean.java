package globaz.pegasus.vb.droit;

import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public abstract class PCDonneeFinanciereAjaxViewBean extends BJadePersistentObjectViewBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected static String[] splitPart(String part) {
        String[] result = new String[] { "1", "1" };
        // try {
        result = part.split("/");
        if (result.length < 2) {
            result = new String[] { part, "1" };
            if (!JadeNumericUtil.isInteger(part)) {
                result[0] = "1";
            }
            ;
        }
        // } catch (Exception e) {
        // JadeLogger
        // .warn(
        // "Part couldn't be parsed and is set to the default 1/1",
        // e);
        // result = new String[] { "1", "1" };
        // }
        return result;
    }

    private ModificateurDroitDonneeFinanciere droit = null;

    private boolean forceClorePeriode = false;
    protected boolean getListe = false;
    private String idDroitMembreFamille = null;

    public PCDonneeFinanciereAjaxViewBean() {
        super();
    }

    /**
     * @return the droit
     */
    public ModificateurDroitDonneeFinanciere getDroit() {
        return droit;
    }

    /**
     * @return the idDroitMembreFamille
     */
    public String getIdDroitMembreFamille() {
        return idDroitMembreFamille;
    }

    /**
     * Retourne le droit du membre de la famille
     * 
     * @return droitMembreFamille
     * @throws DroitException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    protected DroitMembreFamille getInstanceDroitMembreFamille() throws DroitException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        DroitMembreFamille droitMembreFamille = PegasusServiceLocator.getDroitService().readDroitMembreFamille(
                getIdDroitMembreFamille());
        return droitMembreFamille;
    }

    protected String getNoVersion() {
        return getDroit().getSimpleVersionDroit().getNoVersion();
    }

    public abstract SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader();

    public boolean hasList() {
        return true;
    }

    public boolean isForceClorePeriode() {
        return forceClorePeriode;
    }

    /**
     * @param droit
     *            the droit to set
     */
    public void setDroit(ModificateurDroitDonneeFinanciere droit) {
        this.droit = droit;
    }

    public void setForceClorePeriode(boolean forceClorePeriode) {
        this.forceClorePeriode = forceClorePeriode;
    }

    public void setGetListe(boolean getListe) {
        this.getListe = getListe;
    }

    /**
     * @param idDroitMembreFamille
     *            the idDroitMembreFamille to set
     */
    public void setIdDroitMembreFamille(String idDroitMembreFamille) {
        this.idDroitMembreFamille = idDroitMembreFamille;
    }



    public JadeBusinessMessage[] getMessageInfosTabs(int niveauErreur){
        JadeBusinessMessage[] tabs =  JadeThread.logMessages();
        List<JadeBusinessMessage> listTri = new ArrayList<>();
        if(tabs != null) {
            for(int i= 0 ; i< tabs.length;i++){
                JadeBusinessMessage message =tabs[i];
                if(message.getLevel() == niveauErreur){
                    listTri.add(message);
                }
            }
        }
        JadeBusinessMessage[] tabsTri = new JadeBusinessMessage[listTri.size()];
        for(int i = 0 ;i<listTri.size();i++){
            tabsTri[i] = listTri.get(i);
        }
        return tabsTri;
    }

    public JadeBusinessMessage[] getMessageInfosTabs(){
        JadeBusinessMessage[] tabs =  JadeThread.logMessages();
        return tabs;
    }
    public String getMessages(int niveauErreur){
        StringBuffer stringBuffer = new StringBuffer();
        JadeBusinessMessage[] tabs =  JadeThread.logMessages();
        if(tabs != null) {
            for(int i= 0 ; i< tabs.length;i++){
                JadeBusinessMessage message =tabs[i];
                if(message.getLevel() == niveauErreur){
                    stringBuffer.append(JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(), message.getMessageId(), message.getParameters()));
                    stringBuffer.append("\n");
                }
            }
        }
        return stringBuffer.toString();
    }
    public String getMessages(){
        StringBuffer stringBuffer = new StringBuffer();
        JadeBusinessMessage[] tabs =  JadeThread.logMessages();
        if(tabs != null) {
            for(int i= 0 ; i< tabs.length;i++){
                JadeBusinessMessage message =tabs[i];
                    stringBuffer.append(JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(), message.getMessageId(), message.getParameters()));
                    stringBuffer.append("<br>");
            }
        }
        return stringBuffer.toString();
    }
}