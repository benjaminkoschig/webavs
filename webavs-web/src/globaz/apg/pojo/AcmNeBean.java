package globaz.apg.pojo;

import globaz.osiris.api.APIRubrique;

public class AcmNeBean {

    private APIRubrique rubriqueCompensation;
    private APIRubrique rubriqueDeBase;
    private APIRubrique rubriqueFondCompensation;
    private APIRubrique rubriqueImpotSource;
    private APIRubrique rubriqueRestitution;

    public APIRubrique getRubriqueCompensation() {
        return rubriqueCompensation;
    }

    public APIRubrique getRubriqueDeBase() {
        return rubriqueDeBase;
    }

    public APIRubrique getRubriqueFondCompensation() {
        return rubriqueFondCompensation;
    }

    public APIRubrique getRubriqueImpotSource() {
        return rubriqueImpotSource;
    }

    public APIRubrique getRubriqueRestitution() {
        return rubriqueRestitution;
    }

    public void setRubriqueCompensation(APIRubrique rubriqueCompensation) {
        this.rubriqueCompensation = rubriqueCompensation;
    }

    public void setRubriqueDeBase(APIRubrique rubriqueDeBase) {
        this.rubriqueDeBase = rubriqueDeBase;
    }

    public void setRubriqueFondCompensation(APIRubrique rubriqueFondCompensation) {
        this.rubriqueFondCompensation = rubriqueFondCompensation;
    }

    public void setRubriqueImpotSource(APIRubrique rubriqueImpotSource) {
        this.rubriqueImpotSource = rubriqueImpotSource;
    }

    public void setRubriqueRestitution(APIRubrique rubriqueRestitution) {
        this.rubriqueRestitution = rubriqueRestitution;
    }

}
