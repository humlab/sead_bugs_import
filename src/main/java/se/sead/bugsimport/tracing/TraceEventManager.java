package se.sead.bugsimport.tracing;

import org.springframework.stereotype.Component;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.bugsimport.tracing.seadmodel.BugsTraceType;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TraceEventManager {

    private List<BugsTrace> tracesForInsertions;

    public TraceEventManager(){
        reset();
    }

    public void reset(){
        tracesForInsertions = Collections.EMPTY_LIST;
    }

    public List<BugsTrace> getTracesAndReset(){
        List<BugsTrace> copy = this.tracesForInsertions;
        reset();
        return copy;
    }

    private void initEventList(){
        if(tracesForInsertions == Collections.EMPTY_LIST){
            tracesForInsertions = new ArrayList<>();
        }
    }

    public synchronized void addEvent(LoggableEntity entity, BugsTraceType type){
        initEventList();
        String tableName = getTableNameFromClassAnnotation(entity);
        BugsTrace trace = new BugsTrace();
        trace.setSeadTable(tableName);
        trace.setSeadId(entity.getId());
        trace.setType(type);
        addTrace(trace);
    }

    public static String getTableNameFromClassAnnotation(Object entity) {
        Class entityClass = entity.getClass();
        Annotation annotation = entityClass.getAnnotation(Table.class);
        if(annotation instanceof Table){
            return ((Table) annotation).name();
        }
        throw new IllegalStateException("No table name defined for entity: " + entity);
    }

    private void addTrace(BugsTrace trace){
        if(tracesContainInsertForSameItem(trace) && isUpdated(trace)){
            return;
        }
        if(tracesContainUpdateForSameItem(trace) && isInsert(trace)){
            updateTraceTypeToInsert(trace);
        }
        tracesForInsertions.add(trace);
    }

    private boolean isUpdated(BugsTrace trace) {
        return trace.getType() == BugsTraceType.UPDATE;
    }

    private boolean isInsert(BugsTrace trace) {
        return trace.getType() == BugsTraceType.INSERT;
    }

    private boolean tracesContainInsertForSameItem(BugsTrace trace){
        return tracesForInsertions.stream().anyMatch(
            storedTrace ->
                    sameEntity(storedTrace, trace)
                    && storedTrace.getType() == BugsTraceType.INSERT);
    }

    private static boolean sameEntity(BugsTrace storedTrace, BugsTrace suppliedTrace){
        boolean result = storedTrace.getSeadId().equals(suppliedTrace.getSeadId()) &&
                storedTrace.getSeadTable().equals(suppliedTrace.getSeadTable());
        return result;
    }

    private boolean tracesContainUpdateForSameItem(BugsTrace trace){
        return !getUpdateTraces(trace).isEmpty();
    }

    private List<BugsTrace> getUpdateTraces(BugsTrace trace) {
        return tracesForInsertions.stream()
                .filter(storedTrace ->
                        sameEntity(storedTrace, trace)
                        && storedTrace.getType() == BugsTraceType.UPDATE
                ).collect(Collectors.toList());
    }

    private void updateTraceTypeToInsert(BugsTrace trace){
        getUpdateTraces(trace).get(0).setType(BugsTraceType.INSERT);
    }
}
