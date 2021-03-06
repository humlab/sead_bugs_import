package se.sead.bugsimport.species.seadmodel;

import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;

@Entity
@Table(name="tbl_taxa_tree_genera", schema = "public")
@SequenceGenerator(name="taxa_genus_id_seq", sequenceName = "tbl_taxa_tree_genera_genus_id_seq")
public class TaxaGenus extends LoggableEntity {

    @Id
    @GeneratedValue(generator = "taxa_genus_id_seq", strategy = GenerationType.IDENTITY)
    @Column(name="genus_id", nullable = false)
    private Integer id;
    @Column(name="genus_name")
    private String genusName;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="family_id")
    private TaxaFamily family;

    public Integer getId() {
        return id;
    }
    protected void setId(Integer id){
        this.id = id;
    }

    public String getGenusName() {
        return genusName;
    }

    public void setGenusName(String genusName) {
        this.genusName = genusName;
    }

    public TaxaFamily getFamily() {
        return family;
    }

    public void setFamily(TaxaFamily family) {
        this.family = family;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof TaxaGenus)) return false;

        TaxaGenus taxaGenus = (TaxaGenus) o;

        if (id != null ? !id.equals(taxaGenus.id) : taxaGenus.id != null) return false;
        if (!genusName.equals(taxaGenus.genusName)) return false;
        return family.equals(taxaGenus.family);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + genusName.hashCode();
        result = 31 * result + family.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TaxaGenus{" +
                "id=" + id +
                ", genusName='" + genusName + '\'' +
                ", family=" + family.getFamilyName() +
                '}';
    }
}
