package ca.android.famous.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ca.android.famous.R;
import ca.android.famous.WebViewActivity;
import ca.android.famous.adapter.SlidingImageAdapter;
import ca.android.famous.model.ImageModel;
import ca.android.famous.model.Product;

import static ca.android.famous.WebViewActivity.WEBDATA;

public class ProductDetailsFragment extends Fragment {
    private static final String PRODUCT = "product";
    @BindView(R.id.pager)
    ViewPager viewPagerProduct;
    Product product;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;

    @BindView(R.id.txv_product_name)
    TextView txvProductName;
    @BindView(R.id.txv_phone)
    TextView txvPhone;
    @BindView(R.id.txv_web)
    TextView txvWeb;
    @BindView(R.id.txv_price)
    TextView txvPrice;
    @BindView(R.id.txv_description)
    TextView txvDescription;
    @BindView (R.id.indicator)
    CirclePageIndicator indicatorCircle;


    public static ProductDetailsFragment newInstance(Product product) {
        ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(PRODUCT, product);
        productDetailsFragment.setArguments(args);
        return productDetailsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.fragment_product_details, parent, false);
        ButterKnife.bind(this, view);
        product = (Product) getArguments().get(PRODUCT);
        getActivity().setTitle("Product Description");
        //final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);

        txvProductName.setText(product.getName());
        txvPhone.setText(product.getPhone());
        txvWeb.setText(product.getWeb());
        txvDescription.setText(product.getDescription());
        txvPrice.setText("CAD$" + String.valueOf(product.getPrice()));

        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = imageList();

        viewPagerProduct.setAdapter(new SlidingImageAdapter(getActivity(), imageModelArrayList));


        indicatorCircle.setViewPager(viewPagerProduct);
        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicatorCircle.setRadius(5 * density);

        NUM_PAGES = imageModelArrayList.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPagerProduct.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        indicatorCircle.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

        return view;
    }

    private void imageSlide() {


        // Pager listener over indicator
    }
    private ArrayList<ImageModel> imageList() {
        ArrayList<ImageModel> list = new ArrayList<>();
        if (product.getImages() != null) {
            for (int i = 0; i < product.getImages().size() ;i++) {
                ImageModel imageModel = new ImageModel();
                imageModel.setImage_drawable((product.getImages().get(i)));
                list.add(imageModel);
            }
        }
        Log.d("list", String.valueOf(list));
        Toast.makeText(getContext(),"imagee"+list,Toast.LENGTH_LONG).show();
        return list;
    }
    @OnClick(R.id.txv_web)
    public void onClickWeb() {
        Intent webdataIntent = new Intent(getActivity(), WebViewActivity.class);
        webdataIntent.putExtra(WEBDATA, product);
        startActivity(webdataIntent);
    }

    @OnClick(R.id.txv_phone)
    public void onClickPhone() {
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
        phoneIntent.setData(Uri.parse("tel:" + product.getPhone()));
        startActivity(phoneIntent);

    }

}



