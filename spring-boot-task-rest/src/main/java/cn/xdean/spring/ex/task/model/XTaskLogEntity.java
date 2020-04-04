package cn.xdean.spring.ex.task.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task_logs", indexes = @Index(name = "task_run_idx", columnList = "task_id, run_id"))
public class XTaskLogEntity {
    public enum Type {
        ERROR,
        WARN,
        INFO,

        START,
        DONE,
        DONE_ERROR,
        STOP;

        public static List<Type> userTypes() {
            return Arrays.asList(ERROR, WARN, INFO);
        }

        public static List<Type> systemTypes() {
            return Arrays.asList(START, DONE, DONE_ERROR, STOP);
        }
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "task_id", nullable = false)
    private String taskId;

    @Column(name = "run_id", nullable = false)
    private int runId;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Builder.Default
    @Column(name = "time", nullable = false)
    private long time = System.currentTimeMillis();

    @Lob
    @Builder.Default
    @Column(name = "message", nullable = false)
    private String message = "";
}
