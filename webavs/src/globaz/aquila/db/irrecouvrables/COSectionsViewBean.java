package globaz.aquila.db.irrecouvrables;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionViewBean;
import globaz.osiris.db.comptes.CATypeSection;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <H1>Description</H1>
 * 
 * @author dostes, 4 janv. 05
 */
public class COSectionsViewBean extends CASectionViewBean implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = "";
    private String dernierMontantDisponible;
    private String emailAddress;
    private Boolean extourneCI = Boolean.TRUE;
    private List<String> idSectionsList;
    private String libelleJournal = "";
    private BigDecimal montantTotalDisponible;
    private BigDecimal montantTotalIrrecouvrable;
    private Map<String, COPoste> postes = new HashMap<String, COPoste>();// key=idRubriqueIrrecouvrable
    private BigDecimal soldeTotalSections;

    /**
     * @param poste
     */
    public void addPoste(COPoste poste) {
        postes.put(poste.getIdRubriqueIrrecouvrable(), poste);
    }

    public void clearIdSections() {
        idSectionsList.clear();
    }

    /**
     * @return the annee
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * Date de création : (17.01.2002 12:00:16)
     * 
     * @return java.lang.String
     */
    public String getCSTypeSection() {
        BSession caSession;

        try {
            if (getSession().getApplicationId().equals(CAApplication.DEFAULT_APPLICATION_OSIRIS)) {
                caSession = getSession();
            } else {
                caSession = (BSession) GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS)
                        .newSession();
                getSession().connectSession(caSession);
            }

            CATypeSection typeSection = new CATypeSection();
            typeSection.setSession(caSession);
            typeSection.setIdTypeSection(getIdTypeSection());

            typeSection.retrieve();

            if (typeSection.isNew()) {
                return null;
            } else {
                return typeSection.getDescription();
            }
        } catch (Exception e1) {
            return e1.toString();
        }
    }

    /**
     * getter pour l'attribut dernier montant disponible
     * 
     * @return la valeur courante de l'attribut dernier montant disponible
     */
    public String getDernierMontantDisponible() {
        return dernierMontantDisponible;
    }

    /**
     * Returns the emailAddress.
     * 
     * @return String
     */
    public String getEmailAddress() {
        if (emailAddress == null) {
            emailAddress = getSession().getUserEMail();
        }

        return emailAddress;
    }

    /**
     * @return the extourneCI
     */
    public Boolean getExtourneCI() {
        return extourneCI;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getIdFirstCaisseProf() throws Exception {
        for (String idSection : idSectionsList) {
            CASection section = new CASection();
            section.setSession(getSession());
            section.setIdSection(idSection);
            try {
                section.retrieve();
                if (!section.isNew()) {
                    if (!JadeStringUtil.isBlankOrZero(section.getIdCaisseProfessionnelle())) {
                        return section.getIdCaisseProfessionnelle();
                    }
                }
            } catch (Exception e) {
                throw new Exception("unable to retrieve section" + e.getMessage());
            }
        }
        return "";
    }

    /**
     * getter pour l'attribut id sections
     * 
     * @return la valeur courante de l'attribut id sections
     */
    public List<String> getIdSectionsList() {
        return idSectionsList;
    }

    /**
     * getter pour l'attribut libelle journal
     * 
     * @return la valeur courante de l'attribut libelle journal
     */
    public String getLibelleJournal() {
        return libelleJournal;
    }

    /**
     * getter pour l'attribut liste postes tries par ordre
     * 
     * @return la valeur courante de l'attribut liste postes tries par ordre
     */
    public List<COPoste> getListePostesTriesParOrdre() {
        List<COPoste> listePostes = new ArrayList<COPoste>(postes.values());
        Collections.sort(listePostes); // COPoste implemente Comparable
        return listePostes;
    }

    /**
     * getter pour l'attribut montant total disponible
     * 
     * @return la valeur courante de l'attribut montant total disponible
     */
    public BigDecimal getMontantTotalDisponible() {
        return montantTotalDisponible;
    }

    /**
     * getter pour l'attribut montant total disponible
     * 
     * @return la valeur courante de l'attribut montant total disponible
     */
    public String getMontantTotalDisponibleFormatte() {
        return JANumberFormatter.format(montantTotalDisponible);
    }

    /**
     * getter pour l'attribut montant total irrecouvrable
     * 
     * @return la valeur courante de l'attribut montant total irrecouvrable
     */
    public BigDecimal getMontantTotalIrrecouvrable() {
        return montantTotalIrrecouvrable;
    }

    /**
     * getter pour l'attribut montant total irrecouvrable
     * 
     * @return la valeur courante de l'attribut montant total irrecouvrable
     */
    public String getMontantTotalIrrecouvrableFormatte() {
        return JANumberFormatter.format(montantTotalIrrecouvrable);
    }

    /**
     * getter pour l'attribut poste
     * 
     * @param idRubrique
     *            DOCUMENT ME!
     * @return la valeur courante de l'attribut poste
     */
    public COPoste getPoste(String idRubrique) {
        return postes.get(idRubrique);
    }

    /**
     * getter pour l'attribut postes
     * 
     * @return la valeur courante de l'attribut postes
     */
    public Map<String, COPoste> getPostes() {
        return postes;
    }

    /**
     * getter pour l'attribut selected ids
     * 
     * @return la valeur courante de l'attribut selected ids
     */
    public String getSelectedIds() {
        if (idSectionsList == null) {
            return "";
        } else {
            StringBuffer retValue = new StringBuffer();

            for (int idSection = 0; idSection < idSectionsList.size(); ++idSection) {
                if (retValue.length() > 0) {
                    retValue.append(',');
                }

                retValue.append(idSectionsList.get(idSection));
            }

            return retValue.toString();
        }
    }

    /**
     * solde total des sections selectionnées utilisé dans COSectionsViewBean.validerPourExecution
     * 
     * @return the sodleTotalSection
     */
    public BigDecimal getSoldeTotalSections() {
        return soldeTotalSections;
    }

    /**
     * @return the extourneCI
     */
    public Boolean isExtourneCI() {
        return extourneCI;
    }

    /**
     * reinitialise le viewbean (efface tous les postes et remet les montants a 0)
     */
    public void reset() {
        postes.clear();
        montantTotalDisponible = new BigDecimal(0);
        montantTotalIrrecouvrable = new BigDecimal(0);
    }

    /**
     * Remet les montants a 0. <br/>
     * Recalcule les montants en se basant sur les postes COPoste.
     */
    public void resetMontants() {
        montantTotalDisponible = new BigDecimal(0);
        montantTotalIrrecouvrable = new BigDecimal(0);

        for (Iterator<COPoste> postesIter = postes.values().iterator(); postesIter.hasNext();) {
            COPoste poste = postesIter.next();

            poste.resetMontants();
            montantTotalDisponible = montantTotalDisponible.add(poste.getMontantTotalVerse());
            montantTotalIrrecouvrable = montantTotalIrrecouvrable.add(poste.getMontantIrrecouvrable());
        }
    }

    /**
     * @param annee
     *            the annee to set
     */
    public void setAnnee(String annee) {
        this.annee = annee;
    }

    /**
     * setter pour l'attribut dernier montant disponible
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDernierMontantDisponible(String string) {
        dernierMontantDisponible = string;
    }

    /**
     * Sets the emailAddress.
     * 
     * @param emailAddress
     *            The emailAddress to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * @param extourneCI
     *            the extourneCI to set
     */
    public void setExtourneCI(Boolean extourneCI) {
        this.extourneCI = extourneCI;
    }

    /**
     * setter pour l'attribut libelle journal
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setLibelleJournal(String string) {
        libelleJournal = string;
    }

    /**
     * setter pour l'attribut montant total disponible
     * 
     * @param montantTotalDisponible
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantTotalDisponible(String montantTotalDisponible) {
        this.montantTotalDisponible = new BigDecimal(JANumberFormatter.deQuote(montantTotalDisponible));
    }

    /**
     * setter pour l'attribut montant total irrecouvrable
     * 
     * @param montantTotalIrrecouvrable
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantTotalIrrecouvrable(String montantTotalIrrecouvrable) {
        this.montantTotalIrrecouvrable = new BigDecimal(JANumberFormatter.deQuote(montantTotalIrrecouvrable));
    }

    /**
     * setter pour l'attribut montant verse
     * 
     * @param idSection
     *            une nouvelle valeur pour cet attribut
     * @param idRubrique
     *            une nouvelle valeur pour cet attribut
     * @param idRubriqueIrrecouvrable
     *            DOCUMENT ME!
     * @param montant
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantVerse(String idSection, String idRubrique, String typeOrdre, String idRubriqueIrrecouvrable,
            String montant) {
        try {
            postes.get(idRubriqueIrrecouvrable).setMontantVerse(idSection, idRubrique, typeOrdre,
                    JANumberFormatter.deQuote(montant));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * setter pour l'attribut selected ids
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setSelectedIds(String string) {
        idSectionsList = Arrays.asList(JadeStringUtil.split(string, ',', Integer.MAX_VALUE));
    }

    /**
     * solde total des sections selectionnées utilisé dans COSectionsViewBean.validerPourExecution
     * 
     * @param soldeTotalSection
     *            the sodleTotalSection to set
     */
    public void setSoldeTotalSections(BigDecimal soldeTotalSection) {
        soldeTotalSections = soldeTotalSection;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public boolean validerPourExecution() {
        _propertyMandatory(null, emailAddress, getSession().getLabel("AQUILA_ERR_CO042_DESTINATAIRE_PAS_SELECTIONNE"));
        _propertyMandatory(null, libelleJournal, getSession().getLabel("AQUILA_ERR_NOM_JOURNAL"));
        _propertyMandatory(null, annee, getSession().getLabel("AQUILA_ERR_ANNEE_NON_RENS"));

        if (!JadeDateUtil.isGlobazDateYear(annee)) {
            _addError(null, getSession().getLabel("AQUILA_ERR_ANNEE_INCORRECTE"));
        }

        if (!JadeStringUtil.isDecimalEmpty(dernierMontantDisponible)) {
            _addError(null, getSession().getLabel("AQUILA_ERR_MONTANT_DISPONIBLE"));
        }

        if (!getMontantTotalIrrecouvrable().equals(getSoldeTotalSections())) {
            _addError(null, getSession().getLabel("AQUILA_ERR_IRR_SOLDE_SECTION"));
        }

        if (getSession().hasErrors()) {
            setMessage(getSession().getErrors().toString());
            setMsgType(FWViewBeanInterface.ERROR);
        }

        return FWViewBeanInterface.OK.equals(getMsgType());
    }
}
