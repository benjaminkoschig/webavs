package globaz.hermes.process;

import globaz.framework.process.FWProcess;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.db.parametrage.HEAttenteRetourViewBean;
import globaz.hermes.utils.StringUtils;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class HELibererCIProcess extends FWProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String motif;
    private String numeroAVS;
    private String numeroCaisse;
    // HEOutputAnnonceViewBean ciData = null;
    private String refUnique;
    private String statut;

    /**
     * Constructor for HELibererCIProcess.
     */
    public HELibererCIProcess() {
        super();
    }

    /**
     * Constructor for HELibererCIProcess.
     * 
     * @param session
     */
    public HELibererCIProcess(BSession session) {
        super(session);
    }

    /**
     * Constructor for HELibererCIProcess.
     * 
     * @param parent
     */
    public HELibererCIProcess(FWProcess parent) {
        super(parent);
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
        // ciData = null;
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        setSendCompletionMail(false);
        try {
            // reprendre les 3839 pour
            HEOutputAnnonceListViewBean liste3839 = new HEOutputAnnonceListViewBean();
            liste3839.setSession(getSession());
            liste3839.setForCodeApplicationOR("38", "39");
            liste3839.setForRefUnique(getRefUnique());
            liste3839.setForNumeroAVS(getNumeroAVS());
            liste3839.setForNumCaisse(getNumeroCaisse());
            liste3839.setForMotif(getMotif());
            liste3839.wantCallMethodAfter(false);
            liste3839.wantCallMethodAfterFind(false);
            liste3839.wantCallMethodBefore(false);
            liste3839.wantCallMethodBeforeFind(false);
            liste3839.find(getTransaction(), BManager.SIZE_NOLIMIT);
            // rechercher le numéro de l'annonce du 39
            String newRefUnique = liste3839.getRefUniqueCI();
            if (StringUtils.isStringEmpty(newRefUnique)) {
                throw new Exception(getSession().getLabel("HERMES_10005"));
            } else {
                // pour chaque 38, 39 trouvés mettre à jour la référence unique
                // et le statut
                boolean isRetourAttendu = false;
                for (int i = 0; i < liste3839.size(); i++) {
                    HEOutputAnnonceViewBean crt = (HEOutputAnnonceViewBean) liste3839.getEntity(i);
                    crt.retrieve(getTransaction());
                    crt.setRefUnique(newRefUnique);
                    crt.setStatut(IHEAnnoncesViewBean.CS_ORPHELIN);
                    crt.wantCallMethodAfter(false);
                    crt.wantCallMethodBefore(false);
                    crt.wantCallValidate(false);
                    crt.update(getTransaction());
                    // en plus, si on a un 38001 ou 39001
                    if (crt.getChampEnregistrement().startsWith("38001")
                            || crt.getChampEnregistrement().startsWith("39001")) {
                        // vérifier si il y a une annonce en attendue
                        HEAttenteRetourViewBean retour = new HEAttenteRetourViewBean();
                        retour.setSession(getSession());
                        retour.setReferenceUnique(getRefUnique());
                        retour.setMotif(getMotif());
                        retour.setIdAnnonceRetour(crt.getIdAnnonce());
                        retour.setAlternateKey(HEAttenteRetourViewBean.FK_HEA_RNIANN);
                        retour.wantCallMethodAfter(false);
                        retour.wantCallMethodBefore(false);
                        retour.wantCallValidate(false);
                        retour.retrieve(getTransaction());
                        if (!retour.isNew()) {
                            retour.setIdAnnonceRetour("0");
                            retour.setPrimaryKey();
                            retour.update(getTransaction());
                            isRetourAttendu = true;
                        }
                    }
                }
                // si la série était terminé et qu'on lui à ôté une attente
                // retour,
                // repasser la série dans le statut en traitement
                if (IHEAnnoncesViewBean.CS_TERMINE.equals(getStatut()) && isRetourAttendu) {
                    HEOutputAnnonceListViewBean serie = new HEOutputAnnonceListViewBean();
                    serie.setSession(getSession());
                    serie.setForRefUnique(getRefUnique());
                    serie.setForMotif(getMotif());
                    // ôter le numéro avs car il n'y a pas un seul numéro AVS
                    // pour la série à mettre dans le statut en cours
                    // // serie.setForNumeroAVS(ciData.getNumeroAVS());
                    serie.wantCallMethodAfter(false);
                    serie.wantCallMethodAfterFind(false);
                    serie.wantCallMethodBefore(false);
                    serie.wantCallMethodBeforeFind(false);
                    serie.find(getTransaction(), BManager.SIZE_NOLIMIT);
                    for (int i = 0; i < serie.size(); i++) {
                        HEOutputAnnonceViewBean annonceCourante = (HEOutputAnnonceViewBean) serie.getEntity(i);
                        annonceCourante.retrieve(getTransaction());
                        annonceCourante.setStatut(IHEAnnoncesViewBean.CS_A_TRAITER);
                        annonceCourante.wantCallMethodAfter(false);
                        annonceCourante.wantCallMethodBefore(false);
                        annonceCourante.wantCallValidate(false);
                        annonceCourante.update(getTransaction());
                    }
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return null;
    }

    /**
     * @return
     */
    public String getMotif() {
        return motif;
    }

    /**
     * @return
     */
    public String getNumeroAVS() {
        return numeroAVS;
    }

    /**
     * @return
     */
    public String getNumeroCaisse() {
        return numeroCaisse;
    }

    /**
     * @return
     */
    public String getRefUnique() {
        return refUnique;
    }

    /**
     * @return
     */
    public String getStatut() {
        return statut;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * @param string
     */
    public void setMotif(String string) {
        motif = string;
    }

    /**
     * @param string
     */
    public void setNumeroAVS(String string) {
        numeroAVS = string;
    }

    /**
     * @param string
     */
    public void setNumeroCaisse(String string) {
        numeroCaisse = string;
    }

    /**
     * @param string
     */
    public void setRefUnique(String string) {
        refUnique = string;
    }

    /**
     * @param string
     */
    public void setStatut(String string) {
        statut = string;
    }

}
