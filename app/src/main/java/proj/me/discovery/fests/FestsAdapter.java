package proj.me.discovery.fests;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.DecimalFormat;
import java.util.List;

import proj.me.discovery.ActDicovery;
import proj.me.discovery.R;
import proj.me.discovery.views.CustomTextView;

/**
 * Created by root on 18/12/15.
 */
public class FestsAdapter extends RecyclerView.Adapter<FestsAdapter.ViewHolder>{

    private List<BeanFestivals> festivalsList;
    private DiscoveryCallback discoveryCallback;
    private Context context;
    private DecimalFormat df;

    private final static String path = "http://services-node12345js.rhcloud.com/uploads/";

    FestsAdapter(List<BeanFestivals> festivalsList, DiscoveryCallback discoveryCallback){
        this.festivalsList = festivalsList;
        this.discoveryCallback = discoveryCallback;
        df = new DecimalFormat("#.#");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View card = LayoutInflater.from(context).inflate(R.layout.cards, parent, false);
        ViewGroup.LayoutParams params = card.getLayoutParams();
        params.height = (int)ActDicovery.imageHeight;
        card.setLayoutParams(params);
        card.requestLayout();

        return new ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        BeanFestivals beanFestivals = festivalsList.get(position);
        holder.initFields(position);
        holder.festName.setText(beanFestivals.getFestName());
        holder.festPlace.setText(beanFestivals.getFestPlace());
        Picasso.with(context)
                .load(path+beanFestivals.getImageLink())
                .error(R.drawable.placeholder)
                //.placeholder(R.drawable.placeholder)
                .into(new Target() {

                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        holder.imgPro.setVisibility(View.INVISIBLE);
                        holder.festImage.setBackground(new BitmapDrawable(context.getResources(), bitmap));
                    }

                    @Override
                    public void onBitmapFailed(final Drawable errorDrawable) {
                        holder.imgPro.setVisibility(View.INVISIBLE);
                        holder.festImage.setBackground(errorDrawable);
                    }

                    @Override
                    public void onPrepareLoad(final Drawable placeHolderDrawable) {
                        holder.imgPro.setVisibility(View.VISIBLE);
                        holder.festImage.setBackground(null);
                    }
                });
        //holder.festImage.setBackground(context.getResources().getDrawable(beanFestivals.getImageDrawableId()));

        if(beanFestivals.isDoesLike()){
            holder.like.setImageResource(R.drawable.fill_heart);
            holder.playLikedAnim();
        }
        else holder.like.setImageResource(R.drawable.heart3);

        int count = beanFestivals.getLikeCount();
        float k = (float)count/1000f;
        holder.likeCount.setText(k >= 1f ? df.format(k) + "k" : "" +count);
    }

    @Override
    public int getItemCount() {
        return festivalsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView festImage, like;
        CustomTextView festPlace, festName, likeCount;
        ProgressBar imgPro;

        private BeanFestivals beanFestivals;

        private Animator likeAnim, likedAnim;
        private int position;
        void initFields(int position){
            this.beanFestivals = festivalsList.get(position);
            this.position = position;
        }

        void playLikedAnim(){
            likedAnim.start();
        }

        public ViewHolder(View itemView) {
            super(itemView);


            festImage=(ImageView)itemView.findViewById(R.id.festival_image);
            like=(ImageView)itemView.findViewById(R.id.like);
            festPlace=(CustomTextView)itemView.findViewById(R.id.festival_place);
            festName=(CustomTextView)itemView.findViewById(R.id.festival_name);
            likeCount = (CustomTextView)itemView.findViewById(R.id.like_count);
            imgPro = (ProgressBar)itemView.findViewById(R.id.img_pro);
            like.setOnClickListener(this);
            festImage.setOnClickListener(this);
            /*RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)festImage.getLayoutParams();
            params.height = (int)ActDicovery.imageHeight;
            festImage.setLayoutParams(params);
            festImage.requestLayout();*/


            likeAnim = AnimatorInflater.loadAnimator(context, R.anim.rotate_like);
            likedAnim = AnimatorInflater.loadAnimator(context, R.anim.rotate_liked);
            likeAnim.setTarget(like);
            likedAnim.setTarget(like);
            likeAnim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    like.setImageResource(R.drawable.fill_heart);
                    int count = beanFestivals.getLikeCount()+1;
                    float k = (float)count/1000f;
                    beanFestivals.setLikeCount(count);
                    likeCount.setText(k>=1f?df.format(k)+"k":""+count);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.like:
                    if(beanFestivals.isDoesLike())
                        likedAnim.start();
                    else {
                        beanFestivals.setDoesLike(true);
                        likeAnim.start();
                    }
                    break;
                case R.id.festival_image:
                    if(discoveryCallback == null) return;
                    discoveryCallback.performTransition(beanFestivals, position);
                    break;
            }

        }
    }
}
