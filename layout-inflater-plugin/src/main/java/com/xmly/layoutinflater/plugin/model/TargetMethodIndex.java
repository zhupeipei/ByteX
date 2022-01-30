package com.xmly.layoutinflater.plugin.model;

import java.util.List;

/**
 * Created by luhang on 2021/3/22.
 *
 * @author LuHang
 * Email: luhang@ximalaya.com
 * Tel:15918812121
 */
public class TargetMethodIndex {
    public MethodInfo methodInfo;
    public List<Integer> targetIndex;

    public TargetMethodIndex(MethodInfo methodInfo, List<Integer> targetIndex) {
        this.methodInfo = methodInfo;
        this.targetIndex = targetIndex;
    }
}
