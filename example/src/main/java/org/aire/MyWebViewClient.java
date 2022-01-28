package org.aire;

import android.view.ViewGroup;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author ZhuPeipei
 * @date 2021/11/16 15:15
 */
public class MyWebViewClient extends WebViewClient {
    @Override
    public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
        super.onRenderProcessGone(view, detail);

        if (view != null) {
            ((ViewGroup)view.getParent()).removeView(view);
            view.destroy();
        }
        return true;
    }
}
