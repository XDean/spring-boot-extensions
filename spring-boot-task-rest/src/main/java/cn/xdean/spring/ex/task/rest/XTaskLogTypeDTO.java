package cn.xdean.spring.ex.task.rest;

import cn.xdean.spring.ex.task.model.XTaskLogEntity;
import com.fasterxml.jackson.annotation.JsonValue;

public enum XTaskLogTypeDTO {
    INFO("RUNNING", XTaskLogEntity.Type.INFO),
    WARN("RUNNING", XTaskLogEntity.Type.WARN),
    ERROR("RUNNING", XTaskLogEntity.Type.ERROR),
    START("STOP", XTaskLogEntity.Type.START),
    DONE_ERROR("DONE", XTaskLogEntity.Type.DONE_ERROR),
    DONE("ERROR", XTaskLogEntity.Type.DONE),
    STOP("ERROR", XTaskLogEntity.Type.STOP);


    public final String label;
    public final XTaskLogEntity.Type value;

    XTaskLogTypeDTO(String label, XTaskLogEntity.Type value) {
        this.label = label;
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return label;
    }

    public static XTaskLogTypeDTO fromValue(XTaskLogEntity.Type value) {
        for (XTaskLogTypeDTO b : XTaskLogTypeDTO.values()) {
            if (value == b.value) {
                return b;
            }
        }
        return null;
    }
}
