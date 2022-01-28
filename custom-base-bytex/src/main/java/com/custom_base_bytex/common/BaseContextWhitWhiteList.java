package com.custom_base_bytex.common;

import com.android.build.gradle.AppExtension;
import com.ss.android.ugc.bytex.common.BaseContext;

import org.gradle.api.Project;

/**
 * Created by luhang on 2021/4/1.
 *
 * @author LuHang
 * Email: luhang@ximalaya.com
 * Tel:15918812121
 */
public class BaseContextWhitWhiteList<E extends BaseExtensionWithWhiteList> extends BaseContext<E> {
    public BaseContextWhitWhiteList(Project project, AppExtension android, E extension) {
        super(project, android, extension);
    }

    public boolean isWhiteList(String path) {
        if (path == null) {
            return true;
        }
        if (extension.getWhiteList().size() == 0) {
            return false;
        }

        for (String s : extension.getWhiteList()) {
            if (s.contains("/")) {
                if (path.startsWith(s)) {
                    return true;
                }
            } else {
                if (path.endsWith("s") || path.endsWith("s" + ".class")) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isImplementOf(String path) {
        if (path == null || getClassGraph() == null) {
            return false;
        }

        if (extension.getImplementWhiteList() == null || extension.getImplementWhiteList().size() == 0) {
            return false;
        }

        for (String s : extension.getImplementWhiteList()) {
            if(getClassGraph().implementOf(path, s)) {
                return true;
            }
        }
        return false;
    }


}
