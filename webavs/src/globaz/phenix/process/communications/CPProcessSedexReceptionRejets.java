package globaz.phenix.process.communications;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.crypto.JadeDecryptionNotSupportedException;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.crypto.JadeEncrypterNotFoundException;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.log.JadeLogger;
import globaz.jade.sedex.JadeSedexMessageNotHandledException;
import globaz.jade.sedex.annotation.OnReceive;
import globaz.jade.sedex.annotation.Setup;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.phenix.db.communications.CPRejets;
import java.util.Properties;
import ch.eahv_iv.xmlns.eahv_iv_2011_000104._3.Message;

public class CPProcessSedexReceptionRejets {
    private String passSedex = "";
    private BSession session = null;
    private String userSedex = "";

    private void ajoutDeLaCommunication(BTransaction transaction, Message message) throws Exception {
        CPRejets rejet = new CPRejets();
        rejet.setSession(getSession());
        if (message.getHeader().getSenderId() != null) {
            rejet.setSenderId(message.getHeader().getSenderId());
        } else {
            rejet.setSenderId("");
        }

        if (message.getHeader().getDeclarationLocalReference() != null) {
            if (message.getHeader().getDeclarationLocalReference().getDepartment() != null) {
                rejet.setDepartementReference(message.getHeader().getDeclarationLocalReference().getDepartment());
            } else {
                rejet.setDepartementReference("");
            }
            if (message.getHeader().getDeclarationLocalReference().getPhone() != null) {
                rejet.setTelephoneReference(message.getHeader().getDeclarationLocalReference().getPhone());
            } else {
                rejet.setTelephoneReference("");
            }
            if (message.getHeader().getDeclarationLocalReference().getEmail() != null) {
                rejet.setEmailReference(message.getHeader().getDeclarationLocalReference().getEmail());
            } else {
                rejet.setEmailReference("");
            }
            if (message.getHeader().getDeclarationLocalReference().getName() != null) {
                rejet.setNomReference(message.getHeader().getDeclarationLocalReference().getName());
            } else {
                rejet.setNomReference("");
            }
        } else {
            rejet.setDepartementReference("");
            rejet.setTelephoneReference("");
            rejet.setEmailReference("");
            rejet.setNomReference("");
        }
        if (message.getHeader().getRecipientId().size() > 0) {
            if (message.getHeader().getRecipientId().get(0) != null) {
                rejet.setRecipientId(message.getHeader().getRecipientId().get(0));
            } else {
                rejet.setRecipientId("");
            }
        } else {
            rejet.setRecipientId("");
        }
        if (message.getHeader().getMessageId() != null) {
            rejet.setMessageId(message.getHeader().getMessageId());
        } else {
            rejet.setMessageId("");
        }
        if (message.getHeader().getReferenceMessageId() != null) {
            rejet.setReferenceMessageId(message.getHeader().getReferenceMessageId());
        } else {
            rejet.setReferenceMessageId("");
        }
        if (message.getHeader().getOurBusinessReferenceID() != null) {
            rejet.setOurBusinessReferenceId(message.getHeader().getOurBusinessReferenceID());
        } else {
            rejet.setOurBusinessReferenceId("");
        }
        if (message.getHeader().getYourBusinessReferenceId() != null) {
            rejet.setYourBusinessReferenceId(message.getHeader().getYourBusinessReferenceId());
        } else {
            rejet.setYourBusinessReferenceId("");
        }
        if (message.getHeader().getSubject() != null) {
            rejet.setSubject(message.getHeader().getSubject());
        } else {
            rejet.setSubject("");
        }
        if (message.getHeader().getObject().getLocalPersonId() != null) {
            if (message.getHeader().getObject().getLocalPersonId().getPersonIdCategory() != null) {
                rejet.setPersonIdCategory(message.getHeader().getObject().getLocalPersonId().getPersonIdCategory());
            } else {
                rejet.setPersonIdCategory("");
            }
            if (message.getHeader().getObject().getLocalPersonId().getPersonId() != null) {
                rejet.setPersonId(message.getHeader().getObject().getLocalPersonId().getPersonId());
            } else {
                rejet.setPersonId("");
            }
        } else {
            rejet.setPersonIdCategory("");
            rejet.setPersonId("");
        }
        if (message.getHeader().getObject().getOfficialName() != null) {
            rejet.setNom(message.getHeader().getObject().getOfficialName());
        } else {
            rejet.setNom("");
        }
        if (message.getHeader().getObject().getFirstName() != null) {
            rejet.setPrenom(message.getHeader().getObject().getFirstName());
        } else {
            rejet.setPrenom("");
        }
        if (message.getHeader().getObject().getSex() != null) {
            rejet.setSex(message.getHeader().getObject().getSex());
        } else {
            rejet.setSex("");
        }
        if (message.getHeader().getObject().getDateOfBirth() != null) {
            if (message.getHeader().getObject().getDateOfBirth().getYearMonthDay() != null) {
                rejet.setDateNaissance(String.valueOf(message.getHeader().getObject().getDateOfBirth()
                        .getYearMonthDay()));
            }
        }
        if (message.getHeader().getObject().getAddress() != null) {
            if (message.getHeader().getObject().getAddress().getAddressLine1() != null) {
                rejet.setAdresse1(message.getHeader().getObject().getAddress().getAddressLine1());
            } else {
                rejet.setAdresse1("");
            }
            if (message.getHeader().getObject().getAddress().getAddressLine2() != null) {
                rejet.setAdresse2(message.getHeader().getObject().getAddress().getAddressLine2());
            } else {
                rejet.setAdresse2("");
            }
            if (message.getHeader().getObject().getAddress().getStreet() != null) {
                rejet.setRue(message.getHeader().getObject().getAddress().getStreet());
            } else {
                rejet.setRue("");
            }
            if (message.getHeader().getObject().getAddress().getLocality() != null) {
                rejet.setLocalite(message.getHeader().getObject().getAddress().getLocality());
            } else {
                rejet.setLocalite("");
            }
            if (message.getHeader().getObject().getAddress().getTown() != null) {
                rejet.setVille(message.getHeader().getObject().getAddress().getTown());
            } else {
                rejet.setVille("");
            }
        } else {
            rejet.setAdresse1("");
            rejet.setAdresse2("");
            rejet.setRue("");
            rejet.setLocalite("");
            rejet.setVille("");
        }
        if (message.getHeader().getObject().getMaritalStatus() != null) {
            rejet.setEtatCivil(message.getHeader().getObject().getMaritalStatus());
        } else {
            rejet.setEtatCivil("");
        }
        if (message.getHeader().getMessageDate() != null) {
            rejet.setDateMessage(String.valueOf(message.getHeader().getMessageDate()));
        } else {
            rejet.setDateMessage("");
        }
        if (message.getHeader().getInitialMessageDate() != null) {
            rejet.setDateInitialeMessage(String.valueOf(message.getHeader().getInitialMessageDate()));
        } else {
            rejet.setDateInitialeMessage("");
        }
        if (message.getHeader().getAction() != null) {
            rejet.setAction(message.getHeader().getAction());
        } else {
            rejet.setAction("");
        }
        rejet.setTestFlag("");
        if (message.getHeader().getMessagePriority() != null) {
            rejet.setMessagePriorite(String.valueOf(message.getHeader().getMessagePriority()));
        } else {
            rejet.setMessagePriorite("");
        }
        if (message.getContent().getReasonOfRejection() != null) {
            rejet.setRejet(String.valueOf(message.getContent().getReasonOfRejection()));
        } else {
            rejet.setRejet("");
        }
        if (message.getContent().getRemark() != null) {
            rejet.setRemark(message.getContent().getRemark());
        } else {
            rejet.setRemark("");
        }
        rejet.setEtat(CPRejets.CS_ETAT_NON_TRAITE);
        rejet.add(transaction);
        if (!transaction.hasErrors()) {
            transaction.commit();
        } else {
            transaction.rollback();
        }
    }

    public String getDescription() {
        return "Réception des communications fiscales Sedex";
    }

    public String getName() {
        return "ReceptionSedex";
    }

    public String getPassSedex() {
        return passSedex;
    }

    public BSession getSession() {
        return session;
    }

    public String getUserSedex() {
        return userSedex;
    }

    @OnReceive
    public void onReceive(Object o) throws JadeSedexMessageNotHandledException {
        BSession session;
        SimpleSedexMessage message = null;
        Message message2011104 = null;
        try {
            if ((!JadeStringUtil.isEmpty(getUserSedex())) && (!JadeStringUtil.isEmpty(getPassSedex()))) {
                session = new BSession("PHENIX");
                session.connect(getUserSedex(), getPassSedex());
                setSession(session);
            } else {
                throw new Exception("Utilisateur Sedex non défini (JadeSedexService)");
            }

            message = (SimpleSedexMessage) o;
            Object myObject = JAXBServices.getInstance().unmarshal(message.getFileLocation(), false, false,
                    new Class<?>[0]);
            if (myObject instanceof Message) {
                message2011104 = (Message) myObject;
                run(message2011104);
            } else {
                JadeLogger.error(this,
                        "La version des messages reçus n'est pas compatible avec la version actuelle de Web@AVS");
                throw new JadeSedexMessageNotHandledException(
                        "La version des messages reçus n'est pas compatible avec la version actuelle de Web@AVS");
            }
        } catch (Exception e) {
            JadeLogger.error(this, "SEDEX: error receiving message "
                    + ((message2011104 == null) ? "" : message2011104.getHeader().getMessageId()));
            JadeLogger.error(this, e);
            throw new JadeSedexMessageNotHandledException(
                    "Erreur dans la réception des rejets de communication fiscale : " + " : "
                            + ((message2011104 == null) ? "" : message2011104.getHeader().getMessageId()));
        }

    }

    public void run(Message message) throws Exception {
        BTransaction transaction = null;
        try {

            transaction = new BTransaction(getSession());
            transaction.openTransaction();
            ajoutDeLaCommunication(transaction, message);
            if (!transaction.hasErrors()) {
                transaction.commit();
            } else {
                throw new Exception("I won't commit the transaction since it has errors. " + transaction.getErrors());
            }
        } catch (Exception e) {
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception e2) {
                    JadeLogger.error(this, e2);
                }
            }
            throw e;
        } finally {
            try {
                if (transaction != null) {
                    transaction.closeTransaction();
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
    }

    public void setPassSedex(String passSedex) {
        this.passSedex = passSedex;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    @Setup
    public void setUp(Properties properties) throws JadeDecryptionNotSupportedException,
            JadeEncrypterNotFoundException, Exception {
        String encryptedUser = properties.getProperty("userSedex");
        if (encryptedUser == null) {
            JadeLogger.error(this, "Réception message 2011-103: user sedex non renseigné. ");
            throw new IllegalStateException("Réception message 2011-103: user sedex non renseigné. ");
        }
        String decryptedUser = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedUser);

        String encryptedPass = properties.getProperty("passSedex");
        if (encryptedPass == null) {
            JadeLogger.error(this, "Réception message 2011-103: mot de passe sedex non renseigné. ");
            throw new IllegalStateException("Réception message 2011-103: mot de passe sedex non renseigné. ");
        }
        String decryptedPass = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedPass);

        setUserSedex(decryptedUser);
        setPassSedex(decryptedPass);
    }

    public void setUserSedex(String userSedex) {
        this.userSedex = userSedex;
    }

}
