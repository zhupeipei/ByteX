package org.aire;

import android.util.Log;
import android.view.ViewGroup;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author ZhuPeipei
 * @date 2021/11/16 15:15
 */
public class FixWebViewClient extends WebViewClient {
    @Override
    public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
        super.onRenderProcessGone(view, detail);

        Log.i("FixWebViewClient", "onRenderProcessGone: return");

        if (view != null && view.getParent() instanceof ViewGroup) {
            ((ViewGroup)view.getParent()).removeView(view);
            view.destroy();
        }
        return true;
    }
}
