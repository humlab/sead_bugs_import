package se.sead;

import se.sead.bugs.AccessReader;
import se.sead.bugs.BugsTable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

public abstract class AccessReaderTest<T> {

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

    private void readTable(AccessReader reader, BugsTable<T> bugsTable, List<T> expectedResults, Comparator<T> resultSorter){
        List<T> readItems = reader.read(bugsTable);
        if(resultSorter != null){
            Collections.sort(expectedResults, resultSorter);
            Collections.sort(readItems, resultSorter);
        }
        for (int i = 0; i < expectedResults.size(); i++) {
            System.out.println(expectedResults.get(i));
            System.out.println(readItems.get(i));
        }
        assertEquals(expectedResults, readItems);
    }

    protected void readTableFromDefaultFolder(String testMDBFile, BugsTable<T> bugsTable, List<T> expectedResults){
        readTable(createReader(testMDBFile), bugsTable, expectedResults, null);
    }

    private AccessReader createReader(String mdbFile){
        return new AccessReader(baseDirectory + mdbFile);
    }

    protected void readTableFromDefaultFolder(String testMDBFile, BugsTable<T> bugsTable, List<T> expectedResults, Comparator<T> resultSorter){
        readTable(createReader(testMDBFile), bugsTable, expectedResults, resultSorter);
    }
}