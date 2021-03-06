package se.sead.bugsimport.periods.converters.age;

import java.math.BigDecimal;

interface AgeConversion {
    boolean handleValue(String bcad, Integer bugsData);
    BigDecimal convert(String bcad, Integer bugsData);
}
