package cn.xdean.spring.ex.task.controller;

import cn.xdean.spring.ex.task.model.XTaskRunInfo;
import com.fasterxml.jackson.annotation.JsonValue;

public enum XTaskRunStatusDTO {
    RUNNING("RUNNING", XTaskRunInfo.Status.RUNNING),
    STOP("STOP", XTaskRunInfo.Status.STOP),
    DONE("DONE", XTaskRunInfo.Status.DONE),
    ERROR("ERROR", XTaskRunInfo.Status.ERROR);


    public final String label;
    public final XTaskRunInfo.Status value;

    XTaskRunStatusDTO(String label, XTaskRunInfo.Status value) {
        this.label = label;
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return label;
    }

    public static XTaskRunStatusDTO fromValue(XTaskRunInfo.Status value) {
        for (XTaskRunStatusDTO b : XTaskRunStatusDTO.values()) {
            if (value == b.value) {
                return b;
            }
        }
        return null;
    }
}
