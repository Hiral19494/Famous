package ca.android.famous.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.ButterKnife;
import ca.android.famous.R;
import ca.android.famous.model.Product;

public class ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.ViewHolder> {

    private List<Product> productList;
    Context context;

    public ProductDetailsAdapter(List<Product> productList, Context applicationContext) {
        this.productList = productList;
        this.context = applicationContext;
    }

    @NonNull
    @Override
    public ProductDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_comment_adapter, parent, false);
        ButterKnife.bind(this, itemView);

        return new ProductDetailsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductDetailsAdapter.ViewHolder holder, int position) {
        Product productModel = productList.get(position);
        Log.d("PriceValue", productModel.getPrice().toString());
        holder.txvPorductPrice.setText("$" + productModel.getPrice());
        holder.txvUserName.setText(productModel.getName());
        holder.txvPostComment.setText(productModel.getDescription());
        if (productModel.getImages() != null) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.ic_launcher_background);
            requestOptions.error(R.drawable.ic_launcher_foreground);


            try {
                Glide.with(context).asBitmap().load(productModel.getImages().get(0))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)
                                .dontTransform().dontAnimate()
                                .override(90,90))
                        .into(holder.imgProduct);
               // Glide.with(context).setDefaultRequestOptions(requestOptions).load((productModel.getImages().get(1))).into(holder.imgProduct);;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void removeItem(int position) {
        productList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txvPorductPrice;
        TextView txvUserName;
        TextView txvPostComment;
        ImageView imgProduct;
        public RelativeLayout viewBackground, viewForeground;

        public ViewHolder(View itemView) {
            super(itemView);
            txvPorductPrice = itemView.findViewById(R.id.txv_Product_price);
            txvUserName = itemView.findViewById(R.id.txv_user_name);
            imgProduct = itemView.findViewById(R.id.img_product);
            txvPostComment = itemView.findViewById(R.id.txv_post_comment);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
        }
    }
}


