package com.custom_base_bytex.common;

import com.ss.android.ugc.bytex.common.BaseExtension;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luhang on 2021/4/1.
 *
 * @author LuHang
 * Email: luhang@ximalaya.com
 * Tel:15918812121
 */
public abstract class BaseExtensionWithWhiteList extends BaseExtension {
    private List<String> whiteList = new ArrayList<>();

    private List<String> implementWhiteList = new ArrayList<>();

    public void setWhiteList(List<String> whiteList) {
        this.whiteList = whiteList;
    }

    public List<String> getWhiteList() {
        return whiteList;
    }

    public List<String> getImplementWhiteList() {
        return implementWhiteList;
    }

    public void setImplementWhiteList(List<String> implementWhiteList) {
        this.implementWhiteList = implementWhiteList;
    }
}
