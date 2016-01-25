package globaz.phenix.process.communications.receptionReaderImpl;

import globaz.phenix.db.communications.CPCommentaireCommunication;
import globaz.phenix.process.communications.CPProcessAsciiReader;

public class CPJuReader extends CPProcessAsciiReader {

    private static final long serialVersionUID = 8747598785700875290L;

    protected class CPJuReception extends CPReception {

        private String capital;
        private String fortune;
        private String genreAffilie;
        private String genreTaxation;
        private String juCodeApplication;
        private String juDateNaissance;
        private String juEpoux;
        private String juFiller;
        private String juGenreAffilie;
        private String juGenreTaxation;
        private String juLot;
        private String juNbrJour1;
        private String juNbrJour2;
        private String juNewNumContribuable;
        private String juNumContribuable;
        private String juTaxeMan;
        private String periodeIFD;
        private String revenu1;
        private String revenu2;

        /**
         * @param reader
         * @param nbEntreesLues
         */
        public CPJuReception(CPProcessAsciiReader reader, int nbEntreesLues) {
            super(reader, nbEntreesLues);
        }

        @Override
        public String getCapital() {
            return capital;
        }

        @Override
        public String getFortune() {
            return fortune;
        }

        /**
         * @return
         */
        @Override
        public String getGenreAffilie() {
            if (juGenreAffilie.equals("I")) {
                genreAffilie = "602001";
            } else if (juGenreAffilie.equals("N")) {
                genreAffilie = "602002";
            } else {
                genreAffilie = "";
            }
            return genreAffilie;
        }

        /**
         * @return
         */
        @Override
        public String getGenreTaxation() {
            if (juGenreTaxation.equals("4")) {
                genreTaxation = CPCommentaireCommunication.CS_GENRE_TP;
            } else {
                genreTaxation = CPCommentaireCommunication.CS_GENRE_TD;
            }
            return genreTaxation;
        }

        /**
         * @return
         */
        @Override
        public String getJuCodeApplication() {
            return juCodeApplication;
        }

        /**
         * @return
         */
        @Override
        public String getJuDateNaissance() {
            return juDateNaissance;
        }

        /**
         * @return
         */
        @Override
        public String getJuEpoux() {
            return juEpoux;
        }

        /**
         * @return
         */
        @Override
        public String getJuFiller() {
            return juFiller;
        }

        /**
         * @return
         */
        @Override
        public String getJuGenreAffilie() {
            return juGenreAffilie;
        }

        /**
         * @return
         */
        @Override
        public String getJuGenreTaxation() {
            return juGenreTaxation;
        }

        /**
         * @return
         */
        @Override
        public String getJuLot() {
            return juLot;
        }

        /**
         * @return
         */
        @Override
        public String getJuNbrJour1() {
            return juNbrJour1;
        }

        /**
         * @return
         */
        @Override
        public String getJuNbrJour2() {
            return juNbrJour2;
        }

        /**
         * @return
         */
        @Override
        public String getJuNewNumContribuable() {
            return juNewNumContribuable;
        }

        /**
         * @return
         */
        @Override
        public String getJuNumContribuable() {
            return juNumContribuable;
        }

        /**
         * @return
         */
        @Override
        public String getJuTaxeMan() {
            return juTaxeMan;
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
            juCodeApplication = getField(3);
            juNumContribuable = getField(11);
            periodeIFD = getField(2);
            juLot = getField(1);
            juNbrJour1 = getField(3);
            juNbrJour2 = getField(3);
            juGenreAffilie = getField(1);
            juGenreTaxation = getField(1);
            juEpoux = getField(1);
            revenu1 = getField(11);
            revenu2 = getField(11);
            if ("I".equalsIgnoreCase(juGenreAffilie)) {
                capital = getField(11);
            } else {
                fortune = getField(11);
            }
            juFiller = getField(9);
            juTaxeMan = getField(1);
            juDateNaissance = getField(6);
            juNewNumContribuable = getField(11);
            endLine();
            if (valide) {
                return SUCCES;
            } else {
                return EN_ERREUR;
            }
        }

        public void setFortune(String fortune) {
            this.fortune = fortune;
        }

    }

    @Override
    protected CPReception getReception(CPProcessAsciiReader reader, int nbEntreesLues) {
        return new CPJuReception(reader, nbEntreesLues);
    }

}
