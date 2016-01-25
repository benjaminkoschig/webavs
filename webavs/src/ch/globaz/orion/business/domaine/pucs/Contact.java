package ch.globaz.orion.business.domaine.pucs;

public class Contact {

    private String name;
    private String phone;
    private String mail;

    public Contact(String name, String phone, String mail) {
        super();
        this.name = name;
        this.phone = phone;
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getMail() {
        return mail;
    }

}
