package globaz.phenix.process.communications;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.crypto.JadeDecryptionNotSupportedException;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.crypto.JadeEncrypterNotFoundException;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.log.JadeLogger;
import globaz.jade.sedex.JadeSedexMessageNotHandledException;
import globaz.jade.sedex.annotation.OnReceive;
import globaz.jade.sedex.annotation.Setup;
import globaz.jade.sedex.message.SedexMessage;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.phenix.db.communications.CPCommentaireCommunication;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourSEDEXViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.db.communications.CPJournalRetourListViewBean;
import globaz.phenix.db.communications.CPReglePlausibilite;
import globaz.phenix.db.communications.CPSedexConjoint;
import globaz.phenix.db.communications.CPSedexContribuable;
import globaz.phenix.db.communications.CPSedexDonneesBase;
import globaz.phenix.db.communications.CPSedexDonneesCommerciales;
import globaz.phenix.db.communications.CPSedexDonneesPrivees;
import globaz.phenix.db.divers.CPPeriodeFiscale;
import globaz.phenix.db.divers.CPPeriodeFiscaleManager;
import globaz.phenix.toolbox.CPToolBox;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.pyxis.db.tiers.TIHistoriqueAvsManager;
import java.util.Properties;
import ch.eahv_iv.xmlns.eahv_iv_2011_000103._3.Message;

public class CPProcessSedexReception {
    private String passSedex = "";
    private BSession session = null;
    private String userSedex = "";

    private CPCommunicationFiscaleRetourSEDEXViewBean ajoutDeLaCommunication(BTransaction transaction, Message message)
            throws Exception {
        CPJournalRetourListViewBean jrnRetourManager = new CPJournalRetourListViewBean();
        jrnRetourManager.setSession(getSession());
        jrnRetourManager.setForTypeJournal(CPJournalRetour.CS_TYPE_JOURNAL_SEDEX);
        jrnRetourManager.setIsJournalEnCours(new Boolean(true));
        jrnRetourManager.find();
        CPJournalRetour jrn = null;
        if (jrnRetourManager.getSize() == 0) {
            jrn = CPJournalRetour.ouvertureNouveauJournal(getSession());
        } else {
            jrn = (CPJournalRetour) jrnRetourManager.getFirstEntity();
        }
        // On va rechercher le journal Sedex ouvert
        CPCommunicationFiscaleRetourSEDEXViewBean communicationFiscaleRetourBCK = new CPCommunicationFiscaleRetourSEDEXViewBean();
        CPCommunicationFiscaleRetourSEDEXViewBean communicationFiscaleRetour = new CPCommunicationFiscaleRetourSEDEXViewBean();
        communicationFiscaleRetour.setStatus(CPCommunicationFiscaleRetourViewBean.CS_RECEPTIONNE);
        communicationFiscaleRetour.setSession(getSession());
        communicationFiscaleRetour.setIdJournalRetour(jrn.getIdJournalRetour());

        communicationFiscaleRetourBCK.setStatus(CPCommunicationFiscaleRetourViewBean.CS_RECEPTIONNE);
        communicationFiscaleRetourBCK.setSession(getSession());
        communicationFiscaleRetourBCK.setIdJournalRetour(jrn.getIdJournalRetour());
        String annee = "";
        if (message.getHeader().getEventPeriod().getYear() != 0) {
            annee = String.valueOf(message.getHeader().getEventPeriod().getYear());
            communicationFiscaleRetour.setAnnee1(annee);
            communicationFiscaleRetourBCK.setAnnee1(annee);
            // On va rechercher l'idIfd
            CPPeriodeFiscaleManager periodeIFDManager = new CPPeriodeFiscaleManager();
            periodeIFDManager.setSession(getSession());
            periodeIFDManager.setForAnneeRevenuDebut(annee);
            periodeIFDManager.find();
            if (periodeIFDManager.size() > 0) {
                CPPeriodeFiscale periodeIFD = (CPPeriodeFiscale) periodeIFDManager.getFirstEntity();
                communicationFiscaleRetour.setIdIfd(periodeIFD.getIdIfd());
                communicationFiscaleRetour.setNumIfd(periodeIFD.getNumIfd());
                communicationFiscaleRetourBCK.setIdIfd(periodeIFD.getIdIfd());
                communicationFiscaleRetourBCK.setNumIfd(periodeIFD.getNumIfd());
            }
        }

        // calcul du revenu
        String revenuNet = String.valueOf(message.getContent().getSpBasicData().getEmploymentIncome());
        String revenuAVS = String.valueOf(message.getContent().getSpBasicData().getIncomeFromSelfEmployment());
        String revenuRente = String.valueOf(message.getContent().getSpBasicData().getPensionIncome());
        String revenuAgricole = String.valueOf(message.getContent().getSpBasicData().getIncomeInAgriculture());
        String rentePontEmployeur = String.valueOf(message.getContent().getSpBasicData().getOASIBridgingPension());
        // On somme les 4 si ils sont renseignées.
        double revenuTotal = 0.0;
        if (!JadeStringUtil.isEmpty(revenuNet) && !revenuNet.equalsIgnoreCase("null")) {
            revenuTotal += Double.valueOf(revenuNet).doubleValue();
        }
        if (!JadeStringUtil.isEmpty(revenuAVS) && !revenuAVS.equalsIgnoreCase("null")) {
            revenuTotal += Double.valueOf(revenuAVS).doubleValue();
        }
        if (!JadeStringUtil.isEmpty(revenuRente) && !revenuRente.equalsIgnoreCase("null")) {
            revenuTotal += Double.valueOf(revenuRente).doubleValue();
        }
        if (!JadeStringUtil.isEmpty(revenuAgricole) && !revenuAgricole.equalsIgnoreCase("null")) {
            revenuTotal += Double.valueOf(revenuAgricole).doubleValue();
        }
        if (!JadeStringUtil.isEmpty(rentePontEmployeur) && !rentePontEmployeur.equalsIgnoreCase("null")) {
            revenuTotal += Double.valueOf(rentePontEmployeur).doubleValue();
        }
        communicationFiscaleRetour.setRevenuAnnee1(String.valueOf(revenuTotal));
        communicationFiscaleRetourBCK.setRevenuAnnee1(String.valueOf(revenuTotal));
        // Fortune déterminante
        if (message.getContent().getSpBasicData().getAssets() != null) {
            communicationFiscaleRetour.setFortune(String.valueOf(message.getContent().getSpBasicData().getAssets()));
            communicationFiscaleRetourBCK.setFortune(String.valueOf(message.getContent().getSpBasicData().getAssets()));
        } else {
            communicationFiscaleRetour.setFortune("0");
            communicationFiscaleRetourBCK.setFortune("0");
        }
        // Capital Investi
        if (message.getContent().getSpBasicData().getCapital() != null) {
            communicationFiscaleRetour.setCapital(String.valueOf(message.getContent().getSpBasicData().getCapital()));
            communicationFiscaleRetourBCK
                    .setCapital(String.valueOf(message.getContent().getSpBasicData().getCapital()));
        } else {
            communicationFiscaleRetour.setCapital("0");
            communicationFiscaleRetourBCK.setCapital("0");
        }
        // Genre de décision /définitive - rectificative )
        // 1 = normal
        // 2 = spontanée
        // 4 et autre => rectificative
        if (message.getContent().getReportType() != null) {
            if (String.valueOf(message.getContent().getReportType()).equalsIgnoreCase("1")
                    || String.valueOf(message.getContent().getReportType()).equalsIgnoreCase("2")) {
                // Si taxation office
                if (String.valueOf(message.getContent().getAssessmentType()).equalsIgnoreCase("4")) {
                    communicationFiscaleRetour.setGenreTaxation(CPCommentaireCommunication.CS_GENRE_TO);
                } else {
                    // Définitive
                    communicationFiscaleRetour.setGenreTaxation(CPCommentaireCommunication.CS_GENRE_TD);
                }
            } else {
                // Si taxation office rectificative
                if (String.valueOf(message.getContent().getAssessmentType()).equalsIgnoreCase("4")) {
                    communicationFiscaleRetour.setGenreTaxation(CPCommentaireCommunication.CS_GENRE_TOR);
                } else {
                    // Rectificative
                    communicationFiscaleRetour.setGenreTaxation(CPCommentaireCommunication.CS_GENRE_TDR);
                }
            }
        }
        // ReportType
        if (message.getContent().getReportType() != null) {
            communicationFiscaleRetour.setReportType(String.valueOf(message.getContent().getReportType()));
            communicationFiscaleRetourBCK.setReportType(String.valueOf(message.getContent().getReportType()));
        } else {
            communicationFiscaleRetour.setReportType("");
            communicationFiscaleRetourBCK.setReportType("");
        }
        // On set le champ de recherche de l'affiliation NSS pour Sedex
        // Pour la recherche dans l'historique, il faut que le numéro soit
        // formaté.
        if (message.getContent().getTaxpayer().getVn() != null) {
            String vn = String.valueOf(message.getContent().getTaxpayer().getVn());
            if (vn == null) {
                vn = String.valueOf(message.getHeader().getObject().getVn());
            }
            if (vn != null) {
                communicationFiscaleRetour.setNssPourRecherche(NSUtil.formatAVSUnknown(vn));
                communicationFiscaleRetourBCK.setNssPourRecherche(NSUtil.formatAVSUnknown(vn));
            }
        }
        // On va rechercher le conjoint si il existe pour rajouter son id
        // Sinon il faudrait ajouter un conjoint inconnu.
        String numAvsEpouse = "";
        if (message.getContent().getSpouse() != null) {
            if (message.getContent().getSpouse().getVn() != null) {
                numAvsEpouse = String.valueOf(message.getContent().getSpouse().getVn());
            }
        }
        AFAffiliation affiliationEpouse = null;
        if (!JadeStringUtil.isEmpty(numAvsEpouse)) {
            // On recherche l'épouse d'après son numéro AVS
            TIHistoriqueAvsManager histo = new TIHistoriqueAvsManager();
            histo.setSession(getSession());
            histo.setForNumAvs(numAvsEpouse);
            histo.find();
            if (histo.size() > 0) {
                String idTiers = ((TIHistoriqueAvs) histo.getFirstEntity()).getIdTiers();
                affiliationEpouse = CPToolBox.returnAffiliation(getSession(), getSession()
                        .getCurrentThreadTransaction(), idTiers, annee, "", 1);
                if (idTiers != null) {
                    communicationFiscaleRetour.setIdConjoint(idTiers);
                    communicationFiscaleRetourBCK.setIdConjoint(idTiers);
                }
            } else {
                communicationFiscaleRetour.setIdConjoint("1");
                communicationFiscaleRetourBCK.setIdConjoint("1");
            }
            if ((affiliationEpouse != null) && !affiliationEpouse.isNew()) {
                communicationFiscaleRetour.setIdAffiliationConjoint(affiliationEpouse.getAffiliationId());
                communicationFiscaleRetourBCK.setIdAffiliationConjoint(affiliationEpouse.getAffiliationId());
            } else {
                communicationFiscaleRetour.setIdConjoint("1");
                communicationFiscaleRetourBCK.setIdConjoint("1");
            }
        }
        communicationFiscaleRetour.setDateRetour(JACalendar.todayJJsMMsAAAA());
        communicationFiscaleRetourBCK.setDateRetour(JACalendar.todayJJsMMsAAAA());
        if (JadeStringUtil.isEmpty(communicationFiscaleRetour.getIdConjoint())) {
            communicationFiscaleRetour.setIdConjoint("0");
            communicationFiscaleRetourBCK.setIdConjoint("0");
        }
        communicationFiscaleRetour.setWantPlausibilite(false);
        communicationFiscaleRetour.add(transaction);
        communicationFiscaleRetourBCK.setForBackup(true);
        communicationFiscaleRetourBCK.setIdRetour(communicationFiscaleRetour.getIdRetour());
        communicationFiscaleRetourBCK.setWantPlausibilite(false);
        communicationFiscaleRetourBCK.add(transaction);
        jrn.setTypeJournal(CPJournalRetour.CS_TYPE_JOURNAL_SEDEX);
        jrn.update(transaction);
        return communicationFiscaleRetour;
    }

    private void ajoutDesDonneesCommerciales(BTransaction transaction, Message message, String idRetour)
            throws Exception {
        CPSedexDonneesCommerciales donneesCommerciales = new CPSedexDonneesCommerciales();
        donneesCommerciales.setIdRetour(idRetour);
        donneesCommerciales.setSession(getSession());
        if (message.getContent().getSpBusinessData() != null) {
            if (message.getContent().getSpBusinessData().getBusinessDebts() != null) {
                donneesCommerciales.setDebts(String
                        .valueOf(message.getContent().getSpBusinessData().getBusinessDebts()));
            } else {
                donneesCommerciales.setDebts("");
            }
            if (message.getContent().getSpBusinessData().getCommencementOfSelfEmployment() != null) {
                if (String.valueOf(message.getContent().getSpBusinessData().getCommencementOfSelfEmployment()).length() > 10) {
                    donneesCommerciales.setCommencementOfSelfEmployment(String.valueOf(
                            message.getContent().getSpBusinessData().getCommencementOfSelfEmployment())
                            .substring(0, 10));
                } else {
                    donneesCommerciales.setCommencementOfSelfEmployment(String.valueOf(message.getContent()
                            .getSpBusinessData().getCommencementOfSelfEmployment()));
                }
            } else {
                donneesCommerciales.setCommencementOfSelfEmployment("");
            }
            if (message.getContent().getSpBusinessData().getEndOfSelfEmployment() != null) {
                if (String.valueOf(message.getContent().getSpBusinessData().getEndOfSelfEmployment()).length() > 10) {
                    donneesCommerciales.setEndOfSelfEmployment(String.valueOf(
                            message.getContent().getSpBusinessData().getEndOfSelfEmployment()).substring(0, 10));
                } else {
                    donneesCommerciales.setEndOfSelfEmployment(String.valueOf(message.getContent().getSpBusinessData()
                            .getEndOfSelfEmployment()));
                }
            } else {
                donneesCommerciales.setEndOfSelfEmployment("");
            }
            if (message.getContent().getSpBusinessData().getDebtInterest() != null) {
                donneesCommerciales.setDebtInterest(String.valueOf(message.getContent().getSpBusinessData()
                        .getDebtInterest()));
            } else {
                donneesCommerciales.setDebtInterest("");
            }
            if (message.getContent().getSpBusinessData().getDonations() != null) {
                donneesCommerciales.setDonations(String
                        .valueOf(message.getContent().getSpBusinessData().getDonations()));
            } else {
                donneesCommerciales.setDonations("");
            }
            if (message.getContent().getSpBusinessData().getMainIncome() != null) {
                donneesCommerciales.setMainIncome(String.valueOf(message.getContent().getSpBusinessData()
                        .getMainIncome()));
            } else {
                donneesCommerciales.setMainIncome("");
            }
            if (message.getContent().getSpBusinessData().getOtherIncome() != null) {
                donneesCommerciales.setOtherIncome(String.valueOf(message.getContent().getSpBusinessData()
                        .getOtherIncome()));
            } else {
                donneesCommerciales.setOtherIncome("");
            }
            if (message.getContent().getSpBusinessData().getSecuritiesIncome() != null) {
                donneesCommerciales.setSecuritiesIncome(String.valueOf(message.getContent().getSpBusinessData()
                        .getSecuritiesIncome()));
            } else {
                donneesCommerciales.setSecuritiesIncome("");
            }
            if (message.getContent().getSpBusinessData().getMainIncomeInAgriculture() != null) {
                donneesCommerciales.setMainIncomeInAgriculture(String.valueOf(message.getContent().getSpBusinessData()
                        .getMainIncomeInAgriculture()));
            } else {
                donneesCommerciales.setMainIncomeInAgriculture("");
            }
            if (message.getContent().getSpBusinessData().getMainIncomeInRealEstateTrade() != null) {
                donneesCommerciales.setMainIncomeInRealEstateTrade(String.valueOf(message.getContent()
                        .getSpBusinessData().getMainIncomeInRealEstateTrade()));
            } else {
                donneesCommerciales.setMainIncomeInRealEstateTrade("");
            }
            if (message.getContent().getSpBusinessData().getPartTimeEmployment() != null) {
                donneesCommerciales.setPartTimeEmployment(String.valueOf(message.getContent().getSpBusinessData()
                        .getPartTimeEmployment()));
            } else {
                donneesCommerciales.setPartTimeEmployment("");
            }
            if (message.getContent().getSpBusinessData().getPartTimeEmploymentInAgriculture() != null) {
                donneesCommerciales.setPartTimeEmploymentInAgriculture(String.valueOf(message.getContent()
                        .getSpBusinessData().getPartTimeEmploymentInAgriculture()));
            } else {
                donneesCommerciales.setPartTimeEmploymentInAgriculture("");
            }
            if (message.getContent().getSpBusinessData().getPartTimeEmploymentInRealEstateTrade() != null) {
                donneesCommerciales.setPartTimeEmploymentInRealEstateTrade(String.valueOf(message.getContent()
                        .getSpBusinessData().getPartTimeEmploymentInRealEstateTrade()));
            } else {
                donneesCommerciales.setPartTimeEmploymentInRealEstateTrade("");
            }
            if (message.getContent().getSpBusinessData().getRealisationProfit() != null) {
                donneesCommerciales.setRealisationProfit(String.valueOf(message.getContent().getSpBusinessData()
                        .getRealisationProfit()));
            } else {
                donneesCommerciales.setRealisationProfit("");
            }
            if (message.getContent().getSpBusinessData().getCooperativeShares() != null) {
                donneesCommerciales.setCooperativeShares(String.valueOf(message.getContent().getSpBusinessData()
                        .getCooperativeShares()));
            } else {
                donneesCommerciales.setCooperativeShares("");
            }
            if (message.getContent().getSpBusinessData().getBusinessResidencyEntitlement() != null) {
                donneesCommerciales.setResidencyEntitlement(String.valueOf(message.getContent().getSpBusinessData()
                        .getBusinessResidencyEntitlement()));
            } else {
                donneesCommerciales.setResidencyEntitlement("");
            }
            if (message.getContent().getSpBusinessData().getIncomeRealEstate() != null) {
                donneesCommerciales.setIncomeRealEstate(String.valueOf(message.getContent().getSpBusinessData()
                        .getIncomeRealEstate()));
            } else {
                donneesCommerciales.setIncomeRealEstate("");
            }
            if (message.getContent().getSpBusinessData().getBusinessSecurities() != null) {
                donneesCommerciales.setSecurities(String.valueOf(message.getContent().getSpBusinessData()
                        .getBusinessSecurities()));
            } else {
                donneesCommerciales.setSecurities("");
            }
            if (message.getContent().getSpBusinessData().getBusinessCash() != null) {
                donneesCommerciales.setCash(String.valueOf(message.getContent().getSpBusinessData().getBusinessCash()));
            } else {
                donneesCommerciales.setCash("");
            }
            if (message.getContent().getSpBusinessData().getBusinessAssets() != null) {
                donneesCommerciales.setAssets(String.valueOf(message.getContent().getSpBusinessData()
                        .getBusinessAssets()));
            } else {
                donneesCommerciales.setAssets("");
            }
            if (message.getContent().getSpBusinessData().getBusinessRealEstateProperties() != null) {
                donneesCommerciales.setRealEstateProperties(String.valueOf(message.getContent().getSpBusinessData()
                        .getBusinessRealEstateProperties()));
            } else {
                donneesCommerciales.setRealEstateProperties("");
            }
            if (message.getContent().getSpBusinessData().getTotalAssets() != null) {
                donneesCommerciales.setTotalAssets(String.valueOf(message.getContent().getSpBusinessData()
                        .getTotalAssets()));
            } else {
                donneesCommerciales.setTotalAssets("");
            }
        }
        // Données du conjoint
        if (message.getContent().getEpBusinessData() != null) {
            if (message.getContent().getEpBusinessData().getBusinessDebts() != null) {
                donneesCommerciales.setDebtsCjt(String.valueOf(message.getContent().getEpBusinessData()
                        .getBusinessDebts()));
            } else {
                donneesCommerciales.setDebtsCjt("");
            }
            if (message.getContent().getEpBusinessData().getCommencementOfSelfEmployment() != null) {
                if (String.valueOf(message.getContent().getEpBusinessData().getCommencementOfSelfEmployment()).length() > 10) {
                    donneesCommerciales.setCommencementOfSelfEmploymentCjt(String.valueOf(
                            message.getContent().getEpBusinessData().getCommencementOfSelfEmployment())
                            .substring(0, 10));
                } else {
                    donneesCommerciales.setCommencementOfSelfEmploymentCjt(String.valueOf(message.getContent()
                            .getEpBusinessData().getCommencementOfSelfEmployment()));
                }
            } else {
                donneesCommerciales.setCommencementOfSelfEmploymentCjt("");
            }
            if (message.getContent().getEpBusinessData().getEndOfSelfEmployment() != null) {
                if (String.valueOf(message.getContent().getEpBusinessData().getEndOfSelfEmployment()).length() > 10) {
                    donneesCommerciales.setEndOfSelfEmploymentCjt(String.valueOf(
                            message.getContent().getEpBusinessData().getEndOfSelfEmployment()).substring(0, 10));
                } else {
                    donneesCommerciales.setEndOfSelfEmploymentCjt(String.valueOf(message.getContent()
                            .getEpBusinessData().getEndOfSelfEmployment()));
                }
            } else {
                donneesCommerciales.setEndOfSelfEmploymentCjt("");
            }
            if (message.getContent().getEpBusinessData().getDebtInterest() != null) {
                donneesCommerciales.setDebtInterestCjt(String.valueOf(message.getContent().getEpBusinessData()
                        .getDebtInterest()));
            } else {
                donneesCommerciales.setDebtInterestCjt("");
            }
            if (message.getContent().getEpBusinessData().getDonations() != null) {
                donneesCommerciales.setDonationsCjt(String.valueOf(message.getContent().getEpBusinessData()
                        .getDonations()));
            } else {
                donneesCommerciales.setDonationsCjt("");
            }
            if (message.getContent().getEpBusinessData().getMainIncome() != null) {
                donneesCommerciales.setMainIncomeCjt(String.valueOf(message.getContent().getEpBusinessData()
                        .getMainIncome()));
            } else {
                donneesCommerciales.setMainIncomeCjt("");
            }
            if (message.getContent().getEpBusinessData().getOtherIncome() != null) {
                donneesCommerciales.setOtherIncomeCjt(String.valueOf(message.getContent().getEpBusinessData()
                        .getOtherIncome()));
            } else {
                donneesCommerciales.setOtherIncomeCjt("");
            }
            if (message.getContent().getEpBusinessData().getSecuritiesIncome() != null) {
                donneesCommerciales.setSecuritiesIncomeCjt(String.valueOf(message.getContent().getEpBusinessData()
                        .getSecuritiesIncome()));
            } else {
                donneesCommerciales.setSecuritiesIncomeCjt("");
            }
            if (message.getContent().getEpBusinessData().getMainIncomeInAgriculture() != null) {
                donneesCommerciales.setMainIncomeInAgricultureCjt(String.valueOf(message.getContent()
                        .getEpBusinessData().getMainIncomeInAgriculture()));
            } else {
                donneesCommerciales.setMainIncomeInAgricultureCjt("");
            }
            if (message.getContent().getEpBusinessData().getMainIncomeInRealEstateTrade() != null) {
                donneesCommerciales.setMainIncomeInRealEstateTradeCjt(String.valueOf(message.getContent()
                        .getEpBusinessData().getMainIncomeInRealEstateTrade()));
            } else {
                donneesCommerciales.setMainIncomeInRealEstateTradeCjt("");
            }
            if (message.getContent().getEpBusinessData().getPartTimeEmployment() != null) {
                donneesCommerciales.setPartTimeEmploymentCjt(String.valueOf(message.getContent().getEpBusinessData()
                        .getPartTimeEmployment()));
            } else {
                donneesCommerciales.setPartTimeEmploymentCjt("");
            }
            if (message.getContent().getEpBusinessData().getPartTimeEmploymentInAgriculture() != null) {
                donneesCommerciales.setPartTimeEmploymentInAgricultureCjt(String.valueOf(message.getContent()
                        .getEpBusinessData().getPartTimeEmploymentInAgriculture()));
            } else {
                donneesCommerciales.setPartTimeEmploymentInAgricultureCjt("");
            }
            if (message.getContent().getEpBusinessData().getPartTimeEmploymentInRealEstateTrade() != null) {
                donneesCommerciales.setPartTimeEmploymentInRealEstateTradeCjt(String.valueOf(message.getContent()
                        .getEpBusinessData().getPartTimeEmploymentInRealEstateTrade()));
            } else {
                donneesCommerciales.setPartTimeEmploymentInRealEstateTradeCjt("");
            }
            if (message.getContent().getEpBusinessData().getRealisationProfit() != null) {
                donneesCommerciales.setRealisationProfitCjt(String.valueOf(message.getContent().getEpBusinessData()
                        .getRealisationProfit()));
            } else {
                donneesCommerciales.setRealisationProfitCjt("");
            }
            if (message.getContent().getEpBusinessData().getCooperativeShares() != null) {
                donneesCommerciales.setCooperativeSharesCjt(String.valueOf(message.getContent().getEpBusinessData()
                        .getCooperativeShares()));
            } else {
                donneesCommerciales.setCooperativeSharesCjt("");
            }
            if (message.getContent().getEpBusinessData().getBusinessResidencyEntitlement() != null) {
                donneesCommerciales.setResidencyEntitlementCjt(String.valueOf(message.getContent().getEpBusinessData()
                        .getBusinessResidencyEntitlement()));
            } else {
                donneesCommerciales.setResidencyEntitlementCjt("");
            }
            if (message.getContent().getEpBusinessData().getIncomeRealEstate() != null) {
                donneesCommerciales.setIncomeRealEstateCjt(String.valueOf(message.getContent().getEpBusinessData()
                        .getIncomeRealEstate()));
            } else {
                donneesCommerciales.setIncomeRealEstateCjt("");
            }
            if (message.getContent().getEpBusinessData().getBusinessSecurities() != null) {
                donneesCommerciales.setSecuritiesCjt(String.valueOf(message.getContent().getEpBusinessData()
                        .getBusinessSecurities()));
            } else {
                donneesCommerciales.setSecuritiesCjt("");
            }
            if (message.getContent().getEpBusinessData().getBusinessCash() != null) {
                donneesCommerciales.setCashCjt(String.valueOf(message.getContent().getEpBusinessData()
                        .getBusinessCash()));
            } else {
                donneesCommerciales.setCashCjt("");
            }
            if (message.getContent().getEpBusinessData().getBusinessAssets() != null) {
                donneesCommerciales.setAssetsCjt(String.valueOf(message.getContent().getEpBusinessData()
                        .getBusinessAssets()));
            } else {
                donneesCommerciales.setAssetsCjt("");
            }
            if (message.getContent().getEpBusinessData().getBusinessRealEstateProperties() != null) {
                donneesCommerciales.setRealEstatePropertiesCjt(String.valueOf(message.getContent().getEpBusinessData()
                        .getBusinessRealEstateProperties()));
            } else {
                donneesCommerciales.setRealEstatePropertiesCjt("");
            }
            if (message.getContent().getEpBusinessData().getTotalAssets() != null) {
                donneesCommerciales.setTotalAssetsCjt(String.valueOf(message.getContent().getEpBusinessData()
                        .getTotalAssets()));
            } else {
                donneesCommerciales.setTotalAssetsCjt("");
            }
        }
        donneesCommerciales.add(transaction);
        donneesCommerciales.setForBackup(true);
        donneesCommerciales.add(transaction);
    }

    private void ajoutDesDonneesDeBase(BTransaction transaction, Message message, String idRetour) throws Exception {
        CPSedexDonneesBase donneesBase = new CPSedexDonneesBase();
        donneesBase.setIdRetour(idRetour);
        donneesBase.setSession(getSession());
        if (message.getContent().getSpBasicData() != null) {
            if (message.getContent().getSpBasicData().getEmploymentIncome() != null) {
                donneesBase.setEmploymentIncome(String.valueOf(message.getContent().getSpBasicData()
                        .getEmploymentIncome()));
            } else {
                donneesBase.setEmploymentIncome("");
            }
            if (message.getContent().getSpBasicData().getIncomeFromSelfEmployment() != null) {
                donneesBase.setIncomeFromSelfEmployment(String.valueOf(message.getContent().getSpBasicData()
                        .getIncomeFromSelfEmployment()));
            } else {
                donneesBase.setIncomeFromSelfEmployment("");
            }
            if (message.getContent().getSpBasicData().getPensionIncome() != null) {
                donneesBase.setPensionIncome(String.valueOf(message.getContent().getSpBasicData().getPensionIncome()));
            } else {
                donneesBase.setPensionIncome("");
            }
            if (message.getContent().getSpBasicData().getIncomeInAgriculture() != null) {
                donneesBase.setMainIncomeInAgriculture(String.valueOf(message.getContent().getSpBasicData()
                        .getIncomeInAgriculture()));
            } else {
                donneesBase.setMainIncomeInAgriculture("");
            }
            if (message.getContent().getSpBasicData().getCapital() != null) {
                donneesBase.setCapital(String.valueOf(message.getContent().getSpBasicData().getCapital()));
            } else {
                donneesBase.setCapital("");
            }
            if (message.getContent().getSpBasicData().getAssets() != null) {
                donneesBase.setAssets(String.valueOf(message.getContent().getSpBasicData().getAssets()));
            } else {
                donneesBase.setAssets("");
            }
            if (message.getContent().getSpBasicData().getIncomeFromSelfEmployment() != null) {
                donneesBase.setIncomeFromSelfEmployment(String.valueOf(message.getContent().getSpBasicData()
                        .getIncomeFromSelfEmployment()));
            } else {
                donneesBase.setIncomeFromSelfEmployment("");
            }
            if (message.getContent().getSpBasicData().getNonDomesticIncomePresent() != null) {
                donneesBase.setNonDomesticIncomePresent(String.valueOf(message.getContent().getSpBasicData()
                        .getNonDomesticIncomePresent()));
            } else {
                donneesBase.setNonDomesticIncomePresent("");
            }
            if (message.getContent().getSpBasicData().getPurchasingLPP() != null) {
                donneesBase.setPurchasingLPP(String.valueOf(message.getContent().getSpBasicData().getPurchasingLPP()));
            } else {
                donneesBase.setPurchasingLPP("");
            }
            if (message.getContent().getSpBasicData().getOASIBridgingPension() != null) {
                donneesBase.setOASIBridgingPension(String.valueOf(message.getContent().getSpBasicData()
                        .getOASIBridgingPension()));
            } else {
                donneesBase.setOASIBridgingPension("");
            }
        }
        // Données du conjoint
        if (message.getContent().getEpBasicData() != null) {
            if (message.getContent().getEpBasicData().getEmploymentIncome() != null) {
                donneesBase.setEmploymentIncomeCjt(String.valueOf(message.getContent().getEpBasicData()
                        .getEmploymentIncome()));
            } else {
                donneesBase.setEmploymentIncomeCjt("");
            }
            if (message.getContent().getEpBasicData().getIncomeFromSelfEmployment() != null) {
                donneesBase.setIncomeFromSelfEmploymentCjt(String.valueOf(message.getContent().getEpBasicData()
                        .getIncomeFromSelfEmployment()));
            } else {
                donneesBase.setIncomeFromSelfEmploymentCjt("");
            }
            if (message.getContent().getEpBasicData().getPensionIncome() != null) {
                donneesBase.setPensionIncomeCjt(String
                        .valueOf(message.getContent().getEpBasicData().getPensionIncome()));
            } else {
                donneesBase.setPensionIncomeCjt("");
            }
            if (message.getContent().getEpBasicData().getIncomeInAgriculture() != null) {
                donneesBase.setMainIncomeInAgricultureCjt(String.valueOf(message.getContent().getEpBasicData()
                        .getIncomeInAgriculture()));
            } else {
                donneesBase.setMainIncomeInAgricultureCjt("");
            }
            if (message.getContent().getEpBasicData().getCapital() != null) {
                donneesBase.setCapitalCjt(String.valueOf(message.getContent().getEpBasicData().getCapital()));
            } else {
                donneesBase.setCapitalCjt("");
            }
            if (message.getContent().getEpBasicData().getAssets() != null) {
                donneesBase.setAssetsCjt(String.valueOf(message.getContent().getEpBasicData().getAssets()));
            } else {
                donneesBase.setAssetsCjt("");
            }
            if (message.getContent().getEpBasicData().getIncomeFromSelfEmployment() != null) {
                donneesBase.setIncomeFromSelfEmploymentCjt(String.valueOf(message.getContent().getEpBasicData()
                        .getIncomeFromSelfEmployment()));
            } else {
                donneesBase.setIncomeFromSelfEmploymentCjt("");
            }
            if (message.getContent().getEpBasicData().getNonDomesticIncomePresent() != null) {
                donneesBase.setNonDomesticIncomePresentCjt(String.valueOf(message.getContent().getEpBasicData()
                        .getNonDomesticIncomePresent()));
            } else {
                donneesBase.setNonDomesticIncomePresentCjt("");
            }
            if (message.getContent().getEpBasicData().getPurchasingLPP() != null) {
                donneesBase.setPurchasingLPPCjt(String
                        .valueOf(message.getContent().getEpBasicData().getPurchasingLPP()));
            } else {
                donneesBase.setPurchasingLPPCjt("");
            }
            if (message.getContent().getEpBasicData().getOASIBridgingPension() != null) {
                donneesBase.setOASIBridgingPensionCjt(String.valueOf(message.getContent().getEpBasicData()
                        .getOASIBridgingPension()));
            } else {
                donneesBase.setOASIBridgingPensionCjt("");
            }
        }
        donneesBase.add(transaction);
        donneesBase.setForBackup(true);
        donneesBase.add(transaction);
    }

    private void ajoutDesDonneesPrivees(BTransaction transaction, Message message, String idRetour) throws Exception {
        CPSedexDonneesPrivees donneesPrivees = new CPSedexDonneesPrivees();
        donneesPrivees.setSession(getSession());
        donneesPrivees.setIdRetour(idRetour);
        if (message.getContent().getSpPrivateData() != null) {
            if (message.getContent().getSpPrivateData().getMotorVehicle() != null) {
                donneesPrivees.setMotorVehicle(String
                        .valueOf(message.getContent().getSpPrivateData().getMotorVehicle()));
            } else {
                donneesPrivees.setMotorVehicle("");
            }
            if (message.getContent().getSpPrivateData().getOtherAssets() != null) {
                donneesPrivees.setOtherAssets(String.valueOf(message.getContent().getSpPrivateData().getOtherAssets()));
            } else {
                donneesPrivees.setOtherAssets("");
            }
            if (message.getContent().getSpPrivateData().getOtherPensions() != null) {
                donneesPrivees.setOtherPensions(String.valueOf(message.getContent().getSpPrivateData()
                        .getOtherPensions()));
            } else {
                donneesPrivees.setOtherPensions("");
            }
            if (message.getContent().getSpPrivateData().getPensions() != null) {
                donneesPrivees.setPensions(String.valueOf(message.getContent().getSpPrivateData().getPensions()));
            } else {
                donneesPrivees.setPensions("");
            }
            if (message.getContent().getSpPrivateData().getPensionsPillar1() != null) {
                donneesPrivees.setPensionsPillar1(String.valueOf(message.getContent().getSpPrivateData()
                        .getPensionsPillar1()));
            } else {
                donneesPrivees.setPensionsPillar1("");
            }
            if (message.getContent().getSpPrivateData().getPensionsPillar2() != null) {
                donneesPrivees.setPensionsPillar2(String.valueOf(message.getContent().getSpPrivateData()
                        .getPensionsPillar2()));
            } else {
                donneesPrivees.setPensionsPillar2("");
            }
            if (message.getContent().getSpPrivateData().getPensionsPillar3A() != null) {
                donneesPrivees.setPensionsPillar3a(String.valueOf(message.getContent().getSpPrivateData()
                        .getPensionsPillar3A()));
            } else {
                donneesPrivees.setPensionsPillar3a("");
            }
            if (message.getContent().getSpPrivateData().getPensionsPillar3B() != null) {
                donneesPrivees.setPensionsPillar3b(String.valueOf(message.getContent().getSpPrivateData()
                        .getPensionsPillar3B()));
            } else {
                donneesPrivees.setPensionsPillar3b("");
            }
            if (message.getContent().getSpPrivateData().getAnnuities() != null) {
                donneesPrivees.setAnnuities(String.valueOf(message.getContent().getSpPrivateData().getAnnuities()));
            } else {
                donneesPrivees.setAnnuities("");
            }
            if (message.getContent().getSpPrivateData().getMilitaryInsurancePensions() != null) {
                donneesPrivees.setMilitaryInsurancePensions(String.valueOf(message.getContent().getSpPrivateData()
                        .getMilitaryInsurancePensions()));
            } else {
                donneesPrivees.setMilitaryInsurancePensions("");
            }
            if (message.getContent().getSpPrivateData().getPerDiemAllowance() != null) {
                donneesPrivees.setPerDiemAllowance(String.valueOf(message.getContent().getSpPrivateData()
                        .getPerDiemAllowance()));
            } else {
                donneesPrivees.setPerDiemAllowance("");
            }
            if (message.getContent().getSpPrivateData().getMaintenanceContribution() != null) {
                donneesPrivees.setMaintenanceContribution(String.valueOf(message.getContent().getSpPrivateData()
                        .getMaintenanceContribution()));
            } else {
                donneesPrivees.setMaintenanceContribution("");
            }
            if (message.getContent().getSpPrivateData().getChildAllowances() != null) {
                donneesPrivees.setChildAllowance(String.valueOf(message.getContent().getSpPrivateData()
                        .getChildAllowances()));
            } else {
                donneesPrivees.setChildAllowance("");
            }
            if (message.getContent().getSpPrivateData().getPatentLicense() != null) {
                donneesPrivees.setPatentLicense(String.valueOf(message.getContent().getSpPrivateData()
                        .getPatentLicense()));
            } else {
                donneesPrivees.setPatentLicense("");
            }
            if (message.getContent().getSpPrivateData().getPrivateResidencyEntitlement() != null) {
                donneesPrivees.setResidencyEntitlement(String.valueOf(message.getContent().getSpPrivateData()
                        .getPrivateResidencyEntitlement()));
            } else {
                donneesPrivees.setResidencyEntitlement("");
            }
            if (message.getContent().getSpPrivateData().getPrivateSecurities() != null) {
                donneesPrivees.setSecurities(String.valueOf(message.getContent().getSpPrivateData()
                        .getPrivateSecurities()));
            } else {
                donneesPrivees.setSecurities("");
            }
            if (message.getContent().getSpPrivateData().getPrivateCash() != null) {
                donneesPrivees.setCash(String.valueOf(message.getContent().getSpPrivateData().getPrivateCash()));
            } else {
                donneesPrivees.setCash("");
            }
            if (message.getContent().getSpPrivateData().getLifeInsurance() != null) {
                donneesPrivees.setLifeInsurance(String.valueOf(message.getContent().getSpPrivateData()
                        .getLifeInsurance()));
            } else {
                donneesPrivees.setLifeInsurance("");
            }
            if (message.getContent().getSpPrivateData().getInheritance() != null) {
                donneesPrivees.setInheritance(String.valueOf(message.getContent().getSpPrivateData().getInheritance()));
            } else {
                donneesPrivees.setInheritance("");
            }
            if (message.getContent().getSpPrivateData().getPrivateRealEstateProperties() != null) {
                donneesPrivees.setRealEstateProperties(String.valueOf(message.getContent().getSpPrivateData()
                        .getPrivateRealEstateProperties()));
            } else {
                donneesPrivees.setRealEstateProperties("");
            }
            if (message.getContent().getSpPrivateData().getCompanyShares() != null) {
                donneesPrivees.setCompanyShares(String.valueOf(message.getContent().getSpPrivateData()
                        .getCompanyShares()));
            } else {
                donneesPrivees.setCompanyShares("");
            }
            if (message.getContent().getSpPrivateData().getPrivateDebts() != null) {
                donneesPrivees.setDebts(String.valueOf(message.getContent().getSpPrivateData().getPrivateDebts()));
            } else {
                donneesPrivees.setDebts("");
            }
            if (message.getContent().getSpPrivateData().getTaxableIncomeDBG() != null) {
                donneesPrivees.setTaxableIncomeInAccordanceWithDBG(String.valueOf(message.getContent()
                        .getSpPrivateData().getTaxableIncomeDBG()));
            } else {
                donneesPrivees.setTaxableIncomeInAccordanceWithDBG("");
            }
            if (message.getContent().getSpPrivateData().getTaxableIncomeExpenseTaxation() != null) {
                donneesPrivees.setTaxableIncomeExpenseTaxation(String.valueOf(message.getContent().getSpPrivateData()
                        .getTaxableIncomeExpenseTaxation()));
            } else {
                donneesPrivees.setTaxableIncomeExpenseTaxation("");
            }
        }
        // Données du conjoint
        if (message.getContent().getEpPrivateData() != null) {
            if (message.getContent().getEpPrivateData().getMotorVehicle() != null) {
                donneesPrivees.setMotorVehicleCjt(String.valueOf(message.getContent().getEpPrivateData()
                        .getMotorVehicle()));
            } else {
                donneesPrivees.setMotorVehicleCjt("");
            }
            if (message.getContent().getEpPrivateData().getOtherAssets() != null) {
                donneesPrivees.setOtherAssetsCjt(String.valueOf(message.getContent().getEpPrivateData()
                        .getOtherAssets()));
            } else {
                donneesPrivees.setOtherAssetsCjt("");
            }
            if (message.getContent().getEpPrivateData().getOtherPensions() != null) {
                donneesPrivees.setOtherPensionsCjt(String.valueOf(message.getContent().getEpPrivateData()
                        .getOtherPensions()));
            } else {
                donneesPrivees.setOtherPensionsCjt("");
            }
            if (message.getContent().getEpPrivateData().getPensions() != null) {
                donneesPrivees.setPensionsCjt(String.valueOf(message.getContent().getEpPrivateData().getPensions()));
            } else {
                donneesPrivees.setPensionsCjt("");
            }
            if (message.getContent().getEpPrivateData().getPensionsPillar1() != null) {
                donneesPrivees.setPensionsPillar1Cjt(String.valueOf(message.getContent().getEpPrivateData()
                        .getPensionsPillar1()));
            } else {
                donneesPrivees.setPensionsPillar1Cjt("");
            }
            if (message.getContent().getEpPrivateData().getPensionsPillar2() != null) {
                donneesPrivees.setPensionsPillar2Cjt(String.valueOf(message.getContent().getEpPrivateData()
                        .getPensionsPillar2()));
            } else {
                donneesPrivees.setPensionsPillar2Cjt("");
            }
            if (message.getContent().getEpPrivateData().getPensionsPillar3A() != null) {
                donneesPrivees.setPensionsPillar3aCjt(String.valueOf(message.getContent().getEpPrivateData()
                        .getPensionsPillar3A()));
            } else {
                donneesPrivees.setPensionsPillar3aCjt("");
            }
            if (message.getContent().getEpPrivateData().getPensionsPillar3B() != null) {
                donneesPrivees.setPensionsPillar3bCjt(String.valueOf(message.getContent().getEpPrivateData()
                        .getPensionsPillar3B()));
            } else {
                donneesPrivees.setPensionsPillar3bCjt("");
            }
            if (message.getContent().getEpPrivateData().getAnnuities() != null) {
                donneesPrivees.setAnnuitiesCjt(String.valueOf(message.getContent().getEpPrivateData().getAnnuities()));
            } else {
                donneesPrivees.setAnnuitiesCjt("");
            }
            if (message.getContent().getEpPrivateData().getMilitaryInsurancePensions() != null) {
                donneesPrivees.setMilitaryInsurancePensionsCjt(String.valueOf(message.getContent().getEpPrivateData()
                        .getMilitaryInsurancePensions()));
            } else {
                donneesPrivees.setMilitaryInsurancePensionsCjt("");
            }
            if (message.getContent().getEpPrivateData().getPerDiemAllowance() != null) {
                donneesPrivees.setPerDiemAllowanceCjt(String.valueOf(message.getContent().getEpPrivateData()
                        .getPerDiemAllowance()));
            } else {
                donneesPrivees.setPerDiemAllowanceCjt("");
            }
            if (message.getContent().getEpPrivateData().getMaintenanceContribution() != null) {
                donneesPrivees.setMaintenanceContributionCjt(String.valueOf(message.getContent().getEpPrivateData()
                        .getMaintenanceContribution()));
            } else {
                donneesPrivees.setMaintenanceContributionCjt("");
            }
            if (message.getContent().getEpPrivateData().getChildAllowances() != null) {
                donneesPrivees.setChildAllowanceCjt(String.valueOf(message.getContent().getEpPrivateData()
                        .getChildAllowances()));
            } else {
                donneesPrivees.setChildAllowanceCjt("");
            }
            if (message.getContent().getEpPrivateData().getPatentLicense() != null) {
                donneesPrivees.setPatentLicenseCjt(String.valueOf(message.getContent().getEpPrivateData()
                        .getPatentLicense()));
            } else {
                donneesPrivees.setPatentLicenseCjt("");
            }
            if (message.getContent().getEpPrivateData().getPrivateResidencyEntitlement() != null) {
                donneesPrivees.setResidencyEntitlementCjt(String.valueOf(message.getContent().getEpPrivateData()
                        .getPrivateResidencyEntitlement()));
            } else {
                donneesPrivees.setResidencyEntitlementCjt("");
            }
            if (message.getContent().getEpPrivateData().getPrivateSecurities() != null) {
                donneesPrivees.setSecuritiesCjt(String.valueOf(message.getContent().getEpPrivateData()
                        .getPrivateSecurities()));
            } else {
                donneesPrivees.setSecuritiesCjt("");
            }
            if (message.getContent().getEpPrivateData().getPrivateCash() != null) {
                donneesPrivees.setCashCjt(String.valueOf(message.getContent().getEpPrivateData().getPrivateCash()));
            } else {
                donneesPrivees.setCashCjt("");
            }
            if (message.getContent().getEpPrivateData().getLifeInsurance() != null) {
                donneesPrivees.setLifeInsuranceCjt(String.valueOf(message.getContent().getEpPrivateData()
                        .getLifeInsurance()));
            } else {
                donneesPrivees.setLifeInsuranceCjt("");
            }
            if (message.getContent().getEpPrivateData().getInheritance() != null) {
                donneesPrivees.setInheritanceCjt(String.valueOf(message.getContent().getEpPrivateData()
                        .getInheritance()));
            } else {
                donneesPrivees.setInheritanceCjt("");
            }
            if (message.getContent().getEpPrivateData().getPrivateRealEstateProperties() != null) {
                donneesPrivees.setRealEstatePropertiesCjt(String.valueOf(message.getContent().getEpPrivateData()
                        .getPrivateRealEstateProperties()));
            } else {
                donneesPrivees.setRealEstatePropertiesCjt("");
            }
            if (message.getContent().getEpPrivateData().getCompanyShares() != null) {
                donneesPrivees.setCompanySharesCjt(String.valueOf(message.getContent().getEpPrivateData()
                        .getCompanyShares()));
            } else {
                donneesPrivees.setCompanySharesCjt("");
            }
            if (message.getContent().getEpPrivateData().getPrivateDebts() != null) {
                donneesPrivees.setDebtsCjt(String.valueOf(message.getContent().getEpPrivateData().getPrivateDebts()));
            } else {
                donneesPrivees.setDebtsCjt("");
            }
            if (message.getContent().getEpPrivateData().getTaxableIncomeDBG() != null) {
                donneesPrivees.setTaxableIncomeInAccordanceWithDBGCjt(String.valueOf(message.getContent()
                        .getEpPrivateData().getTaxableIncomeDBG()));
            } else {
                donneesPrivees.setTaxableIncomeInAccordanceWithDBGCjt("");
            }
            if (message.getContent().getEpPrivateData().getTaxableIncomeExpenseTaxation() != null) {
                donneesPrivees.setTaxableIncomeExpenseTaxationCjt(String.valueOf(message.getContent()
                        .getEpPrivateData().getTaxableIncomeExpenseTaxation()));
            } else {
                donneesPrivees.setTaxableIncomeExpenseTaxationCjt("");
            }
        }

        donneesPrivees.add(transaction);
        donneesPrivees.setForBackup(true);
        donneesPrivees.add(transaction);
    }

    private void ajoutDesInfosDuConjoint(BTransaction transaction, Message message, String idRetour) throws Exception {
        CPSedexConjoint conjoint = new CPSedexConjoint();
        conjoint.setSession(getSession());
        conjoint.setIdRetour(idRetour);
        if (message.getContent().getSpouse() != null) {
            if (message.getContent().getSpouse().getVn() != null) {
                conjoint.setVn(String.valueOf(message.getContent().getSpouse().getVn()));
            } else {
                conjoint.setVn("");
            }
            if (message.getContent().getSpouse().getOtherPersonId().size() > 0) {
                if (message.getContent().getSpouse().getOtherPersonId().get(0) != null) {
                    conjoint.setPersonIdCategory(message.getContent().getSpouse().getOtherPersonId().get(0)
                            .getPersonIdCategory());
                    conjoint.setPersonId(message.getContent().getSpouse().getOtherPersonId().get(0).getPersonId());
                } else {
                    conjoint.setPersonIdCategory("");
                    conjoint.setPersonId("");
                }
            } else {
                conjoint.setPersonIdCategory("");
                conjoint.setPersonId("");
            }
            if (message.getContent().getSpouse().getOfficialName() != null) {
                conjoint.setOfficialName(message.getContent().getSpouse().getOfficialName());
            } else {
                conjoint.setOfficialName("");
            }
            if (message.getContent().getSpouse().getFirstName() != null) {
                conjoint.setFirstName(message.getContent().getSpouse().getFirstName());
            } else {
                conjoint.setFirstName("");
            }
            if (message.getContent().getSpouse().getSex() != null) {
                conjoint.setSex(message.getContent().getSpouse().getSex());
            } else {
                conjoint.setSex("");
            }
            if (message.getContent().getSpouse().getDateOfBirth().getYearMonthDay() != null) {
                if (String.valueOf(message.getContent().getSpouse().getDateOfBirth().getYearMonthDay()).length() > 10) {
                    conjoint.setYearMonthDay(String.valueOf(
                            message.getContent().getSpouse().getDateOfBirth().getYearMonthDay()).substring(0, 10));
                } else {
                    conjoint.setYearMonthDay(String.valueOf(message.getContent().getSpouse().getDateOfBirth()
                            .getYearMonthDay()));
                }
            } else {
                conjoint.setYearMonthDay("");
            }
            if (message.getContent().getSpouse().getAddress() != null) {
                if (message.getContent().getSpouse().getAddress().getStreet() != null) {
                    conjoint.setStreet(message.getContent().getSpouse().getAddress().getStreet());
                } else {
                    conjoint.setStreet("");
                }
                if (message.getContent().getSpouse().getAddress().getHouseNumber() != null) {
                    conjoint.setHouseNumber(message.getContent().getSpouse().getAddress().getHouseNumber());
                } else {
                    conjoint.setHouseNumber("");
                }
                if (message.getContent().getSpouse().getAddress().getTown() != null) {
                    conjoint.setTown(message.getContent().getSpouse().getAddress().getTown());
                } else {
                    conjoint.setTown("");
                }
                if (message.getContent().getSpouse().getAddress().getSwissZipCode() != null) {
                    conjoint.setSwissZipCode(String.valueOf(message.getContent().getSpouse().getAddress()
                            .getSwissZipCode()));
                } else {
                    conjoint.setSwissZipCode("");
                }
                if (message.getContent().getSpouse().getAddress().getCountry() != null) {
                    conjoint.setCountry(message.getContent().getSpouse().getAddress().getCountry());
                } else {
                    conjoint.setCountry("");
                }
            }
            if (message.getContent().getSpouse().getMaritalStatus() != null) {
                conjoint.setMaritalStatus(message.getContent().getSpouse().getMaritalStatus());
            } else {
                conjoint.setMaritalStatus("");
            }
            if (message.getContent().getSpouse().getDateOfMaritalStatus() != null) {
                conjoint.setDateOfMaritalStatus(String.valueOf(message.getContent().getSpouse()
                        .getDateOfMaritalStatus()));
            } else {
                conjoint.setDateOfMaritalStatus("");
            }
            if (message.getContent().getSpouse().getDateOfEntry() != null) {
                conjoint.setDateOfEntry(String.valueOf(message.getContent().getSpouse().getDateOfEntry()));
            } else {
                conjoint.setDateOfEntry("");
            }
        }
        conjoint.add(transaction);
        conjoint.setForBackup(true);
        conjoint.add(transaction);
    }

    private void ajoutDesInfosDuContribuable(BTransaction transaction, Message message, String idRetour)
            throws Exception {
        CPSedexContribuable contribuable = new CPSedexContribuable();
        contribuable.setSession(getSession());
        contribuable.setIdRetour(idRetour);
        if (message.getContent().getTaxpayer().getLocalPersonId() != null) {
            contribuable.setLocalPersonId(message.getContent().getTaxpayer().getLocalPersonId().getPersonId());
            contribuable.setLocalPersonIdCategory(message.getContent().getTaxpayer().getLocalPersonId()
                    .getPersonIdCategory());
        } else {
            contribuable.setLocalPersonId("");
            contribuable.setLocalPersonIdCategory("");
        }
        contribuable.setSenderId(message.getHeader().getSenderId());
        contribuable.setMessageId(message.getHeader().getMessageId());
        contribuable.setReferenceMessageId(message.getHeader().getReferenceMessageId());
        contribuable.setOurBusinessReferenceID(message.getHeader().getOurBusinessReferenceID());
        contribuable.setYourBusinessReferenceId(message.getHeader().getYourBusinessReferenceId());

        if (message.getContent().getAssessmentDate() != null) {
            if (String.valueOf(message.getContent().getAssessmentDate()).length() > 10) {
                contribuable.setAssessmentDate(String.valueOf(message.getContent().getAssessmentDate())
                        .substring(0, 10));
            } else {
                contribuable.setAssessmentDate(String.valueOf(message.getContent().getAssessmentDate()));
            }
        } else {
            contribuable.setAssessmentDate("");
        }
        if (message.getContent().getAssessmentType() != null) {
            contribuable.setAssessmentType(String.valueOf(message.getContent().getAssessmentType()));
        } else {
            contribuable.setAssessmentType("");
        }
        if (message.getContent().getReportType() != null) {
            contribuable.setReportType(String.valueOf(message.getContent().getReportType()));
        } else {
            contribuable.setReportType("");
        }
        if (message.getContent().getTaxpayer().getVn() != null) {
            contribuable.setVn(String.valueOf(message.getContent().getTaxpayer().getVn()));
        } else {
            contribuable.setVn("");
        }
        if (message.getContent().getTaxpayer().getOtherPersonId().size() > 0) {
            if (message.getContent().getTaxpayer().getOtherPersonId().get(0) != null) {
                contribuable.setPersonIdCategory(String.valueOf(message.getContent().getTaxpayer().getOtherPersonId()
                        .get(0).getPersonIdCategory()));
                contribuable.setPersonIdCategory(String.valueOf(message.getContent().getTaxpayer().getOtherPersonId()
                        .get(0).getPersonId()));
            } else {
                contribuable.setPersonIdCategory("");
                contribuable.setPersonIdCategory("");
            }
        } else {
            contribuable.setPersonIdCategory("");
            contribuable.setPersonIdCategory("");
        }
        if (message.getContent().getTaxpayer().getOfficialName() != null) {
            contribuable.setOfficialName(message.getContent().getTaxpayer().getOfficialName());
        } else {
            contribuable.setOfficialName("");
        }
        if (message.getContent().getTaxpayer().getFirstName() != null) {
            contribuable.setFirstName(message.getContent().getTaxpayer().getFirstName());
        } else {
            contribuable.setFirstName("");
        }
        if (message.getContent().getTaxpayer().getSex() != null) {
            contribuable.setSex(message.getContent().getTaxpayer().getSex());
        } else {
            contribuable.setSex("");
        }
        // PO 9200 - date de naissance au lieu de la date de l'assiette
        if (message.getContent().getTaxpayer().getDateOfBirth().getYearMonthDay() != null) {
            if (String.valueOf(message.getContent().getTaxpayer().getDateOfBirth().getYearMonthDay()).length() > 10) {
                contribuable.setYearMonthDay(String.valueOf(
                        message.getContent().getTaxpayer().getDateOfBirth().getYearMonthDay()).substring(0, 10));
            } else {
                contribuable.setYearMonthDay(String.valueOf(message.getContent().getTaxpayer().getDateOfBirth()
                        .getYearMonthDay()));
            }
        } else {
            contribuable.setYearMonthDay("");
        }
        if (message.getContent().getTaxpayer().getAddress() != null) {
            if (message.getContent().getTaxpayer().getAddress().getStreet() != null) {
                contribuable.setStreet(message.getContent().getTaxpayer().getAddress().getStreet());
            } else {
                contribuable.setStreet("");
            }
            if (message.getContent().getTaxpayer().getAddress().getAddressLine1() != null) {
                contribuable.setAddressLine1(message.getContent().getTaxpayer().getAddress().getAddressLine1());
            } else {
                contribuable.setAddressLine1("");
            }
            if (message.getContent().getTaxpayer().getAddress().getAddressLine2() != null) {
                contribuable.setAddressLine2(message.getContent().getTaxpayer().getAddress().getAddressLine2());
            } else {
                contribuable.setAddressLine2("");
            }
            if (message.getContent().getTaxpayer().getAddress().getLocality() != null) {
                contribuable.setLocality(message.getContent().getTaxpayer().getAddress().getLocality());
            } else {
                contribuable.setLocality("");
            }
            if (message.getContent().getTaxpayer().getAddress().getHouseNumber() != null) {
                contribuable.setHouseNumber(message.getContent().getTaxpayer().getAddress().getHouseNumber());
            } else {
                contribuable.setHouseNumber("");
            }
            if (message.getContent().getTaxpayer().getAddress().getTown() != null) {
                contribuable.setTown(message.getContent().getTaxpayer().getAddress().getTown());
            } else {
                contribuable.setTown("");
            }
            if (message.getContent().getTaxpayer().getAddress().getSwissZipCode() != null) {
                contribuable.setSwissZipCode(String.valueOf(message.getContent().getTaxpayer().getAddress()
                        .getSwissZipCode()));
            } else {
                contribuable.setSwissZipCode(String.valueOf(""));
            }
            if (message.getContent().getTaxpayer().getAddress().getCountry() != null) {
                contribuable.setCountry(message.getContent().getTaxpayer().getAddress().getCountry());
            } else {
                contribuable.setCountry("");
            }
        }
        if (message.getContent().getTaxpayer().getMaritalStatus() != null) {
            contribuable.setMaritalStatus(message.getContent().getTaxpayer().getMaritalStatus());
        } else {
            contribuable.setMaritalStatus("");
        }
        if (message.getContent().getTaxpayer().getDateOfMaritalStatus() != null) {
            contribuable.setDateOfMaritalStatus(String.valueOf(message.getContent().getTaxpayer()
                    .getDateOfMaritalStatus()));
        } else {
            contribuable.setDateOfMaritalStatus("");

        }
        if (message.getContent().getTaxpayer().getDateOfEntry() != null) {
            contribuable.setDateOfEntry(String.valueOf(message.getContent().getTaxpayer().getDateOfEntry()));
        } else {
            contribuable.setDateOfEntry("");
        }
        if (message.getContent().getRemark() != null) {
            contribuable.setRemark(message.getContent().getRemark());
        } else {
            contribuable.setRemark("");
        }
        if (message.getHeader().getInitialMessageDate() != null) {
            contribuable.setInitialMessageDate(String.valueOf(message.getHeader().getInitialMessageDate()));
        } else {
            contribuable.setInitialMessageDate("");
        }
        contribuable.add(transaction);
        contribuable.setForBackup(true);
        contribuable.add(transaction);
    }

    private void controlePlausibilites(BTransaction transaction, CPCommunicationFiscaleRetourSEDEXViewBean comm)
            throws Exception {
        CPProcessValiderPlausibilite process = new CPProcessValiderPlausibilite();
        process.setSession(getSession());
        process.setTransaction(transaction);
        process.setCommunicationRetour(comm);
        process.setSendCompletionMail(false);
        process.setDeclenchement(CPReglePlausibilite.CS_AVANT_GENERATION);
        process.setWantMajBackup(true);
        process.executeProcess();
    }

    public String getDescription() {
        return "Réception des communications fiscales Sedex";
    }

    public String getName() {
        return "ReceptionSedex";
    }

    public String getPassSedex() {
        return passSedex;
    }

    public BSession getSession() {
        return session;
    }

    public String getUserSedex() {
        return userSedex;
    }

    @OnReceive
    public void onReceive(SedexMessage o) throws JadeSedexMessageNotHandledException {
        BSession session;
        SimpleSedexMessage message = null;
        Message message2011103 = null;
        try {
            if ((!JadeStringUtil.isEmpty(getUserSedex())) && (!JadeStringUtil.isEmpty(getPassSedex()))) {
                session = new BSession("PHENIX");
                session.connect(getUserSedex(), getPassSedex());
                setSession(session);
            } else {
                throw new Exception("Utilisateur Sedex non défini (JadeSedexService)");
            }

            message = (SimpleSedexMessage) o;
            Object myObject = JAXBServices.getInstance().unmarshal(message.getFileLocation(), false, false,
                    new Class<?>[0]);
            if (myObject instanceof Message) {
                message2011103 = (Message) myObject;
                run(message2011103);
            } else {
                JadeLogger.error(this,
                        "La version des messages reçus n'est pas compatible avec la version actuelle de Web@AVS");
                throw new JadeSedexMessageNotHandledException(
                        "La version des messages reçus n'est pas compatible avec la version actuelle de Web@AVS");
            }
        } catch (Exception e) {
            JadeLogger.error(this, "SEDEX: error receiving message "
                    + ((message == null) ? "" : message2011103.getHeader().getMessageId()));
            JadeLogger.error(this, e);
            throw new JadeSedexMessageNotHandledException("Erreur dans la réception de la communication fiscale : "
                    + " : " + ((message == null) ? "" : message2011103.getHeader().getMessageId()));
        }

    }

    public void run(Message message) throws Exception {
        BTransaction transaction = null;
        try {

            transaction = new BTransaction(getSession());
            transaction.openTransaction();
            CPCommunicationFiscaleRetourSEDEXViewBean comm = ajoutDeLaCommunication(transaction, message);
            String idRetour = comm.getIdRetour();
            ajoutDesInfosDuContribuable(transaction, message, idRetour);
            ajoutDesInfosDuConjoint(transaction, message, idRetour);
            ajoutDesDonneesDeBase(transaction, message, idRetour);
            ajoutDesDonneesCommerciales(transaction, message, idRetour);
            ajoutDesDonneesPrivees(transaction, message, idRetour);
            controlePlausibilites(transaction, comm);
            if (!transaction.hasErrors()) {
                transaction.commit();
            } else {
                throw new Exception("I won't commit the transaction since it has errors. " + transaction.getErrors());
            }
        } catch (Exception e) {
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception e2) {
                    JadeLogger.error(this, e2);
                }
            }
            throw e;
        } finally {
            try {
                if (transaction != null) {
                    transaction.closeTransaction();
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
    }

    public void setPassSedex(String passSedex) {
        this.passSedex = passSedex;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    @Setup
    public void setUp(Properties properties) throws JadeDecryptionNotSupportedException,
            JadeEncrypterNotFoundException, Exception {
        String encryptedUser = properties.getProperty("userSedex");
        if (encryptedUser == null) {
            JadeLogger.error(this, "Réception message 2011-103: user sedex non renseigné. ");
            throw new IllegalStateException("Réception message 2011-103: user sedex non renseigné. ");
        }
        String decryptedUser = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedUser);

        String encryptedPass = properties.getProperty("passSedex");
        if (encryptedPass == null) {
            JadeLogger.error(this, "Réception message 2011-103: mot de passe sedex non renseigné. ");
            throw new IllegalStateException("Réception message 2011-103: mot de passe sedex non renseigné. ");
        }
        String decryptedPass = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedPass);

        setUserSedex(decryptedUser);
        setPassSedex(decryptedPass);
    }

    public void setUserSedex(String userSedex) {
        this.userSedex = userSedex;
    }

}
