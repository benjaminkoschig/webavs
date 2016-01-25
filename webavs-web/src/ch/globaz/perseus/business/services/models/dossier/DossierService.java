package ch.globaz.perseus.business.services.models.dossier;

import globaz.framework.util.FWCurrency;
import globaz.globall.util.JAException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.perseus.business.exceptions.models.dossier.DossierException;
import ch.globaz.perseus.business.models.dossier.Dossier;
import ch.globaz.perseus.business.models.dossier.DossierSearchModel;
import ch.globaz.perseus.business.models.qd.QD;
import ch.globaz.perseus.business.models.qd.QDSearchModel;
import ch.globaz.perseus.business.models.situationfamille.MembreFamille;

public interface DossierService extends JadeApplicationService {

    /**
     * Permet de calculer le montant qui a �t� vers� pour un dossier pour une p�riode donn�e (mm.yyyy -> mm.yyyy)
     * Uniquement les montants vers�es pour la mesure de coaching
     * 
     * @param dossier
     *            Le dossier conern�
     * @param dateDebut
     *            Mois de d�but
     * @param dateFin
     *            Mois de fin
     * @return Le montant total vers� pour les mesures de coaching (d�cisions et paiements mensuels)
     * @throws JadePersistenceException
     * @throws DossierException
     */
    public Float calculerMontantMesureCoaching(Dossier dossier, String dateDebut, String dateFin)
            throws JadePersistenceException, DossierException;

    /**
     * Permet de calculer le montant qui a �t� vers� pour un dossier pour une p�riode donn�e (mm.yyyy -> mm.yyyy)
     * 
     * @param dossier
     *            Le dossier conern�
     * @param dateDebut
     *            Mois de d�but
     * @param dateFin
     *            Mois de fin
     * @return Le montant total vers� (d�cisions et paiements mensuels)
     * @throws JadePersistenceException
     * @throws DossierException
     */
    public Float calculerMontantVerse(Dossier dossier, String dateDebut, String dateFin)
            throws JadePersistenceException, DossierException;

    public FWCurrency calculerMontantVerseAttestationPCF(Dossier dossier, String anneeAttestation)
            throws JadePersistenceException, DossierException, JAException;

    public FWCurrency calculerMontantVerseAttestationRP(Dossier dossier, String anneeAttestation)
            throws JadePersistenceException, DossierException, JAException;

    public Float calculerMontantVerseImpotSource(Dossier dossier, String dateDebut, String dateFin)
            throws JadePersistenceException, DossierException;

    /**
     * Permet de calculer le montant qui a �t� vers� pour un dossier pour une ann�e (ou jusqu'au dernier pmt mensuel si
     * l'ann�e est en cours)
     * 
     * @param dossier
     *            Le dossier conern�
     * @return Le montant total vers� (d�cisions et paiements mensuels)
     * @throws JadePersistenceException
     * @throws DossierException
     * @throws JAException
     */
    public FWCurrency calculerMontantVerseStatsOFS(Dossier dossier, String annee) throws JadePersistenceException,
            DossierException, JAException;

    public int count(DossierSearchModel search) throws DossierException, JadePersistenceException;

    public Dossier create(Dossier dossier) throws JadePersistenceException, DossierException;

    public Dossier delete(Dossier dossier) throws JadePersistenceException, DossierException;

    /**
     * Service permettant de r�cup�rer la somme des impots � la source vers�s pour une requr�atn
     * 
     * @param dossier
     * @param dateDebut
     *            Format DD.MM.YYYY
     * @param dateFin
     *            Format DD.MM.YYYY
     * @return
     * @throws JadePersistenceException
     * @throws DossierException
     */
    public String findImpotSource(Dossier dossier, String dateDebut, String dateFin) throws JadePersistenceException,
            DossierException;

    public List<MembreFamille> getListAllMembresFamille(String idDossier) throws JadePersistenceException,
            DossierException;

    public List<MembreFamille> getListAllMembresFamilleRentePont(String idDossier) throws JadePersistenceException,
            DossierException;

    /**
     * Permet de retrouver les QD ouvertes pour un dossier � une date pr�cise
     * 
     * @param idDossier
     * @param dateFacture
     * @param dateReception
     * @param csTypeQD
     * @return liste de MembreFamille
     * @throws JadePersistenceException
     * @throws DossierException
     */
    public List<QD> getListQD(String idDossier, String dateFacture, String dateReception, String csTypeQD)
            throws JadePersistenceException, DossierException;

    /**
     * Finte pour avoir un search model comme entr� et retour (utilisation de getListQD(String, sTinr...,.j) Est utile
     * pour le widget lister
     * 
     * @param searchModel
     * @return
     * @throws JadePersistenceException
     * @throws DossierException
     */
    public QDSearchModel getListQDSearch(QDSearchModel searchModel) throws JadePersistenceException, DossierException;

    public List<QD> getListQDSearchByParameters(String idDossier, String dateFacture, String dateReception,
            String csTypeQD, String datePriseEnCharge) throws JadePersistenceException, DossierException;

    /**
     * UTILISE POUR L'AJAX ! RENVOIE UN STRING ! TODO Corriger �a pour la version finale Teste si un tiers a un dossier.
     * 
     * @param idTiers
     * @return
     * @throws JadePersistenceException
     * @throws DossierException
     */
    public Boolean hasDossier(String idTiers) throws JadePersistenceException, DossierException;

    public Dossier read(String idDossier) throws JadePersistenceException, DossierException;

    public DossierSearchModel search(DossierSearchModel searchModel) throws JadePersistenceException, DossierException;

    /**
     * Retourne une liste de dossiers o� le membre famille est pr�sent. Si rentePont � true retourne les dossiers rentes
     * ponts si non les dossiers pc familles
     * 
     * @param idTiers
     * @param rentePont
     * @return
     * @throws DossierException
     * @throws JadePersistenceException
     */
    public List<Dossier> searchDossierForTiers(String idTiers, boolean rentePont) throws DossierException,
            JadePersistenceException;

    public Dossier update(Dossier dossier) throws JadePersistenceException, DossierException;

}
