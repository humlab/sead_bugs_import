package se.sead.fossil;

import se.sead.sead.data.Abundance;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.model.TestAbundance;
import se.sead.model.TestAnalysisEntity;
import se.sead.model.TestDataset;
import se.sead.model.TestEqualityHelper;
import se.sead.repositories.*;
import se.sead.sead.data.AnalysisEntity;
import se.sead.sead.data.DataType;
import se.sead.sead.data.Dataset;
import se.sead.sead.data.DatasetMaster;
import se.sead.sead.methods.Method;
import se.sead.sead.model.LoggableEntity;
import se.sead.testutils.DatabaseContentVerification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class AbundanceDatabaseContentProvider implements DatabaseContentVerification.DatabaseContentTestDataProvider<Abundance>{

    private SampleRepository sampleRepository;
    private SpeciesRepository speciesRepository;
    private DataType defaultDataType;
    private Method palaeoentomology;
    private DatasetMaster bugsDataset;
    private AbundanceRepository abundanceRepository;
    private boolean allowDatasetUpdate;

    AbundanceDatabaseContentProvider(
            SampleRepository sampleRepository,
            SpeciesRepository speciesRepository,
            DataTypeRepository dataTypeRepository,
            MethodRepository methodRepository,
            DatasetMasterRepository datasetMasterRepository,
            AbundanceRepository abundanceRepository,
            boolean allowDatasetUpdate
    ){
        this.sampleRepository = sampleRepository;
        this.speciesRepository = speciesRepository;
        defaultDataType = dataTypeRepository.findOne(1);
        palaeoentomology = methodRepository.findOne(3);
        bugsDataset = datasetMasterRepository.findOne(1);
        this.abundanceRepository = abundanceRepository;
        this.allowDatasetUpdate = allowDatasetUpdate;
    }

    @Override
    public List<Abundance> getExpectedData() {
        Dataset dataset04 = createDataset(3, "COUN000004");
        List<Abundance> expectedAbundances = new ArrayList<>(Arrays.asList(
                TestAbundance.create(
                        1,
                        speciesRepository.findOne(1),
                        TestAnalysisEntity.create(
                                1,
                                createDataset(1, "COUN000001"),
                                sampleRepository.findOne(1)
                        ),
                        1
                ),
                TestAbundance.create(
                        2,
                        speciesRepository.findOne(1),
                        TestAnalysisEntity.create(
                                2,
                                createDataset(2, "COUN000002"),
                                sampleRepository.findOne(2)
                        ),
                        1
                ),
                TestAbundance.create(
                        3,
                        speciesRepository.findOne(2),
                        TestAnalysisEntity.create(
                                2,
                                createDataset(2, "COUN000002"),
                                sampleRepository.findOne(2)
                        ),
                        1
                ),
                TestAbundance.create(
                        4,
                        speciesRepository.findOne(1),
                        TestAnalysisEntity.create(
                                3,
                                dataset04,
                                sampleRepository.findOne(4)
                        ),
                        1
                ),
                TestAbundance.create(
                        5,
                        speciesRepository.findOne(2),
                        TestAnalysisEntity.create(
                                3,
                                createDataset(3, "COUN000004"),
                                sampleRepository.findOne(4)
                        ),
                        1
                ),
                TestAbundance.create(
                        null,
                        speciesRepository.findOne(1),
                        TestAnalysisEntity.create(
                                null,
                                createDataset(null, "COUN000003"),
                                sampleRepository.findOne(3)
                        ),
                        1
                ),
                TestAbundance.create(
                        null,
                        speciesRepository.findOne(2),
                        TestAnalysisEntity.create(
                                null,
                                createDataset(null, "COUN000003"),
                                sampleRepository.findOne(3)
                        ),
                        1
                )
        ));
        Dataset dataset04PossiblyUpdated;
        if(!allowDatasetUpdate){
            dataset04PossiblyUpdated =
                    TestDataset.create(
                            null,
                            "COUN000004",
                            palaeoentomology,
                            bugsDataset,
                            defaultDataType,
                            dataset04
                    );
        } else {
            dataset04PossiblyUpdated = dataset04;
        }
        expectedAbundances.add(TestAbundance.create(
                null,
                speciesRepository.findOne(3),
                TestAnalysisEntity.create(
                        null,
                        dataset04PossiblyUpdated,
                        sampleRepository.findOne(4)
                ),
                1
        ));
        return expectedAbundances;
    }

    private Dataset createDataset(Integer datasetId, String datasetName){
        return TestDataset.create(
                datasetId,
                datasetName,
                palaeoentomology,
                bugsDataset,
                defaultDataType
                );
    }

    @Override
    public List<Abundance> getActualData() {
        return abundanceRepository.findAll();
    }

    @Override
    public Comparator<Abundance> getSorter() {
        return new AbundanceComparator();
    }

    @Override
    public TestEqualityHelper<Abundance> getEqualityHelper() {
        TestEqualityHelper<Abundance> equalityHelper = new TestEqualityHelper<>();
        equalityHelper.addMethodInformation(new TestEqualityHelper.ClassMethodInformation(SeadSite.class, "getSiteLocations"));
        equalityHelper.addMethodInformation(new TestEqualityHelper.ClassMethodInformation(Dataset.class, "getContacts"));
        return equalityHelper;
    }

    @Override
    public boolean useEqualityHelper(Abundance expected, Abundance actual) {
        return true;
    }

    private abstract static class LoggableEntityComparator<SeadType extends LoggableEntity> implements Comparator<SeadType>{

        @Override
        public final int compare(SeadType o1, SeadType o2) {
            if(o1 == null && o2 != null){
                return 1;
            } else if(o1 != null && o2 == null){
                return -1;
            } else if(o1 == null && o2 == null){
                return 0;
            }
            if(o1.getId() == null && o2.getId() != null){
                return 1;
            } else if(o1.getId() != null && o2.getId() == null){
                return -1;
            } else if(o1.getId() != null && o2.getId() != null){
                return o1.getId().compareTo(o2.getId());
            } else {
                return compareByValue(o1, o2);
            }
        }

        protected abstract int compareByValue(SeadType o1, SeadType o2);
    }

    private static class AbundanceComparator extends LoggableEntityComparator<Abundance> {

        private AnalysisEntityComparator aeComparator = new AnalysisEntityComparator();
        private TaxaSpeciesComparator speciesComparator = new TaxaSpeciesComparator();

        @Override
        protected int compareByValue(Abundance o1, Abundance o2) {
            int aeDifference = aeComparator.compare(o1.getAnalysisEntity(), o2.getAnalysisEntity());
            if(aeDifference == 0){
                return speciesComparator.compare(o1.getSpecies(), o2.getSpecies());
            } else {
                return aeDifference;
            }
        }
    }

    private static class AnalysisEntityComparator extends LoggableEntityComparator<AnalysisEntity> {

        private SampleComparator sampleComparator = new SampleComparator();

        @Override
        protected int compareByValue(AnalysisEntity o1, AnalysisEntity o2) {
            return sampleComparator.compare(o1.getSample(), o2.getSample());
        }
    }

    private static class SampleComparator extends LoggableEntityComparator<Sample> {
        @Override
        protected int compareByValue(Sample o1, Sample o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    private static class TaxaSpeciesComparator extends LoggableEntityComparator<TaxaSpecies>{
        @Override
        protected int compareByValue(TaxaSpecies o1, TaxaSpecies o2) {
            return o1.getSpeciesName().compareTo(o2.getSpeciesName());
        }
    }
}
