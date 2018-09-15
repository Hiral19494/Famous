
package ca.android.famous.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product implements Parcelable {

    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("images")
    @Expose
    private List<String> images = null;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("web")
    @Expose
    private String web;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("tags")
    @Expose
    private List<String> tags = null;
    @SerializedName("dimensions")
    @Expose
    private Dimensions dimensions;
    @SerializedName("warehouseLocation")
    @Expose
    private WarehouseLocation warehouseLocation;


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    public WarehouseLocation getWarehouseLocation() {
        return warehouseLocation;
    }

    public void setWarehouseLocation(WarehouseLocation warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productId);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.weight);
        dest.writeStringList(this.images);
        dest.writeString(this.phone);
        dest.writeString(this.web);
        dest.writeValue(this.price);
        dest.writeStringList(this.tags);
        dest.writeParcelable(this.dimensions, flags);
        dest.writeParcelable(this.warehouseLocation, flags);
    }

    public Product() {
    }

    public Product(Parcel in) {
        this.productId = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.weight = in.readString();
        this.images = in.createStringArrayList();
        this.phone = in.readString();
        this.web = in.readString();
        this.price = (Integer) in.readValue(Integer.class.getClassLoader());
        this.tags = in.createStringArrayList();
        this.dimensions = in.readParcelable(Dimensions.class.getClassLoader());
        this.warehouseLocation = in.readParcelable(WarehouseLocation.class.getClassLoader());
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
