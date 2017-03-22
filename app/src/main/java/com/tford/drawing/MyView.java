package com.tford.drawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by tford on 3/22/17.
 */
public class MyView extends View {
    private boolean circleOn;
    private Paint paint;
    private Paint pixelOnPaint;
    private Paint pixelOffPaint;
    private byte[] byteArray = new byte[200];

    private static int pixelStrokeWidth = 20;
    private static int ones = 0xff;

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.parseColor("#CD5C5C"));
        pixelOnPaint = new Paint();
        pixelOnPaint.setStrokeWidth(pixelStrokeWidth);
        pixelOnPaint.setColor(Color.BLACK);
        pixelOffPaint = new Paint();
        pixelOffPaint.setStrokeWidth(pixelStrokeWidth);
        pixelOffPaint.setColor(Color.WHITE);
        //initializeByteArray();
    }

    public void drawHorizontalLine(int x1, int x2, int y) {
        setHorizontalLineBits(x1, x2, y);
        invalidate();
        /*

        */
    }

    private void setHorizontalLineBits(int x1, int x2, int y) {
        if (x1 > x2) {
            System.out.printf("Cannot draw line from x1 = %d to x2 = %d; want x1 <= x2.%n", x1, x2);
            return;
        }
        if (x1 < 0 || x2 > 39) {
            System.out.printf("Cannot draw line from x1 = %d to x2 = %d; want x1, x2 in [0, 39] %n", x1, x2);
            return;
        }
        if (y < 0 || y > 39) {
            System.out.printf("Cannot draw a line at y = %d; want y in [0, 39] %n", y);
            return;
        }
        int rowStart = getRowStart(y);
        int x1Index = getColumnIndex(x1) + rowStart;
        System.out.printf("Got x1Index of: %d %n", x1Index);
        int x1BitIndex = getBitIndex(x1);
        System.out.printf("Got x1BitIndex of: %d %n", x1BitIndex);
        int x2Index = getColumnIndex(x2) + rowStart;
        System.out.printf("Got x2Index of: %d %n", x2Index);
        int x2BitIndex = getBitIndex(x2);
        System.out.printf("Got x2BitIndex of: %d %n", x2BitIndex);
        byte x1Mask = makeLeftMask(x1BitIndex);
        String x1MaskString = binaryString(x1Mask);
        System.out.printf("The value of x1Mask is: %s %n", x1MaskString);
        byte x2Mask = makeRightMask(x2BitIndex);
        String x2MaskString = binaryString(x2Mask);
        System.out.printf("The value of x2Mask is: %s %n", x2MaskString);
        if (x1Index == x2Index) {
            byteArray[x1Index] |= (x1Mask & x2Mask);
            return;
        }
        byteArray[x1Index] |= x1Mask;

        for (int i = x1Index + 1; i < x2Index; i++) {
            byteArray[i] |= ones;
        }

        byteArray[x2Index] |= x2Mask;
    }

    private String binaryString(byte b) {
        return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
    }

    private String poopBinaryString(int x) {
        return String.format("%8s", Integer.toBinaryString(x).replace(' ', '0'));
    }

    private int getRowStart(int y) {
        return 5 * y;
    }

    private int getColumnIndex(int x) {
        return x / 8;
    }

    private int getBitIndex(int x) {
        return 7 - (x % 8);
    }

    private byte makeLeftMask(int x1Bit) {
        return (byte) ~(ones << (x1Bit + 1));
        /*
        System.out.printf("Inside makeLeftMask, got called with x1Bit: %d", x1Bit);
        int result = ones >> (7 - x1Bit);
        System.out.printf("The result of shifting right by %d places is:%s%n", 7 - x1Bit, poopBinaryString(result));
        return (byte) (ones >> (7 - x1Bit));
        */
    }

    private byte makeRightMask(int x2Bit) {
        //return (byte) (ones << x2Bit);
        return (byte) (ones << x2Bit);
    }

    @Override
    public void onDraw(Canvas canvas) {
        //System.out.println("Inside onDraw, got called!!!\n");
        super.onDraw(canvas);
        for (int x = 0; x < byteArray.length; x++) {
            byte b = byteArray[x];
            // Get row:
            int row = x / 5;
            // Get column:
            int column = (x % 5) * 8;
            for (int j = 7; j >= 0; j--) {
                boolean isOn = getBit(b, j);
                int pixelJ = 7 - j;
                int pixelX = pixelStrokeWidth / 2 + (column + pixelJ) * pixelStrokeWidth;
                int pixelY = pixelStrokeWidth / 2 + row * pixelStrokeWidth;
                if (isOn) {
                    canvas.drawPoint(pixelX, pixelY, pixelOnPaint);
                } else {
                    canvas.drawPoint(pixelX, pixelY, pixelOffPaint);
                }
            }

        }
    }

    private boolean getBit(byte b, int i) {
        if ((b & (1 << i)) != 0) {
            return true;
        } else {
            return false;
        }
    }
}
