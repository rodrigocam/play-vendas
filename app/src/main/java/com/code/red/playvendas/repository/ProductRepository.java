package com.code.red.playvendas.repository;

import android.arch.lifecycle.LiveData;

import com.code.red.playvendas.dao.ProductDao;
import com.code.red.playvendas.model.Product;
import com.code.red.playvendas.utils.Webservice;

import java.io.IOException;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Response;


@Singleton
public class UserRepository {
    private final Webservice webservice;
    private final ProductDao productDao;
    private final Executor executor;

    @Inject
    public UserRepository(Webservice webservice, ProductDao productDao, Executor executor) {
        this.webservice = webservice;
        this.productDao = productDao;
        this.executor = executor;
    }

    public LiveData<Product> getUser(int productId) {
        refreshUser(productId);
        // return a LiveData directly from the database.
        return productDao.load(productId);
    }

    private void refreshUser(final int userId) {
        executor.execute(() -> {
            // running in a background thread
            // check if user was fetched recently

            // TODO: Verify what the hell this was.
            //boolean userExists = productDao.hasUser(FRESH_TIMEOUT);
            boolean userExists = true;
            if (!userExists) {
                // refresh the data

                Response response;
                try {
                    response = webservice.getUser(userId).execute();
                }catch (IOException e){
                    //TODO: Verify what this IOException means here;
                    e.printStackTrace();
                }

                try{
                    productDao.save((Product)response.body());
                }catch (Exception e){

                }

                // TODO check for error etc.
                // Update the database.The LiveData will automatically refresh so
                // we don't need to do anything else here besides updating the database
                // TODO: Product conversion done  without checks, verify this
            }
        });
    }
}