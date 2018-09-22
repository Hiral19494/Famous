package ca.android.famous.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import ca.android.famous.fcm.SmsHelper;
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
    public EditText edtNumber;
    public EditText edtMsg;

    @BindView(R.id.txv_product_weight)
    TextView txvProductWeight;
    @BindView(R.id.txv_product_dimension)
    TextView txvProductDimension;
    @BindView(R.id.txv_product_price)
    TextView txvProductPrice;
    @BindView(R.id.txv_product_name)
    TextView txvProductName;
    @BindView(R.id.txv_product_phone)
    TextView txvPhone;
    @BindView(R.id.txv_product_web)
    TextView txvWeb;
    @BindView(R.id.txv_product_desscription)
    TextView txvProductDescription;
    @BindView(R.id.indicator)
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
        //((AppCompatActivity)getActivity()).setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        product = (Product) getArguments().get(PRODUCT);
        getActivity().setTitle("Product Description");

        txvProductName.setText(product.getName());
       // txvPhone.setText(product.getPhone());
        //txvWeb.setText(product.getWeb());
        txvProductDescription.setText("\u2022 " + product.getDescription());
        txvProductPrice.setText("CAD$ " + String.valueOf(product.getPrice()));
        txvProductWeight.setText(product.getWeight() );
        txvProductDimension.setText(product.getDimensions().getLength() +
                " * " + product.getDimensions().getWidth() +
                " * " + product.getDimensions().getHeight());


        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = imageList();
        imageSlide();


        return view;
    }

    private void imageSlide() {

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
        // Pager listener over indicator
    }

    private ArrayList<ImageModel> imageList() {
        ArrayList<ImageModel> list = new ArrayList<>();
        if (product.getImages() != null) {
            for (int i = 0; i < product.getImages().size(); i++) {
                ImageModel imageModel = new ImageModel();
                imageModel.setImage_drawable((product.getImages().get(i)));
                list.add(imageModel);
            }
        }
        Log.d("list", String.valueOf(list));
        return list;
    }

    @OnClick(R.id.txv_product_web)
    public void onClickWeb() {
        Intent webdataIntent = new Intent(getActivity(), WebViewActivity.class);
        webdataIntent.putExtra(WEBDATA, product);
        startActivity(webdataIntent);
    }

    @OnClick(R.id.txv_product_phone)
    public void onClickPhone() {
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
        phoneIntent.setData(Uri.parse("tel:" + product.getPhone()));
        startActivity(phoneIntent);

    }
    @SuppressLint("ResourceAsColor")
    @OnClick(R.id.fab_sms)
    public void onclick() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_sms_box);
        final TextView textView = (TextView) dialog.findViewById(android.R.id.title);
        if (textView != null) {
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setTextColor(R.color.primary_darker_login);
        }
        textView.setText("SMS ");
        edtNumber = (EditText) dialog.findViewById(R.id.edt_number);
        edtMsg = (EditText) dialog.findViewById(R.id.edt_msg);
        dialog.findViewById(R.id.btn_normal_sms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SmsHelper.sendDebugSms(edtNumber.getText().toString(), edtMsg.getText().toString());
                Toast.makeText(getContext(), R.string.toast_sending_sms, Toast.LENGTH_SHORT).show();
            }
        });

        // show dialog on screen
        dialog.show();

    }
}



