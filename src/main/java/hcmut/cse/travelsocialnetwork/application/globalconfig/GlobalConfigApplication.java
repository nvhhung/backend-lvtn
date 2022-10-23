package hcmut.cse.travelsocialnetwork.application.globalconfig;

import hcmut.cse.travelsocialnetwork.command.globalconfig.CommandGlobalConfig;
import hcmut.cse.travelsocialnetwork.model.GlobalConfig;
import hcmut.cse.travelsocialnetwork.repository.globalconfig.IGlobalConfigRepository;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import hcmut.cse.travelsocialnetwork.utils.CustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author : hung.nguyen23
 * @since : 10/23/22 Sunday
 **/
@Component
public class GlobalConfigApplication implements IGlobalConfigApplication {
    private static final Logger log = LogManager.getLogger(GlobalConfigApplication.class);
    IGlobalConfigRepository globalConfigRepository;

    public GlobalConfigApplication(IGlobalConfigRepository globalConfigRepository) {
        this.globalConfigRepository = globalConfigRepository;
    }


    @Override
    public Optional<GlobalConfig> createGlobalConfig(CommandGlobalConfig commandGlobalConfig) throws Exception {
        var globalConfig = GlobalConfig.builder()
            .key(commandGlobalConfig.getKey())
            .value(commandGlobalConfig.getValue())
            .build();
        return globalConfigRepository.add(globalConfig);
    }

    @Override
    public Optional<Boolean> deleteGlobalConfig(CommandGlobalConfig commandGlobalConfig) throws Exception {
        var globalConfig = globalConfigRepository.get(new Document("key", commandGlobalConfig.getKey()));
        if (globalConfig.isEmpty()) {
            log.info("not found global config have key: " + commandGlobalConfig.getKey());
            throw new CustomException("not found global config");
        }
        return globalConfigRepository.delete(globalConfig.get().get_id().toString());
    }

    @Override
    public Optional<List<GlobalConfig>> loadGlobalConfig(CommandGlobalConfig commandGlobalConfig) throws Exception {
        var query = new Document();
        var globalConfigList = globalConfigRepository.search(query,new Document(Constant.FIELD_QUERY.CREATE_TIME, -1), commandGlobalConfig.getPage(), commandGlobalConfig.getSize());
        if (globalConfigList.isEmpty()) {
            return Optional.ofNullable(new ArrayList<>());
        }
        return globalConfigList;
    }

    @Override
    public Optional<GlobalConfig> loadByKey(CommandGlobalConfig commandGlobalConfig) {
        var globalConfig = globalConfigRepository.get(new Document("key", commandGlobalConfig.getKey()));
        if (globalConfig.isEmpty()) {
            log.info("not found global config have key: " + commandGlobalConfig.getKey());
            return Optional.empty();
        }
        return globalConfig;
    }

    @Override
    public Optional<GlobalConfig> updateGlobalConfig(CommandGlobalConfig commandGlobalConfig) throws Exception {
        var globalConfig = globalConfigRepository.get(new Document("key", commandGlobalConfig.getKey()));
        if (globalConfig.isEmpty()) {
            log.info("not found global config have key: " + commandGlobalConfig.getKey());
            throw new CustomException("not found global config");
        }
        Optional.ofNullable(commandGlobalConfig.getKey()).ifPresent(keyUpdate -> globalConfig.get().setKey(keyUpdate));
        Optional.ofNullable(commandGlobalConfig.getValue()).ifPresent(valueUpdate -> globalConfig.get().setValue(valueUpdate));
        return globalConfigRepository.update(globalConfig.get().get_id().toString(), globalConfig.get());
    }
}
