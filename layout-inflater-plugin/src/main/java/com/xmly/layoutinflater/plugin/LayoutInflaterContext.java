package com.xmly.layoutinflater.plugin;

import com.android.build.gradle.AppExtension;
import com.custom_base_bytex.common.BaseContextWhitWhiteList;
import com.xmly.layoutinflater.plugin.model.MethodInfo;
import com.xmly.layoutinflater.plugin.model.TargetMethodIndex;

import org.gradle.api.Project;

import java.util.HashMap;
import java.util.List;

/**
 * Created by luhang on 2021/3/17.
 *
 * @author LuHang
 * Email: luhang@ximalaya.com
 * Tel:15918812121
 */
public class LayoutInflaterContext extends BaseContextWhitWhiteList<LayoutInflaterExtension> {
    private final HashMap<String, TargetMethodIndex> containInflateMethods; // key = classname#methodName#methodDesc

    public static final String INFLATEDESC_1 = "(ILandroid/view/ViewGroup;)Landroid/view/View;";
    public static final String INFLATEDESC_2 = "(ILandroid/view/ViewGroup;Z)Landroid/view/View;";

    public LayoutInflaterContext(Project project, AppExtension android, LayoutInflaterExtension extension) {
        super(project, android, extension);
        containInflateMethods = new HashMap<>(150);
    }


    public boolean isHookedClass(String className) { // 这里的class 都是全限定名
        for (String s : containInflateMethods.keySet()) {
            if (s.startsWith(className + "#")) {
                return true;
            }
        }
        return false;
    }

    public TargetMethodIndex getHookedMethod(String className, String methodName, String desc) {
        String key = className + "#" + methodName + "#" + desc;
        return containInflateMethods.get(key);
    }

    public void addContainInflaterMethod(MethodInfo methodInfo, List<Integer> targetIndex) {
        String key = methodInfo.getClassInfo().getName() + "#" + methodInfo.getName() + "#" + methodInfo.getDesc();
        containInflateMethods.put(key, new TargetMethodIndex(methodInfo, targetIndex));
    }
}
