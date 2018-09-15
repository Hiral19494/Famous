package ca.android.famous.data;

import android.content.Context;

import java.util.ArrayList;

import ca.android.famous.data.remote.APIClient;
import ca.android.famous.model.Product;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class DataManger {
    private static DataManger ourInstance;
    public Context context;

    public static DataManger getInstance(Context context) {

        if (ourInstance == null) {
            ourInstance = new DataManger(context);
        }
        return ourInstance;
    }

    private DataManger(Context context) {
        this.context = context;
    }

    public Observable<ArrayList<Product>> getProductDetails() {



            return APIClient.getApiInstance().productDetails().concatMap(new Function<ArrayList<Product>, Observable<ArrayList<Product>>>() {
                @Override
                public Observable<ArrayList<Product>> apply(ArrayList<Product> products) throws Exception {

                    return Observable.just(products);
                }
            });
        }

    }

