package globaz.cygnus.services;

import globaz.cygnus.api.IRFTypesBeneficiairePc;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoinJointAssPeriodeJointPotAssure;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoinJointAssPeriodeJointPotAssureManager;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;

/**
 * @author JJE
 * 
 */
public class RFRetrieveLimiteAnnuelleSousTypeDeSoinService {

    /**
     * Méthode qui retourne la limite annuelle d'un sous type de soin en fonction du type bénéficiaire, d'un sous type
     * de soin et d'une date String[Limite annuelle, id pot]
     * 
     * @return String[2] contenant la limite annuelle et l'id pot assuré
     * 
     * @throws Exception
     */
    public String[] getLimiteAnnuelleTypeDeSoinIdTiers(BISession session, String codeTypeDeSoin,
            String codeSousTypeDeSoin, String idTiers, String date, BTransaction transaction,
            String csTypeBeneficiairePC, String csGenrePCAccordee) throws Exception {

        String[] resultat = new String[2];
        if (!JadeStringUtil.isBlankOrZero(csTypeBeneficiairePC)) {
            RFSousTypeDeSoinJointAssPeriodeJointPotAssureManager rfTypeDeSoinJointSousTypeDeSoinMgr = new RFSousTypeDeSoinJointAssPeriodeJointPotAssureManager();

            rfTypeDeSoinJointSousTypeDeSoinMgr.setSession((BSession) session);
            rfTypeDeSoinJointSousTypeDeSoinMgr.setForCodeTypeDeSoin(codeTypeDeSoin);
            rfTypeDeSoinJointSousTypeDeSoinMgr.setForCodeSousTypeDeSoin(codeSousTypeDeSoin);
            rfTypeDeSoinJointSousTypeDeSoinMgr.setForDateBetweenPeriode(date);
            rfTypeDeSoinJointSousTypeDeSoinMgr.changeManagerSize(0);
            rfTypeDeSoinJointSousTypeDeSoinMgr.find(transaction);

            if (rfTypeDeSoinJointSousTypeDeSoinMgr.size() == 1) {
                RFSousTypeDeSoinJointAssPeriodeJointPotAssure rfTypeDeSoinJointSousTypeDeSoin = (RFSousTypeDeSoinJointAssPeriodeJointPotAssure) rfTypeDeSoinJointSousTypeDeSoinMgr
                        .getFirstEntity();

                if (null != rfTypeDeSoinJointSousTypeDeSoin) {

                    resultat[1] = rfTypeDeSoinJointSousTypeDeSoin.getIdPotAssure();

                    if ((JadeStringUtil.isBlankOrZero(rfTypeDeSoinJointSousTypeDeSoin.getMontantPlafondPourTous()) && csGenrePCAccordee
                            .equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE))
                            || (JadeStringUtil.isBlankOrZero(rfTypeDeSoinJointSousTypeDeSoin
                                    .getMontantPlafondPensionnairePourTous()) && csGenrePCAccordee
                                    .equals(IPCPCAccordee.CS_GENRE_PC_HOME))) {

                        if (csTypeBeneficiairePC.equals(IRFTypesBeneficiairePc.PERSONNES_SEULES_VEUVES)) {

                            if (csGenrePCAccordee.equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
                                resultat[0] = rfTypeDeSoinJointSousTypeDeSoin.getMontantPlafondPersonneSeule();
                            } else {
                                resultat[0] = rfTypeDeSoinJointSousTypeDeSoin
                                        .getMontantPlafondPensionnairePersonneSeule();
                            }

                        } else if (csTypeBeneficiairePC.equals(IRFTypesBeneficiairePc.COUPLE_A_DOMICILE)) {

                            if (csGenrePCAccordee.equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
                                resultat[0] = rfTypeDeSoinJointSousTypeDeSoin.getMontantPlafondCoupleDomicile();
                            }

                        } else if (csTypeBeneficiairePC.equals(IRFTypesBeneficiairePc.COUPLE_AVEC_ENFANTS)) {

                            if (csGenrePCAccordee.equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
                                resultat[0] = rfTypeDeSoinJointSousTypeDeSoin.getMontantPlafondCoupleEnfant();
                            }

                        } else if (csTypeBeneficiairePC.equals(IRFTypesBeneficiairePc.ADULTE_AVEC_ENFANTS)) {

                            if (csGenrePCAccordee.equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
                                resultat[0] = rfTypeDeSoinJointSousTypeDeSoin.getMontantPlafondAdulteEnfants();
                            }

                        } else if (csTypeBeneficiairePC
                                .equals(IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_DOMICILE)) {

                            if (csGenrePCAccordee.equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
                                resultat[0] = rfTypeDeSoinJointSousTypeDeSoin.getMontantPlafondSepareDomicile();
                            } else {
                                resultat[0] = rfTypeDeSoinJointSousTypeDeSoin
                                        .getMontantPlafondPensionnaireSepareDomicile();
                            }

                        } else if (csTypeBeneficiairePC.equals(IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_HOME)) {

                            if (csGenrePCAccordee.equals(IPCPCAccordee.CS_GENRE_PC_HOME)) {
                                resultat[0] = rfTypeDeSoinJointSousTypeDeSoin.getMontantPlafondPensionnaireSepareHome();
                            }

                        } else if (csTypeBeneficiairePC.equals(IRFTypesBeneficiairePc.ENFANTS_VIVANT_SEPARES)) {

                            if (csGenrePCAccordee.equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
                                resultat[0] = rfTypeDeSoinJointSousTypeDeSoin.getMontantPlafondEnfantsSepares();
                            } else {
                                resultat[0] = rfTypeDeSoinJointSousTypeDeSoin
                                        .getMontantPlafondPensionnaireEnfantsSepares();
                            }

                        } else if (csTypeBeneficiairePC.equals(IRFTypesBeneficiairePc.ENFANTS_AVEC_ENFANTS)) {

                            if (csGenrePCAccordee.equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
                                resultat[0] = rfTypeDeSoinJointSousTypeDeSoin.getMontantPlafondEnfantsEnfants();
                            } else {
                                resultat[0] = rfTypeDeSoinJointSousTypeDeSoin
                                        .getMontantPlafondPensionnaireEnfantsEnfants();
                            }
                        }

                    } else {
                        if (csGenrePCAccordee.equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
                            resultat[0] = rfTypeDeSoinJointSousTypeDeSoin.getMontantPlafondPourTous();
                        } else {
                            resultat[0] = rfTypeDeSoinJointSousTypeDeSoin.getMontantPlafondPensionnairePourTous();
                        }
                    }

                }
            }
        }

        return resultat;
    }

}
