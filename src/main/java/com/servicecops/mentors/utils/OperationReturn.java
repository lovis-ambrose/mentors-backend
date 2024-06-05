package com.servicecops.mentors.utils;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OperationReturn {
    Integer returnCode;
    String returnMessage;

    public void setReturnCodeAndReturnMessage(Integer returnCode, String returnMessage) {
        this.returnCode = returnCode;
    }
}

