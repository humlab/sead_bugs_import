package se.sead.bugsimport.specieskeys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.specieskeys.bugsmodel.Keys;
import se.sead.bugsimport.specieskeys.seadmodel.TextIdentificationKeys;
import se.sead.repositories.TextIdentificationKeysRepository;

@Component
public class IdentificationKeysPersister extends Persister<Keys, TextIdentificationKeys> {

    @Autowired
    private TextIdentificationKeysRepository keysRepository;

    @Override
    protected TextIdentificationKeys save(TextIdentificationKeys seadValue) {
        return keysRepository.saveOrUpdate(seadValue);
    }
}
