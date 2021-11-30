package globaz.ij.vb.process;

import ch.globaz.common.util.Dates;
import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTTexte;
import globaz.babel.api.doc.impl.CTScalableDocumentAbstractViewBeanDefaultImpl;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.ij.api.codesystem.IIJCatalogueTexte;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.decisions.IJDecisionIJAI;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJIJCalculeeManager;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.utils.IJGestionnaireHelper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author BSC
 */
public class IJGenererDecisionViewBean extends CTScalableDocumentAbstractViewBeanDefaultImpl {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final Object[] METHODES_SEL_ADRESSE_COURRIER = new Object[] { new String[] {
            "idTiersAdresseCourrierPyxis", "idTiers" } };
    private static final Object[] METHODES_SEL_ADRESSE_PAIEMENT = new Object[] {
            new String[] { "idTiersAdressePaiementPersonnalisee", "idTiers" },
            new String[] { "idDomaineApplicationAdressePaiementPersonnalisee", "idApplication" },
            new String[] { "numAffilieAdressePaiementPersonnalisee", "idExterneAvoirPaiement" } };
    private static String POSITION_AGE_COTISATION = "100";

    private String adresseCourrierAssureFormatee = "";
    private String adresseCourrierEmployeurFormatee = "";
    // pour la gestion de l'adresse de courrier
    private String adresseCourrierFormatee = "";
    private String adresseCourrierPersonnaliseeFormatee = "";
    // pour la gestion des adresses de paiements (assuré & employeur)
    private String adressePaiementAssureFormatee = "";
    private String adressePaiementEmployeurFormatee = "";
    private String adressePaiementFormatee = "";
    private String adressePaiementPersonnaliseeFormatee = "";
    private String beneficiaire = "";
    private String cantonTauxImposition = "";
    private String dateSurDocument = JACalendar.todayJJsMMsAAAA();
    private String displaySendToGed = "0";
    private String eMailAddress = "";
    private String garantitRevision = "";
    // Référence sur l'idDecision
    private String idDecision = null;
    private String idDomaineApplicationAdressePaiementPersonnalisee = "";
    // les champs à reprendre
    private String idPersonneReference = "";
    private String idPrononce = "";
    private String idTierAdresseCourrier = "";
    private String idTierAdressePaiement = "";
    private String idTierAssureAdressePaiement = "";
    private String idTierDemandeDecision = "";
    private String idTierEmployeurAdressePaiement = "";
    private String idTiersAdressePaiementPersonnalisee = "";
    private boolean isRetourDepuisPyxis = false;
    private Boolean isSendToGed = Boolean.FALSE;
    private String numAffilieAdressePaiementPersonnalisee = "";
    private String personnalisationAdressePaiement = "";
    private String remarque = "";
    private String tauxImposition = "";

    public String getAdresseCourrierAssureFormatee() {
        return adresseCourrierAssureFormatee;
    }

    public String getAdresseCourrierEmployeurFormatee() {
        return adresseCourrierEmployeurFormatee;
    }

    public String getAdresseCourrierFormatee() {
        return adresseCourrierFormatee;
    }

    public String getAdresseCourrierPersonnaliseeFormatee() {
        return adresseCourrierPersonnaliseeFormatee;
    }

    public String getAdressePaiementAssureFormatee() {
        return adressePaiementAssureFormatee;
    }

    public String getAdressePaiementEmployeurFormatee() {
        return adressePaiementEmployeurFormatee;
    }

    public String getAdressePaiementFormatee() {
        return adressePaiementFormatee;
    }

    public String getAdressePaiementPersonnaliseeFormatee() {
        return adressePaiementPersonnaliseeFormatee;
    }

    public String getBeneficiaire() {
        return beneficiaire;
    }

    public String getCantonTauxImposition() {
        return cantonTauxImposition;
    }

    public String getCsCantonImpotSource() throws Exception {

        if (JadeStringUtil.isBlankOrZero((cantonTauxImposition))) {
            IJPrononce prononce = new IJPrononce();
            prononce.setSession(getSession());
            prononce.setIdPrononce(getIdPrononce());
            prononce.retrieve();

            if ((prononce.getSoumisImpotSource() != null) && prononce.getSoumisImpotSource().booleanValue()) {
                return prononce.getCsCantonImpositionSource();
            } else {
                return "";
            }
        } else {
            return cantonTauxImposition;
        }

    }

    public String getCurrentUserId() {
        return getSession().getUserId();
    }

    public String getDateSurDocument() {
        return dateSurDocument;
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les détails
     * 
     * @return le détail du requérant formaté
     * @throws Exception
     */
    public String getDetailRequerantDetail() throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), getIdTierDemandeDecision());

        if (tiers != null) {

            String nationalite = "";

            if (!"999".equals(getSession()
                    .getCode(
                            getSession().getSystemCode("CIPAYORI",
                                    tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))))) {
                nationalite = getSession().getCodeLibelle(
                        getSession().getSystemCode("CIPAYORI",
                                tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
            }

            return PRNSSUtil.formatDetailRequerantDetail(
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)), nationalite);

        } else {
            return "";
        }
    }

    public String getDisplaySendToGed() {
        return displaySendToGed;
    }

    @Override
    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getGarantitRevision() {
        return garantitRevision;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdDomaineApplicationAdressePaiementPersonnalisee() {
        return idDomaineApplicationAdressePaiementPersonnalisee;
    }

    public String getIdPersonneReference() {
        return idPersonneReference;
    }

    public String getIdPrononce() {
        return idPrononce;
    }

    public String getIdTierAdresseCourrier() {
        return idTierAdresseCourrier;
    }

    public String getIdTierAdressePaiement() {
        return idTierAdressePaiement;
    }

    public String getIdTierAssureAdressePaiement() {
        return idTierAssureAdressePaiement;
    }

    public String getIdTierDemandeDecision() {
        return idTierDemandeDecision;
    }

    public String getIdTierEmployeurAdressePaiement() {
        return idTierEmployeurAdressePaiement;
    }

    public String getIdTiersAdressePaiementPersonnalisee() {
        return idTiersAdressePaiementPersonnalisee;
    }

    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    public boolean getIsSoumisImpotSource() {

        try {

            IJPrononce ijPrononce = new IJPrononce();
            ijPrononce.setSession(getSession());
            ijPrononce.setIdPrononce(idPrononce);

            ijPrononce.retrieve();

            return ijPrononce.getSoumisImpotSource().booleanValue();

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * retourne un tableau de correspondance entre méthodes client et provider pour le retour depuis pyxis avec le
     * bouton de selection d'une adresse de paiement.
     * 
     * @return la valeur courante de l'attribut méthodes selection adresse
     */
    public Object[] getMethodesSelectionAdresseCourrier() {
        return IJGenererDecisionViewBean.METHODES_SEL_ADRESSE_COURRIER;
    }

    public Object[] getMethodesSelectionAdressePaiement() {
        return IJGenererDecisionViewBean.METHODES_SEL_ADRESSE_PAIEMENT;
    }

    public String getNumAffilieAdressePaiementPersonnalisee() {
        return numAffilieAdressePaiementPersonnalisee;
    }

    public String getPersonnalisationAdressePaiement() {
        return personnalisationAdressePaiement;
    }

    public Vector getPersonnesReference() {

        BSession session = getSession();

        Vector v = IJGestionnaireHelper.getResponsableData(session);
        String[] s = new String[2];

        s[0] = session.getUserId();
        s[1] = session.getUserId() + " - " + session.getUserFullName();

        boolean cont = false;

        int i;

        for (i = 0; i < v.size(); i++) {
            String[] c = (String[]) v.get(i);

            if (c[0].equals(s[0]) && c[1].equals(s[1])) {
                cont = true;
            }

        }

        if (!cont) {
            v.add(s);
        }

        return v;

    }

    /**
     * Méthode qui va reprendre un texte spécial dans le catalogue de texte pour affichage dans l'écran pour
     * modification avant la génération de la décision
     * 
     * @return la remarque du catalogue de texte pour la décision IJAI
     */
    public String getRemarque() throws Exception {

        if (JadeStringUtil.isEmpty(remarque)) {
            String codeIsoLangue = "";
            ICTDocument document;
            ICTDocument documentHelper = null;
            PRTiersWrapper tiers;

            tiers = PRTiersHelper.getTiersParId(getSession(), getIdTierDemandeDecision());
            codeIsoLangue = getSession().getCode(tiers.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
            codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);

            // chargement du catalogue de texte
            if (documentHelper == null) {
                documentHelper = PRBabelHelper.getDocumentHelper(getISession());
                documentHelper.setCsDomaine(IIJCatalogueTexte.CS_IJ);
                documentHelper.setCsTypeDocument(IIJCatalogueTexte.CS_DECISION);
                documentHelper.setDefault(Boolean.TRUE);
                documentHelper.setActif(Boolean.TRUE);
            }

            documentHelper.setCodeIsoLangue(codeIsoLangue);

            ICTDocument[] documents = documentHelper.load();

            if ((documents == null) || (documents.length == 0)) {
                throw new Exception("Impossible de charger le texte de la décision");
            } else {
                document = documents[0];
            }

            StringBuffer buffer = new StringBuffer();

            for (Iterator iterator = document.getTextes(9).iterator(); iterator.hasNext();) {
                ICTTexte texte = (ICTTexte) iterator.next();

                if(POSITION_AGE_COTISATION.equals(texte.getPosition())) {
                    textAgeCotisation(texte, buffer);
                } else {
                    buffer.append(texte.getDescriptionBrut());
                }

            }

            remarque = buffer.toString();
        }

        return remarque;
    }

    public void textAgeCotisation(ICTTexte texte, StringBuffer buffer) throws Exception {
        IJPrononce prononce = new IJPrononce();
        prononce.setSession(getSession());
        prononce.setIdPrononce(getIdPrononce());
        prononce.retrieve();

        if(!Dates.isAnneeMajeur(prononce.getDateDebutPrononce(), prononce.getDateNaissanceTiers())) {
            buffer.append(texte.getDescriptionBrut());
        }
    }

    public Vector getRevisionAGarantir() {

        Vector revAGarantir = new Vector();

        try {

            // Récupération des IJCalculees pour le prononce courant
            IJIJCalculeeManager ijijCalculeeManager = new IJIJCalculeeManager();
            ijijCalculeeManager.setSession(getSession());
            ijijCalculeeManager.setForIdPrononce(idPrononce);
            ijijCalculeeManager.find(BManager.SIZE_NOLIMIT);

            // Pour chaque IJ on test le numéro de révision à garantir
            for (Iterator ijijCalculeeIter = ijijCalculeeManager.iterator(); ijijCalculeeIter.hasNext();) {

                IJIJCalculee ijijCalculee = (IJIJCalculee) ijijCalculeeIter.next();

                if (ijijCalculee.getNoRevision().equals("4")) {

                    if (!revAGarantir.contains("4")) {
                        revAGarantir.add("4");
                    }
                }

                if (ijijCalculee.getNoRevision().equals("5")) {

                    if (!revAGarantir.contains("5")) {
                        revAGarantir.add("5");
                    }
                }
            }

            return revAGarantir;

        } catch (Exception e) {
            return null;
        }
    }

    public String getTauxImposition() throws Exception {

        if (JadeStringUtil.isBlankOrZero(tauxImposition)) {
            IJPrononce prononce = new IJPrononce();
            prononce.setSession(getSession());
            prononce.setIdPrononce(getIdPrononce());
            prononce.retrieve();

            if ((prononce.getSoumisImpotSource() != null) && prononce.getSoumisImpotSource().booleanValue()) {
                return prononce.getTauxImpositionSource();
            } else {
                return "";
            }
        } else {
            return tauxImposition;
        }
    }

    public boolean initIsDecisionSoumisIS() throws Exception {

        IJDecisionIJAI decision = new IJDecisionIJAI();
        decision.setSession(getSession());
        decision.setIdDecision(getIdDecision());
        decision.retrieve();

        IJPrononce prononce = new IJPrononce();
        prononce.setSession(getSession());
        prononce.setIdPrononce(getIdPrononce());
        prononce.retrieve();

        boolean isDecisionSoumisIS = false;

        if ((decision == null) || decision.isNew()) {
            isDecisionSoumisIS = prononce.getSoumisImpotSource().booleanValue();
        }
        // Une décision existe
        //
        else {

            // Si le prononce est communiqué ou transféré... on récupère les
            // info initiales de la décision
            if (IIJPrononce.CS_COMMUNIQUE.equals(prononce.getCsEtat())
                    || IIJPrononce.CS_TRANSFER_DOSSIER.equals(prononce.getCsEtat())
                    || IIJPrononce.CS_ANNULE.equals(prononce.getCsEtat())) {

                if (JadeStringUtil.isBlankOrZero(decision.getCsCantonTauxImposition())
                        && JadeStringUtil.isBlankOrZero(decision.getTauxImposition())) {
                    cantonTauxImposition = "";
                    tauxImposition = "";
                    isDecisionSoumisIS = false;
                } else {
                    isDecisionSoumisIS = true;
                    cantonTauxImposition = decision.getCsCantonTauxImposition();
                    tauxImposition = decision.getTauxImposition();
                }

            }
            // Si le prononcé est en attente (en cours de saisie), on récupère
            // l'info du prononcé
            else {
                isDecisionSoumisIS = prononce.getSoumisImpotSource().booleanValue();
                cantonTauxImposition = prononce.getCsCantonImpositionSource();
                tauxImposition = prononce.getTauxImpositionSource();
            }
        }
        return isDecisionSoumisIS;
    }

    public boolean isRetourDepuisPyxis() {
        return isRetourDepuisPyxis;
    }

    public void setAdresseCourrierAssureFormatee(String adresseCourrierAssureFormatee) {
        this.adresseCourrierAssureFormatee = adresseCourrierAssureFormatee;
    }

    public void setAdresseCourrierEmployeurFormatee(String adresseCourrierEmployeurFormatee) {
        this.adresseCourrierEmployeurFormatee = adresseCourrierEmployeurFormatee;
    }

    public void setAdresseCourrierFormatee(String adresseCourrierFormatee) {
        this.adresseCourrierFormatee = adresseCourrierFormatee;
    }

    public void setAdresseCourrierPersonnaliseeFormatee(String adresseCourrierFormate) {
        adresseCourrierPersonnaliseeFormatee = adresseCourrierFormate;
    }

    public void setAdressePaiementAssureFormatee(String adressePaiementAssureFormatee) {
        this.adressePaiementAssureFormatee = adressePaiementAssureFormatee;
    }

    public void setAdressePaiementEmployeurFormatee(String adressePaiementEmployeurFormatee) {
        this.adressePaiementEmployeurFormatee = adressePaiementEmployeurFormatee;
    }

    public void setAdressePaiementFormatee(String adressePaiementFormatee) {
        this.adressePaiementFormatee = adressePaiementFormatee;
    }

    public void setAdressePaiementPersonnaliseeFormatee(String adressePaiementPersonnaliseeFormatee) {
        this.adressePaiementPersonnaliseeFormatee = adressePaiementPersonnaliseeFormatee;
    }

    public void setBeneficiaire(String beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    public void setCantonTauxImposition(String cantonTauxImposition) {
        this.cantonTauxImposition = cantonTauxImposition;
    }

    public void setDateSurDocument(String string) {
        dateSurDocument = string;
    }

    public void setDisplaySendToGed(String displaySendToGed) {
        this.displaySendToGed = displaySendToGed;
    }

    @Override
    public void setEMailAddress(String mailAddress) {
        eMailAddress = mailAddress;
    }

    public void setGarantitRevision(String garantitRevision) {
        this.garantitRevision = garantitRevision;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdDomaineApplicationAdressePaiementPersonnalisee(
            String idDomaineApplicationAdressePaiementPersonnalisee) {
        this.idDomaineApplicationAdressePaiementPersonnalisee = idDomaineApplicationAdressePaiementPersonnalisee;
    }

    public void setIdPersonneReference(String idPersonneReference) {
        this.idPersonneReference = idPersonneReference;
    }

    public void setIdPrononce(String string) {
        idPrononce = string;
    }

    public void setIdTierAdresseCourrier(String idTierAdresseCourrier) {
        this.idTierAdresseCourrier = idTierAdresseCourrier;
    }

    public void setIdTierAdressePaiement(String idTierAdressePaiement) {
        this.idTierAdressePaiement = idTierAdressePaiement;
    }

    public void setIdTierAssureAdressePaiement(String idTierAssureAdressePaiement) {
        this.idTierAssureAdressePaiement = idTierAssureAdressePaiement;
    }

    public void setIdTierDemandeDecision(String idTierDemandeDecision) {
        this.idTierDemandeDecision = idTierDemandeDecision;
    }

    public void setIdTierEmployeurAdressePaiement(String idTierEmployeurAdressePaiement) {
        this.idTierEmployeurAdressePaiement = idTierEmployeurAdressePaiement;
    }

    public void setIdTiersAdresseCourrierPyxis(String idTiersAdresseCourrierPyxis) {
        isRetourDepuisPyxis = true;
        idTierAdresseCourrier = idTiersAdresseCourrierPyxis;

        try {
            adresseCourrierFormatee = PRTiersHelper.getAdresseCourrierFormatee(getSession(),
                    idTiersAdresseCourrierPyxis, "", IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_IJAI);
        } catch (Exception e) {
        }
    }

    public void setIdTiersAdressePaiementPersonnalisee(String idTiersAdressePaiementPersonnalisee) {
        isRetourDepuisPyxis = true;
        this.idTiersAdressePaiementPersonnalisee = idTiersAdressePaiementPersonnalisee;
    }

    public void setIsRetourDepuisPyxis(boolean isRetourDepuisPyxis) {
        this.isRetourDepuisPyxis = isRetourDepuisPyxis;
    }

    public void setIsSendToGed(Boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    public void setNumAffilieAdressePaiementPersonnalisee(String numAffilieAdressePaiementPersonnalisee) {
        this.numAffilieAdressePaiementPersonnalisee = numAffilieAdressePaiementPersonnalisee;
    }

    public void setPersonnalisationAdressePaiement(String personnalisationAdressePaiement) {
        this.personnalisationAdressePaiement = personnalisationAdressePaiement;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public void setTauxImposition(String tauxImposition) {
        this.tauxImposition = tauxImposition;
    }

    @Override
    public boolean validate() {
        boolean retValue = true;

        if (JAUtil.isDateEmpty(dateSurDocument)) {
            _addError("DATES_OBLIGATOIRES");
            retValue = false;
        }

        return retValue;
    }
}
