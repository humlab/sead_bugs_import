package se.sead.bugsimport.speciesbiology.bugsmodel;

import se.sead.bugs.TraceableBugsData;

/**
 * Created by erer0001 on 2016-05-18.
 */
public class Biology implements TraceableBugsData {

    private Double code;
    private String ref;
    private String data;

    public Double getCode() {
        return code;
    }

    public void setCode(Double code) {
        this.code = code;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String compressToString() {
        return "{" +
                code + "," +
                ref + "," +
                data + "}";
    }

    @Override
    public String bugsTable() {
        return BiologyBugsTable.TABLE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Biology biology = (Biology) o;

        if (!code.equals(biology.code)) return false;
        if (ref != null ? !ref.equals(biology.ref) : biology.ref != null) return false;
        return data.equals(biology.data);

    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + (ref != null ? ref.hashCode() : 0);
        result = 31 * result + data.hashCode();
        return result;
    }
}
