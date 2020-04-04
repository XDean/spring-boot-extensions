package cn.xdean.spring.ex.task.dao;

import cn.xdean.spring.ex.task.model.XTaskLogEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface XTaskLogRepository extends JpaRepository<XTaskLogEntity, Integer> {
    Optional<XTaskLogEntity> findFirstByTaskIdOrderByRunIdDesc(String taskId);

    List<XTaskLogEntity> findAllByTaskIdAndTypeOrderByIdAsc(String taskId, XTaskLogEntity.Type type, Pageable pageable);

    List<XTaskLogEntity> findAllByTaskIdAndRunIdAndTypeInOrderByIdAsc(String taskId, int runId, Collection<XTaskLogEntity.Type> type);

    boolean existsByTaskIdAndRunId(String taskId, int runId);

    List<XTaskLogEntity> findAllByTimeBeforeAndTypeIs(long time, XTaskLogEntity.Type type);

    List<XTaskLogEntity> findAllByTypeIs(XTaskLogEntity.Type type);

    void deleteAllByTaskIdAndRunId(String taskId, int runId);
}
