package com.erp.sheelafoam.interfaces;

import com.erp.sheelafoam.model.GuaranteeLogModel;

import java.util.List;

/**
 * Created by E5956 on 4/24/2018.
 */

public interface INotifyGuaranteeLog {
    public void onServiceResponse(List<GuaranteeLogModel> guaranteeLogModelList);
}
