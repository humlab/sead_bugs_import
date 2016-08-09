package se.sead.bugsimport.mcr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugs.AccessReaderProvider;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.mcr.bugsmodel.BirmBeetleDat;
import se.sead.bugsimport.mcr.bugsmodel.BirmBeetleDatBugsTable;
import se.sead.bugsimport.mcr.seadmodel.BirmBeetleData;
import se.sead.bugsimport.translations.BugsValueTranslationService;

/**
 * Created by erer0001 on 2016-05-12.
 */
@Component
public class BirmBeetleToMCRMapper extends BugsSeadMapper<BirmBeetleDat, BirmBeetleData>{

    @Autowired
    public BirmBeetleToMCRMapper(
            AccessReaderProvider readerProvider,
            BirmBeetleDatToBirmBeetleRowConverter rowConverter,
            BugsValueTranslationService dataTranslationService){
        super(readerProvider.getReader(), new BirmBeetleDatBugsTable(), rowConverter, dataTranslationService);
    }
}
