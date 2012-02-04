package com.home.lepradroid;

import android.content.Context;
import android.view.LayoutInflater;

import com.home.lepradroid.base.BaseView;
import com.home.lepradroid.listenersworker.ListenersWorker;

public class AuthorView extends BaseView
{
    public AuthorView(Context context)
    {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null)
        {
            contentView = inflater.inflate(R.layout.author_view, null);
        }
    }

    @Override
    public void OnExit()
    {
        ListenersWorker.Instance().unregisterListener(this);
    }
}