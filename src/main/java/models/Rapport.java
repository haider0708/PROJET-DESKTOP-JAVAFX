
package models;
public class Rapport {
    private String note;
    private String type;
    private int rdvId;

    public Rapport(String note, String type, int rdvId) {
        this.note = note;
        this.type = type;
        this.rdvId = rdvId;
    }
    public Rapport(int id, String note, String type, int rdvId) {

        this.note = note;
        this.type = type;
        this.rdvId = rdvId;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRdvId() {
        return rdvId;
    }

    public void setRdvId(int rdvId) {
        this.rdvId = rdvId;
    }
}
