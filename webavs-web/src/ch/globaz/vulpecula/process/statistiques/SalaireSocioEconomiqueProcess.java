package ch.globaz.vulpecula.process.statistiques;

import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.DetailGroupeLocalites;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.external.BProcessWithContext;
import ch.globaz.vulpecula.external.models.affiliation.Adhesion;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;
import com.google.common.base.Function;
import com.google.common.collect.Multimaps;

public class SalaireSocioEconomiqueProcess extends BProcessWithContext {
    private static final long serialVersionUID = -2535219544179557976L;

    private String periodeDebut;
    private String periodeFin;

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        SalaireSocioEconomiqueExcel statExcel = new SalaireSocioEconomiqueExcel(retrieve(), getSession(),
                DocumentConstants.SALAIRE_SOCIO_ECONOMIQUE_NAME, DocumentConstants.SALAIRE_SOCIO_ECONOMIQUE_DOC_NAME);
        statExcel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), statExcel.getOutputFile());
        return true;
    }

    /**
     * M�thode qui retourne une liste d'EntreeSalaireSocioEconomique
     * Une EntreeSalaireSocioEconomique est une classe qui regroupe toute les informations � afficher pour une ligne
     * dans le
     * fichier Excel de sortie
     * 
     * @return une liste d'EntreeSalaireSocioEconomique
     */
    public List<EntreeSalaireSocioEconomique> retrieve() {
        Date dateDebut = new Date(periodeDebut).getFirstDayOfMonth();
        Date dateFin = new Date(periodeFin).getLastDayOfMonth();

        List<EntreeSalaireSocioEconomique> entrees = new ArrayList<EntreeSalaireSocioEconomique>();

        List<Employeur> employeurs = VulpeculaServiceLocator.getEmployeurService().findEmployeurActif(dateDebut,
                dateFin);

        for (Employeur employeur : employeurs) {
            // On v�rifie si on a le droit de lister les employeurs d'apr�s le secure code.
            if (!VulpeculaServiceLocator.getUsersService().hasRightAccesSecurity(employeur.getAccesSecurite())) {
                continue;
            }
            findInfosEmployeur(employeur, dateDebut, dateFin);
            Adhesion caisseMetier = findCaisseMetier(employeur);
            DetailGroupeLocalites detailGroupeLocalites = findDetailGroupeLocalites(employeur);

            Map<PosteTravail, Collection<DecompteSalaire>> decomptesGroupByPoste = findAndGroupByPosteTravail(
                    employeur, dateDebut, dateFin);
            entrees.add(new EntreeSalaireSocioEconomique(employeur, detailGroupeLocalites, caisseMetier,
                    decomptesGroupByPoste));

        }
        return entrees;
    }

    Map<PosteTravail, Collection<DecompteSalaire>> findAndGroupByPosteTravail(Employeur employeur, Date dateDebut,
            Date dateFin) {
        Collection<String> idsPostes = getIdsPostes(employeur.getPostesTravail());
        List<DecompteSalaire> listeDecompte = findListeDecomptes(idsPostes, dateDebut, dateFin);
        return groupLignesDecomptesByPosteTravail(listeDecompte);
    }

    /**
     * M�thode qui retourne le DetailGroupeLocalite de l'employeur pass� en param�tres
     * 
     * @param employeur Employeur pour lequel doit �tre cherch� son DetailGroupeLocalite
     * @return un DetailGroupeLocalite
     */
    private DetailGroupeLocalites findDetailGroupeLocalites(Employeur employeur) {
        DetailGroupeLocalites detailGroupeLocalites = VulpeculaRepositoryLocator.getDetailGroupeLocaliteRepository()
                .findByIdLocalite(employeur.getAdressePrincipale().getIdLocalite());
        return detailGroupeLocalites;
    }

    /**
     * M�thode cherchant la caisseM�tier de l'employeur pass� en param�tre
     * 
     * @param employeur Employeur pour lequel doit �tre cherch� la caisseM�tier
     * @return une Adhesion contenant la caisseM�tier
     */
    private Adhesion findCaisseMetier(Employeur employeur) {
        return VulpeculaRepositoryLocator.getAdhesionRepository().findCaisseMetier(employeur.getId());
    }

    /**
     * M�thode d�finissant les posteTravails et l'adresse � un employeur
     * 
     * @param employeur Employeur auquel doivent �tre d�fini les poste et l'adresse
     * @param dateDebut D�but de la p�riode d'activit� pour les postes
     * @param dateFin Fin de la p�riode d'activit� pour les postes
     */
    private void findInfosEmployeur(Employeur employeur, Date dateDebut, Date dateFin) {
        List<PosteTravail> listePostes = VulpeculaRepositoryLocator.getPosteTravailRepository().findPosteActif(
                employeur.getId(), dateDebut, dateFin);
        Adresse adresse = VulpeculaRepositoryLocator.getAdresseRepository().findAdresseDomicileByIdTiers(
                employeur.getIdTiers());
        employeur.setPostesTravail(listePostes);
        employeur.setAdressePrincipale(adresse);
    }

    /**
     * M�thode retournant tous les d�comptes salaire group� par poste de travail
     * 
     * @param listeDecomptes Liste de d�compte � grouper par Poste de travail
     * @return une Map ou la paire cl� - valeur est la suivante : Poste de travail - Liste de tous ses d�comptes salaire
     *         pour la p�riode donn�e
     */
    Map<PosteTravail, Collection<DecompteSalaire>> groupLignesDecomptesByPosteTravail(
            List<DecompteSalaire> listeDecomptes) {
        return Multimaps.index(listeDecomptes, new Function<DecompteSalaire, PosteTravail>() {
            @Override
            public PosteTravail apply(DecompteSalaire ds) {
                return ds.getPosteTravail();
            }
        }).asMap();
    }

    /**
     * M�thode retournant une liste de d�compte salaire selon une liste d'id poste de travail et une periode(dateDebut
     * et dateFin)
     * 
     * @param idsPostes Liste d'id poste de travail en entr�e
     * @param dateDebut ann�e de d�but de la p�riode
     * @param dateFin ann�e de fin de la p�riode
     * @return une liste de d�compte salaire
     */
    List<DecompteSalaire> findListeDecomptes(Collection<String> idsPostes, Date dateDebut, Date dateFin) {

        List<DecompteSalaire> listeDecomptes = VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                .findByIdPosteTravail(idsPostes, dateDebut, dateFin);

        return listeDecomptes;

    }

    /**
     * M�thode retournant une Collection d'id poste de travail selon une collection de postes de travail pass� en
     * param�tre
     * 
     * @param postes Liste de postes de travail en entr�e
     * @return une Collection d'id poste de travail
     */
    Collection<String> getIdsPostes(Collection<PosteTravail> postes) {
        final Collection<String> idsPostes = new ArrayList<String>();
        for (PosteTravail poste : postes) {
            idsPostes.add(poste.getId());
        }

        return idsPostes;
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
        return DocumentConstants.SALAIRE_SOCIO_ECONOMIQUE_NAME;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }
}
