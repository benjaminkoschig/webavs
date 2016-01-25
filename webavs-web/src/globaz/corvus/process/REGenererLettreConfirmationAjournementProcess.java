package globaz.corvus.process;

import globaz.corvus.application.REApplication;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.properties.REProperties;
import globaz.corvus.topaz.REConfirmationAjournementOO;
import globaz.jade.job.AbstractJadeJob;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import ch.globaz.corvus.domaine.DemandeRente;

public class REGenererLettreConfirmationAjournementProcess extends AbstractJadeJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adresseEmail;
    private String dateDuDocument;
    private DemandeRente demandeRente;
    private boolean miseEnGed;

    public REGenererLettreConfirmationAjournementProcess() {
        super();

        adresseEmail = null;
        dateDuDocument = null;
        demandeRente = null;
        miseEnGed = false;
    }

    public String getAdresseEmail() {
        return adresseEmail;
    }

    public String getDateDuDocument() {
        return dateDuDocument;
    }

    public DemandeRente getDemandeRente() {
        return demandeRente;
    }

    @Override
    public String getDescription() {
        return "Impression de la lettre pour la confirmation de l'ajournement";
    }

    @Override
    public String getName() {
        return "Impression de la lettre pour la confirmation de l'ajournement";
    }

    public boolean getMiseEnGed() {
        return miseEnGed;
    }

    @Override
    public void run() {

        try {
            PRTiersWrapper tiersRequerant = PRTiersHelper.getTiersParId(getSession(), demandeRente.getRequerant()
                    .getId().toString());

            // Chargement du catalogue de texte
            String codeIsoLangue = getSession().getCode(tiersRequerant.getLangue());
            codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);

            String adresseCourrier = PRTiersHelper.getAdresseCourrierFormateeRente(getSession(),
                    tiersRequerant.getIdTiers(), REApplication.CS_DOMAINE_ADRESSE_CORVUS, "", "", null, "");

            REConfirmationAjournementOO lettreRevocationOO = new REConfirmationAjournementOO();
            lettreRevocationOO.setAdresseCourrier(adresseCourrier);
            lettreRevocationOO.setAdresseEmail(adresseEmail);
            lettreRevocationOO.setCodeIsoLangue(codeIsoLangue);
            lettreRevocationOO.setDateDuDocument(dateDuDocument);
            lettreRevocationOO.setDemande(demandeRente);
            lettreRevocationOO.setSession(getSession());
            lettreRevocationOO.setFaireUneCopiePourAgenceCommunale(REProperties.COPIE_POUR_AGENCE_COMMUNALE
                    .getBooleanValue());
            lettreRevocationOO.setMiseEnGed(getMiseEnGed());
            lettreRevocationOO.run();
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }
    }

    public void setAdresseEmail(String adresseEmail) {
        this.adresseEmail = adresseEmail;
    }

    public void setDateDuDocument(String dateDuDocument) {
        this.dateDuDocument = dateDuDocument;
    }

    public void setDemandeRente(DemandeRente demande) {
        demandeRente = demande;
    }

    public void setMiseEnGed(boolean miseEnGed) {
        this.miseEnGed = miseEnGed;
    }
}
