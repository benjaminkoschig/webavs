package globaz.al.helpers.decision;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.globaz.al.business.constantes.ALCSCopie;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALConstCalcul;
import ch.globaz.al.business.models.dossier.CopieModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierDecisionComplexModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.droit.CalculDroitEditingModel;
import ch.globaz.al.business.models.droit.CalculDroitEditingSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.utils.ALEditingUtils;
import globaz.al.helpers.ALAbstractHelper;
import globaz.al.process.adiDecomptes.ALAdiDecomptesImpressionProcess;
import globaz.al.process.decision.ALDecisionProcess;
import globaz.al.vb.decision.ALDecisionViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.editing.Editing;
import globaz.jade.editing.JadeEditing;
import globaz.jade.editing.JadeEditing.Mode;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

/**
 * Helper dédié au viewBean ALDecisionViewBean
 *
 * @author JER
 *
 */
@Editing(family = "FAM_DECISIONS_AF", references = { "3006WAF" })
public class ALDecisionHelper extends ALAbstractHelper {

    public static String sendSOAP(String SOAPUrl, String soapMessage) throws Exception {
        URL url = new URL(SOAPUrl);
        URLConnection connection = url.openConnection();
        HttpURLConnection httpConn = (HttpURLConnection) connection;
        byte[] byteArray = soapMessage.getBytes();
        httpConn.setRequestProperty("Content-Length", String.valueOf(byteArray.length));
        httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        httpConn.setRequestProperty("SOAPAction", "");
        httpConn.setRequestMethod("POST");
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        OutputStream out = httpConn.getOutputStream();
        out.write(byteArray);
        out.close();
        BufferedReader input = null;
        StringBuffer resultMessage = new StringBuffer();
        try {
            InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
            input = new BufferedReader(isr);
            String inputLine;
            while ((inputLine = input.readLine()) != null) {
                resultMessage.append(inputLine);
            }
        }

        finally {
            if (input != null) {
                input.close();
            }
        }
        return resultMessage.toString();
    }

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // on créer éventuelles les copies par défaut ici, car retrieve du
        // viewBean => pas de création
        if (viewBean instanceof ALDecisionViewBean) {
            ALDecisionViewBean vb = (ALDecisionViewBean) viewBean;

            DossierDecisionComplexModel dossierDecision = null;

            if ("1".equals(((ALDecisionViewBean) viewBean).getFromDecompte())) {
                dossierDecision = ALServiceLocator.getDossierDecisionComplexeModelService()
                        .read(((ALDecisionViewBean) viewBean).getIdDossier());
            } else {
                dossierDecision = ALServiceLocator.getDossierDecisionComplexeModelService()
                        .read(((ALDecisionViewBean) viewBean).getId());
            }

            ALServiceLocator.getCopiesBusinessService().createDefaultCopies(dossierDecision, ALCSCopie.TYPE_DECISION);
            vb.setDossierDecisionComplexModel(dossierDecision);

            // Est-ce que le dossier est en file d'attente
            boolean fileAttente = false;
            if (vb.getDossierDecisionComplexModel() != null) {
                if (vb.getDossierDecisionComplexModel().getDossierModel() != null) {
                    fileAttente = !JadeStringUtil
                            .isBlank(vb.getDossierDecisionComplexModel().getDossierModel().getIdGestionnaire());
                }
            }
            vb.setIsFileAttente(fileAttente);
        }
        super._retrieve(viewBean, action, session);
    }

    /*
     * (non-Javadoc)
     *
     * @seeglobaz.framework.controller.FWHelper#_start(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        try {
            Mode mode = JadeEditing.getInstance().publicationMode("3006WAF");
            boolean isEditing = mode.isXml;
            boolean isPdf = mode.isFormated;

            if (isEditing) {
                if (viewBean instanceof ALDecisionViewBean) {
                    try {

                        // numéro du port et adresse serveeur
                        String port = JadePropertiesService.getInstance().getProperty("common.portWTServer");
                        String server = JadePropertiesService.getInstance().getProperty("common.adressWTServer");

                        String idDossier = ((ALDecisionViewBean) viewBean).getDossierDecisionComplexModel()
                                .getDossierModel().getIdDossier();

                        String user = viewBean.getISession().getUserId();
                        // mise en db provisoire des calculs liés aux droits du dossier
                        getListDroitDB(idDossier, user);

                        String soapMessageForDecision = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" "
                                + "	xmlns:q0=\"www.globaz.ch/xmlns/editing/al/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
                                + "	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" + "	<soapenv:Body>"
                                + "		<q0:generateDecision>" + "			<idDossier>" + idDossier + "</idDossier>"
                                + "			<idUser>" + user + "</idUser>" + "		</q0:generateDecision>"
                                + "	</soapenv:Body>" + "</soapenv:Envelope>";

                        ALDecisionHelper.sendSOAP("http://" + server + ":" + port + "/wt/editing/al",
                                soapMessageForDecision);

                    } catch (Exception e) {
                        viewBean.setMessage(e.toString());
                        viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    }
                }
            }

            if (isPdf) {
                if (viewBean instanceof ALDecisionViewBean) {
                    try {
                        // si on vient directement sur la décision
                        if (JadeStringUtil.isBlank(((ALDecisionViewBean) viewBean).getFromDecompte())) {

                            lanceProcessDecision(viewBean, session);
                        }

                        // si on vient par l'imprssion du décompte ADI
                        if (!JadeStringUtil.isBlank(((ALDecisionViewBean) viewBean).getIdDecompteAdi())
                                && (((ALDecisionViewBean) viewBean).getEditionDecompteAvecDecision())) {
                            lanceProcessDecision(viewBean, session);
                            lanceProcessEditionDecompteAdi(viewBean, session);
                        }
                        if (!JadeStringUtil.isBlank(((ALDecisionViewBean) viewBean).getIdDecompteAdi())
                                && (!((ALDecisionViewBean) viewBean).getEditionDecompteAvecDecision())) {

                            lanceProcessEditionDecompteAdi(viewBean, session);
                        }

                    } catch (Exception e) {
                        viewBean.setMessage(e.toString());
                        viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }

    /*
     * Exécute les différentes customAction possibles depuis le viewBean
     * 
     * @seeglobaz.framework.controller.FWHelper#execute(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if ("supprimerCopie".equals(action.getActionPart()) && (viewBean instanceof ALDecisionViewBean)) {
            try {
                if (((ALDecisionViewBean) viewBean).getDossierDecisionComplexModel().isNew()) {
                    ((ALDecisionViewBean) viewBean).retrieve();
                }
                CopieModel copieToDelete = ALServiceLocator.getCopieModelService()
                        .read(((ALDecisionViewBean) viewBean).getIdCopieToDelete());
                ALServiceLocator.getCopieModelService().delete(copieToDelete);
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
            return viewBean;
        } else {
            return super.execute(viewBean, action, session);
        }
    }

    /**
     * Retourne la date à utiliser pour le calcul de la décision selon le dossier
     *
     * @param dossierComplexModel
     *            dossier
     * @return la date à utiliser pour le calcul dans la décision
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private final String getDateCalcul(DossierModel dossierModel) throws JadeApplicationException {

        if (JadeDateUtil.isGlobazDate(dossierModel.getFinValidite())) {
            return dossierModel.getFinValidite();

        } else {
            return dossierModel.getDebutValidite();
        }
    }

    /**
     * Méthode qui permet de mettre en base de donnnées les calculs du dossier
     *
     * @param numeroDossier
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private void getListDroitDB(String numeroDossier, String user)
            throws JadeApplicationServiceNotAvailableException, JadeApplicationException, JadePersistenceException {
        // lecture du dossier
        DossierComplexModel dossierComplex = ALServiceLocator.getDossierComplexModelService().read(numeroDossier);

        // charger les calculs des droits
        List<CalculBusinessModel> resultatCalcul = ALServiceLocator.getCalculBusinessService()
                .getCalcul(dossierComplex, getDateCalcul(dossierComplex.getDossierModel()));
        // calcul du montant total de la décision
        Map<?, ?> total = ALServiceLocator.getCalculBusinessService().getTotal(dossierComplex.getDossierModel(),
                resultatCalcul, ALCSDossier.UNITE_CALCUL_MOIS, "1", false,
                getDateCalcul(dossierComplex.getDossierModel()));
        // calcul du montant total de la décision

        resultatCalcul = (List<CalculBusinessModel>) total.get(ALConstCalcul.DROITS_CALCULES);

        CalculDroitEditingSearchModel calculDroitSearch = new CalculDroitEditingSearchModel();
        List<String> listDroit = new ArrayList<>();

        // recherche des calculs en db
        for (int i = 0; i < resultatCalcul.size(); i++) {
            listDroit.add(resultatCalcul.get(i).getDroit().getDroitModel().getIdDroit());
        }
        calculDroitSearch.setInIdDroit(listDroit);
        calculDroitSearch.setForUser(user);
        calculDroitSearch = ALServiceLocator.getCalculDroitEditingModelService().search(calculDroitSearch);
        // si présence de calculs, on les supprime
        if (calculDroitSearch.getSize() > 0) {
            for (int i = 0; i < calculDroitSearch.getSize(); i++) {
                CalculDroitEditingModel calculDroitModel = ((CalculDroitEditingModel) calculDroitSearch
                        .getSearchResults()[i]);
                calculDroitModel = ALServiceLocator.getCalculDroitEditingModelService().delete(calculDroitModel);
            }
        }
        // mise en db des nouveaux calculs
        for (int i = 0; i < resultatCalcul.size(); i++) {
            CalculDroitEditingModel calculDroit = new CalculDroitEditingModel();
            // ajout idDroit
            calculDroit.setIdDroit(resultatCalcul.get(i).getDroit().getDroitModel().getIdDroit());
            // ajout du montant effectif
            calculDroit.setMontantResultEffectif(resultatCalcul.get(i).getCalculResultMontantEffectif());
            // ajoutDroitCachée
            calculDroit.setIsHide(resultatCalcul.get(i).isHideDroit());
            // ajout du montant du allocataire
            calculDroit.setMontantAllocataire(resultatCalcul.get(i).getMontantAllocataire());
            // ajout du montant de du conjoint
            calculDroit.setMontantAutreParent(resultatCalcul.get(i).getMontantAutreParent());
            // ajout du tarif caisse
            calculDroit.setTarifCaisse(JadeCodesSystemsUtil.getCodeLibelle(resultatCalcul.get(i).getTarifCanton()));
            // ajout du tarif effectif
            calculDroit.setTarifEffectif(JadeCodesSystemsUtil.getCodeLibelle(resultatCalcul.get(i).getTarif()));
            // type prestation
            calculDroit
                    .setTypePrestation(ALEditingUtils.getValueEditingTypePrestation(resultatCalcul.get(i).getType()));
            // total pour l'ensemle des drotis
            calculDroit.setMontantTotal(
                    new FWCurrency(total.get(ALConstCalcul.TOTAL_EFFECTIF).toString()).getBigDecimalValue().toString());
            // total par unité
            calculDroit.setMontantTotalUnite(new FWCurrency((String) total.get(ALConstCalcul.TOTAL_UNITE_EFFECTIF))
                    .getBigDecimalValue().toString());
            // user
            calculDroit.setUser(user);

            // total pour jour début
            if (!JadeStringUtil.isBlankOrZero(dossierComplex.getDossierModel().getNbJoursDebut())
            /*
             * && (!JadeStringUtil.equals(resultatCalcul.get(i).getType(), ALCSDroit.TYPE_NAIS, false) &&
             * !JadeStringUtil .equals(resultatCalcul.get(i).getType(), ALCSDroit.TYPE_ACCE, false))
             */) {
                getMontantTotalJourDebut(dossierComplex, calculDroit);
            }
            // total pour jour fin
            if (!JadeStringUtil.isBlankOrZero(dossierComplex.getDossierModel().getNbJoursFin())
            /*
             * && (!JadeStringUtil.equals(resultatCalcul.get(i).getType(), ALCSDroit.TYPE_NAIS, false) &&
             * !JadeStringUtil .equals(resultatCalcul.get(i).getType(), ALCSDroit.TYPE_ACCE, false))
             */) {
                getMontantTotalJourFin(dossierComplex, calculDroit);
            }
            // enregistrement en base
            calculDroit = ALServiceLocator.getCalculDroitEditingModelService().create(calculDroit);
            try {
                JadeThread.commitSession();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    /**
     * Calcul le montant total dossier pour nombre de jour début
     */
    private void getMontantTotalJourDebut(DossierComplexModel dossierComplex, CalculDroitEditingModel calculDroit)
            throws JadeApplicationException, JadePersistenceException {
        // contrôle du paramète
        // charger les calculs des droits
        List<CalculBusinessModel> resultatCalcul = ALServiceLocator.getCalculBusinessService()
                .getCalcul(dossierComplex, getDateCalcul(dossierComplex.getDossierModel()));
        // calcul du montant total de la décision
        Map<?, ?> total = ALServiceLocator.getCalculBusinessService().getTotal(dossierComplex.getDossierModel(),
                resultatCalcul, ALCSDossier.UNITE_CALCUL_MOIS, "1", false,
                getDateCalcul(dossierComplex.getDossierModel()));
        // calcul du montant total de la décision

        resultatCalcul = (List<CalculBusinessModel>) total.get(ALConstCalcul.DROITS_CALCULES);

        Map<?, ?> tot = ALServiceLocator.getCalculBusinessService().getTotal(dossierComplex.getDossierModel(),
                resultatCalcul, ALCSDossier.UNITE_CALCUL_JOUR, dossierComplex.getDossierModel().getNbJoursDebut(),
                false, getDateCalcul(dossierComplex.getDossierModel()));
        //
        calculDroit.setMontantTotalDebut(
                new FWCurrency((String) tot.get(ALConstCalcul.TOTAL_EFFECTIF)).getBigDecimalValue().toString());

    }

    /**
     * Calcul le montant total du dossier pour le nombre de jour fin
     */
    private void getMontantTotalJourFin(DossierComplexModel dossierComplex, CalculDroitEditingModel calculDroit)
            throws JadeApplicationException, JadePersistenceException {
        // contrôle du paramète
        // charger les calculs des droits
        List<CalculBusinessModel> resultatCalcul = ALServiceLocator.getCalculBusinessService()
                .getCalcul(dossierComplex, getDateCalcul(dossierComplex.getDossierModel()));
        // calcul du montant total de la décision
        Map<?, ?> total = ALServiceLocator.getCalculBusinessService().getTotal(dossierComplex.getDossierModel(),
                resultatCalcul, ALCSDossier.UNITE_CALCUL_MOIS, "1", false,
                getDateCalcul(dossierComplex.getDossierModel()));
        // calcul du montant total de la décision

        resultatCalcul = (List<CalculBusinessModel>) total.get(ALConstCalcul.DROITS_CALCULES);

        Map<?, ?> tot = ALServiceLocator.getCalculBusinessService().getTotal(dossierComplex.getDossierModel(),
                resultatCalcul, ALCSDossier.UNITE_CALCUL_JOUR, dossierComplex.getDossierModel().getNbJoursFin(), false,
                getDateCalcul(dossierComplex.getDossierModel()));
        // TODO ajouter seulement si pas naissance ou acceuil
        calculDroit.setMontantTotalFin(
                new FWCurrency((String) tot.get(ALConstCalcul.TOTAL_EFFECTIF)).getBigDecimalValue().toString());

    }

    /**
     * @param viewBean
     * @param session
     * @throws Exception
     */
    private void lanceProcessDecision(FWViewBeanInterface viewBean, BISession session) throws Exception {
        ALDecisionProcess process = new ALDecisionProcess();

        process.setSession((BSession) session);
        // impression de la décion oui non
        if (((ALDecisionViewBean) viewBean).getDossierDecisionComplexModel().isNew()) {
            ((ALDecisionViewBean) viewBean).retrieve();
        }
        process.setIdDossier(
                ((ALDecisionViewBean) viewBean).getDossierDecisionComplexModel().getDossierModel().getIdDossier());

        process.setDateImpression(((ALDecisionViewBean) viewBean).getDateImpression());

        // selon le bouton utilisé dans l'écran, on envoie en GED ou pas
        if (((ALDecisionViewBean) viewBean).isPrintGed()) {
            process.setEnvoiGED(true);
        } else {
            process.setEnvoiGED(false);
        }

        process.setPreview(((ALDecisionViewBean) viewBean).isPrintPreview());

        BProcessLauncher.start(process, false);
    }

    /**
     * @param viewBean
     * @param session
     * @throws Exception
     */
    private void lanceProcessEditionDecompteAdi(FWViewBeanInterface viewBean, BISession session) throws Exception {
        ALAdiDecomptesImpressionProcess adiDecompteProcess = new ALAdiDecomptesImpressionProcess();
        adiDecompteProcess.setSession((BSession) session);

        adiDecompteProcess.setIdDecompteAdi(((ALDecisionViewBean) viewBean).getIdDecompteAdi());
        adiDecompteProcess.setTypeDecompte(((ALDecisionViewBean) viewBean).getTypeDecompte());
        // selon le bouton utilisé dans l'écran, on envoie en GED ou pas
        if (((ALDecisionViewBean) viewBean).isPrintGed()) {
            adiDecompteProcess.setEnvoiGED(true);
        } else {
            adiDecompteProcess.setEnvoiGED(false);
        }
        adiDecompteProcess.setDate(((ALDecisionViewBean) viewBean).getDateImpression());

        BProcessLauncher.start(adiDecompteProcess, false);
    }

}
