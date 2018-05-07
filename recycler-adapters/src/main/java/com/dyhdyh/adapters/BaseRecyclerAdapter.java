package com.dyhdyh.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * author  dengyuhan
 * created 2017/1/26 14:42
 */
public abstract class BaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private List<T> mData;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    public BaseRecyclerAdapter(List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : data;
    }

    @Override
    public void onBindViewHolder(VH holder, int position, List<Object> payloads) {
        if (payloads == null || payloads.isEmpty()) {
            this.onBindViewHolder(holder, position);
        } else {
            this.onBindViewHolder(holder, position, getItem(position), payloads.get(0));
        }
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        this.onBindViewHolder(holder, position, getItem(position));
        if (holder.itemView != null) {
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new OnItemClickListenerImpl(this, position));
            }
            if (mItemLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new OnItemLongClickListenerImpl(this, position));
            }
        }
    }


    public void onBindViewHolder(VH holder, int position, T item, Object payload) {
        onBindViewHolder(holder, position, item);
    }

    public abstract void onBindViewHolder(VH holder, int position, T item);

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 获取条目数据
     *
     * @param position 要获取的对象在适配器中的位置
     * @return position位置的对象
     */
    public T getItem(int position) {
        T item;
        try {
            item = mData.get(position);
        } catch (IndexOutOfBoundsException e) {
            item = null;
        }
        return item;
    }

    /**
     * 添加一个条目
     *
     * @param item 要添加的数据
     */
    public void addItem(T item) {
        mData.add(item);
        notifyItemInserted(mData.size() - 1);
    }

    /**
     * 添加一个条目到position位置
     *
     * @param position 指定的位置
     * @param item     要添加的数据
     */
    public void addItem(int position, T item) {
        mData.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * 添加多个条目
     *
     * @param collection 要添加的数据集合
     */
    public void addItemAll(Collection<? extends T> collection) {
        int oldSize = mData.size();
        mData.addAll(collection);
        notifyItemRangeInserted(oldSize, collection.size());
    }

    /**
     * 添加多个条目到position位置
     *
     * @param position   指定的位置
     * @param collection 要添加的数据集合
     */
    public void addItemAll(int position, Collection<? extends T> collection) {
        mData.addAll(position, collection);
        notifyItemRangeInserted(position, collection.size());
    }

    /**
     * 删除一个条目
     * <p>为了避免notifyItemRemoved后导致position错乱,所以再调用notifyItemRangeChanged刷新position</p>
     *
     * @param position 要删除的位置
     */
    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
    }


    /**
     * 删除多个条目
     *
     * @param collection 要删除的数据集合
     */
    public void removeItemAll(Collection<? extends T> collection) {
        mData.removeAll(collection);
        notifyDataSetChanged();
    }

    /**
     * 清空适配器数据
     */
    public void clear() {
        mData.clear();
        //notifyDataSetChanged();
        notifyItemRangeRemoved(0, mData.size());
    }


    public List<T> getData() {
        return mData;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mItemLongClickListener = onItemLongClickListener;
    }


    /**
     * 条目点击
     */
    public interface OnItemClickListener {
        void onItemClick(BaseRecyclerAdapter adapter, View view, int position);
    }

    /**
     * 条目长按
     */
    public interface OnItemLongClickListener {
        boolean onItemLongClick(BaseRecyclerAdapter adapter, View view, int position);
    }


    private static class OnItemClickListenerImpl implements View.OnClickListener {
        private BaseRecyclerAdapter mAdapter;
        private int mPosition;

        public OnItemClickListenerImpl(BaseRecyclerAdapter adapter, int position) {
            this.mAdapter = adapter;
            this.mPosition = position;
        }

        @Override
        public void onClick(View v) {
            mAdapter.mOnItemClickListener.onItemClick(mAdapter, v, mPosition);
        }
    }

    private static class OnItemLongClickListenerImpl implements View.OnLongClickListener {
        private BaseRecyclerAdapter mAdapter;
        private int mPosition;

        public OnItemLongClickListenerImpl(BaseRecyclerAdapter adapter, int position) {
            this.mAdapter = adapter;
            this.mPosition = position;
        }

        @Override
        public boolean onLongClick(View v) {
            return mAdapter.mItemLongClickListener.onItemLongClick(mAdapter, v, mPosition);
        }
    }
}