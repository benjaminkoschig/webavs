package ch.globaz.al.businessimpl.echeances;

import globaz.globall.parameters.FWParametersSystemCode;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.exceptions.model.tarif.ALEcheanceModelException;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.documents.AbstractDocument;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * classe abstraite impl�mentant des m�thodes communes aux listes d'�ch�ances communes aux allocataires direct et
 * indirect
 * 
 * @author PTA
 * 
 */
public abstract class EcheancesAllocataireAbstract extends AbstractDocument implements EcheancesAllocataire {

    /**
     * D�finit le tiers utilis� comme destinataire de l'avis d'�ch�ance
     */
    private String idTiersDestinataire = null;
    private String langueDocument = null;

    public String getIdTiersDestinataire() {
        return idTiersDestinataire;
    }

    /**
     * m�thode qui retourne l'id de la langue des codeSyteme en fonctin de langue (code iso) pass�e en param�tre
     * 
     * @return
     */
    protected String getLangueCodeSystem(String langueCodeIso) throws JadeApplicationException,
            JadePersistenceException {
        // contr�le du param�tre
        if (!JadeStringUtil.isBlank(langueCodeIso)) {
            if (!JadeStringUtil.equals(langueCodeIso, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                    && !JadeStringUtil.equals(langueCodeIso, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                    && !JadeStringUtil.equals(langueCodeIso, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
                throw new ALEcheanceModelException("EcheancesListeAllocataireAbstract#getLangueCodeSystem: language  "
                        + langueCodeIso + " is not  valid ");
            }
        }
        String idLangue = null;

        HashMap<String, String> langueCodeIsoIdCodeSy = new HashMap<String, String>();
        langueCodeIsoIdCodeSy.put(ALConstLangue.LANGUE_ISO_FRANCAIS, ALConstLangue.LANGUE_ID_FRANCAIS);
        langueCodeIsoIdCodeSy.put(ALConstLangue.LANGUE_ISO_ALLEMAND, ALConstLangue.LANGUE_ID_ALLEMAND);
        langueCodeIsoIdCodeSy.put(ALConstLangue.LANGUE_ISO_ITALIEN, ALConstLangue.LANGUE_ID_ITALIEN);

        idLangue = langueCodeIsoIdCodeSy.get(langueCodeIso);
        if (JadeStringUtil.isBlank(idLangue)) {
            return ALConstLangue.LANGUE_ID_FRANCAIS;
        }

        else {

            return idLangue;
        }

    }

    @Override
    public String getLangueDocument() {
        return langueDocument;
    }

    @Override
    public DocumentData loadData(ArrayList<DroitEcheanceComplexModel> droits, String nss, String idTiersAllocataire,
            String numDossier, String numAffilie, String nomAlloc, String prenomAlloc, String titre,
            String idTiersBenficiaire) throws JadePersistenceException, JadeApplicationException {

        // document � cr�er
        DocumentData document = new DocumentData();
        // langue pour les documents
        String langueDocument = null;
        // Si langue reprise langue affili�
        DossierComplexModel dossier = ALServiceLocator.getDossierComplexModelService().read(numDossier);

        if (dossier.getAllocataireComplexModel().getAllocataireModel().getLangueAffilie()) {
            this.langueDocument = ALServiceLocator.getLangueAllocAffilieService().langueTiersAffilie(numAffilie);
        }// si reprise langue allocataire
        else {
            this.langueDocument = ALServiceLocator.getLangueAllocAffilieService().langueTiersAlloc(idTiersAllocataire,
                    numAffilie);
        }

        setIdDocument(document);

        // infos relatives � l'allocataire
        setInfos(document, numDossier, numAffilie, nss, nomAlloc, prenomAlloc, titre, idTiersBenficiaire,
                idTiersAllocataire, this.langueDocument);

        // donn�es de l'allocataire
        setTable(document, droits, numDossier, this.langueDocument);

        // infos textes
        setTexte(document, titre, this.langueDocument);

        return document;
    }

    @Override
    public DocumentData loadData(ArrayList<DroitEcheanceComplexModel> droits, String nss, String idTiersAllocataire,
                                 String numDossier, String numAffilie, String numContribuable, String nomAlloc, String prenomAlloc, String titre,
                                 String idTiersBenficiaire) throws JadePersistenceException, JadeApplicationException {

        // document � cr�er
        DocumentData document = new DocumentData();
        // langue pour les documents
        String langueDocument = null;
        // Si langue reprise langue affili�
        DossierComplexModel dossier = ALServiceLocator.getDossierComplexModelService().read(numDossier);

        if (dossier.getAllocataireComplexModel().getAllocataireModel().getLangueAffilie()) {
            this.langueDocument = ALServiceLocator.getLangueAllocAffilieService().langueTiersAffilie(numAffilie);
        }// si reprise langue allocataire
        else {
            this.langueDocument = ALServiceLocator.getLangueAllocAffilieService().langueTiersAlloc(idTiersAllocataire,
                    numAffilie);
        }

        setIdDocument(document);

        // infos relatives � l'allocataire
        setInfos(document, numDossier, numAffilie, numContribuable, nss, nomAlloc, prenomAlloc, titre, idTiersBenficiaire,
                idTiersAllocataire, this.langueDocument);

        // donn�es de l'allocataire
        setTable(document, droits, numDossier, this.langueDocument);

        // infos textes
        setTexte(document, titre, this.langueDocument);

        return document;
    }

    public void setIdTiersDestinataire(String idTiersDestinataire) {
        this.idTiersDestinataire = idTiersDestinataire;
    }

    /**
     * 
     * @param document
     *            � imprimer
     * @param titre
     *            politesse de l'allocataire
     * @param noDossier
     *            identifiant du dossier
     * @param noAffilie
     *            num�ro de l'affili�
     * @param noNSS
     *            num�ro d'assur�
     * @param nomAlloc
     *            nom de l'allocataire
     * @param prenomAlloc
     *            Pr�nom de l'allocataire
     * @param idTiersBeneficiaire
     *            identifiant du tiers b�n�ficiaire
     * @param idTiersAllocataire
     *            identifiant du tiers allocataire
     * @param langueDocument
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    protected void setInfos(DocumentData document, String noDossier, String noAffilie, String noNSS, String nomAlloc,
            String prenomAlloc, String titre, String idTiersBeneficiaire, String idTiersAllocataire,
            String langueDocument) throws JadeApplicationException, JadePersistenceException {

        // v�rification des param�tres

        if (document == null) {
            throw new ALEcheanceModelException("EcheancesListeAllocataireAbstract#setInfos : document is null");
        }

        if (noDossier == null) {
            throw new ALEcheanceModelException("EcheancesListeAllocataireAbstract#setInfos : noDossier is null");

        }
        if (noAffilie == null) {
            throw new ALEcheanceModelException("EcheancesListeAllocataireAbstract#setInfos : noAffilie is null");

        }
        if (noNSS == null) {
            throw new ALEcheanceModelException("EcheancesListeAllocataireAbstract#setInfos : noNss is null");

        }
        if (nomAlloc == null) {
            throw new ALEcheanceModelException("EcheancesListeAllocataireAbstract#setInfos : nomAlloc is null");

        }
        if (prenomAlloc == null) {
            throw new ALEcheanceModelException("EcheancesListeAllocataireAbstract#setInfos : prenomAlloc is null");

        }
        if (idTiersAllocataire == null) {
            throw new ALEcheanceModelException(
                    "EcheancesListeAllocataireAbstract#setInfos : idTiersAllocataire is null");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALEcheanceModelException("EcheancesListeAllocataireAbstract#setInfos: language  "
                    + langueDocument + " is not  valid ");
        }

        // num�ro de dossier
        document.addData("droitEcheanceDossier_no_label",
                this.getText("al.echeances.info.numDossier.libelle", langueDocument));
        document.addData("droitEcheanceDossier_no_val", noDossier);
        // num�ro de l'affili�
        document.addData("droitEcheanceAffilieInd_no_label",
                this.getText("al.echeances.info.numAffilie.libelle", langueDocument));
        document.addData("droitEcheanceAffilieInd_no_val", noAffilie);
        // nss
        document.addData("droitEcheanceAlloc_nss_label",
                this.getText("al.echeances.info.numNss.libelle", langueDocument));
        document.addData("droitEcheanceAlloc_nss_val", noNSS);
        // concerne
        document.addData("droitEcheanceAllocataire_concerne_label",
                this.getText("al.echeances.info.concerne.libelle", langueDocument));
        document.addData("droitEcheanceAllocataire_concerne_val",
                this.getText("al.echeances.info.concerne.val", langueDocument));

    }

    /**
     *
     * @param document
     *            � imprimer
     * @param titre
     *            politesse de l'allocataire
     * @param noDossier
     *            identifiant du dossier
     * @param noAffilie
     *            num�ro de l'affili�
     * @param noNSS
     *            num�ro d'assur�
     * @param numContribuable
     *            num�ro de contribuable
     * @param nomAlloc
     *            nom de l'allocataire
     * @param prenomAlloc
     *            Pr�nom de l'allocataire
     * @param idTiersBeneficiaire
     *            identifiant du tiers b�n�ficiaire
     * @param idTiersAllocataire
     *            identifiant du tiers allocataire
     * @param langueDocument
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    protected void setInfos(DocumentData document, String noDossier, String noAffilie, String numContribuable, String noNSS, String nomAlloc,
                            String prenomAlloc, String titre, String idTiersBeneficiaire, String idTiersAllocataire,
                            String langueDocument) throws JadeApplicationException, JadePersistenceException {

        // v�rification des param�tres

        if (document == null) {
            throw new ALEcheanceModelException("EcheancesListeAllocataireAbstract#setInfos : document is null");
        }

        if (noDossier == null) {
            throw new ALEcheanceModelException("EcheancesListeAllocataireAbstract#setInfos : noDossier is null");

        }
        if (noAffilie == null) {
            throw new ALEcheanceModelException("EcheancesListeAllocataireAbstract#setInfos : noAffilie is null");

        }
        if (numContribuable == null) {
            throw new ALEcheanceModelException("EcheancesListeAllocataireAbstract#setInfos : numContribuable is null");

        }
        if (noNSS == null) {
            throw new ALEcheanceModelException("EcheancesListeAllocataireAbstract#setInfos : noNss is null");

        }
        if (nomAlloc == null) {
            throw new ALEcheanceModelException("EcheancesListeAllocataireAbstract#setInfos : nomAlloc is null");

        }
        if (prenomAlloc == null) {
            throw new ALEcheanceModelException("EcheancesListeAllocataireAbstract#setInfos : prenomAlloc is null");

        }
        if (idTiersAllocataire == null) {
            throw new ALEcheanceModelException(
                    "EcheancesListeAllocataireAbstract#setInfos : idTiersAllocataire is null");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALEcheanceModelException("EcheancesListeAllocataireAbstract#setInfos: language  "
                    + langueDocument + " is not  valid ");
        }

        // num�ro de dossier
        document.addData("droitEcheanceDossier_no_label",
                this.getText("al.echeances.info.numDossier.libelle", langueDocument));
        document.addData("droitEcheanceDossier_no_val", noDossier);
        // num�ro de l'affili�
        document.addData("droitEcheanceAffilieInd_no_label",
                this.getText("al.echeances.info.numAffilie.libelle", langueDocument));
        document.addData("droitEcheanceAffilieInd_no_val", noAffilie);
        // nss
        document.addData("droitEcheanceAlloc_nss_label",
                this.getText("al.echeances.info.numNss.libelle", langueDocument));
        document.addData("droitEcheanceAlloc_nss_val", noNSS);

        //num Contribuable
        if (!numContribuable.isEmpty()){
            document.addData("droitEcheanceAlloc_numContribuable_label",
                    this.getText("al.echeances.info.numContribuable.libelle", langueDocument));
            document.addData("droitEcheanceAlloc_numContribuable_val", numContribuable);
        }
        // concerne
        document.addData("droitEcheanceAllocataire_concerne_label",
                this.getText("al.echeances.info.concerne.libelle", langueDocument));
        document.addData("droitEcheanceAllocataire_concerne_val",
                this.getText("al.echeances.info.concerne.val", langueDocument));

    }

    /**
     * charge dans le document les donn�es li�es aux droits arrivant � �ch�ance
     * 
     * @param document
     *            document � charger
     * @param droits
     *            liste des droits
     * @param noDossier
     *            num�ro de dossier
     * @param langueDocument
     *            langue � utiliser pour le document
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    protected void setTable(DocumentData document, ArrayList droits, String noDossier, String langueDocument)
            throws JadeApplicationException, JadePersistenceException {
        // v�rification du param�tre
        if (document == null) {
            throw new ALEcheanceModelException("EcheancesListeAllocataireAbstract#setTable : document is null");
        }

        if (droits == null) {
            throw new ALEcheanceModelException("EcheancesListeAllocataireAbstract#setTable : droits is null");
        }
        if (JadeNumericUtil.isEmptyOrZero(noDossier)) {
            throw new ALEcheanceModelException("EcheancesListeAllocataireAbstract#setTable: noDossier" + noDossier
                    + " is not valid");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALEcheanceModelException("EcheancesListeAllocataireAbstract#setTable: language  "
                    + langueDocument + " is not  valid ");
        }

        Collection table = new Collection("liste_droits_echeances_allocataire");

        if (droits.size() != 0) {

            DataList entete = new DataList("entete");

            // ent�te Enfant/NSS

            entete.addData("entete_enfant", this.getText("al.echeances.listeAlloc.entete.enfant", langueDocument));

            // ent�te Enfant/N� le

            entete.addData("entete_naissance", this.getText("al.echeances.listeAlloc.entete.naissance", langueDocument));

            // entete Echeance
            entete.addData("entete_dateEcheance",
                    this.getText("al.echeances.listeAlloc.entete.echeance", langueDocument));

            // entete motif
            entete.addData("entete_motif", this.getText("al.echeances.listeAlloc.entete.motif", langueDocument));

            table.add(entete);

            Iterator it = droits.iterator();

            while (it.hasNext()) {
                // traitement de l'enfant
                DroitEcheanceComplexModel droitsEcheance = (DroitEcheanceComplexModel) it.next();
                DataList ligne = new DataList("colonne");

                ligne.addData("col_enfant", droitsEcheance.getNomEnfant() + " " + droitsEcheance.getPrenomEnfant()
                        + ", " + droitsEcheance.getNumNssEnfant());

                // traitement de l'enfant naissance
                ligne.addData("col_naissance", droitsEcheance.getDateNaissanceEnfant());

                // traitement de l'enfant �ch�ance
                ligne.addData("col_dateEcheance", droitsEcheance.getDroitModel().getFinDroitForcee());

                // traitement du motif de l'�ch�ance
                ligne.addData("col_motif",
                        ALServiceLocator.getDroitEcheanceService().getLibelleMotif(droitsEcheance, langueDocument));

                table.add(ligne);

            }
        }
        document.add(table);
    }

    /**
     * M�thode qui set les diff�rentes
     * 
     * @param document
     *            Document sur lequel il faut ajouter les copies
     * @param listeCopies
     *            list des copies
     * @param idTiersLie
     *            identifiant du tiers li�e
     * @param typeActivite
     *            type d'activit�
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    protected void setTableCopie(DocumentData document, DroitEcheanceComplexModel droit, String langueDocument)
            throws JadeApplicationException, JadePersistenceException {

        // contr�le des param�tres
        if (document == null) {
            throw new ALEcheanceModelException("EcheancesAllocataireAbstract#setTableCopie : document is null");
        }
        if (droit == null) {
            throw new ALEcheanceModelException("EcheancesAllocataireAbstract#setTableCopie: copie is null");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALEcheanceModelException("EcheancesAllocataireAbstract#setTableCopie: language  "
                    + langueDocument + " is not  valid ");
        }

        Collection table = new Collection("echeance_Avis_A");
        DataList list = new DataList("colonne");
        if (ALCSDossier.ACTIVITE_NONACTIF.equals(droit.getActiviteAllocataire())
                && ALServiceLocator.getAffiliationBusinessService().requireDocumentLienAgenceCommunale()) {
            String idTiersAgenceCommunale = droit.getTiersLiaisonComplexModel().getTiersLie().getIdTiers();

            if (!JadeStringUtil.isBlank(idTiersAgenceCommunale)) {
                list.addData("col_copie_label", this.getText("al.echeances.copieAvis", langueDocument));
                // recherche de l'adresse de courrier du tiers li�, soit l'agence communale
                AdresseTiersDetail adress = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(
                        idTiersAgenceCommunale, new Boolean(true), JadeDateUtil.getGlobazFormattedDate(new Date()),
                        ALCSTiers.DOMAINE_AF, AdresseService.CS_TYPE_COURRIER, "");

                // charge les donn�es de la caisse communale

                list.addData("col_copie", adress.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D1) + " "
                        + adress.getFields().get(AdresseTiersDetail.ADRESSE_VAR_T2));

                table.add(list);

            } else {
                // cas agence lausanne, ou pas de copie � l'agence communale
                table.add(list);
            }
        } else {
            table.add(list);
        }

        // ajoute le tableau des copies au document
        document.add(table);
    }

    /**
     * 
     * @param document
     *            document � imprimer
     * @param titre
     *            de l'allocataire
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    protected void setTexte(DocumentData document, String titre, String langueDocument)
            throws JadeApplicationException, JadePersistenceException {

        if (document == null) {
            throw new ALEcheanceModelException("EcheanceServiceServiceListeAffilieImpl#setTexte : document is null");
        }

        if (!JadeNumericUtil.isNumeric(titre)) {
            throw new ALEcheanceModelException("EcheanceServiceServiceListeAffilieImpl#setTexte : titre :" + titre
                    + " is not valid");

        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALEcheanceModelException("EcheancesServiceListeAffilieImpl#setTexte: language  " + langueDocument
                    + " is not  valid ");
        }
        // titre de l'allocataire (libell�)
        // String titreAlloc = JadeCodesSystemsUtil.getCodeLibelle(titre);

        // idLangueCodeSyteme titre
        FWParametersSystemCode cs = new FWParametersSystemCode();
        // recherche des donn�es li�es au code syst�me
        cs.getCode(titre);
        String titreAlloc = cs.getCodeUtilisateur(getLangueCodeSystem(langueDocument)).getLibelle();

        // titre de l'allocataire
        document.addData("droitEcheanceAllocataireInd_politesse", titreAlloc + ",");

        document.addData("droitEcheanceAllocataireInd_texteDebut",
                this.getText("al.echeances.AffilieAvisEcheance.texte.debut", langueDocument));
        // texte de fin
        document.addData("droitEcheanceAllocataireInd_texteFin",
                this.getText("al.echeances.AffilieAvisEcheance.texte.fin", langueDocument));
        // texte copie en gras
        document.addData("droitEcheanceAllocataireInd_texteCopie",
                this.getText("al.echeances.AffilieAvisEcheance.texteCopie", langueDocument));
        // texte fin copie
        document.addData("droitEcheanceAllocataireInd_texteCopieFin",
                this.getText("al.echeances.AffilieAvisEcheance.texteCopieFin", langueDocument));
        // texte mentionner nss
        document.addData("droitEcheanceAllocataireInd_texteNss",
                this.getText("al.echeances.AffilieAvisEcheances.texteNSS", langueDocument));
        // texte annoncer fin/interruption �tude
        document.addData("droitEcheanceAllocataireInd_texteFinFormation",
                this.getText("al.echeances.AffilieAvisEcheance.texteFinFormation", langueDocument));
        // texte salutations
        document.addData("droitEcheanceAllocataireInd_texteSalutations", this.getText(
                "al.echeances.AllocataireAvisEcheance.texte.politesseFin", langueDocument, new String[] { titreAlloc }

        ));
    }

}
