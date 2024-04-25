package com.github.helendigger.kafkametricsconsumer.repository;

import com.github.helendigger.kafkametricsconsumer.constant.CacheConst;
import com.github.helendigger.kafkametricsconsumer.model.Metric;
import com.github.helendigger.kafkametricsconsumer.model.MetricServiceEntity;
import com.github.helendigger.kafkametricsconsumer.model.MetricType;
import com.github.helendigger.kafkametricsconsumer.view.MetricInfoView;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MetricRepository extends JpaRepository<Metric, Long> {

    @Query("SELECT DISTINCT m.id AS id, m.name AS name, m.metricServiceEntity.name AS serviceName FROM Metric m ORDER BY m.id")
    @Cacheable(cacheNames = CacheConst.ALL_METRICS, key = CacheConst.ALL_METRICS_KEY)
    List<MetricInfoView> getAllMetrics();

    @Cacheable(cacheNames = CacheConst.METRIC, key = "#result.get().id", condition = "#result.present")
    Optional<Metric> findMetricByNameAndMetricTypeAndMetricServiceEntity(String name,
                                                                         MetricType metricType,
                                                                         MetricServiceEntity metricServiceEntity);

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConst.ALL_METRICS, key = CacheConst.ALL_METRICS_KEY),
            @CacheEvict(cacheNames = CacheConst.METRIC, key = "#result.id")
    })
    <S extends Metric> S saveAndFlush(S entity);

    @Override
    @Cacheable(cacheNames = CacheConst.METRIC, key = "#result.get().id", condition = "#result.present")
    Optional<Metric> findById(Long id);
}
