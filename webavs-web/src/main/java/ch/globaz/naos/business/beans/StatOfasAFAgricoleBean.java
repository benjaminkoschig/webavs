package ch.globaz.naos.business.beans;

import globaz.framework.util.FWCurrency;

public class StatOfasAFAgricoleBean {

    private double montantAllocationEnfantHorsUE;
    private double montantAllocationEnfantSuisse;
    private double montantAllocationEnfantUE;
    private double montantAllocationFormationHorsUE;
    private double montantAllocationFormationSuisse;
    private double montantAllocationFormationUE;
    private double montantAllocationMenage;
    private double montantTotalAllocation;
    private double montantTotalAllocationDifferentielle;
    private double montantTotalAllocationEnfant;
    private double montantTotalAllocationFormation;
    private int nombreAllocationEnfantHorsUE;
    private int nombreAllocationEnfantSuisse;
    private int nombreAllocationEnfantUE;
    private int nombreAllocationFormationHorsUE;
    private int nombreAllocationFormationSuisse;
    private int nombreAllocationFormationUE;
    private int nombreAllocationMenage;
    private int nombreAyantDroit;
    private int nombreAyantDroitEtranger;
    private int nombreTotalAllocation;
    private int nombreTotalAllocationDifferentielle;
    private int nombreTotalAllocationEnfant;
    private int nombreTotalAllocationFormation;

    public StatOfasAFAgricoleBean() {

        nombreAyantDroit = 0;
        nombreAyantDroitEtranger = 0;
        nombreAllocationEnfantSuisse = 0;
        nombreAllocationEnfantUE = 0;
        nombreAllocationEnfantHorsUE = 0;
        nombreTotalAllocationEnfant = 0;
        nombreAllocationFormationSuisse = 0;
        nombreAllocationFormationUE = 0;
        nombreAllocationFormationHorsUE = 0;
        nombreTotalAllocationFormation = 0;
        nombreAllocationMenage = 0;
        nombreTotalAllocation = 0;
        nombreTotalAllocationDifferentielle = 0;
        montantAllocationEnfantSuisse = 0;
        montantAllocationEnfantUE = 0;
        montantAllocationEnfantHorsUE = 0;
        montantTotalAllocationEnfant = 0;
        montantAllocationFormationSuisse = 0;
        montantAllocationFormationUE = 0;
        montantAllocationFormationHorsUE = 0;
        montantTotalAllocationFormation = 0;
        montantAllocationMenage = 0;
        montantTotalAllocation = 0;
        montantTotalAllocationDifferentielle = 0;

    }

    public double getMontantAllocationEnfantHorsUE() {
        return montantAllocationEnfantHorsUE;
    }

    public String getMontantAllocationEnfantHorsUEString() {
        return new FWCurrency(montantAllocationEnfantHorsUE).toStringFormat();
    }

    public double getMontantAllocationEnfantSuisse() {
        return montantAllocationEnfantSuisse;
    }

    public String getMontantAllocationEnfantSuisseString() {
        return new FWCurrency(montantAllocationEnfantSuisse).toStringFormat();
    }

    public double getMontantAllocationEnfantUE() {
        return montantAllocationEnfantUE;
    }

    public String getMontantAllocationEnfantUEString() {
        return new FWCurrency(montantAllocationEnfantUE).toStringFormat();
    }

    public double getMontantAllocationFormationHorsUE() {
        return montantAllocationFormationHorsUE;
    }

    public String getMontantAllocationFormationHorsUEString() {
        return new FWCurrency(montantAllocationFormationHorsUE).toStringFormat();
    }

    public double getMontantAllocationFormationSuisse() {
        return montantAllocationFormationSuisse;
    }

    public String getMontantAllocationFormationSuisseString() {
        return new FWCurrency(montantAllocationFormationSuisse).toStringFormat();
    }

    public double getMontantAllocationFormationUE() {
        return montantAllocationFormationUE;
    }

    public String getMontantAllocationFormationUEString() {
        return new FWCurrency(montantAllocationFormationUE).toStringFormat();
    }

    public double getMontantAllocationMenage() {
        return montantAllocationMenage;
    }

    public String getMontantAllocationMenageString() {
        return new FWCurrency(montantAllocationMenage).toStringFormat();
    }

    public double getMontantTotalAllocation() {
        return montantTotalAllocation;
    }

    public double getMontantTotalAllocationDifferentielle() {
        return montantTotalAllocationDifferentielle;
    }

    public String getMontantTotalAllocationDifferentielleString() {
        return new FWCurrency(montantTotalAllocationDifferentielle).toStringFormat();
    }

    public double getMontantTotalAllocationEnfant() {
        return montantTotalAllocationEnfant;
    }

    public String getMontantTotalAllocationEnfantString() {
        return new FWCurrency(montantTotalAllocationEnfant).toStringFormat();
    }

    public double getMontantTotalAllocationFormation() {
        return montantTotalAllocationFormation;
    }

    public String getMontantTotalAllocationFormationString() {
        return new FWCurrency(montantTotalAllocationFormation).toStringFormat();
    }

    public String getMontantTotalAllocationString() {
        return new FWCurrency(montantTotalAllocation).toStringFormat();
    }

    public int getNombreAllocationEnfantHorsUE() {
        return nombreAllocationEnfantHorsUE;
    }

    public String getNombreAllocationEnfantHorsUEString() {
        return String.valueOf(nombreAllocationEnfantHorsUE);
    }

    public int getNombreAllocationEnfantSuisse() {
        return nombreAllocationEnfantSuisse;
    }

    public String getNombreAllocationEnfantSuisseString() {
        return String.valueOf(nombreAllocationEnfantSuisse);
    }

    public int getNombreAllocationEnfantUE() {
        return nombreAllocationEnfantUE;
    }

    public String getNombreAllocationEnfantUEString() {
        return String.valueOf(nombreAllocationEnfantUE);
    }

    public int getNombreAllocationFormationHorsUE() {
        return nombreAllocationFormationHorsUE;
    }

    public String getNombreAllocationFormationHorsUEString() {
        return String.valueOf(nombreAllocationFormationHorsUE);
    }

    public int getNombreAllocationFormationSuisse() {
        return nombreAllocationFormationSuisse;
    }

    public String getNombreAllocationFormationSuisseString() {
        return String.valueOf(nombreAllocationFormationSuisse);
    }

    public int getNombreAllocationFormationUE() {
        return nombreAllocationFormationUE;
    }

    public String getNombreAllocationFormationUEString() {
        return String.valueOf(nombreAllocationFormationUE);
    }

    public int getNombreAllocationMenage() {
        return nombreAllocationMenage;
    }

    public String getNombreAllocationMenageString() {
        return String.valueOf(nombreAllocationMenage);
    }

    public int getNombreAyantDroit() {
        return nombreAyantDroit;
    }

    public int getNombreAyantDroitEtranger() {
        return nombreAyantDroitEtranger;
    }

    public String getNombreAyantDroitEtrangerString() {
        return String.valueOf(nombreAyantDroitEtranger);
    }

    public String getNombreAyantDroitString() {
        return String.valueOf(nombreAyantDroit);
    }

    public int getNombreTotalAllocation() {
        return nombreTotalAllocation;
    }

    public int getNombreTotalAllocationDifferentielle() {
        return nombreTotalAllocationDifferentielle;
    }

    public String getNombreTotalAllocationDifferentielleString() {
        return String.valueOf(nombreTotalAllocationDifferentielle);
    }

    public int getNombreTotalAllocationEnfant() {
        return nombreTotalAllocationEnfant;
    }

    public String getNombreTotalAllocationEnfantString() {
        return String.valueOf(nombreTotalAllocationEnfant);
    }

    public int getNombreTotalAllocationFormation() {
        return nombreTotalAllocationFormation;
    }

    public String getNombreTotalAllocationFormationString() {
        return String.valueOf(nombreTotalAllocationFormation);
    }

    public String getNombreTotalAllocationString() {
        return String.valueOf(nombreTotalAllocation);
    }

    public void setMontantAllocationEnfantHorsUE(double montantAllocationEnfantHorsUE) {
        this.montantAllocationEnfantHorsUE = montantAllocationEnfantHorsUE;
    }

    public void setMontantAllocationEnfantSuisse(double montantAllocationEnfantSuisse) {
        this.montantAllocationEnfantSuisse = montantAllocationEnfantSuisse;
    }

    public void setMontantAllocationEnfantUE(double montantAllocationEnfantUE) {
        this.montantAllocationEnfantUE = montantAllocationEnfantUE;
    }

    public void setMontantAllocationFormationHorsUE(double montantAllocationFormationHorsUE) {
        this.montantAllocationFormationHorsUE = montantAllocationFormationHorsUE;
    }

    public void setMontantAllocationFormationSuisse(double montantAllocationFormationSuisse) {
        this.montantAllocationFormationSuisse = montantAllocationFormationSuisse;
    }

    public void setMontantAllocationFormationUE(double montantAllocationFormationUE) {
        this.montantAllocationFormationUE = montantAllocationFormationUE;
    }

    public void setMontantAllocationMenage(double montantAllocationMenage) {
        this.montantAllocationMenage = montantAllocationMenage;
    }

    public void setMontantTotalAllocation(double montantTotalAllocation) {
        this.montantTotalAllocation = montantTotalAllocation;
    }

    public void setMontantTotalAllocationDifferentielle(double montantTotalAllocationDifferentielle) {
        this.montantTotalAllocationDifferentielle = montantTotalAllocationDifferentielle;
    }

    public void setMontantTotalAllocationEnfant(double montantTotalAllocationEnfant) {
        this.montantTotalAllocationEnfant = montantTotalAllocationEnfant;
    }

    public void setMontantTotalAllocationFormation(double montantTotalAllocationFormation) {
        this.montantTotalAllocationFormation = montantTotalAllocationFormation;
    }

    public void setNombreAllocationEnfantHorsUE(int nombreAllocationEnfantHorsUE) {
        this.nombreAllocationEnfantHorsUE = nombreAllocationEnfantHorsUE;
    }

    public void setNombreAllocationEnfantSuisse(int nombreAllocationEnfantSuisse) {
        this.nombreAllocationEnfantSuisse = nombreAllocationEnfantSuisse;
    }

    public void setNombreAllocationEnfantUE(int nombreAllocationEnfantUE) {
        this.nombreAllocationEnfantUE = nombreAllocationEnfantUE;
    }

    public void setNombreAllocationFormationHorsUE(int nombreAllocationFormationHorsUE) {
        this.nombreAllocationFormationHorsUE = nombreAllocationFormationHorsUE;
    }

    public void setNombreAllocationFormationSuisse(int nombreAllocationFormationSuisse) {
        this.nombreAllocationFormationSuisse = nombreAllocationFormationSuisse;
    }

    public void setNombreAllocationFormationUE(int nombreAllocationFormationUE) {
        this.nombreAllocationFormationUE = nombreAllocationFormationUE;
    }

    public void setNombreAllocationMenage(int nombreAllocationMenage) {
        this.nombreAllocationMenage = nombreAllocationMenage;
    }

    public void setNombreAyantDroit(int nombreAyantDroit) {
        this.nombreAyantDroit = nombreAyantDroit;
    }

    public void setNombreAyantDroitEtranger(int nombreAyantDroitEtranger) {
        this.nombreAyantDroitEtranger = nombreAyantDroitEtranger;
    }

    public void setNombreTotalAllocation(int nombreTotalAllocation) {
        this.nombreTotalAllocation = nombreTotalAllocation;
    }

    public void setNombreTotalAllocationDifferentielle(int nombreTotalAllocationDifferentielle) {
        this.nombreTotalAllocationDifferentielle = nombreTotalAllocationDifferentielle;
    }

    public void setNombreTotalAllocationEnfant(int nombreTotalAllocationEnfant) {
        this.nombreTotalAllocationEnfant = nombreTotalAllocationEnfant;
    }

    public void setNombreTotalAllocationFormation(int nombreTotalAllocationFormation) {
        this.nombreTotalAllocationFormation = nombreTotalAllocationFormation;
    }

}