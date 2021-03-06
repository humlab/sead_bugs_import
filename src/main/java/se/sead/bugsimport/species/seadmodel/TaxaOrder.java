package se.sead.bugsimport.species.seadmodel;

import javax.persistence.*;

@Entity
@Table(name="tbl_taxa_tree_orders", schema = "public")
@SequenceGenerator(name="taxa_order_seq", sequenceName = "tbl_taxa_tree_orders_order_id_seq")
public class TaxaOrder{

    @Id
    @GeneratedValue(generator = "taxa_order_seq", strategy = GenerationType.IDENTITY)
    @Column(name="order_id", nullable = false)
    private Integer id;
    @Column(name="order_name", nullable = false)
    private String orderName;

    public Integer getId() {
        return id;
    }
    protected void setId(Integer id){
        this.id = id;
    }

    public String getOrderName() {

        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof TaxaOrder)) return false;

        TaxaOrder taxaOrder = (TaxaOrder) o;

        if (id != null ? !id.equals(taxaOrder.id) : taxaOrder.id != null) return false;
        return orderName.equals(taxaOrder.orderName);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + orderName.hashCode();
        return result;
    }
}
