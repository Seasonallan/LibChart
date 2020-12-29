package com.example.myapplication;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.library.chart.DataHelper;
import com.library.chart.KLineChartAdapter;
import com.library.chart.KLineChartView;
import com.library.chart.KLineEntity;
import com.library.chart.formatter.DateFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class DemoActivity extends AppCompatActivity {

    KLineChartView kLineChartView;
    KLineChartAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        setTitle("功能选择");

        kLineChartView = findViewById(R.id.kLineChartView);

        kLineChartView.setDateTimeFormatter(new DateFormatter());
        kLineChartView.setGridRows(4);
        kLineChartView.setGridColumns(4);


        kLineChartView.justShowLoading();

        /**
         * "Close": "86.875",
         *     "Date": "1991/12/16",
         *     "High": "88",
         *     "Low": "86.75",
         *     "Open": "87.75",
         *     "Volume": "5168400"
         */
        String testJson = getStringFromAssert(this, "ibm.json");
        List<KLineEntity> lineEntityList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(testJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                KLineEntity entity = new KLineEntity();
                entity.Close = Float.parseFloat(jsonObject.getString("Close"));
                entity.Date = jsonObject.getString("Date");
                entity.High = Float.parseFloat(jsonObject.getString("High"));
                entity.Low = Float.parseFloat(jsonObject.getString("Low"));
                entity.Open = Float.parseFloat(jsonObject.getString("Open"));
                entity.Volume = Float.parseFloat(jsonObject.getString("Volume"));
                lineEntityList.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DataHelper.calculate(lineEntityList);
        adapter = new KLineChartAdapter();
        adapter.addFooterData(lineEntityList);
        kLineChartView.setAdapter(adapter);
        kLineChartView.startAnimation();
        kLineChartView.refreshEnd();

        kLineChartView.hideSelectData();
        kLineChartView.setChildDraw(0);
    }

    public static String getStringFromAssert(Context context, String fileName) {
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            return new String(buffer, 0, buffer.length, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}