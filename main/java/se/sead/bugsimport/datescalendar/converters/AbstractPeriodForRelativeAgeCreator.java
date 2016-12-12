package se.sead.bugsimport.datescalendar.converters;

import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.periods.bugsmodel.Period;

public abstract class AbstractPeriodForRelativeAgeCreator {

    private static final String STANDARD_NOTES = "Autocreated from bugs import";

    protected DatesCalendar datesCalendar;
    protected String computedAbbreviation;

    public AbstractPeriodForRelativeAgeCreator(DatesCalendar datesCalendar, String computedAbbreviation) {
        this.datesCalendar = datesCalendar;
        this.computedAbbreviation = computedAbbreviation;
    }

    public Period create(){
        Period period = new Period();
        period.setBegin(getStart());
        period.setEnd(getStop());
        period.setBeginBCad(datesCalendar.getBcadbp());
        period.setEndBCad(datesCalendar.getBcadbp());
        period.setType(getType());
        period.setDesc(STANDARD_NOTES);
        period.setPeriodCode(computedAbbreviation);
        return period;
    }

    protected abstract Integer getStart();

    protected abstract Integer getStop();

    protected abstract String getType();
}
