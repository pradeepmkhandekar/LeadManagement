package com.pb.leadmanagement.core;

import com.android.chemistlead.core.APIResponse;

/**
 * Created by Rajeev Ranjan on 22/01/2018.
 */

public interface IResponseSubcriber {

    void OnSuccess(APIResponse response, String message);

    void OnFailure(String error);
}