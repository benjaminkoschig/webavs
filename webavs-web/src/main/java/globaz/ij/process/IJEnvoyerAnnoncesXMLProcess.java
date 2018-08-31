/*
 * Créé le 17 juillet 2008
 */
package globaz.ij.process;

import java.io.File;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.admin.zas.pool.PoolMeldungZurZAS;
import ch.admin.zas.rc.IVTaggelderMeldungType;
import ch.admin.zas.rc.PoolFussType;
import ch.admin.zas.rc.PoolKopfType;
import ch.globaz.common.exceptions.ValidationException;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.ij.api.annonces.IIJAnnonce;
import globaz.ij.db.annonces.IJAnnonce;
import globaz.ij.db.annonces.IJAnnonceManager;
import globaz.ij.helpers.annonces.IJAnnoncesXMLValidatorService;
import globaz.ij.helpers.annonces.IJAnnoncesXmlService;
import globaz.ij.properties.IJProperties;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.common.JadeException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;

/**
 * @author ebko
 * 
 */
public class IJEnvoyerAnnoncesXMLProcess extends BProcess {

    private static final String ENVOYER_ANNONCES = "ENVOYER_ANNONCES";

    private static final Logger LOG = LoggerFactory.getLogger(IJEnvoyerAnnoncesXMLProcess.class);

    private boolean modeTest = true;

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String forDateEnvoi = "";

    private String forMoisAnneeComptable = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJEnvoyerAnnoncesProcess.
     */
    public IJEnvoyerAnnoncesXMLProcess() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe IJEnvoyerAnnoncesProcess.
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public IJEnvoyerAnnoncesXMLProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe IJEnvoyerAnnoncesProcess.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public IJEnvoyerAnnoncesXMLProcess(BSession session) {
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

            PoolMeldungZurZAS.Lot lotAnnonces = initPoolMeldungZurZASLot(IJProperties.CENTRALE_TEST.getBooleanValue(),
                    CommonProperties.KEY_NO_CAISSE.getValue());

            // On prend la date du jour qu'on mettra a toutes les annonces
            // envoyées (pour éviter le problème qui
            // surviendrait si le processus était lancé a 23H59 et se terminait
            // apres minuit.
            String date = JACalendar.todayJJsMMsAAAA();
            BSession session = getSession();
            BTransaction transaction = getTransaction();
            IJAnnonceManager mgr = null;
            IJAnnonce annonce = null;
            BStatement statement = null;
            int nbAnnoncesLot = 0;
            // on doit envoyer dans l'ordre : les annonces du mois et annee
            // comptable en état validé , les annonces
            // erronnées, et si réannonce, les annonces qui ont le meme mois et
            // année comptable qu'une annonce envoyée a
            // la date d'envoi et les annonces envoyées a cette date dont le
            // mois et annee Comptable est différent

            // si il ne s'agit pas d'une réannonce
            if (JAUtil.isDateEmpty(forDateEnvoi)) {
                mgr = new IJAnnonceManager();
                mgr.setSession(session);
                mgr.setForCsEtat(IIJAnnonce.CS_VALIDE);
                mgr.setForMoisAnneeComptable(forMoisAnneeComptable);

                statement = mgr.cursorOpen(transaction);
                
                nbAnnoncesLot += mgr.getCount();

                while ((annonce = (IJAnnonce) (mgr.cursorReadNext(statement))) != null) {
                    annonce.setCsEtat(IIJAnnonce.CS_ENVOYEE);
                    annonce.setDateEnvoi(date);
                    annonce.update(transaction);
                    prepareEnvoieAnnonce(annonce, lotAnnonces);
                }
                
                mgr.cursorClose(statement);
            }

            // Envoi des annonces erronnées
            mgr = new IJAnnonceManager();
            mgr.setSession(session);
            mgr.setForCsEtat(IIJAnnonce.CS_ERRONEE);

            statement = mgr.cursorOpen(transaction);
            nbAnnoncesLot += mgr.getCount();
            while ((annonce = (IJAnnonce) (mgr.cursorReadNext(statement))) != null) {
                annonce.setCsEtat(IIJAnnonce.CS_ENVOYEE);
                annonce.setDateEnvoi(date);
                annonce.update(transaction);
                prepareEnvoieAnnonce(annonce, lotAnnonces);
            }

            mgr.cursorClose(statement);

            // si réenvois
            if (!JAUtil.isDateEmpty(forDateEnvoi)) {
                // envoi des annonces ayant la même date d'envoi et le même
                // moisAnneeComptable
                mgr = new IJAnnonceManager();
                mgr.setSession(session);
                mgr.setForDateEnvoi(forDateEnvoi);
                mgr.setForMoisAnneeComptable(forMoisAnneeComptable);
                statement = mgr.cursorOpen(transaction);
                nbAnnoncesLot += mgr.getCount();

                while ((annonce = (IJAnnonce) (mgr.cursorReadNext(statement))) != null) {
                    prepareEnvoieAnnonce(annonce, lotAnnonces);
                }

                mgr.cursorClose(statement);

                // envoi des annonces ayant la même date d'envoi mais un
                // moisAnneeComptable différent.
                mgr = new IJAnnonceManager();
                mgr.setSession(session);
                mgr.setForDateEnvoi(forDateEnvoi);
                mgr.setForMoisAnneeComptableDifferentDe(forMoisAnneeComptable);
                statement = mgr.cursorOpen(transaction);
                nbAnnoncesLot += mgr.getCount();

                while ((annonce = (IJAnnonce) (mgr.cursorReadNext(statement))) != null) {
                    prepareEnvoieAnnonce(annonce, lotAnnonces);
                }

                mgr.cursorClose(statement);
            }
            LOG.debug(String.valueOf(nbAnnoncesLot));

            if (lotAnnonces
                    .getVAIKMeldungNeuerVersicherterOrVAIKMeldungAenderungVersichertenDatenOrVAIKMeldungVerkettungVersichertenNr()
                    .isEmpty()) {
                throw new JadeException(getSession().getLabel("PROCESS_ENVOI_ANNONCES_ERREUR_AUCUNE_ANNONCE"));
            }

            String fileName = IJAnnoncesXMLValidatorService.getInstance().genereFichier(lotAnnonces, nbAnnoncesLot);
            envoiFichier(fileName);
        } catch (ValidationException e) {
            try {
                getTransaction().rollback();
            } catch (Exception e1) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, getSession().getLabel(ENVOYER_ANNONCES));
            }
            getMemoryLog().logMessage(e.getMessageErreurDeValidation().toString(), FWMessage.ERREUR, getSession().getLabel(ENVOYER_ANNONCES));
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

    /**
     * 
     * @param poolKopfTest if you need to set TEST flag into header
     * @param poolKopfSender
     * @return
     * @throws PropertiesException
     * @throws ParseException
     * @throws DatatypeConfigurationException
     */
    private PoolMeldungZurZAS.Lot initPoolMeldungZurZASLot(boolean poolKopfTest, String poolKopfSender)
            throws PropertiesException, ParseException, DatatypeConfigurationException {
        ch.admin.zas.pool.ObjectFactory factoryPool = new ch.admin.zas.pool.ObjectFactory();
        ch.admin.zas.rc.ObjectFactory factoryType = new ch.admin.zas.rc.ObjectFactory();
        ch.admin.zas.pool.PoolMeldungZurZAS.Lot lot = factoryPool.createPoolMeldungZurZASLot();
        PoolKopfType poolKopf = factoryType.createPoolKopfType();
        if (poolKopfTest) {
            poolKopf.setTest("TEST");
        }
        poolKopf.setSender(poolKopfSender);

        final DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        final String dateStr = JACalendar.todayJJsMMsAAAA();
        final java.util.Date dDate = format.parse(dateStr);

        GregorianCalendar gregory = new GregorianCalendar();
        gregory.setTime(dDate);

        XMLGregorianCalendar dealCloseDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
        poolKopf.setErstellungsdatum(dealCloseDate);
        lot.setPoolKopf(poolKopf);

        PoolFussType poolFuss = factoryType.createPoolFussType();
        poolFuss.setEintragungengesamtzahl(0);
        lot.setPoolFuss(poolFuss);

        return lot;
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
            return MessageFormat.format(getSession().getLabel("PROCESS_ENVOI_ANNONCES_SUCCESS"), forMoisAnneeComptable);
        }

    }

    /**
     * getter pour l'attribut for date envoi
     * 
     * @return la valeur courante de l'attribut for date envoi
     */
    public String getForDateEnvoi() {
        return forDateEnvoi;
    }

    /**
     * getter pour l'attribut for mois annee comptable
     * 
     * @return la valeur courante de l'attribut for mois annee comptable
     */
    public String getForMoisAnneeComptable() {
        return forMoisAnneeComptable;
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
     * selon si c'est une annonce sous la 9ème ou 10ème révision, et si c'est une augmentation ou une diminution
     * 
     * @param annonce
     *            l'annonce à préparer
     * @throws ValidationException si une erreur de validation unitaire d'une annonce survient
     * @throws Exception
     *             si une erreur dans la validation par ANAKIN surivent, une exception est lancée
     */
    void prepareEnvoieAnnonce(IJAnnonce annonce, PoolMeldungZurZAS.Lot poolMeldungLot)
            throws ValidationException, Exception {
        
        annonce.loadPeriodesAnnonces(getTransaction());

        IJAnnoncesXmlService ijService =  IJAnnoncesXmlService.getInstance();
        
        try {
            IVTaggelderMeldungType annonceXml = ijService.getAnnonceXml(annonce);
            IJAnnoncesXMLValidatorService.getInstance().validateUnitMessage(annonceXml);
            poolMeldungLot
            .getVAIKMeldungNeuerVersicherterOrVAIKMeldungAenderungVersichertenDatenOrVAIKMeldungVerkettungVersichertenNr()
            .add(annonceXml);
        } catch (ValidationException e) {
            e.getMessageErreurDeValidation().add(0, annonce.getIdAnnonce() + " - " + annonce.getNoAssure() + " : ");
            throw e;
        }  catch (Exception e) {
            throw new JadeException(annonce.getIdAnnonce() + " - " + annonce.getNoAssure() + " : " + e.getMessage());
        }      
    } 

    protected void logInMemoryLog(String message, String labelSource) {
        getMemoryLog().logMessage(message, FWMessage.ERREUR, getSession().getLabel(labelSource));
    }

    /**
     * setter pour l'attribut for date envoi
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForDateEnvoi(String string) {
        forDateEnvoi = string;
    }

    /**
     * setter pour l'attribut for mois annee comptable
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForMoisAnneeComptable(String string) {
        forMoisAnneeComptable = string;
    }

    /**
     * Méthode qui envoie le fichier à la centrale
     * 
     * @param fichier à envoyer à la centrale
     * @throws PropertiesException 
     * @throws JadeClassCastException 
     * @throws ClassCastException 
     * @throws  
     * @throws JadeServiceActivatorException 
     * @throws JadeServiceLocatorException 
     * @throws Exception
     */
    private void envoiFichier(String fichier) throws JadeServiceLocatorException, JadeServiceActivatorException, JadeClassCastException, PropertiesException  {

        JadeFsFacade.copyFile(fichier, IJProperties.FTP_CENTRALE_PATH.getValue() + "/" + new File(fichier).getName());
    }

    /**
     * Nous donne si c'est le mode test
     * 
     * @return le mode test
     */
    public boolean isModeTest() {
        return modeTest;
    }

    /**
     * set le mode de test
     * 
     * @param modeTest
     */
    public void setModeTest(boolean modeTest) {
        this.modeTest = modeTest;
    }

}
