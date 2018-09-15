package ca.android.famous;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.android.famous.fragment.MapFragment;
import ca.android.famous.fragment.ProductDetailsFragment;
import ca.android.famous.model.Product;

public class ProductDescriptionActivity  extends BaseActivity{

    public static final String PRODUCT = "product";
    @BindView(R.id.view_pager)
    ViewPager viewPagerProduct;
    ProductPageAdapter productPageAdapter;

    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_des_screen);
        ButterKnife.bind(this);
        product = getIntent().getParcelableExtra(PRODUCT);
        productPageAdapter = new ProductPageAdapter(getSupportFragmentManager());
        viewPagerProduct.setAdapter(productPageAdapter);
        Log.d("name", product.getName());
        ActivityCompat.requestPermissions(ProductDescriptionActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                1);
    }

    public class ProductPageAdapter extends FragmentStatePagerAdapter {

        public ProductPageAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ProductDetailsFragment.newInstance(product);

                case 1:
                    return new MapFragment().newInstance(product);


            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            if (position == 0) {
                return "Details";
            } else {
                return "Map";
            }
        }
    }

}


