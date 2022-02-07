package com.ximalaya.commonaspectj;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.webkit.WebView;

import com.ss.android.ugc.bytex.example.R;
import com.ss.android.ugc.bytex.example.webview.WebViewAutoSetWebClient;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Created by luhang on 2019/3/28.
 *
 * @author LuHang
 * Email: luhang@ximalaya.com
 * Tel:15918812121
 */
public class LayoutInflaterAgent {
    String TAG = "LayoutInflaterAgent";

    //  针对LayoutInflater inflate方法进行切面处理
//    @Around("call(public android.view.View android.view.LayoutInflater+.inflate(..)) && target(android.view.LayoutInflater) && !within(androidx.fragment..*)")
//    public Object inflate(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        try {
//            // 开始时间
//            long layoutInflaterStart = SystemClock.uptimeMillis();
//            // 调用inflate方法
//            Object o = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
//            View rootView = null;
//            int id = 0;
//            if (o instanceof View) {
//                rootView = (View) o;
//                if (proceedingJoinPoint.getArgs() != null && proceedingJoinPoint.getArgs().length > 0) {
//                    Object p1 = proceedingJoinPoint.getArgs()[0];
//                    if (p1 instanceof Integer) {
//                        id = (int) p1;
//                    }
//                }
//            }
//            if (rootView != null && id != 0) {
////                rootView.setTag(R.id.common_key_mark_inflate_layout_name, id);
////                Resources resources = rootView.getContext().getResources();
//
////                Log.e(TAG, "\nEntryName:" + resources.getResourceEntryName(id));
//                // 开销时间
//                long cost = SystemClock.uptimeMillis() - layoutInflaterStart;
//                inflateHook(rootView, id, cost);
//                // view layoutid 时间开销
////                LayoutInflaterObserver.layoutInflateDone(rootView, id, cost);
//            }
//            return o;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }

    public static View wrapInflate(LayoutInflater inflater, int id, ViewGroup viewGroup) {
        long layoutInflaterStart = SystemClock.uptimeMillis();
        // 调用inflate方法
        View o = inflater.inflate(id, viewGroup);
        View rootView = null;
        if (o instanceof View) {
            rootView = (View) o;
        }
        if (rootView != null && id != 0) {
            // 开销时间
            long cost = SystemClock.uptimeMillis() - layoutInflaterStart;
            inflateHook(rootView, id, cost);
        }
        return o;
    }

    public static View wrapInflate(LayoutInflater inflater, int id, ViewGroup viewGroup, boolean attachToRoot) {
        long layoutInflaterStart = SystemClock.uptimeMillis();
        // 调用inflate方法
        View o = inflater.inflate(id, viewGroup, attachToRoot);
        View rootView = null;
        if (o instanceof View) {
            rootView = (View) o;
        }
        if (rootView != null && id != 0) {
            // 开销时间
            long cost = SystemClock.uptimeMillis() - layoutInflaterStart;
            inflateHook(rootView, id, cost);
        }
        return o;
    }

    public static View wrapInflate(ViewStub viewStub) {
        Log.i("abc123", "wrapInflate: ");
        System.out.println("onCreate1345");
        View view =  viewStub.inflate();
        handleInflateViews(view);
        return view;
    }

    public static void inflateHook(View view, int layoutId, long cost) {
        if (view == null) {
            return;
        }
        view.setTag(R.id.common_key_mark_inflate_layout_name, layoutId);

        handleInflateViews(view);

//        LayoutInflaterObserver.layoutInflateDone(view, layoutId, cost);
    }

    private static void handleInflateViews(View view) {
        try {
            Set<View> visited = new HashSet<>();
            Queue<View> queue = new LinkedList<>();
            queue.offer(view);
            View tmp;
            while (!queue.isEmpty()) {
                tmp = queue.poll();
                if (!(tmp instanceof ViewGroup)) {
                    continue;
                }
                if (visited.contains(tmp)) {
                    continue;
                }
                visited.add(tmp);
                if (tmp instanceof WebView) {
                    handleWebView((WebView) tmp);
                }
                int children = ((ViewGroup) tmp).getChildCount();
                for (int i = 0; i < children; i++) {
                    tmp = ((ViewGroup) tmp).getChildAt(i);
                    if (tmp != null) {
                        queue.offer(tmp);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleWebView(WebView view) {
        if (view == null || !(view instanceof WebView)) {
            return;
        }
        WebViewAutoSetWebClient.setWebClient(view);
    }

}
