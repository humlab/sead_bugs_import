package se.sead.bugsimport.tracing;

import org.springframework.beans.factory.annotation.Autowired;
import se.sead.Application;
import se.sead.bugsimport.tracing.seadmodel.BugsTraceType;
import se.sead.sead.model.LoggableEntity;

import javax.persistence.*;
import java.util.Date;

public class PostEventListener {

    @Autowired
    private TraceEventManager traceEventManager;

    @PrePersist
    @PreUpdate
    public void onPrePersistAndPreUpdate(Object entity){
        if(entity instanceof LoggableEntity){
            ((LoggableEntity)entity).setDateUpdated(new Date());
        }
    }

    @PostPersist
    public void onPersist(Object entity){
        addEvent(entity, BugsTraceType.INSERT);
    }

    private void addEvent(Object entity, BugsTraceType type){
        ensureAutowire();
        if(entity instanceof LoggableEntity){
            traceEventManager.addEvent((LoggableEntity) entity,type);
        }
    }

    private void ensureAutowire(){
        Application.LazyAutowireHelper.getInstance().autowire(this, this.traceEventManager);
    }

    @PostUpdate
    public void onUpdate(Object entity){
        addEvent(entity, BugsTraceType.UPDATE);
    }

    @PostRemove
    public void onDelete(Object entity){
        addEvent(entity, BugsTraceType.DELETE);
    }
}
