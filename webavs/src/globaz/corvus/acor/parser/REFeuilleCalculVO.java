package globaz.corvus.acor.parser;

import java.util.ArrayList;
import java.util.List;

public class REFeuilleCalculVO {

    public class ElementVO {
        private String genreRente = "";
        private String idTiers = "";
        private String montantRente = "";
        private String RAM = "";

        public String getGenreRente() {
            return genreRente;
        }

        public String getIdTiers() {
            return idTiers;
        }

        public String getMontantRente() {
            return montantRente;
        }

        public String getRAM() {
            return RAM;
        }

        public void setGenreRente(String genreRente) {
            this.genreRente = genreRente;
        }

        public void setIdTiers(String idTiers) {
            this.idTiers = idTiers;
        }

        public void setMontantRente(String montantRente) {
            this.montantRente = montantRente;
        }

        public void setRAM(String rAM) {
            RAM = rAM;
        }
    }

    public List<ElementVO> elementsAP = new ArrayList<ElementVO>();

    public void addElementVO(ElementVO elm) {
        elementsAP.add(elm);
    }

    public List<ElementVO> getElementsAP() {
        return elementsAP;
    }

}
