package globaz.orion.process;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.pdf.JadePdfUtil;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.client.JadePublishServerFacade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.message.JadePublishDocumentMessage;
import globaz.orion.utils.AclComparator;
import globaz.orion.utils.AclComparatorSuccursale;
import globaz.pavo.db.acl.CIAnnonceCollaborateurService;
import globaz.pavo.db.acl.CITreatAclEnCoursResult;
import globaz.pavo.db.acl.CITreatAclSaisieResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import ch.globaz.orion.business.models.acl.Acl;
import ch.globaz.orion.businessimpl.services.acl.AclServiceImpl;
import ch.globaz.xmlns.eb.acl.AclStatutEnum;
import ch.globaz.xmlns.eb.acl.EBACLException_Exception;

/**
 * Processus de traitement des annonces de collaborateurs en provenance d'EBusiness
 * 
 * @author BJO
 * 
 */
public class EBTreatAcl extends EBAbstractJadeJob {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String ATTESTATIONS_ASSURANCES_DOC_NAME = "ATT_ASS.pdf";

    @Override
    public String getDescription() {
        return "Traitement des annonces de collaborateurs Ebusiness";
    }

    @Override
    public String getName() {
        return "Traitement des annonces de collaborateurs Ebusiness";
    }

    /**
     * Imprime sous forme d'une liste Excel le tableau d'annonces de collaborateurs passé en paramètre et retourne le
     * pathfile
     * 
     * @param acl
     * @throws Exception
     */
    private String imprimerListeAcl(Acl[] acl) throws Exception {
        if ((acl == null) || (acl.length < 1)) {
            throw new Exception("Unable to print the list because the table is null or empty");
        }
        // Conversion du tableau en array pour tri par statut
        ArrayList<Acl> aclArray = new ArrayList<Acl>(Arrays.asList(acl));
        // Il faut trier les annonces selon le comparator
        Collections.sort(aclArray, new AclComparator());
        acl = null;
        // Après le tri, on en refait un tableau
        acl = aclArray.toArray(new Acl[aclArray.size()]);
        EBImprimerListeAcl impressionListe = new EBImprimerListeAcl(acl);
        impressionListe.setSession(getSession());
        impressionListe._executeProcess();

        List attachedDocuments = impressionListe.getAttachedDocuments();
        if ((attachedDocuments.size() > 0) && (attachedDocuments.get(0) != null)) {
            JadePublishDocument doc = (JadePublishDocument) attachedDocuments.get(0);
            String docLocation = doc.getDocumentLocation();
            if (!JadeStringUtil.isBlank(docLocation)) {
                return docLocation;
            } else {
                throw new Exception("docLocation is null!");
            }
        } else {
            throw new Exception("No attachedDocuments");
        }
    }

    @Override
    protected void process() throws Exception {

        HashMap<Acl, String> attestationAssurancePathList = new HashMap<Acl, String>();
        ArrayList<Acl> arrayListSorting = new ArrayList<Acl>();
        // System.out.println("Lancement du process de traitement des ACL");

        // ###### 1ERE PHASE DU TRAITEMENT (TRAITEMENT DES ANNONCES AYANT LE STATUT EN COURS) ######
        // System.out.println("1ère phase : Traitement des ACL ayant le statut en cours");
        // Récupération des acl en statut "en cours"
        Acl[] aclEnCours = AclServiceImpl.listACl(AclStatutEnum.EN_COURS, getSession());
        CITreatAclEnCoursResult treatAclEnCoursResult = new CITreatAclEnCoursResult();

        // Traitement des ACL ayant le statut "En cours"
        for (int i = 0; i < aclEnCours.length; i++) {
            try {
                // Vérifier l'état de l'ARC pour l'ACL (si la centrale à répondue à l'annonce)
                treatAclEnCoursResult = CIAnnonceCollaborateurService.treatAclEnCours(getSession()
                        .getCurrentThreadTransaction(), aclEnCours[i].getIdAnnonceCollaborateur(), aclEnCours[i]
                        .getNumeroAssure(), aclEnCours[i].getNouveauNumero(), aclEnCours[i].getDateEngagement(),
                        aclEnCours[i].getDuplicata(), aclEnCours[i].getNumeroAffilie(), aclEnCours[i].getNoEmploye(),
                        aclEnCours[i].getNoSuccursale());

                // Si une attestation pour la CIException a été imprimée
                if (!JadeStringUtil.isBlank(treatAclEnCoursResult.getAttestationAssuranceLocation())) {
                    attestationAssurancePathList.put(aclEnCours[i],
                            treatAclEnCoursResult.getAttestationAssuranceLocation());
                    arrayListSorting.add(aclEnCours[i]);
                }
            } catch (Exception e) {
                // log et met le thread en erreur
                JadeThread.logError(this.getClass().toString(), "Unable to treat the ACL en cours " + e.getMessage());
            } finally {
                if (getSession().hasErrors()
                        || getSession().getCurrentThreadTransaction().hasErrors()
                        || threadOnError()
                        || treatAclEnCoursResult.getArcStatut().equals(
                                CIAnnonceCollaborateurService.ARC_STATUT_PROBLEME)) {
                    JadeThread.rollbackSession();

                    // Mise à jour du statut de l'annonce ("En cours" -> "Problème")
                    AclServiceImpl.changeStatus(Integer.parseInt(aclEnCours[i].getIdAnnonceCollaborateur()),
                            AclStatutEnum.PROBLEME, getSession());

                    // Suppression des logs d'erreur
                    JadeThread.logClear();
                    getSession().getCurrentThreadTransaction().clearErrorBuffer();
                    getSession().getCurrentThreadTransaction().clearWarningBuffer();
                    getSession().getErrors();// vide également le buffer
                    getSession().getWarnings();// vide également le buffer
                } else {
                    JadeThread.commitSession();
                    if (treatAclEnCoursResult.getArcStatut().equals(CIAnnonceCollaborateurService.ARC_STATUT_TERMINE)) {
                        // Mise à jour du statut de l'annonce ("En cours" -> "Terminé")
                        AclServiceImpl.changeStatus(Integer.parseInt(aclEnCours[i].getIdAnnonceCollaborateur()),
                                AclStatutEnum.TERMINE, getSession());
                    }
                }
            }
        }

        // ###### 2EME PHASE DU TRAITEMENT (TRAITEMENT DES ANNONCES AYANT LE STATUT SAISIE) ######
        // System.out.println("2ème phase : Traitement des ACL ayant le statut saisie");
        // Récupération des acl en statut "saisie"
        Acl[] aclSaisie = AclServiceImpl.listACl(AclStatutEnum.SAISIE, getSession());

        CITreatAclSaisieResult treatAclSaisieResult = new CITreatAclSaisieResult();

        // Traitement des ACL ayant le statut "Saisie"
        for (int i = 0; i < aclSaisie.length; i++) {
            try {
                treatAclSaisieResult = CIAnnonceCollaborateurService.treatAclSaisie(getSession()
                        .getCurrentThreadTransaction(), aclSaisie[i].getIdAnnonceCollaborateur(), aclSaisie[i]
                        .getNumeroAssure(), aclSaisie[i].getDateEngagement(), aclSaisie[i].getDuplicata(), aclSaisie[i]
                        .getNumeroAffilie(), aclSaisie[i].getNoEmploye(), aclSaisie[i].getNoSuccursale());

                // Récupération des paramètres retournés par treatAclSaisie
                if (!JadeStringUtil.isBlank(treatAclSaisieResult.getNnssFindInNssra())) {
                    aclSaisie[i].setNumeroAssure(treatAclSaisieResult.getNnssFindInNssra());
                }
                if (!JadeStringUtil.isBlank(treatAclSaisieResult.getTypeArc())) {
                    aclSaisie[i].setTypeArc(treatAclSaisieResult.getTypeArc());
                }
                if (!JadeStringUtil.isBlank(treatAclSaisieResult.getNomPrenom())) {
                    aclSaisie[i].setNomPrenom(treatAclSaisieResult.getNomPrenom());
                }
                if (!JadeStringUtil.isBlank(treatAclSaisieResult.getNationnalite())) {
                    aclSaisie[i].setNationnalite(treatAclSaisieResult.getNationnalite());
                }
                if (!JadeStringUtil.isBlank(treatAclSaisieResult.getDateNaissance())) {
                    aclSaisie[i].setDateNaissance(treatAclSaisieResult.getDateNaissance());
                }

                // Si une attestation pour la CIException a été imprimée
                if (!JadeStringUtil.isBlank(treatAclSaisieResult.getAttestationAssuranceLocation())) {
                    // Un document pour l'exception à été généré
                    attestationAssurancePathList.put(aclSaisie[i],
                            treatAclSaisieResult.getAttestationAssuranceLocation());
                    arrayListSorting.add(aclSaisie[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // log et met le thread en erreur
                JadeThread.logError(this.getClass().toString(), "Unable to treat the ACL saisie " + e.getMessage());
            } finally {
                if (getSession().hasErrors() || getSession().getCurrentThreadTransaction().hasErrors()
                        || threadOnError() || !treatAclSaisieResult.isSuccess()) {
                    JadeThread.rollbackSession();

                    // Mise à jour du statut de l'annonce ("Saisie" -> "Problème")
                    AclServiceImpl.changeStatus(Integer.parseInt(aclSaisie[i].getIdAnnonceCollaborateur()),
                            AclStatutEnum.PROBLEME, getSession());
                    aclSaisie[i].setStatut(getSession().getLabel("ACL_STATUT_PROBLEME"));
                    System.out.println(getSession().getCurrentThreadTransaction().getErrors());

                    // Suppression des logs d'erreur
                    JadeThread.logClear();
                    getSession().getCurrentThreadTransaction().clearErrorBuffer();
                    getSession().getCurrentThreadTransaction().clearWarningBuffer();
                    getSession().getErrors();// vide également le buffer
                    getSession().getWarnings();// vide également le buffer
                } else {
                    JadeThread.commitSession();
                    if (!JadeStringUtil.isBlank(treatAclSaisieResult.getAttestationAssuranceLocation())) {
                        // Mise à jour du statut de l'annonce ("Saisie" -> "Terminé")
                        AclServiceImpl.changeStatus(Integer.parseInt(aclSaisie[i].getIdAnnonceCollaborateur()),
                                AclStatutEnum.TERMINE, getSession());
                        aclSaisie[i].setStatut(getSession().getLabel("ACL_STATUT_TERMINE"));
                    } else {
                        // Mise à jour du statut de l'annonce ("Saisie" -> "En cours")
                        AclServiceImpl.changeStatus(Integer.parseInt(aclSaisie[i].getIdAnnonceCollaborateur()),
                                AclStatutEnum.EN_COURS, getSession());
                        aclSaisie[i].setStatut(getSession().getLabel("ACL_STATUT_ENCOURS"));
                        try {
                            AclServiceImpl.updateNouveauNumeroAvs(
                                    Integer.parseInt(aclSaisie[i].getIdAnnonceCollaborateur()),
                                    aclSaisie[i].getNumeroAssure(), getSession());
                        } catch (EBACLException_Exception e) {
                            // si update pas possible, tans pis informatif, ne pas arrêter le processus
                        }
                    }
                }
            }
        }

        // ###### 3EME PHASE DU TRAITEMENT (IMPRESSION DES DOCUMENTS ET PUBLICATION) ######
        // Impression de la liste récapitulative des ACL traitées (saisies)
        if (aclSaisie.length > 0) {
            try {
                // Impression de la liste
                String AclExcelListPath = imprimerListeAcl(aclSaisie);
                // Publication du fichier excel
                JadePublishDocumentInfo docInfoAclExcelList = new JadePublishDocumentInfo();
                docInfoAclExcelList.setOwnerEmail(getSession().getUserEMail());
                docInfoAclExcelList.setArchiveDocument(false);
                docInfoAclExcelList.setPublishDocument(true);
                docInfoAclExcelList.setDocumentTitle(getSession().getLabel("LISTE_ACL_TITRE"));
                docInfoAclExcelList.setDocumentSubject(getSession().getLabel("LISTE_ACL_DOC_SUJET"));
                JadePublishDocument publishDocAclExcelList = new JadePublishDocument(AclExcelListPath,
                        docInfoAclExcelList);
                JadePublishServerFacade.publishDocument(new JadePublishDocumentMessage(publishDocAclExcelList));
            } catch (Exception e) {
                JadeThread.logError(this.getClass().toString(),
                        "Unable to print the list of ACL (Excel) " + e.getMessage());
            }
        }

        // Création du fichier fusionné des attestations d'assurances
        if (attestationAssurancePathList.size() > 0) {
            // Tableau contenant les règles pour la fusion des fichiers
            String pdfMergeRules[] = new String[1];
            pdfMergeRules[0] = JadePdfUtil.REPLACE_FILE;
            // Récupération des chemins sur les fichiers générés
            // Trier les Acl => pour ça, on doit passer par une arrayList

            Collections.sort(arrayListSorting, new AclComparatorSuccursale());
            String attestationAssurancePathTab[] = new String[attestationAssurancePathList.size()];

            Iterator<Acl> aclIterator = arrayListSorting.iterator();
            ArrayList<String> resultPath = new ArrayList<String>();
            while (aclIterator.hasNext()) {
                resultPath.add(attestationAssurancePathList.get(aclIterator.next()));
            }
            attestationAssurancePathTab = resultPath.toArray(attestationAssurancePathTab);
            // Création du docuement fusionné
            JadePdfUtil.merge(Jade.getInstance().getPersistenceDir() + EBTreatAcl.ATTESTATIONS_ASSURANCES_DOC_NAME,
                    attestationAssurancePathTab, pdfMergeRules);

            // Publication du fichier fusionné des attestations d'assurances
            try {
                JadePublishDocumentInfo docInfoAttestationsAssurances = new JadePublishDocumentInfo();
                docInfoAttestationsAssurances.setOwnerEmail(getSession().getUserEMail());
                docInfoAttestationsAssurances.setArchiveDocument(false);
                docInfoAttestationsAssurances.setPublishDocument(true);
                docInfoAttestationsAssurances.setDocumentTitle(getSession().getLabel("ATT_ASSURANCES_ACL_TITRE"));
                docInfoAttestationsAssurances.setDocumentSubject(getSession().getLabel("ATT_ASSURANCES_ACL_DOC_SUJET"));
                JadePublishDocument publishDocAttestationsAssurances = new JadePublishDocument(Jade.getInstance()
                        .getPersistenceDir() + EBTreatAcl.ATTESTATIONS_ASSURANCES_DOC_NAME,
                        docInfoAttestationsAssurances);
                JadePublishServerFacade
                        .publishDocument(new JadePublishDocumentMessage(publishDocAttestationsAssurances));
            } catch (Exception e) {
                JadeThread.logError(this.getClass().toString(),
                        "Unable to print the document Attestation assurance (pdf) " + e.getMessage());
            }
        }
        // System.out.println("Fin du process de traitement des ACL");
    }
}
