package com.github.helendigger.kafkametricsconsumer.repository;

import com.github.helendigger.kafkametricsconsumer.constant.CacheConst;
import com.github.helendigger.kafkametricsconsumer.model.MetricServiceEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceRepository extends JpaRepository<MetricServiceEntity, Long> {

    @Cacheable(cacheNames = CacheConst.METRIC_SERVICE, key = "args[0]")
    Optional<MetricServiceEntity> findByName(String name);

    @CacheEvict(cacheNames = CacheConst.METRIC_SERVICE, key = "args[0].getName()")
    @Override
    <S extends MetricServiceEntity> S saveAndFlush(S entity);

}
