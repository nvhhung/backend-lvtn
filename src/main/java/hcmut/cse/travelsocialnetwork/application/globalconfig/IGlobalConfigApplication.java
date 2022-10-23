package hcmut.cse.travelsocialnetwork.application.globalconfig;

import hcmut.cse.travelsocialnetwork.command.globalconfig.CommandGlobalConfig;
import hcmut.cse.travelsocialnetwork.model.GlobalConfig;

import java.util.List;
import java.util.Optional;

/**
 * @author : hung.nguyen23
 * @since : 10/23/22 Sunday
 **/
public interface IGlobalConfigApplication {
    Optional<GlobalConfig> createGlobalConfig(CommandGlobalConfig commandGlobalConfig) throws Exception;
    Optional<Boolean> deleteGlobalConfig(CommandGlobalConfig commandGlobalConfig) throws Exception;
    Optional<List<GlobalConfig>> loadGlobalConfig(CommandGlobalConfig commandGlobalConfig) throws Exception;
    Optional<GlobalConfig> updateGlobalConfig(CommandGlobalConfig commandGlobalConfig) throws Exception;
    Optional<GlobalConfig> loadByKey(CommandGlobalConfig commandGlobalConfig);
}
