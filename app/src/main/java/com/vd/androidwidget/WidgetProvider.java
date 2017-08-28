package com.vd.androidwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Created by farooq on 8/10/2017.
 */

public class WidgetProvider extends AppWidgetProvider {
    private Button button;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Toast.makeText(context, "Widget has been added to your home screen!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int appWidgetId : appWidgetIds) {

            // Register an onClickListener
            Intent intent = new Intent(context, MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(context,
                    0, intent, 0);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget);

            remoteViews.setOnClickPendingIntent(R.id.widget_button, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);


            /*
            In some launchers onAppWidgetOptionsChanged() method is not called on first time placement of widget.
            So set the widget here and getAppWidgetOptions() requires api >=16
            You don't need this check if you minimum api >= 16
            */
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
                int colSize = getCellsForSize(options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH));
                updateUI(context, colSize, appWidgetManager, appWidgetId);
                Log.e("App Widget", "Updating UI for the 1st time placement");
            }
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);

        Log.e("App Widget", "onAppWidgetOptionsChanged()");
        int colSize = getCellsForSize(newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH));
        Log.e("App Widget", String.valueOf(colSize));

        updateUI(context, colSize, appWidgetManager, appWidgetId);

    }

    /*
    * update the UI according to the number of cols
    * */
    private void updateUI(Context context, int colSize, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        switch (colSize) {
            case 1:
                remoteViews.setViewVisibility(R.id.icon2, View.GONE);
                remoteViews.setViewVisibility(R.id.icon3, View.GONE);
                remoteViews.setViewVisibility(R.id.icon4, View.GONE);
                remoteViews.setViewVisibility(R.id.icon5, View.GONE);

                remoteViews.setViewVisibility(R.id.icon1, View.VISIBLE);
                break;
            case 2:
                remoteViews.setViewVisibility(R.id.icon5, View.GONE);
                remoteViews.setViewVisibility(R.id.icon4, View.GONE);
                remoteViews.setViewVisibility(R.id.icon3, View.GONE);

                remoteViews.setViewVisibility(R.id.icon2, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.icon1, View.VISIBLE);
                break;
            case 3:
                remoteViews.setViewVisibility(R.id.icon5, View.GONE);
                remoteViews.setViewVisibility(R.id.icon4, View.GONE);

                remoteViews.setViewVisibility(R.id.icon3, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.icon2, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.icon1, View.VISIBLE);
                break;
            default:
                remoteViews.setViewVisibility(R.id.icon5, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.icon4, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.icon3, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.icon2, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.icon1, View.VISIBLE);
                break;
        }

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    /**
     * Returns number of cells needed for given size of the widget.
     *
     * @param size Widget size in dp.
     * @return Size in number of cells.
     */
    private static int getCellsForSize(int size) {
        int n = 2;
        while (70 * n - 30 < size)
            ++n;

        return n - 1;
    }
}
