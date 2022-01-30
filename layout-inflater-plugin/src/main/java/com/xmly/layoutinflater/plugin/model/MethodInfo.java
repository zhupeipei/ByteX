package com.xmly.layoutinflater.plugin.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luhang on 2021/3/22.
 *
 * @author LuHang
 * Email: luhang@ximalaya.com
 * Tel:15918812121
 */
public class MethodInfo {
    private ClassInfo classInfo;
    private int access;
    private String name;
    private String desc;
    private List<String> annotations;

    public MethodInfo(ClassInfo classInfo, int access, String name, String desc) {
        this.classInfo = classInfo;
        this.access = access;
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public int getAccess() {
        return access;
    }

    public void addAnnotation(String annotation) {
        if (annotations == null) {
            annotations = new ArrayList<>();
        }
        annotations.add(annotation);
    }

    public ClassInfo getClassInfo() {
        return classInfo;
    }

    @Override
    public String toString() {
        return "MethodInfo{" +
                "classInfo=" + classInfo +
                ", access=" + access +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", annotations=" + annotations +
                '}';
    }
}

