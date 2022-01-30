package com.xmly.layoutinflater.plugin;

import com.android.build.gradle.AppExtension;
import com.ss.android.ugc.bytex.common.CommonPlugin;
import com.ss.android.ugc.bytex.common.visitor.ClassVisitorChain;
import com.ss.android.ugc.bytex.pluginconfig.anno.PluginConfig;
import com.xmly.layoutinflater.plugin.visitors.FindLayoutInflaterCallClassVisitor;
import com.xmly.layoutinflater.plugin.visitors.LayoutInflaterHookClassVisitor;

import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created by luhang on 2021/3/17.
 *
 * @author LuHang
 * Email: luhang@ximalaya.com
 * Tel:15918812121
 */
@PluginConfig(value = "layout-inflater-plugin")
public class LayoutInflaterPlugin extends CommonPlugin<LayoutInflaterExtension, LayoutInflaterContext> {
    @Override
    protected LayoutInflaterContext getContext(Project project, AppExtension android, LayoutInflaterExtension layoutInflaterExtension) {
        return new LayoutInflaterContext(project, android, layoutInflaterExtension);
    }

//    @Override
//    public void traverse(@NotNull String relativePath, @NotNull ClassVisitorChain chain) {
//        super.traverse(relativePath, chain);
//        if (!context.isWhiteList(relativePath) /*&& relativePath.startsWith("com/ss/android/ugc/bytex/example/TestLayoutInflater")*/) {
//            chain.connect(new FindLayoutInflaterCallClassVisitor(context));
//        }
//    }

    @Override
    public boolean transform(@NotNull String relativePath, @NotNull ClassVisitorChain chain) {
        if (!context.isWhiteList(relativePath)) {
            chain.connect(new LayoutInflaterHookClassVisitor(context));
        }
        return super.transform(relativePath, chain);
    }
}
