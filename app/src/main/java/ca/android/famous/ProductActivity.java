package ca.android.famous;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.android.famous.adapter.ProductDetailsAdapter;
import ca.android.famous.clickListener.RecyclerItemTouchHelper;
import ca.android.famous.clickListener.RecyclerviewClickListener;
import ca.android.famous.data.DataManger;
import ca.android.famous.dividerdecoration.MyDividerItemDecoration;
import ca.android.famous.model.Product;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProductActivity extends BaseActivity {
    @BindView(R.id.rsv_products)
    public RecyclerView rsvProduct;
    private ArrayList<Product> productList = new ArrayList<>();
    private ProductDetailsAdapter productDetailsAdapter;
    public static final String LAYOUTTILE = "ProductListFragment";
    IntentFilter intentFilter;
    private static final int SMS_PERMISSION_CODE = 0;


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



}
