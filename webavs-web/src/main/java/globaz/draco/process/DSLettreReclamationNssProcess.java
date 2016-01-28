package globaz.draco.process;

import globaz.commons.nss.NSUtil;
import globaz.draco.db.declaration.DSCIForNSSReclamation;
import globaz.draco.db.declaration.DSCIForNSSReclamationManager;
import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.print.itext.DSLettreReclamationNssBean;
import globaz.draco.print.itext.DSLettreReclamationNss_doc;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.hercule.service.CETiersService;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.util.AFUtil;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 
 * @author sco
 * @since 15 juil. 2011
 */
public class DSLettreReclamationNssProcess extends BProcess {

    private static final long serialVersionUID = -4988130893246799754L;
    private String annee;
    private StringBuffer corpsMessage = null;
    private String dateDocument;
    private String delaiRappel;
    private String fromAffilie;
    private String genreEdition;
    private StringBuffer listeNumAffilie3Rappel = null;
    private Collection<DSLettreReclamationNssBean> m_container = null;
    private int nbCiReclame = 0;
    private int nbDocument = 0;
    private int nbDocument3Rappel = 0;
    private String observation;
    private String toAffilie;
    private String typeDeclaration;
    private String typeDocument;

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        // ----------------------------
        // Récupération des déclarations suite aux données d'entrée fournit par l'utilisateur
        // ----------------------------
        DSDeclarationListViewBean manager = new DSDeclarationListViewBean();
        manager.setSession(getSession());
        manager.setForTypeDeclaration(getTypeDeclaration());
        manager.setFromAffilie(getFromAffilie());
        manager.setToAffilie(getToAffilie());
        manager.setForEtat(DSDeclarationViewBean.CS_COMPTABILISE);
        manager.setForAnnee(getAnnee());
        manager.setForSelectionTri("3");

        try {
            manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            setProgressScaleValue(manager.getSize());
            Object[] obj_listDs = manager.getContainer().toArray();

            for (Object obj_ds : obj_listDs) {
                DSDeclarationViewBean declaration = (DSDeclarationViewBean) obj_ds;

                // Information pour le process
                setProgressDescription("Num Affilie : " + declaration.getAffilieNumero());

                // ----------------------------
                // Récupération des NSS en suspent
                // ----------------------------
                DSCIForNSSReclamationManager dsCIManager = new DSCIForNSSReclamationManager();
                dsCIManager.setSession(getSession());
                dsCIManager.setForIdDeclaration(declaration.getIdDeclaration());
                dsCIManager.setNotInTypeCompte(CIEcriture.CS_CI_SUSPENS_SUPPRIMES); // PO 8362
                dsCIManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

                Object[] obj_listDsCI = dsCIManager.getContainer().toArray();
                m_container = new ArrayList<DSLettreReclamationNssBean>();

                String langueIso = "";
                String formulePolitesse = "";
                String nomPrenom = "";
                String nomInconnu = "";
                String intituleLettre = "";
                TITiersViewBean tiers = null;

                // Récupération de la langue de l'utilisateur
                if (obj_listDsCI.length > 0) {
                    // récupération du requerant en cours et du tiers correspondant
                    tiers = CETiersService.retrieveTiersViewBean(getSession(), declaration.getAffiliation()
                            .getIdTiers());
                    langueIso = AFUtil.toLangueIso(tiers.getLangue());
                    intituleLettre = getSession().getApplication().getLabel("DECLARATION_SALAIRE", langueIso);

                    String lanque = AFUtil.toLangueSimple(tiers.getLangue());
                    FWParametersUserCode entityUserCode = new FWParametersUserCode();
                    entityUserCode.setSession(getSession());
                    entityUserCode.setIdLangue(lanque);
                    entityUserCode.setIdCodeSysteme(getTypeDeclaration());
                    try {
                        entityUserCode.retrieve();
                        if (!entityUserCode.isNew()) {
                            intituleLettre += " - " + entityUserCode.getLibelle();
                        }
                    } catch (Exception e) {
                        JadeLogger.warn("Unabled to retrieve the code " + getTypeDeclaration() + " for DRATYPDECL", e);
                    }
                    if (!declaration.getTypeDeclaration().equals(DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR)) {
                        intituleLettre += " - " + declaration.getAnnee();
                    }

                    nomInconnu = getSession().getApplication().getLabel("INCONNU", langueIso);

                    // définit le titre (Madame, Monsieur) du requérant
                    formulePolitesse = tiers.getFormulePolitesse(null);
                }

                for (Object obj_dsCI : obj_listDsCI) {
                    DSCIForNSSReclamation dsCI = (DSCIForNSSReclamation) obj_dsCI;

                    boolean casAImprimer = false;

                    // Test du numero AVS ou NSS
                    if (JadeStringUtil.isBlankOrZero(dsCI.getNumeroAvs())) {
                        casAImprimer = true;
                    } else {

                        // Test si le numero est de type AVS ou NSS
                        if (NSUtil.unFormatAVS(dsCI.getNumeroAvs()).trim().length() == 13) {

                            // Test validité du numéro NSS
                            if (!NSUtil.nssCheckDigit(dsCI.getNumeroAvs())) {
                                casAImprimer = true;
                            }
                        } else {
                            // Test validité du numero AVS
                            try {
                                JAUtil.checkAvs(dsCI.getNumeroAvs());
                            } catch (Exception e) {
                                casAImprimer = true;
                            }
                        }
                    }

                    // Si le cas doit être sur la lettre de réclamation, on l'ajoute au container.
                    if (casAImprimer) {
                        String periode = "";
                        if (declaration.getTypeDeclaration().equals(DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR)) {
                            periode = dsCI.getMoisDebut() + " - " + dsCI.getMoisFin() + "." + dsCI.getAnnee();
                        } else {
                            periode = dsCI.getMoisDebut() + " - " + dsCI.getMoisFin() + "." + declaration.getAnnee();
                        }

                        nomPrenom = nomInconnu;

                        // Si le nomn est renseigné, on le spécifie
                        if (!JadeStringUtil.isEmpty(dsCI.getNomPrenom())) {
                            nomPrenom = dsCI.getNomPrenom();
                        }

                        DSLettreReclamationNssBean bean = new DSLettreReclamationNssBean(nomPrenom, periode, "",
                                dsCI.getNumeroAvs());

                        // On ajoute l'inscription CI dans la lettre de réclamation.
                        m_container.add(bean);
                    }
                }

                if (!m_container.isEmpty()) {

                    // On incremente les compteurs de document émis et du nombre de CI réclamés
                    nbDocument += 1;
                    nbCiReclame += m_container.size();

                    // ----------------------------
                    // Création de la lettre
                    // ----------------------------
                    DSLettreReclamationNss_doc doc = new DSLettreReclamationNss_doc(getSession());

                    doc.setM_container(m_container);
                    doc.setLangueIsoRequerant(langueIso);
                    doc.setFormulePolitesse(formulePolitesse);
                    doc.setDateDocument(getDateDocument());
                    doc.setTiers(tiers);
                    doc.setObservations(getObservation());
                    doc.setParent(this);
                    doc.setIntituleLettre(intituleLettre);
                    doc.setTypeDocument(getTypeDocument());
                    doc.setNumAffilie(declaration.getNumeroAffilie());
                    doc.setAnnee(declaration.getAnnee());

                    if (DSLettreReclamationNss_doc.DOCUMENT_LETTRE_RECLAMATION.equals(getTypeDocument())) {
                        // ----------------------------
                        // Dans le cas de la lettre de réclamation
                        // ----------------------------

                        // Si genre définitif et qu'on a pas encore édité de lettre de réclamation, on met a jour
                        if (DSLettreReclamationNss_doc.GENRE_DOCUMENT_DEFINITIF.equals(getGenreEdition())
                                && JadeStringUtil.isBlankOrZero(declaration.getDateEnvoiLettre())) {
                            // Si définitif, on met a jour la date d'envoi de la lettre de réclamation
                            // avec la date sur le document
                            declaration.setDateEnvoiLettre(getDateDocument());
                            // Mise a jour de la déclaration
                            declaration.setForceUpdate(true);
                            declaration.update(getTransaction());
                            // On met en ged
                            doc.setArchiveDocument(true);
                            doc.executeProcess();

                            // Génération du document si on est pas en masse
                        } else if (!isEnMasse()
                                || (DSLettreReclamationNss_doc.GENRE_DOCUMENT_SIMULATION.equals(getGenreEdition()) && JadeStringUtil
                                        .isBlankOrZero(declaration.getDateEnvoiLettre()))) {
                            doc.executeProcess();
                        } else {
                            // On enleve ce document des compteurs car il a déjà été imprimé.
                            nbDocument -= 1;
                            nbCiReclame -= m_container.size();
                        }

                    } else {
                        // ----------------------------
                        // Dans le cas de la lettre de rappel
                        // ----------------------------

                        // -----------------------------
                        // verification délai entre les dates
                        String dateAVerifier = declaration.getDateEnvoiRappel();
                        if (JadeStringUtil.isBlankOrZero(dateAVerifier)) {
                            dateAVerifier = declaration.getDateEnvoiLettre();
                        }
                        if (!JadeStringUtil.isBlankOrZero(dateAVerifier)) {
                            // Si le delai est pas respecté, on passe a la déclaration suivante
                            dateAVerifier = AFAffiliationUtil.addDaysToDate(dateAVerifier, getDelaiRappel());
                            if (BSessionUtil.compareDateFirstGreater(getSession(), dateAVerifier, getDateDocument())) {

                                // On imprime pas le document car le delai n'est pas respecté
                                // On retire ce document des compteurs
                                nbDocument -= 1;
                                nbCiReclame -= m_container.size();
                                continue;
                            }

                        } else {
                            // On passe a la déclaration suivante car on a pas fait de réclamation
                            // On retire ce document des compteurs
                            nbDocument -= 1;
                            nbCiReclame -= m_container.size();
                            continue;
                        }
                        // ------------------------------

                        // Si définitif, on renseigne la date du rappel
                        if (JadeStringUtil.isBlankOrZero(declaration.getNbRappel())) {
                            doc.setDateLettreReclamation(declaration.getDateEnvoiLettre());
                        } else {
                            doc.setDateLettreReclamation(declaration.getDateEnvoiRappel());
                        }

                        declaration.setDateEnvoiRappel(getDateDocument());

                        int nbRappel = CEUtils.transformeStringToInt(declaration.getNbRappel());

                        if (nbRappel < 3) {

                            // On incremente le nombre de rappel
                            nbRappel += 1;
                            declaration.setNbRappel("" + nbRappel);
                            doc.setNumeroRappel(declaration.getNbRappel());

                            // Mise a jour de la déclaration
                            // Si le genre de document est définitif
                            if (DSLettreReclamationNss_doc.GENRE_DOCUMENT_DEFINITIF.equals(getGenreEdition())) {
                                declaration.setForceUpdate(true);
                                declaration.update(getTransaction());

                                // On met en ged
                                doc.setArchiveDocument(true);
                            }

                            // On imprime le rappel seulement si on a déjà fait une réclamation
                            if (!JadeStringUtil.isBlankOrZero(declaration.getDateEnvoiLettre())) {
                                doc.executeProcess();
                            }

                        } else {

                            // ESt-ce que le processus a été lancé en masse ?
                            if (!isEnMasse()) {

                                // On incremente le nombre de rappel
                                nbRappel += 1;
                                declaration.setNbRappel("" + nbRappel);
                                doc.setNumeroRappel(declaration.getNbRappel());

                                // Mise a jour de la déclaration
                                // Si le genre de document est définitif
                                if (DSLettreReclamationNss_doc.GENRE_DOCUMENT_DEFINITIF.equals(getGenreEdition())) {
                                    declaration.setForceUpdate(true);
                                    declaration.update(getTransaction());

                                    // On met en ged
                                    doc.setArchiveDocument(true);
                                }

                                // Génération du document car on a qu'un seul affilié et on force l'impression
                                doc.executeProcess();

                            } else {
                                // Processus lancé en masse
                                nbDocument3Rappel += 1;
                                addNumAffilie3Rappel(declaration.getAffilieNumero());
                                // On retire ce document des compteurs
                                nbDocument -= 1;
                                nbCiReclame -= m_container.size();
                            }
                        }
                    }
                }

                // On fait avancer la progresse bar
                incProgressCounter();

                if (isAborted()) {
                    return true;
                }
            }

            // ----------------------------
            // Ajout des informations dans le mail
            // ----------------------------
            addMailInformations();

            // ----------------------------
            // merge des documents
            // ----------------------------
            JadePublishDocumentInfo _docInfo = createDocumentInfo();
            if (DSLettreReclamationNss_doc.DOCUMENT_LETTRE_RECLAMATION.equals(getTypeDocument())) {
                _docInfo.setDocumentTypeNumber(DSLettreReclamationNss_doc.NUMERO_INFOROM_RECLAMATION);
            } else {
                _docInfo.setDocumentTypeNumber(DSLettreReclamationNss_doc.NUMERO_INFOROM_RAPPEL);
            }
            this.mergePDF(_docInfo, true, 0, false, null);

        } catch (Exception e) {

            if (DSLettreReclamationNss_doc.DOCUMENT_LETTRE_RECLAMATION.equals(getTypeDocument())) {
                this._addError(getTransaction(), getSession()
                        .getLabel("EXECUTION_GENERATION_LETTRE_RECLAMATION_ERREUR"));
            } else {
                this._addError(getTransaction(), getSession().getLabel("EXECUTION_GENERATION_LETTRE_RAPPEL_ERREUR"));
            }

            String messageInformation = "Type declaration : " + getTypeDeclaration() + "\n";
            messageInformation += "From Affilie : " + getFromAffilie() + "\n";
            messageInformation += "To Affilie : " + getToAffilie() + "\n";
            messageInformation += "Année : " + getAnnee() + "\n";
            messageInformation += "Genre édition : " + getGenreEdition() + "\n";
            messageInformation += "Type de document : " + getTypeDocument() + "\n";

            messageInformation += CEUtils.stack2string(e);

            CEUtils.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());

            return false;
        }

        return true;
    }

    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isEmpty(getTypeDocument())) {
            getSession().addError(getSession().getLabel("VAL_TYPE_DOCUMENT"));
        }
        if (DSLettreReclamationNss_doc.DOCUMENT_LETTRE_RAPPEL.equals(getTypeDocument())
                && JadeStringUtil.isEmpty(getDelaiRappel())) {
            getSession().addError(getSession().getLabel("VAL_DELAI_RAPPEL"));
        }
        if (JadeStringUtil.isEmpty(getDateDocument())) {
            getSession().addError(getSession().getLabel("VAL_DATE_DOCUMENT"));
        } else {
            // Test si la date du document est +/- 10 jours par rapport a la date du jour
            String dateDocumentTest = AFAffiliationUtil.addDaysToDate(JACalendar.todayJJsMMsAAAA(), "-10");
            if (BSessionUtil.compareDateFirstGreater(getSession(), dateDocumentTest, getDateDocument())) {
                getSession().addError(getSession().getLabel("VAL_DATE_DOCUMENT_LIMITE_MOINS_10"));
            }
            dateDocumentTest = AFAffiliationUtil.addDaysToDate(JACalendar.todayJJsMMsAAAA(), "10");
            if (BSessionUtil.compareDateFirstGreater(getSession(), getDateDocument(), dateDocumentTest)) {
                getSession().addError(getSession().getLabel("VAL_DATE_DOCUMENT_LIMITE_PLUS_10"));
            }
        }
    }

    /**
     * Ajoute des informations dans l'email.
     */
    private void addMailInformations() throws Exception {

        corpsMessage = new StringBuffer("");
        corpsMessage.append(getSession().getLabel("TOTAL_NB_DOCUMENT") + " : " + nbDocument);
        corpsMessage.append("\n");
        corpsMessage.append(getSession().getLabel("TOTAL_NB_CI_RECLAMES") + " : " + nbCiReclame);
        corpsMessage.append("\n");

        // On indique le nombre de document qui ont 3 ou plus de rappel si c'est dans
        // le cas d'un processus en masse
        if (DSLettreReclamationNss_doc.DOCUMENT_LETTRE_RAPPEL.equals(getTypeDocument()) && isEnMasse()) {
            corpsMessage.append(getSession().getLabel("TOTAL_NB_RAPPEL_3") + " : " + nbDocument3Rappel);
            if (nbDocument3Rappel > 0) {
                corpsMessage.append("\n");
                corpsMessage.append(listeNumAffilie3Rappel.toString());
            }
        }
    }

    /**
     * Ajout un numléro affilié dans la liste des affiliés au 3ieme rappel
     * 
     * @param numAffilie
     */
    private void addNumAffilie3Rappel(String numAffilie) {
        if (listeNumAffilie3Rappel == null) {
            listeNumAffilie3Rappel = new StringBuffer();
        }

        if (!JadeStringUtil.isEmpty(listeNumAffilie3Rappel.toString())) {
            listeNumAffilie3Rappel.append("\n");
        }

        if (!JadeStringUtil.isEmpty(numAffilie)) {
            listeNumAffilie3Rappel.append(numAffilie);
        }
    }

    public String getAnnee() {
        return annee;
    }

    public String getDateDocument() {
        return dateDocument;
    }

    public String getDelaiRappel() {
        return delaiRappel;
    }

    @Override
    protected String getEMailObject() {
        if (DSLettreReclamationNss_doc.DOCUMENT_LETTRE_RECLAMATION.equals(getTypeDocument())) {
            if (isOnError() || getSession().hasErrors() || isAborted()) {
                return getSession().getLabel("GENERATION_LETTRE_RECLAMATION_ERROR");
            } else {
                return getSession().getLabel("GENERATION_LETTRE_RECLAMATION_OK");
            }
        } else {
            if (isOnError() || getSession().hasErrors() || isAborted()) {
                return getSession().getLabel("GENERATION_LETTRE_RAPPEL_ERROR");
            } else {
                return getSession().getLabel("GENERATION_LETTRE_RAPPEL_OK");
            }
        }
    }

    public String getFromAffilie() {
        return fromAffilie;
    }

    public String getGenreEdition() {
        return genreEdition;
    }

    public String getObservation() {
        return observation;
    }

    @Override
    public String getSubjectDetail() {
        if ((getTransaction() != null) && getTransaction().hasErrors()) {
            return super.getSubjectDetail();
        } else if ((getSession() != null) && getSession().hasErrors()) {
            return super.getSubjectDetail();
        }

        return corpsMessage.toString();

    }

    public String getToAffilie() {
        return toAffilie;
    }

    public String getTypeDeclaration() {
        return typeDeclaration;
    }

    public String getTypeDocument() {
        return typeDocument;
    }

    public boolean isEnMasse() {
        boolean vide = getFromAffilie().equals(getToAffilie()) && JadeStringUtil.isEmpty(getFromAffilie());
        boolean masseAvecBorne = !getFromAffilie().equals(getToAffilie());

        return vide || masseAvecBorne;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setDelaiRappel(String delaiRappel) {
        this.delaiRappel = delaiRappel;
    }

    public void setFromAffilie(String fromAffilie) {
        this.fromAffilie = fromAffilie;
    }

    public void setGenreEdition(String genreEdition) {
        this.genreEdition = genreEdition;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public void setToAffilie(String toAffilie) {
        this.toAffilie = toAffilie;
    }

    public void setTypeDeclaration(String typeDeclaration) {
        this.typeDeclaration = typeDeclaration;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

}
