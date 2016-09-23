package se.sead.testutils;

import se.sead.AccessReaderTest;
import se.sead.ApplicationConfiguration;

public abstract class DefaultConfig implements ApplicationConfiguration{

    private final String mdbFile;
    private final String dataFile;

    protected DefaultConfig(String mdbFile, String dataFile){
        this("", mdbFile, dataFile);
    }

    protected DefaultConfig(String subDirectory, String mdbFile, String dataFile){
        this(AccessReaderTest.RESOURCE_FOLDER, subDirectory, mdbFile, dataFile);
    }

    protected DefaultConfig(String resourcePath, String subDirectory, String mdbFile, String dataFile){
        this.mdbFile = pathBuilder(resourcePath, subDirectory, mdbFile);
        this.dataFile = pathBuilder(resourcePath, subDirectory, dataFile);
    }

    private static String pathBuilder(String... pathItems){
        return String.join("/", pathItems)
                .replaceAll("//", "/");
    }

    protected String getMdbFile() {
        return mdbFile;
    }

    protected String getDataFile() {
        return dataFile;
    }
}