package globaz.aquila.db.access.batch.transition;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.application.COApplication;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.print.CODocumentManager;
import globaz.aquila.util.COActionUtils;
import globaz.aquila.util.COStringUtils;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APISection;

/**
 * <h1>Description</h1>
 * <p>
 * Classe abstraite parente de toutes les actions qui doivent générer et envoyer un document.
 * </p>
 * 
 * @author Pascal Lovy, 09-nov-2004
 */
public abstract class COAbstractEnvoyerDocument extends COTransitionAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String EXECUTE_DOCUMENT = "execute.document";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /** L'email du destinatair du document. */
    private String email = "";

    private boolean envoyerDocument = true;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Envoie le document après avoir validé l'adresse du destinataire.
     * 
     * @param contentieux
     * @param document
     *            Le document à envoyer
     * @throws COTransitionException
     *             En cas de problème lors de l'envoi
     */
    protected void _envoyerDocument(COContentieux contentieux, CODocumentManager document) throws COTransitionException {
        this._envoyerDocument(contentieux, document, true);
    }

    /**
     * Envoie le document après avoir validé l'adresse du destinataire.
     * 
     * @param contentieux
     * @param document
     *            Le document à envoyer
     * @param ignoreDocSiMontantMinim
     *            si false, imprime le document dans tous les cas
     * @throws COTransitionException
     *             En cas de problème lors de l'envoi
     */
    protected void _envoyerDocument(COContentieux contentieux, CODocumentManager document,
            boolean ignoreDocSiMontantMinim) throws COTransitionException {
        // ne pas envoyer si ne pas envoyer
        if (!envoyerDocument) {
            return;
        }

        // ne pas envoyer le document si le montant ne dépasse pas la limite pour cette étape
        FWCurrency solde = new FWCurrency(contentieux.getSolde());

        if ((solde.compareTo(new FWCurrency(getTransition().getEtapeSuivante().getMontantMinimal())) < 0)
                && ignoreDocSiMontantMinim) {
            // mettre à jour le motif de l'historique
            setMotif(getMotif()
                    + " ("
                    + COStringUtils.formatMessage(contentieux.getSession().getLabel("AQUILA_DOC_MONTANT_MIN"),
                            new Object[] { getTransition().getEtapeSuivante().getMontantMinimal() }) + ")");
            ignoreCarMontantMinimal = true;

            return;
        }

        document.setIdTransition(getTransition().getIdTransition());
        document.setDeleteOnExit(false);
        document.setDateExecution(getDateExecution());
        document.setPrevisionnel(contentieux.getPrevisionnel());

        if (getParent() != null) {
            document.setParent(getParent());
            document.setSendCompletionMail(false);

            try {
                document.executeProcess();
            } catch (Exception e) {
                throw new COTransitionException(e);
            }
        } else {
            document.setSendMailOnError(true);
            document.setSendCompletionMail(true);

            if (JadeStringUtil.isEmpty(getEmail())) {
                try {
                    document.setEMailUsingCollaborateur();
                } catch (Exception e1) {
                    throw new COTransitionException("AQUILA_IMPOSSIBLE_TROUVER_EMAIL_COLLAB", COActionUtils.getMessage(
                            contentieux.getSession(), "AQUILA_IMPOSSIBLE_TROUVER_EMAIL_COLLAB"));
                }
            } else {
                document.setEMailAddress(getEmail());
            }

            try {
                if (!((COApplication) document.getSession().getApplication()).getProperty(
                        COAbstractEnvoyerDocument.EXECUTE_DOCUMENT, "").equals("false")) {
                    BProcessLauncher.start(document);
                }
            } catch (Exception e) {
                throw new COTransitionException(e);
            }
        }
    }

    @Override
    protected void _validate(COContentieux contentieux, BTransaction transaction) throws COTransitionException {
        // Le mode de compensation "Report" ne doit pas bloquer la création du contentieux lors d'un report de délai.
        if (!contentieux.getEtape().getLibEtape().equals(ICOEtape.CS_AUCUNE)
                && APISection.MODE_REPORT.equals(contentieux.getSection().getIdModeCompensation())) {
            throw new COTransitionException("AQUILA_ERR_SECTION_REPORTEE", COActionUtils.getMessage(contentieux
                    .getSession(), "AQUILA_ERR_SECTION_REPORTEE", new String[] { contentieux.getSection()
                    .getIdExterne() }));
        }
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getEmail() {
        return email;
    }

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_annuler(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.aquila.db.access.poursuite.COHistorique, globaz.globall.db.BTransaction)
     */
    // protected void _annuler(COContentieux contentieux, COHistorique historique, BTransaction transaction) throws
    // COTransitionException {
    // }

    /**
     * @return DOCUMENT ME!
     */
    public boolean isEnvoyerDocument() {
        return envoyerDocument;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setEmail(String string) {
        email = string;
    }

    /**
     * @param envoyerDocument
     */
    public void wantEnvoyerDocument(boolean envoyerDocument) {
        this.envoyerDocument = envoyerDocument;
    }

}
