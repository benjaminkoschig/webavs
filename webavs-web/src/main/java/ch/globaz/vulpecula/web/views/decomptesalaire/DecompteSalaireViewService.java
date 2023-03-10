package ch.globaz.vulpecula.web.views.decomptesalaire;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.net.URISyntaxException;
import javax.xml.bind.JAXBException;
import org.apache.commons.lang.Validate;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.decompte.DecompteSalaireService;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.CodeErreur;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.parametrage.TableParametrage;
import ch.globaz.vulpecula.services.PTAFServices;
import com.google.common.base.Preconditions;

public class DecompteSalaireViewService {
    private DecompteSalaireService decompteSalaireService = VulpeculaServiceLocator.getDecompteSalaireService();

    public DecompteSalaireView findPrecedentComptabilise(String idPosteTravail, String dateRecherche)
            throws JAXBException, URISyntaxException {
        if (!Date.isValid(dateRecherche)) {
            throw new IllegalArgumentException("La date de recherche : " + dateRecherche + " n'est pas valide");
        }

        DecompteSalaire decompteSalaire = decompteSalaireService.findPrecedentValide(idPosteTravail, new Date(
                dateRecherche));

        if (decompteSalaire != null) {
            DecompteSalaireView decompteSalaireView = new DecompteSalaireView();
            decompteSalaireView.setId(decompteSalaire.getId());

            Periode periode = decompteSalaire.getPeriode();
            Date dateDebut = periode.getDateDebut();
            Date dateFin = periode.getDateFin();

            decompteSalaireView.setPeriodeDebut(dateDebut.getMois() + "." + dateDebut.getAnnee());
            decompteSalaireView.setPeriodeFin(dateFin.getMois() + "." + dateFin.getAnnee());

            // Si le salaire horaire n'est pas d?finit, on le calcule.
            if (decompteSalaire.getSalaireHoraire().isZero()) {
                int noCaisseMetier = VulpeculaServiceLocator.getPosteTravailService().getNumeroCaissePrincipale(
                        idPosteTravail);
                double nombreHeures = TableParametrage.getInstance().getHeuresTravailMois(noCaisseMetier);

                if (nombreHeures == 0.0) {
                    throw new IllegalStateException("Le nombre d'heures pour la caisse m?tier " + noCaisseMetier
                            + " n'est pas configur? !");
                }

                Montant salaireHoraire = decompteSalaire.getSalaireTotal();
                salaireHoraire = salaireHoraire.divide(nombreHeures);
                decompteSalaireView.setSalaireHoraire(String.valueOf(salaireHoraire.getBigDecimalArrondi()));
            } else {
                decompteSalaireView.setSalaireHoraire(decompteSalaire.getSalaireHoraireAsValue());
            }

            return decompteSalaireView;
        }
        return new DecompteSalaireView();
    }

    public String deleteCotisationDecompte(String idDecompteSalaire, String idCotisationDecompte) {
        Taux nouveauTaux = VulpeculaServiceLocator.getCotisationDecompteService().deleteCotisationDecompte(
                idDecompteSalaire, idCotisationDecompte);
        return nouveauTaux.getValue();
    }

    public String checkCotisationDecompte(String idDecompteSalaire, String annee) {
        Validate.notEmpty(idDecompteSalaire);
        if (!JadeNumericUtil.isEmptyOrZero(annee) && !"null".equals(annee)) {
            return VulpeculaServiceLocator.getDecompteSalaireService()
                    .checkCotisationDecompte(idDecompteSalaire, new Annee(annee)).getRaison();
        } else {
            return VulpeculaServiceLocator.getDecompteSalaireService().checkCotisationDecompte(idDecompteSalaire, null)
                    .getRaison();
        }
    }

    public String cumulSalaires(String idTravailleur, String dateDebut, String dateFin) {
        return VulpeculaServiceLocator.getDecompteSalaireService()
                .cumulSalaire(idTravailleur, new Date(dateDebut), new Date(dateFin)).toStringFormatTwoDecimales();
    }

    public String cumulSalairesSansDateFin(String idTravailleur, String dateDebut) {
        return VulpeculaServiceLocator.getDecompteSalaireService()
                .cumulSalaire(idTravailleur, new Date(dateDebut), null).toStringFormatTwoDecimales();
    }

    public String cumulSalairesSyndicatWithCotisationsCPR(String idTravailleur, String dateDebut, String dateFin) {
        return VulpeculaServiceLocator.getDecompteSalaireService()
                .cumulSalaireSyndicatWithCotisationsCPR(idTravailleur, new Date(dateDebut), new Date(dateFin))
                .toStringFormatTwoDecimales();
    }

    public String cumulSalairesSyndicatWithCotisationsCPRSansDateFin(String idTravailleur, String dateDebut) {
        return VulpeculaServiceLocator.getDecompteSalaireService()
                .cumulSalaireSyndicatWithCotisationsCPR(idTravailleur, new Date(dateDebut), null)
                .toStringFormatTwoDecimales();
    }

    /**
     * Retourne si le tiers ? un droit actif ou non dans les AF ? la date pass? en param?tre.
     * 
     * @param idTiers Id tiers du tiers poss?dant potentiellement un droit dans les AF
     * @param stringDate String au format suisse (dd.MM.yyyy) d?terminant ? quelle date le droit doit ?tre actif
     * @return true si poss?de un droit, false dans le cas contraire
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public boolean hasDroitActifAF(String idTiers, String stringDate) throws JadeApplicationException,
            JadePersistenceException {
        Preconditions.checkArgument(!JadeNumericUtil.isEmptyOrZero(stringDate));
        Preconditions.checkArgument(!JadeNumericUtil.isEmptyOrZero(idTiers));

        if (!JadeNumericUtil.isIntegerPositif(idTiers)) {
            return false;
        }

        if (PTAFServices.hasDroitsActifs(stringDate, idTiers)) {
            return true;
        }
        return false;
    }

    public String loadCodesErreur(String code, String taux) {
        BSession session = BSessionUtil.getSessionFromThreadContext();
        String libelle = session.getCodeLibelle(code);
        if (code.equals(CodeErreur.TAUX_DIFFERENT.getValue())) {
            libelle += " (" + taux + ")";
        }
        return libelle;
    }
}
