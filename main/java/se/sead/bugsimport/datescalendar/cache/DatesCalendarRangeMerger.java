package se.sead.bugsimport.datescalendar.cache;


import se.sead.bugsimport.MappingResult;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.sead.methods.Method;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DatesCalendarRangeMerger {

    private DatingUncertaintyManager uncertaintyManager;
    private RelativeAgeMerger ageMerger;

    private List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> previouslyMappedRelativeDatesForSample;
    private MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> foundMatchingRelativeDate;

    DatesCalendarRangeMerger(
            DatingUncertaintyManager uncertaintyManager,
            RelativeAgeMerger relativeAgeMerger,
            List<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> previouslyMappedRelativeDatesForSample) {
        this.uncertaintyManager = uncertaintyManager;
        this.ageMerger = relativeAgeMerger;
        this.previouslyMappedRelativeDatesForSample = previouslyMappedRelativeDatesForSample;
    }

    boolean shouldMerge(MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> mapping){
        return previouslyMappedDataIsNotEmpty() &&
                mapping.isErrorFree() &&
                mapping.isNewSeadData() &&
                isTargetUncertainty(mapping) &&
                previouslyMappedDataContainTargetRelativeDate(mapping);
    }

    private boolean isTargetUncertainty(MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> mapping){
        DatingUncertainty uncertainty = extractRelativeDate(mapping).getUncertainty();
        if(uncertainty == null){
            return false;
        } else {
            return uncertaintyManager.isToUncertaintyWithoutCaValidation(uncertainty) ||
                    uncertaintyManager.isFromUncertaintyWithoutCaValidation(uncertainty);
        }
    }

    private boolean previouslyMappedDataIsNotEmpty(){
        return previouslyMappedRelativeDatesForSample != null &&
                !previouslyMappedRelativeDatesForSample.isEmpty();
    }

    private boolean previouslyMappedDataContainTargetRelativeDate(MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> mapping){
        setFoundMatchingRelativeDate(extractRelativeDate(mapping));
        return foundMatchingRelativeDate != null;
    }

    private RelativeDate extractRelativeDate(MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> mapping) {
        return mapping.getSeadData().get(0);
    }

    private void setFoundMatchingRelativeDate(RelativeDate currentDate){
        DatingUncertainty oppositeUncertainty = uncertaintyManager.getOpposite(currentDate.getUncertainty());
        Method currentMethod = currentDate.getDatingMethod();
        Optional<MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate>> foundItem = previouslyMappedRelativeDatesForSample.stream()
                .filter(relativeDate -> matchRelativeDateOnUncertaintyAndDatingMethod(extractRelativeDate(relativeDate), oppositeUncertainty, currentMethod))
                .findFirst();
        if(foundItem.isPresent()){
            foundMatchingRelativeDate = foundItem.get();
        }
    }

    private static boolean matchRelativeDateOnUncertaintyAndDatingMethod(RelativeDate relativeDate, DatingUncertainty uncertainty, Method method){
        return (relativeDate.getUncertainty() != null ?
                relativeDate.getUncertainty().equals(uncertainty) : uncertainty == null )
                &&
                (relativeDate.getDatingMethod() != null ?
                relativeDate.getDatingMethod().equals(method) : method == null);
    }

    void doMerge(MappingResult.BugsListSeadMapping<DatesCalendar, RelativeDate> mapping){
        if(foundMatchingRelativeDate == null){
            return;
        }
        RelativeDate currentRelativeDate = extractRelativeDate(mapping);

        RelativeAge range = ageMerger.createRange(Arrays.asList(foundMatchingRelativeDate, mapping));
        extractRelativeDate(foundMatchingRelativeDate).setRelativeAge(range);
        ageMerger.updateUncertaintyIfNeeded(extractRelativeDate(foundMatchingRelativeDate));
        mergeNotes(currentRelativeDate);
    }

    private void mergeNotes(RelativeDate currentDate){
        if(isEmpty(extractRelativeDate(foundMatchingRelativeDate).getNotes()) && !isEmpty(currentDate.getNotes())){
            extractRelativeDate(foundMatchingRelativeDate).setNotes(currentDate.getNotes());
        }
    }

    private static boolean isEmpty(String notes){
        return notes == null || notes.isEmpty();
    }
}