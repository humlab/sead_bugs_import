package se.sead.bugsimport.site.helper;

import org.springframework.stereotype.Component;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;

import java.util.List;

@Component
public class SiteFromCodeAllowDeletedSite extends SiteFromTrace{

    @Override
    protected BugsTrace filteredResults(List<BugsTrace> siteTraces) {
        if(siteTraces.isEmpty()){
            return BugsTrace.NO_TRACE;
        } else {
            return siteTraces.get(0);
        }
    }
}
