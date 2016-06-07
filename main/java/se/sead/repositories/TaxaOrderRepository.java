package se.sead.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import se.sead.bugsimport.species.seadmodel.TaxaOrder;

/**
 * Created by erer0001 on 2016-04-22.
 */
public interface TaxaOrderRepository extends Repository<TaxaOrder, Integer> {
    @Query("select taxaOrder from TaxaOrder taxaOrder where orderName = 'ORDER PENDING CLASSIFICATION'")
    TaxaOrder getImportOrder();
}
