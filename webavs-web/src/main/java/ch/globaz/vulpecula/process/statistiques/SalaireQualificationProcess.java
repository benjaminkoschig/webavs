package ch.globaz.vulpecula.process.statistiques;

import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import ch.globaz.utils.Pair;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.DetailGroupeLocalite;
import ch.globaz.vulpecula.domain.models.common.DetailGroupeLocalites;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.external.BProcessWithContext;
import com.google.common.base.Function;
import com.google.common.collect.Multimaps;

public class SalaireQualificationProcess extends BProcessWithContext {
    private static final long serialVersionUID = 3606653891930651187L;

    private String periodeDebut;
    private String periodeFin;
    private String idConvention;
    private List<String> codesQualifications;

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();

        String convention = VulpeculaRepositoryLocator.getConventionRepository().findById(idConvention)
                .getDesignation();

        SalaireQualificationExcel statExcel = new SalaireQualificationExcel(retrieve(),
                new Date(periodeDebut).getFirstDayOfMonth(), new Date(periodeFin).getLastDayOfMonth(), convention,
                getSession(), DocumentConstants.SALAIRE_QUALIFICATION_NAME,
                DocumentConstants.SALAIRE_QUALIFICATION_DOC_NAME);

        statExcel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), statExcel.getOutputFile());
        return true;
    }

    /**
     * Méthode qui retourne une liste d'EntreeSalaireQualification
     * Une EntreeSalaireQualification est une classe qui regroupe toute les informations à afficher pour une ligne dans
     * le
     * fichier Excel de sortie
     * 
     * @return une liste d'EntreeSalaireQualification
     */
    public List<EntreeSalaireQualification> retrieve() {
        List<EntreeSalaireQualification> entrees = new ArrayList<EntreeSalaireQualification>();

        Date dateDebut = new Date(periodeDebut).getFirstDayOfMonth();
        Date dateFin = new Date(periodeFin).getLastDayOfMonth();

        Map<Pair<String, Qualification>, Collection<PosteTravail>> map = findAndGroupPostesTravails(dateDebut, dateFin);

        for (Map.Entry<Pair<String, Qualification>, Collection<PosteTravail>> entree : map.entrySet()) {
            Montant moyenneSalaireHoraire = Montant.ZERO;
            List<PosteTravail> postes = new ArrayList<PosteTravail>();

            for (PosteTravail poste : entree.getValue()) {

                Montant moyenneSalairePoste = Montant.ZERO;
                List<DecompteSalaire> ds = findListeDecomptes(dateDebut, dateFin, poste.getId());
                // Liste de décomptes salaires qui sont compté dans les statistiques
                List<DecompteSalaire> decompteComptes = new ArrayList<DecompteSalaire>();
                if (ds.size() > 0) {
                    for (DecompteSalaire decompte : ds) {
                        if (decompte.getAbsences().size() == 0) {
                            moyenneSalairePoste = moyenneSalairePoste.add(decompte.getSalaireHoraire());
                            decompteComptes.add(decompte);
                        }
                    }
                    moyenneSalairePoste = moyenneSalairePoste.divide(decompteComptes.size());
                }
                if (!moyenneSalairePoste.isZero()) {
                    moyenneSalaireHoraire = moyenneSalaireHoraire.add(moyenneSalairePoste);
                    postes.add(poste);
                }
            }
            if (!postes.isEmpty()) {
                moyenneSalaireHoraire = moyenneSalaireHoraire.divide(postes.size());
            }

            if (!moyenneSalaireHoraire.isZero()) {
                entrees.add(new EntreeSalaireQualification(entree.getKey(), postes, moyenneSalaireHoraire));
            }
        }

        return entrees;
    }

    Map<Pair<String, Qualification>, Collection<PosteTravail>> findAndGroupPostesTravails(Date dateDebut, Date dateFin) {
        List<PosteTravail> postesTravails = VulpeculaRepositoryLocator.getPosteTravailRepository()
                .findPosteActifByConventionAndQualification(dateDebut, dateFin, idConvention,
                        mapToQualifications(codesQualifications));

        postesTravails = findInfosPosteTravail(postesTravails);

        return groupeByRegionAndQualification(postesTravails);
    }

    /**
     * Méthode définissant l'adresse aux poste de travail contenus dans la liste en paramètre
     * 
     * @param postesTravails Liste de poste de travail
     * @return une liste de poste de travail
     */
    private List<PosteTravail> findInfosPosteTravail(List<PosteTravail> postesTravails) {
        List<PosteTravail> postes = postesTravails;

        for (PosteTravail poste : postes) {
            if (poste != null) {
                poste.getEmployeur().setAdressePrincipale(
                        VulpeculaRepositoryLocator.getAdresseRepository().findAdresseDomicileByIdTiers(
                                poste.getEmployeur().getIdTiers()));
            } else {
                postes.remove(poste);
            }
        }

        return postes;
    }

    /**
     * Méthode créant et retournant une List de Qualifications selon les codesQualification.
     * 
     * @return une List de Qualifications
     */
    List<Qualification> mapToQualifications(List<String> codesQualifications) {
        List<Qualification> qualifications = new ArrayList<Qualification>();
        for (String qualification : codesQualifications) {
            qualifications.add(Qualification.fromValue(qualification));
        }
        return qualifications;
    }

    /**
     * Méthode retournant tous les postes de travail groupé par region et qualification
     * 
     * @param posteTravails Postes de travail à grouper par Pair<Region, Qualif>
     * @return une Map ou la paire clé - valeur est la suivante : Pair<Region,Qualif> - Liste de tous les postes de
     *         travails
     *         pour la période, la convention et les qualifications données
     */
    Map<Pair<String, Qualification>, Collection<PosteTravail>> groupeByRegionAndQualification(
            List<PosteTravail> posteTravails) {

        return Multimaps.index(posteTravails, new Function<PosteTravail, Pair<String, Qualification>>() {

            @Override
            public Pair<String, Qualification> apply(PosteTravail poste) {
                return findPairRegionQualif(poste);
            }
        }).asMap();
    }

    /**
     * Méthode retournant la pair Region, Qualification selon le poste passé en paramètre
     * 
     * @param poste poste sur lequel les infos de la Pair vont être cherchés
     * @return une Pair de région, qualification
     */
    Pair<String, Qualification> findPairRegionQualif(PosteTravail poste) {
        DetailGroupeLocalite detailGroupeLocalite = findDetailGroupeLocalites(poste).getRegion();
        String region = "Autre";

        if (detailGroupeLocalite != null) {
            region = detailGroupeLocalite.getNomGroupeFR();
        }

        return new Pair<String, Qualification>(region, poste.getQualification());
    }

    /**
     * Méthode qui retourne le DetailGroupeLocalite de l'id loc passé en paramètres
     * 
     * @return un DetailGroupeLocalite
     */
    DetailGroupeLocalites findDetailGroupeLocalites(PosteTravail poste) {
        DetailGroupeLocalites detailGroupeLocalites = VulpeculaRepositoryLocator.getDetailGroupeLocaliteRepository()
                .findByIdLocalite(poste.getIdLocaliteEmployeur());
        return detailGroupeLocalites;
    }

    /**
     * Méthode retournant une liste de décompte salaire selon une periode(dateDebut et dateFin)
     * 
     * @param dateDebut année de début de la période
     * @param dateFin année de fin de la période
     * @return une liste de décompte salaire
     */
    private List<DecompteSalaire> findListeDecomptes(Date dateDebut, Date dateFin, String idPoste) {

        List<DecompteSalaire> listeDecomptes = VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                .findByIdPosteTravail(Arrays.asList(idPoste), dateDebut, dateFin);

        return listeDecomptes;
    }

    public String getIdConvention() {
        return idConvention;
    }

    public void setIdConvention(String idConvention) {
        this.idConvention = idConvention;
    }

    public List<String> getCodesQualifications() {
        return codesQualifications;
    }

    public void setCodesQualifications(List<String> codesQualifications) {
        this.codesQualifications = codesQualifications;
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

    @Override
    protected String getEMailObject() {
        return DocumentConstants.SALAIRE_QUALIFICATION_NAME;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }
}
