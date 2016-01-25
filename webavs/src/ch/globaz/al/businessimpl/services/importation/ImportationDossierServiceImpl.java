package ch.globaz.al.businessimpl.services.importation;

import globaz.framework.db.postit.FWNoteP;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import ch.globaz.al.business.constantes.ALCSCopie;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.exceptions.business.ALDossierBusinessException;
import ch.globaz.al.business.exceptions.model.dossier.ALCommentaireModelException;
import ch.globaz.al.business.exceptions.model.dossier.ALCopieComplexModelException;
import ch.globaz.al.business.exceptions.model.dossier.ALDossierComplexModelException;
import ch.globaz.al.business.exceptions.model.dossier.ALDossierModelException;
import ch.globaz.al.business.models.allocataire.AllocataireComplexModel;
import ch.globaz.al.business.models.allocataire.AllocataireComplexSearchModel;
import ch.globaz.al.business.models.allocataire.AllocataireModel;
import ch.globaz.al.business.models.dossier.CommentaireModel;
import ch.globaz.al.business.models.dossier.CommentaireSearchModel;
import ch.globaz.al.business.models.dossier.CopieComplexModel;
import ch.globaz.al.business.models.dossier.CopieModel;
import ch.globaz.al.business.models.dossier.CopieSearchModel;
import ch.globaz.al.business.models.dossier.DossierFkSearchModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.importation.ImportationDossierService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.pyxis.common.Messages;

/**
 * Implémentation du service d'importation des données de dossier
 * 
 * @author jts
 * 
 */
public class ImportationDossierServiceImpl extends ALAbstractBusinessServiceImpl implements ImportationDossierService {
    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierBusinessService#
     * importCommentaires(ch.globaz.al.business.models.dossier.DossierModel, java.util.ArrayList)
     */
    @Override
    public DossierModel importCommentaires(DossierModel dossier, ArrayList<CommentaireModel> commentaires)
            throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALDossierModelException("Unable to import model (dossierModel) - the model passed is null!");
        }
        if (commentaires.size() != 0) {
            for (int i = 0; i < commentaires.size(); i++) {
                if (!(commentaires.get(i) instanceof CommentaireModel)) {
                    throw new ALCommentaireModelException("The model is not a model CommentaireModel");
                }

                CommentaireModel commentaire = commentaires.get(i);

                // identifiant du dossier pour le commentaire
                commentaire.setIdDossier(dossier.getId());

                if (!JadeStringUtil.equals(commentaire.getType(), ALCSDossier.COMMENTAIRE_TYPE_DOSSIER, false)) {
                    // création du commentaire
                    // recherche du commentaire
                    CommentaireSearchModel cs = new CommentaireSearchModel();
                    cs.setForIdDossier(commentaire.getIdDossier());
                    cs.setForTypeCommentaire(ALCSDossier.COMMENTAIRE_TYPE_DECISION);

                    cs = ALImplServiceLocator.getCommentaireModelService().search(cs);
                    if (cs.getSize() == 0) {
                        commentaire = ALImplServiceLocator.getCommentaireModelService().create(commentaire);
                    }

                } else {

                    // création du commentaire de type dossier
                    importDossierCommentaire(commentaire);
                }
            }
        }
        return dossier;

    }

    @Override
    public DossierModel importCopiesAvisEcheances(DossierModel dossier)
            throws JadeApplicationServiceNotAvailableException, JadeApplicationException, JadePersistenceException {

        // recherche de l'identifiant du tiers alloacataire
        AllocataireModel allocataire = new AllocataireModel();
        allocataire = ALImplServiceLocator.getAllocataireModelService().read(dossier.getIdAllocataire());
        String idTiersAllocataire = allocataire.getIdTiersAllocataire();

        CopieModel copieAvisEcheance = new CopieModel();
        copieAvisEcheance.setIdDossier(dossier.getIdDossier());
        // pour importation original par défaut pour le tiers Allocataire
        copieAvisEcheance.setIdTiersDestinataire(idTiersAllocataire);
        // type de copie à Echeance
        copieAvisEcheance.setTypeCopie(ALCSCopie.TYPE_ECHEANCE);
        // Copie Originale (priorité 1)
        copieAvisEcheance.setOrdreCopie("1");

        // recherche de la copie par id Dossier, priorite et type de
        // copie
        CopieSearchModel cs = new CopieSearchModel();

        cs.setForIdDossier(copieAvisEcheance.getIdDossier());
        cs.setForOrdreCopie(copieAvisEcheance.getOrdreCopie());
        cs.setForTypeCopie(copieAvisEcheance.getTypeCopie());
        cs.setForIdTiersDestinataire(copieAvisEcheance.getIdTiersDestinataire());

        cs = ALServiceLocator.getCopieModelService().search(cs);

        if (cs.getSize() == 0) {
            copieAvisEcheance.setImpressionBatch(false);
            copieAvisEcheance = ALServiceLocator.getCopieModelService().create(copieAvisEcheance);
        }

        return dossier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierBusinessService#
     * importCopies(ch.globaz.al.business.models.dossier.DossierModel, java.util.ArrayList)
     */
    @Override
    public DossierModel importCopiesDecision(DossierModel dossier, ArrayList<CopieComplexModel> copies)
            throws JadeApplicationException, JadePersistenceException {

        if (dossier == null) {
            throw new ALDossierModelException("Unable to import model (dossierModel) - the model passed is null!");
        }
        if (copies.size() != 0) {
            for (int i = 0; i < copies.size(); i++) {
                if (!(copies.get(i) instanceof CopieComplexModel)) {
                    throw new ALCopieComplexModelException("The model is not a model CopieComplexModel");
                }

                CopieComplexModel copieImporte = copies.get(i);

                AllocataireComplexSearchModel allocSearch = new AllocataireComplexSearchModel();
                allocSearch.setForIdAllocataire(dossier.getIdAllocataire());
                ALServiceLocator.getAllocataireComplexModelService().search(allocSearch);
                AllocataireComplexModel allocComplex = new AllocataireComplexModel();

                if ((allocSearch.getSize() == 0) || (allocSearch.getSize() > 1)) {
                    throw new ALDossierBusinessException(
                            "DossierBusinessService#importCopie: allocSearch: the result of the search is nul or >1");

                }
                if (allocSearch.getSize() == 1) {
                    allocComplex = (AllocataireComplexModel) allocSearch.getSearchResults()[0];
                }

                // affecter la priorite de décision pour un salarié à paiement
                // indirect
                if (JadeStringUtil.isBlankOrZero(dossier.getIdTiersBeneficiaire())) {
                    if (dossier.getActiviteAllocataire().equals(ALCSDossier.ACTIVITE_SALARIE)
                            || dossier.getActiviteAllocataire().equals(ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE)
                            || dossier.getActiviteAllocataire().equals(ALCSDossier.ACTIVITE_NONACTIF)) {
                        if (allocComplex.getAllocataireModel().getIdTiersAllocataire()
                                .equals(copieImporte.getCopieModel().getIdTiersDestinataire())) {
                            copieImporte.getCopieModel().setOrdreCopie("2");
                        } else {
                            copieImporte.getCopieModel().setOrdreCopie("1");
                        }
                    } else if ((dossier.getActiviteAllocataire().equals(ALCSDossier.ACTIVITE_INDEPENDANT)
                            || dossier.getActiviteAllocataire().equals(ALCSDossier.ACTIVITE_TSE)
                            || dossier.getActiviteAllocataire().equals(ALCSDossier.ACTIVITE_AGRICULTEUR)
                            || dossier.getActiviteAllocataire().equals(ALCSDossier.ACTIVITE_COLLAB_AGRICOLE) || dossier
                            .getActiviteAllocataire().equals(ALCSDossier.ACTIVITE_PECHEUR))) {

                        copieImporte.getCopieModel().setOrdreCopie("1");
                    }
                }

                // affecter la priorité de décision pour un salarié à paiement
                // direct

                else if (!dossier.getIdTiersBeneficiaire().equals("0")) {
                    if (dossier.getActiviteAllocataire().equals(ALCSDossier.ACTIVITE_SALARIE)
                            || dossier.getActiviteAllocataire().equals(ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE)

                            || dossier.getActiviteAllocataire().equals(ALCSDossier.ACTIVITE_NONACTIF)) {
                        if (allocComplex.getAllocataireModel().getIdTiersAllocataire()
                                .equals(copieImporte.getCopieModel().getIdTiersDestinataire())) {
                            copieImporte.getCopieModel().setOrdreCopie("1");
                        } else {
                            copieImporte.getCopieModel().setOrdreCopie("2");
                        }
                    } else if ((dossier.getActiviteAllocataire().equals(ALCSDossier.ACTIVITE_INDEPENDANT)
                            || dossier.getActiviteAllocataire().equals(ALCSDossier.ACTIVITE_TSE)
                            || dossier.getActiviteAllocataire().equals(ALCSDossier.ACTIVITE_AGRICULTEUR)
                            || dossier.getActiviteAllocataire().equals(ALCSDossier.ACTIVITE_COLLAB_AGRICOLE) || dossier
                            .getActiviteAllocataire().equals(ALCSDossier.ACTIVITE_PECHEUR))) {

                        copieImporte.getCopieModel().setOrdreCopie("1");
                    }
                }

                // recherche de la copie par id Dossier, priorite et type de
                // copie, type échéance, tiers destinataire
                CopieSearchModel cs = new CopieSearchModel();

                cs.setForIdDossier(copieImporte.getCopieModel().getIdDossier());
                cs.setForOrdreCopie(copieImporte.getCopieModel().getOrdreCopie());
                cs.setForTypeCopie(copieImporte.getCopieModel().getTypeCopie());
                cs.setForIdTiersDestinataire(copieImporte.getCopieModel().getIdTiersDestinataire());

                cs = ALServiceLocator.getCopieModelService().search(cs);

                if (cs.getSize() == 0) {

                    // création de la copie

                    copieImporte = ALServiceLocator.getCopieComplexModelService().create(copieImporte);

                }

            }
        }
        return dossier;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierBusinessService#
     * importDossier(ch.globaz.al.business.models.dossier.DossierModel, java.lang.String, java.lang.String)
     */
    @Override
    public DossierModel importDossier(DossierModel dossierImport, String idAllocataire, String idDossier)
            throws JadeApplicationException, JadePersistenceException {

        // recherche d'un dossier par son identifiant de dossier
        DossierFkSearchModel ds = new DossierFkSearchModel();
        ds.setForIdDossier(dossierImport.getIdDossier());

        ALImplServiceLocator.getDossierFkModelService().search(ds);

        // en fonction du résultat de la recherche effectue:
        if (ds.getSize() >= 1) {
            throw new ALDossierComplexModelException("Un dossier correspondant à ce numéro existe déjà : " + idDossier);
        } else {
            dossierImport.setIdAllocataire(idAllocataire);

            // TODO (lot 2) affecter la catégorie professionnelle
            dossierImport.setCategorieProf("catProf");

            dossierImport = ALServiceLocator.getDossierModelService().create(dossierImport);
        }

        return dossierImport;
    }

    /**
     * Ajout un commentaire dans la table FWNOTEP, utilisé par le postit.
     * 
     * @param commentaire
     *            le modèle de commentaire à ajouter
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private void importDossierCommentaire(CommentaireModel commentaire) throws JadePersistenceException {

        BSpy spy = new BSpy(commentaire.getCreationSpy());
        FWNoteP noteComm = new FWNoteP();
        noteComm.setDateCreation(spy.getDate());
        noteComm.setDescription(spy.getDate() + "-reprise");
        noteComm.setSourceId(commentaire.getIdDossier());
        noteComm.setMemo(commentaire.getTexte());
        noteComm.setTableSource("globaz.al.vb.dossier.ALDossierViewBean");
        noteComm.setUser(spy.getUser());

        try {
            noteComm.add();
        } catch (Exception e) {
            throw new JadePersistenceException(Messages.TECHNICAL, e);
        }

    }
}