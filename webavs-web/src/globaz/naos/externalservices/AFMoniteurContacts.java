package globaz.naos.externalservices;

import globaz.globall.db.BEntity;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.tiers.TIAvoirContact;
import globaz.pyxis.db.tiers.TIAvoirContactManager;
import globaz.pyxis.db.tiers.TIMoyenCommunication;

/**
 * Moniteur sur les contacts de Tiers (TIMCOMP), permettant d'insérer toute modification dans les annonces de
 * l'affiliation.
 */
public class AFMoniteurContacts extends AFMoniteurTiersAbs {
    public static final String EMAIL = "511005";
    public static final String FAX = "511004";
    public static final String PORTABLE = "511003";
    // moyens de communications
    public static final String PRIVE = "511001";
    public static final String PROFESSIONNEL = "511002";

    // type de mutations moyens de communications
    // public static final String MOYENS_COMM =
    // CodeSystem.CHAMPS_MOD_MOYEN_COMM; // "813024";
    // public static final String TIERS = "813025";
    // public static final String ADR_COURRIER = "813026";
    // public static final String ADR_PAIEMENT = "813027";
    // public static final String CONJOINT = "813028";
    @Override
    public void createAnnonce(BEntity entity) throws Exception {
        TIMoyenCommunication moyen = (TIMoyenCommunication) entity;
        // créer annonce seulement si type téléphone, fax ou email
        if (AFMoniteurContacts.PRIVE.equals(moyen.getTypeCommunication())
                || AFMoniteurContacts.PROFESSIONNEL.equals(moyen.getTypeCommunication())
                || AFMoniteurContacts.PORTABLE.equals(moyen.getTypeCommunication())
                || AFMoniteurContacts.FAX.equals(moyen.getTypeCommunication())
                || AFMoniteurContacts.EMAIL.equals(moyen.getTypeCommunication())) {

            // test si changement sur contact principal (id contact identique à
            // celui du tiers)
            TIAvoirContactManager aContMgr = new TIAvoirContactManager();
            aContMgr.setSession(moyen.getSession());
            aContMgr.setForIdContact(moyen.getIdContact());
            aContMgr.find();
            boolean mainTiers = false;
            for (int i = 0; (i < aContMgr.size()) && !mainTiers; i++) {
                TIAvoirContact contact = (TIAvoirContact) aContMgr.getEntity(i);
                if (contact.getIdTiers().equals(moyen.getIdContact())) {
                    // tiers principal
                    mainTiers = true;
                }
            }
            if (mainTiers) {
                this.createAnnonceToAff(moyen, moyen.getIdContact(), CodeSystem.CHAMPS_MOD_MOYEN_COMM, null);
            }
        }
    }

}
