package com.hfad.dawrlygp.views.Helpers;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hfad.dawrlygp.views.Fragments.UserFragment.NotificationFragment;

public class myBadgeView extends androidx.appcompat.widget.AppCompatTextView {

    private View target;

    public myBadgeView(Context context, View target) {
        super(context);
        init(context, target);
    }

    private void init(Context context, View target) {
        this.target = target;
    }

    public void updateTabBadge(int badgeNumber) {
        if (badgeNumber > 0) {
            target.setVisibility(View.VISIBLE);
            ((TextView) target).setText(Integer.toString(badgeNumber));
        }
        else {
            target.setVisibility(View.GONE);
        }
    }
}