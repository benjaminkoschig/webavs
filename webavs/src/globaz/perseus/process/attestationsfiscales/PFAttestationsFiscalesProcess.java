package globaz.perseus.process.attestationsfiscales;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.perseus.process.PFAbstractJob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import ch.globaz.perseus.business.constantes.CSEtatDemande;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.demande.DemandeSearchModel;
import ch.globaz.perseus.business.models.dossier.Dossier;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFAttestationsFiscalesProcess extends PFAbstractJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeAttestations = null;
    private String caisse = null;
    private String dateDocument = null;
    /**
     * La variable de l'adresse email est automatiquement setter à NULL si elle est nommée (eMailAddress) et doit donc
     * être renommée différement (mailAd) pour fonctionner correctement.
     */

    private String eMailAdress = null;
    private Set<String> groupDossier = new HashSet<String>();
    private String idDossier = null;
    private boolean isAttestationUnique = false;
    private boolean isSendToGed = false;
    private String mailAd = null;

    public String getAnneeAttestations() {
        return anneeAttestations;
    }

    public String getCaisse() {
        return caisse;
    }

    public String getDateDocument() {
        return dateDocument;
    }

    @Override
    public String getDescription() {
        return null;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getMailAd() {
        return mailAd;
    }

    @Override
    public String getName() {
        return null;
    }

    public boolean isAttestationUnique() {
        return isAttestationUnique;
    }

    public boolean isSendToGed() {
        return isSendToGed;
    }

    @Override
    protected void process() throws Exception {
        try {
            // appeler service sur dossier montant deja versee et si different ou plus grand que 0 à voir alors je crée
            // mondoc ou je passe une liste de dossier à traité
            ArrayList<Dossier> listeDossier = new ArrayList<Dossier>();
            HashMap<String, LinkedList<Demande>> listeDemandeParDossier = new HashMap<String, LinkedList<Demande>>();
            // Recherche de toutes les demandes pour un dossier si attsetation fiscale pour un tiers spécifique
            // si attestations pour tous les dossier, alors je recherche les demandes concernées
            DemandeSearchModel demandeSearchModel = new DemandeSearchModel();
            if (!JadeStringUtil.isEmpty(idDossier)) {
                demandeSearchModel.setForIdDossier(idDossier);
            }
            demandeSearchModel.setForCsEtatDemande(CSEtatDemande.VALIDE.getCodeSystem());
            // demandeSearchModel.setForCsCaisse(this.caisse);
            demandeSearchModel.setForDateDebut("01.01." + anneeAttestations);
            demandeSearchModel.setForDateFin("31.12." + anneeAttestations);
            demandeSearchModel.setWhereKey(DemandeSearchModel.WITH_ANNEE_VALABLE);
            demandeSearchModel.setOrderKey(DemandeSearchModel.ORDER_BY_DATE_DEBUT_ASC);
            demandeSearchModel = PerseusServiceLocator.getDemandeService().search(demandeSearchModel);
            for (JadeAbstractModel model : demandeSearchModel.getSearchResults()) {
                LinkedList<Demande> listeDemande = new LinkedList<Demande>();
                Demande demande = (Demande) model;

                if (JadeDateUtil.addDays(demande.getSimpleDemande().getDateDebut(), 1).equals(
                        demande.getSimpleDemande().getDateFin())) {
                    continue;
                }
                Demande dernierDemande = PerseusServiceLocator.getDemandeService().getDerniereDemande(
                        demande.getDossier().getId());
                if (!caisse.equals(dernierDemande.getSimpleDemande().getCsCaisse())) {
                    continue;
                }
                if (groupDossier.add(demande.getDossier().getId())) {
                    listeDossier.add(demande.getDossier());

                    listeDemande.addLast(demande);
                    listeDemandeParDossier.put(demande.getDossier().getId(), listeDemande);

                } else {
                    // Une demande a déjà été insérée pour ce dossier
                    Demande demandeListe = listeDemandeParDossier.get(demande.getDossier().getId()).getLast();

                    // Je test si les dates de débuts sont identiques
                    if (demande.getSimpleDemande().getDateDebut()
                            .equals(demandeListe.getSimpleDemande().getDateDebut())) {
                        // Ne prendre que la plus récente à l'aide du champ DateTimeDecisionValidation
                        if (Double.parseDouble(demande.getSimpleDemande().getDateTimeDecisionValidation()) > Double
                                .parseDouble(demandeListe.getSimpleDemande().getDateTimeDecisionValidation())) {
                            // je supprime l'ancienne demande et ajoute la nouvelle
                            listeDemandeParDossier.get(demande.getDossier().getId()).removeLast();
                            listeDemandeParDossier.get(demande.getDossier().getId()).addLast(demande);
                        }
                    } else {
                        // Si ce n'est pas le cas, j'aoute la nouvelle demande à ma liste
                        listeDemandeParDossier.get(demande.getDossier().getId()).addLast(demande);
                    }
                }
            }

            if (demandeSearchModel.getSize() == 0) {
                // Si aucune demande trouvée (utilisé pour l'attestation fiscale spécifique à un tiers)
                // Message d'avertissement pour indiquer qu'il n'y a pas d'attestation à générée
                Dossier dossier = new Dossier();
                dossier.setId(idDossier);
                dossier = (Dossier) JadePersistenceManager.read(dossier);
                String[] param = new String[1];
                param[0] = dossier.getDemandePrestation().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel()
                        + " " + dossier.getDemandePrestation().getPersonneEtendue().getTiers().getDesignation1() + " "
                        + dossier.getDemandePrestation().getPersonneEtendue().getTiers().getDesignation2();

                JadeThread.logWarn(this.getClass().getName(), "perseus.attestationsfiscales.generer.pasattestation",
                        param);
            } else {

                // AttestationsFiscalesBuilder builder = new AttestationsFiscalesBuilder();
                // builder.setAnneeAttestations(this.anneeAttestations);
                // builder.setCaisse(this.caisse);
                // builder.setDateDocument(this.dateDocument);
                // builder.setListeDossier(listeDossier);
                // builder.setMailAd(this.mailAd);
                // builder.setSendToGed(this.isSendToGed);
                // builder.setListeDemandeParDossier(listeDemandeParDossier);
                // ArrayList<JadePrintDocumentContainer> container = builder.build();

                // this.createDocuments(container.get(0));
                // this.createDocuments(container.get(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
            JadeThread.logError(this.getClass().getName(),
                    "Erreur : " + System.getProperty("line.separator") + e.toString());

        }

        Iterator test = JadeLogger.getInstance().logsIterator();
        while (test.hasNext()) {
            Object o = test.next();
            JadeThread.logError(this.getClass().getName(), "/n" + "Erreur technique (jadeLogger : " + o.toString());
        }

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            JadeBusinessMessage[] messages = JadeThread.logMessages();
            for (int i = 0; i < messages.length; i++) {
                getLogSession().addMessage(messages[i]);
            }
            List<String> email = new ArrayList<String>();
            email.add(mailAd);
            this.sendCompletionMail(email);
        } else if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN)) {
            JadeBusinessMessage[] messages = JadeThread.logMessages();
            for (int i = 0; i < messages.length; i++) {
                getLogSession().addMessage(messages[i]);
            }
            List<String> email = new ArrayList<String>();
            email.add(mailAd);
            this.sendCompletionMail(email);
        }
    }

    public void setAnneeAttestations(String anneeAttestations) {
        this.anneeAttestations = anneeAttestations;
    }

    public void setAttestationUnique(boolean isAttestationUnique) {
        this.isAttestationUnique = isAttestationUnique;
    }

    public void setCaisse(String caisse) {
        this.caisse = caisse;
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setMailAd(String mailAd) {
        this.mailAd = mailAd;
    }

    public void setSendToGed(boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

}
