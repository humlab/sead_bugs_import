package se.sead;

import se.sead.bugs.AccessDatabase;
import se.sead.bugs.AccessReader;
import se.sead.bugs.BugsTable;
import se.sead.bugs.TraceableBugsData;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class AccessReaderTest<T extends TraceableBugsData> {

    public static final String RESOURCE_FOLDER = "./src/test/resources/";

    private String baseDirectory;

    protected AccessReaderTest(String baseDirectory){
        if(!baseDirectory.startsWith(RESOURCE_FOLDER)){
            if(baseDirectory.startsWith("/")){
                baseDirectory = baseDirectory.substring(0, baseDirectory.lastIndexOf('/'));
            }
            baseDirectory = RESOURCE_FOLDER + baseDirectory;
        }
        if(!baseDirectory.endsWith("/")){
            baseDirectory = baseDirectory + "/";
        }
        this.baseDirectory = baseDirectory;
    }

    private void readTable(AccessDatabase accessDatabase, BugsTable<T> bugsTable, List<T> expectedResults, Comparator<T> resultSorter){
        List<T> readItems = readItems(accessDatabase, bugsTable);
        if(resultSorter != null){
            Collections.sort(expectedResults, resultSorter);
            Collections.sort(readItems, resultSorter);
        }
        try {
            assertEquals(expectedResults, readItems);
        } catch (AssertionError ae){
            logData(expectedResults, readItems);
            throw ae;
        }
    }

    private List<T> readItems(AccessDatabase accessDatabase, BugsTable<T> bugsTable){
        AccessReader reader = new AccessReader(accessDatabase);
        return reader.read(bugsTable);
    }

    private void logData(List<T> expectedResults, List<T> readItems) {
        for (int i = 0; i < Math.max(expectedResults.size(), readItems.size()); i++) {
            printErrorData(expectedResults, i);
            printErrorData(readItems, i);
        }
    }

    private void printErrorData(List<T> expectedResults, int i) {
        try {
            System.out.println(expectedResults.get(i));
        } catch (IndexOutOfBoundsException ioobe){
            System.out.println("No element at index: " + i);
        }
    }

    protected void readTableFromDefaultFolder(String testMDBFile, BugsTable<T> bugsTable, List<T> expectedResults){
        readTable(createReader(testMDBFile), bugsTable, expectedResults, null);
    }

    private AccessDatabase createReader(String mdbFile){
        return new AccessDatabase(baseDirectory + mdbFile);
    }

    protected void readTableFromDefaultFolder(String testMDBFile, BugsTable<T> bugsTable, List<T> expectedResults, Comparator<T> resultSorter){
        readTable(createReader(testMDBFile), bugsTable, expectedResults, resultSorter);
    }
}