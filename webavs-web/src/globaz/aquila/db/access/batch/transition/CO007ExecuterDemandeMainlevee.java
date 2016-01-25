package globaz.aquila.db.access.batch.transition;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.print.CO06DemandeMainlevee;
import globaz.aquila.process.COProcessContentieux;
import globaz.aquila.util.COActionUtils;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.external.IntTiers;
import java.util.List;

/**
 * Effectue les actions spécifiques à la transition.
 * 
 * @author Pascal Lovy, 17-nov-2004
 */
public class CO007ExecuterDemandeMainlevee extends COAbstractEnvoyerDocument {

    /** Liste des annexes */
    private boolean annexeCDPChecked = false;
    private boolean annexeDecisionChecked = false;
    private boolean annexeExtraitArtChecked = false;
    private boolean annexeSommationChecked = false;
    private String annexeTexteLibre = "";
    private boolean annexeTexteLibreChecked = false;
    /** La date de la decision */
    private String dateDecision = "";
    /** La date de situation du compte */
    private String dateSituationCompte = "";
    /** La date de la sommation */
    private String dateSommation = "";
    /** Les frais supplémentaires de poursuite */
    private String fraisSupplementairesPoursuite = "";

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_execute(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.globall.db.BTransaction)
     */
    @Override
    protected void _execute(COContentieux contentieux, List taxes, BTransaction transaction)
            throws COTransitionException {

        // Génération et envoi du document
        try {
            CO06DemandeMainlevee dm = new CO06DemandeMainlevee(transaction.getSession());

            String listeAnnexes = giveAnnexe(transaction, contentieux, dm);

            if ((getParent() == null)
                    || JadeStringUtil.isBlank(((COProcessContentieux) getParent()).getUserIdCollaborateur())) {
                dm.setCollaborateur(transaction.getSession().getUserInfo());
            } else {
                dm.setCollaborateur(((COProcessContentieux) getParent()).getUser());
            }
            dm.addContentieux(contentieux);
            dm.setTaxes(taxes);
            dm.setListeAnnexes(listeAnnexes);
            dm.setDateSituationCompte(getDateSituationCompte());

            this._envoyerDocument(contentieux, dm);
        } catch (Exception e) {
            throw new COTransitionException(e);
        }
    }

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_validate(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.globall.db.BTransaction)
     */
    @Override
    protected void _validate(COContentieux contentieux, BTransaction transaction) throws COTransitionException {
        super._validate(contentieux, transaction);
        // Test des préconditions
        _validerSolde(contentieux);
        _validerEcheance(contentieux);
        if (annexeDecisionChecked && JadeStringUtil.isEmpty(dateDecision)) {
            throw new COTransitionException("AQUILA_ERR_CO007_DATE_DECISION_VIDE", COActionUtils.getMessage(
                    contentieux.getSession(), "AQUILA_ERR_CO007_DATE_DECISION_VIDE"));
        }
        if (annexeSommationChecked && JadeStringUtil.isEmpty(dateSommation)) {
            throw new COTransitionException("AQUILA_ERR_CO007_DATE_SOMMATION_VIDE", COActionUtils.getMessage(
                    contentieux.getSession(), "AQUILA_ERR_CO007_DATE_SOMMATION_VIDE"));
        }
    }

    /**
     * @return La valeur courante de la propriété
     */
    public boolean getAnnexeCDPChecked() {
        return annexeCDPChecked;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public boolean getAnnexeDecisionChecked() {
        return annexeDecisionChecked;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public boolean getAnnexeExtraitArtChecked() {
        return annexeExtraitArtChecked;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public boolean getAnnexeSommationChecked() {
        return annexeSommationChecked;
    }

    /**
     * @return the annexeTexteLibre
     */
    public String getAnnexeTexteLibre() {
        return annexeTexteLibre;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getDateDecision() {
        return dateDecision;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getDateSituationCompte() {
        return dateSituationCompte;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getDateSommation() {
        return dateSommation;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getFraisSupplementairesPoursuite() {
        return fraisSupplementairesPoursuite;
    }

    /**
     * @param contentieux
     * @param id
     * @return
     */
    public String getLabelDocument(COContentieux contentieux, String langueIso, String id) {
        try {
            if (contentieux.getSession().getApplication() == null) {
                return "Unknown label " + id + " (Application not found)";
            }

            return contentieux.getSession().getApplication().getLabel(id, langueIso);
        } catch (Exception e) {
            return "Unknown label " + id + " (Application not found)";
        }
    }

    /**
     * @param transaction
     * @param contentieux
     * @param dm
     * @throws Exception
     */
    private String giveAnnexe(BTransaction transaction, COContentieux contentieux, CO06DemandeMainlevee dm)
            throws Exception {
        IntTiers destinataireDocument = dm.getTiersService().getTribunal(transaction.getSession(),
                contentieux.getCompteAnnexe().getTiers(), contentieux.getCompteAnnexe().getIdExterneRole());

        String langueDoc;
        if (destinataireDocument != null) {
            langueDoc = destinataireDocument.getLangueISO();
        } else {
            langueDoc = transaction.getSession().getIdLangueISO();
        }

        StringBuffer listeAnnexes = new StringBuffer("");
        if (annexeDecisionChecked && !JadeStringUtil.isEmpty(dateDecision)) {
            listeAnnexes.append(" - " + getLabelDocument(contentieux, langueDoc, "AQUILA_ANNEXE_DECISION"));
            listeAnnexes.append(" " + dateDecision + "\n");
        }
        if (annexeSommationChecked && !JadeStringUtil.isEmpty(dateSommation)) {
            listeAnnexes.append(" - " + getLabelDocument(contentieux, langueDoc, "AQUILA_ANNEXE_SOMMATION"));
            listeAnnexes.append(" " + dateSommation + "\n");
        }
        if (annexeCDPChecked) {
            listeAnnexes.append(" - " + getLabelDocument(contentieux, langueDoc, "AQUILA_ANNEXE_CDP") + "\n");
        }
        if (annexeExtraitArtChecked) {
            listeAnnexes.append(" - " + getLabelDocument(contentieux, langueDoc, "AQUILA_ANNEXE_EXTRAIT_ART") + "\n");
        }
        if (!JadeStringUtil.isBlank(annexeTexteLibre)) {
            listeAnnexes.append(annexeTexteLibre);
        }

        return listeAnnexes.toString();
    }

    /**
     * @return the annexeTexteLibreChecked
     */
    public boolean isAnnexeTexteLibreChecked() {
        return annexeTexteLibreChecked;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setAnnexeCDPChecked(boolean annexeChecked) {
        annexeCDPChecked = annexeChecked;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setAnnexeDecisionChecked(boolean annexeChecked) {
        annexeDecisionChecked = annexeChecked;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setAnnexeExtraitArtChecked(boolean annexeChecked) {
        annexeExtraitArtChecked = annexeChecked;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setAnnexeSommationChecked(boolean annexeChecked) {
        annexeSommationChecked = annexeChecked;
    }

    /**
     * @param annexeTexteLibre
     *            the annexeTexteLibre to set
     */
    public void setAnnexeTexteLibre(String annexeTexteLibre) {
        this.annexeTexteLibre = annexeTexteLibre;
    }

    /**
     * @param annexeTexteLibreChecked
     *            the annexeTexteLibreChecked to set
     */
    public void setAnnexeTexteLibreChecked(boolean annexeTexteLibreChecked) {
        this.annexeTexteLibreChecked = annexeTexteLibreChecked;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setDateDecision(String date) {
        dateDecision = date;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setDateSituationCompte(String date) {
        dateSituationCompte = date;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setDateSommation(String date) {
        dateSommation = date;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setFraisSupplementairesPoursuite(String frais) {
        fraisSupplementairesPoursuite = frais;
    }

}
