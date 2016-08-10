package se.sead.bugsimport.siteotherproxies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugs.AccessReader;
import se.sead.bugs.AccessReaderProvider;
import se.sead.bugs.BugsTable;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.siteotherproxies.bugsmodel.SiteOtherProxies;
import se.sead.bugsimport.siteotherproxies.bugsmodel.SiteOtherProxiesBugsTable;
import se.sead.bugsimport.siteotherproxies.converters.SiteOtherProxiesBugsTableConverter;
import se.sead.bugsimport.siteotherproxies.seadmodel.SiteOtherRecord;
import se.sead.bugsimport.translations.BugsValueTranslationService;

@Component
public class SiteOtherProxiesBugsMapper extends BugsSeadMapper<SiteOtherProxies, SiteOtherRecord> {

    @Autowired
    public SiteOtherProxiesBugsMapper(
            AccessReaderProvider accessReaderProvider,
            SiteOtherProxiesBugsTableConverter tableConverter,
            BugsValueTranslationService dataTranslationService) {
        super(
                accessReaderProvider.getReader(),
                new SiteOtherProxiesBugsTable(),
                tableConverter,
                dataTranslationService);
    }
}
