package process;

import globaz.commons.nss.NSUtil;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.adhesion.AFAdhesion;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.lienAffiliation.AFLienAffiliationViewBean;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliationViewBean;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.adressecourrier.TIAdresse;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresseManager;
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TIAliasViewBean;
import globaz.pyxis.db.tiers.TICompositionTiers;
import globaz.pyxis.db.tiers.TICompositionTiersViewBean;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.pyxis.db.tiers.TIHistoriqueAvsManager;
import globaz.pyxis.db.tiers.TIHistoriqueContribuable;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersViewBean;
import db.LAInsertionFichierViewBean;

public class LACreationTiersProcess {

    public static void creationAdresseCourrier(BSession sessionPyxis, String idTiers, LAInsertionFichierViewBean tiers,
            BTransaction transaction) throws Exception {
        TIAvoirAdresse avoirAdresseLegale = new TIAvoirAdresse();
        if (!JadeStringUtil.isEmpty(tiers.getIdCLocalite())) {
            // Si elle a déjà changé pour le même jour on update
            TIAvoirAdresseManager avAdrMng = new TIAvoirAdresseManager();
            TIAdresse adresseLegale = new TIAdresse();
            avAdrMng.setSession(sessionPyxis);
            avAdrMng.setForIdTiers(idTiers);
            avAdrMng.setForTypeAdresse(TIAvoirAdresse.CS_COURRIER);
            avAdrMng.setForDateEntreDebutEtFin(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));
            avAdrMng.find();
            if (avAdrMng.getSize() > 0) {
                avoirAdresseLegale = (TIAvoirAdresse) avAdrMng.getFirstEntity();
                // Mise à jour de l'adresse
                adresseLegale.setSession(sessionPyxis);
                adresseLegale.setIdAdresseUnique(avoirAdresseLegale.getIdAdresse());
                adresseLegale.retrieve();
                if (!adresseLegale.isNew()) {
                    // Création adresse
                    adresseLegale.setSession(sessionPyxis);
                    adresseLegale.setIdLocalite(tiers.getIdCLocalite());
                    adresseLegale.setRue(tiers.getAdresseCRue());
                    adresseLegale.setNumeroRue(tiers.getAdresseCNumero());
                    adresseLegale.setDateDebutAdresse(JACalendar.todayJJsMMsAAAA());
                    // adresseLegale.add(transaction);
                    TIAvoirAdresse adresse = new TIAvoirAdresse();
                    adresse.setSession(sessionPyxis);
                    adresse.setIdTiers(idTiers);
                    adresse.setIdAdresse(adresseLegale.getIdAdresse());
                    adresse.setRue(tiers.getAdresseCRue());
                    adresse.setNumeroRue(tiers.getAdresseCNumero());
                    adresse.setIdLocalite(tiers.getIdCLocalite());
                    adresse.setDateDebutRelation(JACalendar.todayJJsMMsAAAA());
                    adresse.setTypeAdresse(TIAvoirAdresse.CS_COURRIER);
                    // adresse.setByPassCheckDate(true);

                    TIAvoirAdresseManager manager = new TIAvoirAdresseManager();
                    manager.setSession(sessionPyxis);
                    // manager.setForIdApplication(idApplication);
                    manager.setForIdTiers(idTiers);
                    manager.setForTypeAdresse(TIAvoirAdresse.CS_COURRIER);

                    manager.setFromDateDebutRelation(JACalendar.todayJJsMMsAAAA());
                    int nb = manager.getCount();
                    if (nb == 1) {
                        // si il n'y a qu'une adresse, est-ce celle que l'on est
                        // en train de traiter
                        manager.find();
                        TIAvoirAdresse entity = (TIAvoirAdresse) manager.getEntity(0);
                        if (!entity.getIdAdresseIntUnique().equals(adresse.getIdAdresseIntUnique())) {
                            // adresse.update(transaction);
                            adresseLegale.setRue(tiers.getAdresseCRue());
                            adresseLegale.setNumeroRue(tiers.getAdresseCNumero());
                            adresseLegale.setIdLocalite(tiers.getIdCLocalite());
                            adresseLegale.setAllowModification(true);
                            adresseLegale.update(transaction);
                        }
                    } else if (nb > 1) {
                        // adresse.update(transaction);
                        adresseLegale.setRue(tiers.getAdresseCRue());
                        adresseLegale.setNumeroRue(tiers.getAdresseCNumero());
                        adresseLegale.setIdLocalite(tiers.getIdCLocalite());
                        adresseLegale.setAllowModification(true);
                        adresseLegale.update(transaction);
                    } else {
                        /*
                         * try{ adresseLegale.add(transaction); adresse.add(transaction); }catch (Exception e) {
                         */
                        adresseLegale.setRue(tiers.getAdresseCRue());
                        adresseLegale.setNumeroRue(tiers.getAdresseCNumero());
                        adresseLegale.setIdLocalite(tiers.getIdCLocalite());
                        adresseLegale.setAllowModification(true);
                        adresseLegale.update(transaction);
                        // }
                    }
                }
            } else {
                // Création adresse
                adresseLegale.setSession(sessionPyxis);
                adresseLegale.setIdLocalite(tiers.getIdCLocalite());
                adresseLegale.setRue(tiers.getAdresseCRue());
                adresseLegale.setNumeroRue(tiers.getAdresseCNumero());
                adresseLegale.setDateDebutAdresse(JACalendar.todayJJsMMsAAAA());
                adresseLegale.add(transaction);
                TIAvoirAdresse adresse = new TIAvoirAdresse();
                adresse.setSession(sessionPyxis);
                adresse.setIdTiers(idTiers);
                adresse.setIdAdresse(adresseLegale.getIdAdresse());
                adresse.setRue(tiers.getAdresseCRue());
                adresse.setNumeroRue(tiers.getAdresseCNumero());
                adresse.setIdLocalite(tiers.getIdCLocalite());
                adresse.setDateDebutRelation(JACalendar.todayJJsMMsAAAA());
                adresse.setTypeAdresse(TIAvoirAdresse.CS_COURRIER);
                // adresse.setByPassCheckDate(true);
                adresse.add(transaction);
            }
        }
    }

    public static void creationAdresseDomicile(BSession sessionPyxis, String idTiers, LAInsertionFichierViewBean tiers,
            BTransaction transaction) throws Exception {
        TIAvoirAdresse avoirAdresseLegale = new TIAvoirAdresse();
        if (!JadeStringUtil.isEmpty(tiers.getIdDLocalite())) {
            // Si elle a déjà changé pour le même jour on update
            TIAvoirAdresseManager avAdrMng = new TIAvoirAdresseManager();
            TIAdresse adresseLegale = new TIAdresse();
            avAdrMng.setSession(sessionPyxis);
            avAdrMng.setForIdTiers(idTiers);
            avAdrMng.setForTypeAdresse(TIAvoirAdresse.CS_DOMICILE);
            avAdrMng.setForDateEntreDebutEtFin(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));
            avAdrMng.find();
            if (avAdrMng.getSize() > 0) {
                avoirAdresseLegale = (TIAvoirAdresse) avAdrMng.getFirstEntity();
                // Mise à jour de l'adresse
                adresseLegale.setSession(sessionPyxis);
                adresseLegale.setIdAdresseUnique(avoirAdresseLegale.getIdAdresse());
                adresseLegale.retrieve();
                if (!adresseLegale.isNew()) {
                    // Création adresse
                    adresseLegale.setSession(sessionPyxis);
                    adresseLegale.setIdLocalite(tiers.getIdDLocalite());
                    adresseLegale.setRue(tiers.getAdresseDRue());
                    adresseLegale.setNumeroRue(tiers.getAdresseDNumero());
                    adresseLegale.setDateDebutAdresse(JACalendar.todayJJsMMsAAAA());
                    // adresseLegale.add(transaction);
                    TIAvoirAdresse adresse = new TIAvoirAdresse();
                    adresse.setSession(sessionPyxis);
                    adresse.setIdTiers(idTiers);
                    adresse.setIdAdresse(adresseLegale.getIdAdresse());
                    adresse.setRue(tiers.getAdresseDRue());
                    adresse.setNumeroRue(tiers.getAdresseDNumero());
                    adresse.setIdLocalite(tiers.getIdDLocalite());
                    adresse.setDateDebutRelation(JACalendar.todayJJsMMsAAAA());
                    adresse.setTypeAdresse(TIAvoirAdresse.CS_DOMICILE);
                    // adresse.setByPassCheckDate(true);

                    TIAvoirAdresseManager manager = new TIAvoirAdresseManager();
                    manager.setSession(sessionPyxis);
                    // manager.setForIdApplication(idApplication);
                    manager.setForIdTiers(idTiers);
                    manager.setForTypeAdresse(TIAvoirAdresse.CS_DOMICILE);

                    manager.setFromDateDebutRelation(JACalendar.todayJJsMMsAAAA());
                    int nb = manager.getCount();
                    if (nb == 1) {
                        // si il n'y a qu'une adresse, est-ce celle que l'on est
                        // en train de traiter
                        manager.find();
                        TIAvoirAdresse entity = (TIAvoirAdresse) manager.getEntity(0);
                        if (!entity.getIdAdresseIntUnique().equals(adresse.getIdAdresseIntUnique())) {
                            // adresse.update(transaction);
                            adresseLegale.setRue(tiers.getAdresseDRue());
                            adresseLegale.setNumeroRue(tiers.getAdresseDNumero());
                            adresseLegale.setIdLocalite(tiers.getIdDLocalite());
                            adresseLegale.setAllowModification(true);
                            adresseLegale.update(transaction);
                        }
                    } else if (nb > 1) {
                        // adresse.update(transaction);
                        adresseLegale.setRue(tiers.getAdresseDRue());
                        adresseLegale.setNumeroRue(tiers.getAdresseDNumero());
                        adresseLegale.setIdLocalite(tiers.getIdDLocalite());
                        adresseLegale.setAllowModification(true);
                        adresseLegale.update(transaction);
                    } else {
                        /*
                         * try{ adresseLegale.add(transaction); adresse.add(transaction); }catch (Exception e) {
                         */
                        adresseLegale.setRue(tiers.getAdresseDRue());
                        adresseLegale.setNumeroRue(tiers.getAdresseDNumero());
                        adresseLegale.setIdLocalite(tiers.getIdDLocalite());
                        adresseLegale.setAllowModification(true);
                        adresseLegale.update(transaction);
                        // }
                    }
                }
            } else {
                // Création adresse
                adresseLegale.setSession(sessionPyxis);
                adresseLegale.setIdLocalite(tiers.getIdDLocalite());
                adresseLegale.setRue(tiers.getAdresseDRue());
                adresseLegale.setNumeroRue(tiers.getAdresseDNumero());
                adresseLegale.setDateDebutAdresse(JACalendar.todayJJsMMsAAAA());
                adresseLegale.add(transaction);
                TIAvoirAdresse adresse = new TIAvoirAdresse();
                adresse.setSession(sessionPyxis);
                adresse.setIdTiers(idTiers);
                adresse.setIdAdresse(adresseLegale.getIdAdresse());
                adresse.setRue(tiers.getAdresseDRue());
                adresse.setNumeroRue(tiers.getAdresseDNumero());
                adresse.setIdLocalite(tiers.getIdDLocalite());
                adresse.setDateDebutRelation(JACalendar.todayJJsMMsAAAA());
                adresse.setTypeAdresse(TIAvoirAdresse.CS_DOMICILE);
                // adresse.setByPassCheckDate(true);
                adresse.add(transaction);
            }
        }
    }

    public static void creationAdresseSiege(BSession sessionPyxis, String idTiers, LAInsertionFichierViewBean tiers)
            throws Exception {
        if (!JadeStringUtil.isEmpty(tiers.getIdCLocalite())) {
            TIAdresse adresseLegale = new TIAdresse();
            TIAvoirAdresse avoirAdresseLegale = new TIAvoirAdresse();
            /* Recherche adresse du tiers */
            TIAvoirAdresseManager avAdrMng = new TIAvoirAdresseManager();
            avAdrMng.setSession(sessionPyxis);
            avAdrMng.setForIdTiers(idTiers);
            avAdrMng.setForTypeAdresse(TIAvoirAdresse.CS_COURRIER);
            avAdrMng.setForDateEntreDebutEtFin(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));
            avAdrMng.find();
            if (avAdrMng.getSize() > 0) {
                avoirAdresseLegale = (TIAvoirAdresse) avAdrMng.getFirstEntity();
                // Mise à jour de l'adresse
                adresseLegale.setSession(sessionPyxis);
                adresseLegale.setIdAdresseUnique(avoirAdresseLegale.getIdAdresse());
                adresseLegale.retrieve();
                if (!adresseLegale.isNew()) {
                    adresseLegale.setIdLocalite(tiers.getIdLocaliteParent());
                    adresseLegale.update();
                }
            } else {
                // Création adresse
                adresseLegale.setSession(sessionPyxis);
                adresseLegale.setIdLocalite(tiers.getIdLocaliteParent());
                adresseLegale.add();
                avoirAdresseLegale.setSession(sessionPyxis);
                avoirAdresseLegale.setIdTiers(idTiers);
                avoirAdresseLegale.setIdAdresse(adresseLegale.getIdAdresse());
                avoirAdresseLegale.setIdLocalite(tiers.getIdLocaliteParent());
                avoirAdresseLegale.setTypeAdresse(TIAvoirAdresse.CS_COURRIER);
                avoirAdresseLegale.add();
            }
        }
    }

    public static void creationAdresseSuccursale(BSession sessionPyxis, String idTiers, LAInsertionFichierViewBean tiers)
            throws Exception {
        if (!JadeStringUtil.isEmpty(tiers.getIdDLocalite())) {
            TIAdresse adresseLegale = new TIAdresse();
            TIAvoirAdresse avoirAdresseLegale = new TIAvoirAdresse();
            /* Recherche adresse du tiers */
            TIAvoirAdresseManager avAdrMng = new TIAvoirAdresseManager();
            avAdrMng.setSession(sessionPyxis);
            avAdrMng.setForIdTiers(idTiers);
            avAdrMng.setForTypeAdresse(TIAvoirAdresse.CS_DOMICILE);
            avAdrMng.setForDateEntreDebutEtFin(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));
            avAdrMng.find();
            if (avAdrMng.getSize() > 0) {
                avoirAdresseLegale = (TIAvoirAdresse) avAdrMng.getFirstEntity();
                // Mise à jour de l'adresse
                adresseLegale.setSession(sessionPyxis);
                adresseLegale.setIdAdresseUnique(avoirAdresseLegale.getIdAdresse());
                adresseLegale.retrieve();
                if (!adresseLegale.isNew()) {
                    adresseLegale.setIdLocalite(tiers.getIdLocaliteParent());
                    adresseLegale.setAllowModification(true);
                    adresseLegale.update();
                }
            } else {
                // Création adresse
                adresseLegale.setSession(sessionPyxis);
                adresseLegale.setIdLocalite(tiers.getIdLocaliteParent());
                adresseLegale.add();
                avoirAdresseLegale.setSession(sessionPyxis);
                avoirAdresseLegale.setIdTiers(idTiers);
                avoirAdresseLegale.setIdAdresse(adresseLegale.getIdAdresse());
                avoirAdresseLegale.setIdLocalite(tiers.getIdLocaliteParent());
            }
        }
    }

    // Crétation de l'affiliation
    public static AFAffiliation creationAffiliation(TITiersViewBean tiers, LAInsertionFichierViewBean tiersStruct,
            BSession session, BTransaction transaction) throws Exception {
        AFAffiliation affiliation = null;
        try {
            BSession sessionNaos = ((BSession) GlobazSystem.getApplication(AFApplication.DEFAULT_APPLICATION_NAOS)
                    .newSession(session));
            affiliation = new AFAffiliation();
            affiliation.setSession(sessionNaos);
            affiliation.setIdTiers(tiers.getIdTiers());
            affiliation.setDateDebut(JACalendar.todayJJsMMsAAAA());
            affiliation.setRaisonSociale(tiers.getDesignation1() + " " + tiers.getDesignation2());
            String raisonSocialeCourt = tiers.getDesignation1() + " " + tiers.getDesignation2();
            if (raisonSocialeCourt.length() > 30) {
                raisonSocialeCourt = raisonSocialeCourt.substring(0, 30);
            }
            affiliation.setRaisonSocialeCourt(raisonSocialeCourt);
            affiliation.setTypeAffiliation(CodeSystem.TYPE_AFFILI_FICHIER_CENT);// AFFILIE
            // CENTRAL
            affiliation.setPersonnaliteJuridique(tiersStruct.getFormeJuridique());
            affiliation.setMotifCreation(CodeSystem.MOTIF_AFFIL_NOUVELLE_AFFILIATION);

            affiliation.setAffilieNumero(tiersStruct.getNumAffilie());
            if (CodeSystem.TYPE_AFFILI_INDEP.equalsIgnoreCase(affiliation.getTypeAffiliation())) {
                affiliation.setDeclarationSalaire("");
                affiliation.setPersonnelMaison(Boolean.FALSE);
            }
            affiliation.add(transaction);
        } catch (Exception e) {
            throw new Exception("CreationAffiliation: " + e.getMessage());
        }
        return affiliation;
    }

    public static AFAffiliation creationAffiliationSiege(TITiersViewBean tiers, LAInsertionFichierViewBean tiersStruct,
            BSession session) throws Exception {
        AFAffiliation affiliation = null;
        AFAdhesion adhesion = null;
        try {
            BSession sessionNaos = ((BSession) GlobazSystem.getApplication(AFApplication.DEFAULT_APPLICATION_NAOS)
                    .newSession(session));
            AFAffiliationManager affiliationMng = new AFAffiliationManager();
            affiliationMng.setSession(sessionNaos);
            affiliationMng.setForAffilieNumero(tiersStruct.numAffilie);
            affiliationMng.setForTypesAffPersonelles();
            affiliationMng.find();
            if (affiliationMng.isEmpty()) {
                // Création de l'affiliation si elle n'existe pas
                affiliation = new AFAffiliation();
            } else {
                affiliation = (AFAffiliation) affiliationMng.getFirstEntity();
            }
            affiliation.setSession(sessionNaos);
            affiliation.setIdTiers(tiers.getIdTiers());
            affiliation.setDateDebut(JACalendar.todayJJsMMsAAAA());
            affiliation.setRaisonSociale(tiers.getDesignation1() + " " + tiers.getDesignation2());
            String raisonSocialeCourt = tiers.getDesignation1() + " " + tiers.getDesignation2();
            if (raisonSocialeCourt.length() > 30) {
                raisonSocialeCourt = raisonSocialeCourt.substring(0, 30);
            }
            affiliation.setRaisonSocialeCourt(raisonSocialeCourt);
            affiliation.setTypeAffiliation(CodeSystem.TYPE_AFFILI_FICHIER_CENT);// AFFILIE
            // CENTRAL
            affiliation.setPersonnaliteJuridique(tiersStruct.getFormeJuridique());

            affiliation.setAffilieNumero(tiersStruct.getNumAffilie());
            affiliation.setMotifCreation(CodeSystem.MOTIF_AFFIL_NOUVELLE_AFFILIATION);
            if (CodeSystem.TYPE_AFFILI_INDEP.equalsIgnoreCase(affiliation.getTypeAffiliation())) {
                affiliation.setDeclarationSalaire("");
                affiliation.setPersonnelMaison(Boolean.FALSE);
            }
            if (affiliationMng.isEmpty()) {
                affiliation.add();
                // Creation de l'adhesion
                adhesion = new AFAdhesion();
                adhesion.setSession(sessionNaos);
                adhesion.setAffiliationId(affiliation.getAffiliationId());
                adhesion.setDateDebut(JACalendar.todayJJsMMsAAAA());
                adhesion.setDateFin(affiliation.getDateFin());
                adhesion.setIdTiers(affiliation.getIdTiers());
                adhesion.add();
            } else {
                affiliation.update();
            }
        } catch (Exception e) {
            throw new Exception("CreationAffiliation: " + e.getMessage());
        }
        return affiliation;
    }

    private static void creationLien(LAInsertionFichierViewBean viewBean, AFAffiliation affiliation, BSession session,
            BSession sessionNaos, String typeLien, String idTiersEnfant, BTransaction transaction) throws Exception {
        if (viewBean.getSiege().equals("1")) {
            AFLienAffiliationViewBean lien = new AFLienAffiliationViewBean();
            lien.setSession(sessionNaos);
            lien.setAff_AffiliationId(viewBean.getIdParent());
            lien.setAffiliationId(affiliation.getAffiliationId());
            lien.setTypeLien(typeLien);
            lien.setDateDebut(JACalendar.todayJJsMMsAAAA());
            lien.add(transaction);
        } else {
            String idTiers = "";
            if ((!JadeStringUtil.isEmpty(viewBean.getNomParent1()))
                    && (!JadeStringUtil.isEmpty(viewBean.getIdLocaliteParent()))) {
                idTiers = LACreationTiersProcess.creationTiersSuccursale(viewBean, sessionNaos);
            } else {
                session.addError(session.getLabel("NOM_LOCALITE_MAISON_MERE_VIDE"));
            }
            if (!JadeStringUtil.isEmpty(idTiers)) {
                TICompositionTiersViewBean lienTiers = new TICompositionTiersViewBean();
                lienTiers.setTypeLien(TICompositionTiers.CS_SUCCURSALE);
                lienTiers.setIdTiersParent(idTiers);
                lienTiers.setIdTiersEnfant(idTiersEnfant);
                lienTiers.setSession(sessionNaos);
                lienTiers.add(transaction);
            }
        }
    }

    public static void creationLienAffiliation(LAInsertionFichierViewBean viewBean, BSession sessionLacerta,
            AFAffiliation affiliation, String idTiersEnfant, BTransaction transaction) throws Exception {
        BSession sessionNaos = ((BSession) GlobazSystem.getApplication(AFApplication.DEFAULT_APPLICATION_NAOS)
                .newSession(sessionLacerta));
        if (viewBean.getMotif().equals("12")) {
            LACreationTiersProcess.creationLien(viewBean, affiliation, sessionLacerta, sessionNaos,
                    CodeSystem.TYPE_LIEN_ASSOCIE, idTiersEnfant, transaction);
        } else if (viewBean.getMotif().equals("13")) {
            LACreationTiersProcess.creationLien(viewBean, affiliation, sessionLacerta, sessionNaos,
                    CodeSystem.TYPE_LIEN_SUCCURSALE, idTiersEnfant, transaction);
        }
    }

    /*
     * private static TITiersViewBean creationTiersSiege(LAInsertionFichierViewBean viewBean, BSession sessionNaos) {
     * TITiersViewBean tiers = new TITiersViewBean(); tiers.setSession(sessionNaos);
     * tiers.setDesignation1(viewBean.getNomParent1()); tiers.setDesignation2(viewBean.getNomParent2());
     * tiers.setDesignation3(viewBean.getNomParent3()); try { tiers.add(); return tiers; } catch (Exception e) { return
     * null; } }
     */

    public static void creationParticularites(LAInsertionFichierViewBean viewBean, BSession sessionLacerta,
            AFAffiliation affiliation, BTransaction transaction) throws Exception {
        if (!JadeStringUtil.isEmpty(viewBean.getPersonnelMaisonDateDebut())) {
            if (!affiliation.isNew()) {
                BSession sessionNaos = ((BSession) GlobazSystem.getApplication(AFApplication.DEFAULT_APPLICATION_NAOS)
                        .newSession(sessionLacerta));
                AFParticulariteAffiliationViewBean particularite = new AFParticulariteAffiliationViewBean();
                particularite.setSession(sessionNaos);
                particularite.setAffiliationId(affiliation.getAffiliationId());
                particularite.setDateDebut(viewBean.getPersonnelMaisonDateDebut());
                particularite.setDateFin(viewBean.getPersonnelMaisonDateFin());
                particularite.setParticularite(CodeSystem.PARTIC_AFFILIE_PERSONNEL_MAISON);
                particularite.add(transaction);
            }
        }
    }

    private static void creationSuiviCaisse(BTransaction transaction, LAInsertionFichierViewBean viewBean,
            AFAffiliation affiliation, BSession session, BSession sessionNaos, String genre, String codeAdministration,
            String dateDebut, String dateFin, String numAffilieCaisse) throws Exception {
        if (!JadeStringUtil.isEmpty(codeAdministration)) {
            // On va rechercher l'id de la caisse
            TIAdministrationManager managerAdministrationAVS = new TIAdministrationManager();
            managerAdministrationAVS.setSession(sessionNaos);
            managerAdministrationAVS.setForCodeAdministration(codeAdministration);
            managerAdministrationAVS.find();
            if (managerAdministrationAVS.size() > 0) {
                TIAdministrationViewBean administrationAVS = (TIAdministrationViewBean) managerAdministrationAVS
                        .getFirstEntity();
                AFSuiviCaisseAffiliation suiviCaisse = new AFSuiviCaisseAffiliation();
                suiviCaisse.setGenreCaisse(genre);
                suiviCaisse.setSession(sessionNaos);
                suiviCaisse.setIdTiersCaisse(administrationAVS.getIdTiers());
                suiviCaisse.setDateDebut(dateDebut);
                suiviCaisse.setDateFin(dateFin);
                suiviCaisse.setAffiliationId(affiliation.getAffiliationId());
                suiviCaisse.setNumeroAffileCaisse(numAffilieCaisse);
                suiviCaisse.add(transaction);
            } else {
                sessionNaos.addError(session.getLabel("CAISSE") + sessionNaos.getCodeLibelle(genre)
                        + session.getLabel("NON_TROUVEE") + " : " + codeAdministration);
            }
        }
    }

    // Crétation du Tiers ou mise à jour du Tiers s'il existe déjà
    public static void creationSuiviCaisse(LAInsertionFichierViewBean viewBean, BSession sessionLacerta,
            AFAffiliation affiliation, BTransaction transaction) throws Exception {
        BSession sessionNaos = ((BSession) GlobazSystem.getApplication(AFApplication.DEFAULT_APPLICATION_NAOS)
                .newSession(sessionLacerta));
        // SuiviCaisse AVS
        LACreationTiersProcess.creationSuiviCaisse(transaction, viewBean, affiliation, sessionLacerta, sessionNaos,
                CodeSystem.GENRE_CAISSE_AVS, viewBean.getCaisseAVSNum(), viewBean.getCaisseAVSDateDebut(),
                viewBean.getCaisseAVSDateFin(), viewBean.getNumAffilieAVS());
        // SuiviCaisse AF
        LACreationTiersProcess.creationSuiviCaisse(transaction, viewBean, affiliation, sessionLacerta, sessionNaos,
                CodeSystem.GENRE_CAISSE_AF, viewBean.getCaisseAFNum(), viewBean.getCaisseAFDateDebut(),
                viewBean.getCaisseAFDateFin(), viewBean.getNumAffilieAF());
        if (sessionNaos.hasErrors()) {
            sessionLacerta.addError(sessionNaos.getErrors().toString());
        }
    }

    // Création du Tiers ou mise à jour du Tiers s'il existe déjà
    public static TITiersViewBean creationTiers(LAInsertionFichierViewBean tiersStruct, BSession sessionPyxis,
            TITiersViewBean tiersActuel, BTransaction transaction) throws Exception {
        TITiersViewBean tiers = new TITiersViewBean();
        try {
            if (null != tiersActuel) {
                tiers = tiersActuel;
            }
            tiers.setSession(sessionPyxis);
            // Création du Tiers
            tiers.setDesignation1(tiersStruct.nom);
            tiers.setDesignation2(tiersStruct.prenom);
            tiers.setDesignation3(tiersStruct.nomSuite);
            tiers.setTitreTiers(tiersStruct.titre);
            tiers.setTypeTiers(TITiers.CS_TIERS);
            // tiers.setEtatCivil(tiersStruct.csEtatCivil);
            tiers.setSexe("INDEFINI");
            tiers.setNumAvsActuel(tiersStruct.numAvs);
            // tiers.setNumContribuableActuel(tiersStruct.numContribuable);
            tiers.setMotifModifContribuable(TIHistoriqueContribuable.CS_CREATION);
            tiers.setDateModifContribuable("01.01.2008");
            // tiers.setDateNaissance(tiersStruct.dateNaissance);
            tiers.setIdPays(TIPays.CS_SUISSE);
            tiers.setLangue(tiersStruct.getLangue());
            if ((!JadeStringUtil.isEmpty(tiersStruct.numAvs)) && (tiersStruct.numAvs.length() >= 9)) {
                String unformated = NSUtil.unFormatAVS(tiersStruct.numAvs);
                if (unformated.charAt(9) > '4') {
                    tiers.setIdPays("212");
                } else {
                    tiers.setIdPays(TIPays.CS_SUISSE);
                }
            }
            tiers.setPersonnePhysique(Boolean.TRUE);
            tiers.add(transaction);
            // Alias
            if (!JadeStringUtil.isEmpty(tiersStruct.getAlias())) {
                TIAliasViewBean alias = new TIAliasViewBean();
                alias.setSession(sessionPyxis);
                alias.setIdTiers(tiers.getIdTiers());
                alias.setLibelleAlias(tiersStruct.getAlias());
                alias.add(transaction);
            }
        } catch (Exception e) {
            throw new Exception("CreationTiers: " + e.getMessage());
        }
        return tiers;
    }

    public static String creationTiersSuccursale(LAInsertionFichierViewBean viewBean, BSession session)
            throws Exception {
        TITiersViewBean tiers = new TITiersViewBean();
        try {
            tiers.setSession(session);
            // Création du Tiers
            tiers.setDesignation1(viewBean.getNomParent1());
            tiers.setDesignation2(viewBean.getNomParent2());
            tiers.setDesignation3(viewBean.getNomParent3());
            tiers.setMotifModifContribuable(TIHistoriqueContribuable.CS_CREATION);
            tiers.setDateModifContribuable("01.01.2008");
            tiers.setIdPays(TIPays.CS_SUISSE);
            tiers.setPersonnePhysique(Boolean.FALSE);
            tiers.add();
            LACreationTiersProcess.creationAdresseSuccursale(session, tiers.getIdTiers(), viewBean);
            return tiers.getIdTiers();
        } catch (Exception e) {
            return "";
        }
    }

    public static void insertionFichier(BSession session, LAInsertionFichierViewBean viewBean, BTransaction transaction)
            throws Exception {
        // On récupère les tiers du fichier XML
        TITiersViewBean tiers = new TITiersViewBean();
        // On crée les tiers
        // Test si tiers existant
        TIHistoriqueAvsManager hAvsManager = new TIHistoriqueAvsManager();
        hAvsManager.setSession(session);
        hAvsManager.setForNumAvs(viewBean.getNumAvs());
        hAvsManager.find();
        if (hAvsManager.size() > 0) {
            tiers.setSession(session);
            tiers.setIdTiers(((TIHistoriqueAvs) hAvsManager.getFirstEntity()).getIdTiers());
            tiers.retrieve();
        } else {
            tiers = null;
        }
        tiers = LACreationTiersProcess.creationTiers(viewBean, session, tiers, transaction);
        // Mise à jour ou création adresse de courier
        LACreationTiersProcess.creationAdresseCourrier(session, tiers.getIdTiers(), viewBean, transaction);
        // Mise à jour ou création adresse de domicile
        LACreationTiersProcess.creationAdresseDomicile(session, tiers.getIdTiers(), viewBean, transaction);
        // Mise à jour ou création de l'affiliation
        AFAffiliation affiliation = LACreationTiersProcess.creationAffiliation(tiers, viewBean, session, transaction);
        // On va créer les liens de l'affiliation (cas de succursale)
        LACreationTiersProcess.creationLienAffiliation(viewBean, session, affiliation, tiers.getIdTiers(), transaction);
        // On va créer le suivi de caisse
        LACreationTiersProcess.creationSuiviCaisse(viewBean, session, affiliation, transaction);
        // Création particularités pour personnel de maison
        LACreationTiersProcess.creationParticularites(viewBean, session, affiliation, transaction);

    }

}
