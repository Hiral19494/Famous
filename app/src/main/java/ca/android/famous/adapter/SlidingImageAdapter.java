package ca.android.famous.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
        return 0;
    }

    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.sliding_image, view, false);

        assert imageLayout != null;
        ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_launcher_background);
        requestOptions.error(R.drawable.ic_launcher_foreground);
        Log.d("imge", String.valueOf(imageModelArrayList.size()));
        Toast.makeText(context, "size:"+imageModelArrayList.size(), Toast.LENGTH_SHORT).show();
        Glide.with(context)
                .load(imageModelArrayList.get(position))
                .apply(requestOptions)
                .into(imageView);
         imageView.setImageResource(Integer.parseInt(imageModelArrayList.get(position).getImage_drawable()));

        view.addView(imageLayout);

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