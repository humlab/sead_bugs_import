package se.sead.sead.model;

import se.sead.repositories.impl.PostEventListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@MappedSuperclass
@EntityListeners({PostEventListener.class})
public abstract class LoggableEntity {

    @Transient
    private List<String> errors;

    public abstract Integer getId();

    public final boolean isNewItem(){
        return getId() == null;
    }

    public List<String> getErrors(){
        if(errors == null){
            return Collections.EMPTY_LIST;
        }
        return errors;
    }

    public boolean isErrorFree(){
        return errors == null;
    }

    public void addError(String error){
        if(errors == null){
            errors = new ArrayList<>();
        }
        errors.add(error);
    }
}
