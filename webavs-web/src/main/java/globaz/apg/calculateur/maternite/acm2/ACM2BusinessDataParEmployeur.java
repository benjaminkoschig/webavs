package globaz.apg.calculateur.maternite.acm2;

import globaz.apg.db.droits.APSitProJointEmployeur;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import globaz.framework.util.FWCurrency;
import globaz.prestation.beans.PRPeriode;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ACM2BusinessDataParEmployeur {

    /**
     * @param situationProfJointEmployeur
     * @param prestationStandard
     * @param prestationACM1
     */
    public ACM2BusinessDataParEmployeur(String idDroit, int nombreJoursPrestationACM2,
            APSitProJointEmployeur situationProfJointEmployeur) {
        super();
        this.idDroit = idDroit;
        this.nombreJoursPrestationACM2 = nombreJoursPrestationACM2;
        this.situationProfJointEmployeur = situationProfJointEmployeur;

        prestationStandard = new ArrayList<APRepartitionJointPrestation>();
        prestationACM1 = new ArrayList<APRepartitionJointPrestation>();
        prestationLAMat = new ArrayList<APRepartitionJointPrestation>();
    }

    private String idDroit;
    private int nombreJoursPrestationACM2;
    private APSitProJointEmployeur situationProfJointEmployeur;
    private BigDecimal tauxAVS;
    private BigDecimal tauxAC;
    private FWCurrency revenuMoyenDeterminant;
    private List<APRepartitionJointPrestation> prestationStandard;
    private List<APRepartitionJointPrestation> prestationACM1;
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

    public void addPrestationStandard(APRepartitionJointPrestation prestation) {
        prestationStandard.add(prestation);
        Collections.sort(prestationStandard, getComparator());
    }

    public void addPrestationACM1(APRepartitionJointPrestation prestation) {
        prestationACM1.add(prestation);
        Collections.sort(prestationACM1, getComparator());
    }

    public boolean hasPrestationACM1() {
        return prestationACM1.size() > 0;
    }

    /**
     * Retourne une PRPeriode uniquement si la méthode hasACM1() return <code>true</code> sinon null
     * 
     * @return une PRPeriode uniquement si la méthode hasACM1() return <code>true</code> sinon null
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

    public void addPrestationLAMat(APRepartitionJointPrestation prestation) {
        prestationLAMat.add(prestation);
        Collections.sort(prestationLAMat, getComparator());
    }

    public List<APRepartitionJointPrestation> getPrestationStandard() {
        return prestationStandard;
    }

    public List<APRepartitionJointPrestation> getPrestationACM1() {
        return prestationACM1;
    }

    public List<APRepartitionJointPrestation> getPrestationLAMat() {
        return prestationLAMat;
    }

    public BigDecimal getTauxAVS() {
        return tauxAVS;
    }

    public void setTauxAVS(BigDecimal tauxAVS) {
        this.tauxAVS = tauxAVS;
    }

    public BigDecimal getTauxAC() {
        return tauxAC;
    }

    public void setTauxAC(BigDecimal tauxAC) {
        this.tauxAC = tauxAC;
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
                    // Même date de début
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
