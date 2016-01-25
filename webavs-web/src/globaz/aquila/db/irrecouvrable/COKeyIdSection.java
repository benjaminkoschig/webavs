package globaz.aquila.db.irrecouvrable;

public class COKeyIdSection {
    private String idSection;

    public COKeyIdSection(String idSection) {
        super();
        this.idSection = idSection;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        COKeyIdSection other = (COKeyIdSection) obj;
        if (idSection == null) {
            if (other.idSection != null) {
                return false;
            }
        } else if (!idSection.equals(other.idSection)) {
            return false;
        }
        return true;
    }

    public String getIdSection() {
        return idSection;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((idSection == null) ? 0 : idSection.hashCode());
        return result;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }
}
