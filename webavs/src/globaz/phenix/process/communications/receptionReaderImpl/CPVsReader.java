/*
 * Créé le 20 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.process.communications.receptionReaderImpl;

import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.communications.CPCommentaireCommunication;
import globaz.phenix.process.communications.CPProcessAsciiReader;

/**
 * @author mmu Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPVsReader extends CPProcessAsciiReader {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected class CPVsReception extends CPReception {
        private String annee1 = "";
        private String anneeRef = "";
        private String capital = "";
        private String debutExercice1 = "";
        private String finExercice1 = "";
        private String fortune = "";
        private String revenu1 = "";
        private String revenu2 = "";
        private String vsAdresseAffilie1 = "";
        private String vsAdresseAffilie2 = "";
        private String vsAdresseAffilie3 = "";
        private String vsAdresseAffilie4 = "";
        private String vsAdresseConjoint1 = "";
        private String vsAdresseConjoint2 = "";
        private String vsAdresseConjoint3 = "";
        private String vsAdresseConjoint4 = "";
        private String vsAdresseCtb1 = "";
        private String vsAdresseCtb2 = "";
        private String vsAdresseCtb3 = "";
        private String vsAdresseCtb4 = "";
        private String vsAnneeTaxation = "";
        private String vsAutresRevenusConjoint = "";
        private String vsAutresRevenusCtb = "";
        private String vsCapitalPropreEngageEntrepriseConjoint = "";
        private String vsCapitalPropreEngageEntrepriseCtb = "";
        private String vsCodeTaxation1 = "";
        private String vsCodeTaxation2 = "";
        private String vsCotisationAvsAffilie = "";
        private String vsCotisationAvsConjoint = "";
        private String vsDateCommunication = "";
        private String vsDateDebutAffiliation = "";
        private String vsDateDebutAffiliationCaisseProfessionnelle = "";
        private String vsDateDebutAffiliationConjoint = "";
        private String vsDateDebutAffiliationConjointCaisseProfessionnelle = "";
        private String vsDateDecesCtb = "";
        private String vsDateDemandeCommunication = "";
        private String vsDateFinAffiliation = "";
        private String vsDateFinAffiliationCaisseProfessionnelle = "";
        private String vsDateFinAffiliationConjoint = "";
        private String vsDateFinAffiliationConjointCaisseProfessionnelle = "";
        private String vsDateNaissanceAffilie = "";
        private String vsDateNaissanceConjoint = "";
        private String vsDateNaissanceCtb = "";
        private String vsDateTaxation = "";
        private String vsDebutActiviteConjoint = "";
        private String vsDebutActiviteCtb = "";
        private String vsEtatCivilAffilie = "";
        private String vsEtatCivilCtb = "";
        private String vsFinActiviteConjoint = "";
        private String vsFinActiviteCtb = "";
        private String vsFortunePriveeConjoint = "";
        private String vsFortunePriveeCtb = "";
        private String vsLangue = "";
        private String vsRachatLpp = "";
        private String vsRachatLppCjt = "";
        private String vsLibre3 = "";
        private String vsLibre4 = "";
        private String vsNbJoursTaxation = "";
        private String vsNoCaisseAgenceAffilie = "";
        private String vsNoCaisseProfessionnelleAffilie = "";
        private String vsNomAffilie = "";
        private String vsNomConjoint = "";
        private String vsNomPrenomContribuableAnnee = "";
        private String vsNoPostalLocalite = "";
        private String vsNumAffilie = "";
        private String vsNumAffilieConjoint = "";
        private String vsNumAffilieInterneCaisseProfessionnelle = "";
        private String vsNumAffilieInterneConjointCaisseProfessionnelle = "";
        private String vsNumAvsAffilie = "";
        private String vsNumAvsConjoint = "";
        private String vsNumAvsCtb = "";
        private String vsNumCaisseAgenceConjoint = "";
        private String vsNumCaisseProfessionnelleConjoint = "";
        private String vsNumCtb = "";
        private String vsNumCtbSuivant = "";
        private String vsNumPostalLocaliteConjoint = "";
        private String vsNumPostalLocaliteCtb = "";
        private String vsReserve = "";
        private String vsReserveDateNaissanceConjoint = "";
        private String vsReserveFichierImpression = "";
        private String vsReserveTriNumCaisse = "";
        private String vsRevenuAgricoleConjoint = "";
        private String vsRevenuAgricoleCtb = "";
        private String vsRevenuNonAgricoleConjoint = "";
        private String vsRevenuNonAgricoleCtb = "";
        private String vsRevenuRenteConjoint = "";
        private String vsRevenuRenteCtb = "";
        private String vsSalairesConjoint = "";
        private String vsSalairesContribuable = "";
        private String vsSexeAffilie = "";
        private String vsSexeCtb = "";
        private String vsTypeTaxation = "";

        /**
         * @param reader
         * @param nbEntreesLues
         */
        public CPVsReception(CPProcessAsciiReader reader, int nbEntreesLues) {
            super(reader, nbEntreesLues);
        }

        @Override
        public String getAnnee1() {
            return annee1;
        }

        public String getAnneeRef() {
            return anneeRef;
        }

        @Override
        public String getCapital() {
            return capital;
        }

        @Override
        public String getDebutExercice1() {
            return debutExercice1;
        }

        @Override
        public String getFinExercice1() {
            return finExercice1;
        }

        @Override
        public String getFortune() {
            return fortune;
        }

        @Override
        public String getGenreAffilie() {
            // if(!JadeStringUtil.isIntegerEmpty(vsRevenuNonAgricoleCtb)
            // ||!JadeStringUtil.isIntegerEmpty(vsRevenuAgricoleCtb)
            // ||!JadeStringUtil.isIntegerEmpty(vsCapitalPropreEngageEntrepriseCtb))
            // return CPDecision.CS_INDEPENDANT;
            // else
            // return CPDecision.CS_NON_ACTIF;
            return "";
        }

        @Override
        public String getGenreTaxation() {
            if ("1".equalsIgnoreCase(vsCodeTaxation2) || "4".equalsIgnoreCase(vsCodeTaxation2)) {
                if ("R".equalsIgnoreCase(vsTypeTaxation)) {
                    return CPCommentaireCommunication.CS_GENRE_TPR;
                } else {
                    return CPCommentaireCommunication.CS_GENRE_TP;
                }
            } else if ("6".equalsIgnoreCase(vsCodeTaxation2)) {
                if ("R".equalsIgnoreCase(vsTypeTaxation)) {
                    return CPCommentaireCommunication.CS_GENRE_TOR;
                } else {
                    return CPCommentaireCommunication.CS_GENRE_TO;
                }
            } else {
                if ("R".equalsIgnoreCase(vsTypeTaxation)) {
                    return CPCommentaireCommunication.CS_GENRE_TDR;
                } else {
                    return CPCommentaireCommunication.CS_GENRE_TD;
                }
            }
        }

        @Override
        public String getRevenu1() {
            return revenu1;
        }

        @Override
        public String getRevenu2() {
            return revenu2;
        }

        @Override
        public String getVsAdresseAffilie1() {
            return vsAdresseAffilie1;
        }

        @Override
        public String getVsAdresseAffilie2() {
            return vsAdresseAffilie2;
        }

        @Override
        public String getVsAdresseAffilie3() {
            return vsAdresseAffilie3;
        }

        @Override
        public String getVsAdresseAffilie4() {
            return vsAdresseAffilie4;
        }

        @Override
        public String getVsAdresseConjoint1() {
            return vsAdresseConjoint1;
        }

        @Override
        public String getVsAdresseConjoint2() {
            return vsAdresseConjoint2;
        }

        @Override
        public String getVsAdresseConjoint3() {
            return vsAdresseConjoint3;
        }

        @Override
        public String getVsAdresseConjoint4() {
            return vsAdresseConjoint4;
        }

        @Override
        public String getVsAdresseCtb1() {
            return vsAdresseCtb1;
        }

        @Override
        public String getVsAdresseCtb2() {
            return vsAdresseCtb2;
        }

        @Override
        public String getVsAdresseCtb3() {
            return vsAdresseCtb3;
        }

        @Override
        public String getVsAdresseCtb4() {
            return vsAdresseCtb4;
        }

        @Override
        public String getVsAnneeTaxation() {
            return vsAnneeTaxation;
        }

        @Override
        public String getVsAutresRevenusConjoint() {
            return vsAutresRevenusConjoint;
        }

        @Override
        public String getVsAutresRevenusCtb() {
            return vsAutresRevenusCtb;
        }

        @Override
        public String getVsCapitalPropreEngageEntrepriseConjoint() {
            return vsCapitalPropreEngageEntrepriseConjoint;
        }

        @Override
        public String getVsCapitalPropreEngageEntrepriseCtb() {
            return vsCapitalPropreEngageEntrepriseCtb;
        }

        @Override
        public String getVsCodeTaxation1() {
            return vsCodeTaxation1;
        }

        @Override
        public String getVsCodeTaxation2() {
            return vsCodeTaxation2;
        }

        @Override
        public String getVsCotisationAvsAffilie() {
            return vsCotisationAvsAffilie;
        }

        @Override
        public String getVsCotisationAvsConjoint() {
            return vsCotisationAvsConjoint;
        }

        @Override
        public String getVsDateCommunication() {
            return vsDateCommunication;
        }

        @Override
        public String getVsDateDebutAffiliation() {
            return vsDateDebutAffiliation;
        }

        @Override
        public String getVsDateDebutAffiliationCaisseProfessionnelle() {
            return vsDateDebutAffiliationCaisseProfessionnelle;
        }

        @Override
        public String getVsDateDebutAffiliationConjoint() {
            return vsDateDebutAffiliationConjoint;
        }

        @Override
        public String getVsDateDebutAffiliationConjointCaisseProfessionnelle() {
            return vsDateDebutAffiliationConjointCaisseProfessionnelle;
        }

        @Override
        public String getVsDateDecesCtb() {
            return vsDateDecesCtb;
        }

        @Override
        public String getVsDateDemandeCommunication() {
            return vsDateDemandeCommunication;
        }

        @Override
        public String getVsDateFinAffiliation() {
            return vsDateFinAffiliation;
        }

        @Override
        public String getVsDateFinAffiliationCaisseProfessionnelle() {
            return vsDateFinAffiliationCaisseProfessionnelle;
        }

        @Override
        public String getVsDateFinAffiliationConjoint() {
            return vsDateFinAffiliationConjoint;
        }

        @Override
        public String getVsDateFinAffiliationConjointCaisseProfessionnelle() {
            return vsDateFinAffiliationConjointCaisseProfessionnelle;
        }

        @Override
        public String getVsDateNaissanceAffilie() {
            return vsDateNaissanceAffilie;
        }

        @Override
        public String getVsDateNaissanceConjoint() {
            return vsDateNaissanceConjoint;
        }

        @Override
        public String getVsDateNaissanceCtb() {
            return vsDateNaissanceCtb;
        }

        @Override
        public String getVsDateTaxation() {
            return vsDateTaxation;
        }

        @Override
        public String getVsDebutActiviteConjoint() {
            return vsDebutActiviteConjoint;
        }

        @Override
        public String getVsDebutActiviteCtb() {
            return vsDebutActiviteCtb;
        }

        @Override
        public String getVsEtatCivilAffilie() {
            return vsEtatCivilAffilie;
        }

        @Override
        public String getVsEtatCivilCtb() {
            return vsEtatCivilCtb;
        }

        @Override
        public String getVsFinActiviteConjoint() {
            return vsFinActiviteConjoint;
        }

        @Override
        public String getVsFinActiviteCtb() {
            return vsFinActiviteCtb;
        }

        @Override
        public String getVsFortunePriveeConjoint() {
            return vsFortunePriveeConjoint;
        }

        @Override
        public String getVsFortunePriveeCtb() {
            return vsFortunePriveeCtb;
        }

        @Override
        public String getVsLangue() {
            return vsLangue;
        }

        @Override
        public String getVsRachatLpp() {
            return vsRachatLpp;
        }

        @Override
        public String getVsRachatLppCjt() {
            return vsRachatLppCjt;
        }

        @Override
        public String getVsLibre3() {
            return vsLibre3;
        }

        @Override
        public String getVsLibre4() {
            return vsLibre4;
        }

        @Override
        public String getVsNbJoursTaxation() {
            return vsNbJoursTaxation;
        }

        @Override
        public String getVsNoCaisseAgenceAffilie() {
            return vsNoCaisseAgenceAffilie;
        }

        @Override
        public String getVsNoCaisseProfessionnelleAffilie() {
            return vsNoCaisseProfessionnelleAffilie;
        }

        @Override
        public String getVsNomAffilie() {
            return vsNomAffilie;
        }

        @Override
        public String getVsNomConjoint() {
            return vsNomConjoint;
        }

        @Override
        public String getVsNomPrenomContribuableAnnee() {
            return vsNomPrenomContribuableAnnee;
        }

        @Override
        public String getVsNoPostalLocalite() {
            return vsNoPostalLocalite;
        }

        @Override
        public String getVsNumAffilie() {
            return vsNumAffilie;
        }

        @Override
        public String getVsNumAffilieConjoint() {
            return vsNumAffilieConjoint;
        }

        @Override
        public String getVsNumAffilieInterneCaisseProfessionnelle() {
            return vsNumAffilieInterneCaisseProfessionnelle;
        }

        @Override
        public String getVsNumAffilieInterneConjointCaisseProfessionnelle() {
            return vsNumAffilieInterneConjointCaisseProfessionnelle;
        }

        @Override
        public String getVsNumAvsAffilie() {
            return vsNumAvsAffilie;
        }

        @Override
        public String getVsNumAvsConjoint() {
            return vsNumAvsConjoint;
        }

        @Override
        public String getVsNumAvsCtb() {
            return vsNumAvsCtb;
        }

        @Override
        public String getVsNumCaisseAgenceConjoint() {
            return vsNumCaisseAgenceConjoint;
        }

        @Override
        public String getVsNumCaisseProfessionnelleConjoint() {
            return vsNumCaisseProfessionnelleConjoint;
        }

        @Override
        public String getVsNumCtb() {
            return vsNumCtb;
        }

        @Override
        public String getVsNumCtbSuivant() {
            return vsNumCtbSuivant;
        }

        @Override
        public String getVsNumPostalLocaliteConjoint() {
            return vsNumPostalLocaliteConjoint;
        }

        @Override
        public String getVsNumPostalLocaliteCtb() {
            return vsNumPostalLocaliteCtb;
        }

        @Override
        public String getVsReserve() {
            return vsReserve;
        }

        @Override
        public String getVsReserveDateNaissanceConjoint() {
            return vsReserveDateNaissanceConjoint;
        }

        @Override
        public String getVsReserveFichierImpression() {
            return vsReserveFichierImpression;
        }

        @Override
        public String getVsReserveTriNumCaisse() {
            return vsReserveTriNumCaisse;
        }

        @Override
        public String getVsRevenuAgricoleConjoint() {
            return vsRevenuAgricoleConjoint;
        }

        @Override
        public String getVsRevenuAgricoleCtb() {
            return vsRevenuAgricoleCtb;
        }

        @Override
        public String getVsRevenuNonAgricoleConjoint() {
            return vsRevenuNonAgricoleConjoint;
        }

        @Override
        public String getVsRevenuNonAgricoleCtb() {
            return vsRevenuNonAgricoleCtb;
        }

        @Override
        public String getVsRevenuRenteConjoint() {
            return vsRevenuRenteConjoint;
        }

        @Override
        public String getVsRevenuRenteCtb() {
            return vsRevenuRenteCtb;
        }

        @Override
        public String getVsSalairesConjoint() {
            return vsSalairesConjoint;
        }

        @Override
        public String getVsSalairesContribuable() {
            return vsSalairesContribuable;
        }

        @Override
        public String getVsSexeAffilie() {
            return vsSexeAffilie;
        }

        @Override
        public String getVsSexeCtb() {
            return vsSexeCtb;
        }

        @Override
        public String getVsTypeTaxation() {
            return vsTypeTaxation;
        }

        @Override
        public int lireEntree() throws Exception {
            vsNumCtb = getField(11);
            if (vsNumCtb.equals(FIN_FICHIER)) {
                return CPProcessAsciiReader.FIN_FICHIER;
            }
            vsAnneeTaxation = getField(4);
            vsDateDemandeCommunication = readDate(getField(8));
            vsDateCommunication = readDate(getField(8));
            vsDateTaxation = readDate(getField(8));
            vsCodeTaxation1 = getField(1);
            vsCodeTaxation2 = getField(1);
            vsTypeTaxation = getField(2);
            vsNumAffilie = getField(12);
            vsNumAvsAffilie = getField(11);
            vsDateNaissanceAffilie = readDate(getField(8));
            vsDateDebutAffiliation = readDate(getField(8));
            vsDateFinAffiliation = readDate(getField(8));
            vsNomAffilie = getField(50);
            vsAdresseAffilie1 = getField(40);
            vsAdresseAffilie2 = getField(40);
            vsAdresseAffilie3 = getField(40);
            vsAdresseAffilie4 = getField(40);
            vsNoPostalLocalite = getField(40);
            vsNoCaisseAgenceAffilie = getField(6);
            vsNoCaisseProfessionnelleAffilie = getField(12);
            vsDateDebutAffiliationCaisseProfessionnelle = readDate(getField(8));
            vsDateFinAffiliationCaisseProfessionnelle = readDate(getField(8));
            vsNumAffilieInterneCaisseProfessionnelle = getField(20);
            vsCotisationAvsAffilie = getField(11);
            String isPositif = getField(1);
            if (isPositif.equals("-")) {
                vsCotisationAvsAffilie = "-" + vsCotisationAvsAffilie;
            }
            vsEtatCivilAffilie = getField(1);
            vsSexeAffilie = getField(1);
            vsNumAffilieConjoint = getField(12);
            vsNumAvsConjoint = getField(11);
            vsDateNaissanceConjoint = readDate(getField(8));
            vsDateDebutAffiliationConjoint = readDate(getField(8));
            vsDateFinAffiliationConjoint = readDate(getField(8));
            vsNomConjoint = getField(50);
            vsAdresseConjoint1 = getField(40);
            vsAdresseConjoint2 = getField(40);
            vsAdresseConjoint3 = getField(40);
            vsAdresseConjoint4 = getField(40);
            vsNumPostalLocaliteConjoint = getField(40);
            vsNumCaisseAgenceConjoint = getField(6);
            vsNumCaisseProfessionnelleConjoint = getField(12);
            vsDateDebutAffiliationConjointCaisseProfessionnelle = readDate(getField(8));
            vsDateFinAffiliationConjointCaisseProfessionnelle = readDate(getField(8));
            vsNumAffilieInterneConjointCaisseProfessionnelle = getField(20);
            vsCotisationAvsConjoint = getField(11);
            isPositif = getField(1);
            if (isPositif.equals("-")) {
                vsCotisationAvsConjoint = "-" + vsCotisationAvsConjoint;
            }
            vsNomPrenomContribuableAnnee = getField(40);
            vsAdresseCtb1 = getField(40);
            vsAdresseCtb2 = getField(40);
            vsAdresseCtb3 = getField(40);
            vsAdresseCtb4 = getField(40);
            vsNumPostalLocaliteCtb = getField(40);
            vsEtatCivilCtb = getField(1);
            vsSexeCtb = getField(1);
            vsLangue = getField(1);
            vsNumAvsCtb = getField(11);
            vsDateNaissanceCtb = readDate(getField(8));
            vsDebutActiviteCtb = readDate(getField(8));
            vsFinActiviteCtb = readDate(getField(8));
            vsDebutActiviteConjoint = readDate(getField(8));
            vsFinActiviteConjoint = readDate(getField(8));
            vsRevenuNonAgricoleCtb = getField(11);
            isPositif = getField(1);
            if (isPositif.equals("-")) {
                vsRevenuNonAgricoleCtb = "-" + vsRevenuNonAgricoleCtb;
            }
            vsRevenuNonAgricoleConjoint = getField(11);
            isPositif = getField(1);
            if (isPositif.equals("-")) {
                vsRevenuNonAgricoleConjoint = "-" + vsRevenuNonAgricoleConjoint;
            }
            vsRevenuAgricoleCtb = getField(11);
            isPositif = getField(1);
            if (isPositif.equals("-")) {
                vsRevenuAgricoleCtb = "-" + vsRevenuAgricoleCtb;
            }
            vsRevenuAgricoleConjoint = getField(11);
            isPositif = getField(1);
            if (isPositif.equals("-")) {
                vsRevenuAgricoleConjoint = "-" + vsRevenuAgricoleConjoint;
            }
            vsCapitalPropreEngageEntrepriseCtb = getField(11);
            isPositif = getField(1);
            if (isPositif.equals("-")) {
                vsCapitalPropreEngageEntrepriseCtb = "-" + vsCapitalPropreEngageEntrepriseCtb;
            }
            vsCapitalPropreEngageEntrepriseConjoint = getField(11);
            isPositif = getField(1);
            if (isPositif.equals("-")) {
                vsCapitalPropreEngageEntrepriseConjoint = "-" + vsCapitalPropreEngageEntrepriseConjoint;
            }
            vsRevenuRenteCtb = getField(11);
            isPositif = getField(1);
            if (isPositif.equals("-")) {
                vsRevenuRenteCtb = "-" + vsRevenuRenteCtb;
            }
            vsRevenuRenteConjoint = getField(11);
            isPositif = getField(1);
            if (isPositif.equals("-")) {
                vsRevenuRenteConjoint = "-" + vsRevenuRenteConjoint;
            }
            vsFortunePriveeCtb = getField(11);
            isPositif = getField(1);
            if (isPositif.equals("-")) {
                vsFortunePriveeCtb = "-" + vsFortunePriveeCtb;
            }
            vsFortunePriveeConjoint = getField(11);
            isPositif = getField(1);
            if (isPositif.equals("-")) {
                vsFortunePriveeConjoint = "-" + vsFortunePriveeConjoint;
            }
            vsSalairesContribuable = getField(11);
            isPositif = getField(1);
            if (isPositif.equals("-")) {
                vsSalairesContribuable = "-" + vsSalairesContribuable;
            }
            vsSalairesConjoint = getField(11);
            isPositif = getField(1);
            if (isPositif.equals("-")) {
                vsSalairesConjoint = "-" + vsSalairesConjoint;
            }
            vsAutresRevenusCtb = getField(11);
            isPositif = getField(1);
            if (isPositif.equals("-")) {
                vsAutresRevenusCtb = "-" + vsAutresRevenusCtb;
            }
            vsAutresRevenusConjoint = getField(11);
            isPositif = getField(1);
            if (isPositif.equals("-")) {
                vsAutresRevenusConjoint = "-" + vsAutresRevenusConjoint;
            }
            vsRachatLpp = getField(11);
            isPositif = getField(1);
            if (isPositif.equals("-")) {
                vsRachatLpp = "-" + vsRachatLpp;
            }
            vsRachatLppCjt = getField(11);
            isPositif = getField(1);
            if (isPositif.equals("-")) {
                vsRachatLppCjt = "-" + vsRachatLppCjt;
            }
            vsLibre3 = getField(11);
            isPositif = getField(1);
            if (isPositif.equals("-")) {
                vsLibre3 = "-" + vsLibre3;
            }
            vsLibre4 = getField(11);
            isPositif = getField(1);
            if (isPositif.equals("-")) {
                vsLibre4 = "-" + vsLibre4;
            }
            vsReserve = getField(82);
            vsNbJoursTaxation = getField(3);
            vsNumCtbSuivant = getField(11);
            if (JadeStringUtil.isEmpty(vsNumCtbSuivant) || !JadeStringUtil.isDigit(vsNumCtbSuivant)) {
                vsNumCtbSuivant = "";
            }
            vsDateDecesCtb = readDate(getField(8));
            getField(3);
            vsReserveDateNaissanceConjoint = readDate(getField(8));
            vsReserveFichierImpression = getField(1);
            vsReserveTriNumCaisse = getField(6);
            anneeRef = vsAnneeTaxation;
            annee1 = vsAnneeTaxation;
            endLine();
            if (valide) {
                return SUCCES;
            } else {
                return EN_ERREUR;
            }
        }

        private String readDate(String field) {
            if (JadeStringUtil.isEmpty(field) || !JadeStringUtil.isDigit(field)) {
                return "";
            } else {
                String annee = field.substring(0, 4);
                String mois = field.substring(4, 6);
                String jour = field.substring(6, 8);
                return jour + "." + mois + "." + annee;
            }

        }
    }

    private static final String FIN_FICHIER = "99999999999";

    public static boolean ASCIIToEntity() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.phenix.process.CPProcessAsciiReader#getReception(int)
     */
    @Override
    protected CPReception getReception(CPProcessAsciiReader reader, int nbEntreesLues) {
        return new CPVsReception(reader, nbEntreesLues);
    }
}
