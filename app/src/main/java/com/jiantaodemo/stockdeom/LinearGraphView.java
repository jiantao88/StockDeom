package com.jiantaodemo.stockdeom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Shader;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @auther ZhangJianTao
 * function
 * @Version 1.0
 */
public class LinearGraphView extends View {
    private List<Integer> dataList;
    private float tb;
    private float interval_left_right;
    private float interval_left;
    private Paint paint_date, paint_brokenLine, paint_dottedLine, paint_brokenLine_big, framPanint;

    private int time_index;
    private Bitmap bitmap_point;
    private Path path;
    private float dotted_text;

    public float getDottedText() {
        return dotted_text;
    }

    public void setDotted_text(float dotted_text) {
        this.dotted_text = dotted_text;
    }

    private int fineLineColor = Color.LTGRAY;
    private int blueLineColor = Color.BLUE;
    private int redLineColor = Color.RED;

    public LinearGraphView(Context context, List<Integer> date) {
        super(context);
        init(date);
    }

    private void init(List<Integer> date) {
        if (null == date || date.size() == 0)
            return;
        this.dataList = delZero(date);
        tb = getResources().getDimension(R.dimen.historyscore_tb);
        interval_left_right = tb * 2.0f;
        interval_left = tb * 0.5f;

        paint_date = new Paint();
        paint_date.setStrokeWidth(tb * 0.1f);
        paint_date.setTextSize(tb * 1.0f);
        paint_date.setColor(fineLineColor);

        paint_brokenLine = new Paint();
        paint_brokenLine.setStrokeWidth(tb * 0.1f);
        paint_brokenLine.setColor(blueLineColor);
        paint_brokenLine.setAntiAlias(true);

        paint_dottedLine = new Paint();
        paint_dottedLine.setStyle(Paint.Style.STROKE);
        paint_dottedLine.setColor(Color.BLACK);

        paint_brokenLine_big = new Paint();
        paint_brokenLine_big.setStrokeWidth(tb * 0.4f);
        paint_brokenLine_big.setColor(fineLineColor);
        paint_brokenLine_big.setAntiAlias(true);

        framPanint = new Paint();
        framPanint.setAntiAlias(true);
        framPanint.setStrokeWidth(2f);

        path = new Path();
        bitmap_point = BitmapFactory.decodeResource(getResources(), R.drawable.icon_point_blue);
        setLayoutParams(new ViewGroup.LayoutParams(
                (int) (this.dataList.size() * interval_left_right),
                ViewGroup.LayoutParams.MATCH_PARENT));    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == dataList || dataList.size() == 0)
            return;
        drawStraightLine(canvas);
        drawBrokenLine(canvas);
        drawDate(canvas);
    }

    /**
     * 绘制竖线
     *
     * @param canvas
     */
    private void drawStraightLine(Canvas canvas) {
        paint_brokenLine.setColor(fineLineColor);
        int count_line = 0;
        for (int i = 0; i < dataList.size(); i++) {
            //第一个竖线
            if (count_line == 0) {
                canvas.drawLine(interval_left_right * i, 0, interval_left_right * i, getHeight(), paint_date);
            }
            //第二个竖线
            if (count_line == 2) {
                canvas.drawLine(interval_left_right * i, tb * 1.5f, interval_left_right * i, getHeight(), paint_date);
            }
            //虚线
            if (count_line == 1 || count_line == 3) {
                Path path = new Path();
                path.moveTo(interval_left_right * i, tb * 1.5f);
                path.lineTo(interval_left_right * i, getHeight());
                PathEffect effect = new DashPathEffect(new float[]{tb * 0.3f, tb * 0.3f, tb * 0.3f, tb * 0.3f}, tb * 0.5f);
                paint_dottedLine.setPathEffect(effect);
                canvas.drawPath(path, paint_dottedLine);
            }
            count_line++;
            if (count_line >= 4) {
                count_line = 0;
            }
        }
        canvas.drawLine(0, getHeight() - tb * 0.2f, getWidth(), getHeight() - tb * 0.2f, paint_brokenLine_big);
    }

    /**
     * 绘制折线
     */
    public void drawBrokenLine(Canvas canvas) {
        int index = 0;
        float temp_x = 0;
        float temp_y = 0;
        float base = (getHeight() - tb * 3.0f) / (Collections.max(dataList) - Collections.min(dataList));
        Shader shader = new LinearGradient(0, 0, 0, getHeight(), new int[]{Color.argb(100, 0, 255, 255), Color.argb(45, 0, 255, 255), Color.argb(10, 0, 255, 255)}, null, Shader.TileMode.CLAMP);
        framPanint.setShader(shader);
        for (int i = 0; i < dataList.size() - 1; i++) {

            float x1 = interval_left_right * i;
            float y1 = getHeight() - tb * 1.5f - (base * dataList.get(i));
            float x2 = interval_left_right * (i + 1);
            float y2 = getHeight() - tb * 1.5f - (base * dataList.get(i + 1));

            if ((int) (base * dataList.get(i + 1)) == 0 && index == 0) {
                index++;
                temp_x = x1;
                temp_y = y1;
            }
            if ((int) (base * dataList.get(i + 1)) != 0 && index != 0) {
                index = 0;
                x1 = temp_x;
                y1 = temp_y;
            }
            if (index == 0) {
                canvas.drawLine(x1, y1, x2, y2, paint_brokenLine);
                path.lineTo(x1, y1);
                if (i != 0) {
                    canvas.drawBitmap(bitmap_point, x1 - bitmap_point.getWidth() / 2, y1 - bitmap_point.getHeight() / 2, null);
                }
                if (i == dataList.size() - 2) {
                    path.lineTo(x2, y2);
                    path.lineTo(x2, getHeight());
                    path.lineTo(0, getHeight());
                    path.close();
                    canvas.drawPath(path, framPanint);
                    canvas.drawBitmap(bitmap_point, x2 - bitmap_point.getWidth() / 2, y2 - bitmap_point.getHeight() / 2, null);
                }
            }
        }
        paint_dottedLine.setColor(redLineColor);
        Path path = new Path();
        path.moveTo(0, getHeight() - tb * 6.5f);
        path.lineTo(getWidth(), getHeight() - tb * 6.5f);
        PathEffect effects = new DashPathEffect(new float[]{tb * 0.3f,
                tb * 0.3f, tb * 0.3f, tb * 0.3f}, tb * 0.1f);
        paint_dottedLine.setPathEffect(effects);
        canvas.drawPath(path, paint_dottedLine);
    }

    /**
     * 绘制时间
     *
     * @param canvas
     */
    public void drawDate(Canvas canvas) {
        int hour = 0;
        String minute = "";
        float temp = time_index * 0.5f;
        if (temp % 1f == 0.5f) {
            hour = (int) temp;
            minute = ":30";
        } else {
            minute = ":00";
            hour = (int) temp;
        }
        for (int i = 0;i<dataList.size();i+=4){
            paint_date.setStrokeWidth(tb*2.8f);
            canvas.drawText(hour+minute,interval_left_right*i+interval_left,tb*1.0f,paint_date);
            hour+=2;
        }
    }

    /**
     * 移除前后为零的数据
     *
     * @param dateList
     * @return
     */
    public List<Integer> delZero(List<Integer> dateList) {
        List<Integer> list = new ArrayList<>();
        int start = 0;
        int end = 0;
        for (int i = 0; i < dateList.size(); i++) {
            if (dateList.get(i) != 0) {
                start = i;
                break;
            }
        }
        for (int i = dateList.size() - 1; i >= 0; i--) {
            if (dateList.get(i) != 0) {
                end = i;
                break;
            }
        }
        for (int i = 0; i < dateList.size(); i++) {
            if (i >= start && i <= end) {
                list.add(dateList.get(i));
            }
        }
        time_index = start;
        dotted_text = (Collections.max(dateList) - Collections.min(dateList)) / 12.0f * 5.0f;
        return list;
    }


}
