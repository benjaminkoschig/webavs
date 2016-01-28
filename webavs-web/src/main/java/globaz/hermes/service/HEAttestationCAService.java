package globaz.hermes.service;

import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.hermes.print.itext.HEDocumentRemiseAttestCA;
import globaz.hermes.service.dto.HEDataRemiseCA;
import globaz.jade.publish.client.JadePublishDocument;
import java.util.List;

/**
 * Service d'acces au module Hermes
 * 
 * @author jeremy.wermeille
 * @since 12 février 2014
 */
public class HEAttestationCAService {

    /**
     * Génération de l'attestation de remise CA grâce aux informations des exceptions
     * 
     * @param dataRemiseCA
     * @return
     * @throws Exception
     */
    public static HEDocumentRemiseAttestCA genererAttestationRemiseCa(HEDataRemiseCA dataRemiseCA, BSession session,
            String userEmail) throws Exception {

        HEDocumentRemiseAttestCA docRemiseCA = null;

        try {
            docRemiseCA = HEAttestationCAService.preparerParamsAttestationRemiseCA(dataRemiseCA, session, userEmail);
            docRemiseCA.executeProcess();
        } catch (Exception e) {
            throw new Exception("Error lors de la génération de l'attestation de Remise CA", e);
        }

        return docRemiseCA;
    }

    /**
     * Retourne la premiere attestation générer Retourn null sinon
     * 
     * @param exceptions
     * @return
     * @throws Exception
     */
    public static JadePublishDocument getFirstAttestationRemiseCa(HEDataRemiseCA dataRemiseCA, BSession session,
            String userEmail) throws Exception {

        HEDocumentRemiseAttestCA docRemiseCA = HEAttestationCAService.genererAttestationRemiseCa(dataRemiseCA, session,
                userEmail);
        JadePublishDocument publishDocument = null;

        if (docRemiseCA != null) {
            List<JadePublishDocument> attachedDocuments = docRemiseCA.getAttachedDocuments();
            try {
                // on ne prend qu'un document
                if ((attachedDocuments.size() > 0) && (attachedDocuments.get(0) != null)) {
                    publishDocument = attachedDocuments.get(0);
                }
            } catch (Exception e) {
                throw new Exception("no attached documents found for attestation CA " + e);
            }
        }

        return publishDocument;
    }

    /**
     * Préparation des parametres de la l'attestation de remise des CA grace aux informations des exceptions
     * 
     * @param exceptions
     * @return
     * @throws Exception
     */
    public static HEDocumentRemiseAttestCA preparerParamsAttestationRemiseCA(HEDataRemiseCA dataRemiseCA,
            BSession session, String userEmail) throws Exception {

        if (dataRemiseCA == null) {
            throw new NullPointerException("Data for remise ca is null");
        }
        if (session == null) {
            throw new NullPointerException("Session is null");
        }

        HEDocumentRemiseAttestCA docRemiseCa = new HEDocumentRemiseAttestCA();
        docRemiseCa.setSingle(dataRemiseCA.getSingle());

        // Nom
        docRemiseCa.setNom(dataRemiseCA.getNom());
        docRemiseCa.setPrenom(dataRemiseCA.getPrenom());

        // Date de naissance
        docRemiseCa.setDateNaiss(dataRemiseCA.getDateNaiss());

        // NSS
        docRemiseCa.setNnss(dataRemiseCA.getNnss());

        // Anne de cotisation
        docRemiseCa.setAnneeCot(dataRemiseCA.getAnneeCot());

        // Numéro d'afflié
        docRemiseCa.setNAffilie(dataRemiseCA.getNAffilie());

        docRemiseCa.setLangueSingle(dataRemiseCA.getLangueSingle());
        docRemiseCa.setPolitesse(dataRemiseCA.getPolitesse());

        docRemiseCa.setAdresse(dataRemiseCA.getAdresse());

        docRemiseCa.setSession(session);
        docRemiseCa.setEMailAddress(userEmail);
        docRemiseCa.setDocumentTitle(session.getLabel("HERMES_10058"));

        return docRemiseCa;

    }

    public static void soumettreGenerationAttestationRemiseCA(HEDataRemiseCA dataRemiseCA, BSession session,
            String userEmail) throws Exception {

        HEDocumentRemiseAttestCA docRemiseCA = null;

        try {
            docRemiseCA = HEAttestationCAService.preparerParamsAttestationRemiseCA(dataRemiseCA, session, userEmail);
            BProcessLauncher.start(docRemiseCA);
        } catch (Exception e) {
            throw new Exception("Error lors de la génération de l'attestation de Remise CA", e);
        }

    }

}
