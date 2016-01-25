package globaz.musca.process.interets;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.translation.CodeSystem;
import globaz.musca.util.FAUtil;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.db.interets.CAInteretMoratoireManager;
import globaz.osiris.utils.CAUtil;
import java.text.MessageFormat;

public class FAChangementJournalIMProcess extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Attributs
    private String idJournal = "";
    private String listIdIM = "";

    // Constructeur
    public FAChangementJournalIMProcess() {
        super();
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    };

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        try {
            CAInteretMoratoireManager mgrIM = new CAInteretMoratoireManager();
            mgrIM.setSession(getSession());
            mgrIM.setInIdInteretMoratoire(getListIdIM());
            mgrIM.find(BManager.SIZE_NOLIMIT);

            for (int i = 1; i <= mgrIM.size(); i++) {
                CAInteretMoratoire im = ((CAInteretMoratoire) mgrIM.getEntity(i - 1));

                FAEnteteFacture facture = new FAEnteteFacture();
                facture.setSession(getSession());
                // idSection ?????
                facture.setIdEntete(im.getIdSectionFacture());
                facture.retrieve(getTransaction());

                String idExterneFacture = CAUtil.creerNumeroSectionUniquePourInteretMoratoire(getSession(),
                        getTransaction(), facture.getIdRole(), facture.getIdExterneRole(), facture.getIdTypeFacture(),
                        facture.getIdExterneFacture().substring(0, 4), facture.getIdSousType(),
                        facture.getIdExterneFacture());

                FAEnteteFactureManager mgrFactureExistante = new FAEnteteFactureManager();
                mgrFactureExistante.setSession(getSession());
                mgrFactureExistante.setForIdPassage(getIdJournal());
                mgrFactureExistante.setForIdExterneFacture(idExterneFacture);
                mgrFactureExistante.setForIdExterneRole(facture.getIdExterneRole());
                mgrFactureExistante.setForIdRole(facture.getIdRole());
                mgrFactureExistante.setForIdTiers(facture.getIdTiers());
                mgrFactureExistante.find(getTransaction(), BManager.SIZE_NOLIMIT);
                FAEnteteFacture newFacture = new FAEnteteFacture();
                if (mgrFactureExistante.size() == 1) {
                    newFacture = (FAEnteteFacture) mgrFactureExistante.getFirstEntity();
                } else if (mgrFactureExistante.size() <= 0) {
                    newFacture.copyDataFromEntity(facture);
                    newFacture.setIdEntete(null);
                    newFacture.setIdPassage(getIdJournal());
                    newFacture.setIdExterneFacture(idExterneFacture);
                    newFacture.setTotalFacture("0");
                    newFacture.add(getTransaction());
                } else {
                    throw new Exception(FWMessageFormat.format(getSession().getLabel("DOUBLONS_ENTETEFACTURE"),
                            getIdJournal(), idExterneFacture, facture.getIdExterneRole()));
                }

                // idSection ????? la c le meme
                im.setIdSection(newFacture.getIdEntete());
                im.setIdSectionFacture(newFacture.getIdEntete());
                // idJournalCalcul ???? la c le meme
                im.setIdJournalCalcul(getIdJournal());
                im.setIdJournalFacturation(getIdJournal());
                im.update(getTransaction());
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

        if (JadeStringUtil.isEmpty(getListIdIM())) {
            getSession().addError(getSession().getLabel("SELECTION_IM_OBLIGATOIRE"));
            return;
        }

        /**
         * Vérifier que seul des IM à contrôler ont été sélectionnés
         */
        CAInteretMoratoireManager mgrIM = new CAInteretMoratoireManager();
        mgrIM.setSession(getSession());
        mgrIM.setInIdInteretMoratoire(getListIdIM());
        mgrIM.find(BManager.SIZE_NOLIMIT);

        for (int i = 1; i <= mgrIM.size(); i++) {
            CAInteretMoratoire im = ((CAInteretMoratoire) mgrIM.getEntity(i - 1));
            if (!CAInteretMoratoire.CS_A_CONTROLER.equals(im.getStatus())) {
                getSession().addError(getSession().getLabel("SELECTION_IM_A_CONTROLER_UNIQUEMENT"));
                return;
            }
        }

        if (JadeStringUtil.isEmpty(getIdJournal())) {
            getSession().addError(getSession().getLabel("SELECTION_PASSAGE_OBLIGATOIRE"));
            return;
        }

        try {
            if (FAUtil.isModFacInPassage(getSession(), FAModuleFacturation.CS_MODULE_IM_COTARR, getIdJournal())) {
                getSession().addError(
                        MessageFormat.format(getSession().getLabel("MODULE_NON_AUTORISE_IN_PASSAGE"), getIdJournal(),
                                CodeSystem.getLibelle(getSession(), FAModuleFacturation.CS_MODULE_IM_COTARR)));
                return;
            }
        } catch (Exception e) {
            getSession().addError(getSession().getLabel("MESSAGE_TO_SENT_TO_GLOBAZ") + " : " + e.toString());
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

    @Override
    protected String getEMailObject() {

        /**
         * On envoie un mail uniquement en cas d'erreur Ceci est paramétré dans la méthode _validate()
         */
        return getSession().getLabel("EMAIL_OBJECT_CHANGEMENTJOURNALIM_ERROR");
    }

    /**
     * getter
     */

    public String getIdJournal() {
        return idJournal;
    }

    public String getListIdIM() {
        return listIdIM;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setIdJournal(String newIdJournal) {
        idJournal = newIdJournal;
    }

    /**
     * setter
     */

    public void setListIdIM(String newListIdIM) {
        listIdIM = newListIdIM;
    }

}
