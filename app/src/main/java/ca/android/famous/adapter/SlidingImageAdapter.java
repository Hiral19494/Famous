package ca.android.famous.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import ca.android.famous.R;
import ca.android.famous.model.ImageModel;

public class SlidingImageAdapter extends PagerAdapter {


    private ArrayList<ImageModel> imageModelArrayList;
    private LayoutInflater inflater;
    private Context context;


    public SlidingImageAdapter(Context context, ArrayList<ImageModel> imageModelArrayList) {
        this.context = context;
        this.imageModelArrayList = imageModelArrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageModelArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.sliding_image, view, false);

        assert imageLayout != null;
        ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image_slide);
        try {
            Glide.with(context).asBitmap().load(imageModelArrayList.get(position).getImage_drawable())
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                            .dontTransform().dontAnimate()
                            .override(150, 150))
                    .into(imageView);
        }catch (Exception e){
            e.printStackTrace();
        }


        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}