package se.sead.bugsimport.datescalendar.cache;

import se.sead.bugsimport.BugsListSeadMapping;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;

import java.util.Arrays;

class MappingCreator {

    BugsListSeadMapping<DatesCalendar, RelativeDate> createMapping(String sampleCode, String uncertainty){
        return createMapping(sampleCode, uncertainty, "");
    }

    BugsListSeadMapping<DatesCalendar, RelativeDate> createMapping(String sampleCode, String uncertainty, String note){
        DatesCalendar datesCalendar = createDatesCalendar(sampleCode, uncertainty, note);
        DatingUncertainty datingUncertainty = createDatingUncertainty(uncertainty);
        RelativeDate relativeDate = createRelativeDate(datingUncertainty);
        return new BugsListSeadMapping<>(datesCalendar, Arrays.asList(relativeDate));
    }

    private DatesCalendar createDatesCalendar(String sampleCode, String uncertainty, String notes){
        DatesCalendar datesCalendar = new DatesCalendar();
        datesCalendar.setSample(sampleCode);
        datesCalendar.setUncertainty(uncertainty);
        datesCalendar.setNotes(notes);
        return datesCalendar;
    }

    private DatingUncertainty createDatingUncertainty(String uncertainty){
        DatingUncertainty datingUncertainty = new DatingUncertainty();
        datingUncertainty.setName(uncertainty);
        return datingUncertainty;
    }

    private RelativeDate createRelativeDate(DatingUncertainty datingUncertainty){
        RelativeDate relativeDate = new RelativeDate();
        relativeDate.setUncertainty(datingUncertainty);
        return relativeDate;
    }
}
