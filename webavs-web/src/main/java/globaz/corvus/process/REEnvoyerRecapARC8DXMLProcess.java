/*
 * @author jje
 */
package globaz.corvus.process;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;
import ch.admin.zas.pool.PoolMeldungZurZAS;
import ch.admin.zas.rc.MonatsRekapitulationRentenType;
import ch.globaz.common.exceptions.ValidationException;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import globaz.corvus.annonce.service.REAnnonceARC8DXmlService;
import globaz.corvus.annonce.service.REEnvoyerRecapARC8DXMLService;
import globaz.corvus.api.recap.IRERecapMensuelle;
import globaz.corvus.db.recap.access.RERecapElement;
import globaz.corvus.db.recap.access.RERecapMensuelle;
import globaz.corvus.db.recap.access.RERecapMensuelleManager;
import globaz.corvus.properties.REProperties;
import globaz.corvus.vb.recap.REDetailRecapMensuelleViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.common.JadeException;
import globaz.jade.fs.JadeFsFacade;
import globaz.prestation.tools.PRDateFormater;

/**
 *
 * @author ebko
 */
public class REEnvoyerRecapARC8DXMLProcess extends BProcess {

    private static final long serialVersionUID = 1L;

    private REDetailRecapMensuelleViewBean reDetRecMenViewBean = null;
    RERecapMensuelle reRecapMensuelle;

    public REEnvoyerRecapARC8DXMLProcess() {
        super();
    }

    public REEnvoyerRecapARC8DXMLProcess(BProcess parent) {
        super(parent);
    }

    public REEnvoyerRecapARC8DXMLProcess(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        validate();

        try {
            PoolMeldungZurZAS.Lot lotAnnonces = REEnvoyerRecapARC8DXMLService.getInstance().initPoolMeldungZurZASLot(
                    REProperties.RECAP_CENTRALE_TEST.getBooleanValue(), CommonProperties.KEY_NO_CAISSE.getValue());

            prepareEnvoieAnnonce(reDetRecMenViewBean, lotAnnonces);
            envoieRecap(lotAnnonces);

            // Modification de l'état de la récap

            reRecapMensuelle = new RERecapMensuelle();
            reRecapMensuelle.setSession(getSession());
            reRecapMensuelle.setIdRecapMensuelle(reDetRecMenViewBean.getIdRecapMensuelle());
            reRecapMensuelle.retrieve();

            reRecapMensuelle.setCsEtat(IRERecapMensuelle.CS_ETAT_ENVOYE);
            reRecapMensuelle.update(getTransaction());

            // Report du montant total sur champ "recap -1 mois" du prochain
            // mois

            HashMap<String, String> mntRecapTotauxElems = new HashMap<>();
            mntRecapTotauxElems.put("to2_500", reDetRecMenViewBean.getTo2_500());
            mntRecapTotauxElems.put("to2_501", reDetRecMenViewBean.getTo2_501());
            mntRecapTotauxElems.put("to2_503", reDetRecMenViewBean.getTo2_503());
            mntRecapTotauxElems.put("to2_510", reDetRecMenViewBean.getTo2_510());
            mntRecapTotauxElems.put("to2_511", reDetRecMenViewBean.getTo2_511());
            mntRecapTotauxElems.put("to2_513", reDetRecMenViewBean.getTo2_513());

            JADate dateMoisSuivant = new JADate(reRecapMensuelle.getDateRapportMensuel());
            JACalendar cal = new JACalendarGregorian();
            dateMoisSuivant = cal.addMonths(dateMoisSuivant, 1);

            RERecapMensuelleManager reRecapMensuelleMgrMoisSuivant = new RERecapMensuelleManager();
            reRecapMensuelleMgrMoisSuivant.setSession(getSession());
            reRecapMensuelleMgrMoisSuivant.setForDateRapportMensuel(
                    PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dateMoisSuivant.toStrAMJ()));
            reRecapMensuelleMgrMoisSuivant.find(BManager.SIZE_USEDEFAULT);

            Object[] noElements = mntRecapTotauxElems.keySet().toArray();

            for (Object orecapMens : reRecapMensuelleMgrMoisSuivant.toList()) {
                RERecapMensuelle recapMens = (RERecapMensuelle) orecapMens;

                if (IRERecapMensuelle.CS_ETAT_ATTENTE.equals(recapMens.getCsEtat())) {

                    for (int i = 0; i < noElements.length; i++) {

                        RERecapElement recapElemMoisSuivant = new RERecapElement();
                        recapElemMoisSuivant.setSession(getSession());
                        recapElemMoisSuivant.setIdRecapMensuelle(recapMens.getIdRecapMensuelle());
                        recapElemMoisSuivant.setCodeRecap(((String) noElements[i]).substring(4, 7) + "001");
                        recapElemMoisSuivant.retrieve();

                        recapElemMoisSuivant.setMontant(mntRecapTotauxElems.get(noElements[i]));

                        if (recapElemMoisSuivant.isNew()) {
                            recapElemMoisSuivant.save();
                        } else {
                            recapElemMoisSuivant.update();
                        }

                    }

                }
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());
        }

        return true;
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate() throws Exception {
        if (getParent() == null) {
            if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
                setSendCompletionMail(false);
                setSendMailOnError(false);
            } else {
                setSendCompletionMail(true);
                setSendMailOnError(true);
            }

            setControleTransaction(getTransaction() == null);
        }

        if (getSession().hasErrors()) {
            abort();
        }
    }

    void prepareEnvoieAnnonce(REDetailRecapMensuelleViewBean recap, PoolMeldungZurZAS.Lot poolMeldungLot)
            throws ValidationException, PropertiesException, JadeException {

        String noCaisse = CommonProperties.KEY_NO_CAISSE.getValue();
        String noAgence = CommonProperties.NUMERO_AGENCE.getValue();
        try {

            MonatsRekapitulationRentenType annonceXml = REAnnonceARC8DXmlService.getInstance().getAnnonceXml(recap,
                    noCaisse + noAgence);
            REEnvoyerRecapARC8DXMLService.getInstance().validateUnitMessage(annonceXml);
            poolMeldungLot
                    .getVAIKMeldungNeuerVersicherterOrVAIKMeldungAenderungVersichertenDatenOrVAIKMeldungVerkettungVersichertenNr()
                    .add(annonceXml);
        } catch (ValidationException e) {
            e.getMessageErreurDeValidation().add(0, recap.getId() + " - " + recap.getIdRecapMensuelle() + " : ");
            throw e;
        } catch (NumberFormatException e) {
            throw new JadeException(recap.getId() + " - " + recap.getIdRecapMensuelle() + " : "
                    + getSession().getLabel("ERREUR_CHAMP_VALEUR"));
        } catch (Exception e) {
            throw new JadeException(recap.getId() + " - " + recap.getIdRecapMensuelle() + " : " + e.getMessage());
        }
    }

    private void envoieRecap(PoolMeldungZurZAS.Lot lotAnnonces)
            throws JadeException, PropertiesException, IOException, SAXException, JAXBException {
        if (lotAnnonces
                .getVAIKMeldungNeuerVersicherterOrVAIKMeldungAenderungVersichertenDatenOrVAIKMeldungVerkettungVersichertenNr()
                .isEmpty()) {
            throw new JadeException(getSession().getLabel("PROCESS_ENVOI_ANNONCES_ERREUR_AUCUNE_ANNONCE"));
        }

        String fileName = REEnvoyerRecapARC8DXMLService.getInstance().genereFichier(lotAnnonces);
        JadeFsFacade.copyFile(fileName,
                REProperties.RECAP_FTP_CENTRALE_PATH.getValue() + "/" + new File(fileName).getName());
    }

    /**
     * getter pour l'attribut EMail object
     *
     * @return la valeur courante de l'attribut EMail object
     */
    @Override
    protected String getEMailObject() {
        reRecapMensuelle = new RERecapMensuelle();
        reRecapMensuelle.setSession(getSession());
        reRecapMensuelle.setIdRecapMensuelle(reDetRecMenViewBean.getIdRecapMensuelle());
        try {
            reRecapMensuelle.retrieve();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());
        }
        if (getMemoryLog().isOnErrorLevel() || getMemoryLog().isOnFatalLevel()) {
            return getSession().getLabel("PROCESS_ENVOI_RECAP_FAILED") + " - "
                    + reRecapMensuelle.getDateRapportMensuel();
        } else {
            return getSession().getLabel("PROCESS_ENVOI_RECAP_SUCCESS") + " - "
                    + reRecapMensuelle.getDateRapportMensuel();
        }
    }

    public REDetailRecapMensuelleViewBean getReDetRecMenViewBean() {
        return reDetRecMenViewBean;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public void setReDetRecMenViewBean(REDetailRecapMensuelleViewBean reDetRecMenViewBean) {
        this.reDetRecMenViewBean = reDetRecMenViewBean;
    }

}
