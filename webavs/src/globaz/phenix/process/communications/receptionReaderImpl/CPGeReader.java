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
import java.io.BufferedReader;

/**
 * @author mmu
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPGeReader extends CPProcessAsciiReader {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected class CGeReception extends CPReception {

        private String capitalFortune;
        private String debutExercice1;
        private String finExercice1;
        private String geAnnee;
        private String geBourses;
        private String geDateTransfertMAD;
        private String geDivers;
        private String geExplicationsDivers;
        private String geGenreAffilie;
        private String geImpositionSelonDepense;
        private String geImpotSource;
        private String geIndemniteJournaliere;
        private String geNNSS;
        private String geNom;
        private String geNomAFC;
        private String geNomConjoint;
        private String geNonAssujettiIBO;
        private String geNonAssujettiIFD;
        private String genreAffilie;
        private String genreTaxation;
        private String geNumAffilie;
        private String geNumAvs;
        private String geNumAvsConjoint;
        private String geNumCaisse;
        private String geNumCommunication = "";
        private String geNumContribuable = "";
        private String geNumDemande = "";
        private String geObservations = "";
        private String gePasActiviteDeclaree;
        private String gePension;
        private String gePensionAlimentaire;
        private String gePersonneNonIdentifiee;
        private String gePrenom;
        private String gePrenomAFC;
        private String gePrenomConjoint;
        private String geRenteInvalidite;
        private String geRenteViagere;

        private String geRenteVieillesse;
        private String geRetraite;
        private String geTaxationOffice;
        private String periodeIFD;
        private String revenu1;
        private String revenu2;

        /**
         * @param nbEntreesLues
         * @throws Exception
         */
        public CGeReception(CPProcessAsciiReader reader, int nbEntreesLues) {
            super(reader, nbEntreesLues);
        }

        @Override
        public String getAnnee1() {
            return geAnnee;
        }

        @Override
        public String getCapital() {
            if (geGenreAffilie.equals("1")) {
                return capitalFortune;
            } else {
                return "";
            }
        }

        @Override
        public String getDebutExercice1() {
            try {
                if (!"".equalsIgnoreCase(debutExercice1)) {
                    // String annee = debutExercice1.substring(6,4);
                    // String mois = debutExercice1.substring(3,2);
                    // String jour = debutExercice1.substring(0,2);
                    // return jour+'.'+mois+'.'+annee;
                }
            } catch (Exception e) {
                debutExercice1 = "";
            }
            return debutExercice1;
        }

        @Override
        public String getFinExercice1() {
            try {
                if (!"".equalsIgnoreCase(finExercice1)) {
                    // String annee = finExercice1.substring(0,4);
                    // String mois = finExercice1.substring(2,4);
                    // String jour = finExercice1.substring(6);
                    // return jour+'.'+mois+'.'+annee;
                }
            } catch (Exception e) {
                finExercice1 = "";
            }
            return finExercice1;
        }

        @Override
        public String getFortune() {
            if (geGenreAffilie.equals("0")) {
                return capitalFortune;
            } else {
                return "";
            }
        }

        /**
         * @return
         */
        public String getGeAnnee() {
            return geAnnee;
        }

        /**
         * @return
         */
        @Override
        public String getGeBourses() {
            return geBourses;
        }

        /**
         * @return
         */
        @Override
        public String getGeDateTransfertMAD() {
            try {
                if (!"".equalsIgnoreCase(geDateTransfertMAD)) {
                    // String annee = geDateTransfertMAD.substring(0,4);
                    // String mois = geDateTransfertMAD.substring(2,4);
                    // String jour = geDateTransfertMAD.substring(6);
                    // return jour+'.'+mois+'.'+annee;
                }
            } catch (Exception e) {
                geDateTransfertMAD = "";
            }
            return geDateTransfertMAD;
        }

        /**
         * @return
         */
        @Override
        public String getGeDivers() {
            return geDivers;
        }

        /**
         * @return
         */
        @Override
        public String getGeExplicationsDivers() {
            return geExplicationsDivers;
        }

        /**
         * @return
         */
        @Override
        public String getGeGenreAffilie() {
            return geGenreAffilie;
        }

        /**
         * @return
         */
        @Override
        public String getGeImpositionSelonDepense() {
            return geImpositionSelonDepense;
        }

        /**
         * @return
         */
        @Override
        public String getGeImpotSource() {
            return geImpotSource;
        }

        /**
         * @return
         */
        @Override
        public String getGeIndemniteJournaliere() {
            return geIndemniteJournaliere;
        }

        @Override
        public String getGeNNSS() {
            return geNNSS;
        }

        /**
         * @return
         */
        @Override
        public String getGeNom() {
            return geNom;
        }

        @Override
        public String getGeNomAFC() {
            return geNomAFC;
        }

        /**
         * @return
         */
        @Override
        public String getGeNomConjoint() {
            return geNomConjoint;
        }

        /**
         * @return
         */
        @Override
        public String getGeNonAssujettiIBO() {
            return geNonAssujettiIBO;
        }

        /**
         * @return
         */
        @Override
        public String getGeNonAssujettiIFD() {
            return geNonAssujettiIFD;
        }

        @Override
        public String getGenreAffilie() {
            if (geGenreAffilie.equals("1")) {
                genreAffilie = "602001";
            } else if (geGenreAffilie.equals("0")) {
                genreAffilie = "602002";
            } else {
                genreAffilie = "";
            }
            return genreAffilie;
        }

        @Override
        public String getGenreTaxation() {
            genreTaxation = "";
            // Si numCommunication > 1 => décision rectificative
            if (JadeStringUtil.toInt(getGeNumCommunication()) > 1) {
                if ((geTaxationOffice).equals("1")) {
                    // taxation d'office rectificative
                    genreTaxation = CPCommentaireCommunication.CS_GENRE_TOR;
                } else {
                    // Taxation rectificative
                    genreTaxation = CPCommentaireCommunication.CS_GENRE_TDR;
                }
            } else {
                // Taxation d'office
                if ((geTaxationOffice).equals("1")) {
                    genreTaxation = CPCommentaireCommunication.CS_GENRE_TO;
                } else {
                    // Taxation définitive
                    genreTaxation = CPCommentaireCommunication.CS_GENRE_TD;
                }
            }
            return genreTaxation;
        }

        /**
         * @return
         */
        @Override
        public String getGeNumAffilie() {
            return geNumAffilie;
        }

        /**
         * @return
         */
        @Override
        public String getGeNumAvs() {
            return geNumAvs;
        }

        /**
         * @return
         */
        @Override
        public String getGeNumAvsConjoint() {
            return geNumAvsConjoint;
        }

        /**
         * @return
         */
        @Override
        public String getGeNumCaisse() {
            return geNumCaisse;
        }

        /**
         * @return
         */
        @Override
        public String getGeNumCommunication() {
            return geNumCommunication;
        }

        /**
         * @return
         */
        @Override
        public String getGeNumContribuable() {
            return geNumContribuable;
        }

        /**
         * @return
         */
        @Override
        public String getGeNumDemande() {
            return geNumDemande;
        }

        /**
         * @return
         */
        @Override
        public String getGeObservations() {
            return geObservations;
        }

        /**
         * @return
         */
        @Override
        public String getGePasActiviteDeclaree() {
            return gePasActiviteDeclaree;
        }

        /**
         * @return
         */
        @Override
        public String getGePension() {
            return gePension;
        }

        /**
         * @return
         */
        @Override
        public String getGePensionAlimentaire() {
            return gePensionAlimentaire;
        }

        @Override
        public String getGePersonneNonIdentifiee() {
            return gePersonneNonIdentifiee;
        }

        /**
         * @return
         */
        @Override
        public String getGePrenom() {
            return gePrenom;
        }

        @Override
        public String getGePrenomAFC() {
            return gePrenomAFC;
        }

        /**
         * @return
         */
        @Override
        public String getGePrenomConjoint() {
            return gePrenomConjoint;
        }

        /**
         * @return
         */
        @Override
        public String getGeRenteInvalidite() {
            return geRenteInvalidite;
        }

        /**
         * @return
         */
        @Override
        public String getGeRenteViagere() {
            return geRenteViagere;
        }

        /**
         * @return
         */
        @Override
        public String getGeRenteVieillesse() {
            return geRenteVieillesse;
        }

        /**
         * @return
         */
        @Override
        public String getGeRetraite() {
            return geRetraite;
        }

        /**
         * @return
         */
        @Override
        public String getGeTaxationOffice() {
            return geTaxationOffice;
        }

        @Override
        public String getPeriodeIFD() {
            return periodeIFD;
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
        public int lireEntree() throws Exception {
            char separator = ';';
            geNumCaisse = getField(10, separator);
            geNumDemande = getField(10, separator);
            geAnnee = getField(4, separator);
            geGenreAffilie = getField(1, separator);
            geNumAffilie = getField(20, separator);
            geNumAvs = getField(11, separator);
            geNom = getField(60, separator);
            gePrenom = getField(50, separator);
            geNumCommunication = getField(1, separator);
            gePersonneNonIdentifiee = getField(1, separator);
            geNumContribuable = getField(8, separator);
            geNomAFC = getField(60, separator);
            gePrenomAFC = getField(50, separator);
            geNumAvsConjoint = getField(11, separator);
            geNomConjoint = getField(60, separator);
            gePrenomConjoint = getField(50, separator);
            if ("1".equalsIgnoreCase(geGenreAffilie)) {
                // Independant
                gePasActiviteDeclaree = getField(1, separator);
            } else {
                // Non actif
                geImpositionSelonDepense = getField(1, separator);
            }
            debutExercice1 = getField(10, separator);
            finExercice1 = getField(10, separator);
            capitalFortune = getField(10, separator);
            revenu1 = getField(10, separator);
            if ("0".equalsIgnoreCase(geGenreAffilie)) {
                // Non actif
                gePension = getField(1, separator);
                ;
                geRetraite = getField(1, separator);
                geRenteVieillesse = getField(1, separator);
                geRenteInvalidite = getField(1, separator);
                gePensionAlimentaire = getField(1, separator);
                geRenteViagere = getField(1, separator);
                geIndemniteJournaliere = getField(1, separator);
                geBourses = getField(1, separator);
                geDivers = getField(1, separator);
                geExplicationsDivers = getField(50, separator);
            }
            geNonAssujettiIBO = getField(1, separator);
            geImpotSource = getField(1, separator);
            geTaxationOffice = getField(1, separator);
            geNonAssujettiIFD = getField(1, separator);
            geObservations = getField(250, separator);
            geDateTransfertMAD = getField(10, separator);
            BufferedReader tmpReader = fileInputReader;
            int ci = tmpReader.read();
            char c = (char) ci;
            if (c != '\n') {
                String nss = getField(13, separator);
                if (JadeStringUtil.isEmpty(nss)) {
                    geNNSS = "0";
                } else {
                    geNNSS = nss;
                }
                endLine();
            } else {
                geNNSS = "0";
            }
            if (valide) {
                return SUCCES;
            } else {
                return EN_ERREUR;
            }
        }

        /**
         * @param string
         */
        public void setPeriodeIFD(String string) {
            periodeIFD = string;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.phenix.process.CPProcessAsciiReader#getReception(int)
     */
    @Override
    protected CPReception getReception(CPProcessAsciiReader reader, int nbEntreesLues) {
        return new CGeReception(reader, nbEntreesLues);
    }

}
