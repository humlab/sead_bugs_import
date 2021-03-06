package se.sead.ecocodedefinition.bugs;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import se.sead.bugsimport.ecocodedefinition.bugs.BugsDefinitionImporter;
import se.sead.bugsimport.ecocodedefinition.bugs.bugsmodel.EcoDefBugs;
import se.sead.bugsimport.ecocodedefinition.seadmodel.EcocodeDefinition;
import se.sead.repositories.BugsErrorRepository;
import se.sead.repositories.BugsTraceRepository;
import se.sead.repositories.EcocodeDefinitionRepository;
import se.sead.repositories.EcocodeGroupRepository;
import se.sead.testutils.BugsTracesAndErrorsVerification;
import se.sead.testutils.DatabaseContentVerification;
import se.sead.testutils.DefaultConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class BugsDefinitionImporterTest {

    @TestConfiguration
    public static class Config extends DefaultConfig {
        public Config(){
            super("ecocodedefinition/bugs", "ecocodedefinition.mdb", "bugsecocodedefinition.sql");
        }
    }

    @Autowired
    private EcocodeGroupRepository groupRepository;
    @Autowired
    private EcocodeDefinitionRepository definitionRepository;
    @Autowired
    private BugsTraceRepository traceRepository;
    @Autowired
    private BugsErrorRepository errorRepository;

    @Autowired
    private BugsDefinitionImporter importer;

    @Test
    public void run(){
        DatabaseContentVerification<EcocodeDefinition> databaseContentVerifier = createDatabaseContentVerifier();
        BugsTracesAndErrorsVerification<EcoDefBugs> logVerifier = createLogVerifier();
        importer.run();
        databaseContentVerifier.verifyDatabaseContent();
        logVerifier.verifyTraceContent();
    }

    private DatabaseContentVerification<EcocodeDefinition> createDatabaseContentVerifier(){
        return new DatabaseContentVerification<>(new DatabaseContentProvider(
                groupRepository,
                definitionRepository
        ));
    }

    private BugsTracesAndErrorsVerification<EcoDefBugs> createLogVerifier(){
        return new BugsTracesAndErrorsVerification.ByIdentity<>(
                traceRepository,
                errorRepository,
                new LogVerifier(),
                () -> ExpectedBugsData.EXPECTED_DATA
        );
    }
}
