package ca.android.famous;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ca.android.famous.adapter.ProductDetailsAdapter;
import ca.android.famous.clickListener.RecyclerItemTouchHelper;
import ca.android.famous.clickListener.RecyclerviewClickListener;
import ca.android.famous.data.DataManger;
import ca.android.famous.dividerdecoration.MyDividerItemDecoration;
import ca.android.famous.fcm.SmsHelper;
import ca.android.famous.model.Product;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProductActivity extends BaseActivity {
    @BindView(R.id.rsv_products)
    public RecyclerView rsvProduct;
    @BindView(R.id.fab_sms)
    FloatingActionButton fabSms;
    private ArrayList<Product> productList = new ArrayList<>();
    private ProductDetailsAdapter productDetailsAdapter;
    public static final String LAYOUTTILE = "ProductListFragment";
    IntentFilter intentFilter;
    private static final int SMS_PERMISSION_CODE = 0;
    public EditText edtNumber;
    public EditText edtMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_screen);
        ButterKnife.bind(this);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(LAYOUTTILE);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rsvProduct.setLayoutManager(mLayoutManager);
        rsvProduct.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));

        apicallproduct();
        productDetailsAdapter = new ProductDetailsAdapter(productList, getApplicationContext());
        rsvProduct.setAdapter(productDetailsAdapter);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rsvProduct);
        rsvProduct.addOnItemTouchListener(
                new RecyclerviewClickListener(getApplicationContext(), new RecyclerviewClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click


                        Intent postidIntent = new Intent(getApplicationContext(), ProductDescriptionActivity.class);
                        postidIntent.putExtra(ProductDescriptionActivity.PRODUCT, productList.get(position));
                        startActivity(postidIntent);
                    }
                })
        );
    }

    private void apicallproduct() {
        DataManger.getInstance(getApplicationContext())
                .getProductDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<ArrayList<Product>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("subscribe", "subscribe");
                    }

                    @Override
                    public void onNext(ArrayList<Product> products) {
                        productList.addAll(products);
                        productDetailsAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onError(Throwable e) {
                        // Log.d("error", e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d("complete", "complete");
                    }
                });
    }

    @SuppressLint("ResourceAsColor")
    @OnClick(R.id.fab_sms)
    public void onclick() {
        final Dialog dialog = new Dialog(ProductActivity.this);
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
                Toast.makeText(getApplicationContext(), R.string.toast_sending_sms, Toast.LENGTH_SHORT).show();
            }
        });

        // show dialog on screen
        dialog.show();

    }

}
