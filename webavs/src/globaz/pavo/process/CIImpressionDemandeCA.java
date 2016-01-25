package globaz.pavo.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcrituresSuspens;
import globaz.pavo.db.compte.CIEcrituresSuspensManager;
import globaz.pavo.print.itext.CIDemandeCA;
import globaz.pavo.print.itext.CIDemandeCAStruct;
import globaz.pyxis.adresse.formater.TIAdresseFormater;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.ICommonConstantes;
import java.util.ArrayList;

public class CIImpressionDemandeCA extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idJournal = null;
    private String libelle = null;
    private String typeJournal = null;

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        boolean result = true;
        BStatement statement = null;
        CIEcrituresSuspens ecrituresSuspens = null;
        ArrayList list = new ArrayList();
        CIDemandeCA demandeCADoc = null;
        int index = 0;
        CIEcrituresSuspensManager ecrituresSuspensMng = new CIEcrituresSuspensManager();
        ecrituresSuspensMng.setSession(getSession());
        ecrituresSuspensMng.setForIdJournal(getIdJournal());
        ecrituresSuspensMng.setForIdTypeCompte(CIEcriture.CS_CI_SUSPENS);
        ecrituresSuspensMng.setForRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);
        try {
            // ouverture du curseur
            statement = ecrituresSuspensMng.cursorOpen(getTransaction());
            while (((ecrituresSuspens = (CIEcrituresSuspens) ecrituresSuspensMng.cursorReadNext(statement)) != null)
                    && (!ecrituresSuspens.isNew())) {
                // Recherche de l'affiliation
                AFAffiliation affiliation = new AFAffiliation();
                affiliation.setSession(getSession());
                affiliation.setAffiliationId(ecrituresSuspens.getIdAffiliation());
                affiliation.retrieve(getTransaction());
                if ((affiliation == null) || affiliation.isNew()) {
                    throw new Exception(getSession().getLabel("MSG_AUCUNE_AFFILIATION"));
                }
                // Recherche du tiers
                TITiersViewBean tiers = new TITiersViewBean();
                tiers.setSession(getSession());
                tiers.setIdTiers(affiliation.getIdTiers());
                tiers.retrieve(getTransaction());
                if ((tiers == null) || tiers.isNew()) {
                    throw new Exception(getSession().getLabel("MSG_AUCUN_TIERS"));
                }
                // setter l'adresse du tiers
                CIDemandeCAStruct demandeCAStruct = new CIDemandeCAStruct();
                demandeCAStruct.setAdresse(tiers.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        ICommonConstantes.CS_APPLICATION_COTISATION, JACalendar.todayJJsMMsAAAA(),
                        new TIAdresseFormater(), true, tiers.getLangue()));
                // setter la politesse du tiers en fonction de la langue
                demandeCAStruct.setPolitesse(tiers.getFormulePolitesse(tiers.getLangue()));
                // setter la langue
                if (!JadeStringUtil.isBlank(tiers.getLangueIso())) {
                    demandeCAStruct.setLangue(JadeStringUtil.toUpperCase(tiers.getLangueIso()));
                } else {
                    demandeCAStruct.setLangue("FR");
                }
                demandeCAStruct.setNumAffilie(affiliation.getAffilieNumero());
                list.add(index, demandeCAStruct);
                index++;
            }
            demandeCADoc = new CIDemandeCA();
            demandeCADoc.setSession(getSession());
            demandeCADoc.setListeEtudiant(list);
            demandeCADoc.setIdJournal(getIdJournal());
            demandeCADoc.setLibelleJournal(getLibelle());
            demandeCADoc.setParent(this);
            demandeCADoc.setSize(list.size());
            setSendCompletionMail(false);
            demandeCADoc.start();
        } catch (Exception e) {
            e.printStackTrace();
            getMemoryLog().logMessage(e.getMessage().toString(), FWViewBeanInterface.ERROR, "ImpressionDemandeCA");
            result = false;
        } finally {
            if (ecrituresSuspensMng != null) {
                ecrituresSuspensMng.cursorClose(statement);
            }
        }
        return result;
    }

    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isBlank(getEMailAddress())) {
            this._addError(getSession().getLabel("MSG_EMAIL_OBLIGATOIRE"));
        }
        if (JadeStringUtil.isBlankOrZero(getIdJournal())) {
            this._addError(getSession().getLabel("MSG_ID_JOURNAL_OBLIGATOIRE"));
        }
        // divers :
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        // Les erreurs sont ajoutées à la session,
        // abort permet l'arrêt du process
        if (getSession().hasErrors()) {
            abort();
        }
    }

    @Override
    protected String getEMailObject() {
        if (isOnError() || isAborted() || getMemoryLog().getErrorLevel().equals(FWMessage.ERREUR)) {
            return getSession().getLabel("MSG_IMPRESSION_DEMANDE_CA_KO");
        } else {
            return getSession().getLabel("MSG_IMPRESSION_DEMANDE_CA_OK");
        }
    }

    public String getIdJournal() {
        return idJournal;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getTypeJournal() {
        return typeJournal;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public void setLibelleJournal(String libelle) {
        this.libelle = libelle;
    }

    public void setTypeJournal(String typeJournal) {
        this.typeJournal = typeJournal;
    }
}
