package globaz.phenix.process.communications.receptionReaderImpl;

import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.process.communications.CPProcessAsciiReader;

public class CPNeReader extends CPProcessAsciiReader {

    private static final long serialVersionUID = -5465622942185356577L;

    protected class CPNeReception extends CPReception {

        private String annee1;
        private String annee2;
        private String anneeRef;
        private String capital;
        private String debutExercice1;
        private String finExercice1;
        private String fortune;
        private String genreAffilie;
        private String genreTaxation;
        private String neDateDebutAssuj;
        private String neDateValeur;
        private String neDossierTaxe;
        private String neDossierTrouve;
        private String neFiller;
        private String neFortuneAnnee1;
        private String neGenreAffilie;
        private String neGenreTaxation;
        private String neIndemniteJour;
        private String neIndemniteJour1;
        private String neNumAvs;
        private String neNumBDP;
        private String neNumCaisse;
        private String neNumClient;
        private String neNumCommune;
        private String neNumContribuable;
        private String nePension;
        private String nePensionAlimentaire;
        private String nePensionAlimentaire1;
        private String nePensionAnnee1;
        private String nePremiereLettreNom;
        private String neRenteTotale;
        private String neRenteTotale1;
        private String neRenteViagere;
        private String neRenteViagere1;
        private String neTaxationRectificative;
        private String periodeIFD;
        private String revenu1;
        private String revenu2;

        public CPNeReception(CPProcessAsciiReader reader, int nbEntreesLues) {
            super(reader, nbEntreesLues);
        }

        @Override
        public String getAnnee1() {
            return annee1;
        }

        @Override
        public String getAnnee2() {
            return annee2;
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
            try {
                if (!"".equalsIgnoreCase(debutExercice1)) {
                    int idx = debutExercice1.indexOf('/');
                    int day = Integer.parseInt(debutExercice1.substring(0, idx));
                    debutExercice1 = debutExercice1.substring(idx + 1);
                    idx = debutExercice1.indexOf('/');
                    int month = Integer.parseInt(debutExercice1.substring(0, idx));
                    debutExercice1 = debutExercice1.substring(idx + 1);
                    debutExercice1 = JadeStringUtil.removeChar(debutExercice1, ' ');
                    int year = Integer.parseInt(debutExercice1);
                    String dayString = "";
                    String monthString = "";
                    if (day < 10) {
                        dayString = "0" + Integer.toString(day);
                    } else {
                        dayString = Integer.toString(day);
                    }
                    if (month < 10) {
                        monthString = "0" + Integer.toString(month);
                    } else {
                        monthString = Integer.toString(month);
                    }

                    debutExercice1 = dayString + "." + monthString + "." + year;

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
                    int idx = finExercice1.indexOf('/');
                    int day = Integer.parseInt(finExercice1.substring(0, idx));
                    finExercice1 = finExercice1.substring(idx + 1);
                    idx = finExercice1.indexOf('/');
                    int month = Integer.parseInt(finExercice1.substring(0, idx));
                    idx = finExercice1.indexOf('/');
                    finExercice1 = finExercice1.substring(idx + 1, idx + 5);

                    String dayString = "";
                    String monthString = "";
                    if (day < 10) {
                        dayString = "0" + Integer.toString(day);
                    } else {
                        dayString = Integer.toString(day);
                    }
                    if (month < 10) {
                        monthString = "0" + Integer.toString(month);
                    } else {
                        monthString = Integer.toString(month);
                    }
                    finExercice1 = dayString + "." + monthString + "." + finExercice1;
                }
            } catch (Exception e) {
                finExercice1 = "";
            }
            return finExercice1;
        }

        @Override
        public String getFortune() {
            return fortune;
        }

        @Override
        public String getGenreAffilie() {
            if (neGenreAffilie.equals("2")) {
                genreAffilie = "602001";
            } else if (neGenreAffilie.equals("3")) {
                genreAffilie = "602002";
            } else {
                genreAffilie = "";
            }
            return genreAffilie;
        }

        @Override
        public String getGenreTaxation() {
            genreTaxation = "";
            if (neGenreTaxation.equals("TD ") || neGenreTaxation.equals("TD")) {
                genreTaxation = "607051";
            } else if (neGenreTaxation.equals("TDR")) {
                genreTaxation = "607052";
            } else if (neGenreTaxation.equals("TO ") || neGenreTaxation.equals("TO")) {
                genreTaxation = "607053";
            } else if (neGenreTaxation.equals("TOR")) {
                genreTaxation = "607054";
            } else if (neGenreTaxation.equals("TP ") || neGenreTaxation.equals("TP")) {
                genreTaxation = "607055";
            } else if (neGenreTaxation.equals("TPR")) {
                genreTaxation = "607056";
            } else if (neGenreTaxation.equals("TRD")) {
                genreTaxation = "607057";
            } else if (neGenreTaxation.equals("TDD")) {
                genreTaxation = "607065";
            } else if (neGenreTaxation.equals("TPD")) {
                genreTaxation = "607066";
            } else if (neGenreTaxation.equals("TRP")) {
                genreTaxation = "607067";
            }
            return genreTaxation;
        }

        @Override
        public String getNeDateDebutAssuj() {
            return neDateDebutAssuj;
        }

        @Override
        public String getNeDateValeur() {
            try {
                if (!"".equalsIgnoreCase(neDateValeur)) {
                    int idx = neDateValeur.indexOf('/');
                    int day = Integer.parseInt(neDateValeur.substring(0, idx));
                    neDateValeur = neDateValeur.substring(idx + 1);
                    idx = neDateValeur.indexOf('/');
                    int month = Integer.parseInt(neDateValeur.substring(0, idx));
                    neDateValeur = neDateValeur.substring(idx + 1);
                    neDateValeur = JadeStringUtil.removeChar(neDateValeur, ' ');
                    int year = Integer.parseInt(neDateValeur);
                    String dayString = "";
                    String monthString = "";
                    if (day < 10) {
                        dayString = "0" + Integer.toString(day);
                    } else {
                        dayString = Integer.toString(day);
                    }
                    if (month < 10) {
                        monthString = "0" + Integer.toString(month);
                    } else {
                        monthString = Integer.toString(month);
                    }
                    neDateValeur = dayString + "." + monthString + "." + year;
                }
            } catch (Exception e) {
                neDateValeur = "";
            }
            return neDateValeur;
        }

        @Override
        public String getNeDossierTaxe() {
            if (neDossierTaxe.equals("N")) {
                neDossierTaxe = "607060";
            }
            if (neDossierTaxe.equals("O")) {
                neDossierTaxe = "607061";
            }
            if (neDossierTaxe.equals("S")) {
                neDossierTaxe = "607063";
            }
            if (neDossierTaxe.equals("I")) {
                neDossierTaxe = "607064";
            }
            return neDossierTaxe;
        }

        @Override
        public String getNeDossierTrouve() {
            return neDossierTrouve;
        }

        @Override
        public String getNeFiller() {
            return neFiller;
        }

        @Override
        public String getNeFortuneAnnee1() {
            return neFortuneAnnee1;
        }

        @Override
        public String getNeGenreAffilie() {
            return neGenreAffilie;
        }

        @Override
        public String getNeGenreTaxation() {
            return neGenreTaxation;
        }

        @Override
        public String getNeIndemniteJour() {
            return neIndemniteJour;
        }

        @Override
        public String getNeIndemniteJour1() {
            return neIndemniteJour1;
        }

        @Override
        public String getNeNumAvs() {
            return neNumAvs;
        }

        @Override
        public String getNeNumBDP() {
            return neNumBDP;
        }

        @Override
        public String getNeNumCaisse() {
            return neNumCaisse;
        }

        @Override
        public String getNeNumClient() {
            return neNumClient;
        }

        @Override
        public String getNeNumCommune() {
            return neNumCommune;
        }

        @Override
        public String getNeNumContribuable() {
            return neNumContribuable;
        }

        @Override
        public String getNePension() {
            return nePension;
        }

        @Override
        public String getNePensionAlimentaire() {
            return nePensionAlimentaire;
        }

        @Override
        public String getNePensionAlimentaire1() {
            return nePensionAlimentaire1;
        }

        @Override
        public String getNePensionAnnee1() {
            return nePensionAnnee1;
        }

        @Override
        public String getNePremiereLettreNom() {
            return nePremiereLettreNom;
        }

        @Override
        public String getNeRenteTotale() {
            return neRenteTotale;
        }

        @Override
        public String getNeRenteTotale1() {
            return neRenteTotale1;
        }

        @Override
        public String getNeRenteViagere() {
            return neRenteViagere;
        }

        @Override
        public String getNeRenteViagere1() {
            return neRenteViagere1;
        }

        @Override
        public String getNeTaxationRectificative() {
            return neTaxationRectificative;
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
            neFiller = getField(4);
            neNumAvs = getField(11);
            neNumCaisse = getField(6);
            neGenreAffilie = getField(1);
            nePremiereLettreNom = getField(1);
            neNumContribuable = getField(5);
            neNumCommune = getField(2);
            periodeIFD = getField(2);
            neNumBDP = getField(11);
            neNumClient = getField(15);
            revenu2 = getField(9);
            // NE met le renseignement dans annee2 pour les taxations
            // preanumerando
            annee2 = getField(4);
            anneeRef = annee2;
            revenu1 = getField(9);
            annee1 = getField(4);
            capital = getField(9);
            neGenreTaxation = getField(3);
            neTaxationRectificative = getField(1);
            neDateDebutAssuj = getField(10);
            fortune = getField(9);
            neFortuneAnnee1 = getField(9);
            nePensionAnnee1 = getField(9);
            nePension = getField(9);
            nePensionAlimentaire1 = getField(9);
            nePensionAlimentaire = getField(9);
            neRenteViagere1 = getField(9);
            neRenteViagere = getField(9);
            neIndemniteJour1 = getField(9);
            neIndemniteJour = getField(9);
            neRenteTotale1 = getField(9);
            neRenteTotale = getField(9);
            neDateValeur = getField(10);
            neDossierTaxe = getField(1);
            neDossierTrouve = getField(1);
            debutExercice1 = getField(10);
            finExercice1 = getField(10);
            endLine();

            if (valide) {
                return SUCCES;
            } else {
                return EN_ERREUR;
            }
        }

        public void setAnneeRef(String string) {
            anneeRef = string;
        }

    }

    @Override
    protected CPReception getReception(CPProcessAsciiReader reader, int nbEntreesLues) {
        return new CPNeReception(reader, nbEntreesLues);
    }

}
