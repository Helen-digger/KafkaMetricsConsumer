package com.github.helendigger.kafkametricsconsumer.repository;

import com.github.helendigger.kafkametricsconsumer.constant.CacheConst;
import com.github.helendigger.kafkametricsconsumer.model.MetricValue;
import com.github.helendigger.kafkametricsconsumer.view.MetricView;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MetricValueRepository extends JpaRepository<MetricValue, Long> {

    @Cacheable(cacheNames = CacheConst.METRIC_INFO_VIEW, key = "args[0]")
    @Query("SELECT SUM(agg.target)/(5*60) as rate, MAX(agg.target) as max FROM " +
            "(SELECT m.value AS target FROM MetricValue m " +
            "WHERE m.timestamp >= ?#{T(java.time.Instant).now().minusSeconds(60*5)} AND m.metric.id = :metricId) as agg")
    Optional<MetricView> getMetricRate(Long metricId);

    @Override
    @CacheEvict(cacheNames = CacheConst.METRIC_INFO_VIEW, key = "args[0].getMetric().getId()")
    <S extends MetricValue> S saveAndFlush(S entity);
}
