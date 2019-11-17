package com.fm.lvplay;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * 三种颜色选中模式/菜单模式
 * 该设计方式，有个很郁闷的地方，水平滚动，水平方向不能丢失焦点，
 */
public class MenuRecyclerView extends RecyclerView {

    protected int mFocusedPosition = -1;
    protected int mActivatedPosition = -1;
    private int mOrientation;
    private int mSelectedItemOffsetStart, mSelectedItemOffsetEnd;   //滚动到中部位置

    //是否可以纵向移出
    private boolean mCanFocusOutVertical = true;
    //是否可以横向移出
    private boolean mCanFocusOutHorizontal = true;

    private String viewName = MenuRecyclerView.class.getSimpleName();

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public MenuRecyclerView(@NonNull Context context) {
        super(context);
    }

    public MenuRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onChildAttachedToWindow(@NonNull final View child) {
        super.onChildAttachedToWindow(child);
        child.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(child, getChildViewHolder(child).getAdapterPosition());
                }
            }
        });

        child.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, boolean b) {
                if (mItemFocusListener != null) {
                    mItemFocusListener.onItemFocusChange(view, getChildViewHolder(view).getAdapterPosition(), b);
                }

                if (b) {
                    mFocusedPosition = getChildViewHolder(view).getAdapterPosition();
                    if (view.isActivated()) {
                        view.setActivated(false);
                        if (mItemActivatedListener != null) {
                            mItemActivatedListener.onItemActivated(view, mFocusedPosition, false);
                        }
                        Log.e(viewName, "not activated:" + mFocusedPosition);
                    }
                }
            }
        });

        child.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mItemLongClickListener != null) {
                    return mItemLongClickListener.onItemLongClick(child, getChildViewHolder(view).getAdapterPosition());
                }
                return false;
            }
        });
    }

    @Override
    public void onChildDetachedFromWindow(@NonNull View child) {
        super.onChildDetachedFromWindow(child);
        child.setOnClickListener(null);
        child.setOnLongClickListener(null);
        child.setOnFocusChangeListener(null);
    }

    /**
     * 重写这个方法，可以控制焦点框距离父容器的距离,以及由于recyclerView的滚动
     * 产生的偏移量，导致焦点框错位，这里可以记录滑动偏移量。
     * RecyclerView 版本27以及更高版本必须配合V7LinearLayoutManager 才能实现，焦点item居中效果
     */
    @Override
    public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
        //计算出当前viewGroup即是RecyclerView的内容区域
        final int parentLeft = getPaddingLeft();
        final int parentTop = getPaddingTop();
        final int parentRight = getWidth() - getPaddingRight();
        final int parentBottom = getHeight() - getPaddingBottom();

        //计算出child,此时是获取焦点的view请求的区域
        final int childLeft = child.getLeft() + rect.left;
        final int childTop = child.getTop() + rect.top;
        final int childRight = childLeft + rect.width();
        final int childBottom = childTop + rect.height();

        //获取请求区域四个方向与RecyclerView内容四个方向的距离
        final int offScreenLeft = Math.min(0, childLeft - parentLeft - mSelectedItemOffsetStart);
        final int offScreenTop = Math.min(0, childTop - parentTop - mSelectedItemOffsetStart);
        final int offScreenRight = Math.max(0, childRight - parentRight + mSelectedItemOffsetEnd);
        final int offScreenBottom = Math.max(0, childBottom - parentBottom + mSelectedItemOffsetEnd);

        final boolean canScrollHorizontal = getLayoutManager().canScrollHorizontally();
        int dx;
        if (canScrollHorizontal) {
            if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                dx = offScreenRight != 0 ? offScreenRight
                        : Math.max(offScreenLeft, childRight - parentRight);
            } else {
                dx = offScreenLeft != 0 ? offScreenLeft
                        : Math.min(childLeft - parentLeft, offScreenRight);
            }
        } else {
            dx = 0;
        }
        int dy = offScreenTop != 0 ? offScreenTop
                : Math.min(childTop - parentTop, offScreenBottom);
        //在这里可以微调滑动的距离,根据项目的需要
        if (dx != 0 || dy != 0) {
            Log.d(viewName, "immediate:" + immediate + ",dx" + dx + ",dy:" + dy);
            //最后执行滑动
            if (immediate) {
                scrollBy(dx, dy);
            } else {
                smoothScrollBy(dx, dy);
            }
            return true;
        }
        postInvalidate();
        return false;
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if (layout instanceof GridLayoutManager) {
            mOrientation = ((GridLayoutManager) layout).getOrientation();
        } else if (layout instanceof LinearLayoutManager) {
            mOrientation = ((LinearLayoutManager) layout).getOrientation();
        } else {
            throw new IllegalArgumentException("not support StaggeredGridLayoutManager");
        }
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        if (null != child) {
            mSelectedItemOffsetStart = (mOrientation == HORIZONTAL) ? (getFreeWidth() - child.getWidth()) : (getFreeHeight() - child.getHeight());
            mSelectedItemOffsetStart /= 2;
            mSelectedItemOffsetEnd = mSelectedItemOffsetStart;
        }
        super.requestChildFocus(child, focused);
    }

    //实现焦点记忆的关键代码
    @Override
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        View view = null;
        if (this.hasFocus() || mFocusedPosition < 0 || (view = getLayoutManager().findViewByPosition(mFocusedPosition)) == null) {
            super.addFocusables(views, direction, focusableMode);
        } else if (view.isFocusable()) {
            views.add(view);    //将当前的view放到Focusable views列表中，再次移入焦点时会取到该view,实现焦点记忆功能
        } else {
            super.addFocusables(views, direction, focusableMode);
        }
    }

    /**
     * 控制当前焦点最后绘制，防止焦点放大后被遮挡
     * 原顺序123456789，当4是focus时，绘制顺序变为123567894
     */
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        View focusedChild = getFocusedChild();
        Log.i(viewName, "focusedChild =" + focusedChild);
        if (focusedChild == null) {
            return super.getChildDrawingOrder(childCount, i);
        } else {
            int index = indexOfChild(focusedChild);
            Log.i(viewName, " index = " + index + ",i=" + i + ",count=" + childCount);
            if (i == childCount - 1) {
                return index;
            }
            if (i < index) {
                return i;
            }
            return i + 1;
        }
    }

    @Override
    public View focusSearch(View focused, int direction) {
        Log.i(viewName, "focusSearch " + getChildViewHolder(focused).getAdapterPosition() + ",direction= " + direction);
        View view = super.focusSearch(focused, direction);
        if (focused == null) {
            return view;
        }
        if (view != null) {
            //该方法返回焦点view所在的父view,如果是在recyclerview之外，就会是null.所以根据是否是null,来判断是否是移出了recyclerview
            View nextFocusItemView = findContainingItemView(view);
            if (nextFocusItemView == null) {
                if (!mCanFocusOutVertical && (direction == View.FOCUS_DOWN || direction == View.FOCUS_UP)) {
                    return focused;     //屏蔽焦点纵向移出recyclerview
                }
                if (!mCanFocusOutHorizontal && (direction == View.FOCUS_LEFT || direction == View.FOCUS_RIGHT)) {
                    return focused;     //屏蔽焦点横向移出recyclerview
                }
                //调用移出的监听
                if (!focused.isActivated()) {
                    focused.setActivated(true);
                    mActivatedPosition = getChildViewHolder(focused).getAdapterPosition();
                    if (mItemActivatedListener != null) {
                        mItemActivatedListener.onItemActivated(focused, mActivatedPosition, true);
                    }
                    onFocusChanged(false, FOCUS_DOWN, null);
                }
                if (mFocusLostListener != null) {
                    mFocusLostListener.onFocusLost(focused, direction);
                }
                return view;
            }
        }
        return view;
    }

    /**
     * 滚动到指定位置，并且高亮显示
     *
     * @param position
     */
    @Override
    public void smoothScrollToPosition(int position) {
        super.smoothScrollToPosition(position);
        if (position > getItemCount()) {
            return;
        }
        if (mActivatedPosition == position) {
            return;
        }

        View currentView = getChildAt(mActivatedPosition - getFirstVisiblePosition());
        if (currentView != null) {
            currentView.setActivated(false);
            if (mItemActivatedListener != null) {
                mItemActivatedListener.onItemActivated(currentView, mActivatedPosition, false);
            }
        } else {
            Log.i(viewName, "old Activated,view is null");
        }
        mFocusedPosition = position;
        mActivatedPosition = position;
    }

    private void handleScroll(final int position) {
        if (position > getItemCount()) {
            return;
        }
        if (mActivatedPosition == position) {
            return;
        }

        postDelayed(new Runnable() {
            @Override
            public void run() {
                View view = getChildAt(position - getFirstVisiblePosition());
                if (view != null) {
                    view.setActivated(true);
                    if (mItemActivatedListener != null) {
                        mItemActivatedListener.onItemActivated(view, position, true);
                    }
                } else {
                    Log.i(viewName, "new Activated,view is null");
                }
                mFocusedPosition = position;
                mActivatedPosition = position;
            }
        }, 50);
    }

    @Override
    public void scrollToPosition(final int position) {
        super.scrollToPosition(position);
        handleScroll(position);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == SCROLL_STATE_IDLE) {
            Log.d(viewName, "state:SCROLL_STATE_IDLE");
            if (!hasFocus()) {
                View view = getChildAt(mActivatedPosition - getFirstVisiblePosition());
                if (view != null) {
                    if (!view.isActivated()) {
                        view.setActivated(true);
                        if (mItemActivatedListener != null) {
                            mItemActivatedListener.onItemActivated(view, mActivatedPosition, true);
                        }
                    } else {
                        Log.i(viewName, "stop scroll,Activated,view is isActivated.........");
                    }
                } else {
                    Log.i(viewName, " stop scroll, Activated, view is null.........");
                }
            }
        } else if (state == SCROLL_STATE_DRAGGING) {
            Log.d(viewName, "state:SCROLL_STATE_DRAGGING");
        } else if (state == SCROLL_STATE_SETTLING) {
            Log.d(viewName, "state:SCROLL_STATE_SETTLING");
        } else {
            Log.d(viewName, "state UNKNOW:" + state);  //仅仅为了观察日志
        }
    }


    public int getFirstVisiblePosition() {
        if (getChildCount() == 0)
            return 0;
        else {
            int firstPos = getChildAdapterPosition(getChildAt(0));
            Log.d(viewName, "firstPos:" + firstPos);
            return firstPos;
        }

    }

    public int getItemCount() {
        if (null != getAdapter()) {
            return getAdapter().getItemCount();
        }
        return 0;
    }

    public int getFocusedPosition() {
        return mFocusedPosition;
    }


    public void setCurrentPosition(int mCurrentPosition) {
        if (mCurrentPosition >= getItemCount()) {
            mCurrentPosition = -1;
        }
        this.mFocusedPosition = mCurrentPosition;
    }

    public int getActivatedPosition() {
        return mActivatedPosition;
    }


    private int getFreeWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int getFreeHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }


    private ItemClickListener mItemClickListener;
    private ItemFocusListener mItemFocusListener;
    private ItemLongClickListener mItemLongClickListener;
    private ItemActivatedListener mItemActivatedListener;
    private FocusLostListener mFocusLostListener;

    public void setItemActivatedListener(ItemActivatedListener mItemActivatedListener) {
        this.mItemActivatedListener = mItemActivatedListener;
    }

    public void setOnItemLongClickListener(ItemLongClickListener mItemLongClickListener) {
        this.mItemLongClickListener = mItemLongClickListener;
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setOnItemFocusListener(ItemFocusListener mItemFocusListener) {
        this.mItemFocusListener = mItemFocusListener;
    }

    public void setCanFocusOutVertical(boolean mCanFocusOutVertical) {
        this.mCanFocusOutVertical = mCanFocusOutVertical;
    }

    public void setCanFocusOutHorizontal(boolean mCanFocusOutHorizontal) {
        this.mCanFocusOutHorizontal = mCanFocusOutHorizontal;
    }

    public interface ItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public interface ItemLongClickListener {
        boolean onItemLongClick(View itemView, int position);
    }

    public interface ItemFocusListener {
        void onItemFocusChange(View itemView, int position, boolean hasFocus);
    }

    public interface ItemActivatedListener {
        void onItemActivated(View itemView, int position, boolean activated);
    }

    public void setFocusLostListener(FocusLostListener focusLostListener) {
        this.mFocusLostListener = focusLostListener;
    }

    public interface FocusLostListener {
        void onFocusLost(View lastFocusChild, int direction);
    }
}
