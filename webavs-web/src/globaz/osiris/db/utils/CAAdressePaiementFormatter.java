package globaz.osiris.db.utils;

import globaz.globall.db.BSession;
import globaz.globall.util.JACCP;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.external.IntAdresseCourrier;
import globaz.osiris.external.IntAdressePaiement;
import globaz.osiris.external.IntBanque;
import globaz.osiris.external.IntTiers;
import globaz.osiris.formatter.CAAdresseCourrierFormatter;
import globaz.pyxis.api.osiris.TIAdresseCourrierOSI;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.adressepaiement.TIAdressePaiement;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.tiers.TITiers;
import globaz.webavs.common.ICommonConstantes;
import java.util.Vector;

/**
 * Insérez la description du type ici. Date de création : (12.02.2002 15:51:32)
 * 
 * @author: Administrator
 */
/**
 * @author sch
 */
public class CAAdressePaiementFormatter {
    public static final int CODE_SWIFT_FORMAT_MAX_LENGTH = 11;
    public static final int CODE_SWIFT_FORMAT_MIN_LENGTH = 8;

    /**
     * Insérez la description de la méthode ici. Date de création : (21.08.2002 14:41:16)
     * 
     * @param adressePaiement
     *            globaz.osiris.db.utils.CAAdressePaiementFormatter
     */
    public static void checkAdressePaiement(CAAdressePaiementFormatter adr, BSession session) throws Exception {

        // Contrôle du paramètre
        if (adr == null) {
            throw new Exception(session.getLabel("5137"));
        }

        // Vérifier le bénéficiaire
        if (adr.getAdresseCourrierBeneficiaire() == null) {
            throw new Exception(session.getLabel("5210"));
        }

        // VERIFICATION D'UNE ADRESSE BANCAIRE CH
        if (adr.getTypeAdresse().equals(IntAdressePaiement.BANQUE)) {
            // Vérifier le numéro de compte bancaire
            if (JadeStringUtil.isBlank(adr.getNumCompte())) {
                throw new Exception(session.getLabel("5211"));
            }

            // Vérifier la banque
            if (adr.getAdresseCourrierBanque() == null) {
                throw new Exception(session.getLabel("5218"));
            }

            // Vérifier le numéro de clearing
            if (JadeStringUtil.isBlank(adr.getClearing())) {
                throw new Exception(session.getLabel("5212"));
            }
        }

        // VERIFICATION D'UNE ADRESSE POSTALE CH
        if (adr.getTypeAdresse().equals(IntAdressePaiement.CCP)) {
            // Vérifier le numéro de ccp
            try {
                JAUtil.checkCcp(adr.getNumCompte());
            } catch (Exception e) {
                throw new Exception(session.getLabel("5213"));
            }
        }

    }

    /**
     * Vérification de la validité d'une adresse de paiement Date de création : (12.02.2002 15:54:19)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public static void checkAdressePaiement(IntAdressePaiement adr, BSession session) throws java.lang.Exception {

        // Contrôle du paramètre
        if (adr == null) {
            throw new Exception(session.getLabel("5137"));
        }

        // Instancier un nouvel objet formatter
        CAAdressePaiementFormatter pf = new CAAdressePaiementFormatter(adr);

        // Le contrôler
        CAAdressePaiementFormatter.checkAdressePaiement(pf, session);

    }

    public static void checkAdressePaiement(TIAdressePaiementData adr, BSession session) throws Exception {

        // Contrôle du paramètre
        if (adr == null) {
            throw new Exception(session.getLabel("5137"));
        }

        // Vérifier le bénéficiaire
        if (JadeStringUtil.isIntegerEmpty(adr.getIdAdresseBenef())) {
            throw new Exception(session.getLabel("5210"));
        }

        // VERIFICATION D'UNE ADRESSE BANCAIRE CH
        if (CAAdressePaiementFormatter.getTypeAdresse(adr).equals(IntAdressePaiement.BANQUE)) {
            // Vérifier le numéro de compte bancaire
            if (JadeStringUtil.isBlank(adr.getCompte())) {
                throw new Exception(session.getLabel("5211"));
            }

            // Vérifier la banque
            if (CAAdressePaiementFormatter.getAdresseCourrier(adr.getSession(), adr.getIdTiersBanque(),
                    IntAdresseCourrier.PRINCIPALE) == null) {
                throw new Exception(session.getLabel("5218"));
            }

            // Vérifier le numéro de clearing
            if (JadeStringUtil.isBlank(adr.getClearing())) {
                throw new Exception(session.getLabel("5212"));
            }
        }

        // VERIFICATION D'UNE ADRESSE POSTALE CH
        if (CAAdressePaiementFormatter.getTypeAdresse(adr).equals(IntAdressePaiement.CCP)) {
            // Vérifier le numéro de ccp
            try {
                JAUtil.checkCcp(adr.getCcp());
            } catch (Exception e) {
                throw new Exception(session.getLabel("5213"));
            }
        }

    }

    /**
     * Vérification de la validité d'une adresse de paiement Date de création : (12.02.2002 15:54:19)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public static void checkAdressePaiementData(TIAdressePaiementData adr, BSession session) throws java.lang.Exception {

        // Contrôle du paramètre
        if (adr == null) {
            throw new Exception(session.getLabel("5137"));
        }

        // Le contrôler
        CAAdressePaiementFormatter.checkAdressePaiement(adr, session);

    }

    /**
     * Récupère l'adresse de courrier de la banque Date de création : (12.02.2002 16:52:33)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntAdresseCourrier
     * @throws Exception
     */
    public static String getAdresseAsString(JadePublishDocumentInfo docInfo, BSession session, String idTiers,
            String idType) throws Exception {
        TITiers t = new TITiers();
        t.setSession(session);
        t.setIdTiers(idTiers);
        return t.getAdresseAsString(docInfo, idType);
    }

    public static IntAdresseCourrier getAdresseCourrier(BSession session, String idTiers, String typeAdresse) {

        String domaine = ICommonConstantes.CS_APPLICATION_COTISATION;

        if (IntAdresseCourrier.POURSUITE.equals(typeAdresse)) {
            domaine = IConstantes.CS_APPLICATION_CONTENTIEUX;
        }

        try {
            // Récupérer l'adresse par défaut pour l'application
            // todo: selon type d'adresse
            TIAvoirAdresse adr = new TIAvoirAdresse();
            adr.setISession(session);
            // TODO sch Si on a pas d'adresse de contentieux et qu'on est sur
            // une poursuite prendre l'adresse de domicile et non l'adresse de
            // courrier
            String sId = adr.getIdAdresseCourrier(session, idTiers, domaine);

            // Si id fournie
            if (!JadeStringUtil.isBlank(sId)) {
                // Lecture du tiers
                TIAdresseCourrierOSI adrOSI = new TIAdresseCourrierOSI();
                adrOSI.setISession(session);

                adrOSI.retrieve(sId);

                return adrOSI;
                // Id non fournie
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("CAAdressePaiementFormatter.getAdresseCourrier(): exception raised : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.11.2001 15:46:32)
     * 
     * @return int
     */
    public static String getTypeAdresse(TIAdressePaiementData adr) {
        String sType = "";

        if (!JadeStringUtil.isBlank(adr.getCcp())) {
            sType = TIAdressePaiement.CS_TYPE_CCP;
        } else if (!JadeStringUtil.isIntegerEmpty(adr.getIdTiersBanque())) {
            sType = TIAdressePaiement.CS_TYPE_CLEARING;
        } else {
            sType = TIAdressePaiement.CS_TYPE_MANDAT;
        }

        if ((adr.getPaysIso().equals("CH"))) {
            if (sType.equals(TIAdressePaiement.CS_TYPE_CCP)) {
                return IntAdressePaiement.CCP;
            } else if (sType.equals(TIAdressePaiement.CS_TYPE_CLEARING)) {
                return IntAdressePaiement.BANQUE;
            } else if (sType.equals(TIAdressePaiement.CS_TYPE_MANDAT)) {
                return IntAdressePaiement.MANDAT;
            } else {
                System.out.println("TIAdressePaiementOSI.getTypeAdresse(): Can't determine adress type (idAdresse=)"
                        + adr.getIdAdressePaiement());
                return "";
            }
        } else {
            if (sType.equals(TIAdressePaiement.CS_TYPE_CLEARING)) {
                return IntAdressePaiement.BANQUE_INTERNATIONAL;
            } else if (sType.equals(TIAdressePaiement.CS_TYPE_MANDAT)) {
                return IntAdressePaiement.MANDAT_INTERNATIONAL;
            } else if (sType.equals(TIAdressePaiement.CS_TYPE_CCP)) {
                return IntAdressePaiement.CCP_INTERNATIONAL;
            } else {
                System.out.println("TIAdressePaiementOSI.getTypeAdresse(): Can't determine adress type (idAdresse=)"
                        + adr.getIdAdressePaiement());
                return "";
            }
        }
    }

    private IntAdressePaiement adressePaiement = null;

    private BSession session = null;

    /**
     * Commentaire relatif au constructeur CAAdressePaiementFormatter.
     */
    public CAAdressePaiementFormatter() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.02.2002 15:52:17)
     * 
     * @param adr
     *            globaz.osiris.interfaceext.tiers.IntAdressePaiement
     */
    public CAAdressePaiementFormatter(IntAdressePaiement adr) {
        adressePaiement = adr;
        session = (BSession) adr.getISession();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.08.2002 14:36:36)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public void checkAdressePaiement(BSession session) throws java.lang.Exception {
        CAAdressePaiementFormatter.checkAdressePaiement(this, session);
    }

    /**
     * Récupère l'adresse de courrier de la banque Date de création : (12.02.2002 16:52:33)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntAdresseCourrier
     */
    public IntAdresseCourrier getAdresseCourrierBanque() {
        return adressePaiement.getBanque().getTiers().getAdresseCourrier(IntAdresseCourrier.PRINCIPALE);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.02.2002 16:38:35)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntAdresseCourrier
     */
    public IntAdresseCourrier getAdresseCourrierBeneficiaire() {
        if (!JadeStringUtil.isIntegerEmpty(adressePaiement.getIdAdresseCourrier())) {
            return adressePaiement.getAdresseCourrier();
        } else {
            return adressePaiement.getTiersTitulaire().getAdresseCourrier(IntAdresseCourrier.PRINCIPALE);
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.02.2002 15:52:50)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntAdressePaiement
     */
    public IntAdressePaiement getAdressePaiement() {
        return adressePaiement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.03.2002 11:25:18)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntTiers
     */
    public IntBanque getBanque() {
        return getAdressePaiement().getBanque();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.02.2002 17:00:07)
     * 
     * @return java.lang.String
     */
    public String getClearing() {
        return adressePaiement.getBanque().getClearing();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.02.2002 17:00:45)
     * 
     * @return java.lang.String
     */
    public String getCodeSwift() {
        return adressePaiement.getBanque().getCodeSwift();
    }

    /**
     * Retourne le code Swift sans " ". Nécessaire car JAUtil.replaceString ne supporte pas le remplacement de " " par
     * "".
     * 
     * @return
     */
    public String getCodeSwiftWithoutSpaces() {
        String codeSwiftToFormat = getCodeSwift();

        StringBuffer buffer = new StringBuffer();
        int index = -1;
        while ((index = codeSwiftToFormat.indexOf(" ")) != -1) {
            buffer.append(codeSwiftToFormat.substring(0, index));
            codeSwiftToFormat = codeSwiftToFormat.substring(index + " ".length());
        }
        buffer.append(codeSwiftToFormat);
        return buffer.toString();
    }

    /**
     * Récupère l'adresse complète de paiement sous la forme d'un tableau de chaînes de caractères Date de création :
     * (12.02.2002 16:25:25)
     * 
     * @return java.lang.String[] l'adresse de paiement complète sous la forme d'une chaîne
     */
    public String[] getFullAddress() throws Exception {

        // Sauvegarder temporairement les chaînes dans un vecteur
        Vector v = new Vector();

        // Récupérer l'adresse du bénéficiaire
        CAAdresseCourrierFormatter cf = new CAAdresseCourrierFormatter();
        cf.setAdresseCourrier(getTiersBeneficiaire(), getAdresseCourrierBeneficiaire());
        cf.setUseTitle(false);
        cf.setUseCountry(true);

        String nomTiersAdrPmt = adressePaiement.getNomTiersAdrPmt();
        if (!JadeStringUtil.isBlank(getAdresseCourrierBeneficiaire().getAutreNom())) {
            nomTiersAdrPmt = getAdresseCourrierBeneficiaire().getAutreNom();
        }
        String[] sAdr = cf.getAdresseLines(2);

        sAdr[0] = nomTiersAdrPmt;

        for (int i = 0; i < sAdr.length; i++) {
            if (!JadeStringUtil.isBlank(sAdr[i])) {
                v.add(sAdr[i]);
            }
        }

        // Charger le numéro de compte
        if (!JadeStringUtil.isBlank(adressePaiement.getNumCompte())) {
            if (adressePaiement.getTypeAdresse().equals(IntAdressePaiement.CCP)) {
                try {
                    v.add(JACCP.formatWithDash(adressePaiement.getNumCompte()));
                } catch (Exception e) {
                    v.add(adressePaiement.getNumCompte());
                }
            } else {
                v.add(adressePaiement.getNumCompte());
            }
        }

        // Récupérer la banque
        if (!JadeStringUtil.isIntegerEmpty(adressePaiement.getIdBanque())) {
            cf.setAdresseCourrier(getTiersBanque(), getAdresseCourrierBanque());

            // No de clearing ou code swift
            if (!JadeStringUtil.isBlank(getClearing())) {
                v.add(getClearing());
            } else if (!JadeStringUtil.isBlank(getCodeSwift())) {
                v.add(getCodeSwift());
            }

            // Charger l'adresse de la banque dans la table
            sAdr = cf.getAdresseLines(2);
            for (int i = 0; i < sAdr.length; i++) {
                if (!JadeStringUtil.isBlank(sAdr[i])) {
                    v.add(sAdr[i]);
                }
            }
        }

        // Créer un tableau de chaînes
        String[] table = new String[v.size()];
        for (int i = 0; i < v.size(); i++) {
            table[i] = (String) v.get(i);
        }

        // Retourne la table
        return table;

    }

    /**
     * Cette méthode permet de retourner le nom du tiers bénéficaire du paiement
     * 
     * @return String Nom du bénéficiaire
     */
    public String getNomTiersBeneficiaire() {
        if (!JadeStringUtil.isBlank(getAdressePaiement().getAdresseCourrier().getAutreNom())) {
            return getAdressePaiement().getAdresseCourrier().getAutreNom();
        } else {
            return getAdressePaiement().getNomTiersAdrPmt();
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.02.2002 17:04:11)
     * 
     * @return java.lang.String
     */
    public String getNumCompte() {
        return adressePaiement.getNumCompte();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.05.2003 09:08:01)
     * 
     * @return BSession
     */
    public BSession getSession() {
        return session;
    }

    /**
     * Retourne l'adresse sur deux lignes de la manière suivante :
     * <ul>
     * <li>CCP - le numéro de CCP
     * <li>Banque - le numéro de compte bancaire, le nom de la banque avec le clearing
     * <li>Mandat - le nom du bénéficiaire, le lieu Date de création : (22.03.2002 13:15:53)
     * 
     * @return java.lang.String
     */
    public String getShortAddress() throws Exception {
        if ((this.getTypeAdresse().equals(IntAdressePaiement.CCP))
                || (this.getTypeAdresse().equals(IntAdressePaiement.CCP_INTERNATIONAL))) {
            return getNumCompte();
        } else if (this.getTypeAdresse().equals(IntAdressePaiement.BANQUE)) {
            return getNumCompte() + ", (" + getClearing() + ") " + getTiersBanque().getNom();
        } else if ((this.getTypeAdresse().equals(IntAdressePaiement.MANDAT))
                || (this.getTypeAdresse().equals(IntAdressePaiement.MANDAT_INTERNATIONAL))) {
            CAAdresseCourrierFormatter adr = new CAAdresseCourrierFormatter(getTiersBeneficiaire(),
                    getAdresseCourrierBeneficiaire());
            String[] adrs = adr.getAdresseLines(2);
            return adrs[0] + ", " + adrs[1];
        } else if (this.getTypeAdresse().equals(IntAdressePaiement.BANQUE_INTERNATIONAL)) {
            return getNumCompte() + ", (" + getCodeSwift() + ") " + getTiersBanque().getNom();
        } else {
            return "";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.03.2002 11:25:50)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntTiers
     */
    public IntTiers getTiersAyantDroit() {
        return getAdressePaiement().getTiers();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.03.2002 11:25:18)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntTiers
     */
    public IntTiers getTiersBanque() {
        return getBanque().getTiers();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.03.2002 11:26:10)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntTiers
     */
    public IntTiers getTiersBeneficiaire() {
        return getAdressePaiement().getTiersTitulaire();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.02.2002 17:12:19)
     * 
     * @return java.lang.String
     */
    public String getTypeAdresse() {
        return adressePaiement.getTypeAdresse();
    }

    /**
     * Retourne vrai si l'adresse de paiement est un adhérent BVR à 5 positions Date de création : (21.08.2002 14:05:46)
     * 
     * @return boolean
     */
    public boolean isAdherentBvr5() {

        // Si no adhérent BVR à 5 positions
        if (this.getTypeAdresse().equals(IntAdressePaiement.BVR)) {
            if (getNumCompte().length() == 5) {
                return true;
            }
        }

        // Faux dans les autres cas
        return false;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.02.2002 15:52:50)
     * 
     * @param newAdressePaiement
     *            globaz.osiris.interfaceext.tiers.IntAdressePaiement
     */
    public void setAdressePaiement(IntAdressePaiement newAdressePaiement) {
        adressePaiement = newAdressePaiement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.05.2003 09:08:01)
     * 
     * @param newSession
     *            BSession
     */
    public void setSession(BSession newSession) {
        session = newSession;
    }
}
