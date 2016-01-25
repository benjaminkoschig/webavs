package ch.globaz.pegasus.business.vo.decompte;

import ch.globaz.pegasus.business.models.dettecomptatcompense.SimpleDetteComptatCompense;

public class DetteEnComptaVO {
    private String description = null;
    private SimpleDetteComptatCompense dette = null;

    public DetteEnComptaVO() {

    }

    public DetteEnComptaVO(SimpleDetteComptatCompense dette, String description) {

        this.dette = dette;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public SimpleDetteComptatCompense getDette() {
        return dette;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDette(SimpleDetteComptatCompense dette) {
        this.dette = dette;
    }

}
