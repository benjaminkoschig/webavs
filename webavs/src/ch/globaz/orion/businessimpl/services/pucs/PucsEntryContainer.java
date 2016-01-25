package ch.globaz.orion.businessimpl.services.pucs;

public class PucsEntryContainer {
    private PucsEntry[] pucsEntries = null;

    public PucsEntryContainer() {
        super();
    }

    public PucsEntry[] getPucsEntries() {
        return pucsEntries;
    }

    public void setPucsEntries(PucsEntry[] pucsEntries) {
        this.pucsEntries = pucsEntries;
    }

}
