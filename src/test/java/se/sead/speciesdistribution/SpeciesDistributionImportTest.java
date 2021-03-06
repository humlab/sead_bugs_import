package se.sead.speciesdistribution;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.speciesdistribution.SpeciesDistributionImporter;
import se.sead.bugsimport.speciesdistribution.bugsmodel.Distrib;
import se.sead.bugsimport.speciesdistribution.seadmodel.TextDistribution;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.model.TestEqualityHelper;
import se.sead.repositories.*;
import se.sead.testutils.DefaultConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class SpeciesDistributionImportTest {

    @TestConfiguration
    public static class Config extends DefaultConfig {
        public Config(){
            super("speciesdistribution");
        }
    }

    @Autowired
    private TaxaOrderRepository orderRepository;
    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private TextDistributionRepository distributionRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    @Autowired
    private SpeciesDistributionImporter importer;

    private SpeciesDistributionTestDefinition testDefinition;
    private SpeciesBuilder speciesBuilder;

    @Before
    public void setup(){
        speciesBuilder = new SpeciesBuilder(orderRepository.getImportOrder());
        testDefinition = new SpeciesDistributionTestDefinition(speciesBuilder);
    }

    @Test
    public void importTest(){
        importer.run();
        verifyDatabaseContent();
        verifyTraces();
        verifyErrors();
    }

    private void verifyDatabaseContent() {
        List<TextDistribution> actualResults = new ArrayList<>();
        for (TaxaSpecies species :
                speciesBuilder.getSpecies()) {
            TaxaSpecies dbSpecies = speciesRepository.findByName(species.getSpeciesName(), species.getGenus().getGenusName());
            actualResults.addAll(distributionRepository.findBySpecies(dbSpecies));
        }

        compare(testDefinition.getExpectedResults(), actualResults);
    }

    private void compare(List<TextDistribution> expectedResults, List<TextDistribution> actualResults){
        assertEquals(expectedResults.size(), actualResults.size());
        TestTextDistributionComparator comparator = new TestTextDistributionComparator();
        Collections.sort(expectedResults, comparator);
        Collections.sort(actualResults, comparator);
        for (int i = 0; i < expectedResults.size(); i++) {
            assertTrue(TestEqualityHelper.equalsWithoutIdIfNeeded(expectedResults.get(i), actualResults.get(i)));
        }
    }

    private void verifyTraces() {
        for (Distrib bugsData: SpeciesDistributionTestDefinition.EXPECTED_BUGS_ITEMS){
            List<BugsTrace> traces = traceRepository.findByBugsTableAndAccessInformationData("TDistrib", bugsData.getCompressedStringBeforeTranslation());
            testDefinition.assertTraces(traces, bugsData);
        }
    }

    private void verifyErrors() {
        for (Distrib bugsData: SpeciesDistributionTestDefinition.EXPECTED_BUGS_ITEMS){
            List<BugsError> errors = errorRepository.findByBugsTableAndAccessInformationData("TDistrib", bugsData.getCompressedStringBeforeTranslation());
            testDefinition.assertErrors(errors, bugsData);
        }
    }

    private static class TestTextDistributionComparator implements Comparator<TextDistribution>{
        @Override
        public int compare(TextDistribution o1, TextDistribution o2) {
            return o1.getDistribution().compareTo(o2.getDistribution());
        }
    }
}
