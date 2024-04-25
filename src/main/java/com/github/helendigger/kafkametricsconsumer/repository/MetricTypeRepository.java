package com.github.helendigger.kafkametricsconsumer.repository;

import com.github.helendigger.kafkametricsconsumer.constant.CacheConst;
import com.github.helendigger.kafkametricsconsumer.model.MetricType;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MetricTypeRepository extends JpaRepository<MetricType, Long> {

    @Cacheable(cacheNames = CacheConst.METRIC_TYPE, key = "args[0]")
    Optional<MetricType> findMetricTypeByName(String name);

    @CacheEvict(cacheNames = CacheConst.METRIC_TYPE, key = "args[0].getName()")
    @Override
    <S extends MetricType> S saveAndFlush(S entity);
}
