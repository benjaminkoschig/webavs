package ch.globaz.orion.businessimpl.services.partnerWeb;

public class ContactEbusinessAffilie implements Comparable<ContactEbusinessAffilie>, ContactSalaire, Contact {

    private String numeroAffilie;
    private String nom;
    private String dateRadiation;
    private String codeDeclaration;
    private String email;
    private Boolean isAdministrateur = false;
    private String nomMandataire;
    private Boolean isMandataire = false;
    private String description;
    private String nomUser;
    private String prenomUser;
    private String user;
    private Integer idUser;
    private String etape;
    private Boolean isActif;
    private String telephoneUser;
    private Boolean isEnvoye = false;

    @Override
    public String getEtape() {
        return etape;
    }

    public void setEtape(String etape) {
        this.etape = etape;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    @Override
    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    @Override
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String getDateRadiation() {
        return dateRadiation;
    }

    public void setDateRadiation(String dateRadiation) {
        this.dateRadiation = dateRadiation;
    }

    @Override
    public String getCodeDeclaration() {
        return codeDeclaration;
    }

    public void setCodeDeclaration(String codeDeclaration) {
        this.codeDeclaration = codeDeclaration;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Boolean isAdministrateur() {
        return isAdministrateur;
    }

    public void setIsAdministrateur(Boolean isAdministrateur) {
        this.isAdministrateur = isAdministrateur;
    }

    @Override
    public String getNomMandataire() {
        return nomMandataire;
    }

    public void setNomMandataire(String nomMandataire) {
        this.nomMandataire = nomMandataire;
    }

    @Override
    public Boolean isMandataire() {
        return isMandataire;
    }

    public void setIsMandataire(Boolean isMandataire) {
        this.isMandataire = isMandataire;
    }

    @Override
    public String getNomUser() {
        return nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    @Override
    public String getPrenomUser() {
        return prenomUser;
    }

    public void setPrenomUser(String prenomUser) {
        this.prenomUser = prenomUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public Boolean isActif() {
        return isActif;
    }

    public void setIsActif(Boolean isActif) {
        this.isActif = isActif;
    }

    @Override
    public String getTelephoneUser() {
        return telephoneUser;
    }

    public void setTelephoneUser(String telephoneUser) {
        this.telephoneUser = telephoneUser;
    }

    @Override
    public Boolean isEnvoye() {
        return isEnvoye;
    }

    public void setIsEnvoye(boolean isEnvoye) {
        this.isEnvoye = isEnvoye;
    }

    @Override
    public int compareTo(ContactEbusinessAffilie o) {
        return (nom + nomMandataire + idUser + numeroAffilie).toUpperCase().compareTo(
                (o.getNom() + o.getNomMandataire() + o.getIdUser() + o.getNumeroAffilie()).toUpperCase());

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codeDeclaration == null) ? 0 : codeDeclaration.hashCode());
        result = prime * result + ((dateRadiation == null) ? 0 : dateRadiation.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((etape == null) ? 0 : etape.hashCode());
        result = prime * result + ((idUser == null) ? 0 : idUser.hashCode());
        result = prime * result + ((isActif == null) ? 0 : isActif.hashCode());
        result = prime * result + ((isAdministrateur == null) ? 0 : isAdministrateur.hashCode());
        result = prime * result + ((isMandataire == null) ? 0 : isMandataire.hashCode());
        result = prime * result + ((nom == null) ? 0 : nom.hashCode());
        result = prime * result + ((nomMandataire == null) ? 0 : nomMandataire.hashCode());
        result = prime * result + ((nomUser == null) ? 0 : nomUser.hashCode());
        result = prime * result + ((numeroAffilie == null) ? 0 : numeroAffilie.hashCode());
        result = prime * result + ((prenomUser == null) ? 0 : prenomUser.hashCode());
        result = prime * result + ((telephoneUser == null) ? 0 : telephoneUser.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ContactEbusinessAffilie other = (ContactEbusinessAffilie) obj;
        if (codeDeclaration == null) {
            if (other.codeDeclaration != null) {
                return false;
            }
        } else if (!codeDeclaration.equals(other.codeDeclaration)) {
            return false;
        }
        if (dateRadiation == null) {
            if (other.dateRadiation != null) {
                return false;
            }
        } else if (!dateRadiation.equals(other.dateRadiation)) {
            return false;
        }
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (email == null) {
            if (other.email != null) {
                return false;
            }
        } else if (!email.equals(other.email)) {
            return false;
        }
        if (etape == null) {
            if (other.etape != null) {
                return false;
            }
        } else if (!etape.equals(other.etape)) {
            return false;
        }
        if (idUser == null) {
            if (other.idUser != null) {
                return false;
            }
        } else if (!idUser.equals(other.idUser)) {
            return false;
        }
        if (isActif == null) {
            if (other.isActif != null) {
                return false;
            }
        } else if (!isActif.equals(other.isActif)) {
            return false;
        }
        if (isAdministrateur == null) {
            if (other.isAdministrateur != null) {
                return false;
            }
        } else if (!isAdministrateur.equals(other.isAdministrateur)) {
            return false;
        }
        if (isMandataire == null) {
            if (other.isMandataire != null) {
                return false;
            }
        } else if (!isMandataire.equals(other.isMandataire)) {
            return false;
        }
        if (nom == null) {
            if (other.nom != null) {
                return false;
            }
        } else if (!nom.equals(other.nom)) {
            return false;
        }
        if (nomMandataire == null) {
            if (other.nomMandataire != null) {
                return false;
            }
        } else if (!nomMandataire.equals(other.nomMandataire)) {
            return false;
        }
        if (nomUser == null) {
            if (other.nomUser != null) {
                return false;
            }
        } else if (!nomUser.equals(other.nomUser)) {
            return false;
        }
        if (numeroAffilie == null) {
            if (other.numeroAffilie != null) {
                return false;
            }
        } else if (!numeroAffilie.equals(other.numeroAffilie)) {
            return false;
        }
        if (prenomUser == null) {
            if (other.prenomUser != null) {
                return false;
            }
        } else if (!prenomUser.equals(other.prenomUser)) {
            return false;
        }
        if (telephoneUser == null) {
            if (other.telephoneUser != null) {
                return false;
            }
        } else if (!telephoneUser.equals(other.telephoneUser)) {
            return false;
        }
        if (user == null) {
            if (other.user != null) {
                return false;
            }
        } else if (!user.equals(other.user)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ContactEbusinessAffilie [numeroAffilie=" + numeroAffilie + ", nom=" + nom + ", dateRadiation="
                + dateRadiation + ", codeDeclaration=" + codeDeclaration + ", email=" + email + ", isAdministrateur="
                + isAdministrateur + ", nomMandataire=" + nomMandataire + ", isMandataire=" + isMandataire
                + ", description=" + description + ", nomUser=" + nomUser + ", prenomUser=" + prenomUser + ", user="
                + user + ", idUser=" + idUser + ", etape=" + etape + ", isActif=" + isActif + ", telephoneUser="
                + telephoneUser + "]";
    }

}
