package ch.globaz.common.document.reference;

import ch.globaz.common.properties.PropertiesException;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JABVR;
import globaz.globall.util.JACalendar;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.util.AFIDEUtil;
import globaz.osiris.db.comptes.CASection;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.api.ITIAdministration;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAbstractAdresseData;
import globaz.pyxis.db.adressecourrier.TIAdresseDataManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ReferenceEBill extends AbstractReference {

    public static final String PAIEMENT_TYPE_ESR = "ESR";
    public static final String PAIEMENT_TYPE_IBAN = "IBAN";
    public static final String PAIEMENT_TYPE_DD = "DD";
    public static final String PAIEMENT_TYPE_CREDIT = "CREDIT";
    public static final String PAIEMENT_TYPE_OTHER = "OTHER";

    public static final String REFERENCE_TYPE_BILLNUMBER = "BillNumber";

    private static final Logger LOG = LoggerFactory.getLogger(ReferenceEBill.class);

    private static final String LINE_ITEM_TYPE_NORMAL = "NORMAL";
    private static final String LINE_ITEM_TYPE_LINE_ITEM = "LINEITEMALLOWANCEANDCHARGE";
    private static final String LINE_ITEM_TYPE_GLOBAL = "GLOBALALLOWANCEANDCHARGE";
    private static final String LINE_ITEM_TYPE_SHIPPING = "SHIPPINGANDHANDLING";
    private static final String LINE_ITEM_TYPE_ROUNDING = "ROUNDING";

    private static final String DOCUMENT_TYPE_BILL = "BILL";
    private static final String DOCUMENT_TYPE_CREDITADVICE = "CREDITADVICE";
    private static final String DOCUMENT_TYPE_REMINDER = "REMINDER";

    private static final String DEVISE_DEFAUT = "CHF";

    private static final String TAX_LIABILITY_FRE = "FRE";
    private static final String TAX_LIABILITY_VAT = "VAT";

    private static final String POST_FINANCE = "PostFinance";

    private static final String CODE_PAYS_DEFAUT = "CH";

    private String typeDocument;

    private String paymentType;

    private String debCodePostal;
    private String debCasePostale;
    private String debLieu;
    private String debPays;
    private String debNom;
    private String debRueAdresse;
    private String debNumMaisonAdresse;

    private String creCodePostal;
    private String creCasePostale;
    private String creLieu;
    private String nomCaisse;
    private String creRueAdresse;
    private String creNumMaisonAdresse;

    private boolean isNotesCredit;
    private boolean isBulletinsDeSoldes;
    private boolean isQR;
    private boolean isBVR;
    private boolean isBulletinNeutre;
    private boolean isLSV;

    /**
     * @param session
     * @param idTiers
     * @param section
     * @throws PropertiesException
     */
    public void initReferenceEBill(BSession session, String idTiers, CASection section) throws Exception {

        // Récupération de l'adresse du débiteur.
        initAdresseDeb(idTiers);

        // Récupération de l'adresse du créancier.
        initAdresseCre();

        // Récupération du type du document
        initTypeDocument();

        // Récupération du type de payment QR=>IBAN, BVR=>ESR, LSV=>DD ou Notes de crédit=>CREDIT.
        initPaymentType();

        // Générer numéro référence
        genererNumeroReference(section);
    }

    private void initPaymentType() {
        if (isQR) {
            paymentType = PAIEMENT_TYPE_IBAN;
        } else if (isBVR) {
            paymentType = PAIEMENT_TYPE_ESR;
        }
        if (isLSV) {
            paymentType = PAIEMENT_TYPE_DD;
        }
        if (isNotesCredit) {
            paymentType = PAIEMENT_TYPE_CREDIT;
        }
    }

    private void initTypeDocument() {
        if (isNotesCredit) {
            typeDocument = DOCUMENT_TYPE_CREDITADVICE;
        } else if (isBulletinsDeSoldes) {
            typeDocument = DOCUMENT_TYPE_REMINDER;
        } else {
            typeDocument = DOCUMENT_TYPE_BILL;
        }
    }

    private void initAdresseCre() throws Exception {
        ITIAdministration cre = CaisseHelperFactory.getInstance().getAdministrationCaisse(getSession());

        TIAbstractAdresseData addresseCre = chercheAdresse(cre.getIdTiers());
        nomCaisse = cre.getNom();
        creRueAdresse = addresseCre.getRue();
        creNumMaisonAdresse = addresseCre.getNumero();
        creCasePostale = StringUtils.isNotBlank(addresseCre.getCasePostale()) ? addresseCre.getCasePostale() : null;
        creCodePostal = addresseCre.getNpa();
        creLieu = addresseCre.getLocalite();
    }

    private TIAdresseDataSource chercheAdresseAvecCascade(String idTiers) throws Exception {
        AFAffiliationManager affiliationManager = new AFAffiliationManager();
        affiliationManager.setSession(getSession());
        affiliationManager.setForIdTiers(idTiers);
        affiliationManager.find();

        if (affiliationManager.size() == 1) {
            AFAffiliation affiliation = (AFAffiliation) affiliationManager.getFirstEntity();
            return AFIDEUtil.loadAdresseFromCascadeIde(affiliation);
        } else {
            throw new Exception("L'affiliation correspondant au numéro du tiers " + idTiers + " n'a pas été trouvée");
        }

    }

    private TIAbstractAdresseData chercheAdresse(String idTiers) throws Exception {
        TIAdresseDataManager adresseDataManager = new TIAdresseDataManager();
        adresseDataManager.setSession(getSession());
        adresseDataManager.setForIdTiers(idTiers);
        adresseDataManager.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());
        adresseDataManager.setForTypeAdresse(IConstantes.CS_AVOIR_ADRESSE_DOMICILE);

        try {
            adresseDataManager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            LOG.error("Impossible de retrouver l'adresse : " + e.getMessage(), e);
        }

        List<TIAbstractAdresseData> listAdresses = new ArrayList<>();

        for (int i = 0; i < adresseDataManager.size(); i++) {
            TIAbstractAdresseData adresseTemp = ((TIAbstractAdresseData) adresseDataManager.getEntity(i));
            if (StringUtils.isEmpty(adresseTemp.getDateFinRelation())) {
                listAdresses.add(adresseTemp);
            }
        }

        if (listAdresses.size() == 1) {
            return listAdresses.get(0);
        } else {
            throw new Exception("Impossible de retrouver une addresse unique.");
        }

    }

    private void initAdresseDeb(String idTiers) throws Exception {
        TIAdresseDataSource adresseDeb = chercheAdresseAvecCascade(idTiers);

        debNom = adresseDeb.fullLigne1;
        debRueAdresse = adresseDeb.rue;
        debNumMaisonAdresse = adresseDeb.numeroRue;
        debPays = adresseDeb.paysIso;
        debCodePostal = adresseDeb.localiteNpa;
        debCasePostale = StringUtils.isNotBlank(adresseDeb.casePostale) ? adresseDeb.casePostale : null;
        debLieu = adresseDeb.localiteNom;

        /*TIAbstractAdresseData adresseDeb = chercheAdresse(idTiers);

        debNom = adresseDeb.getNom();
        debRueAdresse = adresseDeb.getRue();
        debNumMaisonAdresse = adresseDeb.getNumero();
        debPays = adresseDeb.getPaysIso();
        debCodePostal = adresseDeb.getNpa();
        debCasePostale = adresseDeb.getCasePostale();
        debLieu = adresseDeb.getLocalite();*/
    }

    public void addIfNotEmpty(String value, StringBuffer stringBuffer, String separator) {
        if (StringUtils.isNotBlank(value)) {
            if (StringUtils.isNotEmpty(separator)) {
                stringBuffer.append(separator);
            }
            stringBuffer.append(value);
        }
    }

    /**
     * Méthode permettant de générer le numéro de référence.
     *
     * @param section la section
     */
    private void genererNumeroReference(CASection section) {
        try {
            String numReference = genererNumReference(section.getCompteAnnexe().getIdRole(), section.getCompteAnnexe().getIdExterneRole(), false, section.getIdTypeSection()
                    , section.getIdExterne(), section.getIdCompteAnnexe());
            JABVR bvr = new JABVR(section.getSolde(), numReference, getNoAdherent());
            setLigneReference(bvr.get_ligneReference());
        } catch (Exception e) {
            LOG.error("Impossible de générer le numéro de référence. ", e);
        }
    }

    public String getDocumentType() {
        return typeDocument;
    }

    /**
     * Dans le cadre de WebAVS, on est toujours sur des factures en CHF.
     *
     * @return la devise par défaut CHF
     */
    public String getDevise() {
        return DEVISE_DEFAUT;
    }


    /**
     * Dans le cadre de WebAVS, on est toujours dans le cas FRE.
     *
     * @return FRE si aucune TVA appliquée
     */
    public String getTaxLiability() {
        return TAX_LIABILITY_FRE;
    }

    public String getNetworkName() {
        return POST_FINANCE;
    }

    public String getCreCodePostal() {
        // Limitation du nombre de caractère à 16 ( doc officielle)
        if (creCodePostal.length() > 16) {
            return creCodePostal.substring(0, 16);
        } else return creCodePostal;
    }

    public String getCreCasePostale() {
        return creCasePostale;
    }

    public String getCreLieu() {
        // Limitation du nombre de caractère à 35 ( doc officielle)
        if (creLieu.length() > 35) {
            return creLieu.substring(0, 35);
        } else return creLieu;
    }

    public String getCrePays() {
        try {
            return getCodePays();
        } catch (Exception e) {
            // Le code pays n'a pas été trouvé
            return CODE_PAYS_DEFAUT;
        }
    }

    public String getTypeDocument() {
        return typeDocument;
    }

    public String getDebNom() {
        return debNom;
    }

    public String getDebRueAdresse() {
        return debRueAdresse;
    }

    public String getDebNumMaisonAdresse() {
        return debNumMaisonAdresse;
    }

    public String getDebCodePostal() {
        return debCodePostal;
    }

    public String getDebCasePostale() {
        return debCasePostale;
    }

    public String getDebLieu() {
        return debLieu;
    }

    public String getDebPays() {
        return debPays;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getLineItemType() {
        return LINE_ITEM_TYPE_NORMAL;
    }

    public String getCreRueAdresse() {
        return creRueAdresse;
    }

    public String getCreNumMaisonAdresse() {
        return creNumMaisonAdresse;
    }

    public String getNomCaisse() {
        return nomCaisse;
    }

    public boolean isNotesCredit() {
        return isNotesCredit;
    }

    public void setIsNotesCredit(boolean notesCredit) {
        isNotesCredit = notesCredit;
    }

    public boolean isBulletinsDeSoldes() {
        return isBulletinsDeSoldes;
    }

    public void setIsBulletinsDeSoldes(boolean bulletinsDeSoldes) {
        isBulletinsDeSoldes = bulletinsDeSoldes;
    }

    public boolean isQR() {
        return isQR;
    }

    public void setIsQR(boolean QR) {
        isQR = QR;
    }

    public boolean isBVR() {
        return isBVR;
    }

    public void setIsBVR(boolean BVR) {
        isBVR = BVR;
    }

    public boolean isBulletinNeutre() {
        return isBulletinNeutre;
    }

    public void setIsBulletinNeutre(boolean bulletinNeutre) {
        isBulletinNeutre = bulletinNeutre;
    }

    public boolean isLSV() {
        return isLSV;
    }

    public void setIsLSV(boolean LSV) {
        isLSV = LSV;
    }
}
