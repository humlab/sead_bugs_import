package se.sead.repositories;

import se.sead.bugsimport.species.seadmodel.TaxaFamily;
import se.sead.bugsimport.species.seadmodel.TaxaGenus;

import java.util.List;

public interface TaxaGenusRepository extends CreateAndReadRepository<TaxaGenus, Integer> {
    TaxaGenus findByGenusNameAndFamily(String genusName, TaxaFamily taxaFamily);

    List<TaxaGenus> findAll();
}
