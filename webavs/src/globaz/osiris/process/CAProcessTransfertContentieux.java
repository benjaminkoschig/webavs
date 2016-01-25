package globaz.osiris.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.contentieux.CAEvenementContentieux;
import globaz.osiris.db.contentieux.CAEvenementContentieuxManager;

/**
 * @author dda Transfert le contentieux d'un compte annexe source vers un compte annexe destination (Caisse Suisse). <br/>
 *         1. Copie les informations de bloquage du compte annexe source. <br/>
 *         2. Copie les événements contentieux du compte annexe source vers le compte annexe destination (pour l'année
 *         en cours, si rien transférer => année en cours -1).
 */
public class CAProcessTransfertContentieux extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String LABEL_COMPTE_ANNEXE_DESTINATION = "COMPTE_ANNEXE_DESTINATION";
    private static final String LABEL_COMPTES_ANNEXES_DIFFERENTS = "COMPTES_ANNEXES_DIFFERENTS";
    private static final String LABEL_TIERS_NON_RENSEIGNE = "5045";
    private static final String LABEL_TRANSFERT_CONTENTIEUX_ERROR = "TRANSFERT_CONTENTIEUX_ERROR";
    private static final String LABEL_TRANSFERT_CONTENTIEUX_OK = "TRANSFERT_CONTENTIEUX_OK";

    private static final String SECTION_ID_EXTERNE_RIGHT_PART = "00000";

    private String idExterneDestinationCompteAnnexe = new String();
    private String idRoleDestinationCompteAnnexe = new String();
    private String idSourceCompteAnnexe = new String();

    private String idTiersDestinationCompteAnnexe = null;
    private boolean quittancerCompteAnnexeDestination = false;

    private boolean sendEmail = true;

    private CACompteAnnexe sourceCompteAnnexe;

    /**
     * Constructor for CAProcessTransfertContentieux.
     */
    public CAProcessTransfertContentieux() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Constructor for CAProcessTransfertContentieux.
     * 
     * @param parent
     */
    public CAProcessTransfertContentieux(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CAProcessTransfertContentieux.
     * 
     * @param session
     */
    public CAProcessTransfertContentieux(BSession session) throws Exception {
        super(session);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        if (sourceIdEqualsDestinationId()) {
            getMemoryLog().logMessage(
                    getSession().getLabel(CAProcessTransfertContentieux.LABEL_COMPTES_ANNEXES_DIFFERENTS),
                    FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        if (isQuittancerCompteAnnexeDestination() && JadeStringUtil.isBlank(getIdTiersDestinationCompteAnnexe())) {
            getMemoryLog().logMessage(getSession().getLabel(CAProcessTransfertContentieux.LABEL_TIERS_NON_RENSEIGNE),
                    FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        try {
            CACompteAnnexe destinationCompteAnnexe = getDestinationCompteAnnexe();

            transfertCompteAnnexeContentieux(destinationCompteAnnexe);

            if (!transfertContentieux(destinationCompteAnnexe, "" + JACalendar.today().getYear())) {
                transfertContentieux(destinationCompteAnnexe, "" + (JACalendar.today().getYear() - 1));
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(isSendEmail());
        setSendMailOnError(isSendEmail());

        if (sourceIdEqualsDestinationId()) {
            this._addError(getTransaction(),
                    getSession().getLabel(CAProcessTransfertContentieux.LABEL_COMPTES_ANNEXES_DIFFERENTS));
        }

        if (isQuittancerCompteAnnexeDestination() && JadeStringUtil.isBlank(getIdTiersDestinationCompteAnnexe())) {
            this._addError(getTransaction(),
                    getSession().getLabel(CAProcessTransfertContentieux.LABEL_TIERS_NON_RENSEIGNE));
        }
    }

    /**
     * Recherche les événements contentieux attachés à une section.
     * 
     * @param idSection
     * @return
     * @throws Exception
     */
    private CAEvenementContentieuxManager findEvenementContentieux(String idSection) throws Exception {
        CAEvenementContentieuxManager manager = new CAEvenementContentieuxManager();
        manager.setSession(getSession());
        manager.setForIdSection(idSection);

        manager.find();

        return manager;
    }

    /**
     * Retourne le compte annexe de destination.
     * 
     * @return
     * @throws Exception
     */
    private CACompteAnnexe getDestinationCompteAnnexe() throws Exception {
        CACompteAnnexe destinationCompteAnnexe = new CACompteAnnexe();
        destinationCompteAnnexe.setSession(getSession());

        destinationCompteAnnexe.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
        destinationCompteAnnexe.setIdExterneRole(getIdExterneDestinationCompteAnnexe());
        destinationCompteAnnexe.setIdRole(getIdRoleDestinationCompteAnnexe());

        destinationCompteAnnexe.retrieve();

        if (destinationCompteAnnexe.isNew()) {
            if (isQuittancerCompteAnnexeDestination()) {
                destinationCompteAnnexe.setIdTiers(getIdTiersDestinationCompteAnnexe());

                destinationCompteAnnexe.add(getTransaction());

                if (destinationCompteAnnexe.hasErrors()) {
                    throw new Exception(destinationCompteAnnexe.getErrors().toString());
                }
            } else {
                throw new Exception(getSession()
                        .getLabel(CAProcessTransfertContentieux.LABEL_COMPTE_ANNEXE_DESTINATION));
            }
        }

        return destinationCompteAnnexe;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || getMemoryLog().hasErrors()) {
            return getSession().getLabel(CAProcessTransfertContentieux.LABEL_TRANSFERT_CONTENTIEUX_ERROR);
        } else {
            return getSession().getLabel(CAProcessTransfertContentieux.LABEL_TRANSFERT_CONTENTIEUX_OK);
        }
    }

    /**
     * @return
     */
    public String getIdExterneDestinationCompteAnnexe() {
        return idExterneDestinationCompteAnnexe;
    }

    /**
     * @return
     */
    public String getIdRoleDestinationCompteAnnexe() {
        return idRoleDestinationCompteAnnexe;
    }

    /**
     * @return
     */
    public String getIdSourceCompteAnnexe() {
        return idSourceCompteAnnexe;
    }

    /**
     * @return
     */
    public String getIdTiersDestinationCompteAnnexe() {
        return idTiersDestinationCompteAnnexe;
    }

    /**
     * Retourne la section du compte annexe en fonction de l'idexterne de type "décompte de cotisations".
     * 
     * @param idCompteAnnexe
     * @param idExterne
     * @return
     * @throws Exception
     */
    private CASection getSection(String idCompteAnnexe, String idExterne) throws Exception {
        CASection section = new CASection();
        section.setSession(getSession());

        section.setIdCompteAnnexe(idCompteAnnexe);
        section.setIdExterne(idExterne);
        section.setIdTypeSection(APISection.ID_TYPE_SECTION_DECOMPTE_COTISATION);

        section.setAlternateKey(CASection.AK_IDEXTERNE);

        section.retrieve();

        return section;
    }

    /**
     * Retourne le compte annexe de source.
     * 
     * @return
     */
    public CACompteAnnexe getSourceCompteAnnexe() {
        if (sourceCompteAnnexe == null) {
            sourceCompteAnnexe = new CACompteAnnexe();
            sourceCompteAnnexe.setSession(getSession());
            sourceCompteAnnexe.setIdCompteAnnexe(getIdSourceCompteAnnexe());

            try {
                sourceCompteAnnexe.retrieve();
            } catch (Exception e) {
                JadeLogger.error(this, e);
                getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            }
        }

        return sourceCompteAnnexe;
    }

    /**
     * @return
     */
    public boolean isQuittancerCompteAnnexeDestination() {
        return quittancerCompteAnnexeDestination;
    }

    /**
     * @return
     */
    public boolean isSendEmail() {
        return sendEmail;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * @param string
     */
    public void setIdExterneDestinationCompteAnnexe(String s) {
        idExterneDestinationCompteAnnexe = s;
    }

    /**
     * @param string
     */
    public void setIdRoleDestinationCompteAnnexe(String s) {
        idRoleDestinationCompteAnnexe = s;
    }

    /**
     * @param string
     */
    public void setIdSourceCompteAnnexe(String s) {
        idSourceCompteAnnexe = s;
    }

    /**
     * @param string
     */
    public void setIdTiersDestinationCompteAnnexe(String string) {
        idTiersDestinationCompteAnnexe = string;
    }

    /**
     * @param b
     */
    public void setQuittancerCompteAnnexeDestination(boolean b) {
        quittancerCompteAnnexeDestination = b;
    }

    /**
     * @param b
     */
    public void setSendEmail(boolean b) {
        sendEmail = b;
    }

    /**
     * Le compte annexe source doit être différent du compte source.
     * 
     * @return
     */
    private boolean sourceIdEqualsDestinationId() {
        if (getSourceCompteAnnexe().getIdExterneRole().equals(getIdExterneDestinationCompteAnnexe())
                && getSourceCompteAnnexe().getIdRole().equals(getIdRoleDestinationCompteAnnexe())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Copie les informations de bloquage du compte annexe source vers le compte annexe de destination.
     * 
     * @param destinationCompteAnnexe
     * @throws Exception
     */
    private void transfertCompteAnnexeContentieux(CACompteAnnexe destinationCompteAnnexe) throws Exception {
        boolean modified = false;

        if (getSourceCompteAnnexe().getContEstBloque().booleanValue()) {
            destinationCompteAnnexe.setContEstBloque(new Boolean(true));
            modified = true;
        }

        if (!JAUtil.isDateEmpty(getSourceCompteAnnexe().getContDateDebBloque())) {
            destinationCompteAnnexe.setContDateDebBloque(getSourceCompteAnnexe().getContDateDebBloque());
            modified = true;
        }

        if (!JAUtil.isDateEmpty(getSourceCompteAnnexe().getContDateFinBloque())) {
            destinationCompteAnnexe.setContDateFinBloque(getSourceCompteAnnexe().getContDateFinBloque());
            modified = true;
        }

        if (!JAUtil.isDateEmpty(getSourceCompteAnnexe().getIdContMotifBloque())) {
            destinationCompteAnnexe.setIdContMotifBloque(getSourceCompteAnnexe().getIdContMotifBloque());
            modified = true;
        }

        if (modified) {
            destinationCompteAnnexe.update();
        }
    }

    /**
     * Copie les événements contentieux du compte annexe source vers le compte annexe destination.
     * 
     * @param destinationCompteAnnexe
     * @param forYear
     *            L'année sur laquelle il faut rechercher les événements contentieux.
     * @return
     * @throws Exception
     */
    private boolean transfertContentieux(CACompteAnnexe destinationCompteAnnexe, String forYear) throws Exception {
        CASection sourceSection = getSection(getSourceCompteAnnexe().getIdCompteAnnexe(), forYear
                + CAProcessTransfertContentieux.SECTION_ID_EXTERNE_RIGHT_PART);

        if (sourceSection.isNew()) {
            return false;
        }

        CAEvenementContentieuxManager manager = findEvenementContentieux(sourceSection.getIdSection());

        if (manager.size() > 0) {
            CASection destinationSection = getSection(destinationCompteAnnexe.getIdCompteAnnexe(), forYear
                    + CAProcessTransfertContentieux.SECTION_ID_EXTERNE_RIGHT_PART);

            if (destinationSection.isNew()) {
                destinationSection.setDateSection(sourceSection.getDateSection());

                destinationSection.add();
            }

            if (findEvenementContentieux(destinationSection.getIdSection()).isEmpty()) {
                for (int i = 0; i < manager.size(); i++) {
                    CAEvenementContentieux evenementContentieux = (CAEvenementContentieux) manager.get(i);

                    evenementContentieux.setIdEvenementContentieux("");
                    evenementContentieux.setIdSection(destinationSection.getIdSection());

                    evenementContentieux.add();
                }
            }
            return true;
        } else {
            return false;
        }
    }

}
