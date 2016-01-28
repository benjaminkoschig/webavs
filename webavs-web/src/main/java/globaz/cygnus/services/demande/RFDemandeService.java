package globaz.cygnus.services.demande;

import globaz.cygnus.db.qds.RFAssQdDossierJointDossierJointTiers;
import globaz.cygnus.db.qds.RFAssQdDossierJointDossierJointTiersManager;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.hera.impl.ijai.SFSituationFamiliale;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.ITIAdresseFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementBanqueFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementCppFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ch.globaz.hera.business.constantes.ISFMembreFamille;
import ch.globaz.pegasus.business.constantes.IPCDroits;

public class RFDemandeService {

    /**
     * Methode poure retourner l'id du membre famille
     * 
     * @param idTierDemande
     * @param sf
     * @param listeIdTiersMembresFamille
     * @return
     * @throws Exception
     */
    private static String findFirstIdTierMembreFamille(BSession session, String idTierDemande,
            ISFSituationFamiliale sf, List<String> listeIdTiersMembresFamille) throws Exception {
        String idTiersRequerant = null;
        // Si situation familiale non null, recherche des membres issues de la situtation familiale
        if (sf != null) {
            // Recherche des membres de la famille
            ISFMembreFamilleRequerant[] mf = sf.getMembresFamilleRequerant(idTierDemande);
            // Si aucuns membre de famille
            if (mf != null) {
                // Parcours de chaque membre pour remonter une adresse de paiement conjoint ou parent
                for (ISFMembreFamilleRequerant membre : mf) {
                    // Si idTiers est différent de l'assuré sans adresse de paiement
                    if (!membre.getIdTiers().equals(idTierDemande)) { // Si l'idTiers n'est pas null
                        if (!JadeStringUtil.isBlankOrZero(membre.getIdTiers())) { // Si les membres familles sont des
                                                                                  // conjoints ou parents
                            if ((ISFMembreFamille.CS_TYPE_RELATION_CONJOINT.equals(membre.getRelationAuRequerant()))
                                    || (ISFMembreFamille.CS_TYPE_RELATION_PARENT
                                            .equals(membre.getRelationAuRequerant()))) {

                                idTiersRequerant = membre.getIdTiers();
                                break;
                            }
                        }
                    }
                }
            }
        } else {
            // Recherche d'une adresse depuis la liste d'id reçu en paramètre
            for (String idMembre : listeIdTiersMembresFamille) {
                // Si le membre ne correspond pas à l'ayant droit
                if (!idTierDemande.equals(idMembre)) {
                    // Contrôle de la présence d'une adresse
                    TIAdressePaiementData adressePaiement = PRTiersHelper.getAdressePaiementData(session,
                            session.getCurrentThreadTransaction(), idMembre,
                            IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", JACalendar.todayJJsMMsAAAA());
                    // Si l'id adressePaiement est trouvé.
                    if ((adressePaiement != null) && (adressePaiement.getIdAdressePaiement() != null)) {
                        idTiersRequerant = idMembre;
                        break;
                    }
                }
            }
        }
        return idTiersRequerant;
    }

    public static String getAdresseFormatee(String idTiers, BSession session) throws Exception {

        TIAdressePaiementData adressePaiement = null;
        String adrPaiementNom = "";
        String descAdressePaiement = null;

        PRTiersWrapper tiersWrapper = PRTiersHelper.getTiersParId(session, idTiers);
        // Recherche d'un tiers
        if (null != tiersWrapper) {

            adrPaiementNom = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                    + tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_PRENOM);

            // recherche de l'adresse de paiement
            adressePaiement = PRTiersHelper
                    .getAdressePaiementData(session, session.getCurrentThreadTransaction(), idTiers,
                            IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", JACalendar.todayJJsMMsAAAA());

            if ((adressePaiement != null) && !adressePaiement.isNew()) {
                TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();
                source.load(adressePaiement);

                ITIAdresseFormater tiAdrPaiBanFor;

                // formatter le no de ccp ou le no bancaire
                if (JadeStringUtil.isEmpty(adressePaiement.getCcp())) {
                    tiAdrPaiBanFor = new TIAdressePaiementBanqueFormater();
                } else {
                    tiAdrPaiBanFor = new TIAdressePaiementCppFormater();
                }

                if (!JadeStringUtil.isBlank(adrPaiementNom)) {
                    descAdressePaiement = adrPaiementNom + "<br/>"
                            + JadeStringUtil.change(tiAdrPaiBanFor.format(source), "\n", "<br/>");
                } else {
                    descAdressePaiement = JadeStringUtil.change(tiAdrPaiBanFor.format(source), "\n", "<br/>");
                }

                return descAdressePaiement;
            }
        }
        // Sinon, recherche d'une administration
        else {
            PRTiersWrapper adminWrapper = PRTiersHelper.getAdministrationParId(session, idTiers);
            if (adminWrapper != null) {

                adrPaiementNom = adminWrapper.getProperty(PRTiersWrapper.PROPERTY_NOM);

                // recherche de l'adresse de paiement
                adressePaiement = PRTiersHelper.getAdressePaiementData(session, session.getCurrentThreadTransaction(),
                        idTiers, IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "",
                        JACalendar.todayJJsMMsAAAA());

                if ((adressePaiement != null) && !adressePaiement.isNew()) {
                    TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();
                    source.load(adressePaiement);

                    ITIAdresseFormater tiAdrPaiBanFor;

                    // formatter le no de ccp ou le no bancaire
                    if (JadeStringUtil.isEmpty(adressePaiement.getCcp())) {
                        tiAdrPaiBanFor = new TIAdressePaiementBanqueFormater();
                    } else {
                        tiAdrPaiBanFor = new TIAdressePaiementCppFormater();
                    }

                    if (!JadeStringUtil.isBlank(adrPaiementNom)) {
                        descAdressePaiement = adrPaiementNom + "<br/>"
                                + JadeStringUtil.change(tiAdrPaiBanFor.format(source), "\n", "<br/>");
                    } else {
                        descAdressePaiement = JadeStringUtil.change(tiAdrPaiBanFor.format(source), "\n", "<br/>");
                    }

                    return descAdressePaiement;
                }
            }
        }
        return descAdressePaiement;

    }

    /**
     * Methode pour remonter l'adresse de paiement (formatee)
     * 
     * @param idTierDemande
     * @param idAdressePaiement
     * @param idQdPrincipale
     * @param session
     * @return
     * @throws Exception
     */
    public static String[] getDescAdressePaiementWithIdTiers(String etatFormulaireDemande, String idTierDemande,
            String idAdressePaiement, String idQdPrincipale, BSession session) throws Exception {
        String descAdressePaiement = null;
        String idTiers = null;

        String[] adresseIdTiers = new String[] { "", "" };

        // Si c'est une nouvelle demande, sans id, on recherche une adresse de paiement en cascade : tiers --> qd -->
        // membreFamille
        if ((etatFormulaireDemande.equalsIgnoreCase("ADD"))) {
            // Recherche l'addresse de paiement de l'ayant droit
            if (!JadeStringUtil.isBlankOrZero(idAdressePaiement)) {
                idTiers = idAdressePaiement;
                descAdressePaiement = RFDemandeService.getAdresseFormatee(idAdressePaiement, session);
            }
            // Sinon, recherche de l'adresse de paiement d'un membre famille de la QD concernée.
            if ((descAdressePaiement == null) && !JadeStringUtil.isBlankOrZero(idQdPrincipale)) {
                idTiers = RFDemandeService.getIdTiersPaiementParQdPrincipale(idQdPrincipale, idTierDemande, session);
                descAdressePaiement = RFDemandeService.getAdresseFormatee(idTiers, session);
            }
            // Sinon, recherche de l'adresse de paiement d'un membre famille de la situation familiale.
            if (descAdressePaiement == null) {
                idTiers = RFDemandeService.getIdTierPaiementParIdTierDemande(idTierDemande, session);
                descAdressePaiement = RFDemandeService.getAdresseFormatee(idTiers, session);
                if (descAdressePaiement == null) {
                    idTiers = "";
                    descAdressePaiement = "";
                }
            }
        }
        // Si c'est une demande existante, on charge l'adresse selon l'idAdressePaiement de la demande.
        else {
            idTiers = idAdressePaiement;
            descAdressePaiement = RFDemandeService.getAdresseFormatee(idAdressePaiement, session);
        }

        // Le tiers est égallement retourné, car si le requérant n'a pas d'adresse, on remonte celle d'un membre
        // famille.
        // Il faut donc le setter au viewBean
        adresseIdTiers[0] = descAdressePaiement;
        adresseIdTiers[1] = idTiers;

        return adresseIdTiers;
    }

    /**
     * Methode qui retourne l'idTiers d'un membre de famille si l'ayant droit n'a pas d'adresse
     * 
     * @param tiersWrapper
     * @return
     */
    public static String getIdTierPaiementParIdTierDemande(String idTierDemande, BSession session) throws Exception {
        String idTiersRequerant = "";
        // Recherche de la situation famille
        ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                ISFSituationFamiliale.CS_DOMAINE_RENTES, idTierDemande);

        idTiersRequerant = RFDemandeService.findFirstIdTierMembreFamille(session, idTierDemande, sf, null);
        return idTiersRequerant;
    }

    /**
     * Methode pour charger les membres famille compris dans la Qd, et retourner un membre (parent ou conjoint)
     * 
     * @param idQdPrincipale
     * @param idTierDemande
     * @param session
     * @return
     * @throws Exception
     */
    public static String getIdTiersPaiementParQdPrincipale(String idQdPrincipale, String idTierDemande, BSession session)
            throws Exception {

        RFAssQdDossierJointDossierJointTiersManager rfAssQdDossierJointDossierJointTiersMgr = new RFAssQdDossierJointDossierJointTiersManager();
        rfAssQdDossierJointDossierJointTiersMgr.setSession(session);
        rfAssQdDossierJointDossierJointTiersMgr.setForIdQd(idQdPrincipale);
        rfAssQdDossierJointDossierJointTiersMgr.changeManagerSize(0);
        rfAssQdDossierJointDossierJointTiersMgr.find();

        Iterator<RFAssQdDossierJointDossierJointTiers> membresFamilleItr = rfAssQdDossierJointDossierJointTiersMgr
                .iterator();

        List<String> idTiersMembresFamille = new ArrayList<String>();

        SFSituationFamiliale sf = new SFSituationFamiliale();

        // Insertion de chaque membre famille de la Qd dans la situation familiale
        while (membresFamilleItr.hasNext()) {
            RFAssQdDossierJointDossierJointTiers tiers = membresFamilleItr.next();

            // PArcours uniquement si requerant ou conjoint.
            if (IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(tiers.getTypeRelation())
                    || IPCDroits.CS_ROLE_FAMILLE_CONJOINT.equals(tiers.getTypeRelation())
                    || (((!IPCDroits.CS_ROLE_FAMILLE_ENFANT.equals(tiers.getTypeRelation()))))) {
                // Insertion si idTiers est différent de l'idTierDemande.
                if (idTierDemande != tiers.getIdTiers()) {
                    idTiersMembresFamille.add(tiers.getIdTiers());
                }
            }
        }

        return RFDemandeService.findFirstIdTierMembreFamille(session, idTierDemande, null, idTiersMembresFamille);
    }
}