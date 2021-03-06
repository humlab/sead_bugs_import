package se.sead.taxaseasonality;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugsimport.species.converters.TaxonomicOrderConverter;
import se.sead.bugsimport.taxaseasonality.TaxaSeasonalityImporter;
import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdult;
import se.sead.bugsimport.taxaseasonality.seadmodel.TaxaSeasonality;
import se.sead.repositories.*;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

import java.util.Comparator;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class TaxaSeasonalityImportTest {

    @TestConfiguration
    public static class Config extends DefaultConfig {
        public Config(){
            super("taxaseasonality");
        }
    }

    @Autowired
    private SeasonRepository seasonRepository;
    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private ActivityTypeRepository activityTypeRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;
    @Autowired
    private TaxaSeasonalityRepository seasonalityRepository;
    @Autowired
    private TaxonomicOrderConverter orderConverter;
    @Autowired
    private SeasonTypeRepository seasonTypeRepository;

    @Autowired
    private TaxaSeasonalityImporter importer;

    private ImportTestDefinition testDefinition;
    private BugsTracesAndErrorsVerification<SeasonActiveAdult> tracesAndErrorsVerification;
    private DatabaseContentVerification<TaxaSeasonality> databaseContentVerification;

    @Test
    public void run(){
        createTestDefinition();
        importer.run();
        setupDatabaseContentVerification();
        setupTracesAndErrorsVerification();
        databaseContentVerification.verifyDatabaseContent();
        tracesAndErrorsVerification.verifyTraceContent();
    }

    private void createTestDefinition() {
        testDefinition = new ImportTestDefinition(
                activityTypeRepository.findAdultActiveType(),
                seasonTypeRepository.getBySeasonType("Month"),
                seasonRepository,
                speciesRepository,
                locationRepository);
    }

    private void setupDatabaseContentVerification(){
        databaseContentVerification = new DatabaseContentVerification<>(
            new TaxaSeasonalityDatabaseContentProvider(testDefinition, seasonalityRepository, orderConverter)
        );
    }

    private void setupTracesAndErrorsVerification(){
        tracesAndErrorsVerification = new BugsTracesAndErrorsVerification.ByIdentity<>(
                traceRepository,
                errorRepository,
                new TaxaSeasonalityTracesAndErrorsVerification(),
                () -> ImportTestDefinition.EXPECTED_BUGS_DATA
        );
    }

    private static class TaxaSeasonalityDatabaseContentProvider
            implements
            DatabaseContentVerification.DatabaseContentTestDataProvider<TaxaSeasonality>,
            Comparator<TaxaSeasonality>{

        private ImportTestDefinition testDefinition;
        private TaxaSeasonalityRepository seasonalityRepository;
        private TaxonomicOrderConverter orderConverter;

        public TaxaSeasonalityDatabaseContentProvider(ImportTestDefinition testDefinition, TaxaSeasonalityRepository seasonalityRepository, TaxonomicOrderConverter orderConverter) {
            this.testDefinition = testDefinition;
            this.seasonalityRepository = seasonalityRepository;
            this.orderConverter = orderConverter;
        }

        @Override
        public List<TaxaSeasonality> getExpectedData() {
            return testDefinition.getExpectedData();
        }

        @Override
        public List<TaxaSeasonality> getActualData() {
//            List<TaxaSeasonality> seasonalities = new ArrayList<>();
//            List<Double> bugsCodes = ImportTestDefinition.EXPECTED_BUGS_DATA.stream()
//                    .map(bugsData -> bugsData.getCode())
//                    .distinct()
//                    .collect(Collectors.toList());
//            for (Double bugsCode :
//                    bugsCodes) {
//                TaxaSpecies bugsSpecies = orderConverter.convertToSeadType(bugsCode).getSpecies();
//                seasonalities.addAll(seasonalityRepository.findAllBySpecies(bugsSpecies));
//            }
//            return seasonalities;
            return seasonalityRepository.findAll();
        }

        @Override
        public Comparator<TaxaSeasonality> getSorter() {
            return this;
        }

        @Override
        public int compare(TaxaSeasonality o1, TaxaSeasonality o2) {
            int comparisonResult = o1.getSpecies().getId().compareTo(o2.getSpecies().getId());
            if(comparisonResult == 0){
                comparisonResult = o1.getLocation().getName().compareTo(o2.getLocation().getName());
                if(comparisonResult == 0){
                    comparisonResult = o1.getSeason().getName().compareTo(o2.getSeason().getName());
                }
            }
            return comparisonResult;
        }
    }
}
