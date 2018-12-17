/*
 * Créé le 17 juillet 2008
 */
package globaz.corvus.process;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.JAXBException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import ch.admin.zas.pool.PoolMeldungZurZAS;
import ch.admin.zas.pool.PoolMeldungZurZAS.Lot;
import ch.admin.zas.rc.VAIKMeldungKassenWechselType;
import ch.globaz.common.exceptions.ValidationException;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import globaz.commons.nss.NSUtil;
import globaz.corvus.annonce.service.REAnnonceARC3DXmlService;
import globaz.corvus.annonce.service.REGenererARC3XMLTransfertCIService;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.basescalcul.IRERenteAccordee;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeFamille;
import globaz.corvus.properties.REProperties;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeException;
import globaz.prestation.db.infos.PRInfoCompl;

/**
 * @author mpe
 *
 */
public class REGenererARC3XMLTransfertCIProcess extends BProcess {

    private static final Logger LOG = LoggerFactory.getLogger(REGenererARC3XMLTransfertCIProcess.class);

    private static final String ENVOYER_ANNONCES = "ENVOYER_ANNONCES";

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idDemandeRente = "";

    private String nssAssure = "";

    private String idInfoCompl = "";

    private String chemin = "";

    private List<String> listNss;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe REGenererARCTransfertCI.
     */
    public REGenererARC3XMLTransfertCIProcess() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe REGenererARCTransfertCI.
     *
     * @param parent
     *            DOCUMENT ME!
     */
    public REGenererARC3XMLTransfertCIProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe REGenererARCTransfertCI.
     *
     * @param session
     *            DOCUMENT ME!
     */
    public REGenererARC3XMLTransfertCIProcess(BSession session) {
        super(session);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _executeCleanUp() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _executeProcess() {
        try {
            setProgressScaleValue(listNss.size());
            setProcessDescription("BUILD XML ARC 03");
            setProgressCounter(0);

            REDemandeRente demandeRente = new REDemandeRente();
            demandeRente.setSession(getSession());
            demandeRente.setIdDemandeRente(getIdDemandeRente());
            demandeRente.retrieve();

            PRInfoCompl info = new PRInfoCompl();
            info.setSession(getSession());
            info.setIdInfoCompl(demandeRente.getIdInfoComplementaire());
            info.retrieve();

            String newNumAgence = info.getNoAgence();
            if (JadeStringUtil.isBlankOrZero(newNumAgence)) {
                newNumAgence = "000";
            }

            PoolMeldungZurZAS.Lot lotAnnonces = REGenererARC3XMLTransfertCIService.getInstance()
                    .initPoolMeldungZurZASLot(REProperties.CENTRALE_TEST.getBooleanValue(),
                            CommonProperties.KEY_NO_CAISSE.getValue());

            if(prepareEnvoieAnnonce(lotAnnonces, formatNumCaisseAgence(info.getNoCaisse(), newNumAgence), listNss, demandeRente.getIdDemandeRente())
                    && envoiARC3(lotAnnonces)) {
                   demandeRente.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE);
                   demandeRente.update();
            }


        } catch (ValidationException e) {
            try {
                getTransaction().rollback();
            } catch (Exception e1) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, getSession().getLabel(ENVOYER_ANNONCES));
            }
            getMemoryLog().logMessage(e.getMessageErreurDeValidation().toString(), FWMessage.ERREUR,
                    getSession().getLabel(ENVOYER_ANNONCES));
        } catch (Exception e) {
            try {
                getTransaction().rollback();
            } catch (Exception e1) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, getSession().getLabel(ENVOYER_ANNONCES));
            }

            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, getSession().getLabel(ENVOYER_ANNONCES));

            return false;
        }

        return true;
    }
    
    private void formatErreurMessage(Exception e) {
        StringBuilder messageNss = new StringBuilder(getSession().getLabel("PROCESS_ENVOI_ANNONCES_ERROR_NSS"));
        for(String nss: listNss) {
            if(!JadeStringUtil.isEmpty(nss)) {
                messageNss.append(" ").append(nss);
            }
        }
        logMessage(messageNss.toString());
        logMessage(getSession().getLabel("PROCESS_ENVOI_ANNONCES_ERROR_DONNES")+e.getMessage());
    }

    @Override
    public String getSubjectDetail() {
        if (getMemoryLog().isOnErrorLevel()) {
            return super.getSubjectDetail();
        } else {
            return "Fichiers transférés à l'adresse suivante : " + chemin;
        }
    }

    private boolean envoiARC3(Lot lotAnnonces)
            throws JadeException, PropertiesException, IOException, SAXException, JAXBException {
        try {
            if (lotAnnonces
                    .getVAIKMeldungNeuerVersicherterOrVAIKMeldungAenderungVersichertenDatenOrVAIKMeldungVerkettungVersichertenNr()
                    .isEmpty()) {
                throw new JadeException(getSession().getLabel("PROCESS_ENVOI_ANNONCES_ERREUR_AUCUNE_ANNONCE"));
            }
            String fileName = REGenererARC3XMLTransfertCIService.getInstance().genereFichier(lotAnnonces);
            chemin = REGenererARC3XMLTransfertCIService.getInstance().envoiFichier(fileName);
        } catch (Exception e) {
            formatErreurMessage(e);
            return false;
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

    /**
     * getter pour l'attribut EMail object
     *
     * @return la valeur courante de l'attribut EMail object
     */
    @Override
    protected String getEMailObject() {

        if (getMemoryLog().isOnErrorLevel() || getMemoryLog().isOnFatalLevel()) {
            return getSession().getLabel("PROCESS_ENVOI_ANNONCES_FAILED");
        } else {
            return getSession().getLabel("PROCESS_ENVOI_ANNONCES_SUCCESS");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * Aiguille la préparation de l'annonce passée en paramètre sur la bonne méthode <br/>
     *
     * @param listeNss
     * @param newNumCaisseAgence
     *
     * @throws ValidationException si une erreur de validation unitaire d'une annonce survient
     * @throws Exception
     *             si une erreur dans la validation par ANAKIN surivent, une exception est lancée
     */
    public boolean prepareEnvoieAnnonce(PoolMeldungZurZAS.Lot poolMeldungLot, String newNumCaisseAgence, List<String> listNss, String idDemande)
            throws ValidationException, Exception {

        String noCaisse = CommonProperties.KEY_NO_CAISSE.getValue();
        String noAgence = CommonProperties.NUMERO_AGENCE.getValue();
        String idBase = idDemande.length() > 5 ?  idDemande.substring(idDemande.length() - 5): idDemande;
        int counter = 0;
        Map<String, ValidationException> mapErreur = new HashMap<>();

        for (String nss : listNss) {
            try {
                if (!JadeStringUtil.isBlankOrZero(nss)) {
                    VAIKMeldungKassenWechselType annonceXml = REAnnonceARC3DXmlService.getInstance().getAnnonceXml(
                            newNumCaisseAgence, NSUtil.unFormatAVS(nss), formatNumCaisseAgence(noCaisse, noAgence), new Long(idBase + counter));
                    REGenererARC3XMLTransfertCIService.getInstance().validateUnitMessage(annonceXml);
                    poolMeldungLot
                            .getVAIKMeldungNeuerVersicherterOrVAIKMeldungAenderungVersichertenDatenOrVAIKMeldungVerkettungVersichertenNr()
                            .add(annonceXml);
                }
                setProgressCounter(counter++);
            } catch (ValidationException e) {
                mapErreur.put(nss, e);
            } catch (Exception e) {
                throw new JadeException(getIdDemandeRente() + " - " + nss + " : " + e.getMessage());
            }
        }
        
        if(!mapErreur.isEmpty()) {
            formatErreurMessage(mapErreur);
            return false;
        }
        return true;
    }
    
    private void formatErreurMessage(Map<String, ValidationException> mapErreur) {
        for(Entry<String, ValidationException> entry : mapErreur.entrySet()) {
            logMessage(getSession().getLabel("PROCESS_ENVOI_ANNONCES_ERROR_NSS")+entry.getKey());
            logMessage(getSession().getLabel("PROCESS_ENVOI_ANNONCES_ERROR_DONNES"));
            for(String messErreur: entry.getValue().getMessageErreurDeValidation()) {
                logMessage(messErreur);
            }
        }
    }
    
    private String formatNumCaisseAgence(String caisse, String agence) {
        String numCaisseAgence =caisse;
        numCaisseAgence = StringUtils.leftPad(numCaisseAgence, 3, '0');
        String numAgence = agence;
        numAgence = StringUtils.leftPad(numAgence, 3, '0');
        numCaisseAgence += numAgence;
        return numCaisseAgence;
    }
    
    private void logMessage(String message) {
        getMemoryLog().logMessage(message, FWMessage.ERREUR, getSession().getLabel(ENVOYER_ANNONCES));
    }

    protected void logInMemoryLog(String message, String labelSource) {
        getMemoryLog().logMessage(message, FWMessage.ERREUR, getSession().getLabel(labelSource));
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

    public String getNssAssure() {
        return nssAssure;
    }

    public String getIdInfoCompl() {
        return idInfoCompl;
    }

    public void setNssAssure(String nssAssure) {
        this.nssAssure = nssAssure;
    }

    public void setIdInfoCompl(String idInfoCompl) {
        this.idInfoCompl = idInfoCompl;
    }

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    public List<String> getListNss() {
        return listNss;
    }

    public void setListNss(List<String> listNss) {
        this.listNss = listNss;
    }

}
