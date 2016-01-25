/*
 * @author jje
 */
package globaz.corvus.process;

import globaz.corvus.api.recap.IRERecapMensuelle;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.recap.access.RERecapElement;
import globaz.corvus.db.recap.access.RERecapMensuelle;
import globaz.corvus.db.recap.access.RERecapMensuelleManager;
import globaz.corvus.vb.recap.REDetailRecapMensuelleViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEInputAnnonceLight;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRSession;
import globaz.webavs.common.CommonProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author JJE
 */
public class REEnvoyerRecapARC8DProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String APPLICATION_RECAP = "HERMES";
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    private static final String CODE_APPLICATION = "8D";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private Map attributs = null;
    private String noAgence = "";
    private String noCaisse = "";

    private List recapAEnvoyer = new ArrayList();
    private REDetailRecapMensuelleViewBean reDetRecMenViewBean = null;

    public REEnvoyerRecapARC8DProcess() {
        super();
    }

    public REEnvoyerRecapARC8DProcess(BProcess parent) {
        super(parent);
    }

    public REEnvoyerRecapARC8DProcess(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        validate();

        try {

            noCaisse = getSession().getApplication().getProperty(CommonProperties.KEY_NO_CAISSE);
            noAgence = getSession().getApplication().getProperty(CommonProperties.KEY_NO_AGENCE);

            preparerEnvoieRecap(reDetRecMenViewBean.getDateRapport(), reDetRecMenViewBean.getElem500001(),
                    reDetRecMenViewBean.getElem500002(), reDetRecMenViewBean.getElem500003(),
                    reDetRecMenViewBean.getElem500004(), reDetRecMenViewBean.getElem500005(), "001");

            preparerEnvoieRecap(reDetRecMenViewBean.getDateRapport(), reDetRecMenViewBean.getElem500006(),
                    reDetRecMenViewBean.getElem500007(), reDetRecMenViewBean.getElem500099(),
                    reDetRecMenViewBean.getElem501001(), reDetRecMenViewBean.getElem501002(), "002");

            preparerEnvoieRecap(reDetRecMenViewBean.getDateRapport(), reDetRecMenViewBean.getElem501003(),
                    reDetRecMenViewBean.getElem501004(), reDetRecMenViewBean.getElem501005(),
                    reDetRecMenViewBean.getElem501006(), reDetRecMenViewBean.getElem501007(), "003");

            preparerEnvoieRecap(reDetRecMenViewBean.getDateRapport(), reDetRecMenViewBean.getElem501099(),
                    reDetRecMenViewBean.getElem503001(), reDetRecMenViewBean.getElem503002(),
                    reDetRecMenViewBean.getElem503003(), reDetRecMenViewBean.getElem503004(), "004");

            preparerEnvoieRecap(reDetRecMenViewBean.getDateRapport(), reDetRecMenViewBean.getElem503005(),
                    reDetRecMenViewBean.getElem503007(), reDetRecMenViewBean.getElem503099(),
                    reDetRecMenViewBean.getElem510001(), reDetRecMenViewBean.getElem510002(), "005");

            preparerEnvoieRecap(reDetRecMenViewBean.getDateRapport(), reDetRecMenViewBean.getElem510003(),
                    reDetRecMenViewBean.getElem510004(), reDetRecMenViewBean.getElem510005(),
                    reDetRecMenViewBean.getElem510007(), reDetRecMenViewBean.getElem510099(), "006");

            preparerEnvoieRecap(reDetRecMenViewBean.getDateRapport(), reDetRecMenViewBean.getElem511001(),
                    reDetRecMenViewBean.getElem511002(), reDetRecMenViewBean.getElem511003(),
                    reDetRecMenViewBean.getElem511004(), reDetRecMenViewBean.getElem511005(), "007");

            preparerEnvoieRecap(reDetRecMenViewBean.getDateRapport(), reDetRecMenViewBean.getElem511007(),
                    reDetRecMenViewBean.getElem511099(), reDetRecMenViewBean.getElem513001(),
                    reDetRecMenViewBean.getElem513002(), reDetRecMenViewBean.getElem513003(), "008");

            preparerEnvoieRecap(reDetRecMenViewBean.getDateRapport(), reDetRecMenViewBean.getElem513004(),
                    reDetRecMenViewBean.getElem513005(), reDetRecMenViewBean.getElem513007(),
                    reDetRecMenViewBean.getElem513099(), null, "009");

            envoieRecap();

            // Modification de l'état de la récap

            RERecapMensuelle reRecapMensuelle = new RERecapMensuelle();
            reRecapMensuelle.setSession(getSession());
            reRecapMensuelle.setIdRecapMensuelle(reDetRecMenViewBean.getIdRecapMensuelle());
            reRecapMensuelle.retrieve();

            reRecapMensuelle.setCsEtat(IRERecapMensuelle.CS_ETAT_ENVOYE);
            reRecapMensuelle.update(getTransaction());

            // Report du montant total sur champ "recap -1 mois" du prochain
            // mois

            HashMap mntRecapTotauxElems = new HashMap();
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
            reRecapMensuelleMgrMoisSuivant.setForDateRapportMensuel(PRDateFormater
                    .convertDate_AAAAMMJJ_to_MMxAAAA(dateMoisSuivant.toStrAMJ()));
            reRecapMensuelleMgrMoisSuivant.find();

            Iterator iter = reRecapMensuelleMgrMoisSuivant.iterator();

            Object[] noElements = mntRecapTotauxElems.keySet().toArray();

            while (iter.hasNext()) {

                RERecapMensuelle recapMens = (RERecapMensuelle) iter.next();

                if (IRERecapMensuelle.CS_ETAT_ATTENTE.equals(recapMens.getCsEtat())) {

                    for (int i = 0; i < noElements.length; i++) {

                        RERecapElement recapElemMoisSuivant = new RERecapElement();
                        recapElemMoisSuivant.setSession(getSession());
                        recapElemMoisSuivant.setIdRecapMensuelle(recapMens.getIdRecapMensuelle());
                        recapElemMoisSuivant.setCodeRecap(((String) noElements[i]).substring(4, 7) + "001");
                        recapElemMoisSuivant.retrieve();

                        recapElemMoisSuivant.setMontant((String) mntRecapTotauxElems.get(noElements[i]));

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

    private String convertFormatMntXxCCToFFFFFFFFFFCC(String montant) {

        montant = JadeStringUtil.removeChar(montant, '\'');
        montant = JadeStringUtil.removeChar(montant, '-');

        StringBuffer result = new StringBuffer();

        if (!JadeStringUtil.isEmpty(montant)) {

            String valueC = montant.substring(montant.length() - 2, montant.length());
            String valueF = montant.substring(0, montant.length() - 3);

            int diff = 10 - valueF.length();

            for (int i = 0; i < diff; i++) {
                result.append("0");
            }

            result.append(valueF);
            result.append(valueC);
        } else {

            for (int i = 0; i < 12; i++) {
                result.append("0");
            }
        }

        return result.toString();
    }

    private void envoieChamp(String clefAttribut, String valeurAttribut) {
        if (!JadeStringUtil.isEmpty(valeurAttribut)) {
            attributs.put(clefAttribut, valeurAttribut);
        }
    }

    private void envoieRecap() throws Exception {
        BISession remoteSession = PRSession.connectSession(getSession(), REEnvoyerRecapARC8DProcess.APPLICATION_RECAP);

        // création de l'API
        IHEInputAnnonceLight remoteEcritureAnnonce = (IHEInputAnnonceLight) remoteSession
                .getAPIFor(IHEInputAnnonceLight.class);

        Iterator iter = recapAEnvoyer.iterator();

        while (iter.hasNext()) {
            remoteEcritureAnnonce.clear();
            remoteEcritureAnnonce.setIdProgramme(REApplication.DEFAULT_APPLICATION_CORVUS);
            remoteEcritureAnnonce.setUtilisateur(getSession().getUserId());
            remoteEcritureAnnonce.setStatut(IHEAnnoncesViewBean.CS_EN_ATTENTE);

            Map element = (Map) iter.next();
            remoteEcritureAnnonce.putAll(element);
            remoteEcritureAnnonce.add(getTransaction());
        }
    }

    private String getDoitAvoir(String codeRecap) {

        if (codeRecap.endsWith("7") || codeRecap.endsWith("4")) {
            return "1";
        } else {
            return "0";
        }
    }

    /**
     * getter pour l'attribut EMail object
     * 
     * @return la valeur courante de l'attribut EMail object
     */
    @Override
    protected String getEMailObject() {
        if (getMemoryLog().isOnErrorLevel() || getMemoryLog().isOnFatalLevel()) {
            return getSession().getLabel("PROCESS_ENVOI_RECAP_FAILED");
        } else {
            return getSession().getLabel("PROCESS_ENVOI_RECAP_SUCCESS");
        }
    }

    public List getRecapAEnvoyer() {
        return recapAEnvoyer;
    }

    public REDetailRecapMensuelleViewBean getReDetRecMenViewBean() {
        return reDetRecMenViewBean;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    private void preparerEnvoieRecap(String dateRapport, RERecapElement elem1, RERecapElement elem2,
            RERecapElement elem3, RERecapElement elem4, RERecapElement elem5, String NoRecap) {

        attributs = new HashMap();

        envoieChamp(IHEAnnoncesViewBean.CODE_APPLICATION, REEnvoyerRecapARC8DProcess.CODE_APPLICATION);
        envoieChamp(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, NoRecap);

        envoieChamp(IHEAnnoncesViewBean.NUMERO_CAISSE, noCaisse);
        envoieChamp(IHEAnnoncesViewBean.NUMERO_AGENCE, noAgence);

        envoieChamp(IHEAnnoncesViewBean.PERIODE_COMPTABLE, dateRapport.substring(0, 2));
        envoieChamp(IHEAnnoncesViewBean.EXERCICE_COMPTABLE_AAAA, dateRapport.substring(3, 7));

        if (elem1 != null) {
            envoieChamp(IHEAnnoncesViewBean.NUMERO_DE_RUBRIQUE, elem1.getCodeRecap());

            envoieChamp(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_10, (getDoitAvoir(elem1.getCodeRecap())));

            envoieChamp(IHEAnnoncesViewBean.CS_MONTANT_FFFFFFFFFFCC_1,
                    convertFormatMntXxCCToFFFFFFFFFFCC(elem1.getMontant()));
        }

        if (elem2 != null) {
            envoieChamp(IHEAnnoncesViewBean.NUMERO_DE_RUBRIQUE_2, elem2.getCodeRecap());

            envoieChamp(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_14, (getDoitAvoir(elem2.getCodeRecap())));

            envoieChamp(IHEAnnoncesViewBean.CS_MONTANT_FFFFFFFFFFCC_2,
                    convertFormatMntXxCCToFFFFFFFFFFCC(elem2.getMontant()));
        }

        if (elem3 != null) {
            envoieChamp(IHEAnnoncesViewBean.NUMERO_DE_RUBRIQUE_3, elem3.getCodeRecap());

            envoieChamp(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_18, (getDoitAvoir(elem3.getCodeRecap())));

            envoieChamp(IHEAnnoncesViewBean.CS_MONTANT_FFFFFFFFFFCC_3,
                    convertFormatMntXxCCToFFFFFFFFFFCC(elem3.getMontant()));
        }

        if (elem4 != null) {
            envoieChamp(IHEAnnoncesViewBean.NUMERO_DE_RUBRIQUE_4, elem4.getCodeRecap());

            envoieChamp(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_22, (getDoitAvoir(elem4.getCodeRecap())));

            envoieChamp(IHEAnnoncesViewBean.CS_MONTANT_FFFFFFFFFFCC_4,
                    convertFormatMntXxCCToFFFFFFFFFFCC(elem4.getMontant()));
        }

        if (elem5 != null) {
            envoieChamp(IHEAnnoncesViewBean.NUMERO_DE_RUBRIQUE_5, elem5.getCodeRecap());

            envoieChamp(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_26, (getDoitAvoir(elem5.getCodeRecap())));

            envoieChamp(IHEAnnoncesViewBean.CS_MONTANT_FFFFFFFFFFCC_5,
                    convertFormatMntXxCCToFFFFFFFFFFCC(elem5.getMontant()));
        }

        recapAEnvoyer.add(attributs);

    }

    public void setRecapAEnvoyer(List recapAEnvoyer) {
        this.recapAEnvoyer = recapAEnvoyer;
    }

    public void setReDetRecMenViewBean(REDetailRecapMensuelleViewBean reDetRecMenViewBean) {
        this.reDetRecMenViewBean = reDetRecMenViewBean;
    }

}
