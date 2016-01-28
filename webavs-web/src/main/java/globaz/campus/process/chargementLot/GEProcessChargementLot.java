package globaz.campus.process.chargementLot;

import globaz.campus.db.annonces.GEAnnonces;
import globaz.campus.db.lots.GELots;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import globaz.pyxis.db.tiers.TITiersViewBean;

public class GEProcessChargementLot extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FORMAT_FICHIER_EXCEL = "2";
    public static final String FORMAT_FICHIER_TEXTE = "1";
    private String annee = "";
    private String dateTraitement = "";
    private String filename = "";
    private String formatFichier = "";
    private String idTiersEcole = "";
    private String libelleLot = "";

    private void _creationAnnonce(GELectureFichier fichier, String idLot) throws Exception {
        int sizeEnregistrement = fichier.getEnregistrementSize();
        setProgressScaleValue(sizeEnregistrement);
        for (int i = 0; i < sizeEnregistrement; i++) {
            GESerieRecords rec = fichier.getEnregistrement(i);
            // Alimente la table GEANNOP
            GEAnnonces annonce = new GEAnnonces();
            annonce.setSession(getSession());
            annonce.setCsEtatAnnonce(GEAnnonces.CS_ETAT_A_TRAITER);
            annonce.setIdLot(idLot);
            annonce.setIsImputation(new Boolean(false));
            // Première ligne - record 1
            annonce.setNumSequence(rec.getRecord1().getNumeroSequenceField().format());
            annonce.setNumAvs(rec.getRecord1().getNumeroAvsField().format());
            annonce.setNom(rec.getRecord1().getNomField().format());
            annonce.setPrenom(rec.getRecord1().getPrenomField().format());
            annonce.setDateNaissance(rec.getRecord1().getDateNaissanceField().format());
            if ("M".equals(rec.getRecord1().getSexeField().format())) {
                annonce.setCsSexe(TITiersViewBean.CS_HOMME);
            } else {
                annonce.setCsSexe(TITiersViewBean.CS_FEMME);
            }
            if ("M".equals(rec.getRecord1().getEtatCivilField().format())) {
                annonce.setCsEtatCivil(TITiersViewBean.CS_MARIE);
            } else if ("C".equals(rec.getRecord1().getEtatCivilField().format())) {
                annonce.setCsEtatCivil(TITiersViewBean.CS_CELIBATAIRE);
            } else if ("D".equals(rec.getRecord1().getEtatCivilField().format())) {
                annonce.setCsEtatCivil(TITiersViewBean.CS_DIVORCE);
            } else if ("B".equals(rec.getRecord1().getEtatCivilField().format())) {
                annonce.setCsEtatCivil(TITiersViewBean.CS_SEPARE);
            } else if ("S".equals(rec.getRecord1().getEtatCivilField().format())) {
                annonce.setCsEtatCivil(TITiersViewBean.CS_SEPARE_DE_FAIT);
            } else if ("V".equals(rec.getRecord1().getEtatCivilField().format())) {
                annonce.setCsEtatCivil(TITiersViewBean.CS_VEUF);
            }
            annonce.setNumImmatriculationTransmis(rec.getRecord1().getNumImmatriculationField().format());
            annonce.setReserve1(rec.getRecord1().getReserve1().format());
            if ("D".equals(rec.getRecord1().getCodeDoctorant().format())) {
                annonce.setCsCodeDoctorant(GEAnnonces.CS_DOCTORANT);
            } else if ("P".equals(rec.getRecord1().getCodeDoctorant().format())) {
                annonce.setCsCodeDoctorant(GEAnnonces.CS_POSTGRADE);
            }
            annonce.setReserve2(rec.getRecord1().getReserve2().format());
            // Deuxième ligne - record 2
            annonce.setAdresseEtude(rec.getRecord2().getAdresseEtudeField().format());
            annonce.setRueEtude(rec.getRecord2().getRueEtudeField().format());
            annonce.setNpaEtude(rec.getRecord2().getNpaEtudeField().format());
            annonce.setLocaliteEtude(rec.getRecord2().getLocaliteEtudeField().format());
            annonce.setSuffixePostalEtude(rec.getRecord2().getSuffixePostalEtudeField().format());
            annonce.setReserve3(rec.getRecord2().getReserve3().format());
            // Troisième ligne - record 3
            annonce.setAdresseLegale(rec.getRecord3().getAdresseLegaleField().format());
            annonce.setRueLegal(rec.getRecord3().getRueLegalField().format());
            annonce.setNpaLegal(rec.getRecord3().getNpaLegalField().format());
            annonce.setLocaliteLegal(rec.getRecord3().getLocaliteLegalField().format());
            annonce.setSuffixePostalLegal(rec.getRecord3().getSuffixePostalLegalField().format());
            annonce.setReserve3(rec.getRecord2().getReserve3().format());
            annonce.add(getTransaction());
            // Commit la transaction si elle n'est pas en erreur et rollback
            // dans le cas contraire
            if (getTransaction().hasErrors()) {
                throw new Exception("Dernière annonce traitée: " + annonce.getNumSequence() + " - "
                        + annonce.getNumAvs() + ": " + getTransaction().getErrors());
            } else {
                getTransaction().commit();
                if (!JadeStringUtil.isBlank(annonce.getMessagesErreurs())) {
                    getMemoryLog().logMessage(
                            "annonce: " + annonce.getNumImmatriculationTransmis() + ", " + annonce.getNom() + ", "
                                    + annonce.getPrenom() + ": " + annonce.getMessagesErreurs(),
                            FWMessage.AVERTISSEMENT, "ANNONCES");
                }
            }
            incProgressCounter();
        }
    }

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        boolean result = true;
        GELectureFichier fichier = null;
        GELots lot = null;
        try {
            JadeFsFacade.copyFile("jdbc://" + Jade.getInstance().getDefaultJdbcSchema() + "/" + getFilename(), Jade
                    .getInstance().getHomeDir() + "work/" + getFilename());
            // Lecture du fichier
            fichier = new GELectureFichier(Jade.getInstance().getHomeDir() + "work/" + getFilename(), getSession(),
                    getTransaction());
            fichier.parse();
            // Création du lot avec l'état en cours
            lot = new GELots();
            lot.setSession(getSession());
            lot.setIdTiersEcole(getIdTiersEcole());
            lot.setDateReceptionLot(getDateTraitement());
            lot.setCsEtatLot(GELots.CS_ETAT_EN_COURS);
            lot.setLibelleTraitement(getLibelleLot());
            lot.setAnnee(getAnnee());
            lot.add(getTransaction());
            if (!getTransaction().hasErrors()) {
                // Création des annonces
                _creationAnnonce(fichier, lot.getIdLot());
            }
            if (lot != null && !lot.isNew()) {
                lot.setCsEtatLot(GELots.CS_ETAT_A_TRAITER);
                lot.update(getTransaction());
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "LOT");
            if (lot != null && !lot.isNew()) {
                lot.setCsEtatLot(GELots.CS_ETAT_ERREUR);
                lot.update(getTransaction());
            }
            result = false;
        }
        return result;
    }

    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isBlank(getEMailAddress())) {
            _addError(getSession().getLabel("EMAIL_OBLIGATOIRE"));
        }
        if (JadeStringUtil.isBlank(getIdTiersEcole())) {
            _addError(getSession().getLabel("ECOLE_OBLIGATOIRE"));
        }
        if (JadeStringUtil.isBlankOrZero(getAnnee())) {
            _addError(getSession().getLabel("ANNEE_OBLIGATOIRE"));
        }
        if (JadeStringUtil.isBlank(getLibelleLot())) {
            _addError(getSession().getLabel("LIBELLE_OBLIGATOIRE"));
        }
        if (JadeStringUtil.isBlank(getFilename())) {
            _addError(getSession().getLabel("FILENAME_OBLIGATOIRE"));
        }
        if (JAUtil.isDateEmpty(getDateTraitement())) {
            _addError(getSession().getLabel("DATE_TRAITEMENT_OBLIGATOIRE"));
        }
        if (JadeStringUtil.isBlank(getFormatFichier())) {
            _addError(getSession().getLabel("FORMAT_FICHIER_OBLIGATOIRE"));
        }
        // divers :
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        // Les erreurs sont ajoutées à la session,
        // abort permet l'arrêt du process
        if (getSession().hasErrors()) {
            abort();
        }
    }

    public String getAnnee() {
        return annee;
    }

    public String getDateTraitement() {
        return dateTraitement;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError() || isAborted() || getMemoryLog().getErrorLevel().equals(FWMessage.ERREUR)) {
            return getSession().getLabel("RESULTAT_PROCESS_CHARGEMENT_KO");
        } else {
            return getSession().getLabel("RESULTAT_PROCESS_CHARGEMENT_OK");
        }
    }

    public String getFilename() {
        return filename;
    }

    public String getFormatFichier() {
        return formatFichier;
    }

    public String getIdTiersEcole() {
        return idTiersEcole;
    }

    public String getLibelleLot() {
        return libelleLot;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setDateTraitement(String dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setFormatFichier(String formatFichier) {
        this.formatFichier = formatFichier;
    }

    public void setIdTiersEcole(String idTiersEcole) {
        this.idTiersEcole = idTiersEcole;
    }

    public void setLibelleLot(String libelleLot) {
        this.libelleLot = libelleLot;
    }

}
