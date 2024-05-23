package com.example.ypcloundmusic.component.feed.adapter;

import android.app.Activity;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.ypcloundmusic.R;
import com.example.ypcloundmusic.component.comment.model.Comment;
import com.example.ypcloundmusic.component.feed.model.Feed;
import com.example.ypcloundmusic.component.main.tab.fragments.FeedFragment;
import com.example.ypcloundmusic.component.song.model.User;
import com.example.ypcloundmusic.databinding.ItemFeedCommentBinding;
import com.example.ypcloundmusic.util.Constant;
import com.example.ypcloundmusic.util.DensityUtil;
import com.example.ypcloundmusic.util.ImageUtil;
import com.example.ypcloundmusic.util.PreferenceUtil;
import com.example.ypcloundmusic.util.SpannableStringBuilderUtil;
import com.example.ypcloundmusic.util.SuperDateUtil;
import com.example.ypcloundmusic.util.SuperTextUtil;
import com.google.android.gms.common.util.CollectionUtils;
import com.wanglu.photoviewerlibrary.PhotoViewer;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import superui.text.GridDividerItemDecoration;

public class FeedAdapter extends BaseQuickAdapter<Feed, BaseViewHolder> {

    private final FeedFragment fragment;

    public FeedAdapter(int layoutResId, FeedFragment fragment) {
        super(layoutResId);
        this.fragment = fragment;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, Feed data) {
        ImageUtil.showAvatar((Activity) getContext(), holder.getView(R.id.icon), data.getUser().getIcon());
        holder.setText(R.id.nickname, data.getUser().getNickname());
        holder.setText(R.id.date, SuperDateUtil.commonFormat(data.getCreatedAt()));

        holder.setText(R.id.content, data.getContent());

        //地理位置
        if (StringUtils.isNotBlank(data.getProvince())) {
            holder.setText(R.id.position, String.format("%s . %s", data.getCity(), data.getPosition()));
            holder.setGone(R.id.position, false);
        } else {
            holder.setGone(R.id.position, true);
        }
        if (data.getMedias() != null && data.getMedias().size() > 0) {
            //有图片

            //显示图片列表控件
            holder.setGone(R.id.list, false);

            RecyclerView listView = holder.getView(R.id.list);
            ImageAdapter adapter;
            //动态计算显示列数
            int spanCount = 1;
            if (data.getMedias().size() > 4) {
                //大于4张图片

                //显示3列
                spanCount = 3;
            } else if (data.getMedias().size() > 1) {
                //大于1张

                //显示2列
                spanCount = 2;
            }

            //设置布局管理器
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
            listView.setLayoutManager(layoutManager);

            //分割线
            if (listView.getItemDecorationCount() > 0) {
                listView.removeItemDecorationAt(0);
            }
            GridDividerItemDecoration itemDecoration = new GridDividerItemDecoration(getContext(), (int) DensityUtil.dip2px(getContext(), 5F));
            listView.addItemDecoration(itemDecoration);

            //设置适配器
            adapter = new ImageAdapter(R.layout.item_image);
            List<String> imageUrl = adapter.getImagesUrl();
            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    PhotoViewer.INSTANCE
                            .setData((ArrayList<String>) imageUrl)
                            .setCurrentPage(position)
                            .setImgContainer(listView)
                            .setShowImageViewInterface(new PhotoViewer.ShowImageViewInterface() {
                                @Override
                                public void show(@NonNull ImageView imageView, @NonNull String s) {
                                    Glide.with(listView.getContext()).load(s).into(imageView);
                                }
                            })
                            .start(fragment);
                    ;

                }
            });
            listView.setAdapter(adapter);
            listView.setTag(adapter);

            adapter.setNewInstance(data.getMedias());
        } else {
            holder.setGone(R.id.list, true);
        }

        //显示点赞状
        if (CollectionUtils.isEmpty(data.getLikes())) {
            //没有人点赞
            holder.setGone(R.id.like_container, true);
            holder.setGone(R.id.like_users, true);

            holder.setImageResource(R.id.like, R.drawable.thumb);
        } else {
            //有人点赞
            holder.setGone(R.id.like_container, false);
            holder.setGone(R.id.like_users, false);

            SuperTextUtil.setLinkColor(holder.getView(R.id.like_users), getContext().getColor(R.color.link));
            //显示内容
            holder.setText(R.id.like_users, processLikeUserContent(data.getLikes()));
            if (PreferenceUtil.getInstance(getContext()).isLogin() &&
                    data.getLikes().contains(new User(PreferenceUtil.getInstance(getContext()).getUserId()))) {
                //BaseId重写了，equals，hashCode所以这里才会直接根据id判断

                //我点赞了

                //设置图片
                holder.setImageResource(R.id.like, R.drawable.thumb_selected);
            } else {
                holder.setImageResource(R.id.like, R.drawable.thumb);
            }
        }

        //显示评论
        LinearLayoutCompat commentContentContainer = holder.getView(R.id.comment_content_container);
        commentContentContainer.removeAllViews();
        if (CollectionUtils.isEmpty(data.getComments())){
            //如果没有评论
            holder.setGone(R.id.comment_container, true);
        } else {
            holder.setGone(R.id.comment_container, false);
            for(Comment it : data.getComments()){
                ItemFeedCommentBinding commentBinding = ItemFeedCommentBinding.inflate(LayoutInflater.from(getContext()));
                SuperTextUtil.setLinkColor(commentBinding.getRoot(), getContext().getColor(R.color.link));
                commentBinding.content.setText(processContent(it));
                commentContentContainer.addView(commentBinding.getRoot());
            }

        }
    }

    /**
     * 处理评论内容
     * <p>
     * 普通评论显示格式：
     * 昵称：评论内容
     * 回复其他评论显示格式：
     * 当前评论用户昵称回复被回复评论：评论内容
     * <p>
     * 和微信朋友圈差不多
     *
     * @param data
     * @return
     */
    private SpannableStringBuilder processContent(Comment data) {
        //创建结果字符串
        SpannableStringBuilder result = new SpannableStringBuilder();

        //当前评论用户
        result.append(data.getUser().getNickname());

        //用户可以点击
        SpannableStringBuilderUtil.setUserClickSpan(result, 0, result.length(), data.getUser().getId());

        if (data.getParent() != null) {
            //有被回复的评论

            result.append(getContext().getString(R.string.reply));

            int start = result.length();

            //被回复的评论用户
            result.append(data.getParent().getUser().getNickname());

            SpannableStringBuilderUtil.setUserClickSpan(result, start, result.length(), data.getParent().getUser().getId());
        }

        //冒号
        result.append(getContext().getString(R.string.colon_separator));

        //评论内容
        result.append(data.getContent());

        return result;
    }


    //    详细内容参考富文本 yes https://blog.csdn.net/X_sunmmer/article/details/131301718
    private SpannableStringBuilder processLikeUserContent(List<User> data) {
        SpannableStringBuilder result = new SpannableStringBuilder();
        int start = 0;
        User d;

        for(int i = 0; i < data.size(); i++){
            d = data.get(i);
            result.append(d.getNickname());
            SpannableStringBuilderUtil.setUserClickSpan(result, start, result.length(), d.getId());

            if(i != data.size() - 1){
                //不是最后一个 加逗号
                result.append(Constant.SEPARATOR);
            }

            start = result.length();
        }
        return result;
    }
}
