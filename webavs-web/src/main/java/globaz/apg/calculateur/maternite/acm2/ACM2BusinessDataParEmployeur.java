package globaz.apg.calculateur.maternite.acm2;

import globaz.apg.db.droits.APSitProJointEmployeur;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import globaz.framework.util.FWCurrency;
import globaz.prestation.beans.PRPeriode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ACM2BusinessDataParEmployeur {

    /**
     * @param idDroit
     * @param nombreJoursPrestationACM2
     * @param situationProfJointEmployeur
     */
    public ACM2BusinessDataParEmployeur(String idDroit, int nombreJoursPrestationACM2,
            APSitProJointEmployeur situationProfJointEmployeur) {
        super();
        this.idDroit = idDroit;
        this.nombreJoursPrestationACM2 = nombreJoursPrestationACM2;
        this.situationProfJointEmployeur = situationProfJointEmployeur;

        prestationStandard = new ArrayList<APRepartitionJointPrestation>();
        prestationACM1 = new ArrayList<APRepartitionJointPrestation>();
        prestationMATCIAB1 = new ArrayList<APRepartitionJointPrestation>();
        prestationLAMat = new ArrayList<APRepartitionJointPrestation>();
    }

    private String idDroit;
    private int nombreJoursPrestationACM2;
    private Integer nombreInitialDeSituationsProfessionelles = 0;
    private APSitProJointEmployeur situationProfJointEmployeur;
    private FWCurrency revenuMoyenDeterminant;
    private List<APRepartitionJointPrestation> prestationStandard;
    private List<APRepartitionJointPrestation> prestationACM1;
    private List<APRepartitionJointPrestation> prestationMATCIAB1;
    private List<APRepartitionJointPrestation> prestationLAMat;

    public String getIdDroit() {
        return idDroit;
    }

    public int getNombreJoursPrestationACM2() {
        return nombreJoursPrestationACM2;
    }

    public APSitProJointEmployeur getSituationProfJointEmployeur() {
        return situationProfJointEmployeur;
    }

    public Integer getNombreInitialDeSituationsProfessionelles() {
        return nombreInitialDeSituationsProfessionelles;
    }

    public void setNombreInitialDeSituationsProfessionelles(Integer nombreInitialDeSituationsProfessionelles) {
        this.nombreInitialDeSituationsProfessionelles = nombreInitialDeSituationsProfessionelles;
    }

    public void addPrestationStandard(APRepartitionJointPrestation prestation) {
        prestationStandard.add(prestation);
        Collections.sort(prestationStandard, getComparator());
    }

    public void addPrestationACM1(APRepartitionJointPrestation prestation) {
        prestationACM1.add(prestation);
        Collections.sort(prestationACM1, getComparator());
    }

    public void addPrestationMATCIAB1(APRepartitionJointPrestation prestation) {
        prestationMATCIAB1.add(prestation);
        Collections.sort(prestationMATCIAB1, getComparator());
    }

    public void addPrestationLAMat(APRepartitionJointPrestation prestation) {
        prestationLAMat.add(prestation);
        Collections.sort(prestationLAMat, getComparator());
    }

    public boolean hasPrestationACM1() {
        return prestationACM1.size() > 0;
    }

    public boolean hasPrestationMATCIAB1() {
        return prestationMATCIAB1.size() > 0;
    }

    public boolean hasPrestationLAMat() {
        return prestationLAMat.size() > 0;
    }

    public boolean hasPrestationStandard() {
        return prestationStandard.size() > 0;
    }

    /**
     * Retourne une PRPeriode uniquement si la m?thode hasPrestationACM1() return <code>true</code> sinon null
     * 
     * @return une PRPeriode uniquement si la m?thode hasPrestationACM1() return <code>true</code> sinon null
     */
    public PRPeriode getPeriodeACM1() {
        PRPeriode periodePrestationACM1 = null;
        if (hasPrestationACM1()) {
            periodePrestationACM1 = new PRPeriode();
            periodePrestationACM1.setDateDeDebut(prestationACM1.get(0).getDateDebut());
            periodePrestationACM1.setDateDeFin(prestationACM1.get(prestationACM1.size() - 1).getDateFin());
        }
        return periodePrestationACM1;
    }

    /**
     * Retourne une PRPeriode uniquement si la m?thode hasPrestationStandard() return <code>true</code> sinon null
     *
     * @return une PRPeriode uniquement si la m?thode hasPrestationStandard() return <code>true</code> sinon null
     */
    public PRPeriode getPeriodeStandard() {
        PRPeriode periodePrestationStandard = null;
        if (hasPrestationStandard()) {
            periodePrestationStandard = new PRPeriode();
            periodePrestationStandard.setDateDeDebut(prestationStandard.get(0).getDateDebut());
            periodePrestationStandard.setDateDeFin(prestationStandard.get(prestationStandard.size() - 1).getDateFin());
        }
        return periodePrestationStandard;
    }

    /**
     * Retourne une PRPeriode uniquement si la m?thode hasPrestationMATCIAB1() return <code>true</code> sinon null
     *
     * @return une PRPeriode uniquement si la m?thode hasPrestationMATCIAB1() return <code>true</code> sinon null
     */
    public PRPeriode getPeriodeMATCIAB1() {
        PRPeriode periodePrestationMATCIAB1 = null;
        if (hasPrestationMATCIAB1()) {
            periodePrestationMATCIAB1 = new PRPeriode();
            periodePrestationMATCIAB1.setDateDeDebut(prestationMATCIAB1.get(0).getDateDebut());
            periodePrestationMATCIAB1.setDateDeFin(prestationMATCIAB1.get(prestationMATCIAB1.size() - 1).getDateFin());
        }
        return periodePrestationMATCIAB1;
    }

    /**
     * Retourne une PRPeriode uniquement si la m?thode hasPrestationLAMat() return <code>true</code> sinon null
     * 
     * @return une PRPeriode uniquement si la m?thode hasPrestationLAMat() return <code>true</code> sinon null
     */
    public PRPeriode getPeriodeLAMat() {
        PRPeriode periode = null;
        if (hasPrestationLAMat()) {
            periode = new PRPeriode();
            periode.setDateDeDebut(prestationLAMat.get(0).getDateDebut());
            periode.setDateDeFin(prestationLAMat.get(prestationLAMat.size() - 1).getDateFin());
        }
        return periode;
    }

    public List<APRepartitionJointPrestation> getPrestationStandard() {
        return prestationStandard;
    }

    public List<APRepartitionJointPrestation> getPrestationACM1() {
        return prestationACM1;
    }

    public List<APRepartitionJointPrestation> getPrestationMATCIAB1() {
        return prestationMATCIAB1;
    }

    public List<APRepartitionJointPrestation> getPrestationLAMat() {
        return prestationLAMat;
    }

    public FWCurrency getRevenuMoyenDeterminant() {
        return revenuMoyenDeterminant;
    }

    public void setRevenuMoyenDeterminant(FWCurrency revenuMoyenDeterminant) {
        this.revenuMoyenDeterminant = revenuMoyenDeterminant;
    }

    private Comparator<APRepartitionJointPrestation> comparator;

    public Comparator<APRepartitionJointPrestation> getComparator() {
        if (comparator == null) {
            comparator = createComparator();
        }
        return comparator;
    }

    private Comparator<APRepartitionJointPrestation> createComparator() {
        return new Comparator<APRepartitionJointPrestation>() {
            SimpleDateFormat reader = new SimpleDateFormat("dd.MM.yyyy");
            SimpleDateFormat writer = new SimpleDateFormat("yyyyMMdd");

            @Override
            public int compare(APRepartitionJointPrestation o1, APRepartitionJointPrestation o2) {
                int d1;
                try {
                    d1 = Integer.valueOf(writer.format(reader.parse(o1.getDateDebut())));

                    int d2 = Integer.valueOf(writer.format(reader.parse(o2.getDateDebut())));
                    if (d1 < d2) {
                        return -1;
                    } else if (d1 > d2) {
                        return 1;
                    }
                    // M?me date de d?but
                    else {
                        int df1 = Integer.valueOf(writer.format(reader.parse(o1.getDateFin())));
                        int df2 = Integer.valueOf(writer.format(reader.parse(o2.getDateFin())));
                        if (df1 < df2) {
                            return -1;
                        } else if (df1 > df2) {
                            return 1;
                        }
                    }
                    return 0;
                } catch (Exception e) {
                    throw new RuntimeException("Exception throw on periode comparison : " + e.toString());
                }
            }
        };
    }
}
