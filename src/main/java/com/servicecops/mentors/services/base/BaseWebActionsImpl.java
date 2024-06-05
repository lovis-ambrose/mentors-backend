package com.servicecops.mentors.services.base;

import com.alibaba.fastjson.JSONObject;
import com.servicecops.mentors.utils.OperationReturnObject;

public interface BaseWebActionsImpl {
    public OperationReturnObject switchActions(String action, JSONObject request);
    public OperationReturnObject process(String action, JSONObject request);
}
