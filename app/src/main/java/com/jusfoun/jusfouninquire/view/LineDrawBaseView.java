package com.jusfoun.jusfouninquire.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.jusfoun.baselibrary.Util.PhoneUtil;
import com.jusfoun.baselibrary.base.BaseModel;
import com.qxb.jianhang.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Author  JUSFOUN
 * CreateDate 2015/11/2.
 * Description
 */
public class LineDrawBaseView extends View {

    private static final String TAG = "DrawLineBaseView";
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int CHILD = 2;

    protected Context context;
    protected PointF mCir;
    protected float mRaius;
    protected Paint mCirPaint, mLeftPaint, mRightPaint, mCirAnimPaint;

    protected boolean enableTouch, childAnimStop, threadStop;
    protected Thread thread;

    protected ArrayList<PointF> leftLines = new ArrayList<PointF>();
    protected ArrayList<PointF> rightLines = new ArrayList<PointF>();
    protected ArrayList<Paint> leftPaints = new ArrayList<Paint>();
    protected ArrayList<Paint> rightPaints = new ArrayList<Paint>();

    protected ArrayList<ValueAnimator> leftAnims = new ArrayList<ValueAnimator>();
    protected ArrayList<ValueAnimator> rightAnims = new ArrayList<ValueAnimator>();
    protected ArrayList<ArrayList<PointF>> leftPoints = new ArrayList<ArrayList<PointF>>();
    protected ArrayList<ArrayList<PointF>> rightPoints = new ArrayList<ArrayList<PointF>>();

    protected ArrayList<String> leftInfo = new ArrayList<String>();
    protected ArrayList<String> rightInfo = new ArrayList<String>();
    protected ArrayList<RectF> leftRects = new ArrayList<RectF>();
    protected ArrayList<RectF> rightRects = new ArrayList<RectF>();
    protected ArrayList<TextPaint> childTxtPaints = new ArrayList<TextPaint>();

    protected float leftAngles[], rightAngles[], mTran[], childAngles[];
    protected Path path;
    protected boolean isAnimStop = false, doublePointer, isMove, isDrawChildLine;
    protected int leftCount = 0, rightCount = 0, clickCount = -1, lastClickCount = -1, leftChildCount, rightChildCount;
    protected float downX, downY, initX, initY;

    protected ArrayList<PointF> childList = new ArrayList<PointF>();
    protected ArrayList<RectF> childRect = new ArrayList<RectF>();
    protected ArrayList<ArrayList<PointF>> childPoint = new ArrayList<ArrayList<PointF>>();
    protected TextPaint leftPaint, rightPaint, cirPaint;
    protected ValueAnimator animChild, animPutChild;

    protected float mRaiusCount = 40;
    protected int index = 0, childIndex = 0;
    protected VelocityTracker mVelocityTracker;
    protected float scaleCount = 1;
    protected StaticLayout mStaticLayout;

    protected Bitmap mCirBitmap, mBlueArrow, mBlueLight, mRedArrow, mRedLight;
    protected Matrix mCirMatrix, mRedArrowMatrix, mRedLightMatrix, mBlueArrowMatrix, mBlueLightMatrix, mCirMatrixChild;
    protected PointF leftArrowAngle[], rightArrowAngle[], childArrowAngle[];
    protected ArrayList<StaticLayout> leftStaticLayout = new ArrayList<StaticLayout>();
    protected ArrayList<StaticLayout> rightStaticLayout = new ArrayList<StaticLayout>();

    protected Paint paint;
    protected int maxWidth = 60;
    protected ArrayList<String> alphaList = new ArrayList<String>();
    protected ArrayList<String> startWidthList = new ArrayList<String>();

    protected ArrayList<Paint> childPaints = new ArrayList<Paint>();
    protected ArrayList<String> childInfo = new ArrayList<String>();
    protected List<BaseModel> left = new ArrayList<BaseModel>();
    protected List<BaseModel> right = new ArrayList<BaseModel>();
    protected List<BaseModel> child = new ArrayList<BaseModel>();

    protected float minCirRaius = 0;
    protected boolean allAnimStop;
    protected float mRaiusRatio = 1;//半径所占比例
    protected int rectAddCount;
    protected boolean leftToCen, rightToCen;

    protected String centerString = "";
    protected float minit = 0;
    float lastScale = 1;
    protected float x3;
    protected PointF pointf = new PointF();

    protected int count = 0, lastIndex;
    protected boolean putChildAnim = false;

    public LineDrawBaseView(Context context) {
        super(context);
        initView(context);
    }

    public LineDrawBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LineDrawBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setAllAnimStop(boolean allAnimStop) {
        this.allAnimStop = allAnimStop;
    }

    public void setmRaiusRatio(float mRaiusRatio) {
        this.mRaiusRatio = mRaiusRatio;
    }

    public void initView(Context context) {
        this.context = context;
        mCirPaint = new Paint();
        mCirPaint.setColor(Color.parseColor("#FF7200"));
        mCirPaint.setStrokeWidth(2);
        mCirPaint.setStyle(Paint.Style.FILL);

        mCirAnimPaint = new Paint();
        mCirAnimPaint.setColor(Color.YELLOW);
        mCirAnimPaint.setStrokeWidth(2);
        mCirAnimPaint.setStyle(Paint.Style.STROKE);
        mCirAnimPaint.setAlpha(255);

        mLeftPaint = new Paint();
        mLeftPaint.setColor(Color.RED);
        mLeftPaint.setStrokeWidth(2);
        mLeftPaint.setStyle(Paint.Style.FILL);

        mRightPaint = new Paint();
        mRightPaint.setColor(Color.BLUE);
        mRightPaint.setStrokeWidth(2);
        mRightPaint.setStyle(Paint.Style.FILL);

        path = new Path();
        mCir = new PointF();
        mTran = new float[]{0, 0};

        animChild = ValueAnimator.ofInt(0, 100);
        animChild.setDuration(1000);
        mVelocityTracker = VelocityTracker.obtain();

        animPutChild = ValueAnimator.ofInt(0, 100);
        animPutChild.setDuration(500);

        leftPaint = new TextPaint();
        leftPaint.setAlpha(255);
        leftPaint.setStyle(Paint.Style.FILL);
        leftPaint.setTextAlign(Paint.Align.RIGHT);
        leftPaint.setColor(context.getResources().getColor(R.color.color_text_company_map_info));
        leftPaint.setTextSize(25);

        rightPaint = new TextPaint();
        rightPaint.setAlpha(255);
        rightPaint.setStyle(Paint.Style.FILL);
        rightPaint.setTextAlign(Paint.Align.LEFT);
        rightPaint.setColor(context.getResources().getColor(R.color.color_text_company_map_info));
        rightPaint.setTextSize(25);

        cirPaint = new TextPaint();
        cirPaint.setAlpha(255);
        cirPaint.setStyle(Paint.Style.FILL);
        cirPaint.setTextAlign(Paint.Align.CENTER);
        cirPaint.setColor(Color.WHITE);
        cirPaint.setStrokeWidth(2);
        cirPaint.setTextSize(PhoneUtil.dip2px(context, 12));

        mCirBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.cir);
        mBlueArrow = BitmapFactory.decodeResource(context.getResources(), R.mipmap.investment_arrow);
        mRedArrow = BitmapFactory.decodeResource(context.getResources(), R.mipmap.shareholder_arrow);
        mBlueLight = BitmapFactory.decodeResource(context.getResources(), R.mipmap.investment_light);
        mRedLight = BitmapFactory.decodeResource(context.getResources(), R.mipmap.shareholder_light);

        mCirMatrix = new Matrix();
        mRedArrowMatrix = new Matrix();
        mRedLightMatrix = new Matrix();
        mBlueArrowMatrix = new Matrix();
        mBlueLightMatrix = new Matrix();
        mCirMatrixChild = new Matrix();

        paint = new Paint();
        paint.setColor(Color.RED);//此处颜色可以改为自己喜欢的
        paint.setAlpha(255);
        paint.setStyle(Paint.Style.FILL);
        alphaList.add("255");//圆心的不透明度
        startWidthList.add("0");

        rectAddCount = PhoneUtil.dip2px(context, 20);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRaius = Math.min(w, h) / 2 * mRaiusRatio;
        mCir.x = w / 2;
        mCir.y = h / 2;

        maxWidth = (int) (mRaius * 0.4f);
        minCirRaius = mRaius * 0.06f;
        if (viewSizeChange != null)
            viewSizeChange.onSizeChange();
//        refresh(null);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(mTran[0], mTran[1]);
        canvas.scale(scaleCount, scaleCount, mCir.x, mCir.y);
        //绘制主图
        for (int i = 0; i < leftCount; i++) {
            path.reset();
            path.moveTo(mCir.x, mCir.y);
            ArrayList<PointF> list = leftPoints.get(i);
            if (isAnimStop) {
                enableTouch = true;
                path.lineTo(leftLines.get(i).x, leftLines.get(i).y);
                canvas.drawPath(path, leftPaints.get(i));
                float arc = leftAngles[i] / 180f;
                if (clickCount != i) {
                    canvas.save();
                    canvas.rotate(leftAngles[i] + 180, (leftLines.get(i).x), (leftLines.get(i).y + 12));
                    try {
                        canvas.drawText(leftInfo.get(i), (leftLines.get(i).x - minCirRaius * 2), (leftLines.get(i).y + 12), leftPaint);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    canvas.restore();
                }
                if (!isDrawChildLine && clickCount == i) {
                    canvas.save();
                    canvas.rotate(leftAngles[i] + 180, (leftLines.get(i).x), (leftLines.get(i).y + 12));
                    canvas.drawText(leftInfo.get(i), (leftLines.get(i).x - minCirRaius * 2), (leftLines.get(i).y + 12), leftPaint);
                    canvas.restore();
                }

                if (leftToCen) {
                    if (mRedArrow != null && !mRedArrow.isRecycled()) {
                        mRedArrowMatrix.setRotate(leftAngles[i] + 90, mRedArrow.getWidth() / 2, mRedArrow.getHeight() / 2);
                        mRedArrowMatrix.postTranslate((leftArrowAngle[i].x - mRedArrow.getWidth() / 2)
                                , (leftArrowAngle[i].y - mRedArrow.getHeight() / 2));
                        canvas.drawBitmap(mRedArrow, mRedArrowMatrix, null);
                    }
                } else {
                    if (mBlueArrow != null && !mBlueArrow.isRecycled()) {
                        mBlueArrowMatrix.setRotate(leftAngles[i] + 270, mBlueArrow.getWidth() / 2, mBlueArrow.getHeight() / 2);
                        mBlueArrowMatrix.postTranslate((leftArrowAngle[i].x - mBlueArrow.getWidth() / 2)
                                , (leftArrowAngle[i].y - mBlueArrow.getHeight() / 2));
                        canvas.drawBitmap(mBlueArrow, mBlueArrowMatrix, null);
                    }
                }

                if (!allAnimStop) {
                    int j = index / 2;
                    if (j < list.size()) {
                        if (leftToCen) {
                            if (mRedLight != null && !mRedLight.isRecycled()) {
                                mRedLightMatrix.setRotate(leftAngles[i] + 90, mRedLight.getWidth() / 2, mRedLight.getHeight() / 2);
                                mRedLightMatrix.postTranslate(list.get(list.size() - j - 1).x - mRedLight.getWidth() / 2
                                        , list.get(list.size() - j - 1).y - mRedLight.getHeight() / 2);
                                canvas.drawBitmap(mRedLight, mRedLightMatrix, null);
                            }
                        } else {
                            if (mBlueLight != null && !mBlueArrow.isRecycled()) {
                                mBlueLightMatrix.setRotate(leftAngles[i] + 270, mBlueLight.getWidth() / 2, mBlueLight.getHeight() / 2);
                                mBlueLightMatrix.postTranslate(list.get(j).x - mBlueLight.getWidth() / 2
                                        , list.get(j).y - mBlueLight.getHeight() / 2);
                                canvas.drawBitmap(mBlueLight, mBlueLightMatrix, null);
                            }
                        }
                    } else
                        index = 0;
                }
                canvas.drawCircle(leftLines.get(i).x, leftLines.get(i).y, minCirRaius, leftPaints.get(i));
            } else {
                for (int j = 0; j < list.size(); j++) {
                    path.lineTo(list.get(j).x, list.get(j).y);
                    canvas.drawPath(path, leftPaints.get(i));
                    if (j == list.size() - 1) {
                        canvas.drawCircle(list.get(j).x, list.get(j).y, minCirRaius, leftPaints.get(i));
                    }
                }
            }
            path.close();
        }

        for (int i = 0; i < rightCount; i++) {
            path.reset();
            path.moveTo(mCir.x, mCir.y);
            ArrayList<PointF> list = rightPoints.get(i);
            if (isAnimStop) {
                enableTouch = true;
                path.lineTo(rightLines.get(i).x, rightLines.get(i).y);
                canvas.drawPath(path, rightPaints.get(i));
                if (clickCount - leftCount != i) {
                    canvas.save();
                    canvas.rotate(rightAngles[i], rightLines.get(i).x, rightLines.get(i).y + 12);
                    try {
                        canvas.drawText(rightInfo.get(i), rightLines.get(i).x + minCirRaius * 2, rightLines.get(i).y + 12, rightPaint);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    canvas.restore();
                }

                if (!isDrawChildLine && clickCount - leftCount == i) {
                    canvas.save();
                    canvas.rotate(rightAngles[i], rightLines.get(i).x, rightLines.get(i).y + 12);
                    canvas.drawText(rightInfo.get(i), rightLines.get(i).x + minCirRaius * 2, rightLines.get(i).y + 12, rightPaint);
                    canvas.restore();
                }

                if (rightToCen) {
                    if (mRedArrow != null && !mRedArrow.isRecycled()) {
                        mRedArrowMatrix.setRotate(rightAngles[i] + 90, mRedArrow.getWidth() / 2, mRedArrow.getHeight() / 2);
                        mRedArrowMatrix.postTranslate((rightArrowAngle[i].x - mRedArrow.getWidth() / 2)
                                , (rightArrowAngle[i].y - mRedArrow.getHeight() / 2));
                        canvas.drawBitmap(mRedArrow, mRedArrowMatrix, null);
                    }
                } else {
                    if (mBlueArrow != null && !mBlueArrow.isRecycled()) {
                        mBlueArrowMatrix.setRotate(rightAngles[i] + 270, mBlueArrow.getWidth() / 2, mBlueArrow.getHeight() / 2);
                        mBlueArrowMatrix.postTranslate((rightArrowAngle[i].x - mBlueArrow.getWidth() / 2)
                                , (rightArrowAngle[i].y - mBlueArrow.getHeight() / 2));
                        canvas.drawBitmap(mBlueArrow, mBlueArrowMatrix, null);
                    }
                }
                if (!allAnimStop) {
                    int j = index / 2;
                    if (j < list.size()) {
                        if (rightToCen) {
                            if (mRedLight != null && !mRedLight.isRecycled()) {
                                mRedLightMatrix.setRotate(rightAngles[i] + 90, mRedLight.getWidth() / 2, mRedLight.getHeight() / 2);
                                mRedLightMatrix.postTranslate(list.get(list.size() - j - 1).x - mRedLight.getWidth() / 2
                                        , list.get(list.size() - j - 1).y - mRedLight.getHeight() / 2);
                                canvas.drawBitmap(mRedLight, mRedLightMatrix, null);
                            }
                        } else {
                            if (mBlueLight != null && !mBlueArrow.isRecycled()) {
                                mBlueLightMatrix.setRotate(rightAngles[i] + 270, mBlueLight.getWidth() / 2, mBlueLight.getHeight() / 2);
                                mBlueLightMatrix.postTranslate(list.get(j).x - mBlueLight.getWidth() / 2
                                        , list.get(j).y - mBlueLight.getHeight() / 2);
                                canvas.drawBitmap(mBlueLight, mBlueLightMatrix, null);
                            }
                        }
                    } else
                        index = 0;
                }
                canvas.drawCircle(rightLines.get(i).x, rightLines.get(i).y, minCirRaius, rightPaints.get(i));
            } else {
                for (int j = 0; j < list.size(); j++) {
                    path.lineTo(list.get(j).x, list.get(j).y);
                    canvas.drawPath(path, rightPaints.get(i));
                    if (j == list.size() - 1) {
                        canvas.drawCircle(list.get(j).x, list.get(j).y, minCirRaius, rightPaints.get(i));
                    }
                }
            }
            path.close();
        }
        if (mStaticLayout != null) {
            canvas.drawCircle(mCir.x, mCir.y, PhoneUtil.dip2px(context, 28), mCirPaint);
            canvas.save();
            if (centerString.length() > 3)
                canvas.translate(mCir.x, mCir.y - PhoneUtil.dip2px(context, 14));
            else
                canvas.translate(mCir.x, mCir.y - PhoneUtil.dip2px(context, 7));
            mStaticLayout.draw(canvas);
            canvas.restore();
//            if (leftCount == 0 && rightCount == 0) {
//                canvas.drawText("暂无股东、投资数据", mCir.x, mCir.y + PhoneUtil.dip2px(context, 50), cirPaint);
//            }
        }

        if (!allAnimStop && (leftCount != 0 || rightCount != 0)) {
            if (mCirBitmap != null && !mCirBitmap.isRecycled()) {
                mCirMatrix.setScale(mRaiusCount / 50f, mRaiusCount / 50f, mCirBitmap.getWidth() / 2, mCirBitmap.getHeight() / 2);
                mCirMatrix.postTranslate(mCir.x - mCirBitmap.getWidth() / 2, mCir.y - mCirBitmap.getHeight() / 2);
                canvas.drawBitmap(mCirBitmap, mCirMatrix, null);
            }
        }

        //绘制子节点
        if (isDrawChildLine) {
            if (childAnimStop) {
                if (putChildAnim) {
                    for (int i = 0; i < childPoint.size(); i++) {
                        ArrayList<PointF> list = childPoint.get(i);
                        if (list.size() < 1) {
                            putChildAnim = false;
                            isDrawChildLine = false;
                            animPutChild.start();
                        }
                        path.reset();
                        if (lastClickCount < leftCount)
                            path.moveTo(leftLines.get(lastClickCount).x, leftLines.get(lastClickCount).y);
                        else
                            path.moveTo(rightLines.get(lastClickCount - leftCount).x, rightLines.get(lastClickCount - leftCount).y);
                        for (int j = 0; j < list.size(); j++) {
                            path.lineTo(list.get(j).x, list.get(j).y);
                            if (j == list.size() - 1) {
                                if (i < leftChildCount)
                                    canvas.drawCircle(list.get(j).x, list.get(j).y, minCirRaius, childPaints.get(i));
                                else
                                    canvas.drawCircle(list.get(j).x, list.get(j).y, minCirRaius, childPaints.get(i));
                                list.remove(j);
                                if (j - 1 >= 0)
                                    list.remove(j - 1);
                                if (j - 2 >= 0)
                                    list.remove(j - 2);
                            }
                        }
                        if (i < leftChildCount)
                            canvas.drawPath(path, childPaints.get(i));
                        else {
                            canvas.drawPath(path, childPaints.get(i));
                        }
                    }
                } else {
                    enableTouch = true;
                    for (int i = 0; i < leftChildCount + rightChildCount; i++) {
                        ArrayList<PointF> list = childPoint.get(i);
                        int j = childIndex;
                        path.reset();
                        if (clickCount < leftCount) {
                            path.moveTo(leftLines.get(clickCount).x, leftLines.get(clickCount).y);
                            path.lineTo(childList.get(i).x, childList.get(i).y);
                            canvas.save();
                            canvas.rotate(childAngles[i] + 180, childList.get(i).x, childList.get(i).y);
                            canvas.drawText(childInfo.get(i), childList.get(i).x - minCirRaius * 2, childList.get(i).y, childTxtPaints.get(i));
                            canvas.restore();

                        } else {
                            path.moveTo(rightLines.get(clickCount - leftCount).x, rightLines.get(clickCount - leftCount).y);
                            path.lineTo(childList.get(i).x, childList.get(i).y);
                            canvas.save();
                            canvas.rotate(childAngles[i], childList.get(i).x, childList.get(i).y);
                            canvas.drawText(childInfo.get(i), childList.get(i).x + minCirRaius * 2, childList.get(i).y, childTxtPaints.get(i));
                            canvas.restore();
                        }
                        if (i < leftChildCount) {
                            canvas.drawPath(path, childPaints.get(i));

                            if (mRedArrow != null && !mRedArrow.isRecycled()) {
                                mRedArrowMatrix.setRotate(childAngles[i] + 90, mRedArrow.getWidth() / 2, mRedArrow.getHeight() / 2);
                                mRedArrowMatrix.postTranslate((childArrowAngle[i].x - mRedArrow.getWidth() / 2)
                                        , (childArrowAngle[i].y - mRedArrow.getHeight() / 2));
                                canvas.drawBitmap(mRedArrow, mRedArrowMatrix, null);
                            }
                            if (j < list.size()) {
                                if (mRedLight != null && !mRedLight.isRecycled()) {
                                    mRedLightMatrix.setRotate(childAngles[i] + 270, mRedLight.getWidth() / 2, mRedLight.getHeight() / 2);
                                    mRedLightMatrix.postTranslate(list.get(list.size() - j - 1).x - mRedLight.getWidth() / 2
                                            , list.get(list.size() - j - 1).y - mRedLight.getHeight() / 2);
                                    canvas.drawBitmap(mRedLight, mRedLightMatrix, null);
                                }
                            } else {
                                childIndex = 0;
                            }
                            canvas.drawCircle(childList.get(i).x, childList.get(i).y, minCirRaius, childPaints.get(i));
                        } else {
                            canvas.drawPath(path, childPaints.get(i));
                            if (mBlueArrow != null && !mBlueArrow.isRecycled()) {
                                mBlueArrowMatrix.setRotate(childAngles[i] + 270, mBlueArrow.getWidth() / 2, mBlueArrow.getHeight() / 2);
                                mBlueArrowMatrix.postTranslate((childArrowAngle[i].x - mBlueArrow.getWidth() / 2)
                                        , (childArrowAngle[i].y - mBlueArrow.getHeight() / 2));
                                canvas.drawBitmap(mBlueArrow, mBlueArrowMatrix, null);
                            }
                            if (j < list.size()) {
                                if (mBlueLight != null && !mBlueArrow.isRecycled()) {
                                    mBlueLightMatrix.setRotate(childAngles[i] + 90, mBlueLight.getWidth() / 2, mBlueLight.getHeight() / 2);
                                    mBlueLightMatrix.postTranslate(list.get(j).x - mBlueLight.getWidth() / 2
                                            , list.get(j).y - mBlueLight.getHeight() / 2);
                                    canvas.drawBitmap(mBlueLight, mBlueLightMatrix, null);
                                }
                            } else
                                childIndex = 0;
                            canvas.drawCircle(childList.get(i).x, childList.get(i).y, minCirRaius, childPaints.get(i));
                        }
                        path.close();

                    }
                    if (clickCount < leftCount) {
                        canvas.drawCircle(leftLines.get(clickCount).x, leftLines.get(clickCount).y, PhoneUtil.dip2px(context, 28), mCirPaint);
                        if (mCirBitmap != null && !mCirBitmap.isRecycled()) {
                            mCirMatrixChild.setScale(mRaiusCount / 50f, mRaiusCount / 50f, mCirBitmap.getWidth() / 2, mCirBitmap.getHeight() / 2);
                            mCirMatrixChild.postTranslate(leftLines.get(clickCount).x - mCirBitmap.getWidth() / 2
                                    , leftLines.get(clickCount).y - mCirBitmap.getHeight() / 2);
                            canvas.drawBitmap(mCirBitmap, mCirMatrixChild, null);
                        }

                        canvas.save();
                        if (leftInfo.get(clickCount).length() <= 3)
                            canvas.translate(leftLines.get(clickCount).x, leftLines.get(clickCount).y - PhoneUtil.dip2px(context, 7));
                        else
                            canvas.translate(leftLines.get(clickCount).x, leftLines.get(clickCount).y - PhoneUtil.dip2px(context, 14));
                        leftStaticLayout.get(clickCount).draw(canvas);
                        canvas.restore();

                    } else {
                        canvas.drawCircle(rightLines.get(clickCount - leftCount).x, rightLines.get(clickCount - leftCount).y, PhoneUtil.dip2px(context, 28), mCirPaint);
                        if (mCirBitmap != null && !mCirBitmap.isRecycled()) {
                            mCirMatrixChild.setScale(mRaiusCount / 50f, mRaiusCount / 50f, mCirBitmap.getWidth() / 2, mCirBitmap.getHeight() / 2);
                            mCirMatrixChild.postTranslate(rightLines.get(clickCount - leftCount).x - mCirBitmap.getWidth() / 2
                                    , rightLines.get(clickCount - leftCount).y - mCirBitmap.getHeight() / 2);
                            canvas.drawBitmap(mCirBitmap, mCirMatrixChild, null);
                        }
                        canvas.save();
                        if (rightInfo.get(clickCount - leftCount).length() <= 3)
                            canvas.translate(rightLines.get(clickCount - leftCount).x, rightLines.get(clickCount - leftCount).y - PhoneUtil.dip2px(context, 7));
                        else
                            canvas.translate(rightLines.get(clickCount - leftCount).x, rightLines.get(clickCount - leftCount).y - PhoneUtil.dip2px(context, 14));
                        rightStaticLayout.get(clickCount - leftCount).draw(canvas);
                        canvas.restore();
                    }
                }
            } else {
                enableTouch = false;
                for (int i = 0; i < childPoint.size(); i++) {
                    ArrayList<PointF> list = childPoint.get(i);
                    for (int j = 0; j < list.size(); j++) {
                        path.reset();
                        if (clickCount < leftCount) {
                            path.moveTo(leftLines.get(clickCount).x, leftLines.get(clickCount).y);
                            path.lineTo(list.get(j).x, list.get(j).y);
                            if (i < leftChildCount) {
                                canvas.drawPath(path, childPaints.get(i));
                                if (j == list.size() - 1) {
                                    canvas.drawCircle(list.get(j).x, list.get(j).y, minCirRaius, childPaints.get(i));
                                }
                            } else {
                                canvas.drawPath(path, childPaints.get(i));
                                if (j == list.size() - 1) {
                                    canvas.drawCircle(list.get(j).x, list.get(j).y, minCirRaius, childPaints.get(i));
                                }
                            }

                        } else {
                            path.moveTo(rightLines.get(clickCount - leftCount).x, rightLines.get(clickCount - leftCount).y);
                            path.lineTo(list.get(j).x, list.get(j).y);
                            if (i < leftChildCount) {
                                canvas.drawPath(path, childPaints.get(i));
                                if (j == list.size() - 1) {
                                    canvas.drawCircle(list.get(j).x, list.get(j).y, minCirRaius, childPaints.get(i));
                                }
                            } else {
                                canvas.drawPath(path, childPaints.get(i));
                                if (j == list.size() - 1) {
                                    canvas.drawCircle(list.get(j).x, list.get(j).y, minCirRaius, childPaints.get(i));
                                }
                            }

                        }
                        path.close();
                    }
                }
            }
        }
        canvas.restore();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        threadStop = true;
        stopThread();
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }

        if (mCirBitmap != null) {
            mCirBitmap.recycle();
            mCirBitmap = null;
        }

        if (mRedArrow != null) {
            mRedArrow.recycle();
            mRedArrow = null;
        }

        if (mRedLight != null) {
            mRedLight.recycle();
            mRedLight = null;
        }

        if (mBlueArrow != null) {
            mBlueArrow.recycle();
            mBlueArrow = null;
        }

        if (mBlueLight != null) {
            mBlueLight.recycle();
            mBlueLight = null;
        }

        stopAllAnim();
    }

    private void stopAllAnim() {
        for (ValueAnimator animator : leftAnims) {
            if (animator != null && animator.isRunning())
                animator.cancel();
        }
        for (ValueAnimator animator : rightAnims) {
            if (animator != null && animator.isRunning())
                animator.cancel();
        }

        if (animChild != null && animChild.isRunning()) {
            animChild.cancel();
        }

        if (animPutChild != null && animPutChild.isRunning()) {
            animPutChild.cancel();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!enableTouch)
            return true;
        if (onTouchView != null)
            onTouchView.onTouch();
        switch (event.getPointerCount()) {
            case 1:
                translate(event);
                break;
            case 2:
                scale(event);
                break;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (doublePointer)
                    doublePointer = false;
                if (isMove) {
                    isMove = false;
                    break;
                }
                float x = event.getX() - mTran[0];
                float y = event.getY() - mTran[1];
                Log.e(TAG + "click", "x" + x + "\n" + "y" + y);
                int i = 0;
                int clickCount = isInRect(x, y, leftRects);
                if (clickCount >= 0) {
                    if (potListener != null)
                        potListener.onClickPot(LEFT, clickCount, left.get(clickCount));

                } else {
                    clickCount = isInRect(x, y, rightRects);
                    if (clickCount >= 0) {
                        clickCount += leftCount;
                        potListener.onClickPot(RIGHT, clickCount, right.get(clickCount - leftCount));

                    }
                }

                if (clickCount >= 0) {
                    scaleCount = 1;
                    lastScale = 1;
                    for (PointF pointF : leftLines) {
                        leftRects.get(i).set(pointF.x - rectAddCount, pointF.y - rectAddCount, pointF.x + rectAddCount, pointF.y + rectAddCount);
                        i++;
                    }
                    i = 0;
                    for (PointF pointF : rightLines) {
                        rightRects.get(i).set(pointF.x - rectAddCount, pointF.y - rectAddCount, pointF.x + rectAddCount, pointF.y + rectAddCount);
                        i++;
                    }
                }
                if (LineDrawBaseView.this.clickCount != -1 && LineDrawBaseView.this.clickCount == lastClickCount) {
                    int count = isInRect(x, y, childRect);

                    if (count >= 0) {
                        if (potListener != null) {
                            potListener.onClickPot(CHILD, count, child.get(count));
                        }
                        i = 0;
                        for (PointF pointF : childList) {
                            childRect.get(i).set(pointF.x - rectAddCount, pointF.y - rectAddCount, pointF.x + rectAddCount, pointF.y + rectAddCount);
                            i++;
                        }
                    }
                    return true;
                }
                break;
        }
        return true;
    }

    protected void translate(MotionEvent event) {
        if (doublePointer) {
            return;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                initX = downX;
                initY = downY;
                break;
            case MotionEvent.ACTION_MOVE:
                if ((Math.abs(event.getX() - initX)) < 5 && (Math.abs(event.getY() - initY) < 5))
                    isMove = false;
                else
                    isMove = true;
                mTran[0] = mTran[0] + event.getX() - downX;
                mTran[1] = mTran[1] + event.getY() - downY;
                downX = event.getX();
                downY = event.getY();
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
    }

    protected void scale(MotionEvent event) {
        doublePointer = true;
        mVelocityTracker.addMovement(event);
        mVelocityTracker.computeCurrentVelocity(2000);
        isMove = true;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_UP:
                lastScale = scaleCount;
                break;
            case MotionEvent.ACTION_MOVE:
                float toScal = getXYLength(event);
                x3 = toScal / minit;
                float v = mVelocityTracker.getXVelocity();
                if (Math.abs(v) < 5)
                    break;
                if (x3 < 1 && scaleCount <= 0.6)
                    break;
                int i = 0;
                for (PointF pointF : leftLines) {
                    float x = pointF.x - mCir.x;
                    float y = pointF.y - mCir.y;
                    x = x * scaleCount + mCir.x;
                    y = y * scaleCount + mCir.y;
                    leftRects.get(i).set(x - rectAddCount, y - rectAddCount, x + rectAddCount, y + rectAddCount);
                    i++;
                }
                i = 0;
                for (PointF pointF : rightLines) {
                    float x = pointF.x - mCir.x;
                    float y = pointF.y - mCir.y;
                    x = x * scaleCount + mCir.x;
                    y = y * scaleCount + mCir.y;
                    rightRects.get(i).set(x - rectAddCount, y - rectAddCount, x + rectAddCount, y + rectAddCount);
                    i++;
                }

                i = 0;
                for (PointF pointF : childList) {
                    float x = pointF.x - mCir.x;
                    float y = pointF.y - mCir.y;
                    x = x * scaleCount + mCir.x;
                    y = y * scaleCount + mCir.y;
                    childRect.get(i).set(x - rectAddCount, y - rectAddCount, x + rectAddCount, y + rectAddCount);
                    i++;
                }
                scaleCount = x3 - 1 + lastScale;
                postInvalidate();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:

                if (event.getPointerCount() == 2) {
                    minit = getXYLength(event);
                    scaleCount = lastScale;
                }
                break;
        }
    }

    private int isInRect(float x, float y, ArrayList<RectF> list) {

        int count = 0;
        for (RectF rectF : list) {
            if (rectF.contains(x, y))
                return count;
            count++;
        }
        return -1;
    }

    private void stopThread() {
        if (thread != null && thread.isInterrupted()) {
            thread.interrupt();
            thread = null;
            System.gc();
        }
    }

    public float getXYLength(MotionEvent event) {

        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    public static interface OnClickPotListener {
        public void onClickPot(int type, int clickable, BaseModel baseModel);
    }

    private OnClickPotListener potListener;

    public void setClickPot(OnClickPotListener listener) {
        potListener = listener;
    }

    public static interface OnViewSizeChange {
        public void onSizeChange();
    }

    private OnViewSizeChange viewSizeChange;

    public void setViewSizeChange(OnViewSizeChange viewSizeChange) {
        this.viewSizeChange = viewSizeChange;
    }

    public static interface OnTouchView {
        public void onTouch();
    }

    private OnTouchView onTouchView;

    public void setOnTouchView(OnTouchView onTouchView) {
        this.onTouchView = onTouchView;
    }

    public void getListSize(int size[]) {
        if (size == null && size.length < 2)
            return;
        if (left == null)
            left = new ArrayList<BaseModel>();
        if (right == null)
            right = new ArrayList<BaseModel>();
        size[0] = left.size();
        size[1] = right.size();
    }
}
