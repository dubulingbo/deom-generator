package edu.dublbo.generator.config;

import edu.dublbo.generator.common.utils.SnowflakeIdWorker;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author DubLBo
 * @since 2020-09-05 14:52
 * i believe i can i do
 */
@Configuration
public class DbiConfig {

    private static final Logger logger = LoggerFactory.getLogger(DbiConfig.class);

    @Value(value = "${dublbo.dbi.id-strategy.snowflake.machineId}")
    private String machineId;

    @Value(value = "${dublbo.dbi.id-strategy.snowflake.serverId}")
    private String serverId;

    /**
     * 初始化雪花算法的bean
     * @return 雪花算法id生成策略
     */
    @Bean
    public SnowflakeIdWorker generateSnowflakeIdWorker() {

        long machineIdL;
        long serverIdL;

        if (StringUtils.isEmpty(machineId) || !NumberUtils.isDigits(machineId)) {
            machineIdL = 0L;
        } else {
            machineIdL = Long.parseLong(machineId);
        }

        if (StringUtils.isEmpty(serverId) || !NumberUtils.isDigits(serverId)) {
            serverIdL = 0L;
        } else {
            serverIdL = Long.parseLong(serverId);
        }

        logger.info("初始化雪花算法，machineId:{},serverId:{}", machineIdL, serverIdL);
        return new SnowflakeIdWorker(machineIdL, serverIdL);

    }

}
