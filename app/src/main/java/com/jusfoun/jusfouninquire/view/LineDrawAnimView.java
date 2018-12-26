package com.jusfoun.jusfouninquire.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;

import com.jusfoun.baselibrary.Util.PhoneUtil;
import com.jusfoun.baselibrary.base.BaseModel;
import com.qxb.jianhang.R;
import com.jusfoun.jusfouninquire.bean.CompanyMapModel;
import com.jusfoun.jusfouninquire.bean.InvestmentModel;
import com.jusfoun.jusfouninquire.bean.ShareholderModel;
import com.jusfoun.jusfouninquire.util.BezierOneTypeEvaluator;
import com.qxb.jianhang.ui.util.MathHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Author  JUSFOUN
 * CreateDate 2015/10/19.
 * Description
 */
public class LineDrawAnimView extends LineDrawBaseView {

    private static final String TAG = "LineDrawView";

    public LineDrawAnimView(Context context) {
        super(context);
    }

    public LineDrawAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineDrawAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void startClickAnim(int clickCount, CompanyMapModel mapModel) {

        this.clickCount = clickCount;
        if (clickCount == lastClickCount) {
            if (isDrawChildLine)
                putChildAnim(clickCount, mapModel);
            else
                clickChildAnim(mapModel);
        } else {
            if (isDrawChildLine)
                putChildAnim(lastClickCount, mapModel);
            else
                clickChildAnim(mapModel);
        }

    }

    public void refresh(List<ShareholderModel> shareholderModels
            , List<InvestmentModel> investmentModels
            , int page, int index, String centerString) {

        Log.e("tag","centerStringcenterString"+centerString);
        this.centerString = centerString == null ? "" : centerString;
        ArrayList<BaseModel> leftList = new ArrayList<BaseModel>();
        ArrayList<BaseModel> rightList = new ArrayList<BaseModel>();
        if (shareholderModels == null) {
            shareholderModels = new ArrayList<ShareholderModel>();
        }

        if (investmentModels == null) {
            investmentModels = new ArrayList<InvestmentModel>();
        }

        if (shareholderModels.size() < 1 && investmentModels.size() < 1) {
            refresh(null, true, null, false, centerString);
        } else if (shareholderModels.size() > 0 && investmentModels.size() < 1) {
            if (shareholderModels.size() > (page + 1) * index * 2) {
                for (int i = page * index * 2; i < page * index * 2 + index; i++) {
                    BaseModel baseModel = shareholderModels.get(i);
                    leftList.add(baseModel);
                }
                for (int i = page * index * 2 + index; i < (page + 1) * index * 2; i++) {
                    BaseModel baseModel = shareholderModels.get(i);
                    rightList.add(baseModel);
                }
            } else {
                int size = shareholderModels.size() - page * index * 2;
                if (size % 2 == 0) {
                    for (int i = page * index * 2; i < page * index * 2 + size / 2; i++) {
                        BaseModel baseModel = shareholderModels.get(i);
                        leftList.add(baseModel);
                    }
                    for (int i = page * index * 2 + size / 2; i < page * index * 2 + size; i++) {
                        BaseModel baseModel = shareholderModels.get(i);
                        rightList.add(baseModel);
                    }
                } else {
                    for (int i = page * index * 2; i < page * index * 2 + size / 2 + 1; i++) {
                        BaseModel baseModel = shareholderModels.get(i);
                        leftList.add(baseModel);
                    }
                    for (int i = page * index * 2 + size / 2 + 1; i < page * index * 2 + size; i++) {
                        BaseModel baseModel = shareholderModels.get(i);
                        rightList.add(baseModel);
                    }
                }
            }
            refresh(leftList, true, rightList, true, centerString);
        } else if (shareholderModels.size() < 1 && investmentModels.size() > 0) {
            if (investmentModels.size() > (page + 1) * index * 2) {
                for (int i = page * index * 2; i < page * index * 2 + index; i++) {
                    BaseModel baseModel = investmentModels.get(i);
                    rightList.add(baseModel);
                }
                for (int i = page * index * 2 + index; i < (page + 1) * index * 2; i++) {
                    BaseModel baseModel = investmentModels.get(i);
                    leftList.add(baseModel);
                }
            } else {
                int size = investmentModels.size() - page * index * 2;
                if (size == 1) {
                    BaseModel baseModel = investmentModels.get(page * index * 2);
                    rightList.add(baseModel);
                } else {
                    if (size % 2 == 0) {
                        for (int i = page * index * 2; i < page * index * 2 + size / 2; i++) {
                            BaseModel baseModel = investmentModels.get(i);
                            rightList.add(baseModel);

                        }
                        for (int i = page * index * 2 + size / 2; i < page * index * 2 + size; i++) {
                            BaseModel baseModel = investmentModels.get(i);
                            leftList.add(baseModel);
                        }
                    } else {
                        for (int i = page * index * 2; i < page * index * 2 + size / 2 + 1; i++) {
                            BaseModel baseModel = investmentModels.get(i);
                            rightList.add(baseModel);

                        }
                        for (int i = page * index * 2 + size / 2 + 1; i < page * index * 2 + size; i++) {
                            BaseModel baseModel = investmentModels.get(i);
                            leftList.add(baseModel);
                        }
                    }
                }
            }
            refresh(leftList, false, rightList, false, centerString);
        } else {
            if (shareholderModels.size() > (page + 1) * index) {
                for (int i = page * index; i < (page + 1) * index; i++) {
                    BaseModel baseModel = shareholderModels.get(i);
                    leftList.add(baseModel);
                }
            } else if (shareholderModels.size() == (page + 1) * index) {
                for (int i = shareholderModels.size() - index; i < shareholderModels.size(); i++) {
                    BaseModel baseModel = shareholderModels.get(i);
                    leftList.add(baseModel);
                }
            } else {
                if (shareholderModels.size()%index==0) {
                    for (int i = shareholderModels.size() - index; i < shareholderModels.size(); i++) {
                        BaseModel baseModel = shareholderModels.get(i);
                        rightList.add(baseModel);
                    }
                }else {
                    int start = (shareholderModels.size() / index) * index;
                    for (int i = start; i < shareholderModels.size(); i++) {
                        BaseModel baseModel = shareholderModels.get(i);
                        leftList.add(baseModel);
                    }
                }
            }

            if (investmentModels.size() > (page + 1) * index) {
                for (int i = page * index; i < (page + 1) * index; i++) {
                    BaseModel baseModel = investmentModels.get(i);
                    rightList.add(baseModel);
                }
            } else if (investmentModels.size() == (page + 1) * index) {
                for (int i = investmentModels.size() - index; i < investmentModels.size(); i++) {
                    BaseModel baseModel = investmentModels.get(i);
                    rightList.add(baseModel);
                }
            } else {
                if (investmentModels.size()%index==0) {
                    for (int i = investmentModels.size() - index; i < investmentModels.size(); i++) {
                        BaseModel baseModel = investmentModels.get(i);
                        rightList.add(baseModel);
                    }
                }else {
                    int start = (investmentModels.size() / index) * index;
                    for (int i = start; i < investmentModels.size(); i++) {
                        BaseModel baseModel = investmentModels.get(i);
                        rightList.add(baseModel);
                    }
                }
            }
            refresh(leftList, true, rightList, false, centerString);
        }
    }

    private void refresh(List<BaseModel> left, boolean leftToCen
            , List<BaseModel> right, boolean rightToCen
            , String companyName) {

        this.left = left;
        this.right = right;
        centerString = companyName == null ? "" : companyName;
        if (left == null && right == null) {
            //测试
            leftCount = 0;
            rightCount = 0;
        } else if (left.size() == 0 && right.size() == 0) {
            leftCount = 0;
            rightCount = 0;
        } else {
            leftCount = left.size();
            rightCount = right.size();
        }

        this.leftToCen = leftToCen;
        this.rightToCen = rightToCen;

        leftArrowAngle = new PointF[leftCount];
        rightArrowAngle = new PointF[rightCount];
        mStaticLayout = new StaticLayout(centerString, cirPaint
                , PhoneUtil.dip2px(context, 36), Layout.Alignment.ALIGN_NORMAL, 1, 0, false);

        leftAngles = new float[leftCount];
        rightAngles = new float[rightCount];

        float leftAngle = 0, rightAngle = 0;
        if (leftCount > 1) {
            leftAngle = 90.0f / (leftCount - 1);
        }
        if (rightCount > 1) {
            rightAngle = 90.0f / (rightCount - 1);
        }
        leftLines.clear();
        leftPaints.clear();
        rightLines.clear();
        rightPaints.clear();
        leftRects.clear();
        rightRects.clear();
        leftInfo.clear();
        rightInfo.clear();
        childList.clear();
        childPoint.clear();
        leftStaticLayout.clear();
        rightStaticLayout.clear();
        mTran[0] = 0;
        mTran[1] = 0;
        leftChildCount = 0;
        rightChildCount = 0;
        isDrawChildLine = false;

        for (int i = 0; i < leftCount; i++) {

            Paint paint = new Paint();
            paint.setStrokeWidth(2);
            paint.setAlpha(255);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setTextSize(20);
            paint.setTextAlign(Paint.Align.RIGHT);
            if (leftToCen) {
                ShareholderModel shareholderModel = (ShareholderModel) left.get(i);
                if (1 == shareholderModel.getType())
                    leftInfo.add(shareholderModel.getShortName());
                else if (2 == shareholderModel.getType()) {
                    leftInfo.add(shareholderModel.getName());
                }
                paint.setColor(context.getResources().getColor(R.color.shareholder));
                PointF arrow = new PointF();
                if (leftCount == 1) {
                    arrow.x = MathHelper.getInstance().getPointF(180, mCir.x, mCir.y, mRaius * 0.4f).x;
                    arrow.y = MathHelper.getInstance().getPointF(180, mCir.x, mCir.y, mRaius * 0.4f).y;
                    leftAngles[i] = 180;
                } else {
                    arrow.x = MathHelper.getInstance().getPointF(leftAngle * i + 135, mCir.x, mCir.y, mRaius * 0.4f).x;
                    arrow.y = MathHelper.getInstance().getPointF(leftAngle * i + 135, mCir.x, mCir.y, mRaius * 0.4f).y;
                    leftAngles[i] = leftAngle * i + 135;
                }
                leftArrowAngle[i] = arrow;
            } else {
                InvestmentModel investmentModel = (InvestmentModel) left.get(i);
                leftInfo.add(investmentModel.getShortName());
                paint.setColor(context.getResources().getColor(R.color.investment));
                PointF arrow = new PointF();
                if (leftCount == 1) {
                    arrow.x = MathHelper.getInstance().getPointF(180, mCir.x, mCir.y, mRaius * 0.8f).x;
                    arrow.y = MathHelper.getInstance().getPointF(180, mCir.x, mCir.y, mRaius * 0.8f).y;
                    leftAngles[i] = 180;
                } else {
                    arrow.x = MathHelper.getInstance().getPointF(leftAngle * i + 135, mCir.x, mCir.y, mRaius * 0.8f).x;
                    arrow.y = MathHelper.getInstance().getPointF(leftAngle * i + 135, mCir.x, mCir.y, mRaius * 0.8f).y;
                    leftAngles[i] = leftAngle * i + 135;
                }
                leftArrowAngle[i] = arrow;
            }
            leftPaints.add(paint);

            PointF pointf = new PointF();
            if (leftCount == 1) {
                pointf.x = MathHelper.getInstance().getPointF(180, mCir.x, mCir.y, mRaius).x;
                pointf.y = MathHelper.getInstance().getPointF(180, mCir.x, mCir.y, mRaius).y;
            } else {
                pointf.x = MathHelper.getInstance().getPointF(leftAngle * i + 135, mCir.x, mCir.y, mRaius).x;
                pointf.y = MathHelper.getInstance().getPointF(leftAngle * i + 135, mCir.x, mCir.y, mRaius).y;
            }
            leftLines.add(pointf);

            RectF rect = new RectF(pointf.x - rectAddCount, pointf.y - rectAddCount, pointf.x + rectAddCount, pointf.y + rectAddCount);
            leftRects.add(rect);
            if(leftInfo!=null&&leftInfo.get(i)!=null) {
                StaticLayout staticLayout = new StaticLayout(leftInfo.get(i), cirPaint, PhoneUtil.dip2px(context, 36), Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
                leftStaticLayout.add(staticLayout);
            }
        }

        for (int i = 0; i < rightCount; i++) {

            Paint paint = new Paint();
            paint.setStrokeWidth(2);
            paint.setAlpha(255);
            paint.setAntiAlias(true);
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setTextSize(20);
            if (rightToCen) {
                ShareholderModel shareholderModel = (ShareholderModel) right.get(i);
                if (1 == shareholderModel.getType()) {
                    rightInfo.add(shareholderModel.getShortName());
                } else if (2 == shareholderModel.getType()) {
                    rightInfo.add(shareholderModel.getName());
                }
                paint.setColor(context.getResources().getColor(R.color.shareholder));
                PointF arrow = new PointF();
                if (rightCount == 1) {
                    arrow.x = MathHelper.getInstance().getPointF(0, mCir.x, mCir.y, mRaius * 0.4f).x;
                    arrow.y = MathHelper.getInstance().getPointF(0, mCir.x, mCir.y, mRaius * 0.4f).y;
                    rightAngles[i] = 0;
                } else {
                    arrow.x = MathHelper.getInstance().getPointF(rightAngle * i + 315, mCir.x, mCir.y, mRaius * 0.4f).x;
                    arrow.y = MathHelper.getInstance().getPointF(rightAngle * i + 315, mCir.x, mCir.y, mRaius * 0.4f).y;
                    rightAngles[i] = rightAngle * i + 315 >= 360 ? (rightAngle * i + 315 - 360) : rightAngle * i + 315;
                }
                rightArrowAngle[i] = arrow;
            } else {
                InvestmentModel investmentModel = (InvestmentModel) right.get(i);
                rightInfo.add(investmentModel.getShortName());
                paint.setColor(context.getResources().getColor(R.color.investment));
                PointF arrow = new PointF();
                if (rightCount == 1) {
                    arrow.x = MathHelper.getInstance().getPointF(0, mCir.x, mCir.y, mRaius * 0.8f).x;
                    arrow.y = MathHelper.getInstance().getPointF(0, mCir.x, mCir.y, mRaius * 0.8f).y;
                    rightAngles[i] = 0;
                } else {
                    arrow.x = MathHelper.getInstance().getPointF(rightAngle * i + 315, mCir.x, mCir.y, mRaius * 0.8f).x;
                    arrow.y = MathHelper.getInstance().getPointF(rightAngle * i + 315, mCir.x, mCir.y, mRaius * 0.8f).y;
                    rightAngles[i] = rightAngle * i + 315 >= 360 ? (rightAngle * i + 315 - 360) : rightAngle * i + 315;
                }
                rightArrowAngle[i] = arrow;
            }
            rightPaints.add(paint);
            PointF pointf = new PointF();
            if (rightCount == 1) {
                pointf.x = MathHelper.getInstance().getPointF(0, mCir.x, mCir.y, mRaius).x;
                pointf.y = MathHelper.getInstance().getPointF(0, mCir.x, mCir.y, mRaius).y;
            } else {
                pointf.x = MathHelper.getInstance().getPointF(rightAngle * i + 315, mCir.x, mCir.y, mRaius).x;
                pointf.y = MathHelper.getInstance().getPointF(rightAngle * i + 315, mCir.x, mCir.y, mRaius).y;
            }
            rightLines.add(pointf);

            RectF rect = new RectF(pointf.x - rectAddCount, pointf.y - rectAddCount, pointf.x + rectAddCount, pointf.y + rectAddCount);
            rightRects.add(rect);
            if(rightInfo!=null&&rightInfo.get(i)!=null) {
                StaticLayout staticLayout = new StaticLayout(rightInfo.get(i), cirPaint, PhoneUtil.dip2px(context, 36), Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
                rightStaticLayout.add(staticLayout);
            }
        }

        startAnim();
    }

    public void startAnim() {
        leftAnims.clear();
        leftPoints.clear();
        rightAnims.clear();
        rightPoints.clear();
        isAnimStop = allAnimStop;
        enableTouch = false;
        for (int i = 0; i < leftCount; i++) {
            ArrayList<PointF> list = new ArrayList<PointF>();
            ValueAnimator valu = getAnim(mCir, leftLines.get(i), list);
            leftAnims.add(valu);
            leftPoints.add(list);
            valu.start();
        }

        for (int i = 0; i < rightCount; i++) {
            ArrayList<PointF> list = new ArrayList<PointF>();
            ValueAnimator valu = getAnim(mCir, rightLines.get(i), list);
            rightAnims.add(valu);
            rightPoints.add(list);
            valu.start();
        }

        if (thread == null) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!threadStop) {
                        if (mRaiusCount >= 60) {
                            mRaiusCount = 30;
                        } else
                            mRaiusCount++;
                        index++;
                        childIndex++;
                        try {
                            Thread.sleep(50);
                            postInvalidate();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
//                            Log.e("thread", e.toString());
                        }
                    }
                }
            });
        }
        if (!thread.isAlive())
            thread.start();
    }

    public ValueAnimator getAnim(PointF startPoint, PointF endPoint, final ArrayList<PointF> list) {
        PointF[] linePoints = new PointF[2];
        linePoints[0] = startPoint;
        linePoints[1] = endPoint;
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new BezierOneTypeEvaluator(linePoints), new PointF());
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (1000 <= animation.getCurrentPlayTime()) {
                    if (isDrawChildLine)
                        childAnimStop = true;
                    else
                        isAnimStop = true;
                }
                PointF p = (PointF) animation.getAnimatedValue();
                PointF p1 = new PointF(p.x, p.y);
                if (!list.contains(p1))
                    list.add(p1);
                postInvalidate();
            }
        });
        return valueAnimator;
    }

    public void clickChildAnim(CompanyMapModel mapModel) {
        childRect.clear();
        childList.clear();
        childTxtPaints.clear();
        childPaints.clear();
        childInfo.clear();
        child.clear();
        leftChildCount = mapModel.getShareholders().size();
        rightChildCount = mapModel.getInvestments().size();
        if (leftChildCount!=0&&rightChildCount!=0) {
            if (leftChildCount > 4) {
                leftChildCount = 4;
            }
            if (rightChildCount > 4) {
                rightChildCount = 4;
            }
        }else if (leftChildCount==0){
            if (rightChildCount>8){
                rightChildCount=8;
            }
        }else if (rightChildCount==0){
            if (leftChildCount>8) {
                leftChildCount = 8;
            }
        }
        childAngles = new float[leftChildCount + rightChildCount];
        childArrowAngle = new PointF[leftChildCount + rightChildCount];
        enableTouch = false;
        count = 0;
        lastIndex = 0;
        float angle = 0;
        if (leftChildCount + rightChildCount != 1)
            angle = 90 / (leftChildCount + rightChildCount - 1);
        for (int i = 0; i < leftChildCount + rightChildCount; i++) {
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(context.getResources().getColor(R.color.color_text_company_map_info));
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setTextSize(25);
            textPaint.setAlpha(255);
            if (clickCount < leftCount) {
                textPaint.setTextAlign(Paint.Align.RIGHT);
                if (leftChildCount + rightChildCount == 1)
                    childAngles[leftChildCount + rightChildCount - i - 1] = 180;
                else
                    childAngles[leftChildCount + rightChildCount - i - 1] = 135 + angle * i;
            } else {
                textPaint.setTextAlign(Paint.Align.LEFT);
                if (leftChildCount + rightChildCount == 1)
                    childAngles[leftChildCount + rightChildCount - i - 1] = 0;
                else
                    childAngles[leftChildCount + rightChildCount - i - 1]
                            = 315 + angle * i >= 360 ? 315 + angle * i - 360 : 315 + angle * i;
            }
            childTxtPaints.add(textPaint);
            Paint paint = new Paint();
            paint.setStrokeWidth(2);
            paint.setAlpha(255);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setTextAlign(Paint.Align.RIGHT);
            if (i < leftChildCount) {
                paint.setColor(context.getResources().getColor(R.color.shareholder));
                ShareholderModel shareholderModel = mapModel.getShareholders().get(i);
                if (shareholderModel.getType() == 1)
                    childInfo.add(shareholderModel.getShortName());
                else if (shareholderModel.getType() == 2)
                    childInfo.add(shareholderModel.getName());
                child.add(shareholderModel);
            } else {
                paint.setColor(context.getResources().getColor(R.color.investment));
                childInfo.add(mapModel.getInvestments().get(i - leftChildCount).getShortName());
                child.add(mapModel.getInvestments().get(i - leftChildCount));
            }
            childPaints.add(paint);
        }

        animChild.removeAllUpdateListeners();
        animChild.setDuration(1000);
        animChild.setIntValues(0, 100);
        animChild.setRepeatMode(1);
        animChild.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int index = (Integer) animation.getAnimatedValue();
//                Log.e(TAG, animation.getStartDelay() + "\n" + animation.getRepeatCount() + "\n" + animation.getCurrentPlayTime());
                if (index >= 100) {
                    lastClickCount = clickCount;
                    childAnimStop = true;
                    isDrawChildLine = true;
                    lastIndex = 0;
                    if (clickCount < leftCount) {
                        leftRects.get(clickCount).set(leftLines.get(clickCount).x - rectAddCount, leftLines.get(clickCount).y - rectAddCount
                                , leftLines.get(clickCount).x + rectAddCount, leftLines.get(clickCount).y + rectAddCount);
                        for (int i = 0; i < leftChildCount + rightChildCount; i++) {
                            PointF pointf = new PointF();
                            pointf.x = MathHelper.getInstance().getPointF(childAngles[i]
                                    , leftLines.get(clickCount).x, leftLines.get(clickCount).y, mRaius ).x;
                            pointf.y = MathHelper.getInstance().getPointF(childAngles[i]
                                    , leftLines.get(clickCount).x, leftLines.get(clickCount).y, mRaius).y;
                            if (!childList.contains(pointf))
                                childList.add(pointf);

                            RectF rectf = new RectF(pointf.x - 100, pointf.y - 100, pointf.x + 100, pointf.y + 100);
                            if (!childRect.contains(rectf)) {
                                childRect.add(rectf);
                            }

                            PointF point = new PointF();
                            if (i < leftChildCount) {
                                point.x = MathHelper.getInstance().getPointF(childAngles[i]
                                        , leftLines.get(clickCount).x, leftLines.get(clickCount).y, mRaius * 0.4f).x;
                                point.y = MathHelper.getInstance().getPointF(childAngles[i]
                                        , leftLines.get(clickCount).x, leftLines.get(clickCount).y, mRaius * 0.4f).y;
                            } else {
                                point.x = MathHelper.getInstance().getPointF(childAngles[i]
                                        , leftLines.get(clickCount).x, leftLines.get(clickCount).y, mRaius * 0.4f).x;
                                point.y = MathHelper.getInstance().getPointF(childAngles[i]
                                        , leftLines.get(clickCount).x, leftLines.get(clickCount).y, mRaius * 0.4f).y;
                            }
                            childArrowAngle[i] = point;
                        }
                    } else {
                        rightRects.get(clickCount - leftCount).set(rightLines.get(clickCount - leftCount).x - rectAddCount, rightLines.get(clickCount - leftCount).y - rectAddCount
                                , rightLines.get(clickCount - leftCount).x + rectAddCount, rightLines.get(clickCount - leftCount).y + rectAddCount);
                        for (int i = 0; i < leftChildCount + rightChildCount; i++) {
                            PointF pointf = new PointF();
                            pointf.x = MathHelper.getInstance().getPointF(childAngles[i]
                                    , rightLines.get(clickCount - leftCount).x, rightLines.get(clickCount - leftCount).y, mRaius ).x;
                            pointf.y = MathHelper.getInstance().getPointF(childAngles[i]
                                    , rightLines.get(clickCount - leftCount).x, rightLines.get(clickCount - leftCount).y, mRaius ).y;
                            if (!childList.contains(pointf))
                                childList.add(pointf);

                            RectF rectf = new RectF(pointf.x - 50, pointf.y - 50, pointf.x + 50, pointf.y + 50);
                            if (!childRect.contains(rectf)) {
                                childRect.add(rectf);
                            }

                            PointF point = new PointF();
                            if (i < leftChildCount) {
                                point.x = MathHelper.getInstance().getPointF(childAngles[i]
                                        , rightLines.get(clickCount - leftCount).x, rightLines.get(clickCount - leftCount).y, mRaius * 0.4f).x;
                                point.y = MathHelper.getInstance().getPointF(childAngles[i]
                                        , rightLines.get(clickCount - leftCount).x, rightLines.get(clickCount - leftCount).y, mRaius * 0.4f).y;
                            } else {
                                point.x = MathHelper.getInstance().getPointF(childAngles[i]
                                        , rightLines.get(clickCount - leftCount).x, rightLines.get(clickCount - leftCount).y, mRaius * 0.6f).x;
                                point.y = MathHelper.getInstance().getPointF(childAngles[i]
                                        , rightLines.get(clickCount - leftCount).x, rightLines.get(clickCount - leftCount).y, mRaius * 0.6f).y;
                            }
                            childArrowAngle[i] = point;
                        }
                    }
                    childAnim();
                } else {
                    if (clickCount < leftCount && clickCount >= 0) {
                        //根据角度，算出偏移值，根据sin,cos值正负会自己获取是增是减
                        float argAngle = (float) (Math.PI * leftAngles[clickCount] / 180f);
                        leftLines.get(clickCount).x += Math.cos(argAngle) * (index - lastIndex) * mRaius * 0.01f;
                        leftLines.get(clickCount).y += Math.sin(argAngle) * (index - lastIndex) * mRaius * 0.01f;
//                        Log.e(TAG, "x=" + leftLines.get(clickCount).x + "\ny=" + leftLines.get(clickCount).y + "index" + index);
                        mTran[0] = (float) (-(mCir.x + mRaius) * index * Math.cos(argAngle) / 150);
                        mTran[1] = (float) (-(mCir.y + mRaius * 2) * index * Math.sin(argAngle) / 150);
                    } else if (clickCount >= 0) {
                        float argAngle = (float) (Math.PI * rightAngles[clickCount - leftCount] / 180f);
                        rightLines.get(clickCount - leftCount).x += Math.cos(argAngle) * (index - lastIndex) * mRaius * 0.01f;
                        rightLines.get(clickCount - leftCount).y += Math.sin(argAngle) * (index - lastIndex) * mRaius * 0.01f;
                        mTran[0] = (float) (-(mCir.x + mRaius) * index * Math.cos(argAngle) / 150);
                        mTran[1] = (float) (-(mCir.y + mRaius * 2) * index * Math.sin(argAngle) / 150);
                    }
                    count++;
                    lastIndex = index;
                }
                postInvalidate();
            }
        });
        animChild.start();
    }

    private void childAnim() {
        if (!childAnimStop)
            return;
        childAnimStop = false;
        childPoint.clear();
        for (int i = 0; i < childList.size(); i++) {
            PointF point = childList.get(i);
            ArrayList<PointF> list = new ArrayList<PointF>();
            ValueAnimator valu = null;
            if (clickCount < leftCount)
                valu = getAnim(leftLines.get(clickCount), point, list);
            else
                valu = getAnim(rightLines.get(clickCount - leftCount), point, list);
            valu.setDuration(1000);
            if (!childPoint.contains(list))
                childPoint.add(list);
            valu.start();
        }
    }

    private void putChildAnim(final int click, final CompanyMapModel mapModel) {
        putChildAnim = true;
        childRect.clear();
        childList.clear();
        enableTouch = false;
        lastIndex = 0;
        animPutChild.removeAllUpdateListeners();
        animPutChild.setDuration(500);
        animPutChild.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int index = (Integer) animation.getAnimatedValue();
                if (index >= 100) {
                    if (click < leftCount) {
                        leftRects.get(click).set(leftLines.get(click).x - rectAddCount, leftLines.get(click).y - rectAddCount
                                , leftLines.get(click).x + rectAddCount, leftLines.get(click).y + rectAddCount);
                    } else {
                        rightRects.get(click - leftCount).set(rightLines.get(click - leftCount).x - rectAddCount, rightLines.get(click - leftCount).y - rectAddCount
                                , rightLines.get(click - leftCount).x + rectAddCount, rightLines.get(click - leftCount).y + rectAddCount);
                    }
                    putChildAnim = false;
                    if (clickCount != click && clickCount != -1 && click != -1)
                        clickChildAnim(mapModel);
                    lastIndex = 0;
                    lastClickCount = clickCount;
                    return;
                }
                if (click < leftCount && click >= 0) {
                    //根据角度，算出偏移值，根据sin,cos值正负会自己获取是增是减
                    float argAngle = (float) (Math.PI * leftAngles[click] / 180f);
                    mTran[0] = (float) -((mCir.x + mRaius) * (100 - index) * Math.cos(argAngle) / 150);
                    mTran[1] = (float) -((mCir.y + mRaius) * (100 - index) * Math.sin(argAngle) / 150);
                    leftLines.get(click).x -= Math.cos(argAngle) * (index - lastIndex) * mRaius * 0.01f;
                    leftLines.get(click).y -= Math.sin(argAngle) * (index - lastIndex) * mRaius * 0.01f;
//                    Log.e(TAG, "xx=" + leftLines.get(click).x + "\nyy=" + leftLines.get(click).y + "\nindex" + count);
                } else if (click >= 0) {
                    float argAngle = (float) (Math.PI * rightAngles[click - leftCount] / 180f);
                    mTran[0] = (float) -((mCir.x + mRaius) * (100 - index) * Math.cos(argAngle) / 150);
                    mTran[1] = (float) -((mCir.y + mRaius) * (100 - index) * Math.sin(argAngle) / 100);
                    rightLines.get(click - leftCount).x -= Math.cos(argAngle) * (index - lastIndex) * mRaius * 0.01f;
                    rightLines.get(click - leftCount).y -= Math.sin(argAngle) * (index - lastIndex) * mRaius * 0.01f;
                }
                lastIndex = index;
            }
        });
        postInvalidate();
    }
}