package globaz.aquila.db.irrecouvrables;

import globaz.aquila.vb.COAbstractViewBeanSupport;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.osiris.db.irrecouvrable.CARecouvrementBaseAmortissementContainer;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CORecouvrementSelectionMontantViewBean extends COAbstractViewBeanSupport {

    private static final long serialVersionUID = -7252047021137374068L;
    /** ann�e utilis�e si la base d'amortissement n'est pas disponible */
    private String annee;
    /** Bases d'amortissement disponibles */
    private CARecouvrementBaseAmortissementContainer basesAmortissement;
    /** id du compte annexe � traiter */
    private String idCompteAnnexe;
    /** liste des sections � traiter */
    private List<String> idSectionsList = new ArrayList<String>();
    /**
     * Montant � recouvrir indiqu� par l'utilisateur Il peut �tre �gal ou inf�rieur au montant � disponible pour le
     * recouvrement
     */
    private String montantARecouvrir;
    /** Montant disponible pour le recouvrement */
    private String montantDisponible;
    /** Base d'amortissement s�lectionn�es */
    private List<String> selectedBasesAmortissement = new ArrayList<String>();

    /**
     * Retourne l'ann�e � utiliser si la base d'amortissement n'est pas disponible
     * 
     * @return l'ann�e � utiliser
     */
    public String getAnnee() {
        return annee;
    }

    public boolean isBaseSelected(Integer annee) {
        return selectedBasesAmortissement.contains(String.valueOf(annee));
    }

    /**
     * Retourne les bases d'amortissement
     * 
     * @return liste des bases d'amortissement
     */
    public CARecouvrementBaseAmortissementContainer getBasesAmortissement() {
        return basesAmortissement;
    }

    /**
     * Retourne l'id du compte annexe � traiter
     * 
     * @return l'id du compte annexe
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * Retourne la liste des sections � traiter
     * 
     * @return liste des sections � traiter
     */
    public List<String> getIdSectionsList() {
        return idSectionsList;
    }

    /**
     * Retourne le montant � recouvrir. Il peut �tre �gal ou inf�rieur au montant � disponible pour le recouvrement
     * 
     * @return montant � recouvrir
     * @see getMontantDisponible
     */
    public String getMontantARecouvrir() {
        return montantARecouvrir;
    }

    /**
     * Retourne le montant disponible pour le recouvrement
     * 
     * @return montant disponible
     */
    public String getMontantDisponible() {
        return montantDisponible;
    }

    /**
     * Retourne la liste des bases d'amortissement s�lectionn�es
     * 
     * @return Base d'amortissement s�lectionn�es
     */
    public List<String> getSelectedBasesAmortissement() {
        return selectedBasesAmortissement;
    }

    /**
     * Retourne la liste des id de section sous la forme d'un cha�ne dans laquelle les id sont s�par�e par une ','
     * 
     * @return chaine contenant la liste des id de section
     */
    public String getStringIdSectionsList() {
        StringBuilder stringIdSectionsList = new StringBuilder();

        for (int i = 0; i < getIdSectionsList().size(); i++) {

            stringIdSectionsList.append(getIdSectionsList().get(i));

            if (i != (getIdSectionsList().size() - 1)) {
                stringIdSectionsList.append(",");
            }
        }

        return stringIdSectionsList.toString();
    }

    private boolean isAnneeInValidRange() {
        if (!JadeNumericUtil.isIntegerPositif(annee)) {
            return false;
        }

        int anneeInt = Integer.parseInt(annee);
        boolean inValidRange = (anneeInt >= 1948) && (anneeInt <= Calendar.getInstance().get(Calendar.YEAR));
        return inValidRange;
    }

    private boolean isMontantARecouvrirAboveMontantDisponible() {
        return new BigDecimal(montantARecouvrir).compareTo(new BigDecimal(montantDisponible)) > 0;
    }

    private boolean isMontantARecouvrirAboveZero() {
        return new BigDecimal(montantARecouvrir).compareTo(new BigDecimal("0.0")) > 0;
    }

    /**
     * D�finit l'ann�e � utiliser si la base d'amortissement n'est pas disponible
     * 
     * @param annee
     *            l'ann�e � utiliser
     */
    public void setAnnee(String annee) {
        this.annee = annee;
    }

    /**
     * D�finit la liste des bases d'amortissement
     * 
     * @param basesAmortissement
     *            liste des bases d'amortissement
     */
    public void setBasesAmortissement(CARecouvrementBaseAmortissementContainer basesAmortissement) {
        this.basesAmortissement = basesAmortissement;
    }

    /**
     * D�finit l'id du compte annexe � traiter
     * 
     * @param idCompteAnnexe
     *            Id du compte annexe.
     */
    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    /**
     * D�finit la liste des sections � traiter
     * 
     * @param idSectionToTreatList
     *            liste des sections � traiter
     */
    public void setIdSectionsList(List<String> idSectionToTreatList) {
        idSectionsList = idSectionToTreatList;
    }

    /**
     * D�finit le montant � recouvrir indiqu� par l'utilisateur Il peut �tre �gal ou inf�rieur au montant � disponible
     * pour le recouvrement
     * 
     * @param montantRecouvrir
     *            montant � recouvrir
     */
    public void setMontantARecouvrir(String montantRecouvrir) {
        montantARecouvrir = montantRecouvrir;
    }

    /**
     * d�finit le montant disponible pour le recouvrement
     * 
     * @param montantDisponible
     *            montant
     */
    public void setMontantDisponible(String montantDisponible) {
        this.montantDisponible = montantDisponible;
    }

    public void clearListSelectedBasesAmortissement() {
        selectedBasesAmortissement = new ArrayList<String>();
    }

    public void setSelectedBasesAmortissement(List<String> selectedBasesAmortissement) {
        this.selectedBasesAmortissement = selectedBasesAmortissement;
    }

    public boolean isValid() throws RemoteException {

        if (!isMontantARecouvrirAboveZero()) {
            _addError(((BSession) getISession()).getLabel("RECOUVREMENT_SELECT_MONTANT_DIFF_ZERO_ERROR"));
        }

        if (isMontantARecouvrirAboveMontantDisponible()) {
            _addError(((BSession) getISession()).getLabel("RECOUVREMENT_SELECT_MONTANT_SUPERIEUR_MONTANT_DISPO_ERROR"));
        }

        if ((selectedBasesAmortissement.size() == 0) && !JadeNumericUtil.isIntegerPositif(annee)) {
            _addError(((BSession) getISession()).getLabel("RECOUVREMENT_SELECT_MONTANT_AUCUNE_BASE_SELECTIONNEE_ERROR"));
        }

        if ((selectedBasesAmortissement.size() > 0) && JadeNumericUtil.isIntegerPositif(annee)) {
            _addError(((BSession) getISession())
                    .getLabel("RECOUVREMENT_SELECT_MONTANT_BASE_ET_ANNEE_SELECTIONNES_ERROR"));
        }

        if (JadeNumericUtil.isIntegerPositif(annee) && !isAnneeInValidRange()) {
            _addError(((BSession) getISession()).getLabel("RECOUVREMENT_SELECT_MONTANT_ANNEE_HORS_LIMITES_ERROR"));
        }

        if (getISession().hasErrors()) {
            setMessage(getISession().getErrors().toString());
            setMsgType(FWViewBeanInterface.ERROR);
        }

        return FWViewBeanInterface.OK.equals(getMsgType());
    }
}