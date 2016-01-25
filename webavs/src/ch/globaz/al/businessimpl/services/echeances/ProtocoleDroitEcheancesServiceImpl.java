package ch.globaz.al.businessimpl.services.echeances;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.HashMap;
import ch.globaz.al.business.constantes.ALConstEcheances;
import ch.globaz.al.business.constantes.ALConstProtocoles;
import ch.globaz.al.business.exceptions.model.tarif.ALEcheanceModelException;
import ch.globaz.al.business.exceptions.protocoles.ALProtocoleException;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.echeances.ProtocoleDroitEcheancesService;
import ch.globaz.al.businessimpl.documents.AbstractProtocole;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Implémentation du service de génération de protocole pour les échéances des droits (listes provisoires)
 * 
 * @author PTA
 * 
 */
public class ProtocoleDroitEcheancesServiceImpl extends AbstractProtocole implements ProtocoleDroitEcheancesService {

    /**
     * méthode qui implémente les données à lister
     * 
     * @param document
     *            document à imprimer
     * @param droits
     *            Results de la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    private void addListeEcheances(DocumentData document, ArrayList<DroitEcheanceComplexModel> droits)
            throws JadeApplicationException, JadePersistenceException {

        if (document == null) {
            throw new ALProtocoleException("ProtocoleDroitEcheanceServiceImpl#addListeEcheances : document is null");
        }

        if (droits == null) {
            throw new ALProtocoleException("ProtocoleDroitEcheanceServiceImpl#addListeEcheances : droits is null");
        }

        Collection table = new Collection("liste_droits_echeances");

        if (droits.size() != 0) {

            // JadeAbstractModel[] listeDroits = droits.getSearchResults();

            DataList entete = new DataList("entete");
            // entête affilie
            entete.addData("entete_affilie", this.getText("al.protocoles.echeances.liste.entete.affilie"));
            // entête nss
            entete.addData("entete_nss", this.getText("al.protocoles.echeances.liste.entete.nss"));
            // entête no dossier
            entete.addData("entete_noDossier", this.getText("al.protocoles.echeances.liste.entete.dossier"));
            // entete allocataire
            entete.addData("entete_allocataire", this.getText("al.protocoles.echeances.liste.entete.allocataire"));
            // entete enfant
            entete.addData("entete_enfant", this.getText("al.protocoles.echeances.liste.entete.enfant"));
            // entete dateNaissance
            entete.addData("entete_naissance", this.getText("al.protocoles.echeances.liste.entete.dateNaissance"));
            // entete dateEcheance
            entete.addData("entete_echeance", this.getText("al.protocoles.echeances.liste.entete.dateEcheance"));
            // entete motif
            entete.addData("entete_motif", this.getText("al.protocoles.echeances.liste.entete.motif"));

            table.add(entete);

            String affiliePrecedent = "";
            String nssPrecedent = "";
            String numDossierPrecedent = "";
            String nomPrenomAllocatairePrecedent = "";
            String idDroit = "";

            // parcourir la liste pour ajouter le données au documentdata
            for (int i = 0; i < droits.size(); i++) {
                DroitEcheanceComplexModel model = (droits.get(i));
                // contrôle ne pas prendre 2x le même droit
                if (!JadeStringUtil.equals(idDroit, model.getDroitModel().getIdDroit(), false)) {

                    DataList ligne = new DataList("colonne");
                    // traitement pour l'affilié
                    String affilie = model.getNumAffilie();
                    ligne.addData("col_affilie", (!affilie.equals(affiliePrecedent) ? affilie : ""));
                    affiliePrecedent = affilie;
                    // traitement du NSS
                    String nss = model.getNumNss();
                    ligne.addData("col_nss", (!nss.equals(nssPrecedent) ? nss : ""));
                    nssPrecedent = nss;
                    // traitement pour no dossier
                    String numDossier = model.getDroitModel().getIdDossier();
                    ligne.addData("col_noDossier", (!numDossier.equals(numDossierPrecedent) ? numDossier : ""));
                    numDossierPrecedent = numDossier;
                    // traitement de l'allocataire
                    String nomPrenomAllocataire = model.getNomAllocataire() + " " + model.getPrenomAllocataire();
                    ligne.addData("col_allocataire",
                            (!nomPrenomAllocataire.equals(nomPrenomAllocatairePrecedent) ? nomPrenomAllocataire : ""));
                    nomPrenomAllocatairePrecedent = nomPrenomAllocataire;
                    // traitement de l'enfant
                    ligne.addData("col_enfant", model.getNomEnfant() + " " + model.getPrenomEnfant());
                    // traitement naissance enfant
                    ligne.addData("col_naissance", model.getDateNaissanceEnfant());
                    // traitement de la date d'échéance
                    ligne.addData("col_echeance", model.getDroitModel().getFinDroitForcee());

                    ligne.addData(
                            "col_motif",
                            ALServiceLocator.getDroitEcheanceService().getLibelleMotif(model,
                                    JadeThread.currentLanguage()));

                    // // traitement motif échéance avec libellé cours
                    // ligne.addData("col_motif",
                    // JadeCodesSystemsUtil.getCode(model
                    // .getDroitModel().getMotifFin()));

                    idDroit = model.getDroitModel().getIdDroit();

                    table.add(ligne);
                }
            }
        }

        document.add(table);
    }

    @Override
    public DocumentData loadData(ArrayList<DroitEcheanceComplexModel> droits, String typeList, String dateEcheance)
            throws JadePersistenceException, JadeApplicationException {

        if (droits == null) {
            throw new ALEcheanceModelException("ProtocoleDroitEcheancesServiceImpl#loadData : droits is null");
        }

        if (!JadeStringUtil.equals(typeList, ALConstEcheances.LISTE_AUTRES_ECHEANCES, false)
                && !JadeStringUtil.equals(typeList, ALConstEcheances.LISTE_AVIS_ECHEANCES, false)) {

            throw new ALEcheanceModelException("ProtocoleDroitEcheancesServiceImpl#loadData : typeList " + typeList
                    + " is not a valid type list ");

        }

        // hashmap concernant les informations du protocole à setter
        HashMap<String, String> infos = new HashMap<String, String>();

        infos.put(ALConstProtocoles.INFO_PASSAGE, "-");
        infos.put(ALConstProtocoles.INFO_PROCESSUS, "al.protocoles.echeances.info.processus.val");
        infos.put(ALConstProtocoles.INFO_ECHEANCE, dateEcheance);
        if (JadeStringUtil.equals(ALConstEcheances.LISTE_AVIS_ECHEANCES, typeList, false)) {
            infos.put(ALConstProtocoles.INFO_TRAITEMENT, "al.protocoles.echeances.info.traitement.val");
        }
        if (JadeStringUtil.equals(ALConstEcheances.LISTE_AUTRES_ECHEANCES, typeList, false)) {
            infos.put(ALConstProtocoles.INFO_TRAITEMENT, "al.protocoles.echeances.info.traitementAutres.val");
        }

        // initialisation du document et des infos
        DocumentData document = init(infos);
        // chargement des données
        addListeEcheances(document, droits);

        return document;
    }

    @Override
    protected void setIdDocument(DocumentData document) throws JadeApplicationException {
        if (document == null) {
            throw new ALProtocoleException("ProtocoleDroitEcheancesServiceImpl#setIdDocument : document is null");
        }
        document.addData("AL_idDocument", "AL_protocoleEcheances");
    }
}
