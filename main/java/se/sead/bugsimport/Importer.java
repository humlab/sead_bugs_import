package se.sead.bugsimport;

import se.sead.bugs.TraceableBugsData;
import se.sead.sead.model.LoggableEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Importer<BugsType extends TraceableBugsData, SeadType extends LoggableEntity> {

    private BugsSeadMapper<BugsType, SeadType> dataMapper;
    private Persister<BugsType, SeadType> persister;
    private List<Importer<? extends TraceableBugsData,? extends LoggableEntity>> requiredImporters;
    private boolean hasRun = false;

    protected Importer(BugsSeadMapper<BugsType, SeadType> dataMapper, Persister<BugsType, SeadType> persister) {
        this.dataMapper = dataMapper;
        this.persister = persister;
        requiredImporters = Collections.EMPTY_LIST;
    }

    protected Importer(
            BugsSeadMapper<BugsType, SeadType> dataMapper,
            Persister<BugsType, SeadType> persister,
            Importer<? extends TraceableBugsData, ? extends LoggableEntity>... requiredImporters) {
        this.dataMapper = dataMapper;
        this.persister = persister;
        setRequiredImporters(requiredImporters);
    }

    private void setRequiredImporters(Importer<? extends TraceableBugsData,? extends LoggableEntity>... requiredImporters){
        if(requiredImporters != null && requiredImporters.length > 0 && requiredImporters[0] != null){
            this.requiredImporters = Arrays.asList(requiredImporters);
        } else {
            this.requiredImporters = Collections.EMPTY_LIST;
        }
    }

    public void run(){
        if(hasRun){
            return;
        }
        runRequiredImporters();
        mapData();
        saveData();
        hasRun = true;
    }

    private void runRequiredImporters(){
        for (Importer<? extends TraceableBugsData, ? extends LoggableEntity> importer :
                requiredImporters) {
            importer.run();
        }
    }

    private void mapData() {
        dataMapper.importBugsData();
    }

    private void saveData(){
        persister.persist(dataMapper);
    }

    protected void setHasRun(boolean hasRun){
        this.hasRun = hasRun;
    }
}
