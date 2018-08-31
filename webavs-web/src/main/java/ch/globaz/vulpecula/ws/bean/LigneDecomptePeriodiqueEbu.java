package ch.globaz.vulpecula.ws.bean;

import globaz.jade.client.util.JadeStringUtil;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.decompte.Absence;
import ch.globaz.vulpecula.domain.models.decompte.CodeErreur;
import ch.globaz.vulpecula.domain.models.decompte.CodeErreurDecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeSalaire;

/**
 * Description de la classe
 * 
 * @since eBMS 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ligneDecomptePeriodique")
public class LigneDecomptePeriodiqueEbu {
    @XmlElement(nillable = false, required = true)
    private StatusLine status = StatusLine.UNCHANGED;

    private String idDecompteLine;
    private String lineCorrelationId;
    private PosteTravailEbu posteTravail;
    private String periodeDebut;
    private String periodeFin;
    private double nombreHeure;
    private double salaireHoraire;
    private double totalSalaire;
    private double tauxContribution;
    private String remarque = "";

    private AbsencesEbu absences = new AbsencesEbu();

    public LigneDecomptePeriodiqueEbu() {
        // Constructeur par defaut obligatoire pour le bon fonctionnement du framework
    }

    public LigneDecomptePeriodiqueEbu(DecompteSalaire ligne, StatusLine status) {

        update(ligne, status);

    }

    public LigneDecomptePeriodiqueEbu(DecompteSalaire ligne, boolean isSynchro) {
        if (isSynchro) {
            update(ligne, status);
        } else {
            updateNoSynchro(ligne, status);
        }
    }

    public DecompteSalaire getDecompteSalaire() {
        DecompteSalaire decompteSalaire = new DecompteSalaire();
        decompteSalaire.setHeures(nombreHeure);
        decompteSalaire.setSalaireHoraire(new Montant(salaireHoraire));
        decompteSalaire.calculChampSalaire();
        decompteSalaire.setSalaireTotal(new Montant(totalSalaire));

        decompteSalaire.setPeriode(new Periode(periodeDebut, periodeFin));
        return decompteSalaire;
    }

    public void update(DecompteSalaire ligne, StatusLine status) {

        idDecompteLine = ligne.getId();
        lineCorrelationId = ligne.getLineCorrelationId();

        DecompteSalaire oldLigne = null;
        boolean hasPoste = ligne.getPosteTravail() != null;

        if (hasPoste) {
            hasPoste = !JadeStringUtil.isEmpty(ligne.getPosteTravail().getId());
        }

        if (hasPoste && !JadeStringUtil.isEmpty(ligne.getDecompte().getDateEtablissement().getValue())) {
            oldLigne = VulpeculaRepositoryLocator.getDecompteSalaireRepository().findPrecedentComptabilise(
                    ligne.getPosteTravail().getId(), ligne.getDecompte().getPeriodeDebut());
        }

        if (hasPoste && ligne.getPosteTravail().getTypeSalaireAsValue().equals(TypeSalaire.HEURES.getValue())) {
            if (ligne.getDecompte().getDateReception() == null
                    || JadeStringUtil.isBlankOrZero(ligne.getDecompte().getDateReception().getSwissValue())) {
                nombreHeure = 0.00;
                totalSalaire = 0.00;
            } else {
                nombreHeure = ligne.getHeures();
                totalSalaire = ligne.getSalaireTotal().doubleValue();
            }

            salaireHoraire = ligne.getSalaireHoraire().doubleValue();
            if (Double.compare(ligne.getSalaireHoraire().doubleValue(), 0.00) != 0) {
                salaireHoraire = ligne.getSalaireHoraire().doubleValue();
            } else if (Double.compare(ligne.getSalaireHoraire().doubleValue(), 0.00) == 0 && oldLigne != null) {
                salaireHoraire = oldLigne.getSalaireHoraire().doubleValue();
            }
        } else if (hasPoste && ligne.getPosteTravail().getTypeSalaireAsValue().equals(TypeSalaire.MOIS.getValue())) {
            totalSalaire = ligne.getSalaireTotal().doubleValue();
            if (Double.compare(ligne.getHeures(), 0.00) != 0) {
                nombreHeure = ligne.getHeures();
            } else if (Double.compare(ligne.getHeures(), 0.00) == 0 && oldLigne != null) {
                nombreHeure = oldLigne.getHeures();
            }

            if (Double.compare(ligne.getSalaireTotal().doubleValue(), 0.00) != 0) {
                totalSalaire = ligne.getSalaireTotal().doubleValue();
            } else if (Double.compare(ligne.getSalaireTotal().doubleValue(), 0.00) == 0 && oldLigne != null) {
                totalSalaire = oldLigne.getSalaireTotal().doubleValue();
            }

            if (ligne.getSalaireHoraire() != null && !JadeStringUtil.isEmpty(ligne.getSalaireHoraireAsValue())) {
                salaireHoraire = ligne.getSalaireHoraire().doubleValue();
            }

            if (ligne.getDecompte().getDateReception() != null
                    && !JadeStringUtil.isBlankOrZero(ligne.getDecompte().getDateReception().getSwissValue())) {
                nombreHeure = ligne.getHeures();
                salaireHoraire = ligne.getSalaireHoraire().doubleValue();
                totalSalaire = ligne.getSalaireTotal().doubleValue();
            }

            if (!ligne.getAbsences().isEmpty() && oldLigne != null) {
                salaireHoraire = oldLigne.getSalaireHoraire().doubleValue();
            }
        } else if (hasPoste && ligne.getPosteTravail().getTypeSalaireAsValue().equals(TypeSalaire.CONSTANT.getValue())) {
            if (ligne.getDecompte().getPeriodeDebut().getMois().equals("01")) {
                int year = Integer.valueOf(ligne.getPeriodeDebut().getYear()) - 1;
                Date dateRef = new Date("30.11." + year);
                oldLigne = VulpeculaRepositoryLocator.getDecompteSalaireRepository().findPrecedentComptabilise(
                        ligne.getPosteTravail().getId(), dateRef);
            }
            totalSalaire = ligne.getSalaireTotal().doubleValue();
            if (Double.compare(ligne.getHeures(), 0.00) != 0) {
                nombreHeure = ligne.getHeures();
            } else if (Double.compare(ligne.getHeures(), 0.00) == 0 && oldLigne != null) {
                nombreHeure = oldLigne.getHeures();
            }

            if (Double.compare(ligne.getSalaireTotal().doubleValue(), 0.00) != 0) {
                totalSalaire = ligne.getSalaireTotal().doubleValue();
            } else if (Double.compare(ligne.getSalaireTotal().doubleValue(), 0.00) == 0 && oldLigne != null) {
                totalSalaire = oldLigne.getSalaireTotal().doubleValue();
            }

            if (Double.compare(ligne.getSalaireHoraire().doubleValue(), 0.00) != 0) {
                salaireHoraire = ligne.getSalaireHoraire().doubleValue();
            } else if (Double.compare(ligne.getSalaireHoraire().doubleValue(), 0.00) == 0 && oldLigne != null) {
                salaireHoraire = oldLigne.getSalaireHoraire().doubleValue();
            } else {
                salaireHoraire = 0.00;
            }

            if (ligne.getDecompte().getDateReception() != null
                    && !JadeStringUtil.isBlankOrZero(ligne.getDecompte().getDateReception().getSwissValue())) {
                nombreHeure = ligne.getHeures();
                salaireHoraire = ligne.getSalaireHoraire().doubleValue();
                totalSalaire = ligne.getSalaireTotal().doubleValue();
            }

            if (!ligne.getAbsences().isEmpty() && oldLigne != null) {
                salaireHoraire = oldLigne.getSalaireHoraire().doubleValue();
            }

        }

        // Tant que le décompte n'est pas validé on retourne le taux saisis sur le portail
        if (!EtatDecompte.VALIDE.equals(ligne.getDecompte().getEtat()) && ligne.getTauxContribuableAffiche() != null
                && !JadeStringUtil.isEmpty(ligne.getTauxContribuableAfficheAsValue())) {
            tauxContribution = Double.parseDouble(ligne.getTauxContribuableAfficheAsValue());
        } else {
            tauxContribution = ligne.getTauxContribuableForCaissesSociales(true).doubleValue();
        }

        this.status = status;

        if (ligne.getPosteTravail() != null) {
            posteTravail = new PosteTravailEbu(ligne.getPosteTravail());
            posteTravail.setPosteCorrelationId(ligne.getPosteCorrelationId());
            if (JadeStringUtil.isEmpty(posteTravail.getCorrelationId())) {
                posteTravail.setCorrelationId(ligne.getCorrelationId());
            }
        } else {
            posteTravail = new PosteTravailEbu();
            posteTravail.setCorrelationId(ligne.getCorrelationId());
            posteTravail.setPosteCorrelationId(ligne.getPosteCorrelationId());
        }
        periodeDebut = ligne.getPeriodeDebutAsSwissValue();
        periodeFin = ligne.getPeriodeFinAsSwissValue();

        for (Absence abs : ligne.getAbsences()) {
            absences.addAbsence(abs.getType());
        }

        // TODO à modifier selon BMS
        for (CodeErreurDecompteSalaire code : ligne.getListeCodeErreur()) {
            if (CodeErreur.NOUVELLE_LIGNE.equals(code.getCodeErreur())) {
                salaireHoraire = Montant.ZERO.doubleValue();
                nombreHeure = 0.00;
            }
        }

    }

    public void updateNoSynchro(DecompteSalaire ligne, StatusLine status) {
        idDecompteLine = ligne.getId();
        lineCorrelationId = ligne.getLineCorrelationId();
        boolean hasPoste = ligne.getPosteTravail() != null && ligne.getPosteTravail().getId() != null
                && !JadeStringUtil.isEmpty(ligne.getPosteTravail().getId());

        if (hasPoste && ligne.getPosteTravail().getTypeSalaireAsValue().equals(TypeSalaire.HEURES.getValue())) {
            if (ligne.getDecompte().getDateReception() == null
                    || JadeStringUtil.isBlankOrZero(ligne.getDecompte().getDateReception().getSwissValue())) {
                nombreHeure = 0.00;
                totalSalaire = 0.00;
            } else {
                nombreHeure = ligne.getHeures();
                totalSalaire = ligne.getSalaireTotal().doubleValue();
            }
            salaireHoraire = ligne.getSalaireHoraire().doubleValue();

        } else if (hasPoste && ligne.getPosteTravail().getTypeSalaireAsValue().equals(TypeSalaire.CONSTANT.getValue())) {
            DecompteSalaire oldLigne = null;

            if (hasPoste && !JadeStringUtil.isEmpty(ligne.getDecompte().getDateEtablissement().getValue())) {
                oldLigne = VulpeculaRepositoryLocator.getDecompteSalaireRepository().findPrecedentComptabilise(
                        ligne.getPosteTravail().getId(), ligne.getDecompte().getPeriodeDebut());
            }
            if (ligne.getDecompte().getPeriodeDebut().getMois().equals("01")) {
                int year = Integer.valueOf(ligne.getPeriodeDebut().getYear()) - 1;
                Date dateRef = new Date("30.11." + year);
                oldLigne = VulpeculaRepositoryLocator.getDecompteSalaireRepository().findPrecedentComptabilise(
                        ligne.getPosteTravail().getId(), dateRef);
            }
            totalSalaire = ligne.getSalaireTotal().doubleValue();
            if (Double.compare(ligne.getHeures(), 0.00) != 0) {
                nombreHeure = ligne.getHeures();
            } else if (Double.compare(ligne.getHeures(), 0.00) == 0 && oldLigne != null) {
                nombreHeure = oldLigne.getHeures();
            }

            if (Double.compare(ligne.getSalaireTotal().doubleValue(), 0.00) != 0) {
                totalSalaire = ligne.getSalaireTotal().doubleValue();
            } else if (Double.compare(ligne.getSalaireTotal().doubleValue(), 0.00) == 0 && oldLigne != null) {
                totalSalaire = oldLigne.getSalaireTotal().doubleValue();
            }

            if (Double.compare(ligne.getSalaireHoraire().doubleValue(), 0.00) != 0) {
                salaireHoraire = ligne.getSalaireHoraire().doubleValue();
            } else if (Double.compare(ligne.getSalaireHoraire().doubleValue(), 0.00) == 0 && oldLigne != null) {
                salaireHoraire = oldLigne.getSalaireHoraire().doubleValue();
            } else {
                salaireHoraire = 0.00;
            }

            // if (oldLigne.getSalaireHoraire() != null && !JadeStringUtil.isEmpty(oldLigne.getSalaireHoraireAsValue()))
            // {
            // salaireHoraire = oldLigne.getSalaireHoraire().doubleValue();
            // } else {
            // salaireHoraire = 0.00;
            // }

            if (ligne.getDecompte().getDateReception() != null
                    && !JadeStringUtil.isBlankOrZero(ligne.getDecompte().getDateReception().getSwissValue())) {
                nombreHeure = ligne.getHeures();
                salaireHoraire = ligne.getSalaireHoraire().doubleValue();
                totalSalaire = ligne.getSalaireTotal().doubleValue();
            }

            if (!ligne.getAbsences().isEmpty() && oldLigne != null) {
                salaireHoraire = oldLigne.getSalaireHoraire().doubleValue();
            }

        } else {
            totalSalaire = ligne.getSalaireTotal().doubleValue();
            nombreHeure = ligne.getHeures();
            salaireHoraire = ligne.getSalaireHoraire().doubleValue();

        }

        // Tant que le décompte n'est pas validé on retourne le taux saisi sur le portail
        if (!EtatDecompte.VALIDE.equals(ligne.getDecompte().getEtat()) && ligne.getTauxContribuableAffiche() != null
                && !JadeStringUtil.isEmpty(ligne.getTauxContribuableAfficheAsValue())) {
            tauxContribution = Double.parseDouble(ligne.getTauxContribuableAfficheAsValue());
        } else {
            tauxContribution = ligne.getTauxContribuableForCaissesSociales(true).doubleValue();
        }

        this.status = status;

        if (ligne.getPosteTravail() != null) {
            posteTravail = new PosteTravailEbu(ligne.getPosteTravail());
            posteTravail.setPosteCorrelationId(ligne.getPosteCorrelationId());
            if (JadeStringUtil.isEmpty(posteTravail.getCorrelationId())) {
                posteTravail.setCorrelationId(ligne.getCorrelationId());
            }
        } else {
            posteTravail = new PosteTravailEbu();
            posteTravail.setCorrelationId(ligne.getCorrelationId());
            posteTravail.setPosteCorrelationId(ligne.getPosteCorrelationId());
        }
        periodeDebut = ligne.getPeriodeDebutAsSwissValue();
        periodeFin = ligne.getPeriodeFinAsSwissValue();

        for (Absence abs : ligne.getAbsences()) {
            absences.addAbsence(abs.getType());
        }

        // TODO à modifier selon BMS
        for (CodeErreurDecompteSalaire code : ligne.getListeCodeErreur()) {
            if (CodeErreur.NOUVELLE_LIGNE.equals(code.getCodeErreur())) {
                salaireHoraire = Montant.ZERO.doubleValue();
                nombreHeure = 0.00;
            }
        }
    }

    /**
     * @return the idDecompteLine
     */
    public String getIdDecompteLine() {
        return idDecompteLine;
    }

    public void setIdDecompteLine(String p_idDecompteLine) {
        idDecompteLine = p_idDecompteLine;
    }

    public double getTauxContribution() {
        return tauxContribution;
    }

    public void setTauxContribution(double tauxContribution) {
        this.tauxContribution = tauxContribution;
    }

    /**
     * @return the nombreHeure
     */
    public double getNombreHeure() {
        return nombreHeure;
    }

    /**
     * @return the salaireHoraire
     */
    public double getSalaireHoraire() {
        return salaireHoraire;
    }

    /**
     * @return the totalSalaire
     */
    public double getTotalSalaire() {
        return totalSalaire;
    }

    /**
     * @return the remarque
     */
    public String getRemarque() {
        return remarque;
    }

    /**
     * @return the status
     */
    public StatusLine getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(StatusLine status) {
        this.status = status;
    }

    /**
     * @param listAbsence the listAbsence to set
     */
    public void setAbsences(AbsencesEbu listAbsence) {
        absences = listAbsence;
    }

    /**
     * @return the posteTravail
     */
    public PosteTravailEbu getPosteTravailEbu() {
        return posteTravail;
    }

    /**
     * @param nombreHeure the nombreHeure to set
     */
    public void setNombreHeure(double nombreHeure) {
        this.nombreHeure = nombreHeure;
    }

    /**
     * @param salaireHoraire the salaireHoraire to set
     */
    public void setSalaireHoraire(double salaireHoraire) {
        this.salaireHoraire = salaireHoraire;
    }

    /**
     * @param totalSalaire the totalSalaire to set
     */
    public void setTotalSalaire(double totalSalaire) {
        this.totalSalaire = totalSalaire;
    }

    public String getPeriodeDebut() {
        return periodeDebut;
    }

    public void setPeriodeDebut(String periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public String getPeriodeFin() {
        return periodeFin;
    }

    public void setPeriodeFin(String periodeFin) {
        this.periodeFin = periodeFin;
    }

    /**
     * @return the absences
     */
    public AbsencesEbu getAbsences() {
        return absences;
    }

    /**
     * @param remarque the remarque to set
     */
    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public String getLineCorrelationId() {
        return lineCorrelationId;
    }

    public void setLineCorrelationId(String lineCorrelationId) {
        this.lineCorrelationId = lineCorrelationId;
    }

}
